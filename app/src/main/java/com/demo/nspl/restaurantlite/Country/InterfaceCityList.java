package com.demo.nspl.restaurantlite.Country;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfaceCityList {


    @GET("State/GetCityList")
    Call<ClsCityListParams> interfaceCall(@Query("StateID") int CountryID);

}
