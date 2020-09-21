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
 * Created by Desktop on 3/19/2018.
 */

public class ClsInventoryStock {
    Double min_qty;

    public Double getMax_qty() {
        return max_qty;
    }

    public void setMax_qty(Double max_qty) {
        this.max_qty = max_qty;
    }

    Double max_qty;

    public Double getMin_qty() {
        return min_qty;
    }

    public void setMin_qty(Double min_qty) {
        this.min_qty = min_qty;
    }


    int stock_id, order_id, vendror_id, inventory_item_id;
    String inventory_item_name, type, entry_date, trasaction_date, remark;
    Double qty, amount;
    String vendor_name;
    Double InQty,OutQty;
    String formattedDate;




    public String getUnitname() {
        return unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    String unitname;

    public Double getInQty() {
        return InQty;
    }

    public void setInQty(Double inQty) {
        InQty = inQty;
    }

    public Double getOutQty() {
        return OutQty;
    }

    public void setOutQty(Double outQty) {
        OutQty = outQty;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    static Context context;
    AppCompatActivity activity;
    private static SQLiteDatabase db;
    ClsVendor objVendor;
    ClsInventoryItem objInventoryItem;
    Double Inqty, Outqty;

    public Double getInqty() {
        return Inqty;
    }

    public void setInqty(Double inqty) {
        Inqty = inqty;
    }

    public Double getOutqty() {
        return Outqty;
    }

    public void setOutqty(Double outqty) {
        Outqty = outqty;
    }

    public ClsInventoryItem getObjInventoryItem() {
        return objInventoryItem;
    }

    public void setObjInventoryItem(ClsInventoryItem objInventoryItem) {
        this.objInventoryItem = objInventoryItem;
    }

    public ClsVendor getObjVendor() {
        return objVendor;
    }

    public void setObjVendor(ClsVendor objVendor) {
        this.objVendor = objVendor;
    }

    public ClsInventoryStock() {

    }

    public ClsInventoryStock(Context ctx) {

        context = ctx;
    }

    public int getStock_id() {
        return stock_id;
    }

    public void setStock_id(int stock_id) {
        this.stock_id = stock_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getVendror_id() {
        return vendror_id;
    }

    public void setVendror_id(int vendror_id) {
        this.vendror_id = vendror_id;
    }

    public int getInventory_item_id() {
        return inventory_item_id;
    }

    public void setInventory_item_id(int inventory_item_id) {
        this.inventory_item_id = inventory_item_id;
    }

    public String getInventory_item_name() {
        return inventory_item_name;
    }

    public void setInventory_item_name(String inventory_item_name) {
        this.inventory_item_name = inventory_item_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEntry_date() {
        return entry_date;
    }

    public void setEntry_date(String entry_date) {
        this.entry_date = entry_date;
    }

    public String getTrasaction_date() {
        return trasaction_date;
    }

    public void setTrasaction_date(String trasaction_date) {
        this.trasaction_date = trasaction_date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }


    @SuppressLint("WrongConstant")
    public static int Insert(ClsInventoryItem ObjInVItem, ClsVendor ObjVendor, ClsInventoryStock ObjInventoryStock) {
        int result = 0;
        try {

//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            }


            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String qry = ("INSERT INTO [Inventory_stock_master] ([ORDER_ID]," +
                    "[VENDOR_ID],[INVENTORY_ITEM_ID],[INVENTORY_ITEM_NAME]" +
                    ",[QUANTITY],[AMOUNT],[TYPE],[ENTRY_DATE],[TRANSACTION_DATE],[REMARK]) VALUES (")

                    .concat(String.valueOf(ObjInventoryStock.getOrder_id()))
                    .concat(",")

                    .concat(String.valueOf(ObjInventoryStock.getVendror_id()))
                    .concat(",")

                    .concat(String.valueOf(ObjInVItem.getInventory_item_id()))
                    .concat(",")

                    .concat("'")
                    .concat(ObjInVItem.getInventory_item_name().trim()
                            .replace("'","''"))
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(ObjInventoryStock.getQty()))
                    .concat(",")

                    .concat(String.valueOf(ObjInventoryStock.getAmount()))
                    .concat(",")

                    .concat("'")
                    .concat(ObjInventoryStock.getType())
                    .concat("'")

                    .concat(",")

                    .concat("'")
                    .concat(ClsGlobal.getCurruntDateTime())
                    .concat("'")

//                    .concat(",")
//                    .concat("'")
//                    .concat(ObjInventoryStock.getTrasaction_date())
//                    .concat("'")


                    .concat(",")
                    .concat("'")
                    .concat(ClsGlobal.getDbDateFormat(ObjInventoryStock.getTrasaction_date()))
                    .concat("'")


                    .concat(",")
                    .concat("'")
                    .concat(ObjInventoryStock.getRemark().trim()
                            .replace("'","''"))
                    .concat("'")


                    .concat(");");

            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.e("ClsInventoryStock", "InventoryStock--->>" + qry);

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsInventoryStock", "InventoryStockCatch>>>>>>>>" + e.getMessage());
            e.getMessage();
        }
        return result;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsInventoryStock> getList(String _where, String src) {
        List<ClsInventoryStock> list = new ArrayList<>();
        try {
//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            }


            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);


            String Qry = "SELECT "
                    .concat("tblstock.[VENDOR_ID]")
                    .concat(",tblstock.[STOCK_ID]")
                    .concat(",tblstock.[INVENTORY_ITEM_ID]")
                    .concat(",tblstock.[INVENTORY_ITEM_NAME]")
                    .concat(",tblstock.[QUANTITY]")
                    .concat(",tblstock.[AMOUNT]")
                    .concat(",tblstock.[TYPE]")
//                    .concat(",tblstock.[ENTRY_DATE]")
                    .concat(",strftime('%m/%d/%Y %H:%M:%S',tblstock.[ENTRY_DATE]) [ENTRY_DATE]")
                    .concat(",tblstock.[TRANSACTION_DATE]")
                    .concat(",tblstock.[REMARK]")
                    .concat(",VM.[VENDOR_NAME]")
                    .concat(",IM.[UNIT_NAME]")
                    .concat(" FROM [Inventory_stock_master] as tblstock")
                    .concat(" LEFT JOIN [VENDOR_MASTER] as VM ON VM.[VENDOR_ID]=tblstock.[VENDOR_ID] ")
                    .concat(" LEFT JOIN [INVENTORY_ITEM_MASTER] as IM ON IM.[INVENTORY_ITEM_ID]=tblstock.[INVENTORY_ITEM_ID] ")
                    .concat(" WHERE 1=1 ")
                    .concat(_where);

            if (src.equals("general")) {
                Qry = Qry.concat(" ORDER BY tblstock.[TRANSACTION_DATE] ASC ");
            } else if (src.equals("dashboard")) {
                Qry = Qry.concat(" ORDER BY tblstock.[ENTRY_DATE] DESC ").concat(" LIMIT 5 ");
            }


            Cursor cur = db.rawQuery(Qry, null);
            Log.e("Entryyy", "--->>" + src + Qry);
            Log.e("Entryyy", "Cur--->>" + cur.getCount());

            for(int i=0;i<cur.getColumnCount();i++)
            {
                Log.e("Column",cur.getColumnName(i) );
            }

//            String qry=("SELECT "
//                    .concat("tblstock.[VENDOR_ID]")
//                    .concat(",tblstock.[INVENTORY_ITEM_ID]")
//                    .concat(",tblstock.[INVENTORY_ITEM_NAME]")
//                    .concat(",tblstock.[QUANTITY]")
//                    .concat(",tblstock.[AMOUNT]")
//                    .concat(",tblstock.[TYPE]")
//                    .concat(",tblstock.[ENTRY_DATE]")
//                    .concat(",tblstock.[TRANSACTION_DATE]")
//                    .concat(",tblstock.[REMARK]")
//                    .concat(",VM.[VENDOR_NAME]")
//                    .concat(" FROM [Inventory_stock_master] as tblstock")
//                    .concat(" INNER JOIN [VENDOR_MASTER] as VM ON VM.[VENDOR_ID]=tblstock.[VENDOR_ID]")
//                    .concat(_where)
//                    .concat(" ORDER BY tblstock.[TRANSACTION_DATE] ASC ") );

//            Log.e("ClsCategory", qry);

            while (cur.moveToNext()) {
                ClsInventoryStock getSet = new ClsInventoryStock();
                getSet.setVendror_id(cur.getInt(cur.getColumnIndex("VENDOR_ID")));
                getSet.setStock_id(cur.getInt(cur.getColumnIndex("STOCK_ID")));
                getSet.setInventory_item_id(cur.getInt(cur.getColumnIndex("INVENTORY_ITEM_ID")));
                getSet.setUnitname(cur.getString(cur.getColumnIndex("UNIT_NAME")));
                getSet.setQty(cur.getDouble(cur.getColumnIndex("QUANTITY")));
                getSet.setAmount(cur.getDouble(cur.getColumnIndex("AMOUNT")));
                getSet.setType(cur.getString(cur.getColumnIndex("TYPE")));
                getSet.setInventory_item_name(cur.getString(cur.getColumnIndex("INVENTORY_ITEM_NAME")));
                getSet.setVendor_name(cur.getString(cur.getColumnIndex("VENDOR_NAME")));
                Log.e("Vendors", "-->>" + cur.getInt(cur.getColumnIndex("INVENTORY_ITEM_ID")));
                Log.e("PreetyDateTimeFormat", "dbVAL--" + cur.getString(cur.getColumnIndex("ENTRY_DATE")));
                getSet.setEntry_date(cur.getString(cur.getColumnIndex("ENTRY_DATE")));
                getSet.setTrasaction_date(cur.getString(cur.getColumnIndex("TRANSACTION_DATE")));
                getSet.setRemark(cur.getString(cur.getColumnIndex("REMARK")));
                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("ClsInventoryStock", "GetListQuery--->>" + e.getMessage());
            e.getMessage();
        }
        return list;
    }



    /*
    *  Cursor cur = db.rawQuery("SELECT "
                    .concat("[STOCK_ID]")
                    .concat(",[ORDER_ID]")
                    .concat(",[VENDER_ID]")
                    .concat(",[INVENTORY_ITEM_ID]")
                    .concat(",[INVENTORY_ITEM_NAME]")
                    .concat(",[QUANTITY]")
                    .concat(",[AMOUNT]")
                    .concat(",[TYPE]")
                    .concat(",[ENTRY_DATE]")
                    .concat(",[TRANSACTION_DATE]")
                    .concat(",[REMARK]")
                    .concat(" FROM [Inventory_stock_master] WHERE 1=1 ")
                    .concat(_where)
                    .concat(" ORDER BY [TRANSACTION_DATE] ASC "), null);*/

    @SuppressLint("WrongConstant")
    public static List<ClsInventoryStock> getStock(String _where) {
        List<ClsInventoryStock> Resultlist = new ArrayList<>();

        List<ClsInventoryItem> Items_list = new ArrayList<>();
        Items_list = new ClsInventoryItem(context).getList();

        Log.e("ItemList", String.valueOf(Items_list.size()));

        if (Items_list != null && Items_list.size() != 0) {
//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            }

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            List<ClsInventoryStock> Inlist = new ArrayList<>();
            List<ClsInventoryStock> Outlist = new ArrayList<>();
            try {
                String qry = "SELECT "

                        .concat("stk.[INVENTORY_ITEM_ID]")
                        .concat(",stk.[INVENTORY_ITEM_NAME]")
                        .concat(",SUM(stk.[QUANTITY]) AS [QUANTITY]")
                        .concat(",IM.[UNIT_NAME]")
                        .concat(" FROM [Inventory_stock_master] as stk ")
                        .concat(" LEFT JOIN [INVENTORY_ITEM_MASTER] as IM ON IM.[INVENTORY_ITEM_ID]=stk.[INVENTORY_ITEM_ID] ")
                        .concat(" WHERE 1=1 ")
                        .concat(" AND stk.[TYPE]='IN'")
                        .concat(_where)
                        .concat(" GROUP BY stk.[INVENTORY_ITEM_NAME],stk.[INVENTORY_ITEM_ID],IM.[UNIT_NAME]")
                        .concat(" ORDER BY stk.[INVENTORY_ITEM_NAME] ASC ");
                Log.e("Query", qry);
                Cursor cur = db.rawQuery(qry, null);
                Log.e("CursorCount", "In" + String.valueOf(cur.getCount()));
                while (cur.moveToNext()) {
                    ClsInventoryStock getSet = new ClsInventoryStock();
                    getSet.setInventory_item_id(cur.getInt(cur.getColumnIndex("INVENTORY_ITEM_ID")));
                    getSet.setInventory_item_name(cur.getString(cur.getColumnIndex("INVENTORY_ITEM_NAME")));
                    getSet.setQty(cur.getDouble(cur.getColumnIndex("QUANTITY")));
                    getSet.setUnitname(cur.getString(cur.getColumnIndex("UNIT_NAME")));
                    Log.e("LOGS", "Unit--" + cur.getString(cur.getColumnIndex("UNIT_NAME")));
                    Inlist.add(getSet);
                }


                qry = "SELECT "
                        .concat("stk.[INVENTORY_ITEM_ID]")
                        .concat(",stk.[INVENTORY_ITEM_NAME]")
                        .concat(",SUM(stk.[QUANTITY]) AS [QUANTITY]")
                        .concat(",IM.[UNIT_NAME]")
                        .concat(" FROM [Inventory_stock_master] as stk")
                        .concat(" LEFT JOIN [INVENTORY_ITEM_MASTER] as IM ON IM.[INVENTORY_ITEM_ID]=stk.[INVENTORY_ITEM_ID] ")
                        .concat(" WHERE 1=1")
                        .concat(" AND stk.[TYPE]='OUT'")
                        .concat(_where)
                        .concat("GROUP BY stk.[INVENTORY_ITEM_NAME],stk. [INVENTORY_ITEM_ID],IM.[UNIT_NAME]")
                        .concat(" ORDER BY stk.[INVENTORY_ITEM_NAME] ASC ");
                Log.e("CursorCount", qry);
                cur = db.rawQuery(qry, null);
                Log.e("CursorCount", "Out" + String.valueOf(cur.getCount()));
                while (cur.moveToNext()) {
                    ClsInventoryStock getSet = new ClsInventoryStock();
                    getSet.setInventory_item_id(cur.getInt(cur.getColumnIndex("INVENTORY_ITEM_ID")));
                    getSet.setInventory_item_name(cur.getString(cur.getColumnIndex("INVENTORY_ITEM_NAME")));
                    getSet.setQty(cur.getDouble(cur.getColumnIndex("QUANTITY")));
                    getSet.setUnitname(cur.getString(cur.getColumnIndex("UNIT_NAME")));

                    Log.e("LOGS", "Out_ID--" + String.valueOf(cur.getInt(cur.getColumnIndex("INVENTORY_ITEM_ID"))));
                    Log.e("LOGS", "Out_Qty--" + String.valueOf(cur.getDouble(cur.getColumnIndex("QUANTITY"))));
                    Outlist.add(getSet);

                }
                for (ClsInventoryItem objitem : Items_list) {
                    ClsInventoryStock objInventoryStock = new ClsInventoryStock();
                    objInventoryStock.setInventory_item_id(objitem.getInventory_item_id());
                    objInventoryStock.setInventory_item_name(objitem.getInventory_item_name());
                    objInventoryStock.setMin_qty(objitem.getMin_qty());
                    objInventoryStock.setMax_qty(objitem.getMax_stock());
                    objInventoryStock.setUnitname(objitem.getUnit_name());
                    objInventoryStock.setInqty(0.0);
                    objInventoryStock.setOutqty(0.0);

//                        ClsInventoryStock clsInventoryStock=new ClsInventoryStock();
//                    Log.e("checkId", String.valueOf(clsInventoryStock.getInventory_item_id()));
//                    Log.e("checkId", String.valueOf(objInventoryStock.getInventory_item_id()));


                    for (ClsInventoryStock objIn : Inlist) {

                        if (objitem.getInventory_item_id() == objIn.getInventory_item_id()) {
                            Log.e("Testing", "Working..");
                            Log.e("Testing", String.valueOf(objIn.getQty()));
                            objInventoryStock.setInqty(objIn.getQty());
                            break;
                        }
                    }

                    for (ClsInventoryStock objOut : Outlist) {
                        if (objitem.getInventory_item_id() == objOut.getInventory_item_id()) {
                            objInventoryStock.setOutqty(objOut.getQty());
                            break;
                        }

                    }
                    Resultlist.add(objInventoryStock);
                }


                Log.e("CursorCount" + "ListSize", String.valueOf(Resultlist.size()));

            } catch (Exception e) {
                Log.e("ClsInventoryStock", "GetStock--->>" + e.getMessage());
                e.getMessage();
            }


        }
        return Resultlist;
    }





    @SuppressLint("WrongConstant")
    public static int Delete(ClsInventoryStock objStock) {
        int result = 0;
        try {

//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            }


            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "DELETE FROM [Inventory_stock_master] WHERE [STOCK_ID] = "

                    .concat(String.valueOf(objStock.getStock_id()))
                    .concat(";");
            Log.e("IDDELETE", "DELqRY-" + strSql);
            SQLiteStatement statement = db.compileStatement(strSql);

            result = statement.executeUpdateDelete();

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("Delete", "Delete---" + e.getMessage());
            e.getMessage();
        }
        return result;


    }

    @SuppressLint("WrongConstant")
    public ClsInventoryStock getObject(int orderid) {
        ClsInventoryStock Obj = new ClsInventoryStock();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);


            String sqlStr = "SELECT "
                    .concat("[STOCK_ID]")
                    .concat(",[ORDER_ID]")
                    .concat(",[VENDOR_ID]")
                    .concat(",[INVENTORY_ITEM_ID]")
                    .concat(",[INVENTORY_ITEM_NAME]")
                    .concat(",[AMOUNT]")
                    .concat(",[QUANTITY]")
                    .concat(",[ENTRY_DATE]")
                    .concat(",[TRANSACTION_DATE]")
                    .concat(",[REMARK]")
                    .concat(" FROM [Inventory_stock_master] WHERE 1=1 ")
                    .concat(" AND [STOCK_ID] = ")
                    .concat(String.valueOf(orderid));

            Log.e("GetObj", "Qurty-->>>>>" + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            Log.e("GetObj", "curCount>>>>>" + cur.getCount());
            while (cur.moveToNext()) {

                Obj.setStock_id(cur.getInt(cur.getColumnIndex("STOCK_ID")));
                Obj.setOrder_id(cur.getInt(cur.getColumnIndex("ORDER_ID")));
                Obj.setVendror_id(cur.getInt(cur.getColumnIndex("VENDOR_ID")));
                Obj.setInventory_item_id(cur.getInt(cur.getColumnIndex("INVENTORY_ITEM_ID")));
                Obj.setInventory_item_name(cur.getString(cur.getColumnIndex("INVENTORY_ITEM_NAME")));
                Obj.setAmount(cur.getDouble(cur.getColumnIndex("AMOUNT")));
                Obj.setQty(cur.getDouble(cur.getColumnIndex("QUANTITY")));
                Obj.setRemark(cur.getString(cur.getColumnIndex("REMARK")));
                Obj.setTrasaction_date(cur.getString(cur.getColumnIndex("TRANSACTION_DATE")));
                Obj.setEntry_date(cur.getString(cur.getColumnIndex("ENTRY_DATE")));

            }



        } catch (Exception e) {
            Log.e("OBJ", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }
        return Obj;
    }


    @SuppressLint("WrongConstant")
    public static int Update(ClsInventoryStock objStock) {
        int result=0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "UPDATE [Inventory_stock_master] SET "
                    .concat("[ORDER_ID] = ")

                    .concat(String.valueOf(objStock.getOrder_id()))

                    .concat(" ,[VENDOR_ID] = ")
                    .concat(String.valueOf(objStock.getVendror_id()))


                    .concat(" ,[INVENTORY_ITEM_ID] = ")
                    .concat("'")
                    .concat(String.valueOf(objStock.getInventory_item_id()))
                    .concat("'")

                    .concat(" ,[INVENTORY_ITEM_NAME] = ")
                    .concat("'")
                    .concat(objStock.getInventory_item_name().trim()
                            .replace("'","''"))
                    .concat("'")

                    .concat(" ,[AMOUNT] = ")
                    .concat(String.valueOf(objStock.getAmount()))


                    .concat(" ,[QUANTITY] = ")
                    .concat(String.valueOf(objStock.getQty()))


                    .concat(" ,[ENTRY_DATE] = ")
                    .concat("'")
                    .concat(objStock.getEntry_date())
                    .concat("'")

                    .concat(" ,[TRANSACTION_DATE] = ")
                    .concat("'")
                    .concat(objStock.getTrasaction_date())
                    .concat("'")

                    .concat(" ,[REMARK] = ")
                    .concat("'")
                    .concat(objStock.getRemark().trim()
                            .replace("'","''"))
                    .concat("'")

                    .concat(" WHERE [STOCK_ID] = ")
                    .concat(String.valueOf(objStock.getStock_id()))

                    .concat(";");
            Log.e("Update", strSql);
            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();
            //db.execSQL(strSql);
            db.close();

        }catch (Exception e)
        {
            Log.e("Update", "Update--->>>" + e.getMessage());
        }
        return result;
    }

}
