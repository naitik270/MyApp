package com.demo.nspl.restaurantlite.AsyncTask;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.demo.nspl.restaurantlite.BuildConfig;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.ClsHtmlToPdf;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderDetail;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster;

import java.io.File;
import java.util.List;

public class GeneratePdfAsyncTask extends AsyncTask<String, Void, String> {

    private ProgressDialog loading;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ClsInventoryOrderMaster clsInventoryOrderMaster;
    private List<ClsInventoryOrderDetail> list_Current_Order;
    private String getCurrentPdfFilePath = "";
    private static final String AUTHORITY =
            BuildConfig.APPLICATION_ID + ".myfileprovider";

    public GeneratePdfAsyncTask(Context context,
                                ClsInventoryOrderMaster clsInventoryOrderMaster,
                                List<ClsInventoryOrderDetail> list_Current_Order) {
        this.context = context;
        this.clsInventoryOrderMaster = clsInventoryOrderMaster;
        this.list_Current_Order = list_Current_Order;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        loading = ClsGlobal._prProgressDialog(context
                , "Generating PDF File...", false);
        loading.show();
        Log.d("onPreExecute", "onPreExecute call");
    }

    @Override
    protected String doInBackground(String... strings) {

        String result = "";

        getCurrentPdfFilePath = String.valueOf(ClsHtmlToPdf.generatePDF(context,
                clsInventoryOrderMaster,list_Current_Order,"Send To WhatsApp"));

        /*ClsHtmlToPdf.generatePDF(context,
                clsInventoryOrderMaster, list_Current_Order, "Send To WhatsApp");*/

        if (!getCurrentPdfFilePath.equalsIgnoreCase("")) {
            result = "Successful";
        } else {
            result = "Failed";
        }

        Log.e("getCurrentPdfFilePath", getCurrentPdfFilePath);

        return result;
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (loading.isShowing()) {
            loading.dismiss();
        }

        if (result.equalsIgnoreCase("Successful")) {
            Log.d("onPostExecute", "Successful call");

            ClsGlobal.copyToClipboard(clsInventoryOrderMaster.getMobileNo(), context);

            ClsGlobal.sendNotification("Send PDF File",
                    "Current Mobile Number: ".concat(clsInventoryOrderMaster.getMobileNo())
                    , "Send PDF", context);

            Uri pdfUri;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                pdfUri = FileProvider.getUriForFile(context,
                        AUTHORITY,
                        new File(getCurrentPdfFilePath));
                Log.e("pdfUri ", String.valueOf(pdfUri));
            } else {
                pdfUri = Uri.fromFile(new File(getCurrentPdfFilePath));
                Log.e("pdfUri ", String.valueOf(pdfUri));
            }
            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.setType("application/pdf");
            share.putExtra(Intent.EXTRA_STREAM, pdfUri);
            context.startActivity(Intent.createChooser(share, "Share"));

        }

        Log.d("onPostExecute", "onPostExecute call");
    }

}
