package com.sadda.adda.panchratan.saddaadda.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sadda.adda.panchratan.saddaadda.R;
import com.sadda.adda.panchratan.saddaadda.activities.MainActivity;

/**
 * Created by user on 08-07-2017.
 */
public class FCMMessagingService extends FirebaseMessagingService{
    private static final String TAG = "FCMMessagingService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
if(remoteMessage == null){
    return;
}
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        Log.i(TAG, "onMessageReceived: title: "+title);
        Log.i(TAG, "onMessageReceived: body: "+body);
        Intent  intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this);
        noBuilder.setContentTitle(getString(R.string.app_name));
        noBuilder.setContentText(body);
        noBuilder.setSmallIcon(R.mipmap.ic_launcher);
        noBuilder.setAutoCancel(true);
        noBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0,noBuilder.build());
    }
}
