package com.demo.nspl.restaurantlite.SMS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.util.Log;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_APPEND;

public class ClsMessageFormat implements Serializable {

    int id;
    String title = "", message_format = "", Type = "", Default = "", Remark = "";
    private static SQLiteDatabase db;

    private static Context context;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage_format() {
        return message_format;
    }

    public void setMessage_format(String message_format) {
        this.message_format = message_format;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getDefault() {
        return Default;
    }

    public void setDefault(String aDefault) {
        Default = aDefault;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public static Integer Insert(ClsMessageFormat ObjclsMessageFormat, Context context) {
        int result = 0;

        db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_PRIVATE, null);
        try {

            // Update if we have default value yes.
            // so that we don't have two default SmsSenderId's.
//            if (ObjclsMessageFormat.getDefault().equalsIgnoreCase("Yes")) {
//                ClsMessageFormat clsMessageFormat;
//
//                Log.e("ClsMessageFormat", "getType from Insert " + ObjclsMessageFormat.getType());
//                if (ObjclsMessageFormat.getType().equalsIgnoreCase("Sales")) {
//                    Log.e("ClsMessageFormat", "getType from Insert Sales" );
//                    String where = " AND [Type] = 'Sales'";
//                    // getting default format message where type is == Sales.
//                    clsMessageFormat = getMessageFormatByDefault(db, where);
//                } else {
//                    String where = " AND [Type] != 'Sales'";
//                    // getting default format message where type is not equal to Sales.
//                    Log.e("ClsMessageFormat", "getType from Insert else" );
//                    clsMessageFormat = getMessageFormatByDefault(db, where);
//                }
//
//                if (clsMessageFormat.getMessage_format().length() > 0){
//                    clsMessageFormat.setDefault("No");
//                    Update(clsMessageFormat, "Update default", db);
//                }
//        }

            ClsMessageFormat clsMessageFormat = new ClsMessageFormat();
            if (ObjclsMessageFormat.getDefault().equalsIgnoreCase("Yes")) {
                String where = " AND [Type] = '".concat(ObjclsMessageFormat.getType()).concat("'");
                clsMessageFormat = getMessageFormatByDefault(db, where);
            }

            if (clsMessageFormat.getMessage_format().length() > 0) {

                clsMessageFormat.setDefault("No");
                Update(clsMessageFormat, "Update default", db);
            }


            String qry = ("INSERT INTO [MessageFormat_Master] ([Title],[MessageFormat],[Type]," +
                    "[Default],[Remark]) " +
                    "VALUES (")
                    .concat("'")
                    .concat(String.valueOf(ObjclsMessageFormat.getTitle()))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjclsMessageFormat.getMessage_format())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjclsMessageFormat.getType())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjclsMessageFormat.getDefault())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjclsMessageFormat.getRemark())
                    .concat("'")

                    .concat(");");

            Log.e("Qry", "Qry--->>" + qry);

            SQLiteStatement sqLiteStatement = db.compileStatement(qry);
            result = sqLiteStatement.executeUpdateDelete();
            Log.e("ClsMessageFormat", "Qry--->>result = " + result);

            db.close();


        } catch (Exception e) {
            Log.e("ClsMessageFormat", "Catch--->>>" + e.getMessage());
        }

        return result;
    }

    @SuppressLint("WrongConstant")
    public static ClsMessageFormat QueryById(Context context, String id) {

        ClsMessageFormat clsMessageFormat = new ClsMessageFormat();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT * "
                    .concat(" FROM [MessageFormat_Master] WHERE 1=1 ")
                    .concat(" AND [MessageFormat_ID] = ")
                    .concat(id);

            Log.e("qry", "ClsMessageFormat >>>>MessageFormat_Master" + qry);

            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", "ClsMessageFormat >>>>MessageFormat_Master Count " + cur.getCount());
            while (cur.moveToNext()) {
                clsMessageFormat.setId(cur.getInt(cur.getColumnIndex("MessageFormat_ID")));
                clsMessageFormat.setTitle(cur.getString(cur.getColumnIndex("Title")));
                clsMessageFormat.setType(cur.getString(cur.getColumnIndex("Type")));
                clsMessageFormat.setDefault(cur.getString(cur.getColumnIndex("Default")));
                clsMessageFormat.setMessage_format(cur.getString(cur.getColumnIndex("MessageFormat")));
                clsMessageFormat.setRemark(cur.getString(cur.getColumnIndex("Remark")));
            }

            db.close();

        } catch (Exception e) {
            Log.e("ClsMessageFormat", "ClsMessageFormat >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return clsMessageFormat;
    }

    @SuppressLint("WrongConstant")
    public static ClsMessageFormat getMessageFormatByDefault(SQLiteDatabase db, String where) {

        String messageFormat = "";
        ClsMessageFormat clsMessageFormat = new ClsMessageFormat();

        try {

            String qry = "SELECT * "
                    .concat(" FROM [MessageFormat_Master] WHERE 1=1 ")
                    .concat(" AND [Default] = 'Yes'")
                    .concat(where);


            Log.e("qry", "ClsMessageFormat >>>>getMessageFormatById" + qry);

            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", "ClsMessageFormat >>>>getMessageFormatById Count " + cur.getCount());

            while (cur.moveToNext()) {
                clsMessageFormat.setId(cur.getInt(cur.getColumnIndex("MessageFormat_ID")));
                clsMessageFormat.setTitle(cur.getString(cur.getColumnIndex("Title")));
                clsMessageFormat.setType(cur.getString(cur.getColumnIndex("Type")));
                clsMessageFormat.setDefault(cur.getString(cur.getColumnIndex("Default")));
                clsMessageFormat.setMessage_format(
                        cur.getString(cur.getColumnIndex("MessageFormat")));
                clsMessageFormat.setRemark(cur.getString(cur.getColumnIndex("Remark")));
            }


        } catch (Exception e) {
            Log.e("ClsMessageFormat", "ClsMessageFormat " +
                    ">>>>getMessageFormatById" + e.getMessage());
            e.getMessage();
        }
        return clsMessageFormat;
    }


    @SuppressLint("WrongConstant")
    public static String getMessageFormatById(Context context, String id) {

        String messageFormat = "";


        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT [MessageFormat] "
                    .concat(" FROM [MessageFormat_Master] WHERE 1=1 ")
                    .concat(" AND [MessageFormat_ID] = ")
                    .concat(id);

            Log.e("qry", "ClsMessageFormat >>>>getMessageFormatById" + qry);

            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", "ClsMessageFormat >>>>getMessageFormatById Count " + cur.getCount());
            while (cur.moveToNext()) {
                messageFormat = cur.getString(cur.getColumnIndex("MessageFormat"));

            }

            db.close();

        } catch (Exception e) {
            Log.e("ClsMessageFormat", "ClsMessageFormat >>>>getMessageFormatById" + e.getMessage());
            e.getMessage();
        }
        return messageFormat;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsMessageFormat> getList(Context context) {

        List<ClsMessageFormat> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT "
                    .concat("[MessageFormat_ID]")
                    .concat(",[Title]")
                    .concat(",[Type]")
                    .concat(",[Default]")
                    .concat(",[MessageFormat]")
                    .concat(",[Remark]")
                    .concat(" FROM [MessageFormat_Master] WHERE 1=1 ");

            Log.e("qry", "ClsInventoryLayer >>>>tbl_LayerItem_MasterGetList" + qry);

            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", "ClsInventoryLayer >>>>tbl_LayerItem_MasterGetList Count " + cur.getCount());
            while (cur.moveToNext()) {
                ClsMessageFormat clsMessageFormat = new ClsMessageFormat();
                clsMessageFormat.setId(cur.getInt(cur.getColumnIndex("MessageFormat_ID")));
                clsMessageFormat.setTitle(cur.getString(cur.getColumnIndex("Title")));
                clsMessageFormat.setType(cur.getString(cur.getColumnIndex("Type")));
                clsMessageFormat.setDefault(cur.getString(cur.getColumnIndex("Default")));
                clsMessageFormat.setMessage_format(cur.getString(cur.getColumnIndex("MessageFormat")));
                clsMessageFormat.setRemark(cur.getString(cur.getColumnIndex("Remark")));
                list.add(clsMessageFormat);
            }

            db.close();

        } catch (Exception e) {
            Log.e("ClsItem", "ClsItem >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static int Delete(Context context, int delete_ID) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "DELETE FROM [MessageFormat_Master] WHERE [MessageFormat_ID] = "
                    .concat(String.valueOf(delete_ID))
                    .concat(";");

            Log.e("DELETE", "DELqRY-" + strSql);
            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsOrderDetailMaster", "Delete" + e.getMessage());
            e.getMessage();
        }
        return result;


    }


    @SuppressLint("WrongConstant")
    public static int Update(ClsMessageFormat objUpdate, String mode, SQLiteDatabase db) {
        int result = 0;
        try {

            if (mode.equalsIgnoreCase("Update All")) {
                // Update if we have default value is yes.
                // so that we don't have two default SmsSenderId's.
//                if (objUpdate.getDefault().equalsIgnoreCase("Yes")) {
//                    ClsMessageFormat clsMessageFormat;
//                    Log.e("ClsMessageFormat", "getType from Update" + objUpdate.getType());
//                    if (objUpdate.getType().equalsIgnoreCase("Sales")) {
//                        Log.e("ClsMessageFormat", "Inside type Sales");
//                        String where = " AND [Type] = 'Sales'";
//                        clsMessageFormat = getMessageFormatByDefault(db, where);
//                    } else {
//                        String where = " AND [Type] != 'Sales'";
//                        Log.e("ClsMessageFormat", "Inside type else");
//                        clsMessageFormat = getMessageFormatByDefault(db, where);
//                    }
//
//                    if (clsMessageFormat.getMessage_format().length() > 0) {
//                        // Updating status
//                        clsMessageFormat.setDefault("No");
//                        Update(clsMessageFormat, "Update default", db);
//                    }
//
//
//                }
                ClsMessageFormat clsMessageFormat = new ClsMessageFormat();
                if (objUpdate.getDefault().equalsIgnoreCase("Yes")) {
                    String where = " AND [Type] = '".concat(objUpdate.getType()).concat("'");
                    clsMessageFormat = getMessageFormatByDefault(db, where);
                }

                if (clsMessageFormat.getMessage_format().length() > 0) {

                    clsMessageFormat.setDefault("No");
                    Update(clsMessageFormat, "Update default", db);
                }

            }
//
//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
//            }

            Log.e("ClsMessageFormat", "isOpen" + db.isOpen());

            String strSql = "UPDATE [MessageFormat_Master] SET "
                    .concat("[Title] = ")
                    .concat("'")
                    .concat(objUpdate.getTitle().trim())
                    .concat("'")

                    .concat(" ,[MessageFormat] = ")
                    .concat("'")
                    .concat(objUpdate.getMessage_format().trim())
                    .concat("'")

                    .concat(" ,[Type] = ")
                    .concat("'")
                    .concat(objUpdate.getType().trim())
                    .concat("'")

                    .concat(" ,[Default] = ")
                    .concat("'")
                    .concat(objUpdate.getDefault().trim())
                    .concat("'")

                    .concat(" ,[Remark] = ")
                    .concat("'")
                    .concat(objUpdate.getRemark())
                    .concat("'")

                    .concat(" WHERE [MessageFormat_ID] = ")
                    .concat(String.valueOf(objUpdate.getId()))

                    .concat(";");
            Log.e("Update", strSql);
            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();
            //db.execSQL(strSql);
//            db.close();

        } catch (Exception e) {
            Log.e("ClsMessageFormat", "Update" + e.getMessage());
        }
        return result;
    }


    @SuppressLint("WrongConstant")
    public static ClsMessageFormat getQryByID(Context context) {

        ClsMessageFormat clsMessageFormat = new ClsMessageFormat();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT "
                    .concat("[MessageFormat_ID]")
                    .concat(",[Title]")
                    .concat(",[Type]")
                    .concat(",[Default]")
                    .concat(",[MessageFormat]")
                    .concat(",[Remark]")
                    .concat(" FROM [MessageFormat_Master] WHERE 1=1 ")
                    .concat(" AND [MessageFormat_ID] = ")
                    .concat(String.valueOf(1))
                    .concat(" ORDER BY [Title]");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--list--", "qry: " + qry);
            Log.e("--list--", "Count: " + cur.getCount());



            while (cur.moveToNext()) {
                clsMessageFormat.setId(cur.getInt(cur.getColumnIndex("MessageFormat_ID")));
                clsMessageFormat.setTitle(cur.getString(cur.getColumnIndex("Title")));
                clsMessageFormat.setType(cur.getString(cur.getColumnIndex("Type")));
                clsMessageFormat.setDefault(cur.getString(cur.getColumnIndex("Default")));
                clsMessageFormat.setMessage_format(cur.getString(cur.getColumnIndex("MessageFormat")));
                clsMessageFormat.setRemark(cur.getString(cur.getColumnIndex("Remark")));
            }

            db.close();

        } catch (Exception e) {
            Log.e("ClsItem", "ClsItem >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return clsMessageFormat;
    }

}
