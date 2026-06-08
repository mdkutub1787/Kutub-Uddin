package com.kutub.insurance.serviceInterface;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiClient {

    private static Retrofit retrofit = null;
    private static ApiInterface apiInterface;
    private static final String baseUrl = "https://6807123ce81df7060eb8baf2.mockapi.io/";

    // Method to get Retrofit instance
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // Configure Gson
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            // Configure OkHttpClient
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            // Build Retrofit instance
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    // Method to get ApiInterface
    public static ApiInterface getApiInterface() {
        if (apiInterface == null) {
            apiInterface = getRetrofitInstance().create(ApiInterface.class);
        }
        return apiInterface;
    }
}