package com.demo.nspl.restaurantlite.AsyncTaskReport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Purchase.ClsPurchaseDetail;
import com.demo.nspl.restaurantlite.Stock.PurchaseStockAdapter;

import java.util.List;

public class PurchaseStockReportAsyncTask extends AsyncTask<Void, Void, List<ClsPurchaseDetail>> {


    private String _where = "";


    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private TextView txt;
    private PurchaseStockAdapter purchaseStockAdapter;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    private RecyclerView recyclerView;


    public PurchaseStockReportAsyncTask(String _where, TextView txt,
                                        Context context,
                                        PurchaseStockAdapter purchaseStockAdapter,
                                        ProgressBar progressBar,
                                        RecyclerView recyclerView) {
        this._where = _where;
        this.txt = txt;
        this.context = context;
        this.purchaseStockAdapter = purchaseStockAdapter;
        this.progressBar = progressBar;
        this.recyclerView = recyclerView;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected List<ClsPurchaseDetail> doInBackground(Void... voids) {
        return new ClsPurchaseDetail().getPurchaseItemListItemWise(_where, context);
    }


    @Override
    protected void onPostExecute(List<ClsPurchaseDetail> latClsPurchaseDetails) {
        super.onPostExecute(latClsPurchaseDetails);

        progressBar.setVisibility(View.GONE);

        if (latClsPurchaseDetails != null && latClsPurchaseDetails.size() != 0) {
            txt.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            purchaseStockAdapter.AddItems(latClsPurchaseDetails);
        } else {
            txt.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }


    }

}
