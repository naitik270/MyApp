package com.demo.nspl.restaurantlite.RetrofitApi.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsPackagePaymentParams;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InterfacePackagePayment {

    @POST("CustomerPackage/UpdatePackagePayment")
    Call<ClsPackagePaymentParams> postPackagePayment(@Body ClsPackagePaymentParams objClsPackagePaymentParams);
}
