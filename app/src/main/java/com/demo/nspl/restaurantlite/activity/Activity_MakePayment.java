package com.demo.nspl.restaurantlite.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ApiClient;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsPackagePaymentParams;
import com.demo.nspl.restaurantlite.RetrofitApi.Interface.InterfacePackagePayment;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_MakePayment extends AppCompatActivity implements PaymentResultListener {

    private static final String TAG = Activity_MakePayment.class.getSimpleName();

    String title = "";
    String PaymentGatwayOrderID = "";
    int valid_days, packageID = 0;
    Double price;
    private String get_Mobile_No, _customerId, TransactionRefNumber;
    TextView txt_amount, txt_title, txt_transactionId;
    String _mobileNo = "", _emailAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);

        Checkout.preload(getApplicationContext());

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "Activity_MakePayment"));
        }

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        _customerId = intent.getStringExtra("_customerId");
        _mobileNo = intent.getStringExtra("_mobileNo");
        _emailAddress = intent.getStringExtra("_emailAddress");
        TransactionRefNumber = intent.getStringExtra("TransactionRefNumber");
        valid_days = intent.getIntExtra("valid_days", 0);
        packageID = intent.getIntExtra("packageID", 0);
        price = intent.getDoubleExtra("price", 0.0);
        PaymentGatwayOrderID = intent.getStringExtra("PaymentGatwayOrderID");

        Button button = findViewById(R.id.btn_pay);

        txt_title = findViewById(R.id.txt_title);
        txt_title.setText(title);

        txt_amount = findViewById(R.id.txt_amount);
        txt_transactionId = findViewById(R.id.txt_transactionId);
        txt_transactionId.setText("Order Id# " + TransactionRefNumber);
        txt_amount.setText("INR: " + String.valueOf(price));
        txt_amount.setTag(String.valueOf(price));
        Toast.makeText(this, "INR: " + txt_amount.getTag(), Toast.LENGTH_SHORT).show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClsGlobal.CheckInternetConnection(Activity_MakePayment.this))
                    startPayment();
            }
        });
    }


    String paymentDesc = "RazorPayOrderID: ".concat(PaymentGatwayOrderID)
            .concat("CustomerCode: ").concat(_customerId)
            .concat("Transaction Reference No. ").concat(TransactionRefNumber)
            .concat("Email: ").concat(_emailAddress)
            .concat("Mobile: ").concat(_mobileNo)
            .concat("PackageID: ").concat(String.valueOf(packageID));


    public void startPayment() {

        final AppCompatActivity activity = this;
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", paymentDesc);
//            options.put("image", "https://cdn.razorpay.com/logos/B5S0yCDDpK9MwB_medium.png");
            options.put("currency", "INR");
            options.put("order_id", PaymentGatwayOrderID);
            
// Amount Converted in Paisa.....
            int _packageAmount = (int) (price * 100);
            options.put("amount", _packageAmount);


            JSONObject preFill = new JSONObject();
            preFill.put("email", _emailAddress);
            preFill.put("contact", _mobileNo);
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();

            ClsPackagePaymentParams objClsPackagePaymentParams = new ClsPackagePaymentParams();
            objClsPackagePaymentParams.setCustomerCode(_customerId);
            objClsPackagePaymentParams.setTransactionReferenceNumber(TransactionRefNumber);
            objClsPackagePaymentParams.setPaymentStatus("success");
            objClsPackagePaymentParams.setPaymentMode("Online");
            objClsPackagePaymentParams.setPaymentGateway("razorpay");
            objClsPackagePaymentParams.setPaymentReferenceNumber(razorpayPaymentID);
            objClsPackagePaymentParams.setSatusCode(0);
            objClsPackagePaymentParams.setTransactionMessage("Payment Successful");

            packagePaymentAPI(objClsPackagePaymentParams);

        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
            ClsPackagePaymentParams objClsPackagePaymentParams = new ClsPackagePaymentParams();
            objClsPackagePaymentParams.setCustomerCode(_customerId);
            objClsPackagePaymentParams.setTransactionReferenceNumber(TransactionRefNumber);
            objClsPackagePaymentParams.setPaymentStatus("failed");
            objClsPackagePaymentParams.setPaymentMode("Online");
            objClsPackagePaymentParams.setPaymentGateway("razorpay");
            objClsPackagePaymentParams.setPaymentReferenceNumber("");
            objClsPackagePaymentParams.setSatusCode(code);
            objClsPackagePaymentParams.setTransactionMessage(response);
            packagePaymentAPI(objClsPackagePaymentParams);
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }


    void packagePaymentAPI(ClsPackagePaymentParams objClsReferralApplyParams) {

        InterfacePackagePayment interfacePackagePayment = ApiClient.getRetrofitInstance().create(InterfacePackagePayment.class);
        Log.e("--URL--", "interfaceDesignation: " + interfacePackagePayment.toString());
        Call<ClsPackagePaymentParams> call = interfacePackagePayment.postPackagePayment(objClsReferralApplyParams);
        Log.e("--URL--", "************************  before call : " + call.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(Activity_MakePayment.this, "Working...", true);
        pd.show();

        call.enqueue(new Callback<ClsPackagePaymentParams>() {
            @Override
            public void onResponse(Call<ClsPackagePaymentParams> call, Response<ClsPackagePaymentParams> response) {
                pd.dismiss();
                if (response.body() != null) {
                    String _response = response.body().getSuccess();
                    Log.d("_response", "onResponse: " + _response);
                    switch (_response) {
                        case "1":
                            Toast.makeText(Activity_MakePayment.this, "Registration is successful", Toast.LENGTH_SHORT).show();
                            //After diwali check it....
                            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                            startActivity(intent);

                            finish();
                            break;
                        case "2":
                            Toast.makeText(Activity_MakePayment.this, "Mobile no is not verified", Toast.LENGTH_SHORT).show();
                            break;
                        case "3":
                            Toast.makeText(Activity_MakePayment.this, "Invalid referral code", Toast.LENGTH_SHORT).show();
                            break;
                        case "4":
                            Toast.makeText(Activity_MakePayment.this, "Referral code not found", Toast.LENGTH_SHORT).show();
                            break;
                        case "0":
                            Toast.makeText(Activity_MakePayment.this, "fail", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(Activity_MakePayment.this, "Error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server error...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsPackagePaymentParams> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
