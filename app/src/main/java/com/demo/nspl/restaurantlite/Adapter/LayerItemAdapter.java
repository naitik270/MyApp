package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Interface.OnClickLayerItem;
import com.demo.nspl.restaurantlite.Purchase.OnItemClick;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsLayerItemMaster;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class LayerItemAdapter extends RecyclerView.Adapter<LayerItemAdapter.MyViewHolder> {

    Context c;
    private View itemview;
    List<ClsLayerItemMaster> list = new ArrayList<ClsLayerItemMaster>();
    private OnClickLayerItem mOnItemClickListener;
    private OnItemClick onItemClick;

    public LayerItemAdapter(Context context) {
        this.c = context;

    }

    public void AddItems(List<ClsLayerItemMaster> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void SetOnClickListener(OnClickLayerItem onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        itemview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layer_item, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemview);
        return myViewHolder;
    }


    public void setOnViewImg(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ClsLayerItemMaster current = list.get(i);


        myViewHolder.lbl_Layer_Namer.setText(current.getITEM_NAME().toUpperCase());
        myViewHolder.txt_item_code.setText(current.getITEM_CODE().toUpperCase());
        myViewHolder.lbl_Min.setText(String.valueOf(current.getMIN_STOCK()).toUpperCase());
        myViewHolder.lbl_Max.setText(String.valueOf(current.getMAX_STOCK()).toUpperCase());
        myViewHolder.lbl_Unit.setText(String.valueOf(current.getUNIT_CODE()).toUpperCase());
        myViewHolder.lbl_Active.setText(String.valueOf(current.getACTIVE()).toUpperCase());
        myViewHolder.lbl_Opening_Stock.setText(String.valueOf(current.getOpening_Stock()).toUpperCase());

        myViewHolder.lbl_Rate.setText("\u20B9 " + ClsGlobal.round(current.getRATE_PER_UNIT(), 2));
        myViewHolder.lbl_Rate.setTextColor(Color.parseColor("#225A25"));

        myViewHolder.txt_wholesale_rate.setText("\u20B9 " + ClsGlobal.round(current.getWHOLESALE_RATE(), 2));
        myViewHolder.txt_wholesale_rate.setTextColor(Color.parseColor("#B6861B"));


        if (current.getHSN_SAC_CODE() != null && !current.getHSN_SAC_CODE().isEmpty()) {
            myViewHolder.lbl_hsn_sac_code.setText(current.getHSN_SAC_CODE().toUpperCase());
        } else {
            myViewHolder.lbl_hsn_sac_code.setText("");
        }

        if (current.getTAX_TYPE() != null && !current.getTAX_TYPE().isEmpty()) {
            myViewHolder.txt_tax_type.setText(current.getTAX_TYPE().toUpperCase());
        } else {
            myViewHolder.txt_tax_type.setText("");
        }

        if (current.getTAX_APPLY() != null && !current.getTAX_APPLY().isEmpty()) {
            myViewHolder.txt_apply_tax.setText(current.getTAX_APPLY());
        } else {
            myViewHolder.txt_apply_tax.setText("NO");
        }

        if (current.getSLAB_NAME() != null && !current.getSLAB_NAME().isEmpty()) {
            myViewHolder.lbl_Tax_Slab.setText(current.getSLAB_NAME().toUpperCase());
        } else {
            myViewHolder.lbl_Tax_Slab.setText("");
        }

        myViewHolder.lbl_Remark.setText(current.getREMARK().toUpperCase());

        //set item tag Adapter Here
        List<String> tagList = current.getTagList();
        //Set<String> uniqueTagsList = new HashSet<String>(current.getTagList());
        myViewHolder.entry_chip_group.removeAllViews();
        if (tagList.size() != 0) {

            Gson gson = new Gson();
            String getTaglist = gson.toJson(tagList);
            Log.e("getTaglist", getTaglist);


            for (String name : tagList) {
                final Chip entryChip = getChip(name.toLowerCase());
                Log.e("entryChip", name);
                myViewHolder.entry_chip_group.addView(entryChip);
            }
        }


        myViewHolder.Bind(current, mOnItemClickListener, i);

        myViewHolder.viewImgClick(current, onItemClick, i);

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

    private Chip getChip(String text) {
        final Chip chip = new Chip(c);
        chip.setChipDrawable(ChipDrawable.createFromResource(c, R.xml.tag_chip));
        chip.setText(text);
        return chip;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        TextView lbl_Layer_Namer, lbl_Unit, lbl_Min, lbl_Max, lbl_Rate,
                lbl_hsn_sac_code, lbl_Active, lbl_Opening_Stock, lbl_Remark, lbl_Tax_Slab;

        TextView txt_tax_type, txt_wholesale_rate, txt_apply_tax, txt_item_code;
        ChipGroup entry_chip_group;

        LinearLayout ll_view_img;


        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header = itemView.findViewById(R.id.ll_header);
            txt_apply_tax = itemView.findViewById(R.id.txt_apply_tax);
            lbl_Layer_Namer = itemView.findViewById(R.id.lbl_Layer_Namer);
            lbl_Unit = itemView.findViewById(R.id.lbl_Unit);
            lbl_Min = itemView.findViewById(R.id.lbl_Min);
            lbl_Max = itemView.findViewById(R.id.lbl_Max);
            lbl_Rate = itemView.findViewById(R.id.lbl_Rate);
            lbl_Active = itemView.findViewById(R.id.lbl_Active);
            lbl_Opening_Stock = itemView.findViewById(R.id.lbl_Opening_Stock);
            lbl_Tax_Slab = itemView.findViewById(R.id.lbl_Tax_Slab);
            lbl_Remark = itemView.findViewById(R.id.lbl_Remark);
            entry_chip_group = itemView.findViewById(R.id.entry_chip_group);
            lbl_hsn_sac_code = itemView.findViewById(R.id.lbl_hsn_sac_code);

            txt_tax_type = itemView.findViewById(R.id.txt_tax_type);
            txt_wholesale_rate = itemView.findViewById(R.id.txt_wholesale_rate);
            txt_item_code = itemView.findViewById(R.id.txt_item_code);


            ll_view_img = itemView.findViewById(R.id.ll_view_img);

        }

        void Bind(ClsLayerItemMaster clsLayerItemMaster,
                  OnClickLayerItem OnclickInventoryLayer, int position) {
            ll_header.setOnLongClickListener(v -> {

                OnclickInventoryLayer.OnClick(clsLayerItemMaster, position);
                return false;
            });
        }


        void viewImgClick(ClsLayerItemMaster clsLayerItemMaster,
                          OnItemClick onItemClick, int position) {

            ll_view_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.onItemClick(clsLayerItemMaster, position);
                }
            });

        }
    }
}

