package com.demo.nspl.restaurantlite.Stock;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.demo.nspl.restaurantlite.R;
import com.google.android.material.tabs.TabLayout;

public class StockDetailActivity extends AppCompatActivity {


    TabLayout tabLayout;
    ViewPager viewPager;
    String itemCode, itemName;
    Toolbar toolbar;
    TextView txt_vendor_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_detail);


        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        main();
    }

    private void main() {

        Intent intent = getIntent();

        itemCode = intent.getStringExtra("itemCode");
        itemName = intent.getStringExtra("itemName");


        txt_vendor_name = findViewById(R.id.txt_vendor_name);
        txt_vendor_name.setText(itemName);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));

        tabLayout.setTabTextColors(Color.parseColor("#CFCFCF"), Color.parseColor("#FFFFFF"));
        viewPager.setOffscreenPageLimit(2);

    }


    private void setupViewPager(ViewPager viewPager) {
        StockPagerAdapter adapter = new StockPagerAdapter(getSupportFragmentManager(), itemCode);

        PurchaseStockFragment purchaseStockFragment = new PurchaseStockFragment();
        purchaseStockFragment.setVendor(itemCode);
        adapter.addFrag(purchaseStockFragment, "PURCHASE");

        SaleStockFragment saleStockFragment = new SaleStockFragment();
        saleStockFragment.setVendor(itemCode);
        adapter.addFrag(saleStockFragment, "SALE");

        viewPager.setAdapter(adapter);
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
