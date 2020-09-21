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
 * Created by Desktop on 3/10/2018.
 */

public class ClsItem {
    int item_id;
    String item_name;

    Double Price = 0.0;

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public ClsCategory getObjCategory() {
        return ObjCategory;
    }

    public void setObjCategory(ClsCategory objCategory) {
        ObjCategory = objCategory;
    }

    ClsCategory ObjCategory;
    String category_name;

    public Integer getSort_no() {

        return sort_no;
    }

    public void setSort_no(Integer sort_no) {
        this.sort_no = sort_no;
    }

    Integer sort_no;
    static Context context;
    String remark;
    int category_id;
    String active;

    public ClsItem(Context ctx) {

        context = ctx;
    }

    public ClsItem() {
    }


    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    String food_type;


    private static SQLiteDatabase db;
    AppCompatActivity activity;


    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @SuppressLint("WrongConstant")
    public static int Insert(ClsCategory objCategory, ClsItem ObjItem) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String qry = ("INSERT INTO [ITEM_MASTER] ([ITEM_NAME]," +
                    "[CATEGORY_ID],[CATEGORY_NAME],[FOOD_TYPE]," +
                    "[ACTIVE],[REMARK],[PRICE],[SORT_NO]) VALUES ('")

                    .concat(ObjItem.getItem_name().trim()
                            .replace("'","''"))
                    .concat("'")
                    .concat(",")


                    .concat(String.valueOf(objCategory.getCat_id()))
                    .concat(",")


                    .concat("'")
                    .concat(objCategory.getCat_name().trim()
                            .replace("'","''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjItem.getFood_type().trim()
                            .replace("'","''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjItem.getActive())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjItem.getRemark().trim()
                            .replace("'","''"))
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(ObjItem.getPrice()))
                    .concat(",")

                    .concat(String.valueOf(ObjItem.getSort_no()))

                    .concat(");");

            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.e("INSERT", "Items--->>" + qry);

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsItem", "Insert>>>>>>>>" + e.getMessage());
            e.getMessage();
        }
        return result;

        //ITEM_ID
    }

    @SuppressLint("WrongConstant")
    public boolean checkExists(String _where) {
        boolean result = false;

        try {


            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = "SELECT 1 FROM [ITEM_MASTER] WHERE 1=1 "
                    .concat(_where)
                    .concat(";");
            Log.e("exists", "existsChqQry - " + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            if (cur != null && cur.getCount() != 0) {
                result = true;
            }
            db.close();
        } catch (Exception e) {
            Log.e("ClsItem", "ClsItemMaster>>>>CheckExist" + e.getMessage());
            e.getMessage();
        }
        return result;


    }

    @SuppressLint("WrongConstant")
    public static int Delete(ClsItem ObjItem) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "DELETE FROM [ITEM_MASTER] WHERE [ITEM_ID] = "
                    .concat(String.valueOf(ObjItem.getItem_id()))
                    .concat(";");
            Log.e("DELETE", "DELqRY-" + strSql);
            SQLiteStatement statement = db.compileStatement(strSql);

            result = statement.executeUpdateDelete();

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsItem", "Delete" + e.getMessage());
            e.getMessage();
        }
        return result;


    }

    @SuppressLint("WrongConstant")
    public ClsItem getObject(ClsItem ObjItem) {
        ClsItem Obj = new ClsItem();

        try {


            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = "SELECT [ITEM_ID],[ITEM_NAME],[CATEGORY_ID],[CATEGORY_NAME],[FOOD_TYPE],[SORT_NO],[PRICE],[ACTIVE],[REMARK] FROM [ITEM_MASTER] WHERE 1=1 AND [ITEM_ID] = "
                    .concat(String.valueOf(ObjItem.getItem_id()))

                    .concat(";");
            Log.e("Update", ">>>>>>>>>" + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            Log.e("Update", ">>>>>>>>>" + cur.getCount());
            while (cur.moveToNext()) {

                Obj.setItem_id(cur.getInt(cur.getColumnIndex("ITEM_ID")));
                Obj.setItem_name(cur.getString(cur.getColumnIndex("ITEM_NAME")));
                Obj.setCategory_id(cur.getInt(cur.getColumnIndex("CATEGORY_ID")));
                Obj.setCategory_name(cur.getString(cur.getColumnIndex("CATEGORY_NAME")));
                Obj.setFood_type(cur.getString(cur.getColumnIndex("FOOD_TYPE")));
                Obj.setActive(cur.getString(cur.getColumnIndex("ACTIVE")));
                Obj.setSort_no(cur.getInt(cur.getColumnIndex("SORT_NO")));
                Obj.setRemark(cur.getString(cur.getColumnIndex("REMARK")));
                Obj.setPrice(cur.getDouble(cur.getColumnIndex("PRICE")));

            }

            db.close();

        } catch (Exception e) {
            Log.e("ClsItem", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }
        return Obj;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsItem> getList(String _where) {

        List<ClsItem> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);


            Cursor cur = db.rawQuery("SELECT "
                    .concat("[ITEM_ID]")
                    .concat(",[ITEM_NAME]")
                    .concat(",[CATEGORY_ID]")
                    .concat(",[CATEGORY_NAME]")
                    .concat(",[FOOD_TYPE]")
                    .concat(",[ACTIVE]")
                    .concat(",[SORT_NO]")
                    .concat(",[PRICE]")
                    .concat(",[REMARK]")
                    .concat(" FROM [ITEM_MASTER] WHERE 1=1 ")
                    .concat(_where)
                    .concat(" ORDER BY [SORT_NO] ASC "), null);

            while (cur.moveToNext()) {
                ClsItem getSet = new ClsItem();
                getSet.setItem_id(cur.getInt(cur.getColumnIndex("ITEM_ID")));
                getSet.setItem_name(cur.getString(cur.getColumnIndex("ITEM_NAME")));
                getSet.setCategory_id(cur.getInt(cur.getColumnIndex("CATEGORY_ID")));
                getSet.setCategory_name(cur.getString(cur.getColumnIndex("CATEGORY_NAME")));
                getSet.setFood_type(cur.getString(cur.getColumnIndex("FOOD_TYPE")));
                getSet.setActive(cur.getString(cur.getColumnIndex("ACTIVE")));
                getSet.setSort_no(cur.getInt(cur.getColumnIndex("SORT_NO")));
                getSet.setRemark(cur.getString(cur.getColumnIndex("REMARK")));
                getSet.setPrice(cur.getDouble(cur.getColumnIndex("PRICE")));
                list.add(getSet);
            }
        } catch (Exception e) {
            Log.e("ClsItem", "ClsItem >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsItem> RetailViewGetList(String where) {
        List<ClsItem> list = new ArrayList<>();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String query = "SELECT"
                    .concat(" itemaster.[ITEM_ID]")
                    .concat(",itemaster.[ITEM_NAME]")
                    .concat(",itemaster.[CATEGORY_ID]")
                    .concat(",itemaster.[CATEGORY_NAME]")
                    .concat(",itemaster.[FOOD_TYPE]")
                    .concat(",itemaster.[ACTIVE]")
                    .concat(",itemaster.[SORT_NO]")
                    .concat(",itemaster.[PRICE]")
                    .concat(",itemaster.[REMARK]")
                    .concat(",catmaster.[SORT_NO]")
                    .concat(" FROM [ITEM_MASTER] as itemaster")
                    .concat(" INNER JOIN [CATEGORY_MASTER] as catmaster ON catmaster.[CATEGORY_ID]=itemaster.[CATEGORY_ID] ")
                    .concat(" WHERE 1=1 ")
                    .concat(where)
                    .concat(" ORDER BY catmaster.[SORT_NO] ASC,itemaster.[SORT_NO] ASC ");

            Log.e("query",query);
            Cursor cur= db.rawQuery(query,null);


//            ORDER BY column1 DESC, column2


            while (cur.moveToNext())
            {
                ClsItem Obj=new ClsItem();
                Obj.setItem_id(cur.getInt(cur.getColumnIndex("ITEM_ID")));
                Obj.setItem_name(cur.getString(cur.getColumnIndex("ITEM_NAME")));
                Obj.setCategory_id(cur.getInt(cur.getColumnIndex("CATEGORY_ID")));
                Obj.setCategory_name(cur.getString(cur.getColumnIndex("CATEGORY_NAME")));
                Obj.setFood_type(cur.getString(cur.getColumnIndex("FOOD_TYPE")));
                Obj.setActive(cur.getString(cur.getColumnIndex("ACTIVE")));
                Obj.setSort_no(cur.getInt(cur.getColumnIndex("SORT_NO")));
                Obj.setPrice(cur.getDouble(cur.getColumnIndex("PRICE")));
                Obj.setRemark(cur.getString(cur.getColumnIndex("REMARK")));
                list.add(Obj);
            }


        } catch (Exception e) {
            Log.e("ClsItem", "RetailviewGetList--->>" + e.getMessage());
        }


        return list;
    }

    @SuppressLint("WrongConstant")
    public static int Update(ClsItem objItem) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "UPDATE [ITEM_MASTER] SET "
                    .concat("[ITEM_NAME] = ")
                    .concat("'")
                    .concat(objItem.getItem_name().trim()
                            .replace("'","''"))
                    .concat("'")

                    .concat(" ,[CATEGORY_ID] = ")
                    .concat(String.valueOf(objItem.getCategory_id()))

                    .concat(" ,[CATEGORY_NAME] = ")
                    .concat("'")
                    .concat(objItem.getCategory_name().trim()
                            .replace("'","''"))
                    .concat("'")

                    .concat(" ,[FOOD_TYPE] = ")
                    .concat("'")
                    .concat(objItem.getFood_type().trim()
                            .replace("'","''"))
                    .concat("'")

                    .concat(" ,[PRICE] = ")

                    .concat(String.valueOf(objItem.getPrice()))


                    .concat(" ,[SORT_NO] = ")
                    .concat("'")
                    .concat(String.valueOf(objItem.getSort_no()))
                    .concat("'")

                    .concat(" ,[ACTIVE] = ")
                    .concat("'")
                    .concat(objItem.getActive())
                    .concat("'")


                    .concat(" ,[REMARK] = ")
                    .concat("'")
                    .concat(objItem.getRemark().trim()
                            .replace("'","''"))
                    .concat("'")


                    .concat(" WHERE [ITEM_ID] = ")

                    .concat(String.valueOf(objItem.getItem_id()))

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

    @Override
    public String toString() {
        return "ClsItem{" +
                "item_id=" + item_id +
                ", item_name='" + item_name + '\'' +
                ", Price=" + Price +
                ", ObjCategory=" + ObjCategory +
                ", category_name='" + category_name + '\'' +
                ", sort_no=" + sort_no +
                ", remark='" + remark + '\'' +
                ", category_id=" + category_id +
                ", active='" + active + '\'' +
                ", food_type='" + food_type + '\'' +
                ", activity=" + activity +
                '}';
    }
}
