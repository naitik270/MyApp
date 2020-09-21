package com.demo.nspl.restaurantlite.Interface;

import com.demo.nspl.restaurantlite.Adapter.FilterAdpater;
import com.demo.nspl.restaurantlite.classes.ClsItemFilter;

import java.util.List;

public interface OnClickFilter {

    void OnClickFilterItem(FilterAdpater.Myviewholder myviewholder,
                           ClsItemFilter.LayerItem currentObj,
                           int position
            , List<ClsItemFilter.LayerItem> itemList);
}
