package com.buyer.flashfetch;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.buyer.flashfetch.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MaxResponse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_max_response);
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
        final Spinner hour=(Spinner)findViewById(R.id.hour),minute=(Spinner)findViewById(R.id.minute),area=(Spinner)findViewById(R.id.areaspinner);
        final EditText areaedittext=(EditText)findViewById(R.id.areaedittext);
        final TextView othertext=(TextView)findViewById(R.id.othertext);
        areaedittext.setVisibility(View.GONE);
        othertext.setVisibility(View.GONE);
        List<String> string=new ArrayList<>();
        Calendar c = Calendar.getInstance();
        int curhour=c.get(Calendar.HOUR_OF_DAY);
        Log.i("abc", String.valueOf(curhour));
        //setting min number of response hours based on current time
        int min=1;
        if(curhour<22&&curhour>10)min=1;
        else if(curhour>=22&&curhour<24)min=34-curhour;
        else min=10-curhour;
        for(int i=min;i<25;++i)
        {string.add(i-min,String.valueOf(i));}
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,
                string.toArray(new String[string.size()]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hour.setAdapter(adapter);
        string.clear();
        for(int i=0;i<60;++i)
        {string.add(i,String.valueOf(i));}
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,
                string.toArray(new String[string.size()]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minute.setAdapter(adapter);
        hour.setSelection(0);minute.setSelection(0);
        //final String[] array=new String[]{"Adyar","Besant Nagar","Guindy","Koyambedu","Nungambakkam","Perambur","Tharamani","Velachery"};
        final String[] array=new String[]{"Adyar","Besant Nagar","Tharamani","Thiruvanmiyur","Velachery","Other"};
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        area.setAdapter(adapter);
        area.setSelection(0);
        area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                area.setSelection(position);
                if (position == 5) {
                    areaedittext.setVisibility(View.VISIBLE);
                    othertext.setVisibility(View.VISIBLE);
                } else {
                    areaedittext.setVisibility(View.GONE);
                    othertext.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
