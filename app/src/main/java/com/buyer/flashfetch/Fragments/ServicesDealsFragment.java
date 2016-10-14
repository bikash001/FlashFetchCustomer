package com.buyer.flashfetch.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.buyer.flashfetch.Adapters.NearByDealsAdapter;
import com.buyer.flashfetch.CommonUtils.Toasts;
import com.buyer.flashfetch.CommonUtils.Utils;
import com.buyer.flashfetch.Constants.IEventConstants;
import com.buyer.flashfetch.Constants.NearByDealsConstants;
import com.buyer.flashfetch.Interfaces.UIListener;
import com.buyer.flashfetch.Network.ServiceManager;
import com.buyer.flashfetch.Objects.IEvent;
import com.buyer.flashfetch.Objects.NearByDealsDataModel;
import com.buyer.flashfetch.R;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * Created by KRANTHI on 06-10-2016.
 */
public class ServicesDealsFragment extends Fragment {

    private static final String SERVICES_DEALS = "service_deals";

    private RecyclerView recyclerView;
    private NearByDealsAdapter nearByDealsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog progressDialog;

    private ArrayList<NearByDealsDataModel> servicesDeals = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.deals_tab_layout, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.deals_refresh_container);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                setNearByDealsAdapter();
                nearByDealsAdapter.notifyDataSetChanged();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 3000);
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.ff_green, R.color.ff_black);

        TypedValue typedValue = new TypedValue();
        int actionBarHeight = 0;
        if (getActivity().getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
        }

        swipeRefreshLayout.setProgressViewEndTarget(true, actionBarHeight);

        recyclerView = (RecyclerView) view.findViewById(R.id.deals_tab_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        setNearByDealsAdapter();

        return view;
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        if (savedInstanceState != null) {
//            servicesDeals = (ArrayList<NearByDealsDataModel>) savedInstanceState.getSerializable(SERVICES_DEALS);
//        } else {
//            setUpData();
//        }
//    }

    private void setNearByDealsAdapter() {

        if(servicesDeals == null){
            servicesDeals = NearByDealsDataModel.getDeals(getContext(), NearByDealsConstants.SERVICES + "");
        }

        nearByDealsAdapter = new NearByDealsAdapter(getContext(), servicesDeals);

        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(nearByDealsAdapter);
        alphaInAnimationAdapter.setInterpolator(new OvershootInterpolator());
        alphaInAnimationAdapter.setDuration(2000);
        alphaInAnimationAdapter.setFirstOnly(false);

        recyclerView.setAdapter(new ScaleInAnimationAdapter(alphaInAnimationAdapter));
    }

//    public void showProgressDialog() {
//        progressDialog = new ProgressDialog(getContext());
//        progressDialog.setTitle("Loading...");
//        progressDialog.setMessage("Please wait...");
//        progressDialog.setCancelable(false);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//
//        progressDialog.show();
//    }
//
//    private void hideProgressDialog() {
//        if (progressDialog != null && progressDialog.isShowing()) {
//            progressDialog.dismiss();
//        }
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(SERVICES_DEALS, servicesDeals);
//    }
}
