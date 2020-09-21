package com.demo.nspl.restaurantlite.Country;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsStateList {

    boolean isHeader;

    public String getFirst_letter() {
        return first_letter;
    }

    public void setFirst_letter(String first_letter) {
        this.first_letter = first_letter;
    }

    String first_letter;

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    @SerializedName("StateID")
    @Expose
    private Integer stateID;
    @SerializedName("StateName")
    @Expose
    private String stateName;

    public Integer getStateID() {
        return stateID;
    }

    public void setStateID(Integer stateID) {
        this.stateID = stateID;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

}
