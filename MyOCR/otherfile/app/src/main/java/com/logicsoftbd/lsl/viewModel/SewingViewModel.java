package com.logicsoftbd.lsl.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseSewingInputClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_StyleWiseOperationResponse;
import com.logicsoftbd.lsl.repository.SewingRepository;

import java.io.Closeable;

public class SewingViewModel extends ViewModel {
    private SewingRepository sewingRepository;

    public SewingViewModel() {
        sewingRepository = new SewingRepository();
    }

    public LiveData<Boolean> getIsLoading() {
        return sewingRepository.getIsLoading();
    }

    public MutableLiveData<V1_StyleWiseOperationResponse> getStyleWiseWithoutPOOperationSubContractCall(String JOB_NO_MST, String ITEM_NUMBER_ID, String STYLE_NUMBER, String LINE_ID, String USER_ID) {
        return sewingRepository.getStyleWiseWithoutPOOperationSubContractCall(JOB_NO_MST, ITEM_NUMBER_ID, STYLE_NUMBER, LINE_ID, USER_ID);
    }

    public MutableLiveData<V1_StyleWiseOperationResponse> getStyleWiseWithoutPOOperationCall(String JOB_NO_MST, String ITEM_NUMBER_ID, String STYLE_NUMBER, String LINE_ID, String USER_ID) {
        return sewingRepository.getStyleWiseWithoutPOOperationSubContractCall(JOB_NO_MST, ITEM_NUMBER_ID, STYLE_NUMBER, LINE_ID, USER_ID);
    }

}
