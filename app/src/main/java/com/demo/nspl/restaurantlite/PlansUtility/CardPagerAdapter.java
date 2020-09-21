package com.demo.nspl.restaurantlite.PlansUtility;

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

import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.ClsPackageList;
import com.demo.nspl.restaurantlite.activity.ActivityPaymentProcess;
import com.demo.nspl.restaurantlite.activity.ActivityWebView;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<ClsPackageList> mData = new ArrayList<>();
    private float mBaseElevation;
    Context context;

    public CardPagerAdapter(Context context) {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
        this.context = context;
    }

    public void addCardItem(ClsPackageList item) {
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
                .inflate(R.layout.plans_item_adapter, container, false);
        container.addView(view);
        bind(context, mData.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);

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

    private void bind(Context context, ClsPackageList item, View view) {

        TextView txt_title =  view.findViewById(R.id.txt_title);
        TextView txt_descriptions =  view.findViewById(R.id.txt_descriptions);
        TextView txt_exp_date =  view.findViewById(R.id.txt_exp_date);
        TextView txt_total_days =  view.findViewById(R.id.txt_total_days);
        TextView txt_price =  view.findViewById(R.id.txt_price);
        TextView txt_productBrochureUrl =  view.findViewById(R.id.txt_productBrochureUrl);
        TextView txt_type =  view.findViewById(R.id.txt_type);
        TextView txt_terms =  view.findViewById(R.id.txt_terms);
        CheckBox ckb_accept = view.findViewById(R.id.ckb_accept);
        Button btn_done = view.findViewById(R.id.btn_done);

        txt_title.setText(item.getTitle());
        txt_descriptions.setText(item.getDescription());

        txt_type.setText(Html.fromHtml("<b>TYPE: </b>" + item.getType()));
        txt_productBrochureUrl.setText(Html.fromHtml("<b>PRODUCT URL: </b>" + item.getProductBrochureUrl()));
        txt_exp_date.setText(Html.fromHtml("<b>VALID UP TO: </b>" + item.getPlanValidUptoDate()));
        txt_price.setText(Html.fromHtml("<b>PRICE: </b> \u20B9" + item.getPrice()));
        txt_total_days.setText(Html.fromHtml("<b>TOTAL VALID DAYS: </b>" + item.getValidityInDays()));

        txt_terms.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActivityWebView.class);
            intent.putExtra("terms", item.getTermsFileUrl());
            context.startActivity(intent);
        });


        btn_done.setOnClickListener(v -> {
            if (!ckb_accept.isChecked()) {
                Toast.makeText(context, "Please accept terms & conditions", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(context, ActivityPaymentProcess.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("reg_mode", item.getReg_mode());
                intent.putExtra("_customerId", item.get_customerId());
                intent.putExtra("IsTaxesApplicable", item.getIsTaxesApplicable());
                intent.putExtra("SGSTValue", item.getSGSTValue());
                intent.putExtra("SGSTTaxAmount", item.getSGSTTaxAmount());
                intent.putExtra("CGSTValue", item.getCGSTValue());
                intent.putExtra("CGSTTaxAmount", item.getCGSTTaxAmount());
                intent.putExtra("IGSTValue", item.getIGSTValue());
                intent.putExtra("IGSTTaxAmount", item.getIGSTTaxAmount());
                intent.putExtra("TotalTaxAmount", item.getTotalTaxAmount());
                intent.putExtra("TotalPackageAmount", item.getTotalPackageAmount());
                intent.putExtra("packageID", item.getPackageID());
                intent.putExtra("valid_days", item.getValidityInDays());
                intent.putExtra("price", Double.valueOf(item.getPrice()));
                context.startActivity(intent);
            }
        });
    }

}
