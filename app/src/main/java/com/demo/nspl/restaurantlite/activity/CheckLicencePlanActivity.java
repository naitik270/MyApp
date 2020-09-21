package com.demo.nspl.restaurantlite.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.nspl.restaurantlite.Global.ApiClient;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsCustomerFreeLicenseUpdation;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsReferralApplyParams;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsResendOtpParms;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsVerifiedOtpParms;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceCustomerFreeLicenseUpdation;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceReferralApplyOTP;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceResendOTP;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceVerifiedOTP;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckLicencePlanActivity extends AppCompatActivity {

    TextView btn_referral_applied;
    TextView btn_start;
    private String _mobileNo = "", _customerId = "";
    String _resCustomerStatus = "";
    String _resLicenseType = "";
    private String reg_mode = "";


    WebView webView;
    String freeUrl = "";
    String sendSMSNow = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_licence_plan);
        main();
    }

    private void main() {

        btn_referral_applied = findViewById(R.id.btn_referral_applied);
        btn_start = findViewById(R.id.btn_start);
        webView = findViewById(R.id.webview);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        freeUrl = getIntent().getStringExtra("freeUrl");
        _mobileNo = getIntent().getStringExtra("_mobileNo");
        _customerId = getIntent().getStringExtra("_customerId");
        reg_mode = getIntent().getStringExtra("reg_mode");
        _resCustomerStatus = getIntent().getStringExtra("_resCustomerStatus");
        _resLicenseType = getIntent().getStringExtra("_resLicenseType");
        sendSMSNow = getIntent().getStringExtra("sendSMSNow");


        Log.d("--PackageType--", "_mobileNo: " + _mobileNo);
        Log.d("--PackageType--", "_customerId: " + _customerId);
        Log.d("--PackageType--", "reg_mode: " + reg_mode);
        Log.d("--PackageType--", "_resCustomerStatus: " + _resCustomerStatus);
        Log.d("--PackageType--", "_resLicenseType: " + _resLicenseType);

        webView.loadUrl(freeUrl);

        btn_referral_applied.setOnClickListener(v -> {
            refferalApply();
        });

        btn_start.setOnClickListener(v -> {

//            freePackageDirectSelectDialog();


            CustomerFreeLicenseUpdationAPI("", "No");

        });

    }

    Dialog dialog_reference;
    LinearLayout ll_otp;
    EditText edit_reference, edt_otp;

    void refferalApply() {
        dialog_reference = new Dialog(CheckLicencePlanActivity.this);
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
        btn_cancel.setOnClickListener(v -> {
            resetCounter();
            dialog_reference.cancel();
            dialog_reference.dismiss();
        });

        txt_Display_mobile_no = dialog_reference.findViewById(R.id.txt_Display_mobile_no);
        ll_otp = dialog_reference.findViewById(R.id.ll_otp);
        txt_link_Resend = dialog_reference.findViewById(R.id.txt_link_Resend);
        textView = dialog_reference.findViewById(R.id.textView);

        edit_reference = dialog_reference.findViewById(R.id.edit_reference);
        edt_otp = dialog_reference.findViewById(R.id.edt_otp);

        txt_link_Resend.setOnClickListener(view -> {
            if (increment == false) {
                resendOtpAPI();
//                            Toast.makeText(ActivityPaymentProcess.this, "Reset counter", Toast.LENGTH_SHORT).show();
            } else {
//                            Toast.makeText(ActivityPaymentProcess.this, "ELse counter", Toast.LENGTH_SHORT).show();
            }
        });

        btn_Send_Otp.setOnClickListener(v -> {
            boolean validation = ValidationUserCode();

            if (validation == true) {

                ReferralApplyAPI();


            }
        });

        Button btn_verify = dialog_reference.findViewById(R.id.btn_verify);

        btn_verify.setOnClickListener(v -> {


            boolean validation = OtpValidation();
            if (validation == true) {
                verifiedOtpAPI();
            }
        });

        dialog_reference.show();
    }

    boolean _referralApplied = false;

    void verifiedOtpAPI() {
        ClsVerifiedOtpParms objClsVerifiedOtpParms = new ClsVerifiedOtpParms();
        objClsVerifiedOtpParms.setUserCode(edit_reference.getText().toString());
        objClsVerifiedOtpParms.setMobileNumber(txt_Display_mobile_no.getTag().toString());

        String _OtpConcat = edt_otp.getText().toString();

        objClsVerifiedOtpParms.setVerificationCode(_OtpConcat);
        objClsVerifiedOtpParms.setUserType("Referral");
        objClsVerifiedOtpParms.setOtpSendMode("Referral Apply");

        InterfaceVerifiedOTP interfaceVerifiedOTP = ApiClient.getRetrofitInstance().create(InterfaceVerifiedOTP.class);
        Log.e("--URL--", "interfaceDesignation: " + interfaceVerifiedOTP.toString());
        Call<ClsVerifiedOtpParms> call = interfaceVerifiedOTP.postVerificationOTP(objClsVerifiedOtpParms);
        Log.e("--URL--", "************************  before call : " + call.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(CheckLicencePlanActivity.this, "Working...", true);
        pd.show();

        call.enqueue(new Callback<ClsVerifiedOtpParms>() {
            @Override
            public void onResponse(@NonNull Call<ClsVerifiedOtpParms> call,
                                   @NonNull Response<ClsVerifiedOtpParms> response) {
                pd.dismiss();
                _referralApplied = false;

                if (response.body() != null) {
                    String _response = response.body().getSuccess();
                    Log.d("_response", "onResponse: " + _response);

                    switch (_response) {
                        case "1":
                            Toast.makeText(CheckLicencePlanActivity.this,
                                    "Free Registration is successful", Toast.LENGTH_SHORT).show();

                            _referralApplied = true;

                            CustomerFreeLicenseUpdationAPI(edit_reference.getText().toString()
                                    , "YES"
                            );


                            btn_referral_applied.setText("Referral Applied");
                            btn_referral_applied.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
                            btn_referral_applied.setTextColor(Color.parseColor("#40932d"));
                            btn_referral_applied.setClickable(false);

                            dialog_reference.cancel();
                            dialog_reference.dismiss();
                            break;
                        case "2":
                            Toast.makeText(CheckLicencePlanActivity.this, "OTP not exist", Toast.LENGTH_SHORT).show();
                            break;
                        case "3":
                            Toast.makeText(CheckLicencePlanActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                            break;
                        case "0":
                            Toast.makeText(CheckLicencePlanActivity.this, "Failed OTP", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(CheckLicencePlanActivity.this, "Failed to send OTP", Toast.LENGTH_SHORT).show();
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


    private Boolean OtpValidation() {
        if (edt_otp.getText() == null || edt_otp.getText().toString().length() > 4
                || edt_otp.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "4 characters are required",
                    Toast.LENGTH_SHORT).show();
            edt_otp.requestFocus();
            return false;
        }

        if (!ClsGlobal.CheckInternetConnection(CheckLicencePlanActivity.this)) {
            Toast.makeText(CheckLicencePlanActivity.this,
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
                ClsGlobal._prProgressDialog(CheckLicencePlanActivity.this,
                        "Working...", true);
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
                            Toast.makeText(CheckLicencePlanActivity.this, "Mobile no is verified", Toast.LENGTH_SHORT).show();
                            ll_otp.setVisibility(View.VISIBLE);
                            break;
                        case "2":
                            Toast.makeText(CheckLicencePlanActivity.this, "Mobile no is not verified", Toast.LENGTH_SHORT).show();
                            break;
                        case "3":
                            Toast.makeText(CheckLicencePlanActivity.this, "Invalid referral code", Toast.LENGTH_SHORT).show();
                            break;
                        case "4":
                            Toast.makeText(CheckLicencePlanActivity.this, "Referral code not found", Toast.LENGTH_SHORT).show();
                            break;
                        case "0":
                            Toast.makeText(CheckLicencePlanActivity.this, "fail", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(CheckLicencePlanActivity.this, "Error", Toast.LENGTH_SHORT).show();
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

        if (!ClsGlobal.CheckInternetConnection(CheckLicencePlanActivity.this)) {
            Toast.makeText(CheckLicencePlanActivity.this,
                    "You are not connected to Internet", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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
                ClsGlobal._prProgressDialog(CheckLicencePlanActivity.this, "Working...", true);
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
                            Toast.makeText(CheckLicencePlanActivity.this, "OTP is resend", Toast.LENGTH_SHORT).show();
                            resetCounter();
                            setCounter();
                            break;
                        case "0":
                            Toast.makeText(CheckLicencePlanActivity.this, "Failed OTP", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(CheckLicencePlanActivity.this, "Failed to send OTP", Toast.LENGTH_SHORT).show();
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

    TextView txt_Display_mobile_no, txt_link_Resend, textView;
    public int counter = 30;
    boolean increment = true;
    private static final int PERMISSION_REQUEST_ID = 100;
    CountDownTimer timer;

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

    private void freePackageDirectSelectDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CheckLicencePlanActivity.this);
        alertDialog.setTitle("YOUR FREE ACCOUNT IS START...");
        alertDialog.setMessage("Are you sure you want to start?");
        alertDialog.setIcon(R.drawable.ic_free);
        alertDialog.setPositiveButton("YES", (dialog, which) -> {

            dialog.dismiss();
            dialog.cancel();

            if (!ClsGlobal.CheckInternetConnection(CheckLicencePlanActivity.this)) {
                Toast.makeText(CheckLicencePlanActivity.this,
                        "You are not connected to Internet",
                        Toast.LENGTH_SHORT).show();
            } else {
                CustomerFreeLicenseUpdationAPI("", "No");
            }

        });
        alertDialog.setNegativeButton("NO", (dialog, which) -> {
            dialog.dismiss();
            dialog.cancel();
        });
        // Showing Alert Message
        alertDialog.show();

    }


    private void freePackageSelectDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CheckLicencePlanActivity.this);
        alertDialog.setTitle("YOUR FREE ACCOUNT IS START...");
        alertDialog.setMessage("Are you sure you want to start?");
        alertDialog.setIcon(R.drawable.ic_free);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("YES", (dialog, which) -> {

            dialog.dismiss();
            dialog.cancel();

            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(intent);
            finish();


        });
        alertDialog.show();

    }


    private void CustomerFreeLicenseUpdationAPI(String _referralCode, String _referralApplied) {

        ClsCustomerFreeLicenseUpdation obj = new ClsCustomerFreeLicenseUpdation();
        obj.setCustomerCode(_customerId);
        obj.setIMEINo(ClsGlobal.getIMEIno(CheckLicencePlanActivity.this));
        obj.setMode(reg_mode);
//        obj.setReferalCode("EJK001");
        obj.setReferalCode(_referralCode);
        obj.setReferalApplied(_referralApplied);


        Gson gson = new Gson();
        String jsonInString = gson.toJson(obj);
        Log.d("--GSON--", "FreeAPI: " + jsonInString);


        InterfaceCustomerFreeLicenseUpdation interfaceFreeLicenseUpdation =
                ApiClient.getRetrofitInstance().create(InterfaceCustomerFreeLicenseUpdation.class);
        Log.e("--URL--", "interfaceDesignation: " + interfaceFreeLicenseUpdation.toString());
        Call<ClsCustomerFreeLicenseUpdation> call = interfaceFreeLicenseUpdation.postCall(obj);
        Log.e("--URL--", "URL: " + call.request().url());


        ProgressDialog pd =
                ClsGlobal._prProgressDialog(CheckLicencePlanActivity.this,
                        "Working...", true);
        pd.show();


        call.enqueue(new Callback<ClsCustomerFreeLicenseUpdation>() {
            @Override
            public void onResponse(@NonNull Call<ClsCustomerFreeLicenseUpdation> call,
                                   @NonNull Response<ClsCustomerFreeLicenseUpdation> response) {
                pd.dismiss();

                if (response.body() != null) {
                    String _response = response.body().getSuccess();
                    Log.d("--PackageType--", "onResponse: " + _response);

                    switch (_response) {
                        case "1":

                            if (_referralApplied != null && _referralCode.equalsIgnoreCase("Yes")) {
                                freePackageSelectDialog();

                            } else {


                                if (sendSMSNow != null && !sendSMSNow.isEmpty() && sendSMSNow.equalsIgnoreCase("Yes")) {

                                    ClsGlobal.autoLogout(getApplicationContext());

                                    Intent i = new Intent(getApplicationContext(), LogInActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);

                                } else {


                                    Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }


                            Toast.makeText(CheckLicencePlanActivity.this,
                                    "Successfully Verified.", Toast.LENGTH_SHORT).show();

                            break;
                        case "0":

                            if (_referralApplied != null && _referralCode.equalsIgnoreCase("Yes")) {
                                freePackageSelectDialog();

                            } else {

                                if (sendSMSNow != null && !sendSMSNow.isEmpty() && sendSMSNow.equalsIgnoreCase("Yes")) {

                                    ClsGlobal.autoLogout(getApplicationContext());

                                    Intent i = new Intent(getApplicationContext(), LogInActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);

                                } else {

                                    Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }

                            Toast.makeText(CheckLicencePlanActivity.this,
                                    "Successfully", Toast.LENGTH_SHORT).show();

//                            Toast.makeText(CheckLicencePlanActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(CheckLicencePlanActivity.this, "Failed default", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server error...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ClsCustomerFreeLicenseUpdation> call, @NonNull Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong...API is Not Working!", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
