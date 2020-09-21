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
 * Created by Desktop on 3/13/2018.
 */

public class ClsUnit {
    int unit_id;
    String unit_name;

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

    public ClsUnit() {

    }

    public ClsUnit(Context ctx) {

        context = ctx;
    }

    public int getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(int unit_id) {
        this.unit_id = unit_id;
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
    public static int Insert(ClsUnit ObjUnit, io.requery.android.database.sqlite.SQLiteDatabase
            db) {
        int  result = 0;
        try {

//            db =context .openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String qry = "INSERT INTO [UNIT_MASTER] ([UNIT_NAME],[ACTIVE],[REMARK],[SORT_NO]) VALUES ('"

                    .concat(ObjUnit.getUnit_name().trim()
                            .replace("'","''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjUnit.getActive())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjUnit.getRemark().trim()
                            .replace("'","''"))
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(ObjUnit.getSort_no()))

                    .concat(");");

            io.requery.android.database.sqlite.SQLiteStatement statement =
                    db.compileStatement(qry);
            result = statement.executeUpdateDelete();

            Log.e("INSERT","Unit--->>"+ qry);

//            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsUnit", "Insert>>>>>>>>" + e.getMessage());
            e.getMessage();
        }
        return result;
    }

    @SuppressLint("WrongConstant")
    public boolean checkExists(String _where, io.requery.android.database.sqlite.SQLiteDatabase db) {
        boolean result = false;

        try {


//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = "SELECT 1 FROM [UNIT_MASTER] WHERE 1=1 "
                    .concat(_where)
                    .concat(";");
            Log.e("exists", "existsChqQry - " + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            if (cur != null && cur.getCount() != 0) {
                result = true;
            }
//            db.close();
        } catch (Exception e) {
            Log.e("ClsUnit", "ClsUnitMaster>>>>CheckExist" + e.getMessage());
            e.getMessage();
            db.close();
        }
        return result;


    }

    @SuppressLint("WrongConstant")
    public static List<ClsUnit> getList(String _where) {
        List<ClsUnit> list = new ArrayList<>();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            Cursor cur = db.rawQuery("SELECT "
                    .concat("[UNIT_ID]")
                    .concat(",[UNIT_NAME]")
                    .concat(",[ACTIVE]")
                    .concat(",[SORT_NO]")
                    .concat(",[REMARK]")
                    .concat(" FROM [UNIT_MASTER] WHERE 1=1 ")
                    .concat(_where)
                    .concat(" ORDER BY [SORT_NO] ASC "), null);


            while (cur.moveToNext()) {
                ClsUnit getSet = new ClsUnit();
                getSet.setUnit_id(cur.getInt(cur.getColumnIndex("UNIT_ID")));
                getSet.setUnit_name(cur.getString(cur.getColumnIndex("UNIT_NAME")));
                getSet.setSort_no(cur.getInt(cur.getColumnIndex("SORT_NO")));
                getSet.setActive(cur.getString(cur.getColumnIndex("ACTIVE")));
                getSet.setRemark(cur.getString(cur.getColumnIndex("REMARK")));

                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("ClsUnit", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }



    @SuppressLint("WrongConstant")
    public static List<ClsUnit> getDialogList(String _where) {
        List<ClsUnit> list = new ArrayList<>();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            Cursor cur = db.rawQuery("SELECT "
                    .concat("[UNIT_ID]")
                    .concat(",[UNIT_NAME]")
                    .concat(",[ACTIVE]")
                    .concat(",[SORT_NO]")
                    .concat(",[REMARK]")
                    .concat(" FROM [UNIT_MASTER] WHERE 1=1 ")
                    .concat(_where), null);


            while (cur.moveToNext()) {
                ClsUnit getSet = new ClsUnit();
                getSet.setUnit_id(cur.getInt(cur.getColumnIndex("UNIT_ID")));
                getSet.setUnit_name(cur.getString(cur.getColumnIndex("UNIT_NAME")));
                getSet.setSort_no(cur.getInt(cur.getColumnIndex("SORT_NO")));
                getSet.setActive(cur.getString(cur.getColumnIndex("ACTIVE")));
                getSet.setRemark(cur.getString(cur.getColumnIndex("REMARK")));

                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("ClsUnit", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }



    @SuppressLint("WrongConstant")
    public static int Delete(ClsUnit ObjUnit) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "DELETE FROM [UNIT_MASTER] WHERE [UNIT_ID] = "
                    .concat( String.valueOf(ObjUnit.getUnit_id()))
                    .concat(";");
            Log.e("DELETE", "DELqRY-" + strSql);
            SQLiteStatement statement = db.compileStatement(strSql);

            result = statement.executeUpdateDelete();

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsUnit", "Delete" + e.getMessage());
            e.getMessage();
        }
        return result;


    }

    @SuppressLint("WrongConstant")
    public ClsUnit getObject(ClsUnit objUnit) {
        ClsUnit Obj = new ClsUnit();

        try {


            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = "SELECT [UNIT_ID],[UNIT_NAME],[SORT_NO],[ACTIVE],[REMARK] FROM [UNIT_MASTER] WHERE 1=1 AND [UNIT_ID] = "
                    .concat(String.valueOf(objUnit.getUnit_id()))
                    .concat(";");

            Log.e("Update", ">>>>>>>>>" + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            Log.e("Update", ">>>>>>>>>" + cur.getCount());
            while (cur.moveToNext()) {

                Obj.setUnit_id(cur.getInt(cur.getColumnIndex("UNIT_ID")));
                Obj.setUnit_name(cur.getString(cur.getColumnIndex("UNIT_NAME")));
                Obj.setActive(cur.getString(cur.getColumnIndex("ACTIVE")));
                Obj.setSort_no(cur.getInt(cur.getColumnIndex("SORT_NO")));
                Obj.setRemark(cur.getString(cur.getColumnIndex("REMARK")));

            }

            db.close();

        } catch (Exception e) {
            Log.e("ClsUnit", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }
        return Obj;
    }

    @SuppressLint("WrongConstant")
    public static int Update(ClsUnit objUnit) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "UPDATE [UNIT_MASTER] SET "
                    .concat("[UNIT_NAME] = ")
                    .concat("'")
                    .concat(objUnit.getUnit_name().trim()
                            .replace("'","''"))
                    .concat("'")

                    .concat(" ,[SORT_NO] = ")
                    .concat("'")
                    .concat(String.valueOf(objUnit.getSort_no()))
                    .concat("'")

                    .concat(" ,[ACTIVE] = ")
                    .concat("'")
                    .concat(objUnit.getActive())
                    .concat("'")



                    .concat(" ,[REMARK] = ")
                    .concat("'")
                    .concat(objUnit.getRemark().trim()
                            .replace("'","''"))
                    .concat("'")



                    .concat(" WHERE [UNIT_ID] = ")

                    .concat(String.valueOf(objUnit.getUnit_id()))

                    .concat(";");
            Log.e("Update", strSql);
            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();
            //db.execSQL(strSql);
            db.close();

        }catch (Exception e)
        {
            Log.e("ClsUnit", "Update" + e.getMessage());
        }
        return result;
    }

}
