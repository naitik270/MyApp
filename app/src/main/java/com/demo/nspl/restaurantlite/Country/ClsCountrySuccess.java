package com.demo.nspl.restaurantlite.Country;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClsCountrySuccess {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("data")
    @Expose
    private List<ClsCountryList> data = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<ClsCountryList> getData() {
        return data;
    }

    public void setData(List<ClsCountryList> data) {
        this.data = data;
    }

}
