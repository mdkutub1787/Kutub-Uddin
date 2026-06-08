package com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingRejectModel;

import java.util.ArrayList;

public class V1_Sewing_Output_Reject_Activity extends AppCompatActivity {
    private ArrayList<V1_SewingRejectModel> dataList;
    private RecyclerView rejectPopUpRecyclerView;
    private V1_RejectPopUpRecyclerAdapter rejectPopUpRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_sewing_output_reject);

//        dataList = V1_SewingOutputActivity.sewingRejectModels;
        System.out.println("#######"+dataList.toString());
        initialization();
    }

    private void initialization() {
        rejectPopUpRecyclerView = findViewById(R.id.rejectRecyclerView);
        initAlterRecyclerView();

        findViewById(R.id.rejectCloseBtn).setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void initAlterRecyclerView() {
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        rejectPopUpRecyclerView.setLayoutManager(linearLayoutManager);
//        rejectPopUpRecyclerAdapter = new V1_RejectPopUpRecyclerAdapter(dataList);
//        rejectPopUpRecyclerView.setAdapter(rejectPopUpRecyclerAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}