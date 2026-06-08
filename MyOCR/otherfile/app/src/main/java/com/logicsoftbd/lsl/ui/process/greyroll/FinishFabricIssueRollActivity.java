package com.logicsoftbd.lsl.ui.process.greyroll;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.model.Process;
import com.logicsoftbd.lsl.data.network.model.DefectInchModel;
import com.logicsoftbd.lsl.data.network.model.DefectListModel;
import com.logicsoftbd.lsl.data.network.model.FabricGradeModel;
import com.logicsoftbd.lsl.data.network.model.FabricShade;
import com.logicsoftbd.lsl.data.network.model.FinishFabricIssueSet;
import com.logicsoftbd.lsl.data.network.model.FinishFabricQrCodeResponses;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRequest;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRollRequest;
import com.logicsoftbd.lsl.data.network.model.FloorResponse;
import com.logicsoftbd.lsl.data.network.model.IssuePurposeModel;
import com.logicsoftbd.lsl.data.network.model.IssueStoreModel;
import com.logicsoftbd.lsl.data.network.model.LineResponse;
import com.logicsoftbd.lsl.data.network.model.LocationModel;
import com.logicsoftbd.lsl.data.network.model.MachineResponses;
import com.logicsoftbd.lsl.data.network.model.PurposeResponse;
import com.logicsoftbd.lsl.data.network.model.ReferenceDataResponse;
import com.logicsoftbd.lsl.data.network.model.ShiftResponses;
import com.logicsoftbd.lsl.data.network.model.StoreResponse;
import com.logicsoftbd.lsl.ui.base.BaseActivity;
import com.logicsoftbd.lsl.ui.process.DatePickerFragment;
import com.logicsoftbd.lsl.ui.process.scanprocess.ScannerActivity;
import com.logicsoftbd.lsl.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FinishFabricIssueRollActivity extends BaseActivity implements ReceiveMvpView ,FinishFabricIssueAdapter.Callback, DatePickerDialog.OnDateSetListener{
    private static final int REQUEST_WRITE_PERMISSION = 786;
    public static final String EXTRA_RECEIVE_ID = "extra_bundle_issue_id";
    public static final String EXTRA_PROCESS_ID = "extra_bundle_process_id";
    private  static final int PICK_BARCODE_REQUEST = 1;
    private  static final int PICK_QUANTITY_REQUEST = 2;

    @Inject
    ReceiveMvpPresenter<ReceiveMvpView, ReceiveMvpInteractor> mPresenter;

    @Inject
    FinishFabricIssueAdapter mAdapter;

    @Inject
    LinearLayoutManager mLayoutManager;

    @BindView(R.id.tvDate)
    TextView tvDate;

    private Date mSelectedDate;

    public static Intent getStartIntent(Context context, Process model, FinishFabricIssueSet process) {
        Intent intent = new Intent(context, FinishFabricIssueRollActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_RECEIVE_ID, process);
        bundle.putSerializable(EXTRA_PROCESS_ID, model);
        intent.putExtras(bundle);
        return intent;
    }
    public static Intent getStartIntent(Context context, Process model, FinishFabricIssueSet process, boolean isFirst) {
        process.setFirst(isFirst);
        Intent intent = new Intent(context, FinishFabricIssueRollActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_RECEIVE_ID, process);
        bundle.putSerializable(EXTRA_PROCESS_ID, model);
        intent.putExtras(bundle);
        return intent;
    }


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_grey_roll)
    RecyclerView mRecyclerView;
    @BindView(R.id.spn_purpose)
    Spinner spn_purpose;
    @BindView(R.id.spn_store)
    Spinner spn_store;
    ArrayList<IssuePurposeModel.Result> mArrayIssuePurposeList = new ArrayList<>();
    ArrayList<IssueStoreModel.Result> mArrayIssueStoreList = new ArrayList<>();
    ArrayAdapter<IssuePurposeModel.Result> mIssuePurposeArrayAdapter;
    ArrayAdapter<IssueStoreModel.Result> mIssueStoreArrayAdapter;

    FinishFabricRollRequest finishFabricRollRequest= new FinishFabricRollRequest();
    Boolean forTest=false;
    private FinishFabricRequest sewingRequest;

    private List<FinishFabricIssueSet.DetailsSet> productBarcodes = new ArrayList<>();

    private ArrayList<String> mHourList;
    private FinishFabricIssueSet mBarcodeResponse;
    private Process mProcess;
    private int mPurposeId;
    private String mStoreId;
    private String insertedBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_fabric_issue_roll);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
        mAdapter.setCallback(this);
        SharedPreferences _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        insertedBy = _preferences.getString("login_userid", "");
        Log.d("TAG", "onCreate: ###"+insertedBy);
        setUp();
        invalidateOptionsMenu();
    }

    @Override
    protected void setUp() {

        mBarcodeResponse = (FinishFabricIssueSet) getIntent().getSerializableExtra(EXTRA_RECEIVE_ID);
        mProcess = (Process) getIntent().getSerializableExtra(EXTRA_PROCESS_ID);
        // in case cost id is not sent
        if (mBarcodeResponse.isFirst() && mBarcodeResponse == null) {
            showMessage("Data not found!!");
            finish();
            return;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = new Date(System.currentTimeMillis());
        String currentDate = formatter.format(date);
        tvDate.setText(currentDate);
        mToolbar.setTitle("Finish Fabric Roll Issue");
        mPresenter.getIssuePurposeList();
        mPresenter.getIssueStoreList();
        Log.e("user","id"+mPresenter.getUserId());
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        if(mBarcodeResponse.isFirst()) {
            productBarcodes.add(mBarcodeResponse.getData());

        }
        // setHourSpinner();
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        spn_purpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("sp_water", "" + mArrayIssuePurposeList.get(position).getPURPOSE_ID());
                mPurposeId = Integer.parseInt(mArrayIssuePurposeList.get(position).getPURPOSE_ID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spn_store.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("sp_water", "" + mArrayIssueStoreList.get(position).getID());
                mStoreId = String.valueOf(mArrayIssueStoreList.get(position).getID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.addItems(productBarcodes);
    }

    private void extractFormData() {
        FinishFabricRollRequest.Result result = new FinishFabricRollRequest.Result();
        FinishFabricRollRequest.Barcode barcode = new FinishFabricRollRequest.Barcode();
        FinishFabricRollRequest.MasterPart masterPart = new FinishFabricRollRequest.MasterPart();
        masterPart.setCompanyId(Integer.parseInt(productBarcodes.get(0).getCOMPANY_ID()));
        masterPart.setBATCH_ID(Integer.valueOf(productBarcodes.get(0).getBATCH_ID()));
        masterPart.setISSUE_DATE(tvDate.getText().toString());
        masterPart.setISSUE_PURPOSE(mPurposeId);
        masterPart.setINSERTED_BY(Integer.valueOf(insertedBy));
//        masterPart.setUserId(1);
//        masterPart.setReceiveDate(tvDate.getText().toString());
//        result.setMasterPart(new FinishFabricRequest.MasterPart());
//        result.setDetailsPart(productBarcodes);
//        result.setMasterPart(masterPart);
//
//        sewingRequest = new FinishFabricRequest();
//        sewingRequest.setData(result);
//        sewingRequest.setStatus("true");

        ArrayList<String> barcodeString=new ArrayList<>();

        List<FinishFabricRollRequest.DetailsPart> finisList = new ArrayList<>();
        for (FinishFabricIssueSet.DetailsSet detailsSet :productBarcodes){
            barcodeString.add(detailsSet.getBARCODE_NO());
            FinishFabricRollRequest.DetailsPart detailsPart= new FinishFabricRollRequest.DetailsPart();
            detailsPart.setBARCODE_NO(detailsSet.getBARCODE_NO());
            detailsPart.setRECEIVE_BASIS(detailsSet.getRECEIVE_BASIS_ID());
            detailsPart.setPI_WO_BATCH_NO(detailsSet.getBATCH_ID());
            detailsPart.setBOOKING_WITHOUT_ORDER(detailsSet.getBOOKING_WITHOUT_ORDER());
            detailsPart.setBOOKING_NO(detailsSet.getBOOKING_NO());
            detailsPart.setCOMPANY_ID(detailsSet.getCOMPANY_ID());
            detailsPart.setPROD_ID(detailsSet.getPROD_ID());
            detailsPart.setGMT_ITEM_ID(detailsSet.getGMT_ITEM_ID());
            detailsPart.setBODY_PART_ID(detailsSet.getBODY_PART_ID());
            detailsPart.setCOLOR_ID(detailsSet.getCOLOR_ID());
            detailsPart.setPO_ID(detailsSet.getPO_ID());
            detailsPart.setITEM_CATEGORY("2");
            detailsPart.setTRANSACTION_TYPE("2");
            detailsPart.setSTORE_ID(mStoreId);
            detailsPart.setCONS_QUANTITY(String.valueOf(detailsSet.getQC_PASS_QNTY()));
            detailsPart.setRATE("0");
            detailsPart.setINSERTED_BY(insertedBy);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            Date date = new Date(System.currentTimeMillis());
            String currentDate = formatter.format(date);
            detailsPart.setINSERT_DATE(currentDate);
            detailsPart.setTRANSACTION_DATE(tvDate.getText().toString());
            finisList.add(detailsPart);
        }
        StringJoiner sj = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sj = new StringJoiner(",");
        }
        for (String s : barcodeString) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                sj.add(s);
            }
        }
        barcode.BARCODE_NO=sj.toString();
        result.setDetailsPart(finisList);
        result.setMasterPart(masterPart);
        result.setBarcodeNos(barcode);
        finishFabricRollRequest.setData(result);
        finishFabricRollRequest.setStatus("true");
        Log.e("data","data"+new Gson().toJson(finishFabricRollRequest));
        //  rollReceiveRequest = mPresenter.convertToRollIssue(mBarcodeResponse, productBarcodes);
        //  rollReceiveRequest.getData().getMasterPart().setIssuePurpose( mPurposeList.get( mSpinnerPurpose.getSelectedItemPosition()-1).getId());
    }
    private boolean isValidForm() {
        if(productBarcodes == null || productBarcodes.size() == 0) {
            showAlertDialog("Please add barcode.");
            return false;
        }




        return true;
    }
    @OnClick(R.id.tvDate)
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setListener(this);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mSelectedDate = DateUtils.createDate(year, monthOfYear, dayOfMonth);
        tvDate.setText(DateUtils.formatDate(mSelectedDate));
    }

    @OnClick(R.id.fab)
    void onFabClick () {
        startActivityForResult(ScannerActivity.getStartIntent(this, mProcess, true), PICK_BARCODE_REQUEST);
    }


    private boolean hasItem(FinishFabricIssueSet.DetailsSet productBarcode) {
        int pos  = 0;
        if(productBarcodes != null && productBarcodes.size() > 0) {
            for (FinishFabricIssueSet.DetailsSet barcode: productBarcodes) {
                if(barcode.getBARCODE_NO().endsWith(productBarcode.getBARCODE_NO())) {
                    hasItemPos = pos;
                    return true;
                }
                pos++;
            }
        }

        return false;
    }

    private int hasItemPos = 0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_BARCODE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                FinishFabricIssueSet mBarcodeResponse = (FinishFabricIssueSet) data.getSerializableExtra(EXTRA_RECEIVE_ID);
                if (!hasItem(mBarcodeResponse.getData())) {
                    productBarcodes.add(mBarcodeResponse.getData());
                } else {
                    showMessage("Item already on the list");
                }
            }
        } else if (requestCode == PICK_QUANTITY_REQUEST) {
            if (resultCode == RESULT_OK) {
                FinishFabricIssueSet mBarcodeResponse = (FinishFabricIssueSet) data.getSerializableExtra(EXTRA_RECEIVE_ID);
                if (hasItem(mBarcodeResponse.getData())) {
                    productBarcodes.set(hasItemPos, mBarcodeResponse.getData());
                }
            }
        }
    }
    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            if(isValidForm()) {
                extractFormData();
                mPresenter.onFinishFabricRollIssueSave(finishFabricRollRequest);


            }
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    public void onSuccess(String msg) {
        showAlertDialog(new DialogButtonClickListener() {
            @Override
            public void onButtonClick() {

                finish();
            }
        }, msg);
    }

    @Override
    public void onFailed(String msg) {

    }

    @Override
    public void onStoreListResponse(StoreResponse storeResponse) {

    }

    @Override
    public void onPurposeListResponse(PurposeResponse purposeResponse) {

    }

    @Override
    public void onFloorListResponse(FloorResponse purposeResponse) {

    }

    @Override
    public void onMachineResponses(MachineResponses machineResponses) {

    }

    @Override
    public void onShiftResponses(ShiftResponses shiftResponses) {

    }

    @Override
    public void onLocationListResponse(FloorResponse purposeResponse) {

    }

    @Override
    public void onLineListResponse(LineResponse purposeResponse) {

    }

    @Override
    public void onReferenceListResponse(int type, ReferenceDataResponse referenceDataResponse) {

    }

    @Override
    public void finishFabricQrCodeTwoResponse(FinishFabricQrCodeResponses barcodeResponse) {

    }

    @Override
    public void finishFabricQrCodeBatchNoResponse(FinishFabricQrCodeResponses barcodeResponse) {

    }

    @Override
    public void finishFabricQrCodeBarCodeResponse(FinishFabricQrCodeResponses barcodeResponse) {

    }

    @Override
    public void defectInch(DefectInchModel defectInchModel) {

    }

    @Override
    public void issuePurpose(IssuePurposeModel issuePurposeModel) {
        mArrayIssuePurposeList=issuePurposeModel.getData();
        mIssuePurposeArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mArrayIssuePurposeList);
        mIssuePurposeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_purpose.setAdapter(mIssuePurposeArrayAdapter);
        for (IssuePurposeModel.Result issueStoreModels:mArrayIssuePurposeList){
            if (issueStoreModels.getPURPOSE_NAME().equals("Sewing Production")){
                spn_purpose.setSelection(mArrayIssuePurposeList.indexOf(issueStoreModels));
            }
        }
    }

    @Override
    public void issueStore(IssueStoreModel issueStoreModel) {
        mArrayIssueStoreList=issueStoreModel.getData();
        mIssueStoreArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mArrayIssueStoreList);
        mIssueStoreArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_store.setAdapter(mIssueStoreArrayAdapter);
    }

    @Override
    public void defectList(DefectListModel defectListModel) {

    }

    @Override
    public void fabricGrade(FabricGradeModel fabricGradeModel) {

    }

    @Override
    public void onLogged(String onLog) {
        insertedBy=onLog;
    }

    @Override
    public void onLocation(LocationModel locationModel) {

    }

    @Override
    public void onFabricShade(FabricShade fabricGrade) {

    }

    @Override
    public void onRepoEmptyViewRetryClick() {
        
    }

    @Override
    public void onItemDelete(int position) {

    }

    @Override
    public void onItemForwardClick(int position) {

    }
}