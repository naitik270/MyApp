package com.demo.nspl.restaurantlite.SMS;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InterfaceUpdateSMSServicePackagePayment {


    @POST("SMSServicesPackage/UpdateSMSServicePackagePayment")
    Call<ClsUpdateSMSServicePackagePayment> postUpdatePackagePayment
            (@Body ClsUpdateSMSServicePackagePayment objClsForgotPasswordParms);


}
