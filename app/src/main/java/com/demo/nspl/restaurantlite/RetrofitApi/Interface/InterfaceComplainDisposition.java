package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsComplainParams;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InterfaceComplainDisposition {

    @POST("ComplainDisposition/ComplainSendOTP")
    Call<ClsComplainParams> postComplain(@Body ClsComplainParams objClsComplainParams);
}
