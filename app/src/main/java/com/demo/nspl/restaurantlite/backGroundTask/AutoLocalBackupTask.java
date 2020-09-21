package com.demo.nspl.restaurantlite.backGroundTask;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.classes.ClsQuotationMaster;
import com.demo.nspl.restaurantlite.classes.ClsUserInfo;
import com.demo.nspl.restaurantlite.classes.FileZipOperation;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.LocalBackup;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.ProgressBarNotification;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.STORAGE_PERMISSIONS;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.SharedPreferencesPath;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.SharedPreferencesPathBackupFolder;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.copyDirectory;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.deleteUnWanted_Bkp;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.generateBackUpFileName;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.generateBackupFile;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getCurruntDateTime;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getEntryDateFormat;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.zipPathAutoLocalBkp;

public class AutoLocalBackupTask extends Worker {

    public Context mContext;
    private String TempZipFilePath = zipPathAutoLocalBkp;
    private String currenbBkpFileName = "", mode = "";
    private NotificationCompat.Builder notificationBuilder;

//    private String zipPath = Environment.getExternalStorageDirectory().getAbsolutePath()
//            + "/" + ClsGlobal.LocalBackup + "/";

    public AutoLocalBackupTask(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mContext = context;
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public Result doWork() {
        mode = getInputData().getString("Mode");

        try {
//             Deleting all Bkp zip file's from fTouchPOSLocalBkp folder.
//            deleteAllFiles_from_folder(new File(zipPath));

//            deleteAllFiles_from_folder(new File(ClsGlobal.SDPath + "/" + ClsGlobal.AppFolderName));


            if (ClsGlobal.hasPermissions(mContext,STORAGE_PERMISSIONS)) {
                // Permission is not granted
                // --------------- For Notification ------------------------//
                notificationBuilder = ProgressBarNotification("Local Backup",
                        "fTouch Pos is Taking Local Backup", mContext, 55
                        , "NoPercentage");
                NotificationManager notificationManager = (NotificationManager)
                        mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(55, notificationBuilder.build());
                //------------------------------------------------------------//


                if (mode != null && mode.equalsIgnoreCase("Manual Local Backup From Remainder")) {
                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " - Manual Local Backup From Remainder Started. \n");
                } else {

                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " - AutoLocal Backup Started. \n");
                }

                File _saveLocation = Environment.getExternalStorageDirectory();

                File dir = new File(_saveLocation.getAbsolutePath()
                        + "/" + ClsGlobal.AppFolderName + "/");

                if (!dir.exists()) {
                    dir.mkdir();
                }


                // Create AutoLocal Backup folder if not exist.
                if (!new File(zipPathAutoLocalBkp).exists()) {
                    try {
                        new File(zipPathAutoLocalBkp).mkdir();
                    } catch (Exception e) {
                        Log.d("generateBackupFile", "Zip Path:- " + e.getMessage());
                    }

                }

                // Create SettingsBackup Backup folder if not exist.
                if (!new File(SharedPreferencesPathBackupFolder).exists()) {
                    try {
                        new File(SharedPreferencesPathBackupFolder).mkdir();
                    } catch (Exception e) {
                        Log.d("generateBackupFile", "Zip Path:- " + e.getMessage());
                    }

                }


                generateBackupFile();

                copyDirectory(new File(SharedPreferencesPath),
                        new File(SharedPreferencesPathBackupFolder));


                if (mode != null && mode.equalsIgnoreCase("Manual Local Backup From Remainder")) {

                    currenbBkpFileName = generateBackUpFileName("LocalBkp")
                            .replace("/", "_");
                    TempZipFilePath += currenbBkpFileName;

                } else {
                    currenbBkpFileName = generateBackUpFileName("AutoLocalBkp")
                            .replace("/", "_");
                    TempZipFilePath += currenbBkpFileName;
                }


                Log.d("generateBackupFile", "Zip Path:- " + zipPathAutoLocalBkp);
                Log.d("generateBackupFile", "getAbsolutePath Path:- " + dir.getAbsolutePath());

                if (mode != null && mode.equalsIgnoreCase("Manual Local Backup From Remainder")) {

                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " - Creating Zip file (Manual Local Backup From Remainder). \n");
                } else {

                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " - Creating Zip file (AutoLocal Backup). \n");
                }


                if (FileZipOperation.zip(dir.getAbsolutePath(), zipPathAutoLocalBkp
                        , currenbBkpFileName,
                        true)) {

                    Log.d("generateBackupFile", "IF:- ");

                    if (mode != null && mode.equalsIgnoreCase("Manual Local Backup From Remainder")) {

                        ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                " - Backup Zip file Created (Manual Local Backup From Remainder). \n");

                        ClsGlobal.InsertBackupLogs("Manual Local Backup From Remainder is Successful.",
                                "Backup  successfully To Path:- " +
                                        TempZipFilePath
                                , mContext);
                    } else {
                        ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                " - Backup Zip file Created (AutoLocal Backup). \n");

                        ClsGlobal.InsertBackupLogs("Auto Local Backup Successfully",
                                "Backup successfully To Path:- " +
                                        TempZipFilePath
                                , mContext);
                    }


                    // Wait for 2 second.
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    // Notification Cancel.
                    notificationManager.cancel(55);

                    List<File> sortedFiles = listFilesOldestFirst(zipPathAutoLocalBkp);

//                Log.e("deleteFiles", "After sort:- "+ Arrays.toString(sortedFileName));
                    if (sortedFiles.size() > 2) {

                        File deleteFiles = new File(sortedFiles.get(0).getAbsolutePath());
                        Log.e("deleteFiles", String.valueOf(deleteFiles.delete()));
                        Log.e("deleteFiles", String.valueOf(deleteFiles.getAbsoluteFile()));

//                    if(sortedFiles.size() >= 3){
//                        int deleteNumber_of_files = sortedFiles.size() - 2;
//                        Log.e("for--1", "Example size: "+ String.valueOf(deleteNumber_of_files));
//                        Log.e("for--1", "sortedFiles.size(): "+ String.valueOf(sortedFiles.size()));
//
//                        for (File file : sortedFiles){
//                            Log.e("for--1" ,"Sorted files list:- " + file.getName());
//                        }
//
//                        for (int i = deleteNumber_of_files;  i > 0; i--){
//                            Log.e("for--1", "Example: "+ String.valueOf(i));
//                        }
//
//                    }

                    }

                    if (mode != null && mode.equalsIgnoreCase("Manual Local Backup From Remainder")) {

                        ClsGlobal.sendNotification("Local Backup Successfully",
                                "Backup successfully To Path:- " +
                                        TempZipFilePath
                                        + " " +
                                        " At This Time:- "
                                        + getEntryDateFormat(getCurruntDateTime()),
                                "",
                                mContext);
                    } else {
                        ClsGlobal.sendNotification("AutoLocal Backup Successfully",
                                "Backup successfully To Path:- " +
                                        TempZipFilePath
                                        + " " +
                                        " At This Time:- "
                                        + getEntryDateFormat(getCurruntDateTime()),
                                "",
                                mContext);
                    }


                    if (mode != null && mode.equalsIgnoreCase("Manual Local Backup From Remainder")) {

                        ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                " - Backup Completed Successfully (Manual Local Backup From Remainder). FileName:- "
                                + currenbBkpFileName + "\n");
                    } else {
                        ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                " - Backup Completed Successfully (AutoLocal Backup). FileName:- "
                                + currenbBkpFileName + "\n");
                    }


                } else {

                    if (mode != null && mode.equalsIgnoreCase("Manual Local Backup From Remainder")) {

                        ClsGlobal.sendNotification("Local Backup Failed",
                                "Backup is Failed to Local Device. " +
                                        " At This Time:- "
                                        + getEntryDateFormat(getCurruntDateTime()),
                                "",
                                mContext);

                    } else {
                        ClsGlobal.sendNotification("Auto Local Backup Failed",
                                "Backup is Failed to Local Device. " +
                                        " At This Time:- "
                                        + getEntryDateFormat(getCurruntDateTime()),
                                "",
                                mContext);
                    }


                    if (mode != null && mode.equalsIgnoreCase("Manual Local Backup From Remainder")) {

                        ClsGlobal.InsertBackupLogs("Manual Local Backup Failed",
                                "Backup is Failed to Local Device.", mContext);

                        ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                " - Backup failed (Manual Local Backup From Remainder). FileName:- "
                                + currenbBkpFileName + "\n");

                    } else {
                        ClsGlobal.InsertBackupLogs("Auto Local Backup Failed",
                                "Backup is Failed to Local Device.", mContext);

                        ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                                " - Backup failed (AutoLocal Backup). FileName:- "
                                + currenbBkpFileName + "\n");
                    }


                }
            }else {
                Log.e("check","No permission");
            }




        } catch (Exception e) {
            if (mode != null && mode.equalsIgnoreCase("Manual Local Backup From Remainder")) {

                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) + " - Error:- " +
                        e.getMessage() + " (Manual Local Backup From Remainder). \n");
            } else {

                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) + " - Error:- " +
                        e.getMessage() + " (AutoLocal Backup). \n");
            }

        }

        ClsQuotationMaster.UpdateQuotationStatus(mContext);

        // Delete unwanted backup files.
        deleteUnWanted_Bkp(new File(
                Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/" + LocalBackup + "/"));

        // Auto logout.
        ClsUserInfo userInfo = new ClsUserInfo();

        ClsUserInfo userLoginStatus = ClsGlobal.getUserInfo(mContext);
        if (!userLoginStatus.getLoginStatus().equalsIgnoreCase("DEACTIVE")){
            userInfo.setLoginStatus("DEACTIVE");
            ClsGlobal.setUserInfo(userInfo, mContext);
        }

        return Result.success();
    }


    private static List<File> listFilesOldestFirst(final String directoryPath) throws IOException {
        final List<File> files = Arrays.asList(new File(directoryPath).listFiles());
        final Map<File, Long> constantLastModifiedTimes = new HashMap<File, Long>();
        for (final File f : files) {
            constantLastModifiedTimes.put(f, f.lastModified());
        }
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(final File f1, final File f2) {
                return constantLastModifiedTimes.get(f1).compareTo(constantLastModifiedTimes.get(f2));
            }
        });
        return files;
    }

}
