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

import com.demo.nspl.restaurantlite.Complain.SelectComplainActivity;
import com.demo.nspl.restaurantlite.Global.ApiClient;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsComplainParams;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsVerifiedComplainOtpParms;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceComplainDisposition;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceComplainVerifyOTP;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityHelp extends AppCompatActivity implements TextWatcher {

    TextView txt_Display_mobile_no, txt_link_Resend;
    EditText edt_mobile_no;
    Button btn_send, Btn_verify;
    //    String _customerId = "";
    Toolbar toolbar;
    LinearLayout ll_otp;
    TextView textView;
    public int counter = 30;
    boolean increment = true;
    CountDownTimer timer;
    private static final int PERMISSION_REQUEST_ID = 100;
    //MySmsBroadcastReceiver mSmsBroadcastReceiver;
    private final String BROADCAST_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private IntentFilter intentFilter;
    private static EditText editTextone, editTexttwo, editTextthree, editTextfour;

    EditText edt_otp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestRuntimePermissions(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS);

        main();

    }

    private void main() {


        textView = findViewById(R.id.textView);
        txt_link_Resend = findViewById(R.id.txt_link_Resend);
        edt_mobile_no = findViewById(R.id.edt_mobile_no);


        txt_Display_mobile_no = findViewById(R.id.txt_Display_mobile_no);
        btn_send = findViewById(R.id.btn_next);
        Btn_verify = findViewById(R.id.Btn_verify);
        ll_otp = findViewById(R.id.ll_otp);


        edt_otp = findViewById(R.id.edt_otp);


        editTextone = findViewById(R.id.editTextone);
        editTexttwo = findViewById(R.id.editTexttwo);
        editTextthree = findViewById(R.id.editTextthree);
        editTextfour = findViewById(R.id.editTextfour);

        editTextone.addTextChangedListener(this);
        editTexttwo.addTextChangedListener(this);
        editTextthree.addTextChangedListener(this);
        editTextfour.addTextChangedListener(this);


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_Display_mobile_no.setText("OTP SEND ON ".concat(edt_mobile_no.getText().toString()));

                boolean validation = ChangeMobileNoValidation();
                if (validation == true) {
                    sendComplainOTPAPI();
                }

            }
        });


        txt_link_Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (increment == false) {
                    sendComplainOTPAPI();
                    resetCounter();
                } else {
                    Toast.makeText(ActivityHelp.this, "Wait for counter enable.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Btn_verify.setOnClickListener(v -> {

            boolean validation = OtpValidation();

            if (validation == true) {
                verifiedComplainOtpAPI();
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

        /*if (editTextone.getText() == null || editTextone.getText().toString().trim().isEmpty()) {
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


        if (!ClsGlobal.CheckInternetConnection(ActivityHelp.this)) {
            Toast.makeText(ActivityHelp.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
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


    void sendComplainOTPAPI() {

        ClsComplainParams obj = new ClsComplainParams();
        obj.setMobileNumber(edt_mobile_no.getText().toString());
        obj.setOTPSendMode("RegisterComplain");

        Gson gson = new Gson();
        String jsonInString = gson.toJson(obj);
        Log.d("Result", "ComplainDispositionAPI- " + jsonInString);

        InterfaceComplainDisposition interfaceComplainDisposition = ApiClient.getRetrofitInstance().create(InterfaceComplainDisposition.class);
        Log.e("--URL--", "interfaceDesignation: " + interfaceComplainDisposition.toString());
        Call<ClsComplainParams> call = interfaceComplainDisposition.postComplain(obj);
        Log.e("--URL--", "************************  before call : " + call.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(ActivityHelp.this, "Loading...", true);
        pd.show();

        call.enqueue(new Callback<ClsComplainParams>() {
            @Override
            public void onResponse(Call<ClsComplainParams> call, Response<ClsComplainParams> response) {
                pd.dismiss();
                if (response.body() != null) {
                    String _response = response.body().getSuccess();
                    Log.d("--response--", "_response- " + _response);

                    switch (_response) {
                        case "1":
                            Toast.makeText(ActivityHelp.this, "Waiting for OTP", Toast.LENGTH_SHORT).show();
                            ll_otp.setVisibility(View.VISIBLE);
                            setCounter();
                            break;
                        case "2":
                            Toast.makeText(ActivityHelp.this, "Invalid mobile no", Toast.LENGTH_SHORT).show();
                            break;
                        case "3":
                            Toast.makeText(ActivityHelp.this, "error in mobile no", Toast.LENGTH_SHORT).show();
                            break;

                    }
                }

            }

            @Override
            public void onFailure(Call<ClsComplainParams> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    void verifiedComplainOtpAPI() {
        ClsVerifiedComplainOtpParms objClsVerifiedOtpParms = new ClsVerifiedComplainOtpParms();
        objClsVerifiedOtpParms.setMobileNumber(edt_mobile_no.getText().toString());

        /*String _OtpConcat = editTextone.getText().toString()
                .concat(editTexttwo.getText().toString())
                .concat(editTextthree.getText().toString())
                .concat(editTextfour.getText().toString());*/

        String _OtpConcat = edt_otp.getText().toString();

        objClsVerifiedOtpParms.setVerificationCode(_OtpConcat);
        objClsVerifiedOtpParms.setOtpSendMode("RegisterComplain");

        Gson gson = new Gson();
        String jsonInString = gson.toJson(objClsVerifiedOtpParms);
        Log.d("Result", "objClsVerifiedOtpParms- " + jsonInString);

        InterfaceComplainVerifyOTP interfaceVerifiedOTP = ApiClient.getRetrofitInstance().create(InterfaceComplainVerifyOTP.class);
        Log.e("--URL--", "interfaceDesignation: " + interfaceVerifiedOTP.toString());
        Call<ClsVerifiedComplainOtpParms> call = interfaceVerifiedOTP.postVerifyOTP(objClsVerifiedOtpParms);
        Log.e("--URL--", "************************  before call : " + call.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(ActivityHelp.this, "Working...", true);
        pd.show();

        call.enqueue(new Callback<ClsVerifiedComplainOtpParms>() {
            @Override
            public void onResponse(Call<ClsVerifiedComplainOtpParms> call, Response<ClsVerifiedComplainOtpParms> response) {
                pd.dismiss();
                if (response.body() != null) {
                    String _response = response.body().getSuccess();
                    Log.d("_response", "onResponse: " + _response);
                    switch (_response) {

                        case "1":
                            Toast.makeText(ActivityHelp.this, "OTP is Verified", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), SelectComplainActivity.class);
                            intent.putExtra("mobile", edt_mobile_no.getText().toString());
                            startActivity(intent);
                            finish();
                            break;
                        case "2":
                            Toast.makeText(ActivityHelp.this, "Invalid Record", Toast.LENGTH_SHORT).show();
                            break;
                        case "3":
                            Toast.makeText(ActivityHelp.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                            break;
                        case "0":
                            Toast.makeText(ActivityHelp.this, "Failed OTP", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(ActivityHelp.this, "Failed to send OTP", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }
            }

            @Override
            public void onFailure(Call<ClsVerifiedComplainOtpParms> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong,please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean ChangeMobileNoValidation() {

        if (edt_mobile_no.getText() == null || edt_mobile_no.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter mobile no", Toast.LENGTH_SHORT).show();
            edt_mobile_no.requestFocus();
            return false;
        }

        if (!ClsGlobal.CheckInternetConnection(ActivityHelp.this)) {
            Toast.makeText(ActivityHelp.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

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
