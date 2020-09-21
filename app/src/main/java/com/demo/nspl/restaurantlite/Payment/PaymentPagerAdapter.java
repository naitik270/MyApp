package com.demo.nspl.restaurantlite.Payment;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PaymentPagerAdapter extends FragmentPagerAdapter {

    int vendorId;
    List<Fragment> mFragmentList = new ArrayList<>();
    List<String> mFragmentTitleList = new ArrayList<>();


    public PaymentPagerAdapter(FragmentManager fm, int vendorId) {
        super(fm);
        this.vendorId = vendorId;
        Log.d("--VendorId--", "Adapter: " + vendorId);
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
