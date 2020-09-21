package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.CustomerAdapter;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.ImageEdit.ActivityImageEdit;
import com.demo.nspl.restaurantlite.ImageEdit.PreviewSmallImgBlockAdapter;
import com.demo.nspl.restaurantlite.MultipleImage.ImageController;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsCustomerMaster;
import com.demo.nspl.restaurantlite.classes.ClsImages;
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;
import com.demo.nspl.restaurantlite.classes.ClsUserInfo;
import com.demo.nspl.restaurantlite.classes.ClsVendor;
import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.payment_Imgs_uri;


public class AddPaymentActivity extends AppCompatActivity {

    private Button btn_Save, Btn_Save_whatsapp, Btn_Save_sms;
    private TextView edit_select_date, txtdropdown_vendor,
            txt_vendor_id, txt_customer_number, txt_customer_name_new;
    private RadioGroup radio_group;
    private RadioButton rbCASE, rbCARD, rbOTHER;
    private EditText edit_payment_detail, edit_invoice_no, edit_Amount, edit_remark;
    int mYear, mMonth, mDay;
    private Dialog dialogvendor;
    private StickyListHeadersListView lst;
    private RelativeLayout lyout_nodata;
    private List<ClsVendor> list_vendor;
    private VendorAdapter vendoradapter;
    private CustomerAdapter customerAdapter;
    private ClsVendor ObjVendor = new ClsVendor();
    private List<ClsVendor> listVendorSearch;
    private List<ClsCustomerMaster> list_customer;
    int ReceiptNo = 0;
    String SelectedPaymentMode = "", type = "";
    Toolbar toolbar;
    int getNewnumberReceiptNo = 0;
    RadioGroup rg_payment_group;

    private static SharedPreferences mPreferences;
    private static final String mPreferncesName = "MyPerfernces";

    RadioButton rb_received, rb_refund;
    ImageView iv_add_img;

    RecyclerView rv_img_block;
    PreviewSmallImgBlockAdapter imgAdapter;
    LinearLayoutManager linearLayoutManager;
    String _paymentType = "";
    int _paymentID = 0;
    ClsPaymentMaster ClsPaymentMaster;
    ImageController imageController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_Save = findViewById(R.id.Btn_Save);

        rg_payment_group = findViewById(R.id.rg_payment_group);
        rb_received = findViewById(R.id.rb_received);
        rb_refund = findViewById(R.id.rb_refund);

        edit_select_date = findViewById(R.id.edit_select_date);
        txtdropdown_vendor = findViewById(R.id.txtdropdown_vendor);
        radio_group = findViewById(R.id.radio_group);
        rbCASE = findViewById(R.id.rbCASE);
        rbCARD = findViewById(R.id.rbCARD);
        rbOTHER = findViewById(R.id.rbOTHER);
        edit_payment_detail = findViewById(R.id.edit_payment_detail);
        edit_invoice_no = findViewById(R.id.edit_invoice_no);
        edit_Amount = findViewById(R.id.edit_Amount);
        edit_remark = findViewById(R.id.edit_remark);
        txt_vendor_id = findViewById(R.id.txt_vendor_id);

        txt_customer_number = findViewById(R.id.txt_customer_number);
        txt_customer_name_new = findViewById(R.id.txt_customer_name_new);

        Btn_Save_whatsapp = findViewById(R.id.Btn_Save_whatsapp);
        Btn_Save_sms = findViewById(R.id.Btn_Save_sms);
        rv_img_block = findViewById(R.id.rv_img_block);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);

        rv_img_block.setLayoutManager(linearLayoutManager);

        _paymentID = getIntent().getIntExtra("paymentID", 0);
        ClsPaymentMaster = (ClsPaymentMaster) getIntent().getSerializableExtra("ClsPaymentMaster");

        imgAdapter = new PreviewSmallImgBlockAdapter(AddPaymentActivity.this, imageController,
                payment_Imgs_uri);
        rv_img_block.setAdapter(imgAdapter);

        imgAdapter.setOnClickImg((uri, position) -> {
//            clickImagePOPUP(uri, position);

            open(uri, position);


        });


        iv_add_img = findViewById(R.id.iv_add_img);

        iv_add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectionValidation()) {
                    Intent intent = new Intent(getApplicationContext(), ActivityImageEdit.class);
                    intent.putExtra("_imgMode", "AddPayment");
                    intent.putExtra("imgType", "Payment");
                    intent.putExtra("_paymentType", _paymentType);

                    if (type != null) {
                        if (type.equalsIgnoreCase("Vendor")) {
                            intent.putExtra("_mobileNo", txtdropdown_vendor.getTag().toString());
                        } else if (type.equalsIgnoreCase("Customer")) {
                            intent.putExtra("_mobileNo", txt_customer_number.getText().toString());
                        }
                    }
                    startActivity(intent);
                }
            }
        });

        edit_select_date.setText(ClsGlobal.getEntryDateAndTime());
        edit_select_date.setTag(ClsGlobal.getEntryDateAndTime());

        type = getIntent().getStringExtra("type");
        ReceiptNo = getIntent().getIntExtra("ReceiptNo", 0);

        Log.e("--Payment--", "ReceiptNo: " + ReceiptNo);
        Log.e("--Payment--", "type: " + type);

        if (type.equalsIgnoreCase("Vendor")) {
            txt_customer_name_new.setVisibility(View.GONE);
            txtdropdown_vendor.setVisibility(View.VISIBLE);
            _paymentType = "Vendor Payment";
        } else {
            txt_customer_name_new.setVisibility(View.VISIBLE);
            txtdropdown_vendor.setVisibility(View.GONE);
            edit_invoice_no.setVisibility(View.GONE);
            _paymentType = "Customer Payment";

        }

        if (type != null && !type.equalsIgnoreCase("")) {
            setTitle(type + " Payments");
        }

        getNewnumberReceiptNo = ClsPaymentMaster.getLastReceiptNo(AddPaymentActivity.this);

        radio_group.setOnCheckedChangeListener((group, checkedId) -> {

            int selectedId = radio_group.getCheckedRadioButtonId();
            // find the radio button by returned id
            RadioButton radioButton = findViewById(selectedId);
            SelectedPaymentMode = radioButton.getText().toString();

        });

        edit_select_date.setOnClickListener(v -> {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(AddPaymentActivity.this,
                    (view, year, month, day) -> {
                        c.set(year, month, day);
                        String date = new SimpleDateFormat("dd/MM/yyyy hh:mm aa").format(c.getTime());
                        edit_select_date.setText(date);
                        edit_select_date.setTag(date);
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                    }, mYear, mMonth, mDay);
            dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
            Calendar d = Calendar.getInstance();
            dpd.updateDate(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH));
            dpd.show();

        });

        txt_customer_name_new.setOnClickListener(v -> {
            fillCustomerList();
            CustomerDialog();
        });

        txtdropdown_vendor.setOnClickListener(v -> {
            fillVendorList();
            VendorList();
        });

        btn_Save.setOnClickListener(v -> {
            save();
        });

        Btn_Save_whatsapp.setOnClickListener(v -> {
            sendWhatsApp();
        });

        Btn_Save_sms.setOnClickListener(v -> {
            sendTextSMS();
        });

        if (ReceiptNo != 0) {
            setData();
        }

        if (ClsPaymentMaster != null) {
            if (ClsPaymentMaster.getPaymentID() != 0) {
                setImageBlock();
            }
        }
    }



    public void open(Uri uriForPath, int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure,You wanted to make decision");
        alertDialogBuilder.setPositiveButton("PREVIEW",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        previewImage(uriForPath, position);
                    }
                });

        alertDialogBuilder.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(DialogInterface dialog, int which) {
//
                if (ClsPaymentMaster != null) {
                    if (ClsPaymentMaster.getPaymentID() != 0) {

                        String where = " AND [UniqueId] = ".concat("'" + ClsPaymentMaster.getPaymentID() + "'")
                                .concat(" AND [Type] = ".concat("'Customer Payment'"));

                        db = AddPaymentActivity.this.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
                        ClsImages.DeleteByPaymentID(where, db, AddPaymentActivity.this);
                        db.close();
                    }
                }
                deleteImage(uriForPath, position);
                dialog.dismiss();
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        imgAdapter.changePath(payment_Imgs_uri);
    }

    private void fillCustomerList() {
        list_customer = new ArrayList<>();
        list_customer = new ClsCustomerMaster().ListCustomers("", AddPaymentActivity.this);
    }

    private void fillVendorList() {
        list_vendor = new ArrayList<>();
        listVendorSearch = list_vendor = new ClsVendor(getApplicationContext()).getList(" " +
                "AND [ACTIVE] = 'YES' AND [Type] IN ('SUPPLIER','BOTH') ORDER BY [VENDOR_NAME]");
    }

    private void save() {

        if (validation()) {


            ClsPaymentMaster currentSaveObj = new ClsPaymentMaster();
            currentSaveObj.setPaymentDate(ClsGlobal.getChangeDateFormatDB(edit_select_date.getTag().toString()));

            currentSaveObj.setPaymentMounth(ClsGlobal.getDayMonthForPayment(
                    edit_select_date.getTag().toString()));

            if (!type.equalsIgnoreCase("Vendor")) {

                currentSaveObj.setMobileNo(txt_customer_number.getText().toString().equalsIgnoreCase("")
                        ? "" : txt_customer_number.getText().toString().trim());
                currentSaveObj.setCustomerName(txt_customer_name_new.getText().toString().equalsIgnoreCase("")
                        ? "" : txt_customer_name_new.getText().toString().trim());

                currentSaveObj.setVendorName("");
                currentSaveObj.setVendorID(0);

            } else {
                currentSaveObj.setCustomerName("");
                currentSaveObj.setMobileNo("");
                currentSaveObj.setVendorID(Integer.parseInt(txt_vendor_id.getTag().toString()));
                currentSaveObj.setVendorName(txtdropdown_vendor.getText().toString().equalsIgnoreCase("")
                        ? "" : txtdropdown_vendor.getText().toString());
            }

            currentSaveObj.setPaymentMode(SelectedPaymentMode);
            currentSaveObj.setPaymentDetail(edit_payment_detail.getText().toString().equalsIgnoreCase("")
                    ? "" : edit_payment_detail.getText().toString().trim());

            currentSaveObj.setInvoiceNo(edit_invoice_no.getText().toString().equalsIgnoreCase("")
                    ? "" : edit_invoice_no.getText().toString().trim());

            double _amount = Double.parseDouble(edit_Amount.getText().toString().equalsIgnoreCase("")
                    ? "0.00" : edit_Amount.getText().toString().trim());

            if (rb_refund.isChecked() && type.equalsIgnoreCase("Customer")) {

                _amount = _amount * -1;

            } else if (rb_received.isChecked() && type.equalsIgnoreCase("Vendor")) {

                _amount = _amount * -1;
            }

            currentSaveObj.setAmount(_amount);
            currentSaveObj.setRemark(edit_remark.getText().toString().equalsIgnoreCase("")
                    ? "" : edit_remark.getText().toString().trim());

            currentSaveObj.setEntryDate(ClsGlobal.getEntryDate());
            currentSaveObj.setType(type);


            int result;
            if (ReceiptNo != 0) {
                currentSaveObj.setReceiptNo(String.valueOf(ReceiptNo));
                result = ClsPaymentMaster.updatePaymentReport(currentSaveObj, this);
            } else {
                currentSaveObj.setReceiptNo(String.valueOf(getNewnumberReceiptNo) + 1);
                result = ClsPaymentMaster.Insert(currentSaveObj, this);
            }


            Gson gson2 = new Gson();
            String jsonInString2 = gson2.toJson(currentSaveObj);
            Log.e("--Payment--", "Gson: " + jsonInString2);
            Log.e("--Payment--", "ReceiptNo: " + ReceiptNo);
            Log.e("--Payment--", "getNewnumberReceiptNo: " + getNewnumberReceiptNo);


            if (result != 0) {
                Toast.makeText(this, "Save Successfully", Toast.LENGTH_LONG).show();
                ImageInsert(result);

            } else {
                Toast.makeText(this, "Failed Saving", Toast.LENGTH_LONG).show();
            }
            payment_Imgs_uri.clear();
            finish();
        }
    }


    void deleteImage(Uri clsUri, int position) {
        String path = clsUri.getPath();
        File fDelete = new File(path);

        if (fDelete.exists()) {
            if (fDelete.delete()) {
                imgAdapter.removeImage(position);
                Toast.makeText(this, "Image is delete.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Image not delete.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    void previewImage(Uri clsUri, int position) {
        Dialog nagDialog = new Dialog(AddPaymentActivity.this,
                android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nagDialog.setCancelable(true);
        nagDialog.setContentView(R.layout.dialog_preview_image);
        Objects.requireNonNull(nagDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        ImageView ivPreview = nagDialog.findViewById(R.id.iv_preview_image);

        String path = clsUri.getPath();
        File imgFile = new File(path);
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ivPreview.setImageBitmap(myBitmap);
        }
        nagDialog.show();
    }

    void ImageInsert(int _paymentID) {

        int result = 0;
        List<String> img_paths = new ArrayList<>();

        if (payment_Imgs_uri != null && payment_Imgs_uri.size() != 0) {

            for (Uri _uri : payment_Imgs_uri) {
                img_paths.add(_uri.getPath());
            }

            if (img_paths.size() != 0) {

                for (String _filePathNew : img_paths) {

                    Log.e("--Payment ID--", "_paymentID: " + _paymentID);

                    ClsImages obj = new ClsImages();

                    obj.setDisplayOrder(0);
                    obj.setFilePath(_filePathNew);
                    obj.setFileName(new File(_filePathNew).getName());
                    obj.setUniqueId(String.valueOf(_paymentID));
                    obj.setType(_paymentType);
                    obj.setFileType("Image");
                    obj.setExtension(".jpg");
                    obj.setNotes("");

                    result = ClsImages.Insert(obj, AddPaymentActivity.this);
                }

            }

            if (result == -1) {
                Toast.makeText(getApplicationContext(), "Technical Error", Toast.LENGTH_SHORT).show();
            } else if (result == 0) {
                Toast.makeText(getApplicationContext(), "Data Not Inserted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Photo save successfully.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }


    List<ClsImages> lstImgList = new ArrayList<>();
    private SQLiteDatabase db;

    @SuppressLint("WrongConstant")
    void setImageBlock() {

        lstImgList = new ArrayList<>();

        String where = " AND [UniqueId] = ".concat("'" + ClsPaymentMaster.getPaymentID() + "'")
                .concat(" AND [Type] = ".concat("'Customer Payment'"));

        Log.d("--Update--", "where: " + where);

        db = AddPaymentActivity.this.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
        lstImgList = new ClsImages().getQueryByIdPaymentImageBlockList(where, db, AddPaymentActivity.this);
        Log.d("--Update--", "Size: " + lstImgList.size());
        db.close();

        //fill adp here

//        imgAdapter = new PreviewSmallImgBlockAdapter(AddPaymentActivity.this, imageController,
//                payment_Imgs_uri);

        for (ClsImages obj : lstImgList) {
            obj.getFilePath();
            Log.d("--Update--", "Path: " + obj.getFilePath());
            payment_Imgs_uri.add(Uri.fromFile(new File(obj.getFilePath())));
        }

        rv_img_block.setAdapter(imgAdapter);
        imgAdapter.changePath(payment_Imgs_uri);

//        Gson gson2 = new Gson();
//        String jsonInString2 = gson2.toJson(lstImgList);
//        Log.e("--Update--", "Gson: " + jsonInString2);

    }

    void setData() {

        ClsPaymentMaster clsPaymentMaster = new ClsPaymentMaster();
        clsPaymentMaster.setReceiptNo(String.valueOf(ReceiptNo));

        clsPaymentMaster = ClsPaymentMaster.queryById(clsPaymentMaster.getReceiptNo(),
                AddPaymentActivity.this);

        edit_select_date.setText(ClsGlobal.getDDMMYYYY(clsPaymentMaster.getPaymentDate()));

        if (type.equalsIgnoreCase("Customer")) {
            txt_customer_number.setText(clsPaymentMaster.getMobileNo());
            txt_customer_name_new.setText(clsPaymentMaster.getCustomerName());
            txtdropdown_vendor.setText("");
            edit_invoice_no.setText("");
            txt_vendor_id.setTag(0);

        } else {
            txtdropdown_vendor.setText(clsPaymentMaster.getVendorName());
            edit_invoice_no.setText(clsPaymentMaster.getInvoiceNo());
            txt_vendor_id.setTag(clsPaymentMaster.getVendorID());
            txt_customer_number.setText("");
            txt_customer_name_new.setText("");
        }

        if (clsPaymentMaster.getPaymentMode().equalsIgnoreCase("CASH")) {
            rbCASE.setChecked(true);
        } else if (clsPaymentMaster.getPaymentMode().equalsIgnoreCase("CARD")) {
            rbCARD.setChecked(true);
        } else if (clsPaymentMaster.getPaymentMode().equalsIgnoreCase("OTHER")) {
            rbOTHER.setChecked(true);
        }

        //------------------------------COLLECT - PENDING------------------------------------


        edit_payment_detail.setText(clsPaymentMaster.getPaymentDetail());
        edit_Amount.setText(String.valueOf(clsPaymentMaster.getAmount()));

        edit_remark.setText(clsPaymentMaster.getRemark());

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        payment_Imgs_uri.clear();
    }

    private void CustomerDialog() {

        final EditText edit_search;
        dialogvendor = new Dialog(AddPaymentActivity.this);
        dialogvendor.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogvendor.setContentView(R.layout.dialog_type_n_search);
        dialogvendor.setTitle("Select Customer");
        dialogvendor.setCanceledOnTouchOutside(true);
        dialogvendor.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogvendor.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialogvendor.getWindow().setAttributes(lp);

        lst = dialogvendor.findViewById(R.id.list);
        lyout_nodata = dialogvendor.findViewById(R.id.lyout_nodata);
        edit_search = dialogvendor.findViewById(R.id.edit_search);

        Handler handler = new Handler();
        handler.postDelayed(() -> {

            try {

                if (list_customer != null && list_customer.size() != 0) {

                    customerAdapter = new CustomerAdapter(AddPaymentActivity.this);
                    customerAdapter.AddItems(list_customer);
                    lst.setAdapter(customerAdapter);

                    edit_search.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String where = "";

                            if (s != null && !s.equals("")) {
                                where = " AND ([NAME] LIKE '%".concat(String.valueOf(s)).concat("%'");
                                where = where.concat(" OR [MOBILE_NO] LIKE '%".concat(String.valueOf(s)).concat("%')"));
                                Log.e("where", where);
                            }

                            list_customer = new ClsCustomerMaster().ListCustomers(where, AddPaymentActivity.this);
                            customerAdapter.AddItems(list_customer);
                            lst.setAdapter(customerAdapter);

                        }
                    });

                    customerAdapter.SetOnClickListener(clsCustomerMaster -> {
                        txt_customer_name_new.setText(clsCustomerMaster.getmName());
                        txt_customer_number.setText(String.valueOf(clsCustomerMaster.getmMobile_No()));
                        dialogvendor.dismiss();

                    });

                }

            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            }

        }, 400);
        dialogvendor.show();

    }


    private boolean selectionValidation() {

        boolean result = true;

        if (type.equalsIgnoreCase("Vendor")) {
            if (txtdropdown_vendor.getText() == null ||
                    txtdropdown_vendor.getText().toString().equalsIgnoreCase("Select Vendor")) {
                Toast.makeText(this, "Select Vendor", Toast.LENGTH_LONG).show();
                return false;
            } else {
                txtdropdown_vendor.setError(null);
            }

        } else {
            if (txt_customer_name_new.getText() == null ||
                    txt_customer_name_new.getText().toString().equalsIgnoreCase("Select Customer")) {
                Toast.makeText(this, "Select Customer", Toast.LENGTH_LONG).show();
                return false;
            } else {
                txt_customer_name_new.setError(null);
            }
        }

        return result;
    }


    private boolean validation() {
        boolean result = true;
        //branch validation
        if (edit_select_date.getText() == null ||
                edit_select_date.getText().toString().isEmpty()) {
            Toast.makeText(this, "Select Date", Toast.LENGTH_LONG).show();
            edit_select_date.requestFocus();
            return false;
        } else {
            edit_select_date.setError(null);
        }

        if (type.equalsIgnoreCase("Vendor")) {
            if (txtdropdown_vendor.getText() == null ||
                    txtdropdown_vendor.getText().toString().equalsIgnoreCase("Select Vendor")) {
                Toast.makeText(this, "Select Vendor", Toast.LENGTH_LONG).show();
                return false;
            } else {
                txtdropdown_vendor.setError(null);
            }

        } else {
            if (txt_customer_name_new.getText() == null ||
                    txt_customer_name_new.getText().toString().equalsIgnoreCase("Select Customer")) {
                Toast.makeText(this, "Select Customer", Toast.LENGTH_LONG).show();
                return false;
            } else {
                txt_customer_name_new.setError(null);
            }
        }

        int checkPaymentModeID = radio_group.getCheckedRadioButtonId();
        if (checkPaymentModeID == -1) {
            Toast.makeText(this, "Select Payment Mode", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edit_Amount.getText() == null ||
                edit_Amount.getText().toString().isEmpty()) {
            edit_Amount.setError("Amount Required");
            edit_Amount.requestFocus();
            return false;
        } else {
            edit_Amount.setError(null);
        }

        int checkPaymentTypeID = rg_payment_group.getCheckedRadioButtonId();

        if (checkPaymentTypeID == -1) {
            Toast.makeText(this, "Select Payment Type", Toast.LENGTH_SHORT).show();
            return false;
        }

        return result;
    }

    private void VendorList() {

        final EditText edit_search;
        dialogvendor = new Dialog(AddPaymentActivity.this);
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
        lyout_nodata = dialogvendor.findViewById(R.id.lyout_nodata);
        edit_search = dialogvendor.findViewById(R.id.edit_search);

        vendoradapter = new VendorAdapter(AddPaymentActivity.this);
        lst.setAdapter(vendoradapter);

        Handler handler = new Handler();
        handler.postDelayed(() -> {

            try {

                if (list_vendor != null && list_vendor.size() != 0) {
                    lst.setVisibility(View.VISIBLE);
                    lyout_nodata.setVisibility(View.GONE);
                    vendoradapter.AddItems(list_vendor);

                    edit_search.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (s != null) {
                                String where = " AND ([CONTACT_NO] LIKE '%".concat(String.valueOf(s)).concat("%'");
                                where = where.concat(" OR [VENDOR_NAME] LIKE '%".concat(String.valueOf(s)).concat("%') "));
                                Log.e("where", where);

                                list_vendor = new ClsVendor(getApplicationContext()).getList(where +
                                        " AND [ACTIVE] = 'YES' AND [Type] IN ('SUPPLIER','BOTH')" +
                                        " ORDER BY [VENDOR_NAME]");

                                if (list_vendor != null & list_vendor.size() > 0) {
                                    lyout_nodata.setVisibility(View.GONE);
                                    lst.setVisibility(View.VISIBLE);
                                    vendoradapter.AddItems(list_vendor);
                                } else {
                                    lyout_nodata.setVisibility(View.VISIBLE);
                                    lst.setVisibility(View.GONE);
                                }

                            }

                        }
                    });
                } else {
                    lyout_nodata.setVisibility(View.VISIBLE);
                    lst.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            }

        }, 400);
        dialogvendor.show();
    }


    void sendTextSMS() {
        if (validation()) {
            String sentToNumber = "";

            if (type.equalsIgnoreCase("Vendor")) {
                sentToNumber = txtdropdown_vendor.getTag().toString();
            } else {
                sentToNumber = txt_customer_name_new.getText().toString();
            }

            ClsUserInfo userInfo = ClsGlobal.getUserInfo(AddPaymentActivity.this);

            StringBuilder stringBufferConcat = new StringBuilder()

                    .append(" " + userInfo.getBusinessname().trim() + "" + "\n\n")

                    .append("Contact No: " + userInfo.getMobileNo().trim() + "\n")
                    .append("Email: " + userInfo.getEmailaddress().trim() + "\n")
                    .append("City: " + userInfo.getCity().trim() + "" + "\n\n")

                    .append("Payment Details: " + edit_payment_detail.getText().toString().trim() + "\n")
                    .append("Amount: " + edit_Amount.getText().toString().trim() + "\n")

                    .append("DATE: " + ClsGlobal.getEntryDateFormat(ClsGlobal.getCurruntDateTime()) + "\n\n")
                    .append("Thank you");

            String message = stringBufferConcat.toString();

            ClsGlobal.sendSMS(AddPaymentActivity.this,
                    sentToNumber, message);

            save();
        }
    }

    void sendWhatsApp() {
        if (validation()) {
            boolean isWhatsappInstalled = ClsGlobal.whatsappInstalledOrNot("com.whatsapp", AddPaymentActivity.this);
            boolean isWhatsappBusinessInstalled = ClsGlobal.whatsappInstalledOrNot("com.whatsapp.w4b", AddPaymentActivity.this);

            if (isWhatsappBusinessInstalled || isWhatsappInstalled) {
                ClsUserInfo userInfo = ClsGlobal.getUserInfo(AddPaymentActivity.this);

                StringBuilder stringBufferConcat = new StringBuilder()

                        .append("*" + userInfo.getBusinessname().trim() + "*" + "\n\n")

                        .append("*Contact No:* " + userInfo.getMobileNo().trim() + "\n")
                        .append("*Email:* " + userInfo.getEmailaddress().trim() + "\n")
                        .append("*City: " + userInfo.getCity().trim() + "*" + "\n\n")

                        .append("*Payment Details:* " + edit_payment_detail.getText().toString().trim() + "\n")
                        .append("*Amount:* " + edit_Amount.getText().toString().trim() + "\n")

                        .append("*DATE:* " + ClsGlobal.getEntryDateFormat(ClsGlobal.getCurruntDateTime()) + "\n\n")
                        .append("Thank you");

                String message = stringBufferConcat.toString();
                Log.e("message", message);

                String url = "";

                if (type.equalsIgnoreCase("Vendor")) {
                    url = "https://api.whatsapp.com/send?phone=+91" + txtdropdown_vendor.getTag().toString() + "&text=" + message;
                } else {
                    url = "https://api.whatsapp.com/send?phone=+91" + txt_customer_number.getText().toString() + "&text=" + message;
                }


                Log.e("1message- ", message);
                Log.e("url- ", url);
                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.setAction(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse(url));
                startActivity(sendIntent);
            }

            save();

        }
    }

    class VendorAdapter extends BaseAdapter implements StickyListHeadersAdapter {

        Context context;

        List<ClsVendor> list_vendor = new ArrayList<>();
        private LayoutInflater inflater;

        public VendorAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void AddItems(List<ClsVendor> list_vendor) {
            this.list_vendor = list_vendor;
            notifyDataSetChanged();
        }

        @Override
        public long getHeaderId(int position) {
            return list_vendor.get(position).getVendor_name().toUpperCase().subSequence(0, 1).charAt(0);
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


            lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int Popid = (int) id;
                    txtdropdown_vendor.setText(list_vendor.get(position).getVendor_name());
                    txtdropdown_vendor.setTag(list_vendor.get(position).getContact_no());
                    txt_vendor_id.setText(String.valueOf(list_vendor.get(position).getVendor_id()));

                    txt_vendor_id.setTag(list_vendor.get(position).getVendor_id());

                    ObjVendor = list_vendor.get(position);
                    dialogvendor.dismiss();

                }
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
                holder.text = convertView.findViewById(R.id.rowName);

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
