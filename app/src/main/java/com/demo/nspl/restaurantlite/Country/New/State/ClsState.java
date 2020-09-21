
package com.demo.nspl.restaurantlite.Country.New.State;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsState {

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
