package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.SenderIdAdapter;
import com.demo.nspl.restaurantlite.Adapter.SmsFormatTitleAdapter;
import com.demo.nspl.restaurantlite.Adapter.SmsGroupItemAdapter;
import com.demo.nspl.restaurantlite.Global.ApiClient;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.SMS.ClsCheckCustCreditSMSParams;
import com.demo.nspl.restaurantlite.SMS.ClsMessageFormat;
import com.demo.nspl.restaurantlite.SMS.ClsSmsCustomerGroup;
import com.demo.nspl.restaurantlite.SMS.ClsSmsIdSetting;
import com.demo.nspl.restaurantlite.SMS.InterfaceCheckCustCreditSMS;
import com.demo.nspl.restaurantlite.classes.ClsUserInfo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demo.nspl.restaurantlite.activity.SendSmsPreviewActivity.finishFlag;

public class SendSmsActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView txt_group_name, txt_sender_id, txt_sms_format;
    EditText edit_sms_preview, edit_bulk_sms;
    Button btn_next;
    RadioGroup rg;
    RadioButton rb_transnational, rb_promotional;
    Dialog dialog;
    ImageView img_clear;
    ClsSmsCustomerGroup currentSmsCustomerGroup = new ClsSmsCustomerGroup();
    ClsSmsIdSetting currentSmsIdSetting = new ClsSmsIdSetting();
    ClsMessageFormat currentMessageFormat = new ClsMessageFormat();

    List<ClsSmsCustomerGroup> listSms_Group = new ArrayList<>();
    List<ClsSmsIdSetting> listSms_id = new ArrayList<>();
    List<ClsMessageFormat> listMessageFormat = new ArrayList<>();

    SmsGroupItemAdapter adapterGroup_Name;
    SenderIdAdapter adapter_senderId;
    SmsFormatTitleAdapter adapter_Sms_Format_title;

    List<ClsSmsCustomerGroup> filter_list_SmsCustomerGroup = new ArrayList<>();
    List<ClsSmsIdSetting> filter_list_Sms_id = new ArrayList<>();
    List<ClsMessageFormat> filter_list_MessageFormat = new ArrayList<>();

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        edit_sms_preview = findViewById(R.id.edit_sms_preview);
        edit_bulk_sms = findViewById(R.id.edit_bulk_sms);
        btn_next = findViewById(R.id.btn_next);
        rg = findViewById(R.id.rg);
        rb_transnational = findViewById(R.id.rb_transnational);
        rb_promotional = findViewById(R.id.rb_promotional);

        txt_group_name = findViewById(R.id.txt_group_name);
        txt_sender_id = findViewById(R.id.txt_sender_id);
        txt_sms_format = findViewById(R.id.txt_sms_format);

        CheckSmsBalance();

        adapterGroup_Name = new SmsGroupItemAdapter(SendSmsActivity.this);
        adapter_senderId = new SenderIdAdapter(SendSmsActivity.this);
        adapter_Sms_Format_title = new SmsFormatTitleAdapter(SendSmsActivity.this);


        txt_group_name.setOnClickListener(v -> {
            Dialog("Group Name");
        });

        txt_sender_id.setOnClickListener(v -> {
            Dialog("Sender Id");
        });


        txt_sms_format.setOnClickListener(v -> {
            Dialog("Sms Format");
        });

        btn_next.setOnClickListener(v -> {


            if (checkValidation()) {
                Intent intent = new Intent(SendSmsActivity.this, SendSmsPreviewActivity.class);
                intent.putExtra("Group Name", currentSmsCustomerGroup);
//                intent.putExtra("Sender Id", currentSmsIdSetting);
                intent.putExtra("Sender Id", ClsGlobal.DefaultSenderId);
                intent.putExtra("Sms title", currentMessageFormat);
                intent.putExtra("Sms Format", edit_sms_preview.getText().toString().trim());
                intent.putExtra("Bulk Sms Title", edit_bulk_sms.getText().toString().trim());
                intent.putExtra("source", "SendSmsActivity");
                intent.putExtra("Sms Type", rb_transnational.isChecked()
                        ? "Transactional" : "Promotional");
                startActivity(intent);
            }


        });


        //------------------ Dialog ClickListeners --------------------//

        adapterGroup_Name.SetOnClickListener((name, position) -> {
            dialog.dismiss();
            currentSmsCustomerGroup = name;
            txt_group_name.setText(name.getGroupName());
        });

        adapter_senderId.SetOnClickListener((obj, position) -> {
            dialog.dismiss();
            currentSmsIdSetting = obj;
            txt_sender_id.setText(obj.getSms_id());
        });

        adapter_Sms_Format_title.SetOnClickListener((obj, position) -> {
            dialog.dismiss();
            txt_sms_format.setText(obj.getTitle());
            currentMessageFormat = obj;

            // Get Sms Format by id.
            Observable.just(ClsMessageFormat.getMessageFormatById(SendSmsActivity.this,
                    String.valueOf(obj.getId())))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        edit_sms_preview.setText(String.valueOf(result));
                    });
        });
        // -------------------------------------------------------------//
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Boolean checkValidation() {
        if (edit_bulk_sms.getText() == null
                || edit_bulk_sms.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Sms Bulk Title Required!", Toast.LENGTH_SHORT).show();
            edit_bulk_sms.requestFocus();
            return false;
        }
        if (txt_group_name.getText() == null ||
                txt_group_name.getText().toString().equalsIgnoreCase("GROUP NAME")) {
            Toast.makeText(this, "Group Name Required!", Toast.LENGTH_SHORT).show();
            txt_group_name.requestFocus();
            return false;
        }

        if (edit_sms_preview.getText() == null
                || edit_sms_preview.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Message Required!", Toast.LENGTH_SHORT).show();
            edit_sms_preview.requestFocus();
            return false;
        }

        if (!rb_transnational.isChecked() && !rb_promotional.isChecked()) {
            Toast.makeText(this, "Sms Type Required!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void Dialog(String mode) {

        RecyclerView rv;
        LinearLayout nodata_layout;

        final EditText edit_search;
        dialog = new Dialog(SendSmsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_pager_fragmet);
//        dialogvendor.setTitle("Select Customer");
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);

        rv = dialog.findViewById(R.id.rv);
        nodata_layout = dialog.findViewById(R.id.nodata_layout);
        edit_search = dialog.findViewById(R.id.edit_search);
        img_clear = dialog.findViewById(R.id.img_clear);
        rv.setLayoutManager(new LinearLayoutManager(SendSmsActivity.this));


        if (mode.equalsIgnoreCase("Group Name")) {
            rv.setAdapter(adapterGroup_Name);
        } else if (mode.equalsIgnoreCase("Sender Id")) {
            rv.setAdapter(adapter_senderId);
        } else if (mode.equalsIgnoreCase("Sms Format")) {
            rv.setAdapter(adapter_Sms_Format_title);
        }

        img_clear.setOnClickListener(v -> edit_search.setText(""));


        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            ProgressDialog pDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(SendSmsActivity.this);
                pDialog.setCancelable(false);
                pDialog.setMessage("Loading...");
                pDialog.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (mode.equalsIgnoreCase("Group Name")) {
                    listSms_Group = ClsSmsCustomerGroup
                            .getGroupAllList(SendSmsActivity.this, "");
                    Log.e("--Sales--", "listSms_Group: " + listSms_Group.size());

//                    Gson gson = new Gson();
//                String jsonInString = gson.toJson(listSms_Group);
//                Log.e("--Sales--", "listSms_Group: " + jsonInString);

                } else if (mode.equalsIgnoreCase("Sender Id")) {
//                    listSms_id = ClsSmsIdSetting.getList(SendSmsActivity.this, "");
                    listSms_id = ClsSmsIdSetting.getList(SendSmsActivity.this, " AND [active]='YES' ORDER BY [sms_id] ASC");

                } else if (mode.equalsIgnoreCase("Sms Format")) {
                    listMessageFormat = ClsMessageFormat.getList(SendSmsActivity.this);

                }


                return null;
            }


            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }


                if (mode.equalsIgnoreCase("Group Name")) {
                    if (listSms_Group.size() > 0) {
                        adapterGroup_Name.AddItems(listSms_Group);
                    }
                } else if (mode.equalsIgnoreCase("Sender Id")) {
                    adapter_senderId.AddItems(listSms_id);
                } else if (mode.equalsIgnoreCase("Sms Format")) {
                    adapter_Sms_Format_title.AddItems(listMessageFormat);
                }


            }

        };
        asyncTask.execute();

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("String", "afterTextChanged " + s.toString());
                Log.e("String", "afterTextChanged " + s.toString());


                if (mode.equalsIgnoreCase("Group Name")) {

                    filter(s.toString(), mode, adapterGroup_Name);
                } else if (mode.equalsIgnoreCase("Sender Id")) {
                    filter(s.toString(), mode, adapter_senderId);
                } else if (mode.equalsIgnoreCase("Sms Format")) {
                    filter(s.toString(), mode, adapter_Sms_Format_title);
                }

//                    filter1(s.toString());


            }
        });

        dialog.show();

    }


    <T> void filter(String text, String mode, T adapter) {

        if (mode.equalsIgnoreCase("Group Name")) {
            filter_list_SmsCustomerGroup = StreamSupport.stream(listSms_Group)
                    .filter(str -> str.getGroupName().toLowerCase().contains(text.toLowerCase()))
                    .collect(Collectors.toList());
            SmsGroupItemAdapter myAdapter = (SmsGroupItemAdapter) adapter;

            myAdapter.AddItems(filter_list_SmsCustomerGroup);

//                myAdapter.AddItems(listSms_Group);

            Log.e("String", "text " + text.isEmpty());
            if (text.isEmpty()) {
                Log.e("String", "text " + text.equalsIgnoreCase(""));
                myAdapter.AddItems(listSms_Group);
            }

            Log.e("String", "filter_list_SmsCustomerGroup " + filter_list_SmsCustomerGroup.size());

        } else if (mode.equalsIgnoreCase("Sender Id")) {
            filter_list_Sms_id = StreamSupport.stream(listSms_id)
                    .filter(str -> str.getSms_id().toLowerCase().contains(text.toLowerCase()))
                    .collect(Collectors.toList());

            SenderIdAdapter myAdapter = (SenderIdAdapter) adapter;

            myAdapter.AddItems(filter_list_Sms_id);


            if (text.isEmpty()) {
                myAdapter.AddItems(listSms_id);
            }

            Log.e("String", "filter_list_Sms_id " + filter_list_Sms_id.size());

        } else if (mode.equalsIgnoreCase("Sms Format")) {
            filter_list_MessageFormat = StreamSupport.stream(listMessageFormat)
                    .filter(str -> str.getTitle().toLowerCase().contains(text.toLowerCase()))
                    .collect(Collectors.toList());

            SmsFormatTitleAdapter myAdapter = (SmsFormatTitleAdapter) adapter;

            myAdapter.AddItems(filter_list_MessageFormat);


            if (text.isEmpty()) {
                myAdapter.AddItems(listMessageFormat);
            }
            Log.e("String", "filter_list_MessageFormat " + filter_list_MessageFormat.size());
        }


    }


    private void CheckSmsBalance() {

        if (ClsGlobal.CheckInternetConnection(SendSmsActivity.this)) {
            InterfaceCheckCustCreditSMS interfaceCheckCustCreditSMS =
                    ApiClient.getDemoInstance().create(InterfaceCheckCustCreditSMS.class);

            Log.e("--URL--", "API: " + ApiClient.getDemoInstance().toString());


            ClsUserInfo clsUserInfo = ClsGlobal.getUserInfo(SendSmsActivity.this);

            Call<ClsCheckCustCreditSMSParams> objCall =
                    interfaceCheckCustCreditSMS.value(clsUserInfo.getUserId());

            Log.e("--URL--", "objCall: " + objCall.request().url());


            objCall.enqueue(new Callback<ClsCheckCustCreditSMSParams>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<ClsCheckCustCreditSMSParams> call, Response<ClsCheckCustCreditSMSParams> response) {
                    if (response.body() != null) {
//                        String _response = response.body().getSuccess();
                        String _message = response.body().getMessage();
                        int promotional = response.body().getPromotional();
                        Log.e("--URL--", "promotional: " + promotional);
//                        Toast.makeText(SendSmsActivity.this, String.valueOf(promotional),Toast.LENGTH_LONG).show();
                        if (_message.equalsIgnoreCase("Success")) {

//                                Toast.makeText(SendSmsPreviewActivity.this,"Success",Toast.LENGTH_LONG).show();
                            rb_transnational.setText("Transactional (Bal: " +
                                    response.body().getTransactional() + ")");

                            rb_promotional.setText("Promotional (Bal: " +
                                    response.body().getPromotional() + ")");

                        }

                    }
                }

                @Override
                public void onFailure(Call<ClsCheckCustCreditSMSParams> call, Throwable t) {
//                    Toast.makeText(SendSmsActivity.this, "Error While Sending Messages!", Toast.LENGTH_LONG).show();
                }
            });
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (finishFlag) {
            finish();
            finishFlag = false;
        }

    }
}
