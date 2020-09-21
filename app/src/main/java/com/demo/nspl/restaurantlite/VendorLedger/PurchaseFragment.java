package com.demo.nspl.restaurantlite.VendorLedger;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.AsyncTaskReport.PurchaseVendorAsyncTask;
import com.demo.nspl.restaurantlite.Purchase.ClsPurchaseDetail;
import com.demo.nspl.restaurantlite.Purchase.DisplayItemAdapter;
import com.demo.nspl.restaurantlite.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class PurchaseFragment extends Fragment {


    RecyclerView rv;
    TextView txt_nodata;
    LinearLayout ll_header;
    int vendorId = 0;
    String vendorName = "";
    BottomSheetDialog dialog;
    List<ClsPurchaseDetail> lstClsPurchaseDetails;
    ProgressBar progress_bar;


    public void setVendor(int vendorId, String vendorName) {
        this.vendorId = vendorId;
        this.vendorName = vendorName;

        Log.d("--VendorPurchase--", "setVendor: " + vendorId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.vendor_ledger_tab_list, container, false);

        main(v);

        return v;

    }

    private void main(View v) {
        rv = v.findViewById(R.id.rv);
        ll_header = v.findViewById(R.id.ll_header);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        txt_nodata = v.findViewById(R.id.txt_nodata);

        progress_bar = v.findViewById(R.id.progress_bar);


        clickOnAdapter();
        getPurchaseDetails();

    }

    private VendorPurchaseAdapter vendorPurchaseAdapter;

    private void getPurchaseDetails() {

        String _where = " AND V.[VENDOR_ID] =".concat(String.valueOf(vendorId));


        new PurchaseVendorAsyncTask(_where, txt_nodata, getActivity(), vendorPurchaseAdapter,
                progress_bar, rv).execute();


    }

    private void clickOnAdapter() {
        vendorPurchaseAdapter = new VendorPurchaseAdapter(getActivity());

        vendorPurchaseAdapter.SetOnClickListener((clsPurchaseMaster, position)
                -> openBottomDialog(clsPurchaseMaster.getPurchaseID()));
        rv.setAdapter(vendorPurchaseAdapter);
    }

    private void openBottomDialog(int purchaseID) {
        View view = getLayoutInflater().inflate(R.layout.bottom_dialog, null);
        dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(view);
        dialog.show();


        RecyclerView rv_list = dialog.findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        TextView txt_no_data = dialog.findViewById(R.id.txt_no_data);
        TextView txt_item_list = dialog.findViewById(R.id.txt_item_list);
        TextView txt_total_amt = dialog.findViewById(R.id.txt_total_amt);


        lstClsPurchaseDetails = new ArrayList<>();
        lstClsPurchaseDetails = new ClsPurchaseDetail(getActivity()).getPurchaseItemList(" AND PD.[PurchaseID] =".concat(String.valueOf(purchaseID)), getActivity());
        txt_item_list.setText("Purchase Item List (" + lstClsPurchaseDetails.size() + ")");


        if (lstClsPurchaseDetails != null && lstClsPurchaseDetails.size() != 0) {

            txt_no_data.setVisibility(View.GONE);
            rv_list.setVisibility(View.VISIBLE);

            DisplayItemAdapter displayItemAdapter = new DisplayItemAdapter(getActivity(), lstClsPurchaseDetails);
            rv_list.setAdapter(displayItemAdapter);
            displayItemAdapter.notifyDataSetChanged();

        } else {
            txt_no_data.setVisibility(View.VISIBLE);
            rv_list.setVisibility(View.GONE);
        }

    }

}
