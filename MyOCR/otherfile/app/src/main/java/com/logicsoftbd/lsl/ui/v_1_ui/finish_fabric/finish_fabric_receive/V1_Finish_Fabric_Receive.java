package com.logicsoftbd.lsl.ui.v_1_ui.finish_fabric.finish_fabric_receive;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.logicsoftbd.lsl.data.network.v1_model.V1_finish_fabric_receive.FFRBarcode;
import com.logicsoftbd.lsl.data.network.v1_model.V1_finish_fabric_receive.FFRData;
import com.logicsoftbd.lsl.data.network.v1_model.V1_finish_fabric_receive.FFRResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_finish_fabric_receive.Fff_save_response;
import com.logicsoftbd.lsl.databinding.ActivityV1FinishFabricReceiveNewBinding;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.v_1_ui.finish_fabric.finish_fabric_receive.item_click_widget.Fff_Item_Controller;
import com.logicsoftbd.lsl.ui.v_1_ui.finish_fabric.finish_fabric_receive.item_click_widget.Fff_click_interface;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_ScannerActivity;
import com.logicsoftbd.lsl.utils.ApiUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class V1_Finish_Fabric_Receive extends AppCompatActivity implements Fff_click_interface {

    ActivityV1FinishFabricReceiveNewBinding binding;

    private ApiInterface apiInterface;
    private ApiUtils apiUtils;
    String user_id, base_url;

    //adapter for recyclerView
    Finish_fabric_recycler_adapter adapter;
    List<FFRBarcode> listOfBarcodeOnlySingleUI = new ArrayList<>();
    //default data for barcode scanner
    String btFrRackScanData = "";
    String btBarcodeScanData = "";
    String btBatchCardScanData = "";

    //count item
    List<FFRBarcode> checkedBarcodeList = new ArrayList<>();
    List<FFRBarcode> unCheckedBarcodeList = new ArrayList<>();
    //click item operation
    Fff_click_interface clickInterface;
    Fff_Item_Controller itemController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityV1FinishFabricReceiveNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //recycle viewer
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        binding.finishRecyclerView.setLayoutManager(layoutManager);
        binding.finishRecyclerView.setHasFixedSize(true);

        //local storage
        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = _preferences.getString("login_userid", "");
        base_url = (_preferences.getString("base_url", ""));

        // interface data
        apiUtils = new ApiUtils(this);
        apiInterface = apiUtils.getInterface(base_url);

        //item click operation
        clickInterface = this;
        itemController = Fff_Item_Controller.getInstance();
        itemController.setFff_click_interface(clickInterface);

        binding.btFrRackScan.setOnClickListener(view -> startScanning(1));
        binding.btBarcodeScan.setOnClickListener(view -> {
            if (btFrRackScanData.equals("")) {
                toastMessage("Please add rack first...");
            } else {
                startScanning(2);
            }
        });
        binding.refreshBT.setOnClickListener(view -> callRefreshButton());
        binding.saveBT.setOnClickListener(view -> {
            if (checkedBarcodeList.size() == 0) {
                showMessageWithSweetAlert("You have no scaned barcode.", SweetAlertDialog.WARNING_TYPE);
            } else {
                saveBarcodeList();
            }
        });
        getDefaultData();
    }

    private void callRefreshButton() {
        binding.btFrRackScan.setEnabled(true);
        btFrRackScanData = "";
        btBarcodeScanData = "";
        btBatchCardScanData = "";
        binding.btFrRackScanTV.setText(btFrRackScanData);
        binding.btBarcodeScanTV.setText(btBarcodeScanData);
        binding.btBatchCardScanTV.setText(btBatchCardScanData);

        binding.totalRollCountTV.setText("0");
        binding.totalScanRollTV.setText("0");
        binding.totalBalanceTV.setText("0");

        listOfBarcodeOnlySingleUI.clear();
        callAdapter(listOfBarcodeOnlySingleUI);
    }

    private void saveBarcodeList() {
        callProgressSection(View.VISIBLE);
        JSONArray barcode_list_json_array = new JSONArray();
        try {
            for (int i = 0; i < checkedBarcodeList.size(); i++) {
                JSONObject single_barcode_json_object = new JSONObject();
                single_barcode_json_object.put("cbo_company_id", checkedBarcodeList.get(i).getCompanyId() + "");
                single_barcode_json_object.put("user_id", user_id + "");
                single_barcode_json_object.put("rollId", checkedBarcodeList.get(i).getRackId() + "");
                single_barcode_json_object.put("batchId", checkedBarcodeList.get(i).getBatchId() + "");
                single_barcode_json_object.put("bodyPart", checkedBarcodeList.get(i).getBodypartId() + "");
                single_barcode_json_object.put("colorId", checkedBarcodeList.get(i).getColor() + "");
                single_barcode_json_object.put("deterId", checkedBarcodeList.get(i).getDeterminationId() + "");
                single_barcode_json_object.put("productId", checkedBarcodeList.get(i).getProductId() + "");
                single_barcode_json_object.put("orderId", checkedBarcodeList.get(i).getOrderId() + "");
                single_barcode_json_object.put("rollGsm", checkedBarcodeList.get(i).getGsm() + "");
                single_barcode_json_object.put("rolldia", checkedBarcodeList.get(i).getDia() + "");
                single_barcode_json_object.put("barcodeNo", checkedBarcodeList.get(i).getBarcodeNo() + "");
                single_barcode_json_object.put("rollNo", checkedBarcodeList.get(i).getRollNo() + "");
                single_barcode_json_object.put("rollQty", checkedBarcodeList.get(i).getQnty() + "");
                single_barcode_json_object.put("currentWgt", checkedBarcodeList.get(i).getGreyWgt() + "");
                single_barcode_json_object.put("rejectQty", checkedBarcodeList.get(i).getRejectQnty() + "");
                single_barcode_json_object.put("wideTypeId", checkedBarcodeList.get(i).getWidthType() + "");
                single_barcode_json_object.put("floor", checkedBarcodeList.get(i).getFloorId() + "");
                single_barcode_json_object.put("room", checkedBarcodeList.get(i).getRoomId() + "");
                single_barcode_json_object.put("rack", checkedBarcodeList.get(i).getRackId() + "");
                single_barcode_json_object.put("self", checkedBarcodeList.get(i).getShelfId() + "");
                single_barcode_json_object.put("binBox", checkedBarcodeList.get(i).getBinId() + "");
                single_barcode_json_object.put("preReprocess", checkedBarcodeList.get(i).getPrevReprocess() + "");
                single_barcode_json_object.put("reprocess", checkedBarcodeList.get(i).getReprocess() + "");
                single_barcode_json_object.put("IsSalesId", checkedBarcodeList.get(i).getIsSales() + "");
                single_barcode_json_object.put("bookingWithoutOrder", checkedBarcodeList.get(i).getBookingWithoutOrder() + "");
                single_barcode_json_object.put("bookingNumber", checkedBarcodeList.get(i).getBookingNumber() + "");
                single_barcode_json_object.put("greyQntyPcs", checkedBarcodeList.get(i).getQtyInPcs() + "");
                single_barcode_json_object.put("collerCuffSize", "");
                single_barcode_json_object.put("txt_delivery_date", "");
                single_barcode_json_object.put("txt_challan_no", "");
                single_barcode_json_object.put("cbo_knitting_source", checkedBarcodeList.get(i).getCboKnittingSource() + "");
                single_barcode_json_object.put("knit_company_id", checkedBarcodeList.get(i).getKnittingCompany() + "");
                single_barcode_json_object.put("cbo_location", checkedBarcodeList.get(i).getLocationId() + "");
                single_barcode_json_object.put("cbo_store_name", checkedBarcodeList.get(i).getStoreId() + "");
                single_barcode_json_object.put("knit_location_id", 0);
                single_barcode_json_object.put("update_id", "");
                single_barcode_json_object.put("txt_system_no", "");
                single_barcode_json_object.put("txt_boe_mushak_challan_no", "");
                single_barcode_json_object.put("txt_boe_mushak_challan_date", "");
                single_barcode_json_object.put("updateDetailsId", "0");
                single_barcode_json_object.put("activeId", "0");
                single_barcode_json_object.put("systemId", "");
                single_barcode_json_object.put("productionId", "");
                single_barcode_json_object.put("productionDtlId", "");
                single_barcode_json_object.put("buyerId", checkedBarcodeList.get(i).getBuyerId() + "");
                single_barcode_json_object.put("rollTableId", "0");
                single_barcode_json_object.put("transId", "0");
                barcode_list_json_array.put(single_barcode_json_object);
            }
        } catch (Exception e) {
            //no exception
        }

        String jsonString = barcode_list_json_array.toString();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, jsonString);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Cookie", "ci_session=74ec39fc30902ce9ee7a4c65fac8c3a3");



        apiInterface.finishFabricReceivePostBarcodes(requestBody, headers).enqueue(new Callback<Fff_save_response>() {
            @Override
            public void onResponse(@NonNull Call<Fff_save_response> call, @NonNull Response<Fff_save_response> response) {
                callProgressSection(View.GONE);
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    showMessageWithSweetAlert(response.body().getMsg() + "", SweetAlertDialog.SUCCESS_TYPE);
                    callRefreshButton();
                } else {
                    showMessageWithSweetAlert("Try Again...", SweetAlertDialog.WARNING_TYPE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Fff_save_response> call, @NonNull Throwable t) {
                callProgressSection(View.GONE);
                showMessageWithSweetAlert("Check your network connection.", SweetAlertDialog.WARNING_TYPE);
            }
        });
    }

    private void callProgressSection(int progressbar) {
        binding.loadingProgressBar.setVisibility(progressbar);
        if (progressbar == View.VISIBLE) {
            binding.fullBody.setVisibility(View.GONE);
        } else {
            binding.fullBody.setVisibility(View.VISIBLE);
        }
    }

    private void showMessageWithSweetAlert(String msg, int typeOfAlert) {
        new SweetAlertDialog(this, typeOfAlert)
                .setTitleText("Message")
                .setContentText(msg)
                .setConfirmText("ok")
                .show();
    }

    private void startScanning(int i) {
        //1 = btFrRackScan  //2=btBarcodeScan
        Intent intent = new Intent(this, V1_ScannerActivity.class);
        intent.putExtra("qc", "Gtm Finish Fab. Receive");
        intent.putExtra("finish_fabric_receive___scan_op", i);

        intent.putExtra("finish_fabric_receive___btFrRackScanData", btFrRackScanData);
        intent.putExtra("finish_fabric_receive___btBarcodeScanData", btBarcodeScanData);
        intent.putExtra("finish_fabric_receive___btBatchCardScanData", btBatchCardScanData);

        if (i == 1) {//no implementation
        }
        if (i == 2) {
            intent.putExtra("finish_fabric_receive__listOfAllBarcode", (Serializable) listOfBarcodeOnlySingleUI);
        }
        startActivity(intent);
    }

    private void getDefaultData() {
        Intent intent = getIntent();
        String barcode_scan_result = intent.getStringExtra("barcode_scan");
        String scan_op = intent.getStringExtra("scan_op");
        List<FFRBarcode> listOfAllBarcode = (ArrayList<FFRBarcode>) intent.getSerializableExtra("finish_fabric_receive__listOfAllBarcode");

        if (scan_op != null) {
            if (scan_op.equals("1")) {
                btFrRackScanData = barcode_scan_result;
                btBarcodeScanData = intent.getStringExtra("btBarcodeScanData");
                btBatchCardScanData = intent.getStringExtra("btBatchCardScanData");
            }
            if (scan_op.equals("2")) {
                btFrRackScanData = intent.getStringExtra("btFrRackScanData");
//                String[] btFrRackScanData_split_array = btFrRackScanData.split(Pattern.quote("***"));
//                String number = btFrRackScanData_split_array[0];
                if (listOfAllBarcode.size() == 0) {
                    assert btFrRackScanData != null;
                    requestForBarcodeList(barcode_scan_result, splitRackNumber(btFrRackScanData));
                } else {
                    checkingBarcodeFromList(barcode_scan_result, listOfAllBarcode);
                }
            }
        }
        binding.btFrRackScanTV.setText(splitRackNumber(btFrRackScanData));
        if (!btFrRackScanData.equals("")) {
            binding.btFrRackScan.setEnabled(false);
        }
    }

    private String splitRackNumber(String btFrRackScanData) {
        String[] btFrRackScanData_split_array = btFrRackScanData.split(Pattern.quote("***"));
        return btFrRackScanData_split_array[0];
    }

    private void requestForBarcodeList(String barcodeScanResult, String room_rack_id) {
        callProgressSection(View.VISIBLE);
        listOfBarcodeOnlySingleUI.clear();
        apiInterface.finishFabricReceiveBarcodeData(barcodeScanResult, room_rack_id).enqueue(new Callback<FFRResponse>() {
            @Override
            public void onResponse(@NonNull Call<FFRResponse> call, @NonNull Response<FFRResponse> response) {
                callProgressSection(View.GONE);
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    List<FFRData> responseList = response.body().getBarcodeList();
                    for (FFRData data : responseList) {
                        FFRBarcode ffrBarcode = new FFRBarcode(
                                data.getIsSales(),
                                data.getBarcodeNo(),
                                data.getCompanyId(),
                                data.getBuyerId(),
                                data.getRollNo(),
                                data.getRollId(),
                                data.getBatchId(),
                                data.getBatchNo(),
                                data.getDeterminationId(),
                                data.getBodypartId(),
                                data.getOrderId(),
                                data.getConstruction(),
                                data.getComposition(),
                                data.getColor(),
                                data.getGsm(),
                                data.getDia(),
                                data.getQnty(),
                                data.getQtyInPcs(),
                                data.getItemSize(),
                                data.getRejectQnty(),
                                data.getReprocess(),
                                data.getPrevReprocess(),
                                data.getGreyWgt(),
                                data.getWidthType(),
                                data.getProductId(),
                                data.getGreySysNumber(),
                                data.getBookingNumber(),
                                data.getBookingWithoutOrder(),
                                data.getCboKnittingSource(),
                                data.getKnittingCompany(),
                                data.getLocationId(),
                                data.getStoreId(),
                                data.getFloorId(),
                                data.getRoomId(),
                                data.getRackId(),
                                data.getShelfId(),
                                data.getBinId(),
                                false);
                        listOfBarcodeOnlySingleUI.add(ffrBarcode);
                    }
                    //check matching data
                    try {
                        for (FFRBarcode data : listOfBarcodeOnlySingleUI) {
                            if (barcodeScanResult.equals(data.getBarcodeNo())) {
                                data.setChecked(true);
                                setDataInBarcodeTextView(data.getBarcodeNo(), data.getBatchNo());
                                break;
                            }
                        }
                    } catch (Exception e) {
                        //no operations
                    }
                    countBarcodeList();
                    callAdapter(listOfBarcodeOnlySingleUI);
                } else {
                    callAdapter(listOfBarcodeOnlySingleUI);
                    setDataInBarcodeTextView("", "");
                    showMessageWithSweetAlert("Barcode not found",SweetAlertDialog.WARNING_TYPE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<FFRResponse> call, @NonNull Throwable t) {
                callProgressSection(View.GONE);
                callAdapter(listOfBarcodeOnlySingleUI);
                setDataInBarcodeTextView("", "");
                showMessageWithSweetAlert("Try again...",SweetAlertDialog.WARNING_TYPE);
            }
        });
    }

    private void checkingBarcodeFromList(String barcodeScanResult, List<FFRBarcode> barcodeList) {
        try {
            boolean found = false;
            for (FFRBarcode data : barcodeList) {
                String selectedBarcode = data.getBarcodeNo();
                if (barcodeScanResult.equals(selectedBarcode)) {
                    found = true;
                    data.setChecked(true);
                    binding.btBarcodeScanTV.setText(data.getBarcodeNo());
                    binding.btBatchCardScanTV.setText(data.getBatchNo());
                    break;
                }
            }
            if (found) {
                Toast.makeText(this, "Checked Barcode", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Barcode Not Found", Toast.LENGTH_SHORT).show();
            }
            listOfBarcodeOnlySingleUI.clear();
            listOfBarcodeOnlySingleUI = barcodeList;
            callAdapter(listOfBarcodeOnlySingleUI);
            countBarcodeList();
        } catch (Exception e) {
            //no operations
        }
    }

    @SuppressLint("SetTextI18n")
    private void countBarcodeList() {
        checkedBarcodeList.clear();
        unCheckedBarcodeList.clear();

        int checkRoll = 0;
        int unCheckedRoll = 0;
        for (FFRBarcode data : listOfBarcodeOnlySingleUI) {
            if (data.getChecked().equals(true)) {
                checkRoll += 1;
                checkedBarcodeList.add(data);
            } else {
                unCheckedRoll += 1;
                unCheckedBarcodeList.add(data);
            }
        }
        binding.totalRollCountTV.setText(listOfBarcodeOnlySingleUI.size() + "");
        binding.totalScanRollTV.setText(checkRoll + "");
        binding.totalBalanceTV.setText(unCheckedRoll + "");
    }

    @SuppressLint("SetTextI18n")
    private void setDataInBarcodeTextView(String mainBarcodeData, String mainBatchNoData) {
        btBarcodeScanData = mainBarcodeData;
        btBatchCardScanData = mainBatchNoData;
        binding.btBarcodeScanTV.setText(mainBarcodeData);
        binding.btBatchCardScanTV.setText(mainBatchNoData);
        binding.totalRollCountTV.setText(listOfBarcodeOnlySingleUI.size() + "");
    }

    private void toastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void callAdapter(List<FFRBarcode> listOfBarcode) {
        adapter = new Finish_fabric_recycler_adapter(V1_Finish_Fabric_Receive.this, listOfBarcode);
        binding.finishRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(FFRBarcode ffrBarcode) {
        itemController.setFfrBarcode(ffrBarcode);
        unCheckingBarcodeFromList(ffrBarcode);
    }

    private void unCheckingBarcodeFromList(FFRBarcode ffrBarcode) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Runnable task = () -> {
            try {
//                boolean found = false;
                for (FFRBarcode data : listOfBarcodeOnlySingleUI) {
                    String selectedBarcode = data.getBarcodeNo();
                    if (ffrBarcode.getBarcodeNo().equals(selectedBarcode)) {
//                        found = true;
                        data.setChecked(!data.getChecked());
                        break;
                    }
                }
//            if (found) {
//                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
//            }
                countBarcodeList();
            } catch (Exception e) {
                //no operations
            }
        };
        executorService.execute(task);
        executorService.shutdown();
    }
}
