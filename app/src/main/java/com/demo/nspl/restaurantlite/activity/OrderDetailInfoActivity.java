package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.OrderDetailAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderDetail;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailInfoActivity extends AppCompatActivity {

    Toolbar toolbar;
    OrderDetailAdapter cu;
    RecyclerView rv;
    List<ClsInventoryOrderDetail> list_order;
    TextView tv_Total, tv_Discount, tv_Net_Amount, tv_Tax_Amount, tv_Grand_Total, order_no;
    int getOrderId = 0;

    private Double getDiscountIntent = 0.0, getAmount = 0.0, getTaxAmount = 0.0, getBillAmount = 0.0;
    String getMobileNo, getCustomerName, _applyTax;
    String getOrderNo = "";
    Double SaleReturnDiscount;

    TextView txt_sale_return_discount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_info);
        toolbar = findViewById(R.id.toolbar);
        setTitle("");
        setSupportActionBar(toolbar);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "OrderDetailInfoActivity"));
        }

        rv = findViewById(R.id.rv);
        order_no = toolbar.findViewById(R.id.order_no);

        tv_Total = findViewById(R.id.tv_Total);
        txt_sale_return_discount = findViewById(R.id.txt_sale_return_discount);
        tv_Discount = findViewById(R.id.tv_Discount);
        tv_Net_Amount = findViewById(R.id.tv_Net_Amount);
        tv_Tax_Amount = findViewById(R.id.tv_Tax_Amount);
        tv_Grand_Total = findViewById(R.id.tv_Grand_Total);


        getOrderId = getIntent().getIntExtra("OrderId", 0);

        getAmount = getIntent().getDoubleExtra("Amount", 0.0);
        getTaxAmount = getIntent().getDoubleExtra("TaxAmount", 0.0);
        getBillAmount = getIntent().getDoubleExtra("BillAmount", 0.0);
        getDiscountIntent = getIntent().getDoubleExtra("Discount", 0.0);
        SaleReturnDiscount = getIntent().getDoubleExtra("SaleReturnDiscount", 0.0);

        getMobileNo = getIntent().getStringExtra("MobileNo");
        _applyTax = getIntent().getStringExtra("_applyTax");
        getCustomerName = getIntent().getStringExtra("CustomerName");
        getOrderNo = getIntent().getStringExtra("OrderNo");//orderNo

        order_no.setText(getOrderNo);
//        order_no.setText(String.valueOf(getOrderNo));


        Log.d("--TEST--", "getBillAmount: " + getBillAmount);


        ViewData();
    }

    @SuppressLint("SetTextI18n")
    private void ViewData() {
        list_order = new ArrayList<>();
        rv.setLayoutManager(new LinearLayoutManager(OrderDetailInfoActivity.this));


//        String where = " AND [OrderNo] = ".concat("'").concat(getOrderNo).concat("'");
//        String where = " AND [OrderNo] = ".concat(String.valueOf(getOrderNo));

        Log.d("--TEST--", "getOrderId: " + getOrderId);
        Log.d("--TEST--", "getOrderNo: " + getOrderNo);



        String where = " AND [OrderNo] = ".concat("'").concat(String.valueOf(getOrderNo)).concat("'")
                .concat("AND [OrderID] = ").concat(String.valueOf(getOrderId));


        list_order = ClsInventoryOrderDetail.getList(where, OrderDetailInfoActivity.this);

        Gson gson = new Gson();
        String jsonInString = gson.toJson(list_order);
        Log.d("--TEST--", "lstCurrentOrder: " + jsonInString);


        cu = new OrderDetailAdapter(OrderDetailInfoActivity.this, list_order,
                "OrderDetailInfoActivity",
                _applyTax.equalsIgnoreCase("YES") ? true : false);

        rv.setAdapter(cu);


        tv_Total.setText("TOTAL:\u20B9 " + ClsGlobal.round(getAmount, 2));
        tv_Discount.setText("DISCOUNT:\u20B9 " + ClsGlobal.round(getDiscountIntent, 2));

        double _netAmount = getAmount - getDiscountIntent;
        tv_Net_Amount.setText("NET AMOUNT:\u20B9 " + ClsGlobal.round(_netAmount, 2));
        tv_Tax_Amount.setText("TAX AMOUNT:\u20B9 " + ClsGlobal.round(getTaxAmount, 2));


        if (SaleReturnDiscount != 0.0) {
            txt_sale_return_discount.setVisibility(View.VISIBLE);
            txt_sale_return_discount.setText("SALE RETURN AMOUNT:\u20B9 " + SaleReturnDiscount);
        }


        tv_Grand_Total.setText("\u20B9 " + ClsGlobal.round(getBillAmount, 2));

    }


    @Override
    protected void onResume() {
        super.onResume();
        ViewData();
    }


}
