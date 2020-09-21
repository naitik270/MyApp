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
import com.demo.nspl.restaurantlite.Interface.OnPaymentCustomerClick;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;

import java.util.ArrayList;
import java.util.List;

public class PaymentDetailsAdapter extends RecyclerView.Adapter<PaymentDetailsAdapter.MyViewHolder> {

    Context c;
    private View itemview;
    List<ClsPaymentMaster> list = new ArrayList<ClsPaymentMaster>();
    String mode = "";
    private OnPaymentCustomerClick onPaymentCustomerClick;

    public PaymentDetailsAdapter(Context c, String mode) {
        this.c = c;
        this.mode = mode;
    }


    public void AddItems(List<ClsPaymentMaster> list) {

        this.list = list;
        notifyDataSetChanged();
    }

    public void RemoveItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        notifyDataSetChanged();
    }


    public void SetOnClickListener(OnPaymentCustomerClick onPaymentCustomerClick) {
        this.onPaymentCustomerClick = onPaymentCustomerClick;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_details_list, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemview);
        return myViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClsPaymentMaster current = list.get(position);

        holder.lbl_Date.setText(ClsGlobal.getDDMMYYYY(current.getPaymentDate()));

        holder.lbl_Mode.setText(current.getPaymentMode().toUpperCase());

        holder.txt_mobile.setText(current.getMobileNo());

        if (mode.equalsIgnoreCase("CustomerPayment")) {
            holder.txt_name.setText(current.getCustomerName().toUpperCase());
        } else {
            holder.txt_name.setText(current.getVendorName().toUpperCase());
        }
        String _details = "";


        if (current.getPaymentDetail() != null && !current.getPaymentDetail().isEmpty()) {
            _details = current.getPaymentDetail().toUpperCase();
        }

        if (current.getRemark() != null && !current.getRemark().isEmpty()) {
            _details = _details.concat(", " + current.getRemark().toUpperCase());
        }

        holder.lbl_Payment_Details.setText(_details);
        holder.txt_receipt_no.setText(String.valueOf(current.getReceiptNo()));

        holder.lbl_Amount.setText("\u20B9 " + ClsGlobal.round(current.getAmount(), 2));
//        holder.lbl_Amount.setVisibility(View.GONE);


        if (current.getAmount() < 0) {
            holder.lbl_Amount.setTextColor(Color.parseColor("#c40000"));
        } else {
            holder.lbl_Amount.setTextColor(Color.parseColor("#225A25"));
        }


        holder.Bind(current, onPaymentCustomerClick, position);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        LinearLayout ll_mobile;
        TextView lbl_Date, txt_name, lbl_Mode, lbl_Payment_Details, lbl_Amount,
                txt_receipt_no, txt_mobile;


        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header = itemView.findViewById(R.id.ll_header);
            ll_mobile = itemView.findViewById(R.id.ll_mobile);
            lbl_Date = itemView.findViewById(R.id.lbl_Date);
            txt_mobile = itemView.findViewById(R.id.txt_mobile);
            txt_name = itemView.findViewById(R.id.txt_name);
            lbl_Mode = itemView.findViewById(R.id.lbl_Mode);
            lbl_Payment_Details = itemView.findViewById(R.id.lbl_Payment_Details);
            lbl_Amount = itemView.findViewById(R.id.lbl_Amount);
            txt_receipt_no = itemView.findViewById(R.id.txt_receipt_no);

            if (!mode.equalsIgnoreCase("CustomerPayment")) {
                ll_mobile.setVisibility(View.GONE);
            }

        }


        void Bind(ClsPaymentMaster clsPaymentMaster, OnPaymentCustomerClick onPaymentCustomerClick,
                  int position) {
            ll_header.setOnClickListener(v ->
                    // send current position via Onclick method.
                    onPaymentCustomerClick.OnClick(clsPaymentMaster, position));
        }

    }


}
