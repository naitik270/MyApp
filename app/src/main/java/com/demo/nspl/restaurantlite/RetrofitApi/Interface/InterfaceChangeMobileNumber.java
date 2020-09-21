package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsChangeMobileNumberParams;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InterfaceChangeMobileNumber {

    @POST("password/ChangeMobileNumber")
    Call<ClsChangeMobileNumberParams> postChangeMobileNumber(@Body ClsChangeMobileNumberParams objClsChangeMobileNumberParams);

}
