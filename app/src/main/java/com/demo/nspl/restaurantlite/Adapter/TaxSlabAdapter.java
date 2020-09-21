package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Interface.OnClickTaxSlab;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsTaxSlab;

import java.util.ArrayList;
import java.util.List;

public class TaxSlabAdapter extends RecyclerView.Adapter<TaxSlabAdapter.MyViewHolder>{

    Context c;
    private View itemview;
    List<ClsTaxSlab> list= new ArrayList<>();
    OnClickTaxSlab mOnClickListener;

    public TaxSlabAdapter(Context context,List<ClsTaxSlab> list){
        this.c = context;
        this.list = list;
    }

    public void SetOnClickListener(OnClickTaxSlab onClickSalesItem){
        this.mOnClickListener = onClickSalesItem;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.tax_slab_list, parent, false);
        TaxSlabAdapter.MyViewHolder myViewHolder = new TaxSlabAdapter.MyViewHolder(itemview);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClsTaxSlab current = list.get(position);

        holder.lbl_Slab_name.setText(current.getSLAB_NAME());
        holder.lbl_Remark.setText(current.getREMARK());
        holder.lbl_Active.setText(current.getACTIVE());
        holder.Bind(current,mOnClickListener,position);

    }


    public void remove(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView lbl_Slab_name, lbl_Active, lbl_Remark;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header = itemView.findViewById(R.id.ll_header);
            lbl_Slab_name = itemView.findViewById(R.id.lbl_Slab_name);
            lbl_Active = itemView.findViewById(R.id.lbl_Active);
            lbl_Remark = itemView.findViewById(R.id.lbl_Remark);

        }


        void Bind(final ClsTaxSlab clsItem, OnClickTaxSlab onClickListener, int position) {
            ll_header.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    onClickListener.OnClick(clsItem, position);

                    return false;
                }
            });
        }
    }

}
