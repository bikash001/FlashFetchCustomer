package com.buyer.flashfetch;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.buyer.flashfetch.Adapters.NearByDealsViewPagerAdapter;
import com.buyer.flashfetch.Animations.DepthPageTransformer;
import com.buyer.flashfetch.Animations.ZoomOutPageTransformer;
import com.buyer.flashfetch.Fragments.NearByDealsFragment;

import java.util.ArrayList;

public class DealsNearByActivity extends BaseActivity {

    private static final String TAG = "OfferNearByActivity";

//    public static int TAB_TRENDING = 1000;
    public static int TAB_SHOPPING = 1001;
    public static int TAB_FOOD = 1002;
    public static int TAB_ENTERTAINMENT = 1003;
    public static int TAB_SERVICES = 1004;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NearByDealsViewPagerAdapter dealsViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_offer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.nearby_deals_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("NearBy Deals");

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("NearBy Deals");
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setHomeButtonEnabled(true);
        }

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        setUpDataModel();

        tabLayout = (TabLayout)findViewById(R.id.deal_nearby_tab_layout);
        viewPager = (ViewPager)findViewById(R.id.deals_nearby_view_pager);

        if (viewPager != null) {
            viewPager.setAdapter(dealsViewPagerAdapter);
            viewPager.setPageTransformer(false,new ZoomOutPageTransformer());
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private void setUpDataModel() {
        String[] tabTitles = {"SHOPPING","FOOD","ENTERTAINMENT","SERVICES"};

        if(viewPager != null && viewPager.getAdapter() != null){
            viewPager.removeAllViews();
        }

        ArrayList<Fragment> fragmentsList = new ArrayList<>();

//        fragmentsList.add(NearByDealsFragment.getInstance(TAB_TRENDING));
        fragmentsList.add(NearByDealsFragment.getInstance(TAB_SHOPPING));
        fragmentsList.add(NearByDealsFragment.getInstance(TAB_FOOD));
        fragmentsList.add(NearByDealsFragment.getInstance(TAB_ENTERTAINMENT));
        fragmentsList.add(NearByDealsFragment.getInstance(TAB_SERVICES));

        FragmentManager fragmentManager = getSupportFragmentManager();
        dealsViewPagerAdapter = new NearByDealsViewPagerAdapter(fragmentManager,tabTitles);
        dealsViewPagerAdapter.setFragmentList(fragmentsList);
    }
}
