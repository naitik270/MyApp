package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsPackageParameter;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfacePackage {

    @GET("Package/GetPackage")
    Call<ClsPackageParameter> value(@Query("ProductName") String productName,
                                    @Query("RegistrationMode") String mode);

}
