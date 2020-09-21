package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsUserInfo;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {


    private static final String TAG = "ExpandableListAdapter";
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    ClsUserInfo objClsUserInfo = new ClsUserInfo();


    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_childview, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        ImageView iv_child = convertView
                .findViewById(R.id.iv_child);
        iv_child.setImageResource(R.drawable.ic_arrow);


        if (childText.equalsIgnoreCase("Retail View")) {
            txtListChild.setText("Retail View");
        } else if ((childText.equalsIgnoreCase("Table View"))) {
            txtListChild.setText("Table View");
        } else if ((childText.equalsIgnoreCase("Category"))) {
            txtListChild.setText("Category");
        } else if ((childText.equalsIgnoreCase("Dashboard_Reservation"))) {
            txtListChild.setText("Reservation");
        } else if ((childText.equalsIgnoreCase("Dashboard_Checkout Report"))) {
            txtListChild.setText("Checkout Report");
        } else {
            txtListChild.setText(childText);
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerValue = (String) getGroup(groupPosition);
        Log.e(TAG, "headerValue: " + headerValue);
        String[] headerList = headerValue.split("~");

        Log.e(TAG, "headerValue: " + headerList[0]);
        Log.e(TAG, "headerValue: " + headerList[1]);
        String hasChiled = headerList[0];
        String headerTitle = headerList[1];

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_parentview, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);

        ImageView iv_icon = convertView
                .findViewById(R.id.iv_icon);

        objClsUserInfo = ClsGlobal.getUserInfo(_context);
        Log.e("--Login--", "Adapter: " + objClsUserInfo.getLicenseType());


        if (headerValue.equalsIgnoreCase("NO~Pos")) {
            iv_icon.setImageResource(R.drawable.ic_order_desk);
        } else if (headerValue.equalsIgnoreCase("NO~Purchase")) {
            iv_icon.setImageResource(R.drawable.ic_purchase);
        } else if (headerValue.equalsIgnoreCase("YES~Inventory")) {
            iv_icon.setImageResource(R.drawable.ic_inventory);
        } else if (headerValue.equalsIgnoreCase("YES~Master")) {
            iv_icon.setImageResource(R.drawable.ic_master);
        } else if (headerValue.equalsIgnoreCase("YES~Expenses")) {
            iv_icon.setImageResource(R.drawable.ic_expenses);
        } else if (headerValue.equalsIgnoreCase("YES~Reports")) {
            iv_icon.setImageResource(R.drawable.ic_report);
        } else if (headerValue.equalsIgnoreCase("YES~Sms")) {
            iv_icon.setImageResource(R.drawable.ic_sms);
        } else if (headerValue.equalsIgnoreCase("NO~Settings")) {
            iv_icon.setImageResource(R.drawable.ic_setting);
        } else if (headerValue.equalsIgnoreCase("NO~Premium")) {
            iv_icon.setImageResource(R.drawable.ic_primum);
        } else if (headerValue.equalsIgnoreCase("NO~About")) {
            iv_icon.setImageResource(R.drawable.ic_about);
        } else if (headerValue.equalsIgnoreCase("NO~OverView")) {
            iv_icon.setImageResource(R.drawable.ic_overview);
        } else if (headerValue.equalsIgnoreCase("NO~Share App")) {
            iv_icon.setImageResource(R.drawable.ic_menu_nav_share);
        }

        TextView arrow = convertView
                .findViewById(R.id.arrow);

        if (hasChiled.equalsIgnoreCase("yes")) {
            if (isExpanded) {
                arrow.setVisibility(View.VISIBLE);
                arrow.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_up_black_24dp, 0);
            } else {
                arrow.setVisibility(View.VISIBLE);
                arrow.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_drawer_down_arrow, 0);
            }
        } else {
            arrow.setVisibility(View.GONE);
        }

        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
