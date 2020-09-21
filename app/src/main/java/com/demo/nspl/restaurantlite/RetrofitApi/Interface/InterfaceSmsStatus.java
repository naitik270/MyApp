package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsCheckSmsStatusResponse;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsSmsStatusPrams;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InterfaceSmsStatus {

    //    @POST("DeveloperApiV1/SendBulkSMS")
    @POST("DeveloperApiV1/GetSMSStatus")
    Call<ClsCheckSmsStatusResponse> getSmsStatus(@Body ClsSmsStatusPrams obj);
}
