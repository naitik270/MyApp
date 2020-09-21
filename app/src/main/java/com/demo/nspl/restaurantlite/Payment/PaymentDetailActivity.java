package com.demo.nspl.restaurantlite.Payment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.demo.nspl.restaurantlite.ExportData.ClsExportPaymentReportData;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.VendorLedger.ViewPagerAdapter;
import com.demo.nspl.restaurantlite.classes.ClsExportToExcel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PaymentDetailActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    int vendorId;
    String vendorName;
    Toolbar toolbar;
    TextView txt_vendor_name;
    String Date = "";
    List<ClsExportPaymentReportData> lstDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);


        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        main();
    }

    private void main() {

        Intent intent = getIntent();
        vendorId = intent.getIntExtra("vendorId", 0);
        vendorName = intent.getStringExtra("vendorName");
        Date = intent.getStringExtra("Date");

        Log.d("--VendorId--", "Activity: " + vendorId);
        Log.d("--VendorId--", "Activity: " + vendorName);

        Log.d("--Date--", "Activity: " + Date);

        txt_vendor_name = findViewById(R.id.txt_vendor_name);

        if (Date != null && !Date.equalsIgnoreCase("")) {
            txt_vendor_name.setText(Date);
        }

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
//        tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#CFCFCF"), Color.parseColor("#FFFFFF"));
        viewPager.setOffscreenPageLimit(2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
                @Override
                public void transformPage(View page, float position) {
                    final float normalizedposition = Math.abs(Math.abs(position) - 1);
                    page.setScaleX(normalizedposition / 2 + 0.5f);
                    page.setScaleY(normalizedposition / 2 + 0.5f);
                }
            });
        }
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), vendorId);

        CustomerPaymentFragment customerPaymentFragment = new CustomerPaymentFragment();
        customerPaymentFragment.setValue(vendorId, vendorName, Date);
        adapter.addFrag(customerPaymentFragment, "CUSTOMER");

        VendorPaymentFragment vendorPaymentFragment = new VendorPaymentFragment();
        vendorPaymentFragment.setValue(vendorId, vendorName, Date);
        adapter.addFrag(vendorPaymentFragment, "VENDOR");

        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_export, menu);
        MenuItem menu_excel = menu.findItem(R.id.itm_excel);
//        menu_excel.setVisible(false);
        return true;
    }

    ProgressDialog loading;
    boolean _checkIfNull = false;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }

        if (id == R.id.itm_excel) {
            @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, String> asyncTask =
                    new AsyncTask<Void, Void, String>() {
                        @Override
                        protected String doInBackground(Void... voids) {

                            return exportToExcel();
                        }

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            loading = ClsGlobal._prProgressDialog(PaymentDetailActivity.this
                                    , "Please Wait...", false);
                            loading.show();
                        }

                        @Override
                        protected void onPostExecute(String aVoid) {
                            super.onPostExecute(aVoid);
                            Log.d("--customerReport--", "aVoid: " + aVoid);

                            if (loading.isShowing()) {
                                loading.dismiss();
                            }


                            if (_checkIfNull) {

                                Toast.makeText(PaymentDetailActivity.this, "Export to excel successfully",
                                        Toast.LENGTH_SHORT).show();

                                ClsGlobal.openSnackBarExcelFile(PaymentDetailActivity.this,
                                        aVoid, "Payment Details");
                            } else {
                                Toast.makeText(PaymentDetailActivity.this,
                                        "No record found.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }.execute();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String exportToExcel() {

        String _wherePaymentMIS = " AND [PaymentMounth] = '".concat(Date).concat("'");

        Log.d("--_wherePaymentMIS--", "EntryDate: " + ClsGlobal.getEntryDate());
        Log.d("--_wherePaymentMIS--", "Date: " + Date);
        Log.d("--_wherePaymentMIS--", "Activity: " + vendorId);
        Log.d("--_wherePaymentMIS--", "_wherePaymentMIS: " + _wherePaymentMIS);

        lstDataList = ClsExportPaymentReportData.getPaymentList
                (PaymentDetailActivity.this, _wherePaymentMIS);

        List<String> lstStrings = new ArrayList<>();

        if (lstDataList != null && lstDataList.size() != 0) {
            _checkIfNull = true;

            lstStrings.add("SR#");
            lstStrings.add("DATE");
            lstStrings.add("TYPE");
            lstStrings.add("NAME");
            lstStrings.add("MOBILE NO.");
            lstStrings.add("INVOICE NO.");
            lstStrings.add("DETAIL");
            lstStrings.add("MODE");
            lstStrings.add("IN AMOUNT");
            lstStrings.add("OUT AMOUNT");
//            lstStrings.add("TOTAL");

        } else {

            _checkIfNull = false;
        }

        return ClsExportToExcel.createExcelSheet("PaymentReportPath", lstStrings,
                "Payment Details", lstDataList, null, null);

    }
}
