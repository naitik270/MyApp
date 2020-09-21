package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.activity.AddItemActivity;
import com.demo.nspl.restaurantlite.classes.ClsInventoryLayer;

import java.util.ArrayList;
import java.util.List;

public class LayerNameAdapterNew extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    List<ClsInventoryLayer> layerList = new ArrayList<>();


    public LayerNameAdapterNew(Context context, List<ClsInventoryLayer> layerList) {
        this.context = context;
        this.layerList = layerList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return layerList.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return layerList.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.layer_name_list, viewGroup, false);
        final ClsInventoryLayer _obj = layerList.get(position);
        TextView txt_Layer_name = view.findViewById(R.id.txt_Layer_name);
        EditText Edit_Txt_Layer_Namer = view.findViewById(R.id.Edit_Txt_Layer_Namer);
        txt_Layer_name.setText(_obj.getInventoryLayerName() + ": ");
        Edit_Txt_Layer_Namer.setText(_obj.getLayerValue());

        Edit_Txt_Layer_Namer.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String txtValue = "";
                if (s != null && !s.toString().isEmpty() && s.toString() != "") {
                    txtValue = s.toString();
                }
                //update to activity LIST
                setCustomerInfo(position, _obj.getInventoryLayerName(), txtValue);
            }
        });
        return view;
    }


    void setCustomerInfo(int _position, String layerName, String value) {
        for (ClsInventoryLayer OBJLayer : layerList) {
            if (_position == layerList.indexOf(OBJLayer)) {
                if (layerName.equalsIgnoreCase(OBJLayer.getInventoryLayerName())) {
                    OBJLayer.setLayerValue(value);
                }
                layerList.set(_position, OBJLayer);
            }
        }
        AddItemActivity.updateList(layerList);
    }

}
