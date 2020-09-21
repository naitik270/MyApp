package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsChangePasswordParams;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InterfaceChangePassword {

    @POST("password/Changepassword")
    Call<ClsChangePasswordParams> postChangePass(@Body ClsChangePasswordParams objClsChangePassword);


}
