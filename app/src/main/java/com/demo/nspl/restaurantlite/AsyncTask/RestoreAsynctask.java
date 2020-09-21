package com.demo.nspl.restaurantlite.AsyncTask;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.HttpDownloadUtility;
import com.demo.nspl.restaurantlite.classes.FileZipOperation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class RestoreAsynctask extends AsyncTask<String, Void, String> {

    private String url = "";
    private ProgressDialog loading;
    @SuppressLint("StaticFieldLeak")
    private Context context;


    public RestoreAsynctask(String url, Context context) {
        this.url = url;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        loading = ClsGlobal._prProgressDialog(context
                , "Restoring Your BackUp...", false);
        loading.show();
        Log.d("onPreExecute", "onPreExecute call");
    }

    @Override
    protected String doInBackground(String... voids) {
        String resullt = "";

        try {
            Log.d("doInBackground", "doInBackground call");
            String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String zipPath = SDPath + "/ShapBackup";
            Log.d("zipFilePath", "doInBackground call");

            String zipFilePath = HttpDownloadUtility.downloadFile(url, zipPath);

            Log.d("zipFilePath", "run: " + zipFilePath);
            Log.d("zipFilePath", "STEP--1");
            FileZipOperation.unzip(zipFilePath, SDPath + "/" + ClsGlobal.AppFolderName);
            Log.d("zipFilePath", "STEP--2");

//            Thread.sleep(3000);
            resullt = RESTORE();

            Log.d("zipFilePath", "STEP--3");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return resullt;
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (loading.isShowing()) {
            loading.dismiss();
        }

        switch (result) {
            case "":
                Toast.makeText(context, "Database Restored failed", Toast.LENGTH_SHORT).show();
                break;
            case "successfully":
//                List<String> InventoryOrderMasterNew = ClsInventoryOrderDetail.
//                getInventoryOrderDetailColumns(context);
//
//                Gson gson2 = new Gson();
//                String jsonInString2 = gson2.toJson(InventoryOrderMasterNew);
//                Log.e("--BillTo--", "Final:--- " + jsonInString2);

                ClsGlobal.add_Edit_DataBase_Columns(context);
                Toast.makeText(context, "Database Restored successfully", Toast.LENGTH_SHORT).show();
                break;
            case "failed":
                Toast.makeText(context, "Database Restored failed", Toast.LENGTH_SHORT).show();
                break;

        }

        Log.d("onPostExecute", "onPostExecute call");
    }


    private String RESTORE() {
        String result = "";
        try {

            File _saveLocation = Environment.getExternalStorageDirectory();
            File root = new File(_saveLocation.getAbsolutePath() + "/" + ClsGlobal.AppFolderName + "/Backup/");
            if (!root.exists()) {
                root.mkdirs();
            }

            String currentDBPath = ClsGlobal.AppDatabasePath.concat(ClsGlobal.Database_Name);
            String BackupDbFileName = "dbfile.db";

            File data = Environment.getDataDirectory();
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(root, BackupDbFileName);

            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(backupDB).getChannel();
                FileChannel dst = new FileOutputStream(currentDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

                result = "successfully";
                //  Toast.makeText(context, "Database Restored successfully", Toast.LENGTH_SHORT).show();

            } else {
                result = "failed";
                //  Toast.makeText(context, "Database File Not found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("e", e.getMessage());
            result = "failed";
            // Toast.makeText(context, "Error Exception in Restore", Toast.LENGTH_SHORT).show();
        }

        return result;
    }
}
