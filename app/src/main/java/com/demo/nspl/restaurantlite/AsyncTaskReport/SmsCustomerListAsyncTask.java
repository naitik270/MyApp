package com.demo.nspl.restaurantlite.AsyncTaskReport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.DisplayCustomerNameForSmsAdapter;
import com.demo.nspl.restaurantlite.SMS.ClsSmsCustomerGroup;

import java.util.List;

public class SmsCustomerListAsyncTask extends AsyncTask<Void, Void, List<ClsSmsCustomerGroup>> {


    int _groupID = 0;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private DisplayCustomerNameForSmsAdapter displayCustomerNameForSmsAdapter;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    private RecyclerView recyclerView;

    public SmsCustomerListAsyncTask(int _groupID,
                                    Context context,
                                    DisplayCustomerNameForSmsAdapter displayCustomerNameForSmsAdapter,
                                    ProgressBar progressBar,
                                    RecyclerView recyclerView) {
        this._groupID = _groupID;
        this.context = context;
        this.displayCustomerNameForSmsAdapter = displayCustomerNameForSmsAdapter;
        this.progressBar = progressBar;
        this.recyclerView = recyclerView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected List<ClsSmsCustomerGroup> doInBackground(Void... voids) {
        return new ClsSmsCustomerGroup().getGroupCustomersList(_groupID, context);
    }

    @Override
    protected void onPostExecute(List<ClsSmsCustomerGroup> lstClsCustomerMasters) {
        super.onPostExecute(lstClsCustomerMasters);

        progressBar.setVisibility(View.GONE);

        if (lstClsCustomerMasters != null && lstClsCustomerMasters.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
//
//            for (int i = 1; i <= 5000; i++) {
//                lstClsCustomerMasters.add(lstClsCustomerMasters.get(i));
//            }

            displayCustomerNameForSmsAdapter.AddItems(lstClsCustomerMasters);
        } else {
            recyclerView.setVisibility(View.GONE);
        }

    }
}
