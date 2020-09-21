package com.demo.nspl.restaurantlite.MultipleImage;

import java.util.ArrayList;
import java.util.List;

public class ClsImgPath {

    String pathName = "";

    public List<String> getLstStrings() {
        return lstStrings;
    }

    public void setLstStrings(List<String> lstStrings) {
        this.lstStrings = lstStrings;
    }

    List<String> lstStrings = new ArrayList<>();



    int _position = 0;

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public int get_position() {
        return _position;
    }

    public void set_position(int _position) {
        this._position = _position;
    }

}
