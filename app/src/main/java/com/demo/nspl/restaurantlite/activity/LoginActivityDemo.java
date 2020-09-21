package com.demo.nspl.restaurantlite.activity;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivityDemo extends AppCompatActivity {

    /*TextInputLayout input_user_id, input_password, input_email;
    EditText edt_mobile, edt_password, edt_email;
    TextView forgot_password;
    CheckBox show_hide_password, chk_remember;
    Button btn_login;
    TextView txt_createAccount;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    String username, password;

    private SQLiteDatabase db;

    //ClsUserInfo objClsUserInfo = new ClsUserInfo();
    SharedPreferences pref;
    ClsUserInfo objClsUserInfoOld = new ClsUserInfo();

    ClsLoginResponseList objUser = new ClsLoginResponseList();

    private SharedPreferences mPreferences;
    private static final String mPreferncesName = "MyPerfernces";
    PeriodicWorkRequest periodicWorkRequest;

//    private static final String mPreferncesFileName = "AutoBackUpSettings";
//    private SharedPreferences mPreferencesAutoBackUp;
    //    private static final String SHARED_PREF_NAME = "mysharedpref";
//    private static final String KEY_NAME = "keyname";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_demo);

        getWindow().setStatusBarColor(ContextCompat.getColor(LoginActivityDemo.this, R.color.colorPrimaryDark));
        ClsPermission.checkpermission(LoginActivityDemo.this);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "LoginActivityDemo"));
        }


        // --------------------------- For AutoBackUp (Start here)-------------------------------------//
//        mPreferencesAutoBackUp = getSharedPreferences(mPreferncesFileName, MODE_PRIVATE);
//
//        if (mPreferencesAutoBackUp.getString("AutoBackUpTime", null) == null) {
//            SharedPreferences.Editor editor = mPreferencesAutoBackUp.edit();
//            editor.putString("AutoBackUpTime", "Daily");
//            editor.apply();
//
//            Log.e("mPreferencesAutoBackUp" , "mPreferencesAutoBackUp inside");
//
//            String getAutoBackUpTime = mPreferencesAutoBackUp.getString("AutoBackUpTime","");
//            if (getAutoBackUpTime != null &&
//                    !getAutoBackUpTime.equalsIgnoreCase("") &&
//                    getAutoBackUpTime.equalsIgnoreCase("Daily")){
//
//                Log.e("getAutoBackUpTime" , "getAutoBackUpTime inside");
//                ClsGlobal.SetAutoBackUp(1,"KEEP");
//            }
//        }
//
//        if (mPreferencesAutoBackUp.getString("NetworkType", null) == null) {
//            SharedPreferences.Editor editor = mPreferencesAutoBackUp.edit();
//            editor.putString("NetworkType", "Wi-fi");
//            editor.apply();
//            Log.e("mPreferencesAutoBackUp" , "NetworkType mPreferencesAutoBackUp inside");
//        }

        // --------------------------- For AutoBackUp (End Here) -------------------------------------//


        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        mPreferences = getSharedPreferences(mPreferncesName, MODE_PRIVATE);


        JobInfo myJob = new JobInfo.Builder(24,
                new ComponentName(this, CheckNetworkJob.class))
                .setMinimumLatency(1000)
                .setOverrideDeadline(2000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(myJob);


        main();
        input_email.setTag("0");
        edt_email.setText("");
        create_database();
        objClsUserInfoOld = ClsGlobal.getUserInfo(LoginActivityDemo.this);
        Log.d("UserInfo", "----UserInfo---" + objClsUserInfoOld.getUserId());


        if (objClsUserInfoOld.getLoginStatus() == null || objClsUserInfoOld.getLoginStatus().isEmpty()) {
            ClsUserInfo objClsUserInfo = new ClsUserInfo();
            objClsUserInfo.setLoginStatus("DEACTIVE");
            objClsUserInfo.setMobileNo(edt_mobile.getText().toString().trim());
            ClsGlobal.setBasicUserInfo(objClsUserInfo, LoginActivityDemo.this);
        }
        Log.d("UserInfo", "----UserInfo---" + objClsUserInfoOld.getLoginStatus());

    }


    private void main() {

        input_user_id = findViewById(R.id.input_user_id);
        input_password = findViewById(R.id.input_password);
        input_email = findViewById(R.id.input_email);

        edt_email = findViewById(R.id.edt_email);
        edt_mobile = findViewById(R.id.edt_mobile);


        edt_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edt_mobile.length() == 10) {

                }
            }
        });
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);

        edt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());


        edt_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                        (actionId == EditorInfo.IME_ACTION_DONE)) {

                    btn_login.performClick();
                    return true;

                }
                return false;
            }
        });

        txt_createAccount = findViewById(R.id.txt_createAccount);
        txt_createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
//                startActivity(intent);

//                Intent intent = new Intent(getApplicationContext(), TrialActivity.class);
//                startActivity(intent);

//9
            }
        });

        forgot_password = findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityForgotPassword.class);
                startActivity(intent);
            }
        });

        chk_remember = findViewById(R.id.chk_remember);
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            edt_mobile.setText(loginPreferences.getString("username", ""));
            edt_password.setText(loginPreferences.getString("password", ""));
            chk_remember.setChecked(true);
        }


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = edt_mobile.getText().toString();
                password = edt_password.getText().toString();

                if (chk_remember.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", username);
                    loginPrefsEditor.putString("password", password);
                    loginPrefsEditor.commit();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }

                ClsGlobal.defaultPrinterName = ClsGlobal.GetSharedPreferencesFile
                        ("printerConfig", "pname", LoginActivityDemo.this);

                Log.d("btn_connect", "LoginActivity: " + ClsGlobal.defaultPrinterName);

                Calendar c = Calendar.getInstance();   // this takes current date
                c.set(Calendar.DAY_OF_MONTH, 1);

                String abcd = ClsGlobal.getFirstDateOfMonth(c.getTime());
                Log.d("getFirstDateOfMonth", "getFirstDateOfMonth: " + abcd);

                pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                pref.getString("printername", null);


                if (LOGINVALIDATION()) {
                    LoginAPI();
                }

                objClsUserInfoOld = ClsGlobal.getUserInfo(LoginActivityDemo.this);
                Log.d("getFirstDateOfMonth", "----UserInfo---" + objClsUserInfoOld.getLoginStatus());

                if (objClsUserInfoOld.getLoginStatus().equalsIgnoreCase("ACTIVE")) {
                    Log.d("getFirstDateOfMonth", "----UserInfo---" + objClsUserInfoOld.getLoginStatus());
                    Intent i = new Intent(getApplicationContext(), SHAP_Lite_Activity.class);
                    startActivity(i);

                }

//                Intent intent = new Intent(LoginActivityDemo.this,RegistrationActivity.class);
//                startActivity(intent);

            }
        });

        objClsUserInfoOld = ClsGlobal.getUserInfo(LoginActivityDemo.this);
        Log.d("getFirstDateOfMonth", "----UserInfo---" + objClsUserInfoOld.getLoginStatus());

        if (objClsUserInfoOld.getLoginStatus().equalsIgnoreCase("ACTIVE")) {
            Log.d("getFirstDateOfMonth", "----UserInfo---" + objClsUserInfoOld.getLoginStatus());
            Intent i = new Intent(getApplicationContext(), SHAP_Lite_Activity.class);
            startActivity(i);
//            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private boolean LOGINVALIDATION() {
        boolean result = true;

        if (!ClsGlobal.CheckInternetConnection(LoginActivityDemo.this)) {
            Toast.makeText(LoginActivityDemo.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!validateUserID()) {
            result = false;
        }

        if (!validatePassword()) {
            result = false;
        }
        if (input_email.getTag().toString() == "1") {
            String email = edt_email.getText().toString().trim();
            if (edt_email.getText() == null || email.isEmpty() || !ClsGlobal.isValidEmail(email)) {
                Toast.makeText(getApplicationContext(), R.string.err_msg_email, Toast.LENGTH_SHORT).show();
                edt_email.requestFocus();
                result = false;
            }
        }

        return result;
    }


    private boolean validateUserID() {
        if (edt_mobile.getText().toString().trim().isEmpty()) {
            ClsDialog.ErrorDialog("Alert", getString(R.string.err_msg_user_id), LoginActivityDemo.this);

            requestFocus(edt_mobile);
            return false;
        } else {
            input_user_id.setErrorEnabled(false);
        }
        Log.d("TAG", "validateUserID= \"\" ");
        return true;
    }

    private boolean validatePassword() {
        if (edt_password.getText().toString().trim().isEmpty()) {
//            input_password.setError(getString(R.string.err_msg_password));
            ClsDialog.ErrorDialog("Alert", getString(R.string.err_msg_password), LoginActivityDemo.this);

            requestFocus(edt_password);
            return false;
        } else {
            input_password.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void create_database() {
        db = openOrCreateDatabase(ClsGlobal.Database_Name, MODE_PRIVATE, null);
        createCategoryMaster();
        createItemMaster();
        createSizeMaster();
        createUnitMaster();
        createEmployeeMaster();
        createVendorMaster();
        createExpenseTypeMaster();
        createExpenseMasterNew();
        createExpenseMaster();
        createTaxMaster();
        createEmployeeDocumentMaster();
        createInventoryItemMaster();
        createInventoryStockMaster();
        createTableMaster();
        createOrderMaster();
        createOrderDetails();
        createTerms();
        createCustomerMaster();
        createEmailLogs();

        //----------------new Tables-------------------//
        createInventoryLayer();
        createLayerItemMaster();
        createItemTag();
        createItemLayer();
        createTaxSlab();
        createInventoryOrderDetail();
        createInventoryOrderMaster();

        createPaymentMaster();
        createPurchaseMaster();
        createPurchaseDetail();
        createCommonLogsMaster();


        db.close();

    }

    private void createCommonLogsMaster() {

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[CommonLogs_Master]")
                .concat("(")
                .concat("[CommonLogs_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[Remark] VARCHAR(500)")
                .concat(",[Status] VARCHAR(50)")
                .concat(",[Log_Type] VARCHAR(80)")
                .concat(",[Date_Time] datetime")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " CommonLogs_Master:- " + qry);

    }


    private void createPurchaseDetail() {

        //  db.execSQL("DROP TABLE [tbl_ItemTag]");

      *//*  String qry = "ALTER TABLE [PurchaseDetail] ADD [MonthYear] VARCHAR(10)";
        db.execSQL(qry);
*//*
        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[PurchaseDetail]")
                .concat("(")
                .concat("[PurchaseDetailID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[PurchaseID] INT")
                .concat(",[ItemID] INT")
                .concat(",[MonthYear] VARCHAR(10)")
                .concat(",[ItemCode] VARCHAR(20)")
                .concat(",[Unit] VARCHAR(50)")
                .concat(",[Quantity] DOUBLE")
                .concat(",[Rate] DOUBLE")
                .concat(",[TotalAmount] DOUBLE")//50*10
                .concat(",[Discount] DOUBLE")
                .concat(",[NetAmount] DOUBLE")
                .concat(",[ApplyTax] varchar(3)")
                .concat(",[CGST] DOUBLE")
                .concat(",[SGST] DOUBLE")
                .concat(",[IGST] DOUBLE")
                .concat(",[TotalTaxAmount] DOUBLE") //Total tax amt
                .concat(",[GrandTotal] DOUBLE") //finalAmt
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " Terms " + qry);
    }

    private void createPurchaseMaster() {

        //  db.execSQL("DROP TABLE [tbl_ItemTag]");
        *//*String qry = "ALTER TABLE [PurchaseMaster] ADD [VendorID] INT";

        db.execSQL(qry);*//*


//        db.execSQL("ALTER TABLE [PurchaseMaster]  RENAME [PurchaseNo] VARCHAR(100)");
//        db.execSQL("DROP TABLE [PurchaseMaster]  [PurchaseNo] VARCHAR(100)");

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[PurchaseMaster]")
                .concat("(")
                .concat("[PurchaseID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[PurchaseNo] INT")
                .concat(",[VendorID] INT")
                .concat(",[BillNO] VARCHAR(30)")
                .concat(",[PurchaseDate] DATE")
                .concat(",[Remark] VARCHAR(300)")
                .concat(",[EntryDate] DATETIME")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " Terms " + qry);
    }


    private void createPaymentMaster() {


        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[PaymentMaster]")
                .concat("(")
                .concat("[PaymentID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[PaymentDate] DATETIME")
                .concat(",[PaymentMounth] VARCHAR(20)")
                .concat(",[VendorID] INT")
                .concat(",[MobileNo] VARCHAR(20)")
                .concat(",[CustomerName] VARCHAR(100)")
                .concat(",[VendorName] VARCHAR(100)")
                .concat(",[PaymentMode] VARCHAR(20)")
                .concat(",[PaymentDetail] VARCHAR(150)")
                .concat(",[InvoiceNo] VARCHAR(80)")
                .concat(",[Amount] DOUBLE")
                .concat(",[Remark] VARCHAR(500)")
                .concat(",[EntryDate] DATETIME")
                .concat(",[Type] VARCHAR(10)")
                .concat(",[ReceiptNo] INT")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " tbl_Tax_Slab " + qry);
    }

    private void createInventoryOrderDetail() {

//         db.execSQL("ALTER TABLE [InventoryOrderDetail] ADD [Discount_per] DOUBLE");
//         db.execSQL("ALTER TABLE [InventoryOrderDetail] ADD [Discount_amt] DOUBLE");
        //  ItemComment

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[InventoryOrderDetail]")
                .concat("(")
                .concat("[OrderDetailID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[OrderID] int")
                .concat(",[OrderNo] int")
                .concat(",[ItemCode] VARCHAR(50)")
                .concat(",[Item] VARCHAR(100)")
                .concat(",[ItemComment] VARCHAR(200)")
                .concat(",[Rate] DOUBLE")
                .concat(",[SaleRate] DOUBLE")//100

                .concat(",[SaleRateWithoutTax] DOUBLE")// 84.55

                .concat(",[Quantity] DOUBLE")
                .concat(",[Discount_per] DOUBLE")
                .concat(",[Discount_amt] DOUBLE")
                .concat(",[Amount] DOUBLE")
                .concat(",[CGST] DOUBLE")
                .concat(",[SGST] DOUBLE")
                .concat(",[IGST] DOUBLE")
                .concat(",[TotalTaxAmount] DOUBLE")//15.25
                .concat(",[GrandTotal] DOUBLE")
                .concat(",[SaveStatus] VARCHAR(3)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " tbl_Tax_Slab " + qry);
    }

    private void createInventoryOrderMaster() {

        // Different_Amount_mode
        // db.execSQL("ALTER TABLE [InventoryOrderMaster] ADD [Different_Amount_mode] VARCHAR(50)");
//        db.execSQL("ALTER TABLE [InventoryOrderMaster] ADD [SaleReturnDiscount] DOUBLE");


        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[InventoryOrderMaster]")
                .concat("(")
                .concat("[OrderID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[MobileNo] VARCHAR(15)")
                .concat(",[CustomerName] VARCHAR(100)")
                .concat(",[CompanyName] VARCHAR(100)")
                .concat(",[GSTNo] VARCHAR(50)")
                .concat(",[BillDate] DATETIME")
                .concat(",[OrderNo] INT")
                .concat(",[SaleType] VARCHAR(15)") // Sale & WholeSale

                .concat(",[SaleReturnDiscount] DOUBLE")   //1050
                .concat(",[TotalAmount] DOUBLE")   //1050
                .concat(",[DiscountAmount] DOUBLE") //50
                .concat(",[TotalPaybleAmount] DOUBLE") //1000
                .concat(",[TotalTaxAmount] DOUBLE")   // 100
                .concat(",[TotalReceiveableAmount] DOUBLE")   // 1100
                .concat(",[PaidAmount] DOUBLE") //1050
                .concat(",[AdjumentAmount] DOUBLE") //50

                .concat(",[EntryDate] DATETIME")
                .concat(",[ApplyTax] VARCHAR(3)")
                .concat(",[PaymentMode] VARCHAR(20)")
                .concat(",[PaymentDetail] VARCHAR(50)")
                .concat(",[Different_Amount_mode] VARCHAR(50)")
//                .concat(",[BillTo] VARCHAR(8)")

                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " tbl_Tax_Slab " + qry);

    }

    private void createTaxSlab() {
//         db.execSQL("DROP TABLE [tbl_Tax_Slab]");


        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[tbl_Tax_Slab]")
                .concat("(")
                .concat("[SLAB_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[SLAB_NAME] VARCHAR(100)")
                .concat(",[SGST] DOUBLE")
                .concat(",[CGST] DOUBLE")
                .concat(",[IGST] DOUBLE")
                .concat(",[ACTIVE] VARCHAR (3)")
                .concat(",[REMARK] VARCHAR(100)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);

        boolean exists;
        ClsTaxSlab objClsUnitMaster = new ClsTaxSlab(LoginActivityDemo.this);

        String where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("18.0% IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("18.0% IGST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(18.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("18.0% IGST");
            ClsTaxSlab.Insert(LoginActivityDemo.this, objClsUnitMaster);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("0% IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("0% IGST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("0% IGST");
            ClsTaxSlab.Insert(LoginActivityDemo.this, objClsUnitMaster);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("Out of GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("Out of GST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("Out of GST");
            ClsTaxSlab.Insert(LoginActivityDemo.this, objClsUnitMaster);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("0% GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("0% GST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("0% GST");
            ClsTaxSlab.Insert(LoginActivityDemo.this, objClsUnitMaster);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("6.0% IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("6.0% IGST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(6.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("6.0% IGST");
            ClsTaxSlab.Insert(LoginActivityDemo.this, objClsUnitMaster);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("28.0% IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("28.0% IGST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(28.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("28.0% IGST");
            ClsTaxSlab.Insert(LoginActivityDemo.this, objClsUnitMaster);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("28.0% GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("28.0% GST");
            objClsUnitMaster.setSGST(14.0);
            objClsUnitMaster.setCGST(14.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("28.0% GST");
            ClsTaxSlab.Insert(LoginActivityDemo.this, objClsUnitMaster);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("12.0% GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("12.0% GST");
            objClsUnitMaster.setSGST(6.0);
            objClsUnitMaster.setCGST(6.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("12.0% GST");
            ClsTaxSlab.Insert(LoginActivityDemo.this, objClsUnitMaster);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("18.0% GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("18.0% GST");
            objClsUnitMaster.setSGST(9.0);
            objClsUnitMaster.setCGST(9.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("18.0% GST");
            ClsTaxSlab.Insert(LoginActivityDemo.this, objClsUnitMaster);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("3.0% GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("3.0% GST");
            objClsUnitMaster.setSGST(1.5);
            objClsUnitMaster.setCGST(1.5);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("3.0% GST");
            ClsTaxSlab.Insert(LoginActivityDemo.this, objClsUnitMaster);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("0.25% IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("0.25% IGST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(0.25);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("0.25% IGST");
            ClsTaxSlab.Insert(LoginActivityDemo.this, objClsUnitMaster);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("5.0% GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("5.0% GST");
            objClsUnitMaster.setSGST(2.5);
            objClsUnitMaster.setCGST(2.5);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("5.0% GST");
            ClsTaxSlab.Insert(LoginActivityDemo.this, objClsUnitMaster);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("6.0% GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("6.0% GST");
            objClsUnitMaster.setSGST(3.0);
            objClsUnitMaster.setCGST(3.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("6.0% GST");
            ClsTaxSlab.Insert(LoginActivityDemo.this, objClsUnitMaster);
        }


        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("0.25% GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("0.25% GST");
            objClsUnitMaster.setSGST(0.125);
            objClsUnitMaster.setCGST(0.125);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("0.25% GST");
            ClsTaxSlab.Insert(LoginActivityDemo.this, objClsUnitMaster);
        }


        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("Exempt IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("Exempt IGST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("Exempt IGST");
            ClsTaxSlab.Insert(LoginActivityDemo.this, objClsUnitMaster);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("3.0% IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("3.0% IGST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(3.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("3.0% IGST");
            ClsTaxSlab.Insert(LoginActivityDemo.this, objClsUnitMaster);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("5.0% IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("5.0% IGST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(5.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("5.0% IGST");
            ClsTaxSlab.Insert(LoginActivityDemo.this, objClsUnitMaster);
        }

        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("Exempt GST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("Exempt GST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(0.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("Exempt GST");
            ClsTaxSlab.Insert(LoginActivityDemo.this, objClsUnitMaster);
        }
        where = " AND  [SLAB_NAME] = "
                .concat("'")
                .concat("12.0% IGST")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where);

        if (!exists) {
            objClsUnitMaster.setSLAB_NAME("12.0% IGST");
            objClsUnitMaster.setSGST(0.0);
            objClsUnitMaster.setCGST(0.0);
            objClsUnitMaster.setIGST(12.0);
            objClsUnitMaster.setACTIVE("YES");
            objClsUnitMaster.setREMARK("12.0% IGST");
            ClsTaxSlab.Insert(LoginActivityDemo.this, objClsUnitMaster);
        }

        Log.e("CREATETABLE", " tbl_Tax_Slab " + qry);

    }

    private void createItemTag() {

        //  db.execSQL("DROP TABLE [tbl_ItemTag]");

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[tbl_ItemTag]")
                .concat("(")
                .concat("[ITEMTAG_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[ITEMID] INT")
                .concat(",[ITEMNAME] VARCHAR(100)")
                .concat(",[TAGNAME] VARCHAR(100)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " Terms " + qry);
    }

    private void createItemLayer() {

        // db.execSQL("DROP TABLE [tbl_ItemLayer]");

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[tbl_ItemLayer]")
                .concat("(")
                .concat("[ITEMLAYER_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[LAYERITEM_ID] INT")//layerID
                .concat(",[LAYER_NAME] VARCHAR(40)")
                .concat(",[ITEM_ID] INT")
                .concat(",[ITEM_NAME] VARCHAR(40)")
                .concat(",[LAYER_VALUE] VARCHAR(100)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " Terms " + qry);
    }


    private void createLayerItemMaster() {

        // db.execSQL("DROP TABLE [tbl_LayerItem_Master]");

      *//*  String qryAdd_OPENING_STOCK = "ALTER TABLE [tbl_LayerItem_Master] ADD [WHOLESALE_RATE] DOUBLE";
        db.execSQL(qryAdd_OPENING_STOCK);

        String qryAddTaxType = "ALTER TABLE [tbl_LayerItem_Master] ADD [TAX_TYPE] VARCHAR(10)";
        db.execSQL(qryAddTaxType);*//*


        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[tbl_LayerItem_Master]")
                .concat("(")
                .concat("[LAYERITEM_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[ITEM_NAME] VARCHAR(100)")
                .concat(",[ITEM_CODE] VARCHAR(15)")
                .concat(",[RATE_PER_UNIT] DOUBLE")
                .concat(",[WHOLESALE_RATE] DOUBLE")
                .concat(",[TAX_TYPE] VARCHAR(10)")
                .concat(",[REMARK] VARCHAR(500)")
                .concat(",[MIN_STOCK] DOUBLE")
                .concat(",[MAX_STOCK] DOUBLE")
                .concat(",[UNIT_CODE] VARCHAR(20)")
                .concat(",[TAGS]  VARCHAR(500)")
                .concat(",[ACTIVE] VARCHAR (3)")
                .concat(",[DISPLAY_ORDER] INT")
                .concat(",[OPENING_STOCK] DOUBLE")
                .concat(",[HSN_SAC_CODE] VARCHAR(20)")
                .concat(",[TAX_APPLY] VARCHAR (3)")
                .concat(",[TAX_SLAB_ID] INT")
                .concat(")")
                .concat(";");

        db.execSQL(qry);

        Log.e("CREATETABLE", " Terms " + qry);
    }

    private void createInventoryLayer() {
        // db.execSQL("ALTER TABLE [tbl_InventoryLayer] ADD [SELECTED_LAYER_NAME] VARCHAR(40)");
        // db.execSQL("DROP TABLE [tbl_LayerItem_Master]");
        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[tbl_InventoryLayer]")
                .concat("(")
                .concat("[INVENTORYLAYER_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[LAYER_NAME] VARCHAR(40)")
                .concat(",[PARENT_ID] INT")
                .concat(",[SELECTED_LAYER_NAME] VARCHAR(40)")
                .concat(",[DISPLAY_ORDER] INT")
                .concat(",[REMARK] VARCHAR(40)")
                .concat(",[ACTIVE] VARCHAR (3)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " Terms " + qry);
    }

    private void createTerms() {

//        db.execSQL("ALTER TABLE [tbl_Terms] ADD [TERM_TYPE] VARCHAR(50)");

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[tbl_Terms]")
                .concat("(")
                .concat("[TERMS_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[INVOICE_TYPES] VARCHAR(20)")
                .concat(",[TERMS] VARCHAR(200)")
                .concat(",[TERM_TYPE] VARCHAR(50)")
                .concat(",[SORT_NO] INT")
                .concat(",[ACTIVE] VARCHAR (3)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " Terms " + qry);
    }


    private void createOrderDetails() {
        //   db.execSQL("DROP TABLE [OrderDetail_master]");
        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[OrderDetail_master]")
                .concat("(")
                .concat("[ORDERDETAIL_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[ORDER_ID] INT")
                .concat(",[ORDER_NO] VARCHAR(6)")
                .concat(",[ITEM_ID] INT")
                .concat(",[ITEM_NAME] VARCHAR (100)")
                .concat(",[RATE] DOUBLE")
                .concat(",[QUANTITY] DOUBLE")
                .concat(",[TOTAL_AMOUNT] DOUBLE")   //rate*qty..
                .concat(",[OTHER_TAX1] VARCHAR(50)")
                .concat(",[OTHER_VAL1] DOUBLE")
                .concat(",[OTHER_TAX2] VARCHAR(50)")
                .concat(",[OTHER_VAL2] DOUBLE")
                .concat(",[OTHER_TAX3] VARCHAR(50)")
                .concat(",[OTHER_VAL3] DOUBLE")
                .concat(",[OTHER_TAX4] VARCHAR(50)")
                .concat(",[OTHER_VAL4] DOUBLE")
                .concat(",[OTHER_TAX5] VARCHAR(50)")
                .concat(",[OTHER_VAL5] DOUBLE")
                .concat(",[TOTAL_TAXAMOUNT] DOUBLE")
                .concat(",[GRAND_TOTAL] DOUBLE")
                .concat(",[TYPE] VARCHAR (10)")        //serve,parcel
                .concat(",[STATUS] VARCHAR (20)")      //cooking,ready to serve etc.
                .concat(",[ENTRYDATETIME] DATETIME")
                .concat(")")
                .concat(";");


        db.execSQL(qry);
        Log.e("CREATETABLE", " orderdetails " + qry);
    }


    private void createEmailLogs() {

        // db.execSQL("DROP TABLE [tbl_EmailLogs]");

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[tbl_EmailLogs]")
                .concat("(")
                .concat("[EMAILLOGS_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[MESSAGE] VARCHAR(500)")//success:successfully sent, fail:internet connectivity not awailbale,
                .concat(",[DATE_TIME] datetime")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", " EmailLogs " + qry);
    }


    private void createCustomerMaster() {

//        db.execSQL("ALTER TABLE [CustomerMaster] ADD [Company_Name] VARCHAR (100)");
//        db.execSQL("ALTER TABLE [CustomerMaster] ADD [GST_NO] VARCHAR (20)");
//        db.execSQL("ALTER TABLE [CustomerMaster] ADD [Address] VARCHAR (100)");
//        db.execSQL("ALTER TABLE [CustomerMaster] ADD [Credit] DOUBLE");
//        db.execSQL("ALTER TABLE [CustomerMaster] ADD [OpeningStock] DOUBLE");

//        db.execSQL("ALTER TABLE [CustomerMaster] ADD [Email] VARCHAR (100)");
//        db.execSQL("ALTER TABLE [CustomerMaster] ADD [Note] VARCHAR (500)");
//        db.execSQL("ALTER TABLE [CustomerMaster] ADD [Save_Contact] VARCHAR (3)");
//        db.execSQL("ALTER TABLE [CustomerMaster] ADD [BalanceType] VARCHAR(15)");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[CustomerMaster]")
                .concat("(")
                .concat("[ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[NAME] VARCHAR (20)")
                .concat(",[Company_Name] VARCHAR (100)")
                .concat(",[GST_NO] VARCHAR (20)")
                .concat(",[Address] VARCHAR (100)")
                .concat(",[MOBILE_NO] VARCHAR (15)")/// Expense Type
                .concat(",[Credit] DOUBLE")/// Expense Type
                .concat(",[OpeningStock] DOUBLE")/// Expense Type
                .concat(",[Email] VARCHAR (100)")/// Expense Type
                .concat(",[BalanceType] VARCHAR (15)")/// Expense Type
                .concat(",[Note] VARCHAR (500)")/// Expense Type
                .concat(",[Save_Contact] VARCHAR (3)")/// Expense Type
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", qry);
    }

    private void createOrderMaster() {

//        db.execSQL("DROP TABLE [Ordermaster]");
        //       db.execSQL("ALTER TABLE [Ordermaster] ADD [GRAND_TOTAL] DOUBLE");

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[Ordermaster]")
                .concat("(")
                .concat("[ORDER_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[ORDER_NO] VARCHAR (6)")//y
                .concat(",[ORDER_DATETIME] DATETIME")//y
                .concat(",[TABLE_ID] INT")//n
                .concat(",[TABLE_NO] VARCHAR (10)")//y
                .concat(",[MOBILE_NO] VARCHAR (15)")//y     //Retail or Table..
                .concat(",[SOURCE] VARCHAR (20)")//y     //Retail or Table..
                .concat(",[ENTRYDATETIME] DATETIME")//n  //dbformate..
                .concat(",[TOTAL_TAXAMOUNT] DOUBLE")//y
                .concat(",[TOTAL_AMOUNT] DOUBLE")//y
                .concat(",[DISCOUNT] DOUBLE")//y
                .concat(",[GRAND_TOTAL] DOUBLE")//y
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", "OrderMaster" + qry);
    }

    private void createTableMaster() {
//        String Alterqry = "ALTER TABLE [Table_master] ADD [ORDER_NO] VARCHAR(6)";
//        db.execSQL(Alterqry);

        String qry = "CREATE TABLE IF NOT EXISTS"
                .concat("[Table_master]")
                .concat("(")
                .concat("[TABLE_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[TABLE_NO] VARCHAR(100)")
                .concat(",[ACTIVE] VARCHAR (3)")
                .concat(",[SITTING_CAPACITY] INT")
                .concat(",[STATUS] VARCHAR (20)")
                .concat(",[SORT_NO] INT")
                .concat(",[REMARK] VARCHAR(200)")
                .concat(",[ORDER_NO] VARCHAR(6)")
                .concat(")")
                .concat(";");

        db.execSQL(qry);
        Log.e("CREATETABLE", "TableMaster--" + qry);

    }

    private void createTaxMaster() {
//                db.execSQL("DROP TABLE [Taxes]");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[Taxes]")
                .concat("(")
                .concat("[TAX_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[TAX_TYPE] VARCHAR(10)")
                .concat(",[TAX_NAME] VARCHAR(100)")
                .concat(",[TAX_VALUE] DOUBLE")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "TaxMaster---->>>" + qry);
        db.execSQL(qry);
    }

    private void createEmployeeDocumentMaster() {
//   db.execSQL("DROP TABLE [EmployeeDocument]");


//        db.execSQL("ALTER TABLE [EmployeeDocument] ADD [TYPE] VARCHAR(10)");


        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[EmployeeDocument]")
                .concat("(")
                .concat("[DOCUMENT_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[EMP_ID] INTEGER")
                .concat(",[DOCUMENT_NAME] VARCHAR(50)")
                .concat(",[OTHER_PROF] VARCHAR(50)")
                .concat(",[DOCUMENT_NO] VARCHAR(50)")
                .concat(",[FILE_PATH] VARCHAR(100)")
                .concat(",[EXP_DATE] VARCHAR(10)")
                .concat(",[TYPE] VARCHAR(10)")
                .concat(",[FILE_NAME] VARCHAR(100)")
                .concat(")")
                .concat(";");
        Log.e("CREATETABLE", "EmployeeDocument---->>>" + qry);
        db.execSQL(qry);
    }


    private void createExpenseMaster() {
//           db.execSQL("DROP TABLE [ExpenseMaster]");
//        String qry = "CREATE TABLE IF NOT EXISTS "
//                .concat("[ExpenseMaster]")
//                .concat("(")
//                .concat("[Expense_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
//                .concat(",[VENDOR_ID] INTEGER")
//                .concat(",[EXPENSE_TYPE_ID] INTEGER")/// Expense Type
//                .concat(",[VENDOR_NAME] VARCHAR(50)")//
//                .concat(",[EMPLOYEE_NAME] VARCHAR(50)")//
//                .concat(",[RECEIPT_NO] VARCHAR(20)")//
//                .concat(",[RECEIPT_DATE] DATE")//
//                .concat(",[AMOUNT] DOUBLE")//
//                .concat(",[OTHER_TAX1] VARCHAR(50)")// GST
//                .concat(",[OTHER_VAL1] DOUBLE")//
//                .concat(",[OTHER_TAX2] VARCHAR(50)")//
//                .concat(",[OTHER_VAL2] DOUBLE")//
//                .concat(",[OTHER_TAX3] VARCHAR(50)")//
//                .concat(",[OTHER_VAL3] DOUBLE")//
//                .concat(",[OTHER_TAX4] VARCHAR(50)")//
//                .concat(",[OTHER_VAL4] DOUBLE")//
//                .concat(",[OTHER_TAX5] VARCHAR(50)")//
//                .concat(",[OTHER_VAL5] DOUBLE")//
//                .concat(",[DISCOUNT] DOUBLE")//
//                .concat(",[GRAND_TOTAL] DOUBLE")//
//                .concat(",[ENTRY_DATE] DATETIME")//
//                .concat(",[REMARK] VARCHAR(200)")//
//                .concat(")")
//                .concat(";");
//        db.execSQL(qry);
//
//        Log.e("CREATETABLE","EXPENSE---->>>"+ qry);
//        db.execSQL(qry);


//           db.execSQL("DROP TABLE [ExpenseMaster]");
        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[ExpenseMaster]")
                .concat("(")
                .concat("[EXPENSE_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[VENDOR_ID] INTEGER,")
                .concat("[EXPENSE_TYPE_ID] INTEGER,")
                .concat("[VENDOR_NAME] VARCHAR(100),")
                .concat("[EMPLOYEE_NAME] VARCHAR(100),")
                .concat("[AMOUNT] DOUBLE,")
                .concat("[OTHER_TAX1] VARCHAR(50),")
                .concat("[OTHER_VAL1] DOUBLE,")
                .concat("[OTHER_TAX2] VARCHAR(50),")
                .concat("[OTHER_VAL2] DOUBLE,")
                .concat("[OTHER_TAX3] VARCHAR(50),")
                .concat("[OTHER_VAL3] DOUBLE,")
                .concat("[OTHER_TAX4] VARCHAR(50),")
                .concat("[OTHER_VAL4] DOUBLE,")
                .concat("[OTHER_TAX5] VARCHAR(50),")
                .concat("[OTHER_VAL5] DOUBLE,")
                .concat("[DISCOUNT] DOUBLE,")
                .concat("[GRAND_TOTAL] DOUBLE,")
                .concat("[BILL_RECEIPT_NO] VARCHAR(20),")
                .concat("[TRANSACTION_DATE] DATE,")
                .concat("[ENTRY_DATE] DATETIME,")
                .concat("[REMARK] VARCHAR(200)")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "EXPENSE---->>>" + qry);
        db.execSQL(qry);

    }


    private void createExpenseMasterNew() {

//        db.execSQL("DROP TABLE [ExpenseMasterNew]");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[ExpenseMasterNew]")
                .concat("(")
                .concat("[Expense_ID] INTEGER PRIMARY KEY AUTOINCREMENT")
                .concat(",[VENDOR_ID] INTEGER")
                .concat(",[EXPENSE_TYPE_ID] INTEGER")/// Expense Type
                .concat(",[VENDOR_NAME] VARCHAR(50)")//
                .concat(",[EMPLOYEE_NAME] VARCHAR(50)")//
                .concat(",[RECEIPT_NO] VARCHAR(20)")//
                .concat(",[RECEIPT_DATE] DATE")//
                .concat(",[AMOUNT] DOUBLE")//
                .concat(",[OTHER_TAX1] VARCHAR(50)")// GST
                .concat(",[OTHER_VAL1] DOUBLE")//
                .concat(",[OTHER_TAX2] VARCHAR(50)")//
                .concat(",[OTHER_VAL2] DOUBLE")//
                .concat(",[OTHER_TAX3] VARCHAR(50)")//
                .concat(",[OTHER_VAL3] DOUBLE")//
                .concat(",[OTHER_TAX4] VARCHAR(50)")//
                .concat(",[OTHER_VAL4] DOUBLE")//
                .concat(",[OTHER_TAX5] VARCHAR(50)")//
                .concat(",[OTHER_VAL5] DOUBLE")//
                .concat(",[DISCOUNT] DOUBLE")//
                .concat(",[GRAND_TOTAL] DOUBLE")//
                .concat(",[ENTRY_DATE] DATETIME")//
                .concat(",[REMARK] VARCHAR(200)")//
                .concat(")")
                .concat(";");
        db.execSQL(qry);
        Log.e("CREATETABLE", qry);

    }


    private void createExpenseTypeMaster() {

//                db.execSQL("DROP TABLE [EXPENSE_TYPE_MASTER]");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[EXPENSE_TYPE_MASTER]")
                .concat("(")
                .concat("[EXPENSE_TYPE_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[EXPENSE_TYPE_NAME] VARCHAR(100),")
                .concat("[ACTIVE] VARCHAR(3),")
                .concat("[REMARK] VARCHAR(200),")
                .concat("[SORT_NO] INTEGER")
                .concat(")")
                .concat(";");


        Log.e("CREATETABLE", "EXPENSE_TYPE_MASTER---->>>" + qry);
        db.execSQL(qry);

//        ClsExpenseType Obj = new ClsExpenseType(LogInActivity.this);
//        String where = " AND  [EXPENSE_TYPE_NAME] = "
//                .concat("'")
//                .concat("SALARY")
//                .concat("' ");


//        boolean exists = Obj.checkExists(where);
//        if (!exists) {
//            Obj.setExpense_type_name("SALARY");
//            ClsExpenseType.Insert(Obj);
//
//        }


    }


    private void createInventoryItemMaster() {
//        db.execSQL("DROP TABLE [INVENTORY_ITEM_MASTER]");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[INVENTORY_ITEM_MASTER]")
                .concat("(")
                .concat("[INVENTORY_ITEM_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[INVENTORY_ITEM_NAME] VARCHAR(100),")
                .concat("[UNIT_ID] INTEGER,")
                .concat("[UNIT_NAME] VARCHAR(100),")
                .concat("[MAX_STOCK_QTY] DOUBLE,")
                .concat("[MIN_STOCK_QTY] DOUBLE,")
                .concat("[ACTIVE] VARCHAR(3),")
                .concat("[REMARK] VARCHAR(200)")
                .concat(")")
                .concat(";");


        Log.e("CREATETABLE", "INVENTORY_ITEM_MASTER---->>>" + qry);
        db.execSQL(qry);

    }

    private void createInventoryStockMaster() {
//        db.execSQL("DROP TABLE [Inventory_stock_master]");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[Inventory_stock_master]")
                .concat("(")
                .concat("[STOCK_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[ORDER_ID] INTEGER,")
                .concat("[VENDOR_ID] INTEGER,")
                .concat("[INVENTORY_ITEM_ID] INTEGER,")
                .concat("[INVENTORY_ITEM_NAME] VARCHAR(100),")
                .concat("[QUANTITY] DOUBLE,")
                .concat("[AMOUNT] DOUBLE,")
                .concat("[TYPE] VARCHAR(3),")
                .concat("[ENTRY_DATE] DATETIME,")
                .concat("[TRANSACTION_DATE] DATE,")
                .concat("[REMARK] VARCHAR(200)")
                .concat(")")
                .concat(";");


        Log.e("CREATETABLE", "INVENTORY_STOCK_MASTER---->>>" + qry);
        db.execSQL(qry);
    }

    private void createEmployeeMaster() {

//        db.execSQL("DROP TABLE [EMPLOYEE_MASTER]");
        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[EMPLOYEE_MASTER]")
                .concat("(")
                .concat("[EMPLOYEE_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[EMPLOYEE_NAME] VARCHAR(100),")
                .concat("[CONTACT_NO] VARCHAR(12),")
                .concat("[ADDRESS] VARCHAR(300),")
                .concat("[EMPLOYEE_IMAGE] VARCHAR(100),")
                .concat("[SALARY] DOUBLE(20),")
                .concat("[DOB] DATE,")
                .concat("[ACTIVE] VARCHAR(3),")
                .concat("[REMARK] VARCHAR(200),")
                .concat("[SORT_NO] INTEGER")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "EMP_MASTER---->>>" + qry);
        db.execSQL(qry);
    }

    private void createItemMaster() {

//            db.execSQL("DROP TABLE [ITEM_MASTER]");

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[ITEM_MASTER]")
                .concat("(")
                .concat("[ITEM_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[ITEM_NAME] VARCHAR(100),")
                .concat("[CATEGORY_ID] INTEGER,")
                .concat("[CATEGORY_NAME] VARCHAR(100),")
                .concat("[FOOD_TYPE] VARCHAR(10),")
                .concat("[ACTIVE] VARCHAR(3),")
                .concat("[REMARK] VARCHAR(200),")
                .concat("[SORT_NO] INTEGER,")
                .concat("[PRICE] DOUBLE")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "ITEM_MASTER---->>>" + qry);
        db.execSQL(qry);
    }


    private void createVendorMaster() {

        //   db.execSQL("DROP TABLE [VENDOR_MASTER]");

//        db.execSQL("ALTER TABLE [VENDOR_MASTER] ADD [BalanceType] VARCHAR(15)");




        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[VENDOR_MASTER]")
                .concat("(")
                .concat("[VENDOR_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[VENDOR_NAME] VARCHAR(100),")
                .concat("[CONTACT_NO] VARCHAR(15),")
                .concat("[ADDRESS] VARCHAR(200),")
                .concat("[GST_NO] VARCHAR(20),")
                .concat("[TYPE] VARCHAR(15),")
                .concat("[ACTIVE] VARCHAR(3),")
                .concat("[REMARK] VARCHAR(200),")
                .concat("[BalanceType] VARCHAR(15),")
                .concat("[OpeningStock] DOUBLE,")
                .concat("[SORT_NO] INTEGER")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "VENDOR_MASTER---->>>" + qry);
        db.execSQL(qry);

        ClsVendor objvendor = new ClsVendor(LoginActivityDemo.this);
        String where = " AND  [VENDOR_NAME] = "
                .concat("'")
                .concat("OTHER")
                .concat("' ");
        boolean exists = objvendor.checkExists(where,db);
        if (!exists) {
            objvendor.setVendor_name("OTHER");
            ClsVendor.Insert(objvendor,db);
        }


    }

    private void createUnitMaster() {
        String where = "";

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[UNIT_MASTER]")
                .concat("(")
                .concat("[UNIT_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[UNIT_NAME] VARCHAR(100),")
                .concat("[ACTIVE] VARCHAR(3),")
                .concat("[REMARK] VARCHAR(200),")
                .concat("[SORT_NO] INTEGER")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "UNIT_MASTER---->>>" + qry);
        db.execSQL(qry);

        boolean exists;

        ClsUnit objClsUnitMaster = new ClsUnit(LoginActivityDemo.this);
        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("KG")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where,db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("KG");
            objClsUnitMaster.setRemark("kilogram");
            objClsUnitMaster.setSort_no(1);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster,db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("LTR")
                .concat("' ");


        exists = objClsUnitMaster.checkExists(where,db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("LTR");
            objClsUnitMaster.setRemark("liter");
            objClsUnitMaster.setSort_no(2);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster,db);
        }
        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("PIECE")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where,db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("PIECE");
            objClsUnitMaster.setRemark("single pieces");
            objClsUnitMaster.setSort_no(3);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster,db);
        }
        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("BOX")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where,db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("BOX");
            objClsUnitMaster.setRemark("box");
            objClsUnitMaster.setSort_no(4);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster,db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("NO.")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where,db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("NO.");
            objClsUnitMaster.setRemark("number");
            objClsUnitMaster.setSort_no(5);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster,db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("GRAM")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where,db);

        if (!exists) {
            objClsUnitMaster.setUnit_name("GRAM");
            objClsUnitMaster.setRemark("GRAM");
            objClsUnitMaster.setSort_no(6);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster,db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("HECTOGRAM")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where,db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("HECTOGRAM");
            objClsUnitMaster.setRemark("HECTOGRAM");
            objClsUnitMaster.setSort_no(7);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster,db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("DECIGRAM")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where,db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("DECIGRAM");
            objClsUnitMaster.setRemark("DECIGRAM");
            objClsUnitMaster.setSort_no(8);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster,db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("CENTIGRAM")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where,db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("CENTIGRAM");
            objClsUnitMaster.setRemark("CENTIGRAM");
            objClsUnitMaster.setSort_no(9);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster,db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("MILLIGRAM")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where,db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("MILLIGRAM");
            objClsUnitMaster.setRemark("MILLIGRAM");
            objClsUnitMaster.setSort_no(9);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster,db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("CARAT")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where,db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("CARAT");
            objClsUnitMaster.setRemark("CARAT");
            objClsUnitMaster.setSort_no(10);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster,db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("METER")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where,db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("METER");
            objClsUnitMaster.setRemark("METER");
            objClsUnitMaster.setSort_no(11);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster,db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("DECIMETER")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where,db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("DECIMETER");
            objClsUnitMaster.setRemark("DECIMETER");
            objClsUnitMaster.setSort_no(13);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster,db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("CENTIMETER")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where,db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("CENTIMETER");
            objClsUnitMaster.setRemark("CENTIMETER");
            objClsUnitMaster.setSort_no(14);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster,db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("MILLIMETER")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where,db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("MILLIMETER");
            objClsUnitMaster.setRemark("MILLIMETER");
            objClsUnitMaster.setSort_no(15);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster,db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("MILE")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where,db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("MILE");
            objClsUnitMaster.setRemark("MILE");
            objClsUnitMaster.setSort_no(16);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster,db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("INCH")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where,db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("INCH");
            objClsUnitMaster.setRemark("INCH");
            objClsUnitMaster.setSort_no(17);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster,db);
        }

        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("FOOT")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where,db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("FOOT");
            objClsUnitMaster.setRemark("FOOT");
            objClsUnitMaster.setSort_no(18);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster,db);
        }
        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("YARD")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where,db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("YARD");
            objClsUnitMaster.setRemark("YARD");
            objClsUnitMaster.setSort_no(19);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster,db);
        }
        where = " AND  [UNIT_NAME] = "
                .concat("'")
                .concat("MICROGRAM")
                .concat("' ");

        exists = objClsUnitMaster.checkExists(where,db);
        if (!exists) {
            objClsUnitMaster.setUnit_name("MICROGRAM");
            objClsUnitMaster.setRemark("MICROGRAM");
            objClsUnitMaster.setSort_no(20);
            objClsUnitMaster.setActive("YES");
            ClsUnit.Insert(objClsUnitMaster,db);
        }
    }

    private void createSizeMaster() {

        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[SIZE_MASTER]")
                .concat("(")
                .concat("[SIZE_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[SIZE_NAME] VARCHAR(100),")
                .concat("[ACTIVE] VARCHAR(3),")
                .concat("[REMARK] VARCHAR(200),")
                .concat("[SORT_NO] INTEGER")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "SIZE_MASTER---->>>" + qry);
        db.execSQL(qry);
    }


    private void createCategoryMaster() {
        String qry = "CREATE TABLE IF NOT EXISTS "
                .concat("[CATEGORY_MASTER]")
                .concat("(")
                .concat("[CATEGORY_ID] INTEGER PRIMARY KEY AUTOINCREMENT ,")
                .concat("[CATEGORY_NAME] VARCHAR(100),")
                .concat("[ACTIVE] VARCHAR(3),")
                .concat("[REMARK] VARCHAR(200),")
                .concat("[SORT_NO] INTEGER")
                .concat(")")
                .concat(";");

        Log.e("CREATETABLE", "CATEGORY_MASTER---->>>" + qry);
        db.execSQL(qry);

    }


    void LoginAPI() {
        String UserType = "Employee";

        ClsLoginParams objClsLoginParams = new ClsLoginParams();
        objClsLoginParams.setUserType(UserType);
        objClsLoginParams.setPassword(edt_password.getText().toString().trim());
        objClsLoginParams.setMobileNumber(edt_mobile.getText().toString().trim());
        objClsLoginParams.setProductName(ClsGlobal.AppName);
        objClsLoginParams.setApplicationVersion(ClsGlobal.getApplicationVersion(LoginActivityDemo.this));
        objClsLoginParams.setIMEINumber(ClsGlobal.getIMEIno(LoginActivityDemo.this));
        objClsLoginParams.setDeviceInfo(ClsGlobal.getDeviceInfo(LoginActivityDemo.this));

        if (input_email.getTag().toString() == "1") {
            objClsLoginParams.setEmail(edt_email.getText().toString());
        } else {
            objClsLoginParams.setEmail("");
        }

        Gson gson = new Gson();
        String jsonInString = gson.toJson(objClsLoginParams);
        Log.d("Result", "LoginAPI- " + jsonInString);

        InterfaceLogin interfaceLogin = ApiClient.getRetrofitInstance().create(InterfaceLogin.class);
        Log.e("--URL--", "interfaceLogin: " + interfaceLogin.toString());
        Call<ClsLoginParams> call = interfaceLogin.postLogin(objClsLoginParams);
        Log.e("--URL--", "************************  before call : " + call.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(LoginActivityDemo.this, "Loading...", true);
        pd.show();

        try {
            call.enqueue(new Callback<ClsLoginParams>() {
                @Override
                public void onResponse(Call<ClsLoginParams> call, Response<ClsLoginParams> response) {
                    Log.e("--URL--", "response: " + response);
                    pd.dismiss();
                    if (response.body() != null) {
                        String _response = response.body().getSuccess();
                        Log.e("--response--", "response: " + _response);

                        ClsUserInfo objClsUserInfo = new ClsUserInfo();
                        List<ClsLoginResponseList> lstClsLoginResponseLists = response.body().getData();
                        if (_response.equals("1")) {

                            objUser = lstClsLoginResponseLists.get(0);
                            objClsUserInfo.setUserId(objUser.getId());
                            Log.e("--response--", "response: " + objUser.getId());

                            objClsUserInfo.setRemainDays(String.valueOf(objUser.getRemaindays()));
                            objClsUserInfo.setExpiredDays(objUser.getExpiredate());
                            objClsUserInfo.setContactPersonName(objUser.getContactpersonname());
                            objClsUserInfo.setLoginStatus("ACTIVE");
                            objClsUserInfo.setMobileNo(edt_mobile.getText().toString().trim());
                            objClsUserInfo.setBusinessname(objUser.getBusinessname());
                            objClsUserInfo.setBusinessaddress(objUser.getBusinessaddress());
                            objClsUserInfo.setRegisteredmobilenumber(objUser.getRegisteredmobilenumber());
                            objClsUserInfo.setEmailaddress(objUser.getEmailaddress());
                            objClsUserInfo.setState(objUser.getState());
                            objClsUserInfo.setCity(objUser.getCity());
                            objClsUserInfo.setPincode(objUser.getPincode());
                            objClsUserInfo.setCinnumber(objUser.getCinnumber());
                            objClsUserInfo.setGstnumber(objUser.getGstnumber());


                            ClsGlobal.setUserInfo(objClsUserInfo, LoginActivityDemo.this);
                            Toast.makeText(LoginActivityDemo.this, "Welcome ".concat(objClsUserInfo.getContactPersonName()), Toast.LENGTH_SHORT).show();
                            SaveData("Frist");

                            periodicWorkRequest = new PeriodicWorkRequest.Builder(DailyLogoutTask.class,
                                    1, TimeUnit.DAYS)
                                    .build();

                            // For Setting up Unique PeriodicWork. So there is one PeriodicWork active at a time.
                            // Remember there is Only one PeriodicWork at a time.
                            WorkManager.getInstance().enqueueUniquePeriodicWork(ClsGlobal.AppPackageName.concat("DailyTaskLogoutRetail")
                                    , ExistingPeriodicWorkPolicy.KEEP
                                    , periodicWorkRequest);

                            Intent intent = new Intent(getApplicationContext(), SHAP_Lite_Activity.class);
                            intent.putExtra("_renewAlert", "Yes");
                            intent.putExtra("_customerId", objUser.getId());
                            startActivity(intent);

                        } else if (_response.equals("7")) {
                            Toast.makeText(LoginActivityDemo.this, lstClsLoginResponseLists.get(0).getMessageSales(), Toast.LENGTH_SHORT).show();
                        } else if (_response.equals("8")) {
                            Toast.makeText(LoginActivityDemo.this, lstClsLoginResponseLists.get(0).getMessageSales(), Toast.LENGTH_SHORT).show();
                        } else if (_response.equals("9")) {
                            Toast.makeText(LoginActivityDemo.this, lstClsLoginResponseLists.get(0).getMessageSales(), Toast.LENGTH_SHORT).show();
                        } else if (_response.equals("10")) {
                            Toast.makeText(LoginActivityDemo.this, lstClsLoginResponseLists.get(0).getMessageSales(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Server error...!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ClsLoginParams> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Something went wrong...Internet issue!", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Log.d("Exception", "LoginAPI: " + e.getMessageSales());
        }
    }


//    private void alertbox() {
//        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(LoginActivityDemo.this);
//        alertDialogBuilder.setTitle("Permission");
////        alertDialogBuilder.setView(dialog);
//        alertDialogBuilder.setMessage("Please allow all permissions for access SHAPLite.");
//        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                arg0.dismiss();
//                ClsPermission.checkpermission(LoginActivityDemo.this);
//            }
//        });
//        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//    }


    private void SaveData(String str) {
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString("Status1", str);
        preferencesEditor.apply();

    }

    @SuppressLint("WrongConstant")
    private void DropAllTables() {

        db = openOrCreateDatabase(ClsGlobal.Database_Name, MODE_APPEND, null);

        db.execSQL("DROP TABLE IF EXISTS [tbl_Terms];");
        db.execSQL("DROP TABLE IF EXISTS [OrderDetail_master];");
        db.execSQL("DROP TABLE IF EXISTS [tbl_EmailLogs];");
        db.execSQL("DROP TABLE IF EXISTS [CustomerMaster];");
        db.execSQL("DROP TABLE IF EXISTS [Ordermaster];");
        db.execSQL("DROP TABLE IF EXISTS [Table_master];");
        db.execSQL("DROP TABLE IF EXISTS [Taxes];");
        db.execSQL("DROP TABLE IF EXISTS [EmployeeDocument];");
        db.execSQL("DROP TABLE IF EXISTS [ExpenseMaster];");
        db.execSQL("DROP TABLE IF EXISTS [ExpenseMasterNew];");
        db.execSQL("DROP TABLE IF EXISTS [EXPENSE_TYPE_MASTER];");
        db.execSQL("DROP TABLE IF EXISTS [INVENTORY_ITEM_MASTER];");
        db.execSQL("DROP TABLE IF EXISTS [Inventory_stock_master];");
        db.execSQL("DROP TABLE IF EXISTS [EMPLOYEE_MASTER];");
        db.execSQL("DROP TABLE IF EXISTS [ITEM_MASTER];");
        db.execSQL("DROP TABLE IF EXISTS [VENDOR_MASTER];");
        db.execSQL("DROP TABLE IF EXISTS [UNIT_MASTER];");
        db.execSQL("DROP TABLE IF EXISTS [SIZE_MASTER];");
        db.execSQL("DROP TABLE IF EXISTS [CATEGORY_MASTER];");

        db.execSQL("DROP TABLE IF EXISTS [tbl_InventoryLayer];");
        db.execSQL("DROP TABLE IF EXISTS [tbl_LayerItem_Master];");
        db.execSQL("DROP TABLE IF EXISTS [tbl_ItemTag];");
        db.execSQL("DROP TABLE IF EXISTS [tbl_ItemLayer];");
        db.execSQL("DROP TABLE IF EXISTS [tbl_Tax_Slab];");
        db.execSQL("DROP TABLE IF EXISTS [PaymentMaster];");
        db.execSQL("DROP TABLE IF EXISTS [PurchaseMaster];");
        db.execSQL("DROP TABLE IF EXISTS [PurchaseDetail];");
        db.execSQL("DROP TABLE IF EXISTS [InventoryOrderDetail];");
        db.execSQL("DROP TABLE IF EXISTS [PurchaseMaster];");


        db.close();
    }*/
}
