package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

public class ClsMobileStatus  {


    @SerializedName("MobileNo")
    @Expose
    String MobileNo = "";

    @SerializedName("CreditUsed")
    @Expose
    int CreditUsed;

    @SerializedName("UtilizeType")
    @Expose
    String UtilizeType = "";

    @SerializedName("Status")
    @Expose
    @Nullable
    String Status = "";

    @SerializedName("StatusDateTime")
    @Expose
    String StatusDateTime = "";

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public int getCreditUsed() {
        return CreditUsed;
    }

    public void setCreditUsed(int creditUsed) {
        CreditUsed = creditUsed;
    }

    public String getUtilizeType() {
        return UtilizeType;
    }

    public void setUtilizeType(String utilizeType) {
        UtilizeType = utilizeType;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getStatusDateTime() {
        return StatusDateTime;
    }

    public void setStatusDateTime(String statusDateTime) {
        StatusDateTime = statusDateTime;
    }
}
