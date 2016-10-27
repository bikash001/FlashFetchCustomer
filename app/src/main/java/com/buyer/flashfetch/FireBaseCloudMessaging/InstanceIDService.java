package com.buyer.flashfetch.FireBaseCloudMessaging;

import com.buyer.flashfetch.Objects.UserProfile;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class InstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        String oldGCMToken = UserProfile.getGcmToken(this);
        String refreshedGCMToken = FirebaseInstanceId.getInstance().getToken();

        if(!refreshedGCMToken.equalsIgnoreCase(oldGCMToken)){
            UserProfile.setGCMToken(refreshedGCMToken, this);
        }
    }
}
