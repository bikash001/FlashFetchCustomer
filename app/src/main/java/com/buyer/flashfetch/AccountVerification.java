package com.buyer.flashfetch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.buyer.flashfetch.CommonUtils.Toasts;
import com.buyer.flashfetch.CommonUtils.Utils;
import com.buyer.flashfetch.Constants.RegistrationConstants;
import com.buyer.flashfetch.Interfaces.UIListener;
import com.buyer.flashfetch.Network.ServiceManager;
import com.buyer.flashfetch.Objects.UserProfile;


public class AccountVerification extends BaseActivity {

    private Context context;
    private EditText mVerify;
    private Button submitButton, retryButton;
    private int retryNumber = 0;
    private ProgressDialog progressDialog;

    private long FIRST_TIME_RETRY = 60000;
    private long SECOND_TIME_RETRY = 90000;
    private long THIRD_TIME_RETRY = 120000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = AccountVerification.this;

        setContentView(R.layout.activity_verify_email);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Verify Account");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progressDialog = getProgressDialog(context);

        mVerify = (EditText) findViewById(R.id.verify);
        retryButton = (Button)findViewById(R.id.otp_retry_button);
        submitButton = (Button) findViewById(R.id.otp_submit_button);

        enableRetryButton(false);
        startCountDownTimer(FIRST_TIME_RETRY);

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(retryButton.isEnabled() && Utils.isInternetAvailable(context)){
                    progressDialog.show();

                    ServiceManager.callRetryVerificationService(context, UserProfile.getPhone(context), UserProfile.getToken(context), new UIListener() {
                        @Override
                        public void onSuccess() {
                            progressDialog.dismiss();
                            retryNumber = retryNumber + 1;
                            setTimerTime();
                            Toast.makeText(context, "OTP has been resent", Toast.LENGTH_SHORT).show();
                            enableRetryButton(false);
                        }

                        @Override
                        public void onFailure() {
                            progressDialog.dismiss();
                            Toasts.serverBusyToast(context);
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
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mVerify.getText().toString())){
                    Toast.makeText(context,"Please enter your verification code",Toast.LENGTH_SHORT).show();
                }else{
                    if(Utils.isInternetAvailable(context)){
                        if(Utils.isInternetAvailable(context)){
                            progressDialog.show();

                            ServiceManager.callVerificationService(context, mVerify.getText().toString(), UserProfile.getPhone(context), UserProfile.getToken(context), new UIListener() {
                                @Override
                                public void onSuccess() {
                                    progressDialog.dismiss();
                                    UserProfile.setAccountVerified(context,true);
                                    Toast.makeText(context, "Verification Completed! Welcome to FlashFetch", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AccountVerification.this,NearByDealsActivity.class);
                                    intent.putExtra(RegistrationConstants.FROM_REGISTRATION_FLOW, true);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onFailure() {
                                    progressDialog.dismiss();
                                    Toast.makeText(context, "Please enter correct verification code", Toast.LENGTH_SHORT).show();
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

                                }
                            });
                        }
                    }else{
                        Toasts.internetUnavailableToast(context);
                    }
                }
            }
        });
    }

    public void setTimerTime(){
        switch (retryNumber){
            case 1:
                startCountDownTimer(SECOND_TIME_RETRY);
                break;
            case 2:
                startCountDownTimer(THIRD_TIME_RETRY);
                break;
            default:
                break;
        }
    }

    private void enableRetryButton(boolean enableRetryButton){
        if(!enableRetryButton){
            retryButton.setEnabled(false);
            retryButton.setBackgroundColor(getResources().getColor(R.color.disable_button_background));
            retryButton.setTextColor(getResources().getColor(R.color.black));
        }else{
            retryButton.setEnabled(true);
            retryButton.setText("Retry");
            retryButton.setBackgroundColor(getResources().getColor(R.color.ff_black));
            retryButton.setTextColor(getResources().getColor(R.color.icons));
        }
    }

    private void startCountDownTimer(long milliSeconds){
        new CountDownTimer(milliSeconds,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                retryButton.setText("Retry (" + millisUntilFinished/1000 + ")");
            }

            @Override
            public void onFinish() {
                enableRetryButton(true);
            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_verify_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.account_verify_contact_us){
            Intent intent = new Intent(this,ContactUs.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
