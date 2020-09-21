package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.nspl.restaurantlite.Global.ClsGlobal;
import com.demo.nspl.restaurantlite.classes.ClsTaxItem;
import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desktop on 6/5/2018.
 */

public class MainTaxAdapter extends BaseAdapter {
    Context context;
    private LayoutInflater inflater = null;
    private List<ClsTaxItem> lsClsTaxItems = new ArrayList<>();

    public MainTaxAdapter(Context context, List<ClsTaxItem> lsClsTaxItems) {
        super();
        this.lsClsTaxItems = lsClsTaxItems;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return lsClsTaxItems.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return lsClsTaxItems.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder;


        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.row_main_tax_items, viewGroup, false);
            holder = new ViewHolder();

            holder.ll_layout = convertView.findViewById(R.id.ll_layout);
            holder.txt_type = convertView.findViewById(R.id.txt_type);
            holder.txt_label = convertView.findViewById(R.id.txt_label);
            holder.txt_value = convertView.findViewById(R.id.txt_value);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ClsTaxItem objTax = lsClsTaxItems.get(position);

        holder.txt_type.setText(objTax.getType());
        holder.txt_label.setText(objTax.getTaxName());
        Log.e("Label", objTax.getTaxName());
        holder.txt_value.setText("" + ClsGlobal.round(objTax.get_TaxAmount(),2));

        if (objTax.get_TaxAmount() > 0) {
            Log.e("taxVal", "MainTax-->>" + objTax.get_TaxAmount());
        }


        return convertView;
    }


    static class ViewHolder {
        LinearLayout ll_layout;
        TextView txt_type;
        TextView txt_label;

        TextView txt_value;

    }

    void setTaxValue(int _position, String _txtValue) {
        for (ClsTaxItem OBJsize : lsClsTaxItems) {
            if (_position == lsClsTaxItems.indexOf(OBJsize)) {
                OBJsize.set_TaxAmount(Double.valueOf(_txtValue));
            }
            lsClsTaxItems.set(lsClsTaxItems.indexOf(OBJsize), OBJsize);
        }
//        setTaxValueForSave(lsClsTaxItems);
    }
}
