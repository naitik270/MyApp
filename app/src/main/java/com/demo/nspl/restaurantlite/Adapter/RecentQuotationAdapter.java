package com.demo.nspl.restaurantlite.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsQuotationMaster;

import java.util.ArrayList;
import java.util.List;

public class RecentQuotationAdapter extends RecyclerView.Adapter<RecentQuotationAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private static List<ClsQuotationMaster> List = new ArrayList<>();
    OnItemClick onItemClick;

    public RecentQuotationAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }


    public void AddItems(List<ClsQuotationMaster> List) {
        this.List = List;
        notifyDataSetChanged();
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_quotation_view, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClsQuotationMaster obj = List.get(position);
        holder.txt_quot_no.setText(String.valueOf(obj.getQuotationNo()));
        holder.txt_quot_status.setText(obj.getStatus().toUpperCase());

        if (obj.getStatus().equalsIgnoreCase("PENDING")) {
            holder.txt_quot_status.setTextColor(Color.parseColor("#484E00"));
        } else if (obj.getStatus().equalsIgnoreCase("INVOICE GENERATED")) {
            holder.txt_quot_status.setTextColor(Color.parseColor("#0042A0"));
        } else if (obj.getStatus().equalsIgnoreCase("EXPIRED")) {
            holder.txt_quot_status.setTextColor(Color.parseColor("#C02828"));
        }


        holder.txt_date_time.setText(ClsGlobal.getDDMYYYYAndTimeAM_And_PMFormat(obj.getQuotationDate()));

        holder.txt_valid_up_to.setText("VALIDITY: " + ClsGlobal.getDDMYYYYFormat(obj.getValidUptoDate()));

        holder.txt_cust_name.setText("NAME: " + obj.getCustomerName().toUpperCase()
                + " (" + obj.getMobileNo() + ")");

        holder.txt_bill_to.setText("CUSTOMER");
        holder.txt_net_amount.setText("NET AMOUNT:\u20B9 " + obj.getTotalAmount());
        holder.txt_discount.setText("DISCOUNT:\u20B9 " + ClsGlobal.round(obj.getDiscountAmount(), 2));
        holder.txt_grand_total.setText("TOTAL:\u20B9 " + ClsGlobal.round(obj.getGrandTotal(), 2));//

        if (obj.getApplyTax() != null) {
            if (obj.getApplyTax().equalsIgnoreCase("YES")) {
                holder.txt_tax_amount.setText("TAX AMOUNT:\u20B9 " + ClsGlobal.round(obj.getTotalTaxAmount(), 2));
            } else {
                holder.txt_tax_amount.setText("TAX AMOUNT:\u20B9 0.00");
            }
        }

        if (obj.getQuotationType() != null && !obj.getQuotationType().isEmpty()) {
            holder.txt_type.setText(obj.getQuotationType().toUpperCase());

            if (obj.getQuotationType().equalsIgnoreCase("Retail Quotation")) {
                holder.txt_type.setTextColor(Color.parseColor("#40932d"));
            } else {
                holder.txt_type.setTextColor(Color.parseColor("#B6861B"));
            }

        } else {
            holder.txt_type.setText("Retail Quotation");
            holder.txt_type.setTextColor(Color.parseColor("#40932d"));
        }
        holder.Bind(List.get(position), onItemClick);
    }

    @Override
    public int getItemCount() {
        return List.size();
    }


    public interface OnItemClick {
        void OnClick(ClsQuotationMaster clsQuotationMaster);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_quot_no, txt_date_time, txt_net_amount,
                txt_tax_amount, txt_type, txt_grand_total,
                txt_cust_name, txt_discount, txt_bill_to, txt_valid_up_to;

        TextView txt_quot_status;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_quot_no = itemView.findViewById(R.id.txt_quot_no);
            txt_date_time = itemView.findViewById(R.id.txt_date_time);
            txt_net_amount = itemView.findViewById(R.id.txt_net_amount);
            txt_tax_amount = itemView.findViewById(R.id.txt_tax_amount);
            txt_grand_total = itemView.findViewById(R.id.txt_grand_total);
            txt_discount = itemView.findViewById(R.id.txt_discount);
            txt_cust_name = itemView.findViewById(R.id.txt_cust_name);
            txt_bill_to = itemView.findViewById(R.id.txt_bill_to);
            txt_type = itemView.findViewById(R.id.txt_type);
            txt_valid_up_to = itemView.findViewById(R.id.txt_valid_up_to);
            txt_quot_status = itemView.findViewById(R.id.txt_quot_status);

        }

        void Bind(final ClsQuotationMaster clsQuotationMaster, OnItemClick onItemClick) {
            itemView.setOnClickListener(v ->
                    // send current position via Onclick method.
                    onItemClick.OnClick(clsQuotationMaster));
        }

    }


}
