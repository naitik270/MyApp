package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsCurrentPackageDetailsList {


    @SerializedName("PackageName")
    @Expose
    private String packageName = "";
    @SerializedName("PackageRegisrationOnDate")
    @Expose
    private String packageRegisrationOnDate = "";
    @SerializedName("PackageExpireOnDate")
    @Expose
    private String packageExpireOnDate = "";
    @SerializedName("Price")
    @Expose
    private Integer price = 0;
    @SerializedName("Discount")
    @Expose
    private Integer discount = 0;
    @SerializedName("TotalAmount")
    @Expose
    private Integer totalAmount = 0;
    @SerializedName("ReferalApplied")
    @Expose
    private String referalApplied = "";
    @SerializedName("ReferalCode")
    @Expose
    private Object referalCode;
    @SerializedName("TransectionRefrenceNumber")
    @Expose
    private String transectionRefrenceNumber = "";
    @SerializedName("PaymentMode")
    @Expose
    private String paymentMode = "";
    @SerializedName("PaymentGateway")
    @Expose
    private String paymentGateway = "";
    @SerializedName("PaymentReferenceNumber")
    @Expose
    private Object paymentReferenceNumber;
    @SerializedName("StatusCode")
    @Expose
    private String statusCode = "";
    @SerializedName("PaymentTrancationMessage")
    @Expose
    private String paymentTrancationMessage = "";

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageRegisrationOnDate() {
        return packageRegisrationOnDate;
    }

    public void setPackageRegisrationOnDate(String packageRegisrationOnDate) {
        this.packageRegisrationOnDate = packageRegisrationOnDate;
    }

    public String getPackageExpireOnDate() {
        return packageExpireOnDate;
    }

    public void setPackageExpireOnDate(String packageExpireOnDate) {
        this.packageExpireOnDate = packageExpireOnDate;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getReferalApplied() {
        return referalApplied;
    }

    public void setReferalApplied(String referalApplied) {
        this.referalApplied = referalApplied;
    }

    public Object getReferalCode() {
        return referalCode;
    }

    public void setReferalCode(Object referalCode) {
        this.referalCode = referalCode;
    }

    public String getTransectionRefrenceNumber() {
        return transectionRefrenceNumber;
    }

    public void setTransectionRefrenceNumber(String transectionRefrenceNumber) {
        this.transectionRefrenceNumber = transectionRefrenceNumber;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(String paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public Object getPaymentReferenceNumber() {
        return paymentReferenceNumber;
    }

    public void setPaymentReferenceNumber(Object paymentReferenceNumber) {
        this.paymentReferenceNumber = paymentReferenceNumber;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getPaymentTrancationMessage() {
        return paymentTrancationMessage;
    }

    public void setPaymentTrancationMessage(String paymentTrancationMessage) {
        this.paymentTrancationMessage = paymentTrancationMessage;
    }

}
