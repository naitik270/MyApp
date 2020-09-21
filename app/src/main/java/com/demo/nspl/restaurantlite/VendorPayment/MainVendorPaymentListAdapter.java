package com.demo.nspl.restaurantlite.VendorPayment;

import android.annotation.SuppressLint;
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
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.VendorLedger.ClsVendorLedger;

import java.util.ArrayList;
import java.util.List;

public class MainVendorPaymentListAdapter extends RecyclerView.Adapter<MainVendorPaymentListAdapter.MyViewHolder> {

    Context c;
    private View itemview;
    List<ClsVendorLedger> list = new ArrayList<>();
    OnClickCustPayment mOnClickCustPayment;

    public MainVendorPaymentListAdapter(Context context) {
        this.c = context;
    }

    public void AddItems(List<ClsVendorLedger> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void SetOnClickListener(OnClickCustPayment mOnClickCustPayment) {
        this.mOnClickCustPayment = mOnClickCustPayment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_direct_payment, parent, false);
        return new MyViewHolder(itemview);
    }


    public interface OnClickCustPayment {
        void OnClick(ClsVendorLedger clsVendorLedger);

    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClsVendorLedger current = list.get(position);
        holder.txt_customer_name.setText(current.getVENDOR_NAME().toUpperCase());

        String headerText = "";
        if (current.getVENDOR_NAME() != null && !current.getVENDOR_NAME().equalsIgnoreCase("")) {
            headerText = "" + current.getVENDOR_NAME().charAt(0);
        }

        holder.txt_charcter.setText(headerText.toUpperCase());

        double balanceAmt = 0.0;
        balanceAmt = current.getTotalPaymentAmount() - current.getTotalPurchaseAmount();

        Log.d("--amt--", "getTotalPaymentAmount: "+current.getTotalPaymentAmount());
        Log.d("--amt--", "getTotalPurchaseAmount: "+current.getTotalPurchaseAmount());

        if (balanceAmt < 0) {
            holder.txt_balance.setTextColor(Color.parseColor("#c40000"));
        } else {
            holder.txt_balance.setTextColor(Color.parseColor("#225A25"));
        }
        holder.txt_balance.setText("\u20B9 " + ClsGlobal.round(balanceAmt, 2));

        holder.Bind(current, mOnClickCustPayment);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_top_layer;
        TextView txt_customer_name, txt_charcter, txt_balance;


        public MyViewHolder(View itemView) {
            super(itemView);
            ll_top_layer = itemView.findViewById(R.id.ll_top_layer);
            txt_customer_name = itemView.findViewById(R.id.txt_customer_name);
            txt_charcter = itemView.findViewById(R.id.txt_charcter);
            txt_balance = itemView.findViewById(R.id.txt_balance);
        }

        void Bind(final ClsVendorLedger clsVendorLedger,
                  OnClickCustPayment mOnClickCustPayment) {
            ll_top_layer.setOnClickListener(v ->
                    // send current position via Onclick method.
                    mOnClickCustPayment.OnClick(clsVendorLedger));
        }
    }


}
