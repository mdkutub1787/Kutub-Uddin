package com.logicsoftbd.lsl.ui.v_1_ui.bundle_wise_sewing_pcs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundeWiseSewingInputPCSResponse;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_ScannerActivity;
import com.logicsoftbd.lsl.utils.DialogHelper;
import com.logicsoftbd.lsl.viewModel.BundleWiseViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class V1_BundleWiseSewingInput_PCSActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "V1_BundleWiseSewingInpu";
    private ProgressBar _progressBar;
    private TextView _companyNameTV, _locationTV, _floorTV, _lineTV, _barcodenumberTextTV, _bundleNoTV, _yearTV, _jobNoTV, _buyerTV, _orderNoTV, _irIBTV, _qtyTV, _colorTV,
    _sizeTV, _gmtsTV, _countryTV, _pInputTV;
    private EditText barcodeET;
    private Button sDate, inputScanBtn, incrementBtn, decrementBtn, refreshBtn, saveBtn;
    private CheckBox organicCheckBox;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private int isOrganic = 0, company = 0, location = 0, line = 0, floor = 0, Year, Month, Day = 0, updatedID = 0, rescan = 0, color_type_id = 0,
            companyId = 0, sewingcompanyId = 0, sourceId = 0, locationId = 0, floorId = 0, lineId = 0, totalQnty = 0, totalQCQnty = 0;
    private String base_url = "", currentDate, companyName = "", locationName = "", floorName = "", lineName = "", _currentDate = "", _currentTime = "",
            userId, date, start_date, end_date, barcode, urladdressChk, urladdress, urlString, urlstringbase, urlstring_c_wise_l,
            urlstring_l_wise_f, urlstring_f_wise_l, urlstring_sewing_input, urlPendingData, macAddress, year, jobNo, buyer, orderNo,
            gmtsItem, country, color, size, message, cut_no, bundleNo, barcode_no, order_id, item_id, country_id, color_id, size_id,
            color_size_id, qty, is_rescan, type_entry, barcodeNumber;
    private LinearLayout linearLayoutSewingIn;
    private BundleWiseViewModel bundleWiseViewModel;
    private V1_BundeWiseSewingInputPCSResponse bundeWiseSewingInputPCSResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_bundle_wise_sewing_input_pcsactivity);

        bundleWiseViewModel = new ViewModelProvider(this).get(BundleWiseViewModel.class);
        Intent intent = getIntent();
        String barcodeNumber = intent.getStringExtra("result");

        init_ui();
        getDefaultData();

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = _preferences.getString("login_userid", "");
        macAddress = _preferences.getString("mac", null);
        company = (_preferences.getInt("company", 0));
        location = (_preferences.getInt("location", 0));
        line = (_preferences.getInt("line", 0));
        floor = (_preferences.getInt("floor", 0));


        if (barcodeNumber != null)
        {
            sewingInputGetDataMethod(barcodeNumber);
        }
    }

    private void sewingInputGetDataMethod(String barcodeNumber) {
        progressBarState();
        bundleWiseViewModel.getBundleWiseSewingInputPCSResponse(String.valueOf(companyId), String.valueOf(locationId), String.valueOf(floor), String.valueOf(line), barcodeNumber, "4").observe(this, apiResponse -> {
            if(apiResponse != null) {
                try {
                    if(apiResponse.getResultset().getMessageBng().equals("")){
                        linearLayoutSewingIn.setVisibility(View.VISIBLE);
                        bundeWiseSewingInputPCSResponse = apiResponse;
                        totalQnty = Integer.parseInt(apiResponse.getResultset().getQty());
//                        totalQCQnty = Integer.parseInt(apiResponse.getResultset().getScannedQty());
                        totalQCQnty = Integer.parseInt(apiResponse.getResultset().getSewingInQty());
                        bundleNo = apiResponse.getResultset().getBundleNo();
                        _bundleNoTV.setText(apiResponse.getResultset().getBundleNo());
                        _yearTV.setText(apiResponse.getResultset().getYear());
                        _jobNoTV.setText(apiResponse.getResultset().getJobNo());
                        _buyerTV.setText(apiResponse.getResultset().getBuyer());
                        _orderNoTV.setText(apiResponse.getResultset().getOrderNo());
                        _irIBTV.setText(apiResponse.getResultset().getIntRef());
                        _qtyTV.setText(apiResponse.getResultset().getQty());
                        _pInputTV.setText(apiResponse.getResultset().getSewingInQty());
                        _colorTV.setText(apiResponse.getResultset().getColor());
                        _sizeTV.setText(apiResponse.getResultset().getSize());
                        _gmtsTV.setText(apiResponse.getResultset().getItem());
                        _countryTV.setText(apiResponse.getResultset().getCountry());
//                        _pInputTV.setText(apiResponse.getResultset().getScannedQty());
                        saveBtn.setText(apiResponse.getResultset().getIsRescan().equals("1") ? "UPDATE" : "SAVE");

                    }else {
                        linearLayoutSewingIn.setVisibility(View.GONE);
                        DialogHelper.showSuccessDialog(V1_BundleWiseSewingInput_PCSActivity.this, "Message", apiResponse.getResultset().getMessageBng());
                    }

                }catch (Exception e) {
                    linearLayoutSewingIn.setVisibility(View.GONE);
                    Log.d(TAG, "sewingInputGetDataMethod: "+e.getMessage());
                }
            }
        });
    }

    private void getDefaultData() {
        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        companyId = (_preferences.getInt("company", 0));
        locationId = (_preferences.getInt("location", 0));
        lineId = (_preferences.getInt("line", 0));
        floorId = (_preferences.getInt("floor", 0));
        companyName = (_preferences.getString("companyName", ""));
        locationName = (_preferences.getString("locationName", ""));
        floorName = (_preferences.getString("floorName", ""));
        lineName = (_preferences.getString("lineName", ""));

        if(companyName != null && locationName != null && floorName != null && lineName != null){
            _companyNameTV.setText(companyName);
            _locationTV.setText(locationName);
            _floorTV.setText(floorName);
            _lineTV.setText(lineName);

            sewingcompanyId = companyId;
        }
    }
    private void init_ui() {
        _progressBar = findViewById(R.id.progressBar);
        _companyNameTV = findViewById(R.id.companyNameTV);
        _locationTV = findViewById(R.id.locationTV);
        _floorTV = findViewById(R.id.floorTV);
        _lineTV = findViewById(R.id.lineTV);
        _bundleNoTV = findViewById(R.id.bundleNoTV);
        _yearTV = findViewById(R.id.yearTV);
        _jobNoTV = findViewById(R.id.jobNoTV);
        _buyerTV = findViewById(R.id.buyerTV);
        _orderNoTV = findViewById(R.id.orderNoTV);
        _irIBTV = findViewById(R.id.irIBTV);
        _qtyTV = findViewById(R.id.qtyTV);
        _colorTV = findViewById(R.id.colorTV);
        _sizeTV = findViewById(R.id.sizeTV);
        _gmtsTV = findViewById(R.id.gmtsTV);
        _countryTV = findViewById(R.id.countryTV);
        _pInputTV = findViewById(R.id.pInputTV);
        barcodeET = findViewById(R.id.barcodenumberText);
        linearLayoutSewingIn = findViewById(R.id.linearLayoutSewingIn);

        sDate = findViewById(R.id.sewingDateBtn);
        inputScanBtn = findViewById(R.id.input_scanBtn);
        inputScanBtn.setOnClickListener(this);

        incrementBtn = findViewById(R.id.incrementBtn);
        incrementBtn.setOnClickListener(this);

        decrementBtn = findViewById(R.id.decrementBtn);
        decrementBtn.setOnClickListener(this);

        refreshBtn = findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(this);

        saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(this);

        organicCheckBox = findViewById(R.id.organicCheckBox);
        organicCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                isOrganic = 1;
            }
        });

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = simpleDateFormat.format(calendar.getTime());
        sDate.setText(currentDate);
    }

    private void progressBarState() {
        bundleWiseViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                _progressBar.setVisibility(View.VISIBLE);
            } else {
                _progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.input_scanBtn:
                barcodeScanMethod();
                break;

            case R.id.incrementBtn:
                countDataForSewingInput("inc", v);
                break;

            case R.id.decrementBtn:
                countDataForSewingInput("dec", v);
                break;

            case R.id.saveBtn:
                postDataForSewingInput("save", v);
                break;
            case R.id.refreshBtn:
                countDataForSewingInput("refresh", v);
                break;
        }
    }

    private void countDataForSewingInput(String operation, View clickedButton) {

        if (locationId != 0 && floorId != 0 && lineId != 0) {
            if (bundleNo != null) {
                int inputQuantity = Integer.parseInt(_pInputTV.getText().toString());

                if ((operation.equals("inc") && inputQuantity < totalQnty) ||
                        (operation.equals("dec") && inputQuantity > 0) || operation.equals("refresh")) {
                    refreshDataFromUI(operation);
                } else {
                    String message;
                    if (operation.equals("inc") && inputQuantity >= totalQnty) {
                        message = "Cannot increment: input quantity has reached the total quantity.";
                    } else if (operation.equals("dec") && inputQuantity <= 0) {
                        message = "Cannot decrement: input quantity is already at zero.";
                    } else {
                        message = "Input quantity exceeded.";
                    }

                    DialogHelper.showWarningDialog(
                            V1_BundleWiseSewingInput_PCSActivity.this,
                            "Message",
                            message
                    );
                    clickedButton.setEnabled(true);
                }
            } else {
                DialogHelper.showWarningDialog(V1_BundleWiseSewingInput_PCSActivity.this, "Message", "Barcode not scanned");
                clickedButton.setEnabled(true);
            }
        } else {
            DialogHelper.showWarningDialog(V1_BundleWiseSewingInput_PCSActivity.this, "Message", "Please fill the credentials");
            clickedButton.setEnabled(true);
        }
    }

    private void postDataForSewingInput(String operation, View clickedButton) {
        clickedButton.setEnabled(false);

        if (locationId != 0 && floorId != 0 && lineId != 0) {
            if (bundleNo != null) {
                int inputQuantity = Integer.parseInt(_pInputTV.getText().toString());

                if (inputQuantity > 0) {
                    try {
                        postDataToServer_sewing_input(operation, () -> clickedButton.setEnabled(true), () -> clickedButton.setEnabled(true));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        clickedButton.setEnabled(true);
                    }
                } else {
                    DialogHelper.showWarningDialog(
                            V1_BundleWiseSewingInput_PCSActivity.this,
                            "Message",
                            "Input quantity exceeded."
                    );
                    clickedButton.setEnabled(true);
                }
            } else {
                DialogHelper.showWarningDialog(V1_BundleWiseSewingInput_PCSActivity.this, "Message", "Barcode not scanned");
                clickedButton.setEnabled(true);
            }
        } else {
            DialogHelper.showWarningDialog(V1_BundleWiseSewingInput_PCSActivity.this, "Message", "Please fill the credentials");
            clickedButton.setEnabled(true);
        }
    }

    private void postDataToServer_sewing_input(String operation, Runnable onSuccess, Runnable onError) throws JSONException {
        JSONObject jsonObject = buildJsonObject(operation);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());

        progressBarState();

        bundleWiseViewModel.postBundleWiseSewingInputPCSResponse(body).observe(this, apiResponse -> {
            if (apiResponse != null) {
                if (apiResponse.getStatus()) {
                    DialogHelper.showSuccessDialog(
                            V1_BundleWiseSewingInput_PCSActivity.this,
                            "Message",
                            apiResponse.getResultset().getStatus()
                    );
                    refreshDataFromUI("refresh");
                    onSuccess.run();
                } else {
                    DialogHelper.showWarningDialog(
                            V1_BundleWiseSewingInput_PCSActivity.this,
                            "Message",
                            apiResponse.getResultset().getStatus()
                    );
                    onError.run();
                }
            } else {
                DialogHelper.showErrorDialog(
                        V1_BundleWiseSewingInput_PCSActivity.this,
                        "Message",
                        "Something wrong happened!"
                );
                onError.run();
            }
        });
    }

    private void refreshDataFromUI(String operation) {
        if (operation.equals("inc")) {
            if (totalQCQnty < totalQnty) {
                totalQCQnty++;
            }
        } else if (operation.equals("dec")) {
            if (totalQCQnty > 1) {
                totalQCQnty--;
            }
        } else if(operation.equals("refresh")){
            linearLayoutSewingIn.setVisibility(View.GONE);
        }
        _pInputTV.setText(String.valueOf(totalQCQnty));
    }

    private JSONObject buildJsonObject(String operation) throws JSONException{

        JSONObject save_obj = new JSONObject();
        JSONObject index_obj = new JSONObject();
        JSONObject data_obj = new JSONObject();
        JSONArray dtls_arr = new JSONArray();

        save_obj.put("status",true);
        save_obj.put("mode", "save");
//        save_obj.put("operation", operation);
        save_obj.put("production_type", 4);
        save_obj.put("UPDATE_ID", updatedID);

        index_obj.put("company_id", companyId);
        index_obj.put("location_id", locationId);
        index_obj.put("production_source", 1);
        index_obj.put("serving_company", sewingcompanyId);
        index_obj.put("floor_id", floorId);
        index_obj.put("sewing_line", lineId);
        if(isOrganic == 1){
            index_obj.put("organic", "Organic");
        }else{
            index_obj.put("organic", "");
        }
        index_obj.put("user_id", userId);
        index_obj.put("production_date", sDate.getText().toString());
        index_obj.put("hour", "");
        index_obj.put("remarks", "");
        index_obj.put("txt_system_id", "");
        index_obj.put("mac", macAddress);

        data_obj.put("index", index_obj);

        JSONObject dtls_obj = new JSONObject();
        dtls_obj.put("cut_no", bundeWiseSewingInputPCSResponse.getResultset().getCutNo());
        dtls_obj.put("bundle_no", bundeWiseSewingInputPCSResponse.getResultset().getBundleNo());
        dtls_obj.put("barcode_no", bundeWiseSewingInputPCSResponse.getResultset().getBarcodeNo());
        dtls_obj.put("order_id", bundeWiseSewingInputPCSResponse.getResultset().getOrderId());
        dtls_obj.put("item_id", bundeWiseSewingInputPCSResponse.getResultset().getItemId());
        dtls_obj.put("country_id", bundeWiseSewingInputPCSResponse.getResultset().getCountryId());
        dtls_obj.put("color_id", bundeWiseSewingInputPCSResponse.getResultset().getColorId());
        dtls_obj.put("size_id", bundeWiseSewingInputPCSResponse.getResultset().getSizeId());
        dtls_obj.put("color_size_id", bundeWiseSewingInputPCSResponse.getResultset().getColorSizeId());
        dtls_obj.put("qnty", _pInputTV.getText().toString());
        dtls_obj.put("is_rescan", rescan);
        dtls_obj.put("color_type_id", color_type_id);
        dtls_arr.put(dtls_obj);

        data_obj.put("list_data", dtls_arr);
        save_obj.put("data", data_obj);
        Log.d(TAG, "buildJsonObject: "+save_obj);
        return save_obj;
    }

    private void barcodeScanMethod() {
        barcode = barcodeET.getText().toString();

        if(!barcode.isEmpty())
        {
            barcode = barcodeET.getText().toString();
            sewingInputGetDataMethod(barcode);
        }
        else {
            Intent intent = new Intent(this, V1_ScannerActivity.class);
            intent.putExtra("url", urladdress);
            intent.putExtra("qc", "bundle_wise_sewing_input_pcs");
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

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
                .setCancelClickListener(SweetAlertDialog::cancel)
                .show();
    }
}