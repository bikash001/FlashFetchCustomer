package com.buyer.flashfetch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.buyer.flashfetch.CommonUtils.Toasts;
import com.buyer.flashfetch.CommonUtils.Utils;
import com.buyer.flashfetch.Constants.URLConstants;
import com.buyer.flashfetch.Interfaces.UIListener;
import com.buyer.flashfetch.Network.PostRequest;
import com.buyer.flashfetch.Network.ServiceManager;
import com.buyer.flashfetch.Objects.PostParam;
import com.buyer.flashfetch.Objects.UserProfile;
import com.buyer.flashfetch.Services.IE_RegistrationIntentService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class RegisterActivity extends BaseActivity {

    private static final String TAG = "RegisterActivity";
    private String personName, personEmail, password, personPhone;
    private Context context;
    private ProgressDialog progressDialog;
    private EditText mEmailView, mPasswordView, mPasswordViewConfirm, mName,mPhoneNumber;
    private Button mEmailSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = RegisterActivity.this;

        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Register");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        progressDialog = getProgressDialog(context);

        mName = (EditText) findViewById(R.id.name);
        mEmailView = (EditText) findViewById(R.id.email_register);
        mPhoneNumber = (EditText) findViewById(R.id.phone_number);
        mPasswordViewConfirm = (EditText) findViewById(R.id.password_register_confirm);
        mPasswordView = (EditText) findViewById(R.id.password_register);
        mEmailSignInButton = (Button) findViewById(R.id.button_register);

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

        String name = mName.getText().toString();
        String phoneNumber = mPhoneNumber.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String confirmPassword = mPasswordViewConfirm.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!Utils.isNameValid(name)){
            mName.setError("Name is too short");
            focusView = mName;
            cancel = true;
        }
        if(!Utils.isPhoneValid(phoneNumber)){
            mPhoneNumber.setError("Enter valid phone no.");
            focusView = mPhoneNumber;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!Utils.isValidEmail(email)) {
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

            personName = name;
            personEmail = email;
            personPhone = phoneNumber;
            this.password = password;

            if(Utils.isInternetAvailable(context)){

                progressDialog.show();

                ServiceManager.callUserRegisterService(context, personName, personEmail, personPhone, password, new UIListener() {
                    @Override
                    public void onSuccess() {
                        progressDialog.dismiss();

                        Intent intent = new Intent(context,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure() {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Email is already registered", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int result) {

                    }

                    @Override
                    public void onConnectionError() {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this,"Server is not working",Toast.LENGTH_LONG).show();
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
