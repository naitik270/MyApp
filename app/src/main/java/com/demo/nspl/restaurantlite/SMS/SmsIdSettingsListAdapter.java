package com.demo.nspl.restaurantlite.SMS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

public class SmsIdSettingsListAdapter extends RecyclerView.Adapter<SmsIdSettingsListAdapter.MyViewHolder> {

    private List<ClsSmsIdSetting> data = new ArrayList<>();
    Context context;
    View itemView;
    OnItemClickListener onItemClickListener;

    public SmsIdSettingsListAdapter(Context context) {
        this.context = context;
    }

    public void AddItems(List<ClsSmsIdSetting> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void SetOnClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void remove(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sms_id, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    public interface OnItemClickListener {
        void OnClick(ClsSmsIdSetting clsSmsIdSetting, int position);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClsSmsIdSetting obj = data.get(position);

        String _IndexVal = String.valueOf(position + 1).concat(".");
        holder.txt_sr_no.setText(_IndexVal);

        holder.txt_sender_id.setText(obj.getSms_id());
        holder.txt_default.setText("DEFAULT: " + obj.getDefault_sms());
        holder.txt_active.setText("ACTIVE: " + obj.getActive());
        holder.txt_remark.setText("REMARK: " + obj.getRemark());
        holder.Bind(data.get(position), onItemClickListener, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_sender_id, txt_default, txt_active, txt_sr_no, txt_remark;
        CardView card_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            card_view = itemView.findViewById(R.id.card_view);
            txt_sr_no = itemView.findViewById(R.id.txt_sr_no);
            txt_sender_id = itemView.findViewById(R.id.txt_sender_id);
            txt_active = itemView.findViewById(R.id.txt_active);
            txt_default = itemView.findViewById(R.id.txt_default);
            txt_remark = itemView.findViewById(R.id.txt_remark);
        }

        void Bind(final ClsSmsIdSetting clsSmsIdSetting,
                  OnItemClickListener onItemClickListener, int position) {
            card_view.setOnClickListener(v ->
                    // send current position via Onclick method.
                    onItemClickListener.OnClick(clsSmsIdSetting, position));
        }
    }


}
