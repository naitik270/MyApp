package com.demo.nspl.restaurantlite.Adapter;

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
import com.demo.nspl.restaurantlite.Interface.OnClickSalesReports;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster;

import java.util.ArrayList;
import java.util.List;

public class SaleReportAdapter extends RecyclerView.Adapter<SaleReportAdapter.MyViewHolder> {


    Context context;
    private View itemview;
    List<ClsInventoryOrderMaster> listSalesReports = new ArrayList<>();
    OnClickSalesReports onClickSalesReports;
    String mode = "";


    public SaleReportAdapter(Context context, List<ClsInventoryOrderMaster> list, String mode) {
        this.context = context;
        this.listSalesReports = list;
        this.mode = mode;
        Log.e("PaymentReportAdapter", "" + list.size());
    }


    public void SetOnClickSalesReports(OnClickSalesReports onClickSalesReports) {
        this.onClickSalesReports = onClickSalesReports;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_sale_report, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemview);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.e("onBindViewHolder", "onBindViewHolder call");
        if (mode.equalsIgnoreCase("SalesReports")) {
            ClsInventoryOrderMaster currentInventoryOrderMaster = listSalesReports.get(position);
            holder.lbl_Date.setText(ClsGlobal.getMonthName(currentInventoryOrderMaster.getMounth())
                    + " " + currentInventoryOrderMaster.getYear());
            holder.lbl_total_amount.setText("\u20B9 " + ClsGlobal.round(currentInventoryOrderMaster.getTotalReceiveableAmount(), 2));
            holder.BindSalesReports(currentInventoryOrderMaster, onClickSalesReports);
        }
    }

    @Override
    public int getItemCount() {
        return listSalesReports.size();
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

        void BindSalesReports(ClsInventoryOrderMaster clsInventoryOrderMaster, OnClickSalesReports onClickSalesReports) {
            if (mode.equalsIgnoreCase("SalesReports")) {
                Log.e("PaymentReportAdapter", " BindSalesReports inside");
                ll_header.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickSalesReports.OnClick(clsInventoryOrderMaster);
                    }
                });
            }

        }


    }

}
