package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Interface.OnItemClickListenerClsOrder;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsInventoryOrderMaster;

import java.util.ArrayList;
import java.util.List;

public class PendingPaymentsAdapter extends RecyclerView.Adapter<PendingPaymentsAdapter.ViewHolder> {

    private Context mContext;
    private List<ClsInventoryOrderMaster> list = new ArrayList<>();
    private List<Integer> selectedIds = new ArrayList<>();
    OnItemClickListenerClsOrder mOnItemClickListenerClsOrder;

    public PendingPaymentsAdapter(Context context) {
        this.mContext = context;
    }

    public void AddItems(List<ClsInventoryOrderMaster> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void Clear(){
        this.list.clear();
        notifyDataSetChanged();
    }

    public ClsInventoryOrderMaster getItem(int position){
        return list.get(position);
    }

    public void setSelectedIds(List<Integer> selectedIds) {
        this.selectedIds = selectedIds;
        notifyDataSetChanged();
    }

    public List<ClsInventoryOrderMaster> getAdapterList(){
        return list;
    }

    public void SetOnClickListener(OnItemClickListenerClsOrder onItemClickListenerClsOrder) {
        this.mOnItemClickListenerClsOrder = onItemClickListenerClsOrder;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_pending_payment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClsInventoryOrderMaster current = list.get(position);

//        if (selectedIds.contains(current.getOrderID())){
//            holder.ll_main.setBackgroundColor(ContextCompat.getColor(mContext, R.color.grey_40));
//
//        }else {
//            holder.ll_main.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
//        }


        if (current.isSelected()){
            holder.ll_main.setBackgroundColor(ContextCompat.getColor(mContext, R.color.grey_40));
        }else {
            holder.ll_main.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        }

        holder.txt_Due_Date.setText("Due Date: " +current.getDueDate());
        holder.txt_Order_No.setText(current.getOrderNo());
        holder.txt_CustomerName.setText("Customer Name: " + current.getCustomerName());
        holder.txt_CompanyName.setText("Company Name: " +current.getCompanyName());
        holder.txt_mobile_no.setText(current.getMobileNo());
        holder.txt_Amount.setText("AMOUNT: " + current.getAdjumentAmount());
        holder.txt_gst_no.setText(String.valueOf(current.getGSTNo()));
//        holder.Bind(current,mOnItemClickListenerClsOrder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_main;
        private TextView txt_Due_Date, txt_Order_No,
                txt_CustomerName, txt_CompanyName, txt_mobile_no, txt_Amount, txt_gst_no;

        public ViewHolder(View itemView) {
            super(itemView);
            ll_main = itemView.findViewById(R.id.ll_main);
            txt_Due_Date = itemView.findViewById(R.id.txt_Due_Date);
            txt_Order_No = itemView.findViewById(R.id.txt_Order_No);
            txt_CustomerName = itemView.findViewById(R.id.txt_CustomerName);
            txt_CompanyName = itemView.findViewById(R.id.txt_CompanyName);
            txt_mobile_no = itemView.findViewById(R.id.txt_mobile_no);
            txt_Amount = itemView.findViewById(R.id.txt_Amount);
            txt_gst_no = itemView.findViewById(R.id.txt_gst_no);

        }

        void Bind(final ClsInventoryOrderMaster clsInventoryOrderMaster, OnItemClickListenerClsOrder onItemClickListener) {
            ll_main.setOnClickListener(v ->
                    // send current position via Onclick method.
                    onItemClickListener.OnClick(clsInventoryOrderMaster));
        }

    }


}
