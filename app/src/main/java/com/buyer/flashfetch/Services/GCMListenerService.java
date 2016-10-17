package com.buyer.flashfetch.Services;


import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;

import com.buyer.flashfetch.CommonUtils.Utils;
import com.buyer.flashfetch.Helper.DatabaseHelper;
import com.buyer.flashfetch.NearByDealsActivity;
import com.buyer.flashfetch.Objects.Notification;
import com.buyer.flashfetch.Objects.Quote;
import com.google.android.gms.gcm.GcmListenerService;

public class GCMListenerService extends GcmListenerService {

    private static final String TAG = GCMListenerService.class.getSimpleName();

    private static final String SELLER_ID = "sel_id";
    private static final String PRODUCT_NAME = "pname";
    private static final String BARGAINED = "bargained";
    private static final String QUOTED = "quoted";
    private static final String QUOTE_PRICE = "qprice";
    private static final String COMMENTS = "comment";
    private static final String PRODUCT_TYPE = "prtype";
    private static final String DELIVERY_TYPE = "deltype";
    private static final String LATITUDE = "lat";
    private static final String LONGITUDE = "lon";
    private static final String SELLER_CONFIRMATION = "selcon";
    private static final String BUYER_CONFIRMATION = "cuscon";

    /*
     * Called when message is received.
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
    */

    @Override
    public void onMessageReceived(String from, Bundle data) {

        if (data.getInt(BARGAINED) == 1) {

            ContentValues cv = new ContentValues();

            cv.put("selcon", data.getInt("selcon"));
            cv.put("cuscon", data.getInt("cuscon"));
            cv.put("qid", data.getString("selid"));

            DatabaseHelper dh = new DatabaseHelper(this);
            dh.updateQuote(data.getString("id"), cv);

//            sendNotification("Bargain", "bargain");

        } else if(data.getInt(QUOTED) == 1){

            Log.d("gcm", "GCM MESSAGE : " + data.toString());

            DatabaseHelper databaseHelper = new DatabaseHelper(GCMListenerService.this);

            ContentValues contentValues = new ContentValues();

            contentValues.put(Quote.QUOTE_ID, Math.random());
            contentValues.put(Quote.SELLER_ID, data.getString(SELLER_ID));
            contentValues.put(Quote.PRODUCT_NAME, data.getString(PRODUCT_NAME));
            contentValues.put(Quote.QUOTE_PRICE, data.getString(QUOTE_PRICE));
            contentValues.put(Quote.COMMENTS, data.getString(COMMENTS));
            contentValues.put(Quote.PRODUCT_TYPE, data.getInt(PRODUCT_TYPE));
            contentValues.put(Quote.SELLER_DELIVERY_TYPE, data.getString(DELIVERY_TYPE));
            contentValues.put(Quote.LATITUDE, data.getString(LATITUDE));
            contentValues.put(Quote.LONGITUDE, data.getString(LONGITUDE));


            if(data.getInt(BARGAINED) == 0){
                //This is the first quote from seller after the request has been put by the buyer

                contentValues.put(Quote.BARGAINED,false);
                databaseHelper.addQuote(contentValues);
//                sendNotification("Quote", "You have a new quote for a " + data.getString(PRODUCT_NAME));

            }else if(data.getInt(BARGAINED) == 1){
                // The user has bargained once

                contentValues.put(Quote.BARGAINED,true);

                if(data.getInt(SELLER_CONFIRMATION) > 0){
                    // Seller has accepted your bargain request

                    contentValues.put(Quote.SELLER_CONFIRMATION,true);
//                    sendNotification("Quote", "Seller has accepted your bargain request for the " + data.getString(PRODUCT_NAME));
                }

                databaseHelper.updateQuote(data.getString(SELLER_ID),contentValues);
//                Utils.sendNotification(this, NearByDealsActivity.class, data.getString("heading"), data.getString("text"));
//                sendNotification("Quote", "You have a new bargain for a " + data.getString(PRODUCT_NAME));
            }
        }else{
            ContentValues contentValues = new ContentValues();

            contentValues.put(Notification.NOTIFICATION_ID, System.currentTimeMillis() + "");
            contentValues.put(Notification.NOTIFICATION_HEADING, data.getString("heading"));
            contentValues.put(Notification.NOTIFICATION_DESCRIPTION, data.getString("text"));
            contentValues.put(Notification.NOTIFICATION_IMAGE_URL, data.getString("image"));

            DatabaseHelper databaseHelper = new DatabaseHelper(GCMListenerService.this);
            databaseHelper.addNotification(contentValues);

            Utils.sendNotification(this, Notification.class, data.getString("heading"), data.getString("text"));
        }
    }
}
