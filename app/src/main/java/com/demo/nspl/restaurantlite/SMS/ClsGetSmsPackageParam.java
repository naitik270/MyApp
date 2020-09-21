package com.demo.nspl.restaurantlite.SMS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClsGetSmsPackageParam {


String ProductName = "";
String RegistrationMode = "";

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getRegistrationMode() {
        return RegistrationMode;
    }

    public void setRegistrationMode(String registrationMode) {
        RegistrationMode = registrationMode;
    }

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("data")
    @Expose
    private List<ClsGetSmsPackageList> data = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<ClsGetSmsPackageList> getData() {
        return data;
    }

    public void setData(List<ClsGetSmsPackageList> data) {
        this.data = data;
    }


}
