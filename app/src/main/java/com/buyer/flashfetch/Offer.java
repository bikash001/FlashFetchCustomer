package com.buyer.flashfetch;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.buyer.flashfetch.Adapters.DealsViewPagerAdapter;
import com.buyer.flashfetch.Constants.DealsConstants;
import com.buyer.flashfetch.Fragments.DealsNearByFragment;

import java.util.ArrayList;

public class Offer extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DealsViewPagerAdapter dealsViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("NearBy Deals");

        setUpDataModel();

        tabLayout = (TabLayout)findViewById(R.id.deal_nearby_tab_layout);
        viewPager = (ViewPager)findViewById(R.id.deals_nearby_view_pager);

        if(tabLayout != null){
            tabLayout.setTabTextColors(R.color.secondary_text,R.color.icons);
        }

        viewPager.setAdapter(dealsViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }

    private void setUpDataModel() {
        String[] tabTitles = {"TRENDING","SHOPPING","FOOD","ENTERTAINMENT","SERVICES"};

        if(viewPager != null && viewPager.getAdapter() != null){
            viewPager.removeAllViews();
        }

        FragmentManager fm = getSupportFragmentManager();
        dealsViewPagerAdapter = new DealsViewPagerAdapter(fm,tabTitles);

        ArrayList<Fragment> fragmentsList = new ArrayList<>();
        fragmentsList.add(DealsNearByFragment.getInstance(DealsConstants.TAB_TRENDING));
        fragmentsList.add(DealsNearByFragment.getInstance(DealsConstants.TAB_SHOPPING));
        fragmentsList.add(DealsNearByFragment.getInstance(DealsConstants.TAB_FOOD));
        fragmentsList.add(DealsNearByFragment.getInstance(DealsConstants.TAB_ENTERTAINMENT));
        fragmentsList.add(DealsNearByFragment.getInstance(DealsConstants.TAB_SERVICES));

        dealsViewPagerAdapter.setFragmentList(fragmentsList);
    }
}
