package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsForgotPasswordParms {


    String IMEINumber;
    String MobileNumber;
    String ProductName;
    String UserType;
    String ApplicationVersion;

    public String getDeviceInfo() {
        return DeviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        DeviceInfo = deviceInfo;
    }

    String DeviceInfo;

    public String getIMEINumber() {
        return IMEINumber;
    }

    public void setIMEINumber(String IMEINumber) {
        this.IMEINumber = IMEINumber;
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

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getApplicationVersion() {
        return ApplicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        ApplicationVersion = applicationVersion;
    }

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("data")
    @Expose
    private ClsData data;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public ClsData getData() {
        return data;
    }

    public void setData(ClsData data) {
        this.data = data;
    }

}
