package com.demo.nspl.restaurantlite.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.demo.nspl.restaurantlite.Customer.ClsCustomerWisePayment;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.DatabaseHelper;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getNextFormattedOrderNo;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.get_Current_Day_Month_Yearly;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.set_reset_counter;
import static com.demo.nspl.restaurantlite.classes.ClsBillNoFormat.getSettingApplyTaxBillNo;

public class ClsInventoryOrderMaster implements Serializable {

    int OrderID = 0, Mounth = 0, Year = 0, Sms_Limit = 0;
    String MobileNo = "", CustomerName = "", CompanyName = "", GSTNo = "", BillDate = "",
            EntryDate = "", ApplyTax = "", PaymentMode = "", PaymentDetail = "";
    Double TotalAmount = 0.0, DiscountAmount = 0.0, TotalPaybleAmount = 0.0,
            TotalTaxAmount = 0.0, TotalReceiveableAmount = 0.0,
            PaidAmount, AdjumentAmount;
    String custAddress = "", Different_Amount_mode = "",DueDate = "",message = "";;

    String BillTo = "";
    String OrderNo = "";
    String custEmail = "";


    String QuotationDate = "";
    String ValidUptoDate = "";
    String QuotationType = "";
    boolean isSelected = false;
    double GrandTotal = 0.0;


    public String getDueDate() {
        return DueDate;
    }

    public void setDueDate(String dueDate) {
        DueDate = dueDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }


    public int getSms_Limit() {
        return Sms_Limit;
    }

    public void setSms_Limit(int sms_Limit) {
        Sms_Limit = sms_Limit;
    }

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


    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getBillTo() {
        return BillTo;
    }

    public void setBillTo(String billTo) {
        BillTo = billTo;
    }

    public String getDifferent_Amount_mode() {
        return Different_Amount_mode;
    }

    public void setDifferent_Amount_mode(String different_Amount_mode) {
        Different_Amount_mode = different_Amount_mode;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }


    static Context context;
    private static SQLiteDatabase db;

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

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
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

    public String getBillDate() {
        return BillDate;
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

    public String getPaymentDetail() {
        return PaymentDetail;
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

    public Double getTotalPaybleAmount() {
        return TotalPaybleAmount;
    }

    public void setTotalPaybleAmount(Double totalPaybleAmount) {
        TotalPaybleAmount = totalPaybleAmount;
    }

    public Double getTotalTaxAmount() {
        return TotalTaxAmount;
    }

    public void setTotalTaxAmount(Double totalTaxAmount) {
        TotalTaxAmount = totalTaxAmount;
    }

    public Double getTotalReceiveableAmount() {
        return TotalReceiveableAmount;
    }

    public void setTotalReceiveableAmount(Double totalReceiveableAmount) {
        TotalReceiveableAmount = totalReceiveableAmount;
    }

    public Double getPaidAmount() {
        return PaidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        PaidAmount = paidAmount;
    }

    public Double getAdjumentAmount() {
        return AdjumentAmount;
    }

    public void setAdjumentAmount(Double adjumentAmount) {
        AdjumentAmount = adjumentAmount;
    }

    String SaleType = "";


    public String getSaleType() {
        return SaleType;
    }

    Double SaleReturnDiscount = 0.0;

    public void setSaleType(String saleType) {
        SaleType = saleType;
    }

    public Double getSaleReturnDiscount() {
        return SaleReturnDiscount;
    }

    public void setSaleReturnDiscount(Double saleReturnDiscount) {
        SaleReturnDiscount = saleReturnDiscount;
    }

    int QuotationId = 0;

    public int getQuotationId() {
        return QuotationId;
    }

    public void setQuotationId(int quotationId) {
        QuotationId = quotationId;
    }

    @SuppressLint("WrongConstant")
    public static ClsInventoryResult Insert(ClsInventoryOrderMaster ObjInventoryOrderMaster,
                                            List<ClsInventoryOrderDetail> list, Context context, String mode) {
        int result = 0;
        String OrderNo = "";

        ClsInventoryResult clsInventoryResult = new ClsInventoryResult();

        try {
/*
            if (!db.isOpen()){

                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            }*/
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);


//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            // This is to insert new order.
            if (mode.equalsIgnoreCase("New")) {
                ClsGlobal.Current_Order_No = OrderNo = String.valueOf(getNextOrderNo(context,
                        ObjInventoryOrderMaster.getApplyTax(), ""));
//                ClsGlobal.Current_Order_No = OrderNo = "2";


                Log.e("Check", "Order :- " + OrderNo);
                Log.e("---userInfo---", "OrderNo: " + OrderNo);

//                getNextFormattedOrderNo(NextOrderNo, ObjInventoryOrderMaster.getApplyTax());

            } else {

                // This is to edit existing order.
                ClsGlobal.Current_Order_No = OrderNo = String.valueOf(ObjInventoryOrderMaster.getOrderNo());


                String deleteQry = ("DELETE FROM [InventoryOrderMaster] WHERE [OrderNo]  " +
                        " = ").concat("'").concat(OrderNo).concat("'")
                        .concat(" AND [OrderID] = " + ClsGlobal.editOrderID);
                SQLiteStatement delete = db.compileStatement(deleteQry);
                result = delete.executeUpdateDelete();

                Log.e("---userInfo---", "OrderID delete :- " + ClsGlobal.editOrderID);
                Log.e("---userInfo---", "result delete :- " + result);


            }

            clsInventoryResult.setOrderNo(OrderNo);
//            InvoiceInfoActivity.getCurrent_Order_No = OrderNo;
            Log.e("---userInfo---", "OrderNo: " + OrderNo);

            String qry = ("INSERT INTO [InventoryOrderMaster] " +
                    "([MobileNo],[CustomerName],[CompanyName],[GSTNo],[BillDate],[OrderNo],[TotalAmount]" +
                    ",[DiscountAmount],[TotalPaybleAmount],[TotalTaxAmount],[TotalReceiveableAmount]" +
                    ",[PaidAmount],[AdjumentAmount]" +
                    ",[EntryDate],[ApplyTax],[PaymentMode],[PaymentDetail]" +
                    ",[BillTo],[SaleType],[SaleReturnDiscount],[Different_Amount_mode],[Sms_Limit],[QuotationId]) VALUES ('")

                    .concat(ObjInventoryOrderMaster.getMobileNo())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjInventoryOrderMaster.getCustomerName().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjInventoryOrderMaster.getCompanyName().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjInventoryOrderMaster.getGSTNo())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjInventoryOrderMaster.getBillDate())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(OrderNo)
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(ObjInventoryOrderMaster.getTotalAmount()))
                    .concat(",")

                    .concat(String.valueOf(ObjInventoryOrderMaster.getDiscountAmount()))
                    .concat(",")

                    .concat(String.valueOf(ObjInventoryOrderMaster.getTotalPaybleAmount()))
                    .concat(",")

                    .concat(String.valueOf(ObjInventoryOrderMaster.getTotalTaxAmount()))
                    .concat(",")

                    .concat(String.valueOf(ObjInventoryOrderMaster.getTotalReceiveableAmount()))
                    .concat(",")

                    .concat(String.valueOf(ObjInventoryOrderMaster.getPaidAmount()))
                    .concat(",")

                    .concat(String.valueOf(ObjInventoryOrderMaster.getAdjumentAmount()))
                    .concat(",")

                    .concat("'")
                    .concat(ObjInventoryOrderMaster.getEntryDate())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjInventoryOrderMaster.getApplyTax())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjInventoryOrderMaster.getPaymentMode())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjInventoryOrderMaster.getPaymentDetail().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjInventoryOrderMaster.getBillTo())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjInventoryOrderMaster.getSaleType())
                    .concat("'")
                    .concat(",")


                    .concat(String.valueOf(ObjInventoryOrderMaster.getSaleReturnDiscount()))
                    .concat(",")


                    .concat("'")
                    .concat(ObjInventoryOrderMaster.getDifferent_Amount_mode())
                    .concat("'")
                    .concat(",")


                    .concat(String.valueOf(ObjInventoryOrderMaster.getSms_Limit()))
                    .concat(",")

                    .concat(String.valueOf(ObjInventoryOrderMaster.getQuotationId()))
                    .concat(");");

            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.e("---userInfo---", "InsertMaster: " + qry);


            Cursor c = db.rawQuery("SELECT last_insert_rowid()", null);
            c.moveToFirst();
            int insertedID = c.getInt(0);//orderiD
            ClsGlobal.InventoryOrderDetail_last_id = String.valueOf(c.getInt(0));//orderiD
            Log.e("---userInfo---", "insertedID: " + insertedID);

            String deleteQry = ("DELETE FROM [InventoryOrderDetail] WHERE [OrderNo]  = " +
                    "").concat("'").concat(String.valueOf(ObjInventoryOrderMaster.getOrderNo())).concat("'");

            if (!mode.equalsIgnoreCase("New")) {
                deleteQry += "".concat(" AND [OrderID] = " + ClsGlobal.editOrderID);
            }
            SQLiteStatement delete = db.compileStatement(deleteQry);
            result = delete.executeUpdateDelete();

            Log.e("---userInfo---", "deleteQry: " + deleteQry);
            Log.e("---userInfo---", "deleteResult: " + result);

            Gson gson12 = new Gson();
            String jsonInString12 = gson12.toJson(list);
            Log.e("---userInfo---", "userInfo---  " + jsonInString12);


            for (ClsInventoryOrderDetail currentObj : list) {
                currentObj.setAmount(currentObj.getSaleRateWithoutTax() * currentObj.getQuantity());
                if (ObjInventoryOrderMaster.getApplyTax().equalsIgnoreCase("yes")) {
                    currentObj.setGrandTotal((currentObj.getSaleRateWithoutTax() *
                            currentObj.getQuantity()) + (currentObj.getTotalTaxAmount() *
                            currentObj.getQuantity()));

                } else {
                    currentObj.setGrandTotal(currentObj.getSaleRateWithoutTax() * currentObj.getQuantity());
                }
                list.set(list.indexOf(currentObj), currentObj);
            }

            for (ClsInventoryOrderDetail currentObj : list) {
                String qrystatementInsertInventoryOrderDetail = ("INSERT INTO [InventoryOrderDetail]" +
                        " ([OrderID]," +
                        "[OrderNo],[ItemCode],[Item],[Rate],[SaleRate]" +
                        ",[Quantity],[Amount],[CGST],[SGST],[IGST],[TotalTaxAmount]," +
                        "[GrandTotal],[SaleRateWithoutTax]," +
                        "[Discount_per],[Discount_amt],[ItemComment]) VALUES ('")

                        .concat(String.valueOf(insertedID))
                        .concat("'")
                        .concat(",")

                        .concat("'")
                        .concat(OrderNo) //String.valueOf(currentObj.getOrderNo())
                        .concat("'")
                        .concat(",")

                        .concat("'")
                        .concat(currentObj.getItemCode().trim()
                                .replace("'", "''"))
                        .concat("'")
                        .concat(",")

                        .concat("'")
                        .concat(currentObj.getItem().trim()
                                .replace("'", "''"))
                        .concat("'")
                        .concat(",")

                        .concat(String.valueOf(currentObj.getRate()))
                        .concat(",")

                        .concat(String.valueOf(currentObj.getSaleRateWithoutTax()))
                        //.concat(String.valueOf(currentObj.getSaleRate()))
                        .concat(",")


                        .concat(String.valueOf(currentObj.getQuantity()))
                        .concat(",")

                        .concat(String.valueOf(currentObj.getAmount()))
                        .concat(",")

                        .concat(String.valueOf(currentObj.getCGST()))
                        .concat(",")

                        .concat(String.valueOf(currentObj.getSGST()))
                        .concat(",")

                        .concat(String.valueOf(currentObj.getIGST()))
                        .concat(",")

                        .concat(String.valueOf(ClsGlobal.round(currentObj.getTotalTaxAmount(), 2)))
                        .concat(",")

                        .concat(String.valueOf(currentObj.getGrandTotal()))
                        .concat(",")

                        .concat(String.valueOf(currentObj.getSaleRateWithoutTax()))
                        .concat(",")


                        .concat(String.valueOf(currentObj.getDiscount_per()))
                        .concat(",")


                        .concat(String.valueOf(currentObj.getDiscount_amt()))
                        .concat(",")


                        .concat("'")
                        .concat(currentObj.getItemComment().trim()
                                .replace("'", "''"))
                        .concat("'")

                        .concat(");");


                Log.e("---userInfo---", "InsertDetails: " + qrystatementInsertInventoryOrderDetail);

                SQLiteStatement statementInsertInventoryOrderDetail =
                        db.compileStatement(qrystatementInsertInventoryOrderDetail);
                int InsertInventoryOrderDetail = statementInsertInventoryOrderDetail
                        .executeUpdateDelete();
                Log.e("---userInfo---", "statement: " + InsertInventoryOrderDetail);
            }
            db.close();
        } catch (Exception e) {
            db.close();
            Log.e("---userInfo---", "Exception: " + e.getMessage());
            e.getMessage();
        }

        clsInventoryResult.setResult(result);
        return clsInventoryResult;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsInventoryOrderMaster> getPendingPayments(String where, Context context) {
        List<ClsInventoryOrderMaster> list = new ArrayList<>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

        try {

            io.requery.android.database.sqlite.SQLiteDatabase
                    db = databaseHelper.openDatabase();

            String qry = ("SELECT * " +
                    " FROM [InventoryOrderMaster]")
                    .concat(" WHERE 1=1 ")
                    .concat(where);

            Cursor cur = db.rawQuery(qry, null);

            Log.d("--fillValue--", "qry: " + qry);
            Log.d("--fillValue--", "count: " + cur.getCount());

            while (cur.moveToNext()) {
                ClsInventoryOrderMaster getSet = new ClsInventoryOrderMaster();
                getSet.setOrderID(cur.getInt(cur.getColumnIndex("OrderID")));
                getSet.setMobileNo(cur.getString(cur.getColumnIndex("MobileNo")));
                getSet.setCustomerName(cur.getString(cur.getColumnIndex("CustomerName")));
                getSet.setCompanyName(cur.getString(cur.getColumnIndex("CompanyName")));
                getSet.setGSTNo(cur.getString(cur.getColumnIndex("GSTNo")));
                getSet.setBillDate(cur.getString(cur.getColumnIndex("BillDate")));
                getSet.setOrderNo(cur.getString(cur.getColumnIndex("OrderNo")));
                getSet.setAdjumentAmount(cur.getDouble(cur.getColumnIndex("AdjumentAmount")));

//                getSet.setEntryDate(cur.getString(cur.getColumnIndex("EntryDate")));
                getSet.setApplyTax(cur.getString(cur.getColumnIndex("ApplyTax")));
                getSet.setSaleType(cur.getString(cur.getColumnIndex("SaleType")));

                getSet.setDifferent_Amount_mode(cur.getString(cur.getColumnIndex("Different_Amount_mode")));
                getSet.setPaymentMode(cur.getString(cur.getColumnIndex("PaymentMode")));
//                getSet.setBillTo(cur.getString(cur.getColumnIndex("BillToValue")));
//                getSet.setPaymentDetail(cur.getString(cur.getColumnIndex("PaymentDetail")));

//                getSet.setSaleReturnDiscount(cur.getDouble(cur.getColumnIndex("ReturnDiscount")));
                getSet.setTotalAmount(cur.getDouble(cur.getColumnIndex("TotalAmount")));
                getSet.setDiscountAmount(cur.getDouble(cur.getColumnIndex("DiscountAmount")));
                getSet.setTotalPaybleAmount(cur.getDouble(cur.getColumnIndex("TotalPaybleAmount")));
                getSet.setTotalTaxAmount(cur.getDouble(cur.getColumnIndex("TotalTaxAmount")));
                getSet.setTotalReceiveableAmount(cur.getDouble(cur.getColumnIndex("TotalReceiveableAmount")));
                getSet.setPaidAmount(cur.getDouble(cur.getColumnIndex("PaidAmount")));
                getSet.setSelected(false);
                list.add(getSet);
            }


        } catch (Exception e) {
            Log.d("--fillValue--", "Exception: " + e.getMessage());
            e.getMessage();
        }finally {
            databaseHelper.closeDatabase();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static String getNextOrderNo(Context context, String applyTax, String mode) {
        int NextOrderNo = 0;
        String billNo = "";
        try {

          /*  if (!db.isOpen()){
                Log.e("Check","isOpen: ");
                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            }*/

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
//            String qry1 = "Select MAX(IFNULL([OrderNo],0)) as [MaxOrderNo] from [InventoryOrderMaster];";

            String qry1 = "Select MAX(IFNULL([OrderNo],0)) as [MaxOrderNo] from " +
                    "[OrderSequence] where [TaxApplied] = "
                            .concat("'")
                            .concat(applyTax)
                            .concat("'");

            Cursor cur1 = db.rawQuery(qry1, null);
            Log.e("MaxOrderNoSdf", "cur1: " + cur1.getCount());
            Log.e("MaxOrderNoSdf:  ", "qry1: " + qry1);
            Log.e("MaxOrderNoSdf", "before IF: " + NextOrderNo);

//            if (cur1.getCount() == 0){
//                ClsInventoryOrderMaster.UpdateOrderSequence(context
//                        , String.valueOf(NextOrderNo),
//                        applyTax);
//            }


            if (cur1.getCount() == 1) {
                Log.e("MaxOrderNoSdf", "countIFFF: " + NextOrderNo);
                cur1.moveToFirst();
                NextOrderNo = cur1.getInt(cur1.getColumnIndex("MaxOrderNo"));
                Log.e("MaxOrderNoSdf", "countIFFF after: " + NextOrderNo);
            }

            Log.e("Check", "applyTax:- " + applyTax);

            //table  = Order Sequence  (add create script to login)
            //columns = OrderNo int, Type (TaxApplied = YES/NO)
            //String result = clsgGloble.GetFormattedOrderNo(NextOrderNo,"TaxApplied");


            ClsBillNoFormat clsBillNoFormat = getSettingApplyTaxBillNo(context, applyTax);


            // Check if current date and date in SharedPreferences is same.
            // if same than reset counter.
            if (clsBillNoFormat != null &&
                    !clsBillNoFormat.getReset_Counter_Name().equalsIgnoreCase("") &&
                    !clsBillNoFormat.getReset_Counter_Name().equalsIgnoreCase("Never")) {
                Log.e("Check", "clsBillNoFormat != null");

                if (!get_Current_Day_Month_Yearly(clsBillNoFormat.getReset_Counter_Name())
                        .equals(clsBillNoFormat.getResetCounter())) {
                    Log.e("Check", "inside reset counter:- ");

                    // if current date and date in SharedPreferences not same
                    // than save new date in SharedPreferences.

                    // save new date in SharedPreferences and reset counter.
                    set_reset_counter(get_Current_Day_Month_Yearly(clsBillNoFormat.getReset_Counter_Name()),
                            context, applyTax);

                    billNo = getNextFormattedOrderNo(Integer.parseInt(
                            clsBillNoFormat.getBill_Start_Form())
                            , applyTax, clsBillNoFormat, context);

                } else {

                    NextOrderNo += 1;

                    billNo = getNextFormattedOrderNo(NextOrderNo, applyTax, clsBillNoFormat, context);

                    Log.e("Check", "clsBillNoFormat != null else:- " + billNo);

//                    UpdateOrderSequence(context, String.valueOf(NextOrderNo), applyTax);
//                    //formateed order no
//                    //delete and insert
//
//                    Log.e("Check", String.valueOf(NextOrderNo));
                }

            } else {
                // when bill format is not set up.

                NextOrderNo += 1;

                billNo = getNextFormattedOrderNo(NextOrderNo, applyTax,
                        clsBillNoFormat, context);
                Log.e("Check", "billNo:- " + String.valueOf(NextOrderNo));
                Log.e("Check", "else:- " + billNo);

            }


            if (!mode.equalsIgnoreCase("SendMessage")) {
                Log.e("Check", "billNo:- " + billNo);

                UpdateOrderSequence(context, ClsGlobal.current_nextOrderNo, applyTax);
                //formateed order no
                //delete and insert

                Log.e("Check", String.valueOf(NextOrderNo));
            }

        } catch (Exception e) {
            Log.e("Check", "Exception getNextOrderNo: " + e.getMessage());
            e.getMessage();
        }


        return billNo;
    }

    @SuppressLint("WrongConstant")
    public static int UpdateOrderSequence(Context context, String orderNo, String applyTax) {
        int result = 0;

        Log.e("INSERT", "orderNo--->>" + orderNo);
        Log.e("INSERT", "UpdateOrderSequence--->>" + orderNo);
        try {
           /* if (!db.isOpen()){

                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            }*/
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


    @SuppressLint("WrongConstant")
    public static String setMaxOrderNo(Context context, String applyTax,io.requery.android
            .database.sqlite.SQLiteDatabase db) {
        int NextOrderNo = 0;
        try {

//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

//            String qry1 = "Select MAX(IFNULL([OrderNo],0)) as [MaxOrderNo] from [InventoryOrderMaster];";

            String qry1 = "Select MAX(IFNULL([OrderNo],0)) as [MaxOrderNo] from " +
                    "[InventoryOrderMaster] where [ApplyTax] = "
                            .concat("'")
                            .concat(applyTax)
                            .concat("'");

            Cursor cur1 = db.rawQuery(qry1, null);
            Log.e("MaxOrderNoSdf", "cur1: " + cur1.getCount());
            Log.e("MaxOrderNoSdf:  ", "qry1: " + qry1);
            Log.e("MaxOrderNoSdf", "before IF: " + NextOrderNo);

            if (cur1.getCount() == 1) {
                Log.e("MaxOrderNoSdf", "countIFFF: " + NextOrderNo);
                cur1.moveToFirst();
                NextOrderNo = cur1.getInt(cur1.getColumnIndex("MaxOrderNo"));
                Log.e("MaxOrderNoSdf", "countIFFF after: " + NextOrderNo);
            }

            if (NextOrderNo == 0) {
                qry1 = "Select MAX(IFNULL([OrderNo],0)) as [MaxOrderNo] from " +
                        "[OrderSequence] where [TaxApplied] = "
                                .concat("'")
                                .concat(applyTax)
                                .concat("'");

                cur1 = db.rawQuery(qry1, null);
                Log.e("MaxOrderNoSdf", "cur1: " + cur1.getCount());
                Log.e("MaxOrderNoSdf:  ", "qry1: " + qry1);
                Log.e("MaxOrderNoSdf", "before IF: " + NextOrderNo);

                if (cur1.getCount() == 1) {
                    Log.e("MaxOrderNoSdf", "countIFFF: " + NextOrderNo);
                    cur1.moveToFirst();
                    NextOrderNo = cur1.getInt(cur1.getColumnIndex("MaxOrderNo"));
                    Log.e("MaxOrderNoSdf", "countIFFF after: " + NextOrderNo);
                }
            }


            ClsInventoryOrderMaster.UpdateOrderSequence1(context
                    , String.valueOf(NextOrderNo),
                    applyTax,db);


            Log.e("MaxOrderNoSdf", "after IF: " + NextOrderNo);
        } catch (Exception e) {
            Log.e("Check", "Exception getNextOrderNo: " + e.getMessage());
            e.getMessage();
        }

        return String.valueOf(NextOrderNo);
    }


    @SuppressLint("WrongConstant")
    public static List<ClsInventoryOrderMaster> getList(String where, boolean setLimit, Context context) {
        List<ClsInventoryOrderMaster> list = new ArrayList<>();

        try
        {
//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            }
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            String qry = ("SELECT IOM.*,IFNULL(IOM.[SaleReturnDiscount],0) AS ReturnDiscount " +
                    ",IFNULL(IOM.[BillTo],'') AS BillToValue ," +
                    "CUST.[Address],CUST.[GST_NO],CUST.[Company_Name] FROM [InventoryOrderMaster] AS IOM ")
                    .concat(" LEFT JOIN [CustomerMaster] AS CUST ON CUST.[MOBILE_NO] = IOM.[MobileNo] ")
                    .concat(" WHERE 1=1 ")
                    .concat(where);

//            qry = qry.concat(" ORDER BY IOM.[BillDate] DESC ").concat(" LIMIT 3 ");


            if (setLimit) {
                qry = qry.concat(" ORDER BY IOM.[BillDate] DESC ").concat(" LIMIT 50 ");
            }

            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));
            Log.e("--Inventory--", qry);

            while (cur.moveToNext()) {
                ClsInventoryOrderMaster getSet = new ClsInventoryOrderMaster();

                getSet.setOrderID(cur.getInt(cur.getColumnIndex("OrderID")));
                getSet.setMobileNo(cur.getString(cur.getColumnIndex("MobileNo")));
                getSet.setCustomerName(cur.getString(cur.getColumnIndex("CustomerName")));
                getSet.setCustAddress(cur.getString(cur.getColumnIndex("Address")));
                getSet.setCompanyName(cur.getString(cur.getColumnIndex("Company_Name")));
                getSet.setGSTNo(cur.getString(cur.getColumnIndex("GST_NO")));
                getSet.setBillDate(cur.getString(cur.getColumnIndex("BillDate")));
                getSet.setDifferent_Amount_mode(cur.getString(cur.getColumnIndex("Different_Amount_mode")));
                getSet.setOrderNo(cur.getString(cur.getColumnIndex("OrderNo")));
                getSet.setTotalAmount(cur.getDouble(cur.getColumnIndex("TotalAmount")));
                getSet.setDiscountAmount(cur.getDouble(cur.getColumnIndex("DiscountAmount")));
                getSet.setTotalPaybleAmount(cur.getDouble(cur.getColumnIndex("TotalPaybleAmount")));
                getSet.setTotalTaxAmount(cur.getDouble(cur.getColumnIndex("TotalTaxAmount")));
                getSet.setTotalReceiveableAmount(cur.getDouble(cur.getColumnIndex("TotalReceiveableAmount")));
                getSet.setPaidAmount(cur.getDouble(cur.getColumnIndex("PaidAmount")));
                getSet.setTotalTaxAmount(cur.getDouble(cur.getColumnIndex("TotalTaxAmount")));
                getSet.setAdjumentAmount(cur.getDouble(cur.getColumnIndex("AdjumentAmount")));
                getSet.setEntryDate(cur.getString(cur.getColumnIndex("EntryDate")));
                getSet.setApplyTax(cur.getString(cur.getColumnIndex("ApplyTax")));
                getSet.setSaleType(cur.getString(cur.getColumnIndex("SaleType")));
                getSet.setPaymentMode(cur.getString(cur.getColumnIndex("PaymentMode")));
                getSet.setBillTo(cur.getString(cur.getColumnIndex("BillToValue")));
                getSet.setPaymentDetail(cur.getString(cur.getColumnIndex("PaymentDetail")));
                getSet.setSaleReturnDiscount(cur.getDouble(cur.getColumnIndex("ReturnDiscount")));
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
    public static ClsInventoryOrderMaster getOrderDetail(String where, Context context) {
        ClsInventoryOrderMaster getSet = new ClsInventoryOrderMaster();

        try
        {
//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            }
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            String qry = ("SELECT IOM.*,IFNULL(IOM.[SaleReturnDiscount],0) AS " +
                    "ReturnDiscount ,IFNULL(IOM.[BillTo],'') AS " +
                    "BillToValue ,CUST.[Address],CUST.[GST_NO],CUST.[Company_Name]" +
                    " FROM [InventoryOrderMaster] AS IOM ")
                    .concat(" LEFT JOIN [CustomerMaster] AS CUST ON CUST.[MOBILE_NO] = IOM.[MobileNo] ")
                    .concat(" WHERE 1=1 ")
                    .concat(where);

            Cursor cur = db.rawQuery(qry, null);

            Log.d("--fillValue--", "qry: " + qry);
            Log.d("--fillValue--", "count: " + cur.getCount());

            while (cur.moveToNext()) {

                getSet.setOrderID(cur.getInt(cur.getColumnIndex("OrderID")));
                getSet.setMobileNo(cur.getString(cur.getColumnIndex("MobileNo")));
                getSet.setCustomerName(cur.getString(cur.getColumnIndex("CustomerName")));
                getSet.setCustAddress(cur.getString(cur.getColumnIndex("Address")));
                getSet.setCompanyName(cur.getString(cur.getColumnIndex("Company_Name")));
                getSet.setGSTNo(cur.getString(cur.getColumnIndex("GST_NO")));
                getSet.setBillDate(cur.getString(cur.getColumnIndex("BillDate")));
                getSet.setDifferent_Amount_mode(cur.getString(cur.getColumnIndex("Different_Amount_mode")));
                getSet.setOrderNo(cur.getString(cur.getColumnIndex("OrderNo")));
                getSet.setTotalAmount(cur.getDouble(cur.getColumnIndex("TotalAmount")));
                getSet.setDiscountAmount(cur.getDouble(cur.getColumnIndex("DiscountAmount")));
                getSet.setTotalPaybleAmount(cur.getDouble(cur.getColumnIndex("TotalPaybleAmount")));
                getSet.setTotalTaxAmount(cur.getDouble(cur.getColumnIndex("TotalTaxAmount")));
                getSet.setTotalReceiveableAmount(cur.getDouble(cur.getColumnIndex("TotalReceiveableAmount")));
                getSet.setPaidAmount(cur.getDouble(cur.getColumnIndex("PaidAmount")));
                getSet.setTotalTaxAmount(cur.getDouble(cur.getColumnIndex("TotalTaxAmount")));
                getSet.setAdjumentAmount(cur.getDouble(cur.getColumnIndex("AdjumentAmount")));
                getSet.setEntryDate(cur.getString(cur.getColumnIndex("EntryDate")));
                getSet.setApplyTax(cur.getString(cur.getColumnIndex("ApplyTax")));
                getSet.setSaleType(cur.getString(cur.getColumnIndex("SaleType")));
                getSet.setPaymentMode(cur.getString(cur.getColumnIndex("PaymentMode")));
                getSet.setBillTo(cur.getString(cur.getColumnIndex("BillToValue")));
                getSet.setPaymentDetail(cur.getString(cur.getColumnIndex("PaymentDetail")));
                getSet.setSaleReturnDiscount(cur.getDouble(cur.getColumnIndex("ReturnDiscount")));
            }

            db.close();
        } catch (Exception e) {
            Log.d("--fillValue--", "Exception: " + e.getMessage());
            db.close();
            e.getMessage();
        }
        return getSet;
    }


    @SuppressLint("WrongConstant")
    public static ClsInventoryOrderMaster getOrder(String orderNo, String where, Context c, SQLiteDatabase db) {
        ClsInventoryOrderMaster getSet = new ClsInventoryOrderMaster();

        try {

//            if (!db.isOpen()){
//
//                db = c.openOrCreateDatabase(ClsGlobal.Database_Name, c.MODE_APPEND, null);
//            }
//
            db = c.openOrCreateDatabase(ClsGlobal.Database_Name, c.MODE_APPEND, null);
            String qry = "SELECT * FROM [InventoryOrderMaster] WHERE 1=1 AND [OrderNo] = "
                    .concat("'")
                    .concat(orderNo)
                    .concat("'")
                    .concat(where)
                    .concat(";");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--Invoice--Count-- ", String.valueOf(cur.getCount()));
            Log.e("--Invoice-- ", qry);

            while (cur.moveToNext()) {

                getSet.setOrderID(cur.getInt(cur.getColumnIndex("OrderID")));
                getSet.setMobileNo(cur.getString(cur.getColumnIndex("MobileNo")));
                getSet.setCustomerName(cur.getString(cur.getColumnIndex("CustomerName")));
                getSet.setCompanyName(cur.getString(cur.getColumnIndex("CompanyName")));
                getSet.setGSTNo(cur.getString(cur.getColumnIndex("GSTNo")));
                getSet.setBillDate(cur.getString(cur.getColumnIndex("BillDate")));
                getSet.setDifferent_Amount_mode(cur.getString(cur.getColumnIndex("Different_Amount_mode")));
                getSet.setBillTo(cur.getString(cur.getColumnIndex("BillTo")));
                getSet.setOrderNo(cur.getString(cur.getColumnIndex("OrderNo")));
                getSet.setTotalAmount(cur.getDouble(cur.getColumnIndex("TotalAmount")));
                getSet.setDiscountAmount(cur.getDouble(cur.getColumnIndex("DiscountAmount")));
                getSet.setTotalPaybleAmount(cur.getDouble(cur.getColumnIndex("TotalPaybleAmount")));
                getSet.setTotalTaxAmount(cur.getDouble(cur.getColumnIndex("TotalTaxAmount")));
                getSet.setTotalReceiveableAmount(cur.getDouble(cur.getColumnIndex("TotalReceiveableAmount")));
                getSet.setPaidAmount(cur.getDouble(cur.getColumnIndex("PaidAmount")));
                getSet.setTotalTaxAmount(cur.getDouble(cur.getColumnIndex("TotalTaxAmount")));
                getSet.setAdjumentAmount(cur.getDouble(cur.getColumnIndex("AdjumentAmount")));
                getSet.setEntryDate(cur.getString(cur.getColumnIndex("EntryDate")));
                getSet.setApplyTax(cur.getString(cur.getColumnIndex("ApplyTax")));
                getSet.setPaymentMode(cur.getString(cur.getColumnIndex("PaymentMode")));
                getSet.setPaymentDetail(cur.getString(cur.getColumnIndex("PaymentDetail")));
                getSet.setSms_Limit(cur.getInt(cur.getColumnIndex("Sms_Limit")));

            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return getSet;
    }


    @SuppressLint("WrongConstant")
    public static List<String> getPaymentDetailSuggestion(Context c) {
        List<String> list = new ArrayList<>();
        try {
//            if (!db.isOpen()){
//
//                db = c.openOrCreateDatabase(ClsGlobal.Database_Name, c.MODE_APPEND, null);
//            }

            db = c.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            Cursor cur = db.rawQuery("SELECT DISTINCT [PaymentDetail] FROM [InventoryOrderMaster] WHERE 1=1 AND [PaymentDetail] <> ''".concat(" ORDER BY [PaymentDetail];"), null);
            while (cur.moveToNext()) {
                list.add(cur.getString(cur.getColumnIndex("PaymentDetail")));
            }
        } catch (Exception e) {
            Log.e("jsonObject", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsInventoryOrderMaster> getMonthWiseOrdersList(String where, Context context) {
        List<ClsInventoryOrderMaster> list = new ArrayList<>();

        try {

//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            }

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String qry = "SELECT strftime('%m', [BillDate]) as [Month], strftime('%Y', [BillDate]) as [Year]  , SUM(TotalReceiveableAmount) AS [TotalReceiveableAmount] FROM [InventoryOrderMaster] "
                    .concat(" WHERE 1=1 ")
                    .concat(where)
                    .concat(" GROUP BY strftime('%m', [BillDate]), strftime('%Y', [BillDate]) ")
                    .concat("ORDER BY strftime('%Y', [BillDate]) DESC, strftime('%m', [BillDate]) DESC");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));
            Log.e("qry", qry);

            while (cur.moveToNext()) {

                ClsInventoryOrderMaster getSet = new ClsInventoryOrderMaster();

//
//                Log.e("--Order--", "Month: " +
//                        cur.getString(cur.getColumnIndex("Month")));
//                Log.e("--Order--", "Year: " +
//                        cur.getString(cur.getColumnIndex("Year")));
//                Log.e("--Order--", "Total_Receivable_Amount: " +
//                        cur.getDouble(cur.getColumnIndex("TotalReceiveableAmount")));


                if (cur.getString(cur.getColumnIndex("Month")) != null &&
                        cur.getString(cur.getColumnIndex("Month")) != "" &&
                        cur.getString(cur.getColumnIndex("Year")) != null &&
                        cur.getString(cur.getColumnIndex("Year")) != ""
                        && cur.getDouble(cur.getColumnIndex("TotalReceiveableAmount")) != 0.0) {

                    getSet.setMounth(Integer.parseInt(cur.getString(cur.getColumnIndex("Month"))));
                    getSet.setYear(Integer.parseInt(cur.getString(cur.getColumnIndex("Year"))));
                    getSet.setTotalReceiveableAmount(cur.getDouble(cur.getColumnIndex("TotalReceiveableAmount")));

                    Gson gson = new Gson();
                    String jsonInString = gson.toJson(getSet);
                    Log.d("--GSON--", "getSet:  " + jsonInString);

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
    public static Double getPurchaseAvgValue(String where, Context context) {
        Double result = 0.0;

        try {

//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            }

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            String qry = "SELECT IFNULL(SUM([Rate]),0) AS [TotalAmount], COUNT(1) AS [TotalCount] ,[ItemCode] FROM [PurchaseDetail] ".concat(where)
                    .concat(" GROUP BY [ItemCode] ");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--cur--", String.valueOf(cur.getCount()));
            Log.e("qry", qry);

            while (cur.moveToNext()) {

                result = result + (cur.getDouble(cur.getColumnIndex("TotalAmount")) / cur.getInt(cur.getColumnIndex("TotalCount")));
            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return result;
    }


    @SuppressLint("WrongConstant")
    public static Double getTotalPurchaseAmt(String where, Context context) {
        Double result = 0.0;

        try {

//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            }

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

//            String qry = "SELECT IFNULL(SUM([Rate]),0) AS [TotalAmount], COUNT(1) AS [TotalCount] ,[ItemCode] FROM [PurchaseDetail] ".concat(where)
//                    .concat(" GROUP BY [ItemCode] ");
            String qry = "SELECT IFNULL(SUM([TotalAmount]),0) AS [TotalAmount] , [Quantity] FROM [PurchaseDetail] ".concat(where);


            Cursor cur = db.rawQuery(qry, null);

            Log.e("--cur--", String.valueOf(cur.getCount()));
            Log.e("qry", qry);

            while (cur.moveToNext()) {
//             result = cur.getDouble(cur.getColumnIndex("TotalAmount"))*cur.getDouble(cur.getColumnIndex("Quantity"));
                result = cur.getDouble(cur.getColumnIndex("TotalAmount"));
            }


        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return result;
    }


    @SuppressLint("WrongConstant")
    public static int getNextOrderNo(Context context) {
        int NextOrderNo = 0;
        try {

//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            }


            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            String qry = "Select MAX([OrderID]) as [MaxOrderID] from [InventoryOrderMaster];";

            String qry1 = "Select MAX(IFNULL([OrderNo],0)) as [MaxOrderNo] from [InventoryOrderMaster];";


            Cursor cur1 = db.rawQuery(qry1, null);

            Log.e("MaxOrderNoSdf", "cur1: " + cur1.getCount());

            Log.e("MaxOrderNoSdf:  ", "qry1: " + qry1);
            Log.e("MaxOrderNoSdf", "before IF: " + NextOrderNo);
            if (cur1.getCount() == 1) {
                Log.e("MaxOrderNoSdf", "countIFFF: " + NextOrderNo);
                cur1.moveToFirst();
                NextOrderNo = cur1.getInt(cur1.getColumnIndex("MaxOrderNo"));
                Log.e("MaxOrderNoSdf", "countIFFF after: " + NextOrderNo);

            }
//            while (cur1.moveToNext()) {
//
//                Log.e("MaxOrderNoSdf", "MaxOrderNo: " + NextOrderNo);
//                Log.e("MaxOrderNoSdf", "MaxOrderNo: " +
//                        cur1.getInt(cur1.getColumnIndex("MaxOrderNo")));
//            }
            Log.e("MaxOrderNoSdf", String.valueOf(NextOrderNo));
        } catch (Exception e) {
            Log.e("MaxOrderNoSdf", "GetList" + e.getMessage());
            e.getMessage();
        }


        return NextOrderNo + 1;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsInventoryOrderMaster> getCustomerSalesList(String where, Context context) {
        List<ClsInventoryOrderMaster> list = new ArrayList<>();

        try {
//
//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            }


            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String qry = ("SELECT  ORD.[BillDate], ORD.[OrderNo], ORD.[TotalReceiveableAmount] AS [BillAmount]," +
                    " ORD.[PaymentMode], ORD.[PaymentDetail],ORD.[BillTo], ORD.[SaleType] FROM [InventoryOrderMaster] AS ORD ")
                    .concat(" WHERE 1=1 ")
                    .concat(where) // " AND ORD.[MobileNo] = '9586050796' "
                    .concat(" ORDER BY ORD.[BillDate]");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));
            Log.e("qry", qry);

            while (cur.moveToNext()) {
                ClsInventoryOrderMaster getSet = new ClsInventoryOrderMaster();
                getSet.setBillDate(cur.getString(cur.getColumnIndex("BillDate")));
                getSet.setOrderNo(cur.getString(cur.getColumnIndex("OrderNo")));
                getSet.setTotalReceiveableAmount(cur.getDouble(cur.getColumnIndex("BillAmount")));
                getSet.setPaymentMode(cur.getString(cur.getColumnIndex("PaymentMode")));
                getSet.setPaymentDetail(cur.getString(cur.getColumnIndex("PaymentDetail")));
                getSet.setBillTo(cur.getString(cur.getColumnIndex("BillTo")));
                getSet.setSaleType(cur.getString(cur.getColumnIndex("SaleType")));
                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsCustomerWisePayment> getCustomerWiseSales(SQLiteDatabase db,String where, Context context) {
        List<ClsCustomerWisePayment> list = new ArrayList<>();
        try {
//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            }

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String qry = ("SELECT [BillDate], [OrderNo], [TotalReceiveableAmount] AS [BillAmount]," +
                    " [PaymentMode],[PaymentDetail],[BillTo],[SaleType] FROM [InventoryOrderMaster]")
                    .concat(" WHERE 1=1 ")
                    .concat(where) // " AND ORD.[MobileNo] = '9586050796' "
                    .concat(" ORDER BY [BillDate] DESC");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));
            Log.e("qry", qry);

            while (cur.moveToNext()) {

                ClsCustomerWisePayment clsCustomerWisePayment = new ClsCustomerWisePayment();
                clsCustomerWisePayment.setPaymentDate(cur.getString(cur.getColumnIndex("BillDate")));
                clsCustomerWisePayment.setReceiptNo(cur.getString(cur.getColumnIndex("OrderNo")));
                clsCustomerWisePayment.setAmount(cur.getDouble(cur.getColumnIndex("BillAmount")));
                clsCustomerWisePayment.setPaymentMode(cur.getString(cur.getColumnIndex("PaymentMode")));
                clsCustomerWisePayment.setPaymentDetail(cur.getString(cur.getColumnIndex("PaymentDetail")));
                clsCustomerWisePayment.setBillTo(cur.getString(cur.getColumnIndex("BillTo")));
                clsCustomerWisePayment.setSaleType(cur.getString(cur.getColumnIndex("SaleType")));
                list.add(clsCustomerWisePayment);
            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static int getSmsLimit(String where, Context context, SQLiteDatabase db) {

        int SmsLimit = 0;
        try {

//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String qry = ("SELECT Sms_Limit FROM [InventoryOrderMaster] ")
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
    public static int Update(int OrderId, String OrderNo, int limit, Context context) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String strSql = "UPDATE [InventoryOrderMaster] SET "

                    .concat(" [Sms_Limit] = ")
                    .concat(String.valueOf(limit))


                    .concat(" WHERE [OrderID] = ")
                    .concat(String.valueOf(OrderId))

                    .concat(" AND [OrderNo] = ")
                    .concat("'")
                    .concat(OrderNo)
                    .concat("'")
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
    public static int Delete_OrderDetail_master(String OrderNo, String orderID, Context context) {
        int result = 0;
        try {
//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            }


//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String strSql = "DELETE FROM [InventoryOrderMaster] WHERE [OrderNo] = '"
                    .concat(String.valueOf(OrderNo))
                    .concat("'")
                    .concat(" AND [OrderID] = " + orderID)
                    .concat(";");
            Log.e("--PAYMENT_DELETE--", "OrderDetail_strSql: " + strSql);
            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();
            Log.e("--PAYMENT_DELETE--", "OrderDetail_Result: " + result);
            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsOrderDetailMaster", "Delete" + e.getMessage());
            e.getMessage();
        }
        return result;
    }


    @SuppressLint("WrongConstant")
    public static int deletePendingSaveItems(Context context) {
        int result = 0;
        try
        {

//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            }

//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            // Delete Pending Entry save..
            String deleteQry = "DELETE FROM [InventoryOrderDetail] WHERE 1=1 "
                    .concat(" AND IFNULL([SaveStatus],'') IN ('NO') ");

            Log.e("DELETE", "DELqRY-" + deleteQry);
            SQLiteStatement statement = db.compileStatement(deleteQry);
            result = statement.executeUpdateDelete();
            Log.e("--updateStatus--", "PendingResultNO: " + result);

            deleteQry = "DELETE FROM [InventoryOrderDetail] WHERE 1=1 "
                    .concat(" AND IFNULL([SaveStatus],'') IN ('EDT') ");

            Log.e("DELETE", "DELqRY-" + deleteQry);
             statement = db.compileStatement(deleteQry);
            result = statement.executeUpdateDelete();
            Log.e("--updateStatus--", "PendingResultEDT: " + result);

            // Restore Deleted Item on cancel Update.....
            String strSql = "UPDATE [InventoryOrderDetail] SET "
                    .concat(" [SaveStatus] = NULL WHERE 1= 1 ")
                    .concat(" AND IFNULL([SaveStatus],'') = 'DEL' ");

            Log.e("--updateStatus--", "PendingUpdate: " + strSql);
            statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();
            Log.e("--updateStatus--", "PendingResult: " + result);

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsOrderDetailMaster", "Delete" + e.getMessage());
            e.getMessage();
        }
        return result;

    }


    @SuppressLint("WrongConstant")
    public static List<String> getAddInventoryOrderMasterColumn(Context context,
                                                                io.requery.android.database.sqlite.SQLiteDatabase db) {
        List<String> columns = new ArrayList<>();
        try {

//            io.requery.android.database.sqlite.SQLiteDatabase db;
//
//            db = io.requery.android.database.sqlite.SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath(ClsGlobal.Database_Name).getPath(),
//                    null);

            String qry = "SELECT * FROM [InventoryOrderMaster] LIMIT 1 ";

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
    public static List<String> getInventoryOrderMasterColumns(Context context,
                                                              io.requery.android.database.sqlite.SQLiteDatabase db) {
        List<String> columns = new ArrayList<>();
        try {

//            io.requery.android.database.sqlite.SQLiteDatabase db;
//
//            db = io.requery.android.database.sqlite.SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath(ClsGlobal.Database_Name).getPath(),
//                    null);

            String qry = "SELECT * FROM [InventoryOrderMaster] LIMIT 1 ";

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


    @SuppressLint({"WrongConstant", "DefaultLocale"})
    public static String getTemporaryOrderNo(String mode) {
        String generatedOrderNo = "";

        Random random = new Random();

        generatedOrderNo = String.format("%04d", random.nextInt(10000 - 2034) + 2034);
        Log.e("getTemporaryOrderNo", "getTemporaryOrderNo First:- " + generatedOrderNo);

        while (ClsGlobal._OrderNo.equals(ClsGlobal._WholeSaleOrderNo)
                && generatedOrderNo.substring(0, 1).equals("0")) {
            generatedOrderNo = String.format("%04d", random.nextInt(10000 - 2034) + 2034);
            Log.e("getTemporaryOrderNo", "getTemporaryOrderNo while:- " + generatedOrderNo);

            if (mode.equalsIgnoreCase("WholeSaleOrderNo")) {
                ClsGlobal._WholeSaleOrderNo = generatedOrderNo;
            } else if (mode.equalsIgnoreCase("OrderNo")) {
                ClsGlobal._OrderNo = generatedOrderNo;
            }
        }


        return generatedOrderNo;
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


    @SuppressLint("WrongConstant")
    public static List<ClsInventoryOrderMaster> getCustomerListFromDate(String _where, Context context) {
        List<ClsInventoryOrderMaster> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);


            String qry = "SELECT [CustomerName], [MobileNo] FROM InventoryOrderMaster "
                    .concat("WHERE 1=1 ")
                    .concat(_where)
                    .concat("GROUP BY [MobileNo]");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--qry--", "qry: " + qry);
            Log.e("--qry--", "Count: " + cur.getCount());

            while (cur.moveToNext()) {
                ClsInventoryOrderMaster getSet = new ClsInventoryOrderMaster();
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
    public static int UpdateOrderSequence1(Context context,
                                           String orderNo,
                                           String applyTax,
                                           io.requery.android.database.sqlite.SQLiteDatabase db) {
        int result = 0;

        Log.e("INSERT", "orderNo--->>" + orderNo);
        Log.e("INSERT", "UpdateOrderSequence--->>" + orderNo);
        try {
//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            // Delete By OrderSequence.
            String deleteQry = "Delete from [OrderSequence] where TaxApplied = "
                    .concat("'")
                    .concat(applyTax)
                    .concat("'");

            io.requery.android.database.sqlite.SQLiteStatement
                    delete = db.compileStatement(deleteQry);
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

            io.requery.android.database.sqlite.SQLiteStatement statement
                    = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.e("Check", "INSERT INTO result: " + result);
            Log.e("INSERT", "ExpenseType--->>" + qry);


        } catch (Exception e) {
            Log.e("ClsInventoryOrderMaster", "Insert>>>>>>>>" + e.getMessage());
            e.getMessage();
        }


        return result;
    }

}
