package com.demo.nspl.restaurantlite.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import android.util.Log;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;

import java.util.ArrayList;
import java.util.List;

public class ClsEmailLogs {

    private int EMAILLOGS_ID;
    private String MESSAGE,DATE_TIME;
    static Context context;
    private static SQLiteDatabase db;

    public ClsEmailLogs(Context context){
        this.context = context;
    }


    public int getEMAILLOGS_ID() {
        return EMAILLOGS_ID;
    }

    public void setEMAILLOGS_ID(int EMAILLOGS_ID) {
        this.EMAILLOGS_ID = EMAILLOGS_ID;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public String getDATE_TIME() {
        return DATE_TIME;
    }

    public void setDATE_TIME(String DATE_TIME) {
        this.DATE_TIME = DATE_TIME;
    }


    @SuppressLint("WrongConstant")
    public static int Insert(ClsEmailLogs ObjEmailLogs) {
        int  result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            String qry = "INSERT INTO [tbl_EmailLogs] ([MESSAGE],[DATE_TIME]) VALUES ('"

                    .concat(ObjEmailLogs.getMESSAGE().trim()
                            .replace("'","''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjEmailLogs.getDATE_TIME())
                    .concat("'")

                    .concat(")");

            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.e("INSERT","ClsEmailLogs--->>"+ qry);

            db.close();
            Delete();
            return result;
        } catch (Exception e) {
            Log.e("INSERT", "Insert>>>>>>>>" + e.getMessage());
            e.getMessage();
        }
        return result;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsEmailLogs> getList(Context context) {
        List<ClsEmailLogs> list = new ArrayList<>();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            Cursor cur = db.rawQuery("SELECT [EMAILLOGS_ID],[MESSAGE],[DATE_TIME] FROM [tbl_EmailLogs] ORDER BY [EMAILLOGS_ID] DESC;", null);

            Log.e("cur", String.valueOf(cur.getCount()));

            while (cur.moveToNext()) {
                ClsEmailLogs getSet = new ClsEmailLogs(context);
                getSet.setEMAILLOGS_ID(cur.getInt(cur.getColumnIndex("EMAILLOGS_ID")));
                getSet.setMESSAGE(cur.getString(cur.getColumnIndex("MESSAGE")));
                getSet.setDATE_TIME(cur.getString(cur.getColumnIndex("DATE_TIME")));

                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static void Delete(){
        Log.e("Delete","Delete");
        int result;
        List<String> idList = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String qry = "SELECT [EMAILLOGS_ID] FROM [tbl_EmailLogs] ORDER BY [EMAILLOGS_ID] DESC LIMIT 10";

            Cursor cur = db.rawQuery(qry, null);

            Log.e("qry", qry);
            Log.e("cur23", String.valueOf(cur.getCount()));

            while (cur.moveToNext()) {
                idList.add(String.valueOf(cur.getInt(cur.getColumnIndex("EMAILLOGS_ID"))));
            }

            String _where = TextUtils.join(",", idList);
            Log.e("_where", _where);

            String deleteQry = "DELETE FROM [tbl_EmailLogs] WHERE [EMAILLOGS_ID] NOT IN (".concat(_where).concat(")");
            SQLiteStatement statement = db.compileStatement(deleteQry);
            result = statement.executeUpdateDelete();

            Log.e("deleteQry", "ClsEmailLogs--->>" + deleteQry);
            Log.e("result1", String.valueOf(result));

        }catch (Exception e){

        }
    }

//    @SuppressLint("WrongConstant")
//    public static void Deletetest(Context context){
//        int result;
//
//        try {
//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            String qry = "DELETE * FROM [tbl_EmailLogs]";
////            Cursor cur = db.rawQuery(qry, null);
//
// //           Log.e("cur", String.valueOf(cur.getCount()));
//            SQLiteStatement statement = db.compileStatement(qry);
//            result = statement.executeUpdateDelete();
//
//            Log.e("INSERT","ClsEmailLogs--->>"+ qry);
//            Log.e("result","ClsEmailLogs--->>"+ result);
//
//            db.close();
//
//
//        }catch (Exception e){
//
//        }
//
//
//    }


}
