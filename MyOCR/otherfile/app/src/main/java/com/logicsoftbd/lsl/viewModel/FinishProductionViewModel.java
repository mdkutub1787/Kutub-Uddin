package com.logicsoftbd.lsl.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.logicsoftbd.lsl.data.network.model.CompactBatchScanResponse;
import com.logicsoftbd.lsl.data.network.model.CompactingDefaultResponse;
import com.logicsoftbd.lsl.data.network.model.FabricShade;
import com.logicsoftbd.lsl.data.network.model.FloorWiseMachineResponse;
import com.logicsoftbd.lsl.data.network.model.SlitteringSequzBarCodeResponse;
import com.logicsoftbd.lsl.data.network.model.SlitteringSequzCompanyWiseFloorResponse;
import com.logicsoftbd.lsl.data.network.model.SlitteringSequzSaveResponse;
import com.logicsoftbd.lsl.data.network.model.StenteringBatchScanResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_AOPBagKeepingResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_AOPBagKeepingSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_AOPDepartmentStoreResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_AOPDeptBagReceiveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_AOPDeptBagSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagDeliveryResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagDeliverySaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagEmptyReceiveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagEmptyReceiveSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagIssueResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagIssueSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagKeepingDataBySystemResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagKeepingResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagKeepingSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagPrintResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagReceiveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagReceiveSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagReturnResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagReturnSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BarcodeByBatchForQCResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BarcodeDetailsFromBatchFinishQCResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseCompanyToLocationClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseFloorWiseLineClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseLocationWiseFloorClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseSewingInputClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DataSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DyedAOPBagReceiveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DyeingProductionPDAResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DyeingProductionPDASaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FabricBagColorModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FabricFinishQCUpdateModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FinishLocationWiseFloorClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FinishingDataResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FinishingMachineModelResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FinishingQCModelResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FinishingQCSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FinishingSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GMTFinishReceiveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GMTFinishReceiveSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyStoreRejectBagReceiveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ShiftResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SpecialFinishSubProcessResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_transfer_out.V1GreyFabricTransferOutStoreListResponse;
import com.logicsoftbd.lsl.repository.FinishProductionRepository;
import okhttp3.RequestBody;

public class FinishProductionViewModel extends ViewModel {
    private final FinishProductionRepository finishProductionRepository;
    public FinishProductionViewModel() {
        finishProductionRepository = new FinishProductionRepository();
    }
    public LiveData<Boolean> getIsLoading() {
        return finishProductionRepository.getIsLoading();
    }

    public MutableLiveData<CompactingDefaultResponse> getFinishProductionDefaultResponse(String entry_form_no) {
        return finishProductionRepository.getFinishProductionDefaultResponse(entry_form_no);
    }

    public MutableLiveData<SlitteringSequzCompanyWiseFloorResponse> getFinishProductionCompanyWiseFloorResponse(String company_id) {
        return finishProductionRepository.getFinishProductionCompanyWiseFloorResponse(company_id);
    }

    public MutableLiveData<FloorWiseMachineResponse> getFinishProductionCompanyWiseFloorMachineResponse(String company_floor_id) {
        return finishProductionRepository.getFinishProductionCompanyWiseFloorMachineResponse(company_floor_id);
    }

    public MutableLiveData<CompactBatchScanResponse> getFinishProductionCompactingBatchScanResponse(String batch, String entry_form_no) {
        return finishProductionRepository.getFinishProductionCompactingBatchScanResponse(batch, entry_form_no);
    }

    public MutableLiveData<StenteringBatchScanResponse> getFinishProductionStenteringBatchScanResponse(String batch, String entry_form_no) {
        return finishProductionRepository.getFinishProductionStenteringBatchScanResponse(batch, entry_form_no);
    }

    public MutableLiveData<SlitteringSequzBarCodeResponse> getFinishProductionSlittingBatchScanResponse(String batch, String entry_form_no) {
        return finishProductionRepository.getFinishProductionSlittingBatchScanResponse(batch, entry_form_no);
    }

    public MutableLiveData<SlitteringSequzSaveResponse> postFinishProductionCompactingResponse(RequestBody body) {
        return finishProductionRepository.postFinishProductionCompactingResponse(body);
    }

    public MutableLiveData<V1_BarcodeByBatchForQCResponse> getBarcodeByBatchForQCResponse(String batch) {
        return finishProductionRepository.getBarcodeByBatchForQCResponse(batch);
    }

    public MutableLiveData<V1_BarcodeDetailsFromBatchFinishQCResponse> getBarcodeDetailsFromBatchForQCResponse(String barcode_no) {
        return finishProductionRepository.getBarcodeDetailsFromBatchForQCResponse(barcode_no);
    }

    public MutableLiveData<V1_DataSaveResponse> postFinishFabricQCV2Response(RequestBody body) {
        return finishProductionRepository.postFinishFabricQCV2esponse(body);
    }

    public MutableLiveData<V1_ShiftResponse> getShiftResponse() {
        return finishProductionRepository.getShiftResponse();
    }

    public MutableLiveData<FabricShade> getShadeResponse() {
        return finishProductionRepository.getShadeResponse();
    }

    public MutableLiveData<V1_BagKeepingResponse> getBagKeepingResponse(String batchNo, String bagNo, String selectedColorId) {
        return finishProductionRepository.getBagKeepingResponse(batchNo, bagNo, selectedColorId);
    }
    public MutableLiveData<V1_BagPrintResponse> getBagKeepingPrintResponse(String batchNo, String bagNo) {
        return finishProductionRepository.getBagKeepingPrintResponse(batchNo, bagNo);
    }

    public MutableLiveData<V1_AOPBagKeepingResponse> getAOPBagKeepingResponse(String searchType, String searchQuery, String bagNo, String selectedColorId) {
        return finishProductionRepository.getAOPBagKeepingResponse(searchType, searchQuery, bagNo, selectedColorId);
    }

    public MutableLiveData<V1_BagKeepingDataBySystemResponse> getBagKeepingBySystemResponse(String systemNo, String bagNo, Integer selectedColorId) {
        return finishProductionRepository.getBagKeepingBySystemResponse(systemNo, bagNo, selectedColorId);
    }

    public MutableLiveData<V1_AOPDeptBagReceiveResponse> getAOPDeptBagReceiveResponse( String bagNo, Integer selectedColorId) {
        return finishProductionRepository.getAOPDeptBagReceiveResponse( bagNo, selectedColorId);
    }

    public MutableLiveData<V1_AOPDeptBagReceiveResponse> getAOPDeptBagReceiveBySystemNoResponse(String systemNo) {
        return finishProductionRepository.getAOPDeptBagReceiveBySystemNoResponse(systemNo);
    }

    public MutableLiveData<V1_DyedAOPBagReceiveResponse> getDyedAOPDeptBagReceiveResponse(String systemNo, String bagNo, Integer selectedColorId) {
        return finishProductionRepository.getAOPDyedDeptBagReceiveResponse(systemNo, bagNo, selectedColorId);
    }

    public MutableLiveData<V1_GreyStoreRejectBagReceiveResponse> getGreyStoreRejectBagResponse(String bagNo, Integer selectedColorId) {
        return finishProductionRepository.getGreyStoreRejectBagResponse(bagNo, selectedColorId);
    }

    public MutableLiveData<V1_GreyStoreRejectBagReceiveResponse> getGreyStoreRejectBagBySystemNoResponse(String system_no) {
        return finishProductionRepository.getGreyStoreRejectBagBySystemNoResponse(system_no);
    }
    public MutableLiveData<V1_AOPDepartmentStoreResponse> getAOPDeptStoreResponse(String user_id, String company_id, String item_category_id) {
        return finishProductionRepository.getAOPDeptStoreResponse(user_id, company_id, item_category_id);
    }

    public MutableLiveData<V1_FabricBagColorModel> getFabricBagColorResponse() {
        return finishProductionRepository.getFabricBagColorResponse();
    }

    public MutableLiveData<V1_BagKeepingSaveResponse> postBagKeepingResponse(RequestBody body) {
        return finishProductionRepository.postBagKeeping(body);
    }

    public MutableLiveData<V1_AOPBagKeepingSaveResponse> postAOPBagKeepingResponse(RequestBody body) {
        return finishProductionRepository.postAOPBagKeeping(body);
    }

    public MutableLiveData<V1_BagKeepingSaveResponse> postBagKeepingQCResponse(RequestBody body) {
        return finishProductionRepository.postBagKeepingQC(body);
    }
    public MutableLiveData<V1_AOPDeptBagSaveResponse> postAOPDeptReceiveResponse(RequestBody body) {
        return finishProductionRepository.postAOPDeptReceive(body);
    }
   public MutableLiveData<V1_AOPDeptBagSaveResponse> postAOPDyedReceiveResponse(RequestBody body) {
        return finishProductionRepository.postAOPDyedReceive(body);
    }

    public MutableLiveData<V1_AOPDeptBagSaveResponse> postGreyStoreRejectBagResponse(RequestBody body) {
        return finishProductionRepository.postGreyStoreRejectBagResponse(body);
    }

    public MutableLiveData<V1_BagDeliveryResponse> getBagDeliveryResponse(String bagNo) {
        return  finishProductionRepository.getBagDeliveryResponse(bagNo);
    }

    public MutableLiveData<V1_BagEmptyReceiveResponse> getBagEmptyReceiveResponse(String bagNo, String batchNo, Integer selectedColorId, Integer selectedCategoryId) {
        return  finishProductionRepository.getBagEmptyReceiveResponse(bagNo, batchNo, selectedColorId, selectedCategoryId);
    }

    public MutableLiveData<V1GreyFabricTransferOutStoreListResponse> getStoreResponse(String userId, String itemCategory, String companyId) {
        return  finishProductionRepository.getStoreResponse(userId, itemCategory, companyId);
    }

    public MutableLiveData<V1_BagDeliverySaveResponse> postBagDeliveryResponse(RequestBody body) {
        return finishProductionRepository.postBagDelivery(body);
    }

    public MutableLiveData<V1_BagEmptyReceiveSaveResponse> postBagEmptyReceiveResponse(RequestBody body) {
        return finishProductionRepository.postBagEmptyReceive(body);
    }

    public MutableLiveData<V1_BagReceiveResponse> getBagReceiveResponse(String bagNo, String rackId, Integer colorId, Integer selectedCategoryId) {
        return finishProductionRepository.getBagReceiveResponse(bagNo, rackId, colorId, selectedCategoryId);
    }
    public MutableLiveData<V1_BagReceiveResponse> getBagChallanReceiveResponse(String challan, String rackId, Integer selectedCategoryId) {
        return finishProductionRepository.getBagChallanReceiveResponse(challan, rackId, selectedCategoryId);
    }

    public MutableLiveData<V1_BagReceiveSaveResponse> postBagReceiveResponse(RequestBody body) {
        return finishProductionRepository.postBagReceiveResponse(body);
    }
    public MutableLiveData<V1_BagIssueResponse> getBagIssueResponse(String bagScan, String rackId, Integer selectedColorId, Integer selectedCategoryId) {
        return finishProductionRepository.getBagIssueResponse(bagScan, rackId, selectedColorId, selectedCategoryId);
    }

    public MutableLiveData<V1_BagIssueResponse> getBagIssueByChallanResponse(String challan, String rackId, Integer selectedColorId, Integer selectedCategoryId) {
        return finishProductionRepository.getBagIssueByChallanResponse(challan, rackId, selectedColorId, selectedCategoryId);
    }

    public MutableLiveData<V1_BagIssueSaveResponse> postBagIssueResponse(RequestBody body) {
        return finishProductionRepository.postBagIssueResponse(body);
    }

    public MutableLiveData<V1_BagReturnResponse> getBagReturnResponse(String bagNo, Integer selectedColorId, Integer selectedCategoryId) {
        return finishProductionRepository.getBagReturnResponse(bagNo, selectedColorId, selectedCategoryId);
    }

    public MutableLiveData<V1_BagReturnSaveResponse> postBagReturnResponse(RequestBody body) {
        return finishProductionRepository.postBagReturnResponse(body);
    }

    public MutableLiveData<V1_BundleWiseSewingInputClass> getCompanyAndSourceResponse() {
        return finishProductionRepository.getCompanyAndSourceResponse();
    }

    public MutableLiveData<V1_BundleWiseCompanyToLocationClass> getCompanyWiseLocationResponse(Integer companyId) {
        return finishProductionRepository.getCompanyWiseLocationResponse(companyId);
    }

    public MutableLiveData<V1_BundleWiseLocationWiseFloorClass> getFinishLocationWiseFloorResponse(Integer location_id) {
        return finishProductionRepository.getLocationWiseFloorResponse(location_id);
    }

    public MutableLiveData<V1_FinishLocationWiseFloorClass> getFinishLocationWiseFloorResponse(Integer location_id, Integer process) {
        return finishProductionRepository.getFinishLocationWiseFloorResponse(location_id, process);
    }

    public MutableLiveData<V1_BundleWiseFloorWiseLineClass> getFloorWiseLineResponse(Integer company_id, Integer location_id, Integer floor_id, String issue_date) {
        return finishProductionRepository.getFloorWiseLineResponse(company_id, location_id, floor_id, issue_date);
    }

    public MutableLiveData<V1_GMTFinishReceiveResponse> getGMTFinishReciveBarcodeResponse(String barcode) {
        return finishProductionRepository.getGMTFinishReceiveBarcodeResponse(barcode);
    }

    public MutableLiveData<V1_GMTFinishReceiveSaveResponse> postGmtFinishReceiveResponse(RequestBody body) {
        return finishProductionRepository.postGmtFinishReceiveResponse(body);
    }

    public MutableLiveData<V1_FinishingQCModelResponse> getFinishingingQCDataResponse(String batchNo) {
        return finishProductionRepository.getFinishingQCClassCall(batchNo);
    }

    public MutableLiveData<V1_FabricFinishQCUpdateModel> getFinishingingQCUpdateDataResponse(String rollNo, String barcodeNo, String batchNo) {
        return finishProductionRepository.getFinishingQCUpdateClassCall(rollNo, barcodeNo, batchNo);
    }

    public MutableLiveData<V1_FinishingMachineModelResponse> getFinishingMachineResponse() {
        return finishProductionRepository.getFinishingMachineCall();
    }

    public MutableLiveData<V1_FinishingQCSaveResponse> postFinishQCResponse(RequestBody body) {
        return finishProductionRepository.postFinishQCResponse(body);
    }

    public MutableLiveData<V1_FinishingDataResponse> getFinishingDataResponse(String batchNo, String entryFormNo) {
        return finishProductionRepository.getFinishingClassCall(batchNo, entryFormNo);
    }

    public MutableLiveData<V1_FinishingSaveResponse> postFinishingResponse(RequestBody body) {
        return finishProductionRepository.postFinishingResponse(body);
    }

    public MutableLiveData<V1_SpecialFinishSubProcessResponse> getSpecialFinishSubProcessList() {
        return finishProductionRepository.getSpecialFinishSubProcessList();
    }

    public MutableLiveData<V1_DyeingProductionPDASaveResponse> postDyeingProductionPDAResponse(RequestBody body) {
        return finishProductionRepository.postDyeingProductionPDAResponse(body);
    }

    public MutableLiveData<V1_DyeingProductionPDAResponse> getDyeingProductionPDAResponse(String batch_id) {
        return finishProductionRepository.getDyeingProductionCall(batch_id);
    }
}
