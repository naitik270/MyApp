package com.demo.nspl.restaurantlite.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.EmailAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.SendEmailUtility.Scheduler;
import com.demo.nspl.restaurantlite.SendEmailUtility.SharedPreferenceTime;
import com.demo.nspl.restaurantlite.backGroundTask.SendEmailTask;
import com.demo.nspl.restaurantlite.classes.ClsEmailConfiguration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SettingAutoEmailActivity extends AppCompatActivity {

    private RecyclerView mRv;
    private EmailAdapter mCv;
    private EditText Edit_Add_Email;
    private TextView Txt_Set_Timer, Txt_label_NoEmail;
    // private ImageView imageViewClock;
    private Button mfloatingActionButton;
    private int mHour, mMinute;
    Toolbar toolbar;
    private List<String> EmailList = new ArrayList<>();
    private Calendar c;
    SharedPreferenceTime mSharedPreferenceTime;
    int hour, min;
    // Shared preferences object
    private SharedPreferences mPreferences;


    // Name of shared preferences file
    private static final String mPreferncesEmail = "Email_Settings";

    LinearLayout ll_configuration;
    LinearLayout ll_mail_click;
    TextView txt_email_configuration;
    ImageView iv_setting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_auto_email);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Txt_Set_Timer = findViewById(R.id.Txt_Set_Timer);
        Txt_label_NoEmail = findViewById(R.id.Txt_label_NoEmail);
        Edit_Add_Email = findViewById(R.id.Edit_Add_Email);
        mfloatingActionButton = findViewById(R.id.floatingActionButton);

        ll_configuration = findViewById(R.id.ll_configuration);
        txt_email_configuration = findViewById(R.id.txt_email_configuration);
        iv_setting = findViewById(R.id.iv_setting);
        ll_mail_click = findViewById(R.id.ll_mail_click);

        mPreferences = getSharedPreferences(mPreferncesEmail, MODE_PRIVATE);
        mRv = findViewById(R.id.rv);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mSharedPreferenceTime = new SharedPreferenceTime(getApplication());

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "SettingAutoEmailActivity"));
        }

        if (EmailList.size() == 0) {
            Txt_label_NoEmail.setVisibility(View.VISIBLE);
        } else {
            Txt_label_NoEmail.setVisibility(View.INVISIBLE);
        }


        hour = mSharedPreferenceTime.get_hour();
        min = mSharedPreferenceTime.get_min();
        Log.e("hour", String.valueOf(hour));
        Log.e("min", String.valueOf(min));
        DisplayTime(hour, min);

        SetupdateTime(hour, min);

        EmailListFromSharedPreferencesFile();

        Txt_Set_Timer.setOnClickListener(view -> {

            // Get Current Time
            c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view1, hourOfDay, minute) -> {

                        SetupdateTime(hourOfDay, minute);

                    }, mHour, mMinute, false);

            timePickerDialog.show();

        });


        mfloatingActionButton.setOnClickListener(view -> {

            // Checking if Edit_Add_Email editText is Empty or null.
            if (!Edit_Add_Email.getText().toString().trim().matches("")) {

                // Checking for EMAIL_ADDRESS Validation.
                if (!Patterns.EMAIL_ADDRESS.matcher(Edit_Add_Email.getText().toString().trim()).matches()) {
                    Toast.makeText(this, "Email Not Valid!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (EmailList.size() != 10) {
                    // Adding Email to the EmailList.

                    if (!EmailList.contains(Edit_Add_Email.getText().toString().trim())){
                        EmailList.add(Edit_Add_Email.getText().toString().trim());
                        if (EmailList != null) {

                            String EmailListGson = new Gson().toJson(EmailList);
                            // Save list of Email to SharedPreferences.
                            SaveToSharedPreferencesFile("EmailList", EmailListGson);

                            // Updating the list.
                            EmailListFromSharedPreferencesFile();

                            Toast.makeText(this, "Email Added Successfully!", Toast.LENGTH_SHORT).show();

                            Edit_Add_Email.setText("");
                            Log.e("EmailList", EmailListGson);
                        }
                    }else {
                        Toast.makeText(this, "Email already exists!", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(this, "Maximum 10 email is valid", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Enter Email Id", Toast.LENGTH_SHORT).show();
            }
        });

        ClsEmailConfiguration current = ClsGlobal.getEmailConfiguration(this);

        Log.e("--current--", "--current--" + current.getEmailConfiguration());

        if (current.getEmailConfiguration().equalsIgnoreCase("EmailConfiguration")) {
            txt_email_configuration.setText("EMAIL CONFIGURATION DONE");
            txt_email_configuration.setTextColor(Color.parseColor("#316426"));
            iv_setting.setColorFilter(getApplicationContext().getResources().getColor(R.color.dark_green));
            ll_configuration.setVisibility(View.VISIBLE);
        }

        ll_mail_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingAutoEmailActivity.this,
                        EmailConfigurationActivity.class);

                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        ClsEmailConfiguration current = ClsGlobal.getEmailConfiguration(this);

        Log.e("--current--", "--current--" + current.getEmailConfiguration());

        if (current.getEmailConfiguration().equalsIgnoreCase("EmailConfiguration")) {
            txt_email_configuration.setText("EMAIL CONFIGURATION DONE");
            txt_email_configuration.setTextColor(Color.parseColor("#316426"));
            iv_setting.setColorFilter(getApplicationContext().getResources().getColor(R.color.dark_green));
            ll_configuration.setVisibility(View.VISIBLE);
        }

    }


    private void EmailListFromSharedPreferencesFile() {

        try {

            String getListOfEmail = mPreferences.getString("EmailList", null);

            if (getListOfEmail != null) {
                ArrayList<String> getEmailList = new Gson().fromJson(getListOfEmail,
                        new TypeToken<ArrayList<String>>() {
                        }.getType());

                if (EmailList.size() != 0) {
                    EmailList.clear();
                }

                EmailList.addAll(getEmailList);

                if (EmailList.size() == 0) {
                    Txt_label_NoEmail.setVisibility(View.VISIBLE);
                } else {
                    Txt_label_NoEmail.setVisibility(View.INVISIBLE);
                }

                mCv = new EmailAdapter(this, EmailList);
                mRv.setAdapter(mCv);
                mCv.SetOnClickListener(position -> {

                    Log.e("position", String.valueOf(position));
                    EmailList.remove(position);
                    String EmailListGson = new Gson().toJson(EmailList);

                    // Save list of Email to SharedPreferences.
                    SaveToSharedPreferencesFile("EmailList", EmailListGson);

                    String getListOfEmail1 = mPreferences.getString("EmailList", null);

                    if (getListOfEmail1 != null) {
                        ArrayList<String> getCompleteEmailList = new Gson().fromJson(getListOfEmail1,
                                new TypeToken<ArrayList<String>>() {
                                }.getType());
                        if (EmailList.size() != 0) {
                            EmailList.clear();
                        }

                        EmailList.addAll(getCompleteEmailList);
                        if (EmailList.size() == 0) {
                            Txt_label_NoEmail.setVisibility(View.VISIBLE);
                        } else {
                            Txt_label_NoEmail.setVisibility(View.INVISIBLE);
                        }
                        mCv.Add(EmailList);

                    }

                });
            }

            Log.e("EmailListOnCreate", new Gson().toJson(EmailList));


        } catch (NullPointerException ignored) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_auto_email, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        if (id == R.id.send_email) {
            if (ClsGlobal.CheckInternetConnection(this)) {
                if (EmailList != null && EmailList.size() != 0) {
                    ClsGlobal.SendEmail(this, "Auto generated email, Send Manually");
                    Toast.makeText(this, "You will get email after few minutes", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please configure email first", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        if (id == R.id.send_email_Logs) {

            Intent intent = new Intent(this, EmailLogsActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void SetupdateTime(int hours, int mins) {

        Log.e("TimeInHoursAndMin", String.valueOf(hours) + String.valueOf(mins));

        Log.e("HourMinInMillisecond", String.valueOf(TimeUnit.HOURS.toMillis((long) hours) + TimeUnit.MINUTES.toMillis((long) mins)));

        mSharedPreferenceTime.set_hour(hours);
        mSharedPreferenceTime.set_min(mins);

        Scheduler.setReminder(SettingAutoEmailActivity.this, SendEmailTask.class
                , mSharedPreferenceTime.get_hour(), mSharedPreferenceTime.get_min());


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

        Txt_Set_Timer.setText(aTime);
    }

    private void DisplayTime(int hours, int mins) {
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


        Txt_Set_Timer.setText(aTime);
    }

    private void SaveToSharedPreferencesFile(String key, String values) {
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString(key, values);
        preferencesEditor.apply();
    }
}
