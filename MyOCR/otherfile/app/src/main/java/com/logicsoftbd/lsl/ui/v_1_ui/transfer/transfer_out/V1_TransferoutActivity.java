package com.logicsoftbd.lsl.ui.v_1_ui.transfer.transfer_out;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_transfer_out.V1GreyFabricTransferOutStoreList;
import com.logicsoftbd.lsl.data.network.v1_model.V1_transfer_out.V1GreyFabricTransferOutStoreListResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_transfer_out.barcode.DtlsPart;
import com.logicsoftbd.lsl.data.network.v1_model.V1_transfer_out.barcode.TransferOutResponse;
import com.logicsoftbd.lsl.databinding.ActivityV1TransferoutBinding;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_ScannerActivity;
import com.logicsoftbd.lsl.utils.ApiUtils;
import com.logicsoftbd.lsl.utils.DialogHelper;

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

public class V1_TransferoutActivity extends AppCompatActivity implements DeleteItemFromTransferOutInterface, AdapterView.OnItemSelectedListener {
    private static final String TAG = "V1_TransferoutActivity";
    public ActivityV1TransferoutBinding binding;
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;
    String user_id, base_url;

    //master & dtls part variable catching
    String company_id = "";
    String po_breakdown_id = "", floor_id = "", room_id = "", rack_id = "", shelf_id = "", bin_id = "", item_category_id = "";

    //location related variable
    String separated_location_id, separated_location_name;

    //adapter for recyclerView
    V1_transfer_out_recycler_adapter adapter;
    List<DtlsPart> listOfBarcodeOnlySingleUI = new ArrayList<>();

    //data controller && click interface
    DeleteItemFromTransferOutInterface deleteItemFromTransferOutInterface;
    DataControllerTransferOut dataControllerTransferOut;

    //handler
    private static final int SPLASH_DISPLAY_LENGTH = 1200;
    private Handler handler;

    // spinner for stores
    ArrayAdapter<String> spinnerAdapter;
    private ArrayList<String> storeIdList = new ArrayList<>();
    private ArrayList<String> storeNameList = new ArrayList<>();
    private int selectedStoreIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityV1TransferoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //recycler view layout setup
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        binding.barcodeRecyclerView.setLayoutManager(layoutManager);
        binding.barcodeRecyclerView.setHasFixedSize(true);

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = _preferences.getString("login_userid", "");
        base_url = (_preferences.getString("base_url", ""));

        apiUtils = new ApiUtils(this);
        apiInterface = apiUtils.getInterface(base_url);

        //handler
        handler = new Handler();

        //listener
        binding.storeSpinner.setOnItemSelectedListener(this);

        deleteItemFromTransferOutInterface = this;
        dataControllerTransferOut = DataControllerTransferOut.getInstance();
        dataControllerTransferOut.setClickInterface(deleteItemFromTransferOutInterface);

        binding.locationScanBT.setOnClickListener(view -> startScanning(1));
        binding.barcodeScanBT.setOnClickListener(view -> {
            if (binding.locationScanTV.getText().toString().trim().equals("")) {
                showMessageWithSweetAlert("At first scan location please...");
            } else {
                startScanning(2);
            }
        });
        binding.saveBT.setOnClickListener(view -> showAlertMessageForGettingPermission("Do you want to save?"));
        binding.resetBT.setOnClickListener(view -> {
            binding.locationScanTV.setText("");
            binding.barcodeScanTV.setText("");
            binding.remarksET.setText("");
            listOfBarcodeOnlySingleUI.clear();
            callAdapter((ArrayList<DtlsPart>) listOfBarcodeOnlySingleUI);
            storeIdList.clear();
            storeNameList.clear();
            callAdapterForSpinner(storeNameList);
            binding.locationScanBT.setClickable(true);
        });

        getDefaultData();
    }

    private void callAdapterForSpinner(ArrayList<String> storeNameList) {
        spinnerAdapter = new ArrayAdapter<>(V1_TransferoutActivity.this, android.R.layout.simple_spinner_item, storeNameList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Optional for dropdown layout
        binding.storeSpinner.setAdapter(spinnerAdapter);
    }

    private void postDataToServer() {
        setVisibilityForMainProgressBar(View.VISIBLE);
        setVisibilityForFullLayout(View.GONE);

        JSONObject save_obj = new JSONObject();
        JSONObject data_obj = new JSONObject();
        JSONObject master_obj = new JSONObject();
        JSONArray dtls_arr = new JSONArray();

        try {
            master_obj.put("COMPANY_ID", company_id);
            master_obj.put("APP_USER_ID", user_id);
            master_obj.put("ROOM_RACK_DTLS_ID", separated_location_id);
            master_obj.put("TXT_CHALLAN_NO", "");
            master_obj.put("REMARKS", binding.remarksET.getText().toString().trim());
            master_obj.put("TO_STORE_ID", storeIdList.get(selectedStoreIndex));

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

        String url = base_url + "roll_wise_grey_sales_order_to_sales_order_out";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Cookie", "ci_session=da99116590782e330450d34960a934c0");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, save_obj, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String statusCode = response.getString("STATUS");
                    if (statusCode.equals("200")) {
                        storeIdList.clear();
                        storeNameList.clear();
                        callAdapterForSpinner(storeNameList);
                        listOfBarcodeOnlySingleUI.clear();
                        callAdapter((ArrayList<DtlsPart>) listOfBarcodeOnlySingleUI);
                        binding.locationScanTV.setText("");
                        binding.barcodeScanTV.setText("");
                        showAlertMessageSaveData(response.getString("MSG") + "\nSystem ID- " + response.getString("SYSTEM_ID"));
                    } else {
                        showMessageWithSweetAlert(response.getString("MSG"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showMessageWithSweetAlert("Please Try Again...");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);

        setVisibilityForMainProgressBar(View.GONE);
        setVisibilityForFullLayout(View.VISIBLE);
    }

    private void startScanning(int op) {
        Intent intent = new Intent(this, V1_ScannerActivity.class);
        intent.putExtra("qc", "transfer_out_v1");
        intent.putExtra("transfer_out___scan_op", op);
        intent.putExtra("transfer_out__listOfAllBarcode", (Serializable) listOfBarcodeOnlySingleUI);
        intent.putExtra("transfer_out__storeIdList", storeIdList);
        intent.putExtra("transfer_out__storeNameList", storeNameList);
        if (op == 2) {
            intent.putExtra("transfer_out__location_id", separated_location_id);
            intent.putExtra("transfer_out__location_name", separated_location_name);
            intent.putExtra("transfer_out__company_id", company_id);

            intent.putExtra("transfer_out__po_breakdown_id", po_breakdown_id);
            intent.putExtra("transfer_out__floor_id", floor_id);
            intent.putExtra("transfer_out__room_id", room_id);
            intent.putExtra("transfer_out__rack_id", rack_id);
            intent.putExtra("transfer_out__shelf_id", shelf_id);
            intent.putExtra("transfer_out__bin_id", bin_id);
            intent.putExtra("transfer_out__item_category_id", item_category_id);
        }
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    private void getDefaultData() {
        setVisibilityForMainProgressBar(View.VISIBLE);
        setVisibilityForFullLayout(View.GONE);

        Intent intent = getIntent();
        String barcode_scan_result = intent.getStringExtra("barcode_scan");
        String scan_op = intent.getStringExtra("transfer_out___scan_op");
        String location_id = intent.getStringExtra("transfer_out__location_id");
        String location_name = intent.getStringExtra("transfer_out__location_name");
        String companyId = intent.getStringExtra("transfer_out__company_id");

        String poBreakdownId = intent.getStringExtra("transfer_out__po_breakdown_id");
        String floorId = intent.getStringExtra("transfer_out__floor_id");
        String roomId = intent.getStringExtra("transfer_out__room_id");
        String rackId = intent.getStringExtra("transfer_out__rack_id");
        String shelfId = intent.getStringExtra("transfer_out__shelf_id");
        String binId = intent.getStringExtra("transfer_out__bin_id");
        String itemCategoryId = intent.getStringExtra("transfer_out__item_category_id");

        ArrayList<DtlsPart> listOfAllBarcode = (ArrayList<DtlsPart>) intent.getSerializableExtra("transfer_out__listOfAllBarcode");
        ArrayList<String> store_Id_List = intent.getStringArrayListExtra("transfer_out__storeIdList");
        ArrayList<String> store_Name_List = intent.getStringArrayListExtra("transfer_out__storeNameList");

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
                floor_id = floorId;
                room_id = roomId;
                rack_id = rackId;
                shelf_id = shelfId;
                bin_id = binId;
                item_category_id = itemCategoryId;

                storeNameList.clear();
                storeNameList = store_Name_List;
                storeIdList.clear();
                storeIdList = store_Id_List;

                binding.barcodeScanTV.setText(barcode_scan_result);
                binding.locationScanTV.setText(separated_location_name);

                binding.locationScanBT.setClickable(false);
                //request for barcode and set list data
                requestForBarcode(barcode_scan_result, separated_location_id, listOfAllBarcode);
            }
        } else {
            // no implementation
        }
        setVisibilityForMainProgressBar(View.GONE);
        setVisibilityForFullLayout(View.VISIBLE);
    }

    private void restoreStoreList(ArrayList<String> storeIdList, ArrayList<String> storeNameList) {
        callAdapterForSpinner(storeNameList);
    }

    private void requestForStoreList(String user_id, String category_item_id, String company_id) {
        apiInterface.store_name_list_for_grey_fabric_transfer_out(user_id, category_item_id, company_id).enqueue(new Callback<V1GreyFabricTransferOutStoreListResponse>() {
            @Override
            public void onResponse(Call<V1GreyFabricTransferOutStoreListResponse> call, Response<V1GreyFabricTransferOutStoreListResponse> response) {
                Log.d(TAG, "onResponse: store"+response.toString());
                if(response.isSuccessful() && response.body().getStores() != null && response.body().getStores().size() > 0) {
                    List<V1GreyFabricTransferOutStoreList> stores = response.body().getStores();
                    storeNameList.clear();
                    storeIdList.clear();
                    for (int i = 0; i < stores.size(); i++) {
                        storeIdList.add(stores.get(i).getSTOREID());
                        storeNameList.add(stores.get(i).getSTORENAME());
                    }
                    callAdapterForSpinner(storeNameList);
                }else{
                    DialogHelper.showWarningDialog(V1_TransferoutActivity.this, "Message", "Store not found.");
                }
            }

            @Override
            public void onFailure(Call<V1GreyFabricTransferOutStoreListResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: no store found"+t.getMessage());
                DialogHelper.showErrorDialog(V1_TransferoutActivity.this, "Message", "Something went wrong.");
            }
        });
    }

    private void requestForBarcode(String barcodeScan, String separated_location_id, ArrayList<DtlsPart> listOfAllBarcode) {
        setVisibilityForMainProgressBar(View.VISIBLE);
        setVisibilityForFullLayout(View.GONE);

        apiInterface.transferOutBarcodeData(barcodeScan, separated_location_id, "0").enqueue(new Callback<TransferOutResponse>() {
            @Override
            public void onResponse(Call<TransferOutResponse> call, Response<TransferOutResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse: "+response.toString());
                        if ((response.body().getStatus()).equals("200")) {
                            if (po_breakdown_id.equals("") && floor_id.equals("") && room_id.equals("") && rack_id.equals("") && shelf_id.equals("") && bin_id.equals("")) {
                                //add barcode after save ID
                                po_breakdown_id = response.body().getResultset().getDtlsPart().getPOBREAKDOWNID();
                                floor_id = response.body().getResultset().getDtlsPart().getfLOOR_ID();
                                room_id = response.body().getResultset().getDtlsPart().getrOOM_ID();
                                rack_id = response.body().getResultset().getDtlsPart().getrACK_ID();
                                shelf_id = response.body().getResultset().getDtlsPart().getsHELF_ID();
                                bin_id = response.body().getResultset().getDtlsPart().getbIN_ID();
                                item_category_id = response.body().getResultset().getDtlsPart().getItemCategoryId();
                                addNewBarcodeInListAfterCheckAllId(response, listOfAllBarcode);
                                requestForStoreList(user_id, item_category_id, company_id);
                            } else if (po_breakdown_id.equals(response.body().getResultset().getDtlsPart().getPOBREAKDOWNID()) &&
                                    floor_id.equals(response.body().getResultset().getDtlsPart().getfLOOR_ID()) &&
                                    room_id.equals(response.body().getResultset().getDtlsPart().getrOOM_ID()) &&
                                    rack_id.equals(response.body().getResultset().getDtlsPart().getrACK_ID()) &&
                                    shelf_id.equals(response.body().getResultset().getDtlsPart().getsHELF_ID()) &&
                                    item_category_id.equals(response.body().getResultset().getDtlsPart().getItemCategoryId()) &&
                                    bin_id.equals(response.body().getResultset().getDtlsPart().getbIN_ID())) {
                                //add barcode after matching ID
                                addNewBarcodeInListAfterCheckAllId(response, listOfAllBarcode);
                                restoreStoreList(storeIdList, storeNameList);
                            } else {
                                // no add barcode and set old list of barcode, because id not same with first barcode information
                                showMessageWithSweetAlert("Not Found This Barcode - " + barcodeScan + ", room-" + room_id + ", rack-" + rack_id + ", shelf-" + shelf_id);
                                setOldBarcodeList(listOfAllBarcode);
                                restoreStoreList(storeIdList, storeNameList);
                            }
                        } else {
                            showMessageWithSweetAlert("Not Found This Barcode - " + barcodeScan);
                            setOldBarcodeList(listOfAllBarcode);
                            restoreStoreList(storeIdList, storeNameList);
                        }
                    } else {
                        showMessageWithSweetAlert("Not Found This Barcode - " + barcodeScan);
                        setOldBarcodeList(listOfAllBarcode);
                        restoreStoreList(storeIdList, storeNameList);
                    }
                } catch (Exception e) {
                    showMessageWithSweetAlert("Not Found This Barcode - " + barcodeScan);
                    setOldBarcodeList(listOfAllBarcode);
                    restoreStoreList(storeIdList, storeNameList);
                }
            }

            @Override
            public void onFailure(Call<TransferOutResponse> call, Throwable t) {
                showMessageWithSweetAlert("Not Found This Barcode - " + barcodeScan);
                setOldBarcodeList(listOfAllBarcode);
                restoreStoreList(storeIdList, storeNameList);
            }
        });

        setVisibilityForMainProgressBar(View.GONE);
        setVisibilityForFullLayout(View.VISIBLE);
    }

    private void setOldBarcodeList(ArrayList<DtlsPart> listOfAllBarcode) {
        listOfBarcodeOnlySingleUI.clear();
        listOfBarcodeOnlySingleUI = listOfAllBarcode;
        callAdapter((ArrayList<DtlsPart>) listOfBarcodeOnlySingleUI);
    }

    private void showMessageWithSweetAlert(String msg) {
        new SweetAlertDialog(V1_TransferoutActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Message")
                .setContentText(msg)
                .setConfirmText("OK")
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

    @SuppressLint("NotifyDataSetChanged")
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
        callAdapter((ArrayList<DtlsPart>) listOfBarcodeOnlySingleUI);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void callAdapter(ArrayList<DtlsPart> listOfAllBarcode) {
        adapter = new V1_transfer_out_recycler_adapter(V1_TransferoutActivity.this, listOfAllBarcode);
        binding.barcodeRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void deleteItemFromTransferOutRecycleView(DtlsPart singleDtlsPart) {
        showAlertMessageDeleteItem(singleDtlsPart);
    }

    private void showAlertMessageDeleteItem(DtlsPart singleDtlsPart) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message")
                .setMessage("Do you want to delete this?")
                .setCancelable(true)
                .setPositiveButton("Ok", (dialog, which) -> {
                    listOfBarcodeOnlySingleUI.remove(singleDtlsPart);
                    callAdapter((ArrayList<DtlsPart>) listOfBarcodeOnlySingleUI);
                    dialog.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setVisibilityForMainProgressBar(int visible) {
        binding.transferOutMainProgressbar.setVisibility(visible);
    }

    private void setVisibilityForFullLayout(int visible) {
        binding.transferOutFullLayout.setVisibility(visible);
    }

    private void showAlertMessageSaveData(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message")
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int id = adapterView.getId();
        if (id == R.id.storeSpinner) {
            selectedStoreIndex = i;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}