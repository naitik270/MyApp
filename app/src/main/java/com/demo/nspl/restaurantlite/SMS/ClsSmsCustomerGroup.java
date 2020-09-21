package com.demo.nspl.restaurantlite.SMS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.nspl.restaurantlite.A_Test.ClsGetValue;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.classes.ClsBulkSMSLog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_APPEND;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getBulkMessage;

public class ClsSmsCustomerGroup implements Serializable {

    int GroupId = 0;
    int DetailId = 0;
    String GroupName = "";
    String Active = "";
    String EntryDate = "";
    String Remark = "";
    String CustomerName = "";
    String MobileNo = "";

    public int getGroupId() {
        return GroupId;
    }

    public void setGroupId(int groupId) {
        GroupId = groupId;
    }

    public int getDetailId() {
        return DetailId;
    }

    public void setDetailId(int detailId) {
        DetailId = detailId;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String active) {
        Active = active;
    }

    public String getEntryDate() {
        return EntryDate;
    }

    public void setEntryDate(String entryDate) {
        EntryDate = entryDate;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    static Context context;
    private static SQLiteDatabase db;
    AppCompatActivity activity;

    public ClsSmsCustomerGroup() {
    }

    public ClsSmsCustomerGroup(Context ctx) {
        context = ctx;
    }


    @SuppressLint("WrongConstant")
    public static int Insert(ClsSmsCustomerGroup obj,
                             List<ClsGetValue> lstClsGetValues) {
        int result = 0;

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, MODE_APPEND, null);

            String qry = "INSERT INTO [SmsCustomerGroup] ([GroupName],[Active],[EntryDate],[Remark]) VALUES ('"

                    .concat(obj.getGroupName().trim().replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(obj.getActive())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ClsGlobal.getCurruntDateTime())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(obj.getRemark().trim().replace("'", "''"))
                    .concat("'")
                    .concat(");");

            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();

            Log.e("--Insert--", "SmsCustomerGroup: " + qry);
            Log.e("--Insert--", "result: " + result);

            Cursor c = db.rawQuery("SELECT last_insert_rowid()", null);
            c.moveToFirst();
            int id = c.getInt(0);
            Log.e("id:-- ", String.valueOf(id));

            obj.setGroupId(id);
            db.close();

            if (result != 0) {
                for (ClsGetValue objClsGetValue : lstClsGetValues) {
                    db = context.openOrCreateDatabase(ClsGlobal.Database_Name, MODE_APPEND, null);

                    qry = "INSERT INTO [SMSCustomerGroupDetail] ([GroupId],[CustomerName],[MobileNo]) VALUES ("
                            .concat(String.valueOf(obj.getGroupId()))
                            .concat(",")

                            .concat("'")
                            .concat(objClsGetValue.getmName().replace("'", ""))
                            .concat("'")
                            .concat(",")

                            .concat("'")
                            .concat(objClsGetValue.getmMobile())
                            .concat("'")
                            .concat(");");

                    SQLiteStatement statementinsert = db.compileStatement(qry);
                    result = statementinsert.executeUpdateDelete();
                    Log.e("--Insert--", "SMSCustomerGroupDetail: " + qry);
                    Log.e("--Insert--", "result: " + result);
                    db.close();
                }
            }

            return result;
        } catch (Exception e) {
            Log.e("ClsReservationMaster", "InsertException-->>>>>>>>" + e.getMessage());
            e.getMessage();
        }
        return result;

    }

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int totalCount) {
        TotalCount = totalCount;
    }

    int TotalCount = 0;

    @SuppressLint("WrongConstant")
    public static List<ClsSmsCustomerGroup> getGroupList(Context context, String _where) {
        List<ClsSmsCustomerGroup> list = new ArrayList<>();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            String qry = ("SELECT SCG.*,COUNT(IFNULL(SCGD.[GroupId],0)) " +
                    "as [TotalCount] FROM [SmsCustomerGroup] as SCG ")
                    .concat("INNER JOIN [SMSCustomerGroupDetail] as SCGD ")
                    .concat("WHERE 1=1 ")
                    .concat("AND SCG.[GroupId] = SCGD.[GroupId] ")
                    .concat(_where)
                    .concat(" GROUP BY SCGD.[GroupId]");
//                    .concat(" ORDER BY SCG.[GroupName]");


            Cursor cur = db.rawQuery(qry, null);

            Log.e("--Select--", "SmsCustomerGroup: " + qry);
            Log.e("--Select--", "Count: " + cur.getCount());

            while (cur.moveToNext()) {
                ClsSmsCustomerGroup getSet = new ClsSmsCustomerGroup();
                getSet.setGroupId(cur.getInt(cur.getColumnIndex("GroupId")));
                getSet.setGroupName(cur.getString(cur.getColumnIndex("GroupName")));
                getSet.setActive(cur.getString(cur.getColumnIndex("Active")));
                getSet.setEntryDate(cur.getString(cur.getColumnIndex("EntryDate")));
                getSet.setRemark(cur.getString(cur.getColumnIndex("Remark")));
                getSet.setTotalCount(cur.getInt(cur.getColumnIndex("TotalCount")));
                list.add(getSet);
            }

            db.close();

        } catch (Exception e) {
            e.getMessage();
            Log.e("--Select--", "Exception: " + e.getMessage());
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsSmsCustomerGroup> getGroupAllList(Context context, String _where) {
        List<ClsSmsCustomerGroup> list = new ArrayList<>();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String qry = ("SELECT SCG.*,COUNT(IFNULL(SCGD.[GroupId],0)) " +
                    "as [TotalCount] FROM [SmsCustomerGroup] as SCG ")
                    .concat("INNER JOIN [SMSCustomerGroupDetail] as SCGD ")
                    .concat("WHERE 1=1 ")
                    .concat("AND SCG.[GroupId] = SCGD.[GroupId] ")
                    .concat(_where)
                    .concat(" GROUP BY SCGD.[GroupId]");


            Cursor cur = db.rawQuery(qry, null);

            Log.e("--Select--", "SmsCustomerGroup: " + qry);
            Log.e("--Select--", "Count: " + cur.getCount());


            while (cur.moveToNext()) {
                if (cur.getString(cur.getColumnIndex("GroupName")) != null) {
                    ClsSmsCustomerGroup getSet = new ClsSmsCustomerGroup();
                    getSet.setGroupId(cur.getInt(cur.getColumnIndex("GroupId")));
                    getSet.setGroupName(cur.getString(cur.getColumnIndex("GroupName")));
                    getSet.setActive(cur.getString(cur.getColumnIndex("Active")));
                    getSet.setTotalCount(cur.getInt(cur.getColumnIndex("TotalCount")));
                    list.add(getSet);

                }

            }

            db.close();

        } catch (Exception e) {
            e.getMessage();
            db.close();
            Log.e("--Select--", "Exception: " + e.getMessage());
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static int getGroupMemberCount(Context context, String group_id) {
        int count = 0;

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String qry = ("SELECT count(*) as Total" +
                    " FROM [SMSCustomerGroupDetail] ")
                    .concat("WHERE 1=1 AND [GroupId] = " + group_id)
                    .concat(" GROUP by [GroupId]");


            Cursor cur = db.rawQuery(qry, null);

            Log.e("--Select--", "SmsCustomerGroup: " + qry);
            Log.e("--Select--", "Count: " + cur.getCount());

            if (cur != null) {

                count = cur.getInt(cur.getColumnIndex("Total"));
//                count = cur.getCount();
            } else {
                count = 0;
            }


            db.close();

        } catch (Exception e) {
            e.getMessage();
            db.close();
            Log.e("--Select--", "Exception: " + e.getMessage());
        }
        return count;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsSmsCustomerGroup> getCustomerListByID(Context context, String _where) {
        List<ClsSmsCustomerGroup> list = new ArrayList<>();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);


            String qry = "SELECT * FROM [SMSCustomerGroupDetail] WHERE 1=1 "
                    .concat("AND [GroupId] =")
                    .concat(_where)
                    .concat(";");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--Select--", "SmsCustomerGroup: " + qry);
            Log.e("--Select--", "Count: " + cur.getCount());

            while (cur.moveToNext()) {
                ClsSmsCustomerGroup getSet = new ClsSmsCustomerGroup();
                getSet.setDetailId(cur.getInt(cur.getColumnIndex("DetailId")));
                getSet.setGroupId(cur.getInt(cur.getColumnIndex("GroupId")));
                getSet.setCustomerName(cur.getString(cur.getColumnIndex("CustomerName")));
                getSet.setMobileNo(cur.getString(cur.getColumnIndex("MobileNo")));
                list.add(getSet);
            }
            db.close();

        } catch (Exception e) {
            e.getMessage();
            Log.e("--Select--", "Exception: " + e.getMessage());
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsBulkSMSLog> getCustomerListFromGroupByID(String message,
                                                                   String _where
            , int messageLength, SQLiteDatabase db, Context context) {

        List<ClsBulkSMSLog> list = new ArrayList<>();

        try {
//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            String qry = ("SELECT [MobileNo],[CustomerName] " +
                    "FROM [SMSCustomerGroupDetail] WHERE 1=1 ")
                    .concat(" AND [GroupId] = ")
                    .concat(_where)
                    .concat(" ORDER BY [CustomerName] DESC ")
                    .concat(";");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--Select--", "SmsCustomerGroup: " + qry);
            Log.e("--Select--", "Count: " + cur.getCount());

            while (cur.moveToNext()) {
                ClsBulkSMSLog getSet = new ClsBulkSMSLog();
                getSet.setBulkID(ClsGlobal.last_id_SMSBulkMaster);
                getSet.setMobile(cur.getString(cur.getColumnIndex("MobileNo")));
                getSet.setCustomerName(cur.getString(cur.getColumnIndex("CustomerName")));
                getSet.setCreditCount((int) Math.ceil((double) messageLength / 145));
                getSet.setStatus("Pending");
                getSet.setStatusDateTime("");
                getSet.setMessage(getBulkMessage(message,
                        cur.getString(cur.getColumnIndex("CustomerName")), context));
                getSet.setStatusCode(-1);
                list.add(getSet);


            }
//            db.close();

        } catch (Exception e) {
            e.getMessage();
            Log.e("--Select--", "Exception: " + e.getMessage());
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static int DeleteGroupAndCustomersByGroupId(ClsSmsCustomerGroup obj, Context context) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String qry = "DELETE FROM [SmsCustomerGroup] WHERE [GroupId] = "
                    .concat(String.valueOf(obj.getGroupId()))
                    .concat(";");

            Log.e("--Delete--", "DeleteGroupByID: " + qry);
            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();

            Log.e("--Delete--", "DeleteGroupByResult: " + result);

            String deleteQry = "DELETE FROM [SMSCustomerGroupDetail] WHERE [GroupId] = "
                    .concat(String.valueOf(obj.getGroupId()))
                    .concat(";");


            Log.e("--Delete--", "DeleteCustomerGroupByID: " + deleteQry);
            SQLiteStatement delete = db.compileStatement(deleteQry);
            result = delete.executeUpdateDelete();
            Log.e("--Delete--", "DeleteCustomerGroupByResult: " + result);


            db.close();
            return result;
        } catch (Exception e) {
            Log.e("--Delete--", "Exception: " + e.getMessage());
            e.getMessage();
        }
        return result;
    }


    int lstGroupId = 0;

    public int getLstGroupId() {
        return lstGroupId;
    }

    public void setLstGroupId(int lstGroupId) {
        this.lstGroupId = lstGroupId;
    }

    @SuppressLint("WrongConstant")
    public static ClsReturnListObj getCustomerGroupValue(int _groupId, Context context) {
        List<ClsGetValue> lstGetSet = new ArrayList<>();
        ClsReturnListObj clsReturnListObj = new ClsReturnListObj();
        ClsSmsCustomerGroup getSet = new ClsSmsCustomerGroup();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            String qry = "SELECT SCGD.*,SCGD.[GroupId] As [lstGroupId], SCG.[GroupId],SCG.[GroupName],SCG.[Active],SCG.[Remark] FROM [SmsCustomerGroup] AS SCG "
                    .concat(" INNER JOIN [SMSCustomerGroupDetail] AS SCGD ON SCG.[GroupId] = SCGD.[GroupId] ")
                    .concat(" WHERE 1=1 ")
                    .concat(" AND SCG.[GroupId] = ")
                    .concat(String.valueOf(_groupId))
                    .concat(" AND SCGD.[GroupId] = ")
                    .concat(String.valueOf(_groupId))
                    .concat(";");

            Cursor cur = db.rawQuery(qry, null);


            Log.e("--FillValue--", "getCustomerGroupValue: " + qry);
            Log.e("--FillValue--", "Count: " + cur.getCount());


            while (cur.moveToNext()) {
                getSet.setGroupId(cur.getInt(cur.getColumnIndex("GroupId")));
                getSet.setGroupName(cur.getString(cur.getColumnIndex("GroupName")));
                getSet.setActive(cur.getString(cur.getColumnIndex("Active")));
                getSet.setRemark(cur.getString(cur.getColumnIndex("Remark")));


                ClsGetValue obj = new ClsGetValue();

                obj.setDetailId(cur.getInt(cur.getColumnIndex("DetailId")));


                Log.e("--Invoice--", "DetailId: " + cur.getInt(cur.getColumnIndex("DetailId")));


                obj.setLstGroupId(cur.getInt(cur.getColumnIndex("lstGroupId")));


                Log.e("--Invoice--", "lstGroupId: " + cur.getInt(cur.getColumnIndex("lstGroupId")));

                obj.setmName(cur.getString(cur.getColumnIndex("CustomerName")));


                Log.e("--Invoice--", "CustomerName: " + cur.getString(cur.getColumnIndex("CustomerName")));

                obj.setmMobile(cur.getString(cur.getColumnIndex("MobileNo")));


                Log.e("--Invoice--", "MobileNo: " + cur.getString(cur.getColumnIndex("MobileNo")));

                lstGetSet.add(obj);
            }

            clsReturnListObj.setObj(getSet);
            clsReturnListObj.setLstClsSmsCustomerGroups(lstGetSet);

            db.close();

        } catch (Exception e) {
            Log.e("--FillValue--", "Exception: " + e.getMessage());
            e.getMessage();
        }
        return clsReturnListObj;
    }

    @SuppressLint("WrongConstant")
    public boolean checkExists(String _where, SQLiteDatabase db) {
        boolean result = false;
        try {
            String sqlStr = "SELECT 1 FROM [SmsCustomerGroup] WHERE 1=1 "
                    .concat(_where)
                    .concat(";");
            Log.e("exists", "existsChqQry - " + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            if (cur != null && cur.getCount() != 0) {
                result = true;
            }
        } catch (Exception e) {
            Log.e("ClsUnit", "ClsUnitMaster>>>>CheckExist" + e.getMessage());
            e.getMessage();
            db.close();
        }
        return result;
    }


    @SuppressLint("WrongConstant")
    public static int Update(ClsSmsCustomerGroup objClsSmsCustomerGroup, List<ClsGetValue> lstClsGetValues) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "UPDATE [SmsCustomerGroup] SET "
                    .concat("[GroupName] = ")
                    .concat("'")
                    .concat(objClsSmsCustomerGroup.getGroupName().trim().replace("'", "''"))
                    .concat("'")

                    .concat(" ,[Active] = ")
                    .concat("'")
                    .concat(String.valueOf(objClsSmsCustomerGroup.getActive()))
                    .concat("'")

                    .concat(" ,[Remark] = ")
                    .concat("'")
                    .concat(objClsSmsCustomerGroup.getRemark().trim().replace("'", "''"))
                    .concat("'")

                    .concat(" WHERE [GroupId] = ")
                    .concat(String.valueOf(objClsSmsCustomerGroup.getGroupId()))
                    .concat(";");

            Log.e("--Update--", "strSql: " + strSql);

            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();
            Log.e("--Update--", "result: " + result);


            strSql = "DELETE FROM [SMSCustomerGroupDetail] WHERE [GroupId] = "
                    .concat(String.valueOf(objClsSmsCustomerGroup.getGroupId()));
            statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();
            Log.e("--Update--", "result: " + result);


            if (result != 0) {
                for (ClsGetValue objClsGetValue : lstClsGetValues) {
                    db = context.openOrCreateDatabase(ClsGlobal.Database_Name, MODE_APPEND, null);

                    strSql = "INSERT INTO [SMSCustomerGroupDetail] ([GroupId],[CustomerName],[MobileNo]) VALUES ("
                            .concat(String.valueOf(objClsSmsCustomerGroup.getGroupId()))
                            .concat(",")

                            .concat("'")
                            .concat(objClsGetValue.getmName()
                                    .replace("'", "''"))
                            .concat("'")
                            .concat(",")

                            .concat("'")
                            .concat(objClsGetValue.getmMobile())
                            .concat("'")
                            .concat(");");

                    SQLiteStatement statementinsert = db.compileStatement(strSql);
                    result = statementinsert.executeUpdateDelete();
                    Log.e("--Update--", "SMSCustomerGroupDetail: " + strSql);
                    Log.e("--Update--", "result: " + result);

                    db.close();

                }
            }


            db.close();

        } catch (Exception e) {
            Log.e("--Update--", "Exception: " + e.getMessage());
        }
        return result;
    }


    @SuppressLint("WrongConstant")
    public static int DeleteCustomerByGroupID(ClsSmsCustomerGroup obj, Context context) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String qry = "DELETE FROM [SmsCustomerGroup] WHERE [GroupId] = "
                    .concat(String.valueOf(obj.getGroupId()))
                    .concat(";");

            Log.e("--Delete--", "DeleteGroupByID: " + qry);
            SQLiteStatement statement = db.compileStatement(qry);

            result = statement.executeUpdateDelete();

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("--Delete--", "Exception: " + e.getMessage());
            e.getMessage();
        }
        return result;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsSmsCustomerGroup> getGroupCustomersList(int _groupID, Context context) {
        List<ClsSmsCustomerGroup> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, MODE_APPEND, null);

            String qry = ("SELECT * FROM [SMSCustomerGroupDetail] WHERE 1=1 "
                    .concat(" AND [GroupId] = ")
                    .concat(String.valueOf(_groupID))
                    .concat(" ORDER BY [CustomerName]"));

            @SuppressLint("Recycle") Cursor cur = db.rawQuery(qry, null);

            Log.e("--list--", "qry: " + qry);
            Log.e("--list--", "Count: " + cur.getCount());

            while (cur.moveToNext()) {
                ClsSmsCustomerGroup getSet = new ClsSmsCustomerGroup();

                getSet.setGroupId(cur.getInt(cur.getColumnIndex("GroupId")));
                getSet.setCustomerName(cur.getString(cur.getColumnIndex("CustomerName")));
                getSet.setMobileNo(cur.getString(cur.getColumnIndex("MobileNo")));

                list.add(getSet);
            }
            db.close();
        } catch (Exception e) {
            db.close();
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static ClsSmsCustomerGroup getQryByID(Context context) {
        ClsSmsCustomerGroup getSet = new ClsSmsCustomerGroup();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, MODE_APPEND, null);

            String qry = ("SELECT * COUNT(IFNULL([GroupId],0)) as [TotalCount] FROM [SMSCustomerGroupDetail] WHERE 1=1 "
                    .concat(" AND [GroupId] = ")
                    .concat(String.valueOf(1))
                    .concat(" ORDER BY [CustomerName]"));

            @SuppressLint("Recycle") Cursor cur = db.rawQuery(qry, null);

            Log.e("--list--", "qry: " + qry);
            Log.e("--list--", "Count: " + cur.getCount());

            while (cur.moveToNext()) {

                getSet.setGroupId(cur.getInt(cur.getColumnIndex("GroupId")));
                getSet.setGroupName(cur.getString(cur.getColumnIndex("GroupName")));
                getSet.setActive(cur.getString(cur.getColumnIndex("Active")));
                getSet.setEntryDate(cur.getString(cur.getColumnIndex("EntryDate")));
                getSet.setRemark(cur.getString(cur.getColumnIndex("Remark")));
                getSet.setTotalCount(cur.getInt(cur.getColumnIndex("TotalCount")));

            }
            db.close();
        } catch (Exception e) {
            db.close();
            e.getMessage();
        }
        return getSet;
    }

}
