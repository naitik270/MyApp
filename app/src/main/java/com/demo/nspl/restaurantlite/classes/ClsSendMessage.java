package com.demo.nspl.restaurantlite.classes;

import java.util.List;

public class ClsSendMessage {

    private ClsPaymentMaster getClsPaymentMaster;
    private List<ClsInventoryOrderDetail> list_Current_Order;


    public ClsPaymentMaster getGetClsPaymentMaster() {
        return getClsPaymentMaster;
    }

    public void setGetClsPaymentMaster(ClsPaymentMaster getClsPaymentMaster) {
        this.getClsPaymentMaster = getClsPaymentMaster;
    }

    public List<ClsInventoryOrderDetail> getList_Current_Order() {
        return list_Current_Order;
    }

    public void setList_Current_Order(List<ClsInventoryOrderDetail> list_Current_Order) {
        this.list_Current_Order = list_Current_Order;
    }
}
