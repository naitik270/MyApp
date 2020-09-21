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

import androidx.annotation.NonNull;

public class ClsInventoryLayer {
    String layerValue;

    public String getLayerValue() {
        return layerValue;
    }

    public void setLayerValue(String layerValue) {
        this.layerValue = layerValue;
    }

    int INVENTORYLAYER_ID;
    String InventoryLayerName;
    int InventoryLayerCategory;
    String InventoryLayerCategoryName;
    int DisplayOrder;
    String Remark;
    String Active;
    static Context context;
    private static SQLiteDatabase db;

    public ClsInventoryLayer(Context context) {
        ClsInventoryLayer.context = context;
    }

    public ClsInventoryLayer() {

    }

    public ClsInventoryLayer(int InventoryLayerCategory, String InventoryLayerName) {
        this.InventoryLayerCategory = InventoryLayerCategory;
        this.InventoryLayerName = InventoryLayerName;
    }

    public String getInventoryLayerCategoryName() {
        return InventoryLayerCategoryName;
    }

    public void setInventoryLayerCategoryName(String inventoryLayerCategoryName) {
        InventoryLayerCategoryName = inventoryLayerCategoryName;
    }

    public int getINVENTORYLAYER_ID() {
        return INVENTORYLAYER_ID;
    }

    public void setINVENTORYLAYER_ID(int INVENTORYLAYER_ID) {
        this.INVENTORYLAYER_ID = INVENTORYLAYER_ID;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String active) {
        Active = active;
    }

    public String getInventoryLayerName() {
        return InventoryLayerName;
    }

    public void setInventoryLayerName(String inventoryLayerName) {
        this.InventoryLayerName = inventoryLayerName;
    }

    public int getInventoryLayerCategory() {
        return InventoryLayerCategory;
    }

    public void setInventoryLayerCategory(int inventoryLayerCategory) {
        this.InventoryLayerCategory = inventoryLayerCategory;
    }

    public int getDisplayOrder() {
        return DisplayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        DisplayOrder = displayOrder;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }


    @SuppressLint("WrongConstant")
    public static int Insert(ClsInventoryLayer objInventoryLayer, Context context) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String qry = "INSERT INTO [tbl_InventoryLayer] ([LAYER_NAME],[PARENT_ID],[SELECTED_LAYER_NAME],[DISPLAY_ORDER],[REMARK],[ACTIVE]) VALUES ('"

                    .concat(objInventoryLayer.getInventoryLayerName().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(objInventoryLayer.getInventoryLayerCategory()))
                    .concat(",")

                    .concat("'")
                    .concat(objInventoryLayer.getInventoryLayerCategoryName().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(objInventoryLayer.getDisplayOrder()))
                    .concat(",")

                    .concat("'")
                    .concat(objInventoryLayer.getRemark().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objInventoryLayer.getActive())
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

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String sqlStr = "SELECT 1 FROM [tbl_InventoryLayer] WHERE 1=1 "
                    .concat(_where)
                    .concat(";");
            Log.e("exists", "existsChqQry - " + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            if (cur != null && cur.getCount() != 0) {
                result = true;
            }
            db.close();
        } catch (Exception e) {
            Log.e("ClsUnit", "ClsUnitMaster>>>>CheckExist" + e.getMessage());
            e.getMessage();
        }
        return result;


    }


    @SuppressLint("WrongConstant")
    public static List<ClsInventoryLayer> getAllList(Context context, String _where) {

        List<ClsInventoryLayer> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            String qry = "SELECT "
                    .concat("[INVENTORYLAYER_ID]")
                    .concat(",[LAYER_NAME]")
                    .concat(",[PARENT_ID]")
                    .concat(",[SELECTED_LAYER_NAME]")
                    .concat(",[DISPLAY_ORDER]")
                    .concat(",[REMARK]")
                    .concat(",[ACTIVE]")
                    .concat(" FROM [tbl_InventoryLayer] WHERE 1=1 ")
                    .concat(_where)
                    .concat(" ORDER BY [LAYER_NAME] ASC ");

            Log.e("qry", "ClsInventoryLayer >>>>InventoryLayerGetList" + qry);

            Cursor cur = db.rawQuery(qry, null);
            Log.e("cur", "ClsInventoryLayer >>>>count " + cur.getCount());

            while (cur.moveToNext()) {
                ClsInventoryLayer getSet = new ClsInventoryLayer();
                getSet.setINVENTORYLAYER_ID(cur.getInt(cur.getColumnIndex("INVENTORYLAYER_ID")));
                getSet.setInventoryLayerName(cur.getString(cur.getColumnIndex("LAYER_NAME")));
                getSet.setInventoryLayerCategory(cur.getInt(cur.getColumnIndex("PARENT_ID")));
                getSet.setInventoryLayerCategoryName(cur.getString(cur.getColumnIndex("SELECTED_LAYER_NAME")));
                getSet.setDisplayOrder(cur.getInt(cur.getColumnIndex("DISPLAY_ORDER")));
                getSet.setRemark(cur.getString(cur.getColumnIndex("REMARK")));
                getSet.setActive(cur.getString(cur.getColumnIndex("ACTIVE")));

                list.add(getSet);
            }

            db.close();

        } catch (Exception e) {
            Log.e("ClsItem", "ClsItem >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsInventoryLayer> getAllListInventoryLayer(Context context) {

        List<ClsInventoryLayer> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            String qry = "SELECT "
                    .concat("[INVENTORYLAYER_ID]")
                    .concat(",[LAYER_NAME]")
                    .concat(" FROM [tbl_InventoryLayer] WHERE 1=1 ")
                    .concat(" ORDER BY [LAYER_NAME] ASC ");

            Log.e("qry", "ClsInventoryLayer >>>>InventoryLayerGetList" + qry);

            Cursor cur = db.rawQuery(qry, null);

            while (cur.moveToNext()) {
                ClsInventoryLayer getSet = new ClsInventoryLayer();
                getSet.setINVENTORYLAYER_ID(cur.getInt(cur.getColumnIndex("INVENTORYLAYER_ID")));
                getSet.setInventoryLayerName(cur.getString(cur.getColumnIndex("LAYER_NAME")));
                getSet.setLayerValue("");//cur.getString(cur.getColumnIndex("LAYER_NAME")));
                list.add(getSet);
            }

            db.close();
        } catch (Exception e) {
            Log.e("ClsItem", "ClsItem >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsInventoryLayer> getEDITItemLayers(Context context, int itemID) {

        List<ClsInventoryLayer> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            String qry = "SELECT "
                    .concat("[ITEMLAYER_ID]")
                    .concat(",[LAYER_NAME]")
                    .concat(",[LAYER_VALUE]")
                    .concat(" FROM [tbl_ItemLayer] WHERE 1=1 ")
                    .concat(" AND  [ITEM_ID] = " + itemID);

            Log.e("qry", "ClsInventoryLayer >>>>InventoryLayerGetList" + qry);

            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur count", "count >>>>count" + cur.getCount());
            while (cur.moveToNext()) {
                ClsInventoryLayer getSet = new ClsInventoryLayer();
                getSet.setINVENTORYLAYER_ID(cur.getInt(cur.getColumnIndex("ITEMLAYER_ID")));
                getSet.setInventoryLayerName(cur.getString(cur.getColumnIndex("LAYER_NAME")));
                getSet.setLayerValue(cur.getString(cur.getColumnIndex("LAYER_VALUE")));
                list.add(getSet);
            }

            db.close();
        } catch (Exception e) {
            Log.e("ClsItem", "ClsItem >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsInventoryLayer> getInventoryLayerList(Context context) {

        List<ClsInventoryLayer> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            String qry = "SELECT "
                    .concat("[INVENTORYLAYER_ID]")
                    .concat(",[LAYER_NAME]")
                    .concat(" FROM [tbl_InventoryLayer] WHERE 1=1 ")
                    .concat(" ORDER BY [LAYER_NAME] ASC ");

            Log.e("qry", "ClsInventoryLayer >>>>GetList" + qry);

            Cursor cur = db.rawQuery(qry, null);

            while (cur.moveToNext()) {
                ClsInventoryLayer getSet = new ClsInventoryLayer();
                getSet.setINVENTORYLAYER_ID(cur.getInt(cur.getColumnIndex("INVENTORYLAYER_ID")));
                getSet.setInventoryLayerName(cur.getString(cur.getColumnIndex("LAYER_NAME")));
                list.add(getSet);
            }

            db.close();

        } catch (Exception e) {
            Log.e("ClsItem", "ClsItem >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static ClsInventoryLayer QueryById(int Id, Context context) {
        ClsInventoryLayer Obj = new ClsInventoryLayer();
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String sqlStr = "SELECT [INVENTORYLAYER_ID],[LAYER_NAME],[PARENT_ID],[SELECTED_LAYER_NAME],[DISPLAY_ORDER],[REMARK],[ACTIVE] FROM [tbl_InventoryLayer] WHERE 1=1 AND [INVENTORYLAYER_ID] = "
                    .concat(String.valueOf(Id))
                    .concat(";");

            Log.e("Update", ">>>>>>>>>" + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            Log.e("Update", ">>>>>>>>>" + cur.getCount());
            while (cur.moveToNext()) {
                Obj.setINVENTORYLAYER_ID(cur.getInt(cur.getColumnIndex("INVENTORYLAYER_ID")));
                Obj.setInventoryLayerName(cur.getString(cur.getColumnIndex("LAYER_NAME")));
                Obj.setInventoryLayerCategory(cur.getInt(cur.getColumnIndex("PARENT_ID")));
                Obj.setInventoryLayerCategoryName(cur.getString(cur.getColumnIndex("SELECTED_LAYER_NAME")));
                Obj.setDisplayOrder(cur.getInt(cur.getColumnIndex("DISPLAY_ORDER")));
                Obj.setRemark(cur.getString(cur.getColumnIndex("REMARK")));
                Obj.setActive(cur.getString(cur.getColumnIndex("ACTIVE")));
            }

            db.close();

        } catch (Exception e) {
            Log.e("ClsCategory", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }
        return Obj;
    }

    @SuppressLint("WrongConstant")
    public static int Delete(ClsInventoryLayer objClsInventoryLayer, Context context) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String strSql = "DELETE FROM [tbl_InventoryLayer] WHERE [INVENTORYLAYER_ID] = "
                    .concat(String.valueOf(objClsInventoryLayer.getINVENTORYLAYER_ID()))
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
    public static int Update(ClsInventoryLayer ObjClsInventoryLayer, Context context) {
        int result = 0;

        Log.e("Update", "Query..");

        db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

        String strSql = "UPDATE [tbl_InventoryLayer] SET "

                .concat(" [LAYER_NAME] = ")
                .concat("'")
                .concat(ClsGlobal.getDbDateFormat(ObjClsInventoryLayer.getInventoryLayerName().trim()
                        .replace("'", "''")))
                .concat("'")

                .concat(", [PARENT_ID] = ")
                .concat(String.valueOf(ObjClsInventoryLayer.getInventoryLayerCategory()))

                .concat(", [SELECTED_LAYER_NAME] = ")
                .concat("'")
                .concat(ObjClsInventoryLayer.getInventoryLayerCategoryName().trim()
                        .replace("'", "''"))
                .concat("'")

                .concat(", [DISPLAY_ORDER] = ")
                .concat(String.valueOf(ObjClsInventoryLayer.getDisplayOrder()))

                .concat(", [REMARK] = ")
                .concat("'")
                .concat(ObjClsInventoryLayer.getRemark().trim()
                        .replace("'", "''"))
                .concat("'")

                .concat(", [ACTIVE] = ")
                .concat("'")
                .concat(String.valueOf(ObjClsInventoryLayer.getActive()))
                .concat("'")

                .concat(" WHERE [INVENTORYLAYER_ID] = ")
                .concat(String.valueOf(ObjClsInventoryLayer.getINVENTORYLAYER_ID()))
                .concat(";");

        Log.e("Update", strSql);
        SQLiteStatement statement = db.compileStatement(strSql);
        result = statement.executeUpdateDelete();

        db.close();
        return result;
    }

    @Override
    public String toString() {
        return "ClsInventoryLayer{" +
                "layerValue='" + layerValue + '\'' +
                ", INVENTORYLAYER_ID=" + INVENTORYLAYER_ID +
                ", InventoryLayerName='" + InventoryLayerName + '\'' +
                ", InventoryLayerCategory=" + InventoryLayerCategory +
                ", InventoryLayerCategoryName='" + InventoryLayerCategoryName + '\'' +
                ", DisplayOrder=" + DisplayOrder +
                ", Remark='" + Remark + '\'' +
                ", Active='" + Active + '\'' +
                '}';
    }
}
