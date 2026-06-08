package com.logicsoftbd.lsl.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {
    /*logCat tag*/
    private static String TAG = SessionManager.class.getSimpleName();

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Context _context;
    //Shared pref mode
    int PRIVATE_MODE = 0;

    //Shared preference file name
    private static final String PREF_NAME = "LogicSoftLogin";
    private static final String KEY_IS_LOGEDIN = "isLoggedIn";

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn){
        editor.putBoolean(KEY_IS_LOGEDIN, isLoggedIn);
        //comit changes
        editor.commit();
        Log.d(TAG, "User login session modified");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGEDIN, false);
    }
}

