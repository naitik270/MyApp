package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsBackupTypeParams {

    String CustomerID = "";
    String BackupType = "";
    String ProductName = "";
    String AppVersion = "";
    String AppType = "";
    String IMEINumber = "";
    String DeviceInfo = "";
    String Remark = "";
    String FileName = "";
    String Extentsion = "";
    String Data = "";

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getBackupType() {
        return BackupType;
    }

    public void setBackupType(String backupType) {
        BackupType = backupType;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getAppVersion() {
        return AppVersion;
    }

    public void setAppVersion(String appVersion) {
        AppVersion = appVersion;
    }

    public String getAppType() {
        return AppType;
    }

    public void setAppType(String appType) {
        AppType = appType;
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

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getExtentsion() {
        return Extentsion;
    }

    public void setExtentsion(String extentsion) {
        Extentsion = extentsion;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
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
