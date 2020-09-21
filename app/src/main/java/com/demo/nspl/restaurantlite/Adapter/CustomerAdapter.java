package com.demo.nspl.restaurantlite.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.nspl.restaurantlite.Interface.OnClickCustomerAdapter;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsCustomerMaster;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class CustomerAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    Context context;
    List<ClsCustomerMaster> list_customer = new ArrayList<>();
    private StickyListHeadersListView lst;
    private LayoutInflater inflater;
    private OnClickCustomerAdapter mOnClickCustomerAdapter;

    public CustomerAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);

    }


    public void AddItems(List<ClsCustomerMaster> list_customer) {
        this.list_customer = list_customer;
        notifyDataSetChanged();

    }

    public void SetOnClickListener(OnClickCustomerAdapter onClickCustomerAdapter) {
        this.mOnClickCustomerAdapter = onClickCustomerAdapter;
    }


    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.header_row, parent, false);
            holder.text = convertView.findViewById(R.id.rowName);

            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        String headerText = "";
        ClsCustomerMaster obj = list_customer.get(position);
        if (obj.getmName() != null && !obj.getmName().equalsIgnoreCase("")) {
            headerText = "" + obj.getmName().charAt(0);
        }

        holder.text.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {

        return list_customer.get(position).getmName().toUpperCase().subSequence(0, 1).charAt(0);
    }

    @Override
    public int getCount() {
        return list_customer.size();
    }

    @Override
    public Object getItem(int position) {
        return list_customer.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        ClsCustomerMaster objVendor = list_customer.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.simple_spinner_item, parent, false);
            holder.text = convertView.findViewById(R.id.pagename);
            holder.pageid = convertView.findViewById(R.id.pageid);
            holder.number = convertView.findViewById(R.id.number);
            holder.number.setVisibility(View.GONE);
            holder.Linear_layout = convertView.findViewById(R.id.Linear_layout);

//            holder.number.setVisibility(View.VISIBLE);
            holder.pagelink = convertView.findViewById(R.id.pagelink);
            holder.Bind(objVendor, mOnClickCustomerAdapter);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (objVendor.getmName() != null &&
                !objVendor.getmName().equalsIgnoreCase("")) {

            holder.text.setText(objVendor.getmName().toUpperCase() + " (" + objVendor.getmMobile_No() + ")");
            holder.text.setSelected(true);
        }

        if (objVendor.getmMobile_No() != null &&
                !objVendor.getmMobile_No().equalsIgnoreCase("")) {

            holder.number.setText(objVendor.getmMobile_No());
        }

        return convertView;
    }


    class ViewHolder {
        TextView text, pageid, pagelink, number;
        LinearLayout Linear_layout;

        void Bind(final ClsCustomerMaster customerMaster, OnClickCustomerAdapter onClickCustomerAdapter) {

            Linear_layout.setOnClickListener(v -> {
                onClickCustomerAdapter.OnClick(customerMaster);
            });

        }
    }

    class HeaderViewHolder {
        TextView text;
    }


}
