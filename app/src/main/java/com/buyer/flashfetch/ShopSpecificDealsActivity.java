package com.buyer.flashfetch;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.buyer.flashfetch.Adapters.ShopSpecificDealsViewPagerAdapter;
import com.buyer.flashfetch.Fragments.NearByDealsFragment;
import com.buyer.flashfetch.Fragments.ShopSpecificDealsFragment;

import java.util.ArrayList;

/**
 * Created by kranthikumar_b on 7/1/2016.
 */
public class ShopSpecificDealsActivity extends BaseActivity {

    private Context context;
    private int shopId;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ShopSpecificDealsViewPagerAdapter shopSpecificDealsViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = ShopSpecificDealsActivity.this;

        setContentView(R.layout.activity_specific_deals);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            //TODO: assign shop id from intent bundle
        }

        Toolbar toolbar = (Toolbar)findViewById(R.id.specific_deals_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Deals");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        if(tabLayout != null){
            tabLayout.setTabTextColors(R.color.ff_black,R.color.ff_green);
        }

        if (viewPager != null) {
            viewPager.setAdapter(shopSpecificDealsViewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private void setUpDataModel() {

        //TODO: make service call and setup this data model

        String[] tabTitles = {"TRENDING","SHOPPING","FOOD","ENTERTAINMENT","SERVICES"};

        if(viewPager != null && viewPager.getAdapter() != null){
            viewPager.removeAllViews();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        shopSpecificDealsViewPagerAdapter = new ShopSpecificDealsViewPagerAdapter(fragmentManager,tabTitles);

        ArrayList<Fragment> fragmentsList = new ArrayList<>();

        for(int i = 0; i< tabTitles.length; i++){
            fragmentsList.add(ShopSpecificDealsFragment.getInstance(i));
        }

        shopSpecificDealsViewPagerAdapter.setFragmentList(fragmentsList);
    }
}
