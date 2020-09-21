package com.demo.nspl.restaurantlite.Stock;

import android.graphics.Color;
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

import com.demo.nspl.restaurantlite.AsyncTaskReport.PurchaseStockReportAsyncTask;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Purchase.ClsPurchaseDetail;
import com.demo.nspl.restaurantlite.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PurchaseStockFragment extends Fragment {


    RecyclerView rv;
    TextView txt_nodata;
    LinearLayout ll_header;
    List<ClsPurchaseDetail> lstClsPurchaseDetails = new ArrayList<>();
    String itemCode;
    ProgressBar progress_bar;
    FloatingActionButton fab_filter;


    public void setVendor(String itemCode) {
        this.itemCode = itemCode;
        Log.e("--ItemList--", "itemCode- " + itemCode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.vendor_ledger_tab_list,
                container, false);
        ClsGlobal.isFristFragment = true;
        main(v);
        return v;
    }

    private void main(View v) {

        rv = v.findViewById(R.id.rv);
        ll_header = v.findViewById(R.id.ll_header);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        txt_nodata = v.findViewById(R.id.txt_nodata);
        progress_bar = v.findViewById(R.id.progress_bar);
        fab_filter = v.findViewById(R.id.fab_filter);
        fab_filter.setVisibility(View.GONE);
        fab_filter.setColorFilter(Color.WHITE);

        getPurchaseDetails();
    }


    private void getPurchaseDetails() {

        String where = " AND PD.[ItemCode] =".concat("'" + itemCode + "'");


        Log.e("--StockPurchase--", "where: " + where);

        PurchaseStockAdapter purchaseStockAdapter = new PurchaseStockAdapter(getActivity());

        new PurchaseStockReportAsyncTask(where, txt_nodata,
                getActivity(), purchaseStockAdapter, progress_bar, rv).execute();

        rv.setAdapter(purchaseStockAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        getPurchaseDetails();
    }

    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
    }

}
