package com.logicsoftbd.lsl.ui.v_1_ui.finish_fabric.finish_fabric_issue_old;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.model.FinishFabricIssueSet;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRollIssueResponses;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRollReceive;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRollReceiveRequest;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.v_1_ui.finish_fabric.finish_fabric_receive_old.V1_FinishReceiveRecyclerAdapter;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_ScannerActivity;
import com.logicsoftbd.lsl.utils.ApiUtils;
import com.logicsoftbd.lsl.utils.VerticalSpacingItemDecorator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringJoiner;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class V1_FinishFabricIssueActivity extends AppCompatActivity implements View.OnClickListener, V1_FinishReceiveRecyclerAdapter.OnDeleteSelectListener {
    private static final String TAG = "V1_FinishFabricReceiveA";
    private ProgressDialog _pdialog;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private DatePickerDialog datePickerDialog;
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;

    private TextView _roomRackScanTV, _barcodeScanTV, _challanScanTV, _receiveDateTV, _receiveIdTV, _totalWeightTV;
    private Button _roomRackScanBT, _barcodeScanBT, _challanScanBT, _submitBT, _saveBT, _refreshBT;
    private ImageButton _roomRackRefreshBT, _barcodeRefreshBT, _challanRefreshBT;
    private RecyclerView _finish_receiveRollReceiveRecyclerView;
    private V1_FinishReceiveRecyclerAdapter finishReceiveRecyclerAdapter;
    private ArrayList<FinishFabricRollReceive.DetailsSet> finishFabricReceiveItemModels = new ArrayList<>();
    private ArrayList<FinishFabricRollReceive.DetailsSet> dataList = new ArrayList<>();
    FinishFabricRollReceiveRequest FinishFabricRollReceiveRequest = new FinishFabricRollReceiveRequest();
    private String base_url = "", finish_receive_roll_barcodeScan, currentDate, user_id, delivery_number, room_rack, finish_receive_room_rack_scan, finish_receive_barcode_scan, finish_receive_challan_scan;
    private int finish_receive_scan_op = 0, company_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_finish_fabric_issue);

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = _preferences.getString("login_userid", "");
        base_url = (_preferences.getString("base_url", ""));

        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);

        init_ui();
        initRecyclerView();
        getDefaultData();
    }
    private void getDefaultData() {
        Intent intent = getIntent();
        finish_receive_roll_barcodeScan = intent.getStringExtra("finish_roll_barcodeScan");
        finish_receive_scan_op = intent.getIntExtra("scan_op", 0);
        if(finish_receive_scan_op != 1){
            finish_receive_room_rack_scan = intent.getStringExtra("room_rack_scan");
        }else {
            finish_receive_room_rack_scan = intent.getStringExtra("finish_roll_barcodeScan");
        }
        finish_receive_barcode_scan = intent.getStringExtra("barcode_scan");
        finish_receive_challan_scan = intent.getStringExtra("challan_scan");
        dataList = (ArrayList<FinishFabricRollReceive.DetailsSet>) intent.getSerializableExtra("finish_receive_roll_data");

        _barcodeScanTV.setText(finish_receive_barcode_scan);
        _challanScanTV.setText(finish_receive_challan_scan);


        if(dataList != null){
            finishFabricReceiveItemModels = dataList;
            initRecyclerView();
        }

        if(finish_receive_room_rack_scan != null || finish_receive_scan_op == 1){
//            assert finish_receive_room_rack_scan != null;
            String[] roomRackArray = finish_receive_room_rack_scan.split(Pattern.quote("***"));
//            String[] roomRackArray = finish_receive_room_rack_scan.split(Pattern.quote("-"));  //palmal
//            String[] roomRackArray = finish_receive_room_rack_scan.split("-", 2); // Limit to 2 parts

            if(roomRackArray.length >= 1){
                _roomRackScanTV.setText(String.valueOf(roomRackArray[1]));
                room_rack = roomRackArray[0];
            }else {
                room_rack = roomRackArray[0];
                _roomRackScanTV.setText(String.valueOf(roomRackArray[0]));
            }
        }

        if(finish_receive_roll_barcodeScan != null && finish_receive_scan_op == 2){
            _barcodeScanTV.setText(finish_receive_roll_barcodeScan);
            requestForBarcode(finish_receive_roll_barcodeScan);
        }
        if(finish_receive_roll_barcodeScan != null && finish_receive_scan_op == 3){
            _challanScanTV.setText(finish_receive_roll_barcodeScan);
            requestForChallan(finish_receive_roll_barcodeScan);
        }
    }

    private void requestForChallan(String challanScan) {
        _pdialog.show();
        apiInterface.getFinishFabricReceiveCall(challanScan).enqueue(new Callback<FinishFabricRollReceive>() {
            @Override
            public void onResponse(@NonNull Call<FinishFabricRollReceive> call, @NonNull Response<FinishFabricRollReceive> response) {
                _pdialog.dismiss();
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body().getData() != null && response.body().getData().size() > 0){
                            finishFabricReceiveItemModels = response.body().getData();
                            initRecyclerView();
                    }else{
                        _challanScanTV.setText("");
                        showAlertMessage("Data not found.", 0, 0);
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<FinishFabricRollReceive> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                _pdialog.dismiss();
            }
        });
    }

    private void requestForBarcode(String barcodeScan) {
        _pdialog.show();
        apiInterface.getFinishFabricIssueCall(barcodeScan).enqueue(new Callback<FinishFabricIssueSet>() {
            @Override
            public void onResponse(@NonNull Call<FinishFabricIssueSet> call, @NonNull Response<FinishFabricIssueSet> response) {
                _pdialog.dismiss();
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful()){
                    if (response.body() != null) {

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FinishFabricIssueSet> call, @NonNull Throwable t) {
                _pdialog.dismiss();
            }
        });
    }

    private void postDataToServer() {

        extractFormData();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, new Gson().toJson(FinishFabricRollReceiveRequest));

        _pdialog.show();
        apiInterface.saveFinishBarcodeReceiveCall(body).enqueue(new Callback<FinishFabricRollIssueResponses>() {
            @Override
            public void onResponse(Call<FinishFabricRollIssueResponses> call, Response<FinishFabricRollIssueResponses> response) {
                _pdialog.hide();
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful()){
                    refreshData(3);
                    showAlertMessage(response.body().getData().getMsg(), 0, 0);
                }
            }

            @Override
            public void onFailure(Call<FinishFabricRollIssueResponses> call, Throwable t) {
                _pdialog.hide();
                showAlertMessage("Failed, Please try again.", 0, 0);
            }
        });
    }

    private void extractFormData() {
        FinishFabricRollReceiveRequest.Result result = new FinishFabricRollReceiveRequest.Result();
        FinishFabricRollReceiveRequest.MasterPart masterPart = new FinishFabricRollReceiveRequest.MasterPart();
        masterPart.setCOMPANY_ID(1);
        masterPart.setCHALLAN_NO(_challanScanTV.getText().toString());
        masterPart.setRECV_DATE(currentDate);
        masterPart.setSTORE_ID(room_rack);
        masterPart.setLOCATION_ID(String.valueOf(room_rack));
        masterPart.setINSERTED_BY(Integer.valueOf(user_id));
        ArrayList<String> barcodeString = new ArrayList<>();

        ArrayList<FinishFabricRollReceiveRequest.DetailsPart> finisList = new ArrayList<>();
        for (FinishFabricRollReceive.DetailsSet detailsSet : finishFabricReceiveItemModels) {
            barcodeString.add(detailsSet.getPROD_ID());
            FinishFabricRollReceiveRequest.DetailsPart detailsPart = new FinishFabricRollReceiveRequest.DetailsPart();
            detailsPart.setBARCODE_NO(detailsSet.getBARCODE_NO());
            detailsPart.setBATCH_ID(detailsSet.getBATCH_ID());
            detailsPart.setBOOKING_NO(detailsSet.getBOOKING_NO());
            detailsPart.setBOOKING_WITHOUT_ORDER(detailsSet.getBOOKING_WITHOUT_ORDER());
            detailsPart.setCOMPANY_ID("1");
            detailsPart.setPROD_ID(detailsSet.getPROD_ID());
            detailsPart.setPROD_ID(detailsSet.getPROD_ID());
            detailsPart.setBODYPART_ID(detailsSet.getBODY_PART_ID());
            detailsPart.setCOLOR_ID(detailsSet.getCOLOR_ID());
            detailsPart.setCOLOR_NAME(detailsSet.getCOLOR_NAME());
            detailsPart.setPO_ID(detailsSet.getPO_ID());
            detailsPart.setITEM_CATEGORY("2");
            detailsPart.setTRANSACTION_TYPE("2");
            detailsPart.setDETERMINATION_ID(detailsSet.getDETERMINATION_ID());
            detailsPart.setCONS_QUANTITY(String.valueOf(detailsSet.getQC_PASS_QNTY()));
            detailsPart.setGSM(detailsSet.getGSM());
            detailsPart.setDIA(detailsSet.getDIA_WIDTH_TYPE());
            detailsPart.setROLL_ID(detailsSet.getROLL_ID());
            detailsPart.setROLL_NO(detailsSet.getROLL_NO());
            detailsPart.setCONS_QUANTITY(detailsSet.getQNTY());
            detailsPart.setCURRENT_WEIGHT(detailsSet.getQNTY());
            detailsPart.setREJECT_QNTY(detailsSet.getREJECT_QNTY());
            detailsPart.setGREY_RATE(detailsSet.getGREY_RATE());
            detailsPart.setDYEING_CHARGE(detailsSet.getDYEING_CHARGE());
            detailsPart.setREPROCESS(detailsSet.getREPROCESS());
            detailsPart.setPREV_REPROCESS(detailsSet.getPREV_REPROCESS());
            detailsPart.setINSERTED_BY(user_id);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            Date date = new Date(System.currentTimeMillis());
            String currentDate = formatter.format(date);
            detailsPart.setINSERT_DATE(currentDate);
            detailsPart.setTRANSACTION_DATE(currentDate);
            finisList.add(detailsPart);
        }
        StringJoiner sj = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sj = new StringJoiner(",");
        }
        for (String s : barcodeString) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                sj.add(s);
            }
        }
        masterPart.setPRODUCT_IDS(sj.toString());
        result.setDetailsPart(finisList);
        result.setMasterPart(masterPart);
        FinishFabricRollReceiveRequest.setData(result);
        FinishFabricRollReceiveRequest.setStatus("true");
        Log.e("data", "data" + new Gson().toJson(FinishFabricRollReceiveRequest));
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _finish_receiveRollReceiveRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(2);
        _finish_receiveRollReceiveRecyclerView.addItemDecoration(itemDecorator);
        finishReceiveRecyclerAdapter = new V1_FinishReceiveRecyclerAdapter(finishFabricReceiveItemModels, this, this);
        _finish_receiveRollReceiveRecyclerView.setAdapter(finishReceiveRecyclerAdapter);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(false);
        layoutManager.setStackFromEnd(false); // This ensures that the last item stays at the bottom
        _finish_receiveRollReceiveRecyclerView.setLayoutManager(layoutManager);
    }

    private void init_ui() {
        _pdialog = new ProgressDialog(this);
        _pdialog.setMessage("Loading...");
        _pdialog.setCancelable(false);
        _finish_receiveRollReceiveRecyclerView = findViewById(R.id.finishReceiveRecyclerView);
        _roomRackScanTV = findViewById(R.id.roomRackScanTV);
        _barcodeScanTV = findViewById(R.id.barcodeScanTV);
        _challanScanTV = findViewById(R.id.challanScanTV);
        _totalWeightTV = findViewById(R.id.totalWeightTV);
        _receiveDateTV = findViewById(R.id.receiveDateTV);
        _roomRackScanBT = findViewById(R.id.roomRackScanBT);
        _roomRackScanBT.setOnClickListener(this);
        _barcodeScanBT = findViewById(R.id.barcodeScanBT);
        _barcodeScanBT.setOnClickListener(this);
        _challanScanBT = findViewById(R.id.challanScanBT);
        _challanScanBT.setOnClickListener(this);
        _submitBT = findViewById(R.id.submitBT);
        _submitBT.setOnClickListener(this);
        _saveBT = findViewById(R.id.saveBT);
        _saveBT.setOnClickListener(this);
        _refreshBT = findViewById(R.id.refreshBT);
        _refreshBT.setOnClickListener(this);
        _roomRackRefreshBT = findViewById(R.id.roomRackRefreshBT);
        _roomRackRefreshBT.setOnClickListener(this);
        _barcodeRefreshBT = findViewById(R.id.barcodeRefreshBT);
        _barcodeRefreshBT.setOnClickListener(this);
        _challanRefreshBT = findViewById(R.id.challanRefreshBT);
        _challanRefreshBT.setOnClickListener(this);

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = simpleDateFormat.format(calendar.getTime());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.roomRackScanBT:
                if(_roomRackScanTV.getText().toString().isEmpty())
                    startScanning(1, 1);
                else
                    showAlertMessage("Please Refresh Room/Rack/Self first.", 0, 0);
                break;
            case R.id.barcodeScanBT:
                if(_barcodeScanTV.getText().toString().isEmpty()){
                    String ro_rac = _roomRackScanTV.getText().toString().trim();
                    if(!ro_rac.isEmpty()){
                        startScanning(2, 2);
                    }else {
                        showAlertMessage("Please scan Room/Rack/Shelf/Bin first", 0, 0);
                    }
                }
                else
                    showAlertMessage("Please Refresh Barcode first.", 0, 0);
                break;
            case R.id.challanScanBT:
                if(_challanScanTV.getText().toString().isEmpty()){
                    String ro_rac = _roomRackScanTV.getText().toString().trim();
                    if(!ro_rac.isEmpty()){
                        refreshData(2);
                        startScanning(3, 3);
                    }else {
                        showAlertMessage("Please scan Room/Rack/Shelf/Bin first", 0, 0);
                    }
                }
                else
                    showAlertMessage("Please Refresh Challan first.", 0, 0);
                break;
            case R.id.roomRackRefreshBT:
                _roomRackScanTV.setText("");
                break;
            case R.id.barcodeRefreshBT:
                _barcodeScanTV.setText("");
                break;
            case R.id.challanRefreshBT:
                refreshData(2);
                break;
            case R.id.refreshBT:
                refreshData(3);
                break;
            case R.id.saveBT:
                if(finishFabricReceiveItemModels.size() > 0){
                    postDataToServer();
                }
                break;
            case R.id.receiveDateTV:
                datepicker();
                break;
        }
    }

    private void datepicker() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    _receiveDateTV.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void refreshData(int status) {
        if(status == 3){
            finishFabricReceiveItemModels.clear();
            _roomRackScanTV.setText("");
            _barcodeScanTV.setText("");
            _challanScanTV.setText("");
            finishReceiveRecyclerAdapter.notifyDataSetChanged();
        }else if(status == 2){
            finishFabricReceiveItemModels.clear();
            _barcodeScanTV.setText("");
            _challanScanTV.setText("");
            finishReceiveRecyclerAdapter.notifyDataSetChanged();
        }

    }

    private void startScanning(int op, int finish_receive_roll_status) {
        Intent intent = new Intent(this, V1_ScannerActivity.class);
        intent.putExtra("qc", "finish_roll_receive_v1");
        intent.putExtra("scan_op", op);
        intent.putExtra("finish_receive_roll_status", finish_receive_roll_status);
        intent.putExtra("finish_receive_roll_data", finishFabricReceiveItemModels);
        intent.putExtra("room_rack_scan", finish_receive_room_rack_scan);
        intent.putExtra("barcode_scan", _barcodeScanTV.getText().toString());
        intent.putExtra("challan_scan", _challanScanTV.getText().toString());
        startActivity(intent);
    }

    private void showAlertMessage(String msg, int i, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(V1_FinishFabricIssueActivity.this);
        builder.setTitle("Message")
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("Ok", (dialog, which) -> {
                    if(i == 1){
                        finishFabricReceiveItemModels.remove(position);
                        finishReceiveRecyclerAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }else{
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog  = builder.create();
        dialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onDeleteClick(int position, View v) {
        showAlertMessage("Are you confirm to remove this barcode?", 1, position);
    }
}