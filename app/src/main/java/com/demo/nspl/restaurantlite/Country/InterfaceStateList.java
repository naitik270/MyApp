package com.demo.nspl.restaurantlite.Country;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfaceStateList {


    @GET("State/GetStateList")
    Call<ClsStateListParams> interfaceCall(@Query("CountryID") int CountryID);

}
