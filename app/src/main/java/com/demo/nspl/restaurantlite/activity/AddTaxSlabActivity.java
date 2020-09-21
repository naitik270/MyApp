package com.demo.nspl.restaurantlite.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsTaxSlab;

import java.util.Objects;

public class AddTaxSlabActivity extends AppCompatActivity {

    private EditText edit_Tax_Slab_Name, edit_remark, edit_SGST, edit_IGST, edit_CGST;
    private RadioButton rbYES, rbNO;
    private Button btnSave;
    private int getUpdateId;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tax_slab);

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        edit_Tax_Slab_Name = findViewById(R.id.edit_Tax_Slab_Name);
        edit_SGST = findViewById(R.id.edit_SGST);
        edit_IGST = findViewById(R.id.edit_IGST);
        edit_CGST = findViewById(R.id.edit_CGST);

        edit_remark = findViewById(R.id.edit_remark);
        rbYES = findViewById(R.id.rbYES);
        rbNO = findViewById(R.id.rbNO);
        btnSave = findViewById(R.id.btnSave);

        getUpdateId = getIntent().getIntExtra("UpdateId", 0);

        if (getUpdateId != 0) {

            ClsTaxSlab getCurrent = ClsTaxSlab.QueryById(getUpdateId, getApplication());
            edit_Tax_Slab_Name.setText(getCurrent.getSLAB_NAME());

            if (getCurrent.getSGST() != null) {
                edit_SGST.setText(String.valueOf(getCurrent.getSGST()));
            } else {
                edit_SGST.setText(String.valueOf(0.0));
            }

            if (getCurrent.getCGST() != null) {
                edit_CGST.setText(String.valueOf(getCurrent.getCGST()));
            } else {
                edit_CGST.setText(String.valueOf(0.0));
            }

            if (getCurrent.getIGST() != null) {
                edit_IGST.setText(String.valueOf(getCurrent.getIGST()));
            } else {
                edit_IGST.setText(String.valueOf(0.0));
            }

            edit_remark.setText(getCurrent.getREMARK());

            if (getCurrent.getACTIVE().equalsIgnoreCase("NO")) {
                rbNO.setChecked(true);
            } else if (getCurrent.getACTIVE().equalsIgnoreCase("YES")) {
                rbYES.setChecked(true);
            }

        }

        btnSave.setOnClickListener(v -> {

            if (!edit_Tax_Slab_Name.getText().toString().equalsIgnoreCase("")) {

                if (getUpdateId == 0) {

                    ClsTaxSlab currentObjInsert = new ClsTaxSlab();

                    currentObjInsert.setSLAB_NAME(edit_Tax_Slab_Name.getText().toString().equalsIgnoreCase("")
                            ? "" : edit_Tax_Slab_Name.getText().toString().trim());

                    if (!TextUtils.isEmpty(edit_SGST.getText())) {
                        currentObjInsert.setSGST(Double.valueOf(edit_SGST.getText().toString().trim()));
                    } else {
                        currentObjInsert.setSGST(0.0);
                    }

                    if (!TextUtils.isEmpty(edit_CGST.getText())) {
                        currentObjInsert.setCGST(Double.valueOf(edit_CGST.getText().toString().trim()));
                    } else {
                        currentObjInsert.setCGST(0.0);
                    }

                    if (!TextUtils.isEmpty(edit_IGST.getText())) {
                        currentObjInsert.setIGST(Double.valueOf(edit_IGST.getText().toString().trim()));
                    } else {
                        currentObjInsert.setIGST(0.0);
                    }

                    currentObjInsert.setACTIVE(rbYES.isChecked() ? "YES" : "NO");
                    if (!edit_remark.getText().toString().equals("")) {
                        currentObjInsert.setREMARK(edit_remark.getText().toString().trim());
                    } else {
                        currentObjInsert.setREMARK("");
                    }

                    io.requery.android.database.sqlite.SQLiteDatabase
                            db = io.requery.android.database.sqlite.SQLiteDatabase
                            .openOrCreateDatabase(getDatabasePath(ClsGlobal.Database_Name).getPath(),
                    null);

                    int getResult = ClsTaxSlab.Insert(getApplication(), currentObjInsert, db);

                    Log.e("getResult", String.valueOf(getResult));
                    finish();
                    db.close();

                } else {
                    ClsTaxSlab currentObjUpdate = new ClsTaxSlab();

                    currentObjUpdate.setTaxSlabId(getUpdateId);
                    currentObjUpdate.setSLAB_NAME(edit_Tax_Slab_Name.getText().toString().equalsIgnoreCase("")
                            ? "" : edit_Tax_Slab_Name.getText().toString().trim());

                    if (!TextUtils.isEmpty(edit_SGST.getText())) {
                        currentObjUpdate.setSGST(Double.valueOf(edit_SGST.getText().toString().trim()));
                    } else {
                        currentObjUpdate.setSGST(0.0);
                    }

                    if (!TextUtils.isEmpty(edit_CGST.getText())) {
                        currentObjUpdate.setCGST(Double.valueOf(edit_CGST.getText().toString().trim()));
                    } else {
                        currentObjUpdate.setCGST(0.0);
                    }

                    if (!TextUtils.isEmpty(edit_IGST.getText())) {
                        currentObjUpdate.setIGST(Double.valueOf(edit_IGST.getText().toString().trim()));
                    } else {
                        currentObjUpdate.setIGST(0.0);
                    }

                    currentObjUpdate.setACTIVE(rbYES.isChecked() ? "YES" : "NO");
                    if (!edit_remark.getText().toString().equals("")) {
                        currentObjUpdate.setREMARK(edit_remark.getText().toString().trim());
                    } else {
                        currentObjUpdate.setREMARK("");
                    }

                    int getResult = ClsTaxSlab.Update(currentObjUpdate, getApplication());
                    Log.e("getResult", String.valueOf(getResult));
                    finish();
                }

            } else {

                edit_Tax_Slab_Name.setError("Tax Slab Name Required");
                edit_Tax_Slab_Name.requestFocus();
            }
        });
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
