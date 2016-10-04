package com.buyer.flashfetch.Fragments;

import android.app.ProgressDialog;
import android.content.res.Resources;
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

import com.buyer.flashfetch.Adapters.ShopSpecificDealsAdapter;
import com.buyer.flashfetch.Adapters.NearByDealsAdapter;
import com.buyer.flashfetch.BaseFragment;
import com.buyer.flashfetch.CommonUtils.Toasts;
import com.buyer.flashfetch.CommonUtils.Utils;
import com.buyer.flashfetch.Constants.Constants;
import com.buyer.flashfetch.Constants.NearByDealsConstants;
import com.buyer.flashfetch.Interfaces.UIResponseListener;
import com.buyer.flashfetch.Network.ServiceManager;
import com.buyer.flashfetch.Objects.NearByDealsDataModel;
import com.buyer.flashfetch.Objects.UserProfile;
import com.buyer.flashfetch.R;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;

/**
 * Created by kranthikumar_b on 7/1/2016.
 */
public class NearByDealsFragment extends BaseFragment {

    private int uniqueID;

    private RecyclerView recyclerView;
    private NearByDealsAdapter nearByDealsAdapter;
    private ArrayList<NearByDealsDataModel> list = new ArrayList<>();
    private ProgressDialog progressDialog;

    private String[] imagesURL = new String[]{
            "https://s12.postimg.org/v018dmc71/510x300.png",
            "https://s11.postimg.org/r95e2q8vj/1020x400.png",
            "https://s11.postimg.org/nqte6c7zj/1020x510.png",
            "https://s11.postimg.org/yrt4p3pm7/1020x550.png",
            "https://s11.postimg.org/7v95gs6sv/1020x600.png",
            "https://s11.postimg.org/ni0eu5kkv/1020x1020.png",
            "https://s11.postimg.org/lrhdso31r/1200x600.png",
            "https://s11.postimg.org/frtmp0i9b/1800x900.png",
            "https://s11.postimg.org/s7qcirblb/1920x1080_1.jpg",
            "https://s11.postimg.org/lvb78x8j3/1920x1920.png"
    };
    private SwipeRefreshLayout swipeRefreshLayout;

    public static NearByDealsFragment getInstance(int tabId) {

        NearByDealsFragment fragment = new NearByDealsFragment();
        fragment.setUniqueID(tabId);

        return fragment;
    }

    public int getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(int uniqueID) {
        this.uniqueID = uniqueID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpDataModel();
        progressDialog = getProgressDialog(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.deals_tab_layout, container, false);

        nearByDealsAdapter = new NearByDealsAdapter(getContext(), list);

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.deals_refresh_container);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                setUpDataModel();
                nearByDealsAdapter.notifyDataSetChanged();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(swipeRefreshLayout.isRefreshing()){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },3000);
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.ff_green,R.color.ff_black);

        TypedValue typedValue = new TypedValue();
        int actionBarHeight = 0;
        if (getActivity().getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
        }

        swipeRefreshLayout.setProgressViewEndTarget(true, actionBarHeight);

        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(nearByDealsAdapter);
        alphaInAnimationAdapter.setInterpolator(new OvershootInterpolator());
        alphaInAnimationAdapter.setDuration(2000);
        alphaInAnimationAdapter.setFirstOnly(false);

        recyclerView = (RecyclerView) view.findViewById(R.id.deals_tab_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new ScaleInAnimationAdapter(alphaInAnimationAdapter));

        return view;
    }

    private void setUpDataModel() {

        if(Utils.isInternetAvailable(getContext())){
            progressDialog.show();

            ServiceManager.callFetchDealsService(getContext(), UserProfile.getProfileId(getContext()), new UIResponseListener<ArrayList<NearByDealsDataModel>>() {
                @Override
                public void onSuccess(ArrayList<NearByDealsDataModel> responseObj) {
                    progressDialog.dismiss();
                    if(responseObj != null && responseObj.size() > 0) {
                        list = responseObj;
                    }
                }

                @Override
                public void onFailure() {
                    progressDialog.dismiss();
                    Toasts.serverBusyToast(getContext());
                }

                @Override
                public void onConnectionError() {
                    Toasts.serverBusyToast(getContext());
                }

                @Override
                public void onCancelled() {
                    Toasts.serverBusyToast(getContext());
                }
            });

        }else{
            Toasts.internetUnavailableToast(getContext());
        }

        for(int i = 0; i < 5; i++) {
            NearByDealsDataModel nearByDealsDataModel = new NearByDealsDataModel();

            nearByDealsDataModel.setDealsCategory(NearByDealsConstants.SHOPPING);
            nearByDealsDataModel.setDealsType(NearByDealsConstants.PICKUP_DEALS);
            nearByDealsDataModel.setImageUrl(imagesURL[0]);
            nearByDealsDataModel.setShopName("Modern Sports");
            nearByDealsDataModel.setShopLocation("Adyar");
            nearByDealsDataModel.setItemHeading("30% OFF on Yonex Badminton Racquets");
            nearByDealsDataModel.setItemCode("FFGALC");
            nearByDealsDataModel.setItemDescription("Get 20% Discount on Transcend, Samsung and Western Digital Hard Disks and 30% discount on Transcend, HP 8GB, 16GB and 32GB Pendrives");
            nearByDealsDataModel.setValidTo("Sept 22nd");
            nearByDealsDataModel.setShopAddress("Gandhi Road, Adyar, Chennai, Tamilnadu, 600020");
            nearByDealsDataModel.setShopPhone("044-64581248");
            nearByDealsDataModel.setHowToAvailDeal(getResources().getString(R.string.how_to_avail_deal));

            list.add(nearByDealsDataModel);
        }
    }
}
