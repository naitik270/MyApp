package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsEmployee;
import com.demo.nspl.restaurantlite.classes.ImageFilePath;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class AddEmployeeActivity extends AppCompatActivity {

    EditText edit_employee_name, edit_contact_no, edit_address, edit_salary, edit_sort_no, edit_remark;
    TextView edit_dob;
    Button btnSave;

    Toolbar toolbar;
    TextView employee_ID;
    RadioButton rbYES, rbNO;
    static final int DATE_DIALOG_ID = 0;
    private int result;
    int _ID;
    private Uri selectedImageUri;
    private File imgFile;
    private String selectedImagePath = "";
    private static final int SELECT_PICTURE = 1;
    int flag = 0;
    String receiptDate = "";
    String radioVal = "", formattedDate, upt = "0:0:0";
    private int hour, minute;
    private ClsEmployee objEmployee;


    ImageButton iv_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "AddEmployeeActivity"));
        }

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        edit_employee_name = findViewById(R.id.edit_employee_name);
        edit_contact_no = findViewById(R.id.edit_contact_no);
        edit_address = findViewById(R.id.edit_address);
        edit_sort_no = findViewById(R.id.edit_sort_no);
        edit_salary = findViewById(R.id.edit_salary);
        employee_ID = findViewById(R.id.employee_ID);
        edit_dob = findViewById(R.id.edit_dob);
        rbYES = findViewById(R.id.rbYES);
        rbNO = findViewById(R.id.rbNO);
        btnSave = findViewById(R.id.btnSave);
        edit_remark = findViewById(R.id.edit_remark);
        iv_clear = findViewById(R.id.iv_clear);

        _ID = getIntent().getIntExtra("ID", 0);

        if (_ID != 0) {
            objEmployee = new ClsEmployee(AddEmployeeActivity.this);
            objEmployee.setEmployee_id(_ID);
            objEmployee = objEmployee.getObject(objEmployee);

            employee_ID.setText(String.valueOf(objEmployee.getEmployee_id()));
            edit_employee_name.setText(objEmployee.getEmployee_name());
            edit_contact_no.setText(objEmployee.getContact_no());
            edit_address.setText(objEmployee.getAddress());
            edit_salary.setText(String.valueOf(objEmployee.getSalary()));
            edit_sort_no.setText("0");
            edit_remark.setText(objEmployee.getRemark());
            edit_dob.setText(objEmployee.getDob());

            if (objEmployee.getActive().equalsIgnoreCase("NO")) {
                rbNO.setChecked(true);
            } else if (objEmployee.getActive().equalsIgnoreCase("YES")) {
                rbYES.setChecked(true);
            }

        }

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = df.format(c.getTime());

        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        upt = updateTime(hour, minute);

        edit_dob.setOnClickListener(view -> {
            flag = 1;
            new DatePickerDialog(AddEmployeeActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        });

        iv_clear.setOnClickListener(v -> edit_dob.setText(""));

        btnSave.setOnClickListener(view -> {
            if (validation()) {
                String where = " AND  [CONTACT_NO] = "
                        .concat("'")
                        .concat(edit_contact_no.getText().toString().trim())
                        .concat("' ");

                if (!employee_ID.getText().toString().isEmpty()) {
                    where = where.concat(" AND [EMPLOYEE_ID] <> ").concat(employee_ID.getText().toString());
                }

                ClsEmployee Obj = new ClsEmployee(AddEmployeeActivity.this);
                boolean exists = Obj.checkExists(where);
                if (!exists) {
                    Obj.setEmployee_id(employee_ID.getText() == null || employee_ID.getText().toString().isEmpty() ? 0 : Integer.parseInt(employee_ID.getText().toString()));
                    Obj.setEmployee_name(edit_employee_name.getText().toString().trim());
                    Obj.setContact_no(edit_contact_no.getText().toString().trim());
                    Obj.setAddress(edit_address.getText().toString().trim());
                    Obj.setSalary(Double.valueOf(edit_salary.getText().toString().trim()));
                    Obj.setSort_no(0);
                    Obj.setActive(rbYES.isChecked() ? "YES" : "NO");
                    Obj.setDob(edit_dob.getText().toString().trim());
                    Obj.setRemark(edit_remark.getText().toString().trim());

                    if (Obj.getEmployee_id() != 0) {
                        result = ClsEmployee.Update(Obj);
                        if (result == 1) {
                            finish();
                        }
                    } else {
                        result = ClsEmployee.Insert(Obj);
                        if (result == 1) {
                            finish();
                        }
                    }
                } else {
                    Toast toast = Toast.makeText(AddEmployeeActivity.this, "Employee already exists....", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    return;
                }
            }
        });

    }


    @SuppressLint("MissingSuperCall")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_PICTURE) {
                try {
                    String filePATH = "";
                    selectedImageUri = data.getData();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        filePATH = getRealPathFromURI_API19(AddEmployeeActivity.this, selectedImageUri);
                    } else {
                        filePATH = getRealPathFromURI(data.getData());
                    }

                    imgFile = new File(filePATH);
                    selectedImagePath = ImageFilePath.getPath(getApplicationContext(), selectedImageUri);
                    imgFile = new File(selectedImagePath);

                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }
    }

    private String updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        return aTime;
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateLabel();

        }

        private void updateLabel() {

            String myFormat = "dd/MM/yyyy"; // In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            if (flag == 1) {
                receiptDate = sdf.format(myCalendar.getTime());
                Log.d("--TotalValue-- ", "receiptDate: " + receiptDate);
                edit_dob.setText(receiptDate);

            }
        }
    };
    Calendar myCalendar = Calendar.getInstance();


    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        String filePath = "";

        // Image pick from recent
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;


    }

    private boolean validation() {
        boolean result = true;
        if (edit_employee_name.getText() == null || edit_employee_name.getText().toString().isEmpty()) {
            edit_employee_name.setError("Employee name is required");
            // edit_employee_name.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            edit_employee_name.requestFocus();
            return false;
        } else {
            edit_employee_name.setError(null);
        }

        if (edit_salary.getText() == null || edit_salary.getText().toString().isEmpty()) {
            edit_salary.setError("Please Enter Salary");
            // edit_salary.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            edit_salary.requestFocus();
            return false;
        } else {
            edit_salary.setError(null);
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
