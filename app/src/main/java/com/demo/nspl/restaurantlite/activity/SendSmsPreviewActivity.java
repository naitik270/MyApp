package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;

import com.demo.nspl.restaurantlite.A_Test.Tools;
import com.demo.nspl.restaurantlite.Adapter.DisplayCustomerNameForSmsAdapter;
import com.demo.nspl.restaurantlite.Adapter.SmsPreviewAdapter;
import com.demo.nspl.restaurantlite.AsyncTaskReport.SmsCustomerListAsyncTask;
import com.demo.nspl.restaurantlite.AsyncTaskReport.SmsPreviewAsyncTask;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.SMS.ClsMessageFormat;
import com.demo.nspl.restaurantlite.SMS.ClsSmsCustomerGroup;
import com.demo.nspl.restaurantlite.SMS.ClsSmsIdSetting;
import com.demo.nspl.restaurantlite.backGroundTask.SendBulkSmsWorker;
import com.demo.nspl.restaurantlite.classes.ClsBulkSMSLog;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster;
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;
import com.demo.nspl.restaurantlite.classes.ClsSMSBulkMaster;
import com.demo.nspl.restaurantlite.classes.ClsSMSLogs;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.Create_OneTimeWorkRequest;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getDefaultPaymentMassage;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getSamplePreview;
import static com.demo.nspl.restaurantlite.classes.ClsPaymentMaster.queryByOrderID;
import static com.demo.nspl.restaurantlite.classes.ClsSMSLogs.InsertList;

public class SendSmsPreviewActivity extends AppCompatActivity {

    private static final String TAG = SendSmsPreviewActivity.class.getName();
    //    String message = "";
    ClsSmsCustomerGroup currentSmsCustomerGroup = new ClsSmsCustomerGroup();
    ClsSmsIdSetting currentSmsIdSetting = new ClsSmsIdSetting();
    ClsMessageFormat currentMessageFormat = new ClsMessageFormat();
    String bulk_sms_title = "", sms_format = "", Sms_Type = "", source = "";
    ClsInventoryOrderMaster clsInventoryOrderMaster;
    TextView title, txt_sms_type, txt_sender_id, txt_message_format,
            txt_message_length, txt_transactional, txt_promotional,
            txt_group_info, txt_credit_count,txt_customer_count;
    Toolbar toolbar;
    RecyclerView rv_msg_list;
    List<ClsInventoryOrderMaster> list = new ArrayList<>();
    LinearLayout No_connection_lyout;
    MenuItem item_action_done;
    SmsPreviewAdapter group_member_adapter;
    RelativeLayout main_layout;
    public static boolean finishFlag = false;
    ProgressDialog pd;
    ImageView iv_preview_customer;
    SmsPreviewAdapter smsPreviewAdapter;
    List<ClsBulkSMSLog> lstAsynctask = new ArrayList<>();


    @SuppressLint({"SetTextI18n", "CheckResult"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms_preview);

        title = findViewById(R.id.title);
        txt_sms_type = findViewById(R.id.txt_sms_type);
        iv_preview_customer = findViewById(R.id.iv_preview_customer);
        txt_sender_id = findViewById(R.id.txt_sender_id);
        txt_message_format = findViewById(R.id.txt_message_format);
        txt_message_length = findViewById(R.id.txt_message_length);
        txt_transactional = findViewById(R.id.txt_transactional);
        txt_promotional = findViewById(R.id.txt_promotional);
        txt_group_info = findViewById(R.id.txt_group_info);
        txt_credit_count = findViewById(R.id.txt_credit_count);
        rv_msg_list = findViewById(R.id.rv_msg_list);
        txt_customer_count = findViewById(R.id.txt_customer_count);

        No_connection_lyout = findViewById(R.id.No_connection_lyout);
        main_layout = findViewById(R.id.main_layout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        source = getIntent().getStringExtra("source");

        group_member_adapter = new SmsPreviewAdapter(this);

        if (source != null && source.equalsIgnoreCase("PendingPayment")) {

            if (ClsGlobal.CheckInternetConnection(SendSmsPreviewActivity.this)) {

                list = (List<ClsInventoryOrderMaster>)
                        getIntent().getSerializableExtra("List");

                Gson gson = new Gson();
                String jsonInString = gson.toJson(list);
                Log.d("Check", "list- " + jsonInString);

                title.setText("Payment Reminder");
                txt_sms_type.setText("Transitional");

                txt_sender_id.setText(ClsGlobal.DefaultSenderId);

                PreparingPendingPaymentMessages();
//                txt_group_info.setText("Group Name: " +
//                        currentSmsCustomerGroup.getGroupName() +
//                        " (" + currentSmsCustomerGroup.getTotalCount() + ")");

            } else {
                main_layout.setVisibility(View.GONE);
                No_connection_lyout.setVisibility(View.VISIBLE);
            }

        } else if (source != null && source.equalsIgnoreCase("SendSmsActivity")) {


            if (ClsGlobal.CheckInternetConnection(SendSmsPreviewActivity.this)) {
                currentSmsCustomerGroup = (ClsSmsCustomerGroup) getIntent().getSerializableExtra("Group Name");
//            currentSmsIdSetting = (ClsSmsIdSetting) getIntent().getSerializableExtra("Sender Id");
                ClsGlobal.DefaultSenderId = getIntent().getStringExtra("Sender Id");
                currentMessageFormat = (ClsMessageFormat) getIntent().getSerializableExtra("Sms title");
                sms_format = getIntent().getStringExtra("Sms Format");
                Sms_Type = getIntent().getStringExtra("Sms Type");


//                Gson gson = new Gson();
//                String jsonInString = gson.toJson(currentSmsCustomerGroup);
//                Log.d("--GSON--", "currentSmsCustomerGroup- " + jsonInString);

                title.setText(getIntent().getStringExtra("Bulk Sms Title") != null ?
                        getIntent().getStringExtra("Bulk Sms Title") : "");

                txt_sms_type.setText(Sms_Type);
//            txt_sender_id.setText(currentSmsIdSetting.getSms_id());
                txt_sender_id.setText(ClsGlobal.DefaultSenderId);
                txt_message_format.setText(String.valueOf(getSamplePreview(sms_format,
                        SendSmsPreviewActivity.this)));

                txt_group_info.setText("Group Name: " +
                        currentSmsCustomerGroup.getGroupName() +
                        " (" + currentSmsCustomerGroup.getTotalCount() + ")");

//            // Get Total Members in group count.
//            Observable.just(ClsSmsCustomerGroup.getGroupMemberCount(SendSmsPreviewActivity.this,
//                    String.valueOf(currentSmsCustomerGroup.getGroupId())))
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(result -> {
//
//                        txt_group_info.setText(String.valueOf("Group Name: " + currentSmsCustomerGroup.getGroupName() +
//                                " (" + result + ")"));
//
//                    });

                txt_message_length.setText(String.valueOf(
                        txt_message_format.getText().toString().length()));

//                txt_credit_count.setText(" (" + (int) Math.ceil(
//                        (double) txt_message_format.getText().toString().length() / 145) + ")");


            } else {
                main_layout.setVisibility(View.GONE);
                No_connection_lyout.setVisibility(View.VISIBLE);
            }
        }


        adapter = new DisplayCustomerNameForSmsAdapter(SendSmsPreviewActivity.this);
        displayCustomerDialogList();
        fillCustomerList();
        fillSmsFormatByCustomers();

        iv_preview_customer.setOnClickListener(v ->
                dialogCustomer.show());
    }


    void fillSmsFormatByCustomers() {

        group_member_adapter = new SmsPreviewAdapter(SendSmsPreviewActivity.this);
        rv_msg_list.setLayoutManager(new LinearLayoutManager(this));
        rv_msg_list.setAdapter(group_member_adapter);

        group_member_adapter.SetOnClickListener((obj, position) -> {

            enableActionMode(position);

        });

        actionModeCallback = new ActionModeCallback();

        SmsPreviewAsyncTask smsPreviewAsyncTask = new SmsPreviewAsyncTask(sms_format, String.valueOf(currentSmsCustomerGroup.getGroupId()),
                Integer.parseInt(txt_message_length.getText().toString().trim()),
                SendSmsPreviewActivity.this,
                group_member_adapter,
                progress_bar,
                rv_msg_list,txt_credit_count);

        smsPreviewAsyncTask.OnCustomerSmsListClick(lstClsBulkSMSLog -> {
            lstAsynctask = lstClsBulkSMSLog;
            txt_customer_count.setText("Total count: " + lstAsynctask.size());
        });

        smsPreviewAsyncTask.execute();


    }

    DisplayCustomerNameForSmsAdapter adapter;
    RecyclerView rv_customer_list;
    ProgressBar progress_bar;
    Dialog dialogCustomer;

    void displayCustomerDialogList() {

        dialogCustomer = new Dialog(SendSmsPreviewActivity.this);
        dialogCustomer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCustomer.setContentView(R.layout.customer_display_for_msg);
        dialogCustomer.setCanceledOnTouchOutside(true);
        dialogCustomer.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogCustomer.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogCustomer.getWindow().setAttributes(lp);

        progress_bar = dialogCustomer.findViewById(R.id.progress_bar);
        rv_customer_list = dialogCustomer.findViewById(R.id.rv_customer_list);
        rv_customer_list.setLayoutManager(new LinearLayoutManager(SendSmsPreviewActivity.this));
        rv_customer_list.setAdapter(adapter);

    }

    void fillCustomerList() {
        new SmsCustomerListAsyncTask(currentSmsCustomerGroup.getGroupId(), SendSmsPreviewActivity.this
                , adapter, progress_bar, rv_customer_list).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        MenuItem item = menu.findItem(R.id.action_plus);
        item.setVisible(false);

        item_action_done = menu.findItem(R.id.action_done);
        item_action_done.setIcon(ContextCompat.getDrawable(
                SendSmsPreviewActivity.this, R.drawable.ic_send));

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_done:
                InsertAndSmsApi();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private ActionMode actionMode;
    private ActionModeCallback actionModeCallback;

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        group_member_adapter.toggleSelection(position);
        int count = group_member_adapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private void deleteInboxes() {
        List<Integer> selectedItemPositions = group_member_adapter.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            group_member_adapter.removeData(selectedItemPositions.get(i));
        }
        group_member_adapter.notifyDataSetChanged();
        txt_customer_count.setText("Total count: " + lstAsynctask.size());

    }




    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//            Tools.setSystemBarColor(SendSmsPreviewActivity.this, R.color.orange);
            mode.getMenuInflater().inflate(R.menu.menu_delete, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_delete) {


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SendSmsPreviewActivity.this);
                alertDialog.setTitle("Confirm...");
                alertDialog.setMessage("Are you sure you want delete?");
                alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
                alertDialog.setPositiveButton("YES", (dialog, which) -> {

                    deleteInboxes();
                    mode.finish();
                });
                alertDialog.setNegativeButton("NO", (dialog, which) -> {
                    dialog.dismiss();
                    dialog.cancel();
                });

                // Showing Alert Message
                alertDialog.show();


//                updateInBoxes();


                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            group_member_adapter.clearSelections();
            actionMode = null;
            Tools.setSystemBarColor(SendSmsPreviewActivity.this, R.color.darkblue);
        }
    }




    private void InsertAndSmsApi() {
        //TODO
//        if (txt_message_format.getText() != null
//                && txt_message_format.getText().toString().length() > 0) {
            if (source != null && source.equalsIgnoreCase("PendingPayment")) {

                @SuppressLint("StaticFieldLeak")
                AsyncTask<Void, Void, Integer> asyncTask =
                        new AsyncTask<Void, Void, Integer>() {

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                pd = ClsGlobal._prProgressDialog(SendSmsPreviewActivity.this,
                                        "Sending Messages Please Wait...", true);
                                pd.show();
                            }

                            @SuppressLint("WrongConstant")
                            @Override
                            protected Integer doInBackground(Void... voids) {

                                List<ClsSMSLogs> insertList = new ArrayList<>();

                                for (ClsInventoryOrderMaster item : list) {
                                    ClsSMSLogs clsSMSLogs = new ClsSMSLogs();

                                    clsSMSLogs.setOrderId(item.getOrderID());
                                    clsSMSLogs.setOrderNo(item.getOrderNo());
                                    clsSMSLogs.setMobileNo(item.getMobileNo());
                                    clsSMSLogs.setCustomer_Name(item.getCustomerName());
                                    clsSMSLogs.setEntry_Datetime(ClsGlobal.getCurruntDateTime());
                                    clsSMSLogs.setMessage(item.getMessage().trim()
                                            .replace("'", "''"));
                                    clsSMSLogs.setInvoice_attachment("No");
                                    clsSMSLogs.setStatus("Pending");
                                    clsSMSLogs.setSmsStatus("Pending");
                                    clsSMSLogs.setUtilizeType("");
                                    clsSMSLogs.setType("Pending Payment");
                                    clsSMSLogs.setCredit((int) Math.ceil((double)
                                            item.getMessage().length() / 145));
                                    clsSMSLogs.setSendSMSID("");
                                    clsSMSLogs.setRemark("");

                                    insertList.add(clsSMSLogs);
                                }


                                int result = InsertList(insertList, SendSmsPreviewActivity.this);

                                if (result > 0){
                                    // TODO
//                                    // If there is InternetConnection call sms api
//                                    if (ClsGlobal.CheckInternetConnection(SendSmsPreviewActivity.this)) {
//
//                                        SendBillSms sendBillSms = new SendBillSms(SendSmsPreviewActivity.this);
//                                        sendBillSms.SendSms(SalesSMSLogsMaster_LAST_INSERTED_ID);
//                                        SalesSMSLogsMaster_LAST_INSERTED_ID = "";
//
//                                    } else {
//                                        Constraints myConstraints = new Constraints.Builder()
//                                                .setRequiredNetworkType(NetworkType.CONNECTED)
//                                                .build();
//
//                                        Create_OneTimeWorkRequest(SalesSmsWorker.class,
//                                                "SalesSms", "KEEP",
//                                                null, myConstraints);
//
//
//                                    }
                                }


                                return result;
                            }


                            @Override
                            protected void onPostExecute(Integer result) {
                                super.onPostExecute(result);
                                pd.cancel();

                                if (result > 0){
                                    Toast.makeText(SendSmsPreviewActivity.this,"Messages are Send Successfully " +
                                            "will be delivered shortly.",Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(SendSmsPreviewActivity.this,"Failed to Send Messages!"
                                            ,Toast.LENGTH_LONG).show();
                                }

                                finish();

                            }

                        };
                asyncTask.execute();

            } else if (source != null && source.equalsIgnoreCase("SendSmsActivity")) {
                @SuppressLint("StaticFieldLeak")
                AsyncTask<Void, Void, Integer> asyncTask =
                        new AsyncTask<Void, Void, Integer>() {

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                pd = ClsGlobal._prProgressDialog(SendSmsPreviewActivity.this,
                                        "Loading...", true);
                                pd.show();
                            }

                            @SuppressLint("WrongConstant")
                            @Override
                            protected Integer doInBackground(Void... voids) {

                                int Sendresult = 0;

                                try {
                                    Thread.sleep(400);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                ClsSMSBulkMaster insertSMSBulkMaster = new ClsSMSBulkMaster();
                                insertSMSBulkMaster.setServerBulkID("Pending Status");

                                if (txt_message_format.getText().length() > 0) {
                                    insertSMSBulkMaster.setMessage(
                                            txt_message_format.getText().toString().trim());
                                }

                                insertSMSBulkMaster.setMessageLength(Integer.parseInt(txt_message_length.getText().toString().trim()));
                                insertSMSBulkMaster.setGroupName(currentSmsCustomerGroup.getGroupName());
                                insertSMSBulkMaster.setTotalCustomers(currentSmsCustomerGroup.getTotalCount());
//                            insertSMSBulkMaster.setSenderID(currentSmsIdSetting.getSms_id());
                                insertSMSBulkMaster.setSenderID(ClsGlobal.DefaultSenderId);
                                insertSMSBulkMaster.setMessageType(txt_sms_type.getText().toString());
                                insertSMSBulkMaster.setTitle(currentMessageFormat.getTitle());


                                int result = ClsSMSBulkMaster.Insert(insertSMSBulkMaster,
                                        SendSmsPreviewActivity.this);
                                if (result > 0) {

                                    Log.e(TAG, "insertSMSBulkMaster Successfully: " + result);

                                    @SuppressLint("WrongConstant")
                                    SQLiteDatabase db =
                                            openOrCreateDatabase(ClsGlobal.Database_Name,
                                                    Context.MODE_APPEND, null);

                                    db.beginTransaction();

                                    List<ClsBulkSMSLog> insertList = ClsSmsCustomerGroup
                                            .getCustomerListFromGroupByID(sms_format,
                                                    String.valueOf(currentSmsCustomerGroup.getGroupId()),
                                                    Integer.parseInt(
                                                            txt_message_length.getText()
                                                                    .toString().trim()), db,
                                                    SendSmsPreviewActivity.this);

                                    Log.e(TAG, "insertList: size " + insertList.size());
                                    Sendresult = ClsBulkSMSLog.Insert(
                                            insertList, SendSmsPreviewActivity.this, db);

                                    if (Sendresult > 0) {

                                        Log.e(TAG, "insertSMSLog: Successfully " + result);
//
                                        Data inputData = new Data.Builder()
                                                .putString(SendBulkSmsWorker.EXTRA_SMS_TYPE,
                                                        txt_sms_type.getText().toString())
                                                .build();
//
//                                    // if there is no InternetConnection then create new Worker.
                                        Constraints myConstraints = new Constraints.Builder()
                                                .setRequiredNetworkType(NetworkType.CONNECTED)
                                                .build();

                                        Create_OneTimeWorkRequest(SendBulkSmsWorker.class,
                                                "BulkSms", "KEEP",
                                                inputData, myConstraints);
                                    } else {
                                        Log.e(TAG, "insertSMSLog: failed " + result);
                                    }

                                    db.setTransactionSuccessful();
                                    db.endTransaction();
                                    db.close();

                                } else {
                                    Log.e(TAG, "insertSMSBulkMaster: failed " + result);
                                }


                                return Sendresult;
                            }


                            @Override
                            protected void onPostExecute(Integer result) {
                                super.onPostExecute(result);
                                pd.cancel();
                                if (result > 0) {
                                    Toast.makeText(SendSmsPreviewActivity.this,
                                            "The Sms will be Send Shortly.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SendSmsPreviewActivity.this,
                                            "Failed To Send Bulk Sms.", Toast.LENGTH_SHORT).show();
                                }
                                finishFlag = true;
                                finish();
                            }

                        };
                asyncTask.execute();


//                finishFlag = true;
//                finish();
            }


//        }


    }

    private void PreparingPendingPaymentMessages() {
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Integer> asyncTask =
                new AsyncTask<Void, Void, Integer>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        pd = ClsGlobal._prProgressDialog(SendSmsPreviewActivity.this,
                                "Preparing Messages...", true);
                        pd.show();
                    }

                    @SuppressLint("WrongConstant")
                    @Override
                    protected Integer doInBackground(Void... voids) {


                        list = StreamSupport.stream(list)
                                .filter(ClsInventoryOrderMaster::isSelected)
                                .collect(Collectors.toList());

                        for (ClsInventoryOrderMaster current : list) {
                            ClsPaymentMaster clsPaymentMaster =
                                    queryByOrderID(String.valueOf(current.getOrderID()),
                                            SendSmsPreviewActivity.this);
                            clsPaymentMaster.setAdjustmentAmount(current.getAdjumentAmount());
                            clsPaymentMaster.setOrderNo(current.getOrderNo());

                            String message =
                                    getDefaultPaymentMassage(SendSmsPreviewActivity.this,
                                            clsPaymentMaster, "Payment Pending");
                            current.setMessage(message);

                            Log.e("Check", "message: " + message);

                            list.set(list.indexOf(current), current);
                        }

                        return 0;
                    }


                    @Override
                    protected void onPostExecute(Integer result) {
                        super.onPostExecute(result);
                        pd.cancel();

                        txt_group_info.setText("Group Name: " +
                                "Payment Reminder Group" +
                                " (" + list.size() + ")");

//                        finish();
                    }

                };
        asyncTask.execute();
    }

}
