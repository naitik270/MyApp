package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsComplainParams {


    String MobileNumber="";
    String OTPSendMode="";

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getOTPSendMode() {
        return OTPSendMode;
    }

    public void setOTPSendMode(String OTPSendMode) {
        this.OTPSendMode = OTPSendMode;
    }

    @SerializedName("Success")
    @Expose
    private String success;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }


}
