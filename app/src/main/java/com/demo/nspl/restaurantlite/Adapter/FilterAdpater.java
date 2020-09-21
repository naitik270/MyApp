package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Interface.OnClickFilter;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.classes.ClsItemFilter;

import java.util.ArrayList;
import java.util.List;

public class FilterAdpater extends RecyclerView.Adapter<FilterAdpater.Myviewholder> {

    Context c;
    private View itemview;
    List<ClsItemFilter.LayerItem> _itemList = new ArrayList<ClsItemFilter.LayerItem>();
    private boolean isSelected[];
    String title;
    public static List<String> selectedItems;

    private OnClickFilter mOnClickFilter;


    public FilterAdpater(Context c, List<ClsItemFilter.LayerItem> _itemList, String title) {
        this.title = title;
        this.c = c;
        this._itemList = _itemList;
        isSelected = new boolean[_itemList.size()];
    }


    public void SetOnClickListener(OnClickFilter clickFilter) {
        this.mOnClickFilter = clickFilter;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_list, parent, false);
        FilterAdpater.Myviewholder myViewHolder = new FilterAdpater.Myviewholder(itemview);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
        ClsItemFilter.LayerItem current = _itemList.get(position);

       /* if (position == 3) {
            current.setSelected(true);
        }
*/

        holder.txt_label.setText(current.getItemName().toUpperCase());
        holder.txt_label.setChecked(current.isSelected());
        if (holder.txt_label.isChecked()) {
            holder.linear_layout.setBackgroundColor(Color.parseColor("#FFCDD2D2"));
            holder.ic_Check.setColorFilter(ContextCompat.getColor(c, R.color.red_dark), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else {
            holder.ic_Check.setColorFilter(ContextCompat.getColor(c, R.color.tint_checkImageView), android.graphics.PorterDuff.Mode.MULTIPLY);
            holder.linear_layout.setBackgroundResource(0);
        }
        holder.Bind(holder, current, mOnClickFilter, position,_itemList);
    }

    public List<String> SelectedList() {
        return selectedItems;
    }

    @Override
    public int getItemCount() {
        return _itemList.size();
    }

    public static class Myviewholder extends RecyclerView.ViewHolder {

        public LinearLayout linear_layout;
        public CheckedTextView txt_label;
        public ImageView ic_Check;


        Myviewholder(View itemView) {
            super(itemView);
            linear_layout = itemView.findViewById(R.id.linear_layout);
            txt_label = itemView.findViewById(R.id.txt_label);
            ic_Check = itemView.findViewById(R.id.ic_Check);

        }

        void Bind(final Myviewholder myviewholder, ClsItemFilter.LayerItem currentObj, OnClickFilter onClickFilter, int position, List<ClsItemFilter.LayerItem> layerItems) {
            linear_layout.setOnClickListener(v ->
                    // send current position via Onclick method.

                    //on click get currunt object
                    //get position and send that position to on click listener
                    //int _POS = _itemList.indexOf(currentObj);


                    onClickFilter.OnClickFilterItem(myviewholder, currentObj, layerItems.indexOf(currentObj), layerItems));
        }
    }


    public void updateList(List<ClsItemFilter.LayerItem> list) {
        _itemList = list;
        notifyDataSetChanged();
    }
}
