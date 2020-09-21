package com.demo.nspl.restaurantlite.SMS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsGetValidateSMSOfferList {

    @SerializedName("TransactionSMSTotalCreditASPerPackage")
    @Expose
    private Integer transactionSMSTotalCreditASPerPackage;
    @SerializedName("PromotionalSMSTotalCreditPerPackage")
    @Expose
    private Integer promotionalSMSTotalCreditPerPackage;
    @SerializedName("TotalSMSCreditPerPackage")
    @Expose
    private Integer totalSMSCreditPerPackage;
    @SerializedName("TransactionSMSTotalCreditASPerOffer")
    @Expose
    private Integer transactionSMSTotalCreditASPerOffer;
    @SerializedName("PromotionalSMSTotalCreditPerOffer")
    @Expose
    private Integer promotionalSMSTotalCreditPerOffer;
    @SerializedName("TotalSMSCreditPerOffer")
    @Expose
    private Integer totalSMSCreditPerOffer;
    @SerializedName("TransactionSMSTotalCredit")
    @Expose
    private Integer transactionSMSTotalCredit;
    @SerializedName("PromotionalSMSTotalCredit")
    @Expose
    private Integer promotionalSMSTotalCredit;
    @SerializedName("TotalSMSCredit")
    @Expose
    private Integer totalSMSCredit;
    @SerializedName("PackageAmount")
    @Expose
    private Double packageAmount;
    @SerializedName("DiscountInPercentage")
    @Expose
    private Double discountInPercentage;
    @SerializedName("DiscountAmount")
    @Expose
    private Double discountAmount;
    @SerializedName("TaxableAmount")
    @Expose
    private Double taxableAmount;
    @SerializedName("MaxDiscountedAmount")
    @Expose
    private Double maxDiscountedAmount;
    @SerializedName("FinalDiscountInAmount")
    @Expose
    private Double finalDiscountInAmount;
    @SerializedName("CGSTRate")
    @Expose
    private Double cGSTRate;
    @SerializedName("CGSTAmount")
    @Expose
    private Double cGSTAmount;
    @SerializedName("SGSTRate")
    @Expose
    private Double sGSTRate;
    @SerializedName("SGSTAmount")
    @Expose
    private Double sGSTAmount;
    @SerializedName("IGSTRate")
    @Expose
    private Double iGSTRate;
    @SerializedName("IGSTAmount")
    @Expose
    private Double iGSTAmount;
    @SerializedName("TotalTax")
    @Expose
    private Double totalTax;
    @SerializedName("TotalAmount")
    @Expose
    private Double totalAmount;

    public Integer getTransactionSMSTotalCreditASPerPackage() {
        return transactionSMSTotalCreditASPerPackage;
    }

    public void setTransactionSMSTotalCreditASPerPackage(Integer transactionSMSTotalCreditASPerPackage) {
        this.transactionSMSTotalCreditASPerPackage = transactionSMSTotalCreditASPerPackage;
    }

    public Integer getPromotionalSMSTotalCreditPerPackage() {
        return promotionalSMSTotalCreditPerPackage;
    }

    public void setPromotionalSMSTotalCreditPerPackage(Integer promotionalSMSTotalCreditPerPackage) {
        this.promotionalSMSTotalCreditPerPackage = promotionalSMSTotalCreditPerPackage;
    }

    public Integer getTotalSMSCreditPerPackage() {
        return totalSMSCreditPerPackage;
    }

    public void setTotalSMSCreditPerPackage(Integer totalSMSCreditPerPackage) {
        this.totalSMSCreditPerPackage = totalSMSCreditPerPackage;
    }

    public Integer getTransactionSMSTotalCreditASPerOffer() {
        return transactionSMSTotalCreditASPerOffer;
    }

    public void setTransactionSMSTotalCreditASPerOffer(Integer transactionSMSTotalCreditASPerOffer) {
        this.transactionSMSTotalCreditASPerOffer = transactionSMSTotalCreditASPerOffer;
    }

    public Integer getPromotionalSMSTotalCreditPerOffer() {
        return promotionalSMSTotalCreditPerOffer;
    }

    public void setPromotionalSMSTotalCreditPerOffer(Integer promotionalSMSTotalCreditPerOffer) {
        this.promotionalSMSTotalCreditPerOffer = promotionalSMSTotalCreditPerOffer;
    }

    public Integer getTotalSMSCreditPerOffer() {
        return totalSMSCreditPerOffer;
    }

    public void setTotalSMSCreditPerOffer(Integer totalSMSCreditPerOffer) {
        this.totalSMSCreditPerOffer = totalSMSCreditPerOffer;
    }

    public Integer getTransactionSMSTotalCredit() {
        return transactionSMSTotalCredit;
    }

    public void setTransactionSMSTotalCredit(Integer transactionSMSTotalCredit) {
        this.transactionSMSTotalCredit = transactionSMSTotalCredit;
    }

    public Integer getPromotionalSMSTotalCredit() {
        return promotionalSMSTotalCredit;
    }

    public void setPromotionalSMSTotalCredit(Integer promotionalSMSTotalCredit) {
        this.promotionalSMSTotalCredit = promotionalSMSTotalCredit;
    }

    public Integer getTotalSMSCredit() {
        return totalSMSCredit;
    }

    public void setTotalSMSCredit(Integer totalSMSCredit) {
        this.totalSMSCredit = totalSMSCredit;
    }

    public Double getPackageAmount() {
        return packageAmount;
    }

    public void setPackageAmount(Double packageAmount) {
        this.packageAmount = packageAmount;
    }

    public Double getDiscountInPercentage() {
        return discountInPercentage;
    }

    public void setDiscountInPercentage(Double discountInPercentage) {
        this.discountInPercentage = discountInPercentage;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Double getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(Double taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public Double getMaxDiscountedAmount() {
        return maxDiscountedAmount;
    }

    public void setMaxDiscountedAmount(Double maxDiscountedAmount) {
        this.maxDiscountedAmount = maxDiscountedAmount;
    }

    public Double getFinalDiscountInAmount() {
        return finalDiscountInAmount;
    }

    public void setFinalDiscountInAmount(Double finalDiscountInAmount) {
        this.finalDiscountInAmount = finalDiscountInAmount;
    }

    public Double getCGSTRate() {
        return cGSTRate;
    }

    public void setCGSTRate(Double cGSTRate) {
        this.cGSTRate = cGSTRate;
    }

    public Double getCGSTAmount() {
        return cGSTAmount;
    }

    public void setCGSTAmount(Double cGSTAmount) {
        this.cGSTAmount = cGSTAmount;
    }

    public Double getSGSTRate() {
        return sGSTRate;
    }

    public void setSGSTRate(Double sGSTRate) {
        this.sGSTRate = sGSTRate;
    }

    public Double getSGSTAmount() {
        return sGSTAmount;
    }

    public void setSGSTAmount(Double sGSTAmount) {
        this.sGSTAmount = sGSTAmount;
    }

    public Double getIGSTRate() {
        return iGSTRate;
    }

    public void setIGSTRate(Double iGSTRate) {
        this.iGSTRate = iGSTRate;
    }

    public Double getIGSTAmount() {
        return iGSTAmount;
    }

    public void setIGSTAmount(Double iGSTAmount) {
        this.iGSTAmount = iGSTAmount;
    }

    public Double getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(Double totalTax) {
        this.totalTax = totalTax;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

}
