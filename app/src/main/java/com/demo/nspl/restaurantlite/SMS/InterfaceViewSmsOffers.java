package com.demo.nspl.restaurantlite.SMS;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfaceViewSmsOffers {

    @GET("SMSServicesOffer/GetOffer")
    Call<ClsViewSmsOffersParams> value(@Query("ProductName") String ProductName);

}
