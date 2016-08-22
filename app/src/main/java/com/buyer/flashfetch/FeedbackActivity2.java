package com.buyer.flashfetch;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.buyer.flashfetch.R;

public class FeedbackActivity2 extends BaseActivity {

    boolean app=true,del=true,serv=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RatingBar appbar=(RatingBar)findViewById(R.id.appRatingBar);
        RatingBar delbar=(RatingBar)findViewById(R.id.deliveryRatingBar);
        RatingBar servbar=(RatingBar)findViewById(R.id.serviceRatingBar);
        final EditText feedback=(EditText)findViewById(R.id.feedbackcomment);
        feedback.setVisibility(View.GONE);
        RatingBar.OnRatingBarChangeListener l=new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating<3) {
                    switch(ratingBar.getId()){
                        case R.id.appRatingBar: app=false; break;
                        case R.id.deliveryRatingBar: del=false;break;
                        case R.id.serviceRatingBar: serv=false;break;
                    }
                }
                if(!(app&&del&&serv)) feedback.setVisibility(View.VISIBLE);
                else feedback.setVisibility(View.GONE);
            }
        };
        appbar.setOnRatingBarChangeListener(l);
        delbar.setOnRatingBarChangeListener(l);
        servbar.setOnRatingBarChangeListener(l);
    }
}
