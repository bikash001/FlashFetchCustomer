package com.buyer.flashfetch.Objects;

import android.content.Context;
import android.database.Cursor;

import com.buyer.flashfetch.Helper.DatabaseHelper;

import java.util.ArrayList;

/**
 * Created by KRANTHI on 21-08-2016.
 */
public class Notification {

    public static final String NOTIFICATION_ID = "notificationId";
    public static final String NOTIFICATION_HEADING = "heading";
    public static final String NOTIFICATION_DESCRIPTION = "description";
    public static final String NOTIFICATION_IMAGE_URL = "imageURL";
    public static final String NOTIFICATION_EXP_TIME = "time";

    public static String[] COLUMNS = {"notificationId","heading","description","imageURL","time"};
    public static String TABLE_NAME = "Notifications";

    public int notificationId;
    public String heading, description, imageURL;
    public long timeInMillis;

    public Notification(int notificationId, String heading, String description, String imageURL, long timeInMillis){
        this.notificationId = notificationId;
        this.heading = heading;
        this.description = description;
        this.imageURL = imageURL;
        this.timeInMillis = timeInMillis;
    }

    public static ArrayList<Notification> getArrayList(Cursor c) {
        ArrayList<Notification> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            arrayList.add(parseNotification(c));
        }
        return arrayList;
    }

    public static Notification parseNotification(Cursor c) {
        Notification notification = new Notification(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getLong(4));
        return notification;
    }

    public static ArrayList<Notification> getAllNotifications(Context context){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        return databaseHelper.getAllNotifications();
    }



    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }
}
