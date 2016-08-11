package com.buyer.flashfetch.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyer.flashfetch.Adapters.AcceptedDealsAdapter;
import com.buyer.flashfetch.Adapters.RequestedDealsAdapter;
import com.buyer.flashfetch.Objects.Request;
import com.buyer.flashfetch.R;

import java.util.ArrayList;

public class Accepted extends Fragment {

    private int rank=0;
    private long PHONE=1234567890;
    private String PRODUCT= "Product Name";
    private int PRICE = 0;
    private int PRICE_OFFERED = 0;
    private double LATITUDE = 12.9923;
    private double LONGITUDE = 80.2368;

    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private AcceptedDealsAdapter acceptedDealsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Request> requests;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.accepted_fragment,container,false);

        context = getActivity();

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh_container);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                acceptedDealsAdapter.notifyDataSetChanged();

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

        recyclerView = (RecyclerView)view.findViewById(R.id.accepted_requests);
//        recyclerView.setItemAnimator(new ScaleInOutItemAnimator(recyclerView));
        layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(layoutManager);

        acceptedDealsAdapter = new AcceptedDealsAdapter(context, requests);

        recyclerView.setAdapter(acceptedDealsAdapter);

        return view;

//        if(isAccessed){
//            if (requestedDealsAdapter.getItemCount() == 0) {
//                reqText.setVisibility(View.VISIBLE);
//                noOfRequests = 0;
//            }else if (requestedDealsAdapter.getItemCount() == 1) {
//                reqText.setText("No request yet!! They are on its way!");
//                reqText.setVisibility(View.VISIBLE);
//                noOfRequests = 1;
//            }else{
//                recyclerView.setVisibility(View.VISIBLE);
//            }
//        }else{
//            reqText.setVisibility(View.VISIBLE);
//            reqText.setText("You will receive product requests after FlashFetch Buyer App is launched. Meanwhile get familiarized with the features of your App. Stay tuned!");
//        }
    }

}
