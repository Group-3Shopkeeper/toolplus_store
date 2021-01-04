package com.e.toolplusstore;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;


public class FirebaseReceivingNotification extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String channelId = "my id";
        String channelName = "my channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.enableVibration(true);
            manager.createNotificationChannel(channel);
        }
        Intent intent = new Intent(this, OrderHistoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder nb = new NotificationCompat.Builder(this, channelId);
        nb.setContentTitle(title);
        nb.setContentText(message);
        nb.setSmallIcon(R.drawable.logo_white).setSound(notificationSound)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        manager.notify(1, nb.build());
    }
}