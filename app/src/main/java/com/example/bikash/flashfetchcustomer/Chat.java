package com.example.bikash.flashfetchcustomer;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Chat extends AppCompatActivity implements View.OnClickListener{
    private EditText editText;
    private Button button;
    private ListView listView;
    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        button = (Button) findViewById(R.id.chat_send);
        button.setOnClickListener(this);
        editText = (EditText) findViewById(R.id.message);
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode ==
                        KeyEvent.KEYCODE_ENTER)) {
                    return sendMessage();
                }
                return false;
            }
        });
        adapter = new ChatAdapter(getApplicationContext(),R.layout.activity_chat);
        listView = (ListView) findViewById(R.id.text_message_pane);
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        sendMessage();
    }
    private boolean sendMessage(){
        adapter.add(editText.getText().toString());
        editText.setText(null);
        adapter.notifyDataSetChanged();
        return true;
    }

    private class ChatAdapter extends ArrayAdapter<String>{
        private TextView textView;
        private ArrayList list;

        public ChatAdapter(Context context, int resource) {
            super(context, resource);
            list = new ArrayList();
        }

        @Override
        public void add(String object) {
            list.add(object);
        }

        @Override
        public void clear() {
            list.clear();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position).toString();
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if(v==null){
                LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v =inflater.inflate(R.layout.chat, parent,false);
            }
            String messageobj = getItem(position);
            textView =(TextView)v.findViewById(R.id.SingleMessage);
            textView.setText(messageobj);
            textView.setBackgroundResource(R.color.colorPrimary);
            return v;
        }
    }
}
