package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClsPackageParameter {

    String ProductName;

    public String getReg_mode() {
        return reg_mode;
    }

    public void setReg_mode(String reg_mode) {
        this.reg_mode = reg_mode;
    }

    String reg_mode;

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("data")
    @Expose
    private List<ClsPackageList> data = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<ClsPackageList> getData() {
        return data;
    }

    public void setData(List<ClsPackageList> data) {
        this.data = data;
    }
}
