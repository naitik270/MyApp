package com.demo.nspl.restaurantlite.backGroundTask;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;

public class SendAutoEmailWorker extends Worker {

    private Context context;

    public SendAutoEmailWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        ClsGlobal.SendAutoEmail(context,"Auto generated email");

        return Result.success();
    }
}