package com.logicsoftbd.lsl.ui.v_1_ui.qr_code;

import static android.Manifest.permission.CAMERA;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.logicsoftbd.lsl.ui.v_1_ui.menu.V1_MenuActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.report.V1_BundleTrackingReportActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.report.V1_ChallanReportActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.report.V1_QcReportActivity;

import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class V1_ReportQRBarcodeScannerActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler{

    private static final int REQUEST_CAMERA = 1;
    private ZBarScannerView scannerView;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;

    private String barcodeNumber;
    private int userId = 0, status = 0;
    private String urladdress;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        scannerView = new ZBarScannerView(this);
        setContentView(scannerView);
        int currentApiVersion = Build.VERSION.SDK_INT;

        if(currentApiVersion >=  Build.VERSION_CODES.M)
        {
            if(checkPermission())
            {
                Toast.makeText(getApplicationContext(), "Permission already granted!", Toast.LENGTH_LONG).show();
            }
            else
            {
                requestPermission();
            }
        }

        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", 0);
        urladdress = intent.getStringExtra("url");
        status = intent.getIntExtra("status", 0);
    }

    private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if(scannerView == null) {
                    scannerView = new ZBarScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(V1_ReportQRBarcodeScannerActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(me.dm7.barcodescanner.zbar.Result result) {
        final String myResult = result.getContents();
        barcodeNumber = myResult;

        if(status == 1){
            Intent intent = new Intent(V1_ReportQRBarcodeScannerActivity.this, V1_QcReportActivity.class);
            intent.putExtra("result", myResult);
            intent.putExtra("userId", userId);
            intent.putExtra("url", urladdress);
            intent.putExtra("status", status);
            startActivity(intent);
        }else if(status == 2){
            Intent intent = new Intent(V1_ReportQRBarcodeScannerActivity.this, V1_BundleTrackingReportActivity.class);
            intent.putExtra("result", myResult);
            intent.putExtra("userId", userId);
            intent.putExtra("url", urladdress);
            intent.putExtra("status", status);
            startActivity(intent);
        }else if(status == 3){
            Intent intent = new Intent(V1_ReportQRBarcodeScannerActivity.this, V1_ChallanReportActivity.class);
            intent.putExtra("result", myResult);
            intent.putExtra("userId", userId);
            intent.putExtra("url", urladdress);
            intent.putExtra("status", status);
            startActivity(intent);
        }

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(V1_ReportQRBarcodeScannerActivity.this, V1_MenuActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("url", urladdress);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        finish();
//    }
}

