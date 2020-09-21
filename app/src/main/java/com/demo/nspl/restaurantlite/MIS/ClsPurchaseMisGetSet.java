package com.demo.nspl.restaurantlite.MIS;

public class ClsPurchaseMisGetSet {


    int PurchaseID = 0;
    int PurchaseNo = 0;
    int VendorID = 0;
    int PurchaseDetailID = 0;
    int ItemID = 0;

    String BillNO = "";
    String PurchaseDate = "";
    String Remark = "";
    String EntryDate = "";
    Double PurchaseVal = 0.0;
    Double PaymentVal = 0.0;
    String ItemCode = "";
    String Unit = "";
    Double Quantity = 0.0;
    Double Rate = 0.0;
    Double TotalAmount = 0.0;
    Double Discount = 0.0;
    Double NetAmount = 0.0;
    Double CGST = 0.0;
    Double SGST = 0.0;
    Double IGST = 0.0;
    Double TotalTaxAmount = 0.0;
    Double GrandTotal = 0.0;
    String ApplyTax = "";

    String VendorName= "";
    String MonthYear = "";
    String ItemName= "";
    String VendorContactNo= "";


    public String getVendorContactNo() {
        return VendorContactNo;
    }

    public void setVendorContactNo(String vendorContactNo) {
        VendorContactNo = vendorContactNo;
    }




    public String getVendorName() {
        return VendorName;
    }

    public void setVendorName(String vendorName) {
        VendorName = vendorName;
    }


    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getMonthYear() {
        return MonthYear;
    }

    public void setMonthYear(String monthYear) {
        MonthYear = monthYear;
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

    public String getBillNO() {
        return BillNO;
    }

    public void setBillNO(String billNO) {
        BillNO = billNO;
    }

    public String getPurchaseDate() {
        return PurchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        PurchaseDate = purchaseDate;
    }

    public int getVendorID() {
        return VendorID;
    }

    public void setVendorID(int vendorID) {
        VendorID = vendorID;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getEntryDate() {
        return EntryDate;
    }

    public void setEntryDate(String entryDate) {
        EntryDate = entryDate;
    }

    public Double getPurchaseVal() {
        return PurchaseVal;
    }

    public void setPurchaseVal(Double purchaseVal) {
        PurchaseVal = purchaseVal;
    }

    public Double getPaymentVal() {
        return PaymentVal;
    }

    public void setPaymentVal(Double paymentVal) {
        PaymentVal = paymentVal;
    }

    public int getPurchaseDetailID() {
        return PurchaseDetailID;
    }

    public void setPurchaseDetailID(int purchaseDetailID) {
        PurchaseDetailID = purchaseDetailID;
    }

    public int getItemID() {
        return ItemID;
    }

    public void setItemID(int itemID) {
        ItemID = itemID;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public Double getQuantity() {
        return Quantity;
    }

    public void setQuantity(Double quantity) {
        Quantity = quantity;
    }

    public Double getRate() {
        return Rate;
    }

    public void setRate(Double rate) {
        Rate = rate;
    }

    public Double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        TotalAmount = totalAmount;
    }

    public Double getDiscount() {
        return Discount;
    }

    public void setDiscount(Double discount) {
        Discount = discount;
    }

    public Double getNetAmount() {
        return NetAmount;
    }

    public void setNetAmount(Double netAmount) {
        NetAmount = netAmount;
    }

    public Double getCGST() {
        return CGST;
    }

    public void setCGST(Double CGST) {
        this.CGST = CGST;
    }

    public Double getSGST() {
        return SGST;
    }

    public void setSGST(Double SGST) {
        this.SGST = SGST;
    }

    public Double getIGST() {
        return IGST;
    }

    public void setIGST(Double IGST) {
        this.IGST = IGST;
    }

    public Double getTotalTaxAmount() {
        return TotalTaxAmount;
    }

    public void setTotalTaxAmount(Double totalTaxAmount) {
        TotalTaxAmount = totalTaxAmount;
    }

    public Double getGrandTotal() {
        return GrandTotal;
    }

    public void setGrandTotal(Double grandTotal) {
        GrandTotal = grandTotal;
    }

    public String getApplyTax() {
        return ApplyTax;
    }

    public void setApplyTax(String applyTax) {
        ApplyTax = applyTax;
    }
}
