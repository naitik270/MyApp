package com.demo.nspl.restaurantlite.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Interface.OnClickSalesItem;
import com.demo.nspl.restaurantlite.Purchase.OnItemClick;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsLayerItemMaster;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class SalesItemWithHeaderRV extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int SECTION_VIEW = 0;
    private static final int CONTENT_VIEW = 1;
    OnClickSalesItem onClickSalesItem;
    Context context;
    List<ClsLayerItemMaster> mlist = new ArrayList<>();
    String saleMode = "";


    public SalesItemWithHeaderRV(Context context, String saleMode) {
        this.context = context;
        this.saleMode = saleMode;
    }

    public void AddItems(List<ClsLayerItemMaster> list) {
        if (list != null) {

            this.mlist = list;
            notifyDataSetChanged();
//            notifyItemRangeChanged(0, this.mlist.size());

        }
    }


    public void SetOnClicklistener(OnClickSalesItem onClickSalesItem) {
        this.onClickSalesItem = onClickSalesItem;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == SECTION_VIEW) {
            return new SectionHeaderViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_orders, parent, false));
        } else {
            return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.display_order_item, parent, false));
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ClsLayerItemMaster current = mlist.get(position);

        switch (holder.getItemViewType()) {

            case 0:

                SectionHeaderViewHolder SectionHeaderviewHolder = (SectionHeaderViewHolder) holder;
                SectionHeaderviewHolder.headerTitleTextview.setClickable(false);
                SectionHeaderviewHolder.headerTitleTextview.setText(current.getFirst_letter());

                break;

            case 1:

                ItemViewHolder ItemViewviewHolder = (ItemViewHolder) holder;
//                ItemViewviewHolder.txt_item_name.setText("" + current.getITEM_NAME().toUpperCase().concat(" "+position));
                ItemViewviewHolder.txt_item_name.setText("" + current.getITEM_NAME().toUpperCase());

                String hsnCode = "";
                if (current.getHSN_SAC_CODE() != null && !current.getHSN_SAC_CODE().isEmpty()) {

                    hsnCode = current.getHSN_SAC_CODE().toUpperCase();
                } else {
                    hsnCode = "";
                }

                ItemViewviewHolder.txt_Display_item_code.setText("" + current.getITEM_CODE()
                        .toUpperCase().concat(", UNIT: ").concat(current.getUNIT_CODE().toUpperCase()));
                ItemViewviewHolder.txt_Display_unit.setText(hsnCode);

                Log.d("--MODE--", "Mode: " + saleMode);


              if (saleMode != null && saleMode.equalsIgnoreCase("Sale")) {
                    ItemViewviewHolder.card.setCardBackgroundColor(Color.parseColor("#225A25"));
                    ItemViewviewHolder.txt_Diaplay_price.setText("\u20B9 " + ClsGlobal.round(current.getRATE_PER_UNIT(), 2));
                } else if (saleMode != null && saleMode.equalsIgnoreCase("Wholesale")) {
                    ItemViewviewHolder.card.setCardBackgroundColor(Color.parseColor("#B6861B"));
                    ItemViewviewHolder.txt_Diaplay_price.setText("\u20B9 " + ClsGlobal.round(current.getWHOLESALE_RATE(), 2));
                } else if (saleMode != null && saleMode.equalsIgnoreCase("Retail Quotation")) {
                    ItemViewviewHolder.card.setCardBackgroundColor(Color.parseColor("#8E1F68"));
                    ItemViewviewHolder.txt_Diaplay_price.setText("\u20B9 " + ClsGlobal.round(current.getRATE_PER_UNIT(), 2));
                } else if (saleMode != null && saleMode.equalsIgnoreCase("Wholesale Quotation")) {
                    ItemViewviewHolder.card.setCardBackgroundColor(Color.parseColor("#8E1F68"));
                    ItemViewviewHolder.txt_Diaplay_price.setText("\u20B9 " + ClsGlobal.round(current.getWHOLESALE_RATE(), 2));
                }


                if (current.getOpening_Stock() != null && current.getIN() != null && current.getOUT() != null) {
                    double _balance = (current.getOpening_Stock() + current.getIN()) - current.getOUT();

                    if (_balance < 0 || _balance == 0.0) {
                        ItemViewviewHolder.txt_final_stock.setTextColor(Color.parseColor("#c40000"));
                        ItemViewviewHolder.ll_color.setBackgroundColor(Color.parseColor("#56585858"));
                        ItemViewviewHolder.card.setCardBackgroundColor(Color.parseColor("#585858"));

                    } else {
                        ItemViewviewHolder.txt_final_stock.setTextColor(Color.parseColor("#225A25"));
                        ItemViewviewHolder.ll_color.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                    ItemViewviewHolder.txt_final_stock.setText("AVL: " + ClsGlobal.round(_balance, 2));
                }

                List<String> tagList = current.getTagList();
                ItemViewviewHolder.TagChipGroup.removeAllViews();
                if (tagList.size() != 0) {
                    for (String name : tagList) {
                        final Chip entryChip = getChip(name.toLowerCase());
                        ItemViewviewHolder.TagChipGroup.addView(entryChip);
                    }
                }

                ItemViewviewHolder.Bind(current, onClickSalesItem);
                ItemViewviewHolder.viewImgClick(current, onItemClick, position);
                break;
        }

    }

    @Override
    public int getItemCount() {
        if (mlist != null) {
            return mlist.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mlist.get(position).isHeader()) {

            return SECTION_VIEW;
        } else {
            return CONTENT_VIEW;
        }

    }


    public class SectionHeaderViewHolder extends RecyclerView.ViewHolder {

        TextView headerTitleTextview;

        SectionHeaderViewHolder(View itemView) {
            super(itemView);
            headerTitleTextview = itemView.findViewById(R.id.txt_cat_name);
        }
    }


    private OnItemClick onItemClick;

    public void setOnViewImg(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_item_name, txt_Display_item_code, txt_Display_unit, txt_Diaplay_price, txt_final_stock;


        private CardView card;
        private ChipGroup TagChipGroup;
        private LinearLayout linearLayout;
        private RelativeLayout ll_color;


        ImageView iv_photo;

        ItemViewHolder(View itemView) {
            super(itemView);

            txt_item_name = itemView.findViewById(R.id.txt_item_name);
            txt_Display_item_code = itemView.findViewById(R.id.txt_Display_item_code);
            txt_Display_unit = itemView.findViewById(R.id.txt_Display_unit);
            txt_Diaplay_price = itemView.findViewById(R.id.txt_Diaplay_price);
            txt_final_stock = itemView.findViewById(R.id.txt_final_stock);
            TagChipGroup = itemView.findViewById(R.id.entry_chip_group);
            linearLayout = itemView.findViewById(R.id.linear);
            ll_color = itemView.findViewById(R.id.ll_color);
            card = itemView.findViewById(R.id.card);
            iv_photo = itemView.findViewById(R.id.iv_photo);

        }

        void Bind(final ClsLayerItemMaster clsLayerItemMaster, OnClickSalesItem onClickSalesItem) {
            linearLayout.setOnClickListener(v ->
                    // send current position via Onclick method.
                    onClickSalesItem.OnClick(clsLayerItemMaster));
        }


        void viewImgClick(ClsLayerItemMaster clsLayerItemMaster,
                          OnItemClick onItemClick, int position) {

            iv_photo.setOnClickListener(v -> onItemClick.onItemClick(clsLayerItemMaster, position));

        }


    }


    private Chip getChip(String text) {
        final Chip chip = new Chip(context);
        chip.setChipDrawable(ChipDrawable.createFromResource(context, R.xml.tag_chip));
        chip.setText(text);
        return chip;
    }


}
