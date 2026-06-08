package com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import android.os.Build;
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

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagKeepingDataBySystemResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagKeepingResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseSewingInputClass;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class V1_BagKeepingActivity extends AppCompatActivity implements View.OnClickListener, V1_BagKeepingRecyclerViewAdapter.OnRemoveHeadListener, V1_BagKeepingRecyclerViewAdapter.OnPrintHeadListener {
    private static final String TAG = "V1_BagKeepingActivity";
    private ProgressBar _progressBar;
    private RecyclerView _bagKeepingRecyclerView;
    private CardView _mainCardView;
    private TextView _batchScanTV, _bagNoScanTV, _posPrinterSelectTV, _greyTotalTV, _noOfBagTV, _finishTotalTV, _plParcentageTV;
    private LinearLayout _printerLayout;
    private EditText _bagNoScanET, _rollWeightET, _greyWeightET, _gsmET, _diaET;
    private Spinner _fabricTypeSpinner, _aopSpinner, _colorSpinner;
    private Button _bagNoScan, _batchScan, _saveBtn, _refreshBtn;
    private ImageButton _batchPrintBT;
    private ImageView _back, _printerImage, _printAllStickerIV;
    private String base_url, userID, userName, savedPrinter, defectName, defectId, currentDate, fgsm, mode, bagScan, bagNo, batchScan, rollWeight, noOfRoll, responseSystemNumber, selectedAOP, selectedColorName;
    private Integer scan_op = 0, printPosition = 0, selectedColorId = 0;
    private Boolean packingListPrintStatus = false;
    private ArrayList<V1_BagKeepingResponse.ResultSet> bagKeepingArrayList = new ArrayList<>();
    private ArrayList<V1_BagKeepingDataBySystemResponse.ResultSet> bagKeepingDataByPackingListArrayList = new ArrayList<>();
    private ArrayList<V1_BagKeepingResponse.ResultSet> dataList = new ArrayList<>();
    private ArrayList<V1_FabricBagColorModel.ResultSet> fabricBagColorModel = new ArrayList<>();
    private List<String> _fabricTypeName = new ArrayList<>();
    private List<String> _aopName = new ArrayList<>();
    private List<String> _aopId = new ArrayList<>();
//    private List<String> _colorName = new ArrayList<>();
//    private List<String> _colorId = new ArrayList<>();
    private List<String> _fabricTypeId = new ArrayList<>();
    private List<String> _fabricGsm = new ArrayList<>();
    private SharedPreferences _preferences;
    private V1_BagKeepingRecyclerViewAdapter bagKeepingRecyclerViewAdapter;
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

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(V1_BagKeepingActivity.this);
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
                            bagKeepingArrayList.get(position).setPrintingStatus(false);
                            bagKeepingRecyclerViewAdapter.notifyDataSetChanged();
                        }

                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
                            Log.i("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Print is finished !");
                            bagKeepingArrayList.get(position).setPrintingStatus(true);
                            bagKeepingRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    }
            )
                    .execute(this.getAsyncEscPosPrinter(selectedDevice, position, isBatchPrint));
        });
    }

    @SuppressLint({"SimpleDateFormat", "DefaultLocale"})
    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection printerConnection, int position, boolean isBatchPrint) {
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 100f, 47);

        if(isBatchPrint){
            try {
                StringBuilder textToPrint = new StringBuilder();

                textToPrint.append("[C]<qrcode size='24'>" + responseSystemNumber + "</qrcode>\n");
                textToPrint.append("[C]<u type='double'>" + bagKeepingDataByPackingListArrayList.get(0).getCompanyName() + "</u>\n");
                textToPrint.append("[C]"+ bagKeepingDataByPackingListArrayList.get(0).getLocationName() +"\n");

                textToPrint.append("[L]<font size='normal'>Issue No: [R]"+bagKeepingDataByPackingListArrayList.get(0).getIssue_no()+"</font>\n");
                textToPrint.append("[L]<font size='normal'>Issue Port: [R]"+bagKeepingDataByPackingListArrayList.get(0).getIso_port()+"</font>\n");
                textToPrint.append("[L]<font size='normal'>Issue Date: [R]" + bagKeepingDataByPackingListArrayList.get(0).getIssue_date() + "</font>\n");

                textToPrint.append("[C]<b>Packing List</b>\n");
                textToPrint.append("[C]<b>"+responseSystemNumber+"</b>\n");

                double totalGreyQnty = 0;
                double totalFinishQnty = 0;
                double totalWeight = 0;
                int totalCount = 0;

//                for (V1_BagKeepingDataBySystemResponse.ResultSet bag : bagKeepingDataByPackingListArrayList) {
//                    totalGreyQnty += Double.parseDouble(bag.getGreyWeight());
//                    totalFinishQnty += Double.parseDouble(bag.getWeight());
//                }
//
//                double pl = (totalGreyQnty - totalFinishQnty)/totalGreyQnty * 100;

                for (V1_BagKeepingDataBySystemResponse.ResultSet bag : bagKeepingDataByPackingListArrayList) {
                    String greyWeightStr = bag.getGreyWeight();
                    double greyWeight = 0.0;

                    if (greyWeightStr != null && !greyWeightStr.trim().isEmpty()) {
                        try {
                            greyWeight = Double.parseDouble(greyWeightStr);
                        } catch (NumberFormatException e) {
                            Log.e(TAG, "Invalid grey weight: " + greyWeightStr, e);
                        }
                    }

                    double finishWeight = Double.parseDouble(bag.getWeight());

                    totalGreyQnty += greyWeight;
                    totalFinishQnty += finishWeight;
                }


                double pl = (totalGreyQnty > 0) ? ((totalGreyQnty - totalFinishQnty) / totalGreyQnty) * 100 : 0;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    bagKeepingDataByPackingListArrayList.sort(Comparator.comparing(V1_BagKeepingDataBySystemResponse.ResultSet::getFabricTypeName)
                            .thenComparing(V1_BagKeepingDataBySystemResponse.ResultSet::getGsm)
                            .thenComparing(V1_BagKeepingDataBySystemResponse.ResultSet::getDia));
                }

                textToPrint.append("[L]<font size='8'>Customer: [R]" + bagKeepingDataByPackingListArrayList.get(0).getBuyerName() + "</font>\n");
                textToPrint.append("[L]<font size='8'>Date: [R]" + currentDate + "</font>\n");
                textToPrint.append("[L]<font size='8'>Ref: [R] "+ bagKeepingDataByPackingListArrayList.get(0).getIrIb() + "</font>\n");
                textToPrint.append("[L]<font size='8'>Batch: [R]" + bagKeepingDataByPackingListArrayList.get(0).getBatchNo() + "</font>\n");
                textToPrint.append("[L]<font size='8'>Color: [R]" + bagKeepingDataByPackingListArrayList.get(0).getFabColorName() + "</font>\n");
                textToPrint.append("[L]<font size='8'>G/Qty: [R]" + totalGreyQnty + "</font>\n");
                textToPrint.append("[L]<font size='8'>F/Qty: [R]" + String.format("%.2f", totalFinishQnty) + "</font>\n");
                textToPrint.append("[L]<font size='8'>P/L%: [R] " + String.format("%.2f", pl) + "</font>\n\n");

                textToPrint.append("[C]------------------------------------------------\n");
                textToPrint.append("[C]<font size='8'>Bag No[R] Fab Type[R] Dia[R] GSM[R] Wt in Kg</font>\n");
                textToPrint.append("[C]------------------------------------------------\n");

                String lastFabType = "";
                String lastGSM = "";
                String lastDia = "";
                double groupWeight = 0;
                int groupCount = 0;
                int grandTotalCount = 0;

                for (V1_BagKeepingDataBySystemResponse.ResultSet bag : bagKeepingDataByPackingListArrayList) {
                    String fabTypeShort = bag.getFabricTypeName().length() > 5 ? bag.getFabricTypeName().substring(0, 5) : bag.getFabricTypeName();

                    if (!bag.getFabricTypeName().equals(lastFabType) ||
                            !bag.getGsm().equals(lastGSM) ||
                            !bag.getDia().equals(lastDia)) {

                        if (!lastFabType.equals("")) {
                            textToPrint.append("[C]------------------------------------------------\n");
                            textToPrint.append("[C]<font size='bold'>"+groupCount+" Sub-Total [R] " + String.format("%.2f", groupWeight) + "</font>\n");
                            textToPrint.append("[C]---------------------------------------------\n");
                        }
                        groupWeight = 0;
                        groupCount = 0;
                        lastFabType = bag.getFabricTypeName();
                        lastGSM = bag.getGsm();
                        lastDia = bag.getDia();
                    }

                    textToPrint.append("[C]<font size='8'>" + bag.getBagNo() + " [R] "+ fabTypeShort + " [R] " + bag.getDia() + " [R] " + bag.getGsm() + " [R] " + String.format("%.2f", Double.parseDouble(bag.getWeight())) +"</font>\n");

                    totalWeight += Double.parseDouble(bag.getWeight());
                    totalCount++;
                    groupWeight += Double.parseDouble(bag.getWeight());
                    groupCount++;
                    grandTotalCount++;
                }

                if (groupWeight > 0) {
                    textToPrint.append("[C]---------------------------------------------\n");
//                    textToPrint.append("[C]<font size='bold'>Sub-Total [R] " + String.format("%.2f", groupWeight) + "</font>\n");
                    textToPrint.append("[C]<font size='bold'>" + groupCount + " Sub-Total [R] " + String.format("%.2f", groupWeight) + "</font>\n");
                    textToPrint.append("[C]---------------------------------------------\n");
                }

                textToPrint.append("[C]---------------------------------------------\n");
                textToPrint.append("[C]<font size='bold'>"+grandTotalCount+" Grand-Total [R] " +  String.format("%.2f", totalWeight) + "</font>\n");
                textToPrint.append("[C]---------------------------------------------\n\n\n");
                textToPrint.append("[L]Supervisor        Store Officer       Rcv By\n\n");

                printer.addTextToPrint(textToPrint.toString());

                Log.d(TAG, "getAsyncEscPosPrinter: "+textToPrint.toString());
            } catch (Exception e) {
                DialogHelper.showWarningDialog(this, "Warning", "ডেটাতে কিছু সমস্যার কারণে স্টিকার প্রিন্ট করা যাচ্ছে না, অনুগ্রহ করে আবার চেষ্টা করুন।");
                Log.d(TAG, "getAsyncEscPosPrinter: "+e.getMessage());
            }
        }else{
            try {
                String aopStatus = bagKeepingArrayList.get(position).getAop().equals("0") ? "NO":"YES";
                printer.addTextToPrint("[C]<qrcode size='24'>"+bagKeepingArrayList.get(position).getBagNo()+"***"+bagKeepingArrayList.get(position).getQrNo()+"</qrcode>\n" +
                        "[C]<u type='double'>"+bagKeepingArrayList.get(position).getQrNo()+"</u>\n\n"+
                        "[C]<u type='double'>"+bagKeepingArrayList.get(position).getCompany_name()+"</u>\n\n"+
                        "[L]<u><font size='normal'>BAG NO:[R]"+bagKeepingArrayList.get(position).getBagNo()+"</u>\n" +
                        "[L]<u><font size='normal'>BUYER NAME:[R]"+bagKeepingArrayList.get(position).getBuyerName()+"</u>\n" +
                        "[L]<u><font size='normal'>BOOKING NO:[R]"+bagKeepingArrayList.get(position).getBooking_no()+"</u>\n" +
                        "[L]<u><font size='normal'>IR/IB NO:[R]"+bagKeepingArrayList.get(position).getIrIb()+"</u>\n" +
                        "[L]<u><font size='normal'>BATCH NO:[R]"+bagKeepingArrayList.get(position).getBatchNo()+"</u>\n" +
                        "[L]<u><font size='normal'>FABRIC TYPE:[R]"+bagKeepingArrayList.get(position).getFabTypeName()+"</u>\n" +
                        "[L]<u><font size='normal'>BAG WEIGHT:[R]"+bagKeepingArrayList.get(position).getFinishWeight()+".00"+"</u>\n" +
                        "[L]<u><font size='normal'>COLOR:[R]"+bagKeepingArrayList.get(position).getFabColorName()+"</u>\n" +
                        "[L]<u><font size='normal'>AOP:[R]"+aopStatus+"</u>\n" +
                        "[L]<u><font size='normal'>GSM:[R]"+bagKeepingArrayList.get(position).getGsm()+"</u>\n" +
                        "[L]<u><font size='normal'>DIA:[R]"+bagKeepingArrayList.get(position).getDia()+"</u>\n" +
                        "[L]<u><font size='normal'>BAG COLOR:[R]"+bagKeepingArrayList.get(position).getBagColorName()+"</u>\n" +
                        "[L]<u><font size='normal'>DATE:[R]"+currentDate+"</u>");
            }catch (Exception e){
                Log.d(TAG, "getAsyncEscPosPrinter: "+e.getMessage());
                DialogHelper.showWarningDialog(this, "Warning", "ডেটাতে কিছু সমস্যার কারণে স্টিকার প্রিন্ট করা যাচ্ছে না, অনুগ্রহ করে আবার চেষ্টা করুন।");
            }
        }
        return printer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_bag_keeping);
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
        bagScan = intent.getStringExtra("barcodeScan");
        scan_op = intent.getIntExtra("scan_op", 0);
        if(scan_op != 1){
            batchScan = intent.getStringExtra("barcodeScan");
            bagScan = intent.getStringExtra("bagScan");

        }else {
            bagScan = intent.getStringExtra("barcodeScan");
            batchScan = intent.getStringExtra("batch_scan");
            rollWeight = intent.getStringExtra("rollWeight");
        }
        selectedColorId = intent.getIntExtra("selectedFabricBagColor", 0);
        dataList = (ArrayList<V1_BagKeepingResponse.ResultSet>) intent.getSerializableExtra("bag_keeping_data");
        if(dataList != null && dataList.size() > 0){
            Log.d(TAG, "getDefaultData: ------->"+dataList.get(0).getFabType());
        }

        if(bagScan != null && scan_op == 1) {
            String[] bagScanArray = bagScan.split(Pattern.quote("***"));
            if (bagScanArray.length > 1) {
//                _bagNoScanTV.setText(bagScanArray[0]);
                _bagNoScanET.setText(bagScanArray[0]);
                bagNo = bagScanArray[0];
            } else {
                bagNo = bagScan;
//                _bagNoScanTV.setText(bagScan);
                _bagNoScanET.setText(bagScan);
            }
        }
//        _bagNoScanTV.setText(bagScan);
        _batchScanTV.setText(batchScan);
        _rollWeightET.setText(rollWeight);

        if(dataList != null){
            bagKeepingArrayList = dataList;
        }
        setupYardDefectRecyclerView();
        bagKeepingRecyclerViewAdapter.notifyDataSetChanged();

        if(scan_op == 1 && bagNo != null && batchScan != null){
            fetchBagKeepingData(bagNo, batchScan);
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
    private void fetchBagKeepingData(String bagScan, String batchScan) {
        progressBarState();
        finishProductionViewModel.getBagKeepingResponse(batchScan, bagScan, String.valueOf(selectedColorId)).observe(this, apiResponse -> {
            if(apiResponse!= null){
                if(apiResponse.getResultSet() != null) {

                    try {
                        boolean bagExists = false;
                        for (V1_BagKeepingResponse.ResultSet item : bagKeepingArrayList) {
                            if (item.getBagNo().equals(_bagNoScanET.getText().toString()) &&
                                    item.getBatchNo().equals(_batchScanTV.getText().toString().toUpperCase())) {
                                DialogHelper.showWarningDialog(V1_BagKeepingActivity.this, "Warning", "এই ব্যাগটি ইতিমধ্যেই স্ক্যান করা হয়েছে |");
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
                        DialogHelper.showWarningDialog(this, "Warning", "Please try again.");
                    }
                }
                else{
                    DialogHelper.showWarningDialog(this, "Warning", apiResponse.getMsg());
                    _bagNoScanET.setText("");
                }
            }else{
                DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
            }
        });
    }

    private void setBagKeepingAdapterData(V1_BagKeepingResponse apiResponse) {
        _fabricTypeName.clear();
        _fabricTypeId.clear();
        _fabricGsm.clear();
//        _gsmET.setText(apiResponse.getResultSet().getGsm());
        for (V1_BagKeepingResponse.FabricType fabricType : apiResponse.getResultSet().getFabricType()) {
            _fabricTypeName.add(fabricType.getName());
            _fabricTypeId.add(fabricType.getId());
            _fabricGsm.add(fabricType.getGsm());
        }

        _aopName.clear();
        _aopId.clear();
        _aopName.add(0, "-Select-");
        _aopName.add(1, "YES");
        _aopName.add(2, "NO");
        _aopId.add(0, "-1");
        _aopId.add(1, "1");
        _aopId.add(2, "0");

        ArrayAdapter<String> aopAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, _aopName);
        _aopSpinner.setAdapter(aopAdapter);

        _aopSpinner.setSelection(2);
        _aopSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAOP = _aopId.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        _fabricTypeName.add(0, "-Select-");
        _fabricTypeId.add(0, "0");
        _fabricGsm.add(0, "0");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, _fabricTypeName);
        _fabricTypeSpinner.setAdapter(adapter);

        _fabricTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    try {
                        String gsm = _gsmET.getText().toString().trim();
                        String dia = _diaET.getText().toString().trim();
                        String grey_weight = _greyWeightET.getText().toString().trim();
                        String _weight = _rollWeightET.getText().toString().trim();
                        if(!dia.isEmpty() && !_weight.isEmpty() && !gsm.isEmpty() && !selectedAOP.equals("-1")) {
                            boolean bagExists = false;
                            apiResponse.getResultSet().setFinishWeight(_weight);
                            apiResponse.getResultSet().setGreyWeight(grey_weight);
                            if(!gsm.isEmpty()){
                                apiResponse.getResultSet().setGsm(gsm);
                            }else{
                                apiResponse.getResultSet().setGsm(_fabricGsm.get(position));
                            }
                            apiResponse.getResultSet().setDia(dia);
                            apiResponse.getResultSet().setFabType(_fabricTypeId.get(position));
                            apiResponse.getResultSet().setFabTypeName(_fabricTypeName.get(position));
                            apiResponse.getResultSet().setAop(selectedAOP);
                            apiResponse.getResultSet().setSaveStatus(false);
                            apiResponse.getResultSet().setPrintingStatus(false);
                            apiResponse.getResultSet().setBagColorName(selectedColorName);
                            apiResponse.getResultSet().setBagColorId(String.valueOf(selectedColorId));
                            for (V1_BagKeepingResponse.ResultSet item : bagKeepingArrayList) {
                                if (item.getBagNo().equals(_bagNoScanET.getText().toString()) &&
                                        item.getBatchNo().equals(_bagNoScanET.getText().toString().toUpperCase())) {
                                    DialogHelper.showWarningDialog(V1_BagKeepingActivity.this, "Warning", "এই ব্যাগটি ইতি মধ্যেই স্ক্যান করা হয়েছে |");
                                    _bagNoScanET.setText("");
                                    bagExists = true;
                                    break;
                                }
                            }

                            if (!bagExists) {
                                bagKeepingArrayList.add(apiResponse.getResultSet());
                                _bagNoScanET.setText("");
                                _fabricTypeSpinner.setSelection(0);
                                _aopSpinner.setSelection(2);
                                _rollWeightET.setText("");
                                apiResponse.setResultSet(null);

                                calculateTotalValue();
                            }

                            bagKeepingRecyclerViewAdapter.notifyDataSetChanged();
                        }else{
                            DialogHelper.showWarningDialog(V1_BagKeepingActivity.this, "Warning", "GSM, DIA, FINISH WEIGHT, AOP and FABRIC TYPE can't be empty.");
                            _fabricTypeSpinner.setSelection(0);
                        }
                    }catch (Exception e){
                        Log.d(TAG, "onItemSelected: ");
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void calculateTotalValue() {
        try {
            double greyTotal = 0.0;
            double finishTotal = 0.0;

            for (V1_BagKeepingResponse.ResultSet bag : bagKeepingArrayList) {
                String greyWeightStr = bag.getGreyWeight();
                String finishWeightStr = bag.getFinishWeight();

                if (greyWeightStr != null && !greyWeightStr.trim().isEmpty()) {
                    double greyWeight = Double.parseDouble(greyWeightStr);
                    if (greyWeight > 0) {
                        greyTotal += greyWeight;
                    }
                }

                if (finishWeightStr != null && !finishWeightStr.trim().isEmpty()) {
                    finishTotal += Double.parseDouble(finishWeightStr);
                }
            }

            _greyTotalTV.setText(String.format("%.2f", greyTotal));
            _finishTotalTV.setText(String.format("%.2f", finishTotal));
            _noOfBagTV.setText(String.valueOf(bagKeepingArrayList.size()));

            double plPercentage = (greyTotal > 0) ? ((greyTotal - finishTotal) / greyTotal) * 100 : 0;
            _plParcentageTV.setText(String.format("%.2f", plPercentage) + "%");

        } catch (Exception e) {
            Log.e(TAG, "calculateTotalValue: " + e.getMessage(), e);
        }

    }

    private void setupYardDefectRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _bagKeepingRecyclerView.setLayoutManager(linearLayoutManager);
        bagKeepingRecyclerViewAdapter = new V1_BagKeepingRecyclerViewAdapter( bagKeepingArrayList, this, this, this);
        _bagKeepingRecyclerView.setAdapter(bagKeepingRecyclerViewAdapter);
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
//        _bagNoScanTV = findViewById(R.id.bagNoScanTV);
        _bagNoScanET = findViewById(R.id.bagNoScanET);
        _batchScanTV = findViewById(R.id.batchScanTV);
        _rollWeightET = findViewById(R.id.rollWeightET);
        _greyWeightET = findViewById(R.id.greyWeightET);
//        _greyWeightET.setOnClickListener(this);
        _gsmET = findViewById(R.id.gsmET);
        _diaET = findViewById(R.id.diaET);
//        _diaET.setOnClickListener(this);
        _fabricTypeSpinner = findViewById(R.id.fabricTypeSpinner);
        _aopSpinner = findViewById(R.id.aopSpinner);
        _colorSpinner = findViewById(R.id.colorSpinner);
        _posPrinterSelectTV = findViewById(R.id.posPrinterSelectTV);
        _greyTotalTV = findViewById(R.id.greyTotalTV);
        _noOfBagTV = findViewById(R.id.noOfBagTV);
        _finishTotalTV = findViewById(R.id.finishTotalTV);
        _plParcentageTV = findViewById(R.id.plParcentageTV);
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
                Toast.makeText(v.getContext(), "Failed to print stickers. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });


//        View rootLayout = findViewById(R.id.full_body);
//        rootLayout.setOnTouchListener((v, event) -> {
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                callBagAPI();;
//            }
//            return false;
//        });
//
//        _gsmET.setOnTouchListener((v, event) -> {
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                callBagAPI();;
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
        if (bagKeepingArrayList == null || bagKeepingArrayList.isEmpty()) {
            Log.w("PrintWarning", "Sticker list is empty. Nothing to print.");
            Toast.makeText(this, "No stickers to print.", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < bagKeepingArrayList.size(); i++) {
            try {
                printBluetooth(i, false);
            } catch (Exception e) {
                Log.e("PrintError", "Failed to print sticker at position " + i, e);
            }
        }

        Toast.makeText(this, "Stickers printed successfully.", Toast.LENGTH_SHORT).show();
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fabric, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            if(bagKeepingArrayList.size() > 0){
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
            save_obj.put("BATCH_NO", String.valueOf(bagKeepingArrayList.get(0).getBatchNo()));
            save_obj.put("BUYER_ID", String.valueOf(bagKeepingArrayList.get(0).getBuyerId()));
            save_obj.put("COMPANY_ID", String.valueOf(bagKeepingArrayList.get(0).getCompanyId()));

            for (int i = 0; i < bagKeepingArrayList.size(); i++) {
                JSONObject dtls_obj = new JSONObject();
                dtls_obj.put("BAG_NO", String.valueOf(bagKeepingArrayList.get(i).getBagNo()));
                dtls_obj.put("RFID_NO", String.valueOf(bagKeepingArrayList.get(i).getRfidNo()));
                dtls_obj.put("QR_NO", String.valueOf(bagKeepingArrayList.get(i).getQrNo()));
                dtls_obj.put("BATCH_NO", String.valueOf(bagKeepingArrayList.get(i).getBatchNo()));
                dtls_obj.put("WEIGHT", String.valueOf(bagKeepingArrayList.get(i).getFinishWeight()));
                dtls_obj.put("GREY_WEIGHT", String.valueOf(bagKeepingArrayList.get(i).getGreyWeight()));
                dtls_obj.put("INTERNAL_REF", String.valueOf(bagKeepingArrayList.get(i).getIrIb()));
                dtls_obj.put("FABRIC_COLOR_ID", String.valueOf(bagKeepingArrayList.get(i).getFabColorId()));
                dtls_obj.put("BUYER_ID", String.valueOf(bagKeepingArrayList.get(i).getBuyerId()));
                dtls_obj.put("ROLL_QNTY", String.valueOf(bagKeepingArrayList.get(i).getRollQnty()));
                dtls_obj.put("COMPANY_ID", String.valueOf(bagKeepingArrayList.get(i).getCompanyId()));
                dtls_obj.put("DIA", String.valueOf(bagKeepingArrayList.get(i).getDia()));
                dtls_obj.put("GSM", String.valueOf(bagKeepingArrayList.get(i).getGsm()));
                dtls_obj.put("FABRIC_TYPE", bagKeepingArrayList.get(i).getFabType());
                dtls_obj.put("AOP", bagKeepingArrayList.get(i).getAop());
                dtls_obj.put("COLOR_ID", bagKeepingArrayList.get(i).getBagColorId());
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
        finishProductionViewModel.postBagKeepingResponse(body).observe(this, apiResponse -> {
            if(apiResponse != null) {
                DialogHelper.showSuccessDialog(this, "Success", apiResponse.getMsg());
                responseSystemNumber = apiResponse.getSysNumber();
                refreshData(2);
                _saveBtn.setEnabled(false);
                fetchBagKeepingQCData(responseSystemNumber, "", 0);
            }else{
                DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchBagKeepingQCData(String systemScan, String bagScan, Integer selectedColorId) {
        progressBarState();
        finishProductionViewModel.getBagKeepingBySystemResponse(systemScan, bagScan, selectedColorId).observe(this, apiResponse -> {
            if(apiResponse!= null){
                if(apiResponse.getResultSet() != null && !apiResponse.getStatusCode().equals("400")) {
                    try {
                        bagKeepingDataByPackingListArrayList = apiResponse.getResultSet();
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
    private void refreshData(int statusCode) {
        if(statusCode == 1){
//            _bagNoScanTV.setText("");
            _bagNoScanET.setText("");
            _batchScanTV.setText("");
            _rollWeightET.setText("");
            _gsmET.setText("");
            _diaET.setText("");
            _greyWeightET.setText("");
            _greyTotalTV.setText("");
            _noOfBagTV.setText("");
            _finishTotalTV.setText("");
            _plParcentageTV.setText("");

            _batchPrintBT.setVisibility(View.GONE);
            bagKeepingArrayList.clear();
            bagKeepingRecyclerViewAdapter.notifyDataSetChanged();
        }else{
            if(bagKeepingArrayList.size() > 0){
                for(V1_BagKeepingResponse.ResultSet item: bagKeepingArrayList){
                    item.setSaveStatus(true);
                }
            }
            _batchPrintBT.setVisibility(View.VISIBLE);
            bagKeepingRecyclerViewAdapter.notifyDataSetChanged();
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
                .setTitleText("Confirm Exit")
                .setContentText("Are you sure you want to leave this page? Unsaved changes will be lost.")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener(dialog -> {
                    dialog.dismissWithAnimation();
                    finish();
                    overridePendingTransition(0, 0);
                })
                .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                .show();
    }



    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onRemoveHeadClick(int position, View v) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Warning!")
                .setContentText("আপনি কি এই ব্যাগটি বাদ দিতে চান?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener(sDialog -> {
                    sDialog.cancel();
                    bagKeepingArrayList.remove(position);
                    calculateTotalValue();
                    bagKeepingRecyclerViewAdapter.notifyDataSetChanged();
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
            case R.id.batchScan:
                String _batchNo = _batchScanTV.getText().toString().trim();
                if (_batchNo.isEmpty()) {
                    startScanning(2);
                } else {
                    DialogHelper.showWarningDialog(this, "Warning", "ব্যাচ পরিবর্তন করা যাবে না । প্রথমে রিফ্রেশ করুন ।");
                }

                break;
            case R.id.bagNoScan:
                String _batch = _batchScanTV.getText().toString().trim();
                String _weight = _rollWeightET.getText().toString().trim();
                if (!_batch.isEmpty() && !_weight.isEmpty() && selectedColorId != 0) {
                    String _bag = _bagNoScanET.getText().toString().trim();
                    if(_bag.isEmpty()){
                        startScanning(1);
                    }else{
                        fetchBagKeepingData(_bag, batchScan);
                    }
                } else {
                    DialogHelper.showWarningDialog(this, "Warning", "অনুগ্রহ করে ওজন এবং ব্যাগের রঙ যোগ করুন ।");
                }
                break;
            case R.id.printerLayout:
                browseBluetoothDevice();
                break;
            case R.id.saveBtn:
                if(bagKeepingArrayList.size() > 0){
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
        debouncer.debounce(() -> {
            String _bag = _bagNoScanET.getText().toString().trim();

            if(!_bag.isEmpty() && selectedColorId != 0){
                fetchBagKeepingData(_bag, batchScan);
            } else {
                runOnUiThread(() -> DialogHelper.showWarningDialog(this, "Warning", "অনুগ্রহ করে ওজন এবং ব্যাগের রঙ যোগ করুন ।"));
                _bagNoScanET.setText("");
            }
        }, 2000);
    }

    private void startScanning(int op) {
        Intent intent = new Intent(this, V1_ScannerActivity.class);
        intent.putExtra("qc", "bag_keeping");
        intent.putExtra("scan_op", op);
        intent.putExtra("bag_keeping_data", bagKeepingArrayList);
        intent.putExtra("bagScan", _bagNoScanET.getText().toString());
        intent.putExtra("batch_scan", _batchScanTV.getText().toString());
        intent.putExtra("rollWeight", _rollWeightET.getText().toString());
        intent.putExtra("selectedFabricBagColor", selectedColorId);
        startActivity(intent);
        finish();
    }
}