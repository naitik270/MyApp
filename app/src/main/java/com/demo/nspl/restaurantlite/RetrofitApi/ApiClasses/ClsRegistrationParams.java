package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsRegistrationParams {

    String ProductName;
    String AppType;
    String CompanyName;
    String CINNo;
    String GSTNo;
    String Address;
    String PinCode;
    String State;
    String City;
    String MobileNo;
    String Email;
    String AlternetMobileNo;
    String RegistredDeviceInfo;
    String MACAddress;
    String ApplicationVersion;
    String CapturedAddress;
    String latitude;
    String longitude;
    String ContactPerson;
    String ContactPersonMobileNo;
    String StatusRemark;

    public String getLicenseType() {
        return LicenseType;
    }

    public void setLicenseType(String licenseType) {
        LicenseType = licenseType;
    }

    String LicenseType = "";


    int CountryID = 0;
    int StateID = 0;
    int CityID = 0;

    public int getCountryID() {
        return CountryID;
    }

    public void setCountryID(int countryID) {
        CountryID = countryID;
    }

    public int getStateID() {
        return StateID;
    }

    public void setStateID(int stateID) {
        StateID = stateID;
    }

    public int getCityID() {
        return CityID;
    }

    public void setCityID(int cityID) {
        CityID = cityID;
    }


    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getAppType() {
        return AppType;
    }

    public void setAppType(String appType) {
        AppType = appType;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getCINNo() {
        return CINNo;
    }

    public void setCINNo(String CINNo) {
        this.CINNo = CINNo;
    }

    public String getGSTNo() {
        return GSTNo;
    }

    public void setGSTNo(String GSTNo) {
        this.GSTNo = GSTNo;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        PinCode = pinCode;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAlternetMobileNo() {
        return AlternetMobileNo;
    }

    public void setAlternetMobileNo(String alternetMobileNo) {
        AlternetMobileNo = alternetMobileNo;
    }

    public String getRegistredDeviceInfo() {
        return RegistredDeviceInfo;
    }

    public void setRegistredDeviceInfo(String registredDeviceInfo) {
        RegistredDeviceInfo = registredDeviceInfo;
    }

    public String getMACAddress() {
        return MACAddress;
    }

    public void setMACAddress(String MACAddress) {
        this.MACAddress = MACAddress;
    }

    public String getApplicationVersion() {
        return ApplicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        ApplicationVersion = applicationVersion;
    }

    public String getCapturedAddress() {
        return CapturedAddress;
    }

    public void setCapturedAddress(String capturedAddress) {
        CapturedAddress = capturedAddress;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String contactPerson) {
        ContactPerson = contactPerson;
    }

    public String getContactPersonMobileNo() {
        return ContactPersonMobileNo;
    }

    public void setContactPersonMobileNo(String contactPersonMobileNo) {
        ContactPersonMobileNo = contactPersonMobileNo;
    }

    public String getStatusRemark() {
        return StatusRemark;
    }

    public void setStatusRemark(String statusRemark) {
        StatusRemark = statusRemark;
    }


    @SerializedName("success")
    @Expose
    private String success;

    @SerializedName("customerid")
    @Expose
    private String customerid;

    @SerializedName("customerstatus")
    @Expose
    private String customerstatus;

    @SerializedName("licensetype")
    @Expose
    private String licensetype;

    public String getCustomerstatus() {
        return customerstatus;
    }

    public void setCustomerstatus(String customerstatus) {
        this.customerstatus = customerstatus;
    }

    public String getLicensetype() {
        return licensetype;
    }

    public void setLicensetype(String licensetype) {
        this.licensetype = licensetype;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

}
