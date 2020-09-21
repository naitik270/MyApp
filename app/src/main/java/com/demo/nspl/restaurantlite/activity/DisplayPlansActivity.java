package com.demo.nspl.restaurantlite.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ApiClient;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.PlansUtility.CardFragmentPagerAdapter;
import com.demo.nspl.restaurantlite.PlansUtility.CardPagerAdapter;
import com.demo.nspl.restaurantlite.PlansUtility.ShadowTransformer;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsPackageList;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsPackageParameter;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfacePackage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayPlansActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    Toolbar toolbar;
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private CardFragmentPagerAdapter mFragmentCardAdapter;
    private String _customerId, reg_mode;

    List<ClsPackageList> lstClsPackageLists;



    private String _resCustomerStatus;
    private String _resLicenseType;
    private String _mobileNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_plans);
        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "DisplayPlansActivity"));
        }

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mViewPager = findViewById(R.id.viewPager);

        _mobileNo = getIntent().getStringExtra("_mobileNo");
        reg_mode = getIntent().getStringExtra("reg_mode");
        _customerId = getIntent().getStringExtra("_customerId");
        _resCustomerStatus = getIntent().getStringExtra("_resCustomerStatus");
        _resLicenseType = getIntent().getStringExtra("_resLicenseType");

        Log.e("--URL--", "ActivityDisplayPlans: " + _customerId);
        Log.e("--URL--", "ActivityDisplayPlans: " + reg_mode);

        ClsPackageParameter obj = new ClsPackageParameter();
        obj.setProductName(ClsGlobal.AppName);
        RetrofitPackage(obj);

    }


    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }


    private void RetrofitPackage(final ClsPackageParameter objClsPackageParameter) {

        InterfacePackage interfacePackage =
                ApiClient.getRetrofitInstance().create(InterfacePackage.class);

        Call<ClsPackageParameter> objClsPackageParameterCall =
                interfacePackage.value(objClsPackageParameter.getProductName(), reg_mode);

        Log.e("--URL--", "HHTRequestReport: " + objClsPackageParameterCall.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(DisplayPlansActivity.this, "Loading...", true);
        pd.show();

        objClsPackageParameterCall.enqueue(new Callback<ClsPackageParameter>() {
            @Override
            public void onResponse(Call<ClsPackageParameter> call, Response<ClsPackageParameter> response) {

                pd.dismiss();
                if (response.body() != null) {
                    Log.e("--URL--", "Step: " + response.body());
                    Log.e("--URL--", "Step 1");


                    lstClsPackageLists = new ArrayList<ClsPackageList>();
                    Log.e("--URL--", "Step 2");

                    lstClsPackageLists = response.body().getData();
                    Log.e("--URL--", "Step 3");

                    if (lstClsPackageLists != null && lstClsPackageLists.size() != 0) {
                        Log.e("--URL--", "Step 4");
                        mCardAdapter = new CardPagerAdapter(DisplayPlansActivity.this);

                        Log.e("--URL--", "Step 5");
                        for (ClsPackageList objClsPackageList : lstClsPackageLists) {

                            Log.e("--URL--", "Step 6");

                            objClsPackageList.set_customerId(_customerId);

                            Log.e("--URL--", "Step 7");

                            objClsPackageList.setReg_mode(reg_mode);
                            Log.e("--URL--", "Step 8");
                            mCardAdapter.addCardItem(objClsPackageList);
                            Log.e("--URL--", "Step 9");
                            mFragmentCardAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(),
                                    dpToPixels(2, DisplayPlansActivity.this));
                            Log.e("--URL--", "Step 10");
                            mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
                            Log.e("--URL--", "Step 11");
                            mViewPager.setAdapter(mCardAdapter);
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
            public void onFailure(Call<ClsPackageParameter> call, Throwable t) {
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

}
