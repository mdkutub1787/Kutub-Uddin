package com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagKeepingDataBySystemResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagKeepingResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FabricBagColorModel;
import com.logicsoftbd.lsl.posPrinter.connection.DeviceConnection;
import com.logicsoftbd.lsl.posPrinter.connection.bluetooth.BluetoothConnection;
import com.logicsoftbd.lsl.posPrinter.connection.bluetooth.BluetoothPrintersConnections;
import com.logicsoftbd.lsl.ui.v_1_ui.about.PrintTestActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_ScannerActivity;
import com.logicsoftbd.lsl.utils.Debouncer;
import com.logicsoftbd.lsl.utils.DialogHelper;
import com.logicsoftbd.lsl.utils.async.AsyncBluetoothEscPosPrint;
import com.logicsoftbd.lsl.utils.async.AsyncEscPosPrint;
import com.logicsoftbd.lsl.utils.async.AsyncEscPosPrinter;
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

public class V1_BagKeepingQcActivity extends AppCompatActivity implements View.OnClickListener, V1_BagKeepingQCRecyclerViewAdapter.OnRemoveHeadListener, V1_BagKeepingQCRecyclerViewAdapter.OnRejectHeadListener {
    private static final String TAG = "V1_BagKeepingQcActivity";

    private SharedPreferences _preferences;
    private ProgressBar _progressBar;
    private RecyclerView _bagKeepingRecyclerView;
    private Spinner _colorSpinner;
    private Button _bagNoScan, _systemNoScan, _saveBtn, _refreshBtn;
    private ImageView _back, _printerImage;
    private ImageButton _packingListPrintBT;
    private TextView _systemScanTV, _posPrinterSelectTV;
    private LinearLayout _printerLayout;
    private EditText _bagNoScanET;
    private ScrollView _homeLayout;
    private V1_BagKeepingQCRecyclerViewAdapter bagKeepingRecyclerViewAdapter;
    private String base_url, userID, userName, savedPrinter, defectName, defectId, currentDate, fgsm, mode, bagScan, bagNo, batchScan, rollWeight, noOfRoll, selectedColorName;
    private Integer scan_op = 0, selectedColorId = 0;
    private ArrayList<V1_BagKeepingDataBySystemResponse.ResultSet> dataList = new ArrayList<>();
    private ArrayList<V1_BagKeepingDataBySystemResponse.ResultSet> bagKeepingArrayList = new ArrayList<>();
    private ArrayList<V1_FabricBagColorModel.ResultSet> fabricBagColorModel = new ArrayList<>();
    private FinishProductionViewModel finishProductionViewModel;
    private final Debouncer debouncer = new Debouncer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_bag_keeping_qc);
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
        if(scan_op == 1){
//            batchScan = intent.getStringExtra("barcodeScan");
            bagScan = intent.getStringExtra("barcodeScan");

        }else {
//            bagScan = intent.getStringExtra("barcodeScan");
//            batchScan = intent.getStringExtra("batch_scan");
            batchScan = intent.getStringExtra("barcodeScan");
        }
        selectedColorId = intent.getIntExtra("selectedFabricBagColor", 0);
        dataList = (ArrayList<V1_BagKeepingDataBySystemResponse.ResultSet>) intent.getSerializableExtra("bag_keeping_qc_data");

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
            bagKeepingArrayList = dataList;
        }
        setupRecyclerView();
        bagKeepingRecyclerViewAdapter.notifyDataSetChanged();

        if(scan_op == 1 &&  bagScan != null){
            fetchBagKeepingQCData("0", bagScan, selectedColorId);
        } else if(scan_op == 2 && batchScan != null) {
            fetchBagKeepingQCData(batchScan, "0", selectedColorId);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = new Date(System.currentTimeMillis());
        currentDate = formatter.format(date);

        if(!savedPrinter.equals("")){
            _posPrinterSelectTV.setText(savedPrinter);
            _posPrinterSelectTV.setTextColor(getResources().getColor(R.color.green_A200));
            _printerImage.setImageResource(R.drawable.connectedprinter);
        }else{
            _posPrinterSelectTV.setText("Not Connected");
            _posPrinterSelectTV.setTextColor(getResources().getColor(R.color.white));
            _printerImage.setImageResource(R.drawable.notconnectedprinter);
        }

        fetchFabricBagColorData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchBagKeepingQCData(String systemScan, String bagScan, Integer selectedColorId) {
        progressBarState();
        finishProductionViewModel.getBagKeepingBySystemResponse(systemScan, bagScan, selectedColorId).observe(this, apiResponse -> {
            if(apiResponse!= null){
                if(apiResponse.getResultSet() != null && !apiResponse.getStatusCode().equals("400")) {
                    try {
                        for(int i=0; i<apiResponse.getResultSet().size(); i++){
                            apiResponse.getResultSet().get(i).setIsRejecting("0");
                        }
                        setBagKeepingAdapterData(apiResponse);
                    }catch (Exception e){
                        Log.d(TAG, "fetchBagKeepingData: "+e.getMessage());
                        DialogHelper.showWarningDialog(this, "Warning", "Please try again.");
                    }
                }
                else{
                    _bagNoScanET.setText("");
                    _systemScanTV.setText("");
                    DialogHelper.showWarningDialog(this, "Warning", apiResponse.getMsg());
                }
            }else{
                DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setBagKeepingAdapterData(V1_BagKeepingDataBySystemResponse apiResponse) {
        if(bagKeepingArrayList != null) {
            ArrayList<V1_BagKeepingDataBySystemResponse.ResultSet> newBags = new ArrayList<>();

            boolean singleBagInResponse = apiResponse.getResultSet().size() == 1;

            for (V1_BagKeepingDataBySystemResponse.ResultSet apiItem : apiResponse.getResultSet()) {
                boolean bagExists = false;
                apiItem.setBagColorId(String.valueOf(selectedColorId));
                for (V1_BagKeepingDataBySystemResponse.ResultSet item : bagKeepingArrayList) {
                    if (item.getBagNo().equals(apiItem.getBagNo())) {
                        bagExists = true;
                        break;
                    }
                }
                if (!bagExists) {
                    newBags.add(apiItem);
                } else if (singleBagInResponse) {
                    DialogHelper.showWarningDialog(
                            V1_BagKeepingQcActivity.this,
                            "Warning",
                            "এই ব্যাগটি ইতিমধ্যেই স্ক্যান করা হয়েছে |"
                    );
                }
            }

            if (!newBags.isEmpty()) {
                bagKeepingArrayList.addAll(newBags);
            } else if (!singleBagInResponse) {
                DialogHelper.showWarningDialog(
                        V1_BagKeepingQcActivity.this,
                        "Warning",
                        "সব ব্যাগ ইতিমধ্যে ব্যবহার করা হয় |"
                );
            }

            _bagNoScanET.setText("");
            _systemScanTV.setText("");
            bagKeepingRecyclerViewAdapter.notifyDataSetChanged();

            if(bagKeepingArrayList != null && !bagKeepingArrayList.isEmpty()){
                _packingListPrintBT.setVisibility(View.VISIBLE);
            }else{
                _packingListPrintBT.setVisibility(View.GONE);
            }
        }
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
            save_obj.put("BATCH_NO", String.valueOf(bagKeepingArrayList.get(0).getBatchNo()));


            for (int i = 0; i < bagKeepingArrayList.size(); i++) {
                JSONObject dtls_obj = new JSONObject();
                dtls_obj.put("BAG_NO", String.valueOf(bagKeepingArrayList.get(i).getBagNo()));
                dtls_obj.put("REJECT", bagKeepingArrayList.get(i).getIsRejecting());
                dtls_obj.put("QR_NO", bagKeepingArrayList.get(i).getQrNo());
                dtls_obj.put("COLOR_ID", bagKeepingArrayList.get(i).getBagColorId());
                rfid_dtls_arr.put(dtls_obj);
            }

            save_obj.put("BAG_NUMBERS", rfid_dtls_arr);
            Log.d(TAG, "postDataToServer: ########" + save_obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, save_obj.toString());
        progressBarState();
        finishProductionViewModel.postBagKeepingQCResponse(body).observe(this, apiResponse -> {
            if(apiResponse != null) {
                DialogHelper.showSuccessDialog(this, "Success", apiResponse.getMsg());
                refreshData();
            }else{
                DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
            }
        });
    }

    private void refreshData() {
        _bagNoScanET.setText("");
        _systemScanTV.setText("");

        bagKeepingArrayList.clear();
        bagKeepingRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _bagKeepingRecyclerView.setLayoutManager(linearLayoutManager);
        bagKeepingRecyclerViewAdapter = new V1_BagKeepingQCRecyclerViewAdapter( bagKeepingArrayList, this, this, this);
        _bagKeepingRecyclerView.setAdapter(bagKeepingRecyclerViewAdapter);
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
        _posPrinterSelectTV = findViewById(R.id.posPrinterSelectTV);
        _printerImage = findViewById(R.id.printerImage);
        _packingListPrintBT = findViewById(R.id.packingListPrintBT);
        _packingListPrintBT.setOnClickListener(this);
        _printerLayout = findViewById(R.id.printerLayout);
        _printerLayout.setOnClickListener(this);
        _refreshBtn = findViewById(R.id.refreshBtn);
        _refreshBtn.setOnClickListener(this);
        _saveBtn = findViewById(R.id.saveBtn);
        _saveBtn.setOnClickListener(this);
        _back = findViewById(R.id.back);
        _back.setOnClickListener(this);

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
                    callBagFetchAPI();
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
                if (selectedColorId != 0) {
                    String _bag = _bagNoScanET.getText().toString().trim();
                    if(_bag.isEmpty()){
                        startScanning(1);
                    }else{
                        callBagFetchAPI();
                    }
                } else {
                    DialogHelper.showWarningDialog(this, "Warning", "অনুগ্রহ করে ব্যাগের রঙ যোগ করুন ।");
                }
//                startScanning(1);
                break;
            case R.id.saveBtn:
                if(bagKeepingArrayList.size() > 0){
                    postDataToServer();
                }else{
                    DialogHelper.showWarningDialog(this, "Warning", "অনুগ্রহ করে ন্যূনতম একটি ব্যাগ যোগ করুন।");
                }
                break;
            case R.id.printerLayout:
                browseBluetoothDevice();
                break;
            case R.id.packingListPrintBT:
                printPackingListSticker();
                break;
            case R.id.refreshBtn:
                refreshData();
                break;
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    private void printPackingListSticker() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(V1_BagKeepingQcActivity.this);
        alertDialog.setTitle("\uD83D\uDCE3 Bluetooth Printer Selection");
        alertDialog.setMessage("Please select an action for the PackingList Print:\n\n- QC Pass: For approved items.\n- Reject: For rejected items.");

        alertDialog.setPositiveButton("\u2705 QC Pass PackingList Print", (dialogInterface, which) -> {
            printBluetooth(false);
        });

        alertDialog.setNegativeButton("\u274C Reject PackingList Print", (dialogInterface, which) -> {
            boolean hasValidData = false;
            for (V1_BagKeepingDataBySystemResponse.ResultSet bag : bagKeepingArrayList) {
                if (bag.getIsRejecting().equals("1")) {
                    hasValidData = true;
                    break;
                }
            }

            if (!hasValidData) {
                Toast.makeText(this, "কোনো ব্যাগ রিজেক্ট এর জন্য সিলেক্ট করা হয় নি।", Toast.LENGTH_SHORT).show();
            }else{
                printBluetooth(true);
            }

        });

        alertDialog.setCancelable(true);

        AlertDialog alert = alertDialog.create();

        alert.setOnShowListener(dialog -> {
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.white));
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.white));
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.light_green));
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.red_100));
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTypeface(null, Typeface.BOLD);
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTypeface(null, Typeface.BOLD);
        });

        alert.show();
    }

    private void callBagFetchAPI() {
//        if (selectedColorId != 0) {
//            String _bag = _bagNoScanET.getText().toString().trim();
//            if(!_bag.isEmpty()){
//                fetchBagKeepingQCData("0", _bag, selectedColorId);
//            }
//        } else {
//            DialogHelper.showWarningDialog(this, "Warning", "অনুগ্রহ করে ব্যাগের রঙ যোগ করুন ।");
//        }

        debouncer.debounce(() -> {
            if (selectedColorId != 0) {
                String _bag = _bagNoScanET.getText().toString().trim();
                if(!_bag.isEmpty()){
                    fetchBagKeepingQCData("0", _bag, selectedColorId);
                }
            } else {
                runOnUiThread(() -> DialogHelper.showWarningDialog(this, "Warning", "অনুগ্রহ করে ব্যাগের রঙ যোগ করুন ।"));
                _bagNoScanET.setText("");
            }
        }, 2000);
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
                    bagKeepingArrayList.remove(position);
                    bagKeepingRecyclerViewAdapter.notifyDataSetChanged();
                })
                .setCancelClickListener(SweetAlertDialog::cancel)
                .show();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onRejectHeadClick(int position, View v) {
        if(bagKeepingArrayList.get(position).getIsRejecting().equals("0")){
            bagKeepingArrayList.get(position).setIsRejecting("1");
        }else{
            bagKeepingArrayList.get(position).setIsRejecting("0");
        }
        bagKeepingRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void startScanning(int op) {
        Intent intent = new Intent(this, V1_ScannerActivity.class);
        intent.putExtra("qc", "bag_keeping_qc");
        intent.putExtra("scan_op", op);
        intent.putExtra("bag_keeping_qc_data", bagKeepingArrayList);
        intent.putExtra("selectedFabricBagColor", selectedColorId);
        startActivity(intent);
        finish();
    }

//    Printer
    public PrintTestActivity.OnBluetoothPermissionsGranted onBluetoothPermissionsGranted;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case PrintTestActivity.PERMISSION_BLUETOOTH:
                case PrintTestActivity.PERMISSION_BLUETOOTH_ADMIN:
                case PrintTestActivity.PERMISSION_BLUETOOTH_CONNECT:
                case PrintTestActivity.PERMISSION_BLUETOOTH_SCAN:
                    this.checkBluetoothPermissions(this.onBluetoothPermissionsGranted);
                    break;
            }
        }
    }

    public void checkBluetoothPermissions(PrintTestActivity.OnBluetoothPermissionsGranted onBluetoothPermissionsGranted) {
        this.onBluetoothPermissionsGranted = onBluetoothPermissionsGranted;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, PrintTestActivity.PERMISSION_BLUETOOTH);
        } else if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, PrintTestActivity.PERMISSION_BLUETOOTH_ADMIN);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PrintTestActivity.PERMISSION_BLUETOOTH_CONNECT);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, PrintTestActivity.PERMISSION_BLUETOOTH_SCAN);
        } else {
            this.onBluetoothPermissionsGranted.onPermissionsGranted();
        }
    }

    private BluetoothConnection selectedDevice;

    @SuppressLint("MissingPermission")
    public void browseBluetoothDevice() {
        this.checkBluetoothPermissions(() -> {
            final BluetoothConnection[] bluetoothDevicesList = (new BluetoothPrintersConnections()).getList();

            if (bluetoothDevicesList != null) {
                final String[] items = new String[bluetoothDevicesList.length + 1];
                items[0] = "Default printer";
                int i = 0;
                for (BluetoothConnection device : bluetoothDevicesList) {
                    items[++i] = device.getDevice().getName();
                }

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(V1_BagKeepingQcActivity.this);
                alertDialog.setTitle("Bluetooth printer selection");
                alertDialog.setItems(
                        items,
                        (dialogInterface, i1) -> {
                            int index = i1 - 1;
                            if (index == -1) {
                                selectedDevice = null;
                            } else {
                                selectedDevice = bluetoothDevicesList[index];
                            }
                            _posPrinterSelectTV.setText(items[i1]);
                            _posPrinterSelectTV.setTextColor(getResources().getColor(R.color.green_A200));
                            _printerImage.setImageResource(R.drawable.connectedprinter);
                            _preferences = PreferenceManager.getDefaultSharedPreferences(this);
                            SharedPreferences.Editor _editor = _preferences.edit();
                            _editor.putString("saved_printer", items[i1]);
                            _editor.apply();
                        }
                );

                AlertDialog alert = alertDialog.create();
                alert.setCanceledOnTouchOutside(false);
                alert.show();
            }
        });

    }

    public void printBluetooth(boolean isReject) {
        this.checkBluetoothPermissions(() -> {
            new AsyncBluetoothEscPosPrint(
                    this,
                    new AsyncEscPosPrint.OnPrintFinished() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onError(AsyncEscPosPrinter asyncEscPosPrinter, int codeException) {
                            Log.e("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : An error occurred !");
                            bagKeepingRecyclerViewAdapter.notifyDataSetChanged();
                        }

                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
                            Log.i("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Print is finished !");
                            bagKeepingRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    }
            )
                    .execute(this.getAsyncEscPosPrinter(selectedDevice, isReject));
        });
    }

    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection printerConnection, boolean isReject) {
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 100f, 47);

        try {
            StringBuilder textToPrint = new StringBuilder();

            textToPrint.append("[C]<qrcode size='24'>" + bagKeepingArrayList.get(0).getSystemNo() + "</qrcode>\n");
            textToPrint.append("[C]<u type='double'>" + bagKeepingArrayList.get(0).getCompanyName() + "</u>\n");
            textToPrint.append("[C]"+ bagKeepingArrayList.get(0).getLocationName() +"\n");

            textToPrint.append("[L]<font size='normal'>Issue No: [R]"+bagKeepingArrayList.get(0).getIssue_no()+"</font>\n");
            textToPrint.append("[L]<font size='normal'>Issue Port: [R]"+bagKeepingArrayList.get(0).getIso_port()+"</font>\n");
            textToPrint.append("[L]<font size='normal'>Issue Date: [R]" + bagKeepingArrayList.get(0).getIssue_date() + "</font>\n");

            if(isReject){
                textToPrint.append("[C]<b>Paking List-(Reject)</b>\n");
            }else {
                textToPrint.append("[C]<b>Paking List</b>\n");
            }

            textToPrint.append("[C]<b>"+bagKeepingArrayList.get(0).getSystemNo()+"</b>\n");

            double totalGreyQnty = 0;
            double totalFinishQnty = 0;

            for (V1_BagKeepingDataBySystemResponse.ResultSet bag : bagKeepingArrayList) {
                if (isReject && bag.getIsRejecting().equals("1")) {
                    totalFinishQnty += Double.parseDouble(bag.getWeight());
                } else if (!isReject && bag.getIsRejecting().equals("0")) {
                    totalFinishQnty += Double.parseDouble(bag.getWeight());
                }
            }

            String formatedFinishQnty = String.format("%.2f", totalFinishQnty);

            textToPrint.append("[L]<font size='8'>Customer: [R]" + bagKeepingArrayList.get(0).getBuyerName() + "</font>\n");
            textToPrint.append("[L]<font size='8'>Date: [R]" + currentDate + "</font>\n");
            textToPrint.append("[L]<font size='8'>Ref: [R] "+ bagKeepingArrayList.get(0).getIrIb() + "</font>\n");
            textToPrint.append("[L]<font size='8'>Batch: [R]" + bagKeepingArrayList.get(0).getBatchNo() + "</font>\n");
            textToPrint.append("[L]<font size='8'>Fabric: [R] "+ bagKeepingArrayList.get(0).getFabricTypeName() + "</font>\n");
            textToPrint.append("[L]<font size='8'>Color: [R]" + bagKeepingArrayList.get(0).getFabColorName() + "</font>\n");
            textToPrint.append("[L]<font size='8'>G/Qty: [R]" + totalGreyQnty + "</font>\n");
            textToPrint.append("[L]<font size='8'>F/Qty: [R]" + formatedFinishQnty + "</font>\n");

            textToPrint.append("[C]------------------------------------------\n");
            textToPrint.append("[C]<font size='8'>Bag No:[R] Wt in Kg:[R] Dia:[R] GSM:</font>\n");
            textToPrint.append("[C]------------------------------------------\n");

            double totalWeight = 0;
            int total = 0;

            for (V1_BagKeepingDataBySystemResponse.ResultSet bag : bagKeepingArrayList) {
                if (isReject && bag.getIsRejecting().equals("1")) {
                    textToPrint.append("[C]<font size='8'>" + bag.getBagNo() + " [R] " + bag.getWeight() + " [R] " + bag.getDia() + " [R] " + bag.getGsm() + "</font>\n");
                    totalWeight += Double.parseDouble(bag.getWeight());
                    total++;
                } else if (!isReject && bag.getIsRejecting().equals("0")) {
                    textToPrint.append("[C]<font size='8'>" + bag.getBagNo() + " [R] " + bag.getWeight() + " [R] " + bag.getDia() + " [R] " + bag.getGsm() + "</font>\n");
                    totalWeight += Double.parseDouble(bag.getWeight());
                    total++;
                }
            }

            String formatedTotalFinishQnty = String.format("%.2f", totalWeight);

            textToPrint.append("[C]------------------------------------------\n");
            textToPrint.append("[C]<font size='8'>Total-" + total + " [R]" + formatedTotalFinishQnty + "[R]" + "-" + "[R]" + "-"+ "</font>\n");
            textToPrint.append("[C]------------------------------------------\n\n\n");
            textToPrint.append("[L]Supervisor        Store Officer       Rcv By\n\n");

            printer.addTextToPrint(textToPrint.toString());
        } catch (Exception e) {
            DialogHelper.showWarningDialog(this, "Warning", "ডেটাতে কিছু সমস্যার কারণে স্টিকার প্রিন্ট করা যাচ্ছে না, অনুগ্রহ করে আবার চেষ্টা করুন।");
            Log.d(TAG, "getAsyncEscPosPrinter: "+e.getMessage());
        }
        return printer;
    }



//    @SuppressLint("SimpleDateFormat")
//    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection printerConnection, boolean isReject) {
//        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 100f, 47);
//
//        try {
//            StringBuilder textToPrint = new StringBuilder();
//
//            textToPrint.append("[C]<qrcode size='24'>" + bagKeepingArrayList.get(0).getSystemNo() + "</qrcode>\n");
//            textToPrint.append("[C]<u type='double'>" + bagKeepingArrayList.get(0).getCompanyName() + "</u>\n");
//            textToPrint.append("[C]"+ bagKeepingArrayList.get(0).getLocationName() +"\n");
//
//            textToPrint.append("[L]<font size='normal'>Issue No: [R]01</font>\n");
//            textToPrint.append("[L]<font size='normal'>F/AOPLFN: [R]07</font>\n");
//
//            textToPrint.append("[C]<b>Paking List</b>\n");
//            textToPrint.append("[C]<b>"+bagKeepingArrayList.get(0).getSystemNo()+"</b>\n");
//
//            double totalGreyQnty = 0;
//            double totalFinishQnty = 0;
//
//            for (V1_BagKeepingDataBySystemResponse.ResultSet bag : bagKeepingArrayList) {
//                totalFinishQnty += Double.parseDouble(bag.getWeight());
//            }
//
//            String formatedFinishQnty = String.format("%.2f", totalFinishQnty);
//
//            textToPrint.append("[L]<font size='8'>Customer: [R]" + bagKeepingArrayList.get(0).getBuyerName() + "</font>\n");
//            textToPrint.append("[L]<font size='8'>Date: [R]" + currentDate + "</font>\n");
//            textToPrint.append("[L]<font size='8'>Ref: [R] "+ bagKeepingArrayList.get(0).getIrIb() + "</font>\n");
//            textToPrint.append("[L]<font size='8'>Batch: [R]" + bagKeepingArrayList.get(0).getBatchNo() + "</font>\n");
//            textToPrint.append("[L]<font size='8'>Fabric: [R] "+ bagKeepingArrayList.get(0).getFabricTypeName() + "</font>\n");
//            textToPrint.append("[L]<font size='8'>Color: [R]" + bagKeepingArrayList.get(0).getFabColorName() + "</font>\n");
//            textToPrint.append("[L]<font size='8'>G/Qty: [R]" + totalGreyQnty + "</font>\n");
//            textToPrint.append("[L]<font size='8'>F/Qty: [R]" + formatedFinishQnty + "</font>\n");
//
//            textToPrint.append("[C]------------------------------------------\n");
//            textToPrint.append("[C]<font size='8'>Bag No:[R] Wt in Kg:[R] Dia:[R] GSM:</font>\n");
//            textToPrint.append("[C]------------------------------------------\n");
//
//            double totalWeight = 0;
//
//            for (V1_BagKeepingDataBySystemResponse.ResultSet bag : bagKeepingArrayList) {
//                if(isReject && bag.getIsRejecting().equals("0")){
//                    textToPrint.append("[C]<font size='8'>" + bag.getBagNo() + " [R] " + bag.getWeight() + " [R] " + bag.getDia() + " [R] " + bag.getGsm() + "</font>\n");
//                    totalWeight += Double.parseDouble(bag.getWeight());
//                }else{
//                    textToPrint.append("[C]<font size='8'>" + bag.getBagNo() + " [R] " + bag.getWeight() + " [R] " + bag.getDia() + " [R] " + bag.getGsm() + "</font>\n");
//                    totalWeight += Double.parseDouble(bag.getWeight());
//                }
//
//            }
//            String formatedTotalFinishQnty = String.format("%.2f", totalWeight);
//
//            textToPrint.append("[C]------------------------------------------\n");
//            textToPrint.append("[C]<font size='8'>Total-"+bagKeepingArrayList.size()+" [R]" + formatedTotalFinishQnty + "[R]" + "-" + "[R]" + "-"+ "</font>\n");
//            textToPrint.append("[C]------------------------------------------\n\n\n");
//            textToPrint.append("[L]Supervisor        Store Officer       Rcv By\n\n");
//
//            printer.addTextToPrint(textToPrint.toString());
//        }catch (Exception e){
//            DialogHelper.showWarningDialog(this, "Warning", "ডেটাতে কিছু সমস্যার কারণে স্টিকার প্রিন্ট করা যাচ্ছে না, অনুগ্রহ করে আবার চেষ্টা করুন।");
//            Log.d(TAG, "getAsyncEscPosPrinter: "+e.getMessage());
//        }
//        return printer;
//    }
}