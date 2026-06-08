package com.logicsoftbd.lsl.ui.v_1_ui.without_observation_qc;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.db.v1_db.helper.DBAdapter;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DataSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyFabricModelClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_QcModelRND;
import com.logicsoftbd.lsl.data.prefs.SessionManager;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.v_1_ui.login.V1_LoginActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_ScannerActivity;
import com.logicsoftbd.lsl.utils.ApiUtils;
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

public class V1_GreyFabricActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "V1_GreyFabricActivity";
    private String currentDate, qc_observation_status = "SHOW OBSERVATION";
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private Button saveBtn, tryAginBtn, observ_btn, datePick, scaneBtn;
    private GridView gridView, obs_gridView;
    private int Year, Month, Day;
    public TextView tvtotalPennalty, tvpoint, tvfebricGrade, etRoll, etRollLength, tvErrorMessag, tvConstruction,
            tvGSM, tvDia, tvqcPassQty, tvMCDia, tvColor, tvYarnCount, tvYarnLot, tvSpinning, tvUserName, tvMachineNo, tvFileNo, tvRefNo, tvBuyerName, tvProgramNo, stitchLengthTV, machineGGTV;
    private EditText etQcName, qrcodeEt, etRollWidthInch, etRollKg, etRejectQc, etcomments;
    private Spinner statusSpinner;
    private String mode, barcodeNumber, barcodeNumInput, dia, color, yarnCount, yarnLot, spinningmill, date, comments, updateDate, updateQcName, updateGrade,
            urladdressChk, urladdress, urlString, qc_entry, construction_C, userId, username;
    private long barcode;
    public int roll_inch = 0;
    private int dtl_id, bayer_id, company_id, rollNumber, rollMaintain, rollId, gsm, mc_dia, rollstatus, updateRollWidthInch, updaterollstatus, updatedID, savemenu, updatemenu = 0;
    private boolean status_obs = false;
    public Double roll_w_kg, yds = 0.0;
    private String base_url = "";
    private double rejectQty, updateYds = 0;
    //update
    public double updateTotalPoint = 0;
    public final ArrayList<String> gradename = new ArrayList<>();
    public final ArrayList<String> gradeSerial = new ArrayList<>();
    public final ArrayList<String> defactname = new ArrayList<>();
    public final ArrayList<Integer> defectID = new ArrayList<>();
    public final ArrayList<String> defectInchName = new ArrayList<>();
    public final ArrayList<Integer> defectInch = new ArrayList<>();

    public final ArrayList<String> Obsdefactname = new ArrayList<>();
    public final ArrayList<String> ObsdefectInchName = new ArrayList<>();

    public String[] gradeArray;
    public Integer[] gradeIDArray;
    public String[] defectInchNameArray;

    public final ArrayList<Integer> updatedefectCount = new ArrayList<>();
    public final ArrayList<Integer> updateDefectItem = new ArrayList<>();
    public final ArrayList<Integer> updateDefect = new ArrayList<>();

    public Integer[] updateDefectCountArry;
    public Integer[] updateDefectItemArry;
    public Integer[] updateDefectArry;

    public static Integer[] saveDefectItemArry = {6,4,4,4,4, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,0,4,0,0,0,0,0,0,0,0,0,0,0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0,0,0,0,0,0,0,0,0,0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private V1_QcModelRND qcModelRND;
    private SessionManager session;
    private DBAdapter dbAdapter;
    private View view;
    public static ArrayList<V1_QcModelRND> modelArrayList;
    private V1_CustomGridAdapter customGridAdapter;
    //Retrofit
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;
    private AlertDialog.Builder alertDialogBuilder;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_grey_fabric);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializationMethod();
    }
    private void initializationMethod() {
        qrcodeEt = findViewById(R.id.barcodeET);
        //scaneBtn = findViewById(R.id.scanButton);
        datePick = findViewById(R.id.datepicker);
        gridView = findViewById(R.id.gridViewInfo);
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
        stitchLengthTV = findViewById(R.id.stitchLengthTV);
        machineGGTV = findViewById(R.id.machineGGTV);

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
//                    tvqcPassQty.setText(String.valueOf(roll_w_kg - Double.parseDouble(etRejectQc.getText().toString())));
                    tvqcPassQty.setText(String.valueOf(Double.parseDouble(etRollKg.getText().toString()) - Double.parseDouble(etRejectQc.getText().toString())));
                    if(Double.parseDouble(etRollKg.getText().toString()) < Double.parseDouble(etRejectQc.getText().toString())){
                        showAlertMessage("Reject can't greater than Production Quantity.", 1);
                    }
                }else{
                    tvqcPassQty.setText(String.valueOf(Double.parseDouble(etRollKg.getText().toString()) - 0));
                }
            }
        });

        progressBar = findViewById(R.id.loadingProgress);

        ArrayAdapter<CharSequence> adapterhole = ArrayAdapter.createFromResource(this, R.array.statusroll
                , R.layout.spinner_item);
        statusSpinner.setAdapter(adapterhole);
        statusSpinner.setSelection(0);

        datePick.setOnClickListener(this);
        //scaneBtn.setOnClickListener(this);
        etRoll.setOnClickListener(this);
        etQcName.setOnClickListener(this);
        etRollKg.setOnClickListener(this);
        etRollLength.setOnClickListener(this);
        //etRejectQc.setOnClickListener(this);
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


//        ArrayList<V1_User> loginData = new DBAdapter(this).getLoginData();
//
//        if(urladdressChk != null)
//        {
//            urladdress = urladdressChk;
//            userId = intent.getIntExtra("userId", 0);
//
//        }else {
//            urladdress = loginData.get(0).getUrl();
//            userId = Integer.parseInt(loginData.get(0).getUserId());
//        }

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        base_url = (_preferences.getString("base_url", ""));
        userId = (_preferences.getString("login_userid", ""));
        username = _preferences.getString("login_username", "");

        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);

        etQcName.setText(username);

        barcodeNumber = resultS;
        //barcodeNumber = resultS2;

        if(resultS != null){
            qrcodeEt.setBackgroundColor(getResources().getColor(R.color.white));
            qrcodeEt.setText(resultS);
            /*sendRequestToServer(barcodeNumber);*/
        }
        else {
            saveBtn.setVisibility(View.GONE);
            tryAginBtn.setVisibility(View.GONE);
        }

        sendRequestToServer();

        // session manager
        session = new SessionManager(getApplicationContext());
        dbAdapter = new DBAdapter(this);


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
                        if(!mode.equals("update")){
                            etRejectQc.setText(String.valueOf(roll_w_kg));
                        }
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void sendRequestToServer() {
        progressBar.setVisibility(View.VISIBLE);
        apiInterface.getBarCodeModelClassCall(barcodeNumber).enqueue(new Callback<V1_GreyFabricModelClass>() {
            @Override
            public void onResponse(Call<V1_GreyFabricModelClass> call, Response<V1_GreyFabricModelClass> response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onResponse: "+response.toString());
                if (response.isSuccessful()){
                    if(response.body().getData() != null){
                        gridView.setVisibility(View.VISIBLE);
                        mode = response.body().getData().getIndex().getMODE();
                        if(mode.equals("save"))
                        {
                            savemethod(response);
                            saveBtn.setText("SAVE");
                        }
                        else if(mode.equals("update")){
                            savemethod(response);
                            updateMethod(response);
                            saveBtn.setText("UPDATE");
                        }

                        boolean isDiaNumeric = false;
                        try {
                            int numericValue = Integer.parseInt(response.body().getData().getIndex().getDIA());
                            isDiaNumeric = true;
                        } catch (NumberFormatException e) {
                            isDiaNumeric = false;
                        }

//                         if(!isDiaNumeric){
//                            showAlertMessage("Dia is not numeric. Please make it numeric.", 0);
//                        }
                    }else{
                        tvErrorMessag.setVisibility(View.VISIBLE);
                        tvErrorMessag.setText(response.body().getMsg());
                        showAlertMessage(response.body().getMsg(), 0);
                    }

                }
                else {
                    Toast.makeText(V1_GreyFabricActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }

            private void updateMethod(Response<V1_GreyFabricModelClass> response) {
                updateDate = response.body().getData().getIndex().getQCDATE();
                updateQcName = response.body().getData().getIndex().getQCNAME();
                updateRollWidthInch = Integer.parseInt(response.body().getData().getIndex().getROLLINCH());
                updateYds = Double.parseDouble(response.body().getData().getIndex().getROLLYDS());
                updateTotalPoint = Double.parseDouble(response.body().getData().getIndex().getTOTALPOINT());
                updateGrade = response.body().getData().getIndex().getFABRICGRADE();
                rejectQty = Double.parseDouble(response.body().getData().getIndex().getREJECTQNTY());
                updaterollstatus = Integer.parseInt(response.body().getData().getIndex().getROLLSTATUS());
                updatedID = Integer.parseInt(response.body().getData().getIndex().getUPDATEID());
                comments = response.body().getData().getIndex().getCOMMENTS();

                datePick.setText(updateDate);
                etQcName.setText(updateQcName);
                etRollWidthInch.setText(String.valueOf(updateRollWidthInch));
//                etRejectQc.setText(String.valueOf(rejectQty));
                etRollLength.setText(String.valueOf(String.format("%.4f", updateYds)));
                tvfebricGrade.setText(updateGrade);
                etcomments.setText(comments);

                statusSpinner.setSelection(updaterollstatus);
            }

            private void savemethod(Response<V1_GreyFabricModelClass> response) {
                barcode = response.body().getData().getIndex().getBARCODENO();
                dtl_id = Integer.parseInt(response.body().getData().getIndex().getDTLSID());
                bayer_id = Integer.parseInt(response.body().getData().getIndex().getBUYERID());
                company_id = Integer.parseInt(response.body().getData().getIndex().getCOMPANYID());
                rollNumber = Integer.parseInt(response.body().getData().getIndex().getROLLNO());
                roll_w_kg = Double.valueOf(response.body().getData().getIndex().getROLLKG());
                roll_inch = Integer.parseInt(response.body().getData().getIndex().getROLLINCH());
                construction_C = response.body().getData().getIndex().getCONSTRUCTION();
                rollMaintain = Integer.parseInt(response.body().getData().getIndex().getROLLMAINTAINED());
                rollId = Integer.parseInt(response.body().getData().getIndex().getROLLID());
                gsm = Integer.parseInt(response.body().getData().getIndex().getGSM());
                dia = response.body().getData().getIndex().getDIA();
                mc_dia = Integer.parseInt(response.body().getData().getIndex().getMCDIA());
                color = response.body().getData().getIndex().getCOLOR();
                yarnCount = response.body().getData().getIndex().getYARNCOUNT();
                yarnLot = String.valueOf(response.body().getData().getIndex().getYARNLOT());
                spinningmill = response.body().getData().getIndex().getSPINNINGMILL();



                V1_GreyFabricModelClass.Grade gradeName;
                List<V1_GreyFabricModelClass.Grade> grades = response.body().getData().getIndex().getArrayRefData().getGrade();
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

                V1_GreyFabricModelClass.Grade gradeserial;
                List<V1_GreyFabricModelClass.Grade> gradeSe = response.body().getData().getIndex().getArrayRefData().getGrade();
                for(V1_GreyFabricModelClass.Grade d : grades)
                {
                    gradeserial = d;
                    final V1_GreyFabricModelClass.Grade finalName = gradeserial;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gradeSerial.add(String.valueOf(finalName.getSerial()));
                        }
                    });
                }

                V1_GreyFabricModelClass.Defect defactName;
                List<V1_GreyFabricModelClass.Defect> defactGrades = response.body().getData().getIndex().getArrayRefData().getDefect();
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
                List<V1_GreyFabricModelClass.Defect> ID = response.body().getData().getIndex().getArrayRefData().getDefect();
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
                List<V1_GreyFabricModelClass.Defect> updateCount = response.body().getData().getIndex().getArrayRefData().getDefect();
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
                List<V1_GreyFabricModelClass.Defect> updatedefactList = response.body().getData().getIndex().getArrayRefData().getDefect();
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
                List<V1_GreyFabricModelClass.Defect> updateSpinnerList = response.body().getData().getIndex().getArrayRefData().getDefect();
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

                alertDialogBuilder = new AlertDialog.Builder(V1_GreyFabricActivity.this);
                try{
                    modelArrayList = getModel();
                }catch (NullPointerException e){

                }

                customGridAdapter = new V1_CustomGridAdapter(V1_GreyFabricActivity.this);
                gridView.setAdapter(customGridAdapter);

//                observerCustomGridAdapter = new ObserverCustomGridAdapter(GreyFabricActivity.this);
//                obs_gridView.setAdapter(observerCustomGridAdapter);

                etRoll.setText(String.valueOf(rollNumber));
                double _rejectQnty = Double.parseDouble(response.body().getData().getIndex().getREJECTQNTY());
                double _rollWeight = roll_w_kg + _rejectQnty;
                etRollKg.setText(String.valueOf(_rollWeight));
                rejectQty = Double.parseDouble(response.body().getData().getIndex().getREJECTQNTY());
                etRejectQc.setText(String.valueOf(_rejectQnty));
                tvConstruction.setText(construction_C);
                tvGSM.setText(String.valueOf(gsm));
                tvDia.setText(String.valueOf(dia));
                tvMCDia.setText(String.valueOf(mc_dia));
                tvColor.setText(color);
                tvYarnCount.setText(yarnCount);
                tvYarnLot.setText(yarnLot);
                tvSpinning.setText(spinningmill);
                tvUserName.setText(String.valueOf(username));
                tvMachineNo.setText(response.body().getData().getIndex().getMachine_no());
                tvFileNo.setText(response.body().getData().getIndex().getFile_no());
                tvRefNo.setText(response.body().getData().getIndex().getRef_no());
                tvBuyerName.setText(response.body().getData().getIndex().getBuyer_name());
                tvProgramNo.setText(response.body().getData().getIndex().getProgram_no());
                if(isNumeric(response.body().getData().getIndex().getDIA())){
                    etRollWidthInch.setText(response.body().getData().getIndex().getDIA());
                }
                stitchLengthTV.setText(response.body().getData().getIndex().getSTITCH_LENGTH());
                machineGGTV.setText(response.body().getData().getIndex().getMACHINE_GG());
            }
            @Override
            public void onFailure(Call<V1_GreyFabricModelClass> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                if (t instanceof IOException) {
                    Toast.makeText(V1_GreyFabricActivity.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(V1_GreyFabricActivity.this, String.valueOf(t.getMessage()), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private static boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }

    private void showAlertMessage(String msg, int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(V1_GreyFabricActivity.this);
        builder.setTitle("Message")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Try Again", (dialog, which) -> {
                    if(i == 0){
                        Intent intentC = new Intent(V1_GreyFabricActivity.this, V1_ScannerActivity.class);
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

    public void calculationYds() {
        //double width_inch = Double.parseDouble(etRollWidthInch.getText().toString());
        String inch = etRollWidthInch.getText().toString().trim();
        String sum = tvtotalPennalty.getText().toString().trim();

        if(TextUtils.isEmpty(String.valueOf(sum)))
        {
            tvtotalPennalty.setError("Fill the credential");
        } else if(TextUtils.isEmpty(inch))
        {
            etRollWidthInch.setError("Fill the credential");

        } else {
            try {

                yds = ((Double.parseDouble(String.valueOf(roll_w_kg))* 1000) / (Double.parseDouble(String.valueOf(gsm)) * Double.parseDouble(inch)* 0.0254) * 1.09361);

                etRollLength.setText((String.format("%.4f", yds)));
                Double totalPanalty = ((36 * 100 * Integer.parseInt(sum)) / (Double.parseDouble(inch)* yds));

//            yds = ((Double.parseDouble(String.valueOf(roll_w_kg))* 1000) / (Double.parseDouble(String.valueOf(gsm)) * inchWidth* 0.0254) * 1.09361);
//
//            etRollLength.setText((String.format("%.4f", yds)));
//            Double totalPanalty = ((36 * 100 * Integer.parseInt(sum)) / (inchWidth* yds));
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
//                    tvfebricGrade.setText(String.valueOf(gradename.get(grade)));
                    tvfebricGrade.setText(String.valueOf(gradename.get(grade)));
                    tvfebricGrade.setTextColor(Color.RED);
                }
                else
                {
                    tvfebricGrade.setText("Rejected");
                }
            }catch (Exception e){

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
                String name = null;
//                if(TextUtils.isEmpty(String.valueOf(name))) {
//                    etQcName.setError("Fill the credential");
//                }
//                else {
//                    postDataToServer();
//                }
                if (TextUtils.isEmpty(etQcName.getText().toString().trim())) {
                    etQcName.setError("Fill the credential");
                    return;
                }else if(rollstatus == 0) {
                    Toast.makeText(this, "Roll status not selected.", Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(etRollWidthInch.getText().toString().trim())) {
                    etRollWidthInch.setError("Fill the credential");
                } else if(Integer.parseInt(etRollWidthInch.getText().toString()) < 1) {
                    etRollWidthInch.setError("Can't zero");
                }
//                else if(Integer.parseInt(tvtotalPennalty.getText().toString().trim()) < 1)
//                {
//                    Toast.makeText(this, "Select defect name", Toast.LENGTH_SHORT).show();
//                }
                else {
                    String qcPassQtyStr = tvqcPassQty.getText().toString().trim();

                    if (qcPassQtyStr.isEmpty()) {
                        DialogHelper.showWarningDialog(this, "Warning", "QC Pass Quantity cannot be empty.");
                        return;
                    }

                    try {
                        float qcPassQty = Float.parseFloat(qcPassQtyStr);

                        if (qcPassQty > 0.0) {
                            saveBtn.setEnabled(false);
                            postDataToServer();
                        } else {
                            DialogHelper.showWarningDialog(this, "Warning", "QC Pass Quantity can't be 0, Decrease quantity from reject.");
                        }
                    } catch (NumberFormatException e) {
                        DialogHelper.showWarningDialog(this, "Warning", "Invalid QC Pass Quantity. Please enter a valid number.");
                    }
                }
                break;
            case R.id.btnTryAgain:
                Intent intentC = new Intent(V1_GreyFabricActivity.this, V1_ScannerActivity.class);
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
//            index_obj.put("ROLL_KG",roll_w_kg);
            index_obj.put("ROLL_KG", tvqcPassQty.getText().toString());
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

        progressBar.setVisibility(View.VISIBLE);
        apiInterface.saveUpdateknittingFabricCall(body).enqueue(new Callback<V1_DataSaveResponse>() {
            @Override
            public void onResponse(Call<V1_DataSaveResponse> call, Response<V1_DataSaveResponse> response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful()){
                    saveBtn.setEnabled(true);
                    Toast.makeText(V1_GreyFabricActivity.this, response.body().getResultset(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(V1_GreyFabricActivity.this, V1_ScannerActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("url", urladdress);
                    intent.putExtra("s", savemenu);
                    intent.putExtra("u", updatemenu);
                    intent.putExtra("qc", qc_entry);
                    startActivity(intent);
                }else{
                    Toast.makeText(V1_GreyFabricActivity.this, "Failed, Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<V1_DataSaveResponse> call, Throwable t) {
                saveBtn.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(V1_GreyFabricActivity.this, "Failed, Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

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
            double roll_kg = 0;
            if(!etRejectQc.getText().toString().isEmpty()){
                roll_kg = Double.parseDouble(etRejectQc.getText().toString());
            }
            if(  roll_kg <= roll_w_kg)
            {
//                Pall mall
//                roll_w_kg = roll_w_kg - roll_kg;

//                Others
//                roll_w_kg = roll_w_kg - roll_kg;
            }
        }

    }

    private void setPostRequestContent(HttpURLConnection conn, JSONObject jsonObject) throws IOException {

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonObject.toString());
        Log.i(V1_GreyFabricActivity.class.toString(), jsonObject.toString());
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
            datePick.setText(dayOfMonth+"-"+month+"-"+year);
            date = String.format("%d-%d-%d", dayOfMonth, month, year);
            Toast.makeText(V1_GreyFabricActivity.this, date, Toast.LENGTH_SHORT).show();
        }
    };

    private void logoutUser() {
        session.setLogin(false);
        dbAdapter.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(V1_GreyFabricActivity.this, V1_LoginActivity.class);
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
        Intent intent = new Intent(V1_GreyFabricActivity.this, V1_ScannerActivity.class);
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