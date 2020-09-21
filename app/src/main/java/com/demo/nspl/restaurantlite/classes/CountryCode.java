package com.demo.nspl.restaurantlite.classes;

public class CountryCode {

    int id;
    String CountryName = "", CountryCode = "";

    public CountryCode(String countryCode,String countryName,int id) {
        CountryName = countryName;
        CountryCode = countryCode;
        this.id  = id;

    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }
}
