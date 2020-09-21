package com.demo.nspl.restaurantlite.SMS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClsSaveSMSServiceCustomerPackageParams {

    String CustomerCode ="";
    String PackageID ="";
    String OfferApplied ="";
    String OfferID ="";
    String ProductName ="";
    String IMEINumber ="";

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
    }

    public String getPackageID() {
        return PackageID;
    }

    public void setPackageID(String packageID) {
        PackageID = packageID;
    }

    public String getOfferApplied() {
        return OfferApplied;
    }

    public void setOfferApplied(String offerApplied) {
        OfferApplied = offerApplied;
    }

    public String getOfferID() {
        return OfferID;
    }

    public void setOfferID(String offerID) {
        OfferID = offerID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getIMEINumber() {
        return IMEINumber;
    }

    public void setIMEINumber(String IMEINumber) {
        this.IMEINumber = IMEINumber;
    }

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<ClsSaveSMSServiceCustomerPackageDataResponse> data = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ClsSaveSMSServiceCustomerPackageDataResponse> getData() {
        return data;
    }

    public void setData(List<ClsSaveSMSServiceCustomerPackageDataResponse> data) {
        this.data = data;
    }



}
