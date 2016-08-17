package com.buyer.flashfetch;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.buyer.flashfetch.CommonUtils.Toasts;
import com.buyer.flashfetch.CommonUtils.Utils;
import com.buyer.flashfetch.Constants.Constants;
import com.buyer.flashfetch.Helper.DatabaseHelper;
import com.buyer.flashfetch.Interfaces.UIListener;
import com.buyer.flashfetch.Network.PostRequest;
import com.buyer.flashfetch.Network.ServiceManager;
import com.buyer.flashfetch.Objects.PostParam;
import com.buyer.flashfetch.Objects.Quote;
import com.buyer.flashfetch.Objects.UserProfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DeliveryActivity extends BaseActivity {

    private Context context;
    private RadioButton shopVisit,homeDelivery;
    private TextView productPrice,voucher,deliveryType,deliveryPrice,wallet,total,totalText;
    private LinearLayout lProductPrice,lVoucher,lDeliveryType,lDeliveryPrice,lWallet,lTotal;
    private Button placeOrder;
    private CheckBox termsAndConditions;
    private ProgressDialog progressDialog;

    private String qid = "";
    private int price = 0;
    private int delivery = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = DeliveryActivity.this;

        setContentView(R.layout.activity_delivery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Delivery");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progressDialog = getProgressDialog(context);

        // TODO: get product price from the intent
        qid = getIntent().getStringExtra("id");

        shopVisit = (RadioButton)findViewById(R.id.shop_visit);
        homeDelivery = (RadioButton)findViewById(R.id.home_del);

        productPrice = (TextView)findViewById(R.id.product_price);
        voucher = (TextView)findViewById(R.id.voucher_price);
        deliveryType = (TextView)findViewById(R.id.del_by);
        deliveryPrice = (TextView)findViewById(R.id.del_cost);
        wallet = (TextView)findViewById(R.id.wallet_price);
        total = (TextView)findViewById(R.id.total_price);

        totalText = (TextView)findViewById(R.id.total_title);

        lProductPrice = (LinearLayout)findViewById(R.id.ll_product_price);
        lVoucher = (LinearLayout)findViewById(R.id.ll_voucher);
        lDeliveryPrice = (LinearLayout)findViewById(R.id.ll_del_cost);
        lDeliveryType = (LinearLayout)findViewById(R.id.ll_del_by);
        lWallet = (LinearLayout)findViewById(R.id.ll_wallet);
        lTotal = (LinearLayout)findViewById(R.id.ll_total);

        termsAndConditions = (CheckBox)findViewById(R.id.tandc);

        placeOrder = (Button)findViewById(R.id.place_order);

        totalText.setText("Total Price Payable at Shop");
        lVoucher.setVisibility(View.GONE);
        lDeliveryType.setVisibility(View.GONE);
        lDeliveryPrice.setVisibility(View.GONE);
        lWallet.setVisibility(View.GONE);

        productPrice.setText("Rs. "+ price);

        shopVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shopVisit.setBackgroundColor(ContextCompat.getColor(context,R.color.ff_green));
                homeDelivery.setBackgroundColor(ContextCompat.getColor(context,R.color.icons));

                shopVisit.setChecked(true);
                homeDelivery.setChecked(false);

                totalText.setText("Total Payable price at shop");

                lVoucher.setVisibility(View.GONE);
                lDeliveryType.setVisibility(View.GONE);
                lDeliveryPrice.setVisibility(View.GONE);
                lWallet.setVisibility(View.GONE);

                delivery = Constants.SHOP_VISIT;
            }
        });

        homeDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                homeDelivery.setBackgroundColor(ContextCompat.getColor(DeliveryActivity.this,R.color.ff_green));
                shopVisit.setBackgroundColor(ContextCompat.getColor(DeliveryActivity.this,R.color.icons));

                homeDelivery.setChecked(true);
                shopVisit.setChecked(false);

                totalText.setText("Total Price Payable");

                lVoucher.setVisibility(View.VISIBLE);
                lDeliveryType.setVisibility(View.VISIBLE);
                lDeliveryPrice.setVisibility(View.VISIBLE);
                lWallet.setVisibility(View.VISIBLE);

                delivery = Constants.HOME_DELIVERY;
            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(termsAndConditions.isChecked()) {

                    if(Utils.isInternetAvailable(context)){

                        progressDialog.show();

                        ServiceManager.callPlaceOrderService(context, delivery, qid, new UIListener() {
                            @Override
                            public void onSuccess() {
                                startActivity(new Intent(DeliveryActivity.this,HomeDelivery.class));
                            }

                            @Override
                            public void onFailure() {

                            }

                            @Override
                            public void onFailure(int result) {

                            }

                            @Override
                            public void onConnectionError() {

                            }

                            @Override
                            public void onCancelled() {

                            }
                        });
                    }else {
                        Toasts.internetUnavailableToast(context);
                    }
                }else{
                    Toast.makeText(context,"Please accept terms and conditions",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
