package com.demo.nspl.restaurantlite.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.demo.nspl.restaurantlite.Adapter.MonthWiseStockAdapter;
import com.demo.nspl.restaurantlite.Adapter.SummaryStockAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;

import com.demo.nspl.restaurantlite.classes.ClsInventoryStock;
import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

public class MonthwiseStockActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageButton back_arrow;
    RecyclerView rv, rv_summary;
    LinearLayout nodata_layout;
    MonthWiseStockAdapter cu;
    SummaryStockAdapter adapter;
    private List<ClsInventoryStock> list_stock;

    String where, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthwise_stock);

        toolbar = findViewById(R.id.toolbar);
        back_arrow = findViewById(R.id.back_arrow);
        rv = findViewById(R.id.rv);
        rv_summary = findViewById(R.id.rv_summary);
        nodata_layout = findViewById(R.id.nodata_layout);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "MonthwiseStockActivity"));
        }
        where = getIntent().getStringExtra("whereCondition");
        month = getIntent().getStringExtra("Month");
        year = getIntent().getStringExtra("Year");

        Log.e("NewYear", "Year is-->> " + year);
        Log.e("NewYear", "Month is-->> " + month);


        TextView title = toolbar.findViewById(R.id.title);
        TextView txt_year = toolbar.findViewById(R.id.txt_year);

        title.setText("Monthly Stock");
        txt_year.setText(month.concat(" " + year));
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ViewData("");

    }


    @Override
    public void onResume() {
        super.onResume();
        ViewData("");
    }



    private void ViewData(String whereCondition) {

        rv.setLayoutManager(new LinearLayoutManager(MonthwiseStockActivity.this));
        list_stock = new ArrayList<>();
        list_stock = new ClsInventoryStock(MonthwiseStockActivity.this).getList(where, "general");
        cu = new MonthWiseStockAdapter(MonthwiseStockActivity.this, MonthwiseStockActivity.this, (ArrayList<ClsInventoryStock>) list_stock);
//        int viewHeight = cu.getItemCount() * list_stock.size();
//        rv.getLayoutParams().height = viewHeight;
        rv.setAdapter(cu);
//        cu.notifyDataSetChanged();

        List<ClsInventoryStock> list_summary = new ArrayList<>();

        if (list_stock.size() != 0) {
            List<String> uniqueList = new ArrayList<>();
            for (ClsInventoryStock stock : list_stock) {
                if (!uniqueList.contains(stock.getInventory_item_name())) {
                    uniqueList.add(stock.getInventory_item_name());
                }
            }

            for (String itemName : uniqueList) {
                ClsInventoryStock ObjStk = new ClsInventoryStock();
                double inQty = 0.0;
                double outqTY = 0.0;
                for (ClsInventoryStock stock : list_stock) {
                    if (itemName.equalsIgnoreCase(stock.getInventory_item_name())) {


                        if (stock.getType().equalsIgnoreCase("IN")) {
                            Log.e("QUANTIIII", String.valueOf(stock.getQty()));
                            inQty = inQty +stock.getQty();
                            Log.e("TOTAl", "In----" +stock.getInventory_item_name()+ inQty);

                        } else if (stock.getType().equalsIgnoreCase("OUT")) {

                            outqTY = outqTY + stock.getQty();
                            Log.e("TOTAl", "Out----" + outqTY);


                        }
                        ObjStk.setInventory_item_name(stock.getInventory_item_name());
                        ObjStk.setInqty(inQty);
                        ObjStk.setOutqty(outqTY);
                        ObjStk.setUnitname(stock.getUnitname());
                    }


                }
                list_summary.add(ObjStk);
            }
       }
        rv_summary.setLayoutManager(new LinearLayoutManager(MonthwiseStockActivity.this));
        adapter = new SummaryStockAdapter(MonthwiseStockActivity.this, MonthwiseStockActivity.this, (ArrayList<ClsInventoryStock>) list_summary);
        rv_summary.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }





}
