package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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

import com.demo.nspl.restaurantlite.Adapter.ViewPagerCustomerAdapter;
import com.demo.nspl.restaurantlite.ExportData.ClsExportSingleCustomerData;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Navigation_Drawer.PurchaseDetailFragment;
import com.demo.nspl.restaurantlite.Navigation_Drawer.SalesDetailsFragment;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.VendorLedger.ClsVendorLedger;
import com.demo.nspl.restaurantlite.classes.ClsCustomerMaster;
import com.demo.nspl.restaurantlite.classes.ClsExportToExcel;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster;
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class CustomerDetailActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;
    TextView txt_customer_name, txt_company_name, txt_gst_no;
    ClsVendorLedger clsVendorLedger;
    List<ClsPaymentMaster> lstPaymentMaster = new ArrayList();
    List<ClsInventoryOrderMaster> lstInventoryOrderMaster = new ArrayList();

    List<ClsExportSingleCustomerData> lstClsExportGetSets = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        main();
    }


    private void main() {

        clsVendorLedger = (ClsVendorLedger) getIntent().getSerializableExtra("clsVendorLedger");

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        txt_customer_name = findViewById(R.id.txt_customer_name);
        txt_company_name = findViewById(R.id.txt_company_name);
        txt_gst_no = findViewById(R.id.txt_gst_no);

        txt_customer_name.setText(clsVendorLedger.getCustomerName().toUpperCase());

        if (clsVendorLedger.getCompanyName() != null && !clsVendorLedger.getCompanyName().trim().equalsIgnoreCase("")) {
            txt_company_name.setText("COMPANY: " + clsVendorLedger.getCompanyName().toUpperCase());
            txt_company_name.setVisibility(View.VISIBLE);
        } else {
            txt_company_name.setVisibility(View.GONE);
        }

        if (clsVendorLedger.getGST_NO() != null && !clsVendorLedger.getGST_NO().trim().equalsIgnoreCase("")) {
            txt_gst_no.setText("GST No# " + clsVendorLedger.getGST_NO().toUpperCase());
            txt_gst_no.setVisibility(View.VISIBLE);
        } else {
            txt_gst_no.setVisibility(View.GONE);
        }


        Log.e("txt_gst_no", clsVendorLedger.getGST_NO());

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
//        tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#CFCFCF"), Color.parseColor("#FFFFFF"));
        viewPager.setOffscreenPageLimit(2);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            viewPager.setPageTransformer(false, (page, position) -> {
                final float normalizedposition = Math.abs(Math.abs(position) - 1);
                page.setScaleX(normalizedposition / 2 + 0.5f);
                page.setScaleY(normalizedposition / 2 + 0.5f);
            });
        }

    }


    private void setupViewPager(ViewPager viewPager) {

        ViewPagerCustomerAdapter adapter = new ViewPagerCustomerAdapter(getSupportFragmentManager());

        SalesDetailsFragment salesDetailsFragment = new SalesDetailsFragment();
        salesDetailsFragment.SendObj(clsVendorLedger);

        adapter.addFrag(salesDetailsFragment, "SALES");

        PurchaseDetailFragment purchaseDetailFragment = new PurchaseDetailFragment();
        purchaseDetailFragment.SendObj(clsVendorLedger);

        adapter.addFrag(purchaseDetailFragment, "PAYMENT");

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
                            loading = ClsGlobal._prProgressDialog(CustomerDetailActivity.this
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

                                Toast.makeText(CustomerDetailActivity.this, "Export to excel successfully",
                                        Toast.LENGTH_SHORT).show();

                                ClsGlobal.openSnackBarExcelFile(CustomerDetailActivity.this,
                                        aVoid, "Single Customer");
                            } else {
                                Toast.makeText(CustomerDetailActivity.this,
                                        "No record found.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }.execute();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String exportToExcel() {

        List<String> lstStrings = new ArrayList<>();
        String where = "AND [MOBILE_NO] = ".concat(clsVendorLedger.getCustomerMobileNo());
        lstClsExportGetSets = new ClsExportSingleCustomerData().getList(CustomerDetailActivity.this,
                clsVendorLedger.getCustomerMobileNo());

        ClsCustomerMaster getCurrentCustomer = ClsCustomerMaster.getCustomerByMobileNo(where,
                CustomerDetailActivity.this);
//null condition
        Log.e("--Single--", "_mobileNo: " + clsVendorLedger.getCustomerMobileNo());


        if (lstClsExportGetSets != null && lstClsExportGetSets.size() != 0) {

            _checkIfNull = true;

            lstStrings.add("SR#");
            lstStrings.add("DATE");
            lstStrings.add("DETAIL");
            lstStrings.add("PAYMENT MODE");
            lstStrings.add("PAYMENT DETAILS");
            lstStrings.add("AMOUNT");
            lstStrings.add("BALANCE");

            for (ClsExportSingleCustomerData _oBJ : lstClsExportGetSets) {
                double _bal = 0.0;
                if (lstClsExportGetSets.indexOf(_oBJ) == 0) {

                    _oBJ.set_balance(_oBJ.get_amount());

                } else {

                    _bal = lstClsExportGetSets.get(lstClsExportGetSets.indexOf(_oBJ) - 1)
                            .get_balance();

                    Log.e("--bal--", "First_bal: " + _bal);
                    _bal = _bal + _oBJ.get_amount();

                    Log.e("--bal--", "get_amount: " + _oBJ.get_amount());
                    Log.e("--bal--", "Second_bal: " + _bal);
                    Log.e("--bal--", "---------------------------------------------");

                    _oBJ.set_balance(_bal);
                }

                lstClsExportGetSets.set(lstClsExportGetSets.indexOf(_oBJ), _oBJ);
            }
        } else {
            _checkIfNull = false;
        }

        return ClsExportToExcel.createExcelSheet("SingleCustomerReportPath", lstStrings,
                "Single Customer", lstClsExportGetSets,null,getCurrentCustomer);
    }

}
