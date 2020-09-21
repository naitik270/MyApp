package com.demo.nspl.restaurantlite.AsyncTask;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.backGroundTask.GoogleDriveBackupTask;
import com.demo.nspl.restaurantlite.classes.FileZipOperation;

import java.io.File;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.LocalBackup;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.SharedPreferencesPath;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.SharedPreferencesPathBackupFolder;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.copyDirectory;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.generateBackUpFileName;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.generateBackupFile;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getCurruntDateTime;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getEntryDateFormat;

public class GDriveBackUpAsyncTask extends AsyncTask<String, Void, String> {


    private ProgressDialog loading;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String zipPath = SDPath + "/" + LocalBackup + "/";
    private String _customerId = "";
    private String result = "";
    private String backupFilePath = "", currenbBkpFileName = "";

    public GDriveBackUpAsyncTask(Context context) {
        this.context = context;
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


        ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                " - Manual Google Drive Backup Started. \n");

        String result = "";
        try {
            generateBackupFile();


            copyDirectory(new File(SharedPreferencesPath),
                    new File(SharedPreferencesPathBackupFolder));

            File _saveLocation = Environment.getExternalStorageDirectory();

            File dir = new File(_saveLocation.getAbsolutePath()
                    + "/" + ClsGlobal.AppFolderName + "/");


            currenbBkpFileName = generateBackUpFileName("ManualGDriveBkp")
                    .replace("/", "_");

            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                    " Creating Zip Backup file (Manual Google Drive Backup). \n");

            if (FileZipOperation.zip(dir.getAbsolutePath(), zipPath,
                    currenbBkpFileName, true)) {

                backupFilePath = zipPath + "/" + currenbBkpFileName;

                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " Backup Zip file Created (Manual Google Drive Backup). File Name:- " +
                        currenbBkpFileName + " \n");

                result = "successfully";
            } else {
                result = "failed";

                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " Failed To Create Backup Zip file (Manual Google Drive Backup). File Name:- " +
                        currenbBkpFileName + " \n");
            }

        } catch (Exception e) {
            Log.e("generateBackupFile", "Exception:- " + e.getMessage());

            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                    " Failed To Create Backup Zip file (Manual Google Drive Backup). File Name:- " +
                    currenbBkpFileName + " \n" +"Error:- " + e.getMessage());
        }


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

            ClsGlobal.cancelWorkerTask("GoogleDriveBkp");

            Data data = new Data.Builder()
                    .putString("Mode","Manual")
                    .putString("BackUpPath",backupFilePath)
                    .build();

            // if there is no InternetConnection then create new Worker.
            Constraints myConstraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();


            OneTimeWorkRequest oneTimeWorkRequest =
                    new OneTimeWorkRequest.Builder(GoogleDriveBackupTask.class)
                            .setInputData(data)
                            .setConstraints(myConstraints)
                            .build();

            WorkManager.getInstance().enqueueUniqueWork("GoogleDriveBackup"
                    , ExistingWorkPolicy.KEEP, oneTimeWorkRequest);



        }

    }
}
