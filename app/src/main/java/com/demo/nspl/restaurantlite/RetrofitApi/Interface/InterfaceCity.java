package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsCityParams;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfaceCity {

    @GET("State/GetStateCityList")
    Call<ClsCityParams> interfaceCall(@Query("Mode") String mode);


}
