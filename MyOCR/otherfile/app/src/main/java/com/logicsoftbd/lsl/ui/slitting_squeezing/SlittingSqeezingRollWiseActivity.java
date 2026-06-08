package com.logicsoftbd.lsl.ui.slitting_squeezing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.model.FloorWiseMachineResponse;
import com.logicsoftbd.lsl.data.network.model.SlitteringSequzBarCodeResponse;
import com.logicsoftbd.lsl.data.network.model.SlitteringSequzCompanyWiseFloorResponse;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_ScannerActivity;
import com.logicsoftbd.lsl.utils.DialogHelper;
import com.logicsoftbd.lsl.viewModel.FinishProductionViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SlittingSqeezingRollWiseActivity extends AppCompatActivity implements View.OnClickListener, FinishProductionSlittingRecyclerAdapter.OnSelectSelectListener {
    private static final String TAG = "SlittingSqeezingRollWis";
    private Integer op = 0;
    private Toolbar mToolbar;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat, simpleTimeFormat;
    private Spinner _sourceNameSpinner, _companyNameSpinner, _processNameSpinner, _floorSpinner, _machineNameSpinner, _shiftNameSpinner, _resultNameSpinner;
    private EditText _batchNoScanET, _barcodeScanET, _overFeedET, _temparatureET, _speedET, _steamET, _remarkET, _productionTimeHour, _productionTimeMin, _processStartTimeHour, _processStartTimeMin, _processEndTimeHour, _processEndTimeMin, _reSlittingET;
    private TextView _scannedBatchNoTV, _noRollTV, _scannedNoRollTV, _blRollTV;
    private CheckBox _selectBarcodeCheckbox;
    private Button _productionDate, _productionTime, _processStartDate, _processStartTime, _processEndDate, _processEndTime, _barcodeScan;
    private RecyclerView _finishProductionReceiveRecyclerView;
    private ProgressBar _progressBar;
    private ArrayList<String> sourceNameList = new ArrayList<>();
    private ArrayList<String> sourceIdList = new ArrayList<>();
    private ArrayList<String> companyNameList = new ArrayList<>();
    private ArrayList<String> companyIdList = new ArrayList<>();
    private ArrayList<String> serviceCompanyNameList = new ArrayList<>();
    private ArrayList<String> serviceCompanyIdList = new ArrayList<>();
    private ArrayList<String> outBountCompanyNameList = new ArrayList<>();
    private ArrayList<String> outBountCompanyIdList = new ArrayList<>();
    private ArrayList<String> processNameList = new ArrayList<>();
    private ArrayList<String> processIdList = new ArrayList<>();
    private ArrayList<String> productionTypeNameList = new ArrayList<>();
    private ArrayList<String> productionTypeIdList = new ArrayList<>();
    private ArrayList<String> floodNameList = new ArrayList<>();
    private ArrayList<String> floorIdList = new ArrayList<>();
    private ArrayList<String> machineNameList  = new ArrayList<>();
    private ArrayList<String> machineIdList = new ArrayList<>();
    private ArrayList<String> nextPrcessNameList = new ArrayList<>();
    private ArrayList<String> nextProcessIdList = new ArrayList<>();
    private ArrayList<String> resultNameList = new ArrayList<>();
    private ArrayList<String> resultIdList = new ArrayList<>();
    private ArrayList<String> shiftNameList = new ArrayList<>();
    private ArrayList<String> shiftIdLiest= new ArrayList<>();
    private ArrayList<String> processStartTimeList = new ArrayList<>();
    private FinishProductionSlittingRecyclerAdapter finishProductionRecyclerAdapter;
    private List<SlitteringSequzBarCodeResponse.DtlsIndex> dtlsIndexArrayList = new ArrayList<>();
    private SlitteringSequzBarCodeResponse slitteringSequzBarCodeResponse = new SlitteringSequzBarCodeResponse();

    private String sourceId, companyId, processId, productionTypeId, floorId, machineId, nextProcessId, resultId, shiftId, saved_source_position, saved_wc_position, saved_wc_floor_position, saved_mc_position,  saved_process_position, saved_shift_position, saved_result_position = "0";
    private String USER_ID, BATCH_ID, BATCH_NO, TRIMS_WGT, entry_form_no, IS_RE_DYEING, barcodeScan = "";
    private String base_url ="", scannedBatch, userID, mType = "";
    private FinishProductionViewModel finishProductionViewModel;
    boolean isAllFieldsChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slitting_sqeezing_roll_wise);

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        base_url = (_preferences.getString("base_url", ""));
        userID = _preferences.getString("login_userid", "");

        Intent intent = getIntent();
        barcodeScan = intent.getStringExtra("barcodeScan");
        scannedBatch = intent.getStringExtra("batch_scan");
        slitteringSequzBarCodeResponse = (SlitteringSequzBarCodeResponse) intent.getSerializableExtra("slittering_roll_data");
        op = intent.getIntExtra("scan_op", 0);

        Log.d(TAG, "onCreate: #######"+barcodeScan+" "+scannedBatch+" "+op);

        finishProductionViewModel = new ViewModelProvider(this).get(FinishProductionViewModel.class);
        setUp();
        fetchCompactingData();
        setupCompactingProdScanRecyclerView();
        invalidateOptionsMenu();
        loadData();
    }

    private void fetchCompactingData() {
        progressBarState();
        finishProductionViewModel.getFinishProductionDefaultResponse("30").observe(this, apiResponse -> {
            if(apiResponse != null){
                if(apiResponse.getResultset().getSource().size() > 0){
                    sourceNameList.clear();
                    sourceIdList.clear();
                    for(int i=0; i<apiResponse.getResultset().getSource().size(); i++){
                        sourceNameList.add(apiResponse.getResultset().getSource().get(i).getName());
                        sourceIdList.add(apiResponse.getResultset().getSource().get(i).getId());
                    }
                }
                if(apiResponse.getResultset().getServiceCompany().size() > 0){
                    serviceCompanyNameList.clear();
                    serviceCompanyIdList.clear();
                    for(int i=0; i<apiResponse.getResultset().getServiceCompany().size(); i++){
                        serviceCompanyNameList.add(apiResponse.getResultset().getServiceCompany().get(i).getCompany());
                        serviceCompanyIdList.add(apiResponse.getResultset().getServiceCompany().get(i).getId());
                    }
                }
                if(apiResponse.getResultset().getServiceCompanyOutbound() != null && apiResponse.getResultset().getServiceCompanyOutbound().size() > 0){
                    outBountCompanyNameList.clear();
                    outBountCompanyIdList.clear();
                    for(int i=0; i<apiResponse.getResultset().getServiceCompanyOutbound().size(); i++){
                        outBountCompanyNameList.add(apiResponse.getResultset().getServiceCompanyOutbound().get(i).getName());
                        outBountCompanyIdList.add(apiResponse.getResultset().getServiceCompanyOutbound().get(i).getId());
                    }
                }
                if(apiResponse.getResultset().getProductionType().size() > 0){
                    productionTypeNameList.clear();
                    productionTypeIdList.clear();
                    for(int i=0; i<apiResponse.getResultset().getProductionType().size(); i++){
                        productionTypeNameList.add(apiResponse.getResultset().getProductionType().get(i).getName());
                        productionTypeIdList.add(apiResponse.getResultset().getProductionType().get(i).getId());
                    }
                }
                if(apiResponse.getResultset().getProcessName().size() > 0){
                    processNameList.clear();
                    processIdList.clear();
                    for(int i=0; i<apiResponse.getResultset().getProcessName().size(); i++){
                        processNameList.add(apiResponse.getResultset().getProcessName().get(i).getName());
                        processIdList.add(apiResponse.getResultset().getProcessName().get(i).getId());
                    }
                }
                if(apiResponse.getResultset().getNextProcess().size() > 0){
                    nextPrcessNameList.clear();
                    nextProcessIdList.clear();
                    for(int i=0; i<apiResponse.getResultset().getNextProcess().size(); i++){
                        nextPrcessNameList.add(apiResponse.getResultset().getNextProcess().get(i).getName());
                        nextProcessIdList.add(apiResponse.getResultset().getNextProcess().get(i).getId());
                    }
                }
                if(apiResponse.getResultset().getResult().size() > 0){
                    resultNameList.clear();
                    resultIdList.clear();
                    for(int i=0; i<apiResponse.getResultset().getResult().size(); i++){
                        resultNameList.add(apiResponse.getResultset().getResult().get(i).getName());
                        resultIdList.add(apiResponse.getResultset().getResult().get(i).getId());
                    }
                }
                if(apiResponse.getResultset().getShiftName().size() > 0){
                    shiftNameList.clear();
                    shiftIdLiest.clear();
                    for(int i=0; i<apiResponse.getResultset().getShiftName().size(); i++){
                        shiftNameList.add(apiResponse.getResultset().getShiftName().get(i).getName());
                        shiftIdLiest.add(apiResponse.getResultset().getShiftName().get(i).getId());
                    }
                }

                try {
                    setUpSourceSpinner();
                    setUpProcessSpinner();
                    setUpShiftSpinner();
                    setUpResultSpinner();
                } catch (Exception e){
                    Log.d(TAG, "fetchCompactingData: "+e.getMessage());
                }

            }else{
                DialogHelper.showErrorDialog(SlittingSqeezingRollWiseActivity.this, "Message", "Something went wrong!");
            }
        });
    }

    private void fetchCompanyWiseFloor(String companyId) {
        progressBarState();
        finishProductionViewModel.getFinishProductionCompanyWiseFloorResponse(companyId).observe(this, apiResponse -> {
            if(apiResponse != null){
                setUpCompanyWiseFloorSpinner(apiResponse.getResultset());
            }else{
                Toast.makeText(this, "Floor not selected.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchCompanyFloorMachine(String companyFloorId) {
        progressBarState();
        finishProductionViewModel.getFinishProductionCompanyWiseFloorMachineResponse(companyFloorId).observe(this, apiResponse -> {
            if(apiResponse!= null){
                setUpCompanyFloorMachineSpinner(apiResponse.getResultset());
            }
        });
    }

    private void fetchCompactingBatchScan(String scannedBatch) {
        progressBarState();
        finishProductionViewModel.getFinishProductionSlittingBatchScanResponse(scannedBatch, "30").observe(this, apiResponse -> {
            if(apiResponse!= null){
                if(apiResponse.getResultset() != null){
                    _batchNoScanET.setText(apiResponse.getResultset().getInputAreaIndex().getBatchNo());
                    _scannedBatchNoTV.setText(apiResponse.getResultset().getInputAreaIndex().getBatchNo());
                    slitteringSequzBarCodeResponse = apiResponse;
                    dtlsIndexArrayList = slitteringSequzBarCodeResponse.getResultset().getDtlsIndex();
                    if(apiResponse.getResultset().getDtlsIndex().size() > 0){
                        dtlsIndexArrayList = apiResponse.getResultset().getDtlsIndex();
                        _noRollTV.setText(String.valueOf(dtlsIndexArrayList.size()));
                        for (SlitteringSequzBarCodeResponse.DtlsIndex dtlsIndex : dtlsIndexArrayList) {
                            dtlsIndex.setBarcode_status(false);
                        }
                    }
                    try {
                        setDataToUI(slitteringSequzBarCodeResponse);
                        setupCompactingProdScanRecyclerView();
                    }catch (Exception e){
                        DialogHelper.showWarningDialog(this, "Message", "Please try again.");
                        refreshDataFromUI();
                    }
                }
                else{
                    DialogHelper.showWarningDialog(this, "Message", apiResponse.getMsg());
                }
            }else{
                DialogHelper.showErrorDialog(this, "Error Message", "Something went wrong!");
            }
        });
    }

    private void setDataToUI(SlitteringSequzBarCodeResponse response) {
        BATCH_ID = response.getResultset().getInputAreaIndex().getBatchId();
        BATCH_NO = response.getResultset().getInputAreaIndex().getBatchNo();
        TRIMS_WGT = response.getResultset().getInputAreaIndex().getTrimsWgt();
        entry_form_no = response.getResultset().getInputAreaIndex().getEntryFormNo();
        companyId = response.getResultset().getInputAreaIndex().getServiceCompany();
        floorId = response.getResultset().getInputAreaIndex().getFloorId();
        machineId = response.getResultset().getInputAreaIndex().getDyeingMachine();
        shiftId = response.getResultset().getInputAreaIndex().getShiftId();

        if(response.getResultset().getInputAreaIndex().getReSlittingNo().equals("false")){
            _reSlittingET.setText("0");
        }else{
            _reSlittingET.setText(response.getResultset().getInputAreaIndex().getReSlittingNo());
        }

        dtlsIndexArrayList = response.getResultset().getDtlsIndex();
        if(response.getResultset().getDtlsIndex().size() > 0){
            if(barcodeScan != null && (op == 2 || op == 1)){
                for (SlitteringSequzBarCodeResponse.DtlsIndex dtlsIndex : dtlsIndexArrayList) {
                    if (dtlsIndex.getBarcodeNo().equals(barcodeScan)) {
                        dtlsIndex.setBarcode_status(true);
                    } else if (dtlsIndex.getBarcode_status() && !dtlsIndex.getBarcodeNo().equals(barcodeScan)) {
                        dtlsIndex.setBarcode_status(true);
                    } else {
                        dtlsIndex.setBarcode_status(false);
                    }
                }
            }
        }
    }

    private void setUpSourceSpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.custome_spinner_dropdown, sourceNameList);
        _sourceNameSpinner.setAdapter(spinnerArrayAdapter);

        _sourceNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sourceId = sourceIdList.get(position);
                Log.d(TAG, "onItemSelected: "+sourceId);
                companyIdList.clear();
                companyNameList.clear();
                if(sourceId.equals("1")){
                    companyIdList.addAll(serviceCompanyIdList);
                    companyNameList.addAll(serviceCompanyNameList);
                } else if(sourceId.equals(3)){
                    companyIdList.addAll(outBountCompanyIdList);
                    companyNameList.addAll(outBountCompanyNameList);
                }
                setUpCompanySpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void setUpCompanySpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.custome_spinner_dropdown, companyNameList);
        _companyNameSpinner.setAdapter(spinnerArrayAdapter);
        _companyNameSpinner.setSelection(companyIdList.indexOf(companyId));
        _companyNameSpinner.setSelection(companyIdList.indexOf(saved_mc_position));
        _companyNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                companyId = companyIdList.get(position);
                fetchCompanyWiseFloor(companyId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpCompanyWiseFloorSpinner(List<SlitteringSequzCompanyWiseFloorResponse.Resultset> floorSet) {
        floodNameList.clear();
        floorIdList.clear();
        for(int i=0; i<floorSet.size(); i++){
            floodNameList.add(floorSet.get(i).getName());
            floorIdList.add(floorSet.get(i).getId());
        }
        floodNameList.add(0, "-Select-");
        floorIdList.add(0, "0");
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.custome_spinner_dropdown, floodNameList);
        _floorSpinner.setAdapter(spinnerArrayAdapter);
        _floorSpinner.setSelection(floorIdList.indexOf(floorId));

        _floorSpinner.setSelection(floorIdList.indexOf(saved_wc_floor_position));
        _floorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                floorId = floorIdList.get(position);
                fetchCompanyFloorMachine(floorIdList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpCompanyFloorMachineSpinner(List<FloorWiseMachineResponse.Resultset> machineSet) {
        machineNameList.clear();
        machineIdList.clear();
        for(int i=0; i<machineSet.size(); i++){
            machineNameList.add(machineSet.get(i).getName());
            machineIdList.add(machineSet.get(i).getId());
        }

        machineNameList.add(0, "-Select-");
        machineIdList.add(0, "0");
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.custome_spinner_dropdown, machineNameList);
        _machineNameSpinner.setAdapter(spinnerArrayAdapter);
        _machineNameSpinner.setSelection(machineIdList.indexOf(machineId));

        _machineNameSpinner.setSelection(machineIdList.indexOf(saved_mc_position));

        _machineNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                machineId = machineIdList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpProcessSpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.custome_spinner_dropdown, processNameList);
        _processNameSpinner.setAdapter(spinnerArrayAdapter);
        _processNameSpinner.setSelection(processNameList.indexOf("Slitting/Squeezing"));
        _processNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                processId = processIdList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpShiftSpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.custome_spinner_dropdown, shiftNameList);
        _shiftNameSpinner.setAdapter(spinnerArrayAdapter);
        _shiftNameSpinner.setSelection(shiftIdLiest.indexOf(shiftId));

        _shiftNameSpinner.setSelection(shiftIdLiest.indexOf(saved_shift_position));

        _shiftNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shiftId = shiftIdLiest.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpResultSpinner() {
        resultNameList.add(0, "-Select-");
        resultIdList.add(0, "0");
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.custome_spinner_dropdown, resultNameList);
        _resultNameSpinner.setAdapter(spinnerArrayAdapter);

        _resultNameSpinner.setSelection(resultIdList.indexOf(saved_result_position));

        _resultNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                resultId = resultIdList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupCompactingProdScanRecyclerView() {
        Log.d(TAG, "setupCompactingProdScanRecyclerView: *********"+dtlsIndexArrayList.size());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _finishProductionReceiveRecyclerView.setLayoutManager(linearLayoutManager);
        finishProductionRecyclerAdapter = new FinishProductionSlittingRecyclerAdapter( dtlsIndexArrayList, this, this);
        _finishProductionReceiveRecyclerView.setAdapter(finishProductionRecyclerAdapter);
    }

    private void saveRequestCompactingObject() throws JSONException {
        JSONObject save_obj = new JSONObject();
        JSONObject data_obj = new JSONObject();
        JSONObject index_obj = new JSONObject();
        JSONArray dtls_arr = new JSONArray();

        save_obj.put("status",true);
        save_obj.put("mode", "save");
        save_obj.put("MST_ID", "0");
        save_obj.put("UPDATE_ID", "0");

        index_obj.put("BATCH_ID", BATCH_ID);
        index_obj.put("BATCH_NO", BATCH_NO);
        index_obj.put("TRIMS_WGT", TRIMS_WGT);
        index_obj.put("COMPANY_ID", companyId);
        index_obj.put("SERVICE_COMPANY", companyId);
        index_obj.put("ENTRY_FORM_NO", entry_form_no);
        index_obj.put("RE_SLITING_NO", slitteringSequzBarCodeResponse.getResultset().getInputAreaIndex().getReSlittingNo());
        index_obj.put("PRODUCTION_TYPE", "");
        index_obj.put("PROCESS_ID", processId);
        index_obj.put("NEXT_PROCESS_ID", nextProcessId);
        index_obj.put("RESULT", resultId);
        index_obj.put("PRODUCTION_DATE", _productionDate.getText().toString());
        index_obj.put("PROCESS_START_DATE", _processStartDate.getText().toString());
        index_obj.put("PROCESS_END_DATE", _processEndDate.getText().toString());
//        index_obj.put("START_HOURS", _processStartTime.getText().toString().split(":")[0]);
//        index_obj.put("START_MINUTES", _processStartTime.getText().toString().split(":")[1]);
//        index_obj.put("END_HOURS", _processEndTime.getText().toString().split(":")[0]);
//        index_obj.put("END_MINUTES", _processEndTime.getText().toString().split(":")[1]);
        index_obj.put("START_HOURS", _processStartTimeHour.getText().toString());
        index_obj.put("START_MINUTES", _processStartTimeMin.getText().toString());
        index_obj.put("END_HOURS", _processEndTimeHour.getText().toString());
        index_obj.put("END_MINUTES", _processEndTimeMin.getText().toString());
        index_obj.put("SHIFT_NAME", shiftId);
        index_obj.put("ADVANCED_PROD_QTY", "");
        index_obj.put("FLOOR", floorId);
        index_obj.put("MACHINE_NAME", machineId);
        index_obj.put("WIDTH_SHRINKAGE", "");
        index_obj.put("LENGTH_SHRINKAGE", "");
        index_obj.put("PINNING", "");
        index_obj.put("FEED_IN", "");
        index_obj.put("STRETCH", "");
        index_obj.put("SPEED", _speedET.getText().toString());
        index_obj.put("STEAM", _steamET.getText().toString());
        index_obj.put("OVER_FEED", _overFeedET.getText().toString());
        index_obj.put("TEMPARATURE", _temparatureET.getText().toString());
        index_obj.put("CHEMICAL_NAME", "");
        index_obj.put("IS_RE_DYEING", "");
        index_obj.put("REMARK", _remarkET.getText().toString());
        index_obj.put("USER_ID", userID);

        data_obj.put("index", index_obj);

        for(int i=0; i<dtlsIndexArrayList.size(); i++){
            JSONObject dtls_obj = new JSONObject();
            if(dtlsIndexArrayList.get(i).getBarcode_status() || dtlsIndexArrayList.get(i).getChecked().equals("1")){
                dtls_obj.put("CHECKED", "1");
                dtls_obj.put("PROD_ID", dtlsIndexArrayList.get(i).getProdId());
                dtls_obj.put("FIN_DIA", dtlsIndexArrayList.get(i).getFinDia());
                dtls_obj.put("ROLL_NO", dtlsIndexArrayList.get(i).getRollNo());
                dtls_obj.put("ROLL_ID", dtlsIndexArrayList.get(i).getRollId());
                dtls_obj.put("NO_OF_ROLL", dtlsIndexArrayList.get(i).getNoOfRoll());
                dtls_obj.put("BATCH_QNTY", dtlsIndexArrayList.get(i).getBatchQnty());
                dtls_obj.put("PROD_QTY", dtlsIndexArrayList.get(i).getProdBatchQnty());
                dtls_obj.put("BARCODE_NO", dtlsIndexArrayList.get(i).getBarcodeNo());
                dtls_obj.put("DIA_TYPE", dtlsIndexArrayList.get(i).getDiaType());
                dtls_obj.put("DIA_WIDTH", dtlsIndexArrayList.get(i).getDiaWidth());
                dtls_obj.put("GSM", dtlsIndexArrayList.get(i).getGsm());
                dtls_obj.put("CONS_COMP", dtlsIndexArrayList.get(i).getConsComp());
                dtls_arr.put(dtls_obj);
            }
        }

        data_obj.put("list_data",dtls_arr);
        save_obj.put("data", data_obj);

        Log.d(TAG, "saveRequestDyeingProductionObject: ########"+ save_obj);

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, save_obj.toString());

        JSONArray listDataArray = data_obj.getJSONArray("list_data");

        if (listDataArray.length() == 0) {
            DialogHelper.showWarningDialog(SlittingSqeezingRollWiseActivity.this, "Message", "Please scan bundle before data saving..");
        } else {
            progressBarState();
            finishProductionViewModel.postFinishProductionCompactingResponse(body).observe(this, apiResponse -> {
                if(apiResponse != null){
                    if(apiResponse.getStatus()){
                        DialogHelper.showSuccessDialog(SlittingSqeezingRollWiseActivity.this, "Message", apiResponse.getResultset().getSaveMsg());
                        refreshDataFromUI();
                    }else{
                        DialogHelper.showWarningDialog(SlittingSqeezingRollWiseActivity.this, "Message", apiResponse.getResultset().getSaveMsg());
                    }

                }else{
                    DialogHelper.showErrorDialog(SlittingSqeezingRollWiseActivity.this, "Message", "Something wrong happen!");
                }
            });
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private void refreshDataFromUI() {
        dtlsIndexArrayList.clear();
        companyNameList.clear();
        companyIdList.clear();
        processNameList.clear();
        processIdList.clear();
        productionTypeNameList.clear();
        productionTypeIdList.clear();
        floodNameList.clear();
        floorIdList.clear();
        machineNameList.clear();
        machineIdList.clear();
        nextPrcessNameList.clear();
        nextProcessIdList.clear();
        resultNameList.clear();
        resultIdList.clear();
        shiftNameList.clear();
        shiftIdLiest.clear();
        processStartTimeList.clear();

        finishProductionRecyclerAdapter.notifyDataSetChanged();

        fetchCompactingData();
        _selectBarcodeCheckbox.setChecked(false);
        _scannedBatchNoTV.setText("");
        _noRollTV.setText("");
        _scannedNoRollTV.setText("");
        _blRollTV.setText("");
        _batchNoScanET.setText("");
        _barcodeScanET.setText("");
        _temparatureET.setText("");
        _speedET.setText("");
        _steamET.setText("");
        _overFeedET.setText("");
        _remarkET.setText("");
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fabric, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            isAllFieldsChecked = CheckAllFields();
            if(isAllFieldsChecked){
                if(dtlsIndexArrayList.size() > 0){
                    if(machineId != null && !machineId.equals("0")){
                        if(resultId != null && !resultId.equals("0")){
                            try {
                                saveLocalDataData();
                                saveRequestCompactingObject();
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }else{
                            DialogHelper.showWarningDialog(this, "Warning", "Please Select result and machine.");
                        }
                    }else{
                        DialogHelper.showWarningDialog(this, "Warning", "Please Select floor and machine.");
                    }
                }else {
                    DialogHelper.showWarningDialog(this, "Warning", "Batch or functional batch data is empty.");
                }
            }
            return true;
        }
        else if (id == R.id.action_new){
            refreshDataFromUI();
        } else if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private boolean CheckAllFields() {
//        if (_productionTimeHour.length() == 0) {
//            _productionTimeHour.setError("This field is required");
//            return false;
//        }
//
//        if (_productionTimeMin.length() == 0) {
//            _productionTimeMin.setError("This field is required");
//            return false;
//        }

        if (_processStartTimeHour.length() == 0) {
            _processStartTimeHour.setError("This field is required");
            return false;
        }

        if (_processStartTimeMin.length() == 0) {
            _processStartTimeMin.setError("This field is required");
            return false;
        }
        if (_processEndTimeHour.length() == 0) {
            _processEndTimeHour.setError("This field is required");
            return false;
        }

        if (_processEndTimeMin.length() == 0) {
            _processEndTimeMin.setError("This field is required");
            return false;
        }
        return true;
    }
    @SuppressLint("SetTextI18n")
    private void datetimepicker(final Button sedate) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> sedate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1), year, month, day);
        datePickerDialog.show();
    }

    private void timePicker(Button processStartTime) {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, (timePicker, selectedHour, selectedMinute) -> processStartTime.setText( selectedHour + ":" + selectedMinute), hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.productionDate:
                datetimepicker(_productionDate);
                break;
            case R.id.processStartDate:
                datetimepicker(_processStartDate);
                break;
            case R.id.processEndDate:
                datetimepicker(_processEndDate);
                break;
//            case R.id.productionTime:
//                timePicker(_productionTime);
//                break;
//            case R.id.processStartTime:
//                timePicker(_processStartTime);
//                break;
//            case R.id.processEndTime:
//                timePicker(_processEndTime);
//                break;
            case R.id.toolbar:
                finish();
                break;
//            case R.id.batchScan:
//                Intent intent1 = new Intent(this, V1_ScannerActivity.class);
//                intent1.putExtra("qc", "slitting_roll_wise");
//                intent1.putExtra("scan_op", 1);
//                intent1.putExtra("slittering_roll_data", slitteringSequzBarCodeResponse);
//                startActivity(intent1);
//                finish();
//                break;
            case R.id.barcodeScan:
//                String text = _batchNoScanET.getText().toString().trim();
//                try {
//                    if (text.isEmpty()) {
//                        DialogHelper.showWarningDialog(this, "Warning Message", "Please scan batch first.");
//                    } else if (slitteringSequzBarCodeResponse == null || slitteringSequzBarCodeResponse.getResultset() == null || slitteringSequzBarCodeResponse.getResultset().getDtlsIndex() == null || slitteringSequzBarCodeResponse.getResultset().getDtlsIndex().isEmpty()) {
//                        DialogHelper.showWarningDialog(this, "Warning Message", "No data available for the scanned batch.");
//                    } else {
//                        Intent intent = new Intent(this, V1_ScannerActivity.class);
//                        intent.putExtra("qc", "slitting_roll_wise");
//                        intent.putExtra("scan_op", 2);
//                        intent.putExtra("batch_scan", _batchNoScanET.getText().toString());
//                        intent.putExtra("slittering_roll_data", slitteringSequzBarCodeResponse);
//                        startActivity(intent);
//                        finish();
//                    }
//                } catch (Exception e) {
//                    DialogHelper.showWarningDialog(this, "Warning Message", "Please scan batch first.");
//                }

                String text = _batchNoScanET.getText().toString().trim();
                try {
                    if (text.isEmpty()) {
                        Intent intent1 = new Intent(this, V1_ScannerActivity.class);
                        intent1.putExtra("qc", "slitting_roll_wise");
                        intent1.putExtra("scan_op", 1);
                        intent1.putExtra("slittering_roll_data", slitteringSequzBarCodeResponse);
                        startActivity(intent1);
                        finish();
                    } else if (slitteringSequzBarCodeResponse == null || slitteringSequzBarCodeResponse.getResultset() == null || slitteringSequzBarCodeResponse.getResultset().getDtlsIndex() == null || slitteringSequzBarCodeResponse.getResultset().getDtlsIndex().isEmpty()) {
                        DialogHelper.showWarningDialog(this, "Warning Message", "No data available for the scanned batch.");
                    }else {
                        Intent intent = new Intent(this, V1_ScannerActivity.class);
                        intent.putExtra("qc", "slitting_roll_wise");
                        intent.putExtra("scan_op", 2);
                        intent.putExtra("batch_scan", _batchNoScanET.getText().toString());
                        intent.putExtra("slittering_roll_data", slitteringSequzBarCodeResponse);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    DialogHelper.showWarningDialog(this, "Warning Message", "Please scan batch first.");
                }
                break;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setUp() {
        mToolbar = findViewById(R.id.toolbar);

        mToolbar.setTitle(R.string.slitting_squeezing_roll);
        setSupportActionBar(mToolbar);
        @SuppressLint("UseCompatLoadingForDrawables") final Drawable backArrow = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
        backArrow.setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_ATOP);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(backArrow);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        _sourceNameSpinner = findViewById(R.id.sourceNameSpinner);
        _companyNameSpinner = findViewById(R.id.companyNameSpinner);
        _processNameSpinner = findViewById(R.id.processNameSpinner);
        _floorSpinner = findViewById(R.id.floorSpinner);
        _machineNameSpinner = findViewById(R.id.machineNameSpinner);
        _shiftNameSpinner = findViewById(R.id.shiftNameSpinner);
        _resultNameSpinner = findViewById(R.id.resultNameSpinner);

        _productionDate = findViewById(R.id.productionDate);
        _productionDate.setOnClickListener(this);
//        _productionTime = findViewById(R.id.productionTime);
//        _productionTime.setOnClickListener(this);
        _processStartDate = findViewById(R.id.processStartDate);
        _processStartDate.setOnClickListener(this);
//        _processStartTime = findViewById(R.id.processStartTime);
//        _processStartTime.setOnClickListener(this);
        _processEndDate = findViewById(R.id.processEndDate);
        _processEndDate.setOnClickListener(this);
//        _processEndTime = findViewById(R.id.processEndTime);
//        _processEndTime.setOnClickListener(this);

        _barcodeScan = findViewById(R.id.barcodeScan);
        _barcodeScan.setOnClickListener(this);

        _progressBar = findViewById(R.id.progressBar);

        _finishProductionReceiveRecyclerView = findViewById(R.id.finishProductionReceiveRecyclerView);

        _batchNoScanET = findViewById(R.id.batchNoScanET);
        _barcodeScanET = findViewById(R.id.barcodeScanET);
        _overFeedET = findViewById(R.id.overFeedET);
        _temparatureET = findViewById(R.id.temparatureET);
        _speedET = findViewById(R.id.speedET);
        _steamET = findViewById(R.id.steamET);
        _remarkET = findViewById(R.id.remarkET);
        _reSlittingET = findViewById(R.id.reSlittingET);
        _productionTimeHour = findViewById(R.id.productionTimeHour);
        _productionTimeMin = findViewById(R.id.productionTimeMin);
        _processStartTimeHour = findViewById(R.id.processStartTimeHour);
        _processStartTimeMin = findViewById(R.id.processStartTimeMin);
        _processEndTimeHour = findViewById(R.id.processEndTimeHour);
        _processEndTimeMin = findViewById(R.id.processEndTimeMin);

        _scannedBatchNoTV = findViewById(R.id.scannedBatchNoTV);
        _noRollTV = findViewById(R.id.noRollTV);
        _scannedNoRollTV = findViewById(R.id.scannedNoRollTV);
        _blRollTV = findViewById(R.id.blRollTV);

        _selectBarcodeCheckbox = findViewById(R.id.selectBarcodeCheckbox);
        _selectBarcodeCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            try {
                if(isChecked) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        dtlsIndexArrayList.forEach(item -> item.setBarcode_status(true));
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        dtlsIndexArrayList.forEach(item -> item.setBarcode_status(false));
                    }
                }
                setScenedInformation();
            }catch (Exception e){
                Log.d(TAG, "setUp: ");
            }

            finishProductionRecyclerAdapter.notifyDataSetChanged();
        });

        setDataUI();
    }

    @SuppressLint("SimpleDateFormat")
    private void setDataUI() {
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        simpleTimeFormat = new SimpleDateFormat("HH:mm");
        String currentDate = simpleDateFormat.format(calendar.getTime());
        String currentTime = simpleTimeFormat.format(calendar.getTime());
        _productionDate.setText(currentDate);
        _processEndDate.setText(currentDate);
        _processStartDate.setText(currentDate);
//        _productionTime.setText(currentTime);
//        _processEndTime.setText(currentTime);
//        _processStartTime.setText(currentTime);

        if(barcodeScan != null && op == 1){
            scannedBatch = barcodeScan;
        }

        if(barcodeScan != null && op == 2){
            _barcodeScanET.setText(barcodeScan);
            _batchNoScanET.setText(scannedBatch);
            try {
                setDataToUI(slitteringSequzBarCodeResponse);
            }catch (Exception e){
                DialogHelper.showWarningDialog(this, "Message", "Please try again.");
            }
        }
        if(scannedBatch != null && op == 1){
            fetchCompactingBatchScan(scannedBatch);
        }

        setScenedInformation();

        setTextViewGradientColor(_barcodeScanET);
        setTextViewGradientColor(_batchNoScanET);
        _scannedBatchNoTV.setText(_batchNoScanET.getText().toString());

        manipulateTimeField();
    }

    private void manipulateTimeField() {
        _productionTimeHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String hourText = s.toString();
                if (!hourText.isEmpty()) {
                    int hour = Integer.parseInt(hourText);
                    if (hour > 23) {
                        _productionTimeHour.setText("23");
                        _productionTimeHour.setSelection(_productionTimeHour.getText().length());
                    }
                    if (hourText.length() >= 2) {
                        _productionTimeMin.requestFocus();
                    }
                }
            }
        });
        _productionTimeMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String minuteText = s.toString();
                if (!minuteText.isEmpty()) {
                    int minute = Integer.parseInt(minuteText);
                    if (minute > 59) {
                        _productionTimeMin.setText("59");
                        _productionTimeMin.setSelection(_productionTimeMin.getText().length());
                    }
                }
            }
        });
        _processStartTimeHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String hourText = s.toString();
                if (!hourText.isEmpty()) {
                    int hour = Integer.parseInt(hourText);
                    if (hour > 23) {
                        _processStartTimeHour.setText("23");
                        _processStartTimeHour.setSelection(_processStartTimeHour.getText().length());
                    }
                    if (hourText.length() >= 2) {
                        _processStartTimeMin.requestFocus();
                    }
                }
            }
        });
        _processStartTimeMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String minuteText = s.toString();
                if (!minuteText.isEmpty()) {
                    int minute = Integer.parseInt(minuteText);
                    if (minute > 59) {
                        _processStartTimeMin.setText("59");
                        _processStartTimeMin.setSelection(_processStartTimeMin.getText().length());
                    }
                }
            }
        });
        _processEndTimeHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String hourText = s.toString();
                if (!hourText.isEmpty()) {
                    int hour = Integer.parseInt(hourText);
                    if (hour > 23) {
                        _processEndTimeHour.setText("23");
                        _processEndTimeHour.setSelection(_processEndTimeHour.getText().length());
                    }
                    if (hourText.length() >= 2) {
                        _processEndTimeMin.requestFocus();
                    }
                }
            }
        });
        _processEndTimeMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String minuteText = s.toString();
                if (!minuteText.isEmpty()) {
                    int minute = Integer.parseInt(minuteText);
                    if (minute > 59) {
                        _processEndTimeMin.setText("59");
                        _processEndTimeMin.setSelection(_processEndTimeMin.getText().length());
                    }
                }
            }
        });
    }

    private void setTextViewGradientColor(TextView textView) {
        Shader shader = new LinearGradient(0, 0, 0, textView.getTextSize(),
                new int[]{Color.BLUE,  Color.parseColor("#810366")},
                null, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);

        textView.getPaint().setShader(shader);
    }

    private void setScenedInformation() {
        if (slitteringSequzBarCodeResponse != null
                && slitteringSequzBarCodeResponse.getResultset() != null
                && slitteringSequzBarCodeResponse.getResultset().getDtlsIndex() != null
                && slitteringSequzBarCodeResponse.getResultset().getDtlsIndex().size() > 0) {

            _noRollTV.setText(String.valueOf(slitteringSequzBarCodeResponse.getResultset().getDtlsIndex().size()));

            int checked = 0;
            for (int i = 0; i < slitteringSequzBarCodeResponse.getResultset().getDtlsIndex().size(); i++) {
                if (slitteringSequzBarCodeResponse.getResultset().getDtlsIndex().get(i).getBarcode_status() || slitteringSequzBarCodeResponse.getResultset().getDtlsIndex().get(i).getChecked().equals("1")) {
                    checked++;
                }
            }

            _scannedNoRollTV.setText(String.valueOf(checked));
            _blRollTV.setText(String.valueOf(slitteringSequzBarCodeResponse.getResultset().getDtlsIndex().size() - checked));
        } else {
            Log.e(TAG, "Compact batch scan response or its properties are null.");
        }
    }

    public void onBackPressed() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Leave this Page?")
                .setContentText("Do you want to leave this page? Unsaved changes will not be available.")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener(sDialog -> {
                    sDialog.cancel();
                    clearData();
                    finish();
                })
                .setCancelClickListener(SweetAlertDialog::cancel)
                .show();
    }

    @Override
    public void onSelectClick(int position, View v) {
        if(dtlsIndexArrayList.get(position).getBarcode_status()){
            dtlsIndexArrayList.get(position).setBarcode_status(false);
        }
        setScenedInformation();
    }

    private void saveLocalDataData() {
        SharedPreferences sharedPreferences = getSharedPreferences("SlittingSqeezingPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("source_position", Integer.parseInt(sourceId));
        editor.putInt("wc_position", Integer.parseInt(companyId));
        editor.putInt("wc_floor_position", Integer.parseInt(floorId));
        editor.putInt("mc_position", Integer.parseInt(machineId));
        editor.putInt("process_position", Integer.parseInt(processId));
//        editor.putInt("shift_position", Integer.parseInt(shiftId));
        editor.putInt("result_position", Integer.parseInt(resultId));
        editor.putString("temp", _temparatureET.getText().toString());
        editor.putString("speed", _speedET.getText().toString());
        editor.putString("overfeed", _overFeedET.getText().toString());
        editor.putString("steam", _steamET.getText().toString());
        editor.putString("process_start_hour", _processStartTimeHour.getText().toString());
        editor.putString("process_start_min", _processStartTimeMin.getText().toString());
        editor.putString("process_end_hour", _processEndTimeHour.getText().toString());
        editor.putString("process_end_min", _processEndTimeMin.getText().toString());

        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("SlittingSqeezingPref", MODE_PRIVATE);

        saved_source_position = String.valueOf(sharedPreferences.getInt("source_position", 0));
        saved_wc_position = String.valueOf(sharedPreferences.getInt("wc_position", 0));
        saved_wc_floor_position = String.valueOf(sharedPreferences.getInt("wc_floor_position", 0));
        saved_mc_position = String.valueOf(sharedPreferences.getInt("mc_position", 0));
        saved_process_position = String.valueOf(sharedPreferences.getInt("process_position", 0));
        saved_shift_position = String.valueOf(sharedPreferences.getInt("shift_position", 0));
        saved_result_position = String.valueOf(sharedPreferences.getInt("result_position", 0));

        _temparatureET.setText(sharedPreferences.getString("temp", ""));
        _speedET.setText(sharedPreferences.getString("speed", ""));
        _overFeedET.setText(sharedPreferences.getString("overfeed", ""));
        _steamET.setText(sharedPreferences.getString("steam", ""));
        _processStartTimeHour.setText(sharedPreferences.getString("process_start_hour", ""));
        _processStartTimeMin.setText(sharedPreferences.getString("process_start_min", ""));
        _processEndTimeHour.setText(sharedPreferences.getString("process_end_hour", ""));
        _processEndTimeMin.setText(sharedPreferences.getString("process_end_min", ""));
    }
    private void clearData() {
        SharedPreferences sharedPreferences = getSharedPreferences("SlittingSqeezingPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}