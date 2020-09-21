package com.demo.nspl.restaurantlite.AsyncTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.RecentOrderAdapter;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster;

import java.util.ArrayList;
import java.util.List;

public class OrderListAsyncTask extends AsyncTask<Void, Void, List<ClsInventoryOrderMaster>> {


    private String _where = "";
    private String saleMode = "";
    private int year = 0;
    private int _month = 0;

    List<ClsInventoryOrderMaster> lstClsInventoryOrderMasters = new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private TextView txt;
    private RecentOrderAdapter recentOrderAdapter;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    private RecyclerView recyclerView;


    public OrderListAsyncTask(String _where, TextView txt, Context context,
                              RecentOrderAdapter recentOrderAdapter,
                              ProgressBar progressBar,
                              int year, int _month, String saleMode,
                              List<ClsInventoryOrderMaster> lstClsInventoryOrderMasters,
                              RecyclerView recyclerView) {
        this._where = _where;
        this.txt = txt;
        this.year = year;
        this._month = _month;
        this.saleMode = saleMode;
        this.lstClsInventoryOrderMasters = lstClsInventoryOrderMasters;
        this.context = context;
        this.recentOrderAdapter = recentOrderAdapter;
        this.progressBar = progressBar;
        this.recyclerView = recyclerView;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected List<ClsInventoryOrderMaster> doInBackground(Void... voids) {

        if (year != 0) {
            _where = _where.concat(" AND strftime('%m', IOM.[BillDate]) = ".concat("'" +
                    AddRemoveZero(_month)).concat("'"))
                    .concat(" AND strftime('%Y', IOM.[BillDate]) = ").concat("'" + year).concat("'");

            lstClsInventoryOrderMasters = new ClsInventoryOrderMaster().getList(_where, false,
                    context);

        } else {
            if (saleMode != null && !saleMode.equalsIgnoreCase("")) {
                _where = _where.concat(" AND IFNULL(IOM.[SaleType],'Sale') = ").concat("'")
                        .concat(saleMode).concat("' ");

                lstClsInventoryOrderMasters = new ClsInventoryOrderMaster().getList(_where, true,
                        context);
            }
        }


        return lstClsInventoryOrderMasters;
    }


    @Override
    protected void onPostExecute(List<ClsInventoryOrderMaster> lstClsInventoryOrderMasters) {
        super.onPostExecute(lstClsInventoryOrderMasters);

        progressBar.setVisibility(View.GONE);

        if (lstClsInventoryOrderMasters != null && lstClsInventoryOrderMasters.size() != 0) {
            txt.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recentOrderAdapter.AddItems(lstClsInventoryOrderMasters);
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
