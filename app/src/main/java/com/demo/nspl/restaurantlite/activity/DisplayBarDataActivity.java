package com.demo.nspl.restaurantlite.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.demo.nspl.restaurantlite.Adapter.DisplayBarDataAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsExpenseMasterNew;

import java.util.ArrayList;
import java.util.List;

public class DisplayBarDataActivity extends AppCompatActivity {
    TextView txt_name, txt_nodata;
    Toolbar toolbar;
    String month, totalVal, startDate, lastDate;
    private RecyclerView rv_bardata;
    List<ClsExpenseMasterNew> lstClsExpenseMasterNews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_bar_data);

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        txt_name = findViewById(R.id.txt_name);
        txt_nodata = findViewById(R.id.txt_nodata);
        rv_bardata = findViewById(R.id.rv_bardata);
        rv_bardata.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ViewBarData();

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "DisplayBarDataActivity"));
        }
        Intent intent = getIntent();
        month = intent.getStringExtra("month");
        totalVal = intent.getStringExtra("totalVal");
        startDate = intent.getStringExtra("startDate");
        lastDate = intent.getStringExtra("lastDate");

        Log.e("Bar","Month"+String.valueOf(month));
        Log.e("Bar","TotalValue"+totalVal );
        Log.e("Bar","StartDate"+startDate );
        Log.e("Bar","LastDate"+lastDate );
        txt_name.setText(month);

    }

    @Override
    public void onResume() {
        super.onResume();
        ViewBarData();

    }


    private void ViewBarData() {
        String _where = " AND em.[RECEIPT_DATE] BETWEEN ('" + startDate + "') AND ('" + lastDate + "') ";
        Log.d("_where", "ViewBarData: " + _where);

        lstClsExpenseMasterNews = new ArrayList<>();
        lstClsExpenseMasterNews = new ClsExpenseMasterNew(getApplicationContext()).getListNew(_where);

//        Gson gson = new Gson();
//        String jsonInString = gson.toJson(lstClsExpenseMasterNews);
        Log.e("Display:-- ", String.valueOf(lstClsExpenseMasterNews));

        if (lstClsExpenseMasterNews != null && lstClsExpenseMasterNews.size() != 0) {
            txt_nodata.setVisibility(View.GONE);
            rv_bardata.setVisibility(View.VISIBLE);
            DisplayBarDataAdapter displayBarDataAdapter = new DisplayBarDataAdapter(DisplayBarDataActivity.this, lstClsExpenseMasterNews);
            rv_bardata.setAdapter(displayBarDataAdapter);
        } else {
            txt_nodata.setVisibility(View.VISIBLE);
            rv_bardata.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
