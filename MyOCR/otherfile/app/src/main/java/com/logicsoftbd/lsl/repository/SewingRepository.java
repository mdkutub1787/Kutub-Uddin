package com.logicsoftbd.lsl.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseSewingInputClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_StyleWiseOperationResponse;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.serviceInterface.RetrofitApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SewingRepository {
    private static final String TAG = "SewingRepository";
    private static SewingRepository sewingRepository;

    public static SewingRepository getInstance(){
        if (sewingRepository == null){
            sewingRepository = new SewingRepository();
        }
        return sewingRepository;
    }

    private ApiInterface apiInterface;

    public SewingRepository() {
        apiInterface = RetrofitApiClient.createService(ApiInterface.class);
    }

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<V1_StyleWiseOperationResponse> getStyleWiseWithoutPOOperationSubContractCall(String JOB_NO_MST, String ITEM_NUMBER_ID, String STYLE_NUMBER, String LINE_ID, String USER_ID){
        isLoading.setValue(true);
        MutableLiveData<V1_StyleWiseOperationResponse> apiResponse = new MutableLiveData<>();
        apiInterface.getStyleWiseWithoutPOOperationSubContractCall(JOB_NO_MST, ITEM_NUMBER_ID, STYLE_NUMBER, LINE_ID, USER_ID).enqueue(new Callback<V1_StyleWiseOperationResponse>() {
            @Override
            public void onResponse(Call<V1_StyleWiseOperationResponse> call,
                                   Response<V1_StyleWiseOperationResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        apiResponse.setValue(response.body());
                    }else {
                        apiResponse.setValue(null);
                    }
                }else {
                    apiResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<V1_StyleWiseOperationResponse> call, Throwable t) {
                apiResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return apiResponse;
    }

    public MutableLiveData<V1_StyleWiseOperationResponse> getStyleWiseWithoutPOOperationCall(String JOB_NO_MST, String ITEM_NUMBER_ID, String STYLE_NUMBER, String LINE_ID, String USER_ID){
        isLoading.setValue(true);
        MutableLiveData<V1_StyleWiseOperationResponse> apiResponse = new MutableLiveData<>();
        apiInterface.getStyleWiseWithoutPOOperationCall(JOB_NO_MST, ITEM_NUMBER_ID, STYLE_NUMBER, LINE_ID, USER_ID).enqueue(new Callback<V1_StyleWiseOperationResponse>() {
            @Override
            public void onResponse(Call<V1_StyleWiseOperationResponse> call,
                                   Response<V1_StyleWiseOperationResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        apiResponse.setValue(response.body());
                    }else {
                        apiResponse.setValue(null);
                    }
                }else {
                    apiResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<V1_StyleWiseOperationResponse> call, Throwable t) {
                apiResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return apiResponse;
    }
}
