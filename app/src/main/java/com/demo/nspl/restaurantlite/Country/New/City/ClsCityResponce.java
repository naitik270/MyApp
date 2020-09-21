
package com.demo.nspl.restaurantlite.Country.New.City;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClsCityResponce {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("data")
    @Expose
    private List<ClsCity> data = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<ClsCity> getData() {
        return data;
    }

    public void setData(List<ClsCity> data) {
        this.data = data;
    }

}
