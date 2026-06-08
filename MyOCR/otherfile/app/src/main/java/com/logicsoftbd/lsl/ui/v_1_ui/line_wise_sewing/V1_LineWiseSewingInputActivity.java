package com.logicsoftbd.lsl.ui.v_1_ui.line_wise_sewing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.logicsoftbd.lsl.data.network.v1_model.V1_LineSewingInputModelClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_LineWiseSewingInputModelClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingInputPendingModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingInputPendingModelClass;
import com.logicsoftbd.lsl.data.prefs.SessionManager;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing.V1_CustomDialogPendingList;
import com.logicsoftbd.lsl.ui.v_1_ui.config.V1_ConfigActivity;
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

public class V1_LineWiseSewingInputActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "SewingInputActivity";
    private SessionManager session;

    private Spinner sCompany, sSource, sSewingCompany, sLocation, sFloor, sLineNo;
    private Button sDate, startdate, endDate, sSave, sPendingDaata;
    private EditText sOrganic, barcodeET;
    private ImageButton swingInputScan;

    public static ArrayList<V1_LineSewingInputModelClass> modelArrayList;
    private ArrayList<V1_LineSewingInputModelClass> list;
    public static ArrayList<V1_LineSewingInputModelClass> updatelist;
    private V1_LineSewingInputModelClass inputModelClass;
    private V1_CustomSewingInputAdapter customSewingInputAdapter;
    private V1_LineSewingInputAdapter adapter;

    private AlertDialog.Builder alertDialogBuilder;

    private String date, currentDate, start_date, end_date, barcode, urladdressChk, urladdress, urlString, urlstringbase,urlstring_c_wise_l, urlstring_l_wise_f,
            urlstring_f_wise_l, urlstring_sewing_input, urlPendingData;
    private int Year, Month, Day, userId = 0;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;

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

    //Pending Data
    public final ArrayList<String> pending_job_no = new ArrayList<>();
    public final ArrayList<String> pending_po_no_id = new ArrayList<>();
    public final ArrayList<Long> pending_barcode = new ArrayList<>();
    public final ArrayList<String> pending_bundle_no = new ArrayList<>();
    public final ArrayList<String> pending_cut_no = new ArrayList<>();
    public final ArrayList<Integer> pending_production_qty = new ArrayList<>();

    private String[] arrayCutNo, arrayBarcodetNo, arrayorderNo, arrayItemNo, arraycountryNo, arraycolorNo, arraySizeNo, arraycolorSizeNo,
            arrayBundleNo, arrayYearNo, arrayJob_No, arrayBuyerNo, arrayOrder_No, arrayItemName, arrayCountryNo, arrayColorNo, arraySizeName, arrayqntyNo;

    public String[] companyNameArray;
    public String[] suplierArray;
    public String[] sourceArray;
    public String[] locationArray;
    public String[] floorArray;
    public String[] lineArray;

    public String[] pending_job_no_array;
    public String[] pending_po_no_array;
    public Long[] pending_barcode_array;
    public String[] pending_bundle_array;
    public String[] pending_cut_no_array;
    public Integer[] pending_product_qty_array;


    //sewing Input Field
    private GridView sGridView;

    private int updatedID = 0;
    private String year;
    private String jobNo;
    private String buyer;
    private String orderNo;
    private String gmtsItem;
    private String country;
    private String color;
    private String size;
    private String message;
    private int rescan = 0;
    private int color_type_id = 0;



    private String cut_no;
    private String bundleNo;
    private String barcode_no;
    private String order_id;
    private String item_id;
    private String country_id;
    private String color_id;
    private String size_id;
    private String color_size_id;
    private String qty;
    private String is_rescan;


    /*Id Name*/
    int companyId = 0;
    int sewingcompanyId = 0;
    int sourceId = 0;
    int locationId = 0;
    int floorId = 0;
    int lineId = 0;

    //Save Data
    private ArrayList<String> cut_arrayList;
    private ArrayList<String> bundle_arrayList;
    private ArrayList<String> barcode_arrayList;
    private ArrayList<String> orderId_arrayList;
    private ArrayList<String> itemId_arrayList;
    private ArrayList<String> countryId_arrayList;
    private ArrayList<String> colorId_arrayList;
    private ArrayList<String> sizeId_arrayList;
    private ArrayList<String> colorSizeId_arrayList;


    //Show in List
    private ArrayList<String> year_arrayList;
    private ArrayList<String> job_no_arrayList;
    private ArrayList<String> buyer_no_arrayList;
    private ArrayList<String> order_no_arrayList;
    private ArrayList<String> item_no_arrayList;
    private ArrayList<String> country_no_arrayList;
    private ArrayList<String> color_no_arrayList;
    private ArrayList<String> size_no_arrayList;
    private ArrayList<String> qty_arrayList;

    private int company = 0, location = 0, line = 0, floor = 0;
    private String base_url = "", macAddress;

//    private int savemenu = 0;
//    private int updatemenu = 0;

    //Qc Entry
    private int sewingInput = 4;
    private  String type_entry, barcodeNumber;
    private ProgressDialog pDialog;
    private V1_CustomDialogPendingList customDialogPendingList;
    private DatePickerDialog datePickerDialog;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_line_wise_sewing_input);

//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
        macAddress = _preferences.getString("mac", null);
        base_url = (_preferences.getString("base_url", ""));

        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);

        barcodeNumber = resultS;


        initialization();

        //Toast.makeText(this, String.valueOf(company+location+floor+line+macAddress), Toast.LENGTH_SHORT).show();


    }

    private void initialization() {
        sGridView = findViewById(R.id.sewingInputGrid);
        sCompany = findViewById(R.id.companySpinner);
        sSource = findViewById(R.id.sourceSpinner);
        sSewingCompany = findViewById(R.id.sewingSpinner);
        sLocation = findViewById(R.id.locationSpinnner);
        sFloor = findViewById(R.id.floorSpinner);
        sLineNo = findViewById(R.id.lineSpinner);
        sDate = findViewById(R.id.sewingDateBtn);
        sSave = findViewById(R.id.saveBT);
        sSave.setOnClickListener(this);
//        sPendingDaata = findViewById(R.id.pendingDataBT);
//        sPendingDaata.setOnClickListener(this);
        sDate.setOnClickListener(this);
        sOrganic = findViewById(R.id.organicET);
        swingInputScan = findViewById(R.id.input_scanBtn);
        swingInputScan.setOnClickListener(this);
//        startdate = findViewById(R.id.startdate);
//        startdate.setOnClickListener(this);
//        endDate = findViewById(R.id.enddate);
//        endDate.setOnClickListener(this);

        sendRequestToServer();

        //set Date
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = simpleDateFormat.format(calendar.getTime());
        sDate.setText(currentDate);
//        startdate.setText(currentDate);
//        endDate.setText(currentDate);

        //barcode ET
        barcodeET = findViewById(R.id.barcodenumberText);

        //Show in List
        year_arrayList = new ArrayList<>();
        job_no_arrayList = new ArrayList<>();
        buyer_no_arrayList = new ArrayList<>();
        order_no_arrayList = new ArrayList<>();
        item_no_arrayList = new ArrayList<>();
        country_no_arrayList = new ArrayList<>();
        color_no_arrayList = new ArrayList<>();
        size_no_arrayList = new ArrayList<>();

        //Save Data
        cut_arrayList = new ArrayList<>();
        bundle_arrayList = new ArrayList<>();
        barcode_arrayList = new ArrayList<>();
        orderId_arrayList = new ArrayList<>();
        itemId_arrayList = new ArrayList<>();
        countryId_arrayList = new ArrayList<>();
        colorId_arrayList = new ArrayList<>();
        sizeId_arrayList = new ArrayList<>();
        colorSizeId_arrayList = new ArrayList<>();
        qty_arrayList = new ArrayList<>();



        modelArrayList = new ArrayList<V1_LineSewingInputModelClass>();
        adapter = new V1_LineSewingInputAdapter(getApplicationContext(), R.layout.sewing_input_object_layout, modelArrayList);





//        swingInputScan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(barcode_arrayList.contains(barcodeET.getText().toString()))
//                {
//                    Toast.makeText(SewingInputActivity.this, "Barcode is already scanned", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    barcode = barcodeET.getText().toString();
//                    sewingInputGetDataMethod(barcode);
//                    Toast.makeText(SewingInputActivity.this, Arrays.toString(arrayBarcodetNo), Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });

        sGridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    private void barcodeScanMethod() {
        barcode = barcodeET.getText().toString();

        if(!barcode.isEmpty())
        {
            barcode = barcodeET.getText().toString();
            sewingInputGetDataMethod(barcode);
        }
        else {
            Intent intent = new Intent(this, V1_QRBarcodeScannerActivity.class);
            intent.putExtra("url", urladdress);
            intent.putExtra("qc", "line_wise_sewing_input");
            startActivity(intent);
        }
    }

    private void sendRequestToServer() {
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
                    Toast.makeText(V1_LineWiseSewingInputActivity.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(V1_LineWiseSewingInputActivity.this, "Please Configure Your Tab..", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(V1_LineWiseSewingInputActivity.this, "Please Configure Your Tab..", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        ArrayAdapter<String> adapterLocation = new ArrayAdapter<String>(this, R.layout.sewing_spinner_layout, locationArray);
//        sLocation.setAdapter(adapterLocation);

        /*ArrayAdapter<String> adapterFloor = new ArrayAdapter<String>(this, R.layout.sewing_spinner_layout, floorArray);
        sFloor.setAdapter(adapterFloor);*/

//        ArrayAdapter<String> adapterLine = new ArrayAdapter<String>(this, R.layout.sewing_spinner_layout, lineArray);
//        sLineNo.setAdapter(adapterLine);

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
                        Toast.makeText(V1_LineWiseSewingInputActivity.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
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
        ArrayAdapter<String> adapterLocation = new ArrayAdapter<String>(V1_LineWiseSewingInputActivity.this, R.layout.sewing_spinner_layout, locationArray);
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
                    Toast.makeText(V1_LineWiseSewingInputActivity.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(V1_LineWiseSewingInputActivity.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
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
            if (barcodeNumber != null && type_entry.equals("line_wise_sewing_input"))
            {
                sewingInputGetDataMethod(barcodeNumber);
            }
        }
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
//                        pendingDataShow();

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
            case R.id.saveBT:
                if(sourceId == 1)
                {
                    if(locationId != 0 && floorId != 0 && lineId != 0 && bundleNo != null)
                    {
                        if(bundleNo != null)
                        {
                            postDataToServer_sewing_input();
                        }
                        else {
                            Toast.makeText(this, "Barcode not scanned", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        Toast.makeText(this, "Please fill the credentials", Toast.LENGTH_SHORT).show();
                    }

                }
                else if(sourceId == 3)
                {
                    postDataToServer_sewing_input();
                }
                else
                {
                    Toast.makeText(this, "Please fill the credentials", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.toolbar:
                Intent intent = V1_MenuActivity.getStartIntent(V1_LineWiseSewingInputActivity.this);
                startActivity(intent);
                finish();
        }
    }



    /*Scanning data*/
    private void sewingInputGetDataMethod(String barcode) {
        apiInterface.getLineWiseSewingInputModelClassCall(sewingcompanyId, barcode, 12).enqueue(new Callback<V1_LineWiseSewingInputModelClass>() {
            @Override
            public void onResponse(Call<V1_LineWiseSewingInputModelClass> call, Response<V1_LineWiseSewingInputModelClass> response) {
                hideDialog();
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful())
                {

                    barcodeET.setText("");

                    year = String.valueOf(response.body().getResultset().getYear());
                    jobNo = String.valueOf(response.body().getResultset().getJobNo());
                    buyer = response.body().getResultset().getBuyer();
                    orderNo = response.body().getResultset().getOrderNo();
                    gmtsItem = response.body().getResultset().getItem();
                    country = response.body().getResultset().getCountry();
                    color = response.body().getResultset().getColor();
                    size = response.body().getResultset().getSize();
                    rescan = response.body().getResultset().getIsRescan();
                    color_type_id = response.body().getResultset().getColorTypeId();



                    message = response.body().getResultset().getMessageBng();
                    cut_no = response.body().getResultset().getCutNo();
                    bundleNo = response.body().getResultset().getBundleNo();
                    barcode_no = String.valueOf(response.body().getResultset().getBarcodeNo());
                    order_id = String.valueOf(response.body().getResultset().getOrderId());
                    item_id = String.valueOf(response.body().getResultset().getItemId());
                    country_id = String.valueOf(response.body().getResultset().getCountryId());
                    color_id = String.valueOf(response.body().getResultset().getColorId());
                    size_id = String.valueOf(response.body().getResultset().getSizeId());
                    color_size_id = String.valueOf(response.body().getResultset().getColorSizeId());
                    qty = String.valueOf(response.body().getResultset().getQty());


                    if(message.equals("")){
                        setDataInList();
                        sGridView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(V1_LineWiseSewingInputActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(V1_LineWiseSewingInputActivity.this, "Barcode already scanned Or Invalid Id", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<V1_LineWiseSewingInputModelClass> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(V1_LineWiseSewingInputActivity.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
                else {
                    Toast.makeText(getApplicationContext(), "এই বান্ডেলটি ইতিমধ্যে স্ক্যান হয়েছে/স্ক্যান এর জন্য তৈরী হয়নি , দয়া করে অন্য একটি চেষ্টা করুন ।", Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }
        });
    }


    private void setDataInList() {
        year_arrayList.add(year);
        job_no_arrayList.add(jobNo);
        buyer_no_arrayList.add(buyer);
        order_no_arrayList.add(orderNo);
        item_no_arrayList.add(gmtsItem);
        country_no_arrayList.add(country);
        color_no_arrayList.add(color);
        size_no_arrayList.add(size);



        cut_arrayList.add(cut_no);
        bundle_arrayList.add(bundleNo);
        barcode_arrayList.add(barcode_no);
        orderId_arrayList.add(order_id);
        itemId_arrayList.add(item_id);
        countryId_arrayList.add(country_id);
        colorId_arrayList.add(color_id);
        sizeId_arrayList.add(size_id);
        colorSizeId_arrayList.add(color_size_id);
        qty_arrayList.add(qty);

        //show sewing data
        arrayYearNo = new String[year_arrayList.size()];
        for(int i = 0; i < year_arrayList.size(); i++)
        {
            arrayYearNo[i] = year_arrayList.get(i);
        }

        arrayJob_No = new String[job_no_arrayList.size()];
        for(int i = 0; i < job_no_arrayList.size(); i++)
        {
            arrayJob_No[i] = job_no_arrayList.get(i);
        }

        arrayBuyerNo = new String[buyer_no_arrayList.size()];
        for(int i = 0; i < buyer_no_arrayList.size(); i++)
        {
            arrayBuyerNo[i] = buyer_no_arrayList.get(i);
        }

        arrayOrder_No = new String[order_no_arrayList.size()];
        for(int i = 0; i < order_no_arrayList.size(); i++)
        {
            arrayOrder_No[i] = order_no_arrayList.get(i);
        }

        arrayItemName = new String[item_no_arrayList.size()];
        for(int i = 0; i < item_no_arrayList.size(); i++)
        {
            arrayItemName[i] = item_no_arrayList.get(i);
        }

        arrayCountryNo = new String[country_no_arrayList.size()];
        for(int i = 0; i < country_no_arrayList.size(); i++)
        {
            arrayCountryNo[i] = country_no_arrayList.get(i);
        }

        arraycountryNo = new String[country_no_arrayList.size()];
        for(int i = 0; i < country_no_arrayList.size(); i++)
        {
            arraycountryNo[i] = country_no_arrayList.get(i);
        }

        arrayColorNo = new String[color_no_arrayList.size()];
        for(int i = 0; i < color_no_arrayList.size(); i++)
        {
            arrayColorNo[i] = color_no_arrayList.get(i);
        }

        arraySizeName = new String[size_no_arrayList.size()];
        for(int i = 0; i < size_no_arrayList.size(); i++)
        {
            arraySizeName[i] = size_no_arrayList.get(i);
        }


        //input Sewing data
        arrayCutNo = new String[cut_arrayList.size()];
        for(int i = 0; i < cut_arrayList.size(); i++)
        {
            arrayCutNo[i] = cut_arrayList.get(i);
        }

        arrayBundleNo = new String[bundle_arrayList.size()];
        for(int i = 0; i < bundle_arrayList.size(); i++)
        {
            arrayBundleNo[i] = bundle_arrayList.get(i);
        }

        arrayBarcodetNo = new String[barcode_arrayList.size()];
        for(int i = 0; i < barcode_arrayList.size(); i++)
        {
            arrayBarcodetNo[i] = barcode_arrayList.get(i);
        }

        arrayorderNo = new String[orderId_arrayList.size()];
        for(int i = 0; i < orderId_arrayList.size(); i++)
        {
            arrayorderNo[i] = orderId_arrayList.get(i);
        }

        arrayItemNo = new String[itemId_arrayList.size()];
        for(int i = 0; i < itemId_arrayList.size(); i++)
        {
            arrayItemNo[i] = itemId_arrayList.get(i);
        }

        arraycountryNo = new String[countryId_arrayList.size()];
        for(int i = 0; i < countryId_arrayList.size(); i++)
        {
            arraycountryNo[i] = countryId_arrayList.get(i);
        }

        arraycolorNo = new String[colorId_arrayList.size()];
        for(int i = 0; i < colorId_arrayList.size(); i++)
        {
            arraycolorNo[i] = colorId_arrayList.get(i);
        }

        arraySizeNo = new String[sizeId_arrayList.size()];
        for(int i = 0; i < sizeId_arrayList.size(); i++)
        {
            arraySizeNo[i] = sizeId_arrayList.get(i);
        }

        arraycolorSizeNo = new String[colorSizeId_arrayList.size()];
        for(int i = 0; i < colorSizeId_arrayList.size(); i++)
        {
            arraycolorSizeNo[i] = colorSizeId_arrayList.get(i);
        }

        arrayqntyNo = new String[qty_arrayList.size()];
        for(int i = 0; i < qty_arrayList.size(); i++)
        {
            arrayqntyNo[i] = qty_arrayList.get(i);
        }


        inputModelClass = new V1_LineSewingInputModelClass();

        for(int i = 0; i < arrayCutNo.length; i++)
        {

            //input Data
            inputModelClass.setCut_no(arrayCutNo[i]);
            inputModelClass.setBundle_no(arrayBundleNo[i]);
            inputModelClass.setBarcode_no(arrayBarcodetNo[i]);
            inputModelClass.setOrder_id(Integer.parseInt(arrayorderNo[i]));
            inputModelClass.setItem_id(Integer.parseInt(arrayItemNo[i]));
            inputModelClass.setCountry_id(Integer.parseInt(arraycountryNo[i]));
            inputModelClass.setColor_id(Integer.parseInt(arraycolorNo[i]));
            inputModelClass.setSize_id(Integer.parseInt(arraySizeNo[i]));
            inputModelClass.setColor_size(Integer.parseInt(arraycolorSizeNo[i]));


            //show Data
            inputModelClass.setYearNo(arrayYearNo[i]);
            inputModelClass.setJobNo(arrayJob_No[i]);
            inputModelClass.setBuyer(arrayBuyerNo[i]);
            inputModelClass.setOrderNo(arrayOrder_No[i]);
            inputModelClass.setItemNo((arrayItemName[i]));
            inputModelClass.setCountry(arrayCountryNo[i]);
            inputModelClass.setColorNo(arrayColorNo[i]);
            inputModelClass.setSizeNo(arraySizeName[i]);
            inputModelClass.setQuantity(Integer.parseInt(String.valueOf(arrayqntyNo[i])));
        }
        modelArrayList.add(inputModelClass);
        sGridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }



    /*Post Data*/
    private void postDataToServer_sewing_input() {
        if(checkNetworkConnection())
        {
            //new HTTPAsyncTask().execute(String.format("http://%s/logic-api/index.php/api/android/save_update_sewing_input", urladdress));
            new V1_LineWiseSewingInputActivity.HTTPAsyncTask().execute(String.format("%s"+"logic-api/index.php/api/android/save_update_sewing_input_output", urladdress));
        }
        else
            Toast.makeText(this, "Not Connected!", Toast.LENGTH_SHORT).show();
        checkNetworkConnection();
    }

    private boolean checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isConnected = false;
        if(networkInfo != null && (isConnected = networkInfo.isConnected()))
        {

        }else {
            Toast.makeText(this, "Not connected", Toast.LENGTH_SHORT).show();
        }
        return isConnected;
    }

    private String httpPost(String myUrl) throws IOException, JSONException {
        String result = "";
        URL url = new URL(myUrl);

        // 1. create HttpURLConnection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        // 2. build JSON object
        JSONObject jsonObject = buildJsonObject();

        // 3. add JSON content to POST request body
        setPostRequestContent(conn, jsonObject);

        // 4. make POST request to the given URl
        conn.connect();

        // 5. return response message
        return conn.getResponseMessage()+"";
    }

    private class  HTTPAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                try {
                    return httpPost(urls[0]);
                }catch (JSONException e)
                {
                    e.printStackTrace();
                    return "Failed";
                }
            }catch (IOException e)
            {
                return "Unable to retrives web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            if(result.equals("OK")) {
                Toast.makeText(V1_LineWiseSewingInputActivity.this, "সফলভাবে সংরক্ষিত", Toast.LENGTH_SHORT).show();
                if(arrayCutNo != null && arrayCutNo.length > 0)
                {
                    Intent intent = new Intent(V1_LineWiseSewingInputActivity.this, V1_LineWiseSewingInputActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("url", urladdress);
                    startActivity(intent);
                }
            }

            else {
                showMessageResult(result);

            }
        }
    }

    private void showMessageResult(String result) {
        Toast.makeText(this, String.valueOf(result), Toast.LENGTH_SHORT).show();
//        alertDialogBuilder.setTitle("Save Update");
//
//        alertDialogBuilder.setMessage(result);
//        alertDialogBuilder.setCancelable(false);
////        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface arg0, int arg1) {
////                Intent intent = new Intent(GreyFabricActivity.this, QRBarcodeScannerActivity.class);
////                intent.putExtra("userId", userId);
////                intent.putExtra("url", urladdress);
////                startActivity(intent);
////
////            }
////        });
//        alertDialogBuilder.setNegativeButton("Try Again", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
////                Intent intent = new Intent(GreyFabricActivity.this, QRBarcodeScannerActivity.class);
////                intent.putExtra("userId", userId);
////                intent.putExtra("url", urladdress);
////                startActivity(intent);
//            }
//        });
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
    }

    private void setPostRequestContent(HttpURLConnection conn, JSONObject jsonObject) throws IOException{
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonObject.toString());
        writer.flush();
        writer.close();
        os.close();
    }

    private JSONObject buildJsonObject() throws JSONException{

        JSONObject save_obj = new JSONObject();
        JSONObject index_obj = new JSONObject();
        JSONObject data_obj = new JSONObject();
        JSONArray dtls_arr = new JSONArray();

        save_obj.put("status",true);
        save_obj.put("mode", "save");
        save_obj.put("production_type", 12);
        save_obj.put("UPDATE_ID", updatedID);

        index_obj.put("company_id", companyId);
        index_obj.put("location_id", locationId);
        index_obj.put("production_source", sourceId);
        index_obj.put("serving_company", sewingcompanyId);
        index_obj.put("floor_id", floorId);
        index_obj.put("sewing_line", lineId);
        index_obj.put("organic", sOrganic.getText().toString());
        index_obj.put("user_id", userId);
        index_obj.put("production_date", sDate.getText().toString());
        index_obj.put("hour", "");
        index_obj.put("remarks", "");
        index_obj.put("txt_system_id", "");
        index_obj.put("mac", macAddress);
        //index_obj.put("is_rescan", rescan);

        data_obj.put("index", index_obj);

        for(int i = 0; i < modelArrayList.size(); i++)
        {
            JSONObject dtls_obj = new JSONObject();
            dtls_obj.put("cut_no", modelArrayList.get(i).getCut_no());
            dtls_obj.put("bundle_no", modelArrayList.get(i).getBundle_no());
            dtls_obj.put("barcode_no", modelArrayList.get(i).getBarcode_no());
            dtls_obj.put("order_id", modelArrayList.get(i).getOrder_id());
            dtls_obj.put("item_id", modelArrayList.get(i).getItem_id());
            dtls_obj.put("country_id", modelArrayList.get(i).getCountry_id());
            dtls_obj.put("color_id", modelArrayList.get(i).getColor_id());
            dtls_obj.put("size_id", modelArrayList.get(i).getSize_id());
            dtls_obj.put("color_size_id", modelArrayList.get(i).getColor_size());
            dtls_obj.put("qnty", modelArrayList.get(i).getQuantity());
            dtls_obj.put("is_rescan", rescan);
            dtls_obj.put("color_type_id", color_type_id);
            dtls_arr.put(dtls_obj);
        }

        data_obj.put("list_data", dtls_arr);
        save_obj.put("data", data_obj);
        Log.d(TAG, "buildJsonObject: "+save_obj.toString());

        return save_obj;
    }


    //Pending Data
    private void pendingDataShow(){
        start_date = startdate.getText().toString();
        end_date = endDate.getText().toString();
        apiInterface.getSewingInputPendingModelClassCall(company, location, floor, line, start_date, end_date).enqueue(new Callback<V1_SewingInputPendingModelClass>() {
            @Override
            public void onResponse(Call<V1_SewingInputPendingModelClass> call, Response<V1_SewingInputPendingModelClass> response) {
                if(response.isSuccessful()){

                    if(!pending_job_no.isEmpty()){
                        pending_job_no.clear();
                        pending_barcode.clear();
                        pending_bundle_no.clear();
                        pending_cut_no.clear();
                        pending_po_no_id.clear();
                        pending_production_qty.clear();
                    }


                    V1_SewingInputPendingModelClass.SewingOutputPending job_No;
                    List<V1_SewingInputPendingModelClass.SewingOutputPending> job_List = response.body().getData().getSewingOutputPending();
                    for(V1_SewingInputPendingModelClass.SewingOutputPending d : job_List)
                    {
                        job_No = d;
                        final V1_SewingInputPendingModelClass.SewingOutputPending finalName = job_No;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pending_job_no.add(finalName.getjOBNO());
                            }
                        });
                    }

                    V1_SewingInputPendingModelClass.SewingOutputPending po_No;
                    List<V1_SewingInputPendingModelClass.SewingOutputPending> break_List = response.body().getData().getSewingOutputPending();
                    for(V1_SewingInputPendingModelClass.SewingOutputPending d : break_List)
                    {
                        po_No = d;
                        final V1_SewingInputPendingModelClass.SewingOutputPending finalName = po_No;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pending_po_no_id.add(finalName.getpONUMBER());
                            }
                        });
                    }

                    V1_SewingInputPendingModelClass.SewingOutputPending barcode_No;
                    List<V1_SewingInputPendingModelClass.SewingOutputPending> barcode_List = response.body().getData().getSewingOutputPending();
                    for(V1_SewingInputPendingModelClass.SewingOutputPending d : barcode_List)
                    {
                        barcode_No = d;
                        final V1_SewingInputPendingModelClass.SewingOutputPending finalName = barcode_No;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pending_barcode.add(finalName.getBARCODENO());
                            }
                        });
                    }

                    V1_SewingInputPendingModelClass.SewingOutputPending bundle_no;
                    List<V1_SewingInputPendingModelClass.SewingOutputPending> bundle_List = response.body().getData().getSewingOutputPending();
                    for(V1_SewingInputPendingModelClass.SewingOutputPending d : bundle_List)
                    {
                        bundle_no = d;
                        final V1_SewingInputPendingModelClass.SewingOutputPending finalName = bundle_no;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pending_bundle_no.add(finalName.getBUNDLENO());
                            }
                        });
                    }

                    V1_SewingInputPendingModelClass.SewingOutputPending pen_cut_no;
                    List<V1_SewingInputPendingModelClass.SewingOutputPending> cut_List = response.body().getData().getSewingOutputPending();
                    for(V1_SewingInputPendingModelClass.SewingOutputPending d : cut_List)
                    {
                        pen_cut_no = d;
                        final V1_SewingInputPendingModelClass.SewingOutputPending finalName = pen_cut_no;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pending_cut_no.add(finalName.getCUTNO());
                            }
                        });
                    }

                    V1_SewingInputPendingModelClass.SewingOutputPending pro_qty_no;
                    List<V1_SewingInputPendingModelClass.SewingOutputPending> pro_qty_List = response.body().getData().getSewingOutputPending();
                    for(V1_SewingInputPendingModelClass.SewingOutputPending d : pro_qty_List)
                    {
                        pro_qty_no = d;
                        final V1_SewingInputPendingModelClass.SewingOutputPending finalName = pro_qty_no;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pending_production_qty.add(Integer.valueOf(finalName.getPRODUCTIONQNTY()));
                            }
                        });
                    }

//                    pendingModelArrayList = getPendingModel();

//                    SewingInputPendingAdapter pendingAdapter = new SewingInputPendingAdapter(getApplicationContext(), R.layout.pending_data_item, pendingModelArrayList);

//                    customDialogPendingList = new CustomDialogPendingList(LineWiseSewingInputActivity.this, pendingAdapter);

//                    customDialogPendingList.show();

                }
            }

            @Override
            public void onFailure(Call<V1_SewingInputPendingModelClass> call, Throwable t) {
                Toast.makeText(V1_LineWiseSewingInputActivity.this, "Data not Found !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<V1_SewingInputPendingModel> getPendingModel() {


        ArrayList<V1_SewingInputPendingModel> pendingModels = new ArrayList<>();

        pending_job_no_array = new String[pending_job_no.size()];
        for(int i = 0; i < pending_job_no.size(); i++){
            pending_job_no_array[i] = pending_job_no.get(i);
        }

        pending_po_no_array = new String[pending_po_no_id.size()];
        for(int i = 0; i < pending_po_no_id.size(); i++){
            pending_po_no_array[i] = pending_po_no_id.get(i);
        }

        pending_barcode_array = new Long[pending_barcode.size()];
        for(int i = 0; i < pending_barcode.size(); i++){
            pending_barcode_array[i] = pending_barcode.get(i);
        }

        pending_bundle_array = new String[pending_bundle_no.size()];
        for(int i = 0; i < pending_bundle_no.size(); i++){
            pending_bundle_array[i] = pending_bundle_no.get(i);
        }

        pending_cut_no_array = new String[pending_cut_no.size()];
        for(int i = 0; i < pending_cut_no.size(); i++){
            pending_cut_no_array[i] = pending_cut_no.get(i);
        }

        pending_product_qty_array = new Integer[pending_production_qty.size()];
        for(int i = 0; i < pending_production_qty.size(); i++){
            pending_product_qty_array[i] = pending_production_qty.get(i);
        }

        for(int i = 0; i < pending_po_no_array.length; i++){
            V1_SewingInputPendingModel sewingInputPendingModel = new V1_SewingInputPendingModel();
            sewingInputPendingModel.setJOB_NO(String.valueOf(pending_job_no.get(i)));
            sewingInputPendingModel.setPO_NUMBER(String.valueOf(pending_po_no_array[i]));
            sewingInputPendingModel.setBARCODE_NO(String.valueOf(pending_barcode_array[i]));
            sewingInputPendingModel.setBUNDLE_NO(String.valueOf(pending_bundle_array[i]));
            sewingInputPendingModel.setCUT_NO(String.valueOf(pending_cut_no_array[i]));
            sewingInputPendingModel.setPRODUCTION_QNTY(String.valueOf(pending_product_qty_array[i]));
            pendingModels.add(sewingInputPendingModel);
        }
        return pendingModels;
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

    private void datepicker(final Button setdate) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        datePickerDialog = new DatePickerDialog(V1_LineWiseSewingInputActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        setdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                //logoutUser();
            case R.id.config:
                startActivity(new Intent(V1_LineWiseSewingInputActivity.this, V1_ConfigActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(V1_LineWiseSewingInputActivity.this, V1_MenuActivity.class);
//        startActivity(intent);
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
