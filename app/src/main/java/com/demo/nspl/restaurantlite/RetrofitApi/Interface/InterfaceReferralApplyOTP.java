package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsReferralApplyParams;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InterfaceReferralApplyOTP {

    @POST("SendOTP/ReferralApply")
    Call<ClsReferralApplyParams> postReferApply(@Body ClsReferralApplyParams objClsVerifiedOtpParms);
}
