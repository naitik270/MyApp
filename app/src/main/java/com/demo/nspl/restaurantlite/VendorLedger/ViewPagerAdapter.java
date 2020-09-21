package com.demo.nspl.restaurantlite.VendorLedger;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    int vendorId;
    List<Fragment> mFragmentList = new ArrayList<>();
    List<String> mFragmentTitleList = new ArrayList<>();


    public ViewPagerAdapter(FragmentManager fm, int vendorId) {
        super(fm);
        this.vendorId = vendorId;
        Log.d("--VendorId--", "Adapter: " + vendorId);
    }


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
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
