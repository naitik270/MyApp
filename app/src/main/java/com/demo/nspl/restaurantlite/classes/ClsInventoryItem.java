package com.demo.nspl.restaurantlite.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Desktop on 3/16/2018.
 */

public class ClsInventoryItem {

    int inventory_item_id, unit_id;
    String inventory_item_name;
    String unit_name;

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    String Type;
    String remark;
    String active;
    Double min_qty;
    Double max_stock;
    Double InQuantity;
    Double OutQuantity;

    public Double getQuantity() {
        return Quantity;
    }

    public void setQuantity(Double quantity) {
        Quantity = quantity;
    }

    Double Quantity;

    public Double getInQuantity() {
        return InQuantity;
    }

    public void setInQuantity(Double inQuantity) {
        InQuantity = inQuantity;
    }

    public Double getOutQuantity() {
        return OutQuantity;
    }

    public void setOutQuantity(Double outQuantity) {
        OutQuantity = outQuantity;
    }

    public Double getMin_qty() {
        return min_qty;
    }

    public void setMin_qty(Double min_qty) {
        this.min_qty = min_qty;
    }

    public Double getMax_stock() {
        return max_stock;
    }

    public void setMax_stock(Double max_stock) {
        this.max_stock = max_stock;
    }

    ClsUnit ObjUnit;

    public ClsUnit getObjUnit() {
        return ObjUnit;
    }

    public void setObjUnit(ClsUnit objUnit) {
        ObjUnit = objUnit;
    }

    static Context context;
    AppCompatActivity activity;
    private static SQLiteDatabase db;

    public ClsInventoryItem() {

    }

    public ClsInventoryItem(Context ctx) {

        context = ctx;
    }

    public int getInventory_item_id() {
        return inventory_item_id;
    }

    public void setInventory_item_id(int inventory_item_id) {
        this.inventory_item_id = inventory_item_id;
    }

    public int getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(int unit_id) {
        this.unit_id = unit_id;
    }

    public String getInventory_item_name() {
        return inventory_item_name;
    }

    public void setInventory_item_name(String inventory_item_name) {
        this.inventory_item_name = inventory_item_name;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    @SuppressLint("WrongConstant")
    public static String getInventoryMis(String _where) {
        List<ClsInventoryStock> list = new ArrayList<>();

        StringBuilder stringBuilder = new StringBuilder();
        db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
        try {
            String qry = "Select "
                    .concat("item.[INVENTORY_ITEM_NAME]")
                    .concat(",item.[UNIT_NAME]")
                    .concat(",SUM(ifnull(item.[MAX_STOCK_QTY], 0)) as [MAX_STOCK_QTY]")
                    .concat(",SUM(ifnull(item.[MIN_STOCK_QTY], 0)) as [MIN_STOCK_QTY]")
                    .concat(",SUM(ifnull(stk.[QUANTITY],0)) as [QUANTITY]")
                    .concat(",stk.[TYPE]")
//                    .concat(",CASE")
//                    .concat(" WHEN stk.[TYPE] == 'IN' THEN stk.[QUANTITY]")
//                    .concat(" ELSE 0.0")
//                    .concat(" END as [InQuantity]")
//                    .concat(",CASE")
//                    .concat(" WHEN stk.[TYPE] == 'OUT' THEN stk.[QUANTITY]")
//                    .concat(" ELSE 0.0")
//                    .concat(" END as [OutQuantity]")
                    .concat(" from  [INVENTORY_ITEM_MASTER] as item ")
                    .concat("left join [Inventory_stock_master] as stk on stk.[INVENTORY_ITEM_ID] = item.[INVENTORY_ITEM_ID] ")
                    .concat(" WHERE 1=1 ")
                    .concat(_where)
                    .concat(" GROUP BY ")
                    .concat("item.[INVENTORY_ITEM_NAME]")
                    .concat(",item.[UNIT_NAME]")
//                    .concat(",item.[MAX_STOCK_QTY]")
//                    .concat(",item.[MIN_STOCK_QTY]")
//                    .concat(",stk.[QUANTITY]")
                    .concat(",stk.[TYPE]")
                    .concat(" ORDER BY item.[INVENTORY_ITEM_NAME];");

            Log.e("qry", qry);

            Cursor cur = db.rawQuery(qry, null);
            Log.e("cur", String.valueOf(cur.getCount()));


            while (cur.moveToNext()) {
                ClsInventoryStock Obj = new ClsInventoryStock();
                Obj.setInventory_item_name(cur.getString(cur.getColumnIndex("INVENTORY_ITEM_NAME")));
                Obj.setUnitname(cur.getString(cur.getColumnIndex("UNIT_NAME")));
                Obj.setType(cur.getString(cur.getColumnIndex("TYPE")));
                Obj.setMin_qty(cur.getDouble(cur.getColumnIndex("MIN_STOCK_QTY")));
                Obj.setMax_qty(cur.getDouble(cur.getColumnIndex("MAX_STOCK_QTY")));
//                Obj.setInQuantity(cur.getDouble(cur.getColumnIndex("InQuantity")));
//                Obj.setOutQuantity(cur.getDouble(cur.getColumnIndex("OutQuantity")));
                Obj.setQty(cur.getDouble(cur.getColumnIndex("QUANTITY")));
//                Log.e("INVENTORY_ITEM_NAME", String.valueOf(cur.getString(cur.getColumnIndex("INVENTORY_ITEM_NAME"))));
//                Log.e("INVENTORY_ITEM_NAME", String.valueOf(cur.getString(cur.getColumnIndex("INVENTORY_ITEM_NAME"))));
//                Log.e("UNIT_NAME", String.valueOf(cur.getString(cur.getColumnIndex("UNIT_NAME"))));
//                Log.e("MAX_STOCK_QTY", String.valueOf(cur.getString(cur.getColumnIndex("MAX_STOCK_QTY"))));
//                Log.e("MIN_STOCK_QTY", String.valueOf(cur.getString(cur.getColumnIndex("MIN_STOCK_QTY"))));
//                Log.e("InQuantity", String.valueOf(cur.getString(cur.getColumnIndex("InQuantity"))));
//                Log.e("OutQuantity", String.valueOf(cur.getString(cur.getColumnIndex("OutQuantity"))));
                //   Log.e("TYPE234", String.valueOf(cur.getString(cur.getColumnIndex("TYPE"))));

                list.add(Obj);
            }

//            list.clear();

            if (list != null && list.size() != 0) {
                List<ClsInventoryStock> list_summary = new ArrayList<>();

                List<String> uniqueList = new ArrayList<>();
                for (ClsInventoryStock stock : list) {
                    if (!uniqueList.contains(stock.getInventory_item_name())) {
                        uniqueList.add(stock.getInventory_item_name());
                    }
                }

                for (String itemName : uniqueList) {
                    ClsInventoryStock ObjStk = new ClsInventoryStock();
                    double inQty = 0.0;
                    double outqTY = 0.0;
                    for (ClsInventoryStock stock : list) {
                        if (itemName.equalsIgnoreCase(stock.getInventory_item_name())) {
                            if (stock.getType().equalsIgnoreCase("IN")) {
                                Log.e("QUANTIIII", String.valueOf(stock.getQty()));
                                inQty = inQty + stock.getQty();
                                Log.e("TOTAl", "In----" + stock.getInventory_item_name() + inQty);
                            } else if (stock.getType().equalsIgnoreCase("OUT")) {
                                outqTY = outqTY + stock.getQty();
                                Log.e("TOTAl", "Out----" + outqTY);
                            }
                            ObjStk.setInventory_item_name(stock.getInventory_item_name());
                            ObjStk.setInqty(inQty);
                            ObjStk.setOutqty(outqTY);
                            ObjStk.setMin_qty(stock.getMin_qty());
                            ObjStk.setMax_qty(stock.getMax_qty());
                            ObjStk.setUnitname(stock.getUnitname());
                        }
                    }
                    list_summary.add(ObjStk);
                }


//            Gson gson = new Gson();
////            // convert your list to json
//            String jsonCartList = gson.toJson(list);
//            Log.e("jsonCartList", jsonCartList);
//
                Log.e("jsonCartList", String.valueOf(list.size()));


                int sr = 0;
                stringBuilder.append("<table class=\"table\">");
                stringBuilder.append("<thead>");

                stringBuilder.append("<tr>");

                stringBuilder.append("<th style=\"white-space: normal !important;\">Sr#</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Item Name</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Unit</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Min</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Max</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">In</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Out</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Stock</th>");

                stringBuilder.append("</tr>");

                stringBuilder.append("</thead>");
                stringBuilder.append("<tbody>");//new ROW st

                for (ClsInventoryStock current : list_summary) {
                    sr++;

                    // Tr Tag Open.
                    stringBuilder.append("<tr>");//new ROW start

                    //1st cell
                    stringBuilder.append("<td>");//cell start
                    stringBuilder.append(sr);//cell value
                    stringBuilder.append("</td>");//cell end

                    //2st cell
                    stringBuilder.append("<td>");//cell start
                    stringBuilder.append(current.getInventory_item_name());//cell value
                    stringBuilder.append("</td>");//cell end

                    //2st cell
                    stringBuilder.append("<td>");//cell start
                    stringBuilder.append(current.getUnitname());//cell value
                    stringBuilder.append("</td>");//cell end

                    //3st cell
                    stringBuilder.append("<td>");//cell start
                    stringBuilder.append(current.getMin_qty());//cell value
                    stringBuilder.append("</td>");//cell end

                    //4st cell
                    stringBuilder.append("<td>");//cell start
                    stringBuilder.append(current.getMax_qty());//cell value
                    stringBuilder.append("</td>");//cell end


                    //5st cell
                    stringBuilder.append("<td>");//cell start
                    stringBuilder.append("" + current.getInqty());//cell value
                    stringBuilder.append("</td>");//cell end


                    //6st cell
                    stringBuilder.append("<td>");//cell start
                    stringBuilder.append("" + current.getOutqty());//cell value
                    stringBuilder.append("</td>");//cell end

                    //7st cell
                    Double stock = current.getInqty() - current.getOutqty();
                    stringBuilder.append("<td>");//cell start
                    stringBuilder.append("" + stock);//cell value
                    stringBuilder.append("</td>");//cell end

                    // Tr Tag Close.
                    stringBuilder.append("</tr>");//cell end
                }
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


    @SuppressLint("WrongConstant")
    public static int Insert(ClsUnit objUnit, ClsInventoryItem ObjClsInventoryItem) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String qry = ("INSERT INTO [INVENTORY_ITEM_MASTER] ([INVENTORY_ITEM_NAME]" +
                    ",[UNIT_ID],[UNIT_NAME],[MAX_STOCK_QTY],[MIN_STOCK_QTY],[ACTIVE],[REMARK]) VALUES ('")

                    .concat(ObjClsInventoryItem.getInventory_item_name().trim()
                            .replace("'","''"))
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(objUnit.getUnit_id()))
                    .concat(",")

                    .concat("'")
                    .concat(objUnit.getUnit_name().trim()
                            .replace("'","''"))
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(ObjClsInventoryItem.getMax_stock()))
                    .concat(",")

                    .concat(String.valueOf(ObjClsInventoryItem.getMin_qty()))
                    .concat(",")

                    .concat("'")
                    .concat(ObjClsInventoryItem.getActive())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjClsInventoryItem.getRemark().trim()
                            .replace("'","''"))
                    .concat("'")


                    .concat(");");

            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.e("INSERT", "Inventory--->>" + qry);

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsInventory", "Insert>>>>>>>>" + e.getMessage());
            e.getMessage();
        }
        return result;


    }

    @SuppressLint("WrongConstant")
    public boolean checkExists(String _where) {
        boolean result = false;

        try {


            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = "SELECT 1 FROM [INVENTORY_ITEM_MASTER] WHERE 1=1 "
                    .concat(_where)
                    .concat(";");
            Log.e("exists", "existsChqQry - " + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            if (cur != null && cur.getCount() != 0) {
                result = true;
            }
            db.close();
        } catch (Exception e) {
            Log.e("ClsInventory", "ClsInventory>>>>CheckExist" + e.getMessage());
            e.getMessage();
        }
        return result;


    }

    @SuppressLint("WrongConstant")
    public static int Delete(ClsInventoryItem objInventoryItem) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "DELETE FROM [INVENTORY_ITEM_MASTER] WHERE [INVENTORY_ITEM_ID] = "
                    .concat(String.valueOf(objInventoryItem.getInventory_item_id()))
                    .concat(";");
            Log.e("DELETE", "DELqRY-" + strSql);
            SQLiteStatement statement = db.compileStatement(strSql);

            result = statement.executeUpdateDelete();

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsInventory", "Delete" + e.getMessage());
            e.getMessage();
        }
        return result;


    }

    @SuppressLint("WrongConstant")
    public ClsInventoryItem getObject(ClsInventoryItem ObjInventoryItem) {
        ClsInventoryItem Obj = new ClsInventoryItem();

        try {


            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = "SELECT [INVENTORY_ITEM_ID],[INVENTORY_ITEM_NAME],[UNIT_ID],[UNIT_NAME],[MAX_STOCK_QTY],[MIN_STOCK_QTY],[ACTIVE],[REMARK] FROM [INVENTORY_ITEM_MASTER] WHERE 1=1 AND [INVENTORY_ITEM_ID] = "
                    .concat(String.valueOf(ObjInventoryItem.getInventory_item_id()))

                    .concat(";");
            Log.e("Update", ">>>>>>>>>" + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            Log.e("Update", ">>>>>>>>>" + cur.getCount());
            while (cur.moveToNext()) {

                Obj.setInventory_item_id(cur.getInt(cur.getColumnIndex("INVENTORY_ITEM_ID")));
                Obj.setInventory_item_name(cur.getString(cur.getColumnIndex("INVENTORY_ITEM_NAME")));
                Obj.setUnit_id(cur.getInt(cur.getColumnIndex("UNIT_ID")));
                Obj.setUnit_name(cur.getString(cur.getColumnIndex("UNIT_NAME")));
                Obj.setActive(cur.getString(cur.getColumnIndex("ACTIVE")));
                Obj.setMax_stock(cur.getDouble(cur.getColumnIndex("MAX_STOCK_QTY")));
                Obj.setMin_qty(cur.getDouble(cur.getColumnIndex("MIN_STOCK_QTY")));
                Obj.setRemark(cur.getString(cur.getColumnIndex("REMARK")));

            }

            db.close();

        } catch (Exception e) {
            Log.e("ClsItem", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }
        return Obj;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsInventoryItem> getList(String _where) {

        List<ClsInventoryItem> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT "
                    .concat("[INVENTORY_ITEM_ID]")
                    .concat(",[INVENTORY_ITEM_NAME]")
                    .concat(",[UNIT_ID]")
                    .concat(",[UNIT_NAME]")
                    .concat(",[ACTIVE]")
                    .concat(",[MAX_STOCK_QTY]")
                    .concat(",[MIN_STOCK_QTY]")
                    .concat(",[REMARK]")
                    .concat(" FROM [INVENTORY_ITEM_MASTER] WHERE 1=1 ")
                    .concat(_where)
                    .concat(" ORDER BY [INVENTORY_ITEM_NAME] ASC ");

            Log.e("qry",qry);

            Cursor cur = db.rawQuery(qry, null);

            while (cur.moveToNext()) {
                ClsInventoryItem getSet = new ClsInventoryItem();
                getSet.setInventory_item_id(cur.getInt(cur.getColumnIndex("INVENTORY_ITEM_ID")));
                getSet.setInventory_item_name(cur.getString(cur.getColumnIndex("INVENTORY_ITEM_NAME")));
                getSet.setUnit_id(cur.getInt(cur.getColumnIndex("UNIT_ID")));
                getSet.setUnit_name(cur.getString(cur.getColumnIndex("UNIT_NAME")));
                getSet.setActive(cur.getString(cur.getColumnIndex("ACTIVE")));
                getSet.setMax_stock(cur.getDouble(cur.getColumnIndex("MAX_STOCK_QTY")));
                getSet.setMin_qty(cur.getDouble(cur.getColumnIndex("MIN_STOCK_QTY")));
                getSet.setRemark(cur.getString(cur.getColumnIndex("REMARK")));
                list.add(getSet);
            }
        } catch (Exception e) {
            Log.e("ClsItem", "ClsItem >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static List<String> getItemAutoSuggestionList() {

        List<String> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT "
                    .concat(" [INVENTORY_ITEM_NAME]")
                    .concat(" FROM [INVENTORY_ITEM_MASTER] WHERE 1=1 ")
                    .concat(" AND [ACTIVE] = 'YES' ")
                    .concat(" ORDER BY [INVENTORY_ITEM_NAME] ASC ");

            Log.e("qry",qry);

            Cursor cur = db.rawQuery(qry, null);

            while (cur.moveToNext()) {
                list.add(cur.getString(cur.getColumnIndex("INVENTORY_ITEM_NAME")));
            }

        } catch (Exception e) {
            Log.e("ClsItem", "ClsItem >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsInventoryItem> getList() {

        List<ClsInventoryItem> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            Cursor cur = db.rawQuery("SELECT "
                    .concat("[INVENTORY_ITEM_ID]")
                    .concat(",[INVENTORY_ITEM_NAME]")
                    .concat(",[MAX_STOCK_QTY]")
                    .concat(",[MIN_STOCK_QTY]")
                    .concat(",[UNIT_NAME]")
                    .concat(" FROM [INVENTORY_ITEM_MASTER] WHERE 1=1 ")
                    .concat("  AND [ACTIVE]='YES'")
                    .concat(" ORDER BY [INVENTORY_ITEM_NAME] ASC "), null);

            while (cur.moveToNext()) {
                ClsInventoryItem getSet = new ClsInventoryItem();
                getSet.setInventory_item_id(cur.getInt(cur.getColumnIndex("INVENTORY_ITEM_ID")));
                getSet.setInventory_item_name(cur.getString(cur.getColumnIndex("INVENTORY_ITEM_NAME")));
                getSet.setMax_stock(cur.getDouble(cur.getColumnIndex("MAX_STOCK_QTY")));
                getSet.setMin_qty(cur.getDouble(cur.getColumnIndex("MIN_STOCK_QTY")));
                getSet.setUnit_name(cur.getString(cur.getColumnIndex("UNIT_NAME")));
                Log.e("NewID", "--->" + cur.getInt(cur.getColumnIndex("INVENTORY_ITEM_ID")));
                list.add(getSet);
            }
        } catch (Exception e) {
            Log.e("ClsItem", "ClsItem >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static int Update(ClsInventoryItem objInventoryItem) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "UPDATE [INVENTORY_ITEM_MASTER] SET "
                    .concat("[INVENTORY_ITEM_NAME] = ")
                    .concat("'")
                    .concat(objInventoryItem.getInventory_item_name().trim()
                            .replace("'","''"))
                    .concat("'")

                    .concat(" ,[UNIT_ID] = ")
                    .concat(String.valueOf(objInventoryItem.getUnit_id()))

                    .concat(" ,[UNIT_NAME] = ")
                    .concat("'")
                    .concat(objInventoryItem.getUnit_name().trim()
                            .replace("'","''"))
                    .concat("'")


                    .concat(" ,[MAX_STOCK_QTY] = ")

                    .concat(String.valueOf(objInventoryItem.getMax_stock()))

                    .concat(" ,[MIN_STOCK_QTY] = ")

                    .concat(String.valueOf(objInventoryItem.getMin_qty()))
                    .concat(" ,[ACTIVE] = ")
                    .concat("'")
                    .concat(objInventoryItem.getActive())
                    .concat("'")


                    .concat(" ,[REMARK] = ")
                    .concat("'")
                    .concat(objInventoryItem.getRemark().trim()
                            .replace("'","''"))
                    .concat("'")


                    .concat(" WHERE [INVENTORY_ITEM_ID] = ")

                    .concat(String.valueOf(objInventoryItem.getInventory_item_id()))

                    .concat(";");
            Log.e("Update", strSql);
            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();
            //db.execSQL(strSql);
            db.close();

        } catch (Exception e) {
            Log.e("ClsCategory", "Update" + e.getMessage());
        }
        return result;
    }

}
