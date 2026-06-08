package com.logicsoftbd.lsl.ui.v_1_ui.electronic_approval;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ApprovalItemDetailsModel;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.utils.ApiUtils;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class V1_ApprovalItemDetailsActivity extends AppCompatActivity {
    private static final String TAG = "V1_ApprovalItemDetailsA";
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;
    private ProgressBar progressBar;
    private String base_url, req_mst_id, assetName;
    private RecyclerView approvalRecyclerView;
    private V1_ApprovalDetailsItemRecyclerViewAdapter approvalDetailsRecyclerViewAdapter;
    private ArrayList<V1_ApprovalItemDetailsModel.Datum> approvalDetailsModels = new ArrayList<>();
    private TextView categoryNameTV, assesNameTV;
    private CardView approvalDetailsCard;
    private LinearLayout errorDataSetLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_approval_item_details);

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        base_url = (_preferences.getString("base_url", ""));

        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);
        initUI();
        fetchApprovalDataList();
    }

    private void initUI() {
        progressBar = findViewById(R.id.progressBar);
        approvalRecyclerView = findViewById(R.id.approvalDetailsRecyclerView);
        categoryNameTV = findViewById(R.id.categoryNameTV);
        assesNameTV = findViewById(R.id.assesNameTV);
        errorDataSetLayout = findViewById(R.id.errorDataSetLayout);
        approvalDetailsCard = findViewById(R.id.approvalDetailsCard);

        Intent intent = getIntent();
        req_mst_id = intent.getStringExtra("req_mst_id");
        assetName = intent.getStringExtra("assetName");

        assesNameTV.setText(assetName);
    }

    private void setupRecyclerView () {
        if (approvalDetailsRecyclerViewAdapter == null) {
            approvalDetailsRecyclerViewAdapter = new V1_ApprovalDetailsItemRecyclerViewAdapter(this, approvalDetailsModels);
            approvalRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            approvalRecyclerView.setAdapter(approvalDetailsRecyclerViewAdapter);
            approvalRecyclerView.setItemAnimator(new DefaultItemAnimator());
            approvalRecyclerView.setNestedScrollingEnabled(true);
        } else {
            approvalDetailsRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    private void fetchApprovalDataList() {
        progressBar.setVisibility(View.VISIBLE);
        apiInterface.getApprovalItemDetailsCall(req_mst_id).enqueue(new Callback<V1_ApprovalItemDetailsModel>() {
            @Override
            public String toString() {
                return "$classname{}";
            }

            @Override
            public void onResponse(Call<V1_ApprovalItemDetailsModel> call, Response<V1_ApprovalItemDetailsModel> response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onResponse: "+req_mst_id +" "+response.toString());
                if(response.isSuccessful()){
                    if(response.code() == 200 && response.body().getData() != null && response.body().getData().size() > 0){
                        errorDataSetLayout.setVisibility(View.GONE);
                        approvalDetailsCard.setVisibility(View.VISIBLE);
                        approvalDetailsModels = (ArrayList<V1_ApprovalItemDetailsModel.Datum>) response.body().getData();
                        categoryNameTV.setText(response.body().getData().get(0).getItemCategory());
//                        approvalDetailsRecyclerViewAdapter.notifyDataSetChanged();
                    }
                    setupRecyclerView();
                }{
                    errorDataSetLayout.setVisibility(View.VISIBLE);
                    approvalDetailsCard.setVisibility(View.GONE);
                    new SweetAlertDialog(V1_ApprovalItemDetailsActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Message")
                            .setContentText("Data not found")
                            .setConfirmText("OK")
                            .show();
                }

            }

            @Override
            public void onFailure(Call<V1_ApprovalItemDetailsModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                errorDataSetLayout.setVisibility(View.VISIBLE);
                approvalDetailsCard.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: "+t.getMessage());
                new SweetAlertDialog(V1_ApprovalItemDetailsActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Message")
                        .setContentText("Data not found.")
                        .setConfirmText("OK")
                        .show();
            }
        });
    }
}