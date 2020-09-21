package com.demo.nspl.restaurantlite.AsyncTaskReport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.SmsPreviewAdapter;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.SMS.ClsSmsCustomerGroup;
import com.demo.nspl.restaurantlite.classes.ClsBulkSMSLog;

import java.util.List;

public class SmsPreviewAsyncTask extends AsyncTask<Void, Void, List<ClsBulkSMSLog>> {

    private String _where = "";
    private String msg = "";
    private int msgLength = 0;

    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private SmsPreviewAdapter smsPreviewAdapter;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    private RecyclerView recyclerView;
    private SQLiteDatabase db;

    @SuppressLint("StaticFieldLeak")
    TextView txt_credit_count;
    int CreditCount = 0;

    public SmsPreviewAsyncTask(String msg, String _where, int msgLength,
                               Context context, SmsPreviewAdapter smsPreviewAdapter,
                               ProgressBar progressBar, RecyclerView recyclerView, TextView txt_credit_count) {
        this.msg = msg;
        this._where = _where;
        this.msgLength = msgLength;
        this.context = context;
        this.smsPreviewAdapter = smsPreviewAdapter;
        this.progressBar = progressBar;
        this.recyclerView = recyclerView;
        this.txt_credit_count = txt_credit_count;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("--Fill--", "onPreExecute: ");
        progressBar.setVisibility(View.VISIBLE);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected List<ClsBulkSMSLog> doInBackground(Void... voids) {
        Log.d("--Fill--", "doInBackground: ");
        db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
        List<ClsBulkSMSLog> list =new ClsSmsCustomerGroup().getCustomerListFromGroupByID(msg, _where, msgLength, db, context);


        for (ClsBulkSMSLog item: list){
            CreditCount += item.getCreditCount();
        }

        return list;
    }

    @Override
    protected void onPostExecute(List<ClsBulkSMSLog> lstClsBulkSMSLogs) {
        super.onPostExecute(lstClsBulkSMSLogs);
        db.close();

        progressBar.setVisibility(View.GONE);
        Log.d("--Fill--", "onPostExecute: ");


        txt_credit_count.setText("("+CreditCount+")");

        if (lstClsBulkSMSLogs != null && lstClsBulkSMSLogs.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            smsPreviewAdapter.AddItems(lstClsBulkSMSLogs);
            mOnCharacterClick.onCustSmsList(lstClsBulkSMSLogs);
        } else {
            recyclerView.setVisibility(View.GONE);
        }

    }


    getCustomerSmsList mOnCharacterClick;

    public void OnCustomerSmsListClick(getCustomerSmsList mOnCharacterClick) {
        this.mOnCharacterClick = mOnCharacterClick;
    }


    public interface getCustomerSmsList {

        void onCustSmsList(List<ClsBulkSMSLog> lstClsBulkSMSLog);
    }


}
