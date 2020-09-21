package com.demo.nspl.restaurantlite.SMS;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.PlansUtility.CardAdapter;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.activity.ActivityWebView;

import java.util.ArrayList;
import java.util.List;

public class GetSmsPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<ClsGetSmsPackageList> mData = new ArrayList<>();
    private float mBaseElevation;
    Context context;

    public GetSmsPagerAdapter(Context context) {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
        this.context = context;
    }

    public void addCardItem(ClsGetSmsPackageList item) {
        mViews.add(null);
        mData.add(item);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.get_sms_plans_item_adapter, container, false);
        container.addView(view);
        bind(context, mData.get(position), view);
        CardView cardView = view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(Context context, ClsGetSmsPackageList item, View view) {



        TextView txt_title = view.findViewById(R.id.txt_title);
        TextView txt_descriptions = view.findViewById(R.id.txt_descriptions);
        TextView txt_validity_days = view.findViewById(R.id.txt_validity_days);
        TextView txt_package_type = view.findViewById(R.id.txt_package_type);
        TextView txt_total_sms_credit = view.findViewById(R.id.txt_total_sms_credit);
        TextView txt_transaction_sms_total_credit = view.findViewById(R.id.txt_transaction_sms_total_credit);
        TextView txt_promotional_sms_total_credit = view.findViewById(R.id.txt_promotional_sms_total_credit);
        TextView txt_sms_validity = view.findViewById(R.id.txt_sms_validity);
        TextView txt_package_valid_up_to_date = view.findViewById(R.id.txt_package_valid_up_to_date);
        TextView txt_package_valid_up_to_time = view.findViewById(R.id.txt_package_valid_up_to_time);
        TextView txt_package_price = view.findViewById(R.id.txt_package_price);
        TextView txt_terms = view.findViewById(R.id.txt_terms);
        CheckBox ckb_accept = view.findViewById(R.id.ckb_accept);
        Button btn_done = view.findViewById(R.id.btn_done);

        TextView txt_productBrochureUrl = view.findViewById(R.id.txt_productBrochureUrl);

        txt_title.setText(item.getPackageTitle());
        txt_descriptions.setText(item.getPackageDescription());
        txt_package_type.setVisibility(View.GONE);
        txt_package_type.setText(Html.fromHtml("<b>PACKAGE TYPE: </b>" + item.getPackageType().toUpperCase()));

        txt_validity_days.setText(Html.fromHtml("<b>VALIDITY IN DAYS: </b>" + item.getValidityInDays()));
        txt_total_sms_credit.setText(Html.fromHtml("<b>TOTAL SMS CREDIT: </b>" + item.getTotalSMSCredit()));

        if (item.getTransactionSMSTotalCredit() != 0) {
            txt_transaction_sms_total_credit.setText(Html.fromHtml("<b>TRANSACTION SMS: </b>" + item.getTransactionSMSTotalCredit()));
        } else {
            txt_transaction_sms_total_credit.setVisibility(View.GONE);
        }

        if (item.getPromotionalSMSTotalCredit() != 0) {
            txt_promotional_sms_total_credit.setText(Html.fromHtml("<b>PROMOTIONAL SMS: </b>" + item.getPromotionalSMSTotalCredit()));
        } else {
            txt_promotional_sms_total_credit.setVisibility(View.GONE);
        }

        txt_sms_validity.setText(Html.fromHtml("<b>SMS VALIDITY: </b>" + item.getSMSValidity().toUpperCase()));
        txt_package_valid_up_to_date.setText(Html.fromHtml("<b>VALID UP TO: </b>" + item.getPackageValidUpToDate().concat(" ").concat(item.getPackageValidUpToTime())));

        txt_package_valid_up_to_time.setVisibility(View.GONE);
        txt_package_valid_up_to_time.setText(Html.fromHtml("<b>VALID UP TO TIME: </b>" + item.getPackageValidUpToTime()));

        txt_package_price.setText(Html.fromHtml("<b>PRICE: </b> \u20B9 " + ClsGlobal.round(item.getPackagePrice(), 2)));

        txt_terms.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActivityWebView.class);
            intent.putExtra("terms", item.getTermsFileUrl());
            context.startActivity(intent);
        });


        btn_done.setOnClickListener(v -> {
            if (!ckb_accept.isChecked()) {
                Toast.makeText(context, "Please accept terms & conditions", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(context, ActivitySmsValueDetails.class);

                ClsSmsSummaryValue objClsSmsSummaryValue = new ClsSmsSummaryValue();
                objClsSmsSummaryValue.setTransactionSMSTotalCredit(Double.valueOf(item.getTransactionSMSTotalCredit()));
                objClsSmsSummaryValue.setPromotionalSMSTotalCredit(Double.valueOf(item.getPromotionalSMSTotalCredit()));
                objClsSmsSummaryValue.setTotalSMSCredit(Double.valueOf(item.getTotalSMSCredit()));

                intent.putExtra("title", item.getPackageTitle());
                intent.putExtra("objValue", objClsSmsSummaryValue);
                intent.putExtra("reg_mode", item.getReg_mode());
                intent.putExtra("_customerId", item.get_customerId());
                intent.putExtra("packageId", item.getSMSServicesPackageID());

                intent.putExtra("SGSTValue", item.getSGSTRate());
                intent.putExtra("SGSTTaxAmount", item.getSGSTTaxAmount());
                intent.putExtra("CGSTValue", item.getCGSTRate());
                intent.putExtra("CGSTTaxAmount", item.getCGSTTaxAmount());
                intent.putExtra("IGSTValue", item.getIGSTRate());
                intent.putExtra("IGSTTaxAmount", item.getIGSTTaxAmount());
                intent.putExtra("TotalTaxAmount", item.getTotalTaxAmount());
//                intent.putExtra("TaxableAmount", item.getTa());

                intent.putExtra("TotalPackageAmount", item.getTotalAmount());

                intent.putExtra("packageID", item.getSMSServicesPackageID());
                intent.putExtra("valid_days", item.getValidityInDays());
                intent.putExtra("price", Double.valueOf(item.getPackagePrice()));
                context.startActivity(intent);
            }
        });
    }

}
