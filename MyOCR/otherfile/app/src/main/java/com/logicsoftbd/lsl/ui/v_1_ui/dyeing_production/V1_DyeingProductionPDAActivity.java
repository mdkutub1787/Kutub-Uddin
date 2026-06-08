package com.logicsoftbd.lsl.ui.v_1_ui.dyeing_production;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DyeingProductionPDAResponse;
import com.logicsoftbd.lsl.ui.v_1_ui.finishing.V1_FinishingForPdaActivity;
import com.logicsoftbd.lsl.utils.DialogHelper;
import com.logicsoftbd.lsl.viewModel.FinishProductionViewModel;

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

public class V1_DyeingProductionPDAActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "V1_DyeingProductionPDAA";
    private static final String NO_SELECTION = "0";
    private ProgressBar _progressBar;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private ImageView _back;
    private SimpleDateFormat simpleDateFormat, simpleTimeFormat;
    private TextView _batchScanTV, _machineScanTV, _extensionNoTV, _extensionTitleNoTV, _startDateTV, _startTimeTV,
            _endDateTV, _endTimeTV, _totalRollTV, _batchWeightTV, _multiBatchTitleTV,
            _batchNoTV, _resultArrTitleTV, _dyeingTypeTitleTV, _processTitleTV, _btbLtbTitleTV, _fabricTypeTitleTV, _shiftTitleTV;
    private Button _loadBT, _batchScanBT, _machineScanBT;
    private Spinner _processSpinner, _resultArrSpinner, _multiBatchSpinner, _dyeingTypeSpinner, _btbLtbSpinner, _fabricTypeSpinner, _shiftIdSpinner;
    private String base_url, userID, userName, currentDate, currentTime;
    private String machineId = NO_SELECTION, entryFormId = NO_SELECTION, processId = NO_SELECTION,
            resultId = NO_SELECTION, multiBatchId = NO_SELECTION, dyeingTypeId = NO_SELECTION, btbLtbId = NO_SELECTION;
    private String currentIdentifier;
    private V1_DyeingProductionPDAResponse dyeingProductionPDAResponse = new V1_DyeingProductionPDAResponse();
    private FinishProductionViewModel finishProductionViewModel;
    private boolean isLoadMode = true;


    @SuppressLint({"SetTextI18n", "UnspecifiedRegisterReceiverFlag"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_dyeing_production_pdaactivity);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        base_url = (_preferences.getString("base_url", ""));
        userID = _preferences.getString("login_userid", "");
        userName = _preferences.getString("login_username", "");
        finishProductionViewModel = new ViewModelProvider(this).get(FinishProductionViewModel.class);

        init_ui();

        IntentFilter filter = new IntentFilter("com.logicsoftbd.lsl.SCAN");
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(barcodeReceiver, filter);
    }

    private void fetchBatchDataResponse(String batchNo) {
        progressBarState();
        finishProductionViewModel.getDyeingProductionPDAResponse(batchNo).observe(this, apiResponse -> {
            try {
                if (apiResponse == null || apiResponse.getResultSet() == null) {
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
                if (apiResponse.getResultSet() == null) {
                    DialogHelper.showErrorDialog(this, "Error", "No data found for this batch.");
                    clearData();
                    return;
                }
                dyeingProductionPDAResponse = apiResponse;
                setSpinnerData(apiResponse);
                setData(apiResponse);

            } catch (Exception e) {
                Log.e(TAG, "fetchBatchDataResponse: Error occurred", e);
                DialogHelper.showErrorDialog(this, "Error", "An unexpected error occurred.");
            } finally {
                progressBarGone();
            }
        });
    }

    private void setSpinnerData(V1_DyeingProductionPDAResponse apiResponse) {
        setProcessSpinner(apiResponse.getResultSet().getProcess());
        setBtbLtbSpinner(apiResponse.getResultSet().getBtbLtb());
        setMultiBatchSpinner(apiResponse.getResultSet().getMultiBatch());
        setResultArrSpinner(apiResponse.getResultSet().getResultArr());
        setDyeingTypeSpinner(apiResponse.getResultSet().getDyeingType());
    }

    private void setDyeingTypeSpinner(List<V1_DyeingProductionPDAResponse.DyeingType> dyeingTypeList) {
        List<String> dyeingTypeNames = new ArrayList<>();
        List<String> dyeingTypeIds = new ArrayList<>();

        dyeingTypeNames.add("--Select--");
        dyeingTypeIds.add(NO_SELECTION);

        for (V1_DyeingProductionPDAResponse.DyeingType dyeing : dyeingTypeList) {
            dyeingTypeNames.add(dyeing.getName());
            dyeingTypeIds.add(String.valueOf(dyeing.getId()));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dyeingTypeNames);
        _dyeingTypeSpinner.setAdapter(adapter);
        _dyeingTypeSpinner.setSelection(dyeingTypeIds.indexOf(dyeingTypeId));

        _dyeingTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dyeingTypeId = dyeingTypeIds.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setResultArrSpinner(List<V1_DyeingProductionPDAResponse.ResultArr> resultArrList) {
        List<String> resultArrNames = new ArrayList<>();
        List<String> resultArrIds = new ArrayList<>();

        resultArrNames.add("--Select--");
        resultArrIds.add(NO_SELECTION);

        for (V1_DyeingProductionPDAResponse.ResultArr resultArr : resultArrList) {
            resultArrNames.add(resultArr.getName());
            resultArrIds.add(String.valueOf(resultArr.getId()));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, resultArrNames);
        _resultArrSpinner.setAdapter(adapter);
        _resultArrSpinner.setSelection(resultArrIds.indexOf(resultId));

        _resultArrSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                resultId = resultArrIds.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setMultiBatchSpinner(List<V1_DyeingProductionPDAResponse.MultiBatch> multiBatchList) {
        List<String> multiBatchNames = new ArrayList<>();
        List<String> multiBatchIds = new ArrayList<>();

        multiBatchNames.add("--Select--");
        multiBatchIds.add(NO_SELECTION);

        for (V1_DyeingProductionPDAResponse.MultiBatch multiBatch : multiBatchList) {
            multiBatchNames.add(multiBatch.getName());
            multiBatchIds.add(String.valueOf(multiBatch.getId()));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, multiBatchNames);
        _multiBatchSpinner.setAdapter(adapter);
        _multiBatchSpinner.setSelection(multiBatchIds.indexOf(multiBatchId));

        _multiBatchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                multiBatchId = multiBatchIds.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setBtbLtbSpinner(List<V1_DyeingProductionPDAResponse.BtbLtb> btbLtbList) {
        List<String> btbLtbNames = new ArrayList<>();
        List<String> btbLtbIds = new ArrayList<>();

        btbLtbNames.add("--Select--");
        btbLtbIds.add(NO_SELECTION);

        for (V1_DyeingProductionPDAResponse.BtbLtb btbLtb : btbLtbList) {
            btbLtbNames.add(btbLtb.getName());
            btbLtbIds.add(String.valueOf(btbLtb.getId()));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, btbLtbNames);
        _btbLtbSpinner.setAdapter(adapter);
        _btbLtbSpinner.setSelection(btbLtbIds.indexOf(btbLtbId));

        _btbLtbSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                btbLtbId = btbLtbIds.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setProcessSpinner(List<V1_DyeingProductionPDAResponse.Process> processList) {
        List<String> processNames = new ArrayList<>();
        List<String> processIds = new ArrayList<>();

        processNames.add("--Select--");
        processIds.add(NO_SELECTION);

        for (V1_DyeingProductionPDAResponse.Process process : processList) {
            processNames.add(process.getName());
            processIds.add(String.valueOf(process.getId()));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, processNames);
        _processSpinner.setAdapter(adapter);
        _processSpinner.setSelection(processIds.indexOf(processId));

        _processSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                processId = processIds.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setData(V1_DyeingProductionPDAResponse apiResponse) {
        Log.d(TAG, "setData: Mode from API: " + apiResponse.getResultSet().getMode());
        Log.d(TAG, "setData: Before setting, isLoadMode: " + isLoadMode);

        _extensionNoTV.setText(String.valueOf(apiResponse.getResultSet().getExtensionNo()));
        _totalRollTV.setText(String.valueOf(apiResponse.getResultSet().getTotalRoll()));
        _batchWeightTV.setText(String.valueOf(apiResponse.getResultSet().getBatchWeight()));

        isLoadMode = !apiResponse.getResultSet().getMode().equalsIgnoreCase("Unload");

        if (!isLoadMode) {
            Log.d(TAG, "setData: Entering Unload Mode Logic");
            _startDateTV.setEnabled(false);
            _startTimeTV.setEnabled(false);
            _endDateTV.setText(currentDate);
            _endTimeTV.setText(currentTime);
            _startDateTV.setText(String.valueOf(apiResponse.getResultSet().getStartDate()));
            _startTimeTV.setText(apiResponse.getResultSet().getStartHour() + ":" + apiResponse.getResultSet().getStartMinute());

            _resultArrTitleTV.setVisibility(View.VISIBLE);
            _resultArrSpinner.setVisibility(View.VISIBLE);
            _extensionTitleNoTV.setVisibility(View.VISIBLE);
            _extensionNoTV.setVisibility(View.VISIBLE);

            _processTitleTV.setVisibility(View.GONE);
            _processSpinner.setVisibility(View.GONE);
            _multiBatchTitleTV.setVisibility(View.GONE);
            _multiBatchSpinner.setVisibility(View.GONE);
            _dyeingTypeTitleTV.setVisibility(View.GONE);
            _dyeingTypeSpinner.setVisibility(View.GONE);
            _btbLtbTitleTV.setVisibility(View.GONE);
            _btbLtbSpinner.setVisibility(View.GONE);
            _loadBT.setText("Unload");

        } else {
            Log.d(TAG, "setData: Entering Load Mode Logic");
            _startDateTV.setEnabled(true);
            _startTimeTV.setEnabled(true);
            _endDateTV.setText(currentDate);
            _endTimeTV.setText(currentTime);
            _startDateTV.setText(currentDate);
            _startTimeTV.setText(currentTime);

            _processTitleTV.setVisibility(View.VISIBLE);
            _processSpinner.setVisibility(View.VISIBLE);
            _multiBatchTitleTV.setVisibility(View.VISIBLE);
            _multiBatchSpinner.setVisibility(View.VISIBLE);
            _dyeingTypeTitleTV.setVisibility(View.VISIBLE);
            _dyeingTypeSpinner.setVisibility(View.VISIBLE);
            _btbLtbTitleTV.setVisibility(View.VISIBLE);
            _btbLtbSpinner.setVisibility(View.VISIBLE);

            _resultArrTitleTV.setVisibility(View.GONE);
            _resultArrSpinner.setVisibility(View.GONE);
            _extensionNoTV.setVisibility(View.GONE);
            _extensionTitleNoTV.setVisibility(View.GONE);
            _loadBT.setText("Load");
        }

        Log.d(TAG, "setData: After setting, isLoadMode: " + isLoadMode);
    }


    private void saveRequestObject() {
        Log.d(TAG, "saveRequestObject: Starting save request...");
        Log.d(TAG, "saveRequestObject: isLoadMode: " + isLoadMode);

        try {
            JSONObject saveObj = new JSONObject();
            saveObj.put("STATUS", true);
            saveObj.put("MODE", isLoadMode ? "Load" : "Unload");
            saveObj.put("USER_ID", userID);
            saveObj.put("MACHINE_ID", machineId);
            saveObj.put("DYEING_TYPE", dyeingTypeId);
            saveObj.put("BTB_LTB", btbLtbId);
            saveObj.put("PROCESS_ID", processId);
            saveObj.put("MULTI_BATCH", multiBatchId);
            saveObj.put("FABRIC_TYPE", "");
            saveObj.put("SHIFT", "");

            Integer batchId = dyeingProductionPDAResponse.getResultSet().getBatchId();
            if (batchId != null) {
                saveObj.put("BATCH_ID", batchId);
            } else {
                Log.e(TAG, "saveRequestObject: Batch ID is null!");
                saveObj.put("BATCH_ID", 0);
            }

            Integer totalRoll = dyeingProductionPDAResponse.getResultSet().getTotalRoll();
            if (totalRoll != null) {
                saveObj.put("TOTAL_ROLL", totalRoll);
            } else {
                Log.e(TAG, "saveRequestObject: Total Roll is null!");
                saveObj.put("TOTAL_ROLL", 0);
            }

            Integer batchWeight = dyeingProductionPDAResponse.getResultSet().getBatchWeight();
            if (batchWeight != null) {
                saveObj.put("BATCH_WEIGHT", batchWeight);
            } else {
                Log.e(TAG, "saveRequestObject: Batch Weight is null!");
                saveObj.put("BATCH_WEIGHT", 0);
            }

            if (!isLoadMode) {
                Log.d(TAG, "saveRequestObject: resultId: " + resultId);
                saveObj.put("RESULT_ARR", resultId);
            }

            Log.d(TAG, "saveRequestDyeingObject: saveObj: " + saveObj.toString());
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), saveObj.toString());

//            finishProductionViewModel.postDyeingProductionPDAResponse(body).observe(this, apiResponse -> {
//                Log.d(TAG, "saveRequestObject: API Response: " + apiResponse);
//                if (apiResponse != null && apiResponse.getStatus()) {
//                    DialogHelper.showSuccessDialog(this, "Success", apiResponse.getMsg());
//                    isLoadMode = !isLoadMode;
//                    Log.d(TAG, "saveRequestObject: isLoadMode after saving: " + isLoadMode);
//                    clearData();
//                } else {
//                    DialogHelper.showErrorDialog(this, "Error", "Failed to save data!");
//                }
//            });

        } catch (JSONException e) {
            Log.e(TAG, "saveRequestObject: JSON Error", e);
            DialogHelper.showErrorDialog(V1_DyeingProductionPDAActivity.this, "JSON Error", "Error creating the JSON object.");
        } finally {
            progressBarGone();
        }
    }

    private void clearData() {
        _batchScanTV.setText("");
        _machineScanTV.setText("");
        _startDateTV.setText(currentDate);
        _startTimeTV.setText(currentTime);
        _endDateTV.setText("");
        _endTimeTV.setText("");
        _totalRollTV.setText("");
        _batchWeightTV.setText("");

        isLoadMode = true;
        dyeingProductionPDAResponse = null;
        processId = NO_SELECTION;
        multiBatchId = NO_SELECTION;
        resultId = NO_SELECTION;
        dyeingTypeId = NO_SELECTION;
        btbLtbId = NO_SELECTION;

        _processSpinner.setSelection(0);
        _multiBatchSpinner.setSelection(0);
        _dyeingTypeSpinner.setSelection(0);
        _btbLtbSpinner.setSelection(0);
        _resultArrSpinner.setSelection(0);

        _processTitleTV.setVisibility(View.GONE);
        _processSpinner.setVisibility(View.GONE);
        _multiBatchTitleTV.setVisibility(View.GONE);
        _multiBatchSpinner.setVisibility(View.GONE);
        _dyeingTypeTitleTV.setVisibility(View.GONE);
        _dyeingTypeSpinner.setVisibility(View.GONE);
        _btbLtbTitleTV.setVisibility(View.GONE);
        _btbLtbSpinner.setVisibility(View.GONE);
        _resultArrTitleTV.setVisibility(View.GONE);
        _resultArrSpinner.setVisibility(View.GONE);
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
        _loadBT = findViewById(R.id.loadBT);
        _loadBT.setOnClickListener(this);
        _machineScanBT = findViewById(R.id.machineScanBT);
        _machineScanBT.setOnClickListener(this);
        _batchScanBT = findViewById(R.id.batchScanBT);
        _batchScanBT.setOnClickListener(this);
        _batchScanTV = findViewById(R.id.batchScanTV);
        _machineScanTV = findViewById(R.id.machineScanTV);
        _extensionNoTV = findViewById(R.id.extensionNoTV);
        _extensionTitleNoTV = findViewById(R.id.extensionTitleNoTV);
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
        _batchNoTV = findViewById(R.id.batchNoTV);
        _processSpinner = findViewById(R.id.processDyeSpinner);
        _processTitleTV = findViewById(R.id.processTitleTV);
        _resultArrSpinner = findViewById(R.id.resultArrSpinner);
        _resultArrTitleTV = findViewById(R.id.resultArrTitleTV);
        _multiBatchSpinner = findViewById(R.id.multiBatchSpinner);
        _multiBatchTitleTV = findViewById(R.id.multiBatchTitleTV);
        _dyeingTypeSpinner = findViewById(R.id.dyeingType_Spinner);
        _dyeingTypeTitleTV = findViewById(R.id.dyeingTypeTitleTV);
        _btbLtbSpinner = findViewById(R.id.btb_ltbSpinner);
        _btbLtbTitleTV = findViewById(R.id.btbLtbTitleTV);
        _fabricTypeTitleTV = findViewById(R.id.fabricTypeTitleTV);
        _fabricTypeSpinner = findViewById(R.id.fabricTypeSpinner);
        _shiftTitleTV = findViewById(R.id.shiftTitleTV);
        _shiftIdSpinner = findViewById(R.id.shiftIdSpinner);
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
        progressBarState();
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
                if (isLoadMode) {
                    datetimepicker(_startDateTV);
                } else {
                    DialogHelper.showWarningDialog(this, "Warning", "Load date and time cannot be changed in Unload mode.");
                }
                break;
            case R.id.startTimeTV:
                if (isLoadMode) {
                    timePicker(_startTimeTV);
                } else {
                    DialogHelper.showWarningDialog(this, "Warning", "Load date and time cannot be changed in Unload mode.");
                }
                break;
            case R.id.endDateTV:
                datetimepicker(_endDateTV);
                break;
            case R.id.endTimeTV:
                timePicker(_endTimeTV);
                break;
            case R.id.loadBT:
                if (dyeingProductionPDAResponse != null && dyeingProductionPDAResponse.getResultSet() != null) {
                    if (!isLoadMode) {
                        if (!_batchScanTV.getText().toString().isEmpty() && !_machineScanTV.getText().toString().isEmpty()) {
                            saveRequestObject();
                        } else {
                            DialogHelper.showWarningDialog(this, "Warning", "Please scan Batch and Machine No.");
                        }
                    } else {
                        saveRequestObject();
                    }
                } else {
                    DialogHelper.showWarningDialog(this, "Warning", "Bundle data not found. Please scan bundle first.");
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
            DialogHelper.showWarningDialog(V1_DyeingProductionPDAActivity.this, "Message", "Invalid Machine.");
            _machineScanBT.setBackgroundColor(Color.parseColor("#324F5C"));
            return;
        }

        String machineIdPart = roomRackArray[0].replaceAll("[^0-9]", "");
        String machineNoPart = roomRackArray[1].replace("Machine No:", "").trim();

        if (machineIdPart.isEmpty() || machineNoPart.isEmpty()) {
            DialogHelper.showWarningDialog(V1_DyeingProductionPDAActivity.this, "Message", "Invalid Machine Data.");
            _machineScanBT.setBackgroundColor(Color.parseColor("#324F5C"));
            return;
        }

        machineId = String.valueOf(Integer.valueOf(machineIdPart));
        Log.d(TAG, "Machine ID: " + machineId);
        _machineScanTV.setText(machineNoPart);
        _machineScanBT.setBackgroundColor(Color.parseColor("#324F5C"));
    }

    private void processBatchBarcode(String barcodeData) {
        if (barcodeData != null && !barcodeData.trim().isEmpty()) {
            _batchScanTV.setText(barcodeData);
            _batchScanBT.setBackgroundColor(Color.parseColor("#324F5C"));
            fetchBatchDataResponse(barcodeData);
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