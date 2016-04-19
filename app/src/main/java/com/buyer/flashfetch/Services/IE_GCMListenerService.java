package com.buyer.flashfetch.Services;


import com.buyer.flashfetch.Helper.DatabaseHelper;
import com.buyer.flashfetch.MainActivity;
import com.buyer.flashfetch.R;
import com.google.android.gms.gcm.GcmListenerService;
/*

*
 * Created by kevin selva prasanna on 24-Aug-15.

*/


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;


public class IE_GCMListenerService extends GcmListenerService {

    private static final String TAG = "GcmListenerService";
    String message=" ";
    String data="";
    String type=" ",category,scoreBoardId,title=" ";



/*

*
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().

*/

    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {

        ContentValues cv = new ContentValues();
        cv.put("id",data.getString("cus_id"));
        cv.put("qid",data.getString("sel_id"));
        cv.put("qprice",data.getString("qprice"));
        cv.put("type",data.getString("type"));
        cv.put("deltype",data.getString("deltype"));
        cv.put("comment",data.getString("comment"));
        cv.put("lat",data.getString("lat"));
        cv.put("lon",data.getString("lon"));
        cv.put("distance",data.getString("distance"));
        DatabaseHelper dh = new DatabaseHelper(IE_GCMListenerService.this);
        dh.addQuote(cv);




      /*   * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */
    }



    // [END receive_message]
/*

*
     * Create and show a simple notification containing the received GCM message.
     *
     * message GCM message received.
     */

    private void sendNotification(String email, String category, String price,String id, String time){
        PendingIntent pendingIntent;
        Uri defaultSoundUri;
        NotificationCompat.Builder notificationBuilder;
        Intent i = new Intent(IE_GCMListenerService.this, MainActivity.class);
        NotificationManager notificationManager;
        pendingIntent = PendingIntent.getActivity(this, 0, i,
                PendingIntent.FLAG_ONE_SHOT);



        defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("FlashFetch")
                .setContentText(email + " at price" +  price)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          //  notificationBuilder.setSmallIcon(R.mipmap.nav_transparent)
            //.setColor(getColor(R.color.ff_notif));
        } else {
          //  notificationBuilder.setSmallIcon(R.mipmap.nav);
        }

        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        android.app.Notification not = notificationBuilder.build();
        not.flags = android.app.Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify((int) (Math.random() * 1000), not);


    }
    private void sendNotification(String title, String message){
        PendingIntent pendingIntent;
        Uri defaultSoundUri;
        NotificationCompat.Builder notificationBuilder;
        Intent i = new Intent(IE_GCMListenerService.this,MainActivity.class);
        NotificationManager notificationManager;
        pendingIntent = PendingIntent.getActivity(this, 0, i,
                PendingIntent.FLAG_ONE_SHOT);



        defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          //  notificationBuilder.setSmallIcon(R.mipmap.nav_transparent)
            //        .setColor(getColor(R.color.ff_notif));
        } else {
           // notificationBuilder.setSmallIcon(R.mipmap.nav);
        }

        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify((int)(Math.random()*1000) , notificationBuilder.build());


    }

}
