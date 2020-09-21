package com.demo.nspl.restaurantlite.Complain;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ApiClient;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectComplainActivity extends AppCompatActivity {


    Spinner sp_complain_list;
    String _complainName;

    List<ClsComplainList> lstClsComplainLists = new ArrayList<>();
    List<String> lstStringWithTags = new ArrayList<String>();

    Button btn_browse, btn_send_complain;
    Bitmap bmp;
    String imagePath = "";
    String extension = "";
    String filename = "";
    String file = "";
    File finalFile = new File("");
    Boolean _Selected = false;
    ImageView iv_doc_file;

    String mobile = "";
    String _code = "";
    EditText edt_remark;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_complain);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "SelectComplainActivity"));
        }

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        main();
    }

    private void main() {

        Intent intent = getIntent();
        mobile = intent.getStringExtra("mobile");
        _code = intent.getStringExtra("_code");

        Log.d("--GSON--", "_code: " + _code);

        edt_remark = findViewById(R.id.edt_remark);
        btn_send_complain = findViewById(R.id.btn_send_complain);
        sp_complain_list = findViewById(R.id.sp_complain_list);
        btn_browse = findViewById(R.id.btn_browse);
        iv_doc_file = findViewById(R.id.iv_doc_file);

        sp_complain_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                ClsComplainList objClsComplainList = lstClsComplainLists.get(position);
//                _complainName = adapterView.getItemAtPosition(position).toString();
                _complainName = objClsComplainList.getDispositionCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        getComplainList();

        btn_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browseImage();
            }
        });

        btn_send_complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean val = validation();
                if (val == true) {
                    if ( _code != null && !_code.isEmpty()) {

                        uploadComplainError(_code);
                    } else {

                        uploadComplainError("");
                    }
                }

            }
        });

    }


    private Boolean validation() {


        if (sp_complain_list.getSelectedItem().toString().trim().equals("Select complain")) {
            Toast.makeText(getApplicationContext(), "Select complain type.", Toast.LENGTH_SHORT).show();
            sp_complain_list.requestFocus();
            return false;
        }

        if (edt_remark.getText() == null || edt_remark.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter remark.", Toast.LENGTH_SHORT).show();
            edt_remark.requestFocus();
            return false;
        }

        if (!ClsGlobal.CheckInternetConnection(SelectComplainActivity.this)) {
            Toast.makeText(SelectComplainActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private static final int PICK_FROM_FILE = 2;

    public void browseImage() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        intent.setType("image/*");
        intent.putExtra("return-data", false);
        startActivityForResult(
                Intent.createChooser(intent, "Complete action using"),
                PICK_FROM_FILE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_FILE && resultCode == RESULT_OK && data != null) {
            Uri _SelectedFileUri = data.getData();
            try {
                bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), _SelectedFileUri);
                finalFile = new File(getRealPathFromURI(_SelectedFileUri));
                imagePath = GetFilePathFromDevice.getPath(SelectComplainActivity.this, data.getData());
                filename = imagePath.substring(imagePath.lastIndexOf("/") + 1);

                if (filename.indexOf(".") > 0) {
                    file = filename.substring(0, filename.lastIndexOf("."));
                } else {
                    file = filename;
                }
                if (filename.lastIndexOf(".") > 0) {
                    extension = filename.substring(filename.lastIndexOf("."));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            iv_doc_file.setImageBitmap(bmp);
            _Selected = true;


        } else if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Event Call", Toast.LENGTH_SHORT).show();
        }
    }
//      6.44
    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    void uploadComplainError(String _customerCode) {


        String _jpgFromFile = filename;
        _jpgFromFile = _jpgFromFile.replace(".jpg", "");


        ClsCustomerComplainParams obj = new ClsCustomerComplainParams();
        obj.setMobileNumber(mobile);
        obj.setCustomerCode(_customerCode);
        obj.setRequestSubject(_complainName);
        obj.setRequestRemark(edt_remark.getText().toString());
        obj.setProductName(ClsGlobal.AppName);
        obj.setFileName(_jpgFromFile);
        obj.setFileExtension(extension);
        obj.setData(ClsGlobal.getBytes(finalFile));
        obj.setApplicationType(ClsGlobal.ApplicationType);


        Log.d("--GSON--", "mobile: " + mobile);
        Log.d("--GSON--", "_customerCode: " + _customerCode);
        Log.d("--GSON--", "_complainName: " + _complainName);
        Log.d("--GSON--", "remark: " + edt_remark.getText().toString());
        Log.d("--GSON--", "ProductName: " + ClsGlobal.AppName);
        Log.d("--GSON--", "extension: " + extension);
        Log.d("--GSON--", "_jpgFromFile: " + _jpgFromFile);
        Log.d("--GSON--", "finalFile: " + ClsGlobal.getBytes(finalFile));

        Gson gson = new Gson();
        String jsonInString = gson.toJson(obj);
        Log.d("--GSON--", "uploadDocumentAPI- " + jsonInString);


        InterfaceCustomerComplain interfaceComplain =
                ApiClient.getRetrofitInstance().create(InterfaceCustomerComplain.class);

        Call<ClsCustomerComplainParams> call
                = interfaceComplain.postComplain(obj);


        final ProgressDialog pd =
                ClsGlobal._prProgressDialog(SelectComplainActivity.this,
                        "Working...", true);
        pd.show();

        call.enqueue(new Callback<ClsCustomerComplainParams>() {
            @Override
            public void onResponse(Call<ClsCustomerComplainParams> call, Response<ClsCustomerComplainParams> response) {

                pd.dismiss();
                if (response.body() != null) {
                    String _response = response.body().getSuccess();
                    switch (_response) {
                        case "1":
                            Toast.makeText(SelectComplainActivity.this, "Complain send successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                        case "2":
                            Toast.makeText(SelectComplainActivity.this, "Complain not send", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(SelectComplainActivity.this, "No response", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server error...!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ClsCustomerComplainParams> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void getComplainList() {
        lstClsComplainLists = new ArrayList<>();
        ClsComplainList objClsComplainList = new ClsComplainList();
        objClsComplainList.setDispositionCode("Select complain");
        lstClsComplainLists.add(0, objClsComplainList);
        Log.e("--URL--", "STEP-8");

        InterfaceComplain interfaceComplain = ApiClient.getRetrofitInstance().create(InterfaceComplain.class);
        Log.e("--URL--", "interfaceCountry: " + interfaceComplain);

        Call<ClsComplainSuccess> obj = interfaceComplain.value(ClsGlobal.AppName);

        final ProgressDialog pd =
                ClsGlobal._prProgressDialog(SelectComplainActivity.this, "Waiting...", true);
        pd.show();

        obj.enqueue(new Callback<ClsComplainSuccess>() {
            @Override
            public void onResponse(Call<ClsComplainSuccess> call, Response<ClsComplainSuccess> response) {
                pd.dismiss();

                Log.e("--URL--", "STEP-2");
                if (response.body() != null) {
                    Log.e("--URL--", "STEP-3");
//                    String success = response.body().getSuccess();
                    try {
                        List<ClsComplainList> liveStateList = response.body().getData();
                        if (liveStateList != null && liveStateList.size() != 0) {
                            lstClsComplainLists.addAll(liveStateList);
                        }
                        fillComplainList();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    Log.e("--URL--", "STEP-4");
                }
            }

            @Override
            public void onFailure(Call<ClsComplainSuccess> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Check Internet!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void fillComplainList() {

        Log.e("--URL--", "STEP-6");

        Log.e("--URL--", "STEP-7");
        if (lstClsComplainLists != null && lstClsComplainLists.size() != 0) {
            lstStringWithTags = new ArrayList<>();

            for (ClsComplainList obj : lstClsComplainLists) {
                Log.e("--URL--", "STEP-9");
                lstStringWithTags.add(new String(obj.getDispositionCode()));
                Log.e("--URL--", "STEP-10");
            }
            Log.e("--URL--", "STEP-11");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                    SelectComplainActivity.this, R.layout.spinner_complain_list,
                    lstStringWithTags) {

                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    Log.e("--URL--", "STEP-12");
                    View view = super.getDropDownView(position, convertView, parent);
                    Log.e("--URL--", "STEP-13");
                    TextView tv = (TextView) view;
                    Log.e("--URL--", "STEP-14");
                    if (position % 2 == 1) {
                        Log.e("--URL--", "STEP-15");
                        tv.setBackgroundColor(Color.parseColor("#FF1d1d1d"));
                        Log.e("--URL--", "STEP-16");
                    } else {
                        Log.e("--URL--", "STEP-17");
                        tv.setBackgroundColor(Color.parseColor("#FF313131"));
                        Log.e("--URL--", "STEP-18");
                    }
                    Log.e("--URL--", "STEP-19");
                    return view;

                }
            };
            Log.e("--URL--", "STEP-20");
            dataAdapter.setDropDownViewResource(R.layout.spinner_complain_item);
            Log.e("--URL--", "STEP-21");
            sp_complain_list.setAdapter(dataAdapter);
            Log.e("--URL--", "STEP-22");

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
