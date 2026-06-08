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

import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyRollReceiveItemModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_OperationItemModel;
import com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing.V1_SewingInputActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing.V1_SewingOutputActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing.V1_SewingOutputActivity_v1;
import com.logicsoftbd.lsl.ui.v_1_ui.buyer_meeting.HangerArchiveModel;
import com.logicsoftbd.lsl.ui.v_1_ui.buyer_meeting.V1_MeetingArchiveActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.grey_fabric_roll_receive.V1_GreyRollReceiveActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.home.V1_HomeActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.line_wise_sewing.V1_LineWiseSewingInputActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.line_wise_sewing.V1_LineWiseSewingOutputActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.linking.V1_LinkingInputActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.linking.V1_LinkingOutputActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.menu.V1_MenuActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.production_operation.V1_ProductionOperationActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.report.V1_ChallanReportActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.with_observation_qc.V1_FinishFabricObsActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.with_observation_qc.V1_GreyObsActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.without_observation_qc.V1_FinishFabricActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.without_observation_qc.V1_GreyFabricActivity;

import java.util.ArrayList;

import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class V1_QRBarcodeScannerActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler{

    private static final int REQUEST_CAMERA = 1;
    private ZBarScannerView scannerView;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;

    private String barcodeNumber;
    private int userId = 0, op = 0;
    private String urladdress;
    private String bundleScan, operationScan, empIdScan, grey_room_rack_scan, grey_barcode_scan, grey_challan_scan, buyerName, minutes;

    private ArrayList<V1_OperationItemModel> operationDataList = new ArrayList<>();
    private ArrayList<HangerArchiveModel>  stickerDataList = new ArrayList<>();
    private ArrayList<V1_GreyRollReceiveItemModel> grey_roll_dataList = new ArrayList<>();

    //userPreviledge
    private int savemenu = 0;
    private int updatemenu = 0;

    //QC Entry Scan
    private String type_entry;
    //Finish fabric
    private int finish_fabric_entry = 0, type = 0, grey_scan_op = 0;


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
//                Toast.makeText(getApplicationContext(), "Permission already granted!", Toast.LENGTH_LONG).show();
            }
            else
            {
                requestPermission();
            }
        }
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", 0);
        urladdress = intent.getStringExtra("url");
        savemenu = intent.getIntExtra("s", 0);
        updatemenu = intent.getIntExtra("u", 0);
        type_entry = intent.getStringExtra("qc");
        type = intent.getIntExtra("type", 0);
        bundleScan = intent.getStringExtra("bundleScan");
        operationScan = intent.getStringExtra("operationScan");
        empIdScan = intent.getStringExtra("empIdScan");
        op = intent.getIntExtra("scan_op", 0);
        grey_scan_op = intent.getIntExtra("grey_scan_op", 0);
        grey_room_rack_scan = intent.getStringExtra("room_rack_scan");
        grey_barcode_scan = intent.getStringExtra("barcode_scan");
        grey_challan_scan = intent.getStringExtra("challan_scan");
        buyerName = intent.getStringExtra("bayer_name");
        minutes = intent.getStringExtra("minutes");
        operationDataList = (ArrayList<V1_OperationItemModel>) intent.getSerializableExtra("data");
        stickerDataList = (ArrayList<HangerArchiveModel>) intent.getSerializableExtra("stickerDataList");
        grey_roll_dataList = (ArrayList<V1_GreyRollReceiveItemModel>) intent.getSerializableExtra("grey_roll_data");

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
        new AlertDialog.Builder(V1_QRBarcodeScannerActivity.this)
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
        if(type_entry.equals("Grey_Fabric_QC_Entry_V1"))
        {
            Intent intent = new Intent(V1_QRBarcodeScannerActivity.this, V1_GreyFabricActivity.class);
            intentDataToActivity(intent, myResult);
            startActivity(intent);
        } else if(type_entry.equals("Grey_Fabric_With_Observation")){
            Intent intent = new Intent(V1_QRBarcodeScannerActivity.this, V1_GreyObsActivity.class);
            intentDataToActivity(intent, myResult);
            startActivity(intent);
        }
        else if(type_entry.equals("Finish_Fabric_QC_Entry"))
        {
            Intent intent = new Intent(V1_QRBarcodeScannerActivity.this, V1_FinishFabricActivity.class);
            intentDataToActivity(intent, myResult);
            startActivity(intent);
        } else if(type_entry.equals("Finish_Fabric_With_Observation")){
            Intent intent = new Intent(V1_QRBarcodeScannerActivity.this, V1_FinishFabricObsActivity.class);
            intentDataToActivity(intent, myResult);
            startActivity(intent);
        }
        else if(type_entry.equals("bundle_wise_sewing_input"))
        {
            Intent intent = new Intent(V1_QRBarcodeScannerActivity.this, V1_SewingInputActivity.class);
            intentDataToActivity(intent, myResult);
            startActivity(intent);
        }
        else if(type_entry.equals("bundle_wise_sewing_output"))
        {
            Intent intent = new Intent(V1_QRBarcodeScannerActivity.this, V1_SewingOutputActivity.class);
            intentDataToActivity(intent, myResult);
            startActivity(intent);
        }else if(type_entry.equals("bundle_wise_sewing_output_v3"))
        {
            Intent intent = new Intent(V1_QRBarcodeScannerActivity.this, V1_SewingOutputActivity_v1.class);
            intentDataToActivity(intent, myResult);
            startActivity(intent);
        }
        else if(type_entry.equals("line_wise_sewing_input"))
        {
            Intent intent = new Intent(V1_QRBarcodeScannerActivity.this, V1_LineWiseSewingInputActivity.class);
            intentDataToActivity(intent, myResult);
            startActivity(intent);
        }
        else if(type_entry.equals("line_wise_sewing_output"))
        {
            Intent intent = new Intent(V1_QRBarcodeScannerActivity.this, V1_LineWiseSewingOutputActivity.class);
            intentDataToActivity(intent, myResult);
            startActivity(intent);
        }
        else if(type_entry.equals("linking_input"))
        {
            Intent intent = new Intent(V1_QRBarcodeScannerActivity.this, V1_LinkingInputActivity.class);
            intentDataToActivity(intent, myResult);
            startActivity(intent);
        }
        else if(type_entry.equals("linking_output"))
        {
            Intent intent = new Intent(V1_QRBarcodeScannerActivity.this, V1_LinkingOutputActivity.class);
            intentDataToActivity(intent, myResult);
            startActivity(intent);
        }
        else if(type_entry.equals("challan_wise_sewing_input"))
        {
            Intent intent = new Intent(V1_QRBarcodeScannerActivity.this, V1_ChallanReportActivity.class);
            intentDataToActivity(intent, myResult);
            startActivity(intent);
        }
        else if(type_entry.equals("line_wise_sewing_input_challan_wise"))
        {
            Intent intent = new Intent(V1_QRBarcodeScannerActivity.this, V1_ChallanReportActivity.class);
            intentDataToActivity(intent, myResult);
            intent.putExtra("type", type);
            startActivity(intent);
        }else if(type_entry.equals("grey_roll_receive_v1"))
        {
            Intent intent = new Intent(V1_QRBarcodeScannerActivity.this, V1_GreyRollReceiveActivity.class);
            intent.putExtra("grey_roll_barcodeScan", myResult);
            intent.putExtra("grey_scan_op", grey_scan_op);
            intent.putExtra("grey_roll_data", grey_roll_dataList);
            intent.putExtra("room_rack_scan", grey_room_rack_scan);
            intent.putExtra("barcode_scan", grey_barcode_scan);
            intent.putExtra("challan_scan", grey_challan_scan);
            startActivity(intent);
        }
        else if(type_entry.equals("operations"))
        {
            Intent intent = new Intent(V1_QRBarcodeScannerActivity.this, V1_ProductionOperationActivity.class);
            intentDataToActivity(intent, myResult);
            intent.putExtra("type", type);
            if(op == 0){
                intent.putExtra("bundleScan", myResult);
                intent.putExtra("operationScan", operationScan);
                intent.putExtra("empIdScan", empIdScan);
                intent.putExtra("data", operationDataList);
            }
            if(op == 1){
                intent.putExtra("bundleScan", bundleScan);
                intent.putExtra("operationScan", myResult);
                intent.putExtra("empIdScan", empIdScan);
                intent.putExtra("data", operationDataList);
            }
            if(op == 2){
                intent.putExtra("bundleScan", bundleScan);
                intent.putExtra("operationScan", operationScan);
                intent.putExtra("empIdScan", myResult);
                intent.putExtra("data", operationDataList);
            }
            if(op == 3){
                intent.putExtra("bundleScan", bundleScan);
                intent.putExtra("runningOperation", myResult);
                intent.putExtra("empIdScan", empIdScan);
                intent.putExtra("op", 3);
                intent.putExtra("data", operationDataList);
            }
            startActivity(intent);
        }
        else if(type_entry.equals("hangerArchive")){
            Intent intent = new Intent(V1_QRBarcodeScannerActivity.this, V1_MeetingArchiveActivity.class);
            intent.putExtra("sticker", myResult);
            intent.putExtra("stickerDataList", stickerDataList);
            intent.putExtra("bayer_name", buyerName);
            intent.putExtra("minutes", minutes);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(V1_QRBarcodeScannerActivity.this, V1_MenuActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("url", urladdress);
            intent.putExtra("s", savemenu);
            intent.putExtra("u", updatemenu);
            intent.putExtra("qc", type_entry);
            startActivity(intent);
        }
    }

    private void intentDataToActivity(Intent intent, String myResult) {
        intent.putExtra("result", myResult);
        intent.putExtra("userId", userId);
        intent.putExtra("url", urladdress);
        intent.putExtra("s", savemenu);
        intent.putExtra("u", updatemenu);
        intent.putExtra("qc", type_entry);
    }

    @Override
    public void onBackPressed() {
        if(type_entry.equals("operations")){
            Intent intent = new Intent(V1_QRBarcodeScannerActivity.this, V1_ProductionOperationActivity.class);
            intent.putExtra("type", type);
            if(op == 0){
                intent.putExtra("operationScan", operationScan);
                intent.putExtra("empIdScan", empIdScan);
                intent.putExtra("data", operationDataList);
            }
            if(op == 1){
                intent.putExtra("bundleScan", bundleScan);
                intent.putExtra("empIdScan", empIdScan);
                intent.putExtra("data", operationDataList);
            }
            if(op == 2){
                intent.putExtra("bundleScan", bundleScan);
                intent.putExtra("operationScan", operationScan);
                intent.putExtra("data", operationDataList);
            }
            if(op == 3){
//                intent.putExtra("bundleScan", bundleScan);
                intent.putExtra("operationScan", operationScan);
                intent.putExtra("data", operationDataList);
            }
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(V1_QRBarcodeScannerActivity.this, V1_HomeActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("url", urladdress);
            intent.putExtra("s", savemenu);
            intent.putExtra("u", updatemenu);
            intent.putExtra("qc", type_entry);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}

