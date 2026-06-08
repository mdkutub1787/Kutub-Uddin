package com.logicsoftbd.lsl.ui.compacting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.model.StenteringBatchScanResponse;
import com.logicsoftbd.lsl.ui.dyeingProduction.CompactingBatchScanRecyclerViewAdapter;
import com.logicsoftbd.lsl.ui.stentering.StenteringFunctionalBatchScanRecyclerViewAdapter;
import com.logicsoftbd.lsl.data.model.Process;
import com.logicsoftbd.lsl.data.network.model.CompactBatchScanResponse;
import com.logicsoftbd.lsl.data.network.model.CompactingDefaultResponse;
import com.logicsoftbd.lsl.data.network.model.FloorWiseMachineResponse;
import com.logicsoftbd.lsl.data.network.model.SlitteringSequzCompanyWiseFloorResponse;
import com.logicsoftbd.lsl.data.network.model.SlitteringSequzSaveResponse;
import com.logicsoftbd.lsl.data.network.model.StenteringFunctionalBatchScanResponse;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_ScannerActivity;
import com.logicsoftbd.lsl.utils.ApiUtils;
import com.logicsoftbd.lsl.utils.VerticalSpacingItemDecorator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompactingActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CompactingActivity";
    public static final String EXTRA_BUNDLE_ID = "extra_bundle_id";
    private Integer op = 0;
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;
    private Toolbar mToolbar;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat, simpleTimeFormat;

    private Spinner _companyNameSpinner, _processNameSpinner, _floorSpinner, _machineNameSpinner, _nextProcessSpinner, _resultSpinner, _shiftNameSpinner, _productionTypeSpinner;
    private TextView _batchIdTV, _mcGroupTV, _colorIdTV, _dyeingEndDateTV, _jobNoTV, _dyeingTimeTV, _buyerTV, _mcFloorTV, _orderNoTV, _extNoTV, _trimsWeightTV;
    private EditText _batchNoScanET, _barcodeScanET, _issuesChallanNoET, _lengthShrinkageET, _widthShrinkageET, _spiralityET, _serviceBookingET, _chemicalNameET,_temparatureET, _stretchET, _overFeedET, _feedInET, _pinningET, _speedmET, _advanceProdQtyET, _remarksET;
    private Button _productionDate, _processStartDate, _processStartTime, _processEndDate, _processEndTime, _saveButton, _refreshButton;
    private ImageButton _batchScan, _batchSearch, _barcodeScan;
    private CheckBox _reDyeingCheckBox, _checkAllCheckBox, _checkWithCheckBox, _checkTubularCheckBox;
    private RecyclerView _compactingScanListRecyclerView;
    private ProgressBar _progressBar;
    private ArrayList<String> companyNameList = new ArrayList<>();
    private ArrayList<String> companyIdList = new ArrayList<>();
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
    private CompactingBatchScanRecyclerViewAdapter compactingBatchScanRecyclerViewAdapter;
    private List<CompactBatchScanResponse.DtlsIndex> dtlsIndexArrayList = new ArrayList<>();
    private StenteringFunctionalBatchScanRecyclerViewAdapter stenteringFunctionalBatchScanRecyclerViewAdapter;
    private List<StenteringFunctionalBatchScanResponse.Resultset> batchIndexArrayList = new ArrayList<>();
    private CompactBatchScanResponse compactBatchScanResponse = new CompactBatchScanResponse();

    private String companyId, processId, productionTypeId, floorId, machineId, nextProcessId, resultId, shiftId = "0";
    private String USER_ID, BATCH_ID, BATCH_NO, TRIMS_WGT, entry_form_no, IS_RE_DYEING, barcodeScan = "";

    private String base_url ="", scannedBatch, userID, mType = "";
    private String urladdress;

    private Process mProcess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compacting_test);

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        base_url = (_preferences.getString("base_url", ""));
        userID = _preferences.getString("login_userid", "");

        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);

        Intent intent = getIntent();
        barcodeScan = intent.getStringExtra("barcodeScan");
        scannedBatch = intent.getStringExtra("batch_scan");
        compactBatchScanResponse = (CompactBatchScanResponse) intent.getSerializableExtra("compacting_roll_data");
        op = intent.getIntExtra("scan_op", 0);

        Log.d(TAG, "onCreate: "+barcodeScan+" "+scannedBatch+" "+op);

        setUp();
        setupCompactingProdScanRecyclerView();
        invalidateOptionsMenu();
        requestForCompactingData();
    }

    private void requestForCompactingData() {
        apiInterface.compactingDefaultResponse("33").enqueue(new Callback<CompactingDefaultResponse>() {
            @Override
            public void onResponse(Call<CompactingDefaultResponse> call, Response<CompactingDefaultResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getResultset().getServiceCompany().size() > 0){
                        for(int i=0; i<response.body().getResultset().getServiceCompany().size(); i++){
                            companyNameList.add(response.body().getResultset().getServiceCompany().get(i).getCompany());
                            companyIdList.add(response.body().getResultset().getServiceCompany().get(i).getId());
                        }
                        setUpCompanySpinner();
                    }

                    if(response.body().getResultset().getProductionType().size() > 0){
                        for(int i=0; i<response.body().getResultset().getProductionType().size(); i++){
                            productionTypeNameList.add(response.body().getResultset().getProductionType().get(i).getName());
                            productionTypeIdList.add(response.body().getResultset().getProductionType().get(i).getId());
                        }
                        setUpProductionTypeSpinner();
                    }

                    if(response.body().getResultset().getProcessName().size() > 0){
                        for(int i=0; i<response.body().getResultset().getProcessName().size(); i++){
                            processNameList.add(response.body().getResultset().getProcessName().get(i).getName());
                            processIdList.add(response.body().getResultset().getProcessName().get(i).getId());
                        }
                        setUpProcessSpinner();
                    }
                    if(response.body().getResultset().getNextProcess().size() > 0){
                        for(int i=0; i<response.body().getResultset().getNextProcess().size(); i++){
                            nextPrcessNameList.add(response.body().getResultset().getNextProcess().get(i).getName());
                            nextProcessIdList.add(response.body().getResultset().getNextProcess().get(i).getId());
                        }
                        setUpNextProcessSpinner();
                    }
                    if(response.body().getResultset().getResult().size() > 0){
                        for(int i=0; i<response.body().getResultset().getResult().size(); i++){
                            resultNameList.add(response.body().getResultset().getResult().get(i).getName());
                            resultIdList.add(response.body().getResultset().getResult().get(i).getId());
                        }
                        setUpResultSpinner();
                    }
                    if(response.body().getResultset().getShiftName().size() > 0){
                        for(int i=0; i<response.body().getResultset().getShiftName().size(); i++){
                            shiftNameList.add(response.body().getResultset().getShiftName().get(i).getName());
                            shiftIdLiest.add(response.body().getResultset().getShiftName().get(i).getId());
                        }
                        setUpShiftSpinner();
                    }
                }
            }

            @Override
            public void onFailure(Call<CompactingDefaultResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: ##########"+t.getMessage());
            }
        });
    }

    private void setUpCompanySpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, companyNameList);
        _companyNameSpinner.setAdapter(spinnerArrayAdapter);

        _companyNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                companyId = companyIdList.get(position);
                requestForCompanyWiseFloor(companyId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void requestForCompanyWiseFloor(String companyId) {
        apiInterface.slitteringCompanyWiseFloorResponseCall(companyId).enqueue(new Callback<SlitteringSequzCompanyWiseFloorResponse>() {
            @Override
            public void onResponse(Call<SlitteringSequzCompanyWiseFloorResponse> call, Response<SlitteringSequzCompanyWiseFloorResponse> response) {
                if(response.isSuccessful()){
                    setUpCompanyWiseFloorSpinner(response.body().getResultset());
                }
            }

            @Override
            public void onFailure(Call<SlitteringSequzCompanyWiseFloorResponse> call, Throwable t) {

            }
        });
    }

    private void setUpProductionTypeSpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, productionTypeNameList);
        _productionTypeSpinner.setAdapter(spinnerArrayAdapter);

        _productionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productionTypeId = productionTypeIdList.get(position);
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
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, floodNameList);
        _floorSpinner.setAdapter(spinnerArrayAdapter);

        _floorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                floorId = floorIdList.get(position);
                requestForCompanyFloorMachine(floorIdList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void requestForCompanyFloorMachine(String companyFloorId) {
        apiInterface.slitteringCompanyFloorMachineResponseCall(companyFloorId).enqueue(new Callback<FloorWiseMachineResponse>() {
            @Override
            public void onResponse(Call<FloorWiseMachineResponse> call, Response<FloorWiseMachineResponse> response) {
                if(response.isSuccessful()){
                    setUpCompanyFloorMachineSpinner(response.body().getResultset());
                }
            }

            @Override
            public void onFailure(Call<FloorWiseMachineResponse> call, Throwable t) {

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
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, machineNameList);
        _machineNameSpinner.setAdapter(spinnerArrayAdapter);

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
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, processNameList);
        _processNameSpinner.setAdapter(spinnerArrayAdapter);

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

    private void setUpNextProcessSpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nextPrcessNameList);
        _nextProcessSpinner.setAdapter(spinnerArrayAdapter);

        _nextProcessSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nextProcessId = nextProcessIdList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpResultSpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, resultNameList);
        _resultSpinner.setAdapter(spinnerArrayAdapter);

        _resultSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                resultId = resultIdList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpShiftSpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, shiftNameList);
        _shiftNameSpinner.setAdapter(spinnerArrayAdapter);

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

    private void requestForCompactingBatchScan(String batch, String s) {
        _progressBar.setVisibility(View.VISIBLE);
        apiInterface.compactingBatchScanResponse(batch,"0", s).enqueue(new Callback<CompactBatchScanResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<CompactBatchScanResponse> call, Response<CompactBatchScanResponse> response) {
                _progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body().getResultset() != null) {
                        compactBatchScanResponse = response.body();
                        assert response.body() != null;
                        dtlsIndexArrayList = compactBatchScanResponse.getResultset().getDtlsIndex();
                        if(response.body().getResultset().getDtlsIndex().size() > 0){
                            dtlsIndexArrayList = response.body().getResultset().getDtlsIndex();
                            for (CompactBatchScanResponse.DtlsIndex dtlsIndex : dtlsIndexArrayList) {
                                dtlsIndex.setBarcode_status(false);
                                dtlsIndex.setCheck_status(true);
                            }
                        }
                        setDataToUI(compactBatchScanResponse);
                        setupCompactingProdScanRecyclerView();
                    }else{
                        warningAlertDialog("Warning Message", response.body().getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<CompactBatchScanResponse> call, Throwable t) {
                _progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: ###########"+t.getMessage());
            }
        });
    }

    private void setDataToUI(CompactBatchScanResponse response) {
        BATCH_ID = response.getResultset().getInputAreaIndex().getBatchId();
        BATCH_NO = response.getResultset().getInputAreaIndex().getBatchNo();
        TRIMS_WGT = response.getResultset().getInputAreaIndex().getTrimsWgt();
        entry_form_no = response.getResultset().getInputAreaIndex().getEntryFormNo();
        companyId = response.getResultset().getInputAreaIndex().getCompanyId();

        _companyNameSpinner.setSelection(companyIdList.indexOf(companyId));

        if(response.getResultset().getReferenceIndex() != null){
            _batchIdTV.setText(response.getResultset().getReferenceIndex().getBatchId());
            _extNoTV.setText(response.getResultset().getReferenceIndex().getExtentionNo());
            _colorIdTV.setText(response.getResultset().getReferenceIndex().getColor());
//                        _slittingDateTV.setText(response.body().getResultset().getReferenceIndex().get());
            _jobNoTV.setText(response.getResultset().getReferenceIndex().getJobNo());
            _buyerTV.setText(response.getResultset().getReferenceIndex().getBuyer());
//                        _mcFloorTV.setText(response.body().getResultset().getReferenceIndex().get());
//                        _orderNoTV.setText(response.body().getResultset().getReferenceIndex().get());
//                        _trimsWeightTV.setText(response.body().getResultset().getReferenceIndex().get());

            if(response.getResultset().getReferenceIndex().getExtentionNo() != "" || response.getResultset().getReferenceIndex().getExtentionNo() != "0"){
                _reDyeingCheckBox.setChecked(true);
            }
        }
        dtlsIndexArrayList = response.getResultset().getDtlsIndex();
        if(response.getResultset().getDtlsIndex().size() > 0){
            if(barcodeScan != null && op == 2){
                for (CompactBatchScanResponse.DtlsIndex dtlsIndex : dtlsIndexArrayList) {
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

    private void saveRequestCompactingObject() throws JSONException {
        spliteProcessStartTime();

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
        index_obj.put("PRODUCTION_TYPE", "");
        index_obj.put("PROCESS_ID", processId);
        index_obj.put("NEXT_PROCESS_ID", nextProcessId);
        index_obj.put("RESULT", resultId);
        index_obj.put("PRODUCTION_DATE", _productionDate.getText().toString());
        index_obj.put("PROCESS_START_DATE", _processStartDate.getText().toString());
        index_obj.put("PROCESS_END_DATE", _processEndDate.getText().toString());
        index_obj.put("START_HOURS", processStartTimeList.get(0));
        index_obj.put("START_MINUTES", processStartTimeList.get(1));
        index_obj.put("END_HOURS", processStartTimeList.get(0));
        index_obj.put("END_MINUTES", processStartTimeList.get(1));
        index_obj.put("SHIFT_NAME", shiftId);
        index_obj.put("ADVANCED_PROD_QTY", _advanceProdQtyET.getText().toString());
        index_obj.put("FLOOR", floorId);
        index_obj.put("MACHINE_NAME", machineId);
        index_obj.put("WIDTH_SHRINKAGE", "");
        index_obj.put("LENGTH_SHRINKAGE", "");
        index_obj.put("PINNING", "");
        index_obj.put("FEED_IN", "");
        index_obj.put("STRETCH", "");
        index_obj.put("SPEED", "");
        index_obj.put("STEAM", "");
        index_obj.put("OVER_FEED", _overFeedET.getText().toString());
        index_obj.put("TEMPARATURE", _temparatureET.getText().toString());
        index_obj.put("CHEMICAL_NAME", "");
        index_obj.put("IS_RE_DYEING", IS_RE_DYEING);
        index_obj.put("REMARK", _remarksET.getText().toString());
        index_obj.put("USER_ID", userID);

        data_obj.put("index", index_obj);

        for(int i=0; i<dtlsIndexArrayList.size(); i++){
            if(dtlsIndexArrayList.get(i).getCheck_status()){
                JSONObject dtls_obj = new JSONObject();
                dtls_obj.put("CHECKED", "1");
                dtls_obj.put("PROD_ID", dtlsIndexArrayList.get(i).getProdId());
                dtls_obj.put("FIN_DIA", dtlsIndexArrayList.get(i).getFinDia());
                dtls_obj.put("ROLL_NO", dtlsIndexArrayList.get(i).getRollNo());
                dtls_obj.put("ROLL_ID", dtlsIndexArrayList.get(i).getRollId());
                dtls_obj.put("NO_OF_ROLL", dtlsIndexArrayList.get(i).getNoOfRoll());
                dtls_obj.put("BATCH_QNTY", dtlsIndexArrayList.get(i).getBatchQnty());
                dtls_obj.put("PROD_QTY", dtlsIndexArrayList.get(i).getProdQty());
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
        _progressBar.setVisibility(View.VISIBLE);
        apiInterface.saveUpdateSlittingSquzCall(body).enqueue(new Callback<SlitteringSequzSaveResponse>() {
            @Override
            public void onResponse(Call<SlitteringSequzSaveResponse> call, Response<SlitteringSequzSaveResponse> response) {
                Log.d(TAG, "onResponse: ######### "+response.toString());
                _progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    refreshDataFromUI();
                    Toast.makeText(CompactingActivity.this, response.body().getResultset().getSaveMsg(), Toast.LENGTH_SHORT).show();
//                    requestForStenteringFunctionalByBatchScan(response.body().getResultset().getBatchId());
                }else {
                    Toast.makeText(CompactingActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SlitteringSequzSaveResponse> call, Throwable t) {
                _progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: ##### "+t.getMessage());
            }
        });
    }

    private void setupCompactingProdScanRecyclerView() {
        Log.d(TAG, "setupCompactingProdScanRecyclerView: *********"+dtlsIndexArrayList.size());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _compactingScanListRecyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(_compactingScanListRecyclerView.getContext(),
                linearLayoutManager.getOrientation());
        _compactingScanListRecyclerView.addItemDecoration(dividerItemDecoration);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(5);
        _compactingScanListRecyclerView.addItemDecoration(itemDecorator);
        compactingBatchScanRecyclerViewAdapter = new CompactingBatchScanRecyclerViewAdapter(this, dtlsIndexArrayList);
        _compactingScanListRecyclerView.setAdapter(compactingBatchScanRecyclerViewAdapter);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        _compactingScanListRecyclerView.setLayoutManager(linearLayoutManager);
//        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
//        _compactingScanListRecyclerView.addItemDecoration(itemDecorator);
//        compactingBatchScanRecyclerViewAdapter = new CompactingBatchScanRecyclerViewAdapter(this, dtlsIndexArrayList);
//        _compactingScanListRecyclerView.setAdapter(compactingBatchScanRecyclerViewAdapter);
    }

    private void spliteProcessStartTime() {
        String processStartTime = _processEndTime.getText().toString().trim();
        String[] arrOfStr = processStartTime.split(":", 2);
        for (String a : arrOfStr)
            processStartTimeList.add(a);
    }

    private void datepicker(final Button setdate) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        setdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void timePicker(Button processStartTime) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                processStartTime.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void setUp() {
        mToolbar = findViewById(R.id.toolbar);

        mToolbar.setTitle(R.string.compacting);
        setSupportActionBar(mToolbar);
        final Drawable backArrow = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24); // assuming your back button icon is ic_arrow_back
        backArrow.setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        _companyNameSpinner = findViewById(R.id.companyNameSpinner);
        _processNameSpinner = findViewById(R.id.processNameSpinner);
        _floorSpinner = findViewById(R.id.floorSpinner);
        _machineNameSpinner = findViewById(R.id.machineNameSpinner);
        _nextProcessSpinner = findViewById(R.id.nextProcessSpinner);
        _resultSpinner = findViewById(R.id.resultSpinner);
        _shiftNameSpinner = findViewById(R.id.shiftNameSpinner);
        _productionTypeSpinner = findViewById(R.id.productionTypeSpinner);

        _productionDate = findViewById(R.id.productionDate);
        _productionDate.setOnClickListener(this);
        _processStartDate = findViewById(R.id.processStartDate);
        _processStartDate.setOnClickListener(this);
        _processStartTime = findViewById(R.id.processStartTime);
        _processStartTime.setOnClickListener(this);
        _processEndDate = findViewById(R.id.processEndDate);
        _processEndDate.setOnClickListener(this);
        _processEndTime = findViewById(R.id.processEndTime);
        _processEndTime.setOnClickListener(this);

        _batchScan = findViewById(R.id.batchScan);
        _batchScan.setOnClickListener(this);
        _batchSearch = findViewById(R.id.batchSearch);
        _batchSearch.setOnClickListener(this);
        _barcodeScan = findViewById(R.id.barcodeScan);
        _barcodeScan.setOnClickListener(this);

        _progressBar = findViewById(R.id.progressBar);

        _compactingScanListRecyclerView = findViewById(R.id.compactingScanListRecyclerView);

        _batchIdTV = findViewById(R.id.batchIdTV);
        _mcGroupTV = findViewById(R.id.mcGroupTV);
        _colorIdTV = findViewById(R.id.colorIdTV);
        _dyeingEndDateTV = findViewById(R.id.dyeingEndDateTV);
        _jobNoTV = findViewById(R.id.jobNoTV);
        _dyeingTimeTV = findViewById(R.id.dyeingTimeTV);
        _buyerTV = findViewById(R.id.buyerTV);
        _mcFloorTV = findViewById(R.id.mcFloorTV);
        _orderNoTV = findViewById(R.id.orderNoTV);
        _extNoTV = findViewById(R.id.extNoTV);
        _trimsWeightTV = findViewById(R.id.trimsWeightTV);

        _batchNoScanET = findViewById(R.id.batchNoScanET);
        _barcodeScanET = findViewById(R.id.barcodeScanET);
        _issuesChallanNoET = findViewById(R.id.issuesChallanNoET);
        _lengthShrinkageET = findViewById(R.id.lengthShrinkageET);
        _widthShrinkageET = findViewById(R.id.widthShrinkageET);
        _spiralityET = findViewById(R.id.spiralityET);
        _serviceBookingET = findViewById(R.id.serviceBookingET);
        _chemicalNameET = findViewById(R.id.chemicalNameET);
        _temparatureET = findViewById(R.id.temparatureET);
        _stretchET = findViewById(R.id.stretchET);
        _overFeedET = findViewById(R.id.overFeedET);
        _advanceProdQtyET = findViewById(R.id.advanceProdQtyET);
        _remarksET = findViewById(R.id.remarksET);

        _reDyeingCheckBox = findViewById(R.id.reDyeingCheckBox);
        _reDyeingCheckBox.setOnClickListener(view -> {
            if(((CompoundButton) view).isChecked()){
                IS_RE_DYEING = "1";
            }else {
                IS_RE_DYEING = "0";
            }
        });

        _checkAllCheckBox = findViewById(R.id.checkAllCheckBox);
        _checkWithCheckBox = findViewById(R.id.checkWithCheckBox);
        _checkTubularCheckBox = findViewById(R.id.checkTubularCheckBox);

        _checkAllCheckBox.setChecked(true);

        _checkAllCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                _checkWithCheckBox.setChecked(false);
                _checkTubularCheckBox.setChecked(false);
                updateCheckStatus(0, true);
            } else {
                updateCheckStatus(0, false);
            }
        });

        _checkWithCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                _checkAllCheckBox.setChecked(false);
                _checkTubularCheckBox.setChecked(false);
                updateCheckStatus(1, true);
            } else {
                updateCheckStatus(1, false);
            }
        });

        _checkTubularCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                _checkAllCheckBox.setChecked(false);
                _checkWithCheckBox.setChecked(false);
                updateCheckStatus(2, true);
            } else {
                updateCheckStatus(2, false);
            }
        });

        setDataUI();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateCheckStatus(int statusCode, boolean isChecked) {
        for (CompactBatchScanResponse.DtlsIndex item : dtlsIndexArrayList) {
            boolean shouldCheck = false;
            if (statusCode == 0) {
                shouldCheck = isChecked;
            } else if (statusCode == 1 && "1".equalsIgnoreCase(item.getDiaType())) {
                shouldCheck = isChecked;
            } else if (statusCode == 2 && "2".equalsIgnoreCase(item.getDiaType())) {
                shouldCheck = isChecked;
            }
            item.setCheck_status(shouldCheck);
        }
        compactingBatchScanRecyclerViewAdapter.notifyDataSetChanged();
    }
    private void setDataUI() {
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        simpleTimeFormat = new SimpleDateFormat("HH:mm");
        String currentDate = simpleDateFormat.format(calendar.getTime());
        String currentTime = simpleTimeFormat.format(calendar.getTime());
        _productionDate.setText(currentDate);
        _processStartDate.setText(currentDate);
        _processStartTime.setText(currentTime);
        _processEndDate.setText(currentDate);
        _processEndTime.setText(currentTime);

        if(barcodeScan != null && op == 1){
            _batchNoScanET.setText(barcodeScan);
            scannedBatch = barcodeScan;
        }

        if(barcodeScan != null && op == 2){
            _barcodeScanET.setText(barcodeScan);
            _batchNoScanET.setText(scannedBatch);
            setDataToUI(compactBatchScanResponse);
        }
        if(scannedBatch != null && op == 1){
            requestForCompactingBatchScan(scannedBatch, "33");
        }

        setTextViewGradientColor(_barcodeScanET);
        setTextViewGradientColor(_batchNoScanET);
    }

    private void setTextViewGradientColor(TextView textView) {
        Shader shader = new LinearGradient(0, 0, 0, textView.getTextSize(),
                new int[]{Color.BLUE,  Color.parseColor("#810366")},
                null, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);

        textView.getPaint().setShader(shader);
    }

    private void refreshDataFromUI() {
        dtlsIndexArrayList.clear();
        batchIndexArrayList.clear();
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
//        setupCompactingProdScanRecyclerView();
        compactingBatchScanRecyclerViewAdapter.notifyDataSetChanged();

        requestForCompactingData();
        _batchNoScanET.setText("");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fabric, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            try {
                if(dtlsIndexArrayList.size() > 0){
                    saveRequestCompactingObject();
                }else {
                    Toast.makeText(this, "Batch or functional batch data is empty.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
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


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.batchSearch:
                String functionalBatch = _batchNoScanET.getText().toString();
                if(!functionalBatch.equals("")){
                    if(dtlsIndexArrayList.size() == 0){
                        requestForCompactingBatchScan(functionalBatch, "33");
                    }
                }else {
                    _batchNoScanET.setError(getString(R.string.functional_batch_error));
                }
                break;
            case R.id.productionDate:
                datepicker(_productionDate);
                break;
            case R.id.processStartDate:
                datepicker(_processStartDate);
                break;
            case R.id.processEndDate:
                datepicker(_processEndDate);
                break;
            case R.id.processEndTime:
                timePicker(_processEndTime);
                break;
            case R.id.saveButton:
                try {
                    if(dtlsIndexArrayList.size() > 0){
                        saveRequestCompactingObject();
                    }else {
                        Toast.makeText(this, "Batch or functional batch data is empty.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.refreshButton:
                refreshDataFromUI();
                break;
            case R.id.toolbar:
                finish();
                break;
            case R.id.batchScan:
                Intent intent1 = new Intent(this, V1_ScannerActivity.class);
                intent1.putExtra("qc", "compacting");
                intent1.putExtra("scan_op", 1);
                intent1.putExtra("compacting_roll_data", compactBatchScanResponse);
                startActivity(intent1);
                finish();
                break;
            case R.id.barcodeScan:
                String text = _batchNoScanET.getText().toString().trim();
                if(text.isEmpty()){
                    warningAlertDialog("Warning Message", "Please scan batch first.");
                }else{
                    Intent intent = new Intent(this, V1_ScannerActivity.class);
                    intent.putExtra("qc", "compacting");
                    intent.putExtra("scan_op", 2);
                    intent.putExtra("batch_scan", _batchNoScanET.getText().toString());
                    intent.putExtra("compacting_roll_data", compactBatchScanResponse);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    private void successAlertDialog(){
        new SweetAlertDialog(CompactingActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success Message")
                .setContentText("Date save successfully.")
                .setConfirmText("OK")
                .show();
    }
    private void warningAlertDialog(String title, String message){
        new SweetAlertDialog(CompactingActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText("OK")
                .show();
    }
    private void errorAlertDialog(String title, String message){
        new SweetAlertDialog(CompactingActivity.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText("OK")
                .show();
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
                    finish();
                })
                .setCancelClickListener(sDialog -> sDialog.cancel())
                .show();
    }
}