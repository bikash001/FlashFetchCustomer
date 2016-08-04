package com.buyer.flashfetch;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by kranthikumar_b on 8/4/2016.
 */
public class BaseActivity extends AppCompatActivity {

    public ProgressDialog progressDialog;

    public ProgressDialog getProgressDialog(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        return progressDialog;
    }
}
