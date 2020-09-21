
package com.demo.nspl.restaurantlite.Country.New.City;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsCity {

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
