package com.demo.nspl.restaurantlite.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsQuotationMaster;

import java.util.ArrayList;
import java.util.List;

public class QuotationReportAdapter extends RecyclerView.Adapter<QuotationReportAdapter.MyViewHolder> {

    String mode = "";
    Context context;
    private View itemView;
    List<ClsQuotationMaster> data = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public QuotationReportAdapter(Context context,String mode) {
        this.context = context;
        this.mode = mode;
    }

    public void AddItems(List<ClsQuotationMaster> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void remove(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        notifyDataSetChanged();
    }

    public void SetOnClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_quotation_report, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (mode.equalsIgnoreCase("QuotationReports")) {
            ClsQuotationMaster objClsQuotationMaster = data.get(position);

            String _dateMonth = ClsGlobal.getMonthName(objClsQuotationMaster.getMounth())
                    + " " + objClsQuotationMaster.getYear();

            holder.txt_date.setText(_dateMonth);
            holder.txt_total_amount.setText("\u20B9 " + ClsGlobal.round(objClsQuotationMaster.getGrandTotal(), 2));

            holder.bindQuotationReport(objClsQuotationMaster, onItemClickListener);
        }


    }

    public interface OnItemClickListener {
        void OnClick(ClsQuotationMaster clsQuotationMaster);

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView txt_date, txt_total_amount;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header = itemView.findViewById(R.id.ll_header);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_total_amount = itemView.findViewById(R.id.txt_total_amount);

        }

        void bindQuotationReport(ClsQuotationMaster clsQuotationMaster,
                                 OnItemClickListener onItemClickListener) {

            if (mode.equalsIgnoreCase("QuotationReports")) {

                ll_header.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        onItemClickListener.OnClick(clsQuotationMaster);

                    }
                });
            }

        }
    }

}
