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

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Desktop on 3/24/2018.
 */

public class ClsEmployeeDocuments {
    int doc_id, employee_id;
    String doc_name;
    String other_proof;
    String doc_no;
    String filepath;
    String filename;
    static Context context;
    AppCompatActivity activity;
    private static SQLiteDatabase db;

    public ClsEmployeeDocuments() {

    }

    public ClsEmployeeDocuments(Context ctx) {

        context = ctx;
    }

    public int getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(int doc_id) {
        this.doc_id = doc_id;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public String getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String doc_name) {
        this.doc_name = doc_name;
    }

    public String getOther_proof() {
        return other_proof;
    }

    public void setOther_proof(String other_proof) {
        this.other_proof = other_proof;
    }

    public String getDoc_no() {
        return doc_no;
    }

    public void setDoc_no(String doc_no) {
        this.doc_no = doc_no;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getEXP_DATE() {
        return EXP_DATE;
    }

    public void setEXP_DATE(String EXP_DATE) {
        this.EXP_DATE = EXP_DATE;
    }

    String EXP_DATE = "";

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    String TYPE = "";


    @SuppressLint("WrongConstant")
    public static List<String> getItemMasterColumnEmp(Context context,io.requery.android.database.sqlite
            .SQLiteDatabase db) {
        List<String> columns = new ArrayList<>();
        try {
//            io.requery.android.database.sqlite.SQLiteDatabase db;
//
//            db = io.requery.android.database.sqlite.SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath(ClsGlobal.Database_Name).getPath(),
//                    null);

            String qry = "SELECT * FROM [EmployeeDocument] LIMIT 1 ";
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
    public static int Insert(ClsEmployeeDocuments objempDocuments) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String qry = "INSERT INTO [EmployeeDocument] ([EMP_ID],[DOCUMENT_NAME],[OTHER_PROF],[DOCUMENT_NO],[EXP_DATE],[TYPE],[FILE_NAME]) VALUES ("

                    .concat(String.valueOf(objempDocuments.getEmployee_id()))
                    .concat(",")

                    .concat("'")
                    .concat(objempDocuments.getDoc_name().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objempDocuments.getOther_proof().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objempDocuments.getDoc_no().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objempDocuments.getEXP_DATE())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objempDocuments.getTYPE())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objempDocuments.getFilename().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(");");

            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.e("INSERT", "Document--->>" + qry);

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsEmployeeDocuments", "Insert>>>>>>>>" + e.getMessage());
            e.getMessage();
        }
        return result;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsEmployeeDocuments> getList(String _where) {
        List<ClsEmployeeDocuments> list = new ArrayList<>();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT "
                    .concat("[EMP_ID]")
                    .concat(",[DOCUMENT_NAME]")
                    .concat(",[DOCUMENT_ID]")
                    .concat(",[OTHER_PROF]")
                    .concat(",[DOCUMENT_NO]")
                    .concat(",[EXP_DATE]")
                    .concat(",IFNULL([TYPE],'employee') AS [TYPE]")
//                    .concat(",[TYPE]")
                    .concat(",[FILE_NAME]")
                    .concat(" FROM [EmployeeDocument] WHERE 1=1 ")
                    .concat(_where);


            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));
            Log.e("qry", qry);


            while (cur.moveToNext()) {
                ClsEmployeeDocuments getSet = new ClsEmployeeDocuments();
                getSet.setEmployee_id(cur.getInt(cur.getColumnIndex("EMP_ID")));
                getSet.setDoc_id(cur.getInt(cur.getColumnIndex("DOCUMENT_ID")));
                getSet.setDoc_name(cur.getString(cur.getColumnIndex("DOCUMENT_NAME")));
                getSet.setOther_proof(cur.getString(cur.getColumnIndex("OTHER_PROF")));
                getSet.setDoc_no(cur.getString(cur.getColumnIndex("DOCUMENT_NO")));
                getSet.setEXP_DATE(cur.getString(cur.getColumnIndex("EXP_DATE")));
                getSet.setFilename(cur.getString(cur.getColumnIndex("FILE_NAME")));
                getSet.setTYPE(cur.getString(cur.getColumnIndex("TYPE")));
                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("ClsEmployeeDocuments", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static int Delete(ClsEmployeeDocuments Obj) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "DELETE FROM [EmployeeDocument] WHERE [DOCUMENT_ID] = "
                    .concat(String.valueOf(Obj.getDoc_id()))
                    .concat(";");
            Log.e("DELETE", "DELqRY-" + strSql);
            SQLiteStatement statement = db.compileStatement(strSql);

            result = statement.executeUpdateDelete();

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsCategory", "Delete" + e.getMessage());
            e.getMessage();
        }
        return result;


    }
}
