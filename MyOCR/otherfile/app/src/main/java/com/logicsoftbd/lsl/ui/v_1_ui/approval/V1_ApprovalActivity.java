package com.logicsoftbd.lsl.ui.v_1_ui.approval;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.db.v1_db.helper.DBAdapter;
import com.logicsoftbd.lsl.data.db.v1_db.model.V1_User;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ApprovalDetailsModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ApprovalModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ApprovalModelClass;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.v_1_ui.menu.V1_MenuActivity;
import com.logicsoftbd.lsl.utils.ApiUtils;
import com.logicsoftbd.lsl.utils.VerticalSpacingItemDecorator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class V1_ApprovalActivity extends AppCompatActivity implements PIRecyclerViewAdapter.OnHeadListener, View.OnClickListener {
    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, V1_ApprovalActivity.class);
        return intent;
    }
    public final ArrayList<String> pageIdList = new ArrayList<>();
    public final ArrayList<String> appTitleList = new ArrayList<>();
    public final ArrayList<String> companyIdList = new ArrayList<>();
    public final ArrayList<String> appIdList = new ArrayList<>();
    public final ArrayList<String> message1List = new ArrayList<>();
    public final ArrayList<String> message2List = new ArrayList<>();
    public final ArrayList<String> message3List = new ArrayList<>();
    public final ArrayList<String> message4List = new ArrayList<>();


    public String[] pageIdArray;
    public String[] appTitleArray;
    public String[] companyIdArray;
    public String[] appIdArray;
    public String[] message1Array;
    public String[] message2Array;
    public String[] message3Array;
    public String[] message4Array;

    public static ArrayList<V1_ApprovalModelClass> modelArrayList = new ArrayList<>();

    private String urlString, s_b_appId, s_b_companyId,  s_b_pageId;
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;
    private Toolbar mToolbar;
    private ProgressDialog pDialog;
    private RecyclerView mRecyclerView;
    private PIRecyclerViewAdapter mRecyclerViewAdapter;

    private TextView text1, text2, text3, text4, headerText;
    private EditText approvalAction;
    private AlertDialog.Builder customAddItemDialog;

    private   int userID = 0;
    private String urladdress, urladdressChk, base_url;
    private DBAdapter dbAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_approval);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = findViewById(R.id.headerRecyclerView);
        customAddItemDialog = new AlertDialog.Builder(this);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Intent intent = getIntent();
        userID = intent.getIntExtra("userId", 0);
        urladdress = intent.getStringExtra("url");
        urladdressChk = intent.getStringExtra("url");
        ArrayList<V1_User> loginData = new DBAdapter(this).getLoginData();



        if(urladdressChk != null)
        {
            urladdress = urladdressChk;
            userID = intent.getIntExtra("userId", 0);

        }else {
            if(loginData.size() > 0){
                urladdress = loginData.get(0).getUrl();
                userID = Integer.parseInt(loginData.get(0).getUserId());
            }
        }

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        base_url = (_preferences.getString("base_url", ""));

        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);

        sendRequestToServer();
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);
        mRecyclerViewAdapter = new PIRecyclerViewAdapter(modelArrayList, this);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
    }

    private void sendRequestToServer() {
        showDialog();
        apiInterface.getApprovalModelCall(userID, 0 , 0, 0).enqueue(new Callback<V1_ApprovalModel>() {
            @Override
            public void onResponse(Call<V1_ApprovalModel> call, Response<V1_ApprovalModel> response) {
                if(response.isSuccessful()){
                    hideDialog();

                    V1_ApprovalModel.Datum pageId;
                    List<V1_ApprovalModel.Datum> pageIds = response.body().getData();
                    for(V1_ApprovalModel.Datum d : pageIds)
                    {
                        pageId = d;
                        final V1_ApprovalModel.Datum finalName = pageId;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pageIdList.add(String.valueOf(finalName.getPAGEID()));
                            }
                        });
                    }

                    V1_ApprovalModel.Datum appTitle;
                    List<V1_ApprovalModel.Datum> appTitles = response.body().getData();
                    for(V1_ApprovalModel.Datum d : appTitles)
                    {
                        appTitle = d;
                        final V1_ApprovalModel.Datum finalName = appTitle;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                appTitleList.add(String.valueOf(finalName.getAPPTITLE()));
                            }
                        });
                    }

                    V1_ApprovalModel.Datum companyId;
                    List<V1_ApprovalModel.Datum> companyIds = response.body().getData();
                    for(V1_ApprovalModel.Datum d : companyIds)
                    {
                        companyId = d;
                        final V1_ApprovalModel.Datum finalName = companyId;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                companyIdList.add(String.valueOf(finalName.getCOMPANYID()));
                            }
                        });
                    }

                    V1_ApprovalModel.Datum appId;
                    List<V1_ApprovalModel.Datum> appIds = response.body().getData();
                    for(V1_ApprovalModel.Datum d : appIds)
                    {
                        appId = d;
                        final V1_ApprovalModel.Datum finalName = appId;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                appIdList.add(String.valueOf(finalName.getAPPID()));
                            }
                        });
                    }

                    V1_ApprovalModel.Datum message1;
                    List<V1_ApprovalModel.Datum> message1s = response.body().getData();
                    for(V1_ApprovalModel.Datum d : message1s)
                    {
                        message1 = d;
                        final V1_ApprovalModel.Datum finalName = message1;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                message1List.add(String.valueOf(finalName.getMESSAGE1()));
                            }
                        });
                    }

                    V1_ApprovalModel.Datum message2;
                    List<V1_ApprovalModel.Datum> message2s = response.body().getData();
                    for(V1_ApprovalModel.Datum d : message2s)
                    {
                        message2 = d;
                        final V1_ApprovalModel.Datum finalName = message2;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                message2List.add(String.valueOf(finalName.getMESSAGE2()));
                            }
                        });
                    }

                    V1_ApprovalModel.Datum message3;
                    List<V1_ApprovalModel.Datum> message3s = response.body().getData();
                    for(V1_ApprovalModel.Datum d : message3s)
                    {
                        message2 = d;
                        final V1_ApprovalModel.Datum finalName = message2;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                message3List.add(String.valueOf(finalName.getMESSAGE3()));
                            }
                        });
                    }

                    V1_ApprovalModel.Datum message4;
                    List<V1_ApprovalModel.Datum> message4s = response.body().getData();
                    for(V1_ApprovalModel.Datum d : message4s)
                    {
                        message4 = d;
                        final V1_ApprovalModel.Datum finalName = message4;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                message4List.add(String.valueOf(finalName.getMESSAGE4()));
                            }
                        });
                    }



                    modelArrayList = getModel();
                    initRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<V1_ApprovalModel> call, Throwable t) {
                hideDialog();
                Toast.makeText(V1_ApprovalActivity.this, String.valueOf(t.getMessage()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<V1_ApprovalModelClass> getModel() {
        ArrayList<V1_ApprovalModelClass> list = new ArrayList<>();

        pageIdArray = new String[pageIdList.size()];
        for(int i = 0; i < pageIdList.size(); i++)
        {
            pageIdArray[i] = pageIdList.get(i);
        }

        appTitleArray = new String[appTitleList.size()];
        for(int i = 0; i < appTitleList.size(); i++)
        {
            appTitleArray[i] = appTitleList.get(i);
        }

        companyIdArray = new String[companyIdList.size()];
        for(int i = 0; i < companyIdList.size(); i++)
        {
            companyIdArray[i] = companyIdList.get(i);
        }

        appIdArray = new String[appIdList.size()];
        for(int i = 0; i < appIdList.size(); i++)
        {
            appIdArray[i] = appIdList.get(i);
        }

        message1Array = new String[message1List.size()];
        for(int i = 0; i < message1List.size(); i++)
        {
            message1Array[i] = message1List.get(i);
        }

        message2Array = new String[message2List.size()];
        for(int i = 0; i < message2List.size(); i++)
        {
            message2Array[i] = message2List.get(i);
        }

        message3Array = new String[message3List.size()];
        for(int i = 0; i < message3List.size(); i++)
        {
            message3Array[i] = message3List.get(i);
        }

        message4Array = new String[message4List.size()];
        for(int i = 0; i < message4List.size(); i++)
        {
            message4Array[i] = message4List.get(i);
        }


        for(int i = 0; i < pageIdArray.length; i++){
            V1_ApprovalModelClass approvalModelClass = new V1_ApprovalModelClass();
            approvalModelClass.setPageId(pageIdArray[i]);
            approvalModelClass.setAppTitle(appTitleArray[i]);
            approvalModelClass.setCompanyId(companyIdArray[i]);
            approvalModelClass.setAppId(appIdArray[i]);
            approvalModelClass.setMessage1(message1Array[i]);
            approvalModelClass.setMessage2(message2Array[i]);
            approvalModelClass.setMessage3(message3Array[i]);
            approvalModelClass.setMessage4(message4Array[i]);

            list.add(approvalModelClass);
        }
        return list;
    }

    private void postDataToServer() {
        // perform HTTP POST request
        if(checkNetworkConnection())
            new HTTPAsyncTask().execute(String.format("%s"+"logic-api/index.php/api/android/save_update_all_approval", urladdress));
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
    public void onHeadClick(final int position) {

        int companyId = Integer.parseInt(modelArrayList.get(position).getCompanyId());
        int pageId = Integer.parseInt(modelArrayList.get(position).getPageId());
        String app_id = modelArrayList.get(position).getAppId();
        sendRequestToServerForApprovalDetails(companyId, pageId, app_id);
    }

    private void sendRequestToServerForApprovalDetails(int companyId, int pageId, String app_id) {
        apiInterface.getFabricBookingModelCall(userID, companyId, pageId, app_id).enqueue(new Callback<V1_ApprovalDetailsModel>() {
            @Override
            public void onResponse(Call<V1_ApprovalDetailsModel> call, Response<V1_ApprovalDetailsModel> response) {
                if(response.isSuccessful()){
                    LayoutInflater inflaterlayout = getLayoutInflater();
                    final View new_v = inflaterlayout.inflate(R.layout.approval_dialog, null);

                    headerText = new_v.findViewById(R.id.headerDialog);
                    text1 = new_v.findViewById(R.id.text1);
                    text2 = new_v.findViewById(R.id.text2);
                    text3 = new_v.findViewById(R.id.text3);
                    text4 = new_v.findViewById(R.id.text4);
                    approvalAction = new_v.findViewById(R.id.approvedActionText);

                    s_b_appId = response.body().getData().getAPPID();
                    s_b_companyId = response.body().getData().getCOMPANYID();
                    s_b_pageId = response.body().getData().getPAGEID();


                    headerText.setText(response.body().getData().getAPPID()+" "+response.body().getData().getAPPTITLE());
                    text1.setText(response.body().getData().getMESSAGE1());
                    text2.setText(response.body().getData().getMESSAGE2());
                    text3.setText(response.body().getData().getMESSAGE3());
                    text4.setText(response.body().getData().getMESSAGE4());

                    customAddItemDialog.setTitle(response.body().getData().getAPPTITLE());
                    customAddItemDialog.setView(new_v);
                    customAddItemDialog.setPositiveButton("APPROVED", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            postDataToServer();
                        }
                    });
                    customAddItemDialog.setNegativeButton("NON-APPROVED", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            postDataToServer();
                        }
                    });

                    customAddItemDialog.show();
                }
            }

            @Override
            public void onFailure(Call<V1_ApprovalDetailsModel> call, Throwable t) {
                Toast.makeText(V1_ApprovalActivity.this, String.valueOf(t.getMessage()), Toast.LENGTH_SHORT).show();
            }
        });

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
            Toast.makeText(V1_ApprovalActivity.this, String.valueOf("Successfully Approved"), Toast.LENGTH_SHORT).show();
            if(result.equals("OK")){
                Intent intent = V1_ApprovalActivity.getStartIntent(V1_ApprovalActivity.this);
                startActivity(intent);
            }
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
        Log.i(V1_ApprovalActivity.class.toString(), jsonObject.toString());
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
        save_obj.put("UPDATE_ID", 0);

        index_obj.put("company_id", s_b_companyId);
        index_obj.put("app_id", s_b_appId);
        index_obj.put("page_id", s_b_pageId);
        index_obj.put("user_id", 1);

        data_obj.put("index", index_obj);

        save_obj.put("data", data_obj);

        return save_obj;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.toolbar:
                Intent intent = V1_MenuActivity.getStartIntent(V1_ApprovalActivity.this);
                startActivity(intent);
                finish();
        }
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

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = V1_MenuActivity.getStartIntent(V1_ApprovalActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}