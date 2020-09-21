package com.demo.nspl.restaurantlite.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ClsLayerItemMaster {


    static Context context;
    int LAYERITEM_ID = 0, TAX_SLAB_ID = 0;
    String ITEM_CODE = "";
    String first_letter = "";
    int DISPLAY_ORDER = 0;
    String ITEM_NAME = "", REMARK = "", UNIT_CODE = "", TAGS = "", ACTIVE = "", TAX_APPLY = "", SLAB_NAME = "";
    Double RATE_PER_UNIT = 0.0, MIN_STOCK = 0.0, MAX_STOCK = 0.0, SGST = 0.0, CGST = 0.0, IGST = 0.0,
            Opening_Stock = 0.0, IN = 0.0, OUT = 0.0, AverageSaleRate = 0.0;
    boolean isHeader = false;

    List<String> TagList;

    private static SQLiteDatabase db;
    private ClsLayerItemMaster clsLayerItemMaster;

    String HSN_SAC_CODE = "";
    double lastPurchasePrice = 0.0;


    public double getLastPurchasePrice() {
        return lastPurchasePrice;
    }

    public void setLastPurchasePrice(double lastPurchasePrice) {
        this.lastPurchasePrice = lastPurchasePrice;
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

    public Double getAverageSaleRate() {
        return AverageSaleRate;
    }

    public void setAverageSaleRate(Double averageSaleRate) {
        AverageSaleRate = averageSaleRate;
    }

    public String getHSN_SAC_CODE() {
        return HSN_SAC_CODE;
    }

    public void setHSN_SAC_CODE(String HSN_SAC_CODE) {
        this.HSN_SAC_CODE = HSN_SAC_CODE;
    }


    public ClsLayerItemMaster() {
    }

    public ClsLayerItemMaster(ClsLayerItemMaster clsLayerItemMaster) {
        this.clsLayerItemMaster = clsLayerItemMaster;
    }

    public Double getOpening_Stock() {
        return Opening_Stock;
    }

    public void setOpening_Stock(Double opening_Stock) {
        Opening_Stock = opening_Stock;
    }

    public ClsLayerItemMaster(String first_letter) {
        this.first_letter = first_letter;
    }


    public ClsLayerItemMaster(Context context) {
        this.context = context;
    }


    public List<String> getTagList() {
        return TagList;
    }

    public void setTagList(List<String> tagList) {
        TagList = tagList;
    }

    public String getFirst_letter() {
        return first_letter;
    }

    public void setFirst_letter(String first_letter) {
        this.first_letter = first_letter;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public String getSLAB_NAME() {
        return SLAB_NAME;
    }

    public void setSLAB_NAME(String SLAB_NAME) {
        this.SLAB_NAME = SLAB_NAME;
    }

    public Double getSGST() {
        return SGST;
    }

    public void setSGST(Double SGST) {
        this.SGST = SGST;
    }

    public Double getCGST() {
        return CGST;
    }

    public void setCGST(Double CGST) {
        this.CGST = CGST;
    }

    public Double getIGST() {
        return IGST;
    }

    public void setIGST(Double IGST) {
        this.IGST = IGST;
    }

    public int getTAX_SLAB_ID() {
        return TAX_SLAB_ID;
    }

    public void setTAX_SLAB_ID(int TAX_SLAB_ID) {
        this.TAX_SLAB_ID = TAX_SLAB_ID;
    }

    public String getTAX_APPLY() {
        return TAX_APPLY;
    }

    public void setTAX_APPLY(String TAX_APPLY) {
        this.TAX_APPLY = TAX_APPLY;
    }

    public int getLAYERITEM_ID() {
        return LAYERITEM_ID;
    }

    public void setLAYERITEM_ID(int LAYERITEM_ID) {
        this.LAYERITEM_ID = LAYERITEM_ID;
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

    public int getDISPLAY_ORDER() {
        return DISPLAY_ORDER;
    }

    public void setDISPLAY_ORDER(int DISPLAY_ORDER) {
        this.DISPLAY_ORDER = DISPLAY_ORDER;
    }

    public String getITEM_NAME() {
        return ITEM_NAME;
    }

    public void setITEM_NAME(String ITEM_NAME) {
        this.ITEM_NAME = ITEM_NAME;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getUNIT_CODE() {
        return UNIT_CODE;
    }

    public void setUNIT_CODE(String UNIT_CODE) {
        this.UNIT_CODE = UNIT_CODE;
    }

    public String getTAGS() {
        return TAGS;
    }

    public void setTAGS(String TAGS) {
        this.TAGS = TAGS;
    }

    public String getACTIVE() {
        return ACTIVE;
    }

    public void setACTIVE(String ACTIVE) {
        this.ACTIVE = ACTIVE;
    }

    public Double getRATE_PER_UNIT() {
        return RATE_PER_UNIT;
    }

    public void setRATE_PER_UNIT(Double RATE_PER_UNIT) {
        this.RATE_PER_UNIT = RATE_PER_UNIT;
    }


    Double WHOLESALE_RATE = 0.0;
    String TAX_TYPE = "";


    String AUTO_GENERATE_ITEMCODE = "";


    Double _saleRateIncludingTax = 0.0;
    Double _wholesaleRateIncludingTax = 0.0;


    Double _AmountWithTax = 0.0;
    Double _AmountWithoutTax = 0.0;
    Double _TotalTaxAmount = 0.0;


    public String getAUTO_GENERATE_ITEMCODE() {
        return AUTO_GENERATE_ITEMCODE;
    }

    public void setAUTO_GENERATE_ITEMCODE(String AUTO_GENERATE_ITEMCODE) {
        this.AUTO_GENERATE_ITEMCODE = AUTO_GENERATE_ITEMCODE;
    }

    public Double get_AmountWithTax() {
        return _AmountWithTax;
    }

    public void set_AmountWithTax(Double _AmountWithTax) {
        this._AmountWithTax = _AmountWithTax;
    }

    public Double get_AmountWithoutTax() {
        return _AmountWithoutTax;
    }

    public void set_AmountWithoutTax(Double _AmountWithoutTax) {
        this._AmountWithoutTax = _AmountWithoutTax;
    }

    public Double get_TotalTaxAmount() {
        return _TotalTaxAmount;
    }

    public void set_TotalTaxAmount(Double _TotalTaxAmount) {
        this._TotalTaxAmount = _TotalTaxAmount;
    }

    public Double get_saleRateIncludingTax() {
        return _saleRateIncludingTax;
    }

    public void set_saleRateIncludingTax(Double _saleRateIncludingTax) {
        this._saleRateIncludingTax = _saleRateIncludingTax;
    }

    public Double get_wholesaleRateIncludingTax() {
        return _wholesaleRateIncludingTax;
    }

    public void set_wholesaleRateIncludingTax(Double _wholesaleRateIncludingTax) {
        this._wholesaleRateIncludingTax = _wholesaleRateIncludingTax;
    }

    public Double getWHOLESALE_RATE() {
        return WHOLESALE_RATE;
    }

    public void setWHOLESALE_RATE(Double WHOLESALE_RATE) {
        this.WHOLESALE_RATE = WHOLESALE_RATE;
    }

    public String getTAX_TYPE() {
        return TAX_TYPE;
    }

    public void setTAX_TYPE(String TAX_TYPE) {
        this.TAX_TYPE = TAX_TYPE;
    }

    @SuppressLint("WrongConstant")
    public static int Insert(ClsLayerItemMaster objLayerItemMaster
            , List<String> tagList
            , List<ClsInventoryLayer> listLayers
            , Context context) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String qry = ("INSERT INTO [tbl_LayerItem_Master] ([ITEM_NAME]," +
                    "[ITEM_CODE],[RATE_PER_UNIT],[REMARK]," +
                    "[MIN_STOCK],[MAX_STOCK],[UNIT_CODE],[TAGS],[ACTIVE],[DISPLAY_ORDER]," +
                    "[OPENING_STOCK],[TAX_APPLY],[WHOLESALE_RATE],[TAX_TYPE],[HSN_SAC_CODE],[TAX_SLAB_ID]" +
                    ",[AUTO_GENERATED_ITEM_CODE]) VALUES ('")

                    .concat(objLayerItemMaster.getITEM_NAME().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(String.valueOf(objLayerItemMaster.getITEM_CODE()))
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(objLayerItemMaster.getRATE_PER_UNIT()))
                    .concat(",")

                    .concat("'")
                    .concat(String.valueOf(objLayerItemMaster.getREMARK()))
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(objLayerItemMaster.getMIN_STOCK()))
                    .concat(",")

                    .concat(String.valueOf(objLayerItemMaster.getMAX_STOCK()))
                    .concat(",")

                    .concat("'")
                    .concat(String.valueOf(objLayerItemMaster.getUNIT_CODE()))
                    .concat("'")
                    .concat(",")


                    .concat("'")
                    .concat(String.valueOf(objLayerItemMaster.getTAGS()))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(String.valueOf(objLayerItemMaster.getACTIVE()))
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(objLayerItemMaster.getDISPLAY_ORDER()))
                    .concat(",")

                    .concat(String.valueOf(objLayerItemMaster.getOpening_Stock()))
                    .concat(",")

                    .concat("'")
                    .concat(String.valueOf(objLayerItemMaster.getTAX_APPLY()))
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(objLayerItemMaster.getWHOLESALE_RATE()))
                    .concat(",")

                    .concat("'")
                    .concat(String.valueOf(objLayerItemMaster.getTAX_TYPE()))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objLayerItemMaster.getHSN_SAC_CODE())
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(objLayerItemMaster.getTAX_SLAB_ID()))
                    .concat(",")

                    .concat("'")
                    .concat(objLayerItemMaster.getAUTO_GENERATE_ITEMCODE())
                    .concat("'")

                    .concat(");");

            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.e("INSERT", "Inventory--->>" + qry);

            Cursor c = db.rawQuery("SELECT last_insert_rowid()", null);
            c.moveToFirst();
            int insertedID = c.getInt(0);//orderiD
            Log.e("id:-- ", String.valueOf(insertedID));

            for (String tag : tagList) {
                String qryTag = ("INSERT INTO [tbl_ItemTag] ([ITEMID]," +
                        "[ITEMNAME],[TAGNAME]) VALUES (")

                        .concat(String.valueOf(insertedID))
                        .concat(",")

                        .concat("'")
                        .concat(objLayerItemMaster.getITEM_NAME()
                                .replace("'", "''"))
                        .concat("'")
                        .concat(",")

                        .concat("'")
                        .concat(tag.replace("'", "''"))
                        .concat("'")

                        .concat(");");

                SQLiteStatement statementInsertTags = db.compileStatement(qryTag);
                int resultInsertTags = statementInsertTags.executeUpdateDelete();
                Log.e("INSERT", "Inventory--->>" + qryTag);
                Log.e("resultInsertTags", "Inventory--->>" + resultInsertTags);

            }

            for (ClsInventoryLayer OBJLayer : listLayers) {
                if (OBJLayer.getLayerValue() != null
                        && OBJLayer.getLayerValue() != ""
                        && OBJLayer.getLayerValue().length() != 0) {
                    String qryLyr = ("INSERT INTO [tbl_ItemLayer] ([LAYERITEM_ID]," +
                            "[LAYER_NAME],[ITEM_ID],[ITEM_NAME],[LAYER_VALUE]) VALUES (")

                            .concat(String.valueOf(OBJLayer.getINVENTORYLAYER_ID()))
                            .concat(",")

                            .concat("'")
                            .concat(OBJLayer.getInventoryLayerName().trim()
                                    .replace("'", "''"))
                            .concat("'")
                            .concat(",")

                            .concat(String.valueOf(insertedID))
                            .concat(",")

                            .concat("'")
                            .concat(objLayerItemMaster.getITEM_NAME().trim()
                                    .replace("'", "''"))

                            .concat("'")
                            .concat(",")

                            .concat("'")
                            .concat(OBJLayer.getLayerValue().replace("'", "''"))
                            .concat("'")
                            .concat(");");

                    SQLiteStatement statementInsertItemLayer = db.compileStatement(qryLyr);
                    int resultInsertItemLayer = statementInsertItemLayer.executeUpdateDelete();
                    Log.e("INSERT", "Inventory--->>" + qryLyr);
                    Log.e("resultInsertTags", "Inventory--->>" + resultInsertItemLayer);
                }
            }

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsInventory", "Insert>>>>>>>>" + e.getMessage());
            e.getMessage();
        }
        return result;

    }

    @SuppressLint("WrongConstant")
    public static boolean checkExists(String _where, Context context) {
        boolean result = false;

        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = "SELECT 1 FROM [tbl_LayerItem_Master] WHERE 1=1 "//Salary
                    .concat(_where)
                    .concat(";");
            Log.e("exists", "existsChqQry - " + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            if (cur != null && cur.getCount() != 0) {
                result = true;
            }

            db.close();
        } catch (Exception e) {
            Log.e("ClsExpenseType", "ClsExpenseType>>>>CheckExist" + e.getMessage());
            e.getMessage();
        }
        return result;


    }

    @SuppressLint("WrongConstant")
    public static List<String> getUniqueTagsList(Context context) {

        Log.e("getUniqueTagsList", "getUniqueTagsList call");

        List<String> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT DISTINCT [TAGNAME]".concat(" FROM [tbl_ItemTag] WHERE 1=1 ORDER BY [TAGNAME]");

            Cursor cur = db.rawQuery(qry, null);
            Log.e("cur", "getUniqueTagsList " + String.valueOf(cur.getCount()));
            Log.e("qry", "getUniqueTagsList" + String.valueOf(qry));

            while (cur.moveToNext()) {
                list.add(cur.getString(cur.getColumnIndex("TAGNAME")));
            }

            db.close();
        } catch (Exception e) {
            Log.e("ClsItem", "ClsItem >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static List<String> getItemIdListByTag(Context context, String Where) {

        List<String> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT [ITEMID]".concat(" FROM [tbl_ItemTag] WHERE 1=1 ").concat(Where);


            Log.e("qry", "ClsInventoryLayer >>>>tbl_LayerItem_MasterGetList" + qry);

            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", "ClsInventoryLayer >>>>tbl_LayerItem_MasterGetList Count " + cur.getCount());
            while (cur.moveToNext()) {
                list.add(String.valueOf(cur.getInt(cur.getColumnIndex("ITEMID"))));
            }

            db.close();

        } catch (Exception e) {
            Log.e("ClsItem", "ClsItem >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static List<String> getTagsByItemIdList(Context context, String Where) {

        List<String> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT [TAGNAME]".concat(" FROM [tbl_ItemTag] WHERE 1=1 ").concat(Where);

            Log.e("qry", "ClsInventoryLayer >>>>tbl_LayerItem_MasterGetList" + qry);

            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", "ClsInventoryLayer >>>>tbl_LayerItem_MasterGetList Count " + cur.getCount());
            while (cur.moveToNext()) {
                list.add("".concat("#").concat(cur.getString(cur.getColumnIndex("TAGNAME")).toLowerCase()));
            }

            db.close();

        } catch (Exception e) {
            Log.e("ClsItem", "ClsItem >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static List<String> getLayerValuesByItemId(Context context, String _where) {

        List<String> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
/*
*  strSql = ("INSERT INTO [tbl_ItemLayer] ([LAYERITEM_ID]," +
                        "[LAYER_NAME],[ITEM_ID],[ITEM_NAME],[LAYER_VALUE]) VALUES (")
* */
            String qry = "SELECT [LAYER_VALUE] ,[LAYER_NAME] "
                    .concat("FROM [tbl_ItemLayer] WHERE 1=1 ")
                    .concat(_where)
                    .concat(" order by [LAYER_VALUE]");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("ClsItem", "ClsItem >>>>GetList " + cur.getCount());
            Log.e("qry", "ClsItem >>>>GetList " + qry);

            while (cur.moveToNext()) {

                list.add("".concat(cur.getString(cur.getColumnIndex("LAYER_VALUE"))));
            }

            db.close();
        } catch (Exception e) {
            Log.e("ClsItem", "ClsItem >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static List<String> getLayerValuesWithLayerName(Context context, String _where) {

        List<String> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
/*
*  strSql = ("INSERT INTO [tbl_ItemLayer] ([LAYERITEM_ID]," +
                        "[LAYER_NAME],[ITEM_ID],[ITEM_NAME],[LAYER_VALUE]) VALUES (")
* */
            String qry = "SELECT [LAYER_VALUE] ,[LAYER_NAME] "
                    .concat("FROM [tbl_ItemLayer] WHERE 1=1 ")
                    .concat(_where)
                    .concat(" order by [LAYER_VALUE]");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("ClsItem", "ClsItem >>>>GetList " + cur.getCount());
            Log.e("qry", "ClsItem >>>>GetList " + qry);

            while (cur.moveToNext()) {

                list.add("".concat(cur.getString(cur.getColumnIndex("LAYER_NAME")))
                        .concat(":")
                        .concat(cur.getString(cur.getColumnIndex("LAYER_VALUE")).toUpperCase()));
            }

            db.close();
        } catch (Exception e) {
            Log.e("ClsItem", "ClsItem >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static List<String> getTagsList(Context context, String Where) {

        List<String> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT "
                    .concat("[ITEMTAG_ID]")
                    .concat(",[ITEMID]")
                    .concat(",[ITEMNAME]")
                    .concat(",[TAGNAME]")
                    .concat(" FROM [tbl_ItemTag] WHERE 1=1 ")
                    .concat(Where);


            Log.e("qry", "ClsInventoryLayer >>>>tbl_LayerItem_MasterGetList" + qry);

            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", "ClsInventoryLayer >>>>tbl_LayerItem_MasterGetList Count " + cur.getCount());
            while (cur.moveToNext()) {
                list.add(cur.getString(cur.getColumnIndex("TAGNAME")));
            }

            db.close();

        } catch (Exception e) {
            Log.e("ClsItem", "ClsItem >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsItemLayer> getLayerItems(Context context, String _where) {

        List<ClsItemLayer> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
/*
  strSql = ("INSERT INTO [tbl_ItemLayer] ([LAYERITEM_ID]," +
                        "[LAYER_NAME],[ITEM_ID],[ITEM_NAME],[LAYER_VALUE]) VALUES (")
* */
            String qry = "SELECT * "
                    .concat("FROM [tbl_ItemLayer] WHERE 1=1 ")
                    .concat(_where)
                    .concat(" order by [LAYER_NAME]");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("ClsItem", "ClsItem >>>>GetList " + cur.getCount());
            Log.e("qry", "ClsItem >>>>GetList " + qry);

            while (cur.moveToNext()) {
                ClsItemLayer getSet = new ClsItemLayer();
                getSet.setITEM_ID(cur.getInt(cur.getColumnIndex("ITEMLAYER_ID")));
                getSet.setLAYERITEM_ID(cur.getInt(cur.getColumnIndex("LAYERITEM_ID")));
                getSet.setLAYER_NAME(cur.getString(cur.getColumnIndex("LAYER_NAME")));
                getSet.setITEM_NAME(cur.getString(cur.getColumnIndex("ITEM_NAME")));
                getSet.setLAYER_VALUE(cur.getString(cur.getColumnIndex("LAYER_VALUE")));
                list.add(getSet);
            }

            db.close();
        } catch (Exception e) {
            Log.e("ClsItem", "ClsItem >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static List<String> getItemsByLayers(String _where) {

        List<String> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
/*
*  strSql = ("INSERT INTO [tbl_ItemLayer] ([LAYERITEM_ID]," +
                        "[LAYER_NAME],[ITEM_ID],[ITEM_NAME],[LAYER_VALUE]) VALUES (")
* */
            String qry = "SELECT [ITEM_ID] "
                    .concat("FROM [tbl_ItemLayer] WHERE 1=1 ")
                    .concat(_where);

            Cursor cur = db.rawQuery(qry, null);

            Log.e("ClsItem", "ClsItem >>>>getCount " + cur.getCount());
            Log.e("ClsItem", "ClsItem >>>>qry " + qry);

            while (cur.moveToNext()) {
                list.add(String.valueOf(cur.getInt(cur.getColumnIndex("ITEM_ID"))));
            }

            db.close();

        } catch (Exception e) {
            Log.e("ClsItem", "ClsItem >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsLayerItemMaster> getAllList(Context context, String _where, String paging) {

        List<ClsLayerItemMaster> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name,
                    context.MODE_APPEND, null);

            String qry = "SELECT "
                    .concat("TLIM.[LAYERITEM_ID]")
                    .concat(",TLIM.[ITEM_NAME]")
                    .concat(",TLIM.[ITEM_CODE]")
                    .concat(",TLIM.[ACTIVE]")
                    .concat(",IFNULL(TLIM.[RATE_PER_UNIT],0) AS [RATE_PER_UNIT]")
                    .concat(",TLIM.[DISPLAY_ORDER]")
                    .concat(",TLIM.[OPENING_STOCK]")
                    .concat(",TLIM.[MIN_STOCK]")
                    .concat(",TLIM.[MAX_STOCK]")
                    .concat(",TLIM.[UNIT_CODE]")
                    .concat(",TLIM.[TAX_APPLY]")
                    .concat(",TLIM.[REMARK]")
                    .concat(",IFNULL(TLIM.[WHOLESALE_RATE],0) AS [WHOLESALE_RATE]")
                    .concat(",TLIM.[TAX_TYPE]")
                    .concat(",IFNULL([TLIM].[HSN_SAC_CODE],'') AS [HSN_SAC_CODE]")

                    .concat(",TLIM.[TAX_SLAB_ID]")
                    .concat(",TS.[SLAB_NAME]")

                    .concat(" FROM [tbl_LayerItem_Master] as TLIM ")
                    .concat(" LEFT JOIN [tbl_Tax_Slab] as TS ON TS.[SLAB_ID] = TLIM.[TAX_SLAB_ID]")
                    .concat(" WHERE 1=1 ")
                    .concat(_where)
                    .concat(" ORDER BY upper(TLIM.[ITEM_NAME]) ASC ")
                    .concat(paging);

            Log.e("--ITEM--", "ItemList: " + qry);

            Cursor cur = db.rawQuery(qry, null);
            Log.e("--ITEM--", "ItemListCount: " + cur.getCount());

            while (cur.moveToNext()) {
                ClsLayerItemMaster getSet = new ClsLayerItemMaster();
                int id = cur.getInt(cur.getColumnIndex("LAYERITEM_ID"));
                getSet.setLAYERITEM_ID(id);
                getSet.setITEM_NAME(cur.getString(cur.getColumnIndex("ITEM_NAME")));
                getSet.setITEM_CODE(cur.getString(cur.getColumnIndex("ITEM_CODE")));
                getSet.setACTIVE(cur.getString(cur.getColumnIndex("ACTIVE")));
                getSet.setRATE_PER_UNIT(cur.getDouble(cur.getColumnIndex("RATE_PER_UNIT")));
                getSet.setMIN_STOCK(cur.getDouble(cur.getColumnIndex("MIN_STOCK")));
                getSet.setMAX_STOCK(cur.getDouble(cur.getColumnIndex("MAX_STOCK")));
                getSet.setUNIT_CODE(cur.getString(cur.getColumnIndex("UNIT_CODE")));
                getSet.setDISPLAY_ORDER(cur.getInt(cur.getColumnIndex("DISPLAY_ORDER")));
                getSet.setOpening_Stock(cur.getDouble(cur.getColumnIndex("OPENING_STOCK")));
                getSet.setREMARK(cur.getString(cur.getColumnIndex("REMARK")));
                getSet.setHSN_SAC_CODE(cur.getString(cur.getColumnIndex("HSN_SAC_CODE")));

                getSet.setWHOLESALE_RATE(cur.getDouble(cur.getColumnIndex("WHOLESALE_RATE")));
                getSet.setTAX_TYPE(cur.getString(cur.getColumnIndex("TAX_TYPE")));
                getSet.setTAX_APPLY(cur.getString(cur.getColumnIndex("TAX_APPLY")));


                getSet.setTAX_SLAB_ID(cur.getInt(cur.getColumnIndex("TAX_SLAB_ID")));
                getSet.setSLAB_NAME(cur.getString(cur.getColumnIndex("SLAB_NAME")));

                List<String> itmTagList = new ArrayList<>();

                List<String> itemLayerValues = getLayerValuesByItemId(context, " AND [ITEM_ID] =".concat(String.valueOf(id)));
                if (itemLayerValues != null && itemLayerValues.size() != 0) {
                    itmTagList.addAll(itemLayerValues);
                }

                String _whereTag = " AND [ITEMID] = ".concat(String.valueOf(id));
                List<String> tagList = getTagsByItemIdList(context, _whereTag);
                if (tagList != null && tagList.size() != 0)
                    itmTagList.addAll(tagList);

                getSet.setTagList(itmTagList);

                list.add(getSet);
            }

            db.close();
        } catch (Exception e) {
            Log.e("ClsItem", "ClsItem >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsLayerItemMaster> getLayerItemList(Context context, String _where, String _whereSearch) {
        List<ClsLayerItemMaster> list = new ArrayList<>();
        Log.e("getLayerItemList", "getLayerItemList >>>>tbl_LayerItem_MasterGetList" + _where);
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            Log.e("qry", "ClsInventoryLayer >>>>tbl_LayerItem_MasterGetList" + _where);
            String qry = "SELECT "
                    .concat("[ITM].[LAYERITEM_ID]")
                    .concat(",[ITM].[ITEM_NAME]")
                    .concat(",[ITM].[ITEM_CODE]")
//                    .concat(",IFNULL([ITM].[RATE_PER_UNIT],0) AS [RATE_PER_UNIT]")
                    .concat(",[ITM].[UNIT_CODE]")
                    .concat(",[ITM].[TAX_APPLY]")
                    .concat(",IFNULL([ITM].[HSN_SAC_CODE],'') AS [HSN_SAC_CODE]")
                    .concat(",[ITM].[TAX_SLAB_ID]")

                    .concat(",[TS].[SLAB_NAME]")
                    .concat(",[TS].[SGST]")
                    .concat(",[TS].[CGST]")
                    .concat(",[TS].[IGST]")

                    .concat(",[ITM].[MIN_STOCK]")
                    .concat(",[ITM].[MAX_STOCK]")
                    .concat(",[ITM].[OPENING_STOCK]")
//                    .concat(",IFNULL([ITM].[WHOLESALE_RATE],0) AS [WHOLESALE_RATE]")
                    .concat(",[ITM].[TAX_TYPE]")

                    .concat(" FROM [tbl_LayerItem_Master] AS ITM")
                    .concat(" LEFT JOIN [tbl_Tax_Slab] AS TS ON TS.[SLAB_ID]= ITM.[TAX_SLAB_ID]")

                    .concat(" WHERE 1=1")

                    .concat(_where).concat(_whereSearch)
                    .concat(" GROUP BY [ITM].[ITEM_CODE] ")
                    .concat(" ORDER BY [ITM].[ITEM_NAME] ASC ");

            Log.e("--QRY--", "LayerItemList: " + qry);

            Cursor cur = db.rawQuery(qry, null);
            Log.e("--QRY--", "LayerItemCount: " + cur.getCount());

            while (cur.moveToNext()) {
                ClsLayerItemMaster getSet = new ClsLayerItemMaster();
                int _itemID = cur.getInt(cur.getColumnIndex("LAYERITEM_ID"));
                getSet.setLAYERITEM_ID(_itemID);
                getSet.setITEM_NAME(cur.getString(cur.getColumnIndex("ITEM_NAME")));
                getSet.setITEM_CODE(cur.getString(cur.getColumnIndex("ITEM_CODE")));
//                getSet.setRATE_PER_UNIT(cur.getDouble(cur.getColumnIndex("RATE_PER_UNIT")));
                getSet.setUNIT_CODE(cur.getString(cur.getColumnIndex("UNIT_CODE")));
                getSet.setTAX_APPLY(cur.getString(cur.getColumnIndex("TAX_APPLY")));
                getSet.setHSN_SAC_CODE(cur.getString(cur.getColumnIndex("HSN_SAC_CODE")));
                getSet.setTAX_SLAB_ID(cur.getInt(cur.getColumnIndex("TAX_SLAB_ID")));
                getSet.setSLAB_NAME(cur.getString(cur.getColumnIndex("SLAB_NAME")));
                getSet.setSGST(cur.getDouble(cur.getColumnIndex("SGST")));
                getSet.setCGST(cur.getDouble(cur.getColumnIndex("CGST")));
                getSet.setIGST(cur.getDouble(cur.getColumnIndex("IGST")));

//                getSet.setWHOLESALE_RATE(cur.getDouble(cur.getColumnIndex("WHOLESALE_RATE")));
                getSet.setTAX_TYPE(cur.getString(cur.getColumnIndex("TAX_TYPE")));

                getSet.setMIN_STOCK(cur.getDouble(cur.getColumnIndex("MIN_STOCK")));
                getSet.setMAX_STOCK(cur.getDouble(cur.getColumnIndex("MAX_STOCK")));
                getSet.setOpening_Stock(cur.getDouble(cur.getColumnIndex("OPENING_STOCK")));

                List<String> itmTagList = new ArrayList<>();

                List<String> itemLayerValues = getLayerValuesByItemId(context, " AND [ITEM_ID] =".concat(String.valueOf(_itemID)));
                if (itemLayerValues != null && itemLayerValues.size() != 0) {
                    itmTagList.addAll(itemLayerValues);
                }

                Gson gson = new Gson();
                String jsonInString = gson.toJson(itemLayerValues);
                Log.d("--GSON--", "itemLayerValues:  " + jsonInString);


                String _whereTag = " AND [ITEMID] = ".concat(String.valueOf(_itemID));
                List<String> tagList = getTagsByItemIdList(context, _whereTag);
                if (tagList != null && tagList.size() != 0)
                    itmTagList.addAll(tagList);

                getSet.setTagList(itmTagList);
                list.add(getSet);
            }

            db.close();
        } catch (Exception e) {
            Log.e("ClsItem", "ClsItem >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static int Delete(ClsLayerItemMaster objClsLayerItemMaster, Context context) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "DELETE FROM [tbl_LayerItem_Master] WHERE [LAYERITEM_ID] = "
                    .concat(String.valueOf(objClsLayerItemMaster.getLAYERITEM_ID()))
                    .concat(";");
            Log.e("DELETE", "DELqRY-" + strSql);

            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsInventory", "DELETE" + e.getMessage());
            e.getMessage();
        }
        return result;

    }

    @SuppressLint("WrongConstant")
    public static List<String> getUniqueItemList(Context context, String _whereSearch) {
        List<String> list = new ArrayList<>();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT distinct [ITEM_NAME] "

                    .concat(" FROM [tbl_LayerItem_Master] ")
                    .concat(" WHERE 1=1")

                    .concat(_whereSearch)
                    .concat(" ORDER BY [ITEM_NAME]");

            Log.e("qry", "ClsInventoryLayer >>>>tbl_LayerItem_MasterGetList" + qry);

            Cursor cur = db.rawQuery(qry, null);
            Log.e("cur", "ClsInventoryLayer >>>>tbl_LayerItem_MasterGetList" + cur.getCount());

            while (cur.moveToNext()) {
                list.add(cur.getString(cur.getColumnIndex("ITEM_NAME")));
            }

            db.close();
        } catch (Exception e) {
            Log.e("ClsItem", "ClsItem >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static List<String> getAutoSuggestionItemList(Context context) {

        List<String> list = new ArrayList<>();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT DISTINCT [ITEM_NAME] "

                    .concat(" FROM [tbl_LayerItem_Master] ")
                    .concat(" WHERE 1=1")
                    .concat(" AND [ACTIVE] = 'YES' ")
                    .concat(" ORDER BY [ITEM_NAME]");

            Log.e("qry", "ClsInventoryLayer >>>>tbl_LayerItem_MasterGetList" + qry);

            Cursor cur = db.rawQuery(qry, null);
            Log.e("cur", "ClsInventoryLayer >>>>tbl_LayerItem_MasterGetList" + cur.getCount());

            while (cur.moveToNext()) {
                list.add(cur.getString(cur.getColumnIndex("ITEM_NAME")));
            }

            db.close();
        } catch (Exception e) {
            Log.e("ClsItem", "ClsItem >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static List<String> getItemIdListByItemName(Context context, String _whereSearch) {
        List<String> list = new ArrayList<>();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT distinct [LAYERITEM_ID] "

                    .concat(" FROM [tbl_LayerItem_Master] ")
                    .concat(" WHERE 1=1")

                    .concat(_whereSearch);

            Log.e("--qry--", "--FilterList--" + qry);

            Cursor cur = db.rawQuery(qry, null);
            Log.e("--qry--", "--FilterList--" + cur.getCount());

            while (cur.moveToNext()) {
                list.add(String.valueOf(cur.getInt(cur.getColumnIndex("LAYERITEM_ID"))));
            }

            db.close();
        } catch (Exception e) {
            Log.e("ClsItem", "ClsItem >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static ClsLayerItemMaster QueryById(int Id, Context context) {
        ClsLayerItemMaster getSet = new ClsLayerItemMaster();
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = ("SELECT [LAYERITEM_ID],[ITEM_NAME],[ITEM_CODE],IFNULL([RATE_PER_UNIT],0) AS [RATE_PER_UNIT]" +
                    ",[REMARK],[MIN_STOCK],[MAX_STOCK],[UNIT_CODE]," +
                    "[TAGS],[ACTIVE],[DISPLAY_ORDER],[OPENING_STOCK],[TAX_APPLY],IFNULL([WHOLESALE_RATE],0) AS [WHOLESALE_RATE],[TAX_TYPE],[HSN_SAC_CODE],[TAX_SLAB_ID] " +
                    "FROM [tbl_LayerItem_Master] WHERE 1=1 AND [LAYERITEM_ID] = ")
                    .concat(String.valueOf(Id))
                    .concat(";");

            Log.e("Update", ">>>>>>>>>" + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            Log.e("Update", ">>>>>>>>>" + cur.getCount());

            while (cur.moveToNext()) {
                getSet.setLAYERITEM_ID(cur.getInt(cur.getColumnIndex("LAYERITEM_ID")));
                getSet.setITEM_NAME(cur.getString(cur.getColumnIndex("ITEM_NAME")));
                getSet.setITEM_CODE(cur.getString(cur.getColumnIndex("ITEM_CODE")));
                getSet.setACTIVE(cur.getString(cur.getColumnIndex("ACTIVE")));
                getSet.setRATE_PER_UNIT(cur.getDouble(cur.getColumnIndex("RATE_PER_UNIT")));
                getSet.setMIN_STOCK(cur.getDouble(cur.getColumnIndex("MIN_STOCK")));
                getSet.setMAX_STOCK(cur.getDouble(cur.getColumnIndex("MAX_STOCK")));
                getSet.setDISPLAY_ORDER(cur.getInt(cur.getColumnIndex("DISPLAY_ORDER")));
                getSet.setOpening_Stock(cur.getDouble(cur.getColumnIndex("OPENING_STOCK")));

                getSet.setWHOLESALE_RATE(cur.getDouble(cur.getColumnIndex("WHOLESALE_RATE")));
                getSet.setTAX_TYPE(cur.getString(cur.getColumnIndex("TAX_TYPE")));

                getSet.setTAGS(cur.getString(cur.getColumnIndex("TAGS")));
                getSet.setREMARK(cur.getString(cur.getColumnIndex("REMARK")));
                getSet.setUNIT_CODE(cur.getString(cur.getColumnIndex("UNIT_CODE")));
                getSet.setHSN_SAC_CODE(cur.getString(cur.getColumnIndex("HSN_SAC_CODE")));
                getSet.setTAX_APPLY(cur.getString(cur.getColumnIndex("TAX_APPLY")));
                getSet.setTAX_SLAB_ID(cur.getInt(cur.getColumnIndex("TAX_SLAB_ID")));

            }

            db.close();

        } catch (Exception e) {
            Log.e("ClsCategory", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }
        return getSet;
    }


    @SuppressLint("WrongConstant")
    public static int DeleteTagsById(int Item_Id, Context context) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "DELETE FROM [tbl_ItemTag] WHERE [ITEMID] = "
                    .concat(String.valueOf(Item_Id))
                    .concat(";");
            Log.e("DELETE", "DELqRY-" + strSql);

            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsInventory", "Delete" + e.getMessage());
            e.getMessage();
        }
        return result;

    }

    @SuppressLint("WrongConstant")
    public static int DeleteItemLayerById(int Item_Id, Context context) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "DELETE FROM [tbl_ItemLayer] WHERE [ITEM_ID] = "
                    .concat(String.valueOf(Item_Id))
                    .concat(";");
            Log.e("DELETE", "DELqRY-" + strSql);

            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsInventory", "Delete" + e.getMessage());
            e.getMessage();
        }
        return result;

    }

    @SuppressLint("WrongConstant")
    public static int Update(ClsLayerItemMaster ObjClsLayerItemMaster,
                             List<String> taglist,
                             List<ClsInventoryLayer> listLayers,
                             Context context) {
        int result = 0;

        Log.e("Update", "Query..");

        //----------------------------- Update into tbl_LayerItem_Master ------------------------------//

        db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

        String strSql = "UPDATE [tbl_LayerItem_Master] SET "

                .concat(" [ITEM_NAME] = ")
                .concat("'")
                .concat(ObjClsLayerItemMaster.getITEM_NAME().trim()
                        .replace("'", "''"))
                .concat("'")

                .concat(", [ITEM_CODE] = ")
                .concat("'")
                .concat(String.valueOf(ObjClsLayerItemMaster.getITEM_CODE()))
                .concat("'")

                .concat(", [RATE_PER_UNIT] = ")
                .concat(String.valueOf(ObjClsLayerItemMaster.getRATE_PER_UNIT()))


                .concat(", [REMARK] = ")
                .concat("'")
                .concat(ObjClsLayerItemMaster.getREMARK())
                .concat("'")

                .concat(", [MIN_STOCK] = ")
                .concat(String.valueOf(ObjClsLayerItemMaster.getMIN_STOCK()))


                .concat(", [MAX_STOCK] = ")
                .concat(String.valueOf(ObjClsLayerItemMaster.getMAX_STOCK()))


                .concat(", [UNIT_CODE] = ")
                .concat("'")
                .concat(ObjClsLayerItemMaster.getUNIT_CODE())
                .concat("'")

                .concat(", [TAGS] = ")
                .concat("'")
                .concat(ObjClsLayerItemMaster.getTAGS())
                .concat("'")

                .concat(", [ACTIVE] = ")
                .concat("'")
                .concat(ObjClsLayerItemMaster.getACTIVE())
                .concat("'")

                .concat(", [DISPLAY_ORDER] = ")
                .concat(String.valueOf(ObjClsLayerItemMaster.getDISPLAY_ORDER()))

                .concat(", [OPENING_STOCK] = ")
                .concat(String.valueOf(ObjClsLayerItemMaster.getOpening_Stock()))

                .concat(", [TAX_APPLY] = ")
                .concat("'")
                .concat(ObjClsLayerItemMaster.getTAX_APPLY())
                .concat("'")

                .concat(", [WHOLESALE_RATE] = ")
                .concat(String.valueOf(ObjClsLayerItemMaster.getWHOLESALE_RATE()))

                .concat(", [TAX_TYPE] = ")
                .concat("'")
                .concat(ObjClsLayerItemMaster.getTAX_TYPE())
                .concat("'")

                .concat(", [HSN_SAC_CODE] = ")
                .concat("'")
                .concat(ObjClsLayerItemMaster.getHSN_SAC_CODE())
                .concat("'")

                .concat(", [TAX_SLAB_ID] = ")
                .concat(String.valueOf(ObjClsLayerItemMaster.getTAX_SLAB_ID()))

                .concat(", [AUTO_GENERATED_ITEM_CODE] = ")
                .concat("'")
                .concat(ObjClsLayerItemMaster.getAUTO_GENERATE_ITEMCODE())
                .concat("'")

                .concat(" WHERE [LAYERITEM_ID] = ")
                .concat(String.valueOf(ObjClsLayerItemMaster.getLAYERITEM_ID()))
                .concat(";");

        Log.e("--LayerItem-- ", strSql);
        SQLiteStatement statementUpdate = db.compileStatement(strSql);
        result = statementUpdate.executeUpdateDelete();


        //---------------This is From Updating Tags from tbl_ItemTag-----------------//
        //-----------------------Delete Tags From tbl_ItemTag------------------------//

        strSql = "DELETE FROM [tbl_ItemTag] WHERE [ITEMID] = "
                .concat(String.valueOf(ObjClsLayerItemMaster.getLAYERITEM_ID()))
                .concat(";");
        SQLiteStatement statementDelete = db.compileStatement(strSql);
        int resultDelete = statementDelete.executeUpdateDelete();
        Log.e("resultDelete", String.valueOf(resultDelete));


        //------------------------Insert New Tags into tbl_ItemTag--------------------------//

        for (String tag : taglist) {
            strSql = ("INSERT INTO [tbl_ItemTag] ([ITEMID]," +
                    "[ITEMNAME],[TAGNAME]) VALUES (")

                    .concat(String.valueOf(ObjClsLayerItemMaster.getLAYERITEM_ID()))
                    .concat(",")

                    .concat("'")
                    .concat(ObjClsLayerItemMaster.getITEM_NAME().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(tag.trim()
                            .replace("'", "''"))
                    .concat("'")

                    .concat(");");

            SQLiteStatement statementInsert = db.compileStatement(strSql);
            int resultInsert = statementInsert.executeUpdateDelete();
            Log.e("INSERT", "Inventory--->>" + resultInsert);


        }

        //---------------------Delete by LAYERITEM_ID From tbl_ItemLayer-------------------------//

        strSql = "DELETE FROM [tbl_ItemLayer] WHERE [ITEM_ID] = "
                .concat(String.valueOf(ObjClsLayerItemMaster.getLAYERITEM_ID()))
                .concat(";");
        SQLiteStatement statementItemLayerDelete = db.compileStatement(strSql);
        int resultItemLayerDeleteDelete = statementItemLayerDelete.executeUpdateDelete();
        Log.e("resultDelete", String.valueOf(resultItemLayerDeleteDelete));


        //------------------------Insert New Values into tbl_ItemLayer--------------------------//
        for (ClsInventoryLayer OBJLayer : listLayers) {
            if (OBJLayer.getLayerValue() != null
                    && OBJLayer.getLayerValue() != ""
                    && OBJLayer.getLayerValue().length() != 0) {

                strSql = ("INSERT INTO [tbl_ItemLayer] ([LAYERITEM_ID]," +
                        "[LAYER_NAME],[ITEM_ID],[ITEM_NAME],[LAYER_VALUE]) VALUES (")

                        .concat(String.valueOf(OBJLayer.getINVENTORYLAYER_ID()))
                        .concat(",")

                        .concat("'")
                        .concat(OBJLayer.getInventoryLayerName().trim()
                                .replace("'", "''"))
                        .concat("'")
                        .concat(",")

                        .concat(String.valueOf(ObjClsLayerItemMaster.getLAYERITEM_ID()))
                        .concat(",")

                        .concat("'")
                        .concat(ObjClsLayerItemMaster.getITEM_NAME().trim()
                                .replace("'", "''"))
                        .concat("'")
                        .concat(",")

                        .concat("'")
                        .concat(OBJLayer.getLayerValue())
                        .concat("'")


                        .concat(");");
                SQLiteStatement statementInsertItemLayer = db.compileStatement(strSql);
                int resultInsertItemLayer = statementInsertItemLayer.executeUpdateDelete();
                Log.e("INSERT", "Inventory--->>" + strSql);
                Log.e("resultInsertTags", "Inventory--->>" + resultInsertItemLayer);
            }


        }


        db.close();
        return result;
    }


    @SuppressLint("WrongConstant")
    public static int updateTax(Context c) {
        int result = 0;
        try {

            db = c.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "UPDATE [tbl_LayerItem_Master] set"
                    .concat("[TAX_TYPE] = 'EXCLUSIVE' ")
                    .concat("WHERE [TAX_APPLY] = 'YES' AND ([TAX_TYPE] IS NULL OR [TAX_TYPE]='')");


/*
            String qry = "UPDATE [tbl_LayerItem_Master] set"
                    .concat("[TAX_TYPE] = 'EXCLUSIVE' ")
                    .concat("WHERE [TAX_APPLY] = 'YES' ");*/


            Log.e("--UpdateTax--", "LayerItem: " + qry);


            SQLiteStatement statementInsertItemLayer = db.compileStatement(qry);
            int resultInsertItemLayer = statementInsertItemLayer.executeUpdateDelete();
            Log.e("--result--", "updateTax--->>" + qry);
            Log.e("--result--", "updateTax--->>" + resultInsertItemLayer);

            db.close();

        } catch (Exception e) {
            Log.e("ClsUnit", "Update" + e.getMessage());
        }
        return result;
    }


    @SuppressLint("WrongConstant")
    public static int TestQry(Context c) {
        int result = 0;
        try {

            db = c.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
/*
            String qry = "UPDATE [tbl_LayerItem_Master] set"
                    .concat("[TAX_TYPE] = 'EXCLUSIVE' ")
                    .concat("WHERE [TAX_APPLY] = 'YES' AND [TAX_TYPE] = ''");*/

            String qry = "UPDATE [tbl_LayerItem_Master] set"
                    .concat("[TAX_APPLY] = 'YES' , [TAX_SLAB_ID] = 1 ")
                    .concat(" where [ITEM_CODE] in ('22','83')");

            Log.e("--UpdateTax--", "LayerItem: " + qry);


            SQLiteStatement statementInsertItemLayer = db.compileStatement(qry);
            int resultInsertItemLayer = statementInsertItemLayer.executeUpdateDelete();
            Log.e("--UpdateTax--", "Inventory- " + qry);
            Log.e("--UpdateTax--", "result: " + resultInsertItemLayer);


            db.close();

        } catch (Exception e) {
            Log.e("ClsUnit", "Update" + e.getMessage());
        }
        return result;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsSelectionModel> getItemCharList(String where, Context context) {
        List<ClsSelectionModel> _list = new ArrayList<>();
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);


            String _charQry = "SELECT UPPER([ITEM_NAME]) AS [ITEM_NAME] FROM [tbl_LayerItem_Master] WHERE 1=1 "
                    .concat(where).concat(" ORDER BY [ITEM_NAME] ");


            Log.e("--ItemChar--", "getItemCharList: " + _charQry);

            Cursor cur = db.rawQuery(_charQry, null);

            for (String c : cur.getColumnNames()) {
                Log.e("--ItemChar--", "columnName: " + c);
            }

            Log.e("--ItemChar--", "count: " + cur.getCount());

            while (cur.moveToNext()) {

                ClsSelectionModel Obj = new ClsSelectionModel();
                Obj.set_character(String.valueOf(cur.getString(cur.getColumnIndex("ITEM_NAME")).charAt(0)));
                Obj.setSelected(false);
                _list.add(Obj);
            }

            if (_list != null && _list.size() != 0) {
                List<String> uniqueList = new ArrayList<>();
                for (ClsSelectionModel stock : _list) {
                    if (!uniqueList.contains(stock.get_character())) {
                        uniqueList.add(stock.get_character());
                    }
                }
                _list.clear();

                for (String _c : uniqueList) {
                    ClsSelectionModel Obj = new ClsSelectionModel();
                    Obj.set_character(_c);
                    Obj.setSelected(false);
                    _list.add(Obj);
                }
            }


            db.close();
        } catch (Exception e) {
            Log.e("--ItemChar--", "Exception: " + e.getMessage());
            e.getMessage();
        }
        return _list;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsLayerItemMaster> getStockOutList(String _where, Context context) {
        List<ClsLayerItemMaster> lstClsPurchaseDetails = new ArrayList<>();
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
                ClsLayerItemMaster Obj = new ClsLayerItemMaster();
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

    @SuppressLint("WrongConstant")
//    public static List<ClsLayerItemMaster> getLayerItemListNew(Context context, String _where, String _whereSearch) {
    public static List<ClsLayerItemMaster> getLayerItemListNew(Context context, String _where,
                                                               String _whereSearch, String paging) {

        ClsLayerItemMaster getSet = new ClsLayerItemMaster();

        List<ClsLayerItemMaster> lstClsStocksOut = new ArrayList<>();
        lstClsStocksOut = new ClsLayerItemMaster(context).getStockOutList("", context);


        Gson gsonOut = new Gson();
        String jsonInString2gsonOut = gsonOut.toJson(lstClsStocksOut);
        Log.e("--GSON--", "StockSaleOUT:--- " + jsonInString2gsonOut);


        List<ClsLayerItemMaster> listItemMaster = new ArrayList<>();


        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            Log.e("--QRY--", "TRY_where: " + _where);

            String old_qry = "SELECT "
                    .concat("[ITM].[LAYERITEM_ID]")
                    .concat(",[ITM].[ITEM_NAME]")
                    .concat(",[ITM].[ITEM_CODE]")
                    .concat(",IFNULL([ITM].[RATE_PER_UNIT],0) AS [RATE_PER_UNIT]")
                    .concat(",[ITM].[UNIT_CODE]")
                    .concat(",IFNULL([ITM].[HSN_SAC_CODE],'') AS [HSN_SAC_CODE]")
                    .concat(",[ITM].[TAX_APPLY]")
                    .concat(",[ITM].[TAX_SLAB_ID]")

                    .concat(",[TS].[SLAB_NAME]")
                    .concat(",[TS].[SGST]")
                    .concat(",[TS].[CGST]")
                    .concat(",[TS].[IGST]")

                    .concat(",SUM(IFNULL([PD].[Quantity],0)) AS [IN] ")
//                    .concat(",0 AS [IN] ")
                    .concat(",0 AS [OUT] ")

                    .concat(",[ITM].[MIN_STOCK]")
                    .concat(",[ITM].[MAX_STOCK]")
                    .concat(",[ITM].[OPENING_STOCK]")
                    .concat(",IFNULL([ITM].[WHOLESALE_RATE],0) AS [WHOLESALE_RATE]")
                    .concat(",[ITM].[TAX_TYPE]")

                    .concat(" FROM [tbl_LayerItem_Master] AS [ITM]")
                    .concat(" LEFT JOIN [tbl_Tax_Slab] AS [TS] ON [TS].[SLAB_ID]= [ITM].[TAX_SLAB_ID]")
                    .concat(" LEFT JOIN [PurchaseDetail] AS [PD] ON [PD].[ItemID] = [ITM].[LAYERITEM_ID] ")

                    .concat(" WHERE 1=1")
                    .concat(_where)
                    .concat(_whereSearch)

                    .concat(" GROUP BY [ITM].[ITEM_CODE] ")
                    .concat(" ORDER BY upper([ITM].[ITEM_NAME]) ASC ")
//                    .concat(" LIMIT 10 ,10");
                    .concat(paging);


            Log.e("--QRY--", "qry:- " + old_qry);


            Cursor cur = db.rawQuery(old_qry, null);

            Log.e("--QRY--", "LayerItemNewCount: " + cur.getCount());

            while (cur.moveToNext()) {

                getSet = new ClsLayerItemMaster();
                int _itemID = cur.getInt(cur.getColumnIndex("LAYERITEM_ID"));
                getSet.setLAYERITEM_ID(_itemID);

                getSet.setITEM_NAME(cur.getString(cur.getColumnIndex("ITEM_NAME")));
//                getSet.setITEM_NAME(cur.getString(cur.getColumnIndex("ITEM_NAME")).concat(cur.getString(cur.getColumnIndex("EntryDate"))));
                getSet.setITEM_CODE(cur.getString(cur.getColumnIndex("ITEM_CODE")));
                getSet.setMIN_STOCK(cur.getDouble(cur.getColumnIndex("MIN_STOCK")));
                getSet.setMAX_STOCK(cur.getDouble(cur.getColumnIndex("MAX_STOCK")));
                getSet.setUNIT_CODE(cur.getString(cur.getColumnIndex("UNIT_CODE")));

                getSet.setHSN_SAC_CODE(cur.getString(cur.getColumnIndex("HSN_SAC_CODE")));

                getSet.setSLAB_NAME(cur.getString(cur.getColumnIndex("SLAB_NAME")));

                getSet.setTAX_SLAB_ID(cur.getInt(cur.getColumnIndex("TAX_SLAB_ID")));
                getSet.setTAX_APPLY(cur.getString(cur.getColumnIndex("TAX_APPLY")));
                getSet.setRATE_PER_UNIT(cur.getDouble(cur.getColumnIndex("RATE_PER_UNIT")));
                getSet.setOpening_Stock(cur.getDouble(cur.getColumnIndex("OPENING_STOCK")));

                getSet.setSGST(cur.getDouble(cur.getColumnIndex("SGST")));
                getSet.setCGST(cur.getDouble(cur.getColumnIndex("CGST")));
                getSet.setIGST(cur.getDouble(cur.getColumnIndex("IGST")));

                getSet.setLastPurchasePrice(getLastPurchaseValue(context, _itemID));

                getSet.setWHOLESALE_RATE(cur.getDouble(cur.getColumnIndex("WHOLESALE_RATE")));
                getSet.setTAX_TYPE(cur.getString(cur.getColumnIndex("TAX_TYPE")));

                getSet.setIN(cur.getDouble(cur.getColumnIndex("IN")));
                getSet.setOUT(cur.getDouble(cur.getColumnIndex("OUT")));

                List<String> itmTagList = new ArrayList<>();

                List<String> itemLayerValues = getLayerValuesByItemId(context, " AND [ITEM_ID] =".concat(String.valueOf(_itemID)));
                if (itemLayerValues != null && itemLayerValues.size() != 0) {
                    itmTagList.addAll(itemLayerValues);
                }


                String _whereTag = " AND [ITEMID] = ".concat(String.valueOf(_itemID));
                List<String> tagList = getTagsByItemIdList(context, _whereTag);
                if (tagList != null && tagList.size() != 0)
                    itmTagList.addAll(tagList);

                getSet.setTagList(itmTagList);
                //Tax calculation amount

                if (getSet.getTAX_APPLY() != null && getSet.getTAX_APPLY().equalsIgnoreCase("YES")) {

                    /*PRICE : 100
                     TAX1: 1.5%
                     TAX2: 1.5%
                     TAX3: 1%
                     TOTAL TAX:4%

                     VALUE  = PRICE * (TOTAL TAX / (PRICE + TOTAL TAX))
                     VALUE  = 100 * (4 / ( 100 + 4))*/

                    if (getSet.getTAX_TYPE() != null && getSet.getTAX_TYPE().equalsIgnoreCase("INCLUSIVE")) {

                        // Sale Rate calculation
                        double _rate = getSet.getRATE_PER_UNIT();
                        double _totalTax = getSet.getCGST() + getSet.getSGST() + getSet.getIGST();
//                        Double _value = _rate * (_totalTax / (_rate + _totalTax));

                        double _value = _rate / (100 + _totalTax) * 100;

//                        Double _rateWhitoutTax = _rate - ClsGlobal.round(_value, 2);
                        Log.d("--value--", "_valueSaleRate: " + _value);

                        getSet.set_saleRateIncludingTax(Double.valueOf(ClsGlobal.round(_value, 2)));

                        // Wholesale rate calculation
                        _rate = getSet.getWHOLESALE_RATE();
//                        _value = _rate * (_totalTax / (_rate + _totalTax));

                        _value = _rate / (100 + _totalTax) * 100;

//                        _rateWhitoutTax = _rate - ClsGlobal.round(_value, 2);;

                        getSet.set_wholesaleRateIncludingTax(Double.valueOf(ClsGlobal.round(_value, 2)));

                    } else {

                        // Sale Rate calculation

                        getSet.set_saleRateIncludingTax(Double.valueOf(ClsGlobal.round(getSet.getRATE_PER_UNIT(), 2)));

                        // Wholesale rate calculation

                        getSet.set_wholesaleRateIncludingTax(Double.valueOf(ClsGlobal.round(getSet.getWHOLESALE_RATE(), 2)));

                    }

                } else {
                    getSet.set_saleRateIncludingTax(Double.valueOf(ClsGlobal.round(getSet.getRATE_PER_UNIT(), 2)));
                    getSet.set_wholesaleRateIncludingTax(Double.valueOf(ClsGlobal.round(getSet.getWHOLESALE_RATE(), 2)));

                }

                listItemMaster.add(getSet);
                Log.e("--QRY--", "LayerItemNewCount: ");
            }
            Log.e("--QRY--", "LayerItemNewCount: " + listItemMaster.size());

            if (listItemMaster != null && listItemMaster.size() != 0) {

                if (lstClsStocksOut != null && lstClsStocksOut.size() != 0) {
                    for (ClsLayerItemMaster obj : listItemMaster) {

                        for (ClsLayerItemMaster objClsStockOut : lstClsStocksOut) {

                            if (obj.getITEM_CODE().equalsIgnoreCase(objClsStockOut.getITEM_CODE())) {
                                obj.setOUT(objClsStockOut.getOUT());
                                obj.setAverageSaleRate(objClsStockOut.getAverageSaleRate());
                                listItemMaster.set(listItemMaster.indexOf(obj), obj);
                                break;
                            }
                        }
                    }
                }
            }

            db.close();

//            listItemMaster = sortAndAddSections(listItemMaster);

        } catch (Exception e) {
            Log.e("ClsItem", "ClsItem >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return listItemMaster;
    }


    @SuppressLint("WrongConstant")
    public static Double getLastPurchaseValue(Context context, int _itemID) {
        double _lastPuchaseAmt = 0.0;
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            String qry = "SELECT "
                    .concat(" [PD].Rate from PurchaseMaster ")
                    .concat(" PM LEFT JOIN PurchaseDetail AS PD ")
                    .concat(" ON PD.PurchaseID = PM.PurchaseID WHERE")
                    .concat(" [PD].ItemID = " + _itemID)
                    .concat(" ORDER BY [PM].EntryDate DESC LIMIT 1 ");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--TotalSaleAmount--", "qry:  " + qry);
            Log.e("--TotalSaleAmount--", "TotalSaleAmount:  " + cur.getCount());

            while (cur.moveToNext()) {
                _lastPuchaseAmt = cur.getDouble(cur.getColumnIndex("Rate"));
            }
            cur.close();
        } catch (Exception e) {
            e.getMessage();
            Log.e("ClsCheckInMaster", "getCheckInNo-->>>>>>>>" + e.getMessage());
        }
        return _lastPuchaseAmt;
    }


    @SuppressLint("WrongConstant")
    public static int gettotalItemCount(Context context, String _where, String _whereSearch) {
        int result = 0;
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            Log.e("--QRY--", "TRY_where: " + _where);

            String qry = "SELECT 1 "
                    .concat(" FROM [tbl_LayerItem_Master] AS [ITM]")
                    .concat(" LEFT JOIN [tbl_Tax_Slab] AS [TS] ON [TS].[SLAB_ID]= [ITM].[TAX_SLAB_ID]")
                    .concat(" LEFT JOIN [PurchaseDetail] AS [PD] ON [PD].[ItemID] = [ITM].[LAYERITEM_ID] ")

                    .concat(" WHERE 1=1")
                    .concat(_where)
                    .concat(_whereSearch)

                    .concat(" GROUP BY [ITM].[ITEM_CODE] ")
                    .concat(" ORDER BY [ITM].[ITEM_NAME] ASC ");

            Log.e("--QRY--", "qry:- " + qry);

            Cursor cur = db.rawQuery(qry, null);
            result = cur.getCount();
            Log.e("ClsItem", "result Item Count:-" + result);
            db.close();


        } catch (Exception e) {
            Log.e("ClsItem", "ClsItem >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return result;
    }


    private static List<ClsLayerItemMaster> sortAndAddSections(List<ClsLayerItemMaster> itemList) {

        List<ClsLayerItemMaster> tempList = new ArrayList<>();
        //First we sort the array

        //Loops thorugh the list and add a section before each sectioncell start
        String header = "";
        for (int i = 0; i < itemList.size(); i++) {
            //If it is the start of a new section we create a new listcell and add it to our array
            if (!(header.equals(String.valueOf(itemList.get(i).getITEM_NAME().charAt(0)).toUpperCase()))) {
                ClsLayerItemMaster sectionCell = new ClsLayerItemMaster(String.valueOf(itemList.get(i).getITEM_NAME()
                        .charAt(0)).toUpperCase());
                sectionCell.setHeader(true);
                tempList.add(sectionCell);
                header = String.valueOf(itemList.get(i).getITEM_NAME().charAt(0)).toUpperCase();
                Log.e("header", header);
                Log.e("check", "inside if");
            }

            Log.e("check", "outside if");
            tempList.add(itemList.get(i));
        }


        return tempList;
    }


    @SuppressLint("WrongConstant")
    public static ClsLayerItemMaster getItem(Context context, String _where, String _whereSearch) {

        ClsLayerItemMaster getSet = new ClsLayerItemMaster();

        List<ClsLayerItemMaster> lstClsStocksOut = new ArrayList<>();
        lstClsStocksOut = new ClsLayerItemMaster(context).getStockOutList("", context);


        Gson gsonOut = new Gson();
        String jsonInString2gsonOut = gsonOut.toJson(lstClsStocksOut);
        Log.e("--GSON--", "StockSaleOUT:--- " + jsonInString2gsonOut);


        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            Log.e("--QRY--", "TRY_where: " + _where);

            String qry = "SELECT "
                    .concat("[ITM].[LAYERITEM_ID]")
                    .concat(",[ITM].[ITEM_NAME]")
                    .concat(",[ITM].[ITEM_CODE]")
                    .concat(",IFNULL([ITM].[RATE_PER_UNIT],0) AS [RATE_PER_UNIT]")
                    .concat(",[ITM].[UNIT_CODE]")
                    .concat(",IFNULL([ITM].[HSN_SAC_CODE],'') AS [HSN_SAC_CODE]")
                    .concat(",[ITM].[TAX_APPLY]")
                    .concat(",[ITM].[TAX_SLAB_ID]")

                    .concat(",[TS].[SLAB_NAME]")
                    .concat(",[TS].[SGST]")
                    .concat(",[TS].[CGST]")
                    .concat(",[TS].[IGST]")

                    .concat(",SUM(IFNULL([PD].[Quantity],0)) AS [IN] ")
//                    .concat(",0 AS [IN] ")
                    .concat(",0 AS [OUT] ")

                    .concat(",[ITM].[MIN_STOCK]")
                    .concat(",[ITM].[MAX_STOCK]")
                    .concat(",[ITM].[OPENING_STOCK]")
                    .concat(",IFNULL([ITM].[WHOLESALE_RATE],0) AS [WHOLESALE_RATE]")
//                    .concat(",[ITM].[TAX_TYPE]")
                    .concat(",IFNULL([ITM].[TAX_TYPE],'EXCLUSIVE') AS [TAX_TYPE]")

                    .concat(" FROM [tbl_LayerItem_Master] AS [ITM]")
                    .concat(" LEFT JOIN [tbl_Tax_Slab] AS [TS] ON [TS].[SLAB_ID]= [ITM].[TAX_SLAB_ID]")
                    .concat(" LEFT JOIN [PurchaseDetail] AS [PD] ON [PD].[ItemID] = [ITM].[LAYERITEM_ID] ")

                    .concat(" WHERE 1=1")
                    .concat(_where)
                    .concat(_whereSearch)

                    .concat(" GROUP BY [ITM].[ITEM_CODE] ")
                    .concat(" ORDER BY [ITM].[ITEM_NAME] ASC ");

            Log.e("--QRY--", "LayerItemListNew: " + qry);

            Cursor cur = db.rawQuery(qry, null);
            Log.e("--QRY--", "LayerItemNewCount: " + cur.getCount());

            while (cur.moveToNext()) {

                getSet = new ClsLayerItemMaster();
                int _itemID = cur.getInt(cur.getColumnIndex("LAYERITEM_ID"));
                getSet.setLAYERITEM_ID(_itemID);

                getSet.setITEM_NAME(cur.getString(cur.getColumnIndex("ITEM_NAME")));
                getSet.setITEM_CODE(cur.getString(cur.getColumnIndex("ITEM_CODE")));
                getSet.setMIN_STOCK(cur.getDouble(cur.getColumnIndex("MIN_STOCK")));
                getSet.setMAX_STOCK(cur.getDouble(cur.getColumnIndex("MAX_STOCK")));
                getSet.setUNIT_CODE(cur.getString(cur.getColumnIndex("UNIT_CODE")));

                getSet.setHSN_SAC_CODE(cur.getString(cur.getColumnIndex("HSN_SAC_CODE")));

                getSet.setSLAB_NAME(cur.getString(cur.getColumnIndex("SLAB_NAME")));

                getSet.setTAX_SLAB_ID(cur.getInt(cur.getColumnIndex("TAX_SLAB_ID")));
                getSet.setTAX_APPLY(cur.getString(cur.getColumnIndex("TAX_APPLY")));
                getSet.setRATE_PER_UNIT(cur.getDouble(cur.getColumnIndex("RATE_PER_UNIT")));
                getSet.setOpening_Stock(cur.getDouble(cur.getColumnIndex("OPENING_STOCK")));

                getSet.setSGST(cur.getDouble(cur.getColumnIndex("SGST")));
                getSet.setCGST(cur.getDouble(cur.getColumnIndex("CGST")));
                getSet.setIGST(cur.getDouble(cur.getColumnIndex("IGST")));

                getSet.setWHOLESALE_RATE(cur.getDouble(cur.getColumnIndex("WHOLESALE_RATE")));
                getSet.setTAX_TYPE(cur.getString(cur.getColumnIndex("TAX_TYPE")));

                getSet.setIN(cur.getDouble(cur.getColumnIndex("IN")));
                getSet.setOUT(cur.getDouble(cur.getColumnIndex("OUT")));

                List<String> itmTagList = new ArrayList<>();

                List<String> itemLayerValues = getLayerValuesByItemId(context, " AND [ITEM_ID] =".concat(String.valueOf(_itemID)));
                if (itemLayerValues != null && itemLayerValues.size() != 0) {
                    itmTagList.addAll(itemLayerValues);
                }

                Gson gson = new Gson();
                String jsonInString = gson.toJson(itemLayerValues);
                Log.d("Result", "itemLayerValues- " + jsonInString);


                String _whereTag = " AND [ITEMID] = ".concat(String.valueOf(_itemID));
                List<String> tagList = getTagsByItemIdList(context, _whereTag);
                if (tagList != null && tagList.size() != 0)
                    itmTagList.addAll(tagList);

                getSet.setTagList(itmTagList);


                Gson gson2 = new Gson();
                String jsonInString2 = gson2.toJson(getSet);
                Log.e("--QRY--", "lstClsStocksOut:--- " + jsonInString2);


                //Tax calculation amount

                if (getSet.getTAX_APPLY() != null && getSet.getTAX_APPLY().equalsIgnoreCase("YES")) {

                    /*PRICE : 100
                     TAX1: 1.5%
                     TAX2: 1.5%
                     TAX3: 1%
                     TOTAL TAX:4%

                     VALUE  = PRICE * (TOTAL TAX / (PRICE + TOTAL TAX))
                     VALUE  = 100 * (4 / ( 100 + 4))*/


                    if (getSet.getTAX_TYPE() != null && getSet.getTAX_TYPE().equalsIgnoreCase("INCLUSIVE")) {

                        // Sale Rate calculation
                        double _rate = getSet.getRATE_PER_UNIT();
                        double _totalTax = getSet.getCGST() + getSet.getSGST() + getSet.getIGST();
//                        double _value = _rate * (_totalTax / (_rate + _totalTax));

                        double _value = _rate / (100 + _totalTax) * 100;


//                        double _rateWhitoutTax = _rate - ClsGlobal.round(_value, 2);
                        Log.d("--value--", "_valueSaleRate: " + _value);


                        getSet.set_saleRateIncludingTax(Double.valueOf(ClsGlobal.round(_value, 2)));

                        // Wholesale rate calculation
                        _rate = getSet.getWHOLESALE_RATE();
//                        _value = _rate * (_totalTax / (_rate + _totalTax));

                        _value = _rate / (100 + _totalTax) * 100;

//                        _rateWhitoutTax = _rate - ClsGlobal.round(_value, 2);;


                        getSet.set_wholesaleRateIncludingTax(Double.valueOf(ClsGlobal.round(_value, 2)));


                    } else {

                        // Sale Rate calculation

                        getSet.set_saleRateIncludingTax(Double.valueOf(ClsGlobal.round(getSet.getRATE_PER_UNIT(), 2)));

                        // Wholesale rate calculation

                        getSet.set_wholesaleRateIncludingTax(Double.valueOf(ClsGlobal.round(getSet.getWHOLESALE_RATE(), 2)));

                    }

                } else {
                    getSet.set_saleRateIncludingTax(Double.valueOf(ClsGlobal.round(getSet.getRATE_PER_UNIT(), 2)));
                    getSet.set_wholesaleRateIncludingTax(Double.valueOf(ClsGlobal.round(getSet.getWHOLESALE_RATE(), 2)));

                }

            }


           /* if (listItemMaster != null && listItemMaster.size() != 0) {

                if (lstClsStocksOut != null && lstClsStocksOut.size() != 0) {
                    for (ClsLayerItemMaster obj : listItemMaster) {

                        for (ClsLayerItemMaster objClsStockOut : lstClsStocksOut) {

                            if (obj.getITEM_CODE().equalsIgnoreCase(objClsStockOut.getITEM_CODE())) {
                                obj.setOUT(objClsStockOut.getOUT());
                                obj.setAverageSaleRate(objClsStockOut.getAverageSaleRate());
                                listItemMaster.set(listItemMaster.indexOf(obj), obj);
                                break;
                            }
                        }
                    }
                }
            }*/

            db.close();
        } catch (Exception e) {
            Log.e("ClsItem", "ClsItem >>>>GetList" + e.getMessage());
            e.getMessage();
        }
        return getSet;
    }


    @SuppressLint("WrongConstant")
    public static List<String> getItemMasterColumn(Context context,io.requery.android.database.sqlite
            .SQLiteDatabase db) {
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


    @SuppressLint("WrongConstant")
    public static long getAutoGeneratedItemCode(Context context) {
        long nextItemCode = 0;
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String qry = "SELECT MAX(IFNULL([AUTO_GENERATED_ITEM_CODE],0)) " +
                    "as [AUTO_GENERATED_ITEM_CODE]  FROM " +
                    "[tbl_LayerItem_Master] ";
            Cursor cur = db.rawQuery(qry, null);
            Log.e("--Select--", ">>>>>>>>>" + cur.getCount());
            if (cur != null) {
                while (cur.moveToNext()) {
                    if (cur.getString(cur.getColumnIndex(
                            "AUTO_GENERATED_ITEM_CODE")) == null) {
                        nextItemCode = 0;
                    } else {
//                        qry = "select 1 from [tbl_LayerItem_Master] where [ITEM_CODE] = "
                        nextItemCode = Long.valueOf(cur.getString(cur.getColumnIndex(
                                "AUTO_GENERATED_ITEM_CODE")));
                    }
                }
            }

            nextItemCode += 1;
            Log.e("ClsCategory", "nextItemCode + 1" + nextItemCode);
            db.close();
        } catch (Exception e) {
            Log.e("ClsCategory", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }
        return nextItemCode;
    }


}
