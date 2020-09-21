package com.demo.nspl.restaurantlite.UploadFileToServer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.backGroundTask.SendErrorWorker;

import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class ConnectionCheck extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        final String action = intent.getAction();

        switch (action) {

            case ConnectivityManager.CONNECTIVITY_ACTION:

                ConnectivityManager connMgr = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    //start schedule
                    OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(SendErrorWorker.class).build();

                    // For Setting up Unique PeriodicWork. So there is one PeriodicWork active at a time.
                    // If you set new PeriodicWork it will replace old PeriodicWork with new PeriodicWork.
                    // In Short it update PeriodicWork.
                    // Remember there is Only one PeriodicWork at a time.

                    WorkManager.getInstance().enqueueUniqueWork(ClsGlobal.AppPackageName.concat("ErrorLogUploadToServer"),
                            ExistingWorkPolicy.REPLACE, oneTimeWorkRequest);


                }
                break;
        }

    }


}

