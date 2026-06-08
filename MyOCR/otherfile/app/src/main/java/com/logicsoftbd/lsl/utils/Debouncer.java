package com.logicsoftbd.lsl.utils;

import android.os.Handler;
import android.os.Looper;

public class Debouncer {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable debounceRunnable;

    /**
     * Executes the given action after the specified delay, canceling any previous calls.
     *
     * @param action Action to execute
     * @param delayMillis Delay in milliseconds
     */
    public void debounce(Runnable action, long delayMillis) {
        if (debounceRunnable != null) {
            handler.removeCallbacks(debounceRunnable);
        }
        debounceRunnable = action;
        handler.postDelayed(debounceRunnable, delayMillis);
    }
}

