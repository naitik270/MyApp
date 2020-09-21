package com.demo.nspl.restaurantlite.backGroundTask;

import android.content.Context;
import android.content.IntentFilter;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.demo.nspl.restaurantlite.Global.SendBillSms;
import com.demo.nspl.restaurantlite.UploadFileToServer.ConnectionCheckBroadcast;

public class SalesSmsWorker extends Worker {

    private Context context;
    private ConnectionCheckBroadcast mConnectionCheckBroadcast;
    private static final String TAG = SalesSmsWorker.class.getSimpleName();

    public SalesSmsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        mConnectionCheckBroadcast = new ConnectionCheckBroadcast("SalesSms");

        context.registerReceiver(mConnectionCheckBroadcast, new IntentFilter(
                "android.net.conn.CONNECTIVITY_CHANGE"));

        SendBillSms sendBillSms = new SendBillSms(context);
        sendBillSms.SendSms("");

        // unregisterReceiver ConnectionCheckBroadcast after Work is finish.
        context.unregisterReceiver(mConnectionCheckBroadcast);

        return Result.success();
    }




}
