package com.demo.nspl.restaurantlite.AsyncTask;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.classes.FileZipOperation;

import java.io.File;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.LocalBackup;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.SharedPreferencesPath;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.SharedPreferencesPathBackupFolder;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.copyDirectory;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.deleteUnWanted_Bkp;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.generateBackUpFileName;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.generateBackupFile;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getCurruntDateTime;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getEntryDateFormat;

public class BackUpAsyncTask extends AsyncTask<String, Void, String> {

    private ProgressDialog loading;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String zipPath = SDPath + "/" + LocalBackup + "/";
    private String Path = "",currenbBkpFileName = "";

    public BackUpAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        loading = ClsGlobal._prProgressDialog(context
                , "Backing up to Local...", false);
        loading.show();
        Log.d("onPreExecute", "onPreExecute call");
    }

    @Override
    protected String doInBackground(String... strings) {

        String result = "";
        ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                " - Local Backup Started. \n");


        try {

            // Deleting all Bkp zip file's from fTouchPOSLocalBkp folder.
//            deleteAllFiles_from_folder(new File(zipPath));

            generateBackupFile();

            copyDirectory(new File(SharedPreferencesPath),
                    new File(SharedPreferencesPathBackupFolder));

            File _saveLocation = Environment.getExternalStorageDirectory();
            Log.d("camera", "filepath:- " + _saveLocation);
            File dir = new File(_saveLocation.getAbsolutePath() + "/"
                    + ClsGlobal.AppFolderName + "/");

            Log.d("generateBackupFile", "dir:- " + dir);

            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                    " - Creating Zip file (Local Backup). \n");

            currenbBkpFileName  = generateBackUpFileName("LocalBkp")
                    .replace("/","_");

            if (FileZipOperation.zip(dir.getAbsolutePath(), zipPath,
                    currenbBkpFileName, true)) {

                Path = zipPath + currenbBkpFileName;
                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " - Backup Zip file Created (Local Backup). \n");

                Log.d("generateBackupFile", "IF:- ");
                result = "successfully";
            } else {
                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " - Failed to Create Backup Zip file (Local Backup). \n");

                result = "failed";
            }

        } catch (Exception e) {
            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) + " - Error:- " +
                    e.getMessage()+ " (Local Backup). \n");
        }




        return result;
    }





    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (loading.isShowing()) {
            loading.dismiss();
        }


        switch (result) {
            case "":
                ClsGlobal.InsertBackupLogs("Manual Backup Failed",
                        "Backup is Failed to Local Device", context);
                Toast.makeText(context, "Database Backup failed", Toast.LENGTH_SHORT).show();
                break;
            case "successfully":
                ClsGlobal.InsertBackupLogs("Manual Backup Successfully",
                        "Backup is Successfully to Local Device To this Path:- " + Path, context);

                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " - Backup Completed Successfully (Local Backup). FileName:- " +
                        currenbBkpFileName + "\n");

                Toast.makeText(context, "Database Backup successfully To Path:- "
                        + Path, Toast.LENGTH_LONG).show();

                break;
            case "failed":
                ClsGlobal.InsertBackupLogs("Manual Backup Failed",
                        "Backup is Failed to Local Device", context);
                Toast.makeText(context, "Database Backup failed", Toast.LENGTH_SHORT).show();
                break;



        }

        // Delete unwanted backup files.
        deleteUnWanted_Bkp(new File(SDPath + "/" + LocalBackup +"/"));

        Log.d("onPostExecute", "onPostExecute call");
    }


}
