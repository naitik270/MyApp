package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.SMS.ClsKeywordDescription;

import java.util.ArrayList;
import java.util.List;

public class KeywordAdapter extends RecyclerView.Adapter<KeywordAdapter.MyViewHolder>  {

    List<ClsKeywordDescription> list = new ArrayList<>();
    View itemView;
    Context context;
    private OnClickKeyword mOnClickListener;

    public KeywordAdapter(Context context, List<ClsKeywordDescription> list){
        this.context =context;
        this.list =list;
    }

    public void SetOnClickListener(OnClickKeyword onClickBackUp){
        this.mOnClickListener = onClickBackUp;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_keywords,
                parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClsKeywordDescription item = list.get(position);

        if (item.getKeyword().equalsIgnoreCase("Separator")){
            holder.separator.setVisibility(View.VISIBLE);
            holder.separator.setText(item.getDescription());
            holder.Cv_header.setVisibility(View.GONE);

        }else {
            holder.separator.setVisibility(View.GONE);
            holder.Cv_header.setVisibility(View.VISIBLE);

            holder.tv_keyword.setText(item.getKeyword());
            holder.tv_description.setText(item.getDescription());
        }

        holder.Bind(item,mOnClickListener,position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView Cv_header;

        TextView tv_keyword, tv_description,separator;

        public MyViewHolder(View itemView) {
            super(itemView);

            Cv_header = itemView.findViewById(R.id.Cv_header);
            tv_keyword = itemView.findViewById(R.id.tv_keyword);
            tv_description = itemView.findViewById(R.id.tv_description);
            separator = itemView.findViewById(R.id.separator);


        }

        void Bind(ClsKeywordDescription clsGetBackupDetailsList, OnClickKeyword onClickBackUp, int position){
            if (Cv_header != null){

                Cv_header.setOnClickListener(v -> {
                    onClickBackUp.OnClick(clsGetBackupDetailsList,position);
                });
            }
        }
    }

    public interface OnClickKeyword {

        void OnClick(ClsKeywordDescription clsGetBackupDetailsList, int position);
    }
}
