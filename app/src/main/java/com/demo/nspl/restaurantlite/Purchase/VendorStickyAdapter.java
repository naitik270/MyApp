package com.demo.nspl.restaurantlite.Purchase;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsVendor;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Abhishek on 24-03-2018.
 */

public class VendorStickyAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    List<ClsVendor> listVendor = new ArrayList<>();
    Context context;
    LayoutInflater inflater = null;
    int AdpCount = 0;


    public VendorStickyAdapter( Context context) {

        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void AddItems(List<ClsVendor> listVendor){
        this.listVendor = listVendor;
        notifyDataSetChanged();
    }


    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.header_row, parent, false);
            holder.txt_header = convertView.findViewById(R.id.rowName);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        ClsVendor objClsVendorMaster = listVendor.get(position);
        String headerText = "" + objClsVendorMaster.getVendor_name().subSequence(0, 1).charAt(0);
        holder.txt_header.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return listVendor.get(position).getVendor_name().subSequence(0, 1).charAt(0);
    }

    @Override
    public int getCount() {
        return listVendor.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        ClsVendor objClsVendorMaster = listVendor.get(i);

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.retail_row_vendor_list, viewGroup, false);
            holder.txt_vendor_name = view.findViewById(R.id.txt_vendor_name);

            holder.txt_vendor_name.setText(String.valueOf(objClsVendorMaster.getVendor_name()));

            Log.d("Result", "VendorName- " + objClsVendorMaster.getVendor_name());
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        return view;
    }

    class ViewHolder {
        TextView txt_vendor_name;
    }

    class HeaderViewHolder {
        TextView txt_header;
    }

    public void updateList(List<ClsVendor> list) {
        listVendor = list;
        notifyDataSetChanged();
    }
}
