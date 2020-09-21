
package com.demo.nspl.restaurantlite.Country.New.Country;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClsCountryResponse {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("data")
    @Expose
    private List<ClsCountry> data = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<ClsCountry> getData() {
        return data;
    }

    public void setData(List<ClsCountry> data) {
        this.data = data;
    }

}
