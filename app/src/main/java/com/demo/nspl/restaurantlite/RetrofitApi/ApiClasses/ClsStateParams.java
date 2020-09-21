package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClsStateParams {

    String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("data")
    @Expose
    private List<ClsStateGetSet> data = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<ClsStateGetSet> getData() {
        return data;
    }

    public void setData(List<ClsStateGetSet> data) {
        this.data = data;
    }
}
