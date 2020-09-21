package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsStateParams;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfaceState {

    @GET("State/GetStateCityList")
    Call<ClsStateParams> interfaceCall(@Query("Mode") String mode);


}
