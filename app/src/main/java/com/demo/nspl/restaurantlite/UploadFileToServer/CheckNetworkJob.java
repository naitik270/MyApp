package com.demo.nspl.restaurantlite.UploadFileToServer;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CheckNetworkJob extends JobService {

    private static final String TAG = CheckNetworkJob.class.getSimpleName();
    private ConnectionCheckBroadcast mConnectionCheckBroadcast;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "Service created");

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "Service destroyed");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        return START_NOT_STICKY;
    }


    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e(TAG, "onStartJob");

        // Sending mode whether to start AutoBackup task or SendError task.
        String mode = params.getExtras().getString("mode");

        Log.e("--URL--", "onStartJob: " +mode);

        mConnectionCheckBroadcast = new ConnectionCheckBroadcast(mode);

        registerReceiver(mConnectionCheckBroadcast, new IntentFilter(
                "android.net.conn.CONNECTIVITY_CHANGE"));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        try {
            if (mConnectionCheckBroadcast != null) {
                this.unregisterReceiver(mConnectionCheckBroadcast);
                mConnectionCheckBroadcast = null;
            }
        }catch (Exception e){
            Log.e(TAG, "onStartJob" + e.getMessage());
        }
        return true;
    }
}