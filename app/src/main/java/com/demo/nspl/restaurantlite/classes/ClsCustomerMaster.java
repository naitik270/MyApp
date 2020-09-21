package com.demo.nspl.restaurantlite.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ClsCustomerMaster {


    private String mName = "", mMobile_No = "",
            Company_Name = "", GST_NO = "", Address = "",
            Email = "", Note = "", Save_Contact = "", DOB = "", PanCard = "", AnniversaryDate = "";
    private int mId = 0;
    private Double Credit = 0.0;
    static Context mContext;

    private static SQLiteDatabase db;

    public ClsCustomerMaster() {

        this.mName = "";
        this.mMobile_No = "";
        this.Company_Name = "";
        this.GST_NO = "";
        this.Address = "";
        this.Email = "";
        this.Note = "";
        this.Save_Contact = "";
        this.mId = 0;
        this.Credit = 0.0;
        this.OpeningStock = 0.0;
        this.BalanceType = "";
        this.selected = false;
        this.status = "";
        this.remark = "";
    }
//    Boolean isSelected = false;
//
//
//    public Boolean getSelected() {
//        return isSelected;
//    }
//
//    public void setSelected(Boolean selected) {
//        isSelected = selected;
//    }


    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getPanCard() {
        return PanCard;
    }

    public void setPanCard(String panCard) {
        PanCard = panCard;
    }

    public String getAnniversaryDate() {
        return AnniversaryDate;
    }

    public void setAnniversaryDate(String anniversaryDate) {
        AnniversaryDate = anniversaryDate;
    }

    Double OpeningStock = 0.0;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getSave_Contact() {
        return Save_Contact;
    }

    public void setSave_Contact(String save_Contact) {
        Save_Contact = save_Contact;
    }

    public Double getOpeningStock() {
        return OpeningStock;
    }

    public void setOpeningStock(Double openingStock) {
        OpeningStock = openingStock;
    }


    public ClsCustomerMaster(Context context) {
        mContext = context;
    }


    public Double getCredit() {
        return Credit;
    }

    public void setCredit(Double credit) {
        Credit = credit;
    }

    public String getCompany_Name() {
        return Company_Name;
    }

    public void setCompany_Name(String company_Name) {
        Company_Name = company_Name;
    }

    public String getGST_NO() {
        return GST_NO;
    }

    public void setGST_NO(String GST_NO) {
        this.GST_NO = GST_NO;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmMobile_No() {
        return mMobile_No;
    }

    public void setmMobile_No(String mMobile_No) {
        this.mMobile_No = mMobile_No;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }


    String BalanceType = "";


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    boolean selected;


    public String getBalanceType() {
        return BalanceType;
    }

    public void setBalanceType(String balanceType) {
        BalanceType = balanceType;
    }


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


    @SuppressLint("WrongConstant")
    public static int INSERT(ClsCustomerMaster ObjCustomerMaster, Context context) {
        int result = 0;
        boolean exist = false;
        try {
       /*     if (!db.isOpen()){

                db = context.openOrCreateDatabase(ClsGlobal.Database_Name, mContext.MODE_APPEND, null);
            }*/

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String sqlStr = "SELECT 1 FROM [CustomerMaster] WHERE 1=1 "
                    .concat(" AND [MOBILE_NO] = ".concat("'" +
                            ObjCustomerMaster.getmMobile_No() + "'"))
                    .concat(";");
            Log.e("exists", "existsChqQry - " + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            if (cur != null && cur.getCount() != 0) {
                exist = true;
            }

            if (!exist) {
                String qry = ("INSERT INTO [CustomerMaster] ([NAME],[MOBILE_NO]," +
                        "[Company_Name],[GST_NO],[BalanceType],[Address]) VALUES ('")
                        .concat(ObjCustomerMaster.getmName().trim()
                                .replace("'", "''"))
                        .concat("'")
                        .concat(",")

                        .concat("'")
                        .concat(ObjCustomerMaster.getmMobile_No())
                        .concat("'")
                        .concat(",")

                        .concat("'")
                        .concat(ObjCustomerMaster.getCompany_Name().trim().replace("'", "''"))
                        .concat("'")
                        .concat(",")

                        .concat("'")
                        .concat(ObjCustomerMaster.getGST_NO())
                        .concat("'")
                        .concat(",")

                        .concat("'")
                        .concat(ObjCustomerMaster.getBalanceType())
                        .concat("'")
                        .concat(",")

                        .concat("'")
                        .concat(ObjCustomerMaster.getAddress().trim().replace("'", "''"))
                        .concat("'")

                        .concat(");");

                Log.e("--Insert--", "qry: " + qry);
                SQLiteStatement sqLiteStatement = db.compileStatement(qry);
                result = sqLiteStatement.executeUpdateDelete();
                Log.e("QRERY", qry);
            }


            db.close();

            return result;

        } catch (Exception e) {
            Log.e("ClsTabMaster", "Insert----" + e.getMessage());
        }

        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("WrongConstant")
    public static List<ClsCustomerMaster> InsertExcelRecord(Context context, List<ClsCustomerMaster> lstClsGetValues) {
        //List<ClsCustomerMaster> lstClsCustomerMasters = new ArrayList<>();

        db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_PRIVATE, null);

        try {
            int sr = 0;

            //check exists and update status to list here
            List<String> mobileNos = new ArrayList<>();
            mobileNos = lstClsGetValues
                    .stream()
                    .filter(q -> !q.getStatus().equalsIgnoreCase("INVALID MOBILE NO") &&
                            !q.getStatus().equalsIgnoreCase("MOBILE NO IS REQUIRED"))
                    .map(ClsCustomerMaster::getmMobile_No)//get only mobile no from list!!!
                    .collect(Collectors.toList());

            Log.e("--Main--", "Step:1");

            Gson gson = new Gson();
            String jsonInString = gson.toJson(mobileNos);
            Log.e("--Main--", "mobileNos: " + jsonInString);

//            boolean exist = false;

            if (mobileNos.size() != 0) {

                Log.e("--Main--", "Step:2");


                String sqlStr = "SELECT [MOBILE_NO] FROM [CustomerMaster] WHERE 1=1 "
                        //  .concat(" AND [MOBILE_NO] IN ('9853500003')");
                        .concat(" AND [MOBILE_NO] IN ('".concat(TextUtils.join("','", mobileNos))).concat("')");

                Log.e("--Main--", "existsChqQry - " + sqlStr);
                Cursor cur = db.rawQuery(sqlStr, null);
                Log.e("--Main--", "cur.getCount() - " + cur.getCount());

                if (cur.getCount() != 0) {
                    mobileNos.clear();
                    while (cur.moveToNext()) {
                        mobileNos.add(cur.getString(cur.getColumnIndex("MOBILE_NO")));
                    }

                    List<String> finalMobileNos = mobileNos;

                    lstClsGetValues = lstClsGetValues
                            .stream()
                            .filter(q -> {
                                if (finalMobileNos.contains(q.getmMobile_No())) {
                                    q.setStatus("EXISTS");
                                    return true;
                                } else {
                                    return false;
                                }
                            })
                            .collect(Collectors.toList());


                } else {


                    Set<String> nameSet = new HashSet<>();


                    Log.e("--Main--", "Step:10");

                    lstClsGetValues = lstClsGetValues.stream()
                            .filter(e -> nameSet.add(e.getmMobile_No()))
                            .collect(Collectors.toList());

                    Log.e("--Main--", "Step:11");


                    Gson gson2 = new Gson();
                    String jsonInString2 = gson2.toJson(lstClsGetValues);
                    Log.e("--Main--", "lstClsGetValues else: " + jsonInString2);

                }
            }
            //Log.e("--flterLIST--", "flterLIST: " + _dummy.size());

            for (ClsCustomerMaster objData : lstClsGetValues) {
                sr++;


                if (objData.getStatus() != null && !objData.getStatus().equalsIgnoreCase("INVALID MOBILE NO") &&
                        !objData.getStatus().equalsIgnoreCase("EXISTS") &&
                        !objData.getStatus().equalsIgnoreCase("MOBILE NO IS REQUIRED")) {


                    //ALREADY EXITS
                    //UPDATE
                    //ELSE
                    //INSERT

                    //objData.setSTATUS("SUCEESS");
                    //lstClsGetValues.IndexOf(5,)
                    //if (!exist) {

                    try {
                        String qry = ("INSERT INTO [CustomerMaster] (" +
                                "[NAME],[MOBILE_NO],[Email],[Company_Name]," +
                                "[GST_NO],[Address],[Note]) VALUES ('")

                                .concat(objData.getmName() != null
                                        ? objData.getmName().replace("'", "''") : "")
                                .concat("'")
                                .concat(",")

                                .concat("'")
                                .concat(objData.getmMobile_No() != null ? objData.getmMobile_No() : "")
                                .concat("'")
                                .concat(",")

                                .concat("'")
                                .concat(objData.getEmail() != null ? objData.getEmail() : "")
                                .concat("'")
                                .concat(",")

                                .concat("'")
                                .concat(objData.getCompany_Name() != null
                                        ? objData.getCompany_Name() : "")
                                .concat("'")
                                .concat(",")

                                .concat("'")
                                .concat(objData.getGST_NO() != null ? objData.getGST_NO() : "")
                                .concat("'")
                                .concat(",")

                                .concat("'")
                                .concat(objData.getAddress() != null ? objData.getAddress() : "")
                                .concat("'")
                                .concat(",")

                                .concat("'")
                                .concat(objData.getNote() != null ? objData.getNote() : "")
                                .concat("'")
                                .concat(");");
                        Log.e("--Main--", "Insert qry: " + qry);

                        SQLiteStatement statement = db.compileStatement(qry);
                        int result = statement.executeUpdateDelete();
                        Log.e("--Main--", "Insert: " + qry);
                        Log.e("--Main--", "result: " + result);

                        if (result > 0) {
                            objData.setStatus("SUCCESS");

                        } else {

                            objData.setStatus("FAILED");
                            objData.setRemark("FAILED TO INSERT");

                        }
                    } catch (Exception e) {
                        Log.e("--Main--", "Inner sdf: " + e.getMessage());
                    }

                } else if (objData.getStatus() != null && !objData.getStatus().equalsIgnoreCase("INVALID MOBILE NO") &&
                        objData.getStatus().equalsIgnoreCase("EXISTS") &&
                        !objData.getStatus().equalsIgnoreCase("MOBILE NO IS REQUIRED")) {

                    String strSql = "UPDATE [CustomerMaster] SET "
                            .concat("[MOBILE_NO] = ")
                            .concat("'")
                            .concat(objData.getmMobile_No())
                            .concat("'");


                    Log.e("--Main--", "strSql: " + strSql);


                    if (objData.getmName() != null && objData.getmName() != "") {
                        strSql = strSql.concat(" ,[NAME] = ")
                                .concat("'")
                                .concat(objData.getmName()
                                        .replace("'", "''"))
                                .concat("'");

                        Log.e("--Main--", "Step:18");

                    }

                    if (objData.getCompany_Name() != null && objData.getCompany_Name() != "") {
                        strSql = strSql.concat(" ,[Company_Name] = ")
                                .concat("'")
                                .concat(objData.getCompany_Name()
                                        .replace("'", "''"))
                                .concat("'");


                        Log.e("--Main--", "Step:19");

                    }

                    if (objData.getGST_NO() != null && objData.getGST_NO() != "") {
                        strSql = strSql.concat(" ,[GST_NO] = ")
                                .concat("'")
                                .concat(objData.getGST_NO()
                                        .replace("'", "''"))
                                .concat("'");
                    }

                    if (objData.getAddress() != null && objData.getAddress() != "") {
                        strSql = strSql.concat(" ,[Address] = ")
                                .concat("'")
                                .concat(objData.getAddress()
                                        .replace("'", "''"))
                                .concat("'");

                        Log.e("--Main--", "Step:20");

                    }

                    if (objData.getBalanceType() != null && objData.getBalanceType() != "") {
                        strSql = strSql.concat(" ,[BalanceType] = ")
                                .concat("'")
                                .concat(objData.getBalanceType())
                                .concat("'");

                        Log.e("--Main--", "Step:21");

                    }

                    if (objData.getEmail() != null && objData.getEmail() != "") {
                        strSql = strSql.concat(" ,[Email] = ")
                                .concat("'")
                                .concat(objData.getEmail())
                                .concat("'");


                        Log.e("--Main--", "Step:22");


                    }

                    if (objData.getNote() != null && objData.getNote() != "") {
                        strSql = strSql.concat(" ,[Note] = ")
                                .concat("'")
                                .concat(objData.getNote())
                                .concat("'");


                        Log.e("--Main--", "Step:23");

                    }

                    strSql = strSql.concat(" WHERE [MOBILE_NO] = ")
                            .concat("'")
                            .concat(objData.getmMobile_No())
                            .concat("'");

                    Log.e("--Main--", "Step:24");


                    SQLiteStatement statement = db.compileStatement(strSql);
                    int result = statement.executeUpdateDelete();
                    Log.e("--Main--", "Update: " + strSql);


                    if (result > 0) {
                        objData.setRemark("SUCCESSFULLY UPDATED");

                        Log.e("--Main--", "Step:25");

                    } else {

                        Log.e("--Main--", "Step:26");

                        objData.setRemark("FAILED TO UPDATE");
                    }
                }


                lstClsGetValues.set(lstClsGetValues.indexOf(objData), objData);
//                lstClsCustomerMasters.add(objData);

                Log.e("--Main--", "Step:27");

            }

            db.close();
            //return lstClsCustomerMasters;

            Log.e("--Main--", "Step:28");


        } catch (Exception e) {
            db.close();
            Log.e("--Main--", "Step:29");
            Log.e("--Main--", "CatchBlock");
            Log.e("--Main--", "Exception: " + e.getMessage());
//            e.getMessage();
        }
        return lstClsGetValues;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClsCustomerMaster that = (ClsCustomerMaster) o;

        if (mMobile_No != null) {
            return mMobile_No.equals(that.mMobile_No);
        } else {
            return false;
        }
    }


    @Override
    public int hashCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.hash(mMobile_No);
        } else {
            return Objects.hashCode(mMobile_No);
        }
    }


    @SuppressLint("WrongConstant")
    public static List<String> getAddPaymentMasterColumn(Context context,
                                                         io.requery.android.database.sqlite.SQLiteDatabase db) {
        List<String> columns = new ArrayList<>();
        try {

//            io.requery.android.database.sqlite.SQLiteDatabase db;
//
//            db = io.requery.android.database.sqlite.SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath(ClsGlobal.Database_Name).getPath(),
//                    null);
            String qry = "SELECT * FROM [CustomerMaster] LIMIT 1 ";

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
    public static int Delete(ClsCustomerMaster objClsCustomerMaster, Context context) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "DELETE FROM [CustomerMaster] WHERE [ID] = "
                    .concat(String.valueOf(objClsCustomerMaster.getmId()))
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

/*

    private <T, R> Collector<T, ?, Stream<T>> distinctByKey(Function<T, R> keyExtractor) {
        return Collectors.collectingAndThen(
                toMap(
                        keyExtractor,
                        t -> t,
                        (t1, t2) -> t1
                ),
                (Map<R, T> map) -> map.values().stream()
        );
    }
*/

    @SuppressLint("WrongConstant")
    public static int DeleteAll(Context context) {
        int result = 0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String strSql = "DELETE FROM [CustomerMaster] ";
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
    public static int InsertWithCustomerPayment(ClsCustomerMaster ObjCustomerMaster, ClsPaymentMaster objClsPaymentMaster) {
        int result = 0;
        try {

            db = mContext.openOrCreateDatabase(ClsGlobal.Database_Name, mContext.MODE_APPEND, null);

            String qry = ("INSERT INTO [CustomerMaster] ([NAME],[MOBILE_NO]," +
                    "[Company_Name],[GST_NO],[Address],[OpeningStock],[Credit]" +
                    ",[Email],[Note],[BalanceType],[Save_Contact],[DOB],[AnniversaryDate]" +
                    ",[PanCard]) VALUES ('")

                    .concat(ObjCustomerMaster.getmName().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjCustomerMaster.getmMobile_No())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjCustomerMaster.getCompany_Name().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjCustomerMaster.getGST_NO())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjCustomerMaster.getAddress().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")

                    .concat(String.valueOf(ObjCustomerMaster.getOpeningStock()))
                    .concat(",")

                    .concat(String.valueOf(ObjCustomerMaster.getCredit()))
                    .concat(",")

                    .concat("'")
                    .concat(ObjCustomerMaster.getEmail())
                    .concat("'")
                    .concat(",")


                    .concat("'")
                    .concat(ObjCustomerMaster.getNote().trim()
                            .replace("'", "''"))
                    .concat("'")
                    .concat(",")


                    .concat("'")
                    .concat(ObjCustomerMaster.getBalanceType())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjCustomerMaster.getSave_Contact())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjCustomerMaster.getDOB())
                    .concat("'")
                    .concat(",")

                    .concat("'")
                    .concat(ObjCustomerMaster.getAnniversaryDate())
                    .concat("'")
                    .concat(",")


                    .concat("'")
                    .concat(ObjCustomerMaster.getPanCard())
                    .concat("'")

                    .concat(");");


            SQLiteStatement statement = db.compileStatement(qry);
            result = statement.executeUpdateDelete();
            Log.e("INSERT", "Vendor--->>" + qry);

            ClsPaymentMaster.InsertPaymentCustomer(objClsPaymentMaster, mContext);

            db.close();

        } catch (Exception e) {
            result = -1;
            Log.e("jsonObject", "Insert------" + e.getMessage());
            e.getMessage();
        }
        return result;
    }


//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
//        Set<Object> seen = ConcurrentHashMap.newKeySet();
//        return t -> seen.add(keyExtractor.apply(t));
//    }


    @SuppressLint("WrongConstant")
    public static List<String> getCustomerMasterColumns(Context context,
                                                        io.requery.android
                                                                .database
                                                                .sqlite.SQLiteDatabase db) {
        List<String> columns = new ArrayList<>();
        try {

//            io.requery.android.database.sqlite.SQLiteDatabase db;
//
//            db = io.requery.android.database.sqlite.SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath(ClsGlobal.Database_Name).getPath(),
//                    null);

            String qry = "SELECT * FROM [CustomerMaster] LIMIT 1 ";

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
    public static int UpdateInvoiceInfoCustomer(ClsCustomerMaster objClsCustomerMaster, Context context) {
        int result = 0;

        db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
        String strSql = "UPDATE [CustomerMaster] SET "
                .concat("[MOBILE_NO] = ")
                .concat("'")
                .concat(objClsCustomerMaster.getmMobile_No())
                .concat("'");

        if (objClsCustomerMaster.getmName() != null && objClsCustomerMaster.getmName() != "") {
            strSql = strSql.concat(" ,[NAME] = ")
                    .concat("'")
                    .concat(objClsCustomerMaster.getmName().trim()
                            .replace("'", "''"))
                    .concat("'");
        }

        if (objClsCustomerMaster.getCompany_Name() != null && objClsCustomerMaster.getCompany_Name() != "") {
            strSql = strSql.concat(" ,[Company_Name] = ")
                    .concat("'")
                    .concat(objClsCustomerMaster.getCompany_Name().trim()
                            .replace("'", "''"))
                    .concat("'");
        }

        if (objClsCustomerMaster.getGST_NO() != null && objClsCustomerMaster.getGST_NO() != "") {
            strSql = strSql.concat(" ,[GST_NO] = ")
                    .concat("'")
                    .concat(objClsCustomerMaster.getGST_NO().trim()
                            .replace("'", "''"))
                    .concat("'");
        }

        if (objClsCustomerMaster.getAddress() != null && objClsCustomerMaster.getAddress() != "") {
            strSql = strSql.concat(" ,[Address] = ")
                    .concat("'")
                    .concat(objClsCustomerMaster.getAddress().trim()
                            .replace("'", "''"))
                    .concat("'");
        }

        if (objClsCustomerMaster.getBalanceType() != null && objClsCustomerMaster.getBalanceType() != "") {
            strSql = strSql.concat(" ,[BalanceType] = ")
                    .concat("'")
                    .concat(objClsCustomerMaster.getBalanceType())
                    .concat("'");
        }
        strSql = strSql.concat(" WHERE [ID] = ")
                .concat(String.valueOf(objClsCustomerMaster.getmId()))
                .concat(";");

        Log.e("--Update--", "Update: " + strSql);
        SQLiteStatement statement = db.compileStatement(strSql);
        result = statement.executeUpdateDelete();
        //db.execSQL(strSql);
        db.close();
        return result;
    }


    @SuppressLint("WrongConstant")
    public static int Update(ClsCustomerMaster objClsCustomerMaster, Context context) {
        int result = 0;


        db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
        String strSql = "UPDATE [CustomerMaster] SET "
                .concat("[NAME] = ")
                .concat("'")
                .concat(objClsCustomerMaster.getmName().trim()
                        .replace("'", "''"))
                .concat("'")

                .concat(" ,[Company_Name] = ")
                .concat("'")
                .concat(objClsCustomerMaster.getCompany_Name().trim()
                        .replace("'", "''"))
                .concat("'")

                .concat(" ,[GST_NO] = ")
                .concat("'")
                .concat(objClsCustomerMaster.getGST_NO().trim()
                        .replace("'", "''"))
                .concat("'")

                .concat(" ,[Address] = ")
                .concat("'")
                .concat(objClsCustomerMaster.getAddress().trim()
                        .replace("'", "''"))
                .concat("'")

                .concat(" ,[MOBILE_NO] = ")
                .concat("'")
                .concat(objClsCustomerMaster.getmMobile_No())
                .concat("'")

                .concat(" ,[Credit] = ")
                .concat(String.valueOf(objClsCustomerMaster.getCredit()))

                .concat(" ,[OpeningStock] = ")
                .concat(String.valueOf(objClsCustomerMaster.getOpeningStock()))

                .concat(" ,[Email] = ")
                .concat("'")
                .concat(String.valueOf(objClsCustomerMaster.getEmail()))
                .concat("'")

                .concat(" ,[Note] = ")
                .concat("'")
                .concat(String.valueOf(objClsCustomerMaster.getNote()).trim()
                        .replace("'", "''"))
                .concat("'")

                .concat(" ,[Save_Contact] = ")
                .concat("'")
                .concat(String.valueOf(objClsCustomerMaster.getSave_Contact()))
                .concat("'")

                .concat(" ,[BalanceType] = ")
                .concat("'")
                .concat(objClsCustomerMaster.getBalanceType())
                .concat("'")

                .concat(" ,[DOB] = ")
                .concat("'")
                .concat(objClsCustomerMaster.getDOB())
                .concat("'")

                .concat(" ,[AnniversaryDate] = ")
                .concat("'")
                .concat(objClsCustomerMaster.getAnniversaryDate())
                .concat("'")

                .concat(" ,[PanCard] = ")
                .concat("'")
                .concat(objClsCustomerMaster.getPanCard())
                .concat("'")


                .concat(" WHERE [ID] = ")
                .concat(String.valueOf(objClsCustomerMaster.getmId()))
                .concat(";");

        Log.e("Update", strSql);

        Log.e("--Update--", "Update: " + strSql);
        SQLiteStatement statement = db.compileStatement(strSql);
        result = statement.executeUpdateDelete();
        //db.execSQL(strSql);
        db.close();
        return result;
    }

    @SuppressLint("WrongConstant")
    public boolean checkExists(String _where, Context context) {
        boolean result = false;

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = "SELECT 1 FROM [CustomerMaster] WHERE 1=1 "
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
    public static List<ClsCustomerMaster> getListCustomer(String where, Context context) {
        List<ClsCustomerMaster> list = new ArrayList<>();
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = ("SELECT *,IFNULL([OpeningStock],0) AS " +
                    "[OpeningStockBal],IFNULL([BalanceType], 'TO PAY') AS" +
                    " [BalanceType] FROM [CustomerMaster] where 1=1 ")
                    .concat(" AND [NAME] <> '' ")
                    .concat(where).concat(" ORDER BY [NAME]");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));
            Log.e("--qry--", "getList: " + qry);

            while (cur.moveToNext()) {
                ClsCustomerMaster getSet = new ClsCustomerMaster();
                getSet.setmId(cur.getInt(cur.getColumnIndex("ID")));
                getSet.setmName(cur.getString(cur.getColumnIndex("NAME")));
                getSet.setCompany_Name(cur.getString(cur.getColumnIndex("Company_Name")));
                getSet.setGST_NO(cur.getString(cur.getColumnIndex("GST_NO")));
                getSet.setAddress(cur.getString(cur.getColumnIndex("Address")));
                getSet.setmMobile_No(cur.getString(cur.getColumnIndex("MOBILE_NO")));
                getSet.setCredit(cur.getDouble(cur.getColumnIndex("Credit")));
                getSet.setEmail(cur.getString(cur.getColumnIndex("Email")));
                getSet.setBalanceType(cur.getString(cur.getColumnIndex("BalanceType")));
                getSet.setOpeningStock(cur.getDouble(cur.getColumnIndex("OpeningStockBal")));

                list.add(getSet);
            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    @SuppressLint("WrongConstant")
    public static ClsCustomerMaster getCustomerByMobileNo(String where, Context context) {
        ClsCustomerMaster getSet = new ClsCustomerMaster();
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT * FROM [CustomerMaster] WHERE 1=1 "
                    .concat(where)
                    .concat(";");


//            String qry = "SELECT * FROM [CustomerMaster] where 1=1 ".concat(where);

            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));
            Log.e("qry", qry);

            while (cur.moveToNext()) {

                getSet.setmId(cur.getInt(cur.getColumnIndex("ID")));
                getSet.setmName(cur.getString(cur.getColumnIndex("NAME")));
                getSet.setCompany_Name(cur.getString(cur.getColumnIndex("Company_Name")));
                getSet.setGST_NO(cur.getString(cur.getColumnIndex("GST_NO")));
                getSet.setAddress(cur.getString(cur.getColumnIndex("Address")));
                getSet.setEmail(cur.getString(cur.getColumnIndex("Email")));
                getSet.setCredit(cur.getDouble(cur.getColumnIndex("Credit")));
                getSet.setmMobile_No(cur.getString(cur.getColumnIndex("MOBILE_NO")));
                getSet.setBalanceType(cur.getString(cur.getColumnIndex("BalanceType")));
                getSet.setOpeningStock(cur.getDouble(cur.getColumnIndex("OpeningStock")));

            }


        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return getSet;
    }

    @SuppressLint("WrongConstant")
    public static Double getCustomerCredit(String mobileNo, Context context) {
        Double AvailableCredit = 0.0;
        Double customerCredit = 0.0;
        Double payments = 0.0;
        Double totalSaleAmt = 0.0;
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = "SELECT [Credit] FROM [CustomerMaster] where 1=1 "
                    .concat(" AND [MOBILE_NO]= ").concat("'").concat(mobileNo).concat("'");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));
            Log.e("qry", qry);

            while (cur.moveToNext()) {
                customerCredit = cur.getDouble(cur.getColumnIndex("Credit"));//5000
            }

            if (customerCredit != 0) {


                qry = "SELECT SUM(IFNULL([Amount],0)) AS [Amount] FROM [PaymentMaster] where 1=1 "
                        .concat(" AND [MobileNo]= ").concat("'").concat(mobileNo).concat("'");

                cur = db.rawQuery(qry, null);

                Log.e("cur", String.valueOf(cur.getCount()));
                Log.e("qry", qry);

                if (cur.getCount() != 0) {
                    while (cur.moveToNext()) {
                        payments = cur.getDouble(cur.getColumnIndex("Amount"));//-1000+800-200. -400
                    }
                }

                String saleAmt = "SELECT SUM(IFNULL([TotalReceiveableAmount],0)) AS [TotalSaleAmount] FROM [InventoryOrderMaster] "
                        .concat(" WHERE 1=1 ")
                        .concat(" AND [MobileNo]= ").concat("'").concat(mobileNo).concat("'");

                Cursor curPayment = db.rawQuery(saleAmt, null);

                Log.e("--Purchase--", "cur" + String.valueOf(curPayment.getCount()));
                Log.e("--Purchase--", "Query: " + saleAmt);

                if (curPayment.getCount() != 0) {

                    while (curPayment.moveToNext()) {
                        totalSaleAmt = curPayment.getDouble(curPayment.getColumnIndex("TotalSaleAmount"));//-1000+800-200. -400
                    }

                }
                Log.e("--CustomerMaster--", "totalSaleAmt:  " + totalSaleAmt);

            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }

        AvailableCredit = customerCredit + payments - totalSaleAmt;


        return AvailableCredit;
    }

    @SuppressLint("WrongConstant")
    public static ClsCustomerMaster getObject(ClsCustomerMaster objClsCustomerMaster, Context context) {
        ClsCustomerMaster obj = new ClsCustomerMaster();

        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);
            String sqlStr = ("SELECT [ID],[NAME],[Company_Name],[GST_NO],[Address],[MOBILE_NO]," +
                    "[OpeningStock],[Credit],[Email],[Note],[BalanceType],[Save_Contact]" +
                    ",[DOB],[AnniversaryDate],[PanCard] FROM [CustomerMaster] WHERE 1=1 AND [ID] = ")
                    .concat(String.valueOf(objClsCustomerMaster.getmId()))
                    .concat(";");


            Log.e("getObject", ">>>>>>>>>" + sqlStr);
            Cursor cur = db.rawQuery(sqlStr, null);
            Log.e("getObject", ">>>>>>>>>" + cur.getCount());
            while (cur.moveToNext()) {

                obj.setmId(cur.getInt(cur.getColumnIndex("ID")));
                obj.setmName(cur.getString(cur.getColumnIndex("NAME")));
                obj.setCompany_Name(cur.getString(cur.getColumnIndex("Company_Name")));
                obj.setGST_NO(cur.getString(cur.getColumnIndex("GST_NO")));
                obj.setAddress(cur.getString(cur.getColumnIndex("Address")));
                obj.setmMobile_No(cur.getString(cur.getColumnIndex("MOBILE_NO")));
                obj.setCredit(cur.getDouble(cur.getColumnIndex("Credit")));
                obj.setOpeningStock(cur.getDouble(cur.getColumnIndex("OpeningStock")));
                obj.setEmail(cur.getString(cur.getColumnIndex("Email")));
                obj.setNote(cur.getString(cur.getColumnIndex("Note")));
                obj.setBalanceType(cur.getString(cur.getColumnIndex("BalanceType")));
                obj.setSave_Contact(cur.getString(cur.getColumnIndex("Save_Contact")));
                obj.setDOB(cur.getString(cur.getColumnIndex("DOB")));
                obj.setAnniversaryDate(cur.getString(cur.getColumnIndex("AnniversaryDate")));
                obj.setPanCard(cur.getString(cur.getColumnIndex("PanCard")));

            }

            db.close();

        } catch (Exception e) {
            Log.e("getObject", "GetObjectttt" + e.getMessage());
            e.getMessage();
        }
        return obj;
    }


    @SuppressLint("WrongConstant")
    public static List<ClsCustomerMaster> getCustomerListForDirectPayment
            (String where, Context context, SQLiteDatabase db) {

        List<ClsCustomerMaster> list = new ArrayList<>();
        try {

//            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);


            String qry = ("SELECT [ID],upper([NAME]) AS [NAME],[Credit],[BalanceType],[OpeningStock],[GST_NO],[MOBILE_NO] " +
                    "FROM [CustomerMaster] where 1=1 ")
                    .concat(where).concat(" ORDER BY [NAME]");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("--Cust--", "Qry: " + qry);
            Log.e("--Cust--", "Count: " + cur.getCount());

            while (cur.moveToNext()) {
                ClsCustomerMaster getSet = new ClsCustomerMaster();

                getSet.setmId(cur.getInt(cur.getColumnIndex("ID")));
                getSet.setmName(cur.getString(cur.getColumnIndex("NAME")));
                getSet.setCredit(cur.getDouble(cur.getColumnIndex("Credit")));
                getSet.setOpeningStock(cur.getDouble(cur.getColumnIndex("OpeningStock")));
                getSet.setBalanceType(cur.getString(cur.getColumnIndex("BalanceType")));
                getSet.setGST_NO(cur.getString(cur.getColumnIndex("GST_NO")));
                getSet.setmMobile_No(cur.getString(cur.getColumnIndex("MOBILE_NO")));

                if (cur.getString(cur.getColumnIndex("NAME")) != null &&
                        !cur.getString(cur.getColumnIndex("NAME")).equalsIgnoreCase("") &&
                        cur.getString(cur.getColumnIndex("MOBILE_NO")) != null &&
                        !cur.getString(cur.getColumnIndex("MOBILE_NO")).equalsIgnoreCase("")) {

                    list.add(getSet);
                }
            }

        } catch (Exception e) {
            Log.e("--Cust--", "Exception: " + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsCustomerMaster> ListCustomers(String where, Context context) {
        List<ClsCustomerMaster> list = new ArrayList<>();
        try {

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);


            String qry = ("SELECT [ID],upper([NAME]) AS [NAME],[Company_Name],[GST_NO],[MOBILE_NO] " +
                    "FROM [CustomerMaster] where 1=1 ")
                    .concat(where).concat(" ORDER BY [NAME]");

            Cursor cur = db.rawQuery(qry, null);

            Log.e("cur", String.valueOf(cur.getCount()));
            Log.e("qry", qry);

            while (cur.moveToNext()) {
                ClsCustomerMaster getSet = new ClsCustomerMaster();

                getSet.setmId(cur.getInt(cur.getColumnIndex("ID")));
                getSet.setmName(cur.getString(cur.getColumnIndex("NAME")));
                getSet.setCompany_Name(cur.getString(cur.getColumnIndex("Company_Name")));
                getSet.setGST_NO(cur.getString(cur.getColumnIndex("GST_NO")));
                getSet.setmMobile_No(cur.getString(cur.getColumnIndex("MOBILE_NO")));

                if (cur.getString(cur.getColumnIndex("NAME")) != null &&
                        !cur.getString(cur.getColumnIndex("NAME")).equalsIgnoreCase("") &&
                        cur.getString(cur.getColumnIndex("MOBILE_NO")) != null &&
                        !cur.getString(cur.getColumnIndex("MOBILE_NO")).equalsIgnoreCase("")) {

                    list.add(getSet);
                }
            }

        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }

    @SuppressLint("WrongConstant")
    public static List<ClsCustomerMaster> getAsyncTaskListCustomers(String where, Context context) {
        List<ClsCustomerMaster> list = new ArrayList<>();
        try {
            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, context.MODE_APPEND, null);

            String qry = ("SELECT [ID],upper([NAME]) AS [NAME],[MOBILE_NO] " +
                    "FROM [CustomerMaster] where 1=1 ")
                    .concat(where).concat(" ORDER BY [NAME]");

            Cursor cur = db.rawQuery(qry, null);

            while (cur.moveToNext()) {
                ClsCustomerMaster getSet = new ClsCustomerMaster();
                getSet.setmId(cur.getInt(cur.getColumnIndex("ID")));
                getSet.setmName(cur.getString(cur.getColumnIndex("NAME")));
                getSet.setmMobile_No(cur.getString(cur.getColumnIndex("MOBILE_NO")));

                if (cur.getString(cur.getColumnIndex("MOBILE_NO")) != null &&
                        !cur.getString(cur.getColumnIndex("MOBILE_NO")).equalsIgnoreCase("")) {
                    list.add(getSet);
                }
            }
        } catch (Exception e) {
            Log.e("c", "GetList" + e.getMessage());
            e.getMessage();
        }
        return list;
    }


    public static class ClsExcelImport {
        String _status = "";
        List<ClsCustomerMaster> lstClsCustomerMasters;

        int _totalRecords = 0;
        int _successRecords = 0;
        int _alreadyExists = 0;
        int _failedRecords = 0;

        public int get_totalRecords() {
            return _totalRecords;
        }

        public void set_totalRecords(int _totalRecords) {
            this._totalRecords = _totalRecords;
        }

        public int get_successRecords() {
            return _successRecords;
        }

        public void set_successRecords(int _successRecords) {
            this._successRecords = _successRecords;
        }

        public int get_alreadyExists() {
            return _alreadyExists;
        }

        public void set_alreadyExists(int _alreadyExists) {
            this._alreadyExists = _alreadyExists;
        }

        public int get_failedRecords() {
            return _failedRecords;
        }

        public void set_failedRecords(int _failedRecords) {
            this._failedRecords = _failedRecords;
        }

        public String get_status() {
            return _status;
        }

        public void set_status(String _status) {
            this._status = _status;
        }

        public List<ClsCustomerMaster> getLstClsCustomerMasters() {
            return lstClsCustomerMasters;
        }

        public void setLstClsCustomerMasters(List<ClsCustomerMaster> lstClsCustomerMasters) {
            this.lstClsCustomerMasters = lstClsCustomerMasters;
        }
    }
}
