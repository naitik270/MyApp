package com.demo.nspl.restaurantlite.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.work.WorkManager;

import com.demo.nspl.restaurantlite.AsyncTask.BackUpAsyncTask;
import com.demo.nspl.restaurantlite.AsyncTask.BackUpCloudAsyncTask;
import com.demo.nspl.restaurantlite.AsyncTask.GDriveBackUpAsyncTask;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.DriveServiceHelper;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.backGroundTask.BackupLogsActivity;
import com.demo.nspl.restaurantlite.classes.ClsUserInfo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.util.ArrayList;
import java.util.Collection;

public class BackUpAndRestoreActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private static final String mPreferncesFileName = "AutoBackUpSettings";

    private Toolbar toolbar;
    RadioGroup rg_cloud, rg_over, rg_manually;
    RadioButton rb_daily, rb_weekly, rb_monthly, rb_yearly, rb_never;
    RadioButton rb_wifi, rb_cellular;
    private static final int REQUEST_CODE_SIGN_IN = 1;
    GoogleAccountCredential credential;
    Button btnSave, btn_backup_to_cloud, btn_backUp_Now_local,btn_google_backup;
    String selectedValue = "";

    String getSelectedTime_From_SharedPreference = "", getNetType = "";
    String _customerId = "";
    ClsUserInfo loginStatus;
    String LicenseType = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_up_and_restore);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPreferences = getSharedPreferences(mPreferncesFileName, MODE_PRIVATE);

        rg_cloud = findViewById(R.id.rg_cloud);
        rg_over = findViewById(R.id.rg_over);

        rb_daily = findViewById(R.id.rb_daily);
        rb_weekly = findViewById(R.id.rb_weekly);
        rb_monthly = findViewById(R.id.rb_monthly);
        rb_yearly = findViewById(R.id.rb_yearly);
        rb_never = findViewById(R.id.rb_never);
        btn_backup_to_cloud = findViewById(R.id.btn_backup_to_cloud);
        btn_google_backup = findViewById(R.id.btn_google_backup);


        LicenseType = getIntent().getStringExtra("LicenseType");

/*
        if (LicenseType != null && LicenseType.equalsIgnoreCase("Free")) {
            btn_backup_to_cloud.setVisibility(View.GONE);
        } else {
            btn_backup_to_cloud.setVisibility(View.VISIBLE);
        }
*/


        btn_backUp_Now_local = findViewById(R.id.btn_backUp_Now_local);

        rb_wifi = findViewById(R.id.rb_wifi);
        rb_cellular = findViewById(R.id.rb_cellular);

        btnSave = findViewById(R.id.btnSave);

        loginStatus = ClsGlobal.getUserInfo(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        _customerId = getIntent().getStringExtra("_customerId");

        _customerId = loginStatus.getUserId();
        Log.e("getNetType", _customerId);


        Log.e("getNetType", "getCinnumber:-" + loginStatus.getUserId());

        getNetType = mPreferences.getString("NetworkType", null);


        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(24);


        if (loginStatus.getLoginStatus().equalsIgnoreCase("DEACTIVE")) {
            if (ClsGlobal.AppPackageName.equalsIgnoreCase("com.demo.nspl.retailpos_demo")) {
                Intent i = new Intent(this, LoginActivityDemo.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            } else {
                Intent i = new Intent(this, LogInActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        }


        getSelectedTime_From_SharedPreference = mPreferences.getString("AutoBackUpTime", null);


        if (getSelectedTime_From_SharedPreference != null
                && !getSelectedTime_From_SharedPreference.equalsIgnoreCase("")) {

            switch (getSelectedTime_From_SharedPreference) {

                case "Daily":
                    rb_daily.setChecked(true);
                    break;

                case "Weekly":
                    rb_weekly.setChecked(true);
                    break;

                case "Monthly":
                    rb_monthly.setChecked(true);
                    break;

                case "Yearly":
                    rb_yearly.setChecked(true);
                    break;

                case "Never":
                    rb_never.setChecked(true);
                    break;
            }

        }

        if (getNetType != null
                && !getNetType.equalsIgnoreCase("")) {

            switch (getNetType) {

                case "WI-FI":
                    rb_wifi.setChecked(true);
                    break;

                case "WI-FI OR CELLULAR":
                    rb_cellular.setChecked(true);
                    break;
            }
        }

        buttonClick();

    }


    void buttonClick() {

        btnSave.setOnClickListener(v -> {

            if (rb_daily.isChecked()) {
                selectedValue = rb_daily.getText().toString();
                ClsGlobal.SetAutoBackUp(1, "REPLACE");
            } else if (rb_weekly.isChecked()) {
                selectedValue = rb_weekly.getText().toString();
                ClsGlobal.SetAutoBackUp(7, "REPLACE");
            } else if (rb_monthly.isChecked()) {
                selectedValue = rb_monthly.getText().toString();
                ClsGlobal.SetAutoBackUp(30, "REPLACE");
            } else if (rb_yearly.isChecked()) {
                selectedValue = rb_yearly.getText().toString();
                ClsGlobal.SetAutoBackUp(365, "REPLACE");
            } else if (rb_never.isChecked()) {
                selectedValue = rb_never.getText().toString();
                WorkManager.getInstance().cancelUniqueWork(ClsGlobal.AppPackageName.concat("AutoBackUp"));
            }

            SaveValuesToSharedPreferences("AutoBackUpTime", selectedValue);


            if (rb_wifi.isChecked()) {
                selectedValue = rb_wifi.getText().toString();
            } else if (rb_cellular.isChecked()) {
                selectedValue = rb_cellular.getText().toString();
            }


            Log.e("selectedValue", selectedValue);
            SaveValuesToSharedPreferences("NetworkType", selectedValue);

            Toast.makeText(BackUpAndRestoreActivity.this,
                    "Backup setting saved successfully.", Toast.LENGTH_SHORT).show();
            finish();

        });

        btn_backup_to_cloud.setOnClickListener(v -> {

            new BackUpCloudAsyncTask(this, _customerId).execute();

        });

        btn_backUp_Now_local.setOnClickListener(v -> {
            new BackUpAsyncTask(this).execute();

        });


        btn_google_backup.setOnClickListener(v -> {
            Log.e("Check","btn_google_backup call");
            if (DriveServiceHelper.isSignedIn(BackUpAndRestoreActivity.this)){
                Log.e("Check","DriveServiceHelper.isSignedIn");
                new GDriveBackUpAsyncTask(this).execute();
            }else {
                Log.e("Check","else _firstCheckLogin");
                _firstCheckLogin();

            }


        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_backup, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        if (id == R.id.Backup_Logs) {

            Intent intent = new Intent(this, BackupLogsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void SaveValuesToSharedPreferences(String key, String value) {
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString(key, value);
        preferencesEditor.apply();
    }


    void _firstCheckLogin() {
        Log.e("Check","_firstCheckLogin call");
        if (DriveServiceHelper.isSignedIn(BackUpAndRestoreActivity.this)) {
            Log.e("Check","_firstCheckLogin isSignedIn");
            GoogleSignInAccount googleSignIn =
                    GoogleSignIn.getLastSignedInAccount(BackUpAndRestoreActivity.this);

            if (googleSignIn != null) {


                Collection<String> scopes = new ArrayList<>();
                scopes.add(DriveScopes.DRIVE);
                scopes.add(DriveScopes.DRIVE_FILE);
//                scopes.add(DriveScopes.DRIVE_APPDATA);
//                scopes.add(DriveScopes.DRIVE_SCRIPTS);
                scopes.add(DriveScopes.DRIVE_METADATA);

                credential =
                        GoogleAccountCredential.usingOAuth2(
                                BackUpAndRestoreActivity.this, scopes);
                credential.setSelectedAccount(googleSignIn.getAccount());
                Drive googleDriveService =
                        new Drive.Builder(
                                AndroidHttp.newCompatibleTransport(),
                                new GsonFactory(),
                                credential)
                                .setApplicationName(ClsGlobal.AppName)
                                .build();
            }
        } else {

            Log.e("Check","_firstCheckLogin requestSignIn");
            requestSignIn();

        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN:
                if (resultCode == RESULT_OK && resultData != null) {
                    handleSignInResult(resultData);
                }
                break;
        }

    }


    /**
     * Handles the {@code result} of a completed sign-in activity initiated from {@link
     * #requestSignIn()}.
     */
    private void handleSignInResult(Intent result) {
        Log.e("Check","handleSignInResult call");
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener(googleAccount -> {


                    Collection<String> scopes = new ArrayList<>();
                    scopes.add(DriveScopes.DRIVE);
                    scopes.add(DriveScopes.DRIVE_FILE);
//                    scopes.add(DriveScopes.DRIVE_APPDATA);
//                    scopes.add(DriveScopes.DRIVE_SCRIPTS);
                    scopes.add(DriveScopes.DRIVE_METADATA);


                    // Use the authenticated account to sign in to the Drive service.
                    credential =
                            GoogleAccountCredential.usingOAuth2(
                                    BackUpAndRestoreActivity.this, scopes);

                    credential.setSelectedAccount(googleAccount.getAccount());
                    Drive googleDriveService =
                            new Drive.Builder(
                                    AndroidHttp.newCompatibleTransport(),
                                    new GsonFactory(),
                                    credential)
                                    .setApplicationName(ClsGlobal.AppName)
                                    .build();


                    // The DriveServiceHelper encapsulates all REST API and SAF functionality.
                    // Its instantiation is required before handling any onClick actions.
//                    mDriveServiceHelper = new DriveServiceHelper(googleDriveService);
                    Log.e("Check","addOnSuccessListener call");
//                    new GDriveBackUpAsyncTask(this).execute();
                })

                .addOnFailureListener(exception ->
                        Log.e("--TEST--", "Unable to sign in.", exception));

        Log.e("Check","addOnSuccessListener end");
    }


    /**
     * Starts a sign-in activity using {@link #REQUEST_CODE_SIGN_IN}.
     */
    private void requestSignIn() {

        Log.d("requestSignIn", "Requesting sign-in");
        Log.e("Check","requestSignIn call");
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestScopes(new Scope(DriveScopes.DRIVE),
                                new Scope(DriveScopes.DRIVE_FILE),
//                                new Scope(DriveScopes.DRIVE_APPDATA),
                                new Scope(DriveScopes.DRIVE_METADATA)
                        )
                        .build();
        GoogleSignInClient client = GoogleSignIn.getClient(BackUpAndRestoreActivity.this, signInOptions);

        // The result of the sign-in Intent is handled in onActivityResult.
        startActivityForResult(client.getSignInIntent(), REQUEST_CODE_SIGN_IN);

        Thread thread = new Thread(() -> {
            DriveServiceHelper driveServiceHelper = new DriveServiceHelper("");
            driveServiceHelper.Initialize_DriveServiceHelper(BackUpAndRestoreActivity.this);
            driveServiceHelper.Create_if_Folder_Not_exists(BackUpAndRestoreActivity.this);
        });

        thread.start();
    }
}
