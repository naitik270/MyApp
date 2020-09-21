package com.demo.nspl.restaurantlite.SMS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsUpdateSMSServicePackagePayment {

    String CustomerCode="";
    String TransactionReferenceNumber="";
    String PaymentStatus="";
    String PaymentMode="";
    String PaymentGateway="";
    String PaymentReferenceNumber="";
    int SatusCode;
    String TransactionMessage="";

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
    }

    public String getTransactionReferenceNumber() {
        return TransactionReferenceNumber;
    }

    public void setTransactionReferenceNumber(String transactionReferenceNumber) {
        TransactionReferenceNumber = transactionReferenceNumber;
    }

    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        PaymentStatus = paymentStatus;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    public String getPaymentGateway() {
        return PaymentGateway;
    }

    public void setPaymentGateway(String paymentGateway) {
        PaymentGateway = paymentGateway;
    }

    public String getPaymentReferenceNumber() {
        return PaymentReferenceNumber;
    }

    public void setPaymentReferenceNumber(String paymentReferenceNumber) {
        PaymentReferenceNumber = paymentReferenceNumber;
    }

    public int getSatusCode() {
        return SatusCode;
    }

    public void setSatusCode(int satusCode) {
        SatusCode = satusCode;
    }

    public String getTransactionMessage() {
        return TransactionMessage;
    }

    public void setTransactionMessage(String transactionMessage) {
        TransactionMessage = transactionMessage;
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
