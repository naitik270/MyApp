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

public class ClsExportSingleCustomerData {

    String _mode = "";
    String _date = "";
    String _details = "";
    String _paymentMode = "";
    String _paymentDetails = "";
    double _amount = 0.0;
    double _balance = 0.0;

    public String get_mode() {
        return _mode;
    }

    public void set_mode(String _mode) {
        this._mode = _mode;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String get_details() {
        return _details;
    }

    public void set_details(String _details) {
        this._details = _details;
    }

    public String get_paymentMode() {
        return _paymentMode;
    }

    public void set_paymentMode(String _paymentMode) {
        this._paymentMode = _paymentMode;
    }

    public String get_paymentDetails() {
        return _paymentDetails;
    }

    public void set_paymentDetails(String _paymentDetails) {
        this._paymentDetails = _paymentDetails;
    }

    public double get_amount() {
        return _amount;
    }

    public void set_amount(double _amount) {
        this._amount = _amount;
    }

    public double get_balance() {
        return _balance;
    }

    public void set_balance(double _balance) {
        this._balance = _balance;
    }

    static Context context;
    AppCompatActivity activity;
    private static SQLiteDatabase db;

    @SuppressLint("WrongConstant")
    public static List<ClsExportSingleCustomerData> getList(Context context, String _mobileNo) {

        List<ClsExportSingleCustomerData> list = new ArrayList<>();


        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            Log.e("--Single--", "------------------------------------");
            Log.e("--Single--", "Step:1");

            String qry = ClsGlobal.exportCustomerPayment(context);
            qry = qry.replace("#_mobile", _mobileNo);

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--Single--", "Qry: " + qry);
            Log.e("--Single--", "curCount:" + cur.getCount());

            while (cur.moveToNext()) {
                ClsExportSingleCustomerData getSet = new ClsExportSingleCustomerData();
                getSet.set_paymentDetails(cur.getString(cur.getColumnIndex("PaymentDetail")));
                getSet.set_details(cur.getString(cur.getColumnIndex("Details")));
                getSet.set_paymentMode(cur.getString(cur.getColumnIndex("PaymentMode")));
                getSet.set_date(cur.getString(cur.getColumnIndex("BillDate")));
                getSet.set_amount(cur.getDouble(cur.getColumnIndex("Amount")));
                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

        return list;
    }


}
