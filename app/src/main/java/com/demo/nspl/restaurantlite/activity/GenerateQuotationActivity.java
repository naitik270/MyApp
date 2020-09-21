package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.nspl.restaurantlite.Adapter.CustomerAdapter;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsCustomerMaster;
import com.demo.nspl.restaurantlite.classes.ClsQuotationMaster;
import com.demo.nspl.restaurantlite.classes.ClsQuotationOrderDetail;
import com.demo.nspl.restaurantlite.classes.ClsQuotationResult;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;


public class GenerateQuotationActivity extends AppCompatActivity {

    ClsQuotationMaster getCurrentObj;
    String Mode = "", grandTotal = "";
    String amount = "";
    String saleMode = "";
    String entryMode = "";

    MaterialButtonToggleGroup toggleGroup;
    public static String getCurrentQuotationNo = "";
    ImageButton bt_close;
    TextView txt_select_date, txt_valid_date, txt_title_amt;
    int mYear, mMonth, mDay;
    Button btn_1day, btn_2day, btn_1week, btn_2week, btn_15days, btn_30days;
    ImageButton iv_clear;
    ImageView img_more_details;
    boolean isVisible = true;

    ImageButton iv_search_contact, iv_call_browse;

    private Dialog dialogVendor;
    CustomerAdapter customerAdapter;
    private StickyListHeadersListView lst;
    RelativeLayout lyout_nodata;
    EditText edt_mobile_no, edt_customer_name, edt_company_name, edt_gst_no;
    int _updatedID = 0;
    private static final Pattern MobileNo_Pattern
            = Pattern.compile(ClsGlobal.MobileNo_Pattern);
    MaterialButton btn_save;
    List<ClsQuotationOrderDetail> lstQuotation = new ArrayList<>();

    TextView txt_email, txt_address;
    LinearLayout ll_cust_detail;
    String discount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_quotation);

        main();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void main() {

        getCurrentObj = (ClsQuotationMaster) getIntent().getSerializableExtra("ClsQuotationMaster");


        Gson gson = new Gson();
        String jsonInString = gson.toJson(getCurrentObj);
        Log.d("--Demo--", "getCurrentObj:  " + jsonInString);


        saleMode = getIntent().getStringExtra("saleMode");
        entryMode = getIntent().getStringExtra("entryMode");


        Mode = getIntent().getStringExtra("Mode");

        Log.d("--customerReport--", "Mode: " + Mode);


        grandTotal = getIntent().getStringExtra("grandTotal");
        discount = getIntent().getStringExtra("discount");
        amount = getIntent().getStringExtra("amount");

        if (saleMode.equalsIgnoreCase("Retail Quotation")) {
            getCurrentQuotationNo = ClsGlobal._QuotationSaleOrderNo;
        } else if (saleMode.equalsIgnoreCase("Wholesale Quotation")) {
            getCurrentQuotationNo = ClsGlobal._QuotationWholesaleOrderNo;
        }

        lstQuotation = new ArrayList<>();

        String _where = " AND [QuotationNo] = '" + getCurrentQuotationNo + "' ";

        if (ClsGlobal.editQuotationID != 0) {
            _where += "".concat(" AND [QuotationID] = " + ClsGlobal.editQuotationID + "");
        }

        @SuppressLint("WrongConstant")
        SQLiteDatabase db = openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

        lstQuotation = new ClsQuotationOrderDetail().getQuotationDetailList(
                _where, GenerateQuotationActivity.this, db);

        db.close();

        ll_cust_detail = findViewById(R.id.ll_cust_detail);
        txt_address = findViewById(R.id.txt_address);
        txt_email = findViewById(R.id.txt_email);
        btn_save = findViewById(R.id.btn_save);
        txt_title_amt = findViewById(R.id.txt_title_amt);
        txt_title_amt.setText("Bill Amount: " + grandTotal);

        btn_1day = findViewById(R.id.btn_1day);
        btn_2day = findViewById(R.id.btn_2day);
        btn_1week = findViewById(R.id.btn_1week);
        btn_2week = findViewById(R.id.btn_2week);
        btn_15days = findViewById(R.id.btn_15days);
        btn_30days = findViewById(R.id.btn_30days);

        edt_mobile_no = findViewById(R.id.edt_mobile_no);
        edt_customer_name = findViewById(R.id.edt_customer_name);
        edt_company_name = findViewById(R.id.edt_company_name);
        edt_gst_no = findViewById(R.id.edt_gst_no);

        iv_search_contact = findViewById(R.id.iv_search_contact);
        iv_call_browse = findViewById(R.id.iv_call_browse);
        iv_clear = findViewById(R.id.iv_clear);

        img_more_details = findViewById(R.id.img_more_details);
        txt_valid_date = findViewById(R.id.txt_valid_date);
        txt_select_date = findViewById(R.id.txt_select_date);
        toggleGroup = findViewById(R.id.toggleGroup);
        bt_close = findViewById(R.id.bt_close);

        txt_select_date.setText(ClsGlobal.getEntryDateAndTime());
        txt_select_date.setTag(ClsGlobal.getEntryDateAndTime());

        edt_mobile_no.addTextChangedListener(new TextWatcher() {
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
                    edt_customer_name.setText("");
                    edt_company_name.setText("");
                    edt_gst_no.setText("");
                    txt_email.setText("");
                    txt_address.setText("");
                }
            }
        });

        iv_call_browse.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, 1);
            }
        });

        iv_search_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillCustomerList();

                if (list_customer != null && list_customer.size() != 0) {
                    CustomerDialog();
                } else {
                    Toast.makeText(GenerateQuotationActivity.this,
                            "No customer found!", Toast.LENGTH_LONG).show();
                }
            }
        });

//         yasin.nathani823@gmail.com
        img_more_details.setOnClickListener(v -> {
            if (isVisible) {
                ll_cust_detail.setVisibility(View.VISIBLE);
                edt_company_name.setVisibility(View.VISIBLE);
                edt_gst_no.setVisibility(View.VISIBLE);
                img_more_details.setImageResource(R.drawable.ic_arrow_drop_down);
                isVisible = false;
            } else {
                ll_cust_detail.setVisibility(View.GONE);
                edt_company_name.setVisibility(View.GONE);
                edt_gst_no.setVisibility(View.GONE);
                img_more_details.setImageResource(R.drawable.ic_arrow_up);
                isVisible = true;
            }
        });

        iv_clear.setOnClickListener(v -> {
            txt_valid_date.setText("");
        });

        quotationDateSelection();
        validDate();

        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            switch (checkedId) {
                case R.id.btn_1day:

                    Log.d("--Date--", "isChecked: " + isChecked);
                    try {
                        Log.d("--Date--", "isChecked_btn_1week: " + isChecked);
                        if (isChecked) {
                            String _dateTime = ClsGlobal.getOnlyDate(txt_select_date.getText().toString());
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            Date dateObj = sdf.parse(_dateTime);
                            String _expDate = ClsGlobal.getQuotExpDate(dateObj, 1);
                            txt_valid_date.setText(_expDate);
                            txt_valid_date.setTag(ClsGlobal.getValidDateFormat(_expDate));

                        }
                    } catch (Exception e) {
                        Log.d("--Date--", "_expDate: " + e.getMessage());
                    }

                    break;

                case R.id.btn_2day:
//                   https://www.youtube.com/watch?v=JgKN3BuvC3E

                    try {

                        Log.d("--Date--", "isChecked_btn_1week: " + isChecked);

                        if (isChecked) {
                            String _dateTime = ClsGlobal.getOnlyDate(txt_select_date.getText().toString());
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            Date dateObj = sdf.parse(_dateTime);
                            String _expDate = ClsGlobal.getQuotExpDate(dateObj, 2);
                            txt_valid_date.setText(_expDate);
                            txt_valid_date.setTag(ClsGlobal.getValidDateFormat(_expDate));

                        }
                    } catch (Exception e) {
                        Log.d("--Date--", "_expDate: " + e.getMessage());
                    }

                    break;

                case R.id.btn_1week:
                    try {

                        Log.d("--Date--", "isChecked_btn_1week: " + isChecked);

                        if (isChecked) {
                            String _dateTime = ClsGlobal.getOnlyDate(txt_select_date.getText().toString());
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            Date dateObj = sdf.parse(_dateTime);
                            String _expDate = ClsGlobal.getQuotExpDate(dateObj, 7);
                            txt_valid_date.setText(_expDate);
                            txt_valid_date.setTag(ClsGlobal.getValidDateFormat(_expDate));

                        }
                    } catch (Exception e) {
                        Log.d("--Date--", "_expDate: " + e.getMessage());
                    }
                    break;

                case R.id.btn_2week:
                    try {

                        Log.d("--Date--", "isChecked_btn_2week: " + isChecked);

                        if (isChecked) {

                            String _dateTime = ClsGlobal.getOnlyDate(txt_select_date.getText().toString());
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            Date dateObj = sdf.parse(_dateTime);
                            String _expDate = ClsGlobal.getQuotExpDate(dateObj, 14);
                            txt_valid_date.setText(_expDate);
                            txt_valid_date.setTag(ClsGlobal.getValidDateFormat(_expDate));
                        }
                    } catch (Exception e) {
                        Log.d("--Date--", "_expDate: " + e.getMessage());
                    }
                    break;

                case R.id.btn_15days:
                    try {
                        String _dateTime = ClsGlobal.getOnlyDate(txt_select_date.getText().toString());
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date dateObj = sdf.parse(_dateTime);
                        String _expDate = ClsGlobal.getQuotExpDate(dateObj, 15);
                        txt_valid_date.setText(_expDate);
                        txt_valid_date.setTag(ClsGlobal.getValidDateFormat(_expDate));

                    } catch (Exception e) {
                        Log.d("--Date--", "_expDate: " + e.getMessage());
                    }
                    break;

                case R.id.btn_30days:
                    try {
                        String _dateTime = ClsGlobal.getOnlyDate(txt_select_date.getText().toString());
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date dateObj = sdf.parse(_dateTime);
                        String _expDate = ClsGlobal.getQuotExpDate(dateObj, 30);
                        txt_valid_date.setText(_expDate);
                        txt_valid_date.setTag(ClsGlobal.getValidDateFormat(_expDate));

                    } catch (Exception e) {
                        Log.d("--Date--", "_expDate: " + e.getMessage());
                    }
                    break;

                default:
                    if (!isChecked) {
                        txt_valid_date.setText("");
                        txt_valid_date.setTag("");
                    }
            }
        });

        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        fillValue();


        btn_save.setOnClickListener(v -> {

            if (validation()) {

                moveToOrderSummery();
            }

        });

    }

    void moveToOrderSummery() {

        SaveToDatabase();

    }


    private ProgressDialog loading;
//    String _select_date = "";

    void quotationDateSelection() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        txt_select_date.setOnClickListener(view -> {

            DatePickerDialog dpd = new DatePickerDialog(GenerateQuotationActivity.this,
                    (view1, year, month, day) -> {

                        c.set(year, month, day);
                        @SuppressLint("SimpleDateFormat")
                        String _validDate = new SimpleDateFormat("dd/MM/yyyy hh:mm aa").format(c.getTime());

//                        _select_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime());

//                        txt_select_date.setText(ClsGlobal.getOnlyDate(_validDate));
                        txt_select_date.setText(_validDate);
                        txt_select_date.setTag(_validDate);

                        Log.d("--format--", "3rdDate: " + ClsGlobal.getOnlyDate(_validDate));
                        Log.d("--format--", "4rdDateTag: " + _validDate);

                        txt_valid_date.setText("");
                    }, mYear, mMonth, mDay);


            dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            if (!dpd.isShowing()) {
                dpd.show();
            }
        });
    }

    void validDate() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        txt_valid_date.setOnClickListener(view -> {

            DatePickerDialog dpd = new DatePickerDialog(GenerateQuotationActivity.this,
                    (view1, year, month, day) -> {

                        c.set(year, month, day);
                        @SuppressLint("SimpleDateFormat")
                        String _validDate = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());

                        txt_valid_date.setText(_validDate);


                        txt_valid_date.setTag(ClsGlobal.getValidDateFormat(_validDate));

                    }, mYear, mMonth, mDay);


            dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            if (!dpd.isShowing()) {
                dpd.show();
            }
        });
    }

    private List<ClsCustomerMaster> list_customer;

    private void fillCustomerList() {
        list_customer = new ArrayList<>();
        list_customer = new ClsCustomerMaster().ListCustomers("", GenerateQuotationActivity.this);
        Log.e("list_customer", String.valueOf(list_customer.size()));

        Gson gson = new Gson();
        String jsonInString = gson.toJson(list_customer);
        Log.e("--CustomerList--", "filterStr: " + jsonInString);
    }

    private void CustomerDialog() {

        final EditText edit_search;
        dialogVendor = new Dialog(GenerateQuotationActivity.this);
        dialogVendor.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogVendor.setContentView(R.layout.dialog_type_n_search);
        dialogVendor.setTitle("Select Customer");
        dialogVendor.setCanceledOnTouchOutside(true);
        dialogVendor.setCancelable(true);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogVendor.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogVendor.getWindow().setAttributes(lp);

        lst = dialogVendor.findViewById(R.id.list);
        lyout_nodata = dialogVendor.findViewById(R.id.lyout_nodata);
        edit_search = dialogVendor.findViewById(R.id.edit_search);

        Handler handler = new Handler();
        handler.postDelayed(() -> {

            try {

                if (list_customer != null && list_customer.size() != 0) {

                    customerAdapter = new CustomerAdapter(GenerateQuotationActivity.this);
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
                                    GenerateQuotationActivity.this);

                            customerAdapter.AddItems(list_customer);

                            lst.setAdapter(customerAdapter);

                        }
                    });

                    customerAdapter.SetOnClickListener(clsCustomerMaster -> {

                        edt_mobile_no.setText(clsCustomerMaster.getmMobile_No());
                        dialogVendor.dismiss();

                    });
                }


            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            }

        }, 400);
        dialogVendor.show();

    }


    void fillValue() {

        if (entryMode != null && entryMode.equalsIgnoreCase("edit")) {

            @SuppressLint("WrongConstant")
            SQLiteDatabase db = openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            ClsQuotationMaster _obj = ClsQuotationMaster.getFillQuotationOn(getCurrentQuotationNo,
                    ClsGlobal.editQuotationID, GenerateQuotationActivity.this, db);
            db.close();

            if (!_obj.getQuotationNo().equalsIgnoreCase("0")
                    && !_obj.getQuotationNo().isEmpty()) {

                edt_mobile_no.setText(_obj.getMobileNo());
                edt_customer_name.setText(_obj.getCustomerName());
                txt_valid_date.setText(ClsGlobal.getDDMYYYYFormat(_obj.getValidUptoDate()));
                edt_company_name.setText(_obj.getCompanyName());
                edt_gst_no.setText(_obj.getGSTNo());
                txt_address.setText(_obj.getCustAddress());
                txt_email.setText(_obj.getCustEmail());

                setCustomerDetails(_obj.getMobileNo());
            }
        }
    }


    private void setCustomerDetails(String txtValue) {

        String where = "AND [MOBILE_NO] = ".concat(txtValue);

        ClsCustomerMaster getCurrentCustomer =
                ClsCustomerMaster.getCustomerByMobileNo(where, GenerateQuotationActivity.this);
        _updatedID = getCurrentCustomer.getmId();

        if (getCurrentCustomer != null) {
            edt_customer_name.setText(getCurrentCustomer.getmName());
            edt_company_name.setText(getCurrentCustomer.getCompany_Name());
            edt_gst_no.setText(getCurrentCustomer.getGST_NO());
            txt_email.setText(getCurrentCustomer.getEmail());
            txt_address.setText(getCurrentCustomer.getAddress());
        }
    }


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
                        edt_mobile_no.setText(phone);
                        retrieveContactName();
                    } else {
                        edt_mobile_no.setText("");
                        edt_customer_name.setText("");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private Uri uri = null;


    private void retrieveContactName() {

        String contactName = null;

        // querying contact data store
        Cursor cursor = getContentResolver().query(uri, null,
                null, null, null);

        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex
                    (ContactsContract.Contacts.DISPLAY_NAME));
            edt_customer_name.setText(contactName);
        }
        cursor.close();
    }

    private boolean validation() {
        boolean result = true;

        if (entryMode.equalsIgnoreCase("New")) {

            Matcher matcher = MobileNo_Pattern.matcher(edt_mobile_no.getText().toString());
            if (!matcher.matches()
                    || edt_mobile_no.getText().length() < 10) {
                Toast.makeText(getApplicationContext(), "Invalid Mobile No!", Toast.LENGTH_SHORT).show();

                return false;
            } else if (edt_customer_name.getText() == null || edt_customer_name.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Customer Name Required!", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (txt_valid_date.getText() != null && !txt_valid_date.getText().toString().equalsIgnoreCase("")) {

                if (!CheckDates(ClsGlobal.getOnlyDate(txt_select_date.getText().toString()), txt_valid_date.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Valid date should not be greater than select date", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }

        return result;
    }

    SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");

    public boolean CheckDates(String d1, String d2) {

        boolean b = false;

        try {
            if (dfDate.parse(d1).before(dfDate.parse(d2))) {
                b = true;   //If start date is before end date
            } else if (dfDate.parse(d1).equals(dfDate.parse(d2))) {
                b = true;   //If two dates are equal
            } else {
                b = false; //If start date is after the end date
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
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
                loading = ClsGlobal._prProgressDialog(GenerateQuotationActivity.this
                        , "Please Wait...", false);
                loading.show();
            }

            @Override
            protected Integer doInBackground(Void... voids) {
                //save customer to database
                //if exists then update
                //else insert like invoice


                ClsQuotationResult clsQuotationResult = new ClsQuotationResult();

                try {

//                    getCurrentObj.setQuotationDate(ClsGlobal.getDDMYYYYFormat(txt_select_date.getText().toString().trim()));//20/02/2019 02:21 pm | 2019-02-20 15:21:00
//                    getCurrentObj.setValidUptoDate(ClsGlobal.getDDMYYYYFormat(txt_valid_date.getText().toString().trim()));//20/02/2019 02:21 pm | 2019-02-20 15:21:00

                    getCurrentObj.setQuotationDate(ClsGlobal.getChangeDateFormatDB(txt_select_date.getTag().toString()));//20/02/2019 02:21 pm | 2019-02-20 15:21:00
                    getCurrentObj.setValidUptoDate(ClsGlobal.getValidDateFormat(txt_valid_date.getText().toString().trim()));//20/02/2019 02:21 pm | 2019-02-20 15:21:00
                    getCurrentObj.setMobileNo(edt_mobile_no.getText().toString().equalsIgnoreCase("")
                            ? "" : edt_mobile_no.getText().toString().trim()); //+

                    getCurrentObj.setCustomerName(edt_customer_name.getText().toString().equalsIgnoreCase("")
                            ? "" : edt_customer_name.getText().toString().trim());

                    getCurrentObj.setCompanyName(edt_company_name.getText().toString().equalsIgnoreCase("")
                            ? "" : edt_company_name.getText().toString().trim());

                    getCurrentObj.setGSTNo(edt_gst_no.getText().toString().equalsIgnoreCase("")
                            ? "" : edt_gst_no.getText().toString().trim()); //+

                    getCurrentObj.setTotalAmount(getCurrentObj.getTotalAmount());
                    getCurrentObj.setDiscountAmount(getCurrentObj.getDiscountAmount());
                    getCurrentObj.setGrandTotal(Double.parseDouble(grandTotal));

//                    getCurrentObj.setEntryDate(ClsGlobal.getEntryDate());

                    // QuotationNo is Pending....

//                    getCurrentObj.setQuotationNo(ClsGlobal.current_quotation_no);
                    getCurrentObj.setApplyTax(getCurrentObj.getApplyTax());

                    getCurrentObj.setCustEmail(txt_email.getText().toString());
                    getCurrentObj.setCustAddress(txt_address.getText().toString());
                    getCurrentObj.setQuotationType(saleMode);


                    //-------------------------------------------------------------------------------------------//

                    getCurrentObj.setStatus("PENDING");

                    //-------------------------------------------------------------------------------------------//


//------------------------------- Save Customer Details ----------------------//


                    ClsCustomerMaster saveCustomer = new ClsCustomerMaster();
                    saveCustomer.setmName(edt_customer_name.getText().toString().equalsIgnoreCase("")
                            ? "" : edt_customer_name.getText().toString().trim());

                    saveCustomer.setCompany_Name(edt_company_name.getText().toString().equalsIgnoreCase("")
                            ? "" : edt_company_name.getText().toString().trim());

                    saveCustomer.setAddress("");

                    saveCustomer.setGST_NO(edt_gst_no.getText().toString().equalsIgnoreCase("")
                            ? "" : edt_gst_no.getText().toString().trim());

                    saveCustomer.setmMobile_No(edt_mobile_no.getText().toString().equalsIgnoreCase("")
                            ? "" : edt_mobile_no.getText().toString().trim());

                    saveCustomer.setBalanceType("");

                    //------------------------------------------------------------------------------//

                    Gson gson12 = new Gson();
                    String jsonInString12 = gson12.toJson(getCurrentObj);
                    Log.e("--getCurrentObj--", "Gson: " + jsonInString12);


                    // ----------------------- Insert into ClsCustomerMaster and get result ----------------------//

                    Log.d("--GSON--", "into: " + _updatedID);

                    if (_updatedID != 0) {
                        saveCustomer.setmId(_updatedID);
                        resultInsertCustomer = ClsCustomerMaster.UpdateInvoiceInfoCustomer(
                                saveCustomer, GenerateQuotationActivity.this);
                    } else {
                        resultInsertCustomer = ClsCustomerMaster.INSERT(
                                saveCustomer, GenerateQuotationActivity.this);
                    }


                    if (resultInsertCustomer != 0) {

                        Log.e("resultInsertCustomer", String.valueOf(resultInsertCustomer));
                    } else {
                        Log.e("resultInsertCustomer", String.valueOf(resultInsertCustomer));
                    }

                    // ----------------------- Insert into ClsQuotationMaster and get result --------------------//
                    clsQuotationResult = ClsQuotationMaster.InsertQuotation(getCurrentObj, lstQuotation,
                            GenerateQuotationActivity.this, entryMode);
                    Thread.sleep(300);

                    if (clsQuotationResult.getResult() > 0) {

                        if (saleMode.equalsIgnoreCase("Retail Quotation")) {

                            ClsGlobal._QuotationSaleOrderNo = "";
                            ClsGlobal.editQuotationID = 0;

                            ClsGlobal.SetKeepOrderNo(GenerateQuotationActivity.this,
                                    "",
                                    "Retail Quotation");

                        } else if (saleMode.equalsIgnoreCase("Wholesale Quotation")) {

                            ClsGlobal._QuotationWholesaleOrderNo = "";
                            ClsGlobal.editQuotationID = 0;

                            ClsGlobal.SetKeepOrderNo(GenerateQuotationActivity.this,
                                    "",
                                    "Wholesale Quotation");
                        }

                        // Send sms to ReceiverPhoneNo when bill/Qut is generated.
                        String smsReceiverPhoneNo = ClsGlobal.getSmsReceiver("Quotation",
                                GenerateQuotationActivity.this);
                        if (smsReceiverPhoneNo != null
                                && !smsReceiverPhoneNo.equalsIgnoreCase("")) {


                            ClsGlobal.SendSms("No", "Quotation",
                                    Integer.parseInt(ClsGlobal.GenrateQuotationDetail_last_id),
                                    ClsGlobal.QuotationNo
                                    , smsReceiverPhoneNo,
                                    edt_customer_name.getText().toString().equalsIgnoreCase("")
                                            ? "" : edt_customer_name.getText().toString().trim(), null,
                                    getCurrentObj,
                                    String.valueOf(getCurrentObj.getTotalAmount()),
                                    null, "", GenerateQuotationActivity.this);

                        }
                    }

                } catch (Exception e) {
                    Log.d("--InsertQuotation--", "Exception: " + e.getMessage());
                }
                return clsQuotationResult.getResult();
            }


            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                Log.d("--customerReport--", "aVoid: ");

                if (loading.isShowing()) {
                    loading.dismiss();
                }


                ClsGlobal.SetOrderEditMode(getApplicationContext(), "New");
                Intent intent = new Intent(GenerateQuotationActivity.this,
                        OrderSummeryQuotationActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("ClsQuotationMaster", getCurrentObj);
                intent.putExtra("Mode", "GenerateQuotationActivity");
                intent.putExtra("title", getCurrentObj.getQuotationType());


                intent.putExtra("saleMode", saleMode);
                intent.putExtra("entryMode", entryMode);
                intent.putExtra("_taxApple", getCurrentObj.getApplyTax());
                intent.putExtra("_quotationId", ClsGlobal.GenrateQuotationDetail_last_id);

//                Log.d("--customerReport--", "_quotationId: " + ClsGlobal.GenrateQuotationDetail_last_id);
//                Log.d("--customerReport--", "_quotationNo: " + ClsGlobal.QuotationNo);

                intent.putExtra("_quotationNo", ClsGlobal.QuotationNo);
                intent.putExtra("_doneBtn", "direct");

                startActivity(intent);

            }
        };
        insert.execute();
    }


}
