package com.demo.nspl.restaurantlite.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.nspl.restaurantlite.AsyncTask.LocalRestoreAsyncTask;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.ListOfBkpFile.Activity_ListOfBkp_File;
import com.demo.nspl.restaurantlite.R;

public class RestoreActivity extends AppCompatActivity {

    private Button btn_restore_cloud, btn_restore_local;
    private Toolbar toolbar;
    private String _customerId = "";
    private int PICKFILE_RESULT_CODE = 1002;
    String SDPath = "";
    String LicenseType = "";
    Button btn_demo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn_restore_cloud = findViewById(R.id.btn_restore_cloud);
        btn_restore_local = findViewById(R.id.btn_restore_local);

        btn_demo = findViewById(R.id.btn_demo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        _customerId = getIntent().getStringExtra("_customerId");
        LicenseType = getIntent().getStringExtra("LicenseType");


/*
        if (LicenseType != null && LicenseType.equalsIgnoreCase("Free")) {
            btn_restore_cloud.setVisibility(View.GONE);
        } else {
            btn_restore_cloud.setVisibility(View.VISIBLE);
        }
*/


        SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        btn_restore_cloud.setOnClickListener(v -> {
            Intent intent = new Intent(RestoreActivity.this, ActivityRestoreList.class);
            intent.putExtra("_customerId", _customerId);
            startActivity(intent);


//            Toast.makeText(RestoreActivity.this, "Under Construction", Toast.LENGTH_SHORT).show();

        });

        btn_demo.setOnClickListener(v ->
                LocalRestoreFile());


        btn_restore_local.setOnClickListener(v -> {


//            LocalListFile();
//            LocalListFileNew();

            Intent intent = new Intent(RestoreActivity.this,
                    Activity_ListOfBkp_File.class);
            intent.putExtra("_customerId", _customerId);
            startActivity(intent);




        });

    }


    void LocalRestoreFile() {
        String manufacturer = android.os.Build.MANUFACTURER;
        Log.e("manufacturer", manufacturer);

        if (manufacturer.contains("samsung")) {
            Log.e("manufacturer", "samsung call");
            Intent intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
            intent.putExtra("CONTENT_TYPE", "*/*");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivityForResult(intent, PICKFILE_RESULT_CODE);
        } else {
            Intent chooser = new Intent(Intent.ACTION_GET_CONTENT);
            Uri uri = Uri.parse(Environment.getDownloadCacheDirectory().getPath().toString());
            chooser.addCategory(Intent.CATEGORY_OPENABLE);
            chooser.setDataAndType(uri, "*/*");

            try {
                PackageManager pm = getPackageManager();
                if (chooser.resolveActivity(pm) != null) {
                    startActivityForResult(chooser, PICKFILE_RESULT_CODE);
                }

            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "Please install a File Manager.",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1002 && resultCode == RESULT_OK && data != null
                && requestCode != RESULT_CANCELED) {
            Log.e("contains", "requestCode == 1002");

            Uri getUri = data.getData();
            Log.d("--uri--", "getUri_Restore:: " + getUri);

            if (getUri != null) {
                try {
                    Log.e("contains", "data.getData()");
                    String getPathFromFileManager = data.getData().getPath();
                    Log.d("--uri--", "getPathFromFileManager_Restore: " + getPathFromFileManager);

                    String FilePath = "";
                    Log.e("contains", getPathFromFileManager);

                    if ("content".equals(getUri.getScheme())) {
                        FilePath = ClsGlobal.getPathFromUri(RestoreActivity.this, getUri);
                    } else {
                        FilePath = getUri.getPath();
                    }


                    Log.d("--uri--", "FilePath_Restore: " + FilePath);

                    if (getPathFromFileManager != null && getPathFromFileManager.contains("primary:")) {
                        Log.e("contains", "contains inside");

                        if (FilePath != null && FilePath.contains(".zip")) {
                            new LocalRestoreAsyncTask(this, FilePath).execute();
                            Toast.makeText(this, FilePath, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "Incorrect file", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Log.e("contains", "contains outside");
                        if (getPathFromFileManager != null && getPathFromFileManager.contains(".zip")
                                || FilePath != null && FilePath.contains(".zip")) {

                            new LocalRestoreAsyncTask(this, FilePath).execute();
                            Toast.makeText(this, FilePath, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "Incorrect file", Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
