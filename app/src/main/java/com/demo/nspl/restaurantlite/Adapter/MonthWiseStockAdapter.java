package com.demo.nspl.restaurantlite.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;

import com.demo.nspl.restaurantlite.classes.ClsInventoryStock;
import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Desktop on 4/21/2018.
 */

public class MonthWiseStockAdapter extends RecyclerView.Adapter<MonthWiseStockAdapter.ViewHolder> {

    Context c;
    private View itemview;
    List<ClsInventoryStock> listStock = new ArrayList<ClsInventoryStock>();
    private SQLiteDatabase db;
    AppCompatActivity activity;


    public MonthWiseStockAdapter(AppCompatActivity ac, Context c, ArrayList<ClsInventoryStock> stockMasters) {
        this.c = c;
        this.listStock = stockMasters;
        this.activity = ac;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_monthwise_stock, parent, false);
        MonthWiseStockAdapter.ViewHolder myViewHolder = new MonthWiseStockAdapter.ViewHolder(itemview);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClsInventoryStock objInventoryStock=new ClsInventoryStock();
        objInventoryStock=listStock.get(position);

        holder.txt_srno.setText(String.valueOf(position+1).concat(". "));
        holder.txt_date.setText(ClsGlobal.getChangeDateFormat(objInventoryStock.getTrasaction_date()));
        holder.txt_amount.setText(" "+String.valueOf(objInventoryStock.getAmount()));
        holder.txt_unit.setText(objInventoryStock.getUnitname());
        Log.e("Unitname","-----"+objInventoryStock.getUnitname());

        if(objInventoryStock.getVendor_name()!=null)
        {
            holder.txt_vendor_name.setText(objInventoryStock.getVendor_name());

        }
        else {
            holder.txt_vendor_name.setText(" - ");

        }

//        if(objInventoryStock.getRemark()!=null || objInventoryStock.getRemark().equals(""))
//        {
            holder.txt_remark.setText(objInventoryStock.getRemark());
//        }
//        else
//        {
//            holder.txt_remark.setText(" - ");
//        }

        Log.e("Name"," "+objInventoryStock.getRemark());

        if(objInventoryStock.getType().equalsIgnoreCase("In"))
        {
            holder.txt_qty.setTextColor(Color.parseColor("#3bb61f"));
        }
        else if(objInventoryStock.getType().equalsIgnoreCase("Out"))
        {
            holder.txt_qty.setTextColor(Color.parseColor("#bf1d3e"));
        }

        holder.txt_item.setText(objInventoryStock.getInventory_item_name());
        holder.txt_qty.setText(String.valueOf(objInventoryStock.getQty()));
        holder.linear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(c);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog);
                dialog.setCancelable(true);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);

                dialog.show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return listStock.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_srno,txt_date,txt_amount,txt_vendor_name,txt_qty,txt_unit,txt_remark,txt_item;
        LinearLayout linear;
        public ViewHolder(View itemView) {
            super(itemView);
            txt_srno=itemView.findViewById(R.id.txt_srno);
            txt_date=itemView.findViewById(R.id.txt_date);
            txt_amount=itemView.findViewById(R.id.txt_amount);
            txt_vendor_name=itemView.findViewById(R.id.txt_vendor_name);
            txt_qty=itemView.findViewById(R.id.txt_qty);
            txt_unit=itemView.findViewById(R.id.txt_unit);
            txt_remark=itemView.findViewById(R.id.txt_remark);
            txt_item=itemView.findViewById(R.id.txt_item);
            linear=itemView.findViewById(R.id.linear);
        }

    }
}
