package com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_AlterSewingOutputOperationItemModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_RejectSewingOutputOperationItemModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SpotSewingOutputOperationItemModel;
import com.logicsoftbd.lsl.serviceInterface.ApiInterface;
import com.logicsoftbd.lsl.utils.ApiUtils;
import com.logicsoftbd.lsl.utils.VerticalSpacingItemDecorator;

import java.util.ArrayList;

public class V1_SewingOutputOperationActivity extends AppCompatActivity implements View.OnClickListener, V1_SewingInputPendingAdapter.V1_SewingOutputOperationRecyclerAdapter.OnRejectDefectSelectListener, V1_AlterSewingOperationRecyclerAdapter.OnAlterDefectSelectListener, V1_SpotSewingOperationRecyclerAdapter.OnSpotDefectSelectListener {
    private static final String TAG = "V1_SewingOutputOperation";
    private ProgressDialog _pdialog;
    private RecyclerView operationRecyclerView;
    private ArrayList<V1_RejectSewingOutputOperationItemModel> rejectSewingOutputOperationItemModels = new ArrayList<>();
    private ArrayList<V1_AlterSewingOutputOperationItemModel> alterSewingOutputOperationItemModels = new ArrayList<>();
    private ArrayList<V1_SpotSewingOutputOperationItemModel> spotSewingOutputOperationItemModels = new ArrayList<>();
    private V1_SewingInputPendingAdapter.V1_SewingOutputOperationRecyclerAdapter rejectSewingOperationRecyclerAdapter;
    private V1_AlterSewingOperationRecyclerAdapter alterSewingOperationRecyclerAdapter;
    private V1_SpotSewingOperationRecyclerAdapter spotSewingOperationRecyclerAdapter;
    private ApiInterface apiInterface;
    private ApiUtils apiUtils;
    private Button backButton, nextButton;
    private ImageView imgBack;
    private String base_url, defectKey, defectDataKey, defectType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_sewing_output_operation);

        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        base_url = (_preferences.getString("base_url", ""));
        apiUtils = new ApiUtils(this);
        apiInterface = ApiUtils.getInterface(base_url);

        Intent intent = getIntent();
        defectKey = intent.getStringExtra("defectKey");
        defectDataKey = intent.getStringExtra("defectDataKey");
        defectType = intent.getStringExtra("defect_type");

        if(defectType.equals("R")){
            rejectSewingOutputOperationItemModels = V1_SewingOutputActivity.rejectSewingOutputOperationItemModels;
        }
        if(defectType.equals("A")){
            alterSewingOutputOperationItemModels = V1_SewingOutputActivity.alterSewingOutputOperationItemModels;
        }
        if(defectType.equals("S")){
            spotSewingOutputOperationItemModels = V1_SewingOutputActivity.spotSewingOutputOperationItemModels;
        }

        init_ui();
        initRecyclerView();
    }

    private void init_ui() {
        _pdialog = new ProgressDialog(this);
        _pdialog.setMessage("Loading...");
        _pdialog.setCancelable(false);
        operationRecyclerView = findViewById(R.id.operationRecyclerView);
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        operationRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        operationRecyclerView.addItemDecoration(itemDecorator);
        if(defectType.equals("R")){
            rejectSewingOperationRecyclerAdapter = new V1_SewingInputPendingAdapter.V1_SewingOutputOperationRecyclerAdapter(rejectSewingOutputOperationItemModels, this,  this);
            operationRecyclerView.setAdapter(rejectSewingOperationRecyclerAdapter);
        }
        if(defectType.equals("A")){
            alterSewingOperationRecyclerAdapter = new V1_AlterSewingOperationRecyclerAdapter(alterSewingOutputOperationItemModels, this,  this);
            operationRecyclerView.setAdapter(alterSewingOperationRecyclerAdapter);
        }
        if(defectType.equals("S")){
            spotSewingOperationRecyclerAdapter = new V1_SpotSewingOperationRecyclerAdapter(spotSewingOutputOperationItemModels, this,  this);
            operationRecyclerView.setAdapter(spotSewingOperationRecyclerAdapter);
        }

        operationRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private boolean checkDefectIsSelected() {
        boolean defectStatus = false;
        if(defectType.equals("R")){
            for(int i=0; i<rejectSewingOutputOperationItemModels.size(); i++){
                if(rejectSewingOutputOperationItemModels.get(i).getStatus() == 1){
                    defectStatus = true;
                    break;
                }
            }
        }
        if(defectType.equals("A")){
            for(int i=0; i<alterSewingOutputOperationItemModels.size(); i++){
                if(alterSewingOutputOperationItemModels.get(i).getStatus() == 1){
                    defectStatus = true;
                    break;
                }
            }
        }
        if(defectType.equals("S")){
            for(int i=0; i<spotSewingOutputOperationItemModels.size(); i++){
                if(spotSewingOutputOperationItemModels.get(i).getStatus() == 1){
                    defectStatus = true;
                    break;
                }
            }
        }

        return defectStatus;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgBack:
            case R.id.backButton:
                onBackPressed();
                break;
            case R.id.nextButton:
                if(checkDefectIsSelected()){
//                    Intent intent = new Intent(v.getContext(), EditImageActivity.class);
//                    intent.putExtra("defectKey", defectKey);
//                    intent.putExtra("defectDataKey", defectDataKey);
//                    intent.putExtra("defect_type", defectType);
//                    v.getContext().startActivity(intent);
                }else{
                    showAlertMessage("Please select \"DEFECTIVE-Operation\".");
                }
                break;
        }
    }

    private void showAlertMessage(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(V1_SewingOutputOperationActivity.this);
        builder.setTitle("Message")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                });
        AlertDialog dialog  = builder.create();
        dialog.show();
    }

    @Override
    public void onRejectDefectHeadClick(int position, View v) {
        if(rejectSewingOutputOperationItemModels.get(position).getStatus() == 0){
            rejectSewingOutputOperationItemModels.get(position).setStatus(1);
        }else{
            rejectSewingOutputOperationItemModels.get(position).setStatus(0);
        }
    }

    @Override
    public void onAlterDefectHeadClick(int position, View v) {
        if(alterSewingOutputOperationItemModels.get(position).getStatus() == 0){
            alterSewingOutputOperationItemModels.get(position).setStatus(1);
        }else{
            alterSewingOutputOperationItemModels.get(position).setStatus(0);
        }
    }

    @Override
    public void onSpotDefectHeadClick(int position, View v) {
        if(spotSewingOutputOperationItemModels.get(position).getStatus() == 0){
            spotSewingOutputOperationItemModels.get(position).setStatus(1);
        }else{
            spotSewingOutputOperationItemModels.get(position).setStatus(0);
        }
    }
}