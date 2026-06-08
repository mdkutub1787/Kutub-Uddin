package com.logicsoftbd.lsl.ui.v_1_ui.yarn_rfid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GMTFinishReceiveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_IssueReturnRFIDModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_YarnIssueReturnResponse;
import com.logicsoftbd.lsl.ui.v_1_ui.finish_fabric_roll_receive.V1_GmtFinishReceiveActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_ScannerActivity;
import com.logicsoftbd.lsl.utils.DialogHelper;
import com.logicsoftbd.lsl.viewModel.RFIDTransferViewModal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class V1_RFIDTagForYarnIssueReturnActivity extends AppCompatActivity implements View.OnClickListener, V1_YarnIssueReturnRecyclerViewAdapter.OnHeadListener, V1_YarnRFIDRecyclerViewAdapter.OnRemoveHeadListener {
    private static final String TAG = "V1_RFIDTagForYarnIssueR";
    private RFIDTransferViewModal rfidTransferViewModal;
    private ProgressBar _progressBar;
    private EditText _rollWeightET;
    private Button _issueChallanScanBT, _rfidScanBT;
    private TextView _issueChallanScanTV, _rfidScanTV;
    private RecyclerView _issueChallanRecyclerView, _rfidRecyclerView;
    private V1_YarnIssueReturnRecyclerViewAdapter yarnIssueReturnRecyclerViewAdapter;
    private V1_YarnRFIDRecyclerViewAdapter yarnRFIDRecyclerViewAdapter;
    private ArrayList<V1_YarnIssueReturnResponse.DtlsIssueDetail> dtlsIssueDetailArrayList = new ArrayList<>();
    private  V1_YarnIssueReturnResponse yarnIssueReturnResponse;
    private ArrayList<V1_IssueReturnRFIDModel> dtlsRFIDIssueArrayList = new ArrayList<>();
    private String issueChallanScan, rfidScan, userName, savedPrinter, defectName, defectId, currentDate, fgsm, mode, bagScan, bagNo, batchScan, rollWeight, noOfRoll;
    private Integer scan_op = 0, printPosition = 0;
    private Boolean isValidRFID = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_rfidtag_for_yarn_issue_return);

        rfidTransferViewModal = new ViewModelProvider(this).get(RFIDTransferViewModal.class);

        init_ui();
        getDefaultData();


//        requestDataForYarnIssueReturn();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getDefaultData() {
        Intent intent = getIntent();
        bagScan = intent.getStringExtra("barcodeScan");
        scan_op = intent.getIntExtra("scan_op", 0);
        if(scan_op == 1){
            issueChallanScan = intent.getStringExtra("barcodeScan");
            _issueChallanScanTV.setText(issueChallanScan);
        } else {
            issueChallanScan = intent.getStringExtra("issueChallanScan");
            rfidScan = intent.getStringExtra("barcodeScan");
            rollWeight = intent.getStringExtra("rollWeight");
        }
//        dtlsIssueDetailArrayList = (ArrayList<V1_YarnIssueReturnResponse.DtlsIssueDetail>) intent.getSerializableExtra("yarn_issue_return_data");
        yarnIssueReturnResponse = (V1_YarnIssueReturnResponse) intent.getSerializableExtra("yarn_issue_return_data");
        dtlsRFIDIssueArrayList = (ArrayList<V1_IssueReturnRFIDModel>) intent.getSerializableExtra("yarn_rfid_data");

        if(scan_op == 1 && issueChallanScan != null) {
            requestDataForYarnIssueReturn();
        }

        if(scan_op == 2 && rfidScan!= null && rollWeight!= null) {
            requestDataForYarnRFIDIssueReturn();
        };


        try {
            initYarnIssueRecyclerView();
            yarnRFIDRecyclerViewAdapter.notifyDataSetChanged();
            yarnIssueReturnRecyclerViewAdapter.notifyDataSetChanged();
        }catch (Exception e) {
            Log.d(TAG, "getDefaultData: ");
        }

    }



    @SuppressLint("NotifyDataSetChanged")
    private void requestDataForYarnIssueReturn() {
        progressBarState();
        rfidTransferViewModal.getYarnIssueReturnResponse(issueChallanScan).observe(this, apiResponse -> {
            if(apiResponse!= null){
                if(apiResponse.getData() != null && apiResponse.getData().getDtlsIssueDetails() != null && apiResponse.getData().getDtlsIssueDetails().size() > 0) {

                    try {
                        yarnIssueReturnResponse = apiResponse;
                        dtlsIssueDetailArrayList = (ArrayList<V1_YarnIssueReturnResponse.DtlsIssueDetail>) apiResponse.getData().getDtlsIssueDetails();
                        for(V1_YarnIssueReturnResponse.DtlsIssueDetail item: yarnIssueReturnResponse.getData().getDtlsIssueDetails()){
                            item.setSelectedStatus(false);
                        }
                        initYarnIssueRecyclerView();
                    }catch (Exception e){
                        Log.d(TAG, "fetchBagKeepingData: "+e.getMessage());
                        DialogHelper.showWarningDialog(this, "Warning", "Please try again.");
                    }
                }
                else{
                    DialogHelper.showWarningDialog(this, "Warning", "Data not found.");
                }
            }else{
                DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void requestDataForYarnRFIDIssueReturn() {
        try {
            progressBarState();
            rfidTransferViewModal.getRFIDIssueReturnValidityCheckResponse(rfidScan).observe(this, apiResponse -> {
                if(apiResponse != null) {
                    try {
                        if(apiResponse.getData().get(0).getRfidStatus() == 1){
                            V1_IssueReturnRFIDModel issueReturnRFIDModel = new V1_IssueReturnRFIDModel();
                            issueReturnRFIDModel.setRfid(rfidScan);
                            issueReturnRFIDModel.setWeight(rollWeight);
                            if(dtlsRFIDIssueArrayList == null) {
                                dtlsRFIDIssueArrayList = new ArrayList<>();
                            }
                            boolean rfidExists = false;
                            for (V1_IssueReturnRFIDModel item : dtlsRFIDIssueArrayList) {
                                if (item.getRfid().equals(apiResponse.getData().get(0).getRfidNo())) {
                                    DialogHelper.showWarningDialog(V1_RFIDTagForYarnIssueReturnActivity.this, "Warning", "RFID already exists.");
                                    rfidExists = true;
                                    break;
                                }
                            }
                            if (!rfidExists) {
                                dtlsRFIDIssueArrayList.add(issueReturnRFIDModel);
                            }
//                        dtlsRFIDIssueArrayList.size();
                        } else {
                            DialogHelper.showWarningDialog(this, "Warning", "This scanned "+rfidScan +" is not a valid. Please try valid rfid no.");
                        }
                        initYarnRFIDRecyclerView();
                        yarnRFIDRecyclerViewAdapter.notifyDataSetChanged();
                    }catch (Exception e)
                    {
                        Log.d(TAG, "requestDataForYarnRFIDIssueReturn: ");
                    }

                }else{
                    DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
                }
            });
        }catch (Exception e){
            Log.d(TAG, "requestDataForYarnRFIDIssueReturn: "+e.getMessage());
        }
    }

    private void initYarnIssueRecyclerView() {
        try {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            _issueChallanRecyclerView.setLayoutManager(linearLayoutManager);
            yarnIssueReturnRecyclerViewAdapter = new V1_YarnIssueReturnRecyclerViewAdapter( yarnIssueReturnResponse.getData().getDtlsIssueDetails(), this, this);
            _issueChallanRecyclerView.setAdapter(yarnIssueReturnRecyclerViewAdapter);
        }catch (Exception e) {
            Log.d(TAG, "initYarnIssueRecyclerView: ");
        }

    }

    private void initYarnRFIDRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _rfidRecyclerView.setLayoutManager(linearLayoutManager);
        yarnRFIDRecyclerViewAdapter = new V1_YarnRFIDRecyclerViewAdapter( dtlsRFIDIssueArrayList, this, this);
        _rfidRecyclerView.setAdapter(yarnRFIDRecyclerViewAdapter);
    }

    private void init_ui() {
        _issueChallanScanBT = findViewById(R.id.issueChallanScanBT);
        _issueChallanScanTV = findViewById(R.id.issueChallanScanTV);
        _issueChallanRecyclerView = findViewById(R.id.issueChallanRecyclerView);
        _rfidRecyclerView = findViewById(R.id.rfidRecyclerView);
        _progressBar = findViewById(R.id.progressBar);
        _rollWeightET = findViewById(R.id.rollWeightET);
        _rfidScanBT = findViewById(R.id.rfidScanBT);
        _rfidScanTV = findViewById(R.id.rfidScanTV);

        _issueChallanScanBT.setOnClickListener(this);
        _rfidScanBT.setOnClickListener(this);

        initYarnIssueRecyclerView();
    }

    private void progressBarState() {
        rfidTransferViewModal.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                _progressBar.setVisibility(View.VISIBLE);
            } else {
                _progressBar.setVisibility(View.GONE);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onHeadClick(int position, View v) {
        if(yarnIssueReturnResponse.getData().getDtlsIssueDetails().get(position).getSelectedStatus()){
            yarnIssueReturnResponse.getData().getDtlsIssueDetails().get(position).setSelectedStatus(false);
        }else{
            yarnIssueReturnResponse.getData().getDtlsIssueDetails().get(position).setSelectedStatus(true);
        }
        yarnIssueReturnRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.issueChallanScanBT:
                startScanning(1);
                break;
            case R.id.rfidScanBT:
                String _weight = _rollWeightET.getText().toString().trim();
                if ( !_weight.isEmpty() && yarnIssueReturnResponse.getData().getDtlsIssueDetails().size() > 0 ) {
                    startScanning(2);
                } else {
                    DialogHelper.showWarningDialog(this, "Warning", "Please scan Issue Return first then enter weight.");
                }
                break;
        }
    }

    private void startScanning(int op) {
        Intent intent = new Intent(this, V1_ScannerActivity.class);
        intent.putExtra("qc", "rfid_yarn_issue_return");
        intent.putExtra("scan_op", op);
        intent.putExtra("yarn_issue_return_data", yarnIssueReturnResponse);
        intent.putExtra("yarn_rfid_data", dtlsRFIDIssueArrayList);
        intent.putExtra("rollWeight", _rollWeightET.getText().toString());
        intent.putExtra("issueChallanScan", _issueChallanScanTV.getText().toString());
        startActivity(intent);
        finish();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onRemoveHeadClick(int position, View v) {
        if(dtlsRFIDIssueArrayList.size() > 0) {
            dtlsRFIDIssueArrayList.remove(position);
            yarnRFIDRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fabric, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            boolean selectedStatus = false;
            if(dtlsRFIDIssueArrayList.size() > 0){
                for (int i = 0; i < yarnIssueReturnResponse.getData().getDtlsIssueDetails().size(); i++) {
                    if ( yarnIssueReturnResponse.getData().getDtlsIssueDetails().get(i).getSelectedStatus()) {
                        selectedStatus = true;
                        postDataToServer(i);
                        break;
                    }
                }
                if(!selectedStatus){
                    DialogHelper.showWarningDialog(this, "Warning", "Please select product item.");
                }
            }else{
                DialogHelper.showWarningDialog(this, "Warning", "Please add minimum one rfid.");
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

    private void postDataToServer(int position) {
        JSONObject save_obj = new JSONObject();
        JSONArray rfid_dtls_arr = new JSONArray();

        try {
            JSONObject mst_obj = new JSONObject();
            mst_obj.put("issue_id", yarnIssueReturnResponse.getData().getIssueId());
            mst_obj.put("prod_id", yarnIssueReturnResponse.getData().getDtlsIssueDetails().get(position).getProdId());

            save_obj.put("mst", mst_obj);
            List<V1_YarnIssueReturnResponse.Rfid> savedRFIDList = yarnIssueReturnResponse
                    .getData()
                    .getDtlsIssueDetails()
                    .get(position)
                    .getRfid();

            for (V1_IssueReturnRFIDModel item : dtlsRFIDIssueArrayList) {
                boolean exists = false;

                for (V1_YarnIssueReturnResponse.Rfid savedItem : savedRFIDList) {
                    if (savedItem.getRfidNo().equals(item.getRfid())) {
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    JSONObject rfid_obj = new JSONObject();
                    rfid_obj.put("epcid", item.getRfid());
                    rfid_obj.put("weight", item.getWeight());
                    rfid_dtls_arr.put(rfid_obj);
                }
            }

            save_obj.put("RFID", rfid_dtls_arr);

            Log.d(TAG, "postDataToServer: " + save_obj.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (rfid_dtls_arr.length() == 0) {
            Log.d(TAG, "RFID array is empty after construction.");
            DialogHelper.showWarningDialog(this, "Warning", "Those RFID is already saved.");
        } else {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, save_obj.toString());
            progressBarState();
            rfidTransferViewModal.postRFIDIssueReturnResponse(body).observe(this, apiResponse -> {
                if(apiResponse != null) {
                    DialogHelper.showSuccessDialog(this, "Success", apiResponse.getMsg());
                    refreshData(2);
                }else{
                    DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
                }
            });
        }
    }

    private void refreshData(int i) {
        yarnIssueReturnResponse.getData().getDtlsIssueDetails().clear();
        dtlsRFIDIssueArrayList.clear();
        yarnRFIDRecyclerViewAdapter.notifyDataSetChanged();
        yarnIssueReturnRecyclerViewAdapter.notifyDataSetChanged();
    }
}