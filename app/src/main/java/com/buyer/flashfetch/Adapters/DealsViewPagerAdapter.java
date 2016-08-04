package com.buyer.flashfetch.Adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.buyer.flashfetch.Fragments.DealsNearByFragment;

import java.util.List;

/**
 * Created by kranthikumar_b on 7/1/2016.
 */
public class DealsViewPagerAdapter extends FragmentStatePagerAdapter {

    private String[] tabTitles;
    private List<Fragment> dealsNearByFragmentList;

    public DealsViewPagerAdapter(FragmentManager fm, String[] tabTitles) {
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
        return dealsNearByFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
