package com.example.myrecylverviewapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

    static int notificationID;
    String channel_id = "test";
    String noticeText;
    String noticeTitle;

    public static final String TAG = "MyReceiver: ";

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context, channel_id);

        String id = intent.getStringExtra("ID");
        String name = intent.getStringExtra("Name");

        notificationID = Integer.parseInt(id);

        noticeText = "Assessment: " + name + " is due soon!";
        noticeTitle = name + " Coming Due!";

        Log.d(TAG, "onReceive: notificationID = " + notificationID);
        Log.d(TAG, "onReceive: noticeText = " + noticeText);
        Log.d(TAG, "onReceive: noticeTitle = " + noticeTitle);

        Notification notification = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(noticeText)
                .setContentTitle(noticeTitle)
                .build();

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, notification);
    } // end onReceive

    private void createNotificationChannel(Context context, String channel_id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "term tracker notification channel";
            String description = "notification channel for term tracker";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        } // end if
    } // end createNotificationChannel
} // end MyReceiver
