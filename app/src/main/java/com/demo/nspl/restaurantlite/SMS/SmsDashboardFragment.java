package com.demo.nspl.restaurantlite.SMS;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.demo.nspl.restaurantlite.Adapter.NameValueAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ApiClient;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.activity.Activity_wts_new;
import com.demo.nspl.restaurantlite.activity.AllSmsLogsMainActivity;
import com.demo.nspl.restaurantlite.classes.ClsNameValue;
import com.demo.nspl.restaurantlite.classes.ClsUserInfo;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmsDashboardFragment extends Fragment {


    ViewPager2 viewPager;
    LinearLayout sliderDotspanel;
    LinearLayout ll_view_offers;
    TextView txt_no_data;
    TextView txt_buy_sms;
    TextView txt_free_sms;
    OfferPackagesAdapter adapter;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 100;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3500; // time in milliseconds between successive task executions.

    private int dotscount;
    private ImageView[] dots;

    TextView txt_transactional, txt_promotional, txt_total, txt_no_data_check;

    List<ClsViewSmsOffersList> lstClsViewSmsOffersLists = new ArrayList<>();

    LinearLayout ll_quotation, cv_balance;

    ProgressBar progressBar, pb_check_result;

    LinearLayout ll_data;


    public SmsDashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("SMS Dashboard");

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    getContext(), "SmsDashboardFragment"));
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_sms_dashboard, container, false);

        ClsGlobal.isFristFragment = true;

        main(v);

        return v;
    }

    ClsUserInfo objClsUserInfo = new ClsUserInfo();

    private void main(View v) {

        objClsUserInfo = ClsGlobal.getUserInfo(getActivity());

        ll_data = v.findViewById(R.id.ll_data);

        pb_check_result = v.findViewById(R.id.pb_check_result);
        progressBar = v.findViewById(R.id.progressBar);
        sliderDotspanel = v.findViewById(R.id.SliderDots);
        viewPager = v.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(5);
        txt_no_data = v.findViewById(R.id.txt_no_data);
        ll_view_offers = v.findViewById(R.id.ll_view_offers);
        txt_free_sms = v.findViewById(R.id.txt_free_sms);
        txt_buy_sms = v.findViewById(R.id.txt_buy_sms);
        cv_balance = v.findViewById(R.id.cv_balance);


        txt_transactional = v.findViewById(R.id.txt_transactional);
        txt_promotional = v.findViewById(R.id.txt_promotional);
        txt_total = v.findViewById(R.id.txt_total);


        txt_no_data_check = v.findViewById(R.id.txt_no_data_check);
        ll_quotation = v.findViewById(R.id.ll_quotation);

        adapter = new OfferPackagesAdapter(getActivity());

        if (!ClsGlobal.CheckInternetConnection(Objects.requireNonNull(getActivity()))) {
            Toast.makeText(getActivity(), "You are not connected to Internet", Toast.LENGTH_SHORT).show();
            txt_no_data_check.setText("NO INTERNET");
            pb_check_result.setVisibility(View.GONE);
            ll_view_offers.setVisibility(View.GONE);

        } else {
            CheckCustCreditSMSAPI();

            ll_view_offers.setVisibility(View.VISIBLE);
        }

        ll_quotation.setOnClickListener(v1 -> {
            Intent intent = new Intent(getActivity(), AllSmsLogsMainActivity.class);
            startActivity(intent);


        });

        ll_view_offers.setOnClickListener(view -> {


            if (!ClsGlobal.CheckInternetConnection(Objects.requireNonNull(getActivity()))) {
                Toast.makeText(getActivity(), "You are not connected to Internet", Toast.LENGTH_SHORT).show();
                txt_no_data_check.setText("NO INTERNET");
            } else {
                callAPI();
                openOfferDialog(objCall);
            }


        });

        cv_balance.setOnClickListener(view -> {

            if (!ClsGlobal.CheckInternetConnection(getActivity())) {
                Toast.makeText(getActivity(), "You are not connected to Internet", Toast.LENGTH_SHORT).show();
                txt_no_data_check.setText("NO INTERNET");
                pb_check_result.setVisibility(View.GONE);
            } else {
                CheckCustCreditSMSAPI();
            }


        });

        txt_buy_sms.setOnClickListener(view -> {

            Intent intent = new Intent(getActivity(), GetSmsIdPackagesActivity.class);
            intent.putExtra("_customerId", objClsUserInfo.getUserId());
            intent.putExtra("registrationMode", "PAID");
            startActivity(intent);
        });


        txt_free_sms.setOnClickListener(view -> {

            Intent intent = new Intent(getActivity(), GetSmsIdPackagesActivity.class);
            intent.putExtra("_customerId", objClsUserInfo.getUserId());
            intent.putExtra("registrationMode", "FREE");
            startActivity(intent);
        });


//
//        iv_refresh.setOnClickListener(view -> {
//
//            if (!ClsGlobal.CheckInternetConnection(getActivity())) {
//                Toast.makeText(getActivity(), "You are not connected to Internet", Toast.LENGTH_SHORT).show();
//                txt_no_data_check.setText("NO INTERNET");
//                pb_check_result.setVisibility(View.GONE);
//            } else {
//                CheckCustCreditSMSAPI();
//            }
//
//        });
        callAPI();
        SmsOffersListAPI(objCall);

    }


    List<ClsViewSmsOffersList> filterList = new ArrayList();

    TextView txt_no_offers;
    RecyclerView rv;
    ViewSmsOffersDashboardAdapter viewSmsOffersAdapter;


    void openOfferDialog(Call<ClsViewSmsOffersParams> objCall) {
        View view = getLayoutInflater().inflate(R.layout.view_sms_offers_list, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(bottomSheetDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        rv = bottomSheetDialog.findViewById(R.id.rv);
        txt_no_offers = bottomSheetDialog.findViewById(R.id.txt_no_offers);
        EditText edit_search_main = bottomSheetDialog.findViewById(R.id.edit_search_main);
        ImageView img_clear = bottomSheetDialog.findViewById(R.id.img_clear);

        img_clear.setOnClickListener(v -> edit_search_main.setText(""));

        bottomSheetDialog.show();
        bottomSheetDialog.getWindow().setAttributes(lp);

        ProgressDialog pd =
                ClsGlobal._prProgressDialog(getActivity(), "Loading...", true);
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
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

                        viewSmsOffersAdapter = new ViewSmsOffersDashboardAdapter(getActivity(), lstClsViewSmsOffersLists);

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

                     /*   viewSmsOffersAdapter.SetOnClickListener((clsViewSmsOffersList, position) -> {

//                            txt_get_offer_name.setText(clsViewSmsOffersList.getOfferCode());
//                            offerId = String.valueOf(clsViewSmsOffersList.getSMSServicesOfferID());
//                            ValidateSMSOfferAPI();
//                            dialog.dismiss();

                            ClsGlobal.copyToClipboard(clsViewSmsOffersList.getOfferCode(), Objects.requireNonNull(getActivity()));
                            Toast.makeText(getActivity(), "Code is copy", Toast.LENGTH_SHORT).show();
                            bottomSheetDialog.dismiss();

                        });*/

                        viewSmsOffersAdapter.SetOnTermsUrlClick((clsViewSmsOffersList, position) -> {

                            Intent intent = new Intent(getActivity(), Activity_wts_new.class);
                            intent.putExtra("webViewMode", "Offer");
                            intent.putExtra("webViewLink", clsViewSmsOffersList.getTermsFileUrl());
                            startActivity(intent);

                        });

                        rv.setLayoutManager(layoutManager);
                        rv.setAdapter(viewSmsOffersAdapter);

                    } else {
                        txt_no_offers.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getActivity(), "Server error...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsViewSmsOffersParams> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getActivity(), "Something went wrong,please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    Call<ClsViewSmsOffersParams> objCall;

    void callAPI() {

        InterfaceViewSmsOffers interfaceViewSmsOffers =
                ApiClient.getDemoInstance().create(InterfaceViewSmsOffers.class);
        objCall = interfaceViewSmsOffers.value(ClsGlobal.AppName);
        Log.e("--URL--", "objCall: " + objCall.request().url());

    }

    void CheckCustCreditSMSAPI() {
        InterfaceCheckCustCreditSMS interfaceCheckCustCreditSMS =
                ApiClient.getDemoInstance().create(InterfaceCheckCustCreditSMS.class);

        Log.e("--URL--", "API: " + ApiClient.getDemoInstance().toString());


        ClsUserInfo clsUserInfo = ClsGlobal.getUserInfo(getActivity());


        Call<ClsCheckCustCreditSMSParams> objCall =
                interfaceCheckCustCreditSMS.value(clsUserInfo.getUserId());

        Log.e("--URL--", "objCall: " + objCall.request().url());

        pb_check_result.setVisibility(View.VISIBLE);

        objCall.enqueue(new Callback<ClsCheckCustCreditSMSParams>() {
            @Override
            public void onResponse(Call<ClsCheckCustCreditSMSParams> call, Response<ClsCheckCustCreditSMSParams> response) {
                pb_check_result.setVisibility(View.GONE);
                if (response.body() != null) {

                    String _response = response.body().getSuccess();
                    String _message = response.body().getMessage();


                    String _Transactional = String.valueOf(response.body().getTransactional());
                    String Total = String.valueOf(response.body().getTotal());
                    String Promotional = String.valueOf(response.body().getPromotional());


                    switch (_response) {
                        case "1":
                            txt_no_data_check.setVisibility(View.GONE);

//                            txt_transactional.setVisibility(View.VISIBLE);
//                            txt_promotional.setVisibility(View.VISIBLE);
//                            txt_total.setVisibility(View.VISIBLE);
                            txt_promotional.setText("PROMOTIONAL: " + Promotional);
                            txt_transactional.setText("TRANSACTIONAL: " + _Transactional);
                            txt_total.setText("TOTAL: " + Total);

                            ll_data.setVisibility(View.VISIBLE);

                            break;
                        case "0":

//                            txt_transactional.setVisibility(View.GONE);
//                            txt_promotional.setVisibility(View.GONE);
//                            txt_total.setVisibility(View.GONE);

                            ll_data.setVisibility(View.GONE);

                            txt_no_data_check.setVisibility(View.VISIBLE);
                            txt_no_data_check.setText(_message);
                            break;
                    }

                } else {

                    txt_no_data_check.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<ClsCheckCustCreditSMSParams> call, Throwable t) {
                pb_check_result.setVisibility(View.GONE);
                txt_no_data_check.setVisibility(View.VISIBLE);

            }
        });

    }


    private void SmsOffersListAPI(Call<ClsViewSmsOffersParams> objCall) {
        progressBar.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.GONE);

        objCall.enqueue(new Callback<ClsViewSmsOffersParams>() {
            @Override
            public void onResponse(Call<ClsViewSmsOffersParams> call, Response<ClsViewSmsOffersParams> response) {
                progressBar.setVisibility(View.GONE);


                if (response.body() != null) {
                    viewPager.setVisibility(View.VISIBLE);

                    lstClsViewSmsOffersLists = new ArrayList<ClsViewSmsOffersList>();
                    lstClsViewSmsOffersLists = response.body().getData();

                    if (lstClsViewSmsOffersLists != null && lstClsViewSmsOffersLists.size() != 0) {

                        txt_no_data.setVisibility(View.GONE);

                        adapter.AddItems(lstClsViewSmsOffersLists);
                        viewPager.setAdapter(adapter);

                        adapter.SetOnClickListener((clsViewSmsOffersList, position) -> {

                            if (timer != null) {
                                timer.cancel();
                                timer = null;
                            }
                            showCustomDialog(clsViewSmsOffersList);

                        });

                        dotscount = adapter.getItemCount();
                        dots = new ImageView[dotscount];


                        if (dots != null) {
                            for (int i = 0; i < dotscount; i++) {
                                dots[i] = new ImageView(getActivity());
                                dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins(8, 0, 8, 0);
                                sliderDotspanel.addView(dots[i], params);
                            }

                        }


                        dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));

                        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                            }

                            @Override
                            public void onPageSelected(int position) {

                                Log.d("--Check--", "position: " + position);

                                if (dots != null) {
                                    for (int i = 0; i < dotscount; i++) {
                                        dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));
                                    }
                                    dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));
                                }

                                super.onPageSelected(position);
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                                super.onPageScrollStateChanged(state);
                            }
                        });


                        createSlideShow();


                    } else {
                        txt_no_data.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "NO OFFERS...!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "Server error...!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsViewSmsOffersParams> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                txt_no_data.setVisibility(View.VISIBLE);
            }
        });
    }


    private void showCustomDialog(ClsViewSmsOffersList clsViewSmsOffersList) {   // here is (People p)
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_sms_response);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ListView list = dialog.findViewById(R.id.list);
        ImageButton bt_close = dialog.findViewById(R.id.bt_close);
        TextView txt_doc_url = dialog.findViewById(R.id.txt_doc_url);

        bt_close.setOnClickListener(view -> {
            dialog.dismiss();
            createSlideShow();
        });

        txt_doc_url.setOnClickListener(view -> {
            dialog.dismiss();
            Intent intent = new Intent(getActivity(), Activity_wts_new.class);
            intent.putExtra("webViewMode", "Offer");
            intent.putExtra("webViewLink", clsViewSmsOffersList.getTermsFileUrl());
            startActivity(intent);

        });

        List<ClsNameValue> lstClsNameValues = new ArrayList<>();
        ClsNameValue objClsNameValues = new ClsNameValue();

     /*   objClsNameValues.setName("OfferID: ");
        objClsNameValues.setValue(String.valueOf(clsViewSmsOffersList.getSMSServicesOfferID()));
        lstClsNameValues.add(objClsNameValues);
*/

        objClsNameValues = new ClsNameValue();
        objClsNameValues.setName("Title: ");
        objClsNameValues.setValue(clsViewSmsOffersList.getOfferTitle());
        lstClsNameValues.add(objClsNameValues);

        objClsNameValues = new ClsNameValue();
        objClsNameValues.setName("Description: ");
        objClsNameValues.setValue(clsViewSmsOffersList.getOfferDescription());
        lstClsNameValues.add(objClsNameValues);

        objClsNameValues = new ClsNameValue();
        objClsNameValues.setName("Code: ");
        objClsNameValues.setValue(clsViewSmsOffersList.getOfferCode());
        lstClsNameValues.add(objClsNameValues);

        objClsNameValues = new ClsNameValue();
        objClsNameValues.setName("From : ");
        objClsNameValues.setValue(clsViewSmsOffersList.getValidityFromDate().concat(" ").concat(clsViewSmsOffersList.getValidityFromTime()));
        lstClsNameValues.add(objClsNameValues);

        objClsNameValues = new ClsNameValue();
        objClsNameValues.setName("To: ");
        objClsNameValues.setValue(clsViewSmsOffersList.getValidityToDate().concat(" ").concat(clsViewSmsOffersList.getValidityToTime()));
        lstClsNameValues.add(objClsNameValues);

        objClsNameValues = new ClsNameValue();
        objClsNameValues.setName("Applicable Per User: ");
        objClsNameValues.setValue(String.valueOf(clsViewSmsOffersList.getApplicablePerUser()));
        lstClsNameValues.add(objClsNameValues);

        if (clsViewSmsOffersList.getOfferType().equalsIgnoreCase("Discount on amount")) {

            objClsNameValues = new ClsNameValue();
            objClsNameValues.setName("Discount: ");
            objClsNameValues.setValue(String.valueOf(clsViewSmsOffersList.getDiscountedValue()).concat("%"));
            lstClsNameValues.add(objClsNameValues);

            objClsNameValues = new ClsNameValue();
            objClsNameValues.setName("Max Discount: ");
            objClsNameValues.setValue("\u20B9 " + ClsGlobal.round(clsViewSmsOffersList.getMaxDiscountedAmount(), 2));
            lstClsNameValues.add(objClsNameValues);

        } else {
            objClsNameValues = new ClsNameValue();
            objClsNameValues.setName("Transaction SMS: ");
            objClsNameValues.setValue(String.valueOf(clsViewSmsOffersList.getTransactionSMSTotalCredit()));
            lstClsNameValues.add(objClsNameValues);

            objClsNameValues = new ClsNameValue();
            objClsNameValues.setName("Promotional SMS: ");
            objClsNameValues.setValue(String.valueOf(clsViewSmsOffersList.getPromotionalSMSTotalCredit()));
            lstClsNameValues.add(objClsNameValues);

            objClsNameValues = new ClsNameValue();
            objClsNameValues.setName("Total SMS: ");
            objClsNameValues.setValue(String.valueOf(clsViewSmsOffersList.getTotalSMSCredit()));
            lstClsNameValues.add(objClsNameValues);

        }


/*
        objClsNameValues = new ClsNameValue();
        objClsNameValues.setName("Offer Type: ");
        objClsNameValues.setValue(clsViewSmsOffersList.getOfferType());
        lstClsNameValues.add(objClsNameValues);
*/

        /*objClsNameValues = new ClsNameValue();
        objClsNameValues.setName("Discount Type: ");
        objClsNameValues.setValue(clsViewSmsOffersList.getDiscountType());
        lstClsNameValues.add(objClsNameValues);
*/

     /*   objClsNameValues = new ClsNameValue();
        objClsNameValues.setName("Document Available: ");
        objClsNameValues.setValue(clsViewSmsOffersList.getISDocumentAvailable());
        lstClsNameValues.add(objClsNameValues);*/

//        objClsNameValues = new ClsNameValue();
//        objClsNameValues.setName("Document Url: ");
//        objClsNameValues.setValue(clsViewSmsOffersList.getDocumentUrl());
//        lstClsNameValues.add(objClsNameValues);

        NameValueAdapter adpNameValueAdapter = new NameValueAdapter(getActivity(), lstClsNameValues);
        list.setAdapter(adpNameValueAdapter);

        dialog.show();
        dialog.getWindow().setAttributes(lp);
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
                viewSmsOffersAdapter.updateList(filterList);
                txt_no_offers.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
            } else {
                txt_no_offers.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
            }

        } else {
            viewSmsOffersAdapter.updateList(lstClsViewSmsOffersLists);
            txt_no_offers.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }
    }

    void createSlideShow() {
        /*After setting the adapter use the timer */

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == lstClsViewSmsOffersLists.size()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!ClsGlobal.CheckInternetConnection(Objects.requireNonNull(getActivity()))) {
            Toast.makeText(getActivity(), "You are not connected to Internet",
                    Toast.LENGTH_SHORT).show();
            txt_no_data_check.setText("NO INTERNET");
            pb_check_result.setVisibility(View.GONE);
            ll_view_offers.setVisibility(View.GONE);
        } else {
            CheckCustCreditSMSAPI();
            ll_view_offers.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;

        if (timer != null) {
            timer.cancel();
            timer = null;
        }

    }
}
