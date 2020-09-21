package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsVerifiedComplainOtpParms;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InterfaceComplainVerifyOTP {

    @POST("ComplainDisposition/ComplainVerifyOTP")
    Call<ClsVerifiedComplainOtpParms> postVerifyOTP(@Body ClsVerifiedComplainOtpParms obj);

}