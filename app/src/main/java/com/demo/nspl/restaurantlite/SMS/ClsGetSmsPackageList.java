package com.demo.nspl.restaurantlite.SMS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsGetSmsPackageList {


    public String get_customerId() {
        return _customerId;
    }

    public void set_customerId(String _customerId) {
        this._customerId = _customerId;
    }

    String _customerId = "";

    public String getReg_mode() {
        return reg_mode;
    }

    public void setReg_mode(String reg_mode) {
        this.reg_mode = reg_mode;
    }

    String reg_mode = "";



    @SerializedName("SMSServicesPackageID")
    @Expose
    private Integer sMSServicesPackageID;
    @SerializedName("PackageTitle")
    @Expose
    private String packageTitle;
    @SerializedName("PackageDescription")
    @Expose
    private String packageDescription;
    @SerializedName("TermsFileUrl")
    @Expose
    private String termsFileUrl;
    @SerializedName("PackageType")
    @Expose
    private String packageType;
    @SerializedName("PackagePrice")
    @Expose
    private Double packagePrice;
    @SerializedName("CGSTRate")
    @Expose
    private Double cGSTRate;
    @SerializedName("SGSTRate")
    @Expose
    private Double sGSTRate;
    @SerializedName("IGSTRate")
    @Expose
    private Double iGSTRate;
    @SerializedName("CGSTTaxAmount")
    @Expose
    private Double cGSTTaxAmount;
    @SerializedName("SGSTTaxAmount")
    @Expose
    private Double sGSTTaxAmount;
    @SerializedName("IGSTTaxAmount")
    @Expose
    private Double iGSTTaxAmount;
    @SerializedName("TotalTaxAmount")
    @Expose
    private Double totalTaxAmount;
    @SerializedName("RoundOff")
    @Expose
    private Double roundOff;
    @SerializedName("TotalAmount")
    @Expose
    private Double totalAmount;
    @SerializedName("TotalSMSCredit")
    @Expose
    private Integer totalSMSCredit;
    @SerializedName("TransactionSMSTotalCredit")
    @Expose
    private Integer transactionSMSTotalCredit;
    @SerializedName("PromotionalSMSTotalCredit")
    @Expose
    private Integer promotionalSMSTotalCredit;
    @SerializedName("SignatureRequired")
    @Expose
    private String signatureRequired;
    @SerializedName("SignatureTexts")
    @Expose
    private Object signatureTexts;
    @SerializedName("SMSValidity")
    @Expose
    private String sMSValidity;
    @SerializedName("ValidityInDays")
    @Expose
    private Integer validityInDays;
    @SerializedName("PackageValidUpToDate")
    @Expose
    private String packageValidUpToDate;
    @SerializedName("PackageValidUpToTime")
    @Expose
    private String packageValidUpToTime;

    public Integer getSMSServicesPackageID() {
        return sMSServicesPackageID;
    }

    public void setSMSServicesPackageID(Integer sMSServicesPackageID) {
        this.sMSServicesPackageID = sMSServicesPackageID;
    }

    public String getPackageTitle() {
        return packageTitle;
    }

    public void setPackageTitle(String packageTitle) {
        this.packageTitle = packageTitle;
    }

    public String getPackageDescription() {
        return packageDescription;
    }

    public void setPackageDescription(String packageDescription) {
        this.packageDescription = packageDescription;
    }

    public String getTermsFileUrl() {
        return termsFileUrl;
    }

    public void setTermsFileUrl(String termsFileUrl) {
        this.termsFileUrl = termsFileUrl;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public Double getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(Double packagePrice) {
        this.packagePrice = packagePrice;
    }

    public Double getCGSTRate() {
        return cGSTRate;
    }

    public void setCGSTRate(Double cGSTRate) {
        this.cGSTRate = cGSTRate;
    }

    public Double getSGSTRate() {
        return sGSTRate;
    }

    public void setSGSTRate(Double sGSTRate) {
        this.sGSTRate = sGSTRate;
    }

    public Double getIGSTRate() {
        return iGSTRate;
    }

    public void setIGSTRate(Double iGSTRate) {
        this.iGSTRate = iGSTRate;
    }

    public Double getCGSTTaxAmount() {
        return cGSTTaxAmount;
    }

    public void setCGSTTaxAmount(Double cGSTTaxAmount) {
        this.cGSTTaxAmount = cGSTTaxAmount;
    }

    public Double getSGSTTaxAmount() {
        return sGSTTaxAmount;
    }

    public void setSGSTTaxAmount(Double sGSTTaxAmount) {
        this.sGSTTaxAmount = sGSTTaxAmount;
    }

    public Double getIGSTTaxAmount() {
        return iGSTTaxAmount;
    }

    public void setIGSTTaxAmount(Double iGSTTaxAmount) {
        this.iGSTTaxAmount = iGSTTaxAmount;
    }

    public Double getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(Double totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public Double getRoundOff() {
        return roundOff;
    }

    public void setRoundOff(Double roundOff) {
        this.roundOff = roundOff;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
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

    public String getSignatureRequired() {
        return signatureRequired;
    }

    public void setSignatureRequired(String signatureRequired) {
        this.signatureRequired = signatureRequired;
    }

    public Object getSignatureTexts() {
        return signatureTexts;
    }

    public void setSignatureTexts(Object signatureTexts) {
        this.signatureTexts = signatureTexts;
    }

    public String getSMSValidity() {
        return sMSValidity;
    }

    public void setSMSValidity(String sMSValidity) {
        this.sMSValidity = sMSValidity;
    }

    public Integer getValidityInDays() {
        return validityInDays;
    }

    public void setValidityInDays(Integer validityInDays) {
        this.validityInDays = validityInDays;
    }

    public String getPackageValidUpToDate() {
        return packageValidUpToDate;
    }

    public void setPackageValidUpToDate(String packageValidUpToDate) {
        this.packageValidUpToDate = packageValidUpToDate;
    }

    public String getPackageValidUpToTime() {
        return packageValidUpToTime;
    }

    public void setPackageValidUpToTime(String packageValidUpToTime) {
        this.packageValidUpToTime = packageValidUpToTime;
    }


}
