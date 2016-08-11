package com.buyer.flashfetch;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FillAddressActivity extends BaseActivity implements View.OnClickListener{

    private EditText addressline,street,pin,area,tag,phone;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fill__address);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setTitle("Fill Address");

        addressline = (EditText) findViewById(R.id.addressline);
        pin = (EditText) findViewById(R.id.postal_code);
        area = (EditText) findViewById(R.id.area);
        tag = (EditText) findViewById(R.id.tag);
        street = (EditText) findViewById(R.id.street);
        phone = (EditText) findViewById(R.id.phone_address);
        submitButton = (Button) findViewById(R.id.submit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.putExtra("ADDRESSLINE",addressline.getText().toString());
                intent.putExtra("STREET",street.getText().toString() );
                intent.putExtra("PIN",pin.getText().toString());
                intent.putExtra("AREA",area.getText().toString());
                intent.putExtra("PHONE",phone.getText().toString());
                intent.putExtra("TAG",tag.getText().toString());

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
