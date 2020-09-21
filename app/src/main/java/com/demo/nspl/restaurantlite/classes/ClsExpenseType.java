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
 * Created by Desktop on 3/14/2018.
 */

public class ClsExpenseType {

    int expense_type_id;
    String expense_type_name = "";

    Integer sort_no = 0;
    String remark = "";
    String active = "";
    static Context context;
    AppCompatActivity activity;
    private static SQLiteDatabase db;


    public ClsExpenseType() {

    }

    public ClsExpenseType(Context ctx) {
        context = ctx;
    }

    public int getExpense_type_id() {
        return expense_type_id;
    }

    public void setExpense_type_id(int expense_type_id) {
        this.expense_type_id = expense_type_id;
    }

    public String getExpense_type_name() {
        return expense_type_name;
    }

    public void setExpense_type_name(String expense_type_name) {
        this.expense_type_name = expense_type_name;
    }

    public Integer getSort_no() {
        return sort_no;
    }

    public void setSort_no(Integer sort_no) {
        this.sort_no = sort_no;
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
    public static int Insert(ClsExpenseType ObjExpenseType) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String qry = "INSERT INTO [EXPENSE_TYPE_MASTER] ([EXPENSE_TYPE_NAME],[ACTIVE],[REMARK],[SORT_NO]) VALUES ('"

                    .concat(ObjExpenseType.getExpense_type_name().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjExpenseType.getActive())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjExpenseType.getRemark().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")


                    .concat(String.valueOf(ObjExpenseType.getSort_no()))


                    .concat(");");

            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.e("INSERT", "ExpenseType--->>" + qry);

            db.close();
            return result;
        } catch (Exception e) {
            db.close();
            e.getMessage();
        }
        return result;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsExpenseType> getList(String _where) {
        List<ClsExpenseType> list = new ArrayList<>();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String qry = "SELECT "
                    .concat("[EXPENSE_TYPE_ID]")
                    .concat(",[EXPENSE_TYPE_NAME]")
                    .concat(",[ACTIVE]")
                    .concat(",[SORT_NO]")
                    .concat(",[REMARK]")
                    .concat(" FROM [EXPENSE_TYPE_MASTER] WHERE 1=1 ")
                    .concat(_where);
//                    .concat(" ORDER BY [SORT_NO] ASC "), null);

            Cursor cur = db.rawQuery(qry, null);
            Log.e("ClsExpense", "getList" + qry);
            Log.e("ClsExpense", "cur" + cur.getCount());


            while (cur.moveToNext()) {
                ClsExpenseType getSet = new ClsExpenseType();
                getSet.setExpense_type_id(cur.getInt(cur.getColumnIndex("EXPENSE_TYPE_ID")));
                getSet.setExpense_type_name(cur.getString(cur.getColumnIndex("EXPENSE_TYPE_NAME")));
                getSet.setSort_no(cur.getInt(cur.getColumnIndex("SORT_NO")));
                getSet.setActive(cur.getString(cur.getColumnIndex("ACTIVE")));
                getSet.setRemark(cur.getString(cur.getColumnIndex("REMARK")));

                list.add(getSet);
            }
            db.close();
        } catch (Exception e) {
            db.close();
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public boolean checkExists(String _where) {
        boolean result = false;

        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = "SELECT 1 FROM [EXPENSE_TYPE_MASTER] WHERE 1=1 "//Salary
                    .concat(_where)
                    .concat(";");
            Log.e("exists", "existsChqQry - " + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            if (cur != null && cur.getCount() != 0) {
                result = true;
            }
            db.close();
        } catch (Exception e) {
            db.close();
            e.getMessage();
        }
        return result;


    }

    @SuppressLint("WrongConstant")
    public static int Delete(ClsExpenseType ObjExpenseType) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "DELETE FROM [EXPENSE_TYPE_MASTER] WHERE [EXPENSE_TYPE_ID] = "
                    .concat(String.valueOf(ObjExpenseType.getExpense_type_id()))
                    .concat(";");
            Log.e("DELETE", "DELqRY-" + strSql);
            SQLiteStatement statement = db.compileStatement(strSql);

            result = statement.executeUpdateDelete();

            db.close();
            return result;
        } catch (Exception e) {
            db.close();
            e.getMessage();
        }
        return result;


    }

    @SuppressLint("WrongConstant")
    public ClsExpenseType getObject(ClsExpenseType ObjExpenseType) {
        ClsExpenseType Obj = new ClsExpenseType();

        try {


            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = "SELECT [EXPENSE_TYPE_ID],[EXPENSE_TYPE_NAME],[SORT_NO],[ACTIVE],[REMARK] FROM [EXPENSE_TYPE_MASTER] WHERE 1=1 AND [EXPENSE_TYPE_ID] = "
                    .concat(String.valueOf(ObjExpenseType.getExpense_type_id()))

                    .concat(";");
            Log.e("Update", ">>>>>>>>>" + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            Log.e("Update", ">>>>>>>>>" + cur.getCount());
            while (cur.moveToNext()) {

                Obj.setExpense_type_id(cur.getInt(cur.getColumnIndex("EXPENSE_TYPE_ID")));
                Obj.setExpense_type_name(cur.getString(cur.getColumnIndex("EXPENSE_TYPE_NAME")));
                Obj.setActive(cur.getString(cur.getColumnIndex("ACTIVE")));
                Obj.setSort_no(cur.getInt(cur.getColumnIndex("SORT_NO")));
                Obj.setRemark(cur.getString(cur.getColumnIndex("REMARK")));

            }
            db.close();
        } catch (Exception e) {
            db.close();
            e.getMessage();
        }
        return Obj;
    }

    @SuppressLint("WrongConstant")
    public static int Update(ClsExpenseType ObjExpenseType) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "UPDATE [EXPENSE_TYPE_MASTER] SET "
                    .concat("[EXPENSE_TYPE_NAME] = ")
                    .concat("'")
                    .concat(ObjExpenseType.getExpense_type_name().trim()
                            .replace("'", "''"))
                    .concat("'")

                    .concat(" ,[SORT_NO] = ")

                    .concat(String.valueOf(ObjExpenseType.getSort_no()))


                    .concat(" ,[ACTIVE] = ")
                    .concat("'")
                    .concat(ObjExpenseType.getActive())
                    .concat("'")


                    .concat(" ,[REMARK] = ")
                    .concat("'")
                    .concat(ObjExpenseType.getRemark().trim()
                            .replace("'", "''"))
                    .concat("'")


                    .concat(" WHERE [EXPENSE_TYPE_ID] = ")

                    .concat(String.valueOf(ObjExpenseType.getExpense_type_id()))

                    .concat(";");
            Log.e("Update", strSql);
            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();
            //db.execSQL(strSql);
            db.close();

        } catch (Exception e) {
            db.close();
            e.getMessage();
        }
        return result;
    }


}
