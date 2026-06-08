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

package com.logicsoftbd.lsl.ui.process.scanprocess;

import android.util.Log;

import com.androidnetworking.error.ANError;
import com.logicsoftbd.lsl.data.model.Process;
import com.logicsoftbd.lsl.data.network.model.BarcodeIssueResponse;
import com.logicsoftbd.lsl.data.network.model.BarcodeResponse;
import com.logicsoftbd.lsl.data.network.model.CuttingQcBarcodeResponse;
import com.logicsoftbd.lsl.data.network.model.EmbSpBarcodeResponse;
import com.logicsoftbd.lsl.data.network.model.FinishFabricIssueSet;
import com.logicsoftbd.lsl.data.network.model.FinishFabricResponse;
import com.logicsoftbd.lsl.data.network.model.FinishFabricResultSet;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRollReceive;
import com.logicsoftbd.lsl.data.network.model.GmtsBarcodeResponse;
import com.logicsoftbd.lsl.data.network.model.KnittingResponse;
import com.logicsoftbd.lsl.data.network.model.SewingResponse;
import com.logicsoftbd.lsl.ui.base.BasePresenter;
import com.logicsoftbd.lsl.utils.rx.SchedulerProvider;
import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;


public class ScannerPresenter<V extends ScannerMvpView,
        I extends ScannerMvpInteractor> extends BasePresenter<V, I>
        implements ScannerMvpPresenter<V, I> {

    @Inject
    public ScannerPresenter(I mvpInteractor,
                            SchedulerProvider schedulerProvider,
                            CompositeDisposable compositeDisposable) {
        super(mvpInteractor, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onNextClick(Process.DataParam dataParam) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
        .getBundleApiCall(dataParam)
        .subscribeOn(getSchedulerProvider().io())
        .observeOn(getSchedulerProvider().ui())
        .subscribe(new Consumer<BarcodeResponse>() {
            @Override
            public void accept(BarcodeResponse bundleResponse) throws Exception {
                if (bundleResponse != null) {
                    if( bundleResponse.getData().getStatus() != null && bundleResponse.getData().getStatus().equalsIgnoreCase("Failed")) {
                        getMvpView().bundleErrorResponse(bundleResponse.getData().getMsg());
                    } else {
                        getMvpView().bundleResponse(bundleResponse);
                    }
                }
                getMvpView().hideLoading();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (!isViewAttached()) {
                    return;
                }
                getMvpView().hideLoading();
                getMvpView().issueErrorResponse("Delivery challan is not valid");

                // handle the error here
                if (throwable instanceof ANError) {
                    ANError anError = (ANError) throwable;
                    handleApiError(anError);
                }
            }
        }));
    }

    @Override
    public void onNextClickIssue(Process.DataParam dataParam) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getIssueApiCall(dataParam)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<BarcodeIssueResponse>() {
                    @Override
                    public void accept(BarcodeIssueResponse bundleResponse) throws Exception {
                        if (bundleResponse != null) {
                            if( bundleResponse.getData().getStatus() != null && bundleResponse.getData().getStatus().equalsIgnoreCase("Failed")) {
                                getMvpView().issueErrorResponse(bundleResponse.getData().getMsg());
                            } else {
                                getMvpView().issueResponse(bundleResponse);
                            }
                        }
                        getMvpView().hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();
                        getMvpView().issueErrorResponse("Barcode is not valid");

                        // handle the error here
                       /* if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }*/
                    }
                }));
    }

    @Override
    public void onNextClickGmts(Process.DataParam dataParam) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getGmtsBarcodeCall(dataParam)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<GmtsBarcodeResponse>() {
                    @Override
                    public void accept(GmtsBarcodeResponse bundleResponse) throws Exception {
                        if (bundleResponse != null) {
                           /* if( bundleResponse.getData().getStatus() != null && bundleResponse.getData().getStatus().equalsIgnoreCase("Failed")) {
                                getMvpView().issueErrorResponse(bundleResponse.getData().getMsg());
                            } else {
                                getMvpView().issueResponse(bundleResponse);
                            }*/

                          // getMvpView().sewingInputResponse(bundleResponse);
                        }
                        getMvpView().hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();
                        getMvpView().issueErrorResponse("Barcode is not valid");

                        // handle the error here
                       /* if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }*/
                    }
                }));
    }

    @Override
    public void onNextClickCuttingQc(Process.DataParam dataParam) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getCuttingQcCall(dataParam)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<CuttingQcBarcodeResponse>() {
                    @Override
                    public void accept(CuttingQcBarcodeResponse bundleResponse) throws Exception {
                        if (bundleResponse != null) {
                           if( bundleResponse.getData().getStatus() != null && bundleResponse.getData().getStatus().equalsIgnoreCase("Failed")) {
                                getMvpView().issueErrorResponse(bundleResponse.getData().getMsg());
                            } else {
                               getMvpView().cuttingQcResponse(bundleResponse);
                            }


                        }
                        getMvpView().hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        Log.e("ScannerPresenter", "===="+throwable);
                        getMvpView().hideLoading();
                        getMvpView().issueErrorResponse("Barcode is not valid");

                        // handle the error here
                       /* if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }*/
                    }
                }));
    }

    @Override
    public void onNextClickSewing(Process.DataParam dataParam) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getSewingBarcodeCall(dataParam)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<SewingResponse>() {
                    @Override
                    public void accept(SewingResponse bundleResponse) throws Exception {
                        if (bundleResponse != null) {
                             if( bundleResponse.getData().getStatus() != null && bundleResponse.getData().getStatus().equalsIgnoreCase("Failed")) {
                                getMvpView().issueErrorResponse(bundleResponse.getData().getMsg());
                            } else {
                                getMvpView().sewingInputResponse(bundleResponse);
                            }

                        }
                        getMvpView().hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        Log.e("ScannerPresenter", "===="+throwable);
                        getMvpView().hideLoading();
                        getMvpView().issueErrorResponse("Barcode is not valid");

                        // handle the error here
                       /* if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }*/
                    }
                }));
    }


    @Override
    public void onNextClickEmbSp(Process.DataParam dataParam) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getEmbSpBarcodeCall(dataParam)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<EmbSpBarcodeResponse>() {
                    @Override
                    public void accept(EmbSpBarcodeResponse bundleResponse) throws Exception {
                        if (bundleResponse != null) {
                            if( bundleResponse.getData().getStatus() != null && bundleResponse.getData().getStatus().equalsIgnoreCase("Failed")) {
                                getMvpView().issueErrorResponse(bundleResponse.getData().getMsg());
                            } else {
                                getMvpView().embSpResponse(bundleResponse);
                            }

                        }
                        getMvpView().hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        Log.e("ScannerPresenter", "===="+throwable);
                        getMvpView().hideLoading();
                        getMvpView().issueErrorResponse("Barcode is not valid");

                        // handle the error here
                       /* if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }*/
                    }
                }));
    }

    @Override
    public void onNextClickFinishFabric(Process.DataParam dataParam) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getFinishFabricCall(dataParam)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<FinishFabricResponse>() {
                    @Override
                    public void accept(FinishFabricResponse bundleResponse) throws Exception {
                        if (bundleResponse != null) {
                            if( bundleResponse.getData().getStatus() != null && bundleResponse.getData().getStatus().equalsIgnoreCase("Failed")) {
                                getMvpView().issueErrorResponse(bundleResponse.getData().getMsg());
                            } else {
                                getMvpView().finishFabricResponse(bundleResponse);
                            }

                        }
                        getMvpView().hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        Log.e("ScannerPresenter", "===="+throwable);
                        getMvpView().hideLoading();
                        getMvpView().issueErrorResponse("Barcode is not valid");

                        // handle the error here
                       /* if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }*/
                    }
                }));
    }

    @Override
    public void onNextClickFinishResult(Process.DataParam dataParam) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getFinishFabricResultSetCall(dataParam)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<FinishFabricResultSet>() {
                    @Override
                    public void accept(FinishFabricResultSet bundleResponse) throws Exception {
                        if (bundleResponse != null) {
                            if( bundleResponse.getStatus() != null && bundleResponse.getStatus().equalsIgnoreCase("Failed")) {
                                getMvpView().issueErrorResponse(bundleResponse.getMsg());
                            }
                            else if(bundleResponse.getStatus().equalsIgnoreCase("false")){
                                getMvpView().issueErrorResponse("Barcode Already Scanned");
                            }
                            else {
                                getMvpView().finishFabricResultSetResponse(bundleResponse);
                            }

                        }
                        getMvpView().hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        Log.e("ScannerPresenter", "===="+throwable);
                        getMvpView().hideLoading();
                        getMvpView().issueErrorResponse("Barcode is not valid");

                        // handle the error here
                       /* if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }*/
                    }
                }));
    }

    @Override
    public void onNextClickFinishIssue(Process.DataParam dataParam) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getFinishFabricIssueCall(dataParam)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<FinishFabricIssueSet>() {
                    @Override
                    public void accept(FinishFabricIssueSet bundleResponse) throws Exception {
                        if (bundleResponse != null) {
                            if( bundleResponse.data.getStatus()!=null) {
                                getMvpView().issueErrorResponse(bundleResponse.data.getMsg());
                            } else {
                                getMvpView().finishFabricIssueSetResponse(bundleResponse);
                            }

                        }
                        getMvpView().hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        Log.e("ScannerPresenter", "===="+throwable);
                        getMvpView().hideLoading();
                        getMvpView().issueErrorResponse("Barcode is not valid");

                        // handle the error here
                       /* if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }*/
                    }
                }));
    }

    @Override
    public void onNextClickStoreIssue(Process.DataParam dataParam) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getFinishFabricRollCall(dataParam)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<FinishFabricRollReceive>() {
                    @Override
                    public void accept(FinishFabricRollReceive bundleResponse) throws Exception {
                        if (bundleResponse != null) {
                            if( bundleResponse.getData().get(0).getStatus()!=null) {
                                getMvpView().issueErrorResponse(bundleResponse.getData().get(0).getMsg());
                            } else {
                                getMvpView().finishFabricRollReceiveResponse(bundleResponse);
                            }
                        }
                        getMvpView().hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        Log.e("ScannerPresenter", "===="+throwable);
                        getMvpView().hideLoading();
                        getMvpView().issueErrorResponse("Barcode is not valid");

                        // handle the error here
                       /* if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }*/
                    }
                }));
    }

    @Override
    public void onNextClickKnitting(Process.DataParam dataParam) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getKnittingCall(dataParam)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<KnittingResponse>() {
                    @Override
                    public void accept(KnittingResponse bundleResponse) throws Exception {
                        if (bundleResponse != null) {
                            if( bundleResponse.getData().getStatus()!=null && bundleResponse.getData().getStatus().equals("Failed") ) {
                                getMvpView().issueErrorResponse(bundleResponse.getData().getMsg());
                            } else {
                                getMvpView().knittingResponse(bundleResponse);
                            }
                        }

                        getMvpView().hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        Log.e("ScannerPresenter", "===="+throwable);
                        getMvpView().hideLoading();
                        getMvpView().issueErrorResponse("Barcode is not valid");

                        // handle the error here
                       /* if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }*/
                    }
                }));
    }
}
