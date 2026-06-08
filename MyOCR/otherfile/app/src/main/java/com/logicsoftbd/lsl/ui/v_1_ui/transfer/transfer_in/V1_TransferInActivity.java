package com.logicsoftbd.lsl.ui.v_1_ui.transfer.transfer_in;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.logicsoftbd.lsl.data.network.v1_model.V1_transfer_out.barcode.DtlsPart;
import com.logicsoftbd.lsl.data.network.v1_model.V1_transfer_out.barcode.TransferOutResponse;
import com.logicsoftbd.lsl.databinding.ActivityTransferInBinding;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_ScannerActivity;
import com.logicsoftbd.lsl.utils.ApiUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class V1_TransferInActivity extends AppCompatActivity implements DeleteItemFromTransferInInterface {
    private ActivityTransferInBinding binding;

    private ApiInterface apiInterface;
    private ApiUtils apiUtils;
    String user_id, base_url;

    //master & dtls part variable catching
    String company_id = "";
    String po_breakdown_id = "";

    //location related variable
    String separated_location_id, separated_location_name;

    //adapter for recyclerView
    V1_transfer_in_recycler_adapter adapter;
    List<DtlsPart> listOfBarcodeOnlySingleUI = new ArrayList<>();

    //data controller && click interface
    DeleteItemFromTransferInInterface deleteItemFromTransferInInterface;
    DataControllerTransferIn dataControllerTransferIn;

    //handler
    private static final int SPLASH_DISPLAY_LENGTH = 1200;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransferInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //recycle viewer
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        binding.barcodeRecyclerView.setLayoutManager(layoutManager);
        binding.barcodeRecyclerView.setHasFixedSize(true);

        //handler
        handler = new Handler();

        //local storage
        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = _preferences.getString("login_userid", "");
        base_url = (_preferences.getString("base_url", ""));

        // interface data
        apiUtils = new ApiUtils(this);
        apiInterface = apiUtils.getInterface(base_url);

        // data controller
        deleteItemFromTransferInInterface = this;
        dataControllerTransferIn = DataControllerTransferIn.getInstance();
        dataControllerTransferIn.setClickInterface(deleteItemFromTransferInInterface);

        //all buttons
        binding.locationScanBT.setOnClickListener(view -> startScanning(1));
//        binding.barcodeScanBT.setOnClickListener(view -> startScanning(2));
        binding.barcodeScanBT.setOnClickListener(view -> {
            if (binding.locationScanTV.getText().toString().trim().equals("")) {
                showMessageWithSweetAlert("At first scan location please...");
            } else {
                startScanning(2);
            }
        });
        binding.saveBT.setOnClickListener(view -> showAlertMessageForGettingPermission("Are you want to save?"));
        binding.resetBT.setOnClickListener(view -> resetFullPage());

        //call and get default data when activity started
        getDefaultData();
    }

    private void resetFullPage() {
        binding.locationScanTV.setText("");
        binding.barcodeScanTV.setText("");
        listOfBarcodeOnlySingleUI.clear();
        callAdapter(listOfBarcodeOnlySingleUI);
        binding.locationScanBT.setClickable(true);
    }

    private void postDataToServer() {
        if (binding.locationScanTV.getText().toString().trim().equals("")) {
            showMessageWithSweetAlert("Please set a location...");
        } else {
            JSONObject save_obj = new JSONObject();
            JSONObject data_obj = new JSONObject();
            JSONObject master_obj = new JSONObject();
            JSONArray dtls_arr = new JSONArray();

            try {
                master_obj.put("COMPANY_ID", company_id);
                master_obj.put("APP_USER_ID", user_id);
                master_obj.put("ROOM_RACK_DTLS_ID", separated_location_id);
                master_obj.put("TXT_CHALLAN_NO", "");

                data_obj.put("MasterPart", master_obj);

                for (int i = 0; i < listOfBarcodeOnlySingleUI.size(); i++) {
                    JSONObject dtls_obj = new JSONObject();
                    dtls_obj.put("BARCODE_NO", listOfBarcodeOnlySingleUI.get(i).getBARCODENO());
                    dtls_obj.put("ROLL_ID", listOfBarcodeOnlySingleUI.get(i).getROLLID());
                    dtls_arr.put(dtls_obj);
                }

                data_obj.put("DtlsPart", dtls_arr);
                save_obj.put("resultset", data_obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String url = base_url + "roll_wise_grey_sales_order_to_sales_order_in";

            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Cookie", "ci_session=da99116590782e330450d34960a934c0");

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, save_obj, response -> {
                try {
                    String statusCode = response.getString("STATUS");
                    if (statusCode.equals("200")) {
                        resetFullPage();
                        showAlertMessageSaveData(response.getString("MSG"));
                    } else {
                        showMessageWithSweetAlert(response.getString("MSG"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error ->
                    showMessageWithSweetAlert("Please Try Again...")) {
                @Override
                public Map<String, String> getHeaders() {
                    return headers;
                }
            };

            Volley.newRequestQueue(this).add(jsonObjectRequest);
        }
    }

    private void startScanning(int op) {
        Intent intent = new Intent(this, V1_ScannerActivity.class);
        intent.putExtra("qc", "transfer_in_v1");
        intent.putExtra("transfer_in___scan_op", op);
        intent.putExtra("transfer_in__listOfAllBarcode", (Serializable) listOfBarcodeOnlySingleUI);
        if (op == 2) {
            intent.putExtra("transfer_in__location_id", separated_location_id);
            intent.putExtra("transfer_in__location_name", separated_location_name);
            intent.putExtra("transfer_in__company_id", company_id);
            intent.putExtra("transfer_in__po_breakdown_id", po_breakdown_id);
        }
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    private void getDefaultData() {
        Intent intent = getIntent();
        String barcode_scan_result = intent.getStringExtra("barcode_scan");
        String scan_op = intent.getStringExtra("transfer_in___scan_op");
        String location_id = intent.getStringExtra("transfer_in__location_id");
        String location_name = intent.getStringExtra("transfer_in__location_name");
        String companyId = intent.getStringExtra("transfer_in__company_id");
        String poBreakdownId = intent.getStringExtra("transfer_in__po_breakdown_id");
        ArrayList<DtlsPart> listOfAllBarcode = (ArrayList<DtlsPart>) intent.getSerializableExtra("transfer_in__listOfAllBarcode");

        if (scan_op != null) {
            if (scan_op.equals("1")) {
                //separated location
                try {
                    String[] location_split_array = barcode_scan_result.split(Pattern.quote("***"));
                    separated_location_id = location_split_array[0];
                    separated_location_name = location_split_array[1];
                    binding.locationScanTV.setText(separated_location_name);
                } catch (Exception e) {
                    // need this type of location - 123***LocationName
                    showMessageWithSweetAlert("Please set valid location barcode");
                }
            } else if (scan_op.equals("2")) {
                separated_location_id = location_id;
                separated_location_name = location_name;

                company_id = companyId;
                po_breakdown_id = poBreakdownId;

                binding.barcodeScanTV.setText(barcode_scan_result);
                binding.locationScanTV.setText(separated_location_name);
                binding.locationScanBT.setClickable(false);

                //request for barcode and set list data
                requestForBarcode(barcode_scan_result, separated_location_id, listOfAllBarcode);
            }
        } else {
            // no implementation
        }
    }

    private void requestForBarcode(String barcodeScan, String separated_location_id, ArrayList<DtlsPart> listOfAllBarcode) {
        apiInterface.transferInBarcodeData(barcodeScan, separated_location_id, "1").enqueue(new Callback<TransferOutResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<TransferOutResponse> call, Response<TransferOutResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        if ((response.body().getStatus()).equals("200")) {
                            if (po_breakdown_id.equals("")) {
                                //add barcode after save ID
                                po_breakdown_id = response.body().getResultset().getDtlsPart().getPOBREAKDOWNID();
                                addNewBarcodeInListAfterCheckAllId(response, listOfAllBarcode);
                            } else if (po_breakdown_id.equals(response.body().getResultset().getDtlsPart().getPOBREAKDOWNID())) {
                                //add barcode after matching ID
                                addNewBarcodeInListAfterCheckAllId(response, listOfAllBarcode);
                            } else {
                                // no add barcode and set old list of barcode, because id not same with first barcode information
                                showMessageWithSweetAlert("Not Found This Barcode - " + barcodeScan + ", PO-" + po_breakdown_id);
                                setOldBarcodeList(listOfAllBarcode);
                            }
                        } else if ((response.body().getStatus() + "").equals("404")) {
                            showMessageWithSweetAlert("Not Found This Barcode - " + barcodeScan);
                            setOldBarcodeList(listOfAllBarcode);
                        }
                    } else {
                        showMessageWithSweetAlert("Not Found This Barcode - " + barcodeScan);
                        setOldBarcodeList(listOfAllBarcode);
                    }
                } catch (Exception e) {
                    showMessageWithSweetAlert("No Barcode Found - " + barcodeScan);
                    setOldBarcodeList(listOfAllBarcode);
                }
            }

            @Override
            public void onFailure(Call<TransferOutResponse> call, Throwable t) {
                showMessageWithSweetAlert("No Barcode Found - " + barcodeScan);
                setOldBarcodeList(listOfAllBarcode);
            }
        });
    }

    private void setOldBarcodeList(ArrayList<DtlsPart> listOfAllBarcode) {
        listOfBarcodeOnlySingleUI.clear();
        listOfBarcodeOnlySingleUI = listOfAllBarcode;
        callAdapter(listOfBarcodeOnlySingleUI);
    }


    private void addNewBarcodeInListAfterCheckAllId(Response<TransferOutResponse> response, ArrayList<DtlsPart> listOfAllBarcode) {
        listOfBarcodeOnlySingleUI.clear();
        listOfBarcodeOnlySingleUI = listOfAllBarcode;

        DtlsPart single_dtlsPart = response.body().getResultset().getDtlsPart();

        boolean alreadyTaken = false;
        try {
            for (int j = 0; j <= listOfBarcodeOnlySingleUI.size(); j++) {
                String selected_barcode = listOfBarcodeOnlySingleUI.get(j).getBARCODENO().toString().trim() + "";
                String single_barcode = single_dtlsPart.getBARCODENO().toString().trim() + "";
                if (single_barcode.equals(selected_barcode)) {
                    alreadyTaken = true;
                    break;
                }
            }
        } catch (Exception e) {
            //no code
        }
        if (alreadyTaken) {
            showMessageWithSweetAlertForDuplicatedValueCheck("Already scan this barcode - " + single_dtlsPart.getBARCODENO().trim());
        } else {
            company_id = response.body().getResultset().getMasterPart().getCOMPANYID() + "";
            listOfAllBarcode.add(single_dtlsPart);
            handler.postDelayed(() -> startScanning(2), SPLASH_DISPLAY_LENGTH);
        }
        callAdapter(listOfBarcodeOnlySingleUI);
    }


    @SuppressLint("NotifyDataSetChanged")
    private void callAdapter(List<DtlsPart> listOfBarcode) {
        adapter = new V1_transfer_in_recycler_adapter(V1_TransferInActivity.this, listOfBarcode);
        binding.barcodeRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void deleteItemFromTransferInRecycleView(DtlsPart singleDtlsPart) {
        showAlertMessageDeleteItem(singleDtlsPart);
    }

    private void showAlertMessageDeleteItem(DtlsPart singleDtlsPart) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message")
                .setMessage("Do you want to delete this?")
                .setCancelable(true)
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Ok", (dialog, which) -> {
                    listOfBarcodeOnlySingleUI.remove(singleDtlsPart);
                    callAdapter(listOfBarcodeOnlySingleUI);
                    dialog.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAlertMessageSaveData(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message")
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAlertMessageForGettingPermission(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message")
                .setMessage(msg)
                .setCancelable(true)
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setPositiveButton("Ok", (dialog, which) -> {
                    postDataToServer();
                    dialog.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showMessageWithSweetAlert(String msg) {
        new SweetAlertDialog(V1_TransferInActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Message")
                .setContentText(msg)
                .setConfirmText("ok")
                .show();
    }

    private void showMessageWithSweetAlertForDuplicatedValueCheck(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message")
                .setMessage(msg)
                .setCancelable(false)
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setPositiveButton("Scan Again", (dialog, which) -> {
                    startScanning(2);
                    dialog.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}