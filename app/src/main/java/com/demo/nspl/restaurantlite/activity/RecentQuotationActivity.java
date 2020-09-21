package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
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
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.RecentQuotationAdapter;
import com.demo.nspl.restaurantlite.AsyncTask.QuotationListAsyncTask;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster;
import com.demo.nspl.restaurantlite.classes.ClsQuotationMaster;
import com.demo.nspl.restaurantlite.classes.ClsQuotationOrderDetail;
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

public class RecentQuotationActivity extends AppCompatActivity {

    private RecyclerView mRv;
    private List<ClsQuotationMaster> lstClsQuotationMasters = new ArrayList<>();
    private List<ClsQuotationOrderDetail> lstClsQuotationOrderDetails = new ArrayList<>();
    private Toolbar toolbar;
    private TextView txt_no_data, txt_title, txt_sub_title;

    SharedPreferences mPreferencesDefault;
    private static final String mPreferncesName = "MyPerfernces";
    String editSource = "", saleMode = "", quotType = "", fragMode = "";
    int _month = 0, year = 0;
    ProgressBar progress_bar;
    private RecentQuotationAdapter mAdapter;
    FloatingActionButton fab_filter;
    HorizontalScrollView hs_ckb;

    RadioGroup rb_filter;
    RadioButton rb_all;
    RadioButton rb_pending;
    RadioButton rb_invoice_generate;
    RadioButton rb_expired;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_quotation);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "RecentQuotationActivity"));
        }

        main();
    }


    @SuppressLint("SetTextI18n")
    private void main() {

        saleMode = getIntent().getStringExtra("saleMode");
        editSource = getIntent().getStringExtra("editSource");
        quotType = getIntent().getStringExtra("quotType");
        fragMode = getIntent().getStringExtra("fragMode");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mRv = findViewById(R.id.rv);
        mRv.setLayoutManager(new LinearLayoutManager(this));

        rb_filter = findViewById(R.id.rb_filter);
        rb_all = findViewById(R.id.rb_all);
        rb_pending = findViewById(R.id.rb_pending);
        rb_invoice_generate = findViewById(R.id.rb_invoice_generate);
        rb_expired = findViewById(R.id.rb_expired);

        hs_ckb = findViewById(R.id.hs_ckb);

        txt_sub_title = findViewById(R.id.txt_sub_title);
        txt_title = findViewById(R.id.txt_title);
        txt_no_data = findViewById(R.id.txt_no_data);

        progress_bar = findViewById(R.id.progress_bar);
        fab_filter = findViewById(R.id.fab_filter);
        fab_filter.setColorFilter(Color.WHITE);


        if (fragMode.equalsIgnoreCase("QuotationReportFragment")) {
            fab_filter.setVisibility(View.VISIBLE);
            _month = getIntent().getIntExtra("_month", 0);
            year = getIntent().getIntExtra("Year", 0);
            monthYear = ClsGlobal.getMonthName(_month).concat(" " + year);

            fillCustomerList();

            String getMonthYear = ClsGlobal.getDayMonth(
                    ClsGlobal.getMonthName(_month)
                            + " " + year);

            Log.e("--RecentQuotation--", "getMonthYear: " + getMonthYear);

            fab_filter.setOnClickListener(v1 -> {
                applyFilters();
            });

        } else {
            fab_filter.setVisibility(View.GONE);

        }

        txt_sub_title.setText("Last 50 quotation only");
        if (editSource != null && !editSource.isEmpty()) {
            txt_title.setText(editSource);
            txt_sub_title.setVisibility(View.GONE);
        } else {
            txt_title.setText("Recent Quotation");
            txt_sub_title.setVisibility(View.VISIBLE);
        }

        mPreferencesDefault = getSharedPreferences(mPreferncesName, MODE_PRIVATE);
        if (mPreferencesDefault.getString("WhatsApp Default App", null) == null) {
            SaveData_SharedPreferences("WhatsApp");
        }

        rb_filter.setOnCheckedChangeListener((group, checkedId) -> {

            RadioButton rb = group.findViewById(checkedId);
            String getType = rb.getText().toString();

            if (getType.equalsIgnoreCase("PENDING")) {
                _whereFilter = "";
                _whereFilter = _whereFilter.concat(" AND [Status] = 'PENDING'");
            } else if (getType.equalsIgnoreCase("INVOICE GENERATED")) {
                _whereFilter = "";
                _whereFilter = _whereFilter.concat(" AND [Status] = 'INVOICE GENERATED'");
            } else if (getType.equalsIgnoreCase("EXPIRED")) {
                _whereFilter = "";
                _whereFilter = _whereFilter.concat(" AND [Status] = 'Expired'");
            } else if (getType.equalsIgnoreCase("ALL")) {
                _whereFilter = "";
            } else {
                _whereFilter = "";
            }
            Log.d("--Filters--", "_whereFilter: " +_whereFilter);

            loadData(_whereFilter);

        });


        mAdapter = new RecentQuotationAdapter(this);
        setRecentQuotationClick();
        mRv.setAdapter(mAdapter);
        loadData("");
    }


    TextView txt_quotation_date, txt_exp_date, txt_customer_name;
    int mYear, mMonth, mDay;
    Calendar c;
    String _quotationDate = "";
    String _expDate = "";
    String _quotationNo = "";
    String _whereFilter = "";
    String monthYear = "";
    ImageButton iv_clear_vendor, iv_clear_bill_no, clear_date, iv_clear_mobile_no;

    EditText edt_mobile_no, edt_quot_no;
    ArrayList<String> selectedCustomerName = new ArrayList<>();

    String _custMobile = "";
    String _customerMobile = "";


    Button btn_search, btn_clear;


    private void applyFilters() {
        Dialog dialog = new Dialog(RecentQuotationActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_filter_quotation);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        txt_quotation_date = dialog.findViewById(R.id.txt_quotation_date);
        txt_exp_date = dialog.findViewById(R.id.txt_exp_date);
        clear_date = dialog.findViewById(R.id.clear_date);
        iv_clear_bill_no = dialog.findViewById(R.id.iv_clear_bill_no);
        iv_clear_mobile_no = dialog.findViewById(R.id.iv_clear_mobile_no);

        txt_quotation_date.setText(ClsGlobal.getQuotationDate(_quotationDate));
        txt_exp_date.setText(ClsGlobal.getChangeDateFormat(_expDate));

        txt_customer_name = dialog.findViewById(R.id.txt_customer_name);
        iv_clear_vendor = dialog.findViewById(R.id.iv_clear_vendor);

        edt_mobile_no = dialog.findViewById(R.id.edt_mobile_no);
        edt_quot_no = dialog.findViewById(R.id.edt_quot_no);
        btn_search = dialog.findViewById(R.id.btn_search);
        btn_clear = dialog.findViewById(R.id.btn_clear);
        ImageButton bt_close = dialog.findViewById(R.id.bt_close);

        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        txt_quotation_date.setOnClickListener(view -> {
            quotationDateFromMonth();
        });

        txt_exp_date.setOnClickListener(view -> {
            expDateFromMonth();
        });

        iv_clear_vendor.setOnClickListener(view -> {

            txt_customer_name.setText("");
            _custMobile = "";
            selectedCustomerName.clear();

        });

        clear_date.setOnClickListener(view -> {
            _quotationDate = "";
            _expDate = "";
            txt_quotation_date.setText("");
            txt_exp_date.setText("");
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

        edt_quot_no.setText(_quotationNo);
        edt_mobile_no.setText(_customerMobile);
        txt_quotation_date.setText(ClsGlobal.getChangeDateFormat(_quotationDate));
        txt_exp_date.setText(ClsGlobal.getChangeDateFormat(_expDate));

        btn_search.setOnClickListener(view -> {


            _quotationNo = edt_quot_no.getText().toString();
            _customerMobile = edt_mobile_no.getText().toString();

            _whereFilter = "";
            filtersCondition();

            loadData(_whereFilter);


            dialog.dismiss();
            dialog.hide();

        });

        iv_clear_mobile_no.setOnClickListener(view -> {
            edt_mobile_no.setText("");
            _customerMobile = "";
        });

        iv_clear_bill_no.setOnClickListener(view -> {
            edt_quot_no.setText("");
            _quotationNo = "";
        });


        btn_clear.setOnClickListener(view -> {

            _whereFilter = "";

//Date selection is Clear & reset.
            txt_quotation_date.setText("");
            txt_exp_date.setText("");
            _quotationDate = "";
            _expDate = "";

//Customer Name Selection is Clear & reset.
            txt_customer_name.setText("");
            _custMobile = "";
            selectedCustomerName.clear();

//Customer Mobile is Clear & reset.
            edt_mobile_no.setText("");
            _customerMobile = "";

//QuotationNo No is Clear & reset.
            edt_quot_no.setText("");
            _quotationNo = "";

        });

        bt_close.setOnClickListener(view -> {
            dialog.dismiss();
            dialog.hide();
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void filtersCondition() {

        if (!_quotationDate.equalsIgnoreCase("") &&
                !_expDate.equalsIgnoreCase("")) {

            _whereFilter = _whereFilter.concat(" AND DATE([QuotationDate]) between "
                    .concat("('".concat(_quotationDate).concat("')"))
                    .concat(" AND ")
                    .concat("('".concat(_expDate).concat("')")));

        } else if (!_quotationDate.equalsIgnoreCase("")) {

            _whereFilter = _whereFilter.concat(" AND DATE([QuotationDate]) = ".concat("('"
                    .concat(_quotationDate).concat("')")));

        } else if (!_expDate.equalsIgnoreCase("")) {

            _whereFilter = _whereFilter.concat(" AND [ValidUptoDate] = ".concat("('"
                    .concat(_expDate).concat("')")));
        }

        if (!txt_customer_name.getText().toString().equalsIgnoreCase("")) {

            _whereFilter = _whereFilter.concat(" AND [MobileNo] IN ")
                    .concat("(")
                    .concat(_custMobile)
                    .concat(")");
        }

        if (!_customerMobile.equalsIgnoreCase("")) {

            _whereFilter = _whereFilter.concat(" AND [MobileNo] LIKE ")
                    .concat("'")
                    .concat(edt_mobile_no.getText().toString().trim())
                    .concat("'");
        }

        if (!_quotationNo.equalsIgnoreCase("")) {

            _whereFilter = _whereFilter.concat(" AND [QuotationNo] LIKE ")
                    .concat("'")
                    .concat(edt_quot_no.getText().toString().trim())
                    .concat("'");
        }

        Log.e("--QuotationList--", "_whereFilter: " + _whereFilter);



        loadData(_whereFilter);
    }

    void loadData(String _where) {


        Log.e("--QuotationList--", "year: " + year);
        Log.e("--QuotationList--", "_month: " + _month);
        Log.e("--QuotationList--", "saleMode: " + saleMode);


        new QuotationListAsyncTask(_where,
                txt_no_data,
                RecentQuotationActivity.this, mAdapter,
                progress_bar, year, _month, saleMode, lstClsQuotationMasters, mRv).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData("");

        ClsGlobal.CancelAllNotification(RecentQuotationActivity.this);

    }

    private void selectCustomer() {

        final Dialog dialogCustomer = new Dialog(RecentQuotationActivity.this);
        dialogCustomer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCustomer.setContentView(R.layout.dialog_multiple_selection);
        dialogCustomer.setTitle("SELECT CUSTOMER");


        ListView listView = dialogCustomer.findViewById(R.id.listview);
        Button btn_Select = dialogCustomer.findViewById(R.id.button);

        dialogCustomer.setCanceledOnTouchOutside(true);
        dialogCustomer.setCancelable(true);


        final AdapterCustomers expAdapter = new AdapterCustomers(RecentQuotationActivity.this,
                (ArrayList<ClsQuotationMaster>) lstCustomerMasterList);
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
//            txt_customer_name.setTag(TextUtils.join(",", selectedCustomerNumber));


            _custMobile = TextUtils.join(",", selectedCustomerNumber);

            dialogCustomer.dismiss();
        });
        dialogCustomer.show();
    }

    private List<ClsQuotationMaster> lstCustomerMasterList;

    private void fillCustomerList() {

        String _where = " AND strftime('%m %Y',[QuotationDate]) ="
                .concat("'").concat(ClsGlobal.getMonthYearDigit(monthYear)).concat("'");

        lstCustomerMasterList = new ArrayList<>();
        lstCustomerMasterList = new ClsQuotationMaster().getCustomerListFromDate(_where, RecentQuotationActivity.this);

    }


    public class AdapterCustomers extends BaseAdapter {

        private ArrayList<ClsQuotationMaster> data = new ArrayList<ClsQuotationMaster>();
        private Context context;
        private boolean isSelected[];

        public AdapterCustomers(Context context, ArrayList<ClsQuotationMaster> data) {
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

            holder.row_relative_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // set the check text view
                    boolean flag = holder.checkedTextView.isChecked();
                    holder.checkedTextView.setChecked(!flag);
                    isSelected[position] = !isSelected[position];


                    if (holder.checkedTextView.isChecked()) {
                        holder.row_relative_layout.setBackgroundColor(Color.parseColor("#FFCDD2D2"));
                    } else {
                        holder.row_relative_layout.setBackgroundResource(0);
                    }
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

    public void quotationDateFromMonth() {

        try {
            String dateStr = ClsGlobal.getFirstDateOfMonth(monthYear);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dateObj = sdf.parse(dateStr);

            String _endDate = ClsGlobal.getLastDay(dateObj);
            SimpleDateFormat sdfEndDate = new SimpleDateFormat("dd/MM/yyyy");
            Date lastDateObj = sdfEndDate.parse(_endDate);

            long startDate = dateObj.getTime();
            long endDate = lastDateObj.getTime();

            DatePickerDialog dpd = new DatePickerDialog(RecentQuotationActivity.this,
                    (view12, year, month, day) -> {
                        c.set(year, month, day);
                        _quotationDate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());

                        Log.e("--Date--", "QuotationDate: " + _quotationDate);

                        txt_quotation_date.setText(ClsGlobal.getChangeDateFormat(_quotationDate));

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

    public void expDateFromMonth() {

        try {
            String dateStr = ClsGlobal.getFirstDateOfMonth(monthYear);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dateObj = sdf.parse(dateStr);

            String _endDate = ClsGlobal.getLastDay(dateObj);
            SimpleDateFormat sdfEndDate = new SimpleDateFormat("dd/MM/yyyy");
            Date lastDateObj = sdfEndDate.parse(_endDate);

            long startDate = dateObj.getTime();
            long endDate = lastDateObj.getTime();

            DatePickerDialog dpd = new DatePickerDialog(RecentQuotationActivity.this,
                    (view12, year, month, day) -> {
                        c.set(year, month, day);
                        _expDate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());

                        Log.e("--Date--", "ExpDate: " + _expDate);

                        txt_exp_date.setText(ClsGlobal.getChangeDateFormat(_expDate));

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

    private ProgressDialog loading;

    void setRecentQuotationClick() {
        mAdapter.setOnItemClick(clsQuotationMaster -> {

            final Dialog dialog = new Dialog(RecentQuotationActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_quotation);
            dialog.setCancelable(true);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            LinearLayout ll_options = dialog.findViewById(R.id.ll_options);
            LinearLayout ll_delete = dialog.findViewById(R.id.ll_delete);
            LinearLayout ll_view_details = dialog.findViewById(R.id.ll_view_details);
            View vw_generate = dialog.findViewById(R.id.vw_generate);
            LinearLayout ll_generate_invoice = dialog.findViewById(R.id.ll_generate_invoice);
            ll_generate_invoice.setVisibility(View.VISIBLE);
            vw_generate.setVisibility(View.VISIBLE);


            ll_generate_invoice.setOnClickListener(v -> {

                dialog.dismiss();
                dialog.cancel();


                Log.e("--updateStatus--", "getSaleType: " + clsQuotationMaster.getQuotationType());


                if (clsQuotationMaster.getStatus().equalsIgnoreCase("INVOICE GENERATED")) {
                    quotationToInvoiceDialog(clsQuotationMaster,
                            "INVOICE IS ALREADY GENERATED AGAIN THIS QUOTATION, " +
                                    "SURE TO GENERATE INVOICE?");
                } else if (clsQuotationMaster.getStatus().equalsIgnoreCase("EXPIRED")) {
                    quotationToInvoiceDialog(clsQuotationMaster,
                            "QUOTATION IS EXPIRED ON [EXPIRED DATE]," +
                                    " SURE TO GENERATE INVOICE?");
                } else {
                    generateInvoiceToQuotation(clsQuotationMaster);
                }

            });


            ll_view_details.setOnClickListener(v -> {
                dialog.dismiss();
                dialog.cancel();

                Intent intent = new Intent(RecentQuotationActivity.this,
                        OrderQuotationInfoActivity.class);

                intent.putExtra("quotationNo", clsQuotationMaster.getQuotationNo());
                intent.putExtra("quotationId", clsQuotationMaster.getQuotationID());
                intent.putExtra("netAmount", clsQuotationMaster.getTotalAmount());
                intent.putExtra("grandAmount", clsQuotationMaster.getGrandTotal());

                if (clsQuotationMaster.getApplyTax() != null &&
                        clsQuotationMaster.getApplyTax().equalsIgnoreCase("YES")) {
                    intent.putExtra("taxAmount", ClsGlobal.round(clsQuotationMaster.getTotalTaxAmount(),
                            2));
                    intent.putExtra("_applyTax", "YES");
                } else {
                    intent.putExtra("taxAmount", 0.0);
                    intent.putExtra("_applyTax", "NO");
                }

                intent.putExtra("discountAmount", clsQuotationMaster.getDiscountAmount());
                intent.putExtra("custMobileNO", clsQuotationMaster.getMobileNo());
                intent.putExtra("custName", clsQuotationMaster.getCustomerName());

                startActivity(intent);

            });

            ll_options.setOnClickListener(v -> {
                dialog.dismiss();
                dialog.cancel();

                Intent intent = new Intent(getApplicationContext(), OrderSummeryQuotationActivity.class);
                intent.putExtra("ClsQuotationMaster", clsQuotationMaster);
                intent.putExtra("saleMode", clsQuotationMaster.getQuotationType());
                intent.putExtra("_taxApple", clsQuotationMaster.getApplyTax());
                intent.putExtra("editSource", editSource);
                intent.putExtra("_doneBtn", "list");
                intent.putExtra("entryMode", "displayValue");
                intent.putExtra("_quotationNo", clsQuotationMaster.getQuotationNo());
                intent.putExtra("_quotationId", String.valueOf(clsQuotationMaster.getQuotationID()));
                startActivity(intent);


            });
            ll_delete.setOnClickListener(v -> {
                dialog.dismiss();
                dialog.cancel();


//                Log.e("--QuotationDetail--", "msg: " + getDeleteSmsMessage(
//                        null, clsQuotationMaster,
//                        "Quotation"));


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(RecentQuotationActivity.this);
                alertDialog.setTitle("Confirm Delete...");
                alertDialog.setMessage("Are you sure you want delete?");
                alertDialog.setIcon(R.drawable.ic_delete_black_24dp);

                alertDialog.setPositiveButton("YES", (dialog12, which) -> {

                    @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> delete =
                            new AsyncTask<Void, Void, Void>() {

                                ProgressDialog pd;
                                int ResultInventoryOrderDetail = 0,
                                        ResultInventoryOrderMaster = 0;

                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();

                                    pd = ClsGlobal._prProgressDialog(RecentQuotationActivity.this,
                                            "Loading...", false);
                                    pd.show();
                                }

                                @Override
                                protected Void doInBackground(Void... voids) {

                                    ResultInventoryOrderDetail = ClsQuotationOrderDetail
                                            .deleteQuotationDetail(clsQuotationMaster.getQuotationNo()
                                                    , String.valueOf(clsQuotationMaster.getQuotationID()),
                                                    "DeleteBy OrderNo and OrderID", RecentQuotationActivity.this);

                                    Log.e("--QuotationDetail--", "ResultDetail: " + ResultInventoryOrderDetail);

                                    ResultInventoryOrderMaster = ClsQuotationMaster.deleteQuotationMaster(clsQuotationMaster.getQuotationNo(),
                                            clsQuotationMaster.getQuotationID()
                                            , RecentQuotationActivity.this);

                                    Log.e("--QuotationDetail--", "ResultMaster: " + ResultInventoryOrderMaster);

                                    if (ResultInventoryOrderDetail > 0
                                            && ResultInventoryOrderMaster > 0) {
                                        Log.e("Result", "ResultInventoryOrderMaster ");
                                        String smsReceiverPhoneNo = ClsGlobal.getSmsReceiver("Sales",
                                                RecentQuotationActivity.this);
                                        if (smsReceiverPhoneNo != null
                                                && !smsReceiverPhoneNo.equalsIgnoreCase("")) {
                                            ClsGlobal.SendSms("No", "Quotation",
                                                    clsQuotationMaster.getQuotationID(),
                                                    clsQuotationMaster.getQuotationNo()
                                                    , smsReceiverPhoneNo,
                                                    clsQuotationMaster.getCustomerName(),
                                                    null, clsQuotationMaster,
                                                    String.valueOf(clsQuotationMaster.getTotalAmount()),
                                                    null, getDeleteSmsMessage(
                                                            null, clsQuotationMaster,
                                                            "Quotation"),
                                                    RecentQuotationActivity.this);
                                        }


                                    }


                                    // Delete Pending Sms log.
                                    DeleteByOrderIdAndOrderNo(RecentQuotationActivity.this,
                                            String.valueOf(clsQuotationMaster.getQuotationID()),
                                            clsQuotationMaster.getQuotationNo());


                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    super.onPostExecute(aVoid);
                                    if (pd.isShowing()) {
                                        pd.dismiss();
                                    }

                                    if (ResultInventoryOrderDetail > 0
                                            && ResultInventoryOrderMaster > 0) {
                                        Toast.makeText(RecentQuotationActivity.this,
                                                "Order Deleted Successfully", Toast.LENGTH_LONG).show();
                                        loadData("");
                                    } else {
                                        Toast.makeText(RecentQuotationActivity.this,
                                                "Failed to Delete Order", Toast.LENGTH_LONG).show();
                                    }

                                }
                            };

                    delete.execute();

                    loadData("");

                    dialog12.dismiss();
                    dialog12.cancel();
                });
                alertDialog.setNegativeButton("NO", (dialog1, which) -> {
                    dialog1.dismiss();
                    dialog1.cancel();
                });
                alertDialog.show();
            });

            dialog.show();
            dialog.getWindow().setAttributes(lp);
        });
    }

    private void quotationToInvoiceDialog(ClsQuotationMaster obj, String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RecentQuotationActivity.this);
//                    alertDialog.setTitle("Order is update mode...");
        alertDialog.setMessage(msg);
        alertDialog.setIcon(R.drawable.ic_wholesale);


        alertDialog.setPositiveButton("YES", (dialog13, which) -> {
            generateInvoiceToQuotation(obj);

            dialog13.dismiss();
            dialog13.cancel();


        });
        alertDialog.setNegativeButton("NO", (dialog13, which) -> {
            dialog13.dismiss();
            dialog13.cancel();

        });
        alertDialog.show();
    }

    private void generateInvoiceToQuotation(ClsQuotationMaster obj) {

        @SuppressLint("WrongConstant") SQLiteDatabase db = openOrCreateDatabase(ClsGlobal.Database_Name,
                Context.MODE_APPEND, null);


        Intent intent = new Intent(this, SalesActivity.class);


        if (obj.getQuotationType().equalsIgnoreCase("Retail Quotation")) {
            intent.putExtra("saleMode", "Sale");

            if (ClsGlobal._OrderNo == null || ClsGlobal._OrderNo.isEmpty()
                    || ClsGlobal._OrderNo.matches("")) {

                ClsGlobal._OrderNo = String.valueOf(ClsInventoryOrderMaster
                        .getTemporaryOrderNo("OrderNo"));//ClsSales.getNextOrderNo();
                Log.e("--updateStatus--", "SaleOrderNo: " + ClsGlobal._OrderNo);
            }


            ClsQuotationMaster.getGenerateInvoice(ClsGlobal._OrderNo, 0,
                    obj.getQuotationNo(), obj.getQuotationID(),
                    RecentQuotationActivity.this, db);

            intent.putExtra("editOrderNo", ClsGlobal._OrderNo);


        } else if (obj.getQuotationType().equalsIgnoreCase("Wholesale Quotation")) {
            intent.putExtra("saleMode", "Wholesale");

            if (ClsGlobal._WholeSaleOrderNo == null || ClsGlobal._WholeSaleOrderNo.isEmpty()
                    || ClsGlobal._WholeSaleOrderNo.matches("")) {

                ClsGlobal._WholeSaleOrderNo = String.valueOf(ClsInventoryOrderMaster
                        .getTemporaryOrderNo("OrderNo"));//ClsSales.getNextOrderNo();
                Log.e("--updateStatus--", "WholeSaleOrderNo: " + ClsGlobal._WholeSaleOrderNo);
            }

            ClsQuotationMaster.getGenerateInvoice(ClsGlobal._WholeSaleOrderNo, 0,
                    obj.getQuotationNo(), obj.getQuotationID(),
                    RecentQuotationActivity.this, db);

            intent.putExtra("editOrderNo", ClsGlobal._WholeSaleOrderNo);
        }

        db.close();

        ClsGlobal._quotationStatusID = String.valueOf(obj.getQuotationID());
        ClsGlobal._quotationMobileNO = obj.getMobileNo();
        ClsGlobal.SetOrderEditMode(getApplicationContext(), "New");

        intent.putExtra("editOrderID", "");

        intent.putExtra("_taxApple", obj.getApplyTax());

        startActivity(intent);
        finish();
    }

    private void SaveData_SharedPreferences(String str) {
        SharedPreferences.Editor preferencesEditor = mPreferencesDefault.edit();
        preferencesEditor.putString("WhatsApp Default App", str);
        preferencesEditor.apply();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_recent_order_activity, menu);
        MenuItem item = menu.findItem(R.id.search);

        if (fragMode.equalsIgnoreCase("QuotationReportFragment")) {
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
                _whereFilter = "";
                if (query != null && !query.isEmpty()) {

                    _whereFilter = " AND ([MobileNo] LIKE '%".concat(query).concat("%'");
                    _whereFilter = _whereFilter.concat(" OR [CustomerName] LIKE '%".concat(query).concat("%'"));
                    _whereFilter = _whereFilter.concat(" OR [QuotationNo] LIKE '%".concat(query).concat("%')"));

                    loadData(_whereFilter);
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

}
