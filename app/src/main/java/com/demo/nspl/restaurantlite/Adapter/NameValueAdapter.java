package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.demo.nspl.restaurantlite.classes.ClsNameValue;
import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desktop on 4/24/2018.
 */

public class NameValueAdapter extends BaseAdapter {


    List<ClsNameValue> lstClsNameValues = new ArrayList<>();
    Context context;
    private LayoutInflater inflater;


    public NameValueAdapter(Context context, List<ClsNameValue> lstClsNameValues) {
        inflater = LayoutInflater.from(context);
        this.lstClsNameValues = lstClsNameValues;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lstClsNameValues.size();
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
        view = inflater.inflate(R.layout.view_details_dialog, viewGroup, false);

        TextView txt_lable = (TextView) view.findViewById(R.id.txt_lable);
        TextView txt_value = (TextView) view.findViewById(R.id.txt_value);

        ClsNameValue objClsNameValue = lstClsNameValues.get(i);

        txt_lable.setText(objClsNameValue.getName());
        txt_value.setText(objClsNameValue.getValue());

        return view;
    }
}
