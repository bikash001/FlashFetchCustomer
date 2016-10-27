package com.buyer.flashfetch.FireBaseCloudMessaging;

import android.app.IntentService;
import android.content.Intent;

import com.buyer.flashfetch.Constants.URLConstants;
import com.buyer.flashfetch.Network.PostRequest;
import com.buyer.flashfetch.Objects.PostParam;
import com.buyer.flashfetch.Objects.UserProfile;
import com.google.android.gms.gcm.GcmPubSub;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class RegistrationService extends IntentService {

    private static final String TAG = RegistrationService.class.getSimpleName();
    private static final String[] TOPICS = {"global"};

    public RegistrationService() {
        super(TAG);
    }
    @Override
    protected void onHandleIntent(Intent intent) {

        sendRegistrationToServer(UserProfile.getGcmToken(this));

//        subscribeTopics(refreshedGCMToken);
    }

    private void sendRegistrationToServer(String refreshedGCMToken) {
        ArrayList<PostParam> postParams = new ArrayList<>();

        postParams.add(new PostParam("mobile", UserProfile.getPhone(RegistrationService.this)));
        postParams.add(new PostParam("gcmid",refreshedGCMToken));
        postParams.add(new PostParam("token",UserProfile.getToken(RegistrationService.this)));

        JSONObject response = PostRequest.execute(URLConstants.URL_GCM_REGISTER, postParams, null);
    }

    private void subscribeTopics(String token) throws IOException {
        for (String topic : TOPICS) {
            GcmPubSub pubSub = GcmPubSub.getInstance(this);
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
}
