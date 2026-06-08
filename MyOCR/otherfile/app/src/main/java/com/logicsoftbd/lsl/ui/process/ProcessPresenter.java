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

package com.logicsoftbd.lsl.ui.process;

import android.util.Log;

import com.androidnetworking.error.ANError;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.model.Process;
import com.logicsoftbd.lsl.data.network.model.MenuResponse;
import com.logicsoftbd.lsl.ui.base.BasePresenter;
import com.logicsoftbd.lsl.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by janisharali on 25/05/17.
 */

public class ProcessPresenter<V extends ProcessMvpView,
        I extends ProcessMvpInteractor> extends BasePresenter<V, I>
        implements ProcessMvpPresenter<V, I> {

    @Inject
    public ProcessPresenter(I mvpInteractor,
                            SchedulerProvider schedulerProvider,
                            CompositeDisposable compositeDisposable) {
        super(mvpInteractor, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onViewPrepared() {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getProcessData()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<List<Process>>() {
                    @Override
                    public void accept(@NonNull List<Process> processResponse)
                            throws Exception {
                        if (processResponse != null && processResponse.size() > 0) {
                            getMvpView().updateRepo(processResponse);
                        }
                        getMvpView().hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable)
                            throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void getMenu() {
        Log.e("get", "menu"+getInteractor().getUsername());
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getMenuList(getInteractor().getUsername(),getInteractor().getPassword())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<MenuResponse>() {
                    @Override
                    public void accept(@NonNull MenuResponse menu)
                            throws Exception {
                        if (menu != null && menu.getData().size() > 0) {
                            List<Process> processResponse= new ArrayList();
                            for(MenuResponse.Menu menu1: menu.getData()){
                                if(menu1.getMenu().equals("Grey Roll Receive")){
                                    processResponse.add(new Process(R.drawable.grey_roll_receive, "Grey Roll Receive", "Receive",
                                            new Process.DataParam("grey_roll", "receive")));
                                }
                                else if(menu1.getMenu().equals("Grey Roll Issue")){
                                    processResponse.add(new Process(R.drawable.grey_roll_issue, "Grey Roll Issue", "Issue",
                                            new Process.DataParam("grey_roll", "issue")));
                                }else if(menu1.getMenu().equals("Slitting/Squeezing")){
                                    processResponse.add(new Process(R.drawable.grey_roll_issue, "Slitting Squeezing", "code",
                                            new Process.DataParam("code", "fabric")));
                                }else if(menu1.getMenu().equals("Stentering")){
                                    processResponse.add(new Process(R.drawable.grey_roll_issue, "Stentering", "code",
                                            new Process.DataParam("code", "fabric")));
                                }else if(menu1.getMenu().equals("Compacting")){
                                    processResponse.add(new Process(R.drawable.grey_roll_issue, "Compacting", "code",
                                            new Process.DataParam("code", "fabric")));
                                }
                                else if(menu1.getMenu().equals("Cutting QC")){
                                    processResponse.add(new Process(R.drawable.process, "Cutting Qc", "Cutting Qc",
                                            new Process.DataParam("cutting_qc", "input")));
                                }
                                else if(menu1.getMenu().equals("Print Issue")){
                                    processResponse.add(new Process(R.drawable.process, "Print Issue","Print Issue",
                                            new Process.DataParam("print", "issue")));

                                }
                                else if(menu1.getMenu().equals("Print Receive")){

                                    processResponse.add(new Process(R.drawable.process, "Print Receive","Print Receive",
                                            new Process.DataParam("print", "receive")));
                                }
                                else if(menu1.getMenu().equals("Embroidery Issue")){
                                    processResponse.add(new Process(R.drawable.process, "Embroidery Issue","Embroidery Issue",
                                            new Process.DataParam("embroidery", "issue")));
                                }

                                else if(menu1.getMenu().equals("Embroidery Receive")){
                                    processResponse.add(new Process(R.drawable.process, "Embroidery Receive","Embroidery Receive",
                                            new Process.DataParam("embroidery", "receive")));

                                }
                                else if(menu1.getMenu().equals("Special Work Issue")){
                                    processResponse.add(new Process(R.drawable.process, "Special Work Issue","Special Work Issue",
                                            new Process.DataParam("special_work", "issue")));
                                }
                                else if(menu1.getMenu().equals("Special Work Receive")){

                                    processResponse.add(new Process(R.drawable.process, "Special Work Receive","Special Work Receive",
                                            new Process.DataParam("special_work", "receive")));
                                }
                                else if(menu1.getMenu().equals("Sewing Input")){
                                    processResponse.add(new Process(R.drawable.sewing, "Sewing Input", "Sewing",
                                            new Process.DataParam("sewing", "input")));
                                }

                                else if(menu1.getMenu().equals("Sewing Output")){
                                    processResponse.add(new Process(R.drawable.sewing, "Sewing Output", "Sewing",
                                            new Process.DataParam("sewing", "output")));
                                }

                                else if(menu1.getMenu().equals("Finish Fabric Production QC")){
                                    processResponse.add(new Process(R.drawable.process, "Finish Fabric Production QC", "Fabric",
                                            new Process.DataParam("finish", "fabric")));
                                }

                                else if(menu1.getMenu().equals("Finish Fabric Production Result Entry")){
                                    processResponse.add(new Process(R.drawable.process, "Finish Fabric Production Result Entry", "result",
                                            new Process.DataParam("result", "fabric")));
                                }

                                else if(menu1.getMenu().equals("Result Entry")){
                                    processResponse.add(new Process(R.drawable.process, "Finish Fabric Roll Issue Barcode Scan ", "result",
                                            new Process.DataParam("issue", "fabric")));
                                }

                                else if(menu1.getMenu().equals("Finish Fabric Roll Receive By Store")){
                                    processResponse.add(new Process(R.drawable.process, "Finish Fabric Roll Receive by Store", "result",
                                            new Process.DataParam("store", "fabric")));

                                }

                                else if(menu1.getMenu().equals("Knitting QC Result Entry")){
                                    processResponse.add(new Process(R.drawable.process, "Knitting QC Result Entry", "result",
                                            new Process.DataParam("store", "knitting")));
                                }
                                else if(menu1.getMenu().equals("Finish Fabric Production QC Print")){
                                    processResponse.add(new Process(R.drawable.process, "Finish Fabric Production QC Print", "code",
                                            new Process.DataParam("code", "fabric")));
                                }else if(menu1.getMenu().equals("Dyeing Production")){
                                    processResponse.add(new Process(R.drawable.process, "Dyeing Production", "code",
                                            new Process.DataParam("code", "fabric")));
                                }
                            }
                            getMvpView().updateRepo(processResponse);
                        }
                        getMvpView().hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable)
                            throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }
}
