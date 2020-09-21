package com.demo.nspl.restaurantlite.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.demo.nspl.restaurantlite.Adapter.ExpandableListAdapter;
import com.demo.nspl.restaurantlite.Customer.CustomerReportsFragment;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ApiClient;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Global.ClsPermission;
import com.demo.nspl.restaurantlite.Global.DashboardFragment;
import com.demo.nspl.restaurantlite.Navigation_Drawer.AboutFragment;
import com.demo.nspl.restaurantlite.Navigation_Drawer.CustomerLedgerFragment;
import com.demo.nspl.restaurantlite.Navigation_Drawer.EmployeeFragment;
import com.demo.nspl.restaurantlite.Navigation_Drawer.ExpenseTypeFragment;
import com.demo.nspl.restaurantlite.Navigation_Drawer.ItemFragment;
import com.demo.nspl.restaurantlite.Navigation_Drawer.ItemLayersFragment;
import com.demo.nspl.restaurantlite.Navigation_Drawer.PaymentReportFragment;
import com.demo.nspl.restaurantlite.Navigation_Drawer.QuotationReportFragment;
import com.demo.nspl.restaurantlite.Navigation_Drawer.SalesReportsFragment;
import com.demo.nspl.restaurantlite.Navigation_Drawer.SendSmsReportFragment;
import com.demo.nspl.restaurantlite.Navigation_Drawer.SmsSettingsFragment;
import com.demo.nspl.restaurantlite.Navigation_Drawer.TaxSlabFragment;
import com.demo.nspl.restaurantlite.Navigation_Drawer.TermsFragment;
import com.demo.nspl.restaurantlite.Navigation_Drawer.UnitFragment;
import com.demo.nspl.restaurantlite.Navigation_Drawer.VendorFragment;
import com.demo.nspl.restaurantlite.Purchase.PurchaseListFragment;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsLogoutParams;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfaceLogout;
import com.demo.nspl.restaurantlite.SMS.CustomerGroupFragment;
import com.demo.nspl.restaurantlite.SMS.GetSmsIdPackagesActivity;
import com.demo.nspl.restaurantlite.SMS.SmsDashboardFragment;
import com.demo.nspl.restaurantlite.SMS.SmsFormatsListFragment;
import com.demo.nspl.restaurantlite.Stock.StockFragment;
import com.demo.nspl.restaurantlite.UpdateCalling.ClsItemDefaultTaxUpdate;
import com.demo.nspl.restaurantlite.VendorLedger.VendorLedgerFragment;
import com.demo.nspl.restaurantlite.classes.ClsLayerItemMaster;
import com.demo.nspl.restaurantlite.classes.ClsUserInfo;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.BackUpDbFile;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.REQUEST_APP_UPDATE;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.SetDefaultBackupSettings;
import static com.demo.nspl.restaurantlite.Global.ClsGlobal.ShowBkp_Remainder;
import static com.demo.nspl.restaurantlite.Global.ClsPermission.REQUEST_READ_PHONE_STATE;

public class SHAP_Lite_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, InstallStateUpdatedListener {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    private DrawerLayout mDrawerLayout;
    HashMap<String, List<String>> listDataChild;
    TextView txt_title, txt_email;
    ImageView iv_logout;
    ClsUserInfo objClsUserInfo = new ClsUserInfo();
    String _customerId = "", _renewAlert = "";
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private AppUpdateManager appUpdateManager;

    String _licenseType = "";

    LinearLayout ll_free, ll_premium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shap__lite_);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "SHAP_Lite_Activity"));
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ClsPermission.checkpermission(SHAP_Lite_Activity.this);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().add(R.id.content_frame, new DashboardFragment()).commit();

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();


        // mSharedviewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
//        mSharedviewModel.init();

//        Thread thread = new Thread(() ->
//                ClsGlobal.add_Edit_DataBase_Columns(SHAP_Lite_Activity.this));
//
//        thread.start();


        appUpdateManager = AppUpdateManagerFactory.create(SHAP_Lite_Activity.this);
        appUpdateManager.registerListener(this);
        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // For a flexible update, use AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.

                // Checks that the platform will allow the specified type of update.
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    // Request the update.
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo,
                                AppUpdateType.IMMEDIATE,
                                this,
                                REQUEST_APP_UPDATE);

                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate();
                } else {
                    Log.e("Login", "checkForAppUpdateAvailability: something else");
                }
            }
        });
/*
        if (ChangeDateQuotrationoDateFormat(SHAP_Lite_Activity.this)
                .equalsIgnoreCase("FirstTime")) {
            SharedPreferences mPreferences = getSharedPreferences(mPreferncesName, MODE_PRIVATE);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString("DateQuotrationoDateFormat", "SecondTime");
            editor.apply();

            Thread thread = new Thread(() ->
                    ClsQuotationMaster.UpdateQuotationDateFormat(SHAP_Lite_Activity.this));

            thread.start();


            Log.d("--update--", "IFFIFIFIFIF");

        } else {
            Log.d("--update--", "ELSELSELESLSEL");
        }*/



        ClsGlobal.SetUpAutoLocalBkp();

        init();

/*

        // auto logout......
        moveToLoginPage();
*/


        // Show backup Remainder Daily.
        ShowBkp_Remainder(SHAP_Lite_Activity.this);

        // Take emergency backup in .ftouchlogPOS folder.
        // we take emergency backup when user open 5 times SHAP_Lite_Activity or LoginActivity.
        BackUpDbFile(SHAP_Lite_Activity.this);

        // Set DefaultBackupSettings and start backup worker.
        SetDefaultBackupSettings(SHAP_Lite_Activity.this);


    }

    private void init() {

        _customerId = getIntent().getStringExtra("_customerId");
        _renewAlert = getIntent().getStringExtra("_renewAlert");
        _licenseType = getIntent().getStringExtra("_licenseType");


        Log.e("--Login--", "_licenseType: " + _licenseType);


        ll_free = findViewById(R.id.ll_free);
        ll_premium = findViewById(R.id.ll_premium);

        iv_logout = findViewById(R.id.iv_logout);
        txt_email = findViewById(R.id.txt_email);

        expListView = findViewById(R.id.lvExp);
        expListView.setGroupIndicator(null);
        expListView.setChildIndicator(null);
        expListView.setDivider(getResources().getDrawable(R.color.white));
        mDrawerLayout = findViewById(R.id.drawer_layout);
        txt_title = findViewById(R.id.txt_title);

        objClsUserInfo = ClsGlobal.getUserInfo(SHAP_Lite_Activity.this);
        //Remain days 8, expire on 10
        Log.e("--Login--", "getLicenseType: " + objClsUserInfo.getLicenseType());

        if (objClsUserInfo.getLicenseType().equalsIgnoreCase("Free")) {
//            txt_email.setVisibility(View.GONE);
            txt_email.setVisibility(View.VISIBLE);

//            txt_email.setText("License Type: FREE");

            txt_email.setText(Html.fromHtml("<b>License Type: </b><span style = \"color:#40932d;\">FREE </span>"));// + item.getPackageType().toUpperCase()));


//            txt_email.setTextColor(Color.parseColor("#40932d"));

        } else {
            txt_email.setVisibility(View.VISIBLE);
            txt_email.setText("Expiring on ".concat(objClsUserInfo.getExpiredDays()));
        }

        if (_renewAlert != null && _renewAlert != "") {
            int remainDays = Integer.parseInt(objClsUserInfo.getRemainDays()
                    .equalsIgnoreCase("") ? "0" : objClsUserInfo.getRemainDays());
            if (remainDays <= 10) {
                renewPackageAlert(objClsUserInfo.getRemainDays(), objClsUserInfo.getExpiredDays(), objClsUserInfo.getUserId());
            }
        }


        txt_title.setText("Hello, " + objClsUserInfo.getContactPersonName());

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        prepareListData();


        ClsItemDefaultTaxUpdate objUpdate = ClsGlobal.getUpdateItemDefaultTax(getApplicationContext());

        if (objUpdate.getUpdateItemDefaultTax() == null ||
                objUpdate.getUpdateItemDefaultTax().equalsIgnoreCase("")) {

            objUpdate.setUpdateItemDefaultTax("FirstTime");
            Log.e("--UpdateTax--", "NULL_IF");
            ClsGlobal.setUpdateItemDefaultTax(objUpdate, getApplicationContext());
        } else {
            Log.e("--UpdateTax--", "NULL_ELSE");
        }

        if (objUpdate.getUpdateItemDefaultTax().equalsIgnoreCase("FirstTime")) {
            updateDefaultTax();

            Log.e("--UpdateTax--", "CONDITION_IF");
//            Toast.makeText(this, "First Time", Toast.LENGTH_LONG).show();
        } else {

            Log.e("--UpdateTax--", "CONDITION_ELSE");
//            Toast.makeText(this, "Second Time", Toast.LENGTH_LONG).show();
        }


        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @SuppressLint("WrongConstant")
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                if (moveToLoginPage()) {
                    if (listDataHeader.get(groupPosition).equalsIgnoreCase("NO~Settings")) {

                        Intent intent = new Intent(SHAP_Lite_Activity.this, SettingActivity.class);
                        intent.putExtra("_licenseType", objClsUserInfo.getLicenseType());
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(Gravity.START, true);

                    } else if (listDataHeader.get(groupPosition).equalsIgnoreCase("NO~About")) {

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new AboutFragment())
                                .disallowAddToBackStack().commit();
                        mDrawerLayout.closeDrawer(Gravity.START, true);

                    } else if (listDataHeader.get(groupPosition).equalsIgnoreCase("NO~Pos")) {

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new DashboardFragment())
                                .disallowAddToBackStack().commit();
                        mDrawerLayout.closeDrawer(Gravity.START, true);


                    } else if (listDataHeader.get(groupPosition).equalsIgnoreCase("NO~Purchase")) {

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new PurchaseListFragment())
                                .disallowAddToBackStack().commit();
                        mDrawerLayout.closeDrawer(Gravity.START, true);

                    } else if (listDataHeader.get(groupPosition).equalsIgnoreCase("NO~Premium")) {

                        Intent intent = new Intent(SHAP_Lite_Activity.this, PremiumActivity.class);
                        intent.putExtra("flag", "InSide");
                        intent.putExtra("_customerId", objClsUserInfo.getUserId());
                        intent.putExtra("_mobileNo", objClsUserInfo.getMobileNo());
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(Gravity.START, true);

                    } else if (listDataHeader.get(groupPosition).equalsIgnoreCase("NO~Share App")) {

                        ClsGlobal.ShareAppLink(SHAP_Lite_Activity.this);

                        mDrawerLayout.closeDrawer(Gravity.START, true);
                    }

                }

                return false;

            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });


        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @SuppressLint("WrongConstant")
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {


                if (moveToLoginPage()) {
                    if (listDataChild.get(listDataHeader.get(groupPosition)).get(
                            childPosition).equalsIgnoreCase("Sales")) {

                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(
                            childPosition).equalsIgnoreCase("Payment Details")) {

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new PaymentReportFragment())
                                .disallowAddToBackStack().commit();
                        mDrawerLayout.closeDrawer(Gravity.START, true);


                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(
                            childPosition).equalsIgnoreCase("Sales Reports")) {

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new SalesReportsFragment())
                                .disallowAddToBackStack().commit();
                        mDrawerLayout.closeDrawer(Gravity.START, true);

                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(
                            childPosition).equalsIgnoreCase("Stock")) {

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new StockFragment())
                                .disallowAddToBackStack().commit();
                        mDrawerLayout.closeDrawer(Gravity.START, true);

                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(
                            childPosition).equalsIgnoreCase("Employee")) {

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new EmployeeFragment())
                                .disallowAddToBackStack().commit();
                        mDrawerLayout.closeDrawer(Gravity.START, true);

                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(
                            childPosition).equalsIgnoreCase("Terms")) {

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new TermsFragment())
                                .disallowAddToBackStack().commit();
                        mDrawerLayout.closeDrawer(Gravity.START, true);

                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(
                            childPosition).equalsIgnoreCase("Taxes")) {

                        Intent intent = new Intent(SHAP_Lite_Activity.this, TaxesActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(Gravity.START, true);

                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(
                            childPosition).equalsIgnoreCase("Tax Slab")) {

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new TaxSlabFragment())
                                .disallowAddToBackStack().commit();
                        mDrawerLayout.closeDrawer(Gravity.START, true);

                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(
                            childPosition).equalsIgnoreCase("Dashboard")) {
                        Intent intent = new Intent(SHAP_Lite_Activity.this, ExpenseDashboardActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(Gravity.START, true);

                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(
                            childPosition).equalsIgnoreCase("Expense Type")) {

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new ExpenseTypeFragment())
                                .disallowAddToBackStack().commit();
                        mDrawerLayout.closeDrawer(Gravity.START, true);

                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(
                            childPosition).equalsIgnoreCase("Layer")) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new ItemLayersFragment())
                                .disallowAddToBackStack().commit();
                        mDrawerLayout.closeDrawer(Gravity.START, true);


                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(
                            childPosition).equalsIgnoreCase("Item")) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new ItemFragment())
                                .disallowAddToBackStack().commit();
                        mDrawerLayout.closeDrawer(Gravity.START, true);

                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(
                            childPosition).equalsIgnoreCase("Unit")) {

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new UnitFragment())
                                .disallowAddToBackStack().commit();
                        mDrawerLayout.closeDrawer(Gravity.START, true);


                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(
                            childPosition).equalsIgnoreCase("Vendor")) {


                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new VendorFragment())
                                .disallowAddToBackStack().commit();
                        mDrawerLayout.closeDrawer(Gravity.START, true);

                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(
                            childPosition).equalsIgnoreCase("Vendor Ledger")) {

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new VendorLedgerFragment())
                                .disallowAddToBackStack().commit();
                        mDrawerLayout.closeDrawer(Gravity.START, true);

                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(
                            childPosition).equalsIgnoreCase("Customer Ledger")) {

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new CustomerLedgerFragment())
                                .disallowAddToBackStack().commit();
                        mDrawerLayout.closeDrawer(Gravity.START, true);


                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(
                            childPosition).equalsIgnoreCase("Customer Reports")) {

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new CustomerReportsFragment())
                                .disallowAddToBackStack().commit();
                        mDrawerLayout.closeDrawer(Gravity.START, true);

                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(
                            childPosition).equalsIgnoreCase("Customer Group")) {

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new CustomerGroupFragment())
                                .disallowAddToBackStack().commit();
                        mDrawerLayout.closeDrawer(Gravity.START, true);

                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(
                            childPosition).equalsIgnoreCase("Sender Id")) {

                        senderIdAlertDialog("Dear user,you have to use fixed senderID as \"FTOUCH\" " +
                                "which is already created further we will give option to create your own sender " +
                                "id as soon as possible. ");

//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.content_frame, new SmsIdSettingListFragment())
//                                .disallowAddToBackStack().commit();
//                        mDrawerLayout.closeDrawer(Gravity.START, true);


                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(
                            childPosition).equalsIgnoreCase("BUY")) {

                        Intent intent = new Intent(SHAP_Lite_Activity.this, GetSmsIdPackagesActivity.class);
                        intent.putExtra("_customerId", objClsUserInfo.getUserId());
                        intent.putExtra("registrationMode", "ALL");
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(Gravity.START, true);
                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(
                            childPosition).equalsIgnoreCase("Quotation Reports")) {

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new QuotationReportFragment())
                                .disallowAddToBackStack().commit();
                        mDrawerLayout.closeDrawer(Gravity.START, true);

                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(
                            childPosition).equalsIgnoreCase("Sms Template Design")) {

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new SmsFormatsListFragment())
                                .disallowAddToBackStack().commit();
                        mDrawerLayout.closeDrawer(Gravity.START, true);

                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(
                            childPosition).equalsIgnoreCase("Bulk Sms")) {

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new SendSmsReportFragment())
                                .disallowAddToBackStack().commit();
                        mDrawerLayout.closeDrawer(Gravity.START, true);


                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(
                            childPosition).equalsIgnoreCase("SMS Dashboard")) {

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new SmsDashboardFragment())
                                .disallowAddToBackStack().commit();
                        mDrawerLayout.closeDrawer(Gravity.START, true);

                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(
                            childPosition).equalsIgnoreCase("Sms Settings")) {

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new SmsSettingsFragment())
                                .disallowAddToBackStack().commit();
                        mDrawerLayout.closeDrawer(Gravity.START, true);

                    } else {

                        Toast.makeText(
                                getApplicationContext(),
                                listDataHeader.get(groupPosition)
                                        + " : "
                                        + listDataChild.get(
                                        listDataHeader.get(groupPosition)).get(
                                        childPosition), Toast.LENGTH_SHORT)
                                .show();

                    }
                }
                return false;
            }

        });

        iv_logout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {

                mDrawerLayout.closeDrawer(Gravity.START, true);
                LogoutAlert();
            }
        });

        if (objClsUserInfo.getLicenseType().equalsIgnoreCase("Free")) {
            ll_free.setVisibility(View.VISIBLE);
        } else {
            ll_premium.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {

            if (ClsGlobal.isFristFragment) {
                Log.e("Main", "Inside true of FF");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new DashboardFragment())
                        .disallowAddToBackStack().commit();

            } else {

                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.message_logout_prompt, null);

                TextView tvMessage = alertLayout.findViewById(R.id.tvPromptMessage);

                AlertDialog alertDialog = new AlertDialog.Builder(SHAP_Lite_Activity.this, R.style.AppCompatAlertDialogStyle).create(); //Read Update.
                alertDialog.setView(alertLayout);
                alertDialog.setTitle("Confirmation");
                tvMessage.setText("Are sure you want to close this application?");

                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Yes", (dialog, which) ->
                        finishAffinity());
                alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "No", (dialog, which) ->
                        dialog.cancel());

                alertDialog.setCancelable(false);
                alertDialog.show();

            }
        }
    }


    private void updateDefaultTax() {

        ClsLayerItemMaster.updateTax(getApplicationContext());

        ClsItemDefaultTaxUpdate objUpdate = new ClsItemDefaultTaxUpdate();
        objUpdate.setUpdateItemDefaultTax("SecondTime");
        ClsGlobal.setUpdateItemDefaultTax(objUpdate, getApplicationContext());

    }


    void LogoutAlert() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.message_logout_prompt, null);

        TextView tvMessage = alertLayout.findViewById(R.id.tvPromptMessage);

        AlertDialog alertDialog = new AlertDialog.Builder(SHAP_Lite_Activity.this, R.style.AppCompatAlertDialogStyle).create(); //Read Update.
        alertDialog.setView(alertLayout);
        alertDialog.setTitle("Confirmation");
        tvMessage.setText(getResources().getString(R.string.logout_message));

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ------------------- UnComment for Original app -------------------//
                LogoutApi();

                // ------------------- UnComment for Demo app -----------------------//

//                ClsGlobal.autoLogout(getApplicationContext());
//                Intent i = new Intent(SHAP_Lite_Activity.this, LoginActivityDemo.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i);
//                Toast.makeText(SHAP_Lite_Activity.this,
//                        "Successfully logout", Toast.LENGTH_SHORT).show();

            }
        });
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        moveToLoginPage();

    }


    boolean moveToLoginPage() {
        boolean result = true;

        ClsUserInfo objClsUserInfoOld = ClsGlobal.getUserInfo(SHAP_Lite_Activity.this);
        Log.d("getFirstDateOfMonth", "----UserInfo---" + objClsUserInfoOld.getLoginStatus());

        Gson gson = new Gson();
        String jsonInString = gson.toJson(objClsUserInfoOld);
        Log.d("--moveToLoginPage--", "moveToLoginPage: " + jsonInString);


        if (!objClsUserInfoOld.getLoginStatus().equalsIgnoreCase("ACTIVE")) {
            finish();
        }
        return result;
    }


    /* void moveToLoginPage() {

         ClsUserInfo objClsUserInfoOld = ClsGlobal.getUserInfo(SHAP_Lite_Activity.this);
         Log.d("getFirstDateOfMonth", "----UserInfo---" + objClsUserInfoOld.getLoginStatus());

         Gson gson = new Gson();
         String jsonInString = gson.toJson(objClsUserInfoOld);
         Log.d("--moveToLoginPage--", "moveToLoginPage: " + jsonInString);


         if (!objClsUserInfoOld.getLoginStatus().equalsIgnoreCase("ACTIVE")) {
             finish();
         }
     }
 */
    void LogoutApi() {

        if (ClsGlobal.CheckInternetConnection(SHAP_Lite_Activity.this)) {
            ClsLogoutParams objClsLogoutParams = new ClsLogoutParams();
            objClsLogoutParams.setUserCode(objClsUserInfo.getUserId());
            objClsLogoutParams.setMode("MobileApplication");
            objClsLogoutParams.setIMEINumber(ClsGlobal.getIMEIno(SHAP_Lite_Activity.this));

            Gson gson = new Gson();
            String jsonInString = gson.toJson(objClsLogoutParams);
            Log.d("Result", "objClsVerifiedOtpParms- " + jsonInString);

            InterfaceLogout interfaceLogout = ApiClient.getRetrofitInstance().create(InterfaceLogout.class);
            Log.e("--URL--", "interfaceDesignation: " + interfaceLogout.toString());
            Call<ClsLogoutParams> call = interfaceLogout.postLogout(objClsLogoutParams);
            Log.e("--URL--", "************************  before call : " + call.request().url());

            ProgressDialog pd =
                    ClsGlobal._prProgressDialog(SHAP_Lite_Activity.this, "Wait for logout...", true);
            pd.show();

            call.enqueue(new Callback<ClsLogoutParams>() {
                @Override
                public void onResponse(Call<ClsLogoutParams> call, Response<ClsLogoutParams> response) {
                    pd.dismiss();
                    if (response.body() != null) {
                        String _response = response.body().getSuccess();
                        Log.d("_response", "onResponse: " + _response);

                        switch (_response) {
                            case "1":
                                ClsGlobal.autoLogout(getApplicationContext());
                                //WorkManager.getInstance().cancelUniqueWork(ClsGlobal.AppPackageName.concat("DailyTaskLogoutRetail"));
//                              Intent i = new Intent(SHAP_Lite_Activity.this, LoginActivityDemo.class);
                                Intent i = new Intent(SHAP_Lite_Activity.this, LogInActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                Toast.makeText(SHAP_Lite_Activity.this, "Successfully logout", Toast.LENGTH_SHORT).show();
                                break;
                            case "2":
                                Toast.makeText(SHAP_Lite_Activity.this, "Device is not match or internet issue", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(SHAP_Lite_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Server error...!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ClsLogoutParams> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Something went wrong...Internet issue!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Please check internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add("NO~Pos");
        listDataHeader.add("NO~Purchase");

        listDataHeader.add("YES~Inventory");
        listDataHeader.add("YES~Master");
        listDataHeader.add("YES~Expenses");
        listDataHeader.add("YES~Reports");
        listDataHeader.add("YES~Sms");

        listDataHeader.add("NO~Settings");
        listDataHeader.add("NO~Share App");


        if (objClsUserInfo.getLicenseType().equalsIgnoreCase("Free")) {
            listDataHeader.add("NO~Premium");
        }

        listDataHeader.add("NO~About");
//        listDataHeader.add("NO~OverView");


        List<String> Master = new ArrayList<String>();

        Master.add("Taxes");
        Master.add("Tax Slab");
        Master.add("Employee");
        Master.add("Vendor");
        Master.add("Terms");

        List<String> Expense = new ArrayList<String>();
        Expense.add("Dashboard");
        Expense.add("Expense Type");

        List<String> Inventory = new ArrayList<String>();
        Inventory.add("Layer");
        Inventory.add("Unit");
        Inventory.add("Item");
        // Inventory.add("Inventory Item");

        List<String> Reports = new ArrayList<String>();
        Reports.add("Payment Details");
        Reports.add("Vendor Ledger");
        Reports.add("Customer Ledger");
        Reports.add("Sales Reports");
        Reports.add("Quotation Reports");
        Reports.add("Stock");
        Reports.add("Customer Reports");


        List<String> Sms = new ArrayList<String>();
        Sms.add("SMS Dashboard");
        Sms.add("Sender Id");
        Sms.add("Customer Group");
        Sms.add("SMS Template Design");
        Sms.add("Bulk SMS");
        Sms.add("SMS Settings");


        List<String> Pos = new ArrayList<String>();
        List<String> Purchase = new ArrayList<String>();
        List<String> Setting = new ArrayList<String>();

        List<String> About = new ArrayList<String>();
        List<String> Premium = new ArrayList<String>();
        List<String> Share = new ArrayList<String>();

        listDataChild.put(listDataHeader.get(0), Pos); // Header, Child data
        listDataChild.put(listDataHeader.get(1), Purchase); // Header, Child data

        listDataChild.put(listDataHeader.get(2), Inventory);
        listDataChild.put(listDataHeader.get(3), Master);
        listDataChild.put(listDataHeader.get(4), Expense);
        listDataChild.put(listDataHeader.get(5), Reports);
        listDataChild.put(listDataHeader.get(6), Sms);

        listDataChild.put(listDataHeader.get(7), Setting);


        if (objClsUserInfo.getLicenseType().equalsIgnoreCase("Free")) {

            listDataChild.put(listDataHeader.get(8), Premium);
            listDataChild.put(listDataHeader.get(9), About);
            listDataChild.put(listDataHeader.get(10), Share);

        } else {
            listDataChild.put(listDataHeader.get(8), About);
            listDataChild.put(listDataHeader.get(9), Share);
        }


        if (objClsUserInfo.getLicenseType().equalsIgnoreCase("Free")) {

            listDataChild.put(listDataHeader.get(8), Premium);
            listDataChild.put(listDataHeader.get(9), About);
//            listDataChild.put(listDataHeader.get(10), OverView);
        } else {
            listDataChild.put(listDataHeader.get(8), About);
//            listDataChild.put(listDataHeader.get(9), OverView);
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE: {
                ClsPermission.checkPermission(requestCode, permissions, grantResults, SHAP_Lite_Activity.this);
                return;
            }

        }

    }

    /* Displays the snackbar notification and call to action. */
    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.drawer_layout),
                        "An update has just been downloaded.",
                        Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("RESTART", view ->
                appUpdateManager.completeUpdate());

        snackbar.setActionTextColor(
                getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }


    @Override
    public void onStateUpdate(InstallState installState) {

        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            popupSnackbarForCompleteUpdate();
        } else if (installState.installStatus() == InstallStatus.INSTALLED) {
            if (appUpdateManager != null) {
                appUpdateManager.unregisterListener(this);
            }

        } else {
            Log.i("login", "InstallStateUpdatedListener: state: " + installState.installStatus());
        }

    }

    void freeDialog(String customerId) {

        AlertDialog alertDialog = new AlertDialog.Builder(SHAP_Lite_Activity.this,
                R.style.AppCompatAlertDialogStyle).create(); //Read Update.
        alertDialog.setContentView(R.layout.dialog_renew_pkg);
        alertDialog.setTitle("CONFIRM!");
        alertDialog.setMessage("All settings & data may be lost. PREMIUM functionality will not work! Are you sure to continue as free version?");

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setAttributes(lp);

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "YES",
                (dialog, which) -> {
//                    ClsGlobal.autoLogout(getApplicationContext());

//                    Intent intent = new Intent(getApplicationContext(), DisplayPlansActivity.class);
//                    intent.putExtra("reg_mode", "FREE");
//                    intent.putExtra("_customerId", customerId);
//                    startActivity(intent);




                    Intent intent = new Intent(getApplicationContext(), OtpVerificationActivity.class);

                    intent.putExtra("_mobileNo", objClsUserInfo.getMobileNo());
                    intent.putExtra("_customerId", _customerId);
                    intent.putExtra("_resCustomerStatus", "");
                    intent.putExtra("_resLicenseType", "Free");
                    intent.putExtra("mode", "Free");
                    intent.putExtra("sendSMSNow", "Yes");

                    startActivity(intent);


                });

        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "NO", (dialog, which) -> dialog.cancel());
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    void renewPackageAlert(String remainDays, String expireDate, String customerId) {

        Dialog dialog = new Dialog(SHAP_Lite_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_renew_pkg);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView txt_descriptions = dialog.findViewById(R.id.txt_descriptions);
        TextView txt_not_now = dialog.findViewById(R.id.txt_not_now);
        TextView txt_free = dialog.findViewById(R.id.txt_free);
        TextView txt_renew = dialog.findViewById(R.id.txt_renew);
        txt_descriptions.setText("Your package is expiring on "
                .concat(expireDate).concat(", remaining days is ").concat(remainDays + ",")
                .concat(" please renew before expire existing package to utilise smooth & uninterrupted service."));


        txt_not_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                dialog.dismiss();
            }
        });

        txt_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                dialog.dismiss();

                freeDialog(customerId);


            }
        });

        txt_renew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClsGlobal.autoLogout(getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), DisplayPlansActivity.class);
                intent.putExtra("reg_mode", "RENEW");
                intent.putExtra("_customerId", customerId);
                startActivity(intent);
                dialog.cancel();
                dialog.dismiss();
            }
        });

        dialog.getWindow().setAttributes(lp);
        dialog.show();

    }

    private void senderIdAlertDialog(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SHAP_Lite_Activity.this);
        alertDialog.setMessage(msg);
        alertDialog.setIcon(R.drawable.ic_wholesale);


        alertDialog.setPositiveButton("OK", (dialog13, which) -> {

            dialog13.dismiss();
            dialog13.cancel();


        });

        alertDialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (appUpdateManager != null) {
            appUpdateManager.unregisterListener(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        appUpdateManager.unregisterListener(this);
//        appUpdateManager.unregisterListener();
    }


}
