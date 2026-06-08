package com.kutub.insurance.serviceInterface;

import com.kutub.insurance.model.ApiResponse;
import com.kutub.insurance.model.BillResponse;
import com.kutub.insurance.model.PolicyResponse;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @GET("policy")
    Call<List<PolicyResponse>> getPolicy();

    @POST("policy")
    Call<ApiResponse> postPolicy(@Body RequestBody body);
    @GET("bill")
    Call<List<BillResponse>> getBill();

    @POST("bill")
    Call<ApiResponse> postBill(@Body RequestBody body);
}