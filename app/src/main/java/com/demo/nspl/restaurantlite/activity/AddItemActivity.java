package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.nspl.restaurantlite.Adapter.LayerNameAdapterNew;
import com.demo.nspl.restaurantlite.BarcodeClasses.BarcodeReaderActivity;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsInventoryLayer;
import com.demo.nspl.restaurantlite.classes.ClsLayerItemMaster;
import com.demo.nspl.restaurantlite.classes.ClsTaxSlab;
import com.demo.nspl.restaurantlite.classes.ClsUnit;
import com.demo.nspl.restaurantlite.classes.StringWithTag;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.gujun.android.taggroup.TagGroup;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import static com.demo.nspl.restaurantlite.Navigation_Drawer.ItemFragment.onResumeCall;

public class AddItemActivity extends AppCompatActivity {

    Toolbar toolbar;
    private EditText edit_inventory_item, edit_min, edit_max,
            edit_Remark, edit_ItemCode, edit_Rate_per_unit, Edit_Add_Tags,
            edit_display_order, edit_opening_stock;
    private TextView txtdropdown_category, display_tags, txt_Select_Tax_Slab, label_tax_slab;
    private RadioButton rbYES, rbNO, tax_Slab_YES, tax_Slab_NO;
    private RadioGroup tax_Slab;
    private Button btnSave, Add_Tags;


//    private RecyclerView rv;

    ListView lst_layers;


    private Spinner spinner;
    private LinearLayout hide_layout, ll_hide_tax_type;
    //    LayerNameAdapter cu;
    LayerNameAdapterNew layerNameAdapterNew;

    List<StringWithTag> lstStringWithTags = new ArrayList<StringWithTag>();
    private List<ClsTaxSlab> list;
    List<String> listTags;
    int SelectedTaxSlab_Id = 0;
    String SelectedTaxSlabName = "";
    List<ClsInventoryLayer> listLayerName;
    List<ClsUnit> listUnit;
    private Dialog dialog;
    int TaxSlabId = 0;
    private ClsUnit ObjUnit = new ClsUnit();
    StickyListHeadersListView stickyListview;
    private PopUpPageAdapter adapter;
    int getId;
    String Mode = "",auto_generated_code = "";
    static List<ClsInventoryLayer> listLayerNameUpdate = new ArrayList<>();
    TextView txt_no_layer;

    EditText edt_sca_code, edt_wholesale_rate;
    RadioButton rb_inclusive, rb_exclusive;
    ImageView iv_scan,iv_auto_generate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lst_layers = findViewById(R.id.lst_layers);


        txt_no_layer = findViewById(R.id.txt_no_layer);
        iv_scan = findViewById(R.id.iv_scan);
        edit_inventory_item = findViewById(R.id.edit_inventory_item);
        edit_min = findViewById(R.id.edit_min);
        edit_max = findViewById(R.id.edit_max);
        edit_Remark = findViewById(R.id.edit_Remark);
        Edit_Add_Tags = findViewById(R.id.Edit_Add_Tags);

        Add_Tags = findViewById(R.id.Add_Tags);
        edit_display_order = findViewById(R.id.edit_display_order);
        spinner = findViewById(R.id.spinner);
        tax_Slab_YES = findViewById(R.id.tax_Slab_YES);
        tax_Slab_NO = findViewById(R.id.tax_Slab_NO);
        txt_Select_Tax_Slab = findViewById(R.id.txt_Select_Tax_Slab);
        hide_layout = findViewById(R.id.hide_layout);
        ll_hide_tax_type = findViewById(R.id.ll_hide_tax_type);
        label_tax_slab = findViewById(R.id.label_tax_slab);
        tax_Slab = findViewById(R.id.tax_Slab);
        edit_opening_stock = findViewById(R.id.edit_opening_stock);

        edit_Rate_per_unit = findViewById(R.id.edit_Rate_per_unit);
        txtdropdown_category = findViewById(R.id.txtdropdown_category);
        edit_ItemCode = findViewById(R.id.edit_ItemCode);

        edt_sca_code = findViewById(R.id.edt_sca_code);

        edt_wholesale_rate = findViewById(R.id.edt_wholesale_rate);
        rb_inclusive = findViewById(R.id.rb_inclusive);
        rb_exclusive = findViewById(R.id.rb_exclusive);


        btnSave = findViewById(R.id.btnSave);

        rbYES = findViewById(R.id.rbYES);
        rbNO = findViewById(R.id.rbNO);
        iv_auto_generate = findViewById(R.id.iv_auto_generate);

        hide_layout.setVisibility(View.GONE);
        ll_hide_tax_type.setVisibility(View.GONE);
        /* rv = findViewById(R.id.rv);

         *//*LinearLayout.LayoutParams params = new
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // Set the height by params
        params.height=1700;

        // set height of RecyclerView
        rv.setLayoutParams(params);*//*
        rv.setLayoutManager(new LinearLayoutManager(getApplication()));*/

        txtdropdown_category.setText(ClsGlobal.getDefaultItem_Unit(AddItemActivity.this));

//        auto_generated_code = String.valueOf(ClsLayerItemMaster
//                .getAutoGeneratedItemCode(AddItemActivity.this));

        list = new ArrayList<>();

        tax_Slab.setOnCheckedChangeListener((group, checkedId) -> {

            if (tax_Slab_YES.isChecked()) {
                hide_layout.setVisibility(View.VISIBLE);
                ll_hide_tax_type.setVisibility(View.VISIBLE);
            } else {
                hide_layout.setVisibility(View.GONE);
                ll_hide_tax_type.setVisibility(View.GONE);
            }

        });

        list = new ClsTaxSlab(getApplication()).getTaxSlabNameList("AND [ACTIVE] = 'YES'");

        if (list.size() != 0) {
            SetUpSpinner(list);
        }

        getId = getIntent().getIntExtra("ID", 0);
        Mode = getIntent().getStringExtra("Mode");

        Log.e("getId", String.valueOf(getId));
        Log.e("list_size", String.valueOf(list.size()));


        listTags = new ArrayList<>();
        listLayerName = new ArrayList<>();
        listLayerName = ClsInventoryLayer.getAllListInventoryLayer(AddItemActivity.this);

        List<ClsInventoryLayer> itemLayers = ClsInventoryLayer.getEDITItemLayers(AddItemActivity.this, getId);
        if (itemLayers != null && itemLayers.size() != 0) {
            for (ClsInventoryLayer ObjLayer : listLayerName) {
                for (ClsInventoryLayer ObjItemLayer : itemLayers) {
                    if (ObjLayer.getInventoryLayerName().equalsIgnoreCase(ObjItemLayer.getInventoryLayerName())) {
                        ObjLayer.setLayerValue(ObjItemLayer.getLayerValue());
                        listLayerName.set(listLayerName.indexOf(ObjLayer), ObjLayer);
                    }
                }
            }
        }

        if (listLayerName != null && listLayerName.size() != 0) {

            lst_layers.setVisibility(View.VISIBLE);
            txt_no_layer.setVisibility(View.GONE);
//            listLayerNameUpdate.clear();
            listLayerNameUpdate = listLayerName;
            layerNameAdapterNew = new LayerNameAdapterNew(AddItemActivity.this, listLayerName);

            lst_layers.setAdapter(layerNameAdapterNew);
            layerNameAdapterNew.notifyDataSetChanged();

        } else {
            lst_layers.setVisibility(View.GONE);
            txt_no_layer.setVisibility(View.VISIBLE);
        }
        ClsGlobal.setDynamicHeight(lst_layers);


        TagGroup mTagGroup = findViewById(R.id.tag_group);
        mTagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddItemActivity.this);
                alertDialog.setTitle("Confirm Delete...");
                alertDialog.setMessage("Are you sure you want delete?");
                alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
                alertDialog.setPositiveButton("YES", (dialog, which) -> {

                    Log.e("tag", tag);
                    listTags.remove(listTags.indexOf(tag));
                    Log.e("list1", String.valueOf(listTags.indexOf(tag)));
                    Gson gson = new Gson();
                    String jsonInString = gson.toJson(listTags);
                    Log.e("ListTags:-- ", jsonInString);

                    mTagGroup.setTags(listTags);
                    mTagGroup.submitTag();

                });
                alertDialog.setNegativeButton("NO", (dialog, which) -> {
                    dialog.dismiss();
                    dialog.cancel();


                });
                alertDialog.show();

            }
        });


        iv_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchBarCodeActivity();
//                edit_ItemCode.setText(auto_generated_code);
            }
        });

        iv_auto_generate.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View v) {
                Observable.just(ClsLayerItemMaster
                        .getAutoGeneratedItemCode(AddItemActivity.this))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(nextItemCode -> {
                            auto_generated_code = String.valueOf(nextItemCode);
                            edit_ItemCode.setText(String.valueOf(nextItemCode));
                        });

            }
        });

        if (getId != 0) {

            listTags = ClsLayerItemMaster.getTagsList(AddItemActivity.this,
                    " AND [ITEMID] = " + getId);
            mTagGroup.setTags(listTags);
            mTagGroup.submitTag();

            ClsLayerItemMaster currentObj = ClsLayerItemMaster.QueryById(getId, getApplication());

            Gson gson1 = new Gson();
            String jsonInString1 = gson1.toJson(currentObj);
            Log.e("jsonInString:-- ", jsonInString1);


            edit_inventory_item.setText(currentObj.getITEM_NAME());
            edit_min.setText(String.valueOf(currentObj.getMIN_STOCK()));
            edit_max.setText(String.valueOf(currentObj.getMAX_STOCK()));
            edit_Remark.setText(currentObj.getREMARK());
            txtdropdown_category.setText(currentObj.getUNIT_CODE());
            edit_display_order.setText(String.valueOf(currentObj.getDISPLAY_ORDER()));
            edit_opening_stock.setText(String.valueOf(currentObj.getOpening_Stock()));
            edit_Rate_per_unit.setText(String.valueOf(currentObj.getRATE_PER_UNIT()));
            edit_ItemCode.setText(String.valueOf(currentObj.getITEM_CODE()));

            edt_sca_code.setText(currentObj.getHSN_SAC_CODE());

            edt_wholesale_rate.setText(String.valueOf(currentObj.getWHOLESALE_RATE()));

            List<ClsTaxSlab> current = new ClsTaxSlab(getApplication()).getTaxSlabNameList("AND [SLAB_ID] = " + String.valueOf(currentObj.getTAX_SLAB_ID()));
            for (ClsTaxSlab get : current) {
                setSpinText(spinner, get.getSLAB_NAME());
            }


            if (currentObj.getACTIVE() != null &&
                    currentObj.getACTIVE().equalsIgnoreCase("NO")) {
                rbNO.setChecked(true);
            } else if (currentObj.getACTIVE() != null
                    && currentObj.getACTIVE().equalsIgnoreCase("YES")) {
                rbYES.setChecked(true);
            }

            if (currentObj.getTAX_TYPE() != null) {
                if (currentObj.getTAX_TYPE().equalsIgnoreCase("EXCLUSIVE")) {
                    rb_exclusive.setChecked(true);
                } else if (currentObj.getTAX_TYPE().equalsIgnoreCase("INCLUSIVE")) {
                    rb_inclusive.setChecked(true);
                }
            }

            if (currentObj.getTAX_APPLY().equalsIgnoreCase("NO")) {
                tax_Slab_NO.setChecked(true);
            } else if (currentObj.getACTIVE().equalsIgnoreCase("YES")) {
                tax_Slab_YES.setChecked(true);
                if (hide_layout.getVisibility() == View.GONE) {
                    hide_layout.setVisibility(View.VISIBLE);
                }
                if (ll_hide_tax_type.getVisibility() == View.GONE) {
                    ll_hide_tax_type.setVisibility(View.VISIBLE);
                }
            }

            if (Mode != null && !Mode.isEmpty() && Mode.equalsIgnoreCase("copyItem")) {
                getId = 0;
            }
        }

        btnSave.setOnClickListener(v -> {

            if (validation()) {


                ClsLayerItemMaster currentObj = new ClsLayerItemMaster();
                String where = " AND  [ITEM_CODE] = "
                        .concat("'")
                        .concat(edit_ItemCode.getText().toString())
                        .concat("' ");

                if (getId != 0) {
                    where = where.concat(" AND [LAYERITEM_ID] <> ").concat("" + getId);
                }

                boolean exists = ClsLayerItemMaster.checkExists(where, AddItemActivity.this);
                Log.e("exists", String.valueOf(exists));
                if (!exists) {
                    Log.e("exists", "Inside !exists");


                    if (getId > 0) {
                        currentObj.setLAYERITEM_ID(getId);
                        currentObj.setITEM_NAME(edit_inventory_item.getText().toString().equals("")
                                ? "" : edit_inventory_item.getText().toString().trim());

                        currentObj.setITEM_CODE(edit_ItemCode.getText().toString().equals("")
                                ? "" : edit_ItemCode.getText().toString().trim());

                        currentObj.setRATE_PER_UNIT(Double.valueOf(edit_Rate_per_unit.getText().toString().equals("")
                                ? "0" : edit_Rate_per_unit.getText().toString().trim()));

                        currentObj.setTAGS(edit_Rate_per_unit.getText().toString().equals("")
                                ? "" : edit_Rate_per_unit.getText().toString().trim());

                        if (!edit_Remark.getText().toString().equals("")) {
                            currentObj.setREMARK(edit_Remark.getText().toString().trim());
                        } else {
                            currentObj.setREMARK("");
                        }

                        currentObj.setDISPLAY_ORDER(Integer.valueOf(edit_display_order.getText().toString().equals("")
                                ? "0" : edit_display_order.getText().toString().trim()));
                        currentObj.setOpening_Stock(Double.valueOf(edit_opening_stock.getText().toString().equals("")
                                ? "0" : edit_opening_stock.getText().toString().trim()));

                        currentObj.setMIN_STOCK(Double.valueOf(edit_min.getText().toString().equals("")
                                ? "0" : edit_min.getText().toString().trim()));
                        currentObj.setMAX_STOCK(Double.valueOf(edit_max.getText().toString().equals("")
                                ? "0" : edit_max.getText().toString().trim()));

                        currentObj.setTAGS("");
                        currentObj.setACTIVE(rbYES.isChecked() ? "YES" : "NO");
                        currentObj.setUNIT_CODE(txtdropdown_category.getText().toString().equalsIgnoreCase("")
                                ? "" : txtdropdown_category.getText().toString().trim());

                        currentObj.setHSN_SAC_CODE(edt_sca_code.getText().toString().equals("")
                                ? "" : edt_sca_code.getText().toString().trim());

                        currentObj.setTAX_APPLY(tax_Slab_YES.isChecked() ? "YES" : "NO");

                        currentObj.setWHOLESALE_RATE(Double.valueOf(edt_wholesale_rate.getText().toString().equals("")
                                ? "0" : edt_wholesale_rate.getText().toString().trim()));

                        if (tax_Slab_YES.isChecked()) {
                            currentObj.setTAX_SLAB_ID(getSpinnerValueID(spinner.getSelectedItemPosition()));
                        } else {
                            currentObj.setTAX_SLAB_ID(0);
                        }

                        if (tax_Slab_YES.isChecked()) {
                            currentObj.setTAX_TYPE(rb_inclusive.isChecked() ? "INCLUSIVE" : "EXCLUSIVE");
                        } else {
                            currentObj.setTAX_TYPE("");
                        }

                        if (!edit_ItemCode.getText().toString().trim().equals(auto_generated_code)){
                            auto_generated_code = "";
                            currentObj.setAUTO_GENERATE_ITEMCODE(auto_generated_code);
                        }else {

                            currentObj.setAUTO_GENERATE_ITEMCODE(auto_generated_code);
                        }

                        int getResult = ClsLayerItemMaster.Update(currentObj, listTags, listLayerNameUpdate, AddItemActivity.this);

                        if (getResult != 0) {

                            onResumeCall = true;


                            Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Error While Updating!", Toast.LENGTH_SHORT).show();
                        }
                    } else {


                        Log.e("exists", "OutSide !exists");
                        ClsLayerItemMaster currentObjInsert = new ClsLayerItemMaster();

                        currentObjInsert.setITEM_NAME(edit_inventory_item.getText().toString().equals("")
                                ? "" : edit_inventory_item.getText().toString().trim());

                        currentObjInsert.setITEM_CODE(edit_ItemCode.getText().toString().equals("")
                                ? "" : edit_ItemCode.getText().toString().trim());
                        currentObjInsert.setRATE_PER_UNIT(Double.valueOf(edit_Rate_per_unit.getText().toString()));

                        currentObjInsert.setTAGS(edit_Rate_per_unit.getText().toString().equals("")
                                ? "" : edit_Rate_per_unit.getText().toString().trim());

                        if (!edit_Remark.getText().toString().equals("")) {
                            currentObjInsert.setREMARK(edit_Remark.getText().toString().trim());
                        } else {
                            currentObjInsert.setREMARK("");
                        }

                        currentObjInsert.setDISPLAY_ORDER(Integer.valueOf(edit_display_order.getText().toString().equals("")
                                ? "0" : edit_display_order.getText().toString().trim()));
                        currentObjInsert.setOpening_Stock(Double.valueOf(edit_opening_stock.getText().toString().equals("")
                                ? "0" : edit_opening_stock.getText().toString().trim()));
                        currentObjInsert.setMIN_STOCK(Double.valueOf(edit_min.getText().toString().equals("")
                                ? "0" : edit_min.getText().toString().trim()));
                        currentObjInsert.setMAX_STOCK(Double.valueOf(edit_max.getText().toString().equals("")
                                ? "0" : edit_max.getText().toString().trim()));
                        currentObjInsert.setTAGS("");
                        currentObjInsert.setACTIVE(rbYES.isChecked() ? "YES" : "NO");
                        currentObjInsert.setUNIT_CODE(txtdropdown_category.getText().toString().equalsIgnoreCase("")
                                ? "" : txtdropdown_category.getText().toString().trim());

                        currentObjInsert.setHSN_SAC_CODE(edt_sca_code.getText().toString().equals("")
                                ? "" : edt_sca_code.getText().toString().trim());

                        currentObjInsert.setTAX_APPLY(tax_Slab_YES.isChecked() ? "YES" : "NO");

                        if (tax_Slab_YES.isChecked()) {
                            currentObjInsert.setTAX_SLAB_ID(getSpinnerValueID(spinner.getSelectedItemPosition()));
                        } else {
                            currentObjInsert.setTAX_SLAB_ID(0);
                        }

                        currentObjInsert.setWHOLESALE_RATE(Double.valueOf(edt_wholesale_rate.getText().toString().equals("")
                                ? "0" : edt_wholesale_rate.getText().toString().trim()));

                        if (tax_Slab_YES.isChecked()) {
                            currentObjInsert.setTAX_TYPE(rb_inclusive.isChecked() ? "INCLUSIVE" : "EXCLUSIVE");
                        } else {
                            currentObjInsert.setTAX_TYPE("");
                        }
                        if (!edit_ItemCode.getText().toString().trim().equals(auto_generated_code)){
                            auto_generated_code = "";
                            currentObjInsert.setAUTO_GENERATE_ITEMCODE(auto_generated_code);
                        }else {

                            currentObjInsert.setAUTO_GENERATE_ITEMCODE(auto_generated_code);
                        }

                        int getResultInsert = ClsLayerItemMaster.Insert(currentObjInsert
                                , listTags
                                , listLayerNameUpdate
                                , AddItemActivity.this);

                        if (getResultInsert >= 0) {

                            onResumeCall = true;

                            Toast.makeText(this, "Save Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Error While Saving!", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(this, "Duplicates Inventory Item!", Toast.LENGTH_SHORT).show();
                }


            }

        });

        Add_Tags.setOnClickListener(v -> {

            if (!Edit_Add_Tags.getText().toString().equals("")) {
                if (listTags.contains(Edit_Add_Tags.getText().toString())) {
                    Toast.makeText(this, "Duplicates Tags Not Allow!", Toast.LENGTH_SHORT).show();
                } else {
                    listTags.add(Edit_Add_Tags.getText().toString().trim());
                    mTagGroup.setTags(listTags);
                    mTagGroup.submitTag();
                    Edit_Add_Tags.setText("");
                }


            } else {
                Toast.makeText(this, "Please Enter Tags!", Toast.LENGTH_SHORT).show();
            }


        });

        txtdropdown_category.setOnClickListener(v1 -> {

            listUnit = new ArrayList<>();
            listUnit = new ClsUnit(AddItemActivity.this).getDialogList(" AND [ACTIVE]='YES' ORDER BY [UNIT_NAME] ASC");

            if (listUnit != null && listUnit.size() != 0) {
                dialog = new Dialog(AddItemActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.sticky_header_popup);
                dialog.setCancelable(true);

                final ClsUnit Obj = new ClsUnit(AddItemActivity.this);
                stickyListview = dialog.findViewById(R.id.stickyListview);

                adapter = new PopUpPageAdapter(AddItemActivity.this, listUnit);
                stickyListview.setAdapter(adapter);
                dialog.show();
            } else {
                Toast.makeText(AddItemActivity.this, "There is No Units!", Toast.LENGTH_LONG).show();
            }

        });

    }

    private void launchBarCodeActivity() {
        Intent launchIntent = BarcodeReaderActivity.getLaunchIntent(getApplicationContext(), true, false);
        startActivityForResult(launchIntent, 1208);
    }

    @Override
    public void onActivityResult(int _requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(_requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(getApplicationContext(), "error in  scanning", Toast.LENGTH_SHORT).show();
            return;
        }

        if (_requestCode == 1208 && data != null) {
            Barcode barcode = data.getParcelableExtra(BarcodeReaderActivity.KEY_CAPTURED_BARCODE);
            edit_ItemCode.setText(barcode.rawValue);
            MediaPlayer mp = MediaPlayer.create(this, R.raw.beep);
            mp.start();
            Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(100);
        }
    }

    private boolean validation() {
        Boolean result = true;
        //branch validation
        if (edit_inventory_item.getText() == null ||
                edit_inventory_item.getText().toString().isEmpty()) {
            edit_inventory_item.setError("Item name Required");
            //           edit_inventory_item.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            edit_inventory_item.requestFocus();
            return false;
        } else {
            edit_inventory_item.setError(null);
        }

        if (edit_ItemCode.getText() == null ||
                edit_ItemCode.getText().toString().isEmpty()) {
            edit_ItemCode.setError("Item Code Required");
            //          edit_ItemCode.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            edit_ItemCode.requestFocus();

            return false;
        } else {
            edit_ItemCode.setError(null);
        }

        if (txtdropdown_category.getText() == null ||
                txtdropdown_category.getText().toString().isEmpty()) {

            Toast.makeText(this, "Select Unit", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edit_Rate_per_unit.getText() == null ||
                edit_Rate_per_unit.getText().toString().isEmpty()) {
            edit_Rate_per_unit.setError("Rate per Unit Required");

            return false;
        } else {
            edit_Rate_per_unit.setError(null);
        }

        if (tax_Slab_YES.isChecked()) {

            if (spinner.getSelectedItem() == null || spinner.getSelectedItemPosition() == 0) {
                Toast.makeText(AddItemActivity.this, "Tax slab is required", Toast.LENGTH_SHORT).show();

                hide_layout.setFocusable(true);
                hide_layout.setFocusableInTouchMode(true);

                ll_hide_tax_type.setFocusable(true);
                ll_hide_tax_type.setFocusableInTouchMode(true);


                hide_layout.requestFocus();
                spinner.requestFocus();
                ll_hide_tax_type.requestFocus();
                rb_inclusive.requestFocus();

                return false;
            }
        }

        if (tax_Slab_YES.isChecked()) {
            if (edt_sca_code.getText() == null ||
                    edt_sca_code.getText().toString().isEmpty()) {
                Toast.makeText(this, "HSN code is required", Toast.LENGTH_SHORT).show();
                edt_sca_code.requestFocus();
                return false;
            }
        }


        return result;
    }

    class PopUpPageAdapter extends BaseAdapter implements StickyListHeadersAdapter {

        Context context;

        List<ClsUnit> list_unit = new ArrayList<>();
        private LayoutInflater inflater;

        public PopUpPageAdapter(Context context, List<ClsUnit> list) {
            this.list_unit = list;
            inflater = LayoutInflater.from(context);
        }


        @Override
        public long getHeaderId(int position) {
            return list_unit.get(position).getUnit_name().subSequence(0, 1).charAt(0);
        }

        @Override
        public int getCount() {
            return list_unit.size();
        }

        @Override
        public Object getItem(int position) {
            return list_unit.get(position);
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

            ClsUnit objCategory = list_unit.get(position);


            stickyListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int Popid = (int) id;
                    txtdropdown_category.setText(list_unit.get(position).getUnit_name());
                    ClsGlobal.setDefaultItem_Unit(AddItemActivity.this,
                            list_unit.get(position).getUnit_name());

                    ObjUnit = list_unit.get(position);
                    dialog.dismiss();
                    Log.e("PopID", String.valueOf(Popid));


                }
            });
            holder.text.setText(objCategory.getUnit_name());


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

            ClsUnit clsPageMaster = list_unit.get(position);
            String headerText = "" + clsPageMaster.getUnit_name().subSequence(0, 1).charAt(0);
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

    public static void updateList(List<ClsInventoryLayer> layerList) {
        listLayerNameUpdate = layerList;
    }

    private void SetUpSpinner(List<ClsTaxSlab> list) {

        if (list != null && list.size() != 0) {
            ClsTaxSlab obj = new ClsTaxSlab();
            obj.setTaxSlabId(0);
            obj.setSLAB_NAME("SELECT");
            list.add(0, obj);

            for (ClsTaxSlab objClsRoomTypeMaster : list) {
                lstStringWithTags.add(new StringWithTag(objClsRoomTypeMaster.getSLAB_NAME(),
                        String.valueOf(objClsRoomTypeMaster.getTaxSlabId())));
            }
        }

        ArrayAdapter<StringWithTag> dataAdapter = new ArrayAdapter<StringWithTag>(
                this, R.layout.spinner_item,
                lstStringWithTags) {

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);

                return view;
            }
        };
        dataAdapter
                .setDropDownViewResource(R.layout.spinner_item);

        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SelectedTaxSlab_Id = list.get(i).getTaxSlabId();
                SelectedTaxSlabName = list.get(i).getSLAB_NAME();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                return;
            }
        });

    }


    int getSpinnerValueID(int selectedItemPosition) {
        int _selectedID = 0;
        StringWithTag _Obj = lstStringWithTags.get(selectedItemPosition);
        Log.e("_Obj", " StringWithTag : " + _Obj);
        _selectedID = Integer.parseInt(_Obj.tag.toString());
        Log.e("_selectedID", " _selectedID : " + _selectedID);
        return _selectedID;
    }

    public void setSpinText(Spinner spin, String text) {
        for (int i = 0; i < spin.getAdapter().getCount(); i++) {
            if (spin.getAdapter().getItem(i).toString().contains(text)) {
                spin.setSelection(i);
            }
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
