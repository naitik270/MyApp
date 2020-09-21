package com.demo.nspl.restaurantlite.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.nspl.restaurantlite.activity.InventorySummaryActivity;
import com.demo.nspl.restaurantlite.classes.ClsInventoryStock;
import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Desktop on 3/19/2018.
 */

public class InventoryStockAdater extends RecyclerView.Adapter<InventoryStockAdater.Myviewholder> {
    Context c;
    private View itemview;
    List<ClsInventoryStock> listInventorystock= new ArrayList<ClsInventoryStock>();
    private SQLiteDatabase db;
    AppCompatActivity activity;

    public InventoryStockAdater(AppCompatActivity ac, Context c, ArrayList<ClsInventoryStock> inventorystockMasters) {
        this.c = c;
        this.listInventorystock = inventorystockMasters;
        this.activity = ac;
    }


    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_inventory_stock_master, parent, false);
        InventoryStockAdater.Myviewholder myViewHolder = new InventoryStockAdater.Myviewholder(itemview);
        return myViewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, final int position) {
        ClsInventoryStock ObjInventoryStock= new ClsInventoryStock();
        ObjInventoryStock= listInventorystock.get(position);



        Log.e("Qty", String.valueOf((ObjInventoryStock.getInqty()-ObjInventoryStock.getOutqty())));

        if((ObjInventoryStock.getInqty()-ObjInventoryStock.getOutqty()<ObjInventoryStock.getMin_qty()))
        {
            holder.txt_stock.setTextColor(Color.parseColor("#bf1d3e"));
            holder.unit_stock.setTextColor(Color.parseColor("#bf1d3e"));

        }
        else if((ObjInventoryStock.getInqty()-ObjInventoryStock.getOutqty()>ObjInventoryStock.getMax_qty()))
        {

            holder.txt_stock.setTextColor(Color.parseColor("#3bb61f"));
            holder.unit_stock.setTextColor(Color.parseColor("#3bb61f"));
        }




        holder.txt_stock.setText(String.valueOf((ObjInventoryStock.getInqty()-ObjInventoryStock.getOutqty())));
        holder.txt_itemname.setText(ObjInventoryStock.getInventory_item_name());
        holder.txt_In.setText(String.valueOf(ObjInventoryStock.getInqty()));
        holder.txt_Out.setText(String.valueOf(ObjInventoryStock.getOutqty()));
        holder.txt_Min.setText(String.valueOf(ObjInventoryStock.getMin_qty()));
        holder.txt_Max.setText(String.valueOf(ObjInventoryStock.getMax_qty()));

        holder.unit_min.setText(ObjInventoryStock.getUnitname());
        holder.unit_stock.setText(ObjInventoryStock.getUnitname());
        holder.unit_In.setText(ObjInventoryStock.getUnitname());
        holder.unit_max.setText(ObjInventoryStock.getUnitname());
        holder.unit_out.setText(ObjInventoryStock.getUnitname());
        holder.txt_srno.setText(String.valueOf(position+1).concat("."));


        final ClsInventoryStock finalObjInventoryStock = ObjInventoryStock;
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(c,InventorySummaryActivity.class);
                intent.putExtra("ItemId",String.valueOf(finalObjInventoryStock.getInventory_item_id()));
                intent.putExtra("ItemName", finalObjInventoryStock.getInventory_item_name());
                intent.putExtra("Unit", finalObjInventoryStock.getUnitname());
                c.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount()
    {
        return listInventorystock.size();
    }


    public class Myviewholder extends RecyclerView.ViewHolder {

        TextView txt_itemname,txt_stock,txt_In,txt_Out,txt_Max,txt_Min,unit_min,unit_stock,unit_In,unit_max,unit_out,txt_srno;
        LinearLayout linear;
        CardView cv;


        public Myviewholder(View itemView) {
            super(itemView);

            txt_itemname=itemView.findViewById(R.id.txt_itemname);
            txt_stock=itemView.findViewById(R.id.txt_stock);
            txt_In=itemView.findViewById(R.id.txt_In);
            txt_Out=itemView.findViewById(R.id.txt_Out);
            txt_Max=itemView.findViewById(R.id.txt_Max);
            txt_Min=itemView.findViewById(R.id.txt_Min);
            linear=itemView.findViewById(R.id.linear);
            unit_min=itemView.findViewById(R.id.unit_min);
            unit_stock=itemView.findViewById(R.id.unit_stock);
            unit_In=itemView.findViewById(R.id.unit_In);
            unit_max=itemView.findViewById(R.id.unit_max);
            unit_out=itemView.findViewById(R.id.unit_out);
            txt_srno=itemView.findViewById(R.id.txt_srno);
            cv=itemView.findViewById(R.id.cv);

        }
    }


}
