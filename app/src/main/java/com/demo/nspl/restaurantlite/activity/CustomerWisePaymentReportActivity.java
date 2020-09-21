package com.demo.nspl.restaurantlite.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.AsyncTaskReport.CustomerWisePaymentAsyncTask;
import com.demo.nspl.restaurantlite.Customer.ClsCustomerWisePayment;
import com.demo.nspl.restaurantlite.Customer.CustomerWisePaymentAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.MultipleImage.DisplayImageNewActivity;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.VendorLedger.ClsVendorLedger;
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;

import java.util.Objects;


public class CustomerWisePaymentReportActivity extends AppCompatActivity {

    ClsVendorLedger clsVendorLedger;
    RecyclerView rv;
    ProgressBar progress_bar;
    TextView txt_nodata;
    TextView txt_back_amount;
    TextView txt_type_payment;
    TextView txt_paid_amt,txt_received_amt;
    TextView txt_charcter, txt_customer_name, txt_mobile;
    Toolbar toolbar;

    Button btn_pay, btn_receive;
    String type = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_wise_payment_list);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "CustomerWisePaymentReportActivity"));
        }
        main();
    }

    private void main() {
        clsVendorLedger = (ClsVendorLedger) getIntent().getSerializableExtra("clsVendorLedger");
        Log.d("--Image--", "OnCreate: " + clsVendorLedger.getCustomerMobileNo());
        type = getIntent().getStringExtra("type");
        Log.d("--Type--", "type: " + type);

        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(CustomerWisePaymentReportActivity.this));
        progress_bar = findViewById(R.id.progress_bar);
        txt_nodata = findViewById(R.id.txt_nodata);
        txt_back_amount = findViewById(R.id.txt_back_amount);
        txt_type_payment = findViewById(R.id.txt_type_payment);
        txt_customer_name = findViewById(R.id.txt_customer_name);
        txt_mobile = findViewById(R.id.txt_mobile);
        txt_charcter = findViewById(R.id.txt_charcter);
        txt_received_amt = findViewById(R.id.txt_received_amt);
        txt_paid_amt = findViewById(R.id.txt_paid_amt);

        btn_pay = findViewById(R.id.btn_pay);
        btn_receive = findViewById(R.id.btn_receive);

        String headerText = "";
        if (clsVendorLedger.getCustomerName() != null && !clsVendorLedger.getCustomerName().equalsIgnoreCase("")) {
            txt_customer_name.setText(clsVendorLedger.getCustomerName().toUpperCase());
            headerText = "" + clsVendorLedger.getCustomerName().charAt(0);
            txt_charcter.setText(headerText.toUpperCase());
        }

        if (clsVendorLedger.getCustomerMobileNo() != null) {
            txt_mobile.setText(clsVendorLedger.getCustomerMobileNo());
        }

        double balanceAmt = 0.0;
        balanceAmt = (clsVendorLedger.getTotalSaleAmount() - clsVendorLedger.getTotalPaymentAmount()/*5000*/); //* -1;//-400

        if (balanceAmt < 0) {
            txt_back_amount.setTextColor(Color.parseColor("#c40000"));
            txt_type_payment.setText("TOTAL BALANCE");
        } else {
            txt_type_payment.setText("TOTAL BALANCE ");
            txt_back_amount.setTextColor(Color.parseColor("#225A25"));
        }

        txt_back_amount.setText("\u20B9 " + ClsGlobal.round(balanceAmt, 2));
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentAddPaymentActivity("PAY");
            }
        });

        btn_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentAddPaymentActivity("RECEIVE");
            }
        });

        ViewData();
    }

    public static final int REQUEST_CODE = 1;

    void IntentAddPaymentActivity(String _pmtMode) {

//        int ReceiptNo = Integer.parseInt(clsPaymentMaster.getReceiptNo());

        Intent intent = new Intent(getApplicationContext(), DirectAddPaymentActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("ReceiptNo", 0);
        intent.putExtra("paymentID", 0);
        intent.putExtra("clsVendorLedger", clsVendorLedger);
        intent.putExtra("_pmtMode", _pmtMode);

//        startActivity(intent);

        startActivityForResult(intent, REQUEST_CODE);
    }

    CustomerWisePaymentAdapter customerWisePaymentAdapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

                String requiredValue = data.getStringExtra("key");

                Log.d("--Image--", "requiredValue: " + requiredValue);

                ViewData();
            }


        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void ViewData() {

        String where = "".concat(" AND [MobileNo] = ").concat("'")
                .concat(clsVendorLedger.getCustomerMobileNo()).concat("'");
//                .concat(" AND IFNULL([Type] ,'Customer') = 'Customer' ");

        customerWisePaymentAdapter = new CustomerWisePaymentAdapter(CustomerWisePaymentReportActivity.this);


        customerWisePaymentAdapter.SetOnClickListener(new CustomerWisePaymentAdapter.OnClickCustPayment() {
            @Override
            public void OnClick(ClsCustomerWisePayment clsPaymentMaster, int position) {

                updateDeleteDialog(clsPaymentMaster, position);

            }
        });


        customerWisePaymentAdapter.SetOnImageClickListener((clsPaymentMaster, position) -> {

            int paymentID = clsPaymentMaster.getPaymentID();
            String customerName = clsPaymentMaster.getCustomerName();


            Log.d("--Image--", "paymentID: " + paymentID);


            Intent intent = new Intent(getApplicationContext(), DisplayImageNewActivity.class);
            intent.putExtra("_type", "Customer Payment");
            intent.putExtra("_id", paymentID);
            intent.putExtra("_customerName", customerName);
            intent.putExtra("imgSave", "Preview");
            startActivity(intent);

        });

        rv.setAdapter(customerWisePaymentAdapter);

        new CustomerWisePaymentAsyncTask(where,
                CustomerWisePaymentReportActivity.this,
                customerWisePaymentAdapter, progress_bar, rv).execute();

    }

    @Override
    protected void onResume() {
        super.onResume();

        ViewData();
    }

    void updateDeleteDialog(ClsCustomerWisePayment clsPaymentMaster, int position) {
        final Dialog dialog = new Dialog(CustomerWisePaymentReportActivity.this);

        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delete_edit);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        LinearLayout ll_update = dialog.findViewById(R.id.ll_update);
        LinearLayout ll_delete = dialog.findViewById(R.id.ll_delete);

        ll_delete.setOnClickListener(v -> {

            dialog.dismiss();
            dialog.cancel();

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CustomerWisePaymentReportActivity.this);
            alertDialog.setTitle("Confirm Delete...");
            alertDialog.setMessage("Are you sure you want delete?");
            alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog12, int which) {

                    ClsPaymentMaster obj = new ClsPaymentMaster();
                    obj.setReceiptNo(obj.getReceiptNo());
                    obj.setType(obj.getType());

                    int getResult = ClsPaymentMaster.deletePaymentReportData
                            (Integer.parseInt(clsPaymentMaster.getReceiptNo()),
                                    "Customer",
                                    CustomerWisePaymentReportActivity.this);

                    if (getResult == 1) {
                        Toast.makeText(CustomerWisePaymentReportActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        customerWisePaymentAdapter.RemoveItem(position);
                    } else {
                        Toast.makeText(CustomerWisePaymentReportActivity.this, "Error while Deleting", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            alertDialog.setNegativeButton("NO", (dialog1, which) -> {
                dialog1.dismiss();
                dialog1.cancel();
            });
            // Showing Alert Message
            alertDialog.show();
        });

        ll_update.setOnClickListener(v -> {
            dialog.dismiss();
            dialog.cancel();

            int ReceiptNo = Integer.parseInt(clsPaymentMaster.getReceiptNo());

            Log.d("--No--", "ReceiptNo: " + ReceiptNo);


//            Intent intent = new Intent(CustomerWisePaymentReportActivity.this, AddPaymentActivity.class);
//            intent.putExtra("type", "Customer");
//            intent.putExtra("ReceiptNo", ReceiptNo);
//            startActivity(intent);

        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
