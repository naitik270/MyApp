package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.AsyncTaskReport.DirectPaymentCustomerListAsyncTask;
import com.demo.nspl.restaurantlite.Customer.DirectPaymentCustomerListAdapter;
import com.demo.nspl.restaurantlite.Customer.MainCustomerPaymentListAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Interface.OnClickCustomerAdapter;
import com.demo.nspl.restaurantlite.Purchase.ClsPurchaseMaster;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.VendorLedger.ClsVendorLedger;
import com.demo.nspl.restaurantlite.classes.ClsCustomerMaster;
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class DirectCustomerPaymentActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton fab_add_customer;

    TextView empty_title_text;
    List<ClsVendorLedger> list = new ArrayList<>();
    RecyclerView rv_cust_payment;
    MainCustomerPaymentListAdapter cu;
    String _mainWhereSearch = "";
    String type = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_cust_payment);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "DirectCustomerPaymentActivity"));
        }

        main();

    }

    private void main() {
        type = getIntent().getStringExtra("type");
        Log.d("--Type--", "Step-1: " + type);


        rv_cust_payment = findViewById(R.id.rv_cust_payment);
        rv_cust_payment.setLayoutManager(new LinearLayoutManager(DirectCustomerPaymentActivity.this));

        fab_add_customer = findViewById(R.id.fab_add_customer);
        empty_title_text = findViewById(R.id.empty_title_text);

//        CustomerDialog();

        fab_add_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dialogCustomer.show();

                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, 1);

            }
        });

        ViewData();

    }
            /*##Customer Payment
            0.Create new Customer
            1.Add opening Bal

            2.SALES
            2.1 Sales with payment
            2.2 Sales with pending payment (add to credit)

            3.Payment
            3.1 Collect
            3.2 Pay

            check final Customer balanceadd 3 BOX in dashboard
            Customer Payment - Discussion ✔
            Vendor Payment - Discussion ✔
            Other Payment - Discuss after above Points complete*/

    private void ViewData() {


        cu = new MainCustomerPaymentListAdapter(DirectCustomerPaymentActivity.this);

        rv_cust_payment.setAdapter(cu);

        cu.SetOnClickListener(clsVendorLedger -> {

            Intent intent = new Intent(DirectCustomerPaymentActivity.this,
                    CustomerWisePaymentReportActivity.class);
            intent.putExtra("clsVendorLedger", clsVendorLedger);
            intent.putExtra("type", type);

            startActivity(intent);

//            Toast.makeText(DirectCustomerPaymentActivity.this, "sdf", Toast.LENGTH_SHORT).show();
//            Log.d("--Test--", "CLICK: " + clsVendorLedger.getCustomerName());


        });


        new LoadAsyncTask("", DirectCustomerPaymentActivity.this).execute();
    }

    Dialog dialogCustomer;
    private StickyListHeadersListView lst;
    private RelativeLayout lyout_nodata;
    String _whereCustomerDialogSearch = "";
    DirectPaymentCustomerListAdapter directAdapter;

    private void CustomerDialog() {

        EditText edit_search;
        dialogCustomer = new Dialog(DirectCustomerPaymentActivity.this);
        dialogCustomer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCustomer.setContentView(R.layout.dialog_type_n_search);
        dialogCustomer.setTitle("Select Customer");
        dialogCustomer.setCanceledOnTouchOutside(true);
        dialogCustomer.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogCustomer.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogCustomer.getWindow().setAttributes(lp);

        lst = dialogCustomer.findViewById(R.id.list);
        lyout_nodata = dialogCustomer.findViewById(R.id.lyout_nodata);
        LinearLayout ll_pbar = dialogCustomer.findViewById(R.id.ll_pbar);
        ll_pbar.setVisibility(View.VISIBLE);
        ProgressBar progress_bar = dialogCustomer.findViewById(R.id.progress_bar);
        edit_search = dialogCustomer.findViewById(R.id.edit_search);

        directAdapter = new DirectPaymentCustomerListAdapter(DirectCustomerPaymentActivity.this);
        lst.setAdapter(directAdapter);
        directAdapter.SetOnClickListener(new OnClickCustomerAdapter() {
            @Override
            public void OnClick(ClsCustomerMaster clsCustomerMaster) {
                Toast.makeText(DirectCustomerPaymentActivity.this, "Name: " + clsCustomerMaster.getmName(), Toast.LENGTH_SHORT).show();

            }
        });

        new DirectPaymentCustomerListAsyncTask(_whereCustomerDialogSearch, lyout_nodata,
                DirectCustomerPaymentActivity.this, directAdapter, progress_bar, lst).execute();

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s != null && !s.equals("")) {
                    _whereCustomerDialogSearch = " AND ([NAME] LIKE '%".concat(String.valueOf(s)).concat("%'");
                    _whereCustomerDialogSearch = _whereCustomerDialogSearch.concat(" OR [MOBILE_NO] LIKE '%".concat(String.valueOf(s)).concat("%')"));
                } else {
                    _whereCustomerDialogSearch = "";
                }
                new DirectPaymentCustomerListAsyncTask(_whereCustomerDialogSearch, lyout_nodata,
                        DirectCustomerPaymentActivity.this, directAdapter, progress_bar, lst).execute();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewData();
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
            list = ClsPurchaseMaster.getCustomerLedgerList(_mainWhereSearch, DirectCustomerPaymentActivity.this);

            List<ClsPaymentMaster> lstCustomerPayments = new ClsPaymentMaster().getCustomerPayment
                    ("", DirectCustomerPaymentActivity.this);

            if (lstCustomerPayments != null && lstCustomerPayments.size() != 0) {
                for (ClsVendorLedger obj : list) {
                    for (ClsPaymentMaster objCust : lstCustomerPayments) {
                        if (obj.getCustomerMobileNo().equalsIgnoreCase(objCust.getMobileNo())) {
                            obj.setTotalPaymentAmount(objCust.getAmount());
                            list.set(list.indexOf(obj), obj);
                            break;
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

    private Uri uri = null;
    private static final Pattern MobileNo_Pattern
            = Pattern.compile(ClsGlobal.MobileNo_Pattern);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1) && (resultCode == RESULT_OK)) {
            Cursor cursor = null;
            try {
                uri = data.getData();
                cursor = getContentResolver().query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
                if (cursor != null && cursor.moveToNext()) {

                    String phone = cursor.getString(0);

                    if (phone.contains("+91") || phone.contains("+91 ")) {
                        Log.e("phone", "+91");

                        phone = phone.replace("+91", "");
                    } else if (String.valueOf(phone.charAt(0)).contains("0")) {
                        Log.e("phone", "phone.charAt(0) call");
                        phone = phone.replace("0", "");
                    }

                    if (phone.contains(" ")) {
                        phone = phone.replace(" ", "");
                    }

                    Matcher matcher = MobileNo_Pattern.matcher(phone);
                    String contactNumber = "";
                    String contactName = "";

                    if (matcher.matches()) {
                        contactNumber = phone;

                        // querying contact data store
                        cursor = getContentResolver().query(uri, null,
                                null, null, null);

                        if (cursor.moveToFirst()) {
                            contactName = cursor.getString(cursor.getColumnIndex
                                    (ContactsContract.Contacts.DISPLAY_NAME));
                        }
                        cursor.close();
                    } else {
                        contactNumber = "";
                        contactName = "";
                    }

                    for (ClsVendorLedger obj : list) {
                        if (obj.getCustomerMobileNo().equals(phone)) {

                            Log.d("--Test--", "Contact: " + obj.getCustomerMobileNo());
                            Log.d("--Test--", "phone: " + phone);
                            Log.d("--Test--", "contactNumber: " + contactNumber);
                            Log.d("--Test--", "contactName: " + contactName);

                            Toast.makeText(DirectCustomerPaymentActivity.this, "Already exist", Toast.LENGTH_LONG).show();

                        } else {
                            InsertCustomerPayment(contactName, phone, "", "TO PAY");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void InsertCustomerPayment(String _name, String _phn, String _address, String _balanceType) {

        ClsCustomerMaster objClsCustomerMaster =
                new ClsCustomerMaster(DirectCustomerPaymentActivity.this);

//        objClsCustomerMaster.setmId(0);
        objClsCustomerMaster.setmName(_name);
        objClsCustomerMaster.setmMobile_No(_phn);
        objClsCustomerMaster.setGST_NO("");
        objClsCustomerMaster.setAddress(_address);
        objClsCustomerMaster.setCompany_Name("");
        objClsCustomerMaster.setBalanceType(_balanceType);

        int resultInsertCustomer = ClsCustomerMaster.INSERT(
                objClsCustomerMaster, DirectCustomerPaymentActivity.this);

        Log.d("--Test--", "resultInsertCustomer: " + resultInsertCustomer);
        Log.d("--Test--", "resultInsertName: " + _name);
        Log.d("--Test--", "resultInsertNumber: " + _phn);
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
                new LoadAsyncTask("", DirectCustomerPaymentActivity.this).execute();
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
            new LoadAsyncTask("", DirectCustomerPaymentActivity.this).execute();
            rv_cust_payment.setVisibility(View.VISIBLE);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !query.isEmpty()) {
                    _mainWhereSearch = " AND (CM.[NAME] LIKE '%".concat(query).concat("%')");
                } else {
                    _mainWhereSearch = "";
                }
                new LoadAsyncTask("", DirectCustomerPaymentActivity.this).execute();
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
