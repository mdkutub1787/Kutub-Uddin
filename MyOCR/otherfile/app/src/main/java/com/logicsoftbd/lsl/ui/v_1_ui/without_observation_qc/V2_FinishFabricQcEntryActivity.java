package com.logicsoftbd.lsl.ui.v_1_ui.without_observation_qc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bixolon.labelprinter.BixolonLabelPrinter;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.model.FabricShade;
import com.logicsoftbd.lsl.data.network.model.FinishFabricResponse;
import com.logicsoftbd.lsl.data.network.model.SpinnerModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BarcodeByBatchForQCResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BarcodeDetailsFromBatchFinishQCResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FinishFabricQCDefectModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ShiftResponse;
import com.logicsoftbd.lsl.ui.process.greyroll.FinishFabricInputActivity;
import com.logicsoftbd.lsl.utils.DialogHelper;
import com.logicsoftbd.lsl.viewModel.FinishProductionViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class V2_FinishFabricQcEntryActivity extends AppCompatActivity implements View.OnClickListener, V2_BatchWiseBarcodeForFinishQCAdapter.OnHeadListener,
        V2_YardDefectAdapter.OnYarnHeadListener, V2_FinishFabricDefectQCAdapter.OnIncrementHeadListener, V2_FinishFabricDefectQCAdapter.OnDecrementHeadListener,
        V2_FinishFabricDefectQCAdapter.OnRemoveHeadListener, V2_KnittingDefectAdapter.OnKnittingHeadListener, V2_FinishingDefectAdapter.OnFinishingHeadListener,
        V2_CommonDefectAdapter.OnCommonHeadListener, V2_AOPPrintDefectAdapter.OnAOPHeadListener, V2_DyeingDefectAdapter.OnDyeingHeadListener {
    private static final String TAG = "V2_FinishFabricQcEntryA";
    private ProgressBar _progressBar;
    private RecyclerView _rollRecyclerView, _yarnDefectRecyclerView, _knittingRecyclerView, _dyeingRecyclerView, _commonRecyclerView, _finishingRecyclerView, _aopRecyclerView, _fabricDefectQCRecyclerView;
    private TextView _buyerNameTV, _batchTV, _refNoTV, _fileNoTV, _cons_compositeTV, _rectifyTV, _qcDateTV, _barcodeNoTV, _qcNameTV, _rollWeightKgTV, _weightLoss, _greyDiaTV, _greyGSMTV, _fabricGradeTV, _totalPointTV, _totalPenaltyPointTV;
    private EditText _rejectQntyET, _diaInchET, _gsmET, _ydsTV, _proQntyET, _commentEditField;
    private LinearLayout _rollLayout;
    private CardView _majorHoleCardView, _minorHoleCardView,  _majorCardView, _minorCardView, _gradeCardView, _gradeACardView, _outOfFourCardView;
    private LinearLayout _holeDefectLayout, _commonDefectLayout;
    private Spinner _rollStatusSpinner, _shiftSpinner, _shadeSpinner;
    private RadioButton _radio_major, _radio_minor;
    private String base_url, userID, userName, defectName, defectTypeName, defectId, currentDate, fgsm, mode, scanResult;
    private Boolean selectedDefectStatus = false;
    private Double yds, fProductionQty = 0.0;
    private  int roll_inch = 0, rollstatus = 0, rejectQty = 0, shiftId = 0, shadeId = 0, scan_op = 0, selectedDefectPosition = 0, bundleSelectionPosition = 0, qcPassType = 0, defectItemPosition = 0;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    BixolonLabelPrinter mBixolonLabelPrinter;
    ArrayAdapter<SpinnerModel> mArrayAdapter;
    private List<V1_BarcodeByBatchForQCResponse.Datum> barcodeByBatchIndexArrayList = new ArrayList<>();
    private List<V1_ShiftResponse.Resultset> companyWiseShift = new ArrayList<>();
    private List<FabricShade.Result> companyWiseShade = new ArrayList<>();
    private List<V1_BarcodeDetailsFromBatchFinishQCResponse.Yarn> yarnDefectArrayList = new ArrayList<>();
    private List<V1_BarcodeDetailsFromBatchFinishQCResponse.Knitting> knittingDefectArrayList = new ArrayList<>();
    private List<V1_BarcodeDetailsFromBatchFinishQCResponse.Finishing> finishingDefectArrayList = new ArrayList<>();
    private List<V1_BarcodeDetailsFromBatchFinishQCResponse.Common> commonDefectArrayList = new ArrayList<>();
    private List<V1_BarcodeDetailsFromBatchFinishQCResponse.Dyeing> dyeingDefectArrayList = new ArrayList<>();
    private List<V1_BarcodeDetailsFromBatchFinishQCResponse.AopPrint> aopDefectArrayList = new ArrayList<>();
    private List<V1_BarcodeDetailsFromBatchFinishQCResponse.Grade> gradeArrayList = new ArrayList<>();
    private final List<V1_FinishFabricQCDefectModel> finishFabricQCDefectModelArrayList = new ArrayList<>();
    private V1_BarcodeDetailsFromBatchFinishQCResponse barcodeDetailsFromBatchFinishQCResponse;
    private V2_BatchWiseBarcodeForFinishQCAdapter batchWiseBarcodeForFinishQCAdapter;
    private V2_FinishFabricDefectQCAdapter finishFabricDefectQCAdapter;
    private V2_YardDefectAdapter yardDefectAdapter;
    private V2_KnittingDefectAdapter knittingDefectAdapter;
    private V2_FinishingDefectAdapter finishingDefectAdapter;
    private V2_AOPPrintDefectAdapter aopPrintDefectAdapter;
    private V2_CommonDefectAdapter commonDefectAdapter;
    private V2_DyeingDefectAdapter dyeingDefectAdapter;
    private FinishProductionViewModel finishProductionViewModel;
    ArrayList<SpinnerModel> mArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_finish_fabric_qc_entry);

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        base_url = (_preferences.getString("base_url", ""));
        userID = _preferences.getString("login_userid", "");
        userName = _preferences.getString("login_username", "");

        finishProductionViewModel = new ViewModelProvider(this).get(FinishProductionViewModel.class);
        init_ui();

        fetchCompanyWiseShift();
        fetchCompanyWiseShade();
        getDefaultData();
    }

    private void getDefaultData() {
        Intent intent = getIntent();
        scanResult = intent.getStringExtra("barcodeScan");
        scan_op = intent.getIntExtra("scan_op", 0);
        if(scan_op == 1){
            fetchBarcodeDetailsData(scanResult);
            _rollLayout.setVisibility(View.GONE);
        }else {
            fetchBarcodeDataByScanningBachNo(scanResult);
            _rollLayout.setVisibility(View.VISIBLE);
        }

    }

    private void fetchBarcodeDataByScanningBachNo(String batchScan) {
        progressBarState();
        finishProductionViewModel.getBarcodeByBatchForQCResponse(batchScan).observe(this, apiResponse -> {
            if(apiResponse!= null){
                if(apiResponse.getResultSet().getData() != null && apiResponse.getResultSet().getData().size() > 0) {
                    Log.d(TAG, "fetchBarcodeDataByScaningBachNo: "+ apiResponse);
                    if(apiResponse.getResultSet().getData().size() > 0){
                        barcodeByBatchIndexArrayList = apiResponse.getResultSet().getData();
                    }
                    try {
                        setupBarcodeByBatchRecyclerView();
                    }catch (Exception e){
                        DialogHelper.showWarningDialog(this, "Message", "Batch data not found. Please try again.");
                    }
                }
                else{
                    DialogHelper.showWarningDialog(this, "Message", apiResponse.getResultSet().getMsg());
                }
            }else{
                DialogHelper.showErrorDialog(this, "Error Message", "Something went wrong!");
            }
        });
    }

    private void fetchCompanyWiseShift() {
        progressBarState();
        finishProductionViewModel.getShiftResponse().observe(this, apiResponse -> {
            if(apiResponse != null) {
                if(apiResponse.getResultset() != null && apiResponse.getResultset().size() > 0){
                    companyWiseShift = apiResponse.getResultset();
                    try {
                        setShiftAdapter();
                    }catch (Exception e){
                        DialogHelper.showWarningDialog(this, "Message", "Shift data not found. Please try again.");
                    }
                }
            }else {
                DialogHelper.showErrorDialog(this, "Error Message", "Something went wrong!");
            }
        });
    }

    private void fetchCompanyWiseShade() {
        progressBarState();
        finishProductionViewModel.getShadeResponse().observe(this, apiResponse -> {
            if(apiResponse != null) {
                if(apiResponse.getData() != null && apiResponse.getData().size() > 0){
                    companyWiseShade = apiResponse.getData();
                    try {
                        setShadeAdapter();
                    }catch (Exception e){
                        DialogHelper.showWarningDialog(this, "Message", "Shade date not found. Please try again.");
                    }
                }
            }else {
                DialogHelper.showErrorDialog(this, "Error Message", "Something went wrong!");
            }
        });
    }

    private void fetchBarcodeDetailsData(String barcode) {
        progressBarState();
        finishProductionViewModel.getBarcodeDetailsFromBatchForQCResponse(barcode).observe(this, apiResponse -> {
            if(apiResponse!= null){
                if(apiResponse.getData() != null && apiResponse.getData().getIndex() != null) {
                    Log.d(TAG, "fetchBarcodeDataByScaningBachNo: "+ apiResponse);
                    barcodeDetailsFromBatchFinishQCResponse = apiResponse;
                    try {
                        _buyerNameTV.setText(apiResponse.getData().getIndex().getBuyerName());
                        _batchTV.setText(apiResponse.getData().getIndex().getBatchNo());
                        _refNoTV.setText(apiResponse.getData().getIndex().getRefNo());
                        _fileNoTV.setText(apiResponse.getData().getIndex().getFileNo());
                        _cons_compositeTV.setText(apiResponse.getData().getIndex().getConstruction());
                        _rectifyTV.setText(apiResponse.getData().getIndex().getRectifyNo());
                        _barcodeNoTV.setText(apiResponse.getData().getIndex().getBarcodeNo());
                        _qcNameTV.setText(userName);
                        if(apiResponse.getData().getIndex().getQnty() != null){
                            double weightKg = Double.parseDouble(apiResponse.getData().getIndex().getQnty());
                            String formattedKg = String.format("%.2f", weightKg);
                            _rollWeightKgTV.setText(formattedKg);
                        }
                        double rejectQnty = Double.parseDouble(apiResponse.getData().getIndex().getRejectQnty());
                        _rejectQntyET.setText(String.format("%.2f", rejectQnty));
                        _greyDiaTV.setText(apiResponse.getData().getIndex().getWidth());
                        _gsmET.setText(apiResponse.getData().getIndex().getGsm());
                        _greyGSMTV.setText(apiResponse.getData().getIndex().getGsm());
                        _commentEditField.setText(apiResponse.getData().getIndex().getComments());
                        if(apiResponse.getData().getIndex().getProdQnty() != null){
                            double productionQty = Double.parseDouble(apiResponse.getData().getIndex().getProdQnty());
                            String formattedQty = String.format("%.2f", productionQty);
                            _proQntyET.setText(formattedQty);
                        }
                        fProductionQty = Double.valueOf(apiResponse.getData().getIndex().getProdQnty());
                        fgsm =apiResponse.getData().getIndex().getGsm();

                        if(!apiResponse.getData().getIndex().getRollWidth().equals("0")){
                            _diaInchET.setText(apiResponse.getData().getIndex().getRollWidth());
                        }
                        rollstatus = Integer.parseInt(apiResponse.getData().getIndex().getRollStatus());
                        _rollStatusSpinner.setSelection(rollstatus);

                        double wgtLost = fProductionQty - Double.parseDouble(
                                (_proQntyET.getText().toString() == null || _proQntyET.getText().toString().trim().isEmpty())
                                        ? "0"
                                        : _proQntyET.getText().toString()
                        );
                        String formattedwgtLoss = String.format("%.2f", wgtLost);
                        _weightLoss.setText(formattedwgtLoss);


                        if(apiResponse.getData().getIndex().getArrayRefData() != null && apiResponse.getData().getIndex().getArrayRefData().getDefect() != null
                                && apiResponse.getData().getIndex().getArrayRefData().getDefect().getYarn() != null && apiResponse.getData().getIndex().getArrayRefData().getDefect().getYarn().size() > 0) {
                            yarnDefectArrayList = apiResponse.getData().getIndex().getArrayRefData().getDefect().getYarn();
                            setupYardDefectRecyclerView();
                        }
                        if(apiResponse.getData().getIndex().getArrayRefData() != null && apiResponse.getData().getIndex().getArrayRefData().getDefect() != null
                                && apiResponse.getData().getIndex().getArrayRefData().getDefect().getKnitting() != null && apiResponse.getData().getIndex().getArrayRefData().getDefect().getKnitting().size() > 0) {
                            knittingDefectArrayList = apiResponse.getData().getIndex().getArrayRefData().getDefect().getKnitting();
                            setupKnittingDefectRecyclerView();
                        }

                        if(apiResponse.getData().getIndex().getArrayRefData() != null && apiResponse.getData().getIndex().getArrayRefData().getDefect() != null
                                && apiResponse.getData().getIndex().getArrayRefData().getDefect().getFinishings() != null && apiResponse.getData().getIndex().getArrayRefData().getDefect().getFinishings().size() > 0) {
                            finishingDefectArrayList = apiResponse.getData().getIndex().getArrayRefData().getDefect().getFinishings();
                            setupFinishingDefectRecyclerView();
                        }
                        if(apiResponse.getData().getIndex().getArrayRefData() != null && apiResponse.getData().getIndex().getArrayRefData().getDefect() != null
                                && apiResponse.getData().getIndex().getArrayRefData().getDefect().getCommon() != null && apiResponse.getData().getIndex().getArrayRefData().getDefect().getCommon().size() > 0) {
                            commonDefectArrayList = apiResponse.getData().getIndex().getArrayRefData().getDefect().getCommon();
                            setupCommonDefectRecyclerView();
                        }
                        if(apiResponse.getData().getIndex().getArrayRefData() != null && apiResponse.getData().getIndex().getArrayRefData().getDefect() != null
                                && apiResponse.getData().getIndex().getArrayRefData().getDefect().getDyeing() != null && apiResponse.getData().getIndex().getArrayRefData().getDefect().getDyeing().size() > 0) {
                            dyeingDefectArrayList = apiResponse.getData().getIndex().getArrayRefData().getDefect().getDyeing();
                            setupDyeingDefectRecyclerView();
                        }
                        if(apiResponse.getData().getIndex().getArrayRefData() != null && apiResponse.getData().getIndex().getArrayRefData().getDefect() != null
                                && apiResponse.getData().getIndex().getArrayRefData().getDefect().getAopprint() != null && apiResponse.getData().getIndex().getArrayRefData().getDefect().getAopprint().size() > 0) {
                            aopDefectArrayList = apiResponse.getData().getIndex().getArrayRefData().getDefect().getAopprint();
                            setupAOPPrintDefectRecyclerView();
                        }
                        if(apiResponse.getData().getIndex().getArrayRefData() != null && apiResponse.getData().getIndex().getArrayRefData().getGrade() != null
                                && apiResponse.getData().getIndex().getArrayRefData().getGrade().size() > 0) {
                            gradeArrayList = apiResponse.getData().getIndex().getArrayRefData().getGrade();
                        }

                        if(apiResponse.getData().getIndex().getMode().equals("update")) {
                            _totalPointTV.setText(apiResponse.getData().getIndex().getTotalPoint());
                            double _penaltyPoint = Double.parseDouble(apiResponse.getData().getIndex().getTotalPenaltyPoint());
                            String formattedQty = String.format("%.2f", _penaltyPoint);
                            _totalPenaltyPointTV.setText(formattedQty);
                            _fabricGradeTV.setText(apiResponse.getData().getIndex().getFabricGrade());
                            setUpDefectCountListForUpdate(apiResponse);
                            defectItemPosition = finishFabricQCDefectModelArrayList.size();
                        }
                    }catch (Exception e){
                        Log.d(TAG, "fetchBarcodeDetailsData: "+e.getMessage()+e);
                        DialogHelper.showWarningDialog(this, "Message", "Barcode details not found. Please try again.");
                    }
                }
                else{
                    DialogHelper.showWarningDialog(this, "Message", apiResponse.getShadeMsg());
                }
            }else{
                DialogHelper.showErrorDialog(this, "Error Message", "Something went wrong!");
            }
        });
    }

    private void setupKnittingDefectRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        _knittingRecyclerView.setLayoutManager(gridLayoutManager);
        _knittingRecyclerView.setLayoutManager(linearLayoutManager);
        knittingDefectAdapter = new V2_KnittingDefectAdapter( knittingDefectArrayList, this, this);
        _knittingRecyclerView.setAdapter(knittingDefectAdapter);
    }

    private void setupFinishingDefectRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        _finishingRecyclerView.setLayoutManager(gridLayoutManager);
        _finishingRecyclerView.setLayoutManager(linearLayoutManager);
        finishingDefectAdapter = new V2_FinishingDefectAdapter( finishingDefectArrayList, this, this);
        _finishingRecyclerView.setAdapter(finishingDefectAdapter);
    }

    private void setupCommonDefectRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        _commonRecyclerView.setLayoutManager(gridLayoutManager);
        _commonRecyclerView.setLayoutManager(linearLayoutManager);
        commonDefectAdapter = new V2_CommonDefectAdapter( commonDefectArrayList, this, this);
        _commonRecyclerView.setAdapter(commonDefectAdapter);
    }
    private void setupDyeingDefectRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        _dyeingRecyclerView.setLayoutManager(gridLayoutManager);
        _dyeingRecyclerView.setLayoutManager(linearLayoutManager);
        dyeingDefectAdapter = new V2_DyeingDefectAdapter( dyeingDefectArrayList, this, this);
        _dyeingRecyclerView.setAdapter(dyeingDefectAdapter);
    }

    private void setupAOPPrintDefectRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        _aopRecyclerView.setLayoutManager(gridLayoutManager);
        _aopRecyclerView.setLayoutManager(linearLayoutManager);
        aopPrintDefectAdapter = new V2_AOPPrintDefectAdapter( aopDefectArrayList, this, this);
        _aopRecyclerView.setAdapter(aopPrintDefectAdapter);
    }

    private void setupYardDefectRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _yarnDefectRecyclerView.setLayoutManager(linearLayoutManager);
        yardDefectAdapter = new V2_YardDefectAdapter( yarnDefectArrayList, this, this);
        _yarnDefectRecyclerView.setAdapter(yardDefectAdapter);
    }

    private void setupFinishQCDefectRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _fabricDefectQCRecyclerView.setLayoutManager(linearLayoutManager);
        finishFabricDefectQCAdapter = new V2_FinishFabricDefectQCAdapter( finishFabricQCDefectModelArrayList, this, this, this, this);
        _fabricDefectQCRecyclerView.setAdapter(finishFabricDefectQCAdapter);
    }

    private void setupBarcodeByBatchRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _rollRecyclerView.setLayoutManager(linearLayoutManager);
        batchWiseBarcodeForFinishQCAdapter = new V2_BatchWiseBarcodeForFinishQCAdapter( barcodeByBatchIndexArrayList, this, this);
        _rollRecyclerView.setAdapter(batchWiseBarcodeForFinishQCAdapter);
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

    private void init_ui() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mArrayList = getList();
        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mArrayList);
        mArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBixolonLabelPrinter = new BixolonLabelPrinter(this, mHandler, Looper.getMainLooper());

        _progressBar = findViewById(R.id.progressBar);
        _rollRecyclerView = findViewById(R.id.rollRecyclerView);
        _yarnDefectRecyclerView = findViewById(R.id.yarnDefectRecyclerView);
        _knittingRecyclerView = findViewById(R.id.knittingRecyclerView);
        _dyeingRecyclerView = findViewById(R.id.dyeingRecyclerView);
        _commonRecyclerView = findViewById(R.id.commonRecyclerView);
        _finishingRecyclerView = findViewById(R.id.finishingRecyclerView);
        _aopRecyclerView = findViewById(R.id.aopRecyclerView);
        _fabricDefectQCRecyclerView = findViewById(R.id.fabricDefectQCRecyclerView);
        _buyerNameTV = findViewById(R.id.buyerNameTV);
        _batchTV = findViewById(R.id.batchTV);
        _refNoTV = findViewById(R.id.refNoTV);
        _fileNoTV = findViewById(R.id.fileNoTV);
        _cons_compositeTV = findViewById(R.id.cons_compositeTV);
        _rectifyTV = findViewById(R.id.rectifyTV);
        _qcDateTV = findViewById(R.id.qcDateTV);
        _barcodeNoTV = findViewById(R.id.barcodeNoTV);
        _qcNameTV = findViewById(R.id.qcNameTV);
        _rollWeightKgTV = findViewById(R.id.rollWeightKgTV);
        _weightLoss = findViewById(R.id.weightLoss);
        _rejectQntyET = findViewById(R.id.rejectQntyET);
        _greyDiaTV = findViewById(R.id.greyDiaTV);
        _greyGSMTV = findViewById(R.id.greyGSMTV);
        _fabricGradeTV = findViewById(R.id.fabricGradeTV);
        _totalPointTV = findViewById(R.id.totalPointTV);
        _totalPenaltyPointTV = findViewById(R.id.totalPenaltyPointTV);
        _diaInchET = findViewById(R.id.diaInchET);
        _gsmET = findViewById(R.id.gsmET);
        _ydsTV = findViewById(R.id.ydsTV);
        _proQntyET = findViewById(R.id.proQntyET);
        _commentEditField = findViewById(R.id.commentEditField);
        _rollLayout = findViewById(R.id.rollLayout);
        _rollStatusSpinner = findViewById(R.id.rollStatusSpinner);
        _shiftSpinner = findViewById(R.id.shiftSpinner);
        _shadeSpinner = findViewById(R.id.shadeSpinner);
        _majorHoleCardView = findViewById(R.id.majorHoleCardView);
        _majorHoleCardView.setOnClickListener(this);
        _minorHoleCardView = findViewById(R.id.minorHoleCardView);
        _minorHoleCardView.setOnClickListener(this);
        _majorCardView = findViewById(R.id.majorCardView);
        _majorCardView.setOnClickListener(this);
        _minorCardView = findViewById(R.id.minorCardView);
        _minorCardView.setOnClickListener(this);
        _gradeCardView = findViewById(R.id.gradeCardView);
        _gradeCardView.setOnClickListener(this);
        _gradeACardView = findViewById(R.id.gradeACardView);
        _gradeACardView.setOnClickListener(this);
        _outOfFourCardView = findViewById(R.id.outOfFourCardView);
        _outOfFourCardView.setOnClickListener(this);

        _holeDefectLayout = findViewById(R.id.holeDefectLayout);
        _commonDefectLayout = findViewById(R.id.commonDefectLayout);

        _radio_major = findViewById(R.id.radio_major);
        _radio_minor = findViewById(R.id.radio_minor);

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = simpleDateFormat.format(calendar.getTime());
        _qcDateTV.setText(currentDate);

        setStatusAdapter();
        _diaInchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!_diaInchET.getText().toString().isEmpty()){
//                    calculationYds();
                    calculateTotalPenaltyCount();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        _proQntyET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!_proQntyET.getText().toString().isEmpty()){
                    if(fProductionQty != 0.0){
                        double wgtLost = fProductionQty - Double.parseDouble(_proQntyET.getText().toString());
                        _weightLoss.setText(String.valueOf(wgtLost));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        _radio_major.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                qcPassType = 1;
                _rollStatusSpinner.setSelection(2);
            }
        });
        _radio_minor.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                qcPassType = 2;
                _rollStatusSpinner.setSelection(1);
            }
        });

        _rejectQntyET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateWeight();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @SuppressLint("DefaultLocale")
    private void calculateWeight() {
        try {
            double rejectQnty = 0.0;

            if (!_rejectQntyET.getText().toString().isEmpty()) {
                rejectQnty = Double.parseDouble(_rejectQntyET.getText().toString());
            }

            if (!barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getProdQnty().isEmpty()) {
                double prodQnty = Double.parseDouble(barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getProdQnty());
                double _calculatedProdQnty = prodQnty - rejectQnty;
                _proQntyET.setText(String.format("%.2f", _calculatedProdQnty));
            } else {
                _proQntyET.setText("");
            }
        } catch (Exception e) {
            Log.d(TAG, "calculateWeight: " + e.getMessage());
        }
    }


    private void setStatusAdapter() {
        ArrayAdapter<CharSequence> adapterhole = ArrayAdapter.createFromResource(this, R.array.statusroll
                , android.R.layout.simple_spinner_dropdown_item);
        _rollStatusSpinner.setAdapter(adapterhole);
        _rollStatusSpinner.setSelection(rollstatus);

        _rollStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                        rollstatus = 0;
                        rejectQty = 0;
//                        _rejectQntyET.setText(String.valueOf(rejectQty));

                        break;
                    case 1:
                        rollstatus = 1;
                        //rejectET.setText(String.valueOf(rejectQty));
                        break;
                    case 2:
                        rollstatus = 2;
                        rejectQty = 0;
//                        _rejectQntyET.setText(String.valueOf(rejectQty));

                        break;
                    case 3:
                        rollstatus = 3;
                        double roll_kg = Double.parseDouble(_rejectQntyET.getText().toString());
                        if(  roll_kg >= fProductionQty)
                        {
                            _rejectQntyET.setText(String.valueOf(fProductionQty));
                        }
                        _rejectQntyET.setText(String.valueOf(fProductionQty));
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setShiftAdapter() {
        List<String> _shiftNames = new ArrayList<>();
        List<Integer> _shiftId = new ArrayList<>();
        for (V1_ShiftResponse.Resultset shift : companyWiseShift) {
            _shiftNames.add(shift.getShiftName());
            _shiftId.add(shift.getShiftId());
        }
        ArrayAdapter<String> adapterhole = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, _shiftNames);

        _shiftSpinner.setAdapter(adapterhole);
        _shiftSpinner.setSelection(_shiftId.indexOf(shiftId));

        _shiftSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shiftId = Integer.parseInt(String.valueOf(_shiftId.get(position)));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setShadeAdapter() {
        List<String> _shadeNames = new ArrayList<>();
        List<Integer> _shadeId = new ArrayList<>();
        for (FabricShade.Result shade : companyWiseShade) {
            _shadeNames.add(shade.getSTORE_NAME());
            _shadeId.add(Integer.valueOf(shade.getID()));
        }
        ArrayAdapter<String> adapterhole = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, _shadeNames);

        _shadeSpinner.setAdapter(adapterhole);
        _shadeSpinner.setSelection(_shadeId.indexOf(shadeId));

        _shadeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shadeId = Integer.parseInt(String.valueOf(_shadeId.get(position)));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onSelectClick(int position, View v) {
//        if(!_barcodeNoTV.getText().toString().equals("") && !_barcodeNoTV.getText().toString().equals(barcodeByBatchIndexArrayList.get(position).getBarcodeNo()) && finishFabricQCDefectModelArrayList.size() > 0){
//            refreshUIForNew(position);
//        }

        bundleSelectionPosition = position;

        if( finishFabricQCDefectModelArrayList.size() > 0){
            refreshUIForNew(position);
        }else{
            for (int i = 0; i < barcodeByBatchIndexArrayList.size(); i++) {
                barcodeByBatchIndexArrayList.get(i).setStatus(false);
            }
            barcodeByBatchIndexArrayList.get(position).setStatus(true);
            batchWiseBarcodeForFinishQCAdapter.notifyDataSetChanged();
            setFormForNewEntry();
            fetchBarcodeDetailsData(barcodeByBatchIndexArrayList.get(position).getBarcodeNo());
        }


    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onYarnDefectClick(int position, View v) {
        if(selectedDefectStatus){
            DialogHelper.showWarningDialog(this, "Warning", "Please add previous selected defect to the defect count list.");
        }else{
            if(!yarnDefectArrayList.get(position).isStatus()){
                defectName = yarnDefectArrayList.get(position).getDefectName();
                defectId = yarnDefectArrayList.get(position).getId();
                yarnDefectArrayList.get(position).setStatus(true);
                yardDefectAdapter.notifyDataSetChanged();
                selectedDefectStatus = true;
                defectTypeName = "yarn";
                selectedDefectPosition = position;

                maintainDefectStatus();
            }
        }

        Log.d(TAG, "onYarnDefectClick: "+yarnDefectArrayList.get(position).getDefectName());
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onKnittingDefectClick(int position, View v) {
        if(selectedDefectStatus){
            DialogHelper.showWarningDialog(this, "Warning", "Please add previous selected defect to the defect count list.");
        }else{
            if(!knittingDefectArrayList.get(position).isStatus()){
                defectName = knittingDefectArrayList.get(position).getDefectName();
                defectId = knittingDefectArrayList.get(position).getId();
                knittingDefectArrayList.get(position).setStatus(true);
                knittingDefectAdapter.notifyDataSetChanged();
                selectedDefectStatus = true;
                defectTypeName = "knitting";
                selectedDefectPosition = position;

                maintainDefectStatus();
            }
        }
        Log.d(TAG, "onKnittingDefectClick: "+knittingDefectArrayList.get(position).getDefectName());
    }



    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onAOPDefectClick(int position, View v) {
        if(selectedDefectStatus){
            DialogHelper.showWarningDialog(this, "Warning", "Please add previous selected defect to the defect count list.");
        }else{
            if(!aopDefectArrayList.get(position).isStatus()){
                defectName = aopDefectArrayList.get(position).getDefectName();
                defectId = aopDefectArrayList.get(position).getId();
                aopDefectArrayList.get(position).setStatus(true);
                aopPrintDefectAdapter.notifyDataSetChanged();
                selectedDefectStatus = true;
                defectTypeName = "aop";
                selectedDefectPosition = position;
                maintainDefectStatus();
            }
        }

        Log.d(TAG, "onAOPDefectClick: "+aopDefectArrayList.get(position).getDefectName());
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onCommonDefectClick(int position, View v) {
        if(selectedDefectStatus){
            DialogHelper.showWarningDialog(this, "Warning", "Please add previous selected defect to the defect count list.");
        }else{
            if(!commonDefectArrayList.get(position).isStatus()){
                defectName = commonDefectArrayList.get(position).getDefectName();
                defectId = commonDefectArrayList.get(position).getId();
                commonDefectArrayList.get(position).setStatus(true);
                commonDefectAdapter.notifyDataSetChanged();
                selectedDefectStatus = true;
                defectTypeName = "common";
                selectedDefectPosition = position;
                maintainDefectStatus();
            }
        }
        Log.d(TAG, "onCommonDefectClick: "+commonDefectArrayList.get(position).getDefectName());
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDyeingDefectClick(int position, View v) {
        if(selectedDefectStatus){
            DialogHelper.showWarningDialog(this, "Warning", "Please add previous selected defect to the defect count list.");
        }else{
            if(!dyeingDefectArrayList.get(position).isStatus()){
                defectName = dyeingDefectArrayList.get(position).getDefectName();
                defectId = dyeingDefectArrayList.get(position).getId();
                dyeingDefectArrayList.get(position).setStatus(true);
                dyeingDefectAdapter.notifyDataSetChanged();
                selectedDefectStatus = true;
                defectTypeName = "dyeing";
                selectedDefectPosition = position;
                maintainDefectStatus();
            }
        }
        Log.d(TAG, "onDyeingDefectClick: "+dyeingDefectArrayList.get(position).getDefectName());
    }

    private void maintainDefectStatus() {
        Log.d(TAG, "maintainDefectStatus: "+defectName);
        if(defectName.equals("Hole")) {
            _holeDefectLayout.setVisibility(View.VISIBLE);
            _commonDefectLayout.setVisibility(View.GONE);
        } else {
            _holeDefectLayout.setVisibility(View.GONE);
            _commonDefectLayout.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onFinishingDefectClick(int position, View v) {
        if(selectedDefectStatus){
            DialogHelper.showWarningDialog(this, "Warning", "Please add previous selected defect to the defect count list.");
        }else{
            if(!finishingDefectArrayList.get(position).isStatus()){
                defectName = finishingDefectArrayList.get(position).getDefectName();
                defectId = finishingDefectArrayList.get(position).getId();
                finishingDefectArrayList.get(position).setStatus(true);
                finishingDefectAdapter.notifyDataSetChanged();
                selectedDefectStatus = true;
                defectTypeName = "finishing";
                selectedDefectPosition = position;
                maintainDefectStatus();
            }
        }

        Log.d(TAG, "onFinishingDefectClick: "+finishingDefectArrayList.get(position).getDefectName());
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onIncrementClick(int position, View v) {
        Log.d(TAG, "onInCrementClick: "+finishFabricQCDefectModelArrayList.get(position).getDefectName());
        finishFabricQCDefectModelArrayList.get(position).setDefectCount(finishFabricQCDefectModelArrayList.get(position).getDefectCount()+1);
        finishFabricDefectQCAdapter.notifyDataSetChanged();
        calculateTotalDefectCount(position);
        calculationYds();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void calculateTotalDefectCount(Integer position) {
        try {
            finishFabricQCDefectModelArrayList.get(position).setDefectPenalty(finishFabricQCDefectModelArrayList.get(position).getDefectCount() * finishFabricQCDefectModelArrayList.get(position).getDefectFound());
            calculateTotalPenaltyCount();
        } catch (Exception e) {
            Log.d(TAG, "calculateTotalDefectCount: "+e.getMessage()+e);
        }
        finishFabricDefectQCAdapter.notifyDataSetChanged();
    }

    private void calculateTotalPenaltyCount() {
        int totalDefectCount = 0;
        for (V1_FinishFabricQCDefectModel defect : finishFabricQCDefectModelArrayList) {
            totalDefectCount += defect.getDefectPenalty();
        }
        _totalPointTV.setText(String.valueOf(totalDefectCount));
        calculationYds();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDecrementClick(int position, View v) {
        Log.d(TAG, "onDecrementClick: "+finishFabricQCDefectModelArrayList.get(position).getDefectName());
        if(finishFabricQCDefectModelArrayList.get(position).getDefectCount() > 1) {
            finishFabricQCDefectModelArrayList.get(position).setDefectCount(finishFabricQCDefectModelArrayList.get(position).getDefectCount() - 1);
            finishFabricDefectQCAdapter.notifyDataSetChanged();
            calculateTotalDefectCount(position);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onRemoveClick(int position, View v) {
        try {
            Log.d(TAG, "onRemoveClick: "+finishFabricQCDefectModelArrayList.get(position).getDefectName());
            defectItemPosition--;

            String defectNameToRemove = finishFabricQCDefectModelArrayList.get(position).getDefectId();
            String defectTypeName = finishFabricQCDefectModelArrayList.get(position).getDefectTypeName();

            if (defectTypeName.equals("yarn")) {
                for (V1_BarcodeDetailsFromBatchFinishQCResponse.Yarn yarnDefect : yarnDefectArrayList) {
                    if (yarnDefect.getId().equals(defectNameToRemove)) {
                        yarnDefect.setStatus(false);
                        break;
                    }
                }
                yardDefectAdapter.notifyDataSetChanged();
            }

            if (defectTypeName.equals("aop")) {
                for (V1_BarcodeDetailsFromBatchFinishQCResponse.AopPrint aopDefect : aopDefectArrayList) {
                    if (aopDefect.getId().equals(defectNameToRemove)) {
                        aopDefect.setStatus(false);
                        break;
                    }
                }
                aopPrintDefectAdapter.notifyDataSetChanged();
            }

            if (defectTypeName.equals("knitting")) {
                for (V1_BarcodeDetailsFromBatchFinishQCResponse.Knitting knittingDefect : knittingDefectArrayList) {
                    if (knittingDefect.getId().equals(defectNameToRemove)) {
                        knittingDefect.setStatus(false);
                        break;
                    }
                }
                knittingDefectAdapter.notifyDataSetChanged();
            }

            if (defectTypeName.equals("finishing")) {
                for (V1_BarcodeDetailsFromBatchFinishQCResponse.Finishing finishingDefect : finishingDefectArrayList) {
                    if (finishingDefect.getId().equals(defectNameToRemove)) {
                        finishingDefect.setStatus(false);
                        break;
                    }
                }
                finishingDefectAdapter.notifyDataSetChanged();
            }

            if (defectTypeName.equals("common")) {
                for (V1_BarcodeDetailsFromBatchFinishQCResponse.Common commonDefect : commonDefectArrayList) {
                    if (commonDefect.getId().equals(defectNameToRemove)) {
                        commonDefect.setStatus(false);
                        break;
                    }
                }
                commonDefectAdapter.notifyDataSetChanged();
            }

            if (defectTypeName.equals("dyeing")) {
                for (V1_BarcodeDetailsFromBatchFinishQCResponse.Dyeing dyeingDefect : dyeingDefectArrayList) {
                    if (dyeingDefect.getId().equals(defectNameToRemove)) {
                        dyeingDefect.setStatus(false);
                        break;
                    }
                }
                dyeingDefectAdapter.notifyDataSetChanged();
            }

            finishFabricQCDefectModelArrayList.remove(position);
            finishFabricDefectQCAdapter.notifyDataSetChanged();
            calculateTotalPenaltyCount();
        }catch (Exception e){
            Log.d(TAG, "onRemoveClick: "+e.getMessage());
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.majorHoleCardView:
                addDefectCountFabricQC(2);
                break;
            case R.id.minorHoleCardView:
                addDefectCountFabricQC(4);
                break;
            case R.id.majorCardView:
                addDefectCountFabricQC(1);
                break;
            case R.id.minorCardView:
                addDefectCountFabricQC(2);
                break;
            case R.id.gradeCardView:
                addDefectCountFabricQC(3);
                break;
            case R.id.gradeACardView:
                addDefectCountFabricQC(4);
                break;
            case R.id.outOfFourCardView:
                addDefectCountFabricQC(0);
                break;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addDefectCountFabricQC(int foundInInch) {
        if(selectedDefectStatus){
            V1_FinishFabricQCDefectModel model;
            if(foundInInch == 0){
                model = new V1_FinishFabricQCDefectModel(defectId, defectName, 1, foundInInch, 0, defectTypeName);
            } else {
                model = new V1_FinishFabricQCDefectModel(defectId, defectName, 1, foundInInch, 1, defectTypeName);
            }

            finishFabricQCDefectModelArrayList.add(model);
            setupFinishQCDefectRecyclerView();
            finishFabricDefectQCAdapter.notifyDataSetChanged();
            selectedDefectStatus = false;
            calculateTotalPenaltyCount();

            if(defectTypeName.equals("yarn")){
                yarnDefectArrayList.get(selectedDefectPosition).setStatus(true);
                yardDefectAdapter.notifyDataSetChanged();
            }
            if(defectTypeName.equals("aop")){
                aopDefectArrayList.get(selectedDefectPosition).setStatus(true);
                aopPrintDefectAdapter.notifyDataSetChanged();
            }
            if(defectTypeName.equals("common")){
                commonDefectArrayList.get(selectedDefectPosition).setStatus(true);
                commonDefectAdapter.notifyDataSetChanged();
            }
            if(defectTypeName.equals("dyeing")){
                dyeingDefectArrayList.get(selectedDefectPosition).setStatus(true);
                dyeingDefectAdapter.notifyDataSetChanged();
            }
            if(defectTypeName.equals("knitting")){
                knittingDefectArrayList.get(selectedDefectPosition).setStatus(true);
                knittingDefectAdapter.notifyDataSetChanged();
            }
            if(defectTypeName.equals("finishing")){
                finishingDefectArrayList.get(selectedDefectPosition).setStatus(true);
                finishingDefectAdapter.notifyDataSetChanged();
            }

            calculateTotalDefectCount(defectItemPosition);
            defectItemPosition += 1;
        }else {
            DialogHelper.showWarningDialog(this, "Warning", "Without defect selected, can't add defect to the list.");
            selectedDefectStatus = false;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setUpDefectCountListForUpdate(V1_BarcodeDetailsFromBatchFinishQCResponse apiResponse) {

        if (apiResponse != null
                && apiResponse.getData() != null
                && apiResponse.getData().getIndex() != null
                && apiResponse.getData().getIndex().getArrayRefData() != null
                && apiResponse.getData().getIndex().getArrayRefData().getDefect() != null
                && apiResponse.getData().getIndex().getArrayRefData().getDefect().getKnitting() != null
                && apiResponse.getData().getIndex().getArrayRefData().getDefect().getKnitting().size() > 0) {

            for (V1_BarcodeDetailsFromBatchFinishQCResponse.Knitting defect : apiResponse.getData().getIndex().getArrayRefData().getDefect().getKnitting()) {
                if (!defect.getDefectCount().equals("0")
                        && !defect.getFoundInInch().equals("0")
                        && !defect.getPenaltyPoint().equals("0")) {

                    finishFabricQCDefectModelArrayList.add(new V1_FinishFabricQCDefectModel(
                            defect.getId(),
                            defect.getDefectName(),
                            Integer.parseInt(defect.getDefectCount()),
                            Integer.parseInt(defect.getFoundInInch()),
                            Integer.parseInt(defect.getPenaltyPoint()),
                            "knitting"
                    ));

                    int index = -1;
                    for (int i = 0; i < knittingDefectArrayList.size(); i++) {
                        if (knittingDefectArrayList.get(i).getDefectName().trim().equalsIgnoreCase(defect.getDefectName().trim())) {
                            index = i;
                            break;
                        }
                    }

                    if (index != -1) {
                        knittingDefectArrayList.get(index).setStatus(true);
                        knittingDefectAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("KnittingDefect", "Defect not found: " + defect.getDefectName());
                    }
                }
            }
            setupFinishQCDefectRecyclerView();

            if (finishFabricDefectQCAdapter != null) {
                finishFabricDefectQCAdapter.notifyDataSetChanged();
            } else {
                Log.e("####", "Adapter is null. Initialization needed.");
            }
        }

        if (apiResponse != null
                && apiResponse.getData() != null
                && apiResponse.getData().getIndex() != null
                && apiResponse.getData().getIndex().getArrayRefData() != null
                && apiResponse.getData().getIndex().getArrayRefData().getDefect() != null
                && apiResponse.getData().getIndex().getArrayRefData().getDefect().getYarn() != null
                && apiResponse.getData().getIndex().getArrayRefData().getDefect().getYarn().size() > 0) {

            for (V1_BarcodeDetailsFromBatchFinishQCResponse.Yarn defect : apiResponse.getData().getIndex().getArrayRefData().getDefect().getYarn()) {
                if (!defect.getDefectCount().equals("0")
                        && !defect.getFoundInInch().equals("0")
                        && !defect.getPenaltyPoint().equals("0")) {

                    finishFabricQCDefectModelArrayList.add(new V1_FinishFabricQCDefectModel(
                            defect.getId(),
                            defect.getDefectName(),
                            Integer.parseInt(defect.getDefectCount()),
                            Integer.parseInt(defect.getFoundInInch()),
                            Integer.parseInt(defect.getPenaltyPoint()),
                            "yarn"
                    ));

                    int index = -1;
                    for (int i = 0; i < yarnDefectArrayList.size(); i++) {
                        if (yarnDefectArrayList.get(i).getDefectName().trim().equalsIgnoreCase(defect.getDefectName().trim())) {
                            index = i;
                            break;
                        }
                    }

                    if (index != -1) {
                        yarnDefectArrayList.get(index).setStatus(true);
                        yardDefectAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("KnittingDefect", "Defect not found: " + defect.getDefectName());
                    }
                }
            }
            setupFinishQCDefectRecyclerView();

            if (finishFabricDefectQCAdapter != null) {
                finishFabricDefectQCAdapter.notifyDataSetChanged();
            } else {
                Log.e("####", "Adapter is null. Initialization needed.");
            }
        }

        if (apiResponse != null
                && apiResponse.getData() != null
                && apiResponse.getData().getIndex() != null
                && apiResponse.getData().getIndex().getArrayRefData() != null
                && apiResponse.getData().getIndex().getArrayRefData().getDefect() != null
                && apiResponse.getData().getIndex().getArrayRefData().getDefect().getDyeing() != null
                && apiResponse.getData().getIndex().getArrayRefData().getDefect().getDyeing().size() > 0) {

            for (V1_BarcodeDetailsFromBatchFinishQCResponse.Dyeing defect : apiResponse.getData().getIndex().getArrayRefData().getDefect().getDyeing()) {
                if (!defect.getDefectCount().equals("0")
                        && !defect.getFoundInInch().equals("0")
                        && !defect.getPenaltyPoint().equals("0")) {

                    finishFabricQCDefectModelArrayList.add(new V1_FinishFabricQCDefectModel(
                            defect.getId(),
                            defect.getDefectName(),
                            Integer.parseInt(defect.getDefectCount()),
                            Integer.parseInt(defect.getFoundInInch()),
                            Integer.parseInt(defect.getPenaltyPoint()),
                            "dyeing"
                    ));

                    int index = -1;
                    for (int i = 0; i < dyeingDefectArrayList.size(); i++) {
                        if (dyeingDefectArrayList.get(i).getDefectName().trim().equalsIgnoreCase(defect.getDefectName().trim())) {
                            index = i;
                            break;
                        }
                    }

                    if (index != -1) {
                        dyeingDefectArrayList.get(index).setStatus(true);
                        dyeingDefectAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("KnittingDefect", "Defect not found: " + defect.getDefectName());
                    }
                }
            }
            setupFinishQCDefectRecyclerView();

            if (finishFabricDefectQCAdapter != null) {
                finishFabricDefectQCAdapter.notifyDataSetChanged();
            } else {
                Log.e("####", "Adapter is null. Initialization needed.");
            }
        }

        if (apiResponse != null
                && apiResponse.getData() != null
                && apiResponse.getData().getIndex() != null
                && apiResponse.getData().getIndex().getArrayRefData() != null
                && apiResponse.getData().getIndex().getArrayRefData().getDefect() != null
                && apiResponse.getData().getIndex().getArrayRefData().getDefect().getCommon() != null
                && apiResponse.getData().getIndex().getArrayRefData().getDefect().getCommon().size() > 0) {

            for (V1_BarcodeDetailsFromBatchFinishQCResponse.Common defect : apiResponse.getData().getIndex().getArrayRefData().getDefect().getCommon()) {
                if (!defect.getDefectCount().equals("0")
                        && !defect.getFoundInInch().equals("0")
                        && !defect.getPenaltyPoint().equals("0")) {

                    finishFabricQCDefectModelArrayList.add(new V1_FinishFabricQCDefectModel(
                            defect.getId(),
                            defect.getDefectName(),
                            Integer.parseInt(defect.getDefectCount()),
                            Integer.parseInt(defect.getFoundInInch()),
                            Integer.parseInt(defect.getPenaltyPoint()),
                            "common"
                    ));

                    int index = -1;
                    for (int i = 0; i < commonDefectArrayList.size(); i++) {
                        if (commonDefectArrayList.get(i).getDefectName().trim().equalsIgnoreCase(defect.getDefectName().trim())) {
                            index = i;
                            break;
                        }
                    }

                    if (index != -1) {
                        commonDefectArrayList.get(index).setStatus(true);
                        commonDefectAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("KnittingDefect", "Defect not found: " + defect.getDefectName());
                    }
                }
            }
            setupFinishQCDefectRecyclerView();

            if (finishFabricDefectQCAdapter != null) {
                finishFabricDefectQCAdapter.notifyDataSetChanged();
            } else {
                Log.e("####", "Adapter is null. Initialization needed.");
            }
        }

        if (apiResponse != null
                && apiResponse.getData() != null
                && apiResponse.getData().getIndex() != null
                && apiResponse.getData().getIndex().getArrayRefData() != null
                && apiResponse.getData().getIndex().getArrayRefData().getDefect() != null
                && apiResponse.getData().getIndex().getArrayRefData().getDefect().getFinishings() != null
                && apiResponse.getData().getIndex().getArrayRefData().getDefect().getFinishings().size() > 0) {

            for (V1_BarcodeDetailsFromBatchFinishQCResponse.Finishing defect : apiResponse.getData().getIndex().getArrayRefData().getDefect().getFinishings()) {
                if (!defect.getDefectCount().equals("0")
                        && !defect.getFoundInInch().equals("0")
                        && !defect.getPenaltyPoint().equals("0")) {

                    finishFabricQCDefectModelArrayList.add(new V1_FinishFabricQCDefectModel(
                            defect.getId(),
                            defect.getDefectName(),
                            Integer.parseInt(defect.getDefectCount()),
                            Integer.parseInt(defect.getFoundInInch()),
                            Integer.parseInt(defect.getPenaltyPoint()),
                            "finishing"
                    ));

                    int index = -1;
                    for (int i = 0; i < finishingDefectArrayList.size(); i++) {
                        if (finishingDefectArrayList.get(i).getDefectName().trim().equalsIgnoreCase(defect.getDefectName().trim())) {
                            index = i;
                            break;
                        }
                    }

                    if (index != -1) {
                        finishingDefectArrayList.get(index).setStatus(true);
                        finishingDefectAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("KnittingDefect", "Defect not found: " + defect.getDefectName());
                    }
                }
            }
            setupFinishQCDefectRecyclerView();

            if (finishFabricDefectQCAdapter != null) {
                finishFabricDefectQCAdapter.notifyDataSetChanged();
            } else {
                Log.e("####", "Adapter is null. Initialization needed.");
            }
        }

        if (apiResponse != null
                && apiResponse.getData() != null
                && apiResponse.getData().getIndex() != null
                && apiResponse.getData().getIndex().getArrayRefData() != null
                && apiResponse.getData().getIndex().getArrayRefData().getDefect() != null
                && apiResponse.getData().getIndex().getArrayRefData().getDefect().getAopprint() != null
                && apiResponse.getData().getIndex().getArrayRefData().getDefect().getAopprint().size() > 0) {

            for (V1_BarcodeDetailsFromBatchFinishQCResponse.AopPrint defect : apiResponse.getData().getIndex().getArrayRefData().getDefect().getAopprint()) {
                if (!defect.getDefectCount().equals("0")
                        && !defect.getFoundInInch().equals("0")
                        && !defect.getPenaltyPoint().equals("0")) {

                    finishFabricQCDefectModelArrayList.add(new V1_FinishFabricQCDefectModel(
                            defect.getId(),
                            defect.getDefectName(),
                            Integer.parseInt(defect.getDefectCount()),
                            Integer.parseInt(defect.getFoundInInch()),
                            Integer.parseInt(defect.getPenaltyPoint()),
                            "aop"
                    ));

                    int index = -1;
                    for (int i = 0; i < aopDefectArrayList.size(); i++) {
                        if (aopDefectArrayList.get(i).getDefectName().trim().equalsIgnoreCase(defect.getDefectName().trim())) {
                            index = i;
                            break;
                        }
                    }

                    if (index != -1) {
                        aopDefectArrayList.get(index).setStatus(true);
                        aopPrintDefectAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("KnittingDefect", "Defect not found: " + defect.getDefectName());
                    }
                }
            }
            setupFinishQCDefectRecyclerView();

            if (finishFabricDefectQCAdapter != null) {
                finishFabricDefectQCAdapter.notifyDataSetChanged();
            } else {
                Log.e("####", "Adapter is null. Initialization needed.");
            }
        }
    }

    @SuppressLint("DefaultLocale")
    public void calculationYds() {
        //double width_inch = Double.parseDouble(etRollWidthInch.getText().toString());
        String inch = _diaInchET.getText().toString().trim();
        String sum = _totalPointTV.getText().toString().trim();
        String rl = _ydsTV.getText().toString().trim();
        if(TextUtils.isEmpty(sum))
        {
            _totalPenaltyPointTV.setError("Fill the credential");
        }
        else if(TextUtils.isEmpty(inch))
        {
            _diaInchET.setError("Fill the credential");
        }
        else if(rl.equals("0.0"))
        {
            _ydsTV.setError("Fill the credential");
        }
        else {
            try {
                yds = ((Double.parseDouble(String.valueOf(fProductionQty))* 1000) / (Integer.parseInt(String.valueOf(fgsm)) * Double.parseDouble(inch)* 0.0254) * 1.09361);
                _ydsTV.setText((String.format("%.4f", yds)));

                Double totalPenalty = ((36 * 100 * Integer.parseInt(sum)) / (Double.parseDouble(inch)* yds));
                if(String.valueOf(totalPenalty).equals("Infinity"))
                {
                    _totalPenaltyPointTV.setText(String.valueOf(0));
                }
                else {
                    _totalPenaltyPointTV.setText(String.format("%.4f", totalPenalty));
                }

                int serial = 0;
                try {
                    serial = Integer.parseInt(_totalPointTV.getText().toString());
                } catch (NumberFormatException e) {
                    _fabricGradeTV.setText("");
                    _fabricGradeTV.setTextColor(Color.RED);
                    return;
                }

                boolean serialFound = false;
                for (int i = 0; i < gradeArrayList.size(); i++) {
                    if (Integer.parseInt(gradeArrayList.get(i).getSerial()) == serial) {
                        _fabricGradeTV.setText(gradeArrayList.get(i).getGrade());
                        serialFound = true;
                        break;
                    }
                }

                if (!serialFound) {
                    _fabricGradeTV.setText("Rejected");
                    _fabricGradeTV.setTextColor(Color.RED);
                }
            }catch (Exception e){
                Log.d(TAG, "calculationYds: ");
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fabric, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            if(finishFabricQCDefectModelArrayList.size() > 0){
                if(qcPassType != 0){
                    if(rollstatus == 0)
                    {
                        DialogHelper.showWarningDialog(this, "Message", "Roll status not selected.");
                    } else if(Integer.parseInt(_totalPointTV.getText().toString().trim()) < 1) {
                        DialogHelper.showWarningDialog(this, "Message", "Select defect name.");
                    } else {
                        postDataToServer();
                    }
                }else{
                    DialogHelper.showWarningDialog(this, "Message", "Please select major minor field.");
                }
            }else{
                DialogHelper.showWarningDialog(this, "Message", "Please add minimum one defect.");
            }
            return true;
        }
        else if (id == R.id.action_new){
            refreshData();
        } else if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void postDataToServer() {
        JSONObject save_obj = new JSONObject();
        JSONObject data_obj = new JSONObject();
        JSONObject index_obj = new JSONObject();
        JSONArray dtls_arr = new JSONArray();

        try {
            save_obj.put("status",true);
            save_obj.put("mode", barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getMode());
            save_obj.put("MST_ID", barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getMstId());
            save_obj.put("PROD_ID", barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getProdId());
            save_obj.put("TRANS_ID", barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getTransId());
            save_obj.put("DTLS_ID", barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getDtlsId());
            save_obj.put("QC_MST_ID", barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getQcMstId());
            save_obj.put("RECTIFY_NO", _rectifyTV.getText().toString());

            save_obj.put("UPDATE_ID", 0);

            index_obj.put("BARCODE_NO",barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getBarcodeNo());
            index_obj.put("BATCH_ID", barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getBatchId());
            index_obj.put("BATCH_NO", barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getBatchNo());
            index_obj.put("BODY_PART_ID", barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getBodyPartId());
            index_obj.put("BOOKING_NO", barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getBookingNo());
            index_obj.put("BOOKING_WITHOUT_ORDER", barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getBookingWithoutOrder());
            index_obj.put("COMPANY_ID", barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getCompanyId());
            index_obj.put("SERVICE_COMPANY", barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getServingCompany());
            index_obj.put("SOURCE", barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getSource());
            index_obj.put("SERVICE_LOCATION", barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getServiceLocation());
            index_obj.put("LOCATION", barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getLocation());
            index_obj.put("MACHINE_ID",0);
            index_obj.put("SHIFT",shiftId);
            index_obj.put("SHADE_ID",shadeId);
            index_obj.put("COLOR", barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getColor());
            index_obj.put("ROLL_WIDTH", _diaInchET.getText().toString());
            index_obj.put("ROLL_WEIGHT", barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getQcPassQty());
            index_obj.put("ROLL_LENGTH", _ydsTV.getText().toString());
            index_obj.put("CONS_COMP", barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getConstruction());
            index_obj.put("DETER_ID", barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getDeterD());
            index_obj.put("DIA", _diaInchET.getText().toString());
            index_obj.put("DIA_TYPE", barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getWidthDiaId());
            index_obj.put("GSM", barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getGsm());
            index_obj.put("FINISH_GSM", _gsmET.getText().toString());
            index_obj.put("IS_SALES_ID", barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getIsSales());
            index_obj.put("ORDER_ID", barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getPoBreakdownId());
//            index_obj.put("QC_PASS_QTY", _rollWeightKgTV.getText().toString());
            index_obj.put("QC_PASS_QTY", _proQntyET.getText().toString());
            index_obj.put("REJECT_QTY", _rejectQntyET.getText().toString().isEmpty() ? "0" : _rejectQntyET.getText().toString());
            index_obj.put("ROLL_ID", barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getRollId());
            index_obj.put("ROLL_NO", barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getRollNo());
            index_obj.put("ROLL_WGT", fProductionQty);
            index_obj.put("WGT_LOST","");
            index_obj.put("RECEIVE_DATE", currentDate);
            index_obj.put("COMMENTS", _commentEditField.getText().toString());
            index_obj.put("INSERTED_BY", userID);
            index_obj.put("UPDATED_BY", userID);
            index_obj.put("ROLL_STATUS", rollstatus);
            index_obj.put("TOTAL_PENALTY_POINT", _totalPenaltyPointTV.getText().toString());
            index_obj.put("TOTAL_POINT", _totalPointTV.getText().toString());
            index_obj.put("FABRIC_GRADE", _fabricGradeTV.getText().toString());
            index_obj.put("QC_PASS_TYPE", qcPassType);

            data_obj.put("index",index_obj);
            for (int i = 0; i < finishFabricQCDefectModelArrayList.size(); i++) {
                JSONObject dtls_obj = new JSONObject();
                dtls_obj.put("DEFECT_ID", finishFabricQCDefectModelArrayList.get(i).getDefectId());
                dtls_obj.put("COUNT", finishFabricQCDefectModelArrayList.get(i).getDefectCount());
                dtls_obj.put("INCH_ID", finishFabricQCDefectModelArrayList.get(i).getDefectFound());
                dtls_obj.put("PENALTY", finishFabricQCDefectModelArrayList.get(i).getDefectPenalty());
                dtls_arr.put(dtls_obj);
            }
            data_obj.put("list_data",dtls_arr);

            save_obj.put("data", data_obj);
            Log.d(TAG, "buidJsonObject: ############"+save_obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, save_obj.toString());

        progressBarState();
        finishProductionViewModel.postFinishFabricQCV2Response(body).observe(this, apiResponse -> {
            if(apiResponse != null) {
                Log.d(TAG, "fetchBarcodeDataByScaningBachNo: "+ apiResponse);
                try {
                    DialogHelper.showSuccessDialog(this, "Message", apiResponse.getResultset());
                    showWifiDialog(V2_FinishFabricQcEntryActivity.this, mBixolonLabelPrinter);
                    setFormForNewEntry();
                }catch (Exception e){
                    DialogHelper.showWarningDialog(this, "Message", "Please try again.");
                }
            } else{
                DialogHelper.showWarningDialog(this, "Message", "Something went wrong");
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setFormForNewEntry() {

        if(rollstatus == 1){
            barcodeByBatchIndexArrayList.remove(bundleSelectionPosition);
            batchWiseBarcodeForFinishQCAdapter.notifyDataSetChanged();
        }

        finishFabricQCDefectModelArrayList.clear();
        yarnDefectArrayList.clear();
        knittingDefectArrayList.clear();
        try {
            yardDefectAdapter.notifyDataSetChanged();
            knittingDefectAdapter.notifyDataSetChanged();
            finishFabricDefectQCAdapter.notifyDataSetChanged();
        }catch (Exception e) {
            Log.d(TAG, "setFormForNewEntry: ");
        }

        _buyerNameTV.setText("");
        _batchTV.setText("");
        _refNoTV.setText("");
        _fileNoTV.setText("");
        _cons_compositeTV.setText("");
        _rectifyTV.setText("");
        _barcodeNoTV.setText("");
        _diaInchET.setText("");
        _gsmET.setText("");
        _rollWeightKgTV.setText("");
        _ydsTV.setText("");
        _rejectQntyET.setText("0");
        _proQntyET.setText("");
        _weightLoss.setText("");
        _greyDiaTV.setText("");
        _greyGSMTV.setText("");
        _totalPenaltyPointTV.setText("");
        _totalPointTV.setText("");
        _fabricGradeTV.setText("");
        _commentEditField.setText("");
    }

    private void refreshData() {
        onBackPressed();
    }

    @SuppressLint("NotifyDataSetChanged")
    private  void  refreshUIForNew(int position) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Change this Page for new barcode?")
                .setContentText("Do you want to leave this page? Unsaved changes will not be available.")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener(sDialog -> {
                    for (int i = 0; i < barcodeByBatchIndexArrayList.size(); i++) {
                        barcodeByBatchIndexArrayList.get(i).setStatus(false);
                    }
                    barcodeByBatchIndexArrayList.get(position).setStatus(true);
                    batchWiseBarcodeForFinishQCAdapter.notifyDataSetChanged();
                    setFormForNewEntry();
                    fetchBarcodeDetailsData(barcodeByBatchIndexArrayList.get(position).getBarcodeNo());
                    sDialog.cancel();
                })
                .setCancelClickListener(SweetAlertDialog::cancel)
                .show();
    }

    void showWifiDialog(Context context, final BixolonLabelPrinter printer) {
        AlertDialog dialog = null;
        if (dialog == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.dialog_wifi, null);
            Spinner spinner =layout.findViewById(R.id.spinner_ip);
            spinner.setAdapter(mArrayAdapter);

            dialog = new AlertDialog.Builder(context).setView(layout).setTitle("Wi-Fi Connect")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
// EditText editText = (EditText) layout.findViewById(R.id.editText1);
// String ip = editText.getText().toString();

                            String ip = spinner.getSelectedItem().toString();

                            EditText editText = (EditText) layout.findViewById(R.id.editText2);
                            int port = Integer.parseInt(editText.getText().toString());

                            printer.connect(ip, port, 5000);
                            connectPrinter();

                        }
                    }).create();
        }
        dialog.show();
    }

    private void connectPrinter() {
        File file = new File(V2_FinishFabricQcEntryActivity.this.getFilesDir(), "text");
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM hh:mm aa");
            Date date1 = new Date(System.currentTimeMillis());
            String currentDate = formatter.format(date1);
            double qc;
            qc= Double.parseDouble(barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getQcPassQty());
            if (qc<0){
                qc= Double.parseDouble(barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getQnty());
            }

            String bodyPart = "";
            if (barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getBodyPart().length() > 13) {
                bodyPart = barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getBodyPart().substring(0, 13);
            }else{
                bodyPart = barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getBodyPart();
            }


            String s5 = "SW600\n" +
                    "SL1230,0,G\n" +
                    "SM20,20\n" +
                    "SOB\n" +
                    "\n" +
                    "B215,70,Q,2,M,6,0,'"+barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getBarcodeNo()+"' \n" +
                    "\n" +
                    "T160,70,2,1,1,0,0,N,N,'"+currentDate+"'\n" +
                    "T15,230,2,1,1,0,0,N,N,'"+"F-"+barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getFileNo()+",Ref-"+barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getRefNo()+", R.Roll: "+barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getRollNo()+" ,"+",'\n" +
                    "T15,265,2,1,1,0,0,N,N,'"+"R.Dia-"+barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getWidth()+",R.GSM-"+barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getGsm()+"," +bodyPart+",'\n" +
                    "T15,300,2,1,1,0,0,N,N,'"+barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getColor()+" ,"+barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getConstruction()+"'\n" +
                    "T160,105,2,1,1,0,0,N,N,'"+barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getBarcodeNo()+"'\n" +
                    "T160,10,4,1,1,0,0,N,N,'"+barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getBuyerName()+"'\n" +
                    "T160,140,2,1,1,0,0,N,N,'"+barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getBatchNo()+"'\n" +
                    "T360,130,4,1,1,0,0,N,N,'"+qc+"Kg'\n" +
                    "P1,1";

            Log.d("TAG", "connectPrinter: "+s5);
            File gpxfile = new File(file, "sample" + barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getBarcodeNo());
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(s5);
            writer.flush();
            writer.close();
//                    output.setText(readFile());

        } catch (Exception e) {
        }
    }

    private ArrayList<SpinnerModel> getList() {
        ArrayList<SpinnerModel> SpinnerModelArrayList = new ArrayList<>();
        try {
            SpinnerModel model2 = new SpinnerModel("192.168.0.51", 1);
            SpinnerModel model3 = new SpinnerModel("192.168.0.52", 2);
            SpinnerModel model4 = new SpinnerModel("192.168.0.53", 3);
            SpinnerModel model5 = new SpinnerModel("192.168.0.54", 4);
            SpinnerModel model6 = new SpinnerModel("192.168.0.55", 5);
            SpinnerModel model7 = new SpinnerModel("10.10.10.115", 6);
            SpinnerModel model8 = new SpinnerModel("192.168.11.165", 7);

            SpinnerModelArrayList.add(model2);
            SpinnerModelArrayList.add(model3);
            SpinnerModelArrayList.add(model4);
            SpinnerModelArrayList.add(model5);
            SpinnerModelArrayList.add(model6);
            SpinnerModelArrayList.add(model7);
            SpinnerModelArrayList.add(model8);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("An error occurred while initializing SpinnerModelArrayList: " + e.getMessage());
        }
        return SpinnerModelArrayList;
    }

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg)
        {
            try {
                switch (msg.what) {
                    case BixolonLabelPrinter.MESSAGE_STATE_CHANGE:
                        switch (msg.arg1) {
                            case BixolonLabelPrinter.STATE_CONNECTED:
                                Toast.makeText(V2_FinishFabricQcEntryActivity.this, "Device is connected", Toast.LENGTH_SHORT).show();
                                readFile();
                                break;
                            case BixolonLabelPrinter.STATE_CONNECTING:
                                Toast.makeText(V2_FinishFabricQcEntryActivity.this, "Device is connecting", Toast.LENGTH_SHORT).show();
                                break;
                            case BixolonLabelPrinter.STATE_NONE:
                                Toast.makeText(V2_FinishFabricQcEntryActivity.this, "connect is failed or disconnected", Toast.LENGTH_SHORT).show();
                                break;
                        }
                }
            } catch (Exception e) {
                Toast.makeText(V2_FinishFabricQcEntryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    };

    private void readFile() {
        try {
            File fileEvents = new File(V2_FinishFabricQcEntryActivity.this.getFilesDir() + "/text/sample" + barcodeDetailsFromBatchFinishQCResponse.getData().getIndex().getBarcodeNo());
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(fileEvents));
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                br.close();
            } catch (IOException e) {
            }
            String result = text.toString();
            mBixolonLabelPrinter.executeDirectIo(result, false, 0);
            fileEvents.delete();
            mBixolonLabelPrinter.disconnect();
        }catch (Exception e){
            Log.d(TAG, "readFile: "+e.getMessage());
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
                    startActivity(new Intent(this, V1_FinishFabricScannerDashboardActivity.class));
                    finish();
                })
                .setCancelClickListener(sDialog -> sDialog.cancel())
                .show();
    }


}