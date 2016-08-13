package com.buyer.flashfetch;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class ReferAndEarn extends AppCompatActivity implements View.OnClickListener{

    private ImageView whatsApp,gMail,hangout,more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_refer_and_earn);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Refer and Earn");

        if(getSupportActionBar() != null){
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        whatsApp = (ImageView) findViewById(R.id.whatsapp);
        gMail = (ImageView) findViewById(R.id.gmail);
        hangout = (ImageView) findViewById(R.id.hangout);
        more = (ImageView) findViewById(R.id.more);

        hangout.setOnClickListener(this);
        whatsApp.setOnClickListener(this);
        gMail.setOnClickListener(this);
        more.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent;
        intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "your code");

        switch (v.getId()){
            case R.id.whatsapp:
                intent.setPackage("com.whatsapp");
                try {
                    startActivity(intent);
                } catch (Exception ex) {
                    Toast toast = Toast.makeText(this, "Whatsapp Not Installed", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            case R.id.gmail:
                intent.setPackage("com.google.android.gm");
                try {
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Download FlashFetch App");
                    startActivity(intent);
                } catch (Exception ex) {
                    Toast toast = Toast.makeText(this, "Gmail Not Installed", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            case R.id.hangout:
                intent.setPackage("com.google.android.talk");
                try {
                    startActivity(intent);
                } catch (Exception ex) {
                    Toast toast = Toast.makeText(this, "Hangout Not Installed", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            case R.id.more:
                startActivity(intent);
                break;
        }
    }
}
