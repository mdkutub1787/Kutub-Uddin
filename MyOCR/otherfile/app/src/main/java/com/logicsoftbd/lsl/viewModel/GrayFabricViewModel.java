package com.logicsoftbd.lsl.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.logicsoftbd.lsl.data.network.v1_model.V1_DataSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyFabricModelClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_RollWiseGrayDeliveryToStoreSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_RollWiseGreyFabricDeliveryToStoreResponse;
import com.logicsoftbd.lsl.repository.GrayFabricRepository;

import okhttp3.RequestBody;

public class GrayFabricViewModel extends ViewModel {
    private final GrayFabricRepository grayFabricRepository;
    public GrayFabricViewModel() {
        grayFabricRepository = new GrayFabricRepository();
    }

    public LiveData<Boolean> getIsLoading() {
        return grayFabricRepository.getIsLoading();
    }
    public MutableLiveData<V1_GreyFabricModelClass> getGrayFabricBarcodeDetailsResponse(String barcode_no) {
        return grayFabricRepository.getGrayFabricBarcodeDetailsResponse(barcode_no);
    }

    public MutableLiveData<V1_DataSaveResponse> postGrayFabricBarcodeDetailsResponse(RequestBody body) {
        return grayFabricRepository.postGrayFabricBarcodeDetailsResponse(body);
    }

    public MutableLiveData<V1_RollWiseGreyFabricDeliveryToStoreResponse> getRollWiseGrayFabricDeliveryToStoreResponse(String barcode_no) {
        return grayFabricRepository.getGrayFabricDeliveryToStoreResponse(barcode_no);
    }
    public MutableLiveData<V1_RollWiseGrayDeliveryToStoreSaveResponse> postGrayRollDeliveryResponse(RequestBody body) {
        return grayFabricRepository.postGrayFabricDeliveryStoreResponse(body);
    }
}
