package com.logicsoftbd.lsl.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.logicsoftbd.lsl.data.network.v1_model.V1_DataSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyFabricModelClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_RollWiseGrayDeliveryToStoreSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_RollWiseGreyFabricDeliveryToStoreResponse;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.serviceInterface.RetrofitApiClient;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GrayFabricRepository {
    private static final String TAG = "GrayFabricRepository";
    private  static GrayFabricRepository grayFabricRepository;

    public  static  GrayFabricRepository getInstance() {
        if(grayFabricRepository == null) {
            grayFabricRepository = new GrayFabricRepository();
        }
        return grayFabricRepository;
    }
    private final ApiInterface apiInterface;
    public GrayFabricRepository(){
        apiInterface = RetrofitApiClient.createService(ApiInterface.class);
    }
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<V1_GreyFabricModelClass> getGrayFabricBarcodeDetailsResponse(String barcode_no){
        isLoading.setValue(true);
        MutableLiveData<V1_GreyFabricModelClass> grayFabricBarcodeDetailsResponse = new MutableLiveData<>();
        apiInterface.getBarCodeModelClassCall(barcode_no).enqueue(new Callback<V1_GreyFabricModelClass>() {
            @Override
            public void onResponse(@NonNull Call<V1_GreyFabricModelClass> call,
                                   @NonNull Response<V1_GreyFabricModelClass> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        grayFabricBarcodeDetailsResponse.setValue(response.body());
                    }else {
                        grayFabricBarcodeDetailsResponse.setValue(null);
                    }
                }else {
                    grayFabricBarcodeDetailsResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_GreyFabricModelClass> call, @NonNull Throwable t) {
                grayFabricBarcodeDetailsResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return grayFabricBarcodeDetailsResponse;
    }

    public MutableLiveData<V1_DataSaveResponse> postGrayFabricBarcodeDetailsResponse(RequestBody body){
        isLoading.setValue(true);
        MutableLiveData<V1_DataSaveResponse> grayFabricBarcodeDetailsResponse = new MutableLiveData<>();
        apiInterface.saveUpdateknittingFabricCall(body).enqueue(new Callback<V1_DataSaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_DataSaveResponse> call,
                                   @NonNull Response<V1_DataSaveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        grayFabricBarcodeDetailsResponse.setValue(response.body());
                    }else {
                        grayFabricBarcodeDetailsResponse.setValue(null);
                    }
                }else {
                    grayFabricBarcodeDetailsResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_DataSaveResponse> call, @NonNull Throwable t) {
                grayFabricBarcodeDetailsResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return grayFabricBarcodeDetailsResponse;
    }


    public MutableLiveData<V1_RollWiseGreyFabricDeliveryToStoreResponse> getGrayFabricDeliveryToStoreResponse(String barcode){
        isLoading.setValue(true);
        MutableLiveData<V1_RollWiseGreyFabricDeliveryToStoreResponse> grayFabricDeliveryToStoreResponse = new MutableLiveData<>();
        apiInterface.getRollWiseGrayFabricDeliveryToStoreCall(barcode).enqueue(new Callback<V1_RollWiseGreyFabricDeliveryToStoreResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_RollWiseGreyFabricDeliveryToStoreResponse> call,
                                   @NonNull Response<V1_RollWiseGreyFabricDeliveryToStoreResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        grayFabricDeliveryToStoreResponse.setValue(response.body());
                    }else {
                        grayFabricDeliveryToStoreResponse.setValue(null);
                    }
                }else {
                    grayFabricDeliveryToStoreResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_RollWiseGreyFabricDeliveryToStoreResponse> call, @NonNull Throwable t) {
                grayFabricDeliveryToStoreResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return grayFabricDeliveryToStoreResponse;
    }

    public MutableLiveData<V1_RollWiseGrayDeliveryToStoreSaveResponse> postGrayFabricDeliveryStoreResponse(RequestBody body){
        isLoading.setValue(true);
        MutableLiveData<V1_RollWiseGrayDeliveryToStoreSaveResponse> grayFabricBarcodeDetailsResponse = new MutableLiveData<>();
        apiInterface.saveUpdateGrayFabricDeliveryToStoreCall(body).enqueue(new Callback<V1_RollWiseGrayDeliveryToStoreSaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_RollWiseGrayDeliveryToStoreSaveResponse> call,
                                   @NonNull Response<V1_RollWiseGrayDeliveryToStoreSaveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        grayFabricBarcodeDetailsResponse.setValue(response.body());
                    }else {
                        grayFabricBarcodeDetailsResponse.setValue(null);
                    }
                }else {
                    grayFabricBarcodeDetailsResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_RollWiseGrayDeliveryToStoreSaveResponse> call, @NonNull Throwable t) {
                grayFabricBarcodeDetailsResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return grayFabricBarcodeDetailsResponse;
    }
}
