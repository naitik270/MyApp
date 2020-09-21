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

public class ClsCommonLogs {

    private String Remark="",Status="",Date_Time="",Log_Type = "";
    private int CommonLogs_ID =0;

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    private static SQLiteDatabase db;

    public String getLog_Type() {
        return Log_Type;
    }

    public void setLog_Type(String log_Type) {
        Log_Type = log_Type;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDate_Time() {
        return Date_Time;
    }

    public void setDate_Time(String date_Time) {
        Date_Time = date_Time;
    }

    public int getCommonLogs_ID() {
        return CommonLogs_ID;
    }

    public void setCommonLogs_ID(int commonLogs_ID) {
        CommonLogs_ID = commonLogs_ID;
    }

    @SuppressLint("WrongConstant")
    public static int INSERT(ClsCommonLogs CommonLogs,Context context) {

        int result = 0;
        try {

//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            }


            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String qry = ("INSERT INTO [CommonLogs_Master] ([Remark],[Status],[Log_Type]" +
                    ",[Date_Time]) VALUES ('")
                    .concat(CommonLogs.getRemark().trim()
                            .replace("'","''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(CommonLogs.getStatus())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(CommonLogs.getLog_Type())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(CommonLogs.getDate_Time())
                    .concat("'")

                    .concat(");");

            SQLiteStatement sqLiteStatement = db.compileStatement(qry);
            result = sqLiteStatement.executeUpdateDelete();
            Log.e("QRERY", qry);

            db.close();

            return result;

        } catch (Exception e) {
            Log.e("ClsCommonLogs", "Insert----" + e.getMessage());
        }

        return result;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsCommonLogs> getBackupLogs(String where, Context context) {
        List<ClsCommonLogs> list = new ArrayList<>();
        try {
//
//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            }


            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT * FROM [CommonLogs_Master] where 1=1 "
                    .concat(where).concat(" ORDER BY [CommonLogs_ID] DESC LIMIT 100;");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));
            Log.e("qry", qry);

            while (cur.moveToNext()) {
                ClsCommonLogs getSet = new ClsCommonLogs();
                getSet.setCommonLogs_ID(cur.getInt(cur.getColumnIndex("CommonLogs_ID")));
                getSet.setRemark(cur.getString(cur.getColumnIndex("Remark")));
                getSet.setStatus(cur.getString(cur.getColumnIndex("Status")));
                getSet.setDate_Time(cur.getString(cur.getColumnIndex("Date_Time")));
                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


}
