package com.demo.nspl.restaurantlite.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster;

import java.util.ArrayList;
import java.util.List;

public class SalesDetailAdapter extends RecyclerView.Adapter<SalesDetailAdapter.MyViewHolder> {

    Context c;
    List<ClsInventoryOrderMaster> list = new ArrayList<>();
    private View itemview;

    public SalesDetailAdapter(Context c) {
        this.c = c;
    }


    public void AddItems(List<ClsInventoryOrderMaster> list) {

        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sales_details, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemview);
        return myViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClsInventoryOrderMaster current = list.get(position);
        holder.lbl_Date.setText(ClsGlobal.getDDMYYYYAndTimeAM_And_PMFormat(current.getBillDate()));
        holder.lbl_No_Bill.setText(String.valueOf(current.getOrderNo()).toUpperCase());

        holder.lbl_Amount.setText("\u20B9 " + ClsGlobal.round(current.getTotalReceiveableAmount(), 2));

        if (current.getTotalReceiveableAmount() < 0) {
            holder.lbl_Amount.setTextColor(Color.parseColor("#c40000"));
        } else {
            holder.lbl_Amount.setTextColor(Color.parseColor("#225A25"));
        }

        holder.lbl_Mode.setText(current.getPaymentMode().toUpperCase());
        holder.lbl_Payment_Details.setText(current.getPaymentDetail().toUpperCase());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll_header;
        TextView lbl_Date, lbl_No_Bill, lbl_Amount, lbl_Mode, lbl_Payment_Details;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header = itemView.findViewById(R.id.ll_header);
            lbl_Date = itemView.findViewById(R.id.lbl_Date);
            lbl_No_Bill = itemView.findViewById(R.id.lbl_No_Bill);

            lbl_Amount = itemView.findViewById(R.id.lbl_Amount);
            lbl_Mode = itemView.findViewById(R.id.lbl_Mode);
            lbl_Payment_Details = itemView.findViewById(R.id.lbl_Payment_Details);

        }
    }
}
