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

import static com.logicsoftbd.lsl.serviceInterface.RetrofitApiClient.getUnsafeOkHttpClient;

import android.util.Log;

import com.androidnetworking.common.Priority;
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
import com.logicsoftbd.lsl.data.network.model.RollIssueRequest;
import com.rx2androidnetworking.Rx2AndroidNetworking;
import com.logicsoftbd.lsl.data.network.model.PurposeResponse;
import com.logicsoftbd.lsl.data.network.model.ReferenceDataResponse;
import com.logicsoftbd.lsl.data.network.model.RejectResponse;
import com.logicsoftbd.lsl.data.network.model.ResultEntryRequest;
import com.logicsoftbd.lsl.data.network.model.RollReceiveRequest;
import com.logicsoftbd.lsl.data.network.model.SewingRequest;
import com.logicsoftbd.lsl.data.network.model.SewingResponse;
import com.logicsoftbd.lsl.data.network.model.ShiftResponses;
import com.logicsoftbd.lsl.data.network.model.StoreResponse;
import com.logicsoftbd.lsl.utils.DateUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;

/**
 * Created by janisharali on 28/01/17.
 */

@Singleton
public class AppApiHelper implements ApiHelper {

    private ApiHeader mApiHeader;

    @Inject
    public AppApiHelper(ApiHeader apiHeader) {
        mApiHeader = apiHeader;
    }

    @Override
    public ApiHeader getApiHeader() {
        return mApiHeader;
    }

    @Override
    public Observable<LoginResponse> doGoogleLoginApiCall(LoginRequest.GoogleLoginRequest
                                                                  request) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_GOOGLE_LOGIN)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addBodyParameter(request)
                .build()
                .getObjectObservable(LoginResponse.class);
    }

    @Override
    public Observable<LoginResponse> doFacebookLoginApiCall(LoginRequest.FacebookLoginRequest
                                                                    request) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_FACEBOOK_LOGIN)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addBodyParameter(request)
                .build()
                .getObjectObservable(LoginResponse.class);
    }

    @Override
    public Observable<LoginResponse> doServerLoginApiCall(LoginRequest.ServerLoginRequest
                                                                  request) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_SERVER_LOGIN)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .addApplicationJsonBody(request)
                .build()
                .getObjectObservable(LoginResponse.class);
    }

    @Override
    public Observable<LogoutResponse> doLogoutApiCall() {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_LOGOUT)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .addHeaders(mApiHeader.getProtectedApiHeader())
                .build()
                .getObjectObservable(LogoutResponse.class);
    }

    @Override
    public Observable<BlogResponse> getBlogApiCall() {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_BLOG)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .addHeaders(mApiHeader.getProtectedApiHeader())
                .build()
                .getObjectObservable(BlogResponse.class);
    }

    @Override
    public Observable<OpenSourceResponse> getOpenSourceApiCall() {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_OPEN_SOURCE)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .addHeaders(mApiHeader.getProtectedApiHeader())
                .build()
                .getObjectObservable(OpenSourceResponse.class);
    }

    @Override
    public Observable<BundleResponse> getBundleApiCall(Process.DataParam dataParam) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_BUNDLE)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
               // .addHeaders(mApiHeader.getProtectedApiHeader())
                .addApplicationJsonBody(dataParam)
                .build()
                .getObjectObservable(BundleResponse.class);
    }

    @Override
    public Observable<DefectResponse> getDefectApiCall(String type) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_DDL)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .addQueryParameter("ddl_type", type)
                .build()
                .getObjectObservable(DefectResponse.class);
    }

    @Override
    public Observable<MenuResponse> doLoginApiCall(LoginRequest.ServerLoginRequest request) {

        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_MENU+"/user_id/"+request.getUsername()+"/pwd/"+request.getPassword())
                .setOkHttpClient(getUnsafeOkHttpClient().build())
               // .addApplicationJsonBody(request)
                .build()
                .getObjectObservable(MenuResponse.class);

    }

    @Override
    public Observable<BarcodeResponse> getGreyProductByBarcode(String barcode) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_BARCODE+"/"+barcode)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(BarcodeResponse.class);
    }

    @Override
    public Observable<BarcodeResponse> doGreyRollReceive(RollReceiveRequest receiveRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_ROLL_RECEIVE)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .addApplicationJsonBody(receiveRequest)
                .build()
                .getObjectObservable(BarcodeResponse.class);
    }

    @Override
    public Observable<BarcodeIssueResponse> doGreyRollIssue(RollIssueRequest rollIssueRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_ROLL_ISSUE)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .addApplicationJsonBody(rollIssueRequest)
                .build()
                .getObjectObservable(BarcodeIssueResponse.class);
    }

    @Override
    public Observable<StoreResponse> getStoreList() {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_STORES)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(StoreResponse.class);
    }

    @Override
    public Observable<FinishFabricRollReceive> getFinishFabricRollCall(String challan) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_CHALLAN_SCAN_ISSUE+challan)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(FinishFabricRollReceive.class);
    }

    @Override
    public Observable<KnittingResponse> getKnittingCall(String barcode) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_KNITTING+barcode)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(KnittingResponse.class);
    }

    @Override
    public Observable<BarcodeIssueResponse> getGreyProductIssueByBarcode(String barcode) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_BARCODE_ISSUE+barcode)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(BarcodeIssueResponse.class);
    }

    @Override
    public Observable<PurposeResponse> getPurposeList() {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_PURPOSES)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(PurposeResponse.class);
    }

    @Override
    public Observable<FloorResponse> getLocationList(int companyId) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_LOCATION+companyId)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(FloorResponse.class);
    }

    @Override
    public Observable<MachineResponses> getMachineList() {
        return  Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_MACHINE)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(MachineResponses.class);
    }

    @Override
    public Observable<ShiftResponses> getShiftList() {
        return  Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_SHIFT)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(ShiftResponses.class);
    }

    @Override
    public Observable<FloorResponse> getFloorList(int locationId, int production_process) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_FLOOR+locationId+"/production_process/"+production_process)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(FloorResponse.class);
    }

    @Override
    public Observable<LineResponse> getLineList(int companyId, int locationId, int floorId) {
        Date mSelectedDate = DateUtils.getToday();
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_LINE+"company_id/"+companyId+"/location_id/"+locationId+"/floor_id/"+floorId+"/input_date/"+DateUtils.formatDate(mSelectedDate))
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(LineResponse.class);
    }

    @Override
    public Observable<GmtsBarcodeResponse> getGmtsBarcode(String barcode, int type) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GMTS_BARCODE+barcode+"/type/"+type)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(GmtsBarcodeResponse.class);
    }

    @Override
    public Observable<FinishFabricQrCodeResponses> getFinishFabricTwoCall(String batchNo,String barCode) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_FINISH_FABRIC_QR_TWO+batchNo+"&barcode_no="+barCode)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(FinishFabricQrCodeResponses.class);
    }

    @Override
    public Observable<RejectResponse> getRejectList() {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_REJECT)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(RejectResponse.class);
    }

    @Override
    public Observable<RejectResponse> getSpotList() {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_SPOT_DEFECT)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(RejectResponse.class);
    }

    @Override
    public Observable<RejectResponse> getAlterList() {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_ALTER_DEFECT)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(RejectResponse.class);
    }

    @Override
    public Observable<CuttingQcBarcodeResponse> getCuttingQcBarcode(String barcode) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_CUTTINGQC_BARCODE+barcode)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(CuttingQcBarcodeResponse.class);
    }

    @Override
    public Observable<FinishFabricResponse> getFinishFabricBarcode(String barcode) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_FINISH_FABRIC_QC+barcode)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(FinishFabricResponse.class);
    }

    @Override
    public Observable<FinishFabricResultSet> getFinishFabricResultSetBarcode(String barcode) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_FINISH_FABRIC_QC_RESULT_SET+barcode)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(FinishFabricResultSet.class);
    }

    @Override
    public Observable<FinishFabricIssueSet> getFinishFabricIssueSetBarcode(String barcode) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_FINISH_FABRIC_QC_ISSUE_SET+barcode)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(FinishFabricIssueSet.class);
    }

    @Override
    public Observable<FinishFabricQrCodeResponses> getFinishFabricBatchNo(String batchNo) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                . writeTimeout(120, TimeUnit.SECONDS)
                .build();
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_FINISH_FABRIC_QR_TWO+batchNo)
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .build()
                .getObjectObservable(FinishFabricQrCodeResponses.class);
    }

    @Override
    public Observable<FinishFabricQrCodeResponses> getFinishFabricBarCodes(String barCode) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_FINISH_FABRIC_QR+barCode)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(FinishFabricQrCodeResponses.class);
    }

    @Override
    public Observable<SewingResponse> getSewingBarcode(String barcode, int type) {
        Log.d("TAG", "getSewingBarcode: "+ApiEndPoint.ENDPOINT_SWEING_BARCODE+barcode+"/type/"+type);
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_SWEING_BARCODE+barcode+"/type/"+type)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(SewingResponse.class);
    }

    @Override
    public Observable<IssuePurposeModel> getIssuePurposeList() {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_ISSUE_PURPOSE_LIST)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(IssuePurposeModel.class);
    }

    @Override
    public Observable<IssueStoreModel> getIssueStoreList() {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_ISSUE_STORE_LIST)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(IssueStoreModel.class);
    }

    @Override
    public Observable<IssueStoreModel> getRollReceiveIssueStoreList(String id) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_LOCATION_STORE+id)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(IssueStoreModel.class);
    }

    @Override
    public Observable<LocationModel> getLocationList() {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_LOCATION_LIST)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(LocationModel.class);
    }

    @Override
    public Observable<SewingResponse> doSewingIOSave(SewingRequest sewingRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_SWEING_IO_SAVE)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .addApplicationJsonBody(sewingRequest)
                .build()
                .getObjectObservable(SewingResponse.class);
    }

    @Override
    public Observable<FinishFabricSuccessReponses> doResultEntrySave(ResultEntryRequest resultEntryRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_RESULT_ENTRY_IO_SAVE)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .addApplicationJsonBody(resultEntryRequest)
                .build()
                .getObjectObservable(FinishFabricSuccessReponses.class);
    }

    @Override
    public Observable<FinishFabricSuccessReponses> doKnittingResultEntrySave(ResultEntryRequest resultEntryRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_KNITTING_RESULT_ENTRY_IO_SAVE)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .addApplicationJsonBody(resultEntryRequest)
                .build()
                .getObjectObservable(FinishFabricSuccessReponses.class);
    }

    @Override
    public Observable<EmbSpBarcodeResponse> getEmbSpBarcode(String barcode, int type, int action) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_EMB_SP_BARCODE+barcode+"/type/"+type+"/action/"+action)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(EmbSpBarcodeResponse.class);
    }

    @Override
    public Observable<FinishFabricRollIssueResponses> doFinishFabricRollReceiveSave(FinishFabricRollReceiveRequest finishFabricRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_FINISH_FABRIC_ROLL_RECEIVE)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .addApplicationJsonBody(finishFabricRequest)
                .build()
                .getObjectObservable(FinishFabricRollIssueResponses.class);
    }

    @Override
    public Observable<EmbSpBarcodeResponse> doEmbSpIOSave(EmbSpRequest sewingRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_EMV_SP_SAVE)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .addApplicationJsonBody(sewingRequest)
                .build()
                .getObjectObservable(EmbSpBarcodeResponse.class);
    }

    @Override
    public Observable<MenuResponse> getMenuList(String userName,String password) {

        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_MENU+"/user_id/"+userName+"/pwd/"+password)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                // .addApplicationJsonBody(request)
                .build()
                .getObjectObservable(MenuResponse.class);
    }

    @Override
    public Observable<EmbSpBarcodeResponse> doEmbSpIOReceiveSave(EmbSpRequest sewingRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_EMV_SP_RECEIVE_SAVE)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .addApplicationJsonBody(sewingRequest)
                .build()
                .getObjectObservable(EmbSpBarcodeResponse.class);
    }

    @Override
    public Observable<FinishFabricSuccessReponses> doFinishFabricSave(FinishFabricRequest finishFabricRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_FINISH_FABRIC_SAVE)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .addApplicationJsonBody(finishFabricRequest)
                .build()
                .getObjectObservable(FinishFabricSuccessReponses.class);
    }

    @Override
    public Observable<ReferenceDataResponse> getReferenceList(int type) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_REFERENCE_DATA+type)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(ReferenceDataResponse.class);
    }

    @Override
    public Observable<DefectListModel> getDefectList() {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_DEFECT_LIST)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(DefectListModel.class);
    }

    @Override
    public Observable<DefectListModel> getKnitDefectList() {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_KNIT_DEFECT_LIST)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(DefectListModel.class);
    }

    @Override
    public Observable<DefectInchModel> getDefectInchList() {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_DEFECT_INCH)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(DefectInchModel.class);
    }

    @Override
    public Observable<DefectInchModel> getKnitDefectInchList() {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_DEFECT_INCH)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(DefectInchModel.class);
    }

    @Override
    public Observable<FabricGradeModel> getFabricGradeList() {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_FABRIC_GRADE)
                .build()
                .getObjectObservable(FabricGradeModel.class);
    }

    @Override
    public Observable<FabricShade> getFabricShade() {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_FABRIC_SHADE)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(FabricShade.class);
    }

    @Override
    public Observable<CuttingQcBarcodeResponse> doQcIOSave(CuttingQcBarcodeResponse cuttingQcBarcodeRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_QC_SAVE)
                .addApplicationJsonBody(cuttingQcBarcodeRequest)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .build()
                .getObjectObservable(CuttingQcBarcodeResponse.class);
    }

    @Override
    public Observable<FinishFabricRollIssueResponses> doFinishFabricRollIssueSave(FinishFabricRollRequest finishFabricRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_ROLL_ISSUE_SAVE)
                .setOkHttpClient(getUnsafeOkHttpClient().build())
                .addApplicationJsonBody(finishFabricRequest)
                .build()
                .getObjectObservable(FinishFabricRollIssueResponses.class);
    }
}

