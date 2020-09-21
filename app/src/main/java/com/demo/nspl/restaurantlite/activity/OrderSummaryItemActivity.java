package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.print.PDFConverter;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.CustomerAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.ClsHtmlToPdf;
import com.demo.nspl.restaurantlite.Global.ClsPrintPdf;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.SMS.ClsMessageFormat;
import com.demo.nspl.restaurantlite.classes.ClsCustomerMaster;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderDetail;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster;
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;
import com.demo.nspl.restaurantlite.classes.ClsUserInfo;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.SelectCountryDialog;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.SendPdf;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.formatPhoneNo;

public class OrderSummaryItemActivity extends AppCompatActivity {

    private static final String TAG = OrderSummaryItemActivity.class.getSimpleName();
    Toolbar toolbar;
    RecyclerView rv;
    List<ClsInventoryOrderDetail> list_order = new ArrayList<>();
    TextView tv_Total, txt_discount, tv_Net_Amount, txt_tax_amount,
            txt_grand_total, txt_order_no, tv_mobile, tv_customer_name;
    int getOrderId = 0;
    ClsUserInfo objClsUserInfo;
    String _doneBtn = "";
    private List<ClsCustomerMaster> list_customer;
    private Double getDiscountIntent = 0.0, getAmount = 0.0, getTaxAmount = 0.0, getBillAmount = 0.0;
    String getMobileNo = "", getCustomerName = "", _applyTax = "", mode = "";
    String getOrderNo = "", entryMode = "", editSource = "", saleMode = "", getWhatsAppDefaultApp = "", DefaultMessageFormat = "";
    Double SaleReturnDiscount;
    EditText mEditMobile;
    public int PICK_CONTACT = 2;
    private ProgressDialog loading;
    TextView txt_sale_return_discount;
    ClsInventoryOrderMaster result;
    CustomerAdapter customerAdapter;
    private Dialog dialogvendor;
    RelativeLayout lyout_nodata;
    private StickyListHeadersListView lst;
    private LinearLayout ll_wtsApp, ll_send_sms, ll_generate_pdf,
            ll_send_sms_attactment, ll_send_sms_no_attactment,
            ll_share, ll_edit, ll_email_invoice, ll_done;
    private SharedPreferences mPreferencesDefault;
    private static final String mPreferncesName = "MyPerfernces";
    FloatingActionButton fab_up;
    BottomSheetBehavior sheetBehavior;
    TextView txt_title, txt_valid_up_to, select_country;
    String title = "", _date = "";


    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_summery_activity);


        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "OrderDetailInfoActivity"));
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txt_order_no = findViewById(R.id.txt_order_no);
        tv_Total = findViewById(R.id.txt_total);
        txt_sale_return_discount = findViewById(R.id.txt_sale_return_discount);
        txt_discount = findViewById(R.id.txt_discount);
        tv_Net_Amount = findViewById(R.id.tv_Net_Amount);
        txt_tax_amount = findViewById(R.id.txt_tax_amount);
        txt_grand_total = findViewById(R.id.txt_grand_total);
        tv_mobile = findViewById(R.id.tv_mobile);
        tv_customer_name = findViewById(R.id.tv_customer_name);
        txt_title = findViewById(R.id.txt_title);
        txt_valid_up_to = findViewById(R.id.txt_valid_up_to);

        fab_up = findViewById(R.id.fab_up);
        fab_up.setColorFilter(Color.WHITE);

        ll_wtsApp = findViewById(R.id.ll_wtsApp);
        ll_send_sms = findViewById(R.id.ll_send_sms);
        ll_generate_pdf = findViewById(R.id.ll_generate_pdf);
        ll_send_sms_attactment = findViewById(R.id.ll_send_sms_attactment);
        ll_send_sms_no_attactment = findViewById(R.id.ll_send_sms_no_attactment);
        ll_share = findViewById(R.id.ll_share);
        ll_edit = findViewById(R.id.ll_edit);
        ll_done = findViewById(R.id.ll_done);
        ll_email_invoice = findViewById(R.id.ll_email_invoice);
        LinearLayout bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);

        getOrderId = getIntent().getIntExtra("OrderId", 0);
        getOrderNo = getIntent().getStringExtra("OrderNo");//orderNo
        mode = getIntent().getStringExtra("mode");//orderNo
        entryMode = getIntent().getStringExtra("entryMode");
        editSource = getIntent().getStringExtra("editSource");
        saleMode = getIntent().getStringExtra("saleMode");
        _date = getIntent().getStringExtra("_date");
        _doneBtn = getIntent().getStringExtra("_doneBtn");
        mPreferencesDefault = getSharedPreferences(mPreferncesName, MODE_PRIVATE);

        objClsUserInfo = ClsGlobal.getUserInfo(OrderSummaryItemActivity.this);

        txt_title.setText("SUMMARY");

        if (getOrderNo != null) {
            txt_order_no.setText("NO# " + getOrderNo);
        }

        String where = (" AND IOM.OrderNo =" +
                " '").concat(String.valueOf(getOrderNo).concat("'")
                + " AND IOM.OrderID = " + getOrderId);

        result = ClsInventoryOrderMaster.getOrderDetail(where,
                OrderSummaryItemActivity.this);

        getAmount = result.getTotalAmount();
        getTaxAmount = result.getTotalTaxAmount();
        getBillAmount = result.getTotalReceiveableAmount();
        getDiscountIntent = result.getDiscountAmount();
        SaleReturnDiscount = result.getSaleReturnDiscount();
        getMobileNo = result.getMobileNo();
        _applyTax = result.getApplyTax();
        getCustomerName = result.getCustomerName();

        if (result.getMobileNo() != null && !result.getMobileNo().equalsIgnoreCase("")) {
            tv_mobile.setText("MOBILE: " + result.getMobileNo());
            tv_customer_name.setText("NAME: " + result.getCustomerName().toUpperCase());
        } else {
            tv_mobile.setText("MOBILE: NO NUMBER");
            tv_customer_name.setText("NAME: NO NAME");
        }

        fab_up.setOnClickListener(view -> {
            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                fab_up.setImageResource(R.drawable.ic_arrow_drop_down);
                fab_up.setColorFilter(Color.WHITE);
            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                fab_up.setImageResource(R.drawable.ic_arrow_up);
                fab_up.setColorFilter(Color.WHITE);
            }
        });

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        fab_up.setImageResource(R.drawable.ic_arrow_drop_down);
                        fab_up.setColorFilter(Color.WHITE);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        fab_up.setImageResource(R.drawable.ic_arrow_up);
                        fab_up.setColorFilter(Color.WHITE);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        ViewData();
    }


    @SuppressLint("CheckResult")
    private void ViewData() {


        ll_wtsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenMobileNumber_Dialog("WhatsApp");
            }
        });


        ll_done.setOnClickListener(v -> {

            ClsGlobal.SetOrderEditMode(getApplicationContext(), "New");

            if (_doneBtn.equalsIgnoreCase("direct")) {

                Intent intent = new Intent(OrderSummaryItemActivity.this,
                        SHAP_Lite_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            } else {
                finish();
            }

        });

        ll_send_sms.setOnClickListener(v -> {
            OpenMobileNumber_Dialog("SMS");
        });

        ll_share.setOnClickListener(v -> {
            loading = ClsGlobal._prProgressDialog(OrderSummaryItemActivity.this
                    , "Generating PDF File...", false);

            loading.show();
            String where = " AND [OrderNo] = ".concat("'").concat(String.valueOf(getOrderNo)).concat("'")
                    .concat("AND [OrderID] = ").concat(String.valueOf(getOrderId));

//            list_order = ClsInventoryOrderDetail.getList(where, OrderSummaryItemActivity.this);
//
            @SuppressLint("WrongConstant") SQLiteDatabase db = openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            list_order = new ClsInventoryOrderDetail().getListForInvoicePDF(where,
                    OrderSummaryItemActivity.this, db);
            db.close();

            ClsPrintPdf clsPrintPdf = ClsHtmlToPdf.generatePDF(OrderSummaryItemActivity.this,
                    result, list_order, "Send To WhatsApp");

            Log.e("clsPrintPdf", clsPrintPdf.getPdfString());

            PDFConverter converter = PDFConverter.getInstance();
            converter.convert(OrderSummaryItemActivity.this, clsPrintPdf.getPdfString(),
                    new File(ClsGlobal.CREATE_PDF_FILE_PATH.concat(ClsGlobal.InvoiceFileName)));


            converter.SetOnPDFCreatedListener((filePath) -> {

                if (loading.isShowing()) {
                    loading.dismiss();
                }

                if (!getMobileNo.equalsIgnoreCase("")) {
                    ClsGlobal.copyToClipboard(getMobileNo,
                            OrderSummaryItemActivity.this);

                    ClsGlobal.sendNotification("Send PDF File",
                            "Current Mobile Number: ".concat(getMobileNo)
                            , "Send PDF", OrderSummaryItemActivity.this);

                }

//                ClsGlobal.SendPdfWhatsApp(OrderSummaryItemActivity.this,
//                        clsPrintPdf.getPdfFilePath());

                SendPdf(OrderSummaryItemActivity.this, clsPrintPdf.getPdfFilePath(), getOrderNo);

                loading = null;


            });
        });


        ll_generate_pdf.setOnClickListener(v -> {

            loading = ClsGlobal._prProgressDialog(OrderSummaryItemActivity.this
                    , "Generating PDF File...", false);

            loading.show();

            String where = " AND [OrderNo] = ".concat("'").concat(String.valueOf(getOrderNo)).concat("'")
                    .concat("AND [OrderID] = ").concat(String.valueOf(getOrderId));

            Log.d("--sendPDF--", "where: " + where);

            @SuppressLint("WrongConstant") SQLiteDatabase db = openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            list_order = new ClsInventoryOrderDetail().getListForInvoicePDF(where,
                    OrderSummaryItemActivity.this, db);
            db.close();

            ClsPrintPdf clsPrintPdf = ClsHtmlToPdf.generatePDF(getApplicationContext(),
                    result, list_order, "Generate Pdf");


            Gson gson = new Gson();
            String toJson = gson.toJson(result);
            Log.d("--sendPDF--", "current: " + toJson);


            PDFConverter converter = PDFConverter.getInstance();
            converter.convert(OrderSummaryItemActivity.this, clsPrintPdf.getPdfString(),
                    new File(ClsGlobal.InvoiceFilePath
                            .concat(clsPrintPdf.getPdfFilePath())));

            converter.SetOnPDFCreatedListener((filePath) -> {

                if (loading.isShowing()) {
                    loading.dismiss();
                }

                ClsGlobal.viewPdf(clsPrintPdf.getPdfFilePath(),
                        "/" + ClsGlobal.AppFolderName + "/" + ClsGlobal.InvoiceFolderName + "/"
                        , OrderSummaryItemActivity.this);

                list_order.clear();
                loading = null;
            });

        });

        ll_send_sms_attactment.setOnClickListener(v -> {
//            SendSms("Yes","Sales");

            OpenMobileNumber_Dialog("SmsApiWith_Attactment");


        });

        ll_send_sms_no_attactment.setOnClickListener(v -> {
//            SendSms("No","Sales");
            OpenMobileNumber_Dialog("SmsApiNo_Attactment");


        });

//        btn_sms_invoice_attachment.setOnClickListener(v -> SendSms("Yes"));

        ll_edit.setOnClickListener(v -> {

            Intent intent = new Intent(getApplicationContext(), SalesActivity.class);
            intent.putExtra("saleMode", result.getSaleType());
            intent.putExtra("entryMode", "edit");
            intent.putExtra("editSource", editSource);
            intent.putExtra("_taxApple", _applyTax);

            intent.putExtra("editOrderID", String.valueOf(getOrderId));
            ClsGlobal.SetOrderEditMode(OrderSummaryItemActivity.this, "edit");
            intent.putExtra("editOrderNo", getOrderNo);

            startActivity(intent);

        });

        ll_email_invoice.setOnClickListener(v -> {
            String where = " AND [OrderNo] = ".concat("'").concat(String.valueOf(getOrderNo)).concat("'")
                    .concat("AND [OrderID] = ").concat(String.valueOf(getOrderId));
            @SuppressLint("WrongConstant") SQLiteDatabase db = openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            Observable.just(ClsInventoryOrderDetail
                    .getListForInvoicePDF(where, OrderSummaryItemActivity.this, db))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result_list -> {

                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Bill No: " + getOrderNo);
                        emailIntent.putExtra(Intent.EXTRA_TEXT,
                                ClsGlobal.getSmsString(result_list, result,
                                        getCustomerName.trim(),
                                        getMobileNo == null ? "" : getMobileNo.trim(),
                                        "InvoiceInfo",
                                        OrderSummaryItemActivity.this));

                        emailIntent.setData(Uri.parse("mailto:"));
                        startActivity(Intent.createChooser(emailIntent, "Send Bill Via Email"));
                    });
            db.close();
        });


        txt_valid_up_to.setText("DATE: " + ClsGlobal.getDDMYYYYFormat(_date));
        tv_Total.setText("TOTAL:\u20B9 " + ClsGlobal.round(getAmount, 2));
        txt_discount.setText("DISCOUNT:\u20B9 " + ClsGlobal.round(getDiscountIntent, 2));
        tv_Net_Amount.setText("NET AMOUNT:\u20B9 " + ClsGlobal.round(getAmount - getDiscountIntent, 2));
        txt_tax_amount.setText("TAX AMOUNT:\u20B9 " + ClsGlobal.round(getTaxAmount, 2));

        if (SaleReturnDiscount != 0.0) {
            txt_sale_return_discount.setVisibility(View.VISIBLE);
            txt_sale_return_discount.setText("SALE RETURN AMOUNT:\u20B9 " + ClsGlobal.round(SaleReturnDiscount, 2));
        }
        txt_grand_total.setText("\u20B9 " + ClsGlobal.round(getBillAmount, 2));
    }


    @Override
    protected void onResume() {
        super.onResume();
        ViewData();
    }

    @Override
    public void onBackPressed() {
    /*
     if(mode.equalsIgnoreCase("RecentOrderActivity")){

     }else {

     }
     */
//        super.onBackPressed();
//        finishAll();

    }

//    @Override
//    public void onBackPressed() {
//    }
//

    private void finishAll() {

        Log.d("--mode--", "mode: " + mode);
        Log.d("--mode--", "entryMode: " + entryMode);


        if (!mode.equalsIgnoreCase("RecentOrderActivity")) {

            Log.d("--mode--", "ModeIF");

            if (entryMode.equalsIgnoreCase("edit")) {

                Log.d("--mode--", "EntryModeIF");

                ClsGlobal.SetOrderEditMode(getApplicationContext(), "New");
                Intent intent = new Intent(OrderSummaryItemActivity.this,
                        RecentOrderActivity.class);

                intent.putExtra("Mounth", "");
                intent.putExtra("Year", "");
                intent.putExtra("title", "");
                intent.putExtra("editSource", editSource);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);

            } else {


                Log.d("--mode--", "EntryModeELSE");

                ClsGlobal.SetOrderEditMode(getApplicationContext(), "New");
                Intent intent = new Intent(OrderSummaryItemActivity.this,
                        SalesActivity.class);

                intent.putExtra("saleMode", saleMode);
                intent.putExtra("entryMode", entryMode);
                intent.putExtra("editSource", editSource);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            }
        } else {

            Log.d("--mode--", "ModeELSE");

            finish();
        }
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

            loading = ClsGlobal._prProgressDialog(OrderSummaryItemActivity.this
                    , "Please Wait...", false);
            loading.show();

        }


        @Override
        protected String doInBackground(Void... voids) {

            String _whereSend = " AND ORD.[OrderNo] = '" + getOrderNo + "' "
                    .concat(" AND ORD.[OrderID] = ").concat(String.valueOf(
                            getOrderId));
            @SuppressLint("WrongConstant")
            SQLiteDatabase db = openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            List<ClsInventoryOrderDetail> list = new ClsInventoryOrderDetail()
                    .getListForInvoicePDF(_whereSend,
                            OrderSummaryItemActivity.this, db);
            db.close();
            if (mode.equalsIgnoreCase("WhatsApp")) {

                return ClsGlobal.getWhatsAppString(list,
                        result
                        , new ClsPaymentMaster(),
                        OrderSummaryItemActivity.this,
                        "Yes");
            } else {

                return ClsGlobal.getSmsString(list, result,
                        getCustomerName.trim(),
                        mEditMobile.getText().toString(), "InvoiceInfo",
                        OrderSummaryItemActivity.this);
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
                    url = "https://api.whatsapp.com/send?phone="
                            + select_country.getText().toString()
                            + mEditMobile.getText().toString() + "&text="
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


//                    OrdersActivity.OpenSales = true;
//                    finish();


                } catch (Exception e) {
                    if (getWhatsAppDefaultApp != null
                            && !getWhatsAppDefaultApp.equalsIgnoreCase("")
                            && getWhatsAppDefaultApp.equalsIgnoreCase("WhatsApp")) {
                        Toast.makeText(OrderSummaryItemActivity.this, "WhatsApp Not Install!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(OrderSummaryItemActivity.this, "Business WhatsApp Not Install!", Toast.LENGTH_LONG).show();
                    }

                }

            } else {
//                OrdersActivity.OpenSales = true;
                ClsGlobal.sendSMS(OrderSummaryItemActivity.this,
                        mEditMobile.getText().toString().trim(), message);

//                SaveToDatabase();
//                finish();
            }

        }
    }



    private void CustomerDialog() {

        final EditText edit_search;
        dialogvendor = new Dialog(OrderSummaryItemActivity.this);
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

        customerAdapter = new CustomerAdapter(OrderSummaryItemActivity.this);
        lst.setAdapter(customerAdapter);


        customerAdapter.SetOnClickListener(clsCustomerMaster -> {

            Log.e("clsCustomerMaster", clsCustomerMaster.getmMobile_No());
            Log.e("clsCustomerMaster", clsCustomerMaster.getmName());

            mEditMobile.setText(clsCustomerMaster.getmMobile_No());

            dialogvendor.dismiss();

        });

        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Void>
                openCustomerDialog = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ClsGlobal._prProgressDialog(OrderSummaryItemActivity.this
                        , "Please Wait...", false);
                loading.show();
            }


            @Override
            protected Void doInBackground(Void... voids) {

                list_customer = new ArrayList<>();
                list_customer = new ClsCustomerMaster().ListCustomers("",
                        OrderSummaryItemActivity.this);

                Log.e("list_customer", String.valueOf(list_customer.size()));


                return null;
            }


            @Override
            protected void onPostExecute(Void list) {
                super.onPostExecute(list);

                if (loading.isShowing()) {
                    loading.dismiss();
                }

                try {

                    if (list_customer != null && list_customer.size() != 0) {

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
                                        OrderSummaryItemActivity.this);

                                customerAdapter.AddItems(list_customer);

                                lst.setAdapter(customerAdapter);

                            }
                        });


                    }


                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }

            }
        };
        openCustomerDialog.execute();

        dialogvendor.show();

    }


    @SuppressLint("CheckResult")
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        // Get mobile number.

        switch (reqCode) {
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    if (contactData != null) {
                        Cursor cur = getContentResolver().query(contactData,
                                null, null, null, null);
                        if (cur == null) return;
                        try {
                            if (cur.moveToFirst()) {
                                int phoneIndex = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                                int nameIndex = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

                                Observable.just(formatPhoneNo(
                                        cur.getString(phoneIndex).trim()))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(getResult -> {

                                            mEditMobile.setText(getResult.get(0));

                                            if (!getResult.get(1).equalsIgnoreCase("")) {
                                                select_country.setText(getResult.get(1));

                                            } else {
                                                select_country.setText("Select Country");
                                            }


                                        });

                            }
                        } finally {
                            cur.close();
                        }
                    }

                }
        }

    }

    private void SaveData_SharedPreferences(String str) {
        SharedPreferences.Editor preferencesEditor = mPreferencesDefault.edit();
        preferencesEditor.putString("WhatsApp Default App", str);
        preferencesEditor.apply();

    }

    @SuppressLint("CheckResult")
    private void OpenMobileNumber_Dialog(String mode) {

        AlertDialog alertDialog = new AlertDialog.Builder(OrderSummaryItemActivity.this).create(); //Read Update.
        @SuppressLint("InflateParams")
        View mCustomView = getLayoutInflater().inflate(R.layout.dialog_send_wtsapp, null);
        mEditMobile = mCustomView.findViewById(R.id.edt_phone_no);
        final ImageButton iv_customer = mCustomView.findViewById(R.id.iv_customer);
        final ImageButton iv_contacts = mCustomView.findViewById(R.id.iv_contacts);
        TextView txt_Bill_Sms = mCustomView.findViewById(R.id.Bill_Sms);
        TextView txt_Select_Default_app = mCustomView.findViewById(R.id.txt_Select_Default_app);
        RadioGroup rg = mCustomView.findViewById(R.id.rg);
        RadioButton rbWhatsApp = mCustomView.findViewById(R.id.rbWhatsApp);
        RadioButton rbBusiness_WhatsApp = mCustomView.findViewById(R.id.rbBusiness_WhatsApp);

        ImageButton iv_clear = mCustomView.findViewById(R.id.iv_clear);
        Button btn_send = mCustomView.findViewById(R.id.btn_send);
        alertDialog.setView(mCustomView);
        mEditMobile.setText(getMobileNo.replace(" ", ""));

        select_country = mCustomView.findViewById(R.id.select_country);

        select_country.setText(ClsGlobal
                .getCountryCodePreferences(OrderSummaryItemActivity.this));


        // Set title according mode.
        if (mode.equalsIgnoreCase("WhatsApp")) {
            rbWhatsApp.setVisibility(View.VISIBLE);
            rbBusiness_WhatsApp.setVisibility(View.VISIBLE);
            txt_Select_Default_app.setVisibility(View.VISIBLE);
            txt_Bill_Sms.setText("SEND BILL VIA WhatsApp");
        } else if (mode.equalsIgnoreCase("SMS")) {
            rbWhatsApp.setVisibility(View.GONE);
            rbBusiness_WhatsApp.setVisibility(View.GONE);
            txt_Select_Default_app.setVisibility(View.GONE);
            txt_Bill_Sms.setText("SEND BILL VIA SMS");
        } else if (mode.equalsIgnoreCase("SmsApiWith_Attactment")) {
            rbWhatsApp.setVisibility(View.GONE);
            rbBusiness_WhatsApp.setVisibility(View.GONE);
            txt_Select_Default_app.setVisibility(View.GONE);
            txt_Bill_Sms.setText("SEND BILL VIA SMS PACKAGE WITH PDF ATTACTMENT");
        } else if (mode.equalsIgnoreCase("SmsApiNo_Attactment")) {
            rbWhatsApp.setVisibility(View.GONE);
            rbBusiness_WhatsApp.setVisibility(View.GONE);
            txt_Select_Default_app.setVisibility(View.GONE);
            txt_Bill_Sms.setText("SEND BILL VIA SMS PACKAGE WITH NO PDF ATTACTMENT");
        }

        iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditMobile.setText("");
            }
        });

        iv_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerDialog();

            }
        });

        iv_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Contact selection.
                Intent intent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, PICK_CONTACT);

            }
        });

        select_country.setOnClickListener(v -> {
            SelectCountryDialog(OrderSummaryItemActivity.this
                    , select_country);

        });

        btn_send.setOnClickListener(v -> {

            ClsGlobal.hideKeyboard(OrderSummaryItemActivity.this);

            if (mEditMobile.getText() != null
                    && mEditMobile.getText().toString().length() > 0
                    && mEditMobile.getText().toString().length() <= 15) {

                // hide Keyboard

                // Send according mode.
                if (mode.equalsIgnoreCase("WhatsApp")) {

                    if (select_country.getText().toString().equalsIgnoreCase("")
                            || select_country.getText().toString().equalsIgnoreCase("Select Country")){
                        Toast.makeText(OrderSummaryItemActivity.this,
                                "Please Select Country.",Toast.LENGTH_LONG).show();
                        return;
                    }
                    new MessageAsyncTask("WhatsApp").execute();
                } else if (mode.equalsIgnoreCase("SMS")) {
                    new MessageAsyncTask("SMS").execute();
                } else if (mode.equalsIgnoreCase("SmsApiWith_Attactment")) {

                    @SuppressLint("WrongConstant") SQLiteDatabase db
                            = openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

                    Observable.just(ClsInventoryOrderMaster.getSmsLimit("AND [OrderNo] = '"
                                    + getOrderNo + "' "
                                    .concat(" AND [OrderID] = ")
                                    .concat(String.valueOf(getOrderId)),
                            OrderSummaryItemActivity.this, db))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(sms_limit -> {
                                Log.e("Check", "sms limit:- " + sms_limit);
                                if (sms_limit <= 4) {

                                    @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> sendBillSmsAsync =
                                            new AsyncTask<Void, Void, Void>() {

                                                ProgressDialog pd;

                                                @Override
                                                protected void onPreExecute() {
                                                    super.onPreExecute();

                                                    pd = ClsGlobal._prProgressDialog(OrderSummaryItemActivity.this,
                                                            "Loading...", false);
                                                    pd.show();
                                                }

                                                @Override
                                                protected Void doInBackground(Void... voids) {

                                                    ClsGlobal.SendSms("Yes", "Sales",
                                                            getOrderId, getOrderNo
                                                            , mEditMobile.getText().toString(),
                                                            getCustomerName, result, null,
                                                            String.valueOf(result.getTotalAmount()),
                                                            String.valueOf(sms_limit), "",
                                                            OrderSummaryItemActivity.this);
                                                    return null;
                                                }

                                                @Override
                                                protected void onPostExecute(Void aVoid) {
                                                    super.onPostExecute(aVoid);
                                                    if (pd.isShowing()) {
                                                        pd.cancel();
                                                    }
                                                    Toast.makeText(OrderSummaryItemActivity.this,
                                                            "Sms will be send shortly.", Toast.LENGTH_LONG).show();

                                                }
                                            };

                                    sendBillSmsAsync.execute();


                                } else {
//                                    Toast.makeText(OrderSummaryItemActivity.this,
//                                            "You can't send Sms more than 5 time's on same Bill.",
//                                            Toast.LENGTH_LONG).show();

                                    smsAlertBox("You can't send Sms more than 5 time's on same Bill.");

                                }

                                db.close();

                            });


                } else if (mode.equalsIgnoreCase("SmsApiNo_Attactment")) {
                    @SuppressLint("WrongConstant") SQLiteDatabase db
                            = openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

                    Observable.just(ClsInventoryOrderMaster.getSmsLimit(
                            "AND [OrderNo] = '"
                                    + getOrderNo + "' "
                                    .concat(" AND [OrderID] = ")
                                    .concat(String.valueOf(getOrderId)),
                            OrderSummaryItemActivity.this, db))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(sms_limit -> {
                                Log.e("Check", "sms limit:- " + sms_limit);
                                if (sms_limit <= 4) {
//
                                    @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> sendBillSmsAsync =
                                            new AsyncTask<Void, Void, Void>() {

                                                ProgressDialog pd;

                                                @Override
                                                protected void onPreExecute() {
                                                    super.onPreExecute();

                                                    pd = ClsGlobal._prProgressDialog(OrderSummaryItemActivity.this,
                                                            "Loading...", false);
                                                    pd.show();
                                                }

                                                @Override
                                                protected Void doInBackground(Void... voids) {

                                                    ClsGlobal.SendSms("No", "Sales", getOrderId, getOrderNo
                                                            , mEditMobile.getText().toString(), getCustomerName, result, null,
                                                            String.valueOf(result.getTotalAmount()),
                                                            String.valueOf(sms_limit),
                                                            "",
                                                            OrderSummaryItemActivity.this);
                                                    return null;
                                                }

                                                @Override
                                                protected void onPostExecute(Void aVoid) {
                                                    super.onPostExecute(aVoid);
                                                    if (pd.isShowing()) {
                                                        pd.cancel();
                                                    }

                                                    Toast.makeText(OrderSummaryItemActivity.this,
                                                            "Sms will be send shortly.", Toast.LENGTH_LONG).show();

                                                }
                                            };

                                    sendBillSmsAsync.execute();
                                } else {
//                                    Toast.makeText(OrderSummaryItemActivity.this,
//                                            "You can't send Sms more than 5 time's on same Bill.",
//                                            Toast.LENGTH_LONG).show();

                                    smsAlertBox("You can't send Sms more than 5 time's on same Bill.");


                                }


                                db.close();
                            });


                }


                alertDialog.dismiss();
                alertDialog.cancel();

            } else {
                Toast.makeText(OrderSummaryItemActivity.this,
                        "Please Enter Mobile Number!", Toast.LENGTH_LONG).show();
            }

        });


        getWhatsAppDefaultApp = mPreferencesDefault.getString("WhatsApp Default App", "");

        if (getWhatsAppDefaultApp != null &&
                !getWhatsAppDefaultApp.equalsIgnoreCase("")) {
            if (getWhatsAppDefaultApp.equalsIgnoreCase("WhatsApp")) {
                rbWhatsApp.setChecked(true);
            } else {
                rbBusiness_WhatsApp.setChecked(true);
            }

        }

        rg.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.rbWhatsApp) {
                SaveData_SharedPreferences(rbWhatsApp.getText().toString());
                getWhatsAppDefaultApp = rbWhatsApp.getText().toString();
            } else {
                SaveData_SharedPreferences(rbBusiness_WhatsApp.getText().toString());
                getWhatsAppDefaultApp = rbBusiness_WhatsApp.getText().toString();
            }

        });


        alertDialog.show();

    }

    void smsAlertBox(String _msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(OrderSummaryItemActivity.this,
                R.style.AppCompatAlertDialogStyle).create(); //Read Update.
        alertDialog.setContentView(R.layout.activity_dialog);
        alertDialog.setMessage(_msg);

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", (dialog, which) -> alertDialog.dismiss());

        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void getDefaultMessageFormat() {

        if (!objClsUserInfo.getLicenseType().equalsIgnoreCase("Free")) {
            String where = " AND [Type] = 'Sales'";
            SQLiteDatabase db =
                    openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_PRIVATE, null);

            ClsMessageFormat clsMessageFormat = ClsMessageFormat.getMessageFormatByDefault(db, where);
            if (clsMessageFormat.getMessage_format().length() > 0) {

                DefaultMessageFormat = clsMessageFormat.getMessage_format();

            } else {
                DefaultMessageFormat = ClsGlobal.getDefaultSalesSms(objClsUserInfo.getBusinessname(),
                        String.valueOf(getOrderNo),
                        String.valueOf(result.getTotalAmount())
                );
            }

            db.close();
        } else {
            DefaultMessageFormat = ClsGlobal.getDefaultSalesSms(objClsUserInfo.getBusinessname(),
                    String.valueOf(getOrderNo),
                    String.valueOf(result.getTotalAmount())
            );
        }


    }


}
