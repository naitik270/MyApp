package com.demo.nspl.restaurantlite.Purchase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.nspl.restaurantlite.Interface.OnPurchaseListClick;
import com.demo.nspl.restaurantlite.R;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PurchaseListAdapter extends RecyclerView.Adapter<PurchaseListAdapter.MyViewHolder> {


    Context context;
    private View itemview;
    List<ClsPurchaseDetail> list = new ArrayList<>();
    OnPurchaseListClick onClickPaymentReport;

    public PurchaseListAdapter(Context context, List<ClsPurchaseDetail> list) {
        this.context = context;
        this.list = list;
    }

    public void SetOnClickListener(OnPurchaseListClick onClickPaymentReport) {
        this.onClickPaymentReport = onClickPaymentReport;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_purchase_data, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemview);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClsPurchaseDetail current = list.get(position);
        holder.lbl_Date.setText(current.getMonthYear());
        holder.lbl_total_amount.setText(String.valueOf(current.getGrandTotal()));
        holder.Bind(current, onClickPaymentReport);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView lbl_Date, lbl_total_amount;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header = itemView.findViewById(R.id.ll_header);
            lbl_Date = itemView.findViewById(R.id.lbl_Date);
            lbl_total_amount = itemView.findViewById(R.id.lbl_total_amount);
        }

        void Bind(ClsPurchaseDetail clsPaymentMaster, OnPurchaseListClick onClickPaymentReport) {
            ll_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickPaymentReport.OnClick(clsPaymentMaster);
                }
            });

        }
    }


}
