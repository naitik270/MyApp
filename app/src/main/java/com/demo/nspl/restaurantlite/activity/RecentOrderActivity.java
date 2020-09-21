package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.RecentOrderAdapter;
import com.demo.nspl.restaurantlite.AsyncTask.OrderListAsyncTask;
import com.demo.nspl.restaurantlite.BuildConfig;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderDetail;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster;
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;
import com.demo.nspl.restaurantlite.classes.ClsSendMessage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getDeleteSmsMessage;
import static com.demo.nspl.restaurantlite.classes.ClsSMSLogs.DeleteByOrderIdAndOrderNo;

public class RecentOrderActivity extends AppCompatActivity {

    private RecyclerView mRv;
    private List<ClsInventoryOrderMaster> lstClsQuotationMasters = new ArrayList<>();
    private RecentOrderAdapter mAdapter;
    private Toolbar toolbar;
    private TextView empty_title_text, txt_title, txt_sub_title, txt_no_data;
    private String _whereSearch = "";
    int _month = 0, year = 0;
    List<ClsInventoryOrderDetail> list_Current_Order;
    ClsPaymentMaster getClsPaymentMaster;
    ProgressBar progress_bar;
    LinearLayout progress_bar_layout;
    private static final int REQUEST_SMS = 0;

    private static final String AUTHORITY =
            BuildConfig.APPLICATION_ID + ".myfileprovider";

    String saleMode = "";
    private ProgressDialog loading;

    String getWhatsAppDefaultApp = "";
    private SharedPreferences mPreferencesDefault;
    private static final String mPreferncesName = "MyPerfernces";
    String editSource = "";
    FloatingActionButton fab_filter;
    String fragMode = "", monthYear = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_order);

        toolbar = findViewById(R.id.toolbar);
//        setTitle("Recent Orders");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "RecentOrderActivity"));
        }


        saleMode = getIntent().getStringExtra("saleMode");
        editSource = getIntent().getStringExtra("editSource");


        mRv = findViewById(R.id.rv);
        txt_sub_title = findViewById(R.id.txt_sub_title);
        txt_no_data = findViewById(R.id.txt_no_data);
        progress_bar = findViewById(R.id.progress_bar);
        txt_title = findViewById(R.id.txt_title);
        progress_bar_layout = findViewById(R.id.progress_bar_layout);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        empty_title_text = findViewById(R.id.empty_title_text);

        fab_filter = findViewById(R.id.fab_filter);
        fab_filter.setColorFilter(Color.WHITE);


        if (editSource != null &&
                !editSource.isEmpty() &&
                !editSource.equalsIgnoreCase("Recent Orders")) {

            txt_title.setText(editSource);
            txt_sub_title.setVisibility(View.GONE);

        } else {
            txt_title.setText("Recent Orders");
            txt_sub_title.setVisibility(View.VISIBLE);
        }
        fragMode = getIntent().getStringExtra("fragMode");
        mPreferencesDefault = getSharedPreferences(mPreferncesName, MODE_PRIVATE);

        if (mPreferencesDefault.getString("WhatsApp Default App", null) == null) {
            SaveData_SharedPreferences("WhatsApp");
        }

        if (fragMode != null && fragMode.equalsIgnoreCase("SalesReportsFragment")) {
            fab_filter.setVisibility(View.VISIBLE);
            _month = getIntent().getIntExtra("_month", 0);
            year = getIntent().getIntExtra("Year", 0);
            monthYear = ClsGlobal.getMonthName(_month).concat(" " + year);

            String getMonthDay = ClsGlobal.getDayMonth(ClsGlobal.getMonthName(_month) + " " + year);
            Log.e("year", String.valueOf(getMonthDay));

            fillCustomerList();

            fab_filter.setOnClickListener(v1 -> {
                applyFilters();
            });

        } else {
            fab_filter.setVisibility(View.GONE);
        }

        mAdapter = new RecentOrderAdapter(this);
        setSaleItemClick();
        mRv.setAdapter(mAdapter);
        loadData("");
    }

    @SuppressLint("CheckResult")
    void setSaleItemClick() {
        mAdapter.SetOnclinkListenerClsOrder(clsInventoryOrderMaster -> {
            // ClsInventoryOrderMaster

            String _where2 = " AND ORD.[OrderNo] = '" + clsInventoryOrderMaster.getOrderNo() + "' "
                    .concat(" AND ORD.[OrderID] = ").concat(String.valueOf(clsInventoryOrderMaster.getOrderID()));

            Log.e("OnClick", String.valueOf(clsInventoryOrderMaster.getOrderNo()));
            @SuppressLint("WrongConstant") SQLiteDatabase db =
                    openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            list_Current_Order = new ClsInventoryOrderDetail().getListForInvoicePDF(_where2,
                    RecentOrderActivity.this, db);
            db.close();
            _where2 = "AND [InvoiceNo] = '"
                    .concat(String.valueOf(clsInventoryOrderMaster.getOrderNo() + "'"));
            Log.e("_where", _where2);

            getClsPaymentMaster = ClsPaymentMaster.getAmounts(_where2, RecentOrderActivity.this);

            final Dialog dialog = new Dialog(RecentOrderActivity.this);
            // Include dialog.xml file
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_quotation);
            dialog.setCancelable(true);


            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


            LinearLayout ll_options = dialog.findViewById(R.id.ll_options);
            LinearLayout ll_delete = dialog.findViewById(R.id.ll_delete);
            LinearLayout ll_view_details = dialog.findViewById(R.id.ll_view_details);
            LinearLayout ll_delete_payment = dialog.findViewById(R.id.ll_delete_payment);
            ll_delete_payment.setVisibility(View.VISIBLE);
            View view_payment = dialog.findViewById(R.id.view_payment);
            view_payment.setVisibility(View.VISIBLE);

            TextView txt_delete = dialog.findViewById(R.id.txt_delete);
            txt_delete.setText("DELETE ORDER");

       /*
            if (clsInventoryOrderMaster.getBillTo().equalsIgnoreCase("OTHER")) {
                view_View_Details.setVisibility(View.GONE);
                lyout_whatsapp.setVisibility(View.GONE);
                lyout_sms.setVisibility(View.GONE);
                view_upload.setVisibility(View.GONE);
            }*/


            ll_options.setOnClickListener(v15 -> {

                Intent intent = new Intent(RecentOrderActivity.this,
                        OrderSummaryItemActivity.class);

                intent.putExtra("OrderNo", clsInventoryOrderMaster.getOrderNo());
                intent.putExtra("OrderId", clsInventoryOrderMaster.getOrderID());

                if (clsInventoryOrderMaster.getApplyTax() != null &&
                        clsInventoryOrderMaster.getApplyTax().equalsIgnoreCase("YES")) {
                    intent.putExtra("TaxAmount",
                            clsInventoryOrderMaster.getTotalTaxAmount());
                    intent.putExtra("_applyTax", "YES");

                } else {
                    intent.putExtra("TaxAmount", 0.0);
                    intent.putExtra("_applyTax", "NO");
                }

                intent.putExtra("mode", "RecentOrderActivity");
                intent.putExtra("entryMode", "");
                intent.putExtra("editSource", "");
                intent.putExtra("saleMode", "");
                intent.putExtra("title", clsInventoryOrderMaster.getSaleType());
                intent.putExtra("_date", clsInventoryOrderMaster.getBillDate());

                intent.putExtra("_doneBtn", "list");

                startActivity(intent);

                dialog.dismiss();
                dialog.cancel();

            });

            ll_delete_payment.setOnClickListener(v -> {
                dialog.dismiss();
                dialog.cancel();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(RecentOrderActivity.this);
                alertDialog.setTitle("CONFIRM DELETE ORDER WITH PAYMENT...");
                alertDialog.setMessage("Are you sure you want delete?");
                alertDialog.setIcon(R.drawable.ic_delete_black_24dp);

                alertDialog.setPositiveButton("YES", (dialog1, which) -> {

                    @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> delete
                            = new AsyncTask<Void, Void, Void>() {
                        int ResultInventoryOrderDetail = 0, ResultPayment_Master = 0;

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            loading = ClsGlobal._prProgressDialog(RecentOrderActivity.this
                                    , "Please Wait...", false);
                            loading.show();
                        }

                        @Override
                        protected Void doInBackground(Void... voids) {

                            ResultInventoryOrderDetail = ClsInventoryOrderDetail
                                    .DeleteFromOrdermaster(clsInventoryOrderMaster.getOrderNo()
                                            , String.valueOf(clsInventoryOrderMaster.getOrderID()),
                                            "DeleteBy OrderNo and OrderID", RecentOrderActivity.this);
                            Log.e("Result", String.valueOf(ResultInventoryOrderDetail));


                            ResultPayment_Master = ClsPaymentMaster.Delete_Payment_Master(clsInventoryOrderMaster.getOrderNo()
                                    , RecentOrderActivity.this);
                            Log.e("Result", String.valueOf(ResultPayment_Master));

                            int ResultInventoryOrderMaster = ClsInventoryOrderMaster
                                    .Delete_OrderDetail_master(clsInventoryOrderMaster.getOrderNo(),
                                            String.valueOf(clsInventoryOrderMaster.getOrderID())
                                            , RecentOrderActivity.this);
                            Log.e("Result", String.valueOf(ResultInventoryOrderMaster));

                            if (ResultInventoryOrderDetail > 0
                                    && ResultInventoryOrderMaster > 0) {
                                Log.e("Result", "ResultInventoryOrderMaster ");
                                String smsReceiverPhoneNo = ClsGlobal.getSmsReceiver("Sales",
                                        RecentOrderActivity.this);

                                if (smsReceiverPhoneNo != null
                                        && !smsReceiverPhoneNo.equalsIgnoreCase("")) {
                                    ClsGlobal.SendSms("No", "Sales",
                                            clsInventoryOrderMaster.getOrderID(),
                                            clsInventoryOrderMaster.getOrderNo()
                                            , smsReceiverPhoneNo,
                                            clsInventoryOrderMaster.getCustomerName(),
                                            clsInventoryOrderMaster, null,
                                            String.valueOf(clsInventoryOrderMaster.getTotalAmount()),
                                            null, getDeleteSmsMessage(
                                                    clsInventoryOrderMaster, null, "Sales"),
                                            RecentOrderActivity.this);
                                }


                            }


                            // Delete Pending Sms log.
                            DeleteByOrderIdAndOrderNo(RecentOrderActivity.this,
                                    String.valueOf(clsInventoryOrderMaster.getOrderID()),
                                    clsInventoryOrderMaster.getOrderNo());

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);

                            if (loading.isShowing()) {
                                loading.dismiss();
                            }
                            if (ResultInventoryOrderDetail > 0 && ResultPayment_Master > 0) {
                                Toast.makeText(RecentOrderActivity.this,
                                        "Order Deleted Successfully", Toast.LENGTH_LONG).show();
                                loadData("");

                            } else {
                                Toast.makeText(RecentOrderActivity.this,
                                        "Failed to Delete Order", Toast.LENGTH_LONG).show();
                            }
                        }
                    };
                    delete.execute();


                    dialog1.dismiss();
                    dialog1.cancel();

                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });
                // Showing Alert Message
                alertDialog.show();
            });


            ll_delete.setOnClickListener(v -> {
                dialog.dismiss();
                dialog.cancel();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(RecentOrderActivity.this);
                alertDialog.setTitle("Confirm Delete...");
                alertDialog.setMessage("Are you sure you want delete?");
                alertDialog.setIcon(R.drawable.ic_delete_black_24dp);

                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> detete =
                                new AsyncTask<Void, Void, Void>() {

                                    ProgressDialog pd;
                                    int ResultInventoryOrderDetail = 0, ResultInventoryOrderMaster = 0;

                                    @Override
                                    protected void onPreExecute() {
                                        super.onPreExecute();

                                        pd = ClsGlobal._prProgressDialog(RecentOrderActivity.this,
                                                "Loading...", false);
                                        pd.show();
                                    }

                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        ResultInventoryOrderDetail = ClsInventoryOrderDetail
                                                .DeleteFromOrdermaster(clsInventoryOrderMaster.getOrderNo()
                                                        , String.valueOf(clsInventoryOrderMaster.getOrderID()),
                                                        "DeleteBy OrderNo and OrderID", RecentOrderActivity.this);
                                        Log.e("Result", String.valueOf(ResultInventoryOrderDetail));
//
                                        ResultInventoryOrderMaster = ClsInventoryOrderMaster.Delete_OrderDetail_master(clsInventoryOrderMaster.getOrderNo(), String.valueOf(clsInventoryOrderMaster.getOrderID())
                                                , RecentOrderActivity.this);
                                        Log.e("Result", String.valueOf(ResultInventoryOrderMaster));

                                        if (ResultInventoryOrderDetail > 0
                                                && ResultInventoryOrderMaster > 0) {
                                            Log.e("Result", "ResultInventoryOrderMaster ");
                                            String smsReceiverPhoneNo = ClsGlobal.getSmsReceiver("Sales",
                                                    RecentOrderActivity.this);
                                            if (smsReceiverPhoneNo != null
                                                    && !smsReceiverPhoneNo.equalsIgnoreCase("")) {
                                                ClsGlobal.SendSms("No", "Sales",
                                                        clsInventoryOrderMaster.getOrderID(),
                                                        clsInventoryOrderMaster.getOrderNo()
                                                        , smsReceiverPhoneNo,
                                                        clsInventoryOrderMaster.getCustomerName(),
                                                        clsInventoryOrderMaster, null,
                                                        String.valueOf(clsInventoryOrderMaster.getTotalAmount()),
                                                        null, getDeleteSmsMessage(
                                                                clsInventoryOrderMaster, null, "Sales"),
                                                        RecentOrderActivity.this);
                                            }


                                        }


                                        // Delete Pending Sms log.
                                        DeleteByOrderIdAndOrderNo(RecentOrderActivity.this,
                                                String.valueOf(clsInventoryOrderMaster.getOrderID()), clsInventoryOrderMaster.getOrderNo());
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        super.onPostExecute(aVoid);
                                        if (pd.isShowing()) {
                                            pd.dismiss();
                                        }

                                        if (ResultInventoryOrderMaster > 0
                                                && ResultInventoryOrderDetail > 0) {
                                            Toast.makeText(RecentOrderActivity.this,
                                                    "Order Deleted Successfully", Toast.LENGTH_LONG).show();

                                            loadData("");
                                        } else {
                                            Toast.makeText(RecentOrderActivity.this,
                                                    "Failed to Delete Order", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                };

                        detete.execute();
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });
                // Showing Alert Message
                alertDialog.show();
            });


            ll_view_details.setOnClickListener(v -> {


                Intent intent = new Intent(RecentOrderActivity.this, OrderDetailInfoActivity.class);
                intent.putExtra("OrderNo", clsInventoryOrderMaster.getOrderNo());
                intent.putExtra("OrderId", clsInventoryOrderMaster.getOrderID());
                intent.putExtra("Amount", clsInventoryOrderMaster.getTotalAmount());

                if (clsInventoryOrderMaster.getApplyTax() != null &&
                        clsInventoryOrderMaster.getApplyTax().equalsIgnoreCase("YES")) {
                    intent.putExtra("TaxAmount",
                            clsInventoryOrderMaster.getTotalTaxAmount());
                    intent.putExtra("_applyTax", "YES");

                } else {
                    intent.putExtra("TaxAmount", 0.0);
                    intent.putExtra("_applyTax", "NO");
                }

                Log.d("--TEST--", "getTotalReceiveableAmount: " + clsInventoryOrderMaster.getTotalReceiveableAmount());
                Log.d("--TEST--", "getDiscountAmount: " + clsInventoryOrderMaster.getDiscountAmount());


                intent.putExtra("BillAmount", clsInventoryOrderMaster.getTotalReceiveableAmount());
                intent.putExtra("Discount", clsInventoryOrderMaster.getDiscountAmount());
                intent.putExtra("MobileNo", clsInventoryOrderMaster.getMobileNo());
                intent.putExtra("CustomerName", clsInventoryOrderMaster.getCustomerName());
                intent.putExtra("SaleReturnDiscount", clsInventoryOrderMaster.getSaleReturnDiscount());

                startActivity(intent);

                dialog.dismiss();
                dialog.cancel();
            });


            dialog.show();
            dialog.getWindow().setAttributes(lp);
        });
    }

    private void loadData(String _where) {

        new OrderListAsyncTask(_where,
                txt_no_data,
                RecentOrderActivity.this, mAdapter,
                progress_bar, year, _month, saleMode,
                lstClsQuotationMasters, mRv).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_recent_order_activity, menu);
        MenuItem item = menu.findItem(R.id.search);

        if (fragMode != null
                && fragMode.equalsIgnoreCase("SalesReportsFragment")) {
            item.setVisible(false);
        } else {
            item.setVisible(true);
        }


        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                loadData("");
                return true;
            }
        });

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        View closeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(v -> {
            searchView.setQuery("", false);
            searchView.clearFocus();
            loadData("");

        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                _whereSearch = "";
                if (query != null && !query.isEmpty()) {

                    _whereSearch = " AND (IOM.[MobileNo] LIKE '%".concat(query).concat("%'");
                    _whereSearch = _whereSearch.concat(" OR IOM.[CustomerName] LIKE '%".concat(query).concat("%'"));
                    _whereSearch = _whereSearch.concat(" OR IOM.[OrderNo] LIKE '%".concat(query).concat("%'"));
                    _whereSearch = _whereSearch.concat(" OR IOM.[TotalReceiveableAmount] LIKE '%".concat(query).concat("%')"));

                    loadData(_whereSearch);
                }

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


    @Override
    protected void onResume() {
        super.onResume();
        loadData("");

        ClsGlobal.CancelAllNotification(RecentOrderActivity.this);

    }


    private void SaveData_SharedPreferences(String str) {
        SharedPreferences.Editor preferencesEditor = mPreferencesDefault.edit();
        preferencesEditor.putString("WhatsApp Default App", str);
        preferencesEditor.apply();

    }

    String _where1 = "";

    private void LoadAsyncTask(String mode, String phoneNumber,
                               ClsInventoryOrderMaster clsInventoryOrderMaster,
                               String message) {

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, ClsSendMessage> task =
                new AsyncTask<Void, Void, ClsSendMessage>() {


                    private ProgressDialog loading;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        loading = ClsGlobal._prProgressDialog(RecentOrderActivity.this
                                , "Please Wait...", false);
                        loading.show();
                    }

                    @Override
                    protected ClsSendMessage doInBackground(Void... voids) {

                        ClsSendMessage clsSendMessage = new ClsSendMessage();
                        _where1 = " AND ORD.[OrderNo] = '" + clsInventoryOrderMaster.getOrderNo() + "' "
                                .concat(" AND ORD.[OrderID] = ").concat(String.valueOf(clsInventoryOrderMaster.getOrderID()));

                        @SuppressLint("WrongConstant")
                        SQLiteDatabase db = openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
                        clsSendMessage.setList_Current_Order(new ClsInventoryOrderDetail()
                                .getListForInvoicePDF(_where1, RecentOrderActivity.this, db));
                        db.close();
                        _where1 = "AND [InvoiceNo] = '".concat(clsInventoryOrderMaster.getOrderNo() + "'");
                        clsSendMessage.setGetClsPaymentMaster(ClsPaymentMaster.getAmounts(_where1,
                                RecentOrderActivity.this));

                        return clsSendMessage;
                    }


                    @Override
                    protected void onPostExecute(ClsSendMessage clsSendMessage) {
                        super.onPostExecute(clsSendMessage);

                        if (loading.isShowing()) {
                            loading.dismiss();
                        }

                        list_Current_Order = clsSendMessage.getList_Current_Order();
                        getClsPaymentMaster = clsSendMessage.getGetClsPaymentMaster();

                        if (mode.equalsIgnoreCase("WhatsApp")) {
                            ClsGlobal.SentToWhatApp(RecentOrderActivity.this,
                                    phoneNumber,
                                    list_Current_Order,
                                    clsInventoryOrderMaster,
                                    clsInventoryOrderMaster.getOrderNo(),
                                    "No",
                                    ""
                                    , getWhatsAppDefaultApp);
                        } else {
                            ClsGlobal.sendSMS(RecentOrderActivity.this,
                                    phoneNumber, message);
                        }


                    }
                };

        task.execute();

    }

    TextView txt_order_date, txt_to_date, txt_customer_name;
    String _whereFilter = "";

    int mYear, mMonth, mDay;
    Calendar c;
    String _FromOrderDate = "";
    String _ToOrderDate = "";
    EditText edt_order_no, edt_mobile_no;
    String _orderNo = "";
    String _customerMobile = "";
    ImageButton iv_clear_vendor, iv_clear_bill_no, clear_date, iv_clear_mobile_no;
    ArrayList<String> selectedCustomerName = new ArrayList<>();
    String _custMobile = "";
    Button btn_search, btn_clear;

    private void applyFilters() {
        Dialog dialog = new Dialog(RecentOrderActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_filter_order);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        txt_order_date = dialog.findViewById(R.id.txt_order_date);
        txt_to_date = dialog.findViewById(R.id.txt_to_date);

        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        txt_order_date.setOnClickListener(view -> {
            FromOrderDateFromMonth();
        });

        txt_to_date.setOnClickListener(view -> {
            ToOrderDateFromMonth();
        });

        clear_date = dialog.findViewById(R.id.clear_date);

        txt_order_date.setText(ClsGlobal.getQuotationDate(_FromOrderDate));

        txt_customer_name = dialog.findViewById(R.id.txt_customer_name);
        iv_clear_vendor = dialog.findViewById(R.id.iv_clear_vendor);

        iv_clear_bill_no = dialog.findViewById(R.id.iv_clear_bill_no);
        iv_clear_mobile_no = dialog.findViewById(R.id.iv_clear_mobile_no);

        edt_mobile_no = dialog.findViewById(R.id.edt_mobile_no);
        edt_order_no = dialog.findViewById(R.id.edt_order_no);
        btn_search = dialog.findViewById(R.id.btn_search);
        btn_clear = dialog.findViewById(R.id.btn_clear);

        ImageButton bt_close = dialog.findViewById(R.id.bt_close);

        clear_date.setOnClickListener(view -> {
            txt_order_date.setText("");
            txt_to_date.setText("");
            _FromOrderDate = "";
            _ToOrderDate = "";
        });


        txt_customer_name.setHint("SELECT CUSTOMER");
        txt_customer_name.setText(TextUtils.join(",", selectedCustomerName).toUpperCase());

        txt_customer_name.setOnClickListener(view -> {
            if (lstCustomerMasterList != null && lstCustomerMasterList.size() != 0) {
                selectCustomer();
            } else {
                Toast.makeText(this, "NO CUSTOMER FOUND!", Toast.LENGTH_SHORT).show();
            }
        });

        edt_order_no.setText(_orderNo);
        edt_mobile_no.setText(_customerMobile);
        txt_order_date.setText(ClsGlobal.getChangeDateFormat(_FromOrderDate));
        txt_to_date.setText(ClsGlobal.getChangeDateFormat(_ToOrderDate));


        btn_search.setOnClickListener(view -> {

            _orderNo = edt_order_no.getText().toString();
            _customerMobile = edt_mobile_no.getText().toString();
            _whereFilter = "";

            filtersCondition();

            loadData(_whereFilter);

            dialog.dismiss();
            dialog.hide();

        });


        iv_clear_bill_no.setOnClickListener(view -> {
            edt_order_no.setText("");
            _orderNo = "";
        });

        iv_clear_vendor.setOnClickListener(view -> {

            txt_customer_name.setText("");
            _custMobile = "";
            selectedCustomerName.clear();

        });

        iv_clear_mobile_no.setOnClickListener(view -> {
            edt_mobile_no.setText("");
            _customerMobile = "";
        });

        btn_clear.setOnClickListener(view -> {

            _whereFilter = "";

//Date selection is Clear & reset.
            txt_order_date.setText("");
            txt_to_date.setText("");
            _FromOrderDate = "";
            _ToOrderDate = "";

//Customer Name Selection is Clear & reset.
            txt_customer_name.setText("");
            _custMobile = "";
            selectedCustomerName.clear();

//Customer Mobile is Clear & reset.
            edt_mobile_no.setText("");
            _customerMobile = "";

//Order No is Clear & reset.
            edt_order_no.setText("");
            _orderNo = "";

        });

        bt_close.setOnClickListener(view -> {
            dialog.dismiss();
            dialog.hide();
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }


    private void filtersCondition() {

        if (!_FromOrderDate.equalsIgnoreCase("") &&
                !_ToOrderDate.equalsIgnoreCase("")) {


            _whereFilter = _whereFilter.concat(" AND IOM.[BillDate] between "
                    .concat("('".concat(_FromOrderDate).concat("')"))
                    .concat(" AND ")
                    .concat("('".concat(_ToOrderDate).concat("')")));


        } else if (!_FromOrderDate.equalsIgnoreCase("")) {

            _whereFilter = _whereFilter.concat(" AND IOM.[BillDate] = ".concat("('"
                    .concat(_FromOrderDate).concat("')")));

        } else if (!_ToOrderDate.equalsIgnoreCase("")) {

            _whereFilter = _whereFilter.concat(" AND IOM.[BillDate] = ".concat("('"
                    .concat(_ToOrderDate).concat("')")));
        }

        if (!txt_customer_name.getText().toString().equalsIgnoreCase("")) {

            _whereFilter = _whereFilter.concat(" AND IOM.[MobileNo] IN ")
                    .concat("(")
                    .concat(_custMobile)
                    .concat(")");
        }

        if (!_customerMobile.equalsIgnoreCase("")) {

            _whereFilter = _whereFilter.concat(" AND IOM.[MobileNo] LIKE ")
                    .concat("'")
                    .concat(edt_mobile_no.getText().toString().trim())
                    .concat("'");
        }

        if (!_orderNo.equalsIgnoreCase("")) {

            _whereFilter = _whereFilter.concat(" AND IOM.[OrderNo] LIKE ")
                    .concat("'")
                    .concat(edt_order_no.getText().toString().trim())
                    .concat("'");
        }

        Log.e("--QuotationList--", "_whereFilter: " + _whereFilter);

        loadData(_whereFilter);
    }


    private List<ClsInventoryOrderMaster> lstCustomerMasterList;

    private void fillCustomerList() {

        String _where = " AND strftime('%m %Y', [BillDate]) ="
                .concat("'").concat(ClsGlobal.getMonthYearDigit(monthYear)).concat("'");

        lstCustomerMasterList = new ArrayList<>();
        lstCustomerMasterList = new ClsInventoryOrderMaster().getCustomerListFromDate(_where, RecentOrderActivity.this);

    }

    private void selectCustomer() {

        final Dialog dialogCustomer = new Dialog(RecentOrderActivity.this);
        dialogCustomer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCustomer.setContentView(R.layout.dialog_multiple_selection);
        dialogCustomer.setTitle("SELECT CUSTOMER");


        ListView listView = dialogCustomer.findViewById(R.id.listview);
        Button btn_Select = dialogCustomer.findViewById(R.id.button);

        dialogCustomer.setCanceledOnTouchOutside(true);
        dialogCustomer.setCancelable(true);


        final AdapterCustomers expAdapter = new AdapterCustomers(RecentOrderActivity.this,
                (ArrayList<ClsInventoryOrderMaster>) lstCustomerMasterList);
        listView.setAdapter(expAdapter);


        btn_Select.setOnClickListener(v -> {
            boolean isSelected[] = expAdapter.getSelectedFlags();
            selectedCustomerName = new ArrayList<>();
            ArrayList<String> selectedCustomerNumber = new ArrayList<>();

            for (int i = 0; i < isSelected.length; i++) {
                if (isSelected[i]) {
                    selectedCustomerName.add(lstCustomerMasterList.get(i).getCustomerName());
                    selectedCustomerNumber.add("'" + lstCustomerMasterList.get(i).getMobileNo() + "'");
                }
            }

            txt_customer_name.setText(TextUtils.join(",", selectedCustomerName).toUpperCase());
            _custMobile = TextUtils.join(",", selectedCustomerNumber);

            dialogCustomer.dismiss();
        });
        dialogCustomer.show();
    }

    public class AdapterCustomers extends BaseAdapter {

        private ArrayList<ClsInventoryOrderMaster> data = new ArrayList<ClsInventoryOrderMaster>();
        private Context context;
        private boolean isSelected[];

        public AdapterCustomers(Context context, ArrayList<ClsInventoryOrderMaster> data) {
            this.context = context;
            this.data = data;
            isSelected = new boolean[data.size()];
        }


        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position); //returns list item at the specified position
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final ViewHolder holder;

            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.row_listview, null);
                holder = new ViewHolder();
                holder.row_relative_layout = view.findViewById(R.id.row_relative_layout);
                holder.checkedTextView = view.findViewById(R.id.row_list_checkedtextview);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.checkedTextView.setText(data.get(position).getCustomerName().toUpperCase()
                    .concat(" - ").concat(data.get(position).getMobileNo()));

            holder.row_relative_layout.setOnClickListener(v -> {
                // set the check text view
                boolean flag = holder.checkedTextView.isChecked();
                holder.checkedTextView.setChecked(!flag);
                isSelected[position] = !isSelected[position];


                if (holder.checkedTextView.isChecked()) {
                    holder.row_relative_layout.setBackgroundColor(Color.parseColor("#FFCDD2D2"));
                } else {
                    holder.row_relative_layout.setBackgroundResource(0);
                }
            });

            return view;
        }

        public boolean[] getSelectedFlags() {
            return isSelected;
        }

        private class ViewHolder {
            RelativeLayout row_relative_layout;
            CheckedTextView checkedTextView;
        }

    }

    public void FromOrderDateFromMonth() {

        try {
            String dateStr = ClsGlobal.getFirstDateOfMonth(monthYear);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dateObj = sdf.parse(dateStr);

            String _endDate = ClsGlobal.getLastDay(dateObj);
            SimpleDateFormat sdfEndDate = new SimpleDateFormat("dd/MM/yyyy");
            Date lastDateObj = sdfEndDate.parse(_endDate);

            long startDate = dateObj.getTime();
            long endDate = lastDateObj.getTime();

            DatePickerDialog dpd = new DatePickerDialog(RecentOrderActivity.this,
                    (view12, year, month, day) -> {
                        c.set(year, month, day);
                        _FromOrderDate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());

                        Log.e("--Date--", "QuotationDate: " + _FromOrderDate);

                        txt_order_date.setText(ClsGlobal.getChangeDateFormat(_FromOrderDate));

                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                    }, mYear, mMonth, mDay);
            dpd.getDatePicker().setMaxDate(endDate);

            dpd.getDatePicker().setMinDate(startDate);

            Calendar d = Calendar.getInstance();

            dpd.updateDate(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH));
            dpd.show();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public void ToOrderDateFromMonth() {

        try {
            String dateStr = ClsGlobal.getFirstDateOfMonth(monthYear);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dateObj = sdf.parse(dateStr);

            String _endDate = ClsGlobal.getLastDay(dateObj);
            SimpleDateFormat sdfEndDate = new SimpleDateFormat("dd/MM/yyyy");
            Date lastDateObj = sdfEndDate.parse(_endDate);

            long startDate = dateObj.getTime();
            long endDate = lastDateObj.getTime();

            DatePickerDialog dpd = new DatePickerDialog(RecentOrderActivity.this,
                    (view12, year, month, day) -> {
                        c.set(year, month, day);
                        _ToOrderDate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());

                        Log.e("--Date--", "QuotationDate: " + _ToOrderDate);

                        txt_to_date.setText(ClsGlobal.getChangeDateFormat(_ToOrderDate));

                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                    }, mYear, mMonth, mDay);
            dpd.getDatePicker().setMaxDate(endDate);

            dpd.getDatePicker().setMinDate(startDate);

            Calendar d = Calendar.getInstance();

            dpd.updateDate(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH));
            dpd.show();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}
