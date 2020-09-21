package com.demo.nspl.restaurantlite.SMS;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfaceCheckCustCreditSMS {

    @GET("DeveloperApiV1/CheckCustCreditSMS")
    Call<ClsCheckCustCreditSMSParams> value(@Query("CustomerCode") String CustomerCode);

}
