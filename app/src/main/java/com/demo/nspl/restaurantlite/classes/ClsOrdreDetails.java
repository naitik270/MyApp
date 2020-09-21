package com.demo.nspl.restaurantlite.classes;

/**
 * Created by Desktop on 5/12/2018.
 */

public class ClsOrdreDetails {

    int OrderDetail_id,Order_id,Item_id;
    String Order_no,Item_name,Type,Status,EntryDate;
    Double Rate,Quantity,Amount;

    public int getOrderDetail_id() {
        return OrderDetail_id;
    }

    public void setOrderDetail_id(int orderDetail_id) {
        OrderDetail_id = orderDetail_id;
    }

    public int getOrder_id() {
        return Order_id;
    }

    public void setOrder_id(int order_id) {
        Order_id = order_id;
    }

    public int getItem_id() {
        return Item_id;
    }

    public void setItem_id(int item_id) {
        Item_id = item_id;
    }

    public String getOrder_no() {
        return Order_no;
    }

    public void setOrder_no(String order_no) {
        Order_no = order_no;
    }

    public String getItem_name() {
        return Item_name;
    }

    public void setItem_name(String item_name) {
        Item_name = item_name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getEntryDate() {
        return EntryDate;
    }

    public void setEntryDate(String entryDate) {
        EntryDate = entryDate;
    }

    public Double getRate() {
        return Rate;
    }

    public void setRate(Double rate) {
        Rate = rate;
    }

    public Double getQuantity() {
        return Quantity;
    }

    public void setQuantity(Double quantity) {
        Quantity = quantity;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }
}
