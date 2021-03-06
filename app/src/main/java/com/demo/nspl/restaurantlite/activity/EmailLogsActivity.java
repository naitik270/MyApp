package com.demo.nspl.restaurantlite.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.SendEmailLogsAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsEmailLogs;
import com.demo.nspl.restaurantlite.classes.ClsUserInfo;

import java.util.ArrayList;
import java.util.List;

public class EmailLogsActivity extends AppCompatActivity {

    SendEmailLogsAdapter mAdapter;
    private List<ClsEmailLogs> list;
    private RecyclerView mRv;
    private Toolbar toolbar;
    private TextView empty_title_text;
    ClsUserInfo loginStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_logs);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "EmailLogsActivity"));
        }


        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(23);

        loginStatus = ClsGlobal.getUserInfo(this);

        if (loginStatus.getLoginStatus().equalsIgnoreCase("DEACTIVE")) {
            if (ClsGlobal.AppPackageName.equalsIgnoreCase("com.demo.nspl.retailpos_demo")) {
                Intent i = new Intent(this, LoginActivityDemo.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            } else {
                Intent i = new Intent(this, LogInActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        }


        mRv = findViewById(R.id.rv);
        empty_title_text = findViewById(R.id.empty_title_text);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        ViewData();
    }

    private void ViewData() {
        list = new ArrayList<>();
        list = ClsEmailLogs.getList(this);

        if (list.size() == 0) {
            empty_title_text.setVisibility(View.VISIBLE);
            mRv.setVisibility(View.GONE);
        } else {
            empty_title_text.setVisibility(View.INVISIBLE);
            mRv.setVisibility(View.VISIBLE);
        }

        Log.e("list", String.valueOf(list.size()));

//        Gson gson = new Gson();
//        String jsonInString = gson.toJson(list);
//        Log.e("jsonInString:-- ", jsonInString);

        mAdapter = new SendEmailLogsAdapter(this, list);

        mRv.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
