package com.demo.nspl.restaurantlite.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Interface.OnClickPaymentReport;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsPaymentMaster;

import java.util.ArrayList;
import java.util.List;

public class PaymentReportAdapter extends RecyclerView.Adapter<PaymentReportAdapter.MyViewHolder> {

    Context context;
    private View itemview;
    List<ClsPaymentMaster> list = new ArrayList<>();
    //    List<ClsPaymentMaster> lstClsCustomer = new ArrayList<>();
    OnClickPaymentReport onClickPaymentReport;

    String mode = "";

    public PaymentReportAdapter(Context context, List<ClsPaymentMaster> list) {
        this.context = context;
        this.list = list;
//        import pyautogui
    }


    public void SetOnClickListener(OnClickPaymentReport onClickPaymentReport) {
        this.onClickPaymentReport = onClickPaymentReport;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_payment_list, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemview);
        return myViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.e("onBindViewHolder", "onBindViewHolder call");

        Log.e("PaymentReportAdapter", " ClsPaymentMaster inside");
        ClsPaymentMaster current = list.get(position);

//        ClsPaymentMaster currentCustomer = lstClsCustomer.get(position);

        holder.lbl_Date.setText(current.getPaymentMounth().toUpperCase());
        holder.lbl_total_amount.setText("\u20B9 " + ClsGlobal.round(current.get_totalVendorAmount(), 2));
        holder.txt_customer_payment.setText("\u20B9 " + ClsGlobal.round(current.get_totalCustomerAmount(), 2));
        holder.Bind(current, onClickPaymentReport);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView lbl_Date, lbl_total_amount, txt_customer_payment;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header = itemView.findViewById(R.id.ll_header);
            lbl_Date = itemView.findViewById(R.id.lbl_Date);
            lbl_total_amount = itemView.findViewById(R.id.lbl_total_amount);
            txt_customer_payment = itemView.findViewById(R.id.txt_customer_payment);
        }

        void Bind(ClsPaymentMaster clsPaymentMaster, OnClickPaymentReport onClickPaymentReport) {
            if (!mode.equalsIgnoreCase("SalesReports")) {
                Log.e("PaymentReportAdapter", " Bind inside");
                ll_header.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickPaymentReport.OnClick(clsPaymentMaster);
                    }
                });
            }
        }
    }


}
