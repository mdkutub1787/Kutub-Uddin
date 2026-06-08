package com.logicsoftbd.lsl.ui.v_1_ui.grey_fabric_roll_receive;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyRollReceiveItemModel;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_ScannerActivity;
import com.logicsoftbd.lsl.utils.DialogHelper;
import com.logicsoftbd.lsl.utils.VerticalSpacingItemDecorator;
import com.logicsoftbd.lsl.viewModel.GrayProductionViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class V1_GreyRollReceiveForPDODeviceActivity extends AppCompatActivity implements View.OnClickListener, V1_GreyRollReceiveRecyclerViewAdapter.OnMoreHeadListener, V1_GreyRollReceiveRecyclerViewAdapter.OnRemoveHeadListener {
    private static final String TAG = "GreyRollReceiveActivity";
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private DatePickerDialog datePickerDialog;
    private ProgressBar _progressBar;
    private TextView _roomRackScanTV, _barcodeScanTV, _challanScanTV, _receiveDateTV, _receiveIdTV, _totalWeightTV, _totalRollTV;
    private Button _roomRackScanBT, _barcodeScanBT, _challanScanBT, _submitBT, _saveBT, _refreshBT;
    private ImageButton _roomRackRefreshBT, _barcodeRefreshBT, _challanRefreshBT;
    private RecyclerView _greyRollReceiveRecyclerView;
    private V1_GreyRollReceiveRecyclerViewAdapter greyRollReceiveRecyclerViewAdapter;
    private ArrayList<V1_GreyRollReceiveItemModel> greyRollReceiveItemModels = new ArrayList<>();
    private ArrayList<V1_GreyRollReceiveItemModel> dataList = new ArrayList<>();
    private String grey_roll_barcodeScan, currentDate, user_id, delivery_number, room_rack, grey_room_rack_scan, grey_barcode_scan, grey_challan_scan;
    private int grey_scan_op = 0, company_id = 0;
    private GrayProductionViewModel grayProductionViewModel;
    //handler
    private String currentIdentifier;


    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grey_roll_receive_pdo_device);

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = _preferences.getString("login_userid", "");

        grayProductionViewModel = new ViewModelProvider(this).get(GrayProductionViewModel.class);

        init_ui();
        initRecyclerView();
        getDefaultData();

        IntentFilter filter = new IntentFilter("com.logicsoftbd.lsl.SCAN");
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(barcodeReceiver, filter);
    }

    private void getDefaultData() {
        Intent intent = getIntent();
        grey_roll_barcodeScan = intent.getStringExtra("grey_roll_barcodeScan");
        grey_scan_op = intent.getIntExtra("scan_op", 0);
        if(grey_scan_op != 1){
            grey_room_rack_scan = intent.getStringExtra("room_rack_scan");
        }else {
            grey_room_rack_scan = intent.getStringExtra("grey_roll_barcodeScan");
        }
        grey_barcode_scan = intent.getStringExtra("barcode_scan");
        grey_challan_scan = intent.getStringExtra("challan_scan");
        dataList = (ArrayList<V1_GreyRollReceiveItemModel>) intent.getSerializableExtra("grey_roll_data");

        _barcodeScanTV.setText(grey_barcode_scan);
        _challanScanTV.setText(grey_challan_scan);


        if(dataList != null){
            greyRollReceiveItemModels = dataList;
            initRecyclerView();
            calculateTotalRollWeight();
        }



        if(grey_roll_barcodeScan != null && grey_scan_op == 2){
            _barcodeScanTV.setText(grey_roll_barcodeScan);
            requestForBarcode(grey_roll_barcodeScan);
        }
        if(grey_roll_barcodeScan != null && grey_scan_op == 3){
            _challanScanTV.setText(grey_roll_barcodeScan);
            requestForChallan(grey_roll_barcodeScan);
        }
    }

    private void requestForChallan(String challanScan) {
        progressBarState();
        grayProductionViewModel.getGrayRollReceiveByChallanResponse(challanScan, room_rack).observe(this, apiResponse -> {
            if(apiResponse != null){
                try {
                    company_id = Integer.parseInt(apiResponse.getResultset().getMasterPart().getCompanyId());
                    delivery_number = apiResponse.getResultset().getMasterPart().getSysNumber();
                    for (int i = 0; i < apiResponse.getResultset().getDtlsPart().size(); i++) {
                        V1_GreyRollReceiveItemModel greyRollReceiveItemModel = new V1_GreyRollReceiveItemModel();
                        greyRollReceiveItemModel.setBarcodeNo(apiResponse.getResultset().getDtlsPart().get(i).getBarcodeNo());
                        greyRollReceiveItemModel.setRollNo(apiResponse.getResultset().getDtlsPart().get(i).getRollNo());
                        greyRollReceiveItemModel.setQnty(apiResponse.getResultset().getDtlsPart().get(i).getQnty().trim());
                        greyRollReceiveItemModel.setJobNo(apiResponse.getResultset().getDtlsPart().get(i).getJobNo());
                        greyRollReceiveItemModel.setBookingNo(apiResponse.getResultset().getDtlsPart().get(i).getBookingNo());
                        greyRollReceiveItemModel.setProgramNo(apiResponse.getResultset().getDtlsPart().get(i).getProgramNo());
                        greyRollReceiveItemModel.setConstruction(apiResponse.getResultset().getDtlsPart().get(i).getConstruction());
                        greyRollReceiveItemModel.setComposition(apiResponse.getResultset().getDtlsPart().get(i).getComposition());
                        greyRollReceiveItemModel.setGsm(apiResponse.getResultset().getDtlsPart().get(i).getGsm());
                        greyRollReceiveItemModel.setWidth(apiResponse.getResultset().getDtlsPart().get(i).getWidth());
                        greyRollReceiveItemModel.setColorName(apiResponse.getResultset().getDtlsPart().get(i).getColorName());
                        greyRollReceiveItemModel.setYarnLot(apiResponse.getResultset().getDtlsPart().get(i).getYarnLot());
                        greyRollReceiveItemModel.setStitchLength(apiResponse.getResultset().getDtlsPart().get(i).getStitchLength());
                        greyRollReceiveItemModel.setBrandId(apiResponse.getResultset().getDtlsPart().get(i).getBrandId());
                        greyRollReceiveItemModel.setSysNumber(apiResponse.getResultset().getDtlsPart().get(i).getSysNumber());
                        greyRollReceiveItemModel.setMachineNo(apiResponse.getResultset().getDtlsPart().get(i).getMachineName());
                        greyRollReceiveItemModel.setStatus(false);
                        greyRollReceiveItemModels.add(greyRollReceiveItemModel);
                    }
                    greyRollReceiveRecyclerViewAdapter.notifyDataSetChanged();
                    calculateTotalRollWeight();
                }catch (Exception e) {
                    DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
                }

            }else{
                _challanScanTV.setText("");
                DialogHelper.showErrorDialog(this, "Error", apiResponse.getResultset().getMasterPart().getMsg());
            }
        });
    }

    private void requestForBarcode(String barcodeScan) {
        progressBarState();
        grayProductionViewModel.getGrayRollReceiveByBarcodeResponse(barcodeScan, room_rack).observe(this, apiResponse -> {
            if(apiResponse != null){
                if(!apiResponse.getResultset().getMasterPart().getStatus().equals("Failed")){
                    if (apiResponse.getResultset().getDtlsPart().size() > 0) {
                        company_id = Integer.parseInt(apiResponse.getResultset().getMasterPart().getCompanyId());
                        delivery_number = apiResponse.getResultset().getMasterPart().getSysNumber();
                        for (int i = 0; i < apiResponse.getResultset().getDtlsPart().size(); i++) {
//                            if(dataList.size() > 0 && !greyRollReceiveItemModels.get(i).getSysNumber().equals(apiResponse.getResultset().getDtlsPart().get(i).getSysNumber())){
//                                DialogHelper.showWarningDialog(this, "Warning", "Multiple challan can't scan.");
//                                break;
//                            }
                            if(greyRollReceiveItemModels.size() > 0){
                                if(!greyRollReceiveItemModels.get(i).getSysNumber().equals(apiResponse.getResultset().getDtlsPart().get(i).getSysNumber())){
                                    DialogHelper.showWarningDialog(this, "Warning", "Multiple challan can't scan.");
                                    break;
                                }
                            }

                            boolean s = false;
//                            if(dataList.size() > 0){
//                                for(int j=0; j<greyRollReceiveItemModels.size(); j++){
//                                    if(greyRollReceiveItemModels.get(j).getBarcodeNo().equals(_barcodeScanTV.getText().toString())){
//                                        DialogHelper.showWarningDialog(this, "Warning", "This barcode is already scanned.");
//
//                                        s = true;
//                                        break;
//                                    }
//                                }
//                            }
                            if(greyRollReceiveItemModels.size() > 0){
                                for(int j=0; j<greyRollReceiveItemModels.size(); j++){
                                    if(greyRollReceiveItemModels.get(j).getBarcodeNo().equals(_barcodeScanTV.getText().toString())){
                                        DialogHelper.showWarningDialog(this, "Warning", "This barcode is already scanned.");

                                        s = true;
                                        break;
                                    }
                                }
                            }
                            if(s == true)
                                break;
                            V1_GreyRollReceiveItemModel greyRollReceiveItemModel = new V1_GreyRollReceiveItemModel();
                            greyRollReceiveItemModel.setBarcodeNo(apiResponse.getResultset().getDtlsPart().get(i).getBarcodeNo());
                            greyRollReceiveItemModel.setRollNo(apiResponse.getResultset().getDtlsPart().get(i).getRollNo());
                            greyRollReceiveItemModel.setQnty(apiResponse.getResultset().getDtlsPart().get(i).getQnty().trim());
                            greyRollReceiveItemModel.setJobNo(apiResponse.getResultset().getDtlsPart().get(i).getJobNo());
                            greyRollReceiveItemModel.setBookingNo(apiResponse.getResultset().getDtlsPart().get(i).getBookingNo());
                            greyRollReceiveItemModel.setProgramNo(apiResponse.getResultset().getDtlsPart().get(i).getProgramNo());
                            greyRollReceiveItemModel.setConstruction(apiResponse.getResultset().getDtlsPart().get(i).getConstruction());
                            greyRollReceiveItemModel.setComposition(apiResponse.getResultset().getDtlsPart().get(i).getComposition());
                            greyRollReceiveItemModel.setGsm(apiResponse.getResultset().getDtlsPart().get(i).getGsm());
                            greyRollReceiveItemModel.setWidth(apiResponse.getResultset().getDtlsPart().get(i).getWidth());
                            greyRollReceiveItemModel.setColorName(apiResponse.getResultset().getDtlsPart().get(i).getColorName());
                            greyRollReceiveItemModel.setYarnLot(apiResponse.getResultset().getDtlsPart().get(i).getYarnLot());
                            greyRollReceiveItemModel.setStitchLength(apiResponse.getResultset().getDtlsPart().get(i).getStitchLength());
                            greyRollReceiveItemModel.setBrandId(apiResponse.getResultset().getDtlsPart().get(i).getBrandId());
                            greyRollReceiveItemModel.setSysNumber(apiResponse.getResultset().getDtlsPart().get(i).getSysNumber());
                            greyRollReceiveItemModel.setMachineNo(apiResponse.getResultset().getDtlsPart().get(i).getMachineName());
                            greyRollReceiveItemModel.setStatus(false);
                            greyRollReceiveItemModels.add(greyRollReceiveItemModel);
                        }
                        greyRollReceiveRecyclerViewAdapter.notifyDataSetChanged();
                        calculateTotalRollWeight();
                    }
                }else{
                    _barcodeScanTV.setText("");
                    DialogHelper.showErrorDialog(this, "Error", apiResponse.getResultset().getMasterPart().getMsg());
                }
            }else{
                DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
            }
        });
    }

    private void calculateTotalRollWeight() {
        double totalWeight = 0;
        for(int i=0; i<greyRollReceiveItemModels.size(); i++){
            totalWeight += Double.parseDouble(greyRollReceiveItemModels.get(i).getQnty());
        }
        _totalWeightTV.setText("Total Weight: "+totalWeight);
        if(greyRollReceiveItemModels != null && greyRollReceiveItemModels.size() > 0) {
            _totalRollTV.setText("Total Roll : " + greyRollReceiveItemModels.size());
        }
    }

    private void postDataToServer() {
        JSONObject save_obj = new JSONObject();
        JSONObject data_obj = new JSONObject();
        JSONObject master_obj = new JSONObject();
        JSONArray dtls_arr = new JSONArray();

        try {
            save_obj.put("status",true);
            master_obj.put("COMPANY_ID", company_id);
            master_obj.put("USER_ID", user_id);
            master_obj.put("RECEIVE_DATE", _receiveDateTV.getText().toString());
            master_obj.put("DELIVERY_NUMBER", delivery_number);
            master_obj.put("STORE_ROOM_RACK_ID", room_rack);

            data_obj.put("MasterPart",master_obj);
            for (int i = 0; i < greyRollReceiveItemModels.size(); i++) {
                JSONObject dtls_obj = new JSONObject();
                dtls_obj.put("BARCODE_NO", greyRollReceiveItemModels.get(i).getBarcodeNo());
                dtls_arr.put(dtls_obj);
            }

            data_obj.put("DtlsPart",dtls_arr);
            save_obj.put("resultset", data_obj);
            Log.d(TAG, "postDataToServer: ########"+save_obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, save_obj.toString());

        progressBarState();
        grayProductionViewModel.postGrayRollReceiveResponse(body).observe(this, apiResponse -> {
            if(apiResponse != null){
                refreshData(3);
                DialogHelper.showSuccessDialog(this, "Success", apiResponse.getResultset().getMasterPart().getMsg());
            }else{
                DialogHelper.showSuccessDialog(this, "Error", "Failed, Please try again.");
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _greyRollReceiveRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        _greyRollReceiveRecyclerView.addItemDecoration(itemDecorator);
        greyRollReceiveRecyclerViewAdapter = new V1_GreyRollReceiveRecyclerViewAdapter(greyRollReceiveItemModels, this, this, this);
        _greyRollReceiveRecyclerView.setAdapter(greyRollReceiveRecyclerViewAdapter);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true); // This ensures that the last item stays at the bottom
        _greyRollReceiveRecyclerView.setLayoutManager(layoutManager);
    }

    private void init_ui() {
        _progressBar = findViewById(R.id.progressBar);
        _greyRollReceiveRecyclerView = findViewById(R.id.greyRollReceiveRecyclerView);
        _roomRackScanTV = findViewById(R.id.roomRackScanTV);
        _barcodeScanTV = findViewById(R.id.barcodeScanTV);
        _challanScanTV = findViewById(R.id.challanScanTV);
        _totalWeightTV = findViewById(R.id.totalWeightTV);
        _totalRollTV = findViewById(R.id.totalRollTV);
        _receiveDateTV = findViewById(R.id.receiveDateTV);
        _receiveDateTV.setOnClickListener(this);
        _roomRackScanBT = findViewById(R.id.roomRackScanBT);
        _roomRackScanBT.setOnClickListener(this);
        _barcodeScanBT = findViewById(R.id.barcodeScanBT);
        _barcodeScanBT.setOnClickListener(this);
        _challanScanBT = findViewById(R.id.challanScanBT);
        _challanScanBT.setOnClickListener(this);
        _submitBT = findViewById(R.id.submitBT);
        _submitBT.setOnClickListener(this);
        _saveBT = findViewById(R.id.saveBT);
        _saveBT.setOnClickListener(this);
        _refreshBT = findViewById(R.id.refreshBT);
        _refreshBT.setOnClickListener(this);
        _roomRackRefreshBT = findViewById(R.id.roomRackRefreshBT);
        _roomRackRefreshBT.setOnClickListener(this);
        _barcodeRefreshBT = findViewById(R.id.barcodeRefreshBT);
        _barcodeRefreshBT.setOnClickListener(this);
        _challanRefreshBT = findViewById(R.id.challanRefreshBT);
        _challanRefreshBT.setOnClickListener(this);

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = simpleDateFormat.format(calendar.getTime());
        _receiveDateTV.setText(currentDate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.roomRackScanBT:
                if(_roomRackScanTV.getText().toString().isEmpty())
                {
                    currentIdentifier = "location";
                    _roomRackScanBT.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
                    _barcodeScanBT.setBackgroundColor(Color.parseColor("#324F5C"));
                    _challanScanBT.setBackgroundColor(Color.parseColor("#324F5C"));
//                    startScanning(1, 1);
                }
                else
                    DialogHelper.showWarningDialog(this, "Warning","Please Refresh Room/Rack/Self first.");
                break;
            case R.id.barcodeScanBT:
                String ro_rac = _roomRackScanTV.getText().toString().trim();
                if(!ro_rac.isEmpty()){
                    currentIdentifier = "bundle";
                    _barcodeScanBT.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
                    _roomRackScanBT.setBackgroundColor(Color.parseColor("#324F5C"));
                    _challanScanBT.setBackgroundColor(Color.parseColor("#324F5C"));
//                    startScanning(2, 2);
                }else {
                    DialogHelper.showWarningDialog(this, "Warning","Please scan Room/Rack/Shelf/Bin first");
                }
                break;
            case R.id.challanScanBT:
                String ro_rac_1 = _roomRackScanTV.getText().toString().trim();
                if(!ro_rac_1.isEmpty()){
                    refreshData(2);
                    currentIdentifier = "challan";
                    _challanScanBT.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
                    _roomRackScanBT.setBackgroundColor(Color.parseColor("#324F5C"));
                    _barcodeScanBT.setBackgroundColor(Color.parseColor("#324F5C"));
//                    startScanning(3, 3);
                }else {
                    DialogHelper.showWarningDialog(this, "Warning","Please scan Room/Rack/Shelf/Bin first");
                }
                break;
            case R.id.roomRackRefreshBT:
                _roomRackScanTV.setText("");
                break;
            case R.id.barcodeRefreshBT:
                _barcodeScanTV.setText("");
                break;
            case R.id.challanRefreshBT:
                refreshData(2);
                break;
            case R.id.refreshBT:
                refreshData(3);
                break;
            case R.id.saveBT:
                if(greyRollReceiveItemModels.size() > 0){
                    postDataToServer();
                }
                break;
            case R.id.receiveDateTV:
                datepicker();
                break;
        }
    }

    private void datepicker() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    _receiveDateTV.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void refreshData(int status) {
        if(status == 3){
            greyRollReceiveItemModels.clear();
            _roomRackScanTV.setText("");
            _barcodeScanTV.setText("");
            _challanScanTV.setText("");
            _totalRollTV.setText("");
            _totalWeightTV.setText("");
            greyRollReceiveRecyclerViewAdapter.notifyDataSetChanged();
        }else if(status == 2){
            greyRollReceiveItemModels.clear();
            _barcodeScanTV.setText("");
            _challanScanTV.setText("");
            greyRollReceiveRecyclerViewAdapter.notifyDataSetChanged();
        }

    }

    private void startScanning(int op, int grey_roll_status) {
        Intent intent = new Intent(this, V1_ScannerActivity.class);
        intent.putExtra("qc", "grey_roll_receive_v1");
        intent.putExtra("scan_op", op);
        intent.putExtra("grey_roll_status", grey_roll_status);
        intent.putExtra("grey_roll_data", greyRollReceiveItemModels);
        intent.putExtra("room_rack_scan", grey_room_rack_scan);
        intent.putExtra("barcode_scan", _barcodeScanTV.getText().toString());
        intent.putExtra("challan_scan", _challanScanTV.getText().toString());
        startActivity(intent);
    }

    private void progressBarState() {
        grayProductionViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                _progressBar.setVisibility(View.VISIBLE);
            } else {
                _progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showAlertMessage(String msg, int i, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(V1_GreyRollReceiveForPDODeviceActivity.this);
        builder.setTitle("Message")
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("Ok", (dialog, which) -> {
                    if(i == 1){
                        greyRollReceiveItemModels.remove(position);
                        greyRollReceiveRecyclerViewAdapter.notifyDataSetChanged();
                        calculateTotalRollWeight();
                        dialog.dismiss();
                    }else{
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog  = builder.create();
        dialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onMoreHeadClick(int position, View v) {
        if(greyRollReceiveItemModels.get(position).getStatus() == true){
            greyRollReceiveItemModels.get(position).setStatus(false);
        }else{
            greyRollReceiveItemModels.get(position).setStatus(true);
        }
        greyRollReceiveRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRemoveHeadClick(int position, View v) {
        showAlertMessage("Are you confirm to remove this barcode?", 1, position);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fabric, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            if(greyRollReceiveItemModels.size() > 0){
                postDataToServer();
            }
            return true;
        }
        else if (id == R.id.action_new){
            refreshData(3);
        } else if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

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
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(barcodeReceiver);
    }


    private BroadcastReceiver barcodeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Intent Received");

            if (intent.hasExtra("com.symbol.datawedge.data_string")) {
                String barcodeData = intent.getStringExtra("com.symbol.datawedge.data_string");

                String identifier = intent.getStringExtra("identifier");
                if (identifier == null || identifier.isEmpty()) {
                    identifier = "default";
                }

                if (currentIdentifier != null) {
                    switch (currentIdentifier) {
                        case "location":{
                            if(barcodeData != null){
                                String[] roomRackArray = barcodeData.split(Pattern.quote("***"));
                                if(roomRackArray.length <=1){
                                    DialogHelper.showWarningDialog(V1_GreyRollReceiveForPDODeviceActivity.this, "Message", "Invalid location.");
                                }else{
                                    if(roomRackArray.length >= 1){
                                        _roomRackScanTV.setText(String.valueOf(roomRackArray[1]));
                                        room_rack = roomRackArray[0];
                                    }else {
                                        room_rack = roomRackArray[0];
                                        _roomRackScanTV.setText(String.valueOf(roomRackArray[0]));
                                    }
                                }
                            }
                        }
                        break;
                        case "bundle":
                            if(barcodeData != null){
                                _barcodeScanTV.setText(barcodeData);
                                requestForBarcode(barcodeData);
                            }
                            break;
                        case "challan":
                            if(barcodeData != null ){
                                _challanScanTV.setText(barcodeData);
                                requestForChallan(barcodeData);
                            }
                            break;
                        default:
                            Log.d(TAG, "Unknown identifier: " + currentIdentifier);
                            break;
                    }

                    Log.d(TAG, "Scanned: " + barcodeData + ", Identifier: " + currentIdentifier);
                } else {
                    Log.d(TAG, "No identifier set. Ignoring barcode scan.");
                }
            } else {
                Log.d(TAG, "No barcode data found in the intent.");
            }
        }
    };
}