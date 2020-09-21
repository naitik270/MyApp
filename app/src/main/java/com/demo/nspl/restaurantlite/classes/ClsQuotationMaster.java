package com.demo.nspl.restaurantlite.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.text.TextUtils;
import android.util.Log;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClsQuotationMaster implements Serializable {

    int Mounth = 0, Year = 0, Sms_Limit = 0;
    String MobileNo = "", CustomerName = "", CompanyName = "",
            GSTNo = "", BillDate = "", EntryDate = "", ApplyTax = "",
            PaymentMode = "", PaymentDetail = "";
    Double TotalAmount, DiscountAmount, TotalTaxAmount;
    String custAddress = "";


    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }


    static Context context;
    private static SQLiteDatabase db;

    public int getSms_Limit() {
        return Sms_Limit;
    }

    public void setSms_Limit(int sms_Limit) {
        Sms_Limit = sms_Limit;
    }

    public int getMounth() {
        return Mounth;
    }

    public void setMounth(int mounth) {
        Mounth = mounth;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
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

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getGSTNo() {
        return GSTNo;
    }

    public void setGSTNo(String GSTNo) {
        this.GSTNo = GSTNo;
    }

    public void setBillDate(String billDate) {
        BillDate = billDate;
    }

    public String getEntryDate() {
        return EntryDate;
    }

    public void setEntryDate(String entryDate) {
        EntryDate = entryDate;
    }

    public String getApplyTax() {
        return ApplyTax;
    }

    public void setApplyTax(String applyTax) {
        ApplyTax = applyTax;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    public void setPaymentDetail(String paymentDetail) {
        PaymentDetail = paymentDetail;
    }

    public Double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        TotalAmount = totalAmount;
    }

    public Double getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        DiscountAmount = discountAmount;
    }


    public Double getTotalTaxAmount() {
        return TotalTaxAmount;
    }

    public void setTotalTaxAmount(Double totalTaxAmount) {
        TotalTaxAmount = totalTaxAmount;
    }


    String SaleType = "";


    public String getSaleType() {
        return SaleType;
    }

    String QuotationDate = null;
    String ValidUptoDate = null;

    String QuotationType = "";

    double GrandTotal = 0.0;

    public double getGrandTotal() {
        return GrandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        GrandTotal = grandTotal;
    }

    public String getQuotationType() {
        return QuotationType;
    }

    public void setQuotationType(String quotationType) {
        QuotationType = quotationType;
    }

    public String getQuotationDate() {
        return QuotationDate;
    }

    public void setQuotationDate(String quotationDate) {
        QuotationDate = quotationDate;
    }

    public String getValidUptoDate() {
        return ValidUptoDate;
    }

    public void setValidUptoDate(String validUptoDate) {
        ValidUptoDate = validUptoDate;
    }

    String Status = "";

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @SuppressLint("WrongConstant")
    public static ClsQuotationResult InsertQuotation(ClsQuotationMaster objClsQuotationMaster,
                                                     List<ClsQuotationOrderDetail> list,
                                                     Context context, String mode) {
        int result = 0;
        String QuotationNo = "";
        ClsQuotationResult clsQuotationResult = new ClsQuotationResult();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            // This is to insert new order.
            if (mode.equalsIgnoreCase("New")) {
                ClsGlobal.current_quotation_no = QuotationNo =
                        String.valueOf(getNextOrderNoForQuotation(context));

                Log.e("--InsertQuotation--", "1stIf: " + ClsGlobal.current_quotation_no);

            } else {

                // This is to edit existing order.
                ClsGlobal.current_quotation_no = QuotationNo =
                        String.valueOf(objClsQuotationMaster.getQuotationNo());

                Log.e("--InsertQuotation--", "2ndElse: " + ClsGlobal.current_quotation_no);


                String deleteQry = ("DELETE FROM [QuotationMaster] WHERE [QuotationNo]  " +
                        " =").concat("'").concat(QuotationNo).concat("'")
                        .concat(" AND [QuotationID] = " + ClsGlobal.editQuotationID);


//                String deleteQry = ("DELETE FROM [QuotationMaster] WHERE [QuotationID] = " + ClsGlobal.editQuotationID);
                SQLiteStatement delete = db.compileStatement(deleteQry);
                result = delete.executeUpdateDelete();

            }

            ClsGlobal.QuotationNo = QuotationNo;

            Log.e("--InsertQuotation--", "3nd: " + ClsGlobal.QuotationNo);
            Log.e("--InsertQuotation--", "4th: " + QuotationNo);

            clsQuotationResult.setQuotationNo(QuotationNo);

            String qry = ("INSERT INTO [QuotationMaster] " +
                    "([MobileNo],[CustomerName],[CompanyName],[cust_address],[cust_email],[GSTNo],[Status],[QuotationDate],[ValidUptoDate],[QuotationNo]" +
                    ",[QuotationType],[TotalTaxAmount],[TotalAmount],[DiscountAmount],[GrandTotal],[EntryDate],[ApplyTax],[Sms_Limit] ) VALUES ('")

                    .concat(objClsQuotationMaster.getMobileNo())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objClsQuotationMaster.getCustomerName().trim().replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objClsQuotationMaster.getCompanyName().trim().replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objClsQuotationMaster.getCustAddress())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objClsQuotationMaster.getCustEmail())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objClsQuotationMaster.getGSTNo())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objClsQuotationMaster.getStatus())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objClsQuotationMaster.getQuotationDate())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objClsQuotationMaster.getValidUptoDate())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(QuotationNo)
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objClsQuotationMaster.getQuotationType()) // Sale & Wholesale
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(objClsQuotationMaster.getTotalTaxAmount()))
                    .concat(",")

                    .concat(String.valueOf(objClsQuotationMaster.getTotalAmount()))
                    .concat(",")

                    .concat(String.valueOf(objClsQuotationMaster.getDiscountAmount()))
                    .concat(",")

                    .concat(String.valueOf(objClsQuotationMaster.getGrandTotal()))
                    .concat(",")

                    .concat("'")
                    .concat(ClsGlobal.getEntryDateTime())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objClsQuotationMaster.getApplyTax())
                    .concat("'")

                    .concat(",")
                    .concat(String.valueOf(0))

                    .concat(");");

            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.e("--InsertQuotation--", "qry: " + qry);

            Cursor c = db.rawQuery("SELECT last_insert_rowid()", null);
            c.moveToFirst();
            int insertedID = c.getInt(0);

            ClsGlobal.GenrateQuotationDetail_last_id = String.valueOf(c.getInt(0));

            Log.e("--InsertQuotation--", "insertedID: " + insertedID);
            Log.e("--InsertQuotation--", "last_id: " + ClsGlobal.GenrateQuotationDetail_last_id);

            String deleteQry = ("DELETE FROM [QuotationDetail] WHERE [QuotationNo]  = " +
                    "").concat("'").concat(String.valueOf(objClsQuotationMaster.getQuotationNo()).concat("'"));

            Log.e("--InsertQuotation--", "5th: " + objClsQuotationMaster.getQuotationNo());

            if (!mode.equalsIgnoreCase("New")) {
                deleteQry += "".concat(" AND [QuotationID] = " + ClsGlobal.editQuotationID);
            }

            SQLiteStatement delete = db.compileStatement(deleteQry);
            result = delete.executeUpdateDelete();

            Log.e("--InsertQuotation--", "Delete_Result: " + result);
            Log.e("--InsertQuotation--", "deleteQry: " + deleteQry);

            for (ClsQuotationOrderDetail currentObj : list) {
                currentObj.setAmount(currentObj.getSaleRateWithoutTax() * currentObj.getQuantity());
                if (objClsQuotationMaster.getApplyTax().equalsIgnoreCase("yes")) {
                    currentObj.setGrandTotal((currentObj.getSaleRateWithoutTax() * currentObj.getQuantity()) + (currentObj.getTotalTaxAmount() * currentObj.getQuantity()));

                } else {
                    currentObj.setGrandTotal(currentObj.getSaleRateWithoutTax() * currentObj.getQuantity());
                }
                list.set(list.indexOf(currentObj), currentObj);
            }

            for (ClsQuotationOrderDetail currentObj : list) {
                String qryQuotationDetail = ("INSERT INTO [QuotationDetail] ([QuotationID]," +
                        "[QuotationNo],[ItemCode],[Item],[Unit],[ItemComment],[Rate],[SaleRate]" +
                        ",[SaleRateWithoutTax],[Quantity],[Discount_per],[Discount_amt],[Amount],[CGST],[SGST],[IGST],[TotalTaxAmount]," +
                        "[GrandTotal]) VALUES (")

                        .concat(String.valueOf(insertedID))
                        .concat(",")

                        .concat("'")
                        .concat(QuotationNo)
                        .concat("'")
                        .concat(",")

                        .concat("'")
                        .concat(currentObj.getItemCode())
                        .concat("'")
                        .concat(",")

                        .concat("'")
                        .concat(currentObj.getItem().trim().replace("'", "''"))
                        .concat("'")
                        .concat(",")

                        .concat("'")
                        .concat(currentObj.getUnit())
                        .concat("'")
                        .concat(",")

                        .concat("'")
                        .concat(currentObj.getItemComment().trim().replace("'", "''"))
                        .concat("'")
                        .concat(",")

                        .concat(String.valueOf(currentObj.getRate()))
                        .concat(",")

                        .concat(String.valueOf(currentObj.getSaleRate()))
                        .concat(",")

                        .concat(String.valueOf(currentObj.getSaleRateWithoutTax()))
                        .concat(",")

                        .concat(String.valueOf(currentObj.getQuantity()))
                        .concat(",")

                        .concat(String.valueOf(currentObj.getDiscount_per()))
                        .concat(",")

                        .concat(String.valueOf(currentObj.getDiscount_amt()))
                        .concat(",")

                        .concat(String.valueOf(currentObj.getAmount()))
                        .concat(",")

                        .concat(String.valueOf(currentObj.getCGST()))
                        .concat(",")

                        .concat(String.valueOf(currentObj.getSGST()))
                        .concat(",")

                        .concat(String.valueOf(currentObj.getIGST()))
                        .concat(",")

                        .concat(String.valueOf(currentObj.getTotalTaxAmount()))
                        .concat(",")

                        .concat(String.valueOf(currentObj.getGrandTotal()))
                        .concat(");");

                Log.e("--InsertQuotation--", "InsertQuotationDetail: " + qryQuotationDetail);

                SQLiteStatement statementInsertInventoryOrderDetail = db.compileStatement(qryQuotationDetail);
                int InsertInventoryOrderDetail = statementInsertInventoryOrderDetail.executeUpdateDelete();
                Log.e("--InsertQuotation--", "ResultQuotation: " + InsertInventoryOrderDetail);
            }

            db.close();
        } catch (Exception e) {
            Log.e("--InsertQuotation--", "Exception: " + e.getMessage());
            e.getMessage();
        }

        clsQuotationResult.setResult(result);
        return clsQuotationResult;
    }


    @SuppressLint("WrongConstant")
    public static int UpdateOrderSequence(Context context, String orderNo, String applyTax) {
        int result = 0;

        Log.e("INSERT", "orderNo--->>" + orderNo);
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            // Delete By OrderSequence.
            String deleteQry = "Delete from [OrderSequence] where TaxApplied = "
                    .concat("'")
                    .concat(applyTax)
                    .concat("'");

            SQLiteStatement delete = db.compileStatement(deleteQry);
            result = delete.executeUpdateDelete();
            Log.e("Check", "UpdateOrderSequence deleteQry result: " + result);

            // Insert new Order no into OrderSequence.
            String qry = ("INSERT INTO [OrderSequence] " +
                    "([OrderNo],[TaxApplied]) VALUES (")
                    .concat("'")
                    .concat(orderNo)
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(applyTax)
                    .concat("'")
                    .concat(");");

            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.e("Check", "INSERT INTO result: " + result);
            Log.e("INSERT", "ExpenseType--->>" + qry);


        } catch (Exception e) {
            Log.e("ClsInventoryOrderMaster", "Insert>>>>>>>>" + e.getMessage());
            e.getMessage();
        }


        return result;
    }


    public String getCustEmail() {
        return custEmail;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }

    String custEmail = "";


    @SuppressLint("WrongConstant")
    public static String getNextQuotationNo(Context context) {
        String result = "";

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            String qry = "Select MAX(IFNULL([QuotationNo],0)) as [MaxOrderNo] " +
                    "from [QuotationMaster];";


            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));
            Log.e("--Inventory--", qry);

            while (cur.moveToNext()) {

                result = cur.getString(cur.getColumnIndex("MaxOrderNo"));
            }

//               while (cur.moveToNext()) {
//
//                result = String.valueOf(Integer.valueOf(cur.getString(cur.getColumnIndex("MaxOrderNo"))) + 1);
//            }


        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return result;
    }


    int QuotationID = 0;

    String QuotationNo = "";


    public int getQuotationID() {
        return QuotationID;
    }

    public void setQuotationID(int quotationID) {
        QuotationID = quotationID;
    }

    public String getQuotationNo() {
        return QuotationNo;
    }

    public void setQuotationNo(String quotationNo) {
        QuotationNo = quotationNo;
    }


    @SuppressLint("WrongConstant")
    public static ClsQuotationMaster getFillQuotationOn(String QuotationNo, int QuotationID,
                                                        Context context, SQLiteDatabase db) {
        ClsQuotationMaster getSet = new ClsQuotationMaster();

        try {
//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            String qry = "SELECT * FROM [QuotationMaster] WHERE 1=1 AND [QuotationNo] = "
                    .concat("'")
                    .concat(QuotationNo)
                    .concat("'")
                    .concat(" AND [QuotationID] = " + QuotationID)
                    .concat(";");


            Cursor cur = db.rawQuery(qry, null);

            Log.e("--FillQuotationOn--", "Qry: " + qry);
            Log.e("--FillQuotationOn--", "Count: " + cur.getCount());

            while (cur.moveToNext()) {

                getSet.setQuotationID(cur.getInt(cur.getColumnIndex("QuotationID")));
                getSet.setMobileNo(cur.getString(cur.getColumnIndex("MobileNo")));
                getSet.setCustomerName(cur.getString(cur.getColumnIndex("CustomerName")));
                getSet.setCompanyName(cur.getString(cur.getColumnIndex("CompanyName")));
                getSet.setCustAddress(cur.getString(cur.getColumnIndex("cust_address")));
                getSet.setCustEmail(cur.getString(cur.getColumnIndex("cust_email")));
                getSet.setGSTNo(cur.getString(cur.getColumnIndex("GSTNo")));
                getSet.setStatus(cur.getString(cur.getColumnIndex("Status")));
                getSet.setQuotationDate(cur.getString(cur.getColumnIndex("QuotationDate")));
                getSet.setValidUptoDate(cur.getString(cur.getColumnIndex("ValidUptoDate")));
                getSet.setQuotationNo(cur.getString(cur.getColumnIndex("QuotationNo")));
                getSet.setQuotationType(cur.getString(cur.getColumnIndex("QuotationType")));
                getSet.setTotalAmount(cur.getDouble(cur.getColumnIndex("TotalAmount")));
                getSet.setTotalTaxAmount(cur.getDouble(cur.getColumnIndex("TotalTaxAmount")));
                getSet.setDiscountAmount(cur.getDouble(cur.getColumnIndex("DiscountAmount")));
                getSet.setGrandTotal(cur.getDouble(cur.getColumnIndex("GrandTotal")));
                getSet.setEntryDate(cur.getString(cur.getColumnIndex("EntryDate")));
                getSet.setApplyTax(cur.getString(cur.getColumnIndex("ApplyTax")));
                getSet.setSms_Limit(cur.getInt(cur.getColumnIndex("Sms_Limit")));

            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return getSet;
    }

    @SuppressLint("WrongConstant")
    public static ClsQuotationMaster getGenerateInvoice(String OrderNo, int OrderID, String QuotationNo, int QuotationID,
                                                        Context context, SQLiteDatabase db) {
        ClsQuotationMaster getSet = new ClsQuotationMaster();
        int result = 0;
        try {
//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            //copy data
            String qryCOPY = " INSERT INTO [InventoryOrderDetail] ("
                    .concat("[OrderID]")
                    .concat(",[OrderNo]")
                    .concat(",[ItemCode]")
                    .concat(",[Item]")
                    .concat(",[Rate]")
                    .concat(",[SaleRate]")
                    .concat(",[Quantity]")
                    .concat(",[Amount]")
                    .concat(",[CGST]")
                    .concat(",[SGST]")
                    .concat(",[IGST]")
                    .concat(",[TotalTaxAmount]")
                    .concat(",[GrandTotal]")
                    .concat(",[SaleRateWithoutTax]")
                    .concat(",[Discount_per]")
                    .concat(",[Discount_amt]")
                    .concat(",[ItemComment]")
                    .concat(",[SaveStatus]")
                    .concat(")")
                    .concat("     ")


                    .concat(" SELECT ")
                    .concat(String.valueOf(OrderID))
                    .concat(",'").concat(OrderNo).concat("'")
                    .concat(",[ItemCode]")
                    .concat(",[Item]")
                    .concat(",[Rate]")
                    .concat(",[SaleRate]")
                    .concat(",[Quantity]")
                    .concat(",[Amount]")
                    .concat(",[CGST]")
                    .concat(",[SGST]")
                    .concat(",[IGST]")
                    .concat(",[TotalTaxAmount]")
                    .concat(",[GrandTotal]")
                    .concat(",[SaleRateWithoutTax]")
                    .concat(",[Discount_per]")
                    .concat(",[Discount_amt]")
                    .concat(",[ItemComment]")
                    .concat(",'NO'")
                    .concat(" FROM [QuotationDetail] ")
                    .concat(" WHERE 1=1 ")
                    .concat(" AND [QuotationNo] = ").concat("'").concat(QuotationNo).concat("'")
                    .concat(" AND [QuotationID] = ").concat(String.valueOf(QuotationID));

            SQLiteStatement statement = db.compileStatement(qryCOPY);
            result = statement.executeUpdateDelete();


            if (result > 0) {
                getSet.setStatus("SUCCESS");
                Log.e("--Main--", "Step:14");
            } else {
                Log.e("--Main--", "Step:15");
                getSet.setStatus("PENDING");
            }

            Log.e("--FillQuotationOn--", "Qry: " + qryCOPY);
            Log.e("--FillQuotationOn--", "result: " + result);


        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return getSet;
    }


    @SuppressLint("WrongConstant")
    public static int getSmsLimit_Quotation(String where, Context context, SQLiteDatabase db) {

        int SmsLimit = 0;
        try {

//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String qry = ("SELECT Sms_Limit FROM [QuotationMaster] ")
                    .concat(" WHERE 1=1 ")
                    .concat(where);// " AND ORD.[MobileNo] = '9586050796' "


            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));
            Log.e("qry", qry);

            while (cur.moveToNext()) {
                SmsLimit = cur.getInt(cur.getColumnIndex("Sms_Limit"));
            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return SmsLimit;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsQuotationMaster> getQuotationList(String where, boolean setLimit, Context context) {
        List<ClsQuotationMaster> list = new ArrayList<>();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            String qry = "SELECT * FROM [QuotationMaster]"
                    .concat(" WHERE 1=1 ")
                    .concat(where);


            if (setLimit)
                qry = qry.concat(" ORDER BY [QuotationDate] DESC ").concat(" LIMIT 50 ");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--QuotationList--", "qry: " + qry);
            Log.e("--QuotationList--", "Count: " + cur.getCount());

            while (cur.moveToNext()) {
                ClsQuotationMaster getSet = new ClsQuotationMaster();

                getSet.setQuotationID(cur.getInt(cur.getColumnIndex("QuotationID")));
                getSet.setMobileNo(cur.getString(cur.getColumnIndex("MobileNo")));
                getSet.setCustomerName(cur.getString(cur.getColumnIndex("CustomerName")));
                getSet.setCompanyName(cur.getString(cur.getColumnIndex("CompanyName")));
                getSet.setCustAddress(cur.getString(cur.getColumnIndex("cust_address")));
                getSet.setCustEmail(cur.getString(cur.getColumnIndex("cust_email")));
                getSet.setGSTNo(cur.getString(cur.getColumnIndex("GSTNo")));
                getSet.setStatus(cur.getString(cur.getColumnIndex("Status")));
                getSet.setQuotationDate(cur.getString(cur.getColumnIndex("QuotationDate")));
                getSet.setValidUptoDate(cur.getString(cur.getColumnIndex("ValidUptoDate")));
                getSet.setQuotationNo(cur.getString(cur.getColumnIndex("QuotationNo")));
                getSet.setQuotationType(cur.getString(cur.getColumnIndex("QuotationType")));
                getSet.setTotalAmount(cur.getDouble(cur.getColumnIndex("TotalAmount")));
                getSet.setTotalTaxAmount(cur.getDouble(cur.getColumnIndex("TotalTaxAmount")));
                getSet.setDiscountAmount(cur.getDouble(cur.getColumnIndex("DiscountAmount")));
                getSet.setGrandTotal(cur.getDouble(cur.getColumnIndex("GrandTotal")));
                getSet.setEntryDate(cur.getString(cur.getColumnIndex("EntryDate")));
                getSet.setApplyTax(cur.getString(cur.getColumnIndex("ApplyTax")));
                getSet.setSms_Limit(cur.getInt(cur.getColumnIndex("Sms_Limit")));

                list.add(getSet);
            }

        } catch (Exception e) {
            e.getMessage();
            Log.e("--QuotationList--", "Exception: " + e.getMessage());
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static int Update(int OrderId, String OrderNo, int limit, Context context) {
        int result = 0;
        try {


            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String strSql = "UPDATE [QuotationMaster] SET "

                    .concat(" [Sms_Limit] = ")
                    .concat(String.valueOf(limit))


                    .concat(" WHERE [QuotationID] = ")
                    .concat(String.valueOf(OrderId))

                    .concat(" AND [QuotationNo] = ")
                    .concat("'")
                    .concat(OrderNo)
                    .concat("'")
                    .concat(";");


            Log.e("--updateStatus--", "Update: " + strSql);
            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();
            //db.execSQL(strSql);


        } catch (Exception e) {
            Log.e("ClsItem", "Update" + e.getMessage());
        }
        return result;
    }





    @SuppressLint("WrongConstant")
    public static int UpdateStatus(String _quotationID, String Status, Context context) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String strSql = "UPDATE [QuotationMaster] SET "

                    .concat(" [Status] = ")
                    .concat("'")
                    .concat(Status)
                    .concat("'")

                    .concat(" WHERE [QuotationID] = ")
                    .concat(String.valueOf(_quotationID))
                    .concat(";");


            Log.e("--updateStatus--", "Update: " + strSql);
            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();

        } catch (Exception e) {
            Log.e("--updateStatus--", "Exception: " + e.getMessage());
        }
        return result;
    }


    @SuppressLint("WrongConstant")
    public static ClsQuotationMaster getQuotationOrder(String QuotationNo, Context c) {
        ClsQuotationMaster getSet = new ClsQuotationMaster();

        try {
            db = c.openOrCreateDatabase(ClsGlobal.Database_Name, c.MODE_APPEND, null);
            String qry = "SELECT * FROM [QuotationMaster] WHERE 1=1 AND [QuotationNo] = "
                    .concat("'")
                    .concat(QuotationNo)
                    .concat("'")
                    .concat(";");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--Invoice--Count-- ", String.valueOf(cur.getCount()));
            Log.e("--Invoice-- ", qry);

            while (cur.moveToNext()) {

                getSet.setQuotationID(cur.getInt(cur.getColumnIndex("QuotationID")));
                getSet.setMobileNo(cur.getString(cur.getColumnIndex("MobileNo")));
                getSet.setCustomerName(cur.getString(cur.getColumnIndex("CustomerName")));
                getSet.setCompanyName(cur.getString(cur.getColumnIndex("CompanyName")));
                getSet.setCustAddress(cur.getString(cur.getColumnIndex("cust_address")));
                getSet.setCustEmail(cur.getString(cur.getColumnIndex("cust_email")));
                getSet.setGSTNo(cur.getString(cur.getColumnIndex("GSTNo")));
                getSet.setStatus(cur.getString(cur.getColumnIndex("Status")));
                getSet.setQuotationDate(cur.getString(cur.getColumnIndex("QuotationDate")));
                getSet.setValidUptoDate(cur.getString(cur.getColumnIndex("ValidUptoDate")));
                getSet.setQuotationNo(cur.getString(cur.getColumnIndex("QuotationNo")));
                getSet.setQuotationType(cur.getString(cur.getColumnIndex("QuotationType")));
                getSet.setTotalAmount(cur.getDouble(cur.getColumnIndex("TotalAmount")));
                getSet.setTotalTaxAmount(cur.getDouble(cur.getColumnIndex("TotalTaxAmount")));
                getSet.setDiscountAmount(cur.getDouble(cur.getColumnIndex("DiscountAmount")));
                getSet.setGrandTotal(cur.getDouble(cur.getColumnIndex("GrandTotal")));
                getSet.setEntryDate(cur.getString(cur.getColumnIndex("EntryDate")));
                getSet.setApplyTax(cur.getString(cur.getColumnIndex("ApplyTax")));
                getSet.setSms_Limit(cur.getInt(cur.getColumnIndex("Sms_Limit")));

            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return getSet;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsQuotationMaster> getMonthWiseQuotationList(Context context, String where) {
        List<ClsQuotationMaster> list = new ArrayList<>();

        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String qry = "SELECT strftime('%m', [QuotationDate]) as [Month], strftime('%Y', [QuotationDate]) as [Year] , SUM(GrandTotal) AS [GrandTotal] FROM [QuotationMaster] "
                    .concat(" WHERE 1=1 ")
                    .concat(where)
                    .concat(" GROUP BY strftime('%m', [QuotationDate]), strftime('%Y', [QuotationDate]) ")
                    .concat("ORDER BY strftime('%Y', [QuotationDate]) DESC, strftime('%m', [QuotationDate]) DESC");

//            String qry = "SELECT * FROM [QuotationMaster]";

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--MonthWise--", "cur: " + cur.getCount());
            Log.e("--MonthWise--", "List: " + qry);

            while (cur.moveToNext()) {

                ClsQuotationMaster getSet = new ClsQuotationMaster();

                Gson gson = new Gson();
                String jsonInString = gson.toJson(getSet);
                Log.d("--MonthWise--", "getSet:  " + jsonInString);

                if (cur.getString(cur.getColumnIndex("Month")) != null &&
                        cur.getString(cur.getColumnIndex("Month")) != "" &&
                        cur.getString(cur.getColumnIndex("Year")) != null &&
                        cur.getString(cur.getColumnIndex("Year")) != ""
                        && cur.getDouble(cur.getColumnIndex("GrandTotal")) != 0.0) {

                    getSet.setMounth(Integer.parseInt(cur.getString(cur.getColumnIndex("Month"))));
                    getSet.setYear(Integer.parseInt(cur.getString(cur.getColumnIndex("Year"))));
                    getSet.setGrandTotal(cur.getDouble(cur.getColumnIndex("GrandTotal")));

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
    public static int getNextOrderNoForQuotation(Context context) {
        int NextOrderNo = 0;
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String qry1 = "Select MAX(IFNULL([QuotationID],0)) as [MaxOrderNo] from [QuotationMaster];";
            Cursor cur1 = db.rawQuery(qry1, null);

            if (cur1.getCount() == 1) {
                cur1.moveToFirst();
                NextOrderNo = cur1.getInt(cur1.getColumnIndex("MaxOrderNo"));
            }
        } catch (Exception e) {
            e.getMessage();
            Log.e("MaxOrderNoSdf", "GetList" + e.getMessage());
        }
        return NextOrderNo + 1;
    }


    @SuppressLint("WrongConstant")
    public static int deleteQuotationMaster(String QuotationNo, int QuotationID, Context context) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String strSql = "DELETE FROM [QuotationMaster] WHERE [QuotationNo] = '"
                    .concat(String.valueOf(QuotationNo))
                    .concat("'")
                    .concat(" AND [QuotationID] = " + QuotationID)
                    .concat(";");

            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();
            Log.e("--QuotationDetail--", "strSql: " + strSql);
            Log.e("--QuotationDetail--", "result: " + result);
            db.close();
        } catch (Exception e) {
            Log.e("--QuotationDetail--", "Exception: " + e.getMessage());
            e.getMessage();
        }
        return result;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsQuotationMaster> getCustomerListFromDate(String _where, Context context) {
        List<ClsQuotationMaster> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);


            String qry = "SELECT [CustomerName], [MobileNo] FROM QuotationMaster "
                    .concat("WHERE 1=1 ")
                    .concat(_where)
                    .concat("GROUP BY [MobileNo]");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--qry--", "qry: " + qry);
            Log.e("--qry--", "Count: " + cur.getCount());

            while (cur.moveToNext()) {
                ClsQuotationMaster getSet = new ClsQuotationMaster();
                getSet.setMobileNo(cur.getString(cur.getColumnIndex("MobileNo")));
                getSet.setCustomerName(cur.getString(cur.getColumnIndex("CustomerName")));
                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("--qry--", "Exception: " + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static void UpdateQuotationStatus(Context context) {
        int result = 0;
        try {


            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String _today = ClsGlobal.getEntryDateTime(); //2020-02-12 11:56:20 (format)

            String sqlStr = "UPDATE [QuotationMaster] SET "
                    .concat("[Status]='Expired' ")
                    .concat(" WHERE upper([Status])=upper('Pending') ")
                    .concat(" AND [ValidUptoDate] < ")
                    .concat("'")
                    .concat(ClsGlobal.getEntryDateTime())
                    .concat("'");


            Log.e("--updateStatus--", "Update: " + sqlStr);
            SQLiteStatement statement = db.compileStatement(sqlStr);
            result = statement.executeUpdateDelete();
            Log.e("--updateStatus--", "result: " + result);
        } catch (Exception e) {
            Log.e("--updateStatus--", "Exception: " + e.getMessage());
        }

    }

    @SuppressLint("WrongConstant")
    public static void UpdateQuotationDateFormat(Context context) {
        int result = 0;
        List<ClsQuotationMaster> list = new ArrayList<>();

        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT [QuotationID],[QuotationDate],[ValidUptoDate] " +
                    "FROM QuotationMaster ";

            Cursor cur = db.rawQuery(qry, null);

            while (cur.moveToNext()) {
                ClsQuotationMaster getSet = new ClsQuotationMaster();
                getSet.setQuotationID(cur.getInt(cur.getColumnIndex("QuotationID")));
                getSet.setQuotationDate(cur.getString(cur.getColumnIndex("QuotationDate")));
                getSet.setValidUptoDate(cur.getString(cur.getColumnIndex("ValidUptoDate")));
                list.add(getSet);
            }

            List<String> _qryList = new ArrayList<>();

            for (ClsQuotationMaster obj : list) {

                if (obj.getQuotationDate() != null
                        && !obj.getQuotationDate().trim().isEmpty()
                        && obj.getQuotationDate().length() != 0
                        && !obj.getQuotationDate().equalsIgnoreCase("")) {

                    String dbDate = ClsGlobal.getValidDateFormat(obj.getQuotationDate());
                    String qryStr = "UPDATE QuotationMaster set "
                            .concat(" [QuotationDate] = ")
                            .concat("'")
                            .concat(dbDate)
                            .concat("'")
                            .concat(" WHERE [QuotationID] = ")
                            .concat(String.valueOf(obj.getQuotationID())
                                    .concat(";"));

                    _qryList.add(qryStr);


                    SQLiteStatement statement = db.compileStatement(qryStr);
                    result = statement.executeUpdateDelete();
                    Log.d("--update--", "IF_qry: " + qryStr);
                    Log.d("--update--", "IF_result: "+result);

                }

                if (obj.getValidUptoDate() != null
                        && obj.getValidUptoDate().length() != 0
                        && !obj.getValidUptoDate().trim().isEmpty()
                        && !obj.getValidUptoDate().equalsIgnoreCase("")) {

                    String dbDate = ClsGlobal.getValidDateFormat(obj.getValidUptoDate());
                    String qryStr = "UPDATE QuotationMaster set "
                            .concat(" [ValidUptoDate] = ")
                            .concat("'")
                            .concat(dbDate)
                            .concat("'")
                            .concat(" WHERE [QuotationID] = ")
                            .concat(String.valueOf(obj.getQuotationID())
                                    .concat(";"));

                    _qryList.add(qryStr);


                    SQLiteStatement statement = db.compileStatement(qryStr);
                    result = statement.executeUpdateDelete();
                    Log.d("--update--", "IF_qryVal: " + qryStr);
                    Log.d("--update--", "IF_resultVal: "+result);


                }
            }

            db.close();
        } catch (Exception e) {
            e.getMessage();
            Log.e("--updateStatus--", "Exception: " + e.getMessage());
        }

    }


    @SuppressLint({"WrongConstant", "DefaultLocale"})
    public static String getTemporaryOrderNoQuotation(String mode) {
        String generatedOrderNo = "";

        Random random = new Random();

        generatedOrderNo = String.format("%04d", random.nextInt(10000 - 2034) + 2034);
        Log.e("--TempNo--", "TempNo: " + generatedOrderNo);

        while (ClsGlobal._QuotationSaleOrderNo.equals(ClsGlobal._QuotationWholesaleOrderNo)
                && generatedOrderNo.substring(0, 1).equals("0")) {

            generatedOrderNo = String.format("%04d", random.nextInt(10000 - 2034) + 2034);

            Log.e("--TempNo--", "while: " + generatedOrderNo);

            if (mode.equalsIgnoreCase("QuotationWholesaleOrderNo")) {
                ClsGlobal._QuotationWholesaleOrderNo = generatedOrderNo;
            } else if (mode.equalsIgnoreCase("QuotationSaleOrderNo")) {
                ClsGlobal._QuotationSaleOrderNo = generatedOrderNo;
            }
        }

        return generatedOrderNo;
    }


}
