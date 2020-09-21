package com.demo.nspl.restaurantlite.AsyncTaskReport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.PaymentDetailsAdapter;
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;

import java.util.List;

public class PaymentReportAsyncTask extends AsyncTask<Void, Void, List<ClsPaymentMaster>> {


    private String _where = "";
    private String mode = "";

    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private TextView txt;
    private PaymentDetailsAdapter paymentDetailsAdapter;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    private RecyclerView recyclerView;


    public PaymentReportAsyncTask(String _where, String mode, TextView txt,
                                  Context context,
                                  PaymentDetailsAdapter paymentDetailsAdapter,
                                  ProgressBar progressBar,
                                  RecyclerView recyclerView) {
        this._where = _where;
        this.mode = mode;
        this.txt = txt;
        this.context = context;
        this.paymentDetailsAdapter = paymentDetailsAdapter;
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
            paymentDetailsAdapter.AddItems(lstPaymentMasters);
        } else {
            txt.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }


    }

}
