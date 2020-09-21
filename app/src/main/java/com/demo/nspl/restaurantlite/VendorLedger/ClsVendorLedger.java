package com.demo.nspl.restaurantlite.VendorLedger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;

public class ClsVendorLedger implements Serializable {

    int VENDOR_ID = 0;
    String VENDOR_NAME = "";
    String CusomterName = "";
    String CustomerMobileNo = "";
    String CompanyName = "";
    String GST_NO = "";
    Double TotalPurchaseAmount = 0.0;
    Double TotalPaymentAmount = 0.0;
    Double TotalSaleAmount = 0.0;
    Double creditLimit = 0.0;
    Double OpeningStock = 0.0;


    public Double getOpeningStock() {
        return OpeningStock;
    }

    public void setOpeningStock(Double openingStock) {
        OpeningStock = openingStock;
    }

    public Double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Double creditLimit) {
        this.creditLimit = creditLimit;
    }


    public String getCustomerName() {
        return CusomterName;
    }

    public void setCusomterName(String cusomterName) {
        CusomterName = cusomterName;
    }

    public String getCustomerMobileNo() {
        return CustomerMobileNo;
    }

    public void setCustomerMobileNo(String customerMobileNo) {
        CustomerMobileNo = customerMobileNo;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getGST_NO() {
        return GST_NO;
    }

    public void setGST_NO(String GST_NO) {
        this.GST_NO = GST_NO;
    }

    public Double getTotalSaleAmount() {
        return TotalSaleAmount;
    }

    public void setTotalSaleAmount(Double totalSaleAmount) {
        TotalSaleAmount = totalSaleAmount;
    }

    public int getVENDOR_ID() {
        return VENDOR_ID;
    }

    public void setVENDOR_ID(int VENDOR_ID) {
        this.VENDOR_ID = VENDOR_ID;
    }

    public String getVENDOR_NAME() {
        return VENDOR_NAME;
    }

    public void setVENDOR_NAME(String VENDOR_NAME) {
        this.VENDOR_NAME = VENDOR_NAME;
    }

    public Double getTotalPurchaseAmount() {
        return TotalPurchaseAmount;
    }

    public void setTotalPurchaseAmount(Double totalPurchaseAmount) {
        TotalPurchaseAmount = totalPurchaseAmount;
    }

    public Double getTotalPaymentAmount() {
        return TotalPaymentAmount;
    }

    public void setTotalPaymentAmount(Double totalPaymentAmount) {
        TotalPaymentAmount = totalPaymentAmount;
    }


    private static int result;
    static Context context;
    static SQLiteDatabase db;

    public ClsVendorLedger(Context ctx) {

        context = ctx;
    }

    public ClsVendorLedger() {
    }


}
