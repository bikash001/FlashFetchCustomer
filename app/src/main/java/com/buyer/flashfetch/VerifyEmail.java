package com.buyer.flashfetch;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.buyer.flashfetch.CommonUtils.Toasts;
import com.buyer.flashfetch.CommonUtils.Utils;


public class VerifyEmail extends BaseActivity {

    private Context context;
    private EditText mVerify;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = VerifyEmail.this;

        setContentView(R.layout.activity_verify_email);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Verify Email");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mVerify = (EditText) findViewById(R.id.verify);
        submit = (Button) findViewById(R.id.button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mVerify.getText().toString())){
                    Toast.makeText(context,"Please enter your verification code",Toast.LENGTH_SHORT).show();
                }else{
                    if(Utils.isInternetAvailable(context)){
                        //TODO: Make service call
                    }else{
                        Toasts.internetUnavailableToast(context);
                    }
                }
            }
        });
    }
}
