package com.demo.nspl.restaurantlite.VendorLedger;

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

public class VendorPaymentAdapter extends RecyclerView.Adapter<VendorPaymentAdapter.MyViewHolder> {

    private LayoutInflater inflater = null;
    Context context;
    View itemView;
    List<ClsPaymentMaster> data = new ArrayList<>();


    VendorPaymentAdapter(Context context) {
        super();
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void AddItems(List<ClsPaymentMaster> data){
        this.data = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_vendor_payment_list, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final ClsPaymentMaster objClsPaymentMaster = data.get(position);

        holder.txt_date.setText(ClsGlobal.getDDMMYYYY(objClsPaymentMaster.getPaymentDate()));


        holder.txt_amount.setText("\u20B9 " + ClsGlobal.round(objClsPaymentMaster.getAmount(), 2));


        if (objClsPaymentMaster.getAmount() < 0) {
            holder.txt_amount.setTextColor(Color.parseColor("#c40000"));
        } else {
            holder.txt_amount.setTextColor(Color.parseColor("#225A25"));
        }


        holder.txt_details.setText(objClsPaymentMaster.getPaymentDetail().toUpperCase());
        holder.txt_mode.setText(objClsPaymentMaster.getPaymentMode().toUpperCase());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView txt_amount, txt_details, txt_mode, txt_date;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header = itemView.findViewById(R.id.ll_header);
            txt_amount = itemView.findViewById(R.id.txt_amount);
            txt_details = itemView.findViewById(R.id.txt_details);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_mode = itemView.findViewById(R.id.txt_mode);

        }


    }
}
