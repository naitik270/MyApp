package com.demo.nspl.restaurantlite.backGroundTask;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.DriveServiceHelper;
import com.demo.nspl.restaurantlite.Global.FileUploader;
import com.demo.nspl.restaurantlite.classes.FileZipOperation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import static android.content.Context.MODE_PRIVATE;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.Create_OneTimeWorkRequest;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.FtouchCloud_Bkp_Process_Notification_id;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.GetNetworkTypeStatus;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.ProgressBarNotification;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.STORAGE_PERMISSIONS;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.SharedPreferencesPath;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.SharedPreferencesPathBackupFolder;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.copyDirectory;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.generateBackUpFileName;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getCurruntDateTime;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getEntryDateFormat;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.zipPath;

public class AutoBackUpTask extends Worker {

    private Context context;
    private String NetworkTypeStatus = "", getNetworkTypeStatus = "", currenbBkpFileName = "";
    private String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();


    private SharedPreferences mPreferences;
    private static final String mPreferncesFileName = "AutoBackUpSettings";
    private NotificationCompat.Builder notificationBuilder;
    String mode = "";

    public AutoBackUpTask(@NonNull Context context,
                          @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        mPreferences = context.getSharedPreferences(mPreferncesFileName, MODE_PRIVATE);
    }


    @NonNull
    @Override
    public Result doWork() {
        mode = getInputData().getString("Mode");


        if (ClsGlobal.hasPermissions(context, STORAGE_PERMISSIONS)) {
            if (mode != null && mode.equalsIgnoreCase("Manual Backup Cloud From Remainder")) {
                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " - Manual Backup Cloud From Remainder Started. \n");
            } else {

                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " - Auto Cloud Backup Started. \n");
            }


            Log.e("getNetworkTypeStatus", "getNetworkTypeStatus inside");
            if (ClsGlobal.CheckInternetConnection(context)) {
                NetworkTypeStatus = ClsGlobal.CheckNetWorkType(context);

                // mode is == to Manual Backup Cloud From Remainder then no need to check network type.
                if (mode != null && mode.equalsIgnoreCase("Manual Backup Cloud From Remainder")) {
                    TakeBackUp("AutoBackUp");
                } else {
                    getNetworkTypeStatus = mPreferences.getString("NetworkType", null);
//            Log.e("getNetworkTypeStatus", getNetworkTypeStatus);
                    if (NetworkTypeStatus != null && !NetworkTypeStatus.equalsIgnoreCase("")
                            && getNetworkTypeStatus != null && !getNetworkTypeStatus.equalsIgnoreCase("")) {
                        Log.e("getNetworkTypeStatus", "(NetworkTypeStatus != getNetworkTypeStatus != null");

                        if (getNetworkTypeStatus.equalsIgnoreCase("WI-FI") &&
                                NetworkTypeStatus.equalsIgnoreCase("WIFI")) {
                            Log.e("getNetworkTypeStatus", "getNetworkTypeStatus inside");
                            TakeBackUp("AutoBackUp");
                        } else if (getNetworkTypeStatus.equalsIgnoreCase("WI-FI OR CELLULAR")) {
                            Log.e("getNetworkTypeStatus", "Wi-fi or cellular inside");
                            TakeBackUp("AutoBackUp");
                        }


                    }
                }

            } else {
                Log.e("--URL--", "else ClsGlobal.CheckInternetConnection(context): ");

                ClsGlobal.BackupNotification("Auto Backup Failed",
                        "No Internet Connection Found while Backing up At this Time: "
                                + getEntryDateFormat(getCurruntDateTime()), context,
                        ClsGlobal.FtouchCloud_Bkp_Notification_id);

                ClsGlobal.InsertBackupLogs("Auto Backup Failed",
                        "No Internet Connection Found while Backing up", context);

                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " - Auto Cloud Backup Failed. " +
                        "No Internet Connection Found while Backing up waiting for Next Internet " +
                        "Connectivity. \n");

                // Scheduled JobScheduler for Next Internet connectivity.

//            PersistableBundle bundle = new PersistableBundle();
//            bundle.putString("mode", "AutoBackup again");
//
//            @SuppressLint({"NewApi", "LocalSuppress"})
//            JobInfo myJob = new JobInfo.Builder(26,
//                    new ComponentName(context, CheckNetworkJob.class))
//                    .setMinimumLatency(1000)
//                    .setOverrideDeadline(2000)
//                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
//                    .setPersisted(true)
//                    .setExtras(bundle)
//                    .build();
//
//            @SuppressLint({"NewApi", "LocalSuppress"})
//            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(
//                    Context.JOB_SCHEDULER_SERVICE);
//            jobScheduler.schedule(myJob);

                // Setup OneTimeWorkRequest for taking backup
                // when we get Internet connection.
                Data data = new Data.Builder()
                        .putString("Mode", "AutoBackup again")
                        .build();


                OneTimeWorkRequest oneTimeWorkRequest =
                        new OneTimeWorkRequest.Builder(AutoBackUpTask.class)
                                .setInputData(data)
                                .build();

                WorkManager.getInstance().enqueueUniqueWork(ClsGlobal
                                .AppPackageName.concat("OneTimeAutoBackUp")
                        , ExistingWorkPolicy.KEEP, oneTimeWorkRequest);

                if (GetNetworkTypeStatus(context).equalsIgnoreCase("WI-FI")) {

                    notificationBuilder = ProgressBarNotification("Backup pending",
                            "fTouch Pos is Waiting for Wifi", context,
                            FtouchCloud_Bkp_Process_Notification_id, "Show Percentage");
                } else {
                    notificationBuilder = ProgressBarNotification("Backup pending",
                            "fTouch Pos is Waiting for Internet", context, FtouchCloud_Bkp_Process_Notification_id, "Show Percentage");
                }
            }


        }


        return Result.success();
    }


    private void TakeBackUp(String BackUpMode) {

        //  backupDatabaseAPI("Database", byte_array, ".db", "Database".concat(ClsGlobal.getEntryDate()), BackUpMode);


        try {
//            deleteAllFiles_from_folder(new File(ClsGlobal.SDPath + "/" + ClsGlobal.AppFolderName));

//             Deleting all Bkp zip file's from fTouchPOSLocalBkp folder.
//            deleteAllFiles_from_folder(new File(zipPath));

            generateBackupFile();

            copyDirectory(new File(SharedPreferencesPath),
                    new File(SharedPreferencesPathBackupFolder));


            File _saveLocation = Environment.getExternalStorageDirectory();
            Log.e("camera", "filepath:- " + _saveLocation);
            File dir = new File(_saveLocation.getAbsolutePath() + "/" + ClsGlobal.AppFolderName + "/");
            Log.e("generateBackupFile", "dir:- " + dir);

            // Create fTouchPOSLocalBkp Backup folder if not exist.
            if (!new File(zipPath).exists()) {
                try {
                    new File(zipPath).mkdir();
                } catch (Exception e) {
                    Log.e("generateBackupFile", "dir:- " + e.getMessage());
                }
            }

            if (mode != null && mode.equalsIgnoreCase("Manual Backup Cloud From Remainder")) {
                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " - Creating Zip file (Manual Backup Cloud From Remainder). \n");
                currenbBkpFileName = generateBackUpFileName("ManualCloudBkp")
                        .replace("/", "_");
            } else {

                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " - Creating Zip file (AutoCloud Backup). \n");
                currenbBkpFileName = generateBackUpFileName("AutoCloudBkp")
                        .replace("/", "_");
            }


            if (FileZipOperation.zip(dir.getAbsolutePath(), zipPath,
                    currenbBkpFileName, true)) {

                Log.e("generateBackupFile", "IF:- ");

                if (mode != null && mode.equalsIgnoreCase("Manual Backup Cloud From Remainder")) {
                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " - Backup Zip file Created (Manual Backup Cloud From Remainder). \n");

                } else {
                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " - Backup Zip file Created (AutoCloud Backup). \n");
                }


                if (DriveServiceHelper.isSignedIn(context)) {
                    Data data = new Data.Builder()
                            .putString("Mode", "Auto")
                            .build();

                    // if there is no InternetConnection then create new Worker.
                    Constraints myConstraints = new Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build();


                    Create_OneTimeWorkRequest(GoogleDriveBackupTask.class,
                            "GoogleDriveBackup", "KEEP", data, myConstraints);

                }


                Log.e("--URL--", "filesize: " + mode);
                if (mode != null && mode.equalsIgnoreCase("AutoBackup again")
                        || mode.equalsIgnoreCase("Manual Backup Cloud From Remainder")) {

                    Log.e("--URL--", "filesize: " + "AutoBackup again");

                    // Backup with showing Notification.
                    if (GetNetworkTypeStatus(context).equalsIgnoreCase("WI-FI")) {

                        notificationBuilder = ProgressBarNotification("Backup pending",
                                "fTouch Pos is Waiting for Wifi",
                                context, FtouchCloud_Bkp_Process_Notification_id, "Show Percentage");
                    } else {
                        notificationBuilder = ProgressBarNotification("Backup pending",
                                "fTouch Pos is Waiting for Internet", context, FtouchCloud_Bkp_Process_Notification_id, "Show Percentage");
                    }

                    FileUploader fileUploader = new FileUploader(new File(zipPath
                            + currenbBkpFileName),
                            "Manual", context);

                    NotificationManager notificationManager = (NotificationManager)
                            context.getSystemService(Context.NOTIFICATION_SERVICE);

                    fileUploader.SetCallBack(new FileUploader.FileUploaderCallback() {
                        @Override
                        public void onError() {
                            if (mode != null && mode.equalsIgnoreCase("Manual Backup Cloud From Remainder")) {
                                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                        " - Backup is Failed onError (Manual Backup Cloud From Remainder). \n");
                                ClsGlobal.InsertBackupLogs("Manual Backup Cloud From Remainder Failed",
                                        "Failed to upload Backing up", context);

                            } else {
                                ClsGlobal.InsertBackupLogs("Auto Backup Failed",
                                        "Failed to upload Backing up", context);

                                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                        " - Backup is Failed onError AutoBackup again" +
                                        " (AutoCloud Backup). \n");
                            }


                        }

                        @Override
                        public void onFinish(String responses) {
                            if (responses != null) {
                                if (responses.contains("1")) {
                                    if (mode != null && mode.equalsIgnoreCase("Manual Backup Cloud From Remainder")) {
                                        ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                                " - Backup Completed Successfully (Manual Backup Cloud From Remainder).  FileName:-" +
                                                currenbBkpFileName + "\n");
                                        ClsGlobal.InsertBackupLogs("Manual Backup Cloud From Remainder Successfully",
                                                "Manual Backup Cloud From Remainder Uploaded Successfully", context);

                                    } else {
                                        ClsGlobal.InsertBackupLogs("Auto Backup Completed",
                                                "Auto Backup Uploaded Successfully.", context);

                                        ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                                " - Backup Completed Successfully AutoBackup again (AutoCloud Backup). FileName:-"
                                                + currenbBkpFileName + "\n");
                                    }


                                } else {
                                    if (mode != null && mode.equalsIgnoreCase("Manual Backup Cloud From Remainder")) {
                                        ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                                " - Backup Completed Successfully (Manual Backup Cloud From Remainder).  FileName:-" +
                                                currenbBkpFileName + "\n");
                                        ClsGlobal.InsertBackupLogs("Manual Backup Cloud From Remainder Failed",
                                                "Manual Backup Cloud From Remainder Failed", context);

                                    } else {
                                        ClsGlobal.InsertBackupLogs("Auto Backup Failed",
                                                "Failed to upload Backing up", context);

                                        ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                                " - Auto Backup Failed (AutoCloud Backup). FileName:-"
                                                + currenbBkpFileName + "\n");
                                    }


                                }
                            }

                            Log.e("--URL--", "onFinish: ");
                            // Notification When Finished.
                            notificationBuilder.setContentTitle("Finished");
                            notificationBuilder.setContentText("Backup completed")
                                    // Removes the progress bar
                                    .setProgress(0, 0, false);
                            notificationManager.notify(FtouchCloud_Bkp_Process_Notification_id,
                                    notificationBuilder.build());

                            // Wait for 2 second.
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                            // Notification Cancel.
                            notificationManager.cancel(FtouchCloud_Bkp_Process_Notification_id);

                        }

                        @Override
                        public void onProgressUpdate(int currentpercent, int totalpercent, String msg) {
                            Log.e("--URL--", "onProgressUpdate: ");
                            // Notification When progressing backup.
                            notificationBuilder.setContentTitle("Backup in progress");
                            notificationBuilder.setProgress(100, currentpercent, false);
                            notificationBuilder.setContentText(msg.replace("File Size: ",
                                    "Uploaded: "));
                            notificationManager.notify(FtouchCloud_Bkp_Process_Notification_id,
                                    notificationBuilder.build());
                        }
                    });
                } else {
                    Log.e("--URL--", "else fileUploaderAuto: ");

                    // Backup with out showing Notification.
                    FileUploader fileUploaderAuto = new FileUploader(new File(
                            zipPath + currenbBkpFileName),
                            "Auto", context);

                    fileUploaderAuto.SetCallBack(new FileUploader.FileUploaderCallback() {
                        @Override
                        public void onError() {
                            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                    " - Backup is Failed onError" +
                                    " (AutoCloud Backup). \n");
                            ClsGlobal.InsertBackupLogs("Auto Backup Failed",
                                    "Failed to upload Backing up", context);

                        }

                        @Override
                        public void onFinish(String responses) {
                            if (responses != null) {
                                if (responses.contains("1")) {
                                    ClsGlobal.InsertBackupLogs("Auto Backup Completed",
                                            "Auto Backup Uploaded Successfully.", context);

                                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                            " - Backup Completed Successfully (AutoCloud Backup). FileName:-"
                                            + currenbBkpFileName + "\n");

                                } else {
                                    ClsGlobal.InsertBackupLogs("Auto Backup Failed",
                                            "Failed to upload Backing up", context);

                                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                            " - Backup failed" +
                                            "(AutoCloud Backup). FileName:-"
                                            + currenbBkpFileName + "\n");
                                }
                            }


                        }

                        @Override
                        public void onProgressUpdate(int currentpercent, int totalpercent, String msg) {

                        }
                    });

                }


            } else {
                if (mode != null && mode.equalsIgnoreCase("Manual Backup Cloud From Remainder")) {
                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " - Failed to Create Backup Zip file (Manual Backup Cloud From Remainder). \n");

                } else {

                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " - Failed to Create Backup Zip file (AutoCloud Backup). FileName:-" +
                            currenbBkpFileName + " \n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void generateBackupFile() {
        //generate database backup file
        try {
            Log.e("--TakeBackUp--", "Step:1");
            File _saveLocation = Environment.getExternalStorageDirectory();
            Log.e("--TakeBackUp--", "Step:2");
            Log.d("camera", "filepath:- " + _saveLocation);
            File dir = new File(_saveLocation.getAbsolutePath() + "/"
                    + ClsGlobal.AppFolderName + "/Backup/");
            Log.e("--TakeBackUp--", "Step:3");
            Log.d("generateBackupFile", "dir:- " + dir);

//            copyDirectory(new File(SharedPreferencesPath), new File(SharedPreferencesPathBackupFolder));

            if (!dir.exists()) {
                Log.e("--TakeBackUp--", "Step:4");
                dir.mkdirs();
            }
            Log.e("--TakeBackUp--", "Step:5");

            String AppDatabasePath = ClsGlobal.AppDatabasePath.concat(ClsGlobal.Database_Name);
            Log.e("--TakeBackUp--", "Step:6");

            Log.d("generateBackupFile", "AppDatabasePath:- " + AppDatabasePath);
            File data = Environment.getDataDirectory();
            Log.e("--TakeBackUp--", "Step:7");

            Log.d("generateBackupFile", "data:- " + data);
            File currentDB = new File(data, AppDatabasePath);

            Log.e("--TakeBackUp--", "Step:8");

            Log.d("generateBackupFile", "currentDB:- " + currentDB);
            String BackupDbFileName = "dbfile.db";
            Log.e("--TakeBackUp--", "Step:9");

            File backupDB = new File(dir, BackupDbFileName);

            Log.e("--TakeBackUp--", "Step:10");

            Log.d("generateBackupFile", "backupDB:- " + backupDB);

            if (currentDB.exists()) {

                Log.e("--TakeBackUp--", "Step:11");

                Log.d("generateBackupFile", "IF:- ");
                FileChannel src = new FileInputStream(currentDB).getChannel();
                Log.e("--TakeBackUp--", "Step:12");

                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                Log.e("--TakeBackUp--", "Step:13");

                dst.transferFrom(src, 0, src.size());

                Log.e("--TakeBackUp--", "Step:14");
                src.close();
                dst.close();
                Log.e("--TakeBackUp--", "Step:15");
            }

            Log.e("--TakeBackUp--", "Step:16");
//            byte_array = ClsGlobal.getStringFile(backupDB);
            Log.e("--TakeBackUp--", "Step:17");
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
            Log.e("--TakeBackUp--", "Step:18");

        }
        Log.e("--TakeBackUp--", "Step:19");
        //save to our app folder
        //get that file
        //convert to byte arry (see example in fTouch)

    }
}