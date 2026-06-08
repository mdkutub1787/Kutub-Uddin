package com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_AlterSewingDefectResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_AlterSewingOutputOperationItemModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseCompanyToShiftClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseSewingOutputClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ConfigSewingOperationModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DataSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_RejectSewingDefectResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_RejectSewingOutputOperationItemModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingAlterModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseSewingOperationResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingOutputModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingOutputModelClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingRejectModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingSpotModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SpotSewingDefectResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SpotSewingOutputOperationItemModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_TeamResponseModel;
import com.logicsoftbd.lsl.data.prefs.SessionManager;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.v_1_ui.menu.V1_MenuActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_ScannerActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.style_wise_sewing.V1_SewingOutputAlterDefectRecyclerAdapter;
import com.logicsoftbd.lsl.ui.v_1_ui.style_wise_sewing.V1_SewingOutputRejectDefectRecyclerAdapter;
import com.logicsoftbd.lsl.ui.v_1_ui.style_wise_sewing.V1_SewingOutputSpotDefectRecyclerAdapter;
import com.logicsoftbd.lsl.utils.ApiUtils;
import com.logicsoftbd.lsl.utils.DialogHelper;
import com.logicsoftbd.lsl.utils.NetworkStatus;
import com.logicsoftbd.lsl.utils.VerticalSpacingItemDecorator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class V1_SewingOutputActivity extends AppCompatActivity implements View.OnClickListener,
        V1_SewingOutputRecyclerViewAdapter.OnRejectHeadListener,
        V1_SewingOutputRecyclerViewAdapter.OnAlterHeadListener,
        V1_SewingOutputRecyclerViewAdapter.OnSpotHeadListener,
        V1_BundleWiseSewingOperationRecyclerAdapter.OnOperationSelectListener,
        V1_SewingOutputAlterDefectRecyclerAdapter.OnAlterDefectSelectListener,
        V1_SewingOutputSpotDefectRecyclerAdapter.OnSpotDefectSelectListener,
        V1_SewingOutputRejectDefectRecyclerAdapter.OnRejectDefectSelectListener {
    private static final String TAG = "V1_SewingOutputActivity";
    private SessionManager session;
    private Spinner sCompany, sSource, sSewingCompany, sLocation, sFloor, sLineNo, sSfhift, sTeamSpinner;
    private Button sDate, sTime, sSave, swingInputScan, refreshBT, _backFromOperationButton, _backOperationFromDefectButton, _backOutputFromDefectButton;
    private EditText barcodeET, sOrganic, sRemark;
    private TextView _companyNameTV, _locationTV, _floorTV, _lineTV;
    public static ArrayList<V1_SewingOutputModelClass> modelArrayList;
    private V1_SewingOutputModelClass inputModelClass;
    private V1_SewingOutputAdapter adapter;

    private String[] arrayCutNo, arrayBarcodetNo, arrayorderNo, arrayItemNo, arraycountryNo, arraycolorNo, arraySizeNo, arraycolorSizeNo;
    //Show In grid
    private String[] arrayBundleNo, arrayYearNo, arrayJob_No, arrayBuyerNo, arrayOrder_No, arrayItemName, arrayCountryNo, arrayColorNo, arraySizeName, arrayqntyNo, replace_field_disable_No;
    private AlertDialog.Builder alertDialogBuilder;
    private int Year, Month, Day, Hour, Minute;
    private String currentDate = "", companyName = "", locationName = "", floorName = "", lineName = "", _currentDate = "", _currentTime = "", defectType="";
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private SimpleDateFormat simpleTimeFormat;
    private CheckBox organicCheckBox;
    private CardView _outPutListCard, _operationListCard, _defectListCard, _sewingCompanyInfoCard, _sewingBarcodeScanCard;

    private String base_url, userId, date, currentTime, barcode, urladdressChk, urladdress, urlString, year, jobNo, buyer, orderNo, gmtsItem, country, color, size,
            message, cut_no, bundleNo, barcode_no, order_id, item_id, country_id, color_id, size_id, color_size_id, qty, is_rescan, replace_field_disable,
            type_entry, macAddress, barcodeNumber, reject_defect = "", alter_defect = "", spot_defect= "";
    public int isOrganic = 0, updatedID = 0, rescan = 0, color_type_id = 0, sewing_input_line = 0, companyId = 0, shiftId = 0, teamId = 0, sewingcompanyId = 0,
            sourceId = 1, locationId = 0, floorId = 0, lineId = 0, company = 0,shift = 0, location = 0, line = 0, floor = 0;
    private boolean isSaveButtonClicked = false;
    //int companyPosition = 0;
    public final ArrayList<String> companyNameList = new ArrayList<>();
    public final ArrayList<Integer> companyNameId = new ArrayList<>();
    public final ArrayList<String> shiftNameList = new ArrayList<>();
    public final ArrayList<Integer> shiftNameId = new ArrayList<>();
    public final ArrayList<Integer> locationNameId = new ArrayList<>();
    public final ArrayList<Integer> floorNameId = new ArrayList<>();
    public final ArrayList<Integer> lineNameId = new ArrayList<>();

    public final ArrayList<String> teamNameList = new ArrayList<>();
    public final ArrayList<Integer> teamListId = new ArrayList<>();

    public String[] companyNameArray;
    private RecyclerView sewingOutPutRecyclerView, operationRecyclerView, defectRecyclerView;

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
    private ArrayList<String> replace_field_disable_arrayList;

    private ProgressDialog pDialog;
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;
    private Toolbar mToolbar;

    private V1_SewingOutputRecyclerViewAdapter sewingOutputRecyclerViewAdapter;
    private V1_BundleWiseSewingOperationRecyclerAdapter bundleWiseSewingOperationRecyclerAdapter;
    private V1_SewingOutputAlterDefectRecyclerAdapter sewingOutputAlterDefectRecyclerAdapter;
    private V1_SewingOutputSpotDefectRecyclerAdapter sewingOutputSpotDefectRecyclerAdapter;
    private V1_SewingOutputRejectDefectRecyclerAdapter sewingOutputRejectDefectRecyclerAdapter;
    private V1_RejectPopUpRecyclerAdapter rejectPopUpRecyclerAdapter;
    private V1_AlterPopUpRecyclerAdapter alterPopUpRecyclerAdapter;
    private V1_SpotPopUpRecyclerAdapter spotPopUpRecyclerAdapter;
    private V1_SewingOutputDBAdapter sewingOutputDBAdapter;
    private ArrayList<V1_SewingOutputModel> sewingOutputModels;
    private List<V1_RejectSewingDefectResponse.DefectType> rejectPopUpModelClasses;
    private List<V1_AlterSewingDefectResponse.DefectType> alterPopUPModelClasses;
    private List<V1_SpotSewingDefectResponse.DefectType> spotPopUPModelClasses;

    public static ArrayList<V1_SewingAlterModel> sewingAlterModels = new ArrayList<>();
    public static ArrayList<V1_SewingSpotModel> sewingSpotModels = new ArrayList<>();
    public static ArrayList<V1_SewingRejectModel> sewingRejectModels = new ArrayList<>();
    public static ArrayList<V1_RejectSewingOutputOperationItemModel> rejectSewingOutputOperationItemModels = new ArrayList<>();
    public static ArrayList<V1_AlterSewingOutputOperationItemModel> alterSewingOutputOperationItemModels = new ArrayList<>();
    public static ArrayList<V1_SpotSewingOutputOperationItemModel> spotSewingOutputOperationItemModels = new ArrayList<>();
    public ArrayList<V1_ConfigSewingOperationModel> configSewingOperationModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_sewing_output);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());

        Intent intent = getIntent();

        String resultS = intent.getStringExtra("result");
        urladdressChk = intent.getStringExtra("url");
        type_entry = intent.getStringExtra("qc");

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        company = (_preferences.getInt("company", 0));
        location = (_preferences.getInt("location", 0));
        line = (_preferences.getInt("line", 0));
        floor = (_preferences.getInt("floor", 0));
        macAddress = _preferences.getString("mac", null);
        userId = _preferences.getString("login_userid", "");
        base_url = (_preferences.getString("base_url", ""));
        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);

        barcodeNumber = resultS;

        initialization();



        if (barcodeNumber != null && type_entry.equals("bundle_wise_sewing_output"))
        {
            sewingInputGetDataMethod(barcodeNumber);
        }

    }

    private void initialization() {
        sewingOutPutRecyclerView = findViewById(R.id.sewingInputGrid);
        operationRecyclerView = findViewById(R.id.operationRecyclerView);
        defectRecyclerView = findViewById(R.id.defectRecyclerView);
        _companyNameTV = findViewById(R.id.companyNameTV);
        _locationTV = findViewById(R.id.locationTV);
        _floorTV = findViewById(R.id.floorTV);
        _lineTV = findViewById(R.id.lineTV);
        organicCheckBox = findViewById(R.id.organicCheckBox);
        organicCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                isOrganic = 1;
            }
        });

        sewingOutPutRecyclerView.setBackgroundColor(Color.argb(255, 255, 255, 255));

        getDefaultData();

        sCompany = findViewById(R.id.companySpinner);
        sSfhift = findViewById(R.id.shiftSpinner);
        sTeamSpinner = findViewById(R.id.teamSpinner);
        sSource = findViewById(R.id.sourceSpinner);
        sSewingCompany = findViewById(R.id.sewingSpinner);
        sLocation = findViewById(R.id.locationSpinnner);
        sFloor = findViewById(R.id.floorSpinner);
        sLineNo = findViewById(R.id.lineSpinner);
//        sDate = findViewById(R.id.sewingDateBtn);
//        sTime = findViewById(R.id.sewingTimeBtn);
        sSave = findViewById(R.id.saveBT);
        sSave.setOnClickListener(this);
        refreshBT = findViewById(R.id.refreshBT);
        refreshBT.setOnClickListener(this);
//        sDate.setOnClickListener(this);
//        sTime.setOnClickListener(this);
        sOrganic = findViewById(R.id.organicET);
        swingInputScan = findViewById(R.id.input_scanBtn);
        swingInputScan.setOnClickListener(this);

        _backFromOperationButton = findViewById(R.id.backFromOperationButton);
        _backFromOperationButton.setOnClickListener(this);
        _backOperationFromDefectButton = findViewById(R.id.backOperationFromDefectButton);
        _backOperationFromDefectButton.setOnClickListener(this);
        _backOutputFromDefectButton = findViewById(R.id.backOutputFromDefectButton);
        _backOutputFromDefectButton.setOnClickListener(this);

        _outPutListCard  = findViewById(R.id.outPutListCard);
        _operationListCard = findViewById(R.id.operationListCard);
        _defectListCard = findViewById(R.id.defectListCard);
        _sewingCompanyInfoCard = findViewById(R.id.sewingCompanyInfoCard);
        _sewingBarcodeScanCard = findViewById(R.id.sewingBarcodeScanCard);

        companyWiseShift();
        companyWiseTeam();

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = simpleDateFormat.format(calendar.getTime());
//        sDate.setText(currentDate);

        //set Time
        simpleTimeFormat = new SimpleDateFormat("HH-mm");
        currentTime = simpleTimeFormat.format(calendar.getTime());
//        sTime.setText(currentTime);

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
        replace_field_disable_arrayList = new ArrayList<>();

//        rejectPopUpModelClasses = new ArrayList<>();
//        RejectPopUpModelClass rejectPopUpModelClass = new RejectPopUpModelClass();
//        for(int i=0; i<10; i++){
//            rejectPopUpModelClass.setDefectName("Defect Name");
//            rejectPopUpModelClasses.add(i, rejectPopUpModelClasses);
//        }

//        rejectPopUpModelClasses.add(rejectPopUpModelClass);

        sewingOutputDBAdapter = new V1_SewingOutputDBAdapter(this);
        sewingOutputModels = new V1_SewingOutputDBAdapter(this).getSewingOfflineData();

        if (NetworkStatus.getInstance(this).isOnline()) {
//            sendRequestToServer();
        } else {
            if (barcodeNumber != null)
            {
                {
                    V1_SewingOutputModel l = new V1_SewingOutputModel();
                    l.setDate(currentDate);
                    l.setBarcode(barcodeNumber);
                    if(new V1_SewingOutputDBAdapter(V1_SewingOutputActivity.this).saveLoginData(l))
                    {
                        Toast.makeText(this, "Offline save data", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        modelArrayList = new ArrayList<>();
        adapter = new V1_SewingOutputAdapter(getApplicationContext(), R.layout.sewing_output_object_layout, modelArrayList);

        initRecyclerView();

        if(!checkDefectIsSelected()){
//            requestForSewingOperation();
        }

        if(sewingAlterModels.size() <= 0){
            alterDefectNameRequest("3");
        }
        if(sewingSpotModels.size() <= 0){
            spotDefectNameRequest("4");
        }
        if(sewingRejectModels.size() <= 0){
            rejectDefectNameRequest("2");
        }
    }

    private void companyWiseTeam() {
        showDialog();
        apiInterface.getTeamResponseModelCall(String.valueOf(companyId), String.valueOf(locationId)).enqueue(new Callback<V1_TeamResponseModel>() {
            @Override
            public void onResponse(Call<V1_TeamResponseModel> call, Response<V1_TeamResponseModel> response) {
                hideDialog();
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful() && response.body().getData() != null && response.body().getData().size() > 0){
                    teamNameList.clear();
                    teamListId.clear();
                    for(int i=0; i<response.body().getData().size(); i++){
                        teamNameList.add(response.body().getData().get(i).getTeamName());
                        teamListId.add(response.body().getData().get(i).getId());
                    }
                    teamNameList.add(0, "-Select-");
                    teamListId.add(0, 0);

                    setUpTeamSpinner();
                }else {
//                    DialogHelper.showWarningDialog(V1_SewingOutputActivity.this, "Message", "Team Information not found.");
                }
            }

            @Override
            public void onFailure(Call<V1_TeamResponseModel> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                DialogHelper.showErrorDialog(V1_SewingOutputActivity.this, "Message", "Something went wrong.");
            }
        });
    }

    private void setUpTeamSpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.custome_spinner_dropdown, teamNameList);
        sTeamSpinner.setAdapter(spinnerArrayAdapter);

        sTeamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                teamId = teamListId.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean checkDefectIsSelected() {
        boolean defectStatus = false;
        for(int i=0; i<rejectSewingOutputOperationItemModels.size(); i++){
            if(rejectSewingOutputOperationItemModels.get(i).getStatus() == 1){
                defectStatus = true;
                break;
            }
        }
        return defectStatus;
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

            sewingcompanyId = companyId;
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        sewingOutPutRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        sewingOutPutRecyclerView.addItemDecoration(itemDecorator);
        sewingOutputRecyclerViewAdapter = new V1_SewingOutputRecyclerViewAdapter(modelArrayList, this, this, this, getApplicationContext());
        sewingOutPutRecyclerView.setAdapter(sewingOutputRecyclerViewAdapter);
    }

    private void companyWiseShift() {
        apiInterface.getBundleWiseCompanyToShiftClassCall().enqueue(new Callback<V1_BundleWiseCompanyToShiftClass>() {
            @Override
            public void onResponse(Call<V1_BundleWiseCompanyToShiftClass> call, Response<V1_BundleWiseCompanyToShiftClass> response) {
                hideDialog();
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful() && response.body().getData() != null && response.body().getData().size() > 0){
                    shiftNameList.clear();
                    shiftNameId.clear();
                    for(int i=0; i<response.body().getData().size(); i++){
                        shiftNameList.add(response.body().getData().get(i).getShift());
                        shiftNameId.add(response.body().getData().get(i).getId());
                    }
                    shiftNameList.add(0, "-Select-");
                    shiftNameId.add(0, 0);

                    setAdapterData_Shift();
                }else {
                    DialogHelper.showWarningDialog(V1_SewingOutputActivity.this, "Message", "Shift Information not found.");
                }
            }

            @Override
            public void onFailure(Call<V1_BundleWiseCompanyToShiftClass> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                DialogHelper.showErrorDialog(V1_SewingOutputActivity.this, "Message", "Something went wrong.");
            }
        });
    }

    private void setAdapterData_Shift() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.custome_spinner_dropdown, shiftNameList);
        sSfhift.setAdapter(spinnerArrayAdapter);

        if(shiftNameId.size() > 0) {
            sSfhift.setSelection(1);
            shiftId = shiftNameId.get(1);
        }

        sSfhift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                int locationPosition = position;
//                if(sewingcompanyId == company)
//                {
//                    sSfhift.setSelection(shiftNameId.indexOf(position));
////                   int SHIFT_ID = shiftId;
//
//                }
                shiftId = shiftNameId.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.input_scanBtn:
                hideSoftKeyboard();
                barcodeScanMethod();
                break;
            case R.id.sewingDateBtn:
                datePickerMethod();
                break;
            case R.id.sewingTimeBtn:
                timePickerMethor();
                break;
            case R.id.saveBT:
                if(locationId != 0 && floorId != 0 && lineId != 0&& shiftId !=0)
                {
                    reject_defect = "";
                    alter_defect = "";
                    spot_defect = "";

                    if(modelArrayList.get(0).getAlter() <= 0){
                        for(int i=0; i<sewingAlterModels.size(); i++){
                            sewingAlterModels.get(i).setDefectCount("0");
                        }
                        if(sewingOutputAlterDefectRecyclerAdapter != null)
                            sewingOutputAlterDefectRecyclerAdapter.notifyDataSetChanged();
                    }

                    if(modelArrayList.get(0).getSpot() <= 0){
                        for(int i=0; i<sewingSpotModels.size(); i++){
                            sewingSpotModels.get(i).setDefectCount("0");
                        }
                        if(sewingOutputSpotDefectRecyclerAdapter != null)
                            sewingOutputSpotDefectRecyclerAdapter.notifyDataSetChanged();
                    }

                    if(modelArrayList.get(0).getReject() <= 0){
                        for(int i=0; i<sewingRejectModels.size(); i++){
                            sewingRejectModels.get(i).setDefectCount("0");
                        }
                        if(sewingOutputRejectDefectRecyclerAdapter != null)
                            sewingOutputRejectDefectRecyclerAdapter.notifyDataSetChanged();
                    }

                    for(int i = 0; i < sewingRejectModels.size(); i++)
                    {
                        if(sewingRejectModels.get(i).getDefectCount() != null && !sewingRejectModels.get(i).getDefectCount().equals("") && !sewingRejectModels.get(i).getDefectCount().equals("0")){
                            reject_defect += sewingRejectModels.get(i).getId()+"*"+sewingRejectModels.get(i).getDefectCount()+"__";
                        }
                    }
                    for(int i = 0; i < sewingAlterModels.size(); i++)
                    {
                        if(sewingAlterModels.get(i).getDefectCount() != null && !sewingAlterModels.get(i).getDefectCount().equals("") && !sewingAlterModels.get(i).getDefectCount().equals("0")){
                            alter_defect += sewingAlterModels.get(i).getId()+"*"+ sewingAlterModels.get(i).getDefectCount()+"__";
                        }
                    }
                    for(int i = 0; i < sewingSpotModels.size(); i++)
                    {
                        if(sewingSpotModels.get(i).getDefectCount() != null && !sewingSpotModels.get(i).getDefectCount().equals("") && !sewingSpotModels.get(i).getDefectCount().equals("0")){
                            spot_defect += sewingSpotModels.get(i).getId()+"*"+sewingSpotModels.get(i).getDefectCount()+"__";
                        }
                    }

                    if(modelArrayList.get(0).getReject() > 0 && reject_defect.equals("")){
                        showAlertMessage("Reject defect not selected.", 0);
                    }else if(modelArrayList.get(0).getAlter() > 0 && alter_defect.equals("")){
                        showAlertMessage("Alter defect not selected.", 0);
                    }else if(modelArrayList.get(0).getSpot() > 0 && spot_defect.equals("")){
                        showAlertMessage( "Spot defect not selected.", 0);
                    }else if(modelArrayList.get(0).getAlter() > 0 && !countTotalSelectedAlterDefect(sewingAlterModels)){
                        showAlertMessage( "Selected outside alter defect can't less from inside alter defect.", 0);
                    }else if(modelArrayList.get(0).getSpot() > 0 && !countTotalSelectedSpotDefect(sewingSpotModels)){
                        showAlertMessage( "Selected outside spot defect can't less from inside spot defect.", 0);
                    } else if(modelArrayList.get(0).getReject() > 0 && !countTotalSelectedRejectDefect(sewingRejectModels)){
                        showAlertMessage( "Selected outside reject defect can't less from inside reject defect.", 0);
                    }else{
                        if(bundleNo != null) {
                            try {
                                if (!isSaveButtonClicked) {
                                    isSaveButtonClicked = true;
                                    postDataToServer_sewing_input();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            showAlertMessage("Barcode not scanned", 0);
                        }
                    }

                } else if(qty=="0") {
                    showAlertMessage("Quantity doesn't zero",0);
                } else {
                    showAlertMessage("Please fill the shift name", 0);
                    ((TextView)sSfhift.getSelectedView()).setError("Please fill the shift & Team name");
                }
                break;
            case R.id.refreshBT:
                initialization();
                _outPutListCard.setVisibility(View.VISIBLE);
                _sewingBarcodeScanCard.setVisibility(View.VISIBLE);
                _sewingCompanyInfoCard.setVisibility(View.VISIBLE);
                _operationListCard.setVisibility(View.GONE);
                _defectListCard.setVisibility(View.GONE);
                break;
            case R.id.toolbar:
                Intent intent = V1_MenuActivity.getStartIntent(V1_SewingOutputActivity.this);
                startActivity(intent);
                finish();
            case R.id.backFromOperationButton:
            case R.id.backOutputFromDefectButton:
                _outPutListCard.setVisibility(View.VISIBLE);
                _sewingBarcodeScanCard.setVisibility(View.VISIBLE);
                _sewingCompanyInfoCard.setVisibility(View.VISIBLE);
                _operationListCard.setVisibility(View.GONE);
                _defectListCard.setVisibility(View.GONE);
                break;
            case R.id.backOperationFromDefectButton:
                _outPutListCard.setVisibility(View.GONE);
                _operationListCard.setVisibility(View.VISIBLE);
                _sewingBarcodeScanCard.setVisibility(View.GONE);
                _sewingCompanyInfoCard.setVisibility(View.GONE);
                _defectListCard.setVisibility(View.GONE);
                break;
        }
    }

    private boolean countTotalSelectedAlterDefect(ArrayList<V1_SewingAlterModel> sewingAlterModels) {
        int count = 0;
        for(V1_SewingAlterModel item: sewingAlterModels){
            if( item.getDefectCount() != null && !item.getDefectCount().equals("")){
                if(item.getDefectCount() != null && Integer.parseInt(item.getDefectCount()) > 0){
                    count += Integer.parseInt(item.getDefectCount());
                }
            }
        }
        if(modelArrayList.get(0).getAlter() <= count){
            return true;
        }
        return false;
    }

    private boolean countTotalSelectedSpotDefect(ArrayList<V1_SewingSpotModel> sewingSpotModels) {
        int count = 0;
        for(V1_SewingSpotModel item: sewingSpotModels){
            if( item.getDefectCount() != null && !item.getDefectCount().equals("")){
                if(item.getDefectCount() != null && Integer.parseInt(item.getDefectCount()) > 0){
                    count += Integer.parseInt(item.getDefectCount());
                }
            }
        }
        if(modelArrayList.get(0).getSpot() <= count){
            return true;
        }
        return false;
    }

    private boolean countTotalSelectedRejectDefect(ArrayList<V1_SewingRejectModel> sewingRejectModels) {
        int count = 0;
        for(V1_SewingRejectModel item: sewingRejectModels){
            if( item.getDefectCount() != null && !item.getDefectCount().equals("")){
                if(item.getDefectCount() != null && Integer.parseInt(item.getDefectCount()) > 0){
                    count += Integer.parseInt(item.getDefectCount());
                }
            }
        }
        if(modelArrayList.get(0).getReject() <= count){
            return true;
        }
        return false;
    }

    private void barcodeScanMethod() {
        barcode = barcodeET.getText().toString();

        if(!barcode.isEmpty())
        {
            barcode = barcodeET.getText().toString();
            if(modelArrayList.size()<1){
                sewingInputGetDataMethod(barcode);
            }else{
                showAlertMessage("Multiple Barcode can't scan.",0);
                barcodeET.setText("");
            }

        }
        else {
            sewingAlterModels.clear();
            sewingSpotModels.clear();
            sewingRejectModels.clear();
            rejectSewingOutputOperationItemModels.clear();
            alterSewingOutputOperationItemModels.clear();
            spotSewingOutputOperationItemModels.clear();
            Intent intent = new Intent(this, V1_ScannerActivity.class);
            intent.putExtra("url", urladdress);
            intent.putExtra("qc", "bundle_wise_sewing_output");
            startActivity(intent);
            finish();
        }
    }

    /*Scanning data*/
    private void sewingInputGetDataMethod(String barcode) {
        hideSoftKeyboard();
        showDialog();
        apiInterface.getSewingOutModelClassCall(sewingcompanyId, locationId, floorId, lineId, barcode, 5).enqueue(new Callback<V1_BundleWiseSewingOutputClass>() {
            @Override
            public void onResponse(Call<V1_BundleWiseSewingOutputClass> call, Response<V1_BundleWiseSewingOutputClass> response) {
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
                    sewing_input_line = response.body().getResultset().getSewingInputLine();
//                    if(line !=sewing_input_line){
//
//
//                        Intent i = new Intent(SewingOutputActivity.this, SewingOutputActivity.class);
//
//                        startActivity(i);
//
//
//                        TastyToast.makeText(getApplicationContext(), "Different line not allow!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
//                    }
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
                    replace_field_disable = String.valueOf(response.body().getResultset().getReplaceFieldDisable());

                    if(message.equals("")){

                        if(rescan == 0){
                            sSave.setText("SAVE");
                        }
                        else {
                            sSave.setText("UPDATE");
                        }

                        setDataInList();
                        initRecyclerView();
                        sewingOutputRecyclerViewAdapter.notifyDataSetChanged();
//                        sGridView.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
                    }else {
                        showAlertMessage(message, 0);
                    }

                }
                else {
                    showAlertMessage("Barcode already scanned Or Invalid Id", 0);
                }
            }

            @Override
            public void onFailure(Call<V1_BundleWiseSewingOutputClass> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(V1_SewingOutputActivity.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
                else {
                    showAlertMessage( "এই বান্ডেলটি ইতিমধ্যে স্ক্যান হয়েছে/স্ক্যান এর জন্য তৈরী হয়নি , দয়া করে অন্য একটি চেষ্টা করুন ।",0);
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
        replace_field_disable_arrayList.add(replace_field_disable);

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

        replace_field_disable_No = new String[replace_field_disable_arrayList.size()];
        for(int i = 0; i < replace_field_disable_arrayList.size(); i++)
        {
            replace_field_disable_No[i] = replace_field_disable_arrayList.get(i);
        }

        inputModelClass = new V1_SewingOutputModelClass();

        for(int i = 0; i < arrayCutNo.length; i++)
        {

            //input Data
            inputModelClass.setCut_no(arrayCutNo[i]);
            inputModelClass.setBundle_no(arrayBundleNo[i]);
            inputModelClass.setBarcode_no(arrayBarcodetNo[i]);
            inputModelClass.setOrder_id(Integer.parseInt(arrayorderNo[i]));
            inputModelClass.setItem_id(arrayItemNo[i]);
            inputModelClass.setCountry_id(arraycountryNo[i]);
            inputModelClass.setColor_id(Integer.parseInt(arraycolorNo[i]));
            inputModelClass.setSize_id(Integer.parseInt(arraySizeNo[i]));
            inputModelClass.setColor_size(Integer.parseInt(arraycolorSizeNo[i]));
            inputModelClass.setReplace_field_disable(Integer.parseInt(replace_field_disable_No[i]));

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
        initRecyclerView();
        sewingOutputRecyclerViewAdapter.notifyDataSetChanged();
//        sGridView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();

    }

    /*Post Data*/
    private void postDataToServer_sewing_input() throws JSONException {
        JSONObject jsonObject = buildJsonObject();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
        showDialog();
        sSave.setEnabled(false);
        apiInterface.saveUpdateBundleSewingOutputCall(body).enqueue(new Callback<V1_DataSaveResponse>() {
            @Override
            public void onResponse(Call<V1_DataSaveResponse> call, Response<V1_DataSaveResponse> response) {
                hideDialog();
                Log.d(TAG, "onResponse: "+response.toString());
                sSave.setEnabled(true);
                isSaveButtonClicked = false;
                if(response.isSuccessful()){
                    showAlertMessage(response.body().getResultset(), 1);
//                    if(arrayCutNo != null && arrayCutNo.length > 0)
//                    {
//                        Intent intent = new Intent(V1_SewingOutputActivity.this, V1_SewingOutputActivity.class);
//                        intent.putExtra("userId", userId);
//                        intent.putExtra("url", urladdress);
//                        startActivity(intent);
//
//                    }
                }else{
                    DialogHelper.showWarningDialog(V1_SewingOutputActivity.this, "Message", "Something went wrong. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<V1_DataSaveResponse> call, Throwable t) {
                hideDialog();
                sSave.setEnabled(true);
                isSaveButtonClicked = false;
                Log.d(TAG, "onFailure: "+t.getMessage());
                Toast.makeText(V1_SewingOutputActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private JSONObject buildJsonObject() throws JSONException{

        JSONObject save_obj = new JSONObject();
        JSONObject index_obj = new JSONObject();
        JSONObject data_obj = new JSONObject();
        JSONArray dtls_arr = new JSONArray();

        save_obj.put("status",true);
        save_obj.put("mode", "save");
        save_obj.put("production_type", 5);
        save_obj.put("UPDATE_ID", updatedID);

        index_obj.put("company_id", companyId);
        index_obj.put("shift_id", shiftId);
        index_obj.put("team_id", teamId);
        index_obj.put("location_id", locationId);
        index_obj.put("production_source", sourceId);
        index_obj.put("serving_company", sewingcompanyId);
        index_obj.put("floor_id", floorId);
        index_obj.put("sewing_line", lineId);
        if(isOrganic == 1){
            index_obj.put("organic", "Organic");
        }else{
            index_obj.put("organic", "");
        }
        index_obj.put("user_id", userId);
        index_obj.put("production_date", currentDate);
//        index_obj.put("production_date", sDate.getText().toString());
        index_obj.put("hour", currentTime);
//        index_obj.put("hour", sTime.getText().toString());
//        index_obj.put("remarks", sRemark.getText().toString());
        index_obj.put("remarks", "no");
        index_obj.put("txt_system_id", "");
        index_obj.put("mac", macAddress);
        //index_obj.put("is_rescan", rescan);

        data_obj.put("index", index_obj);

        for(int i = 0; i < 1; i++)
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

            dtls_obj.put("reject", modelArrayList.get(i).getReject());
            dtls_obj.put("alter", modelArrayList.get(i).getAlter());
            dtls_obj.put("spot", modelArrayList.get(i).getSpot());
            dtls_obj.put("replace", modelArrayList.get(i).getReplace());
            dtls_obj.put("qc_qnty", modelArrayList.get(i).getQc_qty());

            dtls_obj.put("is_rescan", rescan);
            dtls_obj.put("color_type_id", color_type_id);
            dtls_obj.put("sewing_input_line", sewing_input_line);

            dtls_arr.put(dtls_obj);
        }

        data_obj.put("actual_reject", reject_defect.equals("")? "" : reject_defect.substring(0, reject_defect.length() - 2));
        data_obj.put("actual_alter", alter_defect.equals("")? "" : alter_defect.substring(0, alter_defect.length() - 2));
        data_obj.put("actual_spot", spot_defect.equals("")? "" : spot_defect.substring(0, spot_defect.length() - 2));

        data_obj.put("list_data", dtls_arr);
        save_obj.put("data", data_obj);
        Log.d("TAG", "buildJsonObject: ######"+save_obj);
        return save_obj;
    }

    @Override
    public void onRejectHeadClick(int position, View v) {
        if(modelArrayList.get(0).getReject() > 0) {
            _outPutListCard.setVisibility(View.GONE);
            _operationListCard.setVisibility(View.GONE);
            _sewingBarcodeScanCard.setVisibility(View.GONE);
            _sewingCompanyInfoCard.setVisibility(View.GONE);
            _defectListCard.setVisibility(View.VISIBLE);
            _backOperationFromDefectButton.setVisibility(View.GONE);
            defectType = "R";
            setSewingDefectData();
//            Intent intent = new Intent(v.getContext(), V1_SewingOutputOperationActivity.class);
//            intent.putExtra("defectKey", "Reject Defect List");
//            intent.putExtra("defectDataKey", "reject");
//            intent.putExtra("defect_type", "R");
//            v.getContext().startActivity(intent);
        }else{
            showAlertMessage("Please enter \"Reject\" quantity.", 0);
        }
    }

    private void rejectDefectNameRequest(String s) {
        apiInterface.getRejectSewingDefectResponseCall(s, 460).enqueue(new Callback<V1_RejectSewingDefectResponse>() {
            @Override
            public void onResponse(Call<V1_RejectSewingDefectResponse> call, Response<V1_RejectSewingDefectResponse> response) {
                hideDialog();
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful()){
                    rejectPopUpModelClasses = response.body().getResultset().getDefectType();
                    if(response.body().getResultset().getDefectType() != null){
//                        for(int i=0; i<response.body().getResultset().getDefectType().size(); i++){
//                            V1_SewingRejectModel sewingRejectModel = new V1_SewingRejectModel();
//                            sewingRejectModel.setId(response.body().getResultset().getDefectType().get(i).getId());
//                            sewingRejectModel.setDefectName(response.body().getResultset().getDefectType().get(i).getDefectName());
//                            sewingRejectModel.setDefectCount(response.body().getResultset().getDefectType().get(i).getDefectCount());
//                            sewingRejectModel.setDefectSelect(false);
//                            sewingRejectModels.add(sewingRejectModel);
//                        }
                        sewingRejectModels.clear();
                        for(int i=0; i<response.body().getResultset().getDefectType().size(); i++){
                            V1_SewingRejectModel sewingRejectModel = new V1_SewingRejectModel();
                            String sentence = response.body().getResultset().getDefectType().get(i).getDefectName();
                            boolean isNumber = sentence.matches("\\d+");
                            if(isNumber){
                                sewingRejectModel.setDefectName(response.body().getResultset().getDefectType().get(i).getDefectName());
                            }else{
                                String[] words = sentence.split("\\s+");
                                StringBuilder sb = new StringBuilder();
                                for (String word : words) {
                                    sb.append(word.substring(0, 1).toUpperCase() + word.substring(1)).append(" ");
                                }
                                String capitalizedSentence = sb.toString().trim();
                                sewingRejectModel.setDefectName(capitalizedSentence);
                            }
                            sewingRejectModel.setId(response.body().getResultset().getDefectType().get(i).getId());
                            sewingRejectModel.setDefectCount(String.valueOf(0));
                            sewingRejectModel.setDefectSelect(false);
                            sewingRejectModels.add(sewingRejectModel);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<V1_RejectSewingDefectResponse> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(V1_SewingOutputActivity.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
                else {
                    showAlertMessage( "Reject Defect not added", 0);
                    hideDialog();
                }
            }
        });
    }

    @Override
    public void onAlterHeadClick(int position, View v) {
        if(modelArrayList.get(0).getAlter() > 0) {
//            Intent intent = new Intent(v.getContext(), V1_SewingOutputOperationActivity.class);
//            intent.putExtra("defectKey", "Alter Defect List");
//            intent.putExtra("defectDataKey", "alter");
//            intent.putExtra("defect_type", "A");
//            v.getContext().startActivity(intent);
            _outPutListCard.setVisibility(View.GONE);
            _operationListCard.setVisibility(View.GONE);
            _sewingBarcodeScanCard.setVisibility(View.GONE);
            _sewingCompanyInfoCard.setVisibility(View.GONE);
            _defectListCard.setVisibility(View.VISIBLE);
            _backOperationFromDefectButton.setVisibility(View.GONE);
            defectType = "A";
            setSewingDefectData();
        }else{
            showAlertMessage("Please enter \"Alter\" quantity.", 0);
        }
    }

    private void alterDefectNameRequest(String s) {
        apiInterface.getAlterSewingDefectResponseCall(s, 460).enqueue(new Callback<V1_AlterSewingDefectResponse>() {
            @Override
            public void onResponse(Call<V1_AlterSewingDefectResponse> call, Response<V1_AlterSewingDefectResponse> response) {
                hideDialog();
                if(response.isSuccessful()){
                    alterPopUPModelClasses = response.body().getResultset().getDefectType();
                    if(response.body().getResultset().getDefectType() != null){
//                        for(int i=0; i<response.body().getResultset().getDefectType().size(); i++){
//                            V1_SewingAlterModel sewingAlterSpotModel = new V1_SewingAlterModel();
//                            sewingAlterSpotModel.setId(response.body().getResultset().getDefectType().get(i).getId());
//                            sewingAlterSpotModel.setDefectName(response.body().getResultset().getDefectType().get(i).getDefectName());
//                            sewingAlterSpotModel.setDefectCount(response.body().getResultset().getDefectType().get(i).getDefectCount());
//                            sewingAlterSpotModel.setDefectSelect(false);
//                            sewingAlterModels.add(sewingAlterSpotModel);
//                        }
                        sewingAlterModels.clear();
                        for(int i=0; i<response.body().getResultset().getDefectType().size(); i++){
                            V1_SewingAlterModel sewingAlterModel = new V1_SewingAlterModel();
                            String sentence = response.body().getResultset().getDefectType().get(i).getDefectName();
                            boolean isNumber = sentence.matches("\\d+");
                            if(isNumber){
                                sewingAlterModel.setDefectName(response.body().getResultset().getDefectType().get(i).getDefectName());
                            }else{
                                String[] words = sentence.split("\\s+");
                                StringBuilder sb = new StringBuilder();
                                for (String word : words) {
                                    sb.append(word.substring(0, 1).toUpperCase() + word.substring(1)).append(" ");
                                }
                                String capitalizedSentence = sb.toString().trim();
                                sewingAlterModel.setDefectName(capitalizedSentence);
                            }
                            sewingAlterModel.setId(response.body().getResultset().getDefectType().get(i).getId());
                            sewingAlterModel.setDefectCount(String.valueOf(0));
                            sewingAlterModel.setDefectSelect(false);
                            sewingAlterModels.add(sewingAlterModel);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<V1_AlterSewingDefectResponse> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(V1_SewingOutputActivity.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
                else {
                    showAlertMessage( "Alter Defect not added", 0);
                    hideDialog();
                }
            }
        });
    }

    @Override
    public void onSportHeadClick(int position, View v) {
        if(modelArrayList.get(0).getSpot() > 0) {
            _outPutListCard.setVisibility(View.GONE);
            _operationListCard.setVisibility(View.GONE);
            _sewingBarcodeScanCard.setVisibility(View.GONE);
            _sewingCompanyInfoCard.setVisibility(View.GONE);
            _defectListCard.setVisibility(View.VISIBLE);
            _backOperationFromDefectButton.setVisibility(View.GONE);
            defectType = "S";
            setSewingDefectData();
//            Intent intent = new Intent(v.getContext(), V1_SewingOutputOperationActivity.class);
//            intent.putExtra("defectKey", "Spot Defect List");
//            intent.putExtra("defectDataKey", "spot");
//            intent.putExtra("defect_type", "S");
//            v.getContext().startActivity(intent);
        }else{
            showAlertMessage("Please enter \"Spot\" quantity.", 0);
        }
    }

    private void requestForSewingOperation() {
        showDialog();
        apiInterface.getSewingOutputOperationModelClassCall("Ref 4Order 6Colour", "4", "1").enqueue(new Callback<V1_BundleWiseSewingOperationResponse>() {
            @Override
            public void onResponse(Call<V1_BundleWiseSewingOperationResponse> call, Response<V1_BundleWiseSewingOperationResponse> response) {
                hideDialog();
                if(response.isSuccessful()){
                    if(response.body().getResult() != null){
                        configSewingOperationModels.clear();
                        for(int i=0; i<response.body().getResult().size(); i++){
                            V1_ConfigSewingOperationModel configSewingOperationModel = new V1_ConfigSewingOperationModel();
                            String sentence = response.body().getResult().get(i).getOperationName();
                            boolean isNumber = sentence.matches("\\d+");
                            if(isNumber){
                                configSewingOperationModel.setOperationName(response.body().getResult().get(i).getOperationName());
                            }else{
                                String[] words = sentence.split("\\s+");
                                StringBuilder sb = new StringBuilder();
                                for (String word : words) {
                                    sb.append(word.substring(0, 1).toUpperCase() + word.substring(1)).append(" ");
                                }
                                String capitalizedSentence = sb.toString().trim();
                                configSewingOperationModel.setOperationName(capitalizedSentence);
                            }
//                            configSewingOperationModel.setOperationName(response.body().getResult().get(i).getOperationName());
                            configSewingOperationModel.setOperationId(String.valueOf(response.body().getResult().get(i).getOperationId()));
                            configSewingOperationModel.setStatus(false);
                            configSewingOperationModels.add(configSewingOperationModel);
                        }

                        initOperationRecyclerView();
//                        for(int i=0; i<response.body().getResult().size(); i++){
//                            V1_RejectSewingOutputOperationItemModel rejectSewingOutputOperationItemModel = new V1_RejectSewingOutputOperationItemModel();
//                            V1_AlterSewingOutputOperationItemModel alterSewingOutputOperationItemModel = new V1_AlterSewingOutputOperationItemModel();
//                            V1_SpotSewingOutputOperationItemModel spotSewingOutputOperationItemModel = new V1_SpotSewingOutputOperationItemModel();
//                            rejectSewingOutputOperationItemModel.setOperationId(String.valueOf(response.body().getResult().get(i).getOperationId()));
//                            alterSewingOutputOperationItemModel.setOperationId(String.valueOf(response.body().getResult().get(i).getOperationId()));
//                            spotSewingOutputOperationItemModel.setOperationId(String.valueOf(response.body().getResult().get(i).getOperationId()));
//                            rejectSewingOutputOperationItemModel.setOperationName(String.valueOf(response.body().getResult().get(i).getOperationName()));
//                            alterSewingOutputOperationItemModel.setOperationName(String.valueOf(response.body().getResult().get(i).getOperationName()));
//                            spotSewingOutputOperationItemModel.setOperationName(String.valueOf(response.body().getResult().get(i).getOperationName()));
//                            rejectSewingOutputOperationItemModel.setStatus(0);
//                            alterSewingOutputOperationItemModel.setStatus(0);
//                            spotSewingOutputOperationItemModel.setStatus(0);
//                            rejectSewingOutputOperationItemModels.add(rejectSewingOutputOperationItemModel);
//                            alterSewingOutputOperationItemModels.add(alterSewingOutputOperationItemModel);
//                            spotSewingOutputOperationItemModels.add(spotSewingOutputOperationItemModel);
//                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<V1_BundleWiseSewingOperationResponse> call, Throwable t) {
                hideDialog();
                showAlertMessage("Data not found.", 0);
            }
        });
    }

    private void initOperationRecyclerView() {
        bundleWiseSewingOperationRecyclerAdapter = new V1_BundleWiseSewingOperationRecyclerAdapter(configSewingOperationModels, this,  this);
        operationRecyclerView.setAdapter(bundleWiseSewingOperationRecyclerAdapter);
        operationRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void spotDefectNameRequest(String s) {
        apiInterface.getSpotSewingDefectResponseCall(s, 460).enqueue(new Callback<V1_SpotSewingDefectResponse>() {
            @Override
            public void onResponse(Call<V1_SpotSewingDefectResponse> call, Response<V1_SpotSewingDefectResponse> response) {
                hideDialog();
                Log.d("TAG", "onResponse: "+response.toString());
                if(response.isSuccessful()){
                    spotPopUPModelClasses = response.body().getResultset().getDefectType();
                    if(response.body().getResultset().getDefectType() != null){
//                        for(int i=0; i<response.body().getResultset().getDefectType().size(); i++){
//                            V1_SewingSpotModel sewingSpotModel = new V1_SewingSpotModel();
//                            sewingSpotModel.setId(response.body().getResultset().getDefectType().get(i).getId());
//                            sewingSpotModel.setDefectName(response.body().getResultset().getDefectType().get(i).getDefectName());
//                            sewingSpotModel.setDefectCount(response.body().getResultset().getDefectType().get(i).getDefectCount());
//                            sewingSpotModel.setDefectSelect(false);
//                            sewingSpotModels.add(sewingSpotModel);
//                        }
                        sewingSpotModels.clear();
                        for(int i=0; i<response.body().getResultset().getDefectType().size(); i++){
                            V1_SewingSpotModel sewingSpotModel = new V1_SewingSpotModel();
                            String sentence = response.body().getResultset().getDefectType().get(i).getDefectName();
                            boolean isNumber = sentence.matches("\\d+");
                            if(isNumber){
                                sewingSpotModel.setDefectName(response.body().getResultset().getDefectType().get(i).getDefectName());
                            }else{
                                String[] words = sentence.split("\\s+");
                                StringBuilder sb = new StringBuilder();
                                for (String word : words) {
                                    sb.append(word.substring(0, 1).toUpperCase() + word.substring(1)).append(" ");
                                }
                                String capitalizedSentence = sb.toString().trim();
                                sewingSpotModel.setDefectName(capitalizedSentence);
                            }
                            sewingSpotModel.setId(response.body().getResultset().getDefectType().get(i).getId());
                            sewingSpotModel.setDefectCount(String.valueOf(0));
                            sewingSpotModel.setDefectSelect(false);
                            sewingSpotModels.add(sewingSpotModel);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<V1_SpotSewingDefectResponse> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(V1_SewingOutputActivity.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
                else {
                    showAlertMessage( "Spot Defect not added", 0);
                    hideDialog();
                }
            }
        });
    }

    private void setSewingDefectData() {
        if(defectType.equals("A")) {
            sewingOutputAlterDefectRecyclerAdapter = new V1_SewingOutputAlterDefectRecyclerAdapter(sewingAlterModels, this,  this);
            defectRecyclerView.setAdapter(sewingOutputAlterDefectRecyclerAdapter);
            defectRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
            sewingOutputAlterDefectRecyclerAdapter.notifyDataSetChanged();
        }else if(defectType.equals("S")){
            sewingOutputSpotDefectRecyclerAdapter = new V1_SewingOutputSpotDefectRecyclerAdapter(sewingSpotModels, this,  this);
            defectRecyclerView.setAdapter(sewingOutputSpotDefectRecyclerAdapter);
            defectRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
            sewingOutputSpotDefectRecyclerAdapter.notifyDataSetChanged();
        }else if(defectType.equals("R")) {
            Log.d(TAG, "setSewingDefectData: "+sewingRejectModels.get(0).getDefectSelect());
            sewingOutputRejectDefectRecyclerAdapter = new V1_SewingOutputRejectDefectRecyclerAdapter(sewingRejectModels, this,  this);
            defectRecyclerView.setAdapter(sewingOutputRejectDefectRecyclerAdapter);
            defectRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
            sewingOutputRejectDefectRecyclerAdapter.notifyDataSetChanged();
        }
    }

    private void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if(view == null){
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /*Data Picker*/
    private void datePickerMethod() {
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

    /*Time Picker*/
    private void  timePickerMethor() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(V1_SewingOutputActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                sTime.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(V1_SewingOutputActivity.this, V1_MenuActivity.class);
//        startActivity(intent);
        sewingAlterModels.clear();
        sewingSpotModels.clear();
        sewingRejectModels.clear();
        rejectSewingOutputOperationItemModels.clear();
        alterSewingOutputOperationItemModels.clear();
        spotSewingOutputOperationItemModels.clear();
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
//        finish();
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

    private void showAlertMessage(String msg, int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(V1_SewingOutputActivity.this);
        builder.setTitle("Message")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", (dialog, which) -> {
                    if(i == 1){
                        sewingAlterModels.clear();
                        sewingSpotModels.clear();
                        sewingRejectModels.clear();
                        rejectSewingOutputOperationItemModels.clear();
                        alterSewingOutputOperationItemModels.clear();
                        spotSewingOutputOperationItemModels.clear();
                        Intent intent = new Intent(V1_SewingOutputActivity.this, V1_ScannerActivity.class);
                        intent.putExtra("url", urladdress);
                        intent.putExtra("qc", "bundle_wise_sewing_output");
                        startActivity(intent);
                        finish();
                    }else{
                        dialog.dismiss();
                    }

                });
        AlertDialog dialog  = builder.create();
        dialog.show();
    }


    @Override
    public void onOperationHeadClick(int position, View v) {
        _outPutListCard.setVisibility(View.GONE);
        _operationListCard.setVisibility(View.GONE);
        _sewingBarcodeScanCard.setVisibility(View.GONE);
        _sewingCompanyInfoCard.setVisibility(View.GONE);
        _defectListCard.setVisibility(View.VISIBLE);
        if(configSewingOperationModels.get(position).getStatus()){
            configSewingOperationModels.get(position).setStatus(false);
        }else{
            configSewingOperationModels.get(position).setStatus(true);
        }
        bundleWiseSewingOperationRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAlterDefectHeadClick(int position, View v) {

    }

    @Override
    public void onRejectDefectHeadClick(int position, View v) {

    }

    @Override
    public void onSpotDefectHeadClick(int position, View v) {

    }
}
