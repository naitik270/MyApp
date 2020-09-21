package com.demo.nspl.restaurantlite.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;
import com.demo.nspl.restaurantlite.classes.ClsVendor;


public class AddVendorActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText edit_vendor_name, edit_contact_no, edit_address, edit_gst_no, edit_sort_no, edit_remark;
    RadioGroup rg, rg_type;
    RadioButton rbYES, rbNO, rbBoth, rb_supplier, rb_expense_vendor;
    Button btnSave;
    static final int DATE_DIALOG_ID = 0;
    private int result;
    TextView vendor_ID;
    String getSelectedVendorType = "";
    int _ID;

    EditText edit_opening_bal;

    RadioButton rb_to_pay, rb_to_collect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vendor);

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        rb_to_pay = findViewById(R.id.rb_to_pay);
        rb_to_collect = findViewById(R.id.rb_to_collect);
        edit_opening_bal = findViewById(R.id.edit_opening_bal);
        edit_vendor_name = findViewById(R.id.edit_vendor_name);
        edit_contact_no = findViewById(R.id.edit_contact_no);
        edit_address = findViewById(R.id.edit_address);
        edit_gst_no = findViewById(R.id.edit_gst_no);
        edit_sort_no = findViewById(R.id.edit_sort_no);
        edit_remark = findViewById(R.id.edit_remark);
        vendor_ID = findViewById(R.id.vendor_ID);
        rg = findViewById(R.id.rg);
        rbYES = findViewById(R.id.rbYES);
        rbNO = findViewById(R.id.rbNO);
        rg_type = findViewById(R.id.rg_type);

        rbBoth = findViewById(R.id.rbBoth);
        rb_supplier = findViewById(R.id.rb_supplier);
        rb_expense_vendor = findViewById(R.id.rb_expense_vendor);
        btnSave = findViewById(R.id.btnSave);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "AddVendorActivity"));
        }
        _ID = getIntent().getIntExtra("ID", 0);

        Log.e("TEST", String.valueOf(_ID));

        if (_ID != 0) {

            ClsVendor objVendor = new ClsVendor(AddVendorActivity.this);
//            objVendor.setVendor_id(_ID);
            objVendor = objVendor.getObject(_ID);

            vendor_ID.setText(String.valueOf(objVendor.getVendor_id()));
            edit_vendor_name.setText(objVendor.getVendor_name());
            edit_contact_no.setText(objVendor.getContact_no());
            edit_address.setText(objVendor.getAddress());
            edit_gst_no.setText(objVendor.getGst_no());
            edit_remark.setText(objVendor.getRemark());
            edit_opening_bal.setText(String.valueOf(objVendor.getOpeningStock()));
            edit_sort_no.setText("0");

            if (objVendor.getActive().equalsIgnoreCase("NO")) {
                rbNO.setChecked(true);
            } else if (objVendor.getActive().equalsIgnoreCase("YES")) {
                rbYES.setChecked(true);
            }

            if (objVendor.getBalanceType() != null) {
                if (objVendor.getBalanceType().equalsIgnoreCase("TO PAY")) {
                    rb_to_pay.setChecked(true);
                } else if (objVendor.getBalanceType().equalsIgnoreCase("TO COLLECT")) {
                    rb_to_collect.setChecked(true);
                }
            }

            String getType = objVendor.getTYPE();

            switch (getType) {

                case "BOTH":
                    rbBoth.setChecked(true);
                    break;

                case "SUPPLIER":
                    rb_supplier.setChecked(true);
                    break;

                case "EXPENSE VENDOR":
                    rb_expense_vendor.setChecked(true);
                    break;

            }


        } else {
            Log.e("branchID", "Else" + _ID);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_DIALOG_ID);


                if (validation()) {

                    String where = "";
                    String whereWithGST = "";
                    //check contact is not empty && gst is not empty

                    //else existing qry
                    boolean withGST = false;
                    where = where + " AND [CONTACT_NO] = "
                            .concat("'")
                            .concat(edit_contact_no.getText().toString().trim())
                            .concat("'");

                    whereWithGST = whereWithGST.concat(" AND ( [CONTACT_NO] = "
                            .concat("'")
                            .concat(edit_gst_no.getText().toString().trim())
                            .concat("'"));

                    if (edit_gst_no.getText() != null && !edit_gst_no.getText().toString().trim().isEmpty()) {
                        whereWithGST = whereWithGST.concat(" OR [GST_NO] = ")
                                .concat("'")
                                .concat(edit_gst_no.getText().toString().trim())
                                .concat("')");

                        withGST = true;
                    }

                    if (_ID != 0) {
                        where = where.concat(" AND [VENDOR_ID] <> ").concat(String.valueOf(_ID));
                        whereWithGST = whereWithGST.concat(" AND [VENDOR_ID] <> ").concat(String.valueOf(_ID));
                    }

                    if (rbBoth.isChecked()) {
                        getSelectedVendorType = rbBoth.getText().toString().trim();
                    } else if (rb_supplier.isChecked()) {
                        getSelectedVendorType = rb_supplier.getText().toString().trim();
                    } else if (rb_expense_vendor.isChecked()) {
                        getSelectedVendorType = rb_expense_vendor.getText().toString().trim();
                    }

                    ClsVendor Obj = new ClsVendor(AddVendorActivity.this);
                    boolean exists = Obj.checkExists(withGST ? whereWithGST : where);

                    if (!exists) {
                        Obj.setVendor_id(vendor_ID.getText() == null || vendor_ID.getText().toString().isEmpty() ? 0 : Integer.parseInt(vendor_ID.getText().toString().trim()));
                        Obj.setVendor_name(edit_vendor_name.getText().toString().trim());
                        Obj.setContact_no(edit_contact_no.getText().toString().trim());
                        Obj.setAddress(edit_address.getText().toString().trim());
                        Obj.setGst_no(edit_gst_no.getText().toString().trim());
                        Obj.setTYPE(getSelectedVendorType);
                        // Obj.setSort_no(edit_sort_no.getText() == null || edit_sort_no.getText().toString().isEmpty() ? null : Integer.parseInt(edit_sort_no.getText().toString()));
                        Obj.setSort_no(0);
                        Obj.setActive(rbYES.isChecked() ? "YES" : "NO");

                        Obj.setBalanceType(rb_to_pay.isChecked() ? "TO PAY" : "TO COLLECT");


                        if (edit_opening_bal.getText() != null && !edit_opening_bal.getText().toString().isEmpty()
                                && !edit_opening_bal.getText().toString().equalsIgnoreCase(".")) {


                            Obj.setOpeningStock(Double.valueOf(edit_opening_bal.getText().toString().trim()));

                            Obj.setOpeningStock(Double.valueOf(edit_opening_bal.getText().toString().trim()));
                        } else {
                            Obj.setOpeningStock(0.0);
                        }

                        Obj.setRemark(edit_remark.getText().toString().trim());


                        //-----------------------------Insert into PaymentMaster Table-----------------------------------//

                        ClsPaymentMaster insertPaymentMaster = new ClsPaymentMaster();
                        insertPaymentMaster.setPaymentDate(ClsGlobal.getCurruntDateTime());
                        insertPaymentMaster.setPaymentMounth(ClsGlobal.getDayMonth(ClsGlobal.getEntryDate_dd_MM_yyyy()));

//                        insertPaymentMaster.setVendorID(Integer.parseInt(vendor_ID.getText().toString()));
                        insertPaymentMaster.setVendorID(Obj.getVendor_id());

                        insertPaymentMaster.setMobileNo(edit_contact_no.getText().toString().equalsIgnoreCase("")
                                ? "" : edit_contact_no.getText().toString().trim());
                        insertPaymentMaster.setCustomerName("");
                        insertPaymentMaster.setVendorName(edit_vendor_name.getText().toString().equalsIgnoreCase("")
                                ? "" : edit_vendor_name.getText().toString().trim());
                        insertPaymentMaster.setPaymentMode("Opening Balance");
                        insertPaymentMaster.setPaymentDetail("Opening Balance");
                        insertPaymentMaster.setInvoiceNo("0");

                        Double openingBal = Double.valueOf(edit_opening_bal.getText().toString().trim().equalsIgnoreCase("")
                                ? "0.00" : edit_opening_bal.getText().toString().trim());

                        if (edit_opening_bal.getText() != null && !edit_opening_bal.getText().toString().isEmpty()
                                && !edit_opening_bal.getText().toString().equalsIgnoreCase(".")) {
                            Obj.setOpeningStock(Double.valueOf(edit_opening_bal.getText().toString().trim()));

                            if (!rb_to_pay.isChecked()) {
                                insertPaymentMaster.setAmount(openingBal);
                            } else {
                                insertPaymentMaster.setAmount(openingBal * -1);
                            }

                        } else {
                            insertPaymentMaster.setAmount(0.0);
                        }

                        if (rb_to_pay.isChecked()) {
                            insertPaymentMaster.setRemark("Vendor opening balance - TO PAY");
                        } else {
                            insertPaymentMaster.setRemark("Vendor opening balance - TO COLLECT");
                        }

                        insertPaymentMaster.setEntryDate(ClsGlobal.getEntryDate());
                        insertPaymentMaster.setType("Vendor");
                        insertPaymentMaster.setReceiptNo("0");
//                        insertPaymentMaster.setOrderId(0);

                        if (Obj.getVendor_id() != 0) {
                            result = ClsVendor.Update(Obj);
                            if (result == 1) {
                                ClsPaymentMaster.deleteOpeningBalanceVendor(String.valueOf(Obj.getVendor_id()),
                                        AddVendorActivity.this);

                                ClsPaymentMaster.InsertPaymentVendor(insertPaymentMaster, AddVendorActivity.this);
                                finish();
                            }

                        } else {
                            result = ClsVendor.InsertWithPayment(Obj, insertPaymentMaster);
                            Log.e("Result", String.valueOf(result));
                            if (result == 1) {
                                finish();
                            }
                        }

                    } else {
                        Toast toast = Toast.makeText(AddVendorActivity.this, "Vendor already exists....", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                        return;
                    }

                }

            }

        });
    }


    private boolean validation() {
        boolean result = true;
        if (edit_vendor_name.getText() == null ||
                edit_vendor_name.getText().toString().isEmpty()) {
            edit_vendor_name.setError("Vendor name is required");
            // edit_vendor_name.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            edit_vendor_name.requestFocus();
            return false;
        } else {
            edit_vendor_name.setError(null);
        }

        if (!edit_gst_no.getText().toString().isEmpty()) {
            if (edit_gst_no.getText().length() < 15) {  //   14<15
                edit_gst_no.setError("GST NO Should be 15 characters");
                //    edit_gst_no.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                edit_gst_no.requestFocus();
                return false;
            } else {
                edit_gst_no.setError(null);
            }
        }

        if (edit_contact_no.getText() == null ||
                edit_contact_no.getText().toString().isEmpty()) {
            edit_contact_no.setError("Contact no. is required");
            //  edit_contact_no.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            edit_contact_no.requestFocus();
            return false;
        } else if (edit_contact_no.getText().toString().length() < 10) {
            edit_contact_no.setError("Invalid contact no.");
            //  edit_contact_no.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            return false;
        } else {
            edit_contact_no.setError(null);
        }
        return result;
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
