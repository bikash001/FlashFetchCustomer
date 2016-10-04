package com.buyer.flashfetch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyer.flashfetch.CommonUtils.Toasts;
import com.buyer.flashfetch.CommonUtils.Utils;
import com.buyer.flashfetch.Interfaces.UIListener;
import com.buyer.flashfetch.Network.ServiceManager;

/**
 * Created by KRANTHI on 03-07-2016.
 */
public class ForgotPassword extends BaseActivity {

    private Context context;
    private EditText emailText;
    private Button button;
    private TextView registerHere;
    private ProgressDialog progressDialog;
    private LinearLayout forgotPasswordLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = ForgotPassword.this;

        setContentView(R.layout.forgot_password);

        Toolbar toolbar = (Toolbar)findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Forgot Password");
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

        forgotPasswordLayout = (LinearLayout)findViewById(R.id.forgot_password_layout);

        progressDialog = getProgressDialog(ForgotPassword.this);

        registerHere = (TextView)findViewById(R.id.login_forgot);

        emailText = (EditText)findViewById(R.id.forgot_email);
        button = (Button)findViewById(R.id.send_button);

        registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPassword.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.isValidEmail(emailText.getText().toString())){

                    if(Utils.isInternetAvailable(ForgotPassword.this)){

                        progressDialog.show();

                        ServiceManager.callForgotPasswordService(ForgotPassword.this, emailText.getText().toString(), new UIListener() {
                            @Override
                            public void onSuccess() {
                                progressDialog.dismiss();
                                Toasts.successfullySentOTP(ForgotPassword.this);

                                Intent intent = new Intent(ForgotPassword.this,PasswordVerification.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure() {
                                progressDialog.dismiss();
                                Toasts.notRegisteredMobileNumberToast(ForgotPassword.this);
                                registerHere.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFailure(int result) {
                                progressDialog.dismiss();
                                if(result == 0){
                                    Toasts.serverBusyToast(context);
                                }
                            }

                            @Override
                            public void onConnectionError() {
                                progressDialog.dismiss();
                                Toasts.serverBusyToast(ForgotPassword.this);
                            }

                            @Override
                            public void onCancelled() {
                                progressDialog.dismiss();
                            }
                        });
                    }else{
                        Toasts.internetUnavailableToast(ForgotPassword.this);
                    }

                }else{
                    Toasts.validEmailToast(ForgotPassword.this);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
