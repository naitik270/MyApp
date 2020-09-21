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
import com.demo.nspl.restaurantlite.SMS.ClsMessageFormat;

import java.util.ArrayList;
import java.util.List;

public class SmsFormatTitleAdapter extends RecyclerView.Adapter<SmsFormatTitleAdapter.ViewHolder>  {

    private LayoutInflater mInflater;
    List<ClsMessageFormat> list = new ArrayList<>();
    OnClickListener mOnClickListener;
    Context context;

    public SmsFormatTitleAdapter(Context context){
        this.context =context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void AddItems(List<ClsMessageFormat> list) {
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
        ClsMessageFormat current = list.get(position);
        holder.item_text.setText(current.getTitle());
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

        void Bind(final ClsMessageFormat obj, OnClickListener OnClickListener, int position) {
            linear_layout.setOnClickListener(v ->
                    // send current position via Onclick method.
                    OnClickListener.OnItemClick(obj,position));
        }

    }

    public interface OnClickListener {
        void OnItemClick(ClsMessageFormat obj,int position);
    }

}
