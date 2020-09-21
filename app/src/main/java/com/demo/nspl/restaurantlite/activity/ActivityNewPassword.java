package com.demo.nspl.restaurantlite.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ApiClient;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsNewPasswordParams;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceNewPassword;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityNewPassword extends AppCompatActivity {

    EditText edt_new_pass, edt_confirm_pass;
    private String get_Mobile_No, _customerId;
    Button btn_add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "ActivityNewPassword"));
        }

        main();

    }

    private void main() {

        Toolbar toolbar = findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        get_Mobile_No = getIntent().getStringExtra("Mobile_No");
        _customerId = getIntent().getStringExtra("_customerId");

        edt_new_pass = findViewById(R.id.edt_new_pass);

        edt_confirm_pass = findViewById(R.id.edt_confirm_pass);
        btn_add = findViewById(R.id.btn_add);

        edt_confirm_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

//                String newPass = edt_new_pass.getText().toString().trim();
//                String confirmPass = edt_confirm_pass.getText().toString().trim();
//                if (newPass.equals(confirmPass)) {
//                    Toast.makeText(ActivityNewPassword.this, "Matching passwords=" + confirmPass, Toast.LENGTH_SHORT).show();
//                }
//

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validation = NewPasswordValidation();
                if (validation == true) {
                    NewPasswordAPI();

                }
            }
        });
    }

    void NewPasswordAPI() {

        ClsNewPasswordParams objClsNewPasswordParams = new ClsNewPasswordParams();
        objClsNewPasswordParams.setUserType("Customer");
        objClsNewPasswordParams.setMobileNumber(get_Mobile_No);
        objClsNewPasswordParams.setProductName(ClsGlobal.AppName);
        objClsNewPasswordParams.setApplicationVersion(ClsGlobal.getApplicationVersion(ActivityNewPassword.this));
        objClsNewPasswordParams.setIMEINumber(ClsGlobal.getIMEIno(ActivityNewPassword.this));
        objClsNewPasswordParams.setDeviceInfo(ClsGlobal.getDeviceInfo(ActivityNewPassword.this));
        objClsNewPasswordParams.setNewPassword(edt_new_pass.getText().toString().trim());

        Gson gson = new Gson();
        String jsonInString = gson.toJson(objClsNewPasswordParams);
        Log.d("Result", "objClsNewPasswordParams- " + jsonInString);

        InterfaceNewPassword interfaceNewPassword = ApiClient.getRetrofitInstance().create(InterfaceNewPassword.class);
        Log.e("--URL--", "interfaceDesignation: " + interfaceNewPassword.toString());
        Call<ClsNewPasswordParams> call = interfaceNewPassword.postNewPassword(objClsNewPasswordParams);
        Log.e("--URL--", "************************  before call : " + call.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(ActivityNewPassword.this,
                        "Working...", true);
        pd.show();

        call.enqueue(new Callback<ClsNewPasswordParams>() {
            @Override
            public void onResponse(Call<ClsNewPasswordParams> call, Response<ClsNewPasswordParams> response) {
                pd.dismiss();
                if (response.body() != null) {
                    String _response = response.body().getSuccess();
                    Log.d("_response", "onResponse: " + _response);
                    if (_response.equals("1")) {

                        // Clear User id and Password from SharedPreferences.
                        ClsGlobal.Clear_User_Password(ActivityNewPassword.this);
                        Toast.makeText(ActivityNewPassword.this, "Password reset successfully", Toast.LENGTH_SHORT).show();


                        finish();
                    } else if (_response.equals("2")) {
                        Toast.makeText(ActivityNewPassword.this, "Invalid mobile no", Toast.LENGTH_SHORT).show();
                    } else if (_response.equals("0")) {
                        Toast.makeText(ActivityNewPassword.this, "New password is not set", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server error...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsNewPasswordParams> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean NewPasswordValidation() {

        if (edt_new_pass.getText() == null || edt_new_pass.getText().toString().length() <= 4
                || edt_new_pass.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Minimum 6 is required", Toast.LENGTH_SHORT).show();
            edt_new_pass.requestFocus();
            return false;
        }
        if (edt_confirm_pass.getText() == null || edt_confirm_pass.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter confirm password", Toast.LENGTH_SHORT).show();
            edt_confirm_pass.requestFocus();
            return false;
        }
        String newPass = edt_new_pass.getText().toString().trim().trim();
        String confirmPass = edt_confirm_pass.getText().toString().trim();
        if (!newPass.equals(confirmPass)) {
            Toast.makeText(ActivityNewPassword.this,
                    "Password is not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!ClsGlobal.CheckInternetConnection(ActivityNewPassword.this)) {
            Toast.makeText(ActivityNewPassword.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
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
