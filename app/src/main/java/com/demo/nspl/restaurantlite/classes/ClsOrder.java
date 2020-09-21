package com.demo.nspl.restaurantlite.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Desktop on 5/11/2018.
 */

public class ClsOrder {
    int Order_id, Table_id, cat_id;
    String Order_no;
    String Order_Datetime;
    String Table_No;
    String Source;
    String EntryDateTime;
    String Category_name;
    String Mobile_No;
    double Discount,GRAND_TOTAL;

    public double getGRAND_TOTAL() {
        return GRAND_TOTAL;
    }

    public void setGRAND_TOTAL(double GRAND_TOTAL) {
        this.GRAND_TOTAL = GRAND_TOTAL;
    }

    public double getDiscount() {
        return Discount;
    }

    public void setDiscount(double discount) {
        Discount = discount;
    }

    Double Total_TaxAmount, Total_Amount;
    static Context context;
    private static SQLiteDatabase db;


    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public String getCategory_name() {
        return Category_name;
    }

    public void setCategory_name(String category_name) {
        Category_name = category_name;
    }


    public ClsOrder() {

    }

    public ClsOrder(Context ctx) {

        context = ctx;
    }

    public String getMobile_No() {
        return Mobile_No;
    }

    public void setMobile_No(String mobile_No) {
        Mobile_No = mobile_No;
    }
    public int getOrder_id() {
        return Order_id;
    }

    public void setOrder_id(int order_id) {
        Order_id = order_id;
    }

    public int getTable_id() {
        return Table_id;
    }

    public void setTable_id(int table_id) {
        Table_id = table_id;
    }

    public String getOrder_no() {
        return Order_no;
    }

    public void setOrder_no(String order_no) {
        Order_no = order_no;
    }

    public String getOrder_Datetime() {
        return Order_Datetime;
    }

    public void setOrder_Datetime(String order_Datetime) {
        Order_Datetime = order_Datetime;
    }

    public String getTable_No() {
        return Table_No;
    }

    public void setTable_No(String table_No) {
        Table_No = table_No;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getEntryDateTime() {
        return EntryDateTime;
    }

    public void setEntryDateTime(String entryDateTime) {
        EntryDateTime = entryDateTime;
    }

    public Double getTotal_TaxAmount() {
        return Total_TaxAmount;
    }

    public void setTotal_TaxAmount(Double total_TaxAmount) {
        Total_TaxAmount = total_TaxAmount;
    }

    public Double getTotal_Amount() {
        return Total_Amount;
    }

    public void setTotal_Amount(Double total_Amount) {
        Total_Amount = total_Amount;
    }


/*    @SuppressLint("WrongConstant")
    public static List<ClsCategory> getList(String _where) {
        List<ClsCategory> list = new ArrayList<>();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            Cursor cur = db.rawQuery("SELECT "
                    .concat("[CATEGORY_ID]")
                    .concat(",[CATEGORY_NAME]")
                    .concat(",[ACTIVE]")
                    .concat(",[REMARK]")
                    .concat(",[SORT_NO]")
                    .concat(" FROM [CATEGORY_MASTER] WHERE 1=1 ")
                    .concat(_where)
                    .concat(" ORDER BY [CATEGORY_NAME] ASC "), null);


            while (cur.moveToNext()) {
                ClsCategory getSet = new ClsCategory();
                getSet.setCat_id(cur.getInt(cur.getColumnIndex("CATEGORY_ID")));

                getSet.setCat_name(cur.getString(cur.getColumnIndex("CATEGORY_NAME")));
                getSet.setSort_no(cur.getInt(cur.getColumnIndex("SORT_NO")));
                getSet.setActive(cur.getString(cur.getColumnIndex("ACTIVE")));
                getSet.setRemark(cur.getString(cur.getColumnIndex("REMARK")));
                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("ClsCategory", "GetList" + e.getMessageSales());
            e.getMessageSales();
        }
        return list;
    }*/

    public static ClsOrder getAmounts(String where,Context context){

        ClsOrder objOrder = new ClsOrder(context);
        try {
            db =context.openOrCreateDatabase(ClsGlobal.Database_Name,Context.MODE_PRIVATE,null);

            String qry= "SELECT "
                    .concat("[TOTAL_AMOUNT]")
                    .concat(",[DISCOUNT]")
                    .concat(",[TOTAL_TAXAMOUNT]")
                    .concat(",[GRAND_TOTAL]")
                    .concat(" FROM [Ordermaster] WHERE 1=1 ")
                    .concat(where);

            Log.e("qry",qry);

            Cursor cursor=db.rawQuery(qry,null);
            Log.e("cursor", String.valueOf(cursor.getCount()));

            while (cursor.moveToNext())
            {
                objOrder.setTotal_Amount(cursor.getDouble(cursor.getColumnIndex("TOTAL_AMOUNT")));
                objOrder.setTotal_TaxAmount(cursor.getDouble(cursor.getColumnIndex("TOTAL_TAXAMOUNT")));
                objOrder.setDiscount(cursor.getDouble(cursor.getColumnIndex("DISCOUNT")));
                objOrder.setGRAND_TOTAL(cursor.getDouble(cursor.getColumnIndex("GRAND_TOTAL")));

            }

        }catch (Exception e){

        }

        return objOrder;
    }

    public static List<ClsOrder> getList(String _where) {
        List<ClsOrder> list = new ArrayList<>();
        try {
           db =context.openOrCreateDatabase(ClsGlobal.Database_Name,Context.MODE_PRIVATE,null);
           String qry="SELECT "
                   .concat("[ORDER_ID]")
                   .concat(",[ORDER_NO]")
                   .concat(",[ORDER_DATETIME]")
                   .concat(",[TABLE_ID]")
                   .concat(",[TABLE_NO]")
                   .concat(",[SOURCE]")
                   .concat(",[TOTAL_TAXAMOUNT]")
                   .concat(",[ENTRYDATETIME]")
                   .concat(",[TOTAL_AMOUNT]")
                   .concat(",[DISCOUNT]")
                   .concat(" FROM [Ordermaster] WHERE 1=1 ")
                   .concat(_where)
                   .concat(" ORDER BY [ORDER_DATETIME] DESC ");

            Log.e("Qry",qry);
            Cursor cursor=db.rawQuery(qry,null);
            Log.e("ClsOrder","CursorCount--" +cursor.getCount());
            Log.e("ClsOrder","CursorCount--" + Arrays.toString(cursor.getColumnNames()));

            while (cursor.moveToNext())
           {
               ClsOrder objOrder = new ClsOrder(context);
               objOrder.setOrder_id(cursor.getInt(cursor.getColumnIndex("ORDER_ID")));
               objOrder.setOrder_no(cursor.getString(cursor.getColumnIndex("ORDER_NO")));
               objOrder.setOrder_Datetime(ClsGlobal.
                       getEntryDateFormat(cursor.getString(cursor.getColumnIndex("ORDER_DATETIME"))));
               objOrder.setTable_id(cursor.getInt(cursor.getColumnIndex("TABLE_ID")));
               objOrder.setTable_No(cursor.getString(cursor.getColumnIndex("TABLE_NO")));
               objOrder.setSource(cursor.getString(cursor.getColumnIndex("SOURCE")));
               objOrder.setEntryDateTime(cursor.getString(cursor.getColumnIndex("ENTRYDATETIME")));
               objOrder.setTotal_Amount(cursor.getDouble(cursor.getColumnIndex("TOTAL_AMOUNT")));
               objOrder.setTotal_TaxAmount(cursor.getDouble(cursor.getColumnIndex("TOTAL_TAXAMOUNT")));
               objOrder.setDiscount(cursor.getDouble(cursor.getColumnIndex("DISCOUNT")));
               list.add(objOrder);

           }

        }catch (Exception e)
        {
            Log.e("ClsOrder","GetList--->>>"+e.getMessage());
        }

        return list;
    }


    public static Double getIncome(Context context) {

        Double getIncome=0.0;

        try {
            db =context.openOrCreateDatabase(ClsGlobal.Database_Name,Context.MODE_PRIVATE,null);
            String qry="SELECT "
                    .concat("SUM ([GRAND_TOTAL]) as [INCOME]")
                    .concat(" FROM [Ordermaster] WHERE 1=1 ");

            Log.e("IncomeQry",qry);
            Cursor cursor=db.rawQuery(qry,null);
            Log.e("ClsOrder","CursorCount--" +cursor.getCount());
            Log.e("ClsOrder","CursorCount--" + Arrays.toString(cursor.getColumnNames()));

            while (cursor.moveToNext())
            {
                getIncome = cursor.getDouble(cursor.getColumnIndex("INCOME"));
            }

        }catch (Exception e)
        {
            Log.e("ClsOrder","GetList--->>>"+e.getMessage());
        }

        Log.e("getIncome", String.valueOf(getIncome));

        return getIncome;
    }

    @SuppressLint("WrongConstant")
    public static int Insert(ClsOrder ObjOrder) {
        int result = 0;
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);


            String qry = "INSERT INTO [Ordermaster] ([ORDER_NO],[ORDER_DATETIME]" +
                    ",[TABLE_ID],[TABLE_NO],[SOURCE]" +
                    ",[ENTRYDATETIME],[TOTAL_AMOUNT]) VALUES('"

                            .concat(ObjOrder.getOrder_no())
                            .concat("'")
                            .concat(",")

                            .concat("'")
                            .concat(ObjOrder.getOrder_Datetime())
                            .concat("'")
                            .concat(",")

                            .concat(String.valueOf(ObjOrder.getTable_id()))
                            .concat(",")

                            .concat("'")
                            .concat(ObjOrder.getTable_No())
                            .concat("'")
                            .concat(",")

                            .concat("'")
                            .concat(ObjOrder.getSource())
                            .concat("'")
                            .concat(",")


                            .concat("'")
                            .concat(ClsGlobal.getCurruntDateTime())
                            .concat("'")
                            .concat(",")

                            .concat(String.valueOf(ObjOrder.getTotal_Amount()))

                            .concat(");");
            Log.e("ClsOrder",qry );

            SQLiteStatement statement=db.compileStatement(qry);
            result=statement.executeUpdateDelete();
            db.close();
            return result;

        } catch (Exception e) {
            Log.e("ClsOrder","Insert---"+e.getMessage());
        }

        return result;
    }

    @SuppressLint("WrongConstant")
    public static int GetMaxItemId() {
        int idd = 0;
        try {
        db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
        String qry="SELECT MAX([ORDER_ID]) as ORDER_ID from [Ordermaster]";
        Cursor cur=db.rawQuery(qry,null);

            Log.e("Cursor", "qry--"+qry);
            Log.e("Cursor", "--->>>"+String.valueOf(cur.getCount()));

            while (cur.moveToNext()) {
                ClsOrder getSet = new ClsOrder();
                getSet.setOrder_id(cur.getInt(cur.getColumnIndex("ORDER_ID")));
                    idd = cur.getInt(cur.getColumnIndex("ORDER_ID"));
                Log.e("IDDDDDDDD","Test--"+idd );

            }

        } catch (Exception e) {
            Log.e("ClsOrder", "GetMaxItemId--->>" + e.getMessage());
            e.getMessage();
        }
        return idd+1;
    }




    @SuppressLint("WrongConstant")
    public static int Delete(ClsOrder ObjOrder) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "DELETE FROM [Ordermaster] WHERE [ORDER_ID] = "
                    .concat(String.valueOf(ObjOrder.getOrder_id()))
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
