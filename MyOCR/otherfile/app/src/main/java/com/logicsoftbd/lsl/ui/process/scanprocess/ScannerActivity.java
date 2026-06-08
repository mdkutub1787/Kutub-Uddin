package com.logicsoftbd.lsl.ui.process.scanprocess;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;
import com.logicsoftbd.lsl.ui.process.greyroll.CuttingQcActivity_test;
import com.notbytes.barcode_reader.BarcodeReaderFragment;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.model.Process;
import com.logicsoftbd.lsl.data.network.model.BarcodeIssueResponse;
import com.logicsoftbd.lsl.data.network.model.BarcodeResponse;
import com.logicsoftbd.lsl.data.network.model.CuttingQcBarcodeResponse;
import com.logicsoftbd.lsl.data.network.model.EmbSpBarcodeResponse;
import com.logicsoftbd.lsl.data.network.model.FinishFabricIssueSet;
import com.logicsoftbd.lsl.data.network.model.FinishFabricResponse;
import com.logicsoftbd.lsl.data.network.model.FinishFabricResultSet;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRollReceive;
import com.logicsoftbd.lsl.data.network.model.KnittingResponse;
import com.logicsoftbd.lsl.data.network.model.SewingResponse;
import com.logicsoftbd.lsl.ui.base.BaseActivity;
import com.logicsoftbd.lsl.ui.dyeingProduction.DyeingProductionActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.CuttingQcActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.FinishFabricInputActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.FinishFabricIssueRollActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.FinishFabricIssueRollReceiveActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.FinishFabricResultSetActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.IssueActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.IssueWorkActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.KnittingQcResultEntryActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.ReceiveActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.ReceiveWorkActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.SewingInputActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScannerActivity extends BaseActivity implements ScannerMvpView, BarcodeReaderFragment.BarcodeReaderListener {
    private static final String TAG = "ScannerActivity";
    private static final String cameraPerm = Manifest.permission.CAMERA;
    public static final String EXTRA_BUNDLE_ID = "extra_bundle_id";

    private boolean mIsActivityResult = false;

    @Inject
    ScannerMvpPresenter<ScannerMvpView, ScannerMvpInteractor> mPresenter;

    public static Intent getStartIntent(Context context, Process process) {
        Intent intent = new Intent(context, ScannerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_BUNDLE_ID, process);
        intent.putExtras(bundle);
        return intent;
    }
    public static Intent getStartIntent(Context context, Process process, boolean isActivityResult) {
        process.setActivityResult(isActivityResult);
        Intent intent = new Intent(context, ScannerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_BUNDLE_ID, process);
        intent.putExtras(bundle);
        return intent;
    }



    // UI
    @BindView(R.id.et_qr_code)
    EditText editTextQrCode;

    @BindView(R.id.image_button_go)
    ImageView imageButtonGo;


    @BindView(R.id.fm_container)
    FrameLayout frameLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    private Process mProcess;

    private List<BarcodeResponse> responseList;

    private String mType, loadUnload;
    private boolean isIssue = true;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        setUp();
        mPresenter.onAttach(ScannerActivity.this);
    }

    @Override
    protected void setUp() {

//        Intent intent = getIntent();
//        Log.d(TAG, "setUp: ######## "+intent.getStringExtra("type"));

        mProcess = (Process) getIntent().getSerializableExtra(EXTRA_BUNDLE_ID);
        mType = mProcess.getDataParam().getPageParam()+"_"+mProcess.getDataParam().getTypeParam();
        loadUnload = mProcess.getTitle();
        Log.d(TAG, "setUp: ########"+mType);
        // in case cost id is not sent
        if (mProcess == null) {
            showMessage("Data not found!!");
            finish();
            return;
        }
        if(mType.equalsIgnoreCase("store_fabric")) {
            editTextQrCode.setHint("Scan/Write Challan");
        }
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setTitle(R.string.scanner);
        /*setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        responseList = new ArrayList<>();
        addBarcodeReaderFragment();
    }

    private void addBarcodeReaderFragment() {
        BarcodeFragment readerFragment = BarcodeFragment.newInstance();
        readerFragment.setListener(this);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fm_container, readerFragment);
        fragmentTransaction.commitAllowingStateLoss();

        if(mType.equalsIgnoreCase("grey_roll_receive")) {
            isIssue = false;
            editTextQrCode.setInputType(InputType.TYPE_CLASS_TEXT);
            editTextQrCode.setHint("Scan/Write Delivery Challan");
        }
        else if(mType.equalsIgnoreCase("cutting_qc_input")) {
            isIssue = false;
            editTextQrCode.setInputType(InputType.TYPE_CLASS_TEXT);
            editTextQrCode.setHint("Scan/Write Delivery Challan");
//            editTextQrCode.setText("AKDL-");
        }
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
        if(editTextQrCode.getText().toString().isEmpty()) {
            String msg = isIssue ? "You must scan/write Barcode No" : "You must scan/write Delivery Challan";
            showAlertDialog(msg);
        } else {
            onNextStep();
        }
    }



    private void onNextStep() {
        stopScanner();
        mProcess.getDataParam().setBarcode(editTextQrCode.getText().toString());

        if(mType.equalsIgnoreCase("grey_roll_receive")) {
            mPresenter.onNextClick(mProcess.getDataParam());
        } else if (mType.equalsIgnoreCase("grey_roll_issue")) {
            String regexStr = "^[0-9]*$";

            if(editTextQrCode.getText().toString().trim().matches(regexStr)) {
                mPresenter.onNextClickIssue(mProcess.getDataParam());
            }
            else{
                startScanner();
                showAlertDialog("Invalid Barcode");
            }
        }
        else if(mType.equalsIgnoreCase("cutting_qc_input")) {
            mPresenter.onNextClickCuttingQc(mProcess.getDataParam());
            mProcess.getDataParam().setProductionProcess(1);
        }
        /*else if(mType.equalsIgnoreCase("cutting_qc_input")) {
            mPresenter.onNextClickGmts(mProcess.getDataParam());
        }*/
        else if(mType.equalsIgnoreCase("print_issue")) {
            mProcess.getDataParam().setType(1);
            mProcess.getDataParam().setProductionProcess(8);
            mPresenter.onNextClickEmbSp(mProcess.getDataParam());
        }
        else if(mType.equalsIgnoreCase("print_receive")) {
            mProcess.getDataParam().setType(1);
            mProcess.getDataParam().setProductionProcess(8);
            mPresenter.onNextClickEmbSp(mProcess.getDataParam());
        }
        else if(mType.equalsIgnoreCase("embroidery_issue")) {
            mProcess.getDataParam().setType(2);
            mProcess.getDataParam().setProductionProcess(9);
            mPresenter.onNextClickEmbSp(mProcess.getDataParam());
        }
        else if(mType.equalsIgnoreCase("embroidery_receive")) {
            mProcess.getDataParam().setType(2);
            mProcess.getDataParam().setProductionProcess(9);
            mPresenter.onNextClickEmbSp(mProcess.getDataParam());
        }
        else if(mType.equalsIgnoreCase("special_work_issue")) {
            mProcess.getDataParam().setType(4);
            mProcess.getDataParam().setProductionProcess(9);
            mPresenter.onNextClickEmbSp(mProcess.getDataParam());
        }
        else if(mType.equalsIgnoreCase("special_work_receive")) {
            mProcess.getDataParam().setType(4);
            mProcess.getDataParam().setProductionProcess(9);
            mPresenter.onNextClickEmbSp(mProcess.getDataParam());
        }
        else if(mType.equalsIgnoreCase("sewing_input")) {
            mPresenter.onNextClickSewing(mProcess.getDataParam());
            mProcess.getDataParam().setProductionProcess(5);
        }
        else if(mType.equalsIgnoreCase("sewing_output")) {
            mPresenter.onNextClickSewing(mProcess.getDataParam());
            mProcess.getDataParam().setProductionProcess(5);
        }
        else if(mType.equalsIgnoreCase("finish_fabric")) {
            mPresenter.onNextClickFinishFabric(mProcess.getDataParam());
            mProcess.getDataParam().setProductionProcess(5);
        }
        else if(mType.equalsIgnoreCase("result_fabric")) {
            mPresenter.onNextClickFinishResult(mProcess.getDataParam());
            mProcess.getDataParam().setProductionProcess(5);
        }
        else if(mType.equalsIgnoreCase("issue_fabric")) {
            mPresenter.onNextClickFinishIssue(mProcess.getDataParam());
            mProcess.getDataParam().setProductionProcess(5);
        }
        else if(mType.equalsIgnoreCase("store_fabric")) {
            mPresenter.onNextClickStoreIssue(mProcess.getDataParam());
            mProcess.getDataParam().setProductionProcess(5);
        }
        else if(mType.equalsIgnoreCase("store_knitting")) {
            mPresenter.onNextClickKnitting(mProcess.getDataParam());
            mProcess.getDataParam().setProductionProcess(5);
        }else if(mType.equalsIgnoreCase("result_dyeing")){
            startActivity( DyeingProductionActivity.getStartIntent(ScannerActivity.this, new Process(R.drawable.process, loadUnload, "",
                    new Process.DataParam("result", editTextQrCode.getText().toString()))));
            editTextQrCode.setText("");
            finish();
        }
//        else if(mType.equalsIgnoreCase("result_slitting")){
//            startActivity( SlittingSqueezingActivity.getStartIntent(ScannerActivity.this, new Process(R.drawable.process, "",
//                    new Process.DataParam("result", editTextQrCode.getText().toString()))));
//            editTextQrCode.setText("");
//            finish();
//        }else if(mType.equalsIgnoreCase("result_stentering")){
//            startActivity( StenteringActivity.getStartIntent(ScannerActivity.this, new Process(R.drawable.process, "",
//                    new Process.DataParam("result", editTextQrCode.getText().toString()))));
//            editTextQrCode.setText("");
//            finish();
//        }
//        else if(mType.equalsIgnoreCase("result_compacting")){
//            startActivity( CompactingActivity.getStartIntent(ScannerActivity.this, new Process(R.drawable.process, "",
//                    new Process.DataParam("result", editTextQrCode.getText().toString()))));
//            editTextQrCode.setText("");
//            finish();
//        }
    }
    @Override
    public void bundleResponse(BarcodeResponse bundleResponse) {
        startActivity(ReceiveActivity.getStartIntent(this, bundleResponse));
        finish();
    }

    @Override
    public void bundleErrorResponse(String msg) {
        showAlertDialog(new DialogButtonClickListener() {
            @Override
            public void onButtonClick() {
                startScanner();
            }
        }, msg);

    }

    @Override
    public void issueResponse(BarcodeIssueResponse bundleResponse) {
        if(mProcess.isActivityResult()) {
            setResult(Activity.RESULT_OK, IssueActivity.getStartIntent(this, bundleResponse, false));
        } else {
            startActivity(IssueActivity.getStartIntent(this, bundleResponse));
        }
        finish();
    }

    @Override
    public void issueErrorResponse(String msg) {
        showAlertDialog(new DialogButtonClickListener() {
            @Override
            public void onButtonClick() {
                startScanner();
            }
        }, msg);
    }

    @Override
    public void sewingInputResponse(SewingResponse barcodeResponse) {
        if(mProcess.isActivityResult()) {
                setResult(Activity.RESULT_OK, SewingInputActivity.getStartIntent(this, mProcess,barcodeResponse, false));

        } else {
                startActivity(SewingInputActivity.getStartIntent(this, mProcess, barcodeResponse));

        }
        finish();
    }

    @Override
    public void embSpResponse(EmbSpBarcodeResponse barcodeResponse) {
        if(mProcess.isActivityResult()) {
            if (mProcess.getDataParam().getTypeParam().equalsIgnoreCase("issue")) {
                setResult(Activity.RESULT_OK, IssueWorkActivity.getStartIntent(this, barcodeResponse, mProcess, false));
            } else {
                setResult(Activity.RESULT_OK, ReceiveWorkActivity.getStartIntent(this, barcodeResponse,mProcess, false));
            }
        } else {
            if (mProcess.getDataParam().getTypeParam().equalsIgnoreCase("issue")) {
                startActivity(IssueWorkActivity.getStartIntent(this, barcodeResponse, mProcess));
            } else {
                startActivity(ReceiveWorkActivity.getStartIntent(this, barcodeResponse,mProcess));
            }
        }
        finish();
    }

    @Override
    public void cuttingQcResponse(CuttingQcBarcodeResponse barcodeResponse) {
        startActivity(CuttingQcActivity_test.getStartIntent(this, barcodeResponse));
        finish();
    }

    @Override
    public void finishFabricResponse(FinishFabricResponse barcodeResponse) {
        Log.e("FinishFabricResponse","FinishFabricResponse"+new Gson().toJson(barcodeResponse));
        if(mProcess.isActivityResult()) {
            setResult(Activity.RESULT_OK, FinishFabricInputActivity.getStartIntent(this, mProcess,barcodeResponse, false));

        } else {
            startActivity(FinishFabricInputActivity.getStartIntent(this, mProcess, barcodeResponse));

        }
        finish();
    }

    @Override
    public void finishFabricResultSetResponse(FinishFabricResultSet finishFabricResultSet) {
        Log.e("finishFabricResultSet","finishFabricResultSet"+new Gson().toJson(finishFabricResultSet));
        startActivity(FinishFabricResultSetActivity.getStartIntent(this, finishFabricResultSet));

    }

    @Override
    public void finishFabricIssueSetResponse(FinishFabricIssueSet finishFabricIssueSet) {
        Log.e("FinishFabricResponse","FinishFabricResponse"+new Gson().toJson(finishFabricIssueSet));
        if(mProcess.isActivityResult()) {
           setResult(Activity.RESULT_OK, FinishFabricIssueRollActivity.getStartIntent(this, mProcess,finishFabricIssueSet, false));

        } else {
           startActivity(FinishFabricIssueRollActivity.getStartIntent(this, mProcess, finishFabricIssueSet));

        }
        finish();
    }


    @Override
    public void finishFabricRollReceiveResponse(FinishFabricRollReceive finishFabricRollReceive) {
        Log.e("finishFabricRollReceive","finishFabricRollReceive"+new Gson().toJson(finishFabricRollReceive));
        startActivity(FinishFabricIssueRollReceiveActivity.getStartIntent(this, mProcess, finishFabricRollReceive,editTextQrCode.getText().toString()));
        finish();
    }

    @Override
    public void knittingResponse(KnittingResponse knittingResponse) {
        Log.e("knittingResponse","knittingResponse"+new Gson().toJson(knittingResponse));
        startActivity(KnittingQcResultEntryActivity.getStartIntent(this, knittingResponse));
        finish();
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

    private void startScanner() {
        editTextQrCode.setText("");
        imageButtonGo.setClickable(true);
        imageButtonGo.setImageAlpha(255);
        addBarcodeReaderFragment();
    }
    private void stopScanner() {
        imageButtonGo.setClickable(false);
        imageButtonGo.setImageAlpha(32);
        removeBarcodeReaderFragment();
    }

}