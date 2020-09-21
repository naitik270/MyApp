package com.demo.nspl.restaurantlite.Country;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClsStateListParams {


    String CountryID = "";

    public String getCountryID() {
        return CountryID;
    }

    public void setCountryID(String countryID) {
        CountryID = countryID;
    }

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("data")
    @Expose
    private List<ClsStateList> data = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<ClsStateList> getData() {
        return data;
    }

    public void setData(List<ClsStateList> data) {
        this.data = data;
    }

}
