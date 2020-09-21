package com.demo.nspl.restaurantlite.Purchase;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.MultipleImage.ClsMultipleImg;
import com.demo.nspl.restaurantlite.MultipleImage.PurchaseImagePreviewActivity;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsVendor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class RetailPurchaseActivity extends AppCompatActivity {

    TextView txt_purchase_date, txt_spn_vendor;
    EditText edt_bill_no, edt_po_no, edt_remark;
    Button btn_next;
    String formattedDate;
    int mYear, mMonth, mDay;
    EditText edit_search;
    ImageView img_clear;
    StickyListHeadersListView lst;
    String _VendorName;
    RelativeLayout lyout_nodata;
    List<ClsVendor> lstClsVendorMasters;
    List<ClsVendor> listVendorSearch = new ArrayList<>();
    int VendorID = 0;
    Integer _ID;
    VendorStickyAdapter vendorStickyAdapter;
    Toolbar toolbar;


    Button btn_add_bill;


    String _purchaseFlag = "", _updatePurchaseDate = "",
            _updatePurchaseBillNo = "", _updatePurchaseVendorName = "", _updatePurchaseRemark = "";

    int _updatePurchaseID = 0, _updatePurchasePoNo = 0, _updatePurchaseVendorID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retail_purchase_activity);
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        main();
    }

    private void main() {

        Intent intent = getIntent();

        _ID = intent.getIntExtra("_ID", 0);

        _updatePurchaseID = intent.getIntExtra("_updatePurchaseID", 0);


        Log.d("--Invoice--", "_ID: " + _ID);
        Log.d("--Invoice--", "_updatePurchaseID: " + _updatePurchaseID);


        _purchaseFlag = intent.getStringExtra("_purchaseFlag");

        _updatePurchaseDate = intent.getStringExtra("_updatePurchaseDate");
        Log.d("--Invoice--", "_updatePurchaseDate: " + _updatePurchaseDate);

        _updatePurchaseBillNo = intent.getStringExtra("_updatePurchaseBillNo");
        _updatePurchasePoNo = intent.getIntExtra("_updatePurchasePoNo", 0);
        _updatePurchaseVendorID = intent.getIntExtra("_updatePurchaseVendorID", 0);
        _updatePurchaseVendorName = intent.getStringExtra("_updatePurchaseVendorName");
        _updatePurchaseRemark = intent.getStringExtra("_updatePurchaseRemark");

        txt_purchase_date = findViewById(R.id.txt_purchase_date);

        txt_purchase_date.setText(ClsGlobal.getEntryDateAndTime());
        txt_purchase_date.setTag(ClsGlobal.getEntryDateAndTime());

        txt_spn_vendor = findViewById(R.id.txt_spn_vendor);
        edt_bill_no = findViewById(R.id.edt_bill_no);
        edt_po_no = findViewById(R.id.edt_po_no);
        edt_remark = findViewById(R.id.edt_remark);
        btn_next = findViewById(R.id.btn_next);


        btn_add_bill = findViewById(R.id.btn_add_bill);

        txt_purchase_date.setOnClickListener(v -> {
            BeforeDate();
        });

        txt_spn_vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VendorList();
            }
        });

        fillVendorList();
        ClsGlobal.purchaseImgPathLst.clear();

        if (!_purchaseFlag.equalsIgnoreCase("AddNewPurchase")) {
            fillItemPhotos();
        }

        if (_updatePurchaseID != 0 && _purchaseFlag.equalsIgnoreCase("purchaseUpdate")) {

            txt_purchase_date.setText(ClsGlobal.getDDMMYYYY(_updatePurchaseDate));
            txt_purchase_date.setText(ClsGlobal.getDDMYYYYAndTimeAM_And_PMFormat(_updatePurchaseDate));

            edt_bill_no.setText(_updatePurchaseBillNo);
            edt_po_no.setText(String.valueOf(_updatePurchasePoNo));
            txt_spn_vendor.setText(_updatePurchaseVendorName);
            edt_remark.setText(_updatePurchaseRemark);
        }

        btn_add_bill.setOnClickListener(v -> {
            Intent intent12 = new Intent(getApplicationContext(), PurchaseImagePreviewActivity.class);
            intent12.putExtra("_imgMode", "purchase");
            intent12.putExtra("_purchaseFlag", _purchaseFlag);
            startActivity(intent12);
        });

        btn_next.setOnClickListener(v -> {


            if (txt_purchase_date.getText() == null || txt_purchase_date.getText().toString().trim().isEmpty()) {
                Toast.makeText(RetailPurchaseActivity.this, "Purchase date is required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (txt_spn_vendor.getText() == null || txt_spn_vendor.getText().toString().trim().isEmpty()) {
                Toast.makeText(RetailPurchaseActivity.this, "Vendor name is required", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent1 = new Intent(getApplicationContext(), PurchaseItemListActivity.class);

            intent1.putExtra("date", ClsGlobal.getChangeDateFormatDB(txt_purchase_date.getTag().toString()));
            intent1.putExtra("vendor_name", txt_spn_vendor.getText().toString());
            intent1.putExtra("_purchaseFlag", _purchaseFlag);

            if (_purchaseFlag.equalsIgnoreCase("AddNewPurchase")) {
                intent1.putExtra("_ID", _ID);
                intent1.putExtra("_vendorId", VendorID);
            } else {
                intent1.putExtra("_ID", _updatePurchaseID);
                intent1.putExtra("_vendorId", _updatePurchaseVendorID);
            }

            intent1.putExtra("bill_no", edt_bill_no.getText().toString());
            intent1.putExtra("po_no", edt_po_no.getText().toString());
            intent1.putExtra("remark", edt_remark.getText().toString());

            startActivity(intent1);
            finish();

        });

    }


    List<ClsMultipleImg> lstImgList = new ArrayList<>();
//    public static List<Uri> lstImgDataBaseURI = new ArrayList<>();

    private void fillItemPhotos() {
        lstImgList = new ArrayList<>();
        lstImgList = new ClsMultipleImg().getImageByPurchaseID(_updatePurchaseID,
                RetailPurchaseActivity.this);


//        ClsGlobal.purchaseImgPathLst.clear();

        for (ClsMultipleImg filePath : lstImgList) {
            File file = new File(filePath.getFile_Path());
            if (file.exists()) {
                Log.d("--Invoice--", "filepath: " + filePath.getFile_Path());


//                String getDirectoryPath = file.getParent();
//                Log.d("--Invoice--", "Folder: " + getDirectoryPath);

                ClsGlobal.purchaseImgPathLst.add(Uri.fromFile(new File(filePath.getFile_Path())));


            }
        }


    }

    private void VendorList() {

        final Dialog dialogVendor = new Dialog(RetailPurchaseActivity.this);
        dialogVendor.setContentView(R.layout.retail_dialog_spinner);
        dialogVendor.setTitle("Select Vendor");
        dialogVendor.setCanceledOnTouchOutside(true);
        dialogVendor.setCancelable(true);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogVendor.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogVendor.getWindow().setAttributes(lp);

        lst = dialogVendor.findViewById(R.id.list);
        edit_search = dialogVendor.findViewById(R.id.edit_search);
        img_clear = dialogVendor.findViewById(R.id.img_clear);
        lyout_nodata = dialogVendor.findViewById(R.id.lyout_nodata);

        vendorStickyAdapter = new VendorStickyAdapter(
                RetailPurchaseActivity.this);
        lst.setAdapter(vendorStickyAdapter);

        fillVendorList();
        img_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility();
                edit_search.setText("");
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (lstClsVendorMasters != null && lstClsVendorMasters.size() != 0) {
                        lst.setVisibility(View.VISIBLE);
                        lyout_nodata.setVisibility(View.GONE);
                        vendorStickyAdapter.AddItems(lstClsVendorMasters);
                        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                dialogVendor.dismiss();
                                ClsVendor objClsVendorMaster = lstClsVendorMasters.get(i);

                                VendorID = objClsVendorMaster.getVendor_id();
                                Log.d("HandlerVendor", "VendorID-- " + VendorID);

                                _VendorName = objClsVendorMaster.getVendor_name();
                                Log.d("HandlerVendor", "_VendorName-- " + _VendorName);

                                txt_spn_vendor.setText(_VendorName);

                            }
                        });
                        edit_search.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                // filter your list from your input
                                //you can use runnable postDelayed like 500 ms to delay search text

                                if (s != null) {

                                    String where = "";

                                    where = " AND ([CONTACT_NO] LIKE '%".concat(String.valueOf(s)).concat("%'");
                                    where = where.concat(" OR [VENDOR_NAME] LIKE '%".concat(String.valueOf(s)).concat("%') "));
                                    Log.e("where", where);

                                    lstClsVendorMasters = new ClsVendor(getApplicationContext())
                                            .getList(where +
                                                    " AND [ACTIVE] = 'YES' AND [Type] IN ('SUPPLIER','BOTH') " +
                                                    " ORDER BY [VENDOR_NAME]");

                                    if (lstClsVendorMasters != null & lstClsVendorMasters.size() > 0) {
                                        lyout_nodata.setVisibility(View.GONE);
                                        lst.setVisibility(View.VISIBLE);
                                        vendorStickyAdapter.AddItems(lstClsVendorMasters);
                                    } else {
                                        lst.setVisibility(View.GONE);
                                        lyout_nodata.setVisibility(View.VISIBLE);
                                    }

                                }

                            }
                        });
                    } else {
                        lst.setVisibility(View.GONE);
                        lyout_nodata.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                }
            }
        }, 400);
        dialogVendor.show();
    }

/*
    private void VendorList() {

        final Dialog dialogVendor = new Dialog(RetailPurchaseActivity.this);
        dialogVendor.setContentView(R.layout.retail_dialog_spinner);
        dialogVendor.setTitle("Select Vendor");
        dialogVendor.setCanceledOnTouchOutside(true);
        dialogVendor.setCancelable(true);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogVendor.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogVendor.getWindow().setAttributes(lp);

        lst = dialogVendor.findViewById(R.id.list);
        edit_search = dialogVendor.findViewById(R.id.edit_search);
        img_clear = dialogVendor.findViewById(R.id.img_clear);
        lyout_nodata = dialogVendor.findViewById(R.id.lyout_nodata);

        img_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility();
                edit_search.setText("");
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (lstClsVendorMasters != null && lstClsVendorMasters.size() != 0) {
                        vendorStickyAdapter = new VendorStickyAdapter(lstClsVendorMasters, RetailPurchaseActivity.this);
                        lst.setAdapter(vendorStickyAdapter);

                        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                dialogVendor.dismiss();
                                ClsVendor objClsVendorMaster = lstClsVendorMasters.get(i);

                                VendorID = objClsVendorMaster.getVendor_id();
                                Log.d("HandlerVendor", "VendorID-- " + VendorID);

                                _VendorName = objClsVendorMaster.getVendor_name();
                                Log.d("HandlerVendor", "_VendorName-- " + _VendorName);

                                txt_spn_vendor.setText(_VendorName);
                                lstClsVendorMasters = listVendorSearch;
                            }
                        });

                        edit_search.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                // filter your list from your input
                                //you can use runnable postDelayed like 500 ms to delay search text

                                if (s != null && !s.toString().isEmpty() && s.toString() != "") {
                                    filter(s.toString());
                                }

                            }
                        });
                    }
                } catch (Exception e) {
                }
            }
        }, 400);
        dialogVendor.show();
    }
*/

    void setVisibility() {
        if (edit_search.getText() != null && edit_search.getText().toString().length() != 0) {
            img_clear.setVisibility(View.VISIBLE);
        } else {
            img_clear.setVisibility(View.GONE);
        }
    }

/*
    private void fillVendorList() {
        lstClsVendorMasters = new ArrayList<>();
        listVendorSearch = lstClsVendorMasters = new ClsVendor(getApplicationContext()).getList(" AND [ACTIVE]='YES' ORDER BY [VENDOR_NAME]");
    }*/


    private void fillVendorList() {
        lstClsVendorMasters = new ArrayList<>();
        listVendorSearch = lstClsVendorMasters = new ClsVendor(getApplicationContext()).getList(" " +
                "AND [ACTIVE] = 'YES' AND [Type] IN ('SUPPLIER','BOTH') ORDER BY [VENDOR_NAME]");
    }

    private void BeforeDate() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(RetailPurchaseActivity.this,
                (view, year, month, day) -> {
                    c.set(year, month, day);
                    String date = new SimpleDateFormat("dd/MM/yyyy hh:mm aa").format(c.getTime());
                    txt_purchase_date.setText(date);
                    txt_purchase_date.setTag(date);
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
        Calendar d = Calendar.getInstance();
        dpd.updateDate(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH));
        dpd.show();
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
