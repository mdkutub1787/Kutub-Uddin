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

package com.logicsoftbd.lsl.di.component;

import com.logicsoftbd.lsl.di.PerActivity;
import com.logicsoftbd.lsl.di.module.ActivityModule;
import com.logicsoftbd.lsl.ui.about.AboutFragment;
import com.logicsoftbd.lsl.ui.feed.FeedActivity;
import com.logicsoftbd.lsl.ui.feed.blogs.BlogFragment;
import com.logicsoftbd.lsl.ui.feed.opensource.OpenSourceFragment;
import com.logicsoftbd.lsl.ui.login.LoginActivity;
import com.logicsoftbd.lsl.ui.main.MainActivity;
import com.logicsoftbd.lsl.ui.main.rating.RateUsDialog;
import com.logicsoftbd.lsl.ui.process.ProcessFragment;
import com.logicsoftbd.lsl.ui.process.greyroll.CuttingQcActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.CuttingQcActivity_test;
import com.logicsoftbd.lsl.ui.process.greyroll.FinishFabricInputActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.FinishFabricIssueRollActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.FinishFabricIssueRollReceiveActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.FinishFabricQrCodeActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.FinishFabricResultSetActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.IssueActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.IssueWorkActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.KnittingQcResultEntryActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.ReceiveActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.ReceiveWorkActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.SewingInputActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.dialog.SewingDialog;
import com.logicsoftbd.lsl.ui.process.quantityactivity.FinishFabricIOQuantityActivity;
import com.logicsoftbd.lsl.ui.process.scanprocess.ScannerActivity;
import com.logicsoftbd.lsl.ui.splash.SplashActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_ScannerActivity;

import dagger.Component;

/**
 * Created by janisharali on 27/01/17.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

    void inject(LoginActivity activity);

    void inject(SplashActivity activity);

    void inject(FeedActivity activity);

    void inject(AboutFragment fragment);

    void inject(OpenSourceFragment fragment);

    void inject(BlogFragment fragment);

    void inject(RateUsDialog dialog);

    void inject(ProcessFragment fragment);

    void inject(ScannerActivity activity);

    void inject(V1_ScannerActivity activity);

    void inject(ReceiveActivity activity);

    void inject(IssueActivity activity);

    void inject(SewingInputActivity activity);
    void inject(FinishFabricIOQuantityActivity activity);
    void inject(FinishFabricInputActivity activity);
    void inject(FinishFabricIssueRollActivity activity);
    void inject(FinishFabricIssueRollReceiveActivity activity);
    void inject(FinishFabricQrCodeActivity activity);
    void inject(CuttingQcActivity activity);
    void inject(CuttingQcActivity_test activity);
    void inject(IssueWorkActivity activity);
    void inject(ReceiveWorkActivity activity);
    void inject(FinishFabricResultSetActivity activity);
    void inject(KnittingQcResultEntryActivity activity);

    void inject(SewingDialog dialog);

}
