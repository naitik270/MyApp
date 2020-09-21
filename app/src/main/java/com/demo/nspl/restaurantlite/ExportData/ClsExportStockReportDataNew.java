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

import static com.demo.nspl.restaurantlite.classes.ClsLayerItemMaster.getLayerValuesByItemId;
import static com.demo.nspl.restaurantlite.classes.ClsLayerItemMaster.getTagsByItemIdList;

public class ClsExportStockReportDataNew {


    String ITEM_NAME = "";
    String ITEM_CODE = "";
    Double MIN_STOCK = 0.0;
    Double MAX_STOCK = 0.0;
    String UNIT_CODE = "";
    Double IN = 0.0;
    Double OUT = 0.0;
    Double OPENING_STOCK = 0.0;
    Double AverageSaleRate = 0.0;
    Double AveragePurchaseRate = 0.0;

    public String getITEM_NAME() {
        return ITEM_NAME;
    }

    public void setITEM_NAME(String ITEM_NAME) {
        this.ITEM_NAME = ITEM_NAME;
    }

    public String getITEM_CODE() {
        return ITEM_CODE;
    }

    public void setITEM_CODE(String ITEM_CODE) {
        this.ITEM_CODE = ITEM_CODE;
    }

    public Double getMIN_STOCK() {
        return MIN_STOCK;
    }

    public void setMIN_STOCK(Double MIN_STOCK) {
        this.MIN_STOCK = MIN_STOCK;
    }

    public Double getMAX_STOCK() {
        return MAX_STOCK;
    }

    public void setMAX_STOCK(Double MAX_STOCK) {
        this.MAX_STOCK = MAX_STOCK;
    }

    public String getUNIT_CODE() {
        return UNIT_CODE;
    }

    public void setUNIT_CODE(String UNIT_CODE) {
        this.UNIT_CODE = UNIT_CODE;
    }

    public Double getIN() {
        return IN;
    }

    public void setIN(Double IN) {
        this.IN = IN;
    }

    public Double getOUT() {
        return OUT;
    }

    public void setOUT(Double OUT) {
        this.OUT = OUT;
    }

    public Double getOPENING_STOCK() {
        return OPENING_STOCK;
    }

    public void setOPENING_STOCK(Double OPENING_STOCK) {
        this.OPENING_STOCK = OPENING_STOCK;
    }

    public Double getAverageSaleRate() {
        return AverageSaleRate;
    }

    public void setAverageSaleRate(Double averageSaleRate) {
        AverageSaleRate = averageSaleRate;
    }

    public Double getAveragePurchaseRate() {
        return AveragePurchaseRate;
    }

    public void setAveragePurchaseRate(Double averagePurchaseRate) {
        AveragePurchaseRate = averagePurchaseRate;
    }

    private static SQLiteDatabase db;

    @SuppressLint("WrongConstant")
    static List<ClsStock> getStockList(String _where, Context context) {
        List<ClsStock> lstClsPurchaseDetails = new ArrayList<>();
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String strSql = "SELECT "
                    .concat("[ITM].[ITEM_NAME]")
                    .concat(",[ITM].[ITEM_CODE]")
                    .concat(",[ITM].[LAYERITEM_ID]")
                    .concat(",[ITM].[MIN_STOCK]")
                    .concat(",[ITM].[MAX_STOCK]")
                    .concat(",[ITM].[UNIT_CODE]")
                    .concat(",[ITM].[OPENING_STOCK]")

                    .concat(",SUM([PD].[Quantity]) AS [IN] ")
                    .concat(",0 AS [OUT] ")
                    .concat(",AVG(IFNULL([PD].[Rate],0)) AS [AveragePurchaseRate] ")
                    .concat(",0 AS [AverageSaleRate] ")
                    .concat(" FROM [tbl_LayerItem_Master] AS [ITM] ")
                    .concat(" LEFT JOIN [PurchaseDetail] AS [PD] ON [PD].[ItemID] = [ITM].[LAYERITEM_ID] ")
//                    .concat(" LEFT JOIN [InventoryOrderDetail] AS [SALE] ON [SALE].[ItemCode] = [ITM].[ITEM_CODE] ")
                    .concat(" WHERE 1=1 ")
                    .concat(_where)
                    .concat(" ")
                    .concat(" GROUP BY  ")
                    .concat("[ITM].[ITEM_NAME]")
                    .concat(",[ITM].[ITEM_CODE]")
                    .concat(",[ITM].[LAYERITEM_ID]")
                    .concat(",[ITM].[MIN_STOCK]")
                    .concat(",[ITM].[MAX_STOCK]")
                    .concat(",[ITM].[UNIT_CODE]")
                    .concat(" ORDER BY [ITM].[ITEM_NAME]");


            Log.e("--Select--", ">>>>>>>>>" + strSql);
            Cursor cur = db.rawQuery(strSql, null);
            Log.e("--Select--", ">>>>>>>>>" + cur.getCount());

            while (cur.moveToNext()) {
                ClsStock Obj = new ClsStock();
                int _itemID = cur.getInt(cur.getColumnIndex("LAYERITEM_ID"));

                Obj.setITEM_NAME(cur.getString(cur.getColumnIndex("ITEM_NAME")));
                Obj.setITEM_CODE(cur.getString(cur.getColumnIndex("ITEM_CODE")));
                Obj.setMIN_STOCK(cur.getDouble(cur.getColumnIndex("MIN_STOCK")));
                Obj.setMAX_STOCK(cur.getDouble(cur.getColumnIndex("MAX_STOCK")));
                Obj.setUNIT_CODE(cur.getString(cur.getColumnIndex("UNIT_CODE")));
                Obj.setOPENING_STOCK(cur.getDouble(cur.getColumnIndex("OPENING_STOCK")));
                Obj.setIN(cur.getDouble(cur.getColumnIndex("IN")));
                Obj.setOUT(cur.getDouble(cur.getColumnIndex("OUT")));
                Obj.setAveragePurchaseRate(cur.getDouble(cur.getColumnIndex("AveragePurchaseRate")));
                Obj.setAverageSaleRate(cur.getDouble(cur.getColumnIndex("AverageSaleRate")));


                List<String> itmTagList = new ArrayList<>();

                List<String> itemLayerValues = getLayerValuesByItemId(context, " AND [ITEM_ID] =".concat(String.valueOf(_itemID)));
                if (itemLayerValues != null && itemLayerValues.size() != 0) {
                    itmTagList.addAll(itemLayerValues);
                }

                String _whereTag = " AND [ITEMID] = ".concat(String.valueOf(_itemID));
                List<String> tagList = getTagsByItemIdList(context, _whereTag);
                if (tagList != null && tagList.size() != 0)
                    itmTagList.addAll(tagList);

                Obj.setTagList(itmTagList);

                lstClsPurchaseDetails.add(Obj);
            }
            db.close();

        } catch (Exception e) {
            Log.e("ClsCategory", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }
        return lstClsPurchaseDetails;
    }



    @SuppressLint("WrongConstant")
    public static List<ClsStock> getStockOutList(String _where, Context context) {
        List<ClsStock> lstClsPurchaseDetails = new ArrayList<>();
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String strSql = "SELECT "
                    .concat("[ITM].[ITEM_CODE]")
                    .concat(",SUM([SALE].[Quantity]) AS [OUT] ")
                    .concat(",AVG(IFNULL([SALE].[SaleRate],0)) AS [AverageSaleRate] ")
                    .concat(" FROM [tbl_LayerItem_Master] AS [ITM] ")
                    .concat(" INNER JOIN [InventoryOrderDetail] AS [SALE] ON [SALE].[ItemCode] = [ITM].[ITEM_CODE] ")
                    .concat(" WHERE 1=1 ")
                    .concat(_where)
                    .concat(" ")
                    .concat(" GROUP BY  ")
                    .concat(" [ITM].[ITEM_CODE]")
                    .concat(" ORDER BY [ITM].[ITEM_NAME]");


            Log.e("--Select--", ">>>>>>>>>" + strSql);
            Cursor cur = db.rawQuery(strSql, null);
            Log.e("--Select--", ">>>>>>>>>" + cur.getCount());

            while (cur.moveToNext()) {
                ClsStock Obj = new ClsStock();

                Obj.setITEM_CODE(cur.getString(cur.getColumnIndex("ITEM_CODE")));
                Obj.setOUT(cur.getDouble(cur.getColumnIndex("OUT")));
                Obj.setAverageSaleRate(cur.getDouble(cur.getColumnIndex("AverageSaleRate")));

                lstClsPurchaseDetails.add(Obj);
            }
            db.close();

        } catch (Exception e) {
            Log.e("ClsCategory", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }
        return lstClsPurchaseDetails;
    }



}
