package com.demo.nspl.restaurantlite.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ApiClient;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsForgotPasswordParms;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceForgotPassword;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityForgotPassword extends AppCompatActivity {

    EditText edt_mobile;
    TextView forgot_button;
    String _customerId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "ActivityForgotPassword"));
        }

        main();

    }

    private void main() {
        forgot_button = findViewById(R.id.forgot_button);

        edt_mobile = findViewById(R.id.edt_mobile);
        edt_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edt_mobile.length() == 10) {
                    Toast.makeText(getApplicationContext(), "Maximum 10 digits only", Toast.LENGTH_SHORT).show();
                }
            }
        });

        forgot_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean validation = ForgotPassNumberValidation();
                if (validation == true) {
                    forgotPassAPI();
                }
            }
        });

    }

    void forgotPassAPI() {

        String AppVersion = ClsGlobal.getApplicationVersion(ActivityForgotPassword.this);
        ClsForgotPasswordParms objClsForgotPasswordParms = new ClsForgotPasswordParms();

        objClsForgotPasswordParms.setUserType("Customer");
        objClsForgotPasswordParms.setMobileNumber(edt_mobile.getText().toString());
        objClsForgotPasswordParms.setProductName(ClsGlobal.AppName);
        objClsForgotPasswordParms.setApplicationVersion(AppVersion);
        objClsForgotPasswordParms.setIMEINumber(ClsGlobal.getIMEIno(ActivityForgotPassword.this));
        objClsForgotPasswordParms.setDeviceInfo(ClsGlobal.getDeviceInfo(ActivityForgotPassword.this));

        Gson gson = new Gson();
        String jsonInString = gson.toJson(objClsForgotPasswordParms);
        Log.d("--URL--", "ForgotPass: " + jsonInString);

        InterfaceForgotPassword interfaceForgotPassword = ApiClient.getRetrofitInstance().create(InterfaceForgotPassword.class);
        Log.e("--URL--", "interfaceDesignation: " + interfaceForgotPassword.toString());
        Call<ClsForgotPasswordParms> call = interfaceForgotPassword.postForgotPass(objClsForgotPasswordParms);
        Log.e("--URL--", "************************  before call : " + call.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(ActivityForgotPassword.this, "Working...", true);
        pd.show();

        call.enqueue(new Callback<ClsForgotPasswordParms>() {
            @Override
            public void onResponse(Call<ClsForgotPasswordParms> call, Response<ClsForgotPasswordParms> response) {
                pd.dismiss();
                if (response.body() != null) {
                    String _response = response.body().getSuccess();
                    Log.d("_response", "onResponse: " + _response);
                    if (_response.equals("1")) {

                        _customerId = String.valueOf(response.body().getData().getCode());
                        Intent intent = new Intent(ActivityForgotPassword.this, ActivityForgotPasswordOTP.class);
                        intent.putExtra("Mobile_No", edt_mobile.getText().toString().trim());
                        intent.putExtra("_customerId", _customerId);
                        Log.i("--URL--", "_customerId--:   " + _customerId);
                        startActivity(intent);
                        finish();

                        Toast.makeText(ActivityForgotPassword.this, "Send OTP successfully", Toast.LENGTH_SHORT).show();
                    } else if (_response.equals("2")) {
                        Toast.makeText(ActivityForgotPassword.this, "Error send OTP", Toast.LENGTH_SHORT).show();
                    } else if (_response.equals("0")) {
                        Toast.makeText(ActivityForgotPassword.this, "Failed OTP", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server error...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsForgotPasswordParms> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private Boolean ForgotPassNumberValidation() {

        if (edt_mobile.getText() == null || edt_mobile.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter mobile no", Toast.LENGTH_SHORT).show();
            edt_mobile.requestFocus();
            return false;
        }

        if (!ClsGlobal.CheckInternetConnection(ActivityForgotPassword.this)) {
            Toast.makeText(ActivityForgotPassword.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
