package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsCustomerFreeLicenseUpdation;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InterfaceCustomerFreeLicenseUpdation {

    @POST("CustomerPackage/CustomerFreeLicenseUpdation")
    Call<ClsCustomerFreeLicenseUpdation> postCall(@Body ClsCustomerFreeLicenseUpdation obj);

}
