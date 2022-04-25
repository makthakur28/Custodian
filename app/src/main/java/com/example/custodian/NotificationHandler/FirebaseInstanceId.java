package com.example.custodian.NotificationHandler;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessaging;

public class FirebaseInstanceId extends FirebaseMessagingService{

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = String.valueOf(FirebaseMessaging.getInstance().getToken());
        Log.d("MyToken", "Refreshed token: " + refreshedToken);

    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }
}
