package com.demo.nspl.restaurantlite.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsBulkSMSLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SmsPreviewAdapter extends RecyclerView.Adapter<SmsPreviewAdapter.MyViewHolder> {

    View itemView;
    List<ClsBulkSMSLog> list = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;
    OnClickListener onClickListener;

    public SmsPreviewAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        selected_items = new SparseBooleanArray();
    }




    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public void AddItems(List<ClsBulkSMSLog> list) {
        Log.d("--Fill--", "AddItems");
        this.list = list;
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selected_items.size());
        for (int i = 0; i < selected_items.size(); i++) {
            items.add(selected_items.keyAt(i));
        }
        return items;
    }

    public void SetOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private int current_selected_idx = -1;
    private SparseBooleanArray selected_items;

    public int getSelectedItemCount() {
        return selected_items.size();
    }

    private void toggleCheckedIcon(MyViewHolder holder, int position) {

        if (selected_items.get(position, false)) {
            holder.lyt_image.setVisibility(View.GONE);
            holder.lyt_checked.setVisibility(View.VISIBLE);
            if (current_selected_idx == position) resetCurrentIndex();
        } else {
            holder.lyt_checked.setVisibility(View.GONE);
            holder.lyt_image.setVisibility(View.VISIBLE);
            if (current_selected_idx == position) resetCurrentIndex();
        }
    }

    public void toggleSelection(int pos) {
        current_selected_idx = pos;
        if (selected_items.get(pos, false)) {
            selected_items.delete(pos);
        } else {
            selected_items.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    private void displayImage(MyViewHolder holder, ClsBulkSMSLog inbox) {
        if (inbox.getMobile() != null) {
//            holder.iv_img.setColorFilter(null);
            holder.iv_img.setVisibility(View.VISIBLE);
        } else {
//            holder.iv_img.setImageResource(R.drawable.shape_circle);
//            holder.iv_img.setColorFilter(inbox.color);
            holder.iv_img.setVisibility(View.GONE);
        }
    }


    public ClsBulkSMSLog getItem(int position) {
        return list.get(position);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = mInflater.inflate(R.layout.item_sms_preview, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClsBulkSMSLog current = list.get(position);

        holder.txt_cust_name.setText("(" + current.getMobile() + ") ".concat(current.getCustomerName()));

        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
//        view.setBackgroundColor(randomAndroidColor);

        holder.iv_img.setColorFilter(randomAndroidColor);
        holder.txt_msg_format.setText(current.getMessage());
        holder.txt_credit_count.setText("Credit used (" + current.getCreditCount() + ") ");

        holder.ll_header.setActivated(selected_items.get(position, false));
//
        holder.ll_header.setOnClickListener(v -> {
            if (onClickListener == null) return;
            onClickListener.OnItemClick(current, position);
        });


        toggleCheckedIcon(holder, position);
        displayImage(holder, current);


//
//        holder.Bind(current, onClickListener, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void clearSelections() {
        selected_items.clear();
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView txt_msg_format;
        TextView txt_cust_name;
        TextView txt_credit_count;
        ImageView iv_img;

        RelativeLayout lyt_checked;
        RelativeLayout lyt_image;


        public MyViewHolder(View itemView) {
            super(itemView);

            txt_msg_format = itemView.findViewById(R.id.txt_msg_format);
            ll_header = itemView.findViewById(R.id.ll_header);
            txt_cust_name = itemView.findViewById(R.id.txt_cust_name);
            iv_img = itemView.findViewById(R.id.iv_img);
            txt_credit_count = itemView.findViewById(R.id.txt_credit_count);

            lyt_checked = itemView.findViewById(R.id.lyt_checked);
            lyt_image = itemView.findViewById(R.id.lyt_image);
        }

        void Bind(ClsBulkSMSLog obj, OnClickListener onClickListener, int position) {
            ll_header.setOnClickListener(v -> {
                onClickListener.OnItemClick(obj, position);
            });
        }
    }

    public interface OnClickListener {
        void OnItemClick(ClsBulkSMSLog obj, int position);
    }


    private void resetCurrentIndex() {
        current_selected_idx = -1;
    }


    public void removeData(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        notifyDataSetChanged();

        resetCurrentIndex();
    }


    public void UpdateData(ClsBulkSMSLog clsBulkSMSLog,int position){

        list.remove(position);
        list.add(clsBulkSMSLog);
        Log.d("--msg--", "Adapter: "+clsBulkSMSLog.getMessage());

        notifyItemChanged(position);
        notifyDataSetChanged();
    }

}
