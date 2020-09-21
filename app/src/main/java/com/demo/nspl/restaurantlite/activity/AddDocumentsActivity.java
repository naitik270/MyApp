package com.demo.nspl.restaurantlite.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.ClsPermission;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsEmployeeDocuments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.demo.nspl.restaurantlite.Global.ClsPermission.REQUEST_READ_PHONE_STATE;

public class AddDocumentsActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView emp_name, doc_ID;
    Spinner spinner_doc_type;
    Button btn_browse, btnSave;
    EditText edit_doc_number, edit_other_doc_name;
    ImageView preview;
    LinearLayout other_linear;

    private int DATE_DIALOG_ID = 1;
    private ProgressDialog pd;
    Uri uri;

    String item = "";
    String _filePath = "";
    TextView txt_select_date;

    private static final int CAMERA_REQUEST = 1;
    private static final int PICK_FROM_FILE = 2;
    String[] _docType = {
            "Select document",
            "PAN CARD",
            "DRIVING LICENSE",
            "VOTER ID",
            "PASSPORT",
            "AADHAR CARD",
            "RASHAN CARD",
            "PHOTO",
            "OTHER"
    };
    Bitmap bmp;
    Boolean _Selected = false;
    int _ID;
    int EMP_ID;


    int flag = 0;
    String receiptDate = "";
    String formattedDate, upt = "0:0:0";
    private int hour, minute;


    int cust_id = 0;
    String cust_name = "";
    String cust_mob = "";
    String _flag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_documents);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "AddDocumentsActivity"));
        }


        toolbar = findViewById(R.id.toolbar);
        txt_select_date = findViewById(R.id.txt_select_date);
        emp_name = findViewById(R.id.emp_name);
        spinner_doc_type = findViewById(R.id.doc_type);
        edit_doc_number = findViewById(R.id.edit_doc_number);
        btn_browse = findViewById(R.id.btn_browse);
        btnSave = findViewById(R.id.btnSave);
        preview = findViewById(R.id.preview);
        doc_ID = findViewById(R.id.doc_ID);
        edit_other_doc_name = findViewById(R.id.edit_other_doc_name);
        other_linear = findViewById(R.id.other_linear);


        ClsPermission.checkpermission(AddDocumentsActivity.this);

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EMP_ID = getIntent().getIntExtra("EMP_ID", 0);
        String Name = getIntent().getStringExtra("Name");
        _ID = getIntent().getIntExtra("ID", 0);


        cust_id = getIntent().getIntExtra("cust_id", 0);
        cust_name = getIntent().getStringExtra("cust_name");
        cust_mob = getIntent().getStringExtra("cust_mob");
        _flag = getIntent().getStringExtra("_flag");


        if (_flag.equalsIgnoreCase("customer"))
            emp_name.setText(cust_name.toUpperCase());
        else
            emp_name.setText(Name.toUpperCase());


        btn_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadDocumentDialog();
            }
        });

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = df.format(c.getTime());

        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        upt = updateTime(hour, minute);


        txt_select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flag = 1;
                new DatePickerDialog(AddDocumentsActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result;
                if (item.equalsIgnoreCase("Select document")) {
                    Toast.makeText(AddDocumentsActivity.this, "Document is required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (item.equalsIgnoreCase("OTHER")) {

                    if (edit_other_doc_name.getText() == null || edit_other_doc_name.getText().toString().trim().isEmpty()) {
                        Toast.makeText(AddDocumentsActivity.this, "Document Name is required", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (_Selected == false) {
                    Toast.makeText(AddDocumentsActivity.this, "Document is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                String fileNameToSaveInDB = SaveFile();
                if (!fileNameToSaveInDB.isEmpty()) {

                    ClsEmployeeDocuments Objclsempdocument =
                            new ClsEmployeeDocuments(AddDocumentsActivity.this);

                    if (_flag.equalsIgnoreCase("customer")) {
                        Objclsempdocument.setEmployee_id(cust_id);
                    } else {
                        Objclsempdocument.setEmployee_id(EMP_ID);
                    }

                    Objclsempdocument.setDoc_id(_ID);

                    Objclsempdocument.setOther_proof(edit_other_doc_name.getText().toString());
                    Objclsempdocument.setFilepath(_filePath);
                    Objclsempdocument.setDoc_name(item);
                    Objclsempdocument.setDoc_no(edit_doc_number.getText().toString());
                    Objclsempdocument.setFilename(fileNameToSaveInDB);

//                    Objclsempdocument.setEXP_DATE(txt_select_date.getText().toString());


                    Objclsempdocument.setEXP_DATE(ClsGlobal.getTextFromTxtBox(txt_select_date));
                    Objclsempdocument.setTYPE(_flag);


                    if (Objclsempdocument.getDoc_id() == 0) {

                        result = ClsEmployeeDocuments.Insert(Objclsempdocument);
                        if (result == -1) {
                            Toast.makeText(getApplicationContext(), "Technical Error", Toast.LENGTH_SHORT).show();
                        } else if (result == 0) {
                            Toast.makeText(getApplicationContext(), "Data Not Inserted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Insert Data", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("INSERT---  ", String.valueOf(result));
                    } else {

////                    result = ClsEmployeeMaster.Update(objClsEmployeeMaster);
////                    Log.e("ELSE---  ", String.valueOf(result));
                    }
                } else {
                    Toast.makeText(AddDocumentsActivity.this, "File Name is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                finish();


            }
        });

        spinner_doc_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                item = adapterView.getItemAtPosition(i).toString();
//                item = adapterView.getSelectedItem().toString();


                if (item.equalsIgnoreCase("OTHER")) {
                    other_linear.setVisibility(View.VISIBLE);
                } else {
                    other_linear.setVisibility(View.GONE);
                }
                // Showing selected spinner item
//                Toast.makeText(getApplicationContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                AddDocumentsActivity.this, R.layout.drop_down,
                _docType);
        dataAdapter
                .setDropDownViewResource(R.layout.drop_down);
        spinner_doc_type.setAdapter(dataAdapter);


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

        // Append in a StringBuilder
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
                txt_select_date.setText(receiptDate);

            }
        }
    };
    Calendar myCalendar = Calendar.getInstance();


    private String SaveFile() {
        String _filename = "";
        OutputStream output;


        if (preview.getDrawable() != null) {

            _filename = item + "_".concat(ClsGlobal.getRandom()).concat(".jpg");

            Log.e("FileNAme", "SaveFile--->>>" + _filename);

            BitmapDrawable drawable = (BitmapDrawable) preview.getDrawable();
            bmp = drawable.getBitmap();

            File _saveLocation = Environment.getExternalStorageDirectory();
            Log.d("camera", "filepath:- " + _saveLocation);


            String _folderName = "";
            int _id = 0;

            if (_flag.equalsIgnoreCase("customer")) {
                _folderName = "/CustomerDocument/";
                _id = cust_id;
            } else {
                _folderName = "/EmployeeDocument/";
                _id = EMP_ID;
            }

            File dir = new File(_saveLocation.getAbsolutePath()
                    + "/" + ClsGlobal.AppFolderName +
                    _folderName.concat(String.valueOf(_id)).concat("/"));

            if (!dir.exists()) {
                dir.mkdirs();
            }


            Log.e("dir-", String.valueOf(dir));
            File filetoSave = new File(dir.getAbsolutePath() + "/".concat(_filename));
            _filePath = String.valueOf(filetoSave);
            Log.e("_filePath-", _filePath);

            try {
                output = new FileOutputStream(filetoSave);
                bmp.compress(Bitmap.CompressFormat.JPEG, 25, output);
                output.flush();
                output.close();

                Toast.makeText(AddDocumentsActivity.this, "Image Saved to SD Card", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                _filename = "";
                Log.e("Exception", "Catch--->>>>" + e.getMessage());
                e.printStackTrace();
            }
        }
        return _filename;
    }

    private void UploadDocumentDialog() {
        final Dialog dialog = new Dialog(AddDocumentsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_cam_gallary);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);


        LinearLayout ll_gallery = dialog.findViewById(R.id.ll_gallery);
        LinearLayout ll_camera = dialog.findViewById(R.id.ll_camera);

        ll_gallery.setOnClickListener(v -> {
            dialog.dismiss();
            dialog.cancel();
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            intent.setType("image/*");
            intent.putExtra("return-data", false);
            startActivityForResult(
                    Intent.createChooser(intent, "Complete action using"),
                    PICK_FROM_FILE);

        });

        ll_camera.setOnClickListener(view -> {
            dialog.dismiss();
            dialog.cancel();
            String extension = ".jpg";
            String filename = item + "_".concat(ClsGlobal.getRandom());
            Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            if (intent1.resolveActivity(getPackageManager()) != null) {
                ContentValues values = new ContentValues(1);
                values.put(MediaStore.Images.Media.MIME_TYPE, "".concat(filename.concat(extension)));
                uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(intent1, CAMERA_REQUEST);
            } else {
                Toast.makeText(AddDocumentsActivity.this, "ERROR SELECT OTHER WAY", Toast.LENGTH_SHORT).show();
            }

        });
        dialog.show();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_FILE && resultCode == RESULT_OK && data != null) {
            Uri _SelectedFileUri = data.getData();
            try {
                bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), _SelectedFileUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            preview.setImageBitmap(bmp);

            _Selected = true;
        } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            try {

                bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                preview.setImageBitmap(bmp);

            } catch (IOException e) {
                e.printStackTrace();
            }

            _Selected = true;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE: {
                ClsPermission.checkPermission(requestCode, permissions, grantResults, AddDocumentsActivity.this);
                return;
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
