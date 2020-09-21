package com.demo.nspl.restaurantlite.VendorPayment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Purchase.ClsPurchaseMaster;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.VendorLedger.ClsVendorLedger;
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DirectVendorPaymentActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton fab_add_vendor;

    TextView empty_title_text;
    List<ClsVendorLedger> list = new ArrayList<>();
    RecyclerView rv_vendor_list;
    MainVendorPaymentListAdapter cu;
    String _mainWhereSearch = "";
    String type = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_vendor_payment);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "DirectVendorPaymentActivity"));
        }

        main();

    }

    private void main() {
        type = getIntent().getStringExtra("type");
        Log.d("--Type--", "Step-1: " + type);

        rv_vendor_list = findViewById(R.id.rv_vendor_list);
        rv_vendor_list.setLayoutManager(new LinearLayoutManager(DirectVendorPaymentActivity.this));

        fab_add_vendor = findViewById(R.id.fab_add_vendor);
        empty_title_text = findViewById(R.id.empty_title_text);


        ViewData();

    }


    private void ViewData() {


        cu = new MainVendorPaymentListAdapter(DirectVendorPaymentActivity.this);

        rv_vendor_list.setAdapter(cu);

        cu.SetOnClickListener(clsVendorLedger -> {

            Intent intent = new Intent(DirectVendorPaymentActivity.this,
                    VendorWisePaymentReportActivity.class);
            intent.putExtra("clsVendorLedger", clsVendorLedger);
            intent.putExtra("type", type);

//            intent.putExtra("vendorId", clsVendorLedger.getVENDOR_ID());
//            intent.putExtra("bill", clsVendorLedger.getVENDOR_ID());
//            intent.putExtra("vendorName", clsVendorLedger.getVENDOR_NAME());
//
            startActivity(intent);

        });


        new LoadAsyncTask("", DirectVendorPaymentActivity.this).execute();
    }


    @SuppressLint("StaticFieldLeak")
    class LoadAsyncTask extends AsyncTask<Void, Void, List<ClsVendorLedger>> {

        String mode = "";
        ProgressDialog pd;
        Context context;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(context);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("Loading..");
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();
        }

        LoadAsyncTask(String mode, Context context) {
            this.mode = mode;
            this.context = context;
        }

        @Override
        protected List<ClsVendorLedger> doInBackground(Void... voids) {
            list = new ArrayList<>();
            if (!mode.equalsIgnoreCase("")) {
                list.clear();
            }
            list = ClsPurchaseMaster.getVendorLedgerList(_mainWhereSearch, DirectVendorPaymentActivity.this);


            List<ClsPaymentMaster> lstCustomerPayments = new ClsPaymentMaster().getVendorPayment
                    ("", DirectVendorPaymentActivity.this);

            if (list != null && list.size() != 0) {
                if (lstCustomerPayments != null && lstCustomerPayments.size() != 0) {
                    for (ClsVendorLedger obj : list) {
                        for (ClsPaymentMaster objVendor : lstCustomerPayments) {
                            if (obj.getVENDOR_ID() == objVendor.getVendorID()) {
                                obj.setTotalPaymentAmount(objVendor.getAmount());
                                list.set(list.indexOf(obj), obj);
                                break;
                            }
                        }
                    }
                }
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<ClsVendorLedger> list) {
            super.onPostExecute(list);
            pd.cancel();
            cu.AddItems(list);
            if (list.size() == 0) {
                empty_title_text.setVisibility(View.VISIBLE);
            } else {
                empty_title_text.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.search);

        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                _mainWhereSearch = "";
                new LoadAsyncTask("", DirectVendorPaymentActivity.this).execute();
                return true;
            }
        });

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search by name");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        View closeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(v -> {
            _mainWhereSearch = "";
            searchView.setQuery("", false);
            searchView.clearFocus();
            new LoadAsyncTask("", DirectVendorPaymentActivity.this).execute();
            rv_vendor_list.setVisibility(View.VISIBLE);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (query != null && !query.isEmpty()) {
                    _mainWhereSearch = " AND (V.[VENDOR_NAME] LIKE '%".concat(query).concat("%')");
                } else {
                    _mainWhereSearch = "";
                }

                new LoadAsyncTask("", DirectVendorPaymentActivity.this).execute();

                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });

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
