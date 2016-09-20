package com.buyer.flashfetch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ContactUs extends BaseActivity implements View.OnClickListener{

    private EditText message;
    private TextView subject;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_contact_us);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Contact Us");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if(toolbar != null){
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }

        subject = (TextView) findViewById(R.id.contact_subject);
        message = (EditText) findViewById(R.id.contact_message);
        button = (Button) findViewById(R.id.contact_submit);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        //TODO: need to edit this
        intent.putExtra(Intent.EXTRA_EMAIL,"abc@gmail.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject.getText().toString());
        intent.putExtra(Intent.EXTRA_TEXT, message.getText().toString());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
