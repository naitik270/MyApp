package com.demo.nspl.restaurantlite.Purchase;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.A_Test.Tools;
import com.demo.nspl.restaurantlite.BarcodeClasses.BarcodeReaderActivity;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.MultipleImage.ClsMultipleImg;
import com.demo.nspl.restaurantlite.Navigation_Drawer.FilterDialogFragment;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsLayerItemMaster;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class PurchaseItemListActivity extends AppCompatActivity {

    Integer _ID;
    String date = "", vendor_name = "", bill_no = "", remark = "", po_no = "";
    int VendorId = 0;
    TextView txt_billNO, txt_date, txt_vendor_name, txt_nodata;
    public static final int DIALOG_QUEST_CODE = 300;
    String itemName = "", qty = "", unitPrice = "";
    RecyclerView rv;
    CoordinatorLayout cc_layout;
    FloatingActionButton fab, fab_done, fab_scan;
    ClsPurchaseMaster objClsPurchaseMaster = new ClsPurchaseMaster();
    private int result;

    String _purchaseFlag = "";
    String _whereSearch = "";
    String _barcodeResult = "";
    public static List<ClsPurchaseDetail> lstClsProductListList = new ArrayList<>();
    ClsLayerItemMaster clsLayerItemMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_item_list);

        main();

        getList();
    }

    private void main() {
        Intent intent = getIntent();

        _ID = intent.getIntExtra("_ID", 0);

        VendorId = intent.getIntExtra("_vendorId", 0);
        date = intent.getStringExtra("date");

        Log.d("--date--", "main: " + date);
//        Log.d("--date--", "FabBtn: " + ClsGlobal.getDbDateFormat(date));

        vendor_name = intent.getStringExtra("vendor_name");
        bill_no = intent.getStringExtra("bill_no");
        po_no = intent.getStringExtra("po_no");
        _purchaseFlag = intent.getStringExtra("_purchaseFlag");
        remark = intent.getStringExtra("remark");
        itemName = intent.getStringExtra("itemName");
        qty = intent.getStringExtra("qty");
        unitPrice = intent.getStringExtra("unitPrice");

        cc_layout = findViewById(R.id.cc_layout);
        txt_nodata = findViewById(R.id.txt_nodata);
        txt_date = findViewById(R.id.txt_date);
        txt_date.setText(ClsGlobal.getEntryDateFormat(date));

        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        Log.d("--ClsMultipleImg--", "_ID:- " + _ID);
//        Log.d("--ClsMultipleImg--", "_ID:- " + _ID);

        fab_scan = findViewById(R.id.fab_scan);
        txt_billNO = findViewById(R.id.txt_billNO);

        if (bill_no != null && bill_no != "") {
            txt_billNO.setText(bill_no);
        } else {
            txt_billNO.setText("-");
        }

        txt_vendor_name = findViewById(R.id.txt_vendor_name);
        txt_vendor_name.setText(vendor_name);

//        fillItemPhotos();


        fab_done = findViewById(R.id.fab_done);
        fab_done.setColorFilter(Color.WHITE);

        fab_done.setOnClickListener(v -> {
            if (lstClsProductListList != null && lstClsProductListList.size() != 0) {
                txt_nodata.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);

                objClsPurchaseMaster.setPurchaseDate(date);

                objClsPurchaseMaster.setBillNO(bill_no);
                objClsPurchaseMaster.setVendorID(VendorId);

                if (po_no == "" || po_no == null || po_no.isEmpty()) {
                    po_no = "0";
                }

                objClsPurchaseMaster.setPurchaseNo(Integer.parseInt(po_no));
                objClsPurchaseMaster.setRemark(remark);
                objClsPurchaseMaster.setEntryDate(ClsGlobal.getEntryDateTime());

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PurchaseItemListActivity.this);
                alertDialog.setTitle("Confirm ?");
                alertDialog.setIcon(R.drawable.ic_done_all_black);
                alertDialog.setMessage("Sure to save ?");

                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        insertPurchase(objClsPurchaseMaster);

                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });
                // Showing Alert Message
                alertDialog.show();

            } else {
                Toast.makeText(getApplicationContext(), "NO RECORD FOUND", Toast.LENGTH_LONG).show();
                txt_nodata.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
            }
        });

        fab = findViewById(R.id.fab);
        fab.setColorFilter(Color.WHITE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Tools.setSystemBarColor(this, R.color.colorPrimaryDark);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
                intent.putExtra("_ID", _ID);
                intent.putExtra("VendorId", VendorId);
                intent.putExtra("date", date);
                intent.putExtra("vendor_name", vendor_name);
                intent.putExtra("bill_no", bill_no);
                intent.putExtra("po_no", po_no);
                intent.putExtra("remark", remark);
                startActivity(intent);


            }
        });


        lstClsProductListList = new ClsPurchaseDetail(getApplicationContext()).getPurchaseItemList(" AND PD.[PurchaseID] ="
                .concat(String.valueOf(_ID)), getApplicationContext());


        Gson gson = new Gson();
        String jsonInString = gson.toJson(lstClsProductListList);
        Log.d("--Gson--", "PurchaseDetails: " + jsonInString);


        fab_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                launchBarCodeActivity();
            }
        });

    }

    @Override
    public void onActivityResult(int _requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(_requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(getApplicationContext(), "No details found.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (_requestCode == 1208 && data != null) {
            Barcode barcode = data.getParcelableExtra(BarcodeReaderActivity.KEY_CAPTURED_BARCODE);
            _barcodeResult = barcode.rawValue;


            _whereSearch = _whereSearch.concat(" AND [ITM].[ITEM_CODE] = ").concat("'").concat(_barcodeResult).concat("'");
            clsLayerItemMaster = new ClsLayerItemMaster();
            clsLayerItemMaster = clsLayerItemMaster.getItem(getApplication(), "", _whereSearch);
            _whereSearch = "";

            Gson gson = new Gson();
            String jsonInString = gson.toJson(clsLayerItemMaster);
            Log.d("--GSON--", "clsLayerItemMaster: " + jsonInString);

        }

        if (clsLayerItemMaster.getITEM_CODE() == null || clsLayerItemMaster.getITEM_CODE().isEmpty()) {
            Toast.makeText(getApplicationContext(), "NO ITEM FOUND", Toast.LENGTH_SHORT).show();
            return;
        } else {
            MediaPlayer mp = MediaPlayer.create(this, R.raw.beep);
            mp.start();
            Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(100);
            itemClick(clsLayerItemMaster);

            cc_layout.setVisibility(View.GONE);
        }
    }


    void itemClick(ClsLayerItemMaster _objItemMaster) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FullScreenDailog newFragment = new FullScreenDailog();
//            newFragment.setRequestCode(clsLayerItemMaster,_ID, date, billNo, VendorId, vendor_name, bill_no, po_no, remark);
        newFragment.setProductList(_objItemMaster, "scan");

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
        newFragment.setOnCallbackResult(new FilterDialogFragment.CallbackResult() {
            @Override
            public void sendResult(int requestCode) {
                if (requestCode == DIALOG_QUEST_CODE) {
                    Log.e("requestCode", String.valueOf(requestCode));
                    cc_layout.setVisibility(View.VISIBLE);
                }
            }
        });
//        cc_layout.setVisibility(View.INVISIBLE);

    }

    private void launchBarCodeActivity() {
        Intent launchIntent = BarcodeReaderActivity.getLaunchIntent(getApplicationContext(), true, false);
        startActivityForResult(launchIntent, 1208);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("--log--", "onResume: ");
        getList();
    }

    void insertPurchase(ClsPurchaseMaster objClsPurchaseMaster) {
        if (_ID == 0) {

            result = ClsPurchaseMaster.Insert(objClsPurchaseMaster, lstClsProductListList);

            if (result <= -1) {
                Toast.makeText(getApplicationContext(), "Technical Error", Toast.LENGTH_SHORT).show();
            } else if (result <= 0) {
                Toast.makeText(getApplicationContext(), "Data Not Inserted", Toast.LENGTH_SHORT).show();
            } else {
                lstClsProductListList.clear();
//                ClsGlobal.purchaseImgPathLst.clear();
                Log.d("--static--", "After: " + ClsGlobal.purchaseImgPathLst.size());


                finish();
                Toast.makeText(getApplicationContext(), "Record Saved successfully", Toast.LENGTH_SHORT).show();
            }


        } else {
            ClsPurchaseMaster.getPurchaseDetailListDelete(_ID);
            result = ClsPurchaseMaster.Insert(objClsPurchaseMaster, lstClsProductListList);

            if (result <= -1) {
                Toast.makeText(getApplicationContext(), "Technical Error", Toast.LENGTH_SHORT).show();
            } else if (result <= 0) {
                Toast.makeText(getApplicationContext(), "Data Not Inserted", Toast.LENGTH_SHORT).show();
            } else {
                lstClsProductListList.clear();

//                ClsGlobal.purchaseImgPathLst.clear();

                Log.d("--static--", "After: " + ClsGlobal.purchaseImgPathLst.size());
                finish();
                Toast.makeText(getApplicationContext(), "Record Saved successfully", Toast.LENGTH_SHORT).show();
            }

        }

        Log.d("--ClsMultipleImg--", "_ID:- " + _ID);

        if (_ID > 0) {
            ClsMultipleImg.DeleteByPurchaseID(_ID);
        }

        InsertMultipleImg();

    }

    void deleteLastFolder() {
        String _path = "";
        for (Uri filePath : ClsGlobal.purchaseImgPathLst) {
            if (filePath != null) {
                _path = filePath.toString();
            }

            File file = new File(_path);

            String getDirectoryPath = file.getParent().replace("file:", "");

            deleteRecursive(new File(getDirectoryPath));

            if (file.isDirectory()) {
                String[] children = file.list();
                for (int i = 0; i < children.length; i++) {
                    new File(getDirectoryPath, children[i]).delete();
                }
            }
        }
    }

    public void InsertMultipleImg() {
        String _folderName = "";
        int _purchaseId = 0;
        List<String> srcDir = new ArrayList<>();

        try {

            Gson gson = new Gson();
            String jsonInString = gson.toJson(ClsGlobal.purchaseImgPathLst);
            Log.e("--Dialog--", "InsertMultipleImg /: " + jsonInString);

            for (Uri uri : ClsGlobal.purchaseImgPathLst) {
                srcDir.add(ClsGlobal.getPathFromUri(PurchaseItemListActivity.this, uri));
            }

            Log.d("--Invoice--", "Step1_srcDir: " + srcDir);

            if (srcDir != null && srcDir.size() > 0) {
                File _saveLocation = Environment.getExternalStorageDirectory();
                Log.d("--Invoice--", "Step2_saveLocation: " + _saveLocation);


                _folderName = ClsGlobal.PurchaseImageDirectory;

                Log.d("--Invoice--", "Step3_folderName: " + _folderName);

                if (ClsGlobal.lastPurchaseID == 0) {
                    _purchaseId = _ID;
                } else {
                    _purchaseId = ClsGlobal.lastPurchaseID;
                }

                Log.d("--Invoice--", "Step4_purchaseId: " + _purchaseId);

                File dir = new File(_saveLocation.getAbsolutePath() + "/"
                        + ClsGlobal.AppFolderName
                        + "/"
                        + _folderName
                        + "/"
                        + String.valueOf(_purchaseId)
                        .concat("/"));

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                ArrayList<String> uniques = new ArrayList<String>(srcDir);
                List<String> newPath = new ArrayList<>();

                HashSet<String> hashSet = new HashSet<String>();
                hashSet.addAll(uniques);
                uniques.clear();
                uniques.addAll(hashSet);

                for (String _filePath : uniques) {
                    File src = new File(_filePath);

                    Log.d("--Invoice--", "Step5_src: " + src);

                    if (src.exists()) {

                        String _copyFileName = ClsGlobal.getRandom() + "_.jpg";
                        String dstFile = "".concat(dir.getAbsolutePath()).concat("/").concat(_copyFileName);
                        File _copy = new File(dstFile);

                        ClsGlobal.copyFileTemp(src.getAbsolutePath(), _copy.getAbsolutePath());
                        newPath.add(_copy.getPath());
                    }
                }


                int result = 0;
                for (String _filePath : newPath) {
                    ClsMultipleImg obj = new ClsMultipleImg(PurchaseItemListActivity.this);

                    if (ClsGlobal.lastPurchaseID == 0) {
                        obj.setPurchase_id(_ID);
                    } else {
                        obj.setPurchase_id(ClsGlobal.lastPurchaseID);
                    }

                    obj.setItem_code("");
                    obj.setDocument_Name("");
                    obj.setDocument_No(bill_no); // Invoice No#
                    obj.setFile_Path(_filePath);
                    obj.setType("PURCHASE");
                    obj.setFile_Name(new File(_filePath).getName());
                    result = ClsMultipleImg.Insert(obj);
                }

                if (result == -1) {
                    Toast.makeText(getApplicationContext(), "Technical Error", Toast.LENGTH_SHORT).show();
                } else if (result == 0) {
                    Toast.makeText(getApplicationContext(), "Data Not Inserted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Photo save successfully.", Toast.LENGTH_SHORT).show();
                    deleteLastFolder();
                    finish();
                    ClsGlobal.purchaseImgPathLst.clear();


                }

                Log.d("--Invoice--", "InsertResult:- " + result);
            }


        } catch (Exception e) {
            Log.d("--path--", "Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }


    void getList() {

//
//        lstClsProductListList = new ClsPurchaseMaster(getApplicationContext()).getPurchaseItemList(" AND PD.[PurchaseID] ="
//                .concat(String.valueOf(_ID)), getApplicationContext());


        if (_purchaseFlag.equalsIgnoreCase("AddNewPurchase")) {
            if (lstClsProductListList != null && lstClsProductListList.size() != 0) {

                rv.setVisibility(View.VISIBLE);
                txt_nodata.setVisibility(View.GONE);

                ItemSelectAdapter itemSelectAdapter = new ItemSelectAdapter(PurchaseItemListActivity.this,
                        lstClsProductListList);

                rv.setAdapter(itemSelectAdapter);
                itemSelectAdapter.notifyDataSetChanged();

            } else {
                txt_nodata.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
            }
        } else if (_purchaseFlag.equalsIgnoreCase("purchaseUpdate")) {


            rv.setVisibility(View.VISIBLE);
            txt_nodata.setVisibility(View.GONE);


            ItemSelectAdapter itemSelectAdapter = new ItemSelectAdapter(PurchaseItemListActivity.this,
                    lstClsProductListList);
            rv.setAdapter(itemSelectAdapter);
            itemSelectAdapter.notifyDataSetChanged();

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("--log--", "onBackPressed: ");

        getList();
        cc_layout.setVisibility(View.VISIBLE);


    }


    void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();

    }

}
