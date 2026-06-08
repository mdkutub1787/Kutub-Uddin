package com.logicsoftbd.lsl.ui.v_1_ui.buyer_meeting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DataSaveResponse;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_QRBarcodeScannerActivity;
import com.logicsoftbd.lsl.utils.ApiUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class V1_MeetingArchiveActivity extends AppCompatActivity implements View.OnClickListener,
        V1_HangerArchiveRecyclerAdapter.OnDeleteSelectListener,
V1_HangerArchiveRecyclerAdapter.OnHeadSelectListener{
    private static final String TAG = "V1_MeetingArchiveActivi";

    Button _stickerScanBT, _saveBT, _refreshBT;
    ImageButton _bundleRefreshBT, _operationRefreshBT, _empIdRefreshBT;
    RecyclerView _hangerArchiveRecyclerView;
    TextView buyerNameTV, minutesTV;
    ProgressDialog pDialog;
    private Integer itemPosition = 0;
    private String base_url, stickerScan, stickerId, stickerText, buyerName, minutes, modified_Base_Url = "";
    private Button currentDateBtn, currentTimeBtn;

    private ArrayList<HangerArchiveModel> hangerArchiveModels = new ArrayList<>();
    private ArrayList<HangerArchiveModel> dataList = new ArrayList<>();
    private V1_HangerArchiveRecyclerAdapter _hangerArchiveRecyclerAdapter;

    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;

    private ApiInterface apiInterface;
    private ApiUtils apiUtils;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_meeting_archive);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        base_url = (_preferences.getString("base_url", ""));

        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);

        makeBaseUrl();

        initialization();
    }

    private void makeBaseUrl() {
        String[] parts = base_url.split("/");
        if (parts.length >= 4) {
            StringBuilder modified = new StringBuilder();
            for (int i = 0; i < parts.length - 4; i++) {
                modified.append(parts[i]);
                modified.append("/");
            }
            modified.deleteCharAt(modified.length() - 1);
            modified_Base_Url = modified.toString();
        }
    }

    private void initialization() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        _stickerScanBT = findViewById(R.id.stickerScanBT);
        _stickerScanBT.setOnClickListener(this);
        _saveBT = findViewById(R.id.saveBT);
        _saveBT.setOnClickListener(this);
        _refreshBT = findViewById(R.id.refreshBT);
        _refreshBT.setOnClickListener(this);

        buyerNameTV = findViewById(R.id.buyerNameTV);
        minutesTV = findViewById(R.id.minutesTV);

        currentDateBtn = findViewById(R.id.currentDateBtn);
        currentDateBtn.setOnClickListener(this);
        currentTimeBtn = findViewById(R.id.currentTimeBtn);
        currentTimeBtn.setOnClickListener(this);

        currentDateBtn.setText(currentDateTime("dd-MMMM-yyyy"));
        currentTimeBtn.setText(currentDateTime("hh:mm a"));

        _hangerArchiveRecyclerView = findViewById(R.id.hangerArchiveRecyclerView);
        initRecyclerView();

        Intent intent = getIntent();
        stickerScan = intent.getStringExtra("sticker");
        dataList = (ArrayList<HangerArchiveModel>) intent.getSerializableExtra("stickerDataList");
        buyerName = intent.getStringExtra("bayer_name");
        minutes = intent.getStringExtra("minutes");

        buyerNameTV.setText(buyerName);
        minutesTV.setText(minutes);
        
        if(stickerScan != null){
            String[] parts = stickerScan.split("\\*\\*");
            if(parts.length == 2){
                stickerId = parts[0];
                stickerText = parts[1];
            }else{
                showAlertMessage("This QR Code is not valid. Please try again.", 0);
            }
        }

        if(dataList != null){
            hangerArchiveModels = dataList;
            initRecyclerView();
//            _hangerArchiveRecyclerAdapter.notifyDataSetChanged();
        }
        getRequestForStickerData();
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _hangerArchiveRecyclerView.setLayoutManager(linearLayoutManager);
        _hangerArchiveRecyclerAdapter = new V1_HangerArchiveRecyclerAdapter(hangerArchiveModels, this, this, this);
        _hangerArchiveRecyclerView.setAdapter(_hangerArchiveRecyclerAdapter);
    }

    private void getRequestForStickerData() {
        if(isMatch()){
            showAlertMessage("Already scanned.", 0);
        }
        if(stickerScan != null){
            HangerArchiveModel hangerArchiveModel = new HangerArchiveModel();
            hangerArchiveModel.setArchiveId(stickerId);
            hangerArchiveModel.setGarmentItem(stickerText);
            hangerArchiveModel.setGarmentItemBarcode(stickerScan);
            hangerArchiveModel.setDateTime(currentDateTime("dd-MMMM-yyyy hh:mm:ss a"));

            hangerArchiveModels.add(hangerArchiveModel);
            _hangerArchiveRecyclerAdapter.notifyDataSetChanged();
        }

    }

    private boolean isMatch(){
        for(int i=0; i<hangerArchiveModels.size(); i++){
            if(hangerArchiveModels.get(i).getArchiveId().equals(stickerId) && hangerArchiveModels.get(i).getGarmentItem().equals(stickerText) ){
                return true;
            }
        }
        return false;
    }

    private void startScanning() {
        Intent intent = new Intent(this, V1_QRBarcodeScannerActivity.class);
        intent.putExtra("qc", "hangerArchive");
        intent.putExtra("bayer_name", buyerNameTV.getText().toString().trim());
        intent.putExtra("minutes", minutesTV.getText().toString().trim());
        intent.putExtra("stickerDataList", hangerArchiveModels);
        startActivity(intent);
        finish();
    }

    private String currentDateTime(String pattern){
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat(pattern);
        String currentDate = simpleDateFormat.format(calendar.getTime());
        return currentDate;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveBT:
                try {
                    saveDataToServer();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.refreshBT:
                hangerArchiveModels.clear();
                _hangerArchiveRecyclerAdapter.notifyDataSetChanged();
                break;
            case R.id.stickerScanBT:
                startScanning();
                break;
            case R.id.currentDateBtn:
                datePickerfun(currentDateBtn);
                break;
            case R.id.currentTimeBtn:
                timePickerfun(currentTimeBtn);
                break;
        }
    }

    private void datePickerfun(Button currentDateBtn) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year1, month1, dayOfMonth);
            dateFormat = new SimpleDateFormat("dd-MMMM-yyyy", Locale.US);
            String formattedDate = dateFormat.format(selectedDate.getTime());
            currentDateBtn.setText(formattedDate);
        }, year, month, day);
        datePickerDialog.show();
    }


    private void timePickerfun(Button currentTimeBtn) {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            Calendar selectedTime = Calendar.getInstance();
            selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            selectedTime.set(Calendar.MINUTE, minute1);

            dateFormat = new SimpleDateFormat("hh:mm a", Locale.US);
            String formattedTime = dateFormat.format(selectedTime.getTime());
            currentTimeBtn.setText(formattedTime);
        }, hour, minute, false);

        timePickerDialog.show();
    }

    private void saveDataToServer() throws JSONException {
        JSONObject jsonObject = buildJsonObject();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
        pDialog.show();
        apiInterface.saveHangerArchiveCall(body).enqueue(new Callback<V1_DataSaveResponse>() {
            @Override
            public void onResponse(Call<V1_DataSaveResponse> call, Response<V1_DataSaveResponse> response) {
                pDialog.dismiss();
                Log.d(TAG, "onResponse: "+response.toString());
                if(response.isSuccessful()){
                    showAlertMessage(response.body().getResultset(), 1);
                }
            }

            @Override
            public void onFailure(Call<V1_DataSaveResponse> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private JSONObject buildJsonObject() throws JSONException {
        JSONObject save_obj = new JSONObject();
        JSONArray dtls_arr = new JSONArray();

        save_obj.put("BUYER_NAME",buyerNameTV.getText().toString().trim());
        save_obj.put("MEETING_MINUTES", minutesTV.getText().toString().trim());
        save_obj.put("INSERTED_BY", 2);
        for(int i = 0; i < hangerArchiveModels.size(); i++) {
            JSONObject dtls_obj = new JSONObject();
            dtls_obj.put("ID", hangerArchiveModels.get(i).getArchiveId());
            dtls_arr.put(dtls_obj);
        }
        save_obj.put("ARCHIVE_DETAILS", dtls_arr);
        Log.d("TAG", "buildJsonObject: ######"+save_obj);
        return save_obj;
    }

    private void showAlertMessage(String msg, int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", (dialog, which) -> {
                    if(i == 0){
                        hangerArchiveModels.remove(hangerArchiveModels.size()-1);
                        _hangerArchiveRecyclerAdapter.notifyDataSetChanged();
                    }else{
                        hangerArchiveModels.clear();
                        _hangerArchiveRecyclerAdapter.notifyDataSetChanged();
                    }

                    dialog.dismiss();
                });
        AlertDialog dialog  = builder.create();
        dialog.show();
    }

    @Override
    public void onHeadClick(int position, View v) {
        new HttpGetRequestTask().execute(modified_Base_Url+"/order/woven_gmts/requires/fabric_hanger_archive_entry_controller.php");
    }
    @Override
    public void onDeleteClick(int position, View v) {
        removeArchiveDialog(position);
    }

    private void removeArchiveDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(V1_MeetingArchiveActivity.this);
        builder.setTitle("Message")
                .setMessage("Are you sure to remove this item?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, which) -> {
                    hangerArchiveModels.remove(position);
                    _hangerArchiveRecyclerAdapter.notifyDataSetChanged();
                });
        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog dialog  = builder.create();
        dialog.show();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public class HttpGetRequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                Log.d(TAG, "doInBackground: "+hangerArchiveModels.get(itemPosition).getGarmentItem());

                String parameters = "action=fabric_print_button&data="+hangerArchiveModels.get(itemPosition).getGarmentItemBarcode()+"&api_key=logic_api_key_2609202332029062";

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(parameters.getBytes());
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    Log.d(TAG, "doInBackground: "+response.toString());
                    return response.toString();
                } else {
                    return "Error: " + responseCode;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if(result != null && !result.isEmpty()){
                Intent intent = new Intent(getApplicationContext(), PdfWebViewActivity.class);
                intent.putExtra("pdf", result.toString());
                startActivity(intent);
            }else {
                Toast.makeText(V1_MeetingArchiveActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
            
        }
    }
}