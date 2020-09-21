package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsReferralApplyParams {

    String UserCode = "";
    String CustomerCode = "";
    String MacAddress = "";
    String AppType = "";

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String userCode) {
        UserCode = userCode;
    }

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
    }

    public String getMacAddress() {
        return MacAddress;
    }

    public void setMacAddress(String macAddress) {
        MacAddress = macAddress;
    }

    public String getAppType() {
        return AppType;
    }

    public void setAppType(String appType) {
        AppType = appType;
    }

    @SerializedName("Success")
    @Expose
    private String success;
    @SerializedName("MobileNumber")
    @Expose
    private String mobileNumber;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
