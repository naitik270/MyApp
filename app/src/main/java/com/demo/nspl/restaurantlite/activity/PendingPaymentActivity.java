package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.AutoSuggestAdapter;
import com.demo.nspl.restaurantlite.Adapter.CustomAutoSuggestAdapter;
import com.demo.nspl.restaurantlite.Adapter.PendingPaymentsAdapter;
import com.demo.nspl.restaurantlite.Adapter.RecyclerItemClickListener;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsCustomerMaster;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster;
import com.google.gson.Gson;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.ConvertDDMMYY_to_YYYYMMDD;
import static com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster.getPendingPayments;

public class PendingPaymentActivity extends AppCompatActivity {

    private RecyclerView rv;
    private TextView empty_title_text, tv_total_amt,
            tv_selected_count, tv_total_item;
    private PendingPaymentsAdapter mAdapter;
    private Toolbar toolbar;
    private ProgressBar progress_bar;
    private String _FromDueDate = "", _ToDueDate = "", _FromBillDate = "",
            _ToBillDate = "", Spinner_Selected = "",
            auto_customer_str = "", auto_company_name_str = "", edt_amount_str = "";
    private Calendar c;
    private List<ClsCustomerMaster> customerList = new ArrayList<>();
    private List<String> CompanyList = new ArrayList<>();
    private CustomAutoSuggestAdapter mAutoCustomerAdapter;
    private int mYear, mMonth, mDay;
    private Spinner spinner_amount_filter;
    private Dictionary<String, String> filter_Operation = new Hashtable<>();
    TextView from_due_date, to_due_date, from_billDate, to_bill_date;
    EditText edt_amount;
    private int listTotalSize = 0;
    private boolean isMultiSelect = false;
    AutoCompleteTextView auto_customer, auto_company_name;
    private LinearLayout ll_status_bar;
    private ImageView iv_send_sms,iv_clear_all;
    private List<Integer> selectedIds = new ArrayList<>();
    private AutoSuggestAdapter mAutoCompanyAdapter;
    private MenuItem item_filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_payment);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (!(Thread.getDefaultUncaughtExceptionHandler()
                instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(
                    new ErrorExceptionHandler(
                            PendingPaymentActivity.this,
                            "PendingPaymentActivity"));
        }

        rv = findViewById(R.id.rv);
        progress_bar = findViewById(R.id.progress_bar);
        empty_title_text = findViewById(R.id.empty_title_text);
        ll_status_bar = findViewById(R.id.ll_status_bar);
        tv_total_amt = findViewById(R.id.tv_total_amt);
        tv_selected_count = findViewById(R.id.tv_selected_count);
        tv_total_item = findViewById(R.id.tv_total_item);
        iv_send_sms = findViewById(R.id.iv_send_sms);
        iv_clear_all = findViewById(R.id.iv_clear_all);

        mAdapter = new PendingPaymentsAdapter(PendingPaymentActivity.this);
        rv.setLayoutManager(new LinearLayoutManager(PendingPaymentActivity.this));
        rv.setAdapter(mAdapter);

//        mAdapter.SetOnClickListener(new OnItemClickListenerClsOrder() {
//            @Override
//            public void OnClick(ClsInventoryOrderMaster clsInventoryOrderMaster) {
//
//            }
//        });

        filter_Operation.put(getString(R.string.LessThan), "<");
        filter_Operation.put(getString(R.string.GreaterThan), ">");
        filter_Operation.put(getString(R.string.LessThanEqualTo), "<=");
        filter_Operation.put(getString(R.string.GreaterThanEqualTo), ">=");


        rv.addOnItemTouchListener(new RecyclerItemClickListener(this, rv,
                new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect){
                    //if multiple selection is enabled then select item
                    // on single click else perform normal click on item.
                    multiSelect(position);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect){
                    selectedIds = new ArrayList<>();
                    isMultiSelect = true;

                    // Show status bar setVisibility to VISIBLE.
                    ll_status_bar.setVisibility(View.VISIBLE);
                    // hide the menu.
                    item_filter.setVisible(false);

                }

                multiSelect(position);
            }
        }));

//        mAdapter.SetOnClickListener(clsInventoryOrderMaster -> {
//
//        });




        iv_send_sms.setOnClickListener(v -> {

            startActivity(new Intent(PendingPaymentActivity.this,
                    SendSmsPreviewActivity.class)
                    .putExtra("source","PendingPayment")
                    .putExtra("List",(Serializable) mAdapter.getAdapterList()));

            ClearAllSelection();
        });

        iv_clear_all.setOnClickListener(v -> {
            ClearAllSelection();
        });

        ViewData("");
    }

    private void ClearAllSelection(){

        if(item_filter != null && mAdapter != null){
            isMultiSelect = false;
            selectedIds = new ArrayList<>();

            // Set selection bar Visibility Gone.
            ll_status_bar.setVisibility(View.GONE);

            mAdapter.setSelectedIds(new ArrayList<Integer>());

            // Set menu Visible.
            item_filter.setVisible(true);

            // Clear all selection from list.
            for (ClsInventoryOrderMaster current : mAdapter.getAdapterList()){
                if (current.isSelected()){
                    current.setSelected(false);
                    mAdapter.getAdapterList().set(mAdapter.getAdapterList()
                            .indexOf(current),current);
                }
            }

        }

    }

    @SuppressLint("StaticFieldLeak")
    private void ViewData(String where) {

        new AsyncTask<Void, Void, List<ClsInventoryOrderMaster>>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                rv.setVisibility(View.GONE);
                progress_bar.setVisibility(View.VISIBLE);
                mAdapter.Clear();
            }

            @Override
            protected List<ClsInventoryOrderMaster> doInBackground(Void... voids) {
                customerList = ClsCustomerMaster
                        .getAsyncTaskListCustomers("", PendingPaymentActivity.this);

                CompanyList = StreamSupport.stream(customerList)
                        .map(ClsCustomerMaster::getCompany_Name)
                        .distinct().collect(Collectors.toList());


                Gson gson2 = new Gson();
                String jsonInString2 = gson2.toJson(CompanyList);
                Log.e("Check", "Customers:--- " + jsonInString2);

                return getPendingPayments(" AND [AdjumentAmount] > 0 " + where,
                        PendingPaymentActivity.this);
            }


            @Override
            protected void onPostExecute(List<ClsInventoryOrderMaster> list) {
                super.onPostExecute(list);

                if (progress_bar.getVisibility() == View.VISIBLE) {
                    progress_bar.setVisibility(View.GONE);

                }

                listTotalSize = list.size();

                if (list.size() > 0) {
                    empty_title_text.setVisibility(View.GONE);
                    rv.setVisibility(View.VISIBLE);
                    mAdapter.AddItems(list);
                } else {
                    empty_title_text.setVisibility(View.VISIBLE);
                }

                mAutoCustomerAdapter = new CustomAutoSuggestAdapter(PendingPaymentActivity.this,
                        (ArrayList<ClsCustomerMaster>) customerList);


                mAutoCompanyAdapter = new AutoSuggestAdapter(PendingPaymentActivity.this,
                        android.R.layout.simple_list_item_1, CompanyList);

            }
        }.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pending_payments, menu);

         item_filter = menu.findItem(R.id.filter);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

            finish();
        }

        if (id == R.id.filter) {
            applyFilters();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ClickableViewAccessibility")
    void applyFilters() {

        Dialog dialogPaymentFilter = new Dialog(PendingPaymentActivity.this);
        dialogPaymentFilter.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPaymentFilter.setContentView(R.layout.dialog_filter_pending_payments);
        Objects.requireNonNull(dialogPaymentFilter.getWindow())
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogPaymentFilter.setCancelable(true);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogPaymentFilter.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        from_due_date = dialogPaymentFilter.findViewById(R.id.from_due_date);
        to_due_date = dialogPaymentFilter.findViewById(R.id.to_due_date);
        from_billDate = dialogPaymentFilter.findViewById(R.id.from_billDate);
        to_bill_date = dialogPaymentFilter.findViewById(R.id.to_bill_date);
        auto_customer = dialogPaymentFilter.findViewById(R.id.auto_customer);
        auto_company_name = dialogPaymentFilter.findViewById(R.id.auto_company_name);

        edt_amount = dialogPaymentFilter.findViewById(R.id.edt_amount);
        spinner_amount_filter = dialogPaymentFilter.findViewById(R.id.spinner_amount_filter);

        Button btn_search = dialogPaymentFilter.findViewById(R.id.btn_search);
        Button btn_clear = dialogPaymentFilter.findViewById(R.id.btn_clear);

        ImageButton bt_close = dialogPaymentFilter.findViewById(R.id.bt_close);
        ImageButton clear_due_date = dialogPaymentFilter.findViewById(R.id.clear_due_date);
        ImageButton clear_Billdate = dialogPaymentFilter.findViewById(R.id.clear_Billdate);
        ImageButton iv_clear_customer = dialogPaymentFilter.findViewById(R.id.iv_clear_customer);
        ImageButton iv_clear_company = dialogPaymentFilter.findViewById(R.id.iv_clear_company);
        ImageButton iv_clear_amt = dialogPaymentFilter.findViewById(R.id.iv_clear_amt);


        clear_due_date.setOnClickListener(v -> {
            _FromDueDate = "";
            _ToDueDate = "";
            from_due_date.setText("");
            to_due_date.setText("");
        });

        clear_Billdate.setOnClickListener(v -> {
            _FromBillDate = "";
            _ToBillDate = "";
            from_billDate.setText("");
            to_bill_date.setText("");
        });

        iv_clear_customer.setOnClickListener(v -> {
            auto_customer_str = "";
            auto_customer.setText("");
        });

        iv_clear_company.setOnClickListener(v -> {
            auto_company_name_str = "";
            auto_company_name.setText("");
        });

        iv_clear_amt.setOnClickListener(v -> {
            edt_amount_str = "";
            Spinner_Selected = "";
            edt_amount.setText("");
            spinner_amount_filter.setSelection(Arrays.asList(getResources()
                    .getStringArray(R.array.array_type_options))
                    .indexOf(getString(R.string.none)));

        });

        // select all Previous search.
        fillFilterDialog();

        // Open auto on click.
        auto_company_name.setOnTouchListener((v, event) -> {
            auto_company_name.showDropDown();
            return false;
        });

        // Open auto on click.
        auto_customer.setOnTouchListener((v, event) -> {
            auto_customer.showDropDown();
            return false;
        });

        auto_customer.setAdapter(mAutoCustomerAdapter);
        auto_company_name.setAdapter(mAutoCompanyAdapter);


        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        btn_clear.setOnClickListener(v -> {
            ClearQueryFilterDialog();
        });

        bt_close.setOnClickListener(v -> {
            dialogPaymentFilter.cancel();
        });

        SetupSpinner();

        from_due_date.setOnClickListener(v -> {
            DatePickerDialog dpd = new DatePickerDialog(PendingPaymentActivity.this,
                    (view1, year, month, day) -> {
                        c.set(year, month, day);

//                        _FromDueDate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
                        _FromDueDate = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
//                        from_due_date.setText(ClsGlobal.getChangeDateFormat(_FromDueDate));
                        from_due_date.setText(_FromDueDate);

                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                    }, mYear, mMonth, mDay);

//            dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
//
//            Calendar d = Calendar.getInstance();
//
//            dpd.updateDate(d.get(Calendar.YEAR),
//                    d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH));
            dpd.show();

        });

        to_due_date.setOnClickListener(v -> {

            DatePickerDialog dpd = new DatePickerDialog(PendingPaymentActivity.this,
                    (view1, year, month, day) -> {
                        c.set(year, month, day);

                        _ToDueDate = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
//                        to_due_date.setText(ClsGlobal.getChangeDateFormat(_ToDueDate));
                        to_due_date.setText(_ToDueDate);

                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                    }, mYear, mMonth, mDay);

//            dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
//
//            Calendar d = Calendar.getInstance();
//
//            dpd.updateDate(d.get(Calendar.YEAR),
//                    d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH));
            dpd.show();

        });

        from_billDate.setOnClickListener(v -> {

            DatePickerDialog dpd = new DatePickerDialog(PendingPaymentActivity.this,
                    (view1, year, month, day) -> {
                        c.set(year, month, day);

                        _FromBillDate = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
//                        from_billDate.setText(ClsGlobal.getChangeDateFormat(_FromBillDate));
                        from_billDate.setText(_FromBillDate);

                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                    }, mYear, mMonth, mDay);

//            dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
//
//            Calendar d = Calendar.getInstance();
//
//            dpd.updateDate(d.get(Calendar.YEAR),
//                    d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH));
            dpd.show();

        });

        to_bill_date.setOnClickListener(v -> {

            DatePickerDialog dpd = new DatePickerDialog(PendingPaymentActivity.this,
                    (view1, year, month, day) -> {
                        c.set(year, month, day);

                        _ToBillDate = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
//                        to_bill_date.setText(ClsGlobal.getChangeDateFormat(_ToBillDate));
                        to_bill_date.setText(_ToBillDate);

                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                    }, mYear, mMonth, mDay);

//            dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
//
//            Calendar d = Calendar.getInstance();
//
//            dpd.updateDate(d.get(Calendar.YEAR),
//                    d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH));
            dpd.show();

        });

        btn_search.setOnClickListener(v -> {


            StringBuilder whereStringBuilder = new StringBuilder();

            auto_customer_str = auto_customer.getText().toString();
            auto_company_name_str = auto_company_name.getText().toString();
            edt_amount_str = edt_amount.getText().toString();


            if (!auto_customer_str.equalsIgnoreCase("")) {
                whereStringBuilder.append(getAndORForQuery(whereStringBuilder)
                        .concat(" [CustomerName] LIKE '% ")
                        .concat(auto_customer.getText().toString()).concat("%'"));

//                _where +=  _where.concat(" and [CustomerName] LIKE '%")
//                        .concat(auto_customer.getText().toString()).concat("%'");
            }


            if (!auto_company_name_str.equalsIgnoreCase("")) {
                Log.e("Check", "auto_company_name_str: " + auto_company_name_str);


                whereStringBuilder.append(getAndORForQuery(whereStringBuilder).concat(" [CompanyName] LIKE ")
                        .concat(auto_company_name.getText().toString())
                        .concat("%'"));

//                _where += _where.concat(" or [CompanyName] LIKE '%")
//                        .concat(auto_company_name.getText().toString())
//                        .concat("%'");


            }


            // means [DueDate] greater than selected date in _FromDueDate.
            if (!_FromDueDate.equalsIgnoreCase("")
                    && _ToDueDate.equalsIgnoreCase("")) {

                whereStringBuilder.append(getAndORForQuery(whereStringBuilder)
                        .concat(" [DueDate] >= '")
                        .concat(ConvertDDMMYY_to_YYYYMMDD(_FromDueDate)).concat("'"));
//                _where += _where.concat(" or [DueDate] >= '" +ConvertDDMMYY_to_YYYYMMDD(_FromDueDate)  + "'");
            }


            // means [DueDate] less than selected date in _ToDueDate.
            if (!_ToDueDate.equalsIgnoreCase("")
                    && _FromDueDate.equalsIgnoreCase("")) {
                whereStringBuilder.append(getAndORForQuery(whereStringBuilder).concat("  [DueDate] <= '")
                        .concat(ConvertDDMMYY_to_YYYYMMDD(_ToDueDate) + "'"));

//                _where += _where.concat(" or [DueDate] <= '" +ConvertDDMMYY_to_YYYYMMDD(_ToDueDate)  + "'");
            }

            // Query between _FromDueDate _ToDueDate in column DueDate.
            if (!_FromDueDate.equalsIgnoreCase("")
                    && !_ToDueDate.equalsIgnoreCase("")) {
                whereStringBuilder.append(getAndORForQuery(whereStringBuilder).concat(" [DueDate] BETWEEN ") +
                        "('" + ConvertDDMMYY_to_YYYYMMDD(_FromDueDate) + "') AND ('" +
                        ConvertDDMMYY_to_YYYYMMDD(_ToDueDate) + "')");

//                _where += _where.concat(" or [DueDate] BETWEEN " +
//                        "('" + ConvertDDMMYY_to_YYYYMMDD(_FromDueDate) + "') AND ('" +
//                        ConvertDDMMYY_to_YYYYMMDD(_ToDueDate)  + "')");
            }

            // means [BillDate] greater than selected date in _FromDueDate.
            if (!_FromBillDate.equalsIgnoreCase("")
                    && _ToBillDate.equalsIgnoreCase("")) {
                whereStringBuilder.append(getAndORForQuery(whereStringBuilder)
                        .concat(" [BillDate] >= '")
                        + ConvertDDMMYY_to_YYYYMMDD(_FromBillDate) + "'");

//                _where += _where.concat(" or [BillDate] >= '"
//                        +ConvertDDMMYY_to_YYYYMMDD(_FromBillDate)  + "'");
            }

            // means [BillDate] less than selected date in _ToBillDate.
            if (!_ToBillDate.equalsIgnoreCase("")
                    && _FromBillDate.equalsIgnoreCase("")) {
                whereStringBuilder.append(getAndORForQuery(whereStringBuilder).concat("  [BillDate] <= '")
                        + ConvertDDMMYY_to_YYYYMMDD(_ToBillDate) + "'");

//                _where += _where.concat(" or [BillDate] <= '" + ConvertDDMMYY_to_YYYYMMDD(_ToBillDate)+ "'");
            }

            if (!_FromBillDate.equalsIgnoreCase("")
                    && !_ToBillDate.equalsIgnoreCase("")) {

                whereStringBuilder.append(getAndORForQuery(whereStringBuilder) +
                        " [BillDate] BETWEEN ('" + ConvertDDMMYY_to_YYYYMMDD(_FromBillDate) + "') AND ('"
                        + ConvertDDMMYY_to_YYYYMMDD(_ToBillDate) + "')");
//                _where += _where.concat(" or [BillDate] BETWEEN " +
//                        "('" + ConvertDDMMYY_to_YYYYMMDD(_FromBillDate) + "') AND ('"
//                        + ConvertDDMMYY_to_YYYYMMDD(_ToBillDate) + "')");
            }

            if (!edt_amount_str.equalsIgnoreCase("")) {
                Log.e("Check", "edt_amount_str: " + auto_company_name_str);

                whereStringBuilder.append(getAndORForQuery(whereStringBuilder)
                        .concat(" [AdjumentAmount] = ") + edt_amount_str);
//                _where += _where.concat(" or [AdjumentAmount] = " + edt_amount_str);

                Log.e("Check", "edt_amount_str after : " + auto_company_name_str);

            }

            if (!Spinner_Selected.equalsIgnoreCase(getString(R.string.none)) &&
                    !edt_amount_str.equalsIgnoreCase("")) {
                whereStringBuilder.append(getAndORForQuery(whereStringBuilder).concat("  [AdjumentAmount] ")
                        .concat(filter_Operation.get(Spinner_Selected))
                        .concat(" " + edt_amount_str));
//                _where += _where.concat(" or [AdjumentAmount] " + filter_Operation.get(Spinner_Selected)
//                        + " " + edt_amount_str);
            }


            Log.e("Check", "Where: " + whereStringBuilder.toString());
            ViewData(whereStringBuilder.toString());

            dialogPaymentFilter.cancel();


        });


        dialogPaymentFilter.show();
        dialogPaymentFilter.getWindow().setAttributes(lp);
    }


    private void fillFilterDialog() {
        auto_customer.setText(auto_customer_str);
        auto_company_name.setText(auto_company_name_str);
        edt_amount.setText(edt_amount_str);
        from_due_date.setText(_FromDueDate);
        to_due_date.setText(_ToDueDate);
        from_billDate.setText(_FromBillDate);
        to_bill_date.setText(_ToBillDate);
        spinner_amount_filter.setSelection(Arrays.asList(getResources()
                .getStringArray(R.array.array_type_options))
                .indexOf(Spinner_Selected));
    }

    private void ClearQueryFilterDialog() {

        auto_customer_str = "";
        auto_company_name_str = "";
        edt_amount_str = "";
        _FromDueDate = "";
        _ToDueDate = "";
        _FromBillDate = "";
        _ToBillDate = "";

        auto_customer.setText("");
        auto_company_name.setText("");
        edt_amount.setText("");
        from_due_date.setText("");
        to_due_date.setText("");
        from_billDate.setText("");
        to_bill_date.setText("");

        spinner_amount_filter.setSelection(Arrays.asList(getResources()
                .getStringArray(R.array.array_type_options))
                .indexOf(getString(R.string.none)));

    }


    private void SetupSpinner() {

        ArrayAdapter<CharSequence> TypeSpinnerAdapter =
                ArrayAdapter.createFromResource(PendingPaymentActivity.this,
                        R.array.array_amount_filter,
                        android.R.layout.simple_spinner_item);

        TypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spinner_amount_filter.setAdapter(TypeSpinnerAdapter);
        spinner_amount_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {

                    if (selection.equals(getString(R.string.none))) {
                        Spinner_Selected = getString(R.string.none);
                    } else if (selection.equals(getString(R.string.LessThan))) {
                        Spinner_Selected = getString(R.string.LessThan);
                    } else if (selection.equals(getString(R.string.GreaterThan))) {
                        Spinner_Selected = getString(R.string.GreaterThan);
                    } else if (selection.equals(getString(R.string.LessThanEqualTo))) {
                        Spinner_Selected = getString(R.string.LessThanEqualTo);
                    } else if (selection.equals(getString(R.string.GreaterThanEqualTo))) {
                        Spinner_Selected = getString(R.string.GreaterThanEqualTo);
                    } else {
                        Spinner_Selected = getString(R.string.none);
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Spinner_Selected = getString(R.string.none);
            }
        });
    }


    private String getAndORForQuery(StringBuilder stringBuilder) {
        return stringBuilder.toString()
                .equalsIgnoreCase("") ? " And " : " or ";
    }


    @SuppressLint("SetTextI18n")
    private void multiSelect(int position) {
        ClsInventoryOrderMaster data = mAdapter.getItem(position);
        if (data != null) {

            if(!data.isSelected()){
                // if current selected item is not selected than change
                // selected field to true and update obj to list.
                data.setSelected(true);
                mAdapter.getAdapterList().set(mAdapter.getAdapterList().indexOf(data),data);
            }else {
                // if current selected item is selected than change
                // selected field to false and update obj to list.
                data.setSelected(false);
                mAdapter.getAdapterList().set(mAdapter.getAdapterList().indexOf(data),data);
            }


            if (selectedIds.contains(data.getOrderID())) {
                selectedIds.remove(Integer.valueOf(data.getOrderID()));
            }else{
                selectedIds.add(data.getOrderID());
            }

            if (selectedIds.size() > 0) {
                // update the counter and total amt to the status bar.
                tv_selected_count.setText(String.valueOf(selectedIds.size()));
                tv_total_item.setText("/" + listTotalSize);


                tv_total_amt.setText("Total Amount: " + ClsGlobal.round(StreamSupport
                        .stream(mAdapter.getAdapterList())
                        .filter(ClsInventoryOrderMaster::isSelected)
                        .mapToDouble(ClsInventoryOrderMaster::getAdjumentAmount)
                        .sum(),2));

            } else {
                // unselect all.
                // hide status_bar.
                ll_status_bar.setVisibility(View.GONE);
                item_filter.setVisible(true);
                ClearAllSelection();
            }

            mAdapter.notifyDataSetChanged();


        }
    }



}


