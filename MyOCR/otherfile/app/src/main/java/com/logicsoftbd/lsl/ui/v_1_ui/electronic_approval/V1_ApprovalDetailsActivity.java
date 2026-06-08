package com.logicsoftbd.lsl.ui.v_1_ui.electronic_approval;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ApprovalMenuDetails;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ApprovalResponseModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ApprovalUnApprovalDetailsModel;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.utils.ApiUtils;
import com.logicsoftbd.lsl.utils.VerticalSpacingItemDecorator;
import com.logicsoftbd.lsl.viewModel.ApprovalViewModel;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class V1_ApprovalDetailsActivity extends AppCompatActivity implements V1_ApprovalDetailsRecyclerViewAdapter.OnHeadListener,
        V1_ApprovalDetailsRecyclerViewAdapter.OnDenyHeadListener, V1_ApprovalDetailsRecyclerViewAdapter.OnUnApproveHeadListener,
        V1_ApprovalDetailsRecyclerViewAdapter.OnDetailsListener{

    private static final String TAG = "V1_ApprovalDetailsActiv";
    private String user_id, base_url, menu_id, menu_name, unApprovedRequestStatus, unApprovedRequestId = "";
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;
    private ProgressDialog pDialog;
    private ProgressBar progressBar;
    private V1_ApprovalDetailsRecyclerViewAdapter approvalDetailsRecyclerViewAdapter;
    private RecyclerView approvalRecyclerView;
    private ArrayList<V1_ApprovalMenuDetails.UnapproveDatum> unApprovalDetailsModels = new ArrayList<>();
    private ArrayList<V1_ApprovalMenuDetails.ApproveDatum> approvalDetailsModels = new ArrayList<>();
    private ArrayList<V1_ApprovalUnApprovalDetailsModel> approvalUnApprovalDetailsModels = new ArrayList<>();
    private ApprovalViewModel approvalViewModel;
    private LinearLayout noDataFoundLy;
    private Button approveBtn, unApproveBtn;
    private Boolean isApprove = false;
    private SearchView _searchView;
    Drawable leftIcon;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_approval_details);

        Intent intent = getIntent();
        menu_id = intent.getStringExtra("menu_id");
        menu_name = intent.getStringExtra("menu_name");
        unApprovedRequestStatus = intent.getStringExtra("unapproved_status");
        unApprovedRequestId = intent.getStringExtra("unApprovedRequestId");
        setTitle(menu_name);

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = (_preferences.getString("login_userid", ""));
        base_url = (_preferences.getString("base_url", ""));


        Log.d(TAG, "onCreate: "+unApprovedRequestStatus+" "+unApprovedRequestId);

        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);
        approvalViewModel = new ViewModelProvider(this).get(ApprovalViewModel.class);

        initUI();
        fetchApprovalDataList();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra("message");
                Log.d(TAG, "onReceive: "+message);
                if (message != null && message.equals("Approval")) {
                    fetchApprovalDataList();
                }
            }
        };
        setupRecyclerView();
    }

    private void initUI () {
        approvalRecyclerView = findViewById(R.id.approvalDetailsRecyclerView);
        noDataFoundLy = findViewById(R.id.noDataFoundLy);
        approveBtn = findViewById(R.id.approveBtn);
        unApproveBtn = findViewById(R.id.unApproveBtn);
        progressBar = findViewById(R.id.progressBar);
        _searchView = findViewById(R.id.searchView);

        approveBtn.setBackgroundColor(getResources().getColor(R.color.ballReleaseColor));
        unApproveBtn.setBackgroundColor(getResources().getColor(R.color.purple_500));

        leftIcon = ContextCompat.getDrawable(this, R.drawable.baseline_check_circle_24);
        unApproveBtn.setCompoundDrawablesWithIntrinsicBounds(leftIcon, null, null, null);
        approveBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

        approveBtn.setOnClickListener(v -> {
            unApprovedDataPopulate();

        });

        unApproveBtn.setOnClickListener(v -> {
            isApprove = false;
            approveBtn.setBackgroundColor(getResources().getColor(R.color.ballReleaseColor));
            unApproveBtn.setBackgroundColor(getResources().getColor(R.color.purple_500));

            unApproveBtn.setCompoundDrawablesWithIntrinsicBounds(leftIcon, null, null, null);
            approveBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

            fetchApprovalDataList();
        });

//        if(unApprovedRequestStatus != null && unApprovedRequestStatus.equals("unapproved")){
//            unApprovedDataPopulate();
//            Log.d(TAG, "initUI: Unapproved");
//        }

        approvalViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        });

        _searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                approvalDetailsRecyclerViewAdapter.filterList(query); // Filter list based on query
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                approvalDetailsRecyclerViewAdapter.filterList(newText); // Filter list as text changes
                return false;
            }
        });
    }

    private void unApprovedDataPopulate() {
        isApprove = true;
        approveBtn.setBackgroundColor(getResources().getColor(R.color.purple_500));
        unApproveBtn.setBackgroundColor(getResources().getColor(R.color.ballReleaseColor));

        unApproveBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        approveBtn.setCompoundDrawablesWithIntrinsicBounds(leftIcon, null, null, null);

        fetchApprovalDataList();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchApprovalDataList() {
        approvalViewModel.getApprovalDetailsResponse(user_id, menu_id).observe(this, apiResponse -> {
            if(apiResponse != null){
                Log.d(TAG, "fetchApprovalDataList: "+apiResponse.toString());
                List<V1_ApprovalMenuDetails.ApproveDatum> approveDatumList = apiResponse.getData().getApproveData();
                List<V1_ApprovalMenuDetails.UnapproveDatum> unapproveDatumList = apiResponse.getData().getUnapproveData();
                approvalUnApprovalDetailsModels.clear();
//                if (approvalUnApprovalDetailsModels.size() > 0)
//                    approvalUnApprovalDetailsModels.clear();
                if(isApprove){
                    for(int i =0; i < approveDatumList.size(); i++){
                        V1_ApprovalUnApprovalDetailsModel approvalUnApprovalDetailsModel = new V1_ApprovalUnApprovalDetailsModel();
                        approvalUnApprovalDetailsModel.setId(approveDatumList.get(i).getId());
                        approvalUnApprovalDetailsModel.setSysNumber(approveDatumList.get(i).getSysNumber());
                        approvalUnApprovalDetailsModel.setDate(approveDatumList.get(i).getDate());
                        approvalUnApprovalDetailsModel.setDeliveryDate(approveDatumList.get(i).getDeliveryDate());
                        approvalUnApprovalDetailsModel.setCompany(approveDatumList.get(i).getCompany());
                        approvalUnApprovalDetailsModel.setBuyer(approveDatumList.get(i).getBuyer());
                        approvalUnApprovalDetailsModel.setSysDef(approveDatumList.get(i).getSysDef());
                        approvalUnApprovalDetailsModel.setDesc(approveDatumList.get(i).getDesc());
                        approvalUnApprovalDetailsModel.setIsSeen(approveDatumList.get(i).getIsSeen());
                        approvalUnApprovalDetailsModel.setIsApproval(false);
                        if(unApprovedRequestId != null && approveDatumList.get(i).getId().equals(unApprovedRequestId)){
                            approvalUnApprovalDetailsModel.setMarked(true);
                        }else{
                            approvalUnApprovalDetailsModel.setMarked(false);
                        }
                        approvalUnApprovalDetailsModels.add(approvalUnApprovalDetailsModel);
                    }
                    Log.d(TAG, "fetchApprovalDataList: ###########"+approvalUnApprovalDetailsModels.size());
                } else {
                    for(int i =0; i < unapproveDatumList.size(); i++){
                        V1_ApprovalUnApprovalDetailsModel approvalUnApprovalDetailsModel = new V1_ApprovalUnApprovalDetailsModel();
                        approvalUnApprovalDetailsModel.setId(unapproveDatumList.get(i).getId());
                        approvalUnApprovalDetailsModel.setSysNumber(unapproveDatumList.get(i).getSysNumber());
                        approvalUnApprovalDetailsModel.setDate(unapproveDatumList.get(i).getDate());
                        approvalUnApprovalDetailsModel.setDeliveryDate(unapproveDatumList.get(i).getDeliveryDate());
                        approvalUnApprovalDetailsModel.setCompany(unapproveDatumList.get(i).getCompany());
                        approvalUnApprovalDetailsModel.setBuyer(unapproveDatumList.get(i).getBuyer());
                        approvalUnApprovalDetailsModel.setSysDef(unapproveDatumList.get(i).getSysDef());
                        approvalUnApprovalDetailsModel.setDesc(unapproveDatumList.get(i).getDesc());
                        approvalUnApprovalDetailsModel.setIsSeen(unapproveDatumList.get(i).getIsSeen());
                        approvalUnApprovalDetailsModel.setIsApproval(true);

                        approvalUnApprovalDetailsModels.add(approvalUnApprovalDetailsModel);
                    }
                    Log.d(TAG, "fetchApprovalDataList: ###########"+approvalUnApprovalDetailsModels.size());
                }

                if(approvalUnApprovalDetailsModels.size() == 0 || approvalUnApprovalDetailsModels == null){
                    noDataFoundLy.setVisibility(View.VISIBLE);
                }else{
                    noDataFoundLy.setVisibility(View.GONE);
                }
                approvalDetailsRecyclerViewAdapter.filterList("");
                approvalDetailsRecyclerViewAdapter.notifyDataSetChanged();
            }else {
                new SweetAlertDialog(V1_ApprovalDetailsActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Message")
                        .setContentText("Something went wrong!")
                        .setConfirmText("OK")
                        .show();
            }

        });

    }
    private void setupRecyclerView () {
        if (approvalDetailsRecyclerViewAdapter == null) {
            approvalDetailsRecyclerViewAdapter = new V1_ApprovalDetailsRecyclerViewAdapter(this, approvalUnApprovalDetailsModels, this, this,this, this);
            approvalRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            approvalRecyclerView.setAdapter(approvalDetailsRecyclerViewAdapter);
            VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
            approvalRecyclerView.addItemDecoration(itemDecorator);
            approvalRecyclerView.setItemAnimator(new DefaultItemAnimator());
            approvalRecyclerView.setNestedScrollingEnabled(true);
        } else {
            approvalDetailsRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onHeadClick(int position, View v) {
        approvalViewModel = new ViewModelProvider(this).get(ApprovalViewModel.class);
        approvalViewModel.postApprovalResponse(user_id, menu_id, approvalUnApprovalDetailsModels.get(position).getId()).observe(this, apiResponse -> {
            V1_ApprovalResponseModel response = apiResponse;
            if(response != null){
                new SweetAlertDialog(V1_ApprovalDetailsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Message")
                        .setContentText(response.getResultset().getMessage())
                        .setConfirmText("OK")
                        .show();
                fetchApprovalDataList();
            }else {
                new SweetAlertDialog(V1_ApprovalDetailsActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Message")
                        .setContentText("Something went wrong!")
                        .setConfirmText("OK")
                        .show();
            }
        });
    }

    private void denyPopUp(int position) {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.deny_reason_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = promptsView.findViewById(R.id.editTextDialogUserInput);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Close",
                        (dialog, id) -> {
                            dialog.cancel();
                        })
                .setNegativeButton("OK",
                        (dialog, id) -> {
                            saveDenyData(position, userInput.getText().toString());
                            dialog.cancel();
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void saveDenyData(int position, String toString) {
        approvalViewModel.postDenyApprovalResponse(user_id, menu_id, approvalUnApprovalDetailsModels.get(position).getId(), toString).observe(this, apiResponse -> {
            V1_ApprovalResponseModel response = apiResponse;
            if(response != null){
                new SweetAlertDialog(V1_ApprovalDetailsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Message")
                        .setContentText(response.getResultset().getMessage())
                        .setConfirmText("OK")
                        .show();
                fetchApprovalDataList();
            }else {
                new SweetAlertDialog(V1_ApprovalDetailsActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Message")
                        .setContentText("Something went wrong!")
                        .setConfirmText("OK")
                        .show();
            }
        });
    }

    @Override
    public void onUnApproveHeadClick(int position, View v) {
        approvalViewModel.postUnApprovalResponse(user_id, menu_id, approvalUnApprovalDetailsModels.get(position).getId()).observe(this, apiResponse -> {
            V1_ApprovalResponseModel response = apiResponse;
            if(response != null){
                new SweetAlertDialog(V1_ApprovalDetailsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Message")
                        .setContentText(response.getResultset().getMessage())
                        .setConfirmText("OK")
                        .show();
                fetchApprovalDataList();
            }else {
                new SweetAlertDialog(V1_ApprovalDetailsActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Message")
                        .setContentText("Something went wrong!")
                        .setConfirmText("OK")
                        .show();
            }
        });
    }

    @Override
    public void onDenyHeadClick(int position, View v) {
        denyPopUp(position);
    }

    @Override
    public void onDetailsClick(int position, View v) {
        Intent intent = new Intent(V1_ApprovalDetailsActivity.this, V1_ApprovalItemDetailsActivity.class);
        if(isApprove){
            intent.putExtra("req_mst_id", approvalUnApprovalDetailsModels.get(position).getId());
            intent.putExtra("assetName", approvalUnApprovalDetailsModels.get(position).getSysNumber());
        }else{
            intent.putExtra("req_mst_id", approvalUnApprovalDetailsModels.get(position).getId());
            intent.putExtra("assetName", approvalUnApprovalDetailsModels.get(position).getSysNumber());
        }
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("custom-event");
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
        fetchApprovalDataList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(V1_ApprovalDetailsActivity.this, V1_ElectronicApprovalActivity.class));
    }
}
