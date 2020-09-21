package com.demo.nspl.restaurantlite.SMS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.R;

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
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_keywords, parent, false);
        KeywordAdapter.MyViewHolder myViewHolder = new KeywordAdapter.MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClsKeywordDescription item = list.get(position);
        holder.tv_keyword.setText(item.getKeyword());
        holder.tv_description.setText(item.getDescription());
        holder.Bind(item,mOnClickListener,position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView tv_keyword, tv_description;

        public MyViewHolder(View itemView) {
            super(itemView);

            ll_header = itemView.findViewById(R.id.ll_header);
            tv_keyword = itemView.findViewById(R.id.tv_keyword);
            tv_description = itemView.findViewById(R.id.tv_description);


        }

        void Bind(ClsKeywordDescription clsGetBackupDetailsList, OnClickKeyword onClickBackUp, int position){
            ll_header.setOnClickListener(v -> {
                onClickBackUp.OnClick(clsGetBackupDetailsList,position);
            });
        }
    }

    public interface OnClickKeyword {

        void OnClick(ClsKeywordDescription clsGetBackupDetailsList , int position);
    }
}
