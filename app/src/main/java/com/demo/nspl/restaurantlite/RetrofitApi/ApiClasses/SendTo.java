package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SendTo implements Serializable {

    @SerializedName("MOBILE")
    @Expose
    private String mOBILE ="";
    @SerializedName("NAME")
    @Expose
    private String nAME;
    @SerializedName("COMPNAY")
    @Expose
    private String cOMPNAY;
    @SerializedName("GSTNO")
    @Expose
    private String gSTNO;
    @SerializedName("CITY")
    @Expose
    private String cITY;
    @SerializedName("PINCODE")
    @Expose
    private String pINCODE;

    @SerializedName("Message")
    @Expose
    private String message;

    public SendTo(){

    }

    public SendTo(String mOBILE, String nAME, String cOMPNAY, String gSTNO, String cITY, String pINCODE, String message) {
        this.mOBILE = mOBILE;
        this.nAME = nAME;
        this.cOMPNAY = cOMPNAY;
        this.gSTNO = gSTNO;
        this.cITY = cITY;
        this.pINCODE = pINCODE;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getmOBILE() {
        return mOBILE;
    }

    public void setmOBILE(String mOBILE) {
        this.mOBILE = mOBILE;
    }

    public String getnAME() {
        return nAME;
    }

    public void setnAME(String nAME) {
        this.nAME = nAME;
    }

    public String getcOMPNAY() {
        return cOMPNAY;
    }

    public void setcOMPNAY(String cOMPNAY) {
        this.cOMPNAY = cOMPNAY;
    }

    public String getgSTNO() {
        return gSTNO;
    }

    public void setgSTNO(String gSTNO) {
        this.gSTNO = gSTNO;
    }

    public String getcITY() {
        return cITY;
    }

    public void setcITY(String cITY) {
        this.cITY = cITY;
    }

    public String getpINCODE() {
        return pINCODE;
    }

    public void setpINCODE(String pINCODE) {
        this.pINCODE = pINCODE;
    }
}
