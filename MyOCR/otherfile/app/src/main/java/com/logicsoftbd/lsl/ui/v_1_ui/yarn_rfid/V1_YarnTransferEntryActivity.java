package com.logicsoftbd.lsl.ui.v_1_ui.yarn_rfid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_RFIDTransferDropdownModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_RFIDTransferModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_RFIDTransferSaveModel;
import com.logicsoftbd.lsl.utils.CommonUtils;
import com.logicsoftbd.lsl.utils.DialogHelper;
import com.logicsoftbd.lsl.utils.VerticalSpacingItemDecorator;
import com.logicsoftbd.lsl.viewModel.RFIDTransferViewModal;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class V1_YarnTransferEntryActivity extends AppCompatActivity {
    private static final String TAG = "V1_YarnTransferEntryAct";
    private RFIDTransferViewModal rfidTransferViewModal;
    private RecyclerView recyclerView;
    private V1_RFIDTransferStoreHeaderRecyclerAdapter rfidTransferStoreRecyclerAdapter;
    private ArrayList<V1_RFIDTransferModel.Datum> rfidTransferModel = new ArrayList<>();
    private ArrayList<V1_RFIDTransferDropdownModel.Datum> rfidTransferDropdownModel = new ArrayList<>();
    private ArrayList<V1_RFIDTransferDropdownModel.Datum> toRFIDTransferDropdownModel = new ArrayList<>();
    private Spinner _companySpinner, _fromStoreSpinner, _fromFloorSpinner, _fromRoomSpinner, _fromRackSpinner, _fromShelfSpinner, _fromBinBoxSpinner, _toStoreSpinner, _toFloorSpinner, _toRoomSpinner, _toRackSpinner, _toShelfSpinner, _toBinBoxSpinner;
    private String _companyId, _fromStoreId, _fromFloorId, _fromRoomId, _fromRackId, _fromShelfId, _fromBinBoxId, _toStoreId, _toFloorId, _toRoomId, _toRackId, _toShelfId, _toBinBoxId = "0";
    private ProgressDialog mProgressDialog;
    private String base_url, userID;
    private CheckBox _selectAllBarcodeCheckbox;
    private ArrayList<String> companyNameList = new ArrayList<>();
    private ArrayList<String> companyIdList = new ArrayList<>();
    private ArrayList<String> storeNameList = new ArrayList<>();
    private ArrayList<String> storeIdList = new ArrayList<>();
    private ArrayList<String> floorNameList = new ArrayList<>();
    private ArrayList<String> floorIdList = new ArrayList<>();
    private ArrayList<String> roomNameList = new ArrayList<>();
    private ArrayList<String> roomIdList = new ArrayList<>();
    private ArrayList<String> rackNameList = new ArrayList<>();
    private ArrayList<String> rackIdList = new ArrayList<>();
    private ArrayList<String> shelfNameList = new ArrayList<>();
    private ArrayList<String> shelfIdList = new ArrayList<>();
    private ArrayList<String> binNameList = new ArrayList<>();
    private ArrayList<String> binIdList = new ArrayList<>();
    private ArrayList<String> toStoreNameList = new ArrayList<>();
    private ArrayList<String> toStoreIdList = new ArrayList<>();
    private ArrayList<String> toFloorNameList = new ArrayList<>();
    private ArrayList<String> toFloorIdList = new ArrayList<>();
    private ArrayList<String> toRoomNameList = new ArrayList<>();
    private ArrayList<String> toRoomIdList = new ArrayList<>();
    private ArrayList<String> toRackNameList = new ArrayList<>();
    private ArrayList<String> toRackIdList = new ArrayList<>();
    private ArrayList<String> toShelfNameList = new ArrayList<>();
    private ArrayList<String> toShelfIdList = new ArrayList<>();
    private ArrayList<String> toBinNameList = new ArrayList<>();
    private ArrayList<String> toBinIdList = new ArrayList<>();
    private V1_RFIDTransferSaveModel rfidTransferSaveModel = new V1_RFIDTransferSaveModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_yarn_transfer_entry);

        rfidTransferViewModal = new ViewModelProvider(this).get(RFIDTransferViewModal.class);

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        base_url = (_preferences.getString("base_url", ""));
        userID = _preferences.getString("login_userid", "");

        init_ui();
        initRecyclerView();

        sendRequestForCompanyDropdown();
//        sendRequestForRFIDTransferDropdown();
    }

    private void sendRequestForCompanyDropdown() {
        rfidTransferViewModal.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                showLoading();
            } else {
                hideLoading();
            }
        });

        rfidTransferViewModal.getCompanyResponse().observe(this, apiResponse -> {
            if(apiResponse != null && apiResponse.getResultset() != null){

                Log.d(TAG, "sendRequestForRFIDTransferDropdown: "+rfidTransferDropdownModel.size());
                if(rfidTransferDropdownModel.size() > 0 || rfidTransferModel != null){
                    try{
                        if(apiResponse.getResultset().getCompany().size() > 0){
                            companyNameList.clear();
                            companyIdList.clear();
                            for(int i=0; i<apiResponse.getResultset().getCompany().size() ; i++){

                                if(apiResponse.getResultset().getCompany().get(i).getCompany() != null && apiResponse.getResultset().getCompany().get(i).getId() != null){
                                    companyNameList.add(apiResponse.getResultset().getCompany().get(i).getCompany());
                                    companyIdList.add(String.valueOf(apiResponse.getResultset().getCompany().get(i).getId()));
                                }
                            }

                            try {
                                setUpCompanySpinner();
                            } catch (Exception e){
                                Log.d(TAG, "fetchCompactingData: "+e.getMessage());
                            }

                        }
                    } catch (Exception e){
                        Log.d(TAG, "sendRequestForRFIDTransferDropdown: "+e.getMessage());
                    }

                    try {
                        setUpStoreSpinner();
                        setUpToStoreSpinner();
                    } catch (Exception e){
                        Log.d(TAG, "fetchCompactingData: "+e.getMessage());
                    }
                }
            } else {
                DialogHelper.showErrorDialog(V1_YarnTransferEntryActivity.this, "Message", "Something went wrong!");
            }
        });
    }
    private void sendRequestForRFIDTransferDropdown() {
        rfidTransferViewModal.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                showLoading();
            } else {
                hideLoading();
            }
        });

        rfidTransferViewModal.getRFIDTransferStoreDropdownResponse(_companyId, userID,"0", "0", "0", "0", "0", "0").observe(this, apiResponse -> {
            if(apiResponse != null && apiResponse.getData() != null){
                rfidTransferDropdownModel.clear();
                rfidTransferDropdownModel.addAll(apiResponse.getData());
                toRFIDTransferDropdownModel.clear();
                toRFIDTransferDropdownModel.addAll(apiResponse.getData());
                Log.d(TAG, "sendRequestForRFIDTransferDropdown: "+rfidTransferDropdownModel.size());
                if(rfidTransferDropdownModel.size() > 0 || rfidTransferModel != null){
                    try{
                        List<String> storeNameListTemp = new ArrayList<>();
                        List<String> storeIdListTemp = new ArrayList<>();

                        for (int i = 0; i < apiResponse.getData().size(); i++) {
                            if (apiResponse.getData().get(i).getStoreName() != null && apiResponse.getData().get(i).getStoreId() != null) {
                                storeNameListTemp.add(apiResponse.getData().get(i).getStoreName());
                                storeIdListTemp.add(apiResponse.getData().get(i).getStoreId());
                            }
                        }

                        // Using LinkedHashSet to maintain order and uniqueness
                        Set<String> uniqueStoreNameSet = new LinkedHashSet<>(storeNameListTemp);
                        Set<String> uniqueStoreIdSet = new LinkedHashSet<>(storeIdListTemp);

                        // Clear existing lists
                        storeNameList.clear();
                        storeIdList.clear();

                        // Add unique elements back to the lists
                        storeNameList.addAll(uniqueStoreNameSet);
                        storeIdList.addAll(uniqueStoreIdSet);

                        // Add "-Select-" option
                        storeNameList.add(0, "-Select-");
                        storeIdList.add(0, "0");

                        toStoreNameList = new ArrayList<>(uniqueStoreNameSet);
                        toStoreIdList = new ArrayList<>(uniqueStoreIdSet);

                        toStoreNameList.add(0, "-Select-");
                        toStoreIdList.add(0, "0");

                    } catch (Exception e){
                        Log.d(TAG, "sendRequestForRFIDTransferDropdown: "+e.getMessage());
                    }

                    try {
                        setUpStoreSpinner();
                        setUpToStoreSpinner();
                    } catch (Exception e){
                        Log.d(TAG, "fetchCompactingData: "+e.getMessage());
                    }
                }
            } else {
                DialogHelper.showErrorDialog(V1_YarnTransferEntryActivity.this, "Message", "Something went wrong!");
            }
        });
    }

    private void setUpCompanySpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.custome_spinner_dropdown, companyNameList);
        _companySpinner.setAdapter(spinnerArrayAdapter);

        _companySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _companyId = companyIdList.get(position);

                sendRequestForRFIDTransferDropdown();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpStoreSpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.custome_spinner_dropdown, storeNameList);
        _fromStoreSpinner.setAdapter(spinnerArrayAdapter);

        ArrayList<String> _floorNameList = new ArrayList<>();
        ArrayList<String> _floorIdList = new ArrayList<>();
        _fromStoreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Clear the existing lists
                floorNameList.clear();
                floorIdList.clear();
                roomNameList.clear();
                roomIdList.clear();
                rackNameList.clear();
                rackIdList.clear();
                shelfNameList.clear();
                shelfIdList.clear();
                binNameList.clear();
                binIdList.clear();

// Set the selected store ID
                _fromStoreId = storeIdList.get(position);

// Temporary lists to hold floor names and IDs
                List<String> _floorNameListTemp = new ArrayList<>();
                List<String> _floorIdListTemp = new ArrayList<>();

// Populate temporary lists with floor names and IDs for the selected store
                for (V1_RFIDTransferDropdownModel.Datum model : rfidTransferDropdownModel) {
                    if (model.getStoreId().equals(storeIdList.get(position))) {
                        if (model.getFloorName() != null && model.getFloorId() != null) {
                            _floorNameListTemp.add(model.getFloorName());
                            _floorIdListTemp.add(model.getFloorId());
                        }
                    }
                }

// Clear the existing lists
                floorNameList.clear();
                floorIdList.clear();

// Use LinkedHashSet to maintain order and uniqueness
                Set<String> uniqueFloorNameSet = new LinkedHashSet<>(_floorNameListTemp);
                Set<String> uniqueFloorIdSet = new LinkedHashSet<>(_floorIdListTemp);

// Add unique elements back to the lists
                floorNameList.addAll(uniqueFloorNameSet);
                floorIdList.addAll(uniqueFloorIdSet);

// Set up the spinners
                setUpFloorSpinner();
                setUpRoomSpinner();
                setUpRackSpinner();
                setUpShelfSpinner();
                setUpBinSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpFloorSpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.custome_spinner_dropdown, floorNameList);
        _fromFloorSpinner.setAdapter(spinnerArrayAdapter);
        ArrayList<String> _roomNameList = new ArrayList<>();
        ArrayList<String> _roomIdList = new ArrayList<>();
        _fromFloorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Clear the existing lists
                roomNameList.clear();
                roomIdList.clear();
                rackNameList.clear();
                rackIdList.clear();
                shelfNameList.clear();
                shelfIdList.clear();
                binNameList.clear();
                binIdList.clear();

// Set the selected floor ID
                _fromFloorId = floorIdList.get(position);

// Temporary lists to hold room names and IDs
                List<String> _roomNameListTemp = new ArrayList<>();
                List<String> _roomIdListTemp = new ArrayList<>();

// Populate temporary lists with room names and IDs for the selected store and floor
                for (V1_RFIDTransferDropdownModel.Datum model : rfidTransferDropdownModel) {
                    if (model.getStoreId().equals(_fromStoreId) && model.getFloorId().equals(floorIdList.get(position))) {
                        if (model.getRoomName() != null && model.getRoomId() != null) {
                            _roomNameListTemp.add(model.getRoomName());
                            _roomIdListTemp.add(model.getRoomId());
                        }
                    }
                }

// Use LinkedHashSet to maintain order and uniqueness
                Set<String> uniqueRoomNameSet = new LinkedHashSet<>(_roomNameListTemp);
                Set<String> uniqueRoomIdSet = new LinkedHashSet<>(_roomIdListTemp);

// Add unique elements back to the lists
                roomNameList.clear();
                roomIdList.clear();
                roomNameList.addAll(uniqueRoomNameSet);
                roomIdList.addAll(uniqueRoomIdSet);

// Set up the spinners
                setUpRoomSpinner();
                setUpRackSpinner();
                setUpShelfSpinner();
                setUpBinSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpRoomSpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.custome_spinner_dropdown, roomNameList);
        _fromRoomSpinner.setAdapter(spinnerArrayAdapter);
        ArrayList<String> _rackNameList = new ArrayList<>();
        ArrayList<String> _rackIdList = new ArrayList<>();
        _fromRoomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Clear the existing lists
                rackNameList.clear();
                rackIdList.clear();
                shelfNameList.clear();
                shelfIdList.clear();
                binNameList.clear();
                binIdList.clear();

// Set the selected room ID
                _fromRoomId = roomIdList.get(position);

// Temporary lists to hold rack names and IDs
                List<String> _rackNameListTemp = new ArrayList<>();
                List<String> _rackIdListTemp = new ArrayList<>();

// Populate temporary lists with rack names and IDs for the selected store, floor, and room
                for (V1_RFIDTransferDropdownModel.Datum model : rfidTransferDropdownModel) {
                    if (_fromStoreId.equals(model.getStoreId()) &&
                            _fromFloorId.equals(model.getFloorId()) &&
                            model.getRoomId() != null && model.getRoomId().equals(roomIdList.get(position))) {

                        if (model.getRackName() != null && model.getRackId() != null) {
                            _rackNameListTemp.add(model.getRackName());
                            _rackIdListTemp.add(model.getRackId());
                        }
                    }
                }

// Use LinkedHashSet to maintain order and uniqueness
                Set<String> uniqueRackNameSet = new LinkedHashSet<>(_rackNameListTemp);
                Set<String> uniqueRackIdSet = new LinkedHashSet<>(_rackIdListTemp);

// Add unique elements back to the lists
                rackNameList.clear();
                rackIdList.clear();
                rackNameList.addAll(uniqueRackNameSet);
                rackIdList.addAll(uniqueRackIdSet);

// Set up the spinners
                setUpRackSpinner();
                setUpShelfSpinner();
                setUpBinSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpRackSpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.custome_spinner_dropdown, rackNameList);
        _fromRackSpinner.setAdapter(spinnerArrayAdapter);
        ArrayList<String> _shelfNameList = new ArrayList<>();
        ArrayList<String> _shelfIdList = new ArrayList<>();
        _fromRackSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Clear existing lists
                shelfNameList.clear();
                shelfIdList.clear();
                binNameList.clear();
                binIdList.clear();

// Set the selected rack ID
                _fromRackId = rackIdList.get(position);

// Temporary lists to hold shelf names and IDs
                List<String> _shelfNameListTemp = new ArrayList<>();
                List<String> _shelfIdListTemp = new ArrayList<>();

// Populate temporary lists with shelf names and IDs for the selected store, floor, room, and rack
                for (V1_RFIDTransferDropdownModel.Datum model : rfidTransferDropdownModel) {
                    if (_fromStoreId.equals(model.getStoreId()) &&
                            _fromFloorId.equals(model.getFloorId()) &&
                            _fromRoomId.equals(model.getRoomId()) &&
                            model.getRackId() != null && model.getRackId().equals(rackIdList.get(position))) {

                        if (model.getShelfName() != null && model.getShelfId() != null) {
                            _shelfNameListTemp.add(model.getShelfName());
                            _shelfIdListTemp.add(model.getShelfId());
                        }
                    }
                }

// Use LinkedHashSet to maintain order and uniqueness
                Set<String> uniqueShelfNameSet = new LinkedHashSet<>(_shelfNameListTemp);
                Set<String> uniqueShelfIdSet = new LinkedHashSet<>(_shelfIdListTemp);

// Add unique elements back to the lists
                shelfNameList.clear();
                shelfIdList.clear();
                shelfNameList.addAll(uniqueShelfNameSet);
                shelfIdList.addAll(uniqueShelfIdSet);

// Set up the spinners
                setUpShelfSpinner();
                setUpBinSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpShelfSpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.custome_spinner_dropdown, shelfNameList);
        _fromShelfSpinner.setAdapter(spinnerArrayAdapter);
        ArrayList<String> _binNameList = new ArrayList<>();
        ArrayList<String> _binIdList = new ArrayList<>();
        _fromShelfSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Clear existing lists
                binNameList.clear();
                binIdList.clear();
                _binNameList.clear();
                _binIdList.clear();

// Set the selected shelf ID
                _fromShelfId = shelfIdList.get(position);

// Temporary lists to hold bin names and IDs
                List<String> _binNameListTemp = new ArrayList<>();
                List<String> _binIdListTemp = new ArrayList<>();

// Populate temporary lists with bin names and IDs for the selected store, floor, room, rack, and shelf
                for (V1_RFIDTransferDropdownModel.Datum model : rfidTransferDropdownModel) {
                    if (_fromStoreId.equals(model.getStoreId()) &&
                            _fromFloorId.equals(model.getFloorId()) &&
                            _fromRoomId.equals(model.getRoomId()) &&
                            _fromRackId.equals(model.getRackId()) &&
                            model.getShelfId() != null && model.getShelfId().equals(shelfIdList.get(position))) {

                        if (model.getBinName() != null && model.getBinId() != null) {
                            _binNameListTemp.add(model.getBinName());
                            _binIdListTemp.add(model.getBinId());
                        }
                    }
                }

// Use LinkedHashSet to maintain order and uniqueness
                Set<String> uniqueBinNameSet = new LinkedHashSet<>(_binNameListTemp);
                Set<String> uniqueBinIdSet = new LinkedHashSet<>(_binIdListTemp);

// Add unique elements back to the lists
                binNameList.clear();
                binIdList.clear();
                binNameList.addAll(uniqueBinNameSet);
                binIdList.addAll(uniqueBinIdSet);

// Set up the spinner
                setUpBinSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpBinSpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.custome_spinner_dropdown, binNameList);
        _fromBinBoxSpinner.setAdapter(spinnerArrayAdapter);

        _fromBinBoxSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _fromBinBoxId = binIdList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpToStoreSpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.custome_spinner_dropdown, toStoreNameList);
        _toStoreSpinner.setAdapter(spinnerArrayAdapter);

        ArrayList<String> _floorNameList = new ArrayList<>();
        ArrayList<String> _floorIdList = new ArrayList<>();
        _toStoreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Clear existing lists
                toFloorNameList.clear();
                toFloorIdList.clear();
                toRoomNameList.clear();
                toRoomIdList.clear();
                toRackNameList.clear();
                toRackIdList.clear();
                toShelfNameList.clear();
                toShelfIdList.clear();
                toBinNameList.clear();
                toBinIdList.clear();
                _floorNameList.clear();
                _floorIdList.clear();

// Set the selected store ID
                _toStoreId = toStoreIdList.get(position);

// Temporary lists to hold floor names and IDs
                List<String> _floorNameListTemp = new ArrayList<>();
                List<String> _floorIdListTemp = new ArrayList<>();

// Populate temporary lists with floor names and IDs for the selected store
                for (V1_RFIDTransferDropdownModel.Datum model : toRFIDTransferDropdownModel) {
                    if (model.getStoreId().equals(toStoreIdList.get(position))) {
                        if (model.getFloorName() != null && model.getFloorId() != null) {
                            _floorNameListTemp.add(model.getFloorName());
                            _floorIdListTemp.add(model.getFloorId());
                        }
                    }
                }

// Use LinkedHashSet to maintain order and uniqueness
                Set<String> uniqueFloorNameSet = new LinkedHashSet<>(_floorNameListTemp);
                Set<String> uniqueFloorIdSet = new LinkedHashSet<>(_floorIdListTemp);

// Add unique elements back to the lists
                toFloorNameList.addAll(uniqueFloorNameSet);
                toFloorIdList.addAll(uniqueFloorIdSet);

// Set up the spinners
                setUpToFloorSpinner();
                setUpToRoomSpinner();
                setUpToRackSpinner();
                setUpToShelfSpinner();
                setUpToBinSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpToFloorSpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.custome_spinner_dropdown, toFloorNameList);
        _toFloorSpinner.setAdapter(spinnerArrayAdapter);
        ArrayList<String> _roomNameList = new ArrayList<>();
        ArrayList<String> _roomIdList = new ArrayList<>();
        _toFloorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Clear existing lists
                toRoomNameList.clear();
                toRoomIdList.clear();
                toRackNameList.clear();
                toRackIdList.clear();
                toShelfNameList.clear();
                toShelfIdList.clear();
                toBinNameList.clear();
                toBinIdList.clear();
                _roomNameList.clear();
                _roomIdList.clear();

// Set the selected floor ID
                _toFloorId = toFloorIdList.get(position);

// Temporary lists to hold room names and IDs
                List<String> _roomNameListTemp = new ArrayList<>();
                List<String> _roomIdListTemp = new ArrayList<>();

// Populate temporary lists with room names and IDs for the selected floor
                for (V1_RFIDTransferDropdownModel.Datum model : toRFIDTransferDropdownModel) {
                    if (model.getStoreId().equals(_toStoreId) && model.getFloorId().equals(toFloorIdList.get(position))) {
                        if (model.getRoomName() != null && model.getRoomId() != null) {
                            _roomNameListTemp.add(model.getRoomName());
                            _roomIdListTemp.add(model.getRoomId());
                        }
                    }
                }

// Use LinkedHashSet to maintain order and uniqueness
                Set<String> uniqueRoomNameSet = new LinkedHashSet<>(_roomNameListTemp);
                Set<String> uniqueRoomIdSet = new LinkedHashSet<>(_roomIdListTemp);

// Add unique elements back to the lists
                toRoomNameList.addAll(uniqueRoomNameSet);
                toRoomIdList.addAll(uniqueRoomIdSet);

// Set up the spinners
                setUpToRoomSpinner();
                setUpToRackSpinner();
                setUpToShelfSpinner();
                setUpToBinSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpToRoomSpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.custome_spinner_dropdown, toRoomNameList);
        _toRoomSpinner.setAdapter(spinnerArrayAdapter);
        ArrayList<String> _rackNameList = new ArrayList<>();
        ArrayList<String> _rackIdList = new ArrayList<>();
        _toRoomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Clear existing lists
                toRackNameList.clear();
                toRackIdList.clear();
                toShelfNameList.clear();
                toShelfIdList.clear();
                toBinNameList.clear();
                toBinIdList.clear();
                _rackNameList.clear();
                _rackIdList.clear();

// Set the selected room ID
                _toRoomId = toRoomIdList.get(position);

// Temporary lists to hold rack names and IDs
                List<String> _rackNameListTemp = new ArrayList<>();
                List<String> _rackIdListTemp = new ArrayList<>();

// Populate temporary lists with rack names and IDs for the selected room
                for (V1_RFIDTransferDropdownModel.Datum model : toRFIDTransferDropdownModel) {
                    if (_toStoreId.equals(model.getStoreId()) &&
                            _toFloorId.equals(model.getFloorId()) &&
                            model.getRoomId() != null && model.getRoomId().equals(toRoomIdList.get(position))) {

                        if (model.getRackName() != null && model.getRackId() != null) {
                            _rackNameListTemp.add(model.getRackName());
                            _rackIdListTemp.add(model.getRackId());
                        }
                    }
                }

// Use LinkedHashSet to maintain order and uniqueness
                Set<String> uniqueRackNameSet = new LinkedHashSet<>(_rackNameListTemp);
                Set<String> uniqueRackIdSet = new LinkedHashSet<>(_rackIdListTemp);

// Add unique elements back to the lists
                toRackNameList.addAll(uniqueRackNameSet);
                toRackIdList.addAll(uniqueRackIdSet);

// Set up the spinners
                setUpToRackSpinner();
                setUpToShelfSpinner();
                setUpToBinSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpToRackSpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.custome_spinner_dropdown, toRackNameList);
        _toRackSpinner.setAdapter(spinnerArrayAdapter);
        ArrayList<String> _shelfNameList = new ArrayList<>();
        ArrayList<String> _shelfIdList = new ArrayList<>();
        _toRackSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Clear existing lists
                toShelfNameList.clear();
                toShelfIdList.clear();
                toBinNameList.clear();
                toBinIdList.clear();
                _shelfNameList.clear();
                _shelfIdList.clear();

// Set the selected rack ID
                _toRackId = toRackIdList.get(position);

// Temporary lists to hold shelf names and IDs
                List<String> _shelfNameListTemp = new ArrayList<>();
                List<String> _shelfIdListTemp = new ArrayList<>();

// Populate temporary lists with shelf names and IDs for the selected rack
                for (V1_RFIDTransferDropdownModel.Datum model : toRFIDTransferDropdownModel) {
                    if (_toStoreId.equals(model.getStoreId()) &&
                            _toFloorId.equals(model.getFloorId()) &&
                            _toRoomId.equals(model.getRoomId()) &&
                            model.getRackId() != null && model.getRackId().equals(toRackIdList.get(position))) {

                        if (model.getShelfName() != null && model.getShelfId() != null) {
                            _shelfNameListTemp.add(model.getShelfName());
                            _shelfIdListTemp.add(model.getShelfId());
                        }
                    }
                }

// Use LinkedHashSet to maintain order and uniqueness
                Set<String> uniqueShelfNameSet = new LinkedHashSet<>(_shelfNameListTemp);
                Set<String> uniqueShelfIdSet = new LinkedHashSet<>(_shelfIdListTemp);

// Add unique elements back to the lists
                toShelfNameList.addAll(uniqueShelfNameSet);
                toShelfIdList.addAll(uniqueShelfIdSet);

// Set up the spinners
                setUpToShelfSpinner();
                setUpToBinSpinner();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpToShelfSpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.custome_spinner_dropdown, toShelfNameList);
        _toShelfSpinner.setAdapter(spinnerArrayAdapter);
        ArrayList<String> _binNameList = new ArrayList<>();
        ArrayList<String> _binIdList = new ArrayList<>();
        _toShelfSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Clear existing lists
                toBinNameList.clear();
                toBinIdList.clear();
                _binNameList.clear();
                _binIdList.clear();

// Set the selected shelf ID
                _toShelfId = shelfIdList.get(position);

// Temporary lists to hold bin names and IDs
                List<String> _binNameListTemp = new ArrayList<>();
                List<String> _binIdListTemp = new ArrayList<>();

// Populate temporary lists with bin names and IDs for the selected shelf
                for (V1_RFIDTransferDropdownModel.Datum model : toRFIDTransferDropdownModel) {
                    if (_toStoreId.equals(model.getStoreId()) &&
                            _toFloorId.equals(model.getFloorId()) &&
                            _toRoomId.equals(model.getRoomId()) &&
                            _toRackId.equals(model.getRackId()) &&
                            model.getShelfId() != null && model.getShelfId().equals(_toShelfId)) {

                        if (model.getBinName() != null && model.getBinId() != null) {
                            _binNameListTemp.add(model.getBinName());
                            _binIdListTemp.add(model.getBinId());
                        }
                    }
                }

// Use LinkedHashSet to maintain order and uniqueness
                Set<String> uniqueBinNameSet = new LinkedHashSet<>(_binNameListTemp);
                Set<String> uniqueBinIdSet = new LinkedHashSet<>(_binIdListTemp);

// Add unique elements back to the lists
                toBinNameList.addAll(uniqueBinNameSet);
                toBinIdList.addAll(uniqueBinIdSet);

// Set up the spinner
                setUpToBinSpinner();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpToBinSpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.custome_spinner_dropdown, toBinNameList);
        _toBinBoxSpinner.setAdapter(spinnerArrayAdapter);

        _toBinBoxSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _toBinBoxId = toBinIdList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void sendRequestForRFIDTransferStore() {
        rfidTransferViewModal = new ViewModelProvider(this).get(RFIDTransferViewModal.class);
        rfidTransferViewModal.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                showLoading();
            } else {
                hideLoading();
            }
        });

        Log.d(TAG, "sendRequestForRFIDTransferStore: ");

        rfidTransferViewModal.getRFIDTransferStoreResponse(_fromStoreId, _fromFloorId, _fromRoomId, _fromRackId, _fromShelfId, "0").observe(this, apiResponse -> {
            if(apiResponse != null && apiResponse.getData() != null){
                rfidTransferModel.clear();

                rfidTransferModel.addAll(apiResponse.getData());
                if(rfidTransferModel.size() == 0 || rfidTransferModel == null){
//                    noDataFoundLy.setVisibility(View.VISIBLE);
                    DialogHelper.showWarningDialog(V1_YarnTransferEntryActivity.this, "Message", "No data found");
                }else{
//                    noDataFoundLy.setVisibility(View.GONE);
                }

                rfidTransferStoreRecyclerAdapter.notifyDataSetChanged();

            } else {
                DialogHelper.showErrorDialog(V1_YarnTransferEntryActivity.this, "Message", "Something went wrong!");
//                noDataFoundLy.setVisibility(View.VISIBLE);
            }
        });
    }

    private void init_ui() {
        recyclerView = findViewById(R.id.rfidTransferStoreRecyclerView);
        _companySpinner = findViewById(R.id.companySpinner);
        _fromStoreSpinner = findViewById(R.id.fromStoreSpinner);
        _fromFloorSpinner = findViewById(R.id.fromFloorSpinner);
        _fromRoomSpinner = findViewById(R.id.fromRoomSpinner);
        _fromRackSpinner = findViewById(R.id.fromRackSpinner);
        _fromShelfSpinner = findViewById(R.id.fromShelfSpinner);
        _fromBinBoxSpinner = findViewById(R.id.fromBinBoxSpinner);

        _toStoreSpinner = findViewById(R.id.toStoreSpinner);
        _toFloorSpinner = findViewById(R.id.toFloorSpinner);
        _toRoomSpinner = findViewById(R.id.toRoomSpinner);
        _toRackSpinner = findViewById(R.id.toRackSpinner);
        _toShelfSpinner = findViewById(R.id.toShelfSpinner);
        _toBinBoxSpinner = findViewById(R.id.toBinBoxSpinner);

        _selectAllBarcodeCheckbox = findViewById(R.id.selectAllBarcodeCheckbox);
        _selectAllBarcodeCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    rfidTransferModel.forEach(datum -> {
                        datum.getRfids().forEach(rfid -> rfid.setSelected(isChecked));
                    });
                } else {
                    for (V1_RFIDTransferModel.Datum datum : rfidTransferModel) {
                        for (V1_RFIDTransferModel.Rfid rfid : datum.getRfids()) {
                            rfid.setSelected(isChecked);
                        }
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, "setUp: ", e);
            }

            rfidTransferStoreRecyclerAdapter.notifyDataSetChanged();
        });

        findViewById(R.id.button).setOnClickListener(v -> sendRequestForRFIDTransferStore());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fabric, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            if(rfidTransferModel != null && rfidTransferModel.size() > 0){
                extractFormData();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, new Gson().toJson(rfidTransferSaveModel));


                if(rfidTransferSaveModel.getRfid() != null && rfidTransferSaveModel.getRfid().size() > 0){
                    if(!_toStoreId.equals("0")){
                        showLoading();
                        rfidTransferViewModal.postRFIDTransferStoreResponse(body).observe(this, apiResponse -> {
                            if(apiResponse != null && apiResponse.getStatus().equals("200")){
                                new SweetAlertDialog(V1_YarnTransferEntryActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Message")
                                        .setContentText(apiResponse.getMsg())
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(sDialog -> {
                                            sDialog.dismissWithAnimation();
                                            startActivity(new Intent(this, V1_YarnTransferEntryActivity.class));
                                            finish();
                                        })
                                        .show();
                            } else {
                                DialogHelper.showErrorDialog(V1_YarnTransferEntryActivity.this, "Message", "Something went wrong!");
                            }
                            hideLoading();
                        });
                    }else{
                        DialogHelper.showWarningDialog(V1_YarnTransferEntryActivity.this, "Message", "Warning: To Store Information is not selected. Please select.");
                    }

                }else{
                    DialogHelper.showWarningDialog(V1_YarnTransferEntryActivity.this, "Message", "Warning: RFID list is empty. Please select RFID");
                }

            }else{
                DialogHelper.showWarningDialog(V1_YarnTransferEntryActivity.this, "Message", "Warning: RFID list is empty.");
            }

            return true;
        }
        else if (id == R.id.action_new){
            rfidTransferModel.clear();
            rfidTransferStoreRecyclerAdapter.notifyDataSetChanged();
            startActivity(new Intent(this, V1_YarnTransferEntryActivity.class));
            finish();
        } else if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void extractFormData() {
        rfidTransferSaveModel.setStatus(true);
        rfidTransferSaveModel.setUserId(userID);
        rfidTransferSaveModel.setCboCompanyId(rfidTransferModel.get(0).getRfids().get(0).getCompanyId());
        rfidTransferSaveModel.setCboStoreName(rfidTransferModel.get(0).getRfids().get(0).getStoreId());
        rfidTransferSaveModel.setCboFloor(rfidTransferModel.get(0).getRfids().get(0).getFloorId());
        rfidTransferSaveModel.setCboRoom(rfidTransferModel.get(0).getRfids().get(0).getRoomId());
        rfidTransferSaveModel.setTxtRack(rfidTransferModel.get(0).getRfids().get(0).getRackId());
        rfidTransferSaveModel.setTxtShelf(rfidTransferModel.get(0).getRfids().get(0).getShelfId());
        rfidTransferSaveModel.setCboBin(rfidTransferModel.get(0).getRfids().get(0).getBinId());
        rfidTransferSaveModel.setCboStoreNameTo(_toStoreId);
        rfidTransferSaveModel.setCboFloorTo(_toFloorId);
        rfidTransferSaveModel.setCboRoomTo(_toRoomId);
        rfidTransferSaveModel.setTxtRackTo(_toRackId);
        rfidTransferSaveModel.setTxtShelfTo(_toShelfId);
        rfidTransferSaveModel.setCboBinTo(_toBinBoxId);

        ArrayList<V1_RFIDTransferSaveModel.Rfid> rfids = new ArrayList<>();
        for (V1_RFIDTransferModel.Datum datum : rfidTransferModel) {
            for (V1_RFIDTransferModel.Rfid rfid : datum.getRfids()) {
                if (rfid.isSelected()) {
                    V1_RFIDTransferSaveModel.Rfid saveRfid = new V1_RFIDTransferSaveModel.Rfid();
                    saveRfid.setEpcid(rfid.getRfidNo());
                    saveRfid.setBagWeight(rfid.getBagWeight());
                    saveRfid.setCboUom(rfid.getCboUom());
                    saveRfid.setTxtRate(rfid.getTxtRate());
                    saveRfid.setTxtYarnBrand(rfid.getTxtYarnBrand());
                    saveRfid.setTxtItemDesc(datum.getTxtItemDesc()); // get from datum
                    saveRfid.setTxtChallanNo("");
                    saveRfid.setCboPurpose("");
                    saveRfid.setTxtYarnLot(datum.getTxtYarnLot()); // get from datum
                    saveRfid.setProdId(datum.getProdId()); // get from datum
                    saveRfid.setRfidId(rfid.getRfidId());
                    rfids.add(saveRfid);
                }
            }
        }
        rfidTransferSaveModel.setRfid(rfids);
        Log.e("data", "data" + new Gson().toJson(rfidTransferSaveModel));
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(5);
        recyclerView.addItemDecoration(itemDecorator);
        rfidTransferStoreRecyclerAdapter = new V1_RFIDTransferStoreHeaderRecyclerAdapter(rfidTransferModel, this);
        recyclerView.setAdapter(rfidTransferStoreRecyclerAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(false);
        layoutManager.setStackFromEnd(false);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void showLoading() {
        hideLoading();
        mProgressDialog = CommonUtils.showLoadingDialog(this);
    }


    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }
}