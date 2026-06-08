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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.db.v1_db.helper.DBAdapter;
import com.logicsoftbd.lsl.data.db.v1_db.model.V1_User;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DataSaveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ObsFinishModelClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ObservationDefect;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ObservationDefectClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ObservationModelClass;
import com.logicsoftbd.lsl.data.network.v1_model.V1_QcModelRND;
import com.logicsoftbd.lsl.data.prefs.SessionManager;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.v_1_ui.login.V1_LoginActivity;
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

public class V1_FinishFabricObsActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "FinishFabricObsActivity";
    private String currentDate, qc_observation_status = "SHOW OBSERVATION";
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private Button saveBtn;
    private Button tryAginBtn, observationBtn;
    private GridView gridView, obs_gridView;
    private int Year, Month, Day;
    public TextView tvtotalPennalty;
    public TextView tvpoint;
    public TextView tvfebricGrade;

    private EditText etcomments;
    private String mode = "";
    public Double yds = 0.0;
    public int roll_inch = 0;
    private String date;

    //update
    private String updateDate;
    public double updateTotalPoint = 0.0;
    public int updaterollstatus = 0;
    private String updateGrade;
    private String updateComments;


    private String urladdressChk;
    private String urladdress;
    public int userId = 0;

    public final ArrayList<String> gradename = new ArrayList<>();
    public final ArrayList<String> gradeNameSerial = new ArrayList<>();
    public final ArrayList<String> defactname = new ArrayList<>();
    public final ArrayList<String> machinename = new ArrayList<>();
    public final ArrayList<Integer> defectID = new ArrayList<>();
    public final ArrayList<Integer> machineNamID = new ArrayList<>();
    public final ArrayList<String> defectInchName = new ArrayList<>();
    public final ArrayList<Integer> defectInch = new ArrayList<>();

    public String[] gradeArray;
    public Integer[] gradeIDArray;
    public String[] defectInchNameArray;


    public final ArrayList<String> obsDefectKey = new ArrayList<>();
    public final ArrayList<String> obsDefectValue = new ArrayList<>();

    public static String[] obsDefectKeyArray;
    public static String[] obsDefectValueArray;


    public final ArrayList<Integer> updatedefectCount = new ArrayList<>();
    public final ArrayList<Integer> updateDefectItem = new ArrayList<>();
    public final ArrayList<Integer> updateDefect = new ArrayList<>();

    public Integer[] updateDefectCountArry;
    public Integer[] updateDefectItemArry;
    public Integer[] updateDefectArry;

    public final ArrayList<Integer> ObsDefectID = new ArrayList<>();
    public final ArrayList<String> Obsdefactname = new ArrayList<>();
    public final ArrayList<Integer> ObsInchSpinnerItem = new ArrayList<>();
    public final ArrayList<Integer> ObsDepartmentSpinnerItem = new ArrayList<>();

    public Integer[] ObsIDArray;
    private String[] obsDefectNameArray;
    public Integer[] ObsfoundInInchArray;
    public Integer[] ObsdepartmentArray;



    private V1_QcModelRND qcModelRND;

    private SessionManager session;
    private DBAdapter dbAdapter;

    private View view;
    public static ArrayList<V1_QcModelRND> modelArrayList;
    public static ArrayList<V1_ObservationModelClass> observationmodelArrayList;
    public static ArrayList<V1_ObservationDefect> observationDefectArrayList;

    private V1_ObsFinishFabricCustomAdapter finishFabricCustomAdapter;
    private V1_ObserverCustomFinishGridAdapter observerCustomFinishGridAdapter;

    //Retrofit
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;

    private AlertDialog.Builder alertDialogBuilder;
    private String resultupdate;

    //userPreviledge
    private int savemenu = 0;
    private int updatemenu = 0;

    //Qc Entry
    private String qc_entry, prod_dlts, batch;



    private Button fDate, dateTV;

    private double fRollWidth = 0;

    private TextView mSystemId, mBatchNo, mBuyer,mRollYds, mFbNo, mRollNumber, mGSM, mDia, mFabDes, mTablemc, mShinkage;
    private EditText mQCName, mActualDia, mActualGsm, mRollKg, mReject, mLength, mWidth, mTwisting;

    private String base_url = "", system_id, batch_No, buyer, fbNo, gsm, dia,
            mst_id, dtls_id, qc_mst_id, fbatch_id, buyer_id,roll_number,
            qcName, actual_dia, actual_gsm, roll_weight_kg, fab_des, fab_des_id, machine_id, table_mc, buyer_grade;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_finish_fabric_obs);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        initializationMethod();
    }
    private void initializationMethod() {
        /*master part*/

        progressBar = findViewById(R.id.loadingProgress);
        mSystemId = findViewById(R.id.system_id);
        mBatchNo = findViewById(R.id.batchNo);
        mBuyer = findViewById(R.id.buyer);
        mFbNo = findViewById(R.id.fbno);
        mRollNumber = findViewById(R.id.rollNumber);

        dateTV = findViewById(R.id.TVDate);
        dateTV.setOnClickListener(this);

        mQCName = findViewById(R.id.qcName);
        mActualDia = findViewById(R.id.actualDia);
        mActualGsm = findViewById(R.id.actualGsm);
        mRollKg = findViewById(R.id.roll_weight_kg);
        mRollYds = findViewById(R.id.roll_weight_yards);
        mReject = findViewById(R.id.reject_qty);
        mGSM = findViewById(R.id.gsm);
        mDia = findViewById(R.id.dia);
        mFabDes = findViewById(R.id.febdes);
        mTablemc = findViewById(R.id.tablemc);
        mShinkage = findViewById(R.id.shrinkage);
        mLength = findViewById(R.id.length);
        mWidth = findViewById(R.id.width);
        mTwisting = findViewById(R.id.twisting);


        gridView = findViewById(R.id.gridViewInfo);
        obs_gridView = findViewById(R.id.obs_gridViewInfo);
        saveBtn = findViewById(R.id.btnSave);

        //refreshBtn = findViewById(R.id.btnrefresh);
        tryAginBtn = findViewById(R.id.btnTryAgain);
        observationBtn = findViewById(R.id.observation_btn);
        observationBtn.setText(qc_observation_status);
        observationBtn.setOnClickListener(this);

        saveBtn.setOnClickListener(this);
        tryAginBtn.setOnClickListener(this);

        //summery
        tvtotalPennalty = findViewById(R.id.penaltyPointTV);
        tvpoint = findViewById(R.id.toalPointTV);
        tvfebricGrade = findViewById(R.id.febricGradeTV);

        etcomments = findViewById(R.id.commentET);

        qcModelRND = new V1_QcModelRND();

        Intent intent = getIntent();

        String resultS = intent.getStringExtra("result");
        urladdressChk = intent.getStringExtra("url");
        savemenu = intent.getIntExtra("s", 0);
        updatemenu = intent.getIntExtra("u", 0);
        qc_entry = intent.getStringExtra("qc");
        batch = intent.getStringExtra("batch_no");
        prod_dlts = intent.getStringExtra("prod_dlts");


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

        sendRequestObservationDefect();
        sendRequestFinishfabricServer();

        // session manager
        session = new SessionManager(getApplicationContext());
        dbAdapter = new DBAdapter(this);


        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = simpleDateFormat.format(calendar.getTime());

        // datePick.setText(currentDate);
        dateTV.setText(currentDate);
    }

    private void sendRequestObservationDefect() {
        apiInterface.getObservationDefectClassCall().enqueue(new Callback<V1_ObservationDefectClass>() {
            @Override
            public void onResponse(Call<V1_ObservationDefectClass> call, Response<V1_ObservationDefectClass> response) {
                if(response.isSuccessful()){

                    V1_ObservationDefectClass.DataArr defactKey;
                    List<V1_ObservationDefectClass.DataArr> defactKeys = response.body().getData().getDataArr();
                    for(V1_ObservationDefectClass.DataArr d : defactKeys)
                    {
                        defactKey = d;
                        final V1_ObservationDefectClass.DataArr finalName = defactKey;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                obsDefectKey.add(String.valueOf(finalName.getKey()));
                            }
                        });
                    }

                    V1_ObservationDefectClass.DataArr defactValue;
                    List<V1_ObservationDefectClass.DataArr> defactValues = response.body().getData().getDataArr();
                    for(V1_ObservationDefectClass.DataArr d : defactValues)
                    {
                        defactValue = d;
                        final V1_ObservationDefectClass.DataArr finalName = defactValue;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                obsDefectValue.add(String.valueOf(finalName.getValue()));
                            }
                        });
                    }

                    observationDefectArrayList = new ArrayList<>();
                    observationDefectArrayList = getObservationDefect();


                }else {
                    Toast.makeText(V1_FinishFabricObsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<V1_ObservationDefectClass> call, Throwable t) {
                Toast.makeText(V1_FinishFabricObsActivity.this, String.valueOf(t.getMessage()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<V1_ObservationDefect> getObservationDefect() {

        ArrayList<V1_ObservationDefect> list = new ArrayList<>();

        obsDefectKeyArray = new String[obsDefectKey.size()];
        for(int i = 0; i < obsDefectKey.size(); i++)
        {
            obsDefectKeyArray[i] = obsDefectKey.get(i);
        }

        obsDefectValueArray = new String[obsDefectValue.size()];
        for(int i = 0; i < obsDefectValue.size(); i++)
        {
            obsDefectValueArray[i] = obsDefectValue.get(i);
        }

        Log.d(TAG, "getObservationDefect: " + obsDefectValueArray.toString());

        for(int i = 0; i < obsDefectKeyArray.length; i++){
            V1_ObservationDefect observationDefect = new V1_ObservationDefect();
            //observationDefect.setKey(obsDefectKeyArray[i]);
            observationDefect.setValue(obsDefectValueArray[i]);
            list.add(observationDefect);
        }
        return list;
    }

    private void sendRequestFinishfabricServer() {

        apiInterface.getFinishFabricObsModelClassCall(prod_dlts).enqueue(new Callback<V1_ObsFinishModelClass>() {
            @Override
            public void onResponse(Call<V1_ObsFinishModelClass> call, Response<V1_ObsFinishModelClass> response) {
                if(response.isSuccessful())
                {


                    mode = response.body().getData().getIndex().getMode();

                    if(!mode.isEmpty()){
                        savemethod(response);
                        updateMethod(response);
                    }


                }
                else {
                    Toast.makeText(V1_FinishFabricObsActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<V1_ObsFinishModelClass> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(V1_FinishFabricObsActivity.this, "No network available, please check your WiFi or Data connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(V1_FinishFabricObsActivity.this, "Invalid Id", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateMethod(Response<V1_ObsFinishModelClass> response) {
        updateDate = response.body().getData().getIndex().getQcDate();
        updateTotalPoint = Double.parseDouble(response.body().getData().getIndex().getTotalPoint());
        updateGrade = response.body().getData().getIndex().getFabricGrade();
        updateComments = response.body().getData().getIndex().getComments();
        updaterollstatus = Integer.parseInt(response.body().getData().getIndex().getRollStatus());
        fRollWidth = Double.parseDouble(response.body().getData().getIndex().getRollWidth());



        //rollWidhInch.setText(String.valueOf(fRollWidth));
        //dateTV.setText(String.valueOf(updateDate));
        tvtotalPennalty.setText(String.valueOf(updateTotalPoint));
//        tvfebricGrade.setText(buyer_grade);
        etcomments.setText(updateComments);
        //statusSpinner.setSelection(updaterollstatus);
    }

    private void savemethod(Response<V1_ObsFinishModelClass> response) {

        system_id = response.body().getData().getIndex().getReceiveNo();
        batch_No = response.body().getData().getIndex().getBatchNo();
        buyer = response.body().getData().getIndex().getBuyerName();
        fbNo = response.body().getData().getIndex().getBookingNo();

        fab_des = response.body().getData().getIndex().getFabDes();
        fab_des_id = response.body().getData().getIndex().getFabDesId();
        table_mc = response.body().getData().getIndex().getMachineNo();
        dia = response.body().getData().getIndex().getWidth();
        gsm = response.body().getData().getIndex().getGsm();
        mst_id = response.body().getData().getIndex().getMstId();
        dtls_id = response.body().getData().getIndex().getDtlsId();
        qc_mst_id = response.body().getData().getIndex().getQcMstId();
        fbatch_id = response.body().getData().getIndex().getBatchId();
        buyer_id = response.body().getData().getIndex().getBuyerId();
        machine_id = response.body().getData().getIndex().getMachineId();
        buyer_grade = response.body().getData().getIndex().getBuyer_grade();





        finishFabricQcData(response);

        mSystemId.setText(system_id);
        mBatchNo.setText(String.valueOf(batch_No));
        mBuyer.setText(buyer);
        mDia.setText(dia);
        mGSM.setText(gsm);
        mFabDes.setText(fab_des);
        mTablemc.setText(table_mc);
        mFbNo.setText(fbNo);

    }

    private void finishFabricQcData(Response<V1_ObsFinishModelClass> response) {
        V1_ObsFinishModelClass.Grade gradeName;
        List<V1_ObsFinishModelClass.Grade> grades = response.body().getData().getIndex().getArrayRefData().getGrade();
        for(V1_ObsFinishModelClass.Grade d : grades)
        {
            gradeName = d;
            final V1_ObsFinishModelClass.Grade finalName = gradeName;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gradename.add(String.valueOf(finalName.getGrade()));
                }
            });
        }

        V1_ObsFinishModelClass.Grade gradeSerial;
        List<V1_ObsFinishModelClass.Grade> gradeSerials = response.body().getData().getIndex().getArrayRefData().getGrade();
        for(V1_ObsFinishModelClass.Grade d : gradeSerials)
        {
            gradeSerial = d;
            final V1_ObsFinishModelClass.Grade finalName = gradeSerial;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gradeNameSerial.add(String.valueOf(finalName.getSerial()));
                }
            });
        }

        V1_ObsFinishModelClass.Defect defactName;
        List<V1_ObsFinishModelClass.Defect> defactGrades = response.body().getData().getIndex().getArrayRefData().getDefect();
        for(V1_ObsFinishModelClass.Defect d : defactGrades)
        {
            defactName = d;
            final V1_ObsFinishModelClass.Defect finalName = defactName;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    defactname.add(String.valueOf(finalName.getDEFECTNAME()));
                }
            });
        }

        V1_ObsFinishModelClass.Defect defactID;
        List<V1_ObsFinishModelClass.Defect> ID = response.body().getData().getIndex().getArrayRefData().getDefect();
        for(V1_ObsFinishModelClass.Defect d : ID)
        {
            defactID = d;
            final V1_ObsFinishModelClass.Defect finalID = defactID;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    defectID.add(Integer.valueOf(finalID.getID()));
                }
            });
        }

        V1_ObsFinishModelClass.Defect udatedefactount;
        List<V1_ObsFinishModelClass.Defect> updateCount = response.body().getData().getIndex().getArrayRefData().getDefect();
        for(V1_ObsFinishModelClass.Defect d : updateCount)
        {
            udatedefactount = d;
            final V1_ObsFinishModelClass.Defect finalDefectCount = udatedefactount;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updatedefectCount.add(Integer.valueOf(finalDefectCount.getDEFECTCOUNT()));
                }
            });
        }

        V1_ObsFinishModelClass.Defect updatedefect;
        List<V1_ObsFinishModelClass.Defect> updatedefactList = response.body().getData().getIndex().getArrayRefData().getDefect();
        for(V1_ObsFinishModelClass.Defect d : updatedefactList)
        {
            updatedefect = d;
            final V1_ObsFinishModelClass.Defect finalDefect = updatedefect;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateDefect.add(Integer.valueOf(finalDefect.getPENALTYPOINT()));
                }
            });
        }
        V1_ObsFinishModelClass.Defect updateSpinner;
        List<V1_ObsFinishModelClass.Defect> updateSpinnerList = response.body().getData().getIndex().getArrayRefData().getDefect();
        for(V1_ObsFinishModelClass.Defect d : updateSpinnerList)
        {
            updateSpinner = d;
            final V1_ObsFinishModelClass.Defect finalSpinnerList = updateSpinner;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateDefectItem.add(Integer.valueOf(finalSpinnerList.getFOUNDININCH()));
                }
            });
        }


        V1_ObsFinishModelClass.Observation ObsdefactId;
        List<V1_ObsFinishModelClass.Observation> ObsdefactNameId = response.body().getData().getIndex().getArrayRefData().getObservation();
        for(V1_ObsFinishModelClass.Observation d : ObsdefactNameId)
        {
            ObsdefactId = d;
            final V1_ObsFinishModelClass.Observation finalName = ObsdefactId;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ObsDefectID.add(Integer.valueOf(finalName.getID()));
                }
            });
        }

        V1_ObsFinishModelClass.Observation ObsdefactName;
        List<V1_ObsFinishModelClass.Observation> ObsdefactGrades = response.body().getData().getIndex().getArrayRefData().getObservation();
        for(V1_ObsFinishModelClass.Observation d : ObsdefactGrades)
        {
            ObsdefactName = d;
            final V1_ObsFinishModelClass.Observation finalName = ObsdefactName;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Obsdefactname.add(String.valueOf(finalName.getDEFECTNAME()));
                }
            });
        }



        V1_ObsFinishModelClass.Observation ObsInchSpinner;
        List<V1_ObsFinishModelClass.Observation> ObsSpinner = response.body().getData().getIndex().getArrayRefData().getObservation();
        for(V1_ObsFinishModelClass.Observation d : ObsSpinner)
        {
            ObsInchSpinner = d;
            final V1_ObsFinishModelClass.Observation finalName = ObsInchSpinner;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ObsInchSpinnerItem.add(Integer.valueOf(finalName.getFOUNDININCH()));
                }
            });
        }

        V1_ObsFinishModelClass.Observation ObsDepartmentSpinner;
        final List<V1_ObsFinishModelClass.Observation> ObsDeptSpinner = response.body().getData().getIndex().getArrayRefData().getObservation();
        for(V1_ObsFinishModelClass.Observation d : ObsDeptSpinner)
        {
            ObsDepartmentSpinner = d;
            final V1_ObsFinishModelClass.Observation finalName = ObsDepartmentSpinner;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ObsDepartmentSpinnerItem.add(Integer.valueOf(finalName.getDEPARTMENT()));
                }
            });
        }


        alertDialogBuilder = new AlertDialog.Builder(V1_FinishFabricObsActivity.this);

        modelArrayList = getModel();
        observationmodelArrayList = getObservatiomModel();

        finishFabricCustomAdapter = new V1_ObsFinishFabricCustomAdapter(V1_FinishFabricObsActivity.this);
        gridView.setAdapter(finishFabricCustomAdapter);

        observerCustomFinishGridAdapter = new V1_ObserverCustomFinishGridAdapter(V1_FinishFabricObsActivity.this);
        obs_gridView.setAdapter(observerCustomFinishGridAdapter);


    }

    public void calculationYds() {

        String sum = tvtotalPennalty.getText().toString().trim();

        double actualDia = 0.0;

        try{
            actualDia = Double.parseDouble(mActualDia.getText().toString().trim());
        }catch (NumberFormatException e){
            actualDia = 0.0;
        }

        double actualgsm = 0.0;
        try{
            actualgsm = Double.parseDouble(mActualGsm.getText().toString().trim());
        }catch (NumberFormatException e){
            actualgsm = 0.0;
        }

        double roll_kg = 0.0;
        try{
            roll_kg = Double.parseDouble(mRollKg.getText().toString().trim());
        }catch (NumberFormatException e){
            roll_kg = 0.0;
        }

        if(TextUtils.isEmpty(String.valueOf(sum)))
        {
            tvtotalPennalty.setError("Fill the credential");
        }
        else if(TextUtils.isEmpty(String.valueOf(actualDia)))
        {
            mDia.setError("Fill the credential");
        }
        else if(TextUtils.isEmpty(String.valueOf(actualgsm)))
        {
            mGSM.setError("Fill the credential");
        }
        else if(TextUtils.isEmpty(String.valueOf(roll_kg)))
        {
            mRollKg.setError("Fill the credential");
        }
        else {
            yds = (43056/(actualDia * actualgsm))*roll_kg;
            if(yds.isNaN()){
                mRollYds.setText(((String.format("%.4f", 0.0))));
            }else {
                mRollYds.setText(((String.format("%.4f", yds))));
            }

            Double totalPanalty = ((36 * 100 * Integer.parseInt(sum)) / (roll_kg* yds));
            qcModelRND.setTotalPoint(totalPanalty);
//            tvpoint.setText((String.valueOf(String.format("%.4f", totalPanalty))));
            if(totalPanalty.isNaN()){
                tvpoint.setText(((String.format("%.4f", 0.0))));
            }else {
                tvpoint.setText(((String.format("%.4f", qcModelRND.getTotalPoint()))));
            }
//            if(String.valueOf(qcModelRND.getTotalPoint()).equals("Infinity"))
//            {
//                tvpoint.setText(String.valueOf(0));
//            }
//            else {
//                tvpoint.setText(String.valueOf(String.format("%.4f", qcModelRND.getTotalPoint())));
//            }

            int grade = 0;
            grade = (int) qcModelRND.getTotalPoint();

//            if(grade >= 0 && grade < gradename.size())
            if(Integer.parseInt(sum) >= 0 && grade < gradename.size()) {
//                tvfebricGrade.setText(String.valueOf(gradename.get(grade)));
                tvfebricGrade.setText(String.valueOf(gradename.get(Integer.parseInt(sum))));
//                tvfebricGrade.setText(buyer_grade);
                tvfebricGrade.setTextColor(Color.RED);

            }
            else
            {
                //tvfebricGrade.setText("Rejected");
//                tvfebricGrade.setText(buyer_grade);
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

        Log.d(TAG, "savemethod: " + ObsfoundInInchArray +"\n"+obsDefectNameArray);


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


                if(Integer.parseInt(tvtotalPennalty.getText().toString().trim()) < 1)
                {
                    Toast.makeText(this, "Select defect name", Toast.LENGTH_SHORT).show();
                }
//                else if(TextUtils.isEmpty(String.valueOf(qc_pass_qtyET.getText().toString().trim()))){
//                    qc_pass_qtyET.setError("Fill the credential");
//                }else if(TextUtils.isEmpty(String.valueOf(rejectET.getText().toString().trim()))){
//                    rejectET.setError("Fill the credential");
//                }
                else {
                    //statusSpinnerItem();
                    postDataToServer();
                }

                //postDataToServer();

                break;
            case R.id.btnTryAgain:
                Intent intent = new Intent(V1_FinishFabricObsActivity.this, V1_BatchDetailsActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("url", urladdress);
                intent.putExtra("s", savemenu);
                intent.putExtra("u", updatemenu);
                intent.putExtra("qc", qc_entry);
                intent.putExtra("batch_no", batch);
                startActivity(intent);
                break;
            case R.id.TVDate:
                datePickerMethod();
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

        if(observationBtn.getText().toString().equals("SHOW OBSERVATION")){
            observationBtn.setText("FOUR POINT QC");
            gridView.setVisibility(View.GONE);
            obs_gridView.setVisibility(View.VISIBLE);
        }else {
            observationBtn.setText("SHOW OBSERVATION");
            obs_gridView.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
        }

    }


    private void postDataToServer() {
        // perform HTTP POST request
//        if(checkNetworkConnection())
//
//            new V1_FinishFabricObsActivity.HTTPAsyncTask().execute(String.format("%s"+"logic-api/index.php/api/android/save_update_finish_fabric_qc_by_batch", urladdress));
//
//        else
//            Toast.makeText(this, "Not Connected!", Toast.LENGTH_SHORT).show();
        checkNetworkConnection();

        JSONObject save_obj = new JSONObject();
        JSONObject data_obj = new JSONObject();
        JSONObject index_obj = new JSONObject();
        JSONArray dtls_arr = new JSONArray();
        JSONArray obs_dtls_arr = new JSONArray();

        try {
            save_obj.put("status",true);
            save_obj.put("mode", "save");
            save_obj.put("MST_ID", mst_id);
            //save_obj.put("PROD_ID", prod_id);
            //save_obj.put("TRANS_ID", trans_id);
            save_obj.put("DTLS_ID", dtls_id);
            save_obj.put("QC_MST_ID", qc_mst_id);

            //save_obj.put("UPDATE_ID", updatedID);

            //index_obj.put("BARCODE_NO",fBarcode);
            index_obj.put("RECEIVE_NO", system_id);
            index_obj.put("BATCH_ID", fbatch_id);
            index_obj.put("BATCH_NO", batch_No);
            index_obj.put("BUYER_ID", buyer_id);
            index_obj.put("QC_DATE", date);
            index_obj.put("QC_NAME", mQCName.getText().toString());
            index_obj.put("ACTUAL_DIA", mActualDia.getText().toString());
            index_obj.put("ACTUAL_GSM", mActualGsm.getText().toString());
            index_obj.put("ROLL_WT_KG", mRollKg.getText().toString());
            index_obj.put("ROLL_WT_YDS", mRollYds.getText().toString());
            index_obj.put("REJECT_QTY", mReject.getText().toString());
            index_obj.put("GSM", gsm);
            index_obj.put("DIA", dia);
            index_obj.put("FEBRIC_DES_ID", fab_des_id);
            index_obj.put("MACHINE_ID", machine_id);
            index_obj.put("LENGTH_PERCENT", mLength.getText().toString());
            index_obj.put("WIDTH_PERCENT", mWidth.getText().toString());
            index_obj.put("TWISTING_PERCENT", mTwisting.getText().toString());


            index_obj.put("COMMENTS", etcomments.getText().toString());
            index_obj.put("INSERTED_BY",userId);

            index_obj.put("TOTAL_PENALTY_POINT",tvtotalPennalty.getText().toString());
            index_obj.put("TOTAL_POINT",tvpoint.getText().toString());
            index_obj.put("FABRIC_GRADE", buyer_grade);

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
                    //obs_dtls_obj.put("OBS_INCH", observationmodelArrayList.get(i).getObservationInchSpinner()+1);
                    obs_dtls_obj.put("OBS_INCH", obsDefectKey.get(observationmodelArrayList.get(i).getObservationInchSpinner()));
                    obs_dtls_obj.put("OBS_DEPARTMENT", observationmodelArrayList.get(i).getObservationDepartmentSpinner());
                    obs_dtls_arr.put(obs_dtls_obj);
                }
            }

            data_obj.put("DEFECT_LIST",dtls_arr);
            data_obj.put("OBSERVATION_LIST",obs_dtls_arr);

            save_obj.put("data", data_obj);
            Log.d(TAG, "buidJsonObject: ###### "+ save_obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, save_obj.toString());
        progressBar.setVisibility(View.VISIBLE);
        apiInterface.saveupdatefinishFabriccall(body).enqueue(new Callback<V1_DataSaveResponse>() {
            @Override
            public void onResponse(Call<V1_DataSaveResponse> call, Response<V1_DataSaveResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    Toast.makeText(V1_FinishFabricObsActivity.this, response.body().getResultset(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(V1_FinishFabricObsActivity.this, V1_BatchDetailsActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("url", urladdress);
                    intent.putExtra("s", savemenu);
                    intent.putExtra("u", updatemenu);
                    intent.putExtra("qc", qc_entry);
                    intent.putExtra("batch_no", batch);

                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<V1_DataSaveResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(V1_FinishFabricObsActivity.this, "Failed, Please try again.", Toast.LENGTH_SHORT).show();
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
//                Intent intent = new Intent(GreyFabricActivity.this, QRBarcodeScannerActivity.class);
//                intent.putExtra("userId", userId);
//                intent.putExtra("url", urladdress);
//                startActivity(intent);
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
//                    Intent intent = new Intent(V1_FinishFabricObsActivity.this, V1_BatchDetailsActivity.class);
//                    intent.putExtra("userId", userId);
//                    intent.putExtra("url", urladdress);
//                    intent.putExtra("s", savemenu);
//                    intent.putExtra("u", updatemenu);
//                    intent.putExtra("qc", qc_entry);
//                    intent.putExtra("batch_no", batch);
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
//
//        JSONObject save_obj = new JSONObject();
//        JSONObject data_obj = new JSONObject();
//        JSONObject index_obj = new JSONObject();
//        JSONArray dtls_arr = new JSONArray();
//        JSONArray obs_dtls_arr = new JSONArray();
//
//        save_obj.put("status",true);
//        save_obj.put("mode", "save");
//        save_obj.put("MST_ID", mst_id);
//        //save_obj.put("PROD_ID", prod_id);
//        //save_obj.put("TRANS_ID", trans_id);
//        save_obj.put("DTLS_ID", dtls_id);
//        save_obj.put("QC_MST_ID", qc_mst_id);
//
//        //save_obj.put("UPDATE_ID", updatedID);
//
//        //index_obj.put("BARCODE_NO",fBarcode);
//        index_obj.put("RECEIVE_NO", system_id);
//        index_obj.put("BATCH_ID", fbatch_id);
//        index_obj.put("BATCH_NO", batch_No);
//        index_obj.put("BUYER_ID", buyer_id);
//        index_obj.put("QC_DATE", date);
//        index_obj.put("QC_NAME", mQCName.getText().toString());
//        index_obj.put("ACTUAL_DIA", mActualDia.getText().toString());
//        index_obj.put("ACTUAL_GSM", mActualGsm.getText().toString());
//        index_obj.put("ROLL_WT_KG", mRollKg.getText().toString());
//        index_obj.put("ROLL_WT_YDS", mRollYds.getText().toString());
//        index_obj.put("REJECT_QTY", mReject.getText().toString());
//        index_obj.put("GSM", gsm);
//        index_obj.put("DIA", dia);
//        index_obj.put("FEBRIC_DES_ID", fab_des_id);
//        index_obj.put("MACHINE_ID", machine_id);
//        index_obj.put("LENGTH_PERCENT", mLength.getText().toString());
//        index_obj.put("WIDTH_PERCENT", mWidth.getText().toString());
//        index_obj.put("TWISTING_PERCENT", mTwisting.getText().toString());
//
//
//        index_obj.put("COMMENTS", etcomments.getText().toString());
//        index_obj.put("INSERTED_BY",userId);
//
//        index_obj.put("TOTAL_PENALTY_POINT",tvtotalPennalty.getText().toString());
//        index_obj.put("TOTAL_POINT",tvpoint.getText().toString());
//        index_obj.put("FABRIC_GRADE", buyer_grade);
//
//        data_obj.put("index",index_obj);
//
//
//        if(gradeArray != null && gradeArray.length > 0) {
//            for (int i = 0; i < gradeArray.length; i++) {
//
//                if (modelArrayList.get(i).getQcDefectNumber() > 0) {
//                    JSONObject dtls_obj = new JSONObject();
//                    dtls_obj.put("DEFECT_ID", modelArrayList.get(i).getDefectID());
//                    dtls_obj.put("COUNT", modelArrayList.get(i).getQcItemNumber());
//                    dtls_obj.put("INCH_ID", modelArrayList.get(i).getSpinneritem());
//                    dtls_obj.put("PENALTY", modelArrayList.get(i).getQcDefectNumber());
//                    dtls_arr.put(dtls_obj);
//                }
//
//            }
//        }
//
//        if(obsDefectNameArray != null && obsDefectNameArray.length > 0){
//            for(int i = 0; i < obsDefectNameArray.length; i++){
//                JSONObject obs_dtls_obj = new JSONObject();
//                obs_dtls_obj.put("OBS_ID", observationmodelArrayList.get(i).getDefectId());
//                //obs_dtls_obj.put("OBS_INCH", observationmodelArrayList.get(i).getObservationInchSpinner()+1);
//                obs_dtls_obj.put("OBS_INCH", obsDefectKey.get(observationmodelArrayList.get(i).getObservationInchSpinner()));
//                obs_dtls_obj.put("OBS_DEPARTMENT", observationmodelArrayList.get(i).getObservationDepartmentSpinner());
//                obs_dtls_arr.put(obs_dtls_obj);
//            }
//        }
//
//        data_obj.put("DEFECT_LIST",dtls_arr);
//        data_obj.put("OBSERVATION_LIST",obs_dtls_arr);
//
//        save_obj.put("data", data_obj);
//        Log.d(TAG, "buidJsonObject: ###### "+ save_obj);
//        return save_obj;
//    }



    private void setPostRequestContent(HttpURLConnection conn, JSONObject jsonObject) throws IOException {

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonObject.toString());
        Log.i(V1_FinishFabricObsActivity.class.toString(), jsonObject.toString());
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
            Toast.makeText(V1_FinishFabricObsActivity.this, date, Toast.LENGTH_SHORT).show();
        }
    };



    private void logoutUser() {
        session.setLogin(false);
        dbAdapter.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(V1_FinishFabricObsActivity.this, V1_LoginActivity.class);
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
        Intent intent = new Intent(V1_FinishFabricObsActivity.this, V1_BatchDetailsActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("url", urladdress);
        intent.putExtra("s", savemenu);
        intent.putExtra("u", updatemenu);
        intent.putExtra("qc", qc_entry);
        intent.putExtra("batch_no", batch);
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