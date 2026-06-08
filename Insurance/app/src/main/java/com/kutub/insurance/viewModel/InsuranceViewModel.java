package com.kutub.insurance.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kutub.insurance.model.ApiResponse;
import com.kutub.insurance.model.BillResponse;
import com.kutub.insurance.model.PolicyResponse;
import com.kutub.insurance.repository.InsuranceRepository;

import java.util.List;

import okhttp3.RequestBody;

public class InsuranceViewModel extends ViewModel {

    private final InsuranceRepository insuranceRepository;
    public InsuranceViewModel() {
        insuranceRepository = new InsuranceRepository();
    }
    public LiveData<Boolean> getIsLoading() {
        return insuranceRepository.getIsLoading();
    }

    public MutableLiveData<List<PolicyResponse>> getPolicy() {
        return insuranceRepository.getPolicy();
    }

    public MutableLiveData<ApiResponse> postPolicy(RequestBody body) {
        return insuranceRepository.postPolicy(body);
    }

    public MutableLiveData<List<BillResponse>> getBill() {
        return insuranceRepository.getBill();
    }

    public MutableLiveData<ApiResponse> postBill(RequestBody body) {
        return insuranceRepository.postBill(body);
    }
}