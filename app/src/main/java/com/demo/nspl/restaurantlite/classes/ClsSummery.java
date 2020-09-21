package com.demo.nspl.restaurantlite.classes;

public class ClsSummery {
    int totalCustomer = 0;
    int creditUtilized = 0;
    int delivered = 0;
    int pending=0,DND = 0,Failed = 0;

    public int getFailed() {
        return Failed;
    }

    public void setFailed(int failed) {
        Failed = failed;
    }

    public int getDND() {
        return DND;
    }

    public void setDND(int DND) {
        this.DND = DND;
    }

    public int getTotalCustomer() {
        return totalCustomer;
    }

    public void setTotalCustomer(int totalCustomer) {
        this.totalCustomer = totalCustomer;
    }

    public int getCreditUtilized() {
        return creditUtilized;
    }

    public void setCreditUtilized(int creditUtilized) {
        this.creditUtilized = creditUtilized;
    }

    public int getDelivered() {
        return delivered;
    }

    public void setDelivered(int delivered) {
        this.delivered = delivered;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }
}
