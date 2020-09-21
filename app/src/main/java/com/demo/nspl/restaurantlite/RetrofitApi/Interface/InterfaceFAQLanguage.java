package com.demo.nspl.restaurantlite.RetrofitApi.Interface;


import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsFAQLanguage;

import retrofit2.Call;
import retrofit2.http.GET;

public interface InterfaceFAQLanguage {

    @GET("KnowledgeBase/GetLanguageList")
    Call<ClsFAQLanguage> getAvailableFAQ_Language();


}
