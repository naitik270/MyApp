package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;

import com.demo.nspl.restaurantlite.classes.ClsInventoryItem;
import com.demo.nspl.restaurantlite.classes.ClsInventoryStock;
import com.demo.nspl.restaurantlite.R;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Desktop on 4/20/2018.
 */

public class RecentStockEntriesAdapter extends RecyclerView.Adapter<RecentStockEntriesAdapter.ViewHolder> {

    Context c;
    private View itemview;
    List<ClsInventoryStock> listStock= new ArrayList<ClsInventoryStock>();
    private SQLiteDatabase db;
    AppCompatActivity activity;
    List<ClsInventoryItem> items= new ArrayList<ClsInventoryItem>();
    List<String> previousMonthValue = new ArrayList<String>();
    List<String> previousMonthText = new ArrayList<String>();


    public RecentStockEntriesAdapter(AppCompatActivity ac, Context c, ArrayList<ClsInventoryStock> stockMasters) {
        this.c = c;
        this.listStock = stockMasters;
        this.activity = ac;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_recent_stockentries, parent, false);
        ViewHolder myViewHolder = new ViewHolder(itemview);
        return myViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClsInventoryStock objInventoryStock=new ClsInventoryStock();
        objInventoryStock=listStock.get(position);






        Formatter fmt = new Formatter();
        Calendar cal = Calendar.getInstance();
        fmt = new Formatter();
        fmt.format("%tb",  cal);
        Log.e("month", String.valueOf(fmt));

        PrettyTime p  = new PrettyTime();
        String datetime= p.format(ClsGlobal.getPreetyDateTimeFormat(objInventoryStock.getEntry_date()));
        holder.txt_item.setText(objInventoryStock.getInventory_item_name());
        holder.txt_qty.setText(" "+String.valueOf(objInventoryStock.getQty()));
        holder.txt_date.setText(datetime);
        holder.txt_amount.setText(" "+String.valueOf(objInventoryStock.getAmount()));
        holder.txt_srno.setText(String.valueOf(position+1).concat("."));

        if(objInventoryStock.getType().equalsIgnoreCase("In"))
        {
            holder.txt_qty.setTextColor(Color.parseColor("#3bb61f"));
            holder.txt_unit.setTextColor(Color.parseColor("#3bb61f"));
        }
        else if(objInventoryStock.getType().equalsIgnoreCase("Out"))
        {
            holder.txt_qty.setTextColor(Color.parseColor("#bf1d3e"));
            holder.txt_unit.setTextColor(Color.parseColor("#bf1d3e"));
        }
        holder.txt_unit.setText(" "+objInventoryStock.getUnitname());

    }

    @Override
    public int getItemCount() {
        return listStock.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_date,txt_qty,txt_amount,txt_item,txt_unit,txt_srno;
        public ViewHolder(View itemView) {
            super(itemView);
            txt_date =itemView.findViewById(R.id.txt_date);
            txt_amount =  itemView.findViewById(R.id.txt_amount);
            txt_item =  itemView.findViewById(R.id.txt_item);
            txt_qty =  itemView.findViewById(R.id.txt_qty);
            txt_unit =  itemView.findViewById(R.id.txt_unit);
            txt_srno =  itemView.findViewById(R.id.txt_srno);



        }
    }


}
