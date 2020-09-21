package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsVerifiedOtpParms {

    String UserCode;
    String MobileNumber;
    String VerificationCode;
    String UserType;
    String OtpSendMode;

    public String getOtpSendMode() {
        return OtpSendMode;
    }

    public void setOtpSendMode(String otpSendMode) {
        OtpSendMode = otpSendMode;
    }


    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String userCode) {
        UserCode = userCode;
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

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
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
