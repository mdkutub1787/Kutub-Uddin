package com.logicsoftbd.lsl.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseSewingInputClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_RFIDIssueReturnSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_RFIDTransferDropdownModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_RFIDTransferModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_RFIDTransferSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_YarnIssueReturnRFIDValidityCheckResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_YarnIssueReturnResponse;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.serviceInterface.RetrofitApiClient;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RFIDRepository {
    private static final String TAG = "RFIDRepository";
    private static RFIDRepository rfidRepository;

    public static RFIDRepository getInstance(){
        if (rfidRepository == null){
            rfidRepository = new RFIDRepository();
        }
        return rfidRepository;
    }

    private ApiInterface apiInterface;

    public RFIDRepository(){
        apiInterface = RetrofitApiClient.createService(ApiInterface.class);
    }

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<V1_BundleWiseSewingInputClass> getCompanayCall(){
        isLoading.setValue(true);
        MutableLiveData<V1_BundleWiseSewingInputClass> companyData = new MutableLiveData<>();
        apiInterface.getBundkeWiseSewingInputClassCall().enqueue(new Callback<V1_BundleWiseSewingInputClass>() {
            @Override
            public void onResponse(Call<V1_BundleWiseSewingInputClass> call,
                                   Response<V1_BundleWiseSewingInputClass> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        companyData.setValue(response.body());
                    }else {
                        companyData.setValue(null);
                    }
                }else {
                    companyData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<V1_BundleWiseSewingInputClass> call, Throwable t) {
                companyData.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return companyData;
    }
    public MutableLiveData<V1_RFIDTransferModel> getRFIDTransferCall(String store_id, String floor_id, String room_id, String rack_id, String shelf_id, String bin_id){
        isLoading.setValue(true);
        MutableLiveData<V1_RFIDTransferModel> rfidTransferData = new MutableLiveData<>();
        apiInterface.rfidTransferStoreData(store_id, floor_id, room_id, rack_id, shelf_id, bin_id).enqueue(new Callback<V1_RFIDTransferModel>() {
            @Override
            public void onResponse(Call<V1_RFIDTransferModel> call,
                                   Response<V1_RFIDTransferModel> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        rfidTransferData.setValue(response.body());
                    }else {
                        rfidTransferData.setValue(null);
                    }
                }else {
                    rfidTransferData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<V1_RFIDTransferModel> call, Throwable t) {
                rfidTransferData.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return rfidTransferData;
    }

    public MutableLiveData<V1_RFIDTransferDropdownModel> getRFIDTransferDropdownCall(String company_id, String user_id, String store_id, String floor_id, String room_id, String rack_id, String shelf_id, String bin_id){
        isLoading.setValue(true);
        MutableLiveData<V1_RFIDTransferDropdownModel> rfidTransferDropdownData = new MutableLiveData<>();
        apiInterface.rfidTransferStoreDropdownData(company_id, user_id, store_id, floor_id, room_id, rack_id, shelf_id, bin_id).enqueue(new Callback<V1_RFIDTransferDropdownModel>() {
            @Override
            public void onResponse(Call<V1_RFIDTransferDropdownModel> call,
                                   Response<V1_RFIDTransferDropdownModel> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        rfidTransferDropdownData.setValue(response.body());
                    }else {
                        rfidTransferDropdownData.setValue(null);
                    }
                }else {
                    rfidTransferDropdownData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<V1_RFIDTransferDropdownModel> call, Throwable t) {
                rfidTransferDropdownData.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return rfidTransferDropdownData;
    }

    public MutableLiveData<V1_RFIDTransferSaveResponse> postRFIDTransferResponse(RequestBody body){
        isLoading.setValue(true);
        MutableLiveData<V1_RFIDTransferSaveResponse> rfidTransferSaveResponse = new MutableLiveData<>();
        apiInterface.saveRFIDTransferStoreCall(body).enqueue(new Callback<V1_RFIDTransferSaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_RFIDTransferSaveResponse> call,
                                   @NonNull Response<V1_RFIDTransferSaveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        rfidTransferSaveResponse.setValue(response.body());
                    }else {
                        rfidTransferSaveResponse.setValue(null);
                    }
                }else {
                    rfidTransferSaveResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_RFIDTransferSaveResponse> call, @NonNull Throwable t) {
                rfidTransferSaveResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return rfidTransferSaveResponse;
    }

    public MutableLiveData<V1_YarnIssueReturnResponse> getYarnIssueReturnResponseCall(String yarn_issue_number){
        isLoading.setValue(true);
        MutableLiveData<V1_YarnIssueReturnResponse> responseData = new MutableLiveData<>();
        apiInterface.getYarnIssueReturnCall(yarn_issue_number).enqueue(new Callback<V1_YarnIssueReturnResponse>() {
            @Override
            public void onResponse(Call<V1_YarnIssueReturnResponse> call,
                                   Response<V1_YarnIssueReturnResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        responseData.setValue(response.body());
                    }else {
                        responseData.setValue(null);
                    }
                }else {
                    responseData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<V1_YarnIssueReturnResponse> call, Throwable t) {
                responseData.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return responseData;
    }

    public MutableLiveData<V1_RFIDIssueReturnSaveResponse> postRFIDIssueReturnResponse(RequestBody body){
        isLoading.setValue(true);
        MutableLiveData<V1_RFIDIssueReturnSaveResponse> apiResponse = new MutableLiveData<>();
        apiInterface.saveRFIDIssueReturnCall(body).enqueue(new Callback<V1_RFIDIssueReturnSaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_RFIDIssueReturnSaveResponse> call,
                                   @NonNull Response<V1_RFIDIssueReturnSaveResponse> response) {
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
            public void onFailure(@NonNull Call<V1_RFIDIssueReturnSaveResponse> call, @NonNull Throwable t) {
                apiResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return apiResponse;
    }

    public MutableLiveData<V1_YarnIssueReturnRFIDValidityCheckResponse> getYarnIssueReturnRFIDValidityResponseCall(String rfidNO){
        isLoading.setValue(true);
        MutableLiveData<V1_YarnIssueReturnRFIDValidityCheckResponse> responseData = new MutableLiveData<>();
        apiInterface.getYarnIssueReturnRFIDValidityCall(rfidNO).enqueue(new Callback<V1_YarnIssueReturnRFIDValidityCheckResponse>() {
            @Override
            public void onResponse(Call<V1_YarnIssueReturnRFIDValidityCheckResponse> call,
                                   Response<V1_YarnIssueReturnRFIDValidityCheckResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        responseData.setValue(response.body());
                    }else {
                        responseData.setValue(null);
                    }
                }else {
                    responseData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<V1_YarnIssueReturnRFIDValidityCheckResponse> call, Throwable t) {
                responseData.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return responseData;
    }
}
