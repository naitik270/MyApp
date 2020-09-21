package com.demo.nspl.restaurantlite.Customer;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsCustomerMaster;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolder> {

    Context context;
    View itemview;
    List<ClsCustomerMaster> data = new ArrayList<>();
    OnCustomerClick onCustomerClick;


    CustomerAdapter(Context context) {
        this.context = context;
    }

    public void AddItems(List<ClsCustomerMaster> data) {

        this.data = data;
        notifyDataSetChanged();
    }


    public void RemoveItem(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        notifyDataSetChanged();
    }


    public void SetOnClickListener(OnCustomerClick onCustomerClick) {
        this.onCustomerClick = onCustomerClick;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_customer_list, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemview);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ClsCustomerMaster current = data.get(position);

        holder.txt_name.setText("Name: " + current.getmName().toUpperCase());
        holder.txt_mobile.setText("Mobile: " + current.getmMobile_No());
        holder.txt_address.setText("Address: " + current.getAddress());
        holder.txt_gst_no.setText("Gst No# " + current.getGST_NO());
        holder.txt_cpny_name.setText("Company: " + current.getCompany_Name());
        holder.txt_Credit.setText("Credit: " + current.getCredit());

        holder.txt_balance_type.setText(current.getBalanceType());


        if (current.getBalanceType().equalsIgnoreCase("TO PAY")) {
            holder.txt_balance_type.setTextColor(Color.parseColor("#c40000"));
        } else {
            holder.txt_balance_type.setTextColor(Color.parseColor("#225A25"));
        }

        holder.txt_opening_bal.setText("\u20B9 " + current.getOpeningStock());

        if (current.getOpeningStock() < 0) {
            holder.txt_opening_bal.setTextColor(Color.parseColor("#c40000"));
        }else {
            holder.txt_opening_bal.setTextColor(Color.parseColor("#225A25"));
        }

        holder.Bind(current, onCustomerClick, position);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView txt_name, txt_mobile, txt_address, txt_cpny_name, txt_balance_type, txt_gst_no, txt_Credit, txt_opening_bal;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header = itemView.findViewById(R.id.ll_header);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_mobile = itemView.findViewById(R.id.txt_mobile);
            txt_address = itemView.findViewById(R.id.txt_address);
            txt_cpny_name = itemView.findViewById(R.id.txt_cpny_name);
            txt_gst_no = itemView.findViewById(R.id.txt_gst_no);
            txt_Credit = itemView.findViewById(R.id.txt_Credit);
            txt_opening_bal = itemView.findViewById(R.id.txt_opening_bal);
            txt_balance_type = itemView.findViewById(R.id.txt_balance_type);

        }

        void Bind(ClsCustomerMaster clsCustomerMaster, OnCustomerClick onCustomerClick, int position) {
            ll_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCustomerClick.OnClick(clsCustomerMaster, position);
                }
            });

        }
    }


}
