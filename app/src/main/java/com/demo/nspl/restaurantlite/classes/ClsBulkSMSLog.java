package com.demo.nspl.restaurantlite.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import android.util.Log;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class ClsBulkSMSLog {
//
//    public int id;
//    public Integer image = null;
//    public Drawable imageDrw;
//    public String from;
//    public String email;
////    public String message;
//    public String date;
//    public int color = -1;


    int logID, bulkID, CreditCount, StatusCode;
    String Mobile = "";
    String CustomerName = "";
    String Status = "";
    String StatusDateTime = "";
    String serverBulkID = "";
    String Remark = "";

    String UtilizeType = "";

    String message = "";


    boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private static SQLiteDatabase db;
    private static Context context;

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getServerBulkID() {
        return serverBulkID;
    }

    public void setServerBulkID(String serverBulkID) {
        this.serverBulkID = serverBulkID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLogID() {
        return logID;
    }

    public void setLogID(int logID) {
        this.logID = logID;
    }

    public int getBulkID() {
        return bulkID;
    }

    public void setBulkID(int bulkID) {
        this.bulkID = bulkID;
    }

    public int getCreditCount() {
        return CreditCount;
    }

    public void setCreditCount(int creditCount) {
        CreditCount = creditCount;
    }

    public int getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(int statusCode) {
        StatusCode = statusCode;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getStatusDateTime() {
        return StatusDateTime;
    }

    public void setStatusDateTime(String statusDateTime) {
        StatusDateTime = statusDateTime;
    }

    public String getUtilizeType() {
        return UtilizeType;
    }

    public void setUtilizeType(String utilizeType) {
        UtilizeType = utilizeType;
    }


    @SuppressLint("WrongConstant")
    public static int Insert(List<ClsBulkSMSLog> listInsertObj, Context context, SQLiteDatabase db) {
        int result = 0;
        try {

//            db =context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            for (ClsBulkSMSLog insertObj : listInsertObj) {
                String qry = ("INSERT INTO [SMSLog] " +
                        "([bulkID],[Mobile],[CustomerName],[CreditCount],[Status],[StatusDateTime]" +
                        ",[StatusCode],[Message],[serverBulkID],[Remark]) VALUES (")
                        .concat(String.valueOf(insertObj.getBulkID()))
                        .concat(",")

                        .concat("'")
                        .concat(insertObj.getMobile()
                                .replace("'", "''"))
                        .concat("'")
                        .concat(",")

                        .concat("'")
                        .concat(insertObj.getCustomerName()
                                .replace("'", "''"))
                        .concat("'")
                        .concat(",")

                        .concat(String.valueOf(insertObj.getCreditCount()))
                        .concat(",")

                        .concat("'")
                        .concat(String.valueOf(insertObj.getStatus())
                                .replace("'", "''"))
                        .concat("'")
                        .concat(",")

                        .concat("'")
                        .concat(ClsGlobal.getEntryDateTime())
                        .concat("'")
                        .concat(",")


                        .concat(String.valueOf(insertObj.getStatusCode()))
                        .concat(",")

                        .concat("'")
                        .concat(insertObj.getMessage()
                                .replace("'", "''"))
                        .concat("'")
                        .concat(",")


                        .concat("'")
                        .concat("")
                        .concat("'")
                        .concat(",")

                        .concat("'")
                        .concat("")
                        .concat("'")

                        .concat(");");

                SQLiteStatement statement = db.compileStatement(qry);
                result = statement.executeUpdateDelete();

                Log.e("--lstAsynctask--", "qry: " + qry);
                Log.e("--lstAsynctask--", "result_result: " + result);
            }

//            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsBulkSMSLog", "Insert>>>>>>>>" + e.getMessage());
            e.getMessage();
        }
        return result;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsBulkSMSLog> getList(String _where, String paging, SQLiteDatabase db) {
        List<ClsBulkSMSLog> list = new ArrayList<>();


        Log.e("--list--", "getList_getList_getList_getList");


        try {
//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT * ".concat(" FROM [SMSLog] WHERE 1=1 ")
                    .concat(_where)
                    .concat(" ORDER BY [StatusDateTime] DESC ")
                    .concat(paging);

            Cursor cur = db.rawQuery(qry, null);
            Log.e("--list--", "qry: " + qry);
            Log.e("--list--", "cur: " + cur.getCount());

            while (cur.moveToNext()) {
                ClsBulkSMSLog getSet = new ClsBulkSMSLog();

                getSet.setLogID(cur.getInt(cur.getColumnIndex("logID")));
                getSet.setBulkID(cur.getInt(cur.getColumnIndex("bulkID")));
                getSet.setMobile(cur.getString(cur.getColumnIndex("Mobile")));
                getSet.setCustomerName(cur.getString(cur.getColumnIndex("CustomerName")));
                getSet.setCreditCount(cur.getInt(cur.getColumnIndex("CreditCount")));
                getSet.setStatus(cur.getString(cur.getColumnIndex("Status")));
                getSet.setStatusDateTime(cur.getString(cur.getColumnIndex("StatusDateTime")));
                getSet.setStatusCode(cur.getInt(cur.getColumnIndex("StatusCode")));
                getSet.setMessage(cur.getString(cur.getColumnIndex("Message")));
                getSet.setServerBulkID(cur.getString(cur.getColumnIndex("serverBulkID")));
                getSet.setRemark(cur.getString(cur.getColumnIndex("Remark")));

                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("ClsBulkSMSLog", "GetList SMSLog " + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static int TotalCredit_UtilizedById(Context context, int id, SQLiteDatabase db) {
        int result = 0;
        int total = 0;
        try {

            String qry = "";

//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name,
//                    Context.MODE_APPEND, null);

            qry = "SELECT sum(CreditCount) as [totalCredit]".concat(" " +
                    "FROM [SMSLog] WHERE 1=1 ")
                    .concat(" AND [bulkID]  = " + id + "" +
                            " AND [Status] = 'Delivered' ");
            Cursor cur = db.rawQuery(qry, null);

            if (cur.moveToFirst()) {
                total = cur.getInt(cur.getColumnIndex("totalCredit"));
            }
            Log.e("--Update--", "qry: " + qry);
            Log.e("--Update--", "cur: " + cur.getCount());

//            db.close();

        } catch (Exception e) {
            Log.e("--Update--", "Exception: " + e.getMessage());
        }
        return total;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsBulkSMSLog> getCustomerListUsingBulkID(int bulkID, SQLiteDatabase db) {
        List<ClsBulkSMSLog> list = new ArrayList<>();

        try {
            String qry = "SELECT "
                    .concat("[CustomerName]")
                    .concat(",[Mobile]")
                    .concat(" FROM [SMSLog] WHERE 1=1 ")
                    .concat(" AND  [bulkID] = " + bulkID)
                    .concat(" GROUP BY [Mobile] ");

            Cursor cur = db.rawQuery(qry, null);
            Log.e("--bulkID--", "qry " + qry);
            Log.e("--bulkID--", "cur " + cur.getCount());

            while (cur.moveToNext()) {
                ClsBulkSMSLog getSet = new ClsBulkSMSLog();
                getSet.setMobile(cur.getString(cur.getColumnIndex("Mobile")));
                getSet.setCustomerName(cur.getString(cur.getColumnIndex("CustomerName")));
                list.add(getSet);
            }
        } catch (Exception e) {
            Log.e("--bulkID--", "Exception: " + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsBulkSMSLog> getStatusListUsingBulkID(int bulkID, SQLiteDatabase db) {
        List<ClsBulkSMSLog> list = new ArrayList<>();

        try {
            String qry = "SELECT "
                    .concat("[Status]")
                    .concat(",[StatusCode]")
                    .concat(" FROM [SMSLog] WHERE 1=1 ")
                    .concat(" AND  [bulkID] = " + bulkID)
                    .concat(" GROUP BY [Status]");

            Cursor cur = db.rawQuery(qry, null);
            Log.e("--status--", "qry: " + qry);
            Log.e("--status--", "cur: " + cur.getCount());

            while (cur.moveToNext()) {
                ClsBulkSMSLog getSet = new ClsBulkSMSLog();
                getSet.setStatus(cur.getString(cur.getColumnIndex("Status")));
                getSet.setStatusCode(cur.getInt(cur.getColumnIndex("StatusCode")));
                list.add(getSet);
            }
        } catch (Exception e) {
            Log.e("--bulkID--", "Exception: " + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static ClsSummery getSummery(int bulkId, SQLiteDatabase db) {
        ClsSummery clsSummery = new ClsSummery();

        try {

            // -------------- For TotalCustomer -------------------------
            String qry = "SELECT count(*) ".concat(" FROM [SMSLog] WHERE 1=1 ")
                    .concat(" AND [bulkID]  =" + bulkId);

            Cursor cur = db.rawQuery(qry, null);
            Log.e("ClsBulkSMSLog", "qry " + qry);
            Log.e("ClsBulkSMSLog", "cur " + cur.getCount());
            cur.moveToFirst();
            clsSummery.setTotalCustomer(cur.getInt(0));
            cur.close();

            // -------------- For CreditCount -------------------------
            qry = "SELECT sum(CreditCount) as [totalCredit]".concat(" FROM [SMSLog] WHERE 1=1 ")
                    .concat(" AND [bulkID]  =" + bulkId + " AND [Status] = 'Delivered' ");
            cur = db.rawQuery(qry, null);

            if (cur.moveToFirst()) {
                int total = cur.getInt(cur.getColumnIndex("totalCredit"));
                clsSummery.setCreditUtilized(total);
            }

            // -------------- For Delivered -------------------------
            qry = "SELECT count(*) as [totalCredit]".concat(" FROM [SMSLog] WHERE 1=1 ")
                    .concat(" AND [bulkID]  =" + bulkId + " AND [Status] ='Delivered'");
            cur = db.rawQuery(qry, null);
            cur.moveToFirst();
            clsSummery.setDelivered(cur.getInt(0));

            // -------------- For Pending -------------------------
            qry = "SELECT count(*) as [totalCredit]".concat(" FROM [SMSLog] WHERE 1=1 ")
                    .concat(" AND [bulkID]  =" + bulkId + " AND [Status] ='Pending'");
            cur = db.rawQuery(qry, null);
            cur.moveToFirst();

            clsSummery.setPending(cur.getInt(0));

            // -------------- For DND -------------------------
            qry = "SELECT count(*) as [totalCredit]".concat(" FROM [SMSLog] WHERE 1=1 ")
                    .concat(" AND [bulkID]  =" + bulkId + " AND [Status] ='DND'");
            cur = db.rawQuery(qry, null);
            cur.moveToFirst();

            clsSummery.setDND(cur.getInt(0));

            // -------------- For Failed -------------------------
            qry = "SELECT count(*) as [totalCredit]".concat(" FROM [SMSLog] WHERE 1=1 ")
                    .concat(" AND [bulkID]  =" + bulkId + " AND [Status] = 'Failed'");
            cur = db.rawQuery(qry, null);
            cur.moveToFirst();

            clsSummery.setFailed(cur.getInt(0));


        } catch (Exception e) {
            Log.e("ClsBulkSMSLog", "GetList SMSLog " + e.getMessage());
            e.getMessage();
        }
        return clsSummery;
    }


    @SuppressLint("WrongConstant")
    public static List<String> getPendingServerBulkID(android.database.sqlite.SQLiteDatabase db) {
        List<String> list = new ArrayList<>();

        try {
//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT [serverBulkID]"
                    .concat(" FROM [SMSLog] WHERE 1=1 ")
                    .concat(" AND [Status] = 'Pending' " +
                            "or [Status] = 'Sending'")
                    .concat(" GROUP BY [serverBulkID] ")
                    .concat(" ORDER BY [StatusDateTime] ");


            Cursor cur = db.rawQuery(qry, null);
            Log.e("ClsSMSLogs", "qry " + qry);

            Log.e("ClsSMSLogs", "cur count " + cur.getCount());

            while (cur.moveToNext()) {
                list.add(cur.getString(cur.getColumnIndex("serverBulkID")));
            }

        } catch (Exception e) {
            Log.e("ClsSMSLogs", "GetList: Test " + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static int Update(ClsBulkSMSLog obj, Context context) {
        int result = 0;
        try {


            db = context.openOrCreateDatabase(ClsGlobal.Database_Name,
                    context.MODE_APPEND, null);

            String strSql = "UPDATE [SMSLog] SET "
                    .concat("[bulkID] = ")
                    .concat(String.valueOf(obj.getBulkID()))


                    .concat(" ,[Mobile] = ")
                    .concat("'")
                    .concat(String.valueOf(obj.getMobile()))
                    .concat("'")

                    .concat(" ,[CustomerName] = ")
                    .concat("'")
                    .concat(obj.getCustomerName().trim()
                            .replace("'", "''"))
                    .concat("'")

                    .concat(" ,[CreditCount] = ")
                    .concat(String.valueOf(obj.getCreditCount()))


                    .concat(" ,[Status] = ")
                    .concat("'")
                    .concat(obj.getStatus().trim()
                            .replace("'", "''"))
                    .concat("'")

                    .concat(" ,[StatusDateTime] = ")
                    .concat("'")
                    .concat(obj.getStatusDateTime().trim())
                    .concat("'")

                    .concat(" ,[StatusCode] = ")
                    .concat(String.valueOf(obj.getStatusCode()))

                    .concat(" ,[Message] = ")
                    .concat("'")
                    .concat(obj.getMessage().trim())
                    .concat("'")


                    .concat(" ,[serverBulkID] = ")
                    .concat("'")
                    .concat(obj.getServerBulkID().trim())
                    .concat("'")


                    .concat(" WHERE [logID] = ")
                    .concat(String.valueOf(obj.getLogID()))
                    .concat(";");

            Log.e("--Update--", "strSql: " + strSql);

            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();
            Log.e("--Update--", "result: " + result);


            db.close();

        } catch (Exception e) {
            Log.e("--Update--", "Exception: " + e.getMessage());
        }
        return result;
    }

    @SuppressLint("WrongConstant")
    public static int UpdateList(List<ClsBulkSMSLog> _list,
                                 String bulkID, String remark, SQLiteDatabase db) {
        int result = 0;
        try {


          /*  db = context.openOrCreateDatabase(ClsGlobal.Database_Name,
                    context.MODE_APPEND, null);*/


            List<Integer> _listID = StreamSupport.stream(_list)
                    .map(ClsBulkSMSLog::getLogID)//get only getBulkID no from list!!!
                    .collect(Collectors.toList());

            Gson gson = new Gson();
            String jsonInString = gson.toJson(_listID);
            Log.d("--Update--", "_listID: " + jsonInString);


            Log.e("--Update--", "_listID: " + _listID);
            String strSql = "UPDATE [SMSLog] SET "
//                    .concat("[bulkID] = ")
//                    .concat(bulkID)

                    .concat("[Status] = ")
                    .concat("'")
                    .concat(bulkID == null ? "Pending" : "Sending")
                    .concat("'")

                    .concat(",[serverBulkID] = ")
                    .concat("'")
                    .concat(bulkID == null ? "Pending" : bulkID)
                    .concat("'")

                    .concat(",[Remark] = ")
                    .concat("'")
                    .concat(remark)
                    .concat("'")

                    .concat(" WHERE [logID] IN  ")
                    .concat("(")
                    .concat(TextUtils.join(",", _listID))
                    .concat(")")
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


    @SuppressLint("WrongConstant")
    public static int DeleteAll(Context context) {
        int result = 0;
        try {


            db = context.openOrCreateDatabase(ClsGlobal.Database_Name,
                    context.MODE_APPEND, null);

            String deleteQry = ("DELETE FROM [SMSBulkMaster] ");
            SQLiteStatement delete = db.compileStatement(deleteQry);
            result = delete.executeUpdateDelete();

            deleteQry = ("DELETE FROM [SMSLog] ");

            delete = db.compileStatement(deleteQry);
            result = delete.executeUpdateDelete();

            deleteQry = ("DELETE FROM [SalesSMSLogsMaster] ");
            delete = db.compileStatement(deleteQry);
            result = delete.executeUpdateDelete();

            db.close();

        } catch (Exception e) {
            Log.e("--Update--", "Exception: " + e.getMessage());
        }
        return result;
    }
}
