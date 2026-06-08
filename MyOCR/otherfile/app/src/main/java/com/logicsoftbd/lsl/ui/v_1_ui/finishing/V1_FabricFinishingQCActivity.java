package com.logicsoftbd.lsl.ui.v_1_ui.finishing;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FabricFinishQCUpdateModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FinishingMachineModelResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FinishingQCModelResponse;
import com.logicsoftbd.lsl.posPrinter.connection.DeviceConnection;
import com.logicsoftbd.lsl.posPrinter.connection.bluetooth.BluetoothConnection;
import com.logicsoftbd.lsl.posPrinter.connection.bluetooth.BluetoothPrintersConnections;
import com.logicsoftbd.lsl.ui.v_1_ui.about.PrintTestActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_ScannerActivity;
import com.logicsoftbd.lsl.utils.DialogHelper;
import com.logicsoftbd.lsl.utils.VerticalSpacingItemDecorator;
import com.logicsoftbd.lsl.utils.async.AsyncBluetoothEscPosPrint;
import com.logicsoftbd.lsl.utils.async.AsyncEscPosPrint;
import com.logicsoftbd.lsl.utils.async.AsyncEscPosPrinter;
import com.logicsoftbd.lsl.viewModel.FinishProductionViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class V1_FabricFinishingQCActivity extends AppCompatActivity implements View.OnClickListener, V1_FabricFinishQCRecyclerViewAdapter.OnMoreHeadListener {
    private static final String TAG = "V1_FabricFinishingQCAct";
    private ProgressBar _progressBar;
    private TextView _batchScanTV, _srBookingTV, _fabColorTV, _posPrinterSelectTV, _barcodeTV;
    private EditText _greyUsedET, _actualDiaET, _actualGSMET, _fabricWeightET, _commentsET, _rollNoET;
    private Spinner _machineSpinner, _styleRefSpinner, _stickerTypeSpinner, _fabDescSpinner;
    private String base_url, userID, userName, currentDate, scannedBarcode, batch_no, savedPrinter, jobNO, jobId, prodId, styleRefNO, _fabDescId, descriptopn, responseBarcodeNo, buyerName, systmeBookingNo,
            styleName, bookingGSM, bookingDIA, fabCons, fabType, companyName, locationName, greyUsed, actualDia, actualGSM, fabricWeight, rollNo, comments;
    private Button _stickerPrintBT, _saveBtn, _refreshBtn, _batchScanBT, _editBtn;
    private RecyclerView _finishQCRecyclerView;
    private LinearLayout _printerLayout;
    private ImageButton _batchPrintBT;
    private ImageView _back, _printerImage;
    private LinearLayout _insertScanFormLayout;
    private CardView _entryFormLayout;
    private Integer _machineId = 0, _stickerId = 0, scan_op = 0, _styleRefId = 0, _isUpdate = 0;
    private FinishProductionViewModel finishProductionViewModel;
    private SharedPreferences _preferences;
    private BluetoothConnection selectedDevice;
    private V1_FabricFinishQCRecyclerViewAdapter fabricFinishQCRecyclerViewAdapter;
    private ArrayList<V1_FabricFinishQCUpdateModel.ResultSet> fabricFinishQCModelList = new ArrayList<>();
    private  V1_FinishingQCModelResponse finishingQCModelResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_fabric_finishing_qc);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        base_url = (_preferences.getString("base_url", ""));
        userID = _preferences.getString("login_userid", "");
        userName = _preferences.getString("login_username", "");
        savedPrinter = _preferences.getString("saved_printer", "");
        finishProductionViewModel = new ViewModelProvider(this).get(FinishProductionViewModel.class);

        init_ui();

        getDefaultData();

    }

    private void getFinishingQCData(String batch_no) {
        progressBarState();
        finishProductionViewModel.getFinishingingQCDataResponse(batch_no).observe(this, apiResponse -> {
            try {
                if (apiResponse == null || apiResponse.getResultSet() == null) {
                    String errorMessage = (apiResponse != null) ? apiResponse.getMsg() : "Something went wrong.";
                    DialogHelper.showErrorDialog(this, "Error", errorMessage);
                    return;
                }
                finishingQCModelResponse = apiResponse;
                setData(apiResponse);
                getMachineData();
            } catch (Exception e) {
                Log.e(TAG, "fetchBatchDataResponse: Error occurred", e);
                DialogHelper.showErrorDialog(this, "Error", "An unexpected error occurred.");
            }
        });
    }

    private void getMachineData() {
        finishProductionViewModel.getFinishingMachineResponse().observe(this, apiResponse -> {
            try {
                if (apiResponse == null || apiResponse.getResultSet() == null) {
                    String errorMessage = (apiResponse != null) ? apiResponse.getMsg() : "Something went wrong.";
                    DialogHelper.showErrorDialog(this, "Error", errorMessage);
                    return;
                }
                setMachineAdapter(apiResponse);
            } catch (Exception e) {
                Log.e(TAG, "fetchBatchDataResponse: Error occurred", e);
                DialogHelper.showErrorDialog(this, "Error", "An unexpected error occurred.");
            }
        });
    }

    private void setMachineAdapter(V1_FinishingMachineModelResponse apiResponse) {
        List<String> _machineNames = new ArrayList<>();
        List<String> _machineIds = new ArrayList<>();
        for (V1_FinishingMachineModelResponse.ResultSet machine : apiResponse.getResultSet()) {
            _machineNames.add(machine.getMachineNo());
            _machineIds.add(String.valueOf(machine.getId()));
        }
        _machineNames.add(0, "--Select--");
        _machineIds.add(0, "0");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, _machineNames);

        _machineSpinner.setAdapter(adapter);
        _machineSpinner.setSelection(_machineIds.indexOf(String.valueOf(_machineId)));

        _machineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _machineId = Integer.parseInt(String.valueOf(_machineIds.get(position)));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setData(V1_FinishingQCModelResponse apiResponse) {
        _srBookingTV.setText(apiResponse.getResultSet().getBookingNo());
        _fabColorTV.setText(apiResponse.getResultSet().getColorName());
        systmeBookingNo = apiResponse.getResultSet().getBookingNo();

        if(_isUpdate == 1){
            _greyUsedET.setText(greyUsed);
            _actualDiaET.setText(actualDia);
            _actualGSMET.setText(actualGSM);
            _fabricWeightET.setText(fabricWeight);
            _rollNoET.setText(rollNo);
            _commentsET.setText(comments);
        }

        List<String> _fabDescNames = new ArrayList<>();
        List<String> _fabDescIds = new ArrayList<>();
        List<String> _fabric_type = new ArrayList<>();
        List<String> _fabric_composition = new ArrayList<>();
        List<String> _prod_id = new ArrayList<>();
        List<String> _detar_id = new ArrayList<>();
        List<String> _booking_GSM = new ArrayList<>();
        List<String> _booking_DIA = new ArrayList<>();

        for (V1_FinishingQCModelResponse.Dtl fabItem : apiResponse.getResultSet().getDtls()) {
            _fabDescNames.add(fabItem.getItemDescription());
            _fabDescIds.add(String.valueOf(fabItem.getDetarId()));
            _fabric_type.add(String.valueOf(fabItem.getFabricType()));
            _fabric_composition.add(String.valueOf(fabItem.getFabricComposition()));
            _prod_id.add(String.valueOf(fabItem.getProdId()));
            _detar_id.add(String.valueOf(fabItem.getDetarId()));
            _booking_GSM.add(String.valueOf(fabItem.getBooking_gsm()));
            _booking_DIA.add(String.valueOf(fabItem.getBooking_dia()));
        }
        _fabDescNames.add(0, "--Select--");
        _fabDescIds.add(0, "0");
        _fabric_type.add(0, "0");
        _fabric_composition.add(0, "0");
        _prod_id.add(0, "0");
        _detar_id.add(0, "0");
        _booking_DIA.add(0, "0");
        _booking_GSM.add(0, "0");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, _fabDescNames);

        _fabDescSpinner.setAdapter(adapter);
        _fabDescSpinner.setSelection(_fabDescNames.indexOf(String.valueOf(_fabDescId)));

        _fabDescSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    _fabDescId = _fabDescIds.get(position);
                    fabCons = _fabric_composition.get(position);
                    fabType = _fabric_type.get(position);
                    bookingDIA = _booking_DIA.get(position);
                    bookingGSM = _booking_GSM.get(position);
                    descriptopn = _fabDescNames.get(position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> _stickerNames = new ArrayList<>();
        List<String> _stickerIds = new ArrayList<>();
        for (V1_FinishingQCModelResponse.StickerType stickerItem : apiResponse.getResultSet().getStickerType()) {
            _stickerNames.add(stickerItem.getStickerName());
            _stickerIds.add(String.valueOf(stickerItem.getStickerId()));
        }
        _stickerNames.add(0, "--Select--");
        _stickerIds.add(0, "0");
        ArrayAdapter<String> adapterSticker = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, _stickerNames);

        _stickerTypeSpinner.setAdapter(adapterSticker);
        _stickerTypeSpinner.setSelection(_stickerIds.indexOf(String.valueOf(_stickerId)));

        _stickerTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _stickerId = Integer.parseInt(String.valueOf(_stickerIds.get(position)));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> _style_name = new ArrayList<>();
        List<String> _company_id = new ArrayList<>();
        List<String> _company_name = new ArrayList<>();
        List<String> _buyer_id = new ArrayList<>();
        List<String> _buyer_name = new ArrayList<>();
        List<String> _location_id = new ArrayList<>();
        List<String> _location_name = new ArrayList<>();
        List<String> _job_id = new ArrayList<>();
        List<String> _job_no = new ArrayList<>();
        for (V1_FinishingQCModelResponse.StyleRefNo styleItem : apiResponse.getResultSet().getStyleRefNo()) {
            _style_name.add(styleItem.getStyleName());
            _company_id.add(styleItem.getCompanyId());
            _company_name.add(styleItem.getCompanyName());
            _buyer_id.add(styleItem.getBuyerId());
            _buyer_name.add(styleItem.getBuyerName());
            _location_id.add(styleItem.getLocationId());
            _location_name.add(styleItem.getLocationName());
            _job_no.add(styleItem.getJob_no());
            _job_id.add(styleItem.getJob_id());
        }
        _style_name.add(0, "--Select--");
        _company_id.add(0, "0");
        _company_name.add(0, "0");
        _buyer_id.add(0, "0");
        _buyer_name.add(0, "0");
        _location_id.add(0, "0");
        _location_name.add(0, "0");
        _job_id.add(0, "0");
        _job_no.add(0, "0");

        ArrayAdapter<String> adapterStyleRef = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, _style_name);

        _styleRefSpinner.setAdapter(adapterStyleRef);
        _styleRefSpinner.setSelection(_style_name.indexOf(String.valueOf(styleName)));

        _styleRefSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                styleName = _style_name.get(position);
                buyerName = _buyer_name.get(position);
                companyName = _company_name.get(position);
                locationName = _location_name.get(position);
                jobId = _job_id.get(position);
                jobNO = _job_no.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getDefaultData() {
        Intent intent = getIntent();
        batch_no = intent.getStringExtra("barcodeScan");
        scan_op = intent.getIntExtra("scan_op", 0);

        if(!savedPrinter.equals("")){
            _posPrinterSelectTV.setText(savedPrinter);
            _posPrinterSelectTV.setTextColor(getResources().getColor(R.color.green_A200));
            _printerImage.setImageResource(R.drawable.connectedprinter);
        }else{
            _posPrinterSelectTV.setText("Not Connected");
            _posPrinterSelectTV.setTextColor(getResources().getColor(R.color.white));
            _printerImage.setImageResource(R.drawable.notconnectedprinter);
        }

        if( scan_op == 1 && batch_no != null){
            getFinishingQCData(batch_no);
            _batchScanTV.setText(batch_no);
        }else if(scan_op == 2 && batch_no != null){
            getUpdateData(2, batch_no);
        }else if(scan_op == 3 && batch_no != null){
            getUpdateData(3, batch_no);
        }
    }

    private void getUpdateData(int i, String batchNo) {
        progressBarState();

        MutableLiveData<V1_FabricFinishQCUpdateModel> liveData;

        if (i == 2) {
            liveData = finishProductionViewModel.getFinishingingQCUpdateDataResponse(batchNo, "0", "0");
        } else if (i == 3) {
            liveData = finishProductionViewModel.getFinishingingQCUpdateDataResponse("0", "0", batchNo);
        } else {
            liveData = finishProductionViewModel.getFinishingingQCUpdateDataResponse("0", "0", batchNo);
        }

        liveData.observe(this, response -> {
            if (response == null || response.getResultSet() == null || response.getResultSet().isEmpty()) {
                DialogHelper.showErrorDialog(this, "Error", "No data found.");
            } else {
                _finishQCRecyclerView.setVisibility(View.VISIBLE);
                _entryFormLayout.setVisibility(View.GONE);
                _isUpdate = 1;
                fabricFinishQCModelList = new ArrayList<>(response.getResultSet());
                initRecyclerView();
            }
        });
    }


    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _finishQCRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(5);
        _finishQCRecyclerView.addItemDecoration(itemDecorator);
        fabricFinishQCRecyclerViewAdapter = new V1_FabricFinishQCRecyclerViewAdapter(fabricFinishQCModelList, this, this);
        _finishQCRecyclerView.setAdapter(fabricFinishQCRecyclerViewAdapter);
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setReverseLayout(true);
//        layoutManager.setStackFromEnd(true); // This ensures that the last item stays at the bottom
//        _finishQCRecyclerView.setLayoutManager(layoutManager);
    }

    private void init_ui() {
        _progressBar = findViewById(R.id.progressBar);
        _batchScanTV = findViewById(R.id.batchScanTV);
        _srBookingTV = findViewById(R.id.srBookingTV);
        _fabColorTV = findViewById(R.id.fabColorTV);
        _posPrinterSelectTV = findViewById(R.id.posPrinterSelectTV);
        _barcodeTV = findViewById(R.id.barcodeTV);
        _greyUsedET = findViewById(R.id.greyUsedET);
        _actualDiaET = findViewById(R.id.actualDiaET);
        _actualGSMET = findViewById(R.id.actualGSMET);
        _fabricWeightET = findViewById(R.id.fabricWeightET);
        _commentsET = findViewById(R.id.commentsET);
        _rollNoET = findViewById(R.id.rollNoET);
        _machineSpinner = findViewById(R.id.machineSpinner);
        _styleRefSpinner = findViewById(R.id.styleRefSpinner);
        _stickerTypeSpinner = findViewById(R.id.stickerTypeSpinner);
        _fabDescSpinner = findViewById(R.id.fabDescSpinner);
        _stickerPrintBT = findViewById(R.id.stickerPrintBT);
        _stickerPrintBT.setOnClickListener(this);
        _printerLayout = findViewById(R.id.printerLayout);
        _printerLayout.setOnClickListener(this);
        _batchPrintBT = findViewById(R.id.batchPrintBT);
        _batchPrintBT.setOnClickListener(this);
        _saveBtn = findViewById(R.id.saveBtn);
        _saveBtn.setOnClickListener(this);
        _refreshBtn = findViewById(R.id.refreshBtn);
        _refreshBtn.setOnClickListener(this);
        _batchScanBT = findViewById(R.id.batchScanBT);
        _batchScanBT.setOnClickListener(this);
        _editBtn = findViewById(R.id.editBtn);
        _editBtn.setOnClickListener(this);
        _printerImage = findViewById(R.id.printerImage);
        _finishQCRecyclerView = findViewById(R.id.finishQCRecyclerView);
        _entryFormLayout = findViewById(R.id.entryFormLayout);
        _insertScanFormLayout = findViewById(R.id.insertScanFormLayout);
        _back = findViewById(R.id.back);
        _back = findViewById(R.id.back);
        _back.setOnClickListener(this);

        initRecyclerView();

        _entryFormLayout.setVisibility(View.VISIBLE);
        _insertScanFormLayout.setVisibility(View.VISIBLE);
        _finishQCRecyclerView.setVisibility(View.GONE);
    }

    private void progressBarState() {
        finishProductionViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                _progressBar.setVisibility(View.VISIBLE);
            } else {
                _progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.batchScanBT:
                String _batchNo = _batchScanTV.getText().toString().trim();
                if (_batchNo.isEmpty()) {
                    startScanning(1);
                } else {
                    DialogHelper.showWarningDialog(this, "Warning", "ব্যাচ পরিবর্তন করা যাবে না । প্রথমে রিফ্রেশ করুন ।");
                }
                break;
            case R.id.printerLayout:
                browseBluetoothDevice();
                break;
            case R.id.stickerPrintBT:
                printBluetooth();
                break;
            case R.id.saveBtn:
                String _greyUsed = _greyUsedET.getText().toString().trim();
                String _actualDia = _actualDiaET.getText().toString().trim();
                String _actualGsm = _actualGSMET.getText().toString().trim();
                String _fabricWeight = _fabricWeightET.getText().toString().trim();
                if ( !_greyUsed.isEmpty() && !_actualDia.isEmpty() && !_actualGsm.isEmpty() && !_fabricWeight.isEmpty() && _machineId != 0 && !_fabDescId.equals("--Select--") && _stickerId != 0) {
                    postDataToServer();
                } else {
                    DialogHelper.showWarningDialog(this, "Warning", "Please fill all the fields.");
                }
                break;
            case R.id.printBT:
                printBluetooth();
                break;
            case R.id.editBtn:
                _entryFormLayout.setVisibility(View.GONE);
                _insertScanFormLayout.setVisibility(View.GONE);
                _finishQCRecyclerView.setVisibility(View.VISIBLE);
                dialogboxForUpdateDataScanning();
                break;
            case R.id.refreshBtn:
                refreshData();
                break;
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    private void dialogboxForUpdateDataScanning() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_scan_options);
        dialog.setCancelable(true);

        Button btnRollScan = dialog.findViewById(R.id.btnRollScan);
        Button btnBatchScan = dialog.findViewById(R.id.btnBatchScan);

        btnRollScan.setOnClickListener(view -> {
            dialog.dismiss();
            startScanning(2);
        });

        btnBatchScan.setOnClickListener(view -> {
            dialog.dismiss();
            startScanning(3);
        });
        dialog.show();
    }

    private void postDataToServer() {
        JSONObject save_obj = new JSONObject();
        try {
            save_obj.put("UPDATE_ID", String.valueOf(_isUpdate));
            save_obj.put("STICKER_TYPE", String.valueOf(_stickerId));
            save_obj.put("BATCH_ID", String.valueOf(finishingQCModelResponse.getResultSet().getBatchId()));
            save_obj.put("BATCH_NO", String.valueOf(finishingQCModelResponse.getResultSet().getBatchNo()));
            save_obj.put("BOOKING_NO", String.valueOf(finishingQCModelResponse.getResultSet().getBookingNo()));
            save_obj.put("JOB_ID", String.valueOf(jobId));
            save_obj.put("JOB_NO", String.valueOf(jobNO));
            save_obj.put("MACHINE_ID", String.valueOf(_machineId));
            save_obj.put("STYLE_REF_NO", styleName);
            save_obj.put("FABRIC_WEIGHT", _fabricWeightET.getText().toString());
            save_obj.put("GSM", _actualGSMET.getText().toString());
            save_obj.put("DIA", _actualDiaET.getText().toString());
            save_obj.put("BARCODE_NO", "0");
            save_obj.put("ROLL_NO", _rollNoET.getText().toString());
            save_obj.put("DETERMINATION_ID", String.valueOf(_fabDescId));
            save_obj.put("FAB_DESC", descriptopn);
            save_obj.put("FAB_COLOR_ID", finishingQCModelResponse.getResultSet().getColorId());
            save_obj.put("BOOKING_GSM", bookingGSM);
            save_obj.put("BOOKING_DIA", bookingDIA);
            save_obj.put("FAB_COLOR", String.valueOf(finishingQCModelResponse.getResultSet().getColorName()));
            save_obj.put("GREY_USED", _greyUsedET.getText().toString());
            save_obj.put("COMMENTS", _commentsET.getText().toString());
            save_obj.put("INSERTED_BY",  String.valueOf(userID));

            Log.d(TAG, "postDataToServer: ########" + save_obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, save_obj.toString());
        progressBarState();
        finishProductionViewModel.postFinishQCResponse(body).observe(this, apiResponse -> {
            if(apiResponse != null) {
                try {
                    if(apiResponse.getStatusCode() == 200){
                        DialogHelper.showSuccessDialog(this, "Success", apiResponse.getMsg());
                        responseBarcodeNo = apiResponse.getBarcodeNo();
                        _barcodeTV.setText(responseBarcodeNo);
//                    refreshData();
                        _saveBtn.setEnabled(false);
                        _stickerPrintBT.setVisibility(View.VISIBLE);
                        _barcodeTV.setVisibility(View.VISIBLE);
                    }else{
                        DialogHelper.showWarningDialog(this, "Error", apiResponse.getMsg());
                    }

                }catch (Exception e){
                    Log.d(TAG, "postDataToServer: "+e.getMessage()+e);
                    DialogHelper.showErrorDialog(this, "Error", "An unexpected error occurred.");
                }
            }else{
                DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
            }
        });
    }

    private void startScanning(int op) {
        Intent intent = new Intent(this, V1_ScannerActivity.class);
        intent.putExtra("qc", "finishing_qc");
        intent.putExtra("scan_op", op);
        startActivity(intent);
        finish();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshData() {
        _batchScanTV.setText("");
        _srBookingTV.setText("");
        _fabColorTV.setText("");
        _greyUsedET.setText("");
        _actualDiaET.setText("");
        _actualGSMET.setText("");
        _fabricWeightET.setText("");
        _commentsET.setText("");
        _stickerPrintBT.setVisibility(View.GONE);
        _barcodeTV.setVisibility(View.GONE);
        _styleRefSpinner.setSelection(0);
        _stickerTypeSpinner.setSelection(0);
        _fabDescSpinner.setSelection(0);
        _machineSpinner.setSelection(0);

        _finishQCRecyclerView.setVisibility(View.GONE);
        _entryFormLayout.setVisibility(View.VISIBLE);
        _insertScanFormLayout.setVisibility(View.VISIBLE);
        fabricFinishQCModelList.clear();
        fabricFinishQCRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMoreHeadClick(int position, View v) {
        try {
            _insertScanFormLayout.setVisibility(View.GONE);
            _entryFormLayout.setVisibility(View.VISIBLE);

            _machineId = Integer.valueOf(fabricFinishQCModelList.get(position).getMachineId());
            _fabDescId = fabricFinishQCModelList.get(position).getFabDesc();
            _stickerId = Integer.valueOf(fabricFinishQCModelList.get(position).getStickerType());
            styleName = fabricFinishQCModelList.get(position).getStyleRefNo();
            greyUsed = fabricFinishQCModelList.get(position).getGreyUsed();
            actualDia = fabricFinishQCModelList.get(position).getDia();
            actualGSM = fabricFinishQCModelList.get(position).getGsm();
            fabricWeight = fabricFinishQCModelList.get(position).getFabWeight();
            rollNo = fabricFinishQCModelList.get(position).getRollNo();
            comments = fabricFinishQCModelList.get(position).getComments();

            _isUpdate = Integer.valueOf(fabricFinishQCModelList.get(position).getId());

            getFinishingQCData(fabricFinishQCModelList.get(position).getBatchNo());
        }catch (Exception e){
            Log.d(TAG, "onMoreHeadClick: "+e.getMessage()+e);
        }
    }

    @SuppressLint("MissingPermission")
    public void browseBluetoothDevice() {
        this.checkBluetoothPermissions(() -> {
            final BluetoothConnection[] bluetoothDevicesList = (new BluetoothPrintersConnections()).getList();

            if (bluetoothDevicesList != null) {
                final String[] items = new String[bluetoothDevicesList.length + 1];
                items[0] = "Default printer";
                int i = 0;
                for (BluetoothConnection device : bluetoothDevicesList) {
                    items[++i] = device.getDevice().getName();
                }

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(V1_FabricFinishingQCActivity.this);
                alertDialog.setTitle("Bluetooth printer selection");
                alertDialog.setItems(
                        items,
                        (dialogInterface, i1) -> {
                            int index = i1 - 1;
                            if (index == -1) {
                                selectedDevice = null;
                            } else {
                                selectedDevice = bluetoothDevicesList[index];
                            }
                            _posPrinterSelectTV.setText(items[i1]);
                            _posPrinterSelectTV.setTextColor(getResources().getColor(R.color.green_A200));
                            _printerImage.setImageResource(R.drawable.connectedprinter);
                            _preferences = PreferenceManager.getDefaultSharedPreferences(this);
                            SharedPreferences.Editor _editor = _preferences.edit();
                            _editor.putString("saved_printer", items[i1]);
                            _editor.apply();
                        }
                );

                AlertDialog alert = alertDialog.create();
                alert.setCanceledOnTouchOutside(false);
                alert.show();
            }
        });

    }

    public PrintTestActivity.OnBluetoothPermissionsGranted onBluetoothPermissionsGranted;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case PrintTestActivity.PERMISSION_BLUETOOTH:
                case PrintTestActivity.PERMISSION_BLUETOOTH_ADMIN:
                case PrintTestActivity.PERMISSION_BLUETOOTH_CONNECT:
                case PrintTestActivity.PERMISSION_BLUETOOTH_SCAN:
                    this.checkBluetoothPermissions(this.onBluetoothPermissionsGranted);
                    break;
            }
        }
    }

    public void checkBluetoothPermissions(PrintTestActivity.OnBluetoothPermissionsGranted onBluetoothPermissionsGranted) {
        this.onBluetoothPermissionsGranted = onBluetoothPermissionsGranted;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, PrintTestActivity.PERMISSION_BLUETOOTH);
        } else if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, PrintTestActivity.PERMISSION_BLUETOOTH_ADMIN);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PrintTestActivity.PERMISSION_BLUETOOTH_CONNECT);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, PrintTestActivity.PERMISSION_BLUETOOTH_SCAN);
        } else {
            this.onBluetoothPermissionsGranted.onPermissionsGranted();
        }
    }

    public void printBluetooth() {
        this.checkBluetoothPermissions(() -> {
            new AsyncBluetoothEscPosPrint(
                    this,
                    new AsyncEscPosPrint.OnPrintFinished() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onError(AsyncEscPosPrinter asyncEscPosPrinter, int codeException) {
                            Log.e("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : An error occurred !");
                        }

                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
                            Log.i("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Print is finished !");
                        }
                    }
            ).execute(this.getAsyncEscPosPrinter(selectedDevice));
        });
    }

    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection printerConnection) {
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 100f, 47);

        try {
            StringBuilder textToPrint = new StringBuilder();

            textToPrint.append("[C]<qrcode size='24'>" + responseBarcodeNo + "</qrcode>\n");
            textToPrint.append("[C]<u type='double'>" + companyName + "</u>\n");
            textToPrint.append("[C]"+ locationName +"\n");
            textToPrint.append("[C]---------------------------------------------\n");
            textToPrint.append("[L]<font size='normal'>Buyer Name: [R]"+buyerName+"</font>\n");
            textToPrint.append("[L]<font size='normal'>Store Reference: [R]"+systmeBookingNo+"</font>\n");
            textToPrint.append("[L]<font size='normal'>Style Name: [R]" + styleName + "</font>\n");
            textToPrint.append("[L]<font size='normal'>Fabric Type: [R]" + fabType + "</font>\n");
//            textToPrint.append("[L]<font size='normal'>Fabric Com.: [R]" + fabCons + "</font>\n");
            textToPrint.append("[L]<font size='normal'>Fabric Com.: [R]" + limitText(fabCons, 20) + "</font>\n");
            textToPrint.append("[L]<font size='normal'>Batch No: [R]" + finishingQCModelResponse.getResultSet().getBatchNo() + "</font>\n");
//            textToPrint.append("[L]<font size='normal'>Color Name: [R]" + finishingQCModelResponse.getResultSet().getColorName() + "</font>\n");
            textToPrint.append("[L]<font size='normal'>Color Name: [R]" + limitText(finishingQCModelResponse.getResultSet().getColorName(), 20) + "</font>\n");
            textToPrint.append("[L]<font size='normal'>Require Dia: [R]" + _actualDiaET.getText().toString() + "</font>\n");
            textToPrint.append("[L]<font size='normal'>Require GSM: [R]" + _actualGSMET.getText().toString() + "</font>\n");
            textToPrint.append("[L]<font size='normal'>Roll No: [R]" + _rollNoET.getText().toString() + "</font>\n");
            textToPrint.append("[L]<font size='normal'>Quantity: [R]" + _fabricWeightET.getText().toString() + "</font>\n");
            textToPrint.append("[C]<barcode type='128' height='10'> {B" + responseBarcodeNo + "</barcode>\n");
            printer.addTextToPrint(textToPrint.toString());

            Log.d(TAG, "getAsyncEscPosPrinter: "+ textToPrint);
        } catch (Exception e) {
            DialogHelper.showWarningDialog(this, "Warning", "ডেটাতে কিছু সমস্যার কারণে স্টিকার প্রিন্ট করা যাচ্ছে না, অনুগ্রহ করে আবার চেষ্টা করুন।");
            Log.d(TAG, "getAsyncEscPosPrinter: "+e.getMessage());
        }
        return printer;
    }
    private String limitText(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength) : text;
    }


}