package com.logicsoftbd.lsl.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseSewingInputClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyRollIssue.V1_GreyRollIssueRequest;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyRollIssue.V1_GreyRollIssueSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyRollIssue.V1_Issue_Purpose.V1_GreyRollIssuePurposeModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyRollReceiveRequest;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyRollSaveRequest;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.serviceInterface.RetrofitApiClient;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GrayProductionRepository {
    private static final String TAG = "GrayProductionRepositor";
    private static GrayProductionRepository grayProductionRepository;

    public  static  GrayProductionRepository getInstance() {
        if(grayProductionRepository == null) {
            grayProductionRepository = new GrayProductionRepository();
        }
        return grayProductionRepository;
    }

    private final ApiInterface apiInterface;
    public GrayProductionRepository() {
        apiInterface = RetrofitApiClient.createService(ApiInterface.class);
    }

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<V1_GreyRollIssueRequest> getGrayRollIssuesResponse(String barcode_no){
        isLoading.setValue(true);
        MutableLiveData<V1_GreyRollIssueRequest> grayRollIssuesResponse = new MutableLiveData<>();
        apiInterface.getGreyRollIssueByBarcodeModelClassCall(barcode_no).enqueue(new Callback<V1_GreyRollIssueRequest>() {
            @Override
            public void onResponse(@NonNull Call<V1_GreyRollIssueRequest> call,
                                   @NonNull Response<V1_GreyRollIssueRequest> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        grayRollIssuesResponse.setValue(response.body());
                    }else {
                        grayRollIssuesResponse.setValue(null);
                    }
                }else {
                    grayRollIssuesResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_GreyRollIssueRequest> call, @NonNull Throwable t) {
                grayRollIssuesResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return grayRollIssuesResponse;
    }

    public MutableLiveData<V1_GreyRollIssuePurposeModel> getGrayRollIssuesPurposeResponse(){
        isLoading.setValue(true);
        MutableLiveData<V1_GreyRollIssuePurposeModel> grayRollIssuesPurposeResponse = new MutableLiveData<>();
        apiInterface.getGreyRollIssuePurposeModelClassCall().enqueue(new Callback<V1_GreyRollIssuePurposeModel>() {
            @Override
            public void onResponse(@NonNull Call<V1_GreyRollIssuePurposeModel> call,
                                   @NonNull Response<V1_GreyRollIssuePurposeModel> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        grayRollIssuesPurposeResponse.setValue(response.body());
                    }else {
                        grayRollIssuesPurposeResponse.setValue(null);
                    }
                }else {
                    grayRollIssuesPurposeResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_GreyRollIssuePurposeModel> call, @NonNull Throwable t) {
                grayRollIssuesPurposeResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return grayRollIssuesPurposeResponse;
    }

    public MutableLiveData<V1_BundleWiseSewingInputClass> getGrayRollIssuesDefaultResponse(){
        isLoading.setValue(true);
        MutableLiveData<V1_BundleWiseSewingInputClass> grayRollIssuesDefaultResponse = new MutableLiveData<>();
        apiInterface.getBundkeWiseSewingInputClassCall().enqueue(new Callback<V1_BundleWiseSewingInputClass>() {
            @Override
            public void onResponse(@NonNull Call<V1_BundleWiseSewingInputClass> call,
                                   @NonNull Response<V1_BundleWiseSewingInputClass> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        grayRollIssuesDefaultResponse.setValue(response.body());
                    }else {
                        grayRollIssuesDefaultResponse.setValue(null);
                    }
                }else {
                    grayRollIssuesDefaultResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_BundleWiseSewingInputClass> call, @NonNull Throwable t) {
                grayRollIssuesDefaultResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return grayRollIssuesDefaultResponse;
    }

    public MutableLiveData<V1_GreyRollIssueSaveResponse> postGrayRollIssueResponse(RequestBody body){
        isLoading.setValue(true);
        MutableLiveData<V1_GreyRollIssueSaveResponse> grayRollIssueResponse = new MutableLiveData<>();
        apiInterface.saveGreyRollIssueCall(body).enqueue(new Callback<V1_GreyRollIssueSaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_GreyRollIssueSaveResponse> call,
                                   @NonNull Response<V1_GreyRollIssueSaveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        grayRollIssueResponse.setValue(response.body());
                    }else {
                        grayRollIssueResponse.setValue(null);
                    }
                }else {
                    grayRollIssueResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_GreyRollIssueSaveResponse> call, @NonNull Throwable t) {
                grayRollIssueResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return grayRollIssueResponse;
    }

    public MutableLiveData<V1_GreyRollReceiveRequest> getGrayRollReceiveByChallanResponse(String challanScan, String location){
        isLoading.setValue(true);
        MutableLiveData<V1_GreyRollReceiveRequest> grayRollReceiveByChallanResponse = new MutableLiveData<>();
        apiInterface.getGreyRollReceiveByChallanModelClassCall(challanScan, location).enqueue(new Callback<V1_GreyRollReceiveRequest>() {
            @Override
            public void onResponse(@NonNull Call<V1_GreyRollReceiveRequest> call,
                                   @NonNull Response<V1_GreyRollReceiveRequest> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        grayRollReceiveByChallanResponse.setValue(response.body());
                    }else {
                        grayRollReceiveByChallanResponse.setValue(null);
                    }
                }else {
                    grayRollReceiveByChallanResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_GreyRollReceiveRequest> call, @NonNull Throwable t) {
                grayRollReceiveByChallanResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return grayRollReceiveByChallanResponse;
    }

    public MutableLiveData<V1_GreyRollReceiveRequest> getGrayRollReceiveByBarcodeResponse(String barcode, String location){
        isLoading.setValue(true);
        MutableLiveData<V1_GreyRollReceiveRequest> grayRollReceiveByBarcodeResponse = new MutableLiveData<>();
        apiInterface.getGreyRollReceiveByBarcodeModelClassCall(barcode, location).enqueue(new Callback<V1_GreyRollReceiveRequest>() {
            @Override
            public void onResponse(@NonNull Call<V1_GreyRollReceiveRequest> call,
                                   @NonNull Response<V1_GreyRollReceiveRequest> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        grayRollReceiveByBarcodeResponse.setValue(response.body());
                    }else {
                        grayRollReceiveByBarcodeResponse.setValue(null);
                    }
                }else {
                    grayRollReceiveByBarcodeResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_GreyRollReceiveRequest> call, @NonNull Throwable t) {
                grayRollReceiveByBarcodeResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return grayRollReceiveByBarcodeResponse;
    }

    public MutableLiveData<V1_GreyRollSaveRequest> postGrayRollReceiveResponse(RequestBody body){
        isLoading.setValue(true);
        MutableLiveData<V1_GreyRollSaveRequest> grayRollReceiveResponse = new MutableLiveData<>();
        apiInterface.saveGreyRollReceiveCall(body).enqueue(new Callback<V1_GreyRollSaveRequest>() {
            @Override
            public void onResponse(@NonNull Call<V1_GreyRollSaveRequest> call,
                                   @NonNull Response<V1_GreyRollSaveRequest> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        grayRollReceiveResponse.setValue(response.body());
                    }else {
                        grayRollReceiveResponse.setValue(null);
                    }
                }else {
                    grayRollReceiveResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_GreyRollSaveRequest> call, @NonNull Throwable t) {
                grayRollReceiveResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return grayRollReceiveResponse;
    }
}
