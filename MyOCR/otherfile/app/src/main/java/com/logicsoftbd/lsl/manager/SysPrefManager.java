package com.logicsoftbd.lsl.manager;

import android.content.Context;
import android.content.SharedPreferences;

public class SysPrefManager {

    private final Context context;
    private SharedPreferences sharedPreferences;

    private void getSharedPreference() {
        sharedPreferences = context.getSharedPreferences("SysPerf", Context.MODE_PRIVATE);
    }

    public SysPrefManager(Context context) {
        this.context = context;
        getSharedPreference();
    }

    public void setValue(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getValue(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void setIntValue(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getIntValue(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public void setBool(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBool(String key) {
        return sharedPreferences.getBoolean(key, true);
    }

    public void clearPreference() {
        sharedPreferences.edit().clear().apply();
    }
}
