package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

public class ClsSendSmsResponse {

    @SerializedName("success")
    @Expose
    private String success="";

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("SingleSMSID")
    @Expose
    @Nullable
    private String sendSMSID;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendSMSID() {
        return sendSMSID;
    }

    public void setSendSMSID(String sendSMSID) {
        this.sendSMSID = sendSMSID;
    }
}
