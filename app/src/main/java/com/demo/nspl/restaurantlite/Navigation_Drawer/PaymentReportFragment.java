package com.demo.nspl.restaurantlite.Navigation_Drawer;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Adapter.PaymentReportAdapter;
import com.demo.nspl.restaurantlite.ErrorLog.ErrorExceptionHandler;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Payment.PaymentDetailActivity;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.activity.AddPaymentActivity;
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;
import com.demo.nspl.restaurantlite.classes.ViewAnimation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class PaymentReportFragment extends Fragment {

    RecyclerView rv;
    private TextView title_text;
    List<ClsPaymentMaster> list_vendor;
    List<ClsPaymentMaster> list_customer;
    PaymentReportAdapter cu;
    private View back_drop;
    private View lyt_vendor_payments;
    private View lyt_customer_payments;
    private boolean rotate = false;
    FloatingActionButton fab_vendor_payments, fab_customer_payments, fab_add;
    TextView txt_vendor, txt_customer;
    LinearLayout lyt_vendor_payment, lyt_customer_payment;


    public PaymentReportFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ErrorExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ErrorExceptionHandler(
                    getActivity(), "PaymentReportFragment"));
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Payment Report");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ClsGlobal.isFristFragment = true;
        View v = inflater.inflate(R.layout.fragment_payment_report, container, false);

        rv = v.findViewById(R.id.rv);
        title_text = v.findViewById(R.id.title_text);
        back_drop = v.findViewById(R.id.back_drop);
        txt_vendor = v.findViewById(R.id.txt_vendor);
        txt_customer = v.findViewById(R.id.txt_customer);
        lyt_customer_payments = v.findViewById(R.id.lyt_customer_payments);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        fab_vendor_payments = v.findViewById(R.id.fab_vendor_payments);
        fab_customer_payments = v.findViewById(R.id.fab_customer_payments);
        fab_add = v.findViewById(R.id.fab_add);
        lyt_vendor_payments = v.findViewById(R.id.lyt_vendor_payments);

        lyt_vendor_payment = v.findViewById(R.id.lyt_vendor_payments);
        lyt_customer_payment = v.findViewById(R.id.lyt_customer_payments);


        ViewAnimation.initShowOut(lyt_vendor_payments);
        ViewAnimation.initShowOut(lyt_customer_payments);
        back_drop.setVisibility(View.GONE);
        fab_add.setColorFilter(Color.WHITE);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyt_vendor_payments.setVisibility(View.VISIBLE);
                lyt_customer_payment.setVisibility(View.VISIBLE);
                toggleFabMode(v);
            }
        });

        back_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(fab_add);
            }
        });

        fab_vendor_payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lyt_vendor_payments.setVisibility(View.GONE);
                lyt_customer_payment.setVisibility(View.GONE);

                Intent intent = new Intent(getActivity(), AddPaymentActivity.class);
                intent.putExtra("type", "Vendor");
                intent.putExtra("ReceiptNo", 0);
                intent.putExtra("paymentID", 0);
                startActivity(intent);

                toggleFabMode(fab_add);

            }
        });

        fab_customer_payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lyt_vendor_payments.setVisibility(View.GONE);
                lyt_customer_payment.setVisibility(View.GONE);

                Intent intent = new Intent(getActivity(), AddPaymentActivity.class);
                intent.putExtra("type", "Customer");
                intent.putExtra("ReceiptNo", 0);
                intent.putExtra("paymentID", 0);
                startActivity(intent);
//                pyautogui.keyDown('winleft')
//                pyautogui.keyUp('winleft')
//                time.sleep(1)
//                pyautogui.press('esc')
                toggleFabMode(fab_add);


            }
        });


        ViewData();

        return v;
    }


    @SuppressLint("NewApi")
    private void ViewData() {
        list_vendor = new ArrayList<>();
        list_customer = new ArrayList<>();

        list_vendor = new ClsPaymentMaster().getListMonthWise(getActivity());
        list_customer = new ClsPaymentMaster().getListMonthWiseCustomer(getActivity());




        List<String> _monthList = new ArrayList<>();

        if (list_vendor != null && list_vendor.size() != 0) {
            for (ClsPaymentMaster obj : list_vendor) {

                if (!_monthList.contains(obj.getPaymentMounth())) {
                    _monthList.add(obj.getPaymentMounth());
                }
            }

        }


        if (list_customer != null && list_customer.size() != 0) {
            for (ClsPaymentMaster obj : list_customer) {

                if (!_monthList.contains(obj.getPaymentMounth())) {
                    _monthList.add(obj.getPaymentMounth());
                }

            }

        }
        // make unque month list

        if (_monthList.size() != 0) {

            List<ClsPaymentMaster> finalList = new ArrayList<>();

            // sort _monthList here.........

            for (String month : _monthList) {
                ClsPaymentMaster objPaymentMaster = new ClsPaymentMaster();
                objPaymentMaster.setPaymentMounth(month);

                if (list_vendor != null && list_vendor.size() != 0) {
                    for (ClsPaymentMaster obj : list_vendor) {
                        if (obj.getPaymentMounth().equalsIgnoreCase(month)) {
                            objPaymentMaster.set_totalVendorAmount(obj.get_totalVendorAmount());
                            break;
                        }
                    }
                }

                if (list_customer != null && list_customer.size() != 0) {
                    for (ClsPaymentMaster obj : list_customer) {
                        if (obj.getPaymentMounth().equalsIgnoreCase(month)) {
                            objPaymentMaster.set_totalCustomerAmount(obj.get_totalCustomerAmount());
                            break;
                        }
                    }
                }
                finalList.add(objPaymentMaster);
            }

            cu = new PaymentReportAdapter(getActivity(), finalList);

            cu.SetOnClickListener(clsPaymentMaster -> {

                Intent intent = new Intent(getActivity(), PaymentDetailActivity.class);
                intent.putExtra("Date", clsPaymentMaster.getPaymentMounth());
                intent.putExtra("vendorId", clsPaymentMaster.getVendorID());
                intent.putExtra("vendorName", clsPaymentMaster.getVendorName());
                startActivity(intent);

            });
        }




        if (list_vendor.size() != 0 || list_customer.size() !=0) {

            title_text.setVisibility(View.GONE);


        } else {


            title_text.setVisibility(View.VISIBLE);

        }
/*
        if (list_customer.size() == 0) {
            title_text.setVisibility(View.VISIBLE);
        } else {
            title_text.setVisibility(View.GONE);
        }*/

        Log.e("list", String.valueOf(list_vendor.size()));

        rv.setAdapter(cu);

    }


    @Override
    public void onResume() {
        super.onResume();
        ViewData();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ClsGlobal.isFristFragment = false;
    }


    private void toggleFabMode(View v) {
        rotate = ViewAnimation.rotateFab(v, !rotate);
        if (rotate) {
            Log.e("rotate", String.valueOf(rotate));
            Log.e("rot", String.valueOf("inside rotate"));

            ViewAnimation.showIn(lyt_vendor_payments);
            ViewAnimation.showIn(lyt_customer_payments);
            back_drop.setVisibility(View.VISIBLE);
        } else {
            Log.e("rot", String.valueOf("outside rotate"));
            Log.e("rotate", String.valueOf(rotate));
            ViewAnimation.showOut(lyt_vendor_payments);
            ViewAnimation.showOut(lyt_customer_payments);
            back_drop.setVisibility(View.GONE);
        }
    }


}
