package com.buyer.flashfetch.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buyer.flashfetch.Adapters.ShopSpecificDealsAdapter;
import com.buyer.flashfetch.Adapters.NearByDealsAdapter;
import com.buyer.flashfetch.CommonUtils.Toasts;
import com.buyer.flashfetch.CommonUtils.Utils;
import com.buyer.flashfetch.Constants.NearByDealsConstants;
import com.buyer.flashfetch.Objects.NearByDealsDataModel;
import com.buyer.flashfetch.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kranthikumar_b on 7/1/2016.
 */
public class NearByDealsFragment extends Fragment {

    private int uniqueID;

    private RecyclerView recyclerView;
    private NearByDealsAdapter nearByDealsAdapter;
    private ArrayList<NearByDealsDataModel> list = new ArrayList<>();

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.deals_tab_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.deals_tab_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        nearByDealsAdapter = new NearByDealsAdapter(getContext(), list);
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpDataModel();
        recyclerView.setAdapter(nearByDealsAdapter);
    }

    private void setUpDataModel() {

//        if(Utils.isInternetAvailable(getContext())){
//
//        }else{
//            Toasts.internetUnavailableToast(getContext());
//        }

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
        nearByDealsDataModel.setShopAddress("\"Gandhi Road, Adayar, Chennai, Tamil Nadu 600020");
        nearByDealsDataModel.setShopPhone("044-64581248");

        list.add(nearByDealsDataModel);
    }
}
