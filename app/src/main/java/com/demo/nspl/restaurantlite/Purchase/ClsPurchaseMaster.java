package com.demo.nspl.restaurantlite.Purchase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.VendorLedger.ClsVendorLedger;
import com.demo.nspl.restaurantlite.VendorPayment.ClsVendorWisePayment;

import java.util.ArrayList;
import java.util.List;

public class ClsPurchaseMaster {


    int PurchaseID = 0;
    int PurchaseNo;

    String BillNO = "";
    String PurchaseDate = "";
    int VendorID = 0;
    String Remark = "";
    String EntryDate = "";


    Double PurchaseVal = 0.0;
    Double PaymentVal = 0.0;


    int _updatePurchaseMaster = 0;

    public int get_updatePurchaseMaster() {
        return _updatePurchaseMaster;
    }

    public void set_updatePurchaseMaster(int _updatePurchaseMaster) {
        this._updatePurchaseMaster = _updatePurchaseMaster;
    }

    int _PurchaseMasterResult = 0;
    int _PurchaseDetailResult = 0;

    public int get_PurchaseMasterResult() {
        return _PurchaseMasterResult;
    }

    public void set_PurchaseMasterResult(int _PurchaseMasterResult) {
        this._PurchaseMasterResult = _PurchaseMasterResult;
    }

    public int get_PurchaseDetailResult() {
        return _PurchaseDetailResult;
    }

    public void set_PurchaseDetailResult(int _PurchaseDetailResult) {
        this._PurchaseDetailResult = _PurchaseDetailResult;
    }


    public Double getPurchaseVal() {
        return PurchaseVal;
    }

    public void setPurchaseVal(Double purchaseVal) {
        PurchaseVal = purchaseVal;
    }

    public Double getPaymentVal() {
        return PaymentVal;
    }

    public void setPaymentVal(Double paymentVal) {
        PaymentVal = paymentVal;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    String vendorName = "";

    public int getPurchaseID() {
        return PurchaseID;
    }

    public void setPurchaseID(int purchaseID) {
        PurchaseID = purchaseID;
    }

    public int getPurchaseNo() {
        return PurchaseNo;
    }

    public void setPurchaseNo(int purchaseNo) {
        PurchaseNo = purchaseNo;
    }


    public String getBillNO() {
        return BillNO;
    }

    public void setBillNO(String billNO) {
        BillNO = billNO;
    }


    public String getPurchaseDate() {
        return PurchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        PurchaseDate = purchaseDate;
    }

    public int getVendorID() {
        return VendorID;
    }

    public void setVendorID(int vendorID) {
        VendorID = vendorID;
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


    private static int result;
    static Context context;
    static SQLiteDatabase db;

    public ClsPurchaseMaster(Context ctx) {

        context = ctx;
    }

    public ClsPurchaseMaster() {
    }


    public static int Insert(ClsPurchaseMaster objParams, List<ClsPurchaseDetail> lstDetails) {
        result = 0;
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_PRIVATE, null);
            String qry = "INSERT INTO [PurchaseMaster] ([PurchaseNo],[PurchaseDate],[BillNO],[VendorID],[Remark],[EntryDate]) VALUES ("
                    .concat(String.valueOf(objParams.getPurchaseNo()))
                    .concat(",")

                    .concat("'")
                    .concat(objParams.getPurchaseDate())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objParams.getBillNO())
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(objParams.getVendorID()))
                    .concat(",")

                    .concat("'")
                    .concat(objParams.getRemark())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ClsGlobal.getEntryDateTime())
                    .concat("'")
                    .concat(");");

            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.d("--Purchase--", "Insert_PurchaseMaster: " + qry);


            Cursor c = db.rawQuery("SELECT last_insert_rowid()", null);
            c.moveToFirst();
            ClsGlobal.lastPurchaseID = c.getInt(0);
            Log.e("id:-- ", String.valueOf(ClsGlobal.lastPurchaseID));

            objParams.setPurchaseID(ClsGlobal.lastPurchaseID);

            for (ClsPurchaseDetail objClsPurchaseDetail : lstDetails) {
                try {
                    String sub_qry = "INSERT INTO [PurchaseDetail] ([PurchaseID],[MonthYear],[ItemID],[ItemCode],[Unit],[Quantity],[Rate],[TotalAmount],[Discount],[NetAmount],[ApplyTax],[CGST],[SGST],[IGST],[TotalTaxAmount],[GrandTotal]) VALUES ("
                            .concat(String.valueOf(objParams.getPurchaseID()))
                            .concat(",")

                            .concat("'")
                            .concat(ClsGlobal.getMonthYearPurchase(objParams.getPurchaseDate()))
                            .concat("'")
                            .concat(",")

                            .concat(String.valueOf(objClsPurchaseDetail.getItemID()))
                            .concat(",")

                            .concat("'")
                            .concat(objClsPurchaseDetail.getItemCode())
                            .concat("'")
                            .concat(",")

                            .concat("'")
                            .concat(objClsPurchaseDetail.getUnit())
                            .concat("'")
                            .concat(",")

                            .concat(String.valueOf(objClsPurchaseDetail.getQuantity()))
                            .concat(",")

                            .concat(String.valueOf(objClsPurchaseDetail.getRate()))
                            .concat(",")

                            .concat(String.valueOf(objClsPurchaseDetail.getTotalAmount()))
                            .concat(",")

                            .concat(String.valueOf(objClsPurchaseDetail.getDiscount()))
                            .concat(",")

                            .concat(String.valueOf(objClsPurchaseDetail.getNetAmount()))
                            .concat(",")

                            .concat("'")
                            .concat(String.valueOf(objClsPurchaseDetail.getApplyTax()))
                            .concat("'")
                            .concat(",")

                            .concat(String.valueOf(objClsPurchaseDetail.getCGST()))
                            .concat(",")

                            .concat(String.valueOf(objClsPurchaseDetail.getSGST()))
                            .concat(",")

                            .concat(String.valueOf(objClsPurchaseDetail.getIGST()))
                            .concat(",")

                            .concat(String.valueOf(objClsPurchaseDetail.getTotalTaxAmount()))
                            .concat(",")

                            .concat(String.valueOf(objClsPurchaseDetail.getGrandTotal()))
                            .concat(");");

                    db.execSQL(sub_qry);
                    Log.d("--Purchase--", "Insert_PurchaseDetail: " + sub_qry);
                } catch (Exception e) {
                    Log.e("jsonObject", "Insert------" + e.getMessage());
                }
            }


            db.close();
        } catch (Exception e) {
            result = -1;
            e.getMessage();
        }
        return result;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsPurchaseMaster> getVendorListByMonthYear(Context context, String _where) {
        List<ClsPurchaseMaster> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
/*
            String qry = "SELECT DISTINCT P.[VendorID],V.[VENDOR_ID],V.[VENDOR_NAME] FROM [PurchaseMaster] AS P "
                    .concat("INNER JOIN [VENDOR_MASTER] AS V ON V.[VENDOR_ID] = P.[VendorID] ")
                    .concat("WHERE 1=1 ")
                    .concat(_where);*/

            String qry = "SELECT DISTINCT P.[VendorID],V.[VENDOR_ID],V.[VENDOR_NAME] FROM [VENDOR_MASTER] AS V "
                    .concat("INNER JOIN [PurchaseMaster] AS P ON  P.[VendorID] = V.[VENDOR_ID]  ")
                    .concat("WHERE 1=1 ")
                    .concat(_where);


            Cursor cur = db.rawQuery(qry, null);

            Log.e("--qry--", "VendorQry: " + qry);
            Log.e("--qry--", "Count: " + cur.getCount());

            while (cur.moveToNext()) {
                ClsPurchaseMaster getSet = new ClsPurchaseMaster();


                Log.e("--qry--", "VendorID: " + cur.getInt(cur.getColumnIndex("VendorID")));
                Log.e("--qry--", "VENDOR_NAME: " + cur.getString(cur.getColumnIndex("VENDOR_NAME")));


                getSet.setVendorID(cur.getInt(cur.getColumnIndex("VENDOR_ID")));
//                getSet.setPurchaseDate(cur.getString(cur.getColumnIndex("PurchaseDate")));
                getSet.setVendorName(cur.getString(cur.getColumnIndex("VENDOR_NAME")));
                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("ClsVendor", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsVendorLedger> getCustomerLedgerList(String _where, Context context) {

        List<ClsVendorLedger> list = new ArrayList<>();

        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT CM.[NAME], CM.[MOBILE_NO],CM.[Credit], CM.[Company_Name], CM.[GST_NO], " +
                    "SUM(ORD.[TotalReceiveableAmount]) AS [TotalSaleAmount], 0 AS [TotalPaymentAmount]  FROM [CustomerMaster] AS CM "
                            .concat(" LEFT JOIN [InventoryOrderMaster] AS ORD ON ORD.[MobileNo] = CM.[MOBILE_NO]   ")
                            //.concat(" LEFT JOIN [PaymentMaster] AS PM ON PM.[MobileNo] = CM.[MOBILE_NO]   ")
                            .concat(" WHERE 1=1 ")
                            .concat(_where)
                            //.concat(" AND IFNULL(PM.[Type] ,'Customer') = 'Customer' ")
                            //.concat(_where)
                            .concat(" GROUP BY CM.[NAME], CM.[MOBILE_NO], CM.[Company_Name], CM.[GST_NO] ")
                            .concat(" ORDER BY CM.[NAME] ");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--Purchase--", "cur" + String.valueOf(cur.getCount()));
            Log.e("--Purchase--", "Query: " + qry);

            if (cur.getCount() != 0) {
                while (cur.moveToNext()) {
                    ClsVendorLedger getSet = new ClsVendorLedger();
                    getSet.setCusomterName(cur.getString(cur.getColumnIndex("NAME")));
                    getSet.setCreditLimit(cur.getDouble(cur.getColumnIndex("Credit")));
                    getSet.setCustomerMobileNo(cur.getString(cur.getColumnIndex("MOBILE_NO")));
                    getSet.setCompanyName(cur.getString(cur.getColumnIndex("Company_Name")));
                    getSet.setGST_NO(cur.getString(cur.getColumnIndex("GST_NO")));

                    getSet.setTotalSaleAmount(cur.getDouble(cur.getColumnIndex("TotalSaleAmount")));
                    getSet.setTotalPaymentAmount(cur.getDouble(cur.getColumnIndex("TotalPaymentAmount")));

                    list.add(getSet);
                }
            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static boolean checkExists(String _where, Context context) {
        boolean result = false;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = "SELECT 1 FROM [PurchaseMaster] WHERE 1=1 "
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
    public static List<ClsPurchaseDetail> getListMonthWise(Context context) {

        List<ClsPurchaseDetail> list = new ArrayList<>();

        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT PD.[MonthYear], SUM(PD.GrandTotal) as GrandTotal FROM [PurchaseDetail] AS PD " +
                    "INNER JOIN [PurchaseMaster] AS PM ON PM.[PurchaseID] = PD.[PurchaseID] WHERE 1=1" +
                    " GROUP BY PD.[MonthYear] ORDER BY PM.[PurchaseDate] DESC";

//            String qry = "SELECT [MonthYear], SUM(GrandTotal) as GrandTotal FROM [PurchaseDetail] GROUP BY MonthYear ORDER BY [MonthYear] DESC";

            Cursor cur = db.rawQuery(qry, null);


            while (cur.moveToNext()) {
                ClsPurchaseDetail getSet = new ClsPurchaseDetail();
                getSet.setMonthYear(cur.getString(cur.getColumnIndex("MonthYear")));
                getSet.setGrandTotal(Double.valueOf(ClsGlobal.round(cur.getDouble(cur.getColumnIndex("GrandTotal")), 2)));
                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsVendorLedger> getVendorLedgerList(String _where, Context context) {

        List<ClsVendorLedger> list = new ArrayList<>();

        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

          /*  String qry = "SELECT V.[VENDOR_ID], V.[VENDOR_NAME], SUM(PD.[GrandTotal]) AS [TotalPurchaseAmount], SUM(PM.[Amount]) AS [TotalPaymentAmount] FROM [VENDOR_MASTER] AS V "
                    .concat(" LEFT JOIN [PurchaseMaster] AS [Purchase] ON V.[VENDOR_ID] = Purchase.[VendorID] ")
                    .concat(" INNER JOIN [PurchaseDetail] AS PD ON PD.[PurchaseID] = Purchase.[PurchaseID] ")
                    .concat(" LEFT JOIN [PaymentMaster] AS PM ON PM.[VendorID] = V.[VENDOR_ID] WHERE 1=1 ")
                    .concat(_where)
                    .concat(" GROUP BY V.[VENDOR_ID], V.[VENDOR_NAME] ")
                    .concat(" ORDER BY V.[VENDOR_NAME] ");*/


            String qry = "SELECT V.[VENDOR_ID] ".concat(", V.[VENDOR_NAME],IFNULL(V.[OpeningStock],0) AS [OpeningStock] ")
                    .concat(", SUM(PD.[GrandTotal]) AS [TotalPurchaseAmount] ")
                    .concat(", 0 AS [TotalPaymentAmount] ")
                    .concat(" FROM [VENDOR_MASTER] AS V ")
                    .concat(" LEFT JOIN [PurchaseMaster] AS [Purchase] ON V.[VENDOR_ID] = Purchase.[VendorID] ")
                    .concat(" LEFT JOIN [PurchaseDetail] AS PD ON PD.[PurchaseID] = Purchase.[PurchaseID] ")
//                    .concat(" LEFT JOIN [PaymentMaster] AS PM ON PM.[VendorID] = V.[VENDOR_ID] WHERE 1=1 ")
                    .concat(" WHERE 1=1")
                    .concat(_where)
                    .concat(" GROUP BY V.[VENDOR_ID], V.[VENDOR_NAME] ")
                    .concat(" ORDER BY V.[VENDOR_NAME] ");


            Cursor cur = db.rawQuery(qry, null);

            Log.e("--Purchase--", "cur" + cur.getCount());
            Log.e("--Purchase--", "Query: " + qry);

            while (cur.moveToNext()) {
                ClsVendorLedger getSet = new ClsVendorLedger();
                getSet.setVENDOR_ID(cur.getInt(cur.getColumnIndex("VENDOR_ID")));
                getSet.setVENDOR_NAME(cur.getString(cur.getColumnIndex("VENDOR_NAME")));
                getSet.setOpeningStock(cur.getDouble(cur.getColumnIndex("OpeningStock")));
                getSet.setTotalPurchaseAmount(cur.getDouble(cur.getColumnIndex("TotalPurchaseAmount")));
                getSet.setTotalPaymentAmount(cur.getDouble(cur.getColumnIndex("TotalPaymentAmount")));

                Log.e("--Purchase--", "TotalPaymentAmount: " + cur.getDouble(cur.getColumnIndex("TotalPaymentAmount")));
                Log.e("--Purchase--", "setVENDOR_NAME: " + cur.getString(cur.getColumnIndex("VENDOR_NAME")));

                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    public double get_totalTax() {
        return _totalTax;
    }

    public void set_totalTax(double _totalTax) {
        this._totalTax = _totalTax;
    }

    double _totalTax = 0.0;

    @SuppressLint("WrongConstant")
    public static List<ClsPurchaseMaster> getPurchaseDetailList(SQLiteDatabase db,String _where, Context context) {
        List<ClsPurchaseMaster> lstClsPurchaseMasters = new ArrayList<>();
        try {

//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = "SELECT P.*,V.[VENDOR_NAME] ,SUM(PD.[TotalTaxAmount]) AS TotalTaxAmount,SUM(PD.[GrandTotal]) AS GrandTotal FROM [PurchaseMaster] AS P "
                    .concat(" INNER JOIN [VENDOR_MASTER] AS V ON V.[VENDOR_ID] = P.[VendorID] ")
                    .concat(" LEFT JOIN [PurchaseDetail] AS PD ON PD.[PurchaseID] = P.[PurchaseID] ")
                    .concat(" WHERE 1=1 ")
                    .concat(_where)
                    .concat(" GROUP BY P.PurchaseID,P.PurchaseNo,P.VendorID,P.BillNO,P.PurchaseDate,P.Remark,P.EntryDate,V.[VENDOR_NAME]")
                    .concat(";");

            Log.e("--VendorPurchase--", "Qry: " + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            Log.e("--VendorPurchase--", "Qry_Count: " + cur.getCount());

            while (cur.moveToNext()) {
                ClsPurchaseMaster Obj = new ClsPurchaseMaster();

                Obj.setPurchaseID(cur.getInt(cur.getColumnIndex("PurchaseID")));
                Obj.setPurchaseNo(cur.getInt(cur.getColumnIndex("PurchaseNo")));
                Obj.setPurchaseDate(cur.getString(cur.getColumnIndex("PurchaseDate")));
                Obj.setBillNO(cur.getString(cur.getColumnIndex("BillNO")));
                Obj.setVendorID(cur.getInt(cur.getColumnIndex("VendorID")));

                Obj.setVendorName(cur.getString(cur.getColumnIndex("VENDOR_NAME")));
                Obj.setPurchaseVal(cur.getDouble(cur.getColumnIndex("GrandTotal")));

                Obj.setEntryDate(cur.getString(cur.getColumnIndex("EntryDate")));
                Obj.setRemark(cur.getString(cur.getColumnIndex("Remark")));
                Obj.set_totalTax(cur.getDouble(cur.getColumnIndex("TotalTaxAmount")));


                lstClsPurchaseMasters.add(Obj);
            }

//            db.close();

        } catch (Exception e) {
            Log.e("ClsCategory", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }
        return lstClsPurchaseMasters;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsVendorWisePayment> getVendorWisePurchaseList(SQLiteDatabase db, String _where, Context context) {
        List<ClsVendorWisePayment> lstClsPurchaseMasters = new ArrayList<>();
        try {

//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = "SELECT P.*,V.[VENDOR_NAME] ,SUM(PD.[TotalTaxAmount]) AS TotalTaxAmount,SUM(PD.[GrandTotal]) AS GrandTotal FROM [PurchaseMaster] AS P "
                    .concat(" INNER JOIN [VENDOR_MASTER] AS V ON V.[VENDOR_ID] = P.[VendorID] ")
                    .concat(" LEFT JOIN [PurchaseDetail] AS PD ON PD.[PurchaseID] = P.[PurchaseID] ")
                    .concat(" WHERE 1=1 ")
                    .concat(_where)
                    .concat(" GROUP BY P.PurchaseID,P.PurchaseNo,P.VendorID,P.BillNO,P.PurchaseDate,P.Remark,P.EntryDate,V.[VENDOR_NAME]")
                    .concat(";");

            Log.e("--VendorPurchase--", "Qry: " + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            Log.e("--VendorPurchase--", "Qry_Count: " + cur.getCount());

            while (cur.moveToNext()) {
                ClsVendorWisePayment Obj = new ClsVendorWisePayment();

                Obj.setPurchaseID(cur.getInt(cur.getColumnIndex("PurchaseID")));
                Obj.setPurchaseNo(cur.getInt(cur.getColumnIndex("PurchaseNo")));
                Obj.setPaymentDate(cur.getString(cur.getColumnIndex("PurchaseDate")));
                Obj.setBillNO(cur.getString(cur.getColumnIndex("BillNO")));
                Obj.setVendorID(cur.getInt(cur.getColumnIndex("VendorID")));

                Obj.setVendorName(cur.getString(cur.getColumnIndex("VENDOR_NAME")));
                Obj.setAmount(cur.getDouble(cur.getColumnIndex("GrandTotal")));

                Obj.setEntryDate(cur.getString(cur.getColumnIndex("EntryDate")));
                Obj.setRemark(cur.getString(cur.getColumnIndex("Remark")));
                Obj.setTotalTaxAmount(cur.getDouble(cur.getColumnIndex("TotalTaxAmount")));


                lstClsPurchaseMasters.add(Obj);
            }

//            db.close();

        } catch (Exception e) {
            Log.e("ClsCategory", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }
        return lstClsPurchaseMasters;
    }


    @SuppressLint("WrongConstant")
    public static ClsPurchaseMaster getPurchaseDetailListDelete(int PurchaseID) {
        int result = 0;
        int result1 = 0;
        ClsPurchaseMaster objClsPurchaseMaster = new ClsPurchaseMaster();


        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String sqlStr = "DELETE FROM [PurchaseMaster] WHERE [PurchaseID] = "
                    .concat(String.valueOf(PurchaseID))
                    .concat(";");

            String sqlStr1 = "DELETE FROM [PurchaseDetail] WHERE [PurchaseID] = "
                    .concat(String.valueOf(PurchaseID))
                    .concat(";");


            Log.e("--DELETE--", "Query: " + sqlStr);

            SQLiteStatement statement = db.compileStatement(sqlStr);
            SQLiteStatement statement1 = db.compileStatement(sqlStr1);
            result = statement.executeUpdateDelete();
            result1 = statement1.executeUpdateDelete();


            objClsPurchaseMaster.set_PurchaseMasterResult(result);
            objClsPurchaseMaster.set_PurchaseDetailResult(result1);


            db.close();

        } catch (Exception e) {
            Log.e("--DELETE--", "Exception: " + e.getMessage());
            e.getMessage();
        }
        return objClsPurchaseMaster;
    }

    @SuppressLint("WrongConstant")
    public static List<String> getPurchaseMasterColumn(
            io.requery.android.database
                    .sqlite.SQLiteDatabase db) {
        List<String> columns = new ArrayList<>();
        try {
//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

//            io.requery.android.database.sqlite.SQLiteDatabase db;
//
//            db = io.requery.android.database.sqlite.SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath(ClsGlobal.Database_Name).getPath(),
//                    null);

            String qry = "SELECT * FROM [PurchaseMaster] LIMIT 1 ";
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
    public static List<String> getColumns(Context context,
                                          io.requery.android.database
                                                  .sqlite.SQLiteDatabase db) {
        List<String> columns = new ArrayList<>();
        try {

//            io.requery.android.database.sqlite.SQLiteDatabase db;
//
//            db = io.requery.android.database.sqlite.SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath(ClsGlobal.Database_Name).getPath(),
//                    null);

            String qry = "SELECT * FROM [tbl_LayerItem_Master] LIMIT 1 ";

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

}
