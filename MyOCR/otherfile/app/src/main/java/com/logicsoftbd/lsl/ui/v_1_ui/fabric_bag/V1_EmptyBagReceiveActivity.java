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
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagEmptyReceiveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FabricBagColorModel;
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

public class V1_EmptyBagReceiveActivity extends AppCompatActivity implements View.OnClickListener, V1_BagReceiveEmptyRecyclerViewAdapter.OnRemoveHeadListener {
    private static final String TAG = "V1_BagKeepingActivity";
    private ProgressBar _progressBar;
    private Spinner _colorSpinner, _categorySpinner;
    private RecyclerView _bagDeliveryRecyclerView;
    private TextView _systemIdScanTV;
    private EditText _bagNoScanET;
    private Spinner _storeSpinner;
    private EditText _remarksET;
    private Button _bagNoScan, _batchScan, _deliveryDateBtn, _saveBtn, _refreshBtn;
    private ImageView _back;
    private String base_url, userID, companyId, userName, defectName, defectId, currentDate, fgsm, mode, bagScan, bagNo, batchScan, rollWeight, selectedColorName;
    private Integer scan_op = 0, storeId = 0, selectedColorId = 0, selectedCategoryId = 0;
    private SimpleDateFormat simpleDateFormat;
    private ArrayList<V1_BagEmptyReceiveResponse.ResultSet> bagEmptyReceiveArrayList = new ArrayList<>();
    private ArrayList<V1_BagEmptyReceiveResponse.ResultSet> dataList = new ArrayList<>();
    private ArrayList<V1GreyFabricTransferOutStoreList> greyFabricTransferOutStoreList;
    private ArrayList<V1_FabricBagColorModel.ResultSet> fabricBagColorModel = new ArrayList<>();
    private V1_BagReceiveEmptyRecyclerViewAdapter bagReceiveEmptyRecyclerViewAdapter;
    private FinishProductionViewModel finishProductionViewModel;
    private final Debouncer debouncer = new Debouncer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_bag_receive_empty);

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
        dataList = (ArrayList<V1_BagEmptyReceiveResponse.ResultSet>) intent.getSerializableExtra("bag_empty_receive_data");

        if(bagScan != null || scan_op == 1) {
            String[] bagScanArray = bagScan.split(Pattern.quote("***"));
            if (bagScanArray.length > 1) {
                _bagNoScanET.setText(bagScanArray[0]);
                bagNo = bagScanArray[0];
            } else {
                bagNo = bagScan;
                _bagNoScanET.setText(bagScan);
            }
        }

        if(dataList != null){
            bagEmptyReceiveArrayList = dataList;
        }
        setupYardDefectRecyclerView();
        bagReceiveEmptyRecyclerViewAdapter.notifyDataSetChanged();

        if(bagScan != null && scan_op == 1){
            fetchBagEmptyReceiveData(bagNo, "");
        }else if(batchScan != null && scan_op == 2){
            fetchBagEmptyReceiveData("", batchScan);
        }

        fetchFabricBagColorData();
        setCategorySpinnerAdapter();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchBagEmptyReceiveData(String bagScan, String batchScan) {
        progressBarState();
        finishProductionViewModel.getBagEmptyReceiveResponse( bagScan, batchScan, selectedColorId, selectedCategoryId).observe(this, apiResponse -> {
            if(apiResponse!= null){
                if(apiResponse.getResultSet() != null) {
                    _bagNoScanET.setText("");
                    try {
                        boolean bagExists = false;

                        for (V1_BagEmptyReceiveResponse.ResultSet item : bagEmptyReceiveArrayList) {
                            item.setCategoryId(String.valueOf(selectedCategoryId));
                            if (item.getBagNo().equals(apiResponse.getResultSet().getBagNo()) &&
                                    item.getBatchNo().equals(apiResponse.getResultSet().getBatchNo())) {
                                DialogHelper.showWarningDialog(V1_EmptyBagReceiveActivity.this, "Warning", "এই ব্যাগটি ইতিমধ্যেই স্ক্যান করা হয়েছে |");
                                bagExists = true;
                                break;
                            }
                        }
                        if (!bagExists) {
                            bagEmptyReceiveArrayList.add(apiResponse.getResultSet());
                        }
                        bagReceiveEmptyRecyclerViewAdapter.notifyDataSetChanged();
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

    private void setCategorySpinnerAdapter() {
        String[] names = getResources().getStringArray(R.array.categry_names_array);
        String[] ids = getResources().getStringArray(R.array.categry_ids_array);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _categorySpinner.setAdapter(adapter);

        _colorSpinner.setSelection(0);
        _categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategoryId = Integer.parseInt(ids[position]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupYardDefectRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _bagDeliveryRecyclerView.setLayoutManager(linearLayoutManager);
        bagReceiveEmptyRecyclerViewAdapter = new V1_BagReceiveEmptyRecyclerViewAdapter( bagEmptyReceiveArrayList, this, this);
        _bagDeliveryRecyclerView.setAdapter(bagReceiveEmptyRecyclerViewAdapter);
    }
    @SuppressLint("ClickableViewAccessibility")
    private void init_ui() {
        _progressBar = findViewById(R.id.progressBar);
        _colorSpinner = findViewById(R.id.colorSpinner);
        _categorySpinner = findViewById(R.id.categorySpinner);
        _bagDeliveryRecyclerView = findViewById(R.id.bagDeliveryRecyclerView);
        _bagNoScan = findViewById(R.id.bagNoScan);
        _bagNoScan.setOnClickListener(this);
        _batchScan = findViewById(R.id.systemIdScan);
        _batchScan.setOnClickListener(this);
//        _deliveryDateBtn = findViewById(R.id.deliveryDateBtn);
//        _deliveryDateBtn.setOnClickListener(this);
        _bagNoScanET = findViewById(R.id.bagNoScanET);
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

    private void postDataToServer() {
        JSONObject save_obj = new JSONObject();
        JSONArray rfid_dtls_arr = new JSONArray();

        try {
            save_obj.put("STATUS", "true");
            save_obj.put("USER_ID", String.valueOf(userID));
            save_obj.put("COMPANY_ID", bagEmptyReceiveArrayList.get(0).getCompanyId());

            for (int i = 0; i < bagEmptyReceiveArrayList.size(); i++) {
                JSONObject dtls_obj = new JSONObject();
                dtls_obj.put("ID", String.valueOf(bagEmptyReceiveArrayList.get(i).getBagReceiveMstId()));
                dtls_obj.put("BAG_NO", String.valueOf(bagEmptyReceiveArrayList.get(i).getBagNo()));
                dtls_obj.put("QR_NO", String.valueOf(bagEmptyReceiveArrayList.get(i).getQrNo()));
                dtls_obj.put("ROLL_QNTY", String.valueOf(bagEmptyReceiveArrayList.get(i).getRollQnty()));
                dtls_obj.put("WEIGHT", String.valueOf(bagEmptyReceiveArrayList.get(i).getWeight()));
                dtls_obj.put("CATEGORY_ID", String.valueOf(bagEmptyReceiveArrayList.get(i).getCategoryId()));
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
        finishProductionViewModel.postBagEmptyReceiveResponse(body).observe(this, apiResponse -> {
            if(apiResponse != null) {
                DialogHelper.showSuccessDialog(this, "Success", apiResponse.getMsg());
                refreshData("");
            }else{
                DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshData(String systemId) {
        _bagNoScanET.setText("");
        bagEmptyReceiveArrayList.clear();
        bagReceiveEmptyRecyclerViewAdapter.notifyDataSetChanged();
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
                    bagEmptyReceiveArrayList.remove(position);
                    bagReceiveEmptyRecyclerViewAdapter.notifyDataSetChanged();
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
            if(bagEmptyReceiveArrayList.size() > 0){
                postDataToServer();
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
            case R.id.systemIdScan:
                startScanning(2);
                break;
            case R.id.deliveryDateBtn:
                pickDate();
                break;
            case R.id.saveBtn:
                if(bagEmptyReceiveArrayList.size() > 0){
                    postDataToServer();
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

    private void callBagAPI() {
//        String _bag = _bagNoScanET.getText().toString().trim();
//        if(!_bag.isEmpty() &&  selectedColorId != 0){
//            fetchBagEmptyReceiveData(_bag, "");
////            if(_bag != null && scan_op == 1){
////                fetchBagEmptyReceiveData(_bag, "");
////            }else if(batchScan != null && scan_op == 2){
////                fetchBagEmptyReceiveData("", batchScan);
////            }
//        } else {
//            DialogHelper.showWarningDialog(this, "Warning", "অনুগ্রহ করে রং এবং ব্যাগ নম্বর যোগ করুন |");
//        }

        debouncer.debounce(() -> {
            String _bag = _bagNoScanET.getText().toString().trim();
            if(!_bag.isEmpty() &&  selectedColorId != 0 && selectedCategoryId != 0){
                fetchBagEmptyReceiveData(_bag, "");
            } else {
                runOnUiThread(() -> DialogHelper.showWarningDialog(this, "Warning", "অনুগ্রহ করে ক্যাটাগরি, রং এবং ব্যাগ নম্বর যোগ করুন |"));
                _bagNoScanET.setText("");
            }
        }, 2000);
    }

    private void startScanning(int op) {
        Intent intent = new Intent(this, V1_ScannerActivity.class);
        intent.putExtra("qc", "bag_empty_receive");
        intent.putExtra("scan_op", op);
        intent.putExtra("bag_empty_receive_data", bagEmptyReceiveArrayList);
        intent.putExtra("bagScan", _bagNoScanET.getText().toString());
        startActivity(intent);
        finish();
    }
}