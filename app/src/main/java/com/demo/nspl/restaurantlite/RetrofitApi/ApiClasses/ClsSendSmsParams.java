package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsSendSmsParams {

    @SerializedName("CustomerCode")
    @Expose
    private String customerCode;
    @SerializedName("SMSType")
    @Expose
    private String sMSType;
    @SerializedName("Source")
    @Expose
    private String source;
    @SerializedName("SenderID")
    @Expose
    private String senderID;
    @SerializedName("SendTo")
    @Expose
    private String sendTo;

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("MessageLength")
    @Expose
    private String messageLength;
    @SerializedName("ProductName")
    @Expose
    private String productName;

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getsMSType() {
        return sMSType;
    }

    public void setsMSType(String sMSType) {
        this.sMSType = sMSType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageLength() {
        return messageLength;
    }

    public void setMessageLength(String messageLength) {
        this.messageLength = messageLength;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
