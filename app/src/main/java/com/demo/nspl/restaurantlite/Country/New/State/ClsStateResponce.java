
package com.demo.nspl.restaurantlite.Country.New.State;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClsStateResponce {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("data")
    @Expose
    private List<ClsState> data = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<ClsState> getData() {
        return data;
    }

    public void setData(List<ClsState> data) {
        this.data = data;
    }

}
