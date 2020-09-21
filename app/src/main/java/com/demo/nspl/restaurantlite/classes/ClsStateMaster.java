package com.demo.nspl.restaurantlite.classes;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.demo.nspl.restaurantlite.A_Test.ClsGetValue;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.DatabaseHelper;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsState;

import java.util.ArrayList;
import java.util.List;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import static android.content.Context.MODE_APPEND;

public class ClsStateMaster {

    private String name = "", StateCode = "";

    private int STATE_ID,CountryId;

    public int getCountryId() {
        return CountryId;
    }

    public void setCountryId(int countryId) {
        CountryId = countryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStateCode() {
        return StateCode;
    }

    public void setStateCode(String stateCode) {
        StateCode = stateCode;
    }

    public int getSTATE_ID() {
        return STATE_ID;
    }

    public void setSTATE_ID(int STATE_ID) {
        this.STATE_ID = STATE_ID;
    }


    @SuppressLint("WrongConstant")
    public static int InsertList(List<ClsState> lstClsGetValues, Context context) {
        int result = 0;

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        io.requery.android.database.sqlite.SQLiteDatabase
                db = databaseHelper.openDatabase();

        try {

            db.beginTransaction();
            if (lstClsGetValues != null && lstClsGetValues.size() > 0) {
                for (ClsState item : lstClsGetValues) {
//                    String qry = ("INSERT INTO [State_MASTER] ([State_Name],[StateCode],[CountryId]) " +
//                            "VALUES ('")
//                            .concat(item.getName().trim().replace("'", "''"))
//                            .concat("'")
//                            .concat(",")
//
//                            .concat("'")
//                            .concat("")
//                            .concat("'")
//                            .concat(",")
//
//                            .concat(item.getCountryId())
//                            .concat(");");
//
//                    io.requery.android.database.sqlite
//                            .SQLiteStatement statement = db.compileStatement(qry);
//                    result = statement.executeUpdateDelete();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("State_Name", item.getName());
                    contentValues.put("StateCode", "");
                    contentValues.put("CountryId", Integer.valueOf(item.getCountryId()));

                    db.insert("State_MASTER", null, contentValues);
                }
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("ClsReservationMaster", "InsertException-->>>>>>>>" + e.getMessage());
            e.getMessage();
        }finally {
            db.endTransaction();
            databaseHelper.closeDatabase();
        }
        return result;

    }


    @SuppressLint("WrongConstant")
    public static int getStatesCounts(Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

        int cursorCount = 0;
        Cursor cur = null;
        try {

            String qry = "SELECT * FROM [State_MASTER] ";

            io.requery.android.database.sqlite.SQLiteDatabase
                    db = databaseHelper.openDatabase();

             cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));
            Log.e("qry", qry);
            cursorCount = cur.getCount();


        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
        }finally {
            if (cur != null) {
                cur.close();
            }
            databaseHelper.closeDatabase();
        }

        return cursorCount;
    }

    @SuppressLint("WrongConstant")
    public static int DeleteAll(Context context) {
        int result = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        try {

            io.requery.android.database.sqlite.SQLiteDatabase
                    db = databaseHelper.openDatabase();

            String strSql = "DELETE FROM [State_MASTER] ";
            Log.e("DELETE", "DELqRY-" + strSql);

            io.requery.android.database.sqlite
                    .SQLiteStatement statement = db.compileStatement(strSql);

            result = statement.executeUpdateDelete();

        } catch (Exception e) {
            Log.e("ClsEmployee", "Delete" + e.getMessage());
            e.getMessage();
        }finally {
            databaseHelper.closeDatabase();
        }

        return result;


    }


    @SuppressLint("WrongConstant")
    public static List<ClsStateMaster> getStateList(String _where,Context context) {
        List<ClsStateMaster> list = new ArrayList<>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

        io.requery.android.database.sqlite.SQLiteDatabase
                db = databaseHelper.openDatabase();
        Cursor cur = null;
        try {

            cur = db.rawQuery("SELECT * "
                    .concat(" FROM [State_MASTER] WHERE 1=1 ")
                    .concat(_where)
                    .concat(" ORDER BY [State_Name] ASC "), null);


            while (cur.moveToNext()) {
                ClsStateMaster getSet = new ClsStateMaster();
                getSet.setSTATE_ID(cur.getInt(cur.getColumnIndex("STATE_ID")));
                getSet.setName(cur.getString(cur.getColumnIndex("State_Name")));
                getSet.setCountryId(cur.getInt(cur.getColumnIndex("CountryId")));
                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("ClsStateMaster", "GetList " + e.getMessage());
        }finally {

            if (cur != null){
                cur.close();
            }
            databaseHelper.closeDatabase();

        }
        return list;
    }

}
