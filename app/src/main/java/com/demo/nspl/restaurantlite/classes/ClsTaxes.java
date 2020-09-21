package com.demo.nspl.restaurantlite.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desktop on 3/26/2018.
 */

public class ClsTaxes {
    int tax_id;
    String tax_type,tax_name;
    Double tax_value;


    public String getTax_amount() {
        return tax_amount;
    }

    public void setTax_amount(String tax_amount) {
        this.tax_amount = tax_amount;
    }

    String tax_amount;

    public Double getTax_value() {
        return tax_value;
    }

    public void setTax_value(Double tax_value) {
        this.tax_value = tax_value;
    }

    public int getTax_id() {
        return tax_id;
    }

    public void setTax_id(int tax_id) {
        this.tax_id = tax_id;
    }

    public String getTax_type() {
        return tax_type;
    }

    public void setTax_type(String tax_type) {
        this.tax_type = tax_type;
    }

    public String getTax_name() {
        return tax_name;
    }

    public void setTax_name(String tax_name) {
        this.tax_name = tax_name;
    }



    static Context context;
    AppCompatActivity activity;
    private static SQLiteDatabase db;

    public ClsTaxes() {

    }

    public ClsTaxes(String tax_name,Double tax_value){
        this.tax_name = tax_name;
        this.tax_value = tax_value;
    }

    public ClsTaxes(Context ctx) {

        context = ctx;
    }




    @SuppressLint("WrongConstant")
    public static int Insert(List<ClsTaxes >ObjTaxes) {

        int  result = 0;
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String deleteQry = "DELETE FROM [Taxes] ".concat(";");
            SQLiteStatement statement = db.compileStatement(deleteQry);
            result = statement.executeUpdateDelete();
            Log.e("ClsTaxes", ">>Insert: " + deleteQry);

            for (ClsTaxes Obj : ObjTaxes) {
                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
                String qry = "INSERT INTO [Taxes] ([TAX_TYPE],[TAX_NAME],[TAX_VALUE]) VALUES ('"

                    .concat(Obj.getTax_type().trim()
                            .replace("'","''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(Obj.getTax_name().trim()
                            .replace("'","''"))
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(Obj.getTax_value()))

                    .concat(");");

            SQLiteStatement statementinsert = db.compileStatement(qry);
            result = statementinsert.executeUpdateDelete();
            Log.e("INSERT", "Taxes--->>" + qry);
        }
            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsTaxes", "Taxes>>>>>>>>" + e.getMessage());
            e.getMessage();
        }
        return result;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsTaxes> getList(String _where) {
        List<ClsTaxes> list = new ArrayList<>();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            Cursor cur = db.rawQuery("SELECT [TAX_ID],[TAX_TYPE],[TAX_NAME],[TAX_VALUE] FROM [Taxes] WHERE 1=1 ".concat(_where).concat(";"), null);

            Log.e("cur_Count","Count" +cur.getCount());

            while (cur.moveToNext()) {
                ClsTaxes getSet = new ClsTaxes();
                getSet.setTax_id(cur.getInt(cur.getColumnIndex("TAX_ID")));
                getSet.setTax_type(cur.getString(cur.getColumnIndex("TAX_TYPE")));
                getSet.setTax_name(cur.getString(cur.getColumnIndex("TAX_NAME")));
                getSet.setTax_value(cur.getDouble(cur.getColumnIndex("TAX_VALUE")));
                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("ClsTaxes", ">>GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsTaxes> getListTaxes(String _where) {
        List<ClsTaxes> list = new ArrayList<>();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            Cursor cur = db.rawQuery("SELECT [TAX_ID],[TAX_TYPE],[TAX_NAME],[TAX_VALUE] FROM [Taxes] WHERE 1=1 ".concat(_where).concat(";"), null);

            Log.e("cur_Count","Count" +cur.getCount());

            while (cur.moveToNext()) {
                ClsTaxes getSet = new ClsTaxes();
                getSet.setTax_id(cur.getInt(cur.getColumnIndex("TAX_ID")));
                getSet.setTax_type(cur.getString(cur.getColumnIndex("TAX_TYPE")));
                getSet.setTax_name(cur.getString(cur.getColumnIndex("TAX_NAME")));
                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("ClsTaxes", ">>GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


}
