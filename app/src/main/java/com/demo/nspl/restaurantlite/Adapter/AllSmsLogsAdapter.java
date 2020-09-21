package com.demo.nspl.restaurantlite.Adapter;

import android.annotation.SuppressLint;
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

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsSMSLogs;

import java.util.ArrayList;
import java.util.List;

public class AllSmsLogsAdapter extends RecyclerView.Adapter<AllSmsLogsAdapter.MyViewHolder> {


    View itemView;
    List<ClsSMSLogs> list = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;
    OnClickListener onClickListener;

    public AllSmsLogsAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }


    public void AddItems(List<ClsSMSLogs> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    public void SetOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = mInflater.inflate(R.layout.sms_logs_items, parent, false);
        return new MyViewHolder(itemView);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClsSMSLogs current = list.get(position);

        holder.txt_customer.setText(current.getCustomer_Name().toUpperCase()
                .concat(" (").concat(current.getMobileNo()).concat(")"));

        holder.txt_credit.setText(String.valueOf(current.getCredit()));


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
        } else if (current.getStatus().equalsIgnoreCase("Time Expired")) {
            holder.txt_status.setTextColor(Color.parseColor("#9F1215"));
        } else if (current.getStatus().equalsIgnoreCase("Sending")) {
            holder.txt_status.setTextColor(Color.parseColor("#8E1F68"));
        } else {
            holder.txt_status.setTextColor(Color.parseColor("#000000"));
        }

        holder.txt_status.setText(current.getStatus().toUpperCase());

        if (current.getUtilizeType() != null) {
            holder.txt_utilize_type.setText(current.getUtilizeType().toUpperCase());
        } else {
            holder.txt_utilize_type.setText("");
        }

        holder.txt_date_time.setText(ClsGlobal.getEntryDateFormat(current.getEntry_Datetime()));

        if (current.getRemark() != null) {
            holder.remark.setText(current.getRemark().toUpperCase());
        } else {
            holder.remark.setText("");
        }

        holder.remark.setText(current.getRemark().toUpperCase());
        holder.Bind(current, onClickListener, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView view;

        LinearLayout ll_header;
        TextView txt_status, txt_customer, txt_credit, remark,
                txt_utilize_type, txt_date_time;

        public MyViewHolder(View itemView) {
            super(itemView);

            view = itemView.findViewById(R.id.view);
            txt_status = itemView.findViewById(R.id.txt_status);
            txt_customer = itemView.findViewById(R.id.txt_customer);
            txt_credit = itemView.findViewById(R.id.txt_credit);
            remark = itemView.findViewById(R.id.remark);
            txt_utilize_type = itemView.findViewById(R.id.txt_utilize_type);
            txt_date_time = itemView.findViewById(R.id.txt_date_time);
            ll_header = itemView.findViewById(R.id.ll_header);
        }

        void Bind(ClsSMSLogs obj, OnClickListener onClickListener, int position) {
            ll_header.setOnClickListener(v -> {
                onClickListener.OnItemClick(obj, position);
            });
        }
    }
    public interface OnClickListener {
        void OnItemClick(ClsSMSLogs obj, int position);
    }
}
