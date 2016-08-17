package com.buyer.flashfetch.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by kranthikumar_b on 8/17/2016.
 */
public class ShopSpecificDealsViewPagerAdapter extends FragmentStatePagerAdapter{

    private String[] tabTitles;
    private List<Fragment> shopSpecificDealsFragment;

    public ShopSpecificDealsViewPagerAdapter(FragmentManager fm, String[] tabTitles) {
        super(fm);
        this.tabTitles = tabTitles;
    }

    public void setFragmentList(List<Fragment> fragmentList){
        shopSpecificDealsFragment = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        if(shopSpecificDealsFragment != null) {
            return shopSpecificDealsFragment.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
