package com.demo.nspl.restaurantlite.backGroundTask;

import android.content.Context;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.demo.nspl.restaurantlite.Global.ApiClient;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsSendSmsParams;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsSendSmsResponse;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.SendTo;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceSendSms;
import com.demo.nspl.restaurantlite.SMS.ClsSmsIdSetting;
import com.demo.nspl.restaurantlite.UploadFileToServer.ConnectionCheckBroadcast;
import com.demo.nspl.restaurantlite.classes.ClsBulkSMSLog;
import com.demo.nspl.restaurantlite.classes.ClsSMSBulkMaster;
import com.demo.nspl.restaurantlite.classes.ClsUserInfo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.Create_OneTimeWorkRequest;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.DefaultSenderId;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getCurruntDateTime;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getEntryDateFormat;

public class SendBulkSmsWorker extends Worker {

    private Context context;
    ClsUserInfo objClsUserInfo;
    private SQLiteDatabase db;
    boolean timer = true;
    public static final String EXTRA_MESSAGE = "Message", EXTRA_SMSCUSTOMERGROUP = "SmsCustomerGroup",
            EXTRA_SMS_ID = "Sms_id", EXTRA_TOTALCOUNT = "TotalCount", EXTRA_SMS_TYPE = "sms_type",
            EXTRA_MESSAGEFORMAT_TITLE = "MessageFormat_Title",
            EXTRA_GROUPID = "GroupId", EXTRA_MESSAGE_TYPE = "Message_Type";

    String message = "", DefaultSmsIdSetting = "", SmsCustomerGroup = "", Sms_id = "",
            sms_type = "", MessageFormat_Title = "", messageType = "";
    int TotalCount, GroupId;
    CountDownLatch latch;
    private ConnectionCheckBroadcast mConnectionCheckBroadcast;

    private static final String TAG = SendBulkSmsWorker.class.getSimpleName();

    public SendBulkSmsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        ClsGlobal.appendSmsLog(getEntryDateFormat(getCurruntDateTime()) +
                " ------------------------------ SendBulkSmsWorker Started---------------------------   \n");

        ClsGlobal.appendSmsLog(getEntryDateFormat(getCurruntDateTime()) +
                "SendBulkSmsWorker call:   \n");

        if (ClsGlobal.CheckInternetConnection(context)) {
            latch =new CountDownLatch(1);
//            messageType = getInputData().getString(EXTRA_SMS_TYPE);
            mConnectionCheckBroadcast = new ConnectionCheckBroadcast("BulkSms");
//            mConnectionCheckBroadcast.setData(messageType);

            context.registerReceiver(mConnectionCheckBroadcast, new IntentFilter(
                    "android.net.conn.CONNECTIVITY_CHANGE"));

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_PRIVATE, null);


            // Get User Details from SharedPreferences.
            objClsUserInfo = ClsGlobal.getUserInfo(context);

            //Get DefaultSenderID.
            getDefaultSenderID();

            List<SendTo> sendToList = new ArrayList<>();

            // get Pending Bulk sms list
            List<ClsSMSBulkMaster> bulkSmsList =
                    ClsSMSBulkMaster.getList(" AND [serverBulkID] = 'Pending Status' ",
                            "",context, db);

            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                    "bulkSmsList.size(): " + bulkSmsList.size()+ " \n");

            Gson gson = new Gson();
            String jsonInString = gson.toJson(bulkSmsList);
            Log.d("--Bulk--", "Test: " + jsonInString);

            Log.d(TAG, "bulkSmsList: " + bulkSmsList.size());

            for (ClsSMSBulkMaster clsSMSBulkMaster : bulkSmsList) {
                // Get All Pending Messages.
                List<ClsBulkSMSLog> pendingSmsList = ClsBulkSMSLog.getList(
                        " AND [Status] = 'Pending' AND  [bulkID] = "
                                + clsSMSBulkMaster.getBulkID()

                        , "", db);

                //Log.e(TAG, "--size:-" + pendingSmsList.size());

                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        "pendingSmsList.size(): " + pendingSmsList.size()+ " \n");

                Gson gson1 = new Gson();
                String jsonInString1 = gson1.toJson(pendingSmsList);
                Log.d(TAG, "bulkSmsList: " + jsonInString1);

                for (ClsBulkSMSLog clsSMSLog : pendingSmsList) {

                    sendToList.add(new SendTo(clsSMSLog.getMobile(), clsSMSLog.getCustomerName()
                            , objClsUserInfo.getBusinessname(), objClsUserInfo.getGstnumber(),
                            objClsUserInfo.getCity(), objClsUserInfo.getPincode(),
                            clsSMSLog.getMessage()));

                }


                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                InterfaceSendSms interfaceSendSms = ApiClient.getDemoInstance()
                        .create(InterfaceSendSms.class);

                ClsSendSmsParams clsSendSmsParams = new ClsSendSmsParams();
                clsSendSmsParams.setCustomerCode(objClsUserInfo.getUserId());
//                clsSendSmsParams.setCustomerCode("CTA001"); // CTA001
//                clsSendSmsParams.setsMSType(messageType);
                clsSendSmsParams.setsMSType(clsSMSBulkMaster.getMessageType());

                clsSendSmsParams.setSource("Bulk");
                clsSendSmsParams.setSenderID(DefaultSmsIdSetting);

                Gson gson2 = new Gson();
                String jsonArry = gson2.toJson(sendToList);

                clsSendSmsParams.setSendTo(jsonArry);

//            clsSendSmsParams.setMessage(message);


//            clsSendSmsParams.setMessageLength(String.valueOf(message.length()));
//        if (DefaultMessageFormat.contains("FOCUS BUSINESS SOLUTIONS PVT")){
//            clsSendSmsParams.setMessage(DefaultMessageFormat);
//            clsSendSmsParams.setMessageLength(String.valueOf(DefaultMessageFormat.length()));
//        }else {
//            clsSendSmsParams.setMessage(finalMessage);
//            clsSendSmsParams.setMessageLength(String.valueOf(finalMessage.length()));
//        }

                clsSendSmsParams.setProductName(ClsGlobal.AppName);

                Gson gsonOrder = new Gson();
                String response1 = gsonOrder.toJson(clsSendSmsParams);
                Log.d(TAG, "body: " + response1);

                ClsGlobal.appendSmsLog(getEntryDateFormat(getCurruntDateTime()) +
                        "SendBulkSmsWorker Gson: " + response1+ " \n");



                Call<ClsSendSmsResponse> sendSms = interfaceSendSms.SendSms(clsSendSmsParams);

                sendSms.enqueue(new Callback<ClsSendSmsResponse>() {
                    @Override
                    public void onResponse(Call<ClsSendSmsResponse> call,
                                           Response<ClsSendSmsResponse> response) {

                        ClsGlobal.appendSmsLog(getEntryDateFormat(getCurruntDateTime()) +
                                "SendBulkSmsWorker onResponse call:   \n");
//                        timer = false;
                        if (response.body() != null && response.code() == 200) {


                            Log.e(TAG, "--Response:-" + response.message());

                            Log.e(TAG, "--getSuccess:-" + response.body().getSuccess());

                            Log.e(TAG, "--getMessageSales:-" + response.body().getMessage());
                            Log.e(TAG, "--getSendSMSID:-" + response.body().getSendSMSID());
                            Log.e(TAG, "--toString:-" + response.body().toString());

                            Gson gsonOrder = new Gson();
                            String response1 = gsonOrder.toJson(response.body());
                            Log.e(TAG, "--response1:-" + response1);
                            // Updating Sms Status from pending to send in SMSLog.


                            int updateResult = ClsBulkSMSLog.UpdateList(pendingSmsList,
                                    response.body().getSendSMSID()
                                    , response.body().getMessage(), db);


                            // Updating ServerBulkID in SMSBulkMaster.
//                            clsSMSBulkMaster.setRemark(response.body().getMessage());
                            clsSMSBulkMaster.setServerBulkID(response.body().getSendSMSID());

                            ClsGlobal.appendSmsLog(getEntryDateFormat(getCurruntDateTime()) +
                                    "SendBulkSmsWorker response.body().getSendSMSID(): "
                                            +response.body().getSendSMSID()+ "\n");

                            ClsSMSBulkMaster.Update(clsSMSBulkMaster, db);
//                            CheckSmsStatus();

                        }

                        latch.countDown();
                    }

                    @Override
                    public void onFailure(Call<ClsSendSmsResponse> call, Throwable t) {
//                        timer = false;
                        latch.countDown();
                        Log.e(TAG, "--onFailure:-" + t.getMessage());
                        Log.e(TAG, "onFailure: " + t.getMessage());

                        ClsGlobal.appendSmsLog(getEntryDateFormat(getCurruntDateTime()) +
                                "SendBulkSmsWorker onFailure: "
                                +t.getMessage()+ "\n");
                    }
                });


//                // to wait for the response from retrofit.
//                while (timer) {
//
////                    Log.e(TAG, "--timer:-" + timer);
//                    if (!timer) {
//
//                        break;
//                    }
//                }
//
//                timer = true;

                try {

                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

//            CheckSmsStatus();

            // CheckSmsStatus Worker.
            Data inputData = new Data.Builder()
                    .putString(CheckSmsStatusTask.EXTRA_MODE,
                            "BulkSms")
                    .build();


            Create_OneTimeWorkRequest(CheckSmsStatusTask.class,
                    "CheckSmsStatus", "KEEP", inputData, null);

        } else {

            // Cancel worker by UniqueWorkName.
            ClsGlobal.cancelWorkerTask("BulkSms");

//            Data inputData = new Data.Builder()
//                    .putString(SendBulkSmsWorker.EXTRA_SMS_TYPE,
//                            messageType)
//                    .build();

            // if there is no InternetConnection then create new Worker.
            Constraints myConstraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            Create_OneTimeWorkRequest(SendBulkSmsWorker.class,
                    "BulkSms", "KEEP", null, myConstraints);
        }

        ClsGlobal.appendSmsLog(getEntryDateFormat(getCurruntDateTime()) +
                "SendBulkSmsWorker Result.success(): "
                +Result.success()+ "\n");

        ClsGlobal.appendSmsLog(getEntryDateFormat(getCurruntDateTime()) +
                " ------------------------------ SendBulkSmsWorker finesh---------------------------   \n");

        return Result.success();
    }


    private void getDefaultSenderID() {

        ClsSmsIdSetting clsSmsIdSetting = ClsSmsIdSetting.getSmsIdSettingByDefault(db);
        if (clsSmsIdSetting.getSms_id().length() > 0) {
            DefaultSmsIdSetting = clsSmsIdSetting.getSms_id();
        } else {
            DefaultSmsIdSetting = DefaultSenderId;
        }
    }
}
