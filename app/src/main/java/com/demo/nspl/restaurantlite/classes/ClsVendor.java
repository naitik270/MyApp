package com.demo.nspl.restaurantlite.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desktop on 3/14/2018.
 */

public class ClsVendor {

    int vendor_id;
    String vendor_name = "";
    String contact_no = "";
    String address = "";
    String gst_no = "";
    Integer sort_no = 0;
    String remark = "";
    String active = "";
    String TYPE = "";


    double OpeningStock = 0.0;

    public double getOpeningStock() {
        return OpeningStock;
    }

    public void setOpeningStock(double openingStock) {
        OpeningStock = openingStock;
    }


    static Context context;
    AppCompatActivity activity;
    private static SQLiteDatabase db;


    public Integer getSort_no() {
        return sort_no;
    }

    public void setSort_no(Integer sort_no) {
        this.sort_no = sort_no;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }


    public ClsVendor() {

    }

    public ClsVendor(Context ctx) {

        context = ctx;
    }

    public int getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(int vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGst_no() {
        return gst_no;
    }

    public void setGst_no(String gst_no) {
        this.gst_no = gst_no;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    String BalanceType = "";

    public String getBalanceType() {
        return BalanceType;
    }

    public void setBalanceType(String balanceType) {
        BalanceType = balanceType;
    }

    @SuppressLint("WrongConstant")
    public static int Insert(ClsVendor objVendor,io.requery.android.database.sqlite.SQLiteDatabase
            db) {
        int result = 0;
        try {

//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String qry = ("INSERT INTO [VENDOR_MASTER] ([VENDOR_NAME],[CONTACT_NO],[ADDRESS],[GST_NO],[TYPE]," +
                    "[ACTIVE],[REMARK],[BalanceType],[OpeningStock],[SORT_NO]) VALUES ('")

                    .concat(objVendor.getVendor_name().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objVendor.getContact_no())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objVendor.getAddress().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objVendor.getGst_no())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objVendor.getTYPE())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objVendor.getActive())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objVendor.getRemark().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objVendor.getBalanceType())
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(objVendor.getOpeningStock()))
                    .concat(",")

                    .concat(String.valueOf(objVendor.getSort_no()))
                    .concat(");");

            io.requery.android.database.sqlite.SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.e("INSERT", "Vendor--->>" + qry);

//            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsVendor", "Insert>>>>>>>>" + e.getMessage());
            e.getMessage();
        }
        return result;
    }


    @SuppressLint("WrongConstant")
    public boolean checkExists(String _where, io.requery.android.database.sqlite.SQLiteDatabase
            db) {
        boolean result = false;

        try {


//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = "SELECT 1 FROM [VENDOR_MASTER] WHERE 1=1 "
                    .concat(_where)
                    .concat(";");
            Log.e("exists", "existsChqQry - " + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            if (cur != null && cur.getCount() != 0) {
                result = true;
            }
//            db.close();
        } catch (Exception e) {
            Log.e("ClsVendor", "ClsVendorMaster>>>>CheckExist" + e.getMessage());
            e.getMessage();
        }
        return result;


    }


    @SuppressLint("WrongConstant")
    public static ClsVendor getPhoneById(int _vendorID, Context context) {

        ClsVendor clsVendor = new ClsVendor();

        try {

         /*   if (!db.isOpen()){

                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            }
*/
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);



//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND,
//                    null);
            String sqlStr = ("SELECT [CONTACT_NO],[VENDOR_NAME] " +
                    "FROM [VENDOR_MASTER] WHERE 1=1 AND [VENDOR_ID] = ")
                    .concat(String.valueOf(_vendorID))

                    .concat(";");
            Log.e("Update", ">>>>>>>>>" + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            Log.e("Update", ">>>>>>>>>" + cur.getCount());
            while (cur.moveToNext()) {
                clsVendor.setContact_no(cur.getString(cur.getColumnIndex(
                        "CONTACT_NO")));
                clsVendor.setVendor_name(cur
                        .getString(cur.getColumnIndex("VENDOR_NAME")));

            }

            db.close();

        } catch (Exception e) {
            Log.e("ClsVendor", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }

        return clsVendor;
    }


    @SuppressLint("WrongConstant")
    public static List<String> getAddVendorMasterColumn(Context context
            ,io.requery.android.database.sqlite.SQLiteDatabase db) {
        List<String> columns = new ArrayList<>();
        try {

//            io.requery.android.database.sqlite.SQLiteDatabase db;
//
//            db = io.requery.android.database.sqlite.SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath(ClsGlobal.Database_Name).getPath(),
//                    null);

            String qry = "SELECT * FROM [VENDOR_MASTER] LIMIT 1 ";

            Cursor cur = db.rawQuery(qry, null);
            Log.e("--Select--", ">>>>>>>>>" + cur.getCount());

            if (cur != null)
                for (String c : cur.getColumnNames()) {
                    columns.add(c);
                }

//            db.close();

        } catch (Exception e) {
            Log.e("ClsCategory", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }
        return columns;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsVendor> getVendorFilterVendorList() {
        List<ClsVendor> list = new ArrayList<>();
        try {
            Log.e("--qry--", "Step:1");

        /*    if (!db.isOpen()){

                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            }*/
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name,
//                    context.MODE_APPEND, null);

            Log.e("--qry--", "Step:2");

          /*  String Qry = "SELECT "
                    .concat("vm.[VENDOR_ID]")
                    .concat(",vm.[VENDOR_NAME] AS [VENDOR_NAME] ")
                    .concat(",vm.[CONTACT_NO] ")
                    .concat(",vm.[ACTIVE] ")
                    .concat(",vm.[TYPE] ")
                    .concat(" FROM [PurchaseDetail] AS pd ")
                    .concat(" LEFT JOIN [VENDOR_MASTER] AS vm ON vm.[VENDOR_ID] = pd.[VendorID] ")
                    .concat(" WHERE 1=1 ")
                    .concat(" AND vm.[ACTIVE] = 'YES' ")
                    .concat(" AND vm.[TYPE] IN ('SUPPLIER','BOTH') ")
                    .concat(" ORDER BY vm.[VENDOR_NAME] ");
*/

            String Qry = "SELECT "
                    .concat("vm.[VENDOR_ID]")
                    .concat(",upper(vm.[VENDOR_NAME]) AS [VENDOR_NAME]")
                    .concat(",vm.[CONTACT_NO]")
                    .concat(",vm.[ADDRESS]")
                    .concat(",vm.[GST_NO]")
                    .concat(",vm.[TYPE]")
                    .concat(",vm.[ACTIVE]")
                    .concat(",vm.[SORT_NO]")
                    .concat(",vm.[REMARK]")
                    .concat(",IFNULL(vm.[BalanceType],'TO PAY') AS [BalanceType]")
                    .concat(",IFNULL(vm.[OpeningStock],0) AS [OpeningStock]")

                    .concat(" FROM [PurchaseMaster] AS pd ")
                    .concat(" LEFT JOIN [VENDOR_MASTER] AS vm ON vm.[VENDOR_ID] = pd.[VendorID] ")

                    .concat(" WHERE 1=1 ")
                    .concat(" AND vm.[ACTIVE] = 'YES' ")
                    .concat(" AND vm.[TYPE] IN ('SUPPLIER','BOTH') ")
                    .concat(" ORDER BY vm.[VENDOR_NAME] ");


            Log.e("--qry--", "Step:3");

            Cursor cur = db.rawQuery(Qry, null);

            Log.e("--qry--", "FilterQry: " + Qry);
            Log.e("--cur--", String.valueOf(cur.getCount()));

            while (cur.moveToNext()) {

                Log.e("--qry--", "Step:4");

                ClsVendor getSet = new ClsVendor();
                getSet.setVendor_id(cur.getInt(cur.getColumnIndex("VENDOR_ID")));
                getSet.setVendor_name(cur.getString(cur.getColumnIndex("VENDOR_NAME")));
                getSet.setContact_no(cur.getString(cur.getColumnIndex("CONTACT_NO")));
                getSet.setAddress(cur.getString(cur.getColumnIndex("ADDRESS")));
                getSet.setGst_no(cur.getString(cur.getColumnIndex("GST_NO")));
                getSet.setSort_no(cur.getInt(cur.getColumnIndex("SORT_NO")));
                getSet.setTYPE(cur.getString(cur.getColumnIndex("TYPE")));
                getSet.setActive(cur.getString(cur.getColumnIndex("ACTIVE")));
                getSet.setRemark(cur.getString(cur.getColumnIndex("REMARK")));
                getSet.setBalanceType(cur.getString(cur.getColumnIndex("BalanceType")));
                getSet.setOpeningStock(cur.getDouble(cur.getColumnIndex("OpeningStock")));
                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("ClsVendor", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;

    }


    @SuppressLint("WrongConstant")
    public static List<ClsVendor> getList(String _where) {
        List<ClsVendor> list = new ArrayList<>();

        try {
           /* if (!db.isOpen()){

                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            }*/
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT "
                    .concat("[VENDOR_ID]")
                    .concat(",upper([VENDOR_NAME]) AS [VENDOR_NAME]")
                    .concat(",[CONTACT_NO]")
                    .concat(",[ADDRESS]")
                    .concat(",[GST_NO]")
                    .concat(",IFNULL([TYPE],'BOTH') AS [TYPE]")
                    .concat(",[ACTIVE]")
                    .concat(",[SORT_NO]")
                    .concat(",[REMARK]")
                    .concat(",IFNULL([BalanceType],'TO PAY') AS [BalanceType]")
                    .concat(",IFNULL([OpeningStock],0) AS [OpeningStock]")
                    .concat(" FROM [VENDOR_MASTER] WHERE 1=1 ")
                    .concat(_where);


            Cursor cur = db.rawQuery(qry, null);

            Log.e("--VendorList--", String.valueOf(cur.getCount()));
            Log.e("--VendorList--", "qry: " + qry);


            while (cur.moveToNext()) {
                ClsVendor getSet = new ClsVendor();
                getSet.setVendor_id(cur.getInt(cur.getColumnIndex("VENDOR_ID")));
                getSet.setVendor_name(cur.getString(cur.getColumnIndex("VENDOR_NAME")));
                getSet.setContact_no(cur.getString(cur.getColumnIndex("CONTACT_NO")));
                getSet.setAddress(cur.getString(cur.getColumnIndex("ADDRESS")));
                getSet.setGst_no(cur.getString(cur.getColumnIndex("GST_NO")));
                getSet.setSort_no(cur.getInt(cur.getColumnIndex("SORT_NO")));
                getSet.setTYPE(cur.getString(cur.getColumnIndex("TYPE")));
                getSet.setActive(cur.getString(cur.getColumnIndex("ACTIVE")));
                getSet.setRemark(cur.getString(cur.getColumnIndex("REMARK")));
                getSet.setBalanceType(cur.getString(cur.getColumnIndex("BalanceType")));
                getSet.setOpeningStock(cur.getDouble(cur.getColumnIndex("OpeningStock")));
                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("ClsVendor", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static ClsVendor getObject(int _vendorID) {
        ClsVendor Obj = new ClsVendor();

        try {
/*

            if (!db.isOpen()){

                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            }*/

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);


//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND,
//                    null);
            String sqlStr = ("SELECT [VENDOR_ID],[VENDOR_NAME],[CONTACT_NO]," +
                    "[ADDRESS],[GST_NO],[TYPE]," +
                    "[ACTIVE],[REMARK],[BalanceType],[OpeningStock],[SORT_NO] " +
                    "FROM [VENDOR_MASTER] WHERE 1=1 AND [VENDOR_ID] = ")
                    .concat(String.valueOf(_vendorID))

                    .concat(";");
            Log.e("Update", ">>>>>>>>>" + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            Log.e("Update", ">>>>>>>>>" + cur.getCount());
            while (cur.moveToNext()) {

                Obj.setVendor_id(cur.getInt(cur.getColumnIndex("VENDOR_ID")));
                Obj.setVendor_name(cur.getString(cur.getColumnIndex("VENDOR_NAME")));
                Obj.setContact_no(cur.getString(cur.getColumnIndex("CONTACT_NO")));
                Obj.setAddress(cur.getString(cur.getColumnIndex("ADDRESS")));
                Obj.setGst_no(cur.getString(cur.getColumnIndex("GST_NO")));
                Obj.setSort_no(cur.getInt(cur.getColumnIndex("SORT_NO")));
                Obj.setTYPE(cur.getString(cur.getColumnIndex("TYPE")));
                Obj.setActive(cur.getString(cur.getColumnIndex("ACTIVE")));
                Obj.setRemark(cur.getString(cur.getColumnIndex("REMARK")));
                Obj.setBalanceType(cur.getString(cur.getColumnIndex("BalanceType")));
                Obj.setOpeningStock(cur.getDouble(cur.getColumnIndex("OpeningStock")));
            }

            db.close();

        } catch (Exception e) {
            Log.e("ClsVendor", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }
        return Obj;
    }


    @SuppressLint("WrongConstant")
    public boolean checkExists(String _where) {
        boolean result = false;

        try {

/*

            if (!db.isOpen()){

                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            }
*/

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = "SELECT 1 FROM [VENDOR_MASTER] WHERE 1=1 "
                    .concat(_where)
                    .concat(";");
            Log.e("exists", "existsChqQry - " + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            if (cur != null && cur.getCount() != 0) {
                result = true;
            }
            db.close();
        } catch (Exception e) {
            Log.e("ClsVendor", "ClsVendorMaster>>>>CheckExist" + e.getMessage());
            e.getMessage();
        }
        return result;


    }

    @SuppressLint("WrongConstant")
    public static int Delete(ClsVendor ObjVendor, Context context) {
        int result = 0;
        try {

/*
            if (!db.isOpen()){

                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            }*/
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "DELETE FROM [VENDOR_MASTER] WHERE [VENDOR_ID] = "
                    .concat(String.valueOf(ObjVendor.getVendor_id()))
                    .concat(";");
            Log.e("DELETE", "DELqRY-" + strSql);
            SQLiteStatement statement = db.compileStatement(strSql);

            result = statement.executeUpdateDelete();

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsVendor", "Delete" + e.getMessage());
            e.getMessage();
        }
        return result;


    }


    @SuppressLint("WrongConstant")
    public static ClsVendor getVendorByPhone(String vendorPhone,Context context) {
        ClsVendor Obj = new ClsVendor();

        try {
/*
            if (!db.isOpen()){

                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            }*/

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND,
//                    null);

            String sqlStr = ("SELECT [VENDOR_ID],[VENDOR_NAME],[CONTACT_NO]," +
                    "[ADDRESS],[GST_NO],[TYPE]," +
                    "[ACTIVE],[REMARK],[BalanceType],[OpeningStock],[SORT_NO] " +
                    "FROM [VENDOR_MASTER] WHERE 1=1 AND [CONTACT_NO] = ")
                    .concat(String.valueOf(vendorPhone))
                    .concat(";");

            Log.e("Update", ">>>>>>>>>" + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            Log.e("Update", ">>>>>>>>>" + cur.getCount());
            while (cur.moveToNext()) {

                Obj.setVendor_id(cur.getInt(cur.getColumnIndex("VENDOR_ID")));
                Obj.setVendor_name(cur.getString(cur.getColumnIndex("VENDOR_NAME")));
                Obj.setContact_no(cur.getString(cur.getColumnIndex("CONTACT_NO")));
                Obj.setAddress(cur.getString(cur.getColumnIndex("ADDRESS")));
                Obj.setGst_no(cur.getString(cur.getColumnIndex("GST_NO")));
                Obj.setSort_no(cur.getInt(cur.getColumnIndex("SORT_NO")));
                Obj.setTYPE(cur.getString(cur.getColumnIndex("TYPE")));
                Obj.setActive(cur.getString(cur.getColumnIndex("ACTIVE")));
                Obj.setRemark(cur.getString(cur.getColumnIndex("REMARK")));
                Obj.setBalanceType(cur.getString(cur.getColumnIndex("BalanceType")));
                Obj.setOpeningStock(cur.getDouble(cur.getColumnIndex("OpeningStock")));
            }

            db.close();

        } catch (Exception e) {
            Log.e("ClsVendor", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }
        return Obj;
    }


    @SuppressLint("WrongConstant")
    public static int Update(ClsVendor objVendor) {
        int result = 0;
        try {
/*
            if (!db.isOpen()){

                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            }*/
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "UPDATE [VENDOR_MASTER] SET "
                    .concat("[VENDOR_NAME] = ")
                    .concat("'")
                    .concat(objVendor.getVendor_name().trim()
                            .replace("'", "''"))
                    .concat("'")

                    .concat(" ,[CONTACT_NO] = ")
                    .concat("'")
                    .concat(objVendor.getContact_no())
                    .concat("'")

                    .concat(" ,[ADDRESS] = ")
                    .concat("'")
                    .concat(objVendor.getAddress().trim()
                            .replace("'", "''"))
                    .concat("'")

                    .concat(" ,[GST_NO] = ")
                    .concat("'")
                    .concat(objVendor.getGst_no())
                    .concat("'")

                    .concat(" ,[SORT_NO] = ")
                    .concat(String.valueOf(objVendor.getSort_no()))

                    .concat(" ,[TYPE] = ")
                    .concat("'")
                    .concat(objVendor.getTYPE())
                    .concat("'")

                    .concat(" ,[ACTIVE] = ")
                    .concat("'")
                    .concat(objVendor.getActive())
                    .concat("'")

                    .concat(" ,[REMARK] = ")
                    .concat("'")
                    .concat(objVendor.getRemark().trim()
                            .replace("'", "''"))
                    .concat("'")

                    .concat(" ,[BalanceType] = ")
                    .concat("'")
                    .concat(objVendor.getBalanceType())
                    .concat("'")

                    .concat(" ,[OpeningStock] = ")
                    .concat(String.valueOf(objVendor.getOpeningStock()))

                    .concat(" WHERE [VENDOR_ID] = ")
                    .concat(String.valueOf(objVendor.getVendor_id()))

                    .concat(";");
            Log.e("Update", strSql);
            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();
            //db.execSQL(strSql);
            db.close();

        } catch (Exception e) {
            Log.e("ClsVendor", "Update" + e.getMessage());
        }
        return result;
    }

    @SuppressLint("WrongConstant")
    public static int InsertWithPayment(ClsVendor Objvendor, ClsPaymentMaster objClsPaymentMaster) {
        int result = 0;
        try {
/*

            if (!db.isOpen()){

                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            }

*/
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String qry = ("INSERT INTO [VENDOR_MASTER] ([VENDOR_NAME],[CONTACT_NO],[ADDRESS],[GST_NO],[TYPE]," +
                    "[ACTIVE],[REMARK],[BalanceType],[OpeningStock],[SORT_NO]) VALUES ('")

                    .concat(Objvendor.getVendor_name().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(Objvendor.getContact_no())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(Objvendor.getAddress().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(Objvendor.getGst_no())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(Objvendor.getTYPE())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(Objvendor.getActive())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(Objvendor.getRemark().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(Objvendor.getBalanceType())
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(Objvendor.getOpeningStock()))
                    .concat(",")

                    .concat(String.valueOf(Objvendor.getSort_no()))
                    .concat(");");

            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.e("INSERT", "Vendor--->>" + qry);


            Cursor c = db.rawQuery("SELECT last_insert_rowid()", null);
            c.moveToFirst();
            int insertedID = c.getInt(0);//orderiD


            objClsPaymentMaster.setVendorID(insertedID);

            ClsPaymentMaster.InsertPaymentVendor(objClsPaymentMaster, context);


            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsVendor", "Insert>>>>>>>>" + e.getMessage());
            e.getMessage();
        }
        return result;
    }

    @SuppressLint("WrongConstant")
    public static int Insert1(ClsVendor objVendor,
                              io.requery.android.database.sqlite.SQLiteDatabase db) {
        int result = 0;
        try {

//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String qry = ("INSERT INTO [VENDOR_MASTER] ([VENDOR_NAME],[CONTACT_NO],[ADDRESS],[GST_NO],[TYPE]," +
                    "[ACTIVE],[REMARK],[BalanceType],[OpeningStock],[SORT_NO]) VALUES ('")

                    .concat(objVendor.getVendor_name().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objVendor.getContact_no())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objVendor.getAddress().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objVendor.getGst_no())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objVendor.getTYPE())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objVendor.getActive())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objVendor.getRemark().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objVendor.getBalanceType())
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(objVendor.getOpeningStock()))
                    .concat(",")

                    .concat(String.valueOf(objVendor.getSort_no()))
                    .concat(");");

            io.requery.android.database.sqlite.SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.e("INSERT", "Vendor--->>" + qry);

//            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsVendor", "Insert>>>>>>>>" + e.getMessage());
            e.getMessage();
        }
        return result;
    }

    @SuppressLint("WrongConstant")
    public boolean checkExists2(String _where, io.requery.android.database.sqlite
            .SQLiteDatabase db) {
        boolean result = false;

        try {


//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = "SELECT 1 FROM [VENDOR_MASTER] WHERE 1=1 "
                    .concat(_where)
                    .concat(";");
            Log.e("exists", "existsChqQry - " + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            if (cur != null && cur.getCount() != 0) {
                result = true;
            }
//            db.close();
        } catch (Exception e) {
            Log.e("ClsVendor", "ClsVendorMaster>>>>CheckExist" + e.getMessage());
            e.getMessage();
        }
        return result;


    }

    @SuppressLint("WrongConstant")
    public static String getVendorNameById(int _vendorID, Context context) {

        ClsVendor clsVendor = new ClsVendor();

        String vendorName = "";

        try {


            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND,
                    null);
            String sqlStr = ("SELECT [VENDOR_NAME] " +
                    "FROM [VENDOR_MASTER] WHERE 1=1 AND [VENDOR_ID] = ")
                    .concat(String.valueOf(_vendorID))

                    .concat(";");
            Log.e("Update", ">>>>>>>>>" + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            Log.e("Update", ">>>>>>>>>" + cur.getCount());
            while (cur.moveToNext()) {
                vendorName = cur.getString(cur.getColumnIndex("VENDOR_NAME"));

            }

            db.close();

        } catch (Exception e) {
            Log.e("ClsVendor", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }

        return vendorName;
    }
}


