package com.kutub.insurance.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kutub.insurance.model.ApiResponse;
import com.kutub.insurance.model.BillResponse;
import com.kutub.insurance.model.PolicyResponse;
import com.kutub.insurance.serviceInterface.ApiInterface;
import com.kutub.insurance.serviceInterface.RetrofitApiClient;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsuranceRepository {
    private static final String TAG = "InsuranceRepository";
    private static InsuranceRepository instance;

    private final ApiInterface apiInterface;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    // Private constructor to enforce singleton pattern
    public InsuranceRepository() {
        apiInterface = RetrofitApiClient.getApiInterface();
    }

    // Singleton instance getter
    public static InsuranceRepository getInstance() {
        if (instance == null) {
            instance = new InsuranceRepository();
        }
        return instance;
    }

    // Getter for loading state
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    // Fetch all policies
    public MutableLiveData<List<PolicyResponse>> getPolicy() {
        isLoading.setValue(true);
        MutableLiveData<List<PolicyResponse>> billsLiveData = new MutableLiveData<>();

        apiInterface.getPolicy().enqueue(new Callback<List<PolicyResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<PolicyResponse>> call, @NonNull Response<List<PolicyResponse>> response) {
                isLoading.setValue(false);
                Log.d(TAG, "Request URL: " + call.request().url()); // Log the request URL
                if (response.isSuccessful() && response.body() != null) {
                    billsLiveData.postValue(response.body());
                } else {
                    Log.e(TAG, "Error: " + response.code() + " - " + response.message());
                    billsLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PolicyResponse>> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                Log.e(TAG, "onFailure: " + t.getMessage());
                Log.d(TAG, "Request URL: " + call.request().url()); // Log the request URL on failure
                billsLiveData.postValue(null);
            }
        });

        return billsLiveData;
    }

    public MutableLiveData<ApiResponse> postPolicy(RequestBody body) {
        MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();
        apiInterface.postPolicy(body).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    responseLiveData.postValue(response.body());
                } else {
                    responseLiveData.postValue(null);
                    Log.e(TAG, "Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                responseLiveData.postValue(null);
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        return responseLiveData;
    }

    public MutableLiveData<List<BillResponse>> getBill() {
        isLoading.setValue(true);
        MutableLiveData<List<BillResponse>> billsLiveData = new MutableLiveData<>();

        apiInterface.getBill().enqueue(new Callback<List<BillResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<BillResponse>> call, @NonNull Response<List<BillResponse>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    billsLiveData.postValue(response.body());
                } else {
                    Log.e(TAG, "Error: " + response.code() + " - " + response.message());
                    billsLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<BillResponse>> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                Log.e(TAG, "onFailure: " + t.getMessage());
                billsLiveData.postValue(null);
            }
        });

        return billsLiveData;
    }


    public MutableLiveData<ApiResponse> postBill(RequestBody body) {
        MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();
        apiInterface.postBill(body).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    responseLiveData.postValue(response.body());
                } else {
                    responseLiveData.postValue(null);
                    Log.e(TAG, "Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                responseLiveData.postValue(null);
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        return responseLiveData;
    }

}