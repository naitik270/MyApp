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

public class ClsImages {

    int ImageId = 0;
    int DisplayOrder = 0;
    String FilePath = "";
    String FileName = "";
    String UniqueId = "";
    String Type = "";
    String FileType = "";
    String Extension = "";
    String Notes = "";

    static Context context;
    AppCompatActivity activity;
    private static SQLiteDatabase db;


    public int getImageId() {
        return ImageId;
    }

    public void setImageId(int imageId) {
        ImageId = imageId;
    }

    public int getDisplayOrder() {
        return DisplayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        DisplayOrder = displayOrder;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getUniqueId() {
        return UniqueId;
    }

    public void setUniqueId(String uniqueId) {
        UniqueId = uniqueId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getFileType() {
        return FileType;
    }

    public void setFileType(String fileType) {
        FileType = fileType;
    }

    public String getExtension() {
        return Extension;
    }

    public void setExtension(String extension) {
        Extension = extension;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }



/*

    public boolean insertToTable(String DESCRIPTION, String AMOUNT, String TRNS){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("imageID",o.gia);
        contentValues.put("5000",AMOUNT);
        contentValues.put("TRAN",TRNS);
        int  i  = (int) db.insert("table",null,contentValues);

        return true;
    }
*/


//nachiketdobariya

    @SuppressLint("WrongConstant")
    public static int Insert(ClsImages obj, Context context) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String qry = ("INSERT INTO [Images] ([DisplayOrder],[FilePath],[FileName]," +
                    "[UniqueId],[Type],[FileType],[Extension],[Notes],[entryDate]) VALUES (")

                    .concat(String.valueOf(obj.getDisplayOrder()))
                    .concat(",")

                    .concat("'")
                    .concat(obj.getFilePath().trim().replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(obj.getFileName())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(obj.getUniqueId())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(obj.getType())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(obj.getFileType())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(obj.getExtension())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(obj.getNotes())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ClsGlobal.getCurruntDateTime())
                    .concat("'")
                    .concat(");");

            SQLiteStatement statement = db.compileStatement(qry);
            result = (int) statement.executeInsert();
            Log.e("--INSERT--", "Image: " + qry);
            Log.e("--INSERT--", "result: " + result);


//            Cursor c = db.rawQuery("SELECT last_insert_rowid()", null);
//            c.moveToFirst();
//            int insertedID = c.getInt(0);//orderiD
//            Log.e("--INSERT--", "insertedID: " + insertedID);

        } catch (Exception e) {
            Log.e("--INSERT--", "Exception: " + e.getMessage());
            e.getMessage();
        } finally {
            db.close();
        }

        return result;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsImages> getImageByPaymentID(String where, SQLiteDatabase db, Context context) {
        List<ClsImages> list = new ArrayList<>();
        try {
            String qry = "SELECT * FROM [Images] where 1=1 ".concat(where);
            Cursor cur = db.rawQuery(qry, null);

            Log.d("--Image--", "getListCount: " + cur.getCount());
            Log.d("--Image--", "qry: " + qry);

            while (cur.moveToNext()) {
                ClsImages getSet = new ClsImages();
                getSet.setImageId(cur.getInt(cur.getColumnIndex("ImageId")));
                getSet.setDisplayOrder(cur.getInt(cur.getColumnIndex("DisplayOrder")));
                getSet.setFilePath(cur.getString(cur.getColumnIndex("FilePath")));
                getSet.setFileName(cur.getString(cur.getColumnIndex("FileName")));
                getSet.setUniqueId(cur.getString(cur.getColumnIndex("UniqueId")));
                getSet.setType(cur.getString(cur.getColumnIndex("Type")));
                getSet.setFileType(cur.getString(cur.getColumnIndex("FileType")));
                getSet.setExtension(cur.getString(cur.getColumnIndex("Extension")));
                getSet.setNotes(cur.getString(cur.getColumnIndex("Notes")));
                list.add(getSet);
            }
        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsImages> getImageByVendorPaymentID(String where, SQLiteDatabase db, Context context) {
        List<ClsImages> list = new ArrayList<>();
        try {
            String qry = "SELECT * FROM [Images] where 1=1 ".concat(where);
            Cursor cur = db.rawQuery(qry, null);

            Log.d("--Image--", "getListCount: " + cur.getCount());
            Log.d("--Image--", "qry: " + qry);

            while (cur.moveToNext()) {
                ClsImages getSet = new ClsImages();
                getSet.setImageId(cur.getInt(cur.getColumnIndex("ImageId")));
                getSet.setDisplayOrder(cur.getInt(cur.getColumnIndex("DisplayOrder")));
                getSet.setFilePath(cur.getString(cur.getColumnIndex("FilePath")));
                getSet.setFileName(cur.getString(cur.getColumnIndex("FileName")));
                getSet.setUniqueId(cur.getString(cur.getColumnIndex("UniqueId")));
                getSet.setType(cur.getString(cur.getColumnIndex("Type")));
                getSet.setFileType(cur.getString(cur.getColumnIndex("FileType")));
                getSet.setExtension(cur.getString(cur.getColumnIndex("Extension")));
                getSet.setNotes(cur.getString(cur.getColumnIndex("Notes")));
                list.add(getSet);
            }
        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsImages> getQueryByIdPaymentImageBlockList(String where, SQLiteDatabase db, Context context) {
        List<ClsImages> list = new ArrayList<>();
        try {

            String qry = "SELECT * FROM [Images] where 1=1 ".concat(where);

            Cursor cur = db.rawQuery(qry, null);

            while (cur.moveToNext()) {
                ClsImages getSet = new ClsImages();
                getSet.setImageId(cur.getInt(cur.getColumnIndex("ImageId")));
                getSet.setDisplayOrder(cur.getInt(cur.getColumnIndex("DisplayOrder")));
                getSet.setFilePath(cur.getString(cur.getColumnIndex("FilePath")));
                getSet.setFileName(cur.getString(cur.getColumnIndex("FileName")));
                getSet.setUniqueId(cur.getString(cur.getColumnIndex("UniqueId")));
                getSet.setType(cur.getString(cur.getColumnIndex("Type")));
                getSet.setFileType(cur.getString(cur.getColumnIndex("FileType")));
                getSet.setExtension(cur.getString(cur.getColumnIndex("Extension")));
                getSet.setNotes(cur.getString(cur.getColumnIndex("Notes")));
                list.add(getSet);
            }
        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static int DeleteByPaymentID(String where, SQLiteDatabase db, Context context) {
        int result = 0;
        try {

            String strSql = "DELETE FROM [Images] where 1=1 ".concat(where);

            Log.e("--ClsImg--", "Delete: " + strSql);

            SQLiteStatement statement = db.compileStatement(strSql);

            result = statement.executeUpdateDelete();
            Log.e("--ClsImg--", "result: " + result);
            db.close();
            return result;
        } catch (Exception e) {
            Log.e("Delete", "Delete---" + e.getMessage());
            e.getMessage();
        }
        return result;


    }


}
