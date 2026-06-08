package com.logicsoftbd.lsl.ui.v_1_ui.finish_fabric_roll_receive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.datatransport.runtime.firebase.transport.LogEventDropped;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagKeepingResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagReturnResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseCompanyToLocationClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseFloorWiseLineClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseLocationWiseFloorClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseSewingInputClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FinishLocationWiseFloorClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GMTFinishReceiveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_transfer_out.V1GreyFabricTransferOutStoreList;
import com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag.V1_BagKeepingActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag.V1_BagReturnRecyclerViewAdapter;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_ScannerActivity;
import com.logicsoftbd.lsl.utils.DialogHelper;
import com.logicsoftbd.lsl.viewModel.FinishProductionViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class V1_GmtFinishReceiveActivity extends AppCompatActivity implements View.OnClickListener, V1_GmtFinishReceiveRecyclerViewAdapter.OnRemoveHeadListener {
    private static final String TAG = "V1_GmtFinishReceiveActi";
    private ProgressBar _progressBar;
    private Spinner _finishCompanySpinner, _poCompanySpinner, _fnLocationSpinner, _fnFloorSpinner, _fnLineSpinnner;
    private RecyclerView _finishReceiveRecyclerView;
    private Button _btBarcodeScan, _saveBtn, _refreshBtn;
    private ImageView _back;
    private TextView _btBarcodeScanTV, _currentDateTV, _totalBundleQNtyTV, _totalFinRCVQntyTV;
    private SimpleDateFormat simpleDateFormat;
    private Integer finishCompanyId = 0, fnCompanyId = 0, poCompanyId = 0, fnCompanyWiseLocationId = 0, fnLocationWiseFloorId = 0, fnFloorWiseLineId = 0, finishCompanyWiseLocationId = 0, finishLOcationWiseFloorId = 0, finishFloorWiseLineId = 0;
    private String base_url, userID, userName, currentDate, scannedBarcode;
    private FinishProductionViewModel finishProductionViewModel;
    private ArrayList<V1_BundleWiseSewingInputClass.Company> finishCompanyList;
    private ArrayList<V1_BundleWiseSewingInputClass.Company> poCompanyList;
    private ArrayList<V1_BundleWiseCompanyToLocationClass.Resultset> fnCompanyWiseLocationList;
    private ArrayList<V1_FinishLocationWiseFloorClass.MasterPart> fnLocationWiseFloorList;
    private ArrayList<V1_BundleWiseFloorWiseLineClass.Resultset> fnFloorWiseLineList;
    private ArrayList<V1_GMTFinishReceiveResponse.Data> gmtFinishReceiveBarcodeList = new ArrayList<>();
    private V1_GmtFinishReceiveRecyclerViewAdapter gtmFinishRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_gmt_finish_receive);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        base_url = (_preferences.getString("base_url", ""));
        userID = _preferences.getString("login_userid", "");
        userName = _preferences.getString("login_username", "");
        finishProductionViewModel = new ViewModelProvider(this).get(FinishProductionViewModel.class);

        init_ui();
        getDefaultData();
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _finishReceiveRecyclerView.setLayoutManager(linearLayoutManager);
        gtmFinishRecyclerViewAdapter = new V1_GmtFinishReceiveRecyclerViewAdapter( gmtFinishReceiveBarcodeList,
        this, this);
        _finishReceiveRecyclerView.setAdapter(gtmFinishRecyclerViewAdapter);
    }

    private void getDefaultData() {

        Intent intent = getIntent();
        scannedBarcode = intent.getStringExtra("barcodeScan");
        finishCompanyId = intent.getIntExtra("finishCompanyId", 0);
        fnCompanyId = intent.getIntExtra("fnCompanyId", 0);
        fnCompanyWiseLocationId = intent.getIntExtra("fnLocationId", 0);
        fnLocationWiseFloorId = intent.getIntExtra("fnFloorId", 0);
        fnFloorWiseLineId = intent.getIntExtra("fnLineId", 0);

        gmtFinishReceiveBarcodeList = (ArrayList<V1_GMTFinishReceiveResponse.Data>) intent.getSerializableExtra("gmtFinishReceiveList");
        if(gmtFinishReceiveBarcodeList != null){
            setupRecyclerView();
        }

        if(scannedBarcode != null) {
            fetchGMTFinishReceiveBarcodeData(scannedBarcode);
        }

        if(finishCompanyId != 0 && finishCompanyList != null)
        {
            setupFinishCompanySpinner();
        }

        if(fnCompanyId != 0 && poCompanyList != null)
        {
            setupPOCompanySpinner();
        }

        if(fnCompanyWiseLocationId != 0 && fnCompanyWiseLocationList != null)
        {
            setupFinishCompanyWiseLocationSpinner();
        }

        if(fnLocationWiseFloorId != 0 && fnLocationWiseFloorList != null)
        {
            setupFinishLocationWiseFloorSpinner();
        }

        if(fnFloorWiseLineId != 0 && fnFloorWiseLineList != null)
        {
            setupFinishFloorWiseLineSpinner();
        }
    }

    private void  fetchGMTFinishReceiveBarcodeData(String barcode) {
        progressBarState();
        finishProductionViewModel.getGMTFinishReciveBarcodeResponse(barcode).observe(this, apiResponse -> {
            if(apiResponse!= null && apiResponse.getData() != null){
                if( gmtFinishReceiveBarcodeList == null) {
                    gmtFinishReceiveBarcodeList = new ArrayList<>();
                }
                boolean bagExists = false;
                for (V1_GMTFinishReceiveResponse.Data item : gmtFinishReceiveBarcodeList) {
                    if (item.getBarcodeNo().equals(apiResponse.getData().getBarcodeNo()) ||
                            (!item.getSweingCompanyId().equals(String.valueOf(fnCompanyId)) && !item.getSweingLocation().equals(String.valueOf(fnCompanyWiseLocationId)))) {
                        DialogHelper.showWarningDialog(V1_GmtFinishReceiveActivity.this, "Warning", "Barcode already exists or don't match with PO company and location.");
                        bagExists = true;
                        break;
                    }
                }
                if (!bagExists) {
                    gmtFinishReceiveBarcodeList.add(apiResponse.getData());
//                    fnCompanyId = Integer.valueOf(apiResponse.getData().getSweingCompanyId());
                    poCompanyId = Integer.valueOf(apiResponse.getData().getPoCompanyId());
//                    finishCompanyId = Integer.valueOf(apiResponse.getData().getSweingCompanyId());
//                    fnCompanyWiseLocationId = Integer.valueOf(apiResponse.getData().getSweingLocation());
                    setupPOCompanySpinner();
//                    setupFinishCompanySpinner();
//                    fetchFNCompanyWiseLocation(fnCompanyId);
                }
                setupRecyclerView();
                calculateTotalQnty();
                gtmFinishRecyclerViewAdapter.notifyDataSetChanged();
            }else{
                try {
                    DialogHelper.showErrorDialog(this, "Error", apiResponse.getMsg());
                    calculateTotalQnty();
                } catch (Exception e) {
                    Log.d(TAG, "fetchGMTFinishReceiveBarcodeData: ");
                    DialogHelper.showErrorDialog(this, "Error", "Something went wrong.");
                }

            }
        });
    }

    private void calculateTotalQnty() {
        try {
            if(gmtFinishReceiveBarcodeList.size() > 0) {
                int totalBundleQnty = 0;
                int totalFinRcvQnty = 0;
                for (V1_GMTFinishReceiveResponse.Data item: gmtFinishReceiveBarcodeList){
                    totalBundleQnty += Integer.parseInt(item.getBundleQnty());
                    totalFinRcvQnty += Integer.parseInt(item.getProductionQnty());
                }

                _totalBundleQNtyTV.setText(String.valueOf(totalBundleQnty));
                _totalFinRCVQntyTV.setText(String.valueOf(totalFinRcvQnty));
            }
        }catch (Exception e){
            Log.d(TAG, "calculateTotalQnty: ");
        }
    }

    private void init_ui() {
        _progressBar = findViewById(R.id.progressBar);
        _finishCompanySpinner = findViewById(R.id.finishCompanySpinner);
//        _finishCompanySpinner.setEnabled(false);
        _poCompanySpinner = findViewById(R.id.poCompanySpinner);
        _poCompanySpinner.setEnabled(false);
        _fnLocationSpinner = findViewById(R.id.fnLocationSpinner);
//        _fnLocationSpinner.setEnabled(false);
        _fnFloorSpinner = findViewById(R.id.fnFloorSpinner);
        _fnLineSpinnner = findViewById(R.id.fnLineSpinnner);
        _finishReceiveRecyclerView = findViewById(R.id.finishReceiveRecyclerView);
        _btBarcodeScanTV = findViewById(R.id.btBarcodeScanTV);
        _currentDateTV = findViewById(R.id.currentDateTV);
        _totalFinRCVQntyTV = findViewById(R.id.totalFinRCVQntyTV);
        _totalBundleQNtyTV = findViewById(R.id.totalBundleQNtyTV);

        _refreshBtn = findViewById(R.id.refreshBtn);
        _refreshBtn.setOnClickListener(this);
        _saveBtn = findViewById(R.id.saveBtn);
        _saveBtn.setOnClickListener(this);
        _back = findViewById(R.id.back);
        _back.setOnClickListener(this);

        _btBarcodeScan = findViewById(R.id.btBarcodeScan);
        _btBarcodeScan.setOnClickListener(this);

        simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = new Date(System.currentTimeMillis());
        currentDate = simpleDateFormat.format(date);
        _currentDateTV.setText(currentDate);

        fetchCompanyData();
    }

    private void fetchCompanyData() {
        progressBarState();
        finishProductionViewModel.getCompanyAndSourceResponse().observe(this, apiResponse -> {
            if(apiResponse!= null){
                finishCompanyList = (ArrayList<V1_BundleWiseSewingInputClass.Company>) apiResponse.getResultset().getCompany();
                poCompanyList = (ArrayList<V1_BundleWiseSewingInputClass.Company>) apiResponse.getResultset().getCompany();
                setupFinishCompanySpinner();
                setupPOCompanySpinner();
            }else{
                DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
            }
        });
    }

    private void setupFinishCompanySpinner() {
        List<String> _companyNames = new ArrayList<>();
        List<String> _companyIds = new ArrayList<>();
        for (V1_BundleWiseSewingInputClass.Company company : finishCompanyList) {
            _companyNames.add(company.getCompany());
            _companyIds.add(String.valueOf(company.getId()));
        }
        _companyNames.add(0, "--Select--");
        _companyIds.add(0, "0");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, _companyNames);

        _finishCompanySpinner.setAdapter(adapter);
        _finishCompanySpinner.setSelection(_companyIds.indexOf(String.valueOf(fnCompanyId)));

        _finishCompanySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                finishCompanyId = Integer.parseInt(String.valueOf(_companyIds.get(position)));
                fnCompanyId = Integer.parseInt(String.valueOf(_companyIds.get(position)));
                if(fnCompanyId != 0){
                    fetchFNCompanyWiseLocation(fnCompanyId);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupPOCompanySpinner() {
        List<String> _companyNames = new ArrayList<>();
        List<String> _companyIds = new ArrayList<>();
        for (V1_BundleWiseSewingInputClass.Company company : poCompanyList) {
            _companyNames.add(company.getCompany());
            _companyIds.add(String.valueOf(company.getId()));
        }
        _companyNames.add(0, "--Select--");
        _companyIds.add(0, "0");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, _companyNames);

        _poCompanySpinner.setAdapter(adapter);
        if(gmtFinishReceiveBarcodeList != null && !gmtFinishReceiveBarcodeList.isEmpty()) {
            _poCompanySpinner.setSelection(_companyIds.indexOf(String.valueOf(gmtFinishReceiveBarcodeList.get(0).getPoCompanyId())));
        }else{
            _poCompanySpinner.setSelection(_companyIds.indexOf(String.valueOf(poCompanyId)));
        }

        _poCompanySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                fnCompanyId = Integer.parseInt(String.valueOf(_companyIds.get(position)));
//                if(fnCompanyId != 0){
//                    fetchFNCompanyWiseLocation(fnCompanyId);
//                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fetchFNCompanyWiseLocation(Integer poCompanyId) {
        progressBarState();
        finishProductionViewModel.getCompanyWiseLocationResponse(poCompanyId).observe(this, apiResponse -> {
            if(apiResponse!= null){
                fnCompanyWiseLocationList = (ArrayList<V1_BundleWiseCompanyToLocationClass.Resultset>) apiResponse.getResultset();
                setupFinishCompanyWiseLocationSpinner();
            }else{
                DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
            }
        });
    }

    private void setupFinishCompanyWiseLocationSpinner() {
        List<String> _locationNames = new ArrayList<>();
        List<String> _locationIds = new ArrayList<>();
        for (V1_BundleWiseCompanyToLocationClass.Resultset location : fnCompanyWiseLocationList) {
            _locationNames.add(location.getName());
            _locationIds.add(String.valueOf(location.getId()));
        }
        _locationNames.add(0, "--Select--");
        _locationIds.add(0, "0");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, _locationNames);

        _fnLocationSpinner.setAdapter(adapter);
        _fnLocationSpinner.setSelection(_locationIds.indexOf(String.valueOf(fnCompanyWiseLocationId)));

        _fnLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fnCompanyWiseLocationId = Integer.parseInt(String.valueOf(_locationIds.get(position)));
                if(fnCompanyWiseLocationId != 0){
                    fetchPOLocationWiseFloor(fnCompanyWiseLocationId);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fetchPOLocationWiseFloor(Integer poLocationId) {
        progressBarState();
        finishProductionViewModel.getFinishLocationWiseFloorResponse(poLocationId, 11).observe(this, apiResponse -> {
            if(apiResponse!= null){
                fnLocationWiseFloorList = (ArrayList<V1_FinishLocationWiseFloorClass.MasterPart>) apiResponse.getResultset().getMasterPart();
                setupFinishLocationWiseFloorSpinner();
            }else{
                DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
            }
        });
    }

    private void setupFinishLocationWiseFloorSpinner() {
        List<String> _floorNames = new ArrayList<>();
        List<String> _floorIds = new ArrayList<>();
        for (V1_FinishLocationWiseFloorClass.MasterPart location : fnLocationWiseFloorList) {
            _floorNames.add(location.getFloorName());
            _floorIds.add(String.valueOf(location.getId()));
        }
        _floorNames.add(0, "--Select--");
        _floorIds.add(0, "0");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, _floorNames);

        _fnFloorSpinner.setAdapter(adapter);
        _fnFloorSpinner.setSelection(_floorIds.indexOf(String.valueOf(fnLocationWiseFloorId)));

        _fnFloorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fnLocationWiseFloorId = Integer.parseInt(String.valueOf(_floorIds.get(position)));
                if(fnLocationWiseFloorId != 0){
                    fetchPOFloorWiseLine(fnLocationWiseFloorId);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fetchPOFloorWiseLine(Integer floorId) {
        progressBarState();
        finishProductionViewModel.getFloorWiseLineResponse(fnCompanyId, fnCompanyWiseLocationId, floorId, currentDate).observe(this, apiResponse -> {
            if(apiResponse!= null){
                fnFloorWiseLineList = (ArrayList<V1_BundleWiseFloorWiseLineClass.Resultset>) apiResponse.getResultset();
                setupFinishFloorWiseLineSpinner();
            }else{
                DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
            }
        });
    }

    private void setupFinishFloorWiseLineSpinner() {
        List<String> _lineNames = new ArrayList<>();
        List<String> _lineIds = new ArrayList<>();
        for (V1_BundleWiseFloorWiseLineClass.Resultset location : fnFloorWiseLineList) {
            _lineNames.add(location.getName());
            _lineIds.add(String.valueOf(location.getId()));
        }
        _lineNames.add(0, "--Select--");
        _lineIds.add(0, "0");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, _lineNames);

        _fnLineSpinnner.setAdapter(adapter);
        _fnLineSpinnner.setSelection(_lineIds.indexOf(String.valueOf(fnFloorWiseLineId)));

        _fnLineSpinnner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fnFloorWiseLineId = Integer.parseInt(String.valueOf(_lineIds.get(position)));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void startScanning(int op) {
        Intent intent = new Intent(this, V1_ScannerActivity.class);
        intent.putExtra("qc", "gmt_finish_receive");
        intent.putExtra("scan_op", op);
        intent.putExtra("finishCompanyId", finishCompanyId);
        intent.putExtra("fnCompanyId", fnCompanyId);
        intent.putExtra("fnLocationId", fnCompanyWiseLocationId);
        intent.putExtra("fnFloorId", fnLocationWiseFloorId);
        intent.putExtra("fnLineId", fnFloorWiseLineId);
        intent.putExtra("gmtFinishReceiveList", gmtFinishReceiveBarcodeList);
        startActivity(intent);
        finish();
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveBtn:
                try {
                    if(!gmtFinishReceiveBarcodeList.isEmpty()){
                        if(finishCompanyId != 0 && fnCompanyId != 0 && fnCompanyWiseLocationId != 0){
                            try {
                                postDataToServer();
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }else{
                            DialogHelper.showWarningDialog(this, "Warning", "কোম্পানি এন্ড লোকেশন খুঁজে পাওয়া যায় নি ।");
                        }
                    }else{
                        DialogHelper.showWarningDialog(this, "Warning", "অনুগ্রহ করে কমপক্ষে একটি আইটেম যোগ করুন।");
                    }
                }catch (Exception e){
                    Log.d(TAG, "onClick: ");
                }

                break;
            case R.id.btBarcodeScan:
                if(fnCompanyId != 0 && fnCompanyWiseLocationId != 0 && fnLocationWiseFloorId != 0){
                    startScanning(1);
                }else{
                    DialogHelper.showWarningDialog(this, "Warning", "কোম্পানি এন্ড লোকেশন খুঁজে পাওয়া যায় নি ।");
                }
                break;
            case R.id.refreshBtn:
                refreshData();
                break;
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onRemoveHeadClick(int position, View v) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Remove Data?")
                .setContentText("আপনি কি এই আইটেমটি বাদ দিতে চান?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener(sDialog -> {
                    sDialog.cancel();
                    if(gmtFinishReceiveBarcodeList.size() > 0) {
                        gmtFinishReceiveBarcodeList.remove(position);
                        gtmFinishRecyclerViewAdapter.notifyDataSetChanged();
                    }
                })
                .setCancelClickListener(sDialog -> sDialog.cancel())
                .show();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fabric, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            if(!gmtFinishReceiveBarcodeList.isEmpty()){
                if(finishCompanyId != 0 && fnCompanyId != 0 && fnCompanyWiseLocationId != 0 && fnLocationWiseFloorId != 0){
                    try {
                        postDataToServer();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    DialogHelper.showWarningDialog(this, "Warning", "অনুগ্রহ করে ফ্লোর নির্বাচন করুন।");
                }
            }else{
                DialogHelper.showWarningDialog(this, "Warning", "অনুগ্রহ করে কমপক্ষে একটি আইটেম যোগ করুন।");
            }
            return true;
        } else if (id == R.id.action_new){
            refreshData();
        }  else if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void postDataToServer() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("STATUS", "true");
        jsonObject.put("USER_ID", userID);
        jsonObject.put("FINISH_COMPANY_ID", finishCompanyId);
        jsonObject.put("PO_COMPANY_ID", gmtFinishReceiveBarcodeList.get(0).getPoCompanyId());
        jsonObject.put("FINISH_LOCATION", gmtFinishReceiveBarcodeList.get(0).getSweingLocation());
        jsonObject.put("FINISH_FLOOR_ID", fnLocationWiseFloorId);
        jsonObject.put("FINISH_LINE_ID", fnFloorWiseLineId);
        jsonObject.put("BUYER_ID", gmtFinishReceiveBarcodeList.get(0).getBuyerId());
        jsonObject.put("PRODUCTION_SOURCE", gmtFinishReceiveBarcodeList.get(0).getProductionSource());

        JSONArray barcodeDetailsArray = new JSONArray();

        for (V1_GMTFinishReceiveResponse.Data item : gmtFinishReceiveBarcodeList) {
            JSONObject barcodeDetails = new JSONObject();
            barcodeDetails.put("PO_BREAK_DOWN_ID", item.getPoBreakDownId());
            barcodeDetails.put("CHALLAN_NO", item.getChallanNo());
            barcodeDetails.put("COLOR_ID", item.getColorNumberId());
            barcodeDetails.put("COLOR_TYPE_ID", item.getColorTypeId());
            barcodeDetails.put("SIZE_ID", item.getSizeNumberId());
            barcodeDetails.put("COUNTRY_ID", item.getCountryId());
            barcodeDetails.put("FINISH_COMPANY_ID", item.getSweingCompanyId());
            barcodeDetails.put("FINISH_LOCATION", item.getSweingLocation());
            barcodeDetails.put("FINISH_FLOOR_ID", item.getSweingFloorId());
            barcodeDetails.put("ITEM_NUMBER_ID", item.getItemNumberId());
            barcodeDetails.put("SEWING_LINE", item.getSewingLine());
            barcodeDetails.put("PRODUCTION_QNTY", item.getProductionQnty());
            barcodeDetails.put("PRODUCTION_DATE", item.getProductionDate());
            barcodeDetails.put("PRODUCTION_HOUR", item.getProductionHour());
            barcodeDetails.put("PO_COMPANY_ID", item.getPoCompanyId());
            barcodeDetails.put("BARCODE_NO", item.getBarcodeNo());
            barcodeDetails.put("BUNDLE_NO", item.getBundleNo());

            barcodeDetailsArray.put(barcodeDetails);
        }

        jsonObject.put("BARCODE_DTLS", barcodeDetailsArray);
        Log.d(TAG, "postDataToServer: ########" + jsonObject.toString());

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
        progressBarState();
        finishProductionViewModel.postGmtFinishReceiveResponse(body).observe(this, apiResponse -> {
            if(apiResponse != null && apiResponse.getStatusCode()) {
                DialogHelper.showSuccessDialog(this, "Success", apiResponse.getMsg());
                refreshData();
            }else{
                DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshData() {
        try {
            gmtFinishReceiveBarcodeList.clear();
            gtmFinishRecyclerViewAdapter.notifyDataSetChanged();
            _finishCompanySpinner.setSelection(0);
            _poCompanySpinner.setSelection(0);
            _fnLocationSpinner.setSelection(0);
            _fnFloorSpinner.setSelection(0);
            _fnLineSpinnner.setSelection(0);
            _totalFinRCVQntyTV.setText("");
            _totalBundleQNtyTV.setText("");
        }catch (Exception e){
            Log.d(TAG, "refreshData: ");
        }

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