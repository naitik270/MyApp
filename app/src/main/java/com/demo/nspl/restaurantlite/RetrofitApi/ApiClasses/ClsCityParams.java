package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClsCityParams {

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    String city;

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("data")
    @Expose
    private List<ClsCityGetSet> data = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<ClsCityGetSet> getData() {
        return data;
    }

    public void setData(List<ClsCityGetSet> data) {
        this.data = data;
    }

}
