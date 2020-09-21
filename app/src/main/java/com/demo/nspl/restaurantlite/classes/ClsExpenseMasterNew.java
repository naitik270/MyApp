package com.demo.nspl.restaurantlite.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desktop on 5/19/2018.
 */

public class ClsExpenseMasterNew {
    int expense_id;
    int vendor_id = 0;


    int expense_type_id;
    List<ClsInventoryStock> list_items = new ArrayList<>();

    public List<ClsInventoryStock> getList_items() {
        return list_items;
    }

    public void setList_items(List<ClsInventoryStock> list_items) {
        this.list_items = list_items;
    }

    String vendor_name;
    String employee_name;
    String receipt_no = "";
    String receipt_date = "";
    Double amount = 0.0;

    String other_tax1 = "";
    Double other_val1 = 0.0;
    String other_tax2 = "";
    Double other_val2 = 0.0;
    String other_tax3 = "";
    Double other_val3 = 0.0;
    String other_tax4 = "";
    Double other_val4 = 0.0;
    String other_tax5 = "";
    Double other_val5 = 0.0;

    Double discount = 0.0;
    Double GRAND_TOTAL = 0.0;
    int TOTAL_COUNT = 0;

    public Double get_finalAmount() {
        return _finalAmount;
    }

    public void set_finalAmount(Double _finalAmount) {
        this._finalAmount = _finalAmount;
    }

    Double _finalAmount = 0.0;
    String entry_date;

    String remark;

    public int getTOTAL_COUNT() {
        return TOTAL_COUNT;
    }

    public void setTOTAL_COUNT(int TOTAL_COUNT) {
        this.TOTAL_COUNT = TOTAL_COUNT;
    }

    public String getMonthUniqueIndex() {
        return monthUniqueIndex;
    }

    public void setMonthUniqueIndex(String monthUniqueIndex) {
        this.monthUniqueIndex = monthUniqueIndex;
    }

    String monthUniqueIndex;
    String expense_type_name;

    public String getExpense_type_name() {
        return expense_type_name;
    }

    public void setExpense_type_name(String expense_type_name) {
        this.expense_type_name = expense_type_name;
    }


    public int getExpense_id() {
        return expense_id;
    }

    public void setExpense_id(int expense_id) {
        this.expense_id = expense_id;
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

    public int getExpense_type_id() {
        return expense_type_id;
    }

    public void setExpense_type_id(int expense_type_id) {
        this.expense_type_id = expense_type_id;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getReceipt_no() {
        return receipt_no;
    }

    public void setReceipt_no(String receipt_no) {
        this.receipt_no = receipt_no;
    }

    public String getReceipt_date() {
        return receipt_date;
    }

    public void setReceipt_date(String receipt_date) {
        this.receipt_date = receipt_date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getOther_tax1() {
        return other_tax1;
    }

    public void setOther_tax1(String other_tax1) {
        this.other_tax1 = other_tax1;
    }

    public Double getOther_val1() {
        return other_val1;
    }

    public void setOther_val1(Double other_val1) {
        this.other_val1 = other_val1;
    }

    public String getOther_tax2() {
        return other_tax2;
    }

    public void setOther_tax2(String other_tax2) {
        this.other_tax2 = other_tax2;
    }

    public Double getOther_val2() {
        return other_val2;
    }

    public void setOther_val2(Double other_val2) {
        this.other_val2 = other_val2;
    }

    public String getOther_tax3() {
        return other_tax3;
    }

    public void setOther_tax3(String other_tax3) {
        this.other_tax3 = other_tax3;
    }

    public Double getOther_val3() {
        return other_val3;
    }

    public void setOther_val3(Double other_val3) {
        this.other_val3 = other_val3;
    }

    public String getOther_tax4() {
        return other_tax4;
    }

    public void setOther_tax4(String other_tax4) {
        this.other_tax4 = other_tax4;
    }

    public Double getOther_val4() {
        return other_val4;
    }

    public void setOther_val4(Double other_val4) {
        this.other_val4 = other_val4;
    }

    public String getOther_tax5() {
        return other_tax5;
    }

    public void setOther_tax5(String other_tax5) {
        this.other_tax5 = other_tax5;
    }

    public Double getOther_val5() {
        return other_val5;
    }

    public void setOther_val5(Double other_val5) {
        this.other_val5 = other_val5;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getGRAND_TOTAL() {
        return GRAND_TOTAL;
    }

    public void setGRAND_TOTAL(Double GRAND_TOTAL) {
        this.GRAND_TOTAL = GRAND_TOTAL;
    }

    public String getEntry_date() {
        return entry_date;
    }

    public void setEntry_date(String entry_date) {
        this.entry_date = entry_date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    private static int result;
    static Context context;
    static SQLiteDatabase db;

    public ClsExpenseMasterNew(Context ctx) {
        context = ctx;
    }

    public ClsExpenseMasterNew() {
    }


    @SuppressLint("WrongConstant")
    public static String getExpenseMIS(String _where) {
        Log.e("getExpenseMIS","getExpenseMIS call");
        List<ClsExpenseMasterNew> list = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

/*
            String qry = "SELECT ifnull(MAX(CHECK_OUT_NO), 0) AS [CHECK_OUT_NO]"
                    .concat(" FROM [CheckOutMaster]");*/

            String qry = "SELECT "
                    .concat("etm.[EXPENSE_TYPE_NAME]")
                    .concat(",COUNT(1) AS [TOTAL_COUNT]")
                    .concat(",SUM(em.[AMOUNT]) AS [AMOUNT]")
                    .concat(",SUM(em.[DISCOUNT]) AS [DISCOUNT]")
                    .concat(",em.[OTHER_TAX1]")
                    .concat(",SUM(em.[OTHER_VAL1]) AS [OTHER_VAL1]")
                    .concat(",em.[OTHER_TAX2]")
                    .concat(",SUM(em.[OTHER_VAL2]) AS [OTHER_VAL2]")
                    .concat(",em.[OTHER_TAX3]")
                    .concat(",SUM(em.[OTHER_VAL3]) AS [OTHER_VAL3]")
                    .concat(",em.[OTHER_TAX4]")
                    .concat(",SUM(em.[OTHER_VAL4]) AS [OTHER_VAL4]")
                    .concat(",em.[OTHER_TAX5]")
                    .concat(",SUM(em.[OTHER_VAL5]) AS [OTHER_VAL5]")
                    .concat(",SUM(em.[GRAND_TOTAL]) AS [GRAND_TOTAL]")
                    .concat(" FROM [ExpenseMasterNew] AS em")
                    .concat(" INNER JOIN [EXPENSE_TYPE_MASTER] AS etm on etm.EXPENSE_TYPE_ID = em.EXPENSE_TYPE_ID ")
                    .concat(" WHERE 1=1 ")
                    .concat(_where)
                    .concat(" GROUP BY etm.[EXPENSE_TYPE_NAME],em.[OTHER_TAX1],em.[OTHER_TAX2],em.[OTHER_TAX3],em.[OTHER_TAX4],em.[OTHER_TAX5]")
                    .concat(" ORDER BY etm.[EXPENSE_TYPE_NAME]");


            Cursor cur = db.rawQuery(qry, null);

            Log.d("getListAllExp", "getListAllExp-- " + qry);
            Log.d("cur123", "count_getAllExp:-- " + cur.getCount());
            Log.d("cur12312", "getColumnCount:-- " + cur.getColumnCount());

            int sr = 0;

            while (cur.moveToNext()) {
                ClsExpenseMasterNew getSet = new ClsExpenseMasterNew();
                getSet.setTOTAL_COUNT(cur.getInt(cur.getColumnIndex("TOTAL_COUNT")));
                getSet.setExpense_type_name(cur.getString(cur.getColumnIndex("EXPENSE_TYPE_NAME")));
                getSet.setAmount(Double.valueOf(cur.getString(cur.getColumnIndex("AMOUNT"))));
                getSet.setDiscount(Double.valueOf(cur.getString(cur.getColumnIndex("DISCOUNT"))));
                getSet.setOther_tax1(cur.getString(cur.getColumnIndex("OTHER_TAX1")));

                getSet.setOther_val1(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL1"))));
                getSet.setOther_tax2(cur.getString(cur.getColumnIndex("OTHER_TAX2")));
                getSet.setOther_val2(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL2"))));
                getSet.setOther_tax3(cur.getString(cur.getColumnIndex("OTHER_TAX3")));
                getSet.setOther_val3(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL3"))));
                getSet.setOther_tax4(cur.getString(cur.getColumnIndex("OTHER_TAX4")));
                getSet.setOther_val4(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL4"))));
                getSet.setOther_tax5(cur.getString(cur.getColumnIndex("OTHER_TAX5")));
                getSet.setOther_val5(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL5"))));
                getSet.setGRAND_TOTAL(Double.valueOf(cur.getString(cur.getColumnIndex("GRAND_TOTAL"))));
                list.add(getSet);
            }

            if (list != null && list.size() !=0){
                stringBuilder.append("<table class=\"table\">");
                stringBuilder.append("<thead>");

                stringBuilder.append("<tr>");

                stringBuilder.append("<th style=\"white-space: normal !important;\">Sr#</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Item Name</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Total Count</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Amount</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Discount</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Total Amount</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Tax1</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Tax1 val</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Tax2</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Tax2 va2</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Tax3</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Tax3 val3</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Tax4</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Tax4 val4</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Tax5</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Tax5 val5</th>");
                stringBuilder.append("<th style=\"white-space: normal !important;\">Grand Total</th>");
                stringBuilder.append("</tr>");
                stringBuilder.append("</thead>");

                int totalCount = 0;
                Double grandTotal = 0.0;
                Double totalAmount = 0.0;
                Double totalDiscount = 0.0;
                Double amountTotal = 0.0;
                stringBuilder.append("<tbody>");//new ROW st

                for (ClsExpenseMasterNew current : list) {

                    sr++;

                    stringBuilder.append("<tr>");//new ROW start
                    //1st cell
                    stringBuilder.append("<td align = \"center\">");//cell start
                    stringBuilder.append(sr + ".");//cell value
                    stringBuilder.append("</td>");//cell end

                    //2nd cell
                    stringBuilder.append("<td>");//cell start
                    stringBuilder.append(current.getExpense_type_name());//cell value
                    stringBuilder.append("</td>");//cell end

                    //3rd cell
                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append(current.getTOTAL_COUNT());//cell value
                    stringBuilder.append("</td>");//cell end
                    //cell 4
                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append(ClsGlobal.round(current.getAmount(), 2));//cell value
                    stringBuilder.append("</td>");//cell end
                    //cell 5
                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append(ClsGlobal.round(current.getDiscount(), 2));//cell value
                    stringBuilder.append("</td>");//cell end


                    Double _TotalAmt = current.getAmount() - current.getDiscount();
                    //cell 6
                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append(ClsGlobal.round(_TotalAmt, 2));//cell value
                    stringBuilder.append("</td>");//cell end


                    //7nd cell
                    stringBuilder.append("<td>");//cell start
                    stringBuilder.append(current.getOther_tax1());//cell value
                    stringBuilder.append("</td>");//cell end
                    //8nd cell
                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append((String.valueOf(current.getOther_val1())));//cell value
                    stringBuilder.append("</td>");//cell end

                    //9nd cell
                    stringBuilder.append("<td>");//cell start
                    stringBuilder.append(current.getOther_tax2());//cell value
                    stringBuilder.append("</td>");//cell end
                    //10nd cell
                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append((String.valueOf(current.getOther_val2())));//cell value
                    stringBuilder.append("</td>");//cell end

                    //11nd cell
                    stringBuilder.append("<td>");//cell start
                    stringBuilder.append(current.getOther_tax3());//cell value
                    stringBuilder.append("</td>");//cell end
                    //12nd cell
                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append((String.valueOf(current.getOther_val3())));//cell value
                    stringBuilder.append("</td>");//cell end

                    //13nd cell
                    stringBuilder.append("<td>");//cell start
                    stringBuilder.append(current.getOther_tax4());//cell value
                    stringBuilder.append("</td>");//cell end
                    //14nd cell
                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append((String.valueOf(current.getOther_val4())));//cell value
                    stringBuilder.append("</td>");//cell end

                    //15nd cell
                    stringBuilder.append("<td>");//cell start
                    stringBuilder.append(current.getOther_tax5());//cell value
                    stringBuilder.append("</td>");//cell end
                    //16nd cell
                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append((String.valueOf(current.getOther_val5())));//cell value
                    stringBuilder.append("</td>");//cell end

                    //176nd cell
                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append(current.getGRAND_TOTAL());//cell value
                    stringBuilder.append("</td>");//cell end
                    stringBuilder.append("</tr>");

                    totalCount += current.getTOTAL_COUNT();
                    totalAmount += current.getAmount();
                    totalDiscount += current.getDiscount();
                    amountTotal += _TotalAmt;
                    grandTotal += current.getGRAND_TOTAL();

                }

                stringBuilder.append("</tbody>");
                stringBuilder.append("<tfoot>");//start footer row
                //  cell 6
                stringBuilder.append("<tr>");//start footer row
                stringBuilder.append("<td>");//empty cell for sr no
                stringBuilder.append("</td>");//empty cell for sr no

                stringBuilder.append("<td>TOTAL</td>");//empty cell for sr no
                stringBuilder.append("<td align = \"right\">");//cell start
                stringBuilder.append(totalCount);//cell value
                stringBuilder.append("</td>");//cell end

                stringBuilder.append("<td align = \"right\">");//cell start
                stringBuilder.append(ClsGlobal.round(totalAmount, 2));//cell value
                stringBuilder.append("</td>");//cell end

                stringBuilder.append("<td align = \"right\">");//cell start
                stringBuilder.append(ClsGlobal.round(totalDiscount, 2));//cell value
                stringBuilder.append("</td>");//cell end

                stringBuilder.append("<td align = \"right\">");//cell start
                stringBuilder.append(ClsGlobal.round(amountTotal, 2));//cell value
                stringBuilder.append("</td>");//cell end


                for (int i = 1; i <= 10; i++) {
                    stringBuilder.append("<td>");//empty cell for sr no
                    stringBuilder.append("</td>");//empty cell for sr no
                }

                stringBuilder.append("<td align = \"right\">");//cell start
                stringBuilder.append(ClsGlobal.round(grandTotal, 2));//cell value
                stringBuilder.append("</td>");//cell end


                stringBuilder.append("</td>");//cell end

                stringBuilder.append("</tr>");//footer row end
                stringBuilder.append("</tfoot>");//start footer
                stringBuilder.append("</table>");

                Log.e("ClsOrderDetails1", "Qry--->>result = " + stringBuilder.toString());

            }else {
                stringBuilder.append("<table class=\"table\">\n" +
                        "<td align=\"left\"><span style=\"color:red;\">No records found!</span></td>\n" +
                        "</table>");
            }


        } catch (Exception e) {
            Log.e("ClsExpenseType", "getListAllExp" + e.getMessage());
            e.getMessage();
        }
        return stringBuilder.toString();
    }


    public static ClsExpenseMasterNew Insert(ClsExpenseMasterNew ObjParams) {
        ClsExpenseMasterNew ObjResult = new ClsExpenseMasterNew();
        result = 0;
        try {
            result = 1;

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_PRIVATE, null);

            String qry = ("INSERT INTO [ExpenseMasterNew] ([VENDOR_ID]" +
                    ",[EXPENSE_TYPE_ID],[VENDOR_NAME],[EMPLOYEE_NAME]" +
                    ",[RECEIPT_NO],[RECEIPT_DATE],[AMOUNT],[OTHER_TAX1]," +
                    "[OTHER_VAL1],[OTHER_TAX2],[OTHER_VAL2],[OTHER_TAX3]," +
                    "[OTHER_VAL3],[OTHER_TAX4],[OTHER_VAL4],[OTHER_TAX5]," +
                    "[OTHER_VAL5],[DISCOUNT],[GRAND_TOTAL],[ENTRY_DATE],[REMARK]) VALUES (")
                    .concat(String.valueOf(ObjParams.getVendor_id()))

                    .concat(",")
                    .concat(String.valueOf(ObjParams.getExpense_type_id()))

                    .concat(",")
                    .concat("'")
                    .concat(ObjParams.getVendor_name().trim()
                            .replace("'","''"))
                    .concat("'")

                    .concat(",")
                    .concat("'")
                    .concat(ObjParams.getEmployee_name().trim()
                            .replace("'","''"))
                    .concat("'")

                    .concat(",")
                    .concat("'")
                    .concat(ObjParams.getReceipt_no())
                    .concat("'")

                    .concat(",")
                    .concat("'")
                    //.concat("2012-09-23")
                    .concat(ClsGlobal.getDbDateFormat(ObjParams.getReceipt_date()))
                    .concat("'")

                    .concat(",")
                    .concat(String.valueOf(ObjParams.getAmount()))

                    .concat(",")
                    .concat("'")
                    .concat(ObjParams.getOther_tax1())
                    .concat("'")

                    .concat(",")
                    .concat(String.valueOf(ObjParams.getOther_val1()))

                    .concat(",")
                    .concat("'")
                    .concat(ObjParams.getOther_tax2())
                    .concat("'")

                    .concat(",")
                    .concat(String.valueOf(ObjParams.getOther_val2()))

                    .concat(",")
                    .concat("'")
                    .concat(ObjParams.getOther_tax3())
                    .concat("'")

                    .concat(",")
                    .concat(String.valueOf(ObjParams.getOther_val3()))

                    .concat(",")
                    .concat("'")
                    .concat(ObjParams.getOther_tax4())
                    .concat("'")

                    .concat(",")
                    .concat(String.valueOf(ObjParams.getOther_val4()))

                    .concat(",")
                    .concat("'")
                    .concat(ObjParams.getOther_tax5())
                    .concat("'")

                    .concat(",")
                    .concat(String.valueOf(ObjParams.getOther_val5()))

                    .concat(",")
                    .concat(String.valueOf(ObjParams.getDiscount()))


                    .concat(",")
                    .concat(String.valueOf(ObjParams.getGRAND_TOTAL()))


                    .concat(",")
                    .concat("'")
                    .concat(ClsGlobal.getEntryDateTime())
                    .concat("'")

                    .concat(",")
                    .concat("'")
                    .concat(ObjParams.getRemark().trim()
                            .replace("'","''"))
                    .concat("'")
                    .concat(");");

            Log.e("Insert-- ", qry);
            SQLiteStatement statement = db.compileStatement(qry);
            statement.executeUpdateDelete();

            Cursor c = db.rawQuery("SELECT last_insert_rowid()", null);
            c.moveToFirst();
            int id = c.getInt(0);
            ObjResult.setExpense_id(id);

        } catch (Exception e) {
            result = -1;
            Log.e("jsonObject", "Insert------" + e.getMessage());
            e.getMessage();
        }
        return ObjResult;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsExpenseMasterNew> getListAllExp(String _where) {
        Log.e("getListAllExp","getListAllExp call");

        List<ClsExpenseMasterNew> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT "
                    .concat("em.[Expense_ID]")
                    .concat(",em.[VENDOR_ID]")
                    .concat(",em.[AMOUNT]")
                    .concat(",em.[RECEIPT_NO]")
                    .concat(",em.[VENDOR_NAME]")
                    .concat(",em.[ENTRY_DATE]")
                    .concat(",em.[RECEIPT_DATE]")
                    .concat(",em.[GRAND_TOTAL]")
                    .concat(",em.[EMPLOYEE_NAME]")
                    .concat(",em.[OTHER_TAX1]")
                    .concat(",em.[OTHER_VAL1]")
                    .concat(",em.[OTHER_TAX2]")
                    .concat(",em.[OTHER_VAL2]")
                    .concat(",em.[OTHER_TAX3]")
                    .concat(",em.[OTHER_VAL3]")
                    .concat(",em.[OTHER_TAX4]")
                    .concat(",em.[OTHER_VAL4]")
                    .concat(",em.[OTHER_TAX5]")
                    .concat(",em.[OTHER_VAL5]")
                    .concat(",em.[DISCOUNT]")
                    .concat(",em.[REMARK]")
                    .concat(",etm.[EXPENSE_TYPE_NAME]")
                    .concat(" FROM [ExpenseMasterNew] AS em")
                    .concat(" INNER JOIN [EXPENSE_TYPE_MASTER] AS etm on etm.EXPENSE_TYPE_ID = em.EXPENSE_TYPE_ID ")
                    .concat(" WHERE 1=1 ")
                    .concat(_where)
                    .concat(" ORDER BY em.[RECEIPT_DATE] DESC ");

//atle khali may (m)j aavu joie ne??
            Cursor cur = db.rawQuery(qry, null);

            Log.d("getListAllExp", "getListAllExp-- " + qry);


            Log.d("cur", "count_getAllExp:-- " + cur.getCount());
            Log.d("cur", "getColumnCount:-- " + cur.getColumnCount());

            while (cur.moveToNext()) {
                ClsExpenseMasterNew getSet = new ClsExpenseMasterNew();

                getSet.setExpense_id(cur.getInt(cur.getColumnIndex("Expense_ID")));
                getSet.setVendor_id(cur.getInt(cur.getColumnIndex("VENDOR_ID")));

                getSet.setReceipt_no(cur.getString(cur.getColumnIndex("RECEIPT_NO")));
                getSet.setVendor_name(cur.getString(cur.getColumnIndex("VENDOR_NAME")));

                getSet.setEntry_date(cur.getString(cur.getColumnIndex("ENTRY_DATE")));
                getSet.setReceipt_date(cur.getString(cur.getColumnIndex("RECEIPT_DATE")));

                getSet.setRemark(cur.getString(cur.getColumnIndex("REMARK")));
                getSet.setExpense_type_name(cur.getString(cur.getColumnIndex("EXPENSE_TYPE_NAME")));
                getSet.setAmount(Double.valueOf(cur.getString(cur.getColumnIndex("AMOUNT"))));
                getSet.setDiscount(Double.valueOf(cur.getString(cur.getColumnIndex("DISCOUNT"))));

                getSet.setOther_tax1(cur.getString(cur.getColumnIndex("OTHER_TAX1")));
                getSet.setOther_val1(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL1"))));
//
                getSet.setOther_tax2(cur.getString(cur.getColumnIndex("OTHER_TAX2")));
                getSet.setOther_val2(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL2"))));
//
                getSet.setOther_tax3(cur.getString(cur.getColumnIndex("OTHER_TAX3")));
                getSet.setOther_val3(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL3"))));
//
                getSet.setOther_tax4(cur.getString(cur.getColumnIndex("OTHER_TAX4")));
                getSet.setOther_val4(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL4"))));
//
                getSet.setOther_tax5(cur.getString(cur.getColumnIndex("OTHER_TAX5")));
                getSet.setOther_val5(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL5"))));
//
//                getSet.setDiscount(Double.valueOf(cur.getString(cur.getColumnIndex("DISCOUNT"))));
//                getSet.setExpense_type_id(cur.getInt(cur.getColumnIndex("EXPENSE_TYPE_ID")));

                getSet.setGRAND_TOTAL(Double.valueOf(cur.getString(cur.getColumnIndex("GRAND_TOTAL"))));
                getSet.setEmployee_name(cur.getString(cur.getColumnIndex("EMPLOYEE_NAME")));


                list.add(getSet);
            }
        } catch (Exception e) {
            Log.e("ClsExpenseType", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsExpenseMasterNew> getListTopExp(String _where) {
        List<ClsExpenseMasterNew> list = new ArrayList<>();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

//            String _delete = "DELETE FROM [ExpenseMasterNew]";
//            db.execSQL(_delete);

            String qry = "SELECT "
                    .concat("em.[Expense_ID]")
                    .concat(",em.[VENDOR_ID]")
                    .concat(",em.[AMOUNT]")
                    .concat(",em.[RECEIPT_NO]")
                    .concat(",em.[VENDOR_NAME]")
                    .concat(",strftime('%m/%d/%Y %H:%M:%S',em.[ENTRY_DATE]) [ENTRY_DATE]")
                    .concat(",em.[RECEIPT_DATE]")
                    .concat(",em.[GRAND_TOTAL]")
                    .concat(",em.[EMPLOYEE_NAME]")
                    .concat(",em.[OTHER_TAX1]")
                    .concat(",em.[OTHER_VAL1]")
                    .concat(",em.[OTHER_TAX2]")
                    .concat(",em.[OTHER_VAL2]")
                    .concat(",em.[OTHER_TAX3]")
                    .concat(",em.[OTHER_VAL3]")
                    .concat(",em.[OTHER_TAX4]")
                    .concat(",em.[OTHER_VAL4]")
                    .concat(",em.[OTHER_TAX5]")
                    .concat(",em.[OTHER_VAL5]")
                    .concat(",em.[DISCOUNT]")
                    .concat(",em.[REMARK]")
                    .concat(",etm.[EXPENSE_TYPE_NAME]")
                    .concat(" FROM [ExpenseMasterNew] AS em")
                    .concat(" INNER JOIN [EXPENSE_TYPE_MASTER] AS etm on etm.EXPENSE_TYPE_ID = em.EXPENSE_TYPE_ID ")
                    .concat(" WHERE 1=1 ")
                    .concat(_where)
                    .concat(" ORDER BY em.[ENTRY_DATE] DESC ").concat("LIMIT 5");


            Cursor cur = db.rawQuery(qry, null);
            Log.d("LIST TESTING", "getListTopExp-- " + qry);


            while (cur.moveToNext()) {
                ClsExpenseMasterNew getSet = new ClsExpenseMasterNew();

                getSet.setExpense_id(cur.getInt(cur.getColumnIndex("Expense_ID")));
                getSet.setVendor_id(cur.getInt(cur.getColumnIndex("VENDOR_ID")));

                getSet.setReceipt_no(cur.getString(cur.getColumnIndex("RECEIPT_NO")));
                getSet.setVendor_name(cur.getString(cur.getColumnIndex("VENDOR_NAME")));

                getSet.setEntry_date(cur.getString(cur.getColumnIndex("ENTRY_DATE")));
                getSet.setReceipt_date(cur.getString(cur.getColumnIndex("RECEIPT_DATE")));

                getSet.setRemark(cur.getString(cur.getColumnIndex("REMARK")));
                getSet.setExpense_type_name(cur.getString(cur.getColumnIndex("EXPENSE_TYPE_NAME")));
                getSet.setAmount(Double.valueOf(cur.getString(cur.getColumnIndex("AMOUNT"))));

                getSet.setOther_tax1(cur.getString(cur.getColumnIndex("OTHER_TAX1")));
                getSet.setOther_val1(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL1"))));

                getSet.setOther_tax2(cur.getString(cur.getColumnIndex("OTHER_TAX2")));
                getSet.setOther_val2(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL2"))));

                getSet.setOther_tax3(cur.getString(cur.getColumnIndex("OTHER_TAX3")));
                getSet.setOther_val3(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL3"))));

                getSet.setOther_tax4(cur.getString(cur.getColumnIndex("OTHER_TAX4")));
                getSet.setOther_val4(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL4"))));

                getSet.setOther_tax5(cur.getString(cur.getColumnIndex("OTHER_TAX5")));
                getSet.setOther_val5(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL5"))));

                getSet.setDiscount(Double.valueOf(cur.getString(cur.getColumnIndex("DISCOUNT"))));
//                getSet.setExpense_type_id(cur.getInt(cur.getColumnIndex("EXPENSE_TYPE_ID")));
                getSet.setGRAND_TOTAL(Double.valueOf(cur.getString(cur.getColumnIndex("GRAND_TOTAL"))));
                getSet.setEmployee_name(cur.getString(cur.getColumnIndex("EMPLOYEE_NAME")));
                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("ClsExpenseType", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsExpenseMasterNew> getMostFiveExp() {
        List<ClsExpenseMasterNew> list = new ArrayList<>();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String qry = "SELECT "
                    .concat("SUM (em.[GRAND_TOTAL]) AS [GRAND_TOTAL]")
                    .concat(",em.[EMPLOYEE_NAME]")
                    .concat(",em.[VENDOR_NAME]")
                    .concat(",etm.[EXPENSE_TYPE_NAME]")
                    .concat(" FROM [ExpenseMasterNew] AS em")
                    .concat(" INNER JOIN [EXPENSE_TYPE_MASTER] AS etm on etm.EXPENSE_TYPE_ID = em.EXPENSE_TYPE_ID ")
                    .concat(" WHERE 1=1 ")
                    .concat(" GROUP BY em.[EMPLOYEE_NAME],em.[VENDOR_NAME],etm.[EXPENSE_TYPE_NAME]")
                    .concat(" ORDER BY SUM(em.[GRAND_TOTAL]) DESC ").concat(" LIMIT 5");

            Cursor cur = db.rawQuery(qry, null);
            Log.d("LIST TESTING", "TAB_2_NEW--" + qry);
            Log.d("cur", "cur_Up:-- " + cur.getCount());
            Log.d("cur", "getColumnCount:-- " + cur.getColumnCount());

//            for (int i = 0; i < cur.getColumnCount(); i++) {
//                Log.d("cur", "Column:-- " + cur.getColumnName(i));
//            }

            while (cur.moveToNext()) {
                ClsExpenseMasterNew getSet = new ClsExpenseMasterNew();
                getSet.setGRAND_TOTAL(Double.valueOf(cur.getString(cur.getColumnIndex("GRAND_TOTAL"))));
                getSet.setVendor_name(cur.getString(cur.getColumnIndex("VENDOR_NAME")));
                getSet.setEmployee_name(cur.getString(cur.getColumnIndex("EMPLOYEE_NAME")));
                getSet.setExpense_type_name(cur.getString(cur.getColumnIndex("EXPENSE_TYPE_NAME")));
                list.add(getSet);
            }
        } catch (Exception e) {
            Log.e("ClsExpenseType", "GetList" + e.getMessage());
            e.getMessage();
        }

        return list;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsExpenseMasterNew> getGrantTotalExp() {
        List<ClsExpenseMasterNew> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String qry = "SELECT "
                    .concat("SUM([GRAND_TOTAL]) AS [GRAND_TOTAL]")
                    .concat(" FROM [ExpenseMasterNew] WHERE 1=1 ");

            Cursor cur = db.rawQuery(qry, null);
            Log.d("LIST TESTING", "getGrantTotalExp-- " + qry);
            Log.d("cur", "getGrantTotalExp-- " + cur.getCount());
            Log.d("cur", "getGrantTotalExp-- " + cur.getColumnCount());

            while (cur.moveToNext()) {
                ClsExpenseMasterNew getSet = new ClsExpenseMasterNew();
                getSet.setGRAND_TOTAL(Double.valueOf(cur.getString(
                        cur.getColumnIndex("GRAND_TOTAL"))));
                list.add(getSet);
            }
        } catch (Exception e) {
            Log.e("ClsExpenseType", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsExpenseMasterNew> getListNew() {
        List<ClsExpenseMasterNew> list = new ArrayList<>();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT "
                    .concat("strftime('%m',[RECEIPT_DATE]) ||' '||  strftime('%Y',[RECEIPT_DATE]) AS [MONTH]")
                    .concat(",SUM ([GRAND_TOTAL]) AS [GRAND_TOTAL]")
                    .concat(" FROM [ExpenseMasterNew] WHERE 1=1 ")
                    .concat("GROUP BY strftime('%m',[RECEIPT_DATE]) ||' '||  strftime('%Y',[RECEIPT_DATE])")
                    .concat("ORDER BY strftime('%m',[RECEIPT_DATE]) ||' '||  strftime('%Y',[RECEIPT_DATE]) DESC ");


//            String qry = "SELECT SUM [GRAND_TOTAL] as [GRAND_TOTAL] FROM ExpenseMasterNew";


            Cursor cur = db.rawQuery(qry, null);

            Log.d("LISTTESTING", "Tab_1_BAR-- " + qry);

            Log.d("cur", "cur_Up:-- " + cur.getCount());
            Log.d("cur", "getColumnCount:-- " + cur.getColumnCount());

//            for (int i = 0; i < cur.getColumnCount(); i++) {
//                Log.d("cur", "Column:-- " + cur.getColumnName(i));
//            }

            while (cur.moveToNext()) {
                ClsExpenseMasterNew getSet = new ClsExpenseMasterNew();
                getSet.setReceipt_date(cur.getString(cur.getColumnIndex("MONTH")));
                getSet.setGRAND_TOTAL(Double.valueOf(cur.getString(cur.getColumnIndex("GRAND_TOTAL"))));
                list.add(getSet);
            }
        } catch (Exception e) {
            Log.e("ClsExpenseType", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static int Update(ClsExpenseMasterNew ObjParams) {
        int result = 0;

        Log.e("Update", "Query..");

        db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

        String strSql = "UPDATE [ExpenseMasterNew] SET "
                .concat(" [RECEIPT_DATE] = ")
                .concat("'")
                .concat(ClsGlobal.getDbDateFormat(ObjParams.getReceipt_date()))
                .concat("'")

                .concat(", [VENDOR_ID] = ")
                .concat(String.valueOf(ObjParams.getVendor_id()))

                .concat(", [EXPENSE_TYPE_ID] = ")
                .concat(String.valueOf(ObjParams.getExpense_type_id()))

                .concat(", [GRAND_TOTAL] = ")
                .concat(String.valueOf(ObjParams.getGRAND_TOTAL()))

                .concat(", [DISCOUNT] = ")
                .concat(String.valueOf(ObjParams.getDiscount()))

                .concat(", [AMOUNT] = ")
                .concat(String.valueOf(ObjParams.getAmount()))

                .concat(", [EMPLOYEE_NAME] = ")
                .concat("'")
                .concat(ObjParams.getEmployee_name().trim()
                        .replace("'","''"))
                .concat("'")

                .concat(", [VENDOR_NAME] = ")
                .concat("'")
                .concat(ObjParams.getVendor_name().trim()
                        .replace("'","''"))
                .concat("'")

                .concat(", [RECEIPT_NO] = ")
                .concat("'")
                .concat(ObjParams.getReceipt_no().trim()
                        .replace("'","''"))
                .concat("'")

                .concat(", [OTHER_TAX1] = ")
                .concat("'")
                .concat(ObjParams.getOther_tax1())
                .concat("'")

                .concat(", [OTHER_VAL1] = ")
                .concat(String.valueOf(ObjParams.getOther_val1()))

                .concat(", [OTHER_TAX2] = ")
                .concat("'")
                .concat(ObjParams.getOther_tax2())
                .concat("'")

                .concat(", [OTHER_VAL2] = ")
                .concat(String.valueOf(ObjParams.getOther_val2()))

                .concat(", [OTHER_TAX3] = ")
                .concat("'")
                .concat(ObjParams.getOther_tax3())
                .concat("'")

                .concat(", [OTHER_VAL3] = ")
                .concat(String.valueOf(ObjParams.getOther_val3()))

                .concat(", [OTHER_TAX4] = ")
                .concat("'")
                .concat(ObjParams.getOther_tax4())
                .concat("'")

                .concat(", [OTHER_VAL4] = ")
                .concat(String.valueOf(ObjParams.getOther_val4()))

                .concat(", [OTHER_TAX5] = ")
                .concat("'")
                .concat(ObjParams.getOther_tax5())
                .concat("'")

                .concat(", [OTHER_VAL5] = ")
                .concat(String.valueOf(ObjParams.getOther_val5()))

                .concat(", [REMARK] = ")
                .concat("'")
                .concat(ObjParams.getRemark().trim()
                        .replace("'","''"))
                .concat("'")

                .concat(" WHERE [Expense_ID] = ")
                .concat(String.valueOf(ObjParams.getExpense_id()))
                .concat(";");

        Log.e("Update", strSql);
        SQLiteStatement statement = db.compileStatement(strSql);
        result = statement.executeUpdateDelete();

        db.close();
        return result;
    }

    @SuppressLint("WrongConstant")
    public static int Delete(ClsExpenseMasterNew ObjParams) {
        int result = 0;
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "DELETE FROM [ExpenseMasterNew] WHERE [Expense_ID] = "
                    .concat(String.valueOf(ObjParams.getExpense_id()))
                    .concat(";");
            Log.e("DELETE", "DELqRY-" + strSql);
            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();
            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsExpenseType", "Delete" + e.getMessage());
            e.getMessage();
        }
        return result;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsExpenseMasterNew> getListNew(String _where) {
        List<ClsExpenseMasterNew> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT "
                    .concat("em. [Expense_ID]")
                    .concat(",em.[VENDOR_ID]")
                    .concat(",em.[AMOUNT]")
                    .concat(",em.[RECEIPT_NO]")
                    .concat(",em.[VENDOR_NAME]")
                    .concat(",em.[RECEIPT_DATE]")
                    .concat(",em.[GRAND_TOTAL]")
                    .concat(",em.[EMPLOYEE_NAME]")
                    .concat(",em.[OTHER_TAX1]")
                    .concat(",em.[OTHER_VAL1]")
                    .concat(",em.[OTHER_TAX2]")
                    .concat(",em.[OTHER_VAL2]")
                    .concat(",em.[OTHER_TAX3]")
                    .concat(",em.[OTHER_VAL3]")
                    .concat(",em.[OTHER_TAX4]")
                    .concat(",em.[OTHER_VAL4]")
                    .concat(",em.[OTHER_TAX5]")
                    .concat(",em.[OTHER_VAL5]")
                    .concat(",em.[DISCOUNT]")
                    .concat(",em.[REMARK]")
                    .concat(",etm.[EXPENSE_TYPE_NAME]")
                    .concat(" FROM [ExpenseMasterNew] AS em")
                    .concat(" INNER JOIN [EXPENSE_TYPE_MASTER] AS etm on etm.EXPENSE_TYPE_ID = em.EXPENSE_TYPE_ID ")
                    .concat(" WHERE 1=1 ")
                    .concat(_where)
                    .concat("ORDER BY em.[RECEIPT_DATE] ASC");


            Cursor cur = db.rawQuery(qry, null);

            Log.d("LIST TESTING", "Start_TO_End-- " + qry);


            Log.d("cur", "cur_Up:-- " + cur.getCount());
            Log.d("cur", "getColumnCount:-- " + cur.getColumnCount());

            while (cur.moveToNext()) {
                ClsExpenseMasterNew getSet = new ClsExpenseMasterNew();
                getSet.setExpense_id(cur.getInt(cur.getColumnIndex("Expense_ID")));
                getSet.setVendor_id(cur.getInt(cur.getColumnIndex("VENDOR_ID")));

                getSet.setReceipt_no(cur.getString(cur.getColumnIndex("RECEIPT_NO")));
                getSet.setVendor_name(cur.getString(cur.getColumnIndex("VENDOR_NAME")));

                getSet.setReceipt_date(cur.getString(cur.getColumnIndex("RECEIPT_DATE")));
                getSet.setRemark(cur.getString(cur.getColumnIndex("REMARK")));
                getSet.setExpense_type_name(cur.getString(cur.getColumnIndex("EXPENSE_TYPE_NAME")));
                getSet.setAmount(Double.valueOf(cur.getString(cur.getColumnIndex("AMOUNT"))));

                getSet.setOther_tax1(cur.getString(cur.getColumnIndex("OTHER_TAX1")));
                getSet.setOther_val1(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL1"))));

                getSet.setOther_tax2(cur.getString(cur.getColumnIndex("OTHER_TAX2")));
                getSet.setOther_val2(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL2"))));

                getSet.setOther_tax3(cur.getString(cur.getColumnIndex("OTHER_TAX3")));
                getSet.setOther_val3(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL3"))));

                getSet.setOther_tax4(cur.getString(cur.getColumnIndex("OTHER_TAX4")));
                getSet.setOther_val4(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL4"))));

                getSet.setOther_tax5(cur.getString(cur.getColumnIndex("OTHER_TAX5")));
                getSet.setOther_val5(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL5"))));

                getSet.setDiscount(Double.valueOf(cur.getString(cur.getColumnIndex("DISCOUNT"))));
//                getSet.setExpense_type_id(cur.getInt(cur.getColumnIndex("EXPENSE_TYPE_ID")));
                getSet.setGRAND_TOTAL(Double.valueOf(cur.getString(cur.getColumnIndex("GRAND_TOTAL"))));
                getSet.setEmployee_name(cur.getString(cur.getColumnIndex("EMPLOYEE_NAME")));
                list.add(getSet);
            }
        } catch (Exception e) {
            Log.e("ClsExpenseType", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static ClsExpenseMasterNew getObject(ClsExpenseMasterNew ObjParams) {
        ClsExpenseMasterNew objClsExpenseMasterNew = new ClsExpenseMasterNew();
        Log.e("GetObjectQry", String.valueOf(objClsExpenseMasterNew.getExpense_id()));
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = ("SELECT [Expense_ID]," +
                    "[VENDOR_ID]," +
                    "[RECEIPT_DATE]," +
                    "[AMOUNT]," +
                    "[RECEIPT_NO]," +
                    "[VENDOR_NAME]," +
                    "[GRAND_TOTAL]," +
                    "[EMPLOYEE_NAME]," +
                    "[OTHER_TAX1]," +
                    "[OTHER_VAL1]," +
                    "[OTHER_TAX2]," +
                    "[OTHER_VAL2]," +
                    "[OTHER_TAX3]," +
                    "[OTHER_VAL3]," +
                    "[OTHER_TAX4]," +
                    "[OTHER_VAL4]," +
                    "[OTHER_TAX5]," +
                    "[OTHER_VAL5]," +
                    "[DISCOUNT]," +
                    "[REMARK]," +
                    "[EXPENSE_TYPE_ID]" +
                    " FROM [ExpenseMasterNew] WHERE 1=1 AND [Expense_ID] = ")
                    .concat(String.valueOf(ObjParams.getExpense_id()))
                    .concat(";");

            Log.e("getObject", ">>>>>>>>>" + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            Log.e("getObject", ">>>>>>>>>" + cur.getCount());

            while (cur.moveToNext()) {
                objClsExpenseMasterNew.setExpense_id(cur.getInt(cur.getColumnIndex("Expense_ID")));
                objClsExpenseMasterNew.setExpense_type_id(cur.getInt(cur.getColumnIndex("EXPENSE_TYPE_ID")));

                objClsExpenseMasterNew.setVendor_id(cur.getInt(cur.getColumnIndex("VENDOR_ID")));
                Log.e("VENDOR_ID", ">>>>>>>>>" + cur.getInt(cur.getColumnIndex("VENDOR_ID")));

                objClsExpenseMasterNew.setVendor_name(cur.getString(cur.getColumnIndex("VENDOR_NAME")));
                objClsExpenseMasterNew.setReceipt_date(cur.getString(cur.getColumnIndex("RECEIPT_DATE")));
                objClsExpenseMasterNew.setReceipt_no(cur.getString(cur.getColumnIndex("RECEIPT_NO")));

                objClsExpenseMasterNew.setAmount(Double.valueOf(cur.getString(cur.getColumnIndex("AMOUNT"))));
                objClsExpenseMasterNew.setDiscount(Double.valueOf(cur.getString(cur.getColumnIndex("DISCOUNT"))));

                objClsExpenseMasterNew.setRemark(cur.getString(cur.getColumnIndex("REMARK")));

                objClsExpenseMasterNew.setOther_tax1(cur.getString(cur.getColumnIndex("OTHER_TAX1")));
                objClsExpenseMasterNew.setOther_val1(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL1"))));

                objClsExpenseMasterNew.setOther_tax2(cur.getString(cur.getColumnIndex("OTHER_TAX2")));
                objClsExpenseMasterNew.setOther_val2(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL2"))));

                objClsExpenseMasterNew.setOther_tax3(cur.getString(cur.getColumnIndex("OTHER_TAX3")));
                objClsExpenseMasterNew.setOther_val3(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL3"))));

                objClsExpenseMasterNew.setOther_tax4(cur.getString(cur.getColumnIndex("OTHER_TAX4")));
                objClsExpenseMasterNew.setOther_val4(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL4"))));

                objClsExpenseMasterNew.setOther_tax5(cur.getString(cur.getColumnIndex("OTHER_TAX5")));
                objClsExpenseMasterNew.setOther_val5(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL5"))));


                objClsExpenseMasterNew.setGRAND_TOTAL(Double.valueOf(cur.getString(cur.getColumnIndex("GRAND_TOTAL"))));
                objClsExpenseMasterNew.setEmployee_name(cur.getString(cur.getColumnIndex("EMPLOYEE_NAME")));
            }
            db.close();
        } catch (Exception e) {
            Log.e("getObject", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }
        return objClsExpenseMasterNew;
    }



    @SuppressLint("WrongConstant")
    public static List<ClsExpenseMasterNew> getListVendor(String _where) {
        List<ClsExpenseMasterNew> list = new ArrayList<>();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT "
                    .concat(" tblexp.[Expense_ID]")
                    .concat(",tblexp.[VENDOR_ID]")
                    .concat(",tblexp.[EXPENSE_TYPE_ID]")
                    .concat(",tblexp.[VENDOR_NAME]")
                    .concat(",tblexp.[EMPLOYEE_NAME]")
                    .concat(",tblexp.[OTHER_TAX1]")
                    .concat(",tblexp.[OTHER_VAL1]")
                    .concat(",tblexp.[OTHER_TAX2]")
                    .concat(",tblexp.[OTHER_VAL2]")
                    .concat(",tblexp.[OTHER_TAX3]")
                    .concat(",tblexp.[OTHER_VAL3]")
                    .concat(",tblexp.[OTHER_TAX4]")
                    .concat(",tblexp.[OTHER_VAL4]")
                    .concat(",tblexp.[OTHER_TAX5]")
                    .concat(",tblexp.[OTHER_VAL5]")
                    .concat(",tblexp.[AMOUNT]")
                    .concat(",tblexp.[DISCOUNT]")
                    .concat(",tblexp.[GRAND_TOTAL]")
                    .concat(",tblexp.[RECEIPT_NO]")
                    .concat(",tblexp.[RECEIPT_DATE]")
                    .concat(",tblexp.[ENTRY_DATE]")
                    .concat(",tblexp.[REMARK]")
                    .concat(",VM.[EXPENSE_TYPE_NAME]")
                    .concat(" FROM [ExpenseMasterNew] as tblexp")
                    .concat(" INNER JOIN [EXPENSE_TYPE_MASTER] as VM ON VM.[EXPENSE_TYPE_ID]=tblexp.[EXPENSE_TYPE_ID] ")
                    .concat(" WHERE 1=1")
                    .concat(_where)
                    .concat(" ORDER BY tblexp.[RECEIPT_DATE] ASC ");
            Cursor cur = db.rawQuery(qry, null);

            Log.e("ClsExpense", "QRY---->>" + qry);
            Log.e("ClsExpense", "Count---->>" + cur.getCount());


            while (cur.moveToNext()) {
                ClsExpenseMasterNew getSet = new ClsExpenseMasterNew();
                getSet.setExpense_id(cur.getInt(cur.getColumnIndex("Expense_ID")));
                getSet.setVendor_id(cur.getInt(cur.getColumnIndex("VENDOR_ID")));
                getSet.setReceipt_no(cur.getString(cur.getColumnIndex("RECEIPT_NO")));
                getSet.setVendor_name(cur.getString(cur.getColumnIndex("VENDOR_NAME")));
                getSet.setReceipt_date(cur.getString(cur.getColumnIndex("RECEIPT_DATE")));
                getSet.setRemark(cur.getString(cur.getColumnIndex("REMARK")));
                getSet.setExpense_type_name(cur.getString(cur.getColumnIndex("EXPENSE_TYPE_NAME")));
                getSet.setAmount(Double.valueOf(cur.getString(cur.getColumnIndex("AMOUNT"))));
                getSet.setOther_tax1(cur.getString(cur.getColumnIndex("OTHER_TAX1")));
                getSet.setOther_val1(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL1"))));
                getSet.setOther_tax2(cur.getString(cur.getColumnIndex("OTHER_TAX2")));
                getSet.setOther_val2(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL2"))));
                getSet.setOther_tax3(cur.getString(cur.getColumnIndex("OTHER_TAX3")));
                getSet.setOther_val3(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL3"))));
                getSet.setOther_tax4(cur.getString(cur.getColumnIndex("OTHER_TAX4")));
                getSet.setOther_val4(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL4"))));
                getSet.setOther_tax5(cur.getString(cur.getColumnIndex("OTHER_TAX5")));
                getSet.setOther_val5(Double.valueOf(cur.getString(cur.getColumnIndex("OTHER_VAL5"))));
                getSet.setDiscount(Double.valueOf(cur.getString(cur.getColumnIndex("DISCOUNT"))));
//                getSet.setExpense_type_id(cur.getInt(cur.getColumnIndex("EXPENSE_TYPE_ID")));
                getSet.setGRAND_TOTAL(Double.valueOf(cur.getString(cur.getColumnIndex("GRAND_TOTAL"))));
                getSet.setEmployee_name(cur.getString(cur.getColumnIndex("EMPLOYEE_NAME")));

                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("ClsExpenseNew", "GetList-->>" + e.getMessage());
            e.getMessage();
        }
        return list;
    }



    @SuppressLint("WrongConstant")
    public ClsExpenseMasterNew getObject(int exp_id) {
        ClsExpenseMasterNew Obj = new ClsExpenseMasterNew();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String sqlStr = "SELECT "
                    .concat("tblstock.[Expense_ID]")
                    .concat(",tblstock.[VENDOR_ID]")
                    .concat(",tblstock.[VENDOR_NAME]")
                    .concat(",tblstock.[EXPENSE_TYPE_ID]")
                    .concat(",tblstock.[EMPLOYEE_NAME]")
                    .concat(",tblstock.[OTHER_TAX1]")
                    .concat(",tblstock.[OTHER_VAL1]")
                    .concat(",tblstock.[OTHER_TAX2]")
                    .concat(",tblstock.[OTHER_VAL2]")
                    .concat(",tblstock.[OTHER_TAX3]")
                    .concat(",tblstock.[OTHER_VAL3]")
                    .concat(",tblstock.[OTHER_TAX4]")
                    .concat(",tblstock.[OTHER_VAL4]")
                    .concat(",tblstock.[OTHER_TAX5]")
                    .concat(",tblstock.[OTHER_VAL5]")
                    .concat(",tblstock.[DISCOUNT]")
                    .concat(",tblstock.[AMOUNT]")
                    .concat(",tblstock.[REMARK]")
                    .concat(",tblstock.[GRAND_TOTAL]")
                    .concat(",tblstock.[RECEIPT_DATE]")
                    .concat(",tblstock.[RECEIPT_NO]")
                    .concat(",tblstock.[ENTRY_DATE]")
                    .concat(",VM.[EXPENSE_TYPE_NAME]")
                    .concat(" FROM [ExpenseMasterNew] as tblstock")
                    .concat(" LEFT JOIN [EXPENSE_TYPE_MASTER] as VM ON VM.[EXPENSE_TYPE_ID]=tblstock.[EXPENSE_TYPE_ID] ")
                    .concat(" WHERE 1=1 ")
                    .concat(" AND [Expense_ID] = ")

//            String sqlStr = ("SELECT [EXPENSE_ID],[VENDOR_ID],[VENDOR_NAME],[EXPENSE_TYPE_ID],[EMPLOYEE_NAME],[OTHER_TAX1],[OTHER_VAL1],[OTHER_TAX2]," +
//                    "[OTHER_VAL2],[OTHER_TAX3],[OTHER_VAL3],[OTHER_TAX4],[OTHER_VAL4],[OTHER_TAX5],[OTHER_VAL5],[DISCOUNT]," +
//                    "[REMARK],[GRAND_TOTAL],[TRANSACTION_DATE],[BILL_RECEIPT_NO],[ENTRY_DATE] FROM [ExpenseMaster] WHERE 1=1 AND [EXPENSE_ID] = ")

                    .concat(String.valueOf(exp_id))
                    .concat(";");
            Log.e("GetObj", "Qurty-->>>>>" + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            Log.e("GetObj", "curCount>>>>>" + cur.getCount());
            while (cur.moveToNext()) {

                Obj.setExpense_id(cur.getInt(cur.getColumnIndex("Expense_ID")));
                Obj.setVendor_id(cur.getInt(cur.getColumnIndex("VENDOR_ID")));
                Obj.setExpense_type_name(cur.getString(cur.getColumnIndex("EXPENSE_TYPE_NAME")));
                Obj.setExpense_type_id(cur.getInt(cur.getColumnIndex("EXPENSE_TYPE_ID")));
                Obj.setVendor_name(cur.getString(cur.getColumnIndex("VENDOR_NAME")));
                Obj.setEmployee_name(cur.getString(cur.getColumnIndex("EMPLOYEE_NAME")));
                Obj.setOther_tax1(cur.getString(cur.getColumnIndex("OTHER_TAX1")));
                Obj.setOther_val1(cur.getDouble(cur.getColumnIndex("OTHER_VAL1")));
                Obj.setOther_tax2(cur.getString(cur.getColumnIndex("OTHER_TAX2")));
                Obj.setOther_val2(cur.getDouble(cur.getColumnIndex("OTHER_VAL2")));
                Obj.setOther_tax3(cur.getString(cur.getColumnIndex("OTHER_TAX3")));
                Obj.setOther_val3(cur.getDouble(cur.getColumnIndex("OTHER_VAL3")));
                Obj.setOther_tax4(cur.getString(cur.getColumnIndex("OTHER_TAX4")));
                Obj.setOther_val4(cur.getDouble(cur.getColumnIndex("OTHER_VAL4")));
                Obj.setOther_tax5(cur.getString(cur.getColumnIndex("OTHER_TAX5")));
                Obj.setOther_val5(cur.getDouble(cur.getColumnIndex("OTHER_VAL5")));
                Obj.setAmount(cur.getDouble(cur.getColumnIndex("AMOUNT")));
                Obj.setRemark(cur.getString(cur.getColumnIndex("REMARK")));
                Obj.setDiscount(cur.getDouble(cur.getColumnIndex("DISCOUNT")));
                Obj.setGRAND_TOTAL(cur.getDouble(cur.getColumnIndex("GRAND_TOTAL")));
                Obj.setReceipt_date(cur.getString(cur.getColumnIndex("RECEIPT_DATE")));
                Obj.setReceipt_no(cur.getString(cur.getColumnIndex("RECEIPT_NO")));
                Obj.setEntry_date(cur.getString(cur.getColumnIndex("ENTRY_DATE")));


            }

            String Qry = "SELECT "
                    .concat("tblstock.[STOCK_ID]")
                    .concat(",tblstock.[INVENTORY_ITEM_ID]")
                    .concat(",tblstock.[INVENTORY_ITEM_NAME]")
                    .concat(",tblstock.[QUANTITY]")
                    .concat(",tblstock.[AMOUNT]")
                    .concat(",IM.[UNIT_NAME]")
                    .concat(" FROM [Inventory_stock_master] as tblstock")
                    .concat(" LEFT JOIN [INVENTORY_ITEM_MASTER] as IM ON IM.[INVENTORY_ITEM_ID]=tblstock.[INVENTORY_ITEM_ID] ")
                    .concat(" WHERE 1=1 ")
                    .concat(" AND [ORDER_ID] = ")
                    .concat(String.valueOf(exp_id)
                            .concat(";"));



//            sqlStr = ("SELECT [STOCK_ID],[INVENTORY_ITEM_ID],[INVENTORY_ITEM_NAME],[QUANTITY],[AMOUNT]" +
//                    " FROM [Inventory_stock_master] WHERE 1=1 AND [ORDER_ID] = ").concat(String.valueOf(exp_id))
//                    .concat(";");
            Log.e("ItemList", ">>>>>>>>>" + Qry);
            cur = db.rawQuery(Qry, null);
            Log.e("ItemList", ">>>>>>>>>" + cur.getCount());
            list_items = new ArrayList<>();
            while (cur.moveToNext()) {
                ClsInventoryStock item = new ClsInventoryStock();
                item.setStock_id((cur.getInt(cur.getColumnIndex("STOCK_ID"))));
                item.setInventory_item_id((cur.getInt(cur.getColumnIndex("INVENTORY_ITEM_ID"))));
                item.setInventory_item_name((cur.getString(cur.getColumnIndex("INVENTORY_ITEM_NAME"))));
                item.setQty((cur.getDouble(cur.getColumnIndex("QUANTITY"))));
                item.setAmount((cur.getDouble(cur.getColumnIndex("AMOUNT"))));
                item.setUnitname((cur.getString(cur.getColumnIndex("UNIT_NAME"))));

                list_items.add(item);
            }
            Obj.setList_items(list_items);
            db.close();

        } catch (Exception e) {
            Log.e("OBJ", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }
        return Obj;
    }

    @Override
    public String toString() {
        return "ClsExpenseMasterNew{" +
                "expense_id=" + expense_id +
                ", vendor_id=" + vendor_id +
                ", expense_type_id=" + expense_type_id +
                ", list_items=" + list_items +
                ", vendor_name='" + vendor_name + '\'' +
                ", employee_name='" + employee_name + '\'' +
                ", receipt_no='" + receipt_no + '\'' +
                ", receipt_date='" + receipt_date + '\'' +
                ", amount=" + amount +
                ", other_tax1='" + other_tax1 + '\'' +
                ", other_val1=" + other_val1 +
                ", other_tax2='" + other_tax2 + '\'' +
                ", other_val2=" + other_val2 +
                ", other_tax3='" + other_tax3 + '\'' +
                ", other_val3=" + other_val3 +
                ", other_tax4='" + other_tax4 + '\'' +
                ", other_val4=" + other_val4 +
                ", other_tax5='" + other_tax5 + '\'' +
                ", other_val5=" + other_val5 +
                ", discount=" + discount +
                ", GRAND_TOTAL=" + GRAND_TOTAL +
                ", _finalAmount=" + _finalAmount +
                ", entry_date='" + entry_date + '\'' +
                ", remark='" + remark + '\'' +
                ", monthUniqueIndex='" + monthUniqueIndex + '\'' +
                ", expense_type_name='" + expense_type_name + '\'' +
                '}';
    }
}
