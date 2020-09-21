package com.demo.nspl.restaurantlite.activity;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class OverViewActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_view);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView = findViewById(R.id.textView);


        List<String> Master_Menu = new ArrayList<>();

        Master_Menu.add("<b>:: Master Menu</b>");
        Master_Menu.add("-Add tax. Some like CGST, SGST and Other Tax.\n");
        Master_Menu.add("-Add tax slab.");
        Master_Menu.add("-Create all employee.");
        Master_Menu.add("<br>");

        Master_Menu.add("<b>:: Inventory Menu</b>");
        Master_Menu.add("-Create all unit.");
        Master_Menu.add("-Create all retail layer.");
        Master_Menu.add("-Create all retail item.");
        Master_Menu.add("<br>");

        Master_Menu.add("<b>::Purchase Menu</b>");
        Master_Menu.add("-Any product item purchase details add. Some like mobile charger, laptop.");
        Master_Menu.add("<br>");

        Master_Menu.add("<b>:: POS Menu</b>");
        Master_Menu.add("-Any product item sales. Some like mobile charger, laptop. Product item sales details\n" +
                "add.");
        Master_Menu.add("<br>");

        Master_Menu.add("<b>:: Expenses Menu</b>");
        Master_Menu.add("Create expenses type and add expenses details.");
        Master_Menu.add("<br>");

        Master_Menu.add("<b>:: Payment Details</b>");
        Master_Menu.add("(1). Vendor payment (2). Customer payment");
        Master_Menu.add("User can select any one payment option. Add payment details.");
        Master_Menu.add("<br>");

        Master_Menu.add("<b>:: Vendor Ledger</b>");
        Master_Menu.add("Vendor purchase and payment details display in report.");
        Master_Menu.add("<br>");

        Master_Menu.add("<b>:: Customer Ledger</b>");
        Master_Menu.add("Customer purchase and sales and Payment details display in report.");
        Master_Menu.add("<br>");

        Master_Menu.add("<b>:: Customer Ledger</b>");
        Master_Menu.add("Any product item stock details display in report. Some like mobile charger, laptop.");
        Master_Menu.add("<br>");

        Master_Menu.add("<b>:: Settings Menu</b>");
        Master_Menu.add("Auto mail report setup, update profile, change password, change mobile no, printer,\n" +
                "current package details, renew package, backup & restore features available in\n" +
                "application.");
        Master_Menu.add("<br>");

        Log.e("stringBuilder", "" + Html.fromHtml(TextUtils.join("<br>", Master_Menu)));

        textView.setText(Html.fromHtml(TextUtils.join("<br>", Master_Menu)));

        Log.e("stringBuilder", String.valueOf(Html.fromHtml(TextUtils.join("<br>", Master_Menu))));
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
