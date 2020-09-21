package com.demo.nspl.restaurantlite.Complain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsComplainList {


    @SerializedName("DispositionCode")
    @Expose
    private String dispositionCode;

    public String getDispositionCode() {
        return dispositionCode;
    }

    public void setDispositionCode(String dispositionCode) {
        this.dispositionCode = dispositionCode;
    }

}
