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

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.logicsoftbd.lsl.data.model.Process;
import com.logicsoftbd.lsl.data.network.model.BarcodeIssueResponse;
import com.logicsoftbd.lsl.data.network.model.BarcodeResponse;
import com.logicsoftbd.lsl.data.network.model.BlogResponse;
import com.logicsoftbd.lsl.data.network.model.CuttingQcBarcodeResponse;
import com.logicsoftbd.lsl.data.network.model.DefectInchModel;
import com.logicsoftbd.lsl.data.network.model.DefectListModel;
import com.logicsoftbd.lsl.data.network.model.EmbSpBarcodeResponse;
import com.logicsoftbd.lsl.data.network.model.FinishFabricIssueSet;
import com.logicsoftbd.lsl.data.network.model.FinishFabricQrCodeResponses;
import com.logicsoftbd.lsl.data.network.model.FinishFabricResponse;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRollReceive;
import com.logicsoftbd.lsl.data.network.model.OpenSourceResponse;
import com.logicsoftbd.lsl.data.network.model.SewingResponse;
import com.logicsoftbd.lsl.di.ActivityContext;
import com.logicsoftbd.lsl.di.PerActivity;
import com.logicsoftbd.lsl.ui.about.AboutInteractor;
import com.logicsoftbd.lsl.ui.about.AboutMvpInteractor;
import com.logicsoftbd.lsl.ui.about.AboutMvpPresenter;
import com.logicsoftbd.lsl.ui.about.AboutMvpView;
import com.logicsoftbd.lsl.ui.about.AboutPresenter;
import com.logicsoftbd.lsl.ui.feed.FeedInteractor;
import com.logicsoftbd.lsl.ui.feed.FeedMvpInteractor;
import com.logicsoftbd.lsl.ui.feed.FeedMvpPresenter;
import com.logicsoftbd.lsl.ui.feed.FeedMvpView;
import com.logicsoftbd.lsl.ui.feed.FeedPagerAdapter;
import com.logicsoftbd.lsl.ui.feed.FeedPresenter;
import com.logicsoftbd.lsl.ui.feed.blogs.BlogAdapter;
import com.logicsoftbd.lsl.ui.feed.blogs.BlogInteractor;
import com.logicsoftbd.lsl.ui.feed.blogs.BlogMvpInteractor;
import com.logicsoftbd.lsl.ui.feed.blogs.BlogMvpPresenter;
import com.logicsoftbd.lsl.ui.feed.blogs.BlogMvpView;
import com.logicsoftbd.lsl.ui.feed.blogs.BlogPresenter;
import com.logicsoftbd.lsl.ui.feed.opensource.OpenSourceAdapter;
import com.logicsoftbd.lsl.ui.feed.opensource.OpenSourceInteractor;
import com.logicsoftbd.lsl.ui.feed.opensource.OpenSourceMvpInteractor;
import com.logicsoftbd.lsl.ui.feed.opensource.OpenSourceMvpPresenter;
import com.logicsoftbd.lsl.ui.feed.opensource.OpenSourceMvpView;
import com.logicsoftbd.lsl.ui.feed.opensource.OpenSourcePresenter;
import com.logicsoftbd.lsl.ui.login.LoginInteractor;
import com.logicsoftbd.lsl.ui.login.LoginMvpInteractor;
import com.logicsoftbd.lsl.ui.login.LoginMvpPresenter;
import com.logicsoftbd.lsl.ui.login.LoginMvpView;
import com.logicsoftbd.lsl.ui.login.LoginPresenter;
import com.logicsoftbd.lsl.ui.main.MainInteractor;
import com.logicsoftbd.lsl.ui.main.MainMvpInteractor;
import com.logicsoftbd.lsl.ui.main.MainMvpPresenter;
import com.logicsoftbd.lsl.ui.main.MainMvpView;
import com.logicsoftbd.lsl.ui.main.MainPagerAdapter;
import com.logicsoftbd.lsl.ui.main.MainPresenter;
import com.logicsoftbd.lsl.ui.main.rating.RatingDialogInteractor;
import com.logicsoftbd.lsl.ui.main.rating.RatingDialogMvpInteractor;
import com.logicsoftbd.lsl.ui.main.rating.RatingDialogMvpPresenter;
import com.logicsoftbd.lsl.ui.main.rating.RatingDialogMvpView;
import com.logicsoftbd.lsl.ui.main.rating.RatingDialogPresenter;
import com.logicsoftbd.lsl.ui.process.ProcessAdapter;
import com.logicsoftbd.lsl.ui.process.ProcessInteractor;
import com.logicsoftbd.lsl.ui.process.ProcessMvpInteractor;
import com.logicsoftbd.lsl.ui.process.ProcessMvpPresenter;
import com.logicsoftbd.lsl.ui.process.ProcessMvpView;
import com.logicsoftbd.lsl.ui.process.ProcessPresenter;
import com.logicsoftbd.lsl.ui.process.greyroll.ButtonListAdapter;
import com.logicsoftbd.lsl.ui.process.greyroll.CuttingQcAdapter;
import com.logicsoftbd.lsl.ui.process.greyroll.DefectListAdapter;
import com.logicsoftbd.lsl.ui.process.greyroll.FinishFabricAdapter;
import com.logicsoftbd.lsl.ui.process.greyroll.FinishFabricIssueAdapter;
import com.logicsoftbd.lsl.ui.process.greyroll.FinishFabricQrAdapter;
import com.logicsoftbd.lsl.ui.process.greyroll.FinishFabricRollReceiveAdapter;
import com.logicsoftbd.lsl.ui.process.greyroll.GmtsAdapter;
import com.logicsoftbd.lsl.ui.process.greyroll.IssueAdapter;
import com.logicsoftbd.lsl.ui.process.greyroll.ReceiveAdapter;
import com.logicsoftbd.lsl.ui.process.greyroll.ReceiveInteractor;
import com.logicsoftbd.lsl.ui.process.greyroll.ReceiveMvpInteractor;
import com.logicsoftbd.lsl.ui.process.greyroll.ReceiveMvpPresenter;
import com.logicsoftbd.lsl.ui.process.greyroll.ReceiveMvpView;
import com.logicsoftbd.lsl.ui.process.greyroll.ReceivePresenter;
import com.logicsoftbd.lsl.ui.process.greyroll.SewingAdapter;
import com.logicsoftbd.lsl.ui.process.scanprocess.ScannerInteractor;
import com.logicsoftbd.lsl.ui.process.scanprocess.ScannerMvpInteractor;
import com.logicsoftbd.lsl.ui.process.scanprocess.ScannerMvpPresenter;
import com.logicsoftbd.lsl.ui.process.scanprocess.ScannerMvpView;
import com.logicsoftbd.lsl.ui.process.scanprocess.ScannerPresenter;
import com.logicsoftbd.lsl.ui.splash.SplashInteractor;
import com.logicsoftbd.lsl.ui.splash.SplashMvpInteractor;
import com.logicsoftbd.lsl.ui.splash.SplashMvpPresenter;
import com.logicsoftbd.lsl.ui.splash.SplashMvpView;
import com.logicsoftbd.lsl.ui.splash.SplashPresenter;
import com.logicsoftbd.lsl.utils.rx.AppSchedulerProvider;
import com.logicsoftbd.lsl.utils.rx.SchedulerProvider;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by janisharali on 27/01/17.
 */

@Module
public class ActivityModule {

    private AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }

    @Provides
    @PerActivity
    SplashMvpPresenter<SplashMvpView, SplashMvpInteractor> provideSplashPresenter(
            SplashPresenter<SplashMvpView, SplashMvpInteractor> presenter) {
        return presenter;
    }

    @Provides
    AboutMvpPresenter<AboutMvpView, AboutMvpInteractor> provideAboutPresenter(
            AboutPresenter<AboutMvpView, AboutMvpInteractor> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    LoginMvpPresenter<LoginMvpView, LoginMvpInteractor> provideLoginPresenter(
            LoginPresenter<LoginMvpView, LoginMvpInteractor> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    MainMvpPresenter<MainMvpView, MainMvpInteractor> provideMainPresenter(
            MainPresenter<MainMvpView, MainMvpInteractor> presenter) {
        return presenter;
    }

    @Provides
    RatingDialogMvpPresenter<RatingDialogMvpView,
            RatingDialogMvpInteractor> provideRateUsPresenter(

            RatingDialogPresenter<RatingDialogMvpView, RatingDialogMvpInteractor> presenter) {
        return presenter;
    }

    @Provides
    FeedMvpPresenter<FeedMvpView, FeedMvpInteractor> provideFeedPresenter(
            FeedPresenter<FeedMvpView, FeedMvpInteractor> presenter) {
        return presenter;
    }

    @Provides
    OpenSourceMvpPresenter<OpenSourceMvpView,
            OpenSourceMvpInteractor> provideOpenSourcePresenter(

            OpenSourcePresenter<OpenSourceMvpView, OpenSourceMvpInteractor> presenter) {
        return presenter;
    }

    @Provides
    ProcessMvpPresenter<ProcessMvpView,
            ProcessMvpInteractor> provideProcessPresenter(

            ProcessPresenter<ProcessMvpView, ProcessMvpInteractor> presenter) {
        return presenter;
    }

    @Provides
    BlogMvpPresenter<BlogMvpView, BlogMvpInteractor> provideBlogMvpPresenter(
            BlogPresenter<BlogMvpView, BlogMvpInteractor> presenter) {
        return presenter;
    }

    @Provides
    ScannerMvpPresenter<ScannerMvpView,
                ScannerMvpInteractor> provideScannerPresenter(

            ScannerPresenter<ScannerMvpView, ScannerMvpInteractor> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    ReceiveMvpPresenter<ReceiveMvpView, ReceiveMvpInteractor> provideReceivePresenter(
            ReceivePresenter<ReceiveMvpView, ReceiveMvpInteractor> presenter) {
        return presenter;
    }

    @Provides
    FeedPagerAdapter provideFeedPagerAdapter(AppCompatActivity activity) {
        return new FeedPagerAdapter(activity.getSupportFragmentManager());
    }

    @Provides
    MainPagerAdapter provideMainPagerAdapter(AppCompatActivity activity) {
        return new MainPagerAdapter(activity.getSupportFragmentManager());
    }

    @Provides
    OpenSourceAdapter provideOpenSourceAdapter() {
        return new OpenSourceAdapter(new ArrayList<OpenSourceResponse.Repo>());
    }

    @Provides
    BlogAdapter provideBlogAdapter() {
        return new BlogAdapter(new ArrayList<BlogResponse.Blog>());
    }

    @Provides
    ProcessAdapter provideProcessAdapter() {
        return new ProcessAdapter(new ArrayList<Process>());
    }

    @Provides
    ReceiveAdapter provideReceiveAdapter() {
        return new ReceiveAdapter(new ArrayList<BarcodeResponse.Challan.ProductBarcode>());
    }

    @Provides
    IssueAdapter provideIssueAdapter() {
        return new IssueAdapter(new ArrayList<BarcodeIssueResponse.Challan.ProductBarcode>());
    }
    @Provides
    GmtsAdapter provideGmtsAdapter() {
        return new GmtsAdapter(new ArrayList<EmbSpBarcodeResponse.BodyPart.DetailsPart>());
    }
    @Provides
    CuttingQcAdapter provideCuttingQcAdapter() {
        return new CuttingQcAdapter(new ArrayList<CuttingQcBarcodeResponse.Result.DetailsPart.BundleData>());
    }

    @Provides
    SewingAdapter provideSewingAdapter() {
        return new SewingAdapter(new ArrayList<SewingResponse.Result.MasterPart>());
    }
    @Provides
    FinishFabricAdapter provideFinishFabricAdapter() {
        return new FinishFabricAdapter(new ArrayList<FinishFabricResponse.ResultSet>());
    }
    @Provides
    FinishFabricIssueAdapter provideFinishFabricIssueAdapter() {
        return new FinishFabricIssueAdapter(new ArrayList<FinishFabricIssueSet.DetailsSet>());
    }
    @Provides
    FinishFabricRollReceiveAdapter provideFinishFabricRollReceiveAdapter() {
        return new FinishFabricRollReceiveAdapter(new ArrayList<FinishFabricRollReceive.DetailsSet>());
    }

    @Provides
    DefectListAdapter provideDefectListAdapter() {
        return new DefectListAdapter(new ArrayList<DefectListModel.Result>(),mActivity);
    }
    @Provides
    ButtonListAdapter provideButtonListAdapter() {
        return new ButtonListAdapter(new ArrayList<DefectInchModel.Result>(),mActivity);
    }

    @Provides
    FinishFabricQrAdapter provideFinishFabricQrAdapter() {
        return new FinishFabricQrAdapter(new ArrayList<FinishFabricQrCodeResponses.ResultSet>());
    }
    @Provides
    LinearLayoutManager provideLinearLayoutManager(AppCompatActivity activity) {
        return new LinearLayoutManager(activity);
    }

    @Provides
    @PerActivity
    SplashMvpInteractor provideSplashMvpInteractor(SplashInteractor interactor) {
        return interactor;
    }

    @Provides
    @PerActivity
    AboutMvpInteractor provideAboutMvpInteractor(AboutInteractor interactor) {
        return interactor;
    }

    @Provides
    @PerActivity
    LoginMvpInteractor provideLoginMvpInteractor(LoginInteractor interactor) {
        return interactor;
    }

    @Provides
    @PerActivity
    MainMvpInteractor provideMainMvpInteractor(MainInteractor interactor) {
        return interactor;
    }

    @Provides
    @PerActivity
    RatingDialogMvpInteractor provideRatingDialogMvpInteractor(
            RatingDialogInteractor interactor) {
        return interactor;
    }

    @Provides
    @PerActivity
    FeedMvpInteractor provideFeedMvpInteractor(FeedInteractor interactor) {
        return interactor;
    }

    @Provides
    @PerActivity
    OpenSourceMvpInteractor provideOpenSourceMvpInteractor(
            OpenSourceInteractor interactor) {
        return interactor;
    }

    @Provides
    @PerActivity
    BlogMvpInteractor provideBlogMvpInteractor(BlogInteractor interactor) {
        return interactor;
    }

    @Provides
    @PerActivity
    ProcessMvpInteractor provideProcessMvpInteractor(
            ProcessInteractor interactor) {
        return interactor;
    }

    @Provides
    @PerActivity
    ScannerMvpInteractor provideScannerMvpInteractor(
            ScannerInteractor interactor) {
        return interactor;
    }

    @Provides
    @PerActivity
    ReceiveMvpInteractor provideReceiveMvpInteractor(
            ReceiveInteractor interactor) {
        return interactor;
    }
}
