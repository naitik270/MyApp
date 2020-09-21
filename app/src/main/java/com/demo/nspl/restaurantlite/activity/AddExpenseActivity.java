package com.demo.nspl.restaurantlite.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.nspl.restaurantlite.Adapter.TaxesAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsEmployee;
import com.demo.nspl.restaurantlite.classes.ClsExpenseMasterNew;
import com.demo.nspl.restaurantlite.classes.ClsExpenseType;
import com.demo.nspl.restaurantlite.classes.ClsTaxItem;
import com.demo.nspl.restaurantlite.classes.ClsTaxes;
import com.demo.nspl.restaurantlite.classes.ClsVendor;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class AddExpenseActivity extends AppCompatActivity {

    TextView txtdropdown_expense_type, expense_ID, expense_type_ID, entry_date, edit_trascation_date,
            txtdropdown_vendor, txtdropdown_emp, vendor_id;

    LinearLayout title_tax, ll_employee;
    static EditText edit_amount, edit_discount;
    EditText edit_bill_receipt_no, edit_vendor_name;
    EditText edit_remark;
    static TextView edit_grandtotal;
    Toolbar toolbar;
    private Dialog dialog;
    static String firstNumberAsString;
    StickyListHeadersListView stickyListview;
    static final int DATE_DIALOG_ID = 0;
    private int result;
    private ProgressDialog pd;
    ListView listView;
    static List<ClsTaxItem> listTaxUpdated = new ArrayList<>();
    private ClsExpenseType objexpenseType = new ClsExpenseType();
    private ClsVendor ObjVendor = new ClsVendor();
    private ClsEmployee ObjEmployee = new ClsEmployee();
    private List<ClsExpenseType> list_expense_type;
    private List<ClsVendor> list_vendor;
    private List<ClsVendor> listVendorSearch;
    private List<ClsEmployee> list_emp;
    private List<ClsTaxes> list_tax;
    private List<ClsTaxItem> lstClsTaxItems;
    private String formattedDate;
    int mYear, mMonth, mDay;
    private TaxesAdapter taxItemAdapter;
    private EmpAdapter empadapter;
    private VendorAdapter vendoradapter;
    String _VendorName;
    int _vendorID;
    Button btn_NEW;
    private ExpensetypeAdapter expenseadapter;
    private StickyListHeadersListView lst;
    private Dialog dialogvendor;
    private RelativeLayout lyout_nodata;
    TextView label_taxes;
    private String FlagValue;
    private ClsTaxItem objClsTaxItem;
    int ExpenseId;
    int ExpenseType_ID;
    int VendorId;
//    private ClsExpense objStock = new ClsExpense();

    ClsExpenseMasterNew objClsExpenseMasterNew = new ClsExpenseMasterNew();
    private int _ID;
    private String _TYPE = "";
    LinearLayout ll_vendor, ll_discount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "AddExpenseActivity"));
        }

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Add Expenses");
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtdropdown_expense_type = findViewById(R.id.txtdropdown_expense_type);
        txtdropdown_vendor = findViewById(R.id.txtdropdown_vendor);
        txtdropdown_emp = findViewById(R.id.txtdropdown_emp);
        expense_ID = findViewById(R.id.expense_ID);
        expense_type_ID = findViewById(R.id.expense_type_ID);
        entry_date = findViewById(R.id.entry_date);
        edit_trascation_date = findViewById(R.id.edit_trascation_date);
        edit_amount = findViewById(R.id.edit_amount);
        edit_bill_receipt_no = findViewById(R.id.edit_bill_receipt_no);
        listView = findViewById(R.id.listView);
        edit_remark = findViewById(R.id.edit_remark);
        title_tax = findViewById(R.id.title_tax);
        lyout_nodata = findViewById(R.id.lyout_nodata);
        label_taxes = findViewById(R.id.label_taxes);
        vendor_id = findViewById(R.id.vendor_id);
        btn_NEW = findViewById(R.id.btn_NEW);
        edit_discount = findViewById(R.id.edit_discount);
        edit_grandtotal = findViewById(R.id.edit_grandtotal);
        edit_vendor_name = findViewById(R.id.edit_vendor_name);
        ll_vendor = findViewById(R.id.ll_vendor);
        ll_discount = findViewById(R.id.ll_discount);
        ll_employee = findViewById(R.id.ll_employee);

        FlagValue = getIntent().getStringExtra("FLAG");
        Log.e("Flag", "AddExpense" + FlagValue);

        ExpenseId = getIntent().getIntExtra("ExpenseId", 0);
        _TYPE = getIntent().getStringExtra("_TYPE");
        Log.e("ExpID", "is--" + ExpenseId);


        _ID = getIntent().getIntExtra("_ID", 0);
        _TYPE = getIntent().getStringExtra("_TYPE");
        Log.e("_TYPE", "is--" + _TYPE);
        getDataInList();
        listTaxUpdated = lstClsTaxItems;


//            if (lstClsTaxItems != null && lstClsTaxItems.size() != 0) {
//                Log.e("Condition", "InIf" + lstClsTaxItems.size());
//                for (ClsTaxItem objClsTax : lstClsTaxItems) {
//                    if (objClsTax.getTaxName().equalsIgnoreCase(objClsExpenseMasterNew.getOther_tax1())) {
//                        Log.e("New", "Working..");
//                        objClsTax.set_TaxAmount(objClsExpenseMasterNew.getOther_val1());
//                    } else if (objClsTax.getTaxName().equalsIgnoreCase(objClsExpenseMasterNew.getOther_tax2())) {
//                        objClsTax.set_TaxAmount(objClsExpenseMasterNew.getOther_val2());
//                    } else if (objClsTax.getTaxName().equalsIgnoreCase(objClsExpenseMasterNew.getOther_tax3())) {
//                        objClsTax.set_TaxAmount(objClsExpenseMasterNew.getOther_val3());
//                    } else if (objClsTax.getTaxName().equalsIgnoreCase(objClsExpenseMasterNew.getOther_tax4())) {
//                        objClsTax.set_TaxAmount(objClsExpenseMasterNew.getOther_val4());
//                    } else if (objClsTax.getTaxName().equalsIgnoreCase(objClsExpenseMasterNew.getOther_tax5())) {
//                        objClsTax.set_TaxAmount(objClsExpenseMasterNew.getOther_val5());
//                    }
//                    lstClsTaxItems.set(lstClsTaxItems.indexOf(objClsTax), objClsTax);
//                }
//                listTaxUpdated = lstClsTaxItems;
//                taxItemAdapter.notifyDataSetChanged();
//    }


        //regionFindViewByID


//        listView.setAdapter(taxItemAdapter);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = df.format(c.getTime());
        edit_trascation_date.setText(formattedDate);

        //endregion
        getDataInList();


        DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dateFormatter.setLenient(false);
        Date today = new Date();
        final String entryDate = dateFormatter.format(today);
        Log.e("TIME", String.valueOf(entryDate));


        if (ExpenseId != 0) {
            Toast.makeText(this, "Edit", Toast.LENGTH_SHORT).show();
            Log.e("Expese", "IS--->" + ExpenseId);
            objClsExpenseMasterNew.setExpense_id(ExpenseId);
            objClsExpenseMasterNew = objClsExpenseMasterNew.getObject(ExpenseId);

            edit_trascation_date.setText(objClsExpenseMasterNew.getReceipt_date());
            txtdropdown_vendor.setText(objClsExpenseMasterNew.getVendor_name());
            txtdropdown_emp.setText(objClsExpenseMasterNew.getEmployee_name());
            edit_bill_receipt_no.setText(objClsExpenseMasterNew.getReceipt_no());
            txtdropdown_expense_type.setText(objClsExpenseMasterNew.getExpense_type_name());
            expense_type_ID.setText(String.valueOf(objClsExpenseMasterNew.getExpense_type_id()));
            vendor_id.setText(String.valueOf(objClsExpenseMasterNew.getVendor_id()));
            edit_grandtotal.setText(String.valueOf(objClsExpenseMasterNew.getGRAND_TOTAL()));
            Log.e("GrandTotal", "Val-->>" + objClsExpenseMasterNew.getGRAND_TOTAL());
            edit_remark.setText(objClsExpenseMasterNew.getRemark());
            edit_amount.setText(String.valueOf(objClsExpenseMasterNew.getAmount()));
            edit_discount.setText(String.valueOf(objClsExpenseMasterNew.getDiscount()));

            if (lstClsTaxItems != null && lstClsTaxItems.size() != 0) {
                for (ClsTaxItem objClsTax : lstClsTaxItems) {
                    if (objClsTax.getTaxName().equalsIgnoreCase(objClsExpenseMasterNew.getOther_tax1())) {
                        objClsTax.set_TaxAmount(objClsExpenseMasterNew.getOther_val1());
                    } else if (objClsTax.getTaxName().equalsIgnoreCase(objClsExpenseMasterNew.getOther_tax2())) {
                        objClsTax.set_TaxAmount(objClsExpenseMasterNew.getOther_val2());
                    } else if (objClsTax.getTaxName().equalsIgnoreCase(objClsExpenseMasterNew.getOther_tax3())) {
                        objClsTax.set_TaxAmount(objClsExpenseMasterNew.getOther_val3());
                    } else if (objClsTax.getTaxName().equalsIgnoreCase(objClsExpenseMasterNew.getOther_tax4())) {
                        objClsTax.set_TaxAmount(objClsExpenseMasterNew.getOther_val4());
                    } else if (objClsTax.getTaxName().equalsIgnoreCase(objClsExpenseMasterNew.getOther_tax5())) {
                        objClsTax.set_TaxAmount(objClsExpenseMasterNew.getOther_val5());
                    }
                    lstClsTaxItems.set(lstClsTaxItems.indexOf(objClsTax), objClsTax);
                }

            }

        }

        txtdropdown_vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VendorList();
            }
        });

        fillVendorList();


        if (_ID != 0) {
            fillValues();
        }

        btn_NEW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txtdropdown_expense_type.getText().toString().equalsIgnoreCase("Select Expense Type")) {
                    Toast.makeText(AddExpenseActivity.this, "Select Expense Type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (txtdropdown_expense_type.getText().toString().equalsIgnoreCase("SALARY")) {

                    if (txtdropdown_emp.getText().toString().equalsIgnoreCase("Select Employee")) {
                        Toast.makeText(AddExpenseActivity.this, "Select Employee", Toast.LENGTH_SHORT).show();
                        return;
                    }

                } else {
                    if (txtdropdown_vendor.getText().toString().equalsIgnoreCase("Select Vendor")) {
                        Toast.makeText(AddExpenseActivity.this, "Select Vendor", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (txtdropdown_vendor.getText().toString().equalsIgnoreCase("Other")) {
                    if (edit_vendor_name.getText() == null || edit_vendor_name.getText().toString().trim().isEmpty()) {
                        edit_vendor_name.requestFocus();
                        Toast.makeText(AddExpenseActivity.this, "Vendor name is required", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (edit_amount.getText() == null || edit_amount.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AddExpenseActivity.this, "Amount is required", Toast.LENGTH_SHORT).show();
                    edit_amount.requestFocus();
                    return;
                }

                Toast.makeText(AddExpenseActivity.this, "Vendror-->>" + objClsExpenseMasterNew.getVendor_id(), Toast.LENGTH_SHORT).show();
                objClsExpenseMasterNew = new ClsExpenseMasterNew(AddExpenseActivity.this);
                objClsExpenseMasterNew.setReceipt_date(edit_trascation_date.getText().toString().trim());
                objClsExpenseMasterNew.setExpense_id(ExpenseId);
                final String venId = vendor_id.getText().toString().trim();
                objClsExpenseMasterNew.setVendor_id(Integer.parseInt(venId));
                Log.e("VEndoRR", String.valueOf(venId));

                if ("update".equalsIgnoreCase(_TYPE))
//                if(_TYPE.equalsIgnoreCase("update") && _TYPE!= null)
                {
                    objClsExpenseMasterNew.setExpense_id(_ID);
                }
//                else if(_TYPE.equalsIgnoreCase("InventoryStock") && _TYPE!= null)
                else if ("InventoryStock".equalsIgnoreCase(_TYPE)) {

                    objClsExpenseMasterNew.setExpense_id(ExpenseId);
                }


                Log.e("SAVE", "IS" + ExpenseId);


                if (txtdropdown_vendor.getText().toString().equalsIgnoreCase("Other"))
                    objClsExpenseMasterNew.setVendor_name(edit_vendor_name.getText().toString());
                else {

                }
                objClsExpenseMasterNew.setVendor_name(txtdropdown_vendor.getText().toString().trim());
                if (txtdropdown_expense_type.getText().toString().equalsIgnoreCase("SALARY")) {
                    objClsExpenseMasterNew.setEmployee_name(txtdropdown_emp.getText().toString().trim());
                    objClsExpenseMasterNew.setVendor_name("");
                    objClsExpenseMasterNew.setVendor_id(0);
                } else {
                    objClsExpenseMasterNew.setEmployee_name("");
                }

                objClsExpenseMasterNew.setReceipt_no(edit_bill_receipt_no.getText().toString().trim());

                if (edit_amount.getText() != null && !edit_amount.getText().toString().isEmpty()) {
                    objClsExpenseMasterNew.setAmount(Double.valueOf(edit_amount.getText().toString().trim()));
                } else {
                    objClsExpenseMasterNew.setAmount(0.0);
                }

                if (edit_discount.getText() != null && !edit_discount.getText().toString().isEmpty()) {
                    objClsExpenseMasterNew.setDiscount(Double.valueOf(edit_discount.getText().toString().trim()));
                } else {
                    objClsExpenseMasterNew.setDiscount(0.0);
                }


                if (firstNumberAsString != null) {
                    objClsExpenseMasterNew.setGRAND_TOTAL(Double.valueOf(firstNumberAsString));
                    Log.e("FirstNum", "Not null");
                } else {
                    objClsExpenseMasterNew.setGRAND_TOTAL(0.0);
                    Log.e("FirstNum", "null");
                }


                objClsExpenseMasterNew.setRemark(edit_remark.getText().toString().trim());
                objClsExpenseMasterNew.setEntry_date(formattedDate);
                String exptype = expense_type_ID.getText().toString();
                objClsExpenseMasterNew.setExpense_type_id(Integer.parseInt(exptype));

                if (listTaxUpdated != null && listTaxUpdated.size() != 0) {
                    for (ClsTaxItem _obj : listTaxUpdated) {
                        if (listTaxUpdated.indexOf(_obj) == 0) {
                            objClsExpenseMasterNew.setOther_tax1(_obj.getTaxName());
                            objClsExpenseMasterNew.setOther_val1(Double.valueOf(_obj.get_TaxAmount()));
                        } else if (listTaxUpdated.indexOf(_obj) == 1) {
                            objClsExpenseMasterNew.setOther_tax2(_obj.getTaxName());
                            objClsExpenseMasterNew.setOther_val2(Double.valueOf(_obj.get_TaxAmount()));
                        } else if (listTaxUpdated.indexOf(_obj) == 2) {
                            objClsExpenseMasterNew.setOther_tax3(_obj.getTaxName());
                            objClsExpenseMasterNew.setOther_val3(Double.valueOf(_obj.get_TaxAmount()));
                        } else if (listTaxUpdated.indexOf(_obj) == 3) {
                            objClsExpenseMasterNew.setOther_tax4(_obj.getTaxName());
                            objClsExpenseMasterNew.setOther_val4(Double.valueOf(_obj.get_TaxAmount()));
                        } else if (listTaxUpdated.indexOf(_obj) == 4) {
                            objClsExpenseMasterNew.setOther_tax5(_obj.getTaxName());
                            objClsExpenseMasterNew.setOther_val5(Double.valueOf(_obj.get_TaxAmount()));
                        }
                    }
                }

                if (objClsExpenseMasterNew.getExpense_id() != 0) {
                    Toast.makeText(AddExpenseActivity.this, "Update", Toast.LENGTH_SHORT).show();
                    result = ClsExpenseMasterNew.Update(objClsExpenseMasterNew);

                    if (result == 1) {
                        finish();
                    } else {
                        Toast.makeText(AddExpenseActivity.this, "Error in update..", Toast.LENGTH_SHORT).show();
                    }

                    Log.e("Update---  ", String.valueOf(result));

                } else {
                    Toast.makeText(AddExpenseActivity.this, "Insert", Toast.LENGTH_SHORT).show();
                    final ClsExpenseMasterNew ObjResult = ClsExpenseMasterNew.Insert(objClsExpenseMasterNew);

                    if (String.valueOf(ObjResult.getExpense_id()).equalsIgnoreCase("-1")) {

                        Toast.makeText(AddExpenseActivity.this, "Technical Error", Toast.LENGTH_LONG).show();

                    } else if (!String.valueOf(ObjResult.getExpense_id()).equals("0")) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(AddExpenseActivity.this);
                        final AlertDialog OptionDialog = builder.create();
                        builder.setMessage("Record added successfully");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                int VendoR = VendorId;
                                Log.e("Ven", "ID--->>>" + VendoR);

//

                                if (txtdropdown_expense_type.getText().toString().equalsIgnoreCase("SALARY")) {
                                    finish();
                                } else if (!txtdropdown_expense_type.getText().toString().equalsIgnoreCase("SALARY") &&
                                        FlagValue != null && FlagValue.equalsIgnoreCase("InventoryStock")) {

                                    Toast.makeText(AddExpenseActivity.this, "Salary..ElseIffffffff", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AddExpenseActivity.this, AddInventoryStockActivity.class);
                                    intent.putExtra("TYPE", "IN");
                                    intent.putExtra("SOURCE", "BILL ENTRY");
                                    intent.putExtra("VendorId", venId);
                                    intent.putExtra("Amount", edit_amount.getText().toString());
                                    intent.putExtra("VendorName", txtdropdown_vendor.getText().toString());
                                    intent.putExtra("OrderId", ObjResult.getExpense_id());
                                    intent.putExtra("Date", edit_trascation_date.getText().toString());
                                    startActivity(intent);
                                    OptionDialog.dismiss();
                                    OptionDialog.cancel();
                                    finish();
                                    Log.e("Flags", "BillACtivity");

                                } else {
                                    finish();
                                    Log.e("Flags", "ExpenseActivity");
                                    OptionDialog.dismiss();
                                    OptionDialog.cancel();
                                }

                            }
                        });
                        builder.show();
                    }
                    Log.e("INSERT---  ", String.valueOf(result));
                }
            }
        });


        edit_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable txtVal) {
                CalculateAmount();
            }
        });
        setListViewHeightBasedOnChildren(listView);

        edit_discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                CalculateAmount();
            }
        });
        BeforeDate();

        txtdropdown_expense_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_expense_type = new ArrayList<>();
                list_expense_type = new ClsExpenseType(AddExpenseActivity.this).getList(" AND [ACTIVE]='YES' ORDER BY [EXPENSE_TYPE_NAME]");

                if (list_expense_type.size() != 0) {
                    dialog = new Dialog(AddExpenseActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.sticky_header_popup);
                    dialog.setCancelable(true);

                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    dialog.getWindow().setAttributes(lp);

                    final ClsExpenseType Obj = new ClsExpenseType(AddExpenseActivity.this);
                    stickyListview = (StickyListHeadersListView) dialog.findViewById(R.id.stickyListview);


                    expenseadapter = new ExpensetypeAdapter(AddExpenseActivity.this, list_expense_type);
                    stickyListview.setAdapter(expenseadapter);
                    dialog.show();
                } else {
                    Toast.makeText(getApplication(), "NO DATA FOUND", Toast.LENGTH_SHORT).show();
                }

            }
        });


      /*  txtdropdown_emp.setOnClickListener(view -> {
            dialog = new Dialog(AddExpenseActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.sticky_header_popup);
            dialog.setCancelable(true);

            final ClsEmployee Obj = new ClsEmployee(AddExpenseActivity.this);
            stickyListview = (StickyListHeadersListView) dialog.findViewById(R.id.stickyListview);

            list_emp = new ArrayList<>();
            list_emp = new ClsEmployee(AddExpenseActivity.this).getList("AND [ACTIVE] = 'YES'");

            empadapter = new EmpAdapter(AddExpenseActivity.this, list_emp);
            stickyListview.setAdapter(empadapter);
            dialog.show();
        });
*/


        txtdropdown_emp.setOnClickListener(view -> {
            dialog = new Dialog(AddExpenseActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.sticky_header_popup);
            dialog.setCancelable(true);


            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(lp);

            final ClsEmployee Obj = new ClsEmployee(AddExpenseActivity.this);
            stickyListview = dialog.findViewById(R.id.stickyListview);

            list_emp = new ArrayList<>();
            list_emp = new ClsEmployee(AddExpenseActivity.this).getList("AND [ACTIVE] = 'YES'");

            if (list_emp != null && list_emp.size() != 0) {

                empadapter = new EmpAdapter(AddExpenseActivity.this, list_emp);
                stickyListview.setAdapter(empadapter);
                dialog.show();
            } else {
                Toast.makeText(this, "No Employee Found.", Toast.LENGTH_LONG).show();
            }


        });


    }


    void fillValues() {

        ClsExpenseMasterNew objClsExpenseMasterNew = new ClsExpenseMasterNew(AddExpenseActivity.this);
        objClsExpenseMasterNew.setExpense_id(_ID);
        objClsExpenseMasterNew = objClsExpenseMasterNew.getObject(_ID);
        edit_trascation_date.setText(objClsExpenseMasterNew.getReceipt_date());
        txtdropdown_vendor.setText(objClsExpenseMasterNew.getVendor_name());
        txtdropdown_emp.setText(objClsExpenseMasterNew.getEmployee_name());
        edit_bill_receipt_no.setText(objClsExpenseMasterNew.getReceipt_no());
        txtdropdown_expense_type.setText(objClsExpenseMasterNew.getExpense_type_name());
        expense_type_ID.setText(String.valueOf(objClsExpenseMasterNew.getExpense_type_id()));
        vendor_id.setText(String.valueOf(objClsExpenseMasterNew.getVendor_id()));
        edit_grandtotal.setText(String.valueOf(objClsExpenseMasterNew.getGRAND_TOTAL()));
        edit_remark.setText(objClsExpenseMasterNew.getRemark());
        edit_amount.setText(String.valueOf(objClsExpenseMasterNew.getAmount()));
        edit_discount.setText(String.valueOf(objClsExpenseMasterNew.getDiscount()));

        if (lstClsTaxItems != null && lstClsTaxItems.size() != 0) {
            for (ClsTaxItem objClsTax : lstClsTaxItems) {
                if (objClsTax.getTaxName().equalsIgnoreCase(objClsExpenseMasterNew.getOther_tax1())) {
                    objClsTax.set_TaxAmount(objClsExpenseMasterNew.getOther_val1());
                    Log.d("_TAX_", "TaxValue-- " + objClsExpenseMasterNew.getOther_val1());
                } else if (objClsTax.getTaxName().equalsIgnoreCase(objClsExpenseMasterNew.getOther_tax2())) {
                    objClsTax.set_TaxAmount(objClsExpenseMasterNew.getOther_val2());
                } else if (objClsTax.getTaxName().equalsIgnoreCase(objClsExpenseMasterNew.getOther_tax3())) {
                    objClsTax.set_TaxAmount(objClsExpenseMasterNew.getOther_val3());
                } else if (objClsTax.getTaxName().equalsIgnoreCase(objClsExpenseMasterNew.getOther_tax4())) {
                    objClsTax.set_TaxAmount(objClsExpenseMasterNew.getOther_val4());
                } else if (objClsTax.getTaxName().equalsIgnoreCase(objClsExpenseMasterNew.getOther_tax5())) {
                    objClsTax.set_TaxAmount(objClsExpenseMasterNew.getOther_val5());
                }
                lstClsTaxItems.set(lstClsTaxItems.indexOf(objClsTax), objClsTax);
            }
            listTaxUpdated = lstClsTaxItems;
            taxItemAdapter.notifyDataSetChanged();
        }

    }

    private void VendorList() {
        final EditText edit_search;
        dialogvendor = new Dialog(AddExpenseActivity.this);
        dialogvendor.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogvendor.setContentView(R.layout.dialog_type_n_search);
        dialogvendor.setTitle("Select Vendor");
        dialogvendor.setCanceledOnTouchOutside(true);
        dialogvendor.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogvendor.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogvendor.getWindow().setAttributes(lp);

        lst = dialogvendor.findViewById(R.id.list);
        lyout_nodata = (RelativeLayout) dialogvendor.findViewById(R.id.lyout_nodata);
        edit_search = dialogvendor.findViewById(R.id.edit_search);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                try {

                    if (list_vendor != null && list_vendor.size() != 0) {

                        vendoradapter = new VendorAdapter(AddExpenseActivity.this, list_vendor);
                        lst.setAdapter(vendoradapter);


                        edit_search.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {


                            }

                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                if (s != null && !s.toString().isEmpty() && s.toString() != "") {
                                    filter(s.toString());
                                }
                            }
                        });

                        dialogvendor.show();

                    } else {
                        Toast.makeText(AddExpenseActivity.this, "Please add vendors.", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }


            }
        }, 400);


    }


    void filter(String text) {
        if (!text.isEmpty()) {

            List<ClsVendor> temp = new ArrayList();
            for (ClsVendor objvClsVendorMaster : list_vendor) {
                if (objvClsVendorMaster.getVendor_name().toLowerCase().contains(text.toLowerCase())) {
                    temp.add(objvClsVendorMaster);
                }
            }

            list_vendor = temp;

        } else {
            list_vendor = listVendorSearch;
        }

        vendoradapter = new VendorAdapter(AddExpenseActivity.this, list_vendor);
        lst.setAdapter(vendoradapter);

        if (list_vendor != null && list_vendor.size() != 0) {
            lyout_nodata.setVisibility(View.GONE);
        } else {
            lyout_nodata.setVisibility(View.VISIBLE);
        }
    }

    private void BeforeDate() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        edit_trascation_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(AddExpenseActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                c.set(year, month, day);
                                String date = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
                                edit_trascation_date.setText(date);
                                mYear = c.get(Calendar.YEAR);
                                mMonth = c.get(Calendar.MONTH);
                                mDay = c.get(Calendar.DAY_OF_MONTH);
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
                Calendar d = Calendar.getInstance();
                dpd.updateDate(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }

        });

    }


    class ExpensetypeAdapter extends BaseAdapter implements StickyListHeadersAdapter {

        Context context;

        List<ClsExpenseType> list_expense_type = new ArrayList<>();
        private LayoutInflater inflater;

        public ExpensetypeAdapter(Context context, List<ClsExpenseType> list) {
            this.list_expense_type = list;
            inflater = LayoutInflater.from(context);
        }


        @Override
        public long getHeaderId(int position) {
            return list_expense_type.get(position).getExpense_type_name().subSequence(0, 1).charAt(0);
        }

        @Override
        public int getCount() {
            return list_expense_type.size();
        }

        @Override
        public Object getItem(int position) {
            return list_expense_type.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.simple_spinner_item, parent, false);
                holder.text = (TextView) convertView.findViewById(R.id.pagename);
                holder.pageid = (TextView) convertView.findViewById(R.id.pageid);
                holder.pagelink = (TextView) convertView.findViewById(R.id.pagelink);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

//            ClsExpenseType objInventoryItem= list_expense_type.get(position);

            ClsExpenseType objexp = list_expense_type.get(position);
            stickyListview.setOnItemClickListener((parent1, view, position1, id) -> {
                int Popid = (int) id;
                txtdropdown_expense_type.setText(list_expense_type.get(position1).getExpense_type_name());
                expense_type_ID.setText(String.valueOf(list_expense_type.get(position1).getExpense_type_id()));

                if (list_expense_type.get(position1).getExpense_type_name().equalsIgnoreCase("Salary")) {
                    ll_employee.setVisibility(View.VISIBLE);
                    ll_vendor.setVisibility(View.GONE);
                    edit_bill_receipt_no.setVisibility(View.GONE);
                    ll_discount.setVisibility(View.GONE);
                    title_tax.setVisibility(View.GONE);
                    edit_grandtotal.setVisibility(View.GONE);
                    vendor_id.setText(String.valueOf(0));

                    dialog.dismiss();
                } else {
                    ll_employee.setVisibility(View.GONE);
                    ll_vendor.setVisibility(View.VISIBLE);
                    edit_bill_receipt_no.setVisibility(View.VISIBLE);
                    edit_amount.setText("");
                    ll_discount.setVisibility(View.GONE);
                    title_tax.setVisibility(View.VISIBLE);
                    edit_grandtotal.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }
            });
            holder.text.setText(objexp.getExpense_type_name());
            return convertView;
        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            HeaderViewHolder holder;
            if (convertView == null) {
                holder = new HeaderViewHolder();
                convertView = inflater.inflate(R.layout.header_row, parent, false);
                holder.text = (TextView) convertView.findViewById(R.id.rowName);

                convertView.setTag(holder);
            } else {
                holder = (HeaderViewHolder) convertView.getTag();
            }

            ClsExpenseType clsExpenseType = list_expense_type.get(position);
            String headerText = "" + clsExpenseType.getExpense_type_name().subSequence(0, 1).charAt(0);
            holder.text.setText(headerText);
            return convertView;
        }


        class ViewHolder {
            TextView text, pageid, pagelink;
        }

        class HeaderViewHolder {
            TextView text;
        }

    }

    class VendorAdapter extends BaseAdapter implements StickyListHeadersAdapter {

        Context context;

        List<ClsVendor> list_vendor = new ArrayList<>();
        private LayoutInflater inflater;

        public VendorAdapter(Context context, List<ClsVendor> list) {
            this.list_vendor = list;
            inflater = LayoutInflater.from(context);
        }


        @Override
        public long getHeaderId(int position) {
            return list_vendor.get(position).getVendor_name().subSequence(0, 1).charAt(0);
        }

        @Override
        public int getCount() {
            return list_vendor.size();
        }

        @Override
        public Object getItem(int position) {
            return list_vendor.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.simple_spinner_item, parent, false);
                holder.text = (TextView) convertView.findViewById(R.id.pagename);
                holder.pageid = (TextView) convertView.findViewById(R.id.pageid);
                holder.pagelink = (TextView) convertView.findViewById(R.id.pagelink);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ClsVendor objVendor = list_vendor.get(position);


            lst.setOnItemClickListener((parent1, view, position1, id) -> {
                int Popid = (int) id;
                txtdropdown_vendor.setText(list_vendor.get(position1).getVendor_name());
                vendor_id.setText(String.valueOf(list_vendor.get(position1).getVendor_id()));

                if (list_vendor.get(position1).getVendor_name().equalsIgnoreCase("Other")) {
                    edit_vendor_name.setVisibility(View.VISIBLE);
                } else {
                    edit_vendor_name.setVisibility(View.GONE);
                }
                ObjVendor = list_vendor.get(position1);
                dialogvendor.dismiss();

            });
            holder.text.setText(objVendor.getVendor_name());
            return convertView;

        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            HeaderViewHolder holder;
            if (convertView == null) {
                holder = new HeaderViewHolder();
                convertView = inflater.inflate(R.layout.header_row, parent, false);
                holder.text = (TextView) convertView.findViewById(R.id.rowName);

                convertView.setTag(holder);
            } else {
                holder = (HeaderViewHolder) convertView.getTag();
            }

            ClsVendor obj = list_vendor.get(position);
            String headerText = "" + obj.getVendor_name().subSequence(0, 1).charAt(0);
            holder.text.setText(headerText);
            return convertView;
        }


        class ViewHolder {
            TextView text, pageid, pagelink;
        }

        class HeaderViewHolder {
            TextView text;
        }

    }


    class EmpAdapter extends BaseAdapter implements StickyListHeadersAdapter {

        Context context;

        List<ClsEmployee> list_Emp;
        private LayoutInflater inflater;
        TextView text, pageid, pagelink;

        public EmpAdapter(Context context, List<ClsEmployee> list) {
            this.list_Emp = list;
            inflater = LayoutInflater.from(context);
        }


        @Override
        public long getHeaderId(int position) {
            return list_Emp.get(position).getEmployee_name().subSequence(0, 1).charAt(0);
        }

        @Override
        public int getCount() {
            return list_Emp.size();
        }

        @Override
        public Object getItem(int position) {
            return list_Emp.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            if (convertView == null) {

                convertView = inflater.inflate(R.layout.simple_spinner_item, parent, false);
                text = (TextView) convertView.findViewById(R.id.pagename);
                pageid = (TextView) convertView.findViewById(R.id.pageid);
                pagelink = (TextView) convertView.findViewById(R.id.pagelink);

            } else {

            }

            final ClsEmployee objEmp = list_Emp.get(position);

            stickyListview.setOnItemClickListener((parent1, view, position1, id) -> {
                int Popid = (int) id;
                txtdropdown_emp.setText(list_Emp.get(position1).getEmployee_name());
                ObjEmployee = list_Emp.get(position1);
                if (ObjEmployee.getSalary() > 0) {
                    edit_amount.setText(String.valueOf(ObjEmployee.getSalary()));
                } else
                    edit_amount.setText("");
                edit_discount.setText("");

                dialog.dismiss();

            });
            text.setText(objEmp.getEmployee_name());


            return convertView;

        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            HeaderViewHolder holder;
            if (convertView == null) {
                holder = new HeaderViewHolder();
                convertView = inflater.inflate(R.layout.header_row, parent, false);
                holder.text = (TextView) convertView.findViewById(R.id.rowName);

                convertView.setTag(holder);
            } else {
                holder = (HeaderViewHolder) convertView.getTag();
            }

            ClsEmployee obj = list_Emp.get(position);
            String headerText = "" + obj.getEmployee_name().subSequence(0, 1).charAt(0);
            holder.text.setText(headerText);
            return convertView;
        }


        class HeaderViewHolder {
            TextView text;
        }

    }

    static void CalculateAmount() {

        double grnadTotal = 0.0, _amount = 0.0;

        if (edit_amount.getText() != null && edit_amount.getText().toString() != ""
                && !edit_amount.getText().toString().isEmpty()
                && !edit_amount.getText().toString().equalsIgnoreCase(".")) {
            _amount = Double.valueOf(edit_amount.getText().toString());
        }
        grnadTotal = _amount;

        if (listTaxUpdated != null && listTaxUpdated.size() != 0) {

            for (ClsTaxItem _obj : listTaxUpdated) {
                grnadTotal = grnadTotal + Double.valueOf(_obj.get_TaxAmount());
            }
            //FOR DICOUNT
        }
        double discount = 0.0;
        if (edit_discount.getText() != null && !edit_discount.getText().toString().isEmpty()
                && !edit_discount.getText().toString().equalsIgnoreCase(".")) {
            discount = Double.valueOf(edit_discount.getText().toString());
        }

        grnadTotal = grnadTotal - discount;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        firstNumberAsString = ClsGlobal.round(grnadTotal, 2);


        edit_grandtotal.setText("".concat("Total: \u20B9 ").concat(firstNumberAsString));

    }


    private void getDataInList() {
        list_tax = new ArrayList<>();
        lstClsTaxItems = new ArrayList<>();
        list_tax = new ClsTaxes(getApplicationContext()).getList("");

        if (list_tax != null && list_tax.size() != 0) {
            title_tax.setVisibility(View.VISIBLE);
            label_taxes.setVisibility(View.VISIBLE);

            taxItemAdapter = new TaxesAdapter(this, lstClsTaxItems);
            listView.setAdapter(taxItemAdapter);
            taxItemAdapter.notifyDataSetChanged();

            for (ClsTaxes objClsTaxes : list_tax) {
                if (!objClsTaxes.getTax_type().equalsIgnoreCase("main")) {
                    objClsTaxItem = new ClsTaxItem();
                    objClsTaxItem.set_TaxAmount(0.0);
                    objClsTaxItem.setType(objClsTaxes.getTax_type());
                    objClsTaxItem.setTaxName(objClsTaxes.getTax_name());
                    lstClsTaxItems.add(objClsTaxItem);
                }
            }
        } else {
            title_tax.setVisibility(View.GONE);
            label_taxes.setVisibility(View.GONE);
        }
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static void setTaxValueForSave(List<ClsTaxItem> taxListUpdated) {
        listTaxUpdated = taxListUpdated;
        CalculateAmount();
    }

    private void fillVendorList() {
        list_vendor = new ArrayList<>();
        listVendorSearch = list_vendor = new ClsVendor(getApplicationContext()).getList(" AND [ACTIVE] = 'YES' ORDER BY [VENDOR_NAME]");
//        listVendorSearch = list_vendor = new ClsVendor(getApplicationContext()).getList(" AND [ACTIVE] = 'YES' AND [Type] IN ('EXPENSE','VENDOR','BOTH') ORDER BY [VENDOR_NAME]");

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
