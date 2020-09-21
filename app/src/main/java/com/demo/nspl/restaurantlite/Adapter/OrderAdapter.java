package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.demo.nspl.restaurantlite.Interface.OnClickListenerClsItem;

import com.demo.nspl.restaurantlite.classes.ClsItem;
import com.demo.nspl.restaurantlite.classes.ClsTaxItem;
import com.demo.nspl.restaurantlite.classes.ClsTaxes;
import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;


/**
 * Created by Desktop on 5/11/2018.
 */

public class OrderAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    Context context;
    View itemview;
    List<ClsItem> list_item = new ArrayList<>();
    private  List<ClsItem> list_ItemFilter ;
    SQLiteDatabase db;
    AppCompatActivity activity;
    private LayoutInflater inflater;
    static int counter = 1;
    static TextView unitprice;
    TextView itemname, item_id;
    RadioGroup rg;
    RadioButton rbServe, rbParcel;
    static TextView txt_amount;
    static Double price;
    static TextView txt_qty;
    private String formattedDate;
    private List<ClsTaxItem> lstClsTaxItems;
    private List<ClsTaxes> list_tax;
    private TaxesAdapter taxItemAdapter;
    private ClsTaxItem objClsTaxItem;
    static List<ClsTaxItem> listTaxUpdated = new ArrayList<>();
    private int mCount_item = 1;
    private double mget_total_amount;
    private OnClickListenerClsItem mOnClickListenerClsItem;


    public OrderAdapter(Context c, List<ClsItem> order, OnClickListenerClsItem onClickListenerClsItem) {
        this.context = c;
        this.list_item = order;
        this.mOnClickListenerClsItem = onClickListenerClsItem;
        inflater = LayoutInflater.from(c);

    }




    public void UpdateList(List<ClsItem> list) {
        this.list_item.clear();
        this.list_item = list;
        notifyDataSetChanged();
    }



    @Override
    public long getHeaderId(int position) {
        return list_item.get(position).getCategory_name().subSequence(0, 1).charAt(0);
    }

    @Override
    public int getCount() {
        return list_item.size();
    }

    @Override
    public Object getItem(int i) {
        return list_item.get(i);
    }


    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {

            holder = new ViewHolder();
            ClsItem obj = list_item.get(i);
            view = inflater.inflate(R.layout.card_child_orders, viewGroup, false);
            holder.item_price = view.findViewById(R.id.item_price);
            holder.linear = view.findViewById(R.id.linear);
            holder.item_name = view.findViewById(R.id.item_name);
            holder.veg_non = view.findViewById(R.id.veg_non);
            holder.Bind(obj, mOnClickListenerClsItem);

            holder.item_name.setText(obj.getItem_name());
            holder.item_price.setText(String.valueOf(obj.getPrice()));
                Log.e("list_Item", obj.getItem_name());
            if (obj.getFood_type().equalsIgnoreCase("VEG")) {
                holder.veg_non.setImageResource(R.drawable.vegetarian);
            } else {
                holder.veg_non.setImageResource(R.drawable.nonvegetarian);
            }
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        return view;

    }



    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        HeaderViewHolder holder;
        ClsItem objOrder = new ClsItem();
        objOrder = list_item.get(position);

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.card_orders, parent, false);
            holder.txt_cat_name = convertView.findViewById(R.id.txt_cat_name);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        String headerText = "" + objOrder.getCategory_name();
        holder.txt_cat_name.setText(headerText);


        return convertView;
    }

    class ViewHolder {
        TextView item_name, item_price;
        ImageButton veg_non;
        LinearLayout linear;

        void Bind(final ClsItem clsItem, OnClickListenerClsItem onClickListenerClsItem) {
            linear.setOnClickListener(v ->
                    // send current position via Onclick method.
                    onClickListenerClsItem.OnClick(clsItem));
        }
    }

    class HeaderViewHolder {
        TextView txt_cat_name;
    }
}
