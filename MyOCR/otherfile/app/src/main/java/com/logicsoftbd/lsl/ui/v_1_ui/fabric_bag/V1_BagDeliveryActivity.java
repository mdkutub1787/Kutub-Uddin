package com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagDeliveryResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_transfer_out.V1GreyFabricTransferOutStoreList;
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

public class V1_BagDeliveryActivity extends AppCompatActivity implements View.OnClickListener, V1_BagDeliveryRecyclerViewAdapter.OnRemoveHeadListener {
    private static final String TAG = "V1_BagKeepingActivity";
    private ProgressBar _progressBar;
    private Spinner _colorSpinner;
    private RecyclerView _bagDeliveryRecyclerView;
    private TextView _systemIdScanTV, _bagNoScanTV;
    private Spinner _storeSpinner;
    private EditText _remarksET;
    private Button _bagNoScan, _batchScan, _deliveryDateBtn, _saveBtn, _refreshBtn;
    private ImageView _back;
    private String base_url, userID, companyId, userName, defectName, defectId, currentDate, fgsm, mode, bagScan, bagNo, batchScan, rollWeight;
    private Integer scan_op = 0, storeId = 0;
    private SimpleDateFormat simpleDateFormat;
    private ArrayList<V1_BagDeliveryResponse.ResultSet> bagDeliveryArrayList = new ArrayList<>();
    private ArrayList<V1_BagDeliveryResponse.ResultSet> dataList = new ArrayList<>();
    private ArrayList<V1GreyFabricTransferOutStoreList> greyFabricTransferOutStoreList;

    private V1_BagDeliveryRecyclerViewAdapter bagDeliveryRecyclerViewAdapter;
    private FinishProductionViewModel finishProductionViewModel;
    private final Debouncer debouncer = new Debouncer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_bag_delivery);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        base_url = (_preferences.getString("base_url", ""));
        userID = _preferences.getString("login_userid", "");
        userName = _preferences.getString("login_username", "");
        companyId = _preferences.getString("company_id", "");
        finishProductionViewModel = new ViewModelProvider(this).get(FinishProductionViewModel.class);

        init_ui();
        getDefaultData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getDefaultData() {
        Intent intent = getIntent();
        bagScan = intent.getStringExtra("barcodeScan");
        scan_op = intent.getIntExtra("scan_op", 0);
        dataList = (ArrayList<V1_BagDeliveryResponse.ResultSet>) intent.getSerializableExtra("bag_delivery_data");

        if(bagScan != null || scan_op == 1) {
            String[] bagScanArray = bagScan.split(Pattern.quote("***"));
            if (bagScanArray.length > 1) {
                _bagNoScanTV.setText(bagScanArray[0]);
                bagNo = bagScanArray[0];
            } else {
                bagNo = bagScan;
                _bagNoScanTV.setText(bagScan);
            }
        }

        if(dataList != null){
            bagDeliveryArrayList = dataList;
        }
        setupYardDefectRecyclerView();
        bagDeliveryRecyclerViewAdapter.notifyDataSetChanged();

        if(bagScan != null && bagNo != null){
            fetchBagDeliveryData(bagNo);
        }

        fetchStoreListData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchBagDeliveryData(String bagScan) {
        progressBarState();
        finishProductionViewModel.getBagDeliveryResponse( bagScan).observe(this, apiResponse -> {
            if(apiResponse!= null){
                if(apiResponse.getResultSet() != null) {
                    _bagNoScanTV.setText("");
                    try {
                        boolean bagExists = false;

                        for (V1_BagDeliveryResponse.ResultSet item : bagDeliveryArrayList) {
                            if (item.getBagNo().equals(apiResponse.getResultSet().getBagNo()) &&
                                    item.getBatchNo().equals(apiResponse.getResultSet().getBatchNo())) {
                                DialogHelper.showWarningDialog(V1_BagDeliveryActivity.this, "Warning", "This bag ("+ _bagNoScanTV.getText().toString() +") is already in used.");
                                bagExists = true;
                                break;
                            }
                        }
                        if (!bagExists) {
                            bagDeliveryArrayList.add(apiResponse.getResultSet());
                        }
                        bagDeliveryRecyclerViewAdapter.notifyDataSetChanged();
                    }catch (Exception e){
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

    private void fetchStoreListData() {
        progressBarState();
        finishProductionViewModel.getStoreResponse(userID, "2", companyId).observe(this, apiResponse -> {
            if(apiResponse != null) {
                if(apiResponse.getStores() != null && apiResponse.getStores().size() > 0){
                    greyFabricTransferOutStoreList = (ArrayList<V1GreyFabricTransferOutStoreList>) apiResponse.getStores();
                    setupStoreSpinner();
                }
            }else {
                DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
            }
        });

    }

    private void setupStoreSpinner() {
        List<String> _storeNames = new ArrayList<>();
        List<String> _storeIds = new ArrayList<>();
        for (V1GreyFabricTransferOutStoreList store : greyFabricTransferOutStoreList) {
            _storeNames.add(store.getSTORENAME());
            _storeIds.add(store.getSTOREID());
        }
        _storeNames.add(0, "--Select--");
        _storeIds.add(0, "0");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, _storeNames);

        _storeSpinner.setAdapter(adapter);
        _storeSpinner.setSelection(_storeIds.indexOf(storeId));

        _storeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                storeId = Integer.parseInt(String.valueOf(_storeIds.get(position)));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupYardDefectRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _bagDeliveryRecyclerView.setLayoutManager(linearLayoutManager);
        bagDeliveryRecyclerViewAdapter = new V1_BagDeliveryRecyclerViewAdapter( bagDeliveryArrayList, this, this);
        _bagDeliveryRecyclerView.setAdapter(bagDeliveryRecyclerViewAdapter);
    }
    private void init_ui() {
        _progressBar = findViewById(R.id.progressBar);
        _colorSpinner = findViewById(R.id.colorSpinner);
        _bagDeliveryRecyclerView = findViewById(R.id.bagDeliveryRecyclerView);
        _bagNoScan = findViewById(R.id.bagNoScan);
        _bagNoScan.setOnClickListener(this);
        _deliveryDateBtn = findViewById(R.id.deliveryDateBtn);
        _deliveryDateBtn.setOnClickListener(this);
        _bagNoScanTV = findViewById(R.id.bagNoScanTV);
        _systemIdScanTV = findViewById(R.id.systemIdScanTV);
        _remarksET= findViewById(R.id.remarksET);
        _storeSpinner= findViewById(R.id.storeSpinner);

        _refreshBtn = findViewById(R.id.refreshBtn);
        _refreshBtn.setOnClickListener(this);
        _saveBtn = findViewById(R.id.saveBtn);
        _saveBtn.setOnClickListener(this);
        _back = findViewById(R.id.back);
        _back.setOnClickListener(this);

        simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = new Date(System.currentTimeMillis());
        currentDate = simpleDateFormat.format(date);
        _deliveryDateBtn.setText(currentDate);

    }

    private void postDataToServer() {
        JSONObject save_obj = new JSONObject();
        JSONArray rfid_dtls_arr = new JSONArray();

        try {
            save_obj.put("STATUS", "true");
            save_obj.put("USER_ID", String.valueOf(userID));
            save_obj.put("COMPANY_ID", bagDeliveryArrayList.get(0).getCompanyId());
            save_obj.put("STORE_ID", storeId);
            save_obj.put("REMARKS", _remarksET.getText().toString());

            for (int i = 0; i < bagDeliveryArrayList.size(); i++) {
                JSONObject dtls_obj = new JSONObject();
                dtls_obj.put("ID", String.valueOf(bagDeliveryArrayList.get(i).getId()));
                dtls_obj.put("BAG_NO", String.valueOf(bagDeliveryArrayList.get(i).getBagNo()));
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
        finishProductionViewModel.postBagDeliveryResponse(body).observe(this, apiResponse -> {
            if(apiResponse != null) {
                DialogHelper.showSuccessDialog(this, "Success", apiResponse.getMsg()+" saved, Challan no is "+ apiResponse.getSystemId());
                refreshData(apiResponse.getSystemId());
            }else{
                DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshData(String systemId) {
        _bagNoScanTV.setText("");
        _systemIdScanTV.setText(systemId);
        _remarksET.setText("");
        storeId = 0;
        bagDeliveryArrayList.clear();
        bagDeliveryRecyclerViewAdapter.notifyDataSetChanged();
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
                    bagDeliveryArrayList.remove(position);
                    bagDeliveryRecyclerViewAdapter.notifyDataSetChanged();
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
            if(bagDeliveryArrayList.size() > 0){
                if(storeId != 0){
                    postDataToServer();
                }else{
                    DialogHelper.showWarningDialog(this, "Warning", "Please select store.");
                }

            }else{
                DialogHelper.showWarningDialog(this, "Warning", "অনুগ্রহ করে ন্যূনতম একটি ব্যাগ যোগ করুন।");
            }
            return true;
        } else if (id == R.id.action_new){
            refreshData("");
        }  else if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bagNoScan:
                startScanning(1);
                break;
            case R.id.deliveryDateBtn:
                pickDate();
                break;
            case R.id.saveBtn:
                if(bagDeliveryArrayList.size() > 0){
                    if(storeId != 0){
                        postDataToServer();
                    }else{
                        DialogHelper.showWarningDialog(this, "Warning", "Please select store.");
                    }
                }else{
                    DialogHelper.showWarningDialog(this, "Warning", "অনুগ্রহ করে ন্যূনতম একটি ব্যাগ যোগ করুন।");
                }
                break;
            case R.id.refreshBtn:
                refreshData("");
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
                    _deliveryDateBtn.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }
    private void startScanning(int op) {
        Intent intent = new Intent(this, V1_ScannerActivity.class);
        intent.putExtra("qc", "bag_delivery");
        intent.putExtra("scan_op", op);
        intent.putExtra("bag_delivery_data", bagDeliveryArrayList);
        intent.putExtra("bagScan", _bagNoScanTV.getText().toString());
        startActivity(intent);
        finish();
    }
}