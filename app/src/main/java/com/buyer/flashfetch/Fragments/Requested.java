package com.buyer.flashfetch.Fragments;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buyer.flashfetch.Adapters.RequestedDealsAdapter;
import com.buyer.flashfetch.Adapters.SectionsPagerAdapter;
import com.buyer.flashfetch.Network.BackGroundTask.DownloadImageTask;
import com.buyer.flashfetch.Objects.Request;
import com.buyer.flashfetch.Quotes;
import com.buyer.flashfetch.R;

import java.util.ArrayList;

public class Requested extends Fragment {

    private int rank=0;
    private String url;

    public static DownloadImageTask refTask;

    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RequestedDealsAdapter requestedDealsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Request> requests;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.requested_fragment,container,false);

        context = getActivity();

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
//        recyclerView.setItemAnimator(new ScaleInOutItemAnimator(recyclerView));
        layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(layoutManager);

        requestedDealsAdapter = new RequestedDealsAdapter(context, requests);

        recyclerView.setAdapter(requestedDealsAdapter);

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
        return view;

////        url = SectionsPagerAdapter.reqs.get(rank-1).pimg;
//        TextView textView = (TextView) view.findViewById(R.id.product_name);
//        textView.setText(SectionsPagerAdapter.reqs.get(rank-1).productName);
//        textView = (TextView) view.findViewById(R.id.set_price);
//        textView.setText(SectionsPagerAdapter.reqs.get(rank-1).productPrice);
//        ImageView imageView = (ImageView) view.findViewById(R.id.image_view_requested);
//        refTask = new DownloadImageTask(imageView);
//        refTask.execute(url);
//        return view;
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        Button button = (Button) getView().findViewById(R.id.button_detail);
//        CharSequence tag = "button_detail"+rank;
//        button.setTag(tag);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast toast = Toast.makeText(getContext(), ""+v.getTag(), Toast.LENGTH_SHORT);
//                toast.show();
//            }
//        });
//        Button button_quote = (Button) getView().findViewById(R.id.button_quote);
//        tag = "button_quote"+rank;
//        button_quote.setTag(tag);
//        button_quote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(),Quotes.class);
//                intent.putExtra("URL",url);
//                   /* Bundle bundle = new Bundle();
//                    bundle.putString("id", reqs.get(rank-1).id);*/
//                intent.putExtra("id",SectionsPagerAdapter.reqs.get(rank-1).productId);
//                startActivity(intent);
//            }
//        });
//    }

}

