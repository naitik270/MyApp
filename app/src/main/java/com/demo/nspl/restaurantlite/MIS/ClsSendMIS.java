package com.demo.nspl.restaurantlite.MIS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Stock.ClsStock;
import com.demo.nspl.restaurantlite.Stock.ClsStockSale;
import com.demo.nspl.restaurantlite.classes.ClsExpenseMasterNew;
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ClsSendMIS {

    private static int result;
    static SQLiteDatabase db;

    @SuppressLint("WrongConstant")
    public static String getStockMIS(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        List<ClsStock> lstClsPurchaseDetails = new ArrayList<>();

        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String strSql = "SELECT "
                    .concat("[ITM].[ITEM_NAME]")
                    .concat(",[ITM].[ITEM_CODE]")
                    .concat(",[ITM].[MIN_STOCK]")
                    .concat(",[ITM].[MAX_STOCK]")
                    .concat(",[ITM].[UNIT_CODE]")
                    .concat(",SUM([PD].[Quantity]) AS [IN] ")
                    .concat(", 0 AS [OUT] ")
                    //.concat(",SUM([SALE].[Quantity]) AS [OUT] ")
                    .concat(",AVG(IFNULL([PD].[Rate],0)) AS [AveragePurchaseRate] ")
                    .concat(",0  AS [AverageSaleRate] ")
//                    .concat(",AVG(IFNULL([SALE].[SaleRate],0)) AS [AverageSaleRate] ")
                    .concat(" FROM [tbl_LayerItem_Master] AS [ITM] ")
                    .concat(" LEFT JOIN [PurchaseDetail] AS [PD] ON [PD].[ItemID] = [ITM].[LAYERITEM_ID] ")
//                    .concat(" LEFT JOIN [InventoryOrderDetail] AS [SALE] ON [SALE].[ItemCode] = [ITM].[ITEM_CODE] ")
                    .concat(" WHERE 1=1 ")
                    .concat(" ")
                    .concat(" GROUP BY  ")
                    .concat("[ITM].[ITEM_NAME]")
                    .concat(",[ITM].[ITEM_CODE]")
                    .concat(",[ITM].[MIN_STOCK]")
                    .concat(",[ITM].[MAX_STOCK]")
                    .concat(",[ITM].[UNIT_CODE]")
                    .concat(" ORDER BY [ITM].[ITEM_NAME]");


            Log.e("--Select--", ">>>>>>>>>" + strSql);
            Cursor cur = db.rawQuery(strSql, null);
            Log.e("--Select--", ">>>>>>>>>" + cur.getCount());
            int sr = 0;

            while (cur.moveToNext()) {
                ClsStock Obj = new ClsStock();

                Obj.setITEM_NAME(cur.getString(cur.getColumnIndex("ITEM_NAME")));
                Obj.setITEM_CODE(cur.getString(cur.getColumnIndex("ITEM_CODE")));
                Obj.setMIN_STOCK(cur.getDouble(cur.getColumnIndex("MIN_STOCK")));
                Obj.setMAX_STOCK(cur.getDouble(cur.getColumnIndex("MAX_STOCK")));
                Obj.setUNIT_CODE(cur.getString(cur.getColumnIndex("UNIT_CODE")));
                Obj.setIN(cur.getDouble(cur.getColumnIndex("IN")));
                Obj.setOUT(cur.getDouble(cur.getColumnIndex("OUT")));
                Obj.setAveragePurchaseRate(cur.getDouble(cur.getColumnIndex("AveragePurchaseRate")));
                Obj.setAverageSaleRate(cur.getDouble(cur.getColumnIndex("AverageSaleRate")));

                lstClsPurchaseDetails.add(Obj);
            }

            //Get Out and Sale Rate of Items.
            List<ClsStock> lstClsStocksOut = new ArrayList<>();
            lstClsStocksOut = new ClsStock(context).getStockOutList("", context);

            if (lstClsPurchaseDetails != null && lstClsPurchaseDetails.size() != 0) {

                if (lstClsStocksOut != null && lstClsStocksOut.size() != 0) {
                    for (ClsStock obj : lstClsPurchaseDetails) {

                        for (ClsStock objClsStockOut : lstClsStocksOut) {

                            if (obj.getITEM_CODE().equalsIgnoreCase(objClsStockOut.getITEM_CODE())) {
                                obj.setOUT(objClsStockOut.getOUT());
                                obj.setAverageSaleRate(objClsStockOut.getAverageSaleRate());
                                lstClsPurchaseDetails.set(lstClsPurchaseDetails.indexOf(obj), obj);
                                break;
                            }

                        }
                    }

                    Gson gson2 = new Gson();
                    String jsonInString2 = gson2.toJson(lstClsPurchaseDetails);
                    Log.e("--ViewStock--", "Final:--- " + jsonInString2);
                }
            }
                db.close();

                Gson gson = new Gson();
                String jsonInString = gson.toJson(lstClsPurchaseDetails);
                Log.d("Result", "ComplainDispositionAPI- " + jsonInString);


                if (lstClsPurchaseDetails != null && lstClsPurchaseDetails.size() != 0) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("<table class=\"table\">");
                    stringBuilder.append("<thead>");
                    stringBuilder.append("<tr>");

                    stringBuilder.append("<th style=\"white-space: normal !important;\">Sr#</th>");
                /*if (isMonthMIS == true) {
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Date</th>");
                }*/

                    stringBuilder.append("<th style=\"white-space: normal !important;\">Item</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Unit</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Min</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Max</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Avg. Purchase Rate</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Avg. Sale Rate</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">In</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Out</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Stock</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Avg. Stock Value</th>");

                    stringBuilder.append("</tr>");//cell end
                    stringBuilder.append("</thead>");//cell end

                    stringBuilder.append("<tbody>");//new ROW st

                    // Stock * Avg. Purchase Rate
                    // Add grand total in footer..

                    Double _totalAvgStockVal = 0.0;

                    for (ClsStock clsStock : lstClsPurchaseDetails) {
                        sr++;
                        stringBuilder.append("<tr>");//new ROW start

                        //1st cell
                        stringBuilder.append("<td>");//cell start
                        stringBuilder.append(sr);//cell value
                        stringBuilder.append("</td>");//cell end

                        //2st cell
                        stringBuilder.append("<td>");//cell start
                        stringBuilder.append(clsStock.getITEM_NAME().toUpperCase());//cell value
                        stringBuilder.append("</td>");//cell end

                        //3rd cell
                        stringBuilder.append("<td>");//cell start
                        stringBuilder.append(clsStock.getUNIT_CODE().toUpperCase());//cell value
                        stringBuilder.append("</td>");//cell end

                        //4th cell
                        stringBuilder.append("<td align = \"right\">");//cell start
                        stringBuilder.append(ClsGlobal.round(clsStock.getMIN_STOCK(), 2));//cell value
                        stringBuilder.append("</td>");//cell end

                        //5th cell
                        stringBuilder.append("<td align = \"right\">");//cell start
                        stringBuilder.append(ClsGlobal.round(clsStock.getMAX_STOCK(), 2));//cell value
                        stringBuilder.append("</td>");//cell end

                        //6th cell
                        stringBuilder.append("<td align = \"right\">");//cell start
                        stringBuilder.append(ClsGlobal.round(clsStock.getAveragePurchaseRate(), 2));//cell value
                        stringBuilder.append("</td>");//cell end

                        //7th cell
                        stringBuilder.append("<td align = \"right\">");//cell start
                        stringBuilder.append(ClsGlobal.round(clsStock.getAverageSaleRate(), 2));//cell value
                        stringBuilder.append("</td>");//cell end

                        //8th cell
                        stringBuilder.append("<td align = \"right\">");//cell start
                        stringBuilder.append(ClsGlobal.round(clsStock.getIN(), 2));//cell value
                        stringBuilder.append("</td>");//cell end

                        //9th cell
                        stringBuilder.append("<td align = \"right\">");//cell start
                        stringBuilder.append(ClsGlobal.round(clsStock.getOUT(), 2));//cell value
                        stringBuilder.append("</td>");//cell end

                        Double _totalStock = clsStock.getIN() - clsStock.getOUT();

                        stringBuilder.append("<td align = \"right\">");//cell start
                        stringBuilder.append(ClsGlobal.round(_totalStock, 2));//cell value
                        stringBuilder.append("</td>");//cell end


                        Double _avgPurchaseRate = _totalStock * clsStock.getAveragePurchaseRate();


                        if (_totalStock > 0.0) {
                            stringBuilder.append("<td align = \"right\">");//cell start
                            stringBuilder.append(ClsGlobal.round(_avgPurchaseRate, 2));//cell value
                            stringBuilder.append("</td>");//cell end
                            stringBuilder.append("</tr>");//cell end
                        } else {
                            stringBuilder.append("<td align = \"right\">");//cell start
                            stringBuilder.append(0.0);//cell value
                            stringBuilder.append("</td>");//cell end
                            stringBuilder.append("</tr>");//cell end
                        }


                        _totalAvgStockVal += _avgPurchaseRate;
                        Log.e("--UNIT_CODE--", "--stringBuilder-- " + clsStock.getITEM_CODE());
                        Log.e("--UNIT_CODE--", "--stringBuilder-- " + clsStock.getIN());
                    }
                    stringBuilder.append("</tbody>");
                    stringBuilder.append("<tfoot>");//start footer row
                    stringBuilder.append("<tr>");//start footer row


                    stringBuilder.append("<td colspan=\"10\" align = \"center\">TOTAL</td>");//empty cell for sr no


                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append(ClsGlobal.round(_totalAvgStockVal, 2));//cell value
                    stringBuilder.append("</td>");//cell end

                    stringBuilder.append("</tr>");//footer row end
                    stringBuilder.append("</tfoot>");//start footer
                    stringBuilder.append("</table>");
                } else {
                    stringBuilder.append("<table class=\"table\"><tr><td align=\"left\"><span style=\"color: red;\">No records found!</span></td></tr></table>");
                }

                Log.e("--MIS--", "--stringBuilder-- " + stringBuilder);
            } catch(Exception e){
                Log.e("ClsCategory", "GetObjectttt" + e.getMessage());
                e.getMessage();
            }
            return stringBuilder.toString();
        }

        @SuppressLint("WrongConstant")
        public static String getPaymentMIS (Context context, String _where,boolean isMonthMIS){
            StringBuilder stringBuilder = new StringBuilder();
            List<ClsPaymentMaster> lstClsPaymentMasters = new ArrayList<>();
            try {

                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

                String strSql = "SELECT "
                        .concat("[PaymentDate]")
                        .concat(",[MobileNo]")
                        .concat(",[InvoiceNo]")
                        .concat(",[CustomerName]")
                        .concat(",[VendorName]")
                        .concat(",[PaymentDetail]")
                        .concat(",[Type]")//Customer OR Vendor
                        .concat(",[Amount]")
                        .concat(",[PaymentMode]")
                        .concat(",[Remark]")
                        .concat(" FROM [PaymentMaster] WHERE 1=1 ")
                        .concat(_where)
                        .concat(" ORDER BY [PaymentDate]");

                Log.e("--Select--", "--Payment--" + strSql);
                Cursor cur = db.rawQuery(strSql, null);
                Log.e("--Select--", "--Payment--" + cur.getCount());

                int sr = 0;

                while (cur.moveToNext()) {
                    ClsPaymentMaster Obj = new ClsPaymentMaster();

                    Obj.setPaymentDate(cur.getString(cur.getColumnIndex("PaymentDate")));
                    Obj.setMobileNo(cur.getString(cur.getColumnIndex("MobileNo")));
                    Obj.setInvoiceNo(cur.getString(cur.getColumnIndex("InvoiceNo")));
                    Obj.setCustomerName(cur.getString(cur.getColumnIndex("CustomerName")));
                    Obj.setVendorName(cur.getString(cur.getColumnIndex("VendorName")));
                    Obj.setType(cur.getString(cur.getColumnIndex("Type")));
                    Obj.setAmount(cur.getDouble(cur.getColumnIndex("Amount")));
                    Obj.setPaymentMode(cur.getString(cur.getColumnIndex("PaymentMode")));
                    Obj.setPaymentDetail(cur.getString(cur.getColumnIndex("PaymentDetail")));
                    Obj.setRemark(cur.getString(cur.getColumnIndex("Remark")));
                    lstClsPaymentMasters.add(Obj);
                }

                Gson gson = new Gson();
                String jsonInString = gson.toJson(lstClsPaymentMasters);
                Log.e("--Payment--", "jsonInString:--- " + jsonInString);

                db.close();
                Double _totalIn = 0.0;
                Double _totalOut = 0.0;

                if (lstClsPaymentMasters != null && lstClsPaymentMasters.size() != 0) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("<table class=\"table\">");
                    stringBuilder.append("<thead>");
                    stringBuilder.append("<tr>");

                    stringBuilder.append("<th style=\"white-space: normal !important;\">Sr#</th>");

                    if (isMonthMIS == true) {
                        stringBuilder.append("<th style=\"white-space: normal !important;\">Date</th>");
                    }

                    stringBuilder.append("<th style=\"white-space: normal !important;\">Type</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Name</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Mobile No.</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Invoice No.</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Detail</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Mode</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">In Amount</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Out Amount</th>");

                    stringBuilder.append("</tr>");//cell end
                    stringBuilder.append("</thead>");//cell end

                    stringBuilder.append("<tbody>");//new ROW st
                    Double _totalAmount = 0.0;
                    for (ClsPaymentMaster clsPaymentMaster : lstClsPaymentMasters) {
                        sr++;
                        stringBuilder.append("<tr>");//new ROW start

                        stringBuilder.append("<td>");//cell start
                        stringBuilder.append(sr);//cell value
                        stringBuilder.append("</td>");//cell end

                        if (isMonthMIS == true) {
                            stringBuilder.append("<td>");//cell start
                            stringBuilder.append(ClsGlobal.getDDMMYYYY(ClsGlobal.getDbDateFormat(clsPaymentMaster.getPaymentDate())));//cell value
                            stringBuilder.append("</td>");//cell end
                        }


                        stringBuilder.append("<td>");//cell start
                        stringBuilder.append(clsPaymentMaster.getType());//cell value
                        stringBuilder.append("</td>");//cell end


                        if (clsPaymentMaster.getType().equalsIgnoreCase("Customer")) {
                            stringBuilder.append("<td>");//cell start
                            stringBuilder.append(clsPaymentMaster.getCustomerName());//cell value
                            stringBuilder.append("</td>");//cell end
                        } else {
                            stringBuilder.append("<td>");//cell start
                            stringBuilder.append(clsPaymentMaster.getVendorName());//cell value
                            stringBuilder.append("</td>");//cell end
                        }


                        stringBuilder.append("<td align = \"right\">");//cell start
                        stringBuilder.append(clsPaymentMaster.getMobileNo());//cell value
                        stringBuilder.append("</td>");//cell end

                        stringBuilder.append("<td align = \"right\">");//cell start
                        stringBuilder.append(clsPaymentMaster.getInvoiceNo());//cell value
                        stringBuilder.append("</td>");//cell end

                        //6th cell
                        stringBuilder.append("<td>");//cell start
                        stringBuilder.append(clsPaymentMaster.getPaymentDetail() + clsPaymentMaster.getRemark());//cell value
                        stringBuilder.append("</td>");//cell end

                        //7th cell
                        stringBuilder.append("<td align = \"right\">");//cell start
                        stringBuilder.append(clsPaymentMaster.getPaymentMode());//cell value
                        stringBuilder.append("</td>");//cell end

                        _totalAmount += clsPaymentMaster.getAmount();

                  /*  //8th cell
                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append(ClsGlobal.round(clsPaymentMaster.getAmount(), 2));//cell value
                    stringBuilder.append("</td>");//cell end*/


                        if (clsPaymentMaster.getType().equalsIgnoreCase("Customer")) {


                            _totalIn += clsPaymentMaster.getAmount();

                            stringBuilder.append("<td align = \"right\">");//cell start
                            stringBuilder.append(ClsGlobal.round(clsPaymentMaster.getAmount(), 2));//cell value
                            stringBuilder.append("</td>");//cell end
                        } else {
                            stringBuilder.append("<td align = \"right\">");//cell start
                            stringBuilder.append(0.0);//cell value
                            stringBuilder.append("</td>");//cell end
                        }


                        if (clsPaymentMaster.getType().equalsIgnoreCase("Vendor")) {
                            _totalOut += clsPaymentMaster.getAmount();

                            stringBuilder.append("<td align = \"right\">");//cell start
                            stringBuilder.append(ClsGlobal.round(clsPaymentMaster.getAmount(), 2));//cell value
                            stringBuilder.append("</td>");//cell end
                        } else {
                            stringBuilder.append("<td align = \"right\">");//cell start
                            stringBuilder.append(0.0);//cell value
                            stringBuilder.append("</td>");//cell end
                        }


                        stringBuilder.append("</tr>");//cell end
                    }
                    stringBuilder.append("</tbody>");
                    stringBuilder.append("<tfoot>");//start footer row
                    stringBuilder.append("<tr>");//start footer row

                    if (isMonthMIS == true) {
                        stringBuilder.append("<td colspan=\"8\" align = \"left\">TOTAL</td>");//empty cell for sr no
                    } else {
                        stringBuilder.append("<td colspan=\"7\" align = \"left\">TOTAL</td>");//empty cell for sr no
                    }

              /*  stringBuilder.append("<td align = \"right\">");//cell start
                stringBuilder.append(ClsGlobal.round(_totalAmount, 2));//cell value
                stringBuilder.append("</td>");//cell end
*/

                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append(ClsGlobal.round(_totalIn, 2));//cell value
                    stringBuilder.append("</td>");//cell end

                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append(ClsGlobal.round(_totalOut, 2));//cell value
                    stringBuilder.append("</td>");//cell end

                    stringBuilder.append("</tr>");//footer row end
                    stringBuilder.append("</tfoot>");//start footer
                    stringBuilder.append("</table>");
                } else {
                    stringBuilder.append("<table class=\"table\"><tr><td align=\"left\"><span style=\"color: red;\">No records found!</span></td></tr></table>");
                }

                Log.e("--MIS--", "--stringBuilder-- " + stringBuilder);
            } catch (Exception e) {
                Log.e("ClsCategory", "GetObjectttt" + e.getMessage());
                e.getMessage();
            }
            return stringBuilder.toString();
        }

        @SuppressLint("WrongConstant")
        public static String getPaymentMIS_In_Out (Context context, String _where){
            StringBuilder stringBuilder = new StringBuilder();
            List<ClsPaymentMaster> lstClsPaymentMasters = new ArrayList<>();
            try {

                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

                String strSql = "SELECT "
                        .concat("[PaymentDate]")
                        .concat(",[MobileNo]")
                        .concat(",[InvoiceNo]")
                        .concat(",[CustomerName]")
                        .concat(",[VendorName]")
                        .concat(",[PaymentDetail]")
                        .concat(",[Type]")//Customer OR Vendor
                        .concat(",[Amount]")
                        .concat(",[PaymentMode]")
                        .concat(",[Remark]")
                        .concat(" FROM [PaymentMaster] WHERE 1=1 ")
                        .concat(_where)
                        .concat(" ORDER BY [PaymentDate]");

                Log.e("--Select--", "--Payment--" + strSql);
                Cursor cur = db.rawQuery(strSql, null);
                Log.e("--Select--", "--Payment--" + cur.getCount());

                int sr = 0;

                while (cur.moveToNext()) {
                    ClsPaymentMaster Obj = new ClsPaymentMaster();

                    Obj.setPaymentDate(cur.getString(cur.getColumnIndex("PaymentDate")));
                    Obj.setMobileNo(cur.getString(cur.getColumnIndex("MobileNo")));
                    Obj.setInvoiceNo(cur.getString(cur.getColumnIndex("InvoiceNo")));
                    Obj.setCustomerName(cur.getString(cur.getColumnIndex("CustomerName")));
                    Obj.setVendorName(cur.getString(cur.getColumnIndex("VendorName")));
                    Obj.setType(cur.getString(cur.getColumnIndex("Type")));
                    Obj.setAmount(cur.getDouble(cur.getColumnIndex("Amount")));
                    Obj.setPaymentMode(cur.getString(cur.getColumnIndex("PaymentMode")));
                    Obj.setPaymentDetail(cur.getString(cur.getColumnIndex("PaymentDetail")));
                    Obj.setRemark(cur.getString(cur.getColumnIndex("Remark")));
                    lstClsPaymentMasters.add(Obj);
                }

                Gson gson = new Gson();
                String jsonInString = gson.toJson(lstClsPaymentMasters);
                Log.e("--Payment--", "jsonInString:--- " + jsonInString);

                db.close();

                if (lstClsPaymentMasters != null && lstClsPaymentMasters.size() != 0) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("<table class=\"table\">");
                    stringBuilder.append("<thead>");
                    stringBuilder.append("<tr>");

                    stringBuilder.append("<th style=\"white-space: normal !important;\">Sr#</th>");


                    stringBuilder.append("<th style=\"white-space: normal !important;\">Date</th>");


                    stringBuilder.append("<th style=\"white-space: normal !important;\">In</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Out</th>");


                    stringBuilder.append("</tr>");//cell end
                    stringBuilder.append("</thead>");//cell end

                    stringBuilder.append("<tbody>");//new ROW st
                    Double _totalAmount = 0.0;

                    Double _Footerbalance = 0.0;
                    Double _Rowbalance = 0.0;

                    Double _totalIn = 0.0;
                    Double _totalOut = 0.0;


                    for (ClsPaymentMaster clsPaymentMaster : lstClsPaymentMasters) {
                        sr++;
                        stringBuilder.append("<tr>");//new ROW start

                        stringBuilder.append("<td>");//cell start
                        stringBuilder.append(sr);//cell value
                        stringBuilder.append("</td>");//cell end


                        stringBuilder.append("<td>");//cell start
                        stringBuilder.append(ClsGlobal.getDDMMYYYY(ClsGlobal.getDbDateFormat(clsPaymentMaster.getPaymentDate())));//cell value
                        stringBuilder.append("</td>");//cell end


                        if (clsPaymentMaster.getType().equalsIgnoreCase("Customer")) {


                            _totalIn += clsPaymentMaster.getAmount();

                            stringBuilder.append("<td align = \"right\">");//cell start
                            stringBuilder.append(ClsGlobal.round(clsPaymentMaster.getAmount(), 2));//cell value
                            stringBuilder.append("</td>");//cell end
                        } else {
                            stringBuilder.append("<td align = \"right\">");//cell start
                            stringBuilder.append(0.0);//cell value
                            stringBuilder.append("</td>");//cell end
                        }


                        if (clsPaymentMaster.getType().equalsIgnoreCase("Vendor")) {
                            _totalOut += clsPaymentMaster.getAmount();

                            stringBuilder.append("<td align = \"right\">");//cell start
                            stringBuilder.append(ClsGlobal.round(clsPaymentMaster.getAmount(), 2));//cell value
                            stringBuilder.append("</td>");//cell end
                        } else {
                            stringBuilder.append("<td align = \"right\">");//cell start
                            stringBuilder.append(0.0);//cell value
                            stringBuilder.append("</td>");//cell end
                        }

                    }
                    stringBuilder.append("</tbody>");
                    stringBuilder.append("<tfoot>");//start footer row
                    stringBuilder.append("<tr>");//start footer row


                    stringBuilder.append("<td align = \"left\">TOTAL</td>");//empty cell for sr no


                    stringBuilder.append("<td>");//cell start
                    stringBuilder.append("</td>");//cell end


                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append(ClsGlobal.round(_totalIn, 2));//cell value
                    stringBuilder.append("</td>");//cell end

                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append(ClsGlobal.round(_totalOut, 2));//cell value
                    stringBuilder.append("</td>");//cell end

                    stringBuilder.append("</tr>");//footer row end


                    stringBuilder.append("<tr>");//start footer row


                    //2nd footer ROW
                    _Footerbalance = _totalIn - _totalOut;
                    stringBuilder.append("<td align =\"center\">BALANCE</td>");//empty cell for sr no

                    stringBuilder.append("<td>");//cell start
                    stringBuilder.append("</td>");//cell end


                    stringBuilder.append("<td colspan=\"2\"  align = \"right\">");//cell start
                    stringBuilder.append(String.valueOf(ClsGlobal.round(_Footerbalance, 2)));//cell value
                    stringBuilder.append("</td>");//cell end

                    stringBuilder.append("</tr>");//footer row end


                    stringBuilder.append("</tfoot>");//start footer
                    stringBuilder.append("</table>");
                } else {
                    stringBuilder.append("<table class=\"table\"><tr><td align=\"left\"><span style=\"color: red;\">No records found!</span></td></tr></table>");
                }

                Log.e("--MIS--", "--stringBuilder-- " + stringBuilder);
            } catch (Exception e) {
                Log.e("ClsCategory", "GetObjectttt" + e.getMessage());
                e.getMessage();
            }
            return stringBuilder.toString();
        }

        @SuppressLint("WrongConstant")
        public static String getExpenseMIS (String _where, Context context){
            Log.e("getExpenseMIS", "getExpenseMIS call");
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

                if (list != null && list.size() != 0) {
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

                } else {
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


        @SuppressLint("WrongConstant")
        public static String getSaleMis (String _where, Context context,boolean isMonthMIS){
            StringBuilder stringBuilder = new StringBuilder();
            List<ClsStockSale> lstClsStockSales = new ArrayList<>();
            try {

                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

                String strSql = "SELECT "
                        .concat(" ord.[OrderNo]")
                        .concat(" ,ord.[MobileNo]")
                        .concat(" ,ord.[CustomerName]")
                        .concat(" ,ord.[BillDate]")
                        .concat(" ,ITM.[ITEM_CODE]")
                        .concat(" ,ITM.[ITEM_NAME]")
                        .concat(" ,dtl.[SaleRate]")
                        .concat(" ,dtl.[Quantity]")
                        .concat(" ,dtl.[Amount]")
                        .concat(" ,dtl.[TotalTaxAmount]")
                        .concat(" ,dtl.[GrandTotal]")

                        .concat(" FROM [InventoryOrderMaster] AS [ord] ")
                        .concat(" INNER JOIN [InventoryOrderDetail] AS [dtl] ON [dtl].[OrderID] = [ord].[OrderID] ")
                        .concat(" INNER JOIN [tbl_LayerItem_Master] AS [ITM] ON [ITM].[ITEM_CODE] = [dtl].[ItemCode] ")
                        .concat(" WHERE 1=1 ")
                        .concat(_where)
                        //  .concat(" ORDER BY ITM.[ITEM_NAME] ")
                        .concat(" ORDER BY ord.[OrderNo] DESC");


                Log.e("--Select--", "--Sale--" + strSql);
                Cursor cur = db.rawQuery(strSql, null);
                Log.e("--Select--", "--Sale--" + cur.getCount());

                int sr = 0;

                while (cur.moveToNext()) {
                    ClsStockSale Obj = new ClsStockSale();

                    Obj.setOrderNo(cur.getInt(cur.getColumnIndex("OrderNo")));
                    Obj.setMobileNo(cur.getString(cur.getColumnIndex("MobileNo")));
                    Obj.setBillDate(cur.getString(cur.getColumnIndex("BillDate")));
                    Obj.setCustomerName(cur.getString(cur.getColumnIndex("CustomerName")));
                    Obj.setItemCode(cur.getString(cur.getColumnIndex("ITEM_CODE")));
                    Obj.setItemName(cur.getString(cur.getColumnIndex("ITEM_NAME")));
                    Obj.setSaleRate(cur.getDouble(cur.getColumnIndex("SaleRate")));
                    Obj.setQuantity(cur.getDouble(cur.getColumnIndex("Quantity")));
                    Obj.setAmount(cur.getDouble(cur.getColumnIndex("Amount")));
                    Obj.setTotalTaxAmount(cur.getDouble(cur.getColumnIndex("TotalTaxAmount")));
                    Obj.setGrandTotal(cur.getDouble(cur.getColumnIndex("GrandTotal")));
                    lstClsStockSales.add(Obj);

                }

                Gson gson = new Gson();
                String jsonInString = gson.toJson(lstClsStockSales);
                Log.e("Step", "jsonInString:--- " + jsonInString);

                db.close();

                if (lstClsStockSales != null && lstClsStockSales.size() != 0) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("<table class=\"table\">");
                    stringBuilder.append("<thead>");
                    stringBuilder.append("<tr>");

                    stringBuilder.append("<th style=\"white-space: normal !important;\">Sr#</th>");

                    if (isMonthMIS == true) {
                        stringBuilder.append("<th style=\"white-space: normal !important;\">Date</th>");
                    }

                    stringBuilder.append("<th style=\"white-space: normal !important;\">Order No</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Mobile No</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Customer Name</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Item Code</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Item</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Rate</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Quantity</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Net Amount</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Tax Amount</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Grand Total</th>");

                    stringBuilder.append("</tr>");//cell end
                    stringBuilder.append("</thead>");//cell end

                    Double _totalStock = 0.0;
                    Double _netAmout = 0.0;
                    Double _taxAmount = 0.0;
                    Double _grandTotal = 0.0;


                    stringBuilder.append("<tbody>");//new ROW st

                    for (ClsStockSale clsStockSale : lstClsStockSales) {
                        sr++;
                        stringBuilder.append("<tr>");//new ROW start

                        //1st cell
                        stringBuilder.append("<td>");//cell start
                        stringBuilder.append(sr);//cell value
                        stringBuilder.append("</td>");//cell end

                        if (isMonthMIS == true) {
                            //2st cell
                            stringBuilder.append("<td>");//cell start
                            stringBuilder.append(ClsGlobal.getDDMYYYYFormat(clsStockSale.getBillDate()));//cell value
                            stringBuilder.append("</td>");//cell end
                        }


                        //2st cell
                        stringBuilder.append("<td>");//cell start
                        stringBuilder.append(clsStockSale.getOrderNo());//cell value
                        stringBuilder.append("</td>");//cell end

                        //3rd cell
                        stringBuilder.append("<td>");//cell start
                        stringBuilder.append(clsStockSale.getMobileNo());//cell value
                        stringBuilder.append("</td>");//cell end

                        //3rd cell
                        stringBuilder.append("<td>");//cell start
                        stringBuilder.append(clsStockSale.getCustomerName());//cell value
                        stringBuilder.append("</td>");//cell end

                        //3rd cell
                        stringBuilder.append("<td>");//cell start
                        stringBuilder.append(clsStockSale.getItemCode());//cell value
                        stringBuilder.append("</td>");//cell end

                        //3rd cell
                        stringBuilder.append("<td>");//cell start
                        stringBuilder.append(clsStockSale.getItemName());//cell value
                        stringBuilder.append("</td>");//cell end

                        //4th cell
                        stringBuilder.append("<td align = \"right\">");//cell start
                        stringBuilder.append(ClsGlobal.round(clsStockSale.getSaleRate(), 2));//cell value
                        stringBuilder.append("</td>");//cell end

                        //5th cell
                        stringBuilder.append("<td align = \"right\">");//cell start
                        stringBuilder.append(ClsGlobal.round(clsStockSale.getQuantity(), 2));//cell value
                        stringBuilder.append("</td>");//cell end

                        //6th cell
                        stringBuilder.append("<td align = \"right\">");//cell start
                        stringBuilder.append(ClsGlobal.round(clsStockSale.getAmount(), 2));//cell value
                        stringBuilder.append("</td>");//cell end

                        //7th cell
                        stringBuilder.append("<td align = \"right\">");//cell start

                        stringBuilder.append(ClsGlobal.round(clsStockSale.getTotalTaxAmount(), 2));//cell value
                        stringBuilder.append("</td>");//cell end

                        //8th cell
                        stringBuilder.append("<td align = \"right\">");//cell start
                        stringBuilder.append(ClsGlobal.round(clsStockSale.getGrandTotal(), 2));//cell value
                        stringBuilder.append("</td>");//cell end
                        stringBuilder.append("</tr>");//cell end

                        _totalStock += clsStockSale.getQuantity();
                        _netAmout += clsStockSale.getAmount();
                        _taxAmount += clsStockSale.getTotalTaxAmount();
                        _grandTotal += clsStockSale.getGrandTotal();

                    }
                    stringBuilder.append("</tbody>");


                    stringBuilder.append("<tfoot>");//start footer row
                    //  cell 6
                    stringBuilder.append("<tr>");//start footer row

                    stringBuilder.append("<td>");//empty cell for sr no
                    stringBuilder.append("</td>");//empty cell for sr no


                    stringBuilder.append("<td>");//empty cell for sr no
                    stringBuilder.append("</td>");//empty cell for sr no

                    stringBuilder.append("<td>");//empty cell for sr no
                    stringBuilder.append("</td>");//empty cell for sr no

                    stringBuilder.append("<td>");//empty cell for sr no
                    stringBuilder.append("</td>");//empty cell for sr no

                    stringBuilder.append("<td>");//empty cell for sr no
                    stringBuilder.append("</td>");//empty cell for sr no

                    stringBuilder.append("<td>");//empty cell for sr no
                    stringBuilder.append("</td>");//empty cell for sr no

                    if (isMonthMIS == true) {

                        stringBuilder.append("<td>");//empty cell for sr no
                        stringBuilder.append("</td>");//empty cell for sr no
                    }

                    stringBuilder.append("<td>TOTAL</td>");//empty cell for sr no
                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append(_totalStock);//cell value
                    stringBuilder.append("</td>");//cell end

                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append(_netAmout);//cell value
                    stringBuilder.append("</td>");//cell end

                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append(_taxAmount);//cell value
                    stringBuilder.append("</td>");//cell end

                    stringBuilder.append("<td align = \"right\">");//cell start
                    stringBuilder.append(_grandTotal);//cell value
                    stringBuilder.append("</td>");//cell end

                    stringBuilder.append("</tr>");//footer row end
                    stringBuilder.append("</tfoot>");//start footer
                    stringBuilder.append("</table>");
                } else {
                    stringBuilder.append("<table class=\"table\"><tr><td align=\"left\"><span style=\"color: red;\">No records found!</span></td></tr></table>");
                }

                Log.e("--MIS--", "--stringBuilder-- " + stringBuilder);
            } catch (Exception e) {
                Log.e("ClsCategory", "GetObjectttt" + e.getMessage());
                e.getMessage();
            }
            return stringBuilder.toString();
        }

        @SuppressLint("WrongConstant")
        public static String getPurchaseMIS (String _where, Context context,boolean isMonthMIS){
            StringBuilder stringBuilder = new StringBuilder();
            List<ClsPurchaseMisGetSet> lstClsPurchaseMisGetSets = new ArrayList<>();
            try {

                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

                String strSql = "SELECT PD.*,ITM.[ITEM_NAME],PM.[PurchaseDate],PM.[BillNO],PM.[PurchaseNo],V.[CONTACT_NO],V.[VENDOR_NAME] FROM [PurchaseDetail] AS PD "
                        .concat(" INNER JOIN [PurchaseMaster] AS PM ON PM.[PurchaseID] = PD.[PurchaseID] ")
                        .concat(" INNER JOIN [VENDOR_MASTER] AS V ON V.[VENDOR_ID] = PM.[VendorID] ")
                        .concat(" INNER JOIN [tbl_LayerItem_Master] AS ITM ON ITM.[LAYERITEM_ID] = PD.[ItemID] ")
                        .concat(" WHERE 1=1 ")
                        .concat(_where)
                        .concat(";");


                Log.e("--Purchase--", "--QRY--" + strSql);
                Cursor cur = db.rawQuery(strSql, null);
                Log.e("--Purchase--", "--COUNT--" + cur.getCount());

                int sr = 0;

                while (cur.moveToNext()) {
                    ClsPurchaseMisGetSet Obj = new ClsPurchaseMisGetSet();

                    Obj.setItemID(cur.getInt(cur.getColumnIndex("ItemID")));
                    Obj.setItemCode(cur.getString(cur.getColumnIndex("ItemCode")));
                    Obj.setItemName(cur.getString(cur.getColumnIndex("ITEM_NAME")));

                    Obj.setMonthYear(cur.getString(cur.getColumnIndex("MonthYear")));
                    Obj.setUnit(cur.getString(cur.getColumnIndex("Unit")));
                    Obj.setQuantity(cur.getDouble(cur.getColumnIndex("Quantity")));
                    Obj.setRate(cur.getDouble(cur.getColumnIndex("Rate")));
                    Obj.setTotalAmount(cur.getDouble(cur.getColumnIndex("TotalAmount")));
                    Obj.setDiscount(cur.getDouble(cur.getColumnIndex("Discount")));
                    Obj.setNetAmount(cur.getDouble(cur.getColumnIndex("NetAmount")));
                    Obj.setApplyTax(cur.getString(cur.getColumnIndex("ApplyTax")));
                    Obj.setCGST(cur.getDouble(cur.getColumnIndex("CGST")));
                    Obj.setSGST(cur.getDouble(cur.getColumnIndex("SGST")));
                    Obj.setIGST(cur.getDouble(cur.getColumnIndex("IGST")));
                    Obj.setTotalTaxAmount(cur.getDouble(cur.getColumnIndex("TotalTaxAmount")));
                    Obj.setGrandTotal(cur.getDouble(cur.getColumnIndex("GrandTotal")));
                    Obj.setPurchaseDate(cur.getString(cur.getColumnIndex("PurchaseDate")));
                    Obj.setBillNO(cur.getString(cur.getColumnIndex("BillNO")));
                    Obj.setPurchaseNo(cur.getInt(cur.getColumnIndex("PurchaseNo")));
                    Obj.setVendorName(cur.getString(cur.getColumnIndex("VENDOR_NAME")));
                    Obj.setVendorContactNo(cur.getString(cur.getColumnIndex("CONTACT_NO")));

                    lstClsPurchaseMisGetSets.add(Obj);

                }

                Gson gson = new Gson();
                String jsonInString = gson.toJson(lstClsPurchaseMisGetSets);
                Log.e("--Purchase--", "GsonPuchase:--- " + jsonInString);

                db.close();

                if (lstClsPurchaseMisGetSets != null && lstClsPurchaseMisGetSets.size() != 0) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("<table class=\"table\">");
                    stringBuilder.append("<thead>");
                    stringBuilder.append("<tr>");

                    stringBuilder.append("<th style=\"white-space: normal !important;\">Sr#</th>");

                    if (isMonthMIS == true) {
                        stringBuilder.append("<th style=\"white-space: normal !important;\">Purchase Date</th>");
                    }

                    stringBuilder.append("<th style=\"white-space: normal !important;\">Vendor Name</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Contact</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Item</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Code</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Bill NO</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Purchase No</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Unit</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Quantity</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Rate</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Discount</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Total Amount</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Net Amount</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Apply Tax</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Tax Amount</th>");
                    stringBuilder.append("<th style=\"white-space: normal !important;\">Grand Total</th>");

                    stringBuilder.append("</tr>");//cell end
                    stringBuilder.append("</thead>");//cell end

                    Double _totalStock = 0.0;
                    Double _netAmout = 0.0;
                    Double _taxAmount = 0.0;
                    Double _grandTotal = 0.0;
                    Double _totalRate = 0.0;
                    Double _totalAmount = 0.0;
                    Double _totalDiscount = 0.0;


                    stringBuilder.append("<tbody>");//new ROW st

                    for (ClsPurchaseMisGetSet clsPurchaseMisGetSet : lstClsPurchaseMisGetSets) {
                        sr++;
                        stringBuilder.append("<tr>");//new ROW start

                        //1st cell
                        stringBuilder.append("<td>");//cell start
                        stringBuilder.append(sr);//cell value
                        stringBuilder.append("</td>");//cell end

                        if (isMonthMIS == true) {
                            //2st cell
                            stringBuilder.append("<td  style=\"white-space: normal !important;\" align = \"center\">");//cell start
                            stringBuilder.append(ClsGlobal.getDDMMYYYY(clsPurchaseMisGetSet.getPurchaseDate()));
                            stringBuilder.append("</td>");//cell end
                        }

                        //2st cell
                        stringBuilder.append("<td  style=\"white-space: normal !important;\" align = \"center\">");//cell start
                        stringBuilder.append(clsPurchaseMisGetSet.getVendorName());//cell value
                        stringBuilder.append("</td>");//cell end

                        stringBuilder.append("<td  style=\"white-space: normal !important;\" align = \"center\">");//cell start
                        stringBuilder.append(clsPurchaseMisGetSet.getVendorContactNo());//cell value
                        stringBuilder.append("</td>");//cell end

                        stringBuilder.append("<td  style=\"white-space: normal !important;\" align = \"center\">");//cell start
                        stringBuilder.append(clsPurchaseMisGetSet.getItemName());//cell value
                        stringBuilder.append("</td>");//cell end

                        stringBuilder.append("<td  style=\"white-space: normal !important;\" align = \"center\">");//cell start
                        stringBuilder.append(clsPurchaseMisGetSet.getItemCode());//cell value
                        stringBuilder.append("</td>");//cell end

                        stringBuilder.append("<td  style=\"white-space: normal !important;\" align = \"center\">");//cell start
                        stringBuilder.append(clsPurchaseMisGetSet.getBillNO());//cell value
                        stringBuilder.append("</td>");//cell end

                        stringBuilder.append("<td  style=\"white-space: normal !important;\" align = \"center\">");//cell start
                        stringBuilder.append(clsPurchaseMisGetSet.getPurchaseNo());//cell value
                        stringBuilder.append("</td>");//cell end

                        stringBuilder.append("<td  style=\"white-space: normal !important;\" align = \"center\">");//cell start
                        stringBuilder.append(clsPurchaseMisGetSet.getUnit());//cell value
                        stringBuilder.append("</td>");//cell end

                        stringBuilder.append("<td  style=\"white-space: normal !important;\" align = \"center\">");//cell start
                        stringBuilder.append(ClsGlobal.round(clsPurchaseMisGetSet.getQuantity(), 2));//cell value
                        stringBuilder.append("</td>");//cell end

                        stringBuilder.append("<td  style=\"white-space: normal !important;\" align = \"right\">");//cell start
                        stringBuilder.append(ClsGlobal.round(clsPurchaseMisGetSet.getRate(), 2));//cell value
                        stringBuilder.append("</td>");//cell end

                        stringBuilder.append("<td  style=\"white-space: normal !important;\" align = \"right\">");//cell start
                        stringBuilder.append(ClsGlobal.round(clsPurchaseMisGetSet.getDiscount(), 2));//cell value
                        stringBuilder.append("</td>");//cell end

                        stringBuilder.append("<td  style=\"white-space: normal !important;\" align = \"right\">");//cell start
                        stringBuilder.append(ClsGlobal.round(clsPurchaseMisGetSet.getTotalAmount(), 2));//cell value
                        stringBuilder.append("</td>");//cell end

                        stringBuilder.append("<td  style=\"white-space: normal !important;\" align = \"right\">");//cell start
                        stringBuilder.append(ClsGlobal.round(clsPurchaseMisGetSet.getNetAmount(), 2));//cell value
                        stringBuilder.append("</td>");//cell end

                        stringBuilder.append("<td  style=\"white-space: normal !important;\" align = \"center\">");//cell start
                        stringBuilder.append(clsPurchaseMisGetSet.getApplyTax());//cell value
                        stringBuilder.append("</td>");//cell end

                        stringBuilder.append("<td  style=\"white-space: normal !important;\" align = \"right\">");//cell start
                        stringBuilder.append(ClsGlobal.round(clsPurchaseMisGetSet.getTotalTaxAmount(), 2));//cell value
                        stringBuilder.append("</td>");//cell end

                        stringBuilder.append("<td  style=\"white-space: normal !important;\" align = \"right\">");//cell start
                        stringBuilder.append(ClsGlobal.round(clsPurchaseMisGetSet.getGrandTotal(), 2));//cell value
                        stringBuilder.append("</td>");//cell end
                        stringBuilder.append("</tr>");//cell end

                        _totalRate += clsPurchaseMisGetSet.getRate();
                        _totalDiscount += clsPurchaseMisGetSet.getDiscount();
                        _totalAmount += clsPurchaseMisGetSet.getTotalAmount();
                        _totalStock += clsPurchaseMisGetSet.getQuantity();
                        _netAmout += clsPurchaseMisGetSet.getNetAmount();
                        _taxAmount += clsPurchaseMisGetSet.getTotalTaxAmount();
                        _grandTotal += clsPurchaseMisGetSet.getGrandTotal();

                    }
                    stringBuilder.append("</tbody>");
                    stringBuilder.append("<tfoot>");//start footer row
                    stringBuilder.append("<tr>");//start footer row


                    if (isMonthMIS == true) {
                        stringBuilder.append("<td colspan=\"9\" align = \"center\">TOTAL</td>");//empty cell for sr no
                    } else {
                        stringBuilder.append("<td colspan=\"8\" align = \"center\">TOTAL</td>");//empty cell for sr no
                    }


                    stringBuilder.append("<td  style=\"white-space: normal !important;\" align = \"center\">");//cell start
                    stringBuilder.append(_totalStock);//cell value
                    stringBuilder.append("</td>");//cell end

                    stringBuilder.append("<td  style=\"white-space: normal !important;\" align = \"right\">");//cell start
                    stringBuilder.append(_totalRate);//cell value
                    stringBuilder.append("</td>");//cell end

                    stringBuilder.append("<td  style=\"white-space: normal !important;\" align = \"right\">");//cell start
                    stringBuilder.append(_totalDiscount);//cell value
                    stringBuilder.append("</td>");//cell end

                    stringBuilder.append("<td  style=\"white-space: normal !important;\" align = \"right\">");//cell start
                    stringBuilder.append(_totalAmount);//cell value
                    stringBuilder.append("</td>");//cell end

                    stringBuilder.append("<td  style=\"white-space: normal !important;\" align = \"right\">");//cell start
                    stringBuilder.append(_netAmout);//cell value
                    stringBuilder.append("</td>");//cell end

                    stringBuilder.append("<td>");//cell start
                    stringBuilder.append("</td>");//cell end

                    stringBuilder.append("<td  style=\"white-space: normal !important;\" align = \"right\">");//cell start
                    stringBuilder.append(_taxAmount);//cell value
                    stringBuilder.append("</td>");//cell end

                    stringBuilder.append("<td  style=\"white-space: normal !important;\" align = \"right\">");//cell start
                    stringBuilder.append(_grandTotal);//cell value
                    stringBuilder.append("</td>");//cell end

                    stringBuilder.append("</tr>");//footer row end
                    stringBuilder.append("</tfoot>");//start footer
                    stringBuilder.append("</table>");
                } else {
                    stringBuilder.append("<table class=\"table\"><tr><td align=\"left\"><span style=\"color: red;\">No records found!</span></td></tr></table>");
                }

                Log.e("--MIS--", "--stringBuilder-- " + stringBuilder);
            } catch (Exception e) {
                Log.e("ClsCategory", "GetObjectttt" + e.getMessage());
                e.getMessage();
            }
            return stringBuilder.toString();
        }


    }
