package com.logicsoftbd.lsl.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.logicsoftbd.lsl.data.network.model.CompactingDefaultResponse;
import com.logicsoftbd.lsl.data.network.model.SlitteringSequzSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundeWiseSewingInputPCSResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DataSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingInputPCSSaveResponse;
import com.logicsoftbd.lsl.repository.BundleWiseRepository;
import com.logicsoftbd.lsl.repository.FinishProductionRepository;

import okhttp3.RequestBody;

public class BundleWiseViewModel extends ViewModel {
    private final BundleWiseRepository bundleWiseRepository;
    public BundleWiseViewModel() {
        bundleWiseRepository = new BundleWiseRepository();
    }
    public LiveData<Boolean> getIsLoading() {
        return bundleWiseRepository.getIsLoading();
    }

    public MutableLiveData<V1_BundeWiseSewingInputPCSResponse> getBundleWiseSewingInputPCSResponse(String company_id, String location, String floor, String line, String barcode, String type) {
        return bundleWiseRepository.getBundleWiseSewingInputPCSResponse(company_id, location, floor, line, barcode, type);
    }

    public MutableLiveData<V1_SewingInputPCSSaveResponse> postBundleWiseSewingInputPCSResponse(RequestBody body) {
        return bundleWiseRepository.postBundleWiseSewingInputPCSResponse(body);
    }
}
