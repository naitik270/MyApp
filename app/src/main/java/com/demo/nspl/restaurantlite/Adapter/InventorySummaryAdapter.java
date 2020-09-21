package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.nspl.restaurantlite.classes.ClsInventoryStock;
import com.demo.nspl.restaurantlite.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Desktop on 4/12/2018.
 */

public class InventorySummaryAdapter extends RecyclerView.Adapter<InventorySummaryAdapter.ViewHolder> {

    Context c;
    private View itemview;
    List<ClsInventoryStock> listStock= new ArrayList<ClsInventoryStock>();
    private SQLiteDatabase db;
    AppCompatActivity activity;
    private String formattedDate;

    public InventorySummaryAdapter(AppCompatActivity ac, Context c, ArrayList<ClsInventoryStock> stockMasters) {
        this.c = c;
        this.listStock = stockMasters;
        this.activity = ac;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_invenrorysummary, parent, false);
        ViewHolder myViewHolder = new ViewHolder(itemview);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ClsInventoryStock objInventoryStock=new ClsInventoryStock();
        objInventoryStock=listStock.get(position);


//        String headerText = "" + objInventoryStock.getTrasaction_date().subSequence(0, 1).charAt(0);
//        holder.txt_date.setText(headerText);
        Log.e("VENDORNAME","--->>>"+objInventoryStock.getVendor_name() );

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = df.format(c.getTime());
        holder.txt_date.setText(objInventoryStock.getTrasaction_date());
        holder.txt_amount.setText(" "+String.valueOf(objInventoryStock.getAmount()));
        holder.txt_srno.setText(String.valueOf(position+1).concat("."));

        if(objInventoryStock.getVendor_name()!=null)
        {
            holder.txt_vendor_name.setText(objInventoryStock.getVendor_name());

        }
        else {
            holder.txt_vendor_name.setText(" - ");



        }

        Log.e("Name"," "+objInventoryStock.getVendor_name());

        if(objInventoryStock.getType().equalsIgnoreCase("In"))
        {
            holder.txt_qty.setTextColor(Color.parseColor("#3bb61f"));
        }
        else if(objInventoryStock.getType().equalsIgnoreCase("Out"))
        {
            holder.txt_qty.setTextColor(Color.parseColor("#bf1d3e"));
        }
        holder.txt_remark.setText(objInventoryStock.getRemark());
        holder.txt_qty.setText(String.valueOf(objInventoryStock.getQty()));
        holder.txt_unit.setText(String.valueOf(objInventoryStock.getUnitname()));


    }

    @Override
    public int getItemCount() {
        return listStock.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_date,txt_vendor_name,txt_amount,txt_qty,txt_remark,txt_unit,txt_srno;

        ViewHolder(View itemView) {
            super(itemView);

            txt_date =itemView.findViewById(R.id.txt_date);
            txt_vendor_name =  itemView.findViewById(R.id.txt_vendor_name);
            txt_amount =  itemView.findViewById(R.id.txt_amount);
            txt_qty =  itemView.findViewById(R.id.txt_qty);
            txt_remark =  itemView.findViewById(R.id.txt_remark);
            txt_unit =  itemView.findViewById(R.id.txt_unit);
            txt_srno =  itemView.findViewById(R.id.txt_srno);
        }
    }
}
