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

package com.logicsoftbd.lsl.ui.login;

import com.androidnetworking.error.ANError;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.model.LoginRequest;
import com.logicsoftbd.lsl.data.network.model.LoginResponse;
import com.logicsoftbd.lsl.data.network.model.MenuResponse;
import com.logicsoftbd.lsl.ui.base.BasePresenter;
import com.logicsoftbd.lsl.utils.AppConstants;
import com.logicsoftbd.lsl.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by janisharali on 27/01/17.
 */

public class LoginPresenter<V extends LoginMvpView, I extends LoginMvpInteractor>
        extends BasePresenter<V, I> implements LoginMvpPresenter<V, I> {

    private static final String TAG = "LoginPresenter";

    @Inject
    public LoginPresenter(I mvpInteractor,
                          SchedulerProvider schedulerProvider,
                          CompositeDisposable compositeDisposable) {
        super(mvpInteractor, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onServerLoginClick(String email, String password) {
        //validate email and password
        if (email == null || email.isEmpty()) {
            getMvpView().onError(R.string.empty_user);
            return;
        }
        /*if (!CommonUtils.isEmailValid(email)) {
            getMvpView().onError(R.string.invalid_email);
            return;
        }*/
        if (password == null || password.isEmpty()) {
            getMvpView().onError(R.string.empty_password);
            return;
        }
        getMvpView().showLoading();

        getCompositeDisposable().add(getInteractor()
                .doServerLoginApiCall(new LoginRequest.ServerLoginRequest(email, password))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<MenuResponse>() {
                    @Override
                    public void accept(MenuResponse response) throws Exception {
                        getMvpView().hideLoading();
                        if(response.isStatus() == true && response.getData() != null && response.getData().size() > 0) {
                            MenuResponse.Menu menu = response.getData().get(0);
                            List<String> menuName=new ArrayList<>();
                            for (MenuResponse.Menu menus:response.getData()){
                                menuName.add(menus.getMenu());
                            }
                            getInteractor().updateUserInfo(
                                    password,
                                    (long) menu.getUserId(),
                                    AppConstants.LoggedInMode.LOGGED_IN_MODE_SERVER,
                                    menu.getUserLoginId(),
                                    menu.getFullName(),
                                    "");
                            getMvpView().openMainActivity();
                        }
                       // getMvpView().showMessage(response.getMessage());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        // handle the login error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void onGoogleLoginClick() {
        // instruct LoginActivity to initiate google login
        getMvpView().showLoading();

        getCompositeDisposable().add(getInteractor()
                .doGoogleLoginApiCall(new LoginRequest.GoogleLoginRequest("test1", "test1"))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<LoginResponse>() {
                    @Override
                    public void accept(LoginResponse response) throws Exception {


                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().openMainActivity();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        // handle the login error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void onFacebookLoginClick() {
        // instruct LoginActivity to initiate facebook login
        getMvpView().showLoading();

        getCompositeDisposable().add(getInteractor()
                .doFacebookLoginApiCall(new LoginRequest.FacebookLoginRequest("test3", "test4"))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<LoginResponse>() {
                    @Override
                    public void accept(LoginResponse response) throws Exception {


                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().openMainActivity();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        // handle the login error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }
}
