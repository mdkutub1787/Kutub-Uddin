package com.logicsoftbd.lsl.ui.v_1_ui.production_operation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_OperationItemModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_OperationItemResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_OperationSaveEntryRequest;
import com.logicsoftbd.lsl.data.network.v1_model.V1_OperationSaveResponse;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_QRBarcodeScannerActivity;
import com.logicsoftbd.lsl.utils.ApiUtils;
import com.logicsoftbd.lsl.utils.VerticalSpacingItemDecorator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class V1_ProductionOperationActivity extends AppCompatActivity implements
        View.OnClickListener,
        V1_OperationRecyclerViewAdapter.OnEndHeadListener,
        V1_OperationRecyclerViewAdapter.OnCancelHeadListener {
    private static final String TAG = "V1_ProductionOperationA";
    TextView _companyNameTV, _locationTV, _floorTV, _lineTV, _bundleScanTV, _operationScanTV, _empIdScanTV, _operationCountTV, _operationScanCheckTV;
    Button _bundleScanBT, _operationScanBT, _empIdScanBT, _operationStartBT, _refreshBT, _operationScanCheckBT;
    ImageButton _bundleRefreshBT, _operationRefreshBT, _empIdRefreshBT;
    RecyclerView _operationRecyclerView;
    ProgressDialog pDialog;
    androidx.appcompat.widget.SearchView searchView;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;

    Timer timer;
    TimerTask timerTask;
    Double time = 0.0;

    String bundleScan, operationScan, operationScanName, operationScanId, libOperationScanId, empIdScan, runningOperation, ws_id;
    private int userID, companyId = 0, locationId = 0, floorId = 0, lineId = 0, search_Op = 0;
    private String base_url = "", username = "", password = "", urladdress = "", companyName = "", locationName = "", floorName = "", lineName = "";

    private String[] scanList = new String[3];

    private ApiInterface apiInterface;
    private ApiUtils apiUtils;
    private ArrayList<V1_OperationItemModel> operationItemModelsList = new ArrayList<>();
    private ArrayList<V1_OperationItemModel>  dataList = new ArrayList<>();
    private V1_OperationRecyclerViewAdapter v1_operationRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production_operation);

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        companyId = (_preferences.getInt("company", 0));
        locationId = (_preferences.getInt("location", 0));
        lineId = (_preferences.getInt("line", 0));
        floorId = (_preferences.getInt("floor", 0));
        companyName = (_preferences.getString("companyName", ""));
        locationName = (_preferences.getString("locationName", ""));
        lineName = (_preferences.getString("lineName", ""));
        floorName = (_preferences.getString("floorName", ""));
        base_url = (_preferences.getString("base_url", ""));

        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);

//        timer = new Timer();
        init_ui();
        initRecyclerView();
        getDefaultData();

    }

    @SuppressLint("LongLogTag")
    private void getDefaultData() {
        if(companyName != null && locationName != null && floorName != null && lineName != null){
            _companyNameTV.setText(companyName);
            _locationTV.setText(locationName);
            _floorTV.setText(floorName);
            _lineTV.setText(lineName);
        }

        Intent intent = getIntent();
        bundleScan = intent.getStringExtra("bundleScan");
        operationScan = intent.getStringExtra("operationScan");
        empIdScan = intent.getStringExtra("empIdScan");
        runningOperation = intent.getStringExtra("runningOperation");
        Log.d(TAG, "getDefaultData: " + bundleScan +" : "+operationScan+" : "+empIdScan+" :");

        if(operationScan != null){
            String[] operationsArray = operationScan.split("_");
            Log.d(TAG, "getDefaultData: "+operationScan);
            operationScanName = operationsArray[operationsArray.length-1];

            if(operationsArray.length > 4){
                ws_id = operationsArray[0];
                operationScanId = operationsArray[4];
                libOperationScanId = operationsArray[8];
                operationScanName = operationsArray[operationsArray.length-1];
            }
            Log.d(TAG, "getDefaultData:  operationScanId "+operationScanId+": "+libOperationScanId);
        }

        if(runningOperation != null){
            String[] operationsArray = runningOperation.split("_");
            runningOperation = operationsArray[4];
        }

        search_Op = intent.getIntExtra("op", 0);
        dataList = (ArrayList<V1_OperationItemModel>) intent.getSerializableExtra("data");

        if(dataList != null){
            operationItemModelsList = dataList;
            initRecyclerView();
//            v1_operationRecyclerViewAdapter.notifyDataSetChanged();
            operationCount();
        }

        Log.d(TAG, "getDefaultData: "+dataList);
//        operationItemModelsList = dataList;

        if(bundleScan != null){
            _bundleScanTV.setText(bundleScan);
        }
        if(operationScan != null){
            _operationScanTV.setText(operationScanName);
        }
        if(empIdScan != null){
            _empIdScanTV.setText(empIdScan);
        }

        if(search_Op  == 3){
            _operationScanCheckTV.setText(runningOperation);
            _operationScanTV.setText("");
            boolean status = false;
            for(int i=0; i<operationItemModelsList.size(); i++){
                if(operationItemModelsList.get(i).getOperation_id().equals(runningOperation)  || operationItemModelsList.get(i).getEmp_id().equals(runningOperation)){
                    operationItemModelsList.get(i).setIs_selected(true);
                    status = true;
                    Collections.swap(operationItemModelsList, 0, i);
                    initRecyclerView();
                    v1_operationRecyclerViewAdapter.notifyDataSetChanged();
                    break;
                }else{
                    status = false;
                    operationItemModelsList.get(i).setIs_selected(false);
                    v1_operationRecyclerViewAdapter.notifyDataSetChanged();
                }
            }
            if(status == false){
                showAlertMessage("Any Operation not found for this Employee.");
            }
        }
    }

    private void  getRequestForOperationData() {
        showDialog();
        apiInterface.getSewingOperationModelClassCall(_bundleScanTV.getText().toString(), libOperationScanId, _empIdScanTV.getText().toString().trim()).enqueue(new Callback<V1_OperationItemResponse>() {
            @Override
            public void onResponse(Call<V1_OperationItemResponse> call, Response<V1_OperationItemResponse> response) {
                hideDialog();
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful()){
                    String message = response.body().getResultset().getMessageBng();
                    if(message.equals("")){
                        V1_OperationItemModel v1OperationItemModel = new V1_OperationItemModel();
                        v1OperationItemModel.setEmp_id(_empIdScanTV.getText().toString());
//                        v1OperationItemModel.setOperation_id(operationScanId);
                        v1OperationItemModel.setOperation_id(libOperationScanId);
                        v1OperationItemModel.setOperator_id(_empIdScanTV.getText().toString().trim());
                        v1OperationItemModel.setOperation_name(operationScanName);
                        v1OperationItemModel.setBundle_id(_bundleScanTV.getText().toString());
                        v1OperationItemModel.setBundleNo(response.body().getResultset().getBundleNo());
                        v1OperationItemModel.setBarcodeNo(response.body().getResultset().getBarcodeNo());
                        v1OperationItemModel.setYear(response.body().getResultset().getYear());
                        v1OperationItemModel.setColorSizeId(response.body().getResultset().getColorSizeId());
                        v1OperationItemModel.setOrderId(response.body().getResultset().getOrderId());
                        v1OperationItemModel.setItemId(response.body().getResultset().getItemId());
                        v1OperationItemModel.setQty(response.body().getResultset().getQty());
                        v1OperationItemModel.setCountryId(response.body().getResultset().getCountryId());
                        v1OperationItemModel.setSizeId(response.body().getResultset().getSizeId());
                        v1OperationItemModel.setColorId(response.body().getResultset().getColorId());
                        v1OperationItemModel.setCutNo(response.body().getResultset().getCutNo());
                        v1OperationItemModel.setJobNo(response.body().getResultset().getJobNo());
                        v1OperationItemModel.setJobId(response.body().getResultset().getJobId());
                        v1OperationItemModel.setBuyerName(response.body().getResultset().getBuyerName());
                        v1OperationItemModel.setBuyerId(response.body().getResultset().getBuyerId());
                        v1OperationItemModel.setOrderNo(response.body().getResultset().getOrderNo());
                        v1OperationItemModel.setOrderId(response.body().getResultset().getOrderId());
                        v1OperationItemModel.setItem(response.body().getResultset().getItem());
                        v1OperationItemModel.setCountry(response.body().getResultset().getCountry());
                        v1OperationItemModel.setColor(response.body().getResultset().getColor());
                        v1OperationItemModel.setSize(response.body().getResultset().getSize());
                        v1OperationItemModel.setStart_date(currentDate());
                        v1OperationItemModel.setWs_id(ws_id);
                        v1OperationItemModel.setIs_selected(false);

                        operationItemModelsList.add(v1OperationItemModel);
                        operationCount();
//                        initRecyclerView();
                        time = 12.0;
//                        startTimer();
                        v1_operationRecyclerViewAdapter.notifyDataSetChanged();
                        refreshData(1);
                    }else{
                        Toast.makeText(V1_ProductionOperationActivity.this, response.body().getResultset().getMessageBng(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<V1_OperationItemResponse> call, Throwable t) {
                hideDialog();
            }
        });
    }

    private void operationCount() {
        _operationCountTV.setText(operationItemModelsList.size()+" Operation(s) Running");
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _operationRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        _operationRecyclerView.addItemDecoration(itemDecorator);
        v1_operationRecyclerViewAdapter = new V1_OperationRecyclerViewAdapter(operationItemModelsList, this, this, getApplicationContext());
        _operationRecyclerView.setAdapter(v1_operationRecyclerViewAdapter);
//        _operationRecyclerView.notify();
    }

    private String currentDate(){
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy hh:mm:ss");
        String currentDate = simpleDateFormat.format(calendar.getTime());
        return currentDate;
    }

    private void init_ui() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        _companyNameTV = findViewById(R.id.companyNameTV);
        _locationTV = findViewById(R.id.locationTV);
        _floorTV = findViewById(R.id.floorTV);
        _lineTV = findViewById(R.id.lineTV);
        _bundleScanTV = findViewById(R.id.bundleScanTV);
        _operationScanTV = findViewById(R.id.operationScanTV);
        _empIdScanTV = findViewById(R.id.empIdScanTV);
        _operationCountTV = findViewById(R.id.operationCountTV);
        _operationScanCheckTV = findViewById(R.id.operationScanCheckTV);
        _bundleScanBT = findViewById(R.id.bundleScanBT);
        _bundleScanBT.setOnClickListener(this);
        _operationScanBT = findViewById(R.id.operationScanBT);
        _operationScanBT.setOnClickListener(this);
        _empIdScanBT = findViewById(R.id.empIdScanBT);
        _empIdScanBT.setOnClickListener(this);
        _operationStartBT = findViewById(R.id.operationStartBT);
        _operationStartBT.setOnClickListener(this);
        _refreshBT = findViewById(R.id.refreshBT);
        _refreshBT.setOnClickListener(this);
        _bundleRefreshBT = findViewById(R.id.bundleRefreshBT);
        _bundleRefreshBT.setOnClickListener(this);
        _operationRefreshBT = findViewById(R.id.operationRefreshBT);
        _operationRefreshBT.setOnClickListener(this);
        _empIdRefreshBT = findViewById(R.id.empIdRefreshBT);
        _empIdRefreshBT.setOnClickListener(this);
        _operationScanCheckBT = findViewById(R.id.operationScanCheckBT);
        _operationScanCheckBT.setOnClickListener(this);
        _operationRecyclerView = findViewById(R.id.operationRecyclerView);
    }

    private void startScanning(int op) {
        Intent intent = new Intent(this, V1_QRBarcodeScannerActivity.class);
        intent.putExtra("qc", "operations");
        intent.putExtra("scan_op", op);
        intent.putExtra("bundleScan", _bundleScanTV.getText().toString());
//        intent.putExtra("operationScan", _operationScanTV.getText().toString());
        intent.putExtra("operationScan", operationScan);
        intent.putExtra("empIdScan", _empIdScanTV.getText().toString());
        intent.putExtra("data", operationItemModelsList);
        startActivity(intent);
        finish();
    }

    private String getTimerText()
    {
        int rounded = (int) Math.round(time);

        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) / 3600);

        return formatTime(seconds, minutes, hours);
    }

    private String formatTime(int seconds, int minutes, int hours)
    {
        return String.format("%02d",hours) + ":" + String.format("%02d",minutes) + ":" + String.format("%02d",seconds);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bundleScanBT:
                if(_bundleScanTV.getText().toString().isEmpty())
                    startScanning(0);
                else
                    showAlertMessage("Please Refresh Bundle Id first.");
                break;
            case R.id.operationScanBT:
                if(_operationScanTV.getText().toString().isEmpty())
                    startScanning(1);
                else
                    showAlertMessage("Please Refresh Operation first.");
                break;
            case R.id.empIdScanBT:
                if(_operationScanCheckTV.getText().toString().isEmpty())
                    startScanning(2);
                else
                    showAlertMessage("Please Refresh Employee Id first.");
                break;
            case R.id.operationScanCheckBT:
                if(_empIdScanTV.getText().toString().isEmpty())
                    startScanning(3);
                break;
            case R.id.bundleRefreshBT:
                _bundleScanTV.setText("");
                break;
            case R.id.operationRefreshBT:
                _operationScanTV.setText("");
                break;
            case R.id.empIdRefreshBT:
                _empIdScanTV.setText("");
                break;
            case R.id.refreshBT:
                refreshData(0);
                break;
            case R.id.operationStartBT:
                if(!_bundleScanTV.getText().toString().isEmpty()
                        && !_operationScanTV.getText().toString().isEmpty()
                        && !_empIdScanTV.getText().toString().isEmpty()) {
                    if(isMatch()){
                        showAlertMessage("Already scanned and operation is running.");
                    } else {
                        getRequestForOperationData();
                    }

                } else{
                    showAlertMessage("Please scan all credentials.");
                }
                break;
        }
    }

    private boolean isMatch(){
        for(int i=0; i<operationItemModelsList.size(); i++){
            if(operationItemModelsList.get(i).getBundle_id().equals(_bundleScanTV.getText().toString()) && operationItemModelsList.get(i).getEmp_id().equals(_empIdScanTV.getText().toString()) && operationItemModelsList.get(i).getOperation_id().equals(libOperationScanId)){
                return true;
            }
        }
        return false;
    }

    private void refreshData(int i) {
        if(i == 0){
            _bundleScanTV.setText("");
            _operationScanTV.setText("");
            _empIdScanTV.setText("");
            bundleScan = "";
            operationScan = "";
            empIdScan = "";
        }else {
            _bundleScanTV.setText("");
//            _operationScanTV.setText("");
//            _empIdScanTV.setText("");
//            bundleScan = "";
//            operationScan = "";
//            empIdScan = "";
        }

    }

    @Override
    public void onEndHeadClick(int position, View v) {
        String bundle_id = operationItemModelsList.get(position).getBundle_id();
        String operation_id = operationItemModelsList.get(position).getOperation_id();
        String emp_id = operationItemModelsList.get(position).getBundle_id();

        V1_OperationSaveEntryRequest operationSaveEntryRequest = new V1_OperationSaveEntryRequest();
        operationSaveEntryRequest.setCompanyId(companyId);
        operationSaveEntryRequest.setCutNo(operationItemModelsList.get(position).getCutNo());
        operationSaveEntryRequest.setItemId(Integer.parseInt(operationItemModelsList.get(position).getItemId()));
        operationSaveEntryRequest.setQty(Integer.parseInt(operationItemModelsList.get(position).getQty()));
        operationSaveEntryRequest.setBundleNo(operationItemModelsList.get(position).getBundleNo());
        operationSaveEntryRequest.setBarcodeNo(operationItemModelsList.get(position).getBarcodeNo());
        operationSaveEntryRequest.setColorSizeId(Integer.parseInt(operationItemModelsList.get(position).getColorSizeId()));
        operationSaveEntryRequest.setColorId(Integer.parseInt(operationItemModelsList.get(position).getColorId()));
        operationSaveEntryRequest.setSizeId(Integer.parseInt(operationItemModelsList.get(position).getSizeId()));
        operationSaveEntryRequest.setOrderId(Integer.parseInt(operationItemModelsList.get(position).getOrderId()));
        operationSaveEntryRequest.setJobId(Integer.parseInt(operationItemModelsList.get(position).getJobId()));
        operationSaveEntryRequest.setJobNo(operationItemModelsList.get(position).getJobNo());
        operationSaveEntryRequest.setBuyerId(Integer.parseInt(operationItemModelsList.get(position).getBuyerId()));
        operationSaveEntryRequest.setCountryId(Integer.parseInt(operationItemModelsList.get(position).getCountryId()));
        operationSaveEntryRequest.setOperatorId(operationItemModelsList.get(position).getOperator_id());
        operationSaveEntryRequest.setLibOperationId(Integer.valueOf(operation_id));
        operationSaveEntryRequest.setOperationStart(String.valueOf(operationItemModelsList.get(position).getStart_date()));
        operationSaveEntryRequest.setOperationEnd(currentDate());
        operationSaveEntryRequest.setLineId(lineId);
        operationSaveEntryRequest.setWsId(Integer.parseInt(operationItemModelsList.get(position).getWs_id()));

        Log.d(TAG, "onEndHeadClick: "+lineId+ ":"+ operationItemModelsList.get(position).getWs_id());
        Log.e("json", "json" + new Gson().toJson(operationSaveEntryRequest));
        showDialog();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, new Gson().toJson(operationSaveEntryRequest));
        apiInterface.saveOperationInputCall(body).enqueue(new Callback<V1_OperationSaveResponse>() {
            @Override
            public void onResponse(Call<V1_OperationSaveResponse> call, Response<V1_OperationSaveResponse> response) {
                hideDialog();
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful()){
                    showAlertMessage("Bundle Id: "+bundle_id+", \nEmployee Id: "+emp_id+"\nand Operation Id: "+operation_id+ " \nis successfully saved");
                }
                if(operationItemModelsList.get(position).getIs_selected()){
                    _operationScanCheckTV.setText("");
                    search_Op = 0;
                }

                if(operationItemModelsList.get(position).getIs_selected()){
                    _operationScanCheckTV.setText("");
                    search_Op = 0;
                    operationItemModelsList.get(0).setIs_selected(false);
                }
                operationItemModelsList.remove(position);
                initRecyclerView();
                v1_operationRecyclerViewAdapter.notifyDataSetChanged();
                operationCount();
            }

            @Override
            public void onFailure(Call<V1_OperationSaveResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                hideDialog();
            }
        });



    }

//    private void showAlertMessage(String msg) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(V1_ProductionOperationActivity.this);
//        builder.setTitle("Message")
//                .setMessage(msg)
//                .setCancelable(false)
//                .setPositiveButton("Ok", (dialog, which) -> {
//
//                });
//        AlertDialog dialog  = builder.create();
//        dialog.show();
//    }

    private void showAlertMessage(String msg){
        ImageView cancel;
        Button updateBtn;
        TextView messageTV, titileTV;

        View alertCustomDialog = LayoutInflater.from(this).inflate(R.layout.custom_update_alert_layout,null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setView(alertCustomDialog);
        cancel = alertCustomDialog.findViewById(R.id.cancel_button);
        updateBtn = alertCustomDialog.findViewById(R.id.btnUpdate);
        messageTV = alertCustomDialog.findViewById(R.id.messageTV);
        titileTV = alertCustomDialog.findViewById(R.id.titileTV);
        titileTV.setText("Message");
        updateBtn.setText("Okay");

        messageTV.setText(msg);
        final AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
        dialog.setCancelable(false);

        updateBtn.setOnClickListener(v -> {
            dialog.dismiss();
        });

        cancel.setOnClickListener( v -> {
            dialog.dismiss();
        });
    }

    @Override
    public void onCancelHeadClick(int position, View v) {
        if(operationItemModelsList.get(position).getIs_selected()){
            _operationScanCheckTV.setText("");
            search_Op = 0;
            operationItemModelsList.get(0).setIs_selected(false);
        }
        String bundle_id = operationItemModelsList.get(position).getBundle_id();
        String operation_id = operationItemModelsList.get(position).getBundle_id();
        String emp_id = operationItemModelsList.get(position).getBundle_id();
        operationItemModelsList.remove(position);
        showAlertMessage("Bundle Id: "+bundle_id+", \nEmployee Id: "+emp_id+"\nand Operation Id: "+operation_id+ " \nis successfully removed");
        initRecyclerView();
        v1_operationRecyclerViewAdapter.notifyDataSetChanged();
        operationCount();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void showDialog() {
        if(!pDialog.isShowing()){
            pDialog.show();
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    @Override
    public void onBackPressed() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Exits");
        builder.setMessage("Do you want to exit?");
        builder.setPositiveButton("Yes", (dialog, id) -> {
            finish();
        });
        builder.show();
    }
}
