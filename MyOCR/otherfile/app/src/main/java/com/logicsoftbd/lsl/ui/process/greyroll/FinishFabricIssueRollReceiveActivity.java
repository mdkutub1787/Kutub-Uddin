package com.logicsoftbd.lsl.ui.process.greyroll;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

import com.google.gson.Gson;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.model.Process;
import com.logicsoftbd.lsl.data.network.model.DefectInchModel;
import com.logicsoftbd.lsl.data.network.model.DefectListModel;
import com.logicsoftbd.lsl.data.network.model.FabricGradeModel;
import com.logicsoftbd.lsl.data.network.model.FabricShade;
import com.logicsoftbd.lsl.data.network.model.FinishFabricQrCodeResponses;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRollReceive;
import com.logicsoftbd.lsl.data.network.model.FinishFabricRollReceiveRequest;
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

public class FinishFabricIssueRollReceiveActivity extends BaseActivity implements ReceiveMvpView, FinishFabricRollReceiveAdapter.Callback, DatePickerDialog.OnDateSetListener {

    public static final String EXTRA_RECEIVE_ID = "extra_bundle_issue_id";
    public static final String EXTRA_PROCESS_ID = "extra_bundle_process_id";
    public static final String EXTRA_CHALLAN = "extra_bundle_challan";
    @Inject
    ReceiveMvpPresenter<ReceiveMvpView, ReceiveMvpInteractor> mPresenter;

    @Inject
    FinishFabricRollReceiveAdapter mAdapter;

    @Inject
    LinearLayoutManager mLayoutManager;

    @BindView(R.id.tvDate)
    TextView tvDate;

    private Date mSelectedDate;

    public static Intent getStartIntent(Context context, Process model, FinishFabricRollReceive process,String challan) {
        Intent intent = new Intent(context, FinishFabricIssueRollReceiveActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_RECEIVE_ID, process);
        bundle.putSerializable(EXTRA_PROCESS_ID, model);
        bundle.putSerializable(EXTRA_CHALLAN, challan);
        intent.putExtras(bundle);
        return intent;
    }

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_grey_roll)
    RecyclerView mRecyclerView;
    @BindView(R.id.spn_location)
    Spinner spn_location;
    @BindView(R.id.spn_store)
    Spinner spn_store;
    ArrayList<LocationModel.Result> mArrayIssueLocationList = new ArrayList<>();
    ArrayList<IssueStoreModel.Result> mArrayIssueStoreList = new ArrayList<>();
    ArrayAdapter<LocationModel.Result> mIssueLocationArrayAdapter;
    ArrayAdapter<IssueStoreModel.Result> mIssueStoreArrayAdapter;

    FinishFabricRollReceiveRequest FinishFabricRollReceiveRequest = new FinishFabricRollReceiveRequest();

    private List<FinishFabricRollReceive.DetailsSet> productBarcodes = new ArrayList<>();

    private ArrayList<String> mHourList;
    private FinishFabricRollReceive mBarcodeResponse;
    private Process mProcess;
    private int mLocationId;
    private String mStoreId="0";
    private String mChallan;
    private String insertedBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_fabric_issue_roll_receive);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
        mAdapter.setCallback(this);
        setUp();
        invalidateOptionsMenu();
    }

    @Override
    protected void setUp() {
        mBarcodeResponse = (FinishFabricRollReceive) getIntent().getSerializableExtra(EXTRA_RECEIVE_ID);
        mChallan= (String) getIntent().getSerializableExtra(EXTRA_CHALLAN);
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
        mToolbar.setTitle("Finish Fabric Roll Receive by Store");
        mPresenter.getLocationList();

        Log.e("user", "id" + mPresenter.getUserId());
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        if (mBarcodeResponse.isFirst()) {
            productBarcodes = mBarcodeResponse.getData();

        }
        // setHourSpinner();
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        spn_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("sp_water", "" + mArrayIssueLocationList.get(position).getID());
                mLocationId = Integer.parseInt(mArrayIssueLocationList.get(position).getID());
                mPresenter.getRollReceiveStoreList(String.valueOf(mLocationId));
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


    @Override
    public void onRepoEmptyViewRetryClick() {

    }

    @Override
    public void onItemDelete(int position) {

    }

    @Override
    public void onItemForwardClick(int position) {

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
            extractFormData();
            mPresenter.onFinishFabricRollReceiveSave(FinishFabricRollReceiveRequest);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void extractFormData() {
        FinishFabricRollReceiveRequest.Result result = new FinishFabricRollReceiveRequest.Result();
        FinishFabricRollReceiveRequest.MasterPart masterPart = new FinishFabricRollReceiveRequest.MasterPart();
        masterPart.setCOMPANY_ID(1);
        masterPart.setCHALLAN_NO(mChallan);
        masterPart.setRECV_DATE(tvDate.getText().toString());
        masterPart.setSTORE_ID(mStoreId);
        masterPart.setLOCATION_ID(String.valueOf(mLocationId));
        masterPart.setINSERTED_BY(Integer.valueOf(insertedBy));
        ArrayList<String> barcodeString = new ArrayList<>();

        ArrayList<FinishFabricRollReceiveRequest.DetailsPart> finisList = new ArrayList<>();
        for (FinishFabricRollReceive.DetailsSet detailsSet : productBarcodes) {
            barcodeString.add(detailsSet.getPROD_ID());
            FinishFabricRollReceiveRequest.DetailsPart detailsPart = new FinishFabricRollReceiveRequest.DetailsPart();
            detailsPart.setBARCODE_NO(detailsSet.getBARCODE_NO());
            detailsPart.setBATCH_ID(detailsSet.getBATCH_ID());
            detailsPart.setBOOKING_NO(detailsSet.getBOOKING_NO());
            detailsPart.setBOOKING_WITHOUT_ORDER(detailsSet.getBOOKING_WITHOUT_ORDER());
            detailsPart.setCOMPANY_ID("1");
            detailsPart.setPROD_ID(detailsSet.getPROD_ID());
            detailsPart.setPROD_ID(detailsSet.getPROD_ID());
            detailsPart.setBODYPART_ID(detailsSet.getBODY_PART_ID());
            detailsPart.setCOLOR_ID(detailsSet.getCOLOR_ID());
            detailsPart.setCOLOR_NAME(detailsSet.getCOLOR_NAME());
            detailsPart.setPO_ID(detailsSet.getPO_ID());
            detailsPart.setITEM_CATEGORY("2");
            detailsPart.setTRANSACTION_TYPE("2");
            detailsPart.setDETERMINATION_ID(detailsSet.getDETERMINATION_ID());
            detailsPart.setCONS_QUANTITY(String.valueOf(detailsSet.getQC_PASS_QNTY()));
            detailsPart.setGSM(detailsSet.getGSM());
            detailsPart.setDIA(detailsSet.getDIA_WIDTH_TYPE());
            detailsPart.setROLL_ID(detailsSet.getROLL_ID());
            detailsPart.setROLL_NO(detailsSet.getROLL_NO());
            detailsPart.setCONS_QUANTITY(detailsSet.getQNTY());
            detailsPart.setCURRENT_WEIGHT(detailsSet.getQNTY());
            detailsPart.setREJECT_QNTY(detailsSet.getREJECT_QNTY());
            detailsPart.setGREY_RATE(detailsSet.getGREY_RATE());
            detailsPart.setDYEING_CHARGE(detailsSet.getDYEING_CHARGE());
            detailsPart.setREPROCESS(detailsSet.getREPROCESS());
            detailsPart.setPREV_REPROCESS(detailsSet.getPREV_REPROCESS());
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
        masterPart.setPRODUCT_IDS(sj.toString());
        result.setDetailsPart(finisList);
        result.setMasterPart(masterPart);
        FinishFabricRollReceiveRequest.setData(result);
        FinishFabricRollReceiveRequest.setStatus("true");
        Log.e("data", "data" + new Gson().toJson(FinishFabricRollReceiveRequest));
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

    }

    @Override
    public void issueStore(IssueStoreModel issueStoreModel) {
        mArrayIssueStoreList = issueStoreModel.getData();
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
        mArrayIssueLocationList = locationModel.getData();
        mIssueLocationArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mArrayIssueLocationList);
        mIssueLocationArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_location.setAdapter(mIssueLocationArrayAdapter);

    }

    @Override
    public void onFabricShade(FabricShade fabricGrade) {

    }
}