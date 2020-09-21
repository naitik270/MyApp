package com.demo.nspl.restaurantlite.backGroundTask;

import android.content.Context;
import androidx.annotation.NonNull;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;


import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SendErrorWorker extends Worker {

    Context context;

    public SendErrorWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        // Send Error.
        ClsGlobal.sendErrorFileToServer(context);

        return Result.success();
    }

}

