package com.logicsoftbd.lsl.ui.v_1_ui.qr_code;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.SparseArray;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.vision.barcode.Barcode;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.model.CompactBatchScanResponse;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRollReceiveRequest;
import com.logicsoftbd.lsl.data.network.model.SlitteringSequzBarCodeResponse;
import com.logicsoftbd.lsl.data.network.model.StenteringBatchScanResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_AOPBagKeepingResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_AOPDeptBagReceiveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagDeliveryResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagEmptyReceiveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagIssueResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagKeepingDataBySystemResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagKeepingResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagReceiveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagReturnResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BundeWiseSewingInputPCSResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_CuttingStoreIssueModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_CuttingStoreReceiveModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_DyedAOPBagReceiveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GMTFinishReceiveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyRollReceiveItemModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyStoreRejectBagReceiveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_IssueReturnRFIDModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_OperationItemModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_YarnIssueReturnResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_YarnRFIDModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_transfer_out.barcode.DtlsPart;
import com.logicsoftbd.lsl.ui.base.BaseActivity;
import com.logicsoftbd.lsl.ui.compacting.CompactingActivity;
import com.logicsoftbd.lsl.ui.compacting.CompactingRollWiseActivity;
import com.logicsoftbd.lsl.ui.process.scanprocess.BarcodeFragment;
import com.logicsoftbd.lsl.ui.slitting_squeezing.SlittingSqeezingRollWiseActivity;
import com.logicsoftbd.lsl.ui.slitting_squeezing.SlittingSqueezingActivity;
import com.logicsoftbd.lsl.ui.stentering.StenteringActivity;
import com.logicsoftbd.lsl.ui.stentering.StenteringRollWiseActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.bundle_wise_sewing_pcs.V1_BundleWiseSewingInput_PCSActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing.V1_SewingInputActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing.V1_SewingOutputActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.cutting.V1_CuttingStoreIssueActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.cutting.V1_CuttingStoreReceiveActivity;
import com.logicsoftbd.lsl.data.network.v1_model.V1_finish_fabric_receive.FFRBarcode;
import com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag.V1_AOPBagKeepingActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag.V1_AOPDeptBagReceiveActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag.V1_BagDeliveryActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag.V1_BagIssueActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag.V1_BagKeepingActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag.V1_BagKeepingPrintActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag.V1_BagKeepingQcActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag.V1_BagReceiveActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag.V1_BagReturnActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag.V1_DyedAOPDeptBagReceiveActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag.V1_EmptyBagReceiveActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag.V1_GreyStoreRejectFabricBagActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.finish_fabric.finish_fabric_receive.V1_Finish_Fabric_Receive;
import com.logicsoftbd.lsl.ui.v_1_ui.finish_fabric.finish_fabric_receive_old.V1_FinishFabricReceiveActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.finish_fabric_roll_receive.V1_GmtFinishReceiveActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.finishing.V1_FabricFinishingQCActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.grey_fabric_roll_issue.V1_GreyFabricRollIssueActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.grey_fabric_roll_receive.V1_GreyRollReceiveActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.line_wise_sewing.V1_LineWiseSewingInputActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.line_wise_sewing.V1_LineWiseSewingOutputActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.linking.V1_LinkingInputActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.linking.V1_LinkingOutputActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.menu.V1_MenuActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.production_operation.V1_ProductionOperationActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.report.V1_ChallanReportActivity;

import com.logicsoftbd.lsl.ui.v_1_ui.roll_wise_gray_fabric_delivery_store.V1_RollWiseGreyFabricDeliveryToStoreActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.transfer.transfer_in.V1_TransferInActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.transfer.transfer_out.V1_TransferoutActivity;

import com.logicsoftbd.lsl.ui.v_1_ui.with_observation_qc.V1_FinishFabricObsActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.with_observation_qc.V1_GreyObsActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.without_observation_qc.V1_FinishFabricActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.without_observation_qc.V1_GreyFabricActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.without_observation_qc.V2_FinishFabricQcEntryActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.yarn_rfid.V1_RFIDTagForYarnIssueReturnActivity;
import com.logicsoftbd.lsl.ui.v_1_ui.yarn_rfid.V1_YarnRfidReceiveActivity;
import com.logicsoftbd.lsl.utils.DialogHelper;
import com.notbytes.barcode_reader.BarcodeReaderFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class V1_ScannerActivity extends BaseActivity implements BarcodeReaderFragment.BarcodeReaderListener {
    private static final String TAG = "V1_ScannerActivity";
    private int userId = 0, op = 0, type = 0, finishCompanyId = 0, fnCompanyId = 0, fnCompanyWiseLocationId = 0, fnLocationWiseFloorId = 0, fnFloorWiseLineId = 0, selectedBagColor = 0, selected_category = 0, selectedCategoryId = 0, selectedColorId = 0;
    private String urladdress;
    private String bundleScan, operationScan, empIdScan, room_rack_scan, grey_barcode_scan, grey_challan_scan, store_receive_room_rack_scan,
            yarn_rfid_receive_roll_data, _grn_barcodeScan, _rfid_location, batch_scan, bagScan, rollWeight, issueChallanScan;

    private ArrayList<V1_OperationItemModel> dataList = new ArrayList<>();
    private ArrayList<V1_GreyRollReceiveItemModel> grey_roll_dataList = new ArrayList<>();
    private ArrayList<FinishFabricRollReceiveRequest.DetailsPart> finish_roll_receive_dataList = new ArrayList<>();
    private ArrayList<V1_CuttingStoreReceiveModel> cuttingStoreReceiveModelArrayList = new ArrayList<>();
    private ArrayList<V1_CuttingStoreIssueModel> cuttingStoreIssueModelArrayList = new ArrayList<>();
    private ArrayList<V1_BagKeepingResponse.ResultSet> bagKeepingArrayList = new ArrayList<>();
    private ArrayList<V1_AOPBagKeepingResponse.ResultSet> aopBagKeepingArrayList = new ArrayList<>();
    private ArrayList<V1_BagKeepingDataBySystemResponse.ResultSet> bagKeepingQCArrayList = new ArrayList<>();
    private ArrayList<V1_AOPDeptBagReceiveResponse.ResultSet> aopDeptBarReceiveList = new ArrayList<>();
    private ArrayList<V1_DyedAOPBagReceiveResponse.ResultSet> aopDyedDeptBarReceiveList = new ArrayList<>();
    private ArrayList<V1_GreyStoreRejectBagReceiveResponse.ResultSet> greyStoreRejectBagList = new ArrayList<>();
    private ArrayList<V1_BagDeliveryResponse.ResultSet> bagDeliveryArrayList = new ArrayList<>();
    private ArrayList<V1_BagEmptyReceiveResponse.ResultSet> bagEmptyReceiveArrayList = new ArrayList<>();
    private ArrayList<V1_BagReceiveResponse.ResultSet> bagReceiveArrayList = new ArrayList<>();
    private ArrayList<V1_BagIssueResponse.ResultSet> bagIssueArrayList = new ArrayList<>();
    private ArrayList<V1_BagReturnResponse.ResultSet> bagReturnArrayList = new ArrayList<>();
    private ArrayList<V1_GMTFinishReceiveResponse.Data> gmtFinishReceiveBarcodeList = new ArrayList<>();
    private ArrayList<V1_YarnRFIDModel> yarnRFIDModelArrayList = new ArrayList<>();
    private CompactBatchScanResponse compactBatchScanResponse = new CompactBatchScanResponse();
    private StenteringBatchScanResponse stenteringBatchScanResponse = new StenteringBatchScanResponse();
    private SlitteringSequzBarCodeResponse slittingBatchScanResponse = new SlitteringSequzBarCodeResponse();
    private ArrayList<V1_YarnIssueReturnResponse.DtlsIssueDetail> dtlsYarnIssueDetailArrayList = new ArrayList<>();
    private  V1_YarnIssueReturnResponse yarnIssueReturnResponse;
    private ArrayList<V1_IssueReturnRFIDModel> dtlsYarnRFIDIssueArrayList = new ArrayList<>();

    //userPreviledge
    private int savemenu = 0;
    private int updatemenu = 0;

    //QC Entry Scan
    private String type_entry;
    //Finish fabric
    private int finish_fabric_entry = 0, cutting_receive_op = 0, yarn_rfid_receive_scan_op = 0;
    // UI
    @BindView(R.id.et_qr_code)
    EditText editTextQrCode;

    @BindView(R.id.image_button_go)
    ImageView imageButtonGo;

    @BindView(R.id.fm_container)
    FrameLayout frameLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.progressBar)
    ProgressBar _progressBar;

    //transfer out activity
    private int transfer_out___scan_op = 0;
    private String transfer_out__location_id;
    private String transfer_out__location_name;
    private String transfer_out__company_id;
    private String transfer_out__po_breakdown_id;
    private String transfer_out__floor_id;
    private String transfer_out__room_id;
    private String transfer_out__rack_id;
    private String transfer_out__shelf_id;
    private String transfer_out__bin_id;
    private String transfer_out__item_category_id;
    private ArrayList<DtlsPart> transfer_out__listOfAllBarcode = new ArrayList<>();
    private ArrayList<String> transfer_out__storeIdList = new ArrayList<>();
    private ArrayList<String> transfer_out__storeNameList = new ArrayList<>();

    //transfer in activity
    private int transfer_in___scan_op = 0;
    private String transfer_in__location_id;
    private String transfer_in__location_name;
    private String transfer_in__company_id;
    private String transfer_in__po_breakdown_id;
    private ArrayList<DtlsPart> transfer_in__listOfAllBarcode = new ArrayList<>();

    //gtm finish fabric receive
    private int finish_fabric_receive___scan_op = 0;
    private String finish_fabric_receive___btFrRackScanData;
    private String finish_fabric_receive___btBarcodeScanData;
    private String finish_fabric_receive___btBatchCardScanData;
    private ArrayList<FFRBarcode> finish_fabric_receive__listOfAllBarcode = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_scanner);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        setUp();
    }

    @Override
    protected void setUp() {
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setTitle(R.string.scanner);

        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", 0);
        urladdress = intent.getStringExtra("url");
        savemenu = intent.getIntExtra("s", 0);
        updatemenu = intent.getIntExtra("u", 0);
        type_entry = intent.getStringExtra("qc");
        type = intent.getIntExtra("type", 0);
        finishCompanyId = intent.getIntExtra("finishCompanyId", 0);
        fnCompanyId = intent.getIntExtra("fnCompanyId", 0);
        fnCompanyWiseLocationId = intent.getIntExtra("fnLocationId", 0);
        fnLocationWiseFloorId = intent.getIntExtra("fnFloorId", 0);
        fnFloorWiseLineId = intent.getIntExtra("fnLineId", 0);
        bundleScan = intent.getStringExtra("bundleScan");
        operationScan = intent.getStringExtra("operationScan");
        empIdScan = intent.getStringExtra("empIdScan");
        op = intent.getIntExtra("scan_op", 0);
        type = intent.getIntExtra("type", 0);
        cutting_receive_op = intent.getIntExtra("store_receive_scan_op", 0);
        yarn_rfid_receive_scan_op = intent.getIntExtra("yarn_rfid_receive_scan_op", 0);
        store_receive_room_rack_scan = intent.getStringExtra("store_receive_room_rack_scan");
        room_rack_scan = intent.getStringExtra("room_rack_scan");
        grey_barcode_scan = intent.getStringExtra("barcode_scan");
        grey_challan_scan = intent.getStringExtra("challan_scan");
        _grn_barcodeScan = intent.getStringExtra("grn_barcodeScan");
        _rfid_location = intent.getStringExtra("rfid_location");
        batch_scan = intent.getStringExtra("batch_scan");
        bagScan = intent.getStringExtra("bagScan");
        rollWeight = intent.getStringExtra("rollWeight");
        issueChallanScan = intent.getStringExtra("issueChallanScan");
        selectedCategoryId = intent.getIntExtra("selected_category", 0);
        selectedColorId = intent.getIntExtra("selected_color", 0);
        selectedBagColor = intent.getIntExtra("selectedFabricBagColor", 0);
        selected_category = intent.getIntExtra("selected_category", 0);
        yarn_rfid_receive_roll_data = intent.getStringExtra("yarn_rfid_receive_roll_data");
        compactBatchScanResponse = (CompactBatchScanResponse) intent.getSerializableExtra("compacting_roll_data");
        stenteringBatchScanResponse = (StenteringBatchScanResponse) intent.getSerializableExtra("stentering_roll_data");
        slittingBatchScanResponse = (SlitteringSequzBarCodeResponse) intent.getSerializableExtra("slittering_roll_data");
        dataList = (ArrayList<V1_OperationItemModel>) intent.getSerializableExtra("data");
        grey_roll_dataList = (ArrayList<V1_GreyRollReceiveItemModel>) intent.getSerializableExtra("grey_roll_data");
        bagKeepingArrayList =  (ArrayList<V1_BagKeepingResponse.ResultSet>) intent.getSerializableExtra("bag_keeping_data");
        aopBagKeepingArrayList =  (ArrayList<V1_AOPBagKeepingResponse.ResultSet>) intent.getSerializableExtra("aop_bag_keeping_data");
        bagKeepingQCArrayList =  (ArrayList<V1_BagKeepingDataBySystemResponse.ResultSet>) intent.getSerializableExtra("bag_keeping_qc_data");
        aopDeptBarReceiveList =  (ArrayList<V1_AOPDeptBagReceiveResponse.ResultSet>) intent.getSerializableExtra("aop_dept_bag_rcv_data");
        aopDyedDeptBarReceiveList =  (ArrayList<V1_DyedAOPBagReceiveResponse.ResultSet>) intent.getSerializableExtra("dyed_aop_dept_bag_rcv_data");
        greyStoreRejectBagList =  (ArrayList<V1_GreyStoreRejectBagReceiveResponse.ResultSet>) intent.getSerializableExtra("grey_store_reject_bag_rcv_data");
        bagDeliveryArrayList =  (ArrayList<V1_BagDeliveryResponse.ResultSet>) intent.getSerializableExtra("bag_delivery_data");
        bagEmptyReceiveArrayList =  (ArrayList<V1_BagEmptyReceiveResponse.ResultSet>) intent.getSerializableExtra("bag_empty_receive_data");
        bagReceiveArrayList =  (ArrayList<V1_BagReceiveResponse.ResultSet>) intent.getSerializableExtra("bag_receive_data");
        bagIssueArrayList =  (ArrayList<V1_BagIssueResponse.ResultSet>) intent.getSerializableExtra("bag_issue_data");
        bagReturnArrayList =  (ArrayList<V1_BagReturnResponse.ResultSet>) intent.getSerializableExtra("bag_return_data");
        finish_roll_receive_dataList = (ArrayList<FinishFabricRollReceiveRequest.DetailsPart>) intent.getSerializableExtra("finish_receive_roll_data");
        cuttingStoreReceiveModelArrayList = (ArrayList<V1_CuttingStoreReceiveModel>) intent.getSerializableExtra("store_receive_roll_data");
        cuttingStoreIssueModelArrayList = (ArrayList<V1_CuttingStoreIssueModel>) intent.getSerializableExtra("store_issue_roll_data");
        yarnRFIDModelArrayList = (ArrayList<V1_YarnRFIDModel>) intent.getSerializableExtra("yarn_rfid_receive_rfid_data");
         yarnIssueReturnResponse = (V1_YarnIssueReturnResponse) intent.getSerializableExtra("yarn_issue_return_data");
        dtlsYarnRFIDIssueArrayList = (ArrayList<V1_IssueReturnRFIDModel>) intent.getSerializableExtra("yarn_rfid_data");
        gmtFinishReceiveBarcodeList = (ArrayList<V1_GMTFinishReceiveResponse.Data>) intent.getSerializableExtra("gmtFinishReceiveList");

        //transfer out activity
        transfer_out___scan_op = intent.getIntExtra("transfer_out___scan_op", 0);
        transfer_out__location_id = intent.getStringExtra("transfer_out__location_id");
        transfer_out__location_name = intent.getStringExtra("transfer_out__location_name");
        transfer_out__company_id = intent.getStringExtra("transfer_out__company_id");
        transfer_out__po_breakdown_id = intent.getStringExtra("transfer_out__po_breakdown_id");
        transfer_out__floor_id = intent.getStringExtra("transfer_out__floor_id");
        transfer_out__room_id = intent.getStringExtra("transfer_out__room_id");
        transfer_out__rack_id = intent.getStringExtra("transfer_out__rack_id");
        transfer_out__shelf_id = intent.getStringExtra("transfer_out__shelf_id");
        transfer_out__bin_id = intent.getStringExtra("transfer_out__bin_id");
        transfer_out__item_category_id = intent.getStringExtra("transfer_out__item_category_id");
        transfer_out__listOfAllBarcode = (ArrayList<DtlsPart>) intent.getSerializableExtra("transfer_out__listOfAllBarcode");
        transfer_out__storeIdList = intent.getStringArrayListExtra("transfer_out__storeIdList");
        transfer_out__storeNameList = intent.getStringArrayListExtra("transfer_out__storeNameList");

        //transfer in activity
        transfer_in___scan_op = intent.getIntExtra("transfer_in___scan_op", 0);
        transfer_in__location_id = intent.getStringExtra("transfer_in__location_id");
        transfer_in__location_name = intent.getStringExtra("transfer_in__location_name");
        transfer_in__company_id = intent.getStringExtra("transfer_in__company_id");
        transfer_in__po_breakdown_id = intent.getStringExtra("transfer_in__po_breakdown_id");
        transfer_in__listOfAllBarcode = (ArrayList<DtlsPart>) intent.getSerializableExtra("transfer_in__listOfAllBarcode");

        // gtm finish fabric receive activity
        finish_fabric_receive___scan_op = intent.getIntExtra("finish_fabric_receive___scan_op", 0);
        finish_fabric_receive___btFrRackScanData = intent.getStringExtra("finish_fabric_receive___btFrRackScanData");
        finish_fabric_receive___btBarcodeScanData = intent.getStringExtra("finish_fabric_receive___btBarcodeScanData");
        finish_fabric_receive___btBatchCardScanData = intent.getStringExtra("finish_fabric_receive___btBatchCardScanData");
        finish_fabric_receive__listOfAllBarcode = (ArrayList<FFRBarcode>) intent.getSerializableExtra("finish_fabric_receive__listOfAllBarcode");

        addBarcodeReaderFragment();
    }

    private void addBarcodeReaderFragment() {
        BarcodeFragment readerFragment = BarcodeFragment.newInstance();
        readerFragment.setListener(this);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fm_container, readerFragment);
        fragmentTransaction.commitAllowingStateLoss();

        editTextQrCode.setInputType(InputType.TYPE_CLASS_TEXT);
        editTextQrCode.setHint("Scan/Write QR/BAR Code");
    }

    private void removeBarcodeReaderFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        Fragment fragmentById = supportFragmentManager.findFragmentById(R.id.fm_container);
        if (fragmentById != null) {
            fragmentTransaction.remove(fragmentById);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    @OnClick(R.id.image_button_go)
    void onGo() {
        if (editTextQrCode.getText().toString().isEmpty()) {
            String msg = "You must scan/write Barcode No";
            showAlertDialog(msg);
        } else {
            onNextStep();
        }
    }

    private void onNextStep() {
        stopScanner();
        String myResult = editTextQrCode.getText().toString();
        if (type_entry.equals("Grey_Fabric_QC_Entry_V1")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_GreyFabricActivity.class);
            intentDataToActivity(intent, myResult);
            startActivity(intent);
            finish();
        } else if (type_entry.equals("Grey_Fabric_With_Observation")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_GreyObsActivity.class);
            intentDataToActivity(intent, myResult);
            startActivity(intent);
            finish();
        } else if (type_entry.equals("Finish_Fabric_QC_Entry")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_FinishFabricActivity.class);
            intentDataToActivity(intent, myResult);
            startActivity(intent);
            finish();
        } else if (type_entry.equals("Finish_Fabric_With_Observation")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_FinishFabricObsActivity.class);
            intentDataToActivity(intent, myResult);
            startActivity(intent);
            finish();
        } else if (type_entry.equals("bundle_wise_sewing_input")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_SewingInputActivity.class);
            intentDataToActivity(intent, myResult);
            startActivity(intent);
            finish();
        }else if (type_entry.equals("bundle_wise_sewing_input_pcs")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_BundleWiseSewingInput_PCSActivity.class);
            intentDataToActivity(intent, myResult);
            startActivity(intent);
            finish();
        } else if (type_entry.equals("bundle_wise_sewing_output")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_SewingOutputActivity.class);
            intentDataToActivity(intent, myResult);
            startActivity(intent);
            finish();
        } else if (type_entry.equals("line_wise_sewing_input")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_LineWiseSewingInputActivity.class);
            intentDataToActivity(intent, myResult);
            startActivity(intent);
            finish();
        } else if (type_entry.equals("line_wise_sewing_output")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_LineWiseSewingOutputActivity.class);
            intentDataToActivity(intent, myResult);
            startActivity(intent);
            finish();
        } else if (type_entry.equals("linking_input")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_LinkingInputActivity.class);
            intentDataToActivity(intent, myResult);
            startActivity(intent);
            finish();
        } else if (type_entry.equals("linking_output")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_LinkingOutputActivity.class);
            intentDataToActivity(intent, myResult);
            startActivity(intent);
            finish();
        } else if (type_entry.equals("challan_wise_sewing_input")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_ChallanReportActivity.class);
            intentDataToActivity(intent, myResult);
            startActivity(intent);
            finish();
        } else if (type_entry.equals("line_wise_sewing_input_challan_wise")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_ChallanReportActivity.class);
            intentDataToActivity(intent, myResult);
            intent.putExtra("type", type);
            startActivity(intent);
            finish();
        } else if (type_entry.equals("finish_fabric_qc_new_page")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V2_FinishFabricQcEntryActivity.class);
            intent.putExtra("barcodeScan", myResult);
            intent.putExtra("scan_op", op);
            startActivity(intent);
            finish();
        } else if (type_entry.equals("grey_roll_receive_v1")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_GreyRollReceiveActivity.class);
            intent.putExtra("grey_roll_barcodeScan", myResult);
            intent.putExtra("scan_op", op);
            intent.putExtra("grey_roll_data", grey_roll_dataList);
            intent.putExtra("room_rack_scan", room_rack_scan);
            intent.putExtra("barcode_scan", grey_barcode_scan);
            intent.putExtra("challan_scan", grey_challan_scan);
            startActivity(intent);
            finish();
        } else if (type_entry.equals("finish_roll_receive_v1")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_FinishFabricReceiveActivity.class);
            intent.putExtra("finish_roll_barcodeScan", myResult);
            intent.putExtra("scan_op", op);
            intent.putExtra("finish_receive_roll_data", finish_roll_receive_dataList);
            intent.putExtra("room_rack_scan", room_rack_scan);
            intent.putExtra("barcode_scan", grey_barcode_scan);
            intent.putExtra("challan_scan", grey_challan_scan);
        } else if (type_entry.equals("bag_keeping")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_BagKeepingActivity.class);
            intent.putExtra("barcodeScan", myResult);
            intent.putExtra("scan_op", op);
            intent.putExtra("bag_keeping_data", bagKeepingArrayList);
            intent.putExtra("bagScan", bagScan);
            intent.putExtra("batch_scan", batch_scan);
            intent.putExtra("rollWeight", rollWeight);
            intent.putExtra("selectedFabricBagColor", selectedBagColor);
            startActivity(intent);
            finish();
        } else if (type_entry.equals("bag_keeping_print")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_BagKeepingPrintActivity.class);
            intent.putExtra("barcodeScan", myResult);
            intent.putExtra("scan_op", op);
            intent.putExtra("bag_keeping_data", bagKeepingArrayList);
            intent.putExtra("bagScan", bagScan);
            intent.putExtra("batch_scan", batch_scan);
            startActivity(intent);
            finish();
        }else if (type_entry.equals("aop_bag_keeping")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_AOPBagKeepingActivity.class);
            intent.putExtra("barcodeScan", myResult);
            intent.putExtra("scan_op", op);
            intent.putExtra("type", type);
            intent.putExtra("aop_bag_keeping_data", aopBagKeepingArrayList);
            intent.putExtra("bagScan", bagScan);
            intent.putExtra("batch_scan", batch_scan);
            intent.putExtra("rollWeight", rollWeight);
            intent.putExtra("selectedFabricBagColor", selectedBagColor);
            startActivity(intent);
            finish();
        }else if (type_entry.equals("bag_keeping_qc")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_BagKeepingQcActivity.class);
            intent.putExtra("barcodeScan", myResult);
            intent.putExtra("scan_op", op);
            intent.putExtra("bag_keeping_qc_data", bagKeepingQCArrayList);
            intent.putExtra("selectedFabricBagColor", selectedBagColor);
            startActivity(intent);
            finish();
        }else if (type_entry.equals("aop_dept_bag_rcv")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_AOPDeptBagReceiveActivity.class);
            intent.putExtra("barcodeScan", myResult);
            intent.putExtra("scan_op", op);
            intent.putExtra("batch_scan", batch_scan);
            intent.putExtra("aop_dept_bag_rcv_data", aopDeptBarReceiveList);
            startActivity(intent);
            finish();
        }else if (type_entry.equals("dyed_aop_dept_bag_rcv")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_DyedAOPDeptBagReceiveActivity.class);
            intent.putExtra("barcodeScan", myResult);
            intent.putExtra("scan_op", op);
            intent.putExtra("batch_scan", batch_scan);
            intent.putExtra("dyed_aop_dept_bag_rcv_data", aopDyedDeptBarReceiveList);
            startActivity(intent);
            finish();
        }else if (type_entry.equals("grey_store_reject_bag_rcv")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_GreyStoreRejectFabricBagActivity.class);
            intent.putExtra("barcodeScan", myResult);
            intent.putExtra("scan_op", op);
            intent.putExtra("batch_scan", batch_scan);
            intent.putExtra("grey_store_reject_bag_rcv_data", greyStoreRejectBagList);
            startActivity(intent);
            finish();
        }else if (type_entry.equals("bag_delivery")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_BagDeliveryActivity.class);
            intent.putExtra("barcodeScan", myResult);
            intent.putExtra("scan_op", op);
            intent.putExtra("bag_delivery_data", bagDeliveryArrayList);
            intent.putExtra("bagScan", bagScan);
            startActivity(intent);
            finish();
        }else if (type_entry.equals("finishing_qc")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_FabricFinishingQCActivity.class);
            intent.putExtra("barcodeScan", myResult);
            intent.putExtra("scan_op", op);
            startActivity(intent);
            finish();
        }else if (type_entry.equals("bag_empty_receive")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_EmptyBagReceiveActivity.class);
            intent.putExtra("barcodeScan", myResult);
            intent.putExtra("scan_op", op);
            intent.putExtra("bag_empty_receive_data", bagEmptyReceiveArrayList);
            intent.putExtra("bagScan", bagScan);
            startActivity(intent);
            finish();
        }else if (type_entry.equals("bag_receive")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_BagReceiveActivity.class);
            intent.putExtra("barcodeScan", myResult);
            intent.putExtra("scan_op", op);
            intent.putExtra("bag_receive_data", bagReceiveArrayList);
//            intent.putExtra("bagScan", bagScan);
            intent.putExtra("room_rack_scan", room_rack_scan);
            intent.putExtra("selected_category", selected_category);
            startActivity(intent);
            finish();
        }else if (type_entry.equals("bag_issue")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_BagIssueActivity.class);
            intent.putExtra("barcodeScan", myResult);
            intent.putExtra("scan_op", op);
            intent.putExtra("bag_issue_data", bagIssueArrayList);
//            intent.putExtra("bagScan", bagScan);
            intent.putExtra("room_rack_scan", room_rack_scan);
            intent.putExtra("selected_category", selectedCategoryId);
            intent.putExtra("selected_color", selectedColorId);
            startActivity(intent);
            finish();
        }else if (type_entry.equals("bag_return")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_BagReturnActivity.class);
            intent.putExtra("barcodeScan", myResult);
            intent.putExtra("scan_op", op);
            intent.putExtra("bag_return_data", bagReturnArrayList);
            startActivity(intent);
            finish();
        }else if (type_entry.equals("transfer_out_v1")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_TransferoutActivity.class);
            intent.putExtra("transfer_out__listOfAllBarcode", transfer_out__listOfAllBarcode);
            intent.putExtra("barcode_scan", myResult);
            if (transfer_out___scan_op == 1) {
                intent.putExtra("transfer_out___scan_op", "1");
            }
            if (transfer_out___scan_op == 2) {
                intent.putExtra("transfer_out___scan_op", "2");
                intent.putExtra("transfer_out__location_id", transfer_out__location_id);
                intent.putExtra("transfer_out__location_name", transfer_out__location_name);
                intent.putExtra("transfer_out__company_id", transfer_out__company_id);
                intent.putExtra("transfer_out__po_breakdown_id", transfer_out__po_breakdown_id);
                intent.putExtra("transfer_out__floor_id", transfer_out__floor_id);
                intent.putExtra("transfer_out__room_id", transfer_out__room_id);
                intent.putExtra("transfer_out__rack_id", transfer_out__rack_id);
                intent.putExtra("transfer_out__shelf_id", transfer_out__shelf_id);
                intent.putExtra("transfer_out__bin_id", transfer_out__bin_id);
                intent.putExtra("transfer_out__item_category_id", transfer_out__item_category_id);
                intent.putExtra("transfer_out__storeIdList", transfer_out__storeIdList);
                intent.putExtra("transfer_out__storeNameList", transfer_out__storeNameList);
            }
            startActivity(intent);
            finish();
        } else if (type_entry.equals("transfer_in_v1")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_TransferInActivity.class);
            intent.putExtra("transfer_in__listOfAllBarcode", transfer_in__listOfAllBarcode);
            intent.putExtra("barcode_scan", myResult);
            if (transfer_in___scan_op == 1) {
                intent.putExtra("transfer_in___scan_op", "1");
            }
            if (transfer_in___scan_op == 2) {
                intent.putExtra("transfer_in___scan_op", "2");
                intent.putExtra("transfer_in__location_id", transfer_in__location_id);
                intent.putExtra("transfer_in__location_name", transfer_in__location_name);
                intent.putExtra("transfer_in__company_id", transfer_in__company_id);
                intent.putExtra("transfer_in__po_breakdown_id", transfer_in__po_breakdown_id);
            }
            startActivity(intent);
            finish();
        } else if (type_entry.equals("Gtm Finish Fab. Receive")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_Finish_Fabric_Receive.class);
            intent.putExtra("barcode_scan", myResult);
            intent.putExtra("finish_fabric_receive__listOfAllBarcode", finish_fabric_receive__listOfAllBarcode);
            if (finish_fabric_receive___scan_op == 1) {
                intent.putExtra("scan_op", "1");
                intent.putExtra("btBarcodeScanData", finish_fabric_receive___btBarcodeScanData);
                intent.putExtra("btBatchCardScanData", finish_fabric_receive___btBatchCardScanData);
            }
            if (finish_fabric_receive___scan_op == 2) {
                intent.putExtra("scan_op", "2");
                intent.putExtra("btFrRackScanData", finish_fabric_receive___btFrRackScanData);
                intent.putExtra("btBatchCardScanData", finish_fabric_receive___btBatchCardScanData);
            }
            startActivity(intent);
            finish();
        } else if (type_entry.equals("grey_roll_issue_v1")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_GreyFabricRollIssueActivity.class);
            intent.putExtra("grey_roll_barcodeScan", myResult);
            intent.putExtra("scan_op", op);
            intent.putExtra("grey_roll_data", grey_roll_dataList);
            intent.putExtra("room_rack_scan", room_rack_scan);
            intent.putExtra("barcode_scan", grey_barcode_scan);
            startActivity(intent);
            finish();
        }else if (type_entry.equals("grey_roll_delivery_v1")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_RollWiseGreyFabricDeliveryToStoreActivity.class);
            intent.putExtra("grey_roll_barcodeScan", myResult);
            intent.putExtra("scan_op", op);
            intent.putExtra("grey_roll_data", grey_roll_dataList);
            intent.putExtra("barcode_scan", grey_barcode_scan);
            startActivity(intent);
            finish();
        } else if (type_entry.equals("cutting_store_roll_receive_v1")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_CuttingStoreReceiveActivity.class);
            intent.putExtra("store_roll_receive_barcodeScan", myResult);
            intent.putExtra("store_receive_scan_op", cutting_receive_op);
            intent.putExtra("store_receive_roll_data", cuttingStoreReceiveModelArrayList);
            intent.putExtra("store_receive_room_rack_scan", store_receive_room_rack_scan);
            startActivity(intent);
            finish();
        } else if (type_entry.equals("cutting_store_roll_issue_v1")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_CuttingStoreIssueActivity.class);
            intent.putExtra("store_roll_issue_barcodeScan", myResult);
            intent.putExtra("store_issue_roll_data", cuttingStoreIssueModelArrayList);
            startActivity(intent);
            finish();
        } else if(type_entry.equals("Finish_Fabric_QC_Entry")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_FinishFabricActivity.class);
            intentDataToActivity(intent, myResult);
            startActivity(intent);
        } else if (type_entry.equals("yarn_rfid_receive_v1")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_YarnRfidReceiveActivity.class);
            intent.putExtra("yarn_rfid_issue_barcodeScan", myResult);
            intent.putExtra("yarn_rfid_receive_scan_op", yarn_rfid_receive_scan_op);
            intent.putExtra("yarn_rfid_receive_rfid_data", yarnRFIDModelArrayList);
            intent.putExtra("yarn_rfid_receive_roll_data", yarn_rfid_receive_roll_data);
            intent.putExtra("grn_barcodeScan", _grn_barcodeScan);
            intent.putExtra("rfid_location", _rfid_location);
            startActivity(intent);
            finish();
        } else if (type_entry.equals("rfid_yarn_issue_return")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_RFIDTagForYarnIssueReturnActivity.class);
            intent.putExtra("barcodeScan", myResult);
            intent.putExtra("scan_op", op);
            intent.putExtra("yarn_issue_return_data", yarnIssueReturnResponse);
            intent.putExtra("yarn_rfid_data", dtlsYarnRFIDIssueArrayList);
            intent.putExtra("rollWeight", rollWeight);
            intent.putExtra("issueChallanScan", issueChallanScan);
            startActivity(intent);
            finish();
        } else if (type_entry.equals("compacting")) {
            Intent intent = new Intent(V1_ScannerActivity.this, CompactingActivity.class);
            intent.putExtra("barcodeScan", myResult);
            intent.putExtra("scan_op", op);
            intent.putExtra("batch_scan", batch_scan);
            intent.putExtra("compacting_roll_data", compactBatchScanResponse);
            startActivity(intent);
            finish();
        } else if (type_entry.equals("gmt_finish_receive")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_GmtFinishReceiveActivity.class);
            intent.putExtra("barcodeScan", myResult);
            intent.putExtra("scan_op", op);
            intent.putExtra("finishCompanyId", finishCompanyId);
            intent.putExtra("fnCompanyId", fnCompanyId);
            intent.putExtra("fnLocationId", fnCompanyWiseLocationId);
            intent.putExtra("fnFloorId", fnLocationWiseFloorId);
            intent.putExtra("fnLineId", fnFloorWiseLineId);
            intent.putExtra("gmtFinishReceiveList", gmtFinishReceiveBarcodeList);
            startActivity(intent);
            finish();
        } else if (type_entry.equals("compacting_roll_wise")) {
            boolean s = false;
            if (op == 2) {
                for (int j = 0; j < compactBatchScanResponse.getResultset().getDtlsIndex().size(); j++) {
                    if (compactBatchScanResponse.getResultset().getDtlsIndex().get(j).getBarcode_status()) {
                        if (myResult.equals(compactBatchScanResponse.getResultset().getDtlsIndex().get(j).getBarcodeNo())) {
                            DialogHelper.showWarningDialog(this, "Warning", "This barcode: " + myResult + " is already scanned.");
                            addBarcodeReaderFragment();
                            s = true;
                            break;
                        }
                    }
                }
            }
            if (!s) {
                boolean exists = isFinishProductionBarcodeExists(myResult, op);
                if (exists || op == 1) {
                    Intent intent = new Intent(V1_ScannerActivity.this, CompactingRollWiseActivity.class);
                    intent.putExtra("barcodeScan", myResult);
                    intent.putExtra("scan_op", op);
                    intent.putExtra("batch_scan", batch_scan);
                    intent.putExtra("compacting_roll_data", compactBatchScanResponse);
                    startActivity(intent);
                    finish();
                } else {
                    DialogHelper.showWarningDialog(this, "Message", "This barcode: " + myResult + " does not found in this batch.");
                    addBarcodeReaderFragment();
                }
            }
        } else if (type_entry.equals("stentering")) {
            Intent intent = new Intent(V1_ScannerActivity.this, StenteringActivity.class);
            intent.putExtra("barcodeScan", myResult);
            intent.putExtra("scan_op", op);
            intent.putExtra("batch_scan", batch_scan);
            intent.putExtra("stentering_roll_data", stenteringBatchScanResponse);
            startActivity(intent);
            finish();
        } else if (type_entry.equals("stentering_roll_wise")) {
            boolean s = false;

            if (op == 2) {
                for (int j = 0; j < stenteringBatchScanResponse.getResultset().getDtlsIndex().size(); j++) {
                    if (stenteringBatchScanResponse.getResultset().getDtlsIndex().get(j).getBarcode_status()) {
                        if (myResult.equals(stenteringBatchScanResponse.getResultset().getDtlsIndex().get(j).getBarcodeNo())) {
                            DialogHelper.showWarningDialog(this, "Warning", "This barcode: " + myResult + " is already scanned.");
                            addBarcodeReaderFragment();
                            s = true;
                            break;
                        }
                    }
                }
            }
            if (!s) {
                boolean exists = isFinishProductionBarcodeExists(myResult, op);
                if (exists || op == 1) {
                    Intent intent = new Intent(V1_ScannerActivity.this, StenteringRollWiseActivity.class);
                    intent.putExtra("barcodeScan", myResult);
                    intent.putExtra("scan_op", op);
                    intent.putExtra("batch_scan", batch_scan);
                    intent.putExtra("stentering_roll_data", stenteringBatchScanResponse);
                    startActivity(intent);
                    finish();
                } else {
                    DialogHelper.showWarningDialog(this, "Message", "This barcode: " + myResult + " does not found in this batch.");
                    addBarcodeReaderFragment();
                }
            }
        } else if (type_entry.equals("slitting")) {
            Intent intent = new Intent(V1_ScannerActivity.this, SlittingSqueezingActivity.class);
            intent.putExtra("barcodeScan", myResult);
            intent.putExtra("scan_op", op);
            intent.putExtra("batch_scan", batch_scan);
            intent.putExtra("slittering_roll_data", slittingBatchScanResponse);
            startActivity(intent);
            finish();
        } else if (type_entry.equals("slitting_roll_wise")) {
            boolean s = false;
            if (op == 2) {
                for (int j = 0; j < slittingBatchScanResponse.getResultset().getDtlsIndex().size(); j++) {
                    if (slittingBatchScanResponse.getResultset().getDtlsIndex().get(j).getBarcode_status()) {
                        if (myResult.equals(slittingBatchScanResponse.getResultset().getDtlsIndex().get(j).getBarcodeNo())) {
                            DialogHelper.showWarningDialog(this, "Warning", "This barcode: " + myResult + " is already scanned.");
                            addBarcodeReaderFragment();
                            s = true;
                            break;
                        }
                    }
                }
            }

            if (!s) {
                boolean exists = isFinishProductionBarcodeExists(myResult, op);
                if (exists || op == 1) {
                    Intent intent = new Intent(V1_ScannerActivity.this, SlittingSqeezingRollWiseActivity.class);
                    intent.putExtra("barcodeScan", myResult);
                    intent.putExtra("scan_op", op);
                    intent.putExtra("batch_scan", batch_scan);
                    intent.putExtra("slittering_roll_data", slittingBatchScanResponse);
                    startActivity(intent);
                    finish();
                } else {
                    DialogHelper.showWarningDialog(this, "Message", "This barcode: " + myResult + " does not found in this batch.");
                    addBarcodeReaderFragment();
                }
            }

        } else if (type_entry.equals("operations")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_ProductionOperationActivity.class);
            intentDataToActivity(intent, myResult);
            intent.putExtra("type", type);
            if (op == 0) {
                intent.putExtra("bundleScan", myResult);
                intent.putExtra("operationScan", operationScan);
                intent.putExtra("empIdScan", empIdScan);
                intent.putExtra("data", dataList);
            }
            if (op == 1) {
                intent.putExtra("bundleScan", bundleScan);
                intent.putExtra("operationScan", myResult);
                intent.putExtra("empIdScan", empIdScan);
                intent.putExtra("data", dataList);
            }
            if (op == 2) {
                intent.putExtra("bundleScan", bundleScan);
                intent.putExtra("operationScan", operationScan);
                intent.putExtra("empIdScan", myResult);
                intent.putExtra("data", dataList);
            }
            if (op == 3) {
                intent.putExtra("bundleScan", bundleScan);
                intent.putExtra("runningOperation", myResult);
                intent.putExtra("empIdScan", empIdScan);
                intent.putExtra("op", 3);
                intent.putExtra("data", dataList);
            }
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_MenuActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("url", urladdress);
            intent.putExtra("s", savemenu);
            intent.putExtra("u", updatemenu);
            intent.putExtra("qc", type_entry);
            startActivity(intent);
            finish();
        }
    }

    private boolean isFinishProductionBarcodeExists(String result, Integer operation) {
        if (operation == 2) {
            if (type_entry.equals("compacting_roll_wise")) {
                for (CompactBatchScanResponse.DtlsIndex dtlsIndex : compactBatchScanResponse.getResultset().getDtlsIndex()) {
                    if (dtlsIndex.getBarcodeNo().equals(result)) {
                        return true;
                    }
                }
            } else if (type_entry.equals("stentering_roll_wise")) {
                for (StenteringBatchScanResponse.DtlsIndex dtlsIndex : stenteringBatchScanResponse.getResultset().getDtlsIndex()) {
                    if (dtlsIndex.getBarcodeNo().equals(result)) {
                        return true;
                    }
                }
            } else if (type_entry.equals("slitting_roll_wise")) {
                for (SlitteringSequzBarCodeResponse.DtlsIndex dtlsIndex : slittingBatchScanResponse.getResultset().getDtlsIndex()) {
                    if (dtlsIndex.getBarcodeNo().equals(result)) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
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
        if (type_entry.equals("operations")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_ProductionOperationActivity.class);
            intent.putExtra("type", type);
            if (op == 0) {
                intent.putExtra("operationScan", operationScan);
                intent.putExtra("empIdScan", empIdScan);
                intent.putExtra("data", dataList);
            }
            if (op == 1) {
                intent.putExtra("bundleScan", bundleScan);
                intent.putExtra("empIdScan", empIdScan);
                intent.putExtra("data", dataList);
            }
            if (op == 2) {
                intent.putExtra("bundleScan", bundleScan);
                intent.putExtra("operationScan", operationScan);
                intent.putExtra("data", dataList);
            }
            if (op == 3) {
//                intent.putExtra("bundleScan", bundleScan);
                intent.putExtra("operationScan", operationScan);
                intent.putExtra("data", dataList);
            }
            startActivity(intent);
            finish();

        } else if (type_entry.equals("transfer_in_v1")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_TransferInActivity.class);
            intent.putExtra("transfer_in__listOfAllBarcode", transfer_in__listOfAllBarcode);
            if (transfer_in___scan_op == 1) {
                intent.putExtra("transfer_in___scan_op", "1");
            }
            if (transfer_in___scan_op == 2) {
                intent.putExtra("transfer_in___scan_op", "2");
                intent.putExtra("transfer_in__location_id", transfer_in__location_id);
                intent.putExtra("transfer_in__location_name", transfer_in__location_name);
                intent.putExtra("transfer_in__company_id", transfer_in__company_id);
                intent.putExtra("transfer_in__po_breakdown_id", transfer_in__po_breakdown_id);
            }
            startActivity(intent);
            finish();
        } else if (type_entry.equals("transfer_out_v1")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_TransferoutActivity.class);
            intent.putExtra("transfer_out__listOfAllBarcode", transfer_out__listOfAllBarcode);
            if (transfer_out___scan_op == 1) {
                intent.putExtra("transfer_out___scan_op", "1");
            }
            if (transfer_out___scan_op == 2) {
                intent.putExtra("transfer_out___scan_op", "2");
                intent.putExtra("transfer_out__location_id", transfer_out__location_id);
                intent.putExtra("transfer_out__location_name", transfer_out__location_name);
                intent.putExtra("transfer_out__company_id", transfer_out__company_id);
                intent.putExtra("transfer_out__po_breakdown_id", transfer_out__po_breakdown_id);
                intent.putExtra("transfer_out__floor_id", transfer_out__floor_id);
                intent.putExtra("transfer_out__room_id", transfer_out__room_id);
                intent.putExtra("transfer_out__rack_id", transfer_out__rack_id);
                intent.putExtra("transfer_out__shelf_id", transfer_out__shelf_id);
                intent.putExtra("transfer_out__bin_id", transfer_out__bin_id);
                intent.putExtra("transfer_out__item_category_id", transfer_out__item_category_id);
                intent.putExtra("transfer_out__storeIdList", transfer_out__storeIdList);
                intent.putExtra("transfer_out__storeNameList", transfer_out__storeNameList);
            }
            startActivity(intent);
            finish();

        } else if (type_entry.equals("compacting_roll_wise")) {
            Intent intent = new Intent(V1_ScannerActivity.this, CompactingRollWiseActivity.class);
            intent.putExtra("barcodeScan", "");
            intent.putExtra("scan_op", op);
            intent.putExtra("batch_scan", batch_scan);
            intent.putExtra("compacting_roll_data", compactBatchScanResponse);
            startActivity(intent);
            finish();
        } else if (type_entry.equals("stentering_roll_wise")) {
            Intent intent = new Intent(V1_ScannerActivity.this, StenteringRollWiseActivity.class);
            intent.putExtra("barcodeScan", "");
            intent.putExtra("scan_op", op);
            intent.putExtra("batch_scan", batch_scan);
            intent.putExtra("stentering_roll_data", stenteringBatchScanResponse);
            startActivity(intent);
            finish();

        } else if (type_entry.equals("slitting_roll_wise")) {
            Intent intent = new Intent(V1_ScannerActivity.this, SlittingSqeezingRollWiseActivity.class);
            intent.putExtra("barcodeScan", "");
            intent.putExtra("scan_op", op);
            intent.putExtra("batch_scan", batch_scan);
            intent.putExtra("slittering_roll_data", slittingBatchScanResponse);
            startActivity(intent);
            finish();

        } else if (type_entry.equals("grey_roll_receive_v1")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_GreyRollReceiveActivity.class);
            intent.putExtra("scan_op", op);
            intent.putExtra("grey_roll_data", grey_roll_dataList);
            intent.putExtra("room_rack_scan", room_rack_scan);
            intent.putExtra("barcode_scan", grey_barcode_scan);
            intent.putExtra("challan_scan", grey_challan_scan);
            startActivity(intent);
            finish();

        }else if (type_entry.equals("rfid_yarn_issue_return")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_RFIDTagForYarnIssueReturnActivity.class);
            intent.putExtra("scan_op", op);
            intent.putExtra("yarn_issue_return_data", yarnIssueReturnResponse);
            intent.putExtra("yarn_rfid_data", dtlsYarnRFIDIssueArrayList);
            intent.putExtra("rollWeight", rollWeight);
            intent.putExtra("issueChallanScan", issueChallanScan);
            startActivity(intent);
        } else if (type_entry.equals("bag_keeping")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_BagKeepingActivity.class);
            intent.putExtra("scan_op", op);
            intent.putExtra("bag_keeping_data", bagKeepingArrayList);
            intent.putExtra("bagScan", bagScan);
            intent.putExtra("batch_scan", batch_scan);
            intent.putExtra("selectedFabricBagColor", selectedBagColor);
            intent.putExtra("rollWeight", rollWeight);
            startActivity(intent);
            finish();
        }else if (type_entry.equals("bag_keeping_print")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_BagKeepingPrintActivity.class);
            intent.putExtra("scan_op", op);
            intent.putExtra("bag_keeping_data", bagKeepingArrayList);
            intent.putExtra("bagScan", bagScan);
            intent.putExtra("batch_scan", batch_scan);
            startActivity(intent);
            finish();
        }else if (type_entry.equals("aop_bag_keeping")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_AOPBagKeepingActivity.class);
            intent.putExtra("scan_op", op);
            intent.putExtra("type", type);
            intent.putExtra("aop_bag_keeping_data", aopBagKeepingArrayList);
            intent.putExtra("bagScan", bagScan);
            intent.putExtra("batch_scan", batch_scan);
            intent.putExtra("selectedFabricBagColor", selectedBagColor);
            intent.putExtra("rollWeight", rollWeight);
            startActivity(intent);
            finish();
        }else if (type_entry.equals("bag_keeping_qc")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_BagKeepingQcActivity.class);
            intent.putExtra("scan_op", op);
            intent.putExtra("bag_keeping_qc_data", bagKeepingQCArrayList);
            intent.putExtra("selectedFabricBagColor", selectedBagColor);
            startActivity(intent);
            finish();
        }else if (type_entry.equals("dyed_aop_dept_bag_rcv")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_DyedAOPDeptBagReceiveActivity.class);
            intent.putExtra("scan_op", op);
            intent.putExtra("batch_scan", batch_scan);
            intent.putExtra("dyed_aop_dept_bag_rcv_data", aopDeptBarReceiveList);
            startActivity(intent);
            finish();
        }else if (type_entry.equals("grey_store_reject_bag_rcv")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_GreyStoreRejectFabricBagActivity.class);
            intent.putExtra("scan_op", op);
            intent.putExtra("batch_scan", batch_scan);
            intent.putExtra("grey_store_reject_bag_rcv_data", greyStoreRejectBagList);
            startActivity(intent);
            finish();
        }else if (type_entry.equals("bag_delivery")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_BagDeliveryActivity.class);
            intent.putExtra("scan_op", op);
            intent.putExtra("bag_delivery_data", bagDeliveryArrayList);
            intent.putExtra("bagScan", bagScan);
            startActivity(intent);
            finish();
        }else if (type_entry.equals("finishing_qc")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_FabricFinishingQCActivity.class);
            intent.putExtra("scan_op", op);
            startActivity(intent);
            finish();
        }else if (type_entry.equals("bag_empty_receive")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_EmptyBagReceiveActivity.class);
            intent.putExtra("scan_op", op);
            intent.putExtra("bag_empty_receive_data", bagEmptyReceiveArrayList);
            intent.putExtra("bagScan", bagScan);
            startActivity(intent);
            finish();
        }else if (type_entry.equals("bag_receive")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_BagReceiveActivity.class);
            intent.putExtra("scan_op", op);
            intent.putExtra("bag_receive_data", bagReceiveArrayList);
//            intent.putExtra("bagScan", bagScan);
            intent.putExtra("room_rack_scan", room_rack_scan);
            intent.putExtra("selected_category", selected_category);
            startActivity(intent);
            finish();
        }else if (type_entry.equals("bag_issue")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_BagIssueActivity.class);
            intent.putExtra("scan_op", op);
            intent.putExtra("bag_issue_data", bagIssueArrayList);
//            intent.putExtra("bagScan", bagScan);
            intent.putExtra("room_rack_scan", room_rack_scan);
            intent.putExtra("selected_category", selectedCategoryId);
            intent.putExtra("selected_color", selectedColorId);
            startActivity(intent);
            finish();
        }else if (type_entry.equals("bag_return")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_BagReturnActivity.class);
            intent.putExtra("scan_op", op);
            intent.putExtra("bag_return_data", bagReturnArrayList);
            startActivity(intent);
            finish();
        }else if (type_entry.equals("gmt_finish_receive")) {
            Intent intent = new Intent(V1_ScannerActivity.this, V1_GmtFinishReceiveActivity.class);
            intent.putExtra("scan_op", op);
            intent.putExtra("finishCompanyId", finishCompanyId);
            intent.putExtra("fnCompanyId", fnCompanyId);
            intent.putExtra("fnLocationId", fnCompanyWiseLocationId);
            intent.putExtra("fnFloorId", fnLocationWiseFloorId);
            intent.putExtra("fnLineId", fnFloorWiseLineId);
            intent.putExtra("gmtFinishReceiveList", gmtFinishReceiveBarcodeList);
            startActivity(intent);
            finish();
        } else {
            finish();
        }
    }

    @Override
    public void onScanned(Barcode barcode) {
        editTextQrCode.setText(barcode.rawValue);
        onNextStep();
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {

    }

    private void stopScanner() {
        imageButtonGo.setClickable(false);
        imageButtonGo.setImageAlpha(32);
        removeBarcodeReaderFragment();
    }
}