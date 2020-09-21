package com.demo.nspl.restaurantlite.backGroundTask;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.CheckBulkSmsStatus;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.CheckSalesSmsStatus;

public class CheckSmsStatusTask extends Worker {

    private Context context;
    public static final String EXTRA_MODE = "MODE";
    private String mode = "";
    private static final String TAG = CheckSmsStatusTask.class.getSimpleName();

    public CheckSmsStatusTask(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        mode = getInputData().getString(EXTRA_MODE);
//        DeleteAllAfterTime(context);

        if (ClsGlobal.CheckInternetConnection(context)) {

            android.database.sqlite.SQLiteDatabase db = context.openOrCreateDatabase(ClsGlobal.Database_Name,
                    Context.MODE_PRIVATE, null);
            if (mode != null && mode.equalsIgnoreCase("SalesSms")) {
                CheckSalesSmsStatus(context,db);
                db.close();
            } else if (mode != null && mode.equalsIgnoreCase("BulkSms")) {
                CheckBulkSmsStatus(context,db);
                db.close();
            }
        }

        return Result.success();
    }
}
