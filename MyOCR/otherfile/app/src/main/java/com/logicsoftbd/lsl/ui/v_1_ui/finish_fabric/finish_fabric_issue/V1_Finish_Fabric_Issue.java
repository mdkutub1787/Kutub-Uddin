package com.logicsoftbd.lsl.ui.v_1_ui.finish_fabric.finish_fabric_issue;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.databinding.ActivityV1FinishFabricIssueBinding;

public class V1_Finish_Fabric_Issue extends AppCompatActivity {
    ActivityV1FinishFabricIssueBinding binding;
    private static final String TAG = "V1_Finish_Fabric_Issue";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityV1FinishFabricIssueBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        

        
    }
}