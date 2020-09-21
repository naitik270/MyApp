package com.demo.nspl.restaurantlite.SMS;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfaceGetSmsPackages {

    @GET("SMSServicesPackage/GetSMSServicesPackage")
    Call<ClsGetSmsPackageParam> value(@Query("ProductName") String ProductName,
                                    @Query("RegistrationMode") String RegistrationMode);

}
