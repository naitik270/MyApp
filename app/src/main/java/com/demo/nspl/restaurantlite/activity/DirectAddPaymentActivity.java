package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.ImageEdit.ActivityImageEdit;
import com.demo.nspl.restaurantlite.ImageEdit.PreviewSmallImgBlockAdapter;
import com.demo.nspl.restaurantlite.MultipleImage.ImageController;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.VendorLedger.ClsVendorLedger;
import com.demo.nspl.restaurantlite.classes.ClsImages;
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;
import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.payment_Imgs_uri;


//2020042403755


public class DirectAddPaymentActivity extends AppCompatActivity {

    ClsVendorLedger clsVendorLedger;
    Toolbar toolbar;
    TextView txt_name;
    TextView txt_invalid_amount;
    ImageView iv_clear_amt;
    EditText edt_amount, edt_payment_detail;
    TextView edit_select_date;
    LinearLayout ll_hide;
    Button btn_save;
    String _pmtMode = "", type = "", SelectedPaymentMode = "";
    int mYear, mMonth, mDay;
    private RadioGroup radio_group;
    int ReceiptNo = 0, _paymentID = 0;
    int getNewNumberReceiptNo = 0;

    PreviewSmallImgBlockAdapter imgAdapter;
    RecyclerView rv_img_block;
    LinearLayoutManager linearLayoutManager;
    ImageController imageController;
    ImageView iv_add_img;
    String _paymentType = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_payment);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "DirectAddPaymentActivity"));
        }

        toolbar = findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        main();
    }

    @SuppressLint("SetTextI18n")
    private void main() {

        clsVendorLedger = (ClsVendorLedger) getIntent().getSerializableExtra("clsVendorLedger");
        _pmtMode = getIntent().getStringExtra("_pmtMode");
        type = getIntent().getStringExtra("type");
        ReceiptNo = getIntent().getIntExtra("ReceiptNo", 0);
        _paymentID = getIntent().getIntExtra("paymentID", 0);
        getNewNumberReceiptNo = ClsPaymentMaster.getLastReceiptNo(DirectAddPaymentActivity.this);


        txt_name = findViewById(R.id.txt_name);
        txt_invalid_amount = findViewById(R.id.txt_invalid_amount);
        iv_clear_amt = findViewById(R.id.iv_clear_amt);
        edt_amount = findViewById(R.id.edt_amount);
        edit_select_date = findViewById(R.id.edit_select_date);
        edt_payment_detail = findViewById(R.id.edt_payment_detail);
        btn_save = findViewById(R.id.btn_save);
        ll_hide = findViewById(R.id.ll_hide);
        radio_group = findViewById(R.id.radio_group);
        iv_add_img = findViewById(R.id.iv_add_img);
        rv_img_block = findViewById(R.id.rv_img_block);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);

        rv_img_block.setLayoutManager(linearLayoutManager);
        Log.d("--Type--", "DirectAddType: " + type);

        Log.d("--Type--", "clsVendorLedger: " + clsVendorLedger);


        Intent intent = getIntent();
        if (type != null) {
            if (type.equalsIgnoreCase("Vendor")) {
                _paymentType = "Vendor Payment";
                intent.putExtra("getVendorID", clsVendorLedger.getVENDOR_ID());
            } else if (type.equalsIgnoreCase("Customer")) {
                _paymentType = "Customer Payment";
                intent.putExtra("key", clsVendorLedger.getCustomerMobileNo());
            }
        }

        Log.d("--Image--", "requiredValue: " + clsVendorLedger.getVENDOR_ID());

        setResult(RESULT_OK, intent);

        edit_select_date.setText(ClsGlobal.getEntryDateAndTime());
        edit_select_date.setTag(ClsGlobal.getEntryDateAndTime());

        if (_pmtMode != null) {

            if (_pmtMode.equalsIgnoreCase("PAY")) {
                txt_name.setText("YOU PAY \u20B9 0" + " TO " + clsVendorLedger.getCustomerName().toUpperCase());
                btn_save.setBackgroundColor(Color.parseColor("#C02828"));
            } else {
                txt_name.setText("YOU RECEIVE \u20B9 0" + " TO " + clsVendorLedger.getCustomerName().toUpperCase());
                btn_save.setBackgroundColor(Color.parseColor("#225A25"));
            }
        }

        edt_amount.addTextChangedListener(new TextWatcher() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {

                if (_pmtMode != null) {
                    if (_pmtMode.equalsIgnoreCase("PAY")) {

                        if (s.toString().length() != 0) {

                            txt_name.setText("YOU PAY \u20B9 " + edt_amount.getText().toString() +
                                    " TO " + clsVendorLedger.getCustomerName().toUpperCase());
                            txt_invalid_amount.setVisibility(View.GONE);
                            ll_hide.setVisibility(View.VISIBLE);
                        } else {

                            txt_name.setText("YOU PAY \u20B9 0" + " TO " +
                                    clsVendorLedger.getCustomerName().toUpperCase());
                            txt_invalid_amount.setVisibility(View.VISIBLE);
                            ll_hide.setVisibility(View.GONE);
                        }

                    } else {

                        if (s.toString().length() != 0) {
                            txt_name.setText("YOU RECEIVE \u20B9 " + edt_amount.getText().toString() +
                                    " TO " + clsVendorLedger.getCustomerName().toUpperCase());
                            txt_invalid_amount.setVisibility(View.GONE);
                            ll_hide.setVisibility(View.VISIBLE);
                        } else {

                            txt_name.setText("YOU RECEIVE \u20B9 0" + " TO " +
                                    clsVendorLedger.getCustomerName().toUpperCase());
                            txt_invalid_amount.setVisibility(View.VISIBLE);
                            ll_hide.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });

        iv_clear_amt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int length = edt_amount.getText().length();
                if (length > 0) {
                    edt_amount.getText().delete(length - 1, length);
                }
            }
        });


        radio_group.setOnCheckedChangeListener((group, checkedId) -> {

            int selectedId = radio_group.getCheckedRadioButtonId();
            // find the radio button by returned id
            RadioButton radioButton = findViewById(selectedId);
            SelectedPaymentMode = radioButton.getText().toString();

        });

        edit_select_date.setOnClickListener(v -> {
            selectDate();
        });

        imgAdapter = new PreviewSmallImgBlockAdapter(DirectAddPaymentActivity.this, imageController,
                payment_Imgs_uri);
        rv_img_block.setAdapter(imgAdapter);

        imgAdapter.setOnClickImg((uri, position) -> {
            clickImagePOPUP(uri, position);
        });

        iv_add_img = findViewById(R.id.iv_add_img);

        iv_add_img.setOnClickListener(view -> {
            Intent intent1 = new Intent(getApplicationContext(), ActivityImageEdit.class);
            intent1.putExtra("_imgMode", "AddPayment");
            intent1.putExtra("imgType", "Payment");
            intent1.putExtra("_mobileNo", clsVendorLedger.getCustomerMobileNo());
            intent1.putExtra("_paymentType", _paymentType);
            Log.d("--Type--", "onCLick: " + _paymentType);
            startActivity(intent1);
        });

        btn_save.setOnClickListener(v -> {
            if (validationForPayment()) {
                DirectSavePayment();
            }
//                finish();
        });
    }

    void DirectSavePayment() {
        ClsPaymentMaster currentSaveObj = new ClsPaymentMaster();

        currentSaveObj.setPaymentDate(ClsGlobal.getChangeDateFormatDB(edit_select_date.getTag().toString()));

        currentSaveObj.setPaymentMounth(ClsGlobal.getDayMonthForPayment(
                edit_select_date.getTag().toString()));

        if (!type.equalsIgnoreCase("Vendor")) {

            currentSaveObj.setMobileNo(clsVendorLedger.getCustomerMobileNo().equalsIgnoreCase("")
                    ? "" : clsVendorLedger.getCustomerMobileNo().trim());
            currentSaveObj.setCustomerName(clsVendorLedger.getCustomerName().equalsIgnoreCase("")
                    ? "" : clsVendorLedger.getCustomerName());

            currentSaveObj.setVendorName("");
            currentSaveObj.setVendorID(0);
            currentSaveObj.setInvoiceNo("");

        } else {
            currentSaveObj.setCustomerName("");
            currentSaveObj.setMobileNo("");

            currentSaveObj.setVendorID(Integer.parseInt(String.valueOf(clsVendorLedger.getVENDOR_ID())));
            currentSaveObj.setVendorName(clsVendorLedger.getVENDOR_NAME().equalsIgnoreCase("")
                    ? "" : clsVendorLedger.getVENDOR_NAME());

            currentSaveObj.setInvoiceNo("00000000");

        }
        currentSaveObj.setPaymentMode(SelectedPaymentMode);

        currentSaveObj.setPaymentDetail(edt_payment_detail.getText().toString().equalsIgnoreCase("")
                ? "" : edt_payment_detail.getText().toString().trim());

        double _amount = Double.parseDouble(edt_amount.getText().toString().equalsIgnoreCase("")
                ? "0.00" : edt_amount.getText().toString().trim());


//        if (_pmtMode != null) {
//            if (_pmtMode.equalsIgnoreCase("PAY") &&
//                    type.equalsIgnoreCase("Customer")) {
//
//                _amount = _amount * -1;
//
//            } else if (_pmtMode.equalsIgnoreCase("RECEIVE") &&
//                    type.equalsIgnoreCase("Vendor")) {
//
//                _amount = _amount * -1;
//            }
//        }

        if (_pmtMode != null) {
            if (_pmtMode.equalsIgnoreCase("PAY") &&
                    type.equalsIgnoreCase("Customer")) {

                _amount = _amount * -1;

            }
        }

        currentSaveObj.setAmount(_amount);
        currentSaveObj.setRemark("Direct Payment");

        currentSaveObj.setEntryDate(ClsGlobal.getEntryDate());
        currentSaveObj.setType(type);

        Gson gson = new Gson();
        String jsonInString = gson.toJson(currentSaveObj);
        Log.d("--Direct--", "Gson: " + jsonInString);

        int result;
        if (ReceiptNo != 0) {
            currentSaveObj.setReceiptNo(String.valueOf(ReceiptNo));
            result = ClsPaymentMaster.updatePaymentReport(currentSaveObj, this);
        } else {
            currentSaveObj.setReceiptNo(String.valueOf(getNewNumberReceiptNo) + 1);
            result = ClsPaymentMaster.Insert(currentSaveObj, this);
        }

        if (result != 0) {
            Toast.makeText(this, "Save Successfully", Toast.LENGTH_LONG).show();
            ImageInsert(result);

        } else {
            Toast.makeText(this, "Failed Saving", Toast.LENGTH_LONG).show();
        }
        payment_Imgs_uri.clear();
        finish();
    }

    void clickImagePOPUP(Uri uriForPath, int position) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.message_logout_prompt, null);

        TextView tvMessage = alertLayout.findViewById(R.id.tvPromptMessage);

        AlertDialog alertDialog = new AlertDialog.Builder(DirectAddPaymentActivity.this,
                R.style.AppCompatAlertDialogStyle).create(); //Read Update.
        alertDialog.setView(alertLayout);
//        tvMessage.setText("Are sure you want to close this application?");
        tvMessage.setText("Selected Image should be?");
        alertDialog.setCancelable(true);

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "PREVIEW", (dialog, which) ->
                previewImage(uriForPath, position));

        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "DELETE", (dialog, which) ->
                deleteImage(uriForPath, position));

        alertDialog.show();
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
        Dialog nagDialog = new Dialog(DirectAddPaymentActivity.this,
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

    @Override
    protected void onResume() {
        super.onResume();
        imgAdapter.changePath(payment_Imgs_uri);
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

                    result = ClsImages.Insert(obj, DirectAddPaymentActivity.this);
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


    private void selectDate() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(DirectAddPaymentActivity.this,
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
    }

    private boolean validationForPayment() {

        boolean result = true;

        double _SaleRateWithoutTax = 0.0;
        if (edt_amount.getText() != null && edt_amount.getText().toString().length() != 0) {
            String txtVal = edt_amount.getText().toString();
            if (txtVal.equalsIgnoreCase(".")) {
                txtVal = "0";
            }
            _SaleRateWithoutTax = Double.parseDouble(txtVal);
        }

        if (_SaleRateWithoutTax <= 0) {//1.0
            edt_amount.setError("Amount Required");
            edt_amount.requestFocus();
            return false;
        } else {
            edt_amount.setError(null);
        }

        if (edt_payment_detail.getText() == null ||
                edt_payment_detail.getText().toString().isEmpty()) {
            edt_payment_detail.setError("Detail Required");
            edt_payment_detail.requestFocus();
            return false;
        } else {
            edt_payment_detail.setError(null);
        }

        int checkPaymentModeID = radio_group.getCheckedRadioButtonId();
        if (checkPaymentModeID == -1) {
            Toast.makeText(this, "Select Payment Mode", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edit_select_date.getText() == null ||
                edit_select_date.getText().toString().isEmpty()) {
            edit_select_date.setError("Date Required");
            edit_select_date.requestFocus();
            return false;
        } else {
            edit_select_date.setError(null);
        }

        return result;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
