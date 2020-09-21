package com.demo.nspl.restaurantlite.Country;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsCityList {



    @SerializedName("CityID")
    @Expose
    private Integer cityID;
    @SerializedName("CityName")
    @Expose
    private String cityName;

    public Integer getCityID() {
        return cityID;
    }

    public void setCityID(Integer cityID) {
        this.cityID = cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }



}
