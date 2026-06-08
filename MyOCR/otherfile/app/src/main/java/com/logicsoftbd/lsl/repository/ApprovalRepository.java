package com.logicsoftbd.lsl.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.logicsoftbd.lsl.data.network.v1_model.V1_ApprovalMenuDetails;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ApprovalNotificationsModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ApprovalResponseModel;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.serviceInterface.RetrofitApiClient;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApprovalRepository {
    private static final String TAG = "ApprovalRepository";
    private static ApprovalRepository approvalRepository;

    public static ApprovalRepository getInstance(){
        if (approvalRepository == null){
            approvalRepository = new ApprovalRepository();
        }
        return approvalRepository;
    }

    private ApiInterface apiInterface;

    public ApprovalRepository(){
        apiInterface = RetrofitApiClient.createService(ApiInterface.class);
    }

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<V1_ApprovalNotificationsModel> getApprovalMenu(String user_id){
        isLoading.setValue(true);
        MutableLiveData<V1_ApprovalNotificationsModel> approvalData = new MutableLiveData<>();
        apiInterface.getApprovalNotificationModelCall(user_id).enqueue(new Callback<V1_ApprovalNotificationsModel>() {
            @Override
            public void onResponse(Call<V1_ApprovalNotificationsModel> call,
                                   Response<V1_ApprovalNotificationsModel> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        approvalData.setValue(response.body());
                    }else {
                        approvalData.setValue(null);
                    }
                }else {
                    approvalData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<V1_ApprovalNotificationsModel> call, Throwable t) {
                approvalData.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return approvalData;
    }

    public MutableLiveData<V1_ApprovalMenuDetails> getApprovalDetails(String user_id, String menu_id){
        MutableLiveData<V1_ApprovalMenuDetails> approvalData = new MutableLiveData<>();
        isLoading.setValue(true);
        apiInterface.getApprovalNotificationDetailsModelCall(user_id, menu_id).enqueue(new Callback<V1_ApprovalMenuDetails>() {
            @Override
            public void onResponse(Call<V1_ApprovalMenuDetails> call,
                                   Response<V1_ApprovalMenuDetails> response) {
                isLoading.setValue(false);
                Log.d(TAG, "onResponse: unApprove"+response.toString());
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        approvalData.setValue(response.body());
                    }else {
                        approvalData.setValue(null);
                    }
                }else {
                    approvalData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<V1_ApprovalMenuDetails> call, Throwable t) {
                approvalData.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: _________________#"+t.getCause());
            }
        });
        return approvalData;
    }

    public MutableLiveData<V1_ApprovalResponseModel> postApproval(String user_id, String menu_id, String ref_no){
        MutableLiveData<V1_ApprovalResponseModel> approvalData = new MutableLiveData<>();
        RequestBody _user_id = RequestBody.create(
                MediaType.parse("text/plain"),
                String.valueOf(user_id));
        RequestBody _menu_id = RequestBody.create(
                MediaType.parse("text/plain"),
                String.valueOf(menu_id));
        RequestBody _ref_no = RequestBody.create(
                MediaType.parse("text/plain"),
                String.valueOf(ref_no));
        isLoading.setValue(true);
        Log.d(TAG, "postApproval: "+user_id +" "+ menu_id +" "+ ref_no);
        apiInterface.postApprovalModelCall(_user_id, _menu_id, _ref_no).enqueue(new Callback<V1_ApprovalResponseModel>() {
            @Override
            public void onResponse(Call<V1_ApprovalResponseModel> call,
                                   Response<V1_ApprovalResponseModel> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        approvalData.setValue(response.body());
                    }else {
                        approvalData.setValue(null);
                    }
                }else {
                    approvalData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<V1_ApprovalResponseModel> call, Throwable t) {
                approvalData.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return approvalData;
    }

    public MutableLiveData<V1_ApprovalResponseModel> postUnApproval(String user_id, String menu_id, String ref_no){
        MutableLiveData<V1_ApprovalResponseModel> approvalData = new MutableLiveData<>();
        RequestBody _user_id = RequestBody.create(
                MediaType.parse("text/plain"),
                String.valueOf(user_id));
        RequestBody _menu_id = RequestBody.create(
                MediaType.parse("text/plain"),
                String.valueOf(menu_id));
        RequestBody _ref_no = RequestBody.create(
                MediaType.parse("text/plain"),
                String.valueOf(ref_no));

        isLoading.setValue(true);
        apiInterface.postUnApprovalModelCall(_user_id, _menu_id, _ref_no).enqueue(new Callback<V1_ApprovalResponseModel>() {
            @Override
            public void onResponse(Call<V1_ApprovalResponseModel> call,
                                   Response<V1_ApprovalResponseModel> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: 500"+response.toString());
                    if(response.code() == 200){
                        approvalData.setValue(response.body());
                    }else {
                        approvalData.setValue(null);
                    }
                }else {
                    approvalData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<V1_ApprovalResponseModel> call, Throwable t) {
                approvalData.setValue(null);
                isLoading.setValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return approvalData;
    }

    public MutableLiveData<V1_ApprovalResponseModel> postDenyApproval(String user_id, String menu_id, String ref_no, String message){
        MutableLiveData<V1_ApprovalResponseModel> approvalData = new MutableLiveData<>();
        RequestBody _user_id = RequestBody.create(
                MediaType.parse("text/plain"),
                String.valueOf(user_id));
        RequestBody _menu_id = RequestBody.create(
                MediaType.parse("text/plain"),
                String.valueOf(menu_id));
        RequestBody _ref_no = RequestBody.create(
                MediaType.parse("text/plain"),
                String.valueOf(ref_no));
        RequestBody _message = RequestBody.create(
                MediaType.parse("text/plain"),
                String.valueOf(message));
        isLoading.setValue(true);
        Log.d(TAG, "postDenyApproval: "+ user_id +" "+menu_id +" "+ref_no);
        apiInterface.postDenyApprovalModelCall(_user_id, _menu_id, _ref_no, _message).enqueue(new Callback<V1_ApprovalResponseModel>() {
            @Override
            public void onResponse(Call<V1_ApprovalResponseModel> call,
                                   Response<V1_ApprovalResponseModel> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                isLoading.setValue(false);
                if (response.isSuccessful()){
                    if(response.code() == 200){
                        approvalData.setValue(response.body());
                    }else {
                        approvalData.setValue(null);
                    }
                }else {
                    approvalData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<V1_ApprovalResponseModel> call, Throwable t) {
                isLoading.setValue(false);
                approvalData.setValue(null);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return approvalData;
    }

}
