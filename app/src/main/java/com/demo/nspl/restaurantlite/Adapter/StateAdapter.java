package com.demo.nspl.restaurantlite.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsStateMaster;

import java.util.ArrayList;
import java.util.List;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.MyViewHolder> {

    List<ClsStateMaster> list = new ArrayList<>();
    OnItemClick onItemClick;

    public void AddItem(List<ClsStateMaster> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void Clear() {
        this.list.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_recycler_country_tile, parent, false);
        return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClsStateMaster current = list.get(position);
        holder.textView_StateName.setText(current.getName());
        holder.textView_code.setText("");
        holder.Bind(current,onItemClick);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout main_rl;
        TextView textView_StateName,textView_code;


        public MyViewHolder(View itemView) {
            super(itemView);

            main_rl = itemView.findViewById(R.id.main_rl);
            textView_StateName = itemView.findViewById(R.id.textView_countryName);
            textView_code = itemView.findViewById(R.id.textView_code);

        }

        void Bind(final ClsStateMaster cls, OnItemClick mOnDeleteClick) {
            main_rl.setOnClickListener(v ->
                    // send current position via Onclick method.
                    mOnDeleteClick.OnClick(cls));
        }

    }

    public interface OnItemClick {
        void OnClick(ClsStateMaster clsStateMaster);
    }
}
