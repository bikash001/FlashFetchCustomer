package com.buyer.flashfetch.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class NearByDealsViewPagerAdapter extends FragmentStatePagerAdapter {

    private String[] tabTitles;
    private List<Fragment> dealsNearByFragmentList;

    public NearByDealsViewPagerAdapter(FragmentManager fm, String[] tabTitles) {
        super(fm);
        this.tabTitles = tabTitles;
    }

    public void setFragmentList(List<Fragment> list){
        this.dealsNearByFragmentList = list;
    }

    @Override
    public Fragment getItem(int position) {
        if(dealsNearByFragmentList != null) {
            return dealsNearByFragmentList.get(position);
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
