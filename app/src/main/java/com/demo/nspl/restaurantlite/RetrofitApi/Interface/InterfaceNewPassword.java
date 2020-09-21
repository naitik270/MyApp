package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsNewPasswordParams;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InterfaceNewPassword {

    @POST("password/ResetPassword")
    Call<ClsNewPasswordParams> postNewPassword(@Body ClsNewPasswordParams objClsNewPasswordParams);


}
