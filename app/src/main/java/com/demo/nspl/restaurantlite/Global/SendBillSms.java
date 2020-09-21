package com.demo.nspl.restaurantlite.Global;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaScannerConnection;
import android.print.PDFConverter;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.NetworkType;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsSendSmsParams;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsSendSmsResponse;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.PdfFileUploadResponse;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.SendTo;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceSendSms;
import com.demo.nspl.restaurantlite.SMS.ClsSmsIdSetting;
import com.demo.nspl.restaurantlite.backGroundTask.CheckSmsStatusTask;
import com.demo.nspl.restaurantlite.backGroundTask.DeletePdfFilesWorker;
import com.demo.nspl.restaurantlite.backGroundTask.SalesSmsWorker;
import com.demo.nspl.restaurantlite.classes.ClsCustomerMaster;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderDetail;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster;
import com.demo.nspl.restaurantlite.classes.ClsQuotationMaster;
import com.demo.nspl.restaurantlite.classes.ClsQuotationOrderDetail;
import com.demo.nspl.restaurantlite.classes.ClsSMSLogs;
import com.demo.nspl.restaurantlite.classes.ClsUserInfo;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.CREATE_PDF_FILE_PATH;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.CREATE_PDF_QUOTATION_PATH_SEND;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.Create_OneTimeWorkRequest;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.DefaultSenderId;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.PDF_Download_URL;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getCurruntDateTime;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.getEntryDateFormat;
import static com.demo.nspl.restaurantlite.classes.ClsSMSLogs.UpdateSmsSendingStatus;

public class SendBillSms implements PdfFileUploader.FileUploaderCallback{

    private SQLiteDatabase db;
    ClsUserInfo objClsUserInfo;
    private Context context;
    int sms_limit = 0;
    private String DefaultMessageFormat = "", DefaultSmsIdSetting = "", finalMessage = "",
            Type = "";
    private ClsInventoryOrderMaster clsInventoryOrderMaster;
    private ClsQuotationMaster clsQuotationMaster;
    private List<ClsInventoryOrderDetail> list_Current_Order = new ArrayList<>();
    private List<ClsQuotationOrderDetail> lstQuotation = new ArrayList<>();

    boolean timer = true;
    PdfFileUploader pdfFileUploader;
    List<ClsSMSLogs> pendingSalesSmsList = new ArrayList<>();
    List<SendTo> sendToList = new ArrayList<>();
    PDFConverter converter = PDFConverter.getInstance();
    ClsSMSLogs currentClsSalesSmsLogs = new ClsSMSLogs();
    CountDownLatch latch;
    private static final String TAG = SendBillSms.class.getSimpleName();

    public SendBillSms(Context context){
        this.context = context;
    }

    public void SendSms(String lastInsertedId){

        Log.e(TAG, "doWork");
        ClsGlobal.appendSmsLog(getEntryDateFormat(getCurruntDateTime()) +
                " ------------------------------ SalesSmsWorker Started---------------------------   \n");

        ClsGlobal.appendSmsLog(getEntryDateFormat(getCurruntDateTime()) +
                " SalesSmsWorker Started.  \n");

        latch = new CountDownLatch(1);

        // Update Sales Sms Status to Expire
        // Which are older than 24 hours .
        UpdateSmsSendingStatus(context,"SalesSmsWorker");

        if (ClsGlobal.CheckInternetConnection(context)) {

            // Get User Details from SharedPreferences.
            objClsUserInfo = ClsGlobal.getUserInfo(context);

            db = context.openOrCreateDatabase(ClsGlobal.Database_Name,
                    Context.MODE_PRIVATE, null);

            // get Pending Sales sms logs from SalesSMSLogsMaster.
            if (!lastInsertedId.equalsIgnoreCase("")){

                pendingSalesSmsList =
                        ClsSMSLogs.getList("AND [Status] = 'Pending' " +
                                        "AND [SmsStatus] = 'Pending' AND [LogId] = "
                                        + lastInsertedId
                                , "", db);
            }else {
                pendingSalesSmsList =
                        ClsSMSLogs.getList("AND [Status] = 'Pending' " +
                                        "AND [SmsStatus] = 'Pending' "
                                , "", db);
            }

            ClsGlobal.appendSmsLog(getEntryDateFormat(getCurruntDateTime()) +
                    "SalesSmsWorker pendingSalesSmsList size:- " +pendingSalesSmsList.size() +"  \n");

            Log.e(TAG, "Size: " + pendingSalesSmsList.size());
            pdfFileUploader = new PdfFileUploader(context);
            pdfFileUploader.SetCallBack(this);

            Gson gsonOrder = new Gson();
            String response1 = gsonOrder.toJson(pendingSalesSmsList);
            Log.d("SalesSmsWorker", "pendingSalesSmsList : " + response1);


            if (!new File(CREATE_PDF_FILE_PATH).exists()) {
                try {
                    new File(CREATE_PDF_FILE_PATH).mkdirs();
                } catch (Exception e) {
                    Log.e(TAG, "Exception " + e.getMessage());
                }

            }

            getDefaultSenderID();


            converter.SetOnPDFCreatedListener((filePath) -> {
//                Log.e("MediaScannerConnection", "Path" + filePath);

                // Refresh directory.
                String[] files = {CREATE_PDF_FILE_PATH};
                MediaScannerConnection.scanFile(context,
                        files,
                        null,
                        (path, uri) -> {

                            ClsGlobal.appendSmsLog(getEntryDateFormat(getCurruntDateTime()) +
                                    "SalesSmsWorker path:- " + path +  "\n");
                            // scanned path and uri
                            Log.e("MediaScannerConnection", "Path" + path);
                            Log.e("MediaScannerConnection", "uri" + uri);

                            Log.e(TAG, "MediaScannerConnection ");

                            // Upload pdf File to server.
                            pdfFileUploader.Start_file_upload(new File(filePath),
                                    currentClsSalesSmsLogs.getOrderNo(), Type);

                        });

            });

            int count = 0;

            for (ClsSMSLogs clsSalesSMSLogs : pendingSalesSmsList) {
                this.currentClsSalesSmsLogs = clsSalesSMSLogs;

                Type = clsSalesSMSLogs.getType();

                count++;
                Log.e(TAG, "Counter: " + String.valueOf(count));


                if (Type.equalsIgnoreCase("Sales")) {

                    String _whereSend = " AND ORD.[OrderNo] = '" + clsSalesSMSLogs.getOrderNo() + "' "
                            .concat(" AND ORD.[OrderID] = ")
                            .concat(String.valueOf(clsSalesSMSLogs.getOrderId()));

                    // get sms Limit.
                    sms_limit = ClsInventoryOrderMaster.getSmsLimit("AND [OrderNo] = '"
                            + clsSalesSMSLogs.getOrderNo() + "' "
                            .concat(" AND [OrderID] = ")
                            .concat(String.valueOf(clsSalesSMSLogs.getOrderId())), context, db);


                    // Get bill Amount,Tax Different Amount,etc.
                    clsInventoryOrderMaster =
                            ClsInventoryOrderMaster.getOrder(clsSalesSMSLogs.getOrderNo(),
                                    " AND [OrderID] = " + clsSalesSMSLogs.getOrderId(), context, db);

                    finalMessage = ClsGlobal.getMessageSales(clsSalesSMSLogs.getMessage(),
                            clsInventoryOrderMaster, objClsUserInfo, context);

                    Log.e(TAG, "finalMessage Sales: " + finalMessage);

                    // get list of order items.
                    list_Current_Order = new ClsInventoryOrderDetail().getListForInvoicePDF(
                            _whereSend, context, db);

                    Log.e(TAG, "list_Current_Order: " + list_Current_Order.size());

                } else if (Type.equalsIgnoreCase("Quotation")) {


                    clsQuotationMaster = ClsQuotationMaster
                            .getFillQuotationOn(clsSalesSMSLogs.getOrderNo()
                                    , clsSalesSMSLogs.getOrderId(), context, db);

                    sms_limit = ClsQuotationMaster
                            .getSmsLimit_Quotation(" AND [QuotationID] = " +
                                    clsSalesSMSLogs.getOrderId() + " AND [QuotationNo] = '"
                                    + clsSalesSMSLogs.getOrderId() + "'", context, db);

                    finalMessage = ClsGlobal.getMessageQuotation(clsSalesSMSLogs.getMessage(),
                            clsQuotationMaster, objClsUserInfo,context);

                    String where = " AND [QuotationNo] = ".concat("'").concat(String.valueOf(clsSalesSMSLogs.getOrderNo())).concat("'")
                            .concat(" AND [QuotationID] = ").concat(String.valueOf(clsSalesSMSLogs.getOrderId()));


                    lstQuotation = new ClsQuotationOrderDetail().getQuotationDetailList(where,
                            context, db);
                    Log.e(TAG, "lstQuotation: " + lstQuotation.size());
                }


                Log.e("clsPrintPdf", "getInvoice_attachment: " + clsSalesSMSLogs.getInvoice_attachment());
                Log.e("clsPrintPdf", "finalMessage: " + finalMessage);

                // Invoice attachment is == Yes then Generate Pdf file and Upload.
                if (clsSalesSMSLogs.getInvoice_attachment().equalsIgnoreCase("Yes")) {
                    Log.e("clsPrintPdf", "getInvoice_attachment Yes");

                    // Generate PDF.
                    if (Type.equalsIgnoreCase("Sales")) {
                        Log.e("clsPrintPdf", "getInvoice_attachment Sales");

                        Log.e(TAG, "Sales Creating pdf:- ");

//                        if (!finalMessage.contains("www.ftouch.app")) {
                        if (!finalMessage.contains("inc.htm?p=")) {
                            ClsPrintPdf clsPrintPdf = ClsHtmlToPdf.generatePDF(context,
                                    clsInventoryOrderMaster, list_Current_Order, "Generate Pdf");

//                            Log.e(TAG, "getPdfString:- " + clsPrintPdf.getPdfString());
                            converter.convert(context, clsPrintPdf.getPdfString(),
                                    new File(CREATE_PDF_FILE_PATH.concat(
                                            "_" + getCurruntDateTime() + ClsGlobal.getRandom() + ".pdf")));
                        } else {
                            AddSendToList(clsSalesSMSLogs);
                            timer = false;
                        }

                    } else if (Type.equalsIgnoreCase("Quotation")) {
//                        Log.e("clsPrintPdf", "getInvoice_attachment Quotation");

//                        if (!finalMessage.contains("www.ftouch.app")) {
                        if (!finalMessage.contains("inc.htm?p=")) {
                            Log.e(TAG, "Sales Quotation pdf:- ");

                            ClsCustomerMaster objClsCustomerMaster = new ClsCustomerMaster();

                            if (clsQuotationMaster.getMobileNo() != null && !clsQuotationMaster.getMobileNo().isEmpty()) {

                                String where = " AND [MOBILE_NO] = ".concat(clsQuotationMaster.getMobileNo());
                                objClsCustomerMaster = new ClsCustomerMaster().getCustomerByMobileNo(where, context);


                            }

                            ClsQuotationPdf clsQuotationPdf = ClsQuotationHtmlToPdf.generatePDF(context,
                                    clsQuotationMaster, objClsCustomerMaster, lstQuotation, "Generate Pdf");

//                            Log.e(TAG, "getPdfString Sales: " + clsQuotationPdf.getPdfString());
                            converter.convert(context, clsQuotationPdf.getPdfString(),
                                    new File(CREATE_PDF_QUOTATION_PATH_SEND.concat(
                                            "_" + getCurruntDateTime() + ClsGlobal.getRandom() + ".pdf")));
                        } else {
                            AddSendToList(clsSalesSMSLogs);
                            timer = false;
                        }


//                        Log.e("clsPrintPdf", clsQuotationPdf.getPdfString());
                    }

                } else {

//                    sendToList.add(new SendTo(currentClsSalesSmsLogs.getMobileNo()
//                            , currentClsSalesSmsLogs.getCustomer_Name()
//                            , objClsUserInfo.getBusinessname(), objClsUserInfo.getGstnumber(),
//                            objClsUserInfo.getCity(), objClsUserInfo.getPincode(),
//                            finalMessage));

                    AddSendToList(clsSalesSMSLogs);
                    timer = false;
                }


                // to wait for Pdf file get's uploaded.
                while (timer) {

//                    Log.e(TAG, "--Refresh:-" + timer);

                    if (!timer) {
                        break;
                    }
                }

                timer = true;

            }

            // if we have pending Sales Sms List then only call send sms api.
            if (pendingSalesSmsList != null && pendingSalesSmsList.size() > 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SendMessageApi();
            }


            // CheckSmsStatus Worker.
            Data inputData = new Data.Builder()
                    .putString(CheckSmsStatusTask.EXTRA_MODE,
                            "SalesSms")
                    .build();


            Create_OneTimeWorkRequest(CheckSmsStatusTask.class,
                    "CheckSmsStatus", "KEEP", inputData, null);

//           Creating new Worker for Deleting Pdf Files.
            Create_OneTimeWorkRequest(DeletePdfFilesWorker.class,
                    "DeletePdfFiles", "KEEP", null, null);


        } else {

            ClsGlobal.cancelWorkerTask("SalesSms");

            // if there is no InternetConnection then create new Worker.
            Constraints myConstraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            Create_OneTimeWorkRequest(SalesSmsWorker.class,
                    "SalesSms", "KEEP", null, myConstraints);

//            return Result.failure();
        }




        ClsGlobal.appendSmsLog(getEntryDateFormat(getCurruntDateTime()) +
                "SalesSmsWorker Result.success(): "+ ListenableWorker.Result.success()+"  \n");


        ClsGlobal.appendSmsLog(getEntryDateFormat(getCurruntDateTime()) +
                " ---------------------------------- SalesSmsWorker finesh---------------------------   \n");
    }

    private void getDefaultSenderID() {
        ClsSmsIdSetting clsSmsIdSetting = ClsSmsIdSetting.getSmsIdSettingByDefault(db);
        if (clsSmsIdSetting.getSms_id().length() > 0) {
            DefaultSmsIdSetting = clsSmsIdSetting.getSms_id();
        } else {
            DefaultSmsIdSetting = DefaultSenderId;
        }
    }

    // Call Send Sms Api.
    private void SendMessageApi() {

        db = context.openOrCreateDatabase(ClsGlobal.Database_Name,
                Context.MODE_PRIVATE, null);

        InterfaceSendSms interfaceSendSms = ApiClient.getDemoInstance()
                .create(InterfaceSendSms.class);


        ClsSendSmsParams clsSendSmsParams = new ClsSendSmsParams();
        clsSendSmsParams.setCustomerCode(objClsUserInfo.getUserId());
//        clsSendSmsParams.setCustomerCode("CTA001");
        clsSendSmsParams.setsMSType("Transactional");
        clsSendSmsParams.setSource("Sale");

        clsSendSmsParams.setSenderID(DefaultSmsIdSetting);

        Log.d(TAG, "size: " + sendToList.size());

        // remove empty mobile number items from list.
        sendToList = StreamSupport.stream(sendToList)
                .filter(str -> !str.getmOBILE().isEmpty())
                .collect(Collectors.toList());

        Gson gson = new Gson();
        String jsonArry = gson.toJson(sendToList);

        Log.d(TAG, "jsonArry: " + jsonArry);
        clsSendSmsParams.setSendTo(gson.toJson(sendToList));

//        clsSendSmsParams.setMessage(finalMessage + "bill: "+url +
//                "\n" + "Powered by fTouch");


//        clsSendSmsParams.setMessageLength(String.valueOf(finalMessage.length()));
//        if (DefaultMessageFormat.contains("FOCUS BUSINESS SOLUTIONS PVT")){
//            clsSendSmsParams.setMessage(DefaultMessageFormat);
//            clsSendSmsParams.setMessageLength(String.valueOf(DefaultMessageFormat.length()));
//        }else {
//            clsSendSmsParams.setMessage(finalMessage);
//            clsSendSmsParams.setMessageLength(String.valueOf(finalMessage.length()));
//        }

        clsSendSmsParams.setProductName(ClsGlobal.AppName);


        Gson gsonOrder = new Gson();
        String jsonInStringOrder = gsonOrder.toJson(clsSendSmsParams);
        Log.d("SalesSmsWorker", "jsonInStringOrder: " + jsonInStringOrder);

        ClsGlobal.appendSmsLog(getEntryDateFormat(getCurruntDateTime()) +
                "SalesSmsWorker Gson  : " + jsonInStringOrder+ " \n");


        Call<ClsSendSmsResponse> sendSms = interfaceSendSms.SendSms(clsSendSmsParams);

        sendSms.enqueue(new Callback<ClsSendSmsResponse>() {
            @Override
            public void onResponse(Call<ClsSendSmsResponse> call, Response<ClsSendSmsResponse> response) {
                Log.e("RecentOrderActivity", "Sms sending : " + response);

                ClsGlobal.appendSmsLog(getEntryDateFormat(getCurruntDateTime()) +
                        "SalesSmsWorker onResponse call  \n");
//                timer = false;
                if (response.body() != null) {
                    Log.e("getResponse", "--Response:-" + response.message());
                    Log.e("getResponse", "--getSuccess:-" + response.body().getSuccess());
                    Log.e("getResponse", "--getMessageSales:-" + response.body().getMessage());
                    Log.e("getResponse", "--getSendSMSID:-" + response.body().getSendSMSID());

                    ClsGlobal.appendSmsLog(getEntryDateFormat(getCurruntDateTime()) +
                            "SalesSmsWorker response.message(): "+response.message()+"  \n");

                    ClsGlobal.appendSmsLog(getEntryDateFormat(getCurruntDateTime()) +
                            "SalesSmsWorker response.getSuccess(): "+response.body().getSuccess()+"  \n");

                    Gson gsonOrder = new Gson();
                    String response1 = gsonOrder.toJson(response.body());
                    Log.d("SalesSmsWorker", "body: " + response1);


//                    pendingSalesSmsList = ClsSMSLogs.getList("AND [Status] = 'Pending' " +
//                            "AND [SmsStatus] ='Pending' " +
//                            "or [Remark] != 'You can't send Sms more than 5 time's on same Bill.'", "", db);
                    // Here Message send successfully.
                    // Update Status in SalesSMSLogsMaster to Send.
                    Gson gsonOrder1 = new Gson();
                    String response11 = gsonOrder1.toJson(pendingSalesSmsList);
                    Log.d("SalesSmsWorker", "pendingSalesSmsList: " + response11);

                    int counter = 0;
                    for (ClsSMSLogs clsSalesSMSLogs : pendingSalesSmsList) {
                        counter++;

                        clsSalesSMSLogs.setStatus("Pending");

                        if (response.body().getSendSMSID() == null) {

                            Log.e(TAG, "response.body().getSendSMSID() == null");
                            clsSalesSMSLogs.setSmsStatus("Pending");
                        } else {
                            clsSalesSMSLogs.setSmsStatus("Send");
                        }


                        try {
                            Log.e(TAG, "Update messge:- " + sendToList.get(counter - 1).getMessage());
                            clsSalesSMSLogs.setMessage(sendToList.get(counter - 1).getMessage());
                        } catch (Exception e) {
                            clsSalesSMSLogs.setMessage("");
                        }


                        clsSalesSMSLogs.setRemark(response.body().getMessage());


                        Log.e(TAG, "!clsSalesSMSLogs.getRemark()");
                        ClsGlobal.appendSmsLog(getEntryDateFormat(getCurruntDateTime()) +
                                "SalesSmsWorker response.body().getSendSMSID(): "+response.body().getSendSMSID()+"  \n");

                        clsSalesSMSLogs.setSendSMSID(response.body().getSendSMSID() == null
                                ? "Pending" : response.body().getSendSMSID());

                        ClsSMSLogs.Update(clsSalesSMSLogs, context, db);

                    }

                    latch.countDown();
                }


                db.close();
            }

            @Override
            public void onFailure(Call<ClsSendSmsResponse> call, Throwable t) {
//                timer = false;
                db.close();
                // unregisterReceiver ConnectionCheckBroadcast after Work is finish.
//                context.unregisterReceiver(mConnectionCheckBroadcast);
                latch.countDown();
                Log.e(TAG, "Sms sending onFailure: " + t.getMessage());

                ClsGlobal.appendSmsLog(getEntryDateFormat(getCurruntDateTime()) +
                        "SalesSmsWorker onFailure : "+t.getMessage()+"  \n");

            }


        });


//        // to wait for the response from retrofit.
//        while (timer) {
//
////            Log.e(TAG, "--response:-" + timer);
//            if (!timer) {
//                break;
//            }
//        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void AddSendToList(ClsSMSLogs clsSalesSMSLogs) {
        // Check sms limit and should
        // not be grater than 5.
        Log.e(TAG, "--sms_limit:-" + sms_limit);


        sendToList.add(new SendTo(currentClsSalesSmsLogs.getMobileNo()
                , currentClsSalesSmsLogs.getCustomer_Name()
                , objClsUserInfo.getBusinessname(), objClsUserInfo.getGstnumber(),
                objClsUserInfo.getCity(), objClsUserInfo.getPincode(),
                finalMessage));


        Gson gson = new Gson();
        String jsonInString = gson.toJson(sendToList);
        Log.d(TAG, "sendToList: " + jsonInString);


    }


    @Override
    public void onError() {
        Log.e(TAG, "onError: ");
//        AddSendToList(currentClsSalesSmsLogs);

        ClsGlobal.appendSmsLog(getEntryDateFormat(getCurruntDateTime()) +
                "SalesSmsWorker onError:- While uploading Pdf File  \n");

        timer = false;
    }

    @Override
    public void onFinish(PdfFileUploadResponse responses) {
        ClsGlobal.appendSmsLog(getEntryDateFormat(getCurruntDateTime()) +
                " SalesSmsWorker PDF File Uploaded Succesfully:- " + responses +  "\n");
        Log.e(TAG, "PDF Upload response: " + responses);

        // If pdf file is uploaded successfully then get pdf url and send sms.
        if (responses.getMessage().equalsIgnoreCase(
                "Success")) {

            if (currentClsSalesSmsLogs.getType()
                    .equalsIgnoreCase("Sales")) {
                finalMessage +=
                        " \nBill: " + PDF_Download_URL +"/inc.htm?p="
                                + responses.getUniqCode() +
                                "\n" + "Powered by fTouch";
            } else if (currentClsSalesSmsLogs.getType()
                    .equalsIgnoreCase("Quotation")) {
                finalMessage +=
                        " \nQut: " + PDF_Download_URL  + "/inc.htm?p="
                                + responses.getUniqCode() +
                                "\n" + "Powered by fTouch";
            }

            Log.e(TAG, "Message : " + finalMessage);

            AddSendToList(currentClsSalesSmsLogs);
            timer = false;


        } else {
//            AddSendToList(currentClsSalesSmsLogs);
            timer = false;
            Log.e(TAG, "PDF Upload Error: " + responses.getMessage());
        }

    }

    @Override
    public void onProgressUpdate(int currentpercent, int totalpercent, String msg) {

    }
}
