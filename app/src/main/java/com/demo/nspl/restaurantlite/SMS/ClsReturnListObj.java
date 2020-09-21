package com.demo.nspl.restaurantlite.SMS;

import com.demo.nspl.restaurantlite.A_Test.ClsGetValue;

import java.util.ArrayList;
import java.util.List;

public class ClsReturnListObj {

    ClsSmsCustomerGroup obj = new ClsSmsCustomerGroup();
    List<ClsGetValue> lstClsSmsCustomerGroups = new ArrayList<>();

    public ClsSmsCustomerGroup getObj() {
        return obj;
    }

    public void setObj(ClsSmsCustomerGroup obj) {
        this.obj = obj;
    }

    public List<ClsGetValue> getLstClsSmsCustomerGroups() {
        return lstClsSmsCustomerGroups;
    }

    public void setLstClsSmsCustomerGroups(List<ClsGetValue> lstClsSmsCustomerGroups) {
        this.lstClsSmsCustomerGroups = lstClsSmsCustomerGroups;
    }
}
