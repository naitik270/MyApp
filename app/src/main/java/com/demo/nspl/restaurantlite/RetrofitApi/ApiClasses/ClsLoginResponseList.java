package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsLoginResponseList {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("remaindays")
    @Expose
    private Integer remaindays;
    @SerializedName("expiredate")
    @Expose
    private String expiredate;
    @SerializedName("contactpersonname")
    @Expose
    private String contactpersonname;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("forcefullychangepassword")
    @Expose
    private String forcefullychangepassword;
    @SerializedName("businessname")
    @Expose
    private String businessname;
    @SerializedName("businessaddress")
    @Expose
    private String businessaddress;
    @SerializedName("registeredmobilenumber")
    @Expose
    private String registeredmobilenumber;
    @SerializedName("emailaddress")
    @Expose
    private String emailaddress;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("cinnumber")
    @Expose
    private String cinnumber;


    @SerializedName("gstnumber")
    @Expose
    private String gstnumber;


    @SerializedName("licenseType")
    @Expose
    private String licenseType;

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRemaindays() {
        return remaindays;
    }

    public void setRemaindays(Integer remaindays) {
        this.remaindays = remaindays;
    }

    public String getExpiredate() {
        return expiredate;
    }

    public void setExpiredate(String expiredate) {
        this.expiredate = expiredate;
    }

    public String getContactpersonname() {
        return contactpersonname;
    }

    public void setContactpersonname(String contactpersonname) {
        this.contactpersonname = contactpersonname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getForcefullychangepassword() {
        return forcefullychangepassword;
    }

    public void setForcefullychangepassword(String forcefullychangepassword) {
        this.forcefullychangepassword = forcefullychangepassword;
    }

    public String getBusinessname() {
        return businessname;
    }

    public void setBusinessname(String businessname) {
        this.businessname = businessname;
    }

    public String getBusinessaddress() {
        return businessaddress;
    }

    public void setBusinessaddress(String businessaddress) {
        this.businessaddress = businessaddress;
    }

    public String getRegisteredmobilenumber() {
        return registeredmobilenumber;
    }

    public void setRegisteredmobilenumber(String registeredmobilenumber) {
        this.registeredmobilenumber = registeredmobilenumber;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCinnumber() {
        return cinnumber;
    }

    public void setCinnumber(String cinnumber) {
        this.cinnumber = cinnumber;
    }

    public String getGstnumber() {
        return gstnumber;
    }

    public void setGstnumber(String gstnumber) {
        this.gstnumber = gstnumber;
    }
}
