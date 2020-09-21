package com.demo.nspl.restaurantlite.Global;

import android.content.Context;
import android.util.Log;

import com.demo.nspl.restaurantlite.Interface.InterfacePdfFileUpload;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.PdfFileUploadResponse;
import com.demo.nspl.restaurantlite.classes.ClsUserInfo;
import com.google.gson.Gson;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PdfFileUploader {

    private FileUploaderCallback mFileUploaderCallback;
    Context mContext;


    public void SetCallBack(FileUploaderCallback fileUploaderCallback) {
        this.mFileUploaderCallback = fileUploaderCallback;
    }

    public PdfFileUploader( Context mContext){
        this.mContext = mContext;
    }


    public void Start_file_upload(File file,String OrderNo,String type){
        ClsUserInfo getUserInfo = ClsGlobal.getUserInfo(mContext);

        InterfacePdfFileUpload interfacePdfFileUpload = ApiClient.getRetrofitInstance()
                .create(InterfacePdfFileUpload.class);


        RequestBody fbody = RequestBody.create(MediaType.parse("application/pdf"), file);

        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file",
                file.getName(), fbody);


        RequestBody CustomerCode = RequestBody.create(
                okhttp3.MultipartBody.FORM, getUserInfo.getUserId());

        RequestBody ProductName = RequestBody.create(
                okhttp3.MultipartBody.FORM, ClsGlobal.AppName);

        RequestBody FileName = RequestBody.create(
                okhttp3.MultipartBody.FORM, file.getName());

        RequestBody Extension = RequestBody.create(
                okhttp3.MultipartBody.FORM, ".pdf");

        RequestBody InvoiceNo = RequestBody.create(
                okhttp3.MultipartBody.FORM, OrderNo);

        RequestBody Type = RequestBody.create(
                okhttp3.MultipartBody.FORM, type);

        Call<PdfFileUploadResponse> fileUpload = interfacePdfFileUpload.PdfUploadFile(fileToUpload,
                CustomerCode, ProductName, FileName, Extension, InvoiceNo,Type);


        fileUpload.enqueue(new Callback<PdfFileUploadResponse>() {
            @Override
            public void onResponse(Call<PdfFileUploadResponse> call,
                                   Response<PdfFileUploadResponse> response) {

                Gson gsonselectedItems = new Gson();
                String jsonInStringselectedItems = gsonselectedItems.toJson(response.body());
                Log.e("getResponse" ,"Pdf File upload: " +  jsonInStringselectedItems);
                if (response.body() != null && response.code() == 200) {
                    Log.e("getResponse", "--Response:- fileUpload " + response.message());


//                    Log.e("getResponse", "--Response:- fileUpload" + response.body().toString());

                    mFileUploaderCallback.onFinish(response.body());
                }else {
                    mFileUploaderCallback.onError();
                }
            }

            @Override
            public void onFailure(Call<PdfFileUploadResponse> call, Throwable t) {
                Log.e("getResponse" ,"onFailure: " +  t.getMessage());
                mFileUploaderCallback.onError();
            }
        });
    }


    public interface FileUploaderCallback {

        void onError();

        void onFinish(PdfFileUploadResponse responses);

        void onProgressUpdate(int currentpercent, int totalpercent, String msg);
    }

}
