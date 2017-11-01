package com.example.ankurkhandelwal.datahungry.services;

import android.util.Log;
import com.example.ankurkhandelwal.datahungry.Utils.NotificationBuilder;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class DataHungryFireBaseMsgService extends FirebaseMessagingService {
    private static final String TAG = DataHungryFireBaseMsgService.class.getSimpleName();

    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "in message");
        Log.d(TAG, remoteMessage.getData().toString());
        String title = (String) remoteMessage.getData().get("title");
        String messageToShow = (String) remoteMessage.getData().get("message");
        if (title == null) {
            title = "";
        }
        if (messageToShow == null) {
            messageToShow = "";
        }
        buildNotification(title, messageToShow);
    }

    private void buildNotification(String title, String messageBody) {
        NotificationBuilder.buildNotification(this, title, messageBody);
    }
}
