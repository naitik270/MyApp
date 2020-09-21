package com.demo.nspl.restaurantlite.SMS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.classes.ClsCustomerMaster;
import com.demo.nspl.restaurantlite.classes.ClsTerms;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClsSmsIdSetting implements Serializable {

    int id = 0;
    String sms_id = "";
    String default_sms = "";
    String active = "";
    String entry_date = "";
    String remark = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSms_id() {
        return sms_id;
    }

    public void setSms_id(String sms_id) {
        this.sms_id = sms_id;
    }

    public String getDefault_sms() {
        return default_sms;
    }

    public void setDefault_sms(String default_sms) {
        this.default_sms = default_sms;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getEntry_date() {
        return entry_date;
    }

    public void setEntry_date(String entry_date) {
        this.entry_date = entry_date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    static Context mContext;
    AppCompatActivity activity;
    private static SQLiteDatabase db;


    public ClsSmsIdSetting() {

    }

    public ClsSmsIdSetting(Context ctx) {

        mContext = ctx;
    }

    @SuppressLint("WrongConstant")
    public static ClsSmsIdSetting getSmsIdSettingByDefault(SQLiteDatabase db) {

        ClsSmsIdSetting clsSmsIdSetting = new ClsSmsIdSetting();
        try {
            String qry = "SELECT *"
                    .concat(" FROM [SmsIdSetting] WHERE 1=1 ")
                    .concat(" AND [default_sms] = 'YES'");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--sms--", "getCount: " + cur.getCount());
            Log.e("--sms--", "qry: " + qry);

            while (cur.moveToNext()) {
                clsSmsIdSetting.setId(cur.getInt(cur.getColumnIndex("id")));
                clsSmsIdSetting.setSms_id(cur.getString(cur.getColumnIndex("sms_id")));
                clsSmsIdSetting.setDefault_sms(cur.getString(cur.getColumnIndex("default_sms")));
                clsSmsIdSetting.setActive(cur.getString(cur.getColumnIndex("active")));
                clsSmsIdSetting.setRemark(cur.getString(cur.getColumnIndex("remark")));
            }
        } catch (Exception e) {
            Log.e("--sms--", "ExceptionList: " + e.getMessage());
            e.getMessage();
        }
        return clsSmsIdSetting;
    }


    @SuppressLint("WrongConstant")
    public static int Insert(ClsSmsIdSetting objClsSmsIdSetting, Context context) {
        int result = 0;
        try {
            db = mContext.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            // Update if we have default value is yes.
            // so that we don't have two default SmsSenderId's.
            if (objClsSmsIdSetting.getDefault_sms().equalsIgnoreCase("YES")) {
                ClsSmsIdSetting clsSmsIdSetting = getSmsIdSettingByDefault(db);
                Log.e("--sms--", "objClsSmsIdSetting.getDefault_sms(): Yes ");

                clsSmsIdSetting.setDefault_sms("No");
                Log.e("--sms--", "objClsSmsIdSetting.getDefault_sms(): Yes ");
                Update(clsSmsIdSetting, "Update default", db);

            }

            String qry = ("INSERT INTO [SmsIdSetting] ([sms_id],[default_sms],[active]," +
                    "[entry_date],[remark]) VALUES ('")

                    .concat(objClsSmsIdSetting.getSms_id().trim().replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objClsSmsIdSetting.getDefault_sms())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objClsSmsIdSetting.getActive())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ClsGlobal.getCurruntDateTime())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objClsSmsIdSetting.getRemark())
                    .concat("'")
                    .concat(");");

            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.e("--sms--", "INSERT: " + qry);

            db.close();

        } catch (Exception e) {
            Log.e("--sms--", "ExceptionInsert: " + e.getMessage());
            db.close();
            e.getMessage();
        }
        return result;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsSmsIdSetting> getList(Context context, String _where) {

        List<ClsSmsIdSetting> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);


            String qry = "SELECT "
                    .concat("[id]")
                    .concat(",[sms_id]")
                    .concat(",[default_sms]")
                    .concat(",[active]")
                    .concat(",[remark]")
                    .concat(" FROM [SmsIdSetting] WHERE 1=1 ")
                    .concat(_where);


            Cursor cur = db.rawQuery(qry, null);

            Log.e("--sms--", "qry: " + qry);
            Log.e("--sms--", "getCount: " + cur.getCount());

            while (cur.moveToNext()) {
                ClsSmsIdSetting getSet = new ClsSmsIdSetting();
                getSet.setId(cur.getInt(cur.getColumnIndex("id")));
                getSet.setSms_id(cur.getString(cur.getColumnIndex("sms_id")));
                getSet.setDefault_sms(cur.getString(cur.getColumnIndex("default_sms")));
                getSet.setActive(cur.getString(cur.getColumnIndex("active")));
                getSet.setRemark(cur.getString(cur.getColumnIndex("remark")));
                list.add(getSet);
            }
            db.close();
        } catch (Exception e) {
            Log.e("--sms--", "ExceptionList: " + e.getMessage());
            db.close();
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static int Delete(int id) {
        int result = 0;
        try {

            db = mContext.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String strSql = "DELETE FROM [SmsIdSetting] WHERE [id] = "
                    .concat(String.valueOf(id))
                    .concat(";");

            Log.e("--sms--", "DELETE: " + strSql);
            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();

            db.close();
        } catch (Exception e) {
            Log.e("--sms--", "Exception_Del: " + e.getMessage());
            db.close();
            e.getMessage();
        }
        return result;

    }

    @SuppressLint("WrongConstant")
    public static int Update(ClsSmsIdSetting objClsSmsIdSetting, String mode, SQLiteDatabase db) {
        int result = 0;
        try {


            if (mode.equalsIgnoreCase("Update All")) {
                // Update if we have default value is yes.
                // so that we don't have two default SmsSenderId's.
                if (objClsSmsIdSetting.getDefault_sms().equalsIgnoreCase("YES")) {
                    ClsSmsIdSetting clsSmsIdSetting = getSmsIdSettingByDefault(db);
                    clsSmsIdSetting.setDefault_sms("No");
                    Update(clsSmsIdSetting, "Update default", db);
                }
            }

//            db = mContext.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String strSql = "UPDATE [SmsIdSetting] SET "

                    .concat(" [sms_id] = ")
                    .concat("'")
                    .concat(objClsSmsIdSetting.getSms_id().trim().replace("'", "''"))
                    .concat("'")

                    .concat(" ,[default_sms] = ")
                    .concat("'")
                    .concat(objClsSmsIdSetting.getDefault_sms())
                    .concat("'")

                    .concat(" ,[active] = ")
                    .concat("'")
                    .concat(objClsSmsIdSetting.getActive())
                    .concat("'")

                    .concat(" ,[remark] = ")
                    .concat("'")
                    .concat(objClsSmsIdSetting.getRemark())
                    .concat("'")

                    .concat(" WHERE [id] = ")
                    .concat(String.valueOf(objClsSmsIdSetting.getId()))
                    .concat(";");

            Log.e("Update", strSql);
            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();
            //db.execSQL(strSql);

        } catch (Exception e) {
            Log.e("ClsItem", "Update" + e.getMessage());
        }
        return result;
    }


    @SuppressLint("WrongConstant")
    public boolean checkExists(String _where, Context context) {
        boolean result = false;

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = "SELECT 1 FROM [SmsIdSetting] WHERE 1=1 "
                    .concat(_where)
                    .concat(";");
            Log.e("exists", "existsChqQry - " + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            if (cur != null && cur.getCount() != 0) {
                result = true;
            }
            db.close();
        } catch (Exception e) {
            Log.e("ClsExpenseType", "ClsExpenseType>>>>CheckExist" + e.getMessage());
            db.close();
            e.getMessage();
        }
        return result;
    }


    @SuppressLint("WrongConstant")
    public static ClsSmsIdSetting getObject(ClsSmsIdSetting objClsSmsIdSetting, Context context) {
        ClsSmsIdSetting obj = new ClsSmsIdSetting();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = ("SELECT [id],[sms_id],[default_sms],[active],[remark] FROM [SmsIdSetting] " +
                    "WHERE 1=1 AND [id] = ")
                    .concat(String.valueOf(objClsSmsIdSetting.getId()))
                    .concat(";");


            Log.e("getObject", ">>>>>>>>>" + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            Log.e("getObject", ">>>>>>>>>" + cur.getCount());
            while (cur.moveToNext()) {

                obj.setId(cur.getInt(cur.getColumnIndex("id")));
                obj.setSms_id(cur.getString(cur.getColumnIndex("sms_id")));
                obj.setDefault_sms(cur.getString(cur.getColumnIndex("default_sms")));
                obj.setActive(cur.getString(cur.getColumnIndex("active")));
                obj.setRemark(cur.getString(cur.getColumnIndex("remark")));
            }

            db.close();

        } catch (Exception e) {
            Log.e("getObject", "GetObjectttt" + e.getMessage());
            db.close();
            e.getMessage();
        }
        return obj;
    }
// 34:40 16:30  = 51:10

}
