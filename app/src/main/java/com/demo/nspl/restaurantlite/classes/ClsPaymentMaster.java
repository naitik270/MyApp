package com.demo.nspl.restaurantlite.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.demo.nspl.restaurantlite.Customer.ClsCustomerWisePayment;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.VendorPayment.ClsVendorWisePayment;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClsPaymentMaster implements Serializable {

    int PaymentID = 0, VendorID = 0;
    Double adjustmentAmount = 0.0;
    int orderId = 0;

    Double Amount = 0.0;

    String PaymentDate = "", PaymentMounth = "", MobileNo = "", CustomerName = "",
            VendorName = "", PaymentMode = "", PaymentDetail = "",
            InvoiceNo = "", Remark = "", EntryDate = "", Type = "", ReceiptNo = "",OrderNo = "";


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }


    private static SQLiteDatabase db;

    static Context context;

    public ClsPaymentMaster(Context ctx) {
        context = ctx;
    }

    public ClsPaymentMaster() {

    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public Double getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public void setAdjustmentAmount(Double adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
    }

    public int getPaymentID() {
        return PaymentID;
    }

    public void setPaymentID(int paymentID) {
        PaymentID = paymentID;
    }

    public int getVendorID() {
        return VendorID;
    }

    public void setVendorID(int vendorID) {
        VendorID = vendorID;
    }

    public String getReceiptNo() {
        return ReceiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        ReceiptNo = receiptNo;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public String getPaymentDate() {
        return PaymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        PaymentDate = paymentDate;
    }

    public String getPaymentMounth() {
        return PaymentMounth;
    }

    public void setPaymentMounth(String paymentMounth) {
        PaymentMounth = paymentMounth;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getVendorName() {
        return VendorName;
    }

    public void setVendorName(String vendorName) {
        VendorName = vendorName;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    public String getPaymentDetail() {
        return PaymentDetail;
    }

    public void setPaymentDetail(String paymentDetail) {
        PaymentDetail = paymentDetail;
    }

    public String getInvoiceNo() {
        return InvoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        InvoiceNo = invoiceNo;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getEntryDate() {
        return EntryDate;
    }

    public void setEntryDate(String entryDate) {
        EntryDate = entryDate;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }


    @SuppressLint("WrongConstant")
    public static Double getTotalSaleAmount() {
        Double TotalSaleAmount = 0.0;
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String qry = "SELECT SUM(IFNULL([TotalReceiveableAmount],0)) AS [TotalReceiveableAmount] "
                    .concat(" FROM [InventoryOrderMaster]");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--TotalSaleAmount--", "qry:  " + qry);
            Log.e("--TotalSaleAmount--", "TotalSaleAmount:  " + cur.getCount());

            while (cur.moveToNext()) {
                TotalSaleAmount += cur.getDouble(cur.getColumnIndex("TotalReceiveableAmount"));
            }
            cur.close();
        } catch (Exception e) {
            e.getMessage();
            Log.e("ClsCheckInMaster", "getCheckInNo-->>>>>>>>" + e.getMessage());
        }
        return TotalSaleAmount;
    }


    @SuppressLint("WrongConstant")
    public static Double getTotalAmount(String where) {
        Double TotalAmount = 0.0;
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String qry = "SELECT SUM([AMOUNT]) AS [AMOUNT]"
                    .concat(" FROM [PaymentMaster] WHERE 1=1 ").concat(where);

            Cursor cur = db.rawQuery(qry, null);
            while (cur.moveToNext()) {
                TotalAmount += cur.getDouble(cur.getColumnIndex("AMOUNT"));
            }
            cur.close();
        } catch (Exception e) {
            e.getMessage();
            Log.e("ClsCheckInMaster", "getCheckInNo-->>>>>>>>" + e.getMessage());
        }
        return TotalAmount;
    }

    @SuppressLint("WrongConstant")
    public static ClsPaymentMaster queryByOrderID(String id, Context context) {

        Log.e("--Payment--", String.valueOf(id));

        ClsPaymentMaster currentObj = new ClsPaymentMaster();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name,
                    Context.MODE_APPEND, null);


            String qry = ("SELECT [PaymentID],[PaymentMounth],[PaymentDate]," +
                    "[VendorName],[CustomerName],[MobileNo],[PaymentMode],[PaymentDetail]," +
                    "[Amount],[ReceiptNo],[VendorID],[Remark],[Type],[InvoiceNo] " +
                    "FROM [PaymentMaster] WHERE OrderID = ")
                    .concat(id)
                    .concat(";");

            Cursor cur = db.rawQuery(qry, null);
            Log.e("--Payment--", "queryById: " + qry);

            while (cur.moveToNext()) {
                currentObj.setPaymentID(cur.getInt(cur.getColumnIndex("PaymentID")));
                currentObj.setPaymentMounth(cur.getString(cur.getColumnIndex("PaymentMounth")));
                currentObj.setPaymentDate(cur.getString(cur.getColumnIndex("PaymentDate")));
                currentObj.setVendorName(cur.getString(cur.getColumnIndex("VendorName")));
                currentObj.setCustomerName(cur.getString(cur.getColumnIndex("CustomerName")));
                currentObj.setMobileNo(cur.getString(cur.getColumnIndex("MobileNo")));
                currentObj.setPaymentMode(cur.getString(cur.getColumnIndex("PaymentMode")));
                currentObj.setPaymentDetail(cur.getString(cur.getColumnIndex("PaymentDetail")));
                currentObj.setRemark(cur.getString(cur.getColumnIndex("Remark")));
                currentObj.setReceiptNo(cur.getString(cur.getColumnIndex("ReceiptNo")));
                currentObj.setVendorID(cur.getInt(cur.getColumnIndex("VendorID")));
                currentObj.setInvoiceNo(cur.getString(cur.getColumnIndex("InvoiceNo")));
                currentObj.setType(cur.getString(cur.getColumnIndex("Type")));
                currentObj.setAmount(cur.getDouble(cur.getColumnIndex("Amount")));
            }

            db.close();
        } catch (Exception e) {
            Log.e("--Payment--", ">>GetList" + e.getMessage());
            e.getMessage();
        }
        return currentObj;
    }


/*
    @SuppressLint("WrongConstant")
    public static int Insert(ClsPaymentMaster ObjclsPaymentMaster, Context context) {
        int result = 0;

        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String qry = ("INSERT INTO [PaymentMaster] ([PaymentDate]," +
                    "[PaymentMounth],[VendorID],[MobileNo]" +
                    ",[CustomerName],[VendorName],[PaymentMode]," +
                    "[PaymentDetail],[InvoiceNo],[Amount],[Remark]," +
                    "[EntryDate],[Type],[ReceiptNo]) VALUES ('")

                    .concat(ClsGlobal.getDbDateFormat(ObjclsPaymentMaster.getPaymentDate()))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(String.valueOf(ObjclsPaymentMaster.getPaymentMounth()))
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(ObjclsPaymentMaster.getVendorID()))
                    .concat(",")

                    .concat("'")
                    .concat(String.valueOf(ObjclsPaymentMaster.getMobileNo()))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjclsPaymentMaster.getCustomerName().trim().replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjclsPaymentMaster.getVendorName().trim().replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(String.valueOf(ObjclsPaymentMaster.getPaymentMode()))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjclsPaymentMaster.getPaymentDetail().trim().replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(String.valueOf(ObjclsPaymentMaster.getInvoiceNo()))
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(ObjclsPaymentMaster.getAmount()))
                    .concat(",")

                    .concat("'")
                    .concat(ObjclsPaymentMaster.getRemark().trim().replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(String.valueOf(ObjclsPaymentMaster.getEntryDate()))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(String.valueOf(ObjclsPaymentMaster.getType()))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjclsPaymentMaster.getReceiptNo())
                    .concat("');");

            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.e("--Payment--", "Insert- " + qry);
            Log.e("--Payment--", "Insert- " + result);

            db.close();
            return result;

        } catch (Exception e) {
            Log.e("ClsTerms", "PaymentMaster>>>>>>>>" + e.getMessageSales());
            e.getMessageSales();
        }
        return result;

    }
    */

    @SuppressLint("WrongConstant")
    public static int Insert(ClsPaymentMaster objClsPaymentMaster, Context context) {
        int result = 0;

        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String qry = ("INSERT INTO [PaymentMaster] ([PaymentDate]," +
                    "[PaymentMounth],[VendorID],[MobileNo]" +
                    ",[CustomerName],[VendorName],[PaymentMode]," +
                    "[PaymentDetail],[InvoiceNo],[Amount],[Remark]," +
                    "[EntryDate],[Type],[ReceiptNo],[OrderID]) VALUES ('")

                    .concat(ClsGlobal.getDbDateFormat(objClsPaymentMaster.getPaymentDate()))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(String.valueOf(objClsPaymentMaster.getPaymentMounth()))
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(objClsPaymentMaster.getVendorID()))
                    .concat(",")

                    .concat("'")
                    .concat(String.valueOf(objClsPaymentMaster.getMobileNo()))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objClsPaymentMaster.getCustomerName().trim().replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objClsPaymentMaster.getVendorName().trim().replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objClsPaymentMaster.getPaymentMode())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objClsPaymentMaster.getPaymentDetail().trim().replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objClsPaymentMaster.getInvoiceNo())
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(objClsPaymentMaster.getAmount()))
                    .concat(",")

                    .concat("'")
                    .concat(objClsPaymentMaster.getRemark().trim().replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ClsGlobal.getCurruntDateTime())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objClsPaymentMaster.getType())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objClsPaymentMaster.getReceiptNo())
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(objClsPaymentMaster.getOrderId()))
                    .concat(");");

            SQLiteStatement statement = db.compileStatement(qry);
            result = (int) statement.executeInsert();
            Log.e("--Payment--", "Insert- " + qry);
            Log.e("--Payment--", "Insert- " + result);

            db.close();
            return result;

        } catch (Exception e) {
            Log.e("ClsTerms", "PaymentMaster>>>>>>>>" + e.getMessage());
            e.getMessage();
        }
        return result;

    }

    @SuppressLint("WrongConstant")
    public static List<String> getPaymentMasterColumn(Context context,
                                                      io.requery.android.database.sqlite.SQLiteDatabase db) {
        List<String> columns = new ArrayList<>();
        try {
//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

//            io.requery.android.database.sqlite.SQLiteDatabase db;
//
//            db = io.requery.android.database.sqlite.SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath(ClsGlobal.Database_Name).getPath(),
//                    null);

            String qry = "SELECT * FROM [PaymentMaster] LIMIT 1 ";
            Cursor cur = db.rawQuery(qry, null);
            Log.e("--Select--", ">>>>>>>>>" + cur.getCount());
            if (cur != null) {
                for (String c : cur.getColumnNames()) {
                    columns.add(c);
                }
            }
//            db.close();
        } catch (Exception e) {
            Log.e("PaymentMaster", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }
        return columns;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsPaymentMaster> getListMonthWiseCustomer(Context context) {
        List<ClsPaymentMaster> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

//            String qry = "SELECT [PaymentMounth], SUM(Amount) as Amount FROM [PaymentMaster] GROUP BY PaymentMounth ORDER BY [PaymentMounth] DESC";

            String qry = "SELECT [PaymentMounth]"
                    .concat(",SUM([Amount]) AS [CustomerAmount]")
                    .concat(" FROM [PaymentMaster] WHERE 1=1 ")
                    .concat(" AND [Type] = 'Customer' ")
                    .concat(" GROUP BY [PaymentMounth]")
                    .concat(" ORDER BY [PaymentDate] DESC");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));
            Log.e("qry", qry);

            while (cur.moveToNext()) {
                ClsPaymentMaster getSet = new ClsPaymentMaster();
                getSet.setPaymentMounth(cur.getString(cur.getColumnIndex("PaymentMounth")));
                getSet.set_totalCustomerAmount(cur.getDouble(cur.getColumnIndex("CustomerAmount")));
                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static Double getTotalCollectedAmount(Context context, String _receiptNo, int OrderId) {
        Double result = 0.0;

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String qry = "SELECT SUM(IFNULL (Amount,0)) AS [Amount] FROM [PaymentMaster] WHERE"
                    .concat(" [ReceiptNo] = '")
                    .concat(_receiptNo + "'")
                    .concat(" AND [OrderId] = ")
                    .concat("'")
                    .concat(String.valueOf(OrderId))
                    .concat("'");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--cur--", String.valueOf(cur.getCount()));
            Log.e("--cur--", "Qry: " + qry);

            while (cur.moveToNext()) {
                result = cur.getDouble(cur.getColumnIndex("Amount"));
            }

            /*SELECT * FROM [InventoryOrderDetail] where 1=1
                    AND [OrderNo] = '7' AND [OrderID] = 13*/


        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return result;
    }

/*

    @SuppressLint("WrongConstant")
    public static int getLastReceiptNo(Context context) {
        int getLastReceiptNo = 1;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

//            String qry = "SELECT [PaymentID] FROM [PaymentMaster] WHERE [PaymentID] = last_insert_rowid()";
            String qry = "SELECT MAX([PaymentID]) AS [PaymentID] FROM [PaymentMaster] ";

            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));
            Log.e("qry", qry);

            while (cur.moveToNext()) {
                getLastReceiptNo = cur.getInt(cur.getColumnIndex("PaymentID"));
            }


        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessageSales());
            e.getMessageSales();
        }
        return getLastReceiptNo;
    }

*/

    public Double get_totalVendorAmount() {
        return _totalVendorAmount;
    }

    public void set_totalVendorAmount(Double _totalVendorAmount) {
        this._totalVendorAmount = _totalVendorAmount;
    }

    Double _totalVendorAmount = 0.0;

    public Double get_totalCustomerAmount() {
        return _totalCustomerAmount;
    }

    public void set_totalCustomerAmount(Double _totalCustomerAmount) {
        this._totalCustomerAmount = _totalCustomerAmount;
    }

    Double _totalCustomerAmount = 0.0;

    @SuppressLint("WrongConstant")
    public static List<ClsPaymentMaster> getListMonthWise(Context context) {

        List<ClsPaymentMaster> list = new ArrayList<>();

        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

//            String qry = "SELECT [PaymentMounth], SUM(Amount) as Amount FROM [PaymentMaster] GROUP BY PaymentMounth ORDER BY [PaymentMounth] DESC";


            String qry = "SELECT "
                    .concat("[PaymentMounth]")
                    .concat(",SUM([Amount]) AS [Amount]")
                    .concat(" FROM [PaymentMaster] WHERE 1=1 ")
//                    .concat(" AND [Type] = 'Customer' ")
                    .concat(" AND [Type] = 'Vendor' ")
                    .concat(" GROUP BY [PaymentMounth]")
                    .concat(" ORDER BY [PaymentDate] DESC");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));

            Log.e("--List--", "Customer:  " + qry);

            while (cur.moveToNext()) {
                ClsPaymentMaster getSet = new ClsPaymentMaster();
                getSet.setPaymentMounth(cur.getString(cur.getColumnIndex("PaymentMounth")));

                getSet.set_totalVendorAmount(cur.getDouble(cur.getColumnIndex("Amount")));

                list.add(getSet);
            }


        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static ClsPaymentMaster queryById(String receiptNO, Context context) {

        Log.e("--Payment--", String.valueOf(receiptNO));

        ClsPaymentMaster currentObj = new ClsPaymentMaster();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);


            String qry = ("SELECT [PaymentMounth],[PaymentDate]," +
                    "[VendorName],[CustomerName],[MobileNo],[PaymentMode],[PaymentDetail]," +
                    "[Amount],[ReceiptNo],[VendorID],[Remark],[Type],[InvoiceNo] " +
                    "FROM [PaymentMaster] WHERE ReceiptNo = ")
                    .concat("'")
                    .concat(receiptNO)
                    .concat("'")
                    .concat(";");

            Cursor cur = db.rawQuery(qry, null);
            Log.e("--Payment--", "queryById: " + qry);

            while (cur.moveToNext()) {
                currentObj.setPaymentMounth(cur.getString(cur.getColumnIndex("PaymentMounth")));
                currentObj.setPaymentDate(cur.getString(cur.getColumnIndex("PaymentDate")));
                currentObj.setVendorName(cur.getString(cur.getColumnIndex("VendorName")));
                currentObj.setCustomerName(cur.getString(cur.getColumnIndex("CustomerName")));
                currentObj.setMobileNo(cur.getString(cur.getColumnIndex("MobileNo")));
                currentObj.setPaymentMode(cur.getString(cur.getColumnIndex("PaymentMode")));
                currentObj.setPaymentDetail(cur.getString(cur.getColumnIndex("PaymentDetail")));
                currentObj.setRemark(cur.getString(cur.getColumnIndex("Remark")));
                currentObj.setReceiptNo(cur.getString(cur.getColumnIndex("ReceiptNo")));
                currentObj.setVendorID(cur.getInt(cur.getColumnIndex("VendorID")));
                currentObj.setInvoiceNo(cur.getString(cur.getColumnIndex("InvoiceNo")));
                currentObj.setType(cur.getString(cur.getColumnIndex("Type")));
                currentObj.setAmount(cur.getDouble(cur.getColumnIndex("Amount")));
            }

        } catch (Exception e) {
            Log.e("--Payment--", ">>GetList" + e.getMessage());
            e.getMessage();
        }
        return currentObj;
    }


    @SuppressLint("WrongConstant")
    public static int updatePaymentReport(ClsPaymentMaster objPaymentMaster, Context context) {
        int result = 0;
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String strSql = "UPDATE [PaymentMaster] SET "

                    .concat("[PaymentDate] = ")
                    .concat("'")
                    .concat(ClsGlobal.getDbDateFormat(objPaymentMaster.getPaymentDate()))
                    .concat("'")

                    .concat(" ,[PaymentMounth] = ")
                    .concat("'")
                    .concat(objPaymentMaster.getPaymentMounth())
                    .concat("'")

                    .concat(" ,[VendorID] = ")
                    .concat(String.valueOf(objPaymentMaster.getVendorID()))

                    .concat(" ,[MobileNo] = ")
                    .concat("'")
                    .concat(objPaymentMaster.getMobileNo())
                    .concat("'")

                    .concat(" ,[CustomerName] = ")
                    .concat("'")
                    .concat(objPaymentMaster.getCustomerName())
                    .concat("'")

                    .concat(" ,[VendorName] = ")
                    .concat("'")
                    .concat(objPaymentMaster.getVendorName())
                    .concat("'")

                    .concat(" ,[PaymentMode] = ")
                    .concat("'")
                    .concat(objPaymentMaster.getPaymentMode())
                    .concat("'")

                    .concat(" ,[PaymentDetail] = ")
                    .concat("'")
                    .concat(objPaymentMaster.getPaymentDetail())
                    .concat("'")

                    .concat(" ,[InvoiceNo] = ")
                    .concat("'")
                    .concat(objPaymentMaster.getInvoiceNo())
                    .concat("'")

                    .concat(" ,[Amount] = ")
                    .concat(String.valueOf(objPaymentMaster.getAmount()))

                    .concat(" ,[Remark] = ")
                    .concat("'")
                    .concat(objPaymentMaster.getRemark())
                    .concat("'")

                    .concat(" ,[Type] = ")
                    .concat("'")
                    .concat(objPaymentMaster.getType())
                    .concat("'")

                    .concat(" ,[OrderID] = ")
                    .concat(String.valueOf(objPaymentMaster.getOrderId()))

                    .concat(" WHERE [ReceiptNo] = ")
                    .concat("'")
                    .concat(objPaymentMaster.getReceiptNo())
                    .concat("'")
                    .concat(";");


            Log.e("--Payment--", "Update- " + strSql);
            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();

            Log.e("--Payment--", "result- " + result);
            db.close();

        } catch (Exception e) {
            Log.e("ClsItem", "Update" + e.getMessage());
        }
        return result;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsPaymentMaster> getListPaymentDetails(String where, Context context) {
        List<ClsPaymentMaster> list = new ArrayList<>();
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            String qry = "SELECT [PaymentID], [PaymentMounth],[PaymentDate],[VendorName],[PaymentMode]" +
                    (",[PaymentDetail],[Amount],[ReceiptNo],[VendorID]," +
                            "[CustomerName],[MobileNo],[Remark] FROM [PaymentMaster] WHERE 1=1 ")
                            .concat(where)
                            .concat(" ORDER BY [PaymentDate] ");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));
            Log.e("qry", qry);

            Log.e("--Purchase--", "qry- " + qry);


            while (cur.moveToNext()) {
                ClsPaymentMaster getSet = new ClsPaymentMaster();
                getSet.setPaymentID(cur.getInt(cur.getColumnIndex("PaymentID")));
                getSet.setPaymentMounth(cur.getString(cur.getColumnIndex("PaymentMounth")));
                getSet.setPaymentDate(cur.getString(cur.getColumnIndex("PaymentDate")));
                getSet.setVendorName(cur.getString(cur.getColumnIndex("VendorName")));
                getSet.setPaymentMode(cur.getString(cur.getColumnIndex("PaymentMode")));
                getSet.setPaymentDetail(cur.getString(cur.getColumnIndex("PaymentDetail")));
                getSet.setRemark(cur.getString(cur.getColumnIndex("Remark")));
                getSet.setCustomerName(cur.getString(cur.getColumnIndex("CustomerName")));
                getSet.setReceiptNo(cur.getString(cur.getColumnIndex("ReceiptNo")));
                getSet.setVendorID(cur.getInt(cur.getColumnIndex("VendorID")));
                getSet.setMobileNo(cur.getString(cur.getColumnIndex("MobileNo")));

                getSet.setAmount(cur.getDouble(cur.getColumnIndex("Amount")));
                list.add(getSet);
            }




        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }finally {
            db.close();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsCustomerWisePayment> getCustomerwisePaymentGOT(SQLiteDatabase db, String where, Context context) {
        List<ClsCustomerWisePayment> list = new ArrayList<>();
        try {
            String qry = "SELECT [PaymentID],[PaymentMounth],[PaymentDate],[VendorName],[PaymentMode]" +
                    (",[PaymentDetail],[Amount],[ReceiptNo],[VendorID]," +
                            "[CustomerName],[MobileNo],[Remark] FROM [PaymentMaster] WHERE 1=1 ")
                            .concat(where).concat(" ORDER BY [PaymentDate] DESC");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--Purchase--", "qry: " + qry);
            Log.e("--Purchase--", "count: " + cur.getCount());


            while (cur.moveToNext()) {
                ClsCustomerWisePayment getSet = new ClsCustomerWisePayment();
                getSet.setPaymentID(cur.getInt(cur.getColumnIndex("PaymentID")));
                getSet.setPaymentMounth(cur.getString(cur.getColumnIndex("PaymentMounth")));
                getSet.setPaymentDate(cur.getString(cur.getColumnIndex("PaymentDate")));
                getSet.setVendorName(cur.getString(cur.getColumnIndex("VendorName")));
                getSet.setPaymentMode(cur.getString(cur.getColumnIndex("PaymentMode")));
                getSet.setPaymentDetail(cur.getString(cur.getColumnIndex("PaymentDetail")));
                getSet.setRemark(cur.getString(cur.getColumnIndex("Remark")));
                getSet.setCustomerName(cur.getString(cur.getColumnIndex("CustomerName")));
                getSet.setReceiptNo(cur.getString(cur.getColumnIndex("ReceiptNo")));
                getSet.setVendorID(cur.getInt(cur.getColumnIndex("VendorID")));
                getSet.setMobileNo(cur.getString(cur.getColumnIndex("MobileNo")));
                getSet.setAmount(cur.getDouble(cur.getColumnIndex("Amount")));

                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsVendorWisePayment> getVendorWisePaymentGOT(SQLiteDatabase db, String where, Context context) {
        List<ClsVendorWisePayment> list = new ArrayList<>();
        try {
            String qry = "SELECT [PaymentID],[PaymentMounth],[PaymentDate],[VendorName],[PaymentMode]" +
                    (",[PaymentDetail],[Amount],[ReceiptNo],[VendorID]," +
                            "[CustomerName],[MobileNo],[Remark] FROM [PaymentMaster] WHERE 1=1 ")
                            .concat(where).concat(" ORDER BY [PaymentDate] DESC");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--Purchase--", "qry: " + qry);
            Log.e("--Purchase--", "count: " + cur.getCount());


            while (cur.moveToNext()) {
                ClsVendorWisePayment getSet = new ClsVendorWisePayment();
                getSet.setPaymentID(cur.getInt(cur.getColumnIndex("PaymentID")));
                getSet.setPaymentMounth(cur.getString(cur.getColumnIndex("PaymentMounth")));
                getSet.setPaymentDate(cur.getString(cur.getColumnIndex("PaymentDate")));
                getSet.setVendorName(cur.getString(cur.getColumnIndex("VendorName")));
                getSet.setPaymentMode(cur.getString(cur.getColumnIndex("PaymentMode")));
                getSet.setPaymentDetail(cur.getString(cur.getColumnIndex("PaymentDetail")));
                getSet.setRemark(cur.getString(cur.getColumnIndex("Remark")));
                getSet.setCustomerName(cur.getString(cur.getColumnIndex("CustomerName")));
                getSet.setReceiptNo(cur.getString(cur.getColumnIndex("ReceiptNo")));
                getSet.setVendorID(cur.getInt(cur.getColumnIndex("VendorID")));
                getSet.setMobileNo(cur.getString(cur.getColumnIndex("MobileNo")));
                getSet.setAmount(cur.getDouble(cur.getColumnIndex("Amount")));

                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static ClsPaymentMaster getAmounts(String where, Context context) {
        ClsPaymentMaster AmountsObj = new ClsPaymentMaster();
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            String qry = "SELECT [Amount] FROM [PaymentMaster] WHERE 1=1 ".concat(where);

            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));
            Log.e("qry", qry);
            Log.e("--PaymentMaster--", "qry- " + qry);

            while (cur.moveToNext()) {
                AmountsObj.setAmount(cur.getDouble(cur.getColumnIndex("Amount")));
            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return AmountsObj;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsPaymentMaster> getVendorPayment(String where, Context context) {
        List<ClsPaymentMaster> list = new ArrayList<>();
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            String qry = "SELECT "
                    .concat(" [VendorID] ")
                    .concat(" ,SUM([Amount]) AS [Amount] ")
                    .concat(" FROM [PaymentMaster] WHERE 1=1 ")
                    .concat(where)
                    .concat(" GROUP BY [VendorID]")
                    .concat(" ORDER BY [VendorName]");


            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));
            Log.e("qry", qry);

            Log.e("--Purchase--", "qry- " + qry);


            while (cur.moveToNext()) {
                ClsPaymentMaster getSet = new ClsPaymentMaster();
                getSet.setVendorID(cur.getInt(cur.getColumnIndex("VendorID")));
                getSet.setAmount(cur.getDouble(cur.getColumnIndex("Amount")));
                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsPaymentMaster> getCustomerPayment(String where, Context context) {
        List<ClsPaymentMaster> list = new ArrayList<>();
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);


            String qry = "SELECT "
                    .concat(" [MobileNo] ")
                    .concat(" ,SUM([Amount]) AS [Amount] ")
                    .concat(" FROM [PaymentMaster] WHERE 1=1 ")
                    .concat(where)
                    .concat(" AND IFNULL([Type] ,'Customer') = 'Customer' ")
                    .concat(" GROUP BY [MobileNo] ")
                    .concat(" ORDER BY [CustomerName] ");


            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));
            Log.e("qry", qry);

            Log.e("--Purchase--", "qry- " + qry);


            while (cur.moveToNext()) {
                ClsPaymentMaster getSet = new ClsPaymentMaster();
                getSet.setMobileNo(cur.getString(cur.getColumnIndex("MobileNo")));
                getSet.setAmount(cur.getDouble(cur.getColumnIndex("Amount")));

                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }




//    @SuppressLint("WrongConstant")
//    public static List<ClsPaymentMaster> getListPaymentDetails(String where, Context context) {
//        List<ClsPaymentMaster> list = new ArrayList<>();
//        try {
//
//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//
//            String qry = "SELECT [PaymentID], [PaymentMounth],[PaymentDate],[VendorName],[PaymentMode]" +
//                    (",[PaymentDetail],[Amount],[ReceiptNo],[VendorID]," +
//                            "[CustomerName],[MobileNo],[Remark] FROM [PaymentMaster] WHERE 1=1 ")
//                            .concat(where)
//                            .concat(" ORDER BY [PaymentDate] ");
//
//            Cursor cur = db.rawQuery(qry, null);
//
//            Log.e("cur", String.valueOf(cur.getCount()));
//            Log.e("qry", qry);
//
//            Log.e("--Purchase--", "qry- " + qry);
//
//
//            while (cur.moveToNext()) {
//                ClsPaymentMaster getSet = new ClsPaymentMaster();
//                getSet.setPaymentID(cur.getInt(cur.getColumnIndex("PaymentID")));
//                getSet.setPaymentMounth(cur.getString(cur.getColumnIndex("PaymentMounth")));
//                getSet.setPaymentDate(cur.getString(cur.getColumnIndex("PaymentDate")));
//                getSet.setVendorName(cur.getString(cur.getColumnIndex("VendorName")));
//                getSet.setPaymentMode(cur.getString(cur.getColumnIndex("PaymentMode")));
//                getSet.setPaymentDetail(cur.getString(cur.getColumnIndex("PaymentDetail")));
//                getSet.setRemark(cur.getString(cur.getColumnIndex("Remark")));
//                getSet.setCustomerName(cur.getString(cur.getColumnIndex("CustomerName")));
//                getSet.setReceiptNo(cur.getString(cur.getColumnIndex("ReceiptNo")));
//                getSet.setVendorID(cur.getInt(cur.getColumnIndex("VendorID")));
//                getSet.setMobileNo(cur.getString(cur.getColumnIndex("MobileNo")));
//
//                getSet.setAmount(cur.getDouble(cur.getColumnIndex("Amount")));
//                list.add(getSet);
//            }
//
//
//
//
//        } catch (Exception e) {
//            Log.e("c", "GetList" + e.getMessage());
//            e.getMessage();
//        }finally {
//            db.close();
//        }
//        return list;
//    }


    @SuppressLint("WrongConstant")
    public static int Delete_Payment_Master(String OrderNo, Context context) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String strSql = "DELETE FROM [PaymentMaster] WHERE [ReceiptNo] = "
                    .concat("'").concat(OrderNo).concat("'")
                    .concat(";");
            Log.e("--PAYMENT_DELETE--", "Payment_Delete:" + strSql);
            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();
            Log.e("--PAYMENT_DELETE--", "Payment_Result: " + result);
            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsOrderDetailMaster", "Delete" + e.getMessage());
            e.getMessage();
        }
        return result;
    }

    @SuppressLint("WrongConstant")
    public static int deletePaymentReportData(int OrderNo, String type, Context context) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String strSql = "DELETE FROM [PaymentMaster] WHERE [ReceiptNo] = "
                    .concat("'")
                    .concat(String.valueOf(OrderNo))
                    .concat("'")

                    .concat(" AND [Type] = ")
                    .concat("'")
                    .concat(type)
                    .concat("'")
                    .concat(";");

            Log.e("--Payment--", "delete:" + strSql);
            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();
            Log.e("--Payment--", "delete:" + result);
            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsOrderDetailMaster", "Delete" + e.getMessage());
            e.getMessage();
        }
        return result;
    }

    @SuppressLint("WrongConstant")
    public static int deleteOpeningBalanceVendor(String vendorId, Context context) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String strSql = "DELETE FROM [PaymentMaster] WHERE 1=1 "
                    .concat(" AND [VendorID] =".concat(vendorId))
                    .concat(" AND [paymentmode] = 'Opening Balance' ")
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

    public static void InsertPaymentVendor(ClsPaymentMaster insertPaymentMaster, Context context) {

        Gson gson = new Gson();
        String jsonInString = gson.toJson(insertPaymentMaster);
        Log.d("amtsdf", "Insert- " + jsonInString);

        if (insertPaymentMaster.getAmount() != 0) {
            int resultPaymentMaster = ClsPaymentMaster.Insert(insertPaymentMaster, context);
            if (resultPaymentMaster != 0) {
                Log.e("resultInsertCustomer", "resultInsertCustomer Save Successfully:" + resultPaymentMaster);//
            } else {
                Log.e("resultInsertCustomer", "resultInsertCustomer Failed: " + resultPaymentMaster);//
            }
        }

    }


    @SuppressLint("WrongConstant")
    public static int deleteOpeningBalanceCustomer(String MobileNo, Context context) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String strSql = "DELETE FROM [PaymentMaster] WHERE "
                    .concat(" [Type] = ".concat("'Customer'"))
                    .concat(" AND [paymentmode] = 'Opening Balance' ")
                    .concat(" AND [MobileNo] = ".concat("'" + MobileNo + "'"))
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

    public static void InsertPaymentCustomer(ClsPaymentMaster insertPaymentMaster, Context context) {

        Gson gson = new Gson();
        String jsonInString = gson.toJson(insertPaymentMaster);
        Log.d("amtsdf", "Insert- " + jsonInString);

        if (insertPaymentMaster.getAmount() != 0) {
            int resultPaymentMaster = ClsPaymentMaster.Insert(insertPaymentMaster, context);
            if (resultPaymentMaster != 0) {
                Log.e("resultInsertCustomer", "resultInsertCustomer Save Successfully:" + resultPaymentMaster);//
            } else {
                Log.e("resultInsertCustomer", "resultInsertCustomer Failed: " + resultPaymentMaster);//
            }
        }

    }

    /*@SuppressLint("WrongConstant")
    public static int getLastReceiptNo(Context context) {
        int getLastReceiptNo = 1;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

//            String qry = "SELECT [PaymentID] FROM [PaymentMaster] WHERE [PaymentID] = last_insert_rowid()";
            String qry = "SELECT MAX([PaymentID]) AS [PaymentID] FROM [PaymentMaster] ";

            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));
            Log.e("qry", qry);

            while (cur.moveToNext()) {
                getLastReceiptNo = cur.getInt(cur.getColumnIndex("PaymentID"));
            }


        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessageSales());
            e.getMessageSales();
        }
        return getLastReceiptNo;
    }*/

    @SuppressLint("WrongConstant")
    public static int getLastReceiptNo(Context context) {
        int getLastReceiptNo = 1;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

//            String qry = "SELECT [PaymentID] FROM [PaymentMaster] WHERE [PaymentID] = last_insert_rowid()";
//            String qry = "SELECT MAX([PaymentID]) AS [PaymentID] FROM [PaymentMaster] ";
            String qry = "SELECT MAX([OrderDetailID]) AS [OrderDetailID] FROM [InventoryOrderDetail] ";

            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));
            Log.e("qry", qry);

            while (cur.moveToNext()) {
                getLastReceiptNo = cur.getInt(cur.getColumnIndex("OrderDetailID"));
            }


        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return getLastReceiptNo;
    }


}
