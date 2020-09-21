package com.demo.nspl.restaurantlite.SMS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsSaveSMSServiceCustomerPackageDataResponse {

    @SerializedName("CustomerCode")
    @Expose
    private String customerCode;
    @SerializedName("TransactionRefNumber")
    @Expose
    private String transactionRefNumber;
    @SerializedName("MobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("ISTrialPackage")
    @Expose
    private String iSTrialPackage;
    @SerializedName("PaymentGatwayOrderID")
    @Expose
    private String paymentGatwayOrderID;

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getTransactionRefNumber() {
        return transactionRefNumber;
    }

    public void setTransactionRefNumber(String transactionRefNumber) {
        this.transactionRefNumber = transactionRefNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getISTrialPackage() {
        return iSTrialPackage;
    }

    public void setISTrialPackage(String iSTrialPackage) {
        this.iSTrialPackage = iSTrialPackage;
    }

    public String getPaymentGatwayOrderID() {
        return paymentGatwayOrderID;
    }

    public void setPaymentGatwayOrderID(String paymentGatwayOrderID) {
        this.paymentGatwayOrderID = paymentGatwayOrderID;
    }
}
