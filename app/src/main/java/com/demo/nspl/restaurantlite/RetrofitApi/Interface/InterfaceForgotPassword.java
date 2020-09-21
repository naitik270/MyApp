package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsForgotPasswordParms;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InterfaceForgotPassword {

    @POST("password/ForgotPassword")
    Call<ClsForgotPasswordParms> postForgotPass(@Body ClsForgotPasswordParms objClsForgotPasswordParms);


}
