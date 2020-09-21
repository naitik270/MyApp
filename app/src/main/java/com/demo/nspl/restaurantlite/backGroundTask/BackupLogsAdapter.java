package com.demo.nspl.restaurantlite.backGroundTask;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsCommonLogs;

import java.util.ArrayList;
import java.util.List;

public class BackupLogsAdapter  extends RecyclerView.Adapter<BackupLogsAdapter.MyViewHolder> {

    private View itemview;
    //    private Context context;
    private List<ClsCommonLogs> list = new ArrayList<>();

    public BackupLogsAdapter(List<ClsCommonLogs> list)
    {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        itemview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.backup_log_item, viewGroup, false);
        BackupLogsAdapter.MyViewHolder myViewHolder = new BackupLogsAdapter.MyViewHolder(itemview);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ClsCommonLogs current = list.get(i);
        myViewHolder.txt_Status.setText(current.getStatus());
        myViewHolder.txt_Remark.setText(current.getRemark());
        myViewHolder.txt_DateTime.setText(current.getDate_Time());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txt_Status,txt_Remark,txt_DateTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_Status = itemView.findViewById(R.id.txt_Status);
            txt_Remark = itemView.findViewById(R.id.txt_Remark);
            txt_DateTime = itemView.findViewById(R.id.txt_DateTime);

        }
    }
}