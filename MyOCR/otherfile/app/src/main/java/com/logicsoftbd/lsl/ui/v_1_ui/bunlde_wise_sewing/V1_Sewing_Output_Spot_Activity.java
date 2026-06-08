package com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingSpotModel;

import java.util.ArrayList;

public class V1_Sewing_Output_Spot_Activity extends AppCompatActivity {
    private ArrayList<V1_SewingSpotModel> dataList;
    private RecyclerView spotPopUpRecyclerView;
    private V1_SpotPopUpRecyclerAdapter spotPopUpRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_sewing_output_spot);

//        dataList = V1_SewingOutputActivity.sewingSpotModels;
        System.out.println("#######"+dataList.toString());
        initialization();
    }

    private void initialization() {
        spotPopUpRecyclerView = findViewById(R.id.spotRecyclerView);
        initAlterRecyclerView();

        findViewById(R.id.spotCloseBtn).setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void initAlterRecyclerView() {
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        spotPopUpRecyclerView.setLayoutManager(linearLayoutManager);
//        spotPopUpRecyclerAdapter = new V1_SpotPopUpRecyclerAdapter(dataList);
//        spotPopUpRecyclerView.setAdapter(spotPopUpRecyclerAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}