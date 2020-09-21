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
 * Created by Desktop on 3/9/2018.
 */

public class ClsCategory {

    int cat_id;
    String cat_name;

    public Integer getSort_no() {
        return sort_no;
    }

    public void setSort_no(Integer sort_no) {
        this.sort_no = sort_no;
    }

    Integer sort_no;
    String remark;
    String active;
    static Context context;
    AppCompatActivity activity;
    private static SQLiteDatabase db;


    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public ClsCategory() {

    }

        public ClsCategory(Context ctx) {

            context = ctx;
        }
    public int getCat_id() {
        return cat_id;
    }
    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }
    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    @SuppressLint("WrongConstant")
    public static int Insert(ClsCategory ObjCategory) {
        int  result = 0;
        try {

            db =context .openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String qry = "INSERT INTO [CATEGORY_MASTER] ([CATEGORY_NAME],[ACTIVE],[REMARK],[SORT_NO]) VALUES ('"

                    .concat(ObjCategory.getCat_name().trim()
                            .replace("'","''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjCategory.getActive())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjCategory.getRemark().trim()
                            .replace("'","''"))
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(ObjCategory.getSort_no()))

                    .concat(");");

            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.e("INSERT","Category--->>"+ qry);

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsCategory", "Insert>>>>>>>>" + e.getMessage());
            e.getMessage();
        }
        return result;
    }



    @SuppressLint("WrongConstant")
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
            Log.e("ClsCategory", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public boolean checkExists(String _where) {
        boolean result = false;

        try {


            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = "SELECT 1 FROM [CATEGORY_MASTER] WHERE 1=1 "
                    .concat(_where)
                    .concat(";");
            Log.e("exists", "existsChqQry - " + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            if (cur != null && cur.getCount() != 0) {
                result = true;
            }
            db.close();
        } catch (Exception e) {
            Log.e("ClsCategory", "ClsCategoryMaster>>>>CheckExist" + e.getMessage());
            e.getMessage();
        }
        return result;


    }


    @SuppressLint("WrongConstant")
    public static int Delete(ClsCategory ObjcategoryMaster) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "DELETE FROM [CATEGORY_MASTER] WHERE [CATEGORY_ID] = "
                    .concat( String.valueOf(ObjcategoryMaster.getCat_id()))
                    .concat(";");
            Log.e("DELETE", "DELqRY-" + strSql);
            SQLiteStatement statement = db.compileStatement(strSql);

            result = statement.executeUpdateDelete();

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsCategory", "Delete" + e.getMessage());
            e.getMessage();
        }
        return result;


    }


    @SuppressLint("WrongConstant")
        public ClsCategory getObject(ClsCategory ObjCategory) {
        ClsCategory Obj = new ClsCategory();

        try {


            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = "SELECT [CATEGORY_ID],[CATEGORY_NAME],[SORT_NO],[ACTIVE],[REMARK] FROM [CATEGORY_MASTER] WHERE 1=1 AND [CATEGORY_ID] = "
                    .concat(String.valueOf(ObjCategory.getCat_id()))

                    .concat(";");
            Log.e("Update", ">>>>>>>>>" + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            Log.e("Update", ">>>>>>>>>" + cur.getCount());
            while (cur.moveToNext()) {

                Obj.setCat_id(cur.getInt(cur.getColumnIndex("CATEGORY_ID")));
                Obj.setCat_name(cur.getString(cur.getColumnIndex("CATEGORY_NAME")));
                Obj.setActive(cur.getString(cur.getColumnIndex("ACTIVE")));
                Obj.setSort_no(cur.getInt(cur.getColumnIndex("SORT_NO")));
                Obj.setRemark(cur.getString(cur.getColumnIndex("REMARK")));

            }

            db.close();

        } catch (Exception e) {
            Log.e("ClsCategory", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }
        return Obj;
    }


    @SuppressLint("WrongConstant")
    public static int Update(ClsCategory objCategory) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "UPDATE [CATEGORY_MASTER] SET "
                    .concat("[CATEGORY_NAME] = ")
                    .concat("'")
                    .concat(objCategory.getCat_name().trim()
                            .replace("'","''"))
                    .concat("'")

                    .concat(" ,[SORT_NO] = ")

                    .concat(String.valueOf(objCategory.getSort_no()))
                    .concat(" ,[ACTIVE] = ")
                    .concat("'")
                    .concat(objCategory.getActive())
                    .concat("'")



                    .concat(" ,[REMARK] = ")
                    .concat("'")
                    .concat(objCategory.getRemark().trim()
                            .replace("'","''"))
                    .concat("'")



                    .concat(" WHERE [CATEGORY_ID] = ")

                    .concat(String.valueOf(objCategory.getCat_id()))

                    .concat(";");
            Log.e("Update", strSql);
            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();
            //db.execSQL(strSql);
            db.close();

        }catch (Exception e)
        {
            Log.e("ClsCategory", "Update" + e.getMessage());
        }
        return result;
    }


}
