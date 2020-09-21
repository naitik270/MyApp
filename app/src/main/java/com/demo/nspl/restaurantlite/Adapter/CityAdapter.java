package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsCityMaster;

import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyViewHolder> {

    Context context;
    List<ClsCityMaster> list = new ArrayList<>();
    private OnClickListener listener;

    public CityAdapter(Context context) {
        this.context = context;
    }

    public void SetOnClickListener(OnClickListener onClickListener) {
        this.listener = onClickListener;
    }

    public void Clear(){
        this.list.clear();
        notifyDataSetChanged();
    }

    public void AddItems(List<ClsCityMaster> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void remove(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent,
                false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClsCityMaster current = list.get(position);
        holder.tv_city_name.setText(current.getCityName());

        holder.Bind(current,listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView tv_city_name;
        ImageView delete, edit;

        public MyViewHolder(View itemView) {
            super(itemView);

            ll_header = itemView.findViewById(R.id.ll_header);
            tv_city_name = itemView.findViewById(R.id.tv_city_name);

            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);

        }

        void Bind(ClsCityMaster clsCityMaster, OnClickListener onClickListener) {

            delete.setOnClickListener(v -> {
                onClickListener.OnClick(clsCityMaster, "delete Click");
            });

            edit.setOnClickListener(v -> {
                onClickListener.OnClick(clsCityMaster, "edit Click");
            });

        }
    }


    public interface OnClickListener {
        void OnClick(ClsCityMaster clsCityMaster, String mode);
    }
}
