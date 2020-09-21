package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsCashCollectionParams;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfaceCashCollection {


    @GET("CustomerPackage/CashCollectionVerification")
    Call<ClsCashCollectionParams> value(@Query("employeeCode") String employeeCode,
                                        @Query("packageID") int packageID,
                                        @Query("packageAmount") Double packageAmount);



}
