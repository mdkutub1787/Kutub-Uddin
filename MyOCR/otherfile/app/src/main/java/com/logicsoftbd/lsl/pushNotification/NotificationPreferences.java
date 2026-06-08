package com.logicsoftbd.lsl.pushNotification;

import android.content.Context;
import android.content.SharedPreferences;

public class NotificationPreferences {
    private static final String PREF_NAME = "notification_preferences";
    private static final String KEY_SOUND_ENABLED = "sound_enabled";

    public static void setSoundEnabled(Context context, boolean enabled) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_SOUND_ENABLED, enabled);
        editor.apply();

        // After changing the preference, recreate the notification channel.
        NotificationUtils.createNotificationChannel(context);
    }

    public static boolean isSoundEnabled(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_SOUND_ENABLED, true); // Default to true if not set
    }
}
