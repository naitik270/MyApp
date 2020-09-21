package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.work.PeriodicWorkRequest;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ApiClient;
import com.demo.nspl.restaurantlite.Global.AppLocationService;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.ClsPermission;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsCustomerFreeLicenseUpdation;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsLoginParams;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsLoginResponseList;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceCustomerFreeLicenseUpdation;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceLogin;
import com.demo.nspl.restaurantlite.backGroundTask.DbBackUpTask;
import com.demo.nspl.restaurantlite.classes.ClsDialog;
import com.demo.nspl.restaurantlite.classes.ClsExpenseType;
import com.demo.nspl.restaurantlite.classes.ClsTaxSlab;
import com.demo.nspl.restaurantlite.classes.ClsUnit;
import com.demo.nspl.restaurantlite.classes.ClsUserInfo;
import com.demo.nspl.restaurantlite.classes.ClsVendor;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import io.requery.android.database.sqlite.SQLiteDatabase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.BackUpDbFile;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.Create_OneTimeWorkRequest;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.LOCASION_PERMISSIONS;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.REQUEST_APP_UPDATE;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.ShowBkp_Remainder;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.UploadErrorLogs_BackupLogs;
import static com.demo.nspl.restaurantlite.Global.ClsPermission.REQUEST_READ_PHONE_STATE;

public class LogInActivity extends AppCompatActivity implements InstallStateUpdatedListener {

    TextInputLayout input_user_id, input_password, input_email;
    EditText edt_mobile, edt_password, edt_email;
    TextView forgot_password, txt_help;
    CheckBox show_hide_password, chk_remember;
    Button btn_login;
    TextView txt_createAccount;
    TextView txt_version;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    String username, password;
    AppLocationService appLocationService;
    private io.requery.android.database.sqlite.SQLiteDatabase db;
    private android.database.sqlite.SQLiteDatabase db1;
    private int result;
    private int StrRandom;

    //ClsUserInfo objClsUserInfo = new ClsUserInfo();
    SharedPreferences pref;
    ClsUserInfo objClsUserInfoOld = new ClsUserInfo();
    private static final String SHARED_PREF_NAME = "mysharedpref";
    private static final String KEY_NAME = "keyname";
    ClsLoginResponseList objUser = new ClsLoginResponseList();

    private SharedPreferences mPreferences;
    private static final String mPreferncesName = "MyPerfernces";


    private static final String mPreferncesFileName = "AutoBackUpSettings";
    private SharedPreferences mPreferencesAutoBackUp;

    PeriodicWorkRequest periodicWorkRequest;
    private AppUpdateManager appUpdateManager;

    TextView txt_faq;
    View vw_email;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        getWindow().setStatusBarColor(ContextCompat.getColor(LogInActivity.this, R.color.colorPrimaryDark));
        ClsPermission.checkpermission(LogInActivity.this);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "LogInActivity"));
        }


        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        mPreferences = getSharedPreferences(mPreferncesName, MODE_PRIVATE);


        appUpdateManager = AppUpdateManagerFactory.create(LogInActivity.this);
        appUpdateManager.registerListener(this);
        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // For a flexible update, use AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.

                // Checks that the platform will allow the specified type of update.
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    // Request the update.
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo,
                                AppUpdateType.IMMEDIATE,
                                this,
                                REQUEST_APP_UPDATE);

                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate();
                } else {
                    Log.e("Login", "checkForAppUpdateAvailability: something else");
                }
            }
        });

        appLocationService = new AppLocationService(
                LogInActivity.this);

        ClsGlobal.SetUpAutoLocalBkp();

        main();

        input_email.setTag("0");
        edt_email.setText("");


        create_database();

        objClsUserInfoOld = ClsGlobal.getUserInfo(LogInActivity.this);
        Log.d("UserInfo", "----UserInfo---" + objClsUserInfoOld.getUserId());


        if (objClsUserInfoOld.getLoginStatus() == null || objClsUserInfoOld.getLoginStatus().isEmpty()) {
            ClsUserInfo objClsUserInfo = new ClsUserInfo();
            objClsUserInfo.setLoginStatus("DEACTIVE");
            objClsUserInfo.setMobileNo(edt_mobile.getText().toString().trim());
            ClsGlobal.setBasicUserInfo(objClsUserInfo, LogInActivity.this);
        }
        Log.d("UserInfo", "----UserInfo---" + objClsUserInfoOld.getLoginStatus());


    }


    @Override
    protected void onPostResume() {
        create_database();
        super.onPostResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE: {
                ClsPermission.checkPermission(requestCode, permissions, grantResults, LogInActivity.this);
                return;
            }

        }


    }

    @SuppressLint("SetTextI18n")
    private void main() {

        txt_version = findViewById(R.id.txt_version);
        input_user_id = findViewById(R.id.input_user_id);
        input_password = findViewById(R.id.input_password);
        input_email = findViewById(R.id.input_email);

        edt_email = findViewById(R.id.edt_email);
        edt_mobile = findViewById(R.id.edt_mobile);
        txt_help = findViewById(R.id.txt_help);
        txt_help.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        txt_faq = findViewById(R.id.txt_faq);
        txt_faq.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        vw_email = findViewById(R.id.vw_email);


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

                }
            }
        });
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);

        edt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());


        edt_password.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                    (actionId == EditorInfo.IME_ACTION_DONE)) {

                btn_login.performClick();
                return true;

            }
            return false;
        });

        txt_help.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ActivityHelp.class);
            startActivity(intent);

        });

        txt_createAccount = findViewById(R.id.txt_createAccount);
        txt_createAccount.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        txt_createAccount.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
            startActivity(intent);


//                Intent intent =new Intent(LogInActivity.this,TrialActivity.class);
//                startActivity(intent);
        });

        forgot_password = findViewById(R.id.forgot_password);
        forgot_password.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        forgot_password.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ActivityForgotPassword.class);
            startActivity(intent);
        });

        chk_remember = findViewById(R.id.chk_remember);
        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        if (saveLogin == true) {
            edt_mobile.setText(loginPreferences.getString("username", ""));
            edt_password.setText(loginPreferences.getString("password", ""));
            chk_remember.setChecked(true);
        }

        txt_version.setText("v" + ClsGlobal.getApplicationVersion(LogInActivity.this));

        txt_faq.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Faq_Activity.class);
            intent.putExtra("_destinationMode", "Login");
            startActivity(intent);
        });


        // Show backup Remainder Daily.
        ShowBkp_Remainder(LogInActivity.this);

        // Take emergency backup in .ftouchlogPOS folder.
        // we take emergency backup when user open 5 times SHAP_Lite_Activity or LoginActivity.
        BackUpDbFile(LogInActivity.this);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);


        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Log.d("--Timer--", "DONE");
//                        getLatitudeLongitude();
                    }
                });
            }
        }, 2000);


        btn_login.setOnClickListener(view -> {

            // Take emergency backup.
            Create_OneTimeWorkRequest(DbBackUpTask.class,
                    ClsGlobal.AppPackageName.concat(".DbBackUp"),
                    "KEEP", null, null);

            // Upload Error Logs BackupLogs.
            UploadErrorLogs_BackupLogs(LogInActivity.this);


            username = edt_mobile.getText().toString();
            password = edt_password.getText().toString();

            if (chk_remember.isChecked()) {
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.putString("username", username);
                loginPrefsEditor.putString("password", password);
                loginPrefsEditor.commit();
            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
            }

            ClsGlobal.defaultPrinterName = ClsGlobal.GetSharedPreferencesFile
                    ("printerConfig", "pname", LogInActivity.this);

            Log.d("btn_connect", "LoginActivity: " + ClsGlobal.defaultPrinterName);

            Calendar c = Calendar.getInstance();   // this takes current date
            c.set(Calendar.DAY_OF_MONTH, 1);

            String abcd = ClsGlobal.getFirstDateOfMonth(c.getTime());
            Log.d("getFirstDateOfMonth", "getFirstDateOfMonth: " + abcd);

            pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            pref.getString("printername", null);

            if (LoginValidation()) {
                if (ClsGlobal.hasPermissions(LogInActivity.this, LOCASION_PERMISSIONS)) {
//                    getLatitudeLongitude();

                    LoginAPI();
                } else {
                    ClsPermission.checkpermission(LogInActivity.this);
                }
            }


        });
//        ClsCustomerMaster.DeleteAll(LogInActivity.this);

        objClsUserInfoOld = ClsGlobal.getUserInfo(LogInActivity.this);
        Log.d("getFirstDateOfMonth", "----UserInfo---" + objClsUserInfoOld.getLoginStatus());

        if (objClsUserInfoOld.getLoginStatus().equalsIgnoreCase("ACTIVE")) {
            Log.d("getFirstDateOfMonth", "----UserInfo---" + objClsUserInfoOld.getLoginStatus());
            Intent i = new Intent(getApplicationContext(), SHAP_Lite_Activity.class);
            i.putExtra("_licenseType", objClsUserInfoOld.getLicenseType());
            startActivity(i);
//            finish();
        } /*else {
            appUpdateManager = AppUpdateManagerFactory.create(LogInActivity.this);
            appUpdateManager.registerListener(this);
            // Returns an intent object that you use to check for an update.
            Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

            // Checks that the platform will allow the specified type of update.
            appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        // For a flexible update, use AppUpdateType.FLEXIBLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    // Request the update.

                    // Checks that the platform will allow the specified type of update.
                    if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        // Request the update.
                        try {
                            appUpdateManager.startUpdateFlowForResult(
                                    appUpdateInfo,
                                    AppUpdateType.IMMEDIATE,
                                    this,
                                    REQUEST_APP_UPDATE);

                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                        popupSnackbarForCompleteUpdate();
                    } else {
                        Log.e("Login", "checkForAppUpdateAvailability: something else");
                    }
                }
            });
        }*/
    }


    public void showSettingsAlert(String provider) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                LogInActivity.this);
        alertDialog.setTitle(provider + " SETTINGS");
        alertDialog.setMessage(provider + " is not enabled! Want to go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        LogInActivity.this.startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    String address = "";
    Double lati = 0.0, longi = 0.0;


//    private void getLatitudeLongitude() {
//
//        Location nwLocation = appLocationService
//                .getLocation(LocationManager.NETWORK_PROVIDER);
//
//        if (nwLocation != null) {
//            try {
//                lati = nwLocation.getLatitude();
//                longi = nwLocation.getLongitude();
//                Geocoder geocoder;
//                List<Address> addresses;
//                geocoder = new Geocoder(this, Locale.getDefault());
//                addresses = geocoder.getFromLocation(lati, longi, 1);
//                address = addresses.get(0).getAddressLine(0);
//
//
//                Log.d("--Timer--", "address: " + address);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            showSettingsAlert("NETWORK");
//        }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (appUpdateManager != null) {
            appUpdateManager.unregisterListener(this);
        }
    }

    private boolean LoginValidation() {
        boolean result = true;

        if (!ClsGlobal.CheckInternetConnection(LogInActivity.this)) {
            Toast.makeText(LogInActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edt_mobile.getText() == null || edt_mobile.getText().toString().trim().isEmpty()) {
            showCustomDialog(1);
            return false;
        }
        if (edt_password.getText() == null || edt_password.getText().toString().trim().isEmpty()) {
            showCustomDialog(2);
            return false;
        }

        if (!validateUserID()) {
            result = false;
        }

        if (!validatePassword()) {
            result = false;
        }
        if (input_email.getTag().toString() == "1") {
            String email = edt_email.getText().toString().trim();
            if (edt_email.getText() == null || email.isEmpty() || !ClsGlobal.isValidEmail(email)) {
                Toast.makeText(getApplicationContext(), R.string.err_msg_email, Toast.LENGTH_SHORT).show();
                edt_email.requestFocus();
                result = false;
            }
        }

        return result;
    }

    @SuppressLint("SetTextI18n")
    private void showCustomDialog(int value) {
        final Dialog dialog = new Dialog(LogInActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_warning);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ImageView icon = dialog.findViewById(R.id.icon);
        TextView title = dialog.findViewById(R.id.title);
        TextView content = dialog.findViewById(R.id.content);

        if (value == 1) {
            icon.setBackgroundResource(R.drawable.ic_contact_phone);
            title.setText("No Mobile !");
            content.setText("Enter Mobile No.");
            edt_mobile.requestFocus();
        } else if (value == 2) {
            icon.setBackgroundResource(R.drawable.ic_change_password);
            title.setText("No Password !");
            content.setText("Enter Password");
            edt_password.requestFocus();
        }
        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    private boolean validateUserID() {
        if (edt_mobile.getText().toString().trim().isEmpty()) {
            ClsDialog.ErrorDialog("Alert", getString(R.string.err_msg_user_id), LogInActivity.this);

            requestFocus(edt_mobile);
            return false;
        } else {
            input_user_id.setErrorEnabled(false);
        }
        Log.d("TAG", "validateUserID= \"\" ");
        return true;
    }

    private boolean validatePassword() {
        if (edt_password.getText().toString().trim().isEmpty()) {
//            input_password.setError(getString(R.string.err_msg_password));
            ClsDialog.ErrorDialog("Alert", getString(R.string.err_msg_password), LogInActivity.this);

            requestFocus(edt_password);
            return false;
        } else {
            input_password.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.
                    LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private void create_database() {

        Log.e("Check","create_database");


        db = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(
                ClsGlobal.Database_Name).getPath(), null);


//        db1 = openOrCreateDatabase(ClsGlobal.Database_Name, MODE_PRIVATE, null);
//            db.beginTransaction();

        createCategoryMaster();
        createItemMaster();
        createSizeMaster();
        createUnitMaster();
        createEmployeeMaster();
        createVendorMaster();
        createExpenseTypeMaster();
        createExpenseMasterNew();
        createExpenseMaster();
        createTaxMaster();
        createEmployeeDocumentMaster();
        createInventoryItemMaster();
        createInventoryStockMaster();
        createTableMaster();
        createOrderMaster();
        createOrderDetails();
        createTerms();
        createCustomerMaster();
        createEmailLogs();

        //----------------new Tables-------------------//
        createInventoryLayer();
        createLayerItemMaster();
        createItemTag();
        createItemLayer();
        createTaxSlab();
        createInventoryOrderDetail();
        createInventoryOrderMaster();

        createPaymentMaster();
        createPurchaseMaster();
        createPurchaseDetail();
        createCommonLogsMaster();

        //----------------new merge-------------------//
        createMultipleImgSave();
        createSmsCustomerGroup();
        createSMSCustomerGroupDetail();
        importCustomerDataToExcel();
        createOrder_Sequence();
        createSmsIdSetting();

        createMessageFormatMaster();
        createSMSBulkMasterMaster();
        createSMSLog();
        createSalesSMSLogsMaster();
        createQuotationMaster();
        createQuotationDetail();

        createActivityLog();

        createSaveImage();

        // after mearge.
        createFeatures();
        createServices();
        createServiceImages();
        createServiceFeatures();
        createEmployeeServiceSetting();
        createCombo();
        createComboService();

//            if (db.isOpen()){
//                db.setTransactionSuccessful();
//                db.endTransaction();
//                db.close();
//            }

//        if (db1.isOpen()) {
//            db1.close();
//        }

        if (db.isOpen()) {
            db.close();
        }


    }

    public void createFeatures() {
        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[Features]")
                .concat("(")
                .concat("[FeatureId] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[FeatureName] VARCHAR(100),")
                .concat("[Description] VARCHAR(200)")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "CATEGORY_MASTER---->>>" + qry);
        db.execSQL(qry);

    }

    public void createComboService() {
        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[ComboService]")
                .concat("(")
                .concat("[ComboServiceId] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[ComboId] INT,")
                .concat("[ServiceId] INT")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "CATEGORY_MASTER---->>>" + qry);
        db.execSQL(qry);
    }


    public void createCombo() {
        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[Combo]")
                .concat("(")
                .concat("[ComboId] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[ComboName] Varchar(150),")
                .concat("[ComboCode] Varchar(20),")
                .concat("[Rate] DOUBLE,")
                .concat("[ApplyTax] Varchar(3),")
                .concat("[TaxSlabId] INT,")
                .concat("[TaxType] Varchar(15),")
                .concat("[Active] Varchar(3),")
                .concat("[Description] Varchar(200),")
                .concat("[ValidUptoDate] Datetime")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "CATEGORY_MASTER---->>>" + qry);
        db.execSQL(qry);
    }

    public void createEmployeeServiceSetting() {
        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[EmployeeServiceSetting]")
                .concat("(")
                .concat("[EmployeeServiceId] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[ServiceId] INT,")
                .concat("[EmployeeId] INT,")
                .concat("[SetCommission] Varchar(3),")
                .concat("[CommissionPercentage] DOUBLE,")
                .concat("[CommissionAmount] DOUBLE,")
                .concat("[Description] Varchar(200)")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "CATEGORY_MASTER---->>>" + qry);
        db.execSQL(qry);
    }

    public void createServices() {
        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[Services]")
                .concat("(")
                .concat("[ServiceId] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[ServiceName] VARCHAR(100),")
                .concat("[ServiceCode] VARCHAR(20),")
                .concat("[ServiceType] VARCHAR(10),")
                .concat("[Rate] DOUBLE,")
                .concat("[ApplyTax] VARCHAR(3),")
                .concat("[TaxSlabId] INT,")
                .concat("[TaxType] Varchar(15),")
                .concat("[Active] Varchar(3),")
                .concat("[Description] Varchar(200),")
                .concat("[ApproxServiceMinute] INT,")
                .concat("[AvailableDay] Varchar(200)")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "CATEGORY_MASTER---->>>" + qry);
        db.execSQL(qry);
    }

    public void createServiceFeatures() {
        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[ServiceFeatures]")
                .concat("(")
                .concat("[ServiceFeatureId] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[ServiceId] INT,")
                .concat("[FeatureId] INT")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "CATEGORY_MASTER---->>>" + qry);
        db.execSQL(qry);
    }

    public void createServiceImages() {
        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[ServiceImages]")
                .concat("(")
                .concat("[ServiceImageId] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[ServiceId] INT,")
                .concat("[DisplayOrder] INT,")
                .concat("[FilePath] Varchar(500),")
                .concat("[FileName] Varchar(100)")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "CATEGORY_MASTER---->>>" + qry);
        db.execSQL(qry);
    }

    private void createSaveImage() {


        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[Images]")
                .concat("(")
                .concat("[ImageId] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[DisplayOrder] INTEGER")
                .concat(",[FilePath] varchar(500)")
                .concat(",[FileName] varchar(100)")
                .concat(",[UniqueId] varchar(100)")
                .concat(",[Type] varchar(100)")   //Payment
                .concat(",[FileType] varchar(100)") //Image/Pdf/Excel/etc..
                .concat(",[Extension] varchar(15)") //.jpg/.pdf/etc
                .concat(",[Notes] varchar(500)")
                .concat(",[entryDate] datetime")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " CommonLogs_Master:- " + qry);

    }


    private void createActivityLog() {

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[ActivityLog]")
                .concat("(")
                .concat("[logId] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[action] varchar(50)")
                .concat(",[entryDate] datetime")
                .concat(",[user] varchar(80) ")
                .concat(",[description] varchar(5000)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " CommonLogs_Master:- " + qry);

    }


    private void createSalesSMSLogsMaster() {

//        String qry1 = "ALTER TABLE [SalesSMSLogsMaster] ADD [UtilizeType] VARCHAR(10)";
//        db.execSQL(qry1);

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[SalesSMSLogsMaster]")
                .concat("(")
                .concat("[LogId] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[orderId] INTEGER")
                .concat(",[orderNo] VARCHAR(50)")
                .concat(",[mobileNo] VARCHAR(50) ")
                .concat(",[Customer_Name] VARCHAR(500)")
                .concat(",[Entry_Datetime] DATETIME")
                .concat(",[message] VARCHAR(5000)")
                .concat(",[invoice_attachment] VARCHAR(3)")
                .concat(",[Status] VARCHAR(50)")
                .concat(",[UtilizeType] VARCHAR(10)")
                .concat(",[SmsStatus] VARCHAR(50)")
                .concat(",[Type] VARCHAR(50)")
                .concat(",[Credit] INTEGER")
                .concat(",[SendSMSID] VARCHAR(50)")
                .concat(",[Remark] VARCHAR(200)")
                .concat(")")
                .concat(";");


        db.execSQL(qry);
        Log.e("CREATETABLE", " CommonLogs_Master:- " + qry);

    }

    private void createSMSBulkMasterMaster() {

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[SMSBulkMaster]")
                .concat("(")
                .concat("[bulkID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[serverBulkID] VARCHAR(30)")
                .concat(",[Message] VARCHAR(5000)")
                .concat(",[MessageLength] INTEGER ")
                .concat(",[GroupName] VARCHAR(500)")
                .concat(",[TotalCustomers] INTEGER")
                .concat(",[SendDate] DATETIME")
                .concat(",[SenderID] varchar(6)")
                .concat(",[MessageType] varchar(30)")
                .concat(",[Title] varchar(200)")
//                .concat(",[Remark] varchar(200)")
                .concat(")")
                .concat(";");


        db.execSQL(qry);
        Log.e("CREATETABLE", " CommonLogs_Master:- " + qry);

    }

    private void createQuotationMaster() {

//        db.execSQL("DROP TABLE [QuotationMaster]");


//        String qry =  "CREATE TABLE IF NOT EXISTS"
//                .concat("[QuotationMaster]")
//                .concat("(")
//                .concat("[QuotationID] INTEGER PRIMARY KEY AUTOINCREMENT")
//                .concat(",[MobileNo] VARCHAR(15)")
//                .concat(",[CustomerName] VARCHAR(100)")
//                .concat(",[CompanyName] VARCHAR(100)")
//                .concat(",[cust_address] VARCHAR(100)")
//                .concat(",[cust_email] VARCHAR(100)")
//                .concat(",[GSTNo] VARCHAR(50)")
//                .concat(",[Status] VARCHAR(50)")
//                .concat(",[QuotationDate] DATETIME")
//                .concat(",[ValidUptoDate] DATETIME")
//                .concat(",[QuotationNo] VARCHAR(25)")
//                .concat(",[QuotationType] VARCHAR(15)") // Sale & WholeSale
//                .concat(",[TotalAmount] DOUBLE")   //1050
//                .concat(",[DiscountAmount] DOUBLE") //50
//                .concat(",[TotalTaxAmount] DOUBLE") //50
//                .concat(",[GrandTotal] DOUBLE") //50
//                .concat(",[EntryDate] DATETIME")
//                .concat(",[ApplyTax] VARCHAR(3)")
//                .concat(",[Sms_Limit] INTEGER")
//                .concat(")")
//                .concat(";");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[QuotationMaster]")
                .concat("(")
                .concat("[QuotationID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[MobileNo] VARCHAR(15)")
                .concat(",[CustomerName] VARCHAR(100)")
                .concat(",[CompanyName] VARCHAR(100)")
                .concat(",[cust_address] VARCHAR(100)")
                .concat(",[cust_email] VARCHAR(100)")
                .concat(",[GSTNo] VARCHAR(50)")
                .concat(",[Status] VARCHAR(50)")
                .concat(",[QuotationDate] DATETIME")
                .concat(",[ValidUptoDate] DATETIME")
                .concat(",[QuotationNo] VARCHAR(25)")
                .concat(",[QuotationType] VARCHAR(15)") // Sale & WholeSale
                .concat(",[TotalAmount] DOUBLE")   //1050
                .concat(",[DiscountAmount] DOUBLE") //50
                .concat(",[TotalTaxAmount] DOUBLE") //50
                .concat(",[GrandTotal] DOUBLE") //50
                .concat(",[EntryDate] DATETIME")
                .concat(",[ApplyTax] VARCHAR(3)")
                .concat(",[Sms_Limit] INTEGER")
                .concat(")")
                .concat(";");


        db.execSQL(qry);
        Log.e("CREATETABLE", " tbl_Tax_Slab " + qry);
    }

    private void createQuotationDetail() {
//        db.execSQL("DROP TABLE [QuotationDetail]");

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[QuotationDetail]")
                .concat("(")
                .concat("[QuotationDetailID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[QuotationID] INTEGER")
                .concat(",[QuotationNo] VARCHAR(25)")
                .concat(",[SaveStatus] VARCHAR(5)")
                .concat(",[ItemCode] VARCHAR(50)")
                .concat(",[Item] VARCHAR(100)")
                .concat(",[Unit] VARCHAR(50)")
                .concat(",[ItemComment] VARCHAR(200)")
                .concat(",[Rate] DOUBLE")
                .concat(",[SaleRate] DOUBLE")
                .concat(",[SaleRateWithoutTax] DOUBLE")
                .concat(",[Quantity] DOUBLE")
                .concat(",[Discount_per] DOUBLE")
                .concat(",[Discount_amt] DOUBLE")
                .concat(",[Amount] DOUBLE")
                .concat(",[CGST] DOUBLE")
                .concat(",[SGST] DOUBLE")
                .concat(",[IGST] DOUBLE")
                .concat(",[TotalTaxAmount] DOUBLE")
                .concat(",[GrandTotal] DOUBLE")
                .concat(")")
                .concat(";");


        db.execSQL(qry);
        Log.e("CREATETABLE", " tbl_Tax_Slab " + qry);

    }


    private void createSMSLog() {

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[SMSLog]")
                .concat("(")
                .concat("[logID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[bulkID] INTEGER")
                .concat(",[Mobile] varchar(10)")
                .concat(",[CustomerName] VARCHAR(150)")
                .concat(",[CreditCount] INTEGER ")
                .concat(",[Status] VARCHAR(100)")
                .concat(",[Message] VARCHAR(5000)")
                .concat(",[StatusDateTime] DATETIME")
                .concat(",[StatusCode] INTEGER")
                .concat(",[serverBulkID] VARCHAR(100)")
                .concat(",[Remark] VARCHAR(200)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " CommonLogs_Master:- " + qry);

    }


    private void createMessageFormatMaster() {

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[MessageFormat_Master]")
                .concat("(")
                .concat("[MessageFormat_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[Title] VARCHAR(2000)")
                .concat(",[MessageFormat] VARCHAR(10000)")
                .concat(",[Type] VARCHAR(20)") // Sale,purches,offer.
                .concat(",[Default] VARCHAR(3)") // YES,NO.
                .concat(",[Remark] VARCHAR(200)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " CommonLogs_Master:- " + qry);

    }

    private void createCommonLogsMaster() {

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[CommonLogs_Master]")
                .concat("(")
                .concat("[CommonLogs_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[Remark] VARCHAR(500)")
                .concat(",[Status] VARCHAR(50)")
                .concat(",[Log_Type] VARCHAR(80)")
                .concat(",[Date_Time] datetime")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " CommonLogs_Master:- " + qry);

    }

    private void createPurchaseDetail() {

        //  db.execSQL("DROP TABLE [tbl_ItemTag]");

      /*  String qry = "ALTER TABLE [PurchaseDetail] ADD [UtilizeType] VARCHAR(10)";
        db.execSQL(qry);
*/
        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[PurchaseDetail]")
                .concat("(")
                .concat("[PurchaseDetailID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[PurchaseID] INT")
                .concat(",[ItemID] INT")
                .concat(",[MonthYear] VARCHAR(10)")
                .concat(",[ItemCode] VARCHAR(20)")
                .concat(",[Unit] VARCHAR(50)")
                .concat(",[Quantity] DOUBLE")
                .concat(",[Rate] DOUBLE")
                .concat(",[TotalAmount] DOUBLE")//50*10
                .concat(",[Discount] DOUBLE")
                .concat(",[NetAmount] DOUBLE")
                .concat(",[ApplyTax] varchar(3)")
                .concat(",[CGST] DOUBLE")
                .concat(",[SGST] DOUBLE")
                .concat(",[IGST] DOUBLE")
                .concat(",[TotalTaxAmount] DOUBLE") //Total tax amt
                .concat(",[GrandTotal] DOUBLE") //finalAmt
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " Terms " + qry);
    }

    private void createPurchaseMaster() {

        //  db.execSQL("DROP TABLE [tbl_ItemTag]");
        /*String qry = "ALTER TABLE [PurchaseMaster] ADD [VendorID] INT";

        db.execSQL(qry);*/


//        db.execSQL("ALTER TABLE [PurchaseMaster]  RENAME [PurchaseNo] VARCHAR(100)");
//        db.execSQL("DROP TABLE [PurchaseMaster]  [PurchaseNo] VARCHAR(100)");

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[PurchaseMaster]")
                .concat("(")
                .concat("[PurchaseID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[PurchaseNo] INT")
                .concat(",[VendorID] INT")
                .concat(",[BillNO] VARCHAR(30)")
                .concat(",[PurchaseDate] DATE")
                .concat(",[Remark] VARCHAR(300)")
                .concat(",[EntryDate] DATETIME")
                .concat(",[Sms_Limit] INTEGER")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " Terms " + qry);
    }


    private void createPaymentMaster() {

//        db.execSQL("ALTER TABLE [PaymentMaster] ADD [OrderID] INT");

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[PaymentMaster]")
                .concat("(")
                .concat("[PaymentID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[PaymentDate] DATETIME")
                .concat(",[PaymentMounth] VARCHAR(20)")
                .concat(",[VendorID] INT")
                .concat(",[MobileNo] VARCHAR(20)")
                .concat(",[CustomerName] VARCHAR(100)")
                .concat(",[VendorName] VARCHAR(100)")
                .concat(",[PaymentMode] VARCHAR(20)")
                .concat(",[PaymentDetail] VARCHAR(150)")
                .concat(",[InvoiceNo] VARCHAR(80)")
                .concat(",[Amount] DOUBLE")
                .concat(",[Remark] VARCHAR(500)")
                .concat(",[EntryDate] DATETIME")
                .concat(",[Type] VARCHAR(10)")
                .concat(",[ReceiptNo] VARCHAR(25)")
                .concat(",[Sms_Limit] INTEGER")
                .concat(",[OrderID] INT")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " tbl_Tax_Slab " + qry);
    }

    private void createInventoryOrderDetail() {

//         db.execSQL("ALTER TABLE [InventoryOrderDetail] ADD [Discount_per] DOUBLE");
//         db.execSQL("ALTER TABLE [InventoryOrderDetail] ADD [Unit] VARCHAR(50)");
//
        //  ItemComment

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[InventoryOrderDetail]")
                .concat("(")
                .concat("[OrderDetailID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[OrderID] VARCHAR(25)")
                .concat(",[OrderNo] VARCHAR(25)")
                .concat(",[ItemCode] VARCHAR(50)")
                .concat(",[Item] VARCHAR(100)")
                .concat(",[ItemComment] VARCHAR(200)")
                .concat(",[Rate] DOUBLE")
                .concat(",[SaleRate] DOUBLE")//100
//                .concat(",[Unit] VARCHAR(50)")
                .concat(",[SaleRateWithoutTax] DOUBLE")// 84.55
                .concat(",[Quantity] DOUBLE")
                .concat(",[Discount_per] DOUBLE")
                .concat(",[Discount_amt] DOUBLE")
                .concat(",[Amount] DOUBLE")
                .concat(",[CGST] DOUBLE")
                .concat(",[SGST] DOUBLE")
                .concat(",[IGST] DOUBLE")
                .concat(",[TotalTaxAmount] DOUBLE")//15.25
                .concat(",[GrandTotal] DOUBLE")
                .concat(",[SaveStatus] VARCHAR(3)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " tbl_Tax_Slab " + qry);
    }

    private void createInventoryOrderMaster() {

        // Different_Amount_mode
        // db.execSQL("ALTER TABLE [InventoryOrderMaster] ADD [Different_Amount_mode] VARCHAR(50)");
//        db.execSQL("ALTER TABLE [InventoryOrderMaster] ADD [QuotationId] INTEGER");


        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[InventoryOrderMaster]")
                .concat("(")
                .concat("[OrderID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[MobileNo] VARCHAR(15)")
                .concat(",[CustomerName] VARCHAR(100)")
                .concat(",[CompanyName] VARCHAR(100)")
                .concat(",[GSTNo] VARCHAR(50)")
                .concat(",[BillDate] DATETIME")
                .concat(",[OrderNo] VARCHAR(25)")
                .concat(",[SaleType] VARCHAR(15)") // Sale & WholeSale
                .concat(",[SaleReturnDiscount] DOUBLE")   //1050
                .concat(",[TotalAmount] DOUBLE")   //1050
                .concat(",[DiscountAmount] DOUBLE") //50
                .concat(",[TotalPaybleAmount] DOUBLE") //1000
                .concat(",[TotalTaxAmount] DOUBLE")   // 100
                .concat(",[TotalReceiveableAmount] DOUBLE")   // 1100
                .concat(",[PaidAmount] DOUBLE") //1050
                .concat(",[AdjumentAmount] DOUBLE") //50
                .concat(",[EntryDate] DATETIME")
                .concat(",[ApplyTax] VARCHAR(3)")
                .concat(",[PaymentMode] VARCHAR(20)")
                .concat(",[PaymentDetail] VARCHAR(50)")
                .concat(",[Different_Amount_mode] VARCHAR(50)")
                .concat(",[BillTo] VARCHAR(8)")
                .concat(",[Sms_Limit] INTEGER")
                .concat(",[QuotationId] INTEGER")

                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " tbl_Tax_Slab " + qry);

    }

    private void createTaxSlab() {
        // db.execSQL("DROP TABLE [tbl_Tax_Slab]");


        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[tbl_Tax_Slab]")
                .concat("(")
                .concat("[SLAB_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[SLAB_NAME] VARCHAR(100)")
                .concat(",[SGST] DOUBLE")
                .concat(",[CGST] DOUBLE")
                .concat(",[IGST] DOUBLE")
                .concat(",[ACTIVE] VARCHAR (3)")
                .concat(",[REMARK] VARCHAR(100)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);

        boolean exists;
        ClsTaxSlab objClsUnitMaster = new ClsTaxSlab(LogInActivity.this);

        String where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("18.0% IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("18.0% IGST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(18.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("18.0% IGST");
            ClsTaxSlab.Insert(LogInActivity.this, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("0% IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("0% IGST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("0% IGST");
            ClsTaxSlab.Insert(LogInActivity.this, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("Out of GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("Out of GST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("Out of GST");
            ClsTaxSlab.Insert(LogInActivity.this, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("0% GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("0% GST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("0% GST");
            ClsTaxSlab.Insert(LogInActivity.this, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("6.0% IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("6.0% IGST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(6.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("6.0% IGST");
            ClsTaxSlab.Insert(LogInActivity.this, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("28.0% IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("28.0% IGST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(28.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("28.0% IGST");
            ClsTaxSlab.Insert(LogInActivity.this, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("28.0% GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("28.0% GST");
            objClsUnitMaster.setSGST(14.0);
            objClsUnitMaster.setCGST(14.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("28.0% GST");
            ClsTaxSlab.Insert(LogInActivity.this, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("12.0% GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("12.0% GST");
            objClsUnitMaster.setSGST(6.0);
            objClsUnitMaster.setCGST(6.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("12.0% GST");
            ClsTaxSlab.Insert(LogInActivity.this, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("18.0% GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("18.0% GST");
            objClsUnitMaster.setSGST(9.0);
            objClsUnitMaster.setCGST(9.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("18.0% GST");
            ClsTaxSlab.Insert(LogInActivity.this, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("3.0% GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("3.0% GST");
            objClsUnitMaster.setSGST(1.5);
            objClsUnitMaster.setCGST(1.5);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("3.0% GST");
            ClsTaxSlab.Insert(LogInActivity.this, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("0.25% IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("3.0% GST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(0.25);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("0.25% IGST");
            ClsTaxSlab.Insert(LogInActivity.this, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("5.0% GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("5.0% GST");
            objClsUnitMaster.setSGST(2.5);
            objClsUnitMaster.setCGST(2.5);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("5.0% GST");
            ClsTaxSlab.Insert(LogInActivity.this, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("6.0% GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("6.0% GST");
            objClsUnitMaster.setSGST(3.0);
            objClsUnitMaster.setCGST(3.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("6.0% GST");
            ClsTaxSlab.Insert(LogInActivity.this, objClsUnitMaster, db);
        }


        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("0.25% GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("0.25% GST");
            objClsUnitMaster.setSGST(0.125);
            objClsUnitMaster.setCGST(0.125);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("0.25% GST");
            ClsTaxSlab.Insert(LogInActivity.this, objClsUnitMaster, db);
        }


        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("Exempt IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("Exempt IGST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("Exempt IGST");
            ClsTaxSlab.Insert(LogInActivity.this, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("3.0% IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("3.0% IGST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(3.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("3.0% IGST");
            ClsTaxSlab.Insert(LogInActivity.this, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("5.0% IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("5.0% IGST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(5.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("5.0% IGST");
            ClsTaxSlab.Insert(LogInActivity.this, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("Exempt GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("Exempt GST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("Exempt GST");
            ClsTaxSlab.Insert(LogInActivity.this, objClsUnitMaster, db);
        }
        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("12.0% IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("12.0% IGST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(12.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("12.0% IGST");
            ClsTaxSlab.Insert(LogInActivity.this, objClsUnitMaster, db);
        }


        Log.e("CREATETABLE", " tbl_Tax_Slab " + qry);

    }

    private void createItemTag() {

        //  db.execSQL("DROP TABLE [tbl_ItemTag]");

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[tbl_ItemTag]")
                .concat("(")
                .concat("[ITEMTAG_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[ITEMID] INT")
                .concat(",[ITEMNAME] VARCHAR(100)")
                .concat(",[TAGNAME] VARCHAR(100)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " Terms " + qry);
    }

    private void createItemLayer() {

        // db.execSQL("DROP TABLE [tbl_ItemLayer]");

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[tbl_ItemLayer]")
                .concat("(")
                .concat("[ITEMLAYER_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[LAYERITEM_ID] INT")//layerID
                .concat(",[LAYER_NAME] VARCHAR(40)")
                .concat(",[ITEM_ID] INT")
                .concat(",[ITEM_NAME] VARCHAR(40)")
                .concat(",[LAYER_VALUE] VARCHAR(100)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " Terms " + qry);
    }


    private void createLayerItemMaster() {

        // db.execSQL("DROP TABLE [tbl_LayerItem_Master]");

      /*  String qryAdd_OPENING_STOCK = "ALTER TABLE [tbl_LayerItem_Master] ADD [WHOLESALE_RATE] DOUBLE";
        db.execSQL(qryAdd_OPENING_STOCK);

        String qryAddTaxType = "ALTER TABLE [tbl_LayerItem_Master] ADD [TAX_TYPE] VARCHAR(10)";
        db.execSQL(qryAddTaxType);*/


        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[tbl_LayerItem_Master]")
                .concat("(")
                .concat("[LAYERITEM_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[ITEM_NAME] VARCHAR(100)")
                .concat(",[ITEM_CODE] VARCHAR(15)")
                .concat(",[RATE_PER_UNIT] DOUBLE")
                .concat(",[WHOLESALE_RATE] DOUBLE")
                .concat(",[TAX_TYPE] VARCHAR(10)")
                .concat(",[REMARK] VARCHAR(500)")
                .concat(",[MIN_STOCK] DOUBLE")
                .concat(",[MAX_STOCK] DOUBLE")
                .concat(",[UNIT_CODE] VARCHAR(20)")
                .concat(",[TAGS]  VARCHAR(500)")
                .concat(",[ACTIVE] VARCHAR (3)")
                .concat(",[DISPLAY_ORDER] INT")
                .concat(",[OPENING_STOCK] DOUBLE")
                .concat(",[HSN_SAC_CODE] VARCHAR(20)")
                .concat(",[TAX_APPLY] VARCHAR (3)")
                .concat(",[TAX_SLAB_ID] INT")
                .concat(",[AUTO_GENERATED_ITEM_CODE] VARCHAR (2500)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);

        Log.e("CREATETABLE", " Terms " + qry);
    }

    private void createInventoryLayer() {
        // db.execSQL("ALTER TABLE [tbl_InventoryLayer] ADD [SELECTED_LAYER_NAME] VARCHAR(40)");
        // db.execSQL("DROP TABLE [tbl_LayerItem_Master]");
        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[tbl_InventoryLayer]")
                .concat("(")
                .concat("[INVENTORYLAYER_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[LAYER_NAME] VARCHAR(40)")
                .concat(",[PARENT_ID] INT")
                .concat(",[SELECTED_LAYER_NAME] VARCHAR(40)")
                .concat(",[DISPLAY_ORDER] INT")
                .concat(",[REMARK] VARCHAR(40)")
                .concat(",[ACTIVE] VARCHAR (3)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " Terms " + qry);
    }

    private void createOrder_Sequence() {

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[OrderSequence]")
                .concat("(")
                .concat("[Order_Sequence_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[OrderNo] INT")
                .concat(",[TaxApplied] VARCHAR(3)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " CommonLogs_Master:- " + qry);

    }

    private void importCustomerDataToExcel() {


        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[tbl_customer_import]")
                .concat("(")
                .concat("[excel_id] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[excel_name] VARCHAR(500)")
                .concat(",[excel_number] VARCHAR(15)")
                .concat(",[excel_email] VARCHAR(100)")
                .concat(",[company_name] VARCHAR(100)")
                .concat(",[gst_no] VARCHAR(20)")
                .concat(",[address] VARCHAR(500)")
                .concat(",[note] VARCHAR(200)")
                .concat(",[status] VARCHAR(50)")
                .concat(",[remark] VARCHAR(200)")
                .concat(")")
                .concat(";");

        Log.d("--uri--", "qry:: " + qry);

        db.execSQL(qry);

    }

    private void createTerms() {

//        db.execSQL("ALTER TABLE [tbl_Terms] ADD [TERM_TYPE] VARCHAR(50)");

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[tbl_Terms]")
                .concat("(")
                .concat("[TERMS_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[INVOICE_TYPES] VARCHAR(20)")
                .concat(",[TERMS] VARCHAR(200)")
                .concat(",[TERM_TYPE] VARCHAR(50)")
                .concat(",[SORT_NO] INT")
                .concat(",[ACTIVE] VARCHAR (3)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " Terms " + qry);
    }


    private void createOrderDetails() {
        //   db.execSQL("DROP TABLE [OrderDetail_master]");
        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[OrderDetail_master]")
                .concat("(")
                .concat("[ORDERDETAIL_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[ORDER_ID] INT")
                .concat(",[ORDER_NO] VARCHAR(6)")
                .concat(",[ITEM_ID] INT")
                .concat(",[ITEM_NAME] VARCHAR (100)")
                .concat(",[RATE] DOUBLE")
                .concat(",[QUANTITY] DOUBLE")
                .concat(",[TOTAL_AMOUNT] DOUBLE")   //rate*qty..
                .concat(",[OTHER_TAX1] VARCHAR(50)")
                .concat(",[OTHER_VAL1] DOUBLE")
                .concat(",[OTHER_TAX2] VARCHAR(50)")
                .concat(",[OTHER_VAL2] DOUBLE")
                .concat(",[OTHER_TAX3] VARCHAR(50)")
                .concat(",[OTHER_VAL3] DOUBLE")
                .concat(",[OTHER_TAX4] VARCHAR(50)")
                .concat(",[OTHER_VAL4] DOUBLE")
                .concat(",[OTHER_TAX5] VARCHAR(50)")
                .concat(",[OTHER_VAL5] DOUBLE")
                .concat(",[TOTAL_TAXAMOUNT] DOUBLE")
                .concat(",[GRAND_TOTAL] DOUBLE")
                .concat(",[TYPE] VARCHAR (10)")        //serve,parcel
                .concat(",[STATUS] VARCHAR (20)")      //cooking,ready to serve etc.
                .concat(",[ENTRYDATETIME] DATETIME")
                .concat(")")
                .concat(";");


        db.execSQL(qry);
        Log.e("CREATETABLE", " orderdetails " + qry);
    }


    private void createEmailLogs() {

        // db.execSQL("DROP TABLE [tbl_EmailLogs]");


        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[tbl_EmailLogs]")
                .concat("(")
                .concat("[EMAILLOGS_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[MESSAGE] VARCHAR(500)")//success:successfully sent, fail:internet connectivity not awailbale,
                .concat(",[DATE_TIME] datetime")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " EmailLogs " + qry);
    }


    private void createCustomerMaster() {


//        db.execSQL("DROP TABLE [CustomerMaster]");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[CustomerMaster]")
                .concat("(")
                .concat("[ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[NAME] VARCHAR (20)")
                .concat(",[Company_Name] VARCHAR (100)")
                .concat(",[GST_NO] VARCHAR (20)")
                .concat(",[Address] VARCHAR (100)")
                .concat(",[MOBILE_NO] VARCHAR (15)")/// Expense Type
                .concat(",[Credit] DOUBLE")/// Expense Type
                .concat(",[OpeningStock] DOUBLE")/// Expense Type
                .concat(",[Email] VARCHAR (100)")/// Expense Type
                .concat(",[BalanceType] VARCHAR (15)")/// Expense Type
                .concat(",[Note] VARCHAR (500)")/// Expense Type
                .concat(",[Save_Contact] VARCHAR (3)")/// Expense Type
                .concat(",[DOB] DateTime")/// Expense Type
                .concat(",[AnniversaryDate] DateTime")/// Expense Type
                .concat(",[PanCard]  VARCHAR (20)")/// Expense Type
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", qry);
    }


    private void createOrderMaster() {

//        db.execSQL("DROP TABLE [Ordermaster]");
        //       db.execSQL("ALTER TABLE [Ordermaster] ADD [GRAND_TOTAL] DOUBLE");

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[Ordermaster]")
                .concat("(")
                .concat("[ORDER_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[ORDER_NO] VARCHAR (6)")//y
                .concat(",[ORDER_DATETIME] DATETIME")//y
                .concat(",[TABLE_ID] INT")//n
                .concat(",[TABLE_NO] VARCHAR (10)")//y
                .concat(",[MOBILE_NO] VARCHAR (15)")//y     //Retail or Table..
                .concat(",[SOURCE] VARCHAR (20)")//y     //Retail or Table..
                .concat(",[ENTRYDATETIME] DATETIME")//n  //dbformate..
                .concat(",[TOTAL_TAXAMOUNT] DOUBLE")//y
                .concat(",[TOTAL_AMOUNT] DOUBLE")//y
                .concat(",[DISCOUNT] DOUBLE")//y
                .concat(",[GRAND_TOTAL] DOUBLE")//y
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", "OrderMaster" + qry);
    }

    private void createTableMaster() {
//        String Alterqry = "ALTER TABLE [Table_master] ADD [ORDER_NO] VARCHAR(6)";
//        db.execSQL(Alterqry);

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[Table_master]")
                .concat("(")
                .concat("[TABLE_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[TABLE_NO] VARCHAR(100)")
                .concat(",[ACTIVE] VARCHAR (3)")
                .concat(",[SITTING_CAPACITY] INT")
                .concat(",[STATUS] VARCHAR (20)")
                .concat(",[SORT_NO] INT")
                .concat(",[REMARK] VARCHAR(200)")
                .concat(",[ORDER_NO] VARCHAR(6)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", "TableMaster--" + qry);

    }

    private void createTaxMaster() {
//                db.execSQL("DROP TABLE [Taxes]");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[Taxes]")
                .concat("(")
                .concat("[TAX_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[TAX_TYPE] VARCHAR(10)")
                .concat(",[TAX_NAME] VARCHAR(100)")
                .concat(",[TAX_VALUE] DOUBLE")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "TaxMaster---->>>" + qry);
        db.execSQL(qry);
    }


    private void createMultipleImgSave() {
//        db.execSQL("DROP TABLE [MultipleImgSave]");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[MultipleImgSave]")
                .concat("(")
                .concat("[ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[Purchase_id] INTEGER")
                .concat(",[Item_code] VARCHAR(50)")
                .concat(",[Document_Name] VARCHAR(20)")
                .concat(",[Document_No] VARCHAR(50)") // Invoice No
                .concat(",[File_Path] VARCHAR(100)")
                .concat(",[Type] VARCHAR(10)") // Puchase / Sale / Item
                .concat(",[File_Name] VARCHAR(100)")
                .concat(",[Entry_date] DATETIME")
                .concat(")")
                .concat(";");
        db.execSQL(qry);

    }


    private void createSmsIdSetting() {


        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[SmsIdSetting]")
                .concat("(")
                .concat("[id] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[sms_id] VARCHAR(6)")
                .concat(",[default_sms] VARCHAR(3)")
                .concat(",[active] VARCHAR(3)")
                .concat(",[entry_date] DATETIME")
                .concat(",[remark] VARCHAR(200)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);

    }

    private void createSmsCustomerGroup() {
//        db.execSQL("DROP TABLE IF EXISTS [SmsCustomerGroup];");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[SmsCustomerGroup]")
                .concat("(")
                .concat("[GroupId] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[GroupName] VARCHAR(100),")
                .concat("[Active] VARCHAR(5),")
                .concat("[EntryDate] DATETIME,")
                .concat("[Remark] VARCHAR(500)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);

    }

    private void createSMSCustomerGroupDetail() {

//        db.execSQL("DROP TABLE IF EXISTS [SMSCustomerGroupDetail];");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[SMSCustomerGroupDetail]")
                .concat("(")
                .concat("[DetailId] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[GroupId] INTEGER ,")
                .concat("[CustomerName] VARCHAR(100),")
                .concat("[MobileNo] VARCHAR(15)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);

    }

    private void createEmployeeDocumentMaster() {
//   db.execSQL("DROP TABLE [EmployeeDocument]");


//        db.execSQL("ALTER TABLE [EmployeeDocument] ADD [TYPE] VARCHAR(10)");


        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[EmployeeDocument]")
                .concat("(")
                .concat("[DOCUMENT_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[EMP_ID] INTEGER")
                .concat(",[DOCUMENT_NAME] VARCHAR(50)")
                .concat(",[OTHER_PROF] VARCHAR(50)")
                .concat(",[DOCUMENT_NO] VARCHAR(50)")
                .concat(",[FILE_PATH] VARCHAR(100)")
                .concat(",[EXP_DATE] VARCHAR(10)")
                .concat(",[TYPE] VARCHAR(10)")
                .concat(",[FILE_NAME] VARCHAR(100)")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "EmployeeDocument---->>>" + qry);
        db.execSQL(qry);
    }


    private void createExpenseMaster() {
//           db.execSQL("DROP TABLE [ExpenseMaster]");
//        String qry = "CREATE TABLE IF NOT EXISTS "
//                .concat("[ExpenseMaster]")
//                .concat("(")
//                .concat("[Expense_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
//                .concat(",[VENDOR_ID] INTEGER")
//                .concat(",[EXPENSE_TYPE_ID] INTEGER")/// Expense Type
//                .concat(",[VENDOR_NAME] VARCHAR(50)")//
//                .concat(",[EMPLOYEE_NAME] VARCHAR(50)")//
//                .concat(",[RECEIPT_NO] VARCHAR(20)")//
//                .concat(",[RECEIPT_DATE] DATE")//
//                .concat(",[AMOUNT] DOUBLE")//
//                .concat(",[OTHER_TAX1] VARCHAR(50)")// GST
//                .concat(",[OTHER_VAL1] DOUBLE")//
//                .concat(",[OTHER_TAX2] VARCHAR(50)")//
//                .concat(",[OTHER_VAL2] DOUBLE")//
//                .concat(",[OTHER_TAX3] VARCHAR(50)")//
//                .concat(",[OTHER_VAL3] DOUBLE")//
//                .concat(",[OTHER_TAX4] VARCHAR(50)")//
//                .concat(",[OTHER_VAL4] DOUBLE")//
//                .concat(",[OTHER_TAX5] VARCHAR(50)")//
//                .concat(",[OTHER_VAL5] DOUBLE")//
//                .concat(",[DISCOUNT] DOUBLE")//
//                .concat(",[GRAND_TOTAL] DOUBLE")//
//                .concat(",[ENTRY_DATE] DATETIME")//
//                .concat(",[REMARK] VARCHAR(200)")//
//                .concat(")")
//                .concat(";");
//        db.execSQL(qry);
//
//        Log.e("CREATETABLE","EXPENSE---->>>"+ qry);
//        db.execSQL(qry);


//           db.execSQL("DROP TABLE [ExpenseMaster]");
        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[ExpenseMaster]")
                .concat("(")
                .concat("[EXPENSE_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[VENDOR_ID] INTEGER,")
                .concat("[EXPENSE_TYPE_ID] INTEGER,")
                .concat("[VENDOR_NAME] VARCHAR(100),")
                .concat("[EMPLOYEE_NAME] VARCHAR(100),")
                .concat("[AMOUNT] DOUBLE,")
                .concat("[OTHER_TAX1] VARCHAR(50),")
                .concat("[OTHER_VAL1] DOUBLE,")
                .concat("[OTHER_TAX2] VARCHAR(50),")
                .concat("[OTHER_VAL2] DOUBLE,")
                .concat("[OTHER_TAX3] VARCHAR(50),")
                .concat("[OTHER_VAL3] DOUBLE,")
                .concat("[OTHER_TAX4] VARCHAR(50),")
                .concat("[OTHER_VAL4] DOUBLE,")
                .concat("[OTHER_TAX5] VARCHAR(50),")
                .concat("[OTHER_VAL5] DOUBLE,")
                .concat("[DISCOUNT] DOUBLE,")
                .concat("[GRAND_TOTAL] DOUBLE,")
                .concat("[BILL_RECEIPT_NO] VARCHAR(20),")
                .concat("[TRANSACTION_DATE] DATE,")
                .concat("[ENTRY_DATE] DATETIME,")
                .concat("[REMARK] VARCHAR(200)")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "EXPENSE---->>>" + qry);
        db.execSQL(qry);

    }


    private void createExpenseMasterNew() {


//        db.execSQL("DROP TABLE [ExpenseMasterNew]");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[ExpenseMasterNew]")
                .concat("(")
                .concat("[Expense_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[VENDOR_ID] INTEGER")
                .concat(",[EXPENSE_TYPE_ID] INTEGER")/// Expense Type
                .concat(",[VENDOR_NAME] VARCHAR(50)")//
                .concat(",[EMPLOYEE_NAME] VARCHAR(50)")//
                .concat(",[RECEIPT_NO] VARCHAR(20)")//
                .concat(",[RECEIPT_DATE] DATE")//
                .concat(",[AMOUNT] DOUBLE")//
                .concat(",[OTHER_TAX1] VARCHAR(50)")// GST
                .concat(",[OTHER_VAL1] DOUBLE")//
                .concat(",[OTHER_TAX2] VARCHAR(50)")//
                .concat(",[OTHER_VAL2] DOUBLE")//
                .concat(",[OTHER_TAX3] VARCHAR(50)")//
                .concat(",[OTHER_VAL3] DOUBLE")//
                .concat(",[OTHER_TAX4] VARCHAR(50)")//
                .concat(",[OTHER_VAL4] DOUBLE")//
                .concat(",[OTHER_TAX5] VARCHAR(50)")//
                .concat(",[OTHER_VAL5] DOUBLE")//
                .concat(",[DISCOUNT] DOUBLE")//
                .concat(",[GRAND_TOTAL] DOUBLE")//
                .concat(",[ENTRY_DATE] DATETIME")//
                .concat(",[REMARK] VARCHAR(200)")//
                .concat(")")
                .concat(";");
        db.execSQL(qry);
        Log.e("CREATETABLE", qry);

    }

    private void createExpenseTypeMaster() {

//                db.execSQL("DROP TABLE [EXPENSE_TYPE_MASTER]");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[EXPENSE_TYPE_MASTER]")
                .concat("(")
                .concat("[EXPENSE_TYPE_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[EXPENSE_TYPE_NAME] VARCHAR(100),")
                .concat("[ACTIVE] VARCHAR(3),")
                .concat("[REMARK] VARCHAR(200),")
                .concat("[SORT_NO] INTEGER")
                .concat(")")
                .concat(";");


        Log.e("CREATETABLE", "EXPENSE_TYPE_MASTER---->>>" + qry);
        db.execSQL(qry);


        ClsExpenseType Obj = new ClsExpenseType(LogInActivity.this);
        String where = " AND [EXPENSE_TYPE_NAME] = "
                .concat("'")
                .concat("SALARY")
                .concat("' ");

        boolean exists = Obj.checkExists(where);
        if (!exists) {
            Obj.setExpense_type_name("SALARY");
            Obj.setActive("YES");
            ClsExpenseType.Insert(Obj);
        }

    }


    private void createInventoryItemMaster() {
//        db.execSQL("DROP TABLE [INVENTORY_ITEM_MASTER]");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[INVENTORY_ITEM_MASTER]")
                .concat("(")
                .concat("[INVENTORY_ITEM_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[INVENTORY_ITEM_NAME] VARCHAR(100),")
                .concat("[UNIT_ID] INTEGER,")
                .concat("[UNIT_NAME] VARCHAR(100),")
                .concat("[MAX_STOCK_QTY] DOUBLE,")
                .concat("[MIN_STOCK_QTY] DOUBLE,")
                .concat("[ACTIVE] VARCHAR(3),")
                .concat("[REMARK] VARCHAR(200)")
                .concat(")")
                .concat(";");


        Log.e("CREATETABLE", "INVENTORY_ITEM_MASTER---->>>" + qry);
        db.execSQL(qry);

    }

    private void createInventoryStockMaster() {
//        db.execSQL("DROP TABLE [Inventory_stock_master]");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[Inventory_stock_master]")
                .concat("(")
                .concat("[STOCK_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[ORDER_ID] INTEGER,")
                .concat("[VENDOR_ID] INTEGER,")
                .concat("[INVENTORY_ITEM_ID] INTEGER,")
                .concat("[INVENTORY_ITEM_NAME] VARCHAR(100),")
                .concat("[QUANTITY] DOUBLE,")
                .concat("[AMOUNT] DOUBLE,")
                .concat("[TYPE] VARCHAR(3),")
                .concat("[ENTRY_DATE] DATETIME,")
                .concat("[TRANSACTION_DATE] DATE,")
                .concat("[REMARK] VARCHAR(200)")
                .concat(")")
                .concat(";");


        Log.e("CREATETABLE", "INVENTORY_STOCK_MASTER---->>>" + qry);
        db.execSQL(qry);
    }

    private void createEmployeeMaster() {

//        db.execSQL("DROP TABLE [EMPLOYEE_MASTER]");
        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[EMPLOYEE_MASTER]")
                .concat("(")
                .concat("[EMPLOYEE_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[EMPLOYEE_NAME] VARCHAR(100),")
                .concat("[CONTACT_NO] VARCHAR(12),")
                .concat("[ADDRESS] VARCHAR(300),")
                .concat("[EMPLOYEE_IMAGE] VARCHAR(100),")
                .concat("[SALARY] DOUBLE(20),")
                .concat("[DOB] DATE,")
                .concat("[ACTIVE] VARCHAR(3),")
                .concat("[REMARK] VARCHAR(200),")
                .concat("[SORT_NO] INTEGER")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "EMP_MASTER---->>>" + qry);
        db.execSQL(qry);
    }

    private void createItemMaster() {
//            db.execSQL("DROP TABLE [ITEM_MASTER]");
        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[ITEM_MASTER]")
                .concat("(")
                .concat("[ITEM_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[ITEM_NAME] VARCHAR(100),")
                .concat("[CATEGORY_ID] INTEGER,")
                .concat("[CATEGORY_NAME] VARCHAR(100),")
                .concat("[FOOD_TYPE] VARCHAR(10),")
                .concat("[ACTIVE] VARCHAR(3),")
                .concat("[REMARK] VARCHAR(200),")
                .concat("[SORT_NO] INTEGER,")
                .concat("[PRICE] DOUBLE")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "ITEM_MASTER---->>>" + qry);
        db.execSQL(qry);
    }

    private void createVendorMaster() {
        //   db.execSQL("DROP TABLE [VENDOR_MASTER]");
//        db.execSQL("ALTER TABLE [VENDOR_MASTER] ADD [BalanceType] VARCHAR(15)");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[VENDOR_MASTER]")
                .concat("(")
                .concat("[VENDOR_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[VENDOR_NAME] VARCHAR(100),")
                .concat("[CONTACT_NO] VARCHAR(15),")
                .concat("[ADDRESS] VARCHAR(200),")
                .concat("[GST_NO] VARCHAR(20),")
                .concat("[TYPE] VARCHAR(15),")
                .concat("[ACTIVE] VARCHAR(3),")
                .concat("[REMARK] VARCHAR(200),")
                .concat("[BalanceType] VARCHAR(15),")
                .concat("[OpeningStock] DOUBLE,")
                .concat("[SORT_NO] INTEGER")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "VENDOR_MASTER---->>>" + qry);
        db.execSQL(qry);

        ClsVendor objvendor = new ClsVendor(LogInActivity.this);
        String where = " AND  [VENDOR_NAME] = "
                .concat("'")
                .concat("OTHER")
                .concat("' ");
        boolean exists = objvendor.checkExists(where, db);
        if (!exists) {
            objvendor.setVendor_name("OTHER");
            objvendor.setActive("YES");
            objvendor.setTYPE("BOTH");
            ClsVendor.Insert(objvendor, db);
        }
    }

    private void createUnitMaster() {
        String where = "";

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[UNIT_MASTER]")
                .concat("(")
                .concat("[UNIT_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[UNIT_NAME] VARCHAR(100),")
                .concat("[ACTIVE] VARCHAR(3),")
                .concat("[REMARK] VARCHAR(200),")
                .concat("[SORT_NO] INTEGER")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "UNIT_MASTER---->>>" + qry);
        db.execSQL(qry);

        boolean exists;

        ClsUnit objClsUnitMaster = new ClsUnit(LogInActivity.this);
        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("KG")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("KG");
            objClsUnitMaster.setRemark("kilogram");
            objClsUnitMaster.setSort_no(1);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("LTR")
                .concat("' ");


        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("LTR");
            objClsUnitMaster.setRemark("liter");
            objClsUnitMaster.setSort_no(2);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster, db);
        }
        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("PIECE")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("PIECE");
            objClsUnitMaster.setRemark("single pieces");
            objClsUnitMaster.setSort_no(3);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster, db);
        }
        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("BOX")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("BOX");
            objClsUnitMaster.setRemark("box");
            objClsUnitMaster.setSort_no(4);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("NO.")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("NO.");
            objClsUnitMaster.setRemark("number");
            objClsUnitMaster.setSort_no(5);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("GRAM")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setUnit_name("GRAM");
            objClsUnitMaster.setRemark("GRAM");
            objClsUnitMaster.setSort_no(6);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("HECTOGRAM")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("HECTOGRAM");
            objClsUnitMaster.setRemark("HECTOGRAM");
            objClsUnitMaster.setSort_no(7);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("DECIGRAM")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("DECIGRAM");
            objClsUnitMaster.setRemark("DECIGRAM");
            objClsUnitMaster.setSort_no(8);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("CENTIGRAM")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("CENTIGRAM");
            objClsUnitMaster.setRemark("CENTIGRAM");
            objClsUnitMaster.setSort_no(9);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("MILLIGRAM")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("MILLIGRAM");
            objClsUnitMaster.setRemark("MILLIGRAM");
            objClsUnitMaster.setSort_no(9);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("CARAT")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("CARAT");
            objClsUnitMaster.setRemark("CARAT");
            objClsUnitMaster.setSort_no(10);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("METER")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("METER");
            objClsUnitMaster.setRemark("METER");
            objClsUnitMaster.setSort_no(11);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("DECIMETER")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("DECIMETER");
            objClsUnitMaster.setRemark("DECIMETER");
            objClsUnitMaster.setSort_no(13);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("CENTIMETER")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("CENTIMETER");
            objClsUnitMaster.setRemark("CENTIMETER");
            objClsUnitMaster.setSort_no(14);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("MILLIMETER")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("MILLIMETER");
            objClsUnitMaster.setRemark("MILLIMETER");
            objClsUnitMaster.setSort_no(15);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("MILE")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("MILE");
            objClsUnitMaster.setRemark("MILE");
            objClsUnitMaster.setSort_no(16);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("INCH")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("INCH");
            objClsUnitMaster.setRemark("INCH");
            objClsUnitMaster.setSort_no(17);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("FOOT")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("FOOT");
            objClsUnitMaster.setRemark("FOOT");
            objClsUnitMaster.setSort_no(18);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster, db);
        }
        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("YARD")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("YARD");
            objClsUnitMaster.setRemark("YARD");
            objClsUnitMaster.setSort_no(19);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster, db);
        }
        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("MICROGRAM")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("MICROGRAM");
            objClsUnitMaster.setRemark("MICROGRAM");
            objClsUnitMaster.setSort_no(20);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster, db);
        }
    }

    private void createSizeMaster() {

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[SIZE_MASTER]")
                .concat("(")
                .concat("[SIZE_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[SIZE_NAME] VARCHAR(100),")
                .concat("[ACTIVE] VARCHAR(3),")
                .concat("[REMARK] VARCHAR(200),")
                .concat("[SORT_NO] INTEGER")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "SIZE_MASTER---->>>" + qry);
        db.execSQL(qry);
    }


    private void createCategoryMaster() {
        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[CATEGORY_MASTER]")
                .concat("(")
                .concat("[CATEGORY_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[CATEGORY_NAME] VARCHAR(100),")
                .concat("[ACTIVE] VARCHAR(3),")
                .concat("[REMARK] VARCHAR(200),")
                .concat("[SORT_NO] INTEGER")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "CATEGORY_MASTER---->>>" + qry);
        db.execSQL(qry);

    }

    @Override
    protected void onResume() {
        super.onResume();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        if (saveLogin == true) {
            edt_mobile.setText(loginPreferences.getString("username", ""));
            edt_password.setText(loginPreferences.getString("password", ""));
            chk_remember.setChecked(true);
        } else {
            edt_mobile.setText(loginPreferences.getString("username", ""));
            edt_password.setText(loginPreferences.getString("password", ""));
            chk_remember.setChecked(false);
            input_email.setVisibility(View.GONE);
            edt_email.setText("");
        }

    }

    void LoginAPI() {
        String UserType = "Customer";

        ClsLoginParams objClsLoginParams = new ClsLoginParams();
        objClsLoginParams.setUserType(UserType);
        objClsLoginParams.setPassword(edt_password.getText().toString().trim());
        objClsLoginParams.setMobileNumber(edt_mobile.getText().toString().trim());
        objClsLoginParams.setProductName(ClsGlobal.AppName);
        objClsLoginParams.setApplicationVersion(ClsGlobal.getApplicationVersion(LogInActivity.this));
        objClsLoginParams.setIMEINumber(ClsGlobal.getIMEIno(LogInActivity.this));
        objClsLoginParams.setDeviceInfo(ClsGlobal.getDeviceInfo(LogInActivity.this));

        if (input_email.getTag().toString() == "1") {
            objClsLoginParams.setEmail(edt_email.getText().toString());
        } else {
            objClsLoginParams.setEmail("");
        }

        Gson gson = new Gson();
        String jsonInString = gson.toJson(objClsLoginParams);
        Log.d("--GSON--", "LoginAPI- " + jsonInString);

        InterfaceLogin interfaceLogin = ApiClient.getRetrofitInstance().create(InterfaceLogin.class);
        Log.e("--URL--", "interfaceLogin: " + interfaceLogin.toString());
        Call<ClsLoginParams> call = interfaceLogin.postLogin(objClsLoginParams);
        Log.e("--URL--", "************************  before call : " + call.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(LogInActivity.this, "Loading...", true);
        pd.show();

        try {
            call.enqueue(new Callback<ClsLoginParams>() {
                @Override
                public void onResponse(Call<ClsLoginParams> call, Response<ClsLoginParams> response) {
                    Log.e("--URL--", "response: " + response);
                    pd.dismiss();
                    if (response.body() != null) {
                        String _response = response.body().getSuccess();
                        Log.e("--response--", "response: " + _response);

                        ClsUserInfo objClsUserInfo = new ClsUserInfo();

                        if (_response.equals("1")) {

                            List<ClsLoginResponseList> lstClsLoginResponseLists = response.body().getData();

                            if (lstClsLoginResponseLists != null && lstClsLoginResponseLists.size() != 0) {
                                objUser = lstClsLoginResponseLists.get(0);
                                objClsUserInfo.setUserId(objUser.getId());

                                Log.e("--log--", "_customerIdLogin: " + objUser.getId());

                                if (objUser.getForcefullychangepassword().equalsIgnoreCase("NO")) {
                                    Intent intent = new Intent(getApplicationContext(), ActivitySetNewPassword.class);
                                    intent.putExtra("_customerId", objUser.getId());
                                    intent.putExtra("_oldPass", edt_password.getText().toString().trim());
                                    startActivity(intent);
                                } else {
                                    objClsUserInfo.setRemainDays(String.valueOf(objUser.getRemaindays()));
                                    objClsUserInfo.setExpiredDays(objUser.getExpiredate());
                                    objClsUserInfo.setContactPersonName(objUser.getContactpersonname());
                                    objClsUserInfo.setLoginStatus("ACTIVE");
                                    objClsUserInfo.setMobileNo(edt_mobile.getText().toString().trim());
                                    objClsUserInfo.setBusinessname(objUser.getBusinessname());
                                    objClsUserInfo.setBusinessaddress(objUser.getBusinessaddress());
                                    objClsUserInfo.setRegisteredmobilenumber(objUser.getRegisteredmobilenumber());
                                    objClsUserInfo.setEmailaddress(objUser.getEmailaddress());
                                    objClsUserInfo.setState(objUser.getState());
                                    objClsUserInfo.setCity(objUser.getCity());
                                    objClsUserInfo.setPincode(objUser.getPincode());
                                    objClsUserInfo.setCinnumber(objUser.getCinnumber());
                                    objClsUserInfo.setGstnumber(objUser.getGstnumber());

//----------------------------------------------------------------------------------------------------------------
                                    objClsUserInfo.setLicenseType(objUser.getLicenseType());
//----------------------------------------------------------------------------------------------------------------

                                    Gson gson = new Gson();
                                    String jsonInString = gson.toJson(objClsUserInfo);
                                    Log.e("--Login--", "UserInfo: " + jsonInString);
                                    Log.e("--Login--", "getLicenseType: " + objUser.getLicenseType());

                                    if (objClsUserInfoOld.getUserId() != null && objClsUserInfoOld.getUserId() != "" && !objClsUserInfoOld.getUserId().equalsIgnoreCase(objUser.getId())) {

                                        AlertDialog alertDialog = new AlertDialog.Builder(LogInActivity.this,
                                                R.style.AppCompatAlertDialogStyle).create(); //Read Update.
                                        alertDialog.setContentView(R.layout.activity_dialog);
                                        alertDialog.setTitle("Confirmation");
                                        alertDialog.setMessage("previous user/login data will be remove make sure your data backup is upto date,click yes to continue for new user login.");
                                        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                DropAllTables();

                                                ClsGlobal.setUserInfo(objClsUserInfo, LogInActivity.this);

                                                Toast.makeText(LogInActivity.this, "Welcome "
                                                        .concat(objClsUserInfo.getContactPersonName()), Toast.LENGTH_SHORT).show();

//                                                SaveData("Frist");

//                                                periodicWorkRequest = new PeriodicWorkRequest.Builder(DailyLogoutTask.class,
//                                                        1, TimeUnit.DAYS)
//                                                        .build();
//
//                                                // For Setting up Unique PeriodicWork. So there is one PeriodicWork active at a time.
//                                                // Remember there is Only one PeriodicWork at a time.
//                                                WorkManager.getInstance().enqueueUniquePeriodicWork(ClsGlobal.AppPackageName.concat("DailyTaskLogoutRetail")
//                                                        , ExistingPeriodicWorkPolicy.KEEP
//                                                        , periodicWorkRequest);

                                                //clear sale & wholesale running order no..

                                                // Start order number

                                                ClsGlobal._OrderNo = "";
                                                ClsGlobal.editOrderID = "";

                                                ClsGlobal.SetKeepOrderNo(LogInActivity.this, "", "Sale");
                                                ClsGlobal._WholeSaleOrderNo = "";
                                                ClsGlobal.SetKeepOrderNo(LogInActivity.this, "", "Wholesale");

                                                // Endorder number

                                                Intent intent = new Intent(getApplicationContext(), SHAP_Lite_Activity.class);
                                                intent.putExtra("_customerId", objUser.getId());
                                                intent.putExtra("_licenseType", objUser.getLicenseType());
                                                intent.putExtra("_renewAlert", "Yes");
                                                startActivity(intent);
                                            }
                                        });
                                        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                        alertDialog.setCancelable(false);
                                        alertDialog.show();

                                    } else {
                                        ClsGlobal.setUserInfo(objClsUserInfo, LogInActivity.this);
                                        Toast.makeText(LogInActivity.this, "Welcome ".concat(objClsUserInfo.getContactPersonName()), Toast.LENGTH_SHORT).show();
                                        SaveData("Frist");

                                      /*  periodicWorkRequest = new PeriodicWorkRequest.Builder(DailyLogoutTask.class, 18, TimeUnit.MINUTES)
                                                .build();
*/
//                                        periodicWorkRequest = new PeriodicWorkRequest.Builder(DailyLogoutTask.class,
//                                                1, TimeUnit.DAYS)
//                                                .build();
//
//                                        // For Setting up Unique PeriodicWork. So there is one PeriodicWork active at a time.
//                                        // Remember there is Only one PeriodicWork at a time.
//                                        WorkManager.getInstance().enqueueUniquePeriodicWork(ClsGlobal.AppPackageName
//                                                        .concat("DailyTaskLogoutRetail")
//                                                , ExistingPeriodicWorkPolicy.KEEP
//                                                , periodicWorkRequest);

                                        Intent intent = new Intent(getApplicationContext(), SHAP_Lite_Activity.class);
                                        intent.putExtra("_renewAlert", "Yes");
                                        intent.putExtra("_customerId", objUser.getId());
                                        intent.putExtra("_licenseType", objUser.getLicenseType());
                                        startActivity(intent);
                                    }
                                }
                            }
                        } else if (_response.equals("2")) {


                            List<ClsLoginResponseList> lstClsLoginResponseLists = response.body().getData();
                            if (lstClsLoginResponseLists != null && lstClsLoginResponseLists.size() != 0) {
                                objUser = lstClsLoginResponseLists.get(0);
                                Toast.makeText(LogInActivity.this, objUser.getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        } else if (_response.equals("3") || _response.equals("5")) {

// response 3 = customer status is DeActive
// response 5 = Package is Exp


//                            Intent intent = new Intent(getApplicationContext(), DisplayPlansActivity.class);
//                            intent.putExtra("reg_mode", "RENEW");
//                            intent.putExtra("_customerId", objUser.getId());
//                            startActivity(intent);


                            Log.e("--log--", "_response: 3");


                            List<ClsLoginResponseList> lstClsLoginResponseLists = response.body().getData();
                            if (lstClsLoginResponseLists != null && lstClsLoginResponseLists.size() != 0) {
                                objUser = lstClsLoginResponseLists.get(0);
                                renewPackageAlert(objUser.getId(), "YOUR PACKAGE IS EXPIRED PLEASE SELECT RENEW PLAN.");

                                Toast.makeText(LogInActivity.this, objUser.getMessage(), Toast.LENGTH_SHORT).show();

//                                Gson gson = new Gson();
//                                String jsonInString = gson.toJson(lstClsLoginResponseLists);
//                                Log.e("--log--", "UserInfo: " + jsonInString);

                                Log.e("--log--", "objUser.getId(): " + objUser.getId());

                            }

                        } else if (_response.equals("4")) {


                            List<ClsLoginResponseList> lstClsLoginResponseLists = response.body().getData();
                            if (lstClsLoginResponseLists != null && lstClsLoginResponseLists.size() != 0) {
                                objUser = lstClsLoginResponseLists.get(0);
                                input_email.setTag("1");
                                input_email.setVisibility(View.VISIBLE);
                                vw_email.setVisibility(View.VISIBLE);
                                Toast.makeText(LogInActivity.this, objUser.getMessage(), Toast.LENGTH_LONG).show();
                                edt_email.requestFocus();
                            }

                        }
                      /*
                        else if (_response.equals("5")) {
//pkg is expired

                            List<ClsLoginResponseList> lstClsLoginResponseLists = response.body().getData();
                            if (lstClsLoginResponseLists != null && lstClsLoginResponseLists.size() != 0) {
                                objUser = lstClsLoginResponseLists.get(0);
                                renewPackageAlert(objUser.getId(), "YOUR PACKAGE IS EXPIRING ON PLEASE RENEW BEFORE EXPIRE EXISTING PACKAGE TO UTILISE SMOOTH &amp; UNINTERRUPTED SERVICE.", "response4");
                            }

                        }*/

                        else if (_response.equals("6")) {
                            Toast.makeText(LogInActivity.this, "New version is available", Toast.LENGTH_SHORT).show();
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=".concat(ClsGlobal.AppPackageName))));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=")));
                            }
                        } else if (_response.equals("11")) {
                            List<ClsLoginResponseList> lstClsLoginResponseLists = response.body().getData();
                            if (lstClsLoginResponseLists != null && lstClsLoginResponseLists.size() != 0) {
                                objUser = lstClsLoginResponseLists.get(0);
                                Toast.makeText(LogInActivity.this, objUser.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Server error...!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ClsLoginParams> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Something went wrong...Internet issue!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d("Exception", "LoginAPI: " + e.getMessage());
        }
    }

    void renewPackageAlert(String _customerId, String _description) {

        Dialog dialog = new Dialog(LogInActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_renew_pkg);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView txt_descriptions = dialog.findViewById(R.id.txt_descriptions);
        txt_descriptions.setText(_description);

        TextView txt_not_now = dialog.findViewById(R.id.txt_not_now);
        TextView txt_free = dialog.findViewById(R.id.txt_free);
        TextView txt_renew = dialog.findViewById(R.id.txt_renew);

//        txt_descriptions.setText("Your package is expired please select renew plan.");

        txt_not_now.setOnClickListener(v -> {
            dialog.cancel();
            dialog.dismiss();
        });

        txt_free.setOnClickListener(v -> {

            dialog.cancel();
            dialog.dismiss();


            Intent intent = new Intent(LogInActivity.this, OtpVerificationActivity.class);

            intent.putExtra("_mobileNo", edt_mobile.getText().toString().trim());
            intent.putExtra("_customerId", _customerId);
            intent.putExtra("_resCustomerStatus", "");
            intent.putExtra("_resLicenseType", "Free");
            intent.putExtra("mode", "Free");
            intent.putExtra("sendSMSNow", "Yes");

            startActivity(intent);

        });

        txt_renew.setOnClickListener(v -> {
            dialog.cancel();
            dialog.dismiss();

            Intent intent = new Intent(getApplicationContext(), DisplayPlansActivity.class);
            intent.putExtra("reg_mode", "RENEW");
            intent.putExtra("_customerId", _customerId);

            startActivity(intent);
        });

        dialog.getWindow().setAttributes(lp);
        if (!((Activity) this).isFinishing()) {
            dialog.show();
        }
    }


    private void CustomerFreeLicenseUpdationAPI(String _customerId) {

        ClsCustomerFreeLicenseUpdation obj = new ClsCustomerFreeLicenseUpdation();
        obj.setCustomerCode(_customerId);
        obj.setIMEINo(ClsGlobal.getIMEIno(LogInActivity.this));
        obj.setMode("Free");
//        obj.setReferalCode("EJK001");
        obj.setReferalCode("");
        obj.setReferalApplied("NO");


        Gson gson = new Gson();
        String jsonInString = gson.toJson(obj);
        Log.d("--GSON--", "FreeAPI: " + jsonInString);


        InterfaceCustomerFreeLicenseUpdation interfaceFreeLicenseUpdation =
                ApiClient.getRetrofitInstance().create(InterfaceCustomerFreeLicenseUpdation.class);
        Log.e("--URL--", "interfaceDesignation: " + interfaceFreeLicenseUpdation.toString());
        Call<ClsCustomerFreeLicenseUpdation> call = interfaceFreeLicenseUpdation.postCall(obj);
        Log.e("--URL--", "URL: " + call.request().url());


        ProgressDialog pd =
                ClsGlobal._prProgressDialog(LogInActivity.this,
                        "Working...", true);
        pd.show();


        call.enqueue(new Callback<ClsCustomerFreeLicenseUpdation>() {
            @Override
            public void onResponse(Call<ClsCustomerFreeLicenseUpdation> call, Response<ClsCustomerFreeLicenseUpdation> response) {
                pd.dismiss();

                if (response.body() != null) {
                    String _response = response.body().getSuccess();
                    Log.d("_response", "onResponse: " + _response);

                    switch (_response) {
                        case "1":

                            Toast.makeText(LogInActivity.this,
                                    "You are Successfully Verified.", Toast.LENGTH_SHORT).show();

                            break;
                        case "0":
                            Toast.makeText(LogInActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(LogInActivity.this, "Failed default", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server error...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsCustomerFreeLicenseUpdation> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong...API is Not Working!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void SaveData(String str) {
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString("Status1", str);
        preferencesEditor.apply();

    }

    @SuppressLint("WrongConstant")
    private void DropAllTables() {

        io.requery.android.database.sqlite.SQLiteDatabase db1 =
                io.requery.android.database.sqlite.SQLiteDatabase.openOrCreateDatabase(
                        getDatabasePath(ClsGlobal.Database_Name).getPath(),
                        null);

        db1.execSQL("DROP TABLE IF EXISTS [tbl_Terms];");
        db1.execSQL("DROP TABLE IF EXISTS [OrderDetail_master];");
        db1.execSQL("DROP TABLE IF EXISTS [tbl_EmailLogs];");
        db1.execSQL("DROP TABLE IF EXISTS [CustomerMaster];");
        db1.execSQL("DROP TABLE IF EXISTS [Ordermaster];");
        db1.execSQL("DROP TABLE IF EXISTS [Table_master];");
        db1.execSQL("DROP TABLE IF EXISTS [Taxes];");
        db1.execSQL("DROP TABLE IF EXISTS [EmployeeDocument];");
        db1.execSQL("DROP TABLE IF EXISTS [ExpenseMaster];");
        db1.execSQL("DROP TABLE IF EXISTS [ExpenseMasterNew];");
        db1.execSQL("DROP TABLE IF EXISTS [EXPENSE_TYPE_MASTER];");
        db1.execSQL("DROP TABLE IF EXISTS [INVENTORY_ITEM_MASTER];");
        db1.execSQL("DROP TABLE IF EXISTS [Inventory_stock_master];");
        db1.execSQL("DROP TABLE IF EXISTS [EMPLOYEE_MASTER];");
        db1.execSQL("DROP TABLE IF EXISTS [ITEM_MASTER];");
        db1.execSQL("DROP TABLE IF EXISTS [VENDOR_MASTER];");
        db1.execSQL("DROP TABLE IF EXISTS [UNIT_MASTER];");
        db1.execSQL("DROP TABLE IF EXISTS [SIZE_MASTER];");
        db1.execSQL("DROP TABLE IF EXISTS [CATEGORY_MASTER];");
        db1.execSQL("DROP TABLE IF EXISTS [tbl_InventoryLayer];");
        db1.execSQL("DROP TABLE IF EXISTS [tbl_LayerItem_Master];");
        db1.execSQL("DROP TABLE IF EXISTS [tbl_ItemTag];");
        db1.execSQL("DROP TABLE IF EXISTS [tbl_ItemLayer];");
        db1.execSQL("DROP TABLE IF EXISTS [tbl_Tax_Slab];");
        db1.execSQL("DROP TABLE IF EXISTS [PaymentMaster];");
        db1.execSQL("DROP TABLE IF EXISTS [PurchaseDetail];");
        db1.execSQL("DROP TABLE IF EXISTS [InventoryOrderDetail];");
        db1.execSQL("DROP TABLE IF EXISTS [PurchaseMaster];");
        db1.execSQL("DROP TABLE IF EXISTS [CommonLogs_Master];");

        db1.execSQL("DROP TABLE IF EXISTS [MultipleImgSave];");
        db1.execSQL("DROP TABLE IF EXISTS [SmsCustomerGroup];");
        db1.execSQL("DROP TABLE IF EXISTS [SMSCustomerGroupDetail];");
        db1.execSQL("DROP TABLE IF EXISTS [tbl_customer_import];");
        db1.execSQL("DROP TABLE IF EXISTS [SmsIdSetting];");
        db1.execSQL("DROP TABLE IF EXISTS [MessageFormat_Master];");
        db1.execSQL("DROP TABLE IF EXISTS [QuotationDetail];");
        db1.execSQL("DROP TABLE IF EXISTS [QuotationMaster];");

        db1.close();
    }

    /* Displays the snackbar notification and call to action. */
    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.main_layout),
                        "An update has just been downloaded.",
                        Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("RESTART", view ->
                appUpdateManager.completeUpdate());

        snackbar.setActionTextColor(
                getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }


    @Override
    public void onStateUpdate(InstallState installState) {

        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            popupSnackbarForCompleteUpdate();
        } else if (installState.installStatus() == InstallStatus.INSTALLED) {
            if (appUpdateManager != null) {
                appUpdateManager.unregisterListener(this);
            }

        } else {
            Log.i("login", "InstallStateUpdatedListener: state: " + installState.installStatus());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (appUpdateManager != null) {
            appUpdateManager.unregisterListener(this);
        }
        if (db != null) {
            db.close();
        }
    }
}
