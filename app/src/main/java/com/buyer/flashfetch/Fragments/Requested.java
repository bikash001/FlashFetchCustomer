package com.buyer.flashfetch.Fragments;

import android.content.Context;
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
import android.widget.TextView;

import com.buyer.flashfetch.Adapters.RequestedAdapter;
import com.buyer.flashfetch.Objects.Request;
import com.buyer.flashfetch.R;

import java.util.ArrayList;

public class Requested extends Fragment {

    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RequestedAdapter requestedDealsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Request> requests;
    private TextView noRequestText;

    private static int numberOfRequests = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.requested_fragment,container,false);

        context = getActivity();

        requests = Request.getAllRequests(context);

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh_container);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                requestedDealsAdapter.notifyDataSetChanged();

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

        recyclerView = (RecyclerView)view.findViewById(R.id.requested_requests);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        requestedDealsAdapter = new RequestedAdapter(context, requests);

        recyclerView.setAdapter(requestedDealsAdapter);

        noRequestText = (TextView)view.findViewById(R.id.no_requests_text);

//        if(requests.size() == 0){
//            //TODO: need to edit
//            noRequestText.setText("Test");
//            noRequestText.setVisibility(View.VISIBLE);
//            swipeRefreshLayout.setVisibility(View.GONE);
//        }
        return view;
    }
}

