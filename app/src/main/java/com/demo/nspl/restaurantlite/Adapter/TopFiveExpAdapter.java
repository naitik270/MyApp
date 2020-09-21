package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.nspl.restaurantlite.classes.ClsExpenseMasterNew;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desktop on 5/7/2018.
 */

public class TopFiveExpAdapter extends RecyclerView.Adapter<TopFiveExpAdapter.MyViewHolder> {

    View itemView;
    Context context;
    List<ClsExpenseMasterNew> data = new ArrayList<ClsExpenseMasterNew>();
    private LayoutInflater inflater = null;


    public TopFiveExpAdapter(Context context, List<ClsExpenseMasterNew> data) {
        super();
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_top_five_exp, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ClsExpenseMasterNew objClsExpenseMasterNew = data.get(position);


        String _IndexVal = String.valueOf(position + 1).concat(".");
        holder.txt_sr_no.setText(_IndexVal);

        holder.txt_exp_name.setText(objClsExpenseMasterNew.getExpense_type_name());
        holder.txt_total_exp.setText("\u20B9 ".concat(String.valueOf(ClsGlobal.round(objClsExpenseMasterNew.getGRAND_TOTAL(), 2))));


        PrettyTime p = new PrettyTime();
        String _EntryDateAction = p.format(ClsGlobal.getPreetyDateTimeFormat(objClsExpenseMasterNew.getEntry_date()));
        Log.e("_EntryDateAction", "_EntryDateAction" + _EntryDateAction);

        holder.txt_last_update.setText(_EntryDateAction);

    }

    @Override
    public int getItemCount() {
        return data.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView txt_sr_no, txt_exp_name, txt_last_update, txt_total_exp;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header = itemView.findViewById(R.id.ll_header);
            txt_last_update = itemView.findViewById(R.id.txt_last_update);
            txt_sr_no = itemView.findViewById(R.id.txt_sr_no);
            txt_exp_name = itemView.findViewById(R.id.txt_exp_name);
            txt_total_exp = itemView.findViewById(R.id.txt_total_exp);
        }
    }
}


