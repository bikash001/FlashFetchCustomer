package com.buyer.flashfetch;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Quotes extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "QUOTES";
    private String url;
    private List<QuotesObject> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductAdapter mAdapter;
    private int height, width;
    private LinearLayout map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        height = WindowManager.LayoutParams.WRAP_CONTENT;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;

        url = getIntent().getStringExtra("URL");
        CollapsingToolbarLayout layout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        new Download(layout).execute(url);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_quotes);
        map = (LinearLayout) findViewById(R.id.quotes_map);
        map.setOnClickListener(this);
        mAdapter = new ProductAdapter(itemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        prepareData();
    }

    private void prepareData() {
        for (int i = 0; i < 10; ++i) {
            QuotesObject object = new QuotesObject("Seller: " + i, "12:00", "5.2 KM", "₹ 45,000", false);
            itemList.add(object);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.quotes_map){
            Intent intent = new Intent(this,MapsActivity.class);
            startActivityForResult(intent,1);
        }
    }


    private class Download extends AsyncTask<String, Void, Bitmap> {
        CollapsingToolbarLayout mylayout;
        ImageView view;

        Download(CollapsingToolbarLayout layout) {
            mylayout = layout;
        }

        Download(ImageView imageView) {
            view = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                return null;
            }
        }

        protected void onPostExecute(Bitmap bitmap) {
            BitmapDrawable background = new BitmapDrawable(getResources(), bitmap);
            setbackground(background);
        }

        @TargetApi(16)
        private void setbackground(BitmapDrawable bitmapDrawable) {
            mylayout.setBackground(bitmapDrawable);
        }
    }

    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

        final private List<QuotesObject> list;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView sellerName, productPrice, timer, distance, bargain,comment,more;
            private TextView bargainButton, acceptButton;
            private boolean bargained;
            private LinearLayout layout;

            TimerClass tt;

            public ViewHolder(View itemView) {
                super(itemView);
                more = (TextView) itemView.findViewById(R.id.more);
                comment = (TextView) itemView.findViewById(R.id.comment);
                bargain = (TextView) itemView.findViewById(R.id.bargained);
                sellerName = (TextView) itemView.findViewById(R.id.seller_name);
                productPrice = (TextView) itemView.findViewById(R.id.price_offered_quotes);
                timer = (TextView) itemView.findViewById(R.id.timer);
                distance = (TextView) itemView.findViewById(R.id.distance);
                bargainButton = (TextView) itemView.findViewById(R.id.bargain);
                bargainButton.setOnClickListener(this);
                acceptButton = (TextView) itemView.findViewById(R.id.accept);
                acceptButton.setOnClickListener(this);
                layout = (LinearLayout) itemView.findViewById(R.id.button_layout);
                Log.d(TAG, "ViewHolder");

                // tt = new TimerClass(0,15,timer);
                //tt.start();

            }

            public void changeView(int pos){
                int n = getItemCount();
                for(int i=0; i<pos; i++){
                    list.get(i).setValid(false);
                }
                for(int i=pos+1; i<n; i++){
                    list.get(i).setValid(false);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(v.getContext());

                switch (v.getId()) {
                    case R.id.accept:
                        Intent intent = new Intent(v.getContext(),Delivery.class);
                        startActivity(intent);
                        break;
                    case R.id.bargain:
                        dialog.setContentView(R.layout.dialog);
                        dialog.getWindow().setLayout((int) (width * 0.8), height);
                        final EditText priceText = (EditText) dialog.findViewById(R.id.new_price);
                        priceText.setFilters(new InputFilter[]{new InputCheck(0, 10000000)});
                        final EditText hourText = (EditText) dialog.findViewById(R.id.hour);
                        hourText.setFilters(new InputFilter[]{new InputCheck(0, 23)});
                        final EditText minText = (EditText) dialog.findViewById(R.id.min);
                        minText.setFilters(new InputFilter[]{new InputCheck(0, 59)});
                        Button button1 = (Button) dialog.findViewById(R.id.ok_dialog);
                        button1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String price = priceText.getText().toString();
                                String hour = hourText.getText().toString();
                                String min = minText.getText().toString();
                                boolean error = false;
                                if (hour.length() == 0) {
                                    error = true;
                                    hourText.setError("Set Hour");
                                }
                                if (min.length() == 0) {
                                    error = true;
                                    minText.setError("Set Min");
                                }
                                if (price.length() == 0) {
                                    error = true;
                                    priceText.setError("Set Price");
                                }
                                if (!error) {
                                    bargainButton.setVisibility(View.GONE);
                                    //list.get(getAdapterPosition()).setBargained(false);
                                    bargain.setText(String.format("%s %s", "Bargained for ₹", price));
                                    // tt.update(Long.valueOf(hour), Long.valueOf(min));
                                    //timer.setText(hour+":"+min);
                                    bargain.setVisibility(View.VISIBLE);
                                    Toast toast = Toast.makeText(v.getContext(), "" + price, Toast.LENGTH_SHORT);
                                    toast.show();
                                    dialog.dismiss();
                                }
                            }
                        });
                        // set the custom dialog components - text, image and button
                        dialog.show();

                        break;
                }
            }
        }

        public void removeAt(int position) {
            itemList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, itemList.size());
        }

        public ProductAdapter(List<QuotesObject> items) {
            list = items;
        }

        @Override
        public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_quotes, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {
            QuotesObject object = list.get(position);
            holder.sellerName.setText(object.getSeller());
            holder.timer.setText(object.getTime());
            holder.distance.setText(object.getDistance());
            holder.productPrice.setText(object.getPrice());
            holder.bargained = object.isBargained();
            Layout temp = holder.comment.getLayout();
            if(temp != null) {
                int lines = temp.getLineCount();
                if(lines > 0) {
                    int ellipsisCount = temp.getEllipsisCount(lines-1);
                    if ( ellipsisCount > 0) {
                        holder.more.setVisibility(View.VISIBLE);
                        Log.d(TAG, "Text is ellipsized");
                    }
                }
            }
            if(holder.bargained){
                holder.bargain.setVisibility(View.GONE);
            }
            if(!object.getValid()){
                holder.layout.setVisibility(View.GONE);
            }
            Log.d(TAG, "bind" + position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

}
