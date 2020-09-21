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

public class ClsActivityLog {

    int logId;
    String action = "", entryDate = "", user = "", description = "";

    private static SQLiteDatabase db;

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @SuppressLint("WrongConstant")
    public static int Insert(ClsActivityLog clsActivityLog, Context context) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String qry = "INSERT INTO [ActivityLog] ([action],[entryDate],[user],[description]) VALUES ('"

                    .concat(clsActivityLog.getAction().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(clsActivityLog.getEntryDate())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(clsActivityLog.getUser().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(String.valueOf(clsActivityLog.getDescription()))
                    .concat("'")
                    .concat(",")

                    .concat(");");

            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.e("INSERT", "ActivityLog--->>" + qry);

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsActivityLog", "Insert>>>>>>>>" + e.getMessage());
            e.getMessage();
        }
        return result;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsActivityLog> getList(String _where,Context context) {
        List<ClsActivityLog> list = new ArrayList<>();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            Cursor cur = db.rawQuery("SELECT "
                    .concat(" * From")
                    .concat(" FROM [ActivityLog] WHERE 1=1 ")
                    .concat(_where)
                    .concat(" ORDER BY [entryDate] ASC "), null);


            while (cur.moveToNext()) {
                ClsActivityLog getSet = new ClsActivityLog();
                getSet.setLogId(cur.getInt(cur.getColumnIndex("logId")));
                getSet.setAction(cur.getString(cur.getColumnIndex("action")));
                getSet.setEntryDate(cur.getString(cur.getColumnIndex("entryDate")));
                getSet.setUser(cur.getString(cur.getColumnIndex("user")));
                getSet.setDescription(cur.getString(cur.getColumnIndex("description")));
                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("ClsActivityLog", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


}
