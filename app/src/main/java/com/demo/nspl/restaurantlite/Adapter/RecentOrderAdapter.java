package com.demo.nspl.restaurantlite.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Interface.OnItemClickListenerClsOrder;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster;

import java.util.ArrayList;
import java.util.List;

public class RecentOrderAdapter extends RecyclerView.Adapter<RecentOrderAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private static List<ClsInventoryOrderMaster> List = new ArrayList<>();
    private OnItemClickListenerClsOrder mOnItemClickListener;

    public RecentOrderAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }


    public void AddItems(List<ClsInventoryOrderMaster> List) {
        this.List = List;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_order_view, parent, false);
        return new ViewHolder(view);
    }

    public void SetOnclinkListenerClsOrder(OnItemClickListenerClsOrder onItemClickListenerClsOrder) {
        this.mOnItemClickListener = onItemClickListenerClsOrder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecentOrderAdapter.ViewHolder holder, int position) {
        ClsInventoryOrderMaster current = List.get(position);

//        holder.txt_Order_No.setText("Order No: " + current.getOrderNo());

        holder.txt_Order_No.setText(String.valueOf(current.getOrderNo()));

//        holder.txt_Order_DateTime.setText("DATE: " + ClsGlobal.getDDMYYYYAndTimeAM_And_PMFormat(current.getBillDate()));
        holder.txt_Order_DateTime.setText(ClsGlobal.getDDMYYYYAndTimeAM_And_PMFormat(current.getBillDate()));


        holder.txt_Discount.setText("DISCOUNT:\u20B9 " + ClsGlobal.round(current.getDiscountAmount(), 2));

        holder.txt_Total_Amount.setText("BILL AMOUNT:\u20B9 " + ClsGlobal.round(current.getTotalReceiveableAmount(), 2));//

        if (current.getApplyTax() != null) {
            if (current.getApplyTax().equalsIgnoreCase("YES")) {
                holder.txt_tax_Amount.setText("TAX AMOUNT:\u20B9 " + ClsGlobal.round(current.getTotalTaxAmount(), 2));
            } else {
                holder.txt_tax_Amount.setText("TAX AMOUNT:\u20B9 0.00");
            }
        }


        holder.txt_Amount.setText("PAID AMOUNT:\u20B9 " + ClsGlobal.round(current.getPaidAmount(), 2));

        if (current.getTotalAmount() < 0) {
            holder.txt_Amount.setTextColor(Color.parseColor("#c40000"));
        } /*else {
            holder.txt_Amount.setTextColor(Color.parseColor("#225A25"));
        }*/

        if (current.getCustomerName() != null) {
            holder.txt_CustomerName.setText("NAME: " + current.getCustomerName().toUpperCase() + " (" + current.getMobileNo() + ")");
        }

        if (current.getSaleReturnDiscount() != null && current.getSaleReturnDiscount() != 0.0) {
            holder.txt_sale_return_discount.setVisibility(View.VISIBLE);
            holder.txt_sale_return_discount.setText("SALE RETURN DISCOUNT:\u20B9 " + ClsGlobal.round(current.getSaleReturnDiscount(), 2));
        } else {
            holder.txt_sale_return_discount.setVisibility(View.GONE);
        }


        if (current.getMobileNo() != null) {

            holder.txt_MobileNo.setText("MOBILE: " + current.getMobileNo());

        }


        if (current.getSaleType() != null && !current.getSaleType().isEmpty()) {
            holder.txt_sale_type.setText(current.getSaleType().toUpperCase());

            if (current.getSaleType().equalsIgnoreCase("SALE")) {
                holder.txt_sale_type.setTextColor(Color.parseColor("#40932d"));
            } else {
                holder.txt_sale_type.setTextColor(Color.parseColor("#B6861B"));
            }

        } else {
            holder.txt_sale_type.setText("SALE");
            holder.txt_sale_type.setTextColor(Color.parseColor("#40932d"));

        }


        if (current.getBillTo() == null || current.getBillTo().equalsIgnoreCase("")) {
            holder.txt_bill_to.setText("CUSTOMER");
            holder.txt_bill_to.setTextColor(Color.parseColor("#000000"));
            Log.e("--Mobile--", "--IF--" + current.getOrderNo() + "---" + current.getMobileNo());
            holder.txt_CustomerName.setVisibility(View.GONE);
        } else if (current.getBillTo() != null && current.getBillTo().equalsIgnoreCase("CUSTOMER")) {
            holder.txt_bill_to.setText("CUSTOMER");
            holder.txt_bill_to.setTextColor(Color.parseColor("#000000"));
            Log.e("--Mobile--", "--IF--" + current.getOrderNo() + "---" + current.getMobileNo());
            holder.txt_CustomerName.setVisibility(View.VISIBLE);
        } else {
            holder.txt_bill_to.setText("OTHER");
            holder.txt_bill_to.setTextColor(Color.parseColor("#bf1d3e"));
            holder.txt_CustomerName.setVisibility(View.GONE);
            Log.e("--Mobile--", "--ELSE--" + current.getOrderNo() + "---" + current.getMobileNo());

        }

        holder.Bind(List.get(position), mOnItemClickListener);
    }


    @Override
    public int getItemCount() {
        return List.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_Order_No, txt_Order_DateTime, txt_Amount,
                txt_tax_Amount, txt_sale_type, txt_Total_Amount, txt_MobileNo,
                txt_CustomerName, txt_Discount, txt_bill_to, txt_sale_return_discount;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_Order_No = itemView.findViewById(R.id.txt_Order_No);
            txt_Order_DateTime = itemView.findViewById(R.id.txt_Order_DateTime);
            txt_Amount = itemView.findViewById(R.id.txt_Amount);
            txt_tax_Amount = itemView.findViewById(R.id.txt_tax_Amount);
            txt_Total_Amount = itemView.findViewById(R.id.txt_Total_Amount);
            txt_Discount = itemView.findViewById(R.id.txt_Discount);
            txt_MobileNo = itemView.findViewById(R.id.txt_MobileNo);
            txt_CustomerName = itemView.findViewById(R.id.txt_CustomerName);
            txt_bill_to = itemView.findViewById(R.id.txt_bill_to);
            txt_sale_type = itemView.findViewById(R.id.txt_sale_type);
            txt_sale_return_discount = itemView.findViewById(R.id.txt_sale_return_discount);


        }

        void Bind(final ClsInventoryOrderMaster clsInventoryOrderMaster, OnItemClickListenerClsOrder onItemClickListener) {
            itemView.setOnClickListener(v ->
                    // send current position via Onclick method.
                    onItemClickListener.OnClick(clsInventoryOrderMaster));
        }

    }


}

