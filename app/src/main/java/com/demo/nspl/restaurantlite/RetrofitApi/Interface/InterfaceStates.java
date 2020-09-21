package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsState;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface InterfaceStates {

    @GET("/data/state.json")
    Call<List<ClsState>> getStates();

}
