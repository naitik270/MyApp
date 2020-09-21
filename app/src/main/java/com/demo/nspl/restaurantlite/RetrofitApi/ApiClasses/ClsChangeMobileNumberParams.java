package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsChangeMobileNumberParams {

    String UserType="";
    String UserCode="";
    String MobileNumber="";
    String ProductName="";
    String ApplicationVersion="";
    String IMEINumber="";
    String OTPSendMode="";

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
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

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getApplicationVersion() {
        return ApplicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        ApplicationVersion = applicationVersion;
    }

    public String getIMEINumber() {
        return IMEINumber;
    }

    public void setIMEINumber(String IMEINumber) {
        this.IMEINumber = IMEINumber;
    }

    public String getOTPSendMode() {
        return OTPSendMode;
    }

    public void setOTPSendMode(String OTPSendMode) {
        this.OTPSendMode = OTPSendMode;
    }

    @SerializedName("success")
    @Expose
    private String success;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

}
