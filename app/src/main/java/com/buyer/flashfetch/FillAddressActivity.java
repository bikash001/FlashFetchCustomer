package com.buyer.flashfetch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.buyer.flashfetch.CommonUtils.Utils;
import com.buyer.flashfetch.Constants.Constants;

public class FillAddressActivity extends BaseActivity{

    private Context context;
    private EditText flatNumber,street,pin,area,tag,phone,city,state;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = FillAddressActivity.this;

        setContentView(R.layout.activity_fill__address);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setTitle("Fill Address");

        flatNumber = (EditText) findViewById(R.id.addressline);
        street = (EditText) findViewById(R.id.street);
        area = (EditText) findViewById(R.id.area);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        pin = (EditText) findViewById(R.id.postal_code);
        phone = (EditText) findViewById(R.id.phone_address);
        tag = (EditText) findViewById(R.id.tag);
        submitButton = (Button) findViewById(R.id.submit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(flatNumber.getText().toString()) || TextUtils.isEmpty(street.getText().toString()) || TextUtils.isEmpty(area.getText().toString()) ||
                        TextUtils.isEmpty(city.getText().toString()) || TextUtils.isEmpty(state.getText().toString()) || TextUtils.isEmpty(pin.getText().toString()) ||
                            TextUtils.isEmpty(phone.getText().toString()) || TextUtils.isEmpty(tag.getText().toString())){
                    Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                }else{
                    if(!Utils.isPhoneValid(phone.getText().toString())){
                        Toast.makeText(context,"Enter valid mobile number",Toast.LENGTH_SHORT).show();
                    }else if(!Utils.isPinCodeValid(pin.getText().toString())){
                        Toast.makeText(context,"Enter valid pincode",Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = new Intent();

                        intent.putExtra(Constants.FLAT_NUMBER,flatNumber.getText().toString());
                        intent.putExtra(Constants.FLAT_STREET,street.getText().toString() );
                        intent.putExtra(Constants.FLAT_AREA,area.getText().toString());
                        intent.putExtra(Constants.FLAT_CITY,city.getText().toString());
                        intent.putExtra(Constants.FLAT_STATE,state.getText().toString());
                        intent.putExtra(Constants.FLAT_PIN_CODE,pin.getText().toString());
                        intent.putExtra(Constants.FLAT_PHONE,phone.getText().toString());
                        intent.putExtra(Constants.FLAT_TAG,tag.getText().toString());

                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        });
    }
}
