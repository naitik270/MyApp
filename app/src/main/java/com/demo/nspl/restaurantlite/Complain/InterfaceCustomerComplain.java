package com.demo.nspl.restaurantlite.Complain;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InterfaceCustomerComplain {

    @POST("ComplainDisposition/RegisterCustomerComplain")
    Call<ClsCustomerComplainParams> postComplain(@Body ClsCustomerComplainParams obj);

}
