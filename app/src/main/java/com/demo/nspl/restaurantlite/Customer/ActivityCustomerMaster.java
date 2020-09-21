package com.demo.nspl.restaurantlite.Customer;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.ClsPermission;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsCustomerMaster;
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.demo.nspl.restaurantlite.Global.ClsPermission.REQUEST_READ_PHONE_STATE;

public class ActivityCustomerMaster extends AppCompatActivity {

    TextView txt_customerID, txt_anniversary_date, txt_dob;

    EditText edit_Mobile_no, edit_CustomerName, edit_CompanyName,
            edit_GSTNo, edit_address, edit_Credit, edit_opening_bal,
            edit_Email_ID, edit_Note, edit_pan_card_no;

    Button Btn_Save;
    Integer _ID;

    private CheckBox CheckBox_Save_Contact;
    private int result;
    int mYear, mMonth, mDay;
    final Calendar c = Calendar.getInstance();

    RadioButton rb_to_pay, rb_to_collect;
    ImageButton iv_clear_anniversary, iv_clear_dob;
    ImageButton iv_call_browse, iv_clear_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_master);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "ActivityCustomerMaster"));
        }

        ClsPermission.checkpermission(ActivityCustomerMaster.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Add Customer");
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        main();
    }

    @SuppressLint("IntentReset")
    private void main() {

        rb_to_pay = findViewById(R.id.rb_to_pay);
        rb_to_collect = findViewById(R.id.rb_to_collect);
        txt_customerID = findViewById(R.id.txt_customerID);
        edit_Mobile_no = findViewById(R.id.edit_Mobile_no);
        edit_CustomerName = findViewById(R.id.edit_CustomerName);
        edit_CompanyName = findViewById(R.id.edit_CompanyName);
        edit_GSTNo = findViewById(R.id.edit_GSTNo);
        edit_Credit = findViewById(R.id.edit_Credit);
        edit_address = findViewById(R.id.edit_address);
        edit_opening_bal = findViewById(R.id.edit_opening_bal);
        edit_Email_ID = findViewById(R.id.edi_Email_ID);
        edit_Note = findViewById(R.id.edit_Note);
        CheckBox_Save_Contact = findViewById(R.id.CheckBox_Save_Contact);
        txt_anniversary_date = findViewById(R.id.txt_anniversary_date);
        txt_dob = findViewById(R.id.txt_dob);
        edit_pan_card_no = findViewById(R.id.edit_pan_card_no);

        iv_clear_anniversary = findViewById(R.id.iv_clear_anniversary);
        iv_clear_dob = findViewById(R.id.iv_clear_dob);


        iv_call_browse = findViewById(R.id.iv_call_browse);
        iv_clear_number = findViewById(R.id.iv_clear_number);


        iv_clear_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_dob.setText("");
            }
        });

        iv_clear_anniversary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_anniversary_date.setText("");
            }
        });

        Btn_Save = findViewById(R.id.Btn_Save);

        Intent intent = getIntent();

        _ID = intent.getIntExtra("_ID", 0);
        Log.e("_ID", "_ID:-- " + _ID);

        if (_ID != 0) {
            fillValues();
        }

        txt_anniversary_date.setOnClickListener(v -> {


            DatePickerDialog dpd = new DatePickerDialog(ActivityCustomerMaster.this,
                    (view1, year, month, day) -> {

                        c.set(year, month, day);
                        @SuppressLint("SimpleDateFormat")
                        String _validDate = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());

                        txt_anniversary_date.setText(_validDate);
                    }, mYear, mMonth, mDay);


            if (!dpd.isShowing()) {
                dpd.show();
            }

        });

        txt_dob.setOnClickListener(v -> {


            DatePickerDialog dpd = new DatePickerDialog(ActivityCustomerMaster.this,
                    (view1, year, month, day) -> {

                        c.set(year, month, day);
                        @SuppressLint("SimpleDateFormat")
                        String _validDate = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());

                        txt_dob.setText(_validDate);
                    }, mYear, mMonth, mDay);


            if (!dpd.isShowing()) {
                dpd.show();
            }
        });


        iv_call_browse.setOnClickListener(v -> {
            Intent intent1 = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            intent1.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(intent1, 1);
        });


        iv_clear_number.setOnClickListener(v -> {
            edit_Mobile_no.setText("");
        });


        Btn_Save.setOnClickListener(v -> {

            if (edit_CustomerName.getText() == null || edit_CustomerName.getText().toString().trim().isEmpty()) {
                Toast.makeText(ActivityCustomerMaster.this, "Name is required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (edit_Mobile_no.getText() == null || edit_Mobile_no.getText().toString().trim().isEmpty()) {
                Toast.makeText(ActivityCustomerMaster.this, "Mobile is required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (CheckBox_Save_Contact.isChecked() && edit_Email_ID.getText().toString().length() > 0) {
                if (edit_Email_ID.getText() == null || edit_Email_ID.getText().toString().trim().isEmpty()
                        || !ClsGlobal.isValidEmail(edit_Email_ID.getText().toString().trim())) {
                    Toast.makeText(ActivityCustomerMaster.this, "Enter Valid Email Id", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            ClsCustomerMaster objClsCustomerMaster =
                    new ClsCustomerMaster(ActivityCustomerMaster.this);

            String where = " AND  [MOBILE_NO] = "
                    .concat("'")
                    .concat(edit_Mobile_no.getText().toString().trim())
                    .concat("'");

            if (_ID != 0) {
                where = where.concat(" AND [ID] <> ").concat(String.valueOf(_ID));
            }

            boolean exists = objClsCustomerMaster.checkExists(where, ActivityCustomerMaster.this);
            Log.d("--RoomMaster--", "onClick: " + where);

            if (exists) {
                Toast toast = Toast.makeText(ActivityCustomerMaster.this, "Customer is already exists....", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                return;
            } else {

                objClsCustomerMaster.setmId(_ID);
                objClsCustomerMaster.setmName(edit_CustomerName.getText().toString().trim());
                objClsCustomerMaster.setmMobile_No(edit_Mobile_no.getText().toString().trim());
                objClsCustomerMaster.setGST_NO(edit_GSTNo.getText().toString().trim());
                objClsCustomerMaster.setAddress(edit_address.getText().toString().trim());
                objClsCustomerMaster.setCompany_Name(edit_CompanyName.getText().toString().trim());
                objClsCustomerMaster.setCredit(Double.valueOf(edit_Credit.getText().toString()
                        .equalsIgnoreCase("") ? "0.00" : edit_Credit.getText().toString().trim()));
                objClsCustomerMaster.setOpeningStock(Double.valueOf(edit_opening_bal.getText().toString()
                        .equalsIgnoreCase("") ? "0.00" : edit_opening_bal.getText().toString().trim()));

                objClsCustomerMaster.setEmail(edit_Email_ID.getText().toString().equalsIgnoreCase("")
                        ? "" : edit_Email_ID.getText().toString().trim());

                objClsCustomerMaster.setNote(edit_Note.getText().toString().equalsIgnoreCase("") ?
                        "" : edit_Note.getText().toString().trim());

                if (CheckBox_Save_Contact.isChecked()) {
                    objClsCustomerMaster.setSave_Contact("YES");
                } else {
                    objClsCustomerMaster.setSave_Contact("NO");
                }

                objClsCustomerMaster.setBalanceType(rb_to_pay.isChecked() ? "TO PAY" : "TO COLLECT");

                objClsCustomerMaster.setDOB(txt_dob.getText().toString()
                        .equalsIgnoreCase("dd/MM/yyyy")
                        ? "" : txt_dob.getText().toString());

                objClsCustomerMaster.setAnniversaryDate(txt_anniversary_date.getText().toString()
                        .equalsIgnoreCase("dd/MM/yyyy")
                        ? "" : txt_anniversary_date.getText().toString());

                objClsCustomerMaster.setPanCard(edit_pan_card_no.getText().toString());

                //-----------------------------Insert into PaymentMaster Table-----------------------------------//

                ClsPaymentMaster insertPaymentMaster = new ClsPaymentMaster();
                insertPaymentMaster.setPaymentDate(ClsGlobal.getCurruntDateTime());
                insertPaymentMaster.setPaymentMounth(ClsGlobal.getDayMonth(ClsGlobal.getEntryDate_dd_MM_yyyy()));
                insertPaymentMaster.setVendorID(0);
                insertPaymentMaster.setMobileNo(edit_Mobile_no.getText().toString().equalsIgnoreCase("")
                        ? "" : edit_Mobile_no.getText().toString().trim());
                insertPaymentMaster.setCustomerName(edit_CustomerName.getText().toString().equalsIgnoreCase("")
                        ? "" : edit_CustomerName.getText().toString().trim());
                insertPaymentMaster.setVendorName("");
                insertPaymentMaster.setPaymentMode("Opening Balance");
                insertPaymentMaster.setPaymentDetail("Opening Balance");
                insertPaymentMaster.setInvoiceNo("0");
                Double openingBal = Double.valueOf(edit_opening_bal.getText().toString().trim().equalsIgnoreCase("")
                        ? "0.00" : edit_opening_bal.getText().toString().trim());

//                openingBal = Double.valueOf(edit_opening_bal.getText().toString());

                if (edit_opening_bal.getText() != null && !edit_opening_bal.getText().toString().isEmpty()
                        && !edit_opening_bal.getText().toString().equalsIgnoreCase(".")) {

                    objClsCustomerMaster.setOpeningStock(Double.valueOf(edit_opening_bal.getText()
                            .toString().trim()));


                    if (rb_to_pay.isChecked()) {
                        insertPaymentMaster.setAmount(openingBal);
                    } else {
                        insertPaymentMaster.setAmount(openingBal * -1);
                    }

                } else {
                    insertPaymentMaster.setAmount(0.0);
                }


                insertPaymentMaster.setRemark("Customer opening balance");
                insertPaymentMaster.setEntryDate(ClsGlobal.getEntryDate());
                insertPaymentMaster.setType("Customer");
                insertPaymentMaster.setReceiptNo("0");


                if (objClsCustomerMaster.getmId() == 0) {
//                    result = ClsCustomerMaster.InsertCustomer(objClsCustomerMaster, ActivityCustomerMaster.this);

                    result = ClsCustomerMaster.InsertWithCustomerPayment(objClsCustomerMaster, insertPaymentMaster);
                    if (result == -1) {
                        Toast.makeText(getApplicationContext(), "Technical Error", Toast.LENGTH_SHORT).show();
                    } else if (result == 0) {
                        Toast.makeText(getApplicationContext(), "Data Not Inserted", Toast.LENGTH_SHORT).show();
                    } else {

                        if (CheckBox_Save_Contact.isChecked()) {
                            ClsGlobal.Save_Contact(edit_Mobile_no.getText().toString().trim()
                                    , edit_CustomerName.getText().toString().trim()
                                    , edit_Email_ID.getText().toString().trim()
                                    , edit_address.getText().toString().trim()
                                    , edit_Note.getText().toString().trim()
                                    , "Add"
                                    , ActivityCustomerMaster.this);
                        }

                    }

                } else {
                    result = ClsCustomerMaster.Update(objClsCustomerMaster, ActivityCustomerMaster.this);
                    Log.e("UPDATE---  ", String.valueOf(result));

                    if (CheckBox_Save_Contact.isChecked() &&
                            !edit_Mobile_no.getText().toString().isEmpty()) {

                        String getContectId = "";

                        getContectId = ClsGlobal.getContactIdByNunber(edit_Mobile_no.getTag().toString().trim()
                                , ActivityCustomerMaster.this);
                        Log.d("getContectId", getContectId);

                        if (!getContectId.isEmpty() || !getContectId.equalsIgnoreCase("")) {
                            int result = ClsGlobal.DeleteContactById(ActivityCustomerMaster.this,
                                    Long.parseLong(getContectId));
                        }

                        Log.e("result ", "DeleteContactById:-" + String.valueOf(result));

                        ClsGlobal.Save_Contact(edit_Mobile_no.getText().toString().trim()
                                , edit_CustomerName.getText().toString().trim()
                                , edit_Email_ID.getText().toString().trim()
                                , edit_address.getText().toString().trim()
                                , edit_Note.getText().toString().trim()
                                , getContectId.equalsIgnoreCase("") ? "Add" : "Update"
                                , ActivityCustomerMaster.this);

                    }

                    ClsPaymentMaster.deleteOpeningBalanceCustomer(objClsCustomerMaster.getmMobile_No(),
                            ActivityCustomerMaster.this);

                    ClsPaymentMaster.InsertPaymentCustomer(insertPaymentMaster, ActivityCustomerMaster.this);

                    if (result > 0) {
                        Toast.makeText(getApplicationContext(), "Update successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error while updating", Toast.LENGTH_SHORT).show();
                    }
                }
                finish();
            }
        });
    }

    private void fillValues() {
        ClsCustomerMaster objClsCustomerMaster = new ClsCustomerMaster();
        objClsCustomerMaster.setmId(_ID);
        objClsCustomerMaster = ClsCustomerMaster.getObject(objClsCustomerMaster, ActivityCustomerMaster.this);

        txt_customerID.setText(String.valueOf(objClsCustomerMaster.getmId()));
        edit_Mobile_no.setText(objClsCustomerMaster.getmMobile_No());

        edit_Mobile_no.setTag(objClsCustomerMaster.getmMobile_No());

        edit_address.setText(objClsCustomerMaster.getAddress());
        edit_GSTNo.setText(objClsCustomerMaster.getGST_NO());
        edit_CompanyName.setText(objClsCustomerMaster.getCompany_Name());
        edit_CustomerName.setText(objClsCustomerMaster.getmName());
        edit_Credit.setText(String.valueOf(objClsCustomerMaster.getCredit()));
        edit_opening_bal.setText(String.valueOf(objClsCustomerMaster.getOpeningStock()));

        if (objClsCustomerMaster.getEmail() != null && objClsCustomerMaster.getNote() != null
                && objClsCustomerMaster.getSave_Contact() != null) {
            edit_Email_ID.setText(objClsCustomerMaster.getEmail());
            edit_Note.setText(objClsCustomerMaster.getNote());

            if (objClsCustomerMaster.getSave_Contact().equalsIgnoreCase("YES")) {
                CheckBox_Save_Contact.setChecked(true);
            } else {
                CheckBox_Save_Contact.setChecked(false);
            }

        } else {
            edit_Email_ID.setText("");
            edit_Note.setText("");
        }

        if (objClsCustomerMaster.getBalanceType() != null) {
            if (objClsCustomerMaster.getBalanceType().equalsIgnoreCase("TO PAY")) {
                rb_to_pay.setChecked(true);
            } else if (objClsCustomerMaster.getBalanceType().equalsIgnoreCase("TO COLLECT")) {
                rb_to_collect.setChecked(true);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE: {
                ClsPermission.checkPermission(requestCode, permissions, grantResults, ActivityCustomerMaster.this);
                return;
            }

        }


    }


    private Uri uri = null;
    private static final Pattern MobileNo_Pattern
            = Pattern.compile(ClsGlobal.MobileNo_Pattern);


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1) && (resultCode == RESULT_OK)) {
            Cursor cursor = null;
            try {
                uri = data.getData();
                cursor = getContentResolver().query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);


//                Cursor cursor = getContentResolver().query(uri, null, null, null, null);



                if (cursor != null && cursor.moveToNext()) {

                    String phone = cursor.getString(0);

                    if (phone.contains("+91") || phone.contains("+91 ")) {
                        Log.e("phone", "+91");

                        phone = phone.replace("+91", "");
                    } else if (String.valueOf(phone.charAt(0)).contains("0")) {
                        Log.e("phone", "phone.charAt(0) call");
                        phone = phone.replace("0", "");
                    }

                    if (phone.contains(" ")) {
                        phone = phone.replace(" ", "");
                    }

                    Matcher matcher = MobileNo_Pattern.matcher(phone);

                    if (matcher.matches()) {
                        edit_Mobile_no.setText(phone);
                        retrieveContactName();
                    } else {
                        edit_Mobile_no.setText("");
                        edit_CustomerName.setText("");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void retrieveContactName() {

        String contactName = null;

        // querying contact data store


        Cursor cursor = getContentResolver().query(uri, null,
                null, null, null);

        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex
                    (ContactsContract.Contacts.DISPLAY_NAME));
            edit_CustomerName.setText(contactName);
        }
        cursor.close();

    }


    public ArrayList<String> getNameEmailDetails() {
        String email = "";
        ArrayList<String> names = new ArrayList<String>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor cur1 = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                while (cur1.moveToNext()) {
                    //to get the contact names

                    String name = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String mobile = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    Log.e("--Email--", "name: " + name);
                    Log.e("--Email--", "mobile-0-0-0-0-0-0-0-0-0: " + mobile);
                    Log.e("--Email--", "edit_Mobile_no-0-0-0-0-0-0-: " + edit_Mobile_no.getText().toString());

                    if (mobile == edit_Mobile_no.getText().toString()) {
                        email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        Log.e("--Email--", "Email: " + email);

                    }


                    if (email != null) {
                        names.add(name);
                    }
                }
                cur1.close();
                edit_Email_ID.setText(email);

            }
        }
        return names;
    }


    /*String[] emailParams = new String[]{ContactId, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE};
                String[] nameParams = new String[]{ContactId, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE};
                String[] numberParams = new String[]{ContactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};
                String[] AddressParams = new String[]{ContactId, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
                String[] NoteParams = new String[]{ContactId, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};*/

    private void retrieveContactEmail() {

        String contactEmail = null;

        Cursor cursor = getContentResolver().query(uri, null,
                null, null, null);


        if (cursor.moveToFirst()) {
            contactEmail = cursor.getString(cursor.getColumnIndex
                    (ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE));

            edit_Email_ID.setText(contactEmail);
        }

        cursor.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
