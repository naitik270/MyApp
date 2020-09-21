package com.demo.nspl.restaurantlite.Global;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.demo.nspl.restaurantlite.Interface.InterfaceFileUpload;
import com.demo.nspl.restaurantlite.classes.ClsUserInfo;
import com.demo.nspl.restaurantlite.classes.UploadResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.LocalBackup;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.SDPath;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.deleteUnWanted_Bkp;


public class FileUploader {

    private FileUploaderCallback mFileUploaderCallback;
    long filesize = 0l;
    String mode = "";
    Context mContext;


    public FileUploader(File file, String mode,Context mContext) {
        this.mode = mode;
        this.mContext = mContext;
        filesize = file.length();

        ClsUserInfo getUserInfo = ClsGlobal.getUserInfo(mContext);

        Log.e("--URL--", "filesize: " + filesize);

        InterfaceFileUpload interfaceFileUpload = ApiClient.getRetrofitInstance()
                .create(InterfaceFileUpload.class);

        Log.e("--URL--", "interfaceDesignation: " + interfaceFileUpload.toString());

//        RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        RequestBody Remark;

        PRRequestBody mFile = new PRRequestBody(file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file",
                file.getName(), mFile);

        RequestBody CustomerID = RequestBody.create(
                MultipartBody.FORM, getUserInfo.getUserId());

        RequestBody ProductName = RequestBody.create(
                MultipartBody.FORM, ClsGlobal.AppName);

        RequestBody AppVersionAppVersion = RequestBody.create(
                MultipartBody.FORM, ClsGlobal.getApplicationVersion(mContext));

        RequestBody AppType = RequestBody.create(
                MultipartBody.FORM, ClsGlobal.AppType);

        RequestBody IMEINumber = RequestBody.create(
                MultipartBody.FORM, ClsGlobal.getIMEIno(mContext));

        RequestBody DeviceInfo = RequestBody.create(
                MultipartBody.FORM, ClsGlobal.getDeviceInfo(mContext));

        RequestBody BackupType = RequestBody.create(
                MultipartBody.FORM, "Document");

        if (mode.equalsIgnoreCase("Manual and Auto")){
             Remark = RequestBody.create(
                    MultipartBody.FORM, "AutoBackup @".concat(ClsGlobal.getEntryDate()));
        }else {
            Remark = RequestBody.create(
                    MultipartBody.FORM, "Backup @".concat(ClsGlobal.getEntryDate()));
        }


        RequestBody Extentsion = RequestBody.create(
                MultipartBody.FORM, ".zip");

        RequestBody FileName = RequestBody.create(
                MultipartBody.FORM, file.getName().replace(".zip","").
                        replace(":","_"));
//        file.getName().replace(".zip","")

        Call<UploadResponse> fileUpload = interfaceFileUpload.UploadFile(fileToUpload,
                CustomerID, ProductName, AppVersionAppVersion, AppType, IMEINumber,
                DeviceInfo, BackupType, Remark, Extentsion, FileName);


        Log.e("--URL--", "************************  before call : " +
                fileUpload.request().url());

        fileUpload.enqueue(new Callback<UploadResponse>() {

            @Override
            public void onResponse(@NonNull Call<UploadResponse> call,
                                   @NonNull Response<UploadResponse> response) {

//                // Delete unwanted backup files.
//                deleteUnWanted_Bkp(new File(ClsGlobal.SDPath + "/" + LocalBackup +"/"));

                if (response.body() != null && response.code() == 200) {
                    Log.e("getResponse", "--Response:-" + response.message());
                    Log.e("getResponse", "--Response:-" + response.body().toString());

                    if (mode.equalsIgnoreCase("Manual")) {
                        mFileUploaderCallback.onFinish(response.body().getSuccess());

                    } else if (mode.equalsIgnoreCase("Auto")) {

                        if (response.body().getSuccess().equalsIgnoreCase("1")) {
                            ClsGlobal.InsertBackupLogs("Auto Backup Completed",
                                    "Auto Backup Uploaded Successfully.", mContext);
                        } else {
                            ClsGlobal.InsertBackupLogs("Auto Backup Failed",
                                    "Failed to upload Backing up", mContext);
                        }

                    }
                }

                // Delete unwanted backup files.
                deleteUnWanted_Bkp(new File(SDPath + "/" + LocalBackup + "/"));
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {

                Log.e("Check","onFailure; " + t.getMessage());

//                // Delete unwanted backup files.
//                deleteUnWanted_Bkp(new File(ClsGlobal.SDPath + "/" + LocalBackup +"/"));
                if (mode.equalsIgnoreCase("Manual")) {
                    mFileUploaderCallback.onError();
                } else {
                    ClsGlobal.InsertBackupLogs("Auto Backup Failed",
                            "Failed to upload Backing up", mContext);
                }

                // Delete unwanted backup files.
                deleteUnWanted_Bkp(new File(SDPath + "/" + LocalBackup + "/"));
            }
        });


    }



    public void SetCallBack(FileUploaderCallback fileUploaderCallback) {
        this.mFileUploaderCallback = fileUploaderCallback;
    }


    public class PRRequestBody extends RequestBody {
        private File mFile;

        private int DEFAULT_BUFFER_SIZE = 20000048;

        PRRequestBody(final File file) {
            mFile = file;
            if (mFile.length() < 524288000) {
                DEFAULT_BUFFER_SIZE = 399999;
            }
        }

        @Override
        public MediaType contentType() {
            // i want to upload only images
            return MediaType.parse("multipart/form-data");
        }

        @Override
        public long contentLength() throws IOException {
            return mFile.length();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            long fileLength = mFile.length();
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            FileInputStream in = new FileInputStream(mFile);
            long uploaded = 0;
//            Source source = null;

            try {
                int read;
//                source = Okio.source(mFile);
                Handler handler = new Handler(Looper.getMainLooper());

                while ((read = in.read(buffer)) != -1) {

                    // update progress on UI thread
                    if (mode.equalsIgnoreCase("Manual")
                            || mode.equalsIgnoreCase("Manual and Auto")) {
                        handler.post(new ProgressUpdater(uploaded, fileLength));
                    }
                    uploaded += read;
                    sink.write(buffer, 0, read);
                }
//                sink.writeAll(source);

            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            } finally {
                in.close();
            }
        }
    }


    private class ProgressUpdater implements Runnable {
        private long mUploaded;
        private long mTotal;

        ProgressUpdater(long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;
        }

        @Override
        public void run() {
            int current_percent = (int) (100 * mUploaded / mTotal);
            int total_percent = (int) (100 * (mUploaded) / mTotal);
            if (mFileUploaderCallback != null){

                mFileUploaderCallback.onProgressUpdate(current_percent, total_percent,
                        "File Size: " + ClsGlobal.readableFileSize(mUploaded) +
                                "/" + ClsGlobal.readableFileSize(filesize));
            }
        }
    }

    public interface FileUploaderCallback {

        void onError();

        void onFinish(String responses);

        void onProgressUpdate(int currentpercent, int totalpercent, String msg);
    }


}
