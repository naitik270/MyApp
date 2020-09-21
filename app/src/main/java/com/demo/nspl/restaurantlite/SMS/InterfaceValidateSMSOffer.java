package com.demo.nspl.restaurantlite.SMS;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfaceValidateSMSOffer {

    @GET("SMSServicesOffer/ValidateSMSOffer")
    Call<ClsGetValidateSMSOfferParams> value(@Query("ProductName") String ProductName,
                                             @Query("CustomerCode") String CustomerCode,
                                             @Query("PackageID") String PackageID,
                                             @Query("OfferID") String OfferID,
                                             @Query("OfferCode") String OfferCode);

}
