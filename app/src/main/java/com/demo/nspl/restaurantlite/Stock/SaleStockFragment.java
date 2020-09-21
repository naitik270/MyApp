package com.demo.nspl.restaurantlite.Stock;

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

import com.demo.nspl.restaurantlite.AsyncTaskReport.SaleStockReportAsyncTask;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class SaleStockFragment extends Fragment {


    RecyclerView rv;
    TextView txt_nodata;
    String itemCode = "";
    ProgressBar progress_bar;
    FloatingActionButton fab_filter;
    List<ClsStockSale> lstClsStockSales = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.vendor_ledger_tab_list, container, false);
        ClsGlobal.isFristFragment = true;
        main(v);
        return v;
    }

    public void setVendor(String itemCode) {
        this.itemCode = itemCode;
    }

    private void main(View v) {
        rv = v.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        txt_nodata = v.findViewById(R.id.txt_nodata);

        progress_bar = v.findViewById(R.id.progress_bar);
        fab_filter = v.findViewById(R.id.fab_filter);
        fab_filter.setVisibility(View.GONE);
        fab_filter.setColorFilter(Color.WHITE);

        getStockSaleList();

    }

    private void getStockSaleList() {

        lstClsStockSales = new ArrayList<>();

        String where = " AND [dtl].[ItemCode] = ".concat("'").concat(itemCode).concat("'");

        SaleStockAdapter saleStockAdapter = new SaleStockAdapter(getActivity());

        new SaleStockReportAsyncTask(where, txt_nodata,
                getActivity(), saleStockAdapter, progress_bar, rv).execute();

        rv.setAdapter(saleStockAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getStockSaleList();
    }

    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
    }
}
