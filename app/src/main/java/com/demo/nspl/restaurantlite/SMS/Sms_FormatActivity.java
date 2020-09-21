package com.demo.nspl.restaurantlite.SMS;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager2.widget.ViewPager2;

import com.demo.nspl.restaurantlite.R;

public class Sms_FormatActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private String mode = "",id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms__format);

        viewPager2 = findViewById(R.id.view_pager);

        id = getIntent().getStringExtra("Id");
        mode = getIntent().getStringExtra("mode");

        setupViewPager(viewPager2);

    }


    private void setupViewPager(ViewPager2 viewPager) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),getLifecycle());

        SmsFormat_Fragment smsformat = new SmsFormat_Fragment();
        adapter.addFrag(smsformat, "Sms Format");
        smsformat.setData(mode,id);

        Sms_Format_Preview_Fragment smsformat_preview = new Sms_Format_Preview_Fragment();
        adapter.addFrag(smsformat_preview, "Sms Format Preview");

        viewPager.setAdapter(adapter);
    }
}
