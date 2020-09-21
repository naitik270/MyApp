package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.nspl.restaurantlite.Interface.OnClickSalesItem;

import com.demo.nspl.restaurantlite.classes.ClsLayerItemMaster;
import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class DiaplayOrderAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    Context context;
    private LayoutInflater inflater;
    List<ClsLayerItemMaster> list_item = new ArrayList<>();
    private OnClickSalesItem mOnItemClickListener;


    public DiaplayOrderAdapter(Context c, List<ClsLayerItemMaster> order) {
        this.context = c;
        this.list_item = order;
        inflater = LayoutInflater.from(c);
    }

    public void SetOnClickListener(OnClickSalesItem onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public long getHeaderId(int position) {
        return list_item.get(position).getITEM_NAME().charAt(0);
    }

    @Override
    public int getCount() {
        return list_item.size();
    }

    @Override
    public Object getItem(int position) {
        return list_item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DiaplayOrderAdapter.ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();
            ClsLayerItemMaster currentObj = list_item.get(position);

            convertView = inflater.inflate(R.layout.display_order_item, parent, false);
            holder.txt_item_name = convertView.findViewById(R.id.txt_item_name);
            holder.txt_item_code = convertView.findViewById(R.id.txt_item_code);
            holder.txt_unit = convertView.findViewById(R.id.txt_unit);
            holder.txt_price = convertView.findViewById(R.id.txt_price);
            holder.linear = convertView.findViewById(R.id.Item_List);

            holder.txt_item_name.setText("" + currentObj.getITEM_NAME());
            holder.txt_item_code.setText("Code:" + currentObj.getITEM_CODE());
            holder.txt_unit.setText("Unit: " + currentObj.getUNIT_CODE());
            holder.txt_price.setText("Price Rs: " + String.valueOf(currentObj.getRATE_PER_UNIT()));
            holder.Bind(currentObj, mOnItemClickListener, position);
            convertView.setTag(holder);

        } else {
            holder = (DiaplayOrderAdapter.ViewHolder) convertView.getTag();
        }


        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        DiaplayOrderAdapter.HeaderViewHolder holder;

        ClsLayerItemMaster currentObj = list_item.get(position);
        if (convertView == null) {
            holder = new DiaplayOrderAdapter.HeaderViewHolder();
            convertView = inflater.inflate(R.layout.card_orders, parent, false);
            holder.txt_cat_name = convertView.findViewById(R.id.txt_cat_name);
            convertView.setTag(holder);
        } else {
            holder = (DiaplayOrderAdapter.HeaderViewHolder) convertView.getTag();
        }

        String headerText = "" + currentObj.getITEM_NAME().charAt(0);
        holder.txt_cat_name.setText(headerText);


        return convertView;
    }


    class ViewHolder {
        TextView txt_item_name, txt_item_code, txt_unit, txt_price;
        LinearLayout linear;


        void Bind(ClsLayerItemMaster clsLayerItemMaster, OnClickSalesItem onClickSalesItem, int position) {
            linear.setOnClickListener(v -> {
                onClickSalesItem.OnClick(clsLayerItemMaster);

            });
        }

    }

    class HeaderViewHolder {
        TextView txt_cat_name;
    }
}
