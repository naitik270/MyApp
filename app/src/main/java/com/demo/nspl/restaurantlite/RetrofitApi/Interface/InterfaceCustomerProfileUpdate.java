package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsCustomerProfileUpdateParams;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InterfaceCustomerProfileUpdate {

    @POST("Customer/CustomerProfileUpdate")
    Call<ClsCustomerProfileUpdateParams> postUpdateProfile(@Body ClsCustomerProfileUpdateParams objClsCustomerProfileUpdateParams);

}
