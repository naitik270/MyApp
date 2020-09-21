package com.demo.nspl.restaurantlite.SMS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClsGetValidateSMSOfferParams {


    String ProductName = "";
    String CustomerCode = "";
    String PackageID = "";
    String OfferID = "";
    String OfferCode = "";


    public String getOfferCode() {
        return OfferCode;
    }

    public void setOfferCode(String offerCode) {
        OfferCode = offerCode;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

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

    public String getOfferID() {
        return OfferID;
    }

    public void setOfferID(String offerID) {
        OfferID = offerID;
    }

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<ClsGetValidateSMSOfferList> data = null;

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

    public List<ClsGetValidateSMSOfferList> getData() {
        return data;
    }

    public void setData(List<ClsGetValidateSMSOfferList> data) {
        this.data = data;
    }


}
