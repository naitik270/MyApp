package com.demo.nspl.restaurantlite.SMS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.A_Test.ClsGetValue;
import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;


public class DisplayCustomerNameAdapter extends RecyclerView.Adapter<DisplayCustomerNameAdapter.MyViewHolder> {

    private List<ClsGetValue> data = new ArrayList<>();
    Context context;
    View itemView;
    private LayoutInflater inflater = null;
    private OnDeleteClick mOnDeleteClick;
    OnAllCheckClick onAllCheckClick;

    public DisplayCustomerNameAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void AddItems(List<ClsGetValue> item) {
        this.data = item;
        notifyDataSetChanged();
    }

    public void SetOnClickListener(OnDeleteClick mOnDeleteClick) {
        this.mOnDeleteClick = mOnDeleteClick;
    }

    public void remove(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_display_customer_name, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final ClsGetValue objClsRoomMaster = data.get(position);


        holder.empty_title_text.setText(objClsRoomMaster.getmMobile() +
                " - " + objClsRoomMaster.getmName().toUpperCase());


//        holder.empty_title_text.setText(objClsRoomMaster.getmName().toUpperCase()
//                + " (" + objClsRoomMaster.getmMobile() + ")");

//        holder.empty_title_text.setText(objClsRoomMaster.getmName().toUpperCase()
//                + " (" + objClsRoomMaster.getmMobile() + ")");


        holder.txt_cust_number.setText(objClsRoomMaster.getmMobile());

        holder.Bind(data.get(position), mOnDeleteClick, position);
//        holder.BindAllCheck(data.get(position), onAllCheckClick, position, holder);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    interface OnDeleteClick {

        void onClick(ClsGetValue cls, int position);

    }

    interface OnAllCheckClick {

        void onClick(ClsGetValue cls, int position, MyViewHolder holder);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_layout;
        TextView txt_cust_number, empty_title_text, txt_cust_id;
        ImageView iv_delete;


        public MyViewHolder(View itemView) {
            super(itemView);
            ll_layout = itemView.findViewById(R.id.ll_layout);
            txt_cust_id = itemView.findViewById(R.id.txt_cust_id);
            empty_title_text = itemView.findViewById(R.id.empty_title_text);
            txt_cust_number = itemView.findViewById(R.id.txt_cust_number);
            iv_delete = itemView.findViewById(R.id.iv_delete);

        }


        void Bind(final ClsGetValue cls, OnDeleteClick mOnDeleteClick, int position) {
            iv_delete.setOnClickListener(v ->
                    // send current position via Onclick method.
                    mOnDeleteClick.onClick(cls, position));
        }


//        void BindAllCheck(final ClsGetValue cls, OnAllCheckClick onAllCheckClick, int position, MyViewHolder holder) {
//            iv_delete.setOnClickListener(v ->
//                    // send current position via Onclick method.
//                    onAllCheckClick.onClick(cls, position, holder));
//        }


    }


    public void updateList(List<ClsGetValue> list) {
        data = list;
        notifyDataSetChanged();
    }


}



