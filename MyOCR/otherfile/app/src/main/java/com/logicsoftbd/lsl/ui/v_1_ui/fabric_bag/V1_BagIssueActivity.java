package com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagIssueResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagReceiveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FabricBagColorModel;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_ScannerActivity;
import com.logicsoftbd.lsl.utils.Debouncer;
import com.logicsoftbd.lsl.utils.DialogHelper;
import com.logicsoftbd.lsl.viewModel.FinishProductionViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class V1_BagIssueActivity extends AppCompatActivity implements View.OnClickListener, V1_BagIssueRecyclerViewAdapter.OnRemoveHeadListener {
    private static final String TAG = "V1_BagKeepingActivity";
    private ProgressBar _progressBar;
    private Spinner _colorSpinner, _categorySpinner;
    private SimpleDateFormat simpleDateFormat;
    private RecyclerView _bagIssueRecyclerView;
    private TextView _rackScanTV, _challanScanTV;
    private EditText _bagNoScanET;
    private Button _rackScan, _bagNoScan, _issueDateBtn, _saveBtn, _refreshBtn, _challanScan;
    private ImageView _back;
    private CheckBox _checkBox;
    private String base_url, userID, userName, defectName, defectId, currentDate, bagNo, challanNo, fgsm, mode, barcodeScannedData, challanScan, rackId, roomRackScan, selectedColorName;
    private Integer scan_op = 0, selectedColorId = 0, selectedCategoryId = 0;
    private ArrayList<V1_BagIssueResponse.ResultSet> bagIssueArrayList = new ArrayList<>();
    private ArrayList<V1_BagIssueResponse.ResultSet> dataList = new ArrayList<>();
    private ArrayList<V1_FabricBagColorModel.ResultSet> fabricBagColorModel = new ArrayList<>();
    private V1_BagIssueRecyclerViewAdapter bagIssueRecyclerViewAdapter;
    private FinishProductionViewModel finishProductionViewModel;
    private final Debouncer debouncer = new Debouncer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_bag_issue);

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

    @SuppressLint("NotifyDataSetChanged")
    private void getDefaultData() {
        Intent intent = getIntent();
        barcodeScannedData = intent.getStringExtra("barcodeScan");
        scan_op = intent.getIntExtra("scan_op", 0);
        selectedCategoryId = intent.getIntExtra("selected_category", 0);
        selectedColorId = intent.getIntExtra("selected_color", 0);

        if(scan_op != 1){
            roomRackScan = intent.getStringExtra("room_rack_scan");
        }else {
            roomRackScan = intent.getStringExtra("barcodeScan");
        }

        Log.d(TAG, "getDefaultData: ------->"+barcodeScannedData+"  "+ roomRackScan+" "+ scan_op);

        dataList = (ArrayList<V1_BagIssueResponse.ResultSet>) intent.getSerializableExtra("bag_issue_data");

//        if(bagScan != null && scan_op == 2) {
//            _bagNoScanET.setText(bagScan);
//        }

        if(barcodeScannedData != null && scan_op == 2) {
            String[] bagScanArray = barcodeScannedData.split(Pattern.quote("***"));
            if (bagScanArray.length > 1) {
                _bagNoScanET.setText(bagScanArray[0]);
                bagNo = bagScanArray[0];
            } else {
                bagNo = barcodeScannedData;
                _bagNoScanET.setText(barcodeScannedData);
            }
        }

//        _challanScan.setText(challanScan);

        if(roomRackScan != null || scan_op == 3) {
            try {
                String[] roomRackArray = roomRackScan.split(Pattern.quote("***"));
                if(roomRackArray.length >= 1){
                    _rackScanTV.setText(String.valueOf(roomRackArray[1]));
                    rackId = roomRackArray[0];
                }else {
                    rackId = roomRackArray[0];
                    _rackScanTV.setText(String.valueOf(roomRackArray[0]));
                }
            }catch (Exception e){
                Log.d(TAG, "getDefaultData: ");
            }
        }

        if(barcodeScannedData != null && scan_op == 4) {
            String[] bagScanArray = barcodeScannedData.split(Pattern.quote("***"));
            if (bagScanArray.length > 1) {
                _challanScanTV.setText(bagScanArray[0]);
                challanNo = bagScanArray[0];
            } else {
                challanNo = barcodeScannedData;
                _challanScanTV.setText(barcodeScannedData);
            }
        }

        if(dataList != null){
            bagIssueArrayList = dataList;
        }
        setupIssueRecyclerView();
        bagIssueRecyclerViewAdapter.notifyDataSetChanged();

        if(scan_op == 2 && bagNo != null){
            fetchBagIssueData(bagNo, rackId);
        } else if(scan_op == 4 && challanNo != null){
            fetchBagIssueByChallanData(challanNo, rackId);
        }

        fetchFabricBagColorData();
        setCategorySpinnerAdapter();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchBagIssueData(String scannedData, String rackId) {
        progressBarState();
        finishProductionViewModel.getBagIssueResponse(scannedData, rackId, selectedColorId, selectedCategoryId).observe(this, apiResponse -> {
            if(apiResponse!= null){
                setFabricStoreBagIssueData(apiResponse);
//                _bagNoScanET.setText("");
//                if(apiResponse.getResultSet() != null) {
//
//                    try {
//                        boolean bagExists = false;
//                        apiResponse.getResultSet().setCategoryId(String.valueOf(selectedCategoryId));
//                        for (V1_BagIssueResponse.ResultSet item : bagIssueArrayList) {
//
//                            if (item.getBagNo().equals(apiResponse.getResultSet().getBagNo())) {
//                                DialogHelper.showWarningDialog(V1_BagIssueActivity.this, "Warning", "এই ব্যাগটি ইতিমধ্যেই স্ক্যান করা হয়েছে |");
//                                bagExists = true;
//                                break;
//                            }
//                        }
//
//                        if (!bagExists) {
//                            bagIssueArrayList.add(apiResponse.getResultSet());
//                        }
//
//                        bagIssueRecyclerViewAdapter.notifyDataSetChanged();
//                    }catch (Exception e){
//                        DialogHelper.showWarningDialog(this, "Warning", "Please try again.");
//                    }
//                }
//                else{
//                    DialogHelper.showWarningDialog(this, "Warning", apiResponse.getMsg());
//                    _bagNoScanET.setText("");
//                }
            }else{
                DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
            }
        });
    }

    private void setFabricStoreBagIssueData(V1_BagIssueResponse apiResponse) {
        if(bagIssueArrayList != null) {
            try {
                if(apiResponse.getStatusCode().equals("200")){
                    ArrayList<V1_BagIssueResponse.ResultSet> newBags = new ArrayList<>();

                    boolean singleBagInResponse = apiResponse.getResultSet().size() == 1;

                    for (V1_BagIssueResponse.ResultSet apiItem : apiResponse.getResultSet()) {
                        boolean bagExists = false;
                        apiItem.setBagColorId(String.valueOf(selectedColorId));
                        apiItem.setCategoryId(String.valueOf(selectedCategoryId));
                        apiItem.setChecked(false);
                        for (V1_BagIssueResponse.ResultSet item : bagIssueArrayList) {
                            if (item.getBagNo().equals(apiItem.getBagNo())) {
                                bagExists = true;
                                break;
                            }
                        }
                        if (!bagExists) {
                            newBags.add(apiItem);
                        } else if (singleBagInResponse) {
                            DialogHelper.showWarningDialog(
                                    V1_BagIssueActivity.this,
                                    "Warning",
                                    "এই ব্যাগটি ইতিমধ্যেই স্ক্যান করা হয়েছে |"
                            );
                        }
                    }

                    if (!newBags.isEmpty()) {
                        bagIssueArrayList.addAll(newBags);
                    } else if (!singleBagInResponse) {
                        DialogHelper.showWarningDialog(
                                V1_BagIssueActivity.this,
                                "Warning",
                                "সব ব্যাগ ইতিমধ্যে ব্যবহার করা হয়েছে |"
                        );
                    }

                    _bagNoScanET.setText("");
                    bagIssueRecyclerViewAdapter.notifyDataSetChanged();
                }else{
                    DialogHelper.showWarningDialog(
                            V1_BagIssueActivity.this,
                            "Warning",
                            apiResponse.getMsg()
                    );
                }

            } catch (Exception e){
                Log.d(TAG, "setFabricStoreBagIssueData: "+e.getMessage()+e);
                DialogHelper.showWarningDialog(
                        V1_BagIssueActivity.this,
                        "Warning",
                        "ডেটা পাওয়া যায়নি। অনুগ্রহ করে আবার চেষ্টা করুন।"
                );
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchBagIssueByChallanData(String scannedData, String rackId) {
        progressBarState();
        finishProductionViewModel.getBagIssueByChallanResponse(scannedData, rackId, selectedColorId, selectedCategoryId).observe(this, apiResponse -> {
            if(apiResponse!= null){
                setFabricStoreBagIssueData(apiResponse);
//                _bagNoScanET.setText("");
//                if(apiResponse.getResultSet() != null) {
//
//                    try {
//                        boolean bagExists = false;
//                        apiResponse.getResultSet().setCategoryId(String.valueOf(selectedCategoryId));
//                        for (V1_BagIssueResponse.ResultSet item : bagIssueArrayList) {
//
//                            if (item.getBagNo().equals(apiResponse.getResultSet().getBagNo())) {
//                                DialogHelper.showWarningDialog(V1_BagIssueActivity.this, "Warning", "এই ব্যাগটি ইতিমধ্যেই স্ক্যান করা হয়েছে |");
//                                bagExists = true;
//                                break;
//                            }
//                        }
//
//                        if (!bagExists) {
//                            bagIssueArrayList.add(apiResponse.getResultSet());
//                        }
//
//                        bagIssueRecyclerViewAdapter.notifyDataSetChanged();
//                    }catch (Exception e){
//                        DialogHelper.showWarningDialog(this, "Warning", "Please try again.");
//                    }
//                }
//                else{
//                    DialogHelper.showWarningDialog(this, "Warning", apiResponse.getMsg());
//                    _bagNoScanET.setText("");
//                }
            }else{
                DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
            }
        });
    }

    private void fetchFabricBagColorData() {
        progressBarState();
        finishProductionViewModel.getFabricBagColorResponse().observe(this, apiResponse -> {
            if(apiResponse != null) {
                try {
                    fabricBagColorModel.clear();
                    fabricBagColorModel = (ArrayList<V1_FabricBagColorModel.ResultSet>) apiResponse.getResultSet();
                    setColorSpinnerAdapter();
                }catch (Exception e) {
                    Log.d(TAG, "fetchFabricBagColorData: ");
                }

            }else{
                DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
            }
        });
    }

    private void setCategorySpinnerAdapter() {
        List<String> _categoryNames = new ArrayList<>();
        List<String> _categoryIds = new ArrayList<>();

        _categoryNames.add(0, "-Select-");
        _categoryNames.add(1, "Finish Fabric");
        _categoryNames.add(2, "Cutting");
        _categoryNames.add(3, "Sewing");
        _categoryNames.add(4, "AOP");

        _categoryIds.add(0, "0");
        _categoryIds.add(1, "1");
        _categoryIds.add(2, "2");
        _categoryIds.add(3, "3");
        _categoryIds.add(4, "4");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, _categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _categorySpinner.setAdapter(adapter);

        if(selectedCategoryId != null){
            _categorySpinner.setSelection(_categoryIds.indexOf(String.valueOf(selectedCategoryId)));
        }

        _categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategoryId = Integer.parseInt(_categoryIds.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setColorSpinnerAdapter() {
        List<String> _colorNames = new ArrayList<>();
        List<String> _colorIds = new ArrayList<>();
        for (V1_FabricBagColorModel.ResultSet item : fabricBagColorModel) {
            _colorNames.add(item.getColorName());
            _colorIds.add(String.valueOf(item.getColorId()));
        }
        _colorNames.add(0, "-Select-");
        _colorIds.add(0, "0");

        ArrayAdapter<String> aopAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, _colorNames);
        _colorSpinner.setAdapter(aopAdapter);

        if(selectedColorId != null){
            _colorSpinner.setSelection(_colorIds.indexOf(String.valueOf(selectedColorId)));
        }

        _colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedColorId = Integer.valueOf(_colorIds.get(position));
                selectedColorName = _colorNames.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupIssueRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _bagIssueRecyclerView.setLayoutManager(linearLayoutManager);
        bagIssueRecyclerViewAdapter = new V1_BagIssueRecyclerViewAdapter( bagIssueArrayList, this, this);
        _bagIssueRecyclerView.setAdapter(bagIssueRecyclerViewAdapter);
    }
    @SuppressLint({"SimpleDateFormat", "ClickableViewAccessibility", "NotifyDataSetChanged"})
    private void init_ui() {
        _progressBar = findViewById(R.id.progressBar);
        _colorSpinner = findViewById(R.id.colorSpinner);
        _categorySpinner = findViewById(R.id.categorySpinner);
        _bagIssueRecyclerView = findViewById(R.id.bagIssueRecyclerView);
        _checkBox = findViewById(R.id.checkBox);
        _rackScan = findViewById(R.id.rackScan);
        _rackScan.setOnClickListener(this);
        _bagNoScan = findViewById(R.id.bagNoScan);
        _bagNoScan.setOnClickListener(this);
        _challanScan = findViewById(R.id.challanScan);
        _challanScan.setOnClickListener(this);
        _issueDateBtn = findViewById(R.id.issueDateBtn);
        _issueDateBtn.setOnClickListener(this);
        _bagNoScanET = findViewById(R.id.bagNoScanET);
        _rackScanTV= findViewById(R.id.rackScanTV);
        _challanScanTV= findViewById(R.id.challanScanTV);

        _refreshBtn = findViewById(R.id.refreshBtn);
        _refreshBtn.setOnClickListener(this);
        _saveBtn = findViewById(R.id.saveBtn);
        _saveBtn.setOnClickListener(this);
        _back = findViewById(R.id.back);
        _back.setOnClickListener(this);

        simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = new Date(System.currentTimeMillis());
        String currentDate = simpleDateFormat.format(date);
        _issueDateBtn.setText(currentDate);

//        View rootLayout = findViewById(R.id.full_body);
//        rootLayout.setOnTouchListener((v, event) -> {
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                callBagAPI();
//            }
//            return false;
//        });

        _bagNoScanET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().trim();
                if (!input.isEmpty()) {
                    callBagAPI();
                } else {
                    debouncer.debounce(() -> {}, 0);
                }
            }
        });

        _checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                for(V1_BagIssueResponse.ResultSet item : bagIssueArrayList){
                    item.setChecked(true);
                }
            }else{
                for(V1_BagIssueResponse.ResultSet item : bagIssueArrayList){
                    item.setChecked(false);
                }
            }
            bagIssueRecyclerViewAdapter.notifyDataSetChanged();
        });
    }

    private void postDataToServer() {
        JSONObject save_obj = new JSONObject();
        JSONArray rfid_dtls_arr = new JSONArray();

        try {
            save_obj.put("STATUS", "true");
            save_obj.put("USER_ID", String.valueOf(userID));
            save_obj.put("COMPANY_ID", bagIssueArrayList.get(0).getCompanyId());
            save_obj.put("RCV_MST_ID", bagIssueArrayList.get(0).getId());
            save_obj.put("STORE_ID", bagIssueArrayList.get(0).getStoreId());
            save_obj.put("ROOM_ID", bagIssueArrayList.get(0).getRoomId());
            save_obj.put("RACK_ID", bagIssueArrayList.get(0).getRackId());
            save_obj.put("SHELF_ID", bagIssueArrayList.get(0).getShelfId());
            save_obj.put("BIN_ID", bagIssueArrayList.get(0).getBinId());

            for (int i = 0; i < bagIssueArrayList.size(); i++) {
                if(bagIssueArrayList.get(i).getChecked()){
                    JSONObject dtls_obj = new JSONObject();
                    dtls_obj.put("ID", String.valueOf(bagIssueArrayList.get(i).getBagKeepingId()));
                    dtls_obj.put("BAG_NO", String.valueOf(bagIssueArrayList.get(i).getBagNo()));
                    dtls_obj.put("QR_NO", String.valueOf(bagIssueArrayList.get(i).getQrNo()));
                    dtls_obj.put("ITEM_CATEGORY", String.valueOf(bagIssueArrayList.get(i).getItemCategory()));
                    dtls_obj.put("ROLL_QNTY", String.valueOf(bagIssueArrayList.get(i).getRollQnty()));
                    dtls_obj.put("WEIGHT", String.valueOf(bagIssueArrayList.get(i).getWeight()));
                    dtls_obj.put("CATEGORY_ID", String.valueOf(bagIssueArrayList.get(i).getCategoryId()));
                    rfid_dtls_arr.put(dtls_obj);
                }
            }

            save_obj.put("RFID_DTLS", rfid_dtls_arr);
            Log.d(TAG, "postDataToServer: ########" + save_obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, save_obj.toString());
        progressBarState();
        finishProductionViewModel.postBagIssueResponse(body).observe(this, apiResponse -> {
            if(apiResponse != null && apiResponse.getStatusCode() == 200) {
                DialogHelper.showSuccessDialog(this, "Success", apiResponse.getMsg());
                refreshData();
            }else{
                DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshData() {
        _bagNoScanET.setText("");
        _rackScanTV.setText("");
        _challanScanTV.setText("");
        bagIssueArrayList.clear();
        bagIssueRecyclerViewAdapter.notifyDataSetChanged();
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
                    overridePendingTransition(0, 0);
                })
                .setCancelClickListener(sDialog -> sDialog.cancel())
                .show();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onRemoveHeadClick(int position, View v) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Remove Data?")
                .setContentText("আপনি কি এই ব্যাগটি বাদ দিতে চান?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener(sDialog -> {
                    sDialog.cancel();
                    bagIssueArrayList.remove(position);
                    bagIssueRecyclerViewAdapter.notifyDataSetChanged();
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
            if(bagIssueArrayList.size() > 0){
                postDataToServer();
            }else{
                DialogHelper.showWarningDialog(this, "Warning", "অনুগ্রহ করে ন্যূনতম একটি ব্যাগ যোগ করুন।");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bagNoScan:
                String _rack = _rackScanTV.getText().toString().trim();
                if ( !_rack.isEmpty()) {
                    startScanning(2);
                } else {
                    DialogHelper.showWarningDialog(this, "Warning", "Please scan location first");
                }
                break;
            case R.id.challanScan:
                String _rack1 = _rackScanTV.getText().toString().trim();
                if ( !_rack1.isEmpty()) {
                    startScanning(4);
                } else {
                    DialogHelper.showWarningDialog(this, "Warning", "Please scan location first");
                }
                break;
            case R.id.rackScan:
                startScanning(1);
                break;
            case R.id.issueDateBtn:
                pickDate();
                break;
            case R.id.saveBtn:
                if(bagIssueArrayList.size() > 0){
                    postDataToServer();
                }else{
                    DialogHelper.showWarningDialog(this, "Warning", "অনুগ্রহ করে ন্যূনতম একটি ব্যাগ যোগ করুন।");
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

    private void pickDate(){
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String selectedDate = simpleDateFormat.format(calendar.getTime());
                    _issueDateBtn.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void callBagAPI() {
//        String _rack = _rackScanTV.getText().toString().trim();
//        String _bag = _bagNoScanET.getText().toString().trim();
//        if(!_rack.isEmpty() && !_bag.isEmpty() &&  selectedColorId != 0){
//            fetchBagIssueData(_bag, rackId);
//        } else {
//            DialogHelper.showWarningDialog(this, "Warning", "অনুগ্রহ করে রং এবং ব্যাগ নম্বর যোগ করুন |");
//        }

        debouncer.debounce(() -> {
            String _rack = _rackScanTV.getText().toString().trim();
            String _bag = _bagNoScanET.getText().toString().trim();
            if(!_rack.isEmpty() && !_bag.isEmpty() &&  selectedColorId != 0 && selectedCategoryId != 0){
                fetchBagIssueData(_bag, rackId);
            } else {
                runOnUiThread(() -> DialogHelper.showWarningDialog(this, "Warning", "অনুগ্রহ করে ক্যাটাগরি, রং এবং ব্যাগ নম্বর যোগ করুন |"));
                _bagNoScanET.setText("");
            }
        }, 2000);
    }

    private void startScanning(int op) {
        Intent intent = new Intent(this, V1_ScannerActivity.class);
        intent.putExtra("qc", "bag_issue");
        intent.putExtra("scan_op", op);
        intent.putExtra("bag_issue_data", bagIssueArrayList);
        intent.putExtra("room_rack_scan", roomRackScan);
        intent.putExtra("selected_category", selectedCategoryId);
        intent.putExtra("selected_color", selectedColorId);
        startActivity(intent);
        finish();
    }
}