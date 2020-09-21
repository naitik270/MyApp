package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsPackageList {

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

    @SerializedName("PackageID")
    @Expose
    private Integer packageID;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("ProductBrochureUrl")
    @Expose
    private Object productBrochureUrl;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Price")
    @Expose
    private Double price;
    @SerializedName("PlanValidUptoDate")
    @Expose
    private String planValidUptoDate;
    @SerializedName("ProductID")
    @Expose
    private Integer productID;
    @SerializedName("TermsFileUrl")
    @Expose
    private String termsFileUrl;
    @SerializedName("ValidityInDays")
    @Expose
    private Integer validityInDays;
    @SerializedName("IsTaxesApplicable")
    @Expose
    private String isTaxesApplicable;
    @SerializedName("SGSTValue")
    @Expose
    private Double sGSTValue;
    @SerializedName("CGSTValue")
    @Expose
    private Double cGSTValue;
    @SerializedName("IGSTValue")
    @Expose
    private Double iGSTValue;
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
    @SerializedName("PackageAmount")
    @Expose
    private Double packageAmount;
    @SerializedName("RoundOffAmount")
    @Expose
    private Double roundOffAmount;
    @SerializedName("TotalPackageAmount")
    @Expose
    private Double totalPackageAmount;

    public Integer getPackageID() {
        return packageID;
    }

    public void setPackageID(Integer packageID) {
        this.packageID = packageID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getProductBrochureUrl() {
        return productBrochureUrl;
    }

    public void setProductBrochureUrl(Object productBrochureUrl) {
        this.productBrochureUrl = productBrochureUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPlanValidUptoDate() {
        return planValidUptoDate;
    }

    public void setPlanValidUptoDate(String planValidUptoDate) {
        this.planValidUptoDate = planValidUptoDate;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public String getTermsFileUrl() {
        return termsFileUrl;
    }

    public void setTermsFileUrl(String termsFileUrl) {
        this.termsFileUrl = termsFileUrl;
    }

    public Integer getValidityInDays() {
        return validityInDays;
    }

    public void setValidityInDays(Integer validityInDays) {
        this.validityInDays = validityInDays;
    }

    public String getIsTaxesApplicable() {
        return isTaxesApplicable;
    }

    public void setIsTaxesApplicable(String isTaxesApplicable) {
        this.isTaxesApplicable = isTaxesApplicable;
    }

    public Double getSGSTValue() {
        return sGSTValue;
    }

    public void setSGSTValue(Double sGSTValue) {
        this.sGSTValue = sGSTValue;
    }

    public Double getCGSTValue() {
        return cGSTValue;
    }

    public void setCGSTValue(Double cGSTValue) {
        this.cGSTValue = cGSTValue;
    }

    public Double getIGSTValue() {
        return iGSTValue;
    }

    public void setIGSTValue(Double iGSTValue) {
        this.iGSTValue = iGSTValue;
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

    public Double getPackageAmount() {
        return packageAmount;
    }

    public void setPackageAmount(Double packageAmount) {
        this.packageAmount = packageAmount;
    }

    public Double getRoundOffAmount() {
        return roundOffAmount;
    }

    public void setRoundOffAmount(Double roundOffAmount) {
        this.roundOffAmount = roundOffAmount;
    }

    public Double getTotalPackageAmount() {
        return totalPackageAmount;
    }

    public void setTotalPackageAmount(Double totalPackageAmount) {
        this.totalPackageAmount = totalPackageAmount;
    }
}
