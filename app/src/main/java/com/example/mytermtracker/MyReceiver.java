package com.example.mytermtracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class MyReceiver extends BroadcastReceiver {

    static int notificationID;
    String channel_id;
    String noticeText;
    String noticeTitle;

    @Override
    public void onReceive(Context context, Intent intent) {

        channel_id = intent.getStringExtra("Channel");
        String id = intent.getStringExtra("ID");
        String name = intent.getStringExtra("Name");

        createNotificationChannel(context, channel_id);

        notificationID = Integer.parseInt(id);

        noticeText = channel_id + ": " + name + " is soon due!";
        noticeTitle = channel_id + " Due Date Alert!";

        Notification notification = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.ic_alert)
                .setContentText(noticeText)
                .setContentTitle(noticeTitle)
                .build();

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, notification);
    } // end onReceive

    private void createNotificationChannel(Context context, String channel_id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "TermTrack notifications";
            String description = "Notifications for Courses and Assessments.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        } // end if
    } // end createNotificationChannel
} // end MyReceiver
