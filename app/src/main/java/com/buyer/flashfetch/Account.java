package com.buyer.flashfetch;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Account extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout layout;
    private LinearLayout.LayoutParams params;
    private TextView button, tag, address,name,phone;
    final static int STATUS = 1;
    private int height, width;
    private EditText input;
    private TextView cancel,confirm,popup_text;
    private Dialog dialog;
    private DialogHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        height = WindowManager.LayoutParams.WRAP_CONTENT;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        layout = (LinearLayout) findViewById(R.id.layout_address);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        button = (TextView) findViewById(R.id.button_add_address);
        button.setOnClickListener(this);
        name = (TextView) findViewById(R.id.profile_name);
        name.setOnClickListener(this);
        phone = (TextView) findViewById(R.id.phone_profile);
        phone.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == STATUS){
            View view = getLayoutInflater().inflate(R.layout.address,layout,false);
            tag = (TextView) view.findViewById(R.id.address_tag);
            address = (TextView) view.findViewById(R.id.address_content);
            tag.setText(data.getStringExtra("TAG"));
            String ss = String.format("%s, %s, %s, %s, %s",data.getStringExtra("ADDRESSLINE"),data.getStringExtra("AREA"),
                    data.getStringExtra("CITY"),data.getStringExtra("STATE"),data.getStringExtra("PIN"));
            address.setText(ss);
            layout.addView(view);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.button_add_address) {
            Intent intent = new Intent(this,Fill_Address.class);
            startActivityForResult(intent,STATUS);
        }
        else if(id == R.id.profile_name || id == R.id.phone_profile){
            dialog = new Dialog(v.getContext());
            handler = new DialogHandler();
            dialog.setContentView(R.layout.popup);
            input = (EditText) dialog.findViewById(R.id.input_popup);
            cancel = (TextView) dialog.findViewById(R.id.cancel_popup);
            cancel.setOnClickListener(handler);
            confirm = (TextView) dialog.findViewById(R.id.confirm_popup);
            confirm.setOnClickListener(handler);
            popup_text = (TextView) dialog.findViewById(R.id.text_popup);
            dialog.getWindow().setLayout((int) (width * 0.8), height);
            if(id == R.id.profile_name){
                popup_text.setText("Enter your name");
                input.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            } else {
                popup_text.setText("Enter your phone number");
                input.setInputType(InputType.TYPE_CLASS_PHONE);
            }
            dialog.show();
        }
    }

    public class DialogHandler implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.cancel_popup){
                dialog.dismiss();
            }
            else{
                if(input.getInputType()==InputType.TYPE_TEXT_VARIATION_PERSON_NAME) {
                    name.setText(input.getText().toString());
                }
                else{
                    phone.setText(input.getText().toString());
                }
                dialog.dismiss();
            }
        }
    }
}
