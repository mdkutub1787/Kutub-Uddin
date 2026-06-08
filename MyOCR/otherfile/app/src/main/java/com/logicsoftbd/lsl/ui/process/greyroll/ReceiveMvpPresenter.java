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
import com.logicsoftbd.lsl.data.network.model.EmbSpRequest;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRequest;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRollReceiveRequest;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRollRequest;
import com.logicsoftbd.lsl.data.network.model.ResultEntryRequest;
import com.logicsoftbd.lsl.data.network.model.RollIssueRequest;
import com.logicsoftbd.lsl.data.network.model.RollReceiveRequest;
import com.logicsoftbd.lsl.data.network.model.SewingRequest;
import com.logicsoftbd.lsl.ui.base.MvpPresenter;

import java.util.List;


public interface ReceiveMvpPresenter<V extends ReceiveMvpView,
        I extends ReceiveMvpInteractor> extends MvpPresenter<V, I> {

    void onRollReceiveSave(RollReceiveRequest receiveRequest);
    void onRollIssueSave(RollIssueRequest rollIssueRequest);
    void onQcSave(CuttingQcBarcodeResponse receiveRequest);

    void getStoreResponse();

    void getPurposeResponse();

    RollReceiveRequest convertToRollReceive(BarcodeResponse barcodeResponse, List<BarcodeResponse.Challan.ProductBarcode> productBarcodeList);
    RollIssueRequest convertToRollIssue(BarcodeIssueResponse barcodeResponse, List<BarcodeIssueResponse.Challan.ProductBarcode> productBarcodeList);

    void getLocation(int companyId);
    void getFloorResponse(int location, int production_process);
    void getMachine();
    void getShift();
    void getDefectInch();
    void getKnitDefectInch();
    void getKnitDefectList();
    void getDefectList();
    void getFabricGradeList();
    void getLineResponse(int companyId, int locationId, int floorId);

    void onSewingIoSave(SewingRequest sewingRequest);
    void onFinishFabricIoSave(FinishFabricRequest finishFabricRequest);
    void onResultSetSave(ResultEntryRequest resultEntryRequest);
    void onKnittingResultSetSave(ResultEntryRequest resultEntryRequest);
    void onEmbSpIoSave(EmbSpRequest embSpRequest);
    void onEmbSpIoRcvSave(EmbSpRequest embSpRequest);

    void getReferenceResponse(int type);
    void onNextClickFinishFabricTwoTypes(String batchNo,String barCode);
    void onNextClickFinishFabricBatchNo(String batchNo);
    void onNextClickFinishFabricBarCode(String barCode);
    String getUserId();
    void getIssuePurposeList();
    void getIssueStoreList();
    void getRollReceiveStoreList(String id);
    void getLocationList();
    void getFabricShade();
    void onFinishFabricRollIssueSave(FinishFabricRollRequest finishFabricRequest);
    void onFinishFabricRollReceiveSave(FinishFabricRollReceiveRequest finishFabricRequest);
}
