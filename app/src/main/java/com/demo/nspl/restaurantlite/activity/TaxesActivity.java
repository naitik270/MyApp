package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsTaxes;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class TaxesActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button btnSave;
    CheckBox chk1, chk2, chk3, chk4, chk5, other_chk1, other_chk2, other_chk3, other_chk4, other_chk5;
    EditText tax1, value1, tax2, value2, tax3, value3, tax4, value4, tax5, value5, other_tax1, other_tax2, other_tax3, other_tax5, other_tax4;
    //    View tax1Bg, val1Bg, tax2Bg, val2Bg, tax3Bg, val3Bg, tax4Bg, val4Bg, tax5Bg, val5Bg, other_v1, other_v2, other_v3, other_v4, other_v5;
    static final int DATE_DIALOG_ID = 0;
    private List<ClsTaxes> list_tax;
    TextView hide_scheme;
    int Sr = 0;
    int Sr_Otehrs = 0;
    TextInputLayout input_tax1, input_tax2;
    int chekedcount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxes);
        ClsGlobal.isFristFragment = true;

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Add Taxes");
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);


        input_tax1 = findViewById(R.id.input_tax1);
        input_tax2 = findViewById(R.id.input_tax2);
        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "TaxesActivity"));
        }

        btnSave = findViewById(R.id.btnSave);
        chk1 = findViewById(R.id.chk1);
        chk2 = findViewById(R.id.chk2);
        chk3 = findViewById(R.id.chk3);
        chk4 = findViewById(R.id.chk4);
        chk5 = findViewById(R.id.chk5);
        other_chk1 = findViewById(R.id.other_chk1);
        other_chk2 = findViewById(R.id.other_chk2);
        other_chk3 = findViewById(R.id.other_chk3);
        other_chk4 = findViewById(R.id.other_chk4);
        other_chk5 = findViewById(R.id.other_chk5);

        tax1 = findViewById(R.id.tax1);


        tax2 = findViewById(R.id.tax2);

        tax3 = findViewById(R.id.tax3);
        tax4 = findViewById(R.id.tax4);
        tax5 = findViewById(R.id.tax5);


        value1 = findViewById(R.id.value1);
        value2 = findViewById(R.id.value2);
        value3 = findViewById(R.id.value3);
        value4 = findViewById(R.id.value4);
        value5 = findViewById(R.id.value5);

        other_tax1 = findViewById(R.id.other_tax1);
        other_tax2 = findViewById(R.id.other_tax2);
        other_tax3 = findViewById(R.id.other_tax3);
        other_tax4 = findViewById(R.id.other_tax4);
        other_tax5 = findViewById(R.id.other_tax5);


        list_tax = new ArrayList<>();
        list_tax = new ClsTaxes(TaxesActivity.this).getList("");


        //regionGetList
        if (list_tax.size() != 0) {
            chekedcount = list_tax.size();
            for (ClsTaxes taxes : list_tax) {


                if (taxes.getTax_type().equalsIgnoreCase("Main")) {
                    Sr++;
                    if (Sr == 1) {
                        chk1.setChecked(true);
                        tax1.setText(taxes.getTax_name());
                        tax1.setEnabled(true);
                        value1.setEnabled(true);
                        tax1.requestFocus();
                        value1.requestFocus();
                        value1.setText(String.valueOf(taxes.getTax_value()));

                    }

                    if (Sr == 2) {
                        chk2.setChecked(true);
                        tax2.setText(taxes.getTax_name());
                        tax2.setEnabled(true);
                        value2.setEnabled(true);
                        value2.setText(String.valueOf(taxes.getTax_value()));
                    }

                    if (Sr == 3) {
                        chk3.setChecked(true);
                        tax3.setText(taxes.getTax_name());
                        tax3.setEnabled(true);
                        value3.setEnabled(true);
                        value3.setText(String.valueOf(taxes.getTax_value()));
                    }

                    if (Sr == 4) {
                        chk4.setChecked(true);
                        tax4.setText(taxes.getTax_name());
                        tax4.setEnabled(true);
                        value4.setEnabled(true);
                        value4.setText(String.valueOf(taxes.getTax_value()));
                    }
                    if (Sr == 5) {
                        chk5.setChecked(true);
                        tax5.setText(taxes.getTax_name());
                        tax5.setEnabled(true);
                        value5.setEnabled(true);
                        value5.setText(String.valueOf(taxes.getTax_value()));
                    }

                }

                if (taxes.getTax_type().equalsIgnoreCase("Other")) {
                    Sr_Otehrs++;

                    if (Sr_Otehrs == 1) {
                        other_chk1.setChecked(true);
                        other_tax1.setText(taxes.getTax_name());
                        other_tax1.setEnabled(true);
                    }

                    if (Sr_Otehrs == 2) {
                        other_chk2.setChecked(true);
                        other_tax2.setText(taxes.getTax_name());
                        other_tax2.setEnabled(true);
                    }

                    if (Sr_Otehrs == 3) {
                        other_chk3.setChecked(true);
                        other_tax3.setText(taxes.getTax_name());
                        other_tax3.setEnabled(true);
                    }

                    if (Sr_Otehrs == 4) {
                        other_chk4.setChecked(true);
                        other_tax4.setText(taxes.getTax_name());
                        other_tax4.setEnabled(true);
                    }
                    if (Sr_Otehrs == 5) {
                        other_chk5.setChecked(true);
                        other_tax5.setText(taxes.getTax_name());
                        other_tax5.setEnabled(true);
                    }
                }

            }
        }

        //endregion

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_DIALOG_ID);
                if (validation()) {

                    int result;

                    List<ClsTaxes> list_tax = new ArrayList<>();

                    ClsTaxes ObjTax = new ClsTaxes(TaxesActivity.this);

                    if (chk1.isChecked()) {
                        ObjTax = new ClsTaxes();
                        ObjTax.setTax_type("Main");
                        ObjTax.setTax_name(tax1.getText().toString().trim());
                        ObjTax.setTax_value(Double.valueOf(value1.getText().toString().trim()));
                        list_tax.add(ObjTax);
                    }

                    if (chk2.isChecked()) {
                        ObjTax = new ClsTaxes();
                        ObjTax.setTax_type("Main");
                        ObjTax.setTax_name(tax2.getText().toString().trim());
                        ObjTax.setTax_value(Double.valueOf(value2.getText().toString().trim()));
                        list_tax.add(ObjTax);
                    }

                    if (chk3.isChecked()) {
                        ObjTax = new ClsTaxes();
                        ObjTax.setTax_type("Main");
                        ObjTax.setTax_name(tax3.getText().toString().trim());
                        ObjTax.setTax_value(Double.valueOf(value3.getText().toString().trim()));
                        list_tax.add(ObjTax);
                    }

                    if (chk4.isChecked()) {
                        ObjTax = new ClsTaxes();
                        ObjTax.setTax_type("Main");
                        ObjTax.setTax_name(tax4.getText().toString().trim());
                        ObjTax.setTax_value(Double.valueOf(value4.getText().toString().trim()));
                        list_tax.add(ObjTax);
                    }

                    if (chk5.isChecked()) {
                        ObjTax = new ClsTaxes();

                        ObjTax.setTax_type("Main");
                        ObjTax.setTax_name(tax5.getText().toString().trim());
                        ObjTax.setTax_value(Double.valueOf(value5.getText().toString().trim()));
                        list_tax.add(ObjTax);
                    }

                    if (other_chk1.isChecked()) {
                        ObjTax = new ClsTaxes();

                        ObjTax.setTax_type("Other");
                        ObjTax.setTax_name(other_tax1.getText().toString().trim());
                        ObjTax.setTax_value(0.0);
                        list_tax.add(ObjTax);
                    }

                    if (other_chk2.isChecked()) {
                        ObjTax = new ClsTaxes();

                        ObjTax.setTax_type("Other");
                        ObjTax.setTax_name(other_tax2.getText().toString().trim());
                        ObjTax.setTax_value(0.0);
                        list_tax.add(ObjTax);
                    }

                    if (other_chk3.isChecked()) {
                        ObjTax = new ClsTaxes();

                        ObjTax.setTax_type("Other");
                        ObjTax.setTax_name(other_tax3.getText().toString().trim());
                        ObjTax.setTax_value(0.0);
                        list_tax.add(ObjTax);
                    }

                    if (other_chk4.isChecked()) {
                        ObjTax = new ClsTaxes();

                        ObjTax.setTax_type("Other");
                        ObjTax.setTax_name(other_tax4.getText().toString().trim());
                        ObjTax.setTax_value(0.0);
                        list_tax.add(ObjTax);
                    }

                    if (other_chk5.isChecked()) {
                        ObjTax = new ClsTaxes();

                        ObjTax.setTax_type("Other");
                        ObjTax.setTax_name(other_tax5.getText().toString().trim());
                        ObjTax.setTax_value(0.0);
                        list_tax.add(ObjTax);
                    }

                    result = ClsTaxes.Insert(list_tax);

                    if (result == 1) {
                        Intent intent = new Intent(TaxesActivity.this, SHAP_Lite_Activity.class);
                        startActivity(intent);
                    }

                }


            }
        });


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TaxesActivity.this, SHAP_Lite_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @SuppressLint("ResourceAsColor")
    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.chk1:

                if (checked) {
                    tax1.setEnabled(true);
                    chekedcount++;

                    tax1.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(tax1, InputMethodManager.SHOW_IMPLICIT);

//                    tax1Bg.setBackgroundColor(R.color.viewblue);
//                    val1Bg.setBackgroundColor(R.color.viewblue);
                    value1.setEnabled(true);
                } else {
                    chekedcount--;
                    tax1.setEnabled(false);
                    value1.setEnabled(false);

                    tax1.setText("");
                    value1.setText("");
                }
                break;
            case R.id.chk2:
                if (checked) {
                    chekedcount++;

                    tax2.setEnabled(true);
                    value2.setEnabled(true);
                    tax2.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(tax2, InputMethodManager.SHOW_IMPLICIT);

                } else {
                    chekedcount--;
                    tax2.setEnabled(false);
                    value2.setEnabled(false);

                    tax2.setText("");
                    value2.setText("");
                }
                break;

            case R.id.chk3:
                if (checked) {
                    chekedcount++;
                    tax3.setEnabled(true);
                    value3.setEnabled(true);
                    tax3.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(tax3, InputMethodManager.SHOW_IMPLICIT);

                } else {
                    chekedcount--;
                    tax3.setEnabled(false);
                    value3.setEnabled(false);

                    tax3.setText("");
                    value3.setText("");
                }
                break;
            case R.id.chk4:
                if (checked) {
                    chekedcount++;
                    tax4.setEnabled(true);
                    value4.setEnabled(true);
                    tax4.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(tax4, InputMethodManager.SHOW_IMPLICIT);

                } else {
                    chekedcount--;
                    tax4.setEnabled(false);
                    value4.setEnabled(false);

                    tax4.setText("");
                    value4.setText("");
                }

                break;

            case R.id.chk5:
                if (checked) {
                    chekedcount++;
                    tax5.setEnabled(true);
                    value5.setEnabled(true);
                    tax5.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(tax5, InputMethodManager.SHOW_IMPLICIT);

                } else {
                    chekedcount--;
                    tax5.setEnabled(false);
                    value5.setEnabled(false);

                    tax5.setText("");
                    value5.setText("");
                }

                break;

            case R.id.other_chk1:
                if (checked) {
                    other_tax1.setEnabled(true);
                    chekedcount++;
                    other_tax1.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(other_tax1, InputMethodManager.SHOW_IMPLICIT);

                } else {
                    other_tax1.setEnabled(false);
//                    other_v1.setBackgroundResource(R.color.red);
                    chekedcount--;
                    other_tax1.setText("");
                }

                break;

            case R.id.other_chk2:
                if (checked) {
                    other_tax2.setEnabled(true);
                    other_tax2.requestFocus();
                    chekedcount++;
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(other_tax2, InputMethodManager.SHOW_IMPLICIT);
//                    other_v2.setBackgroundColor(R.color.viewblue);

                } else {
                    other_tax2.setEnabled(false);
//                    other_v2.setBackgroundResource(R.color.red);
                    chekedcount--;
                    other_tax2.setText("");
                }

                break;

            case R.id.other_chk3:
                if (checked) {
                    other_tax3.setEnabled(true);
                    other_tax3.requestFocus();
                    chekedcount++;
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(other_tax3, InputMethodManager.SHOW_IMPLICIT);
//                    other_v3.setBackgroundColor(R.color.viewblue);

                } else {
                    other_tax3.setEnabled(false);
//                    other_v3.setBackgroundResource(R.color.red);
                    chekedcount--;
                    other_tax3.setText("");
                }

                break;

            case R.id.other_chk4:
                if (checked) {
                    other_tax4.setEnabled(true);
                    other_tax4.requestFocus();
                    chekedcount++;
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(other_tax4, InputMethodManager.SHOW_IMPLICIT);
//                    other_v4.setBackgroundColor(R.color.viewblue);

                } else {
                    other_tax4.setEnabled(false);
//                    other_v4.setBackgroundResource(R.color.red);
                    chekedcount--;
                    other_tax4.setText("");
                }

                break;

            case R.id.other_chk5:
                if (checked) {
                    other_tax5.setEnabled(true);
                    other_tax5.requestFocus();
                    chekedcount++;
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(other_tax5, InputMethodManager.SHOW_IMPLICIT);
//                    other_v5.setBackgroundColor(R.color.viewblue);

                } else {
                    other_tax5.setText("");
                    chekedcount--;
//                    other_v5.setBackgroundResource(R.color.red);
                    other_tax5.setEnabled(false);
                }
                break;
        }
    }

    private boolean validation() {
        Boolean result = true;


        if (chekedcount < 1) {
            Toast.makeText(this, "At least One Tax is required..", Toast.LENGTH_LONG).show();
            return false;
        }


        if (chk1.isChecked()) {
            if (tax1.getText() == null || tax1.getText().toString().isEmpty()) {
                Toast toast = Toast.makeText(TaxesActivity.this, "Tax 1 Name is required..", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();


                return false;
            }


            if (value1.getText() == null || value1.getText().toString().isEmpty()) {


                Toast toast = Toast.makeText(TaxesActivity.this, "Value 1 is required..", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

                return false;
            }

        } else {
            tax1.setError(null);
            value1.setError(null);
        }

        if (chk2.isChecked()) {
            if (tax2.getText() == null || tax2.getText().toString().isEmpty()) {


                Toast toast = Toast.makeText(TaxesActivity.this, "Tax 2 Name is required..", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                return false;
            }


            if (value2.getText() == null || value2.getText().toString().isEmpty()) {


                Toast toast = Toast.makeText(TaxesActivity.this, "Value 2 is required..", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                return false;
            }

        } else {
            tax2.setError(null);
            value2.setError(null);
        }

        if (chk3.isChecked()) {
            if (tax3.getText() == null || tax3.getText().toString().isEmpty()) {

                Toast toast = Toast.makeText(TaxesActivity.this, "Tax 3 Name is required..", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                return false;
            }


            if (value3.getText() == null || value3.getText().toString().isEmpty()) {

                Toast toast = Toast.makeText(TaxesActivity.this, "Value 3 is required..", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                return false;
            }

        } else {
            tax3.setError(null);
            value3.setError(null);
        }

        if (chk4.isChecked()) {
            if (tax4.getText() == null || tax4.getText().toString().isEmpty()) {


                Toast toast = Toast.makeText(TaxesActivity.this, "Tax 4 Name is required..", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                return false;
            }


            if (value4.getText() == null || value4.getText().toString().isEmpty()) {


                Toast toast = Toast.makeText(TaxesActivity.this, "Value 4 is required..", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                return false;
            }

        } else {
            tax4.setError(null);
            value4.setError(null);
        }

        if (chk5.isChecked()) {
            if (tax5.getText() == null || tax5.getText().toString().isEmpty()) {
                Toast toast = Toast.makeText(TaxesActivity.this, "Tax 5 Name is required..", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                return false;
            }


            if (value5.getText() == null || value5.getText().toString().isEmpty()) {
                Toast toast = Toast.makeText(TaxesActivity.this, "Value 5 is required..", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                return false;
            }

        } else {
            tax5.setError(null);
            value5.setError(null);
        }

        if (other_chk1.isChecked()) {
            if (other_tax1.getText() == null || other_tax1.getText().toString().isEmpty()) {


                Toast toast = Toast.makeText(TaxesActivity.this, "Tax Name is required..", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                return false;
            }


        } else {
            other_tax1.setError(null);

        }

        if (other_chk2.isChecked()) {
            if (other_tax2.getText() == null || other_tax2.getText().toString().isEmpty()) {
                Toast toast = Toast.makeText(TaxesActivity.this, "Tax Name is required..", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
//
                return false;
            }


        } else {
            other_tax2.setError(null);

        }

        if (other_chk3.isChecked()) {
            if (other_tax3.getText() == null || other_tax3.getText().toString().isEmpty()) {


                Toast toast = Toast.makeText(TaxesActivity.this, "Tax Name is required..", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

                return false;
            }


        } else {
            other_tax3.setError(null);

        }
        if (other_chk4.isChecked()) {
            if (other_tax4.getText() == null || other_tax4.getText().toString().isEmpty()) {


                Toast toast = Toast.makeText(TaxesActivity.this, "Tax Name is required..", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                return false;
            }


        } else {
            other_tax4.setError(null);

        }

        if (other_chk5.isChecked()) {
            if (other_tax5.getText() == null || other_tax5.getText().toString().isEmpty()) {
                Toast toast = Toast.makeText(TaxesActivity.this, "Tax Name is required..", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                return false;
            }


        } else {
            other_tax5.setError(null);


        }


        return result;
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
