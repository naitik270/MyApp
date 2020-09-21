package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsCategory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddCategoryActivity extends AppCompatActivity {


    //regionDeclaration
    Toolbar toolbar;
    EditText edit_cat_name, edit_sort_no, edit_remark;
    RadioButton rbYES, rbNO;
    Button btnSave;
    static final int DATE_DIALOG_ID = 0;
    private int result;
    private ProgressDialog pd;
    TextView cat_ID;
    int _ID;
    //endregion


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "AddCategoryActivity"));
        }

        toolbar = findViewById(R.id.toolbar);


            toolbar.setTitle("Category");


        edit_cat_name = findViewById(R.id.edit_cat_name);
        edit_sort_no = findViewById(R.id.edit_sort_no);
        edit_remark = findViewById(R.id.edit_remark);
        cat_ID = findViewById(R.id.cat_ID);
        rbYES = findViewById(R.id.rbYES);
        rbNO = findViewById(R.id.rbNO);
        btnSave = findViewById(R.id.btnSave);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "Modules"));
        }

        _ID = getIntent().getIntExtra("ID", 0);

        Log.e("TEST", String.valueOf(_ID));

        if (_ID != 0) {
            ClsCategory objCategory = new ClsCategory(AddCategoryActivity.this);
            objCategory.setCat_id(_ID);
            objCategory = objCategory.getObject(objCategory);
            cat_ID.setText(String.valueOf(objCategory.getCat_id()));
            Log.e("Cat", String.valueOf(cat_ID));
            edit_cat_name.setText(objCategory.getCat_name());
            edit_remark.setText(objCategory.getRemark());
            edit_sort_no.setText(String.valueOf(objCategory.getSort_no()));

            Log.e("ACTIVE", objCategory.getActive());
            if (objCategory.getActive().equalsIgnoreCase("NO")) {
                rbNO.setChecked(true);
            } else if (objCategory.getActive().equalsIgnoreCase("YES")) {
                rbYES.setChecked(true);
            }
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()) {
                    showDialog(DATE_DIALOG_ID);
                    pd = new ProgressDialog(AddCategoryActivity.this);
                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pd.setMessage("Loading..");
                    pd.setIndeterminate(true);
                    pd.setCancelable(true);
                    pd.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            String where = " AND  [CATEGORY_NAME] = "
                                    .concat("'")
                                    .concat(edit_cat_name.getText().toString().trim())
                                    .concat("' ");

                            if (!cat_ID.getText().toString().isEmpty()) {
                                where = where.concat(" AND [CATEGORY_ID] <> ").concat(cat_ID.getText().toString());
                            }

                            ClsCategory Obj = new ClsCategory(AddCategoryActivity.this);
                            boolean exists = Obj.checkExists(where);
                            if (!exists) {
                                Obj.setCat_id(cat_ID.getText() == null || cat_ID.getText().toString().isEmpty() ? 0 : Integer.parseInt(cat_ID.getText().toString()));
                                Obj.setCat_name(edit_cat_name.getText().toString().trim());
                                Obj.setSort_no(edit_sort_no.getText() == null || edit_sort_no.getText().toString().isEmpty() ? null : Integer.parseInt(edit_sort_no.getText().toString()));
                                Obj.setActive(rbYES.isChecked() ? "YES" : "NO");
                                Obj.setRemark(edit_remark.getText().toString().trim());


                                if (Obj.getCat_id() != 0) {
                                    result = ClsCategory.Update(Obj);

                                    if (result == 1) {
                                        ShowAlertDialog("Category updated successfully");

                                    }

                                } else {

                                    result = ClsCategory.Insert(Obj);
                                    Log.e("Result", String.valueOf(result));
                                    if (result == 1) {
                                        ShowAlertDialog("Category added successfully");

                                    }

                                }


                            } else {
                                Toast toast = Toast.makeText(AddCategoryActivity.this, "Category already exists....", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();
                                pd.dismiss();
                                return;
                            }
                            if (pd.isShowing()) {
                                pd.dismiss();
                            }
                        }
                    }, 3000);
                }

            }
        });

    }


    private boolean validation() {
        Boolean result = true;
        if (edit_cat_name.getText() == null ||
                edit_cat_name.getText().toString().isEmpty()) {
            edit_cat_name.setError("Category name is required");
         //   edit_cat_name.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            edit_cat_name.requestFocus();
            return false;
        } else {
            edit_cat_name.setError(null);
        }
        return result;
    }

    private void ShowAlertDialog(String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddCategoryActivity.this);
        final AlertDialog OptionDialog = builder.create();
        builder.setMessage(message);
        builder.setIcon(R.drawable.confirm);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
                OptionDialog.dismiss();
                OptionDialog.cancel();
            }
        });
        builder.show();
    }
}
