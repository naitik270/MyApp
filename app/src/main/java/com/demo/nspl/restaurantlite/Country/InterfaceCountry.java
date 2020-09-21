package com.demo.nspl.restaurantlite.Country;

import retrofit2.Call;
import retrofit2.http.GET;

public interface InterfaceCountry {


    @GET("Country/GetCountryList")
    Call<ClsCountrySuccess> getCountryList();


}
