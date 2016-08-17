package com.buyer.flashfetch.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.buyer.flashfetch.Fragments.Accepted;
import com.buyer.flashfetch.Fragments.Requested;
import com.buyer.flashfetch.Objects.Request;

import java.util.ArrayList;

public class RequestsPagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles = {"Requested", "Accepted"};

    public RequestsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new Requested();
            case 1:
                return new Accepted();
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
