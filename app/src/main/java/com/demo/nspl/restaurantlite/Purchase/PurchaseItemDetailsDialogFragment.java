package com.demo.nspl.restaurantlite.Purchase;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class PurchaseItemDetailsDialogFragment extends DialogFragment {

    private int PurchaseID;
    private double _total = 0.0;
    private double _totalTax= 0.0;
    RecyclerView rv_list;
    TextView txt_nodata;
    TextView txt_total_amt;
    TextView txt_total_tax;
    ImageButton btn_done;

    List<ClsPurchaseDetail> lstClsPurchaseDetails;

    public CallbackResult callbackResult;

    public PurchaseItemDetailsDialogFragment() {
        // Required empty public constructor
    }

    public void setID(int PurchaseID) {
        this.PurchaseID = PurchaseID;
    }

    public void setTotal(double _total,double _totalTax) {
        this._total = _total;
        this._totalTax = _totalTax;
    }

    public void setOnCallbackResult(final CallbackResult callbackResult) {
        this.callbackResult = callbackResult;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_purchase_item_details, container, false);
        main(view);
        return view;
    }

    private void main(View view) {

        txt_total_tax = view.findViewById(R.id.txt_total_tax);
        txt_total_amt = view.findViewById(R.id.txt_total_amt);
        btn_done = view.findViewById(R.id.btn_done);
        rv_list = view.findViewById(R.id.rv_list);
        txt_nodata = view.findViewById(R.id.txt_nodata);
        rv_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        viewItemList();
    }

    public interface CallbackResult {
        void sendResult(int requestCode);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewItemList();
    }

    private void viewItemList() {

        lstClsPurchaseDetails = new ArrayList<>();
        lstClsPurchaseDetails = new ClsPurchaseDetail(getActivity()).getPurchaseItemList(" AND PD.[PurchaseID] =".concat(String.valueOf(PurchaseID)), getActivity());

        Gson gson = new Gson();
        String jsonInString = gson.toJson(lstClsPurchaseDetails);
        Log.e("--ItemList--", "Item- " + jsonInString);

        txt_total_amt.setText("TOTAL AMOUNT: \u20B9 " + _total);
        txt_total_tax.setText("TOTAL TAX: \u20B9 " + _totalTax);

        if (lstClsPurchaseDetails != null && lstClsPurchaseDetails.size() != 0) {
            txt_nodata.setVisibility(View.GONE);
            rv_list.setVisibility(View.VISIBLE);

            DisplayItemAdapter displayItemAdapter = new DisplayItemAdapter(getActivity(), lstClsPurchaseDetails);
            rv_list.setAdapter(displayItemAdapter);
            displayItemAdapter.notifyDataSetChanged();

        } else {
            txt_nodata.setVisibility(View.VISIBLE);
            rv_list.setVisibility(View.GONE);
        }
    }

}
