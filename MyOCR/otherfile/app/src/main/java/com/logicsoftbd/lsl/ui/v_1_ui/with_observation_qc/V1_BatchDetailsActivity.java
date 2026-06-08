package com.logicsoftbd.lsl.ui.v_1_ui.with_observation_qc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.db.v1_db.helper.DBAdapter;
import com.logicsoftbd.lsl.data.db.v1_db.model.V1_User;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BatchModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BatchModelClass;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.v_1_ui.menu.V1_MenuActivity;
import com.logicsoftbd.lsl.utils.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class V1_BatchDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "V1_BatchDetailsActivity";

    private ApiInterface apiInterface;
    private ApiUtils apiUtils;

    private String base_url = "", urladdressChk, urladdress, urlString, batchNo="";
    public int userId = 0, savemenu = 0, updatemenu = 0;

    private EditText batch;
    private Button submit;

    private final ArrayList<String> receive_no_list = new ArrayList<>();
    private final ArrayList<String> description_list = new ArrayList<>();
    private final ArrayList<String> receieve_id_list = new ArrayList<>();
    private final ArrayList<String> pro_dtls_list = new ArrayList<>();
    private final ArrayList<String> batch_no_list = new ArrayList<>();
    private final ArrayList<String> batch_id_list = new ArrayList<>();
    private final ArrayList<String> prod_id_list = new ArrayList<>();
    private final ArrayList<String> buyer_id_list = new ArrayList<>();

    private String[] receive_no_Array;
    private String[] description_Array;
    private String[] receive_id_Array;
    private String[] prod_dtls_Array;
    private String[] batch_no_Array;
    private String[] batch_id_Array;
    private String[] prod_id_Array;
    private String[] buyer_id_Array;

    public static ArrayList<V1_BatchModel> modelArrayList;
    private GridView batchGrid;
    private V1_BatchAdapter batchAdapter;

    private String prod_dlts = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_batch_details);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialization();
    }

    private void initialization() {

        batch = findViewById(R.id.batch);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);
        batchGrid = findViewById(R.id.batchInfo);


        Intent intent = getIntent();

        urladdressChk = intent.getStringExtra("url");
        savemenu = intent.getIntExtra("s", 0);
        updatemenu = intent.getIntExtra("u", 0);
        prod_dlts = intent.getStringExtra("batch_no");

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

        batch.setText(prod_dlts);

//        if(!prod_dlts.isEmpty()){
//            batch.setText(prod_dlts);
//            requestServer(prod_dlts);
//        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:{
                batchNo = String.valueOf(batch.getText().toString().trim());
                if(batch.getText().toString().trim().isEmpty()){
                    batch.setError("Batch no empty");
                }else {
                    requestServer(batchNo.replace("/", ""));
                }
            }
        }
    }

    private void requestServer(String batchNo) {
        apiInterface.getBatchModelClassCall(batchNo, 1).enqueue(new Callback<V1_BatchModelClass>() {
            @Override
            public void onResponse(Call<V1_BatchModelClass> call, Response<V1_BatchModelClass> response) {
                if(response.isSuccessful()){
                    try{
                        batch.setText("");
                        V1_BatchModelClass.Datum receive_no;
                        List<V1_BatchModelClass.Datum> receive_data = response.body().getData();
                        for(V1_BatchModelClass.Datum d : receive_data){
                            receive_no = d;
                            final V1_BatchModelClass.Datum finalName = receive_no;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    receive_no_list.add(finalName.getRECVNUMBER());
                                }
                            });
                        }

                        V1_BatchModelClass.Datum description;
                        List<V1_BatchModelClass.Datum> description_data = response.body().getData();
                        for(V1_BatchModelClass.Datum d : description_data){
                            description = d;
                            final V1_BatchModelClass.Datum finalName = description;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    description_list.add(finalName.getFABRICDESCRIPTIONID());
                                }
                            });
                        }

                        V1_BatchModelClass.Datum receive_id;
                        List<V1_BatchModelClass.Datum> receive_id_data = response.body().getData();
                        for(V1_BatchModelClass.Datum d : receive_id_data){
                            receive_id = d;
                            final V1_BatchModelClass.Datum finalName = receive_id;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    receieve_id_list.add(finalName.getRECVID());
                                }
                            });
                        }

                        V1_BatchModelClass.Datum batch_no;
                        List<V1_BatchModelClass.Datum> batch_no_data = response.body().getData();
                        for(V1_BatchModelClass.Datum d : batch_no_data){
                            batch_no = d;
                            final V1_BatchModelClass.Datum finalName = batch_no;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    batch_no_list.add(finalName.getBATCHNO());
                                }
                            });
                        }

                        V1_BatchModelClass.Datum prod_dtls;
                        List<V1_BatchModelClass.Datum> prod_dtls_data = response.body().getData();
                        for(V1_BatchModelClass.Datum d : prod_dtls_data){
                            prod_dtls = d;
                            final V1_BatchModelClass.Datum finalName = prod_dtls;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pro_dtls_list.add(finalName.getPRODTLSID());
                                }
                            });
                        }

                        V1_BatchModelClass.Datum batch_id;
                        List<V1_BatchModelClass.Datum> batch_id_data = response.body().getData();
                        for(V1_BatchModelClass.Datum d : batch_id_data){
                            batch_id = d;
                            final V1_BatchModelClass.Datum finalName = batch_id;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    batch_id_list.add(finalName.getBATCHID());
                                }
                            });
                        }

                        V1_BatchModelClass.Datum prod_id;
                        List<V1_BatchModelClass.Datum> prod_id_data = response.body().getData();
                        for(V1_BatchModelClass.Datum d : prod_id_data){
                            prod_id = d;
                            final V1_BatchModelClass.Datum finalName = prod_id;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    prod_id_list.add(finalName.getPRODID());
                                }
                            });
                        }

                        V1_BatchModelClass.Datum buyer_id;
                        List<V1_BatchModelClass.Datum> buyer_id_data = response.body().getData();
                        for(V1_BatchModelClass.Datum d : buyer_id_data){
                            buyer_id = d;
                            final V1_BatchModelClass.Datum finalName = buyer_id;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    buyer_id_list.add(finalName.getBUYERID());
                                }
                            });
                        }

                        try{
                            modelArrayList = getModel();
                        }catch (NullPointerException e){

                        }

                        batchAdapter = new V1_BatchAdapter(V1_BatchDetailsActivity.this,R.layout.batch_details_layout, modelArrayList);
                        batchGrid.setAdapter(batchAdapter);

                        batchGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(V1_BatchDetailsActivity.this, V1_FinishFabricObsActivity.class);
                                intent.putExtra("userId", userId);
                                intent.putExtra("url", urladdress);
                                intent.putExtra("s", savemenu);
                                intent.putExtra("u", updatemenu);
                                intent.putExtra("batch_no", batchNo);
                                intent.putExtra("prod_dlts", String.valueOf(modelArrayList.get(position).getPro_dtls_id()));
                                startActivity(intent);
                                finish();
                            }
                        });
                    }catch (NullPointerException e){
                        Toast.makeText(V1_BatchDetailsActivity.this, "Batch is empty", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(V1_BatchDetailsActivity.this, "Batch is empty", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<V1_BatchModelClass> call, Throwable t) {
                Toast.makeText(V1_BatchDetailsActivity.this, String.valueOf(t.getMessage()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<V1_BatchModel> getModel() {

        ArrayList<V1_BatchModel> list = new ArrayList<>();

        receive_no_Array = new String[receive_no_list.size()];
        for(int i = 0; i < receive_no_list.size(); i++){
            receive_no_Array[i] = receive_no_list.get(i);
        }

        description_Array = new String[description_list.size()];
        for(int i = 0; i < description_list.size(); i++){
            description_Array[i] = description_list.get(i);
        }

        receive_id_Array = new String[receieve_id_list.size()];
        for(int i = 0; i < receieve_id_list.size(); i++){
            receive_id_Array[i] = receieve_id_list.get(i);
        }

        batch_no_Array = new String[batch_no_list.size()];
        for(int i = 0; i < batch_no_list.size(); i++){
            batch_no_Array[i] = batch_no_list.get(i);
        }


        prod_dtls_Array = new String[pro_dtls_list.size()];
        for(int i = 0; i < pro_dtls_list.size(); i++){
            prod_dtls_Array[i] = pro_dtls_list.get(i);
        }

        batch_id_Array = new String[batch_id_list.size()];
        for(int i = 0; i < batch_id_list.size(); i++){
            batch_id_Array[i] = batch_id_list.get(i);
        }

        prod_id_Array = new String[prod_id_list.size()];
        for(int i = 0; i < prod_id_list.size(); i++){
            prod_id_Array[i] = prod_id_list.get(i);
        }

        buyer_id_Array = new String[buyer_id_list.size()];
        for(int i = 0; i < buyer_id_list.size(); i++){
            buyer_id_Array[i] = buyer_id_list.get(i);
        }


        for(int i = 0; i < receive_no_Array.length; i++){
            V1_BatchModel batchModel = new V1_BatchModel();
            batchModel.setRecive_number(receive_no_Array[i]);
            batchModel.setFabric_description(description_Array[i]);
            batchModel.setRecive_id(receive_id_Array[i]);
            batchModel.setPro_dtls_id(prod_dtls_Array[i]);
            batchModel.setBatch_no(batch_no_Array[i]);
            batchModel.setBatch_id(batch_id_Array[i]);
            batchModel.setProd_id(prod_id_Array[i]);
            batchModel.setBuyer_id(buyer_id_Array[i]);

            list.add(batchModel);
        }

        return list;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(V1_BatchDetailsActivity.this, V1_MenuActivity.class);
        intent.putExtra("url", urladdress);
        intent.putExtra("s", savemenu);
        intent.putExtra("u", updatemenu);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
