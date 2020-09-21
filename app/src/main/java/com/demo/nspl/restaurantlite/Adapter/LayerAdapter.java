package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Interface.OnClickInventoryLayer;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsInventoryLayer;

import java.util.ArrayList;
import java.util.List;

public class LayerAdapter extends RecyclerView.Adapter<LayerAdapter.MyViewHolder> {

    Context c;
    private View itemview;
    List<ClsInventoryLayer> list = new ArrayList<ClsInventoryLayer>();


    AppCompatActivity activity;
    private OnClickInventoryLayer mOnItemClickListener;

    public LayerAdapter(Context context, List<ClsInventoryLayer> list) {
        this.c = context;
        this.list = list;
    }

    public void SetOnClickListener(OnClickInventoryLayer onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        itemview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layer_list, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemview);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ClsInventoryLayer current = list.get(i);

        Log.e("currentCategoryName",current.getInventoryLayerName());
        myViewHolder.lbl_Layer_Namer.setText(current.getInventoryLayerName());
        myViewHolder.lbl_Category.setText(String.valueOf(current.getInventoryLayerCategoryName()));
        myViewHolder.lbl_Display_Order.setText(String.valueOf(current.getDisplayOrder()));
        myViewHolder.lbl_Active.setText(current.getActive());
        myViewHolder.lbl_remark.setText(current.getRemark());
        myViewHolder.Bind(current,mOnItemClickListener,i);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void remove(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView lbl_Layer_Namer, lbl_Category, lbl_Display_Order, lbl_Active, lbl_remark;


        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header = itemView.findViewById(R.id.ll_header);
            lbl_Layer_Namer = itemView.findViewById(R.id.lbl_Layer_Namer);
            lbl_Category = itemView.findViewById(R.id.lbl_Category);
            lbl_Display_Order = itemView.findViewById(R.id.lbl_Display_Order);
            lbl_Active = itemView.findViewById(R.id.lbl_Active);
            lbl_remark = itemView.findViewById(R.id.lbl_remark);

        }

        void Bind(ClsInventoryLayer clsInventoryLayer,OnClickInventoryLayer OnclickInventoryLayer, int position){
            ll_header.setOnLongClickListener(v -> {

                OnclickInventoryLayer.OnClick(clsInventoryLayer,position);
                return false;
            });
        }
    }
}
