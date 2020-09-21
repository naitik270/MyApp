package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.nspl.restaurantlite.AsyncTaskReport.ExcelDataInsertAsyncTask;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsCustomerMaster;
import com.demo.nspl.restaurantlite.classes.ClsExportToExcel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Activity_ImportCustomer extends AppCompatActivity {


    Button btn_download_format;
    Button btn_browse;
    ProgressBar progress_bar;
    static TextView txt_total_customer;
    static TextView txt_save_customer;
    static TextView txt_existing;
    static TextView txt_failed;
    TextView txt_excel_export;
    public List<ClsCustomerMaster> lstClsCustomerMasters = new ArrayList<>();

    public static String getStatus = "";
    TextView txt_download_path;
    TextView txt_back;
    static LinearLayout ll_bottom;

    ProgressDialog loading;


    public static ClsCustomerMaster.ClsExcelImport objImportResult = new ClsCustomerMaster.ClsExcelImport();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_customer);


        main();

    }

    private void main() {


        txt_back = findViewById(R.id.txt_back);
        ll_bottom = findViewById(R.id.ll_bottom);
        txt_download_path = findViewById(R.id.txt_download_path);
        txt_save_customer = findViewById(R.id.txt_save_customer);
        txt_total_customer = findViewById(R.id.txt_total_customer);
        txt_failed = findViewById(R.id.txt_failed);
        txt_existing = findViewById(R.id.txt_existing);
        txt_excel_export = findViewById(R.id.txt_excel_export);


        progress_bar = findViewById(R.id.progress_bar);
        btn_download_format = findViewById(R.id.btn_download_format);
        btn_download_format.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFormat();
            }
        });

        btn_browse = findViewById(R.id.btn_browse);
        btn_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileBrowse();
            }
        });

        txt_excel_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportToExcel();
            }
        });

        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    void downloadFormat() {
        AssetManager assetManager = getAssets();
        String fileName = "DownloadFormat.xls";

        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fileName);
            File _saveLocation = Environment.getExternalStorageDirectory();
            File dir = new File(_saveLocation.getAbsolutePath() + "/ImportFormat/");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String StoredPath = dir.getAbsolutePath() + "/" + fileName;
            out = new FileOutputStream(StoredPath);

            copyFile(in, out);
            txt_download_path.setVisibility(View.VISIBLE);
            txt_download_path.setText(StoredPath);


            Log.e("--copyAssets--", "Path: " + _saveLocation.getAbsolutePath());

        } catch (IOException e) {
            Log.e("--copyAssets--", "IOException: " + e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e("--copyAssets--", "step 5" + e.getMessage());
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    Log.e("--copyAssets--", "step 11" + e.getMessage());

                    // NOOP
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static void refreshCount() {
        txt_total_customer.setText(String.valueOf(objImportResult.get_totalRecords()));
        txt_save_customer.setText(String.valueOf(objImportResult.get_successRecords()));
        txt_existing.setText(String.valueOf(objImportResult.get_alreadyExists()));
        txt_failed.setText(String.valueOf(objImportResult.get_failedRecords()));

        if (objImportResult.get_status().equalsIgnoreCase("DATA INSERTED SUCCESSFULLY")) {
            ll_bottom.setVisibility(View.VISIBLE);
        } else {
            ll_bottom.setVisibility(View.GONE);
        }
    }
    public void exportToExcel() {

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, String> asyncTask =
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {

                        List<String> lstExportToExcel = new ArrayList<>();
                        lstExportToExcel.add("MOBILE NO");
                        lstExportToExcel.add("CUSTOMER NAME");
                        lstExportToExcel.add("EMAIL");
                        lstExportToExcel.add("COMPANY NAME");
                        lstExportToExcel.add("GST NO");
                        lstExportToExcel.add("ADDRESS");
                        lstExportToExcel.add("NOTE");
                        lstExportToExcel.add("STATUS");
                        lstExportToExcel.add("REMARK");

                        return ClsExportToExcel.createExcelSheet("CustomerFinalReportPath", lstExportToExcel,
                                "Final Customer Report", objImportResult.getLstClsCustomerMasters(),
                                null, null);
                    }

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        loading = ClsGlobal._prProgressDialog(Activity_ImportCustomer.this
                                , "Please Wait...", false);
                        loading.show();
                    }

                    @Override
                    protected void onPostExecute(String aVoid) {
                        super.onPostExecute(aVoid);
                        Log.d("--customerReport--", "aVoid: " + aVoid);

                        if (loading.isShowing()) {
                            loading.dismiss();
                        }


                        if (aVoid.equalsIgnoreCase("No Record Found")) {
                            Toast.makeText(Activity_ImportCustomer.this, "No record found.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Activity_ImportCustomer.this, "Export to excel successfully",
                                    Toast.LENGTH_SHORT).show();

                            ClsGlobal.openSnackBarExcelFile(Objects.requireNonNull(Activity_ImportCustomer.this), aVoid,
                                    "Final Customer Report");
                        }

                    }
                }.execute();

    }

    private int PICKFILE_RESULT_CODE = 1002;

    void openFileBrowse() {
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
            Uri uri = Uri.parse(Environment.getDownloadCacheDirectory().getPath());
            chooser.addCategory(Intent.CATEGORY_OPENABLE);
            chooser.setDataAndType(uri, "*/*");

            try {
                PackageManager pm = getApplicationContext().getPackageManager();
                if (chooser.resolveActivity(pm) != null) {
                    startActivityForResult(chooser, PICKFILE_RESULT_CODE);
                }

            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getApplicationContext(), "Please install a File Manager.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1002 && resultCode == RESULT_OK && data != null
                && requestCode != RESULT_CANCELED) {
            Log.e("--uri--", "requestCode == 1002");


            String PathHolder = data.getData().getPath();


            Log.d("--uri--", "PathHolder:: " + PathHolder);
//            Log.d("--uri--", "extension:: " + extension);


            objImportResult.setLstClsCustomerMasters(lstClsCustomerMasters);
            objImportResult.getLstClsCustomerMasters().clear();

            new ExcelDataInsertAsyncTask(Activity_ImportCustomer.this, PathHolder
                    , progress_bar).execute();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
//    +61 469822600

}
