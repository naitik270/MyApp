package com.demo.nspl.restaurantlite.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
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

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsEmailConfiguration;
import com.google.gson.Gson;

public class EmailConfigurationActivity extends AppCompatActivity {

    EditText edit_From_Mail_Id, edit_password, edit_SMTP,
            edit_Sender_Name, edit_Port;
    Button btn_Save;
    RadioButton rbTrue_SSl, rbFalse_SSl, rbYES_Active, rbNO_Active;
    RadioGroup rg_SSl, rg_Active;
    Toolbar toolbar;
    TextView txt_less_Secure;
    String text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_configuration);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_Save = findViewById(R.id.btn_Save);
        edit_From_Mail_Id = findViewById(R.id.edit_From_Mail_Id);
        edit_password = findViewById(R.id.edit_password);
        edit_SMTP = findViewById(R.id.edit_SMTP);
        edit_Sender_Name = findViewById(R.id.edit_Display_Name);
        edit_Port = findViewById(R.id.edit_Port);
        txt_less_Secure = findViewById(R.id.txt_less_Secure);
        rg_SSl = findViewById(R.id.rg_SSl);
        rg_Active = findViewById(R.id.rg_Active);
        rbTrue_SSl = findViewById(R.id.rbTrue);
        rbFalse_SSl = findViewById(R.id.rbFalse);
        rbYES_Active = findViewById(R.id.rbYES_Active);
        rbNO_Active = findViewById(R.id.rbNO_Active);

        //Less Secure Mode
        //txt_less_Secure.setText("Turn No : https://myaccount.google.com/lesssecureapps ");


        txt_less_Secure.setMovementMethod(LinkMovementMethod.getInstance());

        ClsEmailConfiguration current = ClsGlobal.getEmailConfiguration(this);

        if (current != null) {
            edit_From_Mail_Id.setText(current.getFromEmailId());
            edit_password.setText(current.getPassword());
            edit_SMTP.setText(current.getSMTP());
            edit_Sender_Name.setText(current.getDisplay_Name());
            edit_Port.setText(current.getPort());

            if (current.getFromEmailId() != null && !current.getFromEmailId().isEmpty()) {
                String currentText = current.getFromEmailId();
                String HostAddress = "";
                String[] HostAddressArray;
                if (currentText.contains("@")) {
                    HostAddressArray = currentText.split("@");
                    if (HostAddressArray.length == 2) {
                        HostAddress = HostAddressArray[1];

                        switch (HostAddress) {

                            case "gmail.com":
                                text = "<a href=\"https://myaccount.google.com/lesssecureapps\">Turn On</a>  Less Secure Mode";

                                break;

                            case "hotmail.com":
                                text = "";

                                break;

                            case "outlook.com":
                                text = "";
                                break;

                            case "yahoo.com":
                                text = "<a href=\"https://login.yahoo.com/account/security?.scrumb=jtUvA755gz7\">Turn On</a>  Less Secure Mode";

                                break;

                            case "aol.com":
                                text = "<a href=\"https://login.aol.com/account/security?lang=en-US&.scrumb=ia22XW%2FazUL\">Turn On</a>  Less Secure Mode";

                                break;

                            case "rediffmail.com":
                                text = "";
                                break;

                        }
                        SetLink();

                    }

                }

            }

            if (current.getSSl().equalsIgnoreCase("True")) {
                Log.e("current", current.getSSl());
                rbTrue_SSl.setChecked(true);
            } else {
                rbFalse_SSl.setChecked(true);
            }

            if (current.getActive().equalsIgnoreCase("YES")) {
                Log.e("current", current.getActive());
                rbYES_Active.setChecked(true);
            } else {
                rbNO_Active.setChecked(true);
            }


        }

        edit_From_Mail_Id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().isEmpty() && !s.toString().equalsIgnoreCase("")) {

                    String currentText = s.toString();
                    String HostAddress = "";
                    String[] HostAddressArray;

                    if (currentText.contains("@")) {

                        HostAddressArray = currentText.split("@");

                        Gson gson = new Gson();
                        String getTaglist = gson.toJson(HostAddressArray);
                        Log.e("getTaglist", getTaglist);

                        if (HostAddressArray.length == 2) {

                            HostAddress = HostAddressArray[1];

                            Log.e("HostAddress", "length call");
                            Log.e("HostAddress", HostAddress);


                            switch (HostAddress) {

                                case "gmail.com":
                                    text = "<a href=\"https://myaccount.google.com/lesssecureapps\">Turn On</a>  Less Secure Mode";
                                    edit_SMTP.setText("smtp.gmail.com");
                                    edit_Port.setText("587");
                                    rbTrue_SSl.setChecked(true);
                                    edit_Sender_Name.setText(edit_From_Mail_Id.getText().toString().trim());
                                    break;

                                case "hotmail.com":
                                    text = "";
                                    edit_SMTP.setText("smtp.live.com");
                                    edit_Port.setText("587");
                                    rbTrue_SSl.setChecked(true);
                                    edit_Sender_Name.setText(edit_From_Mail_Id.getText().toString().trim());
                                    break;

                                case "outlook.com":
                                    text = "";
                                    edit_SMTP.setText("smtp-mail.outlook.com");
                                    edit_Port.setText("587");
                                    rbTrue_SSl.setChecked(true);
                                    break;

                                case "yahoo.com":
                                    text = "<a href=\"https://login.yahoo.com/account/security?.scrumb=jtUvA755gz7\">Turn On</a>  Less Secure Mode";
                                    edit_SMTP.setText("smtp.mail.yahoo.com");
                                    edit_Port.setText("587");
                                    rbTrue_SSl.setChecked(true);
                                    edit_Sender_Name.setText(edit_From_Mail_Id.getText().toString().trim());
                                    break;

                                case "aol.com":
                                    text = "<a href=\"https://login.aol.com/account/security?lang=en-US&.scrumb=ia22XW%2FazUL\">Turn On</a>  Less Secure Mode";
                                    edit_SMTP.setText("smtp.aol.com");
                                    edit_Port.setText("587");
                                    rbTrue_SSl.setChecked(true);
                                    edit_Sender_Name.setText(edit_From_Mail_Id.getText().toString().trim());
                                    break;

                                case "rediffmail.com":
                                    text = "";
                                    edit_SMTP.setText("smtp.rediffmail.com");
                                    edit_Port.setText("587");
                                    rbTrue_SSl.setChecked(true);
                                    edit_Sender_Name.setText(edit_From_Mail_Id.getText().toString().trim());
                                    break;

                            }

                        }

                        SetLink();

                    } else {
                        Log.e("HostAddress", "Else if");
                        edit_SMTP.setText("");
                        edit_Port.setText("");
                        rbTrue_SSl.setChecked(true);
                        txt_less_Secure.setText("No Need to Turn On Less Secure Mode");
                        edit_Sender_Name.setText("");
                    }


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_Save.setOnClickListener(v -> {

            if (validation()) {

                ClsEmailConfiguration currentSaveEmailConfiguration = new ClsEmailConfiguration();

                currentSaveEmailConfiguration.setFromEmailId(edit_From_Mail_Id.getText().toString().equalsIgnoreCase("")
                        ? "" : edit_From_Mail_Id.getText().toString().trim());

                currentSaveEmailConfiguration.setPassword(edit_password.getText().toString().equalsIgnoreCase("")
                        ? "" : edit_password.getText().toString().trim());

                currentSaveEmailConfiguration.setSMTP(edit_SMTP.getText().toString().equalsIgnoreCase("")
                        ? "" : edit_SMTP.getText().toString().trim());

                currentSaveEmailConfiguration.setDisplay_Name(edit_Sender_Name.getText().toString().equalsIgnoreCase("")
                        ? "" : edit_Sender_Name.getText().toString().trim());

                currentSaveEmailConfiguration.setPort(edit_Port.getText().toString().equalsIgnoreCase("")
                        ? "" : edit_Port.getText().toString().trim());

                currentSaveEmailConfiguration.setSSl(rbTrue_SSl.isChecked()
                        ? rbTrue_SSl.getText().toString() : rbFalse_SSl.getText().toString().trim());

//                currentSaveEmailConfiguration.setSMTP(rbTrue_SSl.isChecked()
//                        ? rbTrue_SSl.getText().toString() : rbFalse_SSl.getText().toString());

                currentSaveEmailConfiguration.setActive(rbYES_Active.isChecked()
                        ? rbYES_Active.getText().toString().trim() : rbNO_Active.getText().toString().trim());

                currentSaveEmailConfiguration.setEmailConfiguration("EmailConfiguration");

                Gson gson = new Gson();
                String jsonInString = gson.toJson(currentSaveEmailConfiguration);
                Log.e("Result", "currentSaveEmailConfiguration---" + jsonInString);

                ClsGlobal.setEmailConfiguration(currentSaveEmailConfiguration, EmailConfigurationActivity.this);

                Toast.makeText(this, "Email Configuration Save Successfully", Toast.LENGTH_SHORT).show();

                finish();
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

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

        boolean result = true;
        if (edit_From_Mail_Id.getText() == null ||
                edit_From_Mail_Id.getText().toString().isEmpty()) {
            edit_From_Mail_Id.setError("Email Id is Required");
            // edit_vendor_name.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            edit_From_Mail_Id.requestFocus();
            return false;
        } else {
            edit_From_Mail_Id.setError(null);
        }


        if (edit_password.getText() == null ||
                edit_password.getText().toString().isEmpty()) {
            edit_password.setError("Password is Required");
            //  edit_contact_no.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            edit_password.requestFocus();
            return false;
        } else {

            edit_password.setError(null);
        }


        return result;
    }

    private void SetLink() {
        if (!text.equalsIgnoreCase("")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                txt_less_Secure.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
            } else {
                txt_less_Secure.setText(Html.fromHtml(text));
            }
        } else {
            txt_less_Secure.setText("No Need to Turn On Less Secure Mode");
        }
    }

}
