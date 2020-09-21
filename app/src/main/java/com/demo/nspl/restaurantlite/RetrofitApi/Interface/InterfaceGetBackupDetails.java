package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsGetBackupDetailsParams;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfaceGetBackupDetails {

    @GET("FileUpload/GetBackupDetails")
    Call<ClsGetBackupDetailsParams> value(@Query("CustomerID") String CustomerID,
                                          @Query("BackupType") String BackupType,
                                          @Query("ProductName") String ProductName);

}
