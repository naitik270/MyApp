package com.demo.nspl.restaurantlite.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.demo.nspl.restaurantlite.classes.ClsBulkSMSLog.TotalCredit_UtilizedById;

public class ClsSMSBulkMaster implements Serializable {

    int bulkID = 0;
    int MessageLength = 0;
    int TotalCustomers = 0;

    public int getTotalCredit_Utilized() {
        return TotalCredit_Utilized;
    }

    public void setTotalCredit_Utilized(int totalCredit_Utilized) {
        TotalCredit_Utilized = totalCredit_Utilized;
    }

    int TotalCredit_Utilized = 0;
    String serverBulkID = "", Message = "", GroupName = "",
            SendDate = "", SenderID = "", MessageType = "",
            Title = "";


    boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private static SQLiteDatabase db;
    private static Context context;

    public int getBulkID() {
        return bulkID;
    }

    public void setBulkID(int bulkID) {
        this.bulkID = bulkID;
    }

    public int getMessageLength() {
        return MessageLength;
    }

    public void setMessageLength(int messageLength) {
        MessageLength = messageLength;
    }

    public int getTotalCustomers() {
        return TotalCustomers;
    }

    public void setTotalCustomers(int totalCustomers) {
        TotalCustomers = totalCustomers;
    }

    public String getServerBulkID() {
        return serverBulkID;
    }

    public void setServerBulkID(String serverBulkID) {
        this.serverBulkID = serverBulkID;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getSendDate() {
        return SendDate;
    }

    public void setSendDate(String sendDate) {
        SendDate = sendDate;
    }

    public String getSenderID() {
        return SenderID;
    }

    public void setSenderID(String senderID) {
        SenderID = senderID;
    }

    public String getMessageType() {
        return MessageType;
    }

    public void setMessageType(String messageType) {
        MessageType = messageType;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    @SuppressLint("WrongConstant")
    public static int Insert(ClsSMSBulkMaster insertObj, Context context) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String qry = ("INSERT INTO [SMSBulkMaster] " +
                    "([serverBulkID],[Message],[MessageLength],[GroupName],[TotalCustomers],[SendDate]" +
                    ",[SenderID],[MessageType],[Title]) VALUES ('")

                    .concat(insertObj.getServerBulkID()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(insertObj.getMessage().replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(insertObj.getMessageLength()))
                    .concat(",")

                    .concat("'")
                    .concat(insertObj.getGroupName().replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(insertObj.getTotalCustomers()))
                    .concat(",")

                    .concat("'")
                    .concat(ClsGlobal.getEntryDateTime())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(insertObj.getSenderID().replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(insertObj.getMessageType().replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(insertObj.getTitle().replace("'", "''"))
                    .concat("'")
                    .concat(");");

            Log.e("--Insert--", "Qry: " + qry);

            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();

            Cursor c = db.rawQuery("SELECT last_insert_rowid()", null);
            c.moveToFirst();
//            int insertedID = c.getInt(0);//orderiD
            ClsGlobal.last_id_SMSBulkMaster = c.getInt(0);//orderiD

            db.close();
            return result;
        } catch (Exception e) {
            db.close();
            Log.e("ClsSMSBulkMaster", "Insert>>>>>>>>" + e.getMessage());
            e.getMessage();
        }
        return result;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsSMSBulkMaster> getList(String _where, String paging,Context context,
                                                 SQLiteDatabase db) {
        List<ClsSMSBulkMaster> list = new ArrayList<>();

        try {
//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT [bulkID],[serverBulkID],[GroupName],[TotalCustomers] " +
                    " ,[SendDate],[SenderID] ,[MessageType],[Title] "
                            .concat(" FROM [SMSBulkMaster] WHERE 1=1 ")
                            .concat(_where)
                            .concat(" ORDER BY [SendDate] DESC ")
                            .concat(paging);

            Log.e("ClsSMSBulkMaster", "qry " + qry);

            Cursor cur = db.rawQuery(qry, null);
            Log.e("ClsSMSBulkMaster", "cur " + cur.getCount());

            while (cur.moveToNext()) {

                ClsSMSBulkMaster getSet = new ClsSMSBulkMaster();
                getSet.setBulkID(cur.getInt(cur.getColumnIndex("bulkID")));
                getSet.setSenderID(cur.getString(cur.getColumnIndex("serverBulkID")));
                getSet.setGroupName(cur.getString(cur.getColumnIndex("GroupName")));
                getSet.setTotalCustomers(cur.getInt(cur.getColumnIndex("TotalCustomers")));
                getSet.setSendDate(cur.getString(cur.getColumnIndex("SendDate")));
                getSet.setSenderID(cur.getString(cur.getColumnIndex("SenderID")));
                getSet.setTotalCredit_Utilized(TotalCredit_UtilizedById(
                        context,getSet.getBulkID(),db));
                getSet.setMessageType(cur.getString(cur.getColumnIndex("MessageType")));
                getSet.setTitle(cur.getString(cur.getColumnIndex("Title")));
//                getSet.setRemark(cur.getString(cur.getColumnIndex("Remark")));
                list.add(getSet);
            }

//            db.close();
        } catch (Exception e) {
            Log.e("ClsSMSBulkMaster", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsSMSBulkMaster> getSmsTitleList(SQLiteDatabase db) {
        List<ClsSMSBulkMaster> list = new ArrayList<>();

        try {
            String qry = "SELECT "
                    .concat("[Title]")
                    .concat(" FROM [SMSBulkMaster] WHERE 1=1 ")
                    .concat(" GROUP BY [Title]");

            Cursor cur = db.rawQuery(qry, null);
            Log.e("--status--", "qry: " + qry);
            Log.e("--status--", "cur: " + cur.getCount());

            while (cur.moveToNext()) {
                ClsSMSBulkMaster getSet = new ClsSMSBulkMaster();
                getSet.setTitle(cur.getString(cur.getColumnIndex("Title")));
                list.add(getSet);
            }
        } catch (Exception e) {
            Log.e("--bulkID--", "Exception: " + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsSMSBulkMaster> getSmsGroupList(SQLiteDatabase db) {
        List<ClsSMSBulkMaster> list = new ArrayList<>();

        try {
            String qry = "SELECT "
                    .concat("[GroupName]")
                    .concat(" FROM [SMSBulkMaster] WHERE 1=1 ")
                    .concat(" GROUP BY [GroupName]");

            Cursor cur = db.rawQuery(qry, null);
            Log.e("--status--", "qry: " + qry);
            Log.e("--status--", "cur: " + cur.getCount());

            while (cur.moveToNext()) {
                ClsSMSBulkMaster getSet = new ClsSMSBulkMaster();
                getSet.setGroupName(cur.getString(cur.getColumnIndex("GroupName")));
                list.add(getSet);
            }
        } catch (Exception e) {
            Log.e("--bulkID--", "Exception: " + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsSMSBulkMaster> getSmsSenderIDList(SQLiteDatabase db) {
        List<ClsSMSBulkMaster> list = new ArrayList<>();

        try {
            String qry = "SELECT "
                    .concat("[SenderID]")
                    .concat(" FROM [SMSBulkMaster] WHERE 1=1 ")
                    .concat(" GROUP BY [SenderID]");

            Cursor cur = db.rawQuery(qry, null);
            Log.e("--status--", "qry: " + qry);
            Log.e("--status--", "cur: " + cur.getCount());

            while (cur.moveToNext()) {
                ClsSMSBulkMaster getSet = new ClsSMSBulkMaster();
                getSet.setSenderID(cur.getString(cur.getColumnIndex("SenderID")));
                list.add(getSet);
            }
        } catch (Exception e) {
            Log.e("--bulkID--", "Exception: " + e.getMessage());
            e.getMessage();
        }
        return list;
    }




    @SuppressLint("WrongConstant")
    public static int Update(ClsSMSBulkMaster obj, SQLiteDatabase db) {
        int result = 0;
        try {


//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name,
//                    context.MODE_APPEND, null);

            String strSql = "UPDATE [SMSBulkMaster] SET "
                    .concat("[serverBulkID] = ")
                    .concat("'")
                    .concat(String.valueOf(obj.getServerBulkID() == null ? "Pending Status"
                            : obj.getServerBulkID()))
                    .concat("'")


                    .concat(" WHERE [bulkID] = ")
                    .concat(String.valueOf(obj.getBulkID()))
                    .concat(";");

            Log.e("--Update--", "strSql: " + strSql);

            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();
            Log.e("--Update--", "result: " + result);


//            db.close();

        } catch (Exception e) {
            Log.e("--Update--", "Exception: " + e.getMessage());
        }
        return result;
    }

}
