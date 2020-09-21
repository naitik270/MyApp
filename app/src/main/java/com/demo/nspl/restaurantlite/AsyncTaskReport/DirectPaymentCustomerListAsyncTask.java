package com.demo.nspl.restaurantlite.AsyncTaskReport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.demo.nspl.restaurantlite.Customer.DirectPaymentCustomerListAdapter;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.classes.ClsCustomerMaster;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class DirectPaymentCustomerListAsyncTask extends AsyncTask<Void, Void, List<ClsCustomerMaster>> {


    private String _where = "";

    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private RelativeLayout lyout_nodata;
    private DirectPaymentCustomerListAdapter customerAdapter;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    private StickyListHeadersListView stickyListHeadersListView;
    private SQLiteDatabase db;

    public DirectPaymentCustomerListAsyncTask(String _where, RelativeLayout lyout_nodata,
                                              Context context,
                                              DirectPaymentCustomerListAdapter customerAdapter,
                                              ProgressBar progressBar,
                                              StickyListHeadersListView stickyListHeadersListView) {
        this._where = _where;
        this.lyout_nodata = lyout_nodata;
        this.context = context;
        this.customerAdapter = customerAdapter;
        this.progressBar = progressBar;
        this.stickyListHeadersListView = stickyListHeadersListView;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected List<ClsCustomerMaster> doInBackground(Void... voids) {
        db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
        return new ClsCustomerMaster().getCustomerListForDirectPayment(_where, context, db);
    }


    @Override
    protected void onPostExecute(List<ClsCustomerMaster> lstPaymentMasters) {
        super.onPostExecute(lstPaymentMasters);
        db.close();

        progressBar.setVisibility(View.GONE);

        if (lstPaymentMasters != null && lstPaymentMasters.size() != 0) {
            lyout_nodata.setVisibility(View.GONE);
            stickyListHeadersListView.setVisibility(View.VISIBLE);
            customerAdapter.AddItems(lstPaymentMasters);
        } else {
            lyout_nodata.setVisibility(View.VISIBLE);
            stickyListHeadersListView.setVisibility(View.GONE);
        }


    }

}
