package com.demo.nspl.restaurantlite.AsyncTask;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.FileUploader;
import com.demo.nspl.restaurantlite.classes.FileZipOperation;

import java.io.File;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.CreateProgressDialog;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.LocalBackup;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.SharedPreferencesPath;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.SharedPreferencesPathBackupFolder;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.copyDirectory;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.deleteUnWanted_Bkp;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.generateBackUpFileName;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getCurruntDateTime;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getEntryDateFormat;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.hideProgress;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.showProgress;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.updateProgress;

public class BackUpCloudAsyncTask extends AsyncTask<String, Void, String> {

    private ProgressDialog loading;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String zipPath = SDPath + "/" + LocalBackup + "/";
    private String _customerId = "";
    private String result = "";
    private String BackupZipFilePath = "", currenbBkpFileName = "";

    public BackUpCloudAsyncTask(Context context, String _customerId) {
        this.context = context;
        this._customerId = _customerId;
//        ClsGlobal clsGlobal = new ClsGlobal();
//        clsGlobal.SetOnBackupFinish(this);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        loading = ClsGlobal._prProgressDialog(context
                , "Preparing Zip for your Backup file's", false);
        loading.show();
        Log.d("onPreExecute", "onPreExecute call");
    }


    @Override
    protected String doInBackground(String... strings) {

        manuallyBackup();


        return result;
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d("onPostExecute", "onPostExecute call");

        if (loading.isShowing()) {
            loading.dismiss();
        }

        if (result.equalsIgnoreCase("successfully")) {

            CreateProgressDialog(context);
            showProgress("Uploading Backup");
            FileUploader fileUploader = new FileUploader(new File(BackupZipFilePath),
                    "Manual", context);

            fileUploader.SetCallBack(new FileUploader.FileUploaderCallback() {
                @Override
                public void onError() {

                    // Delete unwanted backup files.
                    deleteUnWanted_Bkp(new File(ClsGlobal.SDPath + "/" + LocalBackup + "/"));
                    hideProgress();

                    ClsGlobal.InsertBackupLogs("Manual Backup Failed",
                            "Backup is Failed", context);

                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) + " - " +
                            "Backup is Failed onError (Manual Cloud Backup). \n");

                    Toast.makeText(context, "Backup failed to cloud",
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFinish(String responses) {
                    hideProgress();

                    if (responses != null) {
                        if (responses.contains("1")) {

                            ClsGlobal.InsertBackupLogs("Manual Backup Successfully",
                                    "Backup is Successfully Uploaded to Cloud ", context);
                            Toast.makeText(context, "Backup successfully To Cloud", Toast.LENGTH_LONG).show();

                            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) + " - " +
                                    "Backup Completed Successfully (Manual Cloud Backup). FileName:-"
                                    + currenbBkpFileName + "\n");
                        } else if (responses.contains("0")) {
                            ClsGlobal.InsertBackupLogs("Manual Backup Failed",
                                    "Backup is Failed", context);
                            Toast.makeText(context, "Backup failed To Cloud", Toast.LENGTH_LONG).show();

                            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) + " - " +
                                    "Backup Failed to Upload to server (Manual Cloud Backup). \n");

                        }

                        // Delete unwanted backup files.
                        deleteUnWanted_Bkp(new File(ClsGlobal.SDPath + "/" + LocalBackup + "/"));


                    }

                }

                @Override
                public void onProgressUpdate(int currentpercent, int totalpercent, String msg) {
                    updateProgress(totalpercent, "Uploading Backup", msg);
                }
            });
        } else {
            Toast.makeText(context, "Zipping the Backup File's failed!", Toast.LENGTH_SHORT).show();
        }


    }

    // - not req this line; backupDatabaseAPI("Database", byte_array, ".db", "Database".concat(ClsGlobal.getEntryDate()), _customerId, context);

    void manuallyBackup() {

        try {

            // Logs
            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) + " - " +
                    "Manual Cloud Backup Started. \n");
//            deleteAllFiles_from_folder(new File(ClsGlobal.SDPath + "/" + ClsGlobal.AppFolderName));

            ClsGlobal.generateBackupFile();//this is for only generate  .db file

            Log.e("--TakeBackUp--", "Step:1");

            copyDirectory(new File(SharedPreferencesPath),
                    new File(SharedPreferencesPathBackupFolder));

            File _saveLocation = Environment.getExternalStorageDirectory();
            Log.d("camera", "filepath:- " + _saveLocation);

            Log.e("--TakeBackUp--", "Step:2");

            File dir = new File(_saveLocation.getAbsolutePath() + "/" + ClsGlobal.AppFolderName + "/");
            Log.d("generateBackupFile", "dir:- " + dir);

            Log.e("--TakeBackUp--", "Step:3");

            // Logs
            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) + " - " +
                    "Creating Zip file  (Manual Cloud Backup). \n");

            currenbBkpFileName = generateBackUpFileName("ManualCloudBkp")
                    .replace("/", "_");

            if (FileZipOperation.zip(dir.getAbsolutePath(), zipPath,
                    currenbBkpFileName, true)) {
                Log.d("generateBackupFile", "IF:- ");

                Log.e("--TakeBackUp--", "Step:4");

                // Logs
                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) + " - " +
                        "Backup Zip file Created (Manual Cloud Backup). \n");

                result = "successfully";

                Log.e("--TakeBackUp--", "Step:5");

            } else {
                // Logs
                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) + " - " +
                        "Failed to create Backup Zip file (Manual Cloud Backup). \n");
                Log.e("--TakeBackUp--", "Step:6");
                result = "failed";
            }

            Log.e("--TakeBackUp--", "Step:7");

//        File zipFile = new File(zipPath + "/ShapBKP.zip");

            BackupZipFilePath = zipPath + "/" + currenbBkpFileName;

            Log.e("--TakeBackUp--", "Step:8");

//        String zipFileByteArray = ClsGlobal.getStringFile(zipFile);

            Log.e("--TakeBackUp--", "Step:9");

//        backupDatabaseAPI("Document", zipFileByteArray,
//        ".zip", "ShapBKP".concat(ClsGlobal.getEntryDate()), _customerId, context);

            Log.e("--TakeBackUp--", "Step:10");
        } catch (Exception e) {
            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) + " - Error:- " +
                    e.getMessage() + " (Manual Cloud Backup). \n");
        }


    }

}
