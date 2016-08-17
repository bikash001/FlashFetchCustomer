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
import com.buyer.flashfetch.Objects.NearByDealsDataModel;
import com.buyer.flashfetch.R;

import java.util.List;

/**
 * Created by kranthikumar_b on 7/1/2016.
 */
public class NearByDealsFragment extends Fragment {

    private int uniqueID;

    private RecyclerView recyclerView;
    private NearByDealsAdapter nearByDealsAdapter;
    private List<NearByDealsDataModel> list;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.deals_tab_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView)view.findViewById(R.id.deals_tab_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        nearByDealsAdapter = new NearByDealsAdapter(getContext(),list);
        recyclerView.setAdapter(nearByDealsAdapter);
    }

    private void setUpDataModel(){

        if(Utils.isInternetAvailable(getContext())){
            //TODO: make service call and attach data to the dataModel
        }else{
            Toasts.internetUnavailableToast(getContext());
        }
    }
}
