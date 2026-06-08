package com.logicsoftbd.lsl.ui.v_1_ui.config;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseCompanyToLocationClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseFloorWiseLineClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseLocationWiseFloorClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseSewingInputClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DataSaveResponse;
import com.logicsoftbd.lsl.data.prefs.SessionManager;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.v_1_ui.menu.V1_MenuActivity;
import com.logicsoftbd.lsl.utils.ApiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class V1_ConfigActivity extends AppCompatActivity implements View.OnClickListener {
    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, V1_ConfigActivity.class);
        return intent;
    }

    private static final String TAG = "V1_ConfigActivity";
    private SessionManager session;
    private Spinner cCompany, cLocation, cFloor, cLine;
    private Button cDate, cMacAddress, cConfig;
    private String currentDate;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private String macAddress;

    public final ArrayList<String> companyNameList = new ArrayList<>();
    public final ArrayList<Integer> companyNameId = new ArrayList<>();
    public final ArrayList<String> locationNameList = new ArrayList<>();
    public final ArrayList<Integer> locationNameId = new ArrayList<>();
    public final ArrayList<String> floorNameList = new ArrayList<>();
    public final ArrayList<Integer> floorNameId = new ArrayList<>();
    public final ArrayList<String> lineNameList = new ArrayList<>();
    public final ArrayList<Integer> lineNameId = new ArrayList<>();

    public String[] companyNameArray, locationNameArray, floorNameArray, lineNameArray;

    private int companyId = 0, locationId = 0, floorId = 0, lineId = 0;
    private String userId = "", companyName = "", locationName = "", floorName = "", lineName = "";

    private android.app.AlertDialog.Builder alertDialogBuilder;
    private String base_url = "", urlstringbase, urladdressChk, urladdress, urlstring_c_wise_l, urlstring_l_wise_f, urlstring_f_wise_l;
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;
    private Toolbar mToolbar;
    private TextView _companyNameTV, _locationTV, _floorTV, _lineTV;
    private ProgressDialog pDialog;
    private int company = 0, location = 0, line = 0, floor = 0;


    // Saved data
    private String saveurlstringbase, saveurlstring_c_wise_l, saveurlstring_l_wise_f, saveurlstring_f_wise_l, saveurlstring_sewing_input;
    private Retrofit saveretrofit;

    public final ArrayList<String> savecompanyNameList = new ArrayList<>();
    public final ArrayList<Integer> savecompanyNameId = new ArrayList<>();
    public final ArrayList<String> savesuplierList = new ArrayList<>();
    public final ArrayList<String> savesourceList = new ArrayList<>();
    public final ArrayList<Integer> savesourceNameId = new ArrayList<>();
    public final ArrayList<String> savelocationList = new ArrayList<>();
    public final ArrayList<Integer> savelocationNameId = new ArrayList<>();
    public final ArrayList<String> savefloorList = new ArrayList<>();
    public final ArrayList<Integer> savefloorNameId = new ArrayList<>();
    public final ArrayList<String> savelineList = new ArrayList<>();
    public final ArrayList<Integer> savelineNameId = new ArrayList<>();

    public String[] savecompanyNameArray, savesuplierArray, savesourceArray, savelocationArray, savefloorArray, savelineArray;
    int scompanyId = 0, ssewingcompanyId = 0, ssourceId = 0, slocationId = 0, sfloorId = 0, slineId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        alertDialogBuilder = new android.app.AlertDialog.Builder(V1_ConfigActivity.this);
        session = new SessionManager(getApplicationContext());

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        company = (_preferences.getInt("company", 0));
        location = (_preferences.getInt("location", 0));
        line = (_preferences.getInt("line", 0));
        floor = (_preferences.getInt("floor", 0));
        userId = _preferences.getString("login_userid", "");
        base_url = (_preferences.getString("base_url", ""));
        Log.d(TAG, "onCreate: " + ":"+ company +  ":"+location + ":"+ line +  ":"+floor);

        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);
        macAddress = getMacAddr();
        initialization();
        getDefaultData();


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

    private void getDefaultData() {
        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        companyId = (_preferences.getInt("company", 0));
        locationId = (_preferences.getInt("location", 0));
        lineId = (_preferences.getInt("line", 0));
        floorId = (_preferences.getInt("floor", 0));
        companyName = (_preferences.getString("companyName", ""));
        locationName = (_preferences.getString("locationName", ""));
        lineName = (_preferences.getString("lineName", ""));
        floorName = (_preferences.getString("floorName", ""));

        if(companyName != null && locationName != null && floorName != null && lineName != null){
            _companyNameTV.setText(companyName);
            _locationTV.setText(locationName);
            _floorTV.setText(floorName);
            _lineTV.setText(lineName);
        }
    }

    private void initialization() {

        _companyNameTV = findViewById(R.id.companyNameTV);
        _locationTV = findViewById(R.id.locationTV);
        _floorTV = findViewById(R.id.floorTV);
        _lineTV = findViewById(R.id.lineTV);

        cCompany = findViewById(R.id.companySpinner);
        cLocation = findViewById(R.id.locationSpinnner);
        cFloor = findViewById(R.id.floorSpinner);
        cLine = findViewById(R.id.lineSpinner);

//        sSource = findViewById(R.id.savesourceSpinner);
//        sSewingCompany = findViewById(R.id.saveSewingCompany);
//        sCompany = findViewById(R.id.savecompanySpinner);
//        sLocation = findViewById(R.id.savelocationSpinnner);
//        sFloor = findViewById(R.id.savefloorSpinner);
//        sLineNo = findViewById(R.id.savelineSpinner);

        cDate = findViewById(R.id.configDateBtn);
        cMacAddress = findViewById(R.id.macAddressBtn);
        cConfig = findViewById(R.id.configBtn);

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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.configDateBtn:

                break;
            case R.id.macAddressBtn:

                break;
            case R.id.configBtn:
                SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(V1_ConfigActivity.this);
                SharedPreferences.Editor _editor = _preferences.edit();
                _editor.putInt("company", companyId);
                _editor.putString("companyName", companyName);
                _editor.putInt("location", locationId);
                _editor.putString("locationName", locationName);
                _editor.putInt("floor", floorId);
                _editor.putString("floorName", floorName);
                _editor.putInt("line", lineId);
                _editor.putString("lineName", lineName);
                _editor.putString("mac", String.valueOf(macAddress));
                Log.d(TAG, "onClick: "+companyId +" "+locationId+" "+floorId+" "+lineId);
                if(companyId != 0 && locationId != 0 && floorId != 0 && lineId != 0)
                {
                    _editor.apply();
                    try {
                        sendData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                else {
                    Toast.makeText(this, "Please fill the credentials", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.toolbar:
//                Intent intent = V1_MenuActivity.getStartIntent(V1_ConfigActivity.this);
//                startActivity(intent);
                finish();
        }
    }



    private void sendRequestToServer() {
        showDialog();
        apiInterface.getBundkeWiseSewingInputClassCall().enqueue(new Callback<V1_BundleWiseSewingInputClass>() {
            @Override
            public void onResponse(Call<V1_BundleWiseSewingInputClass> call, Response<V1_BundleWiseSewingInputClass> response) {
                hideDialog();
                Log.d(TAG, "onResponse: "+response.toString());
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
                    Toast.makeText(V1_ConfigActivity.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
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
                        location = 0;
                        line = 0;
                        floor = 0;
                    }
                }else{
                    companyId = companyNameId.get(companyPosition);
                    companyName = parent.getItemAtPosition(position).toString();
                    if(companyId != 0){
                        companyWiseLocation(companyId);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void companyWiseLocation(int companyPosition) {
        showDialog();
        apiInterface.getBundleWiseCompanyToLocationClassCall(companyPosition).enqueue(new Callback<V1_BundleWiseCompanyToLocationClass>() {
            @Override
            public void onResponse(Call<V1_BundleWiseCompanyToLocationClass> call, Response<V1_BundleWiseCompanyToLocationClass> response) {
                {
                    Log.d(TAG, "onResponse: "+response.toString());
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
                    Toast.makeText(V1_ConfigActivity.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
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
        ArrayAdapter<String> adapterLocation = new ArrayAdapter<String>(V1_ConfigActivity.this, R.layout.sewing_spinner_config_layout, locationNameArray);
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
                    Toast.makeText(V1_ConfigActivity.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(V1_ConfigActivity.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void sendData() throws Exception{
        JSONObject jsonObject = buidJsonObject();
        Log.d(TAG, "sendData: "+jsonObject);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
        showDialog();
        apiInterface.saveTabConfigCall(body).enqueue(new Callback<V1_DataSaveResponse>() {
            @Override
            public void onResponse(Call<V1_DataSaveResponse> call, Response<V1_DataSaveResponse> response) {
                hideDialog();
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful()){
                    showAlertMessage("Configuration successful.", 1);
                }
            }

            @Override
            public void onFailure(Call<V1_DataSaveResponse> call, Throwable t) {
                hideDialog();
                Log.d(TAG, "onResponse: "+t.getMessage());
                showAlertMessage("Configuration failed.", 0);
            }
        });
    }

    private JSONObject buidJsonObject() throws JSONException {

        JSONObject save_obj = new JSONObject();
        save_obj.put("status", true);
        save_obj.put("mode", "save");
        save_obj.put("company_id",companyId);
        save_obj.put("location_id",locationId );
        save_obj.put("floor_id", floorId);
        save_obj.put("sewing_line", lineId);
        save_obj.put("mac", macAddress);
        save_obj.put("user_id", userId);
        return save_obj;
    }

    private void showAlertMessage(String msg, int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(V1_ConfigActivity.this);
        builder.setTitle("Message")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", (dialog, which) -> {
                    if(i == 1){
                        Intent intent = V1_MenuActivity.getStartIntent(V1_ConfigActivity.this);
                        startActivity(intent);
                    }else {
                        dialog.dismiss();
                    }

                });
        AlertDialog dialog  = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = V1_MenuActivity.getStartIntent(V1_ConfigActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onPause() {
        super.onPause();
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
}
