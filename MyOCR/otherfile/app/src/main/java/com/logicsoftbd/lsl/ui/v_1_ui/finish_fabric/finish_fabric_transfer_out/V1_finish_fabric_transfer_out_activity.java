package com.logicsoftbd.lsl.ui.v_1_ui.finish_fabric.finish_fabric_transfer_out;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.logicsoftbd.lsl.databinding.ActivityV1FinishFabricTransferOutBinding;

public class V1_finish_fabric_transfer_out_activity extends AppCompatActivity {
    ActivityV1FinishFabricTransferOutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityV1FinishFabricTransferOutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}