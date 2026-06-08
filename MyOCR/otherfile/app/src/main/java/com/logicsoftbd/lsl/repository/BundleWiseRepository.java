package com.logicsoftbd.lsl.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.logicsoftbd.lsl.data.network.model.CompactingDefaultResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundeWiseSewingInputPCSResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DataSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingInputPCSSaveResponse;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.serviceInterface.RetrofitApiClient;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BundleWiseRepository {
    private static final String TAG = "BundleWiseRepository";

    private static BundleWiseRepository bundleWiseRepository;

    public static BundleWiseRepository getInstance() {
        if(bundleWiseRepository == null) {
            bundleWiseRepository = new BundleWiseRepository();
        }
        return bundleWiseRepository;
    }

    private final ApiInterface apiInterface;
    public BundleWiseRepository() {
        apiInterface = RetrofitApiClient.createService(ApiInterface.class);
    }

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<V1_BundeWiseSewingInputPCSResponse> getBundleWiseSewingInputPCSResponse(String company_id, String location, String floor, String line, String barcode, String type){
        isLoading.setValue(true);
        MutableLiveData<V1_BundeWiseSewingInputPCSResponse> finishProductionDefaultResponse = new MutableLiveData<>();
        apiInterface.getSewingInputPCSModelClassCall(company_id, location, floor, line, barcode, type).enqueue(new Callback<V1_BundeWiseSewingInputPCSResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_BundeWiseSewingInputPCSResponse> call,
                                   @NonNull Response<V1_BundeWiseSewingInputPCSResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        finishProductionDefaultResponse.setValue(response.body());
                    }else {
                        finishProductionDefaultResponse.setValue(null);
                    }
                }else {
                    finishProductionDefaultResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_BundeWiseSewingInputPCSResponse> call, @NonNull Throwable t) {
                finishProductionDefaultResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return finishProductionDefaultResponse;
    }

    public MutableLiveData<V1_SewingInputPCSSaveResponse> postBundleWiseSewingInputPCSResponse(RequestBody body){
        isLoading.setValue(true);
        MutableLiveData<V1_SewingInputPCSSaveResponse> finishProductionDefaultResponse = new MutableLiveData<>();
        apiInterface.saveUpdateBundleSewingInputPCSCall(body).enqueue(new Callback<V1_SewingInputPCSSaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_SewingInputPCSSaveResponse> call,
                                   @NonNull Response<V1_SewingInputPCSSaveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        finishProductionDefaultResponse.setValue(response.body());
                    }else {
                        finishProductionDefaultResponse.setValue(null);
                    }
                }else {
                    finishProductionDefaultResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_SewingInputPCSSaveResponse> call, @NonNull Throwable t) {
                finishProductionDefaultResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return finishProductionDefaultResponse;
    }
}
