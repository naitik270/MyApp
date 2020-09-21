package com.demo.nspl.restaurantlite.AsyncTask;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.work.WorkManager;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.SendEmailUtility.Scheduler;
import com.demo.nspl.restaurantlite.SendEmailUtility.SharedPreferenceTime;
import com.demo.nspl.restaurantlite.activity.SHAP_Lite_Activity;
import com.demo.nspl.restaurantlite.backGroundTask.SendEmailTask;
import com.demo.nspl.restaurantlite.classes.FileZipOperation;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.Delete_Unwanted_SharePrefrence_Files;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.RESTORE;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.SharedPreferencesPath;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.SharedPreferencesPathBackupFolder;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.deleteAllFile;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.deleteAllFiles_from_folder;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getCurruntDateTime;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getEntryDateFormat;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.set_current_Day_Remainder;

public class LocalRestoreAsyncTask extends AsyncTask<String, Void, String> {

    private ProgressDialog loading;
    @SuppressLint("StaticFieldLeak")
    private Context context;

    private String path = "",restoreFileName = "";
    private static final String mPreferncesFileName = "AutoBackUpSettings";
    private SharedPreferences mPreferencesAutoBackUp;
    SharedPreferenceTime mSharedPreferenceTime;


    public LocalRestoreAsyncTask(Context context, String path) {
        this.context = context;
        this.path = path;
        this.restoreFileName =  new File(path).getName();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        loading = ClsGlobal._prProgressDialog(context
                , "Restoring your Backup...", false);
        loading.show();
        Log.d("onPreExecute", "onPreExecute call");
    }


    @Override
    protected String doInBackground(String... strings) {
        ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                " - Restore Backup Started (Restore From Local Device). \n");

        // Delete SharedPreferences files from SettingsBackup folder.
        deleteAllFiles_from_folder(new File(SharedPreferencesPathBackupFolder));
//        deleteAllFiles_from_folder(new File(ClsGlobal.SDPath + "/" + ClsGlobal.AppFolderName));

        // Delete SharedPreferences files from our app folder.
        deleteAllFile(context);

        ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                " - Restore Backup Unzipping Backup file Started (Restore From Local Device). \n");

        if (FileZipOperation.unzip(path,
                ClsGlobal.SDPath + "/" + ClsGlobal.AppFolderName)){

            Log.e("check", "Backup Unzipping Backup Successfully True");
            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                    " - Restore Backup Unzipping Backup file Successfully (Restore From Local Device). \n");
        }else {

            Log.e("check", "Backup Unzipping Backup False");
            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                    " - Restore Backup Unzipping Backup file Failed (Restore From Local Device). \n");
        }


        Log.e("path", path);
        String result = RESTORE();

        try {


            Delete_Unwanted_SharePrefrence_Files(SharedPreferencesPathBackupFolder);

            ClsGlobal.copyDirectory(new File(SharedPreferencesPathBackupFolder),
                    new File(SharedPreferencesPath));

        } catch (Exception e) {
            e.printStackTrace();
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
            case "failed":
                Toast.makeText(context, "Database Restored failed", Toast.LENGTH_SHORT).show();
                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " - Restore Backup Failed (Restore From Local Device). FileName: "
                        +restoreFileName+ "\n");
                break;
            case "successfully":

                // Cancel Daily logout and AutoLocal Bkp Task.
                ClsGlobal.cancelWorkerTask(ClsGlobal.AppPackageName
                        .concat("DailyTaskLogoutRetail"));
                ClsGlobal.cancelWorkerTask(ClsGlobal.AppPackageName
                        .concat(".DailyTaskAutoLocalBkp_ftouchPos"));

                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " - Restore Backup Successfully (Restore From Local Device). FileName: "
                        +restoreFileName+ "\n");
                Toast.makeText(context, "Your app will Restart to Restore Settings.", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Database Restored successfully", Toast.LENGTH_SHORT).show();

                mPreferencesAutoBackUp = context.getSharedPreferences(mPreferncesFileName, MODE_PRIVATE);
                mSharedPreferenceTime = new SharedPreferenceTime(context);


                // Set current Day in SharedPreferences file
                // Show that the Backup Remainder Dialog Box do
                // not open every time when user restore.
                set_current_Day_Remainder(context);


                // Setting up Auto Backup.
                if (mPreferencesAutoBackUp.getString("AutoBackUpTime", "") != null
                        && !mPreferencesAutoBackUp.getString("AutoBackUpTime", "")
                        .equalsIgnoreCase("")) {
                    String getAutoBackUpTime = mPreferencesAutoBackUp.getString("AutoBackUpTime"
                            , "");

                    switch (getAutoBackUpTime) {

                        case "Daily":
                            ClsGlobal.SetAutoBackUp(1, "REPLACE");
                            break;
                        case "Weekly":
                            ClsGlobal.SetAutoBackUp(7, "REPLACE");
                            break;
                        case "Monthly":
                            ClsGlobal.SetAutoBackUp(30, "REPLACE");
                            break;
                        case "Yearly":
                            ClsGlobal.SetAutoBackUp(365, "REPLACE");
                            break;
                        case "Never":
                            WorkManager.getInstance().cancelUniqueWork(
                                    ClsGlobal.AppPackageName.concat("AutoBackUp"));
                            break;
                    }

                }

                // Setting Auto Email
                Scheduler.setReminder(context, SendEmailTask.class
                        , mSharedPreferenceTime.get_hour(), mSharedPreferenceTime.get_min());

                @SuppressLint("StaticFieldLeak")
                AsyncTask<Void, Void, Void> databaseAdd_edit = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {

                            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                    " - Restore add_Edit_DataBase_Columns Started (Restore From Local Device). \n");
                            ClsGlobal.add_Edit_DataBase_Columns(context);
                            Log.d("--customerReport--", "doInBackground: ");

                            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                    " - Restore add_Edit_DataBase_Columns Successfully." +
                                    " (Restore From Local Device). \n");

                        } catch (Exception e) {
                            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                    " - Restore add_Edit_DataBase_Columns Failed. (Restore From Local Device). \n"
                                    + "Error: " + e.getMessage());
                        }

                        return null;
                    }

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        loading = ClsGlobal._prProgressDialog(context
                                , "Please Wait...", false);
                        loading.show();
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        Log.d("--customerReport--", "aVoid: ");

                        if (loading.isShowing()) {
                            loading.dismiss();
                        }



//                        // Delete Downloaded backup file.
//                        Thread thread = new Thread(() -> {
//                            try {
//                                Log.d("--customerReport--", "thread: ");
//                                new File(zipPath).delete();
//
//
//                            } catch (Exception e) {
//                                Log.d("--customerReport--", "aVoid: " + e.getMessage());
//                            }
//
//                        });
//
//                        thread.start();

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("Restart App");
                        alertDialog.setMessage("Your app will restart to restore Settings.");
                        alertDialog.setIcon(R.drawable.ic_restore);
                        alertDialog.setPositiveButton("OK", (dialog, id) -> {

                            Intent mStartActivity = new Intent(context, SHAP_Lite_Activity.class);
                            int mPendingIntentId = 123456;
                            PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity,
                                    PendingIntent.FLAG_CANCEL_CURRENT);
                            AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                            System.exit(0);

                        });
                        alertDialog.setCancelable(false);
                        alertDialog.show();


                    }
                };

                databaseAdd_edit.execute();


                break;

        }

        Log.d("onPostExecute", "onPostExecute call");
    }

}
