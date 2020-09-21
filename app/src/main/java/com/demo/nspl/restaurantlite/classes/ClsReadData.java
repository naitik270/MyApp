package com.demo.nspl.restaurantlite.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.util.Log;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;

import java.util.Objects;
import java.util.Set;

public class ClsReadData {

    int excel_id = 0;
    String excel_name = "";
    String excel_number = "";
    String excel_email = "";
    String company_name = "";
    String gst_no = "";
    String address = "";
    String note = "";
    String status = "";
    String remark = "";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getExcel_email() {
        return excel_email;
    }

    public void setExcel_email(String excel_email) {
        this.excel_email = excel_email;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getGst_no() {
        return gst_no;
    }

    public void setGst_no(String gst_no) {
        this.gst_no = gst_no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getExcel_id() {
        return excel_id;
    }

    public void setExcel_id(int excel_id) {
        this.excel_id = excel_id;
    }

    public String getExcel_name() {
        return excel_name;
    }

    public void setExcel_name(String excel_name) {
        this.excel_name = excel_name;
    }

    public String getExcel_number() {
        return excel_number;
    }

    public void setExcel_number(String excel_number) {
        this.excel_number = excel_number;
    }

    static Context mContext;

    private static SQLiteDatabase db;

    public ClsReadData() {
    }

    public ClsReadData(Context context) {
        mContext = context;
    }


    @SuppressLint("WrongConstant")
    public static int Insert(Set<ClsReadData> lstClsGetValues) {
        int result = 0;
        boolean exist = false;


        try
        {

//            if (!db.isOpen()){
//
//                db = mContext.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
//            }

            db = mContext.openOrCreateDatabase(ClsGlobal.Database_Name, mContext.MODE_PRIVATE, null);

            for (ClsReadData objData : lstClsGetValues) {


                String sqlStr = "SELECT 1 FROM [CustomerMaster] WHERE 1=1 "
                        .concat(" AND [MOBILE_NO] = ".concat("'" +
                                objData.getExcel_number() + "'"))
                        .concat(";");
                Log.e("exists", "existsChqQry - " + sqlStr);
                Cursor cur = db.rawQuery(sqlStr, null);
                if (cur != null && cur.getCount() != 0) {
                    exist = true;
                }

                //ALREADY EXITS
                //UPDATE
                //ELSE
                //INSERT


                //objData.setSTATUS("SUCEESS");
                //lstClsGetValues.IndexOf(5,)

                if (!exist) {
                    String qry = ("INSERT INTO [CustomerMaster] ([NAME],[MOBILE_NO],[Email],[Company_Name]," +
                            "[GST_NO],[Address],[Note]) VALUES ('")
                            .concat(objData.getExcel_name().trim().replace("'", "''"))
                            .concat("'")
                            .concat(",")

                            .concat("'")
                            .concat(objData.getExcel_number().trim())
                            .concat("'")
                            .concat(",")

                            .concat("'")
                            .concat(objData.getCompany_name().trim())
                            .concat("'")
                            .concat(",")

                            .concat("'")
                            .concat(objData.getGst_no().trim())
                            .concat("'")
                            .concat(",")

                            .concat("'")
                            .concat(objData.getAddress().trim())
                            .concat("'")
                            .concat(",")

                            .concat("'")
                            .concat(objData.getAddress())
                            .concat("'")
                            .concat(");");

                    SQLiteStatement statement = db.compileStatement(qry);
                    result = statement.executeUpdateDelete();
                    Log.e("--uri--", "Insert: " + qry);

                }

            }


            db.close();
            return result;

        } catch (Exception e) {
            Log.e("--uri--", "Exception: " + e.getMessage());
            e.getMessage();
        }
        return result;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClsReadData that = (ClsReadData) o;
        return excel_number.equals(that.excel_number);
    }

    @Override
    public int hashCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.hash(excel_number);
        } else {
            return Objects.hashCode(excel_number);
        }
    }
}
