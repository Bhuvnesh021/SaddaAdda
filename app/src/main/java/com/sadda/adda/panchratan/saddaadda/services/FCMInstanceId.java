package com.sadda.adda.panchratan.saddaadda.services;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by user on 08-07-2017.
 */
public class FCMInstanceId extends FirebaseInstanceIdService{
    private static final String TAG = "FCMInstanceId";
    @Override
    public void onTokenRefresh() {
        Log.d(TAG, "onTokenRefresh() called with: ");
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "onTokenRefresh: token: "+token);
        SharedPreferences sharedPreferences = getSharedPreferences("fcm_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fcm_token", token);
        editor.commit();
    }
}
