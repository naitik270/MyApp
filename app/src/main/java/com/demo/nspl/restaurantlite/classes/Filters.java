package com.demo.nspl.restaurantlite.classes;

public class Filters {

    String LAYERNAME;
    String LAYERVALUE;

    public Filters(){

    }


    public Filters(String LAYERNAME, String LAYERVALUE) {
        this.LAYERNAME = LAYERNAME;
        this.LAYERVALUE = LAYERVALUE;
    }

    public String getLAYERNAME() {
        return LAYERNAME;
    }

    public void setLAYERNAME(String LAYERNAME) {
        this.LAYERNAME = LAYERNAME;
    }

    public String getLAYERVALUE() {
        return LAYERVALUE;
    }

    public void setLAYERVALUE(String LAYERVALUE) {
        this.LAYERVALUE = LAYERVALUE;
    }
}
