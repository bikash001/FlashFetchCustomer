package com.example.bikash.flashfetchcustomer;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Extract extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract);
        Button okButton = (Button) findViewById(R.id.ok_extract);
        Button exitButton = (Button) findViewById(R.id.exit_extract);
        TextView textView = (TextView) findViewById(R.id.name_extract);
        ImageView imageView = (ImageView) findViewById(R.id.image_extract);
        Intent intent = getIntent();
        String msg = intent.getStringExtra(Intent.EXTRA_TEXT);
        if( msg != null){
            new Download(imageView).execute(msg);
        }
        else{
            textView.setText(R.string.prompt_extract);
            okButton.setVisibility(View.INVISIBLE);
        }
        okButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ok_extract:
                Intent intent = new Intent(this,Main2Activity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.exit_extract:
                finish();
                break;
        }
    }
    class Download extends AsyncTask<String, Void, Bitmap> {
        ImageView view;

        Download(ImageView imageView){
            view = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                return null;
            }
        }
        protected void onPostExecute(Bitmap bitmap) {
            view.setImageBitmap(bitmap);
        }
    }
}
