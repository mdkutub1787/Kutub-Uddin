package com.logicsoftbd.lsl.ui.v_1_ui.report;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.db.v1_db.helper.DBAdapter;
import com.logicsoftbd.lsl.data.db.v1_db.model.V1_User;
import com.logicsoftbd.lsl.data.network.v1_model.V1_QcReportModelClass;
import com.logicsoftbd.lsl.data.prefs.SessionManager;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.utils.ApiUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class V1_QcReportActivity extends AppCompatActivity implements View.OnClickListener{


    private LinearLayout ln;
    private Button allinfo, basicinfo, knittinginfo, yarninfo, batchinfo, qainfo;

    private TextView allinfoView, basicinfoView, knittinginfoView, yarninfoView, lotinfoView, batchinfoView, qainfoView;

    private String base_url ="", urladdress, urlString, barcodeNumber, urladdressChk, buyer, job, style, productionid, date, description, yarncount,
            lot, brand, batchno, batch_date, colorid, qcstatus, qcdate, qcweight, grade, gradepoint;

    public int userId = 0;


    //Retrofit
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;

    private SessionManager session;
    private DBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_qc_report);

//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialization();
    }

    private void initialization() {

        ln = findViewById(R.id.liniarlayout);
        allinfo = findViewById(R.id.allinfoBtn);
        basicinfo = findViewById(R.id.basicinfoBtn);
        knittinginfo = findViewById(R.id.knittinginfoBtn);
        yarninfo = findViewById(R.id.yarninfoBtn);
        batchinfo = findViewById(R.id.batchinfoBtn);
        qainfo = findViewById(R.id.qainfoBtn);


        allinfoView = findViewById(R.id.allinfoTV);
        basicinfoView = findViewById(R.id.basicinfoTV);
        knittinginfoView = findViewById(R.id.knittinginfoTV);
        yarninfoView = findViewById(R.id.yarninfoTV);
        batchinfoView = findViewById(R.id.batchinfoTV);
        qainfoView = findViewById(R.id.qainfoTV);

        allinfo.setOnClickListener(this);
        basicinfo.setOnClickListener(this);
        knittinginfo.setOnClickListener(this);
        yarninfo.setOnClickListener(this);
        batchinfo.setOnClickListener(this);
        qainfo.setOnClickListener(this);

        Intent intent = getIntent();
        String resultS = intent.getStringExtra("result");
        urladdressChk = intent.getStringExtra("url");

        ArrayList<V1_User> loginData = new DBAdapter(this).getLoginData();
        if(urladdressChk != null)
        {
            urladdress = urladdressChk;
            userId = intent.getIntExtra("userId", 0);

        }else {
            urladdress = loginData.get(0).getUrl();
            userId = Integer.parseInt(loginData.get(0).getUserId());
        }
        barcodeNumber = resultS;

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        base_url = (_preferences.getString("base_url", ""));

        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);

        sendRequestToServer();

        // session manager
        session = new SessionManager(getApplicationContext());
        dbAdapter = new DBAdapter(this);
    }

    private void sendRequestToServer() {
        apiInterface.getQcReportModelClassCall(barcodeNumber).enqueue(new Callback<V1_QcReportModelClass>() {
            @Override
            public void onResponse(Call<V1_QcReportModelClass> call, Response<V1_QcReportModelClass> response) {
                if(response.isSuccessful())
                {
                    ln.setVisibility(View.VISIBLE);
                    buyer = response.body().getData().getBASICINFO().getBUYER();
                    job = response.body().getData().getBASICINFO().getJOB();
                    style = response.body().getData().getBASICINFO().getSTYLE();
                    productionid = response.body().getData().getKNITTINGINFO().getPRODUCTIONID();
                    date = response.body().getData().getKNITTINGINFO().getDATE();
                    description = response.body().getData().getYARNINFO().getDESCRIPTION();
                    yarncount = response.body().getData().getYARNINFO().getYARNCOUNT();
                    lot = response.body().getData().getYARNINFO().getLOT();
                    brand = response.body().getData().getYARNINFO().getBRAND();
                    batchno = response.body().getData().getBATCHINFO().getBATCHNO();
                    batch_date = response.body().getData().getBATCHINFO().getBATCHDATE();
                    colorid = response.body().getData().getBATCHINFO().getCOLORID();
                    qcstatus = response.body().getData().getQAINFO().getQCSTATUS();
                    qcdate = response.body().getData().getQAINFO().getQCDATE();
                    qcweight = String.valueOf(response.body().getData().getQAINFO().getROLLWEIGHT());
                    grade = response.body().getData().getQAINFO().getFABRICGRADE();
                    gradepoint = String.valueOf(response.body().getData().getQAINFO().getTOTALPOINT());
                }
                else
                {
                    Toast.makeText(V1_QcReportActivity.this, "Invalid Id", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<V1_QcReportModelClass> call, Throwable t) {
                ln.setVisibility(View.GONE);
                Toast.makeText(V1_QcReportActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.allinfoBtn:
                knittinginfoView.setVisibility(View.GONE);
                basicinfoView.setVisibility(View.GONE);
                yarninfoView.setVisibility(View.GONE);
                batchinfoView.setVisibility(View.GONE);
                qainfoView.setVisibility(View.GONE);
                allinfoView.setVisibility(View.VISIBLE);
                allinfoView.setText(new StringBuilder().append("BUYER: ").append(buyer).append("\n").append("JOB: ").append(job).append("\n").append("STYLE: ").append(style).append("\n").append("PRODUCTION ID: ").append(productionid).append("\n").append("DATE: ").append(date).append("\n").append("DESCRIPTION: ").append(description).append("\n").append("YARN COUNT: ").append(yarncount).append("\n").append("LOT: ").append(lot).append("\n").append("BRAND: ").append(brand).append("\n").append("BATCH NO: ").append(batchno).append("\n").append("BATCH DATE: ").append(batch_date).append("\n").append("COLOR ID: ").append(colorid).append("\n").append("QC STATUS: ").append(qcstatus).append("\n").append("QC DATE: ").append(qcdate).append("\n").append("ROLL WEIGHT: ").append(qcweight).append("\n").append("GRADE: ").append(grade).append("\n").append("GRADE POINT: ").append(gradepoint).append("\n").toString()
                );
                break;
            case R.id.basicinfoBtn:
                allinfoView.setVisibility(View.GONE);
                knittinginfoView.setVisibility(View.GONE);
                yarninfoView.setVisibility(View.GONE);
                batchinfoView.setVisibility(View.GONE);
                qainfoView.setVisibility(View.GONE);
                basicinfoView.setVisibility(View.VISIBLE);
                basicinfoView.setText(new StringBuilder().append("BUYER: ").append(buyer).append("\n").append("JOB: ").append(job).append("\n").append("STYLE: ").append(style).append("\n").toString());
                break;
            case R.id.knittinginfoBtn:
                allinfoView.setVisibility(View.GONE);
                basicinfoView.setVisibility(View.GONE);
                yarninfoView.setVisibility(View.GONE);
                batchinfoView.setVisibility(View.GONE);
                qainfoView.setVisibility(View.GONE);
                knittinginfoView.setVisibility(View.VISIBLE);
                knittinginfoView.setText(new StringBuilder().append("PRODUCTION ID: ").append(productionid).append("\n").append("DATE: ").append(date).append("\n").toString());
                break;
            case R.id.yarninfoBtn:
                allinfoView.setVisibility(View.GONE);
                basicinfoView.setVisibility(View.GONE);
                knittinginfoView.setVisibility(View.GONE);
                batchinfoView.setVisibility(View.GONE);
                qainfoView.setVisibility(View.GONE);
                yarninfoView.setVisibility(View.VISIBLE);
                yarninfoView.setText(new StringBuilder().append("DESCRIPTION: ").append(description).append("\n").append("YARN COUNT: ").append(yarncount).append("\n").append("LOT: ").append(lot).append("\n").append("BRAND: ").append(brand).append("\n").toString());
                break;
            case R.id.batchinfoBtn:
                allinfoView.setVisibility(View.GONE);
                basicinfoView.setVisibility(View.GONE);
                knittinginfoView.setVisibility(View.GONE);
                yarninfoView.setVisibility(View.GONE);
                qainfoView.setVisibility(View.GONE);
                batchinfoView.setVisibility(View.VISIBLE);
                batchinfoView.setText(new StringBuilder().append("BATCH NO: ").append(batchno).append("\n").append("BATCH DATE: ").append(batch_date).append("\n").append("COLOR ID: ").append(colorid).append("\n").toString());
                break;
            case R.id.qainfoBtn:
                allinfoView.setVisibility(View.GONE);
                basicinfoView.setVisibility(View.GONE);
                knittinginfoView.setVisibility(View.GONE);
                yarninfoView.setVisibility(View.GONE);
                batchinfoView.setVisibility(View.GONE);
                qainfoView.setVisibility(View.VISIBLE);
                qainfoView.setText(new StringBuilder().append("QC STATUS: ").append(qcstatus).append("\n").append("QC DATE: ").append(qcdate).append("\n").append("ROLL WEIGHT: ").append(qcweight).append("\n").append("GRADE: ").append(grade).append("\n").append("GRADE POINT: ").append(gradepoint).append("\n").toString());
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(V1_QcReportActivity.this, V1_ReportHomeActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("url", urladdress);
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
