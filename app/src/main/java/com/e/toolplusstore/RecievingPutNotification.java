package com.e.toolplusstore;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class RecievingPutNotification extends FirebaseMessagingService {

    public void onMessageRecieved(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> map = remoteMessage.getData();
        String title = map.get("title");
        String description = map.get("body");
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String channelId = " my id";
        String channelName = "my channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this, channelId);
        nb.setContentTitle(title);
        nb.setContentText(description);
        nb.setSmallIcon(R.drawable.logo_white);
        manager.notify(1, nb.build());
    }
}