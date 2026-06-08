package com.logicsoftbd.lsl.ui.v_1_ui.config;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.ProConfigurationResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseCompanyToLocationClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseFloorWiseLineClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseLocationWiseFloorClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseSewingInputClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ConfigSewingOperationModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ConfigStyleWiseDataItemModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DataSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_OperationSorterListModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ProVariableResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_StyleWiseConfigResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_StyleWiseOperationResponse;
import com.logicsoftbd.lsl.data.prefs.SessionManager;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.v_1_ui.menu.V1_MenuActivity;
import com.logicsoftbd.lsl.utils.ApiUtils;
import com.logicsoftbd.lsl.utils.VerticalSpacingItemDecorator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class V1_StyleWiseLineConfigActivity_v3 extends AppCompatActivity implements View.OnClickListener, V1_TabConfigRecyclerViewAdapter_v3.OnHeadListener,
        V1_StyleWiseSewingOperationConfigRecyclerAdapter.OnOperationSelectListener{
    private static final String TAG = "V1_StyleWiseLineConfigA";
    private SessionManager session;
    private Spinner cCompany, cLocation, cFloor, cLine, cSelfOrderSpinner;
    private Button cDate, cMacAddress, cConfig;
    private String currentDate;
    private LinearLayout dataSetLayout, errorDataSetLayout, poNoLayout;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private String macAddress;
    private Boolean allSelectPosition = false;
    private V1_TabConfigRecyclerViewAdapter_v3 tabConfigRecyclerViewAdapter;
    private V1_StyleWiseSewingOperationConfigRecyclerAdapter styleWiseSewingOperationConfigRecyclerAdapter;
    private RecyclerView _recyclerView, _operationRecyclerView;
    private SearchView styleSearchET;
    private CheckBox _operationSelectCheckbox;

    public final ArrayList<String> companyNameList = new ArrayList<>();
    public final ArrayList<Integer> companyNameId = new ArrayList<>();
    public final ArrayList<String> locationNameList = new ArrayList<>();
    public final ArrayList<Integer> locationNameId = new ArrayList<>();
    public final ArrayList<String> floorNameList = new ArrayList<>();
    public final ArrayList<Integer> floorNameId = new ArrayList<>();
    public final ArrayList<String> lineNameList = new ArrayList<>();
    public final ArrayList<Integer> lineNameId = new ArrayList<>();
    public ArrayList<V1_ConfigSewingOperationModel> configSewingOperationModels = new ArrayList<>();
    public final ArrayList<V1_ConfigStyleWiseDataItemModel> styleWiseDataItemModels = new ArrayList<>();
    public ArrayList<V1_ConfigStyleWiseDataItemModel> searchedStyleWiseDataItemModels = new ArrayList<>();

    private List<V1_ConfigSewingOperationModel> operationItemModelList = new ArrayList<>();


    public String[] companyNameArray, locationNameArray, floorNameArray, lineNameArray, selfOrderArray;

    private int companyId = 0, locationId = 0, floorId = 0, lineId = 0, saVedLineId = 0, selectedStylePosition = 0;
    private String base_url = "", userId = "", companyName = "", locationName = "", floorName = "", lineName = "", jobNo="", styleRefNo="",
            jobId="", po_break_downId="", item_numberId="",  itemName="", countryName = "",
            selectedOperationJson = "", poId="", irNumber = "", buyerName = "",  operation_ids = "", savedJobNo = "",
            savedPONo = "", savedItem = "", savedStyle = "", operationJson = "", style_number = "", companyWiseVariableSetup = "";

    private AlertDialog.Builder alertDialogBuilder;
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;
    private Toolbar mToolbar;
    private ProgressDialog pDialog;
    private int company = 0, location = 0, line = 0, floor = 0;
    private boolean companyStatus = false, locationStatus = false, lineStatus = false, floorStatus = false, subContract= false;
    private int pro_company_id, pro_position, pro_v4_status = 0;
    private List<V1_ProVariableResponse> proVariables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_style_wise_line_config_v3);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        alertDialogBuilder = new AlertDialog.Builder(V1_StyleWiseLineConfigActivity_v3.this);

        session = new SessionManager(getApplicationContext());

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = _preferences.getString("login_userid", "");
        company = (_preferences.getInt("company", 0));
        location = (_preferences.getInt("location", 0));
        line = (_preferences.getInt("line", 0));
        floor = (_preferences.getInt("floor", 0));
        savedJobNo = (_preferences.getString("jobNo", ""));
        savedPONo = (_preferences.getString("poNumber", ""));
        savedItem = (_preferences.getString("itemName", ""));
        savedStyle = (_preferences.getString("style", ""));
        operationJson = _preferences.getString("operationJson", "");

        jobNo = (_preferences.getString("jobNo", ""));
        po_break_downId = (_preferences.getString("poBreakDownId", ""));
        item_numberId = (_preferences.getString("itemNumber", ""));
        base_url = (_preferences.getString("base_url", ""));
        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);

        String proVariableArrayList = _preferences.getString("proVariableArrayList","");
        Log.d(TAG, "onCreate ####: "+proVariableArrayList);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            proVariables = objectMapper.readValue(proVariableArrayList, new TypeReference<List<V1_ProVariableResponse>>(){});
            Log.d(TAG, "onCreate ####" + proVariables.get(0).getCompanyId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        macAddress = getMacAddr();
        initialization();
        getDefaultData();

        if(!operationJson.equals("")){
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            operationItemModelList = Arrays.asList(gson.fromJson(operationJson, V1_ConfigSewingOperationModel[].class));
        }

        styleSearchET = findViewById(R.id.styleSearchET);
        styleSearchET.clearFocus();

        styleSearchET.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                tabConfigRecyclerViewAdapter.getFilter().filter(newText);
                return false;
            }
        });

        Log.d(TAG, "onCreate: " + ":"+ company +  ":"+location + ":"+ line +  ":"+floor + ":"+savedJobNo+":"+savedStyle);
    }

    private void getDefaultData() {
        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        companyId = (_preferences.getInt("company", 0));
        locationId = (_preferences.getInt("location", 0));
        lineId = (_preferences.getInt("line", 0));
        saVedLineId = (_preferences.getInt("line", 0));
        floorId = (_preferences.getInt("floor", 0));
        companyName = (_preferences.getString("companyName", ""));
        locationName = (_preferences.getString("locationName", ""));
        lineName = (_preferences.getString("lineName", ""));
        floorName = (_preferences.getString("floorName", ""));

        companyWiseProVariableSetup();



        try{
            for(int i=0; i<proVariables.size(); i++){
                if(proVariables.get(i).getCompanyId() == companyId){
                    pro_position = i;
                    break;
                }
            }

            if(proVariables.get(pro_position).getSpot().getDefect() == 2){
                pro_v4_status = 1;
            }else if(proVariables.get(pro_position).getAlter().getDefect() == 2){
                pro_v4_status = 1;
            }
        }catch (Exception e){

        }

    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    // res1.append(Integer.toHexString(b & 0xFF) + ":");
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }

    private void initialization() {
        _recyclerView = findViewById(R.id.styleConfigRecyclerView);
        _operationRecyclerView = findViewById(R.id.operationRecyclerView);
        _operationSelectCheckbox = findViewById(R.id.operationSelectCheckbox);

        _operationSelectCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                _operationSelectCheckbox.setText("UNSELECT ALL");
                for(int i=0; i<configSewingOperationModels.size(); i++){
                    configSewingOperationModels.get(i).setStatus(true);
                }
                styleWiseSewingOperationConfigRecyclerAdapter.notifyDataSetChanged();
            }else{
                if(!allSelectPosition){
                    _operationSelectCheckbox.setText("SELECT ALL");
                    for(int i=0; i<configSewingOperationModels.size(); i++){
                        configSewingOperationModels.get(i).setStatus(false);
                    }
                }
                styleWiseSewingOperationConfigRecyclerAdapter.notifyDataSetChanged();
            }
        });

        cCompany = findViewById(R.id.companySpinner);
        cLocation = findViewById(R.id.locationSpinnner);
        cFloor = findViewById(R.id.floorSpinner);
        cLine = findViewById(R.id.lineSpinner);
        cSelfOrderSpinner = findViewById(R.id.selfOrderSpinner);

        cDate = findViewById(R.id.configDateBtn);
        cMacAddress = findViewById(R.id.macAddressBtn);
        cConfig = findViewById(R.id.configBtn);

        errorDataSetLayout = findViewById(R.id.errorDataSetLayout);
        dataSetLayout = findViewById(R.id.dataSetLayout);
        poNoLayout = findViewById(R.id.poNoLayout);

        cDate.setOnClickListener(this);
        cMacAddress.setOnClickListener(this);
        cConfig.setOnClickListener(this);

        sendRequestToServer();

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = simpleDateFormat.format(calendar.getTime());
        cDate.setText(currentDate);

        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        macAddress = info.getMacAddress();
        cMacAddress.setText(macAddress);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _operationRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(5);
        _operationRecyclerView.addItemDecoration(itemDecorator);
    }

    private void showStyleWiseItem(int position) {

        if(position == 0) {
            showDialog();
            subContract = false;
            apiInterface.getStyleWiseTabCongigClassCall( String.valueOf(companyId), String.valueOf(locationId), String.valueOf(floorId), String.valueOf(lineId)).enqueue(new Callback<V1_StyleWiseConfigResponse>() {
                @Override
                public void onResponse(Call<V1_StyleWiseConfigResponse> call, Response<V1_StyleWiseConfigResponse> response) {
                    handleStyleWiseTabConfigResponse(response);
                }

                @Override
                public void onFailure(Call<V1_StyleWiseConfigResponse> call, Throwable t) {
                    hideDialog();
                    Toast.makeText(V1_StyleWiseLineConfigActivity_v3.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        } else if(position == 1) {
            showDialog();
            subContract = true;
            apiInterface.getStyleWiseTabConfigSubContractClassCall( String.valueOf(companyId), String.valueOf(locationId), String.valueOf(floorId), String.valueOf(lineId)).enqueue(new Callback<V1_StyleWiseConfigResponse>() {
                @Override
                public void onResponse(Call<V1_StyleWiseConfigResponse> call, Response<V1_StyleWiseConfigResponse> response) {
                    handleStyleWiseTabConfigResponse(response);
                }

                @Override
                public void onFailure(Call<V1_StyleWiseConfigResponse> call, Throwable t) {
                    hideDialog();
                    Toast.makeText(V1_StyleWiseLineConfigActivity_v3.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void handleStyleWiseTabConfigResponse(Response<V1_StyleWiseConfigResponse> response) {
        hideDialog();
        Log.d(TAG, "onResponse: ------->"+response.toString());
        if(response.isSuccessful()){
            styleWiseDataItemModels.clear();
            if(response.body().getData() != null && response.body().getData().size() > 0){

                for(int i=0; i<response.body().getData().size(); i++){
                    V1_ConfigStyleWiseDataItemModel styleWiseDataItemModel = new V1_ConfigStyleWiseDataItemModel();
                    styleWiseDataItemModel.setJobId(response.body().getData().get(i).getJobId());
                    styleWiseDataItemModel.setJobNo(response.body().getData().get(i).getJobNo());
                    styleWiseDataItemModel.setStyleRefNo(response.body().getData().get(i).getStyleRefNo());
                    styleWiseDataItemModel.setPoId(response.body().getData().get(i).getPoId());
                    styleWiseDataItemModel.setPoNumber(response.body().getData().get(i).getPoNumber());
                    styleWiseDataItemModel.setItemNumberId(response.body().getData().get(i).getItemNumberId());
                    styleWiseDataItemModel.setItemName(response.body().getData().get(i).getItemName());
                    styleWiseDataItemModel.setCountryId(response.body().getData().get(i).getCountryId());
                    styleWiseDataItemModel.setCountryName(response.body().getData().get(i).getCountryName());
                    styleWiseDataItemModel.setIrNumber(response.body().getData().get(i).getIrNumber());
                    styleWiseDataItemModel.setBuyerName(response.body().getData().get(i).getBuyerName());
                    styleWiseDataItemModel.setVariableStatus(companyWiseVariableSetup);
                    styleWiseDataItemModel.setAutoDistribution(response.body().getData().get(i).getAutoDistribution());
                    styleWiseDataItemModels.add(styleWiseDataItemModel);
                }
            }
            setAdapterData();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.configDateBtn:
            case R.id.macAddressBtn:
                break;
            case R.id.configBtn:
                ArrayList<V1_ConfigSewingOperationModel> _configSewingOperationModels = new ArrayList<>();
                for(V1_ConfigSewingOperationModel item : configSewingOperationModels){
                    if(item.getStatus()){
                        V1_ConfigSewingOperationModel configSewingOperationModel = new V1_ConfigSewingOperationModel();
                        configSewingOperationModel.setOperationName(item.getOperationName());
                        configSewingOperationModel.setOperationId(item.getOperationId());
                        configSewingOperationModel.setStatus(item.getStatus());
                        _configSewingOperationModels.add(configSewingOperationModel);
                    }
                }
                selectedOperationJson = new Gson().toJson(_configSewingOperationModels);
//                Log.d(TAG, "onClick: ######"+selectedOperationJson);

                for(int i = 0; i < configSewingOperationModels.size(); i++)
                {
                    if(configSewingOperationModels.get(i).getStatus() == true){
                        operation_ids += configSewingOperationModels.get(i).getOperationId() + ",";
                    }
                }

                SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(V1_StyleWiseLineConfigActivity_v3.this);
                SharedPreferences.Editor _editor = _preferences.edit();
                _editor.putInt("company", companyId);
                _editor.putString("companyName", companyName);
                _editor.putInt("location", locationId);
                _editor.putString("locationName", locationName);
                _editor.putInt("floor", floorId);
                _editor.putString("floorName", floorName);
                _editor.putInt("line", lineId);
                _editor.putString("lineName", lineName);
                _editor.putString("jobNo", jobNo);
                _editor.putString("jobId", jobId);
                _editor.putString("style", styleRefNo);
                _editor.putString("itemNumber", item_numberId);
                _editor.putString("poNumber", poId);
                _editor.putString("poBreakDownId", po_break_downId);
                _editor.putString("countryName", countryName);
                _editor.putString("itemName", itemName);
                _editor.putString("irNumber", irNumber);
                _editor.putString("buyerName", buyerName);
                _editor.putString("mac", String.valueOf(macAddress));
                _editor.putString("operationJson", selectedOperationJson);
                _editor.putBoolean("subContract", subContract);
                Log.d(TAG, "onClick: "+buyerName);
                if(pro_v4_status == 1){
                    if(companyId != 0 && locationId != 0 && floorId != 0 && lineId != 0)
                    {
                        _editor.apply();
                        try {
                            sendData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        alertForApkUpdate("Please fill the credentials & Items & select operation.");
                    }
                }else{
                    if(companyId != 0 && locationId != 0 && floorId != 0 && lineId != 0 && !operation_ids.equals(""))
                    {
                        _editor.apply();
                        try {
                            sendData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        alertForApkUpdate("Please fill the credentials & Items & select operation.");
                    }
                }
                break;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setAdapterData() {
        searchedStyleWiseDataItemModels = styleWiseDataItemModels;
        for(int i=0; i<searchedStyleWiseDataItemModels.size(); i++){
            if(companyWiseVariableSetup.equals("1") && searchedStyleWiseDataItemModels.get(i).getJobNo().equals(savedJobNo) &&
                    searchedStyleWiseDataItemModels.get(i).getStyleRefNo().equals(savedStyle) &&
                    searchedStyleWiseDataItemModels.get(i).getItemNumberId().equals(item_numberId)) {
                searchedStyleWiseDataItemModels.get(i).setStatus(true);
                selectedStylePosition = i;
            }else if(companyWiseVariableSetup.equals("2") && searchedStyleWiseDataItemModels.get(i).getJobNo().equals(savedJobNo) &&
                    searchedStyleWiseDataItemModels.get(i).getPoId().equals(savedPONo) &&
                    searchedStyleWiseDataItemModels.get(i).getStyleRefNo().equals(savedStyle) &&
                    searchedStyleWiseDataItemModels.get(i).getItemNumberId().equals(item_numberId)) {
                searchedStyleWiseDataItemModels.get(i).setStatus(true);
                selectedStylePosition = i;
            }else{
                searchedStyleWiseDataItemModels.get(i).setStatus(false);
            }
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        _recyclerView.setLayoutManager(linearLayoutManager);
        tabConfigRecyclerViewAdapter = new V1_TabConfigRecyclerViewAdapter_v3(getApplicationContext(), searchedStyleWiseDataItemModels, this);
        _recyclerView.setAdapter(tabConfigRecyclerViewAdapter);
        tabConfigRecyclerViewAdapter.notifyDataSetChanged();
        styleWiseSelection(selectedStylePosition);
    }

    private void requestForSewingOperation(String autoDistribution) {
        showDialog();
        try {
            Call<V1_StyleWiseOperationResponse> styleWiseOperationApiCall = autoDistribution.equals("1") ?
                    apiInterface.getStyleWiseWithoutPOOperationV2Call(jobNo, item_numberId, style_number, String.valueOf(lineId), userId) :
                    apiInterface.getStyleWiseWithoutPOOperationCall(jobNo, item_numberId, style_number, String.valueOf(lineId), userId);
            styleWiseOperationApiCall.enqueue(new Callback<V1_StyleWiseOperationResponse>() {
                @Override
                public void onResponse(Call<V1_StyleWiseOperationResponse> call, Response<V1_StyleWiseOperationResponse> response) {
                    hideDialog();
                    Log.d(TAG, "onResponse: "+response.toString());
                    if(response.isSuccessful()){

                        configSewingOperationModels.clear();
                        if(response.body().getResultset().getOperation() != null){
                            configSewingOperationModels.clear();
                            for(int i=0; i<response.body().getResultset().getOperation().size(); i++){
                                V1_ConfigSewingOperationModel configSewingOperationModel = new V1_ConfigSewingOperationModel();

                                String sentence = response.body().getResultset().getOperation().get(i).getName();
                                boolean isNumber = sentence.matches("\\d+");
                                if(isNumber){
                                    configSewingOperationModel.setOperationName(response.body().getResultset().getOperation().get(i).getName());
                                }else{
                                    String[] words = sentence.split("\\s+");
                                    StringBuilder sb = new StringBuilder();
                                    for (String word : words) {
                                        sb.append(word.substring(0, 1).toUpperCase() + word.substring(1)).append(" ");
                                    }
                                    String capitalizedSentence = sb.toString().trim();
                                    configSewingOperationModel.setOperationName(capitalizedSentence);
                                }
                                configSewingOperationModel.setOperationId(String.valueOf(response.body().getResultset().getOperation().get(i).getId()));
                                configSewingOperationModel.setStatus(false);
                                configSewingOperationModels.add(configSewingOperationModel);
                            }
                            V1_OperationSorterListModel operationSorterListModel = new V1_OperationSorterListModel(configSewingOperationModels);
                            ArrayList<V1_ConfigSewingOperationModel> sorterListModelConfigSewingOperationModels = operationSorterListModel.getConfigSewingOperationModels();
                            configSewingOperationModels = sorterListModelConfigSewingOperationModels;

                            setOperationAdapterData();

                        }else{
                            errorDataSetLayout.setVisibility(View.VISIBLE);
                            dataSetLayout.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<V1_StyleWiseOperationResponse> call, Throwable t) {
                    hideDialog();
                    errorDataSetLayout.setVisibility(View.VISIBLE);
                    dataSetLayout.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            hideDialog();
            showErrorLayout("An unexpected error occurred.");
            Log.e(TAG, "requestForSewingOperation: " + e.getMessage(), e);
        }
    }

    private void showErrorLayout(String errorMessage) {
        errorDataSetLayout.setVisibility(View.VISIBLE);
        dataSetLayout.setVisibility(View.GONE);
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void  setOperationAdapterData() {

        if(savedJobNo.equals(jobNo) && savedItem.equals(itemName)){
            Map<String, V1_ConfigSewingOperationModel> map1 = new HashMap<>();
            for (V1_ConfigSewingOperationModel obj : configSewingOperationModels) {
                map1.put(obj.getOperationName(), obj);
            }

            for (V1_ConfigSewingOperationModel obj2 : operationItemModelList) {
                V1_ConfigSewingOperationModel obj1 = map1.get(obj2.getOperationName());
                if (obj1 != null) {
                    obj1.setStatus(true);
                }
            }
        }

        errorDataSetLayout.setVisibility(View.GONE);
        dataSetLayout.setVisibility(View.VISIBLE);
        styleWiseSewingOperationConfigRecyclerAdapter = new V1_StyleWiseSewingOperationConfigRecyclerAdapter(configSewingOperationModels, this,  this);
        _operationRecyclerView.setAdapter(styleWiseSewingOperationConfigRecyclerAdapter);
        _operationRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        styleWiseSewingOperationConfigRecyclerAdapter.notifyDataSetChanged();
    }

    private void sendRequestToServer() {
        showDialog();
        apiInterface.getBundkeWiseSewingInputClassCall().enqueue(new Callback<V1_BundleWiseSewingInputClass>() {
            @Override
            public void onResponse(Call<V1_BundleWiseSewingInputClass> call, Response<V1_BundleWiseSewingInputClass> response) {
                hideDialog();
                if(response.isSuccessful())
                {
                    V1_BundleWiseSewingInputClass.Company companyName;
                    List<V1_BundleWiseSewingInputClass.Company> companies = response.body().getResultset().getCompany();
                    companyNameList.add(0, "--Select Company--");
                    for(V1_BundleWiseSewingInputClass.Company d : companies)
                    {
                        companyName = d;
                        final V1_BundleWiseSewingInputClass.Company CompanyName = companyName;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                companyNameList.add(String.valueOf(CompanyName.getCompany()));
                            }
                        });
                    }

                    companyNameArray = new String[companyNameList.size()];
                    for(int i = 0; i < companyNameList.size(); i++)
                    {
                        companyNameArray[i] = companyNameList.get(i);
                    }

                    V1_BundleWiseSewingInputClass.Company companyId;
                    List<V1_BundleWiseSewingInputClass.Company> companiesId = response.body().getResultset().getCompany();
                    companyNameId.add(0, 0);
                    for(V1_BundleWiseSewingInputClass.Company d : companiesId)
                    {
                        companyId = d;
                        final V1_BundleWiseSewingInputClass.Company CompanyId = companyId;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                companyNameId.add(CompanyId.getId());
                            }
                        });
                    }
                    setSourceAdapterData();
                }
            }

            @Override
            public void onFailure(Call<V1_BundleWiseSewingInputClass> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(V1_StyleWiseLineConfigActivity_v3.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }
        });
    }

    private void setSourceAdapterData() {
        ArrayAdapter<String> adapterCompany = new ArrayAdapter<String>(this, R.layout.sewing_spinner_config_layout, companyNameArray);
        cCompany.setAdapter(adapterCompany);

        if(company != 0)
        {
            cCompany.setSelection(companyNameId.indexOf(companyId));
        }
        cCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int companyPosition = position;
                if(company != companyNameId.get(companyPosition)) {
                    companyId = companyNameId.get(companyPosition);
                    companyName = parent.getItemAtPosition(position).toString();
                    if(companyId != 0){
                        companyWiseLocation(companyId);
                        companyWiseProVariableSetup();
                        location = 0;
                        line = 0;
                        floor = 0;
                        try {
                            styleWiseDataItemModels.clear();
                            tabConfigRecyclerViewAdapter.notifyDataSetChanged();
                        }catch (Exception e){
                            Log.d(TAG, "onItemSelected: "+e.getMessage());
                        }
                        try {
                            configSewingOperationModels.clear();
                            styleWiseSewingOperationConfigRecyclerAdapter.notifyDataSetChanged();
                        }catch (Exception e){
                            Log.d(TAG, "onItemSelected: "+e.getMessage());
                        }
                    }
                }else{
                    companyId = companyNameId.get(companyPosition);
                    companyName = parent.getItemAtPosition(position).toString();
                    if(companyId != 0){
                        companyWiseLocation(companyId);
                        companyWiseProVariableSetup();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void companyWiseProVariableSetup() {
        showDialog();
        apiInterface.getProConfigurationResponseCall(String.valueOf(companyId)).enqueue(new Callback<ProConfigurationResponse>() {
            @Override
            public void onResponse(Call<ProConfigurationResponse> call, Response<ProConfigurationResponse> response) {
                hideDialog();
                if(response.isSuccessful()){
                    try {
                        companyWiseVariableSetup = response.body().getData().getVariableId();
                        Log.d(TAG, "showStyleWiseItem: "+companyWiseVariableSetup);
                        if(companyWiseVariableSetup.equals("1")){
                            poNoLayout.setVisibility(View.GONE);
                        }else if(companyWiseVariableSetup.equals("2")){
                            poNoLayout.setVisibility(View.VISIBLE);
                        }
                    }catch (Exception e){
                        Log.d(TAG, "onResponse: "+e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ProConfigurationResponse> call, Throwable t) {
                hideDialog();
                Log.d(TAG, "onFailure: #######"+t.getMessage());
            }
        });
    }

    private void companyWiseLocation(int companyPosition) {
        showDialog();
        apiInterface.getBundleWiseCompanyToLocationClassCall(companyPosition).enqueue(new Callback<V1_BundleWiseCompanyToLocationClass>() {
            @Override
            public void onResponse(Call<V1_BundleWiseCompanyToLocationClass> call, Response<V1_BundleWiseCompanyToLocationClass> response) {
                {
                    locationNameList.clear();
                    locationNameId.clear();
                    hideDialog();
                    V1_BundleWiseCompanyToLocationClass.Resultset locationName;
                    List<V1_BundleWiseCompanyToLocationClass.Resultset> locationses = response.body().getResultset();
                    locationNameList.add(0, "--Select Location--");
                    for(V1_BundleWiseCompanyToLocationClass.Resultset d : locationses)
                    {
                        locationName = d;
                        final V1_BundleWiseCompanyToLocationClass.Resultset LocationName = locationName;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                locationNameList.add(String.valueOf(LocationName.getName()));
                            }
                        });
                    }
                    locationNameArray = new String[locationNameList.size()];
                    for(int i = 0; i < locationNameList.size(); i++)
                    {
                        locationNameArray[i] = locationNameList.get(i);
                    }

                    V1_BundleWiseCompanyToLocationClass.Resultset locationId;
                    List<V1_BundleWiseCompanyToLocationClass.Resultset> locationsesId = response.body().getResultset();
                    locationNameId.add(0, 0);
                    for(V1_BundleWiseCompanyToLocationClass.Resultset d : locationsesId)
                    {
                        locationId = d;
                        final V1_BundleWiseCompanyToLocationClass.Resultset LocationId = locationId;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                locationNameId.add(LocationId.getId());
                            }
                        });
                    }

                    setAdapterData_Location();
                }
            }

            @Override
            public void onFailure(Call<V1_BundleWiseCompanyToLocationClass> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(V1_StyleWiseLineConfigActivity_v3.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }
        });
    }

    private void setAdapterData_Location() {
        ArrayAdapter<String> adapterLocation = new ArrayAdapter<String>(V1_StyleWiseLineConfigActivity_v3.this, R.layout.sewing_spinner_config_layout, locationNameArray);
        cLocation.setAdapter(adapterLocation);

        if(location != 0)
        {
            cLocation.setSelection(locationNameId.indexOf(location));
        }
        cLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int locationPosition = position;
                if(location != locationNameId.get(locationPosition)) {
                    locationId = locationNameId.get(locationPosition);
                    locationName = parent.getItemAtPosition(position).toString();
                    if(locationId != 0){
                        locationWiseFloor(locationId);
                        line = 0;
                        floor = 0;
                    }
                }else{
                    locationId = locationNameId.get(locationPosition);
                    locationName = parent.getItemAtPosition(position).toString();
                    if(locationId != 0){
                        locationWiseFloor(locationId);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void locationWiseFloor(int locationId) {
        showDialog();
        apiInterface.getBundleWiseLocationWiseFloorClassCall(locationId).enqueue(new Callback<V1_BundleWiseLocationWiseFloorClass>() {
            @Override
            public void onResponse(Call<V1_BundleWiseLocationWiseFloorClass> call, Response<V1_BundleWiseLocationWiseFloorClass> response) {
                hideDialog();
                if(response.isSuccessful())
                {
                    floorNameList.clear();
                    floorNameId.clear();
                    V1_BundleWiseLocationWiseFloorClass.Resultset floorName;
                    List<V1_BundleWiseLocationWiseFloorClass.Resultset> floorNames = response.body().getResultset();
                    floorNameList.add(0, "--Select Floor--");
                    for(V1_BundleWiseLocationWiseFloorClass.Resultset d : floorNames)
                    {
                        floorName = d;
                        final V1_BundleWiseLocationWiseFloorClass.Resultset floorName1 = floorName;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                floorNameList.add(String.valueOf(floorName1.getName()));
                            }
                        });
                    }
                    floorNameArray = new String[floorNameList.size()];
                    for(int i = 0; i < floorNameList.size(); i++)
                    {
                        floorNameArray[i] = floorNameList.get(i);
                    }

                    V1_BundleWiseLocationWiseFloorClass.Resultset floorId;
                    List<V1_BundleWiseLocationWiseFloorClass.Resultset> floorNamesId = response.body().getResultset();
                    floorNameId.add(0, 0);
                    for(V1_BundleWiseLocationWiseFloorClass.Resultset d : floorNamesId)
                    {
                        floorId = d;
                        final V1_BundleWiseLocationWiseFloorClass.Resultset floorName1 = floorId;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                floorNameId.add(floorName1.getId());
                            }
                        });
                    }
                    setAdapterData_Floor();
                }
            }

            @Override
            public void onFailure(Call<V1_BundleWiseLocationWiseFloorClass> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(V1_StyleWiseLineConfigActivity_v3.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }
        });
    }

    private void setAdapterData_Floor() {
        ArrayAdapter<String> adapterFloor = new ArrayAdapter<String>(this, R.layout.sewing_spinner_config_layout, floorNameArray);
        cFloor.setAdapter(adapterFloor);

        if(floor != 0)
        {
            cFloor.setSelection(floorNameId.indexOf(floor));
        }
        cFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int floorPosition = position;
                floorId = floorNameId.get(floorPosition);
                floorName = parent.getItemAtPosition(position).toString();
                flooeWiseline(floorId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void flooeWiseline(int floorId) {
        showDialog();
        apiInterface.getBundleWiseLocationWiseFloorClassCall(companyId, locationId, floorId, currentDate).enqueue(new Callback<V1_BundleWiseFloorWiseLineClass>() {
            @Override
            public void onResponse(Call<V1_BundleWiseFloorWiseLineClass> call, Response<V1_BundleWiseFloorWiseLineClass> response) {
                hideDialog();
                if(response.isSuccessful())
                {
                    Log.d(TAG, "onResponse: "+response.toString());
                    lineNameList.clear();
                    lineNameId.clear();
                    V1_BundleWiseFloorWiseLineClass.Resultset lineName;
                    List<V1_BundleWiseFloorWiseLineClass.Resultset> LineNames = response.body().getResultset();
                    lineNameList.add(0, "--Select Line--");
                    for(V1_BundleWiseFloorWiseLineClass.Resultset d : LineNames)
                    {
                        lineName = d;
                        final V1_BundleWiseFloorWiseLineClass.Resultset lineName1 = lineName;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lineNameList.add(String.valueOf(lineName1.getName()));
                            }
                        });
                    }
                    lineNameArray = new String[lineNameList.size()];
                    for(int i = 0; i < lineNameList.size(); i++)
                    {
                        lineNameArray[i] = lineNameList.get(i);
                    }

                    V1_BundleWiseFloorWiseLineClass.Resultset lineId;
                    List<V1_BundleWiseFloorWiseLineClass.Resultset> LineId = response.body().getResultset();
                    lineNameId.add(0, 0);
                    for(V1_BundleWiseFloorWiseLineClass.Resultset d : LineId)
                    {
                        lineId = d;
                        final V1_BundleWiseFloorWiseLineClass.Resultset lineNamesId = lineId;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lineNameId.add(lineNamesId.getId());
                            }
                        });
                    }

                    setAdapterData_Line();
                }
            }

            @Override
            public void onFailure(Call<V1_BundleWiseFloorWiseLineClass> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(V1_StyleWiseLineConfigActivity_v3.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }
        });
    }

    private void setAdapterData_Line() {
        ArrayAdapter<String> adapterLine = new ArrayAdapter<String>(this, R.layout.sewing_spinner_config_layout, lineNameArray);
        cLine.setAdapter(adapterLine);
        if(line != 0)
        {
            cLine.setSelection(lineNameId.indexOf(line));
        }
        cLine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int linePosition = position;
                lineId = lineNameId.get(linePosition);
                lineName = parent.getItemAtPosition(position).toString();
                selectedStylePosition = -1;
                if(saVedLineId != 0 && saVedLineId != lineId){
                    configSewingOperationModels.clear();
                    styleWiseDataItemModels.clear();
                    searchedStyleWiseDataItemModels.clear();
                    Log.d(TAG, "onItemSelected: "+configSewingOperationModels);
                    setOperationAdapterData();
                    setAdapterData();
//                    styleWiseSewingOperationConfigRecyclerAdapter.notifyDataSetChanged();
//                    if(configSewingOperationModels.size() > 0) {
//                        styleWiseSewingOperationConfigRecyclerAdapter.notifyDataSetChanged();
//                    }
                }
//                showStyleWiseItem();
                setAdapterData_SelftOrder();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setAdapterData_SelftOrder() {
        selfOrderArray = new String[]{"Yes", "No"};
        ArrayAdapter<String> adapterLine = new ArrayAdapter<String>(this, R.layout.sewing_spinner_config_layout, selfOrderArray);
        cSelfOrderSpinner.setAdapter(adapterLine);

        cSelfOrderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showStyleWiseItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void sendData() throws JSONException{
        JSONObject jsonObject = buildJsonObject();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
        showDialog();

        Call<V1_DataSaveResponse> configSaveApiCall = subContract
                ? apiInterface.saveTabConfigSubContractCall(body)
                : apiInterface.saveTabConfigCall(body);

        configSaveApiCall.enqueue(new Callback<V1_DataSaveResponse>() {
            @Override
            public void onResponse(Call<V1_DataSaveResponse> call, Response<V1_DataSaveResponse> response) {
                handleSaveResponse(response);
            }

            @Override
            public void onFailure(Call<V1_DataSaveResponse> call, Throwable t) {
                handleErrorResponse(t);
            }
        });
    }

    private void handleSaveResponse(Response<V1_DataSaveResponse> response) {
        hideDialog();
        Log.d(TAG, "onResponse: "+response.toString());
        if(response.isSuccessful()){
            showAlertMessage("Configuration successful.", 1);
        }else {
            showAlertMessage("Configuration failed, Please try again.", 0);
        }
    }

    private void handleErrorResponse(Throwable t) {
        hideDialog();
        showAlertMessage("Configuration found with this account on another device.", 0);
    }

    @SuppressLint("HardwareIds")
    private JSONObject buildJsonObject() throws JSONException {
        JSONObject save_obj = new JSONObject();
        save_obj.put("status", true);
        save_obj.put("mode", "save");
        save_obj.put("company_id",companyId);
        save_obj.put("location_id",locationId );
        save_obj.put("floor_id", floorId);
        save_obj.put("sewing_line", lineId);
        save_obj.put("job_id", jobId);
        save_obj.put("job_no", jobNo);
        save_obj.put("po_break_down_id", po_break_downId);
        save_obj.put("po_number", "0");
        save_obj.put("item_number_id", item_numberId);
        save_obj.put("IR_NUMBER", irNumber);
        save_obj.put("operation_ids", operation_ids);
        save_obj.put("STYLE_NUMBER", style_number);
        save_obj.put("mac", macAddress);
        save_obj.put("user_id", userId);
        save_obj.put("device_id", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        Log.d(TAG, "buildJsonObject: "+save_obj);
        return save_obj;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onHeadClick(int position, View v) {

        styleWiseSelection(position);
        for(int i=0; i < styleWiseDataItemModels.size(); i++){
            if( i == position){
                styleWiseDataItemModels.get(i).setStatus(true);
            } else {
                styleWiseDataItemModels.get(i).setStatus(false);
            }
        }
        tabConfigRecyclerViewAdapter.notifyDataSetChanged();

    }

    private void styleWiseSelection(int position) {
        Log.d(TAG, "styleWiseSelection: "+String.valueOf(position));
        if(position != -1){
            jobId = styleWiseDataItemModels.get(position).getJobId();
            jobNo = styleWiseDataItemModels.get(position).getJobNo();
            styleRefNo = styleWiseDataItemModels.get(position).getStyleRefNo();
            po_break_downId = styleWiseDataItemModels.get(position).getPoId();
            item_numberId = styleWiseDataItemModels.get(position).getItemNumberId();
            poId = styleWiseDataItemModels.get(position).getPoId();
            if(styleWiseDataItemModels.get(position).getIrNumber() != null){
                irNumber = styleWiseDataItemModels.get(position).getIrNumber();
            }
            countryName = styleWiseDataItemModels.get(position).getCountryName();
            itemName = styleWiseDataItemModels.get(position).getItemName();
            style_number = styleWiseDataItemModels.get(position).getStyleRefNo();
            buyerName = styleWiseDataItemModels.get(position).getBuyerName();
            requestForSewingOperation(styleWiseDataItemModels.get(position).getAutoDistribution());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = V1_MenuActivity.getStartIntent(V1_StyleWiseLineConfigActivity_v3.this);
        startActivity(intent);
        finish();
    }

    private void showAlertMessage(String msg, int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(V1_StyleWiseLineConfigActivity_v3.this);
        builder.setTitle("Message")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", (dialog, which) -> {
                    if(i == 1){
                        Intent intent = V1_MenuActivity.getStartIntent(V1_StyleWiseLineConfigActivity_v3.this);
                        startActivity(intent);
                    }else {
                        dialog.dismiss();
                    }

                });
        AlertDialog dialog  = builder.create();
        dialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onPause() {
        super.onPause();
        hideDialog();
        finish();
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
    public void onOperationHeadClick(int position, View v) {
        if(configSewingOperationModels.get(position).getStatus()){
            configSewingOperationModels.get(position).setStatus(false);
        }else{
            configSewingOperationModels.get(position).setStatus(true);
        }
        styleWiseSewingOperationConfigRecyclerAdapter.notifyDataSetChanged();
    }

    private void alertForApkUpdate(String message){
        ImageView cancel;
        Button updateBtn;
        TextView messageTV;

        View alertCustomDialog = LayoutInflater.from(this).inflate(R.layout.custom_configure_alert_layout,null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setView(alertCustomDialog);
        cancel = alertCustomDialog.findViewById(R.id.cancel_button);
        updateBtn = alertCustomDialog.findViewById(R.id.btnUpdate);
        messageTV = alertCustomDialog.findViewById(R.id.messageTV);

        messageTV.setText(message);
        final AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
        dialog.setCancelable(false);

        updateBtn.setOnClickListener(v -> {
            dialog.dismiss();
        });

        cancel.setOnClickListener( v -> {
            dialog.dismiss();
            finish();
        });
    }
}