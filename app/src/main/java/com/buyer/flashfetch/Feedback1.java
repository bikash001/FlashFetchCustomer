package com.buyer.flashfetch;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyer.flashfetch.R;

public class Feedback1 extends AppCompatActivity {

    ImageButton sad,happy,neutral;
    Button submit;
    TextView unsatisfied_text,satisfied_text,neutral_text,htext,htext1,comment_text,comment_text1;
    LinearLayout hlayout;Button hrefer;
    EditText comment,comment1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.GONE);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        sad=(ImageButton)findViewById(R.id.sad);
        neutral=(ImageButton)findViewById(R.id.neutral);
        happy=(ImageButton)findViewById(R.id.happy);
        unsatisfied_text=(TextView)findViewById(R.id.unsatisfied);
        neutral_text=(TextView)findViewById(R.id.neutral_text);
        satisfied_text=(TextView)findViewById(R.id.satisfied);
        comment=(EditText)findViewById(R.id.commentString);
        submit=(Button)findViewById(R.id.submit);
        comment_text=(TextView)findViewById(R.id.commentText);
        htext=(TextView)findViewById(R.id.htext);
        htext1=(TextView)findViewById(R.id.htext1);
        hlayout=(LinearLayout)findViewById(R.id.hlayout);
        hrefer=(Button)findViewById(R.id.hrefer);
        htext.setVisibility(View.GONE);
        htext1.setVisibility(View.GONE);
        hlayout.setVisibility(View.GONE);
        hrefer.setVisibility(View.GONE);
        comment.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);
        comment_text.setVisibility(View.GONE);
        comment_text1=new TextView(this);
        comment1=new EditText(this);
        LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(width*3/10,width*3/10);
        sad.setLayoutParams(lp);
        neutral.setLayoutParams(lp);
        happy.setLayoutParams(lp);
        comment_text1.setText("Comments:");
        comment_text1.setTextSize(24);
        comment_text1.setTextColor(Color.parseColor("#FFFFFF"));
        comment1.setTextColor(Color.parseColor("#FFFFFF"));
        comment1.setHint("Tell us how we can improve");
        comment1.setHintTextColor(Color.parseColor("#FFFFFF"));
        comment1.setTextSize(15);
        comment1.setBackground(getResources().getDrawable(R.drawable.round));
        LinearLayout.LayoutParams lp1=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp1.setMargins(10, 0, 0, 15);
        comment_text1.setPadding(8, 0, 0, 0);
        comment_text1.setLayoutParams(lp1);
        comment1.setPadding(8, 12, 8, 12);
        comment1.setLayoutParams(lp1);
        LinearLayout ll= (LinearLayout)findViewById(R.id.root);
        ll.addView(comment_text1,3);
        ll.addView(comment1, 4);
        comment_text1.setVisibility(View.GONE);
        comment1.setVisibility(View.GONE);
        lp.setMargins(10, 0, 0, 20);
        unsatisfied_text.setLayoutParams(lp);
        satisfied_text.setLayoutParams(lp);
        neutral_text.setLayoutParams(lp);

        }
    public void abc(View v) {
        switch (v.getId()) {
            case R.id.sad:
                if (comment.getVisibility() == View.VISIBLE) {
                    sad.setImageResource(R.drawable.sad);
                    comment.setVisibility(View.GONE);
                    submit.setVisibility(View.GONE);
                    comment_text.setVisibility(View.GONE);
                    break;
                }
                sad.setImageResource(R.drawable.sadorange);
                neutral.setImageResource(R.drawable.neutral);
                happy.setImageResource(R.drawable.happy);
                comment.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
                comment_text.setVisibility(View.VISIBLE);
                comment1.setVisibility(View.GONE);
                comment_text1.setVisibility(View.GONE);
                htext.setVisibility(View.GONE);
                hlayout.setVisibility(View.GONE);

                break;
            case R.id.neutral:
                if (comment1.getVisibility() == View.VISIBLE) {
                    neutral.setImageResource(R.drawable.neutral);
                    comment1.setVisibility(View.GONE);
                    submit.setVisibility(View.GONE);
                    comment_text1.setVisibility(View.GONE);
                    break;
                }
                neutral.setImageResource(R.drawable.neutralorange);
                sad.setImageResource(R.drawable.sad);
                happy.setImageResource(R.drawable.happy);
                comment.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
                comment_text.setVisibility(View.GONE);
                comment1.setVisibility(View.VISIBLE);
                comment_text1.setVisibility(View.VISIBLE);
                htext.setVisibility(View.GONE);
                hlayout.setVisibility(View.GONE);
                break;
            case R.id.happy:
                if (htext.getVisibility() == View.VISIBLE) {
                    htext.setVisibility(View.GONE);
                    htext1.setVisibility(View.GONE);
                    hlayout.setVisibility(View.GONE);
                    hrefer.setVisibility(View.GONE);
                    happy.setImageResource(R.drawable.happy);
                    submit.setVisibility(View.GONE);
                    break;
                }
                happy.setImageResource(R.drawable.happyorange);
                sad.setImageResource(R.drawable.sad);
                neutral.setImageResource(R.drawable.neutral);
                comment.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
                comment_text.setVisibility(View.GONE);
                comment1.setVisibility(View.GONE);
                comment_text1.setVisibility(View.GONE);
                htext.setVisibility(View.VISIBLE);
                hlayout.setVisibility(View.VISIBLE);
                hrefer.setVisibility(View.GONE);
                break;
        }
    }
    public void referclick(View v){
        //startActivity(new Intent(this,ReferAndEarn.class));
    }
    public void referyes(View v){
        htext1.setVisibility(View.VISIBLE);
        hrefer.setVisibility(View.VISIBLE);
        submit.setVisibility(View.GONE);
    }
    public void referno(View v){
        htext1.setVisibility(View.GONE);
        hrefer.setVisibility(View.GONE);
        submit.setVisibility(View.VISIBLE);
    }
    public void submit(View v){
        startActivity(new Intent(this, Feedback1.class));
    }
}
