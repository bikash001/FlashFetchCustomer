package com.buyer.flashfetch;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.buyer.flashfetch.Network.ServiceManager;

/**
 * Created by KRANTHI on 03-07-2016.
 */
public class PasswordVerification extends BaseActivity {

    private Context context;
    private String email;
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
            email = bundle.getString("EMAIL");
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
        progressDialog = getProgressDialog(context);
        verificationEditText = (EditText)findViewById(R.id.verification_edit_text);
        submitButton = (Button)findViewById(R.id.submit_verification_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(verificationEditText.getText().toString())){

                    if(Utils.isInternetAvailable(context)) {

                        progressDialog.show();

                        //TODO: integrate service call

//                        ServiceManager.callPasswordVerificationService(context, email, verificationEditText.getText().toString(), new UIListener() {
//                            @Override
//                            public void onSuccess() {
//                                Toasts.verificationCodeSuccessfullyVerified(context);
//                                progressDialog.dismiss();
//
//                                Intent intent = new Intent(context, ChangePassword.class);
//                                intent.putExtra("EMAIL", email);
//                                intent.putExtra("FROM_PASSWORD_VERIFICATION_FLOW", Constants.IS_FROM_PASSWORD_VERIFICATION);
//                                startActivity(intent);
//                            }
//
//                            @Override
//                            public void onFailure() {
//                                Toasts.enterValidVerificationCode(context);
//                                progressDialog.dismiss();
//                            }
//
//                            @Override
//                            public void onConnectionError() {
//                                Toasts.serverBusyToast(context);
//                                progressDialog.dismiss();
//                            }
//
//                            @Override
//                            public void onCancelled() {
//                                progressDialog.dismiss();
//                            }
//                        });
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
