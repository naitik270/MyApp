package com.demo.nspl.restaurantlite.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.nspl.restaurantlite.R;


public class TrialActivity extends AppCompatActivity {

    Toolbar toolbar;
    private Button buttonShow;
    private SharedPreferences mPreferences;
    private static final String mPreferncesLicenceVerificationFile = "MyPerfernces";
    private static final String BASE_URL = "http://192.168.1.200/services/appservices.asmx/";
    private static final String BASE_URL1 = "http://192.168.1.200/services/appservices.asmx/";
    String mailBodyStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);

        toolbar = findViewById(R.id.toolbar);
        setTitle("Demo");
        setSupportActionBar(toolbar);

        mPreferences = getSharedPreferences(mPreferncesLicenceVerificationFile, MODE_PRIVATE);
       // buttonShow = findViewById(R.id.buttonShow);
        String getstatus = mPreferences.getString("Status1", "No Task Perform");
        Log.e("Expiry_Date", String.valueOf(getstatus));


        //     SharedPreferences mPreferences = getApplicationContext().getSharedPreferences("Email_Settings", MODE_PRIVATE);

//        String getListOfEmail1 = mPreferences.getString("EmailList", null);
//        ArrayList<String> getCompleteEmailList = new Gson().fromJson(getListOfEmail1,
//                new TypeToken<ArrayList<String>>() {
//                }.getType());


//        buttonShow.setOnClickListener(view -> {
////            Intent intent = new Intent(this,ActivityPaymentProcess.class);
////            startActivity(intent);
//
////            FragmentManager fragmentManager = getSupportFragmentManager();
////            FilterDialogFragment newFragment = new FilterDialogFragment();
////            newFragment.setRequestCode(DIALOG_QUEST_CODE);
////            FragmentTransaction transaction = fragmentManager.beginTransaction();
////            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
////            transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
////            newFragment.setOnCallbackResult(new FilterDialogFragment.CallbackResult() {
////                @Override
////                public void sendResult(int requestCode) {
//////                    if (requestCode == DIALOG_QUEST_CODE) {
//////                        displayDataResult((Event) obj);
//////                    }
////                }
////            });
//
//       //     buttonShow.setVisibility(View.INVISIBLE);
//
//         //   Toast.makeText(this,getstatus,Toast.LENGTH_SHORT).show();
//
//   //         Toast.makeText(this,String.valueOf(ClsOrder.getIncome(TrialActivity.this)),Toast.LENGTH_SHORT).show();
//
//
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        buttonShow.setVisibility(View.VISIBLE);

    }
}
