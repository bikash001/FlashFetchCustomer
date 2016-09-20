package com.buyer.flashfetch;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
