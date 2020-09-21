package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.nspl.restaurantlite.Interface.OnClickListenerEmail;
import com.demo.nspl.restaurantlite.R;


import java.util.List;

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.MyViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<String> mList;
    private OnClickListenerEmail mOnClickListenerEmail;

    public EmailAdapter(Context context, List<String> list){
        this.mContext = context;
        this.mList = list;
        this.mInflater = LayoutInflater.from(context);
    }

    public void SetOnClickListener(OnClickListenerEmail onClickListenerEmail){
        this.mOnClickListenerEmail = onClickListenerEmail;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_email, parent, false);
        return new EmailAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String current = mList.get(position);
        holder.txt_Email_name.setText(current);
        holder.Bind(mOnClickListenerEmail,position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void Add(List<String> list){
        this.mList = list;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_Email_name;
        ImageView imageView_cancel;
        LinearLayout main_layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            main_layout = itemView.findViewById(R.id.main_layout);
            txt_Email_name = itemView.findViewById(R.id.txt_Email_name);
            imageView_cancel = itemView.findViewById(R.id.imageView_cancel);

        }

        void Bind(OnClickListenerEmail onClickListenerEmail, int position) {
            imageView_cancel.setOnClickListener(view -> {
                try{
                    onClickListenerEmail.OnItemClick(position);
                }catch (NullPointerException ignored){

                }
            });

        }

    }
}
