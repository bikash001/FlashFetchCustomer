package com.buyer.flashfetch;

import android.os.CountDownTimer;
import android.widget.TextView;

public class TimerClass{

    private TextView textView;
    private CountDownTimer tt;

    public TimerClass(long hour,long min, final TextView textView) {
        this.textView = textView;
        tt = new CountDownTimer(hour * 3600000+min * 60000,60000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textView.setText(String.format("%d:%d",millisUntilFinished/3600000,(millisUntilFinished%3600000)/60000));
            }

            @Override
            public void onFinish() {
                textView.setText(String.format("%s","Done"));
            }
        };
    }

    public void start(){
        tt.start();
    }
    public void cancel(){
        tt.cancel();
    }

    public void update(long hour,long min){
        tt.cancel();
        tt = new CountDownTimer(hour * 3600000+min * 60000,60000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textView.setText(String.format("%d:%d",millisUntilFinished/3600000,(millisUntilFinished%3600000)/60000));
            }

            @Override
            public void onFinish() {
                textView.setText(String.format("%s","Done"));
            }
        }.start();
    }
}
