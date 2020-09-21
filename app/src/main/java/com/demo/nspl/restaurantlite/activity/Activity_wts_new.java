package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;

public class Activity_wts_new extends AppCompatActivity {

    WebView webView;
    String webViewMode = "";
    String terms_url = "";
    String webViewLink = "";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wts_new);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webViewMode = getIntent().getStringExtra("webViewMode");
        webViewLink = getIntent().getStringExtra("webViewLink");

        if (webViewMode.equalsIgnoreCase("New")) {
            terms_url = "https://ftouch.app/ftouchPOSHelp.html";

            if (toolbar != null) {
                toolbar.setTitle("What's New");
            }

        } else {
            terms_url = webViewLink;
            if (toolbar != null) {
                toolbar.setTitle("Offer Terms");
            }
        }

        webView.loadUrl(terms_url);

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(Activity_wts_new.this,
                        "Waiting for link...", true);
        pd.show();

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                pd.dismiss();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
