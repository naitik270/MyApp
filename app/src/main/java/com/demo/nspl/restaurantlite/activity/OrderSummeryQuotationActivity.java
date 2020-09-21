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

import com.demo.nspl.restaurantlite.Adapter.CustomerAdapter;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.ClsQuotationHtmlToPdf;
import com.demo.nspl.restaurantlite.Global.ClsQuotationPdf;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsCustomerMaster;
import com.demo.nspl.restaurantlite.classes.ClsQuotationMaster;
import com.demo.nspl.restaurantlite.classes.ClsQuotationOrderDetail;
import com.demo.nspl.restaurantlite.classes.ClsSendQuotationMessage;
import com.demo.nspl.restaurantlite.classes.ClsUserInfo;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.RemoveZeroFromDouble;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.SelectCountryDialog;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.formatPhoneNo;

public class OrderSummeryQuotationActivity extends AppCompatActivity {

    private static final String TAG = OrderSummeryQuotationActivity.class.getSimpleName();
    BottomSheetBehavior sheetBehavior;
    FloatingActionButton fab_up;
    String _doneBtn = "";
    String saleMode = "";
    String entryMode = "";
    String _quotationNo = "";
    String _taxApple = "", DefaultMessageFormat = "";
    TextView tv_customer_name,
            tv_mobile, txt_valid_up_to,
            txt_total, txt_order_no,
            txt_discount,
            txt_grand_total, txt_tax_amount;
    ClsUserInfo objClsUserInfo;
    String _quotationId = "";
    LinearLayout ll_done, ll_edit, ll_share, ll_send_sms, ll_generate_pdf,
            ll_wtsApp, ll_send_sms_attactment, ll_send_sms_no_attactment, ll_email_invoice;
    ClsQuotationMaster getCurrentObj;
    private StickyListHeadersListView lst;
    CustomerAdapter customerAdapter;
    RelativeLayout lyout_nodata;
    private List<ClsCustomerMaster> list_customer;
    public int PICK_CONTACT = 2;
    private List<ClsQuotationOrderDetail> lstClsQuotationOrderDetails = new ArrayList<>();
    private ProgressDialog loading;
    private SharedPreferences mPreferencesDefault;
    private Dialog dialogvendor;
    EditText mEditMobile;
    private static final String mPreferncesName = "MyPerfernces";
    TextView txt_title, select_country;
    String title = "";

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_summery_activity);

        LinearLayout bottom_sheet = findViewById(R.id.bottom_sheet);

        fab_up = findViewById(R.id.fab_up);
        fab_up.setColorFilter(Color.WHITE);
        tv_customer_name = findViewById(R.id.tv_customer_name);
        tv_mobile = findViewById(R.id.tv_mobile);
        txt_total = findViewById(R.id.txt_total);
        txt_discount = findViewById(R.id.txt_discount);
        txt_grand_total = findViewById(R.id.txt_grand_total);
        txt_valid_up_to = findViewById(R.id.txt_valid_up_to);
        txt_tax_amount = findViewById(R.id.txt_tax_amount);
        ll_done = findViewById(R.id.ll_done);
        ll_edit = findViewById(R.id.ll_edit);
        ll_share = findViewById(R.id.ll_share);
        txt_order_no = findViewById(R.id.txt_order_no);
        ll_send_sms = findViewById(R.id.ll_send_sms);
        ll_generate_pdf = findViewById(R.id.ll_generate_pdf);
        ll_wtsApp = findViewById(R.id.ll_wtsApp);
        ll_send_sms_attactment = findViewById(R.id.ll_send_sms_attactment);
        ll_send_sms_no_attactment = findViewById(R.id.ll_send_sms_no_attactment);
        ll_email_invoice = findViewById(R.id.ll_email_invoice);
        txt_title = findViewById(R.id.txt_title);

        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);

        getCurrentObj = (ClsQuotationMaster) getIntent().getSerializableExtra(
                "ClsQuotationMaster");
        objClsUserInfo = ClsGlobal.getUserInfo(OrderSummeryQuotationActivity.this);

        saleMode = getIntent().getStringExtra("saleMode");
        entryMode = getIntent().getStringExtra("entryMode");
        _doneBtn = getIntent().getStringExtra("_doneBtn"); // RecentQuotationActivity,
        _taxApple = getIntent().getStringExtra("_taxApple");
        _quotationId = getIntent().getStringExtra("_quotationId");
        _quotationNo = getIntent().getStringExtra("_quotationNo");


        txt_title.setText("SUMMARY");
        txt_order_no.setText("NO# " + _quotationNo);

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

        setValue();

        fillQuotationItemList();


        mPreferencesDefault = getSharedPreferences(mPreferncesName, MODE_PRIVATE);

        if (mPreferencesDefault.getString("WhatsApp Default App", null) == null) {
            SaveData_SharedPreferences("WhatsApp");
        }

        ll_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editQuotation();
            }
        });

        ll_send_sms_attactment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OpenMobileNumber_Dialog("SmsApiWith_Attactment");


            }
        });

        ll_send_sms_no_attactment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OpenMobileNumber_Dialog("SmsApiNo_Attactment");


            }
        });


        ll_done.setOnClickListener(v -> {

            if (_doneBtn.equalsIgnoreCase("direct")) {

                Intent intent = new Intent(OrderSummeryQuotationActivity.this,
                        SHAP_Lite_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            } else {
                finish();
            }


        });

        ll_email_invoice.setOnClickListener(v -> {

            String where = " AND [QuotationNo] = '" + _quotationNo + "' "
                    .concat(" AND [QuotationID] = ").concat(String.valueOf(_quotationId));

            @SuppressLint("WrongConstant") SQLiteDatabase db =
                    openOrCreateDatabase(ClsGlobal.Database_Name,
                            Context.MODE_APPEND, null);


            Observable.just(ClsQuotationOrderDetail.getMsgDescription(db, Integer.parseInt(_quotationId),
                    _quotationNo))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result_list -> {

                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Qut No: " + _quotationNo);
                        emailIntent.putExtra(Intent.EXTRA_TEXT,
                                sendSmsQuotation(lstClsQuotationOrderDetails,
                                        getCurrentObj, "RecentQuotation"));

                        emailIntent.setData(Uri.parse("mailto:"));
                        startActivity(Intent.createChooser(emailIntent,
                                "Send Bill Via Email"));

                    });
            db.close();
        });

        ll_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareQuotationPDF();
            }
        });

        ll_send_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenMobileNumber_Dialog("SMS");

//                sendQuotationSMS();
            }
        });

        ll_generate_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePDF();
            }
        });

        ll_wtsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenMobileNumber_Dialog("WhatsApp");
//                sendQuotationToWtsApp();
            }
        });


    }

    void generatePDF() {


//        fillQuotationItemList();

        loading = ClsGlobal._prProgressDialog(OrderSummeryQuotationActivity.this
                , "Generating PDF File...", false);

        loading.show();


        ClsCustomerMaster objClsCustomerMaster = new ClsCustomerMaster();

        if (getCurrentObj.getMobileNo() != null && !getCurrentObj.getMobileNo().isEmpty()) {

            String where = " AND [MOBILE_NO] = ".concat(getCurrentObj.getMobileNo());
            objClsCustomerMaster = new ClsCustomerMaster().getCustomerByMobileNo(where, OrderSummeryQuotationActivity.this);
        }

        Gson gsonOrder = new Gson();
        String jsonInStringOrder = gsonOrder.toJson(objClsCustomerMaster);
        Log.d("--sendPDF--", "objClsCustomerMaster: " + jsonInStringOrder);


        ClsQuotationPdf clsQuotationPdf = ClsQuotationHtmlToPdf.generatePDF
                (OrderSummeryQuotationActivity.this, getCurrentObj, objClsCustomerMaster,
                        lstClsQuotationOrderDetails, "Generate Pdf");


        Gson gson = new Gson();
        String jsonInString = gson.toJson(lstClsQuotationOrderDetails);
        Log.d("--current--", "Quotation: " + jsonInString);


        PDFConverter converter = PDFConverter.getInstance();

        converter.convert(OrderSummeryQuotationActivity.this, clsQuotationPdf.getPdfString(),
                new File(ClsGlobal.CREATE_PDF_QUOTATION_PATH));

        converter.SetOnPDFCreatedListener((FilePath) -> {

            if (loading.isShowing()) {
                loading.dismiss();
            }

            ClsGlobal.viewPdf(clsQuotationPdf.getPdfFilePath(),
                    "/" + ClsGlobal.AppFolderName + "/QuotationPDF/"
                    , OrderSummeryQuotationActivity.this);

            loading = null;
        });

    }


    void editQuotation() {

        Intent intent = new Intent(getApplicationContext(), SalesActivity.class);
        intent.putExtra("saleMode", saleMode);
        intent.putExtra("entryMode", "edit");
        intent.putExtra("_taxApple", _taxApple);
        intent.putExtra("editQuotationID", Integer.valueOf(_quotationId));

        ClsGlobal.SetOrderEditMode(OrderSummeryQuotationActivity.this, "edit");
        intent.putExtra("editOrderNo", _quotationNo);
        startActivity(intent);
    }

    void shareQuotationPDF() {

        loading = ClsGlobal._prProgressDialog(OrderSummeryQuotationActivity.this
                , "Generating PDF File...", false);

        loading.show();

        ClsCustomerMaster objClsCustomerMaster = new ClsCustomerMaster();

        if (getCurrentObj.getMobileNo() != null && !getCurrentObj.getMobileNo().isEmpty()) {

            String where = " AND [MOBILE_NO] = ".concat(getCurrentObj.getMobileNo());
            objClsCustomerMaster = new ClsCustomerMaster().getCustomerByMobileNo(where, OrderSummeryQuotationActivity.this);


        }


        ClsQuotationPdf clsQuotationPdf = ClsQuotationHtmlToPdf.generatePDF
                (OrderSummeryQuotationActivity.this, getCurrentObj, objClsCustomerMaster,
                        lstClsQuotationOrderDetails, "Send To WhatsApp");

        PDFConverter converter = PDFConverter.getInstance();
        converter.convert(OrderSummeryQuotationActivity.this, clsQuotationPdf.getPdfString(),
                new File(ClsGlobal.CREATE_PDF_QUOTATION_PATH));

        converter.SetOnPDFCreatedListener((FilePath) -> {

            if (loading.isShowing()) {
                loading.dismiss();
            }
            if (!getCurrentObj.getMobileNo().equalsIgnoreCase("")) {

                ClsGlobal.copyToClipboard(getCurrentObj.getMobileNo(),
                        OrderSummeryQuotationActivity.this);

                ClsGlobal.sendNotification("Send PDF File",
                        "Current Mobile Number: ".concat(getCurrentObj.getMobileNo())
                        , "Send PDF", OrderSummeryQuotationActivity.this);
            }

            ClsGlobal.SendPdfWhatsApp(OrderSummeryQuotationActivity.this,
                    FilePath);

            loading = null;
        });
    }

    @SuppressLint("CheckResult")
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        // Get mobile number.

        switch (reqCode) {
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
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

                                        Gson gson = new Gson();
                                        @SuppressLint("CheckResult") String jsonInString = gson.toJson(getResult);
                                        Log.e("--Sales--", "getResult: " + jsonInString);
                                        mEditMobile.setText(getResult.get(0));

                                        if (!getResult.get(1).equalsIgnoreCase("")) {
                                            select_country.setText(getResult.get(1));

                                        } else {
                                            select_country.setText("Select Country");
                                        }


                                    });
//
                        }
                    } finally {
                        cur.close();
                    }
                }
        }

    }

    void fillQuotationItemList() {

        String _whereSend = " AND [QuotationNo] = '" + _quotationNo + "' "
                .concat(" AND [QuotationID] = ").concat(String.valueOf(_quotationId));

        Log.e("--fill--", "Where: " + _whereSend);

        @SuppressLint("WrongConstant")
        SQLiteDatabase db = openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

        lstClsQuotationOrderDetails = new ClsQuotationOrderDetail().getMsgDescription
                (db, Integer.parseInt(_quotationId), _quotationNo);

        db.close();
        Log.e("--QuotationDetailList--",
                "Where: " + lstClsQuotationOrderDetails.size());
    }


    String _where1 = "";
    String getWhatsAppDefaultApp = "";

    private void LoadAsyncTask(String mode, String phoneNumber,
                               ClsQuotationMaster clsQuotationMaster,
                               String message) {

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, ClsSendQuotationMessage> task =
                new AsyncTask<Void, Void, ClsSendQuotationMessage>() {


                    private ProgressDialog loading;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        loading = ClsGlobal._prProgressDialog(OrderSummeryQuotationActivity.this
                                , "Please Wait...", false);
                        loading.show();
                    }

                    @Override
                    protected ClsSendQuotationMessage doInBackground(Void... voids) {

                        ClsSendQuotationMessage _objQuotationMessage = new ClsSendQuotationMessage();
//                        _where1 = " AND [QuotationNo] = '" + clsQuotationMaster.getQuotationNo() + "' "
//                                .concat(" AND [QuotationID] = ").concat(String.valueOf(clsQuotationMaster.getQuotationID()));
//
                        @SuppressLint("WrongConstant")
                        SQLiteDatabase db = openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

                        _objQuotationMessage.setLst(new ClsQuotationOrderDetail()
                                .getMsgDescription(db, clsQuotationMaster.getQuotationID(),
                                        clsQuotationMaster.getQuotationNo()));
                        db.close();

                        return _objQuotationMessage;
                    }


                    @Override
                    protected void onPostExecute(ClsSendQuotationMessage clsSendQuotationMessage) {
                        super.onPostExecute(clsSendQuotationMessage);

                        if (loading.isShowing()) {
                            loading.dismiss();
                        }

                        lstClsQuotationOrderDetails = clsSendQuotationMessage.getLst();

                        if (mode.equalsIgnoreCase("WhatsApp")) {
                            ClsGlobal.sendQuotationToWtsApp(OrderSummeryQuotationActivity.this,
                                    phoneNumber,
                                    lstClsQuotationOrderDetails,
                                    clsQuotationMaster,
                                    clsQuotationMaster.getQuotationNo(),
                                    "No", clsQuotationMaster.getValidUptoDate()
                                    , getWhatsAppDefaultApp);
                        } else {
                            ClsGlobal.sendSMS(OrderSummeryQuotationActivity.this,
                                    phoneNumber, message);
                        }


                    }
                };

        task.execute();

    }

    private String sendSmsQuotation(List<ClsQuotationOrderDetail> lstClsQuotationOrderDetails,
                                    ClsQuotationMaster _obj, String _mode) {

        List<String> items = new ArrayList<>();
        int srNO = 0;

        for (ClsQuotationOrderDetail s : lstClsQuotationOrderDetails) {
            srNO++;

            String str = "";
            str = str.concat(String.valueOf(srNO).concat(". ")).concat(s.getItem()).concat("\n")
                    .concat("Rate: " + ClsGlobal.round(s.getSaleRate(), 2)).concat("\n")
                    .concat("Qty: " + RemoveZeroFromDouble(String.valueOf(ClsGlobal.round(
                            s.getQuantity(), 2))))
                    .concat("\n")
                    .concat("Amount: ").concat(RemoveZeroFromDouble(String.valueOf(ClsGlobal.round(
                            s.getAmount(), 2))))
                    .concat("\n");//"PUL

            if (s.getItemComment() != null && !s.getItemComment().equalsIgnoreCase("")) {
                str = str.concat("Details: ").concat(s.getItemComment().equalsIgnoreCase("")
                        ? "" : s.getItemComment()).concat("\n");
            }

            Log.e("str324", str);

            items.add(str);//"PUL
        }

        items.add("Amt:" + _obj.getTotalAmount());
        if (_obj.getDiscountAmount() != 0.0) {
            items.add("Discount:" + ClsGlobal.round(_obj.getDiscountAmount(), 2));
        }

        if (_obj.getApplyTax().equalsIgnoreCase("YES")) {
            if (_obj.getTotalTaxAmount() != 0.0) {
                items.add("Tax Amt:".concat(String.valueOf(ClsGlobal.round(
                        Double.valueOf(RemoveZeroFromDouble(String.valueOf(
                                _obj.getTotalTaxAmount()))), 2))));
            }
        }


        items.add("Bill Amt:".concat(String.valueOf(ClsGlobal.round(
                _obj.getGrandTotal(), 2))));
        String smsBody = ClsGlobal.getSmsHeaderString(items, OrderSummeryQuotationActivity.this,
                _obj.getCustomerName(),
                String.valueOf(_obj.getQuotationNo()),
                _mode, _obj.getValidUptoDate(),
                _obj.getMobileNo(), _obj.getApplyTax());

        return smsBody;
    }


    @SuppressLint("SetTextI18n")
    void setValue() {
        @SuppressLint("WrongConstant")
        SQLiteDatabase db = openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);


        ClsQuotationMaster _obj = ClsQuotationMaster.getFillQuotationOn(_quotationNo,
                Integer.parseInt(String.valueOf(_quotationId)), OrderSummeryQuotationActivity.this, db);


        db.close();
        tv_customer_name.setText(_obj.getCustomerName().toUpperCase());
        tv_mobile.setText(_obj.getMobileNo());

//        txt_valid_up_to.setText("VALID UP TO: " + ClsGlobal.getEntryDateFormat(_obj.getValidUptoDate()));
        txt_valid_up_to.setText("VALID UP TO: " + ClsGlobal.getDDMYYYYFormat(_obj.getValidUptoDate()));
        txt_total.setText("NET AMOUNT: \u20B9 " + ClsGlobal.round(_obj.getTotalAmount(), 2));
        txt_discount.setText("DISCOUNT: \u20B9 " + ClsGlobal.round(_obj.getDiscountAmount(), 2));
        txt_tax_amount.setText("TAX AMOUNT: \u20B9 " + ClsGlobal.round(_obj.getTotalTaxAmount(), 2));

        txt_grand_total.setText("\u20B9 " + ClsGlobal.round(_obj.getGrandTotal(), 2));


        Gson gson = new Gson();
        String jsonInString = gson.toJson(_obj);
        Log.d("--setValue--", "_obj: " + jsonInString);

    }

    private void SaveData_SharedPreferences(String str) {
        SharedPreferences.Editor preferencesEditor = mPreferencesDefault.edit();
        preferencesEditor.putString("WhatsApp Default App", str);
        preferencesEditor.apply();

    }

    private void CustomerDialog() {

        final EditText edit_search;
        dialogvendor = new Dialog(OrderSummeryQuotationActivity.this);
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

        customerAdapter = new CustomerAdapter(OrderSummeryQuotationActivity.this);
        lst.setAdapter(customerAdapter);


        customerAdapter.SetOnClickListener(clsCustomerMaster -> {

            mEditMobile.setText(clsCustomerMaster.getmMobile_No());

            dialogvendor.dismiss();

        });

        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Void>
                openCustomerDialog = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ClsGlobal._prProgressDialog(OrderSummeryQuotationActivity.this
                        , "Please Wait...", false);
                loading.show();
            }


            @Override
            protected Void doInBackground(Void... voids) {

                list_customer = new ArrayList<>();
                list_customer = new ClsCustomerMaster().ListCustomers("",
                        OrderSummeryQuotationActivity.this);
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
                                        OrderSummeryQuotationActivity.this);

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
    private void OpenMobileNumber_Dialog(String mode) {

        AlertDialog alertDialog = new AlertDialog.Builder(OrderSummeryQuotationActivity.this).create(); //Read Update.
        @SuppressLint("InflateParams")
        View mCustomView = getLayoutInflater().inflate(R.layout.dialog_send_wtsapp, null);
        alertDialog.setView(mCustomView);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setAttributes(lp);


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
        mEditMobile.setText(getCurrentObj.getMobileNo().replace(" ", ""));
        select_country = mCustomView.findViewById(R.id.select_country);

        select_country.setText(ClsGlobal
                .getCountryCodePreferences(OrderSummeryQuotationActivity.this));

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
            txt_Bill_Sms.setText("SEND BILL VIA SMS PACKAGE WITH PDF ATTACHMENT");
        } else if (mode.equalsIgnoreCase("SmsApiNo_Attactment")) {
            rbWhatsApp.setVisibility(View.GONE);
            rbBusiness_WhatsApp.setVisibility(View.GONE);
            txt_Select_Default_app.setVisibility(View.GONE);
            txt_Bill_Sms.setText("SEND BILL VIA SMS PACKAGE WITH NO PDF ATTACHMENT");
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

        iv_contacts.setOnClickListener(v -> {

            // Contact selection.
            Intent intent = new Intent(Intent.ACTION_PICK,
                    ContactsContract.Contacts.CONTENT_URI);
            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(intent, PICK_CONTACT);

        });


        select_country.setOnClickListener(v -> {
            SelectCountryDialog(OrderSummeryQuotationActivity.this
                    , select_country);
        });


        btn_send.setOnClickListener(v -> {

            // hide Keyboard
            ClsGlobal.hideKeyboard(OrderSummeryQuotationActivity.this);

            if (mEditMobile.getText() != null
                    && mEditMobile.getText().toString().length() > 0
                    && mEditMobile.getText().toString().length() <= 15) {

                // Send according mode.
                if (mode.equalsIgnoreCase("WhatsApp")) {
//                    new MessageAsyncTask("WhatsApp").execute();

                    if (mEditMobile.getText() == null ||
                            mEditMobile.getText().toString().trim().isEmpty()) {
                        Toast.makeText(OrderSummeryQuotationActivity.this,
                                "Number is required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (select_country.getText().toString()
                            .equalsIgnoreCase("Select Country")) {
                        Toast.makeText(OrderSummeryQuotationActivity.this,
                                "Select is Country", Toast.LENGTH_SHORT).show();
                        return;
                    }

//                    ClsGlobal.sendQuotationToWtsApp(OrderSummeryQuotationActivity.this,
//                            mEditMobile.getText().toString(),
//                            lstClsQuotationOrderDetails,
//                            getCurrentObj, "No", getCurrentObj.getValidUptoDate(),
//                            getCurrentObj.getQuotationNo()
//                            , getWhatsAppDefaultApp);

                    ClsGlobal.sendQuotationToWtsApp(
                            OrderSummeryQuotationActivity.this,
                            select_country.getText().toString()
                                    + mEditMobile.getText().toString(),
                            lstClsQuotationOrderDetails,
                            getCurrentObj, "No", getCurrentObj.getValidUptoDate(),
                            getCurrentObj.getQuotationNo()
                            , getWhatsAppDefaultApp);


                } else if (mode.equalsIgnoreCase("SMS")) {
//                    new OrderSummaryItemActivity.MessageAsyncTask("SMS").execute();

                    if (mEditMobile.getText() == null || mEditMobile.getText().toString().trim().isEmpty()) {
                        Toast.makeText(OrderSummeryQuotationActivity.this, "Number is required", Toast.LENGTH_SHORT).show();
                        return;
                    } else {

                        Observable.just(sendSmsQuotation(lstClsQuotationOrderDetails,
                                getCurrentObj, "RecentQuotation"))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(message -> {
                                    LoadAsyncTask("SMS", mEditMobile.getText().toString().trim()
                                            , getCurrentObj, message);
                                });
                    }

                } else if (mode.equalsIgnoreCase("SmsApiWith_Attactment")) {

                    @SuppressLint("WrongConstant") SQLiteDatabase db
                            = openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//                    _quotationId
//                            _quotationNo
                    Observable.just(ClsQuotationMaster
                            .getSmsLimit_Quotation(" AND [QuotationID] = " +
                                    _quotationId + " AND [QuotationNo] = '"
                                    + _quotationNo + "'", OrderSummeryQuotationActivity.this, db))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(sms_limit -> {

                                Log.e("Check", "sms limit:- " + sms_limit);


//                                Best Dream11 Telegram channels are:
//
//                                      Cricgram
//                                      Cricinformer
//                                      Fantasy Cricket Guru
//                                      FantasyArena.In
//                                      Sports Fantasy Guruji


                                if (sms_limit <= 4) {

                                    @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> sendBillSmsAsync =
                                            new AsyncTask<Void, Void, Void>() {

                                                ProgressDialog pd;

                                                @Override
                                                protected void onPreExecute() {
                                                    super.onPreExecute();
                                                    pd = ClsGlobal._prProgressDialog(OrderSummeryQuotationActivity.this,
                                                            "Loading...", false);
                                                    pd.show();
                                                }

                                                @Override
                                                protected Void doInBackground(Void... voids) {
                                                    ClsGlobal.SendSms("Yes", "Quotation",
                                                            Integer.parseInt(_quotationId),
                                                            _quotationNo
                                                            , getCurrentObj.getMobileNo(), getCurrentObj.getCustomerName(),
                                                            null, getCurrentObj,
                                                            String.valueOf(getCurrentObj.getTotalAmount())
                                                            , String.valueOf(sms_limit)
                                                            , "", OrderSummeryQuotationActivity.this);

                                                    return null;
                                                }

                                                @Override
                                                protected void onPostExecute(Void aVoid) {
                                                    super.onPostExecute(aVoid);
                                                    if (pd.isShowing()) {
                                                        pd.cancel();
                                                    }
                                                    Toast.makeText(OrderSummeryQuotationActivity.this,
                                                            "Sms will be send shortly.", Toast.LENGTH_LONG).show();

                                                }
                                            };

                                    sendBillSmsAsync.execute();


                                } else {
//                                    Toast.makeText(OrderSummeryQuotationActivity.this,
//                                            "You can't send Sms more than 5 time's on same Bill.",
//                                            Toast.LENGTH_LONG).show();

                                    smsAlertBox("You can't send Sms more than 5 time's on same Bill.");
                                }

                                db.close();

                            });


                } else if (mode.equalsIgnoreCase("SmsApiNo_Attactment")) {

                    @SuppressLint("WrongConstant") SQLiteDatabase db
                            = openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
                    Observable.just(ClsQuotationMaster
                            .getSmsLimit_Quotation(" AND [QuotationID] = " +
                                    _quotationId + " AND [QuotationNo] = '"
                                    + _quotationNo + "'", OrderSummeryQuotationActivity.this, db))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(sms_limit -> {

                                Log.e("Check", "sms limit:- " + sms_limit);
                                if (sms_limit <= 4) {
                                    @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void>
                                            sendBillSmsAsync =
                                            new AsyncTask<Void, Void, Void>() {

                                                ProgressDialog pd;

                                                @Override
                                                protected void onPreExecute() {
                                                    super.onPreExecute();

                                                    pd = ClsGlobal._prProgressDialog(OrderSummeryQuotationActivity.this,
                                                            "Loading...", false);
                                                    pd.show();
                                                }

                                                @Override
                                                protected Void doInBackground(Void... voids) {
                                                    ClsGlobal.SendSms("No", "Quotation", Integer.parseInt(_quotationId),
                                                            _quotationNo
                                                            , getCurrentObj.getMobileNo(), getCurrentObj.getCustomerName(),
                                                            null, getCurrentObj,
                                                            String.valueOf(getCurrentObj.getTotalAmount()),
                                                            String.valueOf(sms_limit), "", OrderSummeryQuotationActivity.this);

                                                    return null;
                                                }

                                                @Override
                                                protected void onPostExecute(Void aVoid) {
                                                    super.onPostExecute(aVoid);
                                                    if (pd.isShowing()) {
                                                        pd.cancel();
                                                    }
                                                    Toast.makeText(OrderSummeryQuotationActivity.this,
                                                            "Sms will be send shortly.", Toast.LENGTH_LONG).show();

                                                }
                                            };

                                    sendBillSmsAsync.execute();

                                } else {
//                                    Toast.makeText(OrderSummeryQuotationActivity.this,
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
                Toast.makeText(OrderSummeryQuotationActivity.this,
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
        AlertDialog alertDialog = new AlertDialog.Builder(OrderSummeryQuotationActivity.this,
                R.style.AppCompatAlertDialogStyle).create(); //Read Update.
        alertDialog.setContentView(R.layout.activity_dialog);
        alertDialog.setMessage(_msg);

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", (dialog, which) -> alertDialog.dismiss());

        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
    }


}
