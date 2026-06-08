/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.logicsoftbd.lsl.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.db.v1_db.helper.DBAdapter;
import com.logicsoftbd.lsl.data.prefs.SessionManager;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.base.BaseActivity;
import com.logicsoftbd.lsl.ui.main.MainActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.about.PrintTestActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.login.V1_LoginActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.menu.V1_MenuActivity;
import com.logicsoftbd.lsl.utils.ApiUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;


/**
 * Created by janisharali on 27/01/17.
 */

public class SplashActivity extends BaseActivity implements SplashMvpView {
    private static final String TAG = "SplashActivity";
    private SessionManager session;

    private ApiInterface apiInterface;
    private ApiUtils apiUtils;

    @Inject
    SplashMvpPresenter<SplashMvpView, SplashMvpInteractor> mPresenter;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        session = new SessionManager(getApplicationContext());
        DBAdapter dbAdapter = new DBAdapter(this);




        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        WebView wView = findViewById(R.id.image_button_profile1);
        WebSettings webSettings = wView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        String file = "file:android_asset/rectingle.gif";
        wView.loadUrl(file);
        
        mPresenter.onAttach(SplashActivity.this);

        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        Log.d(TAG, "handleIntent: "+intent.toString());
        Log.d(TAG, "handleIntent: "+intent.getStringExtra("cmp"));
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals("android.intent.action.MAIN")) {
                // The app was launched from the launcher icon
            } else if (intent.getAction().equals("your.custom.action.FCM_NOTIFICATION")) {
                // The app was launched from an FCM notification
                // Handle the notification here, if needed
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    /**
     * Making the screen wait so that the  branding can be shown
     */
    @Override
    public void openLoginActivity() {
        if (session.isLoggedIn()) {
//            Intent intent = V1_MenuActivity.getStartIntent(SplashActivity.this);
//            startActivity(intent);
//            finish();

            Intent intent = V1_MenuActivity.getStartIntent(SplashActivity.this);
            startActivity(intent);
            finish();
        }else {
            Intent intent = V1_LoginActivity.getStartIntent(SplashActivity.this);
            startActivity(intent);
            finish();

//            Intent intent = new Intent(SplashActivity.this, EditImageActivity.class);
//            startActivity(intent);
//            finish();
        }
    }

    @Override
    public void openMainActivity() {
        if (session.isLoggedIn()) {
            Intent intent = V1_MenuActivity.getStartIntent(SplashActivity.this);
            startActivity(intent);
            finish();
        } else {
            Intent intent = MainActivity.getStartIntent(SplashActivity.this);
            startActivity(intent);
            finish();

        }

    }

    @Override
    public void startSyncService() {
//        SyncService.start(this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void setUp() {

    }
}
