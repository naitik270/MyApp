package com.demo.nspl.restaurantlite.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ApiClient;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.Receives.MySmsBroadcastReceiver;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsCashCollectionParams;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsPackageInsertList;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsPackageInsertParams;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsPackagePaymentParams;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsReferralApplyParams;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsResendOtpParms;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsVerifiedOtpParms;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceCashCollection;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfacePackageInsert;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfacePackagePayment;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceReferralApplyOTP;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceResendOTP;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceVerifiedOTP;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPaymentProcess extends AppCompatActivity implements PaymentResultListener {

    private TextView txt_refer;
    private TextView txt_package, txt_valid_days, txt_price;
    private Button btn_make_payment;
    RadioButton rb_online, rb_cash;
    String _mobileNo = "";
    String _emailAddress = "";

    EditText edit_reference;
    private static EditText editTextone, editTexttwo, editTextthree, editTextfour;


    EditText edt_otp;

    TextView txt_Display_mobile_no, txt_link_Resend, textView;
    public int counter = 30;
    boolean increment = true;
    private static final int PERMISSION_REQUEST_ID = 100;
    MySmsBroadcastReceiver mSmsBroadcastReceiver;
    private IntentFilter intentFilter;
    LinearLayout ll_otp;
    private final String BROADCAST_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    Dialog dialog_reference;
    CountDownTimer timer;
    private String TransactionRefNumber = "", _customerId = "";
    int valid_days = 0, packageID = 0;
    boolean _referralApplied = false;
    String title = "", reg_mode = "";
    Double price = 0.0;
    TextView txt_applyTax, txt_tax1, txt_tax2, txt_tax3, txt_tax_value1,
            txt_tax_value2, txt_tax_value3, txt_totaltaxamount, txt_totalpackageamount;
    String paymentDesc = "";
    String IsTaxesApplicable = "";
    Double IGSTValue = 0.0, CGSTValue = 0.0, SGSTValue = 0.0;
    Double IGSTTaxAmount = 0.0, CGSTTaxAmount = 0.0, SGSTTaxAmount = 0.0,
            TotalTaxAmount = 0.0, TotalPackageAmount = 0.0;
    private String PaymentGatwayOrderID = "";
    LinearLayout ll_tax_layer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_process);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "ActivityPaymentProcess"));
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mSmsBroadcastReceiver = new MySmsBroadcastReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION);

        requestRuntimePermissions(Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS);

        main();
    }


    private void main() {

        txt_refer = findViewById(R.id.txt_refer);

        txt_totaltaxamount = findViewById(R.id.txt_totaltaxamount);
        txt_totalpackageamount = findViewById(R.id.txt_totalpackageamount);

        ll_tax_layer = findViewById(R.id.ll_tax_layer);

        txt_applyTax = findViewById(R.id.txt_applyTax);


        txt_tax1 = findViewById(R.id.txt_tax1);
        txt_tax2 = findViewById(R.id.txt_tax2);
        txt_tax3 = findViewById(R.id.txt_tax3);
        txt_tax_value1 = findViewById(R.id.txt_tax_value1);
        txt_tax_value2 = findViewById(R.id.txt_tax_value2);
        txt_tax_value3 = findViewById(R.id.txt_tax_value3);

        txt_package = findViewById(R.id.txt_package);
        txt_valid_days = findViewById(R.id.txt_valid_days);
        txt_price = findViewById(R.id.txt_price);
        btn_make_payment = findViewById(R.id.btn_make_payment);
        rb_online = findViewById(R.id.rb_online);
        rb_cash = findViewById(R.id.rb_cash);

        Intent intent = getIntent();

        reg_mode = intent.getStringExtra("reg_mode");
        IsTaxesApplicable = intent.getStringExtra("IsTaxesApplicable");
        IGSTValue = intent.getDoubleExtra("IGSTValue", 0.0);
        SGSTValue = intent.getDoubleExtra("SGSTValue", 0.0);
        CGSTValue = intent.getDoubleExtra("CGSTValue", 0.0);
        IGSTTaxAmount = intent.getDoubleExtra("IGSTTaxAmount", 0.0);
        CGSTTaxAmount = intent.getDoubleExtra("CGSTTaxAmount", 0.0);
        SGSTTaxAmount = intent.getDoubleExtra("SGSTTaxAmount", 0.0);
        TotalTaxAmount = intent.getDoubleExtra("TotalTaxAmount", 0.0);
        TotalPackageAmount = intent.getDoubleExtra("TotalPackageAmount", 0.0);
        title = intent.getStringExtra("title");
        _customerId = intent.getStringExtra("_customerId");
        valid_days = intent.getIntExtra("valid_days", 0);
        packageID = intent.getIntExtra("packageID", 0);
        price = intent.getDoubleExtra("price", 0.0);

        txt_applyTax.setText("Tax Applicable: " + IsTaxesApplicable.toLowerCase());
        txt_tax1.setText("SGST (" + SGSTValue + "%)");
        txt_tax_value1.setText("\u20B9 " + SGSTTaxAmount);
        txt_tax2.setText("CGST (" + CGSTValue + "%)");
        txt_tax_value2.setText("\u20B9 " + CGSTTaxAmount);
        txt_tax3.setText("IGST (" + IGSTValue + "%)");
        txt_tax_value3.setText("\u20B9 " + IGSTTaxAmount);

        txt_totaltaxamount.setText("Total Tax: \u20B9 " + TotalTaxAmount);
        txt_totalpackageamount.setText("\u20B9 " + TotalPackageAmount);

        txt_package.setText(title.toUpperCase());
        txt_valid_days.setText("Valid Days: " + valid_days);
        txt_price.setText("Price: \u20B9 " + price);

        if (IsTaxesApplicable.equalsIgnoreCase("NO")) {
            ll_tax_layer.setVisibility(View.GONE);
        } else {
            ll_tax_layer.setVisibility(View.VISIBLE);
        }

        txt_refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_reference = new Dialog(ActivityPaymentProcess.this);
                dialog_reference.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog_reference.setContentView(R.layout.dialog_reference);
                dialog_reference.setCancelable(false);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog_reference.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                dialog_reference.getWindow().setAttributes(lp);

                Button btn_Send_Otp = dialog_reference.findViewById(R.id.btn_Send_Otp);
                Button btn_cancel = dialog_reference.findViewById(R.id.btn_cancel);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resetCounter();
                        dialog_reference.cancel();
                        dialog_reference.dismiss();
                    }
                });

                txt_Display_mobile_no = dialog_reference.findViewById(R.id.txt_Display_mobile_no);
                ll_otp = dialog_reference.findViewById(R.id.ll_otp);
                txt_link_Resend = dialog_reference.findViewById(R.id.txt_link_Resend);
                textView = dialog_reference.findViewById(R.id.textView);

                edit_reference = dialog_reference.findViewById(R.id.edit_reference);
                edt_otp = dialog_reference.findViewById(R.id.edt_otp);

                editTextone = dialog_reference.findViewById(R.id.editTextone);
                editTexttwo = dialog_reference.findViewById(R.id.editTexttwo);
                editTextthree = dialog_reference.findViewById(R.id.editTextthree);
                editTextfour = dialog_reference.findViewById(R.id.editTextfour);

                txt_link_Resend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (increment == false) {
                            resendOtpAPI();
//                            Toast.makeText(ActivityPaymentProcess.this, "Reset counter", Toast.LENGTH_SHORT).show();
                        } else {
//                            Toast.makeText(ActivityPaymentProcess.this, "ELse counter", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                editTextone.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        editTexttwo.requestFocus();
                    }
                });

                editTexttwo.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        editTextthree.requestFocus();
                    }
                });

                editTextthree.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        editTextfour.requestFocus();
                    }
                });

                editTextfour.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        editTextfour.requestFocus();
                    }
                });

                btn_Send_Otp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean validation = ValidationUserCode();

                        if (validation == true) {
                            ReferralApplyAPI();

                        }
                    }
                });

                Button btn_verify = dialog_reference.findViewById(R.id.btn_verify);
                btn_verify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        boolean validation = OtpValidation();
                        if (validation == true) {
                            verifiedOtpAPI();
                        }
                    }
                });

                dialog_reference.show();
            }
        });

        btn_make_payment.setOnClickListener(v -> {
           /* if (ClsGlobal.CheckInternetConnection(ActivityPaymentProcess.this)) {
                packageInsertAPI();
                //CashCollectionVerificationAPI();
            } else {
                Toast.makeText(ActivityPaymentProcess.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }*/

            if (isNetworkConnected()) {
                packageInsertAPI();
                //CashCollectionVerificationAPI();
            } else {
                Toast.makeText(ActivityPaymentProcess.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

    }


    void packageInsertAPI() {
        ClsPackageInsertParams objClsPackageInsertParams = new ClsPackageInsertParams();

        Log.e("--URL--", "packageInsertAPI " + _customerId);

        objClsPackageInsertParams.setCustomerCode(_customerId);
        objClsPackageInsertParams.setIMEINumber(ClsGlobal.getIMEIno(ActivityPaymentProcess.this));
        objClsPackageInsertParams.setPackageId(String.valueOf(packageID));
        objClsPackageInsertParams.setDiscount(String.valueOf(0));
        if (_referralApplied) {
            objClsPackageInsertParams.setReferalApplied("yes");
            objClsPackageInsertParams.setReferalCode(edit_reference.getText().toString());
        } else {

            objClsPackageInsertParams.setReferalApplied("no");
            objClsPackageInsertParams.setReferalCode("");
        }
        objClsPackageInsertParams.setMode(reg_mode);


        Gson gson = new Gson();
        String jsonInString = gson.toJson(objClsPackageInsertParams);
        Log.d("--pkgDetails--", "Details: " + jsonInString);


        InterfacePackageInsert interfacePackageInsert = ApiClient.getRetrofitInstance().create(InterfacePackageInsert.class);
        Log.e("--URL--", "interfaceDesignation: " + interfacePackageInsert.toString());
        Call<ClsPackageInsertParams> call = interfacePackageInsert.postPackageInsert(objClsPackageInsertParams);
        Log.e("--URL--", "************************  before call : " + call.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(ActivityPaymentProcess.this, "Working...", true);
        pd.show();

        call.enqueue(new Callback<ClsPackageInsertParams>() {
            @Override
            public void onResponse(Call<ClsPackageInsertParams> call, Response<ClsPackageInsertParams> response) {
                pd.dismiss();
                if (response.body() != null) {
                    String _response = response.body().getSuccess();
                    List<ClsPackageInsertList> lstClsPackageInsertLists = response.body().getData();
                    if (_response.equals("1")) {
                        if (lstClsPackageInsertLists != null && lstClsPackageInsertLists.size() != 0) {
                            _customerId = lstClsPackageInsertLists.get(0).getCustomerCode();
                            TransactionRefNumber = lstClsPackageInsertLists.get(0).getTransactionRefNumber();
                            PaymentGatwayOrderID = lstClsPackageInsertLists.get(0).getPaymentGatwayOrderID();
                            _mobileNo = lstClsPackageInsertLists.get(0).getMobileNumber();
                            _emailAddress = lstClsPackageInsertLists.get(0).getEmailAddress();


                            paymentDesc = "RazorPayOrderID: ".concat(PaymentGatwayOrderID)
                                    .concat(",CustomerCode: ").concat(_customerId)
                                    .concat(",Transaction Reference No. ").concat(TransactionRefNumber)
                                    .concat(",Email: ").concat(_emailAddress)
                                    .concat(",Mobile: ").concat(_mobileNo)
                                    .concat(",PackageID: ").concat(String.valueOf(packageID));


                            if (rb_online.isChecked()) {
                                //online payment getways
                                if (price > 0) {


                                   /* Intent intent = new Intent(getApplicationContext(), Activity_MakePayment.class);
                                    intent.putExtra("title", title);
                                    intent.putExtra("valid_days", valid_days);
                                    intent.putExtra("price", Double.valueOf(TotalPackageAmount));
                                    intent.putExtra("_customerId", _customerId);
                                    intent.putExtra("packageID", packageID);
                                    intent.putExtra("_mobileNo", _mobileNo);
                                    intent.putExtra("_emailAddress", _emailAddress);
                                    intent.putExtra("PaymentGatwayOrderID", PaymentGatwayOrderID);
                                    intent.putExtra("TransactionRefNumber", TransactionRefNumber);
                                    startActivity(intent);
*/
                                    if (isNetworkConnected()) {
                                        startPayment();
                                    } else {
                                        Toast.makeText(ActivityPaymentProcess.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                    }


                                } else {
                                    AlertDialog alertDialog = new AlertDialog.Builder(ActivityPaymentProcess.this,
                                            R.style.AppCompatAlertDialogStyle).create(); //Read Update.
                                    alertDialog.setContentView(R.layout.activity_dialog);
                                    alertDialog.setTitle("Confirmation");
                                    alertDialog.setMessage("You have selected free package, sure to continue?");
                                    alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            ClsPackagePaymentParams objClsPackagePaymentParams = new ClsPackagePaymentParams();
                                            objClsPackagePaymentParams.setCustomerCode(_customerId);
                                            objClsPackagePaymentParams.setTransactionReferenceNumber(TransactionRefNumber);
                                            objClsPackagePaymentParams.setPaymentStatus("success");
                                            objClsPackagePaymentParams.setPaymentMode("Free");
                                            objClsPackagePaymentParams.setPaymentGateway("");
                                            objClsPackagePaymentParams.setPaymentReferenceNumber("");
                                            objClsPackagePaymentParams.setSatusCode(0);
                                            objClsPackagePaymentParams.setTransactionMessage("Free Payment ONLINE");

                                            Gson gson = new Gson();
                                            String jsonInString = gson.toJson(objClsPackagePaymentParams);
                                            Log.d("Result", "AlertDialog- " + jsonInString);

                                            packagePaymentAPI(objClsPackagePaymentParams);

                                        }
                                    });
                                    alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    alertDialog.setCancelable(false);
                                    alertDialog.show();
                                }
                            } else {
                                //cash mode
                                if (price > 0) {
//cash collect API
                                    CashCollectionVerificationAPI();

                                } else {
                                    AlertDialog alertDialog = new AlertDialog.Builder(ActivityPaymentProcess.this,
                                            R.style.AppCompatAlertDialogStyle).create(); //Read Update.
                                    alertDialog.setContentView(R.layout.activity_dialog);
                                    alertDialog.setTitle("Confirmation");
                                    alertDialog.setMessage("You have selected free package, sure to continue?");
                                    alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            ClsPackagePaymentParams objClsPackagePaymentParams = new ClsPackagePaymentParams();
                                            objClsPackagePaymentParams.setCustomerCode(_customerId);
                                            objClsPackagePaymentParams.setTransactionReferenceNumber(TransactionRefNumber);
                                            objClsPackagePaymentParams.setPaymentStatus("success");
                                            objClsPackagePaymentParams.setPaymentMode("Free");
                                            objClsPackagePaymentParams.setPaymentGateway("");
                                            objClsPackagePaymentParams.setPaymentReferenceNumber("");
                                            objClsPackagePaymentParams.setSatusCode(0);
                                            objClsPackagePaymentParams.setTransactionMessage("Free Payment CASH");

                                            Gson gson = new Gson();
                                            String jsonInString = gson.toJson(objClsPackagePaymentParams);
                                            Log.d("Result", "AlertDialog- " + jsonInString);

                                            packagePaymentAPI(objClsPackagePaymentParams);

                                        }
                                    });
                                    alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    alertDialog.setCancelable(false);
                                    alertDialog.show();
                                }
                            }

                        }
                        Toast.makeText(ActivityPaymentProcess.this, "please process payment to continue", Toast.LENGTH_SHORT).show();
                    } else if (_response.equals("2")) {
                        Toast.makeText(ActivityPaymentProcess.this, "Package not found", Toast.LENGTH_SHORT).show();
                    } else if (_response.equals("3")) {
                        Toast.makeText(ActivityPaymentProcess.this, "Customer not found", Toast.LENGTH_SHORT).show();
                    } else if (_response.equals("0")) {
                        Toast.makeText(ActivityPaymentProcess.this, "fail", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ActivityPaymentProcess.this, "No response", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server error...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsPackageInsertParams> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong,please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    void packagePaymentAPI(ClsPackagePaymentParams objClsReferralApplyParams) {

        InterfacePackagePayment interfacePackagePayment = ApiClient.getRetrofitInstance().create(InterfacePackagePayment.class);
        Log.e("--URL--", "interfaceDesignation: " + interfacePackagePayment.toString());
        Call<ClsPackagePaymentParams> call = interfacePackagePayment.postPackagePayment(objClsReferralApplyParams);
        Log.e("--URL--", "************************  before call : " + call.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(ActivityPaymentProcess.this, "Working...", true);
        pd.show();

        call.enqueue(new Callback<ClsPackagePaymentParams>() {
            @Override
            public void onResponse(Call<ClsPackagePaymentParams> call, Response<ClsPackagePaymentParams> response) {
                pd.dismiss();
                if (response.body() != null) {
                    String _response = response.body().getSuccess();
                    Log.d("_response", "onResponse: " + _response);
                    switch (_response) {
                        case "1":
                            Toast.makeText(ActivityPaymentProcess.this, "Package activated successfully", Toast.LENGTH_SHORT).show();
                            ClsGlobal.autoLogout(getApplicationContext());
                            Intent i = new Intent(ActivityPaymentProcess.this, LogInActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            break;
                        case "2":
                            Toast.makeText(ActivityPaymentProcess.this, "Mobile no is not verified", Toast.LENGTH_SHORT).show();
                            break;
                        case "3":
                            Toast.makeText(ActivityPaymentProcess.this, "Invalid referral code", Toast.LENGTH_SHORT).show();
                            break;
                        case "4":
                            Toast.makeText(ActivityPaymentProcess.this, "Referral code not found", Toast.LENGTH_SHORT).show();
                            break;
                        case "0":
                            Toast.makeText(ActivityPaymentProcess.this, "fail", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(ActivityPaymentProcess.this, "Error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    Toast.makeText(ActivityPaymentProcess.this, "Server error...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsPackagePaymentParams> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(ActivityPaymentProcess.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private Boolean OtpValidation() {

/*
        if (editTextone.getText() == null || editTextone.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter OTP", Toast.LENGTH_SHORT).show();
            editTextone.requestFocus();
            return false;
        }
        if (editTexttwo.getText() == null || editTexttwo.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter OTP", Toast.LENGTH_SHORT).show();
            editTextone.requestFocus();
            return false;
        }
        if (editTextthree.getText() == null || editTextthree.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter OTP", Toast.LENGTH_SHORT).show();
            editTextone.requestFocus();
            return false;
        }
        if (editTextfour.getText() == null || editTextfour.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter OTP", Toast.LENGTH_SHORT).show();
            editTextone.requestFocus();
            return false;
        }
*/
        if (edt_otp.getText() == null || edt_otp.getText().toString().length() > 4
                || edt_otp.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "4 characters are required",
                    Toast.LENGTH_SHORT).show();
            edt_otp.requestFocus();
            return false;
        }


        if (!ClsGlobal.CheckInternetConnection(ActivityPaymentProcess.this)) {
            Toast.makeText(ActivityPaymentProcess.this,
                    "You are not connected to Internet", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    void ReferralApplyAPI() {
        ClsReferralApplyParams objClsReferralApplyParams = new ClsReferralApplyParams();
        objClsReferralApplyParams.setUserCode(edit_reference.getText().toString());
        objClsReferralApplyParams.setCustomerCode(_customerId);
        objClsReferralApplyParams.setMacAddress(ClsGlobal.getIMEIno(this));
        objClsReferralApplyParams.setAppType(ClsGlobal.AppName);

        Gson gson = new Gson();
        String jsonInString = gson.toJson(objClsReferralApplyParams);
        Log.d("Result", "ReferralApplyAPI- " + jsonInString);

        InterfaceReferralApplyOTP interfaceReferralApplyOTP = ApiClient.getRetrofitInstance().create(InterfaceReferralApplyOTP.class);
        Log.e("--URL--", "interfaceDesignation: " + interfaceReferralApplyOTP.toString());
        Call<ClsReferralApplyParams> call = interfaceReferralApplyOTP.postReferApply(objClsReferralApplyParams);
        Log.e("--URL--", "************************  before call : " + call.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(ActivityPaymentProcess.this, "Working...", true);
        pd.show();

        call.enqueue(new Callback<ClsReferralApplyParams>() {
            @Override
            public void onResponse(Call<ClsReferralApplyParams> call, Response<ClsReferralApplyParams> response) {
                pd.dismiss();
                if (response.body() != null) {
                    String _response = response.body().getSuccess();
                    Log.d("_response", "onResponse: " + _response);

                    _mobileNo = response.body().getMobileNumber();
                    txt_Display_mobile_no.setText("OTP SEND ON ".concat(_mobileNo));
                    txt_Display_mobile_no.setTag(_mobileNo);

                    switch (_response) {
                        case "1":
                            setCounter();
                            Toast.makeText(ActivityPaymentProcess.this, "Mobile no is verified", Toast.LENGTH_SHORT).show();
                            ll_otp.setVisibility(View.VISIBLE);
                            break;
                        case "2":
                            Toast.makeText(ActivityPaymentProcess.this, "Mobile no is not verified", Toast.LENGTH_SHORT).show();
                            break;
                        case "3":
                            Toast.makeText(ActivityPaymentProcess.this, "Invalid referral code", Toast.LENGTH_SHORT).show();
                            break;
                        case "4":
                            Toast.makeText(ActivityPaymentProcess.this, "Referral code not found", Toast.LENGTH_SHORT).show();
                            break;
                        case "0":
                            Toast.makeText(ActivityPaymentProcess.this, "fail", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(ActivityPaymentProcess.this, "Error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server error...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsReferralApplyParams> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean ValidationUserCode() {

        if (edit_reference.getText() == null || edit_reference.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter referral code", Toast.LENGTH_SHORT).show();
            edit_reference.requestFocus();
            return false;
        }

        if (!ClsGlobal.CheckInternetConnection(ActivityPaymentProcess.this)) {
            Toast.makeText(ActivityPaymentProcess.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    void verifiedOtpAPI() {
        ClsVerifiedOtpParms objClsVerifiedOtpParms = new ClsVerifiedOtpParms();
        objClsVerifiedOtpParms.setUserCode(edit_reference.getText().toString());
        objClsVerifiedOtpParms.setMobileNumber(txt_Display_mobile_no.getTag().toString());

//        String _OtpConcat = editTextone.getText().toString()
//                .concat(editTexttwo.getText().toString())
//                .concat(editTextthree.getText().toString())
//                .concat(editTextfour.getText().toString());
//
        String _OtpConcat = edt_otp.getText().toString();

        objClsVerifiedOtpParms.setVerificationCode(_OtpConcat);
        objClsVerifiedOtpParms.setUserType("Referral");
        objClsVerifiedOtpParms.setOtpSendMode("Referral Apply");

        InterfaceVerifiedOTP interfaceVerifiedOTP = ApiClient.getRetrofitInstance().create(InterfaceVerifiedOTP.class);
        Log.e("--URL--", "interfaceDesignation: " + interfaceVerifiedOTP.toString());
        Call<ClsVerifiedOtpParms> call = interfaceVerifiedOTP.postVerificationOTP(objClsVerifiedOtpParms);
        Log.e("--URL--", "************************  before call : " + call.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(ActivityPaymentProcess.this, "Working...", true);
        pd.show();

        call.enqueue(new Callback<ClsVerifiedOtpParms>() {
            @Override
            public void onResponse(Call<ClsVerifiedOtpParms> call, Response<ClsVerifiedOtpParms> response) {
                pd.dismiss();
                _referralApplied = false;
                if (response.body() != null) {
                    String _response = response.body().getSuccess();
                    Log.d("_response", "onResponse: " + _response);

                    switch (_response) {
                        case "1":
                            Toast.makeText(ActivityPaymentProcess.this, "OTP is verified", Toast.LENGTH_SHORT).show();

                            _referralApplied = true;

                            txt_refer.setText("Referral Applied");
                            txt_refer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
                            txt_refer.setTextColor(Color.parseColor("#40932d"));
                            txt_refer.setClickable(false);

                            dialog_reference.cancel();
                            dialog_reference.dismiss();
                            rb_cash.setVisibility(View.VISIBLE);

                            break;
                        case "2":
                            Toast.makeText(ActivityPaymentProcess.this, "OTP not exist", Toast.LENGTH_SHORT).show();
                            EmptyEditText();
                            break;
                        case "3":
                            Toast.makeText(ActivityPaymentProcess.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                            EmptyEditText();
                            break;
                        case "0":
                            Toast.makeText(ActivityPaymentProcess.this, "Failed OTP", Toast.LENGTH_SHORT).show();
                            EmptyEditText();
                            break;
                        default:
                            Toast.makeText(ActivityPaymentProcess.this, "Failed to send OTP", Toast.LENGTH_SHORT).show();
                            EmptyEditText();
                            break;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server error...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsVerifiedOtpParms> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong,please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void EmptyEditText() {

        editTextone.setText("");
        editTexttwo.setText("");
        editTextthree.setText("");
        editTextfour.setText("");
        editTextone.requestFocus();
    }

    void resendOtpAPI() {
        ClsResendOtpParms objClsResendOtpParms = new ClsResendOtpParms();
        objClsResendOtpParms.setUserCode(edit_reference.getText().toString());
        objClsResendOtpParms.setMobileNumber(txt_Display_mobile_no.getTag().toString());
        objClsResendOtpParms.setUserType("Referral");
        objClsResendOtpParms.setOtpSendMode("Referral Apply");

        InterfaceResendOTP interfaceResendOTP = ApiClient.getRetrofitInstance().create(InterfaceResendOTP.class);
        Log.e("--URL--", "interfaceDesignation " + interfaceResendOTP.toString());
        Call<ClsResendOtpParms> call = interfaceResendOTP.postResendOTP(objClsResendOtpParms);
        Log.e("--URL--", "************************" + call.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(ActivityPaymentProcess.this, "Working...", true);
        pd.show();

        call.enqueue(new Callback<ClsResendOtpParms>() {
            @Override
            public void onResponse(Call<ClsResendOtpParms> call, Response<ClsResendOtpParms> response) {
                pd.dismiss();
                if (response.body() != null) {

                    String _response = response.body().getSuccess();
                    Log.d("_response", "onResponse: " + _response);

                    switch (_response) {
                        case "1":
                            Toast.makeText(ActivityPaymentProcess.this, "OTP is resend", Toast.LENGTH_SHORT).show();
                            resetCounter();
                            setCounter();
                            break;
                        case "0":
                            Toast.makeText(ActivityPaymentProcess.this, "Failed OTP", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(ActivityPaymentProcess.this, "Failed to send OTP", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server error...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsResendOtpParms> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong...API is Not Working!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void resetCounter() {
        if (increment == true && counter != 30) {
            timer.cancel();
        }
        counter = 30;
        increment = true;
        txt_link_Resend.setEnabled(false);
        txt_link_Resend.setTextColor(Color.parseColor("#4200a8e1"));
        textView.setVisibility(View.VISIBLE);
    }

    void setCounter() {

        timer = new CountDownTimer(31000, 1000) {
            public void onTick(long millisUntilFinished) {
                if (increment) {
                    textView.setText("(" + String.valueOf(counter) + ")");
                    counter--;
                    if (counter == 0) {
                        increment = false;
                        enableResendButton();
                    }
                }
            }

            public void onFinish() {
                txt_link_Resend.setVisibility(View.VISIBLE);


            }
        }.start();
    }

    void enableResendButton() {
        txt_link_Resend.setTextColor(Color.parseColor("#00a8e1"));
        txt_link_Resend.setEnabled(true);
        textView.setText("(0)");
    }

    private void requestRuntimePermissions(String... permissions) {
        for (String perm : permissions) {

            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{perm}, PERMISSION_REQUEST_ID);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_ID) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Log.e("permission", "Permission not granted");

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mSmsBroadcastReceiver, intentFilter);
        Log.e("MainActivity", "Registered receiver");

    }

    @Override
    protected void onPause() {
        super.onPause();
        // unregisterReceiver(mSmsBroadcastReceiver);
        Log.e("MainActivity", "Unregistered receiver");
    }

    void CashCollectionVerificationAPI() {

        InterfaceCashCollection interfacePackage =
                ApiClient.getRetrofitInstance().create(InterfaceCashCollection.class);

        Call<ClsCashCollectionParams> objClsCashCollectionParamsCall =
                interfacePackage.value(edit_reference.getText().toString(), packageID, price);

        Log.e("--URL--", "CashCollectionVerificationAPI: " + objClsCashCollectionParamsCall.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(ActivityPaymentProcess.this, "Loading...", true);
        pd.show();

        objClsCashCollectionParamsCall.enqueue(new Callback<ClsCashCollectionParams>() {
            @Override
            public void onResponse(Call<ClsCashCollectionParams> call, Response<ClsCashCollectionParams> response) {
                pd.dismiss();
                if (response.body() != null) {
                    String success = response.body().getSuccess();
                    Log.e("--success--", "HHTRequestReport:-- " + success);

                    if (success.equals("1")) {
                        String _referralMobile = response.body().getMobileNumber();
                        Intent intent = new Intent(ActivityPaymentProcess.this, ActivityCashCollectionOtp.class);
                        intent.putExtra("_customerId", _customerId);
                        intent.putExtra("_referralMobile", _referralMobile);
                        intent.putExtra("TransactionRefNumber", TransactionRefNumber);
                        intent.putExtra("_empCode", edit_reference.getText().toString());
                        startActivity(intent);

                        //move to verification code
                        Toast.makeText(getApplicationContext(), "sufficient balance and Message will be send to registered mobile number.", Toast.LENGTH_LONG).show();
                    } else if (success.equals("2")) {
                        Toast.makeText(getApplicationContext(), "User Mobile Verification is pending.", Toast.LENGTH_LONG).show();
                    } else if (success.equals("3")) {
                        Toast.makeText(getApplicationContext(), "insufficient balance.", Toast.LENGTH_LONG).show();
                    } else if (success.equals("4")) {
                        Toast.makeText(getApplicationContext(), "User not found.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server error...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsCashCollectionParams> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong,please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

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


    public void startPayment() {

        final AppCompatActivity activity = this;
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", paymentDesc);
//            options.put("image", "https://cdn.razorpay.com/logos/B5S0yCDDpK9MwB_medium.png");
            options.put("currency", "INR");
            options.put("order_id", PaymentGatwayOrderID);

// Amount Converted in Paisa.....
            int _packageAmount = (int) (price * 100);
            options.put("amount", _packageAmount);

            JSONObject preFill = new JSONObject();
            preFill.put("email", _emailAddress);
            preFill.put("contact", _mobileNo);
            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }


    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();

            ClsPackagePaymentParams objClsPackagePaymentParams = new ClsPackagePaymentParams();
            objClsPackagePaymentParams.setCustomerCode(_customerId);
            objClsPackagePaymentParams.setTransactionReferenceNumber(TransactionRefNumber);
            objClsPackagePaymentParams.setPaymentStatus("success");
            objClsPackagePaymentParams.setPaymentMode("Online");
            objClsPackagePaymentParams.setPaymentGateway("razorpay");
            objClsPackagePaymentParams.setPaymentReferenceNumber(razorpayPaymentID);
            objClsPackagePaymentParams.setSatusCode(0);
            objClsPackagePaymentParams.setTransactionMessage("Payment Successful");

            packagePaymentAPI(objClsPackagePaymentParams);

        } catch (Exception e) {
            Log.e("--payment--", "Exception onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
            ClsPackagePaymentParams objClsPackagePaymentParams = new ClsPackagePaymentParams();
            objClsPackagePaymentParams.setCustomerCode(_customerId);
            objClsPackagePaymentParams.setTransactionReferenceNumber(TransactionRefNumber);
            objClsPackagePaymentParams.setPaymentStatus("failed");
            objClsPackagePaymentParams.setPaymentMode("Online");
            objClsPackagePaymentParams.setPaymentGateway("razorpay");
            objClsPackagePaymentParams.setPaymentReferenceNumber("");
            objClsPackagePaymentParams.setSatusCode(code);
            objClsPackagePaymentParams.setTransactionMessage(response);

            packagePaymentAPI(objClsPackagePaymentParams);

        } catch (Exception e) {
            Log.e("--payment--", "Exception onPaymentError", e);
        }
    }
}
