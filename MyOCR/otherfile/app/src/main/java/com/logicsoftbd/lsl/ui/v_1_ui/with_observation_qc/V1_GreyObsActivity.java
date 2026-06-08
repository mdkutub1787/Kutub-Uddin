package com.logicsoftbd.lsl.ui.v_1_ui.with_observation_qc;

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
import android.text.TextUtils;
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
import com.logicsoftbd.lsl.data.db.v1_db.model.V1_User;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DataSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ObsKnittingModelClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ObservationModelClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_QcModelRND;
import com.logicsoftbd.lsl.data.prefs.SessionManager;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.v_1_ui.home.V1_HomeActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.login.V1_LoginActivity;
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

public class V1_GreyObsActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "V1_GreyObsActivity";

    private EditText qrcodeEt;
    private Button scaneBtn;
    private Button datePick;
    private String currentDate, qc_observation_status = "SHOW OBSERVATION";
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private Button saveBtn;
    private Button tryAginBtn, observ_btn;
    private GridView gridView, obs_gridView;
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
    private Spinner statusSpinner;

    private TextView tvConstruction;
    private TextView tvGSM;
    private TextView tvDia;
    private TextView tvMCDia;
    private TextView tvColor;
    private TextView tvYarnCount;
    private TextView tvYarnLot;
    private TextView tvSpinning;

    private String mode;
    private long barcode;
    private int dtl_id = 0;
    private boolean status_obs = false;

    private int bayer_id = 0;
    private int company_id = 0;
    private String barcodeNumber;
    private String barcodeNumInput;
    private int rollNumber = 0;
    public Double roll_w_kg = 0.0;
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
    private double rejectQty = 0, rejectQty_server = 0;

    //update
    private String updateDate;
    private String updateQcName;
    private int updateRollWidthInch = 0;
    private double updateYds = 0;
    public double updateTotalPoint = 0;
    private String base_url = "", updateGrade;
    private int updaterollstatus = 0;
    private int updatedID = 0;


    private String urladdressChk;
    private String urladdress;
    private String urlString;
    public int userId = 0;

    public final ArrayList<String> gradename = new ArrayList<>();
    public final ArrayList<String> defactname = new ArrayList<>();
    public final ArrayList<Integer> defectID = new ArrayList<>();
    public final ArrayList<String> defectInchName = new ArrayList<>();
    public final ArrayList<Integer> defectInch = new ArrayList<>();

    public final ArrayList<Integer> ObsDefectID = new ArrayList<>();
    public final ArrayList<String> Obsdefactname = new ArrayList<>();
    public final ArrayList<Integer> ObsInchSpinnerItem = new ArrayList<>();
    public final ArrayList<Integer> ObsDepartmentSpinnerItem = new ArrayList<>();

    public Integer[] ObsIDArray;
    private String[] obsDefectNameArray;
    public Integer[] ObsfoundInInchArray;
    public Integer[] ObsdepartmentArray;

    public String[] gradeArray;
    public Integer[] gradeIDArray;
    public String[] defectInchNameArray;

    public final ArrayList<Integer> updatedefectCount = new ArrayList<>();
    public final ArrayList<Integer> updateDefectItem = new ArrayList<>();
    public final ArrayList<Integer> updateDefect = new ArrayList<>();

    public Integer[] updateDefectCountArry;
    public Integer[] updateDefectItemArry;
    public Integer[] updateDefectArry;

    public static Integer[] saveDefectItemArry = {6,4,4,4,4, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,0,4,0,0,0,0,0,0,0,0,0,0,0,0, 0, 0};
    public static Integer[] saveInchItemArry = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    public static Integer[] saveDepartItemArry = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};



    private V1_QcModelRND qcModelRND;

    private SessionManager session;
    private DBAdapter dbAdapter;

    private View view;
    public static ArrayList<V1_QcModelRND> modelArrayList;
    public static ArrayList<V1_ObservationModelClass> observationmodelArrayList;
    private V1_CustomObservationGridAdapter customGridAdapter;
    private V1_ObserverCustomGridAdapter observerCustomGridAdapter;

    //Retrofit
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;


    private AlertDialog.Builder alertDialogBuilder;
    private String resultupdate;

    //userPreviledge
    private int savemenu = 0, updatemenu = 0;

    //Qc Entry
    private String qc_entry;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_grey_obs);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializationMethod();
    }
    private void initializationMethod() {
        progressBar = findViewById(R.id.loadingProgress);
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
        etRejectQc = findViewById(R.id.rejectET);
        etRejectQc.setOnClickListener(this);
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

        ArrayAdapter<CharSequence> adapterhole = ArrayAdapter.createFromResource(this, R.array.statusroll
                , R.layout.spinner_item);
        statusSpinner.setAdapter(adapterhole);

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
                        rejectQty = 0;
                        //etRejectQc.setText(String.valueOf(rejectQty));

                        break;
                    case 1:
                        rollstatus = 1;
                        //etRejectQc.setText(String.valueOf(rejectQty));
                        break;
                    case 2:
                        rollstatus = 2;
                        rejectQty = 0;
                        //etRejectQc.setText(String.valueOf(rejectQty));

                        break;
                    case 3:
                        rollstatus = 3;
                        double roll_kg = Double.parseDouble(etRejectQc.getText().toString());
                        if(  roll_kg >= roll_w_kg)
                        {
                            //etRejectQc.setText(String.valueOf(roll_w_kg));
                        }
                        //etRejectQc.setText(String.valueOf(roll_w_kg));
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        etRejectQc.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                rejectMethod();
//            }
//        });

    }

    private void sendRequestToServer() {
        apiInterface.getObsKnittingModelClassCall(barcodeNumber).enqueue(new Callback<V1_ObsKnittingModelClass>() {
            @Override
            public void onResponse(Call<V1_ObsKnittingModelClass> call, Response<V1_ObsKnittingModelClass> response) {
                if (response.isSuccessful()){
                    mode = response.body().getData().getIndex().getMODE();
                    if(mode.equals("save"))
                    {

                        savemethod(response);
                        saveBtn.setText("SAVE");
//                        if(savemenu == 1)
//                        {
//                            saveBtn.setText("SAVE");
//                        }
//                        else {
//                            saveBtn.setVisibility(View.GONE);
//                        }

                    }
                    else if(mode.equals("update")){

                        savemethod(response);
                        updateMethod(response);
                        saveBtn.setText("UPDATE");
//                        if(updatemenu == 1)
//                        {
//                            saveBtn.setText("UPDATE");
//                        }else {
//                            saveBtn.setVisibility(View.GONE);
//                        }

                    }

                }
                else {
                    Toast.makeText(V1_GreyObsActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }

            private void updateMethod(Response<V1_ObsKnittingModelClass> response) {
                updateDate = response.body().getData().getIndex().getQCDATE();
                updateQcName = response.body().getData().getIndex().getQCNAME();
                updateRollWidthInch = Integer.parseInt(response.body().getData().getIndex().getROLLINCH());
                updateYds = Double.parseDouble(response.body().getData().getIndex().getROLLYDS());
                updateTotalPoint = Double.parseDouble(response.body().getData().getIndex().getTOTALPOINT());
                updateGrade = response.body().getData().getIndex().getFABRICGRADE();
                rejectQty_server = Double.parseDouble(response.body().getData().getIndex().getREJECTQNTY());
                updaterollstatus = Integer.parseInt(response.body().getData().getIndex().getROLLSTATUS());
                updatedID = Integer.parseInt(response.body().getData().getIndex().getUPDATEID());
                comments = String.valueOf(response.body().getData().getIndex().getCOMMENTS());

                datePick.setText(updateDate);
                etQcName.setText(updateQcName);
                etRollWidthInch.setText(String.valueOf(updateRollWidthInch));
                etRejectQc.setText(String.valueOf(rejectQty));
                etRollLength.setText(String.valueOf(String.format("%.4f", updateYds)));
                tvfebricGrade.setText(updateGrade);
                etcomments.setText(comments);
                etRejectQc.setText(String.valueOf(rejectQty_server));

                statusSpinner.setSelection(updaterollstatus);
            }

            private void savemethod(Response<V1_ObsKnittingModelClass> response) {
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



                V1_ObsKnittingModelClass.Grade gradeName;
                List<V1_ObsKnittingModelClass.Grade> grades = response.body().getData().getIndex().getArrayRefData().getGrade();
                for(V1_ObsKnittingModelClass.Grade d : grades)
                {
                    gradeName = d;
                    final V1_ObsKnittingModelClass.Grade finalName = gradeName;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gradename.add(String.valueOf(finalName.getGrade()));
                        }
                    });
                }

                V1_ObsKnittingModelClass.Defect defactName;
                List<V1_ObsKnittingModelClass.Defect> defactGrades = response.body().getData().getIndex().getArrayRefData().getDefect();
                for(V1_ObsKnittingModelClass.Defect d : defactGrades)
                {
                    defactName = d;
                    final V1_ObsKnittingModelClass.Defect finalName = defactName;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            defactname.add(String.valueOf(finalName.getDEFECTNAME()));
                        }
                    });
                }

                V1_ObsKnittingModelClass.Defect defactID;
                List<V1_ObsKnittingModelClass.Defect> ID = response.body().getData().getIndex().getArrayRefData().getDefect();
                for(V1_ObsKnittingModelClass.Defect d : ID)
                {
                    defactID = d;
                    final V1_ObsKnittingModelClass.Defect finalID = defactID;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            defectID.add(finalID.getID());
                        }
                    });
                }


                V1_ObsKnittingModelClass.Observation ObsdefactId;
                List<V1_ObsKnittingModelClass.Observation> ObsdefactNameId = response.body().getData().getIndex().getArrayRefData().getObservation();
                for(V1_ObsKnittingModelClass.Observation d : ObsdefactNameId)
                {
                    ObsdefactId = d;
                    final V1_ObsKnittingModelClass.Observation finalName = ObsdefactId;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ObsDefectID.add(finalName.getID());
                        }
                    });
                }

                V1_ObsKnittingModelClass.Observation ObsdefactName;
                List<V1_ObsKnittingModelClass.Observation> ObsdefactGrades = response.body().getData().getIndex().getArrayRefData().getObservation();
                for(V1_ObsKnittingModelClass.Observation d : ObsdefactGrades)
                {
                    ObsdefactName = d;
                    final V1_ObsKnittingModelClass.Observation finalName = ObsdefactName;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Obsdefactname.add(String.valueOf(finalName.getDEFECTNAME()));
                        }
                    });
                }



                V1_ObsKnittingModelClass.Observation ObsInchSpinner;
                List<V1_ObsKnittingModelClass.Observation> ObsSpinner = response.body().getData().getIndex().getArrayRefData().getObservation();
                for(V1_ObsKnittingModelClass.Observation d : ObsSpinner)
                {
                    ObsInchSpinner = d;
                    final V1_ObsKnittingModelClass.Observation finalName = ObsInchSpinner;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ObsInchSpinnerItem.add(finalName.getFOUNDININCH());
                        }
                    });
                }

                V1_ObsKnittingModelClass.Observation ObsDepartmentSpinner;
                final List<V1_ObsKnittingModelClass.Observation> ObsDeptSpinner = response.body().getData().getIndex().getArrayRefData().getObservation();
                for(V1_ObsKnittingModelClass.Observation d : ObsDeptSpinner)
                {
                    ObsDepartmentSpinner = d;
                    final V1_ObsKnittingModelClass.Observation finalName = ObsDepartmentSpinner;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ObsDepartmentSpinnerItem.add(finalName.getDEPARTMENT());
                        }
                    });
                }


                V1_ObsKnittingModelClass.Defect udatedefactount;
                List<V1_ObsKnittingModelClass.Defect> updateCount = response.body().getData().getIndex().getArrayRefData().getDefect();
                for(V1_ObsKnittingModelClass.Defect d : updateCount)
                {
                    udatedefactount = d;
                    final V1_ObsKnittingModelClass.Defect finalDefectCount = udatedefactount;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updatedefectCount.add(finalDefectCount.getDEFECTCOUNT());
                        }
                    });
                }

                V1_ObsKnittingModelClass.Defect updatedefect;
                List<V1_ObsKnittingModelClass.Defect> updatedefactList = response.body().getData().getIndex().getArrayRefData().getDefect();
                for(V1_ObsKnittingModelClass.Defect d : updatedefactList)
                {
                    updatedefect = d;
                    final V1_ObsKnittingModelClass.Defect finalDefect = updatedefect;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateDefect.add(finalDefect.getPENALTYPOINT());
                        }
                    });
                }
                V1_ObsKnittingModelClass.Defect updateSpinner;
                List<V1_ObsKnittingModelClass.Defect> updateSpinnerList = response.body().getData().getIndex().getArrayRefData().getDefect();
                for(V1_ObsKnittingModelClass.Defect d : updateSpinnerList)
                {
                    updateSpinner = d;
                    final V1_ObsKnittingModelClass.Defect finalSpinnerList = updateSpinner;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateDefectItem.add(finalSpinnerList.getFOUNDININCH());
                        }
                    });
                }

                alertDialogBuilder = new AlertDialog.Builder(V1_GreyObsActivity.this);

                modelArrayList = getModel();
                observationmodelArrayList = getObservatiomModel();

                try{
                    observerCustomGridAdapter = new V1_ObserverCustomGridAdapter(V1_GreyObsActivity.this);
                    obs_gridView.setAdapter(observerCustomGridAdapter);
                }catch (Exception e){
                    Log.d(TAG, "savemethod:" + e.getMessage());
                }


                try {
                    customGridAdapter = new V1_CustomObservationGridAdapter(V1_GreyObsActivity.this);
                    gridView.setAdapter(customGridAdapter);
                }catch (Exception e){
                    Log.d(TAG, "savemethod: "+ e.getMessage());
                }





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


            }

            @Override
            public void onFailure(Call<V1_ObsKnittingModelClass> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(V1_GreyObsActivity.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(V1_GreyObsActivity.this, "Invalid Id", Toast.LENGTH_SHORT).show();
                    String s = t.getMessage();
                    Toast.makeText(V1_GreyObsActivity.this, String.valueOf(s), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    public void calculationYds() {

        String inch = etRollWidthInch.getText().toString().trim();
        String sum = tvtotalPennalty.getText().toString().trim();
        if(TextUtils.isEmpty(String.valueOf(sum)))
        {
            tvtotalPennalty.setError("Fill the credential");
        }
        else if(TextUtils.isEmpty(String.valueOf(inch)))
        {
            etRollWidthInch.setError("Fill the credential");
        }
        else {
            yds = ((Double.parseDouble(String.valueOf(roll_w_kg))* 1000) / (Integer.parseInt(String.valueOf(gsm)) * Double.parseDouble(inch)* 0.0254) * 1.09361);
            //etRollLength.setText(String.valueOf(yds));
            etRollLength.setText((String.valueOf(String.format("%.4f", yds))));
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
                qcModel.setSpinneritem(updateDefectItemArry[i]);
            }
            else {
                qcModel.setSpinneritem(updateDefectItemArry[i]);
            }

            list.add(qcModel);
        }
        return list;
    }

    private ArrayList<V1_ObservationModelClass> getObservatiomModel() {
        ArrayList<V1_ObservationModelClass> list = new ArrayList<>();

        ObsIDArray = new Integer[ObsDefectID.size()];
        for(int i = 0; i < ObsDefectID.size(); i++)
        {
            ObsIDArray[i] = ObsDefectID.get(i);
        }

        obsDefectNameArray = new String[Obsdefactname.size()];
        for(int i = 0; i < Obsdefactname.size(); i++)
        {
            obsDefectNameArray[i] = Obsdefactname.get(i);
        }

        ObsfoundInInchArray = new Integer[ObsInchSpinnerItem.size()];
        for(int i = 0; i < ObsInchSpinnerItem.size(); i++)
        {
            ObsfoundInInchArray[i] = ObsInchSpinnerItem.get(i);
        }

        ObsdepartmentArray = new Integer[ObsDepartmentSpinnerItem.size()];
        for(int i = 0; i < ObsDepartmentSpinnerItem.size(); i++)
        {
            ObsdepartmentArray[i] = ObsDepartmentSpinnerItem.get(i);
        }

        Log.d(TAG, "savemethod: " + ObsfoundInInchArray);


        for(int i = 0; i < obsDefectNameArray.length; i++){
            V1_ObservationModelClass ObservationQcModel = new V1_ObservationModelClass();
            ObservationQcModel.setDefectId(ObsIDArray[i]);
            ObservationQcModel.setObservationDefectName(obsDefectNameArray[i]);
            ObservationQcModel.setObservationInchSpinner(ObsfoundInInchArray[i]);
            ObservationQcModel.setObservationDepartmentSpinner(ObsdepartmentArray[i]);
            if(mode.equals("save"))
            {
                ObservationQcModel.setObservationInchSpinner(ObsfoundInInchArray[i]);
                ObservationQcModel.setObservationDepartmentSpinner(ObsdepartmentArray[i]);
            }
            else {
                ObservationQcModel.setObservationInchSpinner(ObsfoundInInchArray[i]);
                ObservationQcModel.setObservationDepartmentSpinner(ObsdepartmentArray[i]);
            }

            list.add(ObservationQcModel);
        }
        return list;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSave:
                String name = null;
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
                else if(Integer.parseInt(tvtotalPennalty.getText().toString().trim()) < 1)
                {
                    Toast.makeText(this, "Select defect name", Toast.LENGTH_SHORT).show();
                }else if(Double.parseDouble(etRejectQc.getText().toString()) >= roll_w_kg){
                    etRejectQc.setError("Reject QC can't be greater than Roll Kg");
                }
                else {
                    double roll_kg = Double.parseDouble(etRejectQc.getText().toString());
                    if(  roll_kg <= roll_w_kg)
                    {
                        if(rejectQty_server == roll_kg){
                            roll_w_kg = roll_w_kg - 0;
                        }else if(roll_kg > rejectQty_server ){
                            double r = roll_kg - rejectQty_server;
                            roll_w_kg = roll_w_kg - r;
                        }else{
                            double r = rejectQty_server - roll_kg;
                            roll_w_kg = roll_w_kg + r;
                        }

                    }
                    calculationYds();
                    postDataToServer();
                }

                //postDataToServer();

                break;
            case R.id.btnTryAgain:
                Intent intentC = new Intent(V1_GreyObsActivity.this, V1_QRBarcodeScannerActivity.class);
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
            case R.id.observation_btn:
                observation_view();
                break;
//            case R.id.rejectET:
//                break;
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
//        // perform HTTP POST request
//        if(checkNetworkConnection())
//            new HTTPAsyncTask().execute(String.format("%s"+"logic-api/index.php/api/android/create_observation_kniting_qc_result", urladdress));
//        else
//            Toast.makeText(this, "Not Connected!", Toast.LENGTH_SHORT).show();

        checkNetworkConnection();
        statusSpinnerItem();

        JSONObject save_obj = new JSONObject();
        JSONObject data_obj = new JSONObject();
        JSONObject index_obj = new JSONObject();
        JSONArray dtls_arr = new JSONArray();
        JSONArray obs_dtls_arr = new JSONArray();

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

            if(obsDefectNameArray != null && obsDefectNameArray.length > 0){
                for(int i = 0; i < obsDefectNameArray.length; i++){
                    JSONObject obs_dtls_obj = new JSONObject();
                    obs_dtls_obj.put("OBS_ID", observationmodelArrayList.get(i).getDefectId());
                    obs_dtls_obj.put("OBS_INCH", observationmodelArrayList.get(i).getObservationInchSpinner());
                    obs_dtls_obj.put("OBS_DEPARTMENT", observationmodelArrayList.get(i).getObservationDepartmentSpinner());
                    obs_dtls_arr.put(obs_dtls_obj);
                }
            }
            data_obj.put("list_data",dtls_arr);
            data_obj.put("obs_list_data",obs_dtls_arr);
            save_obj.put("data", data_obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, save_obj.toString());

        progressBar.setVisibility(View.VISIBLE);
        apiInterface.saveUpdateknittingFabricObsCall(body).enqueue(new Callback<V1_DataSaveResponse>() {
            @Override
            public void onResponse(Call<V1_DataSaveResponse> call, Response<V1_DataSaveResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    Toast.makeText(V1_GreyObsActivity.this, response.body().getResultset(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(V1_GreyObsActivity.this, V1_QRBarcodeScannerActivity.class);
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
                Toast.makeText(V1_GreyObsActivity.this, "Failed, Please try again.", Toast.LENGTH_SHORT).show();
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


//    private String httpPost(String myUrl) throws IOException, JSONException {
//        String result = "";
//
//        URL url = new URL(myUrl);
//
//        // 1. create HttpURLConnection
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
//
//        // 2. build JSON object
//        JSONObject jsonObject = buidJsonObject();
//
//        // 3. add JSON content to POST request body
//        setPostRequestContent(conn, jsonObject);
//
//        // 4. make POST request to the given URL
//        conn.connect();
//
//        // 5. return response message
//        return conn.getResponseMessage()+"";
//
//    }
//
//    private class HTTPAsyncTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... urls) {
//            // params comes from the execute() call: params[0] is the url.
//            try {
//                try {
//                    return httpPost(urls[0]);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    return "Failed!";
//                }
//            } catch (IOException e) {
//                return "Unable to retrieve web page. URL may be invalid.";
//            }
//        }
//        // onPostExecute displays the results of the AsyncTask.
//        @Override
//        protected void onPostExecute(String result) {
//            resultupdate = result;
//            //Toast.makeText(GreyFabricActivity.this, result, Toast.LENGTH_SHORT).show();
//            if(gradeArray != null && gradeArray.length > 0)
//            {
//                if(result.equals("OK"))
//                {
//                    Intent intent = new Intent(V1_GreyObsActivity.this, V1_QRBarcodeScannerActivity.class);
//                    intent.putExtra("userId", userId);
//                    intent.putExtra("url", urladdress);
//                    intent.putExtra("s", savemenu);
//                    intent.putExtra("u", updatemenu);
//                    intent.putExtra("qc", qc_entry);
//
//                    startActivity(intent);
//                }
//                else
//                {
//                    showMessageResult(result);
//                }
//            }
//        }
//    }
//
//    private JSONObject buidJsonObject() throws JSONException {
//
//        statusSpinnerItem();
//
//        JSONObject save_obj = new JSONObject();
//        JSONObject data_obj = new JSONObject();
//        JSONObject index_obj = new JSONObject();
//        JSONArray dtls_arr = new JSONArray();
//        JSONArray obs_dtls_arr = new JSONArray();
//
//        save_obj.put("status",true);
//        save_obj.put("mode", mode);
//        save_obj.put("UPDATE_ID", updatedID);
//
//        index_obj.put("BARCODE_NO",barcode);
//        index_obj.put("BUYER_ID", bayer_id);
//        index_obj.put("COMPANY_ID", company_id);
//        index_obj.put("DTLS_ID", dtl_id);
//        index_obj.put("ROLL_MAINTAINED",rollMaintain);
//        index_obj.put("QC_DATE",datePick.getText().toString());
//        index_obj.put("ROLL_ID",rollId);
//        index_obj.put("ROLL_NO",rollNumber);
//        index_obj.put("QC_NAME",etQcName.getText().toString());
//        index_obj.put("ROLL_INCH", etRollWidthInch.getText().toString());
//        index_obj.put("ROLL_KG",roll_w_kg);
//        index_obj.put("ROLL_YDS",yds);
//        index_obj.put("TOTAL_PENALTY_POINT",tvtotalPennalty.getText().toString());
//        index_obj.put("TOTAL_POINT",tvpoint.getText().toString());
//        index_obj.put("INSERTED_BY",userId);
//        index_obj.put("UPDATED_BY",userId);
//        index_obj.put("UPDATE_ID",updatedID);
//        index_obj.put("REJECT_QNTY",etRejectQc.getText().toString());
//        index_obj.put("FABRIC_GRADE", tvfebricGrade.getText().toString());
//        index_obj.put("ROLL_STATUS", rollstatus);
//        index_obj.put("COMMENTS", etcomments.getText().toString());
//
//        data_obj.put("index",index_obj);
//
//
//        if(gradeArray != null && gradeArray.length > 0) {
//            for (int i = 0; i < gradeArray.length; i++) {
//                if (modelArrayList.get(i).getQcDefectNumber() > 0) {
//                    JSONObject dtls_obj = new JSONObject();
//                    dtls_obj.put("DEFECT_ID", modelArrayList.get(i).getDefectID());
//                    dtls_obj.put("COUNT", modelArrayList.get(i).getQcItemNumber());
//                    dtls_obj.put("INCH_ID", modelArrayList.get(i).getSpinneritem());
//                    dtls_obj.put("PENALTY", modelArrayList.get(i).getQcDefectNumber());
//                    dtls_arr.put(dtls_obj);
//                }
//            }
//        }
//
//        if(obsDefectNameArray != null && obsDefectNameArray.length > 0){
//            for(int i = 0; i < obsDefectNameArray.length; i++){
//                JSONObject obs_dtls_obj = new JSONObject();
//                obs_dtls_obj.put("OBS_ID", observationmodelArrayList.get(i).getDefectId());
//                obs_dtls_obj.put("OBS_INCH", observationmodelArrayList.get(i).getObservationInchSpinner());
//                obs_dtls_obj.put("OBS_DEPARTMENT", observationmodelArrayList.get(i).getObservationDepartmentSpinner());
//                obs_dtls_arr.put(obs_dtls_obj);
//            }
//        }
//        data_obj.put("list_data",dtls_arr);
//        data_obj.put("obs_list_data",obs_dtls_arr);
//        save_obj.put("data", data_obj);
//        return save_obj;
//    }

    private void statusSpinnerItem() {
        if(rollstatus == 1)
        {
//            double roll_kg = Double.parseDouble(etRejectQc.getText().toString());
//            if(  roll_kg <= roll_w_kg)
//            {
//                if(rejectQty_server == roll_kg){
//                    roll_w_kg = roll_w_kg - 0;
//                }else if(roll_kg > rejectQty_server ){
//                    double r = roll_kg - rejectQty_server;
//                    roll_w_kg = roll_w_kg - r;
//                }else{
//                    double r = rejectQty_server - roll_kg;
//                    roll_w_kg = roll_w_kg + r;
//                }
//
//            }
        }

    }

    private void rejectMethod(){
        double roll_kg = Double.parseDouble(etRejectQc.getText().toString());
        if(  roll_kg <= roll_w_kg)
        {
            if(rejectQty_server == roll_kg){
                roll_w_kg = roll_w_kg - 0;
            }else if(roll_kg > rejectQty_server ){
                double r = roll_kg - rejectQty_server;
                roll_w_kg = roll_w_kg - r;
            }else{
                double r = rejectQty_server - roll_kg;
                roll_w_kg = roll_w_kg + r;
            }

        }
    }

    private void setPostRequestContent(HttpURLConnection conn, JSONObject jsonObject) throws IOException {

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonObject.toString());
        Log.i(V1_GreyObsActivity.class.toString(), jsonObject.toString());
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
            Toast.makeText(V1_GreyObsActivity.this, date, Toast.LENGTH_SHORT).show();
        }
    };

    private void logoutUser() {
        session.setLogin(false);
        dbAdapter.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(V1_GreyObsActivity.this, V1_LoginActivity.class);
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
        Intent intent = new Intent(V1_GreyObsActivity.this, V1_HomeActivity.class);
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
