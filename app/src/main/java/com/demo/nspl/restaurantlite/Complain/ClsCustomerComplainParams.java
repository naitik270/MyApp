package com.demo.nspl.restaurantlite.Complain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsCustomerComplainParams {

    String MobileNumber = "";
    String CustomerCode = "";
    String RequestSubject = "";
    String RequestRemark = "";
    String ProductName = "";
    String FileName = "";
    String FileExtension = "";
    String Data = "";
    String ApplicationType = "";

    public String getApplicationType() {
        return ApplicationType;
    }

    public void setApplicationType(String applicationType) {
        ApplicationType = applicationType;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
    }

    public String getRequestSubject() {
        return RequestSubject;
    }

    public void setRequestSubject(String requestSubject) {
        RequestSubject = requestSubject;
    }

    public String getRequestRemark() {
        return RequestRemark;
    }

    public void setRequestRemark(String requestRemark) {
        RequestRemark = requestRemark;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFileExtension() {
        return FileExtension;
    }

    public void setFileExtension(String fileExtension) {
        FileExtension = fileExtension;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
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
