package com.demo.nspl.restaurantlite.backGroundTask;

import android.app.NotificationManager;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.DriveServiceHelper;
import com.demo.nspl.restaurantlite.UploadFileToServer.ConnectionCheckBroadcast;
import com.demo.nspl.restaurantlite.classes.FileZipOperation;

import java.io.File;
import java.io.IOException;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.Create_OneTimeWorkRequest;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.GetNetworkTypeStatus;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.GoogleDrive_Bkp_Process_Notification_id;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.ProgressBarNotification;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.STORAGE_PERMISSIONS;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.SharedPreferencesPath;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.SharedPreferencesPathBackupFolder;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.copyDirectory;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.generateBackUpFileName;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.generateBackupFile;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getCurruntDateTime;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getEntryDateFormat;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.zipPath;


public class GoogleDriveBackupTask extends Worker {

    private Context context;
    private String NetworkTypeStatus = "", getNetworkTypeStatus = "",
            currenbBkpFileName = "", mode = "", backupFilePath = "",BackUpPath ="";
    private final String TAG = GoogleDriveBackupTask.class.getSimpleName();
    private NotificationCompat.Builder notificationBuilder = null;
    private ConnectionCheckBroadcast mConnectionCheckBroadcast;

    public GoogleDriveBackupTask(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;

    }

    @NonNull
    @Override
    public Result doWork() {
        // get mode manual or Auto.
        mode = getInputData().getString("Mode");
        BackUpPath = getInputData().getString("BackUpPath");
        Log.e(TAG, "mode call:- " + mode);

        if (ClsGlobal.hasPermissions(context,STORAGE_PERMISSIONS)) {
            mConnectionCheckBroadcast = new ConnectionCheckBroadcast("GoogleDriveBkp");
            mConnectionCheckBroadcast.setData(mode);

            context.registerReceiver(mConnectionCheckBroadcast, new IntentFilter(
                    "android.net.conn.CONNECTIVITY_CHANGE"));

            if (mode.equalsIgnoreCase("Auto")) {
                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " - Auto Google Drive Backup Started. \n");
            }


            Log.e(TAG, "doWork call:- ");

            if (ClsGlobal.CheckInternetConnection(context)) {
                NetworkTypeStatus = ClsGlobal.CheckNetWorkType(context);
                Log.e(TAG, "NetworkTypeStatus:- " + NetworkTypeStatus);

                // get NetworkType settings from SharedPreferences.
                getNetworkTypeStatus = GetNetworkTypeStatus(context);
                Log.e(TAG, "getNetworkTypeStatus:- " + getNetworkTypeStatus);

                if (NetworkTypeStatus != null && !NetworkTypeStatus.equalsIgnoreCase("")
                        && getNetworkTypeStatus != null && !getNetworkTypeStatus.equalsIgnoreCase("")) {
                    Log.e("getNetworkTypeStatus", "(NetworkTypeStatus != getNetworkTypeStatus != null");
                    Log.e(TAG, "NetworkTypeStatus:- " + NetworkTypeStatus);
                    if (getNetworkTypeStatus.equalsIgnoreCase("WI-FI") &&
                            NetworkTypeStatus.equalsIgnoreCase("WIFI")) {
                        Log.e("getNetworkTypeStatus", "getNetworkTypeStatus inside");
                        TakeBackUp();

                    } else if (getNetworkTypeStatus.equalsIgnoreCase("WI-FI OR CELLULAR")) {
                        Log.e("getNetworkTypeStatus", "Wi-fi or cellular inside");
                        TakeBackUp();
                    }

//                TakeBackUp();


                }

//            TakeBackUp();

            } else {
                Log.e("getNetworkTypeStatus", "else CheckInternetConnection");

                ClsGlobal.BackupNotification("Auto Google Drive Backup Failed",
                        "No Internet Connection Found while Backing up At this Time: "
                                + getEntryDateFormat(getCurruntDateTime()), context
                        , ClsGlobal.GoogleDrive_Bkp_Notification_id);

                ClsGlobal.InsertBackupLogs("Auto Google Drive Backup Failed",
                        "No Internet Connection Found while Backing up", context);

                if (mode.equalsIgnoreCase("Manual")) {
                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " - Manual Google Drive Backup Failed. " +
                            "No Internet Connection Found while Backing up waiting for Next Internet " +
                            "Connectivity. \n");
                } else if (mode.equalsIgnoreCase("Auto")) {
                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " - Auto Google Drive Backup Failed. " +
                            "No Internet Connection Found while Backing up waiting " +
                            "for Next Internet Connectivity.\n");
                }


                ClsGlobal.cancelWorkerTask("GoogleDriveBkp");

                Data data = new Data.Builder()
                        .putString("Mode", mode)
                        .build();

                // if there is no InternetConnection then create new Worker.
                Constraints myConstraints = new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build();


                Create_OneTimeWorkRequest(GoogleDriveBackupTask.class,
                        "GoogleDriveBackup", "KEEP", data, myConstraints);

                if (getNetworkTypeStatus.equalsIgnoreCase("WI-FI")) {

                    notificationBuilder = ProgressBarNotification("Google Drive Backup pending",
                            "fTouch Pos is Waiting for Wifi", context,
                            GoogleDrive_Bkp_Process_Notification_id, "Show Percentage");
                } else {
                    notificationBuilder = ProgressBarNotification("Google Drive Backup pending",
                            "fTouch Pos is Waiting for Internet", context
                            , GoogleDrive_Bkp_Process_Notification_id, "Show Percentage");
                }

            }

            // unregisterReceiver ConnectionCheckBroadcast after Work is finish.
            context.unregisterReceiver(mConnectionCheckBroadcast);
        }



        return Result.success();
    }


    private void TakeBackUp() {

        try {
            Log.e(TAG, "TakeBackUp:- ");

            generateBackupFile();

            copyDirectory(new File(SharedPreferencesPath),
                    new File(SharedPreferencesPathBackupFolder));

            File _saveLocation = Environment.getExternalStorageDirectory();
            Log.e(TAG, "filepath:- " + _saveLocation);
            File dir = new File(_saveLocation.getAbsolutePath()
                    + "/" + ClsGlobal.AppFolderName + "/");
            Log.e(TAG, "dir:- " + dir);

            // Create fTouchPOSLocalBkp Backup folder if not exist.
            if (!new File(zipPath).exists()){
                try {
                    new File(zipPath).mkdir();
                }catch (Exception e){
                    Log.e("generateBackupFile", "dir:- " + e.getMessage());
                }
            }

            if (mode.equalsIgnoreCase("Auto")) {
                currenbBkpFileName = generateBackUpFileName("AutoGDriveBkp")
                        .replace("/", "_");
                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " Creating Zip Backup file (Auto Google Drive Backup). \n");
            }

            if (mode.equalsIgnoreCase("Auto")) {
                // for Auto Backup.
                Log.e(TAG, "currenbBkpFileName:- " + currenbBkpFileName);
                if (FileZipOperation.zip(dir.getAbsolutePath(), zipPath,
                        currenbBkpFileName, true)) {

                    Log.e("generateBackupFile", "IF:- ");

                    Log.e(TAG, "zip:- ");
                    backupFilePath = zipPath + "/" + currenbBkpFileName;


                    if (mode.equalsIgnoreCase("Auto")) {
                        ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                " Backup Zip file Created (Auto Google Drive Backup). File Name:- "
                                + currenbBkpFileName + "\n");
                        Log.e(TAG, "zip  Auto:- ");
                    }


                    notificationBuilder = ProgressBarNotification("Google Drive Backup",
                            "Preparing Backup", context
                            , GoogleDrive_Bkp_Process_Notification_id, "Show Percentage");


                    Log.e(TAG, "DriveServiceHelper :- ");
                    DriveServiceHelper driveServiceHelper = new DriveServiceHelper(mode);
                    driveServiceHelper.Initialize_DriveServiceHelper(context);
                    driveServiceHelper.Prepare_Upload_File(new File(backupFilePath),
                            context,
                            notificationBuilder);


                }
            }else if (mode.equalsIgnoreCase("Manual")) {
                // for Manual Backup.

                notificationBuilder = ProgressBarNotification("Google Drive Backup",
                        "Preparing Backup", context
                        , GoogleDrive_Bkp_Process_Notification_id, "Show Percentage");

                if (new File(BackUpPath).exists()){
                    Log.e(TAG, "DriveServiceHelper :- ");
                    DriveServiceHelper driveServiceHelper = new DriveServiceHelper(mode);
                    driveServiceHelper.Initialize_DriveServiceHelper(context);
                    driveServiceHelper.Prepare_Upload_File(new File(BackUpPath),
                            context,
                            notificationBuilder);
                }else {
                    ClsGlobal.cancelWorkerTask("GoogleDriveBkp");


                    // cancel Notification.
                    NotificationManager notificationManager = (NotificationManager)
                            context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(GoogleDrive_Bkp_Process_Notification_id);
                }




            }



        } catch (IOException e) {
            Log.e(TAG, "Exception" + e.getMessage());
//            e.printStackTrace();
        }

    }
}
