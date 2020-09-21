package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsInventoryStock;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Desktop on 5/1/2018.
 */

public class SummaryStockAdapter extends RecyclerView.Adapter<SummaryStockAdapter.MyViewHolder> {
    Context c;
    private View itemview;
    List<ClsInventoryStock> list_summary= new ArrayList<ClsInventoryStock>();
    private SQLiteDatabase db;
    AppCompatActivity activity;


    public SummaryStockAdapter(AppCompatActivity ac, Context c, ArrayList<ClsInventoryStock> stockMasters) {
        this.c = c;
        this.list_summary = stockMasters;
        this.activity = ac;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_summarystock, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemview);
        return myViewHolder;
    }




    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClsInventoryStock objInventoryStock=new ClsInventoryStock();
        objInventoryStock=list_summary.get(position);

        holder.txt_srno.setText(String.valueOf(position+1).concat("."));
//        holder.txt_instock.setText(objInventoryStock.getUnitname());
//        holder.txt_outstock.setText(objInventoryStock.getUnitname());
        holder.txt_instock.setText(String.valueOf(objInventoryStock.getInqty()).concat(" ").concat(objInventoryStock.getUnitname().toLowerCase()));
        holder.txt_outstock.setText(String.valueOf(objInventoryStock.getOutqty()).concat(" ").concat(objInventoryStock.getUnitname().toLowerCase()));
        holder.txt_item.setText(objInventoryStock.getInventory_item_name());



    }

    @Override
    public int getItemCount() {
        return list_summary.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_srno,txt_item,txt_instock,txt_outstock,txt_in_unit,txt_out_unit;
        public MyViewHolder(View itemView) {
            super(itemView);
            txt_srno=itemView.findViewById(R.id.txt_srno);
            txt_item=itemView.findViewById(R.id.txt_item);
            txt_instock=itemView.findViewById(R.id.txt_instock);
            txt_outstock=itemView.findViewById(R.id.txt_outstock);
        }
    }
}
