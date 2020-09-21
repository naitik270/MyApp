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

public class ClsInventoryOrderDetail implements Serializable {

    int OrderDetailID;

    String ItemCode;
    String Item;
    String Unit;
    String Tax_Apply, OrderNo = "", OrderID = "";

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

    Double SaleRateWithoutTax = 0.0;

    static Context context;
    private static SQLiteDatabase db;


    public String getTax_Apply() {
        return Tax_Apply;
    }

    public void setTax_Apply(String tax_Apply) {
        Tax_Apply = tax_Apply;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public int getOrderDetailID() {
        return OrderDetailID;
    }

    public void setOrderDetailID(int orderDetailID) {
        OrderDetailID = orderDetailID;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
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


    @SuppressLint("WrongConstant")
    public static int InsertInventoryOrderDetail(ClsInventoryOrderDetail currentObj, Context context) {
        int InsertInventoryOrderDetail = 0;

        try {

//            if (!db.isOpen()){
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
//            }
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = ("INSERT INTO [InventoryOrderDetail] ([OrderID]," +
                    "[OrderNo],[ItemCode],[Item],[Rate],[SaleRate]" +
                    ",[Quantity],[Amount],[CGST],[SGST],[IGST],[TotalTaxAmount]," +
                    "[GrandTotal],[SaleRateWithoutTax],[ItemComment],[Discount_per],[Discount_amt],[SaveStatus]) VALUES ('")

                    .concat(String.valueOf(currentObj.getOrderID()))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(String.valueOf(currentObj.getOrderNo()))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(currentObj.getItemCode())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(currentObj.getItem().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(currentObj.getRate()))
                    .concat(",")

                    .concat(String.valueOf(currentObj.getSaleRate()))
                    .concat(",")

                    .concat(String.valueOf(currentObj.getQuantity()))
                    .concat(",")

                    .concat(String.valueOf(currentObj.getAmount()))
                    .concat(",")

                    .concat(String.valueOf(currentObj.getCGST()))
                    .concat(",")

                    .concat(String.valueOf(currentObj.getSGST()))
                    .concat(",")

                    .concat(String.valueOf(currentObj.getIGST()))
                    .concat(",")

                    .concat(String.valueOf(currentObj.getTotalTaxAmount()))
                    .concat(",")

                    .concat(String.valueOf(currentObj.getGrandTotal()))
                    .concat(",")

                    .concat(String.valueOf(currentObj.getSaleRateWithoutTax()))
                    .concat(",")

                    .concat("'")
                    .concat(currentObj.getItemComment().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(currentObj.getDiscount_per()))
                    .concat(",")

                    .concat(String.valueOf(currentObj.getDiscount_amt()))
                    .concat(",")

                    .concat("'")
                    .concat(currentObj.getSaveStatus())
                    .concat("'")
                    .concat(");");

            SQLiteStatement statementInsertInventoryOrderDetail = db.compileStatement(qry);
            InsertInventoryOrderDetail = statementInsertInventoryOrderDetail.executeUpdateDelete();
            Log.e("---userInfo---", "qryOrdDetail: " + qry);
            Log.e("---userInfo---", "Result: " + InsertInventoryOrderDetail);

        } catch (Exception e) {
            Log.e("---userInfo---", "Exception: " + e.getMessage());
            e.getMessage();
        }

        return InsertInventoryOrderDetail;
    }

    @SuppressLint("WrongConstant")
    public static List<String> getInventoryOrderDetailColumns(Context context,io.requery.android.database
            .sqlite.SQLiteDatabase db) {
        List<String> columns = new ArrayList<>();
        try {

//            io.requery.android.database.sqlite.SQLiteDatabase db = io.requery.android.database.sqlite.SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath(ClsGlobal.Database_Name).getPath(),
//                    null);


//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT * FROM [InventoryOrderDetail] LIMIT 1 ";

            Cursor cur = db.rawQuery(qry, null);
            Log.e("--Select--", ">>>>>>>>>" + cur.getCount());

            if (cur != null)
                for (String c : cur.getColumnNames()) {
                    columns.add(c);
                }

//            db.close();

        } catch (Exception e) {
            Log.e("ClsCategory", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }
        return columns;
    }


    @SuppressLint("WrongConstant")
    public static int DeleteFromOrdermaster(String orderNo, String orderID, String mode, Context context) {
        int result = 0;
        try {

//            if (!db.isOpen()){
////
////                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
////            }

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String strSql = "";
            if (mode.equalsIgnoreCase("DeleteById")) {
                strSql = "DELETE FROM [InventoryOrderDetail] WHERE [OrderNo] = '"
                        .concat(String.valueOf(orderNo))
                        .concat("'")
                        .concat(";");
            } else {
                strSql = "DELETE FROM [InventoryOrderDetail] WHERE [OrderNo] = '"
                        .concat(String.valueOf(orderNo))
                        .concat("'")
                        .concat(" AND [OrderID] = " + orderID)
                        .concat(";");
            }

            Log.e("--PAYMENT_DELETE--", "Payment_OrderMaster:" + strSql);
            SQLiteStatement statement = db.compileStatement(strSql);

            result = statement.executeUpdateDelete();
            Log.e("--PAYMENT_DELETE--", "OrderMaster_Result:" + strSql);
            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsInventory", "Delete" + e.getMessage());
            e.getMessage();
        }
        return result;

    }


    @SuppressLint("WrongConstant")
    public static List<ClsInventoryOrderDetail> getList(String where, Context context) {
        List<ClsInventoryOrderDetail> list = new ArrayList<>();

        try {

//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
//            }

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT * FROM [InventoryOrderDetail] where 1=1 ".concat(where);

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--updateStatus--", "Qry: " + qry);
            Log.e("--updateStatus--", "QryCount: " + cur.getCount());

            while (cur.moveToNext()) {
                ClsInventoryOrderDetail getSet = new ClsInventoryOrderDetail();
                getSet.setOrderDetailID(cur.getInt(cur.getColumnIndex("OrderDetailID")));
                getSet.setOrderID(cur.getString(cur.getColumnIndex("OrderID")));
                getSet.setOrderNo(cur.getString(cur.getColumnIndex("OrderNo")));
                getSet.setItemCode(cur.getString(cur.getColumnIndex("ItemCode")));
//                getSet.setUnit(cur.getString(cur.getColumnIndex("Unit")));
                getSet.setRate(cur.getDouble(cur.getColumnIndex("Rate")));
                getSet.setCGST(cur.getDouble(cur.getColumnIndex("CGST")));
                getSet.setSGST(cur.getDouble(cur.getColumnIndex("SGST")));
                getSet.setIGST(cur.getDouble(cur.getColumnIndex("IGST")));
                getSet.setGrandTotal(cur.getDouble(cur.getColumnIndex("GrandTotal")));

                getSet.setDiscount_per(cur.getDouble(cur.getColumnIndex("Discount_per")));
                getSet.setDiscount_amt(cur.getDouble(cur.getColumnIndex("Discount_amt")));

                getSet.setItemComment(cur.getString(cur.getColumnIndex("ItemComment")));
                getSet.setSaveStatus(cur.getString(cur.getColumnIndex("SaveStatus")));

                getSet.setItem(cur.getString(cur.getColumnIndex("Item")));
                getSet.setSaleRate(cur.getDouble(cur.getColumnIndex("SaleRate")));
                getSet.setQuantity(cur.getDouble(cur.getColumnIndex("Quantity")));
                getSet.setAmount(cur.getDouble(cur.getColumnIndex("Amount")));
                getSet.setTotalTaxAmount(cur.getDouble(cur.getColumnIndex("TotalTaxAmount")));
                getSet.setSaleRateWithoutTax(cur.getDouble(cur.getColumnIndex("SaleRateWithoutTax")));

                list.add(getSet);
            }

        } catch (Exception e) {
            e.getMessage();
            Log.e("--updateStatus--", "Exception: " + e.getMessage());

        }
        return list;
    }

    public static class runningOrder {
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


    @SuppressLint("WrongConstant")
    public static runningOrder getRunningOrderDetails(String where, Context context) {
        runningOrder getSet = new runningOrder();

        try {

//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
//            }
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);


            String qry = ("SELECT COUNT(1) AS TOTALITEMS, " +
                    "SUM(IFNULL([SaleRateWithoutTax],0) * IFNULL([Quantity],0)) AS TOTALAMOUNT " +
                    " FROM [InventoryOrderDetail] where 1=1  AND IFNULL([SaveStatus],'') <> 'DEL' ")
                    .concat(where)
                    .concat(" GROUP BY [OrderNo] ");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("getRunningOrderDetails", String.valueOf(cur.getCount()));
            Log.e("getRunningOrderDetails", qry);

            while (cur.moveToNext()) {
                getSet.setTotalAmt(cur.getDouble(cur.getColumnIndex("TOTALAMOUNT")));
                getSet.setItemCount(cur.getInt(cur.getColumnIndex("TOTALITEMS")));
            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return getSet;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsInventoryOrderDetail> getListForInvoicePDF(String where, Context context, SQLiteDatabase db) {
        List<ClsInventoryOrderDetail> list = new ArrayList<>();

        try {

//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = ("SELECT ORD.*, ifnull(ORD.[ItemComment],'-') as [ItemCommentValue] " +
                    ",ITM.[UNIT_CODE] ,ITM.[TAX_APPLY],ITM.[HSN_SAC_CODE] FROM [InventoryOrderDetail] AS ORD ")
                    .concat(" INNER JOIN [tbl_LayerItem_Master] AS ITM ON ITM.[ITEM_CODE] = ORD.[ItemCode] ")
                    .concat(" WHERE 1=1 ")
                    .concat(where);

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--PDF--", "qry: " + qry);
            Log.e("--PDF--", "count: " + cur.getCount());

            Gson gson = new Gson();
            String jsonInString = gson.toJson(cur.getColumnNames());
            Log.e("--PDF--", "gson: " + jsonInString);

            while (cur.moveToNext()) {
                ClsInventoryOrderDetail getSet = new ClsInventoryOrderDetail();

                getSet.setOrderDetailID(cur.getInt(cur.getColumnIndex("OrderDetailID")));
                getSet.setOrderID(cur.getString(cur.getColumnIndex("OrderID")));
                getSet.setOrderNo(cur.getString(cur.getColumnIndex("OrderNo")));
                getSet.setItemCode(cur.getString(cur.getColumnIndex("ItemCode")));

                String valueComment = cur.getString(cur.getColumnIndex("ItemCommentValue"));
                getSet.setItemComment(valueComment);
                Log.e("itemComment", "getSet:value=" + valueComment);


                getSet.setRate(cur.getDouble(cur.getColumnIndex("Rate")));
                getSet.setCGST(cur.getDouble(cur.getColumnIndex("CGST")));
                getSet.setSGST(cur.getDouble(cur.getColumnIndex("SGST")));
                getSet.setIGST(cur.getDouble(cur.getColumnIndex("IGST")));
                getSet.setGrandTotal(cur.getDouble(cur.getColumnIndex("GrandTotal")));

                getSet.setDiscount_per(cur.getDouble(cur.getColumnIndex("Discount_per")));
                getSet.setDiscount_amt(cur.getDouble(cur.getColumnIndex("Discount_amt")));

                getSet.setHSN_SAC_CODE(cur.getString(cur.getColumnIndex("HSN_SAC_CODE")));
                getSet.setItem(cur.getString(cur.getColumnIndex("Item")));
                getSet.setSaleRate(cur.getDouble(cur.getColumnIndex("SaleRate")));
                getSet.setQuantity(cur.getDouble(cur.getColumnIndex("Quantity")));
                getSet.setAmount(cur.getDouble(cur.getColumnIndex("Amount")));
                getSet.setTotalTaxAmount(cur.getDouble(cur.getColumnIndex("TotalTaxAmount")));
                getSet.setUnit(cur.getString(cur.getColumnIndex("UNIT_CODE")));
                getSet.setTax_Apply(cur.getString(cur.getColumnIndex("TAX_APPLY")));
                getSet.setSaleRateWithoutTax(cur.getDouble(cur.getColumnIndex("SaleRateWithoutTax")));

                Gson gson1 = new Gson();
                String jsonInString1 = gson1.toJson(getSet);
                Log.e("itemComment", "getSet" + jsonInString1);

                list.add(getSet);
            }

            Gson gson1 = new Gson();
            String jsonInString1 = gson1.toJson(list);
            Log.e("itemComment", jsonInString1);


        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static int UpdateStatus(int _orderDetailID, String Status, String where, Context context) {
        int result = 0;
        try {

            //insert
            //same flow
            //update
            //item delete > update status = 'DEL'
            //CONTINUE > NOTHING TO DO
            //CANCEL > UPDATE STATUS = '' WHERE ORDERID = ORDERID
         /*   if (!db.isOpen()){

                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            }*/

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);


            String strSql = "UPDATE [InventoryOrderDetail] SET "
                    .concat(" [SaveStatus] = ")
                    .concat("'")
                    .concat(Status)
                    .concat("'")

                    .concat(" WHERE [OrderDetailID] = ")
                    .concat(String.valueOf(_orderDetailID))
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
    public static int Delete(int deleteId, Context context) {
        int result = 0;
        try {

           /* if (!db.isOpen()){

                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            }*/

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);


            String strSql = "DELETE FROM [InventoryOrderDetail] WHERE [OrderDetailID] = "
                    .concat(String.valueOf(deleteId))
                    .concat(";");
            Log.e("--updateStatus--", "DEL_strSql: " + strSql);
            SQLiteStatement statement = db.compileStatement(strSql);

            result = statement.executeUpdateDelete();
            Log.e("--updateStatus--", "DEL_result: " + result);

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsInventoryOrderDetail", "Delete" + e.getMessage());
            e.getMessage();
        }
        return result;

    }


    @SuppressLint("WrongConstant")
    public static int getTotalItemCount(String where, Context context) {
        int count = 0;

        try {
/*

            if (!db.isOpen()){

                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            }
*/
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);


            String qry = ("SELECT count(*) as totalItems FROM [InventoryOrderDetail] " +
                    "where 1=1 ").concat(where);

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--updateStatus--", "Qry: " + qry);
            Log.e("--updateStatus--", "QryCount: " + cur.getCount());

            while (cur.moveToNext()) {
                count = cur.getInt(cur.getColumnIndex("totalItems"));
            }

        } catch (Exception e) {
            e.getMessage();
            Log.e("--updateStatus--", "Exception: " + e.getMessage());
        }

        return count;
    }


    @SuppressLint("WrongConstant")
    public static int getTotalQuantity(String where, Context context) {
        int count = 0;

        try {

            /*if (!db.isOpen()){

                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            }*/

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);


            String qry = ("SELECT sum([Quantity]) as totalQuantity FROM [InventoryOrderDetail] " +
                    "where 1=1 ").concat(where);

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--updateStatus--", "Qry: " + qry);
            Log.e("--updateStatus--", "QryCount: " + cur.getCount());

            while (cur.moveToNext()) {
                count = cur.getInt(cur.getColumnIndex("totalQuantity"));
            }

        } catch (Exception e) {
            e.getMessage();
            Log.e("--updateStatus--", "Exception: " + e.getMessage());
        }

        return count;
    }


    @SuppressLint("WrongConstant")
    public static int DeleteAllRecords(int _orderNO, Context context) {
        int result = 0;
        try {

//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
//            }

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String deleteOrd = "DELETE FROM [InventoryOrderMaster] WHERE [OrderNo] = "
                    .concat("'").concat(String.valueOf(_orderNO)).concat("'");

            SQLiteStatement deleteOrdSt = db.compileStatement(deleteOrd);
            result = deleteOrdSt.executeUpdateDelete();

            String deleteQry = "DELETE FROM [InventoryOrderDetail] WHERE [OrderNo]  = "
                    .concat("'").concat(String.valueOf(_orderNO)).concat("'");

            SQLiteStatement delete = db.compileStatement(deleteQry);
            result = delete.executeUpdateDelete();

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsInventoryOrderDetail", "Delete" + e.getMessage());
            e.getMessage();
        }
        return result;

    }

}
