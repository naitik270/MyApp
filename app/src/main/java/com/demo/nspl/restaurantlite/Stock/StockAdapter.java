package com.demo.nspl.restaurantlite.Stock;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.MyViewHolder> {


    Context context;
    private View itemview;
    List<ClsStock> data = new ArrayList<>();
    OnStockClick onStockClick;


    StockAdapter(Context context) {
        this.context = context;

    }

    void AddItems(List<ClsStock> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void SetOnClickListener(OnStockClick onStockClick) {
        this.onStockClick = onStockClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_stock_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemview);
        return myViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClsStock current = data.get(position);
        holder.txt_stock_name.setText(current.getITEM_NAME().toUpperCase());
        holder.txt_item_code.setText("CODE: " + current.getITEM_CODE().toUpperCase());
        holder.txt_in.setText("IN: " + ClsGlobal.round(current.getIN(), 2));
        holder.txt_out.setText("OUT: " + ClsGlobal.round(current.getOUT(), 2));
        holder.txt_unit.setText("UNIT: " + current.getUNIT_CODE());

        String details = "";
        if (current.getTagList() != null && current.getTagList().size() != 0) {
            details = TextUtils.join(", ", current.getTagList());
            holder.txt_details.setText(details);
        } else {
            holder.hs_view.setVisibility(View.GONE);
        }

        holder.txt_opening_stock.setText("OPENING: " + ClsGlobal.round(current.getOPENING_STOCK(), 2));
        holder.txt_avg_purchase.setText("AVG.PURCHASE: \u20B9 " + ClsGlobal.round(current.getAveragePurchaseRate(), 2));
        holder.txt_avg_sale.setText("AVG.SALE: \u20B9 " + ClsGlobal.round(current.getAverageSaleRate(), 2));
        Double _balance = (current.getIN() + current.getOPENING_STOCK()) - current.getOUT();


        holder.txt_current.setText("STOCK: " + ClsGlobal.round(_balance, 2));

        if (_balance < current.getMIN_STOCK() || _balance < 0) {
            holder.txt_current.setTextColor(Color.parseColor("#c40000"));
        } else {
            holder.txt_current.setTextColor(Color.parseColor("#225A25"));
        }

        holder.Bind(current, onStockClick, position);
    }


    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView txt_stock_name, txt_in, txt_out, txt_current,
                txt_unit, txt_avg_sale, txt_avg_purchase, txt_opening_stock, txt_item_code, txt_details;

        HorizontalScrollView hs_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header = itemView.findViewById(R.id.ll_header);
            txt_stock_name = itemView.findViewById(R.id.txt_stock_name);
            txt_in = itemView.findViewById(R.id.txt_in);
            txt_out = itemView.findViewById(R.id.txt_out);
            txt_current = itemView.findViewById(R.id.txt_current);
            txt_unit = itemView.findViewById(R.id.txt_unit);
            txt_avg_sale = itemView.findViewById(R.id.txt_avg_sale);
            txt_avg_purchase = itemView.findViewById(R.id.txt_avg_purchase);
            txt_opening_stock = itemView.findViewById(R.id.txt_opening_stock);
            txt_item_code = itemView.findViewById(R.id.txt_item_code);
            txt_details = itemView.findViewById(R.id.txt_details);
            hs_view = itemView.findViewById(R.id.hs_view);
        }

        void Bind(ClsStock clsStock, OnStockClick onStockClick, int position) {
            ll_header.setOnClickListener(v -> onStockClick.OnClick(clsStock, position));
        }
    }
}
