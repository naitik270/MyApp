package com.demo.nspl.restaurantlite.Purchase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.classes.ClsVendor;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ClsPurchaseDetail {


    int PurchaseDetailID = 0;
    int PurchaseID = 0;
    int ItemID = 0;
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

    public String getVENDOR_NAME() {
        return VENDOR_NAME;
    }

    public void setVENDOR_NAME(String VENDOR_NAME) {
        this.VENDOR_NAME = VENDOR_NAME;
    }

    String VENDOR_NAME = "";

    public String getPurchaseDate() {
        return PurchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        PurchaseDate = purchaseDate;
    }

    String PurchaseDate = "";

    public String getMonthYear() {
        return MonthYear;
    }

    public void setMonthYear(String monthYear) {
        MonthYear = monthYear;
    }

    String MonthYear = "";

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    String ItemName = "";


    private static int result;
    static Context context;
    static SQLiteDatabase db;

    public ClsPurchaseDetail(Context ctx) {

        context = ctx;
    }

    public ClsPurchaseDetail() {
    }


    public int getPurchaseDetailID() {
        return PurchaseDetailID;
    }

    public void setPurchaseDetailID(int purchaseDetailID) {
        PurchaseDetailID = purchaseDetailID;
    }

    public int getPurchaseID() {
        return PurchaseID;
    }

    public void setPurchaseID(int purchaseID) {
        PurchaseID = purchaseID;
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


    int PurchaseNo = 0;
    int VendorID = 0;
    String BillNO = "";

    public int getPurchaseNo() {
        return PurchaseNo;
    }

    public void setPurchaseNo(int purchaseNo) {
        PurchaseNo = purchaseNo;
    }

    public int getVendorID() {
        return VendorID;
    }

    public void setVendorID(int vendorID) {
        VendorID = vendorID;
    }

    public String getBillNO() {
        return BillNO;
    }

    public void setBillNO(String billNO) {
        BillNO = billNO;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    String Remark = "";

    public String getEntryDate() {
        return EntryDate;
    }

    public void setEntryDate(String entryDate) {
        EntryDate = entryDate;
    }

    String EntryDate = "";

    @SuppressLint("WrongConstant")
    public static List<ClsPurchaseDetail> getPurchaseListByMonth(String _where, Context context) {

        List<ClsPurchaseDetail> lstClsPurchaseMasters = new ArrayList<>();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
//            String sqlStr = "select * FROM [PurchaseMaster]  where 1=1 ".concat(_where);
            String sqlStr = "SELECT PM.*,V.[VENDOR_NAME] ,SUM(PD.[GrandTotal]) AS GrandTotal FROM [PurchaseMaster] AS PM "
                    .concat(" INNER JOIN [VENDOR_MASTER] AS V ON V.[VENDOR_ID] = PM.[VendorID] ")
                    .concat(" INNER JOIN [PurchaseDetail] AS PD ON PD.[PurchaseID] = PM.[PurchaseID] ")
                    .concat(" WHERE 1=1 ")
                    .concat(_where)
                    .concat(" GROUP BY PM.PurchaseID,PM.PurchaseNo,PM.VendorID,PM.BillNO,PM.PurchaseDate,PM.Remark,PM.EntryDate,V.[VENDOR_NAME]")
                    .concat(";");

            Log.e("--New_Purchase--", "Qry: " + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            Log.e("--New_Purchase--", "Qry_Count: " + cur.getCount());

            while (cur.moveToNext()) {
                ClsPurchaseDetail Obj = new ClsPurchaseDetail();

                Obj.setPurchaseID(cur.getInt(cur.getColumnIndex("PurchaseID")));
                Obj.setPurchaseNo(cur.getInt(cur.getColumnIndex("PurchaseNo")));
                Obj.setVendorID(cur.getInt(cur.getColumnIndex("VendorID")));
                Obj.setBillNO(cur.getString(cur.getColumnIndex("BillNO")));
                Obj.setVENDOR_NAME(cur.getString(cur.getColumnIndex("VENDOR_NAME")));
                Obj.setPurchaseDate(cur.getString(cur.getColumnIndex("PurchaseDate")));
                Obj.setGrandTotal(cur.getDouble(cur.getColumnIndex("GrandTotal")));
                Obj.setRemark(cur.getString(cur.getColumnIndex("Remark")));
                Obj.setEntryDate(cur.getString(cur.getColumnIndex("EntryDate")));


                lstClsPurchaseMasters.add(Obj);
            }
            db.close();

        } catch (Exception e) {
            Log.e("ClsCategory", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }
        return lstClsPurchaseMasters;
    }

    double _allTotal = 0.0;


    public double get_allTotal() {
        return _allTotal;
    }

    public void set_allTotal(double _allTotal) {
        this._allTotal = _allTotal;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsPurchaseDetail> getPurchaseItemList(String _where, Context context) {


        List<ClsPurchaseDetail> lstClsPurchaseDetails = new ArrayList<>();
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            /*String qry = "SELECT PD.* ,SUM(PD.[GrandTotal]) as AllTotal , ITM.[ITEM_NAME] FROM [PurchaseDetail] AS PD "
                    .concat(" INNER JOIN [tbl_LayerItem_Master] AS ITM ON ITM.[LAYERITEM_ID] = PD.[ItemID] ")
                    .concat(" WHERE 1=1 ")
                    .concat(_where)
                    .concat(";");


*/
            String qry = "SELECT PD.* , ITM.[ITEM_NAME] FROM [PurchaseDetail] AS PD "
                    .concat(" INNER JOIN [tbl_LayerItem_Master] AS ITM ON ITM.[LAYERITEM_ID] = PD.[ItemID] ")
                    .concat(" WHERE 1=1 ")
                    .concat(_where)
                    .concat(";");


            Log.e("--Select--", ">>>>>>>>>" + qry);
            Cursor cur = db.rawQuery(qry, null);
            Log.e("--Select--", ">>>>>>>>>" + cur.getCount());

            while (cur.moveToNext()) {
                ClsPurchaseDetail Obj = new ClsPurchaseDetail();

                Obj.setItemID(cur.getInt(cur.getColumnIndex("ItemID")));
                Obj.setItemCode(cur.getString(cur.getColumnIndex("ItemCode")));
                Obj.setItemName(cur.getString(cur.getColumnIndex("ITEM_NAME")));
                Obj.setUnit(cur.getString(cur.getColumnIndex("Unit")));

                Obj.setQuantity(cur.getDouble(cur.getColumnIndex("Quantity")));
                Obj.setRate(cur.getDouble(cur.getColumnIndex("Rate")));
                Obj.setTotalAmount(cur.getDouble(cur.getColumnIndex("TotalAmount")));
                Obj.setDiscount(cur.getDouble(cur.getColumnIndex("Discount")));
                Obj.setNetAmount(cur.getDouble(cur.getColumnIndex("NetAmount")));

                Obj.setApplyTax(cur.getString(cur.getColumnIndex("ApplyTax")));
                Obj.setCGST(cur.getDouble(cur.getColumnIndex("CGST")));
                Obj.setSGST(cur.getDouble(cur.getColumnIndex("SGST")));
                Obj.setIGST(cur.getDouble(cur.getColumnIndex("IGST")));
                Obj.setTotalTaxAmount(cur.getDouble(cur.getColumnIndex("TotalTaxAmount")));

                Log.e("--ItemList--", "GrandTotal: " + cur.getDouble(cur.getColumnIndex("GrandTotal")));
                Log.e("--ItemList--", "TotalTaxAmount: " + cur.getDouble(cur.getColumnIndex("TotalTaxAmount")));

                Obj.setGrandTotal(cur.getDouble(cur.getColumnIndex("GrandTotal")));
//                Obj.set_allTotal(cur.getDouble(cur.getColumnIndex("AllTotal")));


                lstClsPurchaseDetails.add(Obj);
            }
            db.close();

        } catch (Exception e) {
            Log.e("ClsCategory", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }
        return lstClsPurchaseDetails;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsPurchaseDetail> getPurchaseItemListItemWise(String _where, Context context) {
        List<ClsPurchaseDetail> lstClsPurchaseDetails = new ArrayList<>();
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT PD.*, ITM.[ITEM_NAME], V.[VENDOR_NAME], [PM].[PurchaseDate] FROM [PurchaseDetail] AS PD "
                    .concat(" INNER JOIN [PurchaseMaster] AS PM ON PM.[PurchaseID] = PD.[PurchaseID] ")
                    .concat(" INNER JOIN [VENDOR_MASTER] AS V ON V.[VENDOR_ID] = PM.[VendorID] ")
                    .concat(" INNER JOIN [tbl_LayerItem_Master] AS ITM ON ITM.[LAYERITEM_ID] = PD.[ItemID] ")
                    .concat(" WHERE 1=1 ")
                    .concat(_where)
                    .concat(";");


            Log.e("--StockPurchase--", "Qry: " + qry);
            Cursor cur = db.rawQuery(qry, null);
            Log.e("--StockPurchase--", "QryCount: " + cur.getCount());

            while (cur.moveToNext()) {
                ClsPurchaseDetail Obj = new ClsPurchaseDetail();

                Obj.setItemID(cur.getInt(cur.getColumnIndex("ItemID")));
                Obj.setItemCode(cur.getString(cur.getColumnIndex("ItemCode")));
                Obj.setItemName(cur.getString(cur.getColumnIndex("ITEM_NAME")));
                Obj.setUnit(cur.getString(cur.getColumnIndex("Unit")));

                Obj.setQuantity(cur.getDouble(cur.getColumnIndex("Quantity")));
                Obj.setRate(cur.getDouble(cur.getColumnIndex("Rate")));
                Obj.setTotalAmount(cur.getDouble(cur.getColumnIndex("TotalAmount")));
                Obj.setDiscount(cur.getDouble(cur.getColumnIndex("Discount")));
                Obj.setNetAmount(cur.getDouble(cur.getColumnIndex("NetAmount")));

                Obj.setApplyTax(cur.getString(cur.getColumnIndex("ApplyTax")));
                Obj.setVENDOR_NAME(cur.getString(cur.getColumnIndex("VENDOR_NAME")));
                Obj.setPurchaseDate(cur.getString(cur.getColumnIndex("PurchaseDate")));
                Obj.setCGST(cur.getDouble(cur.getColumnIndex("CGST")));
                Obj.setSGST(cur.getDouble(cur.getColumnIndex("SGST")));
                Obj.setIGST(cur.getDouble(cur.getColumnIndex("IGST")));
                Obj.setTotalTaxAmount(cur.getDouble(cur.getColumnIndex("TotalTaxAmount")));

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


    public String getVendorPhone() {
        return vendorPhone;
    }

    public void setVendorPhone(String vendorPhone) {
        this.vendorPhone = vendorPhone;
    }

    String vendorPhone = "";

    @SuppressLint("WrongConstant")
    public static ClsPurchaseDetail getPurchaseSummery(String purchaseid, Context context) {

        ClsPurchaseDetail clsPurchaseMaster = new ClsPurchaseDetail();

        int VendorID = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name,
                    context.MODE_APPEND, null);

            // get GrandTotal.
            String qry = ("SELECT [PurchaseID], sum([GrandTotal]) as GrandTotal," +
                    "sum([Discount]) as Discount, " +
                    "sum([TotalTaxAmount]) as TotalTaxAmount," +
                    "sum(TotalAmount) as TotalAmount  " +
                    "FROM [PurchaseDetail] ")
                    .concat(" WHERE 1=1 ")
                    .concat("AND [PurchaseID] = " + purchaseid)
                    .concat(";");

            Log.e("--StockPurchase--", "Qry: " + qry);
            Cursor cur = db.rawQuery(qry, null);
            Log.e("--StockPurchase--", "QryCount: " + cur.getCount());

            while (cur.moveToNext()) {
                clsPurchaseMaster.setPurchaseID(cur.getInt(cur.getColumnIndex(
                        "PurchaseID")));
                clsPurchaseMaster.setGrandTotal(cur.getDouble(cur.getColumnIndex(
                        "GrandTotal")));
                clsPurchaseMaster.setTotalTaxAmount(cur.getDouble(cur.getColumnIndex(
                        "TotalTaxAmount")));
                clsPurchaseMaster.setDiscount(cur.getDouble(cur.getColumnIndex(
                        "Discount")));
                clsPurchaseMaster.setTotalAmount(cur.getDouble(cur.getColumnIndex(
                        "TotalAmount")));
            }

            // get VendorID.
            qry = ("SELECT [VendorID],[PurchaseNo] " +
                    "FROM [PurchaseMaster] ")
                    .concat(" WHERE 1=1 ")
                    .concat("AND [PurchaseID] = " + purchaseid)
                    .concat(";");

            cur = db.rawQuery(qry, null);

            while (cur.moveToNext()) {
                clsPurchaseMaster.setPurchaseNo(cur.getInt(cur.getColumnIndex(
                        "PurchaseNo")));
                clsPurchaseMaster.setVendorID(cur.getInt(cur.getColumnIndex(
                        "VendorID")));
                Log.e("Check", "VendorID:  " + cur.getInt(cur.getColumnIndex(
                        "VendorID")));
                VendorID = cur.getInt(cur.getColumnIndex(
                        "VendorID"));
            }


            ClsVendor clsVendor = ClsVendor.getPhoneById(VendorID, context);
            clsPurchaseMaster.setVendorPhone(clsVendor.getContact_no());
            clsPurchaseMaster.setVENDOR_NAME(clsVendor.getVendor_name());


            qry = ("SELECT [BillNO],[PurchaseDate],[Remark],[EntryDate] FROM " +
                    "[PurchaseMaster] ")
                    .concat(" WHERE 1=1 ")
                    .concat(" AND [PurchaseID]  = " + purchaseid)
                    .concat(";");

            cur = db.rawQuery(qry, null);

            while (cur.moveToNext()) {
                clsPurchaseMaster.setBillNO(cur.getString(cur.getColumnIndex(
                        "BillNO")));
                clsPurchaseMaster.setPurchaseDate(cur.getString(cur.getColumnIndex(
                        "PurchaseDate")));
                clsPurchaseMaster.setRemark(cur.getString(cur.getColumnIndex(
                        "Remark")));
                clsPurchaseMaster.setEntryDate(cur.getString(cur.getColumnIndex(
                        "EntryDate")));
            }

            db.close();

            Gson gson2 = new Gson();
            String jsonInString2 = gson2.toJson(clsPurchaseMaster);
            Log.e("Check", "clsPurchaseMaster from :--- " + jsonInString2);
        } catch (Exception e) {
            Log.e("ClsCategory", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }

        return clsPurchaseMaster;
    }
}
