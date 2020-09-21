package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsBackupTypeParams;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InterfaceBackupType {

    @POST("FileUpload/BackupType")
    Call<ClsBackupTypeParams> postBackup(@Body ClsBackupTypeParams objClsBackupTypeParams);


}
