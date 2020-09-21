package com.demo.nspl.restaurantlite.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ApiClient;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.ClsPermission;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsCustomerProfileUpdateParams;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceCustomerProfileUpdate;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demo.nspl.restaurantlite.Global.ClsPermission.REQUEST_READ_PHONE_STATE;

public class ActivityUpdateProfile extends AppCompatActivity {


    EditText edt_contact_person, edt_address, edt_gst_no, edt_cin_no;
    Button btn_save;
    Toolbar toolbar;
    String _customerId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);


        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "ActivityUpdateProfile"));
        }

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ClsPermission.checkpermission(ActivityUpdateProfile.this);

        main();

    }

    private void main() {

        _customerId = getIntent().getStringExtra("_customerId");

        edt_contact_person = findViewById(R.id.edt_contact_person);
        edt_address = findViewById(R.id.edit_address);
        edt_gst_no = findViewById(R.id.edt_gst_no);
        edt_cin_no = findViewById(R.id.edt_cin_no);
        btn_save = findViewById(R.id.btn_save);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean validation = customerProfileUpdateValidation();
                if (validation == true) {
                    customerProfileUpdateAPI();
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE: {
                ClsPermission.checkPermission(requestCode, permissions, grantResults, ActivityUpdateProfile.this);
                return;
            }

        }

    }
    void customerProfileUpdateAPI() {
        ClsCustomerProfileUpdateParams objClsCustomerProfileUpdateParams = new ClsCustomerProfileUpdateParams();
        objClsCustomerProfileUpdateParams.setCustomerCode(_customerId);
        objClsCustomerProfileUpdateParams.setProductName(ClsGlobal.AppName);
        objClsCustomerProfileUpdateParams.setContactPerson(edt_contact_person.getText().toString().trim());
        objClsCustomerProfileUpdateParams.setCINNo(edt_cin_no.getText().toString().trim());
        objClsCustomerProfileUpdateParams.setGSTNo(edt_gst_no.getText().toString().trim());

        Gson gson = new Gson();
        String jsonInString = gson.toJson(objClsCustomerProfileUpdateParams);


        InterfaceCustomerProfileUpdate interfaceCustomerProfileUpdate = ApiClient.getRetrofitInstance().create(InterfaceCustomerProfileUpdate.class);
        Log.e("--URL--", "interfaceDesignation: " + interfaceCustomerProfileUpdate.toString());
        Call<ClsCustomerProfileUpdateParams> call = interfaceCustomerProfileUpdate.postUpdateProfile(objClsCustomerProfileUpdateParams);
        Log.e("--URL--", "************************  before call : " + call.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(ActivityUpdateProfile.this, "Loading...", true);
        pd.show();

        call.enqueue(new Callback<ClsCustomerProfileUpdateParams>() {
            @Override
            public void onResponse(Call<ClsCustomerProfileUpdateParams> call, Response<ClsCustomerProfileUpdateParams> response) {
                pd.dismiss();
                if (response.body() != null) {
                    String _response = response.body().getSuccess();
                    switch (_response) {
                        case "1":
                            Toast.makeText(ActivityUpdateProfile.this, "Your profile has been updated", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(ActivityUpdateProfile.this, LogInActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            ClsGlobal.autoLogout(ActivityUpdateProfile.this);
                            finish();
                            break;
                        case "0":
                            Toast.makeText(ActivityUpdateProfile.this, "Fail to update profile", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server error...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsCustomerProfileUpdateParams> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private Boolean customerProfileUpdateValidation() {


        if (edt_contact_person.getText() == null || edt_contact_person.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter contact person name", Toast.LENGTH_SHORT).show();
            edt_contact_person.requestFocus();
            return false;
        }
        if (!ClsGlobal.CheckInternetConnection(ActivityUpdateProfile.this)) {
            Toast.makeText(ActivityUpdateProfile.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
