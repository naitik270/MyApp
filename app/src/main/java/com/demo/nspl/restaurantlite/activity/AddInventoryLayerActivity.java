package com.demo.nspl.restaurantlite.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.nspl.restaurantlite.Adapter.InventoryLayerAdapter;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsInventoryLayer;
import com.google.gson.Gson;

import java.util.List;

public class AddInventoryLayerActivity extends AppCompatActivity {

    private EditText edit_layer_name, edit_display_order, edit_remark;
    private TextView txtdropdown_category;
    private Button btnSave;
    RadioButton rbYES, rbNO;
    List<ClsInventoryLayer> list;

    int getId;
    Dialog dialog;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventory_layer);

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edit_layer_name = findViewById(R.id.edit_layer_name);
        txtdropdown_category = findViewById(R.id.txtdropdown_category);
        edit_display_order = findViewById(R.id.edit_display_order);
        edit_remark = findViewById(R.id.edit_remark);
        rbYES = findViewById(R.id.rbYES);
        rbNO = findViewById(R.id.rbNO);
        btnSave = findViewById(R.id.btnSave);


        getId = getIntent().getIntExtra("ID", 0);
        Log.e("getId", String.valueOf(getId));

        if (getId != 0) {
            ClsInventoryLayer getCurrentObj = ClsInventoryLayer.QueryById(getId, AddInventoryLayerActivity.this);

            edit_layer_name.setText(getCurrentObj.getInventoryLayerName());
            txtdropdown_category.setTag(getCurrentObj.getInventoryLayerCategory());
            txtdropdown_category.setText(String.valueOf(getCurrentObj.getInventoryLayerCategoryName()));
            edit_display_order.setText(String.valueOf(getCurrentObj.getDisplayOrder()));
            edit_remark.setText(getCurrentObj.getRemark());

            Log.e("ACTIVE", getCurrentObj.getActive());
            if (getCurrentObj.getActive().equalsIgnoreCase("NO")) {
                rbNO.setChecked(true);
            } else if (getCurrentObj.getActive().equalsIgnoreCase("YES")) {
                rbYES.setChecked(true);
            }

        }

        btnSave.setOnClickListener(v -> {

            if (validation()) {
                String where = " AND  [LAYER_NAME] = "
                        .concat("'")
                        .concat(edit_layer_name.getText().toString().toUpperCase())
                        .concat("' ");

                if (getId != 0) {
                    where = where.concat(" AND [INVENTORYLAYER_ID] <> ").concat(String.valueOf(getId));
                }
                ClsInventoryLayer Obj = new ClsInventoryLayer(AddInventoryLayerActivity.this);
                boolean exists = Obj.checkExists(where);

                if (getId != 0) {

                    if (!exists) {
                        ClsInventoryLayer currentobj = new ClsInventoryLayer();

                        currentobj.setINVENTORYLAYER_ID(getId);
                        currentobj.setInventoryLayerName(edit_layer_name.getText().toString().toUpperCase().trim());

                        if (!txtdropdown_category.getText().toString().equals("")) {
                            Log.e("txtdropdown_category", txtdropdown_category.getTag().toString().trim());
                            currentobj.setInventoryLayerCategory(Integer.valueOf(txtdropdown_category.getTag().toString().trim()));
                            currentobj.setInventoryLayerCategoryName(txtdropdown_category.getText().toString().trim());
                        } else {
                            currentobj.setInventoryLayerCategory(0);
                            currentobj.setInventoryLayerCategoryName("");
                        }

                        currentobj.setDisplayOrder(Integer.parseInt(edit_display_order.getText().toString().equals("")
                                ? "0" : edit_display_order.getText().toString().trim()));
                        currentobj.setActive(rbYES.isChecked() ? "YES" : "NO");
                        currentobj.setRemark(edit_remark.getText().toString().equals("")
                                ? "" : edit_remark.getText().toString().trim());

                        int getResult = ClsInventoryLayer.Update(currentobj, getApplication());
                        if (getResult > 0) {
                            Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Error While Updating! ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Layer Name Already Exist! ", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    if (!exists) {

                        ClsInventoryLayer currentobj = new ClsInventoryLayer();
                        currentobj.setInventoryLayerName(edit_layer_name.getText().toString().toUpperCase().trim());

                        if (!txtdropdown_category.getText().toString().equals("")) {
                            Log.e("txtdropdown_category", txtdropdown_category.getTag().toString().trim());
                            currentobj.setInventoryLayerCategory(Integer.valueOf(txtdropdown_category.getTag().toString().trim()));
                            currentobj.setInventoryLayerCategoryName(txtdropdown_category.getText().toString().trim());
                        } else {
                            currentobj.setInventoryLayerCategory(0);
                            currentobj.setInventoryLayerCategoryName("");
                        }

                        currentobj.setDisplayOrder(Integer.parseInt(edit_display_order.getText().toString().equals("")
                                ? "0" : edit_display_order.getText().toString().trim()));
                        currentobj.setActive(rbYES.isChecked() ? "YES" : "NO");
                        currentobj.setRemark(edit_remark.getText().toString().equals("")
                                ? "" : edit_remark.getText().toString().trim());

                        int getResult = ClsInventoryLayer.Insert(currentobj, AddInventoryLayerActivity.this);

                        if (getResult > 0) {
                            Toast.makeText(this, "Save Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Error While Saving! ", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(this, "Layer Name Already Exist! ", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        txtdropdown_category.setOnClickListener(v -> {

            list = ClsInventoryLayer.getInventoryLayerList(AddInventoryLayerActivity.this);

            Gson gson = new Gson();
            String jsonInString = gson.toJson(list);
            Log.e("jsonInString:-- ", jsonInString);

            ListAdapter adapter = new InventoryLayerAdapter(
                    getApplicationContext(), R.layout.dialog_inventorylayer, list);

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("Select Inventory Layer");
            dialogBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    ClsInventoryLayer get = list.get(item);
                    Log.e("get", String.valueOf(get.getINVENTORYLAYER_ID()));
                    txtdropdown_category.setText(String.valueOf(get.getInventoryLayerName()));
                    txtdropdown_category.setTag(get.getINVENTORYLAYER_ID());
                }
            });
            //Create alert dialog object via builder
            AlertDialog alertDialogObject = dialogBuilder.create();
            //Show the dialog
            alertDialogObject.show();

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

    private boolean validation() {
        Boolean result = true;
        //branch validation
        if (edit_layer_name.getText() == null ||
                edit_layer_name.getText().toString().isEmpty()) {
            edit_layer_name.setError("Layer Name Required");
            //    edit_layer_name.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            edit_layer_name.requestFocus();
            return false;
        } else {
            edit_layer_name.setError(null);
        }


        return result;
    }
}
