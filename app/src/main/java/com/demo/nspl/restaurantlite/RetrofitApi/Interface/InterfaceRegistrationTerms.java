package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsRegistrationTermsParams;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfaceRegistrationTerms {

    @GET("Terms/GetTerms")
    Call<ClsRegistrationTermsParams> value(@Query("ProductName") String productName);


}
