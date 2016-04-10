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

public class Address extends AppCompatActivity implements View.OnClickListener{
    private EditText addressline,street,pin,area,tag,phone;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addressline = (EditText) findViewById(R.id.addressline);
        pin = (EditText) findViewById(R.id.postal_code);
        area = (EditText) findViewById(R.id.area);
        tag = (EditText) findViewById(R.id.tag);
        button = (Button) findViewById(R.id.submit);
        button.setOnClickListener(this);
        street = (EditText) findViewById(R.id.street);
        phone = (EditText) findViewById(R.id.phone_address);
    }

    @Override
    public void onClick(View v) {
        Intent output = new Intent();
        output.putExtra("ADDRESSLINE",addressline.getText().toString());
        output.putExtra("STREET",street.getText().toString() );
        output.putExtra("PIN",pin.getText().toString());
        output.putExtra("AREA",area.getText().toString());
        output.putExtra("PHONE",phone.getText().toString());
        output.putExtra("TAG",tag.getText().toString());
        setResult(RESULT_OK, output);
        finish();
    }
}
