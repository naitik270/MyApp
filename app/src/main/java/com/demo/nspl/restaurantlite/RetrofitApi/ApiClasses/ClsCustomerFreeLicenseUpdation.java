package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsCustomerFreeLicenseUpdation {

    String CustomerCode = "";
    String ReferalApplied = "";
    String ReferalCode = "";
    String Mode = "";
    String IMEINo = "";

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
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

    public String getIMEINo() {
        return IMEINo;
    }

    public void setIMEINo(String IMEINo) {
        this.IMEINo = IMEINo;
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
