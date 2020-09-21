package com.demo.nspl.restaurantlite.SMS;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ApiClient;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.PlansUtility.CardFragmentPagerAdapter;
import com.demo.nspl.restaurantlite.PlansUtility.ShadowTransformer;
import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetSmsIdPackagesActivity extends AppCompatActivity {


    private ViewPager mViewPager;
    Toolbar toolbar;
    TextView txt_no_package;
    List<ClsGetSmsPackageList> lstClsGetSmsPackageLists;
    GetSmsPagerAdapter getSmsPagerAdapter;
    CardFragmentPagerAdapter mFragmentCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    String _customerId = "", registrationMode = "";
    public static boolean finishFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_sms_plans);


        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "GetSmsIdPackagesActivity"));
        }

        toolbar = findViewById(R.id.toolbar);
        txt_no_package = findViewById(R.id.txt_no_package);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager = findViewById(R.id.viewPager);
        _customerId = getIntent().getStringExtra("_customerId");
        registrationMode = getIntent().getStringExtra("registrationMode");

        ClsGetSmsPackageParam obj = new ClsGetSmsPackageParam();
        obj.setProductName(ClsGlobal.AppName);
        obj.setRegistrationMode(registrationMode);

        if (ClsGlobal.CheckInternetConnection(GetSmsIdPackagesActivity.this)) {
            getSmsPackageAPI(obj);
            txt_no_package.setVisibility(View.GONE);
        } else {
            txt_no_package.setVisibility(View.VISIBLE);
        }

        txt_no_package.setOnClickListener(v -> finish());


    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    private void getSmsPackageAPI(final ClsGetSmsPackageParam obj) {

        InterfaceGetSmsPackages interfacePackage =
                ApiClient.getDemoInstance().create(InterfaceGetSmsPackages.class);


        Log.e("--URL--", "API: " + ApiClient.getDemoInstance().toString());

        Call<ClsGetSmsPackageParam> objCall =
                interfacePackage.value(obj.getProductName(), obj.getRegistrationMode());

        Log.e("--URL--", "objCall: " + objCall.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(GetSmsIdPackagesActivity.this, "Loading...", true);
        pd.show();

        objCall.enqueue(new Callback<ClsGetSmsPackageParam>() {
            @Override
            public void onResponse(Call<ClsGetSmsPackageParam> call, Response<ClsGetSmsPackageParam> response) {
                pd.dismiss();
                if (response.body() != null) {
                    lstClsGetSmsPackageLists = new ArrayList<ClsGetSmsPackageList>();
                    lstClsGetSmsPackageLists = response.body().getData();

                    if (lstClsGetSmsPackageLists != null && lstClsGetSmsPackageLists.size() != 0) {
                        getSmsPagerAdapter = new GetSmsPagerAdapter(GetSmsIdPackagesActivity.this);
                        for (ClsGetSmsPackageList objClsGetSmsPackageList : lstClsGetSmsPackageLists) {

                            getSmsPagerAdapter.addCardItem(objClsGetSmsPackageList);
                            mFragmentCardAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(),
                                    dpToPixels(2, GetSmsIdPackagesActivity.this));


                            mCardShadowTransformer = new ShadowTransformer(mViewPager, getSmsPagerAdapter);
                            Log.e("--URL--", "Step 11");
                            mViewPager.setAdapter(getSmsPagerAdapter);
                            Log.e("--URL--", "Step 12");
                            mCardShadowTransformer.enableScaling(true);
                            Log.e("--URL--", "Step 13");

                            mViewPager.setPageTransformer(false, mCardShadowTransformer);

                            Log.e("--URL--", "Step 14");
                            mViewPager.setCurrentItem(1);
                            Log.e("--URL--", "Step 15");

                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server error...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsGetSmsPackageParam> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    protected void onResume() {
        super.onResume();
        if (finishFlag){
            finish();
            finishFlag = false;
        }

    }
}
