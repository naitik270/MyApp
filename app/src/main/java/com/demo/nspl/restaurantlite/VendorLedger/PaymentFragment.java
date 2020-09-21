package com.demo.nspl.restaurantlite.VendorLedger;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.AsyncTaskReport.PaymentVendorReportAsyncTask;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class PaymentFragment extends Fragment {


    RecyclerView rv;
    TextView txt_nodata;
    int vendorId;
    String vendorName;
    ProgressBar progress_bar;
    FloatingActionButton fab_filter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.vendor_ledger_tab_list, container, false);
        ClsGlobal.isFristFragment = true;

        main(v);

        return v;

    }


    public void setVendor(int vendorId, String vendorName) {
        this.vendorId = vendorId;
        this.vendorName = vendorName;

    }

    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
    }

    private void main(View v) {
        rv = v.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        txt_nodata = v.findViewById(R.id.txt_nodata);

        progress_bar = v.findViewById(R.id.progress_bar);
        fab_filter = v.findViewById(R.id.fab_filter);
        fab_filter.setVisibility(View.GONE);
        fab_filter.setColorFilter(Color.WHITE);

        getPaymentDetails();

    }


    @Override
    public void onResume() {
        super.onResume();
        getPaymentDetails();
    }


    private void getPaymentDetails() {

        String where = " AND [VendorID] =".concat(String.valueOf(vendorId));

        VendorPaymentAdapter vendorPaymentAdapter = new VendorPaymentAdapter(getActivity());

        new PaymentVendorReportAsyncTask(where, txt_nodata,
                getActivity(), vendorPaymentAdapter, progress_bar, rv).execute();

        rv.setAdapter(vendorPaymentAdapter);
    }


}
