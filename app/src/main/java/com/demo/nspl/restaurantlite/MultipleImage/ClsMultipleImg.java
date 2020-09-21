package com.demo.nspl.restaurantlite.MultipleImage;

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

public class ClsMultipleImg {


    int ID = 0;

    int Purchase_id = 0;
    String Item_code = "";
    String Document_Name = "";
    String Document_No = "";
    String File_Path = "";
    String Type = "";

    String Entry_date = "";
    String File_Name = "";

    public String getEntry_date() {
        return Entry_date;
    }

    public void setEntry_date(String entry_date) {
        Entry_date = entry_date;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getPurchase_id() {
        return Purchase_id;
    }

    public void setPurchase_id(int purchase_id) {
        Purchase_id = purchase_id;
    }

    public String getItem_code() {
        return Item_code;
    }

    public void setItem_code(String item_code) {
        Item_code = item_code;
    }

    public String getDocument_Name() {
        return Document_Name;
    }

    public void setDocument_Name(String document_Name) {
        Document_Name = document_Name;
    }

    public String getDocument_No() {
        return Document_No;
    }

    public void setDocument_No(String document_No) {
        Document_No = document_No;
    }

    public String getFile_Path() {
        return File_Path;
    }

    public void setFile_Path(String file_Path) {
        File_Path = file_Path;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getFile_Name() {
        return File_Name;
    }

    public void setFile_Name(String file_Name) {
        File_Name = file_Name;
    }


    static Context context;
    AppCompatActivity activity;
    private static SQLiteDatabase db;

    public ClsMultipleImg() {

    }

    public ClsMultipleImg(Context ctx) {

        context = ctx;
    }


    @SuppressLint("WrongConstant")
    public static int Insert(ClsMultipleImg objClsMultipleImg) {
        int result = 0;

        try
        {

//
//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            }

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);


            String qry = ("INSERT INTO [MultipleImgSave] ([Purchase_id],[Item_code],[Document_Name],[Document_No],[File_Path]," +
                    "[Type],[File_Name],[Entry_date]) VALUES (")

                    .concat(String.valueOf(objClsMultipleImg.getPurchase_id()))
                    .concat(",")

                    .concat("'")
                    .concat(objClsMultipleImg.getItem_code().trim())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objClsMultipleImg.getDocument_Name().trim())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objClsMultipleImg.getDocument_No().trim())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objClsMultipleImg.getFile_Path().trim())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objClsMultipleImg.getType().trim())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(objClsMultipleImg.getFile_Name().trim().replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ClsGlobal.getEntryDateTime())
                    .concat("'")
                    .concat(");");

            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.e("--Dialog--", "INSERT: " + qry);


            Cursor c = db.rawQuery("SELECT last_insert_rowid()", null);
            c.moveToFirst();
            int insertedID = c.getInt(0);//orderiD
            Log.e("--Invoice--", "insertedID: " + insertedID);

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("--Invoice--", "Exception: " + e.getMessage());
            e.getMessage();
        }
        return result;

    }

    @SuppressLint("WrongConstant")
    public static int Delete(int _id,Context context) {
        int result = 0;
        try {

//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            }


            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String strSql = "DELETE FROM [MultipleImgSave] WHERE [ID] = "
                    .concat(String.valueOf(_id))
                    .concat(";");

            Log.e("--ClsMultipleImg--", "Delete: " + strSql);

            SQLiteStatement statement = db.compileStatement(strSql);

            result = statement.executeUpdateDelete();


            db.close();
            return result;
        } catch (Exception e) {
            Log.e("Delete", "Delete---" + e.getMessage());
            e.getMessage();
        }
        return result;


    }
    @SuppressLint("WrongConstant")
    public static int DeleteByPurchaseID(int _Purchase_id) {
        int result = 0;
        try {

//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            }
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String strSql = "DELETE FROM [MultipleImgSave] WHERE [Purchase_id] = "
                    .concat(String.valueOf(_Purchase_id))
                    .concat(";");

            Log.e("--ClsMultipleImg--", "Delete: " + strSql);

            SQLiteStatement statement = db.compileStatement(strSql);

            result = statement.executeUpdateDelete();


            db.close();
            return result;
        } catch (Exception e) {
            Log.e("Delete", "Delete---" + e.getMessage());
            e.getMessage();
        }
        return result;


    }


    @SuppressLint("WrongConstant")
    public static List<ClsMultipleImg> getImageByItemCode(String _itemCode, Context context) {
        List<ClsMultipleImg> list = new ArrayList<>();

        try {

//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            }
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String qry = "Select * from [MultipleImgSave] WHERE [Item_code] ="
                    .concat("'").concat(_itemCode).concat("'");


            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));
            Log.e("qry", qry);
            while (cur.moveToNext()) {

                ClsMultipleImg getSet = new ClsMultipleImg();
                getSet.setID(cur.getInt(cur.getColumnIndex("ID")));
                getSet.setPurchase_id(cur.getInt(cur.getColumnIndex("Purchase_id")));
                getSet.setItem_code(cur.getString(cur.getColumnIndex("Item_code")));
                getSet.setDocument_Name(cur.getString(cur.getColumnIndex("Document_Name")));
                getSet.setDocument_No(cur.getString(cur.getColumnIndex("Document_No")));
                getSet.setFile_Path(cur.getString(cur.getColumnIndex("File_Path")));
                getSet.setType(cur.getString(cur.getColumnIndex("Type")));
                getSet.setFile_Name(cur.getString(cur.getColumnIndex("File_Name")));
                getSet.setEntry_date(cur.getString(cur.getColumnIndex("Entry_date")));

                list.add(getSet);
            }


        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }

        return list;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsMultipleImg> getImageByPurchaseID(int _purchaseID, Context context) {
        List<ClsMultipleImg> list = new ArrayList<>();

        try {

//            if (!db.isOpen()){
//
//                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            }


            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String qry = "Select * from [MultipleImgSave] WHERE [Purchase_id] ="
                    .concat(String.valueOf(_purchaseID));


            Cursor cur = db.rawQuery(qry, null);


            Log.d("--Invoice--", "getListCount: " + cur.getCount());
            Log.d("--Invoice--", "qry: " + qry);



            while (cur.moveToNext()) {

                ClsMultipleImg getSet = new ClsMultipleImg();
                getSet.setID(cur.getInt(cur.getColumnIndex("ID")));
                getSet.setPurchase_id(cur.getInt(cur.getColumnIndex("Purchase_id")));
                getSet.setItem_code(cur.getString(cur.getColumnIndex("Item_code")));
                getSet.setDocument_Name(cur.getString(cur.getColumnIndex("Document_Name")));
                getSet.setDocument_No(cur.getString(cur.getColumnIndex("Document_No")));
                getSet.setFile_Path(cur.getString(cur.getColumnIndex("File_Path")));
                getSet.setType(cur.getString(cur.getColumnIndex("Type")));
                getSet.setFile_Name(cur.getString(cur.getColumnIndex("File_Name")));
                getSet.setEntry_date(cur.getString(cur.getColumnIndex("Entry_date")));

                list.add(getSet);
            }


        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }

        return list;
    }


//    @SuppressLint("WrongConstant")
//    public boolean checkExists(String _where, Context context ,String mode) {
//        boolean result = false;
//
//        try {
//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
//            String sqlStr = "SELECT * FROM [MultipleImgSave] WHERE 1=1 "
//                    .concat(_where)
//                    .concat(";");
//            Log.e("exists", "existsChqQry - " + sqlStr);
//            Cursor cur = db.rawQuery(sqlStr, null);
////            if (cur != null && cur.getCount() != 0) {
////                result = true;
////            }
//
//            while (cur.moveToNext()) {
//                ClsMultipleImg getSet = new ClsMultipleImg();
//                getSet.setID(cur.getInt(cur.getColumnIndex("ID")));
//                getSet.setPurchase_id(cur.getInt(cur.getColumnIndex("Purchase_id")));
//                getSet.setItem_code(cur.getString(cur.getColumnIndex("Item_code")));
//                getSet.setFile_Path(cur.getString(cur.getColumnIndex("File_Path")));
//                getSet.setFile_Name(cur.getString(cur.getColumnIndex("File_Name")));
//
//
//
//
//            }
//
//            db.close();
//        } catch (Exception e) {
//            Log.e("ClsExpenseType", "ClsExpenseType>>>>CheckExist" + e.getMessageSales());
//            e.getMessageSales();
//        }
//        return result;
//    }

}
