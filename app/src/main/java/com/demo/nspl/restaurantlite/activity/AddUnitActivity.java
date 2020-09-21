package com.demo.nspl.restaurantlite.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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
import com.demo.nspl.restaurantlite.Global.DatabaseHelper;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsUnit;


public class AddUnitActivity extends AppCompatActivity {

    private Toolbar toolbar;
    EditText edit_unit_name, edit_sort_no, edit_remark;
    RadioGroup rg;
    TextView unit_ID;
    RadioButton rbYES, rbNO;
    Button btnSave;
    int _ID;
    private int result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_unit);

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle("Add Unit");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        edit_unit_name = findViewById(R.id.edit_unit_name);
        edit_sort_no = findViewById(R.id.edit_sort_no);
        edit_remark = findViewById(R.id.edit_remark);
        unit_ID = findViewById(R.id.unit_ID);
        rg = findViewById(R.id.rg);
        rbYES = findViewById(R.id.rbYES);
        rbNO = findViewById(R.id.rbNO);
        btnSave = findViewById(R.id.btnSave);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "AddUnitActivity"));
        }
        _ID = getIntent().getIntExtra("ID", 0);

        if (_ID != 0) {
            ClsUnit objSize = new ClsUnit(AddUnitActivity.this);
            objSize.setUnit_id(_ID);
            objSize = objSize.getObject(objSize);

            unit_ID.setText(String.valueOf(objSize.getUnit_id()));
            edit_unit_name.setText(objSize.getUnit_name());
            edit_remark.setText(objSize.getRemark());
            edit_sort_no.setText(String.valueOf(objSize.getSort_no()));

            if (objSize.getActive().equalsIgnoreCase("NO")) {
                rbNO.setChecked(true);
            } else if (objSize.getActive().equalsIgnoreCase("YES")) {
                rbYES.setChecked(true);
            }
        }

        btnSave.setOnClickListener(view -> {

            if (validation()) {
                String where = " AND  [UNIT_NAME] = "
                        .concat("'")
                        .concat(edit_unit_name.getText().toString().toUpperCase())
                        .concat("' ");

                if (!unit_ID.getText().toString().isEmpty()) {
                    where = where.concat(" AND [UNIT_ID] <> ").concat(unit_ID.getText().toString());
                }

                ClsUnit Obj = new ClsUnit(AddUnitActivity.this);
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(AddUnitActivity.this);

                io.requery.android.database.sqlite.SQLiteDatabase db = databaseHelper
                        .openDatabase();

                boolean exists = Obj.checkExists(where, db);
                if (!exists) {

                    Obj.setUnit_id(unit_ID.getText() == null || unit_ID.getText().toString().isEmpty() ? 0 : Integer.parseInt(unit_ID.getText().toString()));
                    Obj.setUnit_name(edit_unit_name.getText().toString().toUpperCase().trim());
                    Obj.setSort_no(edit_sort_no.getText() == null || edit_sort_no.getText().toString().isEmpty() ? null : Integer.parseInt(edit_sort_no.getText().toString()));
                    Obj.setActive(rbYES.isChecked() ? "YES" : "NO");
                    Obj.setRemark(edit_remark.getText().toString().trim());

                    if (Obj.getUnit_id() != 0) {
                        result = ClsUnit.Update(Obj);
                        if (result == 1) {
                            finish();
                        }

                    } else {

                        io.requery.android.database.sqlite.SQLiteDatabase  db1 =
                                io.requery.android.database.sqlite.SQLiteDatabase
                                        .openOrCreateDatabase(
                                                getDatabasePath(ClsGlobal.Database_Name).getPath(),
                                null);

                        result = ClsUnit.Insert(Obj, db1);
                        Log.e("Result", String.valueOf(result));
                        if (result == 1) {
                            finish();
                        }
                        db1.close();
                    }
                    db.close();
                } else {
                    Toast toast = Toast.makeText(AddUnitActivity.this, "Unit already exists....", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    return;
                }
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

    private boolean validation() {
        Boolean result = true;
        //branch validation
        if (edit_unit_name.getText() == null ||
                edit_unit_name.getText().toString().isEmpty()) {
            edit_unit_name.setError("Unit name is required");
            //  edit_unit_name.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            edit_unit_name.requestFocus();
            return false;
        } else {
            edit_unit_name.setError(null);
        }

        return result;
    }
}
