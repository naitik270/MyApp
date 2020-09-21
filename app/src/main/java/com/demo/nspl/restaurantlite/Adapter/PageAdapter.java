package com.demo.nspl.restaurantlite.Adapter;


import com.demo.nspl.restaurantlite.Navigation_Drawer.PagerFragmet;
import com.demo.nspl.restaurantlite.classes.ClsItemFilter;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {


   // List<String> data;
    //List<String> data;
    //List<ClsItemFilter.LayerItem> itemList;
    List<ClsItemFilter.Layer> data = new ArrayList<>();

    //public PageAdapter(FragmentManager fm, List<String> data,List<ClsItemFilter.LayerItem> _itemList ) {
    public PageAdapter(FragmentManager fm,  List<ClsItemFilter.Layer> filterList ) {
        super(fm);
        this.data = filterList;
       // this.itemList = _itemList;
    }

    @Override
    public Fragment getItem(int position) {
        return PagerFragmet.newInstance(data.get(position).getLayerName(), position, data.get(position).getLayerItemList());
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
