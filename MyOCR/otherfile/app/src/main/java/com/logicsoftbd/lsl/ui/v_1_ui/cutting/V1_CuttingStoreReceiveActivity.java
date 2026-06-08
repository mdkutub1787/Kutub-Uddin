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
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_CuttingRollReceivePostResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_CuttingStoreReceive;
import com.logicsoftbd.lsl.data.network.v1_model.V1_CuttingStoreReceiveModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_CuttingStoreReceivePost;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_ScannerActivity;
import com.logicsoftbd.lsl.utils.ApiUtils;
import com.logicsoftbd.lsl.utils.DialogHelper;
import com.logicsoftbd.lsl.utils.VerticalSpacingItemDecorator;

import java.util.ArrayList;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class V1_CuttingStoreReceiveActivity extends AppCompatActivity implements View.OnClickListener, V1_StoreRollReceiveRecyclerViewAdapter.OnRemoveHeadListener, V1_StoreRollReceiveRecyclerViewAdapter.OnMoreHeadListener {
    private static final String TAG = "CuttingStoreReceiveActivity";
    private Button _locationScanBT, _bundleScanBT, _addBT, _refreshBT, _saveBT;
    private TextView _locationScanTV, _bundleScanTV;
    private EditText _rcvQtyET;
    private RecyclerView _cuttingStoreRecyclerView;
    private ProgressDialog pDialog;
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;
    private String base_url, userId, store_roll_receive_barcodeScan, store_roll_receive_location, room_rack = "";
    private int store_receive_scan_op = 0;
    private ArrayList<V1_CuttingStoreReceiveModel> storeRollReceiveItemModels = new ArrayList<>();
    private ArrayList<V1_CuttingStoreReceiveModel> cuttingStoreReceiveModelArrayDataList = new ArrayList<>();
    private V1_StoreRollReceiveRecyclerViewAdapter storeRollReceiveRecyclerViewAdapter;
    private V1_CuttingStoreReceiveModel cuttingStoreReceiveModel = new V1_CuttingStoreReceiveModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutting_store_receive);

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
        store_receive_scan_op = intent.getIntExtra("store_receive_scan_op", 0);
        if(store_receive_scan_op == 1){
            store_roll_receive_location = intent.getStringExtra("store_roll_receive_barcodeScan");
        }else {
            store_roll_receive_location = intent.getStringExtra("store_receive_room_rack_scan");
            store_roll_receive_barcodeScan = intent.getStringExtra("store_roll_receive_barcodeScan");
        }

        cuttingStoreReceiveModelArrayDataList = (ArrayList<V1_CuttingStoreReceiveModel>) intent.getSerializableExtra("store_receive_roll_data");

        if(cuttingStoreReceiveModelArrayDataList != null){
            storeRollReceiveItemModels = cuttingStoreReceiveModelArrayDataList;
            initRecyclerView();
        }

        if(store_roll_receive_barcodeScan != null || store_receive_scan_op == 1){
            String[] roomRackArray = store_roll_receive_location.split(Pattern.quote("***"), 2); // Limit to 2 parts

            if(roomRackArray.length > 1){
                _locationScanTV.setText("Rack: "+String.valueOf(roomRackArray[1]));
                room_rack = roomRackArray[0];
            }else {
                new SweetAlertDialog(V1_CuttingStoreReceiveActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Data not found.")
                        .setContentText("This barcode is not for location. Please scan right barcode.")
                        .setConfirmText("OK")
                        .show();

            }
        }

        if(store_roll_receive_barcodeScan != null && store_receive_scan_op == 2){
            getRequestForStoreData(store_roll_receive_barcodeScan);
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _cuttingStoreRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        _cuttingStoreRecyclerView.addItemDecoration(itemDecorator);
        storeRollReceiveRecyclerViewAdapter = new V1_StoreRollReceiveRecyclerViewAdapter(storeRollReceiveItemModels, this, this, this);
        _cuttingStoreRecyclerView.setAdapter(storeRollReceiveRecyclerViewAdapter);
    }

    private void init_ui() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        _locationScanTV = findViewById(R.id.locationScanTV);
        _bundleScanTV = findViewById(R.id.bundleScanTV);
        _rcvQtyET = findViewById(R.id.rcvQtyET);
        _cuttingStoreRecyclerView = findViewById(R.id.cuttingStoreRecyclerView);

        _locationScanBT = findViewById(R.id.locationScanBT);
        _locationScanBT.setOnClickListener(this);
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
        apiInterface.getCuttingStoreReceiveModelCall(store_roll_receive_barcodeScan).enqueue(new Callback<V1_CuttingStoreReceive>() {
            @SuppressLint({"LongLogTag", "SetTextI18n"})
            @Override
            public void onResponse(Call<V1_CuttingStoreReceive> call, Response<V1_CuttingStoreReceive> response) {
                hideDialog();
                Log.d(TAG, "onResponse: "+response.toString());
                try {
                    if(response.isSuccessful() && response.code() == 200){
                        if(response.body().getData() != null){
                            _bundleScanTV.setText("Barcode no: "+response.body().getData().getBarcodeNo()+", Bundle no: "+response.body().getData().getBundleNo());
                            cuttingStoreReceiveModel.setBundleNo(response.body().getData().getBundleNo());
                            cuttingStoreReceiveModel.setBarcodeNo(response.body().getData().getBarcodeNo());
                            cuttingStoreReceiveModel.setPoBreakdownId(response.body().getData().getPoBreakdownId());
                            cuttingStoreReceiveModel.setPoNumber(response.body().getData().getPoNumber());
                            cuttingStoreReceiveModel.setBuyerId(response.body().getData().getBuyerId());
                            cuttingStoreReceiveModel.setBuyerName(response.body().getData().getBuyerName());
                            cuttingStoreReceiveModel.setQcPassQnty(response.body().getData().getQcPassQnty());
                            cuttingStoreReceiveModel.setCompanyId(response.body().getData().getCompanyId());
                            cuttingStoreReceiveModel.setCompanyName(response.body().getData().getCompanyName());
                            cuttingStoreReceiveModel.setSizeId(response.body().getData().getSizeId());
                            cuttingStoreReceiveModel.setSizeName(response.body().getData().getSizeName());
                            cuttingStoreReceiveModel.setColorNumberId(response.body().getData().getColorNumberId());
                            cuttingStoreReceiveModel.setColorName(response.body().getData().getColorName());
                            cuttingStoreReceiveModel.setCuttingFloorId(response.body().getData().getCuttingFloorId());
                            cuttingStoreReceiveModel.setCuttingFloorName(response.body().getData().getCuttingFloorName());
                            cuttingStoreReceiveModel.setProductionQnty(response.body().getData().getProductionQnty());
                            cuttingStoreReceiveModel.setPubMsg(response.body().getData().getPubMsg());
                            if(isMatch()){
                                _bundleScanTV.setText("");
                                new SweetAlertDialog(V1_CuttingStoreReceiveActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Re-scan")
                                        .setContentText("Duplicate Data Detected!")
                                        .setConfirmText("OK")
                                        .show();
                            }
                        }else{
                            DialogHelper.showWarningDialog(V1_CuttingStoreReceiveActivity.this, "Message", response.body().getMsg());
                        }

                    }
                }catch (Exception e){
                    Log.d(TAG, "onResponse: "+e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<V1_CuttingStoreReceive> call, Throwable t) {
                hideDialog();
                DialogHelper.showErrorDialog(V1_CuttingStoreReceiveActivity.this, "Message", "Something went wrong.");
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.locationScanBT:
                if(_locationScanTV.getText().toString().isEmpty())
                    startScanning(1, 1);
                else
                    DialogHelper.showWarningDialog(V1_CuttingStoreReceiveActivity.this, "Message", "Please Refresh location first.");
                break;
            case R.id.bundleScanBT:
                if(_bundleScanTV.getText().toString().isEmpty()){
                    String ro_rac = _locationScanTV.getText().toString().trim();
                    if(!ro_rac.isEmpty()){
                        startScanning(2, 2);
                    }else {
                        DialogHelper.showWarningDialog(V1_CuttingStoreReceiveActivity.this, "Message", "Please Refresh location first.");
                    }
                }
                else
                    DialogHelper.showWarningDialog(V1_CuttingStoreReceiveActivity.this, "Message", "Please Refresh Barcode first.");
                break;
            case R.id.addBT:
                addDataToList();
                break;
            case R.id.saveBT:
                saveDataToServer();
                break;
            case R.id.refreshBT:
                _locationScanTV.setText("");
                _bundleScanTV.setText("");
                _rcvQtyET.setText("");
                _rcvQtyET.setText("");
                storeRollReceiveItemModels.clear();
                storeRollReceiveRecyclerViewAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void saveDataToServer() {
        ArrayList<V1_CuttingStoreReceivePost> cuttingStoreReceivePosts = new ArrayList<>();
        for(int i=0; i<storeRollReceiveItemModels.size(); i++){
            V1_CuttingStoreReceivePost cuttingStoreReceivePost = new V1_CuttingStoreReceivePost();
            cuttingStoreReceivePost.setBundleNo(storeRollReceiveItemModels.get(i).getBundleNo());
            cuttingStoreReceivePost.setBarcodeNo(storeRollReceiveItemModels.get(i).getBarcodeNo());
            cuttingStoreReceivePost.setPoBreakdownId(storeRollReceiveItemModels.get(i).getPoBreakdownId());
            cuttingStoreReceivePost.setPoNumber(storeRollReceiveItemModels.get(i).getPoNumber());
            cuttingStoreReceivePost.setBuyerId(storeRollReceiveItemModels.get(i).getBuyerId());
            cuttingStoreReceivePost.setCompanyId(storeRollReceiveItemModels.get(i).getCompanyId());
            cuttingStoreReceivePost.setQcPassQnty(storeRollReceiveItemModels.get(i).getQcPassQnty());
            cuttingStoreReceivePost.setSizeId(storeRollReceiveItemModels.get(i).getSizeId());
            cuttingStoreReceivePost.setColorNumberId(storeRollReceiveItemModels.get(i).getColorNumberId());
            cuttingStoreReceivePost.setColorName(storeRollReceiveItemModels.get(i).getColorName());
            cuttingStoreReceivePost.setCuttingFloorId(storeRollReceiveItemModels.get(i).getCuttingFloorId());
            cuttingStoreReceivePost.setFlRoRackDtlId(storeRollReceiveItemModels.get(i).getRackLocation());
            cuttingStoreReceivePost.setProductionQnty(storeRollReceiveItemModels.get(i).getProductionQnty());
            cuttingStoreReceivePost.setReceiveQntyKg(storeRollReceiveItemModels.get(i).getRcvqntykg());
            cuttingStoreReceivePost.setUserId(userId);
            cuttingStoreReceivePosts.add(cuttingStoreReceivePost);
        }

        Log.e("json", "json" + new Gson().toJson(cuttingStoreReceivePosts));
        showDialog();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, new Gson().toJson(cuttingStoreReceivePosts));
        apiInterface.saveCuttingRollReceiveCall(body).enqueue(new Callback<V1_CuttingRollReceivePostResponse>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<V1_CuttingRollReceivePostResponse> call, Response<V1_CuttingRollReceivePostResponse> response) {
                hideDialog();
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful() && response.code() == 200){
                    _locationScanTV.setText("");
                    _bundleScanTV.setText("");
                    _rcvQtyET.setText("");

                    SweetAlertDialog pDialog = new SweetAlertDialog(V1_CuttingStoreReceiveActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                    pDialog.setTitleText("Success");
                    pDialog.setContentText("Data successfully saved!");
                    pDialog.setCancelable(false);
                    pDialog.setConfirmText("Done")
                            .setConfirmClickListener(sweetAlertDialog -> {
                                storeRollReceiveItemModels.clear();
                                storeRollReceiveRecyclerViewAdapter.notifyDataSetChanged();
                                sweetAlertDialog.cancel();
                            });
                    pDialog.show();
                }else{
                    DialogHelper.showErrorDialog(V1_CuttingStoreReceiveActivity.this, "Message", "Something went wrong!");
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<V1_CuttingRollReceivePostResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                hideDialog();
                DialogHelper.showErrorDialog(V1_CuttingStoreReceiveActivity.this, "Message", "Something went wrong!");
            }
        });
    }

    private void addDataToList() {
        hideKeyboard();
        if(_bundleScanTV.getText().toString().isEmpty()){
            DialogHelper.showWarningDialog(V1_CuttingStoreReceiveActivity.this, "Message", "Please scan bundle first.");
        }else if(_rcvQtyET.getText().toString().isEmpty()){
            DialogHelper.showWarningDialog(V1_CuttingStoreReceiveActivity.this, "Message", "Please add receive quantity(kg) first.");
        }else{
            try {
                cuttingStoreReceiveModel.setRcvqntykg(_rcvQtyET.getText().toString().trim());
                cuttingStoreReceiveModel.setRackLocation(room_rack);
                cuttingStoreReceiveModel.setStatus(false);
                storeRollReceiveItemModels.add(cuttingStoreReceiveModel);
                storeRollReceiveRecyclerViewAdapter.notifyDataSetChanged();
                _rcvQtyET.setText("");
                _bundleScanTV.setText("");
            }catch (Exception e){

            }
        }
    }

    private void startScanning(int op, int barcode_roll_status) {
        Intent intent = new Intent(V1_CuttingStoreReceiveActivity.this, V1_ScannerActivity.class);
        intent.putExtra("qc", "cutting_store_roll_receive_v1");
        intent.putExtra("store_receive_scan_op", op);
        intent.putExtra("store_receive_roll_status", barcode_roll_status);
        intent.putExtra("store_receive_roll_data", storeRollReceiveItemModels);
        intent.putExtra("store_receive_room_rack_scan", store_roll_receive_location);
        startActivity(intent);
        finish();
    }

    private boolean isMatch(){
        for(int i=0; i<storeRollReceiveItemModels.size(); i++){
            if(storeRollReceiveItemModels.get(i).getBarcodeNo().equals(cuttingStoreReceiveModel.getBarcodeNo())){
                return true;
            }
        }
        return false;
    }

//    private void showAlertMessage(String msg, int i, int position) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(V1_CuttingStoreReceiveActivity.this);
//        builder.setTitle("Message")
//                .setMessage(msg)
//                .setCancelable(true)
//                .setPositiveButton("Ok", (dialog, which) -> {
//                    if(i == 1){
//                        storeRollReceiveItemModels.remove(position);
//                        storeRollReceiveRecyclerViewAdapter.notifyDataSetChanged();
//                    }else if(i == 2){
//                        storeRollReceiveItemModels.clear();
//                        storeRollReceiveRecyclerViewAdapter.notifyDataSetChanged();
//                    }else if(i == 3){
//                        _bundleScanTV.setText("");
//                        _rcvQtyET.setText("");
//                        storeRollReceiveRecyclerViewAdapter.notifyDataSetChanged();
//                    }
//                    dialog.dismiss();
//                });
//        AlertDialog dialog  = builder.create();
//        dialog.show();
//    }

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
                .setCancelClickListener(sDialog -> sDialog.cancel())
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
                    storeRollReceiveItemModels.remove(position);
                    storeRollReceiveRecyclerViewAdapter.notifyDataSetChanged();
                    sDialog.cancel();
                })
                .setCancelClickListener(sDialog -> sDialog.cancel())
                .show();
    }

    @Override
    public void onMoreHeadClick(int position, View v) {
        if(storeRollReceiveItemModels.get(position).getStatus() == true){
            storeRollReceiveItemModels.get(position).setStatus(false);
        }else{
            storeRollReceiveItemModels.get(position).setStatus(true);
        }
        storeRollReceiveRecyclerViewAdapter.notifyDataSetChanged();
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