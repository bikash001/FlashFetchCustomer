package com.buyer.flashfetch.BroadcastReceivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bumptech.glide.util.Util;
import com.buyer.flashfetch.CommonUtils.Utils;
import com.buyer.flashfetch.NearByDealsActivity;
import com.buyer.flashfetch.Objects.Notification;

/**
 * Created by kranthikumar_b on 10/15/2016.
 */

public class RegistrationReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Utils.sendNotification(context, NearByDealsActivity.class, "Welcome Aboard", "We are happy you are here");
    }
}
