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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsChangeMobileNumberParams;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsResendOtpParms;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsVerifiedOtpParms;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceChangeMobileNumber;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceResendOTP;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceVerifiedOTP;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityChangeMobileNum extends AppCompatActivity {

    TextView txt_Display_mobile_no, txt_link_Resend;
    EditText edt_mobile_no;
    Button btn_send, Btn_verify;
    String _customerId = "";
    Toolbar toolbar;
    LinearLayout ll_otp;

    TextView textView;
    public int counter = 30;
    boolean increment = true;
    CountDownTimer timer;

    private static final int PERMISSION_REQUEST_ID = 100;
    MySmsBroadcastReceiver mSmsBroadcastReceiver;
    private final String BROADCAST_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private IntentFilter intentFilter;
    private static EditText editTextone, editTexttwo, editTextthree, editTextfour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_mobile_num);


        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "ActivityChangeMobileNum"));
        }

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mSmsBroadcastReceiver = new MySmsBroadcastReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION);
        requestRuntimePermissions(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS);

        main();

    }

    private void main() {


        _customerId = getIntent().getStringExtra("_customerId");

        textView = findViewById(R.id.textView);
        txt_link_Resend = findViewById(R.id.txt_link_Resend);
        edt_mobile_no = findViewById(R.id.edt_mobile_no);
        txt_Display_mobile_no = findViewById(R.id.txt_Display_mobile_no);
        txt_Display_mobile_no.setText("OTP SEND ON ".concat(edt_mobile_no.getText().toString()));
        btn_send = findViewById(R.id.btn_next);
        Btn_verify = findViewById(R.id.Btn_verify);
        ll_otp = findViewById(R.id.ll_otp);

        editTextone = findViewById(R.id.editTextone);
        editTexttwo = findViewById(R.id.editTexttwo);
        editTextthree = findViewById(R.id.editTextthree);
        editTextfour = findViewById(R.id.editTextfour);


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean validation = ChangeMobileNoValidation();
                if (validation == true) {
                    changeMobileNumberAPI();
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


        txt_link_Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (increment == false) {
                    resendForgotOtpAPI();
                    Toast.makeText(ActivityChangeMobileNum.this, "Reset counter", Toast.LENGTH_SHORT).show();
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


    private Boolean OtpValidation() {

        if (editTextone.getText() == null || editTextone.getText().toString().trim().isEmpty()) {
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
        }
        if (!ClsGlobal.CheckInternetConnection(ActivityChangeMobileNum.this)) {
            Toast.makeText(ActivityChangeMobileNum.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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

    void changeMobileNumberAPI() {
        ClsChangeMobileNumberParams objClsChangeMobileNumberParams = new ClsChangeMobileNumberParams();
        objClsChangeMobileNumberParams.setUserType("Customer");
        objClsChangeMobileNumberParams.setUserCode(_customerId);
        objClsChangeMobileNumberParams.setMobileNumber(edt_mobile_no.getText().toString());
        objClsChangeMobileNumberParams.setApplicationVersion(ClsGlobal.getApplicationVersion(ActivityChangeMobileNum.this));
        objClsChangeMobileNumberParams.setProductName(ClsGlobal.AppName);
        objClsChangeMobileNumberParams.setIMEINumber(ClsGlobal.getIMEIno(ActivityChangeMobileNum.this));
        objClsChangeMobileNumberParams.setOTPSendMode("ChangeMobileNo");

        Gson gson = new Gson();
        String jsonInString = gson.toJson(objClsChangeMobileNumberParams);
        Log.d("Result", "changeMobileNumberAPI- " + jsonInString);

        InterfaceChangeMobileNumber interfaceChangeMobileNumber = ApiClient.getRetrofitInstance().create(InterfaceChangeMobileNumber.class);
        Log.e("--URL--", "interfaceDesignation: " + interfaceChangeMobileNumber.toString());
        Call<ClsChangeMobileNumberParams> call = interfaceChangeMobileNumber.postChangeMobileNumber(objClsChangeMobileNumberParams);
        Log.e("--URL--", "************************  before call : " + call.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(ActivityChangeMobileNum.this, "Loading...", true);
        pd.show();

        call.enqueue(new Callback<ClsChangeMobileNumberParams>() {
            @Override
            public void onResponse(Call<ClsChangeMobileNumberParams> call, Response<ClsChangeMobileNumberParams> response) {
                pd.dismiss();
                if (response.body() != null) {
                    String _response = response.body().getSuccess();
                    switch (_response) {
                        case "1":
                            Toast.makeText(ActivityChangeMobileNum.this, "Waiting for OTP", Toast.LENGTH_SHORT).show();
                            ll_otp.setVisibility(View.VISIBLE);
                            setCounter();
                            break;
                        case "2":
                            Toast.makeText(ActivityChangeMobileNum.this, "Invalid mobile no", Toast.LENGTH_SHORT).show();
                            break;
                        case "3":
                            Toast.makeText(ActivityChangeMobileNum.this, "error in mobile no", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server error...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsChangeMobileNumberParams> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void verifiedForgotOtpAPI() {
        ClsVerifiedOtpParms objClsVerifiedOtpParms = new ClsVerifiedOtpParms();
        objClsVerifiedOtpParms.setUserCode(_customerId);
        objClsVerifiedOtpParms.setMobileNumber(edt_mobile_no.getText().toString());

        String _OtpConcat = editTextone.getText().toString()
                .concat(editTexttwo.getText().toString())
                .concat(editTextthree.getText().toString())
                .concat(editTextfour.getText().toString());

        objClsVerifiedOtpParms.setVerificationCode(_OtpConcat);
        objClsVerifiedOtpParms.setUserType("Customer");
        objClsVerifiedOtpParms.setOtpSendMode("ChangeMobileNo");

        Gson gson = new Gson();
        String jsonInString = gson.toJson(objClsVerifiedOtpParms);
        Log.d("Result", "objClsVerifiedOtpParms- " + jsonInString);

        InterfaceVerifiedOTP interfaceVerifiedOTP = ApiClient.getRetrofitInstance().create(InterfaceVerifiedOTP.class);
        Log.e("--URL--", "interfaceDesignation: " + interfaceVerifiedOTP.toString());
        Call<ClsVerifiedOtpParms> call = interfaceVerifiedOTP.postVerificationOTP(objClsVerifiedOtpParms);
        Log.e("--URL--", "************************  before call : " + call.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(ActivityChangeMobileNum.this, "Working...", true);
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
                            Toast.makeText(ActivityChangeMobileNum.this, "your mobile number has been changed successfully. please login with new Mobile No.", Toast.LENGTH_LONG).show();

                            Intent i = new Intent(ActivityChangeMobileNum.this, LogInActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            ClsGlobal.autoLogout(ActivityChangeMobileNum.this);

                            // Clear User id and Password from SharedPreferences.
                            ClsGlobal.Clear_User_Password(ActivityChangeMobileNum.this);
                            finish();
                            break;
                        case "2":
                            Toast.makeText(ActivityChangeMobileNum.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                            break;
                        case "3":
                            Toast.makeText(ActivityChangeMobileNum.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                            break;
                        case "0":
                            Toast.makeText(ActivityChangeMobileNum.this, "Failed OTP", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(ActivityChangeMobileNum.this, "Failed to send OTP", Toast.LENGTH_SHORT).show();
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
        objClsResendOtpParms.setMobileNumber(edt_mobile_no.getText().toString());
        objClsResendOtpParms.setUserType("Customer");
        objClsResendOtpParms.setOtpSendMode("ChangeMobileNo");

        InterfaceResendOTP interfaceResendOTP = ApiClient.getRetrofitInstance().create(InterfaceResendOTP.class);
        Log.e("--URL--", "interfaceDesignation " + interfaceResendOTP.toString());
        Call<ClsResendOtpParms> call = interfaceResendOTP.postResendOTP(objClsResendOtpParms);
        Log.e("--URL--", "************************" + call.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(ActivityChangeMobileNum.this, "Working...", true);
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
                            Toast.makeText(ActivityChangeMobileNum.this, "OTP is resend", Toast.LENGTH_SHORT).show();
                            resetCounter();
                            setCounter();
                            break;
                        default:
                            Toast.makeText(ActivityChangeMobileNum.this, "Failed to send OTP", Toast.LENGTH_SHORT).show();
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

    public static void changeMobileNo(final String message) {
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


    private Boolean ChangeMobileNoValidation() {

        if (edt_mobile_no.getText() == null || edt_mobile_no.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter mobile no", Toast.LENGTH_SHORT).show();
            edt_mobile_no.requestFocus();
            return false;
        }

        if (!ClsGlobal.CheckInternetConnection(ActivityChangeMobileNum.this)) {
            Toast.makeText(ActivityChangeMobileNum.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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


}
