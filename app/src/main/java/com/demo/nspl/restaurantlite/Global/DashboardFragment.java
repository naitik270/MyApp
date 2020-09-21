package com.demo.nspl.restaurantlite.Global;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.VendorPayment.DirectVendorPaymentActivity;
import com.demo.nspl.restaurantlite.activity.DirectCustomerPaymentActivity;
import com.demo.nspl.restaurantlite.activity.SalesActivity;
import com.demo.nspl.restaurantlite.classes.ClsLayerItemMaster;
import com.demo.nspl.restaurantlite.classes.ClsUserInfo;
import com.google.gson.Gson;

public class DashboardFragment extends Fragment {

    TextView txt_sale;
    TextView txt_version;
    TextView txt_wholesale_invoice;
    LinearLayout cv_sale, cv_wholesale, ll_quotation, ll_quotation_wholesale;
    LinearLayout ll_customer_payment,ll_vendor_payment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        txt_sale = v.findViewById(R.id.txt_sale);
        cv_sale = v.findViewById(R.id.cv_sale);
        cv_wholesale = v.findViewById(R.id.cv_wholesale);
        ll_quotation = v.findViewById(R.id.ll_quotation);
        txt_version = v.findViewById(R.id.txt_version);
        txt_wholesale_invoice = v.findViewById(R.id.txt_wholesale_invoice);
        ll_quotation_wholesale = v.findViewById(R.id.ll_quotation_wholesale);

        ll_customer_payment = v.findViewById(R.id.ll_customer_payment);
        ll_vendor_payment = v.findViewById(R.id.ll_vendor_payment);


        txt_version.setText("v" + ClsGlobal.getApplicationVersion(getActivity()));

        cv_sale.setOnClickListener(v1 -> {

            if (moveToLoginPage()) {
                ClsLayerItemMaster.updateTax(getActivity());

                String entryMode = ClsGlobal.GetOrderEditMode(getActivity());

                if (entryMode == null || entryMode.isEmpty()) {
                    entryMode = "New";
                }

                Intent intent = new Intent(getActivity(), SalesActivity.class);
                intent.putExtra("saleMode", "Sale");
                intent.putExtra("entryMode", entryMode);
                intent.putExtra("editSource", "");
                intent.putExtra("editOrderNo", "");
                intent.putExtra("editOrderID", "");
                ClsGlobal.SetOrderEditMode(getActivity(), entryMode);

                startActivity(intent);
            }

//            ClsQuotationMaster.UpdateQuotationDateFormat(getActivity());

//                Data data = new Data.Builder()
//                        .putString("Mode", "AutoBackup again")
//                        .build();
//
//                Constraints myConstraints = new Constraints.Builder()
//                        .setRequiredNetworkType(NetworkType.CONNECTED)
//                        .build();
//
//                OneTimeWorkRequest oneTimeWorkRequest =
//                        new OneTimeWorkRequest.Builder(AutoBackUpTask.class)
//                                .setInputData(data)
//                                .setConstraints(myConstraints)
//                                .build();
//
//                WorkManager.getInstance().enqueueUniqueWork(ClsGlobal.AppPackageName
//                        .concat("AutoBackup_again"), ExistingWorkPolicy.REPLACE, oneTimeWorkRequest);

        });

        cv_wholesale.setOnClickListener(v12 -> {

            if (moveToLoginPage()) {
                ClsLayerItemMaster.updateTax(getActivity());

                String entryMode = ClsGlobal.GetOrderEditMode(getActivity());

                if (entryMode == null || entryMode.isEmpty()) {
                    entryMode = "New";
                }

                Intent intent = new Intent(getActivity(), SalesActivity.class);
                intent.putExtra("saleMode", "Wholesale");
                intent.putExtra("entryMode", entryMode);
                intent.putExtra("editSource", "");
                intent.putExtra("editOrderNo", "");
                intent.putExtra("editOrderID", "");
                ClsGlobal.SetOrderEditMode(getActivity(), entryMode);
                startActivity(intent);
            }

//                ClsSMSLogs.DeleteAll(getActivity());


        });

        ll_quotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moveToLoginPage()) {
                    ClsLayerItemMaster.updateTax(getActivity());

                    String entryMode = ClsGlobal.GetOrderEditMode(getActivity());

                    if (entryMode == null || entryMode.isEmpty()) {
                        entryMode = "New";
                    }

                    Intent intent = new Intent(getActivity(), SalesActivity.class);
                    intent.putExtra("saleMode", "Retail Quotation");
                    intent.putExtra("entryMode", entryMode);
                    intent.putExtra("editSource", "");
                    intent.putExtra("editOrderNo", "");
                    intent.putExtra("editOrderID", "");
                    ClsGlobal.SetOrderEditMode(getActivity(), entryMode);

                    startActivity(intent);
                }

//                ClsBulkSMSLog.DeleteAll(getActivity());

//                SQLiteDatabase db =getActivity().openOrCreateDatabase(ClsGlobal.Database_Name,
//                        Context.MODE_PRIVATE, null);
//
//                String where = " AND [QuotationNo] = ".concat("'").concat(String.valueOf(1)).concat("'")
//                        .concat(" AND [QuotationID] = ").concat(String.valueOf(1));
//
//
//                new ClsQuotationOrderDetail().getQuotationDetailList(where,
//                        getActivity(), db);


            }
        });

        ll_quotation_wholesale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (moveToLoginPage()) {
                    ClsLayerItemMaster.updateTax(getActivity());

                    String entryMode = ClsGlobal.GetOrderEditMode(getActivity());

                    if (entryMode == null || entryMode.isEmpty()) {
                        entryMode = "New";
                    }

                    Intent intent = new Intent(getActivity(), SalesActivity.class);
                    intent.putExtra("saleMode", "Wholesale Quotation");
                    intent.putExtra("entryMode", entryMode);
                    intent.putExtra("editSource", "");
                    intent.putExtra("editOrderNo", "");
                    intent.putExtra("editOrderID", "");
                    ClsGlobal.SetOrderEditMode(getActivity(), entryMode);

                    startActivity(intent);
                }


//                ClsGlobal.SendAutoEmail(getActivity(), "Auto generated email");


            }
        });

        ll_customer_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DirectCustomerPaymentActivity.class);
                intent.putExtra("type", "Customer");
                startActivity(intent);
            }
        });

        ll_vendor_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DirectVendorPaymentActivity.class);
                intent.putExtra("type", "Vendor");
                startActivity(intent);
            }
        });

        return v;
    }


    boolean moveToLoginPage() {
        boolean result = true;

        ClsUserInfo objClsUserInfoOld = ClsGlobal.getUserInfo(getActivity());
        Log.d("getFirstDateOfMonth", "----UserInfo---" + objClsUserInfoOld.getLoginStatus());

        Gson gson = new Gson();
        String jsonInString = gson.toJson(objClsUserInfoOld);
        Log.d("--moveToLoginPage--", "moveToLoginPage: " + jsonInString);


        if (!objClsUserInfoOld.getLoginStatus().equalsIgnoreCase("ACTIVE")) {
            result = false;
            getActivity().finish();
        }
        return result;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Click to sale");
    }


}
