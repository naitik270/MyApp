package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsLogoutParams;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InterfaceLogout {

    @POST("Login/logout")
    Call<ClsLogoutParams> postLogout(@Body ClsLogoutParams objClsLogoutParams);
}
