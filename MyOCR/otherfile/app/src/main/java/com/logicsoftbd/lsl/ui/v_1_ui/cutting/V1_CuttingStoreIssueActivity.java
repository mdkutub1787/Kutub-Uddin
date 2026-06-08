package com.logicsoftbd.lsl.ui.v_1_ui.cutting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.IssueBarcodeModelPost;
import com.logicsoftbd.lsl.data.network.v1_model.V1_CuttingStoreIssue;
import com.logicsoftbd.lsl.data.network.v1_model.V1_CuttingStoreIssueModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_CuttingStoreIssuePost;
import com.logicsoftbd.lsl.data.network.v1_model.V1_CuttingStoreIssueResponse;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_ScannerActivity;
import com.logicsoftbd.lsl.utils.ApiUtils;
import com.logicsoftbd.lsl.utils.DialogHelper;
import com.logicsoftbd.lsl.utils.VerticalSpacingItemDecorator;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class V1_CuttingStoreIssueActivity extends AppCompatActivity implements View.OnClickListener, V1_StoreRollIssueRecyclerViewAdapter.OnMoreHeadListener, V1_StoreRollIssueRecyclerViewAdapter.OnRemoveHeadListener {
    private static final String TAG = "V1_CuttingStoreIssueAct";
    private Button _bundleScanBT, _addBT, _refreshBT, _saveBT;
    private TextView _bundleScanTV;
    private RecyclerView _cuttingStoreRecyclerView;
    private ProgressDialog pDialog;
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;
    private String base_url, userId, store_roll_issue_barcodeScan = "";
    private ArrayList<V1_CuttingStoreIssueModel> storeRollIssueItemModels = new ArrayList<>();
    private ArrayList<V1_CuttingStoreIssueModel> cuttingStoreIssueModelArrayDataList = new ArrayList<>();
    private V1_StoreRollIssueRecyclerViewAdapter storeRollIssueRecyclerViewAdapter;
    private V1_CuttingStoreIssueModel cuttingStoreIssueModel = new V1_CuttingStoreIssueModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutting_store_issue);

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        base_url = (_preferences.getString("base_url", ""));
        userId = _preferences.getString("login_userid", "");

        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);

        init_ui();
        getDefaultData();
    }

    private void getDefaultData() {
        Intent intent = getIntent();
        store_roll_issue_barcodeScan = intent.getStringExtra("store_roll_issue_barcodeScan");

        cuttingStoreIssueModelArrayDataList = (ArrayList<V1_CuttingStoreIssueModel>) intent.getSerializableExtra("store_issue_roll_data");

        if(cuttingStoreIssueModelArrayDataList != null){
            storeRollIssueItemModels = cuttingStoreIssueModelArrayDataList;
            initRecyclerView();
        }

        if(store_roll_issue_barcodeScan != null){
            getRequestForStoreData(store_roll_issue_barcodeScan);
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _cuttingStoreRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        _cuttingStoreRecyclerView.addItemDecoration(itemDecorator);
        storeRollIssueRecyclerViewAdapter = new V1_StoreRollIssueRecyclerViewAdapter(storeRollIssueItemModels, this, this, this);
        _cuttingStoreRecyclerView.setAdapter(storeRollIssueRecyclerViewAdapter);
    }

    private void init_ui() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        _bundleScanTV = findViewById(R.id.bundleScanTV);
        _cuttingStoreRecyclerView = findViewById(R.id.cuttingStoreRecyclerView);

        _bundleScanBT = findViewById(R.id.bundleScanBT);
        _bundleScanBT.setOnClickListener(this);
        _addBT = findViewById(R.id.addBT);
        _addBT.setOnClickListener(this);
        _refreshBT = findViewById(R.id.refreshBT);
        _refreshBT.setOnClickListener(this);
        _saveBT = findViewById(R.id.saveBT);
        _saveBT.setOnClickListener(this);

    }

    private void getRequestForStoreData(String store_roll_receive_barcodeScan) {
        showDialog();
        apiInterface.getCuttingStoreIssueModelCall(store_roll_receive_barcodeScan).enqueue(new Callback<V1_CuttingStoreIssue>() {
            @SuppressLint({"LongLogTag", "SetTextI18n"})
            @Override
            public void onResponse(Call<V1_CuttingStoreIssue> call, Response<V1_CuttingStoreIssue> response) {
                hideDialog();
                Log.d(TAG, "onResponse: "+response.toString());
                try {
                    if(response.isSuccessful() && response.code() == 200){
                        if(response.body().getData() != null){
                            _bundleScanTV.setText("Barcode no: "+response.body().getData().getBarcodeNo()+", Bundle no: "+response.body().getData().getBundleNo());
                            cuttingStoreIssueModel.setBundleNo(response.body().getData().getBundleNo());
                            cuttingStoreIssueModel.setBarcodeNo(response.body().getData().getBarcodeNo());
                            cuttingStoreIssueModel.setPoBreakdownId(response.body().getData().getPoBreakdownId());
                            cuttingStoreIssueModel.setPoNumber(response.body().getData().getPoNumber());
                            cuttingStoreIssueModel.setBuyerId(response.body().getData().getBuyerId());
                            cuttingStoreIssueModel.setBuyerName(response.body().getData().getBuyerName());
                            cuttingStoreIssueModel.setQcPassQnty(response.body().getData().getQcPassQnty());
                            cuttingStoreIssueModel.setCompanyId(response.body().getData().getCompanyId());
                            cuttingStoreIssueModel.setCompanyName(response.body().getData().getCompanyName());
                            cuttingStoreIssueModel.setSizeId(response.body().getData().getSizeId());
                            cuttingStoreIssueModel.setSizeName(response.body().getData().getSizeName());
                            cuttingStoreIssueModel.setColorNumberId(response.body().getData().getColorNumberId());
                            cuttingStoreIssueModel.setColorName(response.body().getData().getColorName());
                            cuttingStoreIssueModel.setCuttingFloorId(response.body().getData().getCuttingFloorId());
                            cuttingStoreIssueModel.setCuttingFloorName(response.body().getData().getCuttingFloorName());
                            cuttingStoreIssueModel.setProductionQnty(response.body().getData().getProductionQnty());
                            cuttingStoreIssueModel.setPubMsg(response.body().getData().getPubMsg());
                            if(isMatch()){
                                _bundleScanTV.setText("");
                                DialogHelper.showWarningDialog(V1_CuttingStoreIssueActivity.this, "Message", "Duplicate Data Detected!");
                            }
                        }else{
                            DialogHelper.showWarningDialog(V1_CuttingStoreIssueActivity.this, "Message", response.body().getMsg());
                        }
                    }
                }catch (Exception e){
                    Log.d(TAG, "onResponse: "+e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<V1_CuttingStoreIssue> call, Throwable t) {
                hideDialog();
                DialogHelper.showErrorDialog(V1_CuttingStoreIssueActivity.this, "Message", "Something went wrong.");
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bundleScanBT:
                if(_bundleScanTV.getText().toString().isEmpty()){
                    startScanning(2, 2);
                }
                else
                    DialogHelper.showErrorDialog(V1_CuttingStoreIssueActivity.this, "Message", "Please Refresh Barcode first.");
                break;
            case R.id.addBT:
                addDataToList();
                break;
            case R.id.saveBT:
                saveDataToServer();
                break;
            case R.id.refreshBT:
                _bundleScanTV.setText("");
                storeRollIssueItemModels.clear();
                storeRollIssueRecyclerViewAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void saveDataToServer() {
        ArrayList<IssueBarcodeModelPost> cuttingStoreIssueBarcode = new ArrayList<>();
        for(int i=0; i<storeRollIssueItemModels.size(); i++){
            IssueBarcodeModelPost issueBarcodeModelPost = new IssueBarcodeModelPost();
            issueBarcodeModelPost.setBarcode(storeRollIssueItemModels.get(i).getBarcodeNo());
            cuttingStoreIssueBarcode.add(issueBarcodeModelPost);
        }

        V1_CuttingStoreIssuePost cuttingStoreIssuePosts = new V1_CuttingStoreIssuePost();
        cuttingStoreIssuePosts.setUserId(userId);
        cuttingStoreIssuePosts.setBarcodes(cuttingStoreIssueBarcode);
        Log.e("json", "json" + new Gson().toJson(cuttingStoreIssuePosts));
        showDialog();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, new Gson().toJson(cuttingStoreIssuePosts));
        apiInterface.saveCuttingRollIssueCall(body).enqueue(new Callback<V1_CuttingStoreIssueResponse>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<V1_CuttingStoreIssueResponse> call, Response<V1_CuttingStoreIssueResponse> response) {
                hideDialog();
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful() && response.code() == 200){
                    _bundleScanTV.setText("");

                    SweetAlertDialog pDialog = new SweetAlertDialog(V1_CuttingStoreIssueActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                    pDialog.setTitleText("Success");
                    pDialog.setContentText("Data successfully saved!");
                    pDialog.setCancelable(false);
                    pDialog.setConfirmText("Done")
                            .setConfirmClickListener(sweetAlertDialog -> {
                                storeRollIssueItemModels.clear();
                                storeRollIssueRecyclerViewAdapter.notifyDataSetChanged();
                                sweetAlertDialog.cancel();
                            });
                    pDialog.show();
                }else{
                    DialogHelper.showErrorDialog(V1_CuttingStoreIssueActivity.this, "Message", "Something went wrong!");
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<V1_CuttingStoreIssueResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                hideDialog();
                DialogHelper.showErrorDialog(V1_CuttingStoreIssueActivity.this, "Message", "Something went wrong!");
            }
        });
    }

    private void addDataToList() {
        hideKeyboard();
        if(_bundleScanTV.getText().toString().isEmpty()){
            DialogHelper.showWarningDialog(V1_CuttingStoreIssueActivity.this, "Message", "Please scan bundle first.");
        }else{
            try {
                cuttingStoreIssueModel.setStatus(false);
                storeRollIssueItemModels.add(cuttingStoreIssueModel);
                storeRollIssueRecyclerViewAdapter.notifyDataSetChanged();
                _bundleScanTV.setText("");
            }catch (Exception e){

            }
        }
    }

    private void startScanning(int op, int barcode_roll_status) {
        Intent intent = new Intent(V1_CuttingStoreIssueActivity.this, V1_ScannerActivity.class);
        intent.putExtra("qc", "cutting_store_roll_issue_v1");
        intent.putExtra("store_issue_roll_data", storeRollIssueItemModels);
        startActivity(intent);
        finish();
    }

    private boolean isMatch(){
        for(int i=0; i<storeRollIssueItemModels.size(); i++){
            if(storeRollIssueItemModels.get(i).getBarcodeNo().equals(cuttingStoreIssueModel.getBarcodeNo())){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialog() {
        if(!pDialog.isShowing()){
            pDialog.show();
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Leave this Page?")
                .setContentText("Do you want to leave this page? Unsaved changes will not be available.")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener(sDialog -> {
                    sDialog.cancel();
                    finish();
                })
                .setCancelClickListener(SweetAlertDialog::cancel)
                .show();
    }

    @Override
    public void onRemoveHeadClick(int position, View v) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Remove Bundle")
                .setContentText("Are you confirm to remove this bundle?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener(sDialog -> {
                    storeRollIssueItemModels.remove(position);
                    storeRollIssueRecyclerViewAdapter.notifyDataSetChanged();
                    sDialog.cancel();
                })
                .setCancelClickListener(sDialog -> sDialog.cancel())
                .show();
    }

    @Override
    public void onMoreHeadClick(int position, View v) {
        if(storeRollIssueItemModels.get(position).isStatus()){
            storeRollIssueItemModels.get(position).setStatus(false);
        }else{
            storeRollIssueItemModels.get(position).setStatus(true);
        }
        storeRollIssueRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}