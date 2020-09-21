package com.demo.nspl.restaurantlite.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ApiClient;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.ClsPermission;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsCurrentPackageDetailsList;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsCurrentPackageDetailsParams;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceCurrentPackageDetails;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demo.nspl.restaurantlite.Global.ClsPermission.REQUEST_READ_PHONE_STATE;

public class CurrentPackageActivity extends AppCompatActivity {

    private Toolbar toolbar;
    String _customerId = "";
    TextView txt_PackageName, txt_PackageRegisrationOnDate, txt_PackageExpireOnDate, txt_Price,
            txt_ReferalCode, txt_TransectionRefrenceNumber,
            txt_PaymentMode, txt_PaymentGateway, txt_nodata;

    List<ClsCurrentPackageDetailsList> lstClsCurrentPackageDetailsLists = new ArrayList<>();
    LinearLayout ll_pkg_details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_package);


        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "CurrentPackageActivity"));
        }

        ClsPermission.checkpermission(CurrentPackageActivity.this);

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _customerId = getIntent().getStringExtra("_customerId");


        main();
    }


    private void main() {

        ll_pkg_details = findViewById(R.id.ll_pkg_details);
        txt_nodata = findViewById(R.id.txt_nodata);
        txt_PackageName = findViewById(R.id.txt_PackageName);
        txt_PackageRegisrationOnDate = findViewById(R.id.txt_PackageRegisrationOnDate);
        txt_PackageExpireOnDate = findViewById(R.id.txt_PackageExpireOnDate);
        txt_Price = findViewById(R.id.txt_Price);
        txt_ReferalCode = findViewById(R.id.txt_ReferalCode);
        txt_TransectionRefrenceNumber = findViewById(R.id.txt_TransectionRefrenceNumber);
        txt_PaymentMode = findViewById(R.id.txt_PaymentMode);
        txt_PaymentGateway = findViewById(R.id.txt_PaymentGateway);


        if (ClsGlobal.CheckInternetConnection(CurrentPackageActivity.this)) {
//            txt_nodata.setVisibility(View.VISIBLE);
//            ll_pkg_details.setVisibility(View.GONE);


            CurrentPackageDetailsAPI();

        } else {
            CurrentPackageDetailsAPI();
//            txt_nodata.setVisibility(View.GONE);
//            ll_pkg_details.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        // ClsPermission.checkpermission(CurrentPackageActivity.this);
//
//        if (!ClsGlobal.CheckInternetConnection(CurrentPackageActivity.this)) {
//            txt_nodata.setVisibility(View.VISIBLE);
//            ll_pkg_details.setVisibility(View.GONE);
//        } else {
//            CurrentPackageDetailsAPI();
//            txt_nodata.setVisibility(View.GONE);
//            ll_pkg_details.setVisibility(View.VISIBLE);
//        }

    }

    void CurrentPackageDetailsAPI() {

        InterfaceCurrentPackageDetails interfacePackage =
                ApiClient.getRetrofitInstance().create(InterfaceCurrentPackageDetails.class);

        Call<ClsCurrentPackageDetailsParams> objClsCurrentPackageDetailsParamsCall =
                interfacePackage.value(_customerId, ClsGlobal.AppName);

        Log.e("--URL--", "CashCollectionVerificationAPI: " + objClsCurrentPackageDetailsParamsCall.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(CurrentPackageActivity.this, "Loading...", true);
        pd.show();

        objClsCurrentPackageDetailsParamsCall.enqueue(new Callback<ClsCurrentPackageDetailsParams>() {
            @Override
            public void onResponse(Call<ClsCurrentPackageDetailsParams> call, Response<ClsCurrentPackageDetailsParams> response) {
                pd.dismiss();
                if (response.body() != null) {
                    String success = response.body().getSuccess();
                    Log.e("--success--", "HHTRequestReport:-- " + success);

                    if (success.equals("1")) {

                        txt_nodata.setVisibility(View.GONE);
                        ll_pkg_details.setVisibility(View.VISIBLE);

                        lstClsCurrentPackageDetailsLists = new ArrayList<>();
                        lstClsCurrentPackageDetailsLists = response.body().getData();

                        ClsCurrentPackageDetailsList obj = lstClsCurrentPackageDetailsLists.get(0);

                        txt_PackageName.setText(obj.getPackageName());

                        txt_PackageRegisrationOnDate.setText("START DATE: " + obj.getPackageRegisrationOnDate());
                        txt_PackageExpireOnDate.setText("EXPIRE ON: " + obj.getPackageExpireOnDate());

                        txt_Price.setText("\u20B9 " + ClsGlobal.round(obj.getTotalAmount(), 2));
                        txt_ReferalCode.setText("REFERRAL CODE: " + obj.getReferalCode());
                        txt_TransectionRefrenceNumber.setText("TRANSACTION NO# " + obj.getTransectionRefrenceNumber());

                        if(obj.getPaymentGateway() != null){
                            txt_PaymentGateway.setText("PAYMENT GATEWAY: " + obj.getPaymentGateway());
                        }else {
                            txt_PaymentGateway.setText("PAYMENT GATEWAY: ");
                        }

                        if(obj.getPaymentMode() != null){
                            txt_PaymentMode.setText("PAYMENT MODE: " + obj.getPaymentMode());
                        }else {
                            txt_PaymentMode.setText("PAYMENT MODE: ");
                        }

                    } else {
                        Toast.makeText(CurrentPackageActivity.this, "Record not found", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    txt_nodata.setVisibility(View.VISIBLE);
                    ll_pkg_details.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Server error...!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ClsCurrentPackageDetailsParams> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong...Internet issue!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE: {
                ClsPermission.checkPermission(requestCode, permissions, grantResults, CurrentPackageActivity.this);
                return;
            }

        }

    }
}