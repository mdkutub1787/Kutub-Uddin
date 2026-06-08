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

import com.logicsoftbd.lsl.data.network.model.DefectInchModel;
import com.logicsoftbd.lsl.data.network.model.DefectListModel;
import com.logicsoftbd.lsl.data.network.model.FabricGradeModel;
import com.logicsoftbd.lsl.data.network.model.FabricShade;
import com.logicsoftbd.lsl.data.network.model.FinishFabricQrCodeResponses;
import com.logicsoftbd.lsl.data.network.model.FloorResponse;
import com.logicsoftbd.lsl.data.network.model.IssuePurposeModel;
import com.logicsoftbd.lsl.data.network.model.IssueStoreModel;
import com.logicsoftbd.lsl.data.network.model.LineResponse;
import com.logicsoftbd.lsl.data.network.model.LocationModel;
import com.logicsoftbd.lsl.data.network.model.MachineResponses;
import com.logicsoftbd.lsl.data.network.model.PurposeResponse;
import com.logicsoftbd.lsl.data.network.model.ReferenceDataResponse;
import com.logicsoftbd.lsl.data.network.model.ShiftResponses;
import com.logicsoftbd.lsl.data.network.model.StoreResponse;
import com.logicsoftbd.lsl.ui.base.MvpView;


public interface ReceiveMvpView extends MvpView {
   void onSuccess(String msg);
   void onFailed(String msg);

   void onStoreListResponse(StoreResponse storeResponse);
   void onPurposeListResponse(PurposeResponse purposeResponse);
   void onFloorListResponse(FloorResponse purposeResponse);
   void onMachineResponses(MachineResponses machineResponses);
   void onShiftResponses(ShiftResponses shiftResponses);
   void onLocationListResponse(FloorResponse purposeResponse);
   void onLineListResponse(LineResponse purposeResponse);
   void onReferenceListResponse(int type, ReferenceDataResponse referenceDataResponse);

   void finishFabricQrCodeTwoResponse(FinishFabricQrCodeResponses barcodeResponse);
   void finishFabricQrCodeBatchNoResponse(FinishFabricQrCodeResponses barcodeResponse);
   void finishFabricQrCodeBarCodeResponse(FinishFabricQrCodeResponses barcodeResponse);
   void defectInch(DefectInchModel defectInchModel);
   void issuePurpose(IssuePurposeModel issuePurposeModel);
   void issueStore(IssueStoreModel issueStoreModel);
   void defectList(DefectListModel defectListModel);
   void fabricGrade(FabricGradeModel fabricGradeModel);
   void onLogged(String onLog);
   void onLocation(LocationModel locationModel);
   void onFabricShade(FabricShade fabricGrade);
}
