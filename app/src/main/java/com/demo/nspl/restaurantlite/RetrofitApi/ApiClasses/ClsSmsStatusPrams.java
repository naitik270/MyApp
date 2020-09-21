package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsSmsStatusPrams {

    @SerializedName("CustomerCode")
    @Expose
    String CustomerCode="";
    @SerializedName("SendSMSID")
    @Expose
    String SendSMSID="";

//    @SerializedName("Mobiles")
//    @Expose
//    List<String> listOfMobileNumbers = new ArrayList<>();

    @SerializedName("Mobiles")
    @Expose
    String listOfMobileNumbers_Str = "";

    @SerializedName("Type")
    @Expose
    String Type = "";

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
    }

    public String getSendSMSID() {
        return SendSMSID;
    }

    public void setSendSMSID(String sendSMSID) {
        SendSMSID = sendSMSID;
    }

    public String getListOfMobileNumbers() {
        return listOfMobileNumbers_Str;
    }

    public void setListOfMobileNumbers(String listOfMobileNumbers) {
        this.listOfMobileNumbers_Str = listOfMobileNumbers;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
