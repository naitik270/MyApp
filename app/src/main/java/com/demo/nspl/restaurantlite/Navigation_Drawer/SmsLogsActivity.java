package com.demo.nspl.restaurantlite.Navigation_Drawer;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.demo.nspl.restaurantlite.Adapter.MultipleSelectionCustSmsAdapter;
import com.demo.nspl.restaurantlite.Adapter.MultipleSelectionSmsStatusAdapter;
import com.demo.nspl.restaurantlite.Adapter.SmsLogsAdapter;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.EndlessRecyclerViewScrollListener;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsBulkSMSLog;
import com.demo.nspl.restaurantlite.classes.ClsSMSBulkMaster;
import com.demo.nspl.restaurantlite.classes.ClsSummery;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.CheckBulkSmsStatus;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getSamplePreview;

public class SmsLogsActivity extends AppCompatActivity {

    protected static final String TAG = SmsLogsActivity.class.getSimpleName();
    private RecyclerView mRv;
    private Toolbar toolbar;
    List<ClsBulkSMSLog> list = new ArrayList<>();
    private SQLiteDatabase db;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ClsSMSBulkMaster clsSMSBulkMaster;
    SmsLogsAdapter adapter;
    TextView tv_total_customer, tv_credit_utilized, tv_delivered, tv_pending,
            tv_dnd, txt_failed, txt_nodata, tv_cust_name;
    LinearLayout progress_bar_layout;
    ClsSummery clsSummery;

    EditText edit_search_main;
    ImageView img_clear;


    FloatingActionButton fab_filter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_logs);

//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mRv = findViewById(R.id.rv);
        txt_nodata = findViewById(R.id.txt_nodata);
        tv_total_customer = findViewById(R.id.tv_total_customer);
        tv_credit_utilized = findViewById(R.id.tv_credit_utilized);
        tv_delivered = findViewById(R.id.tv_delivered);
        tv_pending = findViewById(R.id.tv_pending);
        tv_dnd = findViewById(R.id.tv_dnd);
        mSwipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        txt_failed = findViewById(R.id.txt_failed);
        tv_cust_name = findViewById(R.id.tv_cust_name);

        fab_filter = findViewById(R.id.fab_filter);
        fab_filter.setColorFilter(Color.WHITE);
        fab_filter.setAlpha(0.50f);

        img_clear = findViewById(R.id.img_clear);
        edit_search_main = findViewById(R.id.edit_search_main);

        progress_bar_layout = findViewById(R.id.progress_bar_layout);
//        mRv.setLayoutManager(new LinearLayoutManager(this));

        clsSMSBulkMaster = (ClsSMSBulkMaster) getIntent().getSerializableExtra(
                "ClsSMSBulkMaster");
        tv_cust_name.setText(clsSMSBulkMaster.getGroupName().toUpperCase());

        fillCustomerList();
        fillStatusList();

        adapter = new SmsLogsAdapter(SmsLogsActivity.this);
        mRv.setAdapter(adapter);
        fab_filter.setOnClickListener(v1 -> applyFilters());

        // For SwipeRefresh.
        mSwipeRefreshLayout.setOnRefreshListener(() -> {

            adapter.clear();
            ViewData(0);

        });

        adapter.SetOnClickListener((obj, position) -> {

            AlertDialog.Builder popupBuilder = new AlertDialog
                    .Builder(SmsLogsActivity.this);
            popupBuilder.setTitle("Message");
            popupBuilder.setMessage(getSamplePreview(obj.getMessage(),
                    SmsLogsActivity.this));
            popupBuilder.setPositiveButton("OK", (dialog, which) -> {
                dialog.dismiss();
                dialog.cancel();
            });

            popupBuilder.show();

        });

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SmsLogsActivity.this);
        mRv.setLayoutManager(linearLayoutManager);

        mRv.addOnScrollListener(new EndlessRecyclerViewScrollListener(
                linearLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {

                ViewData(++page);
//                ViewData(0);

            }
        });


        ViewData(0);
    }


    TextView txt_customer_name;
    TextView txt_select_status;
    ImageButton iv_clear_status, iv_clear_customer;
    ArrayList<String> selectedCustomerName = new ArrayList<>();
    ArrayList<String> selectedCustomerMob = new ArrayList<>();
    ArrayList<String> selectedSmsStatus = new ArrayList<>();


    private List<ClsBulkSMSLog> lstClsBulkSMSLogs;

    @SuppressLint("WrongConstant")
    private void fillCustomerList() {


        Log.e("--bulkID--", "getBulkID: " + clsSMSBulkMaster.getBulkID());

        db = openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);


        lstClsBulkSMSLogs = new ArrayList<>();
        lstClsBulkSMSLogs = new ClsBulkSMSLog().getCustomerListUsingBulkID
                (clsSMSBulkMaster.getBulkID(), db);

        db.close();
    }


    private List<ClsBulkSMSLog> lstClsBulkSMSLogsStatus;

    @SuppressLint("WrongConstant")
    private void fillStatusList() {


        Log.e("--status--", "getBulkID: " + clsSMSBulkMaster.getBulkID());

        db = openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);


        lstClsBulkSMSLogsStatus = new ArrayList<>();
        lstClsBulkSMSLogsStatus = new ClsBulkSMSLog().getStatusListUsingBulkID
                (clsSMSBulkMaster.getBulkID(), db);

        db.close();
    }

    void applyFilters() {

        Dialog dialogPaymentFilter = new Dialog(SmsLogsActivity.this);
        dialogPaymentFilter.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPaymentFilter.setContentView(R.layout.dialog_apply_filters_for_sms);
        Objects.requireNonNull(dialogPaymentFilter.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogPaymentFilter.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogPaymentFilter.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        txt_customer_name = dialogPaymentFilter.findViewById(R.id.txt_customer_name);
        iv_clear_customer = dialogPaymentFilter.findViewById(R.id.iv_clear_customer);

        txt_select_status = dialogPaymentFilter.findViewById(R.id.txt_select_status);
        iv_clear_status = dialogPaymentFilter.findViewById(R.id.iv_clear_status);

        ImageButton bt_close = dialogPaymentFilter.findViewById(R.id.bt_close);
        Button btn_search = dialogPaymentFilter.findViewById(R.id.btn_search);
        Button btn_clear_filters = dialogPaymentFilter.findViewById(R.id.btn_clear_filters);

        txt_select_status.setText(TextUtils.join(",", selectedSmsStatus).toUpperCase().replace("'", ""));
        txt_select_status.setOnClickListener(view -> {
            selectStatus();
        });

        iv_clear_customer.setOnClickListener(view -> {
            Check_UnCheckListForCust(false);
            txt_customer_name.setText("");
            selectedCustomerName.clear();
            _custName = "";
        });

        iv_clear_status.setOnClickListener(view -> {
            Check_UnCheckList(false);
            txt_select_status.setText("");
            selectedSmsStatus.clear();
            _smsStatus = "";
        });

        txt_customer_name.setText(TextUtils.join(",", selectedCustomerName).toUpperCase().replace("'", ""));
        txt_customer_name.setOnClickListener(view -> {
            selectCustomer();
        });

        bt_close.setOnClickListener(view -> {
            dialogPaymentFilter.dismiss();
            dialogPaymentFilter.cancel();

        });

        btn_search.setOnClickListener(view -> {
            _where = "";

            filtersCondition();

            ViewData(0);

            dialogPaymentFilter.dismiss();
            dialogPaymentFilter.hide();
        });

        btn_clear_filters.setOnClickListener(view -> {
            _where = "";

            Check_UnCheckListForCust(false);
            txt_customer_name.setText("");
            Check_UnCheckList(false);
            txt_select_status.setText("");

            selectedCustomerName.clear();
            selectedSmsStatus.clear();

            _custName = "";
            _smsStatus = "";

        });

        dialogPaymentFilter.show();
        dialogPaymentFilter.getWindow().setAttributes(lp);
    }

    String _custName = "";
    String _custMob = "";
    String _smsStatus = "";
    String _where = "";


    private void filtersCondition() {

        if (!_custName.equalsIgnoreCase("")) {


            _where = _where.concat(" AND UPPER([CustomerName]) IN ")
                    .concat("(")
                    .concat(_custName.trim())
                    .concat(")");
        }

        if (!_custMob.equalsIgnoreCase("")) {


            _where = _where.concat(" OR [Mobile] IN ")
                    .concat("(")
                    .concat(_custMob.trim())
                    .concat(")");
        }

        if (!_smsStatus.equalsIgnoreCase("")) {


            _where = _where.concat(" AND [Status] IN ")
                    .concat("(")
                    .concat(_smsStatus.trim())
                    .concat(")");
        }

        Log.d("--_where--", "_where: " + _where);
    }


    ProgressBar progress_bar;
    EditText edit_search;
    RecyclerView rv_select_customer;
    MultipleSelectionCustSmsAdapter expAdapter = new MultipleSelectionCustSmsAdapter(SmsLogsActivity.this);
    MultipleSelectionSmsStatusAdapter expAdapterStatus = new MultipleSelectionSmsStatusAdapter(SmsLogsActivity.this);
    boolean flag;

    private void selectStatus() {

        final Dialog dialogExpenseType = new Dialog(SmsLogsActivity.this);
        dialogExpenseType.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogExpenseType.setContentView(R.layout.dialog_multiple_selection_sms_cust);
        dialogExpenseType.setTitle("Select Status");


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogExpenseType.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogExpenseType.getWindow().setAttributes(lp);

        LinearLayout ll_search = dialogExpenseType.findViewById(R.id.ll_search);
        ll_search.setVisibility(View.GONE);
        rv_select_customer = dialogExpenseType.findViewById(R.id.rv_select_customer);
        rv_select_customer.setLayoutManager(new LinearLayoutManager(SmsLogsActivity.this));
        ll_bottomDialog = dialogExpenseType.findViewById(R.id.ll_bottom);

        Button btn_select_item = dialogExpenseType.findViewById(R.id.btn_select_item);
        Button btn_clear_list = dialogExpenseType.findViewById(R.id.btn_clear_list);

        progress_bar = dialogExpenseType.findViewById(R.id.progress_bar);
        edit_search = dialogExpenseType.findViewById(R.id.edit_search);

        dialogExpenseType.setCanceledOnTouchOutside(true);
        dialogExpenseType.setCancelable(true);

        expAdapterStatus = new MultipleSelectionSmsStatusAdapter(getApplicationContext());
        expAdapterStatus.AddItems(lstClsBulkSMSLogsStatus);//FILL ADP

        expAdapterStatus.OnCharacterClick((clsCustomerMaster, position, holder) -> {

            flag = !holder.txt_label.isChecked();
            holder.txt_label.setChecked(flag);

            if (holder.txt_label.isChecked()) {

                holder.linear_layout.setBackgroundColor(Color.parseColor("#FFCDD2D2"));
                holder.ic_Check.setColorFilter(ContextCompat.getColor(getApplicationContext(),
                        R.color.red_dark), android.graphics.PorterDuff.Mode.MULTIPLY);
            } else {
                holder.ic_Check.setColorFilter(ContextCompat.getColor(getApplicationContext(),
                        R.color.tint_checkImageView), android.graphics.PorterDuff.Mode.MULTIPLY);
                holder.linear_layout.setBackgroundResource(0);
            }

            //UPDATE CHECKED LIST
            for (ClsBulkSMSLog obj : lstClsBulkSMSLogsStatus) {
                if (obj.getStatus().equalsIgnoreCase(clsCustomerMaster.getStatus())) {
                    obj.setSelected(flag);
                    lstClsBulkSMSLogsStatus.indexOf(obj);
                    break;
                }
            }

        });
        rv_select_customer.setAdapter(expAdapterStatus);

        btn_select_item.setOnClickListener(v -> {

            selectedSmsStatus = new ArrayList<>();

            for (ClsBulkSMSLog model : lstClsBulkSMSLogsStatus) {

                if (model.isSelected()) {
                    selectedSmsStatus.add("'".concat(model.getStatus()).concat("'"));
                }
            }

            txt_select_status.setText(TextUtils.join(",", selectedSmsStatus).toUpperCase().replace("'", ""));
            _smsStatus = TextUtils.join(",", selectedSmsStatus);

            dialogExpenseType.dismiss();
        });

        btn_clear_list.setOnClickListener(v -> {

            Check_UnCheckList(false);
            edit_search.setText("");
            txt_select_status.setText("");

        });

        dialogExpenseType.show();
    }


    private void Check_UnCheckList(boolean check_uncheck) {
        for (ClsBulkSMSLog Obj : lstClsBulkSMSLogsStatus) {
            Obj.setSelected(check_uncheck);
            lstClsBulkSMSLogsStatus.set(lstClsBulkSMSLogsStatus.indexOf(Obj), Obj);
        }
        expAdapterStatus.notifyDataSetChanged();
    }

    private void Check_UnCheckListForCust(boolean check_uncheck) {
        for (ClsBulkSMSLog Obj : lstClsBulkSMSLogs) {
            Obj.setSelected(check_uncheck);
            lstClsBulkSMSLogs.set(lstClsBulkSMSLogs.indexOf(Obj), Obj);
        }
        expAdapter.notifyDataSetChanged();
    }

    private void selectCustomer() {

        final Dialog dialogExpenseType = new Dialog(SmsLogsActivity.this);
        dialogExpenseType.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogExpenseType.setContentView(R.layout.dialog_multiple_selection_sms_cust);
        dialogExpenseType.setTitle("Select Customer");

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogExpenseType.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogExpenseType.getWindow().setAttributes(lp);

        LinearLayout ll_search = dialogExpenseType.findViewById(R.id.ll_search);
        ll_search.setVisibility(View.VISIBLE);
        rv_select_customer = dialogExpenseType.findViewById(R.id.rv_select_customer);
        rv_select_customer.setLayoutManager(new LinearLayoutManager(SmsLogsActivity.this));
        ll_bottomDialog = dialogExpenseType.findViewById(R.id.ll_bottom);

        Button btn_select_item = dialogExpenseType.findViewById(R.id.btn_select_item);
        Button btn_clear_list = dialogExpenseType.findViewById(R.id.btn_clear_list);

        progress_bar = dialogExpenseType.findViewById(R.id.progress_bar);
        edit_search = dialogExpenseType.findViewById(R.id.edit_search);

        dialogExpenseType.setCanceledOnTouchOutside(true);
        dialogExpenseType.setCancelable(true);

        expAdapter = new MultipleSelectionCustSmsAdapter(getApplicationContext());
        expAdapter.AddItems(lstClsBulkSMSLogs);//FILL ADP

        expAdapter.OnCharacterClick((clsCustomerMaster, position, holder) -> {

            boolean flag = !holder.txt_label.isChecked();
            holder.txt_label.setChecked(flag);

            if (holder.txt_label.isChecked()) {

                holder.linear_layout.setBackgroundColor(Color.parseColor("#FFCDD2D2"));
                holder.ic_Check.setColorFilter(ContextCompat.getColor(getApplicationContext(),
                        R.color.red_dark), android.graphics.PorterDuff.Mode.MULTIPLY);
            } else {
                holder.ic_Check.setColorFilter(ContextCompat.getColor(getApplicationContext(),
                        R.color.tint_checkImageView), android.graphics.PorterDuff.Mode.MULTIPLY);
                holder.linear_layout.setBackgroundResource(0);
            }

            //UPDATE CHECKED LIST
            for (ClsBulkSMSLog obj : lstClsBulkSMSLogs) {
                if (obj.getMobile().equalsIgnoreCase(clsCustomerMaster.getMobile())) {
                    obj.setSelected(flag);
                    lstClsBulkSMSLogs.indexOf(obj);
                    break;
                }
            }

        });
        rv_select_customer.setAdapter(expAdapter);

        edit_search.setOnEditorActionListener((s, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                img_clear.requestFocus();
                ClsGlobal.hideKeyboard(SmsLogsActivity.this);
                return true;
            }
            return true;
        });


        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String _filterTxt = "";

                if (s != null && s.length() != 0)
                    _filterTxt = s.toString();

                filterMain(_filterTxt, lstClsBulkSMSLogs);
            }
        });

        btn_select_item.setOnClickListener(v -> {

            selectedCustomerName = new ArrayList<>();
            selectedCustomerMob = new ArrayList<>();
            for (ClsBulkSMSLog model : lstClsBulkSMSLogs) {

                if (model.isSelected()) {
                    if (model.getCustomerName() != null && model.getCustomerName().equalsIgnoreCase("")) {

                        selectedCustomerMob.add("'".concat(model.getMobile()).concat("'"));
                    } else {
                        selectedCustomerName.add("'".concat(model.getCustomerName().toUpperCase())
                                .concat("'"));
                    }
                }
            }

            txt_customer_name.setText(TextUtils.join(",", selectedCustomerMob).replace("'", ",")
                    .concat(TextUtils.join(",", selectedCustomerName).toUpperCase()).replace("'", ""));

            _custName = TextUtils.join(",", selectedCustomerName);
            _custMob = TextUtils.join(",", selectedCustomerMob);


            Log.d("--_where--", "_custName: " + _custName);
            dialogExpenseType.dismiss();
        });

        btn_clear_list.setOnClickListener(v -> {

            Check_UnCheckListForCust(false);
            edit_search.setText("");
            txt_customer_name.setText("");

        });

        dialogExpenseType.show();
    }

    String _custSmsStatus = "";
    private LinearLayout ll_bottomDialog;


    List<ClsBulkSMSLog> filterList = new ArrayList();

    void filterMain(String text, List<ClsBulkSMSLog> lst) {

        filterList = StreamSupport.stream(lst)
                .filter(str -> str.getCustomerName().toLowerCase().contains(text.toLowerCase())
                        || str.getMobile().contains(text.toLowerCase()))
                .collect(Collectors.toList());


        if (text.isEmpty()) {
            expAdapter.AddItems(lstClsBulkSMSLogs);
        }

        //update recyclerview
        if (filterList.size() != 0) {
            txt_nodata.setVisibility(View.GONE);
            rv_select_customer.setVisibility(View.VISIBLE);
            expAdapter.AddItems(filterList);

        } else {
            txt_nodata.setVisibility(View.VISIBLE);
            rv_select_customer.setVisibility(View.GONE);
        }


    }

    private void ViewData(int currentPageNo) {

        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Void> asyncTask =
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        mSwipeRefreshLayout.setRefreshing(true);
                    }

                    @SuppressLint("WrongConstant")
                    @Override
                    protected Void doInBackground(Void... voids) {
                        Log.e("paging", "doInBackground:- " + currentPageNo);
                        db = openOrCreateDatabase(ClsGlobal.Database_Name,
                                Context.MODE_APPEND, null);
                        CheckBulkSmsStatus(SmsLogsActivity.this, db);


                        String _paging = "";

                        int pageSize = 30;
                        Log.e("paging", "Page no:- " + currentPageNo);
                        int skip = pageSize * (currentPageNo - 1);
                        Log.e("paging skip:-", String.valueOf(skip));

                        _paging = " LIMIT ".concat("" + skip).concat(", ")
                                .concat("" + pageSize);

                        String whereDoInBG = " AND [bulkID] = " +
                                clsSMSBulkMaster.getBulkID();

                        clsSummery = ClsBulkSMSLog.getSummery(clsSMSBulkMaster
                                .getBulkID(), db);

                        if (currentPageNo > 0) {

                            if (ClsBulkSMSLog.getList(_where.concat(whereDoInBG), _paging, db).size() != 0) {

                                list = ClsBulkSMSLog.getList(_where.concat(whereDoInBG),
                                        _paging, db);
                            }
                        } else {
                            if (ClsBulkSMSLog.getList(_where.concat(whereDoInBG), _paging, db).size() != 0) {
                                list = ClsBulkSMSLog.getList(_where.concat(whereDoInBG),
                                        _paging, db);
                            }

                        }
                        db.close();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);

//                        try {
//                            Thread.sleep(200);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }

                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }

                        progress_bar_layout.setVisibility(View.GONE);

                        if (list != null && list.size() > 0) {
                            txt_nodata.setVisibility(View.GONE);
                            tv_total_customer.setText(
                                    "CUSTOMERS: " + clsSummery.getTotalCustomer());
                            tv_credit_utilized.setText(
                                    "TOTAL CREDIT UTILIZED: " + clsSummery.getCreditUtilized());

                            tv_delivered.setText("DELIVERED: " + clsSummery.getDelivered());
                            tv_pending.setText("PENDING: " + clsSummery.getPending());
                            txt_failed.setText("FAILED: " + clsSummery.getFailed());
                            tv_dnd.setText("DND: " + clsSummery.getDND());

                            Log.e("--lstAsynctask--", "Upper: " + list.size());

                            adapter.AddItems(list);


                            Log.e("--lstAsynctask--", "Down: " + list.size());

                        } else {
                            txt_nodata.setVisibility(View.VISIBLE);
                        }
                    }

                };
        asyncTask.execute();

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
