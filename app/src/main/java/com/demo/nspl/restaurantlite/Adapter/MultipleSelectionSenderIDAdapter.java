package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsSMSBulkMaster;

import java.util.ArrayList;
import java.util.List;

public class MultipleSelectionSenderIDAdapter extends RecyclerView.Adapter<MultipleSelectionSenderIDAdapter.myViewHolder> {

    Context context;
    List<ClsSMSBulkMaster> mData;
    OnCharacterClick mOnCharacterClick;
    boolean isSelected[];
    public static List<String> selectedItems =new ArrayList<>();

    public MultipleSelectionSenderIDAdapter(Context context) {
        this.context = context;

    }

    public void AddItems(List<ClsSMSBulkMaster> mData){
        this.mData = mData;
        isSelected = new boolean[mData.size()];
        notifyDataSetChanged();
    }

    public void OnCharacterClick(OnCharacterClick mOnCharacterClick) {
        this.mOnCharacterClick = mOnCharacterClick;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.filter_list, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        ClsSMSBulkMaster obj = mData.get(position);
        holder.txt_label.setText(obj.getSenderID().toUpperCase());

        holder.txt_label.setChecked(obj.isSelected());


        if (holder.txt_label.isChecked()) {
            holder.linear_layout.setBackgroundColor(Color.parseColor("#FFCDD2D2"));
            holder.ic_Check.setColorFilter(ContextCompat.getColor(context,
                    R.color.red_dark), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else {
            holder.ic_Check.setColorFilter(ContextCompat.getColor(context,
                    R.color.tint_checkImageView), android.graphics.PorterDuff.Mode.MULTIPLY);
            holder.linear_layout.setBackgroundResource(0);
        }

        holder.Bind(obj, mOnCharacterClick, position, holder);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


    public interface OnCharacterClick {
        void OnClick(ClsSMSBulkMaster clsCustomerMaster, int position, myViewHolder holder);
    }


    public static class myViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout linear_layout;
        public CheckedTextView txt_label;
        public ImageView ic_Check;


        myViewHolder(View itemView) {
            super(itemView);
            linear_layout = itemView.findViewById(R.id.linear_layout);
            txt_label = itemView.findViewById(R.id.txt_label);
            ic_Check = itemView.findViewById(R.id.ic_Check);

        }

        void Bind(ClsSMSBulkMaster clsCustomerMaster,
                  OnCharacterClick mOnCharacterClick,
                  int position, myViewHolder holder) {

            linear_layout.setOnClickListener(v -> {
                mOnCharacterClick.OnClick(clsCustomerMaster, position, holder);

            });
        }
    }

    List<ClsSMSBulkMaster> selected;

    public List<ClsSMSBulkMaster> getSelected() {
        return selected;
    }

    public void updateList(List<ClsSMSBulkMaster> list) {
        mData = list;
        notifyDataSetChanged();
    }

}
