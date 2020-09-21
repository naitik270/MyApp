package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.R;

import java.util.Objects;

public class PremiumActivity extends AppCompatActivity {

    WebView webView;
    TextView txt_goto_premium;
    String flag = "";
    String _customerId = "";
    String _mobileNo = "";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "PremiumActivity"));
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Premium");
            setSupportActionBar(toolbar);
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        flag = getIntent().getStringExtra("flag");
        _customerId = getIntent().getStringExtra("_customerId");
        _mobileNo = getIntent().getStringExtra("_mobileNo");

        txt_goto_premium = findViewById(R.id.txt_goto_premium);
        webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String premium_url = "https://ftouch.app/PremiumFeature/fTouchPOS.html";
        webView.loadUrl(premium_url);


        if (flag.equalsIgnoreCase("InSide")) {
            txt_goto_premium.setVisibility(View.VISIBLE);

            txt_goto_premium.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(PremiumActivity.this,
                            DisplayPlansActivity.class);

                    intent.putExtra("_customerId", _customerId);
                    intent.putExtra("reg_mode", "new");
                    intent.putExtra("_resCustomerStatus", "");
                    intent.putExtra("_resLicenseType", "");
                    intent.putExtra("_mobileNo", _mobileNo);
                    startActivity(intent);
                    finish();
                }
            });


//            txt_goto_premium.setOnClickListener(v ->
//
//
//
//                    Toast.makeText(PremiumActivity.this, "GO TO PREMIUM", Toast.LENGTH_SHORT).show());

        } else {

            txt_goto_premium.setVisibility(View.GONE);

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
