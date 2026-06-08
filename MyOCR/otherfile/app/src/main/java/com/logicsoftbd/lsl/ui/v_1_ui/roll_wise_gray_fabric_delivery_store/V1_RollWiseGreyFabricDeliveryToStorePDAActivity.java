package com.logicsoftbd.lsl.ui.v_1_ui.roll_wise_gray_fabric_delivery_store;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyRollDeliveryItemModel;
import com.logicsoftbd.lsl.utils.DialogHelper;
import com.logicsoftbd.lsl.utils.VerticalSpacingItemDecorator;
import com.logicsoftbd.lsl.viewModel.GrayFabricViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class V1_RollWiseGreyFabricDeliveryToStorePDAActivity extends AppCompatActivity implements View.OnClickListener,  V1_RollWiseGrayFabricDeliveryToStoreRecyclerViewAdapter.OnRemoveHeadListener {
    private static final String TAG = "V1_Grey_Fabric_Roll_Iss";
    private ProgressDialog _pdialog;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private DatePickerDialog datePickerDialog;
    private ProgressBar _progressBar;
//    private EditText _deliveryRemarkET;
    private TextView _companyNameTV, _knittingCompanyNameTV, _knittingSourceNameTV, _locationNameTV, _floorNameTV, _barcodeScanTV, _challanNoScanTV, _totalWeightTV, _totalRollTV;
    private Button _barcodeScanBT;
    private RecyclerView _greyRollRecyclerView;
    private V1_RollWiseGrayFabricDeliveryToStoreRecyclerViewAdapter grayFabricDeliveryToStoreRecyclerViewAdapter;
    private ArrayList<V1_GreyRollDeliveryItemModel> grayRollDeliveryItemModels = new ArrayList<>();
    private ArrayList<V1_GreyRollDeliveryItemModel> dataList = new ArrayList<>();
    private String base_url = "", grey_roll_barcodeScan, currentDate, user_id, grey_barcode_scan, company, knittingCompany, knittingSource, locationName, locationId, floorName, floorId;
    private int scan_op = 0, company_id = 0, sourch_id = 0, _company_id = 0, knittingCompany_id = 0, knittingSource_id = 0, purpose_id = 0, sourceSelectionPosition = 0, dyeingCompanySelectionPosition = 0;
    private GrayFabricViewModel grayFabricViewModel;
    private String currentIdentifier = "bundle";;

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grey_fabric_delivery_to_store_pda);

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = _preferences.getString("login_userid", "");
        base_url = (_preferences.getString("base_url", ""));

        grayFabricViewModel = new ViewModelProvider(this).get(GrayFabricViewModel.class);

        init_ui();
        initRecyclerView();
//        getDefaultData();

        IntentFilter filter = new IntentFilter("com.logicsoftbd.lsl.SCAN");
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(barcodeReceiver, filter);
    }

    private void getDefaultData() {
        Intent intent = getIntent();
        grey_roll_barcodeScan = intent.getStringExtra("grey_roll_barcodeScan");
        grey_barcode_scan = intent.getStringExtra("barcode_scan");
        dataList = (ArrayList<V1_GreyRollDeliveryItemModel>) intent.getSerializableExtra("grey_roll_data");

        _barcodeScanTV.setText(grey_barcode_scan);

        if(dataList != null){
            grayRollDeliveryItemModels = dataList;
            initRecyclerView();
            calculateTotalRollWeight();
        }

//        if(grey_roll_barcodeScan != null && !grey_roll_barcodeScan.equals("")){
//            _barcodeScanTV.setText(grey_roll_barcodeScan);
//            requestForBarcode(grey_roll_barcodeScan);
//        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void requestForBarcode(String barcodeScan) {
        progressBarState();
        grayFabricViewModel.getRollWiseGrayFabricDeliveryToStoreResponse(barcodeScan).observe(this, apiResponse -> {
            if(apiResponse!= null){
                boolean s = false;
                _company_id = Integer.parseInt(apiResponse.getData().getCompanyId());
                company = apiResponse.getData().getCompanyName();
                knittingCompany = apiResponse.getData().getKnitCompany();
                knittingCompany_id = Integer.parseInt(apiResponse.getData().getKnitCompanyId());
                knittingSource = apiResponse.getData().getKnitSourceName();
                knittingSource_id = Integer.parseInt(apiResponse.getData().getKnitSourceId());
                locationId = apiResponse.getData().getLocationId();
                locationName = apiResponse.getData().getLocationName();
                floorId = apiResponse.getData().getFloorId();
                floorName = apiResponse.getData().getFloorName();

                if (grayRollDeliveryItemModels.size() > 0) {
                    if (!knittingCompany.equals(grayRollDeliveryItemModels.get(0).getKnittingCompanyName())) {
                        s = true;
                        showAlertMessage("Sorry! Job mixed not allowed.", 0, 0);
                    }
                    for (int j = 0; j < grayRollDeliveryItemModels.size(); j++) {
                        if (grayRollDeliveryItemModels.get(j).getBarcodeNo().equals(grey_roll_barcodeScan)) {
                            showAlertMessage("This barcode is already scanned.", 0, 0);
                            s = true;
                            break;
                        }
                    }
                }
                if (!s) {
                    V1_GreyRollDeliveryItemModel greyRollDeliveryItemModel = new V1_GreyRollDeliveryItemModel();
                    greyRollDeliveryItemModel.setCompanyId(apiResponse.getData().getCompanyId());
                    greyRollDeliveryItemModel.setCompanyName(String.valueOf(apiResponse.getData().getCompanyName()));
                    greyRollDeliveryItemModel.setKnittingCompanyId(String.valueOf(apiResponse.getData().getKnitCompanyId()));
                    greyRollDeliveryItemModel.setKnittingCompanyName(String.valueOf(apiResponse.getData().getKnitCompany()));
                    greyRollDeliveryItemModel.setKnittingSourceId(String.valueOf(apiResponse.getData().getKnitSourceId()));
                    greyRollDeliveryItemModel.setKnittingSourceName(apiResponse.getData().getKnitSourceName());
                    greyRollDeliveryItemModel.setLocationId(apiResponse.getData().getLocationId());
                    greyRollDeliveryItemModel.setLocationName(apiResponse.getData().getLocationName());
                    greyRollDeliveryItemModel.setFloorId(apiResponse.getData().getFloorId());
                    greyRollDeliveryItemModel.setFloorName(apiResponse.getData().getFloorName());
                    greyRollDeliveryItemModel.setBarcodeNo(apiResponse.getData().getBarcodeNo());
                    greyRollDeliveryItemModel.setWeight(apiResponse.getData().getWeight());
                    greyRollDeliveryItemModel.setProductionId(apiResponse.getData().getProductionid());
                    greyRollDeliveryItemModel.setProductionDtlsId(apiResponse.getData().getProductiondtlsid());
                    greyRollDeliveryItemModel.setProductId(apiResponse.getData().getProductid());
                    greyRollDeliveryItemModel.setOrderId(apiResponse.getData().getOrderid());
                    greyRollDeliveryItemModel.setDeterId(apiResponse.getData().getDeterid());
                    greyRollDeliveryItemModel.setRollId(apiResponse.getData().getRollid());
                    greyRollDeliveryItemModel.setCurrentDelivery(apiResponse.getData().getCurrentdelivery());
                    greyRollDeliveryItemModel.setRollNo(apiResponse.getData().getRollno());
                    greyRollDeliveryItemModel.setBookingWithoutOrder(apiResponse.getData().getBookingwithoutorder());
                    greyRollDeliveryItemModel.setSmnBookingNo(apiResponse.getData().getSmnbookingno());
                    greyRollDeliveryItemModel.setIsSales(apiResponse.getData().getIssales());
                    grayRollDeliveryItemModels.add(greyRollDeliveryItemModel);
                }
                grayFabricDeliveryToStoreRecyclerViewAdapter.notifyDataSetChanged();
                calculateTotalRollWeight();
            }else{
                DialogHelper.showErrorDialog(this, "Message", "Data not found");
            }
            setMasterPartData();
        });
    }

    private void setMasterPartData() {
        if(grayRollDeliveryItemModels.size() > 0){
            _companyNameTV.setText(grayRollDeliveryItemModels.get(0).getCompanyName());
            _knittingSourceNameTV.setText(grayRollDeliveryItemModels.get(0).getKnittingSourceName());
            _knittingCompanyNameTV.setText(grayRollDeliveryItemModels.get(0).getKnittingCompanyName());
            _locationNameTV.setText(grayRollDeliveryItemModels.get(0).getLocationName());
            _floorNameTV.setText(grayRollDeliveryItemModels.get(0).getFloorName());
        }
    }

    private void setPreviousListData() {
        _barcodeScanTV.setText("");
        knittingCompany = grayRollDeliveryItemModels.get(0).getKnittingCompanyName();
        company = grayRollDeliveryItemModels.get(0).getCompanyName();
        _company_id = Integer.parseInt(grayRollDeliveryItemModels.get(0).getCompanyId());
        knittingCompany_id = Integer.parseInt(grayRollDeliveryItemModels.get(0).getKnittingCompanyId());
        knittingSource = grayRollDeliveryItemModels.get(0).getKnittingSourceName();
        knittingSource_id = Integer.parseInt(grayRollDeliveryItemModels.get(0).getKnittingSourceId());
        locationId = grayRollDeliveryItemModels.get(0).getLocationId();
        locationName = grayRollDeliveryItemModels.get(0).getLocationName();
        floorId = grayRollDeliveryItemModels.get(0).getFloorId();
        floorName = grayRollDeliveryItemModels.get(0).getFloorName();
    }

    private void calculateTotalRollWeight() {
        double totalWeight = 0;
        for(int i=0; i<grayRollDeliveryItemModels.size(); i++){
            totalWeight += Double.parseDouble(grayRollDeliveryItemModels.get(i).getWeight());
        }
        _totalWeightTV.setText("Total Roll Weight: "+totalWeight);
        if(grayRollDeliveryItemModels != null && grayRollDeliveryItemModels.size() > 0) {
            _totalRollTV.setText("Total No of Roll : " + grayRollDeliveryItemModels.size());
        }
    }

    private void postDataToServer() {
        JSONObject save_obj = new JSONObject();
        JSONArray dtls_arr = new JSONArray();

        try {
            save_obj.put("status", "true"); // Make status a string
            save_obj.put("user_id", String.valueOf(user_id)); // Convert user_id to string

            // Directly add properties to data_obj
            JSONObject data_obj = new JSONObject();
            data_obj.put("cbo_company_id", String.valueOf(grayRollDeliveryItemModels.get(0).getCompanyId()));
            data_obj.put("cbo_location_id", String.valueOf(grayRollDeliveryItemModels.get(0).getLocationId()));
            data_obj.put("cbo_knitting_source", String.valueOf(grayRollDeliveryItemModels.get(0).getKnittingSourceId()));
            data_obj.put("knit_company_id", String.valueOf(grayRollDeliveryItemModels.get(0).getKnittingCompanyId()));
//            data_obj.put("txt_remarks", _deliveryRemarkET.getText().toString());
            data_obj.put("txt_remarks", "");
            data_obj.put("floor_ids", String.valueOf(grayRollDeliveryItemModels.get(0).getFloorId()));
            data_obj.put("cbo_barcode_type", "1"); // Make sure it's a string

            // Add barcode details
            for (int i = 0; i < grayRollDeliveryItemModels.size(); i++) {
                JSONObject dtls_obj = new JSONObject();
                dtls_obj.put("barcode_no", String.valueOf(grayRollDeliveryItemModels.get(i).getBarcodeNo()));
                dtls_obj.put("productionId", String.valueOf(grayRollDeliveryItemModels.get(i).getProductionId()));
                dtls_obj.put("productionDtlsId", String.valueOf(grayRollDeliveryItemModels.get(i).getProductionDtlsId()));
                dtls_obj.put("productId", String.valueOf(grayRollDeliveryItemModels.get(i).getProductId()));
                dtls_obj.put("orderId", String.valueOf(grayRollDeliveryItemModels.get(i).getOrderId()));
                dtls_obj.put("deterId", String.valueOf(grayRollDeliveryItemModels.get(i).getDeterId()));
                dtls_obj.put("rollId", String.valueOf(grayRollDeliveryItemModels.get(i).getRollId()));
                dtls_obj.put("currentDelivery",  String.valueOf(grayRollDeliveryItemModels.get(i).getCurrentDelivery()));
                dtls_obj.put("rollNo", String.valueOf(grayRollDeliveryItemModels.get(i).getRollNo()));
                dtls_obj.put("bookingWithoutOrder", String.valueOf(grayRollDeliveryItemModels.get(i).getBookingWithoutOrder()));
                dtls_obj.put("smnBookingNo", String.valueOf(grayRollDeliveryItemModels.get(i).getSmnBookingNo()));
                dtls_obj.put("isSales", String.valueOf(grayRollDeliveryItemModels.get(i).getIsSales()));
                dtls_arr.put(dtls_obj);
            }

            data_obj.put("barcode_dtls", dtls_arr);
            save_obj.put("resultset", data_obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "postDataToServer: ########"+save_obj);


        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, save_obj.toString());

        progressBarState();
        grayFabricViewModel.postGrayRollDeliveryResponse(body).observe(this, apiResponse -> {
            if(apiResponse != null && apiResponse.getStatus().equals("200") ){
                refreshData();
                _challanNoScanTV.setText(apiResponse.getChallanNo());
                DialogHelper.showSuccessDialog(this, "Success", "Successfully data saved");
            }else{
                DialogHelper.showErrorDialog(this, "Error", "Something went wrong!");
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _greyRollRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        _greyRollRecyclerView.addItemDecoration(itemDecorator);
        grayFabricDeliveryToStoreRecyclerViewAdapter = new V1_RollWiseGrayFabricDeliveryToStoreRecyclerViewAdapter(grayRollDeliveryItemModels, this, this);
        _greyRollRecyclerView.setAdapter(grayFabricDeliveryToStoreRecyclerViewAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true); // This ensures that the last item stays at the bottom
        _greyRollRecyclerView.setLayoutManager(layoutManager);
    }
    private void init_ui() {
        _progressBar = findViewById(R.id.progressBar);
//        _deliveryRemarkET = findViewById(R.id.deliveryRemarkET);
        _companyNameTV = findViewById(R.id.companyNameTV);
        _knittingSourceNameTV = findViewById(R.id.knittingSourceNameTV);
        _knittingCompanyNameTV = findViewById(R.id.knittingCompanyNameTV);
        _locationNameTV = findViewById(R.id.locationNameTV);
        _floorNameTV = findViewById(R.id.floorNameTV);
        _greyRollRecyclerView = findViewById(R.id.barcodeRecyclerView);
        _barcodeScanTV = findViewById(R.id.barcodeScanTV);
        _challanNoScanTV = findViewById(R.id.challanNoScanTV);
        _totalWeightTV = findViewById(R.id.totalWeightTV);
        _totalRollTV = findViewById(R.id.totalRollTV);

        _barcodeScanBT = findViewById(R.id.barcodeScanBT);
        _barcodeScanBT.setOnClickListener(this);

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = simpleDateFormat.format(calendar.getTime());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.barcodeScanBT:
                currentIdentifier = "bundle";
                _barcodeScanBT.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
                break;
            case R.id.saveBT:
                if(sourceSelectionPosition != 0){
                    if(sourceSelectionPosition != 2 || dyeingCompanySelectionPosition != 0){
                        if(purpose_id != 0){
                            if(grayRollDeliveryItemModels.size() > 0){
                                postDataToServer();
                            }
                        }else {
                            showAlertMessage("Please select a purpose.", 2, 0);
                        }
                    }else{
                        showAlertMessage("Please select a dyeing company.", 2, 0);
                    }
                }else{
                    showAlertMessage("Please select a dyeing source.", 2, 0);
                }
                break;
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
            if(grayRollDeliveryItemModels.size() > 0){
                postDataToServer();
            }
            return true;
        }
        else if (id == R.id.action_new){
            refreshData();
        } else if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

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
                })
                .setCancelClickListener(sDialog -> sDialog.cancel())
                .show();
    }

    private void datePicker() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
//        datePickerDialog = new DatePickerDialog(this,
//                (view, year1, monthOfYear, dayOfMonth) -> {
//                    _issueDateBT.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1);
//                }, year, month, day);
//        datePickerDialog.show();
    }

    private void refreshData() {
        _barcodeScanTV.setText("");
        _totalWeightTV.setText("Total Roll Weight: ");
        _totalRollTV.setText("Total No of Roll : ");
        _challanNoScanTV.setText("");
        grayRollDeliveryItemModels.clear();
        grayFabricDeliveryToStoreRecyclerViewAdapter.notifyDataSetChanged();

//        String barcodeData = "24020006575"; // Example barcode data
//        String identifier = "bundle";  // Example identifier
//        sendBarcodeData(barcodeData, identifier);
    }

    public void sendBarcodeData(String barcodeData, String identifier) {
        Intent intent = new Intent("com.logicsoftbd.lsl.SCAN"); // Ensure this matches the filter in MainActivity
        intent.putExtra("com.symbol.datawedge.data_string", barcodeData);
        intent.putExtra("identifier", identifier);
        sendBroadcast(intent);
    }

    private void showAlertMessage(String msg, int i, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(V1_RollWiseGreyFabricDeliveryToStorePDAActivity.this);
        builder.setTitle("Message")
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("Ok", (dialog, which) -> {
                    if(i == 1){
                        grayRollDeliveryItemModels.remove(position);
                        grayFabricDeliveryToStoreRecyclerViewAdapter.notifyDataSetChanged();
                        calculateTotalRollWeight();
                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog  = builder.create();
        dialog.show();
    }

    private void progressBarState() {
        grayFabricViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                _progressBar.setVisibility(View.VISIBLE);
            } else {
                _progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onRemoveHeadClick(int position, View v) {
        Log.d(TAG, "onRemoveHeadClick: ");
        showAlertMessage("Are you confirm to remove this barcode?", 1, position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(barcodeReceiver);
    }


    private BroadcastReceiver barcodeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Intent Received");

            if (intent.hasExtra("com.symbol.datawedge.data_string")) {
                String barcodeData = intent.getStringExtra("com.symbol.datawedge.data_string");

                String identifier = intent.getStringExtra("identifier");
                Log.d(TAG, "onReceive: "+identifier);
                if (identifier == null || identifier.isEmpty()) {
                    identifier = "default";
                }

                if (currentIdentifier != null) {
                    switch (currentIdentifier) {
                        case "bundle":
                            if(barcodeData != null){
                                _barcodeScanTV.setText(barcodeData);
                                grey_roll_barcodeScan = barcodeData;
                                requestForBarcode(barcodeData);
                            }
                            break;
                        default:
                            Log.d(TAG, "Unknown identifier: " + currentIdentifier);
                            break;
                    }

                    Log.d(TAG, "Scanned: " + barcodeData + ", Identifier: " + currentIdentifier);
                } else {
                    Log.d(TAG, "No identifier set. Ignoring barcode scan.");
                }
            } else {
                Log.d(TAG, "No barcode data found in the intent.");
            }
        }
    };
}