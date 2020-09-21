package com.demo.nspl.restaurantlite.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClsQuotationOrderDetail implements Serializable {

    String ItemCode;

    String Item;
    String Unit;
    String OrderNo = "";

    public String getSaveStatus() {
        return SaveStatus;
    }

    public void setSaveStatus(String saveStatus) {
        SaveStatus = saveStatus;
    }

    String SaveStatus = "";

    public String getItemComment() {
        return ItemComment;
    }

    public void setItemComment(String itemComment) {
        ItemComment = itemComment;
    }

    String ItemComment = "";
    Double Rate = 0.0;
    Double SaleRate = 0.0;
    Double Quantity = 0.0;
    Double Amount = 0.0;
    Double CGST = 0.0;
    Double SGST = 0.0;
    Double IGST = 0.0;
    Double TotalTaxAmount = 0.0;
    Double GrandTotal = 0.0;

    public Double getSaleRateWithoutTax() {
        return SaleRateWithoutTax;
    }

    public void setSaleRateWithoutTax(Double saleRateWithoutTax) {
        SaleRateWithoutTax = saleRateWithoutTax;
    }

    Double SaleRateWithoutTax = 0.0;

    static Context context;
    private static SQLiteDatabase db;


    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }


    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }

    public Double getRate() {
        return Rate;
    }

    public void setRate(Double rate) {
        Rate = rate;
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


    public String getHSN_SAC_CODE() {
        return HSN_SAC_CODE;
    }

    public void setHSN_SAC_CODE(String HSN_SAC_CODE) {
        this.HSN_SAC_CODE = HSN_SAC_CODE;
    }

    String HSN_SAC_CODE = "";


    Double Discount_per = 0.0;
    Double Discount_amt = 0.0;

    public Double getDiscount_per() {
        return Discount_per;
    }

    public void setDiscount_per(Double discount_per) {
        Discount_per = discount_per;
    }

    public Double getDiscount_amt() {
        return Discount_amt;
    }

    public void setDiscount_amt(Double discount_amt) {
        Discount_amt = discount_amt;
    }

    int QuotationDetailID = 0;
    int QuotationID = 0;
    String QuotationNo = "";

    public int getQuotationDetailID() {
        return QuotationDetailID;
    }

    public void setQuotationDetailID(int quotationDetailID) {
        QuotationDetailID = quotationDetailID;
    }


    public int getQuotationID() {
        return QuotationID;
    }

    public void setQuotationID(int quotationID) {
        QuotationID = quotationID;
    }

    public String getQuotationNo() {
        return QuotationNo;
    }

    public void setQuotationNo(String quotationNo) {
        QuotationNo = quotationNo;
    }

    @SuppressLint("WrongConstant")
    public static int InsertQuotationDetail(ClsQuotationOrderDetail obj, Context context) {
        int InsertQuotationDetail = 0;

        try {
//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
//            }
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String qry = ("INSERT INTO [QuotationDetail] ([QuotationID]," +
                    "[QuotationNo],[SaveStatus],[ItemCode],[Item],[Unit],[ItemComment],[Rate],[SaleRate]" +
                    ",[SaleRateWithoutTax],[Quantity],[Discount_per],[Discount_amt],[Amount],[CGST],[SGST],[IGST],[TotalTaxAmount]," +
                    "[GrandTotal]) VALUES ('")

                    .concat(String.valueOf(obj.getQuotationID()))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(String.valueOf(obj.getQuotationNo()))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(obj.getSaveStatus())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(obj.getItemCode())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(obj.getItem().trim().replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(obj.getUnit())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(obj.getItemComment().trim().replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(obj.getRate()))
                    .concat(",")

                    .concat(String.valueOf(obj.getSaleRate()))
                    .concat(",")

                    .concat(String.valueOf(obj.getSaleRateWithoutTax()))
                    .concat(",")

                    .concat(String.valueOf(obj.getQuantity()))
                    .concat(",")

                    .concat(String.valueOf(obj.getDiscount_per()))
                    .concat(",")

                    .concat(String.valueOf(obj.getDiscount_amt()))
                    .concat(",")

                    .concat(String.valueOf(obj.getAmount()))
                    .concat(",")

                    .concat(String.valueOf(obj.getCGST()))
                    .concat(",")

                    .concat(String.valueOf(obj.getSGST()))
                    .concat(",")

                    .concat(String.valueOf(obj.getIGST()))
                    .concat(",")

                    .concat(String.valueOf(obj.getTotalTaxAmount()))
                    .concat(",")

                    .concat(String.valueOf(obj.getGrandTotal()))
                    .concat(");");

            SQLiteStatement statementInsertInventoryOrderDetail = db.compileStatement(qry);
            InsertQuotationDetail = statementInsertInventoryOrderDetail.executeUpdateDelete();
            Log.e("--InsertQuotation--", "First: " + qry);
            Log.e("--InsertQuotation--", "Count: " + InsertQuotationDetail);

        } catch (Exception e) {
            Log.e("--InsertQuotation--", "Exception: " + e.getMessage());
            e.getMessage();
        }
        return InsertQuotationDetail;
    }


    @SuppressLint("WrongConstant")
    public static int deleteQuotationDetail(String QuotationNo, String QuotationID,
                                            String mode, Context context) {
        int result = 0;
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "";
            if (mode.equalsIgnoreCase("DeleteById")) {
                strSql = "DELETE FROM [QuotationDetail] WHERE [QuotationNo] = '"
                        .concat(String.valueOf(QuotationNo))
                        .concat("'")
                        .concat(";");
            } else {
                strSql = "DELETE FROM [QuotationDetail] WHERE [QuotationNo] = '"
                        .concat(String.valueOf(QuotationNo))
                        .concat("'")
                        .concat(" AND [QuotationID] = " + QuotationID)
                        .concat(";");
            }

            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();

            Log.e("--QuotationDetail--", "Delete: " + strSql);
            Log.e("--QuotationDetail--", "result:" + result);
            db.close();
        } catch (Exception e) {
            Log.e("--QuotationDetail--", "Exception" + e.getMessage());
            e.getMessage();
        }
        return result;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsQuotationOrderDetail> getQuotationDetailList(String where, Context context, SQLiteDatabase db) {
        List<ClsQuotationOrderDetail> list = new ArrayList<>();

        try {

//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT * FROM [QuotationDetail] where 1=1 ".concat(where);

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--QuotationDetailList--", "Qry: " + qry);
            Log.e("--QuotationDetailList--", "CurCount: " + cur.getCount());

            while (cur.moveToNext()) {
                ClsQuotationOrderDetail getSet = new ClsQuotationOrderDetail();
                getSet.setQuotationDetailID(cur.getInt(cur.getColumnIndex("QuotationDetailID")));
                getSet.setQuotationID(cur.getInt(cur.getColumnIndex("QuotationID")));
                getSet.setQuotationNo(cur.getString(cur.getColumnIndex("QuotationNo")));
                getSet.setSaveStatus(cur.getString(cur.getColumnIndex("SaveStatus")));
                getSet.setItemCode(cur.getString(cur.getColumnIndex("ItemCode")));
                getSet.setItem(cur.getString(cur.getColumnIndex("Item")));
                getSet.setUnit(cur.getString(cur.getColumnIndex("Unit")));
                getSet.setItemComment(cur.getString(cur.getColumnIndex("ItemComment")));
                getSet.setRate(cur.getDouble(cur.getColumnIndex("Rate")));
                getSet.setSaleRate(cur.getDouble(cur.getColumnIndex("SaleRate")));
                getSet.setSaleRateWithoutTax(cur.getDouble(cur.getColumnIndex("SaleRateWithoutTax")));
                getSet.setQuantity(cur.getDouble(cur.getColumnIndex("Quantity")));
                getSet.setDiscount_per(cur.getDouble(cur.getColumnIndex("Discount_per")));
                getSet.setDiscount_amt(cur.getDouble(cur.getColumnIndex("Discount_amt")));
                getSet.setAmount(cur.getDouble(cur.getColumnIndex("Amount")));
                getSet.setCGST(cur.getDouble(cur.getColumnIndex("CGST")));
                getSet.setSGST(cur.getDouble(cur.getColumnIndex("SGST")));
                getSet.setIGST(cur.getDouble(cur.getColumnIndex("IGST")));
                getSet.setTotalTaxAmount(cur.getDouble(cur.getColumnIndex("TotalTaxAmount")));
                getSet.setGrandTotal(cur.getDouble(cur.getColumnIndex("GrandTotal")));
                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsQuotationOrderDetail> getMsgDescription(SQLiteDatabase db, int quoID, String quoNo) {
        List<ClsQuotationOrderDetail> list = new ArrayList<>();

        try {

            String qry = "SELECT QuotationMaster.[ValidUptoDate],QuotationDetail.* From QuotationMaster"
                    .concat(" LEFT JOIN QuotationDetail ON QuotationDetail.[QuotationID] = QuotationMaster.[QuotationID]")
                    .concat(" WHERE 1 = 1 ")
                    .concat(" AND QuotationMaster.[QuotationID] = ")
                    .concat(String.valueOf(quoID))
                    .concat(" AND QuotationMaster.[QuotationNo] = ")
                    .concat("'")
                    .concat(quoNo)
                    .concat("'");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--getMsgDescription--", "Qry: " + qry);
            Log.e("--getMsgDescription--", "CurCount: " + cur.getCount());

            while (cur.moveToNext()) {

                ClsQuotationOrderDetail getSet = new ClsQuotationOrderDetail();
                getSet.setQuotationDetailID(cur.getInt(cur.getColumnIndex("QuotationDetailID")));
                getSet.setQuotationID(cur.getInt(cur.getColumnIndex("QuotationID")));
                getSet.setQuotationNo(cur.getString(cur.getColumnIndex("QuotationNo")));
                getSet.setSaveStatus(cur.getString(cur.getColumnIndex("SaveStatus")));
                getSet.setItemCode(cur.getString(cur.getColumnIndex("ItemCode")));
                getSet.setItem(cur.getString(cur.getColumnIndex("Item")));
                getSet.setUnit(cur.getString(cur.getColumnIndex("Unit")));
                getSet.setItemComment(cur.getString(cur.getColumnIndex("ItemComment")));
                getSet.setRate(cur.getDouble(cur.getColumnIndex("Rate")));
                getSet.setSaleRate(cur.getDouble(cur.getColumnIndex("SaleRate")));
                getSet.setSaleRateWithoutTax(cur.getDouble(cur.getColumnIndex("SaleRateWithoutTax")));
                getSet.setQuantity(cur.getDouble(cur.getColumnIndex("Quantity")));
                getSet.setDiscount_per(cur.getDouble(cur.getColumnIndex("Discount_per")));
                getSet.setDiscount_amt(cur.getDouble(cur.getColumnIndex("Discount_amt")));
                getSet.setAmount(cur.getDouble(cur.getColumnIndex("Amount")));
                getSet.setCGST(cur.getDouble(cur.getColumnIndex("CGST")));
                getSet.setSGST(cur.getDouble(cur.getColumnIndex("SGST")));
                getSet.setIGST(cur.getDouble(cur.getColumnIndex("IGST")));
                getSet.setTotalTaxAmount(cur.getDouble(cur.getColumnIndex("TotalTaxAmount")));
                getSet.setGrandTotal(cur.getDouble(cur.getColumnIndex("GrandTotal")));
                list.add(getSet);

            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static int deletePendingQuotationList(Context context) {
        int result = 0;
        try {
//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
//            }
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String cntQry = "SELECT 1  FROM [QuotationDetail]";
            Cursor cur = db.rawQuery(cntQry, null);

            Log.e("--PendingQuotation--", "Before_Delete: " + cntQry);
            Log.e("--PendingQuotation--", "Before_cur: " + cur.getCount());

            String deleteQry = "DELETE FROM [QuotationDetail] WHERE [SaveStatus]  = "
                    .concat("'").concat("NO").concat("'");

            Log.e("--PendingQuotation--", "Delete: " + deleteQry);
            SQLiteStatement statement = db.compileStatement(deleteQry);
            result = statement.executeUpdateDelete();

            Log.e("--PendingQuotation--", "resultDelete: " + result);


            cur = db.rawQuery(cntQry, null);

            Log.e("--PendingQuotation--", "After_Delete: " + cntQry);
            Log.e("--PendingQuotation--", "After_cur: " + cur.getCount());

            db.close();
        } catch (Exception e) {
            Log.e("--PendingQuotation--", "Exception: " + e.getMessage());
            e.getMessage();
        }
        return result;

    }

    @SuppressLint("WrongConstant")
    public static int UpdateQuotationStatus(int _quotationDetailID, String Status, String where, Context context) {
        int result = 0;
        try {

            //insert
            //same flow
            //update
            //item delete > update status = 'DEL'
            //CONTINUE > NOTHING TO DO
            //CANCEL > UPDATE STATUS = '' WHERE ORDERID = ORDERID
            if (!db.isOpen()){

                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            }

            String strSql = "UPDATE [QuotationDetail] SET "
                    .concat(" [SaveStatus] = ")
                    .concat("'")
                    .concat(Status)
                    .concat("'")

                    .concat(" WHERE [QuotationDetailID] = ")
                    .concat(String.valueOf(_quotationDetailID))
                    .concat(where)
                    .concat(";");

            Log.e("--updateStatus--", "Update: " + strSql);
            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();
            Log.e("--updateStatus--", "result: " + result);


        } catch (Exception e) {
            Log.e("--updateStatus--", "Exception: " + e.getMessage());
        }
        return result;
    }


    @SuppressLint("WrongConstant")
    public static ClsQuotationOrderDetail.runningQuotation getRunningQuotationDetail(String where, Context context) {
        ClsQuotationOrderDetail.runningQuotation getSet = new ClsQuotationOrderDetail.runningQuotation();

        try {

//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
//            }

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = ("SELECT COUNT(1) AS TOTALITEMS, " +
                    "SUM(IFNULL([SaleRateWithoutTax],0) * IFNULL([Quantity],0)) AS TOTALAMOUNT " +
                    " FROM [QuotationDetail] where 1=1  AND IFNULL([SaveStatus],'') <> 'DEL' ")
                    .concat(where)
                    .concat(" GROUP BY [QuotationNo] ");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--Edit--", "qry: " + qry);
            Log.e("--Edit--", "Count: " + cur.getCount());

            while (cur.moveToNext()) {
                getSet.setTotalAmt(cur.getDouble(cur.getColumnIndex("TOTALAMOUNT")));
                getSet.setItemCount(cur.getInt(cur.getColumnIndex("TOTALITEMS")));
            }

        } catch (Exception e) {
            Log.e("--Edit--", "Exception: " + e.getMessage());
            e.getMessage();
        }
        return getSet;
    }

    @SuppressLint("WrongConstant")
    public static int deleteQuotationDetailById(int QuotationDetailID, Context context) {
        int result = 0;
        try {
//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
//            }

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String strSql = "DELETE FROM [QuotationDetail] WHERE [QuotationDetailID] = "
                    .concat(String.valueOf(QuotationDetailID))
                    .concat(";");

            Log.e("--deleteQuotationById--", "strSql: " + strSql);

            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();

            db.close();
        } catch (Exception e) {
            Log.e("ClsQuotationOrderDetail", "Delete" + e.getMessage());
            e.getMessage();
        }
        return result;

    }


    @SuppressLint("WrongConstant")
    public static int getQutationItemCount(String _where, Context context) {
        int count = 0;
        try {
//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
//            }
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT count(*) as totalItems FROM QuotationDetail "
                    .concat("WHERE 1=1 ")
                    .concat(_where);

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--qry--", "qry: " + qry);
            Log.e("--qry--", "Count: " + cur.getCount());

            while (cur.moveToNext()) {
                count = cur.getInt(cur.getColumnIndex("totalItems"));

            }

        } catch (Exception e) {
            Log.e("--qry--", "Exception: " + e.getMessage());
            e.getMessage();
        }
        return count;
    }


    @SuppressLint("WrongConstant")
    public static int getTotalQuantity(String _where, Context context) {
        int count = 0;
        try {
//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
//            }

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String qry = "SELECT sum([Quantity]) as totalQuantity FROM QuotationDetail "
                    .concat("WHERE 1=1 ")
                    .concat(_where);

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--qry--", "qry: " + qry);
            Log.e("--qry--", "Count: " + cur.getCount());

            while (cur.moveToNext()) {
                count = cur.getInt(cur.getColumnIndex("totalQuantity"));

            }

        } catch (Exception e) {
            Log.e("--qry--", "Exception: " + e.getMessage());
            e.getMessage();
        }
        return count;
    }


    @SuppressLint("WrongConstant")
    public static int deletePendingSaveQuotation(Context context) {
        int result = 0;
        try {
//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
//            }
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            // Delete Pending Entry save..
            String deleteQry = "DELETE FROM [QuotationDetail] WHERE 1=1 "
                    .concat(" AND IFNULL([SaveStatus],'') IN ('NO') ");

            SQLiteStatement statement = db.compileStatement(deleteQry);
            result = statement.executeUpdateDelete();

            deleteQry = "DELETE FROM [QuotationDetail] WHERE 1=1 "
                    .concat(" AND IFNULL([SaveStatus],'') IN ('EDT') ");

            statement = db.compileStatement(deleteQry);
            result = statement.executeUpdateDelete();

            // Restore Deleted Item on cancel Update.....
            String strSql = "UPDATE [QuotationDetail] SET "
                    .concat(" [SaveStatus] = NULL WHERE 1= 1 ")
                    .concat(" AND IFNULL([SaveStatus],'') = 'DEL' ");

            statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();
            db.close();
            return result;
        } catch (Exception e) {
            e.getMessage();
        }
        return result;

    }


    @SuppressLint("WrongConstant")
    public static int DeleteAllQuotationRecords(int _quotationNo, Context context) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String deleteOrd = "DELETE FROM [QuotationMaster] WHERE [QuotationNo] = "
                    .concat("'").concat(String.valueOf(_quotationNo)).concat("'");

            SQLiteStatement deleteOrdSt = db.compileStatement(deleteOrd);
            result = deleteOrdSt.executeUpdateDelete();

            String deleteQry = "DELETE FROM [QuotationDetail] WHERE [QuotationNo]  = "
                    .concat("'").concat(String.valueOf(_quotationNo)).concat("'");

            SQLiteStatement delete = db.compileStatement(deleteQry);
            result = delete.executeUpdateDelete();
            db.close();

        } catch (Exception e) {
            Log.e("ClsQuotationOrderDetail", "Delete" + e.getMessage());
            e.getMessage();
        }
        return result;

    }

    public static class runningQuotation {
        int itemCount = 0;
        Double totalAmt = 0.0;

        public int getItemCount() {
            return itemCount;
        }

        public void setItemCount(int itemCount) {
            this.itemCount = itemCount;
        }

        public Double getTotalAmt() {
            return totalAmt;
        }

        public void setTotalAmt(Double totalAmt) {
            this.totalAmt = totalAmt;
        }
    }


}
