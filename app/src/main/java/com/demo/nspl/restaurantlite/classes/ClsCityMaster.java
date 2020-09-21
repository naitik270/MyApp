package com.demo.nspl.restaurantlite.classes;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.DatabaseHelper;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsCitys;

import java.util.ArrayList;
import java.util.List;

public class ClsCityMaster {

    String CityName = "", CityCode = "";
    int city_id = 0, stateId = 0;

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getCityCode() {
        return CityCode;
    }

    public void setCityCode(String cityCode) {
        CityCode = cityCode;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        this.CityName = cityName;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    @SuppressLint("WrongConstant")
    public static int InsertList(List<ClsCitys> lstClsGetValues, Context context) {
        int result = 0;

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        io.requery.android.database.sqlite.SQLiteDatabase
                db = databaseHelper.openDatabase();
        try {

            db.beginTransaction();

            if (lstClsGetValues != null && lstClsGetValues.size() > 0) {
                for (ClsCitys item : lstClsGetValues) {

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("City_Name", item.getName());
                    contentValues.put("CityCode", "");
                    contentValues.put("StateId", Integer.valueOf(item.getStateId()));

                    db.insert("City_MASTER", null, contentValues);

                }
            }


            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("ClsReservationMaster", "InsertException-->>>>>>>>" + e.getMessage());
            e.getMessage();
        } finally {
            db.endTransaction();
            databaseHelper.closeDatabase();
        }
        return result;

    }


    @SuppressLint("WrongConstant")
    public static long Insert(ClsCityMaster clsCityMaster, Context context) {
        long result = 0;

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        io.requery.android.database.sqlite.SQLiteDatabase
                db = databaseHelper.openDatabase();
        try {


            ContentValues contentValues = new ContentValues();
            contentValues.put("City_Name", clsCityMaster.getCityName());
            contentValues.put("CityCode", clsCityMaster.getCityCode());
            contentValues.put("StateId", clsCityMaster.getStateId());

            result = db.insert("City_MASTER", null, contentValues);


        } catch (Exception e) {
            Log.e("ClsReservationMaster", "InsertException-->>>>>>>>" + e.getMessage());
            e.getMessage();
        } finally {

            databaseHelper.closeDatabase();
        }
        return result;

    }

    @SuppressLint("WrongConstant")
    public static int getCityCounts(Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

        int cursorCount = 0;
        Cursor cur = null;
        try {

            String qry = "SELECT * FROM [City_MASTER]";

            io.requery.android.database.sqlite.SQLiteDatabase
                    db = databaseHelper.openDatabase();

            cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));
            Log.e("qry", qry);
            cursorCount = cur.getCount();


        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());

        } finally {
            if (cur != null) {
                cur.close();
            }
            databaseHelper.closeDatabase();
        }

        return cursorCount;
    }


    @SuppressLint("WrongConstant")
    public static boolean checkExists(String _where,Context context) {
        boolean result = false;


        DatabaseHelper databaseHelper =
                DatabaseHelper.getInstance(context);
        io.requery.android.database.sqlite.SQLiteDatabase
                db = databaseHelper.openDatabase();

        Cursor cur = null;
        try {


            String sqlStr = "SELECT 1 FROM [City_MASTER] WHERE 1=1 "
                    .concat(_where)
                    .concat(";");
            Log.e("exists", "existsChqQry - " + sqlStr);

             cur = db.rawQuery(sqlStr, null);

            if (cur != null && cur.getCount() != 0) {
                result = true;
            }

            Log.e("Check", "ClsCityMaster>>>>result" + result);
        } catch (Exception e) {
            Log.e("ClsCityMaster", "ClsCityMaster>>>>CheckExist" + e.getMessage());

        }finally {
            if (cur != null){
                cur.close();
            }
            databaseHelper.closeDatabase();
        }
        return result;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsCityMaster> getCityList(String _where, Context context) {
        List<ClsCityMaster> list = new ArrayList<>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

        io.requery.android.database.sqlite.SQLiteDatabase
                db = databaseHelper.openDatabase();
        Cursor cur = null;
        try {

            cur = db.rawQuery("SELECT * "
                    .concat(" FROM [City_MASTER] WHERE 1=1 ")
                    .concat(_where)
                    .concat(" ORDER BY [City_Name] ASC "), null);


            while (cur.moveToNext()) {
                ClsCityMaster getSet = new ClsCityMaster();
                getSet.setCity_id(cur.getInt(cur.getColumnIndex("City_ID")));
                getSet.setCityName(cur.getString(cur.getColumnIndex("City_Name")));
                getSet.setStateId(cur.getInt(cur.getColumnIndex("StateId")));
                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("ClsCityMaster", "GetList " + e.getMessage());
        } finally {
            if (cur != null) {
                cur.close();
            }
            databaseHelper.closeDatabase();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static int DeleteAll(Context context) {
        int result = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        try {

            io.requery.android.database.sqlite.SQLiteDatabase
                    db = databaseHelper.openDatabase();

            String strSql = "DELETE FROM [City_MASTER] ";
            Log.e("DELETE", "DELqRY-" + strSql);

            io.requery.android.database.sqlite
                    .SQLiteStatement statement = db.compileStatement(strSql);

            result = statement.executeUpdateDelete();


        } catch (Exception e) {
            Log.e("ClsEmployee", "Delete" + e.getMessage());
            e.getMessage();
        } finally {
            databaseHelper.closeDatabase();
        }

        return result;

    }

    public static int updateCity(ClsCityMaster clsCityMaster,Context context) {
        int mNumberOfRowsUpdated = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        io.requery.android.database.sqlite.SQLiteDatabase
                db = databaseHelper.openDatabase();

        try {


            ContentValues values = new ContentValues();
            values.put("City_Name", clsCityMaster.getCityName());
            values.put("CityCode", clsCityMaster.getCityCode());
            values.put("StateId", clsCityMaster.getStateId());

            String selection = "City_ID" + "=?";
            String[] selectionArgs = {String.valueOf(clsCityMaster.getCity_id())};

            mNumberOfRowsUpdated = db.update("City_MASTER", //table to change
                    values, // new values to insert
                    selection, // selection criteria for row (in this case, the _id column)
                    selectionArgs); //selection args; the actual value of the id

        } catch (Exception e) {
            //Log.d (TAG, "UPDATE EXCEPTION! " + e.getMessage());
        }finally {
            databaseHelper.closeDatabase();
        }
        return mNumberOfRowsUpdated;
    }


    @SuppressLint("WrongConstant")
    public static int DeleteById(String where, Context context) {
        int result = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        try {

            io.requery.android.database.sqlite.SQLiteDatabase
                    db = databaseHelper.openDatabase();

            String strSql = "DELETE FROM [City_MASTER] where 1=1"
                    .concat(where);

            Log.e("DELETE", "DELqRY-" + strSql);

            io.requery.android.database.sqlite
                    .SQLiteStatement statement = db.compileStatement(strSql);

            result = statement.executeUpdateDelete();


        } catch (Exception e) {
            Log.e("ClsEmployee", "Delete" + e.getMessage());
            e.getMessage();
        } finally {
            databaseHelper.closeDatabase();
        }

        return result;

    }
}
