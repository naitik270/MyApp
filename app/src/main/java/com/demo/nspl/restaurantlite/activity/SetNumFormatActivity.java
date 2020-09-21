package com.demo.nspl.restaurantlite.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.nspl.restaurantlite.R;

public class SetNumFormatActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    TextView txt_amount;
    RadioButton rb_one, rb_two, rb_three, rb_four;
    RadioGroup rg_decimal;
    Button btn_save;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_num_format);


        main();
    }

    private void main() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Number format");

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        txt_amount = findViewById(R.id.txt_amount);
        txt_amount.setText(" \u20B9 10000.00");
        rg_decimal = findViewById(R.id.rg_decimal);

        rb_one = findViewById(R.id.rb_one);
        rb_two = findViewById(R.id.rb_two);
        rb_three = findViewById(R.id.rb_three);
        rb_four = findViewById(R.id.rb_four);

        btn_save = findViewById(R.id.btn_save);


        rg_decimal.setOnCheckedChangeListener(this);

        btn_save.setOnClickListener(v -> {

        });

    }

/*
    void setDecimalNumber() {

        ClsUserInfo objSignatoryName = ClsGlobal.getUserInfo(getApplicationContext());

        if(objSignatoryName.getGetDecimalNumber().equalsIgnoreCase())

        edt_bill_title.setText(objSignatoryName.getBillTitle());

    }
*/

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

        int radioGroupId = radioGroup.getId();

        if (radioGroupId == R.id.rg_decimal) {
            switch (checkedId) {
                case R.id.rb_one:
                    txt_amount.setText(" \u20B9 10000.0");
                    break;
                case R.id.rb_two:
                    txt_amount.setText(" \u20B9 10000.00");
                    break;
                case R.id.rb_three:
                    txt_amount.setText(" \u20B9 10000.000");
                    break;
                case R.id.rb_four:
                    txt_amount.setText(" \u20B9 10000.0000");
                default:
                    break;
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
