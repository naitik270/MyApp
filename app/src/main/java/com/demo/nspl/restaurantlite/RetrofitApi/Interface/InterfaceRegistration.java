package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsRegistrationParams;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InterfaceRegistration {

    @POST("Customer/save")
    Call<ClsRegistrationParams> postRegistration(@Body ClsRegistrationParams objClsDesignationGetSet);



}
