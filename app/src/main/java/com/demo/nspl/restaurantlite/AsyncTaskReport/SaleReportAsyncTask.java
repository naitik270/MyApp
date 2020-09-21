package com.demo.nspl.restaurantlite.AsyncTaskReport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.SalesDetailAdapter;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster;

import java.util.List;

public class SaleReportAsyncTask extends AsyncTask<Void, Void, List<ClsInventoryOrderMaster>> {


    private String _where = "";
    private String mode = "";

    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private TextView txt;
    private SalesDetailAdapter salesDetailAdapter;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    private RecyclerView recyclerView;


    public SaleReportAsyncTask(String _where, String mode, TextView txt,
                               Context context,
                               SalesDetailAdapter salesDetailAdapter,
                               ProgressBar progressBar,
                               RecyclerView recyclerView) {
        this._where = _where;
        this.mode = mode;
        this.txt = txt;
        this.context = context;
        this.salesDetailAdapter = salesDetailAdapter;
        this.progressBar = progressBar;
        this.recyclerView = recyclerView;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected List<ClsInventoryOrderMaster> doInBackground(Void... voids) {
        return new ClsInventoryOrderMaster().getCustomerSalesList(_where, context);

    }


    @Override
    protected void onPostExecute(List<ClsInventoryOrderMaster> lstClsInventoryOrderMasters) {
        super.onPostExecute(lstClsInventoryOrderMasters);

        progressBar.setVisibility(View.GONE);

        if (lstClsInventoryOrderMasters != null && lstClsInventoryOrderMasters.size() != 0) {
            txt.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            salesDetailAdapter.AddItems(lstClsInventoryOrderMasters);
        } else {
            txt.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }


    }

}
