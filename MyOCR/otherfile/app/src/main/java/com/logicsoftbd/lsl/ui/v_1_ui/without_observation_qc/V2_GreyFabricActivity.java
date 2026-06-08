package com.logicsoftbd.lsl.ui.v_1_ui.without_observation_qc;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyFabricModelClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_QcModelRND;
import com.logicsoftbd.lsl.data.prefs.SessionManager;
import com.logicsoftbd.lsl.ui.process.scanprocess.ScannerActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_QRBarcodeScannerActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_ScannerActivity;
import com.logicsoftbd.lsl.utils.VerticalSpacingItemDecorator;
import com.logicsoftbd.lsl.viewModel.GrayFabricViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class V2_GreyFabricActivity extends AppCompatActivity implements View.OnClickListener, V1_GrayFabricDefectRecyclerViewAdapter.OnIncrementHeadListener, V1_GrayFabricDefectRecyclerViewAdapter.OnDecrementHeadListener, V1_GrayFabricDefectRecyclerViewAdapter.OnSpinnerListener{
    private static final String TAG = "V2_GreyFabricActivity";
    private String currentDate, qc_observation_status = "SHOW OBSERVATION";
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private Button saveBtn, tryAginBtn, observ_btn, datePick, scaneBtn;
    private GridView gridView, obs_gridView;
    private RecyclerView _defectRecyclerView;
    public TextView tvtotalPennalty, tvpoint, tvfebricGrade, etRoll, etRollLength, tvErrorMessag, tvConstruction,
            tvGSM, tvDia, tvqcPassQty, tvMCDia, tvColor, tvYarnCount, tvYarnLot, tvSpinning, tvUserName, tvMachineNo, tvFileNo, tvRefNo, tvBuyerName, tvProgramNo;
    private EditText etQcName, qrcodeEt, etRollWidthInch, etRollKg, etRejectQc, etcomments;
    private Spinner statusSpinner;
    private String mode, barcodeNumber, barcodeNumInput, dia, color, yarnCount, yarnLot, spinningmill, date, comments, updateDate, updateQcName, updateGrade,
            urladdressChk, urladdress, urlString, qc_entry, construction_C, userId, username;
    private long barcode;
    public int roll_inch = 0;
    private int Year, Month, Day, dtl_id, bayer_id, company_id, rollNumber, rollMaintain, rollId, gsm, mc_dia, rollstatus, updateRollWidthInch, updaterollstatus, updatedID, savemenu, updatemenu = 0;
    private boolean status_obs = false;
    public Double roll_w_kg, yds = 0.0;
    private String base_url = "";
    private double rejectQty, updateYds = 0;
    //update
    public double updateTotalPoint = 0;
    public final ArrayList<String> gradename = new ArrayList<>();
    public final ArrayList<String> defactname = new ArrayList<>();
    public final ArrayList<Integer> defectID = new ArrayList<>();
    public final ArrayList<String> defectInchName = new ArrayList<>();
    public final ArrayList<Integer> defectInch = new ArrayList<>();
    public String[] gradeArray, defectInchNameArray;
    public final ArrayList<Integer> updatedefectCount = new ArrayList<>();
    public final ArrayList<Integer> updateDefectItem = new ArrayList<>();
    public final ArrayList<Integer> updateDefect = new ArrayList<>();
    public Integer[] updateDefectCountArry, updateDefectArry, updateDefectItemArry, gradeIDArray;


    public static Integer[] saveDefectItemArry = {6,4,4,4,4, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,0,4,0,0,0,0,0,0,0,0,0,0,0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private V1_QcModelRND qcModelRND;
    private SessionManager session;

    private View view;
    public static ArrayList<V1_QcModelRND> modelArrayList;
//    private V2_CustomGridAdapter customGridAdapter;
    private V1_GrayFabricDefectRecyclerViewAdapter v1_defectRecyclerViewAdapter;
    //Retrofit
    private ProgressBar progressBar;
    private GrayFabricViewModel grayFabricViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_grey_fabric);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        grayFabricViewModel = new ViewModelProvider(this).get(GrayFabricViewModel.class);

        initializationMethod();
    }
    @SuppressLint("SimpleDateFormat")
    private void initializationMethod() {
        qrcodeEt = findViewById(R.id.barcodeET);
        //scaneBtn = findViewById(R.id.scanButton);
        datePick = findViewById(R.id.datepicker);
//        gridView = findViewById(R.id.gridViewInfo);
        _defectRecyclerView = findViewById(R.id.defectRecyclerView);
        obs_gridView = findViewById(R.id.obs_gridViewInfo);
        saveBtn = findViewById(R.id.btnSave);

        //refreshBtn = findViewById(R.id.btnrefresh);
        tryAginBtn = findViewById(R.id.btnTryAgain);
        observ_btn = findViewById(R.id.observation_btn);
        observ_btn.setText(qc_observation_status);
        observ_btn.setOnClickListener(this);

        saveBtn.setOnClickListener(this);
        tryAginBtn.setOnClickListener(this);

        //summery
        tvtotalPennalty = findViewById(R.id.penaltyPointTV);
        tvpoint = findViewById(R.id.toalPointTV);
        tvfebricGrade = findViewById(R.id.febricGradeTV);

        //resultShet
        etRoll = findViewById(R.id.rollnumberEt);
        etQcName = findViewById(R.id.qcnameET);
        etRollWidthInch = findViewById(R.id.roll_Width_incET);
        etRollKg = findViewById(R.id.roll_width_KgET);
        etRollLength = findViewById(R.id.roll_lengthET);
        tvErrorMessag = findViewById(R.id.errorMessage);
        etRejectQc = findViewById(R.id.rejectET);
        etRejectQc.setOnClickListener(this);
        etcomments = findViewById(R.id.commentET);

        tvConstruction = findViewById(R.id.constructionTV);
        tvGSM = findViewById(R.id.gsmTV);
        tvDia = findViewById(R.id.diaTV);
        tvqcPassQty = findViewById(R.id.qcPassQtyTV);
        tvMCDia = findViewById(R.id.mcDiaTV);
        tvColor = findViewById(R.id.colorTV);
        tvYarnCount = findViewById(R.id.yarn_countTV);
        tvYarnLot = findViewById(R.id.yarnLotTV);
        tvSpinning = findViewById(R.id.spinningTV);
        statusSpinner = findViewById(R.id.spinnerQc);
        tvUserName = findViewById(R.id.userNameTV);
        tvMachineNo = findViewById(R.id.machineNoTV);
        tvFileNo = findViewById(R.id.fileNoTV);
        tvRefNo = findViewById(R.id.refNoTV);
        tvBuyerName = findViewById(R.id.buyerNameTV);
        tvProgramNo = findViewById(R.id.programNoTV);

        etRejectQc.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(!etRejectQc.getText().toString().isEmpty()){
                    tvqcPassQty.setText(String.valueOf(roll_w_kg - Double.parseDouble(etRejectQc.getText().toString())));
                    if(roll_w_kg < Double.parseDouble(etRejectQc.getText().toString())){
                        showAlertMessage("Reject can't greater than Production Quantity.", 1);
                    }
                }else{
                    tvqcPassQty.setText(String.valueOf(roll_w_kg - 0));
                }
            }
        });

        progressBar = findViewById(R.id.loadingProgress);

        ArrayAdapter<CharSequence> adapterhole = ArrayAdapter.createFromResource(this, R.array.statusroll
                , R.layout.spinner_item);
        statusSpinner.setAdapter(adapterhole);

        datePick.setOnClickListener(this);
        etRoll.setOnClickListener(this);
        etQcName.setOnClickListener(this);
        etRollKg.setOnClickListener(this);
        etRollLength.setOnClickListener(this);
        tvConstruction.setOnClickListener(this);
        tvGSM.setOnClickListener(this);
        tvDia.setOnClickListener(this);
        tvMCDia.setOnClickListener(this);
        tvColor.setOnClickListener(this);
        tvYarnCount.setOnClickListener(this);
        tvYarnLot.setOnClickListener(this);
        tvSpinning.setOnClickListener(this);

        qcModelRND = new V1_QcModelRND();

        Intent intent = getIntent();

        String resultS = intent.getStringExtra("result");
        urladdressChk = intent.getStringExtra("url");
        savemenu = intent.getIntExtra("s", 0);
        updatemenu = intent.getIntExtra("u", 0);
        qc_entry = intent.getStringExtra("qc");

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = (_preferences.getString("login_userid", ""));
        username = _preferences.getString("login_username", "");
        etQcName.setText(username);
        barcodeNumber = resultS;

        if(resultS != null){
            qrcodeEt.setBackgroundColor(getResources().getColor(R.color.white));
            qrcodeEt.setText(resultS);
        }
        else {
            saveBtn.setVisibility(View.GONE);
            tryAginBtn.setVisibility(View.GONE);
        }
        sendRequestToServer();

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = simpleDateFormat.format(calendar.getTime());

        datePick.setText(currentDate);

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                        rollstatus = 0;
                        break;
                    case 1:
                        rollstatus = 1;
                        break;
                    case 2:
                        rollstatus = 2;
                        break;
                    case 3:
                        rollstatus = 3;
                        double roll_kg = Double.parseDouble(etRejectQc.getText().toString());
                        if(  roll_kg >= roll_w_kg)
                        {
                            etRejectQc.setText(String.valueOf(roll_w_kg));
                        }
                        etRejectQc.setText(String.valueOf(roll_w_kg));
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void sendRequestToServer() {
        progressBarState();
        grayFabricViewModel.getGrayFabricBarcodeDetailsResponse(barcodeNumber).observe(this, apiResponse -> {
            if(apiResponse != null){
//                gridView.setVisibility(View.VISIBLE);
                mode = apiResponse.getData().getIndex().getMODE();
                if(mode.equals("save"))
                {
                    savemethod(apiResponse);
                    saveBtn.setText("SAVE");
                }
                else if(mode.equals("update")){
                    savemethod(apiResponse);
                    updateMethod(apiResponse);
                    saveBtn.setText("UPDATE");
                }
            }else{
                tvErrorMessag.setVisibility(View.VISIBLE);
                tvErrorMessag.setText(apiResponse.getMsg());
                showAlertMessage(apiResponse.getMsg(), 0);
            }
        });
    }

    private void updateMethod(V1_GreyFabricModelClass apiResponse) {
        updateDate = apiResponse.getData().getIndex().getQCDATE();
        updateQcName = apiResponse.getData().getIndex().getQCNAME();
        updateRollWidthInch = Integer.parseInt(apiResponse.getData().getIndex().getROLLINCH());
        updateYds = Double.parseDouble(apiResponse.getData().getIndex().getROLLYDS());
        updateTotalPoint = Double.parseDouble(apiResponse.getData().getIndex().getTOTALPOINT());
        updateGrade = apiResponse.getData().getIndex().getFABRICGRADE();
        rejectQty = Double.parseDouble(apiResponse.getData().getIndex().getREJECTQNTY());
        updaterollstatus = Integer.parseInt(apiResponse.getData().getIndex().getROLLSTATUS());
        updatedID = Integer.parseInt(apiResponse.getData().getIndex().getUPDATEID());
        comments = apiResponse.getData().getIndex().getCOMMENTS();

        datePick.setText(updateDate);
        etQcName.setText(updateQcName);
        etRollWidthInch.setText(String.valueOf(updateRollWidthInch));
        etRejectQc.setText(String.valueOf(rejectQty));
        etRollLength.setText(String.format("%.4f", updateYds));
        tvfebricGrade.setText(updateGrade);
        etcomments.setText(comments);

        statusSpinner.setSelection(updaterollstatus);
    }

    private void savemethod(V1_GreyFabricModelClass apiResponse) {
        barcode = apiResponse.getData().getIndex().getBARCODENO();
        dtl_id = Integer.parseInt(apiResponse.getData().getIndex().getDTLSID());
        bayer_id = Integer.parseInt(apiResponse.getData().getIndex().getBUYERID());
        company_id = Integer.parseInt(apiResponse.getData().getIndex().getCOMPANYID());
        rollNumber = Integer.parseInt(apiResponse.getData().getIndex().getROLLNO());
        roll_w_kg = Double.valueOf(apiResponse.getData().getIndex().getROLLKG());
        roll_inch = Integer.parseInt(apiResponse.getData().getIndex().getROLLINCH());
        construction_C = apiResponse.getData().getIndex().getCONSTRUCTION();
        rollMaintain = Integer.parseInt(apiResponse.getData().getIndex().getROLLMAINTAINED());
        rollId = Integer.parseInt(apiResponse.getData().getIndex().getROLLID());
        gsm = Integer.parseInt(apiResponse.getData().getIndex().getGSM());
        dia = apiResponse.getData().getIndex().getDIA();
        mc_dia = Integer.parseInt(apiResponse.getData().getIndex().getMCDIA());
        color = apiResponse.getData().getIndex().getCOLOR();
        yarnCount = apiResponse.getData().getIndex().getYARNCOUNT();
        yarnLot = String.valueOf(apiResponse.getData().getIndex().getYARNLOT());
        spinningmill = apiResponse.getData().getIndex().getSPINNINGMILL();



        V1_GreyFabricModelClass.Grade gradeName;
        List<V1_GreyFabricModelClass.Grade> grades = apiResponse.getData().getIndex().getArrayRefData().getGrade();
        for(V1_GreyFabricModelClass.Grade d : grades)
        {
            gradeName = d;
            final V1_GreyFabricModelClass.Grade finalName = gradeName;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gradename.add(String.valueOf(finalName.getGrade()));
                }
            });
        }

        V1_GreyFabricModelClass.Defect defactName;
        List<V1_GreyFabricModelClass.Defect> defactGrades = apiResponse.getData().getIndex().getArrayRefData().getDefect();
        for(V1_GreyFabricModelClass.Defect d : defactGrades)
        {
            defactName = d;
            final V1_GreyFabricModelClass.Defect finalName = defactName;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    defactname.add(String.valueOf(finalName.getDEFECTNAME()));
                }
            });
        }

        V1_GreyFabricModelClass.Defect defactID;
        List<V1_GreyFabricModelClass.Defect> ID = apiResponse.getData().getIndex().getArrayRefData().getDefect();
        for(V1_GreyFabricModelClass.Defect d : ID)
        {
            defactID = d;
            final V1_GreyFabricModelClass.Defect finalID = defactID;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    defectID.add(finalID.getID());
                }
            });
        }

        V1_GreyFabricModelClass.Defect udatedefactount;
        List<V1_GreyFabricModelClass.Defect> updateCount = apiResponse.getData().getIndex().getArrayRefData().getDefect();
        for(V1_GreyFabricModelClass.Defect d : updateCount)
        {
            udatedefactount = d;
            final V1_GreyFabricModelClass.Defect finalDefectCount = udatedefactount;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updatedefectCount.add(finalDefectCount.getDEFECTCOUNT());
                }
            });
        }

        V1_GreyFabricModelClass.Defect updatedefect;
        List<V1_GreyFabricModelClass.Defect> updatedefactList = apiResponse.getData().getIndex().getArrayRefData().getDefect();
        for(V1_GreyFabricModelClass.Defect d : updatedefactList)
        {
            updatedefect = d;
            final V1_GreyFabricModelClass.Defect finalDefect = updatedefect;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateDefect.add(finalDefect.getPENALTYPOINT());
                }
            });
        }
        V1_GreyFabricModelClass.Defect updateSpinner;
        List<V1_GreyFabricModelClass.Defect> updateSpinnerList = apiResponse.getData().getIndex().getArrayRefData().getDefect();
        for(V1_GreyFabricModelClass.Defect d : updateSpinnerList)
        {
            updateSpinner = d;
            final V1_GreyFabricModelClass.Defect finalSpinnerList = updateSpinner;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateDefectItem.add(finalSpinnerList.getFOUNDININCH());
                }
            });
        }

        try{
            modelArrayList = getModel();
        }catch (NullPointerException e){

        }

//        customGridAdapter = new V2_CustomGridAdapter(V2_GreyFabricActivity.this);
//        gridView.setAdapter(customGridAdapter);

        initRecyclerView();

        etRoll.setText(String.valueOf(rollNumber));
        etRollKg.setText(String.valueOf(roll_w_kg));
        tvConstruction.setText(construction_C);
        tvGSM.setText(String.valueOf(gsm));
        tvDia.setText(String.valueOf(dia));
        tvMCDia.setText(String.valueOf(mc_dia));
        tvColor.setText(color);
        tvYarnCount.setText(yarnCount);
        tvYarnLot.setText(yarnLot);
        tvSpinning.setText(spinningmill);
        tvUserName.setText(String.valueOf(username));
        tvMachineNo.setText(apiResponse.getData().getIndex().getMachine_no());
        tvFileNo.setText(apiResponse.getData().getIndex().getFile_no());
        tvRefNo.setText(apiResponse.getData().getIndex().getRef_no());
        tvBuyerName.setText(apiResponse.getData().getIndex().getBuyer_name());
        tvProgramNo.setText(apiResponse.getData().getIndex().getProgram_no());
        etRollWidthInch.setText(apiResponse.getData().getIndex().getDIA());
    }
    public void calculationYds() {
        String inch = etRollWidthInch.getText().toString().trim();
        String sum = tvtotalPennalty.getText().toString().trim();

        boolean isDiaNumeric = false;
        try {
            int numericValue = Integer.parseInt(inch);
            isDiaNumeric = true;
        } catch (NumberFormatException e) {
            isDiaNumeric = false;
        }

        if(TextUtils.isEmpty(sum))
        {
            tvtotalPennalty.setError("Fill the credential");
        } else if(TextUtils.isEmpty(inch))
        {
            etRollWidthInch.setError("Fill the credential");

        } else if(!isDiaNumeric){
            showAlertMessage("Dia is not numeric. Please make it numeric.", 0);
        } else {
            yds = ((Double.parseDouble(String.valueOf(roll_w_kg))* 1000) / (Double.parseDouble(String.valueOf(gsm)) * Double.parseDouble(inch)* 0.0254) * 1.09361);

            etRollLength.setText((String.format("%.4f", yds)));
            double totalPanalty = ((36 * 100 * Integer.parseInt(sum)) / (Double.parseDouble(inch)* yds));

            qcModelRND.setTotalPoint(totalPanalty);
            if(String.valueOf(qcModelRND.getTotalPoint()).equals("Infinity"))
            {
                tvpoint.setText(String.valueOf(0));
            }
            else {
                tvpoint.setText(String.format("%.4f", qcModelRND.getTotalPoint()));
            }

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
            if(mode.equals("save"))
            {
                qcModel.setSpinneritem(saveDefectItemArry[i]);
            }
            else {
                qcModel.setSpinneritem(updateDefectItemArry[i]);
            }

            list.add(qcModel);
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSave:
                if (TextUtils.isEmpty(etQcName.getText().toString().trim())) {
                    etQcName.setError("Fill the credential");
                    return;
                }else if(rollstatus == 0)
                {
                    Toast.makeText(this, "Roll status not selected.", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(etRollWidthInch.getText().toString().trim()))
                {
                    etRollWidthInch.setError("Fill the credential");
                }
                else if(Integer.parseInt(etRollWidthInch.getText().toString()) < 1)
                {
                    etRollWidthInch.setError("Can't zero");
                }
                else {
                    postDataToServer();
                }
                break;
            case R.id.btnTryAgain:
                Intent intentC = new Intent(V2_GreyFabricActivity.this, V1_ScannerActivity.class);
                intentC.putExtra("userId", userId);
                intentC.putExtra("url", urladdress);
                intentC.putExtra("s", savemenu);
                intentC.putExtra("u", updatemenu);
                intentC.putExtra("qc", qc_entry);
                startActivity(intentC);

                break;
            case R.id.datepicker:
                datePickerMethod();
                break;
            case R.id.rollnumberEt:
            case R.id.qcnameET:
            case R.id.roll_width_KgET:
            case R.id.roll_lengthET:
            case R.id.constructionTV:
            case R.id.gsmTV:
            case R.id.diaTV:
            case R.id.mcDiaTV:
            case R.id.colorTV:
            case R.id.yarn_countTV:
            case R.id.yarnLotTV:
                calculationYds();
                break;
            case R.id.observation_btn:
                observation_view();
                break;

            default:
                break;
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _defectRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(2);
        _defectRecyclerView.addItemDecoration(itemDecorator);
        v1_defectRecyclerViewAdapter = new V1_GrayFabricDefectRecyclerViewAdapter(modelArrayList, this, this, this, this);
        _defectRecyclerView.setAdapter(v1_defectRecyclerViewAdapter);
        v1_defectRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void observation_view() {
        if(observ_btn.getText().toString().equals("SHOW OBSERVATION")){
            observ_btn.setText("FOUR POINT QC");
            gridView.setVisibility(View.GONE);
            obs_gridView.setVisibility(View.VISIBLE);
        }else {
            observ_btn.setText("SHOW OBSERVATION");
            obs_gridView.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
        }
    }

    private void postDataToServer() {
        checkNetworkConnection();
        statusSpinnerItem();
        JSONObject save_obj = new JSONObject();
        JSONObject data_obj = new JSONObject();
        JSONObject index_obj = new JSONObject();
        JSONArray dtls_arr = new JSONArray();
        try {
            save_obj.put("status",true);
            save_obj.put("mode", mode);
            save_obj.put("UPDATE_ID", updatedID);

            index_obj.put("BARCODE_NO",barcode);
            index_obj.put("BUYER_ID", bayer_id);
            index_obj.put("COMPANY_ID", company_id);
            index_obj.put("DTLS_ID", dtl_id);
            index_obj.put("ROLL_MAINTAINED",rollMaintain);
            index_obj.put("QC_DATE",datePick.getText().toString());
            index_obj.put("ROLL_ID",rollId);
            index_obj.put("ROLL_NO",rollNumber);
            index_obj.put("QC_NAME",etQcName.getText().toString());
            index_obj.put("ROLL_INCH", etRollWidthInch.getText().toString());
            index_obj.put("ROLL_KG",roll_w_kg);
            index_obj.put("ROLL_YDS",yds);
            index_obj.put("TOTAL_PENALTY_POINT",tvtotalPennalty.getText().toString());
            index_obj.put("TOTAL_POINT",tvpoint.getText().toString());
            index_obj.put("INSERTED_BY",userId);
            index_obj.put("UPDATED_BY",userId);
            index_obj.put("UPDATE_ID",updatedID);
            index_obj.put("REJECT_QNTY",etRejectQc.getText().toString());
            index_obj.put("FABRIC_GRADE", tvfebricGrade.getText().toString());
            index_obj.put("ROLL_STATUS", rollstatus);
            index_obj.put("COMMENTS", etcomments.getText().toString());

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
            Log.d(TAG, "postDataToServer: ########"+save_obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, save_obj.toString());
        progressBarState();
        grayFabricViewModel.postGrayFabricBarcodeDetailsResponse(body).observe(this, apiResponse -> {
            if(apiResponse != null) {
                Toast.makeText(V2_GreyFabricActivity.this, apiResponse.getResultset(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(V2_GreyFabricActivity.this, V1_ScannerActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("url", urladdress);
                intent.putExtra("s", savemenu);
                intent.putExtra("u", updatemenu);
                intent.putExtra("qc", qc_entry);
                startActivity(intent);
            }else{
                Toast.makeText(V2_GreyFabricActivity.this, "Failed, Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isConnected = false;
        if (networkInfo != null && (isConnected = networkInfo.isConnected())) {

        } else {
            Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show();
        }
        return isConnected;
    }

    private void statusSpinnerItem() {
        if(rollstatus == 1)
        {
            double roll_kg = 0;
            if(!etRejectQc.getText().toString().isEmpty()){
                roll_kg = Double.parseDouble(etRejectQc.getText().toString());
            }
        }
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
            datePick.setText(dayOfMonth+"-"+month+"-"+year);
            date = String.format("%d-%d-%d", dayOfMonth, month, year);
            Toast.makeText(V2_GreyFabricActivity.this, date, Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(V2_GreyFabricActivity.this, V1_ScannerActivity.class);
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

    private void progressBarState() {
        grayFabricViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showAlertMessage(String msg, int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(V2_GreyFabricActivity.this);
        builder.setTitle("Message")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Try Again", (dialog, which) -> {
                    if(i == 0){
                        Intent intentC = new Intent(V2_GreyFabricActivity.this, V1_ScannerActivity.class);
                        intentC.putExtra("userId", userId);
                        intentC.putExtra("url", urladdress);
                        intentC.putExtra("s", savemenu);
                        intentC.putExtra("u", updatemenu);
                        intentC.putExtra("qc", qc_entry);
                        startActivity(intentC);
                    }else{
                        etRejectQc.setText("");
                        dialog.dismiss();
                    }

                });
        AlertDialog dialog  = builder.create();
        dialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onIncrementHeadClick(int position, View v) {
        int number = modelArrayList.get(position).getQcItemNumber() + 1;
        modelArrayList.get(position).setQcItemNumber(number);
        v1_defectRecyclerViewAdapter.notifyDataSetChanged();
        defectCountNumber(position);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDecrementHeadClick(int position, View v) {
        int number = modelArrayList.get(position).getQcItemNumber() - 1;
        if(number <= 0)
            number = 0;
        modelArrayList.get(position).setQcItemNumber(number);
        v1_defectRecyclerViewAdapter.notifyDataSetChanged();
        defectCountNumber(position);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onSpinnerHeadClick(int position, View v, int selectedItem) {
//        Toast.makeText(this, String.valueOf(position+" "+selectedItem), Toast.LENGTH_SHORT).show();

        defectCountNumber(position);
    }

    private void defectCountNumber(int position) {
//        defectItemName = holder.mSpinner.getSelectedItem().toString();
//        qcItemName = holder.tvItemName.getText().toString();
        int numberItem = Integer.parseInt(String.valueOf(modelArrayList.get(position).getQcItemNumber()));
        if(modelArrayList.get(position).getSpinneritem() > 0){
            int defectnumber = numberItem*modelArrayList.get(position).getSpinneritem();
//        textColorChange(defectnumber, holder);
            modelArrayList.get(position).setQcDefectNumber(defectnumber);
            calculationOfDefect();
        }

//        holder.tvDefectCount.setText(String.valueOf(defectnumber));
//        V1_GreyFabricActivity.modelArrayList.get(position).setQcDefectNumber(defectnumber);
        //GreyFabricActivity.modelArrayList.get(position).setQcDefectItemName(defectItemName);
//        V1_GreyFabricActivity.modelArrayList.get(position).setQcItemName(qcItemName);

    }
    public void calculationOfDefect() {


        double inch = 0;
        //double yds = 0;
//        double totalPanalty = 0;
        inch = roll_inch;
        //yds = ((GreyFabricActivity)context).yds;

        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < gradeArray.length; i++) {
            list.add(modelArrayList.get(i).getQcDefectNumber());
        }

        int sum = 0;
        for (int i : list)
        {
            sum += i;
        }

        double totalPanalty = 0;
        totalPanalty = ((36*100*sum) / (inch * (yds)));

        if(sum == 0){
            tvtotalPennalty.setTextColor(Color.BLACK);
            tvpoint.setTextColor(Color.BLACK);

        }
        else {
            tvtotalPennalty.setTextColor(Color.RED);
            tvpoint.setTextColor(Color.RED);

        }
        tvtotalPennalty.findViewById(R.id.penaltyPointTV);
        tvtotalPennalty.setText(String.valueOf(sum));
        if((yds) == 0){
            tvpoint.setText("0.00");
        }

        calculationYds();
        qcModelRND = new V1_QcModelRND();
        qcModelRND.setTotalPoint(totalPanalty);



        //fabricGradeMethod();
        tvfebricGrade.findViewById(R.id.febricGradeTV);

    }
}