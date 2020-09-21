package com.demo.nspl.restaurantlite.SMS;

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

import java.util.ArrayList;
import java.util.List;

public class SmsFormatAdapter extends RecyclerView.Adapter<SmsFormatAdapter.MyViewHolder>{

    List<ClsMessageFormat> list = new ArrayList<>();
    View itemView;
    Context context;
    private OnClickSmsFormat listener;

    public SmsFormatAdapter(Context context){
        this.context =context;
    }

    public void AddItems(List<ClsMessageFormat> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void remove(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        notifyDataSetChanged();
    }

    public void SetOnClickListener(OnClickSmsFormat onClickSmsFormat){
        this.listener = onClickSmsFormat;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sms_format, parent,
                false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClsMessageFormat item = list.get(position);
        holder.title.setText(item.getTitle().equalsIgnoreCase("") ?
                "No Title" : item.getTitle());
        holder.tx_default.setText("Default : " + item.getDefault());
        holder.type.setText("Type : " +item.getType());
        holder.remark.setText("Remark : " +item.getRemark());
        holder.Bind(item,listener,position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView title, type,remark,tx_default;
        ImageView view,delete,edit;

        public MyViewHolder(View itemView) {
            super(itemView);

            ll_header = itemView.findViewById(R.id.ll_header);
            title = itemView.findViewById(R.id.title);
            type = itemView.findViewById(R.id.type);
            remark = itemView.findViewById(R.id.remark);
            tx_default = itemView.findViewById(R.id.tx_default);
            view = itemView.findViewById(R.id.view);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);

        }

        void Bind(ClsMessageFormat clsMessageFormat, OnClickSmsFormat onClickBackUp, int position){
//            ll_header.setOnClickListener(v -> {
//                onClickBackUp.OnClick(clsMessageFormat,position,"header Click");
//            });
            view.setOnClickListener(v -> {
                onClickBackUp.OnClick(clsMessageFormat,position,"Image Click");
            });

            delete.setOnClickListener(v -> {
                onClickBackUp.OnClick(clsMessageFormat,position,"delete Click");
            });

            edit.setOnClickListener(v -> {
                onClickBackUp.OnClick(clsMessageFormat,position,"edit Click");
            });



        }
    }


    public interface OnClickSmsFormat {

        void OnClick(ClsMessageFormat clsMessageFormat , int position ,String mode);
    }
}
