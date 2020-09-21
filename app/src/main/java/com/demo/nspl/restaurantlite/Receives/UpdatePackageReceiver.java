package com.demo.nspl.restaurantlite.Receives;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.SendEmailUtility.Scheduler;
import com.demo.nspl.restaurantlite.SendEmailUtility.SharedPreferenceTime;
import com.demo.nspl.restaurantlite.UpdateCalling.ClsVendorLedgerUpdate;
import com.demo.nspl.restaurantlite.backGroundTask.SendEmailTask;
import com.demo.nspl.restaurantlite.classes.ClsEmailConfiguration;

import static android.content.Context.MODE_PRIVATE;

public class UpdatePackageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_MY_PACKAGE_REPLACED)
                && intent.getPackage().equalsIgnoreCase(context.getPackageName())) {


            ClsGlobal.add_Edit_DataBase_Columns(context);
//            ClsGlobal.sendNotification("Update","Update call","",context);

            ClsVendorLedgerUpdate objUpdate = new ClsVendorLedgerUpdate();
            objUpdate.setUpdateVendorLedger("FirstTime");



            // Auto Logout......
//            ClsGlobal.autoLogout(context);

            ClsGlobal.setUpdateVendorLedger(objUpdate, context);


            OnUpdateTask(context);
        }


    }


    private void OnUpdateTask(Context context) {

        // --------------------------- For ReScheduling AutoLogout Task ----------------------------//

        ClsGlobal.cancelWorkerTask(ClsGlobal.AppPackageName.concat(
                "DailyTaskLogoutRetail"));

//        WorkManager.getInstance().cancelUniqueWork(
//                ClsGlobal.AppPackageName.concat("DailyTaskLogoutRestaurant"));


//        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder
//                (DailyLogoutTask.class,
//                1, TimeUnit.DAYS)
//                .build();
//
//
//        WorkManager.getInstance().enqueueUniquePeriodicWork(ClsGlobal.AppPackageName
//                        .concat("DailyTaskLogoutRetail")
//                , ExistingPeriodicWorkPolicy.KEEP
//                , periodicWorkRequest);


        // -------------------------- For ReScheduling AutoBackup Task ----------------------------//

        ClsGlobal.cancelWorkerTask(ClsGlobal.AppPackageName.concat("AutoBackUp"));
//        WorkManager.getInstance().cancelUniqueWork(ClsGlobal.AppPackageName.concat("AutoBackUp"));

        SharedPreferences mAutoBackupPreferences = context.getSharedPreferences(
                ClsGlobal.AutoBackUpSharedPreferencesFileName, MODE_PRIVATE);

        String getAutoBackupTime = mAutoBackupPreferences.getString("AutoBackUpTime", "");

        if (getAutoBackupTime != null) {
            switch (getAutoBackupTime) {

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

                    ClsGlobal.cancelWorkerTask(ClsGlobal.AppPackageName.concat("AutoBackUp"));

                    break;
            }
        }


        // --------------------- ReScheduling Auto Email -------------------------//

        ClsEmailConfiguration EmailConfiguration = ClsGlobal.getEmailConfiguration(context);
        if (EmailConfiguration != null) {

            if (!EmailConfiguration.getFromEmailId().equalsIgnoreCase("")
                    && !EmailConfiguration.getPassword().equalsIgnoreCase("")) {

                SharedPreferenceTime mSharedPreferenceTime = new SharedPreferenceTime(context);
                Scheduler.setReminder(context, SendEmailTask.class
                        , mSharedPreferenceTime.get_hour(), mSharedPreferenceTime.get_min());
            }

        }

        // ----------------------------- AutoLocal Backup --------------------------//

        ClsGlobal.cancelWorkerTask(ClsGlobal.AppPackageName
                .concat(".DailyTaskAutoLocalBkp_ftouchPos"));

        ClsGlobal.SetUpAutoLocalBkp();


    }


}
