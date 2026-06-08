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

import com.logicsoftbd.lsl.data.network.model.BarcodeIssueResponse;
import com.logicsoftbd.lsl.data.network.model.BarcodeResponse;
import com.logicsoftbd.lsl.data.network.model.CuttingQcBarcodeResponse;
import com.logicsoftbd.lsl.data.network.model.EmbSpBarcodeResponse;
import com.logicsoftbd.lsl.data.network.model.FinishFabricIssueSet;
import com.logicsoftbd.lsl.data.network.model.FinishFabricResponse;
import com.logicsoftbd.lsl.data.network.model.FinishFabricResultSet;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRollReceive;
import com.logicsoftbd.lsl.data.network.model.KnittingResponse;
import com.logicsoftbd.lsl.data.network.model.SewingResponse;
import com.logicsoftbd.lsl.ui.base.MvpView;


public interface ScannerMvpView extends MvpView {

    void bundleResponse(BarcodeResponse bundleResponse);
    void bundleErrorResponse(String msg);

    void issueResponse(BarcodeIssueResponse bundleResponse);
    void issueErrorResponse(String msg);

    void sewingInputResponse(SewingResponse barcodeResponse);

    void embSpResponse(EmbSpBarcodeResponse barcodeResponse);

    void cuttingQcResponse(CuttingQcBarcodeResponse barcodeResponse);
    void finishFabricResponse(FinishFabricResponse barcodeResponse);
    void finishFabricResultSetResponse(FinishFabricResultSet finishFabricResultSet);
    void finishFabricIssueSetResponse(FinishFabricIssueSet finishFabricIssueSet);
    void finishFabricRollReceiveResponse(FinishFabricRollReceive finishFabricRollReceive);
    void knittingResponse(KnittingResponse knittingResponse);
}
