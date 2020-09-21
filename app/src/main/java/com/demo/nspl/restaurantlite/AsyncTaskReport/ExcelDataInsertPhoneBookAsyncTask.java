package com.demo.nspl.restaurantlite.AsyncTaskReport;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.classes.ClsCustomerMaster;
import com.demo.nspl.restaurantlite.classes.ClsReadData;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ExcelDataInsertPhoneBookAsyncTask extends AsyncTask<Void, Void, ClsCustomerMaster.ClsExcelImport> {


    private String _where = "";
    private String mode = "";

    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private TextView txt;
    //    private PurchaseMasterAdapter purchaseMasterAdapter;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    Set<ClsReadData> lstClsGetValues = new LinkedHashSet<>();


    public ExcelDataInsertPhoneBookAsyncTask(Context context, ProgressBar progressBar) {
        this.context = context;
        this.progressBar = progressBar;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);

    }

    @SuppressLint("NewApi")
    @Override
    protected ClsCustomerMaster.ClsExcelImport doInBackground(Void... voids) {

        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);

        String id = null, name = null, email = null, phone = null, note = null, orgName = null, title = null;
        String type1 = "", type2 = "", type3 = "";
        String _valueAdd = "";

        int size = cur.getCount();
        List<ClsCustomerMaster> lstClsCustomerMasters = new ArrayList<>();
        ClsCustomerMaster.ClsExcelImport objResult = new ClsCustomerMaster.ClsExcelImport();
        objResult.setLstClsCustomerMasters(lstClsCustomerMasters);

        ClsCustomerMaster _rowObj = new ClsCustomerMaster(context);

        try {
            if (cur.getCount() > 0) {
                int cnt = 1;
                while (cur.moveToNext()) {
                    email = "";
                    name = "";
                    cnt++;
                    id = cur.getString(cur
                            .getColumnIndex(ContactsContract.Contacts._ID));
                    name = cur
                            .getString(cur
                                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    if (Integer
                            .parseInt(cur.getString(cur
                                    .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor pCur = cr
                                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                                + " = ?", new String[]{id},
                                        null);
                        while (pCur.moveToNext()) {
                            String phonetype = pCur
                                    .getString(pCur
                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                            String MainNumber = pCur
                                    .getString(pCur
                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            String noteWhere = ContactsContract.Data.CONTACT_ID
                                    + " = ? AND " + ContactsContract.Data.MIMETYPE
                                    + " = ?";
                            String[] noteWhereParams = new String[]{
                                    id,
                                    ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
                            Cursor noteCur = cr.query(
                                    ContactsContract.Data.CONTENT_URI, null,
                                    noteWhere, noteWhereParams, null);

                            note = " ";

                            Cursor emailCur = cr
                                    .query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                            null,
                                            ContactsContract.CommonDataKinds.Email.CONTACT_ID
                                                    + " = ?", new String[]{id},
                                            null);
                            email = "";


                            String f_number = MainNumber.replace("+91", "");
//                        Log.e("--Main--", "MainNumber: " + f_number.trim());


                            if (f_number.trim().matches(ClsGlobal.MobileNo_Pattern)) {

//                            Log.e("--Main--", "MainNumber: " + f_number.trim());

                                if (name != null && name != "") {
                                    name = name;
                                }

                                if (noteCur.moveToFirst()) {
                                    note = noteCur
                                            .getString(noteCur
                                                    .getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));

                                    if (note == null) {
                                        note = "";
                                    } else {
                                        Log.e("note", note);

                                    }
                                }
                                noteCur.close();

                                while (emailCur.moveToNext()) {

                                    email = emailCur
                                            .getString(emailCur
                                                    .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                                    String emailType = emailCur
                                            .getString(emailCur
                                                    .getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

                                    if (email == null) {
                                        email = "";
                                    } else {
                                        Log.e("email", email);
                                    }

                                    if (emailType.equalsIgnoreCase("1")) {
                                    } else {
                                        Log.e("emailType", emailType);
                                    }
                                }
                                // add
                                emailCur.close();


                                String orgWhere = ContactsContract.Data.CONTACT_ID
                                        + " = ? AND " + ContactsContract.Data.MIMETYPE
                                        + " = ?";
                                String[] orgWhereParams = new String[]{
                                        id,
                                        ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
                                Cursor orgCur = cr.query(
                                        ContactsContract.Data.CONTENT_URI, null,
                                        orgWhere, orgWhereParams, null);
                                orgName = " ";
                                if (orgCur.moveToFirst()) {
                                    orgName = orgCur
                                            .getString(orgCur
                                                    .getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY));

                                }
                                if (orgName == null) {
                                    orgName = "";
                                } else {
                                    Log.e("OrgName", orgName);
                                }
                                orgCur.close();

                                Log.e("--orgName--", "orgName: " + orgName);

                                Cursor addrCur = cr
                                        .query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                                                null,
                                                ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID
                                                        + " = ?", new String[]{id},
                                                null);
                                if (addrCur.getCount() == 0) {
                                    // addbuffer.append("unknown");
                                } else {
                                    int cntr = 0;
                                    while (addrCur.moveToNext()) {

                                        cntr++;
                                        String poBox = addrCur
                                                .getString(addrCur
                                                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
                                        if (poBox == null) {
                                            poBox = "";
                                        } else {
                                            Log.e("--Text--", "poBox: " + poBox);
                                        }
                                        String street = addrCur
                                                .getString(addrCur
                                                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                                        if (street == null) {
                                            street = "";
                                        } else {
                                            Log.e("street", street);

                                            street = street.concat(" ");
                                        }
                                        String neb = addrCur
                                                .getString(addrCur
                                                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.NEIGHBORHOOD));
                                        if (neb == null) {
                                            neb = "";
                                        } else {
                                            Log.e("neb", neb);
                                        }
                                        String city = addrCur
                                                .getString(addrCur
                                                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                                        if (city == null) {
                                            city = "";
                                        } else {
                                            city = city.concat(" ");
                                        }
                                        String state = addrCur
                                                .getString(addrCur
                                                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                                        if (state == null) {
                                            state = "";
                                        } else {
                                            state = state.concat(" ");
                                        }
                                        String postalCode = addrCur
                                                .getString(addrCur
                                                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
                                        if (postalCode == null) {
                                            postalCode = "";
                                        } else {
                                            Log.e("postalCode", postalCode);
                                        }
                                        String country = addrCur
                                                .getString(addrCur
                                                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
                                        if (country == null) {
                                            country = "";
                                        } else {
                                            country = country.concat(",");
                                        }

                                        String type = addrCur
                                                .getString(addrCur
                                                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
                                        if (type == null) {
                                            type = "";
                                        } else {
                                            Log.e("--Text--", "type: " + type);
                                        }


                       /* String _valueAdd = "".concat(street).concat("")
                                .concat(city).concat(" ")
                                .concat(state).concat(" ")
                                .concat(country).concat(",")
                                .concat(postalCode);*/

                                         _valueAdd = "".concat(street)
                                                .concat(city)
                                                .concat(state)
                                                .concat(country)
                                                .concat(postalCode);

                                        Log.e("--Address--", "_valueAdd: " + _valueAdd);
                                        Log.e("--Address--", "-----------------------------");
                                    }

                                }

                                addrCur.close();


                                _rowObj.setmName(name);
                                _rowObj.setmMobile_No(f_number.trim());
                                _rowObj.setEmail(email);
                                _rowObj.setNote(note);
                                _rowObj.setCompany_Name(orgName);
                                _rowObj.setGST_NO("");
                                _rowObj.setAddress(_valueAdd);
                                _rowObj.setStatus("SUCCESS");

                                lstClsCustomerMasters.add(_rowObj);

//                                Gson gson = new Gson();
//                                String jsonInString = gson.toJson(lstClsCustomerMasters);
//                                Log.e("--Main--", "_rowObj: " + jsonInString);

                            }

                            if (lstClsCustomerMasters.size() != 0) {
                                lstClsCustomerMasters = ClsCustomerMaster.InsertExcelRecord(context,
                                        lstClsCustomerMasters);
                                objResult.set_status("DATA INSERTED SUCCESSFULLY");
                                objResult.setLstClsCustomerMasters(lstClsCustomerMasters);

                            }
                        }
                        Log.e("--Main--", "----------------------------");
                        pCur.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            objResult.set_status("wrong file selected");
        }
        return objResult;
    }


    @Override
    protected void onPostExecute(ClsCustomerMaster.ClsExcelImport objResult) {
        super.onPostExecute(objResult);

        progressBar.setVisibility(View.GONE);
        Log.e("--objResult--", "getResult: " + objResult.get_status());

        if (objResult.get_status().equalsIgnoreCase("Wrong file selected")) {
            Toast.makeText(context, "Wrong file selected", Toast.LENGTH_SHORT).show();
        } else if (objResult.get_status().equalsIgnoreCase("sheet1 not found")) {
            Toast.makeText(context, "Sheet1 is not found", Toast.LENGTH_SHORT).show();
        } else if (objResult.get_status().equalsIgnoreCase("No Data found")) {
            Toast.makeText(context, "No data found in selected file", Toast.LENGTH_SHORT).show();
        } else if (objResult.get_status().equalsIgnoreCase("MobileNo column")) {
            Toast.makeText(context, "Mobile No column not found", Toast.LENGTH_SHORT).show();
        } else if (objResult.get_status().equalsIgnoreCase("Customer Name column")) {
            Toast.makeText(context, "Customer Name column not found", Toast.LENGTH_SHORT).show();
        } else if (objResult.get_status().equalsIgnoreCase("DATA INSERTED SUCCESSFULLY")) {
            Toast.makeText(context, "DATA INSERTED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
        }

    }

}
