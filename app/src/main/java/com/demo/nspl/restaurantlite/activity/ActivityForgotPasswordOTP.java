package com.demo.nspl.restaurantlite.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ApiClient;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.Receives.MySmsBroadcastReceiver;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsResendOtpParms;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsVerifiedOtpParms;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceResendOTP;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceVerifiedOTP;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityForgotPasswordOTP extends AppCompatActivity implements TextWatcher {


    private Toolbar mToolbar;

    private Button Btn_verify;
    private TextView txt_link_Resend, txt_Display_mobile_no;
    private String get_Mobile_No, _customerId;
    private int Count = 1;

    private static final int PERMISSION_REQUEST_ID = 100;
    MySmsBroadcastReceiver mSmsBroadcastReceiver;
    private final String BROADCAST_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private IntentFilter intentFilter;
    private static EditText editTextone, editTexttwo, editTextthree, editTextfour;

    TextView textView;
    public int counter = 30;
    boolean increment = true;
    CountDownTimer timer;


    EditText edt_otp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_otp);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "ActivityForgotPasswordOTP"));
        }
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mSmsBroadcastReceiver = new MySmsBroadcastReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION);
        requestRuntimePermissions(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS);

        txt_link_Resend = findViewById(R.id.txt_link_Resend);
        textView = findViewById(R.id.textView);
        Btn_verify = findViewById(R.id.Btn_verify);
        txt_Display_mobile_no = findViewById(R.id.txt_Display_mobile_no);
        edt_otp = findViewById(R.id.edt_otp);

        get_Mobile_No = getIntent().getStringExtra("Mobile_No");
        _customerId = getIntent().getStringExtra("_customerId");

        Log.i("On_Create", _customerId);
        Log.e("On_Create", get_Mobile_No);

        if (!get_Mobile_No.matches("")) {
            txt_Display_mobile_no.setText("OTP SEND ON " + get_Mobile_No);
        }

        editTextone = findViewById(R.id.editTextone);
        editTexttwo = findViewById(R.id.editTexttwo);
        editTextthree = findViewById(R.id.editTextthree);
        editTextfour = findViewById(R.id.editTextfour);

        editTextone.addTextChangedListener(this);
        editTexttwo.addTextChangedListener(this);
        editTextthree.addTextChangedListener(this);
        editTextfour.addTextChangedListener(this);


        setCounter();
        txt_link_Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (increment == false) {
                    resendForgotOtpAPI();
                    Toast.makeText(ActivityForgotPasswordOTP.this, "Reset counter", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Btn_verify.setOnClickListener(v -> {

            boolean validation = OtpValidation();

            if (validation == true) {
                verifiedForgotOtpAPI();
            }
        });

    }


    void verifiedForgotOtpAPI() {
        ClsVerifiedOtpParms objClsVerifiedOtpParms = new ClsVerifiedOtpParms();
        objClsVerifiedOtpParms.setUserCode(_customerId);
        objClsVerifiedOtpParms.setMobileNumber(get_Mobile_No);

      /*  String _OtpConcat = editTextone.getText().toString()
                .concat(editTexttwo.getText().toString())
                .concat(editTextthree.getText().toString())
                .concat(editTextfour.getText().toString());*/


        String _OtpConcat = edt_otp.getText().toString();

        objClsVerifiedOtpParms.setVerificationCode(_OtpConcat);
        objClsVerifiedOtpParms.setUserType("Customer");
        objClsVerifiedOtpParms.setOtpSendMode("forgotPwd");

        Gson gson = new Gson();
        String jsonInString = gson.toJson(objClsVerifiedOtpParms);
        Log.d("Result", "objClsVerifiedOtpParms- " + jsonInString);

        InterfaceVerifiedOTP interfaceVerifiedOTP = ApiClient.getRetrofitInstance().create(InterfaceVerifiedOTP.class);
        Log.e("--URL--", "interfaceDesignation: " + interfaceVerifiedOTP.toString());
        Call<ClsVerifiedOtpParms> call = interfaceVerifiedOTP.postVerificationOTP(objClsVerifiedOtpParms);
        Log.e("--URL--", "************************  before call : " + call.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(ActivityForgotPasswordOTP.this, "Working...", true);
        pd.show();


        call.enqueue(new Callback<ClsVerifiedOtpParms>() {
            @Override
            public void onResponse(Call<ClsVerifiedOtpParms> call, Response<ClsVerifiedOtpParms> response) {
                pd.dismiss();
                if (response.body() != null) {
                    String _response = response.body().getSuccess();
                    Log.d("_response", "onResponse: " + _response);

                    switch (_response) {
                        case "1":
                            Toast.makeText(ActivityForgotPasswordOTP.this, "OTP is verified", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ActivityForgotPasswordOTP.this, ActivityNewPassword.class);
                            intent.putExtra("Mobile_No", get_Mobile_No);
                            intent.putExtra("_customerId", _customerId);
                            startActivity(intent);
                            finish();
                            break;
                        case "2":
                            Toast.makeText(ActivityForgotPasswordOTP.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                            break;
                        case "3":
                            Toast.makeText(ActivityForgotPasswordOTP.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                            break;
                        case "0":
                            Toast.makeText(ActivityForgotPasswordOTP.this, "Failed OTP", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(ActivityForgotPasswordOTP.this, "Failed to send OTP", Toast.LENGTH_SHORT).show();
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

    void resendForgotOtpAPI() {
        ClsResendOtpParms objClsResendOtpParms = new ClsResendOtpParms();
        objClsResendOtpParms.setUserCode(_customerId);
        objClsResendOtpParms.setMobileNumber(get_Mobile_No);
        objClsResendOtpParms.setUserType("Customer");
        objClsResendOtpParms.setOtpSendMode("forgotPwd");

        InterfaceResendOTP interfaceResendOTP = ApiClient.getRetrofitInstance().create(InterfaceResendOTP.class);
        Log.e("--URL--", "interfaceDesignation " + interfaceResendOTP.toString());
        Call<ClsResendOtpParms> call = interfaceResendOTP.postResendOTP(objClsResendOtpParms);
        Log.e("--URL--", "************************" + call.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(ActivityForgotPasswordOTP.this, "Working...", true);
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
                            Toast.makeText(ActivityForgotPasswordOTP.this, "OTP is resend", Toast.LENGTH_SHORT).show();
                            resetCounter();
                            setCounter();
                            break;
                        default:
                            Toast.makeText(ActivityForgotPasswordOTP.this, "Failed to send OTP", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server error...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsResendOtpParms> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong,please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private Boolean OtpValidation() {

       /* if (editTextone.getText() == null || editTextone.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter character", Toast.LENGTH_SHORT).show();
            editTextone.requestFocus();
            return false;
        }
        if (editTexttwo.getText() == null || editTexttwo.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter character", Toast.LENGTH_SHORT).show();
            editTextone.requestFocus();
            return false;
        }
        if (editTextthree.getText() == null || editTextthree.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter character", Toast.LENGTH_SHORT).show();
            editTextone.requestFocus();
            return false;
        }
        if (editTextfour.getText() == null || editTextfour.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter character", Toast.LENGTH_SHORT).show();
            editTextone.requestFocus();
            return false;
        }*/

        if (edt_otp.getText() == null || edt_otp.getText().toString().length() > 4
                || edt_otp.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "4 characters are required",
                    Toast.LENGTH_SHORT).show();
            edt_otp.requestFocus();
            return false;
        }


        if (!ClsGlobal.CheckInternetConnection(ActivityForgotPasswordOTP.this)) {
            Toast.makeText(ActivityForgotPasswordOTP.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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

    public static void setOtpAutoFill(final String message) {
        try {
            if (message != null && message != "" && message.length() == 4) {

                Log.e("setOtpAutoFill ", message);

                String str1 = String.valueOf(message.charAt(0));
                String str2 = String.valueOf(message.charAt(1));
                String str3 = String.valueOf(message.charAt(2));
                String str4 = String.valueOf(message.charAt(3));

                editTextone.setText(str1);
                editTexttwo.setText(str2);
                editTextthree.setText(str3);
                editTextfour.setText(str4);

            }

        } catch (Exception e) {
            Log.d("_response", "message123: " + e.getMessage());
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
        unregisterReceiver(mSmsBroadcastReceiver);
        Log.e("MainActivity", "Unregistered receiver");

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        Log.e("afterCng", "editable:" + editable.toString());
//        if (editable.toString().length() == 0) {
//            if (editTextfour.getText().toString().length() == 0) {
//
//            }
//        }
//

        if (editable.length() == 1) {
            if (editTextone.length() != 0
                    && editTexttwo.length() == 0) {
                editTexttwo.requestFocus();
            } else if (editTexttwo.length() != 0
                    && editTextthree.length() == 0) {
                editTextthree.requestFocus();
            } else if (editTextthree.length() != 0
                    && editTextfour.length() == 0) {
                editTextfour.requestFocus();
            }
        } else if (editable.length() == 0) {
            if (editTextfour.length() == 0) {
                editTextthree.requestFocus();
            }
            if (editTextthree.length() == 0) {
                editTexttwo.requestFocus();
            }
            if (editTexttwo.length() == 0) {
                editTextone.requestFocus();
            }
        }

    }
}
