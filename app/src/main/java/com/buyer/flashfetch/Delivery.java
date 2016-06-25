package com.buyer.flashfetch;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.buyer.flashfetch.Helper.DatabaseHelper;
import com.buyer.flashfetch.Network.PostRequest;
import com.buyer.flashfetch.Objects.PostParam;
import com.buyer.flashfetch.Objects.Quote;
import com.buyer.flashfetch.Objects.UserProfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Delivery extends AppCompatActivity {

    RadioButton shopvis,homdel;
    TextView prodprice,voucher,deltype,delprice,wallet,total,tpshop;
    LinearLayout lprodprice,lvoucher,ldeltype,ldelprice,lwallet,ltotal;
    Button placeorder;
    CheckBox tandc;
    String qid = " ";
    int price = 0;
    int del =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        qid = getIntent().getStringExtra("id");

        shopvis = (RadioButton)findViewById(R.id.shop_visit);
        homdel = (RadioButton)findViewById(R.id.home_del);

        prodprice = (TextView)findViewById(R.id.product_price);
        voucher = (TextView)findViewById(R.id.voucher_price);
        deltype = (TextView)findViewById(R.id.del_by);
        delprice = (TextView)findViewById(R.id.del_cost);
        wallet = (TextView)findViewById(R.id.wallet_price);
        total = (TextView)findViewById(R.id.total_price);
        tpshop = (TextView)findViewById(R.id.total_title);

        lprodprice = (LinearLayout)findViewById(R.id.ll_product_price);
        lvoucher = (LinearLayout)findViewById(R.id.ll_voucher);
        ldelprice = (LinearLayout)findViewById(R.id.ll_del_cost);
        ldeltype = (LinearLayout)findViewById(R.id.ll_del_by);
        lwallet = (LinearLayout)findViewById(R.id.ll_wallet);
        ltotal = (LinearLayout)findViewById(R.id.ll_total);

        tandc = (CheckBox)findViewById(R.id.tandc);

        placeorder = (Button)findViewById(R.id.place_order);

        tpshop.setText("Total Payable price at shop");
        lvoucher.setVisibility(View.GONE);
        ldeltype.setVisibility(View.GONE);
        ldelprice.setVisibility(View.GONE);
        lwallet.setVisibility(View.GONE);

        prodprice.setText("Rs. "+price);
        voucher.setText("Rs. "+price);
        delprice.setText("Rs. "+price);
        wallet.setText("Rs. "+price);
        total.setText("Rs. "+price);
        deltype.setText("FlashFetch");

        shopvis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopvis.setBackgroundColor(ContextCompat.getColor(Delivery.this,R.color.ff_green));
                homdel.setBackgroundColor(ContextCompat.getColor(Delivery.this,R.color.icons));
                shopvis.setChecked(true);
                homdel.setChecked(false);
                tpshop.setText("Total Payable price at shop");
                lvoucher.setVisibility(View.GONE);
                ldeltype.setVisibility(View.GONE);
                ldelprice.setVisibility(View.GONE);
                lwallet.setVisibility(View.GONE);
                del=1;
            }
        });
        homdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homdel.setBackgroundColor(ContextCompat.getColor(Delivery.this,R.color.ff_green));
                shopvis.setBackgroundColor(ContextCompat.getColor(Delivery.this,R.color.icons));
                homdel.setChecked(true);
                shopvis.setChecked(false);
                tpshop.setText("Total Payable Price");
                lvoucher.setVisibility(View.VISIBLE);
                ldeltype.setVisibility(View.VISIBLE);
                ldelprice.setVisibility(View.VISIBLE);
                lwallet.setVisibility(View.VISIBLE);
                del=2;
            }
        });

        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcceptTask at =new AcceptTask();
                at.execute();
                if(tandc.isChecked())
                {
                    startActivity(new Intent(Delivery.this,HomeDelivery.class));
                }
            }
        });


    }
    private class AcceptTask extends AsyncTask<Void, Void, Boolean> {
        ArrayList<PostParam> iPostParams;
        Quote quote;
        String bprice;
        JSONObject ResponseJSON =  new JSONObject();
        AcceptTask(){
            iPostParams = new ArrayList<>();
        }



        @Override
        protected Boolean doInBackground(Void... params) {
            iPostParams.add(new PostParam("Sel_id",qid));
            iPostParams.add(new PostParam("delivery",Integer.toString(del)));
            iPostParams.add(new PostParam("token", UserProfile.getToken(Delivery.this)));
            iPostParams.add(new PostParam("email",UserProfile.getEmail(Delivery.this)));
            ResponseJSON = PostRequest.execute("http://ec2-54-169-112-228.ap-southeast-1.compute.amazonaws.com/del/", iPostParams, null);
            Log.d("RESPONSE", ResponseJSON.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            ContentValues cv= new ContentValues();
            try {
                if (ResponseJSON.getJSONObject("data").getInt("result")==1){
                    cv.put("cuscon",1);
                    cv.put("del",del);
                    DatabaseHelper dh = new DatabaseHelper(Delivery.this);
                    dh.updateQuote(qid,cv);
                    Toast toast = Toast.makeText(Delivery.this , bprice + "  sdkfjksd", Toast.LENGTH_SHORT);
                    toast.show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
