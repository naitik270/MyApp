package com.demo.nspl.restaurantlite.UploadFileToServer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.backGroundTask.GoogleDriveBackupTask;
import com.demo.nspl.restaurantlite.backGroundTask.SalesSmsWorker;
import com.demo.nspl.restaurantlite.backGroundTask.SendBulkSmsWorker;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.CheckInternetConnection;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.Create_OneTimeWorkRequest;

public class ConnectionCheckBroadcast extends BroadcastReceiver {

    Context context;

    String mode = "";
    String data = "";

    public ConnectionCheckBroadcast(String mode) {
        this.mode = mode;
        Log.e("--Noti--", "ConnectionCheckBroadcast: " + mode);

    }

    public void setData(String Data){
        this.data =Data;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        Log.e("--Noti--", "ConnectionCheckBroadcast: " + "onReceive");
        Log.e("--Noti--", "mode:- " + mode);
//        Toast.makeText(context,"Mode:- " + mode,Toast.LENGTH_LONG).show();
//
//        if (CheckInternetConnection(context)) {
////            Log.e("--Noti--", "CheckInternetConnection: ");
////            if (mode != null && mode.equalsIgnoreCase("SendErrorWorkerTask")) {
////                Toast.makeText(context,"SendErrorWorkerTask",Toast.LENGTH_LONG).show();
////                Log.e("--Noti--", "SendErrorWorkerTask: ");
////
////                OneTimeWorkRequest oneTimeWorkRequest = new
////                        OneTimeWorkRequest.Builder(SendErrorWorker.class).build();
////
////                WorkManager.getInstance().enqueueUniqueWork(ClsGlobal.AppPackageName
////                        .concat("ErrorLogUploadToServer"), ExistingWorkPolicy.REPLACE, oneTimeWorkRequest);
////            }
//
////            // For AutoBackup again.
////            if (mode != null && mode.equalsIgnoreCase("AutoBackup again")) {
////                Toast.makeText(context,"AutoBackup again",Toast.LENGTH_LONG).show();
////
////                Log.e("--Noti--", "AutoBackup again: ");
//////                Data data = new Data.Builder()
//////                        .putString("Mode", "AutoBackup again")
//////                        .build();
//////
//////                OneTimeWorkRequest oneTimeWorkRequest =
//////                        new OneTimeWorkRequest.Builder(AutoBackUpTask.class)
//////                                .setInputData(data)
//////                                .build();
//////
//////                WorkManager.getInstance().enqueueUniqueWork(ClsGlobal.AppPackageName
//////                        .concat("AutoBackup_again"), ExistingWorkPolicy.REPLACE, oneTimeWorkRequest);
////
////            }
//
//
//
//        }

        if (mode != null && mode.equalsIgnoreCase("SalesSms")) {
            if (!CheckInternetConnection(context)) {
                // Cancel Work.
                ClsGlobal.cancelWorkerTask("SalesSms");

                // if there is no InternetConnection then create new Worker.
                Constraints myConstraints = new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build();

                Create_OneTimeWorkRequest(SalesSmsWorker.class,
                        "SalesSms", "KEEP", null,myConstraints);

            }

        }

        if (mode != null && mode.equalsIgnoreCase("BulkSms")) {
            if (!CheckInternetConnection(context)) {
                // Cancel Work.
                ClsGlobal.cancelWorkerTask("BulkSms");

                Data inputData = new Data.Builder()
                        .putString(SendBulkSmsWorker.EXTRA_SMS_TYPE,
                                data)
                        .build();


                // if there is no InternetConnection then create new Worker.
                Constraints myConstraints = new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build();

                Create_OneTimeWorkRequest(SendBulkSmsWorker.class,
                        "BulkSms", "KEEP", inputData, myConstraints);

            }

        }

        if (mode != null && mode.equalsIgnoreCase("GoogleDriveBkp")) {

            ClsGlobal.cancelWorkerTask("GoogleDriveBkp");

            Data inputData = new Data.Builder()
                    .putString("Mode",data)
                    .build();

            // if there is no InternetConnection then create new Worker.
            Constraints myConstraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            Create_OneTimeWorkRequest(GoogleDriveBackupTask.class,
                    "GoogleDriveBackup", "KEEP", inputData, myConstraints);

        }


    }


}

