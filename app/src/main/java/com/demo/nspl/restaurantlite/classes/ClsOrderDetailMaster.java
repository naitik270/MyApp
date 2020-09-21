package com.demo.nspl.restaurantlite.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desktop on 5/18/2018.
 */

public class ClsOrderDetailMaster {

    private static SQLiteDatabase db;
    private static Context context;
    int OrderDetailId;
    int OrderId;
    int Item_ID;

    public int getItemCount() {
        return ItemCount;
    }

    public void setItemCount(int itemCount) {
        ItemCount = itemCount;
    }

    public String getTable_No() {
        return Table_No;
    }

    public void setTable_No(String table_No) {
        Table_No = table_No;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    int ItemCount;
    String OrderNo, Item_Name, Table_No,OtherTax1, OtherTax2, OtherTax3, OtherTax4, OtherTax5, Type, Status, EntryDateTime;
    Double Rate, Qty, Total_Amount, OtherTaxVal1, OtherTaxVal2, OtherTaxVal3, OtherTaxVal4, OtherTaxVal5, TotalTaxAmount, Grand_Total,discount = 0.0;

    public ClsOrderDetailMaster(Context ctx) {
        context = ctx;
    }


    public ClsOrderDetailMaster() {

    }

    public int getOrderDetailId() {
        return OrderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        OrderDetailId = orderDetailId;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public int getItem_ID() {
        return Item_ID;
    }

    public void setItem_ID(int item_ID) {
        Item_ID = item_ID;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getItem_Name() {
        return Item_Name;
    }

    public void setItem_Name(String item_Name) {
        Item_Name = item_Name;
    }

    public String getOtherTax1() {
        return OtherTax1;
    }

    public void setOtherTax1(String otherTax1) {
        OtherTax1 = otherTax1;
    }

    public String getOtherTax2() {
        return OtherTax2;
    }

    public void setOtherTax2(String otherTax2) {
        OtherTax2 = otherTax2;
    }

    public String getOtherTax3() {
        return OtherTax3;
    }

    public void setOtherTax3(String otherTax3) {
        OtherTax3 = otherTax3;
    }

    public String getOtherTax4() {
        return OtherTax4;
    }

    public void setOtherTax4(String otherTax4) {
        OtherTax4 = otherTax4;
    }

    public String getOtherTax5() {
        return OtherTax5;
    }

    public void setOtherTax5(String otherTax5) {
        OtherTax5 = otherTax5;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getEntryDateTime() {
        return EntryDateTime;
    }

    public void setEntryDateTime(String entryDateTime) {
        EntryDateTime = entryDateTime;
    }

    public Double getRate() {
        return Rate;
    }

    public void setRate(Double rate) {
        Rate = rate;
    }

    public Double getQty() {
        return Qty;
    }

    public void setQty(Double qty) {
        Qty = qty;
    }

    public Double getTotal_Amount() {
        return Total_Amount;
    }

    public void setTotal_Amount(Double total_Amount) {
        Total_Amount = total_Amount;
    }

    public Double getOtherTaxVal1() {
        return OtherTaxVal1;
    }

    public void setOtherTaxVal1(Double otherTaxVal1) {
        OtherTaxVal1 = otherTaxVal1;
    }

    public Double getOtherTaxVal2() {
        return OtherTaxVal2;
    }

    public void setOtherTaxVal2(Double otherTaxVal2) {
        OtherTaxVal2 = otherTaxVal2;
    }

    public Double getOtherTaxVal3() {
        return OtherTaxVal3;
    }

    public void setOtherTaxVal3(Double otherTaxVal3) {
        OtherTaxVal3 = otherTaxVal3;
    }

    public Double getOtherTaxVal4() {
        return OtherTaxVal4;
    }

    public void setOtherTaxVal4(Double otherTaxVal4) {
        OtherTaxVal4 = otherTaxVal4;
    }

    public Double getOtherTaxVal5() {
        return OtherTaxVal5;
    }

    public void setOtherTaxVal5(Double otherTaxVal5) {
        OtherTaxVal5 = otherTaxVal5;
    }

    public Double getTotalTaxAmount() {
        return TotalTaxAmount;
    }

    public void setTotalTaxAmount(Double totalTaxAmount) {
        TotalTaxAmount = totalTaxAmount;
    }

    public Double getGrand_Total() {
        return Grand_Total;
    }

    public void setGrand_Total(Double grand_Total) {
        Grand_Total = grand_Total;
    }


    public static Integer Insert(ClsOrderDetailMaster ObjOrderDetailMaster) {
        int result = 0;

//        if (!db.isOpen()){
//
//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//        }
        db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_PRIVATE, null);
        try {
            String qry = "INSERT INTO [OrderDetail_master] ([ORDER_ID],[ORDER_NO]," +
                    "[ITEM_ID],[ITEM_NAME],[RATE]," +
                    "[QUANTITY],[TOTAL_AMOUNT],[OTHER_TAX1],[OTHER_VAL1],[OTHER_TAX2]," +
                    "[OTHER_VAL2],[OTHER_TAX3]," +
                    "[OTHER_VAL3],[OTHER_TAX4],[OTHER_VAL4],[OTHER_TAX5],[OTHER_VAL5],[TOTAL_TAXAMOUNT]," +
                    "[GRAND_TOTAL],[TYPE],[STATUS],[ENTRYDATETIME]) VALUES ("
                            .concat(String.valueOf(ObjOrderDetailMaster.getOrderId()))
                            .concat(",")

                            .concat("'")
                            .concat(ObjOrderDetailMaster.getOrderNo())
                            .concat("'")
                            .concat(",")

                            .concat(String.valueOf(ObjOrderDetailMaster.getItem_ID()))
                            .concat(",")

                            .concat("'")
                            .concat(ObjOrderDetailMaster.getItem_Name().trim()
                                    .replace("'","''"))
                            .concat("'")
                            .concat(",")

                            .concat(String.valueOf(ObjOrderDetailMaster.getRate()))
                            .concat(",")

                            .concat(String.valueOf(ObjOrderDetailMaster.getQty()))
                            .concat(",")

                            .concat(String.valueOf(ObjOrderDetailMaster.getTotal_Amount()))
                            .concat(",")

                            .concat("'")
                            .concat(ObjOrderDetailMaster.getOtherTax1())
                            .concat("'")
                            .concat(",")

                            .concat(String.valueOf(ObjOrderDetailMaster.getOtherTaxVal1()))
                            .concat(",")

                            .concat("'")
                            .concat(ObjOrderDetailMaster.getOtherTax2())
                            .concat("'")
                            .concat(",")

                            .concat(String.valueOf(ObjOrderDetailMaster.getOtherTaxVal2()))
                            .concat(",")

                            .concat("'")
                            .concat(ObjOrderDetailMaster.getOtherTax3())
                            .concat("'")
                            .concat(",")

                            .concat(String.valueOf(ObjOrderDetailMaster.getOtherTaxVal3()))
                            .concat(",")

                            .concat("'")
                            .concat(ObjOrderDetailMaster.getOtherTax4())
                            .concat("'")
                            .concat(",")

                            .concat(String.valueOf(ObjOrderDetailMaster.getOtherTaxVal4()))
                            .concat(",")

                            .concat("'")
                            .concat(ObjOrderDetailMaster.getOtherTax5())
                            .concat("'")
                            .concat(",")

                            .concat(String.valueOf(ObjOrderDetailMaster.getOtherTaxVal5()))
                            .concat(",")

                            .concat(String.valueOf(ObjOrderDetailMaster.getTotalTaxAmount()))
                            .concat(",")

                            .concat(String.valueOf(ObjOrderDetailMaster.getGrand_Total()))
                            .concat(",")

                            .concat("'")
                            .concat(ObjOrderDetailMaster.getType())
                            .concat("'")
                            .concat(",")

                            .concat("'")
                            .concat(ObjOrderDetailMaster.getStatus())
                            .concat("'")
                            .concat(",")

                            .concat("'")
                            .concat(ObjOrderDetailMaster.getEntryDateTime())
                            .concat("'")
                            .concat(");");

            Log.e("Qry", "Qry--->>" + qry);

            SQLiteStatement sqLiteStatement = db.compileStatement(qry);
            result = sqLiteStatement.executeUpdateDelete();
            Log.e("ClsOrderDetails", "Qry--->>result = " + result);


        } catch (Exception e) {
            Log.e("ClsOrderDetails", "Catch--->>>" + e.getMessage());
        }

        return result;
    }

    @SuppressLint("WrongConstant")
    public static String getSalesMIS(String where) {

        StringBuilder stringBuilder = new StringBuilder();

        if (!db.isOpen()){

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
        }

//        db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
        try {

            List<ClsOrderDetailMaster> list = new ArrayList<>();
//            String qry = "SELECT "
//                    .concat("om.[ORDER_DATETIME]")
//                    .concat(",om.[TABLE_NO]")
//                    .concat(",om.[ORDER_NO]")
//                    .concat(",COUNT(om.[ORDER_NO]) AS [ItemCount]")
//                    .concat(",ifnull(om.[TOTAL_TAXAMOUNT],0) AS [TOTAL_TAXAMOUNT]")
//                    .concat(",ifnull(om.[TOTAL_AMOUNT],0) AS [TOTAL_AMOUNT]")
//                    .concat(",ifnull(odm.[GRAND_TOTAL],0) AS [GRAND_TOTAL]")
//                    .concat(" FROM [OrderDetail_master] as odm")
//                    .concat(" inner JOIN [Ordermaster] as om ON odm.ORDER_ID = om.ORDER_ID")
//                    .concat(" WHERE 1=1")
//                    .concat(where)
////                    .concat( " GROUP BY ")
////                    .concat("om.[ORDER_DATETIME]")
////                    .concat(",om.[TABLE_NO]")
////                    .concat(",om.[ORDER_NO]")
//                    .concat(" ORDER BY om.[ORDER_DATETIME] ");

            String qry = "SELECT "
                    .concat("om.[ORDER_DATETIME]")
                    .concat(",om.[TABLE_NO]")
                    .concat(",om.[ORDER_NO]")
                    .concat(",COUNT(odm.[ORDER_NO]) AS [ItemCount]")
                    .concat(",om.[TOTAL_TAXAMOUNT] AS [TOTAL_TAXAMOUNT]")
                    .concat(",om.[TOTAL_AMOUNT] AS [TOTAL_AMOUNT]")
                    .concat(",om.[GRAND_TOTAL] AS [GRANDTOTAL]")
                    .concat(",om.[DISCOUNT] AS [DISCOUNT]")
                    .concat(" FROM [OrderDetail_master] as odm")
                    .concat(" inner JOIN [Ordermaster] as om ON odm.ORDER_ID = om.ORDER_ID")
                    .concat(" WHERE 1=1")
                    .concat(where)
                    .concat( " GROUP BY ")
//                    .concat("om.[ORDER_DATETIME]")
//                    .concat(",om.[TABLE_NO]")
                    .concat("om.[ORDER_NO]")
                    .concat(" ORDER BY om.[ORDER_DATETIME] , om.[TABLE_NO]");

            Log.e("qry", qry);

            Cursor cur = db.rawQuery(qry, null);
            Log.d("cur123", "getSalesMIS:-- " + cur.getCount());
            Log.d("cur1212", "getColumnCount:-- " + cur.getColumnCount());

            int sr = 0;
            while (cur.moveToNext()) {
                ClsOrderDetailMaster currentObj = new ClsOrderDetailMaster();
                currentObj.setEntryDateTime(cur.getString(cur.getColumnIndex("ORDER_DATETIME")));
                currentObj.setTable_No(cur.getString(cur.getColumnIndex("TABLE_NO")));
                currentObj.setOrderNo(cur.getString(cur.getColumnIndex("ORDER_NO"))); //Bill NO.
                currentObj.setItemCount(cur.getInt(cur.getColumnIndex("ItemCount"))); // Total items.
                currentObj.setTotal_Amount(cur.getDouble(cur.getColumnIndex("TOTAL_AMOUNT"))); // Item Amount.
                currentObj.setDiscount(cur.getDouble(cur.getColumnIndex("DISCOUNT")));// Discount.
                currentObj.setTotalTaxAmount(cur.getDouble(cur.getColumnIndex("TOTAL_TAXAMOUNT"))); // Total Tax Amount.
                currentObj.setGrand_Total(cur.getDouble(cur.getColumnIndex("GRANDTOTAL"))); // Grant Total.

//                Gson gson = new Gson();
//                String jsonInString = gson.toJson(currentObj);
//                Log.e("Result", "All Exp:- " + jsonInString);

                list.add(currentObj);


            }

//            list.clear();

            if (list !=null && list.size() != 0){
                Gson gson = new Gson();
                String jsonInString = gson.toJson(list);
                Log.e("Result", "final Exp:- " + jsonInString);

                stringBuilder.append("<table class=\"table\">");
                stringBuilder.append("<thead>");

                stringBuilder.append("<tr>");

                stringBuilder.append("<th style=\"white-space: normal !important;\">Sr#</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Date</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Table No</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Bill No</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Total Items</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Item Amount</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Discount</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Bill Amount</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Tax Amount</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Grand Total</th>");

                stringBuilder.append("</tr>");
                stringBuilder.append("</thead>");

                stringBuilder.append("<tbody>");//new ROW st

                for (ClsOrderDetailMaster current : list){

                    sr++;
                    stringBuilder.append("<tr>");//new ROW start

                    //1st cell
                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append(sr + ".");//cell value
                    stringBuilder.append("</td>");//cell end

                    //2st cell
                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append(ClsGlobal.getDDMYYYYFormat(current.getEntryDateTime()));//cell value
                    stringBuilder.append("</td>");//cell end

                    //3st cell
                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append(current.getTable_No());//cell value
                    stringBuilder.append("</td>");//cell end

                    //4st cell
                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append(current.getOrderNo()); // Bill NO.
                    stringBuilder.append("</td>");//cell end

                    //5st cell
                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append(current.getItemCount());// Total items.
                    stringBuilder.append("</td>");//cell end

                    //6st cell
                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append(current.getTotal_Amount());// Item Amount.
                    stringBuilder.append("</td>");//cell end

                    //7st cell
                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append(current.getDiscount());// Discount.
                    stringBuilder.append("</td>");//cell end

                    //8st cell
                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append(current.getTotal_Amount() - current.getDiscount());// Bill Amount.
                    stringBuilder.append("</td>");//cell end

                    //9st cell
                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append(current.getTotalTaxAmount());// Total Tax Amount.
                    stringBuilder.append("</td>");//cell end

                    //10st cell
                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append(current.getGrand_Total());// Grant Total.
                    stringBuilder.append("</td>");//cell end


                    stringBuilder.append("</tr>");

                }

                Log.e("stringBuilder",stringBuilder.toString());
                stringBuilder.append("</table>");
            }else {
                stringBuilder.append("<table class=\"table\">\n" +
                        "<td align=\"left\"><span style=\"color:red;\">No records found!</span></td>\n" +
                        "</table>");
            }




        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public static String getOrderMIS(String _where) {
        StringBuilder stringBuilder = new StringBuilder();
//        if (!db.isOpen()){
//
//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//        }
        db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_PRIVATE, null);
//
        List<ClsOrderDetailMaster> list = new ArrayList<>();

        try {
            String qry = "SELECT "
                    .concat("[ITEM_NAME]")
                    .concat(",COUNT(1) as [ItemCount]")
                    .concat(",SUM([GRAND_TOTAL]) as [GTOTAL]")

                    .concat(" FROM  [OrderDetail_master] WHERE 1=1 ")
                    .concat(_where)
                    //.concat("") apply today date here with entry date column format : yyyy-mm-dd
                    .concat(" GROUP BY [ITEM_NAME]")
                    .concat(" ORDER BY [ITEM_NAME]");

            //   String qry = "SELECT * FROM OrderDetail_master";
            Log.e("qry", qry);
            Cursor cursor = db.rawQuery(qry, null);

            // Cursor cursor = db.rawQuery(qry, null);
            Log.e("ClsOrderDetailsMaster", "CursorCount--" + cursor.getCount());
            int sr = 0;
            while (cursor.moveToNext()) {
                ClsOrderDetailMaster objOrder = new ClsOrderDetailMaster();
                objOrder.setItem_Name(cursor.getString(cursor.getColumnIndex("ITEM_NAME")));
                objOrder.setItemCount(cursor.getInt(cursor.getColumnIndex("ItemCount")));
                objOrder.setGrand_Total(cursor.getDouble(cursor.getColumnIndex("GTOTAL")));
                double get = cursor.getDouble(cursor.getColumnIndex("GTOTAL"));
                Log.e("get:-- ", String.valueOf(get));
                list.add(objOrder);
            }

            //list count
            //gSon of list
//            list.clear();
            if (list != null && list.size() !=0){
                stringBuilder.append("<table class=\"table\">");
                stringBuilder.append("<thead>");

                stringBuilder.append("<tr>");

                stringBuilder.append("<th style=\"white-space: normal !important;\">Sr#</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Item Name</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Total Count</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Total Amount</th>");

                stringBuilder.append("</tr>");

                stringBuilder.append("</thead>");

                int totalCount = 0;
                Double grandTotal = 0.0;
                stringBuilder.append("<tbody>");//new ROW st
                for (ClsOrderDetailMaster current : list) {

                    sr++;

                    stringBuilder.append("<tr>");//new ROW start
                    //1st cell
                    stringBuilder.append("<td align=\"right\">");//cell start
                    stringBuilder.append(sr);//cell value
                    stringBuilder.append("</td>");//cell end


                    //2nd cell
                    stringBuilder.append("<td>");//cell start
                    stringBuilder.append(current.getItem_Name());//cell value
                    stringBuilder.append("</td>");//cell end

                    //3rd cell
                    stringBuilder.append("<td align=\"right\">");//cell start
                    stringBuilder.append(ClsGlobal.round(current.getItemCount(), 2));//cell value
                    stringBuilder.append("</td>");//cell end

                    //  cell 4
                    stringBuilder.append("<td align=\"right\">");//cell start
                    stringBuilder.append(ClsGlobal.round(current.getGrand_Total(), 2));//cell value
                    Log.e("price", String.valueOf(current.getGrand_Total()));
                    stringBuilder.append("</td>");//cell end

                    stringBuilder.append("</tr>");//row end

                    totalCount += current.getItemCount();
                    grandTotal += current.getGrand_Total();
                }

                stringBuilder.append("</tbody>");

                stringBuilder.append("<tfoot>");//start footer row
                //  cell 6
                stringBuilder.append("<tr>");//start footer row
                stringBuilder.append("<td>");//empty cell for sr no
                stringBuilder.append("</td>");//empty cell for sr no

                stringBuilder.append("<td>Total</td>");//empty cell for sr no
                stringBuilder.append("<td align=\"right\">");//cell start
                stringBuilder.append(ClsGlobal.round(totalCount, 2));//cell value
                stringBuilder.append("</td>");//cell end

                //  cell 6
                stringBuilder.append("<td align=\"right\">");//cell start
                stringBuilder.append(ClsGlobal.round(grandTotal, 2));//cell value
                stringBuilder.append("</td>");//cell end

                stringBuilder.append("</tr>");//footer row end

                stringBuilder.append("</tfoot>");//start footer
                stringBuilder.append("</table>");
                Log.e("ClsOrderDetails", "Qry--->>result = " + stringBuilder.toString());

            }else {
                stringBuilder.append("<table class=\"table\">\n" +
                        "<td align=\"left\"><span style=\"color:red;\">No records found!</span></td>\n" +
                        "</table>");
            }

        } catch (Exception e) {
            Log.e("ClsOrderDetails", "Catch--->>>" + e.getMessage());
        }
        return stringBuilder.toString();
    }


    public static String getMenuMIS(String _where) {
        Log.e("InsertOrder", "InsertOrder call");
        StringBuilder stringBuilder = new StringBuilder();
        List<ClsOrderDetailMaster> list = new ArrayList<>();
//        if (!db.isOpen()){
//
//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//        }
        db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_PRIVATE, null);

        try {
            String qry = "Select "
                    .concat("[ITEM_NAME]")
                    .concat(",SUM(ifnull([QUANTITY],0)) AS [QUANTITY]")
                    .concat(",SUM(ifnull([TOTAL_AMOUNT],0)) AS [TOTAL_AMOUNT]")
                    .concat(",[RATE]")
                    .concat(",SUM(ifnull([TOTAL_TAXAMOUNT],0)) AS [TOTAL_TAXAMOUNT]")
                    .concat(",SUM(ifnull([GRAND_TOTAL],0)) AS [GRAND_TOTAL]")
                    .concat(" From OrderDetail_master Where 1=1 ")
                    .concat(_where)
                    .concat(" group by [ITEM_NAME] ")
                    .concat(" order by [ITEM_NAME] ");

            Log.e("qry", qry);
            Cursor cursor = db.rawQuery(qry, null);

            Log.e("ClsOrderDetailsMaster", "CursorCount--33 " + cursor.getCount());

            int sr = 0;
            while (cursor.moveToNext()) {
                ClsOrderDetailMaster objOrder = new ClsOrderDetailMaster();
                objOrder.setItem_Name(cursor.getString(cursor.getColumnIndex("ITEM_NAME")));
                objOrder.setQty(cursor.getDouble(cursor.getColumnIndex("QUANTITY")));
                objOrder.setRate(cursor.getDouble(cursor.getColumnIndex("RATE")));
                objOrder.setTotal_Amount(cursor.getDouble(cursor.getColumnIndex("TOTAL_AMOUNT")));
                objOrder.setTotalTaxAmount(cursor.getDouble(cursor.getColumnIndex("TOTAL_TAXAMOUNT")));
                objOrder.setGrand_Total(cursor.getDouble(cursor.getColumnIndex("GRAND_TOTAL")));
                list.add(objOrder);
//                Log.e("ITEM_NAME", String.valueOf(cursor.getString(cursor.getColumnIndex("ITEM_NAME"))));
//                Log.e("QUANTITY", String.valueOf(cursor.getString(cursor.getColumnIndex("QUANTITY"))));
//                Log.e("RATE", String.valueOf(cursor.getString(cursor.getColumnIndex("TOTAL_AMOUNT"))));
//                Log.e("TOTAL_TAXAMOUNT", String.valueOf(cursor.getString(cursor.getColumnIndex("TOTAL_TAXAMOUNT"))));
//                Log.e("GRAND_TOTAL", String.valueOf(cursor.getString(cursor.getColumnIndex("GRAND_TOTAL"))));
            }

//            list.clear();
            if (list != null && list.size() != 0){

                stringBuilder.append("<table class=\"table\">");
                stringBuilder.append("<thead>");

                stringBuilder.append("<tr>");

                stringBuilder.append("<th style=\"white-space: normal !important;\">Sr#</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Item</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Quantity</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Rate</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Amount</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Tax Amount</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Total Amount</th>");
                stringBuilder.append("</tr>");
                stringBuilder.append("</thead>");
                stringBuilder.append("<tbody>");//new ROW st


                Double TotalQuantity = 0.0;
                Double TotalRate = 0.0; // This var is in use do not Remove.
                Double TotalAmountCount = 0.0;
                Double TotalTaxAmount = 0.0;
                Double TotalAmount = 0.0;

                for (ClsOrderDetailMaster current : list) {
                    sr++;

                    stringBuilder.append("<tr>");//new ROW start

                    //1st cell
                    stringBuilder.append("<td align=\"right\">");//cell start
                    stringBuilder.append(sr);//cell value
                    stringBuilder.append("</td>");//cell end

                    //2st cell
                    stringBuilder.append("<td>");//cell start
                    stringBuilder.append(current.getItem_Name());//cell value
                    stringBuilder.append("</td>");//cell end

                    //3st cell
                    stringBuilder.append("<td align=\"right\">");//cell start
                    stringBuilder.append(current.getQty());//cell value
                    stringBuilder.append("</td>");//cell end

                    //4st cell
                    stringBuilder.append("<td align=\"right\">");//cell start
                    stringBuilder.append(ClsGlobal.round(current.getRate(), 2));//cell value
                    stringBuilder.append("</td>");//cell end

                    //5st cell
                    stringBuilder.append("<td align=\"right\">");//cell start
                    stringBuilder.append(current.getTotal_Amount());//cell value
                    stringBuilder.append("</td>");//cell end

                    //6st cell
                    stringBuilder.append("<td align=\"right\">");//cell start
                    stringBuilder.append(ClsGlobal.round(current.getTotalTaxAmount(), 2));//cell value
                    stringBuilder.append("</td>");//cell end

                    //7st cell
                    stringBuilder.append("<td align=\"right\">");//cell start
                    stringBuilder.append(ClsGlobal.round(current.getTotal_Amount() + current.getTotalTaxAmount(), 2));//cell value
                    stringBuilder.append("</td>");//cell end

                    stringBuilder.append("</tr>");//row end

                    // ----------- Total Count ------------ //
                    TotalQuantity += current.getQty();
                    TotalRate += current.getRate();
                    TotalAmountCount += current.getTotal_Amount();
                    TotalTaxAmount += current.getTotalTaxAmount();
                    TotalAmount += current.getTotal_Amount();
                }

                stringBuilder.append("</tbody>");

                stringBuilder.append("<tfoot>");//start footer row
                //  cell 6
                stringBuilder.append("<tr>");//start footer row

                stringBuilder.append("<td>");//empty cell for sr no
                stringBuilder.append("</td>");//empty cell for sr no

                stringBuilder.append("<tb>Total</td>");//empty cell for sr no
                stringBuilder.append("<td align=\"right\">");//cell start
                stringBuilder.append(ClsGlobal.round(TotalQuantity, 2));//cell value
                stringBuilder.append("</td>");//cell end

                //  cell 6
                stringBuilder.append("<td align=\"right\">");//cell start
                stringBuilder.append("");//cell value
                stringBuilder.append("</td>");//cell end

                stringBuilder.append("<td align=\"right\">");//cell start
                stringBuilder.append(ClsGlobal.round(TotalAmountCount, 2));//cell value
                stringBuilder.append("</td>");//cell end

                stringBuilder.append("<td align=\"right\">");//cell start
                stringBuilder.append(ClsGlobal.round(TotalTaxAmount, 2));//cell value
                stringBuilder.append("</td>");//cell end

                stringBuilder.append("<td align=\"right\">");//cell start
                stringBuilder.append(ClsGlobal.round(TotalAmount, 2));//cell value
                stringBuilder.append("</td>");//cell end

                stringBuilder.append("</tr>");//footer row end

                stringBuilder.append("</tfoot>");//start footer
                stringBuilder.append("</table>");

            }else {
                stringBuilder.append("<table class=\"table\">\n" +
                        "<td align=\"left\"><span style=\"color:red;\">No records found!</span></td>\n" +
                        "</table>");
            }


        } catch (Exception ignored) {

        }

        Log.e("stringBuilder", stringBuilder.toString());
        return stringBuilder.toString();
    }




    public static Integer InsertOrder(List<ClsOrderDetailMaster> listOrderItems, String OrderNo, ClsOrder ObjOrderMaster, double totalTaxAmount) {
        Log.e("InsertOrder", "InsertOrder call");
        int result = 0;
//        if (!db.isOpen()){
//
//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//        }
        db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_PRIVATE, null);

        try {

            //insert into OrderMaster

            Double TotalTaxAmt = 0.0, TotalAmt = 0.0;

            for (ClsOrderDetailMaster ObjOrderDetailMaster : listOrderItems) {
                TotalTaxAmt += ObjOrderDetailMaster.getTotalTaxAmount();
                TotalAmt += ObjOrderDetailMaster.getTotal_Amount();
            }

            ObjOrderMaster.setTotal_Amount(TotalAmt);
            ObjOrderMaster.setTotal_TaxAmount(TotalTaxAmt);

            String qry = "INSERT INTO [Ordermaster] ([ORDER_NO]," +
                    "[ORDER_DATETIME],[TABLE_ID],[TABLE_NO],[SOURCE]" +
                    (",[MOBILE_NO],[ENTRYDATETIME],[TOTAL_AMOUNT],[TOTAL_TAXAMOUNT]," +
                            "[DISCOUNT],[GRAND_TOTAL]) VALUES('")

                            .concat(ObjOrderMaster.getOrder_no())
                            .concat("'")
                            .concat(",")

                            .concat("'")
                            .concat(ObjOrderMaster.getOrder_Datetime())
                            .concat("'")
                            .concat(",")

                            .concat(String.valueOf(ObjOrderMaster.getTable_id()))
                            .concat(",")

                            .concat("'")
                            .concat(ObjOrderMaster.getTable_No())
                            .concat("'")
                            .concat(",")

                            .concat("'")
                            .concat(ObjOrderMaster.getSource())
                            .concat("'")
                            .concat(",")

                            .concat("'")
                            .concat(ObjOrderMaster.getMobile_No())
                            .concat("'")
                            .concat(",")

                            .concat("'")
                            .concat(ClsGlobal.getCurruntDateTime())
                            .concat("'")
                            .concat(",")

                            .concat(String.valueOf(ClsGlobal.round(ObjOrderMaster.getTotal_Amount(),
                                    2)))
                            .concat(",")
                            .concat(String.valueOf(ClsGlobal.round(totalTaxAmount, 2)))

                            .concat(",")

                            .concat(String.valueOf(ClsGlobal.round(ObjOrderMaster.getDiscount(),
                                    2)))
                            .concat(",")

                            .concat(String.valueOf(ClsGlobal.round(ObjOrderMaster.getGRAND_TOTAL(),
                                    2)))

                            .concat(");");


            Log.e("ClsOrder", qry);
            Log.e("getTotal_TaxAmount", String.valueOf(totalTaxAmount));
            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();

            // Log.i("Ordermaster_insert ",String.valueOf(effectedRows));

            Cursor c = db.rawQuery("SELECT last_insert_rowid()", null);
            c.moveToFirst();
            int insertedID = c.getInt(0);//orderiD
            Log.e("id:-- ", String.valueOf(insertedID));


            String strSql = "DELETE FROM [OrderDetail_master] WHERE [ORDER_NO] = ".concat("'").concat(OrderNo).concat("'");
            db.execSQL(strSql);

            for (ClsOrderDetailMaster ObjOrderDetailMaster : listOrderItems) {
                int i = 0;
                i += 1;

                Log.e("ObjOrderDetailMaster", "ObjOrderDetailMaster call");
                Log.e("count", String.valueOf(i));

                qry = "INSERT INTO [OrderDetail_master] ([ORDER_ID],[ORDER_NO]," +
                        "[ITEM_ID],[ITEM_NAME],[RATE]," +
                        "[QUANTITY],[TOTAL_AMOUNT],[OTHER_TAX1]," +
                        "[OTHER_VAL1],[OTHER_TAX2],[OTHER_VAL2],[OTHER_TAX3]," +
                        "[OTHER_VAL3],[OTHER_TAX4],[OTHER_VAL4],[OTHER_TAX5]," +
                        "[OTHER_VAL5],[TOTAL_TAXAMOUNT]," +
                        "[GRAND_TOTAL],[TYPE],[STATUS],[ENTRYDATETIME]) VALUES ("
                                .concat(String.valueOf(insertedID))
                                .concat(",")

                                .concat("'")
                                .concat(OrderNo)
                                .concat("'")
                                .concat(",")

                                .concat(String.valueOf(ObjOrderDetailMaster.getItem_ID()))
                                .concat(",")

                                .concat("'")
                                .concat(ObjOrderDetailMaster.getItem_Name().trim()
                                        .replace("'","''"))
                                .concat("'")
                                .concat(",")

                                .concat(String.valueOf(ObjOrderDetailMaster.getRate()))
                                .concat(",")

                                .concat(String.valueOf(ObjOrderDetailMaster.getQty()))
                                .concat(",")

                                .concat(String.valueOf(ObjOrderDetailMaster.getTotal_Amount()))
                                .concat(",")

                                .concat("'")
                                .concat(ObjOrderDetailMaster.getOtherTax1())
                                .concat("'")
                                .concat(",")

                                .concat(String.valueOf(ObjOrderDetailMaster.getOtherTaxVal1()))
                                .concat(",")

                                .concat("'")
                                .concat(ObjOrderDetailMaster.getOtherTax2())
                                .concat("'")
                                .concat(",")

                                .concat(String.valueOf(ObjOrderDetailMaster.getOtherTaxVal2()))
                                .concat(",")

                                .concat("'")
                                .concat(ObjOrderDetailMaster.getOtherTax3())
                                .concat("'")
                                .concat(",")

                                .concat(String.valueOf(ObjOrderDetailMaster.getOtherTaxVal3()))
                                .concat(",")

                                .concat("'")
                                .concat(ObjOrderDetailMaster.getOtherTax4())
                                .concat("'")
                                .concat(",")

                                .concat(String.valueOf(ObjOrderDetailMaster.getOtherTaxVal4()))
                                .concat(",")

                                .concat("'")
                                .concat(ObjOrderDetailMaster.getOtherTax5())
                                .concat("'")
                                .concat(",")

                                .concat(String.valueOf(ObjOrderDetailMaster.getOtherTaxVal5()))
                                .concat(",")

                                .concat(String.valueOf(ObjOrderDetailMaster.getTotalTaxAmount()))
                                .concat(",")

                                .concat(String.valueOf(ObjOrderDetailMaster.getGrand_Total()))
                                .concat(",")

                                .concat("'")
                                .concat(ObjOrderDetailMaster.getType().trim()
                                        .replace("'","''"))
                                .concat("'")
                                .concat(",")

                                .concat("'")
                                .concat(ObjOrderDetailMaster.getStatus())
                                .concat("'")
                                .concat(",")

                                .concat("'")
                                .concat(ObjOrderDetailMaster.getEntryDateTime())
                                .concat("'")
                                .concat(");");


                Log.e("ClsOrderDetails", "Qry--->>" + qry);

                Log.e("grant_total", String.valueOf(ObjOrderDetailMaster.getGrand_Total()));
                SQLiteStatement sqLiteStatement = db.compileStatement(qry);
                result = sqLiteStatement.executeUpdateDelete();

                Log.e("ClsOrderDetails", "Qry--->>result = " + result);


                //insert in custmst
                //if mobileno != "" and lenght ==10
                //{insert } else skip
            }


        } catch (Exception e) {
            Log.e("ClsOrderDetails", "Catch--->>>" + e.getMessage());
        }

        return result;
    }

    public static List<ClsOrderDetailMaster> getList(String _where) {
        List<ClsOrderDetailMaster> list = new ArrayList<>();

        try {

//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            }
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_PRIVATE, null);
            String qry = "SELECT "
                    .concat("[ORDERDETAIL_ID]")
                    .concat(",[ORDER_ID]")
                    .concat(",[ORDER_NO]")
                    .concat(",[ITEM_ID]")
                    .concat(",[ITEM_NAME]")
                    .concat(",[RATE]")
                    .concat(",[TOTAL_AMOUNT]")
                    .concat(",[OTHER_TAX1]")
                    .concat(",[OTHER_VAL1]")
                    .concat(",[OTHER_TAX2]")
                    .concat(",[OTHER_VAL2]")
                    .concat(",[OTHER_TAX3]")
                    .concat(",[OTHER_VAL3]")
                    .concat(",[OTHER_TAX4]")
                    .concat(",[OTHER_VAL4]")
                    .concat(",[OTHER_TAX5]")
                    .concat(",[OTHER_VAL5]")
                    .concat(",[TOTAL_TAXAMOUNT]")
                    .concat(",[QUANTITY]")
                    .concat(",[GRAND_TOTAL]")
                    .concat(",[TYPE]")
                    .concat(",[STATUS]")
                    .concat(",[ENTRYDATETIME]")
                    .concat(" FROM [OrderDetail_master] WHERE 1=1 ")
                    .concat(_where)
                    .concat(" ORDER BY [ENTRYDATETIME] ASC ");


            Cursor cursor = db.rawQuery(qry, null);
            Log.e("ClsOrderDetailsMaster", "CursorCount--" + cursor.getCount());
            while (cursor.moveToNext()) {
                ClsOrderDetailMaster objOrder = new ClsOrderDetailMaster(context);
                objOrder.setOrderDetailId(cursor.getInt(cursor.getColumnIndex("ORDERDETAIL_ID")));
                objOrder.setOrderId(cursor.getInt(cursor.getColumnIndex("ORDER_ID")));
                objOrder.setOrderNo(cursor.getString(cursor.getColumnIndex("ORDER_NO")));
                objOrder.setItem_ID(cursor.getInt(cursor.getColumnIndex("ITEM_ID")));
                objOrder.setItem_Name(cursor.getString(cursor.getColumnIndex("ITEM_NAME")));
                objOrder.setRate(cursor.getDouble(cursor.getColumnIndex("RATE")));
                objOrder.setTotal_Amount(cursor.getDouble(cursor.getColumnIndex("TOTAL_AMOUNT")));
                objOrder.setOtherTax1(cursor.getString(cursor.getColumnIndex("OTHER_TAX1")));
                objOrder.setOtherTax2(cursor.getString(cursor.getColumnIndex("OTHER_TAX2")));
                objOrder.setOtherTax3(cursor.getString(cursor.getColumnIndex("OTHER_TAX3")));
                objOrder.setOtherTax4(cursor.getString(cursor.getColumnIndex("OTHER_TAX4")));
                objOrder.setOtherTax5(cursor.getString(cursor.getColumnIndex("OTHER_TAX5")));
                objOrder.setOtherTaxVal1(cursor.getDouble(cursor.getColumnIndex("OTHER_VAL1")));
                objOrder.setOtherTaxVal2(cursor.getDouble(cursor.getColumnIndex("OTHER_VAL2")));
                objOrder.setOtherTaxVal3(cursor.getDouble(cursor.getColumnIndex("OTHER_VAL3")));
                objOrder.setOtherTaxVal4(cursor.getDouble(cursor.getColumnIndex("OTHER_VAL4")));
                objOrder.setOtherTaxVal5(cursor.getDouble(cursor.getColumnIndex("OTHER_VAL5")));
                objOrder.setQty(cursor.getDouble(cursor.getColumnIndex("QUANTITY")));
                objOrder.setTotalTaxAmount(cursor.getDouble(cursor.getColumnIndex("TOTAL_TAXAMOUNT")));
                objOrder.setGrand_Total(cursor.getDouble(cursor.getColumnIndex("GRAND_TOTAL")));
                objOrder.setType(cursor.getString(cursor.getColumnIndex("TYPE")));
                objOrder.setStatus(cursor.getString(cursor.getColumnIndex("STATUS")));
                objOrder.setEntryDateTime(cursor.getString(cursor.getColumnIndex("ENTRYDATETIME")));
                list.add(objOrder);

            }

        } catch (Exception e) {
            Log.e("ClsOrder", "GetList--->>>" + e.getMessage());
        }

        return list;
    }

    @SuppressLint("WrongConstant")
    public static int Delete(int _OrderDetailID) {
        int result = 0;
        try {

//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            }

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "DELETE FROM [OrderDetail_master] WHERE [ORDERDETAIL_ID] = "
                    .concat(String.valueOf(_OrderDetailID))
                    .concat(";");
            Log.e("DELETE", "DELqRY-" + strSql);
            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsOrderDetailMaster", "Delete" + e.getMessage());
            e.getMessage();
        }
        return result;


    }


    @SuppressLint("WrongConstant")
    public static int Delete_OrderDetail_master(int OrderNo,Context context) {
        int result = 0;
        try {

//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            }

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "DELETE FROM [InventoryOrderMaster] WHERE [ORDER_NO] = "
                    .concat("'").concat(String.valueOf(OrderNo)).concat("'")
                    .concat(";");
            Log.e("DELETE", "DELqRY-" + strSql);
            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsOrderDetailMaster", "Delete" + e.getMessage());
            e.getMessage();
        }
        return result;


    }




}
