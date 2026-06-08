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
import com.logicsoftbd.lsl.ui.base.MvpInteractor;

import io.reactivex.Observable;


public interface ReceiveMvpInteractor extends MvpInteractor {

    Observable<BarcodeResponse> doRollReceive(RollReceiveRequest receiveRequest);
    Observable<BarcodeIssueResponse> doRollIssue(RollIssueRequest rollIssueRequest);

    Observable<StoreResponse> getStoreList();

    Observable<PurposeResponse> getPurposeList();
    Observable<FloorResponse> getFloorList(int locationId, int production_process);
    Observable<FloorResponse> getLocationList(int companyId);
    Observable<MachineResponses> getMachineList();
    Observable<ShiftResponses> geShiftList();
    String getCurrentUserLoggedInMode();
    Observable<DefectListModel> getDefectList();
    Observable<DefectListModel> getKnitDefectList();
    Observable<IssuePurposeModel> getIssuePurposeList();
    Observable<IssueStoreModel> getIssueStoreList();
    Observable<DefectInchModel> getDefectInchList();
    Observable<DefectInchModel> getKnitDefectInchList();
    Observable<FabricGradeModel> getFabricGradeList();
    Observable<FabricShade> getFabricShadeList();
    Observable<FinishFabricQrCodeResponses> getFinishFabricTwoCall(String batchNo,String barCode);
    Observable<FinishFabricQrCodeResponses> getFinishFabricBatchNo(String batchNo);
    Observable<FinishFabricQrCodeResponses> getFinishFabricBarCode(String barCode);
    Observable<ReferenceDataResponse> getReferenceDataList(int type);

    Observable<LineResponse> getLineList(int companyId, int locationId, int floorId);

    Observable<SewingResponse> doSewingSave(SewingRequest sewingRequest);
    Observable<CuttingQcBarcodeResponse> doQcSave(CuttingQcBarcodeResponse CuttingQcBarcodeRequest);
    Observable<EmbSpBarcodeResponse> doEmbSpSave(EmbSpRequest embSpRequest);
    Observable<EmbSpBarcodeResponse> doEmbSpRcvSave(EmbSpRequest embSpRequest);
    Observable<FinishFabricSuccessReponses> doFinishFabricSave(FinishFabricRequest finishFabricRequest);
    Observable<FinishFabricRollIssueResponses> doFinishFabricRollIssueSave(FinishFabricRollRequest finishFabricRequest);
    Observable<IssueStoreModel> getRollReceiveIssueStoreList(String id);
    Observable<LocationModel> getLocationList();
    Observable<FinishFabricSuccessReponses> doResultEntrySave(ResultEntryRequest resultEntryRequest);
    Observable<FinishFabricSuccessReponses> doKnittingResultEntrySave(ResultEntryRequest resultEntryRequest);
    Observable<FinishFabricRollIssueResponses> doFinishFabricRollReceiveSave(FinishFabricRollReceiveRequest finishFabricRequest);

}
