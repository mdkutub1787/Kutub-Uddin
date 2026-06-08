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
import com.logicsoftbd.lsl.data.network.ApiHelper;
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
import com.logicsoftbd.lsl.data.prefs.PreferencesHelper;
import com.logicsoftbd.lsl.ui.base.BaseInteractor;

import javax.inject.Inject;

import io.reactivex.Observable;


public class ScannerInteractor extends BaseInteractor
        implements ScannerMvpInteractor {

    @Inject
    public ScannerInteractor(PreferencesHelper preferencesHelper,
                             ApiHelper apiHelper) {

        super(preferencesHelper, apiHelper);
    }

    @Override
    public Observable<BarcodeResponse> getBundleApiCall(Process.DataParam dataParam) {
        return getApiHelper().getGreyProductByBarcode(dataParam.getBarcode());
    }

    @Override
    public Observable<BarcodeIssueResponse> getIssueApiCall(Process.DataParam dataParam) {
        return getApiHelper().getGreyProductIssueByBarcode(dataParam.getBarcode());
    }

    @Override
    public Observable<GmtsBarcodeResponse> getGmtsBarcodeCall(Process.DataParam dataParam) {
        return getApiHelper().getGmtsBarcode(dataParam.getBarcode(), 4);
    }

    @Override
    public Observable<CuttingQcBarcodeResponse> getCuttingQcCall(Process.DataParam dataParam) {
        return getApiHelper().getCuttingQcBarcode(dataParam.getBarcode());
    }

    @Override
    public Observable<FinishFabricResponse> getFinishFabricCall(Process.DataParam dataParam) {
        return getApiHelper().getFinishFabricBarcode(dataParam.getBarcode());
    }

    @Override
    public Observable<FinishFabricResultSet> getFinishFabricResultSetCall(Process.DataParam dataParam) {
        return getApiHelper().getFinishFabricResultSetBarcode(dataParam.getBarcode());
    }

    @Override
    public Observable<FinishFabricIssueSet> getFinishFabricIssueCall(Process.DataParam dataParam) {
        return getApiHelper().getFinishFabricIssueSetBarcode(dataParam.getBarcode());
    }

    @Override
    public Observable<FinishFabricRollReceive> getFinishFabricRollCall(Process.DataParam dataParam) {
        return getApiHelper().getFinishFabricRollCall(dataParam.getBarcode());
    }

    @Override
    public Observable<KnittingResponse> getKnittingCall(Process.DataParam dataParam) {
        return getApiHelper().getKnittingCall(dataParam.getBarcode());
    }

    @Override
    public Observable<SewingResponse> getSewingBarcodeCall(Process.DataParam dataParam) {
        return getApiHelper().getSewingBarcode(dataParam.getBarcode(), dataParam.getTypeParam().equalsIgnoreCase("input")?4:5);
    }

    @Override
    public Observable<EmbSpBarcodeResponse> getEmbSpBarcodeCall(Process.DataParam dataParam) {
        int type = dataParam.getType();
        int action = dataParam.getTypeParam().equalsIgnoreCase("issue")?1:2;
        return getApiHelper().getEmbSpBarcode(dataParam.getBarcode(), type, action);
    }
}
