package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsCustomerProfileUpdateParams {

    String CustomerCode = "";
    String ProductName = "";
    String ContactPerson = "";
    String CINNo = "";
    String GSTNo = "";

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String contactPerson) {
        ContactPerson = contactPerson;
    }

    public String getCINNo() {
        return CINNo;
    }

    public void setCINNo(String CINNo) {
        this.CINNo = CINNo;
    }

    public String getGSTNo() {
        return GSTNo;
    }

    public void setGSTNo(String GSTNo) {
        this.GSTNo = GSTNo;
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
