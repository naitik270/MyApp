package com.demo.nspl.restaurantlite.backGroundTask;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.classes.FileZipOperation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.List;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.STORAGE_PERMISSIONS;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.deleteAllFiles_from_folder;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.ftouchLogs_Folder;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.generateBackUpFileName;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getCurruntDateTime;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getEntryDateFormat;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.listFilesOldestFirst;

public class DbBackUpTask extends Worker {

    private Context mContext;
    private String currenbBkpFileName = "";

    public DbBackUpTask(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        if (ClsGlobal.hasPermissions(mContext,STORAGE_PERMISSIONS)) {
            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                    " - Emergency backup Database Started. \n");

            File log_folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/"+ftouchLogs_Folder+"/Backup/");

            if (!log_folder.exists()) {
                log_folder.mkdir();
            }

            try {

                // Copying Database file from app data folder to "/.ftouchlogPOS/Backup/".

                String AppDatabasePath = ClsGlobal.AppDatabasePath.concat(ClsGlobal.Database_Name);
                Log.d("generateBackupFile", "AppDatabasePath:- " + AppDatabasePath);
                File data = Environment.getDataDirectory();
                Log.d("generateBackupFile", "data:- " + data);
                File currentDB = new File(data, AppDatabasePath);
                Log.d("generateBackupFile", "currentDB:- " + currentDB);

                String BackupDbFileName = "dbfile.db";

                File backupDB = new File(log_folder, BackupDbFileName);
                Log.d("generateBackupFile", "backupDB:- " + backupDB);



                if (currentDB.exists()) {
                    Log.d("generateBackupFile", "IF:- ");
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }

                Log.d("generateBackupFile", "log.getParent():- " +log_folder.getParent());
                currenbBkpFileName = generateBackUpFileName("DbBackUp");

                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " - Creating Zip file (Emergency backup Database). \n");

                // Creating backup zip file of /.ftouchlogPOS/Backup/ in .ftouchlogPOS folder.
                // Backup Zip file name ex:- ftouchPOSEmergBkp 2_3_2019 2:00 PM.zip
                if (FileZipOperation.zip(log_folder.getAbsolutePath(), log_folder.getParent(),
                        currenbBkpFileName
                                .replace("/","_"), true)) {

                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " - Backup Zip file Created (Emergency backup Database). \n");


                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " - Backup is Successfully Created (Emergency backup Database) FileName:- " +
                            currenbBkpFileName +". \n");
                    Log.d("generateBackupFile", "zip :- ");
                } else {

                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " - Failed to Create Backup Zip file (Emergency backup Database). FileName:-"+
                            currenbBkpFileName+ " \n");

                    ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                            " - Backup Failed (Emergency backup Database) FileName:- " +
                            currenbBkpFileName +". \n");

                    Log.d("generateBackupFile", "failed:- ");

                }

                // deleting db file from /.ftouchlogPOS/Backup/ folder.
                deleteAllFiles_from_folder(new File(log_folder.getAbsolutePath()));

                log_folder = new File(log_folder.getParent());

                File[] filter_file_list =  log_folder.listFiles(pathname ->
                        pathname.getName().contains("ftouchPOSEmergBkp"));

                if (filter_file_list.length > 5){
                    List<File> file_list = listFilesOldestFirst(filter_file_list);
                    File deleteFiles = new File(file_list.get(0).getAbsolutePath());
                    Log.e("deleteFiles", String.valueOf(deleteFiles.delete()));
                    Log.e("deleteFiles", String.valueOf(deleteFiles.getAbsoluteFile()));

                }




            }catch (Exception e){
                Log.d("Exception", "Exception:- " + e.getMessage());
                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " - Backup Failed (Emergency backup Database) Error:- "
                        + e.getMessage() +" FileName:- " +
                        currenbBkpFileName +". \n");
            }

        }



        return Result.success();
    }
}
