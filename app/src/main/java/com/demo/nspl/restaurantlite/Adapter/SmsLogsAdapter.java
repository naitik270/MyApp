package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsBulkSMSLog;

import java.util.ArrayList;
import java.util.List;

public class SmsLogsAdapter extends RecyclerView.Adapter<SmsLogsAdapter.MyViewHolder> {

    Context context;
    private LayoutInflater mInflater;
    List<ClsBulkSMSLog> list = new ArrayList<>();
    private OnClickListener listener;

    public SmsLogsAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public void AddItems(List<ClsBulkSMSLog> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void SetOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_sms_logs, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClsBulkSMSLog current = list.get(position);

        holder.txt_credit.setText(String.valueOf(current.getCreditCount()));

        holder.txt_customer.setText(current.getCustomerName().toUpperCase()
                .concat(" (").concat(current.getMobile()).concat(")"));

        if (current.getStatus() != null && current.getStatus().equalsIgnoreCase("DND")) {
            holder.txt_status.setTextColor(Color.parseColor("#B47A00"));
        } else if (current.getStatus() != null && current.getStatus().equalsIgnoreCase("Failed")) {
            holder.txt_status.setTextColor(Color.parseColor("#C02828"));
        } else if (current.getStatus() != null && current.getStatus().equalsIgnoreCase("Pending")) {
            holder.txt_status.setTextColor(Color.parseColor("#0042A0"));
        } else if (current.getStatus() != null && current.getStatus().equalsIgnoreCase("Delivered")) {
            holder.txt_status.setTextColor(Color.parseColor("#225A25"));
        } else if (current.getStatus() != null && current.getStatus().equalsIgnoreCase("Rejected")) {
            holder.txt_status.setTextColor(Color.parseColor("#601B8F"));
        } else if (current.getStatus() != null && current.getStatus().equalsIgnoreCase("Blocked")) {
            holder.txt_status.setTextColor(Color.parseColor("#0098ac"));
        } else if (current.getStatus() != null && current.getStatus().equalsIgnoreCase("Submit")) {
            holder.txt_status.setTextColor(Color.parseColor("#8E1F68"));
        }

        /*Submit
Pending
DND
Rejected
Blocked
Failed
Delivered*/

        holder.txt_status.setText(current.getStatus().toUpperCase());
        holder.txt_date_time.setText(String.valueOf(current.getStatusDateTime()));
        holder.txt_utilize_type.setText("Status Code: " + current.getStatusCode());

        if (current.getRemark() != null) {
            holder.remark.setText(current.getRemark().toUpperCase());
        } else {
            holder.remark.setText("");
        }

        holder.Bind(current, listener, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        ImageView imageView;
        TextView txt_status, txt_customer, txt_credit, remark, txt_utilize_type, txt_date_time;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header = itemView.findViewById(R.id.ll_header);
            txt_customer = itemView.findViewById(R.id.txt_customer);
            txt_credit = itemView.findViewById(R.id.txt_credit);
            txt_status = itemView.findViewById(R.id.txt_status);
            txt_date_time = itemView.findViewById(R.id.txt_date_time);
            txt_utilize_type = itemView.findViewById(R.id.txt_utilize_type);
            txt_utilize_type.setVisibility(View.INVISIBLE);
            remark = itemView.findViewById(R.id.remark);
            imageView = itemView.findViewById(R.id.imageView);
        }

        void Bind(final ClsBulkSMSLog obj, OnClickListener onItemClickListener, int position) {
            ll_header.setOnClickListener(v ->
                    // send current position via Onclick method.
                    onItemClickListener.OnItemClick(obj, position));
        }
    }


    public interface OnClickListener {
        void OnItemClick(ClsBulkSMSLog obj, int position);
    }

}
