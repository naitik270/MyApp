package com.demo.nspl.restaurantlite.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.demo.nspl.restaurantlite.R;

import java.util.List;

public class TabAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private final List<String> data;
    private final ListView listView;
    private OnItemClickListener listener;

    private int currentSelected = 0;

    public TabAdapter(List<String> data, ListView listView) {
        this(data, listView, null);
    }


    public TabAdapter(List<String> data, ListView listView, OnItemClickListener listener) {
        this.data = data;
        this.listView = listView;
        this.listener = listener;

        listView.setOnItemClickListener(this);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    //generate tab titles/pagess
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // Currently not using viewHolder pattern cause there aren't too many tabs in the demo project

        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tab, viewGroup, false);
        }

        TextView tabTitle = view.findViewById(R.id.txt_tab_title);
        tabTitle.setText(data.get(i).toUpperCase());

        if (i == currentSelected) {
            setTextViewToSelected(tabTitle);
        } else {
            setTextViewToUnSelected(tabTitle);
        }

        return view;
    }


    /**
     * Return item view at the given position or null if position is not visible.
     */
    public View getViewByPosition(int pos) {
        if (listView == null) {
            return null;
        }
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return null;
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }


    private void setTextViewToSelected(TextView tabTitle) {
        tabTitle.setTextColor(Color.BLACK);
    }

    private void setTextViewToUnSelected(TextView tabTitle) {
        tabTitle.setTextColor(Color.GRAY);
    }


    private void select(int position) {
        if (currentSelected >= 0) {
            deselect(currentSelected);
        }

        View targetView = getViewByPosition(position);
        if (targetView != null) {
            setTextViewToSelected((targetView.findViewById(R.id.txt_tab_title)));
        }

        if (listener != null) {
            listener.selectItem(position);
        }

        currentSelected = position;

    }

    private void deselect(int position) {
        if (getViewByPosition(position) != null) {
            View targetView = getViewByPosition(position);
            if (targetView != null) {
                setTextViewToUnSelected((TextView) (targetView.findViewById(R.id.txt_tab_title)));
            }
        }

        currentSelected = -1;
    }


    // OnClick Events

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        select(i);
    }


    public void OnItemClickListener(TabAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setCurrentSelected(int i) {
        select(i);
    }

    public interface OnItemClickListener {
        void selectItem(int position);
    }
}