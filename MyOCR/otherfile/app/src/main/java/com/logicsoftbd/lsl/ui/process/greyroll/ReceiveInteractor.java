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

import com.logicsoftbd.lsl.data.network.ApiHelper;
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
import com.logicsoftbd.lsl.data.prefs.PreferencesHelper;
import com.logicsoftbd.lsl.ui.base.BaseInteractor;

import javax.inject.Inject;

import io.reactivex.Observable;


public class ReceiveInteractor extends BaseInteractor
        implements ReceiveMvpInteractor {

    @Inject
    public ReceiveInteractor(PreferencesHelper preferencesHelper,
                             ApiHelper apiHelper) {

        super(preferencesHelper, apiHelper);
    }

    @Override
    public Observable<BarcodeResponse> doRollReceive(RollReceiveRequest receiveRequest) {
        return getApiHelper().doGreyRollReceive(receiveRequest);
    }

    @Override
    public Observable<BarcodeIssueResponse> doRollIssue(RollIssueRequest rollIssueRequest) {
        return getApiHelper().doGreyRollIssue(rollIssueRequest);
    }

    @Override
    public Observable<StoreResponse> getStoreList() {
        return getApiHelper().getStoreList();
    }

    @Override
    public Observable<PurposeResponse> getPurposeList() {
        return getApiHelper().getPurposeList();
    }

    @Override
    public Observable<FloorResponse> getLocationList(int companyId) {
        return getApiHelper().getLocationList(companyId);
    }

    @Override
    public Observable<MachineResponses> getMachineList() {
        return getApiHelper().getMachineList();
    }

    @Override
    public Observable<ShiftResponses> geShiftList() {
        return getApiHelper().getShiftList();
    }

    @Override
    public String getCurrentUserLoggedInMode() {
        return String.valueOf(getPreferencesHelper().getCurrentUserId());
    }

    @Override
    public Observable<DefectListModel> getDefectList() {
        return getApiHelper().getDefectList();
    }

    @Override
    public Observable<DefectListModel> getKnitDefectList() {
        return getApiHelper().getKnitDefectList();
    }

    @Override
    public Observable<IssuePurposeModel> getIssuePurposeList() {
        return getApiHelper().getIssuePurposeList();
    }

    @Override
    public Observable<IssueStoreModel> getIssueStoreList() {
        return getApiHelper().getIssueStoreList();
    }

    @Override
    public Observable<DefectInchModel> getDefectInchList() {
        return getApiHelper().getDefectInchList();
    }

    @Override
    public Observable<DefectInchModel> getKnitDefectInchList() {
        return getApiHelper().getKnitDefectInchList();
    }

    @Override
    public Observable<FabricGradeModel> getFabricGradeList() {
        return getApiHelper().getFabricGradeList();
    }

    @Override
    public Observable<FabricShade> getFabricShadeList() {
        return getApiHelper().getFabricShade();
    }

    @Override
    public Observable<FinishFabricQrCodeResponses> getFinishFabricTwoCall(String batchNo,String barCode) {
        return getApiHelper().getFinishFabricTwoCall(batchNo,barCode);
    }

    @Override
    public Observable<FinishFabricQrCodeResponses> getFinishFabricBatchNo(String batchNo) {
        return getApiHelper().getFinishFabricBatchNo(batchNo);
    }

    @Override
    public Observable<FinishFabricQrCodeResponses> getFinishFabricBarCode(String barCode) {
        return getApiHelper().getFinishFabricBarCodes(barCode);
    }

    @Override
    public Observable<FloorResponse> getFloorList(int locationId, int production_process) {
        return getApiHelper().getFloorList(locationId, production_process);
    }

    @Override
    public Observable<LineResponse> getLineList(int companyId, int locationId, int floorId) {
        return getApiHelper().getLineList(companyId,locationId,floorId);
    }

    @Override
    public Observable<SewingResponse> doSewingSave(SewingRequest sewingRequest) {
        return getApiHelper().doSewingIOSave(sewingRequest);
    }

    @Override
    public Observable<EmbSpBarcodeResponse> doEmbSpSave(EmbSpRequest embSpRequest) {
        return getApiHelper().doEmbSpIOSave(embSpRequest);
    }

    @Override
    public Observable<EmbSpBarcodeResponse> doEmbSpRcvSave(EmbSpRequest embSpRequest) {
        return getApiHelper().doEmbSpIOReceiveSave(embSpRequest);
    }

    @Override
    public Observable<FinishFabricSuccessReponses> doFinishFabricSave(FinishFabricRequest finishFabricRequest) {
        return getApiHelper().doFinishFabricSave(finishFabricRequest);
    }

    @Override
    public Observable<FinishFabricRollIssueResponses> doFinishFabricRollIssueSave(FinishFabricRollRequest finishFabricRequest) {
        return getApiHelper().doFinishFabricRollIssueSave(finishFabricRequest);
    }

    @Override
    public Observable<IssueStoreModel> getRollReceiveIssueStoreList(String id) {
        return getApiHelper().getRollReceiveIssueStoreList(id);
    }

    @Override
    public Observable<LocationModel> getLocationList() {
        return getApiHelper().getLocationList();
    }

    @Override
    public Observable<FinishFabricSuccessReponses> doResultEntrySave(ResultEntryRequest resultEntryRequest) {
        return getApiHelper().doResultEntrySave(resultEntryRequest);
    }

    @Override
    public Observable<FinishFabricSuccessReponses> doKnittingResultEntrySave(ResultEntryRequest resultEntryRequest) {
        return getApiHelper().doKnittingResultEntrySave(resultEntryRequest);
    }

    @Override
    public Observable<FinishFabricRollIssueResponses> doFinishFabricRollReceiveSave(FinishFabricRollReceiveRequest finishFabricRequest) {
        return getApiHelper().doFinishFabricRollReceiveSave(finishFabricRequest);
    }

    @Override
    public Observable<ReferenceDataResponse> getReferenceDataList(int type) {
        return getApiHelper().getReferenceList(type);
    }

    @Override
    public Observable<CuttingQcBarcodeResponse> doQcSave(CuttingQcBarcodeResponse CuttingQcBarcodeRequest) {
        return getApiHelper().doQcIOSave(CuttingQcBarcodeRequest);
    }
}
