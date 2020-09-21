package com.demo.nspl.restaurantlite.Complain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClsComplainSuccess {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("data")
    @Expose
    private List<ClsComplainList> data = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<ClsComplainList> getData() {
        return data;
    }

    public void setData(List<ClsComplainList> data) {
        this.data = data;
    }


}
