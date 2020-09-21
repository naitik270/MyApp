package com.demo.nspl.restaurantlite.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.CountryCode;

import java.util.ArrayList;
import java.util.List;

public class CountryCodeAdapter extends RecyclerView.Adapter
        <CountryCodeAdapter.MyViewHolder> {

    List<CountryCode> list = new ArrayList<>();
    OnItemClick onItemClick;


    public CountryCodeAdapter() {

    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void AddItem(List<CountryCode> list) {
        this.list = list;
        notifyDataSetChanged();
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
        CountryCode current = list.get(position);
        holder.textView_countryName.setText(current.getCountryName());
        holder.textView_code.setText(current.getCountryCode());
        holder.Bind(current, onItemClick, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout main_rl;
        TextView textView_countryName,textView_code;


        public MyViewHolder(View itemView) {
            super(itemView);

            main_rl = itemView.findViewById(R.id.main_rl);
            textView_countryName = itemView.findViewById(R.id.textView_countryName);
            textView_code = itemView.findViewById(R.id.textView_code);

        }

        void Bind(final CountryCode cls, OnItemClick mOnDeleteClick, int position) {
            main_rl.setOnClickListener(v ->
                    // send current position via Onclick method.
                    mOnDeleteClick.OnClick(cls, position));
        }

    }


    public interface OnItemClick {
        void OnClick(CountryCode countryCode, int position);
    }
}