package com.demo.nspl.restaurantlite.Navigation_Drawer;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.PurchaseDetailReportAdapter;
import com.demo.nspl.restaurantlite.AsyncTaskReport.PurchaseReportAsyncTask;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.VendorLedger.ClsVendorLedger;
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class PurchaseDetailFragment extends Fragment {

    RecyclerView rv;
    TextView txt_nodata;
    LinearLayout ll_header;
    List<ClsPaymentMaster> list = new ArrayList<>();
    PurchaseDetailReportAdapter purchaseDetailReportAdapter;
    ClsVendorLedger clsVendorLedger;
    ProgressBar progress_bar;
    FloatingActionButton fab_filter;

    public PurchaseDetailFragment() {
        // Required empty public constructor
    }

    public void SendObj(ClsVendorLedger clsVendorLedger) {
        this.clsVendorLedger = clsVendorLedger;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_detail, container, false);

        rv = view.findViewById(R.id.rv);
        txt_nodata = view.findViewById(R.id.txt_nodata);
        ll_header = view.findViewById(R.id.ll_header);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        progress_bar = view.findViewById(R.id.progress_bar);
        fab_filter = view.findViewById(R.id.fab_filter);
        fab_filter.setVisibility(View.GONE);
        fab_filter.setColorFilter(Color.WHITE);

        ViewData();

        return view;
    }


    private void ViewData() {

        String where = "".concat(" AND [MobileNo] = ").concat("'")
                .concat(clsVendorLedger.getCustomerMobileNo()).concat("'")
                .concat(" AND IFNULL([Type] ,'Customer') = 'Customer' ");

        purchaseDetailReportAdapter = new PurchaseDetailReportAdapter(getActivity());

        new PurchaseReportAsyncTask(where, "CustomerPayment", txt_nodata,
                getActivity(), purchaseDetailReportAdapter, progress_bar, rv).execute();

        rv.setAdapter(purchaseDetailReportAdapter);
    }
}
