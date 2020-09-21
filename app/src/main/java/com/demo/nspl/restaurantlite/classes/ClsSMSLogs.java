package com.demo.nspl.restaurantlite.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ClsSMSLogs {

    int LogId, orderId, credit;
    String orderNo = "", mobileNo = "", Customer_Name = "", Entry_Datetime = "", message = "",
            invoice_attachment = "", Status = "", Type = "", SendSMSID = "",
            SmsStatus = "", Remark = "",UtilizeType = "";

    boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    static Context context;
    private static SQLiteDatabase db;

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getSmsStatus() {
        return SmsStatus;
    }

    public void setSmsStatus(String smsStatus) {
        SmsStatus = smsStatus;
    }

    public String getSendSMSID() {
        return SendSMSID;
    }

    public void setSendSMSID(String sendSMSID) {
        SendSMSID = sendSMSID;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public int getLogId() {
        return LogId;
    }

    public void setLogId(int logId) {
        LogId = logId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCustomer_Name() {
        return Customer_Name;
    }

    public void setCustomer_Name(String customer_Name) {
        Customer_Name = customer_Name;
    }

    public String getEntry_Datetime() {
        return Entry_Datetime;
    }

    public void setEntry_Datetime(String entry_Datetime) {
        Entry_Datetime = entry_Datetime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getInvoice_attachment() {
        return invoice_attachment;
    }

    public void setInvoice_attachment(String invoice_attachment) {
        this.invoice_attachment = invoice_attachment;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }



    public String getUtilizeType() {
        return UtilizeType;
    }

    public void setUtilizeType(String utilizeType) {
        UtilizeType = utilizeType;
    }

    @SuppressLint("WrongConstant")
    public static int Insert(ClsSMSLogs Obj, Context context) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String qry = ("INSERT INTO [SalesSMSLogsMaster] " +
                    "([orderId],[orderNo],[mobileNo],[Customer_Name],[Entry_Datetime]" +
                    ",[message],[invoice_attachment],[Status],[UtilizeType],[SmsStatus],[Type],[Credit]" +
                    ",[SendSMSID],[Remark]) VALUES (")

                    .concat(String.valueOf(Obj.getOrderId()))
                    .concat(",")

                    .concat("'")
                    .concat(Obj.getOrderNo())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(Obj.getMobileNo().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(Obj.getCustomer_Name().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ClsGlobal.getCurruntDateTime())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(Obj.getMessage().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")


                    .concat("'")
                    .concat(Obj.getInvoice_attachment().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")


                    .concat("'")
                    .concat(Obj.getStatus().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")


                    .concat("'")
                    .concat(Obj.getUtilizeType().trim().replace("'", "''"))
                    .concat("'")
                    .concat(",")


                    .concat("'")
                    .concat(Obj.getSmsStatus().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(Obj.getType().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(Obj.getCredit()))
                    .concat(",")

                    .concat("'")
                    .concat(String.valueOf(Obj.getSendSMSID()))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat("")
                    .concat("'")


                    .concat(");");


            Log.e("ClsSMSLogs", "qry: " + qry);
            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.e("ClsSMSLogs", "result: " + result);

            Cursor c = db.rawQuery("SELECT last_insert_rowid()", null);
            c.moveToFirst();

            ClsGlobal.SalesSMSLogsMaster_LAST_INSERTED_ID = String.valueOf(c.getInt(0));//ID


            db.close();
            return result;
        } catch (Exception e) {
            db.close();
            Log.e("ClsSMSLogs", "Insert>>>>>>>>" + e.getMessage());
            e.getMessage();
        }
        return result;
    }


    @SuppressLint("WrongConstant")
    public static int DeleteAll(Context context) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String qry = "Delete From [SalesSMSLogsMaster]";

            Log.e("ClsSMSLogs", "qry: " + qry);
            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.e("ClsSMSLogs", "result: " + result);

            db.close();
            return result;
        } catch (Exception e) {
            db.close();
            Log.e("ClsSMSLogs", "Insert>>>>>>>>" + e.getMessage());
            e.getMessage();
        }
        return result;
    }

    @SuppressLint("WrongConstant")
    public static int DeleteAllAfterTime(Context context) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String qry = "Delete From [SalesSMSLogsMaster] where [Entry_Datetime] " +
                    ">= datetime('now','-1 day')";

            Log.e("ClsSMSLogs", "qry: " + qry);
            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.e("ClsSMSLogs", "result: " + result);

            db.close();
            return result;
        } catch (Exception e) {
            db.close();
            Log.e("ClsSMSLogs", "Insert>>>>>>>>" + e.getMessage());
            e.getMessage();
        }
        return result;
    }


    @SuppressLint("WrongConstant")
    public static int DeleteByOrderIdAndOrderNo(Context context, String orderId, String orderNo) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String qry = "Delete From [SalesSMSLogsMaster] where [orderId] = " + orderId
                    .concat("AND [orderNo] = '".concat(orderNo).concat("'"));


            Log.e("ClsSMSLogs", "qry: " + qry);
            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.e("ClsSMSLogs", "result: " + result);

            db.close();
            return result;
        } catch (Exception e) {
            db.close();
            Log.e("ClsSMSLogs", "Insert>>>>>>>>" + e.getMessage());
            e.getMessage();
        }
        return result;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsSMSLogs> getList(String _where, String paging, SQLiteDatabase db) {
        List<ClsSMSLogs> list = new ArrayList<>();

        try {
//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT *"
                    .concat(" FROM [SalesSMSLogsMaster] WHERE 1=1 ")
                    .concat(_where)
                    .concat(" ORDER BY [Entry_Datetime] DESC ")
                    .concat(paging);

            Cursor cur = db.rawQuery(qry, null);
            Log.e("--Test--", "qry: " + qry);

            Log.e("--Test--", "count: " + cur.getCount());

            while (cur.moveToNext()) {
                ClsSMSLogs getSet = new ClsSMSLogs();
                getSet.setLogId(cur.getInt(cur.getColumnIndex("LogId")));
                getSet.setOrderId(cur.getInt(cur.getColumnIndex("orderId")));
                getSet.setOrderNo(cur.getString(cur.getColumnIndex("orderNo")));
                getSet.setMobileNo(cur.getString(cur.getColumnIndex("mobileNo")));
                getSet.setCustomer_Name(cur.getString(cur.getColumnIndex("Customer_Name")));
                getSet.setEntry_Datetime(cur.getString(cur.getColumnIndex("Entry_Datetime")));
                getSet.setMessage(cur.getString(cur.getColumnIndex("message")));
                getSet.setInvoice_attachment(cur.getString(cur.getColumnIndex("invoice_attachment")));
                getSet.setStatus(cur.getString(cur.getColumnIndex("Status")));
                getSet.setType(cur.getString(cur.getColumnIndex("Type")));
                getSet.setCredit(cur.getInt(cur.getColumnIndex("Credit")));
                getSet.setSendSMSID(cur.getString(cur.getColumnIndex("SendSMSID")));
                getSet.setSmsStatus(cur.getString(cur.getColumnIndex("SmsStatus")));
                getSet.setRemark(cur.getString(cur.getColumnIndex("Remark")));
                getSet.setUtilizeType(cur.getString(cur.getColumnIndex("UtilizeType")));
                list.add(getSet);
            }

            Gson gson = new Gson();
            String jsonInString = gson.toJson(list);
            Log.e("--ITEM--", "Item: " + jsonInString);

        } catch (Exception e) {
            Log.e("ClsSMSLogs", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static List<String> getPendingSendSmsId(SQLiteDatabase db) {
        List<String> list = new ArrayList<>();

        try {
//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT [SendSMSID]"
                    .concat(" FROM [SalesSMSLogsMaster] WHERE 1=1 ")
                    .concat(" AND [Status] = 'Pending'")
                    .concat(" GROUP BY [SendSMSID] ")
                    .concat(" ORDER BY [Entry_Datetime] ");


            Cursor cur = db.rawQuery(qry, null);
            Log.e("ClsSMSLogs", "qry " + qry);

            Log.e("ClsSMSLogs", "cur count " + cur.getCount());

            while (cur.moveToNext()) {
                list.add(cur.getString(cur.getColumnIndex("SendSMSID")));
            }

        } catch (Exception e) {
            Log.e("ClsSMSLogs", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static int Update(ClsSMSLogs obj, Context context, SQLiteDatabase db) {
        int result = 0;
        try {


            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String strSql = "UPDATE [SalesSMSLogsMaster] SET "

                    .concat(" [orderId] = ")
                    .concat(String.valueOf(obj.getOrderId()))

                    .concat(" ,[orderNo] = ")
                    .concat("'")
                    .concat(obj.getOrderNo())
                    .concat("'")

                    .concat(" ,[mobileNo] = ")
                    .concat("'")
                    .concat(obj.getMobileNo())
                    .concat("'")

                    .concat(" ,[Customer_Name] = ")
                    .concat("'")
                    .concat(obj.getCustomer_Name())
                    .concat("'")

                    .concat(" ,[Entry_Datetime] = ")
                    .concat("'")
                    .concat(obj.getEntry_Datetime())
                    .concat("'")

                    .concat(" ,[message] = ")
                    .concat("'")
                    .concat(obj.getMessage().replace("'", "''"))
                    .concat("'")

                    .concat(" ,[invoice_attachment] = ")
                    .concat("'")
                    .concat(obj.getInvoice_attachment())
                    .concat("'")

                    .concat(" ,[Status] = ")
                    .concat("'")
                    .concat(obj.getStatus())
                    .concat("'")

                    .concat(" ,[SmsStatus] = ")
                    .concat("'")
                    .concat(obj.getSmsStatus())
                    .concat("'")

                    .concat(" ,[Type] = ")
                    .concat("'")
                    .concat(obj.getType())
                    .concat("'")

                    .concat(" ,[Credit] = ")
                    .concat("'")
                    .concat(String.valueOf((int) Math.ceil((double)
                            obj.getMessage().length() / 145)))
                    .concat("'")

                    .concat(" ,[Remark] = ")
                    .concat("'")
                    .concat(obj.getRemark().replace("'", "''"))
                    .concat("'")

                    .concat(" ,[SendSMSID] = ")
                    .concat("'")
                    .concat(String.valueOf(obj.getSendSMSID()))
                    .concat("'")


                    .concat(" WHERE [LogId] = ")
                    .concat(String.valueOf(obj.getLogId()))
                    .concat(";");


//            SendSMSID
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
    public static List<ClsSMSLogs> getSmsLogCustomerList(String _where,SQLiteDatabase db) {
        List<ClsSMSLogs> list = new ArrayList<>();

        try {
            String qry = "SELECT "
                    .concat("[Customer_Name]")
                    .concat(",[mobileNo]")
                    .concat(" FROM [SalesSMSLogsMaster] WHERE 1=1 ")
                    .concat(_where)
                    .concat(" GROUP BY [mobileNo] ");

            Cursor cur = db.rawQuery(qry, null);
            Log.e("--bulkID--", "qry " + qry);
            Log.e("--bulkID--", "cur " + cur.getCount());

            while (cur.moveToNext()) {
                ClsSMSLogs getSet = new ClsSMSLogs();
                getSet.setMobileNo(cur.getString(cur.getColumnIndex("mobileNo")));
                getSet.setCustomer_Name(cur.getString(cur.getColumnIndex("Customer_Name")));
                list.add(getSet);
            }
        } catch (Exception e) {
            Log.e("--bulkID--", "Exception: " + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsSMSLogs> getSmsLogForStatus(String _where,SQLiteDatabase db) {
        List<ClsSMSLogs> list = new ArrayList<>();

        try {
            String qry = "SELECT "
                    .concat("[Status]")
                    .concat(" FROM [SalesSMSLogsMaster] WHERE 1=1 ")
                    .concat(_where)
                    .concat(" GROUP BY [Status] ");

            Cursor cur = db.rawQuery(qry, null);
            Log.e("--bulkID--", "qry " + qry);
            Log.e("--bulkID--", "cur " + cur.getCount());

            while (cur.moveToNext()) {
                ClsSMSLogs getSet = new ClsSMSLogs();
                getSet.setStatus(cur.getString(cur.getColumnIndex("Status")));
                list.add(getSet);
            }
        } catch (Exception e) {
            Log.e("--bulkID--", "Exception: " + e.getMessage());
            e.getMessage();
        }
        return list;
    }





    @SuppressLint("WrongConstant")
    public static int UpdateStatus(ClsSMSLogs obj, Context context, SQLiteDatabase db) {
        int result = 0;
        try {


//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String strSql = "UPDATE [SalesSMSLogsMaster] SET "

                    .concat(" [orderId] = ")
                    .concat(String.valueOf(obj.getOrderId()))

                    .concat(" ,[orderNo] = ")
                    .concat("'")
                    .concat(obj.getOrderNo())
                    .concat("'")

                    .concat(" ,[mobileNo] = ")
                    .concat("'")
                    .concat(obj.getMobileNo())
                    .concat("'")

                    .concat(" ,[Customer_Name] = ")
                    .concat("'")
                    .concat(obj.getCustomer_Name())
                    .concat("'")

                    .concat(" ,[Entry_Datetime] = ")
                    .concat("'")
                    .concat(obj.getEntry_Datetime())
                    .concat("'")

                    .concat(" ,[message] = ")
                    .concat("'")
                    .concat(obj.getMessage())
                    .concat("'")

                    .concat(" ,[invoice_attachment] = ")
                    .concat("'")
                    .concat(obj.getInvoice_attachment())
                    .concat("'")

                    .concat(" ,[Status] = ")
                    .concat("'")
                    .concat(obj.getStatus())
                    .concat("'")

                    .concat(" ,[UtilizeType] = ")
                    .concat("'")
                    .concat(obj.getUtilizeType())
                    .concat("'")

                    .concat(" ,[Type] = ")
                    .concat("'")
                    .concat(obj.getType())
                    .concat("'")

                    .concat(" ,[Credit] = ")
                    .concat("'")
                    .concat(String.valueOf((int) Math.ceil((double)
                            obj.getMessage().length() / 145)))
                    .concat("'")

                    .concat(" ,[SendSMSID] = ")
                    .concat("'")
                    .concat(String.valueOf(obj.getSendSMSID()))
                    .concat("'")


                    .concat(" WHERE [LogId] = ")
                    .concat(String.valueOf(obj.getLogId()))
                    .concat(";");


//            SendSMSID
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
    public static void UpdateSmsSendingStatus(Context context, String _mode) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);


            String sqlStr = "UPDATE [SalesSMSLogsMaster] SET "
                    .concat("[Status]='Time Expired' ")
                    .concat(" WHERE upper([Status])=upper('Pending') ")
                    .concat(" AND [Entry_Datetime] < ")
                    .concat("'")
                    .concat(ClsGlobal.getEntryDateForExpiredAllData())
                    .concat("'");


            Log.e("--updateStatus--", "Update: " + sqlStr + " :   " + _mode);
            SQLiteStatement statement = db.compileStatement(sqlStr);
            result = statement.executeUpdateDelete();
            Log.e("--updateStatus--", "result: " + result);
        } catch (Exception e) {
            Log.e("--updateStatus--", "Exception: " + e.getMessage());
        }

    }


    @SuppressLint("WrongConstant")
    public static List<String> getSalesSMSLogsMasterColumn(Context context,io.requery.android.database
            .sqlite.SQLiteDatabase db) {
        List<String> columns = new ArrayList<>();
        try {
//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

//            io.requery.android.database.sqlite.SQLiteDatabase db;
//
//            db = io.requery.android.database.sqlite.SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath(ClsGlobal.Database_Name).getPath(),
//                    null);

            String qry = "SELECT * FROM [SalesSMSLogsMaster] LIMIT 1 ";
            Cursor cur = db.rawQuery(qry, null);
            Log.e("--Select--", ">>>>>>>>>" + cur.getCount());
            if (cur != null) {
                for (String c : cur.getColumnNames()) {
                    columns.add(c);
                }
            }
//            db.close();
        } catch (Exception e) {
            Log.e("SalesSMSLogsMaster", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }
        return columns;
    }



    @SuppressLint("WrongConstant")
    public static int InsertList(List<ClsSMSLogs> list, Context context) {
        int result = 0;
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            if (list != null && list.size() > 0) {
                for (ClsSMSLogs item : list) {
                    String qry = ("INSERT INTO [SalesSMSLogsMaster] " +
                            "([orderId],[orderNo],[mobileNo],[Customer_Name],[Entry_Datetime]" +
                            ",[message],[invoice_attachment],[Status],[UtilizeType],[SmsStatus],[Type],[Credit]" +
                            ",[SendSMSID],[Remark]) VALUES (")

                            .concat(String.valueOf(item.getOrderId()))
                            .concat(",")

                            .concat("'")
                            .concat(item.getOrderNo())
                            .concat("'")
                            .concat(",")

                            .concat("'")
                            .concat(item.getMobileNo().trim()
                                    .replace("'", "''"))
                            .concat("'")
                            .concat(",")

                            .concat("'")
                            .concat(item.getCustomer_Name().trim()
                                    .replace("'", "''"))
                            .concat("'")
                            .concat(",")

                            .concat("'")
                            .concat(ClsGlobal.getCurruntDateTime())
                            .concat("'")
                            .concat(",")

                            .concat("'")
                            .concat(item.getMessage().trim()
                                    .replace("'", "''"))
                            .concat("'")
                            .concat(",")


                            .concat("'")
                            .concat(item.getInvoice_attachment().trim()
                                    .replace("'", "''"))
                            .concat("'")
                            .concat(",")


                            .concat("'")
                            .concat(item.getStatus().trim()
                                    .replace("'", "''"))
                            .concat("'")
                            .concat(",")


                            .concat("'")
                            .concat(item.getUtilizeType().trim().replace("'", "''"))
                            .concat("'")
                            .concat(",")


                            .concat("'")
                            .concat(item.getSmsStatus().trim()
                                    .replace("'", "''"))
                            .concat("'")
                            .concat(",")

                            .concat("'")
                            .concat(item.getType().trim()
                                    .replace("'", "''"))
                            .concat("'")
                            .concat(",")

                            .concat(String.valueOf(item.getCredit()))
                            .concat(",")

                            .concat("'")
                            .concat(String.valueOf(item.getSendSMSID()))
                            .concat("'")
                            .concat(",")

                            .concat("'")
                            .concat("")
                            .concat("'")


                            .concat(");");


                    Log.e("ClsSMSLogs", "qry: " + qry);
                    SQLiteStatement statement = db.compileStatement(qry);
                    result = statement.executeUpdateDelete();
                    Log.e("ClsSMSLogs", "result: " + result);
                }
            }


//            Cursor c = db.rawQuery("SELECT last_insert_rowid()", null);
//            c.moveToFirst();
//
//            ClsGlobal.SalesSMSLogsMaster_LAST_INSERTED_ID
//                    = String.valueOf(c.getInt(0));//ID

        } catch (Exception e) {

            Log.e("ClsSMSLogs", "Insert>>>>>>>>" + e.getMessage());
            e.getMessage();
        } finally {
            db.close();
        }
        return result;
    }


}
