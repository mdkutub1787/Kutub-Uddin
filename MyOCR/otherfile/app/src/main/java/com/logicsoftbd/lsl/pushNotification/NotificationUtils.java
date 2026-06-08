package com.logicsoftbd.lsl.pushNotification;

import static com.logicsoftbd.lsl.pushNotification.NotificationPreferences.isSoundEnabled;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class NotificationUtils {
    public static final String CHANNEL_ID = "alert_001";
    public static final String CHANNEL_NAME = "Alert";

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            // Set the sound for the channel based on user preference.
            if (isSoundEnabled(context)) {
                channel.setSound(null, null); // Replace with your sound URI
            } else {
                channel.setSound(null, null); // Set sound to null for silent channel
            }

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
