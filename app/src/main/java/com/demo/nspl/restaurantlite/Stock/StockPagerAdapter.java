package com.demo.nspl.restaurantlite.Stock;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class StockPagerAdapter extends FragmentPagerAdapter {

    String itemCode;
    List<Fragment> mFragmentList = new ArrayList<>();
    List<String> mFragmentTitleList = new ArrayList<>();


    public StockPagerAdapter(FragmentManager fm, String itemCode) {
        super(fm);
        this.itemCode = itemCode;

    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
