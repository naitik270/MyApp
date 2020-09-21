package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClsGetBackupDetailsParams {

    String CustomerID = "";
    String BackupType = "";

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    String ProductName = "";

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

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("data")
    @Expose
    private List<ClsGetBackupDetailsList> data = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<ClsGetBackupDetailsList> getData() {
        return data;
    }

    public void setData(List<ClsGetBackupDetailsList> data) {
        this.data = data;
    }


}
