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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.buyer.flashfetch.CommonUtils.Toasts;
import com.buyer.flashfetch.CommonUtils.Utils;
import com.buyer.flashfetch.Helper.DatabaseHelper;
import com.buyer.flashfetch.Interfaces.UIResponseListener;
import com.buyer.flashfetch.Network.PostRequest;
import com.buyer.flashfetch.Network.ServiceManager;
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
    private String price, name, url, text, category;
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

                ServiceManager.callProductFetchService(context,text, new UIResponseListener<ProductDetailsResponse>() {
                    @Override
                    public void onSuccess(ProductDetailsResponse responseObj) {

                    }

                    @Override
                    public void onFailure() {

                    }

                    @Override
                    public void onConnectionError() {

                    }

                    @Override
                    public void onCancelled() {

                    }
                });
            }else{

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

//                    ServiceManager.callBargainService(context,ba);
                }else{
                    Toasts.internetUnavailableToast(context);
                }

                Intent intent = new Intent(ExtractActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
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


//    private class BargainTask extends AsyncTask<Void, Void, Boolean> {
//        ArrayList<PostParam> iPostParams = new ArrayList<PostParam>();
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            PostParam postname = new PostParam("pname", name);
//            PostParam postprice = new PostParam("price", price);
//            PostParam postimg = new PostParam("img", url);
//            iPostParams.add(postname);
//            iPostParams.add(postprice);
//            iPostParams.add(postimg);
//            iPostParams.add(new PostParam("cat", String.valueOf(cat)));
//            iPostParams.add(new PostParam("time", String.valueOf(System.currentTimeMillis() + 10000000)));
//            iPostParams.add(new PostParam("cus_loc","(12.324225,80.234234)"));
//            iPostParams.add(new PostParam("name", UserProfile.getName(ExtractActivity.this)));
//            iPostParams.add(new PostParam("token",UserProfile.getToken(ExtractActivity.this)));
//            iPostParams.add(new PostParam("cus_email",UserProfile.getEmail(ExtractActivity.this)));
//            ResponseJSON = PostRequest.execute("http://ec2-54-169-112-228.ap-southeast-1.compute.amazonaws.com/c2s/", iPostParams, null);
//            Log.d("RESPONSE", ResponseJSON.toString());
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);
//            ContentValues cv= new ContentValues();
//            try {
//                if (ResponseJSON.getJSONObject("data").getInt("result")==1){
//                    cv.put("id",ResponseJSON.getJSONObject("data").getString("id"));
//                    cv.put("pname",name);
//                    cv.put("pprice",price);
//                    cv.put("pimg",url);
//                    cv.put("url",text);
//                    cv.put("exptime",String.valueOf(System.currentTimeMillis() + 10000000));
//                    cv.put("cat",cat);
//                    DatabaseHelper dh = new DatabaseHelper(ExtractActivity.this);
//                    dh.addRequest(cv);
//                    Intent intent = new Intent(ExtractActivity.this,MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }

}
