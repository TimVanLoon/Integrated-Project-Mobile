package com.example.keiichi.project_mobile;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by keanuvanhees on 16/01/18.
 */

public class FirebaseInstance extends FirebaseInstanceIdService {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        AccessTokenSingleton ats = AccessTokenSingleton.getInstance();
        ats.setFirebaseToken(refreshedToken);
    }
}
