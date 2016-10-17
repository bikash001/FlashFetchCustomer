package com.buyer.flashfetch.Helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.buyer.flashfetch.Constants.Constants;

/**
 * Created by kranthikumar_b on 10/15/2016.
 */

public class IntentManager {

    public static void launchPlayStore(Context context){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.GOOGLE_PLAY_STORE_URL));
        context.startActivity(intent);
    }
}
