package com.demo.nspl.restaurantlite.Global;

import android.content.Context;
import android.util.Log;

import com.demo.nspl.restaurantlite.activity.LogInActivity;
import com.demo.nspl.restaurantlite.classes.ClsExpenseType;
import com.demo.nspl.restaurantlite.classes.ClsTaxSlab;
import com.demo.nspl.restaurantlite.classes.ClsUnit;
import com.demo.nspl.restaurantlite.classes.ClsVendor;

import io.requery.android.database.sqlite.SQLiteDatabase;

public class CreateTables {

    public Context context;

    public synchronized void create_tables(SQLiteDatabase db, Context context) {
        this.context = context;
//            db = openOrCreateDatabase(ClsGlobal.Database_Name, MODE_PRIVATE, null);
//            db.beginTransaction();

        createCategoryMaster(db);
        createItemMaster(db);
        createSizeMaster(db);
        createUnitMaster(db);
        createEmployeeMaster(db);
        createVendorMaster(db);
        createExpenseTypeMaster(db);
        createExpenseMasterNew(db);
        createExpenseMaster(db);
        createTaxMaster(db);
        createEmployeeDocumentMaster(db);
        createInventoryItemMaster(db);
        createInventoryStockMaster(db);
        createTableMaster(db);
        createOrderMaster(db);
        createOrderDetails(db);
        createTerms(db);
        createCustomerMaster(db);
        createEmailLogs(db);

        //----------------new Tables-------------------//
        createInventoryLayer(db);
        createLayerItemMaster(db);
        createItemTag(db);
        createItemLayer(db);
        createTaxSlab(db);
        createInventoryOrderDetail(db);
        createInventoryOrderMaster(db);

        createPaymentMaster(db);
        createPurchaseMaster(db);
        createPurchaseDetail(db);
        createCommonLogsMaster(db);

        //----------------new merge-------------------//
        createMultipleImgSave(db);
        createSmsCustomerGroup(db);
        createSMSCustomerGroupDetail(db);
        importCustomerDataToExcel(db);
        createOrder_Sequence(db);
        createSmsIdSetting(db);

        createMessageFormatMaster(db);
        createSMSBulkMasterMaster(db);
        createSMSLog(db);
        createSalesSMSLogsMaster(db);
        createQuotationMaster(db);
        createQuotationDetail(db);

        createActivityLog(db);

        createPendingPaymentBulkSms(db);

        createFeatures(db);
        createServices(db);
        createServiceImages(db);
        createServiceFeatures(db);
        createEmployeeServiceSetting(db);
        createCombo(db);
        createComboService(db);

//            if (db.isOpen()){
//                db.setTransactionSuccessful();
//                db.endTransaction();
//                db.close();
//            }

//        db.close();


    }


    private void createActivityLog(SQLiteDatabase db) {

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[ActivityLog]")
                .concat("(")
                .concat("[logId] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[action] varchar(50)")
                .concat(",[entryDate] datetime")
                .concat(",[user] varchar(80) ")
                .concat(",[description] varchar(5000)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " CommonLogs_Master:- " + qry);

    }


    private void createSalesSMSLogsMaster(SQLiteDatabase db) {

//        String qry1 = "ALTER TABLE [SalesSMSLogsMaster] ADD [UtilizeType] VARCHAR(10)";
//        db.execSQL(qry1);

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[SalesSMSLogsMaster]")
                .concat("(")
                .concat("[LogId] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[orderId] INTEGER")
                .concat(",[orderNo] VARCHAR(50)")
                .concat(",[mobileNo] VARCHAR(50) ")
                .concat(",[Customer_Name] VARCHAR(500)")
                .concat(",[Entry_Datetime] DATETIME")
                .concat(",[message] VARCHAR(5000)")
                .concat(",[invoice_attachment] VARCHAR(3)")
                .concat(",[Status] VARCHAR(50)")
                .concat(",[UtilizeType] VARCHAR(10)")
                .concat(",[SmsStatus] VARCHAR(50)")
                .concat(",[Type] VARCHAR(50)")
                .concat(",[Credit] INTEGER")
                .concat(",[SendSMSID] VARCHAR(50)")
                .concat(",[Remark] VARCHAR(200)")
                .concat(")")
                .concat(";");


        db.execSQL(qry);
        Log.e("CREATETABLE", " CommonLogs_Master:- " + qry);

    }

    private void createSMSBulkMasterMaster(SQLiteDatabase db) {

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[SMSBulkMaster]")
                .concat("(")
                .concat("[bulkID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[serverBulkID] VARCHAR(30)")
                .concat(",[Message] VARCHAR(5000)")
                .concat(",[MessageLength] INTEGER ")
                .concat(",[GroupName] VARCHAR(500)")
                .concat(",[TotalCustomers] INTEGER")
                .concat(",[SendDate] DATETIME")
                .concat(",[SenderID] varchar(6)")
                .concat(",[MessageType] varchar(30)")
                .concat(",[Title] varchar(200)")
//                .concat(",[Remark] varchar(200)")
                .concat(")")
                .concat(";");


        db.execSQL(qry);
        Log.e("CREATETABLE", " CommonLogs_Master:- " + qry);

    }

    private void createQuotationMaster(SQLiteDatabase db) {

//        db.execSQL("DROP TABLE [QuotationMaster]");


//        String qry =  "CREATE TABLE IF NOT EXISTS"
//                .concat("[QuotationMaster]")
//                .concat("(")
//                .concat("[QuotationID] INTEGER PRIMARY KEY AUTOINCREMENT")
//                .concat(",[MobileNo] VARCHAR(15)")
//                .concat(",[CustomerName] VARCHAR(100)")
//                .concat(",[CompanyName] VARCHAR(100)")
//                .concat(",[cust_address] VARCHAR(100)")
//                .concat(",[cust_email] VARCHAR(100)")
//                .concat(",[GSTNo] VARCHAR(50)")
//                .concat(",[Status] VARCHAR(50)")
//                .concat(",[QuotationDate] DATETIME")
//                .concat(",[ValidUptoDate] DATETIME")
//                .concat(",[QuotationNo] VARCHAR(25)")
//                .concat(",[QuotationType] VARCHAR(15)") // Sale & WholeSale
//                .concat(",[TotalAmount] DOUBLE")   //1050
//                .concat(",[DiscountAmount] DOUBLE") //50
//                .concat(",[TotalTaxAmount] DOUBLE") //50
//                .concat(",[GrandTotal] DOUBLE") //50
//                .concat(",[EntryDate] DATETIME")
//                .concat(",[ApplyTax] VARCHAR(3)")
//                .concat(",[Sms_Limit] INTEGER")
//                .concat(")")
//                .concat(";");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[QuotationMaster]")
                .concat("(")
                .concat("[QuotationID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[MobileNo] VARCHAR(15)")
                .concat(",[CustomerName] VARCHAR(100)")
                .concat(",[CompanyName] VARCHAR(100)")
                .concat(",[cust_address] VARCHAR(100)")
                .concat(",[cust_email] VARCHAR(100)")
                .concat(",[GSTNo] VARCHAR(50)")
                .concat(",[Status] VARCHAR(50)")
                .concat(",[QuotationDate] DATETIME")
                .concat(",[ValidUptoDate] DATETIME")
                .concat(",[QuotationNo] VARCHAR(25)")
                .concat(",[QuotationType] VARCHAR(15)") // Sale & WholeSale
                .concat(",[TotalAmount] DOUBLE")   //1050
                .concat(",[DiscountAmount] DOUBLE") //50
                .concat(",[TotalTaxAmount] DOUBLE") //50
                .concat(",[GrandTotal] DOUBLE") //50
                .concat(",[EntryDate] DATETIME")
                .concat(",[ApplyTax] VARCHAR(3)")
                .concat(",[Sms_Limit] INTEGER")
                .concat(")")
                .concat(";");


        db.execSQL(qry);
        Log.e("CREATETABLE", " tbl_Tax_Slab " + qry);
    }

    private void createQuotationDetail(SQLiteDatabase db) {
//        db.execSQL("DROP TABLE [QuotationDetail]");

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[QuotationDetail]")
                .concat("(")
                .concat("[QuotationDetailID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[QuotationID] INTEGER")
                .concat(",[QuotationNo] VARCHAR(25)")
                .concat(",[SaveStatus] VARCHAR(5)")
                .concat(",[ItemCode] VARCHAR(50)")
                .concat(",[Item] VARCHAR(100)")
                .concat(",[Unit] VARCHAR(50)")
                .concat(",[ItemComment] VARCHAR(200)")
                .concat(",[Rate] DOUBLE")
                .concat(",[SaleRate] DOUBLE")
                .concat(",[SaleRateWithoutTax] DOUBLE")
                .concat(",[Quantity] DOUBLE")
                .concat(",[Discount_per] DOUBLE")
                .concat(",[Discount_amt] DOUBLE")
                .concat(",[Amount] DOUBLE")
                .concat(",[CGST] DOUBLE")
                .concat(",[SGST] DOUBLE")
                .concat(",[IGST] DOUBLE")
                .concat(",[TotalTaxAmount] DOUBLE")
                .concat(",[GrandTotal] DOUBLE")
                .concat(")")
                .concat(";");


        db.execSQL(qry);
        Log.e("CREATETABLE", " tbl_Tax_Slab " + qry);

    }


    private void createSMSLog(SQLiteDatabase db) {

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[SMSLog]")
                .concat("(")
                .concat("[logID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[bulkID] INTEGER")
                .concat(",[Mobile] varchar(10)")
                .concat(",[CustomerName] VARCHAR(150)")
                .concat(",[CreditCount] INTEGER ")
                .concat(",[Status] VARCHAR(100)")
                .concat(",[Message] VARCHAR(5000)")
                .concat(",[StatusDateTime] DATETIME")
                .concat(",[StatusCode] INTEGER")
                .concat(",[serverBulkID] VARCHAR(100)")
                .concat(",[Remark] VARCHAR(200)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " CommonLogs_Master:- " + qry);

    }


    private void createMessageFormatMaster(SQLiteDatabase db) {

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[MessageFormat_Master]")
                .concat("(")
                .concat("[MessageFormat_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[Title] VARCHAR(2000)")
                .concat(",[MessageFormat] VARCHAR(10000)")
                .concat(",[Type] VARCHAR(20)") // Sale,purches,offer.
                .concat(",[Default] VARCHAR(3)") // YES,NO.
                .concat(",[Remark] VARCHAR(200)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " CommonLogs_Master:- " + qry);

    }

    private void createCommonLogsMaster(SQLiteDatabase db) {

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[CommonLogs_Master]")
                .concat("(")
                .concat("[CommonLogs_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[Remark] VARCHAR(500)")
                .concat(",[Status] VARCHAR(50)")
                .concat(",[Log_Type] VARCHAR(80)")
                .concat(",[Date_Time] datetime")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " CommonLogs_Master:- " + qry);

    }

    private void createPurchaseDetail(SQLiteDatabase db) {

        //  db.execSQL("DROP TABLE [tbl_ItemTag]");

      /*  String qry = "ALTER TABLE [PurchaseDetail] ADD [UtilizeType] VARCHAR(10)";
        db.execSQL(qry);
*/
        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[PurchaseDetail]")
                .concat("(")
                .concat("[PurchaseDetailID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[PurchaseID] INT")
                .concat(",[ItemID] INT")
                .concat(",[MonthYear] VARCHAR(10)")
                .concat(",[ItemCode] VARCHAR(20)")
                .concat(",[Unit] VARCHAR(50)")
                .concat(",[Quantity] DOUBLE")
                .concat(",[Rate] DOUBLE")
                .concat(",[TotalAmount] DOUBLE")//50*10
                .concat(",[Discount] DOUBLE")
                .concat(",[NetAmount] DOUBLE")
                .concat(",[ApplyTax] varchar(3)")
                .concat(",[CGST] DOUBLE")
                .concat(",[SGST] DOUBLE")
                .concat(",[IGST] DOUBLE")
                .concat(",[TotalTaxAmount] DOUBLE") //Total tax amt
                .concat(",[GrandTotal] DOUBLE") //finalAmt
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " Terms " + qry);
    }

    private void createPurchaseMaster(SQLiteDatabase db) {

        //  db.execSQL("DROP TABLE [tbl_ItemTag]");
        /*String qry = "ALTER TABLE [PurchaseMaster] ADD [VendorID] INT";

        db.execSQL(qry);*/


//        db.execSQL("ALTER TABLE [PurchaseMaster]  RENAME [PurchaseNo] VARCHAR(100)");
//        db.execSQL("DROP TABLE [PurchaseMaster]  [PurchaseNo] VARCHAR(100)");

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[PurchaseMaster]")
                .concat("(")
                .concat("[PurchaseID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[PurchaseNo] INT")
                .concat(",[VendorID] INT")
                .concat(",[BillNO] VARCHAR(30)")
                .concat(",[PurchaseDate] DATE")
                .concat(",[Remark] VARCHAR(300)")
                .concat(",[EntryDate] DATETIME")
                .concat(",[Sms_Limit] INTEGER")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " Terms " + qry);
    }


    private void createPaymentMaster(SQLiteDatabase db) {

//        db.execSQL("ALTER TABLE [PaymentMaster] ADD [OrderID] INT");

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[PaymentMaster]")
                .concat("(")
                .concat("[PaymentID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[PaymentDate] DATETIME")
                .concat(",[PaymentMounth] VARCHAR(20)")
                .concat(",[VendorID] INT")
                .concat(",[MobileNo] VARCHAR(20)")
                .concat(",[CustomerName] VARCHAR(100)")
                .concat(",[VendorName] VARCHAR(100)")
                .concat(",[PaymentMode] VARCHAR(20)")
                .concat(",[PaymentDetail] VARCHAR(150)")
                .concat(",[InvoiceNo] VARCHAR(80)")
                .concat(",[Amount] DOUBLE")
                .concat(",[Remark] VARCHAR(500)")
                .concat(",[EntryDate] DATETIME")
                .concat(",[Type] VARCHAR(10)")
                .concat(",[ReceiptNo] VARCHAR(25)")
                .concat(",[OrderID] INT")
                .concat(",[Sms_Limit] INTEGER")
                .concat(",[Payment_DateTime] DATETIME")

                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " tbl_Tax_Slab " + qry);
    }

    private void createInventoryOrderDetail(SQLiteDatabase db) {

//         db.execSQL("ALTER TABLE [InventoryOrderDetail] ADD [Discount_per] DOUBLE");
//         db.execSQL("ALTER TABLE [InventoryOrderDetail] ADD [Unit] VARCHAR(50)");
//
        //  ItemComment

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[InventoryOrderDetail]")
                .concat("(")
                .concat("[OrderDetailID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[OrderID] VARCHAR(25)")
                .concat(",[OrderNo] VARCHAR(25)")
                .concat(",[ItemCode] VARCHAR(50)")
                .concat(",[Item] VARCHAR(100)")
                .concat(",[ItemComment] VARCHAR(200)")
                .concat(",[Rate] DOUBLE")
                .concat(",[SaleRate] DOUBLE")//100
//                .concat(",[Unit] VARCHAR(50)")
                .concat(",[SaleRateWithoutTax] DOUBLE")// 84.55
                .concat(",[Quantity] DOUBLE")
                .concat(",[Discount_per] DOUBLE")
                .concat(",[Discount_amt] DOUBLE")
                .concat(",[Amount] DOUBLE")
                .concat(",[CGST] DOUBLE")
                .concat(",[SGST] DOUBLE")
                .concat(",[IGST] DOUBLE")
                .concat(",[TotalTaxAmount] DOUBLE")//15.25
                .concat(",[GrandTotal] DOUBLE")
                .concat(",[SaveStatus] VARCHAR(3)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " tbl_Tax_Slab " + qry);
    }

    private void createInventoryOrderMaster(SQLiteDatabase db) {

        // Different_Amount_mode
        // db.execSQL("ALTER TABLE [InventoryOrderMaster] ADD [Different_Amount_mode] VARCHAR(50)");
//        db.execSQL("ALTER TABLE [InventoryOrderMaster] ADD [QuotationId] INTEGER");


        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[InventoryOrderMaster]")
                .concat("(")
                .concat("[OrderID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[MobileNo] VARCHAR(15)")
                .concat(",[CustomerName] VARCHAR(100)")
                .concat(",[CompanyName] VARCHAR(100)")
                .concat(",[GSTNo] VARCHAR(50)")
                .concat(",[BillDate] DATETIME")
                .concat(",[OrderNo] VARCHAR(25)")
                .concat(",[SaleType] VARCHAR(15)") // Sale & WholeSale

                .concat(",[SaleReturnDiscount] DOUBLE")   //1050
                .concat(",[TotalAmount] DOUBLE")   //1050
                .concat(",[DiscountAmount] DOUBLE") //50
                .concat(",[TotalPaybleAmount] DOUBLE") //1000
                .concat(",[TotalTaxAmount] DOUBLE")   // 100
                .concat(",[TotalReceiveableAmount] DOUBLE")   // 1100
                .concat(",[PaidAmount] DOUBLE") //1050
                .concat(",[AdjumentAmount] DOUBLE") //50

                .concat(",[EntryDate] DATETIME")
                .concat(",[ApplyTax] VARCHAR(3)")
                .concat(",[PaymentMode] VARCHAR(20)")
                .concat(",[PaymentDetail] VARCHAR(50)")
                .concat(",[Different_Amount_mode] VARCHAR(50)")
                .concat(",[BillTo] VARCHAR(8)")
                .concat(",[Sms_Limit] INTEGER")
                .concat(",[QuotationId] INTEGER")
                .concat(",[DueDate] DATETIME")

                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " tbl_Tax_Slab " + qry);

    }

    private void createTaxSlab(SQLiteDatabase db) {
        // db.execSQL("DROP TABLE [tbl_Tax_Slab]");


        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[tbl_Tax_Slab]")
                .concat("(")
                .concat("[SLAB_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[SLAB_NAME] VARCHAR(100)")
                .concat(",[SGST] DOUBLE")
                .concat(",[CGST] DOUBLE")
                .concat(",[IGST] DOUBLE")
                .concat(",[ACTIVE] VARCHAR (3)")
                .concat(",[REMARK] VARCHAR(100)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);

        boolean exists;
        ClsTaxSlab objClsUnitMaster = new ClsTaxSlab(context);

        String where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("18.0% IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("18.0% IGST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(18.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("18.0% IGST");
//            ClsTaxSlab.Insert(context, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("0% IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("0% IGST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("0% IGST");
//            ClsTaxSlab.Insert(context, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("Out of GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("Out of GST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("Out of GST");
//            ClsTaxSlab.Insert(context, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("0% GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("0% GST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("0% GST");
//            ClsTaxSlab.Insert(context, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("6.0% IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("6.0% IGST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(6.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("6.0% IGST");
//            ClsTaxSlab.Insert(context, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("28.0% IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("28.0% IGST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(28.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("28.0% IGST");
//            ClsTaxSlab.Insert(context, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("28.0% GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("28.0% GST");
            objClsUnitMaster.setSGST(14.0);
            objClsUnitMaster.setCGST(14.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("28.0% GST");
//            ClsTaxSlab.Insert(context, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("12.0% GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("12.0% GST");
            objClsUnitMaster.setSGST(6.0);
            objClsUnitMaster.setCGST(6.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("12.0% GST");
//            ClsTaxSlab.Insert(context, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("18.0% GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("18.0% GST");
            objClsUnitMaster.setSGST(9.0);
            objClsUnitMaster.setCGST(9.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("18.0% GST");
//            ClsTaxSlab.Insert(context, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("3.0% GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("3.0% GST");
            objClsUnitMaster.setSGST(1.5);
            objClsUnitMaster.setCGST(1.5);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("3.0% GST");
//            ClsTaxSlab.Insert(context, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("0.25% IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("3.0% GST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(0.25);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("0.25% IGST");
//            ClsTaxSlab.Insert(context, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("5.0% GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("5.0% GST");
            objClsUnitMaster.setSGST(2.5);
            objClsUnitMaster.setCGST(2.5);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("5.0% GST");
//            ClsTaxSlab.Insert(context, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("6.0% GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("6.0% GST");
            objClsUnitMaster.setSGST(3.0);
            objClsUnitMaster.setCGST(3.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("6.0% GST");
//            ClsTaxSlab.Insert(context, objClsUnitMaster, db);
        }


        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("0.25% GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("0.25% GST");
            objClsUnitMaster.setSGST(0.125);
            objClsUnitMaster.setCGST(0.125);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("0.25% GST");
//            ClsTaxSlab.Insert(context, objClsUnitMaster, db);
        }


        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("Exempt IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("Exempt IGST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("Exempt IGST");
//            ClsTaxSlab.Insert(context, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("3.0% IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("3.0% IGST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(3.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("3.0% IGST");
//            ClsTaxSlab.Insert(context, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("5.0% IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("5.0% IGST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(5.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("5.0% IGST");
//            ClsTaxSlab.Insert(context, objClsUnitMaster, db);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("Exempt GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("Exempt GST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("Exempt GST");
//            ClsTaxSlab.Insert(context, objClsUnitMaster, db);
        }
        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("12.0% IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("12.0% IGST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(12.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("12.0% IGST");
//            ClsTaxSlab.Insert(context, objClsUnitMaster, db);
        }


        Log.e("CREATETABLE", " tbl_Tax_Slab " + qry);

    }

    private void createItemTag(SQLiteDatabase db) {

        //  db.execSQL("DROP TABLE [tbl_ItemTag]");

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[tbl_ItemTag]")
                .concat("(")
                .concat("[ITEMTAG_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[ITEMID] INT")
                .concat(",[ITEMNAME] VARCHAR(100)")
                .concat(",[TAGNAME] VARCHAR(100)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " Terms " + qry);
    }

    private void createItemLayer(SQLiteDatabase db) {

        // db.execSQL("DROP TABLE [tbl_ItemLayer]");

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[tbl_ItemLayer]")
                .concat("(")
                .concat("[ITEMLAYER_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[LAYERITEM_ID] INT")//layerID
                .concat(",[LAYER_NAME] VARCHAR(40)")
                .concat(",[ITEM_ID] INT")
                .concat(",[ITEM_NAME] VARCHAR(40)")
                .concat(",[LAYER_VALUE] VARCHAR(100)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " Terms " + qry);
    }


    private void createLayerItemMaster(SQLiteDatabase db) {

        // db.execSQL("DROP TABLE [tbl_LayerItem_Master]");

      /*  String qryAdd_OPENING_STOCK = "ALTER TABLE [tbl_LayerItem_Master] ADD [WHOLESALE_RATE] DOUBLE";
        db.execSQL(qryAdd_OPENING_STOCK);

        String qryAddTaxType = "ALTER TABLE [tbl_LayerItem_Master] ADD [TAX_TYPE] VARCHAR(10)";
        db.execSQL(qryAddTaxType);*/


        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[tbl_LayerItem_Master]")
                .concat("(")
                .concat("[LAYERITEM_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[ITEM_NAME] VARCHAR(100)")
                .concat(",[ITEM_CODE] VARCHAR(15)")
                .concat(",[RATE_PER_UNIT] DOUBLE")
                .concat(",[WHOLESALE_RATE] DOUBLE")
                .concat(",[TAX_TYPE] VARCHAR(10)")
                .concat(",[REMARK] VARCHAR(500)")
                .concat(",[MIN_STOCK] DOUBLE")
                .concat(",[MAX_STOCK] DOUBLE")
                .concat(",[UNIT_CODE] VARCHAR(20)")
                .concat(",[TAGS]  VARCHAR(500)")
                .concat(",[ACTIVE] VARCHAR (3)")
                .concat(",[DISPLAY_ORDER] INT")
                .concat(",[OPENING_STOCK] DOUBLE")
                .concat(",[HSN_SAC_CODE] VARCHAR(20)")
                .concat(",[TAX_APPLY] VARCHAR (3)")
                .concat(",[TAX_SLAB_ID] INT")
                .concat(",[AUTO_GENERATED_ITEM_CODE] VARCHAR (2500)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);

        Log.e("CREATETABLE", " Terms " + qry);
    }

    private void createInventoryLayer(SQLiteDatabase db) {
        // db.execSQL("ALTER TABLE [tbl_InventoryLayer] ADD [SELECTED_LAYER_NAME] VARCHAR(40)");
        // db.execSQL("DROP TABLE [tbl_LayerItem_Master]");
        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[tbl_InventoryLayer]")
                .concat("(")
                .concat("[INVENTORYLAYER_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[LAYER_NAME] VARCHAR(40)")
                .concat(",[PARENT_ID] INT")
                .concat(",[SELECTED_LAYER_NAME] VARCHAR(40)")
                .concat(",[DISPLAY_ORDER] INT")
                .concat(",[REMARK] VARCHAR(40)")
                .concat(",[ACTIVE] VARCHAR (3)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " Terms " + qry);
    }

    private void createOrder_Sequence(SQLiteDatabase db) {

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[OrderSequence]")
                .concat("(")
                .concat("[Order_Sequence_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[OrderNo] INT")
                .concat(",[TaxApplied] VARCHAR(3)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " CommonLogs_Master:- " + qry);

    }

    private void importCustomerDataToExcel(SQLiteDatabase db) {


        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[tbl_customer_import]")
                .concat("(")
                .concat("[excel_id] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[excel_name] VARCHAR(500)")
                .concat(",[excel_number] VARCHAR(15)")
                .concat(",[excel_email] VARCHAR(100)")
                .concat(",[company_name] VARCHAR(100)")
                .concat(",[gst_no] VARCHAR(20)")
                .concat(",[address] VARCHAR(500)")
                .concat(",[note] VARCHAR(200)")
                .concat(",[status] VARCHAR(50)")
                .concat(",[remark] VARCHAR(200)")
                .concat(")")
                .concat(";");

        Log.d("--uri--", "qry:: " + qry);

        db.execSQL(qry);

    }

    private void createTerms(SQLiteDatabase db) {

//        db.execSQL("ALTER TABLE [tbl_Terms] ADD [TERM_TYPE] VARCHAR(50)");

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[tbl_Terms]")
                .concat("(")
                .concat("[TERMS_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[INVOICE_TYPES] VARCHAR(20)")
                .concat(",[TERMS] VARCHAR(200)")
                .concat(",[TERM_TYPE] VARCHAR(50)")
                .concat(",[SORT_NO] INT")
                .concat(",[ACTIVE] VARCHAR (3)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " Terms " + qry);
    }


    private void createOrderDetails(SQLiteDatabase db) {
        //   db.execSQL("DROP TABLE [OrderDetail_master]");
        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[OrderDetail_master]")
                .concat("(")
                .concat("[ORDERDETAIL_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[ORDER_ID] INT")
                .concat(",[ORDER_NO] VARCHAR(6)")
                .concat(",[ITEM_ID] INT")
                .concat(",[ITEM_NAME] VARCHAR (100)")
                .concat(",[RATE] DOUBLE")
                .concat(",[QUANTITY] DOUBLE")
                .concat(",[TOTAL_AMOUNT] DOUBLE")   //rate*qty..
                .concat(",[OTHER_TAX1] VARCHAR(50)")
                .concat(",[OTHER_VAL1] DOUBLE")
                .concat(",[OTHER_TAX2] VARCHAR(50)")
                .concat(",[OTHER_VAL2] DOUBLE")
                .concat(",[OTHER_TAX3] VARCHAR(50)")
                .concat(",[OTHER_VAL3] DOUBLE")
                .concat(",[OTHER_TAX4] VARCHAR(50)")
                .concat(",[OTHER_VAL4] DOUBLE")
                .concat(",[OTHER_TAX5] VARCHAR(50)")
                .concat(",[OTHER_VAL5] DOUBLE")
                .concat(",[TOTAL_TAXAMOUNT] DOUBLE")
                .concat(",[GRAND_TOTAL] DOUBLE")
                .concat(",[TYPE] VARCHAR (10)")        //serve,parcel
                .concat(",[STATUS] VARCHAR (20)")      //cooking,ready to serve etc.
                .concat(",[ENTRYDATETIME] DATETIME")
                .concat(")")
                .concat(";");


        db.execSQL(qry);
        Log.e("CREATETABLE", " orderdetails " + qry);
    }


    private void createEmailLogs(SQLiteDatabase db) {

        // db.execSQL("DROP TABLE [tbl_EmailLogs]");


        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[tbl_EmailLogs]")
                .concat("(")
                .concat("[EMAILLOGS_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[MESSAGE] VARCHAR(500)")//success:successfully sent, fail:internet connectivity not awailbale,
                .concat(",[DATE_TIME] datetime")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " EmailLogs " + qry);
    }


    private void createCustomerMaster(SQLiteDatabase db) {


//        db.execSQL("DROP TABLE [CustomerMaster]");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[CustomerMaster]")
                .concat("(")
                .concat("[ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[NAME] VARCHAR (20)")
                .concat(",[Company_Name] VARCHAR (100)")
                .concat(",[GST_NO] VARCHAR (20)")
                .concat(",[Address] VARCHAR (100)")
                .concat(",[MOBILE_NO] VARCHAR (15)")/// Expense Type
                .concat(",[Credit] DOUBLE")/// Expense Type
                .concat(",[OpeningStock] DOUBLE")/// Expense Type
                .concat(",[Email] VARCHAR (100)")/// Expense Type
                .concat(",[BalanceType] VARCHAR (15)")/// Expense Type
                .concat(",[Note] VARCHAR (500)")/// Expense Type
                .concat(",[Save_Contact] VARCHAR (3)")/// Expense Type
                .concat(",[DOB] DateTime")/// Expense Type
                .concat(",[AnniversaryDate] DateTime")/// Expense Type
                .concat(",[PanCard]  VARCHAR (20)")/// Expense Type
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", qry);
    }


    private void createOrderMaster(SQLiteDatabase db) {

//        db.execSQL("DROP TABLE [Ordermaster]");
        //       db.execSQL("ALTER TABLE [Ordermaster] ADD [GRAND_TOTAL] DOUBLE");

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[Ordermaster]")
                .concat("(")
                .concat("[ORDER_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[ORDER_NO] VARCHAR (6)")//y
                .concat(",[ORDER_DATETIME] DATETIME")//y
                .concat(",[TABLE_ID] INT")//n
                .concat(",[TABLE_NO] VARCHAR (10)")//y
                .concat(",[MOBILE_NO] VARCHAR (15)")//y     //Retail or Table..
                .concat(",[SOURCE] VARCHAR (20)")//y     //Retail or Table..
                .concat(",[ENTRYDATETIME] DATETIME")//n  //dbformate..
                .concat(",[TOTAL_TAXAMOUNT] DOUBLE")//y
                .concat(",[TOTAL_AMOUNT] DOUBLE")//y
                .concat(",[DISCOUNT] DOUBLE")//y
                .concat(",[GRAND_TOTAL] DOUBLE")//y
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", "OrderMaster" + qry);
    }

    private void createTableMaster(SQLiteDatabase db) {
//        String Alterqry = "ALTER TABLE [Table_master] ADD [ORDER_NO] VARCHAR(6)";
//        db.execSQL(Alterqry);

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[Table_master]")
                .concat("(")
                .concat("[TABLE_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[TABLE_NO] VARCHAR(100)")
                .concat(",[ACTIVE] VARCHAR (3)")
                .concat(",[SITTING_CAPACITY] INT")
                .concat(",[STATUS] VARCHAR (20)")
                .concat(",[SORT_NO] INT")
                .concat(",[REMARK] VARCHAR(200)")
                .concat(",[ORDER_NO] VARCHAR(6)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", "TableMaster--" + qry);

    }

    private void createTaxMaster(SQLiteDatabase db) {
//                db.execSQL("DROP TABLE [Taxes]");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[Taxes]")
                .concat("(")
                .concat("[TAX_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[TAX_TYPE] VARCHAR(10)")
                .concat(",[TAX_NAME] VARCHAR(100)")
                .concat(",[TAX_VALUE] DOUBLE")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "TaxMaster---->>>" + qry);
        db.execSQL(qry);
    }


    private void createMultipleImgSave(SQLiteDatabase db) {
//        db.execSQL("DROP TABLE [MultipleImgSave]");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[MultipleImgSave]")
                .concat("(")
                .concat("[ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[Purchase_id] INTEGER")
                .concat(",[Item_code] VARCHAR(50)")
                .concat(",[Document_Name] VARCHAR(20)")
                .concat(",[Document_No] VARCHAR(50)") // Invoice No
                .concat(",[File_Path] VARCHAR(100)")
                .concat(",[Type] VARCHAR(10)") // Puchase / Sale / Item
                .concat(",[File_Name] VARCHAR(100)")
                .concat(",[Entry_date] DATETIME")
                .concat(")")
                .concat(";");
        db.execSQL(qry);

    }


    private void createSmsIdSetting(SQLiteDatabase db) {


        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[SmsIdSetting]")
                .concat("(")
                .concat("[id] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[sms_id] VARCHAR(6)")
                .concat(",[default_sms] VARCHAR(3)")
                .concat(",[active] VARCHAR(3)")
                .concat(",[entry_date] DATETIME")
                .concat(",[remark] VARCHAR(200)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);

    }

    private void createSmsCustomerGroup(SQLiteDatabase db) {
//        db.execSQL("DROP TABLE IF EXISTS [SmsCustomerGroup];");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[SmsCustomerGroup]")
                .concat("(")
                .concat("[GroupId] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[GroupName] VARCHAR(100),")
                .concat("[Active] VARCHAR(5),")
                .concat("[EntryDate] DATETIME,")
                .concat("[Remark] VARCHAR(500)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);

    }

    private void createSMSCustomerGroupDetail(SQLiteDatabase db) {

//        db.execSQL("DROP TABLE IF EXISTS [SMSCustomerGroupDetail];");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[SMSCustomerGroupDetail]")
                .concat("(")
                .concat("[DetailId] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[GroupId] INTEGER ,")
                .concat("[CustomerName] VARCHAR(100),")
                .concat("[MobileNo] VARCHAR(15)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);

    }

    private void createEmployeeDocumentMaster(SQLiteDatabase db) {
//   db.execSQL("DROP TABLE [EmployeeDocument]");


//        db.execSQL("ALTER TABLE [EmployeeDocument] ADD [TYPE] VARCHAR(10)");


        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[EmployeeDocument]")
                .concat("(")
                .concat("[DOCUMENT_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[EMP_ID] INTEGER")
                .concat(",[DOCUMENT_NAME] VARCHAR(50)")
                .concat(",[OTHER_PROF] VARCHAR(50)")
                .concat(",[DOCUMENT_NO] VARCHAR(50)")
                .concat(",[FILE_PATH] VARCHAR(100)")
                .concat(",[EXP_DATE] VARCHAR(10)")
                .concat(",[TYPE] VARCHAR(10)")
                .concat(",[FILE_NAME] VARCHAR(100)")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "EmployeeDocument---->>>" + qry);
        db.execSQL(qry);
    }


    private void createExpenseMaster(SQLiteDatabase db) {
//           db.execSQL("DROP TABLE [ExpenseMaster]");
//        String qry = "CREATE TABLE IF NOT EXISTS "
//                .concat("[ExpenseMaster]")
//                .concat("(")
//                .concat("[Expense_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
//                .concat(",[VENDOR_ID] INTEGER")
//                .concat(",[EXPENSE_TYPE_ID] INTEGER")/// Expense Type
//                .concat(",[VENDOR_NAME] VARCHAR(50)")//
//                .concat(",[EMPLOYEE_NAME] VARCHAR(50)")//
//                .concat(",[RECEIPT_NO] VARCHAR(20)")//
//                .concat(",[RECEIPT_DATE] DATE")//
//                .concat(",[AMOUNT] DOUBLE")//
//                .concat(",[OTHER_TAX1] VARCHAR(50)")// GST
//                .concat(",[OTHER_VAL1] DOUBLE")//
//                .concat(",[OTHER_TAX2] VARCHAR(50)")//
//                .concat(",[OTHER_VAL2] DOUBLE")//
//                .concat(",[OTHER_TAX3] VARCHAR(50)")//
//                .concat(",[OTHER_VAL3] DOUBLE")//
//                .concat(",[OTHER_TAX4] VARCHAR(50)")//
//                .concat(",[OTHER_VAL4] DOUBLE")//
//                .concat(",[OTHER_TAX5] VARCHAR(50)")//
//                .concat(",[OTHER_VAL5] DOUBLE")//
//                .concat(",[DISCOUNT] DOUBLE")//
//                .concat(",[GRAND_TOTAL] DOUBLE")//
//                .concat(",[ENTRY_DATE] DATETIME")//
//                .concat(",[REMARK] VARCHAR(200)")//
//                .concat(")")
//                .concat(";");
//        db.execSQL(qry);
//
//        Log.e("CREATETABLE","EXPENSE---->>>"+ qry);
//        db.execSQL(qry);


//           db.execSQL("DROP TABLE [ExpenseMaster]");
        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[ExpenseMaster]")
                .concat("(")
                .concat("[EXPENSE_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[VENDOR_ID] INTEGER,")
                .concat("[EXPENSE_TYPE_ID] INTEGER,")
                .concat("[VENDOR_NAME] VARCHAR(100),")
                .concat("[EMPLOYEE_NAME] VARCHAR(100),")
                .concat("[AMOUNT] DOUBLE,")
                .concat("[OTHER_TAX1] VARCHAR(50),")
                .concat("[OTHER_VAL1] DOUBLE,")
                .concat("[OTHER_TAX2] VARCHAR(50),")
                .concat("[OTHER_VAL2] DOUBLE,")
                .concat("[OTHER_TAX3] VARCHAR(50),")
                .concat("[OTHER_VAL3] DOUBLE,")
                .concat("[OTHER_TAX4] VARCHAR(50),")
                .concat("[OTHER_VAL4] DOUBLE,")
                .concat("[OTHER_TAX5] VARCHAR(50),")
                .concat("[OTHER_VAL5] DOUBLE,")
                .concat("[DISCOUNT] DOUBLE,")
                .concat("[GRAND_TOTAL] DOUBLE,")
                .concat("[BILL_RECEIPT_NO] VARCHAR(20),")
                .concat("[TRANSACTION_DATE] DATE,")
                .concat("[ENTRY_DATE] DATETIME,")
                .concat("[REMARK] VARCHAR(200)")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "EXPENSE---->>>" + qry);
        db.execSQL(qry);

    }


    private void createExpenseMasterNew(SQLiteDatabase db) {


//        db.execSQL("DROP TABLE [ExpenseMasterNew]");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[ExpenseMasterNew]")
                .concat("(")
                .concat("[Expense_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[VENDOR_ID] INTEGER")
                .concat(",[EXPENSE_TYPE_ID] INTEGER")/// Expense Type
                .concat(",[VENDOR_NAME] VARCHAR(50)")//
                .concat(",[EMPLOYEE_NAME] VARCHAR(50)")//
                .concat(",[RECEIPT_NO] VARCHAR(20)")//
                .concat(",[RECEIPT_DATE] DATE")//
                .concat(",[AMOUNT] DOUBLE")//
                .concat(",[OTHER_TAX1] VARCHAR(50)")// GST
                .concat(",[OTHER_VAL1] DOUBLE")//
                .concat(",[OTHER_TAX2] VARCHAR(50)")//
                .concat(",[OTHER_VAL2] DOUBLE")//
                .concat(",[OTHER_TAX3] VARCHAR(50)")//
                .concat(",[OTHER_VAL3] DOUBLE")//
                .concat(",[OTHER_TAX4] VARCHAR(50)")//
                .concat(",[OTHER_VAL4] DOUBLE")//
                .concat(",[OTHER_TAX5] VARCHAR(50)")//
                .concat(",[OTHER_VAL5] DOUBLE")//
                .concat(",[DISCOUNT] DOUBLE")//
                .concat(",[GRAND_TOTAL] DOUBLE")//
                .concat(",[ENTRY_DATE] DATETIME")//
                .concat(",[REMARK] VARCHAR(200)")//
                .concat(")")
                .concat(";");
        db.execSQL(qry);
        Log.e("CREATETABLE", qry);

    }


    private void createExpenseTypeMaster(SQLiteDatabase db) {

//                db.execSQL("DROP TABLE [EXPENSE_TYPE_MASTER]");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[EXPENSE_TYPE_MASTER]")
                .concat("(")
                .concat("[EXPENSE_TYPE_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[EXPENSE_TYPE_NAME] VARCHAR(100),")
                .concat("[ACTIVE] VARCHAR(3),")
                .concat("[REMARK] VARCHAR(200),")
                .concat("[SORT_NO] INTEGER")
                .concat(")")
                .concat(";");


        Log.e("CREATETABLE", "EXPENSE_TYPE_MASTER---->>>" + qry);
        db.execSQL(qry);


        ClsExpenseType Obj = new ClsExpenseType(context);
        String where = " AND [EXPENSE_TYPE_NAME] = "
                .concat("'")
                .concat("SALARY")
                .concat("' ");

        boolean exists = Obj.checkExists(where);
        if (!exists) {
            Obj.setExpense_type_name("SALARY");
            Obj.setActive("YES");
            ClsExpenseType.Insert(Obj);
        }

    }


    private void createInventoryItemMaster(SQLiteDatabase db) {
//        db.execSQL("DROP TABLE [INVENTORY_ITEM_MASTER]");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[INVENTORY_ITEM_MASTER]")
                .concat("(")
                .concat("[INVENTORY_ITEM_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[INVENTORY_ITEM_NAME] VARCHAR(100),")
                .concat("[UNIT_ID] INTEGER,")
                .concat("[UNIT_NAME] VARCHAR(100),")
                .concat("[MAX_STOCK_QTY] DOUBLE,")
                .concat("[MIN_STOCK_QTY] DOUBLE,")
                .concat("[ACTIVE] VARCHAR(3),")
                .concat("[REMARK] VARCHAR(200)")
                .concat(")")
                .concat(";");


        Log.e("CREATETABLE", "INVENTORY_ITEM_MASTER---->>>" + qry);
        db.execSQL(qry);

    }

    private void createInventoryStockMaster(SQLiteDatabase db) {
//        db.execSQL("DROP TABLE [Inventory_stock_master]");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[Inventory_stock_master]")
                .concat("(")
                .concat("[STOCK_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[ORDER_ID] INTEGER,")
                .concat("[VENDOR_ID] INTEGER,")
                .concat("[INVENTORY_ITEM_ID] INTEGER,")
                .concat("[INVENTORY_ITEM_NAME] VARCHAR(100),")
                .concat("[QUANTITY] DOUBLE,")
                .concat("[AMOUNT] DOUBLE,")
                .concat("[TYPE] VARCHAR(3),")
                .concat("[ENTRY_DATE] DATETIME,")
                .concat("[TRANSACTION_DATE] DATE,")
                .concat("[REMARK] VARCHAR(200)")
                .concat(")")
                .concat(";");


        Log.e("CREATETABLE", "INVENTORY_STOCK_MASTER---->>>" + qry);
        db.execSQL(qry);
    }

    private void createEmployeeMaster(SQLiteDatabase db) {

//        db.execSQL("DROP TABLE [EMPLOYEE_MASTER]");
        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[EMPLOYEE_MASTER]")
                .concat("(")
                .concat("[EMPLOYEE_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[EMPLOYEE_NAME] VARCHAR(100),")
                .concat("[CONTACT_NO] VARCHAR(12),")
                .concat("[ADDRESS] VARCHAR(300),")
                .concat("[EMPLOYEE_IMAGE] VARCHAR(100),")
                .concat("[SALARY] DOUBLE(20),")
                .concat("[DOB] DATE,")
                .concat("[ACTIVE] VARCHAR(3),")
                .concat("[REMARK] VARCHAR(200),")
                .concat("[SORT_NO] INTEGER")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "EMP_MASTER---->>>" + qry);
        db.execSQL(qry);
    }

    private void createItemMaster(SQLiteDatabase db) {

//            db.execSQL("DROP TABLE [ITEM_MASTER]");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[ITEM_MASTER]")
                .concat("(")
                .concat("[ITEM_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[ITEM_NAME] VARCHAR(100),")
                .concat("[CATEGORY_ID] INTEGER,")
                .concat("[CATEGORY_NAME] VARCHAR(100),")
                .concat("[FOOD_TYPE] VARCHAR(10),")
                .concat("[ACTIVE] VARCHAR(3),")
                .concat("[REMARK] VARCHAR(200),")
                .concat("[SORT_NO] INTEGER,")
                .concat("[PRICE] DOUBLE")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "ITEM_MASTER---->>>" + qry);
        db.execSQL(qry);
    }


    private void createVendorMaster(SQLiteDatabase db) {

        //   db.execSQL("DROP TABLE [VENDOR_MASTER]");

//        db.execSQL("ALTER TABLE [VENDOR_MASTER] ADD [BalanceType] VARCHAR(15)");


        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[VENDOR_MASTER]")
                .concat("(")
                .concat("[VENDOR_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[VENDOR_NAME] VARCHAR(100),")
                .concat("[CONTACT_NO] VARCHAR(15),")
                .concat("[ADDRESS] VARCHAR(200),")
                .concat("[GST_NO] VARCHAR(20),")
                .concat("[TYPE] VARCHAR(15),")
                .concat("[ACTIVE] VARCHAR(3),")
                .concat("[REMARK] VARCHAR(200),")
                .concat("[BalanceType] VARCHAR(15),")
                .concat("[OpeningStock] DOUBLE,")
                .concat("[SORT_NO] INTEGER")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "VENDOR_MASTER---->>>" + qry);
        db.execSQL(qry);

        ClsVendor objvendor = new ClsVendor(context);
        String where = " AND  [VENDOR_NAME] = "
                .concat("'")
                .concat("OTHER")
                .concat("' ");
//        boolean exists = objvendor.checkExists(where, db);
//        if (!exists) {
//            objvendor.setVendor_name("OTHER");
//            objvendor.setActive("YES");
//            objvendor.setTYPE("BOTH");
////            ClsVendor.Insert(objvendor, db);
//        }


    }

    private void createUnitMaster(SQLiteDatabase db) {
        String where = "";

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[UNIT_MASTER]")
                .concat("(")
                .concat("[UNIT_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[UNIT_NAME] VARCHAR(100),")
                .concat("[ACTIVE] VARCHAR(3),")
                .concat("[REMARK] VARCHAR(200),")
                .concat("[SORT_NO] INTEGER")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "UNIT_MASTER---->>>" + qry);
        db.execSQL(qry);

        boolean exists;

        ClsUnit objClsUnitMaster = new ClsUnit(context);
        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("KG")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("KG");
            objClsUnitMaster.setRemark("kilogram");
            objClsUnitMaster.setSort_no(1);
            objClsUnitMaster.setActive("YES");
//            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("LTR")
                .concat("' ");


        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("LTR");
            objClsUnitMaster.setRemark("liter");
            objClsUnitMaster.setSort_no(2);
            objClsUnitMaster.setActive("YES");
//            ClsUnit.Insert(objClsUnitMaster, db);
        }
        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("PIECE")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("PIECE");
            objClsUnitMaster.setRemark("single pieces");
            objClsUnitMaster.setSort_no(3);
            objClsUnitMaster.setActive("YES");
//            ClsUnit.Insert(objClsUnitMaster, db);
        }
        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("BOX")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("BOX");
            objClsUnitMaster.setRemark("box");
            objClsUnitMaster.setSort_no(4);
            objClsUnitMaster.setActive("YES");
//            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("NO.")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("NO.");
            objClsUnitMaster.setRemark("number");
            objClsUnitMaster.setSort_no(5);
            objClsUnitMaster.setActive("YES");
//            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("GRAM")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);

        if (!exists) {
            objClsUnitMaster.setUnit_name("GRAM");
            objClsUnitMaster.setRemark("GRAM");
            objClsUnitMaster.setSort_no(6);
            objClsUnitMaster.setActive("YES");
//            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("HECTOGRAM")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("HECTOGRAM");
            objClsUnitMaster.setRemark("HECTOGRAM");
            objClsUnitMaster.setSort_no(7);
            objClsUnitMaster.setActive("YES");
//            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("DECIGRAM")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("DECIGRAM");
            objClsUnitMaster.setRemark("DECIGRAM");
            objClsUnitMaster.setSort_no(8);
            objClsUnitMaster.setActive("YES");
//            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("CENTIGRAM")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("CENTIGRAM");
            objClsUnitMaster.setRemark("CENTIGRAM");
            objClsUnitMaster.setSort_no(9);
            objClsUnitMaster.setActive("YES");
//            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("MILLIGRAM")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("MILLIGRAM");
            objClsUnitMaster.setRemark("MILLIGRAM");
            objClsUnitMaster.setSort_no(9);
            objClsUnitMaster.setActive("YES");
//            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("CARAT")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("CARAT");
            objClsUnitMaster.setRemark("CARAT");
            objClsUnitMaster.setSort_no(10);
            objClsUnitMaster.setActive("YES");
//            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("METER")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("METER");
            objClsUnitMaster.setRemark("METER");
            objClsUnitMaster.setSort_no(11);
            objClsUnitMaster.setActive("YES");
//            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("DECIMETER")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("DECIMETER");
            objClsUnitMaster.setRemark("DECIMETER");
            objClsUnitMaster.setSort_no(13);
            objClsUnitMaster.setActive("YES");
//            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("CENTIMETER")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("CENTIMETER");
            objClsUnitMaster.setRemark("CENTIMETER");
            objClsUnitMaster.setSort_no(14);
            objClsUnitMaster.setActive("YES");
//            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("MILLIMETER")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("MILLIMETER");
            objClsUnitMaster.setRemark("MILLIMETER");
            objClsUnitMaster.setSort_no(15);
            objClsUnitMaster.setActive("YES");
//            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("MILE")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("MILE");
            objClsUnitMaster.setRemark("MILE");
            objClsUnitMaster.setSort_no(16);
            objClsUnitMaster.setActive("YES");
//            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("INCH")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("INCH");
            objClsUnitMaster.setRemark("INCH");
            objClsUnitMaster.setSort_no(17);
            objClsUnitMaster.setActive("YES");
//            ClsUnit.Insert(objClsUnitMaster, db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("FOOT")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("FOOT");
            objClsUnitMaster.setRemark("FOOT");
            objClsUnitMaster.setSort_no(18);
            objClsUnitMaster.setActive("YES");
//            ClsUnit.Insert(objClsUnitMaster, db);
        }
        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("YARD")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("YARD");
            objClsUnitMaster.setRemark("YARD");
            objClsUnitMaster.setSort_no(19);
            objClsUnitMaster.setActive("YES");
//            ClsUnit.Insert(objClsUnitMaster, db);
        }
        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("MICROGRAM")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where, db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("MICROGRAM");
            objClsUnitMaster.setRemark("MICROGRAM");
            objClsUnitMaster.setSort_no(20);
            objClsUnitMaster.setActive("YES");
//            ClsUnit.Insert(objClsUnitMaster, db);
        }
    }

    private void createSizeMaster(SQLiteDatabase db) {

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[SIZE_MASTER]")
                .concat("(")
                .concat("[SIZE_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[SIZE_NAME] VARCHAR(100),")
                .concat("[ACTIVE] VARCHAR(3),")
                .concat("[REMARK] VARCHAR(200),")
                .concat("[SORT_NO] INTEGER")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "SIZE_MASTER---->>>" + qry);
        db.execSQL(qry);
    }


    private void createCategoryMaster(SQLiteDatabase db) {
        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[CATEGORY_MASTER]")
                .concat("(")
                .concat("[CATEGORY_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[CATEGORY_NAME] VARCHAR(100),")
                .concat("[ACTIVE] VARCHAR(3),")
                .concat("[REMARK] VARCHAR(200),")
                .concat("[SORT_NO] INTEGER")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "CATEGORY_MASTER---->>>" + qry);
        db.execSQL(qry);

    }


    public void createStates(SQLiteDatabase db) {

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[State_MASTER]")
                .concat("(")
                .concat("[STATE_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[State_Name] VARCHAR(200),")
                .concat("[StateCode] VARCHAR(20),")
                .concat("[CountryId] INTEGER")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "CATEGORY_MASTER---->>>" + qry);
        db.execSQL(qry);

    }

    public void createCity(SQLiteDatabase db) {
        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[City_MASTER]")
                .concat("(")
                .concat("[City_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[City_Name] VARCHAR(200),")
                .concat("[CityCode] VARCHAR(20),")
                .concat("[StateId] INTEGER")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "CATEGORY_MASTER---->>>" + qry);
        db.execSQL(qry);

    }


    private void createPendingPaymentBulkSms(SQLiteDatabase db) {
        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[PendingPaymentBulkSms]")
                .concat("(")
                .concat("[PendingPaymentBulkSms_id] INTEGER PRIMARY KEY AUTOINCREMENT ")
                .concat(",[orderId] INTEGER")
                .concat(",[orderNo] VARCHAR(50)")
                .concat(",[mobileNo] VARCHAR(50) ")
                .concat(",[Customer_Name] VARCHAR(500)")
                .concat(",[Entry_Datetime] DATETIME")
                .concat(",[message] VARCHAR(5000)")
                .concat(",[message_length] INT")
                .concat(",[Status] VARCHAR(50)")
                .concat(",[Source] VARCHAR(50)")
                .concat(",[SendSMSID] VARCHAR(50)")
                .concat(",[Credit] INTEGER")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "CATEGORY_MASTER---->>>" + qry);
        db.execSQL(qry);

//        .concat("[SalesSMSLogsMaster]")
//                .concat("(")
//                .concat("[LogId] INTEGER PRIMARY KEY AUTOINCREMENT")
//                .concat(",[orderId] INTEGER")
//                .concat(",[orderNo] VARCHAR(50)")
//                .concat(",[mobileNo] VARCHAR(50) ")
//                .concat(",[Customer_Name] VARCHAR(500)")
//                .concat(",[Entry_Datetime] DATETIME")
//                .concat(",[message] VARCHAR(5000)")
//                .concat(",[invoice_attachment] VARCHAR(3)")
//                .concat(",[Status] VARCHAR(50)")
//                .concat(",[UtilizeType] VARCHAR(10)")
//                .concat(",[SmsStatus] VARCHAR(50)")
//                .concat(",[Type] VARCHAR(50)")
//                .concat(",[Credit] INTEGER")
//                .concat(",[SendSMSID] VARCHAR(50)")
//                .concat(",[Remark] VARCHAR(200)")
//                .concat(")")
//                .concat(";");

    }


    public void createFeatures(SQLiteDatabase db) {
        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[Features]")
                .concat("(")
                .concat("[FeatureId] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[FeatureName] VARCHAR(100),")
                .concat("[Description] VARCHAR(200)")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "CATEGORY_MASTER---->>>" + qry);
        db.execSQL(qry);

    }

    public void createServices(SQLiteDatabase db) {
        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[Services]")
                .concat("(")
                .concat("[ServiceId] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[ServiceName] VARCHAR(100),")
                .concat("[ServiceCode] VARCHAR(20),")
                .concat("[ServiceType] VARCHAR(10),")
                .concat("[Rate] DOUBLE,")
                .concat("[ApplyTax] VARCHAR(3),")
                .concat("[TaxSlabId] INT,")
                .concat("[TaxType] Varchar(15),")
                .concat("[Active] Varchar(3),")
                .concat("[Description] Varchar(200),")
                .concat("[ApproxServiceMinute] INT,")
                .concat("[AvailableDay] Varchar(200)")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "CATEGORY_MASTER---->>>" + qry);
        db.execSQL(qry);
    }


    public void createServiceImages(SQLiteDatabase db) {
        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[ServiceImages]")
                .concat("(")
                .concat("[ServiceImageId] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[ServiceId] INT,")
                .concat("[DisplayOrder] INT,")
                .concat("[FilePath] Varchar(500),")
                .concat("[FileName] Varchar(100)")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "CATEGORY_MASTER---->>>" + qry);
        db.execSQL(qry);
    }

    public void createServiceFeatures(SQLiteDatabase db) {
        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[ServiceFeatures]")
                .concat("(")
                .concat("[ServiceFeatureId] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[ServiceId] INT,")
                .concat("[FeatureId] INT")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "CATEGORY_MASTER---->>>" + qry);
        db.execSQL(qry);
    }

    public void createEmployeeServiceSetting(SQLiteDatabase db) {
        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[EmployeeServiceSetting]")
                .concat("(")
                .concat("[EmployeeServiceId] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[ServiceId] INT,")
                .concat("[EmployeeId] INT,")
                .concat("[SetCommission] Varchar(3),")
                .concat("[CommissionPercentage] DOUBLE,")
                .concat("[CommissionAmount] DOUBLE,")
                .concat("[Description] Varchar(200)")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "CATEGORY_MASTER---->>>" + qry);
        db.execSQL(qry);
    }


    public void createCombo(SQLiteDatabase db) {
        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[Combo]")
                .concat("(")
                .concat("[ComboId] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[ComboName] Varchar(150),")
                .concat("[ComboCode] Varchar(20),")
                .concat("[Rate] DOUBLE,")
                .concat("[ApplyTax] Varchar(3),")
                .concat("[TaxSlabId] INT,")
                .concat("[TaxType] Varchar(15),")
                .concat("[Active] Varchar(3),")
                .concat("[Description] Varchar(200),")
                .concat("[ValidUptoDate] Datetime")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "CATEGORY_MASTER---->>>" + qry);
        db.execSQL(qry);
    }

    public void createComboService(SQLiteDatabase db) {
        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[ComboService]")
                .concat("(")
                .concat("[ComboServiceId] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[ComboId] INT,")
                .concat("[ServiceId] INT")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "CATEGORY_MASTER---->>>" + qry);
        db.execSQL(qry);
    }


}
