package com.buyer.flashfetch.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.buyer.flashfetch.Fragments.FoodDealsFragment;
import com.buyer.flashfetch.Fragments.ServicesDealsFragment;
import com.buyer.flashfetch.Fragments.ShoppingDealsFragment;

import java.util.List;

public class NearByDealsViewPagerAdapter extends FragmentStatePagerAdapter {

    private String[] tabTitles;
    private List<Fragment> dealsNearByFragmentList;

    public NearByDealsViewPagerAdapter(FragmentManager fm, String[] tabTitles) {
        super(fm);
        this.tabTitles = tabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ShoppingDealsFragment();
            case 1:
                return new FoodDealsFragment();
            case 2:
                return new ServicesDealsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
