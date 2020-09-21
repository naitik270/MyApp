package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Interface.OnClickCustomerLedger;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.VendorLedger.ClsVendorLedger;

import java.util.ArrayList;
import java.util.List;

public class CustomerLedgerAdapter extends RecyclerView.Adapter<CustomerLedgerAdapter.MyViewHolder> {

    Context c;
    private View itemview;
    List<ClsVendorLedger> list = new ArrayList<>();
    OnClickCustomerLedger onClickCustomerLedger;

    public CustomerLedgerAdapter(Context context) {
        this.c = context;
    }

    public void AddItems(List<ClsVendorLedger> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void SetOnClickListener(OnClickCustomerLedger onClickCustomerLedger) {
        this.onClickCustomerLedger = onClickCustomerLedger;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_customer_ladger, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemview);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClsVendorLedger current = list.get(position);
        holder.txt_customer_name.setText(current.getCustomerName().toUpperCase());
        holder.txt_Mobile_No.setText("MOBILE: " + current.getCustomerMobileNo());
        holder.txt_purchase.setText("\u20B9 " + ClsGlobal.round(current.getTotalSaleAmount(), 2));
        holder.txt_payments.setText("\u20B9 " + ClsGlobal.round(current.getTotalPaymentAmount(), 2));

        // TotalPaymentAmount
        String log = "";
        log = log.concat("current.getCustomerName()" + current.getCustomerName());

        Double balanceAmt = 0.0;


        log = log.concat("true1");
        log = log.concat("getTotalPaymentAmount" + current.getTotalPaymentAmount());
        log = log.concat("getTotalSaleAmount" + current.getTotalSaleAmount());

        balanceAmt = (current.getTotalSaleAmount() - current.getTotalPaymentAmount()/*5000*/) * -1;//-400

        log = log.concat("balanceAmt" + balanceAmt);

        Log.e("log", log);

        holder.txt_balance.setText("\u20B9 " + ClsGlobal.round(balanceAmt, 2));

        if (balanceAmt < 0) {
            holder.txt_balance.setTextColor(Color.parseColor("#c40000"));
        } else {
            holder.txt_balance.setTextColor(Color.parseColor("#225A25"));
        }

        if (current.getCreditLimit() != 0.0) {

            holder.txt_credit_limit.setText("(CREDIT) LIMIT:\u20B9 " +
                    ClsGlobal.round(current.getCreditLimit(), 2));
            holder.txt_available_limit.setText("AVL.:\u20B9 " +
                    ClsGlobal.round(current.getCreditLimit() + current.getTotalPaymentAmount() - current.getTotalSaleAmount(), 2));

        } else {
            holder.txt_credit_limit.setText("(CREDIT) LIMIT:\u20B9 " + "0.00");
            holder.txt_available_limit.setText("AVL.:\u20B9 " + "0.00");
        }

        holder.Bind(current, onClickCustomerLedger);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView txt_customer_name, txt_Mobile_No, txt_purchase, txt_payments, txt_balance, txt_credit_limit, txt_available_limit;


        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header = itemView.findViewById(R.id.ll_header);
            txt_customer_name = itemView.findViewById(R.id.txt_customer_name);
            txt_Mobile_No = itemView.findViewById(R.id.txt_Mobile_No);
            txt_purchase = itemView.findViewById(R.id.txt_purchase);
            txt_payments = itemView.findViewById(R.id.txt_payments);
            txt_balance = itemView.findViewById(R.id.txt_balance);
            txt_credit_limit = itemView.findViewById(R.id.txt_credit_limit);
            txt_available_limit = itemView.findViewById(R.id.txt_available_limit);

        }

        void Bind(final ClsVendorLedger clsVendorLedger, OnClickCustomerLedger onItemClickListener) {
            ll_header.setOnClickListener(v ->
                    // send current position via Onclick method.
                    onItemClickListener.OnClick(clsVendorLedger));
        }
    }
}
