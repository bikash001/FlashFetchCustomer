package com.buyer.flashfetch;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
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

import com.buyer.flashfetch.Helper.DatabaseHelper;
import com.buyer.flashfetch.Network.PostRequest;
import com.buyer.flashfetch.Objects.PostParam;
import com.buyer.flashfetch.Objects.Quote;
import com.buyer.flashfetch.Objects.Request;
import com.buyer.flashfetch.Objects.UserProfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Quotes extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "QUOTES";
    private String url,id;
    private List<Quote> mItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductAdapter mAdapter;
    private int height, width;
    private LinearLayout map;
    TextView pname,pprice;
    Dialog dialog;


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
        pname = (TextView)findViewById(R.id.product_name_quotes);
        pprice = (TextView)findViewById(R.id.price_quotes);

        url = getIntent().getStringExtra("URL");
        id= getIntent().getStringExtra("id");
        mItems = Quote.getAllQuotes(Quotes.this,id);
        pname.setText(Request.getRequest(Quotes.this, id).get(0).pname);
        pprice.setText("Price: Rs." + Request.getRequest(Quotes.this, id).get(0).pprice);
        CollapsingToolbarLayout layout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        new Download(layout).execute(url);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_quotes);
        map = (LinearLayout) findViewById(R.id.quotes_map);
        map.setOnClickListener(this);
        mAdapter = new ProductAdapter(mItems);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        //prepareData();
    }

   /* private void prepareData() {
        for (int i = 0; i < 10; ++i) {
            QuotesObject object = new QuotesObject("Seller: " + i, "12:00", "5.2 KM", "₹ 45,000", false);
            itemList.add(object);
        }
    }
*/
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.quotes_map){
            Intent intent = new Intent(this,MapsActivity.class);
            intent.putParcelableArrayListExtra("Bundle", (ArrayList<? extends Parcelable>) mItems);
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

        final private List<Quote> list;

        public class ViewHolder extends RecyclerView.ViewHolder {
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
                acceptButton = (TextView) itemView.findViewById(R.id.accept);
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


        }

        public void removeAt(int position) {
            mItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mItems.size());
        }

        public ProductAdapter(List<Quote> items) {

            list = items;
        }

        @Override
        public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_quotes, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ProductAdapter.ViewHolder holder, int position) {
            final Quote object = list.get(position);
            holder.sellerName.setText(object.name);
            //holder.timer.setText(object.);
            holder.distance.setText(object.distance);
            holder.productPrice.setText(object.qprice);
            holder.bargained = object.bargained >0;
            if(object.selcon > 0){
                holder.distance.setText("bargain accepted");
                holder.bargainButton.setVisibility(View.GONE);
            }
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
            holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),Delivery.class);
                    intent.putExtra("qid",object.qid);
                    startActivity(intent);
                }
            });
            holder.bargainButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new Dialog(v.getContext());
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
                                BargainTask bt =  new BargainTask(object, priceText.getText().toString());
                                bt.execute();
                                holder.bargainButton.setVisibility(View.GONE);
                                holder.bargain.setText(String.format("%s %s", "Bargained for ₹", price));
                                holder.bargain.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    // set the custom dialog components - text, image and button
                    dialog.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class BargainTask extends AsyncTask<Void, Void, Boolean> {
        ArrayList<PostParam> iPostParams = new ArrayList<PostParam>();
        Quote quote;
        String bprice;
        JSONObject ResponseJSON =  new JSONObject();

        public BargainTask(Quote object, String s) {
            quote = object;
            bprice =s;

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            iPostParams.add(new PostParam("selid",quote.qid));
            iPostParams.add(new PostParam("btime", String.valueOf(System.currentTimeMillis() + 10000000)));
            iPostParams.add(new PostParam("bprice",bprice));
            iPostParams.add(new PostParam("token",UserProfile.getToken(Quotes.this)));
            iPostParams.add(new PostParam("email",UserProfile.getEmail(Quotes.this)));
            ResponseJSON = PostRequest.execute("http://ec2-54-169-112-228.ap-southeast-1.compute.amazonaws.com/bargain/", iPostParams, null);
            Log.d("RESPONSE", ResponseJSON.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            ContentValues cv= new ContentValues();
            try {
                if (ResponseJSON.getJSONObject("data").getInt("result")==1){
                    cv.put("bargained",1);
                    cv.put("bgprice",bprice);
                    cv.put("bgexptime",String.valueOf(System.currentTimeMillis() + 10000000));
                    DatabaseHelper dh = new DatabaseHelper(Quotes.this);
                    dh.updateQuote(quote.qid,cv);
                    Toast toast = Toast.makeText(Quotes.this , bprice, Toast.LENGTH_SHORT);
                    toast.show();
                    dialog.dismiss();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
