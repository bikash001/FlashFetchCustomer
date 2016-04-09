package com.example.bikash.flashfetchcustomer;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class Delivery extends AppCompatActivity {

    RadioButton shopvis,homdel;
    TextView prodprice,voucher,deltype,delprice,wallet,total,tpshop;
    LinearLayout lprodprice,lvoucher,ldeltype,ldelprice,lwallet,ltotal;
    Button placeorder;
    CheckBox tandc;
    int price = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

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
            }
        });

        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tandc.isChecked())
                {
                    startActivity(new Intent(Delivery.this,HomeDelivery.class));
                }
            }
        });


    }
}
