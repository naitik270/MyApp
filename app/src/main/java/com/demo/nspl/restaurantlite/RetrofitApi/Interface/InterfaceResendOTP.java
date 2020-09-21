package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsResendOtpParms;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InterfaceResendOTP {

    @POST("SendOTP/ResendMobileOTP")
    Call<ClsResendOtpParms> postResendOTP(@Body ClsResendOtpParms objClsResendOtpParms);

}
