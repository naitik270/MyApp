package com.demo.nspl.restaurantlite.Adapter;

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
import com.demo.nspl.restaurantlite.classes.ClsSMSBulkMaster;

import java.util.ArrayList;
import java.util.List;

public class SendSmsReportAdapter extends RecyclerView.Adapter<SendSmsReportAdapter.MyViewHolder> {

    Context context;
    private LayoutInflater mInflater;
    List<ClsSMSBulkMaster> list = new ArrayList<>();
    private OnClickListener listener;

    public SendSmsReportAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void AddItems(List<ClsSMSBulkMaster> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void SetOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_send_sms_reports, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClsSMSBulkMaster current = list.get(position);

        holder.txt_title.setText(current.getTitle().toUpperCase());

        holder.txt_bulk_ib.setText(String.valueOf(current.getBulkID()));
        holder.txt_message_type.setText(current.getMessageType().toUpperCase());

        holder.txt_groupName.setText(current.getGroupName().toUpperCase());
        holder.txt_total_customer.setText(String.valueOf(current.getTotalCustomers()));
        holder.txt_sender_id.setText(current.getSenderID());

        holder.txt_date_time.setText(ClsGlobal.getDDMYYYYAndTimeAM_And_PMFormat(current.getSendDate()));

        holder.txt_total_credit_utilized.setText(String.valueOf(current.getTotalCredit_Utilized()));

        holder.Bind(current, listener, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView txt_title, txt_total_credit_utilized, txt_bulk_ib, txt_message_type, txt_groupName, txt_total_customer, txt_sender_id, txt_date_time;


        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header = itemView.findViewById(R.id.ll_header);
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_bulk_ib = itemView.findViewById(R.id.txt_bulk_ib);
            txt_message_type = itemView.findViewById(R.id.txt_message_type);
            txt_groupName = itemView.findViewById(R.id.txt_groupName);
            txt_total_customer = itemView.findViewById(R.id.txt_total_customer);
            txt_sender_id = itemView.findViewById(R.id.txt_sender_id);
            txt_date_time = itemView.findViewById(R.id.txt_date_time);
            txt_total_credit_utilized = itemView.findViewById(R.id.txt_total_credit_utilized);

        }

        void Bind(final ClsSMSBulkMaster obj, OnClickListener onItemClickListener, int position) {
            ll_header.setOnClickListener(v ->
                    // send current position via Onclick method.
                    onItemClickListener.OnItemClick(obj, position));
        }
    }


    public interface OnClickListener {
        void OnItemClick(ClsSMSBulkMaster obj, int position);
    }

}
