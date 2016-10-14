package com.buyer.flashfetch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.buyer.flashfetch.CommonUtils.Toasts;
import com.buyer.flashfetch.CommonUtils.Utils;
import com.buyer.flashfetch.Constants.Constants;
import com.buyer.flashfetch.Interfaces.UIListener;
import com.buyer.flashfetch.Network.ServiceManager;

/**
 * Created by KRANTHI on 03-07-2016.
 */
public class PasswordVerification extends BaseActivity {

    private Context context;
    private String number;
    private EditText verificationEditText;
    private Button submitButton;
    private ProgressDialog progressDialog;
    private LinearLayout passwordVerificationLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = PasswordVerification.this;

        setContentView(R.layout.password_verification);

        Bundle bundle =getIntent().getExtras();
        if(bundle != null && bundle.getBoolean("FROM_FORGOT_PASSWORD_FLOW")) {
            number = bundle.getString("MOBILE_NUMBER");
        }

        Toolbar toolbar = (Toolbar)findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Password Verification");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        progressDialog = getProgressDialog(context);

        passwordVerificationLayout = (LinearLayout)findViewById(R.id.password_verification_layout);
        verificationEditText = (EditText)findViewById(R.id.verification_edit_text);
        submitButton = (Button)findViewById(R.id.submit_verification_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(verificationEditText.getText().toString())){

                    if(Utils.isInternetAvailable(context)) {

                        progressDialog.show();

                        ServiceManager.callPasswordVerificationService(context, number, verificationEditText.getText().toString(), new UIListener() {
                            @Override
                            public void onSuccess() {
                                Toasts.verificationCodeSuccessfullyVerified(context);
                                progressDialog.dismiss();

                                Intent intent = new Intent(context, ChangePassword.class);
                                intent.putExtra("MOBILE_NUMBER", number);
                                intent.putExtra("FROM_PASSWORD_VERIFICATION_FLOW", true);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure() {
                                progressDialog.dismiss();
                                Toasts.enterValidVerificationCode(context);
                            }

                            @Override
                            public void onFailure(int result) {

                            }

                            @Override
                            public void onConnectionError() {
                                progressDialog.dismiss();
                                Toasts.serverBusyToast(context);
                            }

                            @Override
                            public void onCancelled() {
                                progressDialog.dismiss();
                            }
                        });
                    }else{
                        Toasts.internetUnavailableToast(context);
                    }
                }else{
                    Toasts.enterVerificationCode(context);
                }
            }
        });
    }
}
