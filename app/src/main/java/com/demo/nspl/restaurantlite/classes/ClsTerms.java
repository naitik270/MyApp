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

public class ClsTerms {

    private String mTerms = "", mActive = "", mInvoice_Types = "";
    private int mSort_No, mTerms_id;
    static Context mContext;

    private static SQLiteDatabase db;

    public ClsTerms() {

    }

    public ClsTerms(Context context) {
        mContext = context;
    }

    public String getmTerms() {
        return mTerms;
    }

    public String getmInvoice_Types() {
        return mInvoice_Types;
    }

    public void setmInvoice_Types(String mInvoice_Types) {
        this.mInvoice_Types = mInvoice_Types;
    }

    public int getmTerms_id() {
        return mTerms_id;
    }

    public void setmTerms_id(int mTerms_id) {
        this.mTerms_id = mTerms_id;
    }

    public void setmTerms(String mTerms) {
        this.mTerms = mTerms;
    }

    public String getmActive() {
        return mActive;
    }

    public void setmActive(String mActive) {
        this.mActive = mActive;
    }

    public int getmSort_No() {
        return mSort_No;
    }

    public void setmSort_No(int mSort_No) {
        this.mSort_No = mSort_No;
    }

    String TERM_TYPE = "";

    public String getTERM_TYPE() {
        return TERM_TYPE;
    }

    public void setTERM_TYPE(String TERM_TYPE) {
        this.TERM_TYPE = TERM_TYPE;
    }


    @SuppressLint("WrongConstant")
    public static int Insert(ClsTerms objTerms) {
        int result = 0;

        try {

            db = mContext.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String qry = "INSERT INTO [tbl_Terms] ([TERMS],[SORT_NO],[TERM_TYPE],[ACTIVE]) VALUES ('"

                    .concat(objTerms.getmTerms().trim()
                            .replace("'", "''"))
                    .concat("'")

                    .concat(",")
                    .concat(String.valueOf(objTerms.getmSort_No()))
                    .concat(",")

                    .concat("'")
                    .concat(objTerms.getTERM_TYPE())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objTerms.getmActive().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(");");

            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.e("INSERT", "Items--->>" + qry);

            db.close();
            return result;

        } catch (Exception e) {
            db.close();
            e.getMessage();
        }
        return result;

    }

    @SuppressLint("WrongConstant")
    public static List<ClsTerms> getList(String _where) {

        List<ClsTerms> list = new ArrayList<>();
        try {
            db = mContext.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);


            Cursor cur = db.rawQuery("SELECT "
                            .concat("[TERMS_ID]")
                            .concat(",[TERMS]")
                            .concat(",[SORT_NO]")
                            .concat(",[ACTIVE]")
                            .concat(",IFNULL([TERM_TYPE],'SALE INVOICE') AS [TERM_TYPE]")
                            .concat(" FROM [tbl_Terms] WHERE 1=1 ")
                            .concat(_where)
                    , null);

            while (cur.moveToNext()) {
                ClsTerms getSet = new ClsTerms();
                getSet.setmTerms_id(cur.getInt(cur.getColumnIndex("TERMS_ID")));
                getSet.setmTerms(cur.getString(cur.getColumnIndex("TERMS")));
                getSet.setmSort_No(cur.getInt(cur.getColumnIndex("SORT_NO")));
                getSet.setTERM_TYPE(cur.getString(cur.getColumnIndex("TERM_TYPE")));
                getSet.setmActive(cur.getString(cur.getColumnIndex("ACTIVE")));
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
    public static List<ClsTerms> getInvoiceTerms(String _where) {

        List<ClsTerms> list = new ArrayList<>();
        try {
            db = mContext.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);


            String qry = "SELECT "
                    .concat("[TERMS]")
                    .concat(" FROM [tbl_Terms] WHERE 1=1 AND [ACTIVE] ='YES' ")
                    .concat(_where)
                    .concat(" ORDER BY [SORT_NO],[TERMS] ");

            Cursor cur = db.rawQuery(qry, null);


            Log.e("--qry--", "Qry: " + qry);
            Log.e("--qry--", "Count: " + cur.getCount());

            while (cur.moveToNext()) {
                ClsTerms getSet = new ClsTerms();
                getSet.setmTerms(cur.getString(cur.getColumnIndex("TERMS")));
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
    public static int Delete(int terms_id) {
        int result = 0;
        try {

            db = mContext.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String strSql = "DELETE FROM [tbl_Terms] WHERE [TERMS_ID] = "
                    .concat(String.valueOf(terms_id))
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
    public static int Update(ClsTerms objTerms) {
        int result = 0;
        try {

            db = mContext.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String strSql = "UPDATE [tbl_Terms] SET "

                    .concat(" [TERMS] = ")
                    .concat("'")
                    .concat(objTerms.getmTerms().trim()
                            .replace("'", "''"))
                    .concat("'")

                    .concat(" ,[SORT_NO] = ")
                    .concat(String.valueOf(objTerms.getmSort_No()))

                    .concat(" ,[ACTIVE] = ")
                    .concat("'")
                    .concat(objTerms.getmActive())
                    .concat("'")

                    .concat(" ,[TERM_TYPE] = ")
                    .concat("'")
                    .concat(objTerms.getTERM_TYPE())
                    .concat("'")

                    .concat(" WHERE [TERMS_ID] = ")

                    .concat(String.valueOf(objTerms.getmTerms_id()))

                    .concat(";");

            Log.e("Update", strSql);
            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();
            //db.execSQL(strSql);
            db.close();

        } catch (Exception e) {
            Log.e("ClsItem", "Update" + e.getMessage());
        }
        return result;
    }

    @SuppressLint("WrongConstant")
    public static ClsTerms QureyById(int table_id) {

        Log.e("ClsTerms_Id", String.valueOf(table_id));

        ClsTerms currentObj = new ClsTerms();

        try {
            db = mContext.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            Cursor cur = db.rawQuery("SELECT "
                    .concat("[TERMS_ID]")
                    .concat(",[TERMS]")
                    .concat(",[SORT_NO]")
                    .concat(",IFNULL([TERM_TYPE],'SALE INVOICE') AS [TERM_TYPE]")
                    .concat(",[ACTIVE]")
                    .concat(" FROM [tbl_Terms] WHERE TERMS_ID = ")
                    .concat(String.valueOf(table_id)), null);

            while (cur.moveToNext()) {
                currentObj.setmTerms_id(cur.getInt(cur.getColumnIndex("TERMS_ID")));
                currentObj.setmTerms(cur.getString(cur.getColumnIndex("TERMS")));
                currentObj.setmSort_No(cur.getInt(cur.getColumnIndex("SORT_NO")));
                currentObj.setTERM_TYPE(cur.getString(cur.getColumnIndex("TERM_TYPE")));
                currentObj.setmActive(cur.getString(cur.getColumnIndex("ACTIVE")));
            }

        } catch (Exception e) {
            Log.e("ClsTerms", ">>GetList" + e.getMessage());
            e.getMessage();
        }
        return currentObj;
    }


    @SuppressLint("WrongConstant")
    public static List<String> getAddColumn(Context context,io.requery.android.database.
            sqlite.SQLiteDatabase db) {
        List<String> columns = new ArrayList<>();
        try {

//            io.requery.android.database.sqlite.SQLiteDatabase db;
//
//            db = io.requery.android.database.sqlite.SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath(ClsGlobal.Database_Name).getPath(),
//                    null);

            String qry = "SELECT * FROM [tbl_Terms] LIMIT 1 ";

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

}
