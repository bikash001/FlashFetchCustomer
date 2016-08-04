package com.buyer.flashfetch.Services;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.buyer.flashfetch.Helper.DatabaseHelper;
import com.buyer.flashfetch.MainActivity;
import com.buyer.flashfetch.R;
import com.google.android.gms.gcm.GcmListenerService;

/*

*
 * Created by kevin selva prasanna on 24-Aug-15.

*/


public class IE_GCMListenerService extends GcmListenerService {

    private static final String TAG = "GCMListenerService";
    String message = " ";
    String data = "";
    String type = " ", category, scoreBoardId, title = " ";

    /*
     * Called when message is received.
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
    */

    @Override
    public void onMessageReceived(String from, Bundle data) {

        if (data.getInt("bargained") == 1) {

            ContentValues cv = new ContentValues();
            cv.put("selcon", data.getInt("selcon"));
            cv.put("cuscon", data.getInt("cuscon"));
            cv.put("qid", data.getString("selid"));

            DatabaseHelper dh = new DatabaseHelper(this);
            dh.updateQuote(data.getString("id"), cv);

            sendNotification("Bargain", "bargain");
        } else {
            Log.d("gcm", "GCM MESSAGE : " + data.toString());

            ContentValues cv = new ContentValues();
            cv.put("id", data.getString("cus_id"));
            cv.put("qid", data.getString("sel_id"));
            cv.put("qprice", data.getString("qprice"));
            cv.put("type", data.getString("prtype"));
            cv.put("deltype", data.getString("deltype"));
            cv.put("comment", data.getString("comment"));
            cv.put("lat", data.getString("lat"));
            cv.put("long", data.getString("lon"));
            cv.put("distance", data.getString("distance"));

            DatabaseHelper dh = new DatabaseHelper(IE_GCMListenerService.this);
            dh.addQuote(cv);

            sendNotification("New Quote", "You have a new quote for a product");
        }
    }

    /*
     * Create and show a simple notification containing the received GCM message.
     *
     * message GCM message received.
     */

    private void sendNotification(String title, String message) {

        PendingIntent pendingIntent;
        Uri defaultSoundUri;
        NotificationCompat.Builder notificationBuilder;
        NotificationManager notificationManager;

        Intent i = new Intent(IE_GCMListenerService.this, MainActivity.class);

        pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT);

        defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher).setColor(Color.RED);
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) (Math.random() * 1000), notificationBuilder.build());
    }

}
