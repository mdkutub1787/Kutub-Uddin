package com.logicsoftbd.lsl.ui.v_1_ui.yarn_rfid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.Rfid;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GRNWiseYarnModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GRNWiseYarnSaveObject;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GRNWiseYarnSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_YarnRFIDModel;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_ScannerActivity;
import com.logicsoftbd.lsl.utils.ApiUtils;
import com.logicsoftbd.lsl.utils.VerticalSpacingItemDecorator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class V1_YarnRfidReceiveActivity extends AppCompatActivity implements View.OnClickListener, V1_YarnRFIDRecyclerAdapter.OnDeleteSelectListener {
    private static final String TAG = "YarnRfidReceiveActivity";
    private Button _grnScanBT, _locationScanBT, _rfidScanBT, _refreshBT, _saveBT;
    private TextView _grnScanTV, _locationScanTV, _rfidScanTV;
    private RecyclerView _yarnRfidRecyclerView;
    private V1_YarnRFIDRecyclerAdapter yarnRFIDRecyclerAdapter;
    private ArrayList<V1_YarnRFIDModel> _yarnRFIDModelArrayList = new ArrayList<>();
    private ArrayList<V1_YarnRFIDModel> yarnRFIDModelArrayList = new ArrayList<>();
    private ArrayList<String> rfidList = new ArrayList<>();
    private ArrayList<String> rfidMSTList = new ArrayList<>();
    private V1_GRNWiseYarnModel grnWiseYarnModel = new V1_GRNWiseYarnModel();
    private V1_YarnRFIDModel yarnRFIDModel = new V1_YarnRFIDModel();
    private ProgressDialog pDialog;
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private int yarn_rfid_receive_scan_op = 0;
    private String base_url, userId, _grn_barcodeScan, _yarn_rfid_receive_roll_data, _locationScan, _rfidScan, room_rack = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yarn_rfid_receive);

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
        yarn_rfid_receive_scan_op = intent.getIntExtra("yarn_rfid_receive_scan_op", 0);
        _yarn_rfid_receive_roll_data = intent.getStringExtra("yarn_rfid_receive_roll_data");
        _grn_barcodeScan = intent.getStringExtra("grn_barcodeScan");
        _locationScan = intent.getStringExtra("rfid_location");

        if(yarn_rfid_receive_scan_op == 1){
            _grn_barcodeScan = intent.getStringExtra("yarn_rfid_issue_barcodeScan");
        }else if(yarn_rfid_receive_scan_op == 2) {
            _locationScan = intent.getStringExtra("yarn_rfid_issue_barcodeScan");
        }else{
            _rfidScan = intent.getStringExtra("yarn_rfid_issue_barcodeScan");
        }

        _grnScanTV.setText(_grn_barcodeScan);

        yarnRFIDModelArrayList = (ArrayList<V1_YarnRFIDModel>) intent.getSerializableExtra("yarn_rfid_receive_rfid_data");
        if(yarnRFIDModelArrayList != null){
            _yarnRFIDModelArrayList = yarnRFIDModelArrayList;
        }

        grnWiseYarnModel = new Gson().fromJson(_yarn_rfid_receive_roll_data, V1_GRNWiseYarnModel.class);


        if(_locationScan != null && !_locationScan.equals("")){
            String[] roomRackArray = _locationScan.split(Pattern.quote("***"), 2);

            if(roomRackArray.length > 1){
                _locationScanTV.setText(roomRackArray[1]);
                room_rack = roomRackArray[0];
            }else {
                _locationScan = "";
                _locationScanTV.setText("");
                warningAlertDialog("Warning Message", "This barcode is not for location. Please scan right barcode.");

            }
        }

        if(_grn_barcodeScan != null && yarn_rfid_receive_scan_op == 1){
            getRequestForStoreData(_grn_barcodeScan);
        }

        if(yarn_rfid_receive_scan_op == 3){
            boolean s = false;
            if(_yarnRFIDModelArrayList.size() > 0){
                for(int j=0; j<_yarnRFIDModelArrayList.size(); j++){
                    if(_yarnRFIDModelArrayList.get(j).getRfidItem().equals(_rfidScan)){
                        s = true;
                        warningAlertDialog("Warning Message", "This barcode is already scanned.");
                        break;
                    }
                }
            }
            if(!s){
                String currentDataTime = currentDateTime();
                yarnRFIDModel.setRfidItem(_rfidScan);
                yarnRFIDModel.setDateTime(currentDataTime);
                _yarnRFIDModelArrayList.add(yarnRFIDModel);
            }

        }

        if(_yarnRFIDModelArrayList != null){
            initRecyclerView();
        }
    }
    private void getRequestForStoreData(String grnBarcodeScan) {
        showDialog();
        apiInterface.getGrnWiseYarnCall(grnBarcodeScan).enqueue(new Callback<V1_GRNWiseYarnModel>() {
            @Override
            public void onResponse(Call<V1_GRNWiseYarnModel> call, Response<V1_GRNWiseYarnModel> response) {
                hideDialog();
                Log.d(TAG, "onResponse: "+response.toString());
                try {
                    if(response.isSuccessful() && response.code() == 200 &&
                            (response.body().getData().getRfids() != null && response.body().getData().getRfids().size() > 0)){
                        _yarn_rfid_receive_roll_data = new Gson().toJson(response.body());
                    }else{
                        _grn_barcodeScan = "";
                        _grnScanTV.setText("");
                        warningAlertDialog("Warning Message", "RFID data not found.");
                    }
                }catch (Exception e){
                    _grn_barcodeScan = "";
                    _grnScanTV.setText("");
                    errorAlertDialog("Error Message", "Data not found.");
                }
            }

            @Override
            public void onFailure(Call<V1_GRNWiseYarnModel> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                hideDialog();
                errorAlertDialog("Error Message", "Something went wrong!");
            }
        });


    }
    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _yarnRfidRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        _yarnRfidRecyclerView.addItemDecoration(itemDecorator);
        yarnRFIDRecyclerAdapter = new V1_YarnRFIDRecyclerAdapter(_yarnRFIDModelArrayList, this, this);
        _yarnRfidRecyclerView.setAdapter(yarnRFIDRecyclerAdapter);
    }

    private void init_ui() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        _grnScanTV = findViewById(R.id.grnScanTV);
        _locationScanTV = findViewById(R.id.locationScanTV);
        _rfidScanTV = findViewById(R.id.rfidScanTV);
        _yarnRfidRecyclerView = findViewById(R.id.yarnRfidRecyclerView);

        _locationScanBT = findViewById(R.id.locationScanBT);
        _locationScanBT.setOnClickListener(this);
        _grnScanBT = findViewById(R.id.grnScanBT);
        _grnScanBT.setOnClickListener(this);
        _rfidScanBT = findViewById(R.id.rfidScanBT);
        _rfidScanBT.setOnClickListener(this);
        _refreshBT = findViewById(R.id.refreshBT);
        _refreshBT.setOnClickListener(this);
        _saveBT = findViewById(R.id.saveBT);
        _saveBT.setOnClickListener(this);
    }

    private void startScanning(int op) {
        Intent intent = new Intent(V1_YarnRfidReceiveActivity.this, V1_ScannerActivity.class);
        intent.putExtra("qc", "yarn_rfid_receive_v1");
        intent.putExtra("yarn_rfid_receive_scan_op", op);
        intent.putExtra("yarn_rfid_receive_roll_data", _yarn_rfid_receive_roll_data);
        intent.putExtra("yarn_rfid_grn_barcodeScan", _grn_barcodeScan);
        intent.putExtra("yarn_rfid_location_barcodeScan", _locationScan);
        intent.putExtra("yarn_rfid_rfid_barcodeScan", _rfidScan);
        intent.putExtra("yarn_rfid_receive_rfid_data", _yarnRFIDModelArrayList);
        intent.putExtra("grn_barcodeScan", _grn_barcodeScan);
        intent.putExtra("rfid_location", _locationScan);
        startActivity(intent);
        finish();
    }

    private void saveDataToServer() {

        for(int i=0; i<_yarnRFIDModelArrayList.size(); i++){
            rfidList.add(_yarnRFIDModelArrayList.get(i).getRfidItem());
        }

        if(grnWiseYarnModel.getData().getRfids() != null && grnWiseYarnModel.getData().getRfids().size() > 0){
            for(int i=0; i<grnWiseYarnModel.getData().getRfids().size(); i++){
                rfidMSTList.add(grnWiseYarnModel.getData().getRfids().get(i).getRfidNo());
            }
        }

        Set<String> set1 = new HashSet<>(rfidList);
        Set<String> set2 = new HashSet<>(rfidMSTList);

        boolean isEqual = set2.containsAll(set1);

        if (isEqual) {
            if(rfidList.size() > 0){
                prepareDataForSave();
            }else{
                warningAlertDialog("Warning Message", "Please scan RFID first.");
            }
        } else {
            warningAlertDialog("Warning Message", "Selected RFID doesn't match. Please check again.");
        }
    }


    private void prepareDataForSave() {
        V1_GRNWiseYarnSaveObject grnWiseYarnSaveObject = new V1_GRNWiseYarnSaveObject();
        grnWiseYarnSaveObject.setMrrId(String.valueOf(grnWiseYarnModel.getData().getMrrId()));
        grnWiseYarnSaveObject.setFloorRoomRackDtlsId(room_rack);
        grnWiseYarnSaveObject.setUserId(userId);
        ArrayList<Rfid> _rfidList = new ArrayList<>();
        for(int i=0; i<rfidList.size(); i++){
            Rfid rfid = new Rfid();
            rfid.setRfidNo(rfidList.get(i));
            _rfidList.add(rfid);
        }

        grnWiseYarnSaveObject.setRfids(_rfidList);

        Log.e("json", "json" + new Gson().toJson(grnWiseYarnSaveObject));
        showDialog();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, new Gson().toJson(grnWiseYarnSaveObject));
        showDialog();
        apiInterface.saveGRNYarnReceiceSaveCall(body).enqueue(new Callback<V1_GRNWiseYarnSaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<V1_GRNWiseYarnSaveResponse> call, @NonNull Response<V1_GRNWiseYarnSaveResponse> response) {
                hideDialog();
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful() && response.code() == 200 && Objects.requireNonNull(response.body()).getStatus() == 200){
                    try {
                        successAlertDialog();
                        resetData();
                    }catch (Exception e){
                        warningAlertDialog("Warning Message", "Something went wrong.");
                    }
                }else{
                    warningAlertDialog("Warning Message", "Something went wrong.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<V1_GRNWiseYarnSaveResponse> call, @NonNull Throwable t) {
                hideDialog();
                errorAlertDialog("Error message", "Something went wrong.");
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.grnScanBT:
                if(_grnScanTV.getText().toString().isEmpty())
                    startScanning(1);
                else
                    warningAlertDialog("Warning message", "Please Refresh GRN first.");
                break;
            case R.id.locationScanBT:
                if(_locationScanTV.getText().toString().isEmpty()){
                    String grn = _grnScanTV.getText().toString().trim();
                    if(!grn.isEmpty()){
                        if(_yarn_rfid_receive_roll_data != null){
                            startScanning(2);
                        }else{
                            warningAlertDialog("Warning message", "Data not found for GRN.");
                        }

                    }else {
                        warningAlertDialog("Warning message", "Please Refresh GRN first.");
                    }
                }
                else
                    warningAlertDialog("Warning message", "Please Refresh location first.");
                break;
            case R.id.rfidScanBT:
                if(_rfidScanTV.getText().toString().isEmpty()){
                    String grn = _grnScanTV.getText().toString().trim();
                    String location = _locationScanTV.getText().toString().trim();
                    if(!grn.isEmpty() && !location.isEmpty()){
                        startScanning(3);
                    }else {
                        warningAlertDialog("Warning message", "Please scan GRN & location first.");
                    }
                }
                else
                    warningAlertDialog("Warning message", "Please Refresh RFID first.");
                break;
            case R.id.saveBT:
                saveDataToServer();
                break;
            case R.id.refreshBT:
                resetData();
                break;
        }
    }

    private void resetData() {
        _locationScanTV.setText("");
        _grnScanTV.setText("");
        _rfidScanTV.setText("");
        _grn_barcodeScan = "";
        _locationScan = "";
        _rfidScan = "";
        yarn_rfid_receive_scan_op = 0;
        yarnRFIDModelArrayList.clear();
        yarnRFIDRecyclerAdapter.notifyDataSetChanged();
    }

    private String currentDateTime(){
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String currentDate = simpleDateFormat.format(calendar.getTime());
        return currentDate;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDeleteClick(int position, View v) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Remove item?")
                .setContentText("Do you want to remove this item?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener(sDialog -> {
                    sDialog.cancel();
                    _yarnRFIDModelArrayList.remove(position);
//                    rfidList.remove(position);
                    yarnRFIDRecyclerAdapter.notifyDataSetChanged();
                })
                .setCancelClickListener(SweetAlertDialog::cancel)
                .show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void successAlertDialog(){
        new SweetAlertDialog(V1_YarnRfidReceiveActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success Message")
                .setContentText("Date save successfully.")
                .setConfirmText("OK")
                .show();
    }
    private void warningAlertDialog(String title, String message){
        new SweetAlertDialog(V1_YarnRfidReceiveActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText("OK")
                .show();
    }
    private void errorAlertDialog(String title, String message){
        new SweetAlertDialog(V1_YarnRfidReceiveActivity.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText("OK")
                .show();
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

}