package com.demo.nspl.restaurantlite.ExportData;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Stock.ClsStock;

import java.util.ArrayList;
import java.util.List;

public class ClsExportStockReportData {

    String _item = "";
    String _unit = "";
    double min = 0.0;
    double max = 0.0;
    double avgPurchaseSaleRate = 0.0;
    double avgSaleRate = 0.0;
    double _in = 0.0;
    double _out = 0.0;
    double _stock = 0.0;
    double avgStockValue = 0.0;

    public String get_item() {
        return _item;
    }

    public void set_item(String _item) {
        this._item = _item;
    }

    public String get_unit() {
        return _unit;
    }

    public void set_unit(String _unit) {
        this._unit = _unit;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getAvgPurchaseSaleRate() {
        return avgPurchaseSaleRate;
    }

    public void setAvgPurchaseSaleRate(double avgPurchaseSaleRate) {
        this.avgPurchaseSaleRate = avgPurchaseSaleRate;
    }

    public double getAvgSaleRate() {
        return avgSaleRate;
    }

    public void setAvgSaleRate(double avgSaleRate) {
        this.avgSaleRate = avgSaleRate;
    }

    public double get_in() {
        return _in;
    }

    public void set_in(double _in) {
        this._in = _in;
    }

    public double get_out() {
        return _out;
    }

    public void set_out(double _out) {
        this._out = _out;
    }

    public double get_stock() {
        return _stock;
    }

    public void set_stock(double _stock) {
        this._stock = _stock;
    }

    public double getAvgStockValue() {
        return avgStockValue;
    }

    public void setAvgStockValue(double avgStockValue) {
        this.avgStockValue = avgStockValue;
    }

    private static SQLiteDatabase db;

    @SuppressLint("WrongConstant")
    public static List<ClsExportStockReportData> getStockList(Context context) {

        List<ClsExportStockReportData> list = new ArrayList<>();
        List<ClsStock> lstClsPurchaseDetails = new ArrayList<>();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            Log.e("--Single--", "------------------------------------");
            Log.e("--Single--", "Step:1");

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


            Cursor cur = db.rawQuery(strSql, null);

            Log.e("--Payment--", "Qry: " + strSql);
            Log.e("--Payment--", "curCount:" + cur.getCount());

            while (cur.moveToNext()) {
                ClsExportStockReportData getSet = new ClsExportStockReportData();
                getSet.set_item(cur.getString(cur.getColumnIndex("ITEM_NAME")));
                getSet.set_unit(cur.getString(cur.getColumnIndex("UNIT_CODE")));
                getSet.setMin(cur.getDouble(cur.getColumnIndex("MIN_STOCK")));
                getSet.setMax(cur.getDouble(cur.getColumnIndex("MAX_STOCK")));
                getSet.set_in(cur.getDouble(cur.getColumnIndex("IN")));
                getSet.set_out(cur.getDouble(cur.getColumnIndex("OUT")));
                getSet.setAvgPurchaseSaleRate(cur.getDouble(cur.getColumnIndex("AveragePurchaseRate")));
                getSet.setAvgSaleRate(cur.getDouble(cur.getColumnIndex("AverageSaleRate")));

                list.add(getSet);
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
                }
            }


        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

        return list;
    }


}
