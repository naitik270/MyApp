package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.Interface.OnClickSalesItem;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsLayerItemMaster;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class DisplayOrderWithHeaderAdapter extends ArrayAdapter<ClsLayerItemMaster> {

    private LayoutInflater mInflater;
    LinearLayout linearLayout;
    OnClickSalesItem onClickSalesItem;
    Context context;
    String saleMode = "";

    public DisplayOrderWithHeaderAdapter(@NonNull Context context,
                                         List<ClsLayerItemMaster> list, String saleMode) {
        super(context, 0, list);
        this.context = context;
        this.saleMode = saleMode;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void SetOnClicklistener(OnClickSalesItem onClickSalesItem) {
        this.onClickSalesItem = onClickSalesItem;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

       Log.e("getView","getView call");
        ClsLayerItemMaster current = getItem(position);

        //If the cell is a section header we inflate the header layout
        if (current.isHeader()) {
            v = mInflater.inflate(R.layout.card_orders, null);
            v.setClickable(false);


            TextView header = v.findViewById(R.id.txt_cat_name);

            header.setText(String.valueOf(current.getFirst_letter()));
            Log.e("header", current.getFirst_letter());

        } else {
            v = mInflater.inflate(R.layout.display_order_item, null);
            TextView txt_item_name = v.findViewById(R.id.txt_item_name);
            TextView txt_Display_item_code = v.findViewById(R.id.txt_Display_item_code);
            TextView txt_Display_unit = v.findViewById(R.id.txt_Display_unit);
            TextView txt_Diaplay_price = v.findViewById(R.id.txt_Diaplay_price);

            TextView txt_final_stock = v.findViewById(R.id.txt_final_stock);


            CardView card = v.findViewById(R.id.card);

            ChipGroup TagChipGroup = v.findViewById(R.id.entry_chip_group);
            linearLayout = v.findViewById(R.id.linear);


            String hsnCode = "";
            if (current.getHSN_SAC_CODE() != null && !current.getHSN_SAC_CODE().isEmpty()) {

                hsnCode = ", ".concat("HSN: ").concat(current.getHSN_SAC_CODE()).toUpperCase();
            } else {
                hsnCode = ", ".concat("HSN: ").concat("");
            }

            txt_item_name.setText("" + current.getITEM_NAME().toUpperCase());
            txt_Display_item_code.setText("" + current.getITEM_CODE().toUpperCase().concat(hsnCode));
            txt_Display_unit.setText("" + current.getUNIT_CODE().toUpperCase());


            if (saleMode != null && !saleMode.equalsIgnoreCase("")) {
                if (saleMode.equalsIgnoreCase("Sale")) {
                    txt_Diaplay_price.setText("" + current.getRATE_PER_UNIT());
//                    txt_Diaplay_price.setText("" + ClsGlobal.round(current.get_saleRateIncludingTax(), 2));
                    card.setCardBackgroundColor(Color.parseColor("#225A25"));
                } else {
                    txt_Diaplay_price.setText("" + current.getWHOLESALE_RATE());
//                    txt_Diaplay_price.setText("" + ClsGlobal.round(current.get_wholesaleRateIncludingTax(), 2));
                    card.setCardBackgroundColor(Color.parseColor("#B6861B"));
                }
            }


            if (current.getOpening_Stock() != null && current.getIN() != null && current.getOUT() != null) {
                double _balance = (current.getOpening_Stock() + current.getIN()) - current.getOUT();

                if (_balance < 0) {
                    txt_final_stock.setTextColor(Color.parseColor("#c40000"));
                } else {
                    txt_final_stock.setTextColor(Color.parseColor("#225A25"));
                }
                txt_final_stock.setText("AVL: " + ClsGlobal.round(_balance, 2));
            }


            List<String> tagList = current.getTagList();
            if (tagList.size() != 0) {

                for (String name : tagList) {
                    final Chip entryChip = getChip(name.toLowerCase());
                    Log.e("entryChip", name);
                    TagChipGroup.addView(entryChip);
                }
            }

            Bind(current, onClickSalesItem);

        }

        return v;
    }

    public static class ViewHolder {
        public TextView textView;
    }


    void Bind(final ClsLayerItemMaster clsLayerItemMaster, OnClickSalesItem onClickSalesItem) {
        linearLayout.setOnClickListener(v ->
                // send current position via Onclick method.
                onClickSalesItem.OnClick(clsLayerItemMaster));
    }


    private Chip getChip(String text) {
        final Chip chip = new Chip(context);
        chip.setChipDrawable(ChipDrawable.createFromResource(context, R.xml.tag_chip));
        chip.setText(text);
        return chip;
    }

}