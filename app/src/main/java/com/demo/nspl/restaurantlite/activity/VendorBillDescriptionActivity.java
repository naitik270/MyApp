package com.demo.nspl.restaurantlite.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.demo.nspl.restaurantlite.Adapter.NameValueAdapter;
import com.demo.nspl.restaurantlite.Adapter.StockItemsAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;

import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsExpenseMasterNew;

import com.demo.nspl.restaurantlite.classes.ClsInventoryStock;
import com.demo.nspl.restaurantlite.classes.ClsNameValue;

import java.util.ArrayList;
import java.util.List;

public class VendorBillDescriptionActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView list, list_items;
    StockItemsAdapter adapter;
    private List<ClsInventoryStock> list_stock = new ArrayList<>();
    LinearLayout nodata_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_bill_description);

        list = findViewById(R.id.list);
        list_items = findViewById(R.id.list_items);
        nodata_layout = findViewById(R.id.nodata_layout);
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Vendor Bill Report");
            toolbar.setTitleTextColor(android.graphics.Color.WHITE);
            setSupportActionBar(toolbar);
        }
        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "VendorBillDescriptionActivity"));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        getSupportActionBar().setDisplayShowTitleEnabled(false);


        int Expense_Id = getIntent().getIntExtra("ID", 0);


        ClsExpenseMasterNew objExpense = new ClsExpenseMasterNew();
        objExpense = new ClsExpenseMasterNew(this).getObject(Expense_Id);
        Log.e("EXPE", String.valueOf(Expense_Id));


        list_stock = objExpense.getList_items();

        if (list_stock.size() == 0) {
            nodata_layout.setVisibility(View.VISIBLE);
        } else {
            nodata_layout.setVisibility(View.GONE);
        }

        ViewItemsData();


        List<ClsNameValue> lstClsNameValues = new ArrayList<>();
        ClsNameValue objClsNameValues = new ClsNameValue();

        objClsNameValues.setName("Trasaction Date:");
        objClsNameValues.setValue(objExpense.getReceipt_date());
        lstClsNameValues.add(objClsNameValues);


        objClsNameValues = new ClsNameValue();
        objClsNameValues.setName("Expense Type:");
        objClsNameValues.setValue(String.valueOf(objExpense.getExpense_type_name()));
        lstClsNameValues.add(objClsNameValues);


        objClsNameValues = new ClsNameValue();
        objClsNameValues.setName("Vendor:");
        objClsNameValues.setValue(objExpense.getVendor_name());
        lstClsNameValues.add(objClsNameValues);

        objClsNameValues = new ClsNameValue();
        objClsNameValues.setName("Bill/Receipt No:");
        objClsNameValues.setValue(objExpense.getReceipt_no());
        lstClsNameValues.add(objClsNameValues);

        objClsNameValues = new ClsNameValue();
        objClsNameValues.setName("Amount:");
        objClsNameValues.setValue(String.valueOf(objExpense.getAmount()));
        lstClsNameValues.add(objClsNameValues);


        if (objExpense.getOther_tax1() != null && !objExpense.getOther_tax1().isEmpty()) {
            objClsNameValues = new ClsNameValue();
            objClsNameValues.setName(objExpense.getOther_tax1());
            objClsNameValues.setValue(String.valueOf(objExpense.getOther_val1()));
            lstClsNameValues.add(objClsNameValues);
        }

        if (objExpense.getOther_tax2() != null && !objExpense.getOther_tax2().isEmpty()) {
            objClsNameValues = new ClsNameValue();
            objClsNameValues.setName(objExpense.getOther_tax2());
            objClsNameValues.setValue(String.valueOf(objExpense.getOther_val2()));
            lstClsNameValues.add(objClsNameValues);
        }

        if (objExpense.getOther_tax3() != null && !objExpense.getOther_tax3().isEmpty()) {
            objClsNameValues = new ClsNameValue();
            objClsNameValues.setName(objExpense.getOther_tax3());
            objClsNameValues.setValue(String.valueOf(objExpense.getOther_val3()));
            lstClsNameValues.add(objClsNameValues);
        }

        if (objExpense.getOther_tax4() != null && !objExpense.getOther_tax4().isEmpty()) {
            objClsNameValues = new ClsNameValue();
            objClsNameValues.setName(objExpense.getOther_tax4());
            objClsNameValues.setValue(String.valueOf(objExpense.getOther_val4()));
            lstClsNameValues.add(objClsNameValues);
        }


        if (objExpense.getOther_tax5() != null && !objExpense.getOther_tax5().isEmpty()) {
            objClsNameValues = new ClsNameValue();
            objClsNameValues.setName(objExpense.getOther_tax5());
            objClsNameValues.setValue(String.valueOf(objExpense.getOther_val5()));
            lstClsNameValues.add(objClsNameValues);
        }


        objClsNameValues = new ClsNameValue();
        objClsNameValues.setName("Discount:");
        objClsNameValues.setValue(String.valueOf(objExpense.getDiscount()));
        lstClsNameValues.add(objClsNameValues);

        objClsNameValues = new ClsNameValue();
        objClsNameValues.setName("Total:");
        objClsNameValues.setValue(String.valueOf(objExpense.getGRAND_TOTAL()));
        lstClsNameValues.add(objClsNameValues);

        objClsNameValues = new ClsNameValue();
        objClsNameValues.setName("Remark:");
        objClsNameValues.setValue(String.valueOf(objExpense.getRemark()));
        lstClsNameValues.add(objClsNameValues);

        objClsNameValues = new ClsNameValue();
        objClsNameValues.setName("Entry Date:");
        objClsNameValues.setValue(String.valueOf(objExpense.getEntry_date()));
        lstClsNameValues.add(objClsNameValues);

        NameValueAdapter adpNameValueAdapter = new NameValueAdapter(getApplicationContext(), lstClsNameValues);
        list.setAdapter(adpNameValueAdapter);
    }

    private void ViewItemsData() {
        adapter = new StockItemsAdapter(VendorBillDescriptionActivity.this,list_stock);
        list_items.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.notifyDataSetInvalidated();
    }


    @Override
    protected void onResume() {
        super.onResume();
//        ViewItemsData();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
