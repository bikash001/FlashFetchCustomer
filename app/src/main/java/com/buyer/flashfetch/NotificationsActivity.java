package com.buyer.flashfetch;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.buyer.flashfetch.Adapters.NotificationsAdapter;
import com.buyer.flashfetch.Helper.DatabaseHelper;
import com.buyer.flashfetch.Objects.Notification;

/**
 * Created by KRANTHI on 21-08-2016.
 */
public class NotificationsActivity extends BaseActivity{

    private Context context;
    private NotificationsAdapter notificationsAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = NotificationsActivity.this;

        setContentView(R.layout.notifications_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Notifications");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        ContentValues contentValues = new ContentValues();

        contentValues.put(Notification.NOTIFICATION_ID, 1234);
        contentValues.put(Notification.NOTIFICATION_HEADING, "Welcome to Family");
        contentValues.put(Notification.NOTIFICATION_DESCRIPTION, "Let us serve you with the best deals around you.");
        contentValues.put(Notification.NOTIFICATION_IMAGE_URL, "https://s5.postimg.org/kyy74k5qf/Welcome_Notification_Final.png");
        contentValues.put(Notification.NOTIFICATION_EXP_TIME, System.currentTimeMillis() + 5*60*1000);

        databaseHelper.addNotification(contentValues);

        notificationsAdapter = new NotificationsAdapter(context, Notification.getAllNotifications(context));

        recyclerView = (RecyclerView)findViewById(R.id.notifications_recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(notificationsAdapter);
    }
}
