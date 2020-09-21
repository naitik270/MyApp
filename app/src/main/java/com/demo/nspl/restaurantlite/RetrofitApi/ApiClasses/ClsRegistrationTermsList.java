package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsRegistrationTermsList {

    @SerializedName("TermsID")
    @Expose
    private Integer termsID;
    @SerializedName("TermsTitle")
    @Expose
    private String termsTitle;
    @SerializedName("ProductID")
    @Expose
    private Integer productID;
    @SerializedName("TermsFileUrl")
    @Expose
    private String termsFileUrl;

    public Integer getTermsID() {
        return termsID;
    }

    public void setTermsID(Integer termsID) {
        this.termsID = termsID;
    }

    public String getTermsTitle() {
        return termsTitle;
    }

    public void setTermsTitle(String termsTitle) {
        this.termsTitle = termsTitle;
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

    @Override
    public String toString() {
        return "ClsRegistrationTermsList{" +
                "termsID=" + termsID +
                ", termsTitle='" + termsTitle + '\'' +
                ", productID=" + productID +
                ", termsFileUrl='" + termsFileUrl + '\'' +
                '}';
    }

}
