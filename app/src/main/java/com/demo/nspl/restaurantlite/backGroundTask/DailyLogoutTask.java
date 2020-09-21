package com.demo.nspl.restaurantlite.backGroundTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.classes.ClsUserInfo;

import static android.content.Context.MODE_PRIVATE;
import static com.demo.nspl.restaurantlite.classes.ClsSMSLogs.UpdateSmsSendingStatus;

public class DailyLogoutTask extends Worker {

    private SharedPreferences mPreferences;
    private static final String mPreferncesName = "MyPerfernces";

    private Context context;

    public DailyLogoutTask(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        mPreferences = context.getSharedPreferences(mPreferncesName, MODE_PRIVATE);

        Log.e("DailyLogoutTask", "DailyLogoutTask call");
    }

    @NonNull
    @Override
    public Result doWork() {

        String getstatus = mPreferences.getString("Status1", "No Task Perform");
        Log.d("---getstatus---", "----getstatus---" + getstatus);
        Log.e("doWork", "doWork call");

        // Update Sales Sms Status to Expire
        // Which are older than 24 hours .
        UpdateSmsSendingStatus(context,"DailyLogoutTask");

        if (!getstatus.equalsIgnoreCase("Frist")) {

            ClsUserInfo userInfo = new ClsUserInfo();

            ClsUserInfo userLoginStatus = ClsGlobal.getUserInfo(context);

            if (!userLoginStatus.getLoginStatus().equalsIgnoreCase("DEACTIVE")){
                Log.e("chack", "Logout");
                userInfo.setLoginStatus("DEACTIVE");
                ClsGlobal.setUserInfo(userInfo, context);

          //      ClsGlobal.sendNotification("IF", "IF background Logout".concat(ClsGlobal.getEntryDate()),context);
                WorkManager.getInstance().cancelUniqueWork(ClsGlobal.AppPackageName.concat("DailyTaskLogoutRetail"));
                Log.d("getFirstDateOfMonth", "----UserInfo---" + userLoginStatus.getLoginStatus());
            }


        } else {
            Log.e("chack", "Not Frist");
         //   ClsGlobal.sendNotification("ELSE", "ELSE background Logout".concat(ClsGlobal.getEntryDate()),context);
            SaveData("Second");
        }


        return Result.success();
    }



    private void SaveData(String str) {
        Log.e("SaveData", str);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString("Status1", str);
        preferencesEditor.apply();

    }
}
