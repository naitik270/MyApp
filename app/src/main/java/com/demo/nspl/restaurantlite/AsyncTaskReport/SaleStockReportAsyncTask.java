package com.demo.nspl.restaurantlite.AsyncTaskReport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Stock.ClsStockSale;
import com.demo.nspl.restaurantlite.Stock.SaleStockAdapter;

import java.util.List;

public class SaleStockReportAsyncTask extends AsyncTask<Void, Void, List<ClsStockSale>> {


    private String _where = "";


    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private TextView txt;
    private SaleStockAdapter stockAdapter;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    private RecyclerView recyclerView;


    public SaleStockReportAsyncTask(String _where, TextView txt,
                                    Context context,
                                    SaleStockAdapter stockAdapter,
                                    ProgressBar progressBar,
                                    RecyclerView recyclerView) {
        this._where = _where;
        this.txt = txt;
        this.context = context;
        this.stockAdapter = stockAdapter;
        this.progressBar = progressBar;
        this.recyclerView = recyclerView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected List<ClsStockSale> doInBackground(Void... voids) {
        return new ClsStockSale().getStockSaleList(_where, context);
    }

    @Override
    protected void onPostExecute(List<ClsStockSale> lstClsStockSales) {
        super.onPostExecute(lstClsStockSales);
        progressBar.setVisibility(View.GONE);

        if (lstClsStockSales != null && lstClsStockSales.size() != 0) {
            txt.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            stockAdapter.AddItems(lstClsStockSales);
        } else {
            txt.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

    }

}
