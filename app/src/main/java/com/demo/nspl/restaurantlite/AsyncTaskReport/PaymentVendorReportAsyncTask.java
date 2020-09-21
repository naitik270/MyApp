package com.demo.nspl.restaurantlite.AsyncTaskReport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.VendorLedger.VendorPaymentAdapter;
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;

import java.util.List;

public class PaymentVendorReportAsyncTask extends AsyncTask<Void, Void, List<ClsPaymentMaster>> {


    private String _where = "";

    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private TextView txt;
    private VendorPaymentAdapter vendorPaymentAdapter;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    private RecyclerView recyclerView;


    public PaymentVendorReportAsyncTask(String _where, TextView txt,
                                        Context context,
                                        VendorPaymentAdapter vendorPaymentAdapter,
                                        ProgressBar progressBar,
                                        RecyclerView recyclerView) {
        this._where = _where;
        this.txt = txt;
        this.context = context;
        this.vendorPaymentAdapter = vendorPaymentAdapter;
        this.progressBar = progressBar;
        this.recyclerView = recyclerView;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected List<ClsPaymentMaster> doInBackground(Void... voids) {
        return new ClsPaymentMaster().getListPaymentDetails(_where, context);
    }

    @Override
    protected void onPostExecute(List<ClsPaymentMaster> lstPaymentMasters) {
        super.onPostExecute(lstPaymentMasters);

        progressBar.setVisibility(View.GONE);

        if (lstPaymentMasters != null && lstPaymentMasters.size() != 0) {
            txt.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            vendorPaymentAdapter.AddItems(lstPaymentMasters);
        } else {
            txt.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

}
