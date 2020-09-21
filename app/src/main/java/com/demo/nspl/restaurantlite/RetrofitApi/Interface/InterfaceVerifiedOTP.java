package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsVerifiedOtpParms;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InterfaceVerifiedOTP {

    @POST("SendOTP/VerifyMobileOTP")
    Call<ClsVerifiedOtpParms> postVerificationOTP(@Body ClsVerifiedOtpParms objClsVerifiedOtpParms);

}
