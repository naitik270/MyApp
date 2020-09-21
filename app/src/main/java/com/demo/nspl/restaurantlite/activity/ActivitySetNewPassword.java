package com.demo.nspl.restaurantlite.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ApiClient;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsChangePasswordParams;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceChangePassword;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySetNewPassword extends AppCompatActivity {

    TextInputLayout input_new_pass, input_confirm_pass;
    EditText edt_new_pass, edt_confirm_pass;
    Toolbar toolbar;
    Button btn_save;
    String _customerId = "", _oldPass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "ActivitySetNewPassword"));
        }

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        main();
    }

    private void main() {

        _customerId = getIntent().getStringExtra("_customerId");
        _oldPass = getIntent().getStringExtra("_oldPass");

        input_new_pass = findViewById(R.id.input_new_pass);
        input_confirm_pass = findViewById(R.id.input_confirm_pass);

        edt_new_pass = findViewById(R.id.edt_new_pass);
        edt_confirm_pass = findViewById(R.id.edt_confirm_pass);
        btn_save = findViewById(R.id.btn_save);

        edt_new_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edt_new_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        edt_confirm_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edt_confirm_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());

        btn_save.setOnClickListener(v -> {

            boolean validation = ChangePassValidation();
            if (validation == true) {
                changePasswordAPI();
            }
        });

    }

    void changePasswordAPI() {
        ClsChangePasswordParams objClsChangePasswordParams = new ClsChangePasswordParams();
        objClsChangePasswordParams.setUserName(_customerId);
        objClsChangePasswordParams.setOldPassword(_oldPass);
        objClsChangePasswordParams.setNewPassword(edt_new_pass.getText().toString().trim());
        objClsChangePasswordParams.setProductName(ClsGlobal.AppName);
        objClsChangePasswordParams.setApplicationVersion(ClsGlobal.getApplicationVersion(ActivitySetNewPassword.this));
        objClsChangePasswordParams.setIMEINumber(ClsGlobal.getIMEIno(ActivitySetNewPassword.this));

        Gson gson = new Gson();
        String jsonInString = gson.toJson(objClsChangePasswordParams);
        Log.d("Result", "changePasswordAPI- " + jsonInString);


        InterfaceChangePassword interfaceChangePassword = ApiClient.getRetrofitInstance().create(InterfaceChangePassword.class);
        Log.e("--URL--", "interfaceDesignation: " + interfaceChangePassword.toString());
        Call<ClsChangePasswordParams> call = interfaceChangePassword.postChangePass(objClsChangePasswordParams);
        Log.e("--URL--", "************************  before call : " + call.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(ActivitySetNewPassword.this,
                        "Waiting for set new password...", true);
        pd.show();

        call.enqueue(new Callback<ClsChangePasswordParams>() {
            @Override
            public void onResponse(Call<ClsChangePasswordParams> call, Response<ClsChangePasswordParams> response) {
                pd.dismiss();
                if (response.body() != null) {
                    String _response = response.body().getSuccess();
                    switch (_response) {
                        case "1":
                            Toast.makeText(ActivitySetNewPassword.this, "Your password has been change. Login with new password", Toast.LENGTH_LONG).show();
                            // Clear User id and Password from SharedPreferences.
                            ClsGlobal.Clear_User_Password(ActivitySetNewPassword.this);
                            finish();
                            break;
                        case "2":
                            Toast.makeText(ActivitySetNewPassword.this, "Invalid current password", Toast.LENGTH_SHORT).show();
                            break;
                        case "3":
                            Toast.makeText(ActivitySetNewPassword.this, "error in change password", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server error...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsChangePasswordParams> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private Boolean ChangePassValidation() {

        String newPass = edt_new_pass.getText().toString().trim();
        String confirmPass = edt_confirm_pass.getText().toString().trim();

        if (edt_new_pass.getText() == null || edt_new_pass.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter new password", Toast.LENGTH_SHORT).show();
            edt_new_pass.requestFocus();
            return false;
        } else if (edt_new_pass.getText().length() < 5) {
            Toast.makeText(getApplicationContext(), "Minimum 6 character is set", Toast.LENGTH_SHORT).show();
            edt_new_pass.requestFocus();
            return false;
        } else if (edt_confirm_pass.getText() == null || edt_confirm_pass.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter confirm password", Toast.LENGTH_SHORT).show();
            edt_confirm_pass.requestFocus();
            return false;
        } else if (!newPass.equals(confirmPass)) {
            Toast.makeText(getApplicationContext(), "Password not matching", Toast.LENGTH_SHORT).show();
            edt_confirm_pass.requestFocus();
            return false;
        }
        if (!ClsGlobal.CheckInternetConnection(ActivitySetNewPassword.this)) {
            Toast.makeText(ActivitySetNewPassword.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}
