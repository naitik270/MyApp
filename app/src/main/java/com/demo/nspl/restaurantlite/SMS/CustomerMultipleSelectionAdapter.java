package com.demo.nspl.restaurantlite.SMS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.nspl.restaurantlite.Interface.OnClickCustomerAdapter;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsCustomerMaster;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class CustomerMultipleSelectionAdapter
        extends BaseAdapter implements StickyListHeadersAdapter {

    Context context;
    List<ClsCustomerMaster> list_customer = new ArrayList<>();
    private StickyListHeadersListView lst;
    private LayoutInflater inflater;

    private boolean isSelected[];


    private OnClickCustomerAdapter mOnClickCustomerAdapter;

    public CustomerMultipleSelectionAdapter(Context context, List<ClsCustomerMaster> list_customer) {
        this.context = context;
        this.list_customer = list_customer;
        isSelected = new boolean[list_customer.size()];
        inflater = LayoutInflater.from(context);

    }


    public void AddItems(List<ClsCustomerMaster> list_customer) {
        this.list_customer = list_customer;
        notifyDataSetChanged();

    }



/*



        public void SetOnClickListener(OnClickCustomerAdapter onClickCustomerAdapter) {
            this.mOnClickCustomerAdapter = onClickCustomerAdapter;
        }

*/

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        ClsCustomerMaster objVendor = list_customer.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row_multiple_select_cust, parent, false);
            holder.text = convertView.findViewById(R.id.pagename);
            holder.number = convertView.findViewById(R.id.number);
            holder.Linear_layout = convertView.findViewById(R.id.Linear_layout);
            holder.iv_done = convertView.findViewById(R.id.iv_done);
            holder.ckb_name_selection = convertView.findViewById(R.id.ckb_name_selection);

            holder.number.setVisibility(View.VISIBLE);


            holder.Linear_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean flag = holder.ckb_name_selection.isChecked();
                    holder.ckb_name_selection.setChecked(!flag);
                    isSelected[position] = !isSelected[position];
                }
            });

            holder.ckb_name_selection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isSelected[position] = !isSelected[position];
                }
            });


//                holder.Bind(objVendor, mOnClickCustomerAdapter);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (objVendor.getmName() != null &&
                !objVendor.getmName().equalsIgnoreCase("")) {

            holder.text.setText(objVendor.getmName().toUpperCase());
        }

        if (objVendor.getmMobile_No() != null &&
                !objVendor.getmMobile_No().equalsIgnoreCase("")) {

            holder.number.setText(objVendor.getmMobile_No());
        }

        return convertView;
    }


    class ViewHolder {
        TextView text, number;
        ImageView iv_done;
        LinearLayout Linear_layout;
        CheckBox ckb_name_selection;

//            void Bind(final ClsCustomerMaster customerMaster, OnClickCustomerAdapter onClickCustomerAdapter) {
//
//                Linear_layout.setOnClickListener(v -> {
//                    onClickCustomerAdapter.OnClick(customerMaster);
//                });
//
//            }

    }

    class HeaderViewHolder {
        TextView text;
    }

    public boolean[] getSelectedFlags() {
        return isSelected;
    }

}

