package com.demo.nspl.restaurantlite.SMS;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InterfaceSaveSMSServiceCustomerPackage {


    @POST("SMSServicesPackage/SaveSMSServiceCustomerPackage")
    Call<ClsSaveSMSServiceCustomerPackageParams> postSaveSms
            (@Body ClsSaveSMSServiceCustomerPackageParams
                     objClsSaveSMSServiceCustomerPackageParams);


}
