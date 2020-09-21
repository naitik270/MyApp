package com.demo.nspl.restaurantlite.activity;

import android.os.Bundle;
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
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsExpenseType;

import java.util.Objects;

public class AddExpenseTypeActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText edit_expense_type_name, edit_sort_no, edit_remark;
    RadioGroup rg;
    RadioButton rbYES, rbNO;
    Button btnSave;
    private int result;
    TextView expense_type_ID;
    int _ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense_type);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "AddExpenseTypeActivity"));
        }

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        edit_expense_type_name = findViewById(R.id.edit_expense_type_name);
        edit_sort_no = findViewById(R.id.edit_sort_no);
        edit_remark = findViewById(R.id.edit_remark);
        expense_type_ID = findViewById(R.id.expense_type_ID);
        rg = findViewById(R.id.rg);
        rbYES = findViewById(R.id.rbYES);
        rbNO = findViewById(R.id.rbNO);
        btnSave = findViewById(R.id.btnSave);

        _ID = getIntent().getIntExtra("ID", 0);

        if (_ID != 0) {
            ClsExpenseType objExpenseType = new ClsExpenseType(AddExpenseTypeActivity.this);
            objExpenseType.setExpense_type_id(_ID);
            objExpenseType = objExpenseType.getObject(objExpenseType);
            expense_type_ID.setText(String.valueOf(objExpenseType.getExpense_type_id()));
            edit_expense_type_name.setText(objExpenseType.getExpense_type_name());
            edit_remark.setText(objExpenseType.getRemark());
            edit_sort_no.setText("0");
            if (objExpenseType.getActive().equalsIgnoreCase("NO")) {
                rbNO.setChecked(true);
            } else if (objExpenseType.getActive().equalsIgnoreCase("YES")) {
                rbYES.setChecked(true);
            }
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validation()) {
                    String where = " AND  [EXPENSE_TYPE_NAME] = "
                            .concat("'")
                            .concat(edit_expense_type_name.getText().toString().trim())
                            .concat("' ");

                    if (!expense_type_ID.getText().toString().isEmpty()) {
                        where = where.concat(" AND [EXPENSE_TYPE_ID] <> ").concat(expense_type_ID.getText().toString());
                    }

                    ClsExpenseType Obj = new ClsExpenseType(AddExpenseTypeActivity.this);
                    boolean exists = Obj.checkExists(where);
                    if (!exists) {
                        Obj.setExpense_type_id(expense_type_ID.getText() == null || expense_type_ID.getText().toString().isEmpty() ? 0 : Integer.parseInt(expense_type_ID.getText().toString()));
                        Obj.setExpense_type_name(edit_expense_type_name.getText().toString().trim());
                        Obj.setSort_no(0);
                        Obj.setActive(rbYES.isChecked() ? "YES" : "NO");
                        Obj.setRemark(edit_remark.getText().toString().trim());

                        if (Obj.getExpense_type_id() != 0) {
                            result = ClsExpenseType.Update(Obj);
                            if (result == 1) {
                                finish();
                            }
                        } else {
                            result = ClsExpenseType.Insert(Obj);
                            if (result == 1) {
                                finish();
                            }
                        }
                    } else {
                        Toast toast = Toast.makeText(AddExpenseTypeActivity.this, "ExpenseType already exists....", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                        return;
                    }

                }
            }

            private boolean validation() {
                boolean result = true;
                if (edit_expense_type_name.getText() == null ||
                        edit_expense_type_name.getText().toString().isEmpty()) {
                    edit_expense_type_name.setError("ExpenseType is required");
                    edit_expense_type_name.requestFocus();
                    return false;
                } else {
                    edit_expense_type_name.setError(null);
                }
                return result;
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
