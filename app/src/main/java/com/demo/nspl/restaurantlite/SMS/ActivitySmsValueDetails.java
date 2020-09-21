package com.demo.nspl.restaurantlite.SMS;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ApiClient;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.activity.Activity_wts_new;
import com.demo.nspl.restaurantlite.classes.ClsUserInfo;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demo.nspl.restaurantlite.SMS.GetSmsIdPackagesActivity.finishFlag;

public class ActivitySmsValueDetails extends AppCompatActivity
        implements PaymentResultListener {

    private TextView txt_refer, txt_get_offer_name;
    private TextView txt_package, txt_valid_days, txt_price;
    private Button btn_make_payment;


    public int counter = 30;
    Dialog dialog_reference;
    int valid_days = 0, packageId = 0;
    String title = "", reg_mode = "", _customerId = "", offerId = "",
            _emailAddress = "", _mobileNo = "";
    double price = 0.0;
    TextView txt_applyTax, txt_tax1, txt_tax2, txt_tax3,
            txt_tax_value1, txt_tax_value2, txt_tax_value3,
            txt_totaltaxamount, txt_totalpackageamount;
    LinearLayout ll_no_offer, ll_view_offers, ll_offer_name, ll_remove;

    List<ClsViewSmsOffersList> lstClsViewSmsOffersLists;

    TextView txt_transaction_sms_pkgdetl, txt_promotional_sms_pkgdetl, txt_total_sms_pkgdetl;
    TextView txt_transaction_sms_offer, txt_promotional_sms_offer, txt_total_sms_offer;
    TextView txt_transaction_sms_summary, txt_promotional_sms_summary, txt_total_sms_summary;
    TextView txt_discount_per, txt_discount_value;

    String IsTaxesApplicable = "";
    double IGSTValue = 0.0, CGSTValue = 0.0, SGSTValue = 0.0;
    double IGSTTaxAmount = 0.0, CGSTTaxAmount = 0.0, SGSTTaxAmount = 0.0, TotalTaxAmount = 0.0, TotalPackageAmount = 0.0;

    ClsSmsSummaryValue objClsSmsSummaryValue = new ClsSmsSummaryValue();
    TextView txt_set_msg;
    TextView txt_error_msg;
    TextView txt_taxable_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_value_details);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    this, "ActivitySmsValueDetails"));
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        main();
    }


    @SuppressLint("SetTextI18n")
    private void main() {

        ClsUserInfo userInfo = ClsGlobal.getUserInfo(ActivitySmsValueDetails.this);

        txt_error_msg = findViewById(R.id.txt_error_msg);
        txt_set_msg = findViewById(R.id.txt_set_msg);
        ll_remove = findViewById(R.id.ll_remove);
        ll_no_offer = findViewById(R.id.ll_no_offer);
        ll_offer_name = findViewById(R.id.ll_offer_name);
        txt_taxable_amount = findViewById(R.id.txt_taxable_amount);

        txt_refer = findViewById(R.id.txt_refer);
        txt_get_offer_name = findViewById(R.id.txt_get_offer_name);

        ll_view_offers = findViewById(R.id.ll_view_offers);
        txt_totaltaxamount = findViewById(R.id.txt_totaltaxamount);
        txt_totalpackageamount = findViewById(R.id.txt_totalpackageamount);

        txt_applyTax = findViewById(R.id.txt_applyTax);
        txt_tax1 = findViewById(R.id.txt_tax1);
        txt_tax2 = findViewById(R.id.txt_tax2);
        txt_tax3 = findViewById(R.id.txt_tax3);

        txt_tax_value1 = findViewById(R.id.txt_tax_value1);
        txt_tax_value2 = findViewById(R.id.txt_tax_value2);
        txt_tax_value3 = findViewById(R.id.txt_tax_value3);

        txt_package = findViewById(R.id.txt_package);
        txt_valid_days = findViewById(R.id.txt_valid_days);
        txt_price = findViewById(R.id.txt_price);
        btn_make_payment = findViewById(R.id.btn_make_payment);

        txt_transaction_sms_pkgdetl = findViewById(R.id.txt_transaction_sms_pkgdetl);
        txt_promotional_sms_pkgdetl = findViewById(R.id.txt_promotional_sms_pkgdetl);
        txt_total_sms_pkgdetl = findViewById(R.id.txt_total_sms_pkgdetl);

        txt_transaction_sms_offer = findViewById(R.id.txt_transaction_sms_offer);
        txt_promotional_sms_offer = findViewById(R.id.txt_promotional_sms_offer);
        txt_total_sms_offer = findViewById(R.id.txt_total_sms_offer);

        txt_transaction_sms_summary = findViewById(R.id.txt_transaction_sms_summary);
        txt_promotional_sms_summary = findViewById(R.id.txt_promotional_sms_summary);
        txt_total_sms_summary = findViewById(R.id.txt_total_sms_summary);

        txt_discount_per = findViewById(R.id.txt_discount_per);
        txt_discount_value = findViewById(R.id.txt_discount_value);

        txt_totalpackageamount.setTag(0);

        Intent intent = getIntent();
        reg_mode = intent.getStringExtra("reg_mode");

        objClsSmsSummaryValue = (ClsSmsSummaryValue) intent.getSerializableExtra("objValue");

        txt_transaction_sms_pkgdetl.setText("TRANSACTION SMS: " + ClsGlobal.round(objClsSmsSummaryValue.getTransactionSMSTotalCredit(), 2));
        txt_promotional_sms_pkgdetl.setText("PROMOTIONAL SMS: " + ClsGlobal.round(objClsSmsSummaryValue.getPromotionalSMSTotalCredit(), 2));
        txt_total_sms_pkgdetl.setText("TOTAL SMS: " + ClsGlobal.round(objClsSmsSummaryValue.getTotalSMSCredit(), 2));

        title = intent.getStringExtra("title");
        _customerId = userInfo.getUserId();
//        _customerId = "CTA001";
        _emailAddress = userInfo.getEmailaddress();
        _mobileNo = userInfo.getMobileNo();

        valid_days = intent.getIntExtra("valid_days", 0);
        packageId = intent.getIntExtra("packageId", 0);
        price = intent.getDoubleExtra("price", 0.0);

        IGSTValue = intent.getDoubleExtra("IGSTValue", 0.0);
        SGSTValue = intent.getDoubleExtra("SGSTValue", 0.0);
        CGSTValue = intent.getDoubleExtra("CGSTValue", 0.0);
        IGSTTaxAmount = intent.getDoubleExtra("IGSTTaxAmount", 0.0);
        CGSTTaxAmount = intent.getDoubleExtra("CGSTTaxAmount", 0.0);
        SGSTTaxAmount = intent.getDoubleExtra("SGSTTaxAmount", 0.0);
        TotalTaxAmount = intent.getDoubleExtra("TotalTaxAmount", 0.0);

        Log.d("--value--", "TotalTaxAmount: " + TotalTaxAmount);

        TotalPackageAmount = intent.getDoubleExtra("TotalPackageAmount", 0.0);

        txt_package.setText("PACKAGE: " + title.toUpperCase());
        txt_valid_days.setText("VALID DAYS: " + valid_days);

        objClsSmsSummaryValue.setSGSTValue(SGSTValue);
        objClsSmsSummaryValue.setSGSTTaxAmount(SGSTTaxAmount);

        objClsSmsSummaryValue.setIGSTValue(IGSTValue);
        objClsSmsSummaryValue.setIGSTTaxAmount(IGSTTaxAmount);

        objClsSmsSummaryValue.setCGSTValue(CGSTValue);
        objClsSmsSummaryValue.setCGSTTaxAmount(CGSTTaxAmount);

        objClsSmsSummaryValue.setTotalTaxAmount(TotalTaxAmount);
//        objClsSmsSummaryValue.setTaxableAmount(TotalTaxAmount);
        objClsSmsSummaryValue.setTotalPackageAmount(TotalPackageAmount);

        fillPackageData(objClsSmsSummaryValue, false);

        ClsViewSmsOffersParams obj = new ClsViewSmsOffersParams();
        obj.setProductName(ClsGlobal.AppName);
//        getViewSmsOffersAPI(obj);
//        bottomDialogViewSmsOffersListAPI(obj);

        ll_view_offers.setOnClickListener(v -> {
            bottomDialogViewSmsOffersListAPI(obj);

//            UpdateSMSServicePackagePaymentAPI();
        });


        ll_remove.setOnClickListener(v -> {

//            ll_no_offer.setVisibility(View.VISIBLE);
//            ll_offer_name.setVisibility(View.GONE);

            confirmDialog();

        });

        btn_make_payment.setOnClickListener(v -> {
            if (ClsGlobal.CheckInternetConnection(ActivitySmsValueDetails.this)) {
                //CashCollectionVerificationAPI();


                if (Double.valueOf(txt_totalpackageamount.getTag().toString()) > 0) {

                    SaveSMSServiceCustomerPackageAPI(false);

                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(ActivitySmsValueDetails.this,
                            R.style.AppCompatAlertDialogStyle).create(); //Read Update.
                    alertDialog.setContentView(R.layout.activity_dialog);
                    alertDialog.setTitle("Confirmation !");
                    alertDialog.setMessage("You have selected free SMS package, sure to continue?");

                    alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            _offerAppliedTest = "NO";
                            SaveSMSServiceCustomerPackageAPI(true);
                        }
                    });
                    alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "No",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.cancel();
                                }
                            });
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
            }
        });
    }

    ClsGetValidateSMSOfferList obj = new ClsGetValidateSMSOfferList();

    void ValidateSMSOfferAPI() {
        InterfaceValidateSMSOffer interfaceValidateSMSOffer =
                ApiClient.getDemoInstance().create(InterfaceValidateSMSOffer.class);

        Call<ClsGetValidateSMSOfferParams> objCall =
                interfaceValidateSMSOffer.value(ClsGlobal.AppName,
                        _customerId,
                        String.valueOf(packageId),
                        offerId,
                        txt_get_offer_name.getText().toString());

        Log.e("--URL--", "objCall: " + objCall.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(ActivitySmsValueDetails.this,
                        "Loading...", true);
        pd.show();

        objCall.enqueue(new Callback<ClsGetValidateSMSOfferParams>() {
            @Override
            public void onResponse(Call<ClsGetValidateSMSOfferParams> call, Response<ClsGetValidateSMSOfferParams> response) {

                pd.dismiss();

                if (response.body() != null) {
                    String _response = response.body().getSuccess();
                    String _message = response.body().getMessage();

                    Log.e("--URL--", "_message: " + _message);

                    List<ClsGetValidateSMSOfferList> lst = response.body().getData();
                    ClsSmsSummaryValue objClsSmsSummaryValue = new ClsSmsSummaryValue();

                    if (_response != null
                            && !_response.equalsIgnoreCase("")
                            && _response.equals("1")) {

                        objClsSmsSummaryValue.setDiscountPer(lst.get(0).getDiscountInPercentage());
                        objClsSmsSummaryValue.setDiscountAmount(lst.get(0).getDiscountAmount());

                        objClsSmsSummaryValue.setTransactionSMSTotalCreditASPerOffer(lst.get(0).getTransactionSMSTotalCreditASPerOffer());
                        objClsSmsSummaryValue.setPromotionalSMSTotalCreditPerOffer(lst.get(0).getPromotionalSMSTotalCreditPerOffer());
                        objClsSmsSummaryValue.setTotalSMSCreditPerOffer(lst.get(0).getTotalSMSCreditPerOffer());

                        objClsSmsSummaryValue.setTransactionSMSTotalCredit(lst.get(0).getTransactionSMSTotalCredit());
                        objClsSmsSummaryValue.setPromotionalSMSTotalCredit(lst.get(0).getPromotionalSMSTotalCredit());
                        objClsSmsSummaryValue.setTotalSMSCredit(lst.get(0).getTotalSMSCredit());

                        objClsSmsSummaryValue.setIGSTValue(lst.get(0).getIGSTRate());
                        objClsSmsSummaryValue.setIGSTTaxAmount(lst.get(0).getIGSTAmount());

                        objClsSmsSummaryValue.setCGSTValue(lst.get(0).getCGSTRate());
                        objClsSmsSummaryValue.setCGSTTaxAmount(lst.get(0).getCGSTAmount());

                        objClsSmsSummaryValue.setSGSTValue(lst.get(0).getSGSTRate());
                        objClsSmsSummaryValue.setSGSTTaxAmount(lst.get(0).getSGSTAmount());

                        objClsSmsSummaryValue.setTotalTaxAmount(lst.get(0).getTotalTax());
                        objClsSmsSummaryValue.setTaxableAmount(lst.get(0).getTaxableAmount());

                        objClsSmsSummaryValue.setTotalPackageAmount(lst.get(0).getTotalAmount());
                        objClsSmsSummaryValue.setTaxableAmount(lst.get(0).getTaxableAmount());


                        Gson gson = new Gson();
                        String jsonInString = gson.toJson(objClsSmsSummaryValue);
                        Log.e("--Main--", "item: " + jsonInString);


                        fillPackageData(objClsSmsSummaryValue, true);

/*                      objValue.setTransactionSMSTotalCreditASPerOffer(obj.getTransactionSMSTotalCreditASPerOffer());
                        objValue.setPromotionalSMSTotalCreditPerOffer(obj.getPromotionalSMSTotalCreditPerOffer());
                        objValue.setTotalSMSCreditPerOffer(obj.getTotalSMSCreditPerOffer());
*/
                        _offerAppliedTest = "YES";
                        txt_error_msg.setVisibility(View.GONE);

                    } else if (_response != null
                            && !_response.equalsIgnoreCase("")
                            && Integer.valueOf(_response) >= 2) {

                        _offerAppliedTest = "NO";
                        txt_error_msg.setText(_message.toUpperCase());
                        txt_error_msg.setVisibility(View.VISIBLE);

                    }
                }
            }

            @Override
            public void onFailure(Call<ClsGetValidateSMSOfferParams> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong,please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @SuppressLint("SetTextI18n")
    void fillPackageData(ClsSmsSummaryValue objValue, boolean _offerApplied) {

        txt_price.setText("PRICE: \u20B9 " + price);

        if (_offerApplied) {

            ll_no_offer.setVisibility(View.GONE);
            ll_offer_name.setVisibility(View.VISIBLE);

            txt_transaction_sms_offer.setVisibility(View.VISIBLE);
            txt_promotional_sms_offer.setVisibility(View.VISIBLE);
            txt_total_sms_offer.setVisibility(View.VISIBLE);

            objClsSmsSummaryValue.setTransactionSMSTotalCreditASPerOffer(objValue.getTransactionSMSTotalCreditASPerOffer());
            objClsSmsSummaryValue.setPromotionalSMSTotalCreditPerOffer(objValue.getPromotionalSMSTotalCreditPerOffer());
            objClsSmsSummaryValue.setTotalSMSCreditPerOffer(objValue.getTotalSMSCreditPerOffer());

            objClsSmsSummaryValue.setTransactionSMSTotalCredit(objValue.getTransactionSMSTotalCredit());
            objClsSmsSummaryValue.setPromotionalSMSTotalCredit(objValue.getPromotionalSMSTotalCredit());
            objClsSmsSummaryValue.setTotalSMSCredit(objValue.getTotalSMSCredit());

            objClsSmsSummaryValue.setDiscountPer(objValue.getDiscountPer());
            objClsSmsSummaryValue.setDiscountAmount(objValue.getDiscountAmount());

            objClsSmsSummaryValue.setIGSTValue(objValue.getIGSTValue());
            objClsSmsSummaryValue.setIGSTTaxAmount(objValue.getIGSTTaxAmount());

            objClsSmsSummaryValue.setCGSTValue(objValue.getCGSTValue());
            objClsSmsSummaryValue.setCGSTTaxAmount(objValue.getCGSTTaxAmount());

            objClsSmsSummaryValue.setSGSTValue(objValue.getSGSTValue());
            objClsSmsSummaryValue.setSGSTTaxAmount(objValue.getSGSTTaxAmount());

            objClsSmsSummaryValue.setTotalTaxAmount(objValue.getTotalTaxAmount());
            objClsSmsSummaryValue.setTotalPackageAmount(objValue.getTotalPackageAmount());

            objClsSmsSummaryValue.setTaxableAmount(objValue.getTaxableAmount());

            txt_transaction_sms_offer.setText("TRANSACTION SMS: " + ClsGlobal.round(objValue.getTransactionSMSTotalCreditASPerOffer(), 2));
            txt_promotional_sms_offer.setText("PROMOTIONAL SMS: " + ClsGlobal.round(objValue.getPromotionalSMSTotalCreditPerOffer(), 2));
            txt_total_sms_offer.setText("TOTAL SMS: " + ClsGlobal.round(objValue.getTotalSMSCreditPerOffer(), 2));

            txt_transaction_sms_summary.setText("TRANSACTION SMS: " + ClsGlobal.round(objValue.getTransactionSMSTotalCredit(), 2));
            txt_promotional_sms_summary.setText("PROMOTIONAL SMS: " + ClsGlobal.round(objValue.getPromotionalSMSTotalCredit(), 2));
            txt_total_sms_summary.setText("TOTAL SMS: " + ClsGlobal.round(objValue.getTotalSMSCredit(), 2));
            txt_taxable_amount.setText("TAXABLE AMOUNT: \u20B9 " + ClsGlobal.round(objValue.getTaxableAmount(), 2));

            txt_discount_per.setText("DISCOUNT (" + ClsGlobal.round(objValue.getDiscountPer(), 2) + "%)");

            txt_discount_per.setTextColor(this.getResources().getColor(R.color.dark_green));

            txt_discount_value.setText("\u20B9 " + ClsGlobal.round(objValue.getDiscountAmount(), 2));

            txt_discount_value.setTextColor(this.getResources().getColor(R.color.dark_green));

            txt_applyTax.setText("TAXABLE AMOUNT: \u20B9 " + ClsGlobal.round(objValue.getTaxableAmount(), 2));
            txt_tax1.setText("SGST (" + ClsGlobal.round(objValue.getSGSTValue(), 2) + "%)");
            txt_tax_value1.setText("\u20B9 " + ClsGlobal.round(objValue.getSGSTTaxAmount(), 2));
            txt_tax2.setText("CGST (" + ClsGlobal.round(objValue.getCGSTValue(), 2) + "%)");
            txt_tax_value2.setText("\u20B9 " + ClsGlobal.round(objValue.getCGSTTaxAmount(), 2));
            txt_tax3.setText("IGST (" + ClsGlobal.round(objValue.getIGSTValue(), 2) + "%)");
            txt_tax_value3.setText("\u20B9 " + ClsGlobal.round(objValue.getIGSTTaxAmount(), 2));

            txt_totaltaxamount.setText("TOTAL TAX AMOUNT: \u20B9 " + ClsGlobal.round(objValue.getTotalTaxAmount(), 2));
            txt_totalpackageamount.setText("PACKAGE AMOUNT: \u20B9 " + ClsGlobal.round(objValue.getTotalPackageAmount(), 2));
            txt_totalpackageamount.setTag(objValue.getTotalPackageAmount());

        } else {
            //red color. no offer applied!

            ll_no_offer.setVisibility(View.VISIBLE);
            ll_offer_name.setVisibility(View.GONE);

            txt_transaction_sms_offer.setVisibility(View.GONE);
            txt_promotional_sms_offer.setVisibility(View.GONE);
            txt_total_sms_offer.setVisibility(View.GONE);

            txt_transaction_sms_offer.setText("TRANSACTION SMS: 0.00");
            txt_promotional_sms_offer.setText("PROMOTIONAL SMS: 0.00");
            txt_total_sms_offer.setText("TOTAL SMS PER OFFER: 0.00");

            txt_transaction_sms_summary.setText("TRANSACTION SMS: 0.00");
            txt_promotional_sms_summary.setText("PROMOTIONAL SMS: 0.00");
            txt_total_sms_summary.setText("TOTAL SMS: 0.00");

            txt_applyTax.setText("Tax Applicable: " + IsTaxesApplicable);
            txt_tax1.setText("SGST (" + ClsGlobal.round(SGSTValue, 2) + "%)");
//            txt_tax_value1.setText(String.valueOf(SGSTTaxAmount));

            txt_tax_value1.setText(ClsGlobal.round(SGSTTaxAmount, 2));

            txt_tax2.setText("CGST (" + ClsGlobal.round(CGSTValue, 2) + "%)");
//            txt_tax_value2.setText(String.valueOf(CGSTTaxAmount));
            txt_tax_value2.setText(ClsGlobal.round(CGSTTaxAmount, 2));
            txt_tax3.setText("IGST (" + ClsGlobal.round(IGSTValue, 2) + "%)");

//            txt_tax_value3.setText(String.valueOf(IGSTTaxAmount));
            txt_tax_value3.setText(ClsGlobal.round(IGSTTaxAmount, 2));

            txt_totaltaxamount.setText("TOTAL TAX AMOUNT: \u20B9 " + ClsGlobal.round(TotalTaxAmount, 2));
            txt_totalpackageamount.setText("PACKAGE AMOUNT: \u20B9 " + ClsGlobal.round(TotalPackageAmount, 2));
            txt_totalpackageamount.setTag(TotalPackageAmount);
            txt_applyTax.setText("TAXABLE AMOUNT: \u20B9 0.00");

            txt_discount_per.setText("DISCOUNT (0.00%)");
            txt_discount_value.setText("\u20B9 0.00");

            txt_discount_per.setTextColor(this.getResources().getColor(R.color.black));
            txt_discount_value.setTextColor(this.getResources().getColor(R.color.black));

        }

    }

    void filter(String text) {
        Log.e("--Text--", "step2");
        filterList = new ArrayList<>();
        if (text != null && text != "") {
            for (ClsViewSmsOffersList obj : lstClsViewSmsOffersLists) {
                Log.e("--Text--", "--getItemName--" + obj.getOfferCode());
                if (obj.getOfferCode().toLowerCase().contains(text.toLowerCase())
                        || (obj.getOfferTitle() != null && !obj.getOfferTitle().isEmpty()
                        && obj.getOfferTitle().toLowerCase().contains(text.toLowerCase()))) {

                    filterList.add(obj);
                }
            }

            //update recyclerview
            if (filterList.size() != 0) {
                adapter.updateList(filterList);
                txt_no_offers.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
            } else {
                txt_no_offers.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
            }

        } else {
            adapter.updateList(lstClsViewSmsOffersLists);
            txt_no_offers.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }
    }

    List<ClsViewSmsOffersList> filterList = new ArrayList();
    TextView txt_no_offers;
    RecyclerView rv;
    ViewSmsOffersAdapter adapter;

    void bottomDialogViewSmsOffersListAPI(ClsViewSmsOffersParams obj) {
        View view = getLayoutInflater().inflate(R.layout.view_sms_offers_list, null);
        BottomSheetDialog dialog = new BottomSheetDialog(ActivitySmsValueDetails.this);
        dialog.setContentView(view);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        rv = dialog.findViewById(R.id.rv);
        txt_no_offers = dialog.findViewById(R.id.txt_no_offers);
        EditText edit_search_main = dialog.findViewById(R.id.edit_search_main);
        ImageView img_clear = dialog.findViewById(R.id.img_clear);

        img_clear.setOnClickListener(v -> edit_search_main.setText(""));

        dialog.show();
        dialog.getWindow().setAttributes(lp);


        InterfaceViewSmsOffers interfaceViewSmsOffers =
                ApiClient.getDemoInstance().create(InterfaceViewSmsOffers.class);

        Call<ClsViewSmsOffersParams> objCall =
                interfaceViewSmsOffers.value(obj.getProductName());

        Log.e("--URL--", "objCall: " + objCall.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(ActivitySmsValueDetails.this,
                        "Loading...", true);
        pd.show();

        objCall.enqueue(new Callback<ClsViewSmsOffersParams>() {
            @Override
            public void onResponse(Call<ClsViewSmsOffersParams> call, Response<ClsViewSmsOffersParams> response) {
                pd.dismiss();

                if (response.body() != null) {
                    lstClsViewSmsOffersLists = new ArrayList<ClsViewSmsOffersList>();
                    lstClsViewSmsOffersLists = response.body().getData();
                    if (lstClsViewSmsOffersLists != null && lstClsViewSmsOffersLists.size() != 0) {
                        txt_no_offers.setVisibility(View.GONE);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivitySmsValueDetails.this);

                        adapter = new ViewSmsOffersAdapter(ActivitySmsValueDetails.this, lstClsViewSmsOffersLists);

                        edit_search_main.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {


                            }

                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                                String txtValue = "";
                                if (s != null && !s.toString().isEmpty() && s.toString() != "") {
                                    txtValue = s.toString();
                                }
                                Log.e("--Text--", "txtValue: " + txtValue);
                                filter(txtValue);
                            }
                        });

                        adapter.SetOnClickListener((clsViewSmsOffersList, position) -> {

                            txt_get_offer_name.setText(clsViewSmsOffersList.getOfferCode());
                            offerId = String.valueOf(clsViewSmsOffersList.getSMSServicesOfferID());

                            ValidateSMSOfferAPI();
                            dialog.dismiss();

                        });

                        adapter.SetOnTermsUrlClick((clsViewSmsOffersList, position) -> {
                            Intent intent = new Intent(getApplicationContext(), Activity_wts_new.class);
                            intent.putExtra("webViewMode", "Offer");
                            intent.putExtra("webViewLink", clsViewSmsOffersList.getTermsFileUrl());
                            startActivity(intent);
                        });

                        rv.setLayoutManager(layoutManager);
                        rv.setAdapter(adapter);

                    } else {
                        txt_no_offers.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server error...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsViewSmsOffersParams> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong,please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void confirmDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivitySmsValueDetails.this);
        alertDialog.setTitle("Confirm ?");
        alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
        alertDialog.setMessage("Sure to remove ?");

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                fillPackageData(objClsSmsSummaryValue, false);
            }
        });
        alertDialog.setNegativeButton("NO", (dialog, which) -> {
            dialog.dismiss();
            dialog.cancel();
        });
        // Showing Alert Message
        alertDialog.show();
    }


    String _transactionRefNum = "";
    String _offerAppliedTest = "NO";
    String PaymentGatwayOrderID = "";

    void SaveSMSServiceCustomerPackageAPI(boolean _freePkg) {

        ClsSaveSMSServiceCustomerPackageParams obj =
                new ClsSaveSMSServiceCustomerPackageParams();

        obj.setCustomerCode(_customerId);
        obj.setPackageID(String.valueOf(packageId));
        obj.setOfferApplied(_offerAppliedTest);
        obj.setOfferID(offerId);
        obj.setProductName(ClsGlobal.AppName);
        obj.setIMEINumber(ClsGlobal.getIMEIno(ActivitySmsValueDetails.this));


        InterfaceSaveSMSServiceCustomerPackage interfaceUpdatePackagePayment =
                ApiClient.getDemoInstance().create(InterfaceSaveSMSServiceCustomerPackage.class);

        Call<ClsSaveSMSServiceCustomerPackageParams> objCall =
                interfaceUpdatePackagePayment.postSaveSms(obj);

        Gson gson = new Gson();
        String jsonInString = gson.toJson(obj);
        Log.d("--Gson--", "Obj: " + jsonInString);

        Log.e("--Gson--", "URL: " + objCall.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(ActivitySmsValueDetails.this,
                        "Working...", true);
        pd.show();

        objCall.enqueue(new Callback<ClsSaveSMSServiceCustomerPackageParams>() {
            @Override
            public void onResponse(Call<ClsSaveSMSServiceCustomerPackageParams> call,
                                   Response<ClsSaveSMSServiceCustomerPackageParams> response) {
                pd.dismiss();

                if (response.body() != null) {

                    String _response = response.body().getSuccess();
                    String _message = response.body().getMessage();

                    List<ClsSaveSMSServiceCustomerPackageDataResponse> lst = response.body().getData();

                    if (_response.equals("1")) {
                        Toast.makeText(ActivitySmsValueDetails.this, _message, Toast.LENGTH_SHORT).show();

                        _transactionRefNum = lst.get(0).getTransactionRefNumber();
                        PaymentGatwayOrderID = lst.get(0).getPaymentGatwayOrderID();


                        paymentDesc = "RazorPayOrderID: ".concat(PaymentGatwayOrderID)
                                .concat(",CustomerCode: ").concat(_customerId)
                                .concat(",Transaction Reference No. ").concat(_transactionRefNum)
                                .concat(",Email: ").concat(_emailAddress)
                                .concat(",Mobile: ").concat(_mobileNo)
                                .concat(",PackageID: ").concat(String.valueOf(packageId));


                        if (_freePkg) {

                            ClsUpdateSMSServicePackagePayment obj = new ClsUpdateSMSServicePackagePayment();
                            obj.setCustomerCode(_customerId);
                            obj.setTransactionReferenceNumber(_transactionRefNum);
                            obj.setPaymentStatus("success");
                            obj.setPaymentMode("FREE");
                            obj.setPaymentGateway("");
                            obj.setPaymentReferenceNumber("");
                            obj.setSatusCode(0);
                            obj.setTransactionMessage("Free Payment, Amount:0");

                            UpdateSMSServicePackagePaymentAPI(obj, true, "Free Payment, Amount:0");

                        } else {
                            startPayment();
                        }

                    } else if (_response.equals("2")) {
                        Toast.makeText(ActivitySmsValueDetails.this, _message, Toast.LENGTH_SHORT).show();
                    } else if (_response.equals("3")) {
                        Toast.makeText(ActivitySmsValueDetails.this, _message, Toast.LENGTH_SHORT).show();
                    } else if (_response.equals("4")) {
                        Toast.makeText(ActivitySmsValueDetails.this, _message, Toast.LENGTH_SHORT).show();
                    } else if (_response.equals("5")) {
                        Toast.makeText(ActivitySmsValueDetails.this, _message, Toast.LENGTH_SHORT).show();
                    } else if (_response.equals("6")) {
                        Toast.makeText(ActivitySmsValueDetails.this, _message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ActivitySmsValueDetails.this, "technical error.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ClsSaveSMSServiceCustomerPackageParams> call, Throwable t) {
                pd.dismiss();
            }
        });
    }

    void UpdateSMSServicePackagePaymentAPI(ClsUpdateSMSServicePackagePayment obj, boolean _paymentStatus, String _msg) {

        InterfaceUpdateSMSServicePackagePayment interfaceUpdatePackagePayment =
                ApiClient.getDemoInstance().create(InterfaceUpdateSMSServicePackagePayment.class);

        Call<ClsUpdateSMSServicePackagePayment> objCall =
                interfaceUpdatePackagePayment.postUpdatePackagePayment(obj);

        Log.e("--Gson--", "URL: " + objCall.request().url());

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(ActivitySmsValueDetails.this,
                        "Working...", true);
        pd.show();

        objCall.enqueue(new Callback<ClsUpdateSMSServicePackagePayment>() {
            @Override
            public void onResponse(Call<ClsUpdateSMSServicePackagePayment> call,
                                   Response<ClsUpdateSMSServicePackagePayment> response) {
                pd.dismiss();
                if (response.body() != null) {


                    String _response = response.body().getSuccess();
                    Log.d("--Gson--", "onResponse: " + _response);

                    if (_paymentStatus) {

                        if (_response.equals("1")) {

                            // redirect to SMS dashboard...

                            Toast.makeText(ActivitySmsValueDetails.this, "Success", Toast.LENGTH_SHORT).show();
                            finishFlag = true;
                            finish();

                        } else if (_response.equals("2")) {
                            Toast.makeText(ActivitySmsValueDetails.this, "Customer is not found", Toast.LENGTH_SHORT).show();
                        } else if (_response.equals("3")) {
                            Toast.makeText(ActivitySmsValueDetails.this, "if payment status parameter value is other than success.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ActivitySmsValueDetails.this, "technical error.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ActivitySmsValueDetails.this, _msg, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ClsUpdateSMSServicePackagePayment> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    String paymentDesc = "";


    public void startPayment() {

        final AppCompatActivity activity = this;
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", ClsGlobal.companyName);
            options.put("description", paymentDesc);
//            options.put("image", "https://cdn.razorpay.com/logos/B5S0yCDDpK9MwB_medium.png");


            options.put("order_id", PaymentGatwayOrderID);

            options.put("currency", "INR");


// Amount Converted in Paisa.....
//            int _packageAmount = (int) (Double.valueOf(txt_totalpackageamount.getTag().toString()) * 100);
            int _packageAmount = (int) (1 * 100);
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

//        razorpayPaymentID = "pay_DRwMym42Fzr8Qb";

        try {

            ClsUpdateSMSServicePackagePayment obj = new ClsUpdateSMSServicePackagePayment();
            obj.setCustomerCode(_customerId);
            obj.setTransactionReferenceNumber(_transactionRefNum);
            obj.setPaymentStatus("success");
            obj.setPaymentMode("online");
            obj.setPaymentGateway("razorpay");
            obj.setPaymentReferenceNumber(razorpayPaymentID);
            obj.setSatusCode(0);
            obj.setTransactionMessage("Payment Successful");

            UpdateSMSServicePackagePaymentAPI(obj, true, "Payment Successful");

            Toast.makeText(this, "Payment Successful. " + razorpayPaymentID, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Payment Successful Exception." + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPaymentError(int code, String response) {

        try {
            ClsUpdateSMSServicePackagePayment obj = new ClsUpdateSMSServicePackagePayment();
            obj.setCustomerCode(_customerId);
            obj.setTransactionReferenceNumber(_transactionRefNum);
            obj.setPaymentStatus("failed");
            obj.setPaymentMode("online");
            obj.setPaymentGateway("razorpay");
            obj.setPaymentReferenceNumber("");
            obj.setSatusCode(code);
            obj.setTransactionMessage(response);

            UpdateSMSServicePackagePaymentAPI(obj, false, response);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "onPaymentError", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

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
//       yasin.nathani823@gmail.com