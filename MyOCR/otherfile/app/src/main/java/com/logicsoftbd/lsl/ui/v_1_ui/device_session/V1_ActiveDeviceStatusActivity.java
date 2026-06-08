package com.logicsoftbd.lsl.ui.v_1_ui.device_session;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ActiveDeviceModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DataSaveResponse;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.utils.ApiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class V1_ActiveDeviceStatusActivity extends AppCompatActivity implements V1_ActiveDeviceStatusRecyclerViewAdapter.OnHeadListener{
    private static final String TAG = "ActiveDeviceStatusActiv";

    private ApiInterface apiInterface;
    private ApiUtils apiUtils;
    private ProgressDialog pDialog;
    private RecyclerView _activeDeviceStatusRecyclerView;
    private List<V1_ActiveDeviceModel.Resultset> activeDeviceList = new ArrayList<>();
    private V1_ActiveDeviceStatusRecyclerViewAdapter activeDeviceStatusRecyclerViewAdapter;
    private String rowId = "", base_url = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_device_status);

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        base_url = (_preferences.getString("base_url", ""));
        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);

        init_ui();
    }

    private void init_ui() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        _activeDeviceStatusRecyclerView = findViewById(R.id.activeDeviceStatusRecyclerView);

        requestActiveDevice();
    }

    private void requestActiveDevice() {
        showDialog();
        apiInterface.getActiveDeviceModelCall().enqueue(new Callback<V1_ActiveDeviceModel>() {
            @Override
            public void onResponse(Call<V1_ActiveDeviceModel> call, Response<V1_ActiveDeviceModel> response) {
                hideDialog();
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: "+response.toString());
                    if(response.body().getResultset() != null){
                        activeDeviceList = response.body().getResultset();
                        setAdapter();
                    }
                }
            }

            @Override
            public void onFailure(Call<V1_ActiveDeviceModel> call, Throwable t) {
                hideDialog();
                Log.d(TAG, "onFailure: Failed");
            }
        });
    }

    private void setAdapter() {
        activeDeviceStatusRecyclerViewAdapter = new V1_ActiveDeviceStatusRecyclerViewAdapter(this, activeDeviceList,  this);
        _activeDeviceStatusRecyclerView.setAdapter(activeDeviceStatusRecyclerViewAdapter);
        _activeDeviceStatusRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        activeDeviceStatusRecyclerViewAdapter.notifyDataSetChanged();
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
    public void onHeadClick(int position, View v) {
        rowId = activeDeviceList.get(position).getRowid();
        alertForApkUpdate(activeDeviceList.get(position).getDeviceId());
        activeDeviceStatusRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void alertForApkUpdate(String message){
        ImageView cancel;
        Button updateBtn;
        TextView messageTV;

        View alertCustomDialog = LayoutInflater.from(this).inflate(R.layout.custom_remove_alert_layout,null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setView(alertCustomDialog);
        cancel = alertCustomDialog.findViewById(R.id.cancel_button);
        updateBtn = alertCustomDialog.findViewById(R.id.btnUpdate);
        messageTV = alertCustomDialog.findViewById(R.id.messageTV);

        messageTV.setText("Are you want to deactivate this device?");
        final AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
        dialog.setCancelable(false);

        updateBtn.setOnClickListener(v -> {
            JSONObject jsonObject = null;
            try {
                jsonObject = buidJsonObject();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
            showDialog();
            apiInterface.inActiveTabConfigCall(body).enqueue(new Callback<V1_DataSaveResponse>() {
                @Override
                public void onResponse(Call<V1_DataSaveResponse> call, Response<V1_DataSaveResponse> response) {
                    hideDialog();
                    if(response.isSuccessful()){
                        Toasty.success(V1_ActiveDeviceStatusActivity.this, "Successfully Removed");
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<V1_DataSaveResponse> call, Throwable t) {
                    hideDialog();
                    Toasty.error(V1_ActiveDeviceStatusActivity.this, "Can't Remove");
                    dialog.dismiss();
                }
            });
        });

        cancel.setOnClickListener( v -> dialog.dismiss());
    }

    private JSONObject buidJsonObject() throws JSONException {

        JSONObject save_obj = new JSONObject();
        save_obj.put("status", true);
        save_obj.put("row_id", rowId);
        return save_obj;
    }
}
