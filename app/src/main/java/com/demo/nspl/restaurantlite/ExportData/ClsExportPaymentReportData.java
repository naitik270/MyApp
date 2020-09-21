package com.demo.nspl.restaurantlite.ExportData;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;

import java.util.ArrayList;
import java.util.List;

public class ClsExportPaymentReportData {

    String _date = "";
    String _type = "";
    String _custName = "";
    String _vendorName = "";


    String _mobileNo = "";
    String _invoiceNo = "";
    String _detail = "";
    String _mode = "";

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    double Amount = 0.0;


    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }


    public String get_custName() {
        return _custName;
    }

    public void set_custName(String _custName) {
        this._custName = _custName;
    }

    public String get_vendorName() {
        return _vendorName;
    }

    public void set_vendorName(String _vendorName) {
        this._vendorName = _vendorName;
    }

    public String get_mobileNo() {
        return _mobileNo;
    }

    public void set_mobileNo(String _mobileNo) {
        this._mobileNo = _mobileNo;
    }

    public String get_invoiceNo() {
        return _invoiceNo;
    }

    public void set_invoiceNo(String _invoiceNo) {
        this._invoiceNo = _invoiceNo;
    }

    public String get_detail() {
        return _detail;
    }

    public void set_detail(String _detail) {
        this._detail = _detail;
    }

    public String get_mode() {
        return _mode;
    }

    public void set_mode(String _mode) {
        this._mode = _mode;
    }

    static Context context;
    AppCompatActivity activity;
    private static SQLiteDatabase db;

    @SuppressLint("WrongConstant")
    public static List<ClsExportPaymentReportData> getPaymentList(Context context, String _where) {

        List<ClsExportPaymentReportData> list = new ArrayList<>();


        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            Log.e("--Single--", "------------------------------------");
            Log.e("--Single--", "Step:1");

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
                    .concat(" FROM [PaymentMaster] WHERE 1=1 ")
                    .concat(_where)
                    .concat(" ORDER BY [PaymentDate]");


            Cursor cur = db.rawQuery(strSql, null);

            Log.e("--Payment--", "Qry: " + strSql);
            Log.e("--Payment--", "curCount:" + cur.getCount());

            while (cur.moveToNext()) {
                ClsExportPaymentReportData getSet = new ClsExportPaymentReportData();
                getSet.set_date(cur.getString(cur.getColumnIndex("PaymentDate")));
                getSet.set_mobileNo(cur.getString(cur.getColumnIndex("MobileNo")));
                getSet.set_invoiceNo(cur.getString(cur.getColumnIndex("InvoiceNo")));
                getSet.set_custName(cur.getString(cur.getColumnIndex("CustomerName")));
                getSet.set_vendorName(cur.getString(cur.getColumnIndex("VendorName")));
                getSet.set_detail(cur.getString(cur.getColumnIndex("PaymentDetail")));
                getSet.set_mode(cur.getString(cur.getColumnIndex("PaymentMode")));
                getSet.set_type(cur.getString(cur.getColumnIndex("Type")));
                getSet.setAmount(cur.getDouble(cur.getColumnIndex("Amount")));
                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

        return list;
    }


}
