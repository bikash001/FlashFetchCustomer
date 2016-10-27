package com.buyer.flashfetch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.buyer.flashfetch.CommonUtils.Toasts;
import com.buyer.flashfetch.CommonUtils.Utils;
import com.buyer.flashfetch.Interfaces.UIListener;
import com.buyer.flashfetch.Network.ServiceManager;
import com.buyer.flashfetch.Objects.SignUpObject;

public class RegistrationActivity extends BaseActivity {

    private static final String TAG = RegistrationActivity.class.getSimpleName();

    private Context context;
    private ProgressDialog progressDialog;
    private EditText mEmailView, mPasswordView, mPasswordViewConfirm, mName,mPhoneNumber, mReferralCode;
    private String personName, personEmail, password, personPhone, referralCode, confirmPassword;
    private Button mEmailSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = RegistrationActivity.this;

        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Register");
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

        mName = (EditText) findViewById(R.id.name);
        mEmailView = (EditText) findViewById(R.id.email_register);
        mPhoneNumber = (EditText) findViewById(R.id.phone_number);
        mPasswordViewConfirm = (EditText) findViewById(R.id.password_register_confirm);
        mPasswordView = (EditText) findViewById(R.id.password_register);
        mEmailSignInButton = (Button) findViewById(R.id.button_register);
        mReferralCode = (EditText)findViewById(R.id.referral_code);

        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {

        mEmailView.setError(null);
        mPasswordView.setError(null);
        mName.setError(null);
        mPhoneNumber.setError(null);
        mPasswordViewConfirm.setError(null);

        personName = mName.getText().toString();
        personPhone = mPhoneNumber.getText().toString();
        personEmail = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();
        confirmPassword  = mPasswordViewConfirm.getText().toString();
        referralCode = mReferralCode.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!Utils.isNameValid(personName)){
            mName.setError("Name is too short");
            focusView = mName;
            cancel = true;
        }
        if(!Utils.isPhoneValid(personPhone)){
            mPhoneNumber.setError("Enter valid phone no.");
            focusView = mPhoneNumber;
            cancel = true;
        }

        if (TextUtils.isEmpty(personEmail)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!Utils.isValidEmail(personEmail)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (TextUtils.isEmpty(password) && !Utils.isValidPassword(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(confirmPassword)){
            mPasswordViewConfirm.setError("This can't be empty");
            focusView = mPasswordViewConfirm;
            cancel = true;
        }
        else if(!Utils.checkSamePassword(confirmPassword,password)){
            mPasswordViewConfirm.setError("Password did not matched");
            focusView = mPasswordViewConfirm;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

            if(Utils.isInternetAvailable(context)){

                SignUpObject signUpObject = new SignUpObject();

                signUpObject.setPersonName(personName);
                signUpObject.setPersonEmail(personEmail);
                signUpObject.setPhoneNumber(personPhone);
                signUpObject.setPassword(password);
                signUpObject.setReferralCode(referralCode);

                progressDialog.show();

                ServiceManager.callUserRegisterService(context, signUpObject, new UIListener() {
                    @Override
                    public void onSuccess() {
                        progressDialog.dismiss();

                        Intent intent = new Intent(context, AccountVerification.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure() {
                        progressDialog.dismiss();
//                        Toast.makeText(context, "Email is already registered", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int result) {
                        progressDialog.dismiss();
                        if(result == -1){
                            Toast.makeText(context, "Mobilenumber is already registered", Toast.LENGTH_LONG).show();
                        }
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
            } else{
                Toasts.internetUnavailableToast(context);
            }
        }
    }
}
