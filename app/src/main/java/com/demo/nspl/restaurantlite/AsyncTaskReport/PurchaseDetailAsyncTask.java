package com.demo.nspl.restaurantlite.AsyncTaskReport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Purchase.ClsPurchaseMaster;
import com.demo.nspl.restaurantlite.Purchase.PurchaseDetailAdapter;

import java.util.List;

public class PurchaseDetailAsyncTask extends AsyncTask<Void, Void, List<ClsPurchaseMaster>> {


    private String _where = "";
    private String mode = "";

    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private TextView txt;
    private PurchaseDetailAdapter purchaseDetailAdapter;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    private RecyclerView recyclerView;

    private SQLiteDatabase db;

    public PurchaseDetailAsyncTask(String _where, TextView txt,
                                   Context context,
                                   PurchaseDetailAdapter purchaseDetailAdapter,
                                   ProgressBar progressBar,
                                   RecyclerView recyclerView) {
        this._where = _where;
        this.txt = txt;
        this.context = context;
        this.purchaseDetailAdapter = purchaseDetailAdapter;
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
    protected List<ClsPurchaseMaster> doInBackground(Void... voids) {
        db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

        return new ClsPurchaseMaster().getPurchaseDetailList(db,_where, context);

    }


    @Override
    protected void onPostExecute(List<ClsPurchaseMaster> lstPaymentMasters) {
        super.onPostExecute(lstPaymentMasters);
        db.close();
        progressBar.setVisibility(View.GONE);

        if (lstPaymentMasters != null && lstPaymentMasters.size() != 0) {
            txt.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            purchaseDetailAdapter.AddItems(lstPaymentMasters);
        } else {
            txt.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }


    }

}
