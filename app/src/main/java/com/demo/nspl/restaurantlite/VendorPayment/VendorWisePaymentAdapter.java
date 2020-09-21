package com.demo.nspl.restaurantlite.VendorPayment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

public class VendorWisePaymentAdapter extends RecyclerView.Adapter<VendorWisePaymentAdapter.MyViewHolder> {

    Context c;
    private View itemview;
    List<ClsVendorWisePayment> list = new ArrayList<>();
    OnClickCustPayment mOnClickCustPayment;
    OnClickCustPaymentImage mOnClickCustPaymentImage;

    public VendorWisePaymentAdapter(Context context) {
        this.c = context;
    }

    public void AddItems(List<ClsVendorWisePayment> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void RemoveItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        notifyDataSetChanged();
    }

    public void SetOnClickListener(OnClickCustPayment mOnClickCustPayment) {
        this.mOnClickCustPayment = mOnClickCustPayment;
    }

    public void SetOnImageClickListener(OnClickCustPaymentImage mOnClickCustPaymentImage) {
        this.mOnClickCustPaymentImage = mOnClickCustPaymentImage;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_customer_wise_payment, parent, false);
        return new MyViewHolder(itemview);
    }

    public interface OnClickCustPayment {
        void OnClick(ClsVendorWisePayment clsPaymentMaster, int position);
    }

    public interface OnClickCustPaymentImage {
        void OnClick(ClsVendorWisePayment clsPaymentMaster, int position);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClsVendorWisePayment current = list.get(position);


        if(current.getAmount() < 0){

            holder.txt_you_gave.setText("\u20B9" + ClsGlobal.round(current.getAmount(), 2));
            holder.txt_you_gave.setVisibility(View.VISIBLE);
            holder.txt_date_time.setText(ClsGlobal.getPaymentDateFormat(current.getPaymentDate()));
            holder.ll_set_bg_color.setBackgroundColor(Color.parseColor("#27225A25"));
            holder.txt_you_gave.setTextColor(Color.parseColor("#225A25"));
        }else {

            holder.txt_you_gave.setText("\u20B9" + ClsGlobal.round(current.getAmount(), 2));
            holder.txt_you_gave.setVisibility(View.VISIBLE);
            holder.txt_date_time.setText(ClsGlobal.getPaymentDateFormat(current.getPaymentDate()));
            holder.ll_set_bg_color.setBackgroundColor(Color.parseColor("#26C02828"));
        }

        Log.d("--date--", "main: " + current.getPaymentDate());
        Log.d("--date--", "DateFormat: " + ClsGlobal.getPaymentDateFormat(current.getPaymentDate()));
        Log.d("--date--", "getAmount: " + current.getAmount());

        holder.Bind(current, mOnClickCustPayment, position);
        holder.BindViewImage(current, mOnClickCustPaymentImage, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_top_layer, ll_set_bg_color;
        TextView txt_date_time, txt_you_got, txt_you_gave, txt_payments, txt_balance, txt_credit_limit, txt_available_limit;
        ImageView iv_view_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_top_layer = itemView.findViewById(R.id.ll_top_layer);
            ll_set_bg_color = itemView.findViewById(R.id.ll_set_bg_color);
            txt_date_time = itemView.findViewById(R.id.txt_date_time);
            txt_you_got = itemView.findViewById(R.id.txt_you_got);
            txt_you_gave = itemView.findViewById(R.id.txt_you_gave);
            iv_view_img = itemView.findViewById(R.id.iv_view_img);

        }

        void Bind(final ClsVendorWisePayment clsPaymentMaster,
                  OnClickCustPayment mOnClickCustPayment, int position) {
            ll_top_layer.setOnClickListener(v ->
                    // send current position via Onclick method.
                    mOnClickCustPayment.OnClick(clsPaymentMaster, position));
        }

        void BindViewImage(final ClsVendorWisePayment clsPaymentMaster,
                           OnClickCustPaymentImage mOnClickCustPaymentImage, int position) {
            iv_view_img.setOnClickListener(v ->
                    // send current position via Onclick method.
                    mOnClickCustPaymentImage.OnClick(clsPaymentMaster, position));
        }
    }
}