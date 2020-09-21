package com.demo.nspl.restaurantlite.backGroundTask;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;

import java.io.File;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.CREATE_PDF_FILE_PATH;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.STORAGE_PERMISSIONS;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.deleteAllFiles_from_folder;

public class DeletePdfFilesWorker extends Worker {

    private Context mContext;

    public DeletePdfFilesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        if (ClsGlobal.hasPermissions(mContext,STORAGE_PERMISSIONS)) {
            // Delete All Pdf File From POS_Sales_Invoice folder.
            deleteAllFiles_from_folder(new File(CREATE_PDF_FILE_PATH));

        }

        return Result.success();
    }
}
