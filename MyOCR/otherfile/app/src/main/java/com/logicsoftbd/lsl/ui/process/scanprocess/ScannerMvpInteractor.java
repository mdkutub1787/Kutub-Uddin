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
import com.logicsoftbd.lsl.ui.base.MvpInteractor;

import io.reactivex.Observable;



public interface ScannerMvpInteractor extends MvpInteractor {

    Observable<BarcodeResponse> getBundleApiCall(Process.DataParam dataParam);
    Observable<BarcodeIssueResponse> getIssueApiCall(Process.DataParam dataParam);
    Observable<GmtsBarcodeResponse> getGmtsBarcodeCall(Process.DataParam dataParam);
    Observable<SewingResponse> getSewingBarcodeCall(Process.DataParam dataParam);
    Observable<EmbSpBarcodeResponse> getEmbSpBarcodeCall(Process.DataParam dataParam);
    Observable<CuttingQcBarcodeResponse> getCuttingQcCall(Process.DataParam dataParam);
    Observable<FinishFabricResponse> getFinishFabricCall(Process.DataParam dataParam);
    Observable<FinishFabricResultSet> getFinishFabricResultSetCall(Process.DataParam dataParam);
    Observable<FinishFabricIssueSet> getFinishFabricIssueCall(Process.DataParam dataParam);
    Observable<FinishFabricRollReceive> getFinishFabricRollCall(Process.DataParam dataParam);
    Observable<KnittingResponse> getKnittingCall(Process.DataParam dataParam);
}
