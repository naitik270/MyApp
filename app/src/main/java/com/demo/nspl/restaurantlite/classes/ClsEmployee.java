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
 * Created by Desktop on 3/14/2018.
 */

public class ClsEmployee {

    int employee_id;
    String employee_name;
    String contact_no;
    String address;
    Double salary;

    public Integer getSort_no() {
        return sort_no;
    }

    public void setSort_no(Integer sort_no) {
        this.sort_no = sort_no;
    }

    Integer sort_no;
    String remark;
    String active;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String image;
    String dob;

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }



    static Context context;
    AppCompatActivity activity;
    private static SQLiteDatabase db;


    public ClsEmployee() {

    }

    public ClsEmployee(Context ctx) {

        context = ctx;
    }


    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }



    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }


    @SuppressLint("WrongConstant")
    public static int Insert(ClsEmployee ObjEmployee) {
        int  result = 0;
        try {

            db =context .openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = ("INSERT INTO [EMPLOYEE_MASTER] ([EMPLOYEE_NAME]," +
                    "[CONTACT_NO],[EMPLOYEE_IMAGE],[DOB],[ADDRESS],[SALARY]," +
                    "[ACTIVE],[REMARK],[SORT_NO]) VALUES ('")

                    .concat(ObjEmployee.getEmployee_name().trim()
                            .replace("'","''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjEmployee.getContact_no())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat("")
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjEmployee.getDob())
                    .concat("'")

                    .concat(",")

                    .concat("'")
                    .concat(ObjEmployee.getAddress().trim()
                            .replace("'","''"))
                    .concat("'")
                    .concat(",")


                    .concat(String.valueOf(ObjEmployee.getSalary()))
                    .concat(",")

                    .concat("'")
                    .concat(ObjEmployee.getActive())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjEmployee.getRemark().trim()
                            .replace("'","''"))
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(ObjEmployee.getSort_no()))

                    .concat(");");

            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.e("INSERT","ClsEmployee--->>"+ qry);

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("INSERT", "Insert>>>>>>>>" + e.getMessage());
            e.getMessage();
        }
        return result;
    }

//    @SuppressLint("WrongConstant")
//    public static List<ClsEmployee> getList() {
//        List<ClsEmployee> list = new ArrayList<>();
//
//        try {
//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
//            Cursor cur = db.rawQuery("SELECT [EMPLOYEE_ID],[EMPLOYEE_NAME],[CONTACT_NO],[DOB],[EMPLOYEE_IMAGE],[ADDRESS],[SALARY],[ACTIVE],[REMARK],[SORT_NO] FROM [EMPLOYEE_MASTER];", null);
//
//            while (cur.moveToNext()) {
//                ClsEmployee getSet = new ClsEmployee();
//                getSet.setEmployee_id(cur.getInt(cur.getColumnIndex("EMPLOYEE_ID")));
//                getSet.setEmployee_name(cur.getString(cur.getColumnIndex("EMPLOYEE_NAME")));
//                getSet.setContact_no(cur.getString(cur.getColumnIndex("CONTACT_NO")));
//                getSet.setImage(cur.getString(cur.getColumnIndex("EMPLOYEE_IMAGE")));
//                getSet.setDob(cur.getString(cur.getColumnIndex("DOB")));
//                getSet.setAddress(cur.getString(cur.getColumnIndex("ADDRESS")));
//                getSet.setSalary(cur.getDouble(cur.getColumnIndex("SALARY")));
//                getSet.setSort_no(cur.getInt(cur.getColumnIndex("SORT_NO")));
//                getSet.setActive(cur.getString(cur.getColumnIndex("ACTIVE")));
//                getSet.setRemark(cur.getString(cur.getColumnIndex("REMARK")));
//                list.add(getSet);
//            }
//
//        } catch (Exception e) {
//            Log.e("c", "GetList" + e.getMessageSales());
//            e.getMessageSales();
//        }
//        return list;
//    }

    @SuppressLint("WrongConstant")
    public static List<ClsEmployee> getList(String where) {
        List<ClsEmployee> list = new ArrayList<>();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT [EMPLOYEE_ID],[EMPLOYEE_NAME],[CONTACT_NO],[DOB],[EMPLOYEE_IMAGE]," +
                    "[ADDRESS],[SALARY]," +
                    "[ACTIVE],[REMARK],[SORT_NO] FROM [EMPLOYEE_MASTER] WHERE 1=1 ".concat(where);

            Log.e("qry",qry);


            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));
            while (cur.moveToNext()) {
                ClsEmployee getSet = new ClsEmployee();
                getSet.setEmployee_id(cur.getInt(cur.getColumnIndex("EMPLOYEE_ID")));
                getSet.setEmployee_name(cur.getString(cur.getColumnIndex("EMPLOYEE_NAME")));
                getSet.setContact_no(cur.getString(cur.getColumnIndex("CONTACT_NO")));
                getSet.setImage(cur.getString(cur.getColumnIndex("EMPLOYEE_IMAGE")));
                getSet.setDob(cur.getString(cur.getColumnIndex("DOB")));
                getSet.setAddress(cur.getString(cur.getColumnIndex("ADDRESS")));
                getSet.setSalary(cur.getDouble(cur.getColumnIndex("SALARY")));
                getSet.setSort_no(cur.getInt(cur.getColumnIndex("SORT_NO")));
                getSet.setActive(cur.getString(cur.getColumnIndex("ACTIVE")));
                getSet.setRemark(cur.getString(cur.getColumnIndex("REMARK")));
                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public boolean checkExists(String _where) {
        boolean result = false;

        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = "SELECT 1 FROM [EMPLOYEE_MASTER] WHERE 1=1 "
                    .concat(_where)
                    .concat(";");
            Log.e("exists", "existsChqQry - " + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            if (cur != null && cur.getCount() != 0) {
                result = true;
            }
            db.close();

        } catch (Exception e) {
            Log.e("ClsEmployee", "ClsEmployee>>>>CheckExist" + e.getMessage());
            e.getMessage();
        }
        return result;
    }

    @SuppressLint("WrongConstant")
    public static int Delete(ClsEmployee ObjEmployee, Context context) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "DELETE FROM [EMPLOYEE_MASTER] WHERE [EMPLOYEE_ID] = "
                    .concat( String.valueOf(ObjEmployee.getEmployee_id()))
                    .concat(";");
            Log.e("DELETE", "DELqRY-" + strSql);
            SQLiteStatement statement = db.compileStatement(strSql);

            result = statement.executeUpdateDelete();

            db.close();
            return result;
        } catch (Exception e) {
            Log.e("ClsEmployee", "Delete" + e.getMessage());
            e.getMessage();
        }
        return result;


    }

    @SuppressLint("WrongConstant")
    public ClsEmployee getObject(ClsEmployee ObjEmployee) {
        ClsEmployee Obj = new ClsEmployee();

        try {


            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = "SELECT [EMPLOYEE_ID],[EMPLOYEE_NAME],[CONTACT_NO],[DOB],[EMPLOYEE_IMAGE],[ADDRESS],[SALARY],[ACTIVE],[REMARK],[SORT_NO] FROM [EMPLOYEE_MASTER] WHERE 1=1 AND [EMPLOYEE_ID] = "
                    .concat(String.valueOf(ObjEmployee.getEmployee_id()))

                    .concat(";");
            Log.e("Update", ">>>>>>>>>" + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            Log.e("Update", ">>>>>>>>>" + cur.getCount());
            while (cur.moveToNext()) {

                Obj.setEmployee_id(cur.getInt(cur.getColumnIndex("EMPLOYEE_ID")));
                Obj.setEmployee_name(cur.getString(cur.getColumnIndex("EMPLOYEE_NAME")));
                Obj.setContact_no(cur.getString(cur.getColumnIndex("CONTACT_NO")));
                Obj.setImage(cur.getString(cur.getColumnIndex("EMPLOYEE_IMAGE")));
                Obj.setDob(cur.getString(cur.getColumnIndex("DOB")));
                Obj.setAddress(cur.getString(cur.getColumnIndex("ADDRESS")));
                Obj.setSalary(cur.getDouble(cur.getColumnIndex("SALARY")));
                Obj.setSort_no(cur.getInt(cur.getColumnIndex("SORT_NO")));
                Obj.setActive(cur.getString(cur.getColumnIndex("ACTIVE")));
                Obj.setRemark(cur.getString(cur.getColumnIndex("REMARK")));

            }

            db.close();

        } catch (Exception e) {
            Log.e("ClsEmployee", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }
        return Obj;
    }


    @SuppressLint("WrongConstant")
    public static int Update(ClsEmployee objEmployee) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "UPDATE [EMPLOYEE_MASTER] SET "
                    .concat("[EMPLOYEE_NAME] = ")
                    .concat("'")
                    .concat(objEmployee.getEmployee_name().trim()
                            .replace("'","''"))
                    .concat("'")

                    .concat(" ,[CONTACT_NO] = ")
                    .concat("'")
                    .concat(objEmployee.getContact_no())
                    .concat("'")

                    .concat(" ,[DOB] = ")
                    .concat("'")
                    .concat(objEmployee.getDob())
                    .concat("'")

                    .concat(" ,[EMPLOYEE_IMAGE] = ")
                    .concat("'")
                    .concat("")
                    .concat("'")

                    .concat(" ,[ADDRESS] = ")
                    .concat("'")
                    .concat(objEmployee.getAddress().trim()
                            .replace("'","''"))
                    .concat("'")

                    .concat(" ,[SALARY] = ")
                    .concat(String.valueOf(objEmployee.getSalary()))


                    .concat(" ,[SORT_NO] = ")
                    .concat(String.valueOf(objEmployee.getSort_no()))


                    .concat(" ,[ACTIVE] = ")
                    .concat("'")
                    .concat(objEmployee.getActive())
                    .concat("'")



                    .concat(" ,[REMARK] = ")
                    .concat("'")
                    .concat(objEmployee.getRemark().trim()
                            .replace("'","''"))
                    .concat("'")



                    .concat(" WHERE [EMPLOYEE_ID] = ")

                    .concat(String.valueOf(objEmployee.getEmployee_id()))

                    .concat(";");
            Log.e("Update", strSql);
            SQLiteStatement statement = db.compileStatement(strSql);
            result = statement.executeUpdateDelete();
            //db.execSQL(strSql);
            db.close();

        }catch (Exception e)
        {
            Log.e("Update", "Update......" + e.getMessage());
        }
        return result;
    }

}
