package com.buyer.flashfetch.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean isAeroplaneModeOn = intent.getBooleanExtra("state", false);
    }
}
