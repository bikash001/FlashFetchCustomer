package com.buyer.flashfetch.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyer.flashfetch.Network.BackGroundTask.DownloadImageTask;
import com.buyer.flashfetch.R;

public class AcceptedListFragment extends Fragment {

    private int rank=0;
    private long PHONE=1234567890;
    private String PRODUCT= "Product Name";
    private int PRICE = 0;
    private int PRICE_OFFERED = 0;
    private double LATITUDE = 12.9923;
    private double LONGITUDE = 80.2368;

    public AcceptedListFragment(){}

    public static AcceptedListFragment newInstance(int index) {
        AcceptedListFragment f = new AcceptedListFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rank = getArguments()!=null? getArguments().getInt("index"):1;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_accepted,container,false);
        String url = "http://img5a.flixcart.com/image/book/4/5/1/advantage-india-from-challenge-to-opportunity-400x400-imaec4hqddggjjs7.jpeg";

        ImageView imageView = (ImageView) view.findViewById(R.id.image_view_accepted);
        new DownloadImageTask(imageView).execute(url);

        ZoomImage zoomImage = new ZoomImage(url);
        imageView.setOnClickListener(zoomImage);

        TextView textView = (TextView) view.findViewById(R.id.product_name_accepted);
        textView.setText(PRODUCT);

        textView = (TextView)view.findViewById(R.id.price_from_store);
        textView.setText(String.format("Price: Rs %d",PRICE));

        textView = (TextView) view.findViewById(R.id.price_offered);
        textView.setText(String.format("Price Offered: %d",PRICE_OFFERED));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout button_call = (LinearLayout) view.findViewById(R.id.ll_call);
        CharSequence tag = "button_call"+rank;
        button_call.setTag(tag);
        button_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + PHONE));
                startActivity(intent);
            }
        });

        LinearLayout button_location = (LinearLayout) view.findViewById(R.id.ll_locate);
        tag = "button_location"+rank;
        button_location.setTag(tag);
        button_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(String.format("geo:%f,%f?z=16&q=%f,%f", LATITUDE, LONGITUDE, LATITUDE, LONGITUDE));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    public class ZoomImage implements View.OnClickListener{
        String url;
        public ZoomImage(String img){
            url = img;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
            startActivity(intent);
        }
    }

}
