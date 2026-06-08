package com.logicsoftbd.lsl.ui.v_1_ui.finish_fabric.finish_fabric_transfer_in;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.logicsoftbd.lsl.databinding.ActivityV1FinishFabricTransferInBinding;

public class V1_finish_fabric_transfer_in_Activity extends AppCompatActivity {
    ActivityV1FinishFabricTransferInBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityV1FinishFabricTransferInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }


}