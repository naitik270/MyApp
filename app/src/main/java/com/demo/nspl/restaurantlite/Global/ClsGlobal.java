package com.demo.nspl.restaurantlite.Global;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.job.JobScheduler;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.ListenableWorker;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.demo.nspl.restaurantlite.Adapter.CountryCodeAdapter;
import com.demo.nspl.restaurantlite.BuildConfig;
import com.demo.nspl.restaurantlite.MIS.ClsSendMIS;
import com.demo.nspl.restaurantlite.Purchase.ClsPurchaseDetail;
import com.demo.nspl.restaurantlite.Purchase.ClsPurchaseMaster;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsBackupTypeParams;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsCheckSmsStatusResponse;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsMobileStatus;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsSmsStatusPrams;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceBackupType;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceSmsStatus;
import com.demo.nspl.restaurantlite.SMS.ClsKeywordDescription;
import com.demo.nspl.restaurantlite.SMS.ClsMessageFormat;
import com.demo.nspl.restaurantlite.SendEmailUtility.SendEmail;
import com.demo.nspl.restaurantlite.UpdateCalling.ClsItemDefaultTaxUpdate;
import com.demo.nspl.restaurantlite.UpdateCalling.ClsVendorLedgerUpdate;
import com.demo.nspl.restaurantlite.activity.BackUpAndRestoreActivity;
import com.demo.nspl.restaurantlite.activity.EmailLogsActivity;
import com.demo.nspl.restaurantlite.activity.SettingAutoEmailActivity;
import com.demo.nspl.restaurantlite.backGroundTask.AutoBackUpTask;
import com.demo.nspl.restaurantlite.backGroundTask.AutoLocalBackupTask;
import com.demo.nspl.restaurantlite.backGroundTask.BackupLogsActivity;
import com.demo.nspl.restaurantlite.backGroundTask.DbBackUpTask;
import com.demo.nspl.restaurantlite.backGroundTask.NotificationReceiver;
import com.demo.nspl.restaurantlite.backGroundTask.SalesSmsWorker;
import com.demo.nspl.restaurantlite.backGroundTask.SendErrorWorker;
import com.demo.nspl.restaurantlite.classes.ClsBillNoFormat;
import com.demo.nspl.restaurantlite.classes.ClsBulkSMSLog;
import com.demo.nspl.restaurantlite.classes.ClsCommonLogs;
import com.demo.nspl.restaurantlite.classes.ClsCustomerMaster;
import com.demo.nspl.restaurantlite.classes.ClsDateTime;
import com.demo.nspl.restaurantlite.classes.ClsEmailConfiguration;
import com.demo.nspl.restaurantlite.classes.ClsEmailLogs;
import com.demo.nspl.restaurantlite.classes.ClsEmployeeDocuments;
import com.demo.nspl.restaurantlite.classes.ClsExpenseMasterNew;
import com.demo.nspl.restaurantlite.classes.ClsInventoryItem;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderDetail;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster;
import com.demo.nspl.restaurantlite.classes.ClsLayerItemMaster;
import com.demo.nspl.restaurantlite.classes.ClsOrderDetailMaster;
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;
import com.demo.nspl.restaurantlite.classes.ClsQuotationMaster;
import com.demo.nspl.restaurantlite.classes.ClsQuotationOrderDetail;
import com.demo.nspl.restaurantlite.classes.ClsSMSLogs;
import com.demo.nspl.restaurantlite.classes.ClsTerms;
import com.demo.nspl.restaurantlite.classes.ClsUserInfo;
import com.demo.nspl.restaurantlite.classes.ClsVendor;
import com.demo.nspl.restaurantlite.classes.CountryCode;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.requery.android.database.sqlite.SQLiteDatabase;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.demo.nspl.restaurantlite.Purchase.ClsPurchaseMaster.getPurchaseMasterColumn;
import static com.demo.nspl.restaurantlite.classes.ClsBillNoFormat.set;
import static com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster.getNextOrderNo;
import static com.demo.nspl.restaurantlite.classes.ClsVendor.getVendorNameById;

/**
 * Created by Desktop on 3/12/2018.
 */


//https://www.studytutorial.in/android-export-sqlite-data-into-excel-sheet-xls-using-java-excel-library
//    https://developer.android.com/training/printing/custom-docs
public class ClsGlobal {

    // use for update quotation status...
    public static String _quotationStatusID = "";
    public static String _quotationMobileNO = "";


    public static String _OrderNo = "";
    public static String _WholeSaleOrderNo = "";
    public static String current_OrderNo = "";
    public static String DefaultSenderId = "FTOUCH";
    public static String companyName = "Nathani Software Pvt. Ltd.";
    public static int last_id_SMSBulkMaster;
    static boolean timer = true;
    private static int DEFAULT_FLAG_RES = 0;

    public static final String[] STORAGE_PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    public static final String[] LOCASION_PERMISSIONS = {
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,

    };


   /* public static final String MobileNo_Pattern =
            "^(?:(?:\\+|0{0,2})91(\\s*[\\ -]\\s*)?|[0]?)?[789]\\d{9}|(\\d[ -]?){10}\\d$";*/

    public static final String MobileNo_Pattern = "^[0-9]{10}$";

    private static final String mPreferncesEmail = "Email_Settings";
    public static String defaultPrinterName = "NO DEVICE";
    public static String defaultStatus = "NO STATUS";

    public static final String AppName = "fTouch POS";
    public static final String AppType = "Retail";
    public static final String Google_Bkp_Folder = "fTouch POS Backup";
    public static final int GoogleDrive_Bkp_Process_Notification_id = 76;
    public static final int GoogleDrive_Bkp_Notification_id = 106;

    public static final int FtouchCloud_Bkp_Process_Notification_id = 98;
    public static final int FtouchCloud_Bkp_Notification_id = 890;

//    -------------------FOR LIVE---------------------------

    public static final String LocalBackup = "fTouchPOSLocalBkp";
    public static final String AppDatabasePath = "//data//com.demo.nspl.retailpos//databases//";
    public static final String AppPackageName = "com.demo.nspl.retailpos";
    public static final String ApplicationType = "CustomerApp";
    public static final String AppFolderName = "fTouchPOS";
    public static final String Database_Name = "fTouchRetail";
    public static final String ItemImageDirectory = "ItemImage";
    public static final String PurchaseImageDirectory = "PurchaseImage";
    public static int lastPurchaseID = 0;
    public static int lastItemImgID = 0;
    public static ArrayList<Uri> purchaseImgPathLst = new ArrayList<>();
    public static ArrayList<Uri> payment_Imgs_uri = new ArrayList<>();

//    public static String _fileSmallImgPath = "";

    public static String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static String PDF_Download_URL = "https://www.ftouch.app";

//    -------------------FOR LIVE---------------------------

//    -------------------FOR DEMO---------------------------

//    public static final String LocalBackup = "fTouchPOSLocalBkpDemo";
//    public static final String AppDatabasePath = "//data//com.demo.nspl.retailpos_Demo//databases//";
//    public static final String AppPackageName = "com.demo.nspl.retailpos_demo";
//    public static final String ApplicationType = "DemoApp";
//    public static final String AppFolderName = "fTouchPOSDemo";
//    public static final String Database_Name = "fTouchRetail_Demo";

//    -------------------FOR DEMO---------------------------


//    -------------------FOR COUNTRY STATE CITY---------------------------

    public static int countryStateCityCode = 0; // 1 for county, 2 for State, 3 for City

    public static boolean clearMode = false;

    public static boolean textAvailable = false;

//    -------------------FOR COUNTRY STATE CITY---------------------------

    public static final int REQUEST_APP_UPDATE = 999;
    public static final String AutoBackUpSharedPreferencesFileName = "AutoBackUpSettings";
    public static final String AutoLocalBackup = "ftouchPos AutoLocalBkp";
    public static final String zipPathAutoLocalBkp = Environment.getExternalStorageDirectory()
            .getAbsolutePath()
            + "/" + AutoLocalBackup + "/";

    public static final String zipPath = SDPath + "/" + ClsGlobal.LocalBackup + "/";
    public static final String InvoiceFileName = "Invoice.pdf";
    public static final String InvoiceFolderName = "POS_Sales_Invoice";
    public static final String InvoiceFilePath = Environment.getExternalStorageDirectory()
            + "/" + AppFolderName + "/POS_Sales_Invoice/";
    private static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".myfileprovider";

    public static final String CREATE_PDF_FILE_PATH = Environment.getExternalStorageDirectory() + "/"
            + ClsGlobal.AppFolderName + "/" + ClsGlobal.InvoiceFolderName + "/";

    public static final String SharedPreferencesPathBackupFolder =
            Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/" + ClsGlobal.AppFolderName + "/SettingsBackup/";

    @SuppressLint("SdCardPath")
    public static final String SharedPreferencesPath = "/data/data/"
            + AppPackageName + "/shared_prefs";

    public static String SalesSMSLogsMaster_LAST_INSERTED_ID = "";
    static SharedPreferences mSharedPreferences;
    static Context context;
    public static Boolean isFristFragment = false;
    private static SharedPreferences mPreferences;
    public static final String mPreferncesName = "MyPerfernces";
    public static BackupStatus mBackupStatus;

    private static String Language_Settings = "Language_Settings";
    private static String FAQ_Language_Key = "FAQ_Language";
    public static String ftouchLogs_Folder = ".ftouchlogPOS";

    public static String _QuotationSaleOrderNo = "";
    public static String _QuotationWholesaleOrderNo = "";

    public static final String Bill_No_Format_ApplyTax_Preferences = "Bill No Format Apply Tax";
    public static final String Bill_No_Format_Tax_Not_Apply_Preferences = "Bill No Format Tax Not Apply";

    public static String current_nextOrderNo = "";
    public static String InventoryOrderDetail_last_id = "";
    public static String editOrderID = "";
    public static String imgPreviewMode = "";

    public static final String QuotationFilePath =
            Environment.getExternalStorageDirectory() + "/" + AppFolderName + "/QuotationPDF/";

    public static final String QuotationFileName = "Quotation.pdf";

    public static int editQuotationID = 0;
    public static String Current_Order_No = "";
    public static String current_quotation_no = "";

    public static String QuotationNo = "";
    public static String GenrateQuotationDetail_last_id = "";

    public static final String CREATE_PDF_QUOTATION_PATH = Environment.getExternalStorageDirectory() + "/"
            + ClsGlobal.AppFolderName + "/QuotationPDF/" + ClsGlobal.QuotationFileName;

    public static final String CREATE_PDF_QUOTATION_PATH_SEND = Environment.getExternalStorageDirectory() + "/"
            + ClsGlobal.AppFolderName + "/QuotationPDF/";

    private static final String TAG = ClsGlobal.class.getSimpleName();

    public ClsGlobal() {

    }


    public ClsGlobal(Context context) {
        ClsGlobal.context = context;
        mSharedPreferences = context.getSharedPreferences(context.getResources()
                .getString(R.string.Login_preference), Context.MODE_PRIVATE);
    }

    public static String GetNetworkTypeStatus(Context context) {
        SharedPreferences Preferences =
                context.getSharedPreferences("AutoBackUpSettings", MODE_PRIVATE);
        if (Preferences != null) {
            String getNetworkType = Preferences.getString("NetworkType", "");
            Log.e("getNetworkType", "sdef " + getNetworkType);
            return getNetworkType;
        }

        return "";
    }

    /**
     * Set DefaultBackupSettings to SharedPreferences and start backup worker.
     *
     * @param context
     */
    public static void ShareAppLink(Context context) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "fTouch POS");
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

            context.startActivity(Intent.createChooser(shareIntent, "fTouch POS"));
        } catch (Exception e) {
            //e.toString();
        }

    }

    public static void SetDefaultBackupSettings(Context context) {
        SharedPreferences Preferences =
                context.getSharedPreferences("AutoBackUpSettings", MODE_PRIVATE);
        if (Preferences != null) {
            String getNetworkType = Preferences.getString("NetworkType", "");
            String AutoBackUpTime = Preferences.getString("AutoBackUpTime", "");
            Log.e("getNetworkType", "sdef " + getNetworkType);

            SharedPreferences.Editor editor = Preferences.edit();

            if (getNetworkType == "") {
                editor.putString("NetworkType", "WI-FI OR CELLULAR");
                editor.apply();
            }

            if (AutoBackUpTime == "") {
                editor.putString("AutoBackUpTime", "Daily");
                editor.apply();

                ClsGlobal.SetAutoBackUp(1, "REPLACE");
            }


        }


    }

    /**
     * Get Current day,month,year according
     * format like Never,Daily,Yearly,Monthly.
     *
     * @param format
     * @return current day,month,year
     */
    public static String get_Current_Day_Month_Yearly(String format) {
        Log.e("Check", "get_Current_Day_Month_Yearly:- ");
        Calendar c = Calendar.getInstance();

        if (format.contains("Never")) {
            return "";
        } else if (format.contains("Daily")) {
            Log.e("Check", "get_Current_Day_Month_Yearly:- " + "Daily");
            Log.e("Check", "day:- " + c.get(Calendar.DAY_OF_MONTH));
            return String.valueOf(c.get(Calendar.DAY_OF_MONTH));///10 SEp,   11 Sep:   100

        } else if (format.contains("Monthly")) {
            return String.valueOf(c.get(Calendar.MONTH) + 1);

        } else if (format.contains("Yearly")) {
            return String.valueOf(c.get(Calendar.YEAR));
        }
        return "";
    }


    /**
     * Get current formatted year.
     *
     * @param format
     * @return result
     */
    public static String yearFormat(String format) {
        int year;
        String result = "";

        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        result = String.valueOf(year);

        if (format.equalsIgnoreCase("CC")) {
            return result.substring(0, 2);
        } else if (format.equalsIgnoreCase("YY")) {
            return result.substring(2, 4);
        } else if (format.equalsIgnoreCase("YYYY")) {
            return result;
        }
        return "";
    }


    /**
     * Get current Formatted month.
     *
     * @param format
     * @return result
     */
    public static String MonthFormat(String format) {
        int month;
        String result = "";
        Calendar c = Calendar.getInstance();
        month = c.get(Calendar.MONTH) + 1;

        result = String.valueOf(month);
        Log.e("result", "Month:- " + result);
        if (format.equalsIgnoreCase("M")) {
            return result;

        } else if (format.equalsIgnoreCase("MM")) {
            if (result.length() > 1) {
                return result;
            } else {
                Log.e("result", "Month:- " + result);
                return "0" + result;
            }
        }

        return "";
    }

    /**
     * Get current Formatted Month and Year.
     *
     * @param format_str
     * @return result
     */
    public static String formatsMonthYearNo(String format_str) {

        int nextYear = Integer.valueOf(yearFormat("YY")) + 1;

        String result = "";

        switch (format_str) {

            case "CCYYdashCCnYY": // 2019-2020
                result = yearFormat("YYYY") +
                        "-" + yearFormat("CC") + nextYear;
                break;

            case "YYdashnYY": // 19-20

                result = yearFormat("YY") + "-"
                        + nextYear;

                break;

            case "CCYYnYY": //201920

                result = yearFormat("YYYY") + nextYear;

                break;

            case "YYnYY": // 1920
                result = yearFormat("YY")
                        + nextYear;
                break;

            case "CCYYdashnYY": // 2019-20
                result = yearFormat("YYYY") + "-" + nextYear;
                break;

            case "CCYYCCnYY": // 20192020
                result = yearFormat("YYYY") + yearFormat("CC") + nextYear;
                break;

            case "CCYY": // 2019

                result = yearFormat("YYYY");

                break;

            case "YY": // 19
                result = yearFormat("YY");

                break;

            case "MM": // 09
                result = MonthFormat("MM");
                break;

            case "M": // 9
                result = MonthFormat("M");
                break;
        }
        return result;
    }


    /**
     * Here we are getting next formatted full order no.
     * for ex: mum/2019-2020/2019/09/22/an
     *
     * @param nextOrderNo // 234...
     * @return bill_no_format
     */
    public static String getNextFormattedOrderNo(int nextOrderNo
            , String applyTax, ClsBillNoFormat clsBillNoFormat, Context context) {


//        ClsBillNoFormat clsBillNoFormat = getSettingApplyTaxBillNo(context);
        Log.e("bill_no_format", "mode:- " + applyTax);
        Log.e("bill_no_format", "nextOrderNo:- " + nextOrderNo);
        Log.e("bill_no_format", "isSettingApply:- " + clsBillNoFormat.isApplybillFormat());
        Log.e("bill_no_format", "getBillNo_Format:- " + clsBillNoFormat.getBillNo_Format());
        Log.e("bill_no_format", "getBill_no_length:- " + clsBillNoFormat.getBill_no_length());

        Gson gson = new Gson();
        String jsonInString = gson.toJson(clsBillNoFormat);
        Log.e("--Sales--", "SalesDetailsFragment: " + jsonInString);

        String bill_no_format = "";
        String nextCounter = "";

//        Log.e("bill_no_format", "getBill_no_length().length():- " +
//                "" + clsBillNoFormat.getBill_no_length().length());
//        Log.e("bill_no_format", "String length:- " + String.valueOf(nextOrderNo).length());
//        Log.e("bill_no_format", "condition:- " + String.valueOf(
//                Integer.valueOf(clsBillNoFormat.getBill_no_length())
//                        > String.valueOf(nextOrderNo).length()));

        // Here we are adding zero's in bill counter.
        if (Integer.valueOf(clsBillNoFormat.getBill_no_length()) >
                String.valueOf(nextOrderNo).length()) {

            Log.e("bill_no_format", "if lenght:- " + nextCounter);
            nextCounter = String.format("%0" + (Integer.valueOf(clsBillNoFormat.getBill_no_length()
                            .equalsIgnoreCase("") ? "0"
                            : clsBillNoFormat.getBill_no_length()))
                            + "d",
                    nextOrderNo);

        } else {
            Log.e("bill_no_format", "else lenght:- " + nextCounter);
            nextCounter = String.valueOf(nextOrderNo);
        }

        Log.e("bill_no_format", "nextCounter:- " + nextCounter);
        current_nextOrderNo = String.valueOf(nextCounter);

        if (clsBillNoFormat != null) {
            Log.e("bill_no_format", "clsBillNoFormat != null");

            // Generate different bill no format when tax is apply.

            if (applyTax.equalsIgnoreCase("YES")) {
                Log.e("bill_no_format", "bill_no_format:- " + "YES");

                bill_no_format = clsBillNoFormat.getBillNo_Format();
                Log.e("bill_no_format", "bill_no_format:- " + bill_no_format);

                if (bill_no_format.contains(clsBillNoFormat.getFinancial_Year_format())) {
                    Log.e("bill_no_format", "Financial_Year_format");

//                    Log.e("Check", "Financial_Year_format:- "
//                            + formatsFinancialYear(clsBillNoFormat.getFinancial_Year_format()
//                            ,clsBillNoFormat,applyTax,context));


                    Log.e("bill_no_format", "getFinancial_Year_format:- " + bill_no_format);
                    // Replace Financial_Year_format.
                    bill_no_format = bill_no_format.replace(clsBillNoFormat.getFinancial_Year_format(),
                            formatsFinancialYear(clsBillNoFormat.getFinancial_Year_format(),
                                    clsBillNoFormat, applyTax, context));
                }

                if (bill_no_format.contains(clsBillNoFormat.getYear_format())) {
                    Log.e("bill_no_format", "contains getYear_format:- ");
                    // Replace Year.
                    bill_no_format = bill_no_format.replace(clsBillNoFormat.getYear_format(),
                            formatsMonthYearNo(clsBillNoFormat.getYear_format()));

                    Log.e("bill_no_format", "contains getYear_format:- " + bill_no_format);
                }

                if (bill_no_format.contains(clsBillNoFormat.getMonth_format())) {
                    Log.e("bill_no_format", "contains getYear_format:- ");
                    // Replace Month.
                    bill_no_format = bill_no_format.replace(clsBillNoFormat.getMonth_format(),
                            formatsMonthYearNo(clsBillNoFormat.getMonth_format()));
                }

                if (bill_no_format.contains("Counter")) {
                    Log.e("bill_no_format", "Counter");

                    // Replace Counter.
                    bill_no_format = bill_no_format.replace("Counter",
                            String.valueOf(nextCounter));
                }

                Log.e("bill_no_format", "nextCounter:- " + String.valueOf(nextCounter));
                Log.e("bill_no_format", "after:- " + bill_no_format);
                if (!clsBillNoFormat.isApplybillFormat()) {
                    Log.e("bill_no_format", "isApplybillFormat:- inside ");
                    bill_no_format = String.valueOf(nextCounter);
                }


            } else if (applyTax.equals("NO")) {
                Log.e("bill_no_format", "bill_no_format:- " + "NO");

//                // If there is no bill format in apply tax then return normal format.
//                if (!clsBillNoFormat.isApplybillFormat()) {
//
//                    bill_no_format = String.valueOf(nextOrderNo);
//                }

                // Generate different bill no format when tax is not apply.

                bill_no_format = clsBillNoFormat.getBillNo_Format();
                Log.e("bill_no_format", "bill_no_format:- " + bill_no_format);


                if (bill_no_format.contains(clsBillNoFormat.getFinancial_Year_format())) {
                    Log.e("bill_no_format", "Financial_Year_format");
//                    Log.e("bill_no_format", "Financial_Year_format:- "
//                            +  formatsFinancialYear(clsBillNoFormat.getFinancial_Year_format(),clsBillNoFormat
//                            ,applyTax,context));
                    Log.e("bill_no_format", "getFinancial_Year_format:- " + bill_no_format);
                    // Replace Financial_Year_format.
                    bill_no_format = bill_no_format.replace(clsBillNoFormat.getFinancial_Year_format(),
                            formatsFinancialYear(clsBillNoFormat.getFinancial_Year_format(), clsBillNoFormat
                                    , applyTax, context));
                }

                if (bill_no_format.contains(clsBillNoFormat.getYear_format())) {
                    Log.e("bill_no_format", "contains getYear_format:- ");
                    // Replace Year.
                    bill_no_format = bill_no_format.replace(clsBillNoFormat.getYear_format(),
                            formatsMonthYearNo(clsBillNoFormat.getYear_format()));

                    Log.e("bill_no_format", "contains getYear_format:- " + bill_no_format);
                }

                if (bill_no_format.contains(clsBillNoFormat.getMonth_format())) {
                    Log.e("bill_no_format", "contains getYear_format:- ");
                    // Replace Month.
                    bill_no_format = bill_no_format.replace(clsBillNoFormat.getMonth_format(),
                            formatsMonthYearNo(clsBillNoFormat.getMonth_format()));
                }

                if (bill_no_format.contains("Counter")) {
                    Log.e("bill_no_format", "Counter");

                    // Replace Counter.
                    bill_no_format = bill_no_format.replace("Counter",
                            String.valueOf(nextCounter));
                }

                Log.e("bill_no_format", "nextCounter:- " + String.valueOf(nextCounter));
                Log.e("bill_no_format", "after:- " + bill_no_format);

                if (!clsBillNoFormat.isApplybillFormat()) {
                    Log.e("bill_no_format", "isApplybillFormat:- inside ");
                    bill_no_format = String.valueOf(nextCounter);
                }


            }

        }

        if (!clsBillNoFormat.isApplybillFormat()) {
            Log.e("clsBillNoFormat", "isSettingApply:- " + clsBillNoFormat);
            bill_no_format = String.valueOf(nextCounter);

        }


        return bill_no_format;


    }

    /**
     * Get Formatted FinancialYear.
     *
     * @param format_str
     * @return result
     */
    public static String formatsFinancialYear(String format_str,
                                              ClsBillNoFormat clsBillNoFormat,
                                              String applyTax, Context context) {
        String result = "";

        if (clsBillNoFormat != null
                && !clsBillNoFormat.getFinancialYear().equalsIgnoreCase("")) {
            // we are sprareting 2019-2020 to 2019 and 2020.
            String previousYear = clsBillNoFormat.getFinancialYear().split("-")[0];
            String NextYear = clsBillNoFormat.getFinancialYear().split("-")[1];


            Log.e("ClsGlobal", "previousYear: "
                    + previousYear);
            Log.e("ClsGlobal", "NextYear: "
                    + NextYear);

            try {


                // Check if Current Date is between From Date and to Date.
                if (!DateIsBetween(new Date(new SimpleDateFormat("dd/MM/yyyy")
                                .parse(clsBillNoFormat.getFY_From_Date()).getTime() - 1)
                        , new Date(new SimpleDateFormat("dd/MM/yyyy")
                                .parse(clsBillNoFormat.getFY_To_Date()).getTime() + 1)
                        , new SimpleDateFormat("dd/MM/yyyy")
                                .parse(getEntryDateFormat(getCurruntDateTime())))) {

                    // if Date is not between.
                    // Change Current FinancialYear and from date and to date in Setting.
                    previousYear = String.valueOf(Integer.valueOf(previousYear) + 1);
                    NextYear = String.valueOf(Integer.valueOf(NextYear) + 1);

                    Log.e("ClsGlobal", "DateIsBetween previousYear getFY_From_Date: "
                            + previousYear);
                    Log.e("ClsGlobal", "DateIsBetween NextYea getFY_To_Dater: "
                            + NextYear);

                    if (applyTax.equalsIgnoreCase("YES")) {
                        ClsBillNoFormat clsBillNoFormat1 = new ClsBillNoFormat();

                        clsBillNoFormat.setFY_From_Date(clsBillNoFormat.getFY_From_Date().replace(
                                String.valueOf(Integer.valueOf(previousYear) - 1)
                                , String.valueOf(previousYear)));

                        clsBillNoFormat.setFY_To_Date(clsBillNoFormat.getFY_To_Date()
                                .replace(String.valueOf(
                                        Integer.valueOf(NextYear) - 1)
                                        , String.valueOf(NextYear)));

                        clsBillNoFormat.setFinancialYear(previousYear
                                + "-" + NextYear);

                        ClsGlobal.saveFY_Date_Change(clsBillNoFormat, context,
                                "ApplyTax");

                    } else if (applyTax.equalsIgnoreCase("NO")) {
                        ClsBillNoFormat clsBillNoFormat1 = new ClsBillNoFormat();

                        clsBillNoFormat.setFY_From_Date(clsBillNoFormat.getFY_From_Date().replace(
                                String.valueOf(Integer.valueOf(previousYear) - 1)
                                , String.valueOf(previousYear)));

                        clsBillNoFormat.setFY_To_Date(clsBillNoFormat.getFY_To_Date()
                                .replace(String.valueOf(
                                        Integer.valueOf(NextYear) - 1)
                                        , String.valueOf(NextYear)));

                        clsBillNoFormat.setFinancialYear(previousYear
                                + "-" + NextYear);

                        ClsGlobal.saveFY_Date_Change(clsBillNoFormat, context,
                                "Tax Not Apply");
                    }


                }
            } catch (Exception e) {
                Log.e("ClsGlobal", "Exception: " + e.getMessage());
                e.printStackTrace();
            }


            switch (format_str) {

                case "CCYYdashCCnYY": // 2019-2020
                    result = previousYear +
                            "-" + previousYear.substring(0, 2) + NextYear.substring(2, 4);
                    break;

                case "YYdashnYY": // 19-20

                    result = previousYear.substring(2, 4) + "-"
                            + NextYear.substring(2, 4);

                    break;

                case "CCYYnYY": //201920

                    result = previousYear + NextYear.substring(2, 4);

                    break;

                case "YYnYY": // 1920
                    result = previousYear.substring(2, 4)
                            + NextYear.substring(2, 4);
                    break;

                case "CCYYdashnYY": // 2019-20
                    result = previousYear + "-" + NextYear.substring(2, 4);
                    break;

                case "CCYYCCnYY": // 20192020
                    result = previousYear + previousYear.substring(0, 2) + NextYear.substring(2, 4);
                    break;


            }

            Log.e("ClsGlobal", "out previousYear: " + previousYear);
            Log.e("ClsGlobal", "out NextYear: " + NextYear);
        }


        return result;
    }


    /**
     * Check if Date is between From_Date to_Date.
     *
     * @param From
     * @param ToDate
     * @param currentDate
     * @return boolean
     */
    public static boolean DateIsBetween(Date From, Date ToDate, Date currentDate) {
        return currentDate.after(From) && currentDate.before(ToDate);
    }

    public static void saveFY_Date_Change(ClsBillNoFormat clsBillNoFormat,
                                          Context c, String mode) {

        Gson gson = new Gson();
        String jsonInString = gson.toJson(clsBillNoFormat);
        Log.e("ClsGlobal", "clsBillNoFormat saveFY_Date_Change: " + jsonInString);

        if (mode.equalsIgnoreCase("ApplyTax")) {
            mPreferences = c.getSharedPreferences(Bill_No_Format_ApplyTax_Preferences,
                    MODE_PRIVATE);

            SharedPreferences.Editor editor = mPreferences.edit();

            editor.putString("FY_From_Date", clsBillNoFormat.getFY_From_Date());
            editor.putString("FY_To_Date", clsBillNoFormat.getFY_To_Date());
            editor.putString("FinancialYear", clsBillNoFormat.getFinancialYear());
//            editor.putString("current_bill_counter", clsBillNoFormat.getCurrent_bill_counter());

            editor.apply();
        } else if (mode.equalsIgnoreCase("Tax Not Apply")) {

            mPreferences = c.getSharedPreferences(Bill_No_Format_Tax_Not_Apply_Preferences,
                    MODE_PRIVATE);

            SharedPreferences.Editor editor = mPreferences.edit();


            editor.putString("FY_From_Date", clsBillNoFormat.getFY_From_Date());
            editor.putString("FY_To_Date", clsBillNoFormat.getFY_To_Date());
            editor.putString("FinancialYear", clsBillNoFormat.getFinancialYear());

//            editor.putString("current_bill_counter", clsBillNoFormat.getCurrent_bill_counter());
            editor.apply();
        }
    }


    public static void set_reset_counter(String counter, Context c, String mode) {

        if (mode.equalsIgnoreCase("YES")) {
            mPreferences = c.getSharedPreferences(Bill_No_Format_ApplyTax_Preferences,
                    MODE_PRIVATE);


        } else if (mode.equalsIgnoreCase("No")) {
            mPreferences = c.getSharedPreferences(Bill_No_Format_Tax_Not_Apply_Preferences,
                    MODE_PRIVATE);
        }

        SharedPreferences.Editor editor = mPreferences.edit();

        editor.putString("Reset_Counter", counter);

        editor.apply();

    }

    public static void clearBillFormat(Context context, String mode) {

        ClsBillNoFormat clsBillNoFormat = new ClsBillNoFormat();

        clsBillNoFormat.setPrefix("");
        clsBillNoFormat.setSuffix("");

        clsBillNoFormat.setFinancial_Year(false);
        clsBillNoFormat.setFinancial_Year_format("");
        clsBillNoFormat.setFinancial_Year_rb_id(0);

        clsBillNoFormat.setMonth(false);
        clsBillNoFormat.setMonth_format("NO");
        clsBillNoFormat.setMonth_rb_id(0);

        clsBillNoFormat.setYear(false);
        clsBillNoFormat.setYear_format("");
        clsBillNoFormat.setYear_rb_id(0);

        clsBillNoFormat.setSeparator("");
        clsBillNoFormat.setSeparator_rb_id(0);

        clsBillNoFormat.setBill_Start_Form("1");
        clsBillNoFormat.setBill_no_length("1");

        clsBillNoFormat.setReset_Counter_Name("Never");
        clsBillNoFormat.setReset_Counter_rb_id(0);
        clsBillNoFormat.setResetCounter("0");
        clsBillNoFormat.setBillNo_Format("Counter");

        clsBillNoFormat.setCurrent_bill_counter("0");

        clsBillNoFormat.setApplybillFormat(false);
        ClsGlobal.saveSettingApplyTax_Bill_No(clsBillNoFormat, context, mode);

    }

    public static void saveSettingApplyTax_Bill_No(ClsBillNoFormat clsBillNoFormat,
                                                   Context c, String mode) {


        if (mode.equalsIgnoreCase("ApplyTax")) {
            mPreferences = c.getSharedPreferences(Bill_No_Format_ApplyTax_Preferences,
                    MODE_PRIVATE);

            SharedPreferences.Editor editor = mPreferences.edit();

            editor.putString("Prefix", clsBillNoFormat.getPrefix());
            editor.putString("Suffix", clsBillNoFormat.getSuffix());

            // Financial Year.
            editor.putBoolean("Financial Year", clsBillNoFormat.isFinancial_Year());
            editor.putString("Financial Year format", clsBillNoFormat.getFinancial_Year_format());
            editor.putInt("Financial Year rb_id", clsBillNoFormat.getFinancial_Year_rb_id());

            // Month Year.
            editor.putBoolean("Month", clsBillNoFormat.isMonth());
            editor.putString("Month format", clsBillNoFormat.getMonth_format());
            editor.putInt("Month rb_id", clsBillNoFormat.getMonth_rb_id());

            // Year.
            editor.putBoolean("Year", clsBillNoFormat.isYear());
            editor.putString("Year format", clsBillNoFormat.getYear_format());
            editor.putInt("Year rb_id", clsBillNoFormat.getYear_rb_id());

            // Separator.
            editor.putString("Separator", clsBillNoFormat.getSeparator());
            editor.putInt("Separator rb_id", clsBillNoFormat.getSeparator_rb_id());

            // Bill Start from.
            editor.putString("Bill Start Form", clsBillNoFormat.getBill_Start_Form());
            editor.putString("Bill no length", clsBillNoFormat.getBill_no_length()
                    .equalsIgnoreCase("") ? "1" : clsBillNoFormat.getBill_no_length());

            editor.putString("Reset Counter Name", clsBillNoFormat.getReset_Counter_Name());
            editor.putInt("Reset Counter rb_id", clsBillNoFormat.getReset_Counter_rb_id());
            editor.putString("Reset_Counter", clsBillNoFormat.getResetCounter());

            editor.putString("Bill No Format", clsBillNoFormat.getBillNo_Format());

            editor.putBoolean("ApplyFormat", clsBillNoFormat.isApplybillFormat());

            editor.putString("FY_From_Date", clsBillNoFormat.getFY_From_Date());
            editor.putString("FY_To_Date", clsBillNoFormat.getFY_To_Date());
            editor.putString("FinancialYear", clsBillNoFormat.getFinancialYear());

//            editor.putString("current_bill_counter", clsBillNoFormat.getCurrent_bill_counter());

            editor.apply();
        } else if (mode.equalsIgnoreCase("Tax Not Apply")) {

            mPreferences = c.getSharedPreferences(Bill_No_Format_Tax_Not_Apply_Preferences,
                    MODE_PRIVATE);

            SharedPreferences.Editor editor = mPreferences.edit();

            editor.putString("Prefix", clsBillNoFormat.getPrefix());
            editor.putString("Suffix", clsBillNoFormat.getSuffix());

            // Financial Year.
            editor.putBoolean("Financial Year", clsBillNoFormat.isFinancial_Year());
            editor.putString("Financial Year format", clsBillNoFormat.getFinancial_Year_format());
            editor.putInt("Financial Year rb_id", clsBillNoFormat.getFinancial_Year_rb_id());

            // Month Year.
            editor.putBoolean("Month", clsBillNoFormat.isMonth());
            editor.putString("Month format", clsBillNoFormat.getMonth_format());
            editor.putInt("Month rb_id", clsBillNoFormat.getMonth_rb_id());

            // Year.
            editor.putBoolean("Year", clsBillNoFormat.isYear());
            editor.putString("Year format", clsBillNoFormat.getYear_format());
            editor.putInt("Year rb_id", clsBillNoFormat.getYear_rb_id());

            // Separator.
            editor.putString("Separator", clsBillNoFormat.getSeparator());
            editor.putInt("Separator rb_id", clsBillNoFormat.getSeparator_rb_id());

            // Bill Start from.
            editor.putString("Bill Start Form", clsBillNoFormat.getBill_Start_Form());
            editor.putString("Bill no length", clsBillNoFormat.getBill_no_length()
                    .equalsIgnoreCase("") ? "1" : clsBillNoFormat.getBill_no_length());
            editor.putString("Reset Counter Name", clsBillNoFormat.getReset_Counter_Name());
            editor.putInt("Reset Counter rb_id", clsBillNoFormat.getReset_Counter_rb_id());
            editor.putString("Reset_Counter", clsBillNoFormat.getResetCounter());

            editor.putString("Bill No Format", clsBillNoFormat.getBillNo_Format());

            editor.putBoolean("ApplyFormat", clsBillNoFormat.isApplybillFormat());

            editor.putString("FY_From_Date", clsBillNoFormat.getFY_From_Date());
            editor.putString("FY_To_Date", clsBillNoFormat.getFY_To_Date());
            editor.putString("FinancialYear", clsBillNoFormat.getFinancialYear());
//            editor.putString("current_bill_counter", clsBillNoFormat.getCurrent_bill_counter());
            editor.apply();
        }
    }

    public static void CreateBillSettingFile(String mode, Context cx) {

        if (mode.equalsIgnoreCase("TaxApply")) {
            Log.e("Checkfile", "TaxApply");

            File file = new File(
                    "/data/data/" + AppPackageName + "/shared_prefs/" +
                            Bill_No_Format_ApplyTax_Preferences + ".xml");
            if (!file.exists()) {
                Log.e("Checkfile", "TaxApply + f.exists()");
                SharedPreferences Preferences = cx.getSharedPreferences(
                        Bill_No_Format_ApplyTax_Preferences,
                        MODE_PRIVATE);

                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = Preferences.edit();
                editor.putString("Empty", "");
                editor.apply();

            }
        }

        if (mode.equalsIgnoreCase("TaxNotApply")) {
            Log.e("Checkfile", "TaxNotApply ");
            File file = new File(
                    "/data/data/" + AppPackageName + "/shared_prefs/" +
                            Bill_No_Format_Tax_Not_Apply_Preferences + ".xml");

            if (!file.exists()) {
                Log.e("Checkfile", "TaxNotApply !file.exists()");
                SharedPreferences Preferences = cx.getSharedPreferences(
                        Bill_No_Format_Tax_Not_Apply_Preferences,
                        MODE_PRIVATE);
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = Preferences.edit();
                editor.putString("Empty", "");
                editor.apply();

            }


        }
    }


    public static ClsBillNoFormat getSettingApplyTaxBillNo(Context c, String mode) {

        ClsBillNoFormat clsBillNoFormat = new ClsBillNoFormat();
        SharedPreferences mPreferences;
        if (mode.equalsIgnoreCase("YES")) { // Tax apply.

            File f = new File(
                    "/data/data/" + AppPackageName + "/shared_prefs/" +
                            Bill_No_Format_ApplyTax_Preferences + ".xml");
            if (f.exists()) {
                mPreferences = c.getSharedPreferences(
                        Bill_No_Format_ApplyTax_Preferences,
                        MODE_PRIVATE);

//            Log.e("Check", mPreferences.getString("Bill Start Form", ""));

//            clsBillNoFormat.setSettingApply(true);

                clsBillNoFormat = set(mPreferences, clsBillNoFormat);

                Log.d("TAG", "SharedPreferences Name_of_your_preference : exist");
            } else {
                Log.d("TAG", "Setup default preferences");

//                clearBillFormat(c,"ApplyTax");
//                mPreferences = c.getSharedPreferences(
//                        Bill_No_Format_ApplyTax_Preferences,
//                        MODE_PRIVATE);
//
//                return set(mPreferences,clsBillNoFormat);

                return clsBillNoFormat;
            }
        } else if (mode.equalsIgnoreCase("No")) { // Tax not apply.
            File f = new File(
                    "/data/data/" + AppPackageName + "/shared_prefs/"
                            + Bill_No_Format_Tax_Not_Apply_Preferences + ".xml");
            if (f.exists()) {
                mPreferences = c.getSharedPreferences(
                        Bill_No_Format_Tax_Not_Apply_Preferences,
                        MODE_PRIVATE);

//            Log.e("Check", mPreferences.getString("Bill Start Form", ""));

//            clsBillNoFormat.setSettingApply(true);

                clsBillNoFormat = set(mPreferences, clsBillNoFormat);


                Log.d("TAG", "SharedPreferences Name_of_your_preference : exist");
            } else {
                Log.d("TAG", "Setup default preferences");


                return clsBillNoFormat;
            }
        }


        return clsBillNoFormat;
    }

    public static NotificationCompat.Builder ProgressBarNotification(String title, String message,
                                                                     Context context,
                                                                     int id, String progressMode) {

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        //If on Oreo then notification required a notification channel.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_Name,
                    NotificationManager.IMPORTANCE_LOW);

            channel.enableVibration(false);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context,
                CHANNEL_ID);
        notification.setContentTitle(title);
        notification.setContentTitle(title);
        notification.setContentText(message);
        notification.setOngoing(true);
        notification.setDefaults(Notification.DEFAULT_SOUND);
        if (progressMode.equalsIgnoreCase("Show Percentage")) {
            notification.setProgress(100, 0, false);
        } else if (progressMode.equalsIgnoreCase("NoPercentage")) {
            notification.setProgress(0, 0, true);
        }
        notification.setAutoCancel(true);
        notification.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        notification.setSmallIcon(R.drawable.ic_backup_new);
//        builder.setProgress(0, 0, true);
        notificationManager.notify(id, notification.build());

        return notification;
    }

    public static void Delete_Unwanted_SharePrefrence_Files(String path) {
        try {

            File file = new File(path);
            String[] files;
            files = file.list();

            List<String> delete_files = new ArrayList<>();
            delete_files.add("androidx.work.util.id.xml");
            delete_files.add("androidx.work.util.id.xml.bak");
            delete_files.add("LoginDetails.xml.bak");
            delete_files.add("rzp_preference_private.xml");
            delete_files.add("com.demo.nspl.retailpos_preferences.xml");
            delete_files.add("rzp_preferences_storage_bridge.xml");
            delete_files.add("rzp_preference_public.xml");

            for (int i = 0; i < files.length; i++) {
                if (delete_files.contains(files[i])) {
                    File myFile = new File(file, files[i]);
                    myFile.delete();
                }
            }
        } catch (Exception e) {
            Log.e("Check", "Exception " + e.getMessage());
        }

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getTextFromTxtBox(TextView txt) {
        String result = "";
        try {

            if (txt.getText() != null && !txt.getText().toString().isEmpty())
                result = txt.getText().toString();
        } catch (Exception e) {
            e.getMessage();
        }

        return result;
    }

    public static String setTextBoxValue(Object val) {
        String result = "";
        try {

            if (val != null && !val.toString().isEmpty())
                result = val.toString();

        } catch (Exception e) {
            e.getMessage();
        }

        return result;
    }


    public static void BackupNotification(String title, String message, Context context, int id) {

        // ------------------- PendingIntent for SettingAutoEmailActivity ------------------------//

        Intent BackUpAndRestoreIntent = getNotificationIntent(context, BackUpAndRestoreActivity.class);
        PendingIntent contentBackUpAndRestorIntent = PendingIntent.getActivity(context,
                1, BackUpAndRestoreIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // ------------------------------ PendingIntent for ViewLogs-------------------------------//

        Intent ViewLogsIntent = getNotificationIntent(context, BackupLogsActivity.class);
        PendingIntent LogsIntent = PendingIntent.getActivity(context,
                2, ViewLogsIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //If on Oreo then notification required a notification channel.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_Name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            channel.setShowBadge(true);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .addAction(R.drawable.ic_backup, "Backup Now", contentBackUpAndRestorIntent)
                .addAction(R.drawable.ic_logs, "Logs", LogsIntent)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.ic_backup);

        if (notificationManager != null) {
            notificationManager.notify(id, notification.build());
        }
    }

    public static String getFileDateFormat() {
        // set the format to sql date time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh mm aa");
        Date date = new Date();
        return dateFormat.format(date);
    }


    /*public static String GetKeepOrderNo(Context context, String mode) {
        mPreferences = context.getSharedPreferences(mPreferncesName, MODE_PRIVATE);


        if (mode.equalsIgnoreCase("Sale")) {
            Log.e("getKeepOrderNo", "Sale call");
            return mPreferences.getString("KeepOrderNo_Sale", "") != null
                    && !mPreferences.getString("KeepOrderNo_Sale", "").equalsIgnoreCase("") ?
                    mPreferences.getString("KeepOrderNo_Sale", "") : "";
        } else if (mode.equalsIgnoreCase("Wholesale")) {

            Log.e("getKeepOrderNo", "Wholesale call");
            return mPreferences.getString("KeepOrderNo_Wholesale", "") != null
                    && !mPreferences.getString("KeepOrderNo_Wholesale", "").equalsIgnoreCase("") ?
                    mPreferences.getString("KeepOrderNo_Wholesale", "") : "";
        }

        return "";

    }
*/


    public static String GetKeepOrderNo(Context context, String mode) {
        mPreferences = context.getSharedPreferences(mPreferncesName, MODE_PRIVATE);


        if (mode.equalsIgnoreCase("Sale")) {

            return mPreferences.getString("KeepOrderNo_Sale", "") != null
                    && !mPreferences.getString("KeepOrderNo_Sale", "").equalsIgnoreCase("") ?
                    mPreferences.getString("KeepOrderNo_Sale", "") : "";

        } else if (mode.equalsIgnoreCase("Wholesale")) {

            return mPreferences.getString("KeepOrderNo_Wholesale", "") != null
                    && !mPreferences.getString("KeepOrderNo_Wholesale", "").equalsIgnoreCase("") ?
                    mPreferences.getString("KeepOrderNo_Wholesale", "") : "";

        } else if (mode.equalsIgnoreCase("Retail Quotation")) {

            return mPreferences.getString("KeepOrderNo_RetailQuotation", "") != null
                    && !mPreferences.getString("KeepOrderNo_RetailQuotation", "").equalsIgnoreCase("") ?
                    mPreferences.getString("KeepOrderNo_RetailQuotation", "") : "";

        } else if (mode.equalsIgnoreCase("Wholesale Quotation")) {

            return mPreferences.getString("KeepOrderNo_WholesaleQuotation", "") != null
                    && !mPreferences.getString("KeepOrderNo_WholesaleQuotation", "").equalsIgnoreCase("") ?
                    mPreferences.getString("KeepOrderNo_WholesaleQuotation", "") : "";
        }
        return "";
    }


    public static String ChangeDateQuotrationoDateFormat(Context context) {
        mPreferences = context.getSharedPreferences(mPreferncesName, MODE_PRIVATE);
        String yesOrNo = mPreferences.getString("DateQuotrationoDateFormat", "");
        if (yesOrNo.equalsIgnoreCase("")) {


            return "FirstTime";
        } else {
            return "SecondTime";
        }

    }

    /*public static void SetKeepOrderNo(Context context, String KeepOrderNo, String mode) {
        Log.e("resultibn", "SetKeepOrderNo: ");
        mPreferences = context.getSharedPreferences(mPreferncesName, MODE_PRIVATE);

        if (mode.equalsIgnoreCase("Sale")) {
            Log.e("getKeepOrderNo", "SetKeepOrderNo Sale call");

            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString("KeepOrderNo_Sale", KeepOrderNo);
            editor.apply();
        } else {
            Log.e("getKeepOrderNo", "SetKeepOrderNo Wholesale call");

            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString("KeepOrderNo_Wholesale", KeepOrderNo);
            editor.apply();

        }


    }*/


    public static void SetKeepOrderNo(Context context, String KeepOrderNo, String mode) {
        mPreferences = context.getSharedPreferences(mPreferncesName, MODE_PRIVATE);

        if (mode.equalsIgnoreCase("Sale")) {
            Log.e("getKeepOrderNo", "SetKeepOrderNo Sale call");

            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString("KeepOrderNo_Sale", KeepOrderNo);
            editor.apply();
        } else if (mode.equalsIgnoreCase("Wholesale")) {
            Log.e("getKeepOrderNo", "SetKeepOrderNo Wholesale call");

            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString("KeepOrderNo_Wholesale", KeepOrderNo);
            editor.apply();

        } else if (mode.equalsIgnoreCase("Retail Quotation")) {

            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString("KeepOrderNo_RetailQuotation", KeepOrderNo);
            editor.apply();

        } else if (mode.equalsIgnoreCase("Wholesale Quotation")) {

            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString("KeepOrderNo_WholesaleQuotation", KeepOrderNo);
            editor.apply();
        }

    }

    public static void Set_Folder_Id(Context context, String folderId) {
        Log.e("Upload", "Set_Folder_Id:- ");
        mPreferences = context.getSharedPreferences(mPreferncesName, MODE_PRIVATE);

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString("Folder_Id", folderId);
        editor.apply();
    }

    public static String get_FolderId(Context context) {
        mPreferences = context.getSharedPreferences(mPreferncesName, MODE_PRIVATE);
        return mPreferences.getString("Folder_Id", "") != null
                && !mPreferences.getString("Folder_Id", "").equalsIgnoreCase("")
                ? mPreferences.getString("Folder_Id", "") : "";

    }

    public static ProgressDialog pDialog;

    public static void CreateProgressDialog(Context context) {
        pDialog = new ProgressDialog(context);
    }

    public static void updateProgress(int val, String title, String msg) {
        pDialog.setTitle(title);
        pDialog.setMessage(msg);
        pDialog.setProgress(val);
    }

    public static void showProgress(String str) {
        try {
            pDialog.setCancelable(false);
            pDialog.setTitle("Please wait");
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setMax(100); // Progress Dialog Max Value
            pDialog.setMessage(str);
            if (pDialog.isShowing())
                pDialog.dismiss();
            pDialog.show();
        } catch (Exception e) {

        }
    }

    public static void hideProgress() {
        try {
            if (pDialog.isShowing())
                pDialog.dismiss();
        } catch (Exception e) {

        }

    }


       public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups))
                + " " + units[digitGroups];
    }


    public static String GetOrderEditMode(Context context) {
        mPreferences = context.getSharedPreferences(mPreferncesName, MODE_PRIVATE);

        return mPreferences.getString("entryMode", "") != null &&
                !mPreferences.getString("entryMode", "").equalsIgnoreCase("") ?
                mPreferences.getString("entryMode", "") : "";


    }


    public static void SetOrderEditMode(Context context, String entryMode) {
        mPreferences = context.getSharedPreferences(mPreferncesName, MODE_PRIVATE);

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString("entryMode", entryMode);
        editor.apply();

    }


    public static void InsertBackupLogs(String status, String Remark, Context context) {
        ClsCommonLogs clsCommonLogs = new ClsCommonLogs();
        clsCommonLogs.setStatus(status);
        clsCommonLogs.setRemark(Remark);
        clsCommonLogs.setLog_Type("Backup Logs");
        clsCommonLogs.setDate_Time(getEntryDateFormat(getCurruntDateTime()));
        ClsCommonLogs.INSERT(clsCommonLogs, context);
    }

    static List<String> list_step = new ArrayList<>();

    public static void SendAutoEmail(Context context, String mode) {
        final String[] stepLog = {""};

        list_step.clear();
        list_step.add("Start-".concat(ClsGlobal.getEntryDateTime()));

        Log.e("SendEmail", "SendEmail call");

        list_step.add("Check Internet Connection-".concat(ClsGlobal.getEntryDateTime()));


        if (CheckInternetConnection(context)) {
            list_step.add("Email to List-".concat(ClsGlobal.getEntryDateTime()));
            SharedPreferences mPreferences = context.getApplicationContext().getSharedPreferences(mPreferncesEmail, MODE_PRIVATE);

            String getListOfEmail1 = mPreferences.getString("EmailList", null);
            ArrayList<String> getCompleteEmailList = new Gson().fromJson(getListOfEmail1,
                    new TypeToken<ArrayList<String>>() {
                    }.getType());

            String str = "";


            if (getListOfEmail1 != null && getCompleteEmailList.size() != 0) {
                Log.e("CompleteEmailList_size", String.valueOf(getCompleteEmailList.size()));
                Log.e("getCompleteEmailList", "getCompleteEmailList inside call");
                list_step.add("Email preparing-".concat(ClsGlobal.getEntryDateTime()));
                String mailBodyStr = "";
                Calendar c = Calendar.getInstance();   // this takes current date
                c.set(Calendar.DAY_OF_MONTH, 1);

                String _firstDateOfMonth = ClsGlobal.getFirstDateOfMonth(c.getTime());
                StringBuilder mailTemplateStr = new StringBuilder();
                try {

                    InputStream json = context.getAssets().open("Mis_Template.txt");
                    ///String demo  =json.toString();
                    BufferedReader in = new BufferedReader(new InputStreamReader(json));
                    String strr = "";


                    while ((strr = in.readLine()) != null) {
                        mailTemplateStr.append(strr);
                    }

                    in.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                list_step.add("Getting MIS-".concat(ClsGlobal.getEntryDateTime()));
                //GET OrderMIS
                ClsOrderDetailMaster Obj = new ClsOrderDetailMaster(context);
                ClsExpenseMasterNew obj = new ClsExpenseMasterNew(context);
                ClsInventoryItem inventoryItem = new ClsInventoryItem(context);
                ClsExpenseMasterNew clsExpenseMasterNew = new ClsExpenseMasterNew(context);
                // ------------------InventoryMIS Today and Current Month ---------------------
                String _where = " AND stk.[TRANSACTION_DATE] = " + "'" + ClsGlobal.getEntryDate() + "'";
                Log.e("_where", _where);

                String getInventoryTodayMis = ClsInventoryItem.getInventoryMis(_where);

                _where = " AND stk.[TRANSACTION_DATE] BETWEEN "
                        .concat("('")
                        .concat(_firstDateOfMonth)
                        .concat("')")
                        .concat(" AND ")
                        .concat("('")
                        .concat(ClsGlobal.getEntryDate())
                        .concat("')");

                String getInventoryThisMonthMis = ClsInventoryItem.getInventoryMis(_where);

                // -----------------OrderMIS Today and Current Month.---------------------------
                _where = " AND [ENTRYDATETIME] BETWEEN ( " + "'" + ClsGlobal.getEntryDate() + " 00:00:00' )"
                        .concat(" AND (" + "'" + ClsGlobal.getEntryDate() + " 23:59:59')");
                Log.e("_where", _where);

                String orderMIS = ClsOrderDetailMaster.getOrderMIS(_where);//HTML table here

                _where = " AND [ENTRYDATETIME] BETWEEN ( " + "'" + _firstDateOfMonth + " 00:00:00' )"
                        .concat(" AND (" + "'" + ClsGlobal.getEntryDate() + " 23:59:59')");
                Log.e("_where", _where);

                String orderMISCurrentMonth = ClsOrderDetailMaster.getOrderMIS(_where);

                Log.e("_firstDateOfMonth", _firstDateOfMonth);

                // -----------------ExpenseMIS Today and Current Month.-------------------
                String _wher = " AND [RECEIPT_DATE] = "
                        .concat("'")
                        .concat(ClsGlobal.getEntryDate())
                        .concat("'");
                String ExpenseMISToday = ClsExpenseMasterNew.getExpenseMIS(_wher);

                //first date = 2018-10-01
                //currunt date/entrydate/todaydate: 2018-10-17

                //curunt dat MIS
                _wher = " AND [RECEIPT_DATE] BETWEEN "
                        .concat("('")
                        .concat(_firstDateOfMonth)
                        .concat("')")
                        .concat(" AND ")
                        .concat("('")
                        .concat(ClsGlobal.getEntryDate())
                        .concat("')");

                Log.d("_checkQRY", "_checkORY-- " + _wher);

                String ExpenseMISCurrentMonth = ClsExpenseMasterNew.getExpenseMIS(_wher);
                Log.e("ExpenseMISCurrentMonth", ExpenseMISCurrentMonth);

                // -------------------- MenuMIS Today and Current Month. --------------------

                _where = " AND [ENTRYDATETIME] BETWEEN ( " + "'" + ClsGlobal.getEntryDate() + " 00:00:00' )"
                        .concat(" AND (" + "'" + ClsGlobal.getEntryDate() + " 23:59:59')");// FOR TODAY

                String MenuMISToday = ClsOrderDetailMaster.getMenuMIS(_where);

                _where = " AND [ENTRYDATETIME] BETWEEN ( " + "'" + _firstDateOfMonth + " 00:00:00' )"
                        .concat(" AND (" + "'" + ClsGlobal.getEntryDate() + " 23:59:59')");

//                String MenuMISCurrentMonth = ClsOrderDetailMaster.getMenuMIS(_where);

                String _changeDateFormat = ClsGlobal.getCurruntDateTime();

                // --------------------- Sales MIS Current Month -------------------------//

                _where = " AND odm.[ENTRYDATETIME] BETWEEN ( " + "'" + _firstDateOfMonth + " 00:00:00' )"
                        .concat(" AND (" + "'" + ClsGlobal.getEntryDate() + " 23:59:59')");

//                String SalesMisCurrentMonth = ClsOrderDetailMaster.getSalesMIS(_where);

                mailBodyStr = mailTemplateStr.toString();
                String title = "auto generated MIS as on " + ClsGlobal.getEntryDateFormat(_changeDateFormat);
                mailBodyStr = mailBodyStr.replace("#title", title);


                mailBodyStr = mailBodyStr.replace("#MISTITLE1", "Order MIS - Today");
                mailBodyStr = mailBodyStr.replace("#MISSTR1", orderMIS);
                mailBodyStr = mailBodyStr.replace("#MISTITLE1", "");
//                    mailBodyStr = mailBodyStr.replace("#MISSTR1", "");

                mailBodyStr = mailBodyStr.replace("#MISTITLE2", "Order MIS - Current Month");
                mailBodyStr = mailBodyStr.replace("#MISSTR2", orderMISCurrentMonth);
                mailBodyStr = mailBodyStr.replace("#MISTITLE2", "");
//                    mailBodyStr = mailBodyStr.replace("#MISSTR2", "");

                mailBodyStr = mailBodyStr.replace("#MISTITLE3", "Vendor MIS - Today");
                mailBodyStr = mailBodyStr.replace("#MISSTR3", ExpenseMISToday);
                mailBodyStr = mailBodyStr.replace("#MISTITLE3", "");
//                    mailBodyStr = mailBodyStr.replace("#MISSTR3", "");

                mailBodyStr = mailBodyStr.replace("#MISTITLE4", "Vendor MIS - Current Month");
                mailBodyStr = mailBodyStr.replace("#MISSTR4", ExpenseMISCurrentMonth);
                mailBodyStr = mailBodyStr.replace("#MISTITLE4", "");
//                    mailBodyStr = mailBodyStr.replace("#MISSTR4", "");

                mailBodyStr = mailBodyStr.replace("#MISTITLE5", "Inventory MIS - Today");
                mailBodyStr = mailBodyStr.replace("#MISSTR5", getInventoryTodayMis);
                mailBodyStr = mailBodyStr.replace("#MISTITLE5", "");
//                    mailBodyStr = mailBodyStr.replace("#MISSTR5", "");

                mailBodyStr = mailBodyStr.replace("#MISTITLE6", "Inventory MIS - Current Month");
                mailBodyStr = mailBodyStr.replace("#MISSTR6", getInventoryThisMonthMis);
                mailBodyStr = mailBodyStr.replace("#MISTITLE6", "");
//                    mailBodyStr = mailBodyStr.replace("#MISSTR6", "");

                mailBodyStr = mailBodyStr.replace("#MISTITLE7", "Menu MIS - Today");
                mailBodyStr = mailBodyStr.replace("#MISSTR7", MenuMISToday);
                mailBodyStr = mailBodyStr.replace("#MISTITLE7", "");
//                    mailBodyStr = mailBodyStr.replace("#MISSTR7", "");

//                mailBodyStr = mailBodyStr.replace("#MISTITLE8", "");
//                mailBodyStr = mailBodyStr.replace("#MISSTR8", "");
//
//                mailBodyStr = mailBodyStr.replace("#MISTITLE9", "");
//                mailBodyStr = mailBodyStr.replace("#MISSTR9", "");

//                    mailBodyStr = mailBodyStr.replace("#MISSTR9", "");

//                mailBodyStr = mailBodyStr.replace("#MISTITLE9", "Sales MIS - Current Month");
//                mailBodyStr = mailBodyStr.replace("#MISSTR9", SalesMisCurrentMonth);

                // String getCheck = getInputData().getString(Check);

                // sendNotification("hi","I m From BackGround in RestaurantLite!");

                str = TextUtils.join(",", getCompleteEmailList);

                list_step.add("Getting Email config-".concat(ClsGlobal.getEntryDateTime()));
                ClsEmailConfiguration getEmailConfiguration = ClsGlobal.getEmailConfiguration(context);

//                        SendEmail androidEmail = new SendEmail("noreply@nathanisoftware.com",
//                                "1234567@nspl", (List) getCompleteEmailList, "Email for final Testing Hours and mini",
//                                mailBodyStr);

                list_step.add("Sending-".concat(ClsGlobal.getEntryDateTime()));
                SendEmail androidEmail = new SendEmail(getEmailConfiguration.getFromEmailId(),
                        getEmailConfiguration.getPassword(), getCompleteEmailList, mode,
                        mailBodyStr, getEmailConfiguration);
                stepLog[0] = TextUtils.join(", ", list_step);

                try {
                    androidEmail.createEmailMessage();
                    androidEmail.sendEmail();
                    InsertEmailLogs("Email Send Successfully.", context);
                } catch (MessagingException e) {
                    // Error Log.
                    ClsGlobal.CreateNotification("Auto Mail Sending Failed",
                            "Error while Sending Email or you Did not Configure Email Properly. At this Time: " + getEntryDateFormat(getCurruntDateTime()), context);

                    InsertEmailLogs("Error while Sending Email or you Did not Configure Email Properly. At this Time: "
                            + getEntryDateFormat(getCurruntDateTime()).concat(stepLog[0]).concat(", Error"), context);

                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {

                    ClsGlobal.CreateNotification("Auto Mail Sending Failed",
                            "Error while Sending Email. At this Time: " + getEntryDateFormat(getCurruntDateTime()), context);
                    // Error Log.
                    Log.e("EmailError", "Error email 2:" + e.getMessage().length());
                    InsertEmailLogs("Error while Sending Email or you Did not Configure Email Properly. At this Time: " + getEntryDateFormat(getCurruntDateTime()).concat(stepLog[0]).concat(", Error").concat(e.getMessage()), context);

                    e.printStackTrace();
                }

                Log.e("SendMailTask", "Mail Sent.");
            } else {

                // No Email id Found in SharedPreferences log.
                Log.e("NoEmail", "No Email id Found!");
                InsertEmailLogs("No Email id Found! ".concat(stepLog[0]), context);

            }
        } else {
            // No InterConnection Found log.
            ClsGlobal.CreateNotification("Auto Mail Sending Failed",
                    "No Internet Connection Found while Sending Email. At this Time: " + getEntryDateFormat(getCurruntDateTime()), context);

            Log.e("No_InterConnection", "No Internet Connection Found while Sending Email");
            InsertEmailLogs("No Internet Connection Found while Sending Email. At this Time: " + getEntryDateFormat(getCurruntDateTime()).concat(stepLog[0]), context);
        }


    }

    public static void SendEmail(Context context, String mode) {

        Log.e("SendEmail", "SendEmail call");

        Thread backGroundThreadSendingEmail = new Thread(() -> {

//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);

            if (CheckInternetConnection(context)) {
                SharedPreferences mPreferences = context.getApplicationContext().getSharedPreferences(mPreferncesEmail, MODE_PRIVATE);

                String getListOfEmail1 = mPreferences.getString("EmailList", null);
                ArrayList<String> getCompleteEmailList = new Gson().fromJson(getListOfEmail1,
                        new TypeToken<ArrayList<String>>() {
                        }.getType());

                String str = "";


                if (getListOfEmail1 != null && getCompleteEmailList.size() != 0) {

                    Log.e("CompleteEmailList_size", String.valueOf(getCompleteEmailList.size()));
                    Log.e("getCompleteEmailList", "getCompleteEmailList inside call");

                    String mailBodyStr = "";
                    Calendar c = Calendar.getInstance();   // this takes current date
                    c.set(Calendar.DAY_OF_MONTH, 1);

                    String _firstDateOfMonth = ClsGlobal.getFirstDateOfMonth(c.getTime());
                    StringBuilder mailTemplateStr = new StringBuilder();

                    try {

                        InputStream json = context.getAssets().open("Mis_Template.txt");
                        ///String demo  =json.toString();
                        BufferedReader in = new BufferedReader(new InputStreamReader(json));
                        String strr = "";

                        while ((strr = in.readLine()) != null) {
                            mailTemplateStr.append(strr);
                        }

                        in.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //GET OrderMIS
                    ClsOrderDetailMaster Obj = new ClsOrderDetailMaster(context);
                    ClsExpenseMasterNew obj = new ClsExpenseMasterNew(context);
                    ClsInventoryItem inventoryItem = new ClsInventoryItem(context);
                    ClsExpenseMasterNew clsExpenseMasterNew = new ClsExpenseMasterNew(context);
                    // ------------------InventoryMIS Today and Current Month ---------------------
                    String _where = " AND stk.[TRANSACTION_DATE] = " + "'" + ClsGlobal.getEntryDate() + "'";
                    Log.e("_where", _where);

                    String getInventoryTodayMis = ClsInventoryItem.getInventoryMis(_where);

                    _where = " AND stk.[TRANSACTION_DATE] BETWEEN "
                            .concat("('")
                            .concat(_firstDateOfMonth)
                            .concat("')")
                            .concat(" AND ")
                            .concat("('")
                            .concat(ClsGlobal.getEntryDate())
                            .concat("')");

                    String getInventoryThisMonthMis = ClsInventoryItem.getInventoryMis(_where);

                    // -----------------OrderMIS Today and Current Month.---------------------------
                   /* _where = " AND [ENTRYDATETIME] BETWEEN ( " + "'" + ClsGlobal.getEntryDate() + " 00:00:00' )"
                            .concat(" AND (" + "'" + ClsGlobal.getEntryDate() + " 23:59:59')");
                    Log.e("_where", _where);*/


                    // -------------------- MenuMIS Today and Current Month. --------------------

                   /* _where = " AND [ENTRYDATETIME] BETWEEN ( " + "'" + ClsGlobal.getEntryDate() + " 00:00:00' )"
                            .concat(" AND (" + "'" + ClsGlobal.getEntryDate() + " 23:59:59')");// FOR TODAY

                    String MenuMISToday = ClsOrderDetailMaster.getMenuMIS(_where);

                    _where = " AND [ENTRYDATETIME] BETWEEN ( " + "'" + _firstDateOfMonth + " 00:00:00' )"
                            .concat(" AND (" + "'" + ClsGlobal.getEntryDate() + " 23:59:59')");

                    String MenuMISCurrentMonth = ClsOrderDetailMaster.getMenuMIS(_where);

                    String _changeDateFormat = ClsGlobal.getCurruntDateTime();*/

                    // --------------------- Sales MIS Current Month -------------------------//

                  /*  _where = " AND odm.[ENTRYDATETIME] BETWEEN ( " + "'" + _firstDateOfMonth + " 00:00:00' )"
                            .concat(" AND (" + "'" + ClsGlobal.getEntryDate() + " 23:59:59')");

                    String SalesMisCurrentMonth = ClsOrderDetailMaster.getSalesMIS(_where);*/

                    mailBodyStr = mailTemplateStr.toString();
                    String _changeDateFormat = ClsGlobal.getCurruntDateTime();
                    String title = "auto generated MIS as on " + ClsGlobal.getEntryDateFormat(_changeDateFormat);

                    mailBodyStr = mailBodyStr.replace("#title", title);
//                    mailBodyStr = mailBodyStr.replace("#title", "Hello Hello New Test");


//------------------------------------   stockMIS   -----------------------------------------------------------

                    String stockMIS = ClsSendMIS.getStockMIS(context);//HTML table here
                    Log.e("--MIS--", "--stockMIS-- " + stockMIS);
                    mailBodyStr = mailBodyStr.replace("#MISTITLE1", "STOCK MIS");
                    mailBodyStr = mailBodyStr.replace("#MISSTR1", stockMIS);

                    Log.d("--mail--", "stockMIS: " + stockMIS);

// -----------------PAYMENT MIS Today and Current Month.-------------------
                    String _wherePaymentMIS = " AND [PaymentDate] BETWEEN ( " + "'" + ClsGlobal.getEntryDate() + "' )"
                            .concat(" AND (" + "'" + ClsGlobal.getEntryDate() + "')");

//                        String _wherePaymentMIS = " AND [PaymentDate] BETWEEN ( " + "'" + ClsGlobal.getEntryDate() + " 00:00:00' )"
//                                .concat(" AND (" + "'" + ClsGlobal.getEntryDate() + " 23:59:59')");

                    String paymentMISforToday = ClsSendMIS.getPaymentMIS(context, _wherePaymentMIS, false);//HTML table here
                    Log.e("--MIS--", "--paymentMISforToday-- " + paymentMISforToday);
                    mailBodyStr = mailBodyStr.replace("#MISTITLE2", "PAYMENT MIS (" + ClsGlobal.getChangeDateFormatAllExp(ClsGlobal.getEntryDate()) + ")");
                    mailBodyStr = mailBodyStr.replace("#MISSTR2", paymentMISforToday);

                    _wherePaymentMIS = " AND [PaymentDate] BETWEEN ( " + "'" + _firstDateOfMonth + "' )"
                            .concat(" AND (" + "'" + ClsGlobal.getEntryDate() + "')");

                    String paymentMISforMonth = ClsSendMIS.getPaymentMIS(context, _wherePaymentMIS, true);//HTML table here
                    Log.e("--MIS--", "--paymentMISforMonth-- " + paymentMISforMonth);

                    mailBodyStr = mailBodyStr.replace("#MISTITLE3", "PAYMENT MIS (" + ClsGlobal.getMonthYear(ClsGlobal.getEntryDate()) + ")");
                    mailBodyStr = mailBodyStr.replace("#MISSTR3", paymentMISforMonth);

                    String paymentMISforTodayInOut = ClsSendMIS.getPaymentMIS_In_Out(context, _wherePaymentMIS);//HTML table here
                    Log.e("--MIS--", "--paymentMISforTodayInOut-- " + paymentMISforTodayInOut);

                    mailBodyStr = mailBodyStr.replace("#MISTITLE4", "PAYMENT MIS IN OUT (" + ClsGlobal.getMonthYear(ClsGlobal.getEntryDate()) + ")");
                    mailBodyStr = mailBodyStr.replace("#MISSTR4", paymentMISforTodayInOut);


                  /*  String paymentMISforMonthlyInOut = ClsSendMIS.getPaymentMIS_In_Out_Monthly(context, _wherePaymentMIS);//HTML table here
                    Log.e("--MIS--", "--paymentMISforMonthlyInOut-- " + paymentMISforTodayInOut);

                    mailBodyStr = mailBodyStr.replace("#MISTITLE9", "PAYMENT MIS IN OUT (" + ClsGlobal.getChangeDateFormatAllExp(ClsGlobal.getEntryDate()) + ")");
                    mailBodyStr = mailBodyStr.replace("#MISSTR9", paymentMISforMonthlyInOut);

*/

// -----------------ExpenseMIS Today and Current Month.-------------------

                    String _wher = " AND [RECEIPT_DATE] = "
                            .concat("'")
                            .concat(ClsGlobal.getEntryDate())
                            .concat("'");

                    String ExpenseMISToday = ClsExpenseMasterNew.getExpenseMIS(_wher);
                    Log.e("--MIS--", "--ExpenseMISToday-- " + ExpenseMISToday);

                    //first date = 2018-10-01
                    //currunt date/entrydate/todaydate: 2018-10-17

                    //curunt dat MIS
                    _wher = " AND [RECEIPT_DATE] BETWEEN "
                            .concat("('")
                            .concat(_firstDateOfMonth)
                            .concat("')")
                            .concat(" AND ")
                            .concat("('")
                            .concat(ClsGlobal.getEntryDate())
                            .concat("')");

                    Log.d("_checkQRY", "_checkORY-- " + _wher);

                    String ExpenseMISCurrentMonth = ClsExpenseMasterNew.getExpenseMIS(_wher);

                    Log.e("--MIS--", "--ExpenseMISCurrentMonth-- " + ExpenseMISCurrentMonth);


// -----------------SALE MIS Today and Current Month.-------------------


                    String _whereSale = " AND [ord].[BillDate] BETWEEN ( " + "'" + ClsGlobal.getEntryDate() + " 00:00:00' )"
                            .concat(" AND (" + "'" + ClsGlobal.getEntryDate() + " 23:59:59')");
                    Log.e("_where", _where);

                    String SaleMisToday = ClsSendMIS.getSaleMis(_whereSale, context, false);
                    Log.e("--MIS--", "--str-- " + SaleMisToday);

                    _whereSale = " AND [ord].[BillDate] BETWEEN ( " + "'" + _firstDateOfMonth + " 00:00:00' )"
                            .concat(" AND (" + "'" + ClsGlobal.getEntryDate() + " 23:59:59')");

                    Log.e("--MIS--", "--_where-- " + _whereSale);

                    String SaleMisMonthly = ClsSendMIS.getSaleMis(_whereSale, context, true);
                    Log.d("--MIS--", "SaleMisMonthly-- " + SaleMisMonthly);
                    Log.e("_firstDateOfMonth", _firstDateOfMonth);

                    mailBodyStr = mailBodyStr.replace("#MISTITLE5", "SALE MIS (" + ClsGlobal.getChangeDateFormatAllExp(ClsGlobal.getEntryDate()) + ")");
                    mailBodyStr = mailBodyStr.replace("#MISSTR5", SaleMisToday);

                    mailBodyStr = mailBodyStr.replace("#MISTITLE6", "SALE MIS (" + ClsGlobal.getMonthYear(ClsGlobal.getEntryDate()) + ")");
                    mailBodyStr = mailBodyStr.replace("#MISSTR6", SaleMisMonthly);


// -----------------PURCHASE MIS Today and Current Month.-------------------


                    String _wherePurchase = " AND PM.[PurchaseDate] BETWEEN ( " + "'" + _firstDateOfMonth + "' )"
                            .concat(" AND (" + "'" + ClsGlobal.getEntryDate() + "')");
                    Log.e("_where", _where);


                    String PuchaseMisToday = ClsSendMIS.getPurchaseMIS(_wherePurchase, context, true);
                    Log.e("--Purchase--", "--str-- " + PuchaseMisToday);


                    mailBodyStr = mailBodyStr.replace("#MISTITLE7", "PURCHASE MIS (" + ClsGlobal.getMonthYear(ClsGlobal.getEntryDate()) + ")");
                    mailBodyStr = mailBodyStr.replace("#MISSTR7", PuchaseMisToday);
                    mailBodyStr = mailBodyStr.replace("#appVersion", "AppName: " +
                            context.getResources().getString(R.string.app_name)
                            + ",V" + getApplicationVersion(context));
//                    mailBodyStr = mailBodyStr.replace("#appVersion", "AppName:fTouch POS,V1.1.0");


                    // String getCheck = getInputData().getString(Check);
                    // sendNotification("hi","I m From BackGround in RestaurantLite!");

                    str = TextUtils.join(",", getCompleteEmailList);

                    int maxLogSize = 1000;
                    for (int i = 0; i <= mailBodyStr.length() / maxLogSize; i++) {
                        int start = i * maxLogSize;
                        int end = (i + 1) * maxLogSize;
                        end = end > mailBodyStr.length() ? mailBodyStr.length() : end;
                        Log.v("ExpenseMisBody- ", mailBodyStr.substring(start, end));
                        Log.v("--MIS--", "----FINAL---- " + mailBodyStr.substring(start, end));
                    }

                    ClsEmailConfiguration getEmailConfiguration = ClsGlobal.getEmailConfiguration(context);

//                        SendEmail androidEmail = new SendEmail("noreply@nathanisoftware.com",
//                                "1234567@nspl", (List) getCompleteEmailList, "Email for final Testing Hours and mini",
//                                mailBodyStr);

                    SendEmail androidEmail = new SendEmail(getEmailConfiguration.getFromEmailId(),
                            getEmailConfiguration.getPassword(), getCompleteEmailList, mode,
                            mailBodyStr, getEmailConfiguration);

                    Log.e("--MIS--", "--STOCK-- " + androidEmail);

                    try {
                        androidEmail.createEmailMessage();
                        androidEmail.sendEmail();
                        InsertEmailLogs("Email Send Successfully", context);
                    } catch (MessagingException e) {
                        // Error Log.
                        Log.e("EmailError", "Error email");
                        InsertEmailLogs("Error while Sending Email or you Did not Configure Email Properly! ", context);

                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        // Error Log.
                        Log.e("EmailError1", "Error email 2");
                        InsertEmailLogs("Error while Sending Email or you Did not Configure Email Properly!", context);

                        e.printStackTrace();
                    }
                    Log.e("SendMailTask", "Mail Sent.");
                } else {
                    // No Email id Found in SharedPreferences log.
                    Log.e("NoEmail", "No Email id Found!");
                    InsertEmailLogs("No Email id Found!", context);

                }
            } else {
                // No InterConnection Found log.
                Log.e("No_InterConnection", "No Internet Connection Found while Sending Email");
                InsertEmailLogs("No Internet Connection Found while Sending Email", context);

            }

        });

        backGroundThreadSendingEmail.start();

    }

    private static final String CHANNEL_ID = ClsGlobal.AppPackageName.concat(".Notification");
    private static final String CHANNEL_Name = "App Important Notification";
    private static final String CHANNEL_description = "All App System Important Notification's";

    public static void CreateNotification(String title, String message, Context context) {

        // ------------------- PendingIntent for SettingAutoEmailActivity ------------------------//

        Intent activityIntent = getNotificationIntent(context, SettingAutoEmailActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                1, activityIntent, 0);

        // ------------------------------ PendingIntent for ViewLogs-------------------------------//

        Intent ViewLogsIntent = getNotificationIntent(context, EmailLogsActivity.class);
        PendingIntent LogsIntent = PendingIntent.getActivity(context,
                2, ViewLogsIntent, 0);

        //------------------------- For Sending broadcastIntent ----------------------------------//

        Intent broadcastIntent = new Intent(context, NotificationReceiver.class);
        broadcastIntent.putExtra("notificationId", "23");

        PendingIntent actionIntent = PendingIntent.getBroadcast(context,
                3, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //----------------------------------------------------------------------------------------//

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //If on Oreo then notification required a notification channel.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_Name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            channel.setShowBadge(true);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .addAction(R.drawable.ic_send, "Send Now", actionIntent)
                .addAction(R.drawable.ic_logs, "Logs", LogsIntent)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_email_dark);

        if (notificationManager != null) {
            notificationManager.notify(23, notification.build());
        }


    }

    private static Intent getNotificationIntent(Context context, Class className) {
        Intent intent = new Intent(context, className);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    public static void viewPdf(String file, String directory, Context context) {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
        Log.e("File", "pdfFile" + pdfFile);
        //   Uri uri=Uri.fromFile(pdfFile);
        Uri outputUri = FileProvider.getUriForFile(context, AUTHORITY, pdfFile);

        Log.e("File", "uri--->>" + outputUri);

        Intent viewFile = new Intent(Intent.ACTION_VIEW);
        viewFile.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        viewFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        viewFile.setDataAndType(outputUri, "application/pdf");
        context.startActivity(viewFile);

        Log.d("FILE--", "path-- " + viewFile.setDataAndType(outputUri, "application/pdf"));

        try {
            context.startActivity(viewFile);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static ProgressDialog _prProgressDialog(Context c, String msg, Boolean setCancelable) {
        ProgressDialog progressDialog = new ProgressDialog(c, R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage(msg); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(setCancelable);
//        progressDialog.show(); // Display Progress Dialog
        return progressDialog;
    }

    public static void copyToClipboard(String text, Context context) {
        // Get clipboard manager object.
        Object clipboardService = context.getSystemService(CLIPBOARD_SERVICE);
        final ClipboardManager clipboardManager = (ClipboardManager) clipboardService;

        ClipData clipData = ClipData.newPlainText("Source Text", text);
        // Set it as primary clip data to copy text to system clipboard.

        clipboardManager.setPrimaryClip(clipData);
//        Toast.makeText(context, "Mobile Number Copied", Toast.LENGTH_LONG).show();
    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    public static void SetSharedPreferencesFile(String filename, String key, String values, Context c) {
        SharedPreferences mPreferences = c.getSharedPreferences(filename, MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString(key, values);
        preferencesEditor.apply();
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getIMEIno(Context context) {

        String deviceId;

        if (android.os.Build.VERSION.SDK_INT >= 29) {

            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        } else {

            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);


            if (mTelephony.getDeviceId() != null) {
                deviceId = mTelephony.getDeviceId();
            } else {
                deviceId = Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }
        return deviceId;
    }


    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getDeviceInfo(Context context) {

        List<String> lstDeviceInfo = new ArrayList<>();

        String model = android.os.Build.MODEL;
        String serial = "";

        if (android.os.Build.VERSION.SDK_INT >= 29) {
            serial = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
                serial = android.os.Build.SERIAL;
            } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                serial = Build.getSerial();
            }

        }


        String manufacturer = android.os.Build.MANUFACTURER;
        String androidSDK = String.valueOf(android.os.Build.VERSION.SDK_INT);
        String androidVersion = android.os.Build.VERSION.RELEASE;


        lstDeviceInfo = new ArrayList<>();
        lstDeviceInfo.add("MODEL: ".concat(model) + "\n");
        lstDeviceInfo.add("SERIAL: ".concat(serial) + "\n");
        lstDeviceInfo.add("MANUFACTURER: ".concat(manufacturer) + "\n");
        lstDeviceInfo.add("ANDROIDSDK: ".concat(androidSDK) + "\n");
        lstDeviceInfo.add("ANDROIDVERSION: ".concat(androidVersion) + "\n");
        return TextUtils.join("", lstDeviceInfo);

    }

    static StringBuilder getCheckOutInvoiceTemplate(Context context) {
        StringBuilder mailBody = new StringBuilder();

        try {
            InputStream json = context.getAssets().open("SaleInvoiceTemplate.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(json));
            String str = "";

            while ((str = in.readLine()) != null) {
                mailBody.append(str);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mailBody;
    }

    public static String getApplicationVersion(Context context) {
        String result = "";
        try {
            /*Version name*/
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            result = String.valueOf(pInfo.versionName);
            Log.i("current", "" + result);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(context, "Error in getApplicationVersion", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    public static String getEntryDateAndTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
        return df.format(calendar.getTime());
    }

    public static String getEntryDate_dd_MM_yyyy() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        return df.format(calendar.getTime());

    }


    /**
     * Make first char capital from given string and
     * characters from first characters convert in to Lower Case.
     * For EX: input is "goa" than output is "Goa".
     * For EX: input is "Goa" than output is "Goa".
     * For EX: input is "GOA" than output is "Goa".
     * For EX: input is "gOA" than output is "Goa".
     * @param str
     * @return  str //formated.
     */
    public static String capitalize_First_Char(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }


            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public static String getEntryDate() {
        // set the format to sql date time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getFirstDateOfMonth(Date _dt) {
        final String outFormat = "yyyy-MM-dd";
        Date date = _dt;
        DateFormat formatter = new SimpleDateFormat(outFormat, Locale.getDefault());
        return formatter.format(date);
    }

    public static String getLastBkpFileDate(Date _dt) {
        final String outFormat = "dd MMM yyyy hh:mm aa";
        Date date = _dt;
        DateFormat formatter = new SimpleDateFormat(outFormat, Locale.getDefault());
        return formatter.format(date);
    }

    public static String GetSharedPreferencesFile(String filename, String key, Context c) {

        String result = "";
        SharedPreferences mPreferences = c.getSharedPreferences(filename, MODE_PRIVATE);
        result = mPreferences.getString(key, null);
        return result;
    }

    public static String getRandom() {
        String result = "";
        Random random = new Random();
        result = String.format("%03d", random.nextInt(10000));
        return result;
    }


    public static String getCurruntDateTime() {
        // set the format to sql date time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getDateFromMilliSeconds(long milliSeconds, String dateFormat) throws NumberFormatException {
        Log.e("mili", milliSeconds + dateFormat);
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        String dateToReturn = formatter.format(calendar.getTime());
        Log.e("dateToReturn", dateToReturn);
        return dateToReturn;
    }

    public static Date getPreetyDateTimeFormat(String yyyy_mm_dd_hh_mm_ss) {
        Date convertedDate = new Date();
        if (yyyy_mm_dd_hh_mm_ss != null && !yyyy_mm_dd_hh_mm_ss.isEmpty() &&
                yyyy_mm_dd_hh_mm_ss.length() != 0) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

            try {
                convertedDate = dateFormat.parse(yyyy_mm_dd_hh_mm_ss);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return convertedDate;
    }


    public static String getSmsString(List<ClsInventoryOrderDetail> list,
                                      ClsInventoryOrderMaster getCurrentObj,
                                      String CustomerName,
                                      String Mobile_no,
                                      String mode,
                                      Context context) {

        List<String> items = new ArrayList<>();

        int srNO = 0;

        Gson gson = new Gson();
        String jsonInString = gson.toJson(list);
        Log.e("jsonInString:-- ", jsonInString);

        for (ClsInventoryOrderDetail s : list) {
            srNO++;

            items.add("".concat(String.valueOf(srNO).concat(". ")).concat(s.getItem().concat("\n")
                    .concat("Rate: " + ClsGlobal.round(s.getSaleRate(), 2)).concat("\n")
                    .concat("Qty: " + ClsGlobal.round(Double.parseDouble(
                            RemoveZeroFromDouble(String.valueOf(s.getQuantity()))), 2)).concat("\n")
                    .concat("Amount: ").concat(String.valueOf(ClsGlobal.round(Double.parseDouble(
                            RemoveZeroFromDouble(String.valueOf(s.getAmount()))), 2))).concat("\n"))//"PUL
                    .concat("Details: ").concat(s.getItemComment().equalsIgnoreCase("")
                            ? "" : s.getItemComment()).concat("\n"));//"PUL
        }

        items.add("Amt: " + getCurrentObj.getTotalAmount());

        if (getCurrentObj.getDiscountAmount() != 0.0) {
            items.add("Discount: " + ClsGlobal.round(getCurrentObj.getDiscountAmount(), 2));
        }

        if (getCurrentObj.getApplyTax().equalsIgnoreCase("YES")) {
            items.add("Tax Amt: ".concat(String.valueOf(ClsGlobal.round(
                    Double.valueOf(RemoveZeroFromDouble(String.valueOf(
                            getCurrentObj.getTotalTaxAmount()))), 2))));///////remove decimal place if is 0
        }

        items.add("Received Amt: " + ClsGlobal.round(getCurrentObj.getPaidAmount(), 2));


        if (getCurrentObj.getDifferent_Amount_mode().equalsIgnoreCase("ADJUSTMENT")) {

            items.add("Adjustment Amt: " + ClsGlobal.round(getCurrentObj.getAdjumentAmount(), 2));

        } else {

            items.add("Pending Amt: " + ClsGlobal.round(getCurrentObj.getAdjumentAmount(), 2));

        }


        items.add("Bill Amt: ".concat(String.valueOf(ClsGlobal.round(
                getCurrentObj.getTotalReceiveableAmount(), 2))));

        String smsBody = ClsGlobal.getSmsHeaderString(items, context,
                CustomerName.equalsIgnoreCase("") ?
                        "" : CustomerName.trim(),
                String.valueOf(getCurrentObj.getOrderNo())
                , mode, ""
                , Mobile_no.equalsIgnoreCase("")
                        ? "" : Mobile_no.trim(), getCurrentObj.getApplyTax());

        Log.i("stringBufferConcat", String.valueOf(smsBody));
        return smsBody;
    }


    public static String getSmsHeaderString(List<String> list, Context context,
                                            String CustomerName,
                                            String _orderNo,
                                            String _mode,
                                            String _validity,
                                            String MobileNo, String applyTax) {

        ClsUserInfo userInfo = getUserInfo(context);


        StringBuilder stringBufferConcat = new StringBuilder();
        stringBufferConcat.append(userInfo.getBusinessname() + "\n\n");
        stringBufferConcat.append("Contact No: " + userInfo.getMobileNo() + "\n");
        stringBufferConcat.append("Email: " + userInfo.getEmailaddress() + "\n");
        stringBufferConcat.append("City: " + userInfo.getCity() + "\n");
        stringBufferConcat.append("DATE: " + ClsGlobal.getEntryDateFormat(ClsGlobal.getCurruntDateTime()) + "\n");

        if (_mode.equalsIgnoreCase("RecentOrder")) {
            stringBufferConcat.append("BILL NO: " + _orderNo + "\n");
        } else if (_mode.equalsIgnoreCase("InvoiceInfo")) {
            stringBufferConcat.append("BILL NO: " + getNextOrderNo(context, applyTax, "SendMessage") + "\n");
        } else if (_mode.equalsIgnoreCase("RecentQuotation")) {
            stringBufferConcat.append("QUOTATION NO: " + getNextOrderNo(context, applyTax, "SendMessage") + "\n");
            stringBufferConcat.append("VALID UPTO: " + _validity + "\n\n");
        } else {
            stringBufferConcat.append("BILL NO: " + getNextOrderNo(context, applyTax, "SendMessage") + "\n");
        }


        if (!CustomerName.equalsIgnoreCase("")
                && !MobileNo.equalsIgnoreCase("")) {
            stringBufferConcat.append("Customer Details \n");
            stringBufferConcat.append(CustomerName + "\n");
            stringBufferConcat.append(MobileNo + "\n\n");
        }

        stringBufferConcat.append("Item Details\n");

        for (String s : list) {
            Log.i("list", String.valueOf(list.size()));

            stringBufferConcat.append(s);//pulow x1 200, pul x2.5 100.20
            stringBufferConcat.append("\n");
        }

        stringBufferConcat.append("\nThank you");
        Log.i("stringBufferConcat1", String.valueOf(stringBufferConcat));
        return stringBufferConcat.toString();

    }


//
//    public static void copyFile(File sourceFile, File destFile) throws IOException {
//        if (!sourceFile.exists()) {
//            return;
//        }
//
//        FileChannel source = null;
//        FileChannel destination = null;
//        source = new FileInputStream(sourceFile).getChannel();
//        destination = new FileOutputStream(destFile).getChannel();
//        if (destination != null && source != null) {
//            destination.transferFrom(source, 0, source.size());
//        }
//        if (source != null) {
//            source.close();
//        }
//        if (destination != null) {
//            destination.close();
//        }
//    }


    public static String getEntryDateTime() {
        // set the format to sql date time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }


    public static String getEntryDateForExpiredAllData() {
        // set the format to sql date time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date).concat(" 00:00:00");
    }


    public static String getMonthName(int month) {
        switch (month) {
            case 1:
                return "Jan";

            case 2:
                return "Feb";

            case 3:
                return "Mar";

            case 4:
                return "Apr";

            case 5:
                return "May";

            case 6:
                return "Jun";

            case 7:
                return "Jul";

            case 8:
                return "Aug";

            case 9:
                return "Sep";

            case 10:
                return "Oct";

            case 11:
                return "Nov";

            case 12:
                return "Dec";
        }

        return "";
    }

    public static String getPDFFormat(String dateStr) {
        final String DATE_DASH_FORMAT = "dd-MM-yyyy";
        final String DATE_FORMAT = "yyyy-MM-dd";
        try {
            if (dateStr != null && !dateStr.isEmpty() && dateStr != "") {
                Date date = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).parse(dateStr);
                DateFormat formatter = new SimpleDateFormat(DATE_DASH_FORMAT, Locale.getDefault());
                dateStr = formatter.format(date.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateStr;
    }

    public static String getEntryDateFormat(String e_Date) {
        final String DATE_DASH_FORMAT = "dd/MM/yyyy hh:mm aa";
        final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
        try {
            if (e_Date != null && !e_Date.isEmpty() && e_Date != "") {
                Date date = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).parse(e_Date);
                DateFormat formatter = new SimpleDateFormat(DATE_DASH_FORMAT, Locale.getDefault());
                e_Date = formatter.format(date.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return e_Date;
    }

    public static String getChangeDateFormatDB(String e_Date) {

        final String OUT = "yyyy-MM-dd HH:mm:ss";
        final String DATE_FORMAT = "dd/MM/yyyy hh:mm aa";
        try {
            if (e_Date != null && !e_Date.isEmpty() && e_Date != "") {
                Date date = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).parse(e_Date);
                DateFormat formatter = new SimpleDateFormat(OUT, Locale.getDefault());
                e_Date = formatter.format(date.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return e_Date;
    }


    public static String getDDMYYYYFormat(String dbFormateString) {
        final String outFormat = "dd/MM/yyyy";
        final String InFORMAT = "yyyy-MM-dd hh:mm:ss";
        String result = "";
        try {
            if (dbFormateString != null && !dbFormateString.isEmpty() && dbFormateString != "") {
                Date date = new SimpleDateFormat(InFORMAT, Locale.ENGLISH).parse(dbFormateString);
                DateFormat formatter = new SimpleDateFormat(outFormat, Locale.getDefault());
                result = formatter.format(date.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }


    public static String getSmsDateFormat(String dbFormateString) {
        final String outFormat = "dd MMM hh:mm aa";
        final String InFORMAT = "yyyy-MM-dd hh:mm:ss";
        String result = "";
        try {
            if (dbFormateString != null && !dbFormateString.isEmpty() && dbFormateString != "") {
                Date date = new SimpleDateFormat(InFORMAT, Locale.ENGLISH).parse(dbFormateString);
                DateFormat formatter = new SimpleDateFormat(outFormat, Locale.getDefault());
                result = formatter.format(date.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getPaymentDateFormat(String dbFormateString) {
        final String outFormat = "dd MMM yyyy hh:mm aa";
        final String InFORMAT = "yyyy-MM-dd hh:mm:ss";
        String result = "";
        try {
            if (dbFormateString != null && !dbFormateString.isEmpty() && dbFormateString != "") {
                Date date = new SimpleDateFormat(InFORMAT, Locale.ENGLISH).parse(dbFormateString);
                DateFormat formatter = new SimpleDateFormat(outFormat, Locale.getDefault());
                result = formatter.format(date.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getValidDateFormat(String dbFormateString) {
        final String outFormat = "yyyy-MM-dd hh:mm:ss";
        final String InFORMAT = "dd/MM/yyyy";
        String result = "";
        try {
            if (dbFormateString != null && !dbFormateString.isEmpty() && dbFormateString != "") {
                Date date = new SimpleDateFormat(InFORMAT, Locale.ENGLISH).parse(dbFormateString);
                DateFormat formatter = new SimpleDateFormat(outFormat, Locale.getDefault());
                result = formatter.format(date.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getDefaultSalesSms(String businessName, String billNo
            , String amount) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("GREETINGS FROM.." + "\n");
        stringBuilder.append(getFinalBusinessName(businessName) + "\n");
        stringBuilder.append("\n");
        stringBuilder.append("DATE: " + getChangeDateFormatAllExp(getEntryDate()) + "\n");
        stringBuilder.append("BILL NO: " + billNo + "\n");
        stringBuilder.append("AMOUNT: " + amount + "\n");
        stringBuilder.append("\n");
//        stringBuilder.append("bill: " + url + "\n");
//        stringBuilder.append("Powered by fTouch");

        return stringBuilder.toString();
    }

    public static String getDefaultQuotationSms(String businessName, String billNo
            , String amount
    ) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("GREETINGS FROM.." + "\n");
        stringBuilder.append(getFinalBusinessName(businessName) + "\n");
        stringBuilder.append("\n");
        stringBuilder.append("DATE: " + getChangeDateFormatAllExp(getEntryDate()) + "\n");
        stringBuilder.append("QUOTATION: " + billNo + "\n");
        stringBuilder.append("AMOUNT: " + amount + "\n");
        stringBuilder.append("\n");
//        stringBuilder.append("bill: " + url + "\n");
//        stringBuilder.append("Powered by fTouch");

        return stringBuilder.toString();
    }


    public static String getMonthYear(String _monthYear) {

//        final String DATE_DASH_FORMAT = "MM";

        final String DATE_DASH_FORMAT = "MMM yyyy";

//        final String DATE_FORMAT = "dd-MM-yyyy";
        final String DATE_FORMAT = "yyyy-MM-dd";
        try {
            Date date = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).parse(_monthYear);
            DateFormat formatter = new SimpleDateFormat(DATE_DASH_FORMAT, Locale.getDefault());
            _monthYear = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return _monthYear;
    }


    public static String getChangeDateFormatAllExp(String _dateFormat) {
        final String DATE_DASH_FORMAT = "dd MMM";
//        final String DATE_FORMAT = "dd-MM-yyyy";
        final String DATE_FORMAT = "yyyy-MM-dd";
        try {
            Date date = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).parse(_dateFormat);
            DateFormat formatter = new SimpleDateFormat(DATE_DASH_FORMAT, Locale.getDefault());
            _dateFormat = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return _dateFormat;
    }


    public static String getFilterDate(String _dateFormat) {
        final String DATE_DASH_FORMAT = "yyyy-MM-dd";

//        final String DATE_FORMAT = "dd-MM-yyyy";
        final String DATE_FORMAT = "dd MMM";
        try {
            Date date = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).parse(_dateFormat);
            DateFormat formatter = new SimpleDateFormat(DATE_DASH_FORMAT, Locale.getDefault());
            _dateFormat = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return _dateFormat;
    }


    public static String getMonthIndex(int MonthIndex)//0118//0119 OR JAN18,
    {

        String month = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String res = "";
        res = String.valueOf(month.charAt(MonthIndex));
        return res;
    }

    public static String getMonthYearPurchase(String _monthYear) {


        final String DATE_DASH_FORMAT = "MMM yyyy";

        final String DATE_FORMAT = "yyyy-MM-dd";
        try {
            Date date = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).parse(_monthYear);
            DateFormat formatter = new SimpleDateFormat(DATE_DASH_FORMAT, Locale.getDefault());
            _monthYear = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return _monthYear;
    }


    public static String getMonthYearDigit(String _monthYear) {


        final String DATE_DASH_FORMAT = "MM yyyy";

        final String DATE_FORMAT = "MMM yyyy";
        try {
            Date date = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).parse(_monthYear);
            DateFormat formatter = new SimpleDateFormat(DATE_DASH_FORMAT, Locale.getDefault());
            _monthYear = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return _monthYear;
    }


    public static String getPurchaseDateDDMMM(String _dateFormat) {
        final String OUT = "dd MMM";
//        final String DATE_FORMAT = "dd-MM-yyyy";
        final String IN = "yyyy-MM-dd";
        try {
            Date date = new SimpleDateFormat(IN, Locale.ENGLISH).parse(_dateFormat);
            DateFormat formatter = new SimpleDateFormat(OUT, Locale.getDefault());
            _dateFormat = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return _dateFormat;
    }


    public static String getDDMMYYYY(String _dateFormat) {
        final String OUT = "dd/MM/yyyy";
//        final String DATE_FORMAT = "dd-MM-yyyy";
        final String IN = "yyyy-MM-dd";
        try {
            Date date = new SimpleDateFormat(IN, Locale.ENGLISH).parse(_dateFormat);
            DateFormat formatter = new SimpleDateFormat(OUT, Locale.getDefault());
            _dateFormat = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return _dateFormat;
    }

    public static String getDayMonth(String _monthYear) {
        final String OUT = "MMM yyyy";
        final String IN = "dd/MM/yyyy";
        try {
            Date date = new SimpleDateFormat(IN, Locale.ENGLISH).parse(_monthYear);
            DateFormat formatter = new SimpleDateFormat(OUT, Locale.getDefault());
            _monthYear = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return _monthYear;
    }

    public static String getDayMonthForPayment(String _monthYear) {
        final String OUT = "MMM yyyy";
        final String IN = "dd/MM/yyyy hh:mm aa";
        try {
            Date date = new SimpleDateFormat(IN, Locale.ENGLISH).parse(_monthYear);
            DateFormat formatter = new SimpleDateFormat(OUT, Locale.getDefault());
            _monthYear = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return _monthYear;
    }

    public static String getFirstDay(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date dddd = calendar.getTime();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        return sdf1.format(dddd);
    }

    public static String getLastDay(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date dddd = calendar.getTime();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        return sdf1.format(dddd);
    }


    public static String getFirstDateOfMonth(String _monthYear) {
        final String OUT = "dd/MM/yyyy";
        final String IN = "MMM yyyy";
        try {
            Date date = new SimpleDateFormat(IN, Locale.ENGLISH).parse(_monthYear);
            DateFormat formatter = new SimpleDateFormat(OUT, Locale.getDefault());
            _monthYear = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return _monthYear;
    }


    public static ClsDateTime getMonth(int addmonth) {

        ClsDateTime obj = new ClsDateTime();
        String FirstDate = "";

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, addmonth);


        FirstDate = new SimpleDateFormat("MM/yyyy").format(cal.getTime());
        FirstDate = "01/".concat(FirstDate);

        String LastDate = new SimpleDateFormat("MM/yyyy").format(cal.getTime());
        int lastDate = cal.getActualMaximum(Calendar.DATE);
        LastDate = lastDate + "/" + (LastDate);

        String Year = new SimpleDateFormat("yyyy").format(cal.getTime());

        String shortYear = new SimpleDateFormat("yy").format(cal.getTime());

        Formatter fmt = new Formatter();
        fmt = new Formatter();
        fmt.format("%tb", cal);
        Log.e("month", String.valueOf(fmt));


        obj.setFirstDate(FirstDate);
        obj.setLastDate(LastDate);
        obj.setMonthName(String.valueOf(fmt));
        obj.setYear(Integer.parseInt(Year));
        obj.setShortYear(Integer.parseInt(shortYear));

        Log.e("Globaldate", "Result--->>>>" + FirstDate);
        Log.e("Globaldate", "LastDate--->>>>" + LastDate);
        Log.e("Globaldate", "Year--->>>>" + Year);
        Log.e("Globaldate", "Month--->>>>" + fmt);
        Log.e("Globaldate", "Month--->>>>" + shortYear);

        return obj;

    }

    public static String getDeleteSmsMessage(ClsInventoryOrderMaster clsInventoryOrderMaster
            , ClsQuotationMaster clsQuotationMaster, String mode) {
        StringBuilder stringBuilder = new StringBuilder();
        if (mode.equalsIgnoreCase("Sales")) {
            stringBuilder.append("Bill Deleted!!! \n");
            stringBuilder.append("Bill No:" + clsInventoryOrderMaster.getOrderNo() + "\n");
            if (clsInventoryOrderMaster.getMobileNo().equalsIgnoreCase("")) {
                stringBuilder.append("Mobile:" + "- \n");
            } else {
                stringBuilder.append("Mobile:" + clsInventoryOrderMaster.getMobileNo() + "\n");
            }

            if (!clsInventoryOrderMaster.getCustomerName().equalsIgnoreCase("")) {
                stringBuilder.append("Customer:" + clsInventoryOrderMaster.getCustomerName() + "\n");
            }
            stringBuilder.append("Amount:" + clsInventoryOrderMaster.getTotalAmount() + "\n");
            stringBuilder.append(getSmsDateFormat(clsInventoryOrderMaster.getBillDate()) + "\n\n");

        } else if (mode.equalsIgnoreCase("Quotation")) {
            stringBuilder.append("Quotation Deleted!!! \n");
            stringBuilder.append("Quotation No:" + clsQuotationMaster.getQuotationNo() + "\n");

            if (clsQuotationMaster.getMobileNo().equalsIgnoreCase("")) {
                stringBuilder.append("Mobile:- \n");
            } else {
                stringBuilder.append("Mobile:" + clsQuotationMaster.getMobileNo() + "\n");
            }

            if (!clsQuotationMaster.getCustomerName().equalsIgnoreCase("")) {
                stringBuilder.append("Customer:" + clsQuotationMaster.getCustomerName() + "\n");
            }
            stringBuilder.append("Quotation Amount:" + clsQuotationMaster.getTotalAmount() + "\n");
            stringBuilder.append(getSmsDateFormat(clsQuotationMaster.getQuotationDate()) + "\n\n");
        }

        stringBuilder.append("Powered by fTouch");


        return stringBuilder.toString();
    }


    public static String getChangeDateFormat(String _dateFormat) {

        final String DATE_DASH_FORMAT = "dd MMM";
//        final String DATE_FORMAT = "dd-MM-yyyy";
        final String DATE_FORMAT = "yyyy-MM-dd";


        try {
            Date date = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).parse(_dateFormat);
            DateFormat formatter = new SimpleDateFormat(DATE_DASH_FORMAT, Locale.getDefault());
            _dateFormat = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return _dateFormat;
    }

    public static String getDbDateFormat(String dateStr) {
        final String DATE_DASH_FORMAT = "yyyy-MM-dd";
        final String DATE_FORMAT = "dd/MM/yyyy";
        try {
            if (dateStr != null && !dateStr.isEmpty() && dateStr != "") {
                Date date = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).parse(dateStr);
                DateFormat formatter = new SimpleDateFormat(DATE_DASH_FORMAT, Locale.getDefault());
                dateStr = formatter.format(date.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateStr;
    }

    public static String getDDMYYYYAndTimeAM_And_PMFormat(String dbFormateString) {
        final String outFormat = "dd/MM/yyyy hh:mm aa";
        final String InFORMAT = "yyyy-MM-dd hh:mm:ss";
        String result = "";
        try {
            if (dbFormateString != null && !dbFormateString.isEmpty() && dbFormateString != "") {
                Date date = new SimpleDateFormat(InFORMAT, Locale.ENGLISH).parse(dbFormateString);
                DateFormat formatter = new SimpleDateFormat(outFormat, Locale.getDefault());
                result = formatter.format(date.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String formatNumber(int decimals, double number) {
        StringBuilder sb = new StringBuilder(decimals + 2);
        sb.append("#.");
        for (int i = 0; i < decimals; i++) {
            sb.append("0");
        }
        return new DecimalFormat(sb.toString()).format(number);
    }

    /* public static double round(double value, int places) {
         if (places < 0) throw new IllegalArgumentException();

         long factor = (long) Math.pow(10, places);
         value = value * factor;
         long tmp = Math.round(value);
         return (double) tmp / factor;
     }
 */
    public static String round(double input, int places) {

        String strDouble = String.format("%." + places + "f", input);
        return strDouble;

    }

    public static void setDynamicHeight(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        //check adapter if null
        if (adapter == null) {
            return;
        }
        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.height = height + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(layoutParams);
        listView.requestLayout();
    }


    public static void openSnackBarExcelFile(Activity context, String _FilePath, String _title) {
        // Create the Snackbar
        LinearLayout.LayoutParams objLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        Snackbar snackbar = Snackbar.make(context.findViewById(R.id.coordinatorLayout),
                "Open file.",
                Snackbar.LENGTH_LONG);


        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.setPadding(0, 0, 0, 0);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View snackView = context.getLayoutInflater().inflate(R.layout.my_snackbar, null);
        TextView txt_open = snackView.findViewById(R.id.txt_open);

        txt_open.setOnClickListener(v -> {
            File _fileToSave = new File(_FilePath);
            Uri outputUri = FileProvider.getUriForFile(context, AUTHORITY, _fileToSave);

            if (_fileToSave.exists()) {

                Intent viewFile = new Intent(Intent.ACTION_VIEW);
                viewFile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                viewFile.setDataAndType(outputUri, "application/vnd.ms-excel");
                viewFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    context.startActivity(viewFile);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "No Application available to viewExcel", Toast.LENGTH_SHORT).show();
                }


            } else {
                Toast.makeText(context, "File does not exists", Toast.LENGTH_SHORT).show();
            }
        });

        TextView txt_share = snackView.findViewById(R.id.txt_share);
        txt_share.setOnClickListener(v -> {

            Log.d("--uri--", "txt_share: " + _FilePath);

//            ClsGlobal.shareExcelFile(context, _FilePath, "Customer Report");
            ClsGlobal.shareExcelFile(context, _FilePath, _title);

        });

        layout.addView(snackView, objLayoutParams);
        snackbar.show();
    }


    public static String exportCustomerPayment(Context context) {

        StringBuilder stringBuilder = new StringBuilder();
        String result = "";
        try {
            InputStream json = context.getAssets().open("ExportToExcelCustomerLedgerQry.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(json));
            String strr = "";

            while ((strr = in.readLine()) != null) {
                stringBuilder.append(strr);
            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        result = stringBuilder.toString();
        return result;
    }

    public static String exportVendorPayment(Context context) {

        StringBuilder stringBuilder = new StringBuilder();
        String result = "";

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.getAssets().open("ExportToExcelVendorLedgerQry.txt")))) {
            String strr = "";

            while ((strr = reader.readLine()) != null) {
                stringBuilder.append(strr);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        result = stringBuilder.toString();
        return result;
    }


    private static void InsertEmailLogs(String message, Context context) {
        Log.e("insert", message);
        ClsEmailLogs insertEmailLogs = new ClsEmailLogs(context);
        insertEmailLogs.setMESSAGE(message);
        insertEmailLogs.setDATE_TIME(getEntryDateFormat(getCurruntDateTime()));

        int result = ClsEmailLogs.Insert(insertEmailLogs);
        Log.e("result", String.valueOf(result));
    }

    public static boolean CheckInternetConnection(Context context) {

        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static String getStringFile(File f) {
        InputStream inputStream = null;
        String encodedFile = "", lastVal;
        try {
            inputStream = new FileInputStream(f.getAbsolutePath());

            byte[] buffer = new byte[(int) f.length()];//specify the size to allow
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }

            output64.close();


            encodedFile = output.toString();

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        lastVal = encodedFile;
        return lastVal;
    }


    public static void setSignatoryName(String signatoryName, Context c) {

        String CustomerSignatory = "CustomerSignatory";

        String MyPREFERENCES = "LoginDetails";
        SharedPreferences mPreferences = c.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();

        editor.putString(CustomerSignatory, signatoryName);
        editor.apply();
        editor.commit();
    }


    public static void setDecimalNumber(String _DecimalNumber, Context c) {

        String DecimalNumber = "DecimalNumber";

        String MyPREFERENCES = "LoginDetails";
        SharedPreferences mPreferences = c.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();

        editor.putString(DecimalNumber, DecimalNumber);
        editor.apply();
        editor.commit();
    }


    public static void setBillTitle(String _billTitle, Context c) {

        String billTitle = "billTitle";

        String MyPREFERENCES = "LoginDetails";
        SharedPreferences mPreferences = c.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();

        editor.putString(billTitle, _billTitle);
        editor.apply();
        editor.commit();
    }

    public static String getBytes(File filePath) {

        String result = "";

        Log.e("ClsGobal", "getBytes-- " + filePath.getAbsolutePath());

        byte[] byteArray = null;
        try {
            InputStream inputStream = new FileInputStream(filePath);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024 * 8];
            int bytesRead = 0;

            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();
            result = Base64.encodeToString(byteArray, 0);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }

    public static void saveLoginStatus(String status) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.Login_status_key), status);
        editor.apply();
    }

    public static String getLoginStatus() {
        String getStatus = mSharedPreferences.getString(context.getResources()
                .getString(R.string.Login_status_key), "");
        Log.i("login", String.valueOf(getStatus));
        return getStatus;
    }


    public static void setUserInfo(ClsUserInfo objClsUserInfo, Context c) {

        String MyPREFERENCES = "LoginDetails";
        SharedPreferences mPreferences = c.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();

        String UserID = "userID";
        String remaindays = "remaindays";
        String expiredate = "expiredate";
        String contactpersonname = "contactpersonname";
        String LoginStatus = "LoginStatus";
        String MobileNo = "MobileNo";
        String businessname = "businessname";
        String businessaddress = "businessaddress";
        String registeredmobilenumber = "registeredmobilenumber";
        String emailaddress = "emailaddress";
        String state = "state";
        String city = "city";
        String pincode = "pincode";
        String cinnumber = "cinnumber";
        String gstnumber = "gstnumber";
//        String CustomerSignatory = "CustomerSignatory";
        String LicenseType = "LicenseType";


        editor.putString(UserID, objClsUserInfo.getUserId());
        editor.putString(remaindays, objClsUserInfo.getRemainDays());
        editor.putString(expiredate, objClsUserInfo.getExpiredDays());
        editor.putString(contactpersonname, objClsUserInfo.getContactPersonName());
        editor.putString(LoginStatus, objClsUserInfo.getLoginStatus());
        editor.putString(MobileNo, objClsUserInfo.getMobileNo());
        editor.putString(businessname, objClsUserInfo.getBusinessname());
        editor.putString(businessaddress, objClsUserInfo.getBusinessaddress());
        editor.putString(registeredmobilenumber, objClsUserInfo.getRegisteredmobilenumber());
        editor.putString(emailaddress, objClsUserInfo.getEmailaddress());
        editor.putString(state, objClsUserInfo.getState());
        editor.putString(city, objClsUserInfo.getCity());
        editor.putString(pincode, objClsUserInfo.getPincode());
        editor.putString(cinnumber, objClsUserInfo.getCinnumber());
        editor.putString(gstnumber, objClsUserInfo.getGstnumber());

        editor.putString(LicenseType, objClsUserInfo.getLicenseType());


        editor.apply();
        editor.commit();

    }


    public static void setEmailConfiguration(ClsEmailConfiguration objClsEmailConfiguration, Context c) {

        String MyPREFERENCES = "EmailConfiguration";
        SharedPreferences mPreferences = c.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();

        String FromEmailId = "FromEmailId";
        String Password = "Password";
        String SMTP = "SMTP";
        String Display_Name = "Display_Name";
        String SSl = "SSl";
        String Port = "Port";
        String Active = "Active";
        String EmailConfiguration = "EmailConfiguration";

        editor.putString(FromEmailId, objClsEmailConfiguration.getFromEmailId());
        editor.putString(Password, objClsEmailConfiguration.getPassword());
        editor.putString(SMTP, objClsEmailConfiguration.getSMTP());
        editor.putString(Display_Name, objClsEmailConfiguration.getDisplay_Name());
        editor.putString(SSl, objClsEmailConfiguration.getSSl());
        editor.putString(Port, objClsEmailConfiguration.getPort());
        editor.putString(Active, objClsEmailConfiguration.getActive());
        editor.putString(EmailConfiguration, objClsEmailConfiguration.getEmailConfiguration());

        editor.apply();
        editor.commit();

        Gson gson = new Gson();
        String jsonInString = gson.toJson(objClsEmailConfiguration);
        Log.e("Result", "objClsUserInfo---" + jsonInString);
    }

    public static ClsEmailConfiguration getEmailConfiguration(Context c) {
        ClsEmailConfiguration objEmailConfiguration = new ClsEmailConfiguration();

        String MyPREFERENCES = "EmailConfiguration";
        SharedPreferences mPreferences = c.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();

        String FromEmailId = "FromEmailId";
        String Password = "Password";
        String SMTP = "SMTP";
        String Display_Name = "Display_Name";
        String SSl = "SSl";
        String Port = "Port";
        String Active = "Active";
        String EmailConfiguration = "EmailConfiguration";

        if (mPreferences.getString(FromEmailId, null) == null) {
            Log.e("EmailConfiguration", "First Time");
            editor.putString(FromEmailId, "");
            editor.putString(Password, "");
            editor.putString(SMTP, "");
            editor.putString(Display_Name, "");
            editor.putString(SSl, "");
            editor.putString(Port, "");
            editor.putString(Active, "");
            editor.putString(EmailConfiguration, "");

            editor.apply();
            editor.commit();
        }

        objEmailConfiguration.setFromEmailId(mPreferences.getString(FromEmailId, objEmailConfiguration.getFromEmailId()));
        objEmailConfiguration.setPassword(mPreferences.getString(Password, objEmailConfiguration.getPassword()));
        objEmailConfiguration.setSMTP(mPreferences.getString(SMTP, objEmailConfiguration.getSMTP()));
        objEmailConfiguration.setDisplay_Name(mPreferences.getString(Display_Name, objEmailConfiguration.getDisplay_Name()));
        objEmailConfiguration.setSSl(mPreferences.getString(SSl, objEmailConfiguration.getSSl()));
        objEmailConfiguration.setPort(mPreferences.getString(Port, objEmailConfiguration.getPort()));
        objEmailConfiguration.setActive(mPreferences.getString(Active, objEmailConfiguration.getActive()));
        objEmailConfiguration.setEmailConfiguration(mPreferences.getString(EmailConfiguration, objEmailConfiguration.getEmailConfiguration()));


        Gson gson = new Gson();
        String jsonInString = gson.toJson(objEmailConfiguration);
        Log.e("Result", "getobjClsUserInfo---" + jsonInString);

        return objEmailConfiguration;
    }

    public static void setBasicUserInfo(ClsUserInfo objClsUserInfo, Context c) {

        String MyPREFERENCES = "LoginDetails";
        SharedPreferences mPreferences = c.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();

        String LoginStatus = "LoginStatus";
        String MobileNo = "MobileNo";

        editor.putString(LoginStatus, objClsUserInfo.getLoginStatus());
        editor.putString(MobileNo, objClsUserInfo.getMobileNo());
        editor.apply();
        editor.commit();
    }


    public static void autoLogout(Context c) {
        String MyPREFERENCES = "LoginDetails";
        SharedPreferences mPreferences = c.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();

        String LoginStatus = "LoginStatus";

        editor.putString(LoginStatus, "DEACTIVE");

        editor.apply();
        editor.commit();
    }


    public static ClsItemDefaultTaxUpdate setUpdateItemDefaultTax(ClsItemDefaultTaxUpdate objUpdate, Context c) {

        String MyPREFERENCES = "UpdateItemDefaultTax";
        SharedPreferences mPreferences = c.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();

        String updateItemDefaultTax = "updateItemDefaultTax";

        editor.putString(updateItemDefaultTax, objUpdate.getUpdateItemDefaultTax());

        editor.apply();
        editor.commit();
        Gson gson = new Gson();
        String jsonInString = gson.toJson(objUpdate);
        Log.e("--obj--", "obj-- " + jsonInString);

        return objUpdate;
    }


    public static ClsItemDefaultTaxUpdate getUpdateItemDefaultTax(Context c) {

        ClsItemDefaultTaxUpdate obj = new ClsItemDefaultTaxUpdate();
        String MyPREFERENCES = "UpdateItemDefaultTax";
        SharedPreferences mPreferences = c.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();

        String updateItemDefaultTax = "updateItemDefaultTax";

        if (mPreferences.getString(updateItemDefaultTax, null) == null) {

            Log.e("--getUserInfo--", "GET_CALL");

            editor.putString(updateItemDefaultTax, "");

            editor.apply();
            editor.commit();
        }

        obj.setUpdateItemDefaultTax(mPreferences.getString(updateItemDefaultTax, obj.getUpdateItemDefaultTax()));

        Gson gson = new Gson();
        String jsonInString = gson.toJson(obj);
        Log.e("--getUserInfo--", "obj-- " + jsonInString);

        return obj;
    }


    public static ClsVendorLedgerUpdate setUpdateVendorLedger(ClsVendorLedgerUpdate objUpdate, Context c) {

        String MyPREFERENCES = "UpdateVendor";
        SharedPreferences mPreferences = c.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();

        String updateVendorLedger = "updateVendorLedger";

        editor.putString(updateVendorLedger, objUpdate.getUpdateVendorLedger());

        editor.apply();
        editor.commit();
        Gson gson = new Gson();
        String jsonInString = gson.toJson(objUpdate);
        Log.e("--obj--", "obj-- " + jsonInString);

        return objUpdate;
    }

    public static ClsVendorLedgerUpdate getUpdateVendorLedger(Context c) {

        ClsVendorLedgerUpdate obj = new ClsVendorLedgerUpdate();
        String MyPREFERENCES = "UpdateVendor";
        SharedPreferences mPreferences = c.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();

        String updateVendorLedger = "updateVendorLedger";

        if (mPreferences.getString(updateVendorLedger, null) == null) {

            Log.e("--getUserInfo--", "GET_CALL");

            editor.putString(updateVendorLedger, "");

            editor.apply();
            editor.commit();
        }

        obj.setUpdateVendorLedger(mPreferences.getString(updateVendorLedger, obj.getUpdateVendorLedger()));

        Gson gson = new Gson();
        String jsonInString = gson.toJson(obj);
        Log.e("--getUserInfo--", "obj-- " + jsonInString);

        return obj;
    }

    public static String get_FAQ_language(Context context) {
        SharedPreferences mPreferences = context.getSharedPreferences(Language_Settings, MODE_PRIVATE);
        return mPreferences.getString(FAQ_Language_Key, null);
    }

    public static void Save_FAQ_language(Context context, String language) {
        Log.e("Save_FAQ_language", language);
        SharedPreferences mPreferences = context.getSharedPreferences(Language_Settings, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(FAQ_Language_Key, language);
        editor.apply();
    }

    public static ClsUserInfo getUserInfo(Context c) {
        ClsUserInfo objClsUserInfo = new ClsUserInfo();

        String MyPREFERENCES = "LoginDetails";
        SharedPreferences mPreferences = c.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        String UserID = "userID";
        String remaindays = "remaindays";
        String expiredate = "expiredate";
        String contactpersonname = "contactpersonname";
        String LoginStatus = "LoginStatus";
        String MobileNo = "MobileNo";
        String businessname = "businessname";
        String businessaddress = "businessaddress";
        String registeredmobilenumber = "registeredmobilenumber";
        String emailaddress = "emailaddress";
        String state = "state";
        String city = "city";
        String pincode = "pincode";
        String cinnumber = "cinnumber";
        String gstnumber = "gstnumber";

        String CustomerSignatory = "CustomerSignatory";
        String billTitle = "billTitle";
        String DecimalNumber = "DecimalNumber";
        String LicenseType = "LicenseType";


        objClsUserInfo.setUserId(mPreferences.getString(UserID, objClsUserInfo.getUserId()));
        objClsUserInfo.setRemainDays(mPreferences.getString(remaindays, objClsUserInfo.getRemainDays()));
        objClsUserInfo.setExpiredDays(mPreferences.getString(expiredate, objClsUserInfo.getExpiredDays()));
        objClsUserInfo.setContactPersonName(mPreferences.getString(contactpersonname, objClsUserInfo.getContactPersonName()));
        objClsUserInfo.setLoginStatus(mPreferences.getString(LoginStatus, objClsUserInfo.getLoginStatus()));
        objClsUserInfo.setMobileNo(mPreferences.getString(MobileNo, objClsUserInfo.getMobileNo()));
        objClsUserInfo.setBusinessname(mPreferences.getString(businessname, objClsUserInfo.getBusinessname()));
        objClsUserInfo.setBusinessaddress(mPreferences.getString(businessaddress, objClsUserInfo.getBusinessaddress()));
        objClsUserInfo.setRegisteredmobilenumber(mPreferences.getString(registeredmobilenumber, objClsUserInfo.getRegisteredmobilenumber()));
        objClsUserInfo.setEmailaddress(mPreferences.getString(emailaddress, objClsUserInfo.getEmailaddress()));
        objClsUserInfo.setState(mPreferences.getString(state, objClsUserInfo.getState()));
        objClsUserInfo.setCity(mPreferences.getString(city, objClsUserInfo.getCity()));
        objClsUserInfo.setPincode(mPreferences.getString(pincode, objClsUserInfo.getPincode()));
        objClsUserInfo.setCinnumber(mPreferences.getString(cinnumber, objClsUserInfo.getCinnumber()));
        objClsUserInfo.setGstnumber(mPreferences.getString(gstnumber, objClsUserInfo.getGstnumber()));


        objClsUserInfo.setGetDecimalNumber(mPreferences.getString(DecimalNumber, objClsUserInfo.getGetDecimalNumber()));
        objClsUserInfo.setCustomerSignatory(mPreferences.getString(CustomerSignatory, objClsUserInfo.getCustomerSignatory()));
        objClsUserInfo.setBillTitle(mPreferences.getString(billTitle, objClsUserInfo.getBillTitle()));
        objClsUserInfo.setLicenseType(mPreferences.getString(LicenseType, objClsUserInfo.getLicenseType()));

        Gson gson = new Gson();
        String jsonInString = gson.toJson(objClsUserInfo);
        Log.e("Result", "getobjClsUserInfo---" + jsonInString);

        return objClsUserInfo;
    }


    public static void sendNotification(String title, String message, String mode, Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //If on Oreo then notification required a notification channel.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Default",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "default")
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setSmallIcon(R.mipmap.ic_launch_icon_new);

        if (mode.equalsIgnoreCase("Send PDF")) {
            notification.setOngoing(true);
        }

        notificationManager.notify(1, notification.build());
    }

    public static void set_current_Day_Remainder(Context context) {
        SharedPreferences mPreferences = context.getSharedPreferences(mPreferncesName, MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt("Day", Integer.parseInt(get_Current_Day_Month_Yearly("Daily")));
        editor.apply();

    }

    public static void CancelAllNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }


    public static boolean whatsappInstalledOrNot(String uri, Context context) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public static void SendPdfWhatsApp(Context context, String getPath) {
        Uri pdfUri;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            pdfUri = FileProvider.getUriForFile(context,
                    AUTHORITY,
                    new File(getPath));
            Log.e("pdfUri ", String.valueOf(pdfUri));
        } else {
            pdfUri = Uri.fromFile(new File(getPath));
            Log.e("pdfUri ", String.valueOf(pdfUri));
        }


        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, pdfUri);
        context.startActivity(Intent.createChooser(share, "Share"));

    }


    public static void SendPdf(Context context, String getPath, String billNo) {
        Uri pdfUri;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            pdfUri = FileProvider.getUriForFile(context,
                    AUTHORITY,
                    new File(getPath));
            Log.e("pdfUri ", String.valueOf(pdfUri));
        } else {
            pdfUri = Uri.fromFile(new File(getPath));
            Log.e("pdfUri ", String.valueOf(pdfUri));
        }


        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_SUBJECT, "Bill No: " + billNo);
        share.putExtra(Intent.EXTRA_STREAM, pdfUri);
        context.startActivity(Intent.createChooser(share, "Share"));

    }


    public static void shareExcelFile(Context context, String getPath, String title) {
        Uri pdfUri;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            pdfUri = FileProvider.getUriForFile(context,
                    AUTHORITY,
                    new File(getPath));
            Log.e("pdfUri ", String.valueOf(pdfUri));
        } else {
            pdfUri = Uri.fromFile(new File(getPath));
            Log.e("pdfUri ", String.valueOf(pdfUri));
        }


        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/vnd.ms-excel");
        share.putExtra(Intent.EXTRA_SUBJECT, title);
        share.putExtra(Intent.EXTRA_STREAM, pdfUri);
        context.startActivity(Intent.createChooser(share, "Share"));

    }

    public static void SendPdfWhatsAppNew(Context context, String phone, String getPath) {
        Uri pdfUri;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            pdfUri = FileProvider.getUriForFile(context,
                    AUTHORITY,
                    new File(getPath));
            Log.e("pdfUri ", String.valueOf(pdfUri));
        } else {
            pdfUri = Uri.fromFile(new File(getPath));
            Log.e("pdfUri ", String.valueOf(pdfUri));
        }


        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, pdfUri);
        share.putExtra("phone", phone);
        share.setPackage("com.whatsapp");

        context.startActivity(share);
    }


    public static String getSignaturePath() {

        File _saveLocation = Environment.getExternalStorageDirectory();
        Log.v("--mGetSign--", "step-3 " + _saveLocation);


        File dir = new File(_saveLocation.getAbsolutePath() + "/" + ClsGlobal.AppFolderName + "/Signature/");
        Log.v("--mGetSign--", "step-4 " + dir);
        if (!dir.exists()) {
            Log.v("--mGetSign--", "step-5");
            dir.mkdirs();
            Log.v("--mGetSign--", "step-6");
        }
        Log.v("--mGetSign--", "step-7");

        return dir.getAbsolutePath() + "/Signature" + ".png";

    }

    public static String getLogoPath() {

        File _saveLocation = Environment.getExternalStorageDirectory();
        Log.v("--mGetSign--", "step-3 " + _saveLocation);

        File dir = new File(_saveLocation.getAbsolutePath() + "/" + ClsGlobal.AppFolderName + "/Logo/");
        Log.v("--mGetSign--", "step-4 " + dir);
        if (!dir.exists()) {
            Log.v("--mGetSign--", "step-5");
            dir.mkdirs();
            Log.v("--mGetSign--", "step-6");
        }
        Log.v("--mGetSign--", "step-7");
        return dir.getAbsolutePath() + "/Logo" + ".png";
    }


    public static String getLogo() {
        String result = "";

        File _saveLocation = Environment.getExternalStorageDirectory();
        File dir = new File(_saveLocation.getAbsolutePath() + "/"
                + ClsGlobal.AppFolderName + "/Logo/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String StoredPath = dir.getAbsolutePath() + "/Logo" + ".png";
        File img = new File(StoredPath);
        if (img.exists()) {
            result = StoredPath;
        }
        return result;
    }


    static String getSignature() {
        String result = "";

        File _saveLocation = Environment.getExternalStorageDirectory();
        File dir = new File(_saveLocation.getAbsolutePath() + "/" +
                ClsGlobal.AppFolderName + "/Signature/");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String StoredPath = dir.getAbsolutePath() + "/Signature" + ".png";
        File img = new File(StoredPath);
        if (img.exists()) {
            result = StoredPath;
        }
        return result;

    }


    public static String generateErrorFile(String fileName, String error, Context context) {

        StringBuilder stringBuilderErr = new StringBuilder();

        //generate database backup file

        try {
            ClsUserInfo objUserInfo = new ClsUserInfo();
            objUserInfo = ClsGlobal.getUserInfo(context);

            stringBuilderErr.append("App Name:".concat(AppName));
            stringBuilderErr.append("\n");
            stringBuilderErr.append(" App Version:".concat(getApplicationVersion(context)));
            stringBuilderErr.append("\n");
            stringBuilderErr.append(" File Name:".concat(fileName));
            stringBuilderErr.append("\n");
            stringBuilderErr.append(" userID:".concat(objUserInfo.getUserId()));
            stringBuilderErr.append("\n");
            stringBuilderErr.append(" Contact Person Name:".concat(objUserInfo.getContactPersonName()));
            stringBuilderErr.append("\n");
            stringBuilderErr.append(" MobileNo:".concat(objUserInfo.getMobileNo()));
            stringBuilderErr.append("\n");
            stringBuilderErr.append(" Device Info:".concat(getDeviceInfo(context)));
            stringBuilderErr.append("\n");
            stringBuilderErr.append(" Error:".concat(error));


            File _saveLocation = Environment.getExternalStorageDirectory();
            Log.d("camera", "filepath:- " + _saveLocation);
            File root = new File(_saveLocation.getAbsolutePath() + "/" + ClsGlobal.AppFolderName + "/Log/");
            Log.d("generateBackupFile", "dir:- " + root);
            if (!root.exists()) {
                root.mkdirs();
            }

            String _fileName = "".concat(getEntryDate()).concat(" " + getRandomCharacters()).concat(".txt");
            File gpxfile = new File(root, _fileName);

            FileWriter writer = new FileWriter(gpxfile);
            writer.append(stringBuilderErr.toString());
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(context, "Exception", Toast.LENGTH_SHORT).show();
        }
        return stringBuilderErr.toString();
    }


    public static String getRandomCharacters() {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = upper.toLowerCase(Locale.ROOT);
        String digits = "0123456789";
        String CHAR_LIST = upper + lower + digits;
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * CHAR_LIST.length());
            salt.append(CHAR_LIST.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public static void sendErrorFileToServer(Context context) {

        Log.d("sendErrorFileToServer", "step1- ");

        File _saveLocation = Environment.getExternalStorageDirectory();
        Log.d("camera", "filepath:- " + _saveLocation);
        Log.d("sendErrorFileToServer", "step2- ");

        File root = new File(_saveLocation.getAbsolutePath() +
                "/" + ClsGlobal.AppFolderName + "/Log/");
        Log.d("generateBackupFile", "dir:- " + root);
        Log.d("sendErrorFileToServer", "step3- ");

        if (!root.exists()) {
            root.mkdirs();
        }
        Log.d("sendErrorFileToServer", "step4- ");

        try {
            if (root.listFiles() != null) {
                Log.d("sendErrorFileToServer", "step5- ");
                for (File f : root.listFiles()) {
                    Log.d("sendErrorFileToServer", "step6- ");
                    if (f.isFile()) {
                        Log.d("sendErrorFileToServer", "step7- ");
                        String name = f.getName();
                        Log.d("sendErrorFileToServer", "step8- ");
                        Log.d("sendErrorFileToServer", "step8- " + name);

                        int lasIndex = name.lastIndexOf(".");
                        Log.d("sendErrorFileToServer", "stepInx- " + lasIndex);

                        if (name.lastIndexOf(".") > 0) {
                            String extension = name.substring(name.lastIndexOf("."));

                            Log.d("sendErrorFileToServer", "step9- ");
                            Log.d("camera", "name:- " + name);
                            if (extension != null && extension != "" && extension.equalsIgnoreCase(".txt")) {
                                Log.d("sendErrorFileToServer", "step10- ");
                                String byteArray = ClsGlobal.getBytes(f);
                                Log.d("sendErrorFileToServer", "step11- ");
                                Log.d("sendErrorFileToServer", "step11- " + byteArray);

                                name = stripExtension(name);
                                Log.d("sendErrorFileToServer", "step11- " + name);

                                extension = f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("."));
                                Log.d("sendErrorFileToServer", "step12- " + extension);

                                uploadErrorFile(byteArray, extension, name, context, f);//upload & delete

                                Log.d("sendErrorFileToServer", "step12- ");
                            }
                        } else f.delete();
                    }
                }
            }

            File file = new File(_saveLocation.getAbsolutePath()
                    + "/" + ftouchLogs_Folder + "/" +
                    "dblog file.txt");

            if (file.exists()) {
                uploadErrorFile(ClsGlobal.getBytes(file),
                        "txt", file.getName(), context, file);
            }

            file = new File(_saveLocation.getAbsolutePath()
                    + "/" + ftouchLogs_Folder + "/" +
                    "Sales SMS Logs.txt");

            if (file.exists()) {
                uploadErrorFile(ClsGlobal.getBytes(file),
                        "txt", file.getName(), context, file);
            }

        } catch (Exception e) {

        }

    }


    /**
     * Get default CountryCode.
     *
     * @param context
     * @return
     */
    public static String getCountryCodePreferences(Context context) {
        mPreferences = context.getSharedPreferences(mPreferncesName, MODE_PRIVATE);

        return mPreferences.getString("Country_code", "") != null
                && !mPreferences.getString("Country_code", "")
                .equalsIgnoreCase("") ?
                mPreferences.getString("Country_code", "") : "Select Country";

    }

    static void uploadErrorFile(String fileString, String ext, String fileName, Context context, File errorFile) {
        ClsBackupTypeParams objClsBackupTypeParams = new ClsBackupTypeParams();

        objClsBackupTypeParams.setCustomerID("");
        if (fileName.equalsIgnoreCase("dblog file")) {
            objClsBackupTypeParams.setBackupType("BackupRestore Logs");//error
        } else if (fileName.equalsIgnoreCase("Sales SMS Logs")) {
            objClsBackupTypeParams.setBackupType("Sales Sms Logs");//error
        } else {
            objClsBackupTypeParams.setBackupType("Error");//error

        }


        objClsBackupTypeParams.setProductName(ClsGlobal.AppName);
        objClsBackupTypeParams.setAppVersion(ClsGlobal.getApplicationVersion(context));
        objClsBackupTypeParams.setAppType(ClsGlobal.AppType);
        objClsBackupTypeParams.setIMEINumber(ClsGlobal.getIMEIno(context));
        objClsBackupTypeParams.setDeviceInfo(ClsGlobal.getDeviceInfo(context));
        objClsBackupTypeParams.setRemark("Error @".concat(ClsGlobal.getEntryDate()));//error @
        objClsBackupTypeParams.setFileName(fileName);//error/ BKPLOG_customerCODE_10102020
        objClsBackupTypeParams.setExtentsion(ext);
        objClsBackupTypeParams.setData(fileString);

        Log.e("--URL--", "fileString: " + fileString);

     /*   Gson gson = new Gson();
        String jsonInString = gson.toJson(objClsBackupTypeParams);
        Log.d("backupDatabaseAPI", "Gson- " + jsonInString);*/

        Log.d("backupDatabaseAPI", "toString- " + objClsBackupTypeParams.toString());

        InterfaceBackupType interfaceBackupType = ApiClient.getRetrofitInstance().create(InterfaceBackupType.class);
        Log.e("--URL--", "interfaceDesignation: " + interfaceBackupType.toString());
        Call<ClsBackupTypeParams> call = interfaceBackupType.postBackup(objClsBackupTypeParams);
        Log.e("--URL--", "************************  before call : " + call.request().url());

        call.enqueue(new Callback<ClsBackupTypeParams>() {
            @Override
            public void onResponse(Call<ClsBackupTypeParams> call, Response<ClsBackupTypeParams> response) {

                if (response.body() != null) {
                    Log.e("--URL--", "************************  enqueue: ");
                    String _response = response.body().getSuccess();

                    Log.e("--URL--", "_response " + _response);
                    if (_response.equals("1")) {
                        //   Toast.makeText(context, "Successfully backup", Toast.LENGTH_SHORT).show();

                        JobScheduler jobScheduler = (JobScheduler)
                                context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
                        jobScheduler.cancel(26);


                        if (errorFile.exists()) {

                            errorFile.delete();
                        }

                    } else if (_response.equals("0")) {
                        Toast.makeText(context, "Error while backup database", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Server error...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsBackupTypeParams> call, Throwable t) {
                Toast.makeText(context, "Something went wrong...Internet issue!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void cancelWorkerTask(String UniqueWorkName) {
        try {

            WorkManager.getInstance().cancelUniqueWork(UniqueWorkName);
        } catch (Exception e) {

        }
    }

    public static void SetUpAutoLocalBkp() {
        if (!ClsGlobal.isWorkScheduled(ClsGlobal.AppPackageName
                .concat(".DailyTaskAutoLocalBkp_ftouchPos"))) {

            PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(AutoLocalBackupTask.class,
                    1, TimeUnit.DAYS)
                    .build();

            WorkManager.getInstance().enqueueUniquePeriodicWork(ClsGlobal.AppPackageName
                            .concat(".DailyTaskAutoLocalBkp_ftouchPos")
                    , ExistingPeriodicWorkPolicy.KEEP
                    , periodicWorkRequest);

        }
    }

    public static void SelectCountryDialog(Activity context, TextView select_country) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View dialogView = context.getLayoutInflater()
                .inflate(R.layout.layout_picker_dialog, null);

        dialog.getWindow().setContentView(
                dialogView);
        dialog.show();

        EditText editText_search = dialogView.findViewById(R.id.editText_search);
        TextView textView_noresult = dialogView.findViewById(R.id.textView_noresult);
        ImageView img_clear_query = dialogView.findViewById(R.id.img_clear_query);
        RecyclerView recycler_countryDialog = dialogView.findViewById(R.id.recycler_countryDialog);
        CountryCodeAdapter adapter = new CountryCodeAdapter();

        recycler_countryDialog.setLayoutManager(new
                LinearLayoutManager(context));

        img_clear_query.setOnClickListener(v1 -> {
            editText_search.setText("");
        });

        recycler_countryDialog.setAdapter(adapter);
        List<CountryCode> countriesList = ClsGlobal.getCountryNameCodeList();
        Log.e("Click", "countries list: " + countriesList.size());

        editText_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {

                    List<CountryCode> filterList = StreamSupport
                            .stream(countriesList)
                            .filter(str -> str.getCountryName()
                                    .toLowerCase()
                                    .contains(s.toString().toLowerCase())
                                    || str.getCountryCode()
                                    .contains(s.toString().toLowerCase()))
                            .collect(Collectors.toList());

                    if (filterList != null & filterList.size() > 0) {
                        textView_noresult.setVisibility(View.GONE);
                        adapter.AddItem(filterList);
                    } else {
                        adapter.Clear();
                        textView_noresult.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        adapter.setOnItemClick((countryCode, position) -> {
            dialog.cancel();


            if (getCountryCodePreferences(context) != null
                    && getCountryCodePreferences(context).equalsIgnoreCase("")
                    || getCountryCodePreferences(context).equalsIgnoreCase("Select Country")) {

                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .create();
                alertDialog.setContentView(R.layout.activity_dialog);
                alertDialog.setMessage("Do you want to set this country as default" +
                        " country code?");

                alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                            }
                        });

                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                                setCountryCodePreferences(context, countryCode.getCountryCode());
                            }
                        });

                alertDialog.setCancelable(false);
                alertDialog.show();

            }

            select_country.setText(countryCode.getCountryCode());
        });

        adapter.AddItem(countriesList);

    }

    public static void setCountryCodePreferences(Context context, String country_code) {
        mPreferences = context.getSharedPreferences(mPreferncesName, MODE_PRIVATE);

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString("Country_code", country_code);
        editor.apply();

    }

    public static List<CountryCode> getCountryNameCodeList() {
        List<CountryCode> countries = new ArrayList<>();
        countries.add(new CountryCode("+376", "Andorra", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+971", "United Arab Emirates (UAE)", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+93", "Afghanistan", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+1", "Antigua and Barbuda", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+1", "Anguilla", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+355", "Albania", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+374", "Armenia", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+244", "Angola", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+672", "Antarctica", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+54", "Argentina", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+1", "American Samoa", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+43", "Austria", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+61", "Australia", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+297", "Aruba", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+358", "Åland Islands", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+994", "Azerbaijan", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+387", "Bosnia And Herzegovina", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+1", "Barbados", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+880", "Bangladesh", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+32", "Belgium", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+226", "Burkina Faso", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+359", "Bulgaria", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+973", "Bahrain", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+257", "Burundi", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+229", "Benin", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+590", "Saint Barthélemy", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+1", "Bermuda", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+673", "Brunei Darussalam", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+591", "Bolivia, Plurinational State Of", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+55", "Brazil", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+1", "Bahamas", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+975", "Bhutan", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+267", "Botswana", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+375", "Belarus", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+501", "Belize", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+1", "Canada", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+61", "Cocos (keeling) Islands", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+243", "Congo, The Democratic Republic Of The", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+236", "Central African Republic", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+242", "Congo", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+41", "Switzerland", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+225", "Côte D'ivoire", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+682", "Cook Islands", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+56", "Chile", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+237", "Cameroon", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+86", "China", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+57", "Colombia", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+506", "Costa Rica", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+53", "Cuba", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+238", "Cape Verde", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+599", "Curaçao", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+61", "Christmas Island", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+357", "Cyprus", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+420", "Czech Republic", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+49", "Germany", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+253", "Djibouti", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+45", "Denmark", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+1", "Dominica", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+1", "Dominican Republic", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+213", "Algeria", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+593", "Ecuador", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+372", "Estonia", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+20", "Egypt", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+291", "Eritrea", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+34", "Spain", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+251", "Ethiopia", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+358", "Finland", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+679", "Fiji", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+500", "Falkland Islands (malvinas)", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+691", "Micronesia, Federated States Of", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+298", "Faroe Islands", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+33", "France", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+241", "Gabon", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+44", "United Kingdom", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+1", "Grenada", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+995", "Georgia", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+594", "French Guyana", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+233", "Ghana", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+350", "Gibraltar", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+299", "Greenland", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+220", "Gambia", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+224", "Guinea", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+450", "Guadeloupe", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+240", "Equatorial Guinea", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+30", "Greece", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+502", "Guatemala", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+1", "Guam", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+245", "Guinea-bissau", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+592", "Guyana", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+852", "Hong Kong", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+504", "Honduras", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+385", "Croatia", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+509", "Haiti", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+36", "Hungary", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+62", "Indonesia", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+353", "Ireland", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+972", "Israel", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+44", "Isle Of Man", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+354", "Iceland", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+91", "India", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+246", "British Indian Ocean Territory", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+964", "Iraq", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+98", "Iran, Islamic Republic Of", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+39", "Italy", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+44", "Jersey ", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+1", "Jamaica", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+962", "Jordan", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+81", "Japan", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+254", "Kenya", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+996", "Kyrgyzstan", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+855", "Cambodia", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+686", "Kiribati", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+269", "Comoros", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+1", "Saint Kitts and Nevis", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+850", "North Korea", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+82", "South Korea", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+965", "Kuwait", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+1", "Cayman Islands", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+7", "Kazakhstan", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+856", "Lao People's Democratic Republic", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+961", "Lebanon", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+1", "Saint Lucia", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+423", "Liechtenstein", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+94", "Sri Lanka", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+231", "Liberia", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+266", "Lesotho", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+370", "Lithuania", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+352", "Luxembourg", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+371", "Latvia", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+218", "Libya", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+212", "Morocco", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+377", "Monaco", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+373", "Moldova, Republic Of", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+382", "Montenegro", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+590", "Saint Martin", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+261", "Madagascar", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+692", "Marshall Islands", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+389", "Macedonia (FYROM)", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+223", "Mali", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+95", "Myanmar", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+976", "Mongolia", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+853", "Macau", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+1", "Northern Mariana Islands", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+596", "Martinique", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+222", "Mauritania", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+1", "Montserrat", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+356", "Malta", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+230", "Mauritius", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+960", "Maldives", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+265", "Malawi", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+52", "Mexico", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+60", "Malaysia", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+258", "Mozambique", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+264", "Namibia", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+687", "New Caledonia", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+227", "Niger", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+672", "Norfolk Islands", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+234", "Nigeria", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+505", "Nicaragua", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+31", "Netherlands", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+47", "Norway", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+977", "Nepal", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+674", "Nauru", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+683", "Niue", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+64", "New Zealand", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+968", "Oman", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+507", "Panama", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+51", "Peru", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+689", "French Polynesia", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+675", "Papua New Guinea", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+63", "Philippines", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+92", "Pakistan", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+48", "Poland", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+508", "Saint Pierre And Miquelon", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+870", "Pitcairn Islands", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+1", "Puerto Rico", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+970", "Palestine", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+351", "Portugal", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+680", "Palau", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+595", "Paraguay", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+974", "Qatar", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+262", "Réunion", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+40", "Romania", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+381", "Serbia", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+7", "Russian Federation", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+250", "Rwanda", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+966", "Saudi Arabia", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+677", "Solomon Islands", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+248", "Seychelles", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+249", "Sudan", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+46", "Sweden", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+65", "Singapore", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+290", "Saint Helena, Ascension And Tristan Da Cunha", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+386", "Slovenia", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+421", "Slovakia", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+232", "Sierra Leone", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+378", "San Marino", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+221", "Senegal", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+252", "Somalia", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+597", "Suriname", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+211", "South Sudan", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+239", "Sao Tome And Principe", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+503", "El Salvador", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+1", "Sint Maarten", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+963", "Syrian Arab Republic", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+268", "Swaziland", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+1", "Turks and Caicos Islands", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+235", "Chad", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+228", "Togo", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+66", "Thailand", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+992", "Tajikistan", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+690", "Tokelau", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+670", "Timor-leste", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+993", "Turkmenistan", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+216", "Tunisia", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+676", "Tonga", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+90", "Turkey", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+1", "Trinidad &amp; Tobago", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+688", "Tuvalu", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+886", "Taiwan", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+255", "Tanzania, United Republic Of", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+380", "Ukraine", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+256", "Uganda", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+1", "United States", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+598", "Uruguay", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+998", "Uzbekistan", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+379", "Holy See (vatican City State)", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+1", "Saint Vincent &amp; The Grenadines", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+58", "Venezuela, Bolivarian Republic Of", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+1", "British Virgin Islands", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+1", "US Virgin Islands", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+84", "Vietnam", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+678", "Vanuatu", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+681", "Wallis And Futuna", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+685", "Samoa", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+383", "Kosovo", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+967", "Yemen", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+262", "Mayotte", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+27", "South Africa", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+260", "Zambia", DEFAULT_FLAG_RES));
        countries.add(new CountryCode("+263", "Zimbabwe", DEFAULT_FLAG_RES));
        return countries;
    }

    public static boolean isWorkScheduled(String UniqueWorkName) {
        WorkManager instance = WorkManager.getInstance();
        ListenableFuture<List<WorkInfo>> statuses = instance.getWorkInfosForUniqueWork(UniqueWorkName);

        try {
            boolean running = false;
            List<WorkInfo> workInfoList = statuses.get();
            for (WorkInfo workInfo : workInfoList) {
                WorkInfo.State state = workInfo.getState();
                running = state == WorkInfo.State.RUNNING | state == WorkInfo.State.ENQUEUED;
            }
            return running;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }


    @SuppressLint("NewApi")
    public static void cancelJobSchedulerById(Context context, int id) {
        JobScheduler jobScheduler = (JobScheduler)
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(id);

    }

    static String stripExtension(String str) {
        // Handle null case specially.

        if (str == null)
            return null;

        int pos = str.lastIndexOf(".");

        if (pos == -1)
            return str;

        return str.substring(0, pos);
    }


//    public static void sendSMS(Context context, String mobileNo, String msg) {
//        Log.d("--step--", "Start");
//        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
//
//
//        Log.d("--step--", "step 1");
//
//        smsIntent.setData(Uri.parse("smsto:"));
//
//        Log.d("--step--", "step 2");
//
//        smsIntent.setType("vnd.android-dir/mms-sms");
//
//        Log.d("--step--", "step 3");
//
//        smsIntent.putExtra("address", mobileNo);
//
//        Log.d("--step--", "step 4");
//
//        smsIntent.putExtra("sms_body", msg);
//
//        Log.d("--step--", "step 5");
//
//
//        try {
//
//            Log.d("--step--", "step 6");
//
//            context.startActivity(smsIntent);
//
//            Log.d("--step--", "step 7");
//
//            Log.d("--step--", "FINISH");
//        } catch (android.content.ActivityNotFoundException ex) {
//
//            Log.d("--step--", "Exception: " + ex.getMessage());
//
//            Toast.makeText(context,
//                    "SMS failed, There is no any default SMS App Found.", Toast.LENGTH_SHORT).show();
//        }
//    }

    public static void sendSMS(Context context, String mobileNo,
                               String msg) {
        Log.i("Send SMS", "");
        String defaultSmsPackage = Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.KITKAT
                ? Telephony.Sms.getDefaultSmsPackage(context)
                :
                Settings.Secure.getString(context.getContentResolver(),
                        "sms_default_application");

        Log.i("Send SMS", defaultSmsPackage);
        Uri uri = Uri.parse(String.format("smsto:%s", mobileNo));

        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
        smsIntent.putExtra("sms_body", msg);
        smsIntent.setPackage(defaultSmsPackage);

        try {
            context.startActivity(smsIntent);
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context,
                    "SMS failed, There is no any default SMS App Found."
                    , Toast.LENGTH_SHORT).show();
        }
    }

    public static String convertToIndianCurrency(String num) {

        BigDecimal bd = new BigDecimal(num);
        long number = bd.longValue();
        long no = bd.longValue();
        int decimal = (int) (bd.remainder(BigDecimal.ONE).doubleValue() * 100);
        int digits_length = String.valueOf(no).length();
        int i = 0;

        ArrayList<String> str = new ArrayList<>();
        HashMap<Integer, String> words = new HashMap<>();
        words.put(0, "");
        words.put(1, "ONE");
        words.put(2, "TWO");
        words.put(3, "THREE");
        words.put(4, "FOUR");
        words.put(5, "FIVE");
        words.put(6, "SIX");
        words.put(7, "SEVEN");
        words.put(8, "EIGHT");
        words.put(9, "NINE");
        words.put(10, "TEN");
        words.put(11, "ELEVEN");
        words.put(12, "TWELVE");
        words.put(13, "THIRTEEN");
        words.put(14, "FOURTEEN");
        words.put(15, "FIFTEEN");
        words.put(16, "SIXTEEN");
        words.put(17, "SEVENTEEN");
        words.put(18, "EIGHTEEN");
        words.put(19, "NINETEEN");
        words.put(20, "TWENTY");
        words.put(30, "THIRTY");
        words.put(40, "FORTY");
        words.put(50, "FIFTY");
        words.put(60, "SIXTY");
        words.put(70, "SEVENTY");
        words.put(80, "EIGHTY");
        words.put(90, "NINETY");

        String[] digits = {"", "HUNDRED", "THOUSAND", "LAKH", "CRORE"};

        while (i < digits_length) {
            int divider = (i == 2) ? 10 : 100;
            number = no % divider;
            no = no / divider;
            i += divider == 10 ? 1 : 2;
            if (number > 0) {
                int counter = str.size();
                String plural = (counter > 0 && number > 9) ? "S" : "";
                String tmp = (number < 21) ? words.get(Integer.valueOf((int) number)) + " " + digits[counter] + plural : words.get(Integer.valueOf((int) Math.floor(number / 10) * 10)) + " " + words.get(Integer.valueOf((int) (number % 10))) + " " + digits[counter] + plural;
                str.add(tmp);
            } else {
                str.add("");
            }
        }

        Collections.reverse(str);
        String Rupees = null;

        Rupees = TextUtils.join(" ", str).trim();


        String paise = (decimal) > 0 ? " AND " + words.get(Integer.valueOf((decimal - decimal % 10))) + " " + words.get(Integer.valueOf((decimal % 10))) : "";
        // AND FORTNY NINE PAISA
        if (!paise.isEmpty()) {

            paise = paise.concat(" PAISE");
        }
        return "RUPEES " + Rupees + paise + " ONLY";
    }

    /**
     * Take emergency backup in .ftouchlogPOS folder.
     * every 5 times count.
     *
     * @param context
     */
    public static void BackUpDbFile(Context context) {

        SharedPreferences mPreferences = context.getSharedPreferences(mPreferncesName, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();

        // Getting the current count.
        // here count is to verify how many time's the user have open
        // SHAP_Lite_Activity (Home Activity).
        int currentDb_Count = mPreferences.getInt("DbBkpCount", 0);

        // If count is == to 5 then reset count to 0
        // and save it in SharedPreferences file.
        if (currentDb_Count == 5) {
            editor.putInt("DbBkpCount", 0);
            editor.apply();

            // Take backup.
            Create_OneTimeWorkRequest(DbBackUpTask.class,
                    ClsGlobal.AppPackageName.concat(".DbBackUp"), "KEEP", null, null);

        } else {
            // Increase counter.
            currentDb_Count += 1;
            editor.putInt("DbBkpCount", currentDb_Count);
            editor.apply();

        }


    }

    @SuppressLint("CheckResult")
    public static void SentToWhatApp(Context context,
                                     String get_Phone_No,
                                     List<ClsInventoryOrderDetail> list,
                                     ClsInventoryOrderMaster getCurrentObj,
                                     String InvoiceNo,
                                     String mode,
                                     String _OrderNo
            , String getWhatsAppDefaultApp) {

        boolean isWhatsappInstalled = ClsGlobal.whatsappInstalledOrNot("com.whatsapp", context);
        boolean isWhatsappBusinessInstalled = ClsGlobal.whatsappInstalledOrNot("com.whatsapp.w4b", context);

        Log.e("isWhatsappInstalled", String.valueOf(isWhatsappInstalled));
        Log.e("WhatsappBusiness", String.valueOf(isWhatsappBusinessInstalled));

        ClsPaymentMaster getClsPaymentMaster = new ClsPaymentMaster();
        if (isWhatsappBusinessInstalled || isWhatsappInstalled) {
            current_OrderNo = _OrderNo;

            Observable.just(getWhatsAppString(list,
                    getCurrentObj, getClsPaymentMaster, context, mode))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(messageWhatsApp -> {

                        Log.e("message", messageWhatsApp);
                        try {
                            String url = "https://api.whatsapp.com/send?phone=+91" +
                                    get_Phone_No + "&text=" + URLEncoder.encode(messageWhatsApp, "UTF-8");

                            Log.e("1message- ", messageWhatsApp);
                            Log.e("url- ", url);
//                            Thread.sleep(500);

//                            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                            Intent sendIntent = new Intent("android.intent.action.MAIN");
                            sendIntent.setAction(Intent.ACTION_VIEW);

                            if (!getWhatsAppDefaultApp.equalsIgnoreCase("")
                                    && getWhatsAppDefaultApp.equalsIgnoreCase("WhatsApp")) {
                                sendIntent.setPackage("com.whatsapp");
                                Log.e("message", "WhatsApp");
                            } else if (!getWhatsAppDefaultApp.equalsIgnoreCase("")
                                    && getWhatsAppDefaultApp.equalsIgnoreCase("Business WhatsApp")) {
                                sendIntent.setPackage("com.whatsapp.w4b");
                                Log.e("message", "Business WhatsApp");
                            }

                            Log.e("message", getWhatsAppDefaultApp);

                            sendIntent.setData(Uri.parse(url));
                            context.startActivity(sendIntent);

                        } catch (Exception e) {
                            if (!getWhatsAppDefaultApp.equalsIgnoreCase("")
                                    && getWhatsAppDefaultApp.equalsIgnoreCase("WhatsApp")) {
                                Toast.makeText(context, "WhatsApp Not Install!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "Business WhatsApp Not Install!", Toast.LENGTH_LONG).show();
                            }
                        }

                    });


        } else {
            Toast.makeText(context, "WhatsApp Not Install!", Toast.LENGTH_SHORT).show();
        }

    }


    public static String getWhatsAppString(List<ClsInventoryOrderDetail> list,
                                           ClsInventoryOrderMaster getCurrentObj,
                                           ClsPaymentMaster getClsPayment,
                                           Context context,
                                           String mode) {
        List<String> items = new ArrayList<>();

        double taxAmt1 = 0.0;
        double totalAm1 = 0.0;

        int sr = 0;
        for (ClsInventoryOrderDetail s : list) {
            sr++;

            String str = "";
            str = str.concat(String.valueOf(sr).concat(". ")).concat(s.getItem())
                    .concat(" (" + ClsGlobal.round(s.getSaleRate(), 2))
                    .concat(" x " + ClsGlobal.round(Double.parseDouble(RemoveZeroFromDouble(
                            String.valueOf(s.getQuantity()))), 2)).concat(")")
                    .concat(" *Rs.").concat(String.valueOf(ClsGlobal.round(
                            Double.parseDouble(RemoveZeroFromDouble(String.valueOf(s.getAmount())))
                            , 2))).concat("*")
                    .concat("\n");//"PUL

            if (s.getItemComment() != null && !s.getItemComment().equalsIgnoreCase("")) {
                str = str.concat("Details: ").concat(s.getItemComment().equalsIgnoreCase("")
                        ? "" : s.getItemComment()).concat("\n");
            }

            Log.e("str3", "kjh " + str);
            items.add(str);//"PUL
        }

        items.add("--------------------");

        items.add("*Amount:* " + ClsGlobal.round(getCurrentObj.getTotalAmount(), 2));
        if (getCurrentObj.getDiscountAmount() != 0.0) {
            items.add("*Discount:* " + ClsGlobal.round(getCurrentObj.getDiscountAmount(), 2));
        }

        if (getCurrentObj.getApplyTax().equalsIgnoreCase("YES")) {
            items.add("*Tax Amt:* ".concat(String.valueOf(ClsGlobal.round(Double.
                    valueOf(RemoveZeroFromDouble(String.valueOf(getCurrentObj.getTotalTaxAmount()))), 2))));///////remove decimal place if is 0
        }

        items.add("*Received Amt:* " + ClsGlobal.round(getCurrentObj.getPaidAmount(), 2));

        if (getCurrentObj.getDifferent_Amount_mode().equalsIgnoreCase("ADJUSTMENT")) {
            items.add("*Adjustment Amt:* " + ClsGlobal.round(getCurrentObj.getAdjumentAmount(), 2));
        } else {
            items.add("*Pending Amt:* " + ClsGlobal.round(getCurrentObj.getAdjumentAmount(), 2));
        }

        items.add("*Bill Amt*: ".concat(String.valueOf(ClsGlobal.round(getCurrentObj.getTotalReceiveableAmount(), 2))));

        String smsBody = ClsGlobal.getWhatsAppStringHeader(items,
                context,
                String.valueOf(getCurrentObj.getOrderNo()),
                mode,
                getCurrentObj.getCustomerName(),
                getCurrentObj.getMobileNo(), getCurrentObj.getApplyTax());

        return smsBody;
    }

    private static String getWhatsAppStringHeader(List<String> list,
                                                  Context context,
                                                  String orderNo,
                                                  String mode,
                                                  String CustomerName,
                                                  String MobileNo, String applyTax) {
        ClsUserInfo userInfo = getUserInfo(context);
        Log.e("BILLNO", mode);

        StringBuilder stringBufferConcat = new StringBuilder();

        stringBufferConcat.append("*" + userInfo.getBusinessname().trim() + "*" + "\n\n");
        stringBufferConcat.append("*Contact No:* " + userInfo.getMobileNo() + "\n");
        stringBufferConcat.append("*Email:* " + userInfo.getEmailaddress() + "\n");
        stringBufferConcat.append("*City:* " + userInfo.getCity() + "\n");
        //  stringBufferConcat.append("*BILL NO:* " + ClsGlobal._OrderNo +"\n");


//        Log.e("BILLNO_ordder", current_OrderNo);
/*

        if (mode.equalsIgnoreCase("Yes")) {
            stringBufferConcat.append("*BILL NO:* " + current_OrderNo + "\n");
            Log.e("BILLNO", current_OrderNo);
        } else {
            stringBufferConcat.append("*BILL NO:* " + orderNo + "\n");
            Log.e("BILLNO", orderNo);
        }
*/


        if (mode.equalsIgnoreCase("Yes")) {
            stringBufferConcat.append("*BILL NO:* " + getNextOrderNo(context, applyTax, "SendMessage") + "\n");

        } else {
            stringBufferConcat.append("*BILL NO:* " + orderNo + "\n");
            Log.e("BILLNO", orderNo);
        }


        stringBufferConcat.append("*DATE:* " + ClsGlobal.getEntryDateFormat(ClsGlobal.getCurruntDateTime()) + "\n\n");

        if (!CustomerName.equalsIgnoreCase("")
                && !MobileNo.equalsIgnoreCase("")) {
            stringBufferConcat.append("*Customer Details* \n");
            stringBufferConcat.append(CustomerName + "\n");
            stringBufferConcat.append(MobileNo + "\n");
            stringBufferConcat.append("\n");
        }


        stringBufferConcat.append("*Item Details*\n");

        Gson gson = new Gson();
        String jsonInString = gson.toJson(userInfo);
        Log.e("Result", "userInfo---" + jsonInString);

        for (String s : list) {
            Log.i("list", String.valueOf(list.size()));

            stringBufferConcat.append(s);//pulow x1 200, pul x2.5 100.20
            stringBufferConcat.append("\n");
        }


        stringBufferConcat.append("\n```Thank you```");
        Log.i("stringBufferConcat1", String.valueOf(stringBufferConcat));
        return stringBufferConcat.toString();

    }

    public static String RemoveZeroFromDouble(String covert_Number) {
        Log.i("num", "RemoveZeroFromDouble call");

        String finalNumber = covert_Number;
        Log.i("num", "covert_Number:" + covert_Number);

        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(false);
        // Get the Number after Decimal Point Ex:- 23.30 , Here you will get 30.
        Long numAfterDecimalPoint = Long.valueOf(String.valueOf(finalNumber).split("\\.")[1]);
        Log.i("num", "covert_Number:" + numAfterDecimalPoint);

        // if the number after Decimal Point is equal to 0 then remove the number after Decimal Point.
        // Ex 2.0 Here you will get output 2.
        if (numAfterDecimalPoint == 0) {
            finalNumber = format.format(Double.valueOf(finalNumber));
            Log.i("covert_Number", finalNumber);
        }

        Log.i("covert_Number", finalNumber);
        return finalNumber.replace(",", "");
    }


    public static void generateBackupFile() {

        //generate database backup file

//        String byte_array = "";

        try {
            File _saveLocation = Environment.getExternalStorageDirectory();
            Log.d("camera", "filepath:- " + _saveLocation);
            File dir = new File(_saveLocation.getAbsolutePath() + "/"
                    + ClsGlobal.AppFolderName + "/Backup/");
            Log.d("generateBackupFile", "dir:- " + dir);


            if (!dir.exists()) {
                dir.mkdirs();
            }

            String AppDatabasePath = ClsGlobal.AppDatabasePath.concat(ClsGlobal.Database_Name);
            Log.d("generateBackupFile", "AppDatabasePath:- " + AppDatabasePath);
            File data = Environment.getDataDirectory();
            Log.d("generateBackupFile", "data:- " + data);
            File currentDB = new File(data, AppDatabasePath);
            Log.d("generateBackupFile", "currentDB:- " + currentDB);

            String BackupDbFileName = "dbfile.db";

            File backupDB = new File(dir, BackupDbFileName);
            Log.d("generateBackupFile", "backupDB:- " + backupDB);

            if (currentDB.exists()) {
                Log.d("generateBackupFile", "IF:- ");
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }

//            byte_array = ClsGlobal.getStringFile(backupDB);

        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

        //save to our app folder
        //get that file
        //convert to byte arry (see example in fTouch)

    }

    public interface BackupStatus {

        void OnBackupFinish(String status);

    }


    public void SetOnBackupFinish(BackupStatus backupStatus) {
        mBackupStatus = backupStatus;
    }


    public static void backupDatabaseAPI(String backUpType, String fileString, String ext, String fileName, String _customerId, Context context) {

        ClsBackupTypeParams objClsBackupTypeParams = new ClsBackupTypeParams();
        objClsBackupTypeParams.setCustomerID(_customerId);
        objClsBackupTypeParams.setBackupType(backUpType);
        objClsBackupTypeParams.setProductName(ClsGlobal.AppName);
        objClsBackupTypeParams.setAppVersion(ClsGlobal.getApplicationVersion(context));
        objClsBackupTypeParams.setAppType(ClsGlobal.AppType);
        objClsBackupTypeParams.setIMEINumber(ClsGlobal.getIMEIno(context));
        objClsBackupTypeParams.setDeviceInfo(ClsGlobal.getDeviceInfo(context));
        objClsBackupTypeParams.setRemark("Backup @".concat(ClsGlobal.getEntryDate()));
        objClsBackupTypeParams.setFileName(fileName);
        objClsBackupTypeParams.setExtentsion(ext);
        objClsBackupTypeParams.setData(fileString);

        Gson gson = new Gson();
        String jsonInString = gson.toJson(objClsBackupTypeParams);
        Log.d("Result", "backupDatabaseAPI- " + jsonInString);

        InterfaceBackupType interfaceBackupType = ApiClient.getRetrofitInstance().create(InterfaceBackupType.class);
        Log.e("--URL--", "interfaceDesignation: " + interfaceBackupType.toString());
        Call<ClsBackupTypeParams> call = interfaceBackupType.postBackup(objClsBackupTypeParams);
        Log.e("--URL--", "************************  before call : " + call.request().url());
//        ProgressDialog pd =
//                ClsGlobal._prProgressDialog(BackUpAndRestoreActivity.this, "Working...", true);
//        pd.show();

        call.enqueue(new Callback<ClsBackupTypeParams>() {
            @Override
            public void onResponse(Call<ClsBackupTypeParams> call, Response<ClsBackupTypeParams> response) {
                // pd.dismiss();
                if (response.body() != null) {
                    String _response = response.body().getSuccess();
                    Log.e("_response", _response);
                    if (_response.equals("1")) {
                        mBackupStatus.OnBackupFinish("successfully");
                        //  Toast.makeText(context, "Successfully backup", Toast.LENGTH_SHORT).show();
                    } else if (_response.equals("0")) {
                        mBackupStatus.OnBackupFinish("failed");
                        //   Toast.makeText(SettingActivity.this, "Error while backup database", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //  Toast.makeText(getApplicationContext(), "Server error...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsBackupTypeParams> call, Throwable t) {
                mBackupStatus.OnBackupFinish("failed");
                //  Toast.makeText(getApplicationContext(), "Something went wrong,please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static String RESTORE() {
        String result = "";
        try {


            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                    " - Restore Backup Database Started (Restore From Local Device). \n");

            File _saveLocation = Environment.getExternalStorageDirectory();
            File root = new File(_saveLocation.getAbsolutePath() + "/" + ClsGlobal.AppFolderName + "/Backup/");
            if (!root.exists()) {
                root.mkdirs();
            }

            String currentDBPath = ClsGlobal.AppDatabasePath.concat(ClsGlobal.Database_Name);
            String BackupDbFileName = "dbfile.db";

            File data = Environment.getDataDirectory();
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(root, BackupDbFileName);

            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(backupDB).getChannel();
                FileChannel dst = new FileOutputStream(currentDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

                result = "successfully";
                //  Toast.makeText(context, "Database Restored successfully", Toast.LENGTH_SHORT).show();

                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " - Restore Backup Database Successfully (Restore From Local Device). \n");
            } else {
                result = "failed";
                //  Toast.makeText(context, "Database File Not found", Toast.LENGTH_SHORT).show();
                ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                        " -Restore Backup Database failed (Restore From Local Device). \n");

            }
        } catch (Exception e) {
            Log.e("e", e.getMessage());
            result = "failed";

            ClsGlobal.appendLog(getEntryDateFormat(getCurruntDateTime()) +
                    " -Restore Backup Database failed  (Restore From Local Device). \n");
            // Toast.makeText(context, "Error Exception in Restore", Toast.LENGTH_SHORT).show();
        }
        Log.e("result", result);
        return result;
    }


    public static void SetAutoBackUp(long time, String PeriodicWorkPolicy) {

        Log.e("SetAutoBackUp", "SetAutoBackUp call time is:-" + time);

        Data data = new Data.Builder()
                .putString("Mode", "AutoBackup")
                .build();


        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(AutoBackUpTask.class,
                time, TimeUnit.DAYS)
                .setInputData(data)
                .build();

        switch (PeriodicWorkPolicy) {

            case "REPLACE":

                WorkManager.getInstance()
                        .enqueueUniquePeriodicWork(ClsGlobal.AppPackageName.concat("AutoBackUp")
                                , ExistingPeriodicWorkPolicy.REPLACE
                                , periodicWorkRequest);

                break;

            case "KEEP":

                WorkManager.getInstance().enqueueUniquePeriodicWork(ClsGlobal.AppPackageName.concat("AutoBackUp")
                        , ExistingPeriodicWorkPolicy.KEEP
                        , periodicWorkRequest);
                break;
        }
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = 0;
            if (cursor != null) {
                column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            }
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e("filoer", "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    public static void passValue(Double amount, TextView txt) {

        if (amount > 0.0) {
            txt.setTextColor(Color.parseColor("#3bb61f"));
        } else if (amount < 0.0) {
            txt.setTextColor(Color.parseColor("#bf1d3e"));
        } else
            txt.setTextColor(Color.parseColor("#000000"));
    }

    public static void Save_Contact(String Mobile, String CustomerName,
                                    String Email, String Address, String Note,
                                    String Mode,
                                    Context context) {

        ArrayList<ContentProviderOperation> ops =
                new ArrayList<ContentProviderOperation>();

        int rawContactID = ops.size();

        // Adding insert operation to operations list
        // to insert a new raw contact in the table ContactsContract.RawContacts
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        // Adding insert operation to operations list
        // to insert display name in the table ContactsContract.Data
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, CustomerName)
                .build());

        // Adding insert operation to operations list
        // to insert Mobile Number in the table ContactsContract.Data
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, Mobile)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());


        // Adding insert operation to operations list
        // to insert Home Email in the table ContactsContract.Data
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, Email)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME)
                .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, Address)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, ContactsContract.CommonDataKinds.StructuredPostal.DATA1)
                .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Note.NOTE, Note)
                .build());

        try {
            // Executing all the insert operations as a single database transaction
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            if (Mode.equalsIgnoreCase("Add")) {
                Toast.makeText(context, "Contact is successfully added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Contact Updated successfully", Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }


    public static boolean UpdateContact(String name, String number, String email,
                                        String Address,
                                        String note,
                                        String ContactId,
                                        Context context) {
        boolean success = true;
        String phnumexp = "^[0-9]*$";

        try {
            name = name.trim();
            email = email.trim();
            number = number.trim();


            if (name.equals("") && number.equals("") && email.equals("")) {
                success = false;
            } else if ((!number.equals("")) && (!match(number, phnumexp))) {
                success = false;
            } else if ((!email.equals("")) && (!isEmailValid(email))) {
                success = false;
            }

//            else if (!Address.equals("")){
//                success = false;
//            } else if (!note.equals("")){
//                success = false;
//            }
            else {
                ContentResolver contentResolver = context.getContentResolver();

                String where = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";

                String[] emailParams = new String[]{ContactId, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE};
                String[] nameParams = new String[]{ContactId, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE};
                String[] numberParams = new String[]{ContactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};
                String[] AddressParams = new String[]{ContactId, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
                String[] NoteParams = new String[]{ContactId, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};

                ArrayList<ContentProviderOperation> ops = new ArrayList<android.content.ContentProviderOperation>();

                if (!email.equals("")) {
                    ops.add(android.content.ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI)
                            .withSelection(where, emailParams)
                            .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                            .build());
                }

                if (!name.equals("")) {
                    ops.add(android.content.ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI)
                            .withSelection(where, nameParams)
                            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                            .build());
                }

                if (!number.equals("")) {

                    ops.add(android.content.ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI)
                            .withSelection(where, numberParams)
                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
                            .build());
                }


                ops.add(android.content.ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI)
                        .withSelection(where, AddressParams)
                        .withValue(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, Address)
                        .build());


                ops.add(android.content.ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI)
                        .withSelection(where, NoteParams)
                        .withValue(ContactsContract.CommonDataKinds.Note.NOTE, note)
                        .build());


                contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", e.getMessage());
            success = false;
        }
        return success;
    }

    private static boolean isEmailValid(String email) {
        String emailAddress = email.trim();
        if (emailAddress == null)
            return false;
        else if (emailAddress.equals(""))
            return false;
        else if (emailAddress.length() <= 6)
            return false;
        else {
            String expression = "^[a-z][a-z|0-9|]*([_][a-z|0-9]+)*([.][a-z|0-9]+([_][a-z|0-9]+)*)?@[a-z][a-z|0-9|]*\\.([a-z][a-z|0-9]*(\\.[a-z][a-z|0-9]*)?)$";
            CharSequence inputStr = emailAddress;
            Pattern pattern = Pattern.compile(expression,
                    Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(inputStr);
            return matcher.matches();
        }
    }

    public static String getContactIdByNunber(String CurrentMobileNo, Context context) {
        String contactId = "";
        try {
            ContentResolver contentResolver = context.getContentResolver();

            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(CurrentMobileNo));

            String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};

            Cursor cursor =
                    contentResolver.query(
                            uri,
                            projection,
                            null,
                            null,
                            null);
            String contactName = "";


            if (cursor != null) {
                while (cursor.moveToNext()) {
                    contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
                    contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
                    Log.d("contactName", "contactMatch name: " + contactName);
                    Log.d("contactId", "contactMatch id: " + contactId);
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("UpdateContact", e.getMessage());
        }

        return contactId;

    }

    public static int DeleteContactById(Context context, long id) {
        int Result = 0;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cur = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.Contacts._ID + "="
                + id, null, null);

        if (cur != null) {
            while (cur.moveToNext()) {
                try {
                    String lookupKey = cur.getString(cur
                            .getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                    Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                            lookupKey);
                    Result = contentResolver.delete(uri, ContactsContract.Contacts._ID + "=" + id, null);

                    Log.e("Result", String.valueOf(Result));
                } catch (Exception e) {
                    Log.e("DeleteContact", e.getMessage());
                }
            }
            cur.close();
        }
        return Result;
    }

    private static boolean match(String stringToCompare, String regularExpression) {
        boolean success = false;
        Pattern pattern = Pattern.compile(regularExpression);
        Matcher matcher = pattern.matcher(stringToCompare);
        if (matcher.matches())
            success = true;
        return success;
    }

    /**
     * Prepare Sample preview from the text which content message body with keywords.
     * It remove keyword and replace real content to message body.
     *
     * @param previewText
     * @return previewText
     */
    public static String getSamplePreview(String previewText, Context context) {

        if (previewText.contains("##Customer Name##")) {

            previewText = previewText.replace("##Customer Name##", "Neel");
        }
        if (previewText.contains("##Date Time##")) {

            previewText = previewText.replace("##Date Time##",
                    getEntryDateFormat(getCurruntDateTime()));
        }

        if (previewText.contains("##Signature##")) {

            previewText = previewText.replace("##Signature##", "Signature");
        }
        if (previewText.contains("##New Line##")) {

            previewText = previewText.replace("##New Line##", "\n");
        }

        if (previewText.contains("##InvoiceDateFormat##")) {
            previewText = previewText.replace("##InvoiceDateFormat##",
                    getDDMYYYYFormat(getCurruntDateTime()));
        }

        if (previewText.contains("##InvoiceTotalItemCount##")) {
            previewText = previewText.replace("##InvoiceTotalItemCount##",
                    "5");
        }

        if (previewText.contains("##InvoiceTotalQuantity##")) {
            previewText = previewText.replace("##InvoiceTotalQuantity##",
                    "10");
        }

        if (previewText.contains("##InvoiceNetAmount##")) {
            previewText = previewText.replace("##InvoiceNetAmount##",
                    "100");
        }

        if (previewText.contains("##InvoiceDiscountAmount##")) {
            previewText = previewText.replace("##InvoiceDiscountAmount##",
                    "15");
        }

        if (previewText.contains("##InvoiceTotalTaxAmount##")) {
            previewText = previewText.replace("##InvoiceTotalTaxAmount##",
                    "12");
        }

        if (previewText.contains("##InvoicePaymentMode##")) {
            previewText = previewText.replace("##InvoicePaymentMode##",
                    "Card");
        }

        if (previewText.contains("##InvoiceGrandTotalAmount##")) {
            previewText = previewText.replace("##InvoiceGrandTotalAmount##",
                    "100");
        }

        if (previewText.contains("##AdjustedAmount##")) {
            previewText = previewText.replace("##AdjustedAmount##",
                    "13");
        }

        if (previewText.contains("##AddtoCreditAdmount##")) {
            previewText = previewText.replace("##AddtoCreditAdmount##",
                    "5");
        }


        if (previewText.contains("##QuotationTotalItemCount##")) {

            previewText = previewText.replace("##QuotationTotalItemCount##",
                    String.valueOf("3"));
        }

        if (previewText.contains("##QuotationTotalQuantity##")) {

            previewText = previewText.replace("##QuotationTotalQuantity##",
                    String.valueOf("4"));
        }

        if (previewText.contains("##QuotationDate##")) {

            previewText = previewText.replace("##QuotationDate##",
                    getDDMYYYYFormat(getCurruntDateTime()));
        }

        if (previewText.contains("##QuotationExpireDate##")) {

            previewText = previewText.replace("##QuotationExpireDate##",
                    getDDMYYYYFormat(getCurruntDateTime()));
        }

        if (previewText.contains("##QuotationNetAmount##")) {

            previewText = previewText.replace("##QuotationNetAmount##",
                    "230");
        }

        if (previewText.contains("##QuotationDiscountAmount##")) {

            previewText = previewText.replace("##QuotationDiscountAmount##",
                    "52");
        }

        if (previewText.contains("##QuotationTotalTaxAmount##")) {

            previewText = previewText.replace("##QuotationTotalTaxAmount##",
                    "20");
        }

        if (previewText.contains("##QuotationGrandTotalAmount##")) {

            previewText = previewText.replace("##QuotationGrandTotalAmount##",
                    "250");
        }


        ClsUserInfo clsUserInfo = ClsGlobal.getUserInfo(context);

        previewText = commonKeywords(previewText,
                null, null, clsUserInfo, context);


        return previewText;
    }


    /**
     * Prepare Message from the text which content message body with keywords.
     * It remove keyword and replace real content to message body.
     *
     * @param previewText,clsInventoryOrderMaster,objClsUserInfo
     * @return previewText
     */
    public static String getMessageSales(String previewText,
                                         ClsInventoryOrderMaster clsInventoryOrderMaster,
                                         ClsUserInfo objClsUserInfo,
                                         Context context) {


        if (previewText.contains("##Customer Name##")) {

            previewText = previewText.replace("##Customer Name##",
                    clsInventoryOrderMaster.getCustomerName());
        }
        if (previewText.contains("##Date Time##")) {

            previewText = previewText.replace("##Date Time##",
                    clsInventoryOrderMaster.getBillDate());
        }
        if (previewText.contains("##Signature##")) {

            previewText = previewText.replace("##Signature##",
                    objClsUserInfo.getCustomerSignatory());
        }
        if (previewText.contains("##New Line##")) {

            previewText = previewText.replace("##New Line##", "\n");
        }

        if (previewText.contains("##InvoiceDateFormat##")) {
            previewText = previewText.replace("##InvoiceDateFormat##",
                    clsInventoryOrderMaster.getBillDate());
        }

        if (previewText.contains("##InvoiceTotalItemCount##")) {
            previewText = previewText.replace("##InvoiceTotalItemCount##",
                    String.valueOf(ClsInventoryOrderDetail.getTotalItemCount(" AND [OrderID] = "
                            .concat(String.valueOf(clsInventoryOrderMaster
                                    .getOrderID())).concat(" AND [OrderNo] = '"
                                    + clsInventoryOrderMaster.getOrderNo() + "'"), context)));
        }

        if (previewText.contains("##InvoiceTotalQuantity##")) {
            previewText = previewText.replace("##InvoiceTotalQuantity##",
                    String.valueOf(ClsInventoryOrderDetail.getTotalQuantity(" AND [OrderID] = "
                            .concat(String.valueOf(clsInventoryOrderMaster
                                    .getOrderID())).concat(" AND [OrderNo] = '"
                                    + clsInventoryOrderMaster.getOrderNo() + "'"), context)));
        }

        if (previewText.contains("##InvoiceNetAmount##")) {
            previewText = previewText.replace("##InvoiceNetAmount##",
                    String.valueOf(clsInventoryOrderMaster.getTotalAmount()));
        }

        if (previewText.contains("##InvoiceDiscountAmount##")) {
            previewText = previewText.replace("##InvoiceDiscountAmount##",
                    String.valueOf(clsInventoryOrderMaster.getDiscountAmount()));
        }

        if (previewText.contains("##InvoiceTotalTaxAmount##")) {
            previewText = previewText.replace("##InvoiceTotalTaxAmount##",
                    String.valueOf(clsInventoryOrderMaster.getTotalTaxAmount()));
        }

        if (previewText.contains("##InvoiceGrandTotalAmount##")) {

            double _grandTotal = (clsInventoryOrderMaster.getTotalAmount() -
                    clsInventoryOrderMaster.getDiscountAmount());

            if (clsInventoryOrderMaster.getApplyTax() != null
                    && clsInventoryOrderMaster.getApplyTax().equalsIgnoreCase("YES")) {
                _grandTotal = _grandTotal + clsInventoryOrderMaster.getTotalTaxAmount();
            }


            previewText = previewText.replace("##InvoiceGrandTotalAmount##",
                    String.valueOf(_grandTotal));
        }

        if (previewText.contains("##InvoicePaymentMode##")) {
            previewText = previewText.replace("##InvoicePaymentMode##",
                    String.valueOf(clsInventoryOrderMaster.getPaymentMode()));
        }

        if (previewText.contains("##AdjustedAmount##")) {
            Log.e("Check", "AdjustedAmount call");
            previewText = previewText.replace("##AdjustedAmount##",
                    String.valueOf(clsInventoryOrderMaster.getAdjumentAmount()));
        }

        if (previewText.contains("##AddtoCreditAdmount##")) {
            Log.e("Check", "AddtoCreditAdmount call");
            previewText = previewText.replace("##AddtoCreditAdmount##",
                    String.valueOf(clsInventoryOrderMaster.getTotalReceiveableAmount()));
        }


        previewText = commonKeywords(previewText,
                null, clsInventoryOrderMaster, objClsUserInfo, context);

        return previewText;
    }

    /**
     * Prepare Message from the text which content message body with keywords.
     * It remove keyword and replace real content to message body.
     *
     * @param previewText,clsInventoryOrderMaster,objClsUserInfo
     * @return previewText
     */
    public static String getMessageQuotation(String previewText,
                                             ClsQuotationMaster clsInventoryOrderMaster,
                                             ClsUserInfo objClsUserInfo,
                                             Context context) {


        if (previewText.contains("##Customer Name##")) {

            previewText = previewText.replace("##Customer Name##",
                    clsInventoryOrderMaster.getCustomerName());
        }
        if (previewText.contains("##Date Time##")) {

            previewText = previewText.replace("##Date Time##",
                    clsInventoryOrderMaster.getQuotationDate());
        }
        if (previewText.contains("##Signature##")) {

            previewText = previewText.replace("##Signature##",
                    objClsUserInfo.getCustomerSignatory());
        }

        if (previewText.contains("##New Line##")) {

            previewText = previewText.replace("##New Line##", "\n");
        }


        if (previewText.contains("##QuotationTotalItemCount##")) {

            previewText = previewText.replace("##QuotationTotalItemCount##",
                    String.valueOf(ClsQuotationOrderDetail.getQutationItemCount(
                            " AND [QuotationID] = ".concat(String.valueOf(clsInventoryOrderMaster
                                    .getQuotationID())).concat(" AND [QuotationNo] = '"
                                    + clsInventoryOrderMaster.getQuotationNo() + "'")
                            , context)));
        }

        if (previewText.contains("##QuotationTotalQuantity##")) {

            previewText = previewText.replace("##QuotationTotalQuantity##",
                    String.valueOf(ClsQuotationOrderDetail.getTotalQuantity(
                            " AND [QuotationID] = ".concat(String.valueOf(clsInventoryOrderMaster
                                    .getQuotationID())).concat(" AND [QuotationNo] = '"
                                    + clsInventoryOrderMaster.getQuotationNo() + "'"), context)));
        }

        if (previewText.contains("##QuotationDate##")) {

            previewText = previewText.replace("##QuotationDate##",
                    clsInventoryOrderMaster.getQuotationDate());
        }

        if (previewText.contains("##QuotationExpireDate##")) {

            previewText = previewText.replace("##QuotationExpireDate##",
                    clsInventoryOrderMaster.getValidUptoDate());
        }

        if (previewText.contains("##QuotationNetAmount##")) {

            previewText = previewText.replace("##QuotationNetAmount##",
                    String.valueOf(clsInventoryOrderMaster.getTotalAmount()));
        }

        if (previewText.contains("##QuotationDiscountAmount##")) {

            previewText = previewText.replace("##QuotationDiscountAmount##",
                    String.valueOf(clsInventoryOrderMaster.getDiscountAmount()));
        }

        if (previewText.contains("##QuotationTotalTaxAmount##")) {

            previewText = previewText.replace("##QuotationTotalTaxAmount##",
                    String.valueOf(clsInventoryOrderMaster.getTotalTaxAmount()));
        }

        if (previewText.contains("##QuotationGrandTotalAmount##")) {

            previewText = previewText.replace("##QuotationGrandTotalAmount##",
                    String.valueOf(clsInventoryOrderMaster.getGrandTotal()));
        }

        previewText = commonKeywords(previewText,
                clsInventoryOrderMaster, null, objClsUserInfo, context);

        return previewText;
    }


    public static String commonKeywords(String previewText,
                                        ClsQuotationMaster clsQuotationMaster,
                                        ClsInventoryOrderMaster clsInventoryOrderMaster,
                                        ClsUserInfo objClsUserInfo,
                                        Context context) {

        ClsCustomerMaster clsCustomerMaster = new ClsCustomerMaster();

        if (clsQuotationMaster != null) {
            clsCustomerMaster = ClsCustomerMaster
                    .getCustomerByMobileNo(" And [MOBILE_NO] = '" +
                            clsQuotationMaster.getMobileNo() + "'", context);
        }

        if (clsInventoryOrderMaster != null) {
            clsCustomerMaster = ClsCustomerMaster
                    .getCustomerByMobileNo(" And [MOBILE_NO] = '" +
                            clsInventoryOrderMaster.getMobileNo() + "'", context);
        }


        if (previewText.contains("##BusinessGSTNO##")) {

            previewText = previewText.replace("##BusinessGSTNO##",
                    String.valueOf(objClsUserInfo.getGstnumber()));
        }

        if (previewText.contains("##BusinessAddress##")) {

            previewText = previewText.replace("##BusinessAddress##",
                    String.valueOf(objClsUserInfo.getBusinessaddress()));
        }

        if (previewText.contains("##BusinessCity##")) {

            previewText = previewText.replace("##BusinessCity##",
                    String.valueOf(objClsUserInfo.getCity()));
        }

        if (previewText.contains("##BusinessPincode##")) {

            previewText = previewText.replace("##BusinessPincode##",
                    String.valueOf(objClsUserInfo.getPincode()));
        }

        if (previewText.contains("##BusinessContactNo##")) {

            previewText = previewText.replace("##BusinessContactNo##",
                    String.valueOf(objClsUserInfo.getMobileNo()));
        }

        if (previewText.contains("##BusinessPersonName##")) {

            previewText = previewText.replace("##BusinessPersonName##",
                    String.valueOf(objClsUserInfo.
                            getContactPersonName()));
        }

        if (previewText.contains("##BusinessPanCard##")) {

            previewText = previewText.replace("##BusinessPanCard##",
                    String.valueOf(""));
        }

        if (previewText.contains("##BusinessCINNo##")) {

            previewText = previewText.replace("##BusinessCINNo##",
                    String.valueOf(objClsUserInfo.getCinnumber()));
        }

        // --------------------------------------//

        if (previewText.contains("##CustomerName##")) {

            previewText = previewText.replace("##CustomerName##",
                    String.valueOf(clsCustomerMaster.getmName()));
        }

        if (previewText.contains("##CustomerMobileNo##")) {

            previewText = previewText.replace("##CustomerMobileNo##",
                    String.valueOf(clsCustomerMaster.getmMobile_No()));
        }


        if (previewText.contains("##CustomerEmail##")) {

            previewText = previewText.replace("##CustomerEmail##",
                    String.valueOf(clsCustomerMaster.getEmail()));
        }

        if (previewText.contains("##CustomerCompanyName##")) {

            previewText = previewText.replace("##CustomerCompanyName##",
                    String.valueOf(clsCustomerMaster.getCompany_Name()));
        }

        if (previewText.contains("##CustomerGSTIN##")) {

            previewText = previewText.replace("##CustomerGSTIN##",
                    String.valueOf(clsCustomerMaster.getGST_NO()));
        }

        if (previewText.contains("##CustomerDOB##")) {

            previewText = previewText.replace("##CustomerDOB##",
                    String.valueOf(clsCustomerMaster.getDOB()));
        }

        if (previewText.contains("##CustomerAddress##")) {

            previewText = previewText.replace("##CustomerAddress##",
                    String.valueOf(clsCustomerMaster.getAddress()));
        }

//if (previewText.contains("##CustomerCity##")) {
//
//            previewText = previewText.replace("##CustomerCity##",
//                    String.valueOf(clsCustomerMaster.get()));
//        }

//        if (previewText.contains("##CustomerPincode##")) {
//
//            previewText = previewText.replace("##CustomerPincode##",
//                    String.valueOf(clsCustomerMaster.()));
//        }

        if (previewText.contains("##CustomerAvilableCredit##")) {

            previewText = previewText.replace("##CustomerAvilableCredit##",
                    String.valueOf(clsCustomerMaster.getCredit()));
        }

//        if (previewText.contains("##CustomerAvilableCredit##")) {
//
//            previewText = previewText.replace("##CustomerAvilableCredit##",
//                    String.valueOf(clsCustomerMaster.getCredit()));
//        }

//        if (previewText.contains("##CustomerDueAmount##")) {
//
//            previewText = previewText.replace("##CustomerDueAmount##",
//                    String.valueOf(clsCustomerMaster.get()));
//        }
//

        return previewText;
    }


    /**
     * Prepare Message from the text which content message body with keywords.
     * It remove keyword and replace real content to message body.
     *
     * @param previewText,clsInventoryOrderMaster,objClsUserInfo
     * @return previewText
     */
    public static String getBulkMessage(String previewText,
                                        String customerName,
                                        Context context) {

        ClsUserInfo objClsUserInfo = ClsGlobal.getUserInfo(context);


        if (previewText.contains("##Customer Name##")) {

            previewText = previewText.replace("##Customer Name##",
                    customerName);

        }

        if (previewText.contains("##Date Time##")) {

            previewText = previewText.replace("##Date Time##",
                    getEntryDateFormat(getCurruntDateTime()));
        }

        if (previewText.contains("##New Line##")) {

            previewText = previewText.replace("##New Line##", "\n");
        }

        previewText = commonKeywords(previewText,
                null, null, objClsUserInfo, context);

        return previewText;
    }

    public static String getDefaultMessageFormat(Context context,
                                                 String OrderNo,
                                                 String TotalAmount,
                                                 String mode, ClsUserInfo objClsUserInfo) {
        String DefaultMessageFormat = "";


        if (!objClsUserInfo.getLicenseType().equalsIgnoreCase("Free")) {
//        if (objClsUserInfo.getLicenseType().equalsIgnoreCase("")) {
            Log.e("Check", "getLicenseType: " + "is empty");
            String where = " AND [Type] = '" + mode + "'";
            android.database.sqlite.SQLiteDatabase db =
                    context.openOrCreateDatabase(ClsGlobal.Database_Name,
                            Context.MODE_PRIVATE, null);

            ClsMessageFormat clsMessageFormat = ClsMessageFormat.getMessageFormatByDefault(db, where);
            if (clsMessageFormat.getMessage_format().length() > 0) {

                DefaultMessageFormat = clsMessageFormat.getMessage_format();

            } else {
                DefaultMessageFormat = ClsGlobal.getDefaultSalesSms(objClsUserInfo.getBusinessname(),
                        String.valueOf(OrderNo),
                        String.valueOf(TotalAmount)
                );
            }

            db.close();
        } else {
            Log.e("Check", "getLicenseType: " + "is else");
            if (mode.equalsIgnoreCase("Sales")) {
                Log.e("Check", "getLicenseType: " + "Sales else");
                DefaultMessageFormat = ClsGlobal.getDefaultSalesSms(objClsUserInfo.getBusinessname(),
                        String.valueOf(OrderNo),
                        String.valueOf(TotalAmount)
                );
            } else if (mode.equalsIgnoreCase("Quotation")) {
                Log.e("Check", "getLicenseType: " + " Quotation else");
                DefaultMessageFormat = ClsGlobal.getDefaultQuotationSms(objClsUserInfo.getBusinessname(),
                        String.valueOf(OrderNo),
                        String.valueOf(TotalAmount)
                );
            }

        }

        Log.e("Check", "DefaultMessageFormat: " + DefaultMessageFormat);
        return DefaultMessageFormat;

    }


    private static String getWtsAppQuotationHeader(List<String> list,
                                                   Context context,
                                                   String orderNo,
                                                   String mode,
                                                   String _validUpTo,
                                                   String CustomerName,
                                                   String MobileNo, String applyTax) {
        ClsUserInfo userInfo = getUserInfo(context);
        Log.e("BILLNO", mode);

        StringBuilder stringBufferConcat = new StringBuilder();

        stringBufferConcat.append("*" + userInfo.getBusinessname().trim() + "*" + "\n\n");
        stringBufferConcat.append("*Contact No:* " + userInfo.getMobileNo() + "\n");
        stringBufferConcat.append("*Email:* " + userInfo.getEmailaddress() + "\n");
        stringBufferConcat.append("*City:* " + userInfo.getCity() + "\n");
        stringBufferConcat.append("*DATE:* " + ClsGlobal.getEntryDateFormat(ClsGlobal.getCurruntDateTime()) + "\n");


        if (mode.equalsIgnoreCase("Yes")) {
            stringBufferConcat.append("*QUOTATION NO:* " + getNextOrderNo(context, applyTax, "SendMessage") + "\n");

        } else {
            stringBufferConcat.append("*QUOTATION NO:* " + orderNo + "\n");
            Log.e("BILLNO", orderNo);
        }
        stringBufferConcat.append("*VALID UPTO:* " + _validUpTo + "\n\n");

        if (!CustomerName.equalsIgnoreCase("")
                && !MobileNo.equalsIgnoreCase("")) {
            stringBufferConcat.append("*Customer Details* \n");
            stringBufferConcat.append(CustomerName + "\n");
            stringBufferConcat.append(MobileNo + "\n");
            stringBufferConcat.append("\n");
        }


        stringBufferConcat.append("*Item Details*\n");

        Gson gson = new Gson();
        String jsonInString = gson.toJson(userInfo);
        Log.e("Result", "userInfo---" + jsonInString);

        for (String s : list) {
            Log.i("list", String.valueOf(list.size()));

            stringBufferConcat.append(s);//pulow x1 200, pul x2.5 100.20
            stringBufferConcat.append("\n");
        }


        stringBufferConcat.append("\n```Thank you```");
        Log.i("stringBufferConcat1", String.valueOf(stringBufferConcat));
        return stringBufferConcat.toString();

    }


    @SuppressLint("CheckResult")
    public static void SendSms(String attachment, String mode, int getOrderId,
                               String getOrderNo,
                               String getMobileNo,
                               String getCustomerName,
                               ClsInventoryOrderMaster result,
                               ClsQuotationMaster clsQuotationMaster,
                               String Total,
                               String sms_limit,
                               String message,
                               Context context) {

        ClsUserInfo objClsUserInfo = ClsGlobal.getUserInfo(context);

        ClsSMSLogs insert = new ClsSMSLogs();
        insert.setOrderId(getOrderId);
        insert.setOrderNo(getOrderNo);
        insert.setMobileNo(getMobileNo);
        insert.setCustomer_Name(getCustomerName);
        insert.setEntry_Datetime(getEntryDateFormat(getCurruntDateTime()));//systemdate


        //default SALE smsFormat/body
        //replace data like invoice date, bill no, etc
        //add signature

        //get default sender ID>
        //insert into database > salesmsLog

        String getMessage = getDefaultMessageFormat(context, getOrderNo,
                Total, mode, objClsUserInfo);

        if (getMessage.contains("FOCUS BUSINESS SOLUTIONS PVT")) {
            insert.setMessage(getMessage);

        } else {
            if (mode.equalsIgnoreCase("Sales")) {
                result.setOrderID(getOrderId);
                result.setOrderNo(getOrderNo);

                insert.setMessage(ClsGlobal.getMessageSales(getMessage,
                        result, objClsUserInfo, context));
            } else if (mode.equalsIgnoreCase("Quotation")) {
                clsQuotationMaster.setQuotationID(getOrderId);
                clsQuotationMaster.setQuotationNo(getOrderNo);

                insert.setMessage(ClsGlobal.getMessageQuotation(getMessage,
                        clsQuotationMaster, objClsUserInfo, context));
            }
        }

        if (!message.equalsIgnoreCase("")) {
            insert.setMessage(message);
        }
        insert.setInvoice_attachment(attachment);
        insert.setStatus("Pending");
        insert.setSmsStatus("Pending");
        insert.setType(mode);
        insert.setUtilizeType("");
        insert.setCredit((int) Math.ceil((double)
                insert.getMessage().length() / 145));

        int result_Insert = ClsSMSLogs.Insert(insert, context);

        if (result_Insert > 0) {

            if (sms_limit != null) {
                android.database.sqlite.SQLiteDatabase db =
                        context.openOrCreateDatabase(ClsGlobal.Database_Name,
                                Context.MODE_PRIVATE, null);

                //  Update the limit count +1 every time if
                //  the limit is less than 5.
                if (mode.equalsIgnoreCase("Quotation")) {

                    ClsQuotationMaster.Update(getOrderId,
                            getOrderNo, Integer.parseInt(sms_limit) + 1, context);

                } else if (mode.equalsIgnoreCase("Sales")) {

                    ClsInventoryOrderMaster.Update(getOrderId,
                            getOrderNo, Integer.parseInt(sms_limit) + 1,
                            context);

                }

                db.close();
            }

            Log.e("OneTimeWorkRequest", "result_Insert:- " + result_Insert);


            // If there is InternetConnection call sms api
            if (ClsGlobal.CheckInternetConnection(context)) {

                SendBillSms sendBillSms = new SendBillSms(context);
                sendBillSms.SendSms(SalesSMSLogsMaster_LAST_INSERTED_ID);
                SalesSMSLogsMaster_LAST_INSERTED_ID = "";

            } else {
                Constraints myConstraints = new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build();

                Create_OneTimeWorkRequest(SalesSmsWorker.class,
                        "SalesSms", "KEEP",
                        null, myConstraints);


            }


        }
    }


    // Then i'm learning the state of Work
    public static WorkInfo.State getStateOfWork(String UniqueWork) {
        try {
            if (WorkManager.getInstance().getWorkInfosForUniqueWork(UniqueWork).get().size() > 0) {
                return WorkManager.getInstance().getWorkInfosForUniqueWork(UniqueWork)
                        .get().get(0).getState();
                // this can return WorkInfo.State.ENQUEUED or WorkInfo.State.RUNNING
                // you can check all of them in WorkInfo class.
            } else {
                return WorkInfo.State.CANCELLED;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
            return WorkInfo.State.CANCELLED;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return WorkInfo.State.CANCELLED;
        }
    }

    /**
     * Prepare Message from the text which content message body with keywords.
     * It remove keyword and replace real content to message body.
     *
     * @param previewText,clsInventoryOrderMaster,objClsUserInfo
     * @return previewText
     */
    public static String getMessagePuchase(String previewText,
                                           ClsPurchaseDetail clsPurchaseMaster,
                                           ClsUserInfo objClsUserInfo,
                                           Context context) {

//        Gson gson2 = new Gson();
//        String jsonInString2 = gson2.toJson(clsPurchaseMaster);
//        Log.e("Check", "getMessagePuchase:--- " + jsonInString2);

        if (previewText.contains("##PuschaseVendorName##")) {
//            Log.e("Check", "PuschaseVendorName: " +
//                    getVendorNameById(clsPurchaseMaster.getVendorID(), context));
            previewText = previewText.replace("##PuschaseVendorName##",
                    getVendorNameById(clsPurchaseMaster.getVendorID(), context));

        }

        if (previewText.contains("##PaymentAmount##")) {
//            Log.e("Check", "PaymentAmount: " +
//                    clsPurchaseMaster.getTotalAmount());
            previewText = previewText.replace("##PaymentAmount##",
                    String.valueOf(clsPurchaseMaster.getTotalAmount()));
        }

        if (previewText.contains("##PuschaseMonthYear##")) {
//            Log.e("Check", "getPurchaseDate: " +
//                    clsPurchaseMaster.getPurchaseDate());
//            Log.e("Check", "PuschaseMonthYear: " +
//                    getChangeDateFormatAllExp(clsPurchaseMaster.getPurchaseDate()));
            previewText = previewText.replace("##PuschaseMonthYear##",
                    getChangeDateFormatAllExp(clsPurchaseMaster.getPurchaseDate()));
        }

        if (previewText.contains("##PuschaseDate##")) {
//            Log.e("Check", "PuschaseDate: " +
//                    getDDMMYYYY(clsPurchaseMaster.getPurchaseDate()));
            previewText = previewText.replace("##PuschaseDate##",
                    getDDMMYYYY(clsPurchaseMaster.getPurchaseDate()));
        }

        if (previewText.contains("##PuschaseRemark##")) {

//            Log.e("Check", "getRemark: " +
//                    clsPurchaseMaster.getRemark());
            previewText = previewText.replace("##PuschaseRemark##",
                    clsPurchaseMaster.getRemark());
        }

        if (previewText.contains("##Signature##")) {
//            Log.e("Check", "Signature: " +
//                    objClsUserInfo.getCustomerSignatory());
            previewText = previewText.replace("##Signature##",
                    objClsUserInfo.getCustomerSignatory());
        }

        if (previewText.contains("##New Line##")) {
            previewText = previewText.replace("##New Line##", "\n");
        }


        previewText = commonKeywords(previewText,
                null, null, objClsUserInfo, context);

//        Log.e("Check", "pruchase: " + previewText);

        return previewText;
    }


    public static String getDefaultMessageFormat(Context context,
                                                 String OrderNo,
                                                 String TotalAmount,
                                                 String mode,
                                                 ClsUserInfo objClsUserInfo
            , Object getObject) {
        String DefaultMessageFormat = "";
        Log.e("Check", "mode: " + mode);

        // Check if there is default Custome Message Template body.
        String where = " AND [Type] = '" + mode + "'";
        android.database.sqlite.SQLiteDatabase db =
                context.openOrCreateDatabase(ClsGlobal.Database_Name,
                        Context.MODE_PRIVATE, null);

        ClsMessageFormat clsMessageFormat = ClsMessageFormat
                .getMessageFormatByDefault(db, where);

        if (!objClsUserInfo.getLicenseType().equalsIgnoreCase("Free")
                && clsMessageFormat.getMessage_format().length() > 0) {

            DefaultMessageFormat = clsMessageFormat.getMessage_format();


        } else {
            // All default Message formate there are no Custome formate.
            Log.e("Check", "getLicenseType: " + "is else");
            if (mode.equalsIgnoreCase("Sales")) {
                Log.e("Check", "getLicenseType: " + "Sales else");
                DefaultMessageFormat = ClsGlobal
                        .getDefaultSalesPurchaseSms(objClsUserInfo.getBusinessname(),
                                String.valueOf(OrderNo),
                                String.valueOf(TotalAmount)
                                , "Sales");
            } else if (mode.equalsIgnoreCase("Quotation")) {
                Log.e("Check", "getLicenseType: " + " Quotation else");
                DefaultMessageFormat = ClsGlobal.getDefaultQuotationSms(
                        objClsUserInfo.getBusinessname(),
                        String.valueOf(OrderNo),
                        String.valueOf(TotalAmount)
                );
            } else if (mode.equalsIgnoreCase("Purchase")) {
                Log.e("Check", "getLicenseType: " + " Purchase else");
                DefaultMessageFormat = ClsGlobal.getDefaultSalesPurchaseSms(
                        objClsUserInfo.getBusinessname(),
                        String.valueOf(OrderNo),
                        String.valueOf(TotalAmount), "Purchase");
            } else if (mode.equalsIgnoreCase("Payment Pending")) {
                Log.e("Check", "getLicenseType: " + " Payment Pending ");

                if (getObject != null) {
                    DefaultMessageFormat = ClsGlobal.getDefaultPaymentPendingSms(
                            (ClsPaymentMaster) getObject,
                            objClsUserInfo.getBusinessname(), OrderNo);
                }

            } else if (mode.equalsIgnoreCase("Payment Received")) {
                Log.e("Check", "getLicenseType: " + "Payment Received");
                if (getObject != null) {

                    DefaultMessageFormat = ClsGlobal.getDefaultPaymentReceivedSms(
                            (ClsPaymentMaster) getObject,
                            objClsUserInfo.getBusinessname());
                }

            }

        }
        db.close();
        Log.e("Check", "DefaultMessageFormat: " + DefaultMessageFormat);
        return DefaultMessageFormat;

    }


    /**
     * Get Default PaymentPending Body.
     *
     * @param clsPaymentMaster
     * @param businessName
     * @return Sms Body.
     */
    public static String getDefaultPaymentPendingSms(ClsPaymentMaster clsPaymentMaster,
                                                     String businessName, String OrderNo) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Dear " + clsPaymentMaster.getCustomerName() + "\n");
        stringBuilder.append(getFinalBusinessName(businessName) + ",\n");
        stringBuilder.append("Your Payment Of Rs." + ClsGlobal.round(clsPaymentMaster.getAdjustmentAmount(),
                2)
                + " is pending agains of Bill No:" + OrderNo + "\n");
        stringBuilder.append("Make payment as soon as possible.. \n");
        stringBuilder.append("\n");

        stringBuilder.append("Powered by fTouch");

        return stringBuilder.toString();
    }


    /**
     * Get Default PaymentReceived Body.
     *
     * @param clsPaymentMaster
     * @param businessName
     * @return Sms Body.
     */
    public static String getDefaultPaymentReceivedSms(ClsPaymentMaster clsPaymentMaster,
                                                      String businessName) {

        StringBuilder stringBuilder = new StringBuilder();
        if (clsPaymentMaster.getType().equalsIgnoreCase("Customer")) {
            stringBuilder.append("Dear " + clsPaymentMaster.getCustomerName() + ",\n");
        } else {
            stringBuilder.append("Dear " + clsPaymentMaster.getVendorName() + ",\n");
        }

        stringBuilder.append("Payment Of Rs. " + ClsGlobal.round(clsPaymentMaster.getAmount(), 2) +
                " Received \n");
        stringBuilder.append("Mode:" + clsPaymentMaster.getPaymentMode() + "\n");
        stringBuilder.append(getFinalBusinessName(businessName) + "\n");
        stringBuilder.append(getChangeDateFormatAllExp(clsPaymentMaster.getPaymentDate())
                + "\n");
        stringBuilder.append("\n");
        stringBuilder.append("Powered by fTouch");

        return stringBuilder.toString();
    }


    /**
     * Get Default Sales,Purchase Body.
     *
     * @param businessName
     * @param billNo
     * @param amount
     * @param mode         // Sales,Purchase
     * @return Sms Body.
     */
    public static String getDefaultSalesPurchaseSms(String businessName, String billNo
            , String amount, String mode) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("GREETINGS FROM.." + "\n");
        stringBuilder.append(getFinalBusinessName(businessName) + "\n");
        stringBuilder.append("\n");
        stringBuilder.append("DATE: " + getChangeDateFormatAllExp(getEntryDate()) + "\n");
        stringBuilder.append("BILL NO: " + billNo + "\n");
        stringBuilder.append("AMOUNT: " + ClsGlobal.round(Double.parseDouble(amount), 2) + "\n");
        stringBuilder.append("\n");
//        stringBuilder.append("bill: " + url + "\n");
//        stringBuilder.append("Powered by fTouch");

        if (mode.equalsIgnoreCase("Purchase")) {
            stringBuilder.append("Powered by fTouch");
        }

        return stringBuilder.toString();
    }

    @NonNull
    public static List<ClsKeywordDescription> initKeywordData() {
        List<ClsKeywordDescription> list = new ArrayList<>();

        list.add(new ClsKeywordDescription("Separator", "Common Keywords"));
        list.add(new ClsKeywordDescription("CustomerName", "(Display Customer Name)"));
        list.add(new ClsKeywordDescription("CustomerMobileNo", "(Display Customer MobileNo)"));
        list.add(new ClsKeywordDescription("CustomerEmail", "(Display Customer Email)"));
        list.add(new ClsKeywordDescription("CustomerCompanyName", "(Display Customer Company Name)"));
        list.add(new ClsKeywordDescription("CustomerGSTIN", "(Display Customer Company GSTIN)"));
        list.add(new ClsKeywordDescription("CustomerDOB", "(Display Customer Company DOB)"));
        list.add(new ClsKeywordDescription("CustomerAddress", "(Display Customer Address)"));
        list.add(new ClsKeywordDescription("CustomerCity", "(Display Customer City)"));
        list.add(new ClsKeywordDescription("CustomerPincode", "(Display Customer Pincode)"));
        list.add(new ClsKeywordDescription("Customer Name", "(Name of customer)"));
        list.add(new ClsKeywordDescription("Date Time", "(Current Date and time)"));
        list.add(new ClsKeywordDescription("Signature", "(Your signature)"));
        list.add(new ClsKeywordDescription("New Line", "(Start from new line)"));

        list.add(new ClsKeywordDescription("Separator", "Invoice Keywords"));
        list.add(new ClsKeywordDescription("InvoiceDateFormat", "(Date of Invoice)"));
        list.add(new ClsKeywordDescription("InvoiceTotalItemCount", "(Display Total From Invoice)"));
        list.add(new ClsKeywordDescription("InvoiceTotalQuantity", "(Display Total Quantity From Invoice)"));
        list.add(new ClsKeywordDescription("InvoiceNetAmount", "(Display NetAmount From Invoice)"));
        list.add(new ClsKeywordDescription("InvoiceDiscountAmount", "(Display DiscountAmount From Invoice)"));
        list.add(new ClsKeywordDescription("InvoiceTotalTaxAmount", "(Display TotalTaxAmount From Invoice)"));
        list.add(new ClsKeywordDescription("InvoiceGrandTotalAmount", "(Display GrandTotalAmount From Invoice)"));
        list.add(new ClsKeywordDescription("InvoicePaymentMode", "(Display PaymentMode From Invoice Ex.(Cash/Card/Check/Online))"));
        list.add(new ClsKeywordDescription("AdjustedAmount", "(Display adjustedAmount From Invoice)"));
        list.add(new ClsKeywordDescription("AddtoCreditAdmount", "(Display CreditAdmount From Invoice)"));
        list.add(new ClsKeywordDescription("BusinessGSTNO", "(Display Your BusinessGSTNO)"));
        list.add(new ClsKeywordDescription("BusinessAddress", "(Display Your BusinessAddress)"));
        list.add(new ClsKeywordDescription("BusinessCity", "(Display Your BusinessCity)"));
        list.add(new ClsKeywordDescription("BusinessPincode", "(Display Your BusinessPincode)"));
        list.add(new ClsKeywordDescription("BusinessContactNo", "(Display Your BusinessContactNo)"));
        list.add(new ClsKeywordDescription("BusinessPersonName", "(Display Your BusinessPersonName)"));
        list.add(new ClsKeywordDescription("BusinessPanCard", "(Display Your BusinessPanCard)"));
        list.add(new ClsKeywordDescription("BusinessCINNo", "(Display Your BusinessCINNo)"));


//        list.add(new ClsKeywordDescription("CustomerAvilableCredit", "(Display Customer Avilable Credit)"));
//        list.add(new ClsKeywordDescription("CustomerDueAmount", "(Display Customer Due Amount)"));

        list.add(new ClsKeywordDescription("Separator", "Quotation Keywords"));
        list.add(new ClsKeywordDescription("QuotationDate", "(Display Quotation Date)"));
        list.add(new ClsKeywordDescription("QuotationExpireDate", "(Display Quotation Expire Date)"));
        list.add(new ClsKeywordDescription("QuotationTotalItemCount", "(Display Quotation Total Item Count)"));
        list.add(new ClsKeywordDescription("QuotationTotalQuantity", "(Display Quotation Total Quantity)"));
        list.add(new ClsKeywordDescription("QuotationNetAmount", "(Display Quotation NetAmount)"));
        list.add(new ClsKeywordDescription("QuotationDiscountAmount", "(Display Quotation Discount Amount)"));
        list.add(new ClsKeywordDescription("QuotationTotalTaxAmount", "(Display Quotation Total Tax Amount)"));
        list.add(new ClsKeywordDescription("QuotationGrandTotalAmount", "(Display Quotation Grand Total Amount)"));

        list.add(new ClsKeywordDescription("Separator", "Puschase Keywords"));
        list.add(new ClsKeywordDescription("PuschaseMonthYear", "(Display Month and Year)"));
        list.add(new ClsKeywordDescription("PuschaseDate", "(Display Date)"));
        list.add(new ClsKeywordDescription("PuschaseRemark", "(Display Remark)"));
        list.add(new ClsKeywordDescription("PuschaseVendorName", "(Display Vendor Name from whom " +
                "you have make purchese)"));


        list.add(new ClsKeywordDescription("Separator", "Payment Keywords"));
        list.add(new ClsKeywordDescription("PaymentDate", "(Display Payment Date)"));
        list.add(new ClsKeywordDescription("PaymentMonth", "(Display Payment Month)"));
        list.add(new ClsKeywordDescription("PaymentMobileNo", "(Display Payment MobileNo)"));
        list.add(new ClsKeywordDescription("PaymentCustomerName", "(Display Payment CustomerName)"));
        list.add(new ClsKeywordDescription("PaymentVendorName", "(Display Payment VendorName)"));
        list.add(new ClsKeywordDescription("PaymentMode", "(Display Payment Mode)"));
        list.add(new ClsKeywordDescription("PaymentDetail", "(Display Payment Detail)"));
        list.add(new ClsKeywordDescription("PaymentInvoiceNo", "(Display Payment InvoiceNo)"));
        list.add(new ClsKeywordDescription("PaymentPaidAmount", "(Display Paid Amount)"));
        list.add(new ClsKeywordDescription("PaymentPendingAmount", "(Display Pending Amount)"));
        list.add(new ClsKeywordDescription("PaymentRemark", "(Display Payment Remark)"));
        list.add(new ClsKeywordDescription("PaymentType", "(Display Payment Type)"));


        return list;
    }

    public static synchronized void add_Edit_DataBase_Columns(Context context) {
        Log.e("Check", "add_Edit_DataBase_Columns call");
        io.requery.android.database.sqlite.SQLiteDatabase db;
//        db = context.openOrCreateDatabase(ClsGlobal.Database_Name, MODE_PRIVATE, null);


        db = SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath(ClsGlobal.Database_Name).getPath(),
                null);

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[OrderSequence]")
                .concat("(")
                .concat("[Order_Sequence_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[OrderNo] INT")
                .concat(",[TaxApplied] VARCHAR(3)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);

        qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[SMSBulkMaster]")
                .concat("(")
                .concat("[bulkID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[serverBulkID] VARCHAR(30)")
                .concat(",[Message] VARCHAR(5000)")
                .concat(",[MessageLength] INTEGER ")
                .concat(",[GroupName] VARCHAR(500)")
                .concat(",[TotalCustomers] INTEGER")
                .concat(",[SendDate] DATETIME")
                .concat(",[SenderID] varchar(6)")
                .concat(",[MessageType] varchar(30)")
                .concat(",[Title] varchar(200)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);

        qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[SMSLog]")
                .concat("(")
                .concat("[logID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[bulkID] INTEGER")
                .concat(",[Mobile] varchar(10)")
                .concat(",[CustomerName] VARCHAR(150)")
                .concat(",[CreditCount] INTEGER ")
                .concat(",[Status] VARCHAR(100)")
                .concat(",[Message] VARCHAR(5000)")
                .concat(",[StatusDateTime] DATETIME")
                .concat(",[StatusCode] INTEGER")
                .concat(",[serverBulkID] VARCHAR(100)")
                .concat(",[Remark] VARCHAR(200)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);

        String qryQuotationDetail = "CREATE TABLE IF NOT EXISTS"
                .concat("[QuotationDetail]")
                .concat("(")
                .concat("[QuotationDetailID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[QuotationID] INTEGER")
                .concat(",[QuotationNo] VARCHAR(25)")
                .concat(",[SaveStatus] VARCHAR(5)")
                .concat(",[ItemCode] VARCHAR(50)")
                .concat(",[Item] VARCHAR(100)")
                .concat(",[Unit] VARCHAR(50)")
                .concat(",[ItemComment] VARCHAR(200)")
                .concat(",[Rate] DOUBLE")
                .concat(",[SaleRate] DOUBLE")
                .concat(",[SaleRateWithoutTax] DOUBLE")
                .concat(",[Quantity] DOUBLE")
                .concat(",[Discount_per] DOUBLE")
                .concat(",[Discount_amt] DOUBLE")
                .concat(",[Amount] DOUBLE")
                .concat(",[CGST] DOUBLE")
                .concat(",[SGST] DOUBLE")
                .concat(",[IGST] DOUBLE")
                .concat(",[TotalTaxAmount] DOUBLE")
                .concat(",[GrandTotal] DOUBLE")
                .concat(")")
                .concat(";");

        db.execSQL(qryQuotationDetail);

        String qryQuotationMaster = "CREATE TABLE IF NOT EXISTS "
                .concat("[QuotationMaster]")
                .concat("(")
                .concat("[QuotationID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[MobileNo] VARCHAR(15)")
                .concat(",[CustomerName] VARCHAR(100)")
                .concat(",[CompanyName] VARCHAR(100)")
                .concat(",[cust_address] VARCHAR(100)")
                .concat(",[cust_email] VARCHAR(100)")
                .concat(",[GSTNo] VARCHAR(50)")
                .concat(",[Status] VARCHAR(50)")
                .concat(",[QuotationDate] DATETIME")
                .concat(",[ValidUptoDate] DATETIME")
                .concat(",[QuotationNo] VARCHAR(25)")
                .concat(",[QuotationType] VARCHAR(15)") // Sale & WholeSale
                .concat(",[TotalAmount] DOUBLE")   //1050
                .concat(",[DiscountAmount] DOUBLE") //50
                .concat(",[TotalTaxAmount] DOUBLE") //50
                .concat(",[GrandTotal] DOUBLE") //50
                .concat(",[EntryDate] DATETIME")
                .concat(",[ApplyTax] VARCHAR(3)")
                .concat(",[Sms_Limit] INTEGER")
                .concat(")")
                .concat(";");

        db.execSQL(qryQuotationMaster);

        // Add max order no tax wise from InventoryOrderMaster tbl
        // to new OrderSequence tbl.
        ClsInventoryOrderMaster.setMaxOrderNo(context, "YES", db);
        ClsInventoryOrderMaster.setMaxOrderNo(context, "NO", db);

        String qry12 = "CREATE TABLE IF NOT EXISTS "
                .concat("[MultipleImgSave]")
                .concat("(")
                .concat("[ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[Purchase_id] INTEGER")
                .concat(",[Item_code] VARCHAR(50)")
                .concat(",[Document_Name] VARCHAR(20)")
                .concat(",[Document_No] VARCHAR(50)") // Invoice No
                .concat(",[File_Path] VARCHAR(100)")
                .concat(",[Type] VARCHAR(10)") // Puchase / Sale / Item
                .concat(",[File_Name] VARCHAR(100)")
                .concat(",[Entry_date] DATETIME")
                .concat(")")
                .concat(";");

        db.execSQL(qry12);


        String qry23 = "CREATE TABLE IF NOT EXISTS "
                .concat("[SmsIdSetting]")
                .concat("(")
                .concat("[id] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[sms_id] VARCHAR(6)")
                .concat(",[default_sms] VARCHAR(3)")
                .concat(",[active] VARCHAR(3)")
                .concat(",[entry_date] DATETIME")
                .concat(",[remark] VARCHAR(200)")
                .concat(")")
                .concat(";");

        db.execSQL(qry23);


        String qry45 = "CREATE TABLE IF NOT EXISTS"
                .concat("[MessageFormat_Master]")
                .concat("(")
                .concat("[MessageFormat_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[Title] VARCHAR(2000)")
                .concat(",[MessageFormat] VARCHAR(10000)")
                .concat(",[Type] VARCHAR(20)") // Sale,purches,offer.
                .concat(",[Default] VARCHAR(3)") // YES,NO.
                .concat(",[Remark] VARCHAR(200)")
                .concat(")")
                .concat(";");

        db.execSQL(qry45);

        qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[SalesSMSLogsMaster]")
                .concat("(")
                .concat("[LogId] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[orderId] INTEGER")
                .concat(",[orderNo] VARCHAR(50)")
                .concat(",[mobileNo] VARCHAR(50) ")
                .concat(",[Customer_Name] VARCHAR(500)")
                .concat(",[Entry_Datetime] DATETIME")
                .concat(",[message] VARCHAR(5000)")
                .concat(",[invoice_attachment] VARCHAR(3)")
                .concat(",[Status] VARCHAR(50)")
                .concat(",[UtilizeType] VARCHAR(10)")
                .concat(",[SmsStatus] VARCHAR(50)")
                .concat(",[Type] VARCHAR(50)")
                .concat(",[Credit] INTEGER")
                .concat(",[SendSMSID] VARCHAR(50)")
                .concat(",[Remark] varchar(200)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);


        CreateTables createTables = new CreateTables();

        createTables.createFeatures(db);
        createTables.createServices(db);
        createTables.createServiceImages(db);
        createTables.createServiceFeatures(db);
        createTables.createEmployeeServiceSetting(db);
        createTables.createCombo(db);
        createTables.createComboService(db);

//        android.database.sqlite.SQLiteDatabase db1 =
//                context.openOrCreateDatabase(ClsGlobal.Database_Name, MODE_PRIVATE,
//                        null);

        ClsVendor objvendor = new ClsVendor(context);
        String where = " AND  [VENDOR_NAME] = "
                .concat("'")
                .concat("OTHER")
                .concat("' ");
        boolean exists = objvendor.checkExists2(where, db);
        if (!exists) {
            objvendor.setVendor_name("OTHER");
            objvendor.setActive("YES");
            ClsVendor.Insert1(objvendor, db);
        }
//        db.close();

        //--------------------------------- tbl_LayerItem_Master -------------------------------------//
        List<String> columns = ClsPurchaseMaster.getColumns(context, db);

        boolean exist = false;
        for (String current_Column : columns) {
            if (current_Column.equalsIgnoreCase("OPENING_STOCK")) {
                exist = true;
                break;
            }
        }

        if (!exist) {
            Log.e("onReceive", "inside UpdatePackageReceiver");
            String qryAdd_OPENING_STOCK = "ALTER TABLE [tbl_LayerItem_Master] " +
                    "ADD [OPENING_STOCK] DOUBLE";
            db.execSQL(qryAdd_OPENING_STOCK);
        }

        //--------------------------------- ClsPurchaseMaster -------------------------------------//
        List<String> columns_PurchaseMaster = getPurchaseMasterColumn(db);

        boolean addSms_Limit = false;
        for (String SaleTypeColumn : columns_PurchaseMaster) {
            if (SaleTypeColumn.equalsIgnoreCase("Sms_Limit")) {
                addSms_Limit = true;
                break;
            }
        }

        if (!addSms_Limit) {
            String qrySms_Limit = "ALTER TABLE [PurchaseMaster] ADD [Sms_Limit] INTEGER";
            db.execSQL(qrySms_Limit);
        }
        //--------------------------------- ClsPaymentMaster -------------------------------------//


        List<String> columns_paymentMaster = ClsPaymentMaster
                .getPaymentMasterColumn(context, db);

        boolean addReceiptNo_temp = false;
        for (String SaleTypeColumn : columns_paymentMaster) {
            if (SaleTypeColumn.equalsIgnoreCase("ReceiptNo_temp")) {
                addReceiptNo_temp = true;
                break;
            }
        }

        if (!addReceiptNo_temp) {
            String qryOrderNo_temp = "ALTER TABLE [PaymentMaster] ADD [ReceiptNo_temp] VARCHAR(25)";
            db.execSQL(qryOrderNo_temp);

            db.execSQL("update [PaymentMaster] set [ReceiptNo_temp] = [ReceiptNo]");
            db.execSQL("ALTER TABLE `PaymentMaster` RENAME COLUMN `ReceiptNo` to `ReceiptNo_Old`");
            db.execSQL("ALTER TABLE `PaymentMaster` RENAME COLUMN `ReceiptNo_temp` to `ReceiptNo`");
            db.execSQL("ALTER TABLE `PaymentMaster` RENAME COLUMN `ReceiptNo_Old` to `ReceiptNo_temp`");

        }


        //--------------------------------- CustomerMaster -------------------------------------//

        List<String> CustomerCompany_Name = ClsCustomerMaster
                .getCustomerMasterColumns(context, db);

        boolean exist_CustomerCompany_Name = false;
        for (String CustomerMasterColumns_Current : CustomerCompany_Name) {
            if (CustomerMasterColumns_Current.equalsIgnoreCase("Company_Name")) {
                exist_CustomerCompany_Name = true;
                break;
            }
        }

        if (!exist_CustomerCompany_Name) {
            Log.e("onReceive", "inside UpdatePackageReceiver");
            String qryAdd_Credit = "ALTER TABLE [CustomerMaster] ADD [Company_Name] VARCHAR(100)";
            db.execSQL(qryAdd_Credit);
        }

        List<String> CustomerMasterColumns = ClsCustomerMaster
                .getCustomerMasterColumns(context, db);

        boolean exist_DOB = false;
        for (String CustomerMasterColumns_Current : CustomerMasterColumns) {
            if (CustomerMasterColumns_Current.equalsIgnoreCase("DOB")) {
                exist_DOB = true;
                break;
            }
        }

        if (!exist_DOB) {

            String qryDOB = "ALTER TABLE [CustomerMaster] ADD [DOB] DATETIME";
            db.execSQL(qryDOB);
        }

        boolean exist_AnniversaryDate = false;
        for (String CustomerMasterColumns_Current : CustomerMasterColumns) {
            if (CustomerMasterColumns_Current.equalsIgnoreCase("AnniversaryDate")) {
                exist_AnniversaryDate = true;
                break;
            }
        }

        if (!exist_AnniversaryDate) {
            String qryAnniversaryDate = "ALTER TABLE [CustomerMaster] ADD [AnniversaryDate] DATETIME";
            db.execSQL(qryAnniversaryDate);
        }

        boolean exist_PanCard = false;
        for (String CustomerMasterColumns_Current : CustomerMasterColumns) {
            if (CustomerMasterColumns_Current.equalsIgnoreCase("PanCard")) {
                exist_PanCard = true;
                break;
            }
        }

        if (!exist_PanCard) {
            String qryPanCard = "ALTER TABLE [CustomerMaster] ADD [PanCard] VARCHAR (20)";
            db.execSQL(qryPanCard);
        }


        boolean exist_CustomerMasterColumns = false;
        for (String CustomerMasterColumns_Current : CustomerMasterColumns) {
            if (CustomerMasterColumns_Current.equalsIgnoreCase("Credit")) {
                exist_CustomerMasterColumns = true;
                break;
            }
        }

        if (!exist_CustomerMasterColumns) {
            Log.e("onReceive", "inside UpdatePackageReceiver");
            String qryAdd_Credit = "ALTER TABLE [CustomerMaster] ADD [Credit] DOUBLE";
            db.execSQL(qryAdd_Credit);
        }


        boolean exist_CustomerMasterEmail = false;
        for (String CustomerMasterColumns_Current : CustomerMasterColumns) {
            if (CustomerMasterColumns_Current.equalsIgnoreCase("Email")) {
                exist_CustomerMasterEmail = true;
                break;
            }
        }

        if (!exist_CustomerMasterEmail) {
            Log.e("onReceive", "inside UpdatePackageReceiver");
            String qryAdd_Email = "ALTER TABLE [CustomerMaster] ADD [Email] VARCHAR (100)";
            db.execSQL(qryAdd_Email);
        }


        boolean exist_CustomerMasterNote = false;
        for (String CustomerMasterColumns_Current : CustomerMasterColumns) {
            if (CustomerMasterColumns_Current.equalsIgnoreCase("Note")) {
                exist_CustomerMasterNote = true;
                break;
            }
        }

        if (!exist_CustomerMasterNote) {
            Log.e("onReceive", "inside UpdatePackageReceiver");
            String qryAdd_Note = "ALTER TABLE [CustomerMaster] ADD [Note] VARCHAR (500)";
            db.execSQL(qryAdd_Note);
        }


        boolean exist_CustomerMasterSave_Contact = false;
        for (String CustomerMasterColumns_Current : CustomerMasterColumns) {
            if (CustomerMasterColumns_Current.equalsIgnoreCase("Save_Contact")) {
                exist_CustomerMasterSave_Contact = true;
                break;
            }
        }

        if (!exist_CustomerMasterSave_Contact) {
            Log.e("onReceive", "inside UpdatePackageReceiver");
            String qryAdd_Save_Contact = "ALTER TABLE [CustomerMaster] ADD [Save_Contact] VARCHAR (3)";
            db.execSQL(qryAdd_Save_Contact);
        }


        // -------------------------------------------------------------------------------------------------

        //------------------------------------ InventoryOrderDetail ---------------------------------------//

        List<String> InventoryOrderDetailColumns = ClsInventoryOrderDetail
                .getInventoryOrderDetailColumns(context, db);

        Gson gson = new Gson();
        String jsonInString = gson.toJson(InventoryOrderDetailColumns);
        Log.d("--GSON--", "itemLayerValues:  " + jsonInString);

        boolean exist_InventoryOrderDetailColumns = false;
        for (String InventoryOrderDetailColumns_Current : InventoryOrderDetailColumns) {
            if (InventoryOrderDetailColumns_Current.equalsIgnoreCase("ItemComment")) {
                exist_InventoryOrderDetailColumns = true;
                break;
            }
        }

        if (!exist_InventoryOrderDetailColumns) {
            String qryAdd_ItemComment = "ALTER TABLE [InventoryOrderDetail] ADD [ItemComment] VARCHAR(200)";
            db.execSQL(qryAdd_ItemComment);
        }

/*
        boolean exist_UnitColumns = false;
        for (String InventoryOrderDetailColumns_Current : InventoryOrderDetailColumns) {
            if (InventoryOrderDetailColumns_Current.equalsIgnoreCase("Unit")) {
                exist_UnitColumns = true;
                break;
            }
        }

        if (!exist_UnitColumns) {
            String qryUnit = "ALTER TABLE [InventoryOrderDetail] ADD [Unit] VARCHAR(50)";
            db.execSQL(qryUnit);
        }*/


        boolean exist_OrderNo_temp = false;
        for (String InventoryOrderDetailColumns_Current : InventoryOrderDetailColumns) {
            if (InventoryOrderDetailColumns_Current.equalsIgnoreCase("OrderNo_temp")) {
                exist_OrderNo_temp = true;
                break;
            }
        }

        if (!exist_OrderNo_temp) {
            String qryOrderNo_temp = "ALTER TABLE [InventoryOrderDetail] ADD [OrderNo_temp] VARCHAR(25)";
            db.execSQL(qryOrderNo_temp);

//            db.execSQL("ALTER TABLE [InventoryOrderDetail] ADD [OrderNo_temp] VARCHAR(25)");
            db.execSQL("update [InventoryOrderDetail] set [OrderNo_temp] = [OrderNo]");
            db.execSQL("ALTER TABLE `InventoryOrderDetail` RENAME COLUMN `OrderNo` to `OrderNo_Old`");
            db.execSQL("ALTER TABLE `InventoryOrderDetail` RENAME COLUMN `OrderNo_temp` to `OrderNo`");

            db.execSQL("ALTER TABLE `InventoryOrderDetail` RENAME COLUMN `OrderNo_Old` to `OrderNo_temp`");

        }


        boolean exist_OrderID_temp = false;
        for (String InventoryOrderDetailColumns_Current : InventoryOrderDetailColumns) {
            if (InventoryOrderDetailColumns_Current.equalsIgnoreCase("OrderID_temp")) {
                exist_OrderID_temp = true;
                break;
            }
        }

        if (!exist_OrderID_temp) {
            String qrOrderID_temp = "ALTER TABLE [InventoryOrderDetail] ADD [OrderID_temp] VARCHAR(25)";
            db.execSQL(qrOrderID_temp);

//            db.execSQL("ALTER TABLE [InventoryOrderDetail] ADD [OrderID_temp] VARCHAR(25)");

            db.execSQL("update [InventoryOrderDetail] set [OrderID_temp] = [OrderID]");
            db.execSQL("ALTER TABLE `InventoryOrderDetail` RENAME COLUMN `OrderID` to `OrderID_Old`");
            db.execSQL("ALTER TABLE `InventoryOrderDetail` RENAME COLUMN `OrderID_temp` to `OrderID`");
            db.execSQL("ALTER TABLE `InventoryOrderDetail` RENAME COLUMN `OrderID_Old` to `OrderID_temp`");
        }


        //-------------------------------------- VendorMaster ---------------------------------//
        List<String> VendorMasterColumn = ClsVendor
                .getAddVendorMasterColumn(context, db);

        boolean exist_VendorMasterColumn = false;
        for (String VendorMasterColumn_Current : VendorMasterColumn) {
            if (VendorMasterColumn_Current.equalsIgnoreCase("OpeningStock")) {
                exist_VendorMasterColumn = true;
                break;
            }
        }

        if (!exist_VendorMasterColumn) {
            Log.e("onReceive", "inside UpdatePackageReceiver");
            String qryOpeningStock = "ALTER TABLE [VENDOR_MASTER] ADD [OpeningStock] DOUBLE";
            db.execSQL(qryOpeningStock);
        }


        //-------------------------------------- CustomerMaster ---------------------------------//
        List<String> CustomerMasterColumn = ClsCustomerMaster
                .getAddPaymentMasterColumn(context, db);

        boolean exist_CustomerMasterColumn = false;
        for (String CustomerMasterColumn_Current : CustomerMasterColumn) {
            if (CustomerMasterColumn_Current.equalsIgnoreCase("OpeningStock")) {
                exist_CustomerMasterColumn = true;
                break;
            }
        }

        if (!exist_CustomerMasterColumn) {
            Log.e("onReceive", "inside UpdatePackageReceiver");
            String qryOpeningStock = "ALTER TABLE [CustomerMaster] ADD [OpeningStock] DOUBLE";
            db.execSQL(qryOpeningStock);
        }


        //------------------------------------ InventoryOrderMaster ---------------------------------------//
        List<String> AddSaleType = ClsInventoryOrderMaster.
                getInventoryOrderMasterColumns(context, db);


        boolean addColumnSaleType = false;
        for (String SaleTypeColumn : AddSaleType) {
            if (SaleTypeColumn.equalsIgnoreCase("SaleType")) {
                addColumnSaleType = true;
                break;
            }
        }

        if (!addColumnSaleType) {
            String qryAdd_BillTo = "ALTER TABLE [InventoryOrderMaster] ADD [SaleType] VARCHAR(15)";
            db.execSQL(qryAdd_BillTo);
        }


        List<String> AddSaleReturnDiscount = ClsInventoryOrderMaster
                .getInventoryOrderMasterColumns(context, db);


        boolean addSaleReturnDiscount = false;
        for (String SaleTypeColumn : AddSaleReturnDiscount) {
            if (SaleTypeColumn.equalsIgnoreCase("SaleReturnDiscount")) {
                addSaleReturnDiscount = true;
                break;
            }
        }

        if (!addSaleReturnDiscount) {
            String qryAdd_BillTo = "ALTER TABLE [InventoryOrderMaster] ADD [SaleReturnDiscount] DOUBLE";
            db.execSQL(qryAdd_BillTo);
        }

        List<String> addQuotationIDQry = ClsInventoryOrderMaster
                .getInventoryOrderMasterColumns(context, db);


        boolean addQuotationID = false;
        for (String SaleTypeColumn : addQuotationIDQry) {
            if (SaleTypeColumn.equalsIgnoreCase("QuotationId")) {
                addQuotationID = true;
                break;
            }
        }

        if (!addQuotationID) {
            String qry_AddQuotationID = "ALTER TABLE [InventoryOrderMaster] ADD [QuotationId] INTEGER";
            db.execSQL(qry_AddQuotationID);
        }


        List<String> InventoryOrderMasterColumn = ClsInventoryOrderMaster
                .getAddInventoryOrderMasterColumn(context, db);

        boolean exist_InventoryOrderMasterColumn = false;
        for (String InventoryOrderMasterColumn_Current : InventoryOrderMasterColumn) {
            if (InventoryOrderMasterColumn_Current.equalsIgnoreCase("Different_Amount_mode")) {
                exist_InventoryOrderMasterColumn = true;
                break;
            }
        }

        if (!exist_InventoryOrderMasterColumn) {
            Log.e("onReceive", "inside UpdatePackageReceiver");
            String qryDifferent_Amount_mode = "ALTER TABLE [InventoryOrderMaster] ADD [Different_Amount_mode] VARCHAR(50)";
            db.execSQL(qryDifferent_Amount_mode);
        }

        List<String> InventoryOrderMasterNew = ClsInventoryOrderMaster
                .getInventoryOrderMasterColumns(context, db);

        boolean exist_Add_BillTo = false;
        for (String InventoryOrderDetailColumns_CurrentNew : InventoryOrderMasterNew) {
            if (InventoryOrderDetailColumns_CurrentNew.equalsIgnoreCase("BillTo")) {
                exist_Add_BillTo = true;
                break;
            }
        }

        if (!exist_Add_BillTo) {
            String qryAdd_BillTo = "ALTER TABLE [InventoryOrderMaster] ADD [BillTo] VARCHAR(8)";
            db.execSQL(qryAdd_BillTo);
        }

        boolean addOrderNo_temp = false;
        for (String SaleTypeColumn : AddSaleType) {
            if (SaleTypeColumn.equalsIgnoreCase("OrderNo_temp")) {
                addOrderNo_temp = true;
                break;
            }
        }

        if (!addOrderNo_temp) {
            String qryOrderNo_temp = "ALTER TABLE [InventoryOrderMaster] ADD [OrderNo_temp] VARCHAR(25)";
            db.execSQL(qryOrderNo_temp);

            db.execSQL("update [InventoryOrderMaster] set [OrderNo_temp] = [OrderNo]");
            db.execSQL("ALTER TABLE `InventoryOrderMaster` RENAME COLUMN `OrderNo` to `OrderNo_Old`");
            db.execSQL("ALTER TABLE `InventoryOrderMaster` RENAME COLUMN `OrderNo_temp` to `OrderNo`");
            db.execSQL("ALTER TABLE `InventoryOrderMaster` RENAME COLUMN `OrderNo_Old` to `OrderNo_temp`");

        }

        // ----------- add Sms_Limit --------------//
        boolean exist_Sms_Limit = false;
        for (String InventoryOrderDetailColumns_CurrentNew : InventoryOrderMasterNew) {
            if (InventoryOrderDetailColumns_CurrentNew.equalsIgnoreCase("Sms_Limit")) {
                exist_Sms_Limit = true;
                break;
            }
        }

        if (!exist_Sms_Limit) {
            String qrySms_Limit = "ALTER TABLE [InventoryOrderMaster] ADD [Sms_Limit] INTEGER";
            db.execSQL(qrySms_Limit);
        }


        //-------------------------------------- LayerItemMaster ---------------------------------------//
        List<String> LayerItemMaster = ClsLayerItemMaster.getItemMasterColumn(context, db);

        boolean LayerItemMasterNewColumn = false;
        for (String LayerItemMasterColumn : LayerItemMaster) {
            if (LayerItemMasterColumn.equalsIgnoreCase("HSN_SAC_CODE")) {
                LayerItemMasterNewColumn = true;
                break;
            }
        }

        if (!LayerItemMasterNewColumn) {
            String qryAddItemColumn = "ALTER TABLE [tbl_LayerItem_Master] ADD [HSN_SAC_CODE] VARCHAR(20)";
            db.execSQL(qryAddItemColumn);
        }

        //------------------------------------ InventoryOrderDetail ---------------------------------------//
        List<String> AddSaleRateWithoutTax = ClsInventoryOrderDetail.
                getInventoryOrderDetailColumns(context, db);

        boolean addSaleRateWithoutTax = false;
        boolean addSaveStatus = false;
        boolean addDiscount_per = false;
        boolean addDiscount_amt = false;
        for (String SaleTypeColumn : AddSaleRateWithoutTax) {
            if (SaleTypeColumn.equalsIgnoreCase("SaleRateWithoutTax")) {
                addSaleRateWithoutTax = true;
            }

            if (SaleTypeColumn.equalsIgnoreCase("SaveStatus")) {
                addSaveStatus = true;
            }

            if (SaleTypeColumn.equalsIgnoreCase("Discount_per")) {
                addDiscount_per = true;
            }

            if (SaleTypeColumn.equalsIgnoreCase("Discount_amt")) {
                addDiscount_amt = true;
            }

        }

        if (!addSaleRateWithoutTax) {
            String qryAdd_BillTo = "ALTER TABLE [InventoryOrderDetail] ADD [SaleRateWithoutTax] DOUBLE";
            db.execSQL(qryAdd_BillTo);
        }

        if (!addSaveStatus) {
            String qryWholesale = "ALTER TABLE [InventoryOrderDetail] ADD [SaveStatus] VARCHAR(3)";
            db.execSQL(qryWholesale);
        }


        if (!addDiscount_per) {
            String qryWholesale = "ALTER TABLE [InventoryOrderDetail] ADD [Discount_per] DOUBLE";
            db.execSQL(qryWholesale);
        }

        if (!addDiscount_amt) {
            String qryWholesale = "ALTER TABLE [InventoryOrderDetail] ADD [Discount_amt] DOUBLE";
            db.execSQL(qryWholesale);
        }


//------------------------------------ LayerItemMaster ---------------------------------------//
        List<String> LayerItemMasterNew = ClsLayerItemMaster.getItemMasterColumn(context, db);

        boolean WholeSaleColumn = false;
        boolean taxTypeColumn = false;
        for (String LayerItemMasterColumn : LayerItemMasterNew) {

            if (LayerItemMasterColumn.equalsIgnoreCase("WHOLESALE_RATE")) {
                WholeSaleColumn = true;
            }

            if (LayerItemMasterColumn.equalsIgnoreCase("TAX_TYPE")) {
                taxTypeColumn = true;
            }
        }

        if (!WholeSaleColumn) {
            String qryWholesale = "ALTER TABLE [tbl_LayerItem_Master] ADD [WHOLESALE_RATE] DOUBLE";
            db.execSQL(qryWholesale);
        }

        if (!taxTypeColumn) {
            String qryWholesale = "ALTER TABLE [tbl_LayerItem_Master] ADD [TAX_TYPE] VARCHAR(10)";
            db.execSQL(qryWholesale);
        }

        boolean AUTO_GENERATED_ITEM_CODE = false;
        for (String LayerItemMasterColumn : LayerItemMaster) {
            if (LayerItemMasterColumn.equalsIgnoreCase("AUTO_GENERATED_ITEM_CODE")) {
                AUTO_GENERATED_ITEM_CODE = true;
                break;
            }
        }

        if (!AUTO_GENERATED_ITEM_CODE) {
            String qryAUTO_GENERATED_ITEM_CODE = "ALTER TABLE [tbl_LayerItem_Master] ADD " +
                    "[AUTO_GENERATED_ITEM_CODE] VARCHAR(2500)";
            db.execSQL(qryAUTO_GENERATED_ITEM_CODE);
            db.execSQL("update [tbl_LayerItem_Master] set [AUTO_GENERATED_ITEM_CODE] = [ITEM_CODE]");
        }


//----------------------------------------- TermsColumn ---------------------------------------//
        List<String> TermsType = ClsTerms.getAddColumn(context, db);

        boolean TermTypeColumn = false;
        for (String _termsType : TermsType) {
            if (_termsType.equalsIgnoreCase("TERM_TYPE")) {
                TermTypeColumn = true;
                break;
            }
        }

        if (!TermTypeColumn) {
            String addTermsTypecolumn = "ALTER TABLE [tbl_Terms] ADD [TERM_TYPE] VARCHAR(50)";
            db.execSQL(addTermsTypecolumn);
        }

//------------------------------------------ VendorMaster ---------------------------------//
        List<String> BalanceTypeVendor = ClsVendor.getAddVendorMasterColumn(context, db);

        boolean BalanceTypeColumn = false;
        for (String VendorMasterColumn_Current : BalanceTypeVendor) {
            if (VendorMasterColumn_Current.equalsIgnoreCase("BalanceType")) {
                BalanceTypeColumn = true;
                break;
            }
        }

        if (!BalanceTypeColumn) {
            Log.e("onReceive", "inside UpdatePackageReceiver");
            String qryBalanceType = "ALTER TABLE [VENDOR_MASTER] ADD [BalanceType] VARCHAR(15)";
            db.execSQL(qryBalanceType);
        }


//------------------------------------------ PaymentMaster ---------------------------------//
        List<String> BalanceTypeCustomer = ClsPaymentMaster
                .getPaymentMasterColumn(context, db);

        boolean OrderIDColumnPaymentMaster = false;
        for (String OrderID_Current : BalanceTypeCustomer) {
            if (OrderID_Current.equalsIgnoreCase("OrderID")) {
                OrderIDColumnPaymentMaster = true;
                break;
            }
        }

        if (!OrderIDColumnPaymentMaster) {
            String qryBalanceType = "ALTER TABLE [PaymentMaster] ADD [OrderID] INT";
            db.execSQL(qryBalanceType);
        }

//------------------------------------ EmployeeDocuments ---------------------------------------//
        List<String> EmployeeDocument = ClsEmployeeDocuments
                .getItemMasterColumnEmp(context, db);

        boolean expDateColumn = false;
        boolean typeColumn = false;

        for (String empDocColumn : EmployeeDocument) {

            if (empDocColumn.equalsIgnoreCase("EXP_DATE")) {
                expDateColumn = true;
            }

            if (empDocColumn.equalsIgnoreCase("TYPE")) {
                typeColumn = true;
            }
        }

        if (!expDateColumn) {
            String qryWholesale = "ALTER TABLE [EmployeeDocument] ADD [EXP_DATE] DOUBLE";
            db.execSQL(qryWholesale);
        }

        if (!typeColumn) {
            String qryWholesale = "ALTER TABLE [EmployeeDocument] ADD [TYPE] VARCHAR(10)";
            db.execSQL(qryWholesale);
        }

        List<String> columns_SalesSMSLogsMaster =
                ClsSMSLogs.getSalesSMSLogsMasterColumn(context, db);

        boolean UtilizeType = false;
        for (String UtilizeType_Current : columns_SalesSMSLogsMaster) {
            if (UtilizeType_Current.equalsIgnoreCase("UtilizeType")) {
                UtilizeType = true;
                break;
            }
        }

        if (!UtilizeType) {
            String qryUtilizeType = "ALTER TABLE [SalesSMSLogsMaster]" +
                    " ADD [UtilizeType] VARCHAR(10)";
            db.execSQL(qryUtilizeType);
        }

        db.close();
    }

    // If targetLocation does not exist, it will be created.
    // If targetLocation does not exist, it will be created.
    public static void copyDirectory(File sourceLocation, File targetLocation)
            throws IOException {

        // If you add new sharedpreferences file to project
        // then you have add then file name to settingsFiles ArrayList.
        ArrayList<String> settingsFiles = new ArrayList<>();
        settingsFiles.add("AutoBackUpSettings.xml");
        settingsFiles.add("Email_Settings.xml");
        settingsFiles.add("EmailConfiguration.xml");
        settingsFiles.add("Language_Settings.xml");
        settingsFiles.add("LoginDetails.xml");
        settingsFiles.add("TimePref.xml");
        settingsFiles.add("UpdateItemDefaultTax.xml");
        settingsFiles.add("UpdateVendor.xml");
//        settingsFiles.add("loginPrefs.xml");
        settingsFiles.add("MyPerfernces.xml");
        settingsFiles.add("printerConfig.xml");
        settingsFiles.add("MyPref.xml");
        settingsFiles.add("Bill No Format Apply Tax.xml");
        settingsFiles.add("Bill No Format Tax Not Apply.xml");

        Log.e("ChekFile", "copyDirectory:- ");
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists() && !targetLocation.mkdirs()) {
//                throw new IOException("Cannot create dir " + targetLocation.getAbsolutePath());
                targetLocation.mkdir();
            }


            List<String> children = new ArrayList<>();
            children.addAll(Arrays.asList(sourceLocation.list()));

            children.remove("androidx.work.util.id.xml");
            children.remove("androidx.work.util.id.xml.bak");
            children.remove("LoginDetails.xml.bak");
            children.remove("rzp_preference_private.xml");
            children.remove("com.demo.nspl.retailpos_preferences.xml");
            children.remove("rzp_preferences_storage_bridge.xml");
            children.remove("rzp_preference_public.xml");

            Log.e("children", "children: = " + children.toString());

            for (String getFileName : children) {
                if (settingsFiles.contains(getFileName.trim())) {
                    Log.e("children", "getFileName: = " + getFileName);
                    copyDirectory(new File(sourceLocation, getFileName),
                            new File(targetLocation, getFileName));
                }
            }

        } else {

            // make sure the directory we plan to store the recording in exists
            File directory = targetLocation.getParentFile();
            if (directory != null && !directory.exists() && !directory.mkdirs()) {
                throw new IOException("c create dir " + directory.getAbsolutePath());
            }

            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

    public static void writeSharedPreferencesFile(List<String> filepath, String storefilepath,
                                                  Context context) {

        int count = 0;
        int READ_BLOCK_SIZE = 300;

        for (String getFilePath : filepath) {

            Log.e("SharedPreferencesFile", "" + count++);
            Log.e("SharedPreferencesFile", "Names:-" + getFilePath);

            try {

                File _saveLocation = Environment.getExternalStorageDirectory();
                File root = new File(_saveLocation.getAbsolutePath() + "/" + ClsGlobal.AppFolderName + "/SettingsBackup/");
                if (!root.exists()) {
                    root.mkdirs();
                }

                String currentDBPath = ClsGlobal.SharedPreferencesPathBackupFolder.concat(getFilePath);

                Log.e("SharedPreferencesFile", "Current file:-" + currentDBPath);
                File file_To_Write = new File(SharedPreferencesPath + "/" + getFilePath);
                file_To_Write.createNewFile();

                FileInputStream fileIn = new FileInputStream(currentDBPath);
                InputStreamReader InputRead = new InputStreamReader(fileIn);

                char[] inputBuffer = new char[READ_BLOCK_SIZE];
                String s = "";
                int charRead;

                while ((charRead = InputRead.read(inputBuffer)) > 0) {
                    // char to string conversion
                    String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                    s += readstring;
                }

                Log.e("SharedPreferencesFile", "Input Stream str:-" + s);

                FileOutputStream fileout = new FileOutputStream(file_To_Write.getAbsoluteFile());
//                        openFileOutput("mytextfile.txt", MODE_PRIVATE);
                OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
                outputWriter.write(s);
                outputWriter.close();

                InputRead.close();


                String[] files = {SharedPreferencesPath};
                MediaScannerConnection.scanFile(context,
                        files,
                        null,
                        (path, uri) -> {
                            // scanned path and uri
                            Log.e("MediaScannerConnection", "Path" + path);
                            Log.e("MediaScannerConnection", "uri" + uri);

                        });


            } catch (Exception e) {
                Log.e("e", e.getMessage());
            }

        }

    }

    public static void copyFileTemp(String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            in = new FileInputStream(inputFile);
            out = new FileOutputStream(outputPath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        } catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }


    /**
     * Generate backup file name according to mode.
     *
     * @param mode ex:- LocalBkp,ManualCloudBkp,AutoCloudBkp,AutoLocalBkp,DbBackUp.
     * @return FileName.
     */
    public static String generateBackUpFileName(String mode) {

        if (mode.equalsIgnoreCase("LocalBkp")) {
            return "ftouchPOSLocBKP " + getEntryDateFormat(getCurruntDateTime()) + ".zip";
        } else if (mode.equalsIgnoreCase("ManualCloudBkp")) {
            return "ftouchPOSManualCloudBKP " + getEntryDateFormat(getCurruntDateTime()) + ".zip";
        } else if (mode.equalsIgnoreCase("AutoCloudBkp")) {
            return "ftouchPOSAutoCloudBKP " + getEntryDateFormat(getCurruntDateTime()) + ".zip";
        } else if (mode.equalsIgnoreCase("AutoLocalBkp")) {
            return "ftouchPOSAutoLocalBKP " + getEntryDateFormat(getCurruntDateTime()) + ".zip";
        } else if (mode.equalsIgnoreCase("DbBackUp")) {
            return "ftouchPOSEmergBkp " + getEntryDateFormat(getCurruntDateTime()) + ".zip";
        } else if (mode.equalsIgnoreCase("AutoGDriveBkp")) {
            return "ftouchPOSAutoGDriveBKP " + getEntryDateFormat(getCurruntDateTime()) + ".zip";
        } else if (mode.equalsIgnoreCase("ManualGDriveBkp")) {
            return "ftouchPOSManualGDriveBKP " + getEntryDateFormat(getCurruntDateTime()) + ".zip";
        }

        return "ShapBkp.zip";
    }


    /**
     * Delete UnWanted Backup's from fTouchPOSLocalBkp folder.
     * after taking backup.
     */
    public static void deleteUnWanted_Bkp(File dir) {
        try {
            Gson gson = new Gson();
            String jsonInString = gson.toJson(dir.listFiles());
            Log.d("--customerReport--", "listFiles: " + jsonInString);

            for (File file : dir.listFiles()) {
                Log.d("--customerReport--", "file name: " + file.getName());

                if (!file.getName().contains("ftouchPOSLocBKP")) {
                    Log.e("--customerReport--", "not LocBkp");
                    Log.e("--customerReport--", "Delete" + file.delete());

                }

            }
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }

    /**
     * Create OneTimeWorkRequest.
     */
    public static void Create_OneTimeWorkRequest(Class<? extends ListenableWorker>
                                                         workerClass,
                                                 String UniqueWorkName,
                                                 String Policy,
                                                 Data data,
                                                 Constraints constraints) {
        OneTimeWorkRequest oneTimeWorkRequest;
        Log.e("OneTimeWorkRequest", "Create_OneTimeWorkRequest");
        // Check if worker exists or not
        // if not exists than only create new worker.
        if (!ClsGlobal.isWorkScheduled(UniqueWorkName)) {

            if (data != null && constraints != null) {
                Log.e("OneTimeWorkRequest", "data != null && constraints != null");
                oneTimeWorkRequest = new OneTimeWorkRequest
                        .Builder(workerClass)
                        .setInputData(data)
                        .setConstraints(constraints)
                        .build();
            } else if (data != null) {
                Log.e("OneTimeWorkRequest", "data != null");
                oneTimeWorkRequest = new OneTimeWorkRequest
                        .Builder(workerClass)
                        .setInputData(data)
                        .build();
            } else if (constraints != null) {
                Log.e("OneTimeWorkRequest", "constraints  != null");
                oneTimeWorkRequest = new OneTimeWorkRequest
                        .Builder(workerClass)
                        .setConstraints(constraints)
                        .build();
            } else {
                Log.e("OneTimeWorkRequest", "else");
                oneTimeWorkRequest = new OneTimeWorkRequest
                        .Builder(workerClass)
                        .build();
            }

            if (Policy.equalsIgnoreCase("REPLACE")) {
                Log.e("OneTimeWorkRequest", "REPLACE");
                WorkManager.getInstance().enqueueUniqueWork(
                        UniqueWorkName,
                        ExistingWorkPolicy.REPLACE,
                        oneTimeWorkRequest);
            } else if (Policy.equalsIgnoreCase("KEEP")) {
                Log.e("OneTimeWorkRequest", "KEEP");
                WorkManager.getInstance().enqueueUniqueWork(
                        UniqueWorkName,
                        ExistingWorkPolicy.KEEP,
                        oneTimeWorkRequest);
            }
        } else {
            Log.e("OneTimeWorkRequest", "Create_OneTimeWorkRequest");
        }


    }

    public static void deleteAllFile(Context context) {
        try {

            File dir = new File(context.getFilesDir().getParent() + "/shared_prefs/");
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                // clear each preference file
                context.getSharedPreferences(children[i].replace(".xml", ""),
                        Context.MODE_PRIVATE).edit().clear().apply();
                //delete the file
                new File(dir, children[i]).delete();
            }

        } catch (Exception e) {
            Log.e("Exception", "Exception:- " + e.getMessage());
        }

//        Log.e("deleteResult","deleteResult:- "+ deleteResult);
    }


    /**
     * Delete All Files from folder.
     *
     * @param dir
     */
    public static void deleteAllFiles_from_folder(File dir) {
        try {
            for (File file : dir.listFiles()) {
                if (file.isDirectory())
                    deleteAllFiles_from_folder(file);
                file.delete();
            }
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }

    public static void setDefaultItem_Unit(Context context, String defaultUnit) {
        mPreferences = context.getSharedPreferences(mPreferncesName, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString("Default_item_unit", defaultUnit);
        editor.apply();
    }

    public static String getDefaultItem_Unit(Context context) {
        mPreferences = context.getSharedPreferences(mPreferncesName, MODE_PRIVATE);

        return mPreferences.getString("Default_item_unit", "") != null
                && !mPreferences.getString("Default_item_unit", "")
                .equalsIgnoreCase("") ?
                mPreferences.getString("Default_item_unit", "") : "";
    }


    public static void ShowBkp_Remainder(Context context) {
        SharedPreferences mPreferences = context.getSharedPreferences(mPreferncesName, MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits")
        SharedPreferences.Editor editor = mPreferences.edit();

        int get_Day = mPreferences.getInt("Day", 0);

        if (get_Day == 0) {
            // if user have frease install app don't ask for backup Remainder dialog.
            // just save current day.
            editor.putInt("Day", Integer.parseInt(get_Current_Day_Month_Yearly("Daily")));
            editor.apply();
        } else {
            if (!get_Current_Day_Month_Yearly("Daily").equals(String.valueOf(get_Day))) {

                AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                @SuppressLint("InflateParams") View mView = inflater.inflate(R.layout.backup_reminder, null);
                final Button btn_bkp_cloud = mView.findViewById(R.id.br_backup_to_cloud);
                final Button btn_bkp_local = mView.findViewById(R.id.br_backup_to_local);
                mAlertDialogBuilder.setView(mView);
                mAlertDialogBuilder.setCancelable(false);
                final AlertDialog dialog = mAlertDialogBuilder.create();
                dialog.show();

                btn_bkp_cloud.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                        Data data = new Data.Builder()
                                .putString("Mode", "Manual Backup Cloud From Remainder")
                                .build();

                        Create_OneTimeWorkRequest(AutoBackUpTask.class, ClsGlobal.AppPackageName
                                        .concat(".ManualBkpRemainder"),
                                "KEEP", data, null);
                        editor.putInt("Day", Integer.parseInt(get_Current_Day_Month_Yearly("Daily")));
                        editor.apply();
                    }
                });

                btn_bkp_local.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                        Data data = new Data.Builder()
                                .putString("Mode", "Manual Local Backup From Remainder")
                                .build();


                        Create_OneTimeWorkRequest(AutoLocalBackupTask.class, ClsGlobal.AppPackageName
                                        .concat(".ManualLocalBkpRemainder"),
                                "KEEP", data, null);

                        editor.putInt("Day", Integer.parseInt(get_Current_Day_Month_Yearly("Daily")));
                        editor.apply();
                    }
                });
            }
        }


    }

    public static List<File> listFilesOldestFirst(final File[] list_files) throws IOException {
        final List<File> files = Arrays.asList(list_files);
        final Map<File, Long> constantLastModifiedTimes = new HashMap<File, Long>();
        for (final File f : files) {
            constantLastModifiedTimes.put(f, f.lastModified());
        }
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(final File f1, final File f2) {
                return constantLastModifiedTimes.get(f1).compareTo(constantLastModifiedTimes.get(f2));
            }
        });
        return files;
    }

    public static void appendSmsLog(String text) {

        File log = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/" + ftouchLogs_Folder + "/");

        if (!log.exists()) {
            log.mkdir();
        }

        File logFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                + ftouchLogs_Folder + "/Sales SMS Logs.txt");

        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                Log.e("appendLog", e.getMessage());
                e.printStackTrace();
            }
        }

        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            Log.e("appendLog", e.getMessage());
            e.printStackTrace();
        }
    }

    public static void appendLog(String text) {

        File log = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/" + ftouchLogs_Folder + "/");

        if (!log.exists()) {
            log.mkdir();
        }

        File logFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                + ftouchLogs_Folder + "/dblog file.txt");

        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                Log.e("appendLog", e.getMessage());
                e.printStackTrace();
            }
        }

        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            Log.e("appendLog", e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Remove char if more then 30 and replace ... from char 28 to 30
     * and char above 30 remove all.
     *
     * @param businessName
     * @return
     */
    public static String getFinalBusinessName(String businessName) {
//        System.out.println(businessName.length());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(businessName);
        if (businessName != null) {
            if (businessName.length() > 30) {
                stringBuilder.setCharAt(28, '.');
                stringBuilder.setCharAt(29, '.');
                stringBuilder.setCharAt(30, '.');
                stringBuilder.delete(31, businessName.length());
                return stringBuilder.toString();
            } else {
                return stringBuilder.toString();
            }
        }
        return stringBuilder.toString();

    }

    public static String getQuotExpDate(Date _date, int _daysToAdd) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(_date);
        calendar.add(Calendar.DAY_OF_MONTH, _daysToAdd);

        _date = calendar.getTime();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        return sdf1.format(_date);
    }


    public static String getOnlyDate(String e_Date) {

        final String OUT_FORMAT = "dd/MM/yyyy";
        final String IN_FORMAT = "dd/MM/yyyy hh:mm aa";

        try {
            if (e_Date != null && !e_Date.isEmpty() && e_Date != "") {
                Date date = new SimpleDateFormat(IN_FORMAT, Locale.ENGLISH).parse(e_Date);
                DateFormat formatter = new SimpleDateFormat(OUT_FORMAT, Locale.getDefault());
                e_Date = formatter.format(date.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return e_Date;
    }


    static StringBuilder getQuotationTemplate(Context context) {
        StringBuilder mailBody = new StringBuilder();

        try {
//            InputStream json = context.getAssets().open("Quotation.html");
//            InputStream json = context.getAssets().open("QuotationTemplate.txt");
            InputStream json = context.getAssets().open("V2QuotationTemplate.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(json));
            String str = "";

            while ((str = in.readLine()) != null) {
                mailBody.append(str);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mailBody;
    }


    @SuppressLint("WrongConstant")
    public static int getNextOrderNoForQuotation(Context context) {
        int NextOrderNo = 0;
        try {
            android.database.sqlite.SQLiteDatabase db
                    = context.openOrCreateDatabase(ClsGlobal.Database_Name, Context.MODE_APPEND, null);
            String qry1 = "Select MAX(IFNULL([QuotationNo],0)) as [MaxOrderNo] from [QuotationMaster];";
            Cursor cur1 = db.rawQuery(qry1, null);

            if (cur1.getCount() == 1) {
                cur1.moveToFirst();
                NextOrderNo = cur1.getInt(cur1.getColumnIndex("MaxOrderNo"));
            }
        } catch (Exception e) {
            e.getMessage();
            Log.e("MaxOrderNoSdf", "GetList" + e.getMessage());
        }
        return NextOrderNo + 1;
    }


    public static String getSmsReceiver(String type, Context context) {
        mPreferences = context.getSharedPreferences(mPreferncesName, MODE_PRIVATE);
        if (type.equalsIgnoreCase("Sales")) {
            return mPreferences.getString("Sales Sms Receiver", "") == null ?
                    "" : mPreferences.getString("Sales Sms Receiver", "");

        } else if (type.equalsIgnoreCase("Quotation")) {
            return mPreferences.getString("Quotation Sms Receiver", "") == null ?
                    "" : mPreferences.getString("Quotation Sms Receiver", "");
        }

        return "";

    }

    // Call Check Sms Status.
    public static void CheckSalesSmsStatus(Context context, android.database.sqlite.SQLiteDatabase db) {
//        android.database.sqlite.SQLiteDatabase db = context.openOrCreateDatabase(ClsGlobal.Database_Name,
//                Context.MODE_PRIVATE, null);
        List<String> pending_SendSmsIds = ClsSMSLogs.getPendingSendSmsId(db);

        Log.e(TAG, "pending_SendSmsIds:- " + pending_SendSmsIds.size());

        ClsUserInfo clsUserInfo = ClsGlobal.getUserInfo(context);

        for (String sendSmsId : pending_SendSmsIds) {
            List<ClsSMSLogs> pendingSalesSmsList =
                    ClsSMSLogs.getList("AND [Status] = 'Pending' AND [SendSMSID] = '"
                            + sendSmsId + "'", "", db);

            InterfaceSmsStatus interfaceSmsStatus = ApiClient.getDemoInstance()
                    .create(InterfaceSmsStatus.class);

            ClsSmsStatusPrams clsSmsStatusPrams = new ClsSmsStatusPrams();

//            clsSmsStatusPrams.setCustomerCode("CTA001");
            clsSmsStatusPrams.setCustomerCode(clsUserInfo.getUserId());
            clsSmsStatusPrams.setSendSMSID(sendSmsId);


            String mobile_list = TextUtils.join(", ", StreamSupport.stream(pendingSalesSmsList)
                    .map(ClsSMSLogs::getMobileNo).collect(Collectors.toList()));

//            Log.e(TAG, "--listOfmobile:- " + mobile_list);
            clsSmsStatusPrams.setListOfMobileNumbers(mobile_list);

            clsSmsStatusPrams.setType("single");

            Gson gsonOrder = new Gson();
            String response1 = gsonOrder.toJson(clsSmsStatusPrams);
            Log.e(TAG, "--clsSmsStatusPrams:- " + response1);

            Call<ClsCheckSmsStatusResponse> checkSmsStatus =
                    interfaceSmsStatus.getSmsStatus(clsSmsStatusPrams);

            checkSmsStatus.enqueue(new Callback<ClsCheckSmsStatusResponse>() {
                @Override
                public void onResponse(Call<ClsCheckSmsStatusResponse> call,
                                       Response<ClsCheckSmsStatusResponse> response) {
                    timer = false;


                    if (response.body() != null) {
//                        Log.e(TAG, "getMessageSales: " + response.body().getMessage());
//                    Log.e(TAG, "getSuccess: " + response.body().getSuccess());
//                    Log.e(TAG, "abcd: ");


                        List<ClsMobileStatus> listOfMobile = response.body().getList();

                        Gson gsonOrder2 = new Gson();
                        String response11 = gsonOrder2.toJson(listOfMobile);
                        Log.e(TAG, "--listOfMobile:- " + response11);
                        Log.e(TAG, "--listOfMobile:- " + listOfMobile.size());

                        if (listOfMobile != null && listOfMobile.size() > 0) {

                            Gson gsonOrder = new Gson();
                            String response1 = gsonOrder.toJson(listOfMobile);
                            Log.e(TAG, "--listOfMobile:- " + response1);

                            Gson gsonOrder1 = new Gson();
                            String response12 = gsonOrder1.toJson(response.body());
                            Log.e(TAG, "--response.body():- " + response12);

                            for (ClsSMSLogs _smsOBJ : pendingSalesSmsList) {

                                if (_smsOBJ.getStatus().equalsIgnoreCase("Pending")) {

                                    for (ClsMobileStatus _smsStatus : listOfMobile) {
                                        if (_smsOBJ.getMobileNo().equalsIgnoreCase(_smsStatus.getMobileNo())) {

                                            _smsOBJ.setStatus(_smsStatus.getStatus());

                                            _smsOBJ.setRemark(_smsStatus.getStatus() == null
                                                    ? "Pending" : _smsStatus.getStatus());
                                            _smsOBJ.setCredit(_smsStatus.getCreditUsed());

                                            _smsOBJ.setUtilizeType(_smsStatus.getUtilizeType());

                                            pendingSalesSmsList.set(pendingSalesSmsList.indexOf(_smsOBJ), _smsOBJ);
                                            break;
                                        }
                                    }
                                }


                            }

                            android.database.sqlite.SQLiteDatabase db = context.openOrCreateDatabase(ClsGlobal.Database_Name,
                                    Context.MODE_PRIVATE, null);

                            //update database status as below
                            //fore each llop
                            List<String> _updateQryList = new ArrayList<>();
                            for (ClsSMSLogs _smsOBJ : pendingSalesSmsList) {
                                Log.e(TAG, "--getStatus:- " + _smsOBJ.getStatus());


                                String qry = "UPDATE [SalesSMSLogsMaster] "
                                        .concat(" set ")
                                        .concat(" [Status] = ")
                                        .concat("'")
                                        .concat(_smsOBJ.getStatus() == null
                                                ? "Pending" : _smsOBJ.getStatus())
                                        .concat("'")

                                        .concat(" ,[UtilizeType] = ")
                                        .concat("'")
                                        .concat(_smsOBJ.getUtilizeType())
                                        .concat("'")


                                        .concat(" ,[Credit] = ")
                                        .concat("'")
                                        .concat(String.valueOf(_smsOBJ.getCredit()))
                                        .concat("'")

                                        .concat(" ,[Remark] = ")
                                        .concat("'")
                                        .concat(_smsOBJ.getRemark()
                                                .replace("'", "''"))
                                        .concat("'")


                                        .concat(" WHERE ")
                                        .concat(" [mobileNo] = ")
                                        .concat("'")
                                        .concat(_smsOBJ.getMobileNo())
                                        .concat("'")

                                        .concat(" AND ")

                                        .concat(" [LogId] = ")
                                        .concat("'")
                                        .concat(String.valueOf(_smsOBJ.getLogId()))
                                        .concat("'")
                                        .concat(" AND ")

                                        .concat(" [Status] = 'Pending' ");

//                        _updateQryList.add(qry);

//                        db.execSQL(qry);

                                SQLiteStatement statement = db.compileStatement(qry);
                                int result = statement.executeUpdateDelete();
                            }
//                            db.close();
                        }
                    }


                }

                @Override
                public void onFailure(Call<ClsCheckSmsStatusResponse> call, Throwable t) {
                    timer = false;

                }
            });

            // to wait for the response from retrofit.
            while (timer) {

//                Log.e(TAG, "--response:-" + timer);
                if (!timer) {

                    break;
                }
            }

        }
//        db.close();

    }

    // Check Bulk Sms Status.
    public static void CheckBulkSmsStatus(Context context, android.database.sqlite.SQLiteDatabase db) {

//        android.database.sqlite.SQLiteDatabase db = context.openOrCreateDatabase(ClsGlobal.Database_Name,
//                Context.MODE_PRIVATE, null);

        List<String> pending_ServerBulkID = ClsBulkSMSLog.getPendingServerBulkID(db);
        pending_ServerBulkID.remove("");

        Gson gsonOrder = new Gson();
        String response1 = gsonOrder.toJson(pending_ServerBulkID);
        Log.e(TAG, "--pending_ServerBulkID:- " + response1);

        for (String sendSmsId : pending_ServerBulkID) {

            List<ClsBulkSMSLog> pendingSmsList = ClsBulkSMSLog.getList(" " +
                            "AND [Status] = 'Pending' or [Status] = 'Sending'" +
                            "AND [serverBulkID] ='" + sendSmsId + "'"
                    , "", db);


            Gson gsonOrderpendingSmsList = new Gson();
            String response1_pendingSmsList = gsonOrderpendingSmsList.toJson(pending_ServerBulkID);
            Log.e(TAG, "--pendingSmsList:- " + response1_pendingSmsList);

            InterfaceSmsStatus interfaceSmsStatus = ApiClient.getDemoInstance()
                    .create(InterfaceSmsStatus.class);

            ClsSmsStatusPrams clsSmsStatusPrams = new ClsSmsStatusPrams();

            ClsUserInfo clsUserInfo = ClsGlobal.getUserInfo(context);


//            clsSmsStatusPrams.setCustomerCode("CTA001");
            clsSmsStatusPrams.setCustomerCode(clsUserInfo.getUserId());
            clsSmsStatusPrams.setSendSMSID(sendSmsId);

            String mobile_list = TextUtils.join(", ", StreamSupport.stream(pendingSmsList)
                    .map(ClsBulkSMSLog::getMobile).collect(Collectors.toList()));

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            clsSmsStatusPrams.setListOfMobileNumbers(mobile_list);

            clsSmsStatusPrams.setType("single");

            Call<ClsCheckSmsStatusResponse> checkSmsStatus =
                    interfaceSmsStatus.getSmsStatus(clsSmsStatusPrams);

            Log.e("--URL--", "interfaceDesignation: " + interfaceSmsStatus.toString());

            Log.e("--URL--", "************************  before call : " + checkSmsStatus.request().url());

            Gson gsonOrderResponce = new Gson();
            String response2 = gsonOrderResponce.toJson(clsSmsStatusPrams);
            Log.e(TAG, "--Check Status Prams:- " + response2);

            checkSmsStatus.enqueue(new Callback<ClsCheckSmsStatusResponse>() {
                @Override
                public void onResponse(Call<ClsCheckSmsStatusResponse> call,
                                       Response<ClsCheckSmsStatusResponse> response) {

                    timer = false;

                    Gson gsonOrderResponce = new Gson();
                    String response2 = gsonOrderResponce.toJson(response.body());
                    Log.e(TAG, "--response:- " + response2);

                    if (response.body() != null) {

                        List<ClsMobileStatus> listOfMobile = response.body().getList();

                        if (listOfMobile != null && listOfMobile.size() > 0) {
                            for (ClsBulkSMSLog _smsOBJ : pendingSmsList) {

                                if (_smsOBJ.getStatus().equalsIgnoreCase("Pending") ||
                                        _smsOBJ.getStatus().equalsIgnoreCase("Sending")) {

                                    for (ClsMobileStatus _smsStatus : listOfMobile) {
                                        if (_smsOBJ.getMobile().equalsIgnoreCase(_smsStatus.getMobileNo())) {
                                            _smsOBJ.setStatus(_smsStatus.getStatus());
                                            _smsOBJ.setRemark(_smsStatus.getStatus());
                                            _smsOBJ.setCreditCount(_smsStatus.getCreditUsed());
                                            pendingSmsList.set(pendingSmsList.indexOf(_smsOBJ), _smsOBJ);
                                            break;
                                        }
                                    }
                                }


                            }


                            android.database.sqlite.SQLiteDatabase db = context.openOrCreateDatabase(ClsGlobal.Database_Name,
                                    Context.MODE_PRIVATE, null);


                            //update database status as below
                            //fore each loop
                            List<String> _updateQryList = new ArrayList<>();
                            for (ClsBulkSMSLog _smsOBJ : pendingSmsList) {
                                String qry = "UPDATE [SMSLog] "
                                        .concat(" set ")
                                        .concat(" [Status] = ")
                                        .concat("'")
                                        .concat(_smsOBJ.getStatus() == null ?
                                                "Pending" : _smsOBJ.getStatus())
                                        .concat("'")

                                        .concat(" ,[Remark] = ")
                                        .concat("'")
                                        .concat(_smsOBJ.getRemark().replace("'", "''"))
                                        .concat("'")

                                        .concat(" ,[CreditCount] = ")
                                        .concat("'")
                                        .concat(String.valueOf(_smsOBJ.getCreditCount()))
                                        .concat("'")

                                        .concat(" WHERE ")
                                        .concat(" [Mobile] = ")
                                        .concat("'")
                                        .concat(_smsOBJ.getMobile())
                                        .concat("'")

                                        .concat(" AND ")

                                        .concat(" [logID] = ")
                                        .concat("'")
                                        .concat(String.valueOf(_smsOBJ.getLogID()))
                                        .concat("'");

                                //.concat(" AND ").concat("( [Status] = 'Pending' " +"or [Status] = 'Sending' )");


                                SQLiteStatement statement = db.compileStatement(qry);
                                int result = statement.executeUpdateDelete();
                                Log.e(TAG, "result: " + result);

                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ClsCheckSmsStatusResponse> call, Throwable t) {
                    timer = false;
                }
            });
            // to wait for the response from retrofit.
            while (timer) {
                if (!timer) {
                    break;
                }
            }
        }
//        db.close();
    }

    @SuppressLint("CheckResult")
    public static void sendQuotationToWtsApp(Context context,
                                             String get_Phone_No,
                                             List<ClsQuotationOrderDetail> list,
                                             ClsQuotationMaster getCurrentObj,
                                             String mode,
                                             String _validUpTo,
                                             String _OrderNo
            , String getWhatsAppDefaultApp) {

        boolean isWtsAppInstalled = ClsGlobal.whatsappInstalledOrNot("com.whatsapp", context);
        boolean isWtsAppBusinessInstalled = ClsGlobal.whatsappInstalledOrNot("com.whatsapp.w4b", context);

        if (isWtsAppBusinessInstalled || isWtsAppInstalled) {
            current_OrderNo = _OrderNo;

            Observable.just(getQuotationWhatsAppString(list,
                    getCurrentObj, context, mode, _validUpTo))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(messageWhatsApp -> {

                        Log.e("message", messageWhatsApp);
                        try {
                            String url = "https://api.whatsapp.com/send?phone=" +
                                    get_Phone_No + "&text=" + URLEncoder.encode(messageWhatsApp, "UTF-8");

                            Intent sendIntent = new Intent("android.intent.action.MAIN");
                            sendIntent.setAction(Intent.ACTION_VIEW);

                            if (!getWhatsAppDefaultApp.equalsIgnoreCase("")
                                    && getWhatsAppDefaultApp.equalsIgnoreCase("WhatsApp")) {
                                sendIntent.setPackage("com.whatsapp");
                                Log.e("message", "WhatsApp");
                            } else if (!getWhatsAppDefaultApp.equalsIgnoreCase("")
                                    && getWhatsAppDefaultApp.equalsIgnoreCase("Business WhatsApp")) {
                                sendIntent.setPackage("com.whatsapp.w4b");
                                Log.e("message", "Business WhatsApp");
                            }

                            sendIntent.setData(Uri.parse(url));
                            context.startActivity(sendIntent);

                        } catch (Exception e) {
                            if (!getWhatsAppDefaultApp.equalsIgnoreCase("")
                                    && getWhatsAppDefaultApp.equalsIgnoreCase("WhatsApp")) {
                                Toast.makeText(context, "WhatsApp Not Install!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "Business WhatsApp Not Install!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(context, "WhatsApp Not Install!", Toast.LENGTH_SHORT).show();
        }

    }

    public static String getQuotationWhatsAppString(List<ClsQuotationOrderDetail> list,
                                                    ClsQuotationMaster getCurrentObj,
                                                    Context context,
                                                    String _validUpTo,
                                                    String mode) {
        List<String> items = new ArrayList<>();

        double taxAmt1 = 0.0;
        double totalAm1 = 0.0;

        int sr = 0;
        for (ClsQuotationOrderDetail s : list) {
            sr++;

            String str = "";
            str = str.concat(String.valueOf(sr).concat(". ")).concat(s.getItem())
                    .concat(" (" + ClsGlobal.round(s.getSaleRate(), 2))
                    .concat(" x " + ClsGlobal.round(Double.parseDouble(RemoveZeroFromDouble(
                            String.valueOf(s.getQuantity()))), 2)).concat(")")
                    .concat(" *Rs.").concat(String.valueOf(ClsGlobal.round(
                            Double.parseDouble(RemoveZeroFromDouble(String.valueOf(s.getAmount())))
                            , 2))).concat("*")
                    .concat("\n");//"PUL

            if (s.getItemComment() != null && !s.getItemComment().equalsIgnoreCase("")) {
                str = str.concat("Details: ").concat(s.getItemComment().equalsIgnoreCase("")
                        ? "" : s.getItemComment()).concat("\n");
            }

            Log.e("str3", "kjh " + str);
            items.add(str);//"PUL
        }

        items.add("--------------------");

        items.add("*Amount:* " + ClsGlobal.round(getCurrentObj.getTotalAmount(), 2));
        if (getCurrentObj.getDiscountAmount() != 0.0) {
            items.add("*Discount:* " + ClsGlobal.round(getCurrentObj.getDiscountAmount(), 2));
        }

        if (getCurrentObj.getApplyTax().equalsIgnoreCase("YES")) {
            items.add("*Tax Amt:* ".concat(String.valueOf(ClsGlobal.round(Double.
                    valueOf(RemoveZeroFromDouble(String.valueOf(getCurrentObj.getTotalTaxAmount()))), 2))));///////remove decimal place if is 0
        }

        items.add("*Bill Amt*: ".concat(String.valueOf(ClsGlobal.round(getCurrentObj.getGrandTotal(), 2))));

        String smsBody = ClsGlobal.getWtsAppQuotationHeader(items,
                context,
                String.valueOf(getCurrentObj.getQuotationNo()),
                _validUpTo, mode,
                getCurrentObj.getCustomerName(),
                getCurrentObj.getMobileNo(), getCurrentObj.getApplyTax());

        return smsBody;
    }


    public static String getQuotationDate(String dateFormate) {
        final String outFormat = "yyyy-MM-dd";
        final String InFORMAT = "yyyy-MM-dd hh:mm:ss";
        String result = "";
        try {
            if (dateFormate != null && !dateFormate.isEmpty() && dateFormate != "") {
                Date date = new SimpleDateFormat(InFORMAT, Locale.ENGLISH).parse(dateFormate);
                DateFormat formatter = new SimpleDateFormat(outFormat, Locale.getDefault());
                result = formatter.format(date.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * Get File MineType.
     *
     * @param filePath
     * @return
     */
    public static String getMimeType(String filePath) {
        String type = null;
        String extension = null;
        int i = filePath.lastIndexOf('.');
        if (i > 0)
            extension = filePath.substring(i + 1);
        if (extension != null) {

            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        Log.e("Upload", "type:- " + type);

        return type;
    }

    /**
     * Get Network Type WIFI or Mobile Data.
     *
     * @param context
     * @return
     */
    public static String CheckNetWorkType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return activeNetwork.getTypeName();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return activeNetwork.getTypeName();
            }
        }
        return "";
    }


    /**
     * Check for the permission from backgorund.
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * UploadErrorLogs BackupLogs.
     *
     * @param context
     * @param permissions
     * @return
     */
    public static void UploadErrorLogs_BackupLogs(Context context) {
        Constraints myConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        // Upload Error and backup logs to server.
        Create_OneTimeWorkRequest(SendErrorWorker.class, ClsGlobal.AppPackageName
                .concat("ErrorLogUploadToServer"), "KEEP", null, myConstraints);
    }


    /**
     * Convert dd/mm/yyyy to yyyy-mm-dd format date.
     *
     * @param dateStr
     * @return
     */
    public static String ConvertDDMMYY_to_YYYYMMDD(String dateStr) {
        final String OUT_DATE_FORMAT = "yyyy-MM-dd";
        final String IN_DATE_FORMAT = "dd MM yyyy";
        try {

            if (dateStr != null && !dateStr.isEmpty() && dateStr != "") {
                Date date = new SimpleDateFormat(IN_DATE_FORMAT, Locale.ENGLISH).parse(dateStr.replaceAll("/", " "));
                DateFormat formatter = new SimpleDateFormat(OUT_DATE_FORMAT, Locale.getDefault());
                dateStr = formatter.format(date.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateStr;
    }


    /**
     * Clear User id and Password from SharedPreferences.
     *
     * @param context
     * @param permissions
     * @return
     */
    public static void Clear_User_Password(Context context) {
        SharedPreferences loginPreferences;
        SharedPreferences.Editor loginPrefsEditor;
        loginPreferences = context.getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        loginPrefsEditor.putString("username", "");
        loginPrefsEditor.putString("password", "");
        loginPrefsEditor.putBoolean("saveLogin", false);
        loginPrefsEditor.apply();
    }


    /**
     * Format Phone Number remove contry code and empty space
     * from given string.
     *
     * @return formatted Phone Number.
     */
    @SuppressLint("CheckResult")
    public static List<String> formatPhoneNo(final String number) {

        List<String> resultList = new ArrayList<>();

        List<String> codeList = StreamSupport
                .stream(getCountryNameCodeList())
                .map(CountryCode::getCountryCode)
                .collect(Collectors.toList());

        Log.e("Check", "countryCode codeList:  "
                + codeList);

        for (String countryCode : codeList) {
            Log.e("Check", "countryCode countryCode:  "
                    + countryCode);
            if (number.contains(countryCode) ||
                    number.contains(countryCode + " ")) {

                resultList.add(number.replace(countryCode, "")
                        .replace(" ", ""));
                resultList.add(countryCode);


                if (String.valueOf(number.charAt(0)).equalsIgnoreCase("0")) {
                    resultList.set(0, number.replace("0", "")
                            .replace(" ", ""));
                }

                break;

            }
        }
        if (resultList.size() == 0) {

            resultList.add(number.replace(" ", ""));
            resultList.add("");

        }

        return resultList;

    }

    /**
     * Get DD/MM/YYYY hh:mm aa for long.
     * Ex:- you will get this output 02/1/2020 4:50 AM.
     *
     * @return formated Phone Number.
     */
    public static String convertLongDateTime(long DateTime) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(DateTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy hh:mm aa");
        return dateFormat.format(cal1.getTime());

    }


    /**
     * If the worker is RUNNING or ENQUEUED then cancel.
     * create new worker.
     */
    public static void SalesWorkerReschedule() {
        WorkInfo.State state = getStateOfWork("SalesSms");
        ClsGlobal.appendSmsLog(getEntryDateFormat(getCurruntDateTime()) +
                "workInfos:- " + state.isFinished() + " " + state.isFinished() + "  \n");

        if (state == WorkInfo.State.ENQUEUED || state == WorkInfo.State.RUNNING) {
            Log.e("Check", "SalesWorkerReschedule " +
                    "state == WorkInfo.State.ENQUEUED || state == WorkInfo.State.RUNNING");
            ClsGlobal.cancelWorkerTask("SalesSms");
        } else {
            Log.e("Check", "SalesWorkerReschedule else");
        }


    }

    /**
     * Get Selected Text from radioButton from radioGroup.
     *
     * @param radioGroup
     * @return
     */
    public static String getSelectedIndexRadioGroup(@NonNull RadioGroup radioGroup) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {

            if ((radioGroup.getChildAt(i) instanceof RadioButton)) {
                if (((RadioButton) radioGroup.getChildAt(i)).isChecked()) {
                    RadioButton rb = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                    return String.valueOf(i);
                }
            }
        }
        return "";
    }

    /**
     * Check RadioButton by Text.
     *
     * @param radioGroup
     * @return
     */
    public static void setCheckIndexRadioGroup(@NonNull RadioGroup radioGroup,
                                               ClsBillNoFormat clsBillNoFormat) {

        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            if (radioGroup.findViewById(radioGroup.getChildAt(i).getId())
                    instanceof RadioButton) {
                RadioButton rb = (RadioButton)
                        radioGroup.findViewById(radioGroup.getChildAt(i).getId());

                Log.e("Check", "rb text" + rb.getText().toString());
//                Log.e("Check" ,"getSelected_Financial_Year_rb"  + clsBillNoFormat
//                        .getSelected_Financial_Year_rb());

                Log.e("Check", "getSelected_Year_rb" + rb.getText().toString()
                        .equalsIgnoreCase(clsBillNoFormat
                                .getSelected_Year_rb()));

                switch (radioGroup.getId()) {

                    case R.id.rg_Financial_Year:
                        if (rb != null && String.valueOf(i)
                                .equalsIgnoreCase(clsBillNoFormat
                                        .getSelected_Financial_Year_rb())) {
                            rb.setChecked(true);
                            break;
                        }
                        break;

                    case R.id.rg_Year:
                        if (rb != null && String.valueOf(i)
                                .equalsIgnoreCase(clsBillNoFormat
                                        .getSelected_Year_rb())) {
                            rb.setChecked(true);
                            break;
                        }
                        break;

                    case R.id.rg_month:

                        Log.e("Check", "getChildCount rg_month"
                                + radioGroup.getChildCount());
                        if (rb != null && String.valueOf(i)
                                .equalsIgnoreCase(clsBillNoFormat
                                        .getSelected_Month_rb())) {
                            Log.e("Check", "Selected_Month_rb ");
                            rb.setChecked(true);
                            break;
                        } else {
                            Log.e("Check", "Selected_Month_rb esle");
                        }
                        break;


                    case R.id.rg_Separator:
                        if (rb.getText() != null && String.valueOf(i)
                                .equalsIgnoreCase(clsBillNoFormat
                                        .getSelected_Separator_rd())) {
                            rb.setChecked(true);
                            break;
                        }
                        break;

                    case R.id.rg_Reset_Counter:
                        if (rb != null && String.valueOf(i)
                                .equalsIgnoreCase(clsBillNoFormat
                                        .getSelected_Reset_Counter_rb())) {
                            rb.setChecked(true);
                            break;
                        }
                        break;

                }
            }
        }

    }



    /**
     * Get Default Message for Payment Received,Pending for
     * WhatsApp,Send Sms form user side,Email.
     *
     * @param context
     * @param clsPaymentMaster
     * @param mode             // Payment Received,Payment Pending
     * @return getMessage // return formated message.
     */
    public static String getDefaultPaymentMassage(Context context,
                                                  ClsPaymentMaster clsPaymentMaster,
                                                  String mode) {

        ClsUserInfo objClsUserInfo = ClsGlobal.getUserInfo(context);

        // get Default Message.
        String getMessage = getDefaultMessageFormat(context,
                clsPaymentMaster.getOrderNo(),
                String.valueOf(clsPaymentMaster.getAmount()), mode,
                // getmode can be // Payment Pending,Payment
                // Received,Quotation,Sales,Purchase
                objClsUserInfo, clsPaymentMaster);

        Log.e("Check", "getMessage: " + getMessage);

        // Replace the meaasge body by keyword if keyword exist.
        getMessage = getPaymentMessage(getMessage, clsPaymentMaster
                , objClsUserInfo
                , context);

        Log.e("Check", getMessage);
        return getMessage;
    }


    /**
     * Prepare Message from the text which content message body with keywords.
     * It remove keyword and replace real content to message body.
     *
     * @param previewText,clsInventoryOrderMaster,objClsUserInfo
     * @return previewText
     */
    public static String getPaymentMessage(String previewText,
                                           ClsPaymentMaster clsPaymentMaster,
                                           ClsUserInfo objClsUserInfo,
                                           Context context) {

        Gson gson2 = new Gson();
        String jsonInString2 = gson2.toJson(clsPaymentMaster);
        Log.e("Check", "clsPaymentMaster:--- " + jsonInString2);


        if (previewText.contains("##PaymentDate##")) {
//            Log.e("Check", "PuschaseVendorName: " +
//                    getVendorNameById(clsPurchaseMaster.getVendorID(), context));
            previewText = previewText.replace("##PaymentDate##",
                    getDDMMYYYY(clsPaymentMaster.getPaymentDate()));
        }

        if (previewText.contains("##PaymentMonth##")) {
//            Log.e("Check", "PaymentAmount: " +
//                    clsPurchaseMaster.getTotalAmount());
            previewText = previewText.replace("##PaymentMonth##",
                    String.valueOf(clsPaymentMaster.getPaymentMounth()).split(" ")[0]);
        }

        if (previewText.contains("##PaymentMobileNo##")) {
//            Log.e("Check", "getPurchaseDate: " +
//                    clsPurchaseMaster.getPurchaseDate());
//            Log.e("Check", "PuschaseMonthYear: " +
//                    getChangeDateFormatAllExp(clsPurchaseMaster.getPurchaseDate()));
            previewText = previewText.replace("##PaymentMobileNo##",
                    clsPaymentMaster.getMobileNo());
        }

        if (previewText.contains("##PaymentCustomerName##")) {
//            Log.e("Check", "PuschaseDate: " +
//                    getDDMMYYYY(clsPurchaseMaster.getPurchaseDate()));
            previewText = previewText.replace("##PaymentCustomerName##",
                    clsPaymentMaster.getCustomerName());
        }

        if (previewText.contains("##PaymentVendorName##")) {

//            Log.e("Check", "getRemark: " +
//                    clsPurchaseMaster.getRemark());
            previewText = previewText.replace("##PaymentVendorName##",
                    clsPaymentMaster.getVendorName());
        }

        if (previewText.contains("##PaymentMode##")) {

//            Log.e("Check", "getRemark: " +
//                    clsPurchaseMaster.getRemark());
            previewText = previewText.replace("##PaymentMode##",
                    clsPaymentMaster.getPaymentMode());
        }

        if (previewText.contains("##PaymentDetail##")) {

//            Log.e("Check", "getRemark: " +
//                    clsPurchaseMaster.getRemark());
            previewText = previewText.replace("##PaymentDetail##",
                    clsPaymentMaster.getPaymentDetail());
        }

        if (previewText.contains("##PaymentInvoiceNo##")) {

//            Log.e("Check", "getRemark: " +
//                    clsPurchaseMaster.getRemark());
            previewText = previewText.replace("##PaymentInvoiceNo##",
                    clsPaymentMaster.getInvoiceNo());
        }

        if (previewText.contains("##PaymentPendingAmount##")) {

//            Log.e("Check", "getRemark: " +
//                    clsPurchaseMaster.getRemark());
            previewText = previewText.replace("##PaymentPendingAmount##",
                    String.valueOf(clsPaymentMaster.getAdjustmentAmount()));
        }

        if (previewText.contains("##PaymentPaidAmount##")) {

//            Log.e("Check", "getRemark: " +
//                    clsPurchaseMaster.getRemark());
            previewText = previewText.replace("##PaymentPaidAmount##",
                    String.valueOf(clsPaymentMaster.getAmount()));
        }

        if (previewText.contains("##PaymentRemark##")) {

//            Log.e("Check", "getRemark: " +
//                    clsPurchaseMaster.getRemark());
            previewText = previewText.replace("##PaymentRemark##",
                    String.valueOf(clsPaymentMaster.getRemark()));
        }

        if (previewText.contains("##PaymentType##")) {

//            Log.e("Check", "getRemark: " +
//                    clsPurchaseMaster.getRemark());
            previewText = previewText.replace("##PaymentType##",
                    String.valueOf(clsPaymentMaster.getType()));
        }

        if (previewText.contains("##Signature##")) {
//            Log.e("Check", "Signature: " +
//                    objClsUserInfo.getCustomerSignatory());
            previewText = previewText.replace("##Signature##",
                    objClsUserInfo.getCustomerSignatory());
        }

        if (previewText.contains("##New Line##")) {
            previewText = previewText.replace("##New Line##", "\n");
        }


        previewText = commonKeywords(previewText,
                null, null, objClsUserInfo, context);

        Log.e("Check", "pruchase: " + previewText);

        return previewText;
    }


}
