package com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_SewingAlterModel;

import java.util.ArrayList;

public class V1_Sewing_Output_Alter_Activity extends AppCompatActivity {
    private ArrayList<V1_SewingAlterModel> dataList;
    private RecyclerView alterPopUpRecyclerView;
    private V1_AlterPopUpRecyclerAdapter alterPopUpRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_sewing_output_alter);

//        dataList = V1_SewingOutputActivity.sewingAlterModels;
        System.out.println("#######"+dataList.toString());
        initialization();
    }

    private void initialization() {
        alterPopUpRecyclerView = findViewById(R.id.alterRecyclerView);

        findViewById(R.id.alterCloseBtn).setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}