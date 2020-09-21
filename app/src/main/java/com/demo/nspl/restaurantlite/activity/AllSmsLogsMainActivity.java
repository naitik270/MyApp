package com.demo.nspl.restaurantlite.activity;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.demo.nspl.restaurantlite.Global.TabLayoutMediator;
import com.demo.nspl.restaurantlite.Navigation_Drawer.QuotationSmsLogsFragment;
import com.demo.nspl.restaurantlite.Navigation_Drawer.SaleSmsLogsFragment;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.SMS.PagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AllSmsLogsMainActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_sms_logs_main);

        viewPager2 = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabLayout);

        setupViewPager(viewPager2);

        List<String> titles = new ArrayList<>();
        titles.add("Sales");
        titles.add("Quotation");

        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));

        tabLayout.setTabTextColors(Color.parseColor("#CFCFCF"),
                Color.parseColor("#FFFFFF"));

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) ->
                tab.setText(titles.get(position))).attach();

    }

    private void setupViewPager(ViewPager2 viewPager) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),getLifecycle());

        SaleSmsLogsFragment saleSmsLogsFragment = new SaleSmsLogsFragment();
        adapter.addFrag(saleSmsLogsFragment, "Sales");
//        smsformat.setData("","");


        QuotationSmsLogsFragment quotationSmsLogsFragment = new QuotationSmsLogsFragment();
        adapter.addFrag(quotationSmsLogsFragment, "Quotation");

        viewPager.setAdapter(adapter);
    }
}
