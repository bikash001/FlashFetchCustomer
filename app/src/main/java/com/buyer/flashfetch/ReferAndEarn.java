package com.buyer.flashfetch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buyer.flashfetch.Objects.UserProfile;

public class ReferAndEarn extends AppCompatActivity implements View.OnClickListener{

    private ImageView whatsApp, facebook, gmail, more;
    private TextView referralCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_refer_and_earn);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Refer");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        referralCode = (TextView)findViewById(R.id.code);

        referralCode.setText("Your Code: " + UserProfile.getReferralCode(ReferAndEarn.this));

        whatsApp = (ImageView) findViewById(R.id.whatsApp);
        facebook = (ImageView) findViewById(R.id.facebook);
        gmail = (ImageView) findViewById(R.id.gMail);
        more = (ImageView) findViewById(R.id.more);

        gmail.setOnClickListener(this);
        whatsApp.setOnClickListener(this);
        facebook.setOnClickListener(this);
        more.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent;
        intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "your code");

        switch (v.getId()){
            case R.id.whatsApp:
                intent.setPackage("com.whatsapp");
                try {
                    startActivity(intent);
                } catch (Exception ex) {
                    Toast toast = Toast.makeText(this, "WhatsApp Not Installed", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            case R.id.facebook:
                intent.setPackage("com.facebook.katana");
                try {
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Download FlashFetch App");
                    startActivity(intent);
                } catch (Exception ex) {
                    Toast toast = Toast.makeText(this, "Facebook Not Installed", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            case R.id.gMail:
                intent.setPackage("com.google.android.gm");
                try {
                    startActivity(intent);
                } catch (Exception ex) {
                    Toast toast = Toast.makeText(this, "Gmail Not Installed", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            case R.id.more:
                startActivity(intent);
                break;
        }
    }
}
