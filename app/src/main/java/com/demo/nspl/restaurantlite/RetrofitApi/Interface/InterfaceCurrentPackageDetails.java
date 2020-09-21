package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsCurrentPackageDetailsParams;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfaceCurrentPackageDetails {

    @GET("CustomerPackage/CurrentPackageDetails")
    Call<ClsCurrentPackageDetailsParams> value(@Query("customerCode") String customerCode,
                                               @Query("ProductName") String ProductName);


}
