package com.buyer.flashfetch;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by KRANTHI on 02-10-2016.
 */
public class BaseFragment extends Fragment {

    public ProgressDialog progressDialog;

    public ProgressDialog getProgressDialog(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        return progressDialog;
    }
}
