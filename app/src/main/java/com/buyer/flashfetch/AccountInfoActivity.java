package com.buyer.flashfetch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyer.flashfetch.Constants.Constants;

public class AccountInfoActivity extends BaseActivity{

    private Context context;
    private LinearLayout addressLayout;
    private TextView profile,deliveryAddress,addAddress, tagText, address,name,phone;
    final static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = AccountInfoActivity.this;

        setContentView(R.layout.activity_account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Account Information");
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addressLayout = (LinearLayout) findViewById(R.id.layout_address);

        profile = (TextView)findViewById(R.id.profile_text);
        deliveryAddress  = (TextView)findViewById(R.id.delivery_address_text);
        addAddress = (TextView) findViewById(R.id.button_add_address);
        name = (TextView) findViewById(R.id.profile_name);
        phone = (TextView) findViewById(R.id.phone_profile);

        SpannableString profileText = new SpannableString(getResources().getString(R.string.profile));
        profileText.setSpan(new UnderlineSpan(),0,getResources().getString(R.string.profile).length(),0);

        SpannableString deliveryText = new SpannableString(getResources().getString(R.string.delivery_address));
        deliveryText.setSpan(new UnderlineSpan(),0,getResources().getString(R.string.delivery_address).length(),0);

        profile.setText(profileText);
        deliveryAddress.setText(deliveryText);

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,FillAddressActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE){

            View view = getLayoutInflater().inflate(R.layout.address,addressLayout,false);

            tagText = (TextView) view.findViewById(R.id.address_tag);
            address = (TextView) view.findViewById(R.id.address_content);

            if(data != null) {
                String tag = data.getStringExtra(Constants.FLAT_TAG);
                String flatNumber = data.getStringExtra(Constants.FLAT_NUMBER);
                String flatStreet = data.getStringExtra(Constants.FLAT_STREET);
                String flatArea = data.getStringExtra(Constants.FLAT_AREA);
                String flatCity = data.getStringExtra(Constants.FLAT_CITY);
                String flatState = data.getStringExtra(Constants.FLAT_STATE);
                String flatPinCode = data.getStringExtra(Constants.FLAT_PIN_CODE);
                String flatPhone = data.getStringExtra(Constants.FLAT_PHONE);

                SpannableString spannableString = new SpannableString(tag);
                spannableString.setSpan(new UnderlineSpan(),0,tag.length(),0);

                tagText.setText(spannableString);
                address.setText(flatNumber + "," + "\n" + flatStreet + "," + "\n" + flatArea + "," + "\n" + flatCity + "," + "\n" + flatState + " - " + flatPinCode + "," + "\n" + flatPhone);

                addressLayout.addView(view);

            }
        }
    }
}
