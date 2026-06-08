package com.logicsoftbd.lsl.ui.v_1_ui.finishing;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FinishingDataResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SpecialFinishSubProcessResponse;
import com.logicsoftbd.lsl.utils.DialogHelper;
import com.logicsoftbd.lsl.viewModel.FinishProductionViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class V1_FinishingForPdaActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "V1_FinishingForPdaActiv";
    private ProgressBar _progressBar;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private ImageView _back;
    private SimpleDateFormat simpleDateFormat, simpleTimeFormat;
    private TextView _batchScanTV, _machineScanTV, _extentionNoTV, _startDateTV, _startTimeTV, _endDateTV, _endTimeTV, _totalRollTV, _batchWeightTV, _subProcessTitleTV;
    private Button _startBT, _batchScanBT, _machineScanBT;
    private Spinner _processSpinner, _subProcessSpinner;
    private String base_url, userID, userName, currentDate, currentTime, scannedBarcode;
    private Integer processId = 0, machine_id = 0, subProcessId = 0;
    private static final int PROCESS_ID_DEFAULT = 0;
    private static final int PROCESS_ID_STENTERING = 48;
    private static final int PROCESS_ID_SLITTING_SQUEEZING = 30;
    private static final int PROCESS_ID_COMPACTING = 33;
    private static final int PROCESS_ID_SPECIAL_FINISH = 34;
    private String currentIdentifier;
    private V1_FinishingDataResponse finishingDataResponse;
    private V1_SpecialFinishSubProcessResponse specialFinishSubProcessResponse = new V1_SpecialFinishSubProcessResponse();
    private FinishProductionViewModel finishProductionViewModel;
    private boolean isSpecialFinishSelected = false;
    private boolean isStartMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_finishing_for_pda);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        base_url = (_preferences.getString("base_url", ""));
        userID = _preferences.getString("login_userid", "");
        userName = _preferences.getString("login_username", "");
        finishProductionViewModel = new ViewModelProvider(this).get(FinishProductionViewModel.class);

        init_ui();
        setupProcessSpinner();

        IntentFilter filter = new IntentFilter("com.logicsoftbd.lsl.SCAN");
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(barcodeReceiver, filter);
        finishingDataResponse = null;
    }

    private void setupProcessSpinner() {
        List<String> _processNames = new ArrayList<>();
        List<Integer> _processIds = new ArrayList<>();

        _processNames.add("--Select--");
        _processNames.add("Stentering");
        _processNames.add("Slitting/Squeezing");
        _processNames.add("Compacting");
        _processNames.add("Special Finish");
        _processIds.add(0);
        _processIds.add(48);
        _processIds.add(30);
        _processIds.add(33);
        _processIds.add(34);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, _processNames);
        _processSpinner.setAdapter(adapter);

        int index = _processIds.indexOf(processId);
        if (index != -1) {
            _processSpinner.setSelection(index);
        } else {
            _processSpinner.setSelection(0);
        }

        _processSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                processId = _processIds.get(position);

                if (processId == PROCESS_ID_SPECIAL_FINISH) {
                    isSpecialFinishSelected = true;
                    _subProcessSpinner.setVisibility(View.VISIBLE);
                    _subProcessTitleTV.setVisibility(View.VISIBLE);
                    fetchSubProcessData();
                } else {
                    isSpecialFinishSelected = false;
                    _subProcessSpinner.setVisibility(View.GONE);
                    _subProcessTitleTV.setVisibility(View.GONE);
                    subProcessId = 0;
                }
                if (processId != 0) {
                    fetchBatchDataResponse(processId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fetchBatchDataResponse(Integer processId) {
        progressBarState();
        finishProductionViewModel.getFinishingDataResponse(_batchScanTV.getText().toString(), String.valueOf(processId)).observe(this, apiResponse -> {
            try {
                if (apiResponse == null || apiResponse.getResultset() == null) {
                    String errorMessage = (apiResponse != null) ? apiResponse.getMsg() : "Something went wrong.";
                    DialogHelper.showErrorDialog(this, "Error", errorMessage);
                    clearData();
                    return;
                }

                if (!apiResponse.getStatus()) {
                    DialogHelper.showErrorDialog(this, "Error", apiResponse.getMsg());
                    clearData();
                    return;
                }

                if (apiResponse.getResultset() == null) {
                    DialogHelper.showErrorDialog(this, "Error", "No data found for this batch.");
                    clearData();
                    return;
                }

                setData(apiResponse);
                finishingDataResponse = apiResponse;

            } catch (Exception e) {
                Log.e(TAG, "fetchBatchDataResponse: Error occurred", e);
                DialogHelper.showErrorDialog(this, "Error", "An unexpected error occurred.");
                clearData();
            }
            progressBarGone();
        });
    }

    private void fetchSubProcessData() {
        progressBarState();
        finishProductionViewModel.getSpecialFinishSubProcessList().observe(this, apiResponse -> {
            try {
                if (apiResponse == null || apiResponse.getResultSet() == null || !apiResponse.getStatus()) {
                    String errorMessage = (apiResponse != null) ? apiResponse.toString() : "Something went wrong.";
                    Toast.makeText(V1_FinishingForPdaActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    return;
                }
                specialFinishSubProcessResponse = apiResponse;
                populateSubProcessSpinner(apiResponse.getResultSet());

            } catch (Exception e) {
                Log.e(TAG, "fetchSubProcessData: Error occurred", e);
                Toast.makeText(V1_FinishingForPdaActivity.this, "An unexpected error occurred.", Toast.LENGTH_SHORT).show();
            }
            progressBarGone();
        });
    }

    private void populateSubProcessSpinner(List<V1_SpecialFinishSubProcessResponse.SubProcess> subProcessList) {
        List<String> subProcessNames = new ArrayList<>();
        List<Integer> subProcessIds = new ArrayList<>();
        subProcessNames.add("--Select--");
        subProcessIds.add(0);

        for (V1_SpecialFinishSubProcessResponse.SubProcess subProcess : subProcessList) {
            subProcessNames.add(subProcess.getProcessName());
            subProcessIds.add(subProcess.getId());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subProcessNames);
        _subProcessSpinner.setAdapter(adapter);

        _subProcessSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subProcessId = subProcessIds.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void setData(V1_FinishingDataResponse apiResponse) {
        _extentionNoTV.setText(apiResponse.getResultset().getReferenceIndex().getExtentionNo());
        _totalRollTV.setText(String.valueOf(apiResponse.getResultset().getDtlsIndex().size()));
        double totalWeight = 0.0;
        for (V1_FinishingDataResponse.DtlsIndex index : apiResponse.getResultset().getDtlsIndex()) {
            totalWeight += Double.parseDouble(index.getProdQty());
        }
        _batchWeightTV.setText(String.valueOf(totalWeight));

        _startBT.setText(apiResponse.getMode().equalsIgnoreCase("end") ? "End" : "Start");
        isStartMode = !apiResponse.getMode().equalsIgnoreCase("end");

        if ("end".equals(apiResponse.getMode())) {
            _startDateTV.setEnabled(false);
            _startTimeTV.setEnabled(false);
            _endDateTV.setText(currentDate);
            _endTimeTV.setText(currentTime);

            if (apiResponse.getResultset().getReferenceIndex() != null) {
                _startDateTV.setText(apiResponse.getResultset().getStartDate());
                _startTimeTV.setText(apiResponse.getResultset().getStartHour() + ":" + apiResponse.getResultset().getStartMinute());
            } else {
                Log.e(TAG, "ReferenceIndex missing.");
                DialogHelper.showErrorDialog(this, "Error", "ReferenceIndex missing.");
                _startDateTV.setText("");
                _startTimeTV.setText("");
            }

            V1_FinishingDataResponse.InputAreaIndex inputAreaIndex = apiResponse.getResultset().getInputAreaIndex();
            if (inputAreaIndex != null) {
                String mName = inputAreaIndex.getMachineName();
                if (mName != null && !mName.trim().isEmpty()) {
                    _machineScanTV.setText(mName);
                } else {
                    _machineScanTV.setText("");
                }
            } else {
                Log.e(TAG, "InputAreaIndex missing.");
                DialogHelper.showErrorDialog(this, "Error", "InputAreaIndex missing.");
            }
        } else {
            _startDateTV.setEnabled(true);
            _startTimeTV.setEnabled(true);
            _endDateTV.setText(currentDate);
            _endTimeTV.setText(currentTime);
            _startDateTV.setText(currentDate);
            _startTimeTV.setText(currentTime);
        }
    }

    private void saveRequestObject() throws JSONException {
        JSONObject save_obj = new JSONObject();
        JSONObject data_obj = new JSONObject();
        JSONObject index_obj = new JSONObject();
        JSONArray dtls_arr = new JSONArray();

        save_obj.put("status", true);
        save_obj.put("mode", finishingDataResponse.getMode());
        save_obj.put("MST_ID", finishingDataResponse.getResultset().getDtlsIndex().get(0).getMstId());
        save_obj.put("UPDATE_ID", "0");

        index_obj.put("BATCH_ID", finishingDataResponse.getResultset().getInputAreaIndex().getBatchId());
        index_obj.put("BATCH_NO", finishingDataResponse.getResultset().getInputAreaIndex().getBatchNo());
        index_obj.put("TRIMS_WGT", finishingDataResponse.getResultset().getInputAreaIndex().getTrimsWgt());
        index_obj.put("COMPANY_ID", finishingDataResponse.getResultset().getInputAreaIndex().getCompanyId());
        index_obj.put("SERVICE_COMPANY", finishingDataResponse.getResultset().getInputAreaIndex().getServiceCompany());
        index_obj.put("ENTRY_FORM_NO", finishingDataResponse.getResultset().getInputAreaIndex().getEntryFormNo());
        index_obj.put("RE_SLITING_NO", finishingDataResponse.getResultset().getInputAreaIndex().getReSlitingNo());
        index_obj.put("PRODUCTION_TYPE", "");
        index_obj.put("PROCESS_ID", processId);
        index_obj.put("NEXT_PROCESS_ID", "");
        index_obj.put("RESULT", "");
        index_obj.put("PRODUCTION_DATE", currentDate);
        index_obj.put("PROCESS_START_DATE", _startDateTV.getText().toString());
        index_obj.put("PROCESS_END_DATE", _endDateTV.getText().toString());
        index_obj.put("START_HOURS", _startTimeTV.getText().toString());
        index_obj.put("START_MINUTES", _startTimeTV.getText().toString());
        index_obj.put("END_HOURS", _endTimeTV.getText().toString());
        index_obj.put("END_MINUTES", _endTimeTV.getText().toString());
        index_obj.put("SHIFT_NAME", "");
        index_obj.put("ADVANCED_PROD_QTY", "");
        index_obj.put("FLOOR", "");
        index_obj.put("MACHINE_NAME", _machineScanTV.getText().toString());
        index_obj.put("MACHINE_ID", machine_id);
        index_obj.put("WIDTH_SHRINKAGE", "");
        index_obj.put("LENGTH_SHRINKAGE", "");
        index_obj.put("PINNING", "");
        index_obj.put("FEED_IN", "");
        index_obj.put("STRETCH", "");
        index_obj.put("SPEED", "");
        index_obj.put("STEAM", "");
        index_obj.put("OVER_FEED", "");
        index_obj.put("TEMPARATURE", "");
        index_obj.put("CHEMICAL_NAME", "");
        index_obj.put("IS_RE_DYEING", "");
        index_obj.put("REMARK", "");
        index_obj.put("USER_ID", userID);
        index_obj.put("SUB_PROCESS_ID", (processId == PROCESS_ID_SPECIAL_FINISH) ? subProcessId : 0);

        for (int i = 0; i < finishingDataResponse.getResultset().getDtlsIndex().size(); i++) {
            JSONObject dtls_obj = new JSONObject();
            dtls_obj.put("PROD_ID", finishingDataResponse.getResultset().getDtlsIndex().get(i).getProdId());
            dtls_obj.put("FIN_DIA", finishingDataResponse.getResultset().getDtlsIndex().get(i).getFinDia());
            dtls_obj.put("ROLL_NO", finishingDataResponse.getResultset().getDtlsIndex().get(i).getRollNo());
            dtls_obj.put("ROLL_ID", finishingDataResponse.getResultset().getDtlsIndex().get(i).getRollId());
            dtls_obj.put("NO_OF_ROLL", finishingDataResponse.getResultset().getDtlsIndex().get(i).getNoOfRoll());
            dtls_obj.put("BATCH_QNTY", finishingDataResponse.getResultset().getDtlsIndex().get(i).getBatchQnty());
            dtls_obj.put("PROD_QTY", finishingDataResponse.getResultset().getDtlsIndex().get(i).getProdQty());
            dtls_obj.put("BARCODE_NO", finishingDataResponse.getResultset().getDtlsIndex().get(i).getBarcodeNo());
            dtls_obj.put("DIA_TYPE", finishingDataResponse.getResultset().getDtlsIndex().get(i).getDiaType());
            dtls_obj.put("DIA_WIDTH", finishingDataResponse.getResultset().getDtlsIndex().get(i).getDiaWidth());
            dtls_obj.put("GSM", finishingDataResponse.getResultset().getDtlsIndex().get(i).getGsm());
            dtls_obj.put("CONS_COMP", finishingDataResponse.getResultset().getDtlsIndex().get(i).getConsComp());
            dtls_arr.put(dtls_obj);
        }

        data_obj.put("index", index_obj);
        data_obj.put("list_data", dtls_arr);
        save_obj.put("data", data_obj);

        Log.d(TAG, "saveRequestDyeingObject: ######## " + save_obj);

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, save_obj.toString());

        JSONArray listDataArray = data_obj.getJSONArray("list_data");

        if (listDataArray.length() == 0) {
            DialogHelper.showWarningDialog(V1_FinishingForPdaActivity.this, "Message", "Please scan bundle before data saving..");
        } else {
            progressBarState();
            finishProductionViewModel.postFinishingResponse(body).observe(this, apiResponse -> {
                if (apiResponse != null) {
                    try {
                        if (apiResponse.getStatus()) {
                            DialogHelper.showSuccessDialog(V1_FinishingForPdaActivity.this, "Success", apiResponse.getResultset().getSaveMsg());
                            clearData();
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "saveRequestObject: " + e.getMessage() + e);
                        DialogHelper.showWarningDialog(V1_FinishingForPdaActivity.this, "Warning", "An unexpected error occurred.");
                    }
                } else {
                    DialogHelper.showErrorDialog(V1_FinishingForPdaActivity.this, "Error", "Something wrong happen!");
                }
                progressBarGone();
            });
        }
    }

    private void clearData() {
        _batchScanTV.setText("");
        _machineScanTV.setText("");
        _extentionNoTV.setText("");
        _startDateTV.setText(currentDate);
        _startTimeTV.setText(currentTime);
        _endDateTV.setText("");
        _endTimeTV.setText("");
        _totalRollTV.setText("");
        _batchWeightTV.setText("");
        isStartMode = true;
        finishingDataResponse = null;
        processId = 0;
        subProcessId = 0;
        _processSpinner.setSelection(0);
        _subProcessSpinner.setSelection(0);
        _subProcessSpinner.setVisibility(View.GONE);
        _subProcessTitleTV.setVisibility(View.GONE);
    }


    @SuppressLint("SetTextI18n")
    private void datetimepicker(final TextView sedate) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear,
                 dayOfMonth) -> sedate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1),
                year, month, day);
        datePickerDialog.show();
    }

    private void timePicker(TextView processStartTime) {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this,
                (timePicker, selectedHour,
                 selectedMinute) -> processStartTime.setText(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)),
                hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    @SuppressLint("SimpleDateFormat")
    private void init_ui() {
        _progressBar = findViewById(R.id.progressBar);
        _startBT = findViewById(R.id.startBT);
        _startBT.setOnClickListener(this);
        _machineScanBT = findViewById(R.id.machineScanBT);
        _machineScanBT.setOnClickListener(this);
        _batchScanBT = findViewById(R.id.batchScanBT);
        _batchScanBT.setOnClickListener(this);
        _batchScanTV = findViewById(R.id.batchScanTV);
        _machineScanTV = findViewById(R.id.machineScanTV);
        _extentionNoTV = findViewById(R.id.extentionNoTV);
        _startDateTV = findViewById(R.id.startDateTV);
        _startDateTV.setFocusable(false);
        _startDateTV.setClickable(false);
        _startTimeTV = findViewById(R.id.startTimeTV);
        _startTimeTV.setFocusable(false);
        _startTimeTV.setClickable(false);
        _endDateTV = findViewById(R.id.endDateTV);
        _endDateTV.setFocusable(false);
        _endDateTV.setClickable(false);
        _endTimeTV = findViewById(R.id.endTimeTV);
        _endTimeTV.setFocusable(false);
        _endTimeTV.setClickable(false);
        _totalRollTV = findViewById(R.id.totalRollTV);
        _batchWeightTV = findViewById(R.id.batchWeightTV);
        _processSpinner = findViewById(R.id.processSpinner);
        _subProcessSpinner = findViewById(R.id.subProcessSpinner);
        _subProcessTitleTV = findViewById(R.id.subProcessTitleTV);
        _back = findViewById(R.id.back);
        _back.setOnClickListener(this);

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        simpleTimeFormat = new SimpleDateFormat("HH:mm");
        currentDate = simpleDateFormat.format(calendar.getTime());
        currentTime = simpleTimeFormat.format(calendar.getTime());
        _startDateTV.setText(currentDate);
        _startTimeTV.setText(currentTime);
        _endDateTV.setText("");
        _endTimeTV.setText("");
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

    private void progressBarGone() {
        _progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.batchScanBT:
                if (_batchScanTV.getText().toString().isEmpty()) {
                    currentIdentifier = "batch";
                    _batchScanBT.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
                    _machineScanBT.setBackgroundColor(Color.parseColor("#324F5C"));
                } else
                    DialogHelper.showWarningDialog(this, "Warning", "Please Refresh Batch No first.");
                break;
            case R.id.machineScanBT:
                String ro_rac = _batchScanTV.getText().toString().trim();
                if (!ro_rac.isEmpty()) {
                    currentIdentifier = "machine";
                    _machineScanBT.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
                    _batchScanBT.setBackgroundColor(Color.parseColor("#324F5C"));
                } else {
                    DialogHelper.showWarningDialog(this, "Warning", "Please scan Batch first.");
                }
                break;
            case R.id.startDateTV:
                if (isStartMode) {
                    datetimepicker(_startDateTV);
                } else {
                    DialogHelper.showWarningDialog(this, "Warning", "Start date and time cannot be changed in End mode.");
                }
                break;
            case R.id.startTimeTV:
                if (isStartMode) {
                    timePicker(_startTimeTV);
                } else {
                    DialogHelper.showWarningDialog(this, "Warning", "Start date and time cannot be changed in End mode.");
                }
                break;
            case R.id.endDateTV:
                datetimepicker(_endDateTV);
                break;
            case R.id.endTimeTV:
                timePicker(_endTimeTV);
                break;
            case R.id.startBT:
                try {
                    if (finishingDataResponse != null && finishingDataResponse.getResultset() != null
                            && finishingDataResponse.getResultset().getDtlsIndex() != null &&
                            finishingDataResponse.getResultset().getDtlsIndex().size() > 0) {
                        if (!isStartMode) {
                            if (!_batchScanTV.getText().toString().isEmpty() && !_machineScanTV.getText().toString().isEmpty()) {
                                saveRequestObject();
                            } else {
                                DialogHelper.showWarningDialog(this, "Warning", "Please scan Batch and Machine no.");
                            }
                        } else {
                            saveRequestObject();
                        }
                    } else {
                        DialogHelper.showWarningDialog(this, "Warning", "Bundle data not found. Please scan bundle first.");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "save data json exception" + e);
                    DialogHelper.showWarningDialog(this, "Warning", "An error occurred when saving data");
                }
                break;
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    private void processMachineBarcode(String barcodeData) {
        String[] roomRackArray = barcodeData.split(Pattern.quote("**"));

        if (roomRackArray.length < 2) {
            DialogHelper.showWarningDialog(V1_FinishingForPdaActivity.this, "Message", "Invalid Machine.");
            _machineScanBT.setBackgroundColor(Color.parseColor("#324F5C"));
            return;
        }

        String machineIdPart = roomRackArray[0].replaceAll("[^0-9]", "");
        String machineNoPart = roomRackArray[1].replace("Machine No:", "").trim();

        if (machineIdPart.isEmpty() || machineNoPart.isEmpty()) {
            DialogHelper.showWarningDialog(V1_FinishingForPdaActivity.this, "Message", "Invalid Machine Data.");
            _machineScanBT.setBackgroundColor(Color.parseColor("#324F5C"));
            return;
        }

        machine_id = Integer.valueOf(machineIdPart);
        Log.d(TAG, "Machine ID: " + machine_id);
        _machineScanTV.setText(machineNoPart);
        _machineScanBT.setBackgroundColor(Color.parseColor("#324F5C"));
    }

    private void processBatchBarcode(String barcodeData) {
        if (barcodeData != null && !barcodeData.trim().isEmpty()) {
            _batchScanTV.setText(barcodeData);
            _batchScanBT.setBackgroundColor(Color.parseColor("#324F5C"));
            fetchBatchDataResponse(processId);
        } else {
            DialogHelper.showWarningDialog(this, "Warning", "Invalid Batch Barcode");
            _batchScanTV.setText("");
        }
    }

    private BroadcastReceiver barcodeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Intent Received");

            if (intent.hasExtra("com.symbol.datawedge.data_string")) {
                String barcodeData = intent.getStringExtra("com.symbol.datawedge.data_string");

                if (barcodeData == null || barcodeData.trim().isEmpty()) {
                    Log.d(TAG, "No barcode data received.");
                    return;
                }

                if (currentIdentifier != null && currentIdentifier.equals("batch")) {
                    processBatchBarcode(barcodeData);
                } else if (currentIdentifier != null && currentIdentifier.equals("machine")) {
                    processMachineBarcode(barcodeData);
                } else {
                    Log.d(TAG, "Barcode data : " + barcodeData);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(barcodeReceiver);
    }
}