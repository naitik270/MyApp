package com.demo.nspl.restaurantlite.classes;

/**
 * Created by Desktop on 3/31/2018.
 */

public class ClsTaxItem {
    String TaxName;
    String TaxValue;
    String Type;

    public Double get_TaxAmount() {
        return _TaxAmount;
    }

    public void set_TaxAmount(Double _TaxAmount) {
        this._TaxAmount = _TaxAmount;
    }

    Double _TaxAmount;




    public String getTaxName() {
        return TaxName;
    }

    public void setTaxName(String taxName) {
        TaxName = taxName;
    }

    public String getTaxValue() {
        return TaxValue;
    }

    public void setTaxValue(String taxValue) {
        TaxValue = taxValue;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
