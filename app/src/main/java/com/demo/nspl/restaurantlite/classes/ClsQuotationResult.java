package com.demo.nspl.restaurantlite.classes;

public class ClsQuotationResult {

    int result = 0;

    public String getQuotationNo() {
        return QuotationNo;
    }

    public void setQuotationNo(String quotationNo) {
        QuotationNo = quotationNo;
    }

    String QuotationNo = "";

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

}
