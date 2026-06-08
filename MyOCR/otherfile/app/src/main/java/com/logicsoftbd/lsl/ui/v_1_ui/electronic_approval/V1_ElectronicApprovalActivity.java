package com.logicsoftbd.lsl.ui.v_1_ui.electronic_approval;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ApprovalNotificationsModel;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.v_1_ui.menu.V1_MenuActivity;
import com.logicsoftbd.lsl.utils.ApiUtils;
import com.logicsoftbd.lsl.utils.VerticalSpacingItemDecorator;
import com.logicsoftbd.lsl.viewModel.ApprovalViewModel;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class V1_ElectronicApprovalActivity extends AppCompatActivity implements V1_ApprovalMenuRecyclerViewAdapter.OnHeadListener, V1_ApprovalMenuRecyclerViewAdapter.OnHeadLongClickListener {
    private static final String TAG = "V1_ElectronicApprovalAc";

    private ProgressBar progressBar;
    private String user_id, base_url = "";
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;
    private ProgressDialog pDialog;
    private V1_ApprovalMenuRecyclerViewAdapter approvalMenuRecyclerViewAdapter;
    private RecyclerView approvalRecyclerView;
    private ArrayList<V1_ApprovalNotificationsModel.Datum> approvalNotificationModels = new ArrayList<>();
    private ApprovalViewModel approvalViewModel;
    private BroadcastReceiver receiver;
    private LinearLayout noDataFoundLy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_electronic_approval);

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = (_preferences.getString("login_userid", ""));
        base_url = (_preferences.getString("base_url", ""));

        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);

        initUI();
        fetchBatchData();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra("message");
                if (message != null && message.equals("Approval")) {
                    fetchBatchData();
                }
            }
        };

        setupRecyclerView();
    }

    private void fetchBatchData() {
        approvalViewModel = new ViewModelProvider(this).get(ApprovalViewModel.class);

        approvalViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        });

        approvalViewModel.getApprovalResponse(user_id).observe(this, apiResponse -> {

            if(apiResponse != null && apiResponse.getData() != null){
                approvalNotificationModels.clear();

                approvalNotificationModels.addAll(apiResponse.getData());
                if(approvalNotificationModels.size() == 0 || approvalNotificationModels == null){
                    noDataFoundLy.setVisibility(View.VISIBLE);
                }else{
                    noDataFoundLy.setVisibility(View.GONE);
                }

                approvalMenuRecyclerViewAdapter.notifyDataSetChanged();

            } else {
                new SweetAlertDialog(V1_ElectronicApprovalActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Message")
                        .setContentText("Something went wrong!")
                        .setConfirmText("OK")
                        .show();
                noDataFoundLy.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initUI() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        approvalRecyclerView = findViewById(R.id.approvalMenuRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        noDataFoundLy = findViewById(R.id.noDataFoundLy);
    }

    private void setupRecyclerView() {
        if (approvalMenuRecyclerViewAdapter == null) {
            approvalMenuRecyclerViewAdapter = new V1_ApprovalMenuRecyclerViewAdapter(this, approvalNotificationModels, this, this);
            approvalRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            approvalRecyclerView.setAdapter(approvalMenuRecyclerViewAdapter);
            VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(5);
            approvalRecyclerView.addItemDecoration(itemDecorator);
            approvalRecyclerView.setItemAnimator(new DefaultItemAnimator());
            approvalRecyclerView.setNestedScrollingEnabled(true);
        } else {
            approvalMenuRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onHeadClick(int position, View v) {
        Intent intent = new Intent(this, V1_ApprovalDetailsActivity.class);
        intent.putExtra("menu_id", approvalNotificationModels.get(position).getMenuId());
        intent.putExtra("menu_name", approvalNotificationModels.get(position).getMenu());
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
        fetchBatchData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public void onHeadLongClick(int position, View v) {
        TextView okButton;

        View alertCustomDialog = LayoutInflater.from(this).inflate(R.layout.notification_settings_layout,null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setView(alertCustomDialog);
        okButton = alertCustomDialog.findViewById(R.id.ok);

        final AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
        dialog.setCancelable(false);

        okButton.setOnClickListener(v1 -> dialog.dismiss());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(V1_ElectronicApprovalActivity.this, V1_MenuActivity.class));
    }
}