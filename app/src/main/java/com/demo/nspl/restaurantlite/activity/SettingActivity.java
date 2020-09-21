package com.demo.nspl.restaurantlite.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.SettingAdapter;
import com.demo.nspl.restaurantlite.Complain.SelectComplainActivity;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ApiClient;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.ClsPermission;
import com.demo.nspl.restaurantlite.ListOfBkpFile.Activity_ListOfBkp_File;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsBackupTypeParams;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceBackupType;
import com.demo.nspl.restaurantlite.Signature.ActivityLogoSignature;
import com.demo.nspl.restaurantlite.classes.ClsSettingNames;
import com.demo.nspl.restaurantlite.classes.ClsUserInfo;
import com.demo.nspl.restaurantlite.printerUtility.ClsPrinter;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getUserInfo;
import static com.demo.nspl.restaurantlite.Global.ClsPermission.REQUEST_READ_PHONE_STATE;

public class SettingActivity extends AppCompatActivity {

    private List<ClsSettingNames> mList;
    private RecyclerView mRv;
    private SettingAdapter mCv;
    private Toolbar toolbar;
    private ProgressDialog pd;
    static final int DATE_DIALOG_ID = 0;
    String byte_array = "";

    private String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    //  private String dataPath = SDPath + "/instinctcoder/zipunzip/data/" ;

    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 120;
    String _customerId = "";
//    String LicenseType = "";

    String _licenseType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkPermission();
        mRv = findViewById(R.id.rv);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "SettingActivity"));
        }

        ClsPermission.checkpermission(SettingActivity.this);
        ClsUserInfo getUserInfo = ClsGlobal.getUserInfo(this);

        _customerId = getUserInfo.getUserId();
//        LicenseType = getUserInfo.getLicenseType();

//        Log.e("_customerId", _customerId);

        _licenseType = getIntent().getStringExtra("_licenseType");
        Log.e("--LicenseType--", "_licenseType: " + _licenseType);
        Log.e("--LicenseType--", "_customerId: " + _customerId);

        ViewData();

    }

    private void ViewData() {

        mList = new ArrayList<>();

        mRv.setLayoutManager(new LinearLayoutManager(this));


        mList.add(new ClsSettingNames(R.drawable.ic_email_new, "Auto mail report setup", "Configure Auto Male and get MIS on male"));


        //------------------------------- For Demo App -------------------------------------//

        mList.add(new ClsSettingNames(R.drawable.ic_update_profile, "Update profile", "Change owner name,change address,GST no,CIN no"));
        mList.add(new ClsSettingNames(R.drawable.ic_change_password, "Change password", "Current password and set new password"));
        mList.add(new ClsSettingNames(R.drawable.ic_mobile_settings, "Change mobile number", "Change your application registration number"));
//        mList.add(new ClsSettingNames(R.drawable.ic_printer_black, "Printer"));
        mList.add(new ClsSettingNames(R.drawable.ic_package, "Current package", "Display current package details"));


        if (_licenseType != null && !_licenseType.equalsIgnoreCase("Free")) {
            mList.add(new ClsSettingNames(R.drawable.ic_renew_settings, "Renew Package", "New packages information"));
        }

        //------------------------------- For Demo App -------------------------------------//

        mList.add(new ClsSettingNames(R.drawable.ic_backup_settings, "Backup", "Backup To Cloud,Local device,Google drive"));
        mList.add(new ClsSettingNames(R.drawable.ic_restore_new, "Restore", "Get backup from Cloud,Phone Storage,Google Drive"));
        mList.add(new ClsSettingNames(R.drawable.ic_logo_sign, "Logo & Signature", "Set your firm logo & signature"));
        mList.add(new ClsSettingNames(R.drawable.ic_new, "What's New", "What's new about application"));
        mList.add(new ClsSettingNames(R.drawable.ic_bill_format, "Bill Number Formats", "Change your Bill/Invoice generated no"));
        mList.add(new ClsSettingNames(R.drawable.ic_help, "Help", "Select complain and also send image also"));
        mList.add(new ClsSettingNames(R.drawable.ic_translate, "FAQ", "Frequently asked questions"));

//        mList.add(new ClsSettingNames(R.drawable.ic_new, "Set Number Format"));

        mCv = new SettingAdapter(this, mList, clsSettingNames -> {

            String current = clsSettingNames.getSettingName();
            if (current.equals("Auto mail report setup")) {
                StartIntent(SettingAutoEmailActivity.class);
//            } else if (current.equals("Printer")) {
//                PrinterSetting();
            } else if (current.equals("Current package")) {

                Intent intent = new Intent(SettingActivity.this, CurrentPackageActivity.class);
                intent.putExtra("_customerId", _customerId);
                startActivity(intent);

            } else if (current.equals("Backup")) {

                Intent intent = new Intent(SettingActivity.this, BackUpAndRestoreActivity.class);
                intent.putExtra("_customerId", _customerId);
                intent.putExtra("LicenseType", _licenseType);
                startActivity(intent);


//                    if (!ClsGlobal.CheckInternetConnection(SettingActivity.this)) {
//                        Toast.makeText(SettingActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
//                    } else {
//                        try {
//
//                            AlertDialog alertDialog = new AlertDialog.Builder(SettingActivity.this,
//                                    R.style.AppCompatAlertDialogStyle).create(); //Read Update.
//                            alertDialog.setContentView(R.layout.activity_dialog);
//                            alertDialog.setTitle("Confirmation");
//                            alertDialog.setMessage("sure to start backup?");
//                            alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    Toast.makeText(getApplicationContext(), "CLICK", Toast.LENGTH_SHORT).show();
//                                    generateBackupFile();
//                                    backupDatabaseAPI("Database", byte_array, ".db", "Database".concat(ClsGlobal.getEntryDate()));
//
//                                    File _saveLocation = Environment.getExternalStorageDirectory();
//                                    Log.d("camera", "filepath:- " + _saveLocation);
//                                    File dir = new File(_saveLocation.getAbsolutePath() + "/" + ClsGlobal.AppFolderName + "/");
//                                    Log.d("generateBackupFile", "dir:- " + dir);
//
//                                    if (FileZipOperation.zip(dir.getAbsolutePath(), zipPath, "ShapBKP.zip", true)) {
//                                        Log.d("generateBackupFile", "IF:- ");
//                                        Toast.makeText(SettingActivity.this, "Zip successfully.", Toast.LENGTH_LONG).show();
//                                    }
//
//                                    File zipFile = new File(zipPath + "/ShapBKP.zip");
//                                    String zipFileByteArray = ClsGlobal.getStringFile(zipFile);
//                                    backupDatabaseAPI("Document", zipFileByteArray, ".zip", "ShapBKP".concat(ClsGlobal.getEntryDate()));
//                                }
//                            });
//                            alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
//
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                }
//                            });
//                            alertDialog.setCancelable(false);
//                            alertDialog.show();
//
//
//                        } catch (Exception e) {
//                            Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_SHORT).show();
//                        }
//                    }


//                } else if (current.equals("Restore")) {
//                    if (!ClsGlobal.CheckInternetConnection(SettingActivity.this)) {
//                        Toast.makeText(SettingActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
//                    } else {
//                        try {
//
//                            Intent intent = new Intent(SettingActivity.this, ActivityRestoreList.class);
//                            intent.putExtra("_customerId", _customerId);
//                            startActivity(intent);
//
//                        } catch (Exception e) {
//                            Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_SHORT).show();
//                        }
//                    }
            } else if (current.equals("Change mobile number")) {
                Intent intent = new Intent(SettingActivity.this, ActivityChangeMobileNum.class);
                intent.putExtra("_customerId", _customerId);
                startActivity(intent);
            } else if (current.equals("Change password")) {

                Intent intent = new Intent(SettingActivity.this, ActivityChangePassword.class);
                intent.putExtra("_customerId", _customerId);
                startActivity(intent);

            } else if (current.equals("Renew Package")) {

                ClsUserInfo objClsUserInfo = ClsGlobal.getUserInfo(SettingActivity.this);
                checkTotalDaysDialog(objClsUserInfo.getRemainDays(), objClsUserInfo.getExpiredDays(), objClsUserInfo.getUserId());

            } else if (current.equals("Update profile")) {

                Intent intent = new Intent(SettingActivity.this, ActivityUpdateProfile.class);
                intent.putExtra("_customerId", _customerId);
                startActivity(intent);

            } else if (current.equals("Logo & Signature")) {

                Intent intent = new Intent(SettingActivity.this, ActivityLogoSignature.class);
                intent.putExtra("_customerId", _customerId);
                startActivity(intent);
            } else if (current.equals("Restore")) {

                Intent intent = new Intent(SettingActivity.this, Activity_ListOfBkp_File.class);
                intent.putExtra("_customerId", _customerId);
                intent.putExtra("LicenseType", _licenseType);
                startActivity(intent);

            } else if (current.equals("Help")) {
                ClsUserInfo objUserInfo = getUserInfo(getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), SelectComplainActivity.class);
                intent.putExtra("_code", objUserInfo.getUserId());
                intent.putExtra("mobile", objUserInfo.getMobileNo());
                startActivity(intent);
            } else if (current.equals("What's New")) {
                Intent intent = new Intent(getApplicationContext(), Activity_wts_new.class);
                intent.putExtra("webViewMode", "New");
                startActivity(intent);
            } else if (current.equals("Bill Number Formats")) {
                Intent intent = new Intent(getApplicationContext(), Bill_No_FormatsActivity.class);
                startActivity(intent);
            } else if (current.equals("FAQ")) {
                Intent intent = new Intent(getApplicationContext(), Faq_Activity.class);
                intent.putExtra("_destinationMode", "Settings");
                startActivity(intent);
            }/*else if (current.equals("Set Number Format")) {
                Intent intent = new Intent(getApplicationContext(), SetNumFormatActivity.class);
                startActivity(intent);
            }*/

        });

        mRv.setAdapter(mCv);
    }

    private void StartIntent(Class className) {
        Intent intent = new Intent(SettingActivity.this, className);
        startActivity(intent);
    }

    private void PrinterSetting() {
        final Dialog dialog = new Dialog(SettingActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_printer);

        dialog.setCancelable(true);
        dialog.show();

        Spinner sp_pname = dialog.findViewById(R.id.sp_pname);
        Button btn_connect = dialog.findViewById(R.id.btn_connect);

        //Global class
        List<String> list = new ArrayList<String>(ClsPrinter.loadbtpname());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SettingActivity.this, R.layout.spinner_item, list);
        sp_pname.setAdapter(adapter);

        int _pos = 0;
        for (String _Obj : list) {
            if (_Obj.equalsIgnoreCase(ClsGlobal.defaultPrinterName)) {
                Log.e("_parameter", " defaultPrinterName : " + ClsGlobal.defaultPrinterName);

                _pos = list.indexOf(_Obj);
                sp_pname.setSelection(_pos);
                break;
            }
        }

        if (!ClsPrinter.isbluetoothconnection(SettingActivity.this)) {
            Toast.makeText(getApplicationContext(), "Bluetooth Not Connect", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth Connected", Toast.LENGTH_SHORT).show();
        }
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_connect.getTag().toString().trim().equals("0")) {
                    if (sp_pname.getCount() > 0) {
                        Log.e("btn", "inside connect");
                        String btname = sp_pname.getSelectedItem().toString();
                        ClsGlobal.SetSharedPreferencesFile("printerConfig",
                                "pname", btname, SettingActivity.this);

                        new ClsPrinter.ConnectBT().execute();

                        editor.putString("printername", btname);
                        editor.apply();
                    }

                } else {
                    try {
                        ClsPrinter.closeBT();
                        btn_connect.setTag("0");
                        btn_connect.setText("connect");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                dialog.dismiss();
            }
        });
    }


    void checkTotalDaysDialog(String remainDays, String expireDate, String customerId) {

        AlertDialog alertDialog = new AlertDialog.Builder(SettingActivity.this,
                R.style.AppCompatAlertDialogStyle).create(); //Read Update.
        alertDialog.setContentView(R.layout.activity_dialog);
        alertDialog.setTitle("Package Alert!");
//        alertDialog.setMessage("your package is expiring on ".concat(expireDate).concat(", remain days is ").concat(remainDays).concat(". please renew before it's expired to continue use service without any interruption."));
        alertDialog.setMessage("Your package is expiring on ".concat(expireDate).concat(", remaining days is ").concat(remainDays + ",").concat(" please renew before expire existing package to utilise somooth & uninterrupted service."));


        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Renew", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ClsGlobal.autoLogout(getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), DisplayPlansActivity.class);
                intent.putExtra("reg_mode", "RENEW");
                intent.putExtra("_customerId", customerId);
                startActivity(intent);
            }
        });
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Not now", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(SettingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                    builder.setMessage("This permission is important to record audio.")
                            .setTitle("Important permission required");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                        }
                    });
                } else {
                    ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
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

    void generateBackupFile() {


        //generate database backup file

        try {
            File _saveLocation = Environment.getExternalStorageDirectory();
            Log.d("camera", "filepath:- " + _saveLocation);
            File dir = new File(_saveLocation.getAbsolutePath() + "/" + ClsGlobal.AppFolderName + "/Backup/");
            Log.d("generateBackupFile", "dir:- " + dir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String AppDatabasePath = ClsGlobal.AppDatabasePath.concat(ClsGlobal.Database_Name);
            Log.d("generateBackupFile", "AppDatabasePath:- " + AppDatabasePath);
            File data = Environment.getDataDirectory();
            Log.d("generateBackupFile", "data:- " + data);
            File currentDB = new File(data, AppDatabasePath);
            Log.d("generateBackupFile", "currentDB:- " + currentDB);
            String BackupDbFileName = "dbfile.db";
            File backupDB = new File(dir, BackupDbFileName);
            Log.d("generateBackupFile", "backupDB:- " + backupDB);

            if (currentDB.exists()) {
                Log.d("generateBackupFile", "IF:- ");
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
            Toast.makeText(this, "Data has been written to Report File", Toast.LENGTH_SHORT).show();
            byte_array = ClsGlobal.getStringFile(backupDB);

        } catch (Exception e) {
            Toast.makeText(this, "Exception", Toast.LENGTH_SHORT).show();
        }

        //save to our app folder
        //get that file
        //convert to byte arry (see example in fTouch)

    }

    void backupDatabaseAPI(String backUpType, String fileString, String ext, String fileName) {
        ClsBackupTypeParams objClsBackupTypeParams = new ClsBackupTypeParams();
        objClsBackupTypeParams.setCustomerID(_customerId);
        objClsBackupTypeParams.setBackupType(backUpType);
        objClsBackupTypeParams.setProductName(ClsGlobal.AppName);
        objClsBackupTypeParams.setAppVersion(ClsGlobal.getApplicationVersion(SettingActivity.this));
        objClsBackupTypeParams.setAppType(ClsGlobal.getApplicationVersion(SettingActivity.this));
        objClsBackupTypeParams.setIMEINumber(ClsGlobal.getIMEIno(SettingActivity.this));
        objClsBackupTypeParams.setDeviceInfo(ClsGlobal.getDeviceInfo(SettingActivity.this));
        objClsBackupTypeParams.setRemark("Backup @".concat(ClsGlobal.getEntryDate()));
        objClsBackupTypeParams.setFileName(fileName);
        objClsBackupTypeParams.setExtentsion(ext);
        objClsBackupTypeParams.setData(fileString);

        Gson gson = new Gson();
        String jsonInString = gson.toJson(objClsBackupTypeParams);
        Log.d("Result", "backupDatabaseAPI- " + jsonInString);

        InterfaceBackupType interfaceBackupType = ApiClient.getRetrofitInstance().create(InterfaceBackupType.class);
        Log.e("--URL--", "interfaceDesignation: " + interfaceBackupType.toString());
        Call<ClsBackupTypeParams> call = interfaceBackupType.postBackup(objClsBackupTypeParams);
        Log.e("--URL--", "************************  before call : " + call.request().url());
        ProgressDialog pd =
                ClsGlobal._prProgressDialog(SettingActivity.this, "Working...", true);
        pd.show();


        call.enqueue(new Callback<ClsBackupTypeParams>() {
            @Override
            public void onResponse(Call<ClsBackupTypeParams> call, Response<ClsBackupTypeParams> response) {
                pd.dismiss();
                if (response.body() != null) {
                    String _response = response.body().getSuccess();
                    if (_response.equals("1")) {
                        Toast.makeText(SettingActivity.this, "Successfully backup", Toast.LENGTH_SHORT).show();
                    } else if (_response.equals("0")) {
                        Toast.makeText(SettingActivity.this, "Error while backup database", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server error...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsBackupTypeParams> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong,please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE: {
                ClsPermission.checkPermission(requestCode, permissions, grantResults, SettingActivity.this);
                return;
            }

        }

    }


}
