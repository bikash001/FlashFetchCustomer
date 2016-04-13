package com.buyer.flashfetch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class VerifyEmail extends AppCompatActivity implements View.OnClickListener{
    private EditText code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        code = (EditText) findViewById(R.id.verify);
        TextView view = (TextView) findViewById(R.id.button);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
