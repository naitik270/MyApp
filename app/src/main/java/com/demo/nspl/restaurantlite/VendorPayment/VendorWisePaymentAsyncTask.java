package com.demo.nspl.restaurantlite.VendorPayment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Purchase.ClsPurchaseMaster;
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;

import java.util.ArrayList;
import java.util.List;

public class VendorWisePaymentAsyncTask extends AsyncTask<Void, Void, List<ClsVendorWisePayment>> {

    private String _where = "";

    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private VendorWisePaymentAdapter vendorWisePaymentAdapter;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    private RecyclerView recyclerView;
    private SQLiteDatabase db;

    public VendorWisePaymentAsyncTask(String _where, Context context,
                                      VendorWisePaymentAdapter vendorWisePaymentAdapter,
                                      ProgressBar progressBar, RecyclerView recyclerView) {

        this._where = _where;
        this.context = context;
        this.vendorWisePaymentAdapter = vendorWisePaymentAdapter;
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
    protected List<ClsVendorWisePayment> doInBackground(Void... voids) {
        db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

        List<ClsVendorWisePayment> _listResult = new ArrayList<>();

        _listResult = new ClsPurchaseMaster().getVendorWisePurchaseList(db, _where, context);
        _listResult.addAll(new ClsPaymentMaster().getVendorWisePaymentGOT(db, _where, context));


        return _listResult;
    }

    @Override
    protected void onPostExecute(List<ClsVendorWisePayment> lstClsBulkSMSLogs) {
        super.onPostExecute(lstClsBulkSMSLogs);
        db.close();

        progressBar.setVisibility(View.GONE);

        if (lstClsBulkSMSLogs != null && lstClsBulkSMSLogs.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            vendorWisePaymentAdapter.AddItems(lstClsBulkSMSLogs);
//            mOnCharacterClick.onCustSmsList(lstClsBulkSMSLogs);
        } else {
            recyclerView.setVisibility(View.GONE);
        }

    }

}
