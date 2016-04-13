package com.buyer.flashfetch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeDelivery extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout layout;
    private LinearLayout.LayoutParams params;
    private int count = 0;
    private TextView button, tag, address;
    final static int STATUS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_delivery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        layout = (LinearLayout) findViewById(R.id.layout_address);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        button = (TextView) findViewById(R.id.button_add_address);
        button.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == STATUS){
            View view = getLayoutInflater().inflate(R.layout.address,layout,false);
            tag = (TextView) view.findViewById(R.id.address_tag);
            address = (TextView) view.findViewById(R.id.address_content);
            tag.setText(data.getStringExtra("TAG"));
            String ss = String.format("%s, %s, %s, %s, %s",data.getStringExtra("ADDRESSLINE"),data.getStringExtra("STREET"),
                    data.getStringExtra("AREA"),data.getStringExtra("PIN"),data.getStringExtra("PHONE"));
            address.setText(ss);
            layout.addView(view);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_add_address:
                Intent intent = new Intent(this,Fill_Address.class);
                startActivityForResult(intent,STATUS);
                break;
        }
    }
}