package com.buyer.flashfetch;

import android.app.ProgressDialog;
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

/**
 * Created by KRANTHI on 03-07-2016.
 */

public class ChangePassword extends BaseActivity {

    private EditText newPassword, confirmPassword;
    private Button changePassword;
    private String email;
    private ProgressDialog progressDialog;
    private LinearLayout changePasswordLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.change_password);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getBoolean("FROM_PASSWORD_VERIFICATION_FLOW")) {
            email = bundle.getString("EMAIL");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ChangePassword.this, PasswordVerification.class);
//                  startActivity(intent);
                onBackPressed();
            }
        });

        changePasswordLayout = (LinearLayout) findViewById(R.id.change_password_layout);
        progressDialog = getProgressDialog(ChangePassword.this);

        newPassword = (EditText) findViewById(R.id.forgot_new_password);
        confirmPassword = (EditText) findViewById(R.id.forgot_confirm_password);
        changePassword = (Button) findViewById(R.id.change_password);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(newPassword.getText().toString()) || TextUtils.isEmpty(confirmPassword.getText().toString())) {

                    Toasts.emptyPasswordToast(ChangePassword.this);

                } else {
                    if (Utils.checkSamePassword(newPassword.getText().toString(), confirmPassword.getText().toString())) {

                        if (Utils.isInternetAvailable(ChangePassword.this)) {

                            progressDialog.show();

//                            ServiceManager.callPasswordChangeService(ChangePassword.this, email, newPassword.getText().toString(), new UIListener() {
//
//                                @Override
//                                public void onSuccess() {
//                                    Toasts.successfulPasswordChangeToast(ChangePassword.this);
//
//                                    if (UserProfile.getCategory(ChangePassword.this) == 1) {
//                                        Intent i = new Intent(ChangePassword.this, CategoryActivity.class);
////                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(i);
//                                    } else {
//                                        Intent i = new Intent(ChangePassword.this, MainActivity.class);
////                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(i);
//                                        finish();
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure() {
//                                    progressDialog.dismiss();
//                                    Toasts.serverBusyToast(ChangePassword.this);
//                                }
//
//                                @Override
//                                public void onConnectionError() {
//                                    progressDialog.dismiss();
//                                    Toasts.serverBusyToast(ChangePassword.this);
//                                }
//
//                                @Override
//                                public void onCancelled() {
//                                    progressDialog.dismiss();
//                                }
//                            });
                        } else {
                            Toasts.internetUnavailableToast(ChangePassword.this);
                        }
                    } else {
                        Toasts.passwordNotSameToast(ChangePassword.this);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
