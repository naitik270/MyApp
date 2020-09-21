package com.demo.nspl.restaurantlite.AsyncTaskReport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Customer.ClsCustomerWisePayment;
import com.demo.nspl.restaurantlite.Customer.CustomerWisePaymentAdapter;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster;
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;

import java.util.ArrayList;
import java.util.List;

public class CustomerWisePaymentAsyncTask extends AsyncTask<Void, Void, List<ClsCustomerWisePayment>> {

    private String _where = "";

    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private CustomerWisePaymentAdapter customerWisePaymentAdapter;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    private RecyclerView recyclerView;
    private SQLiteDatabase db;

    public CustomerWisePaymentAsyncTask(String _where, Context context,
                                        CustomerWisePaymentAdapter customerWisePaymentAdapter,
                                        ProgressBar progressBar, RecyclerView recyclerView) {

        this._where = _where;
        this.context = context;
        this.customerWisePaymentAdapter = customerWisePaymentAdapter;
        this.progressBar = progressBar;
        this.recyclerView = recyclerView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected List<ClsCustomerWisePayment> doInBackground(Void... voids) {
        db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

        List<ClsCustomerWisePayment> _listResult = new ArrayList<>();
        _listResult = new ClsInventoryOrderMaster().getCustomerWiseSales(db, _where, context);
        _listResult.addAll(new ClsPaymentMaster().getCustomerwisePaymentGOT(db, _where, context));


        return _listResult;
    }

    @Override
    protected void onPostExecute(List<ClsCustomerWisePayment> lstClsBulkSMSLogs) {
        super.onPostExecute(lstClsBulkSMSLogs);
        db.close();

        progressBar.setVisibility(View.GONE);

        if (lstClsBulkSMSLogs != null && lstClsBulkSMSLogs.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            customerWisePaymentAdapter.AddItems(lstClsBulkSMSLogs);
//            mOnCharacterClick.onCustSmsList(lstClsBulkSMSLogs);
        } else {
            recyclerView.setVisibility(View.GONE);
        }

    }

}
