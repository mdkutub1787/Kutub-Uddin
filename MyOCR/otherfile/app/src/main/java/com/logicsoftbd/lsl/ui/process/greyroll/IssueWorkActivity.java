package com.logicsoftbd.lsl.ui.process.greyroll;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.model.Process;
import com.logicsoftbd.lsl.data.network.model.DefectInchModel;
import com.logicsoftbd.lsl.data.network.model.DefectListModel;
import com.logicsoftbd.lsl.data.network.model.EmbSpBarcodeResponse;
import com.logicsoftbd.lsl.data.network.model.EmbSpRequest;
import com.logicsoftbd.lsl.data.network.model.FabricGradeModel;
import com.logicsoftbd.lsl.data.network.model.FabricShade;
import com.logicsoftbd.lsl.data.network.model.FinishFabricQrCodeResponses;
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
import com.logicsoftbd.lsl.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IssueWorkActivity extends BaseActivity implements ReceiveMvpView, GmtsAdapter.Callback, DatePickerDialog.OnDateSetListener  {

    public static final String EXTRA_RECEIVE_ID = "extra_bundle_issue_work_id";
    public static final String EXTRA_PROCESS_ID = "extra_process_issue_work_id";
    private  static final int PICK_BARCODE_REQUEST = 1;

    @Inject
    ReceiveMvpPresenter<ReceiveMvpView, ReceiveMvpInteractor> mPresenter;

    @Inject
    GmtsAdapter mAdapter;

    @Inject
    LinearLayoutManager mLayoutManager;


    public static Intent getStartIntent(Context context, EmbSpBarcodeResponse barcodeResponse, Process process) {
        Intent intent = new Intent(context, IssueWorkActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_RECEIVE_ID, barcodeResponse);
        bundle.putSerializable(EXTRA_PROCESS_ID, process);
        intent.putExtras(bundle);
        return intent;
    }
    public static Intent getStartIntent(Context context, EmbSpBarcodeResponse barcodeResponse, Process process, boolean isFirst) {
        barcodeResponse.setFirst(isFirst);
        Intent intent = new Intent(context, IssueWorkActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_RECEIVE_ID, barcodeResponse);
        bundle.putSerializable(EXTRA_PROCESS_ID, process);
        intent.putExtras(bundle);
        return intent;
    }


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_grey_roll)
    RecyclerView mRecyclerView;

    @BindView(R.id.tvDate)
    TextView tvDate;

    @BindView(R.id.spinner_floor)
    Spinner mSpinnerFloor;

    @BindView(R.id.spinner_location)
    Spinner mSpinnerLocation;

    @BindView(R.id.spinner_emb_name)
    Spinner mSpinnerEmbName;

    @BindView(R.id.spinner_emb_type)
    Spinner mSpinnerEmbType;

    @BindView(R.id.spinner_body)
    Spinner mSpinnerBody;

   /* @BindView(R.id.rv_grey_roll)
    RecyclerView mRecyclerView;

    @BindView(R.id.text_view_1)
    TextView mTextView1;

    @BindView(R.id.text_view_2)
    TextView mTextView2;

   *//* @BindView(R.id.text_view_3)
    TextView mTextView3;*//*

    @BindView(R.id.spinner_issue_purpose)
    Spinner mSpinnerPurpose;*/

    private List<FloorResponse.Challan.MasterPart> mFloorList = new ArrayList<>();
    private List<FloorResponse.Challan.MasterPart> mLocationList = new ArrayList<>();
    private List<ReferenceDataResponse.Challan.MasterPart> mEmbNameList = new ArrayList<>();
    private List<ReferenceDataResponse.Challan.MasterPart> mEmbTypeList = new ArrayList<>();
    private List<ReferenceDataResponse.Challan.MasterPart> mBodyList = new ArrayList<>();
    private List<LineResponse.Challan.MasterPart> mLineList;

    private EmbSpRequest embSpRequest;

    private List<EmbSpBarcodeResponse.BodyPart.DetailsPart> productBarcodes = new ArrayList<>();


    private EmbSpBarcodeResponse mBarcodeResponse;
    private Process mProcess;

    private Date mSelectedDate;

    private int mSelectedEmbNameId = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_work);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
        mAdapter.setCallback(this);
        setUp();
    }

    @Override
    protected void setUp() {
        mBarcodeResponse = (EmbSpBarcodeResponse) getIntent().getSerializableExtra(EXTRA_RECEIVE_ID);
        mProcess = (Process) getIntent().getSerializableExtra(EXTRA_PROCESS_ID);
        // in case cost id is not sent
        if (mBarcodeResponse.isFirst() && mBarcodeResponse == null) {
            showMessage("Data not found!!");
            finish();
            return;
        }

        mToolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        mToolbar.setTitle(mProcess.getTitle());
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if(mBarcodeResponse.isFirst()) {
            productBarcodes.add(mBarcodeResponse.getData().getDetails());

           // mTextView1.setText(mBarcodeResponse.getData().getMasterPart().getKnittingCompany());
          //  mTextView2.setText(mBarcodeResponse.getData().getMasterPart().getKnittingSource());
           // mTextView3.setText(mBarcodeResponse.getData().getMasterPart().getKnittingSource());
           mPresenter.getLocation(1);






        ArrayList<String> embNames = new ArrayList<>();
        if(mProcess.getDataParam().getPageParam().equalsIgnoreCase("print")) {
            embNames.add("Printing");
            mSelectedEmbNameId = 1;
            mPresenter.getReferenceResponse(2);
        }  else if(mProcess.getDataParam().getPageParam().equalsIgnoreCase("embroidery")) {
            embNames.add("Embroidery");
            mSelectedEmbNameId = 2;
            mPresenter.getReferenceResponse(4);
        }  else if(mProcess.getDataParam().getPageParam().equalsIgnoreCase("special_work")) {
            mSelectedEmbNameId = 4;
            embNames.add("Special Works");
            mPresenter.getReferenceResponse(5);
        }
        ViewUtils.prepareSpinner(this, mSpinnerEmbName, embNames );


        }

        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSelectedDate = DateUtils.getToday();
        tvDate.setText(DateUtils.formatDate(mSelectedDate));
        mAdapter.addItems(productBarcodes);
        if (mProcess.getDataParam().getTypeParam().equalsIgnoreCase("issue")) {
            mAdapter.hasForward(false);
        }
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
                // pass the data onto the presenter
                mPresenter.onEmbSpIoSave(embSpRequest);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private boolean isValidForm() {
        if(productBarcodes == null || productBarcodes.size() == 0) {
            showAlertDialog("Please add barcode.");
            return false;
        }


        if(mSpinnerEmbType.getSelectedItemPosition() == 0) {
            showAlertDialog("Please select a Embel. Type.");
            return false;
        }
        if(mSpinnerFloor.getSelectedItemPosition() == 0) {
            showAlertDialog("Please select a floor.");
            return false;
        }
        if(mSpinnerBody.getSelectedItemPosition() == 0) {
            showAlertDialog("Please select a body part.");
            return false;
        }



        return true;
    }

    private void extractFormData() {
        EmbSpRequest.Result result = new EmbSpRequest.Result();
        EmbSpRequest.MasterPart masterPart = new EmbSpRequest.MasterPart();
        masterPart.setCompanyId(1);
        masterPart.setEmbelId(mSelectedEmbNameId);
       // masterPart.setEmbNameId(mSelectedEmbNameId);
        masterPart.setEmbTypeId(mEmbTypeList.get(mSpinnerEmbType.getSelectedItemPosition()-1).getId());
        masterPart.setFloorId(mFloorList.get(mSpinnerFloor.getSelectedItemPosition()-1).getId());
        masterPart.setBodyPartId(mBodyList.get(mSpinnerBody.getSelectedItemPosition()-1).getId());
        masterPart.setLocationId(mLocationList.get(mSpinnerLocation.getSelectedItemPosition()-1).getId());
        masterPart.setDeliveryDate(tvDate.getText().toString());
        masterPart.setProductionType(mBarcodeResponse.getData().getDetails().getProductionType());
        result.setMasterPart(masterPart);
        result.setDetailsPart(productBarcodes);
        embSpRequest = new EmbSpRequest();
        embSpRequest.setData(result);
        embSpRequest.setStatus("true");

    }

    @OnClick(R.id.fab)
    void onFabClick () {

        startActivityForResult(ScannerActivity.getStartIntent(this, mProcess, true), PICK_BARCODE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_BARCODE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                EmbSpBarcodeResponse mBarcodeResponse = (EmbSpBarcodeResponse) data.getSerializableExtra(EXTRA_RECEIVE_ID);
                if (!hasItem(mBarcodeResponse.getData())) {
                    productBarcodes.add(mBarcodeResponse.getData().getDetails());
                } else {
                    showMessage("Item already on the list");
                }
            }
        }
    }

    @Override
    public void onRepoEmptyViewRetryClick() {

    }

    @Override
    public void onItemDelete(final int position) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        try {
                            productBarcodes.remove(position);
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                        mAdapter.addItems(productBarcodes);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to Remove this Barcode?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }

  /*  @Override
    public void onSuccess() {
        showMessage("Item Saved Successfully");
        finish();
    }*/

    private boolean hasItem(EmbSpBarcodeResponse.BodyPart productBarcode) {
        if(productBarcodes != null && productBarcodes.size() > 0) {
            for (EmbSpBarcodeResponse.BodyPart.DetailsPart barcode: productBarcodes) {
                if(barcode.getBarcodeNo().equalsIgnoreCase(productBarcode.getDetails().getBarcodeNo())) {
                    return true;
                }
            }
        }

        return false;
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
    public void onPurposeListResponse(PurposeResponse purposeResponse) {


        //ViewUtils.prepareSpinner(this, mSpinnerPurpose, storeNames );
    }

    @Override
    public void onFloorListResponse(FloorResponse purposeResponse) {
        mFloorList = purposeResponse.getData().getMasterPart();

        ArrayList<String> storeNames = new ArrayList<>();
        storeNames.add("--Select--");
        if(mFloorList != null && mFloorList.size() > 0) {
            for(FloorResponse.Challan.MasterPart masterPart : mFloorList) {
                storeNames.add(masterPart.getFloorName());
            }
        }

        ViewUtils.prepareSpinner(this, mSpinnerFloor, storeNames );


    }

    @Override
    public void onMachineResponses(MachineResponses machineResponses) {

    }

    @Override
    public void onShiftResponses(ShiftResponses shiftResponses) {

    }

    @Override
    public void onLocationListResponse(FloorResponse purposeResponse) {

        mLocationList = purposeResponse.getData().getMasterPart();

        ArrayList<String> storeNames = new ArrayList<>();
        storeNames.add("--Select--");
        if(mLocationList != null && mLocationList.size() > 0) {
            for(FloorResponse.Challan.MasterPart masterPart : mLocationList) {
                storeNames.add(masterPart.getLocationName());
            }
        }

        ViewUtils.prepareSpinner(this, mSpinnerLocation, storeNames );

        mSpinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               mSpinnerFloor.setAdapter(null);
                if(i > 0) {
                    mPresenter.getFloorResponse(mLocationList.get(mSpinnerLocation.getSelectedItemPosition()-1).getId(), mProcess.getDataParam().getProductionProcess());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onLineListResponse(LineResponse purposeResponse) {
    }

    @Override
    public void onReferenceListResponse(int type, ReferenceDataResponse referenceDataResponse) {
        if(type == 2 || type == 4|| type == 5) {
            mEmbTypeList = referenceDataResponse.getData().getMasterPart();

            ArrayList<String> embTypes = new ArrayList<>();
            embTypes.add("--Select--");
            if(mEmbTypeList != null && mEmbTypeList.size() > 0) {
                for(ReferenceDataResponse.Challan.MasterPart masterPart : mEmbTypeList) {
                    embTypes.add(masterPart.getType());
                }
            }

            ViewUtils.prepareSpinner(this, mSpinnerEmbType, embTypes );
            mPresenter.getReferenceResponse(3);
        }
        if(type == 3) {
            mBodyList = referenceDataResponse.getData().getMasterPart();

            ArrayList<String> bodies = new ArrayList<>();
            bodies.add("--Select--");
            if(mBodyList != null && mBodyList.size() > 0) {
                for(ReferenceDataResponse.Challan.MasterPart masterPart : mBodyList) {
                    bodies.add(masterPart.getBodyPart());
                }
            }

            ViewUtils.prepareSpinner(this, mSpinnerBody, bodies );
        }
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

    }

    @Override
    public void defectList(DefectListModel defectListModel) {

    }

    @Override
    public void fabricGrade(FabricGradeModel fabricGradeModel) {

    }

    @Override
    public void onLogged(String onLog) {

    }

    @Override
    public void onLocation(LocationModel locationModel) {

    }

    @Override
    public void onFabricShade(FabricShade fabricGrade) {

    }

    @Override
    public void onStoreListResponse(StoreResponse storeResponse) {

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
    public void onItemForwardClick(int position) {
       // SewingDialog.newInstance(this, productBarcodes.get(position)).show(getSupportFragmentManager());
    }
}
