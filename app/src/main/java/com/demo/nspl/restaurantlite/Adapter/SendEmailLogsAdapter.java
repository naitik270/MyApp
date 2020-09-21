package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.nspl.restaurantlite.classes.ClsEmailLogs;
import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

public class SendEmailLogsAdapter extends RecyclerView.Adapter<SendEmailLogsAdapter.MyViewHolder>  {

    private View itemview;
    List<ClsEmailLogs> list = new ArrayList<ClsEmailLogs>();
    Context context;

    public SendEmailLogsAdapter(Context context,List<ClsEmailLogs> list){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        itemview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_email_logs, viewGroup, false);
        SendEmailLogsAdapter.MyViewHolder myViewHolder = new SendEmailLogsAdapter.MyViewHolder(itemview);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ClsEmailLogs current = list.get(i);
        myViewHolder.txt_Status.setText("Status: "+current.getMESSAGE());
        myViewHolder.txt_DateTime.setText("Date And Time: "+current.getDATE_TIME());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txt_Status,txt_DateTime;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_Status = itemView.findViewById(R.id.txt_Status);
            txt_DateTime = itemView.findViewById(R.id.txt_DateTime);

        }
    }
}
