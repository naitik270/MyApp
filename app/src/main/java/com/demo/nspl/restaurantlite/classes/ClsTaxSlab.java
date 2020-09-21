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

public class ClsTaxSlab {

    int taxSlabId;

    String SLAB_NAME, ACTIVE, REMARK;
    Double SGST, CGST, IGST;
    private static SQLiteDatabase db;
    static Context context;

    public ClsTaxSlab(Context context) {
        ClsTaxSlab.context = context;
    }

    public ClsTaxSlab() {

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

    public int getTaxSlabId() {
        return taxSlabId;
    }

    public void setTaxSlabId(int taxSlabId) {
        this.taxSlabId = taxSlabId;
    }

    public String getSLAB_NAME() {
        return SLAB_NAME;
    }

    public void setSLAB_NAME(String SLAB_NAME) {
        this.SLAB_NAME = SLAB_NAME;
    }


    public String getACTIVE() {
        return ACTIVE;
    }

    public void setACTIVE(String ACTIVE) {
        this.ACTIVE = ACTIVE;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    @SuppressLint("WrongConstant")
    public static int Insert(Context context,ClsTaxSlab objTaxSlab,
                             io.requery.android.database.sqlite.SQLiteDatabase db) {
        int result = 0;


        try {

//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String qry = ("INSERT INTO [tbl_Tax_Slab] ([SLAB_NAME]," +
                    "[SGST],[CGST],[IGST],[ACTIVE],[REMARK]) VALUES ('")

                    .concat(objTaxSlab.getSLAB_NAME().trim()
                            .replace("'","''"))
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(objTaxSlab.getSGST()))
                    .concat(",")

                    .concat(String.valueOf(objTaxSlab.getCGST()))
                    .concat(",")

                    .concat(String.valueOf(objTaxSlab.getIGST()))
                    .concat(",")

                    .concat("'")
                    .concat(String.valueOf(objTaxSlab.getACTIVE()))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objTaxSlab.getREMARK().trim()
                            .replace("'","''"))
                    .concat("'")

                    .concat(");");

            io.requery.android.database.sqlite.SQLiteStatement
                    statement = db.compileStatement(qry);

            result = statement.executeUpdateDelete();
            Log.e("INSERT", "Items--->>" + qry);

//            db.close();
            return result;

        } catch (Exception e) {
            Log.e("ClsTerms", "Insert>>>>>>>>" + e.getMessage());
            e.getMessage();
            db.close();
        }
        return result;

    }




    @SuppressLint("WrongConstant")
    public static List<ClsTaxSlab> getList() {
        List<ClsTaxSlab> list = new ArrayList<>();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

            String qry = "SELECT * FROM [tbl_Tax_Slab]";

            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));
            Log.e("qry", qry);


            while (cur.moveToNext()) {
                ClsTaxSlab getSet = new ClsTaxSlab();
                getSet.setTaxSlabId(cur.getInt(cur.getColumnIndex("SLAB_ID")));
                getSet.setSLAB_NAME(cur.getString(cur.getColumnIndex("SLAB_NAME")));

                getSet.setSGST(cur.getDouble(cur.getColumnIndex("SGST")));
                getSet.setCGST(cur.getDouble(cur.getColumnIndex("CGST")));
                getSet.setIGST(cur.getDouble(cur.getColumnIndex("IGST")));

                getSet.setACTIVE(cur.getString(cur.getColumnIndex("ACTIVE")));
                getSet.setREMARK(cur.getString(cur.getColumnIndex("REMARK")));

                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsTaxSlab> getTaxSlabNameList(String where) {
        List<ClsTaxSlab> list = new ArrayList<>();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

          /*  String qry = ("SELECT [SLAB_ID],[SLAB_NAME] FROM [tbl_Tax_Slab] " +
                    "WHERE 1=1 ")
                    .concat(where)
                    .concat(";");
*/
            String qry = ("SELECT * FROM [tbl_Tax_Slab] " +
                    "WHERE 1=1 ")
                    .concat(where)
                    .concat(";");


            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));
            Log.e("qry", qry);


            while (cur.moveToNext()) {
                ClsTaxSlab getSet = new ClsTaxSlab();
                getSet.setTaxSlabId(cur.getInt(cur.getColumnIndex("SLAB_ID")));
                getSet.setSLAB_NAME(cur.getString(cur.getColumnIndex("SLAB_NAME")));

                getSet.setSGST(cur.getDouble(cur.getColumnIndex("SGST")));
                getSet.setCGST(cur.getDouble(cur.getColumnIndex("CGST")));
                getSet.setIGST(cur.getDouble(cur.getColumnIndex("IGST")));

                getSet.setACTIVE(cur.getString(cur.getColumnIndex("ACTIVE")));
                getSet.setREMARK(cur.getString(cur.getColumnIndex("REMARK")));

                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public boolean checkExists(String _where, io.requery.android.database.sqlite.SQLiteDatabase db) {
        boolean result = false;

        try {


//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = "SELECT 1 FROM [tbl_Tax_Slab] WHERE 1=1 "
                    .concat(_where)
                    .concat(";");
            Log.e("exists", "existsChqQry - " + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            if (cur != null && cur.getCount() != 0) {
                result = true;
            }
//            db.close();
        } catch (Exception e) {
            Log.e("--ClsTaxSlab--", "Slab: " + e.getMessage());
            e.getMessage();
        }
        return result;

    }


    @SuppressLint("WrongConstant")
    public static ClsTaxSlab QueryById(int Id, Context context) {
        ClsTaxSlab Obj = new ClsTaxSlab();
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String sqlStr = "SELECT * FROM [tbl_Tax_Slab] WHERE 1=1 AND [SLAB_ID] = "
                    .concat(String.valueOf(Id))
                    .concat(";");

            Log.e("Update", ">>>>>>>>>" + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            Log.e("Update", ">>>>>>>>>" + cur.getCount());

            while (cur.moveToNext()) {
                Obj.setTaxSlabId(cur.getInt(cur.getColumnIndex("SLAB_ID")));
                Obj.setSLAB_NAME(cur.getString(cur.getColumnIndex("SLAB_NAME")));

                Obj.setSGST(cur.getDouble(cur.getColumnIndex("SGST")));
                Obj.setCGST(cur.getDouble(cur.getColumnIndex("CGST")));
                Obj.setIGST(cur.getDouble(cur.getColumnIndex("IGST")));

                Obj.setACTIVE(cur.getString(cur.getColumnIndex("ACTIVE")));
                Obj.setREMARK(cur.getString(cur.getColumnIndex("REMARK")));
            }

            db.close();

        } catch (Exception e) {
            Log.e("ClsCategory", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }
        return Obj;
    }


    @SuppressLint("WrongConstant")
    public static int Update(ClsTaxSlab ObjClsTaxSlab,
                             Context context) {
        int result = 0;

        Log.e("Update", "Query..");

        //----------------------------- Update into tbl_LayerItem_Master ------------------------------//

        db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);

        String strSql = "UPDATE [tbl_Tax_Slab] SET "

                .concat(" [SLAB_NAME] = ")
                .concat("'")
                .concat(ClsGlobal.getDbDateFormat(ObjClsTaxSlab.getSLAB_NAME().trim()
                        .replace("'", "''")))
                .concat("'")

                .concat(", [SGST] = ")
                .concat(String.valueOf(ObjClsTaxSlab.getSGST()))

                .concat(", [CGST] = ")
                .concat(String.valueOf(ObjClsTaxSlab.getCGST()))

                .concat(", [IGST] = ")
                .concat(String.valueOf(ObjClsTaxSlab.getIGST()))

                .concat(", [ACTIVE] = ")
                .concat("'")
                .concat(String.valueOf(ObjClsTaxSlab.getACTIVE()))
                .concat("'")

                .concat(", [REMARK] = ")
                .concat("'")
                .concat(ObjClsTaxSlab.getREMARK().trim()
                        .replace("'", "''"))
                .concat("'")

                .concat(" WHERE [SLAB_ID] = ")
                .concat(String.valueOf(ObjClsTaxSlab.getTaxSlabId()))
                .concat(";");

        Log.e("Update", strSql);
        SQLiteStatement statementUpdate = db.compileStatement(strSql);
        result = statementUpdate.executeUpdateDelete();


        db.close();
        return result;
    }


    @SuppressLint("WrongConstant")
    public boolean checkExists(String _where) {
        boolean result = false;

        try {


            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = "SELECT 1 FROM [tbl_Tax_Slab] WHERE 1=1 "
                    .concat(_where)
                    .concat(";");
            Log.e("exists", "existsChqQry - " + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            if (cur != null && cur.getCount() != 0) {
                result = true;
            }
            db.close();
        } catch (Exception e) {
            Log.e("--ClsTaxSlab--", "Slab: " + e.getMessage());
            e.getMessage();
        }
        return result;


    }


    @SuppressLint("WrongConstant")
    public static int Delete(ClsTaxSlab objClsTaxSlab, Context context) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String strSql = "DELETE FROM [tbl_Tax_Slab] WHERE [SLAB_ID] = "
                    .concat(String.valueOf(objClsTaxSlab.getTaxSlabId()))
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

}
