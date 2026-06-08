package com.logicsoftbd.lsl.ui.v_1_ui.without_observation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.db.v1_db.helper.DBAdapter;
import com.logicsoftbd.lsl.data.db.v1_db.model.V1_User;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundleWiseCompanyToShiftClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DataSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_FinishFabricModelClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_QcModelRND;
import com.logicsoftbd.lsl.data.prefs.SessionManager;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing.V1_SewingOutputActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.home.V1_HomeActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.login.V1_LoginActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_QRBarcodeScannerActivity;
import com.logicsoftbd.lsl.utils.ApiUtils;
import com.logicsoftbd.lsl.utils.AppUtils;
import com.logicsoftbd.lsl.utils.DialogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
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

public class V1_FinishFabricActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "V1_FinishFabricActivity";
    private EditText qrcodeEt;
    private Button scaneBtn;
    private Button datePick;
    private String currentDate;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private Button saveBtn;
    private Button tryAginBtn;
    private GridView gridView;
    private int Year, Month, Day;

    public TextView tvtotalPennalty;
    public TextView tvpoint;
    public TextView tvfebricGrade;

    private TextView etRoll;
    private EditText etQcName;
    public EditText etRollWidthInch;
    public EditText etRollKg;
    public TextView etRollLength;
    private EditText etRejectQc;
    private EditText etcomments;
    private Spinner statusSpinner, sSfhift;

    private TextView tvConstruction;
    private TextView tvGSM;
    private TextView tvDia;
    private TextView tvMCDia;
    private TextView tvColor;
    private TextView tvYarnCount;
    private TextView tvYarnLot;
    private TextView tvSpinning;

    private String mode;
    private int mst_id = 0;
    private int prod_id = 0;
    private int trans_id = 0;
    private int dtls_id = 0;
    private int qc_mst_id = 0;
    private long barcode;
    private int dtl_id = 0;

    private int bayer_id = 0;
    private int company_id = 0;
    private String barcodeNumber;
    private String barcodeNumInput;
    private int rollNumber = 0;
    //public int roll_w_kg = 0;
    public Double yds = 0.0;
    public int roll_inch = 0;
    private String construction_C;
    private int rollMaintain = 0;
    private int rollId = 0;
    public int gsm = 0;
    private String dia;
    private int mc_dia = 0;
    private String color;
    private String yarnCount;
    private String yarnLot;
    private String spinningmill;
    private String date;
    private String comments;
    private int rollstatus = 0;
    private int shift_id = 0;
    private int machineNameId = 0;
    private int rejectQty = 0;

    //update
    private String updateDate;
    private int updateRollWidthInch = 0;
    public double updateTotalPoint = 0.0;
    private String updateGrade;
    private String updateComments;
    private int updaterollstatus = 0;
    private int updatedID = 0;

    private String urladdressChk;
    private String urladdress;
    private String urlString;
    public int userId = 0;

    public final ArrayList<String> gradename = new ArrayList<>();
    public final ArrayList<String> defactname = new ArrayList<>();
    public final ArrayList<String> machinename = new ArrayList<>();
    public final ArrayList<Integer> defectID = new ArrayList<>();
    public final ArrayList<Integer> machineNamID = new ArrayList<>();
    public final ArrayList<String> defectInchName = new ArrayList<>();
    public final ArrayList<Integer> defectInch = new ArrayList<>();

    public String[] gradeArray;
    public Integer[] gradeIDArray;
    public String[] defectInchNameArray;

    public String[] machineNameArray;
    public Integer[] machineIdArray;

    public final ArrayList<Integer> updatedefectCount = new ArrayList<>();
    public final ArrayList<Integer> updateDefectItem = new ArrayList<>();
    public final ArrayList<Integer> updateDefect = new ArrayList<>();

    public Integer[] updateDefectCountArry;
    public Integer[] updateDefectItemArry;
    public Integer[] updateDefectArry;

    public static Integer[] saveDefectItemArry = {6,4,4,4,4, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,0,4,0,0,0,0,0,0,0,0,0,0,0,0, 0, 0};

    private V1_QcModelRND qcModelRND;

    private SessionManager session;
    private DBAdapter dbAdapter;

    private View view;
    public static ArrayList<V1_QcModelRND> modelArrayList;

    private V1_FinishFabricCustomAdapter finishFabricCustomAdapter;

    //Retrofit
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;

    private AlertDialog.Builder alertDialogBuilder;
    private String resultupdate;

    //userPreviledge
    private int savemenu = 0;
    private int updatemenu = 0;

    //Qc Entry
    private String qc_entry;
    private String shiftId ="0";

    //Master Part
    private Spinner fCompany;
    private Spinner fSource;
    private Spinner fServiceCompany;
    private Spinner fLocation;
    private Spinner fserviceLocation;

    private String urlstringbase;
    private String urlstring_c_wise_l;

    public final ArrayList<String> companyNameList = new ArrayList<>();
    public final ArrayList<Integer> companyNameId = new ArrayList<>();
    public final ArrayList<String> serviceList = new ArrayList<>();
    public final ArrayList<Integer> serviceNameId = new ArrayList<>();
    public final ArrayList<String> sourceList = new ArrayList<>();
    public final ArrayList<Integer> sourceNameId = new ArrayList<>();
    public final ArrayList<String> locationList = new ArrayList<>();
    public final ArrayList<Integer> locationNameId = new ArrayList<>();
    public final ArrayList<String> serviceLocationList = new ArrayList<>();
    public final ArrayList<Integer> serviceLocationNameId = new ArrayList<>();

    public final ArrayList<String> machineList = new ArrayList<>();
    public final ArrayList<Integer> machineListId = new ArrayList<>();

    public final ArrayList<String> shifNametList = new ArrayList<>();
    public final ArrayList<Integer> shiftListId = new ArrayList<>();

    public String[] companyNameArray;
    public String[] serviceArray;
    public String[] sourceArray;
    public String[] locationArray;
    public String[] servicelocationArray;
    public String[] machineArray;
    public String[] shiftArray;

    int companyId = 0;
    int servicecompanyId = 0;
    int sourceId = 0;
    int locationId = 0;
    int serviceLocationId = 0;

    private EditText receiveNoET, challanNoET;
    private Button fDate, dateTV;

    private ImageButton scanButton;
    private TextView barcodeText;

    private TextView barcodeTV, jobNumberTV, batchNoTV, rollNoTV, rollKg, orderNoTV, bodyPartTV, styleTV, colorTV, constructionTV, dia_widthTV, wigt_lost, TVirlb;
    private EditText  gsmET, qc_pass_qtyET, rejectET, rollWidhInch, rollLength, pro_qtyTV, diaTV;
    // private Spinner machineNameSpinner, shiftNameSpinner;

    private String base_url = "", fBarcode, frollNo, forderNo, fjobNumber, fbodyPart,fReceiveDate, fstyle, fconstruction, fdia_width, fbatch, fgsm, fInternalRef,  fdia, fcolor, fqcpass, freject;
    private int fbatch_id = 0, fBodyPart_id = 0, fBooking_no = 0, fBookingwithoutOrder = 0, fMachine_id = 0, fRollWidth = 0,
            fShift_id = 0, fDeter_id = 0, fDia_type = 0, fIsSalesId = 0, fOrdetId = 0, fRollId = 0, fRollWeight = 0;
    private Double fProductionQty = 0.0, frollLength = 0.0;
    private Double statusQty, fproqc = 0.0;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_finish_fabric);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializationMethod();
    }
    private void initializationMethod() {
        /*master part*/
        barcodeText = findViewById(R.id.barcodenumberText);

        dateTV = findViewById(R.id.TVDate);
        dateTV.setOnClickListener(this);
        barcodeTV = findViewById(R.id.TVbarcode);
        jobNumberTV = findViewById(R.id.TVJobNumber);
        //rollNoTV = findViewById(R.id.TVrollNo);
        rollWidhInch = findViewById(R.id.ETRollWidthInch);
        rollKg = findViewById(R.id.TVRollKg);
        rollLength = findViewById(R.id.TVRollLength);
        orderNoTV = findViewById(R.id.TVorderNo);
        bodyPartTV = findViewById(R.id.TVbodyPart);
        styleTV = findViewById(R.id.TVstyle);
        constructionTV = findViewById(R.id.TVconstruction);
        dia_widthTV = findViewById(R.id.TVdia_width);

        batchNoTV = findViewById(R.id.TVBatchNo);
        gsmET = findViewById(R.id.ETgsm);
        diaTV = findViewById(R.id.ETdia);
        wigt_lost = findViewById(R.id.width_loss);
        TVirlb = findViewById(R.id.TVirlb);
        colorTV = findViewById(R.id.ETcolor);
        pro_qtyTV = findViewById(R.id.TVPro_qty);
        qc_pass_qtyET = findViewById(R.id.ETqc_pass);
        rejectET = findViewById(R.id.ETreject);


        gridView = findViewById(R.id.gridViewInfo);
        saveBtn = findViewById(R.id.btnSave);

        //refreshBtn = findViewById(R.id.btnrefresh);
        tryAginBtn = findViewById(R.id.btnTryAgain);

        saveBtn.setOnClickListener(this);
        tryAginBtn.setOnClickListener(this);

        //summery
        tvtotalPennalty = findViewById(R.id.penaltyPointTV);
        tvpoint = findViewById(R.id.toalPointTV);
        tvfebricGrade = findViewById(R.id.febricGradeTV);

        etcomments = findViewById(R.id.commentET);


        tvConstruction = findViewById(R.id.constructionTV);
        tvGSM = findViewById(R.id.gsmTV);
        tvDia = findViewById(R.id.diaTV);
        tvMCDia = findViewById(R.id.mcDiaTV);
        tvColor = findViewById(R.id.colorTV);
        tvYarnCount = findViewById(R.id.yarn_countTV);
        tvYarnLot = findViewById(R.id.yarnLotTV);
        tvSpinning = findViewById(R.id.spinningTV);
        statusSpinner = findViewById(R.id.spinnerQc);
        sSfhift = findViewById(R.id.shiftSpinner);

        progressBar = findViewById(R.id.loadingProgress);

        ArrayAdapter<CharSequence> adapterhole = ArrayAdapter.createFromResource(this, R.array.statusroll
                , R.layout.spinner_item);
        statusSpinner.setAdapter(adapterhole);



        qcModelRND = new V1_QcModelRND();

        Intent intent = getIntent();

        String resultS = intent.getStringExtra("result");
        urladdressChk = intent.getStringExtra("url");
        savemenu = intent.getIntExtra("s", 0);
        updatemenu = intent.getIntExtra("u", 0);
        qc_entry = intent.getStringExtra("qc");


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
        base_url = (_preferences.getString("base_url", ""));

        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);


        barcodeNumber = resultS;
        //barcodeNumber = resultS2;

        if(resultS != null){
            //barcodeTV.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            barcodeTV.setText(resultS);
            /*sendRequestToServer(barcodeNumber);*/
        }
        else {
            saveBtn.setVisibility(View.GONE);
            tryAginBtn.setVisibility(View.GONE);
        }

        sendRequestFinishfabricServer();
        companyWiseShift();
        // session manager
        session = new SessionManager(getApplicationContext());
        dbAdapter = new DBAdapter(this);

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = simpleDateFormat.format(calendar.getTime());

        // datePick.setText(currentDate);
        dateTV.setText(currentDate);

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                        rollstatus = 0;
                        rejectQty = 0;
                        rejectET.setText(String.valueOf(rejectQty));

                        break;
                    case 1:
                        rollstatus = 1;
                        //rejectET.setText(String.valueOf(rejectQty));
                        break;
                    case 2:
                        rollstatus = 2;
                        rejectQty = 0;
                        rejectET.setText(String.valueOf(rejectQty));

                        break;
                    case 3:
                        rollstatus = 3;
                        double roll_kg = Double.parseDouble(rejectET.getText().toString());
                        if(  roll_kg >= fProductionQty)
                        {
                            rejectET.setText(String.valueOf(fProductionQty));
                        }
                        rejectET.setText(String.valueOf(fProductionQty));
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        pro_qtyTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!pro_qtyTV.getText().toString().isEmpty()){
                    double wgtLost = fProductionQty - Double.parseDouble(pro_qtyTV.getText().toString());
                    wigt_lost.setText(String.valueOf(wgtLost));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void companyWiseShift() {
        apiInterface.getBundleWiseCompanyToShiftClassCall().enqueue(new Callback<V1_BundleWiseCompanyToShiftClass>() {
            @Override
            public void onResponse(Call<V1_BundleWiseCompanyToShiftClass> call, Response<V1_BundleWiseCompanyToShiftClass> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful())
                {
                    if(response.body().getData().size() > 0){
                        shifNametList.clear();
                        shiftListId.clear();
                        for(int i=0; i<response.body().getData().size(); i++){
                            shifNametList.add(response.body().getData().get(i).getShift());
                            shiftListId.add(response.body().getData().get(i).getId());
                        }
                    }
                    try {
                        setUpShiftSpinner();
                    } catch (Exception e){
                        Log.d(TAG, "fetchFinishData: "+e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<V1_BundleWiseCompanyToShiftClass> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(V1_FinishFabricActivity.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setUpShiftSpinner() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.custome_spinner_dropdown, shifNametList);
        sSfhift.setAdapter(spinnerArrayAdapter);
        sSfhift.setSelection(shiftListId.indexOf(shiftId));
        sSfhift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shiftId = String.valueOf(shiftListId.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void sendRequestFinishfabricServer() {
        progressBar.setVisibility(View.VISIBLE);
        apiInterface.getFinishFabricModelClassCall(barcodeNumber).enqueue(new Callback<V1_FinishFabricModelClass>() {
            @Override
            public void onResponse(Call<V1_FinishFabricModelClass> call, Response<V1_FinishFabricModelClass> response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful())
                {
                    if(response.body().getStatus()){
                        mode = response.body().getData().getIndex().getMode();
                        if(mode.equals("save"))
                        {
                            saveBtn.setText("SAVE");
                            savemethod(response);
                        }
                        else if(mode.equals("update")){
                            saveBtn.setText("UPDATE");
                            savemethod(response);
                            updateMethod(response);
                        }
                    }else{
                        DialogHelper.showWarningDialog(V1_FinishFabricActivity.this, "Message", response.body().getShadeMsg());
                    }

                }
                else {
                    DialogHelper.showWarningDialog(V1_FinishFabricActivity.this, "Message", "This bundle is not found");
                }
            }

            @Override
            public void onFailure(Call<V1_FinishFabricModelClass> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: "+t.getMessage());
                DialogHelper.showErrorDialog(V1_FinishFabricActivity.this, "Message", "Something went wrong");
            }
        });
    }

    private void updateMethod(Response<V1_FinishFabricModelClass> response) {
        updateDate = response.body().getData().getIndex().getQcDate();
        updateTotalPoint = Double.parseDouble(response.body().getData().getIndex().getTotalPoint());
        updateGrade = response.body().getData().getIndex().getFabricGrade();
        updateComments = response.body().getData().getIndex().getComments();
        updaterollstatus = Integer.parseInt(response.body().getData().getIndex().getRollStatus());
        fRollWidth = Integer.parseInt(response.body().getData().getIndex().getRollWidth());



        rollWidhInch.setText(String.valueOf(fRollWidth));
        dateTV.setText(String.valueOf(updateDate));
        tvtotalPennalty.setText(String.valueOf(updateTotalPoint));
        tvfebricGrade.setText(updateGrade);
        etcomments.setText(updateComments);
        statusSpinner.setSelection(updaterollstatus);
    }

    private void savemethod(Response<V1_FinishFabricModelClass> response) {
        mst_id = Integer.parseInt(response.body().getData().getIndex().getMstId());
        prod_id = Integer.parseInt(response.body().getData().getIndex().getProdId());
        trans_id = Integer.parseInt(response.body().getData().getIndex().getTransId());
        dtls_id = Integer.parseInt(response.body().getData().getIndex().getDtlsId());
        qc_mst_id = Integer.parseInt(response.body().getData().getIndex().getQcMstId());
        fBarcode = String.valueOf(response.body().getData().getIndex().getBarcodeNo());
        frollNo = String.valueOf(response.body().getData().getIndex().getRollNo());
        //roll_w_kg = String.valueOf(response.body().getData().getIndex().getProdQnty());
        forderNo = String.valueOf(response.body().getData().getIndex().getPoNumber());
        fjobNumber = response.body().getData().getIndex().getJobNumber();
        fbodyPart = String.valueOf(response.body().getData().getIndex().getBodyPart());
        fReceiveDate = response.body().getData().getIndex().getBatchNo();
        fstyle = String.valueOf(response.body().getData().getIndex().getStyleRefNo());
        fconstruction = String.valueOf(response.body().getData().getIndex().getConstruction());
        fdia_width = String.valueOf(response.body().getData().getIndex().getWidthDiaVal());
        fbatch = String.valueOf(response.body().getData().getIndex().getBatchNo());
        fgsm = String.valueOf(response.body().getData().getIndex().getGsm());
        fInternalRef = String.valueOf(response.body().getData().getIndex().getInternalRef());
        fdia = String.valueOf(response.body().getData().getIndex().getWidth());
        fcolor = String.valueOf(response.body().getData().getIndex().getColor());
        fproqc = Double.valueOf(String.valueOf(response.body().getData().getIndex().getProdQnty()));
        fqcpass = String.valueOf(response.body().getData().getIndex().getQcPassQty());
        frollLength = Double.valueOf(response.body().getData().getIndex().getRollLength());

//        yds = frollLength;
//        rollLength.setText(String.valueOf(frollLength));


        fbatch_id = Integer.parseInt(String.valueOf(response.body().getData().getIndex().getBatchId()));
        fBodyPart_id = Integer.parseInt(response.body().getData().getIndex().getBodyPartId());
        //fBooking_no = (int) response.body().getData().getIndex().getBookingNo();
        if(response.body().getData().getIndex().getBookingWithoutOrder() != null){
            fBookingwithoutOrder = Integer.parseInt(response.body().getData().getIndex().getBookingWithoutOrder());
        }

        company_id = Integer.parseInt(response.body().getData().getIndex().getCompanyId());
        servicecompanyId = Integer.parseInt(response.body().getData().getIndex().getCompanyId());
        sourceId = Integer.parseInt(response.body().getData().getIndex().getCompanyId());
        servicecompanyId = Integer.parseInt(response.body().getData().getIndex().getCompanyId());
        serviceLocationId = Integer.parseInt(response.body().getData().getIndex().getCompanyId());
        fMachine_id = Integer.parseInt(response.body().getData().getIndex().getBatchId());
        fShift_id = Integer.parseInt(response.body().getData().getIndex().getBatchId());
        fDeter_id = Integer.parseInt(response.body().getData().getIndex().getDeterD());
        fDia_type = Integer.parseInt(response.body().getData().getIndex().getWidthDiaId());
        fIsSalesId = Integer.parseInt(response.body().getData().getIndex().getIsSales());
        fOrdetId = Integer.parseInt(String.valueOf(response.body().getData().getIndex().getPoBreakdownId()));
        fRollId = Integer.parseInt(response.body().getData().getIndex().getRollId());
        fProductionQty = Double.valueOf(response.body().getData().getIndex().getProdQnty());
        fRollWeight = Integer.parseInt(response.body().getData().getIndex().getRollId());
        fRollWeight = Integer.parseInt(response.body().getData().getIndex().getRollId());

        finishFabricQcData(response);

        barcodeTV.setText(fBarcode);
        //rollNoTV.setText(frollNo);
        rollKg.setText(String.valueOf(fProductionQty));
        jobNumberTV.setText(fjobNumber);
        orderNoTV.setText(forderNo);
        bodyPartTV.setText(fbodyPart);
        styleTV.setText(fstyle);
        constructionTV.setText(fconstruction);
        dia_widthTV.setText(fdia_width);
        batchNoTV.setText(fbatch);
        gsmET.setText(fgsm);
        diaTV.setText(String.valueOf(fdia));
        TVirlb.setText(String.valueOf(fInternalRef));
        colorTV.setText(fcolor);
//        pro_qtyTV.setText(String.valueOf(String.format("%.2f", fproqc)));

        qc_pass_qtyET.setText(String.valueOf(0));

//        qc_pass_qtyET.setText(fqcpass);
        //rollLength.setText(String.valueOf(frollLength));
    }

    private void finishFabricQcData(Response<V1_FinishFabricModelClass> response) {
        V1_FinishFabricModelClass.Grade gradeName;
        List<V1_FinishFabricModelClass.Grade> grades = response.body().getData().getIndex().getArrayRefData().getGrade();
        for(V1_FinishFabricModelClass.Grade d : grades)
        {
            gradeName = d;
            final V1_FinishFabricModelClass.Grade finalName = gradeName;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gradename.add(String.valueOf(finalName.getGrade()));
                }
            });
        }

        V1_FinishFabricModelClass.Defect defactName;
        List<V1_FinishFabricModelClass.Defect> defactGrades = response.body().getData().getIndex().getArrayRefData().getDefect();
        for(V1_FinishFabricModelClass.Defect d : defactGrades)
        {
            defactName = d;
            final V1_FinishFabricModelClass.Defect finalName = defactName;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    defactname.add(String.valueOf(finalName.getDEFECTNAME()));
                }
            });
        }

        V1_FinishFabricModelClass.Defect defactID;
        List<V1_FinishFabricModelClass.Defect> ID = response.body().getData().getIndex().getArrayRefData().getDefect();
        for(V1_FinishFabricModelClass.Defect d : ID)
        {
            defactID = d;
            final V1_FinishFabricModelClass.Defect finalID = defactID;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    defectID.add(finalID.getID());
                }
            });
        }

        V1_FinishFabricModelClass.Defect udatedefactount;
        List<V1_FinishFabricModelClass.Defect> updateCount = response.body().getData().getIndex().getArrayRefData().getDefect();
        for(V1_FinishFabricModelClass.Defect d : updateCount)
        {
            udatedefactount = d;
            final V1_FinishFabricModelClass.Defect finalDefectCount = udatedefactount;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updatedefectCount.add(finalDefectCount.getDEFECTCOUNT());
                }
            });
        }

        V1_FinishFabricModelClass.Defect updatedefect;
        List<V1_FinishFabricModelClass.Defect> updatedefactList = response.body().getData().getIndex().getArrayRefData().getDefect();
        for(V1_FinishFabricModelClass.Defect d : updatedefactList)
        {
            updatedefect = d;
            final V1_FinishFabricModelClass.Defect finalDefect = updatedefect;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateDefect.add(finalDefect.getPENALTYPOINT());
                }
            });
        }
        V1_FinishFabricModelClass.Defect updateSpinner;
        List<V1_FinishFabricModelClass.Defect> updateSpinnerList = response.body().getData().getIndex().getArrayRefData().getDefect();
        for(V1_FinishFabricModelClass.Defect d : updateSpinnerList)
        {
            updateSpinner = d;
            final V1_FinishFabricModelClass.Defect finalSpinnerList = updateSpinner;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateDefectItem.add(finalSpinnerList.getFOUNDININCH());
                }
            });
        }

        alertDialogBuilder = new AlertDialog.Builder(V1_FinishFabricActivity.this);
        modelArrayList = getModel();
        finishFabricCustomAdapter = new V1_FinishFabricCustomAdapter(V1_FinishFabricActivity.this);
        try {
            gridView.setAdapter(finishFabricCustomAdapter);
        }catch (Exception e){
            Toast.makeText(this, "Defect Data not found.", Toast.LENGTH_SHORT).show();
        }
    }

    public void calculationYds() {
        //double width_inch = Double.parseDouble(etRollWidthInch.getText().toString());
        String inch = rollWidhInch.getText().toString().trim();
        String sum = tvtotalPennalty.getText().toString().trim();
        String rl = rollLength.getText().toString().trim();
        if(TextUtils.isEmpty(String.valueOf(sum)))
        {
            tvtotalPennalty.setError("Fill the credential");
        }
        else if(TextUtils.isEmpty(String.valueOf(inch)))
        {
            rollWidhInch.setError("Fill the credential");
        }
        else if(rl.equals("0.0"))
        {
            rollLength.setError("Fill the credential");
        }
        else {
            yds = ((Double.parseDouble(String.valueOf(fProductionQty))* 1000) / (Integer.parseInt(String.valueOf(fgsm)) * Double.parseDouble(inch)* 0.0254) * 1.09361);
//            etRollLength.setText(String.valueOf(yds));
            rollLength.setText((String.valueOf(String.format("%.4f", yds))));

//            yds = Double.valueOf(rollLength.getText().toString());

            Double totalPanalty = ((36 * 100 * Integer.parseInt(sum)) / (Double.parseDouble(inch)* yds));
            qcModelRND.setTotalPoint(totalPanalty);
//            tvpoint.setText((String.valueOf(String.format("%.4f", totalPanalty))));
            if(String.valueOf(qcModelRND.getTotalPoint()).equals("Infinity"))
            {
                tvpoint.setText(String.valueOf(0));
            }
            else {
                tvpoint.setText(String.valueOf(String.format("%.4f", qcModelRND.getTotalPoint())));
            }

            //tvpoint.setText(String.valueOf(qcModelRND.getTotalPoint()));


            int grade = 0;
            grade = (int) qcModelRND.getTotalPoint();
            if(grade >= 0 && grade < gradename.size())
            {
                tvfebricGrade.setText(String.valueOf(gradename.get(grade)));
                tvfebricGrade.setTextColor(Color.RED);
            }
            else
            {
                tvfebricGrade.setText("Rejected");
            }
        }
    }



    private ArrayList<V1_QcModelRND> getModel() {

        ArrayList<V1_QcModelRND> list = new ArrayList<>();
        gradeArray = new String[defactname.size()];
        for(int i = 0; i < defactname.size(); i++)
        {
            gradeArray[i] = defactname.get(i);
        }
        gradeIDArray = new Integer[defectID.size()];
        for(int i = 0; i < defectID.size(); i++)
        {
            gradeIDArray[i] = defectID.get(i);
        }
        defectInchNameArray = new String[defectInchName.size()];
        for(int i = 0; i < defectInchName.size(); i++)
        {
            defectInchNameArray[i] = defectInchName.get(i);
        }

        updateDefectCountArry = new Integer[updatedefectCount.size()];
        for(int i = 0; i < updatedefectCount.size(); i++)
        {
            updateDefectCountArry[i] = updatedefectCount.get(i);
        }

        updateDefectArry = new Integer[updateDefect.size()];
        for(int i = 0; i < updateDefect.size(); i++)
        {
            updateDefectArry[i] = updateDefect.get(i);
        }

        updateDefectItemArry = new Integer[updateDefectItem.size()];
        for(int i = 0; i < updateDefectItem.size(); i++)
        {
            updateDefectItemArry[i] = updateDefectItem.get(i);
        }


        for(int i = 0; i < gradeArray.length; i++){
            V1_QcModelRND qcModel = new V1_QcModelRND();
            qcModel.setQcItemName(gradeArray[i]);
            qcModel.setDefectID(gradeIDArray[i]);
            qcModel.setQcItemNumber(updateDefectCountArry[i]);
            qcModel.setQcDefectNumber(updateDefectArry[i]);
            qcModel.setSpinneritem(updateDefectItemArry[i]);


            list.add(qcModel);
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSave:
                String name = null;
                String rl = rollLength.getText().toString().trim();
                if(rollstatus == 0)
                {
                    Toast.makeText(this, "Roll status not selected.", Toast.LENGTH_SHORT).show();
                }
                else if(Integer.parseInt(tvtotalPennalty.getText().toString().trim()) < 1)
                {
                    Toast.makeText(this, "Select defect name", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(String.valueOf(qc_pass_qtyET.getText().toString().trim()))){
                    qc_pass_qtyET.setError("Fill the credential");
                }else if(TextUtils.isEmpty(String.valueOf(rejectET.getText().toString().trim()))){
                    rejectET.setError("Fill the credential");
                }else if(TextUtils.isEmpty(String.valueOf(pro_qtyTV.getText().toString().trim()))){
                    pro_qtyTV.setError("Fill the credential");
                }else if(rl.equals("0.0"))
                {
                    rollLength.setError("Fill the credential");
                }
                else {
                    statusSpinnerItem();
                    postDataToServer();
                }

                //postDataToServer();

                break;
            case R.id.btnTryAgain:
                Intent intentC = new Intent(V1_FinishFabricActivity.this, V1_QRBarcodeScannerActivity.class);
                intentC.putExtra("userId", userId);
                intentC.putExtra("url", urladdress);
                intentC.putExtra("s", savemenu);
                intentC.putExtra("u", updatemenu);
                intentC.putExtra("qc", qc_entry);
                startActivity(intentC);

                break;
//            case R.id.scanButton:
//
//                //startActivity(new Intent(this, GreyFabricActivity.class));
//                break;
            case R.id.TVDate:
                datePickerMethod();
                break;
//            case R.id.finishDateBtn:
//                masterdatePickerMethod();
//                break;
            case R.id.rollnumberEt:
                calculationYds();
                break;
            case R.id.qcnameET:
                calculationYds();
                break;
            case R.id.roll_width_KgET:
                calculationYds();
                break;
            case R.id.roll_lengthET:
                calculationYds();
                break;
            /*case R.id.rejectET:
                calculationYds();
                break;*/
            case R.id.constructionTV:
                calculationYds();
                break;
            case R.id.gsmTV:
                calculationYds();
                break;
            case R.id.diaTV:
                calculationYds();
                break;
            case R.id.mcDiaTV:
                calculationYds();
                break;
            case R.id.colorTV:
                calculationYds();
                break;
            case R.id.yarn_countTV:
                calculationYds();
                break;
            case R.id.yarnLotTV:
                calculationYds();
                break;


            default:
                break;
        }
    }


    private void postDataToServer() {
        checkNetworkConnection();
        JSONObject save_obj = new JSONObject();
        JSONObject data_obj = new JSONObject();
        JSONObject index_obj = new JSONObject();
        JSONArray dtls_arr = new JSONArray();

        try {
            save_obj.put("status",true);
            save_obj.put("mode", mode);
            save_obj.put("MST_ID", mst_id);
            save_obj.put("PROD_ID", prod_id);
            save_obj.put("TRANS_ID", trans_id);
            save_obj.put("DTLS_ID", dtls_id);
            save_obj.put("QC_MST_ID", qc_mst_id);

            save_obj.put("UPDATE_ID", updatedID);

            index_obj.put("BARCODE_NO",fBarcode);
            index_obj.put("BATCH_ID", fbatch_id);
            index_obj.put("BATCH_NO", fbatch);
            index_obj.put("BODY_PART_ID", fBodyPart_id);
            index_obj.put("BOOKING_NO", fBooking_no);
            index_obj.put("BOOKING_WITHOUT_ORDER", fBookingwithoutOrder);
            index_obj.put("COMPANY_ID", company_id);
            index_obj.put("SERVICE_COMPANY", 1);
            index_obj.put("SOURCE", 1);
            index_obj.put("SERVICE_LOCATION",11);
            index_obj.put("LOCATION",18);
            index_obj.put("MACHINE_ID",machineNameId);
            index_obj.put("SHIFT",shiftId);
            index_obj.put("COLOR",fcolor);
            index_obj.put("ROLL_WIDTH",rollWidhInch.getText().toString());
            index_obj.put("ROLL_WEIGHT",fqcpass);
            index_obj.put("ROLL_LENGTH",rollLength.getText().toString());
            index_obj.put("CONS_COMP",fbodyPart);
            index_obj.put("DETER_ID",fDeter_id);
            index_obj.put("DIA", diaTV.getText().toString());
            index_obj.put("DIA_TYPE",fDia_type);
            index_obj.put("GSM",gsmET.getText().toString());
            index_obj.put("IS_SALES_ID",fIsSalesId);
            index_obj.put("ORDER_ID",fOrdetId);
//        index_obj.put("QC_PASS_QTY",qc_pass_qtyET.getText().toString());
//        index_obj.put("QC_PASS_QTY",fProductionQty);
            index_obj.put("QC_PASS_QTY",pro_qtyTV.getText().toString());
            index_obj.put("REJECT_QTY",String.valueOf(rejectET.getText().toString()));
            index_obj.put("ROLL_ID",fRollId);
            index_obj.put("ROLL_NO",frollNo);
            index_obj.put("ROLL_WGT",fProductionQty);
            index_obj.put("WGT_LOST",wigt_lost.getText().toString());
            index_obj.put("RECEIVE_DATE",dateTV.getText().toString());
            index_obj.put("COMMENTS", etcomments.getText().toString());
            index_obj.put("INSERTED_BY", userId);
            index_obj.put("UPDATED_BY", userId);
            index_obj.put("ROLL_STATUS",rollstatus);
            index_obj.put("TOTAL_PENALTY_POINT",tvtotalPennalty.getText().toString());
            index_obj.put("TOTAL_POINT",tvpoint.getText().toString());
            index_obj.put("FABRIC_GRADE", tvfebricGrade.getText().toString());


            data_obj.put("index",index_obj);


            if(gradeArray != null && gradeArray.length > 0) {
                for (int i = 0; i < gradeArray.length; i++) {

                    if (modelArrayList.get(i).getQcDefectNumber() > 0) {
                        JSONObject dtls_obj = new JSONObject();
                        dtls_obj.put("DEFECT_ID", modelArrayList.get(i).getDefectID());
                        dtls_obj.put("COUNT", modelArrayList.get(i).getQcItemNumber());
                        dtls_obj.put("INCH_ID", modelArrayList.get(i).getSpinneritem());
                        dtls_obj.put("PENALTY", modelArrayList.get(i).getQcDefectNumber());
                        dtls_arr.put(dtls_obj);
                    }

                }
            }

            data_obj.put("list_data",dtls_arr);

            save_obj.put("data", data_obj);
            Log.d(TAG, "buidJsonObject: ############"+save_obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, save_obj.toString());

        progressBar.setVisibility(View.VISIBLE);
        saveBtn.setEnabled(false);
        apiInterface.saveupdatefinishFabriccall(body).enqueue(new Callback<V1_DataSaveResponse>() {
            @Override
            public void onResponse(Call<V1_DataSaveResponse> call, Response<V1_DataSaveResponse> response) {
                progressBar.setVisibility(View.GONE);
                saveBtn.setEnabled(true);
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful()){
                    Toast.makeText(V1_FinishFabricActivity.this, response.body().getResultset(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(V1_FinishFabricActivity.this, V1_QRBarcodeScannerActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("url", urladdress);
                    intent.putExtra("s", savemenu);
                    intent.putExtra("u", updatemenu);
                    intent.putExtra("qc", qc_entry);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<V1_DataSaveResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                saveBtn.setEnabled(true);
                DialogHelper.showErrorDialog(V1_FinishFabricActivity.this, "Message", "Something went wrong.");
            }
        });
    }

    private void showMessageResult(String result) {
        alertDialogBuilder.setTitle("Save Update");

        alertDialogBuilder.setMessage(result);
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setNegativeButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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


    private void statusSpinnerItem() {
        if(rollstatus == 1)
        {
            double roll_kg = Double.parseDouble(rejectET.getText().toString());
            if(  roll_kg <= fProductionQty)
            {
//                fProductionQty = fProductionQty - roll_kg;
                fProductionQty = Double.parseDouble(String.valueOf(fproqc)) - roll_kg;
            }
        }

    }

    private void setPostRequestContent(HttpURLConnection conn, JSONObject jsonObject) throws IOException {

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonObject.toString());
        Log.i(V1_FinishFabricActivity.class.toString(), jsonObject.toString());
        writer.flush();
        writer.close();
        os.close();
    }


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
            dateTV.setText(dayOfMonth+"-"+month+"-"+year);
            date = String.format("%d-%d-%d", dayOfMonth, month, year);
            Toast.makeText(V1_FinishFabricActivity.this, date, Toast.LENGTH_SHORT).show();
        }
    };



    private void logoutUser() {
        session.setLogin(false);
        dbAdapter.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(V1_FinishFabricActivity.this, V1_LoginActivity.class);
        startActivity(intent);
        finish();
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
                logoutUser();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(V1_FinishFabricActivity.this, V1_HomeActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("url", urladdress);
        intent.putExtra("s", savemenu);
        intent.putExtra("u", updatemenu);
        intent.putExtra("qc", qc_entry);
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

}