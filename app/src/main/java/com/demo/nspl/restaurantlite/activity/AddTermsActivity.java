package com.demo.nspl.restaurantlite.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsTerms;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

public class AddTermsActivity extends AppCompatActivity {

    private EditText edit_Terms, edit_sort_no;
    private RadioButton rbYES, rbNO;
    private Button btnSave;
    private int result;
    private Toolbar mToolbar;
    private int get_Terms_id = 0;

    CheckBox rb_sale_invoice, rb_whole_sale, rb_q_terms, rb_chalan_terms;
    ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_terms);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "AddTermsActivity"));
        }

        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar != null) {

            setSupportActionBar(mToolbar);
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        get_Terms_id = getIntent().getIntExtra("Terms_id", 0);

        edit_Terms = findViewById(R.id.edit_Terms);
        edit_sort_no = findViewById(R.id.edit_sort_no);

        rb_sale_invoice = findViewById(R.id.rb_sale_invoice);
        rb_whole_sale = findViewById(R.id.rb_whole_sale);
        rb_q_terms = findViewById(R.id.rb_q_terms);
        rb_chalan_terms = findViewById(R.id.rb_chalan_terms);

        rbYES = findViewById(R.id.rbYES);
        rbNO = findViewById(R.id.rbNO);

        ViewData();

        btnSave = findViewById(R.id.btnSave);


        btnSave.setOnClickListener(view -> {
//            list.clear();
            if (edit_Terms.getText() == null || edit_Terms.getText().toString().trim().isEmpty()) {
                Toast.makeText(getBaseContext(), "Terms is required", Toast.LENGTH_SHORT).show();
                return;
            }

            ClsTerms currentObj = new ClsTerms();
            currentObj.setmTerms(edit_Terms.getText().toString().trim());
            if (edit_sort_no.getText().toString().matches("")) {
                currentObj.setmSort_No(0);
            } else {
                currentObj.setmSort_No(Integer.valueOf(edit_sort_no.getText().toString().trim()));
            }

            currentObj.setmActive(rbYES.isChecked() ? "YES" : "NO");

            String termType = "";


            if (rb_sale_invoice.isChecked()) {
                termType = "SALE INVOICE";
                list.add(termType);
            }

            if (rb_whole_sale.isChecked()) {
                termType = "WHOLESALE";
                list.add(termType);
            }

            if (rb_q_terms.isChecked()) {
                termType = "QUOTATION";

                list.add(termType);

            }

            if (rb_chalan_terms.isChecked()) {
                termType = "CHALLAN";
                list.add(termType);
            }


            Gson gson = new Gson();
            String jsonInString = gson.toJson(list);

            currentObj.setTERM_TYPE(jsonInString);
            Log.d("--currentObj--", "currentObj: " + jsonInString);

            Log.d("--currentObj--", "TextUtils: " + TextUtils.join(",", list));

            Gson gson1 = new Gson();
            String jsonInString1 = gson1.toJson(currentObj);
            Log.d("--currentObj--", "jsonInString1: " + jsonInString1);


            if (get_Terms_id == 0) {
                Log.i("get_Terms", "inside get Terms call");
                result = ClsTerms.Insert(currentObj);
                if (result != 0) {
                    Toast.makeText(this, "Terms Added Successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Error While Adding Terms", Toast.LENGTH_LONG).show();
                }

            } else if (get_Terms_id > 0) {
                currentObj.setmTerms_id(get_Terms_id);
                Log.i("get_Terms", "inside not get Terms call");
                result = ClsTerms.Update(currentObj);

                if (result > 0) {
                    Toast.makeText(this, "Terms Updated Successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Error While Updating Terms", Toast.LENGTH_LONG).show();
                }

            }

            finish();

        });

    }

    private void ViewData() {
        if (get_Terms_id != 0) {

            ClsTerms current_Obj = ClsTerms.QureyById(get_Terms_id);
            Log.i("Terms", current_Obj.getmTerms());

            edit_Terms.setText(String.valueOf(current_Obj.getmTerms()));
            edit_sort_no.setText(String.valueOf(current_Obj.getmSort_No()));

            String getActive = current_Obj.getmActive();

            if (getActive.equals("YES")) {
                rbYES.setChecked(true);
            } else if (getActive.equals("NO")) {
                rbNO.setChecked(true);
            }

            Log.e("Terms", "getTERM_TYPE: " + current_Obj.getTERM_TYPE());



            if (current_Obj.getTERM_TYPE() != null) {
                if (current_Obj.getTERM_TYPE().contains("SALE INVOICE")) {
                    rb_sale_invoice.setChecked(true);
                }
                if (current_Obj.getTERM_TYPE().contains("WHOLESALE")) {
                    rb_whole_sale.setChecked(true);
                }
                if (current_Obj.getTERM_TYPE().contains("QUOTATION")) {
                    rb_q_terms.setChecked(true);
                }
                if (current_Obj.getTERM_TYPE().contains("CHALLAN")) {
                    rb_chalan_terms.setChecked(true);
                }
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
