package com.demo.nspl.restaurantlite.backGroundTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.SendEmailUtility.Scheduler;
import com.demo.nspl.restaurantlite.SendEmailUtility.SharedPreferenceTime;


public class SendEmailTask extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Log.d("onReceive", "onReceive: BOOT_COMPLETED");
//                Toast.makeText(context,"After Boot",Toast.LENGTH_SHORT).show();
                SharedPreferenceTime localData = new SharedPreferenceTime(context);
                Scheduler.setReminder(context, SendEmailTask.class, localData.get_hour(), localData.get_min());
                return;
            }

            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_TIME_CHANGED)) {
                // Set the alarm here.
                Log.d("onReceive", "onReceive: ACTION_TIME_CHANGED");
//                Toast.makeText(context,"After Boot",Toast.LENGTH_SHORT).show();
                SharedPreferenceTime localData = new SharedPreferenceTime(context);
                Scheduler.setReminder(context, SendEmailTask.class, localData.get_hour(), localData.get_min());
                return;
            }

            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_TIMEZONE_CHANGED)) {
                // Set the alarm here.
                Log.d("onReceive", "onReceive: ACTION_TIMEZONE_CHANGED");
//                Toast.makeText(context,"After Boot",Toast.LENGTH_SHORT).show();
                SharedPreferenceTime localData = new SharedPreferenceTime(context);
                Scheduler.setReminder(context, SendEmailTask.class, localData.get_hour(), localData.get_min());
                return;
            }

            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_REBOOT)) {
                // Set the alarm here.
                Log.d("onReceive", "onReceive: ACTION_REBOOT");
//                Toast.makeText(context,"After Boot",Toast.LENGTH_SHORT).show();
                SharedPreferenceTime localData = new SharedPreferenceTime(context);
                Scheduler.setReminder(context, SendEmailTask.class, localData.get_hour(), localData.get_min());
                return;
            }

            if (intent.getAction().equalsIgnoreCase("android.intent.action.QUICKBOOT_POWERON")) {
                // Set the alarm here.
                Log.d("onReceive", "onReceive: android.intent.action.QUICKBOOT_POWERON");
//                Toast.makeText(context,"After Boot",Toast.LENGTH_SHORT).show();
                SharedPreferenceTime localData = new SharedPreferenceTime(context);
                Scheduler.setReminder(context, SendEmailTask.class, localData.get_hour(), localData.get_min());
                return;
            }
        }

        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(SendAutoEmailWorker.class).build();

        WorkManager.getInstance().enqueueUniqueWork(ClsGlobal.AppPackageName.concat("SendAutoEmails"),
                ExistingWorkPolicy.REPLACE, oneTimeWorkRequest);


//        ClsGlobal.SendEmail(context,"Auto generated email");
    }
}
