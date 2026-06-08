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

package com.logicsoftbd.lsl.data.network;

import com.logicsoftbd.lsl.data.model.Process;
import com.logicsoftbd.lsl.data.network.model.BarcodeIssueResponse;
import com.logicsoftbd.lsl.data.network.model.BarcodeResponse;
import com.logicsoftbd.lsl.data.network.model.BlogResponse;
import com.logicsoftbd.lsl.data.network.model.BundleResponse;
import com.logicsoftbd.lsl.data.network.model.CuttingQcBarcodeResponse;
import com.logicsoftbd.lsl.data.network.model.DefectInchModel;
import com.logicsoftbd.lsl.data.network.model.DefectListModel;
import com.logicsoftbd.lsl.data.network.model.DefectResponse;
import com.logicsoftbd.lsl.data.network.model.EmbSpBarcodeResponse;
import com.logicsoftbd.lsl.data.network.model.EmbSpRequest;
import com.logicsoftbd.lsl.data.network.model.FabricGradeModel;
import com.logicsoftbd.lsl.data.network.model.FabricShade;
import com.logicsoftbd.lsl.data.network.model.FinishFabricIssueSet;
import com.logicsoftbd.lsl.data.network.model.FinishFabricQrCodeResponses;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRequest;
import com.logicsoftbd.lsl.data.network.model.FinishFabricResponse;
import com.logicsoftbd.lsl.data.network.model.FinishFabricResultSet;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRollIssueResponses;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRollReceive;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRollReceiveRequest;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRollRequest;
import com.logicsoftbd.lsl.data.network.model.FinishFabricSuccessReponses;
import com.logicsoftbd.lsl.data.network.model.FloorResponse;
import com.logicsoftbd.lsl.data.network.model.GmtsBarcodeResponse;
import com.logicsoftbd.lsl.data.network.model.IssuePurposeModel;
import com.logicsoftbd.lsl.data.network.model.IssueStoreModel;
import com.logicsoftbd.lsl.data.network.model.KnittingResponse;
import com.logicsoftbd.lsl.data.network.model.LineResponse;
import com.logicsoftbd.lsl.data.network.model.LocationModel;
import com.logicsoftbd.lsl.data.network.model.LoginRequest;
import com.logicsoftbd.lsl.data.network.model.LoginResponse;
import com.logicsoftbd.lsl.data.network.model.LogoutResponse;
import com.logicsoftbd.lsl.data.network.model.MachineResponses;
import com.logicsoftbd.lsl.data.network.model.MenuResponse;
import com.logicsoftbd.lsl.data.network.model.OpenSourceResponse;
import com.logicsoftbd.lsl.data.network.model.PurposeResponse;
import com.logicsoftbd.lsl.data.network.model.ReferenceDataResponse;
import com.logicsoftbd.lsl.data.network.model.RejectResponse;
import com.logicsoftbd.lsl.data.network.model.ResultEntryRequest;
import com.logicsoftbd.lsl.data.network.model.RollIssueRequest;
import com.logicsoftbd.lsl.data.network.model.RollReceiveRequest;
import com.logicsoftbd.lsl.data.network.model.SewingRequest;
import com.logicsoftbd.lsl.data.network.model.SewingResponse;
import com.logicsoftbd.lsl.data.network.model.ShiftResponses;
import com.logicsoftbd.lsl.data.network.model.StoreResponse;

import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Created by janisharali on 27/01/17.
 */

@Singleton
public interface ApiHelper {

    ApiHeader getApiHeader();

    Observable<LoginResponse> doGoogleLoginApiCall(LoginRequest.GoogleLoginRequest request);

    Observable<LoginResponse> doFacebookLoginApiCall(LoginRequest.FacebookLoginRequest request);

    Observable<LoginResponse> doServerLoginApiCall(LoginRequest.ServerLoginRequest request);

    Observable<LogoutResponse> doLogoutApiCall();

    Observable<BlogResponse> getBlogApiCall();

    Observable<OpenSourceResponse> getOpenSourceApiCall();

    Observable<BundleResponse> getBundleApiCall(Process.DataParam dataParam);

    Observable<DefectResponse> getDefectApiCall(String type);

    Observable<MenuResponse> doLoginApiCall(LoginRequest.ServerLoginRequest request);

    Observable<BarcodeResponse> getGreyProductByBarcode(String barcode);

    Observable<BarcodeResponse> doGreyRollReceive(RollReceiveRequest receiveRequest);

    Observable<BarcodeIssueResponse> doGreyRollIssue(RollIssueRequest rollIssueRequest);

    Observable<StoreResponse> getStoreList();
    Observable<FinishFabricRollReceive> getFinishFabricRollCall(String challan);
    Observable<KnittingResponse> getKnittingCall(String barcode);
    Observable<BarcodeIssueResponse> getGreyProductIssueByBarcode(String barcode);

    Observable<PurposeResponse> getPurposeList();
    Observable<RejectResponse> getRejectList();
    Observable<RejectResponse> getSpotList();
    Observable<RejectResponse> getAlterList();
    Observable<FloorResponse> getFloorList(int locationId, int production_process);
    Observable<FloorResponse> getLocationList(int companyId);
    Observable<MachineResponses> getMachineList();
    Observable<ShiftResponses> getShiftList();
    Observable<LineResponse> getLineList(int companyId, int locationId, int floorId);
    Observable<GmtsBarcodeResponse> getGmtsBarcode(String barcode, int type);
    Observable<FinishFabricQrCodeResponses> getFinishFabricTwoCall(String batchNo,String barCode);
    Observable<CuttingQcBarcodeResponse> getCuttingQcBarcode(String barcode);
    Observable<FinishFabricResponse> getFinishFabricBarcode(String barcode);
    Observable<FinishFabricResultSet> getFinishFabricResultSetBarcode(String barcode);
    Observable<FinishFabricIssueSet> getFinishFabricIssueSetBarcode(String barcode);
    Observable<FinishFabricQrCodeResponses> getFinishFabricBatchNo(String batchNo);
    Observable<FinishFabricQrCodeResponses> getFinishFabricBarCodes(String barCode);
    Observable<SewingResponse> getSewingBarcode(String barcode, int type);
    Observable<IssuePurposeModel> getIssuePurposeList();
    Observable<IssueStoreModel> getIssueStoreList();
    Observable<IssueStoreModel> getRollReceiveIssueStoreList(String id);
    Observable<LocationModel> getLocationList();
    Observable<SewingResponse> doSewingIOSave(SewingRequest sewingRequest);
    Observable<FinishFabricSuccessReponses> doResultEntrySave(ResultEntryRequest resultEntryRequest);
    Observable<FinishFabricSuccessReponses> doKnittingResultEntrySave(ResultEntryRequest resultEntryRequest);
    Observable<EmbSpBarcodeResponse> getEmbSpBarcode(String barcode, int type, int aaction);
    Observable<FinishFabricRollIssueResponses> doFinishFabricRollReceiveSave(FinishFabricRollReceiveRequest finishFabricRequest);
    Observable<EmbSpBarcodeResponse> doEmbSpIOSave(EmbSpRequest sewingRequest);
    Observable<MenuResponse> getMenuList(String userName,String password);
    Observable<EmbSpBarcodeResponse> doEmbSpIOReceiveSave(EmbSpRequest sewingRequest);
    Observable<FinishFabricSuccessReponses> doFinishFabricSave(FinishFabricRequest finishFabricRequest);

    Observable<CuttingQcBarcodeResponse> doQcIOSave(CuttingQcBarcodeResponse cuttingQcBarcodeRequest);
    Observable<FinishFabricRollIssueResponses> doFinishFabricRollIssueSave(FinishFabricRollRequest finishFabricRequest);

    Observable<ReferenceDataResponse> getReferenceList(int type);
    Observable<DefectListModel> getDefectList();
    Observable<DefectListModel> getKnitDefectList();
    Observable<DefectInchModel> getDefectInchList();
    Observable<DefectInchModel> getKnitDefectInchList();
    Observable<FabricGradeModel> getFabricGradeList();
    Observable<FabricShade> getFabricShade();
}
