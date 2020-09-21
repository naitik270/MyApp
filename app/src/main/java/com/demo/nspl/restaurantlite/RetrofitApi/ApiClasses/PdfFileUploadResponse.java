package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PdfFileUploadResponse {

    @SerializedName("Success")
    @Expose
    String Success = "";

    @SerializedName("Message")
    @Expose
    String Message = "";

    @SerializedName("UniqCode")
    @Expose
    String UniqCode="";


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

    public String getUniqCode() {
        return UniqCode;
    }

    public void setUniqCode(String uniqCode) {
        UniqCode = uniqCode;
    }
}
