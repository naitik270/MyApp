package com.demo.nspl.restaurantlite.AsyncTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.RecentQuotationAdapter;
import com.demo.nspl.restaurantlite.classes.ClsQuotationMaster;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class QuotationListAsyncTask extends AsyncTask<Void, Void, List<ClsQuotationMaster>> {


    private String _where = "";
    private String saleMode = "";
    private int year = 0;
    private int _month = 0;

    List<ClsQuotationMaster> lstClsQuotationMasters = new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private TextView txt;
    @SuppressLint("StaticFieldLeak")
    private RecentQuotationAdapter recentQuotationAdapter;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    private RecyclerView recyclerView;


    public QuotationListAsyncTask(String _where, TextView txt, Context context,
                                  RecentQuotationAdapter recentQuotationAdapter,
                                  ProgressBar progressBar,
                                  int year, int _month, String saleMode,
                                  List<ClsQuotationMaster> lstClsQuotationMasters,
                                  RecyclerView recyclerView) {
        this._where = _where;
        this.txt = txt;
        this.year = year;
        this._month = _month;
        this.saleMode = saleMode;
        this.lstClsQuotationMasters = lstClsQuotationMasters;
        this.context = context;
        this.recentQuotationAdapter = recentQuotationAdapter;
        this.progressBar = progressBar;
        this.recyclerView = recyclerView;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected List<ClsQuotationMaster> doInBackground(Void... voids) {

        if (year != 0) {
            _where = _where.concat(" AND strftime('%m', [QuotationDate]) = ".concat("'" +
                    AddRemoveZero(_month)).concat("'"))
                    .concat(" AND strftime('%Y', [QuotationDate]) = ").concat("'" + year).concat("'");

            lstClsQuotationMasters = new ClsQuotationMaster().getQuotationList(_where, false,
                    context);

        } else {
            if (saleMode != null && !saleMode.equalsIgnoreCase("")) {
                _where = _where.concat(" AND IFNULL([QuotationType],'Retail Quotation') = ").concat("'")
                        .concat(saleMode).concat("' ");

                lstClsQuotationMasters = new ClsQuotationMaster().getQuotationList(_where, true,
                        context);

            }
        }
        Gson gson = new Gson();
        String jsonInString = gson.toJson(lstClsQuotationMasters);
        Log.d("--Gson--", "list: " + jsonInString);

        return lstClsQuotationMasters;
    }


    @Override
    protected void onPostExecute(List<ClsQuotationMaster> lstClsQuotationMasters) {
        super.onPostExecute(lstClsQuotationMasters);

        progressBar.setVisibility(View.GONE);

        if (lstClsQuotationMasters != null && lstClsQuotationMasters.size() != 0) {
            txt.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recentQuotationAdapter.AddItems(lstClsQuotationMasters);

        } else {
            txt.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private String AddRemoveZero(int value) {

        if (value < 10) {
            return "0" + value;
        } else {
            return "" + value;
        }
    }

}
