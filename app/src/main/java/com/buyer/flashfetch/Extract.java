package com.buyer.flashfetch;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.buyer.flashfetch.Helper.DatabaseHelper;
import com.buyer.flashfetch.Network.PostRequest;
import com.buyer.flashfetch.Objects.PostParam;
import com.buyer.flashfetch.Objects.UserProfile;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Extract extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    JSONObject ResponseJSON;
    ArrayList<PostParam> iPostParams;
    String price, name, url;
    private View mProgressView;
    String text, category;
    TextView tvname, tvprice;
    ImageView iv;
    int cat;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* if((UserProfile.getEmail(Extract.this)=="")){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }*/
        setContentView(R.layout.activity_extract);
        Button okButton = (Button) findViewById(R.id.ok_extract);
        Button exitButton = (Button) findViewById(R.id.exit_extract);
        mProgressView = findViewById(R.id.login_progress);
        tvname = (TextView) findViewById(R.id.name_extract);
        iv = (ImageView) findViewById(R.id.image_extract);
        tvprice = (TextView) findViewById(R.id.price_extract);
        Intent intent = getIntent();
        text = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (text != null) {
            GetTask gt = new GetTask();
            gt.execute();
        } else {
            tvname.setText(R.string.prompt_extract);
            okButton.setVisibility(View.INVISIBLE);
        }
        okButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_extract:

                BargainTask bt = new BargainTask();
                bt.execute();
                Intent intent = new Intent(this, Main2Activity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.exit_extract:
                finish();
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

           /* mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });*/

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            //mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
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


    public class BargainTask extends AsyncTask<Void, Void, Boolean> {
        ArrayList<PostParam> iPostParams = new ArrayList<PostParam>();

        @Override
        protected Boolean doInBackground(Void... params) {
            PostParam postname = new PostParam("pname", name);
            PostParam postprice = new PostParam("price", price);
            PostParam postimg = new PostParam("img", url);
            iPostParams.add(postname);
            iPostParams.add(postprice);
            iPostParams.add(postimg);
            iPostParams.add(new PostParam("cat", String.valueOf(cat)));
            iPostParams.add(new PostParam("time", String.valueOf(System.currentTimeMillis() + 10000000)));
            iPostParams.add(new PostParam("cus_loc", UserProfile.getLocation(Extract.this)));
            iPostParams.add(new PostParam("name", UserProfile.getName(Extract.this)));
            iPostParams.add(new PostParam("token",UserProfile.getToken(Extract.this)));
            iPostParams.add(new PostParam("cus_email",UserProfile.getEmail(Extract.this)));
            ResponseJSON = PostRequest.execute("http://ec2-54-169-112-228.ap-southeast-1.compute.amazonaws.com/c2s/", iPostParams, null);
            Log.d("RESPONSE", ResponseJSON.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            ContentValues cv= new ContentValues();
            try {
                if (ResponseJSON.getJSONObject("data").getInt("result")==1){
                    cv.put("id",ResponseJSON.getJSONObject("data").getString("id"));
                    cv.put("pname",name);
                    cv.put("pprice",price);
                    cv.put("pimg",url);
                    cv.put("url",text);
                    cv.put("exptime",String.valueOf(System.currentTimeMillis() + 10000000));
                    cv.put("cat",cat);
                    DatabaseHelper dh = new DatabaseHelper(Extract.this);
                    dh.addRequest(cv);
                    Intent intent = new Intent(Extract.this,Main2Activity.class);
                    startActivity(intent);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    public class GetTask extends AsyncTask<Void, Void, Boolean> {
        ArrayList<PostParam> iPostParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            iPostParams = new ArrayList<PostParam>();
            PostParam post = new PostParam("item", text);
            iPostParams.add(post);
            ResponseJSON = PostRequest.execute("http://ec2-54-169-112-228.ap-southeast-1.compute.amazonaws.com/clink/", iPostParams, null);
            try {
                JSONObject data = ResponseJSON.getJSONObject("data");
                name = data.getString("result");
                price = data.getString("price");
                url = data.getString("img");
                category = data.getString("category");
                name = name + " (" + category + ")";
                cat = data.getInt("cat");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("RESPONSE", ResponseJSON.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            tvname.setText(name);
            tvprice.setText("Price: " + price);
            Glide
                    .with(Extract.this)
                    .load(url)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(iv);
            showProgress(false);

        }
    }

}
