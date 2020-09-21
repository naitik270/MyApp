package com.demo.nspl.restaurantlite.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.demo.nspl.restaurantlite.Adapter.BillFormatViewPagerAdapter;
import com.demo.nspl.restaurantlite.Navigation_Drawer.Bill_No_Format_No_TaxFragment;
import com.demo.nspl.restaurantlite.Navigation_Drawer.Bill_No_Format_Tax_ApplyFragment;
import com.demo.nspl.restaurantlite.R;
import com.google.android.material.tabs.TabLayout;

public class Bill_No_FormatsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill__no__formats);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        main();
    }

    private void main() {

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
//        tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#CFCFCF"), Color.parseColor("#FFFFFF"));
        viewPager.setOffscreenPageLimit(2);


    }


    private void setupViewPager(ViewPager viewPager) {

        BillFormatViewPagerAdapter adapter = new BillFormatViewPagerAdapter(getSupportFragmentManager());

        Bill_No_Format_Tax_ApplyFragment bill_no_format_tax_applyFragment
                = new Bill_No_Format_Tax_ApplyFragment();
        adapter.addFrag(bill_no_format_tax_applyFragment, "Apply Tax");

        Bill_No_Format_No_TaxFragment bill_no_format_no_taxFragment = new Bill_No_Format_No_TaxFragment();
        adapter.addFrag(bill_no_format_no_taxFragment, "Tax not Apply");

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
