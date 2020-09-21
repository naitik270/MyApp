package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.nspl.restaurantlite.Global.ApiClient;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsFAQLanguage;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsLanguage;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceFAQLanguage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Faq_Activity extends AppCompatActivity {

    private Toolbar toolbar;
    private WebView webView;
    String language_preference;

    List<String> Radio_list = new ArrayList<>();


    String _destinationMode = "";


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ProgressDialog pd =
                ClsGlobal._prProgressDialog(Faq_Activity.this, "Please wait...", true);
        pd.show();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);


        _destinationMode = getIntent().getStringExtra("_destinationMode");


        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webSettings.setAllowFileAccess(true);
                webSettings.setAppCacheEnabled(true);
                Log.e("Check", "url: " + url);

                if (!url.contains("youtu.be")) {
                    view.loadUrl(url);

                } else {
                    String videoId = getVideoIdFromYoutubeUrl(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
                    intent.putExtra("VIDEO_ID", videoId);
                    startActivity(intent);

                }

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pd.dismiss();
            }
        });


        // get language preference from setting file.
        language_preference = ClsGlobal.get_FAQ_language(Faq_Activity.this);
        Log.e("Check", "language_preference:- " + language_preference);


        if (ClsGlobal.CheckInternetConnection(Faq_Activity.this)
                && language_preference == null) {
            // If there is no Language saved in setting then open language Dialog.
            getLanguage("on Create");


        } else {
            if (language_preference.equalsIgnoreCase("")) {
                Log.e("Check", "language_preference:- " + "true");
            } else {
                Log.e("Check", "language_preference:- " + "false");
            }

            // If there is Language saved in setting then Load FAQ url in web view.
            LoadFaq_Url();
        }
    }


    public String getVideoIdFromYoutubeUrl(String url) {
        String videoId = null;
        String regex = "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            videoId = matcher.group(1);
        }
        return videoId;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_faq, menu);
        return true;
    }

    @SuppressLint("CheckResult")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.change_language) {
            if (ClsGlobal.CheckInternetConnection(Faq_Activity.this)) {
                getLanguage("Menu");

            } else {
                Toast.makeText(Faq_Activity.this,
                        "There No Internet available!", Toast.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    // Dialog for Language.
    public void CreateRadio_Btn_Dialog() {

        language_preference = ClsGlobal.get_FAQ_language(Faq_Activity.this);
        Log.e("Check", "language_preference:- " + language_preference);
        AlertDialog.Builder builder = new AlertDialog.Builder(Faq_Activity.this);
        builder.setTitle("Choose Language");
        builder.setCancelable(false);

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(
                Faq_Activity.this, // Context
                android.R.layout.simple_list_item_single_choice, // Layout
                Radio_list  // List
        );

        final String[] selectedItem = {""};
        int checkedItem = Radio_list.indexOf(language_preference == null ?
                "English" : language_preference); //this will checked the item when user open the dialog

        selectedItem[0] = Radio_list.get(checkedItem);
        builder.setSingleChoiceItems(arrayAdapter, checkedItem,
                (dialog, which) -> selectedItem[0] = Radio_list.get(which));

        builder.setPositiveButton("Done", (dialog, which) -> {

            // Save Language to setting.
            ClsGlobal.Save_FAQ_language(Faq_Activity.this,
                    String.valueOf(selectedItem[0]));

            // Load FAQ url.
            LoadFaq_Url();

            dialog.dismiss();
        });


        AlertDialog dialog = builder.create();
        if (!((Activity) this).isFinishing()) {
            dialog.show();
        }
    }

    // Get Language From Api.
    private void getLanguage(String mode) {
        ProgressDialog pd =
                ClsGlobal._prProgressDialog(Faq_Activity.this, "Loading...", true);
        pd.show();

        InterfaceFAQLanguage interfaceFAQLanguage =
                ApiClient.getRetrofitInstance().create(InterfaceFAQLanguage.class);
        Call<ClsFAQLanguage> call = interfaceFAQLanguage.getAvailableFAQ_Language();

        Log.e("Check", "getLanguage");

        call.enqueue(new Callback<ClsFAQLanguage>() {
            @Override
            public void onResponse(Call<ClsFAQLanguage> call, Response<ClsFAQLanguage> response) {
                pd.dismiss();
                Log.e("Check", "code:- " + response.code());

                if (response.body() != null && response.code() == 200) {

                    Log.e("Check", "getData:- " + response.body().getData().toString());

                    List<ClsLanguage> list = response.body().getData();

                    Radio_list.clear();

                    for (ClsLanguage current : list) {
                        Radio_list.add(current.getLanguage());
                    }

                    if (Radio_list.size() > 0 && language_preference == null
                            || mode.equalsIgnoreCase("Menu")) {

                        // Open Dialog.
                        CreateRadio_Btn_Dialog();
                    }
                } else {
                    Toast.makeText(Faq_Activity.this,
                            "No Language or Error while getting Language's!",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ClsFAQLanguage> call, Throwable t) {
                pd.dismiss();
                Log.e("Check", t.getMessage());
            }
        });
    }

    String _productName = "fTouch POS";

    // Load FAQ Url.
    @SuppressLint("SetJavaScriptEnabled")
    private void LoadFaq_Url() {
        // get language preference from setting file.
        language_preference = ClsGlobal.get_FAQ_language(Faq_Activity.this);
        String Faq_Url = "";

        if (!_destinationMode.contains("Settings")) {
            Faq_Url = "https://www.ftouch.app/Admin/Knowledge/KnowledgeBase/" +
                    "?Product= " + ClsGlobal.AppName + " & Language= " + language_preference +
                    "&FilterText=&Title=Registration";
        } else {
            Faq_Url = "https://www.ftouch.app/Admin/Knowledge/KnowledgeBase/" +
                    "?Product=" + ClsGlobal.AppName + "& Language=" + language_preference;
        }
        webView.loadUrl(Faq_Url);
    }

}
