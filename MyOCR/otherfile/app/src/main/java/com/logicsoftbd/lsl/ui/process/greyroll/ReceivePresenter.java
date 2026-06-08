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

package com.logicsoftbd.lsl.ui.process.greyroll;

import android.util.Log;

import com.androidnetworking.error.ANError;
import com.logicsoftbd.lsl.data.network.model.BarcodeIssueResponse;
import com.logicsoftbd.lsl.data.network.model.BarcodeResponse;
import com.logicsoftbd.lsl.data.network.model.CuttingQcBarcodeResponse;
import com.logicsoftbd.lsl.data.network.model.DefectInchModel;
import com.logicsoftbd.lsl.data.network.model.DefectListModel;
import com.logicsoftbd.lsl.data.network.model.EmbSpBarcodeResponse;
import com.logicsoftbd.lsl.data.network.model.EmbSpRequest;
import com.logicsoftbd.lsl.data.network.model.FabricGradeModel;
import com.logicsoftbd.lsl.data.network.model.FabricShade;
import com.logicsoftbd.lsl.data.network.model.FinishFabricQrCodeResponses;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRequest;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRollIssueResponses;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRollReceiveRequest;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRollRequest;
import com.logicsoftbd.lsl.data.network.model.FinishFabricSuccessReponses;
import com.logicsoftbd.lsl.data.network.model.FloorResponse;
import com.logicsoftbd.lsl.data.network.model.IssuePurposeModel;
import com.logicsoftbd.lsl.data.network.model.IssueStoreModel;
import com.logicsoftbd.lsl.data.network.model.LineResponse;
import com.logicsoftbd.lsl.data.network.model.LocationModel;
import com.logicsoftbd.lsl.data.network.model.MachineResponses;
import com.logicsoftbd.lsl.data.network.model.PurposeResponse;
import com.logicsoftbd.lsl.data.network.model.ReferenceDataResponse;
import com.logicsoftbd.lsl.data.network.model.ResultEntryRequest;
import com.logicsoftbd.lsl.data.network.model.RollIssueRequest;
import com.logicsoftbd.lsl.data.network.model.RollReceiveRequest;
import com.logicsoftbd.lsl.data.network.model.SewingRequest;
import com.logicsoftbd.lsl.data.network.model.SewingResponse;
import com.logicsoftbd.lsl.data.network.model.ShiftResponses;
import com.logicsoftbd.lsl.data.network.model.StoreResponse;
import com.logicsoftbd.lsl.ui.base.BasePresenter;
import com.logicsoftbd.lsl.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;


public class ReceivePresenter<V extends ReceiveMvpView,
        I extends ReceiveMvpInteractor> extends BasePresenter<V, I>
        implements ReceiveMvpPresenter<V, I> {

    @Inject
    public ReceivePresenter(I mvpInteractor,
                            SchedulerProvider schedulerProvider,
                            CompositeDisposable compositeDisposable) {
        super(mvpInteractor, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onRollIssueSave(RollIssueRequest rollIssueRequest) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .doRollIssue(rollIssueRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<BarcodeIssueResponse>() {
                    @Override
                    public void accept(BarcodeIssueResponse bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            if(bundleResponse.getData().getStatus() != null && bundleResponse.getData().getStatus().equalsIgnoreCase("Success")) {
                                getMvpView().onSuccess(bundleResponse.getData().getMsg());
                            } else {
                                getMvpView().showMessage(bundleResponse.getData().getMsg());
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void onRollReceiveSave(RollReceiveRequest receiveRequest) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .doRollReceive(receiveRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<BarcodeResponse>() {
                    @Override
                    public void accept(BarcodeResponse bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            if(bundleResponse.getData().getStatus() != null && bundleResponse.getData().getStatus().equalsIgnoreCase("Success")) {
                                getMvpView().onSuccess(bundleResponse.getData().getMsg());
                            } else {
                                getMvpView().showMessage(bundleResponse.getData().getMsg());
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void onQcSave(CuttingQcBarcodeResponse receiveRequest) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .doQcSave(receiveRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<CuttingQcBarcodeResponse>() {
                    @Override
                    public void accept(CuttingQcBarcodeResponse bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            if(bundleResponse.getData().getStatus() != null && bundleResponse.getData().getStatus().equalsIgnoreCase("Success")) {
                                getMvpView().onSuccess(bundleResponse.getData().getMsg());
                            } else {
                                getMvpView().showMessage(bundleResponse.getData().getMsg());
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void getStoreResponse() {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getStoreList()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<StoreResponse>() {
                    @Override
                    public void accept(StoreResponse bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                           getMvpView().onStoreListResponse(bundleResponse);
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));

    }

    @Override
    public void getPurposeResponse() {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getPurposeList()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<PurposeResponse>() {
                    @Override
                    public void accept(PurposeResponse bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            getMvpView().onPurposeListResponse(bundleResponse);
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));

    }

    @Override
    public RollReceiveRequest convertToRollReceive(BarcodeResponse barcodeResponse, List<BarcodeResponse.Challan.ProductBarcode> productBarcodeList) {
       RollReceiveRequest rollReceiveRequest = new RollReceiveRequest();

       rollReceiveRequest.setStatus(barcodeResponse.getStatus());
       rollReceiveRequest.setData(convertToChallan(barcodeResponse.getData(), productBarcodeList));

        return rollReceiveRequest;
    }

    @Override
    public RollIssueRequest convertToRollIssue(BarcodeIssueResponse barcodeResponse, List<BarcodeIssueResponse.Challan.ProductBarcode> productBarcodeList) {
        RollIssueRequest rollIssueRequest = new RollIssueRequest();

        rollIssueRequest.setStatus(barcodeResponse.getStatus());
        rollIssueRequest.setData(convertToIssueChallan(barcodeResponse.getData(), productBarcodeList));

        return rollIssueRequest;
    }

    @Override
    public void getLocation(int companyId) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getLocationList(companyId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<FloorResponse>() {
                    @Override
                    public void accept(FloorResponse bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            getMvpView().onLocationListResponse(bundleResponse);
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void getFloorResponse(int location, int production_process) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getFloorList(location, production_process)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<FloorResponse>() {
                    @Override
                    public void accept(FloorResponse bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            getMvpView().onFloorListResponse(bundleResponse);
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void getMachine() {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getMachineList()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<MachineResponses>() {
                    @Override
                    public void accept(MachineResponses bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            getMvpView().onMachineResponses(bundleResponse);
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void getShift() {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .geShiftList()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<ShiftResponses>() {
                    @Override
                    public void accept(ShiftResponses bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            getMvpView().onShiftResponses(bundleResponse);
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void getDefectInch() {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getDefectInchList()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<DefectInchModel>() {
                    @Override
                    public void accept(DefectInchModel bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            getMvpView().defectInch(bundleResponse);
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void getKnitDefectInch() {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getKnitDefectInchList()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<DefectInchModel>() {
                    @Override
                    public void accept(DefectInchModel bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            getMvpView().defectInch(bundleResponse);
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void getKnitDefectList() {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getKnitDefectList()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<DefectListModel>() {
                    @Override
                    public void accept(DefectListModel bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            getMvpView().defectList(bundleResponse);
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void getDefectList() {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getDefectList()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<DefectListModel>() {
                    @Override
                    public void accept(DefectListModel bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            getMvpView().defectList(bundleResponse);
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void getFabricGradeList() {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getFabricGradeList()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<FabricGradeModel>() {
                    @Override
                    public void accept(FabricGradeModel bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            getMvpView().fabricGrade(bundleResponse);
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void getLineResponse(int companyId, int locationId, int floorId) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getLineList(companyId, locationId, floorId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<LineResponse>() {
                    @Override
                    public void accept(LineResponse bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            getMvpView().onLineListResponse(bundleResponse);
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void onSewingIoSave(SewingRequest sewingRequest) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .doSewingSave(sewingRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<SewingResponse>() {
                    @Override
                    public void accept(SewingResponse bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            if(bundleResponse.getData().getStatus() != null && bundleResponse.getData().getStatus().equalsIgnoreCase("Success")) {
                                getMvpView().onSuccess(bundleResponse.getData().getMsg());
                            } else {
                                getMvpView().showMessage(bundleResponse.getData().getMsg());
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void onFinishFabricIoSave(FinishFabricRequest finishFabricRequest) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .doFinishFabricSave(finishFabricRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<FinishFabricSuccessReponses>() {
                    @Override
                    public void accept(FinishFabricSuccessReponses bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            if(bundleResponse.getData().getStatus() != null && bundleResponse.getData().getStatus().equalsIgnoreCase("Success")) {
                                getMvpView().onSuccess(bundleResponse.getData().getMsg());
                            } else {
                                getMvpView().showMessage(bundleResponse.getData().getMsg());
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void onResultSetSave(ResultEntryRequest resultEntryRequest) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .doResultEntrySave(resultEntryRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<FinishFabricSuccessReponses>() {
                    @Override
                    public void accept(FinishFabricSuccessReponses bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            if(bundleResponse.getData().getStatus() != null && bundleResponse.getData().getStatus().equalsIgnoreCase("Success")) {
                                getMvpView().onSuccess(bundleResponse.getData().getMsg());
                            } else {
                                getMvpView().showMessage(bundleResponse.getData().getMsg());
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void onKnittingResultSetSave(ResultEntryRequest resultEntryRequest) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .doKnittingResultEntrySave(resultEntryRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<FinishFabricSuccessReponses>() {
                    @Override
                    public void accept(FinishFabricSuccessReponses bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            if(bundleResponse.getStatus() != null && bundleResponse.getStatus().equalsIgnoreCase("false")) {

                                getMvpView().showMessage(bundleResponse.getData().getMsg());
                            } else {
                                getMvpView().onSuccess(bundleResponse.getData().getMsg());
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void onEmbSpIoSave(EmbSpRequest embSpRequest) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .doEmbSpSave(embSpRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<EmbSpBarcodeResponse>() {
                    @Override
                    public void accept(EmbSpBarcodeResponse bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            if(bundleResponse.getData().getStatus() != null && bundleResponse.getData().getStatus().equalsIgnoreCase("Success")) {
                                getMvpView().onSuccess(bundleResponse.getData().getMsg());
                            } else {
                                getMvpView().showMessage(bundleResponse.getData().getMsg());
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void onEmbSpIoRcvSave(EmbSpRequest embSpRequest) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .doEmbSpRcvSave(embSpRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<EmbSpBarcodeResponse>() {
                    @Override
                    public void accept(EmbSpBarcodeResponse bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            if(bundleResponse.getData().getStatus() != null && bundleResponse.getData().getStatus().equalsIgnoreCase("Success")) {
                                getMvpView().onSuccess(bundleResponse.getData().getMsg());
                            } else {
                                getMvpView().showMessage(bundleResponse.getData().getMsg());
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void getReferenceResponse(final int type) {
        final int mTYpe = type;
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getReferenceDataList(type)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<ReferenceDataResponse>() {
                    @Override
                    public void accept(ReferenceDataResponse response) throws Exception {

                        if (response != null) {
                            getMvpView().onReferenceListResponse(mTYpe,response);
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void onNextClickFinishFabricTwoTypes(String batchNo,String barCode) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getFinishFabricTwoCall(batchNo,barCode)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<FinishFabricQrCodeResponses>() {
                    @Override
                    public void accept(FinishFabricQrCodeResponses bundleResponse) throws Exception {
                        if (bundleResponse != null) {
                            if( bundleResponse.getStatus() != null && bundleResponse.getStatus().equalsIgnoreCase("Failed")) {
                                getMvpView().onFailed(bundleResponse.getMsg());
                            } else {
                                getMvpView().finishFabricQrCodeTwoResponse(bundleResponse);
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
                        getMvpView().onFailed("No Data Found");

                        // handle the error here
                       /* if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }*/
                    }
                }));
    }

    @Override
    public void onNextClickFinishFabricBatchNo(String batchNo) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getFinishFabricBatchNo(batchNo)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<FinishFabricQrCodeResponses>() {
                    @Override
                    public void accept(FinishFabricQrCodeResponses bundleResponse) throws Exception {
                        if (bundleResponse != null) {
                            if( bundleResponse.getStatus() != null && bundleResponse.getStatus().equalsIgnoreCase("Failed")) {
                                getMvpView().onFailed(bundleResponse.getMsg());
                            } else {
                                getMvpView().finishFabricQrCodeTwoResponse(bundleResponse);
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
                        getMvpView().onFailed("No Data Found");

                        // handle the error here
                       /* if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }*/
                    }
                }));
    }

    @Override
    public void onNextClickFinishFabricBarCode(String barCode) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getFinishFabricBarCode(barCode)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<FinishFabricQrCodeResponses>() {
                    @Override
                    public void accept(FinishFabricQrCodeResponses bundleResponse) throws Exception {
                        if (bundleResponse != null) {
                            if( bundleResponse.getStatus() != null && bundleResponse.getStatus().equalsIgnoreCase("Failed")) {
                                getMvpView().onFailed(bundleResponse.getMsg());
                            } else {
                                getMvpView().finishFabricQrCodeTwoResponse(bundleResponse);
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
                        getMvpView().onFailed("No Data Found");

                        // handle the error here
                       /* if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }*/
                    }
                }));
    }

    @Override
    public String getUserId() {
        Log.e("getUserId", "===="+getInteractor().getCurrentUserLoggedInMode());
        getMvpView().onLogged(getInteractor().getCurrentUserLoggedInMode());
        return getInteractor().getCurrentUserLoggedInMode();
    }



    @Override
    public void getIssuePurposeList() {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getIssuePurposeList()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<IssuePurposeModel>() {
                    @Override
                    public void accept(IssuePurposeModel bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            getMvpView().issuePurpose(bundleResponse);
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void getIssueStoreList() {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getIssueStoreList()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<IssueStoreModel>() {
                    @Override
                    public void accept(IssueStoreModel bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            getMvpView().issueStore(bundleResponse);
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void getRollReceiveStoreList(String id) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getRollReceiveIssueStoreList(id)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<IssueStoreModel>() {
                    @Override
                    public void accept(IssueStoreModel bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            getMvpView().issueStore(bundleResponse);
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void getLocationList() {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getLocationList()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<LocationModel>() {
                    @Override
                    public void accept(LocationModel bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            getMvpView().onLocation(bundleResponse);
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void getFabricShade() {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .getFabricShadeList()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<FabricShade>() {
                    @Override
                    public void accept(FabricShade bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            getMvpView().onFabricShade(bundleResponse);
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void onFinishFabricRollIssueSave(FinishFabricRollRequest finishFabricRequest) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .doFinishFabricRollIssueSave(finishFabricRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<FinishFabricRollIssueResponses>() {
                    @Override
                    public void accept(FinishFabricRollIssueResponses bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            if(bundleResponse.getData().getStatus() != null && bundleResponse.getData().getStatus().equalsIgnoreCase("Success")) {
                                getMvpView().onSuccess(bundleResponse.getData().getMsg());
                            } else {
                                getMvpView().showMessage(bundleResponse.getData().getMsg());
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void onFinishFabricRollReceiveSave(FinishFabricRollReceiveRequest finishFabricRequest) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getInteractor()
                .doFinishFabricRollReceiveSave(finishFabricRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<FinishFabricRollIssueResponses>() {
                    @Override
                    public void accept(FinishFabricRollIssueResponses bundleResponse) throws Exception {

                        if (bundleResponse != null) {
                            if(bundleResponse.getData().getStatus() != null && bundleResponse.getData().getStatus().equalsIgnoreCase("Success")) {
                                getMvpView().onSuccess(bundleResponse.getData().getMsg());
                            } else {
                                getMvpView().showMessage(bundleResponse.getData().getMsg());
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

                        // handle the error here
                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    private RollIssueRequest.Challan convertToIssueChallan(BarcodeIssueResponse.Challan challan, List<BarcodeIssueResponse.Challan.ProductBarcode> productBarcodeList) {
        RollIssueRequest.Challan requestChallan = new RollIssueRequest.Challan();
        requestChallan.setMasterPart(convertToIssueMasterPart(challan.getMasterPart()));
        requestChallan.setProductBarcodes(convertIssueList(productBarcodeList));
        return requestChallan;
    }

    private RollReceiveRequest.Challan convertToChallan(BarcodeResponse.Challan challan, List<BarcodeResponse.Challan.ProductBarcode> productBarcodeList) {
        RollReceiveRequest.Challan requestChallan = new RollReceiveRequest.Challan();
        requestChallan.setMasterPart(convertTiMasterPart(challan.getMasterPart()));
        requestChallan.setProductBarcodes(convertList(productBarcodeList));
        return requestChallan;
    }

    private RollIssueRequest.Challan.MasterPart convertToIssueMasterPart(BarcodeIssueResponse.Challan.MasterPart obj) {
        RollIssueRequest.Challan.MasterPart masterPart = new RollIssueRequest.Challan.MasterPart();
        masterPart.setCompanyId(obj.getCompanyId());
        masterPart.setDeliveryDate(obj.getDeliveryDate());
        masterPart.setDeliveryId(obj.getDeliveryId());
        masterPart.setKnittingCompanyId(obj.getKnittingCompanyId());
        masterPart.setKnittingSource(obj.getKnittingSource());
        masterPart.setKnittingSourceId(obj.getKnittingSourceId());
        masterPart.setSysNumber(obj.getSysNumber());
        masterPart.setSysNumberPrefixNum(obj.getSysNumberPrefixNum());
        return  masterPart;
    }
    private RollReceiveRequest.Challan.MasterPart convertTiMasterPart(BarcodeResponse.Challan.MasterPart obj) {
        RollReceiveRequest.Challan.MasterPart masterPart = new RollReceiveRequest.Challan.MasterPart();
        masterPart.setCompanyId(obj.getCompanyId());
        masterPart.setDeliveryDate(obj.getDeliveryDate());
        masterPart.setDeliveryId(obj.getDeliveryId());
        masterPart.setKnittingCompanyId(obj.getKnittingCompanyId());
        masterPart.setKnittingSource(obj.getKnittingSource());
        masterPart.setKnittingSourceId(obj.getKnittingSourceId());
        masterPart.setSysNumber(obj.getSysNumber());
        masterPart.setSysNumberPrefixNum(obj.getSysNumberPrefixNum());
        masterPart.setLocationId(obj.getLocationId());
        return  masterPart;
    }

    private   List<RollReceiveRequest.Challan.ProductBarcode> convertList(List<BarcodeResponse.Challan.ProductBarcode> list) {
        List<RollReceiveRequest.Challan.ProductBarcode> convertedList = new ArrayList<>();

        for (BarcodeResponse.Challan.ProductBarcode object : list) {
            convertedList.add(convertToDetailsPart(object));
        }
        return convertedList;
    }

    private   List<RollIssueRequest.Challan.ProductBarcode> convertIssueList(List<BarcodeIssueResponse.Challan.ProductBarcode> list) {
        List<RollIssueRequest.Challan.ProductBarcode> convertedList = new ArrayList<>();

        for (BarcodeIssueResponse.Challan.ProductBarcode object : list) {
            convertedList.add(convertToIssueDetailsPart(object));
        }
        return convertedList;
    }

    private RollReceiveRequest.Challan.ProductBarcode convertToDetailsPart(BarcodeResponse.Challan.ProductBarcode barcode) {
        RollReceiveRequest.Challan.ProductBarcode productBarcode = new RollReceiveRequest.Challan.ProductBarcode();
        productBarcode.setBarcodeNo(barcode.getBarcodeNo());
        productBarcode.setBuyerId(barcode.getBuyerId());
        productBarcode.setBuyerName(barcode.getBuyerName());
        productBarcode.setProductionBasis(barcode.getProductionBasis());
        productBarcode.setProductionBasisName(barcode.getProductionBasisName());
        productBarcode.setBookingNo(barcode.getBookingNo());
        productBarcode.setKnittingCompany(barcode.getKnittingCompany());
        productBarcode.setBookingWithoutOrder(barcode.getBookingWithoutOrder());
        productBarcode.setBookingId(barcode.getBookingId());
        productBarcode.setBodyPartId(barcode.getBodyPartId());
        productBarcode.setBodyPartName(barcode.getBodyPartName());
        productBarcode.setYarnLot(barcode.getYarnLot());
        productBarcode.setBrandId(barcode.getBrandId());
        productBarcode.setShiftName(barcode.getShiftName());
        productBarcode.setFloorId(barcode.getFloorId());
        productBarcode.setMachineNoId(barcode.getMachineNoId());
        productBarcode.setYarnCount(barcode.getYarnCount());
        productBarcode.setColorId(String.valueOf(barcode.getColorId()));
        productBarcode.setColorName(barcode.getColorName());
        productBarcode.setColorRangeId(barcode.getColorRangeId());
        productBarcode.setColorRangeName(barcode.getColorRangeName());
        productBarcode.setRollId(barcode.getRollId());
        productBarcode.setUom(barcode.getUom());
        productBarcode.setDtlsId(barcode.getDtlsId());
        productBarcode.setProdId(barcode.getProdId());
        productBarcode.setDeterId(barcode.getDeterId());
        productBarcode.setConstruction(barcode.getConstruction());
        productBarcode.setComposition(barcode.getComposition());
        productBarcode.setGsm(barcode.getGsm());
        productBarcode.setWidth(barcode.getWidth());
        productBarcode.setStitchLength(barcode.getStitchLength());
        productBarcode.setRollNo(barcode.getRollNo());
        productBarcode.setPoBreakDownId(barcode.getPoBreakDownId());
        productBarcode.setPoNumber(barcode.getPoNumber());
        productBarcode.setQnty(barcode.getQnty());
        productBarcode.setRejectQnty(barcode.getRejectQnty());

        return productBarcode;
    }

    private RollIssueRequest.Challan.ProductBarcode convertToIssueDetailsPart(BarcodeIssueResponse.Challan.ProductBarcode barcode) {
        RollIssueRequest.Challan.ProductBarcode productBarcode = new RollIssueRequest.Challan.ProductBarcode();
        productBarcode.setBarcodeNo(barcode.getBarcodeNo());
        productBarcode.setBuyerId(String.valueOf(barcode.getBuyerId()));
        productBarcode.setBuyerName(barcode.getBuyerName());
        productBarcode.setProductionBasis(String.valueOf(barcode.getProductionBasis()));
        productBarcode.setProductionBasisName(barcode.getProductionBasisName());
        productBarcode.setBookingNo(String.valueOf(barcode.getBookingNo()));
        productBarcode.setKnittingCompany(String.valueOf(barcode.getKnittingCompany()));
        productBarcode.setBookingWithoutOrder(String.valueOf(barcode.getBookingWithoutOrder()));
        productBarcode.setBookingId(String.valueOf(barcode.getBookingId()));
        productBarcode.setSampBooking(String.valueOf(barcode.getSampBooking()));
        productBarcode.setBodyPartId(String.valueOf(barcode.getBodyPartId()));
        productBarcode.setBodyPartName(barcode.getBodyPartName());
        productBarcode.setYarnLot(barcode.getYarnLot());
        productBarcode.setBrandId(String.valueOf(barcode.getBrandId()));
        productBarcode.setShiftName(String.valueOf(barcode.getShiftName()));
        productBarcode.setStoreId(String.valueOf(barcode.getStoreId()));
        productBarcode.setFloorId(String.valueOf(barcode.getFloorId()));
        productBarcode.setRoomId(String.valueOf(barcode.getRoomId()));
        productBarcode.setRackId(String.valueOf(barcode.getRackId()));
        productBarcode.setShelfId(String.valueOf(barcode.getShelfId()));
        productBarcode.setBinBoxId(String.valueOf(barcode.getBinBoxId()));
        productBarcode.setMachineNoId(String.valueOf(barcode.getMachineNoId()));
        productBarcode.setYarnCount(String.valueOf(barcode.getYarnCount()));
        productBarcode.setColorId(barcode.getColorId());
        productBarcode.setColorName(barcode.getColorName());
        productBarcode.setColorRangeId(String.valueOf(barcode.getColorRangeId()));
        productBarcode.setColorRangeName(barcode.getColorRangeName());
        productBarcode.setRollId(String.valueOf(barcode.getRollId()));
        productBarcode.setUom(String.valueOf(barcode.getUom()));
        productBarcode.setDtlsId(String.valueOf(barcode.getDtlsId()));
        productBarcode.setProdId(String.valueOf(barcode.getProdId()));
        productBarcode.setDeterId(String.valueOf(barcode.getDeterId()));
        productBarcode.setConstruction(barcode.getConstruction());
        productBarcode.setComposition(barcode.getComposition());
        productBarcode.setGsm(String.valueOf(barcode.getGsm()));
        productBarcode.setWidth(String.valueOf(barcode.getWidth()));
        productBarcode.setStitchLength(barcode.getStitchLength());
        productBarcode.setRollNo(String.valueOf(barcode.getRollNo()));
        productBarcode.setPoBreakDownId(String.valueOf(barcode.getPoBreakDownId()));
        productBarcode.setPoNumber(barcode.getPoNumber());
        productBarcode.setQnty(String.valueOf(barcode.getQnty()));
        productBarcode.setRejectQnty(String.valueOf(barcode.getRejectQnty()));
        productBarcode.setUserId(String.valueOf(barcode.getUserId()));
        productBarcode.setYarnRate(String.valueOf(barcode.getYarnRate()));
        productBarcode.setKnitingCharge(String.valueOf(barcode.getKnitingCharge()));
        productBarcode.setRollRate(String.valueOf(barcode.getRollRate()));
        productBarcode.setIsSales(String.valueOf(barcode.getIsSales()));

        return productBarcode;
    }

}
