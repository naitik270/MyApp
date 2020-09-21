package com.demo.nspl.restaurantlite.VendorLedger;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Interface.OnVendorLedgerClick;
import com.demo.nspl.restaurantlite.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class VendorLedgerAdapter extends RecyclerView.Adapter<VendorLedgerAdapter.MyViewHolder> {

    Context context;
    private View itemview;
    List<ClsVendorLedger> list = new ArrayList<>();
    OnVendorLedgerClick onVendorLedgerClick;

    VendorLedgerAdapter(Context context) {
        this.context = context;
    }

    void AddItems(List<ClsVendorLedger> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void SetOnClickListener(OnVendorLedgerClick onVendorLedgerClick) {
        this.onVendorLedgerClick = onVendorLedgerClick;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_vendor_ledger, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemview);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClsVendorLedger current = list.get(position);


        Gson gson = new Gson();
        String jsonInString = gson.toJson(current);
        Log.e("--VendorLedgerList--", "VendorPayment: " + jsonInString);


        holder.txt_vendor_name.setText(current.getVENDOR_NAME().toUpperCase());
        holder.txt_purchase.setText("\u20B9 " + ClsGlobal.round(current.getTotalPurchaseAmount(), 2));
        holder.txt_payments.setText("\u20B9 " + ClsGlobal.round(current.getTotalPaymentAmount(), 2));

        Double _balance = current.getTotalPaymentAmount() - current.getTotalPurchaseAmount();

        holder.txt_balance.setText("\u20B9 " + ClsGlobal.round(_balance, 2));

        if (_balance < 0) {
            holder.txt_balance.setTextColor(Color.parseColor("#c40000"));
        } else {
            holder.txt_balance.setTextColor(Color.parseColor("#225A25"));
        }

        holder.txt_opening_bal.setText("(BAL) OPENING: \u20B9 " + ClsGlobal.round(current.getOpeningStock(), 2));

        double currentBal = current.getOpeningStock() + _balance;

        holder.txt_current_bal.setText("CURR.: \u20B9 " + ClsGlobal.round(currentBal, 2));

        if (currentBal < 0) {

            holder.txt_current_bal.setTypeface(holder.txt_current_bal.getTypeface(), Typeface.BOLD);
        }


        holder.Bind(current, onVendorLedgerClick, position);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView txt_vendor_name, txt_purchase, txt_payments, txt_balance, txt_opening_bal, txt_current_bal;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header = itemView.findViewById(R.id.ll_header);
            txt_vendor_name = itemView.findViewById(R.id.txt_vendor_name);
            txt_purchase = itemView.findViewById(R.id.txt_purchase);
            txt_payments = itemView.findViewById(R.id.txt_payments);
            txt_balance = itemView.findViewById(R.id.txt_balance);
            txt_opening_bal = itemView.findViewById(R.id.txt_opening_bal);
            txt_current_bal = itemView.findViewById(R.id.txt_current_bal);
        }

        void Bind(ClsVendorLedger clsVendorLedger, OnVendorLedgerClick onVendorLedgerClick, int position) {
            ll_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onVendorLedgerClick.OnClick(clsVendorLedger, position);
                }
            });

        }
    }

}
