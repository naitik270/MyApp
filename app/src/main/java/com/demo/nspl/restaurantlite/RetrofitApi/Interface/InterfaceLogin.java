package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsLoginParams;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InterfaceLogin {

    @POST("Login/Login")
    Call<ClsLoginParams> postLogin(@Body ClsLoginParams objClsLoginParams);

}
