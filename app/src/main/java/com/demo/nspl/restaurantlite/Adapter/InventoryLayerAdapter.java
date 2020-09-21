package com.demo.nspl.restaurantlite.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.demo.nspl.restaurantlite.classes.ClsInventoryLayer;
import com.demo.nspl.restaurantlite.R;


import java.util.ArrayList;
import java.util.List;

public class InventoryLayerAdapter extends ArrayAdapter<ClsInventoryLayer> {

    Context context;
    List<ClsInventoryLayer> list = new ArrayList<>();

    public InventoryLayerAdapter(Context context, int resource, List<ClsInventoryLayer> list) {
        super(context, resource ,list);
        this.context = context;
        this.list = list;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());

        ClsInventoryLayer get = getItem(position);

        @SuppressLint("ViewHolder") View rowView= inflater.inflate(R.layout.dialog_inventorylayer, null, true);
        TextView txtTitle = rowView.findViewById(R.id.title);

        Log.e("txtTitle",get.getInventoryLayerName());
        txtTitle.setText(get.getInventoryLayerName());

        return rowView;
    }

}
