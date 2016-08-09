package com.buyer.flashfetch;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.buyer.flashfetch.CommonUtils.Toasts;
import com.buyer.flashfetch.CommonUtils.Utils;
import com.buyer.flashfetch.Helper.DatabaseHelper;
import com.buyer.flashfetch.Interfaces.UIListener;
import com.buyer.flashfetch.Interfaces.UIResponseListener;
import com.buyer.flashfetch.Network.PostRequest;
import com.buyer.flashfetch.Network.ServiceManager;
import com.buyer.flashfetch.Objects.BargainObject;
import com.buyer.flashfetch.Objects.PostParam;
import com.buyer.flashfetch.Objects.UserProfile;
import com.buyer.flashfetch.ServiceResponseObjects.ProductDetailsResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExtractActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Context context;

    private Button okButton,exitButton;
    private String productPrice, productName, imageURL, text;
    private long productCategory;
    private ProgressDialog progressDialog;
    private TextView tvname, tvprice;
    private ImageView iv;
    private int cat;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = ExtractActivity.this;

        setContentView(R.layout.activity_extract);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Product Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        okButton = (Button) findViewById(R.id.ok_extract);
        exitButton = (Button) findViewById(R.id.exit_extract);

        tvname = (TextView) findViewById(R.id.name_extract);
        iv = (ImageView) findViewById(R.id.image_extract);
        tvprice = (TextView) findViewById(R.id.price_extract);

        progressDialog = getProgressDialog(context);

        Intent intent = getIntent();
        text = intent.getStringExtra(Intent.EXTRA_TEXT);

        if (text != null) {

            if(Utils.isInternetAvailable(context)){

                progressDialog.show();

                ServiceManager.callProductFetchService(context,text, new UIResponseListener<ProductDetailsResponse>() {
                    @Override
                    public void onSuccess(ProductDetailsResponse responseObj) {
                        progressDialog.dismiss();

                        tvname.setText(responseObj.productName + " (" + responseObj.productCategory + ")");
                        tvprice.setText("Price: " + responseObj.productPrice);

                        Glide.with(ExtractActivity.this).load(responseObj.imageURL).placeholder(R.mipmap.ic_launcher).into(iv);
                    }

                    @Override
                    public void onFailure() {
                        progressDialog.dismiss();

                        Toast.makeText(context,"Service is not available for the selected product or category right now",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(context,"Please Check your internet connection",Toast.LENGTH_SHORT).show();
            }
        } else {
            tvname.setText(R.string.prompt_extract);
            okButton.setVisibility(View.INVISIBLE);
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utils.isInternetAvailable(context)){

                    progressDialog.show();

                    BargainObject bargainObject  = new BargainObject();

                    bargainObject.setProductName(productName);
                    bargainObject.setProductPrice(productPrice);
                    bargainObject.setImageURL(imageURL);
                    bargainObject.setProductCategory(productCategory);
                    bargainObject.setExpiryTime("");
                    bargainObject.setCustomerLocation(UserProfile.getLocation(context));
                    bargainObject.setCustomerEmail(UserProfile.getEmail(context));
//                    bargainObject.setCustomerName(UserProfile.get);

                    ServiceManager.callBargainService(context, bargainObject, new UIListener() {
                        @Override
                        public void onSuccess() {
                            progressDialog.dismiss();

                            if(UserProfile.getEmail(context) != ""){
                                Intent intent = new Intent(context,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Intent intent = new Intent(context,LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure() {

                        }

                        @Override
                        public void onFailure(int result) {

                        }

                        @Override
                        public void onConnectionError() {

                        }

                        @Override
                        public void onCancelled() {

                        }
                    });
                }
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
