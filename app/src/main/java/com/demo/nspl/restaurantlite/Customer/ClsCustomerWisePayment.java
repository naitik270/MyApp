package com.demo.nspl.restaurantlite.Customer;

public class ClsCustomerWisePayment {

    private  String SaleType = "",OrderNo = "",
            PaymentMode = "",PaymentDetail = "",BillTo = "",CustomerName = ""
            ,ReceiptNo = "",VendorName = "",MobileNo = "",PaymentMounth =""
            ,PaymentDate = "",Remark = "";

    private   int VendorID = 0;
    private Double Amount = 0.0;


    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getPaymentDate() {
        return PaymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        PaymentDate = paymentDate;
    }


    public String getSaleType() {
        return SaleType;
    }

    public void setSaleType(String saleType) {
        SaleType = saleType;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    public String getPaymentDetail() {
        return PaymentDetail;
    }

    public void setPaymentDetail(String paymentDetail) {
        PaymentDetail = paymentDetail;
    }

    public String getBillTo() {
        return BillTo;
    }

    public void setBillTo(String billTo) {
        BillTo = billTo;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getReceiptNo() {
        return ReceiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        ReceiptNo = receiptNo;
    }

    public String getVendorName() {
        return VendorName;
    }

    public void setVendorName(String vendorName) {
        VendorName = vendorName;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getPaymentMounth() {
        return PaymentMounth;
    }

    public void setPaymentMounth(String paymentMounth) {
        PaymentMounth = paymentMounth;
    }

    public int getVendorID() {
        return VendorID;
    }

    public void setVendorID(int vendorID) {
        VendorID = vendorID;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public int getPaymentID() {
        return PaymentID;
    }

    public void setPaymentID(int paymentID) {
        PaymentID = paymentID;
    }

    int PaymentID=0;
}
