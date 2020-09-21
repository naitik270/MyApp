package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.QuotationDetailAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsQuotationOrderDetail;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class OrderQuotationInfoActivity extends AppCompatActivity {

    Toolbar toolbar;
    QuotationDetailAdapter mAdapter;
    RecyclerView rv;

    TextView tv_Total, tv_Discount, tv_Net_Amount, tv_Tax_Amount, tv_Grand_Total, order_no;

    int quotationId = 0;
    String custMobileNO = "", custName = "", _applyTax = "";
    String quotationNo = "";
    Double netAmount = 0.0, grandAmount = 0.0, taxAmount = 0.0, discountAmount = 0.0;

    TextView txt_sale_return_discount, title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_info);


        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "OrderQuotationInfoActivity"));
        }

        toolbar = findViewById(R.id.toolbar);
        setTitle("");
        setSupportActionBar(toolbar);

        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(OrderQuotationInfoActivity.this));

        order_no = toolbar.findViewById(R.id.order_no);


        title = findViewById(R.id.title);
        title.setText("Quotation List");


        tv_Total = findViewById(R.id.tv_Total);
        txt_sale_return_discount = findViewById(R.id.txt_sale_return_discount);
        tv_Discount = findViewById(R.id.tv_Discount);
        tv_Net_Amount = findViewById(R.id.tv_Net_Amount);
        tv_Tax_Amount = findViewById(R.id.tv_Tax_Amount);
        tv_Grand_Total = findViewById(R.id.tv_Grand_Total);

        quotationId = getIntent().getIntExtra("quotationId", 0);
        netAmount = getIntent().getDoubleExtra("netAmount", 0.0);
        taxAmount = getIntent().getDoubleExtra("taxAmount", 0.0);
        grandAmount = getIntent().getDoubleExtra("grandAmount", 0.0);
        discountAmount = getIntent().getDoubleExtra("discountAmount", 0.0);

        _applyTax = getIntent().getStringExtra("_applyTax");
        custMobileNO = getIntent().getStringExtra("custMobileNO");
        custName = getIntent().getStringExtra("custName");
        quotationNo = getIntent().getStringExtra("quotationNo");//orderNo

        order_no.setText("QUOTATION NO#" + quotationNo);

        ViewData();
    }

    List<ClsQuotationOrderDetail> lstQuotation = new ArrayList<>();


    private void ViewData() {
        lstQuotation = new ArrayList<>();

        String where = " AND [QuotationNo] = ".concat("'").concat(String.valueOf(quotationNo)).concat("'")
                .concat(" AND [QuotationID] = ").concat(String.valueOf(quotationId));

        @SuppressLint("WrongConstant") SQLiteDatabase db =  openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

        lstQuotation = new ClsQuotationOrderDetail()
                .getQuotationDetailList(where, OrderQuotationInfoActivity.this,db);
        db.close();

        Gson gson = new Gson();
        String jsonInString = gson.toJson(lstQuotation);
        Log.d("--ViewData--", "ViewData:  " + jsonInString);

        mAdapter = new QuotationDetailAdapter(OrderQuotationInfoActivity.this, lstQuotation,
                "QuotationList",
                _applyTax.equalsIgnoreCase("YES") ? true : false);

        rv.setAdapter(mAdapter);

        tv_Total.setText("TOTAL:\u20B9 " + netAmount);
        tv_Discount.setText("DISCOUNT:\u20B9 " + discountAmount);
        tv_Net_Amount.setText("NET AMOUNT:\u20B9 " + (netAmount - discountAmount));
        tv_Tax_Amount.setText("TAX AMOUNT:\u20B9 " + taxAmount);

        tv_Grand_Total.setText("\u20B9 " + grandAmount);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewData();
    }

}
