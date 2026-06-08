package com.logicsoftbd.lsl.ui.v_1_ui.report;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.db.v1_db.helper.DBAdapter;
import com.logicsoftbd.lsl.data.db.v1_db.model.V1_User;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseCompanyToLocationClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseFloorWiseLineClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseLocationWiseFloorClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseSewingInputClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ChallanModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ChallanModelClass;
import com.logicsoftbd.lsl.data.prefs.SessionManager;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.v_1_ui.menu.V1_MenuActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_QRBarcodeScannerActivity;
import com.logicsoftbd.lsl.utils.ApiUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class V1_ChallanReportActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ChallanReportActivity";
    private SessionManager session;

    private Retrofit retrofit;
    private String urlString;

    private   int userID = 0;
    private String urladdress;
    private String urladdressChk;
    private String barcode;
    private DBAdapter dbAdapter;

    public final ArrayList<String> messageBnList = new ArrayList<>();
    public final ArrayList<String> messageEngList = new ArrayList<>();
    public final ArrayList<String> bundleNoList = new ArrayList<>();
    public final ArrayList<String> barcodeNoList = new ArrayList<>();
    public final ArrayList<String> yearList = new ArrayList<>();
    public final ArrayList<String> colorSizeIdList = new ArrayList<>();
    public final ArrayList<String> orderIdList = new ArrayList<>();
    public final ArrayList<String> itemIdList = new ArrayList<>();
    public final ArrayList<String> countryIdList = new ArrayList<>();
    public final ArrayList<String> sizeIdList = new ArrayList<>();
    public final ArrayList<String> colorIdList = new ArrayList<>();
    public final ArrayList<String> cutNoList = new ArrayList<>();
    public final ArrayList<String> jobNoList = new ArrayList<>();
    public final ArrayList<String> buyerList = new ArrayList<>();
    public final ArrayList<String> orderNoList = new ArrayList<>();
    public final ArrayList<String> itemList = new ArrayList<>();
    public final ArrayList<String> countryList = new ArrayList<>();
    public final ArrayList<String> colorList = new ArrayList<>();
    public final ArrayList<String> sizeList = new ArrayList<>();
    public final ArrayList<String> qtyList = new ArrayList<>();
    public final ArrayList<String> isRescanList = new ArrayList<>();
    public final ArrayList<String> colorTypeIdList = new ArrayList<>();


    public String[] messageBngArray, messageEngArray, bundleNoArray, barcodeNoArray, yearArray, colorSizeIdArray, orderIdArray, itemIdArray, countryIdArray, sizeIdArray, colorIdArray, cutNoArray, jobNoArray,
            buyerArray, orderNoArray, itemArray, countryArray, colorArray, sizeArray, qtyArray, isRescanArray, colorTypeIdArray;

    public static ArrayList<V1_ChallanModelClass> modelArrayList;

    private GridView challanGrid;
    private Button challanSubmit;
    private V1_ChallanReportAdapter challanReportAdapter;
    private ProgressDialog pDialog;
    private int status = 0, type = 12, company = 0, location = 0, line = 0, floor = 0;
    private String barcodeResult, currentDate;

    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;

    private Spinner sCompany, sSource, sSewingCompany, sLocation, sFloor, sLineNo;
    private String date, start_date, end_date, urlstringbase,urlstring_c_wise_l, urlstring_l_wise_f,
            urlstring_f_wise_l, urlstring_sewing_input, urlPendingData;

    private ApiInterface apiInterface;
    private ApiUtils apiUtils;

    public final ArrayList<String> companyNameList = new ArrayList<>();
    public final ArrayList<Integer> companyNameId = new ArrayList<>();
    public final ArrayList<String> suplierList = new ArrayList<>();
    public final ArrayList<String> sourceList = new ArrayList<>();
    public final ArrayList<Integer> sourceNameId = new ArrayList<>();
    public final ArrayList<String> locationList = new ArrayList<>();
    public final ArrayList<Integer> locationNameId = new ArrayList<>();
    public final ArrayList<String> floorList = new ArrayList<>();
    public final ArrayList<Integer> floorNameId = new ArrayList<>();
    public final ArrayList<String> lineList = new ArrayList<>();
    public final ArrayList<Integer> lineNameId = new ArrayList<>();

    public String[] companyNameArray;
    public String[] suplierArray;
    public String[] sourceArray;
    public String[] locationArray;
    public String[] floorArray;
    public String[] lineArray;

    /*Id Name*/
    int companyId = 0;
    int sewingcompanyId = 0;
    int sourceId = 0;
    int locationId = 0;
    int floorId = 0;
    int lineId = 0;

    private EditText sOrganic, barcodeET;
    private Button sDate, startdate, endDate, sSave, sPendingDaata;
    private ImageButton swingInputScan;
    private  String base_url="", type_entry, barcodeNumber;
    private int Year, Month, Day, userId = 0;
    private DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_challan_report);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        session = new SessionManager(getApplicationContext());

        Intent intent = getIntent();

        String resultS = intent.getStringExtra("result");
        urladdressChk = intent.getStringExtra("url");
        type_entry = intent.getStringExtra("qc");

        ArrayList<V1_User> loginData = new DBAdapter(this).getLoginData();

        if(urladdressChk != null)
        {
            urladdress = urladdressChk;
            userId = intent.getIntExtra("userId", 0);

        }else {
            urladdress = loginData.get(0).getUrl();
            userId = Integer.parseInt(loginData.get(0).getUserId());
        }

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        company = (_preferences.getInt("company", 0));
        location = (_preferences.getInt("location", 0));
        line = (_preferences.getInt("line", 0));
        floor = (_preferences.getInt("floor", 0));
        base_url = (_preferences.getString("base_url", ""));

        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);

        barcodeNumber = resultS;

        initialization();


    }

    private void initialization() {

        challanGrid = findViewById(R.id.challanGridView);
        sCompany = findViewById(R.id.companySpinner);
        sSource = findViewById(R.id.sourceSpinner);
        sSewingCompany = findViewById(R.id.sewingSpinner);
        sLocation = findViewById(R.id.locationSpinnner);
        sFloor = findViewById(R.id.floorSpinner);
        sLineNo = findViewById(R.id.lineSpinner);
        sDate = findViewById(R.id.sewingDateBtn);
        challanSubmit = findViewById(R.id.challanSubmitButton);
        challanSubmit.setOnClickListener(this);
        swingInputScan = findViewById(R.id.input_scanBtn);
        swingInputScan.setOnClickListener(this);

        sendRequestToServerForCompany();

        //set Date
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = simpleDateFormat.format(calendar.getTime());
        sDate.setText(currentDate);

        //barcode ET
        barcodeET = findViewById(R.id.barcodenumberText);

    }

    private void sendRequestToServerForCompany() {
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

                    V1_BundleWiseSewingInputClass.Source sourceName;
                    List<V1_BundleWiseSewingInputClass.Source> sources = response.body().getResultset().getSource();
                    sourceList.add(0, "--Select Source--");
                    for(V1_BundleWiseSewingInputClass.Source d : sources)
                    {
                        sourceName = d;
                        final V1_BundleWiseSewingInputClass.Source SourceName = sourceName;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sourceList.add(String.valueOf(SourceName.getName()));
                            }
                        });
                    }

                    sourceArray = new String[sourceList.size()];
                    for(int i = 0; i < sourceList.size(); i++)
                    {
                        sourceArray[i] = sourceList.get(i);
                    }

                    V1_BundleWiseSewingInputClass.Source sourceId;
                    List<V1_BundleWiseSewingInputClass.Source> sourcesId = response.body().getResultset().getSource();
                    sourceNameId.add(0, 0);
                    for(V1_BundleWiseSewingInputClass.Source d : sourcesId)
                    {
                        sourceId = d;
                        final V1_BundleWiseSewingInputClass.Source SourceName = sourceId;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sourceNameId.add(SourceName.getId());
                            }
                        });
                    }

                    V1_BundleWiseSewingInputClass.Supplier supplierName;
                    List<V1_BundleWiseSewingInputClass.Supplier> suppliers = response.body().getResultset().getSupplier();
                    suplierList.add(0, "--Select Supplier--");
                    for(V1_BundleWiseSewingInputClass.Supplier d : suppliers)
                    {
                        supplierName = d;
                        final V1_BundleWiseSewingInputClass.Supplier SourceName = supplierName;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                suplierList.add(String.valueOf(SourceName.getName()));
                            }
                        });
                    }
                    suplierArray = new String[suplierList.size()];
                    for(int i = 0; i < suplierList.size(); i++)
                    {
                        suplierArray[i] = suplierList.get(i);
                    }

                    setSourceadapterData();
                }
            }

            @Override
            public void onFailure(Call<V1_BundleWiseSewingInputClass> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(V1_ChallanReportActivity.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }
        });
    }

    private void setSourceadapterData() {
        ArrayAdapter<String> adapterCompany = new ArrayAdapter<String>(this, R.layout.sewing_spinner_layout, companyNameArray);
        sCompany.setAdapter(adapterCompany);

        sCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int companyPosition = position;
                if(company != 0)
                {
                    sCompany.setSelection(companyNameId.indexOf(company));
                    companyId = companyNameId.get(companyPosition);
                }
                else {
                    Toast.makeText(V1_ChallanReportActivity.this, "Please Configure Your Tab..", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapterSource = new ArrayAdapter<String>(this, R.layout.sewing_spinner_layout, sourceArray);
        sSource.setAdapter(adapterSource);

        sSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(company != 0)
                {
                    sSource.setSelection(1);
                }

                switch (position)
                {
                    case 0:
                        setSuplierDefault();
                        sourceId = 0;
                        break;
                    case 1:
                        setSuplierInhoise();
                        sourceId = 1;
                        break;
                    case 2:
                        setUpOutBountDefault();
                        sourceId = 3;
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpOutBountDefault() {

        setSuplierOutBound();

        ArrayAdapter<CharSequence> adapterDefault = ArrayAdapter.createFromResource(this,R.array.selectSupplier
                , R.layout.sewing_spinner_layout);
        sLocation.setAdapter(adapterDefault);
        sFloor.setAdapter(adapterDefault);
        sLineNo.setAdapter(adapterDefault);


    }
    private void setSuplierOutBound() {
        ArrayAdapter<String> adapterSuplier = new ArrayAdapter<String>(this, R.layout.sewing_spinner_layout, suplierArray);
        sSewingCompany.setAdapter(adapterSuplier);
    }
    private void setSuplierDefault() {
        ArrayAdapter<CharSequence> adapterDefault = ArrayAdapter.createFromResource(this,R.array.selectSupplier
                , R.layout.sewing_spinner_layout);
        sSewingCompany.setAdapter(adapterDefault);
        sLocation.setAdapter(adapterDefault);
        sFloor.setAdapter(adapterDefault);
        sLineNo.setAdapter(adapterDefault);
    }
    private void setSuplierInhoise() {

        setUpOutBountDefault();

        ArrayAdapter<String> adapterSuplier = new ArrayAdapter<String>(this, R.layout.sewing_spinner_layout, companyNameArray);
        sSewingCompany.setAdapter(adapterSuplier);

        sSewingCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int companyPosition = position;
                if(company > 0)
                {
                    if(sourceId == 1)
                    {
                        sSewingCompany.setSelection(companyNameId.indexOf(company));
                    }
                    sewingcompanyId = companyNameId.get(companyPosition);
                    companyWiseLocation(sewingcompanyId);
                }
                else {
                    Toast.makeText(V1_ChallanReportActivity.this, "Please Configure Your Tab..", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /*Company and Location*/

    private void companyWiseLocation(int position) {
        if(sourceId == 1) {
            apiInterface.getBundleWiseCompanyToLocationClassCall(position).enqueue(new Callback<V1_BundleWiseCompanyToLocationClass>() {
                @Override
                public void onResponse(Call<V1_BundleWiseCompanyToLocationClass> call, Response<V1_BundleWiseCompanyToLocationClass> response) {
                    hideDialog();
                    if(response.isSuccessful())
                    {
                        if(locationList.isEmpty())
                        {
                            loadDataLocation(response);
                        }
                        else if(!locationList.isEmpty())
                        {
                            locationList.clear();
                            loadDataLocation(response);
                        }
                    }
                }

                private void loadDataLocation(Response<V1_BundleWiseCompanyToLocationClass> response) {
                    V1_BundleWiseCompanyToLocationClass.Resultset locationName;
                    List<V1_BundleWiseCompanyToLocationClass.Resultset> locationses = response.body().getResultset();
                    locationList.add(0, "--Select Location--");
                    for(V1_BundleWiseCompanyToLocationClass.Resultset d : locationses)
                    {
                        locationName = d;
                        final V1_BundleWiseCompanyToLocationClass.Resultset LocationName = locationName;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                locationList.add(String.valueOf(LocationName.getName()));
                            }
                        });
                    }
                    locationArray = new String[locationList.size()];
                    for(int i = 0; i < locationList.size(); i++)
                    {
                        locationArray[i] = locationList.get(i);
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

                @Override
                public void onFailure(Call<V1_BundleWiseCompanyToLocationClass> call, Throwable t) {
                    if (t instanceof IOException) {
                        Toast.makeText(V1_ChallanReportActivity.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                }
            });
        }
    }

    private void setAdapterData_Location() {
        ArrayAdapter<String> adapterLocation = new ArrayAdapter<String>(V1_ChallanReportActivity.this, R.layout.sewing_spinner_layout, locationArray);
        sLocation.setAdapter(adapterLocation);

        sLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int locationPosition = position;
                if(sewingcompanyId == company)
                {
                    sLocation.setSelection(locationNameId.indexOf(location));
                }
                locationId = locationNameId.get(locationPosition);
                locationWiseFloor(locationId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /*Floor Data*/
    private void locationWiseFloor(int locationPosition) {
        apiInterface.getBundleWiseLocationWiseFloorClassCall(locationPosition).enqueue(new Callback<V1_BundleWiseLocationWiseFloorClass>() {
            @Override
            public void onResponse(Call<V1_BundleWiseLocationWiseFloorClass> call, Response<V1_BundleWiseLocationWiseFloorClass> response) {
                hideDialog();
                if(response.isSuccessful())
                {
                    if(floorList.isEmpty())
                    {
                        loadFloorData(response);
                    }
                    else if(!floorList.isEmpty())
                    {
                        floorList.clear();
                        loadFloorData(response);
                    }
                }
            }

            @Override
            public void onFailure(Call<V1_BundleWiseLocationWiseFloorClass> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(V1_ChallanReportActivity.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }
        });
    }


    private void loadFloorData(Response<V1_BundleWiseLocationWiseFloorClass> response) {
        V1_BundleWiseLocationWiseFloorClass.Resultset floorName;
        List<V1_BundleWiseLocationWiseFloorClass.Resultset> floorNames = response.body().getResultset();
        floorList.add(0, "--Select Floor--");
        for(V1_BundleWiseLocationWiseFloorClass.Resultset d : floorNames)
        {
            floorName = d;
            final V1_BundleWiseLocationWiseFloorClass.Resultset floorName1 = floorName;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    floorList.add(String.valueOf(floorName1.getName()));
                }
            });
        }
        floorArray = new String[floorList.size()];
        for(int i = 0; i < floorList.size(); i++)
        {
            floorArray[i] = floorList.get(i);
        }

        V1_BundleWiseLocationWiseFloorClass.Resultset floorId;
        List<V1_BundleWiseLocationWiseFloorClass.Resultset> floorNamesId = response.body().getResultset();
        floorNameId.add(0, 0);
        for(V1_BundleWiseLocationWiseFloorClass.Resultset d : floorNamesId)
        {
            floorId = d;
            final V1_BundleWiseLocationWiseFloorClass.Resultset FloodId = floorId;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    floorNameId.add(FloodId.getId());
                }
            });
        }

//        floorArray = new String[floorList.size()];
//        for(int i = 0; i < floorList.size(); i++)
//        {
//            floorArray[i] = floorList.get(i);
//        }

        setAdapterData_Floor();
    }

    private void setAdapterData_Floor() {
        ArrayAdapter<String> adapterFloor = new ArrayAdapter<String>(this, R.layout.sewing_spinner_layout, floorArray);
        sFloor.setAdapter(adapterFloor);

        sFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int floorPosition = position;
                if(locationId == location)
                {
                    sFloor.setSelection(floorNameId.indexOf(floor));
                }

                floorId = floorNameId.get(floorPosition);
                flooeWiseline(floorId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /*Line Data*/
    private void flooeWiseline(int floorPosition) {
        apiInterface.getBundleWiseLocationWiseFloorClassCall(sewingcompanyId, locationId, floorPosition, currentDate).enqueue(new Callback<V1_BundleWiseFloorWiseLineClass>() {
            @Override
            public void onResponse(Call<V1_BundleWiseFloorWiseLineClass> call, Response<V1_BundleWiseFloorWiseLineClass> response) {
                hideDialog();
                if(response.isSuccessful())
                {
                    if(lineList.isEmpty())
                    {
                        loadLineData(response);
                    }
                    else if(!lineList.isEmpty())
                    {
                        lineList.clear();
                        loadLineData(response);
                    }
                }
            }

            @Override
            public void onFailure(Call<V1_BundleWiseFloorWiseLineClass> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(V1_ChallanReportActivity.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }
        });
    }


    private void loadLineData(Response<V1_BundleWiseFloorWiseLineClass> response) {
        V1_BundleWiseFloorWiseLineClass.Resultset lineName;
        List<V1_BundleWiseFloorWiseLineClass.Resultset> LineNames = response.body().getResultset();
        lineList.add(0, "--Select Line--");
        for(V1_BundleWiseFloorWiseLineClass.Resultset d : LineNames)
        {
            lineName = d;
            final V1_BundleWiseFloorWiseLineClass.Resultset lineName1 = lineName;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lineList.add(String.valueOf(lineName1.getName()));
                }
            });
        }
        lineArray = new String[lineList.size()];
        for(int i = 0; i < lineList.size(); i++)
        {
            lineArray[i] = lineList.get(i);
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

    private void setAdapterData_Line() {
        ArrayAdapter<String> adapterLine = new ArrayAdapter<String>(this, R.layout.sewing_spinner_layout, lineArray);
        sLineNo.setAdapter(adapterLine);

        sLineNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int linePosition = position;
                if(floorId == floor)
                {
                    sLineNo.setSelection(lineNameId.indexOf(line));
                }
                lineId = lineNameId.get(linePosition);
                sendReuestToServerAfterScan(lineId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void sendReuestToServerAfterScan(int lineId) {
        if(lineId > 0)
        {
            if (barcodeNumber != null && type_entry.equals("line_wise_sewing_input_challan_wise"))
            {
                challansewingInputGetDataMethod(barcodeNumber);
            }
        }
    }

    private void challansewingInputGetDataMethod(String barcode) {
        showDialog();
        apiInterface.getChallanModelCall(sewingcompanyId, barcode, type).enqueue(new Callback<V1_ChallanModel>() {
            @Override
            public void onResponse(Call<V1_ChallanModel> call, Response<V1_ChallanModel> response) {
                if(response.isSuccessful()){
                    hideDialog();
                    V1_ChallanModel.DTL messageBn;
                    final List<V1_ChallanModel.DTL> messagesBN = response.body().getResultset().getDTLS();
                    for(V1_ChallanModel.DTL d : messagesBN)
                    {
                        messageBn = d;
                        final V1_ChallanModel.DTL finalName = messageBn;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messageBnList.add(String.valueOf(finalName.getMessageBng()));
                            }
                        });
                    }

                    V1_ChallanModel.DTL messageEn;
                    final List<V1_ChallanModel.DTL> messagesEN = response.body().getResultset().getDTLS();
                    for(V1_ChallanModel.DTL d : messagesEN)
                    {
                        messageEn = d;
                        final V1_ChallanModel.DTL finalName = messageEn;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messageEngList.add(String.valueOf(finalName.getMessageEng()));
                            }
                        });
                    }

                    V1_ChallanModel.DTL bundleNo;
                    final List<V1_ChallanModel.DTL> bundleNos = response.body().getResultset().getDTLS();
                    for(V1_ChallanModel.DTL d : bundleNos)
                    {
                        bundleNo = d;
                        final V1_ChallanModel.DTL finalName = bundleNo;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bundleNoList.add(String.valueOf(finalName.getBundleNo()));
                            }
                        });
                    }

                    V1_ChallanModel.DTL barcodeNo;
                    final List<V1_ChallanModel.DTL> barcodeNos = response.body().getResultset().getDTLS();
                    for(V1_ChallanModel.DTL d : barcodeNos)
                    {
                        barcodeNo = d;
                        final V1_ChallanModel.DTL finalName = barcodeNo;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                barcodeNoList.add(String.valueOf(finalName.getBarcodeNo()));
                            }
                        });
                    }

                    V1_ChallanModel.DTL year;
                    final List<V1_ChallanModel.DTL> years = response.body().getResultset().getDTLS();
                    for(V1_ChallanModel.DTL d : years)
                    {
                        year = d;
                        final V1_ChallanModel.DTL finalName = year;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                yearList.add(String.valueOf(finalName.getYear()));
                            }
                        });
                    }

                    V1_ChallanModel.DTL colorSizeId;
                    final List<V1_ChallanModel.DTL> colorSizeIds = response.body().getResultset().getDTLS();
                    for(V1_ChallanModel.DTL d : colorSizeIds)
                    {
                        colorSizeId = d;
                        final V1_ChallanModel.DTL finalName = colorSizeId;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                colorSizeIdList.add(String.valueOf(finalName.getColorSizeId()));
                            }
                        });
                    }

                    V1_ChallanModel.DTL orderId;
                    final List<V1_ChallanModel.DTL> orderIds = response.body().getResultset().getDTLS();
                    for(V1_ChallanModel.DTL d : orderIds)
                    {
                        orderId = d;
                        final V1_ChallanModel.DTL finalName = orderId;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                orderIdList.add(String.valueOf(finalName.getColorId()));
                            }
                        });
                    }

                    V1_ChallanModel.DTL itemId;
                    final List<V1_ChallanModel.DTL> itemIds = response.body().getResultset().getDTLS();
                    for(V1_ChallanModel.DTL d : itemIds)
                    {
                        itemId = d;
                        final V1_ChallanModel.DTL finalName = itemId;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                itemIdList.add(String.valueOf(finalName.getItemId()));
                            }
                        });
                    }

                    V1_ChallanModel.DTL countryId;
                    final List<V1_ChallanModel.DTL> countryIds = response.body().getResultset().getDTLS();
                    for(V1_ChallanModel.DTL d : countryIds)
                    {
                        countryId = d;
                        final V1_ChallanModel.DTL finalName = countryId;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                countryIdList.add(String.valueOf(finalName.getCountryId()));
                            }
                        });
                    }

                    V1_ChallanModel.DTL sizeId;
                    final List<V1_ChallanModel.DTL> sizeIds = response.body().getResultset().getDTLS();
                    for(V1_ChallanModel.DTL d : sizeIds)
                    {
                        sizeId = d;
                        final V1_ChallanModel.DTL finalName = sizeId;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sizeIdList.add(String.valueOf(finalName.getSizeId()));
                            }
                        });
                    }

                    V1_ChallanModel.DTL colorId;
                    final List<V1_ChallanModel.DTL> colorIds = response.body().getResultset().getDTLS();
                    for(V1_ChallanModel.DTL d : colorIds)
                    {
                        colorId = d;
                        final V1_ChallanModel.DTL finalName = colorId;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                colorIdList.add(String.valueOf(finalName.getColorId()));
                            }
                        });
                    }

                    V1_ChallanModel.DTL cutNo;
                    final List<V1_ChallanModel.DTL> cutNos = response.body().getResultset().getDTLS();
                    for(V1_ChallanModel.DTL d : cutNos)
                    {
                        cutNo = d;
                        final V1_ChallanModel.DTL finalName = cutNo;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cutNoList.add(String.valueOf(finalName.getCutNo()));
                            }
                        });
                    }

                    V1_ChallanModel.DTL jobNo;
                    final List<V1_ChallanModel.DTL> jobNos = response.body().getResultset().getDTLS();
                    for(V1_ChallanModel.DTL d : jobNos)
                    {
                        jobNo = d;
                        final V1_ChallanModel.DTL finalName = jobNo;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                jobNoList.add(String.valueOf(finalName.getJobNo()));
                            }
                        });
                    }

                    V1_ChallanModel.DTL buyer;
                    final List<V1_ChallanModel.DTL> buyers = response.body().getResultset().getDTLS();
                    for(V1_ChallanModel.DTL d : buyers)
                    {
                        buyer = d;
                        final V1_ChallanModel.DTL finalName = buyer;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                buyerList.add(String.valueOf(finalName.getBuyer()));
                            }
                        });
                    }

                    V1_ChallanModel.DTL orderNo;
                    final List<V1_ChallanModel.DTL> orderNos = response.body().getResultset().getDTLS();
                    for(V1_ChallanModel.DTL d : orderNos)
                    {
                        orderNo = d;
                        final V1_ChallanModel.DTL finalName = orderNo;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                orderNoList.add(String.valueOf(finalName.getOrderNo()));
                            }
                        });
                    }

                    V1_ChallanModel.DTL item;
                    final List<V1_ChallanModel.DTL> items = response.body().getResultset().getDTLS();
                    for(V1_ChallanModel.DTL d : items)
                    {
                        item = d;
                        final V1_ChallanModel.DTL finalName = item;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                itemList.add(String.valueOf(finalName.getItem()));
                            }
                        });
                    }

                    V1_ChallanModel.DTL country;
                    final List<V1_ChallanModel.DTL> countrys = response.body().getResultset().getDTLS();
                    for(V1_ChallanModel.DTL d : countrys)
                    {
                        country = d;
                        final V1_ChallanModel.DTL finalName = country;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                countryList.add(String.valueOf(finalName.getCountry()));
                            }
                        });
                    }

                    V1_ChallanModel.DTL color;
                    final List<V1_ChallanModel.DTL> colors = response.body().getResultset().getDTLS();
                    for(V1_ChallanModel.DTL d : colors)
                    {
                        color = d;
                        final V1_ChallanModel.DTL finalName = color;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                colorList.add(String.valueOf(finalName.getColor()));
                            }
                        });
                    }

                    V1_ChallanModel.DTL size;
                    final List<V1_ChallanModel.DTL> sizes = response.body().getResultset().getDTLS();
                    for(V1_ChallanModel.DTL d : sizes)
                    {
                        size = d;
                        final V1_ChallanModel.DTL finalName = size;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sizeList.add(String.valueOf(finalName.getSize()));
                            }
                        });
                    }

                    V1_ChallanModel.DTL qty;
                    final List<V1_ChallanModel.DTL> qtys = response.body().getResultset().getDTLS();
                    for(V1_ChallanModel.DTL d : qtys)
                    {
                        qty = d;
                        final V1_ChallanModel.DTL finalName = qty;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                qtyList.add(String.valueOf(finalName.getQty()));
                            }
                        });
                    }

                    V1_ChallanModel.DTL isRescan;
                    final List<V1_ChallanModel.DTL> isRescans = response.body().getResultset().getDTLS();
                    for(V1_ChallanModel.DTL d : isRescans)
                    {
                        isRescan = d;
                        final V1_ChallanModel.DTL finalName = isRescan;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isRescanList.add(String.valueOf(finalName.getIsRescan()));
                            }
                        });
                    }

                    V1_ChallanModel.DTL colorTypeId;
                    final List<V1_ChallanModel.DTL> colorTypeIds = response.body().getResultset().getDTLS();
                    for(V1_ChallanModel.DTL d : colorTypeIds)
                    {
                        colorTypeId = d;
                        final V1_ChallanModel.DTL finalName = colorTypeId;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                colorTypeIdList.add(String.valueOf(finalName.getColorTypeId()));
                            }
                        });
                    }

                    modelArrayList = getModel();
                    challanReportAdapter = new V1_ChallanReportAdapter(V1_ChallanReportActivity.this, R.layout.challan_report_layout, modelArrayList);
                    challanGrid.setAdapter(challanReportAdapter);

                }
                else {
                    hideDialog();
                }
            }

            @Override
            public void onFailure(Call<V1_ChallanModel> call, Throwable t) {
                hideDialog();
                Toast.makeText(V1_ChallanReportActivity.this, String.valueOf(t.getMessage()), Toast.LENGTH_SHORT).show();
            }
        });

//        challanSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                postDataoServer();
//            }
//        });

    }

    private ArrayList<V1_ChallanModelClass> getModel() {
        ArrayList<V1_ChallanModelClass> list = new ArrayList<>();
        messageBngArray = new String[messageBnList.size()];
        for(int i = 0; i < messageBnList.size(); i++)
        {
            messageBngArray[i] = messageBnList.get(i);
        }

        messageEngArray = new String[messageEngList.size()];
        for(int i = 0; i < messageEngList.size(); i++)
        {
            messageEngArray[i] = messageEngList.get(i);
        }

        bundleNoArray = new String[bundleNoList.size()];
        for(int i = 0; i < bundleNoList.size(); i++)
        {
            bundleNoArray[i] = bundleNoList.get(i);
        }

        barcodeNoArray = new String[barcodeNoList.size()];
        for(int i = 0; i < barcodeNoList.size(); i++)
        {
            barcodeNoArray[i] = barcodeNoList.get(i);
        }

        yearArray = new String[yearList.size()];
        for(int i = 0; i < yearList.size(); i++)
        {
            yearArray[i] = yearList.get(i);
        }

        colorSizeIdArray = new String[colorSizeIdList.size()];
        for(int i = 0; i < colorSizeIdList.size(); i++)
        {
            colorSizeIdArray[i] = colorSizeIdList.get(i);
        }

        orderIdArray = new String[orderIdList.size()];
        for(int i = 0; i < orderIdList.size(); i++)
        {
            orderIdArray[i] = orderIdList.get(i);
        }

        itemIdArray = new String[itemIdList.size()];
        for(int i = 0; i < itemIdList.size(); i++)
        {
            itemIdArray[i] = itemIdList.get(i);
        }

        countryIdArray = new String[countryIdList.size()];
        for(int i = 0; i < countryIdList.size(); i++)
        {
            countryIdArray[i] = countryIdList.get(i);
        }

        sizeIdArray = new String[sizeIdList.size()];
        for(int i = 0; i < sizeIdList.size(); i++)
        {
            sizeIdArray[i] = sizeIdList.get(i);
        }

        colorIdArray = new String[colorIdList.size()];
        for(int i = 0; i < colorIdList.size(); i++)
        {
            colorIdArray[i] = colorIdList.get(i);
        }

        cutNoArray = new String[cutNoList.size()];
        for(int i = 0; i < cutNoList.size(); i++)
        {
            cutNoArray[i] = cutNoList.get(i);
        }

        jobNoArray = new String[jobNoList.size()];
        for(int i = 0; i < jobNoList.size(); i++)
        {
            jobNoArray[i] = jobNoList.get(i);
        }

        buyerArray = new String[buyerList.size()];
        for(int i = 0; i < buyerList.size(); i++)
        {
            buyerArray[i] = buyerList.get(i);
        }

        orderNoArray = new String[orderNoList.size()];
        for(int i = 0; i < orderNoList.size(); i++)
        {
            orderNoArray[i] = orderNoList.get(i);
        }

        itemArray = new String[itemList.size()];
        for(int i = 0; i < itemList.size(); i++)
        {
            itemArray[i] = itemList.get(i);
        }

        countryArray = new String[countryList.size()];
        for(int i = 0; i < countryList.size(); i++)
        {
            countryArray[i] = countryList.get(i);
        }

        colorArray = new String[colorList.size()];
        for(int i = 0; i < colorList.size(); i++)
        {
            colorArray[i] = colorList.get(i);
        }

        sizeArray = new String[sizeList.size()];
        for(int i = 0; i < sizeList.size(); i++)
        {
            sizeArray[i] = sizeList.get(i);
        }

        qtyArray = new String[qtyList.size()];
        for(int i = 0; i < qtyList.size(); i++)
        {
            qtyArray[i] = qtyList.get(i);
        }

        isRescanArray = new String[isRescanList.size()];
        for(int i = 0; i < isRescanList.size(); i++)
        {
            isRescanArray[i] = isRescanList.get(i);
        }

        colorTypeIdArray = new String[colorTypeIdList.size()];
        for(int i = 0; i < colorTypeIdList.size(); i++)
        {
            colorTypeIdArray[i] = colorTypeIdList.get(i);
        }

        for(int i = 0; i < messageBngArray.length; i++){
            V1_ChallanModelClass challanModelClass = new V1_ChallanModelClass();
            challanModelClass.setMessageBng(messageBngArray[i]);
            challanModelClass.setMessageEng(messageBngArray[i]);
            challanModelClass.setBundleNo(bundleNoArray[i]);
            challanModelClass.setBarcodeNo(barcodeNoArray[i]);
            challanModelClass.setYear(yearArray[i]);
            challanModelClass.setColorSizeId(colorSizeIdArray[i]);
            challanModelClass.setOrderId(orderIdArray[i]);
            challanModelClass.setItemId(itemIdArray[i]);
            challanModelClass.setCountryId(countryIdArray[i]);
            challanModelClass.setSizeId(sizeIdArray[i]);
            challanModelClass.setColorId(colorIdArray[i]);
            challanModelClass.setCutNo(cutNoArray[i]);
            challanModelClass.setJobNo(jobNoArray[i]);
            challanModelClass.setBuyer(buyerArray[i]);
            challanModelClass.setOrderNo(orderNoArray[i]);
            challanModelClass.setItem(itemArray[i]);
            challanModelClass.setCountry(countryArray[i]);
            challanModelClass.setColor(colorArray[i]);
            challanModelClass.setSize(sizeArray[i]);
            challanModelClass.setQty(qtyArray[i]);
            challanModelClass.setIsRescan(isRescanArray[i]);
            challanModelClass.setColorTypeId(colorTypeIdArray[i]);
            challanModelClass.setDeleteStatus(0);
            list.add(challanModelClass);
        }
        return list;
    }

    private void postDataoServer() {
        // perform HTTP POST request
        if(checkNetworkConnection())
            new HTTPAsyncTask().execute(String.format("http://%s/logic-api/index.php/api/android/save_update_sewing_input_output_by_challan", urladdress));
        else
            Toast.makeText(this, "Not Connected!", Toast.LENGTH_SHORT).show();
        checkNetworkConnection();
    }

    private boolean checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isConnected = false;
        if (networkInfo != null && (isConnected = networkInfo.isConnected())) {
            //Toast.makeText(this, networkInfo.getTypeName(), Toast.LENGTH_SHORT).show();


        } else {
            Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show();
        }

        return isConnected;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.input_scanBtn:
                barcodeScanMethod();
                break;
            case R.id.sewingDateBtn:
                datePickerMethod(sDate);
                break;

            case R.id.pendingDataBT:
                if(sourceId == 1)
                {
                    if(locationId != 0)
                    {
                        //pendingDataShow();

                    }
                    else {
                        Toast.makeText(this, "Please fill the credentials", Toast.LENGTH_SHORT).show();
                    }

                }

                break;

            case R.id.startdate:
                datepicker(startdate);
                break;

            case R.id.enddate:
                datepicker(endDate);
                break;
//            case R.id.input_scanBtn:
//                barcode = barcodeET.getText().toString();
//                sewingInputGetDataMethod(barcode);
//                break;
            case R.id.challanSubmitButton:
                if(sourceId == 1)
                {
                    if(locationId != 0 && floorId != 0 && lineId != 0)
                    {
                        postDataoServer();
                    }
                    else {
                        Toast.makeText(this, "Please fill the credentials", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
        }
    }

    private void barcodeScanMethod() {
        barcode = barcodeET.getText().toString();

        if(!barcode.isEmpty())
        {
            barcode = barcodeET.getText().toString();
            challansewingInputGetDataMethod(barcode);
        }
        else {
            Intent intent = new Intent(this, V1_QRBarcodeScannerActivity.class);
            intent.putExtra("url", urladdress);
            intent.putExtra("qc", "line_wise_sewing_input_challan_wise");
            startActivity(intent);
        }
    }

    private class HTTPAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                try {
                    return httpPost(urls[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return "Failed!";
                }
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(V1_ChallanReportActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }

    private String httpPost(String myUrl) throws IOException, JSONException {
        String result = "";

        URL url = new URL(myUrl);

        // 1. create HttpURLConnection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        // 2. build JSON object
        JSONObject jsonObject = buidJsonObject();

        // 3. add JSON content to POST request body
        setPostRequestContent(conn, jsonObject);

        // 4. make POST request to the given URL
        conn.connect();

        // 5. return response message
        return conn.getResponseMessage()+"";

    }

    private void setPostRequestContent(HttpURLConnection conn, JSONObject jsonObject) throws IOException {

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonObject.toString());
        Log.i(V1_ChallanReportActivity.class.toString(), jsonObject.toString());
        writer.flush();
        writer.close();
        os.close();
    }

    private JSONObject buidJsonObject() throws JSONException {

        JSONObject save_obj = new JSONObject();
        JSONObject data_obj = new JSONObject();
        JSONObject index_obj = new JSONObject();
        JSONArray dtls_arr = new JSONArray();

        save_obj.put("status",true);
        save_obj.put("mode", "save");
        save_obj.put("production_type", 12);
        save_obj.put("UPDATE_ID", 0);

        index_obj.put("company_id", sewingcompanyId);
        index_obj.put("location_id", locationId);
        index_obj.put("production_source", 0);
        index_obj.put("serving_company", sewingcompanyId);
        index_obj.put("floor_id", floorId);
        index_obj.put("sewing_line", lineId);
        index_obj.put("organic", "");
        index_obj.put("user_id", userID);
        index_obj.put("production_date", currentDate);
        index_obj.put("hour", "");
        index_obj.put("remarks", "");
        index_obj.put("txt_system_id", "");
        index_obj.put("mac", "");

        data_obj.put("index", index_obj);

        if(messageBngArray != null && messageBngArray.length > 0) {
            for (int i = 0; i < messageBngArray.length; i++) {

                if (modelArrayList.get(i).getDeleteStatus() == 0) {
                    JSONObject dtls_obj = new JSONObject();
                    dtls_obj.put("cut_no", modelArrayList.get(i).getCutNo());
                    dtls_obj.put("bundle_no", modelArrayList.get(i).getBundleNo());
                    dtls_obj.put("barcode_no", modelArrayList.get(i).getBarcodeNo());
                    dtls_obj.put("order_id", modelArrayList.get(i).getOrderId());
                    dtls_obj.put("item_id", modelArrayList.get(i).getItemId());
                    dtls_obj.put("country_id", modelArrayList.get(i).getCountryId());
                    dtls_obj.put("color_id", modelArrayList.get(i).getColorId());
                    dtls_obj.put("size_id", modelArrayList.get(i).getSizeId());
                    dtls_obj.put("color_size_id", modelArrayList.get(i).getColorSizeId());
                    dtls_obj.put("qnty", modelArrayList.get(i).getQty());
                    dtls_obj.put("is_rescan", modelArrayList.get(i).getIsRescan());
                    dtls_obj.put("color_type_id", modelArrayList.get(i).getColorTypeId());
                    dtls_arr.put(dtls_obj);
                }
            }
        }

        data_obj.put("list_data", dtls_arr);
        save_obj.put("data", data_obj);

        return save_obj;
    }


    private void datepicker(final Button setdate) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        datePickerDialog = new DatePickerDialog(V1_ChallanReportActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        setdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }


    /*Data Picker*/
    private void datePickerMethod(Button sDate) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this, dateListener,Year, Month, Day);
        dpd.show();

    }
    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            month = month + 1;
            date = String.format("%d-%d-%d", dayOfMonth, month, year);
            sDate.setText(date);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(V1_ChallanReportActivity.this, V1_MenuActivity.class);
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