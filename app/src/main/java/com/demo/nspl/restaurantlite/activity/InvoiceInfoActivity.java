package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.demo.nspl.restaurantlite.Adapter.AutoSuggestAdapter;
import com.demo.nspl.restaurantlite.Adapter.CustomerAdapter;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsCustomerMaster;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderDetail;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster;
import com.demo.nspl.restaurantlite.classes.ClsInventoryResult;
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;
import com.demo.nspl.restaurantlite.classes.ClsQuotationMaster;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.InventoryOrderDetail_last_id;

public class InvoiceInfoActivity extends AppCompatActivity {

    private static final String TAG = InvoiceInfoActivity.class.getName();

    private ClsInventoryOrderMaster getCurrentObj;
    private ClsPaymentMaster objPaymentMaster;
    private MaterialButton Btn_Save;
    private ImageButton bt_close;
    private ImageView imgView_More_Details;
    private RadioGroup radio_group, rg_Select_Default_App;
    private LinearLayout Received_linear, Radio_linear;
    private TextInputLayout txt_input_CompanyName, txt_input_GSTNo;

    private TextView txt_Aul_Limit, txt_pending_amt, txt_Default;
    private LinearLayout layout_Select_Default;
    Double BillAmount = 0.0;
    Double PaidAmount = 0.0;
    Double DiffAmount = 0.0;


    ImageButton iv_call_browse, iv_search_contact;
    CheckBox check_Send_Pdf;
    private TextView edit_select_date, txt_title_amt;
    RadioButton rbCASE, rbCARD, rbOTHER, rbPending,
            rbAdevstment, rbAdd_To_Credit, rbWhatsApp, rbBusiness_WhatsApp;

    private EditText edit_Mobile_no, edit_CustomerName,
            edit_CompanyName, edit_GSTNo, edt_received_amt;
    List<ClsInventoryOrderDetail> list = new ArrayList<>();
    String getSelectedPaymentMode = "", Mode = "", getAmount = "", BillTo = "";
    public static String getCurrent_Order_No = "";
    int mYear, mMonth, mDay;
    private static final int REQUEST_SMS = 0;
    boolean isVisible = true;
    private Dialog dialogvendor;
    AutoCompleteTextView edt_payment_detail;
    List<String> listOfText = new ArrayList<>();


    private List<ClsCustomerMaster> list_customer;
    private StickyListHeadersListView lst;
    RelativeLayout lyout_nodata;
    CustomerAdapter customerAdapter;
    LinearLayout lv_first;
    RadioGroup rg_mode;
    RadioButton rb_customer, rb_none;
    private SharedPreferences mPreferencesDefault;
    String getWhatsAppDefaultApp = "";
    private static final String mPreferncesName = "MyPerfernces";

    private static final Pattern MobileNo_Pattern
            = Pattern.compile(ClsGlobal.MobileNo_Pattern);

    String saleMode = "";
    String entryMode = "";
    String editSource = "";

    int _updatedID = 0;

    LinearLayout ll_new;
    LinearLayout ll_update;

    //    -------------------05/08-------------------
    LinearLayout ll_sale_return_discount;
    EditText edt_sale_return_discount;
    TextInputLayout til_received_amt;
    //    -------------------05/08-------------------

    TextView txt_bill_to, txt_cust_name, txt_cust_number,
            txt_paid_amt, txt_payment_mode, txt_receivable_amt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_info);

        edit_select_date = findViewById(R.id.edit_select_date);
        Btn_Save = findViewById(R.id.Btn_Save);
        bt_close = findViewById(R.id.bt_close);
        edit_Mobile_no = findViewById(R.id.edit_Mobile_no);
        edit_CustomerName = findViewById(R.id.edit_CustomerName);
        edit_CompanyName = findViewById(R.id.edit_CompanyName);
        edit_GSTNo = findViewById(R.id.edit_GSTNo);
        edt_payment_detail = findViewById(R.id.edt_payment_detail);
        fillSuggestion();
        txt_title_amt = findViewById(R.id.txt_title_amt);
        radio_group = findViewById(R.id.radio_group);
        rbCASE = findViewById(R.id.rbCASE);
        rbCARD = findViewById(R.id.rbCARD);
        rbOTHER = findViewById(R.id.rbOTHER);
        rbPending = findViewById(R.id.rbPending);
        check_Send_Pdf = findViewById(R.id.check_Send_Pdf);
        imgView_More_Details = findViewById(R.id.imgView_More_Details);
        txt_input_CompanyName = findViewById(R.id.txt_input_CompanyName);
        txt_input_GSTNo = findViewById(R.id.txt_input_GSTNo);
        txt_Aul_Limit = findViewById(R.id.txt_Aul_Limit);
        edt_received_amt = findViewById(R.id.edt_received_amt);
        txt_pending_amt = findViewById(R.id.txt_pending_amt);

        rbAdevstment = findViewById(R.id.rbAdevstment);
        rbAdd_To_Credit = findViewById(R.id.rbAdd_To_Credit);
        Received_linear = findViewById(R.id.Received_linear);
        Radio_linear = findViewById(R.id.Radio_linear);
        iv_call_browse = findViewById(R.id.iv_call_browse);
        iv_search_contact = findViewById(R.id.iv_search_contact);
        txt_Default = findViewById(R.id.txt_Default);
        layout_Select_Default = findViewById(R.id.layout_Select_Default);
        rg_Select_Default_App = findViewById(R.id.rg_Select_Default_App);
        rbWhatsApp = findViewById(R.id.rbWhatsApp);
        rbBusiness_WhatsApp = findViewById(R.id.rbBusiness_WhatsApp);


        ll_update = findViewById(R.id.ll_update);
        lv_first = findViewById(R.id.lv_first);
        rg_mode = findViewById(R.id.rg_mode);
        rb_customer = findViewById(R.id.rb_customer);
        rb_none = findViewById(R.id.rb_none);
        ll_sale_return_discount = findViewById(R.id.ll_sale_return_discount);
        edt_sale_return_discount = findViewById(R.id.edt_sale_return_discount);
        edt_sale_return_discount.setTag(0.0);

        ll_new = findViewById(R.id.ll_new);
        txt_bill_to = findViewById(R.id.txt_bill_to);
        txt_cust_name = findViewById(R.id.txt_cust_name);
        txt_cust_number = findViewById(R.id.txt_cust_number);

        txt_paid_amt = findViewById(R.id.txt_paid_amt);
        txt_receivable_amt = findViewById(R.id.txt_receivable_amt);
        txt_payment_mode = findViewById(R.id.txt_payment_mode);
        til_received_amt = findViewById(R.id.til_received_amt);


        saleMode = getIntent().getStringExtra("saleMode");
        entryMode = getIntent().getStringExtra("entryMode");
        editSource = getIntent().getStringExtra("editSource");

        Log.e("--Test--", "InvoiceInfoEditSource: " + saleMode);

        if (entryMode.equalsIgnoreCase("New")) {
            ll_update.setVisibility(View.GONE);
            ll_sale_return_discount.setVisibility(View.GONE);
            ll_new.setVisibility(View.VISIBLE);
            edt_sale_return_discount.setText("0.00");
        } else {
            ll_update.setVisibility(View.VISIBLE);
            ll_sale_return_discount.setVisibility(View.GONE); //Sale Edit changes...
            ll_new.setVisibility(View.GONE);

        }


        rg_mode.setOnCheckedChangeListener((radioGroup, i) -> {

            if (rb_customer.isChecked()) {
                lv_first.setVisibility(View.VISIBLE);
                rbAdd_To_Credit.setVisibility(View.VISIBLE);
                rbPending.setVisibility(View.VISIBLE);
                edit_Mobile_no.requestFocus();
            } else {
                rbAdevstment.setChecked(true);
                edit_Mobile_no.setText("");
                edit_CustomerName.setText("");
                lv_first.setVisibility(View.GONE);
                rbPending.setVisibility(View.GONE);
                rbAdd_To_Credit.setVisibility(View.GONE);
            }
        });

        list = new ArrayList<>();
        edit_select_date.setText(ClsGlobal.getEntryDateAndTime());

        getCurrentObj = (ClsInventoryOrderMaster) getIntent()
                .getSerializableExtra("ClsInventoryOrderMaster");

        Gson gson = new Gson();
        String jsonInString = gson.toJson(getCurrentObj);
        Log.e("--Invoice--", "UpdateMode: " + jsonInString);

        Mode = getIntent().getStringExtra("Mode");

        getAmount = getIntent().getStringExtra("Amount");

        BillAmount = Double.valueOf(getAmount);


        if (saleMode != null && saleMode.equalsIgnoreCase("Sale")) {

            getCurrent_Order_No = ClsGlobal._OrderNo;
        } else {
            getCurrent_Order_No = ClsGlobal._WholeSaleOrderNo;
        }

        txt_title_amt.setText("Bill Amount: " + getAmount);


        txt_pending_amt.setText(getAmount);
        txt_pending_amt.setTag(getAmount);

        if (Mode.equalsIgnoreCase("WhatAppsSave")) {

            layout_Select_Default.setVisibility(View.VISIBLE);
            txt_Default.setVisibility(View.VISIBLE);

            mPreferencesDefault = getSharedPreferences(mPreferncesName, MODE_PRIVATE);

            if (mPreferencesDefault.getString("WhatsApp Default App", null) == null) {
                SaveData_SharedPreferences("WhatsApp");
            }

            getWhatsAppDefaultApp = mPreferencesDefault.getString("WhatsApp Default App", "");

            if (getWhatsAppDefaultApp != null &&
                    !getWhatsAppDefaultApp.equalsIgnoreCase("")) {
                if (getWhatsAppDefaultApp.equalsIgnoreCase("WhatsApp")) {
                    rbWhatsApp.setChecked(true);
                } else {
                    rbBusiness_WhatsApp.setChecked(true);
                }

            }

            rg_Select_Default_App.setOnCheckedChangeListener((group, checkedId) -> {

                if (checkedId == R.id.rbWhatsApp) {
                    SaveData_SharedPreferences(rbWhatsApp.getText().toString());
                    getWhatsAppDefaultApp = rbWhatsApp.getText().toString();
                } else {
                    SaveData_SharedPreferences(rbBusiness_WhatsApp.getText().toString());
                    getWhatsAppDefaultApp = rbBusiness_WhatsApp.getText().toString();
                }

            });
        } else {
            layout_Select_Default.setVisibility(View.GONE);
            txt_Default.setVisibility(View.GONE);
        }


        String _where = " AND [OrderNo] = '" + getCurrent_Order_No + "' ";
        if (ClsGlobal.editOrderID != null
                && !ClsGlobal.editOrderID.equalsIgnoreCase("")) {
            _where += "".concat(" AND [OrderID] = '" + ClsGlobal.editOrderID + "'");
        }
        list = new ClsInventoryOrderDetail().getList(_where, InvoiceInfoActivity.this);

        Gson gson1 = new Gson();
        String jsonInString1 = gson1.toJson(list);
        Log.d("list", "objClsVerifiedOtpParms- " + jsonInString1);

        // Set Save button icon.
        switch (Mode) {

            case "Save":
                Btn_Save.setIcon(ContextCompat.getDrawable(Objects.requireNonNull(InvoiceInfoActivity.this), R.drawable.ic_btn_add));
                Btn_Save.setText("Save");
                rb_none.setVisibility(View.VISIBLE);
                break;

            case "SendSmsSave":
                Btn_Save.setIcon(ContextCompat.getDrawable(Objects.requireNonNull(InvoiceInfoActivity.this), R.drawable.ic_sms));
                Btn_Save.setText("Save and Send SMS");
                rb_none.setVisibility(View.GONE);
                break;

            case "WhatAppsSave":
                //  check_Send_Pdf.setVisibility(View.VISIBLE);
                Btn_Save.setIcon(ContextCompat.getDrawable(Objects.requireNonNull(InvoiceInfoActivity.this), R.drawable.ic_whatsapp));
                Btn_Save.setText("Save and Sent To WhatsApp");
                rb_none.setVisibility(View.GONE);
                break;

        }

        iv_call_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, 1);
            }
        });

//        fillCustomerList();


        iv_search_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillCustomerList();

                if (list_customer != null && list_customer.size() != 0) {
                    CustomerDialog();
                } else {
                    Toast.makeText(InvoiceInfoActivity.this, "No customer found!", Toast.LENGTH_LONG).show();
                }
            }
        });


        imgView_More_Details.setOnClickListener(v -> {

            if (isVisible) {
                txt_input_CompanyName.setVisibility(View.VISIBLE);
                txt_input_GSTNo.setVisibility(View.VISIBLE);
                edit_GSTNo.setVisibility(View.VISIBLE);
                edit_CompanyName.setVisibility(View.VISIBLE);
                imgView_More_Details.setImageResource(R.drawable.ic_arrow_drop_down);
                isVisible = false;
            } else {
                txt_input_CompanyName.setVisibility(View.GONE);
                txt_input_GSTNo.setVisibility(View.GONE);
                edit_GSTNo.setVisibility(View.GONE);
                edit_CompanyName.setVisibility(View.GONE);
                imgView_More_Details.setImageResource(R.drawable.ic_arrow_up);
                isVisible = true;
            }

        });


        edit_select_date.setOnClickListener(v -> {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(InvoiceInfoActivity.this,
                    (view, year, month, day) -> {
                        c.set(year, month, day);
                        String date = new SimpleDateFormat("dd/MM/yyyy hh:mm aa").format(c.getTime());
                        edit_select_date.setText(date);
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                    }, mYear, mMonth, mDay);
            dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
            Calendar d = Calendar.getInstance();
            dpd.updateDate(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH));
            dpd.show();

        });

        if (Double.valueOf(txt_pending_amt.getText().toString()) < 0) {
            txt_pending_amt.setTextColor(Color.parseColor("#c40000"));

        } else {
            txt_pending_amt.setTextColor(Color.parseColor("#000000"));
        }

        edt_sale_return_discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                _diffAmount("SALE RETURN DISCOUNT");

            }
        });

        edt_received_amt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                _diffAmount("RECEIVED AMOUNT");
            }
        });


        edit_Mobile_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String txtValue = "";
                if (s != null && !s.toString().isEmpty() && s.toString() != "") {
                    txtValue = s.toString();
                }

                if (txtValue.length() == 10) {

                    setCustomerDetails(txtValue);

                } else {
                    edit_CustomerName.setText("");
                    edit_CompanyName.setText("");
                    edit_GSTNo.setText("");
                    txt_Aul_Limit.setText("0.00");
                    txt_Aul_Limit.setTextColor(Color.parseColor("#000000"));

                }

            }
        });

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rbPending.isChecked()) {
                    Received_linear.setVisibility(View.GONE);
                    Radio_linear.setVisibility(View.GONE);
                    edt_received_amt.setText("0.00");
                    rbAdevstment.setChecked(true);
                } else {
                    Received_linear.setVisibility(View.VISIBLE);
                    Radio_linear.setVisibility(View.VISIBLE);
                    edt_received_amt.setText("");
                }
            }
        });

        bt_close.setOnClickListener(v -> {

            finish();
            //callbackResult.sendResult(300);
        });


        fillValue();

        if (ClsGlobal._quotationStatusID != null && ClsGlobal._quotationStatusID != "") {
            fillCustomerForGenerateToInvoice();
        }


        Btn_Save.setOnClickListener(v -> {


            if (validation()) {

                switch (Mode) {

                    case "WhatAppsSave":

//                        String curruntOrderNo = "";
                        if (saleMode.equalsIgnoreCase("Sale")) {
//                            curruntOrderNo = ClsGlobal._OrderNo;
                            ClsGlobal.current_OrderNo = ClsGlobal._OrderNo;

                        } else {
//                            curruntOrderNo = ClsGlobal._WholeSaleOrderNo;
                            ClsGlobal.current_OrderNo = ClsGlobal._WholeSaleOrderNo;
                        }

                        if (!edit_Mobile_no.getText().toString().equalsIgnoreCase("")) {

                            new MessageAsyncTask("WhatsApp").execute();

                        } else {
                            Toast.makeText(InvoiceInfoActivity.this, "Enter Mobile Number!", Toast.LENGTH_LONG).show();
                        }
                        break;

                    case "SendSmsSave":

                        new MessageAsyncTask("SMS").execute();

                        break;
                    case "Save":

                        SaveToDatabase();

//                        if (editSource.equalsIgnoreCase("Recent Order")) {
//                            Log.e("--Test--", "IF: " + editSource);
//                        } else {
//                            Log.e("--Test--", "ELSE: " + editSource);
//                        }


//                        if (entryMode.equalsIgnoreCase("edit")) {
//                            ClsGlobal.SetOrderEditMode(getApplicationContext(), "New");
//                            Intent intent = new Intent(InvoiceInfoActivity.this, RecentOrderActivity.class);
//                            intent.putExtra("Mounth", "");
//                            intent.putExtra("Year", "");
//                            intent.putExtra("title", "");
//                            intent.putExtra("editSource", editSource);
//
//
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//
//                        } else {
//
//                            ClsGlobal.SetOrderEditMode(getApplicationContext(), "New");
//                            Intent intent = new Intent(InvoiceInfoActivity.this, SalesActivity.class);
//                            intent.putExtra("saleMode", saleMode);
//                            intent.putExtra("entryMode", "New");
//                            intent.putExtra("editSource", editSource);
//
//                            Log.e("--Test--", "InvoiceInfoMode: " + saleMode);
//                            Log.e("--Test--", "InvoiceInfoEntryMode: " + entryMode);
//                            Log.e("--Test--", "editSource: " + editSource);
//
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                        }
                        break;
                }
            }
        });
    }

    private void setCustomerDetails(String txtValue) {


        String where = "AND [MOBILE_NO] = ".concat(txtValue);

        ClsCustomerMaster getCurrentCustomer = ClsCustomerMaster.getCustomerByMobileNo(where, InvoiceInfoActivity.this);
        _updatedID = getCurrentCustomer.getmId();


        Log.d("--GSON--", "_updatedID: " + _updatedID);

        Gson gson = new Gson();
        String jsonInString = gson.toJson(getCurrentCustomer);
        Log.d("--GSON--", "getCurrentCustomer: " + jsonInString);


        if (getCurrentCustomer != null) {
            edit_CustomerName.setText(getCurrentCustomer.getmName());
            edit_CompanyName.setText(getCurrentCustomer.getCompany_Name());
            edit_GSTNo.setText(getCurrentCustomer.getGST_NO());


            Double _avlCreditLimit = ClsCustomerMaster.getCustomerCredit(txtValue, InvoiceInfoActivity.this);
            ClsGlobal.passValue(_avlCreditLimit, txt_Aul_Limit);
            txt_Aul_Limit.setText(String.valueOf(_avlCreditLimit));

        }


    }

    void _diffAmount(String _mode) {

        double _diffAmt = Double.valueOf(txt_pending_amt.getTag() == null || txt_pending_amt.getTag().toString().equalsIgnoreCase("")
                ? "0.00" : txt_pending_amt.getTag().toString());

        Log.e("--DiffAmount-- ", "_diffAmt: " + _diffAmt);


        double _receivedAmt = Double.valueOf(edt_received_amt.getText() == null || edt_received_amt.getText().toString().equalsIgnoreCase("")
                ? "0.00" : edt_received_amt.getText().toString());

        Log.e("--DiffAmount-- ", "_receivedAmt: " + _receivedAmt);


        double get_pending_amt = 0.0;

        if (_diffAmt < 0) {
            get_pending_amt = _diffAmt + _receivedAmt;
        } else {
            get_pending_amt = _diffAmt - _receivedAmt;
        }


        Log.e("--DiffAmount-- ", "get_pending_amt: " + ClsGlobal.round(get_pending_amt, 2));

        if (edt_sale_return_discount.getText() != null && !edt_sale_return_discount.getText().toString().isEmpty()) {
            get_pending_amt = get_pending_amt + Double.valueOf(edt_sale_return_discount.getText().toString());
        }

        txt_pending_amt.setText(String.valueOf(ClsGlobal.round(get_pending_amt, 2)));
//        txt_pending_amt.setTag(ClsGlobal.round(get_pending_amt, 2));


        if (Double.valueOf(txt_pending_amt.getText().toString()) < 0) {
            txt_pending_amt.setTextColor(Color.parseColor("#c40000"));

        } else {
            txt_pending_amt.setTextColor(Color.parseColor("#000000"));
        }

        if (_mode != null && _mode.equalsIgnoreCase("SALE RETURN DISCOUNT")) {

            if (Double.valueOf(txt_pending_amt.getText().toString()) < 0) {
                til_received_amt.setHint("REFUND AMOUNT");
            } else {
                til_received_amt.setHint("RECEIVED AMOUNT");
            }

        }

    }


    void fillCustomerForGenerateToInvoice() {

        String _quotCustMobile = ClsGlobal._quotationMobileNO;
        if (_quotCustMobile != null && !_quotCustMobile.isEmpty()) {
            edit_Mobile_no.setText(_quotCustMobile);
            //call customer function.
            setCustomerDetails(_quotCustMobile);
        }
    }


    void fillValue() {

        Log.d("--fillValue--", "entryMode: " + entryMode);


        if (entryMode != null && entryMode.equalsIgnoreCase("edit")) {

            rbAdevstment.setChecked(true);
            edit_Mobile_no.setText("");
            edit_CustomerName.setText("");
            lv_first.setVisibility(View.GONE);
            rbPending.setVisibility(View.GONE);
            rbAdd_To_Credit.setVisibility(View.GONE);


            ClsInventoryOrderMaster _obj = ClsInventoryOrderMaster.getOrderDetail((" AND IOM.OrderNo =" +
                            " '").concat(String.valueOf(getCurrent_Order_No).concat("'")),
                    InvoiceInfoActivity.this);

            if (!_obj.getOrderNo().equalsIgnoreCase("0") &&
                    !_obj.getOrderNo().isEmpty()) {

                txt_bill_to.setText("BILL TO: " + _obj.getBillTo());
                txt_cust_name.setText("NAME: " + _obj.getCustomerName().toUpperCase());
                txt_cust_number.setText("MOBILE NO: " + _obj.getMobileNo());
                txt_payment_mode.setText("MODE: " + _obj.getPaymentMode());
                txt_receivable_amt.setText("BILL AMOUNT: \u20B9 " + _obj.getTotalReceiveableAmount());

                edit_select_date.setText(ClsGlobal.getEntryDateFormat(_obj.getBillDate()));
                edit_Mobile_no.setText(_obj.getMobileNo());

                if (_obj.getBillTo() != null && _obj.getBillTo().equalsIgnoreCase("customer")) {
                    setCustomerDetails(_obj.getMobileNo());
                }
            }

            // -----------------Total Collected From Payment Master ---------------------------

            txt_paid_amt.setText("PAID AMOUNT: \u20B9 0.00");
            txt_paid_amt.setTag(0.00);


            Log.e("--DiffAmount--", "_receivedAmt: " + getCurrent_Order_No);

            Double _paidAmt = ClsPaymentMaster.getTotalCollectedAmount
                    (InvoiceInfoActivity.this, getCurrent_Order_No,
                            Integer.parseInt(ClsGlobal.editOrderID));


            if (_paidAmt != null && _paidAmt != 0.0 && _paidAmt != 0) {
                txt_paid_amt.setText("PAID AMOUNT: \u20B9 " + _paidAmt);
                txt_paid_amt.setTag(ClsGlobal.round(_paidAmt, 2));
            }

            double _collectedAmt = 0.0;

            _collectedAmt = Double.valueOf(getAmount) - _paidAmt;

            txt_pending_amt.setText(String.valueOf(ClsGlobal.round(_collectedAmt, 2)));
            txt_pending_amt.setTag(ClsGlobal.round(_collectedAmt, 2));
        }

    }

    private Uri uri = null;

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1) && (resultCode == RESULT_OK)) {
            Cursor cursor = null;
            try {
                uri = data.getData();
                cursor = getContentResolver().query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
                if (cursor != null && cursor.moveToNext()) {

                    String phone = cursor.getString(0);

                    Matcher matcher = MobileNo_Pattern.matcher(formatPhoneNo(phone));

                    if (matcher.matches()) {
                        edit_Mobile_no.setText(phone);
                        retrieveContactName();
                    } else {
                        edit_Mobile_no.setText("");
                        edit_CustomerName.setText("");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
*/


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

                    if (matcher.matches()) {
                        edit_Mobile_no.setText(phone);
                        retrieveContactName();
                    } else {
                        edit_Mobile_no.setText("");
                        edit_CustomerName.setText("");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void fillSuggestion() {
        listOfText = ClsInventoryOrderMaster.getPaymentDetailSuggestion(InvoiceInfoActivity.this);

        AutoSuggestAdapter autoSuggestAdapter = new AutoSuggestAdapter(this,
                android.R.layout.simple_list_item_1, listOfText);

        edt_payment_detail.setAdapter(autoSuggestAdapter);
        edt_payment_detail.setThreshold(1);//comparesion start from first character

    }


    private void fillCustomerList() {
        list_customer = new ArrayList<>();
        list_customer = new ClsCustomerMaster().ListCustomers("", InvoiceInfoActivity.this);
    }


    private void CustomerDialog() {

        final EditText edit_search;
        dialogvendor = new Dialog(InvoiceInfoActivity.this);
        dialogvendor.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogvendor.setContentView(R.layout.dialog_type_n_search);
        dialogvendor.setTitle("Select Customer");
        dialogvendor.setCanceledOnTouchOutside(true);
        dialogvendor.setCancelable(true);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogvendor.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogvendor.getWindow().setAttributes(lp);

        lst = dialogvendor.findViewById(R.id.list);
        lyout_nodata = dialogvendor.findViewById(R.id.lyout_nodata);
        edit_search = dialogvendor.findViewById(R.id.edit_search);


        Handler handler = new Handler();
        handler.postDelayed(() -> {

            try {

                if (list_customer != null && list_customer.size() != 0) {

                    customerAdapter = new CustomerAdapter(InvoiceInfoActivity.this);
                    lst.setAdapter(customerAdapter);

                    customerAdapter.AddItems(list_customer);

                    edit_search.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String where = "";

                            if (s != null && !s.equals("")) {
                                where = " AND ([NAME] LIKE '%".concat(String.valueOf(s)).concat("%'");
                                where = where.concat(" OR [MOBILE_NO] LIKE '%".concat(String.valueOf(s)).concat("%')"));
                                Log.e("where", where);
                            }


                            list_customer = new ClsCustomerMaster().ListCustomers(where,
                                    InvoiceInfoActivity.this);

                            customerAdapter.AddItems(list_customer);

                            lst.setAdapter(customerAdapter);

                        }
                    });

                    customerAdapter.SetOnClickListener(clsCustomerMaster -> {

                        Log.e("clsCustomerMaster", clsCustomerMaster.getmMobile_No());
                        Log.e("clsCustomerMaster", clsCustomerMaster.getmName());
                        edit_Mobile_no.setText(clsCustomerMaster.getmMobile_No());
//                            custom_name.setText(String.valueOf(clsCustomerMaster.getmName()));
                        dialogvendor.dismiss();

                    });
                }


            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            }

        }, 400);
        dialogvendor.show();

    }


    int resultInsertCustomer = 0;

    @SuppressLint("CheckResult")
    private void SaveToDatabase() {

        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Integer> insert = new AsyncTask<Void, Void, Integer>() {
            private ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ClsGlobal._prProgressDialog(InvoiceInfoActivity.this
                        , "Please Wait...", false);
                loading.show();
            }

            @Override
            protected Integer doInBackground(Void... voids) {
                ClsInventoryResult clsInventoryResult = new ClsInventoryResult();

                try {
                    BillAmount = Double.valueOf(getAmount);
                    PaidAmount = Double.valueOf(edt_received_amt.getText().toString().equalsIgnoreCase("")
                            ? "0.00" : edt_received_amt.getText().toString());


//        DiffAmount = BillAmount - PaidAmount;    // ADD SALE RETURN DISCOUNT HERE........


                    if (txt_pending_amt.getText() != null && !txt_pending_amt.getText().toString().isEmpty()) {
                        double _pendingAmt = Double.valueOf(txt_pending_amt.getText().toString());

                        if (_pendingAmt != 0 && _pendingAmt != 0.0) {

                            DiffAmount = _pendingAmt;

                        }
                    }


                    getCurrentObj.setBillDate(ClsGlobal.getChangeDateFormatDB(edit_select_date.getText().toString().trim()));//20/02/2019 02:21 pm | 2019-02-20 15:21:00

                    getCurrentObj.setMobileNo(edit_Mobile_no.getText().toString().equalsIgnoreCase("")
                            ? "" : edit_Mobile_no.getText().toString().trim()); //+

                    getCurrentObj.setCustomerName(edit_CustomerName.getText().toString().equalsIgnoreCase("")
                            ? "" : edit_CustomerName.getText().toString().trim());

                    getCurrentObj.setCompanyName(edit_CompanyName.getText().toString().equalsIgnoreCase("")
                            ? "" : edit_CompanyName.getText().toString().trim());

                    getCurrentObj.setGSTNo(edit_GSTNo.getText().toString().equalsIgnoreCase("")
                            ? "" : edit_GSTNo.getText().toString().trim()); //+

                    getCurrentObj.setSaleType(saleMode);

                    if (rbCASE.isChecked()) {
                        getSelectedPaymentMode = rbCASE.getText().toString();
                    } else if (rbCARD.isChecked()) {
                        getSelectedPaymentMode = rbCARD.getText().toString();
                    } else if (rbOTHER.isChecked()) {
                        getSelectedPaymentMode = rbOTHER.getText().toString();
                    } else if (rbPending.isChecked()) {
                        getSelectedPaymentMode = rbPending.getText().toString();
                    }


                    if (rb_customer.isChecked()) {
                        BillTo = rb_customer.getText().toString();
                    } else if (rb_none.isChecked()) {
                        BillTo = rb_none.getText().toString();
                    }

                    // getCurrentObj.setBillDate();

                    if (rb_customer.isChecked()) {
                        getCurrentObj.setBillTo("CUSTOMER");
                    } else {
                        getCurrentObj.setBillTo("OTHER");
                    }


                    getCurrentObj.setPaymentMode(getSelectedPaymentMode);
                    getCurrentObj.setPaymentDetail(edt_payment_detail.getText().toString().equalsIgnoreCase("")
                            ? "" : edt_payment_detail.getText().toString().trim());

                    getCurrentObj.setTotalReceiveableAmount(BillAmount);

                    // ------------- SATURDAY--------------------

                    if (entryMode != null && entryMode.equalsIgnoreCase("edit")) {
                        getCurrentObj.setPaidAmount(PaidAmount + Double.valueOf(txt_paid_amt.getTag().toString()));
                    } else {
                        getCurrentObj.setPaidAmount(PaidAmount);
                    }

                    getCurrentObj.setSaleReturnDiscount(Double.valueOf(edt_sale_return_discount.getText().toString().equalsIgnoreCase("")
                            ? "0.00" : edt_sale_return_discount.getText().toString()));

                    getCurrentObj.setAdjumentAmount(DiffAmount);

                    getCurrentObj.setDifferent_Amount_mode(rbAdd_To_Credit.isChecked()
                            ? "ADD TO CREDIT" : "ADJUSTMENT");

                    getCurrentObj.setSms_Limit(0);

                    Gson gson = new Gson();
                    String jsonInString = gson.toJson(getCurrentObj);
                    Log.d("getCurrentObj", "Insert- " + jsonInString);

//------------------------------- Save Customer Details ----------------------//

                    if (!rb_none.isChecked()) {
                        ClsCustomerMaster saveCustomer = new ClsCustomerMaster();
                        saveCustomer.setmName(edit_CustomerName.getText().toString().equalsIgnoreCase("")
                                ? "" : edit_CustomerName.getText().toString().trim());

                        saveCustomer.setCompany_Name(edit_CompanyName.getText().toString().equalsIgnoreCase("")
                                ? "" : edit_CompanyName.getText().toString().trim());

                        saveCustomer.setAddress("");

                        saveCustomer.setGST_NO(edit_GSTNo.getText().toString().equalsIgnoreCase("")
                                ? "" : edit_GSTNo.getText().toString().trim());

                        saveCustomer.setmMobile_No(edit_Mobile_no.getText().toString().equalsIgnoreCase("")
                                ? "" : edit_Mobile_no.getText().toString().trim());

                        saveCustomer.setBalanceType("");

                        //------------------------------------------------------------------------------//
                        Gson gson12 = new Gson();
                        String jsonInString12 = gson12.toJson(saveCustomer);
                        Log.e("---userInfo---", "saveCustomer---  " + jsonInString12);


                        // ----------------------- Insert into ClsCustomerMaster and get result ----------------------//

                        Log.d("--GSON--", "into: " + _updatedID);

                        //ENTRY MODE IS EDIT THEN DON'T UPDATE CUSTOMER INFO...

//            if (entryMode != null && !entryMode.equalsIgnoreCase("edit")) {

                        if (_updatedID != 0) {
                            saveCustomer.setmId(_updatedID);
                            resultInsertCustomer = ClsCustomerMaster.UpdateInvoiceInfoCustomer(
                                    saveCustomer, InvoiceInfoActivity.this);
                        } else {
                            resultInsertCustomer = ClsCustomerMaster.INSERT(
                                    saveCustomer, InvoiceInfoActivity.this);
                        }

                        if (resultInsertCustomer != 0) {

                            Log.e("resultInsertCustomer", String.valueOf(resultInsertCustomer));
                        } else {
                            Log.e("resultInsertCustomer", String.valueOf(resultInsertCustomer));
                        }


                    }
                    //-------------------------------------------------------------------------------------------//


                    // ----------------------- Insert into ClsInventoryOrderMaster and get result --------------------//

                    if (ClsGlobal._quotationStatusID != null && ClsGlobal._quotationStatusID != "") {
                        getCurrentObj.setQuotationId(Integer.parseInt(ClsGlobal._quotationStatusID));
                    } else {
                        getCurrentObj.setQuotationId(0);
                    }

                    clsInventoryResult = ClsInventoryOrderMaster.Insert(getCurrentObj, list,
                            InvoiceInfoActivity.this, entryMode);
                    Thread.sleep(300);

                    if (clsInventoryResult.getResult() > 0) {
//                        Toast.makeText(getApplicationContext(), "Order Save Successfully",
////                                Toast.LENGTH_LONG).show();
                        if (saleMode.equalsIgnoreCase("Sale")) {
                            Log.e("---krunal---", "saleMode: ");
                            ClsGlobal._OrderNo = "";
                            ClsGlobal.editOrderID = "";

                            Log.e("---krunal---", "_OrderNo: " + String.valueOf(ClsGlobal._OrderNo));

                            ClsGlobal.SetKeepOrderNo(InvoiceInfoActivity.this, "",
                                    "Sale");
                        } else {

                            ClsGlobal._WholeSaleOrderNo = "";
                            ClsGlobal.editOrderID = "";
                            Log.e("---krunal---", "_WholeSaleOrderNo: " + String.valueOf(ClsGlobal._WholeSaleOrderNo));
                            ClsGlobal.SetKeepOrderNo(InvoiceInfoActivity.this, "",
                                    "Wholesale");
                        }


                        //-----------------------------Insert into PaymentMaster Table-----------------------------------//

                        objPaymentMaster = new ClsPaymentMaster();
                        objPaymentMaster.setPaymentDate(ClsGlobal.getCurruntDateTime());
                        objPaymentMaster.setPaymentMounth(ClsGlobal.getDayMonth(ClsGlobal.getEntryDate_dd_MM_yyyy()));
                        objPaymentMaster.setVendorID(0);
                        objPaymentMaster.setMobileNo(edit_Mobile_no.getText().toString().equalsIgnoreCase("")
                                ? "" : edit_Mobile_no.getText().toString().trim());
                        objPaymentMaster.setCustomerName(edit_CustomerName.getText().toString().equalsIgnoreCase("")
                                ? "" : edit_CustomerName.getText().toString().trim());
                        objPaymentMaster.setVendorName("");
                        objPaymentMaster.setPaymentMode(getSelectedPaymentMode);


                        objPaymentMaster.setPaymentDetail(edt_payment_detail.getText().toString().equalsIgnoreCase("")
                                ? "" : edt_payment_detail.getText().toString().trim());


                        objPaymentMaster.setInvoiceNo(getCurrent_Order_No);

                        objPaymentMaster.setAmount(BillAmount * (-1));

                        String PaymentRemark = "".concat("Bill No: ")
                                .concat(ClsGlobal.Current_Order_No);

                        objPaymentMaster.setRemark(PaymentRemark);
                        objPaymentMaster.setEntryDate(ClsGlobal.getEntryDate());
                        objPaymentMaster.setType("Customer");

                        objPaymentMaster.setOrderId(Integer.parseInt(ClsGlobal.InventoryOrderDetail_last_id));

//                        insertPaymentMaster.setReceiptNo(Integer.parseInt(clsInventoryResult.getOrderNo()));

                        objPaymentMaster.setReceiptNo(ClsGlobal.Current_Order_No);

                        Log.e("--Log--", "getReceiptNo: " + objPaymentMaster.getReceiptNo());
                        Log.e("--Log--", "getOrderId: " + objPaymentMaster.getOrderId());


                        //Insert Invoice Amt.
                        //  InsertPaymentVendor(insertPaymentMaster);
                        Double _actualDiffAmt = 0.0;
                        PaymentRemark = "".concat("Bill No: ").concat(ClsGlobal.Current_Order_No);

                        if (txt_pending_amt.getTag() != null && !txt_pending_amt.getTag().toString().equalsIgnoreCase("")) {

                            _actualDiffAmt = Double.valueOf(txt_pending_amt.getTag().toString());

                            Log.e("--Log--", "_actualDiffAmt: " + _actualDiffAmt);

                            if (_actualDiffAmt < 0) {
                                PaidAmount = PaidAmount * (-1);
                                PaymentRemark = PaymentRemark.concat(", Amount Refund.");
                            } else {
                                PaymentRemark = PaymentRemark.concat(", Amount Paid.");
                            }
                        }


                        //Insert Rec. Amt.
                        if (PaidAmount != 0.0) {


                            Log.e("--Payment--", "-----------------------------1stIF-----------------------------");

                            objPaymentMaster.setAmount(PaidAmount);
                            objPaymentMaster.setRemark(PaymentRemark);
                            InsertPayment(objPaymentMaster);
                        }


                        //Insert Diff. Amt.
                        if (DiffAmount != 0 && !rbPending.isChecked()) {
                            if (!rbAdd_To_Credit.isChecked()) {

                                Log.e("--Payment--", "-----------------------------2ndIF-----------------------------");

                                PaymentRemark = PaymentRemark.concat(", Diff Amount Adjusted");
                                objPaymentMaster.setAmount(DiffAmount);
                                objPaymentMaster.setRemark(PaymentRemark);
                                InsertPayment(objPaymentMaster);
                            }
                        }


                        if (edt_sale_return_discount.getText() != null && !edt_sale_return_discount.getText().toString().isEmpty()) {
                            double _saleReturnDis = Double.valueOf(edt_sale_return_discount.getText().toString());

                            if (_saleReturnDis != 0 && _saleReturnDis != 0.0) {
                                Log.e("--Payment--", "-----------------------------3rdIF-----------------------------");

                                PaymentRemark = PaymentRemark.concat(", Sale Return Discount");
                                objPaymentMaster.setAmount(_saleReturnDis * (-1));
                                objPaymentMaster.setRemark(PaymentRemark);
                                InsertPayment(objPaymentMaster);
                            }

                        }

                        // Send sms to ReceiverPhoneNo when bill/Qut is generated.
                        String smsReceiverPhoneNo = ClsGlobal.getSmsReceiver("Sales",
                                InvoiceInfoActivity.this);
                        if (smsReceiverPhoneNo != null
                                && !smsReceiverPhoneNo.equalsIgnoreCase("")) {

                            ClsGlobal.SendSms("No", "Sales",
                                    Integer.parseInt(ClsGlobal.InventoryOrderDetail_last_id),
                                    ClsGlobal.Current_Order_No
                                    , smsReceiverPhoneNo,
                                    edit_CustomerName.getText().toString().equalsIgnoreCase("")
                                            ? "" : edit_CustomerName.getText().toString().trim(), getCurrentObj, null,
                                    String.valueOf(getCurrentObj.getTotalAmount()),
                                    null, "", InvoiceInfoActivity.this);

                        }
                    }


                } catch (Exception e) {
                    Log.e(TAG, "Exception: " + e.getMessage());
                }

                return clsInventoryResult.getResult();
            }


            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                Log.d("--customerReport--", "aVoid: ");

                if (loading.isShowing()) {
                    loading.dismiss();
                }
                ClsGlobal.SetOrderEditMode(getApplicationContext(), "New");

                if (result > 0) {
                    Toast.makeText(getApplicationContext(), "Order Save Successfully",
                            Toast.LENGTH_LONG).show();


                    if (ClsGlobal._quotationStatusID != null && ClsGlobal._quotationStatusID != "") {
//update quotation status
                        ClsQuotationMaster.UpdateStatus(ClsGlobal._quotationStatusID, "INVOICE GENERATED",
                                InvoiceInfoActivity.this);

                    }

                    Intent intent = new Intent(InvoiceInfoActivity.this,
                            OrderSummaryItemActivity.class);
                    intent.putExtra("OrderId",
                            Integer.parseInt(ClsGlobal.InventoryOrderDetail_last_id));
                    intent.putExtra("OrderNo", ClsGlobal.Current_Order_No);
                    intent.putExtra("mode", "InvoiceInfoActivity");
                    intent.putExtra("entryMode", entryMode);
                    intent.putExtra("saleMode", saleMode);
                    intent.putExtra("editSource", editSource);
                    intent.putExtra("title", saleMode);
                    intent.putExtra("_doneBtn", "direct");
                    intent.putExtra("_date", ClsGlobal.getChangeDateFormatDB(edit_select_date.getText().toString().trim()));
                    startActivity(intent);
                }
            }
        };
        insert.execute();
    }

    private void retrieveContactName() {

        String contactName = null;

        // querying contact data store
        Cursor cursor = getContentResolver().query(uri, null,
                null, null, null);

        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex
                    (ContactsContract.Contacts.DISPLAY_NAME));
            edit_CustomerName.setText(contactName);
        }
        cursor.close();
    }

    @SuppressLint("StaticFieldLeak")
    class MessageAsyncTask extends AsyncTask<Void, Void, String> {


        private String mode = "";
        private ProgressDialog loading;

        MessageAsyncTask(String mode) {
            this.mode = mode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading = ClsGlobal._prProgressDialog(InvoiceInfoActivity.this
                    , "Please Wait...", false);
            loading.show();

        }


        @Override
        protected String doInBackground(Void... voids) {

            SaveToDatabase();

            String _whereSend = " AND ORD.[OrderNo] = '" + ClsGlobal.Current_Order_No + "' "
                    .concat(" AND ORD.[OrderID] = ").concat(String.valueOf(
                            InventoryOrderDetail_last_id));
            @SuppressLint("WrongConstant")
            SQLiteDatabase db = openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            list = new ClsInventoryOrderDetail().getListForInvoicePDF(_whereSend,
                    InvoiceInfoActivity.this, db);
            db.close();

            if (mode.equalsIgnoreCase("WhatsApp")) {


                return ClsGlobal.getWhatsAppString(list,
                        getCurrentObj
                        , new ClsPaymentMaster(),
                        InvoiceInfoActivity.this,
                        "Yes");
            } else {


                return ClsGlobal.getSmsString(list, getCurrentObj,
                        edit_CustomerName.getText().toString().trim(),
                        edit_Mobile_no.getText().toString().trim(), "InvoiceInfo",
                        InvoiceInfoActivity.this);
            }

        }


        @Override
        protected void onPostExecute(String message) {
            super.onPostExecute(message);

            if (loading.isShowing()) {
                loading.dismiss();
            }

            if (mode.equalsIgnoreCase("WhatsApp")) {
                String url = null;
                try {
                    url = "https://api.whatsapp.com/send?phone=+91"
                            + edit_Mobile_no.getText().toString() + "&text="
                            + URLEncoder.encode(message, "UTF-8");


                    Log.e("1message- ", message);
                    Log.e("url- ", url);


                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.setAction(Intent.ACTION_VIEW);

                    if (getWhatsAppDefaultApp != null
                            && !getWhatsAppDefaultApp.equalsIgnoreCase("")
                            && getWhatsAppDefaultApp.equalsIgnoreCase("WhatsApp")) {
                        sendIntent.setPackage("com.whatsapp");
                    } else if (getWhatsAppDefaultApp != null
                            && !getWhatsAppDefaultApp.equalsIgnoreCase("")
                            && getWhatsAppDefaultApp.equalsIgnoreCase("Business WhatsApp")) {
                        sendIntent.setPackage("com.whatsapp.w4b");
                    }
                    sendIntent.setData(Uri.parse(url));
                    startActivity(sendIntent);


                    OrdersActivity.OpenSales = true;
                    finish();


                } catch (Exception e) {
                    if (getWhatsAppDefaultApp != null
                            && !getWhatsAppDefaultApp.equalsIgnoreCase("")
                            && getWhatsAppDefaultApp.equalsIgnoreCase("WhatsApp")) {
                        Toast.makeText(InvoiceInfoActivity.this, "WhatsApp Not Install!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(InvoiceInfoActivity.this, "Business WhatsApp Not Install!", Toast.LENGTH_LONG).show();
                    }

                }

            } else {
                OrdersActivity.OpenSales = true;
                ClsGlobal.sendSMS(InvoiceInfoActivity.this,
                        edit_Mobile_no.getText().toString(), message);

//                SaveToDatabase();
                finish();
            }

        }
    }


    private void InsertPayment(ClsPaymentMaster insertPaymentMaster) {


        Gson gson1 = new Gson();
        String jsonInString1 = gson1.toJson(insertPaymentMaster);
        Log.e("--Payment--", "jsonInString1: " + jsonInString1);


        Log.e("--Payment--", "----- 1st____INSERT PAYMENT CALL -----");
        int resultPaymentMaster = ClsPaymentMaster.Insert(insertPaymentMaster, InvoiceInfoActivity.this);

        Log.e("--Payment--", "----- 2nd____INSERT PAYMENT CALL -----");
    }


    private boolean validation() {
        Boolean result = true;

        if (entryMode.equalsIgnoreCase("New")) {

            if (rb_customer.isChecked()) {
                Matcher matcher = MobileNo_Pattern.matcher(edit_Mobile_no.getText().toString());

                if (!matcher.matches()
                        || edit_Mobile_no.getText().length() < 10) {
                    Toast.makeText(getApplicationContext(), "Invalid Mobile No!", Toast.LENGTH_SHORT).show();

                    return false;
                } else if (edit_CustomerName.getText() == null || edit_CustomerName.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Customer Name Required!", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (edt_received_amt.getText() == null || edt_received_amt.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Received Amt !", Toast.LENGTH_SHORT).show();
                    return false;
                }

            } else if (rb_none.isChecked()) {
                if (edt_received_amt.getText() == null || edt_received_amt.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Received Amt Required!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

        } else {

            if (rbPending.isChecked()) {
                if (edt_payment_detail.getText() == null || edt_payment_detail.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Details is required!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }


        }


        return result;
    }

    private void SaveData_SharedPreferences(String str) {
        SharedPreferences.Editor preferencesEditor = mPreferencesDefault.edit();
        preferencesEditor.putString("WhatsApp Default App", str);
        preferencesEditor.apply();

    }
}
