package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClsLoginParams {

    String UserType;
    String Password;
    String MobileNumber;
    String ProductName;
    String ApplicationVersion;
    String IMEINumber;
    String DeviceInfo;
    String Email;

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
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

    public String getDeviceInfo() {
        return DeviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        DeviceInfo = deviceInfo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("data")
    @Expose
    private List<ClsLoginResponseList> data = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<ClsLoginResponseList> getData() {
        return data;
    }

    public void setData(List<ClsLoginResponseList> data) {
        this.data = data;
    }
}
