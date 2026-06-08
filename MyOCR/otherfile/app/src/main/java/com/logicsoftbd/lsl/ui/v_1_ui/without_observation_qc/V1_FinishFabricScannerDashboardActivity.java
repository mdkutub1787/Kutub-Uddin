package com.logicsoftbd.lsl.ui.v_1_ui.without_observation_qc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.ui.v_1_ui.qr_code.V1_ScannerActivity;

public class V1_FinishFabricScannerDashboardActivity extends AppCompatActivity {
    private static final String TAG = "V1_FinishFabricScannerD";
    private Button _barcodeScan, _batchScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_finish_fabric_scanner_dashboard);

        _barcodeScan = findViewById(R.id.barcodeScanBTN);
        _batchScan = findViewById(R.id.batchScanBTN);

        _barcodeScan.setOnClickListener(v -> {
            Intent intent = new Intent(V1_FinishFabricScannerDashboardActivity.this, V1_ScannerActivity.class);
            intent.putExtra("qc", "finish_fabric_qc_new_page");
            intent.putExtra("scan_op", 1);
            startActivity(intent);
            finish();
        });

        _batchScan.setOnClickListener(v -> {
            Intent intent = new Intent(V1_FinishFabricScannerDashboardActivity.this, V1_ScannerActivity.class);
            intent.putExtra("qc", "finish_fabric_qc_new_page");
            intent.putExtra("scan_op", 2);
            startActivity(intent);
            finish();
        });
    }
}