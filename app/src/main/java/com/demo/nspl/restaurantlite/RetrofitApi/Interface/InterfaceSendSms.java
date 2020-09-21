package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsSendSmsParams;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsSendSmsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InterfaceSendSms {

//    @POST("DeveloperApiV1/SendBulkSMS")
    @POST("DeveloperApiV1/SendBulkSingleSMS")
    Call<ClsSendSmsResponse> SendSms(@Body ClsSendSmsParams obj);

}
