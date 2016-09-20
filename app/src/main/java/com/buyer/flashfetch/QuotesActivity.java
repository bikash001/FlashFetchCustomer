package com.buyer.flashfetch;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.buyer.flashfetch.Adapters.QuotesAdapter;
import com.buyer.flashfetch.Constants.Constants;
import com.buyer.flashfetch.Objects.Quote;
import com.buyer.flashfetch.Objects.Request;

import java.util.ArrayList;
import java.util.List;

public class QuotesActivity extends BaseActivity {

    private static final String TAG = "Quotes";

    private Context context;
    private NestedScrollView nestedScrollView;
    private RecyclerView recyclerView;
    private QuotesAdapter quotesAdapter;
    private BottomSheetBehavior mBottomSheetBehavior;
    private LinearLayout mapsLayout;
    private ImageView productImageView;
    private TextView productName,productPrice;
    private String imageURL,productID,expTime;

    private List<Quote> quoteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = QuotesActivity.this;

        setContentView(R.layout.activity_quotes);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            imageURL = bundle.getString("URL");
            productID= bundle.getString("id");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Quotes");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        quoteList = Quote.getAllQuotes(QuotesActivity.this,productID);

        nestedScrollView = (NestedScrollView)findViewById(R.id.quote_nested_scroll_view);
        nestedScrollView.setFillViewport(true);

        productName = (TextView)findViewById(R.id.product_name_quotes);
        productPrice = (TextView)findViewById(R.id.product_price_quotes);
        productImageView = (ImageView)findViewById(R.id.quote_product_picture);
        mapsLayout = (LinearLayout) findViewById(R.id.quotes_map);

        Glide.with(context).load(imageURL).centerCrop().into(productImageView);

        productName.setText(Request.getRequest(context, productID).get(0).productName);
        productPrice.setText("Price: Rs." + Request.getRequest(QuotesActivity.this, productID).get(0).productPrice);

//        View bottomSheet = findViewById(R.id.bottomSheet);
//        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        quotesAdapter = new QuotesAdapter(context, productID, quoteList);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_quotes);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(quotesAdapter);

        mapsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
//                bundle.putParcelableArrayList(Constants.QUOTES_LIST, (ArrayList<? extends Parcelable>) quoteList);

                Intent intent = new Intent(context,MapsActivity.class);
                intent.putExtra(Constants.QUOTES_LIST_BUNDLE,bundle);
                startActivityForResult(intent,1);
            }
        });
    }
}
