package com.demo.nspl.restaurantlite.backGroundTask;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.activity.SettingAutoEmailActivity;

public class NotificationReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("onReceive", "NotificationReceiver call");

        if (intent != null) {
            Log.e("onReceive", "intent != null");
            String notificationId = intent.getStringExtra("notificationId");

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(Integer.parseInt(notificationId));
            if (ClsGlobal.CheckInternetConnection(context)) {
                ClsGlobal.SendEmail(context, "Auto generated email, Send Manually");
            } else {
                Intent intent1 = new Intent(context, SettingAutoEmailActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
                Toast.makeText(context, "There is No Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }

    }
}