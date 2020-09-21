package com.demo.nspl.restaurantlite.Adapter;

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
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;

import java.util.ArrayList;
import java.util.List;

public class PurchaseDetailReportAdapter extends RecyclerView.Adapter<PurchaseDetailReportAdapter.MyViewHolder> {

    Context c;
    List<ClsPaymentMaster> list = new ArrayList<>();
    View itemView;

    public PurchaseDetailReportAdapter(Context c) {
        this.c = c;

    }
//

    public void AddItems(List<ClsPaymentMaster> list) {

        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase_detail, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClsPaymentMaster current = list.get(position);

        holder.txt_date.setText(ClsGlobal.getDDMMYYYY(current.getPaymentDate().toUpperCase()));
        holder.txt_mode.setText(current.getPaymentMode().toUpperCase());

        holder.txt_amount.setText("\u20B9 " + ClsGlobal.round(current.getAmount(), 2));
        if (current.getAmount() < 0) {
            holder.txt_amount.setTextColor(Color.parseColor("#c40000"));
        } else {
            holder.txt_amount.setTextColor(Color.parseColor("#225A25"));
        }

        String Payment = "";
        if (!current.getPaymentDetail().isEmpty()) {
            Payment = current.getPaymentDetail();
        }

        if (!current.getRemark().isEmpty()) {
//            Payment = Payment.concat(", Remark:").concat(current.getRemark().toUpperCase());
            Payment = Payment.concat(", ").concat(current.getRemark().toUpperCase());
        }


        holder.txt_details.setText(Payment);
        holder.lbl_Payment_Details.setVisibility(View.GONE);
        holder.txt_payment_Details.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll_header;
        TextView txt_date, txt_mode, txt_amount, txt_details, lbl_Payment_Details, txt_payment_Details;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header = itemView.findViewById(R.id.ll_header);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_mode = itemView.findViewById(R.id.txt_mode);
            txt_amount = itemView.findViewById(R.id.txt_amount);
            txt_details = itemView.findViewById(R.id.txt_details);
            lbl_Payment_Details = itemView.findViewById(R.id.lbl_Payment_Details);
            txt_payment_Details = itemView.findViewById(R.id.txt_payment_Details);


        }
    }
}
