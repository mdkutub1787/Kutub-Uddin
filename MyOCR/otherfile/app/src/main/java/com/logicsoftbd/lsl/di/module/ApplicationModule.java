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

package com.logicsoftbd.lsl.di.module;

import android.app.Application;
import android.content.Context;

import com.logicsoftbd.lsl.BuildConfig;
import com.logicsoftbd.lsl.data.db.DbOpenHelper;
import com.logicsoftbd.lsl.data.db.model.DaoMaster;
import com.logicsoftbd.lsl.data.db.model.DaoSession;
import com.logicsoftbd.lsl.data.network.ApiHeader;
import com.logicsoftbd.lsl.data.network.ApiHelper;
import com.logicsoftbd.lsl.data.network.AppApiHelper;
import com.logicsoftbd.lsl.data.prefs.AppPreferencesHelper;
import com.logicsoftbd.lsl.data.prefs.PreferencesHelper;
import com.logicsoftbd.lsl.di.ApiInfo;
import com.logicsoftbd.lsl.di.ApplicationContext;
import com.logicsoftbd.lsl.di.DatabaseInfo;
import com.logicsoftbd.lsl.di.PreferenceInfo;
import com.logicsoftbd.lsl.utils.AppConstants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


/**
 * Created by janisharali on 27/01/17.
 */

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return AppConstants.DB_NAME;
    }

    @Provides
    @ApiInfo
    String provideApiKey() {
        return BuildConfig.API_KEY;
    }

    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return AppConstants.PREF_NAME;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }

    @Provides
    @Singleton
    ApiHelper provideApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }

    @Provides
    @Singleton
    ApiHeader.ProtectedApiHeader provideProtectedApiHeader(
            @ApiInfo String apiKey,
            PreferencesHelper preferencesHelper) {
        return new ApiHeader.ProtectedApiHeader(
                apiKey,
                preferencesHelper.getCurrentUserId(),
                preferencesHelper.getAccessToken());
    }



    @Provides
    @Singleton
    DaoSession provideDaoSession(DbOpenHelper dbOpenHelper) {
        return new DaoMaster(dbOpenHelper.getWritableDb()).newSession();
    }
}
