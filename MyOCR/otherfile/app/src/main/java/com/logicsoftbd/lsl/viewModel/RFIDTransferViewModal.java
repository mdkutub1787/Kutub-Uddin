package com.logicsoftbd.lsl.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseSewingInputClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_RFIDIssueReturnSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_RFIDTransferDropdownModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_RFIDTransferModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_RFIDTransferSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_YarnIssueReturnRFIDValidityCheckResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_YarnIssueReturnResponse;
import com.logicsoftbd.lsl.repository.RFIDRepository;

import okhttp3.RequestBody;

public class RFIDTransferViewModal extends ViewModel {
    private RFIDRepository rfidRepository;

    public RFIDTransferViewModal() {
        rfidRepository = new RFIDRepository();
    }

    public LiveData<Boolean> getIsLoading() {
        return rfidRepository.getIsLoading();
    }

    public MutableLiveData<V1_BundleWiseSewingInputClass> getCompanyResponse() {
        return rfidRepository.getCompanayCall();
    }
    public MutableLiveData<V1_RFIDTransferModel> getRFIDTransferStoreResponse(String store_id, String floor_id, String room_id, String rack_id, String shelf_id, String bin_id) {
        return rfidRepository.getRFIDTransferCall(store_id, floor_id, room_id, rack_id, shelf_id, bin_id);
    }

    public MutableLiveData<V1_RFIDTransferDropdownModel> getRFIDTransferStoreDropdownResponse(String company_id, String user_id, String store_id, String floor_id, String room_id, String rack_id, String shelf_id, String bin_id) {
        return rfidRepository.getRFIDTransferDropdownCall(company_id, user_id, store_id, floor_id, room_id, rack_id, shelf_id, bin_id);
    }

    public MutableLiveData<V1_RFIDTransferSaveResponse> postRFIDTransferStoreResponse(RequestBody body) {
        return rfidRepository.postRFIDTransferResponse(body);
    }

    public MutableLiveData<V1_YarnIssueReturnResponse> getYarnIssueReturnResponse(String yarn_issue_number) {
        return rfidRepository.getYarnIssueReturnResponseCall(yarn_issue_number);
    }

    public MutableLiveData<V1_RFIDIssueReturnSaveResponse> postRFIDIssueReturnResponse(RequestBody body) {
        return rfidRepository.postRFIDIssueReturnResponse(body);
    }

    public MutableLiveData<V1_YarnIssueReturnRFIDValidityCheckResponse> getRFIDIssueReturnValidityCheckResponse(String rfidNo) {
        return rfidRepository.getYarnIssueReturnRFIDValidityResponseCall(rfidNo);
    }
}
