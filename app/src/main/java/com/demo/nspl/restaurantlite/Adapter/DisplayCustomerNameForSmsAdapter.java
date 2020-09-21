package com.demo.nspl.restaurantlite.Adapter;

import android.annotation.SuppressLint;
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
import com.demo.nspl.restaurantlite.SMS.ClsSmsCustomerGroup;

import java.util.ArrayList;
import java.util.List;


public class DisplayCustomerNameForSmsAdapter extends RecyclerView.Adapter<DisplayCustomerNameForSmsAdapter.MyViewHolder> {

    private List<ClsSmsCustomerGroup> data = new ArrayList<>();
    Context context;
    View itemView;
    private LayoutInflater inflater = null;

    public DisplayCustomerNameForSmsAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void AddItems(List<ClsSmsCustomerGroup> item) {
        this.data = item;
        notifyDataSetChanged();
    }


    public void remove(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_customer_name, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ClsSmsCustomerGroup objClsRoomMaster = data.get(position);

        holder.empty_title_text.setText(objClsRoomMaster.getMobileNo() +
                " - " + objClsRoomMaster.getCustomerName().toUpperCase());

        holder.txt_cust_number.setText(objClsRoomMaster.getMobileNo());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_layout;
        TextView txt_cust_number, empty_title_text, txt_cust_id;
        ImageView iv_delete;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_layout = itemView.findViewById(R.id.ll_layout);
            txt_cust_id = itemView.findViewById(R.id.txt_cust_id);
            empty_title_text = itemView.findViewById(R.id.empty_title_text);
            txt_cust_number = itemView.findViewById(R.id.txt_cust_number);
            iv_delete = itemView.findViewById(R.id.iv_delete);

        }

    }




}



