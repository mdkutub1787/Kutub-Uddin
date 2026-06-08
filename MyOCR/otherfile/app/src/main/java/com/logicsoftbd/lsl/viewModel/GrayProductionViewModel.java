package com.logicsoftbd.lsl.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseSewingInputClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyRollIssue.V1_GreyRollIssueRequest;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyRollIssue.V1_GreyRollIssueSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyRollIssue.V1_Issue_Purpose.V1_GreyRollIssuePurposeModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyRollReceiveRequest;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyRollSaveRequest;
import com.logicsoftbd.lsl.repository.GrayProductionRepository;

import okhttp3.RequestBody;

public class GrayProductionViewModel extends ViewModel {
    private final GrayProductionRepository grayProductionRepository;

    public GrayProductionViewModel() {
        grayProductionRepository = new GrayProductionRepository();
    }

    public LiveData<Boolean> getIsLoading() {
        return grayProductionRepository.getIsLoading();
    }

    public MutableLiveData<V1_GreyRollIssueRequest> getGrayRollIssuesResponse(String barcode_no) {
        return grayProductionRepository.getGrayRollIssuesResponse(barcode_no);
    }

    public MutableLiveData<V1_GreyRollIssuePurposeModel> getGrayRollIssuesPurposeResponse() {
        return grayProductionRepository.getGrayRollIssuesPurposeResponse();
    }

    public MutableLiveData<V1_BundleWiseSewingInputClass> getGrayRollIssuesDefaultResponse() {
        return grayProductionRepository.getGrayRollIssuesDefaultResponse();
    }

    public MutableLiveData<V1_GreyRollIssueSaveResponse> postGrayRollIssueResponse(RequestBody body) {
        return grayProductionRepository.postGrayRollIssueResponse(body);
    }

    public MutableLiveData<V1_GreyRollReceiveRequest> getGrayRollReceiveByChallanResponse(String challan_no, String location) {
        return grayProductionRepository.getGrayRollReceiveByChallanResponse(challan_no, location);
    }
    public MutableLiveData<V1_GreyRollReceiveRequest> getGrayRollReceiveByBarcodeResponse(String barcode_no, String location) {
        return grayProductionRepository.getGrayRollReceiveByBarcodeResponse(barcode_no, location);
    }

    public MutableLiveData<V1_GreyRollSaveRequest> postGrayRollReceiveResponse(RequestBody body) {
        return grayProductionRepository.postGrayRollReceiveResponse(body);
    }
}
