package com.demo.nspl.restaurantlite.Stock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ClsStockSale {


    int OrderNo = 0;
    String BillDate = "";
    String MobileNo = "";
    String CustomerName = "";
    Double SaleRate = 0.0;
    Double Quantity = 0.0;
    Double Amount = 0.0;
    Double TotalTaxAmount = 0.0;
    Double GrandTotal = 0.0;
    String ApplyTax= "";
    String ItemCode = "";
    String ItemName = "";

    public String getApplyTax() {
        return ApplyTax;
    }

    public void setApplyTax(String applyTax) {
        ApplyTax = applyTax;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(int orderNo) {
        OrderNo = orderNo;
    }

    public String getBillDate() {
        return BillDate;
    }

    public void setBillDate(String billDate) {
        BillDate = billDate;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public Double getSaleRate() {
        return SaleRate;
    }

    public void setSaleRate(Double saleRate) {
        SaleRate = saleRate;
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

    private static int result;
    static Context context;
    static SQLiteDatabase db;

    public ClsStockSale(Context ctx) {

        context = ctx;
    }

    public ClsStockSale() {
    }


    @SuppressLint("WrongConstant")
    public static List<ClsStockSale> getStockSaleList(String _where, Context context) {
        List<ClsStockSale> lstClsPurchaseDetails = new ArrayList<>();
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String strSql = "SELECT "
                    .concat(" ord.[OrderNo]  ")
                    .concat(", ord.[BillDate]  ")
                    .concat(", ord.[MobileNo]  ")
                    .concat(", ord.[CustomerName]  ")
                    .concat(", ord.[ApplyTax]  ")

                    .concat(", dtl.[SaleRate]  ")
                    .concat(", dtl.[Quantity]  ")
                    .concat(", dtl.[Amount]  ")
                    .concat(", dtl.[TotalTaxAmount]  ")
                    .concat(", dtl.[GrandTotal]  ")

                    .concat(" FROM [InventoryOrderMaster] AS [ord] ")
                    .concat(" INNER JOIN [InventoryOrderDetail] AS [dtl] ON [dtl].[OrderID] = [ord].[OrderID] ")
                    .concat(" WHERE 1=1 ")
                    .concat(_where)
                    .concat(" ORDER BY ord.[BillDate] ")
                    .concat(" ,ord.[OrderNo] DESC ");


            Log.e("--Select--", ">>>>>>>>>" + strSql);
            Cursor cur = db.rawQuery(strSql, null);
            Log.e("--Select--", ">>>>>>>>>" + cur.getCount());

            while (cur.moveToNext()) {
                ClsStockSale Obj = new ClsStockSale();

                Gson gson2 = new Gson();
                String jsonInString2 = gson2.toJson(Obj);
                Log.e("--GSON--", "StockSale:--- " + jsonInString2);


                Obj.setOrderNo(cur.getInt(cur.getColumnIndex("OrderNo")));
                Obj.setBillDate(cur.getString(cur.getColumnIndex("BillDate")));
                Obj.setMobileNo(cur.getString(cur.getColumnIndex("MobileNo")));
                Obj.setSaleRate(cur.getDouble(cur.getColumnIndex("SaleRate")));
                Obj.setCustomerName(cur.getString(cur.getColumnIndex("CustomerName")));
                Obj.setQuantity(cur.getDouble(cur.getColumnIndex("Quantity")));
                Obj.setAmount(cur.getDouble(cur.getColumnIndex("Amount")));
                Obj.setTotalTaxAmount(cur.getDouble(cur.getColumnIndex("TotalTaxAmount")));
                Obj.setApplyTax(cur.getString(cur.getColumnIndex("ApplyTax")));
                Obj.setGrandTotal(cur.getDouble(cur.getColumnIndex("GrandTotal")));

                lstClsPurchaseDetails.add(Obj);
            }
            db.close();

        } catch (Exception e) {
            Log.e("ClsCategory", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }
        return lstClsPurchaseDetails;
    }


}
