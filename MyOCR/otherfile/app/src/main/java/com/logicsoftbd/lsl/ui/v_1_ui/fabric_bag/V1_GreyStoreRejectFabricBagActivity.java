package com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.logicsoftbd.lsl.data.network.v1_model.V1_AOPDepartmentStoreResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagKeepingDataBySystemResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FabricBagColorModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyStoreRejectBagReceiveResponse;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_ScannerActivity;
import com.logicsoftbd.lsl.utils.Debouncer;
import com.logicsoftbd.lsl.utils.DialogHelper;
import com.logicsoftbd.lsl.viewModel.FinishProductionViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class V1_GreyStoreRejectFabricBagActivity extends AppCompatActivity implements View.OnClickListener, V1_GreyStoreRejectFabricBagRecyclerViewAdapter.OnRemoveHeadListener{
    private static final String TAG = "V1_GreyStoreRejectFabri";
    private SharedPreferences _preferences;
    private ProgressBar _progressBar;
    private Spinner _colorSpinner;
    private RecyclerView _bagKeepingRecyclerView;
    private Button _bagNoScan, _systemNoScan, _saveBtn, _refreshBtn;
    private ImageView _back;
    private Spinner _aopSpinner;
    private TextView _systemScanTV;
    private EditText _bagNoScanET;
    private V1_GreyStoreRejectFabricBagRecyclerViewAdapter aopDeptReceiveRecyclerViewAdapter;
    private String base_url, userID, userName, savedPrinter, defectName, defectId, currentDate, fgsm, mode, bagScan, bagNo, batchScan, rollWeight, noOfRoll, responseSystemNumber, selectedColorName;
    private Integer scan_op = 0, selectedColorId = 0;
    private String storeIdSelectedId, storeIdSelectedName;
    private ArrayList<V1_GreyStoreRejectBagReceiveResponse.ResultSet> dataList = new ArrayList<>();
    private ArrayList<V1_GreyStoreRejectBagReceiveResponse.ResultSet> greyStoreRejectBag = new ArrayList<>();
    private List<String> storeName = new ArrayList<>();
    private List<String> storeId = new ArrayList<>();
    private ArrayList<V1_FabricBagColorModel.ResultSet> fabricBagColorModel = new ArrayList<>();
    private FinishProductionViewModel finishProductionViewModel;
    private final Debouncer debouncer = new Debouncer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_grey_store_reject_bag);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        base_url = (_preferences.getString("base_url", ""));
        userID = _preferences.getString("login_userid", "");
        userName = _preferences.getString("login_username", "");
        savedPrinter = _preferences.getString("saved_printer", "");
        finishProductionViewModel = new ViewModelProvider(this).get(FinishProductionViewModel.class);

        init_ui();
        getDefaultData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getDefaultData() {
        Intent intent = getIntent();
        bagScan = intent.getStringExtra("barcodeScan");
        scan_op = intent.getIntExtra("scan_op", 0);
        if(scan_op != 1){
            batchScan = intent.getStringExtra("barcodeScan");
            bagScan = intent.getStringExtra("bagScan");

        }else {
            bagScan = intent.getStringExtra("barcodeScan");
            batchScan = intent.getStringExtra("batch_scan");
        }
        dataList = (ArrayList<V1_GreyStoreRejectBagReceiveResponse.ResultSet>) intent.getSerializableExtra("grey_store_reject_bag_rcv_data");


        if(bagScan != null && scan_op == 1) {
            String[] bagScanArray = bagScan.split(Pattern.quote("***"));
            if (bagScanArray.length > 1) {
                _bagNoScanET.setText(bagScanArray[0]);
                bagNo = bagScanArray[0];
            } else {
                bagNo = bagScan;
                _bagNoScanET.setText(bagScan);
            }
        }
//        _bagNoScanET.setText(bagScan);
        _systemScanTV.setText(batchScan);

        if(dataList != null){
            greyStoreRejectBag = dataList;
        }
        setupRecyclerView();
        aopDeptReceiveRecyclerViewAdapter.notifyDataSetChanged();

        if(scan_op == 1 && bagNo != null && batchScan != null){
            fetchAopDeptRcvData( bagNo);
        }else if(scan_op == 2 && batchScan != null){
            fetchAopDeptRcvBySystemNoData( batchScan);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = new Date(System.currentTimeMillis());
        currentDate = formatter.format(date);

        fetchFabricBagColorData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchAopDeptRcvData(String bagScan) {
        progressBarState();
        finishProductionViewModel.getGreyStoreRejectBagResponse(bagScan, selectedColorId).observe(this, apiResponse -> {
            if(apiResponse!= null){
                _bagNoScanET.setText("");
                if(apiResponse.getResultSet() != null) {
                    try {
                        setBagKeepingAdapterData(apiResponse);
                    }catch (Exception e){
                        Log.d(TAG, "fetchBagKeepingData: "+e.getMessage());
                        DialogHelper.showWarningDialog(this, "Warning", "Please try again.");
                    }
                }
                else{
                    DialogHelper.showWarningDialog(this, "Warning", apiResponse.getMsg());
                }
            }else{
                DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchAopDeptRcvBySystemNoData(String batchScan) {
        progressBarState();
        finishProductionViewModel.getGreyStoreRejectBagBySystemNoResponse(batchScan).observe(this, apiResponse -> {
            if(apiResponse!= null){
                _bagNoScanET.setText("");
                if(apiResponse.getResultSet() != null) {
                    try {
                        setBagKeepingAdapterData(apiResponse);
                    }catch (Exception e){
                        Log.d(TAG, "fetchBagKeepingData: "+e.getMessage());
                        DialogHelper.showWarningDialog(this, "Warning", "Please try again.");
                    }
                }
                else{
                    DialogHelper.showWarningDialog(this, "Warning", apiResponse.getMsg());
                }
            }else{
                DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
            }
        });
    }


    @SuppressLint("NotifyDataSetChanged")
    private void setBagKeepingAdapterData(V1_GreyStoreRejectBagReceiveResponse apiResponse) {
//        try {
//            boolean bagExists = false;
//
//            for (V1_GreyStoreRejectBagReceiveResponse.ResultSet item : greyStoreRejectBag) {
//                if (item.getBagNo().equals(_bagNoScanET.getText().toString())) {
//                    DialogHelper.showWarningDialog(V1_GreyStoreRejectFabricBagActivity.this, "Warning", "This bag ("+ _bagNoScanET.getText().toString() +") is already in used.");
//                    _bagNoScanET.setText("");
//                    bagExists = true;
//                    break;
//                }
//            }
//
//            if (!bagExists) {
//                greyStoreRejectBag.add(apiResponse.getResultSet().get(0));
//                _bagNoScanET.setText("");
//                fetchAOPDeptStoreData(greyStoreRejectBag.get(greyStoreRejectBag
//                        .size() - 1).getCompanyId());
//            }
//            aopDeptReceiveRecyclerViewAdapter.notifyDataSetChanged();
//        }catch (Exception e){
//            Log.d(TAG, "setBagKeepingAdapterData: "+e.getMessage());
//        }

        if(greyStoreRejectBag != null) {
            ArrayList<V1_GreyStoreRejectBagReceiveResponse.ResultSet> newBags = new ArrayList<>();

            boolean singleBagInResponse = apiResponse.getResultSet().size() == 1;

            for (V1_GreyStoreRejectBagReceiveResponse.ResultSet apiItem : apiResponse.getResultSet()) {
                boolean bagExists = false;
                apiItem.setBagColorId(String.valueOf(selectedColorId));
                for (V1_GreyStoreRejectBagReceiveResponse.ResultSet item : greyStoreRejectBag) {
                    if (item.getBagNo().equals(apiItem.getBagNo())) {
                        bagExists = true;
                        break;
                    }
                }
                if (!bagExists) {
                    newBags.add(apiItem);
                } else if (singleBagInResponse) {
                    DialogHelper.showWarningDialog(
                            V1_GreyStoreRejectFabricBagActivity.this,
                            "Warning",
                            "এই ব্যাগটি ইতিমধ্যেই স্ক্যান করা হয়েছে |"
                    );
                }
            }


            if (!newBags.isEmpty()) {
                greyStoreRejectBag.addAll(newBags);
            } else if (!singleBagInResponse) {
                DialogHelper.showWarningDialog(
                        V1_GreyStoreRejectFabricBagActivity.this,
                        "Warning",
                        "সব ব্যাগ ইতিমধ্যে ব্যবহার করা হয় |"
                );
            }
            fetchAOPDeptStoreData(greyStoreRejectBag.get(greyStoreRejectBag.size() - 1).getCompanyId());
            _bagNoScanET.setText("");
            _systemScanTV.setText("");
            aopDeptReceiveRecyclerViewAdapter.notifyDataSetChanged();
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchAOPDeptStoreData(String companyId) {
        progressBarState();
        finishProductionViewModel.getAOPDeptStoreResponse(userID, companyId, "2").observe(this, apiResponse -> {
            if(apiResponse!= null){
                if(apiResponse.getData() != null) {
                    try {
                        setUpAOPDeptStore(apiResponse);
                    }catch (Exception e){
                        Log.d(TAG, "fetchBagKeepingData: "+e.getMessage());
                        DialogHelper.showWarningDialog(this, "Warning", "Please try again.");
                    }
                }
                else{
                    DialogHelper.showWarningDialog(this, "Warning", apiResponse.getMsg());
                }
            }else{
                DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
            }
        });
    }

    private void setUpAOPDeptStore(V1_AOPDepartmentStoreResponse apiResponse) {
        storeId.clear();
        storeName.clear();
        for (V1_AOPDepartmentStoreResponse.Datum item : apiResponse.getData()) {
            storeName.add(item.getStoreName());
            storeId.add(item.getStoreId());
        }

        storeName.add(0, "-Select-");
        storeId.add(0, "0");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, storeName);
        _aopSpinner.setAdapter(adapter);

        _aopSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                storeIdSelectedId = storeId.get(position);
                storeIdSelectedName = storeName.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        _colorSpinner.setSelection(_colorIds.indexOf(String.valueOf(selectedColorId)));
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

    private void postDataToServer() {
        JSONObject save_obj = new JSONObject();
        JSONArray rfid_dtls_arr = new JSONArray();

        try {
            save_obj.put("STATUS", "true");
            save_obj.put("USER_ID", String.valueOf(userID));
            save_obj.put("BATCH_NO", String.valueOf(greyStoreRejectBag.get(0).getBatchNo()));
            save_obj.put("BUYER_ID", String.valueOf(greyStoreRejectBag.get(0).getBuyerId()));
            save_obj.put("COMPANY_ID", String.valueOf(greyStoreRejectBag.get(0).getCompanyId()));
            save_obj.put("STORE_ID", String.valueOf(storeIdSelectedId));
            save_obj.put("STORE_NAME", String.valueOf(storeIdSelectedName));

            for (int i = 0; i < greyStoreRejectBag.size(); i++) {
                JSONObject dtls_obj = new JSONObject();
                dtls_obj.put("BAG_NO", String.valueOf(greyStoreRejectBag.get(i).getBagNo()));
                dtls_obj.put("RFID_NO", String.valueOf(greyStoreRejectBag.get(i).getRfidNo()));
                dtls_obj.put("QR_NO", String.valueOf(greyStoreRejectBag.get(i).getQrNo()));
                dtls_obj.put("BATCH_NO", String.valueOf(greyStoreRejectBag.get(i).getBatchNo()));
                dtls_obj.put("WEIGHT", String.valueOf(greyStoreRejectBag.get(i).getWeight()));
                dtls_obj.put("INTERNAL_REF", String.valueOf(greyStoreRejectBag.get(i).getInternalRef()));
                dtls_obj.put("BUYER_ID", String.valueOf(greyStoreRejectBag.get(i).getBuyerId()));
                dtls_obj.put("ROLL_QNTY", String.valueOf(greyStoreRejectBag.get(i).getRollQnty()));
                dtls_obj.put("COMPANY_ID", String.valueOf(greyStoreRejectBag.get(i).getCompanyId()));
                dtls_obj.put("FABRIC_COLOR_ID", greyStoreRejectBag.get(i).getFabColorId());
                dtls_obj.put("BAG_KEEPING_MST_ID", greyStoreRejectBag.get(i).getBagKeepingMstId());
                dtls_obj.put("BAG_KEEPING_DETAILS_ID", greyStoreRejectBag.get(i).getBagKeepingDetailsId());
                dtls_obj.put("SYSTEM_NO", greyStoreRejectBag.get(i).getSystemNo());
                dtls_obj.put("COLOR_ID", greyStoreRejectBag.get(i).getFabColorId());
                dtls_obj.put("ROLL_QNTY", greyStoreRejectBag.get(i).getRollQnty());
                dtls_obj.put("DIA", greyStoreRejectBag.get(i).getDia());
                dtls_obj.put("GSM", greyStoreRejectBag.get(i).getGsm());
                dtls_obj.put("AOP", greyStoreRejectBag.get(i).getAop());
                dtls_obj.put("REJECT", greyStoreRejectBag.get(i).getReject());
                dtls_obj.put("QC_DONE", greyStoreRejectBag.get(i).getQcDone());
                dtls_obj.put("FABRIC_TYPE", greyStoreRejectBag.get(i).getFabricType());
                rfid_dtls_arr.put(dtls_obj);
            }

            save_obj.put("RFID_DTLS", rfid_dtls_arr);
            Log.d(TAG, "postDataToServer: ########" + save_obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, save_obj.toString());
        progressBarState();
        finishProductionViewModel.postGreyStoreRejectBagResponse(body).observe(this, apiResponse -> {
            if(apiResponse != null) {
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
        _systemScanTV.setText("");

        _aopSpinner.setSelection(0);
        greyStoreRejectBag.clear();
        aopDeptReceiveRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _bagKeepingRecyclerView.setLayoutManager(linearLayoutManager);
        aopDeptReceiveRecyclerViewAdapter = new V1_GreyStoreRejectFabricBagRecyclerViewAdapter( greyStoreRejectBag, this, this);
        _bagKeepingRecyclerView.setAdapter(aopDeptReceiveRecyclerViewAdapter);
    }
    @SuppressLint("ClickableViewAccessibility")
    private void init_ui() {
        _progressBar = findViewById(R.id.progressBar);
        _colorSpinner = findViewById(R.id.colorSpinner);
        _bagKeepingRecyclerView = findViewById(R.id.bagKeepingRecyclerView);
        _bagNoScan = findViewById(R.id.bagNoScan);
        _bagNoScan.setOnClickListener(this);
        _systemNoScan = findViewById(R.id.systemScan);
        _systemNoScan.setOnClickListener(this);
        _bagNoScanET = findViewById(R.id.bagNoScanET);
        _systemScanTV = findViewById(R.id.systemScanTV);
        _refreshBtn = findViewById(R.id.refreshBtn);
        _refreshBtn.setOnClickListener(this);
        _saveBtn = findViewById(R.id.saveBtn);
        _saveBtn.setOnClickListener(this);
        _back = findViewById(R.id.back);
        _back.setOnClickListener(this);
        _aopSpinner = findViewById(R.id.aopSpinner);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.systemScan:
                startScanning(2);
                break;
            case R.id.bagNoScan:
                startScanning(1);
                break;
            case R.id.saveBtn:
                try {
                    if(greyStoreRejectBag.size() > 0){
                        if(!storeIdSelectedName.equals("-Select-")){
                            postDataToServer();
                        }else{
                            DialogHelper.showWarningDialog(this, "Warning", "Please select store.");
                        }
                    }else{
                        DialogHelper.showWarningDialog(this, "Warning", "অনুগ্রহ করে ন্যূনতম একটি ব্যাগ যোগ করুন।");
                    }
                }catch (Exception e){
                    Log.d(TAG, "onClick: "+e.getMessage());
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
                    greyStoreRejectBag.remove(position);
                    aopDeptReceiveRecyclerViewAdapter.notifyDataSetChanged();
                })
                .setCancelClickListener(SweetAlertDialog::cancel)
                .show();
    }

    private void callBagAPI() {
//        String _bag = _bagNoScanET.getText().toString().trim();
//        if(!_bag.isEmpty() &&  selectedColorId != 0){
//            fetchAopDeptRcvData( batchScan, _bag);
//        } else {
//            DialogHelper.showWarningDialog(this, "Warning", "অনুগ্রহ করে রং এবং ব্যাগ নম্বর যোগ করুন |");
//        }

        debouncer.debounce(() -> {
            String _bag = _bagNoScanET.getText().toString().trim();
            if(!_bag.isEmpty() &&  selectedColorId != 0){
                fetchAopDeptRcvData(_bag);
            } else {
                runOnUiThread(() -> DialogHelper.showWarningDialog(this, "Warning", "অনুগ্রহ করে রং এবং ব্যাগ নম্বর যোগ করুন |"));
                _bagNoScanET.setText("");
            }
        }, 2000);
    }

    private void startScanning(int op) {
        Intent intent = new Intent(this, V1_ScannerActivity.class);
        intent.putExtra("qc", "grey_store_reject_bag_rcv");
        intent.putExtra("scan_op", op);
        intent.putExtra("batch_scan", _systemScanTV.getText().toString());
        intent.putExtra("grey_store_reject_bag_rcv_data", greyStoreRejectBag);
        startActivity(intent);
        finish();
    }
}