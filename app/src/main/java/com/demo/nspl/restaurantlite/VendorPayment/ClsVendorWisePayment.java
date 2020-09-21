package com.demo.nspl.restaurantlite.VendorPayment;

public class ClsVendorWisePayment {

//    private String PurchaseDate = "";
    private String BillNO = "";

    public String getVendorName() {
        return VendorName;
    }

    public void setVendorName(String vendorName) {
        VendorName = vendorName;
    }

    private String VendorName = "";
    private String EntryDate = "";
    private String Remark = "";

    private int VendorID = 0;
    private int PurchaseID = 0;
    private int PurchaseNo = 0;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPaymentID() {
        return PaymentID;
    }

    public void setPaymentID(int paymentID) {
        PaymentID = paymentID;
    }

    public String getPaymentMounth() {
        return PaymentMounth;
    }

    public void setPaymentMounth(String paymentMounth) {
        PaymentMounth = paymentMounth;
    }

    public String getPaymentDate() {
        return PaymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        PaymentDate = paymentDate;
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

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    private double amount = 0.0;
    private double TotalTaxAmount = 0.0;

//    public String getPurchaseDate() {
//        return PurchaseDate;
//    }
//
//    public void setPurchaseDate(String purchaseDate) {
//        PurchaseDate = purchaseDate;
//    }

    public String getBillNO() {
        return BillNO;
    }

    public void setBillNO(String billNO) {
        BillNO = billNO;
    }

    public String getEntryDate() {
        return EntryDate;
    }

    public void setEntryDate(String entryDate) {
        EntryDate = entryDate;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public int getVendorID() {
        return VendorID;
    }

    public void setVendorID(int vendorID) {
        VendorID = vendorID;
    }

    public int getPurchaseID() {
        return PurchaseID;
    }

    public void setPurchaseID(int purchaseID) {
        PurchaseID = purchaseID;
    }

    public int getPurchaseNo() {
        return PurchaseNo;
    }

    public void setPurchaseNo(int purchaseNo) {
        PurchaseNo = purchaseNo;
    }

    public double getTotalTaxAmount() {
        return TotalTaxAmount;
    }

    public void setTotalTaxAmount(double totalTaxAmount) {
        TotalTaxAmount = totalTaxAmount;
    }


    int PaymentID = 0;
    String PaymentMounth = "", PaymentDate = "",
            PaymentMode = "", PaymentDetail = "", CustomerName = "",
            ReceiptNo = "", MobileNo = "";

}
