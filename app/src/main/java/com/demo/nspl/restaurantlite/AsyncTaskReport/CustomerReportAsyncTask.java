package com.demo.nspl.restaurantlite.AsyncTaskReport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Customer.CustomerAdapter;
import com.demo.nspl.restaurantlite.classes.ClsCustomerMaster;

import java.util.List;

public class CustomerReportAsyncTask extends AsyncTask<Void, Void, List<ClsCustomerMaster>> {


    private String _where = "";

    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private TextView txt;
    private CustomerAdapter customerAdapter;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    private RecyclerView recyclerView;


    public CustomerReportAsyncTask(String _where, TextView txt,
                                   Context context,
                                   CustomerAdapter customerAdapter,
                                   ProgressBar progressBar,
                                   RecyclerView recyclerView) {
        this._where = _where;
        this.txt = txt;
        this.context = context;
        this.customerAdapter = customerAdapter;
        this.progressBar = progressBar;
        this.recyclerView = recyclerView;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected List<ClsCustomerMaster> doInBackground(Void... voids) {
        return new ClsCustomerMaster().getListCustomer(_where, context);

    }


    @Override
    protected void onPostExecute(List<ClsCustomerMaster> lstPaymentMasters) {
        super.onPostExecute(lstPaymentMasters);

        progressBar.setVisibility(View.GONE);
        
        if (lstPaymentMasters != null && lstPaymentMasters.size() != 0) {
            txt.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            customerAdapter.AddItems(lstPaymentMasters);
        } else {
            txt.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }


    }

}
