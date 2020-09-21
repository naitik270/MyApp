package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.SMS.ClsSmsIdSetting;

import java.util.ArrayList;
import java.util.List;

public class SenderIdAdapter extends RecyclerView.Adapter<SenderIdAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    List<ClsSmsIdSetting> list = new ArrayList<>();
    OnClickListener mOnClickListener;
    Context context;

    public SenderIdAdapter(Context context){
        this.context =context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void AddItems(List<ClsSmsIdSetting> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void SetOnClickListener(OnClickListener onClickListener){
        this.mOnClickListener = onClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClsSmsIdSetting current = list.get(position);
        holder.item_text.setText(current.getSms_id());
        holder.Bind(current,mOnClickListener,position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView item_text;
        private LinearLayout linear_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            linear_layout = itemView.findViewById(R.id.linear_layout);
            item_text = itemView.findViewById(R.id.item_text);

        }

        void Bind(final ClsSmsIdSetting obj, OnClickListener OnClickListener, int position) {
            linear_layout.setOnClickListener(v ->
                    // send current position via Onclick method.
                    OnClickListener.OnItemClick(obj,position));
        }

    }

    public interface OnClickListener {
        void OnItemClick(ClsSmsIdSetting obj,int position);
    }


}
