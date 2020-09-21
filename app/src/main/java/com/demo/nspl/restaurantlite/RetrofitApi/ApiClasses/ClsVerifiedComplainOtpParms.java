package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsVerifiedComplainOtpParms {

    String MobileNumber;
    String VerificationCode;
    String OtpSendMode;

    public String getOtpSendMode() {
        return OtpSendMode;
    }

    public void setOtpSendMode(String otpSendMode) {
        OtpSendMode = otpSendMode;
    }


    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getVerificationCode() {
        return VerificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        VerificationCode = verificationCode;
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
