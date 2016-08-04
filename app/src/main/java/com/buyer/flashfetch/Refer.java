package com.buyer.flashfetch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Refer extends AppCompatActivity implements View.OnClickListener{

    private Button enter,skip;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        enter = (Button) findViewById(R.id.enter_button);
        skip = (Button) findViewById(R.id.skip_button);
        editText = (EditText) findViewById(R.id.refer_code);
        enter.setOnClickListener(this);
        skip.setOnClickListener(this);
        editText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.refer_code:
                break;
            case R.id.enter_button:
                break;
            case R.id.skip_button:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
