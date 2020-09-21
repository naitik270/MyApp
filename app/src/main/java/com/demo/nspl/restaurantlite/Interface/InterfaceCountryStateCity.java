package com.demo.nspl.restaurantlite.Interface;

import com.demo.nspl.restaurantlite.Country.New.City.ClsCityResponce;
import com.demo.nspl.restaurantlite.Country.New.Country.ClsCountryResponse;
import com.demo.nspl.restaurantlite.Country.New.State.ClsStateResponce;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfaceCountryStateCity {

    @GET("Country/GetCountryList")
    Call<ClsCountryResponse> GetCountryList();

    @GET("State/GetStateList")
    Call<ClsStateResponce> GetStateList(@Query("CountryID") int CountryID);

    @GET("State/GetCityList")
    Call<ClsCityResponce> GetCityList(@Query("StateID") int StateID);

}
