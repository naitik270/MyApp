package com.demo.nspl.restaurantlite.VendorLedger;

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

import com.demo.nspl.restaurantlite.ExportData.ClsExportSingleVendorData;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsExportToExcel;
import com.demo.nspl.restaurantlite.classes.ClsVendor;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class VendorDetailActivity extends AppCompatActivity {


    TabLayout tabLayout;
    ViewPager viewPager;
    int vendorId;
    String vendorName;
    Toolbar toolbar;
    TextView txt_vendor_name;
    List<ClsExportSingleVendorData> lstClsExportGetSets = new ArrayList<>();

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
        vendorId = intent.getIntExtra("vendorId", 0);
        vendorName = intent.getStringExtra("vendorName");

        Log.d("--VendorPurchase--", "Activity: " + vendorId);
        Log.d("--VendorPurchase--", "Activity: " + vendorName);


        txt_vendor_name = findViewById(R.id.txt_vendor_name);
        txt_vendor_name.setText(vendorName);

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

        PurchaseFragment purchaseFragment = new PurchaseFragment();
        purchaseFragment.setVendor(vendorId, vendorName);
        adapter.addFrag(purchaseFragment, "PURCHASE");

        PaymentFragment paymentFragment = new PaymentFragment();
        paymentFragment.setVendor(vendorId, vendorName);
        adapter.addFrag(paymentFragment, "PAYMENT");

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
                            loading = ClsGlobal._prProgressDialog(VendorDetailActivity.this
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

                                Toast.makeText(VendorDetailActivity.this, "Export to excel successfully",
                                        Toast.LENGTH_SHORT).show();

                                ClsGlobal.openSnackBarExcelFile(VendorDetailActivity.this,
                                        aVoid, "Single Vendor");
                            } else {
                                Toast.makeText(VendorDetailActivity.this,
                                        "No record found.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }.execute();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String exportToExcel() {
        List<String> lstStrings = new ArrayList<>();


        lstClsExportGetSets = ClsExportSingleVendorData.getVendorList(VendorDetailActivity.this,
                vendorId);

        ClsVendor objClsVendor = new ClsVendor(VendorDetailActivity.this);
        objClsVendor = ClsVendor.getObject(vendorId);

        if (lstClsExportGetSets != null && lstClsExportGetSets.size() != 0) {
            _checkIfNull = true;

            lstStrings.add("SR#");
            lstStrings.add("DATE");
            lstStrings.add("DETAIL");
            lstStrings.add("PAYMENT MODE");
            lstStrings.add("PAYMENT DETAILS");
            lstStrings.add("AMOUNT");
            lstStrings.add("BALANCE");

            for (ClsExportSingleVendorData _oBJ : lstClsExportGetSets) {
                double _bal = 0.0;
                if (lstClsExportGetSets.indexOf(_oBJ) == 0) {

                    _oBJ.set_balance(_oBJ.get_amount());

                } else {

                    _bal = lstClsExportGetSets.get(lstClsExportGetSets.indexOf(_oBJ) - 1)
                            .get_balance();


                    _bal = _bal + _oBJ.get_amount();



                    _oBJ.set_balance(_bal);
                }

                lstClsExportGetSets.set(lstClsExportGetSets.indexOf(_oBJ), _oBJ);
            }

        } else {
            _checkIfNull = false;
        }


        return ClsExportToExcel.createExcelSheet("SingleVendorReportPath", lstStrings,
                "Single Vendor", lstClsExportGetSets, objClsVendor, null);
    }

}
