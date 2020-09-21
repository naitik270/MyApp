package com.demo.nspl.restaurantlite.Country;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClsCityListParams {


    int _stateID = 0;

    public int get_stateID() {
        return _stateID;
    }

    public void set_stateID(int _stateID) {
        this._stateID = _stateID;
    }

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("data")
    @Expose
    private List<ClsCityList> data = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<ClsCityList> getData() {
        return data;
    }

    public void setData(List<ClsCityList> data) {
        this.data = data;
    }


}
