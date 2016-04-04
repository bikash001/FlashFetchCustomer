package com.example.bikash.flashfetchcustomer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Quotes extends AppCompatActivity {
    private static final String TAG = "QUOTES";
    private String url;
    private List<QuotesObject> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductAdapter mAdapter;
    private int height, width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       /* Window window = getWindow();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
       */ height = WindowManager.LayoutParams.WRAP_CONTENT;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
       // int height = size.y;

        url = getIntent().getStringExtra("URL");
        CollapsingToolbarLayout layout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        new Downnload(layout).execute(url);
       /* Button button = (Button) findViewById(R.id.map_sellers);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Quotes.this,MapsActivity.class);
                startActivity(intent);
            }
        });*/
       // fill_list();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_quotes);

        mAdapter = new ProductAdapter(itemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        prepareData();
    }

    private void prepareData() {
        for(int i=0; i<10; ++i){
            QuotesObject object = new QuotesObject("Seller: "+i,"12:00","Distance: 5.2 KM","Price: Rs 45,000",false);
            itemList.add(object);
        }
    }


    private void fill_list(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for(int i=0; i<10; i++){
            MakeList list = MakeList.newInstance(i);
            transaction.add(R.id.quotes_list,list,Integer.toString(i));
        }
        transaction.commit();
    }

    public static class MakeList extends Fragment{
        private final int RESULTOFBARGAIN = 1;

        Button acceptButton, bargainButton;
        private int rank;
        public MakeList(){}
        public static MakeList newInstance(int index){
            MakeList fragment = new MakeList();
            Bundle args = new Bundle();
            args.putInt("index", index);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            rank = getArguments()!=null? getArguments().getInt("index"):1;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View linearLayout = inflater.inflate(R.layout.list_quotes,container,false);
            LinearLayout layout = (LinearLayout) linearLayout.findViewById(R.id.button_layout);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
            TextView seller = (TextView) linearLayout.findViewById(R.id.seller_name);
            seller.setText("Seller: "+rank);
            if(bargained()){
                TextView view = new TextView(getContext());
                view.setText("Price Quoted: $30");
                view.setLayoutParams(params);
                layout.addView(view);
            }
            if(!bargained()){
                bargainButton = new Button(getContext());
                bargainButton.setText(R.string.button_bargain);
                bargainButton.setLayoutParams(params);
                //bargainButton.setId(R.id.button_bargain);
                layout.addView(bargainButton);
            }
            acceptButton = new Button(getContext());
            acceptButton.setLayoutParams(params);
            acceptButton.setText(R.string.button_accept);
            layout.addView(acceptButton);
            return linearLayout;
        }


        private boolean bargained() {
            return false;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            CharSequence tag = "button_accept"+rank;
            acceptButton.setTag(tag);
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v(TAG, "clicked " + v.getTag());
                   // Toast toast = Toast.makeText(getContext(), "" + v.getTag(), Toast.LENGTH_SHORT);
                    //toast.show();
                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.dialog_accept);
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int width = displayMetrics.widthPixels;
                    int height = WindowManager.LayoutParams.WRAP_CONTENT;
                    dialog.getWindow().setLayout((int) (width * 0.8), height);
                    final RadioButton home = (RadioButton) dialog.findViewById(R.id.home_delivery);
                    final RadioButton shop = (RadioButton) dialog.findViewById(R.id.visit_shop);
                    final Button button = (Button) dialog.findViewById(R.id.ok);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(home.isChecked() || shop.isChecked()){
                                FragmentManager manager = getFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                                transaction.setCustomAnimations(R.anim.left_out, R.anim.right_out);
                                transaction.remove(getFragmentManager().findFragmentByTag(getTag())).commit();
                                dialog.dismiss();
                            }
                            else{
                                Toast toast = Toast.makeText(getContext(),"Select delivery type", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    });
                    dialog.show();
                }
            });
            if(!bargained()){
               // Button button1 = (Button) getView().findViewById(R.id.button_bargain);
                CharSequence tag1= "button_bargain"+rank;
                bargainButton.setTag(tag1);
                bargainButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.dialog);
                        dialog.setTitle("Title...");
                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        int width = displayMetrics.widthPixels;
                        int height = WindowManager.LayoutParams.WRAP_CONTENT;
                        dialog.getWindow().setLayout((int) (width * 0.8), height);
                        final EditText priceText = (EditText) dialog.findViewById(R.id.new_price);
                        priceText.setFilters(new InputFilter[]{new InputCheck(0, 10000000)});
                        final EditText hourText = (EditText) dialog.findViewById(R.id.hour);
                        hourText.setFilters(new InputFilter[]{ new InputCheck(0, 23)});
                        final EditText minText = (EditText) dialog.findViewById(R.id.min);
                        minText.setFilters(new InputFilter[]{ new InputCheck(0, 59)});
                        Button button = (Button) dialog.findViewById(R.id.ok);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String price = priceText.getText().toString();
                                String hour = hourText.getText().toString();
                                String min = minText.getText().toString();

                                if(hour.length()==0){
                                    hourText.setError("Set Hour");
                                }
                                if(min.length()==0){
                                    minText.setError("Set Min");
                                }
                                if( price.length() == 0) {
                                    priceText.setError("Set Price");
                                }
                                else{
                                    Toast toast = Toast.makeText(getContext(), ""+ price, Toast.LENGTH_SHORT);
                                    toast.show();
                                    dialog.dismiss();
                                }
                            }
                        });
                        // set the custom dialog components - text, image and button
                        dialog.show();
                    }
                });
            }
        }

    }

    class Downnload extends AsyncTask<String, Void, Bitmap> {
        CollapsingToolbarLayout mylayout;
        ImageView view;
        Downnload(CollapsingToolbarLayout layout){
            mylayout = layout;
        }

        Downnload(ImageView imageView){
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
            BitmapDrawable background = new BitmapDrawable(getResources(),bitmap);
            setbackground(background);
        }
        @TargetApi(16)
        private void setbackground(BitmapDrawable bitmapDrawable){
            mylayout.setBackground(bitmapDrawable);
        }
    }

    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{

        final private List<QuotesObject> list;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private TextView sellerName, productPrice,timer,distance,bargain;
            private Button bargainButton,acceptButton;
            private boolean bargained;
            TimerClass tt;

            public ViewHolder(View itemView) {
                super(itemView);
                bargain = (TextView) itemView.findViewById(R.id.bargined_price);
                sellerName = (TextView) itemView.findViewById(R.id.seller_name);
                productPrice = (TextView) itemView.findViewById(R.id.price_offered_quotes);
                timer = (TextView) itemView.findViewById(R.id.timer);
                distance = (TextView) itemView.findViewById(R.id.distance);
                bargainButton = (Button) itemView.findViewById(R.id.button_bargain);
                bargainButton.setOnClickListener(this);
                acceptButton = (Button) itemView.findViewById(R.id.button_accept);
                acceptButton.setOnClickListener(this);
                tt = new TimerClass(0,15,timer);
                tt.start();
            }

            @Override
            public void onClick(final View v) {
                final Dialog dialog = new Dialog(v.getContext());

                switch (v.getId()){
                    case R.id.button_accept:
                        dialog.setContentView(R.layout.dialog_accept);
                        dialog.getWindow().setLayout((int) (width * 0.8), height);
                        final RadioButton home = (RadioButton) dialog.findViewById(R.id.home_delivery);
                        final RadioButton shop = (RadioButton) dialog.findViewById(R.id.visit_shop);
                        final Button button = (Button) dialog.findViewById(R.id.ok);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(home.isChecked()){
                                    Toast toast = Toast.makeText(view.getContext(),"Home Delivery Selected", Toast.LENGTH_SHORT);
                                    toast.show();
                                    removeAt(getAdapterPosition());
                                    dialog.dismiss();
                                }
                                else if(shop.isChecked()){
                                    Toast toast = Toast.makeText(view.getContext(),"Visit Shop Selected", Toast.LENGTH_SHORT);
                                    toast.show();
                                    removeAt(getAdapterPosition());
                                    dialog.dismiss();
                                }
                                else{
                                    Toast toast = Toast.makeText(view.getContext(),"Select delivery type", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                        });
                        dialog.show();

                        break;
                    case R.id.button_bargain:
                        dialog.setContentView(R.layout.dialog);
                        dialog.getWindow().setLayout((int) (width * 0.8), height);
                        final EditText priceText = (EditText) dialog.findViewById(R.id.new_price);
                        priceText.setFilters(new InputFilter[]{new InputCheck(0, 10000000)});
                        final EditText hourText = (EditText) dialog.findViewById(R.id.hour);
                        hourText.setFilters(new InputFilter[]{ new InputCheck(0, 23)});
                        final EditText minText = (EditText) dialog.findViewById(R.id.min);
                        minText.setFilters(new InputFilter[]{ new InputCheck(0, 59)});
                        Button button1 = (Button) dialog.findViewById(R.id.ok_dialog);
                        button1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String price = priceText.getText().toString();
                                String hour = hourText.getText().toString();
                                String min = minText.getText().toString();
                                boolean error = false;
                                if(hour.length()==0){
                                    error = true;
                                    hourText.setError("Set Hour");
                                }
                                if(min.length()==0){
                                    error = true;
                                    minText.setError("Set Min");
                                }
                                if( price.length() == 0) {
                                    error = true;
                                    priceText.setError("Set Price");
                                }
                                if(!error){
                                    bargainButton.setVisibility(View.GONE);
                                    //list.get(getAdapterPosition()).setBargained(false);
                                    bargain.setText(String.format("%s %s","Expected: Rs",price));
                                   // tt.update(Long.valueOf(hour), Long.valueOf(min));
                                    //timer.setText(hour+":"+min);
                                    bargain.setVisibility(View.VISIBLE);
                                    Toast toast = Toast.makeText(v.getContext(), ""+ price, Toast.LENGTH_SHORT);
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

        public ProductAdapter(List<QuotesObject> items){
            list = items;
        }

        @Override
        public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_quotes,parent,false);
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
            if(holder.bargained){
                holder.bargain.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

}
