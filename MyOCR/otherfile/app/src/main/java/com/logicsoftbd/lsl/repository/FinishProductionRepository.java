package com.logicsoftbd.lsl.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.logicsoftbd.lsl.data.network.model.CompactBatchScanResponse;
import com.logicsoftbd.lsl.data.network.model.CompactingDefaultResponse;
import com.logicsoftbd.lsl.data.network.model.FabricShade;
import com.logicsoftbd.lsl.data.network.model.FloorWiseMachineResponse;
import com.logicsoftbd.lsl.data.network.model.SlitteringSequzBarCodeResponse;
import com.logicsoftbd.lsl.data.network.model.SlitteringSequzCompanyWiseFloorResponse;
import com.logicsoftbd.lsl.data.network.model.SlitteringSequzSaveResponse;
import com.logicsoftbd.lsl.data.network.model.StenteringBatchScanResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_AOPBagKeepingResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_AOPBagKeepingSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_AOPDepartmentStoreResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_AOPDeptBagReceiveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_AOPDeptBagSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagDeliveryResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagDeliverySaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagEmptyReceiveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagEmptyReceiveSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagIssueResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagIssueSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagKeepingDataBySystemResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagKeepingResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagKeepingSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagPrintResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagReceiveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagReceiveSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagReturnResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagReturnSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BarcodeByBatchForQCResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BarcodeDetailsFromBatchFinishQCResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseCompanyToLocationClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseFloorWiseLineClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseLocationWiseFloorClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseSewingInputClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DataSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DyedAOPBagReceiveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DyeingProductionPDAResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DyeingProductionPDASaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FabricBagColorModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FabricFinishQCUpdateModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FinishLocationWiseFloorClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FinishingDataResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FinishingMachineModelResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FinishingQCModelResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FinishingQCSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FinishingSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GMTFinishReceiveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GMTFinishReceiveSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyStoreRejectBagReceiveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ShiftResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SpecialFinishSubProcessResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_transfer_out.V1GreyFabricTransferOutStoreListResponse;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.serviceInterface.RetrofitApiClient;

import java.io.IOException;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinishProductionRepository {
    private static final String TAG = "FinishProductionReposit";
    private static FinishProductionRepository finishProductionRepository;

    public static FinishProductionRepository getInstance() {
        if(finishProductionRepository == null) {
            finishProductionRepository = new FinishProductionRepository();
        }
        return finishProductionRepository;
    }

    private final ApiInterface apiInterface;
    public FinishProductionRepository() {
        apiInterface = RetrofitApiClient.createService(ApiInterface.class);
    }

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<CompactingDefaultResponse> getFinishProductionDefaultResponse(String entry_form_no){
        isLoading.setValue(true);
        MutableLiveData<CompactingDefaultResponse> finishProductionDefaultResponse = new MutableLiveData<>();
        apiInterface.compactingDefaultResponse(entry_form_no).enqueue(new Callback<CompactingDefaultResponse>() {
            @Override
            public void onResponse(@NonNull Call<CompactingDefaultResponse> call,
                                   @NonNull Response<CompactingDefaultResponse> response) {
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
            public void onFailure(@NonNull Call<CompactingDefaultResponse> call, @NonNull Throwable t) {
                finishProductionDefaultResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return finishProductionDefaultResponse;
    }

    public MutableLiveData<SlitteringSequzCompanyWiseFloorResponse> getFinishProductionCompanyWiseFloorResponse(String company_id){
        isLoading.setValue(true);
        MutableLiveData<SlitteringSequzCompanyWiseFloorResponse> finishProductionCompanyWiseFloor = new MutableLiveData<>();
        apiInterface.slitteringCompanyWiseFloorResponseCall(company_id).enqueue(new Callback<SlitteringSequzCompanyWiseFloorResponse>() {
            @Override
            public void onResponse(@NonNull Call<SlitteringSequzCompanyWiseFloorResponse> call,
                                   @NonNull Response<SlitteringSequzCompanyWiseFloorResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        finishProductionCompanyWiseFloor.setValue(response.body());
                    }else {
                        finishProductionCompanyWiseFloor.setValue(null);
                    }
                }else {
                    finishProductionCompanyWiseFloor.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SlitteringSequzCompanyWiseFloorResponse> call, @NonNull Throwable t) {
                finishProductionCompanyWiseFloor.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return finishProductionCompanyWiseFloor;
    }

    public MutableLiveData<FloorWiseMachineResponse> getFinishProductionCompanyWiseFloorMachineResponse(String company_floor_id){
        isLoading.setValue(true);
        MutableLiveData<FloorWiseMachineResponse> finishProductionCompanyWiseFloorMachineResponse = new MutableLiveData<>();
        apiInterface.slitteringCompanyFloorMachineResponseCall(company_floor_id).enqueue(new Callback<FloorWiseMachineResponse>() {
            @Override
            public void onResponse(@NonNull Call<FloorWiseMachineResponse> call,
                                   @NonNull Response<FloorWiseMachineResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        finishProductionCompanyWiseFloorMachineResponse.setValue(response.body());
                    }else {
                        finishProductionCompanyWiseFloorMachineResponse.setValue(null);
                    }
                }else {
                    finishProductionCompanyWiseFloorMachineResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<FloorWiseMachineResponse> call, @NonNull Throwable t) {
                finishProductionCompanyWiseFloorMachineResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return finishProductionCompanyWiseFloorMachineResponse;
    }

    public MutableLiveData<CompactBatchScanResponse> getFinishProductionCompactingBatchScanResponse(String barcode_no, String entry_form_no){
        isLoading.setValue(true);
        MutableLiveData<CompactBatchScanResponse> finishProductionCompactingBatchScanResponse = new MutableLiveData<>();
        apiInterface.compactingBatchScanResponse("0", barcode_no, entry_form_no).enqueue(new Callback<CompactBatchScanResponse>() {
            @Override
            public void onResponse(@NonNull Call<CompactBatchScanResponse> call,
                                   @NonNull Response<CompactBatchScanResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        finishProductionCompactingBatchScanResponse.setValue(response.body());
                    }else {
                        finishProductionCompactingBatchScanResponse.setValue(null);
                    }
                }else {
                    finishProductionCompactingBatchScanResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CompactBatchScanResponse> call, @NonNull Throwable t) {
                finishProductionCompactingBatchScanResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return finishProductionCompactingBatchScanResponse;
    }

    public MutableLiveData<StenteringBatchScanResponse> getFinishProductionStenteringBatchScanResponse(String batch, String entry_form_no){
        isLoading.setValue(true);
        MutableLiveData<StenteringBatchScanResponse> finishProductionStenteringBatchScanResponse = new MutableLiveData<>();
        apiInterface.stenteringBarcodeResponseCall("0", batch, entry_form_no).enqueue(new Callback<StenteringBatchScanResponse>() {
            @Override
            public void onResponse(@NonNull Call<StenteringBatchScanResponse> call,
                                   @NonNull Response<StenteringBatchScanResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        finishProductionStenteringBatchScanResponse.setValue(response.body());
                    }else {
                        finishProductionStenteringBatchScanResponse.setValue(null);
                    }
                }else {
                    finishProductionStenteringBatchScanResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<StenteringBatchScanResponse> call, @NonNull Throwable t) {
                finishProductionStenteringBatchScanResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return finishProductionStenteringBatchScanResponse;
    }

    public MutableLiveData<SlitteringSequzBarCodeResponse> getFinishProductionSlittingBatchScanResponse(String batch, String entry_form_no){
        isLoading.setValue(true);
        MutableLiveData<SlitteringSequzBarCodeResponse> finishProductionSlittingBatchScanResponse = new MutableLiveData<>();
        apiInterface.slitteringSequzBarcodeResponseCall("0", batch, entry_form_no).enqueue(new Callback<SlitteringSequzBarCodeResponse>() {
            @Override
            public void onResponse(@NonNull Call<SlitteringSequzBarCodeResponse> call,
                                   @NonNull Response<SlitteringSequzBarCodeResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        finishProductionSlittingBatchScanResponse.setValue(response.body());
                    }else {
                        finishProductionSlittingBatchScanResponse.setValue(null);
                    }
                }else {
                    finishProductionSlittingBatchScanResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SlitteringSequzBarCodeResponse> call, @NonNull Throwable t) {
                finishProductionSlittingBatchScanResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return finishProductionSlittingBatchScanResponse;
    }

    public MutableLiveData<SlitteringSequzSaveResponse> postFinishProductionCompactingResponse(RequestBody body){
        isLoading.setValue(true);
        MutableLiveData<SlitteringSequzSaveResponse> finishProductionCompactingResponse = new MutableLiveData<>();
        apiInterface.saveUpdateSlittingSquzCall(body).enqueue(new Callback<SlitteringSequzSaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<SlitteringSequzSaveResponse> call,
                                   @NonNull Response<SlitteringSequzSaveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        finishProductionCompactingResponse.setValue(response.body());
                    }else {
                        finishProductionCompactingResponse.setValue(null);
                    }
                }else {
                    finishProductionCompactingResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SlitteringSequzSaveResponse> call, @NonNull Throwable t) {
                finishProductionCompactingResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return finishProductionCompactingResponse;
    }

    public MutableLiveData<V1_BarcodeByBatchForQCResponse> getBarcodeByBatchForQCResponse(String batch){
        isLoading.setValue(true);
        MutableLiveData<V1_BarcodeByBatchForQCResponse> barcodeByBatchForQCResponse = new MutableLiveData<>();
        apiInterface.getBarcodeByBatchForQCClassCall(batch).enqueue(new Callback<V1_BarcodeByBatchForQCResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_BarcodeByBatchForQCResponse> call,
                                   @NonNull Response<V1_BarcodeByBatchForQCResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        barcodeByBatchForQCResponse.setValue(response.body());
                    }else {
                        barcodeByBatchForQCResponse.setValue(null);
                    }
                }else {
                    barcodeByBatchForQCResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_BarcodeByBatchForQCResponse> call, @NonNull Throwable t) {
                barcodeByBatchForQCResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return barcodeByBatchForQCResponse;
    }

    public MutableLiveData<V1_BarcodeDetailsFromBatchFinishQCResponse> getBarcodeDetailsFromBatchForQCResponse(String barcode_no){
        isLoading.setValue(true);
        MutableLiveData<V1_BarcodeDetailsFromBatchFinishQCResponse> barcodeDetailsFromBatchForQCResponse = new MutableLiveData<>();
        apiInterface.getBarcodeDetailsFromBatchForQCClassCall(barcode_no).enqueue(new Callback<V1_BarcodeDetailsFromBatchFinishQCResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_BarcodeDetailsFromBatchFinishQCResponse> call,
                                   @NonNull Response<V1_BarcodeDetailsFromBatchFinishQCResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        barcodeDetailsFromBatchForQCResponse.setValue(response.body());
                    }else {
                        barcodeDetailsFromBatchForQCResponse.setValue(null);
                    }
                }else {
                    barcodeDetailsFromBatchForQCResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_BarcodeDetailsFromBatchFinishQCResponse> call, @NonNull Throwable t) {
                barcodeDetailsFromBatchForQCResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return barcodeDetailsFromBatchForQCResponse;
    }

    public MutableLiveData<V1_DataSaveResponse> postFinishFabricQCV2esponse(RequestBody body){
        isLoading.setValue(true);
        MutableLiveData<V1_DataSaveResponse> finishFabricQCV2esponse = new MutableLiveData<>();
        apiInterface.saveUpdateFinishFabricQCV2Call(body).enqueue(new Callback<V1_DataSaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_DataSaveResponse> call,
                                   @NonNull Response<V1_DataSaveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        finishFabricQCV2esponse.setValue(response.body());
                    }else {
                        finishFabricQCV2esponse.setValue(null);
                    }
                }else {
                    finishFabricQCV2esponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_DataSaveResponse> call, @NonNull Throwable t) {
                finishFabricQCV2esponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return finishFabricQCV2esponse;
    }

    public MutableLiveData<V1_ShiftResponse> getShiftResponse(){
        isLoading.setValue(true);
        MutableLiveData<V1_ShiftResponse> shiftResponse = new MutableLiveData<>();
        apiInterface.getShiftClassCall().enqueue(new Callback<V1_ShiftResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_ShiftResponse> call,
                                   @NonNull Response<V1_ShiftResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        shiftResponse.setValue(response.body());
                    }else {
                        shiftResponse.setValue(null);
                    }
                }else {
                    shiftResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_ShiftResponse> call, @NonNull Throwable t) {
                shiftResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return shiftResponse;
    }

    public MutableLiveData<FabricShade> getShadeResponse(){
        isLoading.setValue(true);
        MutableLiveData<FabricShade> shadeResponse = new MutableLiveData<>();
        apiInterface.getShadeClassCall().enqueue(new Callback<FabricShade>() {
            @Override
            public void onResponse(@NonNull Call<FabricShade> call,
                                   @NonNull Response<FabricShade> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        shadeResponse.setValue(response.body());
                    }else {
                        shadeResponse.setValue(null);
                    }
                }else {
                    shadeResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<FabricShade> call, @NonNull Throwable t) {
                shadeResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return shadeResponse;
    }

    public MutableLiveData<V1_BagKeepingResponse> getBagKeepingResponse(String batchNo, String bagNo, String selectedColorId){
        isLoading.setValue(true);
        MutableLiveData<V1_BagKeepingResponse> bagKeepingResponse = new MutableLiveData<>();
        apiInterface.getBagKeepingClassCall(batchNo, bagNo, selectedColorId).enqueue(new Callback<V1_BagKeepingResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_BagKeepingResponse> call,
                                   @NonNull Response<V1_BagKeepingResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        bagKeepingResponse.setValue(response.body());
                    }else {
                        bagKeepingResponse.setValue(null);
                    }
                }else {
                    bagKeepingResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_BagKeepingResponse> call, @NonNull Throwable t) {
                bagKeepingResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return bagKeepingResponse;
    }

    public MutableLiveData<V1_BagPrintResponse> getBagKeepingPrintResponse(String batchNo, String bagNo){
        isLoading.setValue(true);
        MutableLiveData<V1_BagPrintResponse> bagKeepingResponse = new MutableLiveData<>();
        apiInterface.getBagKeepingPrintClassCall(batchNo, bagNo).enqueue(new Callback<V1_BagPrintResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_BagPrintResponse> call,
                                   @NonNull Response<V1_BagPrintResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        bagKeepingResponse.setValue(response.body());
                    }else {
                        bagKeepingResponse.setValue(null);
                    }
                }else {
                    bagKeepingResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_BagPrintResponse> call, @NonNull Throwable t) {
                bagKeepingResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return bagKeepingResponse;
    }

    public MutableLiveData<V1_AOPBagKeepingResponse> getAOPBagKeepingResponse(String searchType, String searchQuery, String bagNo, String selectedColorId){
        isLoading.setValue(true);
        MutableLiveData<V1_AOPBagKeepingResponse> bagKeepingResponse = new MutableLiveData<>();
        apiInterface.getAOPBagKeepingClassCall(searchType, searchQuery, bagNo, selectedColorId).enqueue(new Callback<V1_AOPBagKeepingResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_AOPBagKeepingResponse> call,
                                   @NonNull Response<V1_AOPBagKeepingResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        bagKeepingResponse.setValue(response.body());
                    }else {
                        bagKeepingResponse.setValue(null);
                    }
                }else {
                    bagKeepingResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_AOPBagKeepingResponse> call, @NonNull Throwable t) {
                bagKeepingResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return bagKeepingResponse;
    }


    public MutableLiveData<V1_BagKeepingSaveResponse> postBagKeeping(RequestBody body){
        isLoading.setValue(true);
        MutableLiveData<V1_BagKeepingSaveResponse> postBagKeepingResponse = new MutableLiveData<>();
        apiInterface.saveUpdateBagKeepingCall(body).enqueue(new Callback<V1_BagKeepingSaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_BagKeepingSaveResponse> call,
                                   @NonNull Response<V1_BagKeepingSaveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        postBagKeepingResponse.setValue(response.body());
                    }else {
                        postBagKeepingResponse.setValue(null);
                    }
                }else {
                    postBagKeepingResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_BagKeepingSaveResponse> call, @NonNull Throwable t) {
                postBagKeepingResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return postBagKeepingResponse;
    }

    public MutableLiveData<V1_AOPBagKeepingSaveResponse> postAOPBagKeeping(RequestBody body){
        isLoading.setValue(true);
        MutableLiveData<V1_AOPBagKeepingSaveResponse> postBagKeepingResponse = new MutableLiveData<>();
        apiInterface.saveUpdateAOPBagKeepingCall(body).enqueue(new Callback<V1_AOPBagKeepingSaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_AOPBagKeepingSaveResponse> call,
                                   @NonNull Response<V1_AOPBagKeepingSaveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        postBagKeepingResponse.setValue(response.body());
                    }else {
                        postBagKeepingResponse.setValue(null);
                    }
                }else {
                    postBagKeepingResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_AOPBagKeepingSaveResponse> call, @NonNull Throwable t) {
                postBagKeepingResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return postBagKeepingResponse;
    }

    public MutableLiveData<V1_BagKeepingSaveResponse> postBagKeepingQC(RequestBody body){
        isLoading.setValue(true);
        MutableLiveData<V1_BagKeepingSaveResponse> postBagKeepingResponse = new MutableLiveData<>();
        apiInterface.saveUpdateBagKeepingQCCall(body).enqueue(new Callback<V1_BagKeepingSaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_BagKeepingSaveResponse> call,
                                   @NonNull Response<V1_BagKeepingSaveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        postBagKeepingResponse.setValue(response.body());
                    }else {
                        postBagKeepingResponse.setValue(null);
                    }
                }else {
                    postBagKeepingResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_BagKeepingSaveResponse> call, @NonNull Throwable t) {
                postBagKeepingResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return postBagKeepingResponse;
    }

    public MutableLiveData<V1_AOPDeptBagSaveResponse> postAOPDeptReceive(RequestBody body){
        isLoading.setValue(true);
        MutableLiveData<V1_AOPDeptBagSaveResponse> postBagKeepingResponse = new MutableLiveData<>();
        apiInterface.saveUpdateAopDeptRcvCall(body).enqueue(new Callback<V1_AOPDeptBagSaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_AOPDeptBagSaveResponse> call,
                                   @NonNull Response<V1_AOPDeptBagSaveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        postBagKeepingResponse.setValue(response.body());
                    }else {
                        postBagKeepingResponse.setValue(null);
                    }
                }else {
                    postBagKeepingResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_AOPDeptBagSaveResponse> call, @NonNull Throwable t) {
                postBagKeepingResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return postBagKeepingResponse;
    }

    public MutableLiveData<V1_AOPDeptBagSaveResponse> postAOPDyedReceive(RequestBody body){
        isLoading.setValue(true);
        MutableLiveData<V1_AOPDeptBagSaveResponse> postBagKeepingResponse = new MutableLiveData<>();
        apiInterface.saveUpdateAopDyedRcvCall(body).enqueue(new Callback<V1_AOPDeptBagSaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_AOPDeptBagSaveResponse> call,
                                   @NonNull Response<V1_AOPDeptBagSaveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        postBagKeepingResponse.setValue(response.body());
                    }else {
                        postBagKeepingResponse.setValue(null);
                    }
                }else {
                    postBagKeepingResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_AOPDeptBagSaveResponse> call, @NonNull Throwable t) {
                postBagKeepingResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return postBagKeepingResponse;
    }

    public MutableLiveData<V1_AOPDeptBagSaveResponse> postGreyStoreRejectBagResponse(RequestBody body){
        isLoading.setValue(true);
        MutableLiveData<V1_AOPDeptBagSaveResponse> postBagKeepingResponse = new MutableLiveData<>();
        apiInterface.saveUpdateGreyStoreRejectBagCall(body).enqueue(new Callback<V1_AOPDeptBagSaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_AOPDeptBagSaveResponse> call,
                                   @NonNull Response<V1_AOPDeptBagSaveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        postBagKeepingResponse.setValue(response.body());
                    }else {
                        postBagKeepingResponse.setValue(null);
                    }
                }else {
                    postBagKeepingResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_AOPDeptBagSaveResponse> call, @NonNull Throwable t) {
                postBagKeepingResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return postBagKeepingResponse;
    }

    public MutableLiveData<V1_BagDeliveryResponse> getBagDeliveryResponse(String bagNo){
        isLoading.setValue(true);
        MutableLiveData<V1_BagDeliveryResponse> bagDeliveryResponse = new MutableLiveData<>();
        apiInterface.getBagDeliveryClassCall( bagNo).enqueue(new Callback<V1_BagDeliveryResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_BagDeliveryResponse> call,
                                   @NonNull Response<V1_BagDeliveryResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        bagDeliveryResponse.setValue(response.body());
                    }else {
                        bagDeliveryResponse.setValue(null);
                    }
                }else {
                    bagDeliveryResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_BagDeliveryResponse> call, @NonNull Throwable t) {
                bagDeliveryResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return bagDeliveryResponse;
    }

    public MutableLiveData<V1_BagEmptyReceiveResponse> getBagEmptyReceiveResponse(String bagNo, String batchNo, Integer selectedColorId, Integer selectedCategoryId){
        isLoading.setValue(true);
        MutableLiveData<V1_BagEmptyReceiveResponse> bagEmptyReceiveResponse = new MutableLiveData<>();
        apiInterface.getBagEmptyReceiveClassCall( bagNo, selectedColorId, selectedCategoryId).enqueue(new Callback<V1_BagEmptyReceiveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_BagEmptyReceiveResponse> call,
                                   @NonNull Response<V1_BagEmptyReceiveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        bagEmptyReceiveResponse.setValue(response.body());
                    }else {
                        bagEmptyReceiveResponse.setValue(null);
                    }
                }else {
                    bagEmptyReceiveResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_BagEmptyReceiveResponse> call, @NonNull Throwable t) {
                bagEmptyReceiveResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return bagEmptyReceiveResponse;
    }

    public MutableLiveData<V1GreyFabricTransferOutStoreListResponse> getStoreResponse(String userId, String itemCategory, String companyId){
        isLoading.setValue(true);
        MutableLiveData<V1GreyFabricTransferOutStoreListResponse> storeResponse = new MutableLiveData<>();
        apiInterface.store_name_list_for_grey_fabric_transfer_out(userId, itemCategory, companyId).enqueue(new Callback<V1GreyFabricTransferOutStoreListResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1GreyFabricTransferOutStoreListResponse> call,
                                   @NonNull Response<V1GreyFabricTransferOutStoreListResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        storeResponse.setValue(response.body());
                    }else {
                        storeResponse.setValue(null);
                    }
                }else {
                    storeResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1GreyFabricTransferOutStoreListResponse> call, @NonNull Throwable t) {
                storeResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return storeResponse;
    }

    public MutableLiveData<V1_BagDeliverySaveResponse> postBagDelivery(RequestBody body){
        isLoading.setValue(true);
        MutableLiveData<V1_BagDeliverySaveResponse> postBagDeliveryResponse = new MutableLiveData<>();
        apiInterface.saveUpdateBagDeliveryCall(body).enqueue(new Callback<V1_BagDeliverySaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_BagDeliverySaveResponse> call,
                                   @NonNull Response<V1_BagDeliverySaveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        postBagDeliveryResponse.setValue(response.body());
                    }else {
                        postBagDeliveryResponse.setValue(null);
                    }
                }else {
                    postBagDeliveryResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_BagDeliverySaveResponse> call, @NonNull Throwable t) {
                postBagDeliveryResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return postBagDeliveryResponse;
    }

    public MutableLiveData<V1_BagEmptyReceiveSaveResponse> postBagEmptyReceive(RequestBody body){
        isLoading.setValue(true);
        MutableLiveData<V1_BagEmptyReceiveSaveResponse> postBagEmptyReceiveResponse = new MutableLiveData<>();
        apiInterface.saveUpdateBagEmptyReceiveCall(body).enqueue(new Callback<V1_BagEmptyReceiveSaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_BagEmptyReceiveSaveResponse> call,
                                   @NonNull Response<V1_BagEmptyReceiveSaveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        postBagEmptyReceiveResponse.setValue(response.body());
                    }else {
                        postBagEmptyReceiveResponse.setValue(null);
                    }
                }else {
                    postBagEmptyReceiveResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_BagEmptyReceiveSaveResponse> call, @NonNull Throwable t) {
                postBagEmptyReceiveResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return postBagEmptyReceiveResponse;
    }

    public MutableLiveData<V1_BagReceiveResponse> getBagReceiveResponse(String bagNo, String rackId, Integer colorId, Integer selectedCategoryId){
        isLoading.setValue(true);
        MutableLiveData<V1_BagReceiveResponse> bagReceiveResponse = new MutableLiveData<>();
        apiInterface.getBagReceiveClassCall(bagNo, rackId, colorId, selectedCategoryId).enqueue(new Callback<V1_BagReceiveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_BagReceiveResponse> call,
                                   @NonNull Response<V1_BagReceiveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        bagReceiveResponse.setValue(response.body());
                    }else {
                        bagReceiveResponse.setValue(null);
                    }
                }else {
                    bagReceiveResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_BagReceiveResponse> call, @NonNull Throwable t) {
                bagReceiveResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return bagReceiveResponse;
    }

    public MutableLiveData<V1_BagReceiveResponse> getBagChallanReceiveResponse(String challan, String rackId, Integer selectedCategoryId){
        isLoading.setValue(true);
        MutableLiveData<V1_BagReceiveResponse> bagChallanReceiveResponse = new MutableLiveData<>();
        apiInterface.getBagChallanReceiveClassCall(challan, rackId, selectedCategoryId).enqueue(new Callback<V1_BagReceiveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_BagReceiveResponse> call,
                                   @NonNull Response<V1_BagReceiveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        bagChallanReceiveResponse.setValue(response.body());
                    }else {
                        bagChallanReceiveResponse.setValue(null);
                    }
                }else {
                    bagChallanReceiveResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_BagReceiveResponse> call, @NonNull Throwable t) {
                bagChallanReceiveResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return bagChallanReceiveResponse;
    }

    public MutableLiveData<V1_BagReceiveSaveResponse> postBagReceiveResponse(RequestBody body){
        isLoading.setValue(true);
        MutableLiveData<V1_BagReceiveSaveResponse> bagReceiveResponse = new MutableLiveData<>();
        apiInterface.saveUpdateBagReceiveCall(body).enqueue(new Callback<V1_BagReceiveSaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_BagReceiveSaveResponse> call,
                                   @NonNull Response<V1_BagReceiveSaveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        bagReceiveResponse.setValue(response.body());
                    }else {
                        bagReceiveResponse.setValue(null);
                    }
                }else {
                    bagReceiveResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_BagReceiveSaveResponse> call, @NonNull Throwable t) {
                bagReceiveResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return bagReceiveResponse;
    }

    public MutableLiveData<V1_BagIssueResponse> getBagIssueResponse(String bagScan, String rackId, Integer selectedColorId, Integer selectedCategoryId){
        isLoading.setValue(true);
        MutableLiveData<V1_BagIssueResponse> bagIssueResponse = new MutableLiveData<>();
        apiInterface.getBagIssueClassCall(bagScan, rackId, String.valueOf(selectedColorId), selectedCategoryId).enqueue(new Callback<V1_BagIssueResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_BagIssueResponse> call,
                                   @NonNull Response<V1_BagIssueResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        bagIssueResponse.setValue(response.body());
                    }else {
                        bagIssueResponse.setValue(null);
                    }
                }else {
                    bagIssueResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_BagIssueResponse> call, @NonNull Throwable t) {
                bagIssueResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return bagIssueResponse;
    }

    public MutableLiveData<V1_BagIssueResponse> getBagIssueByChallanResponse(String challan, String rackId, Integer selectedColorId, Integer selectedCategoryId){
        isLoading.setValue(true);
        MutableLiveData<V1_BagIssueResponse> bagIssueResponse = new MutableLiveData<>();
        apiInterface.getBagIssueByChallanClassCall(challan, rackId, String.valueOf(selectedColorId), selectedCategoryId).enqueue(new Callback<V1_BagIssueResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_BagIssueResponse> call,
                                   @NonNull Response<V1_BagIssueResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        bagIssueResponse.setValue(response.body());
                    }else {
                        bagIssueResponse.setValue(null);
                    }
                }else {
                    bagIssueResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_BagIssueResponse> call, @NonNull Throwable t) {
                bagIssueResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return bagIssueResponse;
    }

    public MutableLiveData<V1_BagIssueSaveResponse> postBagIssueResponse(RequestBody body){
        isLoading.setValue(true);
        MutableLiveData<V1_BagIssueSaveResponse> bagIssueResponse = new MutableLiveData<>();
        apiInterface.saveUpdateBagIssueCall(body).enqueue(new Callback<V1_BagIssueSaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_BagIssueSaveResponse> call,
                                   @NonNull Response<V1_BagIssueSaveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        bagIssueResponse.setValue(response.body());
                    }else {
                        bagIssueResponse.setValue(null);
                    }
                }else {
                    bagIssueResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_BagIssueSaveResponse> call, @NonNull Throwable t) {
                bagIssueResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return bagIssueResponse;
    }

    public MutableLiveData<V1_BagReturnResponse> getBagReturnResponse(String bagNo, Integer selectedColorId, Integer selectedCategoryId){
        isLoading.setValue(true);
        MutableLiveData<V1_BagReturnResponse> bagReturnResponse = new MutableLiveData<>();
        apiInterface.getBagReturnClassCall(bagNo, selectedColorId, selectedCategoryId).enqueue(new Callback<V1_BagReturnResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_BagReturnResponse> call,
                                   @NonNull Response<V1_BagReturnResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        bagReturnResponse.setValue(response.body());
                    }else {
                        bagReturnResponse.setValue(null);
                    }
                }else {
                    bagReturnResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_BagReturnResponse> call, @NonNull Throwable t) {
                bagReturnResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return bagReturnResponse;
    }

    public MutableLiveData<V1_BagReturnSaveResponse> postBagReturnResponse(RequestBody body){
        isLoading.setValue(true);
        MutableLiveData<V1_BagReturnSaveResponse> bagReturnResponse = new MutableLiveData<>();
        apiInterface.saveUpdateBagReturnCall(body).enqueue(new Callback<V1_BagReturnSaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_BagReturnSaveResponse> call,
                                   @NonNull Response<V1_BagReturnSaveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        bagReturnResponse.setValue(response.body());
                    }else {
                        bagReturnResponse.setValue(null);
                    }
                }else {
                    bagReturnResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_BagReturnSaveResponse> call, @NonNull Throwable t) {
                bagReturnResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return bagReturnResponse;
    }

    public MutableLiveData<V1_BundleWiseSewingInputClass> getCompanyAndSourceResponse(){
        isLoading.setValue(true);
        MutableLiveData<V1_BundleWiseSewingInputClass> apiResponse = new MutableLiveData<>();
        apiInterface.getBundkeWiseSewingInputClassCall().enqueue(new Callback<V1_BundleWiseSewingInputClass>() {
            @Override
            public void onResponse(@NonNull Call<V1_BundleWiseSewingInputClass> call,
                                   @NonNull Response<V1_BundleWiseSewingInputClass> response) {
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
            public void onFailure(@NonNull Call<V1_BundleWiseSewingInputClass> call, @NonNull Throwable t) {
                apiResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return apiResponse;
    }

    public MutableLiveData<V1_BundleWiseCompanyToLocationClass> getCompanyWiseLocationResponse(Integer companyId){
        isLoading.setValue(true);
        MutableLiveData<V1_BundleWiseCompanyToLocationClass> apiResponse = new MutableLiveData<>();
        apiInterface.getBundleWiseCompanyToLocationClassCall(companyId).enqueue(new Callback<V1_BundleWiseCompanyToLocationClass>() {
            @Override
            public void onResponse(@NonNull Call<V1_BundleWiseCompanyToLocationClass> call,
                                   @NonNull Response<V1_BundleWiseCompanyToLocationClass> response) {
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
            public void onFailure(@NonNull Call<V1_BundleWiseCompanyToLocationClass> call, @NonNull Throwable t) {
                apiResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return apiResponse;
    }

    public MutableLiveData<V1_BundleWiseLocationWiseFloorClass> getLocationWiseFloorResponse(Integer location_id){
        isLoading.setValue(true);
        MutableLiveData<V1_BundleWiseLocationWiseFloorClass> apiResponse = new MutableLiveData<>();
        apiInterface.getBundleWiseLocationWiseFloorClassCall(location_id).enqueue(new Callback<V1_BundleWiseLocationWiseFloorClass>() {
            @Override
            public void onResponse(@NonNull Call<V1_BundleWiseLocationWiseFloorClass> call,
                                   @NonNull Response<V1_BundleWiseLocationWiseFloorClass> response) {
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
            public void onFailure(@NonNull Call<V1_BundleWiseLocationWiseFloorClass> call, @NonNull Throwable t) {
                apiResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return apiResponse;
    }

    public MutableLiveData<V1_FinishLocationWiseFloorClass> getFinishLocationWiseFloorResponse(Integer location_id, Integer process){
        isLoading.setValue(true);
        MutableLiveData<V1_FinishLocationWiseFloorClass> apiResponse = new MutableLiveData<>();
        apiInterface.getFinishWiseLocationWiseFloorClassCall(location_id, process).enqueue(new Callback<V1_FinishLocationWiseFloorClass>() {
            @Override
            public void onResponse(@NonNull Call<V1_FinishLocationWiseFloorClass> call,
                                   @NonNull Response<V1_FinishLocationWiseFloorClass> response) {
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
            public void onFailure(@NonNull Call<V1_FinishLocationWiseFloorClass> call, @NonNull Throwable t) {
                apiResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return apiResponse;
    }

    public MutableLiveData<V1_BundleWiseFloorWiseLineClass> getFloorWiseLineResponse(Integer company_id, Integer location_id, Integer floor_id, String issue_date){
        isLoading.setValue(true);
        MutableLiveData<V1_BundleWiseFloorWiseLineClass> apiResponse = new MutableLiveData<>();
        apiInterface.getBundleWiseLocationWiseFloorClassCall(company_id, location_id, floor_id, issue_date).enqueue(new Callback<V1_BundleWiseFloorWiseLineClass>() {
            @Override
            public void onResponse(@NonNull Call<V1_BundleWiseFloorWiseLineClass> call,
                                   @NonNull Response<V1_BundleWiseFloorWiseLineClass> response) {
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
            public void onFailure(@NonNull Call<V1_BundleWiseFloorWiseLineClass> call, @NonNull Throwable t) {
                apiResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return apiResponse;
    }

    public MutableLiveData<V1_GMTFinishReceiveResponse> getGMTFinishReceiveBarcodeResponse(String barcode){
        isLoading.setValue(true);
        MutableLiveData<V1_GMTFinishReceiveResponse> apiResponse = new MutableLiveData<>();
        apiInterface.getGmrFinishReceiveClassCall(barcode).enqueue(new Callback<V1_GMTFinishReceiveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_GMTFinishReceiveResponse> call,
                                   @NonNull Response<V1_GMTFinishReceiveResponse> response) {
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
            public void onFailure(@NonNull Call<V1_GMTFinishReceiveResponse> call, @NonNull Throwable t) {
                apiResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return apiResponse;
    }

    public MutableLiveData<V1_GMTFinishReceiveSaveResponse> postGmtFinishReceiveResponse(RequestBody body){
        isLoading.setValue(true);
        MutableLiveData<V1_GMTFinishReceiveSaveResponse> apiResponse = new MutableLiveData<>();
        apiInterface.saveGmtFinishReceiveCall(body).enqueue(new Callback<V1_GMTFinishReceiveSaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_GMTFinishReceiveSaveResponse> call,
                                   @NonNull Response<V1_GMTFinishReceiveSaveResponse> response) {
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
            public void onFailure(@NonNull Call<V1_GMTFinishReceiveSaveResponse> call, @NonNull Throwable t) {
                apiResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return apiResponse;
    }

    public MutableLiveData<V1_BagKeepingDataBySystemResponse> getBagKeepingBySystemResponse(String SystemNo, String bagNo, Integer selectedColorId){
        isLoading.setValue(true);
        MutableLiveData<V1_BagKeepingDataBySystemResponse> bagKeepingResponse = new MutableLiveData<>();
        apiInterface.getBagKeepingBySystemClassCall(SystemNo, bagNo, selectedColorId).enqueue(new Callback<V1_BagKeepingDataBySystemResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_BagKeepingDataBySystemResponse> call,
                                   @NonNull Response<V1_BagKeepingDataBySystemResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        bagKeepingResponse.setValue(response.body());
                    }else {
                        bagKeepingResponse.setValue(null);
                    }
                }else {
                    bagKeepingResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_BagKeepingDataBySystemResponse> call, @NonNull Throwable t) {
                bagKeepingResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return bagKeepingResponse;
    }

    public MutableLiveData<V1_AOPDeptBagReceiveResponse> getAOPDeptBagReceiveResponse(String bagNo, Integer selectedColorId){
        isLoading.setValue(true);
        MutableLiveData<V1_AOPDeptBagReceiveResponse> bagKeepingResponse = new MutableLiveData<>();
        apiInterface.getAOPDeptBagReceiveClassCall( bagNo, selectedColorId).enqueue(new Callback<V1_AOPDeptBagReceiveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_AOPDeptBagReceiveResponse> call,
                                   @NonNull Response<V1_AOPDeptBagReceiveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        bagKeepingResponse.setValue(response.body());
                    }else {
                        bagKeepingResponse.setValue(null);
                    }
                }else {
                    bagKeepingResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_AOPDeptBagReceiveResponse> call, @NonNull Throwable t) {
                bagKeepingResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return bagKeepingResponse;
    }

    public MutableLiveData<V1_AOPDeptBagReceiveResponse> getAOPDeptBagReceiveBySystemNoResponse(String SystemNo){
        isLoading.setValue(true);
        MutableLiveData<V1_AOPDeptBagReceiveResponse> bagKeepingResponse = new MutableLiveData<>();
        apiInterface.getAOPDeptBagReceiveBySystemNoClassCall(SystemNo).enqueue(new Callback<V1_AOPDeptBagReceiveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_AOPDeptBagReceiveResponse> call,
                                   @NonNull Response<V1_AOPDeptBagReceiveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        bagKeepingResponse.setValue(response.body());
                    }else {
                        bagKeepingResponse.setValue(null);
                    }
                }else {
                    bagKeepingResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_AOPDeptBagReceiveResponse> call, @NonNull Throwable t) {
                bagKeepingResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return bagKeepingResponse;
    }

    public MutableLiveData<V1_DyedAOPBagReceiveResponse> getAOPDyedDeptBagReceiveResponse(String SystemNo, String bagNo, Integer selectedColorId){
        isLoading.setValue(true);
        MutableLiveData<V1_DyedAOPBagReceiveResponse> bagKeepingResponse = new MutableLiveData<>();
        apiInterface.getAOPDyedDeptBagReceiveClassCall(SystemNo, bagNo, selectedColorId).enqueue(new Callback<V1_DyedAOPBagReceiveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_DyedAOPBagReceiveResponse> call,
                                   @NonNull Response<V1_DyedAOPBagReceiveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        bagKeepingResponse.setValue(response.body());
                    }else {
                        bagKeepingResponse.setValue(null);
                    }
                }else {
                    bagKeepingResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_DyedAOPBagReceiveResponse> call, @NonNull Throwable t) {
                bagKeepingResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return bagKeepingResponse;
    }

    public MutableLiveData<V1_GreyStoreRejectBagReceiveResponse> getGreyStoreRejectBagResponse(String bagNo, Integer selectedColorId){
        isLoading.setValue(true);
        MutableLiveData<V1_GreyStoreRejectBagReceiveResponse> bagKeepingResponse = new MutableLiveData<>();
        apiInterface.getGreyStoreRejectBagClassCall(bagNo, selectedColorId).enqueue(new Callback<V1_GreyStoreRejectBagReceiveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_GreyStoreRejectBagReceiveResponse> call,
                                   @NonNull Response<V1_GreyStoreRejectBagReceiveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        bagKeepingResponse.setValue(response.body());
                    }else {
                        bagKeepingResponse.setValue(null);
                    }
                }else {
                    bagKeepingResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_GreyStoreRejectBagReceiveResponse> call, @NonNull Throwable t) {
                bagKeepingResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return bagKeepingResponse;
    }

    public MutableLiveData<V1_GreyStoreRejectBagReceiveResponse> getGreyStoreRejectBagBySystemNoResponse(String system_no){
        isLoading.setValue(true);
        MutableLiveData<V1_GreyStoreRejectBagReceiveResponse> bagKeepingResponse = new MutableLiveData<>();
        apiInterface.getGreyStoreRejectBagBySystemNoClassCall(system_no).enqueue(new Callback<V1_GreyStoreRejectBagReceiveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_GreyStoreRejectBagReceiveResponse> call,
                                   @NonNull Response<V1_GreyStoreRejectBagReceiveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        bagKeepingResponse.setValue(response.body());
                    }else {
                        bagKeepingResponse.setValue(null);
                    }
                }else {
                    bagKeepingResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_GreyStoreRejectBagReceiveResponse> call, @NonNull Throwable t) {
                bagKeepingResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return bagKeepingResponse;
    }

    public MutableLiveData<V1_AOPDepartmentStoreResponse> getAOPDeptStoreResponse(String user_id, String company_id, String item_category_id){
        isLoading.setValue(true);
        MutableLiveData<V1_AOPDepartmentStoreResponse> apiResponse = new MutableLiveData<>();
        apiInterface.getAOPDepartmentStoreModelCall(user_id, company_id, item_category_id).enqueue(new Callback<V1_AOPDepartmentStoreResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_AOPDepartmentStoreResponse> call,
                                   @NonNull Response<V1_AOPDepartmentStoreResponse> response) {
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
            public void onFailure(@NonNull Call<V1_AOPDepartmentStoreResponse> call, @NonNull Throwable t) {
                apiResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return apiResponse;
    }

    public MutableLiveData<V1_FabricBagColorModel> getFabricBagColorResponse(){
        isLoading.setValue(true);
        MutableLiveData<V1_FabricBagColorModel> apiResponse = new MutableLiveData<>();
        apiInterface.getFabricBagColorCall().enqueue(new Callback<V1_FabricBagColorModel>() {
            @Override
            public void onResponse(@NonNull Call<V1_FabricBagColorModel> call,
                                   @NonNull Response<V1_FabricBagColorModel> response) {
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
            public void onFailure(@NonNull Call<V1_FabricBagColorModel> call, @NonNull Throwable t) {
                apiResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return apiResponse;
    }

    public MutableLiveData<V1_FinishingQCModelResponse> getFinishingQCClassCall(String batchNo){
        isLoading.setValue(true);
        MutableLiveData<V1_FinishingQCModelResponse> apiResponse = new MutableLiveData<>();
        apiInterface.getFinishingQCClassCall(batchNo).enqueue(new Callback<V1_FinishingQCModelResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_FinishingQCModelResponse> call,
                                   @NonNull Response<V1_FinishingQCModelResponse> response) {
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
            public void onFailure(@NonNull Call<V1_FinishingQCModelResponse> call, @NonNull Throwable t) {
                apiResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage()+t);
            }
        });
        return apiResponse;
    }

    public MutableLiveData<V1_FabricFinishQCUpdateModel> getFinishingQCUpdateClassCall(String rollNo, String barcodeNo, String batchNo){
        isLoading.setValue(true);
        MutableLiveData<V1_FabricFinishQCUpdateModel> apiResponse = new MutableLiveData<>();
        apiInterface.getFinishingQCUpdateClassCall(rollNo, barcodeNo, batchNo).enqueue(new Callback<V1_FabricFinishQCUpdateModel>() {
            @Override
            public void onResponse(@NonNull Call<V1_FabricFinishQCUpdateModel> call,
                                   @NonNull Response<V1_FabricFinishQCUpdateModel> response) {
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
            public void onFailure(@NonNull Call<V1_FabricFinishQCUpdateModel> call, @NonNull Throwable t) {
                apiResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage()+t);
            }
        });
        return apiResponse;
    }

    public MutableLiveData<V1_FinishingMachineModelResponse> getFinishingMachineCall(){
        isLoading.setValue(true);
        MutableLiveData<V1_FinishingMachineModelResponse> apiResponse = new MutableLiveData<>();
        apiInterface.getFinishingMachineCall().enqueue(new Callback<V1_FinishingMachineModelResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_FinishingMachineModelResponse> call,
                                   @NonNull Response<V1_FinishingMachineModelResponse> response) {
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
            public void onFailure(@NonNull Call<V1_FinishingMachineModelResponse> call, @NonNull Throwable t) {
                apiResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage()+t);
            }
        });
        return apiResponse;
    }

    public MutableLiveData<V1_FinishingQCSaveResponse> postFinishQCResponse(RequestBody body){
        isLoading.setValue(true);
        MutableLiveData<V1_FinishingQCSaveResponse> apiResponse = new MutableLiveData<>();
        apiInterface.postFinishQCResponse(body).enqueue(new Callback<V1_FinishingQCSaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_FinishingQCSaveResponse> call,
                                   @NonNull Response<V1_FinishingQCSaveResponse> response) {
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
            public void onFailure(@NonNull Call<V1_FinishingQCSaveResponse> call, @NonNull Throwable t) {
                apiResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage()+t);
            }
        });
        return apiResponse;
    }

    public MutableLiveData<V1_FinishingDataResponse> getFinishingClassCall(String batchNo, String entryFormNo){
        isLoading.setValue(true);
        MutableLiveData<V1_FinishingDataResponse> apiResponse = new MutableLiveData<>();
        apiInterface.getFinishingClassCall(batchNo, entryFormNo).enqueue(new Callback<V1_FinishingDataResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_FinishingDataResponse> call,
                                   @NonNull Response<V1_FinishingDataResponse> response) {
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
            public void onFailure(@NonNull Call<V1_FinishingDataResponse> call, @NonNull Throwable t) {
                apiResponse.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage()+t);
            }
        });
        return apiResponse;
    }

    public MutableLiveData<V1_FinishingSaveResponse> postFinishingResponse(RequestBody body){
        isLoading.setValue(true);
        MutableLiveData<V1_FinishingSaveResponse> finishProductionCompactingResponse = new MutableLiveData<>();
        apiInterface.saveUpdateFinishingCall(body).enqueue(new Callback<V1_FinishingSaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_FinishingSaveResponse> call,
                                   @NonNull Response<V1_FinishingSaveResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    finishProductionCompactingResponse.setValue(response.body());
                    Log.d(TAG, "Response Code: ${response.code()}");
                    Log.d(TAG, "Response Body: ${response.body()}");

                } else {
                    try {
                        if(response.errorBody() != null)
                            Log.e(TAG, "Error Response Body: " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e(TAG, "Error parsing error body", e);
                    }
                    Log.e(TAG, "Response Error Code: " + response.code());
                    finishProductionCompactingResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_FinishingSaveResponse> call, @NonNull Throwable t) {
                finishProductionCompactingResponse.setValue(null);
                isLoading.setValue(false);
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        return finishProductionCompactingResponse;
    }


    public MutableLiveData<V1_SpecialFinishSubProcessResponse> getSpecialFinishSubProcessList() {
        MutableLiveData<V1_SpecialFinishSubProcessResponse> data = new MutableLiveData<>();
        apiInterface.getSpecialFinishSubProcessList().enqueue(new Callback<V1_SpecialFinishSubProcessResponse>() {
            @Override
            public void onResponse(Call<V1_SpecialFinishSubProcessResponse> call, Response<V1_SpecialFinishSubProcessResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                    Log.d(TAG, "onResponse: " + response.body());
                } else {
                    Log.e(TAG, "onResponse: Error: " + response.message());
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<V1_SpecialFinishSubProcessResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                data.setValue(null);
            }
        });
        return data;
    }

//    public MutableLiveData<V1_DyeingProductionPDASaveResponse> postDyeingProductionPDAResponse(RequestBody body){
//        isLoading.setValue(true);
//        MutableLiveData<V1_DyeingProductionPDASaveResponse> finishProductionCompactingResponse = new MutableLiveData<>();
//        apiInterface.saveUpdateDyeingProductionResponse(body).enqueue(new Callback<V1_DyeingProductionPDASaveResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<V1_DyeingProductionPDASaveResponse> call,
//                                   @NonNull Response<V1_DyeingProductionPDASaveResponse> response) {
//                Log.d(TAG, "onResponse: "+response.toString());
//                isLoading.setValue(false);
//
//                if (response.isSuccessful() && response.body() != null) {
//                    finishProductionCompactingResponse.setValue(response.body());
//                    Log.d(TAG, "Response Code: "+response.code());
//                    Log.d(TAG, "Response Body: "+response.body());
//
//                } else {
//                    try {
//                        if(response.errorBody() != null)
//                            Log.e(TAG, "Error Response Body: " + response.errorBody().string());
//                    } catch (IOException e) {
//                        Log.e(TAG, "Error parsing error body", e);
//                    }
//                    Log.e(TAG, "Response Error Code: " + response.code());
//                    finishProductionCompactingResponse.setValue(null);
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<V1_DyeingProductionPDASaveResponse> call, @NonNull Throwable t) {
//                finishProductionCompactingResponse.setValue(null);
//                isLoading.setValue(false);
//                Log.e(TAG, "onFailure: " + t.getMessage());
//            }
//        });
//        return finishProductionCompactingResponse;
//    }
//
//    public MutableLiveData<V1_DyeingProductionPDAResponse> getDyeingProductionCall(String batch_id) {
//        isLoading.setValue(true);
//        MutableLiveData<V1_DyeingProductionPDAResponse> data = new MutableLiveData<>();
//        apiInterface.getDyeingProductionResponseCall(batch_id).enqueue(new Callback<V1_DyeingProductionPDAResponse>() {
//            @Override
//            public void onResponse(Call<V1_DyeingProductionPDAResponse> call, Response<V1_DyeingProductionPDAResponse> response) {
//                isLoading.setValue(false);
//                if (response.isSuccessful()) {
//                    data.setValue(response.body());
//                    Log.d(TAG,"onResponse: " + response.body());
//                } else {
//                    Log.e(TAG, "onResponse: Error: "+response.message()+" Response Code: "+response.code());
//                    data.setValue(null);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<V1_DyeingProductionPDAResponse> call, Throwable t) {
//                isLoading.setValue(false);
//                Log.e(TAG, "onFailure: " + t.getMessage());
//                data.setValue(null);
//            }
//        });
//        return data;
//    }

    public MutableLiveData<V1_DyeingProductionPDASaveResponse> postDyeingProductionPDAResponse(RequestBody body) {
        isLoading.setValue(true);
        MutableLiveData<V1_DyeingProductionPDASaveResponse> finishProductionCompactingResponse = new MutableLiveData<>();
        apiInterface.saveUpdateDyeingProductionResponse(body).enqueue(new Callback<V1_DyeingProductionPDASaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_DyeingProductionPDASaveResponse> call,
                                   @NonNull Response<V1_DyeingProductionPDASaveResponse> response) {
                Log.d(TAG, "onResponse: " + response.toString());
                isLoading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    finishProductionCompactingResponse.setValue(response.body());
                    Log.d(TAG, "Response Code: " + response.code());
                    Log.d(TAG, "Response Body: " + response.body());
                } else {
                    try {
                        if (response.errorBody() != null)
                            Log.e(TAG, "Error Response Body: " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e(TAG, "Error parsing error body", e);
                    }
                    Log.e(TAG, "Response Error Code: " + response.code());
                    finishProductionCompactingResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_DyeingProductionPDASaveResponse> call, @NonNull Throwable t) {
                finishProductionCompactingResponse.setValue(null);
                isLoading.setValue(false);
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        return finishProductionCompactingResponse;
    }

    public MutableLiveData<V1_DyeingProductionPDAResponse> getDyeingProductionCall(String batch_id) {
        isLoading.setValue(true);
        MutableLiveData<V1_DyeingProductionPDAResponse> data = new MutableLiveData<>();
        apiInterface.getDyeingProductionResponseCall(batch_id).enqueue(new Callback<V1_DyeingProductionPDAResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_DyeingProductionPDAResponse> call, @NonNull Response<V1_DyeingProductionPDAResponse> response) {
                isLoading.setValue(false);
                Log.d(TAG, "onResponse: " + response.toString());
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                    Log.d(TAG, "onResponse: " + response.body());
                } else {
                    data.setValue(null);
                    Log.e(TAG, "onResponse: Error: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_DyeingProductionPDAResponse> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                data.setValue(null);
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        return data;
    }

//    public MutableLiveData<V1_DyeingProductionPDAResponse> getDyeingProductionCall(String batch_id, String entryFormNo) {
//        isLoading.setValue(true);
//        MutableLiveData<V1_DyeingProductionPDAResponse> apiResponse = new MutableLiveData<>();
//        apiInterface.getDyeingProductionResponseCall(batch_id, entryFormNo).enqueue(new Callback<V1_DyeingProductionPDAResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<V1_DyeingProductionPDAResponse> call,
//                                   @NonNull Response<V1_DyeingProductionPDAResponse> response) {
//                Log.d(TAG, "onResponse: "+response.toString());
//                isLoading.setValue(false);
//                if (response.isSuccessful()){
//                    if(response.code() == 200){
//                        apiResponse.setValue(response.body());
//                    }else {
//                        apiResponse.setValue(null);
//                    }
//                }else {
//                    apiResponse.setValue(null);
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<V1_DyeingProductionPDAResponse> call, @NonNull Throwable t) {
//                apiResponse.setValue(null);
//                isLoading.setValue(false);
//                Log.d(TAG, "onFailure: "+t.getMessage()+t);
//            }
//        });
//        return apiResponse;
//    }

}

