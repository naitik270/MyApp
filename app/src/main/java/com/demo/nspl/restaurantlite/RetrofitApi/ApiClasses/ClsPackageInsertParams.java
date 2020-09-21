package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClsPackageInsertParams {
    String CustomerCode = "";
    String IMEINumber = "";
    String PackageId = "";
    String Discount = "";
    String ReferalApplied = "";
    String ReferalCode = "";
    String Mode = "";

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
    }

    public String getIMEINumber() {
        return IMEINumber;
    }

    public void setIMEINumber(String IMEINumber) {
        this.IMEINumber = IMEINumber;
    }

    public String getPackageId() {
        return PackageId;
    }

    public void setPackageId(String packageId) {
        PackageId = packageId;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getReferalApplied() {
        return ReferalApplied;
    }

    public void setReferalApplied(String referalApplied) {
        ReferalApplied = referalApplied;
    }

    public String getReferalCode() {
        return ReferalCode;
    }

    public void setReferalCode(String referalCode) {
        ReferalCode = referalCode;
    }

    public String getMode() {
        return Mode;
    }

    public void setMode(String mode) {
        Mode = mode;
    }


    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("data")
    @Expose
    private List<ClsPackageInsertList> data = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<ClsPackageInsertList> getData() {
        return data;
    }

    public void setData(List<ClsPackageInsertList> data) {
        this.data = data;
    }

}
