package com.demo.nspl.restaurantlite.SMS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsViewSmsOffersList {


    @SerializedName("SMSServicesOfferID")
    @Expose
    private Integer sMSServicesOfferID;
    @SerializedName("OfferTitle")
    @Expose
    private String offerTitle;
    @SerializedName("OfferDescription")
    @Expose
    private String offerDescription;
    @SerializedName("TermsFileUrl")
    @Expose
    private String termsFileUrl;
    @SerializedName("OfferCode")
    @Expose
    private String offerCode;
    @SerializedName("ValidityFromDate")
    @Expose
    private String validityFromDate;
    @SerializedName("ValidityFromTime")
    @Expose
    private String validityFromTime;
    @SerializedName("ValidityToDate")
    @Expose
    private String validityToDate;
    @SerializedName("ValidityToTime")
    @Expose
    private String validityToTime;
    @SerializedName("ApplicablePerUser")
    @Expose
    private Integer applicablePerUser;
    @SerializedName("OfferType")
    @Expose
    private String offerType;
    @SerializedName("TotalSMSCredit")
    @Expose
    private Integer totalSMSCredit;
    @SerializedName("TransactionSMSTotalCredit")
    @Expose
    private Integer transactionSMSTotalCredit;
    @SerializedName("PromotionalSMSTotalCredit")
    @Expose
    private Integer promotionalSMSTotalCredit;
    @SerializedName("DiscountType")
    @Expose
    private String discountType;
    @SerializedName("DiscountedValue")
    @Expose
    private Integer discountedValue;
    @SerializedName("MaxDiscountedAmount")
    @Expose
    private Integer maxDiscountedAmount;
    @SerializedName("ISDocumentAvailable")
    @Expose
    private String iSDocumentAvailable;
    @SerializedName("DocumentUrl")
    @Expose
    private String documentUrl;

    public Integer getSMSServicesOfferID() {
        return sMSServicesOfferID;
    }

    public void setSMSServicesOfferID(Integer sMSServicesOfferID) {
        this.sMSServicesOfferID = sMSServicesOfferID;
    }

    public String getOfferTitle() {
        return offerTitle;
    }

    public void setOfferTitle(String offerTitle) {
        this.offerTitle = offerTitle;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }

    public String getTermsFileUrl() {
        return termsFileUrl;
    }

    public void setTermsFileUrl(String termsFileUrl) {
        this.termsFileUrl = termsFileUrl;
    }

    public String getOfferCode() {
        return offerCode;
    }

    public void setOfferCode(String offerCode) {
        this.offerCode = offerCode;
    }

    public String getValidityFromDate() {
        return validityFromDate;
    }

    public void setValidityFromDate(String validityFromDate) {
        this.validityFromDate = validityFromDate;
    }

    public String getValidityFromTime() {
        return validityFromTime;
    }

    public void setValidityFromTime(String validityFromTime) {
        this.validityFromTime = validityFromTime;
    }

    public String getValidityToDate() {
        return validityToDate;
    }

    public void setValidityToDate(String validityToDate) {
        this.validityToDate = validityToDate;
    }

    public String getValidityToTime() {
        return validityToTime;
    }

    public void setValidityToTime(String validityToTime) {
        this.validityToTime = validityToTime;
    }

    public Integer getApplicablePerUser() {
        return applicablePerUser;
    }

    public void setApplicablePerUser(Integer applicablePerUser) {
        this.applicablePerUser = applicablePerUser;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public Integer getTotalSMSCredit() {
        return totalSMSCredit;
    }

    public void setTotalSMSCredit(Integer totalSMSCredit) {
        this.totalSMSCredit = totalSMSCredit;
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

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public Integer getDiscountedValue() {
        return discountedValue;
    }

    public void setDiscountedValue(Integer discountedValue) {
        this.discountedValue = discountedValue;
    }

    public Integer getMaxDiscountedAmount() {
        return maxDiscountedAmount;
    }

    public void setMaxDiscountedAmount(Integer maxDiscountedAmount) {
        this.maxDiscountedAmount = maxDiscountedAmount;
    }

    public String getISDocumentAvailable() {
        return iSDocumentAvailable;
    }

    public void setISDocumentAvailable(String iSDocumentAvailable) {
        this.iSDocumentAvailable = iSDocumentAvailable;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

}
