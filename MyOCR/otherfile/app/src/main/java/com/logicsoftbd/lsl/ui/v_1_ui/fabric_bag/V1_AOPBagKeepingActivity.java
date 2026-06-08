package com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_AOPBagKeepingResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_AOPBagKeepingResponse;
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

public class V1_AOPBagKeepingActivity extends AppCompatActivity implements View.OnClickListener, V1_AOPBagKeepingRecyclerViewAdapter.OnRemoveHeadListener, V1_AOPBagKeepingRecyclerViewAdapter.OnPrintHeadListener {
    private static final String TAG = "V1_BagKeepingActivity";
    private ProgressBar _progressBar;
    private RecyclerView _bagKeepingRecyclerView;
    private CardView _mainCardView;
    private TextView _pListScanTV, _batchScanTV, _posPrinterSelectTV, _processLossTV;
    private LinearLayout _printerLayout;
    private EditText _bagNoScanET, _aopWeightET, _solidWeightET, _gsmET, _diaET;
    private Spinner _fabricTypeSpinner, _colorSpinner;
    private Button _bagNoScan, _batchScan, _pListScan, _saveBtn, _refreshBtn;
    private ImageButton _batchPrintBT;
    private ImageView _back, _printerImage, _printAllStickerIV;
    private String base_url, userID, userName, savedPrinter, defectName, defectId, currentDate, fgsm, mode, bagScan, bagNo, batchScan, rollWeight, noOfRoll, responseSystemNumber, selectedColorName;
    private Integer scan_op = 0, type_no = 0, printPosition = 0, selectedAOP = 0, selectedColorId = 0;
    private Boolean packingListPrintStatus = false;
    private ArrayList<V1_AOPBagKeepingResponse.ResultSet> aopBagKeepingArrayList = new ArrayList<>();
    private ArrayList<V1_AOPBagKeepingResponse.ResultSet> dataList = new ArrayList<>();
    private ArrayList<V1_FabricBagColorModel.ResultSet> fabricBagColorModel = new ArrayList<>();
    private List<String> _fabricTypeName = new ArrayList<>();
    private List<String> _fabricTypeId = new ArrayList<>();
    private List<String> _fabricGsm = new ArrayList<>();
    private SharedPreferences _preferences;
    private V1_AOPBagKeepingRecyclerViewAdapter aopBagKeepingRecyclerViewAdapter;
    private FinishProductionViewModel finishProductionViewModel;
    private final Debouncer debouncer = new Debouncer();

    /*==============================================================================================
    ======================================BLUETOOTH PART============================================
    ==============================================================================================*/

    public interface OnBluetoothPermissionsGranted {
        void onPermissionsGranted();
    }

    public static final int PERMISSION_BLUETOOTH = 1;
    public static final int PERMISSION_BLUETOOTH_ADMIN = 2;
    public static final int PERMISSION_BLUETOOTH_CONNECT = 3;
    public static final int PERMISSION_BLUETOOTH_SCAN = 4;

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

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(V1_AOPBagKeepingActivity.this);
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

    public void printBluetooth(int position, boolean isBatchPrint) {
        this.checkBluetoothPermissions(() -> {
            new AsyncBluetoothEscPosPrint(
                    this,
                    new AsyncEscPosPrint.OnPrintFinished() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onError(AsyncEscPosPrinter asyncEscPosPrinter, int codeException) {
                            Log.e("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : An error occurred !");
                            aopBagKeepingArrayList.get(position).setPrintingStatus(false);
                            aopBagKeepingRecyclerViewAdapter.notifyDataSetChanged();
                        }

                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
                            Log.i("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Print is finished !");
                            aopBagKeepingArrayList.get(position).setPrintingStatus(true);
                            aopBagKeepingRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    }
            )
                    .execute(this.getAsyncEscPosPrinter(selectedDevice, position, isBatchPrint));
        });
    }

    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection printerConnection, int position, boolean isBatchPrint) {
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 100f, 47);
        if(isBatchPrint){
            StringBuilder textToPrint = new StringBuilder();

            textToPrint.append("[C]<qrcode size='24'>" + responseSystemNumber + "</qrcode>\n");
            textToPrint.append("[C]<u type='double'>" + aopBagKeepingArrayList.get(0).getCompanyName() + "</u>\n");
//            textToPrint.append("[C]"+ aopBagKeepingArrayList.get(0).getLocation_name() +"\n");

            textToPrint.append("[L]<font size='normal'>Issue No: [R]"+aopBagKeepingArrayList.get(0).getIssue_no()+"</font>\n");
            textToPrint.append("[L]<font size='normal'>Issue Port: [R]"+aopBagKeepingArrayList.get(0).getIso_port()+"</font>\n");
            textToPrint.append("[L]<font size='normal'>Issue Date: [R]" + aopBagKeepingArrayList.get(0).getIssue_date() + "</font>\n");

            textToPrint.append("[C]<b>Paking List (AOP)</b>\n"); // Assuming "Paking List" is bold in the center
            textToPrint.append("[C]<b>"+responseSystemNumber+"</b>\n");
            double totalSolidWeight = 0;
            double totalAOPWeight = 0;

            for (V1_AOPBagKeepingResponse.ResultSet bag : aopBagKeepingArrayList) {
                totalSolidWeight += Double.parseDouble(bag.getSolidWeight());
                totalAOPWeight += Double.parseDouble(bag.getAopWeight());
            }

            String formatedTotalSolidWeight = String.format("%.2f", totalSolidWeight);
            String formatedTotalAOPWeight = String.format("%.2f", totalAOPWeight);

            textToPrint.append("[L]<font size='8'>Customer: [R]" + aopBagKeepingArrayList.get(0).getBuyerName() + "</font>\n");
            textToPrint.append("[L]<font size='8'>Date: [R]" + currentDate + "</font>\n");
            textToPrint.append("[L]<font size='8'>Ref: [R] "+ aopBagKeepingArrayList.get(0).getInternalRef() + "</font>\n");
            textToPrint.append("[L]<font size='8'>Batch: [R]" + aopBagKeepingArrayList.get(0).getBatchNo() + "</font>\n");
            textToPrint.append("[L]<font size='8'>Fabric: [R] "+ aopBagKeepingArrayList.get(0).getFabTypeName() + "</font>\n");
            textToPrint.append("[L]<font size='8'>Color: [R]" + aopBagKeepingArrayList.get(0).getFabColorName() + "</font>\n");
            textToPrint.append("[L]<font size='8'>G/Qty: [R]" + formatedTotalSolidWeight + "</font>\n");
            textToPrint.append("[L]<font size='8'>F/Qty: [R]" + formatedTotalAOPWeight + "</font>\n");
//            textToPrint.append("[L]<font size='8'>P/L%: [R] " + pl + "</font>\n\n");

            textToPrint.append("[C]------------------------------------------\n");
            textToPrint.append("[C]<font size='8'>Bag No:[R] Wt in Kg:[R] Dia:[R] GSM:</font>\n");
            textToPrint.append("[C]------------------------------------------\n");

            double totalWeight = 0;
            double totalDia = 0;
            double totalGsm = 0;

            for (V1_AOPBagKeepingResponse.ResultSet bag : aopBagKeepingArrayList) {
                textToPrint.append("[C]<font size='8'>" + bag.getBagNo() + " [R] " + bag.getAopWeight() + " [R] " + bag.getDia() + " [R] " + bag.getGsm() + "</font>\n");
                totalWeight += Double.parseDouble(bag.getAopWeight());
                totalDia += Double.parseDouble(bag.getDia());
                totalGsm += Double.parseDouble(bag.getGsm());
            }
            String formatedTotalWeightQnty = String.format("%.2f", totalSolidWeight);

            double avgGSM = totalGsm / aopBagKeepingArrayList.size();
            double avgDia = totalDia / aopBagKeepingArrayList.size();

            textToPrint.append("[C]------------------------------------------\n");
            textToPrint.append("[C]<font size='8'>Total-"+aopBagKeepingArrayList.size()+" [R]" + formatedTotalWeightQnty + "[R]" + "-" + "[R]" + "-"+ "</font>\n");
            textToPrint.append("[C]------------------------------------------\n\n\n");
            textToPrint.append("[L]Supervisor        Store Officer       Rcv By\n\n");

            printer.addTextToPrint(textToPrint.toString());

        }else{
            printer.addTextToPrint("[C]<qrcode size='24'>"+aopBagKeepingArrayList.get(position).getBagNo()+"***"+aopBagKeepingArrayList.get(position).getQrNo()+"</qrcode>\n" +
                    "[C]<u type='double'>"+aopBagKeepingArrayList.get(position).getQrNo()+"</u>\n\n"+
                    "[C]<u type='double'>"+aopBagKeepingArrayList.get(position).getCompanyName()+"</u>\n\n"+
                    "[L]<u><font size='bold'>BAG NO:[R]"+aopBagKeepingArrayList.get(position).getBagNo()+"</u>\n" +
                    "[L]<u><font size='normal'>BUYER NAME:[R]"+aopBagKeepingArrayList.get(position).getBuyerName()+"</u>\n" +
//                    "[L]<u><font size='normal'>BOOKING NO:[R]"+aopBagKeepingArrayList.get(position).getBooking_no()+"</u>\n" +
                    "[L]<u><font size='normal'>IR/IB NO:[R]"+aopBagKeepingArrayList.get(position).getInternalRef()+"</u>\n" +
                    "[L]<u><font size='normal'>BATCH NO:[R]"+aopBagKeepingArrayList.get(position).getBatchNo()+"</u>\n" +
                    "[L]<u><font size='normal'>FABRIC TYPE:[R]"+aopBagKeepingArrayList.get(position).getFabTypeName()+"</u>\n" +
                    "[L]<u><font size='normal'>BAG WEIGHT:[R]"+aopBagKeepingArrayList.get(position).getWeight()+".00"+"</u>\n" +
                    "[L]<u><font size='normal'>COLOR:[R]"+aopBagKeepingArrayList.get(position).getFabColorName()+"</u>\n" +
                    "[L]<u><font size='normal'>GSM:[R]"+aopBagKeepingArrayList.get(position).getGsm()+"</u>\n" +
                    "[L]<u><font size='normal'>DIA:[R]"+aopBagKeepingArrayList.get(position).getDia()+"</u>\n" +
                    "[L]<u><font size='normal'>BAG COLOR:[R]"+aopBagKeepingArrayList.get(position).getBagColorName()+"</u>\n" +
                    "[L]<u><font size='normal'>DATE:[R]"+currentDate+"</u>");
        }

        return printer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_aop_bag_keeping);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        base_url = (_preferences.getString("base_url", ""));
        userID = _preferences.getString("login_userid", "");
        userName = _preferences.getString("login_username", "");
        savedPrinter = _preferences.getString("saved_printer", "");
        finishProductionViewModel = new ViewModelProvider(this).get(FinishProductionViewModel.class);

        Log.d(TAG, "onCreate: ----------->"+savedPrinter);

        init_ui();
        getDefaultData();

        bluetothPart();
    }

    private void bluetothPart() {

    }

    @SuppressLint("NotifyDataSetChanged")
    private void getDefaultData() {
        Intent intent = getIntent();
//        bagScan = intent.getStringExtra("barcodeScan");
        scan_op = intent.getIntExtra("scan_op", 0);
        type_no = intent.getIntExtra("type", 0);

//        if(scan_op != 1){
//            batchScan = intent.getStringExtra("barcodeScan");
//            bagScan = intent.getStringExtra("bagScan");
//        }else {
//            bagScan = intent.getStringExtra("barcodeScan");
//            batchScan = intent.getStringExtra("batch_scan");
//            rollWeight = intent.getStringExtra("rollWeight");
//            noOfRoll = intent.getStringExtra("noOfRoll");
//        }

        if(scan_op == 1){
            bagScan = intent.getStringExtra("barcodeScan");
            batchScan = intent.getStringExtra("batch_scan");
        }else {
            batchScan = intent.getStringExtra("barcodeScan");
//            batchScan = intent.getStringExtra("batch_scan");
            rollWeight = intent.getStringExtra("rollWeight");
        }
        selectedColorId = intent.getIntExtra("selectedFabricBagColor", 0);

        Log.d(TAG, "getDefaultData: ######"+scan_op +" "+type_no+" "+bagScan+" "+batchScan);
        dataList = (ArrayList<V1_AOPBagKeepingResponse.ResultSet>) intent.getSerializableExtra("aop_bag_keeping_data");
        if(dataList != null && dataList.size() > 0){
            Log.d(TAG, "getDefaultData: ------->"+dataList.get(0).getFabricType());
        }

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
        _bagNoScanET.setText(bagScan);
        if(type_no == 2){
            _batchScanTV.setText(batchScan);
        }else if ( type_no == 3){
            _pListScanTV.setText(batchScan);
        }
//        if(type_no == 2){
//            batchScan = intent.getStringExtra("batch_scan");
//            _batchScanTV.setText(batchScan);
//        }else if (type_no == 3){
//            batchScan = intent.getStringExtra("batch_scan");
//            _pListScanTV.setText(batchScan);
//        }
        _aopWeightET.setText(rollWeight);

        if(dataList != null){
            aopBagKeepingArrayList = dataList;
        }
        setupYardDefectRecyclerView();
        aopBagKeepingRecyclerViewAdapter.notifyDataSetChanged();

        if(scan_op == 1 && bagNo != null && batchScan != null){
            if(_batchScanTV.getText().toString() != null) {
                fetchBagKeepingData(bagNo, "2", batchScan);
            }else if(_pListScanTV.getText().toString() != null){
                fetchBagKeepingData(bagNo, "3", batchScan);
            }

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

    @SuppressLint("NotifyDataSetChanged")
    private void fetchBagKeepingData(String bagScan, String searchTpe, String searchQuery) {
        progressBarState();
        finishProductionViewModel.getAOPBagKeepingResponse(searchTpe, searchQuery, bagScan, String.valueOf(selectedColorId)).observe(this, apiResponse -> {
            if(apiResponse!= null){
                if(apiResponse.getResultSet() != null) {
                    try {
                        boolean bagExists = false;
                        for (V1_AOPBagKeepingResponse.ResultSet item : aopBagKeepingArrayList) {
                            if (item.getBagNo().equals(_bagNoScanET.getText().toString()) &&
                                    item.getBatchNo().equals(_batchScanTV.getText().toString().toUpperCase())) {
//                                DialogHelper.showWarningDialog(V1_AOPBagKeepingActivity.this, "Warning", "This bag ("+ _bagNoScanET.getText().toString() +") is already in used.");
                                DialogHelper.showWarningDialog(V1_AOPBagKeepingActivity.this, "Warning", "এই ব্যাগটি ইতিমধ্যেই স্ক্যান করা হয়েছে |");
                                _bagNoScanET.setText("");
                                bagExists = true;
                                break;
                            }
                        }
                        if(!bagExists){
                            setBagKeepingAdapterData(apiResponse);
                        }

                    }catch (Exception e){
                        Log.d(TAG, "fetchBagKeepingData: "+e.getMessage());
                        DialogHelper.showWarningDialog(this, "Warning", "অনুগ্রহ করে আবার চেষ্টা করুন।");
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

    private void setBagKeepingAdapterData(V1_AOPBagKeepingResponse apiResponse) {

//        _gsmET.setText(apiResponse.getResultSet().getGsm());
        _solidWeightET.setText(apiResponse.getResultSet().getWeight());

        _fabricTypeName.clear();
        _fabricTypeId.clear();
        _fabricGsm.clear();

        for (V1_AOPBagKeepingResponse.FabricType fabricType : apiResponse.getResultSet().getFabricType()) {
            _fabricTypeName.add(fabricType.getName());
            _fabricTypeId.add(fabricType.getId());
            _fabricGsm.add(fabricType.getGsm());
        }

        _fabricTypeName.add(0, "-Select-");
        _fabricTypeId.add(0, "0");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, _fabricTypeName);
        _fabricTypeSpinner.setAdapter(adapter);

        _fabricTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    String gsm = _gsmET.getText().toString().trim();
                    String dia = _diaET.getText().toString().trim();
                    String _aopWeight = _aopWeightET.getText().toString().trim();
                    String _solidWeight = _solidWeightET.getText().toString().trim();
                    if(!dia.isEmpty() && !gsm.isEmpty()  && !_aopWeight.isEmpty() && !_solidWeight.isEmpty()) {
                        boolean bagExists = false;
                        apiResponse.getResultSet().setAopWeight(_aopWeightET.getText().toString());
                        apiResponse.getResultSet().setSolidWeight(_solidWeightET.getText().toString());
                        if(!gsm.isEmpty()){
                            apiResponse.getResultSet().setGsm(_gsmET.getText().toString());
                        }else{
                            apiResponse.getResultSet().setGsm(_fabricGsm.get(position));
                        }
                        apiResponse.getResultSet().setDia(_diaET.getText().toString());
                        apiResponse.getResultSet().setBagColorName(selectedColorName);
                        apiResponse.getResultSet().setBagColorId(String.valueOf(selectedColorId));
                        apiResponse.getResultSet().setFabType(_fabricTypeId.get(position));
                        apiResponse.getResultSet().setFabTypeName(_fabricTypeName.get(position));
                        apiResponse.getResultSet().setAop(String.valueOf(selectedAOP == 1 ? 1 : 0));
                        apiResponse.getResultSet().setSaveStatus(false);
                        apiResponse.getResultSet().setPrintingStatus(false);
                        for (V1_AOPBagKeepingResponse.ResultSet item : aopBagKeepingArrayList) {
                            if (item.getBagNo().equals(_bagNoScanET.getText().toString()) &&
                                    item.getBatchNo().equals(_batchScanTV.getText().toString().toUpperCase())) {
//                                DialogHelper.showWarningDialog(V1_AOPBagKeepingActivity.this, "Warning", "This bag ("+ _bagNoScanET.getText().toString() +") is already in used.");
                                DialogHelper.showWarningDialog(V1_AOPBagKeepingActivity.this, "Warning", "এই ব্যাগটি ইতিমধ্যেই স্ক্যান করা হয়েছে |");
                                _bagNoScanET.setText("");
                                bagExists = true;
                                break;
                            }
                        }



                        if (!bagExists) {
                            try {
                                double processLoss = 100 - (Double.parseDouble(_aopWeightET.getText().toString()) / Double.parseDouble(_solidWeightET.getText().toString()))*100;
                                @SuppressLint("DefaultLocale") String formattedProcessLoss = String.format("%.2f", processLoss);
                                _processLossTV.setText(formattedProcessLoss);
                                apiResponse.getResultSet().setProcessLoss(String.valueOf(processLoss));
                            }catch (Exception e) {
                                Log.d(TAG, "onItemSelected: ");
                            }
                            aopBagKeepingArrayList.add(apiResponse.getResultSet());
                            _fabricTypeSpinner.setSelection(0);
//                            _diaET.setText("");
//                            _gsmET.setText(_fabricGsm.get(position));
                            _bagNoScanET.setText("");
                            _aopWeightET.setText("");
                            _bagNoScanET.setText("");
                            _solidWeightET.setText("");
                        }

                        aopBagKeepingRecyclerViewAdapter.notifyDataSetChanged();
                    }else{
                        DialogHelper.showWarningDialog(V1_AOPBagKeepingActivity.this, "Warning", "অনুগ্রহ করে AOP ওয়েইট , GSM, DIA এবং Solid ওয়েইট অ্যাড করুন।");
                        _fabricTypeSpinner.setSelection(0);
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupYardDefectRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _bagKeepingRecyclerView.setLayoutManager(linearLayoutManager);
        aopBagKeepingRecyclerViewAdapter = new V1_AOPBagKeepingRecyclerViewAdapter( aopBagKeepingArrayList, this, this, this);
        _bagKeepingRecyclerView.setAdapter(aopBagKeepingRecyclerViewAdapter);
    }
    @SuppressLint("ClickableViewAccessibility")
    private void init_ui() {
        _progressBar = findViewById(R.id.progressBar);
        _bagKeepingRecyclerView = findViewById(R.id.bagKeepingRecyclerView);
        _bagNoScan = findViewById(R.id.bagNoScan);
        _bagNoScan.setOnClickListener(this);
        _batchScan = findViewById(R.id.batchScan);
        _batchPrintBT = findViewById(R.id.batchPrintBT);
        _batchScan.setOnClickListener(this);
        _pListScan = findViewById(R.id.pListScan);
        _pListScan.setOnClickListener(this);
        _bagNoScanET = findViewById(R.id.bagNoScanET);
        _pListScanTV = findViewById(R.id.pListScanTV);
        _batchScanTV = findViewById(R.id.batchScanTV);
        _aopWeightET = findViewById(R.id.aopWeightET);
        _solidWeightET = findViewById(R.id.solidWeightET);
        _gsmET = findViewById(R.id.gsmET);
        _diaET = findViewById(R.id.diaET);
        _fabricTypeSpinner = findViewById(R.id.fabricTypeSpinner);
        _colorSpinner = findViewById(R.id.colorSpinner);
        _posPrinterSelectTV = findViewById(R.id.posPrinterSelectTV);
        _processLossTV = findViewById(R.id.processLossTV);
        _printerLayout = findViewById(R.id.printerLayout);
        _printerLayout.setOnClickListener(this);
        _refreshBtn = findViewById(R.id.refreshBtn);
        _refreshBtn.setOnClickListener(this);
        _saveBtn = findViewById(R.id.saveBtn);
        _saveBtn.setOnClickListener(this);
        _printerImage = findViewById(R.id.printerImage);
        _printAllStickerIV = findViewById(R.id.printAllStickerIV);
        _back = findViewById(R.id.back);
        _back.setOnClickListener(this);
        _mainCardView= findViewById(R.id.mainCardView);


        _batchPrintBT.setOnClickListener(v -> {
            printBluetooth(0, true);
            packingListPrintStatus = true;
        });

        _printAllStickerIV.setOnClickListener(v -> {
            try {
                printAllStickers();
            } catch (Exception e) {
                // Log the error and notify the user
                Log.e("PrintError", "An error occurred while printing: ", e);
                Toast.makeText(v.getContext(), "স্টিকার প্রিন্ট করা যায়নি। অনুগ্রহ করে আবার চেষ্টা করুন।", Toast.LENGTH_SHORT).show();
            }
        });

//        View rootLayout = findViewById(R.id.full_body);
//        rootLayout.setOnTouchListener((v, event) -> {
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                callBagAPI();
//            }
//            return true;
//        });
//
//        _aopWeightET.setOnTouchListener((v, event) -> {
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


    private void printAllStickers() {
        if (aopBagKeepingArrayList == null || aopBagKeepingArrayList.isEmpty()) {
            Log.w("PrintWarning", "Sticker list is empty. Nothing to print.");
            Toast.makeText(this, "No stickers to print.", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < aopBagKeepingArrayList.size(); i++) {
            try {
                printBluetooth(i, false);
            } catch (Exception e) {
                Log.e("PrintError", "Failed to print sticker at position " + i, e);
            }
        }

        Toast.makeText(this, "Stickers printed successfully.", Toast.LENGTH_SHORT).show();
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fabric, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            if(aopBagKeepingArrayList.size() > 0){
                postDataToServer();
            }else{
                DialogHelper.showWarningDialog(this, "Warning", "অনুগ্রহ করে ন্যূনতম একটি ব্যাগ যোগ করুন।");
            }
            return true;
        }
        else if (id == R.id.action_new){
            refreshData(1);
        } else if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void postDataToServer() {
        JSONObject save_obj = new JSONObject();
        JSONArray rfid_dtls_arr = new JSONArray();

        try {
            save_obj.put("STATUS", "true");
            save_obj.put("USER_ID", String.valueOf(userID));
            save_obj.put("BATCH_NO", String.valueOf(aopBagKeepingArrayList.get(0).getBatchNo()));
            save_obj.put("BUYER_ID", String.valueOf(aopBagKeepingArrayList.get(0).getBuyerId()));
            save_obj.put("COMPANY_ID", String.valueOf(aopBagKeepingArrayList.get(0).getCompanyId()));

            for (int i = 0; i < aopBagKeepingArrayList.size(); i++) {
                JSONObject dtls_obj = new JSONObject();
                dtls_obj.put("BAG_CREATION_ID", String.valueOf(aopBagKeepingArrayList.get(i).getBagCreationId()));
                dtls_obj.put("BAG_STATUS", String.valueOf(aopBagKeepingArrayList.get(i).getBagStatus()));
                dtls_obj.put("ITEM_CATEGORY", String.valueOf(aopBagKeepingArrayList.get(i).getItemCategory()));
                dtls_obj.put("BAG_NO", String.valueOf(aopBagKeepingArrayList.get(i).getBagNo()));
                dtls_obj.put("RFID_NO", String.valueOf(aopBagKeepingArrayList.get(i).getRfidNo()));
                dtls_obj.put("QR_NO", String.valueOf(aopBagKeepingArrayList.get(i).getQrNo()));
                dtls_obj.put("BATCH_NO", String.valueOf(aopBagKeepingArrayList.get(i).getBatchNo()));
                dtls_obj.put("AOP_WEIGHT", String.valueOf(aopBagKeepingArrayList.get(i).getAopWeight()));
                dtls_obj.put("SOLID_WEIGHT", String.valueOf(aopBagKeepingArrayList.get(i).getSolidWeight()));
                dtls_obj.put("INTERNAL_REF", String.valueOf(aopBagKeepingArrayList.get(i).getInternalRef()));
                dtls_obj.put("FABRIC_COLOR_ID", String.valueOf(aopBagKeepingArrayList.get(i).getFabColorId()));
                dtls_obj.put("BUYER_ID", String.valueOf(aopBagKeepingArrayList.get(i).getBuyerId()));
                dtls_obj.put("ROLL_QNTY", String.valueOf(aopBagKeepingArrayList.get(i).getRollQnty()));
                dtls_obj.put("COMPANY_ID", String.valueOf(aopBagKeepingArrayList.get(i).getCompanyId()));
                dtls_obj.put("DIA", String.valueOf(aopBagKeepingArrayList.get(i).getDia()));
                dtls_obj.put("GSM", String.valueOf(aopBagKeepingArrayList.get(i).getGsm()));
                dtls_obj.put("FABRIC_TYPE", aopBagKeepingArrayList.get(i).getFabType());
                dtls_obj.put("BAG_KEEPING_MST_ID", aopBagKeepingArrayList.get(i).getBagReceiveMstId());
                dtls_obj.put("BAG_KEEPING_DETAILS_ID", aopBagKeepingArrayList.get(i).getBagReceiveDetailsId());
                dtls_obj.put("SYSTEM_NO", aopBagKeepingArrayList.get(i).getSystemNo());
                dtls_obj.put("COLOR_ID", aopBagKeepingArrayList.get(i).getBagColorId());
                dtls_obj.put("AOP", "1");
                dtls_obj.put("REJECT", "0");
                dtls_obj.put("QC_DONE", "1");
                dtls_obj.put("PROCESS_LOSS", _processLossTV.getText().toString());
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
        finishProductionViewModel.postAOPBagKeepingResponse(body).observe(this, apiResponse -> {
            if(apiResponse != null) {
                DialogHelper.showSuccessDialog(this, "Success", apiResponse.getResultset().getMsg());
                responseSystemNumber = apiResponse.getResultset().getSysNumber();
                refreshData(2);
                _saveBtn.setEnabled(false);
            }else{
                DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshData(int statusCode) {
        if(statusCode == 1){
            _bagNoScanET.setText("");
            _pListScanTV.setText("");
            _batchScanTV.setText("");
            _aopWeightET.setText("");
            _gsmET.setText("");
            _diaET.setText("");
            _solidWeightET.setText("");
            _processLossTV.setText("");

            _batchPrintBT.setVisibility(View.GONE);
            aopBagKeepingArrayList.clear();
            aopBagKeepingRecyclerViewAdapter.notifyDataSetChanged();
        }else{
            if(aopBagKeepingArrayList.size() > 0){
                for(V1_AOPBagKeepingResponse.ResultSet item: aopBagKeepingArrayList){
                    item.setSaveStatus(true);
                }
            }
            _batchPrintBT.setVisibility(View.VISIBLE);
            aopBagKeepingRecyclerViewAdapter.notifyDataSetChanged();
        }
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
                    aopBagKeepingArrayList.remove(position);
                    aopBagKeepingRecyclerViewAdapter.notifyDataSetChanged();
                })
                .setCancelClickListener(sDialog -> sDialog.cancel())
                .show();
    }

    @Override
    public void onPrintHeadClick(int position, View v) {
        printBluetooth(position, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pListScan:
                String _pListNo1 = _pListScanTV.getText().toString().trim();
                String _batchNo1 = _batchScanTV.getText().toString().trim();

                if (_pListNo1.isEmpty() && _batchNo1.isEmpty()) {
                    startScanning(3, 3);
                } else {
                    DialogHelper.showWarningDialog(this, "Warning", "Batch can't be changed. Please refresh first.");
                }
                break;
            case R.id.batchScan:
                String _batchNo2 = _batchScanTV.getText().toString().trim();
                String _pListNo2 = _pListScanTV.getText().toString().trim();
                if (_batchNo2.isEmpty() && _pListNo2.isEmpty()) {
                    startScanning(2, 2);
                } else {
                    DialogHelper.showWarningDialog(this, "Warning", "Batch can't be changed. Please refresh first.");
                }
                break;
            case R.id.bagNoScan:
                String _batch = _batchScanTV.getText().toString().trim();
                String _pList = _pListScanTV.getText().toString().trim();
                if ((!_batch.isEmpty())  && selectedColorId != 0) {
                    String _bag = _bagNoScanET.getText().toString().trim();
                    if(_bag.isEmpty()){
                        startScanning(1, 2);
                    }else{
                        if(_batchScanTV.getText().toString() != null) {
                            fetchBagKeepingData(_bag, "2", batchScan);
                        }else if(_pListScanTV.getText().toString() != null){
                            fetchBagKeepingData(_bag, "3", batchScan);
                        }
                    }
//                    startScanning(1, 2);
                }else if( !_pList.isEmpty()  && selectedColorId != 0) {
                    String _bag = _bagNoScanET.getText().toString().trim();
                    if(_bag.isEmpty()){
                        startScanning(1, 3);
                    }else{
                        if(_batchScanTV.getText().toString() != null) {
                            fetchBagKeepingData(_bag, "2", batchScan);
                        }else if(_pListScanTV.getText().toString() != null){
                            fetchBagKeepingData(_bag, "3", batchScan);
                        }
                    }
//                    startScanning(1, 3);
                }
//                if ((!_batch.isEmpty() || !_pList.isEmpty())  && !_noOfRoll.isEmpty()) {
//                    startScanning(1, 0);
//                }
                else {
                    DialogHelper.showWarningDialog(this, "Warning", "অনুগ্রহ করে প্রথমে সিস্টেম আইডি এবং ব্যাগের রঙ স্ক্যান করুন।");
                }
                break;
            case R.id.printerLayout:
                browseBluetoothDevice();
                break;
            case R.id.saveBtn:
                if(aopBagKeepingArrayList.size() > 0){
                    postDataToServer();
                }else{
                    DialogHelper.showWarningDialog(this, "Warning", "অনুগ্রহ করে ন্যূনতম একটি ব্যাগ যোগ করুন।");
                }
                break;
            case R.id.refreshBtn:
                refreshData(1);
                break;
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    private void callBagAPI() {
//        String _bag = _bagNoScanET.getText().toString().trim();
//        if(!_bag.isEmpty() && !batchScan.isEmpty()){
//            fetchBagKeepingData(_bag, "2", batchScan);
////            if(_batchScanTV.getText().toString() != null) {
////                fetchBagKeepingData(_bag, "2", batchScan);
////            }else if(_pListScanTV.getText().toString() != null){
////                fetchBagKeepingData(_bag, "3", batchScan);
////            }
//        }

        debouncer.debounce(() -> {
            String _bag = _bagNoScanET.getText().toString().trim();
            if(!_bag.isEmpty() && !batchScan.isEmpty()){
                fetchBagKeepingData(_bag, "2", batchScan);
            } else {
                runOnUiThread(() -> DialogHelper.showWarningDialog(this, "Warning", "অনুগ্রহ করে ওজন এবং ব্যাগের রঙ যোগ করুন ।"));
            }
        }, 2000);
    }
    private void startScanning(int op, int type) {
        Intent intent = new Intent(this, V1_ScannerActivity.class);
        intent.putExtra("qc", "aop_bag_keeping");
        intent.putExtra("scan_op", op);
        intent.putExtra("type", type);
        intent.putExtra("aop_bag_keeping_data", aopBagKeepingArrayList);
        intent.putExtra("bagScan", _bagNoScanET.getText().toString());
        String _batch = _batchScanTV.getText().toString().trim();
        String _pList = _pListScanTV.getText().toString().trim();
        if ((!_batch.isEmpty())) {
            intent.putExtra("batch_scan", _batchScanTV.getText().toString());
        }else if(!_pList.isEmpty()) {
            intent.putExtra("batch_scan", _pListScanTV.getText().toString());
        }
        intent.putExtra("rollWeight", _aopWeightET.getText().toString());
        intent.putExtra("selectedFabricBagColor", selectedColorId);
        startActivity(intent);
        finish();
    }

}