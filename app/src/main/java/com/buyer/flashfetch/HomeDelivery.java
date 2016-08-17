package com.buyer.flashfetch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeDelivery extends BaseActivity{

    private Context context;
    private int count = 0;
    private TextView addAddress, tag, address;
    private Button confirmButton;
    final static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = HomeDelivery.this;

        setContentView(R.layout.activity_home_delivery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Delivery Address");

        if(getSupportActionBar() != null){
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addAddress = (TextView) findViewById(R.id.button_add_address);
        confirmButton = (Button)findViewById(R.id.button_confirm);

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,FillAddressActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: need to integrate service call
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE){
//            View view = getLayoutInflater().inflate(R.layout.address,layout,false);
//            tag = (TextView) view.findViewById(R.id.address_tag);
//            address = (TextView) view.findViewById(R.id.address_content);
//            tag.setText(data.getStringExtra("TAG"));
//            String ss = String.format("%s, %s, %s, %s, %s",data.getStringExtra("ADDRESSLINE"),data.getStringExtra("STREET"),
//                    data.getStringExtra("AREA"),data.getStringExtra("PIN"),data.getStringExtra("PHONE"));
//            address.setText(ss);
//            layout.addView(view);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
