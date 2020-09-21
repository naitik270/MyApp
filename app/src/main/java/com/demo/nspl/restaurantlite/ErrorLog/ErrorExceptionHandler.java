package com.demo.nspl.restaurantlite.ErrorLog;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;

import org.apache.http.NameValuePair;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.UploadErrorLogs_BackupLogs;


public class ErrorExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler defaultUEH;

    Context activity;
    String stacktrace, url, ip, mstringuploaduri, method, current;
    SQLiteDatabase db;
    Handler mHandler;
    Runnable runn;
    List<NameValuePair> parameters;
    private static String responseJSON;

    public ErrorExceptionHandler(Context context, String string) {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        this.activity = context;
        this.method = string;
    }

    public void uncaughtException(Thread t, Throwable e) {
        try {
            PackageInfo pInfo = activity.getPackageManager().getPackageInfo(
                    activity.getPackageName(), PackageManager.GET_META_DATA);
            current = String.valueOf(pInfo.versionName);
        } catch (PackageManager.NameNotFoundException f) {
        }
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        printWriter.close();

        String mailBody = ClsGlobal.generateErrorFile(this.method, result.toString(), this.activity);

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);

        emailIntent.putExtra(Intent.EXTRA_EMAIL,
                new String[]{"rushangbhavani@gmail.com", "support@nathanisoftware.com"});

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Error report App version:-" +
                ClsGlobal.getApplicationVersion(this.activity)
                        .concat(", App Name-").concat(ClsGlobal.AppName).concat(" Date-")
                        .concat(ClsGlobal.getEntryDateTime()));

        emailIntent.putExtra(Intent.EXTRA_TEXT, "" + mailBody);

        emailIntent.setData(Uri.parse("mailto:"));
        activity.startActivity(Intent.createChooser(emailIntent,
                "Please Email Error Report:"));

        Toast.makeText(activity, "Send Error Report to online using email.",
                Toast.LENGTH_SHORT).show();


//            PersistableBundle bundle = new PersistableBundle();
//            bundle.putString("mode", "SendErrorWorkerTask");
//
//            @SuppressLint({"NewApi", "LocalSuppress"}) JobInfo myJob =
//                    new JobInfo.Builder(26, new ComponentName(activity, CheckNetworkJob.class))
//                            .setMinimumLatency(1000)
//                            .setOverrideDeadline(2000)
//                            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
//                            .setExtras(bundle)
//                            .setPersisted(true)
//                            .build();
//
//            JobScheduler jobScheduler = (JobScheduler) activity.getSystemService(
//                    Context.JOB_SCHEDULER_SERVICE);
//            jobScheduler.schedule(myJob);


        UploadErrorLogs_BackupLogs(activity);



        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);


        StackTraceElement[] arr = e.getStackTrace();
        String report = e.toString() + "\n\n";
        report += "--------- Stack trace ---------\n\n";
        for (int i = 0; i < arr.length; i++) {
            report += "    " + arr[i].toString() + "\n";
        }
        report += "-------------------------------\n\n";

        report += "--------- Cause ---------\n\n";
        Throwable cause = e.getCause();
        if (cause != null) {
            report += cause.toString() + "\n\n";
            arr = cause.getStackTrace();
            for (int i = 0; i < arr.length; i++) {
                report += "    " + arr[i].toString() + "\n";
            }
        }
        report += "-------------------------------\n\n";

        defaultUEH.uncaughtException(t, e);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) activity
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
