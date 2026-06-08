package com.logicsoftbd.lsl.ui.dyeingProduction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.model.Process;
import com.logicsoftbd.lsl.data.network.model.CompanyWiseFloorResponse;
import com.logicsoftbd.lsl.data.network.model.DyeingProFunctionalBatchResponse;
import com.logicsoftbd.lsl.data.network.model.DyeingProdBatchScanResponse;
import com.logicsoftbd.lsl.data.network.model.DyeingProdSaveResponse;
import com.logicsoftbd.lsl.data.network.model.DyeingProductionLoadResponse;
import com.logicsoftbd.lsl.data.network.model.FloorWiseMachineResponse;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.process.scanprocess.ScannerActivity;
import com.logicsoftbd.lsl.utils.ApiUtils;
import com.logicsoftbd.lsl.utils.DialogHelper;
import com.logicsoftbd.lsl.utils.VerticalSpacingItemDecorator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DyeingProductionActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "DyeingProductionActivit";
    public static final String EXTRA_RECEIVE_ID = "dyeing_production";
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;
    private Toolbar mToolbar;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat, simpleTimeFormat;
    private Spinner _ldUnLdSpinner, _dyeingTypeSpinner, _companyNameSpinner, _floorNameSpinner,
            _processNameSpinner, _btb_ltbSpinner, _multiBatchLoadingSpinner, _machineNameSpinner,
            _resultSpinner, _fabricSpinner, _shiftNameSpinner, _responsibilitySpinner;
    private Button _processStartDate, _processStartTime, _processEndTime, _productionDate, _processEndDate, _saveButton, _refreshButton;
    private ImageButton _batchScan, _batchSearch, _batchFunctionalScannButton;
    private SegmentedGroup segmented2;
    private ProgressBar _progressBar;
    private RadioButton _loadingBtn, _unloadingBtn;
    private RecyclerView _dyeingScanListRecyclerView, _dyeingFunctionalScanListRecyclerView;
    private TextView _batchIdTV, _extNoTV, _jobNoTV, _buyerTV, _orderNoTV, _colorIdTV, _refNoTV, _fileNoTV, _loadingDateTV, _loadingTimeTV, _processStartDateTVCaption  ;
    private EditText _batchNoScanET, _functionalBatchET, _waterFlowET, _hourlyLoadMeterET;
    private LinearLayout _uploadingOp0, _uploadingOp1, _uploadingOp2, _uploadingOp3, _loadingTimely;
    private DyeingBatchScanRecyclerViewAdapter dyeingBatchScanRecyclerViewAdapter;
    private DyeingFunctionalBatchScanRecyclerViewAdapter dyeingFunctionalBatchScanRecyclerViewAdapter;
    private List<DyeingProdBatchScanResponse.DtlsIndex> dtlsIndexArrayList = new ArrayList<>();
    private List<DyeingProFunctionalBatchResponse.FunctionalBatchIndex> functionalBatchIndices = new ArrayList<>();
    private ArrayList<String> loadUnLoadName = new ArrayList<>();
    private ArrayList<String> loadUnLoadId = new ArrayList<>();
    private ArrayList<String> dyeingName = new ArrayList<>();
    private ArrayList<String> dyeingId = new ArrayList<>();
    private ArrayList<String> processName = new ArrayList<>();
    private ArrayList<String> processId = new ArrayList<>();
    private ArrayList<String> btbLtbName = new ArrayList<>();
    private ArrayList<String> btbLtbId = new ArrayList<>();
    private ArrayList<String> multiBatchLoadingName = new ArrayList<>();
    private ArrayList<String> multiBatchLoadingId = new ArrayList<>();
    private ArrayList<String> companyName = new ArrayList<>();
    private ArrayList<String> companyListId = new ArrayList<>();
    private ArrayList<String> floorName = new ArrayList<>();
    private ArrayList<String> floorId = new ArrayList<>();
    private ArrayList<String> machineName = new ArrayList<>();
    private ArrayList<String> machineId = new ArrayList<>();
    private ArrayList<String> resultName = new ArrayList<>();
    private ArrayList<String> resultId = new ArrayList<>();
    private ArrayList<String> fabricTypeName = new ArrayList<>();
    private ArrayList<String> fabricTypeId = new ArrayList<>();
    private ArrayList<String> resDepName = new ArrayList<>();
    private ArrayList<String> resDepId = new ArrayList<>();
    private ArrayList<String> shiftListName = new ArrayList<>();
    private ArrayList<String> shiftId = new ArrayList<>();
    ArrayList<String> processStartTimeList = new ArrayList<>();
    private String USER_ID, BATCH_ID, BATCH_NO, FUNCTIONAL_NO, EXTENTION_NO, JOB_NO, PO_NO, FILE_NO, REF_NO, BUYER, COLOR_ID, BATCH_TYPE, LOADING, DYEING_TYPE, COMPANY,
            SERVICE_COMPANY, PROCESS_NAME, BTB_LTB, PROCESS_START_DATE, END_HOURS, END_MINUTES, PRODUCTION_DATE, PROCESS_END_DATE, RESULT, SHIFT_NAME, WATER_FLOW,
            FLOOR, MACHINE_NAME, MULTI_BATCH_LOADING, HOUR_LOAD_METER, FABRIC_TYPE, RESPONSIBILITY_DEPT, flood_Id, machine_Id = "";
    private String base_url ="", scannedBatch, mType = "";
    private String urladdress, userID;
    private Process mProcess;
    public static final String EXTRA_BUNDLE_ID = "extra_bundle_id";


    public static Intent getStartIntent(Context context, Process process) {
        Intent intent = new Intent(context, DyeingProductionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_BUNDLE_ID, process);
        intent.putExtras(bundle);
        return intent;
    }
    public static Intent getStartIntent(Context context, Process process, boolean isActivityResult) {
        process.setActivityResult(isActivityResult);
        Intent intent = new Intent(context, DyeingProductionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_BUNDLE_ID, process);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dyeing_production);

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        base_url = (_preferences.getString("base_url", ""));
        userID =  (_preferences.getString("login_userid", ""));

        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);

        Log.d(TAG, "onCreate: ---------->"+USER_ID);

        setUp();
        requestForDyeingProductionLoad();
    }

    private void requestForDyeingProductionLoad() {
        apiInterface.dyeingProductionLoadResponseCall().enqueue(new Callback<DyeingProductionLoadResponse>() {
            @Override
            public void onResponse(Call<DyeingProductionLoadResponse> call, Response<DyeingProductionLoadResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful()){
                    if(response.body().getResultset().getLoadingUnloading().size() > 0){
                        for(int i=0; i<response.body().getResultset().getLoadingUnloading().size(); i++){
                            loadUnLoadName.add(response.body().getResultset().getLoadingUnloading().get(i).getName());
                            loadUnLoadId.add(response.body().getResultset().getLoadingUnloading().get(i).getId());
                        }
                        setUpLoadingUnLoadingSpinner();
                    }
                    if(response.body().getResultset().getDyeingType().size() > 0){
                        for(int i=0; i<response.body().getResultset().getDyeingType().size(); i++){
                            dyeingName.add(response.body().getResultset().getDyeingType().get(i).getName());
                            dyeingId.add(response.body().getResultset().getDyeingType().get(i).getId());
                        }
                        setUpDyeingTypeSpinner();
                    }
                    if(response.body().getResultset().getProcess().size() > 0){
                        for(int i=0; i<response.body().getResultset().getProcess().size(); i++){
                            processName.add(response.body().getResultset().getProcess().get(i).getName());
                            processId.add(response.body().getResultset().getProcess().get(i).getId());
                        }
                        setUpProcessSpinner();
                    }
                    if(response.body().getResultset().getLtbBtb().size() > 0){
                        for(int i=0; i<response.body().getResultset().getLtbBtb().size(); i++){
                            btbLtbName.add(response.body().getResultset().getLtbBtb().get(i).getName());
                            btbLtbId.add(response.body().getResultset().getLtbBtb().get(i).getId());
                        }
                        setUpLTBLTBSpinner();
                    }
                    if(response.body().getResultset().getMultiBatch().size() > 0){
                        for(int i=0; i<response.body().getResultset().getMultiBatch().size(); i++){
                            multiBatchLoadingName.add(response.body().getResultset().getMultiBatch().get(i).getName());
                            multiBatchLoadingId.add(response.body().getResultset().getMultiBatch().get(i).getId());
                        }
                        setUpMultiBatchSpinner();
                    }
                    if(response.body().getResultset().getCompany().size() > 0){
                        setUpCompanySpinner(response.body().getResultset().getCompany());
                    }
                    if(response.body().getResultset().getResult().size() > 0){
                        setUpResultSpinner(response.body().getResultset().getResult());
                    }
                    if(response.body().getResultset().getFabricType().size() > 0){
                        setUpFabricTypeSpinner(response.body().getResultset().getFabricType());
                    }
                    if(response.body().getResultset().getResponsibility().size() > 0){
                        setUpResponsibilityDeptSpinner(response.body().getResultset().getResponsibility());
                    }
                    if(response.body().getResultset().getShiftName().size() > 0){
                        setUpShiftNameSpinner(response.body().getResultset().getShiftName());
                    }
                }
            }

            @Override
            public void onFailure(Call<DyeingProductionLoadResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: ###### "+"Failed");
            }
        });
    }

    private void requestForCompanyWiseFloor(String companyId) {
        apiInterface.companyWiseFloorResponseCall(companyId).enqueue(new Callback<CompanyWiseFloorResponse>() {
            @Override
            public void onResponse(Call<CompanyWiseFloorResponse> call, Response<CompanyWiseFloorResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful()){
                    setUpCompanyWiseFloorSpinner(response.body().getResultset());
                }
            }

            @Override
            public void onFailure(Call<CompanyWiseFloorResponse> call, Throwable t) {

            }
        });
    }

    private void requestForCompanyFloorMachine(String companyFloorId) {
        apiInterface.compnayFloorWiseMachineResponseCall(companyFloorId).enqueue(new Callback<FloorWiseMachineResponse>() {
            @Override
            public void onResponse(Call<FloorWiseMachineResponse> call, Response<FloorWiseMachineResponse> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful()){
                    setUpCompanyFloorMachineSpinner(response.body().getResultset());
                }
            }

            @Override
            public void onFailure(Call<FloorWiseMachineResponse> call, Throwable t) {

            }
        });
    }

    private void requestForDyeingProductionByBatchScan(String batchSearch) {
        _progressBar.setVisibility(View.VISIBLE);
        apiInterface.deDyeingProdBatchScanResponseCall(LOADING, batchSearch).enqueue(new Callback<DyeingProdBatchScanResponse>() {
            @Override
            public void onResponse(Call<DyeingProdBatchScanResponse> call, Response<DyeingProdBatchScanResponse> response) {
                _progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful()){
                    _batchIdTV.setText(response.body().getResultset().getReferenceIndex().getBatchId());
                    _extNoTV.setText(response.body().getResultset().getReferenceIndex().getExtentionNo());
                    _jobNoTV.setText(response.body().getResultset().getReferenceIndex().getJobNo());
                    _buyerTV.setText(response.body().getResultset().getReferenceIndex().getBuyer());
                    _orderNoTV.setText(response.body().getResultset().getReferenceIndex().getBatchId());
                    _colorIdTV.setText(response.body().getResultset().getReferenceIndex().getColorId());
                    _refNoTV.setText(response.body().getResultset().getReferenceIndex().getRefNo());
                    _fileNoTV.setText(response.body().getResultset().getReferenceIndex().getFileNo());
                    _loadingDateTV.setText(response.body().getResultset().getReferenceIndex().getLoadingDate());
                    _loadingTimeTV.setText(response.body().getResultset().getReferenceIndex().getLoadingTime());

                    if(LOADING.equals("2")){
                        _processStartDate.setText(response.body().getResultset().getReferenceIndex().getLoadingDate());
                        _processStartTime.setText(response.body().getResultset().getReferenceIndex().getLoadingTime());
                    }

                    _companyNameSpinner.setSelection(companyListId.indexOf(response.body().getResultset().getInputAreaIndex().getCompanyId()));
                    _companyNameSpinner.setEnabled(false);

                    if(response.body().getResultset().getInputAreaIndex() != null){
                        BATCH_ID = response.body().getResultset().getInputAreaIndex().getBatchId();
                        BATCH_NO = response.body().getResultset().getInputAreaIndex().getBatchNo();
                        FUNCTIONAL_NO = response.body().getResultset().getInputAreaIndex().getFuntionalBatchNo();
                        EXTENTION_NO = response.body().getResultset().getInputAreaIndex().getExtentionNo();
                        JOB_NO = response.body().getResultset().getReferenceIndex().getJobNo();
                        PO_NO = response.body().getResultset().getReferenceIndex().getPoNo();
                        FILE_NO = response.body().getResultset().getReferenceIndex().getFileNo();
                        REF_NO = response.body().getResultset().getReferenceIndex().getRefNo();
                        BUYER = response.body().getResultset().getReferenceIndex().getBuyer();
                        COLOR_ID = response.body().getResultset().getReferenceIndex().getColorId();
                        BATCH_TYPE = response.body().getResultset().getReferenceIndex().getBatchType();
//                        LOADING = response.body().getResultset().getInputAreaIndex().getLoadUnload();
                        COMPANY = response.body().getResultset().getInputAreaIndex().getCompanyId();
                        SERVICE_COMPANY = response.body().getResultset().getInputAreaIndex().getServiceCompany();
                        PO_NO = response.body().getResultset().getInputAreaIndex().getBatchId();
                        PO_NO = response.body().getResultset().getInputAreaIndex().getBatchId();
                        PO_NO = response.body().getResultset().getInputAreaIndex().getBatchId();
                        PO_NO = response.body().getResultset().getInputAreaIndex().getBatchId();
                        flood_Id = response.body().getResultset().getInputAreaIndex().getFloorId();
                        machine_Id = response.body().getResultset().getInputAreaIndex().getMachineId();

                    }

                    if(response.body().getResultset().getDtlsIndex() != null && response.body().getResultset().getDtlsIndex().size() > 0){
                        dtlsIndexArrayList = response.body().getResultset().getDtlsIndex();
                    }
                    setupDyeingProdScanRecyclerView();
                    dyeingBatchScanRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<DyeingProdBatchScanResponse> call, Throwable t) {
                _progressBar.setVisibility(View.GONE);
                Toast.makeText(DyeingProductionActivity.this, R.string.api_default_error, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    private void requestForDyeingProductionFunctionalByBatchScan(String functionalBatchSearch) {
        _progressBar.setVisibility(View.VISIBLE);
        apiInterface.dyeingProFunctionalBatchResponseCall(LOADING, functionalBatchSearch).enqueue(new Callback<DyeingProFunctionalBatchResponse>() {
            @Override
            public void onResponse(Call<DyeingProFunctionalBatchResponse> call, Response<DyeingProFunctionalBatchResponse> response) {
                _progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful()){
                    functionalBatchIndices.clear();
                    if(response.body().getResultset().getFunctionalBatchIndex().size() > 0){
                        functionalBatchIndices = response.body().getResultset().getFunctionalBatchIndex();
                    }
                    setupDyeingProdFunctionalScanRecyclerView();
                    dyeingFunctionalBatchScanRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<DyeingProFunctionalBatchResponse> call, Throwable t) {
                _progressBar.setVisibility(View.GONE);
                Toast.makeText(DyeingProductionActivity.this, R.string.api_default_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupDyeingProdFunctionalScanRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _dyeingFunctionalScanListRecyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(_responsibilitySpinner.getContext(),
                linearLayoutManager.getOrientation());
        _dyeingFunctionalScanListRecyclerView.addItemDecoration(dividerItemDecoration);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(5);
        _dyeingFunctionalScanListRecyclerView.addItemDecoration(itemDecorator);
        dyeingFunctionalBatchScanRecyclerViewAdapter = new DyeingFunctionalBatchScanRecyclerViewAdapter(this, functionalBatchIndices);
        _dyeingFunctionalScanListRecyclerView.setAdapter(dyeingFunctionalBatchScanRecyclerViewAdapter);
    }

    private void saveRequestDyeingProductionObject() throws JSONException {

        spliteProcessStartTime();

        String processStartTime = _processStartTime.getText().toString();
        String processEndTime = _processEndTime.getText().toString();
        String[] processStartTimeParts = processStartTime.split(":");
        String[] processEndTimeParts = processEndTime.split(":");

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
        index_obj.put("FUNCTIONAL_NO", FUNCTIONAL_NO);
        index_obj.put("EXTENTION_NO", EXTENTION_NO);
        index_obj.put("JOB_NO", JOB_NO);
        index_obj.put("PO_NO", PO_NO);
        index_obj.put("FILE_NO", FILE_NO);
        index_obj.put("REF_NO", REF_NO);
        index_obj.put("BUYER", BUYER);
        index_obj.put("COLOR_ID", COLOR_ID);
        index_obj.put("BATCH_TYPE", BATCH_TYPE);
        index_obj.put("LOADING", LOADING);
        index_obj.put("DYEING_TYPE", DYEING_TYPE);
        index_obj.put("COMPANY", COMPANY);
        index_obj.put("SERVICE_COMPANY", SERVICE_COMPANY);
        index_obj.put("PROCESS_NAME", PROCESS_NAME);
        index_obj.put("BTB_LTB", BTB_LTB);
//        index_obj.put("PROCESS_START_DATE", _processStartDate.getText().toString());
//        index_obj.put("PRODUCTION_DATE", _productionDate.getText().toString());
//        index_obj.put("PROCESS_END_DATE", _processEndDate.getText().toString());
        if(LOADING.equals("1")){
            index_obj.put("END_HOURS", processStartTimeParts[0]);
            index_obj.put("END_MINUTES", processStartTimeParts[1]);
            index_obj.put("PROCESS_END_DATE", _processStartDate.getText().toString());
        }else{
            index_obj.put("END_HOURS", processEndTimeParts[0]);
            index_obj.put("END_MINUTES", processEndTimeParts[1]);
            index_obj.put("PROCESS_END_DATE", _processEndDate.getText().toString());
            index_obj.put("PRODUCTION_DATE", _productionDate.getText().toString());
        }
        index_obj.put("RESULT", RESULT);
        index_obj.put("SHIFT_NAME", SHIFT_NAME);
        index_obj.put("WATER_FLOW", _waterFlowET.getText().toString());
        index_obj.put("FLOOR", FLOOR);
        index_obj.put("MACHINE_NAME", MACHINE_NAME);
        index_obj.put("MACHINE_ID", machine_Id);
        index_obj.put("MULTI_BATCH_LOADING", MULTI_BATCH_LOADING);
        index_obj.put("HOUR_LOAD_METER", _hourlyLoadMeterET.getText().toString().trim());
        index_obj.put("FABRIC_TYPE", FABRIC_TYPE);
        index_obj.put("RESPONSIBILITY_DEPT", RESPONSIBILITY_DEPT);
        index_obj.put("USER_ID", userID);

        data_obj.put("index", index_obj);

        for(int i=0; i<dtlsIndexArrayList.size(); i++){
            JSONObject dtls_obj = new JSONObject();
            dtls_obj.put("PROD_ID", dtlsIndexArrayList.get(i).getProdId());
            dtls_obj.put("CONS_COMPS", dtlsIndexArrayList.get(i).getConsComps().toString());
            dtls_obj.put("GSM", dtlsIndexArrayList.get(i).getGsm());
            dtls_obj.put("DIA_WIDTH", dtlsIndexArrayList.get(i).getDiaWidth());
            dtls_obj.put("FABRIC_TYPEE", dtlsIndexArrayList.get(i).getFabricTypee());
            dtls_obj.put("FABRIC_TYPEE_ID", dtlsIndexArrayList.get(i).getFabricTypeeId());
            dtls_obj.put("ROLL_ID", dtlsIndexArrayList.get(i).getRollId());
            dtls_obj.put("BARCODE_NO", dtlsIndexArrayList.get(i).getBarcodeNo());
            dtls_obj.put("BATCH_QNTY", dtlsIndexArrayList.get(i).getBatchQnty());
            dtls_obj.put("BATCH_ROLLNO", dtlsIndexArrayList.get(i).getBatchRollno());
            dtls_obj.put("PROD_QTY", dtlsIndexArrayList.get(i).getProdQty());
            dtls_obj.put("PROD_QTY_READONLY", dtlsIndexArrayList.get(i).getProdQtyReadonly());
            dtls_arr.put(dtls_obj);
        }

        data_obj.put("list_data",dtls_arr);
        save_obj.put("data", data_obj);

        Log.d(TAG, "saveRequestDyeingProductionObject: ########"+ save_obj);

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, save_obj.toString());
        _progressBar.setVisibility(View.VISIBLE);
        apiInterface.saveUpdateDyeingProductionCall(body).enqueue(new Callback<DyeingProdSaveResponse>() {
            @Override
            public void onResponse(Call<DyeingProdSaveResponse> call, Response<DyeingProdSaveResponse> response) {
                Log.d(TAG, "onResponse: ######### "+response.toString());
                _progressBar.setVisibility(View.GONE);
//                if(response.isSuccessful()){
//                    refreshDataFromUI();
//                    DialogHelper.showWarningDialog(DyeingProductionActivity.this, "Message", response.body().getResultset().getSaveMsg());
//                    try{
//                        if(response.body().getResultset().getFunctionalBatch() != null && !response.body().getResultset().getFunctionalBatch().equals("") ){
//                            requestForDyeingProductionFunctionalByBatchScan(response.body().getResultset().getFunctionalBatch());
//                        }
//                    }catch (Exception e){
//                        DialogHelper.showWarningDialog(DyeingProductionActivity.this, "Message", response.body().getResultset().getSaveMsg());
//                    }
//
//                }else {
//                    DialogHelper.showWarningDialog(DyeingProductionActivity.this, "Message", response.message());
//                }
                if (response.isSuccessful()) {
                    refreshDataFromUI();
                    String saveMsg = response.body().getResultset().getSaveMsg();

                    try {
                        String functionalBatch = response.body().getResultset().getFunctionalBatch();
                        if (functionalBatch != null && !functionalBatch.isEmpty()) {
                            DialogHelper.showSuccessDialog(DyeingProductionActivity.this, "Success", saveMsg);
                            requestForDyeingProductionFunctionalByBatchScan(functionalBatch);
                        } else {
                            DialogHelper.showWarningDialog(DyeingProductionActivity.this, "Warning", saveMsg);
                        }
                    } catch (Exception e) {
                        DialogHelper.showWarningDialog(DyeingProductionActivity.this, "Warning", saveMsg);
                    }
                } else {
                    DialogHelper.showWarningDialog(DyeingProductionActivity.this, "Warning", response.message());
                }
            }

            @Override
            public void onFailure(Call<DyeingProdSaveResponse> call, Throwable t) {
                _progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: ##### "+t.getMessage());
                DialogHelper.showErrorDialog(DyeingProductionActivity.this, "Error", "Data not saved.");
            }
        });
    }

    private void spliteProcessStartTime() {
        String processStartTime = _processStartTime.getText().toString().trim();
        String[] arrOfStr = processStartTime.split(":", 2);
        for (String a : arrOfStr)
            processStartTimeList.add(a);
    }

    private void setupDyeingProdScanRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _dyeingScanListRecyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(_responsibilitySpinner.getContext(),
                linearLayoutManager.getOrientation());
        _dyeingScanListRecyclerView.addItemDecoration(dividerItemDecoration);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(5);
        _dyeingScanListRecyclerView.addItemDecoration(itemDecorator);
        dyeingBatchScanRecyclerViewAdapter = new DyeingBatchScanRecyclerViewAdapter(this, dtlsIndexArrayList);
        _dyeingScanListRecyclerView.setAdapter(dyeingBatchScanRecyclerViewAdapter);
    }

    private void setUpCompanyFloorMachineSpinner(List<FloorWiseMachineResponse.Resultset> machineSet) {
        machineName.clear();
        machineId.clear();
        for(int i=0; i<machineSet.size(); i++){
            machineName.add(machineSet.get(i).getName());
            machineId.add(machineSet.get(i).getId());
        }

        machineName.add(0, "-Select-");
        machineId.add(0, "0");
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, machineName);
        _machineNameSpinner.setAdapter(spinnerArrayAdapter);

        try {
            _machineNameSpinner.setSelection(machineId.indexOf(machine_Id));
        } catch (Exception e) {
            Log.d(TAG, "setUpCompanyFloorMachineSpinner: ");
        }

        _machineNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                machine_Id = machineId.get(position);
                MACHINE_NAME = machineName.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpCompanyWiseFloorSpinner(List<CompanyWiseFloorResponse.Resultset> floorSet) {
        floorName.clear();
        floorId.clear();
        for(int i=0; i<floorSet.size(); i++){
            floorName.add(floorSet.get(i).getName());
            floorId.add(floorSet.get(i).getId());
        }
        floorName.add(0, "-Select-");
        floorId.add(0, "0");
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, floorName);
        _floorNameSpinner.setAdapter(spinnerArrayAdapter);

        try {
            _floorNameSpinner.setSelection(floorId.indexOf(flood_Id));
        } catch (Exception e) {
            Log.d(TAG, "setUpCompanyFloorMachineSpinner: ");
        }

        _floorNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FLOOR = floorId.get(position);
                requestForCompanyFloorMachine(floorId.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpShiftNameSpinner(List<DyeingProductionLoadResponse.ShiftName> shiftName) {
        for(int i=0; i<shiftName.size(); i++){
            shiftListName.add(shiftName.get(i).getName());
            shiftId.add(shiftName.get(i).getId());
        }
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, shiftListName);
        _shiftNameSpinner.setAdapter(spinnerArrayAdapter);

        _shiftNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SHIFT_NAME = shiftId.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpResponsibilityDeptSpinner(List<DyeingProductionLoadResponse.Responsibility> responsibility) {
        for(int i=0; i<responsibility.size(); i++){
            resDepName.add(responsibility.get(i).getName());
            resDepId.add(responsibility.get(i).getId());
        }
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, resDepName);
        _responsibilitySpinner.setAdapter(spinnerArrayAdapter);

        _responsibilitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RESPONSIBILITY_DEPT = resDepId.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpFabricTypeSpinner(List<DyeingProductionLoadResponse.FabricType> fabricType) {
        for(int i=0; i<fabricType.size(); i++){
            fabricTypeName.add(fabricType.get(i).getName());
            fabricTypeId.add(fabricType.get(i).getId());
        }

        fabricTypeName.add(0, "--Select--");
        fabricTypeId.add(0, "0");

        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, fabricTypeName);
        _fabricSpinner.setAdapter(spinnerArrayAdapter);

        _fabricSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FABRIC_TYPE = fabricTypeId.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpResultSpinner(List<DyeingProductionLoadResponse.Result> result) {
        for(int i=0; i<result.size(); i++){
            resultName.add(result.get(i).getName());
            resultId.add(result.get(i).getId());
        }
        resultName.add(0, "--Select--");
        resultId.add(0, "0");
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, resultName);
        _resultSpinner.setAdapter(spinnerArrayAdapter);

        _resultSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RESULT = resultId.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpCompanySpinner(List<DyeingProductionLoadResponse.Company> company) {
        for(int i=0; i<company.size(); i++){
            companyName.add(company.get(i).getCompany());
            companyListId.add(company.get(i).getId());
        }
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, companyName);
        _companyNameSpinner.setAdapter(spinnerArrayAdapter);

        _companyNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                requestForCompanyWiseFloor(companyListId.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpLoadingUnLoadingSpinner() {
        _loadingBtn.setText(loadUnLoadName.get(0));
        _unloadingBtn.setText(loadUnLoadName.get(1));
//        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, loadUnLoadName);
//        _ldUnLdSpinner.setAdapter(spinnerArrayAdapter);
//
//        _ldUnLdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }

    private void setUpDyeingTypeSpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dyeingName);
        _dyeingTypeSpinner.setAdapter(spinnerArrayAdapter);

        _dyeingTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DYEING_TYPE = dyeingId.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpProcessSpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, processName);
        _processNameSpinner.setAdapter(spinnerArrayAdapter);

        _processNameSpinner.setSelection(3);

        _processNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PROCESS_NAME = processId.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpLTBLTBSpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, btbLtbName);
        _btb_ltbSpinner.setAdapter(spinnerArrayAdapter);

        _btb_ltbSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BTB_LTB = btbLtbId.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpMultiBatchSpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, multiBatchLoadingName);
        _multiBatchLoadingSpinner.setAdapter(spinnerArrayAdapter);

        _multiBatchLoadingSpinner.setSelection(1);

        _multiBatchLoadingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MULTI_BATCH_LOADING = multiBatchLoadingId.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    private void setUp() {
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        mToolbar.setTitle(R.string.dyeing_production);
        mToolbar.setOnClickListener(this);

        _dyeingTypeSpinner = findViewById(R.id.dyeingTypeSpinner);
        _processNameSpinner = findViewById(R.id.processNameSpinner);
        _btb_ltbSpinner = findViewById(R.id.btb_ltbSpinner);
        _multiBatchLoadingSpinner = findViewById(R.id.multiBatchLoadingSpinner);
        _companyNameSpinner = findViewById(R.id.companyNameSpinner);
        _floorNameSpinner = findViewById(R.id.floorNameSpinner);
        _machineNameSpinner = findViewById(R.id.machineNameSpinner);
        _resultSpinner = findViewById(R.id.resultSpinner);
        _fabricSpinner = findViewById(R.id.fabricSpinner);
        _shiftNameSpinner = findViewById(R.id.shiftNameSpinner);
        _responsibilitySpinner = findViewById(R.id.responsibilitySpinner);

        _processStartDate = findViewById(R.id.processStartDate);
        _processStartDate.setOnClickListener(this);
        _processStartTime = findViewById(R.id.processStartTime);
        _processStartTime.setOnClickListener(this);
        _processEndTime = findViewById(R.id.processEndTime);
        _processEndTime.setOnClickListener(this);
        _processEndDate = findViewById(R.id.processEndDate);
        _processEndDate.setOnClickListener(this);
        _productionDate = findViewById(R.id.productionDate);
        _productionDate.setOnClickListener(this);
        _saveButton = findViewById(R.id.saveButton);
        _saveButton.setOnClickListener(this);
        _refreshButton = findViewById(R.id.refreshButton);
        _refreshButton.setOnClickListener(this);

        _batchScan = findViewById(R.id.batchScan);
        _batchScan.setOnClickListener(this);

        _batchSearch = findViewById(R.id.batchSearch);
        _batchSearch.setOnClickListener(this);

        segmented2 = findViewById(R.id.segmented2);
        segmented2.setTintColor(R.color.colorPrimaryDark);

        _progressBar = findViewById(R.id.progressBar);

        _loadingBtn = findViewById(R.id.loadingBtn);
        _loadingBtn.setOnClickListener(this);

        _unloadingBtn = findViewById(R.id.unloadingBtn);
        _unloadingBtn.setOnClickListener(this);

        _batchFunctionalScannButton = findViewById(R.id.batchFunctionalScannButton);
        _batchFunctionalScannButton.setOnClickListener(this);

        _dyeingScanListRecyclerView = findViewById(R.id.dyeingScanListRecyclerView);
        _dyeingFunctionalScanListRecyclerView = findViewById(R.id.dyeingFunctionalScanListRecyclerView);

        _batchIdTV = findViewById(R.id.batchIdTV);
        _extNoTV = findViewById(R.id.extNoTV);
        _jobNoTV = findViewById(R.id.jobNoTV);
        _buyerTV = findViewById(R.id.buyerTV);
        _orderNoTV = findViewById(R.id.orderNoTV);
        _colorIdTV = findViewById(R.id.colorIdTV);
        _refNoTV = findViewById(R.id.refNoTV);
        _fileNoTV = findViewById(R.id.fileNoTV);
        _loadingDateTV = findViewById(R.id.loadingDateTV);
        _loadingTimeTV = findViewById(R.id.loadingTimeTV);
        _processStartDateTVCaption = findViewById(R.id.processStartDateTVCaption);

        _waterFlowET = findViewById(R.id.waterFlowET);
        _hourlyLoadMeterET = findViewById(R.id.hourlyLoadMeterET);
        _batchNoScanET = findViewById(R.id.batchNoScanET);
        _functionalBatchET = findViewById(R.id.functionalBatchET);

        _uploadingOp0 = findViewById(R.id.uploadingOp0);
        _uploadingOp1 = findViewById(R.id.uploadingOp1);
        _uploadingOp2 = findViewById(R.id.uploadingOp2);
        _uploadingOp3 = findViewById(R.id.uploadingOp3);
        _loadingTimely = findViewById(R.id.loadingTimely);

        setDataUI();
    }

    private void setDataUI() {
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        simpleTimeFormat = new SimpleDateFormat("HH:mm");
        String currentDate = simpleDateFormat.format(calendar.getTime());
        String currentTime = simpleTimeFormat.format(calendar.getTime());
        _processStartDate.setText(currentDate);
        _processStartTime.setText(currentTime);
        _processEndTime.setText(currentTime);
        _productionDate.setText(currentDate);
        _processEndDate.setText(currentDate);

        mProcess = (Process) getIntent().getSerializableExtra(EXTRA_BUNDLE_ID);

        if(mProcess != null){
            mType = mProcess.getDataParam().getPageParam()+"_"+mProcess.getDataParam().getTypeParam();
            Log.d(TAG, "onCreate: ###"+mType);
            Log.d(TAG, "onCreate: ###"+mProcess.getTitle());

            scannedBatch = mProcess.getDataParam().getTypeParam();
            LOADING = mProcess.getTitle();

            if(LOADING.equals("2")){
                _unloadingBtn.setChecked(true);
                _uploadingOp0.setVisibility(View.VISIBLE);
                _uploadingOp1.setVisibility(View.VISIBLE);
                _uploadingOp2.setVisibility(View.VISIBLE);
                _uploadingOp3.setVisibility(View.VISIBLE);
                _loadingTimely.setVisibility(View.VISIBLE);
                _floorNameSpinner.setEnabled(false);
                _machineNameSpinner.setEnabled(false);
                _processStartDate.setClickable(false);
                _processStartTime.setClickable(false);
//                _processStartDateTVCaption.setText("Process End Time");
            }else if(LOADING.equals("1")) {
                _loadingBtn.setChecked(true);
                _loadingTimely.setVisibility(View.GONE);
                _processStartDate.setClickable(true);
                _processStartTime.setClickable(true);
            }

            _batchNoScanET.setText(scannedBatch);

            if(!scannedBatch.equals("")){
                requestForDyeingProductionByBatchScan(scannedBatch);
            }
        }



    }
    private void refreshDataFromUI() {
        functionalBatchIndices.clear();
        dtlsIndexArrayList.clear();
        loadUnLoadName.clear();
        loadUnLoadId.clear();
        dyeingName.clear();
        dyeingId.clear();
        processName.clear();
        processId.clear();
        btbLtbName.clear();
        btbLtbId.clear();
        multiBatchLoadingName.clear();
        multiBatchLoadingId.clear();
        companyName.clear();
        companyListId.clear();
        floorName.clear();
        floorId.clear();
        machineName.clear();
        machineId.clear();
        resultName.clear();
        resultId.clear();
        fabricTypeName.clear();
        fabricTypeId.clear();
        resDepName.clear();
        resDepId.clear();
        shiftListName.clear();
        shiftId.clear();
        setupDyeingProdFunctionalScanRecyclerView();
        dyeingFunctionalBatchScanRecyclerViewAdapter.notifyDataSetChanged();
        setupDyeingProdScanRecyclerView();
        dyeingBatchScanRecyclerViewAdapter.notifyDataSetChanged();
        requestForDyeingProductionLoad();
        _batchNoScanET.setText("");
        _functionalBatchET.setText("");
        _loadingBtn.setChecked(false);
        _unloadingBtn.setChecked(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar:
                finish();
                break;
            case R.id.processStartDate:
                datepicker(_processStartDate);
                break;
            case R.id.processEndDate:
                datepicker(_processEndDate);
                break;
            case R.id.processStartTime:
                timePicker(_processStartTime);
                break;
            case R.id.processEndTime:
                timePicker(_processEndTime);
                break;
            case R.id.productionDate:
                datepicker(_productionDate);
                break;
            case R.id.batchScan:
                if(LOADING != null){
                    startActivity( ScannerActivity.getStartIntent(DyeingProductionActivity.this, new Process(R.drawable.process, LOADING, "",
                            new Process.DataParam("result", "dyeing"))));
                    finish();
                }else {
                    Toast.makeText(this, "Select Load/Un-Load option.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.batchSearch:
                String batch = _batchNoScanET.getText().toString();
                if(LOADING != null){
                    if(!batch.equals("")){
                        requestForDyeingProductionByBatchScan(batch);
                    }else {
                        _batchNoScanET.setError(getString(R.string.batch_error));
                    }
                }else {
                    Toast.makeText(this, "Select Load/Un-Load option.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.batchFunctionalScannButton:
                String functionalBatch = _functionalBatchET.getText().toString();
                if(!functionalBatch.equals("")){
                    if(LOADING != null){
                        requestForDyeingProductionFunctionalByBatchScan(functionalBatch);
                    }else {
                        Toast.makeText(this, "Select Load/Un-Load option.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    _functionalBatchET.setError(getString(R.string.functional_batch_error));
                }
                break;
            case R.id.saveButton:
                try {
                    if(dtlsIndexArrayList.size() > 0){
                        String _waterFlow = _waterFlowET.getText().toString();
                        if(LOADING.equals("1")){
                            if(!_waterFlow.isEmpty() && !FLOOR.equals("0") && !machine_Id.equals("0")){
                                saveRequestDyeingProductionObject();
                            }else{
                                DialogHelper.showWarningDialog(DyeingProductionActivity.this, "Message", "Please enter and Select Water Flow & floor & machine");
                            }

                        }else if(LOADING.equals("2") && !FLOOR.equals("0") && !machine_Id.equals("0")){
                            Log.d(TAG, "onClick: "+RESULT+" : "+FABRIC_TYPE);

                            boolean dateComparison = compareLoadingAndProductionDate(_processStartDate.getText().toString(), _processStartTime.getText().toString(), _processEndDate.getText().toString(), _processEndTime.getText().toString());

                            if(dateComparison){
                                if(!RESULT.equals("0") && !FABRIC_TYPE.equals("0") && !_waterFlow.isEmpty() && !FLOOR.equals("0") && !machine_Id.equals("0")){
                                    saveRequestDyeingProductionObject();
                                }else{
                                    DialogHelper.showWarningDialog(DyeingProductionActivity.this, "Message", "Please enter and Select Water Flow & floor & machine result & fabric type.");
                                }
                            }else{
                                DialogHelper.showWarningDialog(DyeingProductionActivity.this, "Message", "Loading date and time (" + _processStartDate.getText().toString() +" "+ _processStartTime.getText().toString() + ") is after process end date and time (" + _processEndDate.getText().toString() +" "+_processEndTime.getText().toString() + ").");
                            }

                        }
//                        saveRequestDyeingProductionObject();
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
            case R.id.loadingBtn:
                LOADING = loadUnLoadId.get(0);
                _uploadingOp0.setVisibility(View.GONE);
                _uploadingOp1.setVisibility(View.GONE);
                _uploadingOp2.setVisibility(View.GONE);
                _uploadingOp3.setVisibility(View.GONE);
                _loadingTimely.setVisibility(View.GONE);
                _floorNameSpinner.setEnabled(true);
                _machineNameSpinner.setEnabled(true);
                _processStartDate.setClickable(true);
                _processStartTime.setClickable(true);
                _processStartDateTVCaption.setText("Process Start Time");
                break;
            case R.id.unloadingBtn:
                LOADING = loadUnLoadId.get(1);
                _uploadingOp0.setVisibility(View.VISIBLE);
                _uploadingOp1.setVisibility(View.VISIBLE);
                _uploadingOp2.setVisibility(View.VISIBLE);
                _uploadingOp3.setVisibility(View.VISIBLE);
                _loadingTimely.setVisibility(View.VISIBLE);
                _floorNameSpinner.setEnabled(false);
                _machineNameSpinner.setEnabled(false);
                _processStartDate.setClickable(false);
                _processStartTime.setClickable(false);

//                _processStartDateTVCaption.setText("Process End Time");
                break;
        }
    }

    private boolean compareLoadingAndProductionDate(String loadingDate, String loadingTime, String processEndDate, String processEndTime) {

        String loading = loadingDate+" "+loadingTime;
        String process = processEndDate+" "+processEndTime;
        Log.d(TAG, "compareLoadingAndProductionDate: ------------> "+loading +" "+process);
        try {
            @SuppressLint("SimpleDateFormat") Date date1 = new SimpleDateFormat("dd-MMM-yy HH:mm").parse(loading);
            @SuppressLint("SimpleDateFormat") Date date2 = new SimpleDateFormat("dd-MM-yy HH:mm").parse(process);

            if (date1.before(date2)) {
                return true;
            } else if (date1.after(date2)) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Invalid date format");
        }

        return false;

    }
}