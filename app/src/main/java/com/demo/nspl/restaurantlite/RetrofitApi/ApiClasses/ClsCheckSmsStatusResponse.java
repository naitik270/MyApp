package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ClsCheckSmsStatusResponse {


    @SerializedName("Success")
    @Expose
    String Success= "";

    @SerializedName("Message")
    @Expose
    String Message = "";

    @SerializedName("data")
    @Expose
    List<ClsMobileStatus> list = new ArrayList<>();

    public String getSuccess() {
        return Success;
    }

    public void setSuccess(String success) {
        Success = success;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public List<ClsMobileStatus> getList() {
        return list;
    }

    public void setList(List<ClsMobileStatus> list) {
        this.list = list;
    }
}
