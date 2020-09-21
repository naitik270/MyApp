package com.demo.nspl.restaurantlite.SMS;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;

import java.util.Objects;

public class SmsIdSettingActivity extends AppCompatActivity {


    EditText edt_sender_id;
    EditText edit_remark;
    RadioButton rb_default_yes, rb_default_no;
    RadioButton rb_yes, rb_no;
    int _ID = 0;
    int result;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_id_setting_form);


        main();
    }

    private void main() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        rb_default_yes = findViewById(R.id.rb_default_yes);
        rb_default_no = findViewById(R.id.rb_default_no);
        edit_remark = findViewById(R.id.edit_remark);
        rb_yes = findViewById(R.id.rb_yes);
        rb_no = findViewById(R.id.rb_no);

        edt_sender_id = findViewById(R.id.edt_sender_id);

        _ID = getIntent().getIntExtra("_ID", 0);


        fillValue();


    }


    void saveRecord() {

        ClsSmsIdSetting objClsSmsIdSetting =
                new ClsSmsIdSetting(SmsIdSettingActivity.this);

        String where = " AND  [sms_id] = "
                .concat("'")
                .concat(edt_sender_id.getText().toString().trim())
                .concat("'");

        if (_ID != 0) {
            where = where.concat(" AND [id] <> ").concat(String.valueOf(_ID));
        }

        boolean exists = objClsSmsIdSetting.checkExists(where, SmsIdSettingActivity.this);

        if (exists) {
            Toast toast = Toast.makeText(SmsIdSettingActivity.this, "Sender ID is already exists....", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            return;
        } else {
            objClsSmsIdSetting.setId(_ID);
            objClsSmsIdSetting.setSms_id(edt_sender_id.getText().toString().trim());
            objClsSmsIdSetting.setDefault_sms(rb_default_yes.isChecked() ? "YES" : "NO");
            objClsSmsIdSetting.setActive(rb_yes.isChecked() ? "YES" : "NO");
            objClsSmsIdSetting.setRemark(edit_remark.getText().toString().trim());

            if (objClsSmsIdSetting.getId() == 0) {
                result = ClsSmsIdSetting.Insert(objClsSmsIdSetting, SmsIdSettingActivity.this);

                if (result != 0) {
                    Toast.makeText(this, "Sms id setting save Successfully.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Error While Adding Sms id setting.", Toast.LENGTH_LONG).show();
                }

            } else if (objClsSmsIdSetting.getId() > 0) {
                objClsSmsIdSetting.setId(_ID);

                @SuppressLint("WrongConstant")
                SQLiteDatabase db = openOrCreateDatabase(ClsGlobal.Database_Name, MODE_APPEND, null);
                result = ClsSmsIdSetting.Update(objClsSmsIdSetting, "Update All", db);

                if (result != 0) {
                    Toast.makeText(this, "Sms id setting Updated Successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Error While Updating Sms id setting", Toast.LENGTH_LONG).show();
                }
                db.close();
            }
            finish();
        }
    }

    void fillValue() {

        if (_ID != 0) {
            ClsSmsIdSetting obj = new ClsSmsIdSetting();
            obj.setId(_ID);
            obj = ClsSmsIdSetting.getObject(obj, SmsIdSettingActivity.this);

            edt_sender_id.setText(obj.getSms_id());
            String getDefault = obj.getDefault_sms();


            if (getDefault.equals("YES")) {
                rb_default_yes.setChecked(true);
            } else {
                rb_default_no.setChecked(true);
            }


            String getActive = obj.getActive();

            if (getActive.equals("YES")) {
                rb_yes.setChecked(true);
            } else {
                rb_no.setChecked(true);
            }

            edit_remark.setText(obj.getRemark());
        }

    }


    private boolean validation() {
        boolean result = true;

        if (edt_sender_id.getText() == null || edt_sender_id.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Sender id required.", Toast.LENGTH_SHORT).show();
            edt_sender_id.requestFocus();
            return false;
        }

        if (edt_sender_id.getText().toString().length() < 6) {
            Toast.makeText(getApplicationContext(), "Six characters is required.", Toast.LENGTH_SHORT).show();
            edt_sender_id.requestFocus();
            return false;
        }

        return result;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cust_group, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        if (id == R.id.itm_done) {
            if (validation()) {
                saveRecord();
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
