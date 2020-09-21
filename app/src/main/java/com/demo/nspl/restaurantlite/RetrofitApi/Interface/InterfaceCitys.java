package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsCitys;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface InterfaceCitys {

    @GET("/data/city.json")
    Call<List<ClsCitys>> getCitys();

}
